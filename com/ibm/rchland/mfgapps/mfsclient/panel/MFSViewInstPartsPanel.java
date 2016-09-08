/* © Copyright IBM Corporation 2005, 2016. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2006-07-13   ~1          D.Fichtinger     -add new constructor and do various clean up
 * 2006-08-17   ~2 34222BP  VH Avila         -Add new Search part logic
 * 2006-10-02   ~3 36494BP  VH Avila         -Don't compare the RC to determinate if the Barcode
 *                                            is valid, the barcode will be valid if have one
 *                                            field filled from BCPartObject and don't 
 *                                            return the HDR field   
 * 2007-02-20   ~4 34242JR  R Prechel        -Java 5 version
 *                                           -Redo search logic to avoid scrolling
 * 2016-02-18   ~5 1471226  Miguel Rivas     -MFSPanel deprecated
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.panel;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.ScrollPaneConstants;

import com.ibm.rchland.mfgapps.client.utils.messagebox.IGSMessageBox;
import com.ibm.rchland.mfgapps.mfsclient.MFSDialog;
import com.ibm.rchland.mfgapps.mfsclient.MFSFrame;
import com.ibm.rchland.mfgapps.mfsclient.MFSPanel;
import com.ibm.rchland.mfgapps.mfsclient.barcode.MFSBCBarcodeDecoder;
import com.ibm.rchland.mfgapps.mfsclient.barcode.MFSBCIndicatorValue;
import com.ibm.rchland.mfgapps.mfsclient.barcode.MFSBCPartObject;
import com.ibm.rchland.mfgapps.mfsclient.dialog.rework.MFSRwkDialog;
import com.ibm.rchland.mfgapps.mfsclient.struct.MFSComponentListModel;
import com.ibm.rchland.mfgapps.mfsclient.struct.MFSComponentRec;
import com.ibm.rchland.mfgapps.mfsclient.struct.MFSHeaderRec;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSConstants;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSRework;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSScroller;
import com.ibm.rchland.mfgapps.mfscommon.MFSConfig;

/**
 * <code>MFSViewInstPartsPanel</code> is the <code>MFSPanel</code> used to
 * display a list of installed parts for a work unit.
 * @author The MFS Client Development Team
 */
public class MFSViewInstPartsPanel
	extends MFSPanel
{
	private static final long serialVersionUID = 1L;
	/** The default screen name of an <code>MFSViewInstPartsPanel</code>. */
	public static final String SCREEN_NAME = "View Installed Parts - Does Not Include Current Operation";

	/** The search criteria input <code>JTextField</code>. */
	private JTextField tfInput = MFSDialog.createTextField(15, 0);

	/** The search criteria <code>JLabel</code>. */
	private JLabel lblSearch = MFSDialog.createLabel("Search Criteria: ");

	/** The <code>JList</code> of installed parts. */
	private JList lstInstParts = MFSDialog.createList();

	/** The <code>JScrollPane</code> that displays the list of installed parts. */
	private JScrollPane spInstParts = new JScrollPane(this.lstInstParts,
			ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
			ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

	/** The Clear <code>JButton</code>. */
	private JButton pbClear = MFSDialog.createButton("Clear", 'C');

	/** The Search <code>JButton</code>. */
	private JButton pbSearch = MFSDialog.createButton("Search", 'S');

	/** The Rework <code>JButton</code>. */
	private JButton pbRework = MFSDialog.createButton("F6 - Rework", 'R');

	/** The Cancel <code>JButton</code>. */
	private JButton pbCancel = MFSDialog.createButton("Cancel", 'n');

	/** The <code>Vector</code> of search criteria. */
	@SuppressWarnings("rawtypes")
	private Vector fieldSearchCriteria = new Vector();

	/** The <code>MFSHeaderRec</code> for the work unit. */
	private MFSHeaderRec fieldHeaderRec;

	/** The current operation number of the work unit. */
	private String fieldCurrNmbr;

	/**
	 * Constructs a new <code>MFSViewInstPartsPanel</code>.
	 * @param parent the <code>MFSFrame</code> used to display this panel
	 * @param source the <code>MFSPanel</code> that caused this panel to be displayed
	 * @param headerRec the <code>MFSHeaderRec</code> for the work unit
	 */
	public MFSViewInstPartsPanel(MFSFrame parent, MFSPanel source, MFSHeaderRec headerRec)
	{
		super(parent, source, SCREEN_NAME, "", new GridBagLayout()); //~5
		this.fieldHeaderRec = headerRec;
		this.fieldCurrNmbr = headerRec.getNmbr();
		this.lstInstParts.setModel(new MFSComponentListModel());
		createLayout();
		addMyListeners();
	}

	/** Adds this panel's <code>Component</code> s to the layout. */
	protected void createLayout()
	{
		this.pbClear.setEnabled(false);
		this.pbSearch.setEnabled(false);
		MFSDialog.setButtonDimensions(this.pbClear, this.pbSearch);

		JLabel headerLabel = new JLabel(
				" Fam Cd  Part Number    Seq. Number   M. Serial  Description                     EC             CA           CN      Container    Nmbr   User       Plug Data                                                       ");
		headerLabel.setOpaque(true);
		headerLabel.setBackground(new Color(204, 204, 255));
		headerLabel.setForeground(MFSConstants.PRIMARY_FOREGROUND_COLOR);
		headerLabel.setFont(MFSConstants.MEDIUM_MONOSPACED_FONT);

		this.spInstParts.setColumnHeaderView(headerLabel);

		JPanel bottomButtonPanel = new JPanel(new GridLayout(1, 2, 100, 0));
		bottomButtonPanel.setBackground(MFSConstants.PRIMARY_BACKGROUND_COLOR);
		bottomButtonPanel.add(this.pbRework);
		bottomButtonPanel.add(this.pbCancel);

		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1, 0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, 
				new Insets(10, 100, 0, 5), 0, 0);

		this.add(this.tfInput, gbc);

		gbc.gridx = 1;
		gbc.weightx = 0;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(10, 0, 0, 0);
		this.add(this.pbClear, gbc);
		gbc.gridx = 2;
		gbc.insets = new Insets(10, 5, 0, 100);
		this.add(this.pbSearch, gbc);

		gbc.gridx = 0;
		gbc.gridy++;
		gbc.gridwidth = 3;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(10, 100, 0, 100);
		this.add(this.lblSearch, gbc);

		gbc.gridy++;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(15, 50, 0, 50);
		this.add(this.spInstParts, gbc);

		gbc.gridy++;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(10, 0, 10, 0);
		this.add(bottomButtonPanel, gbc);
	}

	/** Adds the listeners to this panel's <code>Component</code>s. */
	private void addMyListeners()
	{
		this.pbClear.addActionListener(this);
		this.pbSearch.addActionListener(this);
		this.pbRework.addActionListener(this);
		this.pbCancel.addActionListener(this);

		this.pbClear.addKeyListener(this);
		this.pbSearch.addKeyListener(this);
		this.pbRework.addKeyListener(this);
		this.pbCancel.addKeyListener(this);
		this.tfInput.addKeyListener(this);
		this.lstInstParts.addKeyListener(this);
	}

	/** Removes the listeners from this panel's <code>Component</code>s. */
	private void removeMyListeners()
	{
		this.pbClear.removeActionListener(this);
		this.pbSearch.removeActionListener(this);
		this.pbRework.removeActionListener(this);
		this.pbCancel.removeActionListener(this);

		this.pbClear.removeKeyListener(this);
		this.pbSearch.removeKeyListener(this);
		this.pbRework.removeKeyListener(this);
		this.pbCancel.removeKeyListener(this);
		this.tfInput.removeKeyListener(this);
		this.lstInstParts.removeKeyListener(this);
	}

	/**
	 * Adds the specified criteria to the current search criteria.
	 * @param criteria the search criteria to add
	 */
	@SuppressWarnings("unchecked")
	public void addSearchCriteria(String criteria)
	{
		if (this.fieldSearchCriteria.size() < 3)
		{
			this.fieldSearchCriteria.add(criteria);
		}
		else
		{
			this.fieldSearchCriteria.remove(2);
			this.fieldSearchCriteria.add(criteria);
		}

		this.pbClear.setEnabled(true);
		this.pbSearch.setEnabled(true);

		int posVector = 1;
		StringBuffer searchCriteria = new StringBuffer();
		searchCriteria.append("Search Criteria: ");
		searchCriteria.append(this.fieldSearchCriteria.get(0));
		while (posVector < this.fieldSearchCriteria.size())
		{
			searchCriteria.append(" and ");
			searchCriteria.append(this.fieldSearchCriteria.get(posVector));
			posVector++;
		}

		this.lblSearch.setText(searchCriteria.toString());
		this.tfInput.setText("");
	}

	/** {@inheritDoc} */
	public void assignFocus()
	{
		this.tfInput.requestFocusInWindow();
		this.spInstParts.getVerticalScrollBar().setValue(0);
		this.spInstParts.getHorizontalScrollBar().setValue(0);
	}

	/**
	 * Invoked when {@link #pbClear}is selected. Clears the current search
	 * criteria.
	 */
	public void clear()
	{
		this.lblSearch.setText("Search Criteria: ");
		this.fieldSearchCriteria.clear();
		this.pbClear.setEnabled(false);
		this.pbSearch.setEnabled(false);
		this.tfInput.requestFocusInWindow();
	}

	/**
	 * Returns the title for the <code>MFSFrame</code> when this
	 * <code>MFSPanel</code> is displayed.
	 * @return the title for the <code>MFSFrame</code> when this
	 *         <code>MFSPanel</code> is displayed
	 */
	public String getMFSPanelTitle()
	{
		return SCREEN_NAME;
	}

	//~4A New method
	/**
	 * Loads the list model for the installed parts list.
	 * @param data the data used to load the list model
	 * @return the size of the loaded list model
	 */
	public int loadListModel(String data)
	{
		MFSComponentListModel listModel = (MFSComponentListModel) this.lstInstParts.getModel();
		listModel.loadListModel(data, new MFSHeaderRec());
		removeCurrNmbrParts(listModel);
		return listModel.size();
	}

	/**
	 * Removes <code>MFSComponentRec</code> s for the current operation from
	 * the sepecified <code>listModel</code>
	 * @param listModel an <code>MFSComponentListModel</code> filled with
	 *        <code>MFSComponentRec</code> s
	 */
	private void removeCurrNmbrParts(MFSComponentListModel listModel)
	{
		int index = 0;
		while (index < listModel.size())
		{
			MFSComponentRec cmp = listModel.getComponentRecAt(index);
			if (cmp.getNmbr().equals(this.fieldCurrNmbr))
			{
				listModel.remove(index);
			}
			else
			{
				index++;
			}
		}
	}

	/** Runs the barcode decoder on the search criteria input text. */
	public void runDecodeBarcode()
	{
		//Used to not show an error message if the Decode return 4 or more values
		int count = 0;

		final MFSConfig config = MFSConfig.getInstance();
		MFSBCBarcodeDecoder barcode = MFSBCBarcodeDecoder.getInstance();
		barcode.setMyBarcodeInput(this.tfInput.getText());

		barcode.setMyBCPartObject(new MFSBCPartObject());

		/* setup barcode indicator values */
		MFSBCIndicatorValue bcIndVal = new MFSBCIndicatorValue();

		bcIndVal.setFillFieldItem(true);
		String sni = config.getConfigValue("DECODESN"); //$NON-NLS-1$
		if (!sni.equals(MFSConfig.NOT_FOUND))
		{
			bcIndVal.setCSNI(sni);
		}
		else
		{
			bcIndVal.setCSNI("1"); //$NON-NLS-1$
		}

		String eci = config.getConfigValue("DECODEEC"); //$NON-NLS-1$
		if (!eci.equals(MFSConfig.NOT_FOUND))
		{
			bcIndVal.setECRI(eci);
		}
		else
		{
			bcIndVal.setECRI("1"); //$NON-NLS-1$
		}
		bcIndVal.setCCAI("1"); //$NON-NLS-1$
		bcIndVal.setCMTI("1"); //$NON-NLS-1$
		bcIndVal.setCOOI("1"); //$NON-NLS-1$
		bcIndVal.setHDR_CODE("            "); //$NON-NLS-1$
		bcIndVal.setITEM("            "); //$NON-NLS-1$
		bcIndVal.setMACI("1"); //$NON-NLS-1$
		bcIndVal.setMATP("1"); //$NON-NLS-1$
		bcIndVal.setMCSN("1"); //$NON-NLS-1$
		bcIndVal.setMMDL("1"); //$NON-NLS-1$
		bcIndVal.setPNRI("1"); //$NON-NLS-1$
		bcIndVal.setPRLN("        "); //$NON-NLS-1$
		barcode.setMyBCIndicatorValue(bcIndVal);

		barcode.decodeBarcodeFor(this);

		count = 0;
		if (!barcode.getBCMyPartObject().getPN().equals("")) //$NON-NLS-1$
		{
			addSearchCriteria(barcode.getBCMyPartObject().getPN());
			count++;
		}
		if (!barcode.getBCMyPartObject().getSN().equals("")) //$NON-NLS-1$
		{
			addSearchCriteria(barcode.getBCMyPartObject().getSN());
			count++;
		}
		if (!barcode.getBCMyPartObject().getEC().equals("")) //$NON-NLS-1$
		{
			addSearchCriteria(barcode.getBCMyPartObject().getEC());
			count++;
		}
		if ((!barcode.getBCMyPartObject().getCA().equals("")) && (count < 3)) //$NON-NLS-1$
		{
			addSearchCriteria(barcode.getBCMyPartObject().getCA());
			count++;
		}
		if ((!barcode.getBCMyPartObject().getPL().equals("")) && (count < 3)) //$NON-NLS-1$
		{
			addSearchCriteria(barcode.getBCMyPartObject().getPL());
			count++;
		}
		if ((!barcode.getBCMyPartObject().getON().equals("")) && (count < 3)) //$NON-NLS-1$
		{
			addSearchCriteria(barcode.getBCMyPartObject().getON());
			count++;
		}
		if ((!barcode.getBCMyPartObject().getDEF().equals("")) && (count < 3)) //$NON-NLS-1$
		{
			addSearchCriteria(barcode.getBCMyPartObject().getDEF());
			count++;
		}
		if ((!barcode.getBCMyPartObject().getMSN().equals("")) && (count < 3)) //$NON-NLS-1$
		{
			addSearchCriteria(barcode.getBCMyPartObject().getMSN());
			count++;
		}
		if ((!barcode.getBCMyPartObject().getPC().equals("")) && (count < 3)) //$NON-NLS-1$
		{
			addSearchCriteria(barcode.getBCMyPartObject().getPC());
			count++;
		}
		if ((!barcode.getBCMyPartObject().getMTC().equals("")) && (count < 3)) //$NON-NLS-1$
		{
			addSearchCriteria(barcode.getBCMyPartObject().getMTC());
			count++;
		}
		if ((!barcode.getBCMyPartObject().getMD().equals("")) && (count < 3)) //$NON-NLS-1$
		{
			addSearchCriteria(barcode.getBCMyPartObject().getMD());
			count++;
		}
		if ((!barcode.getBCMyPartObject().getWU().equals("")) && (count < 3)) //$NON-NLS-1$
		{
			addSearchCriteria(barcode.getBCMyPartObject().getWU());
			count++;
		}
		if ((!barcode.getBCMyPartObject().getCT().equals("")) && (count < 3)) //$NON-NLS-1$
		{
			addSearchCriteria(barcode.getBCMyPartObject().getCT());
			count++;
		}
		if ((!barcode.getBCMyPartObject().getQT().equals("")) && (count < 3)) //$NON-NLS-1$
		{
			addSearchCriteria(barcode.getBCMyPartObject().getQT());
			count++;
		}
		if ((!barcode.getBCMyPartObject().getCO().equals("")) && (count < 3)) //$NON-NLS-1$
		{
			addSearchCriteria(barcode.getBCMyPartObject().getCO());
			count++;
		}
		if ((!barcode.getBCMyPartObject().getMC().equals("")) && (count < 3)) //$NON-NLS-1$
		{
			addSearchCriteria(barcode.getBCMyPartObject().getMC());
			count++;
		}
		if (count == 0) /* ~03A */
		{
			addSearchCriteria(this.tfInput.getText());
		}
	}

	/** Executes the rework logic for the selected part. */
	public void rwk_part()
	{
		try
		{
			removeMyListeners();
			MFSComponentRec currComp = (MFSComponentRec) this.lstInstParts.getSelectedValue();
			if (this.fieldCurrNmbr.equals(currComp.getNmbr()))
			{
				String erms = "Invalid Part to Rework, Part is at Current Operation.";
				IGSMessageBox.showOkMB(getParentFrame(), null, erms, null);
			}
			else
			{
				MFSComponentListModel listModel = (MFSComponentListModel) this.lstInstParts.getModel();
				MFSRework rwk = new MFSRework(this, this.lstInstParts, listModel,
						this.fieldHeaderRec, currComp);
				rwk.rework(MFSRwkDialog.LF_PARTIAL);
			}
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}

		addMyListeners();
		this.tfInput.requestFocusInWindow(); /* ~02A */
	}

	// New method ~02
	/** Searchs for a part that matches all the search criteria. */
	public void search()
	{
		//~4C Method was redone to avoid scrolling while searching.
		//Start at row after selected row (startRow).
		//To search the entire table, mod by listSize to wrap to top
		//Stop when a match is found or currentRow == startRow
		//Uppercase the criteria and the records to perform
		// a case insensitive search
		final ListModel model = this.lstInstParts.getModel();
		final int modelSize = model.getSize();
		final int criteriaSize = this.fieldSearchCriteria.size();
		final int startRow = (this.lstInstParts.getSelectedIndex() + 1) % modelSize;
		int currentRow = startRow;
		boolean found = false;

		String criteria[] = new String[criteriaSize];
		for (int i = 0; i < criteriaSize; i++)
		{
			criteria[i] = this.fieldSearchCriteria.elementAt(i).toString().toUpperCase();
		}

		do
		{
			found = true;
			String record = model.getElementAt(currentRow).toString().toUpperCase();
			for (int i = 0; i < criteriaSize; i++)
			{
				if (record.indexOf(criteria[i]) == -1)
				{
					found = false;
					currentRow = (currentRow + 1) % modelSize;
					break;
				}
			}
		}
		while (found == false && currentRow != startRow);

		if (found)
		{
			this.lstInstParts.setSelectedIndex(currentRow);
			this.lstInstParts.ensureIndexIsVisible(currentRow);
		}
		else
		{
			String erms = "No records match the Search Criteria.";
			IGSMessageBox.showOkMB(getParentFrame(), null, erms, null);
			this.tfInput.requestFocusInWindow();
		}
	}

	/**
	 * Invoked when an action occurs.
	 * @param ae the <code>ActionEvent</code>
	 */
	public void actionPerformed(ActionEvent ae)
	{
		try
		{
			final Object source = ae.getSource();
			if (source == this.pbRework)
			{
				rwk_part();
			}
			else if (source == this.pbCancel)
			{
				getParentFrame().restorePreviousScreen(this);
			}
			else if (source == this.pbClear)
			{
				clear();
			}
			else if (source == this.pbSearch)
			{
				search();
			}
		}
		catch (Throwable throwable)
		{
			throwable.printStackTrace();
		}
	}

	/**
	 * Invoked when a key is pressed.
	 * @param ke the <code>KeyEvent</code>
	 */
	public void keyPressed(KeyEvent ke)
	{

		final int keyCode = ke.getKeyCode();
		final Object source = ke.getSource();
		if (keyCode == KeyEvent.VK_ENTER)
		{
			if (source instanceof JButton)
			{
				JButton button = (JButton) source;
				button.requestFocusInWindow();
				button.doClick();
			}
			else if (source == this.tfInput)
			{
				if (this.tfInput.getText().length() != 0)
				{
					this.tfInput.requestFocusInWindow();
					runDecodeBarcode();
				}
			}
		}
		else if (keyCode == KeyEvent.VK_F6)
		{
			this.pbRework.requestFocusInWindow();
			this.pbRework.doClick();
		}
		else if (keyCode == KeyEvent.VK_ESCAPE)
		{
			this.pbCancel.requestFocusInWindow();
			this.pbCancel.doClick();
		}
		else if (keyCode == KeyEvent.VK_PAGE_DOWN)
		{
			MFSScroller.scrollDown(this.spInstParts, this.lstInstParts);
			ke.consume();
		}
		else if (keyCode == KeyEvent.VK_PAGE_UP)
		{
			MFSScroller.scrollUp(this.spInstParts, this.lstInstParts);
			ke.consume();
		}
		else if (keyCode == KeyEvent.VK_RIGHT && source != this.tfInput)
		{
			JScrollBar scroll = this.spInstParts.getHorizontalScrollBar();
			int value = scroll.getValue();
			int max = scroll.getMaximum();
			if (value + 100 > max)
			{
				scroll.setValue(max);
			}
			else
			{
				scroll.setValue(value + 50);
			}
		}
		else if (keyCode == KeyEvent.VK_LEFT && source != this.tfInput)
		{
			JScrollBar scroll = this.spInstParts.getHorizontalScrollBar();
			int value = scroll.getValue() - 50;
			if (value < 0)
			{
				scroll.setValue(0);
			}
			else
			{
				scroll.setValue(value);
			}
		}
	}
}
