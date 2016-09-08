/* © Copyright IBM Corporation 2006, 2007. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2006-09-22      26994PT  JL Woodward      -Initial version
 * 2007-01-25      34242JR  R Prechel        -Java 5 version
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.dialog;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.util.Vector;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;

import mfsxml.MISSING_XML_TAG_EXCEPTION;
import mfsxml.MfsXMLDocument;
import mfsxml.MfsXMLParser;

import com.ibm.rchland.mfgapps.client.utils.messagebox.IGSMessageBox;
import com.ibm.rchland.mfgapps.mfsclient.MFSActionableDialog;
import com.ibm.rchland.mfgapps.mfsclient.MFSFrame;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSConstants;
import com.ibm.rchland.mfgapps.mfscommon.MFSConfig;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSComm;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSTransaction;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSXmlTransaction;

/**
 * <code>MFSUsersFlagAuthDialog</code> is the <code>MFSActionableDialog</code>
 * used to display user flags authorization.
 * @author The MFS Client Development Team
 */
public class MFSUsersFlagAuthDialog
	extends MFSActionableDialog
{
	private static final long serialVersionUID = 1L;
	/** The Show All <code>JRadioButton</code>. */
	private JRadioButton rbShowAll = createRadioButton("Show All", true);
	
	/** The Show Quality <code>JRadioButton</code>. */
	private JRadioButton rbQuality = createRadioButton("Show Quality", false);
	
	/** The Show Test <code>JRadioButton</code>. */
	private JRadioButton rbTest = createRadioButton("Show Test", false);
	
	/** The flags <code>JTable</code>. */
	private JTable tblFlags = new JTable();
	
	/** The <code>JScrollPane</code> that contains the flags table. */
	private JScrollPane spFlags = new JScrollPane(this.tblFlags,
			ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
			ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
	
	/** The refresh <code>JButton</code>. */
	private JButton pbRefresh = createButton("F5=Refresh");
	
	/** The cancel <code>JButton</code>. */
	private JButton pbCancel = createButton("F3=Cancel");
	
	/** The <code>TableModel</code> for the flags table. */
	private FlagsTableModel flagsTableModel = new FlagsTableModel();
	
	/** The index of the User Group <code>TableColumn</code>. */
	private static final int COL_UGRP = 0;
	/** The index of the Flag ID <code>TableColumn</code>. */
	private static final int COL_FGID = 1;
	/** The index of the Flag Values <code>TableColumn</code>. */
	private static final int COL_FVAL = 2;
	/** The index of the Type <code>TableColumn</code>. */
	private static final int COL_FTYP = 3;
	
	/**
	 * Constructs a new <code>MFSUsersFlagAuthDialog</code>.
	 * @param parent the <code>MFSFrame</code> that caused this
	 *        <code>MFSUsersFlagAuthDialog</code> to be displayed
	 */
	public MFSUsersFlagAuthDialog(MFSFrame parent)
	{
		super(parent, "Users Flags Authorization");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		this.tblFlags.setModel(this.flagsTableModel);
		createLayout();
		addMyListeners();
	}
	
	/** Adds this dialog's <code>Component</code>s to the layout. */
	private void createLayout()
	{
		this.tblFlags.addColumn(createTableColumn(COL_UGRP, "User Group", 80));
		this.tblFlags.addColumn(createTableColumn(COL_FGID,"Flag ID", 80));
		this.tblFlags.addColumn(createTableColumn(COL_FVAL,"Flag Value(s)", 280));
		this.tblFlags.addColumn(createTableColumn(COL_FTYP,"Type", 32));
		
		String user = MFSConfig.getInstance().getConfigValue("USER").trim(); //$NON-NLS-1$
		JLabel userLabel = createLabel("User ID:   " + user);
		
		JPanel radioButtonPanel = new JPanel(new FlowLayout());
		radioButtonPanel.setBackground(MFSConstants.PRIMARY_BACKGROUND_COLOR);
		radioButtonPanel.add(this.rbShowAll);
		radioButtonPanel.add(this.rbQuality);
		radioButtonPanel.add(this.rbTest);
		
		ButtonGroup optionsGroup = new ButtonGroup();
		optionsGroup.add(this.rbShowAll);
		optionsGroup.add(this.rbQuality);
		optionsGroup.add(this.rbTest);
		
		JPanel contentPane = new JPanel(new GridBagLayout());
		contentPane.setBackground(MFSConstants.PRIMARY_BACKGROUND_COLOR);
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 2, 1, 0.5, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0);

		/* User Label goes on the top, centered */
		contentPane.add(userLabel, gbc);
			
		/* Now below the user label, goes the radio buttons */
		gbc.gridy = 1;
		contentPane.add(radioButtonPanel, gbc);
		
		/* Here comes the flags table */ 
		gbc.weighty = 1.0;   /* requests extra vertical space */
		gbc.gridy = 2;
		gbc.fill = GridBagConstraints.BOTH;
		contentPane.add(this.spFlags, gbc);
		
		/* Cancel and refresh buttons */
		gbc.gridy = 3;
		gbc.gridwidth = 1;
		gbc.weightx = 1.0;
		gbc.weighty = 0.0;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(10, 0, 10, 0);
		
		contentPane.add(this.pbRefresh, gbc);
		
		gbc.gridx = 1;
		contentPane.add(this.pbCancel, gbc);
		
		setContentPane(contentPane);
		setButtonDimensions(this.pbRefresh, this.pbCancel);
		pack();
	}
	
	/**
	 * Creates a <code>JRadioButton</code>.
	 * @param text the text for the <code>JRadioButton</code>
	 * @param isSelected <code>true</code> if the button is selected
	 * @return the <code>JRadioButton</code>
	 */
	private JRadioButton createRadioButton(String text, boolean isSelected)
	{
		JRadioButton result = new JRadioButton(text);
		result.setActionCommand(text);
		result.setSelected(isSelected);
		result.setBackground(MFSConstants.PRIMARY_BACKGROUND_COLOR);
		return result;
	}
	
	/**
	 * Creates a <code>TableColumn</code>.
	 * @param modelIndex the index of the <code>TableColumn</code>
	 * @param header the header text of the <code>TableColumn</code>
	 * @param width the width of the <code>TableColumn</code>
	 * @return the <code>TableColumn</code>
	 */
	private TableColumn createTableColumn(int modelIndex, String header, int width)
	{
		TableColumn result = new TableColumn(modelIndex);
		result.setModelIndex(modelIndex);
		result.setHeaderValue(header);
		result.setResizable(true);
		result.setPreferredWidth(width);
		result.setWidth(width);
		return result;
	}
	
	/** Adds the listeners to this dialog's <code>Component</code>s. */
	private void addMyListeners()
	{
		this.pbRefresh.addActionListener(this);
		this.pbCancel.addActionListener(this);
		this.rbShowAll.addActionListener(this);
		this.rbQuality.addActionListener(this);
		this.rbTest.addActionListener(this);
		
		this.pbRefresh.addKeyListener(this);
		this.pbCancel.addKeyListener(this);
		this.rbShowAll.addKeyListener(this);
		this.rbQuality.addKeyListener(this);
		this.rbTest.addKeyListener(this);
		this.tblFlags.addKeyListener(this);
		this.spFlags.addKeyListener(this);
	}
	
	/** Removes the listeners from this dialog's <code>Component</code>s. */
	private void removeMyListeners()
	{
		this.pbRefresh.removeActionListener(this);
		this.pbCancel.removeActionListener(this);
		this.rbShowAll.removeActionListener(this);
		this.rbQuality.removeActionListener(this);
		this.rbTest.removeActionListener(this);
		
		this.pbRefresh.removeKeyListener(this);
		this.pbCancel.removeKeyListener(this);
		this.rbShowAll.removeKeyListener(this);
		this.rbQuality.removeKeyListener(this);
		this.rbTest.removeKeyListener(this);
		this.tblFlags.removeKeyListener(this);
		this.spFlags.removeKeyListener(this);
	}
	
	/** Refreshes the RV10 data that is displayed in the table. */
	private void refreshRV10Data()
	{	
		MfsXMLDocument xml_data = new MfsXMLDocument("RTVUSRFLGS"); //$NON-NLS-1$
		xml_data.addOpenTag("DATA"); //$NON-NLS-1$
		xml_data.addCompleteField("USER", MFSConfig.getInstance().getConfigValue("USER").trim()); //$NON-NLS-1$ //$NON-NLS-2$
		xml_data.addCloseTag("DATA"); //$NON-NLS-1$
		xml_data.finalizeXML();
		
		MFSTransaction rtvusrflgs = new MFSXmlTransaction(xml_data.toString());
		rtvusrflgs.setActionMessage("Retrieving User Flags, Please Wait...");
		MFSComm.getInstance().execute(rtvusrflgs, this);
		
		this.flagsTableModel.buildFlagsTable(rtvusrflgs.getOutput());
		this.flagsTableModel.fireTableDataChanged();

		if(rtvusrflgs.getReturnCode()!=0)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, rtvusrflgs.getErms(), null);
		}
	}
	
	/**
	 * Invoked when an action occurs.
	 * @param ae the <code>ActionEvent</code>
	 */
	public void actionPerformed(ActionEvent ae) 
	{
		final Object source = ae.getSource();
		if (source == this.rbQuality)
		{
			this.flagsTableModel.setFlagTypesToShow(FlagsTableModel.ENUM_QUALITY);
		}
		else if (source == this.rbTest)
		{
			this.flagsTableModel.setFlagTypesToShow(FlagsTableModel.ENUM_TEST);
		}
		else if (source == this.rbShowAll)
		{
			this.flagsTableModel.setFlagTypesToShow(FlagsTableModel.ENUM_ALL);
		}
		else if(source== this.pbRefresh)
		{
			removeMyListeners();
			refreshRV10Data();
			addMyListeners();
		}
		else if(source==this.pbCancel)
		{
			this.dispose();
		}

	}
	
	/**
	 * Invoked when a key is pressed.
	 * @param ke the <code>KeyEvent</code>
	 */
	public void keyPressed(KeyEvent ke) 
	{
		final int keycode = ke.getKeyCode();
		final Object source = ke.getSource();
		if(keycode == KeyEvent.VK_ENTER)
		{
			if(source instanceof AbstractButton)
			{
				AbstractButton button = (AbstractButton) source;
				button.requestFocusInWindow();
				button.doClick();
			}
		}
		else if ((keycode == KeyEvent.VK_ESCAPE) || (keycode == KeyEvent.VK_F3))
		{
			this.pbCancel.requestFocusInWindow();
			this.pbCancel.doClick();
		}
		else if ((keycode == KeyEvent.VK_F5))
		{
			this.pbRefresh.requestFocusInWindow();
			this.pbRefresh.doClick();
		}
	}
	
	/**
	 * Invoked the first time a window is made visible.
	 * <p>
	 * Calls {@link #refreshRV10Data()}.
	 * @param we the <code>WindowEvent</code>
	 */
	public void windowOpened(WindowEvent we) 
	{
		refreshRV10Data();
	}
	
	/**
	 * <code>FlagsTableModel</code> is the <code>TableModel</code> for the flags table.
	 * @author The MFS Client Development Team
	 */
	private class FlagsTableModel extends AbstractTableModel
	{
		/** The <code>Vector</code> with all the row data. */
		private Vector rowsAll = new Vector();
		/** The <code>Vector</code> with the quality row data. */
		private Vector rowsQuality = new Vector();
		/** The <code>Vector</code> with the test row data. */
		private Vector rowsTest = new Vector();
		/** The currently displayed data. */
		String data[][];
		
		/** The number of columns in the table. */
		private static final int NUM_COLUMNS = 4;
		
		/** The enum value to display all data. */
		private static final int ENUM_ALL = 0;
		/** The enum value to display quality data. */
		private static final int ENUM_QUALITY = 1;
		/** The enum value to display test data. */
		private static final int ENUM_TEST = 2;
		/** Stores the enum value of the currently displayed type. */
		private int showFtyp = ENUM_ALL;  //default to display All flag types
		
		/** Constructs a new <code>FlagsTableModel</code>. */
		public FlagsTableModel()
		{
			super();
		}
		
		/**
		 * Builds the flags tables from the specified <code>xmlData</code>
		 * @param xmlData the XML data used to build the tables
		 */
		public void buildFlagsTable(String xmlData)
		{
			MfsXMLParser xmlParser = new MfsXMLParser(xmlData);
			this.rowsAll.removeAllElements();
			this.rowsQuality.removeAllElements();
			this.rowsTest.removeAllElements();
			Vector myRows = new Vector();
			boolean firstPass = true;
			boolean addRec = false;
			String tmpFvals = new String();
			String prevFgid = new String();
			String lastUgrp = new String();
			String lastFtyp = new String();
			
			try
			{
				MfsXMLParser fieldData = new MfsXMLParser(xmlParser.getNextField("REC", 0)); //$NON-NLS-1$
				while (fieldData.unparsedLength() > 0)
				{
					if(firstPass)
					{
						prevFgid = fieldData.getField("FGID"); //$NON-NLS-1$
						tmpFvals = fieldData.getField("FVAL").trim(); //$NON-NLS-1$
						firstPass = false;
					}
					else
					{
						if(!prevFgid.equals(fieldData.getField("FGID"))|| //$NON-NLS-1$
							!lastUgrp.equals(fieldData.getField("UGRP"))) //$NON-NLS-1$
						{
							myRows.add(lastUgrp);
							myRows.add(prevFgid);
							myRows.add(tmpFvals);
							myRows.add(lastFtyp);
							prevFgid = fieldData.getField("FGID"); //$NON-NLS-1$
							tmpFvals = fieldData.getField("FVAL").trim(); //$NON-NLS-1$
							addRec = true;
						}
						else
						{
							addRec = false;
							tmpFvals = tmpFvals + ",  " + fieldData.getField("FVAL").trim(); //$NON-NLS-1$ //$NON-NLS-2$
						}
					}
					lastUgrp = fieldData.getField("UGRP"); //$NON-NLS-1$
					lastFtyp = fieldData.getField("FTYP"); //$NON-NLS-1$
					
					if(addRec)
					{
						addToVectors(myRows);
					}
					
					fieldData.setUnparsedXML(xmlParser.getNextField("REC")); //$NON-NLS-1$
				}
			}
			catch (MISSING_XML_TAG_EXCEPTION mt)
			{
				mt.printStackTrace();
			}
			/* After read all XML, process the last one */
			myRows.add(lastUgrp);
			myRows.add(prevFgid);
			myRows.add(tmpFvals);
			myRows.add(lastFtyp);
			addToVectors(myRows);
		}
		
		/**
		 * Adds the <code>Vector</code> of row data.
		 * @param myRows the <code>Vector</code> of row data
		 */
		private void addToVectors(Vector myRows)
		{
			Vector tempRow = (Vector) myRows.clone();
			this.rowsAll.add(tempRow);
			if(myRows.elementAt(COL_FTYP).equals("T")) //$NON-NLS-1$
			{
				this.rowsTest.add(tempRow);
			}
			else
			{
				this.rowsQuality.add(tempRow);
			}	
			myRows.removeAllElements();
		}
		
		/**
		 * Returns 0. 
		 * @return 0
		 */
		public int getColumnCount() 
		{ 
			return 0;
		}
		
		/**
		 * Returns the number of rows. 
		 * @return the number of rows
		 */
		public int getRowCount()
		{ 
			int retSize = 0;
			switch (this.showFtyp)
			{
				case ENUM_ALL:
					retSize = this.rowsAll.size();
					break;
				case ENUM_TEST:
					retSize = this.rowsTest.size();
					break;
				case ENUM_QUALITY:
					retSize = this.rowsQuality.size();
					break;
				
			}
			return retSize;
		}
		
		/**
		 * Returns the value for the cell at <code>row</code> index and
		 * <code>col</code> index.
		 * @param row the index of the row
		 * @param col the index of the column
		 * @return the <code>Object</code> value of the specified cell
		 */
		public Object getValueAt(int row, int col) 
		{ 
			int data_index = 0;
			Vector myRows = new Vector();
			Vector tmpRows = new Vector();
			switch (this.showFtyp)
			{
				case ENUM_ALL:
					tmpRows = (Vector)this.rowsAll.clone();
					break;
				case ENUM_TEST:
					tmpRows = (Vector)this.rowsTest.clone();
					break;
				case ENUM_QUALITY:
					tmpRows = (Vector)this.rowsQuality.clone();
					break;
			}
			
			this.data = new String[tmpRows.size()][NUM_COLUMNS];

			for(data_index = 0; data_index < tmpRows.size(); data_index++)
			{
				myRows = (Vector)tmpRows.elementAt(data_index);
				for(int i = 0; i < NUM_COLUMNS; i++)
				{
					this.data[data_index][i] = (String)myRows.elementAt(i);
				}
			} 

			return this.data[row][col];
		}
		
		/**
		 * Set which flags to display
		 * @param enumFtyp the flag type
		 * 0 - Display all types of flags
		 * 1 - Display Quality flags only
		 * 2 - Display Test flags only
		 */
		public void setFlagTypesToShow(int enumFtyp)
		{
			this.showFtyp = enumFtyp;
			this.fireTableDataChanged();
		}
	}
}
