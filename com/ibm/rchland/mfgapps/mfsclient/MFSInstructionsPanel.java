/* © Copyright IBM Corporation 2006, 2016. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2007-03-12      34242JR  R Prechel        -Initial version
 *                                           -Fixes GUI layout issues PTR34177JR
 * 2007-04-09   ~1 38176JM  R Prechel        -Fix MFS Client hang
 *                                           -Delete run method, do not implement Runnable
 * 2009-06-03   ~2 43813MZ  Christopher O    -Call loadDataCollect function in loadListModel Function
 * 											  if collect data is required
 * 2013-01-23	~3 RCQ00228377 Andy Williams - Changed modifyInstr to modifyInstruction
 * 2016-02-18	~4 1471226  Miguel Rivas     -MFSPanel(parent, source, screenName, layout) deprecated
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient;

// GUI layout
//  _______________________________________
// |              | Header Rows            |
// | pnlButtons   | in header panel        |
// |              |________________________|
// |              |tpInsParts in spInsParts|
// |______________|________________________|
// |                                       |
// |      pnlRowHolder in spPartsInst      |
// |                                       |
// |      pnlRowHolder contains the        |
// |      MFSPartInstructionJPanels        |
// |_______________________________________|
// |         ActionIndicator               |
// |_______________________________________|

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import mfsxml.MISSING_XML_TAG_EXCEPTION;
import mfsxml.MfsXMLParser;

import com.ibm.rchland.mfgapps.client.utils.messagebox.IGSMessageBox;
import com.ibm.rchland.mfgapps.mfsclient.panel.MFSDeconfigPanel;
import com.ibm.rchland.mfgapps.mfsclient.panel.MFSDirectWorkPanel;
import com.ibm.rchland.mfgapps.mfsclient.panel.MFSViewOpsPanel;
import com.ibm.rchland.mfgapps.mfsclient.struct.MFSComponentListModel;
import com.ibm.rchland.mfgapps.mfsclient.struct.MFSComponentRec;
import com.ibm.rchland.mfgapps.mfsclient.struct.MFSHeaderRec;
import com.ibm.rchland.mfgapps.mfsclient.struct.MFSInstructionRec;
import com.ibm.rchland.mfgapps.mfsclient.struct.MFSSubInstructionRec;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSConstants;
import com.ibm.rchland.mfgapps.mfscommon.MFSConfig;

/**
 * <code>MFSInstructionsPanel</code> is an abstract subclass of
 * <code>MFSPanel</code> that is the base class for panels used to display
 * {@link MFSPartInstructionJPanel}s.
 * @author The MFS Client Development Team
 */
@SuppressWarnings("serial")
public abstract class MFSInstructionsPanel
	extends MFSPanel
	implements ListSelectionListener, PropertyChangeListener
{
	/** The status message for the {@link #setupPartInstPanel()} method. */
	protected final String SETUP_PART_INST_MSG = "Preparing Instructions for Display, Please Wait...";

	/** The status message for the {@link #loadInstructions} method. */
	protected final String LOADING_INST_MSG = "Loading Instructions, Please Wait...";

	/**
	 * The <code>MFSComponentListModel</code> that contains all of the
	 * <code>MFSComponentRec</code>s for the work unit.
	 */
	protected MFSComponentListModel fieldComponentListModel = new MFSComponentListModel();

	/** The index of the active row. */
	protected int fieldActiveRow = 0;

	/** The index of the blue row. */
	protected int fieldBlueRow = 0;

	/** The <code>Vector</code> of {@link MFSInstructionRec}s. */
	protected Vector<MFSInstructionRec> fieldInstVector = new Vector<MFSInstructionRec>();

	/** The <code>Vector</code> of {@link MFSComponentListModel}s. */
	protected Vector <MFSComponentListModel>fieldLmVector = new Vector<MFSComponentListModel>();

	/** The <code>Vector</code> of {@link MFSPartInstructionJPanel}s. */
	protected Vector <MFSPartInstructionJPanel>fieldRowVector = new Vector<MFSPartInstructionJPanel>();

	/** The current {@link MFSComponentRec}. */
	protected MFSComponentRec fieldCurrentCompRec = null;

	/** The {@link MFSHeaderRec} for the work unit. */
	protected MFSHeaderRec fieldHeaderRec = new MFSHeaderRec();

	/** The <code>JPanel</code> containing the menu buttons. */
	protected final JPanel pnlButtons = new JPanel(null);

	/** The installed parts <code>JTextPane</code>. */
	protected final JTextPane tpInstalledParts = new JTextPane();

	/** The <code>JScrollPane</code> for {@link #tpInstalledParts}. */
	protected final JScrollPane spInstalledParts = new JScrollPane(this.tpInstalledParts,
			ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
			ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

	/** Holds the <code>MFSPartInstructionJPanel</code>s. */
	protected final MFSPIPHolderJPanel pnlRowHolder = new MFSPIPHolderJPanel();

	/**
	 * The <code>JScrollPane</code> used to display the
	 * <code>MFSPartInstructionJPanel</code>s.
	 */
	protected final JScrollPane spPartsInst = new JScrollPane(this.pnlRowHolder,
			ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
			ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

	//Header row 1 labels
	/** The user ("User:") <code>JLabel</code>. */
	protected final JLabel lblUser = createHeaderLabel();

	/** The cntr ("Cntr:") <code>JLabel</code>. */
	protected final JLabel lblCntr = createHeaderLabel();

	/** The mctl ("Mctl:") <code>JLabel</code>. */
	protected final JLabel lblMctl = createHeaderLabel();

	//Extra header row if not MFSViewOpsPanel
	/** The sapo ("P Order:") <code>JLabel</code>. */
	protected final JLabel lblSapo = createHeaderLabel();

	/** The saps ("S Order:") <code>JLabel</code>. */
	protected final JLabel lblSaps = createHeaderLabel();

	/** The count ("COUNT:") <code>JLabel</code>. */
	protected final JLabel lblCount = createHeaderLabel();

	//Header row 2 labels
	/** The mfgn ("Sys Num:") name <code>JLabel</code>. */
	protected final JLabel nlSysNum = createHeaderNameLabel("Sys Num: ");

	/** The mfgn ("Sys Num:") value <code>JLabel</code>. */
	protected final JLabel vlMfgn = createHeaderValueLabel(this.nlSysNum);

	/** The orno ("Order:") name <code>JLabel</code>. */
	protected final JLabel nlOrder = createHeaderNameLabel("Order: ");

	/** The orno ("Order:") value <code>JLabel</code>. */
	protected final JLabel vlOrno = createHeaderValueLabel(this.nlOrder);

	/** The loc ("LOC:") name <code>JLabel</code>. */
	protected final JLabel nlLoc = createHeaderNameLabel("");

	/** The loc ("LOC:") value <code>JLabel</code>. */
	protected final JLabel vlLoc = createHeaderValueLabel(this.nlLoc);

	//Header row 3 labels
	/** The ctry ("Country:") name <code>JLabel</code>. */
	protected final JLabel nlCountry = createHeaderNameLabel("Country: ");

	/** The ctry ("Country:") value <code>JLabel</code>. */
	protected final JLabel vlCtry = createHeaderValueLabel(this.nlCountry);

	/** The brof ("Brof:") name <code>JLabel</code>. */
	protected final JLabel nlBrof = createHeaderNameLabel("Brof: ");

	/** The brof ("Brof:") value <code>JLabel</code>. */
	protected final JLabel vlBrof = createHeaderValueLabel(this.nlBrof);

	/** The prod ("Prod ID:") name <code>JLabel</code>. */
	protected final JLabel nlProdID = createHeaderNameLabel("Prod ID: ");

	/** The prod ("Prod ID:") value <code>JLabel</code>. */
	protected final JLabel vlProd = createHeaderValueLabel(this.nlProdID);

	//Header row 4 labels
	/** The mspi_mcsn ("Mach Num:") name <code>JLabel</code>. */
	protected final JLabel nlMachNum = createHeaderNameLabel("Mach Num: ");

	/** The mspi_mcsn ("Mach Num:") value <code>JLabel</code>. */
	protected final JLabel vlMspiMcsn = createHeaderValueLabel(this.nlMachNum);

	/** The mmdl ("Model:") name <code>JLabel</code>. */
	protected final JLabel nlModel = createHeaderNameLabel("Model: ");

	/** The mmdl ("Model:") value <code>JLabel</code>. */
	protected final JLabel vlMmdl = createHeaderValueLabel(this.nlModel);

	/** The nmbr ("Oper:") name <code>JLabel</code>. */
	protected final JLabel nlOper = createHeaderNameLabel("Oper: ");

	/** The nmbr ("Oper:") value <code>JLabel</code>. */
	protected final JLabel vlNmbr = createHeaderValueLabel(this.nlOper);

	/** The schd ("Schd:") name <code>JLabel</code>. */
	protected final JLabel nlShipDate = createHeaderNameLabel("Schd: ");

	/** The schd ("Schd:") value <code>JLabel</code>. */
	protected final JLabel vlSchd = createHeaderValueLabel(this.nlShipDate);

	//Header row 5 labels
	/** The early build <code>JLabel</code>. */
	protected final JLabel lblUnpr = createHeaderLabel();

	/** The recon <code>JLabel</code>. */
	protected final JLabel lblRecon = createHeaderLabel();

	/** The hot <code>JLabel</code>. */
	protected final JLabel lblHot = createHeaderLabel();

	/** The system type <code>JLabel</code>. */
	protected final JLabel lblType = createHeaderLabel();

	/** The LU <code>JLabel</code>. */
	protected final JLabel lblLupn = createHeaderLabel();

	/** The tower count <code>JLabel</code>. */
	protected final JLabel lblTwrCnt = createHeaderLabel();

	/**
	 * Constructs a new <code>MFSInstructionsPanel</code>.
	 * @param parent the <code>MFSFrame</code> used to display this panel
	 * @param source the <code>MFSPanel</code> that caused this panel to be displayed
	 * @param screenName the screen name of this panel
	 */
	@SuppressWarnings("deprecation")
	public MFSInstructionsPanel(MFSFrame parent, MFSPanel source, String screenName)
	{
		super(parent, source, screenName, "", new GridBagLayout()); //~4C

		this.pnlButtons.setBackground(MFSConstants.PRIMARY_BACKGROUND_COLOR);

		JPanel buttonPanelHolder = new JPanel(new FlowLayout());
		buttonPanelHolder.setBorder(BorderFactory.createLineBorder(MFSConstants.PRIMARY_FOREGROUND_COLOR));
		buttonPanelHolder.setBackground(MFSConstants.PRIMARY_BACKGROUND_COLOR);
		buttonPanelHolder.add(this.pnlButtons);

		JPanel headerPanel = setupHeaderPanel();

		this.tpInstalledParts.setFont(MFSConstants.MEDIUM_MONOSPACED_FONT);
		this.tpInstalledParts.setEditable(false);
		this.tpInstalledParts.setRequestFocusEnabled(false);

		this.spInstalledParts.setRequestFocusEnabled(false);
		this.spInstalledParts.setMinimumSize(new Dimension(540, 65));
		this.spInstalledParts.setPreferredSize(new Dimension(540, 65));

		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 2, 0.0, 0.0,
				GridBagConstraints.NORTH, GridBagConstraints.BOTH,
				new Insets(1, 1, 1, 1), 0, 0);

		add(buttonPanelHolder, gbc);

		gbc.gridx = 1;
		gbc.gridheight = 1;
		gbc.weightx = 1.0;
		gbc.fill = GridBagConstraints.HORIZONTAL;

		add(headerPanel, gbc);

		gbc.gridy = 1;
		gbc.anchor = GridBagConstraints.SOUTH;

		add(this.spInstalledParts, gbc);

		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridwidth = 2;
		gbc.weighty = 1.0;
		gbc.anchor = GridBagConstraints.NORTH;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(4, 4, 4, 4);

		add(this.spPartsInst, gbc);
	}

	/**
	 * Creates a header <code>JLabel</code>.
	 * @return the new header <code>JLabel</code>
	 */
	protected JLabel createHeaderLabel()
	{
		JLabel label = new JLabel(" ", null, SwingConstants.CENTER); //$NON-NLS-1$
		label.setFont(MFSConstants.SMALL_MONOSPACED_FONT);
		label.setForeground(MFSConstants.PRIMARY_FOREGROUND_COLOR);
		return label;
	}

	/**
	 * Creates a header name <code>JLabel</code> with the specified text.
	 * @param text the text displayed by the header name <code>JLabel</code>
	 * @return the new header name <code>JLabel</code>
	 */
	private JLabel createHeaderNameLabel(String text)
	{
		JLabel result = new JLabel(text, null, SwingConstants.RIGHT);
		result.setFont(MFSConstants.SMALL_MONOSPACED_FONT);
		result.setForeground(MFSConstants.PRIMARY_FOREGROUND_COLOR);
		return result;
	}

	/**
	 * Creates a header value <code>JLabel</code>.
	 * @param nameLabel the name <code>JLabel</code> for this value
	 *        <code>JLabel</code>
	 * @return the new header value <code>JLabel</code>
	 */
	private JLabel createHeaderValueLabel(JLabel nameLabel)
	{
		JLabel result = new JLabel("", null, SwingConstants.LEFT); //$NON-NLS-1$
		result.setFont(MFSConstants.MEDIUM_MONOSPACED_FONT);
		result.setForeground(MFSConstants.PRIMARY_FOREGROUND_COLOR);
		nameLabel.setLabelFor(result);
		return result;
	}

	/**
	 * Creates a header line <code>JPanel</code> that uses a
	 * <code>BoxLayout</code> to lay out components.
	 * @return the new header line <code>JPanel</code>
	 */
	private JPanel createHeaderLineBoxPanel()
	{
		JPanel result = new JPanel(null);
		result.setLayout(new BoxLayout(result, BoxLayout.X_AXIS));
		result.setBackground(MFSConstants.PRIMARY_BACKGROUND_COLOR);
		return result;
	}

	/**
	 * Creates a header line <code>JPanel</code> that uses a
	 * <code>GridLayout</code> to lay out components.
	 * @return the new header line <code>JPanel</code>
	 */
	private JPanel createHeaderLineGridPanel()
	{
		JPanel result = new JPanel(new GridLayout(1, 0));
		result.setBackground(MFSConstants.PRIMARY_BACKGROUND_COLOR);
		return result;
	}

	/**
	 * Creates a <code>GridBagConstraints</code> that can be used to add an
	 * <code>MFSPartInstructionJPanel</code> to {@link #spPartsInst}.
	 * @param last <code>true</code> iff the next
	 *        <code>MFSPartInstructionJPanel</code> is the last
	 *        <code>MFSPartInstructionJPanel</code>
	 * @param yPos the y position of the <code>MFSPartInstructionJPanel</code>
	 * @return the <code>GridBagConstraints</code>
	 */
	protected GridBagConstraints createPipConstraints(boolean last, int yPos)
	{
		GridBagConstraints result = new GridBagConstraints();
		result.gridx = 0;
		result.gridy = yPos;
		result.fill = GridBagConstraints.BOTH;
		result.anchor = GridBagConstraints.NORTH;
		result.weightx = 1.0;

		//If it is the last row, set weighty to 1 so
		//it can extend vertically to the bottom of the screen.
		//This usually does not matter because the rows cover
		//more than one screen full.
		if (last)
		{
			result.weighty = 1.0;
		}
		else
		{
			result.weighty = 0.0;
		}
		return result;
	}

	/** Disables {@link MFSPIPHolderJPanel#scrollRectToVisible(java.awt.Rectangle)}. */
	public void disableInstructionScrollRectToVisible()
	{
		this.pnlRowHolder.disableScrollRectToVisible();
	}

	/** Enables {@link MFSPIPHolderJPanel#scrollRectToVisible(java.awt.Rectangle)}. */
	public void enableInstructionScrollRectToVisible()
	{
		EventQueue.invokeLater(this.pnlRowHolder);
	}

	/**
	 * Returns the <code>MFSComponentRec</code> at the specified index.
	 * @param index an index into {@link #fieldComponentListModel}
	 * @return the <code>MFSComponentRec</code> at the specified index
	 * @throws ArrayIndexOutOfBoundsException if the <code>index</code> is
	 *         negative or not less than the current size of the list model
	 * @throws ClassCastException if the element at <code>index</code> is not
	 *         an <code>MFSComponentRec</code>
	 */
	public MFSComponentRec getComponentListModelCompRecAt(int index)
	{
		return this.fieldComponentListModel.getComponentRecAt(index);
	}

	/**
	 * Returns the size of the <code>MFSComponentListModel</code>.
	 * @return the size of the <code>MFSComponentListModel</code>
	 */
	public int getComponentListModelSize()
	{
		return this.fieldComponentListModel.size();
	}

	/**
	 * Returns the MCTL (Work Unit Control Number) for the current work unit.
	 * @return the MCTL for the current work unit
	 */
	public String getCurrMctl()
	{
		return this.fieldHeaderRec.getMctl();
	}

	/**
	 * Returns the XML value returned by {@link MfsXMLParser#getField(String)}
	 * or an empty <code>String</code> if a
	 * <code>MISSING_XML_TAG_EXCEPTION</code> is thrown.
	 * @param fieldName the name of the XML field
	 * @param parser the <code>MfsXMLParser</code>
	 * @return the XML field value or blank
	 */
	private String getFieldOrEmptyString(String fieldName, MfsXMLParser parser)
	{
		try
		{
			return parser.getField(fieldName);
		}
		catch (mfsxml.MISSING_XML_TAG_EXCEPTION e)
		{
			return ""; //$NON-NLS-1$
		}
	}

	/**
	 * Returns the <code>MFSHeaderRec</code>.
	 * @return the <code>MFSHeaderRec</code>
	 */
	public MFSHeaderRec getHeaderRec()
	{
		return this.fieldHeaderRec;
	}

	/**
	 * Returns the <code>MFSInstructionRec</code> at the specified index.
	 * @param index an index into {@link #fieldInstVector}
	 * @return the <code>MFSInstructionRec</code> at the specified index
	 * @throws ArrayIndexOutOfBoundsException if the <code>index</code> is
	 *         negative or not less than the current size of the vector
	 * @throws ClassCastException if the element at <code>index</code> is not
	 *         an <code>MFSInstructionRec</code>
	 */
	public MFSInstructionRec getInstVectorElementAt(int index)
	{
		return this.fieldInstVector.elementAt(index);
	}

	/**
	 * Returns the <code>MFSComponentListModel</code> at the specified index.
	 * @param index an index into {@link #fieldLmVector}
	 * @return the <code>MFSComponentListModel</code> at the specified index
	 * @throws ArrayIndexOutOfBoundsException if the <code>index</code> is
	 *         negative or not less than the current size of the vector
	 * @throws ClassCastException if the element at <code>index</code> is not
	 *         an <code>MFSComponentListModel</code>
	 */
	public MFSComponentListModel getLmVectorElementAt(int index)
	{
		return (MFSComponentListModel) this.fieldLmVector.elementAt(index);
	}

	/**
	 * Returns the size of {@link #fieldLmVector}.
	 * @return the size of {@link #fieldLmVector}
	 */
	public int getLMVectorSize()
	{
		return this.fieldLmVector.size();
	}

	/**
	 * Returns the <code>MFSPartInstructionJPanel</code> at the specified index.
	 * @param index an index into {@link #fieldRowVector}
	 * @return the <code>MFSPartInstructionJPanel</code> at the specified index
	 * @throws ArrayIndexOutOfBoundsException if the <code>index</code> is
	 *         negative or not less than the current size of the vector
	 * @throws ClassCastException if the element at <code>index</code> is not
	 *         an <code>MFSPartInstructionJPanel</code>
	 */
	public MFSPartInstructionJPanel getRowVectorElementAt(int index)
	{
		return (MFSPartInstructionJPanel) this.fieldRowVector.elementAt(index);
	}

	/** {@inheritDoc} */
	public String getMFSPanelTitle()
	{
		return getScreenName()
				+ " - Move thru Parts: Arrow Up/Down - Move thru Instructions: Page Up/Down";
	}

	/** Initializes the text of most work unit header labels. */
	protected final void initHeaderLabels()
	{
		final MFSHeaderRec headerRec = this.fieldHeaderRec;

		this.vlMfgn.setText(headerRec.getMfgn());
		this.vlOrno.setText(headerRec.getOrno());
		this.vlCtry.setText(headerRec.getCtry());
		this.vlBrof.setText(headerRec.getBrof());
		this.vlProd.setText(headerRec.getProd());
		this.vlMspiMcsn.setText(headerRec.getMspi() + headerRec.getMcsn());
		this.vlMmdl.setText(headerRec.getMmdl());
		this.vlNmbr.setText(headerRec.getNmbr());
		this.vlSchd.setText(headerRec.getSchd());

		/* Early Build */
		if (headerRec.getUnpr().equals("1")) //$NON-NLS-1$
		{
			this.lblUnpr.setText("ERLY BLD");
		}
		else
		{
			this.lblUnpr.setText(""); //$NON-NLS-1$
		}

		/* Recon */
		if (headerRec.getRcon().equals("1")) //$NON-NLS-1$
		{
			this.lblRecon.setText("RECON");
		}
		else
		{
			this.lblRecon.setText(""); //$NON-NLS-1$
		}

		/* Hot */
		final String emri = headerRec.getEmri();
		if (emri.equals("0") //$NON-NLS-1$
				|| emri.equals("1") //$NON-NLS-1$ 
				|| emri.equals("2") //$NON-NLS-1$ 
				|| emri.equals("3")) //$NON-NLS-1$
		{
			this.lblHot.setText("HOT");
			this.lblHot.setBackground(Color.red);
		}
		else
		{
			this.lblHot.setText(""); //$NON-NLS-1$
			this.lblHot.setBackground(MFSConstants.PRIMARY_BACKGROUND_COLOR);
		}

		/* Typz */
		final String typz = headerRec.getTypz();
		if (typz.equals("1")) //$NON-NLS-1$
		{
			this.lblType.setText("WT MES");
		}
		else if (typz.equals("2")) //$NON-NLS-1$
		{
			this.lblType.setText("DOM MES");
		}
		else if (typz.equals("3")) //$NON-NLS-1$
		{
			this.lblType.setText("WT SYS");
		}
		else if (typz.equals("4")) //$NON-NLS-1$
		{
			this.lblType.setText("DOM SYS");
		}
		else if (typz.equals("5")) //$NON-NLS-1$
		{
			this.lblType.setText("DSS");
		}
		else if (typz.equals("6")) //$NON-NLS-1$
		{
			this.lblType.setText("IPR");
		}
		else
		{
			this.lblType.setText(""); //$NON-NLS-1$
		}

		/* LU */
		if (headerRec.getLupn_flag())
		{
			this.lblLupn.setText("LU");
		}
		else
		{
			this.lblLupn.setText(""); //$NON-NLS-1$
		}

		/* Tower Count */
		this.lblTwrCnt.setText(headerRec.getWunm() + " / " + headerRec.getTwrc()); //$NON-NLS-1$
	}

	/**
	 * Loads the this panel's {@link MFSComponentListModel}.
	 * @param data the data used to load the <code>MFSComponentListModel</code>
	 * @return this panel's <code>MFSHeaderRec</code>, which is loaded with
	 *         data when the <code>MFSComponentListModel</code> is loaded
	 */
	public final MFSHeaderRec loadListModel(String data)
	{
		this.fieldHeaderRec = new MFSHeaderRec();
		this.fieldComponentListModel = new MFSComponentListModel();
		this.fieldComponentListModel.loadListModel(data, this.fieldHeaderRec);
		//Check if data Collection is required for this MCTL
		if(this.fieldHeaderRec.isCollectRequired())  								//~2
		{																			//~2
			//Retrieve the data Collection from W_PARTCOL Program and store in MFSHeaderRec
			this.fieldComponentListModel.loadDataCollection(this.fieldHeaderRec,fieldHeaderRec.getMctl(),this.getParentFrame());//~2		
		}																			//~2
		return this.fieldHeaderRec;
	}

	/**
	 * Loads the instruction with information from the <code>MfsXMLParser</code>.
	 * Note: The <code>MfsXMLParser</code> should contain a single SQ record
	 * and an SQ record consists of 1 to many SS records.
	 * @param curInst the <code>MFSInstructionRec</code> to load
	 * @param curSqXML the <code>MfsXMLParser</code>
	 * @param path the IR Path
	 * @param rIncfAllowed <code>true</code> if a value of R is allowed in the
	 *        SS record data; <code>false</code> otherwise
	 * @throws MISSING_XML_TAG_EXCEPTION as thrown by
	 *         {@link MfsXMLParser#getField(String)}
	 */
	public void loadInstruction(MFSInstructionRec curInst, MfsXMLParser curSqXML,
								String path, boolean rIncfAllowed)
		throws MISSING_XML_TAG_EXCEPTION
	{
		//set up the suffix seq for the instruction
		curInst.setSuff(curSqXML.getField("INSX")); //$NON-NLS-1$
		curInst.setIseq(curSqXML.getField("NMSQ")); //$NON-NLS-1$

		curInst.setChanged(false);
		curInst.setCompletionStatus("0"); //$NON-NLS-1$

		String curSSRec = curSqXML.getField("SS"); //$NON-NLS-1$
		while (curSSRec.length() != 0)
		{
			MFSSubInstructionRec curSubInst = new MFSSubInstructionRec();
			MfsXMLParser curSSXML = new MfsXMLParser(curSSRec);

			//M's trump all. so if we have a mix and match of INCF values
			//in our instruction, we'll give this priority, 'M'->'A'->'R'->' '
			//unknown values will be treated a blanks
			final String incf = curSSXML.getField("INCF"); //$NON-NLS-1$
			if (incf.equals("M")) //$NON-NLS-1$
			{
				curInst.setInstrClass("M"); //$NON-NLS-1$
			}
			else if (incf.equals("A") && //$NON-NLS-1$ 
					!curInst.getInstrClass().equals("M")) //$NON-NLS-1$
			{
				curInst.setInstrClass("A"); //$NON-NLS-1$
			}
			else if (rIncfAllowed && incf.equals("R") //$NON-NLS-1$
					&& !curInst.getInstrClass().equals("M") //$NON-NLS-1$
					&& !curInst.getInstrClass().equals("A")) //$NON-NLS-1$
			{
				curInst.setInstrClass("R"); //$NON-NLS-1$
			}
			else if (!curInst.getInstrClass().equals("M") //$NON-NLS-1$
					&& !curInst.getInstrClass().equals("A") //$NON-NLS-1$
					&& (!rIncfAllowed || !curInst.getInstrClass().equals("R"))) //$NON-NLS-1$
			{
				curInst.setInstrClass(" "); //$NON-NLS-1$
			}

			//Suspend overrides all - Blank Overrides 'C'omplete - 'C'
			// overrides other values only
			if (curSSXML.getField("STAT").equals("S")) //$NON-NLS-1$ //$NON-NLS-2$
			{
				curInst.setCompletionStatus("S"); //$NON-NLS-1$
				curInst.setCompletionStatusOriginal("S"); //$NON-NLS-1$
			}
			else if (curSSXML.getField("STAT").equals(" ") //$NON-NLS-1$ //$NON-NLS-2$
					&& !curInst.getCompletionStatus().equals("S")) //$NON-NLS-1$
			{
				curInst.setCompletionStatus(" "); //$NON-NLS-1$
				curInst.setCompletionStatusOriginal(" "); //$NON-NLS-1$
			}
			else if (curSSXML.getField("STAT").equals("C") //$NON-NLS-1$ //$NON-NLS-2$
					&& !curInst.getCompletionStatus().equals(" ") //$NON-NLS-1$
					&& !curInst.getCompletionStatus().equals("S")) //$NON-NLS-1$
			{
				curInst.setCompletionStatus("C"); //$NON-NLS-1$
				curInst.setCompletionStatusOriginal("C"); //$NON-NLS-1$
			}
			else
			{
				curInst.setCompletionStatus(curSSXML.getField("STAT")); //$NON-NLS-1$
				curInst.setCompletionStatusOriginal(curSSXML.getField("STAT")); //$NON-NLS-1$
			}

			//fill in drawing properties
			try
			{
				curSubInst.setDrawing(path + curSSXML.getField("RSPC").trim()); //$NON-NLS-1$
			}
			catch (mfsxml.MISSING_XML_TAG_EXCEPTION e)
			{
				curSubInst.setDrawing(""); //$NON-NLS-1$
			}

			curSubInst.setExplodeDrawing(getFieldOrEmptyString("RDSP", curSSXML).trim()); //$NON-NLS-1$	
			curSubInst.setSizeDrawing(getFieldOrEmptyString("RVIS", curSSXML).trim()); //$NON-NLS-1$
			curSubInst.setSheet(getFieldOrEmptyString("SHET", curSSXML)); //$NON-NLS-1$
			curSubInst.setZone(getFieldOrEmptyString("ZONE", curSSXML)); //$NON-NLS-1$	

			// set media 1 properties
			try
			{
				curSubInst.setMedia1(path + curSSXML.getField("SPC1").trim()); //$NON-NLS-1$
			}
			catch (mfsxml.MISSING_XML_TAG_EXCEPTION e)
			{
				curSubInst.setMedia1(""); //$NON-NLS-1$
			}

			curSubInst.setExplodeMedia1(getFieldOrEmptyString("DSP1", curSSXML).trim()); //$NON-NLS-1$
			curSubInst.setSizeMedia1(getFieldOrEmptyString("VIS1", curSSXML).trim()); //$NON-NLS-1$

			// set media 2 properties
			try
			{
				curSubInst.setMedia2(path + curSSXML.getField("SPC2").trim()); //$NON-NLS-1$
			}
			catch (mfsxml.MISSING_XML_TAG_EXCEPTION e)
			{
				curSubInst.setMedia2(""); //$NON-NLS-1$
			}
			curSubInst.setExplodeMedia2(getFieldOrEmptyString("DSP2", curSSXML).trim()); //$NON-NLS-1$
			curSubInst.setSizeMedia2(getFieldOrEmptyString("VIS2", curSSXML).trim()); //$NON-NLS-1$

			// set media 3 properties
			try
			{
				curSubInst.setMedia3(path + curSSXML.getField("SPC3").trim()); //$NON-NLS-1$
			}
			catch (mfsxml.MISSING_XML_TAG_EXCEPTION e)
			{
				curSubInst.setMedia3(""); //$NON-NLS-1$
			}
			curSubInst.setExplodeMedia3(getFieldOrEmptyString("DSP3", curSSXML).trim()); //$NON-NLS-1$
			curSubInst.setSizeMedia3(getFieldOrEmptyString("VIS3", curSSXML).trim()); //$NON-NLS-1$

			// set the instruction text
			String tmpIdat = ""; //$NON-NLS-1$
			StringBuffer idatHolder = new StringBuffer();

			try
			{
				tmpIdat = curSSXML.getField("IDAT"); //$NON-NLS-1$
			}
			catch (mfsxml.MISSING_XML_TAG_EXCEPTION e)
			{
				curSubInst.setInstrText(" "); //$NON-NLS-1$
			}
			while (!tmpIdat.equals("")) //$NON-NLS-1$
			{
				idatHolder.append(tmpIdat);
				tmpIdat = curSSXML.getNextField("IDAT"); //$NON-NLS-1$
			}
			curSubInst.setInstrText(idatHolder.toString());
			curSubInst.modifyInstruction(path);  // ~3C

			curInst.getSsVector().addElement(curSubInst);

			curSSRec = curSqXML.getNextField("SS"); //$NON-NLS-1$
		}//end of SS loop

		//we want to set 'R' class instructions to complete
		if (rIncfAllowed && curInst.getInstrClass().equals("R")) //$NON-NLS-1$
		{
			curInst.setCompletionStatus("C"); //$NON-NLS-1$
			
			//If not MFSViewOpsPanel, set changed
			if((this instanceof MFSViewOpsPanel) == false)
			{
				curInst.setChanged(true);
			}
		}
	}

	/**
	 * Loads the instructions.
	 * @param xml the <code>MfsXMLParser</code> containing the instructions
	 */
	public void loadInstructions(MfsXMLParser xml)
	{
		try
		{
			getSource().startAction(this.LOADING_INST_MSG); //~1C

			String path = MFSConfig.getInstance().getConfigValue("IRPATH"); //$NON-NLS-1$
			if (path.equals(MFSConfig.NOT_FOUND))
			{
				path = xml.getField("PATH"); //$NON-NLS-1$
			}
			path = path.trim();

			this.fieldHeaderRec.setPath(path);
			this.fieldHeaderRec.setBlvl(xml.getField("PMLV")); //$NON-NLS-1$

			String curSqRec = null;
			try
			{
				curSqRec = xml.getField("SQ"); //$NON-NLS-1$
			}
			catch (MISSING_XML_TAG_EXCEPTION mxte)
			{
				curSqRec = null;
			}

			if (curSqRec != null)
			{
				while (curSqRec.length() != 0)
				{
					MFSInstructionRec curInst = new MFSInstructionRec();
					MfsXMLParser curSqXML = new MfsXMLParser(curSqRec);

					loadInstruction(curInst, curSqXML, path, true);

					this.fieldInstVector.add(curInst);
					curSqRec = xml.getNextField("SQ"); //$NON-NLS-1$
				}// end of SQ loop

				String curAgRec = null;
				try
				{
					curAgRec = xml.getField("AG"); //$NON-NLS-1$
				}
				catch (mfsxml.MISSING_XML_TAG_EXCEPTION e)
				{
					curAgRec = null;
				}
				if (curAgRec != null)
				{
					while (!curAgRec.equals("")) //$NON-NLS-1$
					{
						MfsXMLParser curAGXML = new MfsXMLParser(curAgRec);

						String suff = curAGXML.getField("INSX"); //$NON-NLS-1$
						String seq = curAGXML.getField("NMSQ"); //$NON-NLS-1$
						String item = curAGXML.getField("ITEM"); //$NON-NLS-1$
						String qnty = curAGXML.getField("QNTY"); //$NON-NLS-1$

						boolean found = false;

						for (int index = 0; index < this.fieldInstVector.size() && !found; index++)
						{
							found = false;
							MFSInstructionRec rec = getInstVectorElementAt(index);
							if (rec.getSuff().equals(suff) && rec.getIseq().equals(seq))
							{
								StringBuffer agList = new StringBuffer();
								agList.append(rec.getAgragatePartList());
								agList.append("PN: ");
								agList.append(item.substring(2));
								agList.append("  QTY: ");
								agList.append(qnty);
								agList.append("  ");
								rec.setAgragatePartList(agList.toString());
								found = true;
							}
						}
						curAgRec = xml.getNextField("AG"); //$NON-NLS-1$
					}//while more agragate parts records
				}//some agragates were found
			}//some instructions found
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(this, null, null, e);
		}
		finally
		{
			getSource().stopAction(); //~1C
		}
	}

	/**
	 * Loads the separate list models that have the correct conditions (shorted
	 * parts, no suffix/seq, like suffix/seq, etc.) from the
	 * {@link MFSComponentListModel}.<br>
	 * Precondition: The {@link MFSComponentListModel} for this panel must be
	 * full of components. This {@link MFSComponentListModel} is loaded by the
	 * {@link #loadListModel(String)} method.
	 */
	public abstract void loadPartsModels();

	/**
	 * If the specified <code>MFSComponentRec</code> is not the current
	 * <code>MFSComponentRec</code>:
	 * <ol>
	 * <li>unregisters this panel as a <code>PropertyChangeListener</code>
	 * for the current <code>MFSComponentRec</code></li>
	 * <li>sets the specified <code>MFSComponentRec</code> as the current
	 * <code>MFSComponentRec</code></li>
	 * <li>registers this panel as a <code>PropertyChangeListener</code> for
	 * the specified <code>MFSComponentRec</code></li>
	 * <li>sets the text of the installed parts <code>JTextArea</code></li>
	 * </ol>
	 * @param componentRec the new current <code>MFSComponentRec</code>
	 */
	protected void setCurrentCompRec(MFSComponentRec componentRec)
	{
		if (this.fieldCurrentCompRec != componentRec)
		{
			try
			{
				/* Stop listening for events from the current MFSComponentRec */
				if (this.fieldCurrentCompRec != null)
				{
					this.fieldCurrentCompRec.removePropertyChangeListener(this);
				}

				this.fieldCurrentCompRec = componentRec;

				/* Listen for events from the new MFSComponentRec */
				if (this.fieldCurrentCompRec != null)
				{
					this.fieldCurrentCompRec.addPropertyChangeListener(this);
					this.tpInstalledParts.setText(this.fieldCurrentCompRec.getInstalledParts());
				}
			}
			catch (Throwable t)
			{
				t.printStackTrace();
			}
		}
	}

	/**
	 * Sets up the header line <code>JPanel</code>s and returns a
	 * <code>JPanel</code> that contains all of the header line
	 * <code>JPanel</code>s.
	 * @return a <code>JPanel</code> that contains all of the header line
	 *         <code>JPanel</code>s
	 */
	private JPanel setupHeaderPanel()
	{
		JPanel headerLine[] = new JPanel[6];
		int index = 0;

		headerLine[index] = createHeaderLineGridPanel();
		headerLine[index].add(this.lblUser);
		headerLine[index].add(this.lblCntr);
		headerLine[index].add(this.lblMctl);

		//The extra row is not displayed on the MFSViewOpsPanel
		if (this instanceof MFSDirectWorkPanel)
		{
			headerLine[++index] = createHeaderLineGridPanel();
			headerLine[index].add(this.lblSapo);
			headerLine[index].add(this.lblSaps);
			headerLine[index].add(this.lblCount);
			this.lblCount.setForeground(new Color(0, 140, 0));
			this.lblCount.setFont(MFSConstants.MEDIUM_DIALOG_FONT);
		}
		else if (this instanceof MFSDeconfigPanel)
		{
			headerLine[++index] = createHeaderLineGridPanel();
			headerLine[index].add(this.lblSapo);
			headerLine[index].add(this.lblSaps);
		}

		headerLine[++index] = createHeaderLineGridPanel();
		headerLine[index].add(this.nlSysNum);
		headerLine[index].add(this.vlMfgn);
		headerLine[index].add(this.nlOrder);
		headerLine[index].add(this.vlOrno);
		headerLine[index].add(this.nlLoc);
		headerLine[index].add(this.vlLoc);

		headerLine[++index] = createHeaderLineGridPanel();
		headerLine[index].add(this.nlCountry);
		headerLine[index].add(this.vlCtry);
		headerLine[index].add(this.nlBrof);
		headerLine[index].add(this.vlBrof);
		headerLine[index].add(this.nlProdID);
		headerLine[index].add(this.vlProd);

		headerLine[++index] = createHeaderLineBoxPanel();
		headerLine[index].add(Box.createHorizontalGlue());
		headerLine[index].add(this.nlMachNum);
		headerLine[index].add(this.vlMspiMcsn);
		headerLine[index].add(Box.createHorizontalGlue());
		headerLine[index].add(this.nlModel);
		headerLine[index].add(this.vlMmdl);
		headerLine[index].add(Box.createHorizontalGlue());
		headerLine[index].add(this.nlOper);
		headerLine[index].add(this.vlNmbr);
		headerLine[index].add(Box.createHorizontalGlue());
		headerLine[index].add(this.nlShipDate);
		headerLine[index].add(this.vlSchd);
		headerLine[index].add(Box.createHorizontalGlue());

		if (this instanceof MFSViewOpsPanel)
		{
			headerLine[++index] = createHeaderLineGridPanel();
			headerLine[index].add(this.lblUnpr);
			headerLine[index].add(this.lblRecon);
			headerLine[index].add(this.lblHot);
			headerLine[index].add(this.lblType);
			headerLine[index].add(this.lblLupn);
			headerLine[index].add(this.lblTwrCnt);
		}
		else
		{
			headerLine[++index] = createHeaderLineGridPanel();
			headerLine[index].add(this.lblUnpr);
			headerLine[index].add(this.lblHot);
			headerLine[index].add(this.lblType);
			headerLine[index].add(this.lblLupn);
			headerLine[index].add(this.lblTwrCnt);
			headerLine[index].add(this.lblRecon);

			this.lblTwrCnt.setFont(MFSConstants.MEDIUM_MONOSPACED_FONT);
			this.lblRecon.setFont(MFSConstants.SMALL_DIALOG_FONT);
		}

		this.lblHot.setOpaque(true);
		this.lblHot.setBackground(MFSConstants.PRIMARY_BACKGROUND_COLOR);

		JPanel result = new JPanel(new GridLayout(index + 1, 1));
		result.setBackground(MFSConstants.PRIMARY_BACKGROUND_COLOR);
		for (int i = 0; i <= index; i++)
		{
			result.add(headerLine[i]);
		}
		return result;
	}

	/**
	 * Merges the vector of component lists ({@link #fieldLmVector}) and the
	 * vector of instructions ({@link #fieldRowVector}) into rows which are
	 * added to the part instruction panel ({@link #pnlRowHolder}). Each row
	 * is an <code>MFSPartInstructionJPanel</code> and is configured based on
	 * the types of instructions and components.
	 */
	public abstract void setupPartInstPanel();

	/**
	 * Updates the cntr property of the <code>MFSHeaderRec</code> and the text
	 * of {@link #lblCntr}.
	 * @param cntr the new value of the Cntr property
	 */
	public void updt_cntr(String cntr)
	{
		this.fieldHeaderRec.setCntr(cntr);
		if (!cntr.equals("          ")) //$NON-NLS-1$
		{
			this.lblCntr.setText("Cntr: " + getHeaderRec().getCntr());
		}
		else
		{
			this.lblCntr.setText("                ");
		}
	}

	/**
	 * Invoked when a bound property is changed.
	 * @param evt the <code>PropertyChangeEvent</code>
	 */
	public void propertyChange(PropertyChangeEvent evt)
	{
		if (evt.getSource() == this.fieldCurrentCompRec
				&& (evt.getPropertyName().equals("installedParts"))) //$NON-NLS-1$
		{
			if (this.fieldCurrentCompRec != null)
			{
				this.tpInstalledParts.setText(this.fieldCurrentCompRec.getInstalledParts());
			}
		}
	}

	/**
	 * Invoked when the value of a list selection changes.
	 * <p>
	 * This method was designed to handle a <code>ListSelectionEvent</code>
	 * from the part number list of a <code>MFSPartInstructionJPanel</code>.
	 * <p>
	 * Sets the current {@link MFSComponentRec} to the selected value of the
	 * list that the <code>ListSelectionEvent</code> came from. Then goes
	 * through the other part number lists and clears the selection, but does
	 * not clear the contents of the lists.
	 * @param e the <code>ListSelectionEvent</code>
	 */
	public void valueChanged(ListSelectionEvent e)
	{
		int selectedIndex = -1;
		final JList source = (JList) e.getSource();
		if (source.getSelectedIndex() != -1)
		{
			setCurrentCompRec((MFSComponentRec) source.getSelectedValue());
			selectedIndex = source.getSelectedIndex();
		}

		for (int index = 0; index < this.fieldRowVector.size(); index++)
		{
			MFSPartInstructionJPanel pip = getRowVectorElementAt(index);
			if (pip.getPNList().getSelectedIndex() != -1)
			{
				if (!this.fieldCurrentCompRec.equals(pip.getPNList().getSelectedValue()))
				{
					pip.getPNList().clearSelection();
				}
				else
				{
					this.fieldActiveRow = index;
					this.spPartsInst.requestFocusInWindow();

					int yBounds = (int) pip.getBounds().getY();
					this.spPartsInst.getVerticalScrollBar().setValue(yBounds);
					pip.getPNList().ensureIndexIsVisible(selectedIndex);
					pip.getPNList().requestFocusInWindow();
				}
			}
		}
	}
}
