/* © Copyright IBM Corporation 2002, 2007. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 *              ~1 30852PT  D Fichtinger     -Moved remove & add listener method
 *                                            calls to improve key listening 
 * 2006-06-14   ~2 30260AT  R Prechel        -Added Restore button and method
 *                                           -Functionalized getData
 *                                           -Updated layout management
 *                                           -Updated event listener management
 *                                           -Updated component creation for
 *                                            new layout/event listening
 *                                           -Used iterator for layout/event
 *                                            listener management
 *                                           -Removed unused variables, methods, and imports
 *                                           -Used utils method for ErrorMsgBox
 *                                           -Updated creation of user object
 *                                            for assembly tree nodes.
 * 2006-08-04   ~3 26994PT  JL Woodward      -Add Users/Flags authorization logic.
 * 2006-09-15	~4 35696DF	A Williams		 -Performance Enhancement Changes.
 * 2006-09-25	~5 36228PB	A Williams		 -Fix Assembly Tree for iProxy
 * 2007-02-16   ~6 34242JR  R Prechel        -Java 5 version
 *                                           -Changed assembly tree logic
 *                                           -Changed layout to remove extra components
 *                                           -Methods called from MFSSerializedComponentFnctPanel
 * 2007-04-02   ~7 38166JM  R Prechel        -Add setLocationRelativeTo for dialogs
 * 2007-05-14   ~8 38139JM  R Prechel        -Assembly tree and PARTINFOSHEET printing changes
 * 2007-05-16   ~9 38716JM  R Prechel        -Removed null check in PartHistoryTableCellRenderer
 * 2007-11-06  ~10 40104PB  R Prechel        -MFSLabelButton changes for display of web pages
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import mfsxml.MISSING_XML_TAG_EXCEPTION;
import mfsxml.MfsXMLDocument;
import mfsxml.MfsXMLParser;

import com.ibm.rchland.mfgapps.client.utils.layout.IGSGridLayout;
import com.ibm.rchland.mfgapps.client.utils.messagebox.IGSMessageBox;
import com.ibm.rchland.mfgapps.mfsclient.MFSFrame;
import com.ibm.rchland.mfgapps.mfsclient.MFSLabelButton;
import com.ibm.rchland.mfgapps.mfsclient.MFSMenuButton;
import com.ibm.rchland.mfgapps.mfsclient.MFSPanel;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSAddCommentDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSAddFlagDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSChangeFlagDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSIncPlugCount;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSPNSNDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSPnSnChangeDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSRemoveFlagDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSSwapPlugCountDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSUsersFlagAuthDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSViewPartOpsDialog;
import com.ibm.rchland.mfgapps.mfsclient.media.MFSMediaHandler;
import com.ibm.rchland.mfgapps.mfsclient.renderer.MFSAssemblyTreeCellRenderer;
import com.ibm.rchland.mfgapps.mfsclient.tree.MFSAssemblyTreeUserObject;
import com.ibm.rchland.mfgapps.mfsclient.tree.MFSAssemblyTreeXmlParseStrategy;
import com.ibm.rchland.mfgapps.mfsclient.tree.MFSXmlTreeHandler;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSButtonIterator;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSConstants;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSPrintingMethods;
import com.ibm.rchland.mfgapps.mfscommon.MFSConfig;
import com.ibm.rchland.mfgapps.mfscommon.MFSPartDetailTableModel;
import com.ibm.rchland.mfgapps.mfscommon.MFSPartHistoryTableModel;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSComm;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSPipedTransaction;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSTransaction;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSXmlTransaction;

/**
 * <code>MFSPartFunctionDisplayPanel</code> is the <code>MFSPanel</code>
 * used to perform part functions and display part function information.
 * @author The MFS Client Development Team
 */
public class MFSPartFunctionDisplayPanel
	extends MFSPanel
	implements TreeSelectionListener
{
	private static final long serialVersionUID = 1L;

	/** The default screen ID of an <code>MFSPartFunctionDisplayPanel</code>. */
	public static final String SCREEN_ID = "PARTFUNC"; //$NON-NLS-1$ //~10A
	
	/** The default screen name of an <code>MFSPartFunctionDisplayPanel</code>. */
	public static final String SCREEN_NAME = "Part Functions"; //~6A
	
	//~6A Added the TRAN_ Strings
	/** The Retrieve Part Detail transaction <code>String</code>. */
	private final static String TRAN_DETAIL = "detail"; //$NON-NLS-1$

	/** The Edit Flags transaction <code>String</code>. */
	private final static String TRAN_EDIT_FLAGS = "editflags"; //$NON-NLS-1$

	/** The Retrieve Part History transaction <code>String</code>. */
	private final static String TRAN_HISTORY = "history"; //$NON-NLS-1$

	/** The Retrieve Assembly Tree transaction <code>String</code>. */
	private final static String TRAN_TREE = "tree"; //$NON-NLS-1$

	/** The Retrieve Plug Pictorial transaction <code>String</code>. */
	private final static String TRAN_PICTORIAL = "pictorial"; //$NON-NLS-1$

	//~2A Added the TITLE Strings
	/** The title of this panel when Current Part History view is displayed. */
	private static final String CURRENT_PART_HISTORY_TITLE = "Current Part History";

	/** The title of this panel when Archived Part History view is displayed. */
	private static final String ARCHIVED_PART_HISTORY_TITLE = "Archived Part History";

	/** The title of this panel when the Assembly Tree view is displayed. */
	private static final String ASSEMBLY_TREE_TITLE = "Assembly Tree";

	/** The title of this panel when Current Part Detail view is displayed. */
	private static final String CURRENT_PART_DETAIL_TITLE = "Current Part Detail";

	/** The title of this panel when a Plug Pictorial view is displayed. */
	private static final String PLUG_PICTORIAL_TITLE = "Plug Pictorial";

	/** The <code>Font</code> for the Part History Table. */
	protected final Font PART_HIST_FONT = new Font("SansSerif", Font.PLAIN, 12); //$NON-NLS-1$

	//~6C Eager instantiation of the buttons instead of lazy instantiation
	/** The Part Detail (F2) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbPartDetail = MFSMenuButton.createLargeButton("Part Detail",
			"partDetailF2.gif", null, true); //$NON-NLS-1$

	/** The Part History (F3) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbPartHistory = MFSMenuButton.createLargeButton("Part History",
			"partHistoryF3.gif", null, true); //$NON-NLS-1$

	/** The Change PN/SN (F4) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbChangePNSN = MFSMenuButton.createLargeButton("Change PN/SN",
			"PNSNChangeF4.gif", null, true); //$NON-NLS-1$

	/** The Assembly Tree (F5) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbAssemblyTree = MFSMenuButton.createLargeButton(
			"Assembly Tree", "assemblyTreeF5.gif", null, true); //$NON-NLS-2$

	/** The Plug Pictorial (F6) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbPlugPictorial = MFSMenuButton.createLargeButton(
			"Plug Pictorial", "plugPictorialF6.gif", null, true); //$NON-NLS-2$

	/** The Add Flag (F7) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbAddFlag1 = MFSMenuButton.createLargeButton("Add Flag",
			"addFlagF7.gif", null, false); //$NON-NLS-1$

	/** The Change Flag (F8) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbChangeFlag1 = MFSMenuButton.createLargeButton("Change Flag",
			"changeFlagF8.gif", null, false); //$NON-NLS-1$

	/** The Remove Flag (F9) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbRemoveFlag = MFSMenuButton.createLargeButton("Remove Flag",
			"removeFlagF9.gif", null, false); //$NON-NLS-1$

	/** The Add Grp Cmnt (F10) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbAddComment = MFSMenuButton.createLargeButton("Add Comment",
			"addCommentF10.gif", null, true); //$NON-NLS-1$

	/** The Print (CTRL + P)<code>MFSMenuButton</code>. */
	private MFSMenuButton pbPrint = MFSMenuButton.createLargeButton("Print",
			"printCTRLP.gif", null, false); //$NON-NLS-1$

	/** The View Test Level (F11) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbViewOps = MFSMenuButton.createLargeButton("View Test Level",
			"deconfigF11.gif", null, true); //$NON-NLS-1$

	/** The Inc Plug Cnt (F12) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbIncPlugCnt = MFSMenuButton.createLargeButton("Inc Plug Cnt",
			"incPlugCntF12.gif", null, false); //$NON-NLS-1$

	/** The Swap Plug Cnt (F13) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbSwapPlug = MFSMenuButton.createLargeButton("Swap Plug Cnt",
			"swapPlugCntF13.gif", null, false); //$NON-NLS-1$

	/** The Recalc Tstlv (F14) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbRecalcCtlv = MFSMenuButton.createLargeButton("Recalc Tstlv",
			"recalcTstlvF14.gif", null, false); //$NON-NLS-1$

	/** The Restore (F15) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbRestore = MFSMenuButton.createLargeButton("Restore",
			"restoreF15.gif", null, false); //$NON-NLS-1$

	/** The sers/Flag Auth (F16) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbUsersFlag = MFSMenuButton.createLargeButton(
			"Users/Flag Auth", "usersFlagsF16.gif", null, true); //$NON-NLS-2$
	

	//~6C Add field information to MFSLabelButton state
	//remove fields Hashtable and loadFields method 
	
	/** The Part Number <code>MFSLabelButton</code>. */
	private MFSLabelButton lbPartNumber = new MFSLabelButton("Part:  ", "PART"); //$NON-NLS-2$

	/** The Serial Number <code>MFSLabelButton</code>. */
	private MFSLabelButton lbSerialNumber = new MFSLabelButton("Serial:  ", "SNUM"); //$NON-NLS-2$

	/** The Custom Card ID <code>MFSLabelButton</code>. */
	private MFSLabelButton lbCustomCardID = new MFSLabelButton("CCIN:  ", "CCIN"); //$NON-NLS-2$

	/** The Work Unit <code>MFSLabelButton</code>. */
	private MFSLabelButton lbMCTL = new MFSLabelButton("Work Unit:  ", "MCTL"); //$NON-NLS-2$

	/** The Machine Serial <code>MFSLabelButton</code>. */
	private MFSLabelButton lbMCSN = new MFSLabelButton("Machine Serial:  ", "MCSN"); //$NON-NLS-2$

	/** The SAP Loc <code>MFSLabelButton</code>. */
	private MFSLabelButton lbPLOM = new MFSLabelButton("SAP Loc:  ", "PLOM"); //$NON-NLS-2$

	/** The Product ID <code>MFSLabelButton</code>. */
	private MFSLabelButton lbProductID = new MFSLabelButton("Product ID:  ", "PROD"); //$NON-NLS-2$

	/** The Date <code>MFSLabelButton</code>. */
	private MFSLabelButton lbDateStamp = new MFSLabelButton("Date:  ", "CSDS"); //$NON-NLS-2$

	/** The Child Reduce Flag <code>MFSLabelButton</code>. */
	private MFSLabelButton lbChildReducedFlag = new MFSLabelButton(
			"Child Reduce Flag:  ", "CRFG"); //$NON-NLS-2$

	/** The Part Quality Disposition <code>MFSLabelButton</code>. */
	private MFSLabelButton lbPartQualityDisposition = new MFSLabelButton(
			"Part Quality Disp:  ", "PRTD"); //$NON-NLS-2$

	/** The Product Line <code>MFSLabelButton</code>. */
	private MFSLabelButton lbProductLine = new MFSLabelButton("Product Line:  ", "PROD"); //$NON-NLS-2$

	/** The Family Type <code>MFSLabelButton</code>. */
	private MFSLabelButton lbFamilyType = new MFSLabelButton("Family Type:  ", "FAMT"); //$NON-NLS-2$

	/** The Test Level <code>MFSLabelButton</code>. */
	private MFSLabelButton lbTestLevel = new MFSLabelButton("Test Level:  ", "CTLV"); //$NON-NLS-2$

	/** The Macro Location <code>MFSLabelButton</code>. */
	private MFSLabelButton lbMacroLocation = new MFSLabelButton("Macro Loc:  ", "MALC"); //$NON-NLS-2$

	/** The Micro Location <code>MFSLabelButton</code>. */
	private MFSLabelButton lbMicroLocation = new MFSLabelButton("Micro Loc:  ", "MILC"); //$NON-NLS-2$

	/** The Part Type Flag <code>MFSLabelButton</code>. */
	private MFSLabelButton lbPartTypeFlag = new MFSLabelButton("Part Type Flag:  ",
			"PTFG"); //$NON-NLS-1$

	/** The Plug Count Interposer <code>MFSLabelButton</code>. */
	private MFSLabelButton lbCurrentInterposerPlugCountLabel = new MFSLabelButton(
			"Plug Count Int:  ", "CPCI"); //$NON-NLS-2$

	/** The Current Interposer Count <code>MFSLabelButton</code>. */
	private MFSLabelButton lbCurrentInterposerCount = new MFSLabelButton(
			"Interposer Count:  ", "CICT"); //$NON-NLS-2$

	/** The Part Disposition Count <code>MFSLabelButton</code>. */
	private MFSLabelButton lbPartDispositionCount = new MFSLabelButton(
			"Part Disp Count:  ", "PRDC"); //$NON-NLS-2$

	/** The Parent System <code>MFSLabelButton</code>. */
	private MFSLabelButton lbParentSystem = new MFSLabelButton("Parent System:  ", "PSYS"); //$NON-NLS-2$

	/** The Parent Type <code>MFSLabelButton</code>. */
	private MFSLabelButton lbParentType = new MFSLabelButton("Parent Type:  ", "PSTP"); //$NON-NLS-2$

	/** The Parent Work Unit <code>MFSLabelButton</code>. */
	private MFSLabelButton lbParentWu = new MFSLabelButton("Parent Work Unit:  ", "MCTL"); //$NON-NLS-2$

	/** The Parent Indicator <code>MFSLabelButton</code>. */
	private MFSLabelButton lbParentIndicator = new MFSLabelButton("Parent Indicator:  ",
			"PARI"); //$NON-NLS-1$

	/** The root <code>DefaultMutableTreeNode</code> of the assembly tree. */
	private DefaultMutableTreeNode fieldTreeRoot;

	/** The selected <code>DefaultMutableTreeNode</code> of the assembly tree. */
	private DefaultMutableTreeNode fieldSelectedTreeNode;

	/** The <code>JTree</code> for the assembly tree. */
	private JTree trAssembly = null;

	/** The <code>JTextArea</code> for the plug pictorial. */
	private JTextArea taPlugPictorial = null;

	/** The <code>JTable</code> for the part history and part detail tables. */
	private JTable tblVariety = null;

	/**
	 * The <code>JScrollPane</code> that contains either:
	 * <ol>
	 * <li>{@link #trAssembly}</li>,
	 * <li>{@link #taPlugPictorial}</li>, or
	 * <li>{@link #tblVariety}</li>
	 * </ol>
	 */
	private JScrollPane spVariety = new JScrollPane(null,
			ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
			ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

	/**
	 * Set false when the return code of RTVPRTDTL is 110. Used as a branch
	 * condition in {@link #refresh()}.
	 */
	private boolean fieldQD20found = true;

	/** Stores the data returned from the server when a transaction is run. */
	@SuppressWarnings("rawtypes")
	private Hashtable fieldReturnedData = new Hashtable();

	/** The part number used to display the current screen. */
	private String fieldPart = ""; //$NON-NLS-1$

	/** The serial number used to display the current screen. */
	private String fieldSerial = ""; //$NON-NLS-1$

	/** The part number of the selected tree node. */
	private String fieldSelectedPN = null; //~8C set null

	/** The serial number of the selected tree node. */
	private String fieldSelectedSN = null; //~8C set null

	/** Stores the most recent transaction selected by the user. */
	private String fieldTransaction = ""; //$NON-NLS-1$
	
	/** Stores the title for the panel given its current contents. */
	private String fieldTitle = ""; //$NON-NLS-1$ //~6A

	//FIXME 34242JR fieldPsys and fieldPstp are never set to non-blank,
	//but are used to set the text of lbParentSystem and lbParentType
	/** The parent system. */
	private String fieldPsys = ""; //$NON-NLS-1$

	/** The parent type. */
	private String fieldPstp = ""; //$NON-NLS-1$

	/** Stores the XML output from a server transaction. */
	private String fieldDownloadedXML = ""; //$NON-NLS-1$

	/**
	 * Set by {@link #rtvprtdtl}. Controls the enabled status of
	 * {@link #pbIncPlugCnt} and {@link #pbSwapPlug}.
	 */
	private String fieldPlugOptn = ""; //$NON-NLS-1$

	/**
	 * <code>true</code> if the part history data came from the archive
	 * tables; <code>false</code> otherwise.
	 */
	protected boolean fieldQdDataIsArchived = false; //~2 new

	/**
	 * <code>true</code> if all listeners are currently added and active;
	 * <code>false</code> if some listeners are currently removed.
	 */
	private boolean fieldListenersActive = false; //~2 new

	/**
	 * Synchronization lock <code>Object</code> used to synchronize adding
	 * listeners based on the <code>fieldListenersActive</code> flag.
	 */
	private Object fieldListenersActiveLock = new Object(); //~2 new

	//~10C Changed class and variable names
	/** The <code>MyLabelButtonListener</code> for the <code>MFSLabelButton</code>s. */
	private MyLabelButtonListener fieldLabelButtonListener = new MyLabelButtonListener();
	
	/** The <code>MFSButtonIterator</code> for the <code>MFSMenuButton</code>s. */
	private MFSButtonIterator fieldMenuButtonIterator = createMenuButtonIterator();
	
	/** The <code>MFSButtonIterator</code> for the <code>MFSLabelButton</code>s. */
	private MFSButtonIterator fieldLabelButtonIterator = createLabelButtonIterator();

	/**
	 * Constructs a new <code>MFSPartFunctionDisplayPanel</code>.
	 * @param parent the <code>MFSFrame</code> used to display this panel
	 * @param source the <code>MFSPanel</code> that caused this panel to be
	 *        displayed
	 * @param pn the part number
	 * @param sn the serial number
	 */
	public MFSPartFunctionDisplayPanel(MFSFrame parent, MFSPanel source, String pn,
										String sn)
	{
		super(parent, source, SCREEN_NAME, SCREEN_ID, new BorderLayout()); //~10C

		this.fieldPart = pn;
		this.fieldSerial = sn;

		this.lbPartNumber.setForeground(MFSConstants.PRIMARY_FOREGROUND_HIGHLIGHT_COLOR);
		this.lbSerialNumber.setForeground(MFSConstants.PRIMARY_FOREGROUND_HIGHLIGHT_COLOR);
		this.lbParentWu.setToolTipText("Parent Work Unit #");
		this.spVariety.setBackground(MFSConstants.PRIMARY_BACKGROUND_COLOR);
		this.spVariety.getViewport().setBackground(MFSConstants.PRIMARY_BACKGROUND_COLOR);
		
		createLayout();
		addMyListeners();
	}

	/** Adds this panel's <code>Component</code>s to the layout. */
	private void createLayout()
	{
		JPanel varietyPanel = new JPanel(new BorderLayout());
		varietyPanel.setBorder(BorderFactory.createEtchedBorder());
		varietyPanel.setBackground(MFSConstants.PRIMARY_BACKGROUND_COLOR);
		varietyPanel.add(this.spVariety, BorderLayout.CENTER);

		JPanel temp = new JPanel(new BorderLayout(4, 4));
		temp.add(createPartInfoPanel(), BorderLayout.NORTH);
		temp.add(varietyPanel, BorderLayout.CENTER);
		add(temp, BorderLayout.CENTER);

		add(createButtonPanelComponent(), BorderLayout.EAST);
	}
	
	/**
	 * Creates the <code>JPanel</code> that displays part information.
	 * @return the <code>JPanel</code> that displays part information
	 */
	private JPanel createPartInfoPanel()
	{
		JPanel partInfoPanel = new JPanel(new GridBagLayout());
		
		TitledBorder border = createTitledBorder();
		border.setTitleFont(MFSConstants.SMALL_DIALOG_FONT);
		partInfoPanel.setBorder(border);
		
		partInfoPanel.setBackground(MFSConstants.PRIMARY_BACKGROUND_COLOR);
		partInfoPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1, 1,
				GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(0, 10, 0, 10), 0, 0);

		//Add the first half (+1 if odd) of the label buttons to the left
		// column and the rest of the label buttons to the right column
		this.fieldLabelButtonIterator.reset();
		int firstRow = (int) Math.ceil((double) this.fieldLabelButtonIterator.size() / 2);
		int count = 0;
		while (this.fieldLabelButtonIterator.hasNext())
		{
			if (count < firstRow)
			{
				gbc.gridx = 0;
				gbc.gridy = count;
			}
			else
			{
				gbc.gridx = 1;
				gbc.gridy = count - firstRow;
			}
			//~10C Add fieldLabelButtonListener as a FocusListener
			MFSLabelButton button = this.fieldLabelButtonIterator.nextLabelButton();
			button.addFocusListener(this.fieldLabelButtonListener);
			partInfoPanel.add(button, gbc);
			count++;
		}
		return partInfoPanel;
	}
	
	/**
	 * Creates the <code>JComponent</code> containing the menu buttons.
	 * @return the <code>JComponent</code> containing the menu buttons
	 */
	private JComponent createButtonPanelComponent()
	{
		JPanel innerButtonPanel = new JPanel(new IGSGridLayout(0, 2, 2, 2));
		innerButtonPanel.setBackground(MFSConstants.PRIMARY_BACKGROUND_COLOR);

		this.fieldMenuButtonIterator.reset();
		while (this.fieldMenuButtonIterator.hasNext())
		{
			innerButtonPanel.add(this.fieldMenuButtonIterator.nextMenuButton());
		}

		JPanel outerButtonPanel = new JPanel(new FlowLayout());
		outerButtonPanel.add(innerButtonPanel);
		outerButtonPanel.setBackground(MFSConstants.PRIMARY_BACKGROUND_COLOR);
		outerButtonPanel.setBorder(BorderFactory.createEtchedBorder());
		
		return new JScrollPane(outerButtonPanel);
	}

	/** {@inheritDoc} */
	public void assignFocus()
	{
		this.requestFocusInWindow();
	}

	/** {@inheritDoc} */
	public String getMFSPanelTitle()
	{
		return this.fieldTitle;
	}

	/** Adds the listeners to this panel's <code>Component</code>s. */
	private void addMyListeners()
	{
		//~2 method redone to make use of this as a listener,
		// ButtonIterators to access components, and synchronized block

		//The combination of the synchronized block and
		//boolean flag allow the listeners to only be added once.
		synchronized (this.fieldListenersActiveLock)
		{
			if (this.fieldListenersActive)
			{
				return;
			}
			this.fieldListenersActive = true;
		}
		
		//~6 Combined addMyKeyListeners into this method

		this.fieldLabelButtonIterator.reset();
		while (this.fieldLabelButtonIterator.hasNext())
		{
			MFSLabelButton button = this.fieldLabelButtonIterator.nextLabelButton();
			button.addKeyListener(this);
			button.addMouseListener(this.fieldLabelButtonListener); //~10C
		}

		this.fieldMenuButtonIterator.reset();
		while (this.fieldMenuButtonIterator.hasNext())
		{
			MFSMenuButton button = this.fieldMenuButtonIterator.nextMenuButton();
			button.addActionListener(this);
			button.addKeyListener(this);
		}

		this.addKeyListener(this);

		if (this.spVariety != null)
		{
			this.spVariety.addKeyListener(this);

			//Call remove first to make sure it is not added twice
			Component c = this.spVariety.getViewport().getView();
			if (c != null)
			{
				c.removeKeyListener(this);
				c.addKeyListener(this);
			}
		}
		
		//~6 Do not add PropertyChangeListener because the local variables for
		// the table model were removed and thus do not need to be updated
	}

	/** Removes the listeners from this panel's <code>Component</code>s. */
	private void removeMyListeners()
	{
		//~2 method redone to make use of this as a listener,
		//ButtonIterators to access components, and synchronized block

		this.removeKeyListener(this);

		if (this.spVariety != null)
		{
			this.spVariety.removeKeyListener(this);

			Component c = this.spVariety.getViewport().getView();
			if (c != null)
			{
				c.removeKeyListener(this);
			}
		}

		this.fieldLabelButtonIterator.reset();
		while (this.fieldLabelButtonIterator.hasNext())
		{
			MFSLabelButton button = this.fieldLabelButtonIterator.nextLabelButton();
			button.removeKeyListener(this);
			button.removeMouseListener(this.fieldLabelButtonListener); //~10C
		}

		this.fieldMenuButtonIterator.reset();
		while (this.fieldMenuButtonIterator.hasNext())
		{
			MFSMenuButton button = this.fieldMenuButtonIterator.nextMenuButton();
			button.removeActionListener(this);
			button.removeKeyListener(this);
		}
		
		//~6 Do not remove PropertyChangeListener because the local variables for
		// the table model were removed and thus do not need to be updated

		//Set flag false after listeners have been removed.
		synchronized (this.fieldListenersActiveLock)
		{
			this.fieldListenersActive = false;
		}
	}
	
	/**
	 * Creates the <code>MFSButtonIterator</code> for the <code>MFSLabelButton</code>s.
	 * @return the <code>MFSButtonIterator</code>
	 */
	private MFSButtonIterator createLabelButtonIterator()
	{
		//The constructor param is the number of buttons
		MFSButtonIterator result = new MFSButtonIterator(23);
		result.add(this.lbPartNumber);
		result.add(this.lbSerialNumber);
		result.add(this.lbCustomCardID);
		result.add(this.lbMCTL);
		result.add(this.lbMCSN);
		result.add(this.lbPLOM);
		result.add(this.lbProductID);
		result.add(this.lbDateStamp);
		result.add(this.lbChildReducedFlag);
		result.add(this.lbPartQualityDisposition);
		result.add(this.lbProductLine);
		result.add(this.lbFamilyType);
		result.add(this.lbTestLevel);
		result.add(this.lbMacroLocation);
		result.add(this.lbMicroLocation);
		result.add(this.lbPartTypeFlag);
		result.add(this.lbCurrentInterposerPlugCountLabel);
		result.add(this.lbCurrentInterposerCount);
		result.add(this.lbPartDispositionCount);
		result.add(this.lbParentSystem);
		result.add(this.lbParentType);
		result.add(this.lbParentWu);
		result.add(this.lbParentIndicator);
		return result;
	}

	/**
	 * Creates the <code>MFSButtonIterator</code> for the <code>MFSMenuButton</code>s.
	 * @return the <code>MFSButtonIterator</code>
	 */
	private MFSButtonIterator createMenuButtonIterator()
	{
		//The constructor param is the number of buttons
		MFSButtonIterator result = new MFSButtonIterator(16);
		result.add(this.pbPartDetail);
		result.add(this.pbPartHistory);
		result.add(this.pbChangePNSN);
		result.add(this.pbAssemblyTree);
		result.add(this.pbPlugPictorial);
		result.add(this.pbAddFlag1);
		result.add(this.pbChangeFlag1);
		result.add(this.pbRemoveFlag);
		result.add(this.pbAddComment);
		result.add(this.pbPrint);
		result.add(this.pbViewOps);
		result.add(this.pbIncPlugCnt);
		result.add(this.pbSwapPlug);
		result.add(this.pbRecalcCtlv);
		result.add(this.pbRestore);
		result.add(this.pbUsersFlag);
		return result;
	}

	/**
	 * Calls the CHK_ACCLVL to determine the user's access level for a flag.
	 * @param flagName the flag name
	 * @param flagValu the flag value
	 * @return the access <code>String</code>
	 */
	public String access(String flagName, String flagValu)
	{
		removeMyListeners();
		String accessString = ""; //$NON-NLS-1$
		try
		{
			MFSConfig config = MFSConfig.getInstance();
			MfsXMLDocument xmlData = new MfsXMLDocument("CHK_ACCLVL"); //$NON-NLS-1$
			xmlData.addOpenTag("DATA"); //$NON-NLS-1$
			xmlData.addCompleteField("FGID", flagName.trim().toUpperCase()); //$NON-NLS-1$
			xmlData.addCompleteField("FLAG", flagValu.trim().toUpperCase()); //$NON-NLS-1$
			xmlData.addCompleteField("USER", config.getConfigValue("USER").trim()); //$NON-NLS-1$ //$NON-NLS-2$
			xmlData.addCloseTag("DATA"); //$NON-NLS-1$
			xmlData.finalizeXML();

			MFSTransaction chk_acclvl = new MFSXmlTransaction(xmlData.toString());
			chk_acclvl.setActionMessage("Retrieving PN/SN Flags, Please Wait...");
			MFSComm.getInstance().execute(chk_acclvl, this);

			int rc = chk_acclvl.getReturnCode();
			MfsXMLParser xmlParser = new MfsXMLParser(chk_acclvl.getOutput());

			if (rc == 0)
			{
				String returnString = xmlParser.getField("RVACCL"); //$NON-NLS-1$

				if (returnString.equals("CHANGE")) //$NON-NLS-1$
				{
					accessString = "CHANGE"; //$NON-NLS-1$
				}
				else if (returnString.equals("ADD   ")) //$NON-NLS-1$
				{
					accessString = "ADD   "; //$NON-NLS-1$
				}
				else if ((returnString.equals("REMOVE")) || (returnString.equals("*ALL  "))) //$NON-NLS-1$ //$NON-NLS-2$
				{
					accessString = "REMOVE"; //$NON-NLS-1$
				}
			}

			else if (rc == 100)
			{
				accessString = "NOTFND"; //$NON-NLS-1$
			}
			else if (rc == 200)
			{
				accessString = xmlParser.getField("ERMS"); //$NON-NLS-1$
			}
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
		finally
		{
			addMyListeners();
		}
		return accessString;
	}

	/** Invoked when {@link #pbAddComment} is selected. */
	private void addComment()
	{
		removeMyListeners();
		try
		{
			String pn = this.lbPartNumber.getText().substring(7, 19);
			String sn = this.lbSerialNumber.getText().substring(9, 21);
			MFSAddCommentDialog myAddCommentD = new MFSAddCommentDialog(getParentFrame(), pn, sn, this);
			myAddCommentD.setLocationRelativeTo(getParentFrame()); //~7A
			myAddCommentD.setVisible(true);

			if (myAddCommentD.getPressedEnter())
			{
				if (this.getData() == 0)
				{
					this.refresh();
					this.requestFocusInWindow();
				}
				else
				{
					this.close();
				}
			}
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
		finally
		{
			addMyListeners();
		}
	}

	/** Invoked when {@link #pbAddFlag1} is selected. */
	private void addFlag()
	{
		removeMyListeners();
		try
		{
			this.fieldTransaction = TRAN_EDIT_FLAGS;
			String pn = this.lbPartNumber.getText().substring(7, 19);
			String sn = this.lbSerialNumber.getText().substring(9, 21);
			MFSAddFlagDialog myAddFlagD = new MFSAddFlagDialog(getParentFrame(), pn, sn, this);
			myAddFlagD.setLocationRelativeTo(getParentFrame()); //~7A
			myAddFlagD.setVisible(true);

			if (myAddFlagD.getPressedEnter())
			{
				if (this.getData() == 0)
				{
					this.refresh();
					this.requestFocusInWindow();
				}
				else
				{
					this.close();
				}
			}
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
		finally
		{
			addMyListeners();
		}
	}

	/**
	 * Displays the Assembly Tree view.
	 * @param collectPNSN <code>true</code> to collect PN and SN,
	 *        <code>false</code> if they were already collected
	 */
	public void assemblyTree(boolean collectPNSN)
	{
		//~2 Changed if statement to use promptPnSnAndCheck
		//~6A Add collectPNSN check so method can be called from MFSSerializedComponentFnctPanel
		if (collectPNSN == false || promptPnSnAndCheck(ASSEMBLY_TREE_TITLE))
		{
			removeMyListeners();

			this.fieldTransaction = TRAN_TREE;
			this.fieldTitle = ASSEMBLY_TREE_TITLE;
			this.clear();
			getParentFrame().displayMFSPanel(this);

			if (this.getData() == 0)
			{
				this.refresh();
				this.pbPrint.setEnabled(true);
				disablePartDetailOnlyButtons();
				this.pbRestore.setEnabled(false);
				getParentFrame().validate();
			}
			else
			{
				this.close();
			}
			addMyListeners();
		}
	}

	/**
	 * Called by {@link #rtv_plgpic()} to build the plug pictorial.
	 * @param xmlData the data used to build the plug pictorial
	 * @return 0 on success; 1 on error 
	 * @throws MISSING_XML_TAG_EXCEPTION
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private int buildPlugPictorial(String xmlData)
		throws MISSING_XML_TAG_EXCEPTION
	{
		MfsXMLParser xmlParser = new MfsXMLParser(xmlData);
		Hashtable templateRows = new Hashtable();
		MfsXMLParser rowParser = new MfsXMLParser(xmlParser.getField("RU10")); //$NON-NLS-1$
		long rule_length = rowParser.getUnparsedXML().length();
		MfsXMLParser partParser = null;
		Vector parts = new Vector();

		String tagName = rowParser.getNextTag();
		StringBuffer rowBuffer = null;
		String thisRow = ""; //$NON-NLS-1$
		String locationNumber = ""; //$NON-NLS-1$
		Enumeration myEnum;

		/* While the tagName is a Row tagName */
		while (rowParser.index < rule_length)
		{
			if (tagName.startsWith("R") && !tagName.startsWith("RR")) //$NON-NLS-1$ //$NON-NLS-2$
			{
				templateRows.put(tagName, rowParser.getNextField(tagName));
			}
			else
			{
				if (tagName.equals("RRET")) //$NON-NLS-1$
				{
					rowParser.getNextField(tagName);
				}
				else
				{
					rowParser.index += tagName.length() + 2;
					if (tagName.startsWith("/")) //$NON-NLS-1$
					{
						tagName = tagName.substring(1);
					}
				}
			}
			if (rowParser.index < rule_length)
			{
				tagName = rowParser.getNextTag(rowParser.index);
			}
		}

		rowParser = null; /* Return for garbage collection */

		try
		{
			partParser = new MfsXMLParser(xmlParser.getField("CR10")); //$NON-NLS-1$

			this.fieldReturnedData.put("PSYS", partParser.getField("PSYS")); //$NON-NLS-1$ //$NON-NLS-2$
			this.fieldReturnedData.put("PSTP", partParser.getField("PSTP")); //$NON-NLS-1$ //$NON-NLS-2$
		}
		catch (MISSING_XML_TAG_EXCEPTION mte)
		{
			/* Do nothing. No PSYS or PSTP was found, which is not fatal */
		}

		try
		{
			while (true)
			{
				MfsXMLParser thisPartParser = new MfsXMLParser(partParser.getNextField("PART")); //$NON-NLS-1$
				//~6 Removed clone by creating a new array each time
				String[] part = new String[4];
				part[0] = thisPartParser.getField("INPN"); //$NON-NLS-1$
				part[1] = thisPartParser.getField("INSQ"); //$NON-NLS-1$
				part[2] = thisPartParser.getField("PLOC"); //$NON-NLS-1$
				part[3] = thisPartParser.getField("PCNT"); //$NON-NLS-1$
				parts.addElement(part);
			}
		}
		catch (Exception mt)
		{
			/* Do nothing. We just needed to escape the loop. */
		}

		Vector rowsChanged = new Vector(templateRows.size());
		int rowsChangedIndex = 0;
		int rowNumber = 0;
		String get_str = " "; //$NON-NLS-1$
		boolean loc_found = false;
		boolean error_occur = false;
		for (int i = 0; i < parts.size(); i++)
		{
			myEnum = templateRows.keys();

			String[] part = (String[]) parts.elementAt(i);
			String loc = part[2];
			loc_found = false;

			//Look through all the templateRows rows looking for our location.
			while (myEnum.hasMoreElements() && !error_occur && !loc_found)
			{
				String key = (String) myEnum.nextElement();
				thisRow = (String) templateRows.get(key);

				if (!loc.trim().equals("") && thisRow.indexOf(loc) != -1) //$NON-NLS-1$
				{
					locationNumber = thisRow.substring(thisRow.indexOf(loc) - 3, thisRow.indexOf(loc) - 2);

					if (locationNumber.equals(">")) //$NON-NLS-1$
					{
						locationNumber = thisRow.substring(thisRow.indexOf(loc) - 4, thisRow.indexOf(loc) - 3);
					}
					rowNumber = Integer.parseInt(key.substring(1)) - 2;

					try
					{
						/* get Plug count record */
						get_str = "PC"; //$NON-NLS-1$
						key = "R" + rowNumber; //$NON-NLS-1$
						if (key.length() == 2)
						{
							key = "R0" + rowNumber; //$NON-NLS-1$
						}
						thisRow = (String) templateRows.get(key);
						rowBuffer = new StringBuffer(thisRow.substring(0, thisRow.indexOf("<PC" + locationNumber + ">"))); //$NON-NLS-1$ //$NON-NLS-2$
						rowBuffer.append("  " + part[3]); //$NON-NLS-1$
						rowBuffer.append(thisRow.substring(thisRow.indexOf("<PC" + locationNumber + ">") + 5)); //$NON-NLS-1$ //$NON-NLS-2$
						if (!rowsChanged.contains(key))
						{
							rowBuffer.insert(0, "     "); //$NON-NLS-1$
						}
						templateRows.remove(key);
						templateRows.put(key, rowBuffer.toString());
						rowsChanged.add(key);
						rowsChangedIndex++;

						/* get Part Number record */
						get_str = "PN"; //$NON-NLS-1$
						rowNumber--;
						key = "R" + rowNumber; //$NON-NLS-1$
						if (key.length() == 2)
						{
							key = "R0" + rowNumber; //$NON-NLS-1$
						}
						thisRow = (String) templateRows.get(key);
						rowBuffer = new StringBuffer(thisRow.substring(0, thisRow.indexOf("<PN" + locationNumber + ">"))); //$NON-NLS-1$ //$NON-NLS-2$
						if (!part[0].equals("")) //$NON-NLS-1$
						{
							rowBuffer.append("  "); //$NON-NLS-1$
							rowBuffer.append(part[0].substring(5, part[0].length()));
						}
						else
						{
							rowBuffer.append("         "); //$NON-NLS-1$
						}
						rowBuffer.append(thisRow.substring(thisRow.indexOf("<PN" + locationNumber + ">") + 9)); //$NON-NLS-1$ //$NON-NLS-2$

						if (!rowsChanged.contains(key))
						{
							rowBuffer.insert(0, "     "); //$NON-NLS-1$
						}
						templateRows.remove(key);
						templateRows.put(key, rowBuffer.toString());
						rowsChanged.add(key);
						rowsChangedIndex++;

						/* get SN Number record */
						get_str = "SN"; //$NON-NLS-1$
						rowNumber--;
						key = "R" + rowNumber; //$NON-NLS-1$
						if (key.length() == 2)
						{
							key = "R0" + rowNumber; //$NON-NLS-1$
						}
						thisRow = (String) templateRows.get(key);
						rowBuffer = new StringBuffer(thisRow.substring(0, thisRow.indexOf("<SN" + locationNumber + ">"))); //$NON-NLS-1$ //$NON-NLS-2$
						if (!part[1].equals("")) //$NON-NLS-1$
						{
							rowBuffer.append(part[1]);
						}
						else
						{
							rowBuffer.append("            "); //$NON-NLS-1$
						}
						rowBuffer.append(thisRow.substring(thisRow.indexOf("<SN" + locationNumber + ">") + 12)); //$NON-NLS-1$ //$NON-NLS-2$
						if (!rowsChanged.contains(key))
						{
							rowBuffer.insert(0, "     "); //$NON-NLS-1$
						}
						templateRows.remove(key);
						templateRows.put(key, rowBuffer.toString());
						/* Keep track of which rows we've changed */
						rowsChanged.add(key);
						rowsChangedIndex++;
						loc_found = true;
					}
					catch (Exception e)
					{
						error_occur = true;

						/* display error to user */
						StringBuffer errorMessage = new StringBuffer();
						errorMessage.append("Error getting ");
						errorMessage.append(get_str);
						errorMessage.append(" from PPIC rule record ");
						errorMessage.append(key);
						errorMessage.append(" for loc ");
						errorMessage.append(loc);

						IGSMessageBox.showOkMB(getParentFrame(), null, errorMessage.toString(), null);
					}
				}
			}
			if (error_occur)
			{
				break;
			}
		}

		// Look through all the templateRows rows looking for our location.
		if (!error_occur)
		{
			for (int i = 0; i < 2; i++)
			{
				myEnum = templateRows.keys();
				while (myEnum.hasMoreElements())
				{
					String key = (String) myEnum.nextElement();
					thisRow = (String) templateRows.get(key);

					if ((thisRow.indexOf("<SN") != -1) || (thisRow.indexOf("<PN") != -1)) //$NON-NLS-1$ //$NON-NLS-2$
					{

						rowBuffer = new StringBuffer(thisRow.substring(0, thisRow.indexOf("<"))); //$NON-NLS-1$
						rowBuffer.append("            "); //$NON-NLS-1$
						rowBuffer.append(thisRow.substring(thisRow.indexOf("<") + 12)); //$NON-NLS-1$

						if (!rowsChanged.contains(key))
						{
							rowBuffer.insert(0, "     "); //$NON-NLS-1$
							rowsChanged.add(key);
						}
					}
					else if (thisRow.indexOf("<PC") != -1) //$NON-NLS-1$
					{
						rowBuffer = new StringBuffer(thisRow.substring(0, thisRow.indexOf("<PC"))); //$NON-NLS-1$
						rowBuffer.append(" 0000");
						rowBuffer.append(thisRow.substring(thisRow.indexOf("<PC") + 5)); //$NON-NLS-1$

						if (!rowsChanged.contains(key))
						{
							rowBuffer.insert(0, "     "); //$NON-NLS-1$
							rowsChanged.add(key);
						}
					}
					else if (thisRow.indexOf("<L") != -1) //$NON-NLS-1$
					{

						rowBuffer = new StringBuffer(thisRow.substring(0, thisRow.indexOf("<L") - 1)); //$NON-NLS-1$
						rowBuffer.append(" LOC: " + thisRow.substring(thisRow.indexOf("<L") + 5)); //$NON-NLS-2$

						if (!rowsChanged.contains(key))
						{
							rowBuffer.insert(0, "     "); //$NON-NLS-1$
							rowsChanged.add(key);
						}
					}
					else
					{
						rowBuffer = new StringBuffer(thisRow);

						if (!rowsChanged.contains(key))
						{
							rowBuffer.insert(0, "     "); //$NON-NLS-1$
							rowsChanged.add(key);
						}
					}
					templateRows.remove(key);
					templateRows.put(key, rowBuffer.toString());
				}
			}

			xmlParser = new MfsXMLParser(xmlData);
			String fullDate = xmlParser.getField("DATE"); //$NON-NLS-1$
			String time = xmlParser.getField("TIME").replace('.', ':'); //$NON-NLS-1$

			//~6C Use a StringBuffer
			StringBuffer plugStrBuffer = new StringBuffer();
			plugStrBuffer.append("\n  Board plug pictorial for - SN: ");
			plugStrBuffer.append(this.fieldSerial);
			plugStrBuffer.append("  PN: ");
			plugStrBuffer.append(this.fieldPart);
			plugStrBuffer.append("\n");
			try
			{
				if (!xmlParser.getField("ORNO").trim().equals("")) //$NON-NLS-1$ //$NON-NLS-2$
				{
					plugStrBuffer.append("  In ORDER: ");
					plugStrBuffer.append(xmlParser.getField("ORNO"));
					plugStrBuffer.append("  MACHINE: ");
					plugStrBuffer.append(xmlParser.getField("MFGN"));
					plugStrBuffer.append("\n");
				}
				else
				{
					plugStrBuffer.append("  In ORDER: ");
					plugStrBuffer.append(xmlParser.getField("MFGN"));
					plugStrBuffer.append("  MACHINE: ");
					plugStrBuffer.append(xmlParser.getField("MFGN"));
					plugStrBuffer.append("\n");
				}
				plugStrBuffer.append("  This listing was generated on: ");
				plugStrBuffer.append(fullDate.substring(5, 7));
				plugStrBuffer.append("-");
				plugStrBuffer.append(fullDate.substring(8, 10));
				plugStrBuffer.append("-");
				plugStrBuffer.append(fullDate.substring(0, 4));
				plugStrBuffer.append(" at ");
				plugStrBuffer.append(time);
				plugStrBuffer.append(" \n\n\n");
			}
			catch (Exception e)
			{
				plugStrBuffer.append("   Part not installed in any order.");
			}

			for (int i = 1; i <= templateRows.size(); i++)
			{
				if (i < 10)
				{
					plugStrBuffer.append(templateRows.get("R0" + i));
					plugStrBuffer.append("\n");
				}
				else
				{
					plugStrBuffer.append(templateRows.get("R" + i));
					plugStrBuffer.append("\n");
				}
			}
			//~6 Start: Create and configure the taPlugPictorial 
			this.taPlugPictorial = new JTextArea(plugStrBuffer.toString());
			this.taPlugPictorial.setFont(new Font("Monospaced", Font.PLAIN, 12)); //$NON-NLS-1$
			this.taPlugPictorial.setEditable(false);
			//~6 End: Create and configure the taPlugPictorial
			
			return 0;
		} // end if (error_occur)
		
		return 1;
	}

	/** Invoked when {@link #pbChangeFlag1} is selected. */
	private void changeFlag()
	{
		try
		{
			removeMyListeners(); //~1
			
			int rowNumber = this.tblVariety.getSelectedRow();
			String flagName = (String) this.tblVariety.getValueAt(rowNumber, 0);
			String flagValu = (String) this.tblVariety.getValueAt(rowNumber, 1);

			String accessString = access(flagName, flagValu);

			if ((accessString.equals("CHANGE")) //$NON-NLS-1$
					|| (accessString.equals("REMOVE")) //$NON-NLS-1$
					|| (accessString.equals("*ALL  ")) //$NON-NLS-1$
					|| (accessString.equals(""))) /* for cancel *///$NON-NLS-1$
			{

				String pn = this.lbPartNumber.getText().substring(7, 19);
				String sn = this.lbSerialNumber.getText().substring(9, 21);
				String flag = flagName.concat("        ").substring(0, 8) + flagValu.concat("        ").substring(0, 8);
				MFSChangeFlagDialog myChangeFlagD = new MFSChangeFlagDialog(getParentFrame(), pn, sn, flag);
				myChangeFlagD.setLocationRelativeTo(getParentFrame()); //~7A
				myChangeFlagD.setVisible(true);
				if (myChangeFlagD.getPressedEnter())
				{
					if (this.getData() == 0)
					{
						this.refresh();
						this.requestFocusInWindow();
					}
					else
					{
						this.close();
					}
				}
			}
			else
			{
				String erms = "You are not authorized to change this flag";
				IGSMessageBox.showOkMB(getParentFrame(), null, erms, null);
			}
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
		finally
		{
			this.addMyListeners(); //~1
		}
	}

	/** Invoked when {@link #pbChangePNSN} is selected. */
	private void changePnSn()
	{
		removeMyListeners();
		MFSPnSnChangeDialog myPnSnChangeDialog = new MFSPnSnChangeDialog(getParentFrame());
		myPnSnChangeDialog.setLocationRelativeTo(getParentFrame()); //~7A
		myPnSnChangeDialog.setVisible(true);
		addMyListeners();
	}
	
	//~6A New method
	/**
	 * Cleanup existing contents of variety panel before the contents are
	 * replaced with a new <code>Component</code>.
	 */
	private void cleanupVarietyPanelContent()
	{
		if (this.trAssembly != null)
		{
			if (this.fieldSelectedTreeNode != null)
			{
				this.fieldSelectedTreeNode.removeAllChildren();
				this.fieldSelectedTreeNode = null;
			}
			if (this.fieldTreeRoot != null)
			{
				this.fieldTreeRoot.removeAllChildren();
				this.fieldTreeRoot = null;
			}

			if (this.trAssembly.getModel() != null
					&& this.trAssembly.getModel().getRoot() != null)
			{
				((DefaultMutableTreeNode) this.trAssembly.getModel().getRoot()).removeAllChildren();
				this.trAssembly.setModel(null);
			}

			this.trAssembly.removeKeyListener(this);
			this.trAssembly.removeTreeSelectionListener(this);
			this.trAssembly = null;
		}
		if (this.taPlugPictorial != null)
		{
			this.taPlugPictorial.removeKeyListener(this);
			this.taPlugPictorial = null;
		}
		if (this.tblVariety != null)
		{
			this.tblVariety.removeKeyListener(this);
			this.tblVariety = null;
		}
		System.gc();
	}

	/**
	 * Removes the <code>Component</code>s from {@link #spVariety} and clears
	 * the text of the <code>MFSLabelButton</code>s.
	 */
	private void clear()
	{
		this.spVariety.setViewportView(null);

		//~2 Changed to use ButtonIterator
		this.fieldLabelButtonIterator.reset();
		while (this.fieldLabelButtonIterator.hasNext())
		{
			this.fieldLabelButtonIterator.nextLabelButton().setText(""); //$NON-NLS-1$
		}
	}

	/** Closes this panel and restores the previous screen. */
	private void close()
	{
		cleanupVarietyPanelContent();
		this.spVariety.setViewportView(null);
		this.disablePartDetailOnlyButtons();
		this.pbRestore.setEnabled(false);
		getParentFrame().restorePreviousScreen(this);
	}
	
	//~8A New method
	/**
	 * Creates a <code>Serializable</code> version of a <code>JTree</code>
	 * that can be sent to the MFS Print Server.
	 * @param result a <code>List</code> of <code>String</code>s, where
	 *        each <code>String</code> represents one <code>TreeNode</code>
	 * @param node a <code>TreeNode</code> from the tree to be printed
	 * @param indent a <code>String</code> of spaces used to indent the
	 *        <code>String</code> representation of <code>node</code>
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void createPrintableTree(List result, TreeNode node, String indent)
	{	
		StringBuffer buffer = new StringBuffer();
		buffer.append(indent);
		buffer.append(node.toString());
		if(node == this.fieldSelectedTreeNode)
		{
			buffer.append(" <<<<<<");
		}
		result.add(buffer.toString());
		
		if(node.isLeaf() == false)
		{
			String nextIndent = indent + "      ";
			Enumeration children = node.children();
			while(children.hasMoreElements())
			{
				createPrintableTree(result, (TreeNode)children.nextElement(), nextIndent);
			}
		}
	}

	//~2 New method
	/**
	 * Disables <code>MFSMenuButton</code>s that should only be enabled on
	 * the Current Part Detail view.
	 */
	private void disablePartDetailOnlyButtons()
	{
		this.pbAddFlag1.setEnabled(false);
		this.pbChangeFlag1.setEnabled(false);
		this.pbRemoveFlag.setEnabled(false);
		this.pbIncPlugCnt.setEnabled(false);
		this.pbSwapPlug.setEnabled(false);
		this.pbRecalcCtlv.setEnabled(false);
	}

	//~3
	/** Display the Users Flag Authorization Dialog */
	private void displayUsersFlag()
	{
		removeMyListeners();
		try
		{
			MFSUsersFlagAuthDialog myUsersFlagD = new MFSUsersFlagAuthDialog(getParentFrame());
			myUsersFlagD.setLocationRelativeTo(getParentFrame()); //~7A
			myUsersFlagD.setVisible(true);
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
		finally
		{
			addMyListeners();
		}
	}
	

	/**
	 * This method retrieves data from the server based on what transaction is
	 * being called.
	 * @return 0 on success, nonzero on error
	 */
	private int getData()
	{
		try
		{
			cleanupVarietyPanelContent(); //~6A
			
			if (TRAN_DETAIL.equals(this.fieldTransaction)
					|| TRAN_EDIT_FLAGS.equals(this.fieldTransaction))
			{
				return rtvprtdtl();
			}
			else if (TRAN_HISTORY.equals(this.fieldTransaction))
			{
				return rtvprthist();
			}
			else if (TRAN_TREE.equals(this.fieldTransaction))
			{
				return rtvasmtree();
			}
			else if (TRAN_PICTORIAL.equals(this.fieldTransaction))
			{
				return rtv_plgpic();
			}
		}
		catch (MISSING_XML_TAG_EXCEPTION mt)
		{
			String error = "ERMS Tag missing from returned data!";
			IGSMessageBox.showOkMB(getParentFrame(), null, error, mt);
			return 1;
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
			return 1;
		}
		return 0;
	}

	/**
	 * Prompts the user for the part number and serial number.
	 * @return <code>true</code> iff the user entered a part number and serial number
	 */
	private boolean getPNSN()
	{
		MFSPNSNDialog myPNSNDialog = new MFSPNSNDialog(getParentFrame());

		myPNSNDialog.setLocationRelativeTo(getParentFrame());

		//The transaction check is against the previous transaction
		//(fieldTransaction must be updated after getPNSN is called)
		//~8C Changed conditions to check against null
		if (TRAN_TREE.equals(this.fieldTransaction)
				&& this.fieldSelectedPN != null
				&& this.fieldSelectedPN.trim().length() != 0
				&& this.fieldSelectedSN != null
				&& this.fieldSelectedSN.trim().length() != 0)
		{
			myPNSNDialog.setPNText(this.fieldSelectedPN);
			myPNSNDialog.setSNText(this.fieldSelectedSN);
		}
		else
		{
			myPNSNDialog.setPNText(this.fieldPart);
			myPNSNDialog.setSNText(this.fieldSerial);
		}

		myPNSNDialog.setVisible(true);

		if (myPNSNDialog.getPressedEnter())
		{
			this.fieldPart = myPNSNDialog.getPNText();
			this.fieldSerial = myPNSNDialog.getSNText();
			return true;
		}
		return false;
	}

	/** Invoked when {@link #pbIncPlugCnt} is selected. */
	private void incPlugCnt()
	{
		removeMyListeners();
		try
		{
			String pn = this.lbPartNumber.getText().substring(7, 19);
			String sn = this.lbSerialNumber.getText().substring(9, 21);
			MFSIncPlugCount IncPlug = new MFSIncPlugCount(getParentFrame(), pn, sn);
			IncPlug.setLocationRelativeTo(getParentFrame()); //~7A
			IncPlug.setVisible(true);
			if (IncPlug.isMakeChange())
			{
				if (this.getData() == 0)
				{
					this.refresh();
					this.requestFocusInWindow();
				}
				else
				{
					this.close();
				}
			}
		}
		catch (IndexOutOfBoundsException index)
		{
			String erms = "Error showing Plug Interposer Count screen";
			IGSMessageBox.showOkMB(getParentFrame(), null, erms, null);
		}
		finally
		{
			addMyListeners();
		}
	}

	/**
	 * Displays the Current Part Detail view.
	 * @param collectPNSN <code>true</code> to collect PN and SN,
	 *        <code>false</code> if they were already collected
	 * @param enableButtons <code>true</code> iff the print and part detail
	 *        specific <code>MFSMenuButton</code>s should be enabled
	 */
	public void partDetail(boolean collectPNSN, boolean enableButtons)
	{
		//~2 Changed if statement to use promptPnSnAndCheck
		//~6A Add collectPNSN check so method can be called from MFSSerializedComponentFnctPanel
		if (collectPNSN == false || promptPnSnAndCheck(CURRENT_PART_DETAIL_TITLE))
		{
			removeMyListeners();

			this.fieldTransaction = TRAN_DETAIL;
			this.fieldTitle = CURRENT_PART_DETAIL_TITLE;
			this.clear();
			this.pbIncPlugCnt.setEnabled(false);
			this.pbSwapPlug.setEnabled(false);
			this.fieldPlugOptn = ""; //$NON-NLS-1$
			getParentFrame().displayMFSPanel(this);

			if (this.getData() == 0)
			{
				this.refresh();
				this.pbRestore.setEnabled(false);
				if (enableButtons)
				{
					this.pbPrint.setEnabled(true);
					this.pbAddFlag1.setEnabled(true);
					this.pbChangeFlag1.setEnabled(true);
					this.pbRemoveFlag.setEnabled(true);
					this.pbRecalcCtlv.setEnabled(true);

					if (this.fieldPlugOptn.equals("UPDT")) //$NON-NLS-1$
					{
						this.pbIncPlugCnt.setEnabled(true);
					}
					else if (this.fieldPlugOptn.equals("SWAP")) //$NON-NLS-1$
					{
						this.pbSwapPlug.setEnabled(true);
					}
					else if (this.fieldPlugOptn.equals("*ALL")) //$NON-NLS-1$
					{
						this.pbIncPlugCnt.setEnabled(true);
						this.pbSwapPlug.setEnabled(true);
					}
				}
				getParentFrame().validate();
			}
			else
			{
				this.close();
			}
			addMyListeners();
		}
	}

	/**
	 * Displays the Current Part History view or the Archived Part History view
	 * depending on the data returned by the RTVPRTHIST transaction.
	 * @param collectPNSN <code>true</code> to collect PN and SN,
	 *        <code>false</code> if they were already collected
	 */
	public void partHistory(boolean collectPNSN)
	{
		//~2 Changed if statement to use promptPnSnAndCheck
		//~6A Add collectPNSN check so method can be called from MFSSerializedComponentFnctPanel
		if (collectPNSN == false || promptPnSnAndCheck(CURRENT_PART_HISTORY_TITLE, ARCHIVED_PART_HISTORY_TITLE))
		{
			removeMyListeners();
			//~2 Moved functionality to partHistoryHelper
			partHistoryHelper();
			addMyListeners();
		}
	}

	//~2 New method to suport partHistory and restoreArchiveData
	/**
	 * Called by {@link #partHistory} and {@link #restoreArchiveData} to execute
	 * {@link #getData()} such that the <code>RTVPRTHIST</code> server
	 * transaction is executed.
	 */
	private void partHistoryHelper()
	{
		this.fieldTransaction = TRAN_HISTORY; //$NON-NLS-1$
		this.fieldTitle = CURRENT_PART_HISTORY_TITLE;
		this.clear();
		//PBResotore is setEnabled as part of getData call
		//if the data comes from the archive table
		this.pbRestore.setEnabled(false);
		this.getParentFrame().displayMFSPanel(this);

		if (this.getData() == 0)
		{
			this.refresh();
			this.pbPrint.setEnabled(true);
			this.disablePartDetailOnlyButtons();
			this.requestFocusInWindow();
			this.getParentFrame().validate();
		}
		else
		{
			this.close();
		}
	}

	/**
	 * Displays the Plug Pictorial view.
	 * @param collectPNSN <code>true</code> to collect PN and SN,
	 *        <code>false</code> if they were already collected
	 */
	public void plugPictorial(boolean collectPNSN)
	{
		//~2 Changed if statement to use promptPnSnAndCheck
		//~6A Add collectPNSN check so method can be called from MFSSerializedComponentFnctPanel
		if (collectPNSN == false || promptPnSnAndCheck(PLUG_PICTORIAL_TITLE))
		{
			removeMyListeners();

			this.fieldTransaction = TRAN_PICTORIAL;
			this.fieldTitle = PLUG_PICTORIAL_TITLE;
			this.clear();
			getParentFrame().displayMFSPanel(this);

			if (this.getData() == 0)
			{
				this.refresh();
				this.pbPrint.setEnabled(false);
				this.disablePartDetailOnlyButtons();
				this.pbRestore.setEnabled(false);
				getParentFrame().validate();
				this.taPlugPictorial.requestFocusInWindow();
			}
			else
			{
				this.close();
			}
			addMyListeners();
		}
	}

	/** This method calls the print method for the currently active screen. */
	@SuppressWarnings("rawtypes")
	private void print()
	{
		if (TRAN_DETAIL.equals(this.fieldTransaction))
		{
			//~8C Create a new model without any listeners so it is serializable
			MFSPartDetailTableModel printModel = 
				new MFSPartDetailTableModel((MFSPartDetailTableModel)this.tblVariety.getModel());
			MFSPrintingMethods.partInfo(this.fieldTransaction, this.fieldReturnedData, printModel, 1, getParentFrame());
		}
		else if (TRAN_HISTORY.equals(this.fieldTransaction))
		{
			//~8C Create a new model without any listeners so it is serializable
			MFSPartHistoryTableModel printModel =
				new MFSPartHistoryTableModel((MFSPartHistoryTableModel)this.tblVariety.getModel());
			MFSPrintingMethods.partInfo(this.fieldTransaction, this.fieldReturnedData, printModel, 1, getParentFrame());
		}
		else if (TRAN_TREE.equals(this.fieldTransaction))
		{
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) this.trAssembly.getLastSelectedPathComponent();

			if (node != null)
			{
				Object userObj = node.getUserObject();
				String partInfo = userObj.toString();

				//~8C Look for PN=, not PART
				if (partInfo.indexOf("PN=") != -1) //$NON-NLS-1$
				{
					//~6C Obtain qd10 Hashtable directly from node
					Hashtable qd10Hash = ((MFSAssemblyTreeUserObject) userObj).getQD10Data();

					//~8C Create a List of Strings to send to the MFS Print Server
					LinkedList printList = new LinkedList();
					createPrintableTree(printList, this.fieldTreeRoot, "");
					
					/* Print */
					MFSPrintingMethods.assemblyTree(qd10Hash, printList, 1, getParentFrame());
				}
				else
				{
					String erms = "The selected branch is not a part.  Please select a part before printing!";
					IGSMessageBox.showOkMB(getParentFrame(), null, erms, null);
				}
			}
			else
			{
				String erms = "You must select a part before printing!";
				IGSMessageBox.showOkMB(getParentFrame(), null, erms, null);
			}
		}
	}

	//~2 New method containing logic for checking if a transaction should run
	/**
	 * Prompts the user for the part number and serial number to use for a transaction.
	 * @param newTitle the title for the transaction in question
	 * @return <code>true</code> if the transaction should run;
	 *         <code>false</code> if the transaction should not run.
	 */
	private boolean promptPnSnAndCheck(String newTitle)
	{
		String oldPart = this.fieldPart;
		String oldSerial = this.fieldSerial;
		return getPNSN() 
		&& ( !this.fieldPart.equals(oldPart) || !this.fieldSerial.equals(oldSerial) ||
				!getParentFrame().getTitle().equals(newTitle) )
		&& ( !this.fieldPart.equals("") && !this.fieldSerial.equals("") ); //$NON-NLS-1$ //$NON-NLS-2$
	}

	//~2 New method containing logic for checking if a transaction should run
	/**
	 * Prompts the user for the part number and serial number to use for a transaction.
	 * @param newTitleOne one of two possible titles for the transaction
	 * @param newTitleTwo one of two possible titles for the transaction
	 * @return <code>true</code> if the transaction should run;
	 *         <code>false</code> if the transaction should not run.
	 */
	private boolean promptPnSnAndCheck(String newTitleOne, String newTitleTwo)
	{
		String currentTitle = getParentFrame().getTitle();
		String oldPart = this.fieldPart;
		String oldSerial = this.fieldSerial;
		return getPNSN()
		&& ( !this.fieldPart.equals(oldPart) || !this.fieldSerial.equals(oldSerial) || 
			(!currentTitle.equals(newTitleOne) && !currentTitle.equals(newTitleTwo)) )
		&& ( !this.fieldPart.equals("") && !this.fieldSerial.equals("") ); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/** Invoked when {@link #pbRecalcCtlv} is selected. */
	private void recalcCtlv()
	{
		//~2 Getting PN/SN from user was removed as part of IPSR30260AT*/
		try
		{
			if (!this.fieldPart.equals("") && !this.fieldSerial.equals("")) //$NON-NLS-1$ //$NON-NLS-2$
			{
				MfsXMLDocument xml_data1 = new MfsXMLDocument("CHG_PNSNF"); //$NON-NLS-1$
				xml_data1.addOpenTag("DATA"); //$NON-NLS-1$
				xml_data1.addCompleteField("INPN", this.fieldPart.trim().toUpperCase()); //$NON-NLS-1$
				xml_data1.addCompleteField("INSQ", this.fieldSerial.trim().toUpperCase()); //$NON-NLS-1$
				xml_data1.addCompleteField("FGID", "        "); //$NON-NLS-1$ //$NON-NLS-2$
				xml_data1.addCompleteField("FVAL", "        "); //$NON-NLS-1$ //$NON-NLS-2$
				xml_data1.addCompleteField("NEWF", "        "); //$NON-NLS-1$ //$NON-NLS-2$
				xml_data1.addCompleteField("CMMT", "                                                                                ");  //$NON-NLS-1$ //$NON-NLS-2$
				xml_data1.addCompleteField("UACT", "T"); //$NON-NLS-1$ //$NON-NLS-2$
				xml_data1.addCompleteField("USER", MFSConfig.getInstance().get8CharUser()); //$NON-NLS-1$
				xml_data1.addCloseTag("DATA"); //$NON-NLS-1$
				xml_data1.finalizeXML();

				MFSTransaction chg_pnsnf = new MFSXmlTransaction(xml_data1.toString());
				chg_pnsnf.setActionMessage("Updating PN/SN Flag, Please Wait...");
				MFSComm.getInstance().execute(chg_pnsnf, this);

				if (chg_pnsnf.getReturnCode() == 0)
				{
					this.getData();
					refresh();
				}
				else
				{
					IGSMessageBox.showOkMB(getParentFrame(), null, chg_pnsnf.getErms(),null);
				}
			}
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
	}

	/** Updates the display with the data returned from the server. */
	private void refresh()
	{
		try
		{
			// 2/10/2004 (TOU LEE)
			if (this.fieldQD20found == false)
			{
				clear();
				this.lbPartNumber.setValueText(this.fieldPart);
				this.lbSerialNumber.setValueText(this.fieldSerial);
				this.fieldQD20found = true;
			}
			else if (TRAN_TREE.equals(this.fieldTransaction) == false)
			{
				updateNonTreeQD10();
			}

			if (TRAN_DETAIL.equals(this.fieldTransaction)
					|| TRAN_EDIT_FLAGS.equals(this.fieldTransaction))
			{
				MFSPartDetailTableModel dataModel = new MFSPartDetailTableModel(this.fieldDownloadedXML);
				this.tblVariety = new JTable(dataModel);
				//~8C Set the size of the TableColumns created by the JTable
				// instead of creating the TableColumns
				TableColumnModel columnModel = this.tblVariety.getColumnModel();
				columnModel.getColumn(0).setPreferredWidth(60);
				columnModel.getColumn(1).setPreferredWidth(60);
				columnModel.getColumn(2).setPreferredWidth(200);
				columnModel.getColumn(3).setPreferredWidth(200);
				this.tblVariety.getSelectionModel().setSelectionInterval(0, 0);
				this.tblVariety.setBackground(MFSConstants.PRIMARY_BACKGROUND_COLOR);
				this.spVariety.setViewportView(this.tblVariety);
				this.tblVariety.addKeyListener(this);
			}
			else if (TRAN_HISTORY.equals(this.fieldTransaction))
			{
				MFSPartHistoryTableModel dataModel = new MFSPartHistoryTableModel(this.fieldDownloadedXML);
				this.tblVariety = new JTable(dataModel);
				//~8C Set the size of the TableColumns created by the JTable
				// instead of creating the TableColumns
				TableColumnModel columnModel = this.tblVariety.getColumnModel();
				columnModel.getColumn(0).setPreferredWidth(65);
				columnModel.getColumn(1).setPreferredWidth(65);
				columnModel.getColumn(2).setPreferredWidth(65);
				columnModel.getColumn(3).setPreferredWidth(75);
				columnModel.getColumn(4).setPreferredWidth(550);
				//~8C Set the renderer used by the table
				this.tblVariety.setDefaultRenderer(String.class, new PartHistoryTableCellRenderer());
				this.tblVariety.getSelectionModel().setSelectionInterval(0, 0);
				this.tblVariety.setRowHeight(60 + (dataModel.getMaxFlags() * 16));
				this.tblVariety.setFont(this.PART_HIST_FONT);
				this.tblVariety.setBackground(MFSConstants.PRIMARY_BACKGROUND_COLOR);
				this.spVariety.setViewportView(this.tblVariety);
				this.tblVariety.addKeyListener(this);
			}
			else if (TRAN_PICTORIAL.equals(this.fieldTransaction))
			{
				this.spVariety.setViewportView(this.taPlugPictorial);
				this.taPlugPictorial.addKeyListener(this);
			}
			else if (TRAN_TREE.equals(this.fieldTransaction))
			{
				this.trAssembly = new JTree(this.fieldTreeRoot);
				this.trAssembly.setCellRenderer(new MFSAssemblyTreeCellRenderer());
				this.trAssembly.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

				if (this.fieldSelectedTreeNode == null)
				{
					this.fieldSelectedTreeNode = this.fieldTreeRoot;
				}

				TreePath selectedPath = new TreePath(this.fieldSelectedTreeNode.getPath());
				this.trAssembly.setSelectionPath(selectedPath);
				this.trAssembly.expandPath(selectedPath);

				Object obj = this.fieldSelectedTreeNode.getUserObject();
				if (obj instanceof MFSAssemblyTreeUserObject)
				{
					//~6C Obtain qd10 Hashtable directly from node
					updateTreeQD10(((MFSAssemblyTreeUserObject) obj).getQD10Data());
				}

				this.trAssembly.addKeyListener(this);
				//~6C Make this the TreeSelectionListener
				this.trAssembly.addTreeSelectionListener(this);
				this.spVariety.setViewportView(this.trAssembly);
			}
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
	}

	/** Invoked when {@link #pbRemoveFlag} is selected. */
	private void removeFlag()
	{
		try
		{
			removeMyListeners(); //~1
			int rowNumber = this.tblVariety.getSelectedRow();
			String flagName = (String) this.tblVariety.getValueAt(rowNumber, 0);
			String flagValu = (String) this.tblVariety.getValueAt(rowNumber, 1);

			String accessString = access(flagName, flagValu);

			if ((accessString.equals("REMOVE")) //$NON-NLS-1$
					|| (accessString.equals("*ALL  ")) //$NON-NLS-1$
					|| (accessString.equals(""))) /* for cancel *///$NON-NLS-1$
			{
				String pn = this.lbPartNumber.getText().substring(7, 19);
				String sn = this.lbSerialNumber.getText().substring(9, 21);
				String flag = flagName.concat("        ").substring(0, 8) + flagValu.concat("        ").substring(0, 8);
				MFSRemoveFlagDialog myRemoveFlagD = new MFSRemoveFlagDialog(getParentFrame(), pn, sn, flag);
				myRemoveFlagD.setLocationRelativeTo(getParentFrame()); //~7A
				myRemoveFlagD.setVisible(true);

				if (myRemoveFlagD.getPressedEnter())
				{
					if (this.getData() == 0)
					{
						this.refresh();
						this.requestFocusInWindow();
					}
					else
					{
						this.close();
					}
				}
			}
			else
			{
				String erms = "You are not authorized to remove this flag";
				IGSMessageBox.showOkMB(getParentFrame(), null, erms, null);
			}
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
		finally
		{
			this.addMyListeners(); //~1
		}
	}

	//~2 New method for restore button
	/** Restores data from the archive table. */
	private void restoreArchiveData()
	{
		//Safety check. Calling RSTQDJC is only valid from the Archived Part History view
		if (getParentFrame().getTitle().equals(ARCHIVED_PART_HISTORY_TITLE))
		{
			removeMyListeners();
			MfsXMLDocument xmlData = new MfsXMLDocument("RSTQDJC"); //$NON-NLS-1$
			xmlData.addOpenTag("DATA"); //$NON-NLS-1$
			xmlData.addCompleteField("QDPN", this.fieldPart); //$NON-NLS-1$
			xmlData.addCompleteField("QDSQ", this.fieldSerial); //$NON-NLS-1$
			xmlData.addCloseTag("DATA"); //$NON-NLS-1$
			xmlData.finalizeXML();

			MFSTransaction rstqdjc = new MFSXmlTransaction(xmlData.toString());
			rstqdjc.setActionMessage("Restoring data from archive...");
			MFSComm.getInstance().execute(rstqdjc, this);

			partHistoryHelper();
			addMyListeners();
		}
	}

	//~2 New method resulting from functionalization of getData
	/**
	 * Helper method used by {@link #getData()} to call the
	 * <code>RTVASMTREE</code> server transaction.
	 * @return 0 on success, nonzero on failure
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private int rtvasmtree()
	{
		MfsXMLDocument xml_data = new MfsXMLDocument("RTVASMTREE"); //$NON-NLS-1$
		xml_data.addOpenTag("DATA"); //$NON-NLS-1$
		xml_data.addCompleteField("INPN", this.fieldPart); //$NON-NLS-1$
		xml_data.addCompleteField("INSQ", this.fieldSerial); //$NON-NLS-1$
		xml_data.addCloseTag("DATA"); //$NON-NLS-1$
		xml_data.finalizeXML();

		MFSPipedTransaction rtvasmtree = new MFSPipedTransaction(xml_data.toString());
		rtvasmtree.setActionMessage("Retrieving data.  Please wait...");

		//~6C The logic used to parse the assembly tree changed
		// to use the SAX parser instead of the StAX parser
		// so that the StAX jars did not have to be included
		//The buildTree, createNodes, and createNodeString methods were replaced
		MFSAssemblyTreeXmlParseStrategy strategy = 
			new MFSAssemblyTreeXmlParseStrategy(this.fieldPart, this.fieldSerial);
		MFSXmlTreeHandler handler = new MFSXmlTreeHandler(rtvasmtree, strategy);
		DefaultMutableTreeNode node = handler.parse(this);

		if (rtvasmtree.getReturnCode() != 0)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, rtvasmtree.getErms(), null);
		}
		else if (node == null)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, rtvasmtree.getErms(), null);
			rtvasmtree.setReturnCode(10);
		}
		else
		{
			this.fieldTreeRoot = node;
			this.fieldSelectedTreeNode = handler.getSelectedNode();

			Hashtable rootQD10Data = ((MFSAssemblyTreeUserObject) node.getUserObject()).getQD10Data();
			
			//Make sure the row contains MCTL
			if (rootQD10Data.contains("MCTL") == false) //$NON-NLS-1$
			{
				rootQD10Data.put("MCTL", "        "); //$NON-NLS-1$
			}
		}
		return rtvasmtree.getReturnCode();
	}

	//~2 New method resulting from functionalization of getData
	/**
	 * Helper method used by {@link #getData()} to call the
	 * <code>RTV_PLGPIC</code> server transaction.
	 * @return 0 on success, nonzero on failure
	 * @throws MISSING_XML_TAG_EXCEPTION
	 */
	@SuppressWarnings("unchecked")
	private int rtv_plgpic()
		throws MISSING_XML_TAG_EXCEPTION
	{
		MfsXMLDocument xml_data = new MfsXMLDocument("RTV_PLGPIC"); //$NON-NLS-1$
		xml_data.addOpenTag("DATA"); //$NON-NLS-1$
		xml_data.addCompleteField("INPN", this.fieldPart); //$NON-NLS-1$
		xml_data.addCompleteField("INSQ", this.fieldSerial); //$NON-NLS-1$
		xml_data.addCloseTag("DATA"); //$NON-NLS-1$
		xml_data.finalizeXML();

		MFSTransaction rtv_plgpic = new MFSXmlTransaction(xml_data.toString());
		rtv_plgpic.setActionMessage("Retrieving plug data.  Please wait...");
		MFSComm.getInstance().execute(rtv_plgpic, this);
		int rc = rtv_plgpic.getReturnCode();

		this.fieldDownloadedXML = rtv_plgpic.getOutput();
		if (rc == 0)
		{
			xml_data = new mfsxml.MfsXMLDocument("RTV_QD10"); //$NON-NLS-1$
			xml_data.addOpenTag("DATA"); //$NON-NLS-1$
			xml_data.addOpenTag("FLD"); //$NON-NLS-1$
			xml_data.addCompleteField("OPRT", "="); //$NON-NLS-1$ //$NON-NLS-2$
			xml_data.addCompleteField("NAME", "QDQDPN"); //$NON-NLS-1$ //$NON-NLS-2$
			xml_data.addCompleteField("VALU", "'" + this.fieldPart + "'"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			xml_data.addCloseTag("FLD"); //$NON-NLS-1$
			xml_data.addOpenTag("FLD"); //$NON-NLS-1$
			xml_data.addCompleteField("OPRT", "AND"); //$NON-NLS-1$ //$NON-NLS-2$
			xml_data.addCloseTag("FLD"); //$NON-NLS-1$
			xml_data.addOpenTag("FLD"); //$NON-NLS-1$
			xml_data.addCompleteField("OPRT", "="); //$NON-NLS-1$ //$NON-NLS-2$
			xml_data.addCompleteField("NAME", "QDQDSQ"); //$NON-NLS-1$ //$NON-NLS-2$
			xml_data.addCompleteField("VALU", "'" + this.fieldSerial + "'"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			xml_data.addCloseTag("FLD"); //$NON-NLS-1$
			xml_data.addCloseTag("DATA"); //$NON-NLS-1$
			xml_data.finalizeXML();

			MFSTransaction rtv_qd10 = new MFSXmlTransaction(xml_data.toString());
			rtv_qd10.setActionMessage("Retrieving QD10 data.  Please wait...");
			MFSComm.getInstance().execute(rtv_qd10, this);

			rc += rtv_qd10.getReturnCode();
			this.fieldDownloadedXML += rtv_qd10.getOutput();
		}
		MfsXMLParser xmlParser = new mfsxml.MfsXMLParser(this.fieldDownloadedXML);

		if (rc == 0)
		{
			rc = buildPlugPictorial(this.fieldDownloadedXML);
			if (rc == 1)
			{
				return rc;
			}

			MfsXMLParser fieldData = new MfsXMLParser(xmlParser.getNextField("FLD", 0)); //$NON-NLS-1$
			try
			{
				while (fieldData.unparsedLength() > 0)
				{
					this.fieldReturnedData.put(fieldData.getField("NAME"), fieldData.getField("VALU")); //$NON-NLS-1$ //$NON-NLS-2$
					fieldData.setUnparsedXML(xmlParser.getNextField("FLD")); //$NON-NLS-1$
				}
			}
			catch (MISSING_XML_TAG_EXCEPTION mt)
			{
				String erms = "Missing tag exception!";
				IGSMessageBox.showOkMB(getParentFrame(), null, erms, null);
				return 1;
			}
		}
		else
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, xmlParser.getField("ERMS"), null); //$NON-NLS-1$
			return 1;
		}
		return 0;
	}

	//~2 New method resulting from functionalization of getData
	/**
	 * Helper method used by {@link #getData()} to call the
	 * <code>RTVPRTDTL</code> server transaction.
	 * @return 0 on success, nonzero on failure
	 * @throws MISSING_XML_TAG_EXCEPTION
	 */
	@SuppressWarnings("unchecked")
	private int rtvprtdtl()
		throws MISSING_XML_TAG_EXCEPTION
	{
		MfsXMLDocument xmlData = new MfsXMLDocument("RTVPRTDTL"); //$NON-NLS-1$
		xmlData.addOpenTag("DATA"); //$NON-NLS-1$
		xmlData.addCompleteField("INPN", this.fieldPart); //$NON-NLS-1$
		xmlData.addCompleteField("INSQ", this.fieldSerial); //$NON-NLS-1$
		xmlData.addCompleteField("USER", MFSConfig.getInstance().get8CharUser()); //$NON-NLS-1$
		xmlData.addCloseTag("DATA"); //$NON-NLS-1$
		xmlData.finalizeXML();

		MFSTransaction rtvprtdtl = new MFSXmlTransaction(xmlData.toString());
		rtvprtdtl.setActionMessage("Retrieving data.  Please wait...");
		MFSComm.getInstance().execute(rtvprtdtl, this);

		int rc = rtvprtdtl.getReturnCode();
		this.fieldDownloadedXML = rtvprtdtl.getOutput();
		MfsXMLParser xmlParser = new MfsXMLParser(this.fieldDownloadedXML);

		if (rc >= 0 && rc <= 100)
		{
			if (rc != 0)
			{
				/* There was an error. Write it out... */
				System.out.println(IGSMessageBox.SEPARATOR);
				System.out.println("Non-fatal error reported in rtvprtdtl().\nError = " + xmlParser.getField("ERMS"));
				System.out.println("\nTransaction = " + this.fieldTransaction);
				System.out.println(IGSMessageBox.SEPARATOR);
			}
			this.fieldPlugOptn = xmlParser.getFieldOnly("OPTN"); //$NON-NLS-1$
			this.fieldQD20found = true;

			MfsXMLParser fieldData = new MfsXMLParser(xmlParser.getNextField("FLD", 0)); //$NON-NLS-1$

			try
			{
				while (fieldData.unparsedLength() > 0)
				{
					this.fieldReturnedData.put(fieldData.getField("NAME"), fieldData.getField("VALU")); //$NON-NLS-1$ //$NON-NLS-2$
					fieldData.setUnparsedXML(xmlParser.getNextField("FLD")); //$NON-NLS-1$
				}
			}
			catch (MISSING_XML_TAG_EXCEPTION mt)
			{
				String erms = "Missing tag exception!";
				IGSMessageBox.showOkMB(getParentFrame(), null, erms, null);
				return 1;
			}
		}
		else if (rc == 110)
		{
			//~2 For a part with archive data: Display error message
			//Run RTVPRTHIST to determine if there is archive data
			MfsXMLDocument xmlData2 = new MfsXMLDocument("RTVPRTHIST"); //$NON-NLS-1$
			xmlData2.addOpenTag("DATA"); //$NON-NLS-1$
			xmlData2.addCompleteField("INPN", this.fieldPart); //$NON-NLS-1$
			xmlData2.addCompleteField("INSQ", this.fieldSerial); //$NON-NLS-1$
			xmlData2.addCloseTag("DATA"); //$NON-NLS-1$
			xmlData2.finalizeXML();

			MFSTransaction rtvprthist = new MFSXmlTransaction(xmlData2.toString());
			rtvprthist.setActionMessage("Retrieving data.  Please wait...");
			MFSComm.getInstance().execute(rtvprthist, this);

			//Display error message if there is archive data
			if (rtvprthist.getOutput().indexOf("<AGQD10>") != -1) //$NON-NLS-1$
			{
				String erms = "Archived data exists.  The Part History retrieval must be executed to restore the data.";
				IGSMessageBox.showOkMB(getParentFrame(), null, erms, null);
				return 2;
			}

			this.fieldQD20found = false;
			//For a part without archive data (Pre IPSR30260AT comment follows):
			//DO NOTHING...ALLOW THE USER TO ADD FLAG EVEN IF THE PART IS NOT IN QD10.
			//AFTER USER ADDS A FLAG, CHG_PNSNF should be called to log to QD10 and QD20.
		}
		else
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, xmlParser.getField("ERMS"), null); //$NON-NLS-1$
			return 1;
		}
		return 0;
	}

	//~2 New method resulting from functionalization of getData
	/**
	 * Helper method used by {@link #getData()}to call the
	 * <code>RTVPRTHIST</code> server transaction.
	 * @return 0 on success, nonzero on failure
	 * @throws MISSING_XML_TAG_EXCEPTION
	 */
	@SuppressWarnings("unchecked")
	private int rtvprthist()
		throws MISSING_XML_TAG_EXCEPTION
	{
		MfsXMLDocument xmlData = new MfsXMLDocument("RTVPRTHIST"); //$NON-NLS-1$
		xmlData.addOpenTag("DATA"); //$NON-NLS-1$
		xmlData.addCompleteField("INPN", this.fieldPart); //$NON-NLS-1$
		xmlData.addCompleteField("INSQ", this.fieldSerial); //$NON-NLS-1$
		xmlData.addCloseTag("DATA"); //$NON-NLS-1$
		xmlData.finalizeXML();

		MFSTransaction rtvprthist = new MFSXmlTransaction(xmlData.toString());
		rtvprthist.setActionMessage("Retrieving data.  Please wait...");
		MFSComm.getInstance().execute(rtvprthist, this);

		int rc = rtvprthist.getReturnCode();

		this.fieldDownloadedXML = rtvprthist.getOutput();
		MfsXMLParser xmlParser = new MfsXMLParser(this.fieldDownloadedXML);
		MfsXMLParser QD10xml = new MfsXMLParser(this.fieldDownloadedXML);
		MfsXMLParser QD20xml = new MfsXMLParser(this.fieldDownloadedXML);

		this.fieldQdDataIsArchived = false;
		if (rc == 0)
		{
			try
			{
				if (xmlParser.contains("QD10")) //$NON-NLS-1$
				{
					QD10xml = new MfsXMLParser(xmlParser.getFieldOnly("QD10")); //$NON-NLS-1$
					QD20xml = new MfsXMLParser(xmlParser.getFieldOnly("QD20")); //$NON-NLS-1$
				}
				else if (xmlParser.contains("AGQD10")) //$NON-NLS-1$
				{
					QD10xml = new MfsXMLParser(xmlParser.getFieldOnly("AGQD10")); //$NON-NLS-1$
					QD20xml = new MfsXMLParser(xmlParser.getFieldOnly("AGQD20")); //$NON-NLS-1$
					this.fieldQdDataIsArchived = true;
					this.fieldTitle = ARCHIVED_PART_HISTORY_TITLE;
					this.getParentFrame().setTitle(ARCHIVED_PART_HISTORY_TITLE);
					this.pbRestore.setEnabled(true);
				}
			}
			catch (MISSING_XML_TAG_EXCEPTION mt)
			{
				String erms = "QD10 Tag missing from returned data!";
				IGSMessageBox.showOkMB(getParentFrame(), null, erms, null);
				return 1;
			}
		}
		else if (rc == -10)
		{
			/* A problem occured running the transaction */
			String errorMessage;
			try
			{
				errorMessage = xmlParser.getField("ERMS"); //$NON-NLS-1$
			}
			catch (MISSING_XML_TAG_EXCEPTION mt)
			{
				errorMessage = rtvprthist.getOutput();
			}
			IGSMessageBox.showOkMB(getParentFrame(), null, errorMessage, null);
			return 1;
		}

		if (rc <= 100)
		{
			if (rc != 0)
			{
				/* There was an error. Write it out... */
				System.out.println(IGSMessageBox.SEPARATOR);
				System.out.println("Non-fatal error reported in rtvprthist().\nError = " + xmlParser.getField("ERMS"));
				System.out.println("\nTransaction = " + this.fieldTransaction);
				System.out.println(IGSMessageBox.SEPARATOR);
			}

			MfsXMLParser QD10Data = new MfsXMLParser(QD10xml.getNextField("FLD", 0)); //$NON-NLS-1$
			MfsXMLParser QD20Data = new MfsXMLParser(QD20xml.getNextField("FLD", 0)); //$NON-NLS-1$

			try
			{
				while (QD10Data.unparsedLength() > 0)
				{
					this.fieldReturnedData.put(QD10Data.getField("NAME"), QD10Data.getField("VALU")); //$NON-NLS-1$ //$NON-NLS-2$
					QD10Data.setUnparsedXML(QD10xml.getNextField("FLD")); //$NON-NLS-1$
				}

				while (QD20Data.unparsedLength() > 0)
				{
					this.fieldReturnedData.put(QD20Data.getField("NAME"), QD20Data.getField("VALU")); //$NON-NLS-1$ //$NON-NLS-2$
					QD20Data.setUnparsedXML(QD20xml.getNextField("FLD")); //$NON-NLS-1$
				}
			}
			catch (MISSING_XML_TAG_EXCEPTION mt)
			{
				IGSMessageBox.showOkMB(getParentFrame(), null, null, mt);
				return 1;
			}
		}
		else
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, xmlParser.getField("ERMS"), null); //$NON-NLS-1$
			return 1;
		}
		return 0;
	}

	/**
	 * Sets the field that stores the XML output from a server transaction.
	 * @param downloadedXML the XML output
	 */
	public void setDownloadedXML(String downloadedXML)
	{
		this.fieldDownloadedXML = downloadedXML;
	}

	/** Invoked when {@link #pbSwapPlug} is selected. */
	private void swapPlugCnt()
	{
		removeMyListeners();
		try
		{
			String pn = this.lbPartNumber.getText().substring(7, 19);
			String sn = this.lbSerialNumber.getText().substring(9, 21);
			MFSSwapPlugCountDialog SwapPlug = new MFSSwapPlugCountDialog(getParentFrame(), pn, sn);
			SwapPlug.setLocationRelativeTo(getParentFrame()); //~7A
			SwapPlug.setVisible(true);

			if (SwapPlug.isChangeMake())
			{
				this.fieldPart = SwapPlug.getToPNLabelText();
				this.fieldSerial = SwapPlug.getToSNLabelText();
				if (this.getData() == 0)
				{
					this.refresh();
					this.requestFocusInWindow();
				}
				else
				{
					this.close();
				}
			}
		}
		catch (IndexOutOfBoundsException index)
		{
			String erms = "Error showing Plug Interposer Count screen";
			IGSMessageBox.showOkMB(getParentFrame(), null, erms, null);
		}
		finally
		{
			addMyListeners();
		}
	}

	/** Updates the QD10 information for transaction other than assembly tree. */
	private void updateNonTreeQD10()
	{
		//~6C Changed logic to use methods of MFSLabelButton to set text
		
		this.lbPLOM.setLabelText("SAP Plant:  ");
		this.lbProductID.setLabelText("Product ID:  ");

		this.lbPartNumber.setValueText(this.fieldReturnedData.get("QDQDPN")); //$NON-NLS-1$
		this.lbSerialNumber.setValueText(this.fieldReturnedData.get("QDQDSQ")); //$NON-NLS-1$
		this.lbMCTL.setValueText(this.fieldReturnedData.get("QDMCTL")); //$NON-NLS-1$
		this.lbProductID.setValueText(this.fieldReturnedData.get("QDPROD")); //$NON-NLS-1$
		this.lbDateStamp.setValueText(this.fieldReturnedData.get("QDCSDS")); //$NON-NLS-1$
		this.lbPartQualityDisposition.setValueText(this.fieldReturnedData.get("QDPRTD")); //$NON-NLS-1$
		this.lbProductLine.setValueText(this.fieldReturnedData.get("Q2PRLN")); //$NON-NLS-1$
		this.lbFamilyType.setValueText(this.fieldReturnedData.get("Q2FAMT")); //$NON-NLS-1$
		this.lbTestLevel.setValueText(this.fieldReturnedData.get("QDCTLV")); //$NON-NLS-1$
		this.lbMacroLocation.setValueText(this.fieldReturnedData.get("QDMALC")); //$NON-NLS-1$
		this.lbMicroLocation.setValueText(this.fieldReturnedData.get("QDMILC")); //$NON-NLS-1$
		this.lbPartTypeFlag.setValueText(this.fieldReturnedData.get("QDPTFG")); //$NON-NLS-1$
		this.lbCurrentInterposerPlugCountLabel.setValueText(this.fieldReturnedData.get("QDCPCI")); //$NON-NLS-1$
		this.lbCurrentInterposerCount.setValueText(this.fieldReturnedData.get("QDCICT")); //$NON-NLS-1$
		this.lbPartDispositionCount.setValueText(this.fieldReturnedData.get("QDPRDC")); //$NON-NLS-1$
		this.lbParentSystem.setValueText(this.fieldPsys);
		this.lbParentType.setValueText(this.fieldPstp);
		this.lbParentIndicator.setValueText(this.fieldReturnedData.get("CRPARI")); //$NON-NLS-1$

		StringBuffer valueBuffer = null;
		String valueString1, valueString2;

		//lbCustomCardID value
		valueString1 = (String) this.fieldReturnedData.get("QDCCIN"); //$NON-NLS-1$
		if (valueString1 != null && valueString1.trim().length() != 0)
		{
			valueString2 = (String) this.fieldReturnedData.get("RZGRUP"); //$NON-NLS-1$
			if (valueString2 != null && valueString2.trim().length() != 0)
			{
				valueBuffer = new StringBuffer();
				valueBuffer.append(valueString1);
				valueBuffer.append(" (");
				valueBuffer.append(valueString2);
				valueBuffer.append(")");
				this.lbCustomCardID.setValueText(valueBuffer);
				valueBuffer = null;
			}
			else
			{
				this.lbCustomCardID.setValueText(valueString1);
			}
		}
		else
		{
			this.lbCustomCardID.setValueText(null);
		}

		//lbMCSN value
		valueString1 = (String) this.fieldReturnedData.get("QDMSPI"); //$NON-NLS-1$
		valueString2 = (String) this.fieldReturnedData.get("QDMCSN"); //$NON-NLS-1$
		if (valueString1 != null && valueString2 != null)
		{
			valueBuffer = new StringBuffer();
			valueBuffer.append(valueString1);
			valueBuffer.append("-");
			valueBuffer.append(valueString2.substring(2, valueString2.length()));
		}
		this.lbMCSN.setValueText(valueBuffer);
		valueBuffer = null;

		//lbPLOM value
		valueString1 = (String) this.fieldReturnedData.get("QDSPLT"); //$NON-NLS-1$
		if (valueString1 != null && valueString1.trim().length() != 0)
		{
			valueString2 = (String) this.fieldReturnedData.get("QDPLNT"); //$NON-NLS-1$
			if (valueString2 != null && valueString2.trim().length() != 0)
			{
				valueBuffer = new StringBuffer();
				valueBuffer.append(valueString1);
				valueBuffer.append(" - ");
				valueBuffer.append(valueString2);
				this.lbPLOM.setValueText(valueBuffer.toString());
				valueBuffer = null;
			}
			else
			{
				this.lbPLOM.setValueText(valueString1);
			}
		}
		else
		{
			this.lbPLOM.setValueText(null);
		}

		//lbChildReducedFlag value
		valueString1 = (String) this.fieldReturnedData.get("QDCRFG"); //$NON-NLS-1$
		this.lbChildReducedFlag.setValueText(valueString1);
		if (valueString1 != null && valueString1.equals("Y")) //$NON-NLS-1$
		{
			this.lbChildReducedFlag.setForeground(Color.red);
		}
		else
		{
			this.lbChildReducedFlag.setForeground(Color.black);
		}

		//lbParentWu value
		valueString1 = (String) this.fieldReturnedData.get("CRMCTL"); //$NON-NLS-1$
		if (valueString1 != null && valueString1.trim().length() != 0)
		{
			valueString2 = (String) this.fieldReturnedData.get("PWUNIDSP"); //$NON-NLS-1$
			if (valueString2 != null && valueString2.trim().equals("I")) //$NON-NLS-1$
			{
				this.lbParentWu.setForeground(Color.orange);
			}
			else
			{
				this.lbParentWu.setForeground(Color.black);
			}
			this.lbParentWu.setValueText(valueString1);
		}
		else
		{
			this.lbParentWu.setForeground(Color.black);
			this.lbParentWu.setValueText(null);
		}
	}

	/**
	 * Updates the QD10 information for the assembly tree transaction.
	 * @param qd10Data the data for the selected node
	 */
	@SuppressWarnings("rawtypes")
	private void updateTreeQD10(Hashtable qd10Data)
	{
		//~6C Changed logic to use methods of MFSLabelButton to set text
		
		this.lbPLOM.setLabelText("SAP Loc:  ");
		this.lbProductID.setLabelText("Product Line:  ");
		
		if (qd10Data == null)
		{
			this.fieldLabelButtonIterator.reset();
			while (this.fieldLabelButtonIterator.hasNext())
			{
				MFSLabelButton next = this.fieldLabelButtonIterator.nextLabelButton();
				next.setText(next.getLabelText());
			}
		}
		else
		{
			this.lbPartNumber.setValueText(qd10Data.get("QDPN")); //$NON-NLS-1$
			this.lbSerialNumber.setValueText(qd10Data.get("QDSQ")); //$NON-NLS-1$
			this.lbMCTL.setValueText(qd10Data.get("QDMCTL")); //$NON-NLS-1$
			this.lbPLOM.setValueText(qd10Data.get("PLOM")); //$NON-NLS-1$
			this.lbProductID.setValueText(qd10Data.get("PROD")); //$NON-NLS-1$
			this.lbDateStamp.setValueText(qd10Data.get("CSDS")); //$NON-NLS-1$
			this.lbPartQualityDisposition.setValueText(qd10Data.get("PRTD")); //$NON-NLS-1$
			this.lbProductLine.setValueText(qd10Data.get("PRLN")); //$NON-NLS-1$
			this.lbTestLevel.setValueText(qd10Data.get("CTLV")); //$NON-NLS-1$
			this.lbMacroLocation.setValueText(qd10Data.get("MALC")); //$NON-NLS-1$
			this.lbMicroLocation.setValueText(qd10Data.get("MILC")); //$NON-NLS-1$
			this.lbPartTypeFlag.setValueText(qd10Data.get("PTFG")); //$NON-NLS-1$
			this.lbCurrentInterposerPlugCountLabel.setValueText(qd10Data.get("CPCI")); //$NON-NLS-1$
			this.lbCurrentInterposerCount.setValueText(qd10Data.get("CICT")); //$NON-NLS-1$
			this.lbPartDispositionCount.setValueText(qd10Data.get("PRDC")); //$NON-NLS-1$
			this.lbParentSystem.setValueText(qd10Data.get("PSYS")); //$NON-NLS-1$
			this.lbParentType.setValueText(qd10Data.get("PSTP")); //$NON-NLS-1$
			this.lbParentIndicator.setValueText(qd10Data.get("CRPARI")); //$NON-NLS-1$

			StringBuffer valueBuffer = null;
			String valueString1, valueString2;

			//lbCustomCardID value
			valueString1 = (String) qd10Data.get("CCIN"); //$NON-NLS-1$
			if (valueString1 != null && valueString1.trim().length() != 0)
			{
				valueString2 = (String) qd10Data.get("RZGRUP"); //$NON-NLS-1$
				valueBuffer = new StringBuffer();
				valueBuffer.append(valueString1);
				valueBuffer.append("(");
				valueBuffer.append(valueString2);
				valueBuffer.append(")");
				this.lbCustomCardID.setValueText(valueBuffer);
				valueBuffer = null;
			}
			else
			{
				this.lbCustomCardID.setValueText(null);
			}

			//lbMCSN value
			valueString1 = (String) qd10Data.get("MSPI"); //$NON-NLS-1$
			valueString2 = (String) qd10Data.get("MCSN"); //$NON-NLS-1$
			if (valueString1 != null && valueString2 != null)
			{
				valueBuffer = new StringBuffer();
				valueBuffer.append(valueString1);
				valueBuffer.append("-");
				valueBuffer.append(valueString2.substring(2, valueString2.length()));
			}
			this.lbMCSN.setValueText(valueBuffer);
			valueBuffer = null;

			//lbChildReducedFlag value
			valueString1 = (String) qd10Data.get("CRFG"); //$NON-NLS-1$
			this.lbChildReducedFlag.setValueText(valueString1);
			if (valueString1 != null && valueString1.equals("Y")) //$NON-NLS-1$
			{
				this.lbChildReducedFlag.setForeground(Color.red);
			}
			else
			{
				this.lbChildReducedFlag.setForeground(Color.black);
			}

			//lbParentWu value
			valueString1 = (String) qd10Data.get("MCTL"); //$NON-NLS-1$
			if (valueString1 == null || valueString1.trim().length() == 0)
			{
				valueString1 = (String) qd10Data.get("CRMCTL"); //$NON-NLS-1$
			}
			if (valueString1 != null && valueString1.trim().length() != 0)
			{
				valueString2 = (String) qd10Data.get("PWUNIDSP"); //$NON-NLS-1$
				if (valueString2 != null && valueString2.trim().equals("I")) //$NON-NLS-1$
				{
					this.lbParentWu.setForeground(Color.orange);
				}
				else
				{
					this.lbParentWu.setForeground(Color.black);
				}
				this.lbParentWu.setValueText(valueString1);
			}
			else
			{
				this.lbParentWu.setForeground(Color.black);
				this.lbParentWu.setValueText(null);
			}
		}
		
		this.update(this.getGraphics());
	}

	/** Invoked when {@link #pbViewOps} is selected. */
	private void viewOps()
	{
		removeMyListeners();
		try
		{
			String ccin = this.lbCustomCardID.getText().substring(7, 11);
			MFSViewPartOpsDialog VPOD = new MFSViewPartOpsDialog(getParentFrame(), this, ccin);
			VPOD.loadTree();
			VPOD.setLocationRelativeTo(getParentFrame()); //~7A
			VPOD.setVisible(true);
		}
		catch (IndexOutOfBoundsException index)
		{
			String erms = "Error getting CCIN number.";
			IGSMessageBox.showOkMB(getParentFrame(), null, erms, null);
		}
		finally
		{
			addMyListeners();
		}
	}

	/**
	 * Invoked when an action occurs.
	 * @param ae the <code>ActionEvent</code>
	 */
	public void actionPerformed(ActionEvent ae)
	{
		//~2 anonymous action listeners where removed
		// action listener code was added here
		try
		{
			final Object source = ae.getSource();
			if (source == this.pbPartDetail)
			{
				partDetail(true, true);
			}
			else if (source == this.pbPartHistory)
			{
				partHistory(true);
			}
			else if (source == this.pbChangePNSN)
			{
				changePnSn();
			}
			else if (source == this.pbAssemblyTree)
			{
				assemblyTree(true);
			}
			else if (source == this.pbPlugPictorial)
			{
				plugPictorial(true);
			}
			else if (source == this.pbAddFlag1)
			{
				addFlag();
			}
			else if (source == this.pbChangeFlag1)
			{
				changeFlag();
			}
			else if (source == this.pbRemoveFlag)
			{
				removeFlag();
			}
			else if (source == this.pbPrint)
			{
				print();
			}
			else if (source == this.pbAddComment)
			{
				addComment();
			}
			else if (source == this.pbViewOps)
			{
				viewOps();
			}
			else if (source == this.pbIncPlugCnt)
			{
				incPlugCnt();
			}
			else if (source == this.pbSwapPlug)
			{
				swapPlugCnt();
			}
			else if (source == this.pbRecalcCtlv)
			{
				recalcCtlv();
			}
			else if (source == this.pbRestore)
			{
				restoreArchiveData();
			}
			else if (source == this.pbUsersFlag) //~3A
			{
				displayUsersFlag();
			}
		}
		catch (Throwable t)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, t);
		}
	}

	/**
	 * Invoked when a key is pressed.
	 * @param ke the <code>KeyEvent</code>
	 */
	public void keyPressed(KeyEvent ke)
	{
		//~2 fixed to check isShiftDown instead of F13, F14
		final int keyCode = ke.getKeyCode();
		if (keyCode == KeyEvent.VK_ESCAPE)
		{
			close();
		}
		else if (keyCode == KeyEvent.VK_ENTER && ke.getSource() instanceof MFSMenuButton)
		{
			MFSMenuButton source = (MFSMenuButton) ke.getSource();
			source.requestFocusInWindow();
			source.doClick();
		}
		else if (keyCode == KeyEvent.VK_P && ke.isControlDown())
		{
			this.pbPrint.requestFocusInWindow();
			this.pbPrint.doClick();
		}
		else if (ke.isShiftDown())
		{
			if (keyCode == KeyEvent.VK_F1)
			{
				this.pbSwapPlug.requestFocusInWindow();
				this.pbSwapPlug.doClick();
			}
			else if (keyCode == KeyEvent.VK_F2)
			{
				this.pbRecalcCtlv.requestFocusInWindow();
				this.pbRecalcCtlv.doClick();
			}
			else if (keyCode == KeyEvent.VK_F3)
			{ //~2 new
				this.pbRestore.requestFocusInWindow();
				this.pbRestore.doClick();
			}
			else if (keyCode == KeyEvent.VK_F4)
			{ //~3 new
				this.pbUsersFlag.requestFocusInWindow();
				this.pbUsersFlag.doClick();
			}
			else if (keyCode == KeyEvent.VK_TAB
					&& ke.getSource().getClass().equals(JTable.class))
			{
				this.lbPartNumber.requestFocusInWindow();
			}
		}
		else if (keyCode == KeyEvent.VK_F1)
		{
			final Object source = ke.getSource();
			if (source == this.tblVariety)
			{
				TableModel tableModel = this.tblVariety.getModel();
				if (tableModel instanceof MFSPartDetailTableModel)
				{
					int rowNumber = this.tblVariety.getSelectedRow();
					String flagName = ((String) this.tblVariety.getValueAt(rowNumber, 0)).trim();
					//~8C Use String getUrl(String) instead of Hashtable getUrls()
					String url = ((MFSPartDetailTableModel) tableModel).getUrl(flagName);
					MFSMediaHandler.displayWebPage(this, MFSMediaHandler.URL_PREFIX + url); //~10C
				}
			}
			else if (source instanceof MFSLabelButton)
			{
				MFSMediaHandler.displayWebPage(this, ((MFSLabelButton) source).getID()); //~10C
			}
		}
		else if (keyCode == KeyEvent.VK_F2)
		{
			this.pbPartDetail.requestFocusInWindow();
			this.pbPartDetail.doClick();
		}
		else if (keyCode == KeyEvent.VK_F3)
		{
			this.pbPartHistory.requestFocusInWindow();
			this.pbPartHistory.doClick();
		}
		else if (keyCode == KeyEvent.VK_F4)
		{
			this.pbChangePNSN.requestFocusInWindow();
			this.pbChangePNSN.doClick();
		}
		else if (keyCode == KeyEvent.VK_F5)
		{
			this.pbAssemblyTree.requestFocusInWindow();
			this.pbAssemblyTree.doClick();
		}
		else if (keyCode == KeyEvent.VK_F6)
		{
			this.pbPlugPictorial.requestFocusInWindow();
			this.pbPlugPictorial.doClick();
		}
		else if (keyCode == KeyEvent.VK_F7)
		{
			this.pbAddFlag1.requestFocusInWindow();
			this.pbAddFlag1.doClick();
		}
		else if (keyCode == KeyEvent.VK_F8)
		{
			this.pbChangeFlag1.requestFocusInWindow();
			this.pbChangeFlag1.doClick();
		}
		else if (keyCode == KeyEvent.VK_F9)
		{
			this.pbRemoveFlag.requestFocusInWindow();
			this.pbRemoveFlag.doClick();
		}
		else if (keyCode == KeyEvent.VK_F10)
		{
			this.pbAddComment.requestFocusInWindow();
			this.pbAddComment.doClick();
		}
		else if (keyCode == KeyEvent.VK_F11)
		{
			this.pbViewOps.requestFocusInWindow();
			this.pbViewOps.doClick();
		}
		else if (keyCode == KeyEvent.VK_F12)
		{
			this.pbIncPlugCnt.requestFocusInWindow();
			this.pbIncPlugCnt.doClick();
		}
	}

	/**
	 * Invoked when the value of a tree selection changes.
	 * @param e the <code>TreeSelectionEvent</code>
	 */
	@SuppressWarnings("rawtypes")
	public void valueChanged(TreeSelectionEvent e)
	{
		try
		{
			Object obj = this.trAssembly.getLastSelectedPathComponent();
			if (obj instanceof DefaultMutableTreeNode)
			{
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) obj;
				Object partInfo = node.getUserObject();
				if (partInfo instanceof MFSAssemblyTreeUserObject)
				{
					//~6C Obtain qd10 Hashtable directly from node
					Hashtable qd10Hash = ((MFSAssemblyTreeUserObject) partInfo).getQD10Data();
					updateTreeQD10(qd10Hash);

					this.fieldSelectedPN = (String) qd10Hash.get("QDPN"); //$NON-NLS-1$
					this.fieldSelectedSN = (String) qd10Hash.get("QDSQ"); //$NON-NLS-1$
					this.fieldSelectedTreeNode = node; //~8A
				}
			}

		}
		catch (Exception exception)
		{
			exception.printStackTrace();
		}
	}

	//~6 Named anonymous MouseAdapter and used MFSMediaHandler.displayHelp
	//~10C Changed name and implemented FocusListener
	/**
	 * <code>MyLabelButtonListener</code> is the <code>MouseListener</code>
	 * and <code>FocusListener</code> for the <code>MFSLabelButton</code>s.
	 * @author The MFS Client Development Team
	 */
	private class MyLabelButtonListener
		extends MouseAdapter
		implements FocusListener
	{
		/** Constructs a new <code>MyLabelButtonListener</code>. */
		public MyLabelButtonListener()
		{
			super();
		}

		/**
		 * Invoked when the mouse has been clicked on a <code>Component</code>.
		 * @param me the <code>MouseEvent</code>
		 */
		public void mouseClicked(MouseEvent me)
		{
			/* Display help if the mouse was clicked twice. */
			if (me.getClickCount() == 2)
			{
				//~10C Name changes
				String id = ((MFSLabelButton) me.getComponent()).getID();
				MFSMediaHandler.displayWebPage(MFSPartFunctionDisplayPanel.this, id);
			}
		}
		
		//~10A New method
		/**
		 * Invoked when a <code>Component</code> with this registered as a
		 * <code>FocusListener</code> gains the keyboard focus.
		 * @param fe the <code>FocusEvent</code>
		 */
		public void focusGained(FocusEvent fe)
		{
			((MFSLabelButton) fe.getSource()).setBackground(Color.lightGray);	
		}

		//~10A New method
		/**
		 * Invoked when a <code>Component</code> with this registered as a
		 * <code>FocusListener</code> loses the keyboard focus.
		 * @param fe the <code>FocusEvent</code>
		 */
		public void focusLost(FocusEvent fe)
		{
			((MFSLabelButton) fe.getSource()).setBackground(Color.white);
		}
	}

	//~2 New class. Replaces mfscommon -> common.PartTableCellRenderer
	// Required to access qdDataIsArchived
	/**
	 * <code>PartHistoryTableCellRenderer</code> is the
	 * <code>TableCellRenderer</code> for the part history table.
	 */
	private class PartHistoryTableCellRenderer
		extends JTextArea
		implements TableCellRenderer
	{
		private static final long serialVersionUID = 1L;
		/** Constructs a new <code>PartHistoryTableCellRenderer</code>. */
		public PartHistoryTableCellRenderer()
		{
			super();
		}

		/** {@inheritDoc} */
		public Component getTableCellRendererComponent(JTable table, Object value,
														boolean isSelected,
														boolean hasFocus, int row,
														int column)
		{
			setFont(MFSPartFunctionDisplayPanel.this.PART_HIST_FONT);
			setLineWrap(false);
			
			//~9D Removed null check
			setText(value.toString());
			
			if (isSelected)
			{
				setBackground(MFSConstants.SELECTED_CELL_BACKGROUND_COLOR);
			}
			else if (MFSPartFunctionDisplayPanel.this.fieldQdDataIsArchived)
			{
				setBackground(MFSConstants.PRIMARY_BACKGROUND_HIGHLIGHT_COLOR);
			}
			else
			{
				setBackground(MFSConstants.PRIMARY_BACKGROUND_COLOR);
			}
			
			setForeground(MFSConstants.PRIMARY_FOREGROUND_COLOR);
			return this;
		}
	}
}
