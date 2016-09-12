/* @ Copyright IBM Corporation 2005, 2010. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2008-08-29      31091JM  D. Fichtinger    initial version
 * 2008-10-07      42932JM  D. Fichtinger    tweak the na logic a bit to prevent CRCOOC
 *              ~1                             getting an na in there
 * 2010-11-01  ~02 49513JM	Toribio H.   	 -Make RTV_CTRY Cacheable                    
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.dialog;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.TableModelEvent;
import javax.swing.table.TableColumn;

import mfsxml.MISSING_XML_TAG_EXCEPTION;
import mfsxml.MfsXMLParser;

import com.ibm.rchland.mfgapps.client.utils.messagebox.IGSMessageBox;
import com.ibm.rchland.mfgapps.client.utils.table.IGSCellInformation;
import com.ibm.rchland.mfgapps.client.utils.table.IGSSortableTableModel;
import com.ibm.rchland.mfgapps.mfsclient.MFSActionableDialog;
import com.ibm.rchland.mfgapps.mfsclient.MFSFrame;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSConstants;
import com.ibm.rchland.mfgapps.mfscommon.comm.trx.RTV_CTRY;

/**
 * <code>MFSWorkUnitLocDisplayDialog</code> is the <code>MFSDialog</code>
 * used to display work unit locations.
 * @author The MFS Client Development Team
 */
@SuppressWarnings("serial")
public class MFSReductionPartListDialog
	extends MFSActionableDialog
	//implements IGSCellInformationProcessor
{	
	private static final String QUESTION_MARKS = "??";  //$NON-NLS-1$
	
	private static final String NOT_APPLICABLE = "na";  //$NON-NLS-1$
	
	/** The Remove <code>JButton</code>. */
	private JButton pbMove = createButton("Move", 'M'); //$NON-NLS-1$

	/** The COO List<code>JButton</code>. */
	private JButton pbCOOCList = createButton("Choose COO", 'H'); //$NON-NLS-1$
	
	/** The Cancel <code>JButton</code>. */
	private JButton pbCancel = createButton("Cancel", 'C'); //$NON-NLS-1$

	/** The <code>JTable</code> that displays parts. */
	private JTable partsTable = new JTable();
	
	/** Set <code>String</code> to collection of MCTL's/CRCT selected by user when pressing remove button */
	private String partsString = new String(""); //$NON-NLS-1$
	
	/** The active container <code>String</code> that displays the container number in the header of dialog*/
	private String container = new String(""); //$NON-NLS-1$

	/** The part number <code>String</code> that gets displayed in the header of dialog*/
	private String partNumber = new String(""); //$NON-NLS-1$

	/** The part description <code>String</code> that gets displayed in the header of dialog*/
	private String partDescription = new String(""); //$NON-NLS-1$
	
	/** The COO List of Country Codes */
	private String cooListWithCountryNames = new String(""); //$NON-NLS-1$
	
	/** The <code>JScrollPane</code> that contains the list of work unit locations. */
	private JScrollPane spPartsTable = new JScrollPane(this.partsTable);

	/**
	 * Constructs a new <code>MFSWorkUnitLocDisplayDialog</code>.
	 * @param parent the <code>MFSFrame</code> that caused this
	 *        <code>MFSWorkUnitLocDisplayDialog</code> to be displayed
	 */
	public MFSReductionPartListDialog(MFSFrame parent,String title,String partNumber,
			String partDescription,String container)
	{
		super(parent, title);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.partNumber = partNumber;
		this.partDescription = partDescription;
		this.container = container;
		createLayout();
		addMyListeners();
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
	
	/** Adds this dialog's <code>Component</code>s to the layout. */
	private void createLayout()
	{

		this.partsTable.addColumn(createTableColumn(0, "Plug Info", 40)); //$NON-NLS-1$
		this.partsTable.addColumn(createTableColumn(1, "Sequence Number", 20)); //$NON-NLS-1$
		this.partsTable.addColumn(createTableColumn(2, "Country of Origin", 20)); //$NON-NLS-1$

		this.partsTable.setFont(MFSConstants.MEDIUM_MONOSPACED_FONT);
		this.partsTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

		JPanel contentPane = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, 
				new Insets(10, 4, 0, 4), 0, 0);
		
		JLabel partNumberLabel = createLabel("Part Number: " + this.partNumber ); //$NON-NLS-1$
		JLabel partDescriptionLabel = createLabel("Description: " + this.partDescription ); //$NON-NLS-1$
		JLabel containerNameLabel = createLabel("Container: " + this.container); //$NON-NLS-1$
		
		gbc.gridy = 1;
		gbc.weighty = 0.0;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(15, 4, 5, 4);
		contentPane.add(partNumberLabel, gbc);
		
		gbc.gridy += 1;
		gbc.weighty = 0.0;
		gbc.insets = new Insets(0, 4, 5, 4);
		contentPane.add(partDescriptionLabel, gbc);
		
		gbc.gridy += 1;
		gbc.weighty = 0.0;
		gbc.insets = new Insets(0, 4, 5, 4);
		contentPane.add(containerNameLabel, gbc);

		gbc.gridy+=1;
		gbc.weighty = 1.0;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(10, 4, 20, 4);
		contentPane.add(this.spPartsTable, gbc);

		JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 20, 0));
		buttonPanel.add(this.pbMove);
		buttonPanel.add(this.pbCOOCList);
		buttonPanel.add(this.pbCancel);

		gbc.gridy+=1;
		gbc.weighty = 0.0;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(0, 4, 5, 4);
		contentPane.add(buttonPanel, gbc);

		this.setContentPane(contentPane);
	}

	/** Adds the listeners to this dialog's <code>Component</code>s. */
	private void addMyListeners()
	{
		this.pbCancel.addActionListener(this);
		this.pbMove.addActionListener(this);
		this.pbCOOCList.addActionListener(this);
	}

	/** returns pseudo XML string */
	public String getPartsString()
	{
		return this.partsString;
	}

	/**
	 * Loads the work unit location list using the specified data.
	 * @param inputData the data used to load the list
	 * @return <code>true</code> on success; <code>false</code> on failure
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void loadList(MfsXMLParser parser)
	{
		try
		{
			Vector currentTableData = new Vector();
			try
			{
				if (parser.contains("REC")){ //$NON-NLS-1$
					MfsXMLParser rowParser = new MfsXMLParser(parser.getNextField("REC")); //$NON-NLS-1$
					while (rowParser.contains("INPN")){ //$NON-NLS-1$
						//set up a row of error data


						Object[] row = new Object[] 
						{
							createCellInformation(rowParser),	
							rowParser.getFieldOnly("INSQ"), //$NON-NLS-1$
							createCOOCCellInformation(rowParser)
						};

						currentTableData.add(row);
						
						rowParser = new MfsXMLParser(parser.getNextField("REC")); //$NON-NLS-1$
					}
				}
			}
			catch (MISSING_XML_TAG_EXCEPTION mte)
			{
				IGSMessageBox.showOkMB(this, null, null, mte);
			}

			String[] columnNames = new String[] {"Plug Info","Sequence Number","Country of Origin"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					
			IGSSortableTableModel tableModel = new IGSSortableTableModel(currentTableData,columnNames);
			this.partsTable.setModel(tableModel);
			tableModel.setSortTableHeader(this.partsTable.getTableHeader());
			
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(this, null, null, e);
		}
		finally
		{
			pack();
		}
	}
	/**
	 * Utility Function that constructs a COOC cell to display - parses COOLIST values and comma delimits them */
	private String createCOOCCellInformation(MfsXMLParser rowParser) 
	{
		StringBuffer coocDisplay = new StringBuffer(""); //$NON-NLS-1$
		try
		{
			MfsXMLParser coocParser = new MfsXMLParser(rowParser.getNextField("COOLIST")); //$NON-NLS-1$
			String cooc = coocParser.getNextField("COOC"); //$NON-NLS-1$
			while (!cooc.equals("")) //$NON-NLS-1$
			{
				if(coocDisplay.length() != 0)
				{
					coocDisplay.append(","); //$NON-NLS-1$
				}
				coocDisplay.append(cooc);
				cooc = coocParser.getNextField("COOC"); //$NON-NLS-1$
			}
			
		}
		catch (MISSING_XML_TAG_EXCEPTION mte)
		{
			IGSMessageBox.showOkMB(this, null, null, mte);
		}
		return coocDisplay.toString();

	}
	/**
	 * Utility Function that constructs a special type of cell - can store the necessary details
	 * we need to perform the actions necessary, but doesn't display that unneeded stuff
	 * @param parser the data we need to capture
	 * @return <code>IGSCellInformation</code> on success
	 */
	private IGSCellInformation createCellInformation(final MfsXMLParser parser)
	{
		String displayText;
		String mctl = null;
		String crct = null;
		String cooList = null;
		try
		{
			displayText = constructPlugInfo(parser);
			
			mctl = parser.getFieldOnly("MCTL").trim(); //$NON-NLS-1$
			crct = parser.getFieldOnly("CRCT").trim(); //$NON-NLS-1$
			cooList = parser.getFieldOnly("COOLIST").trim(); //$NON-NLS-1$
			
		}
		catch (MISSING_XML_TAG_EXCEPTION mte)
		{
			displayText = "Tag Missing!"; //$NON-NLS-1$
		}

		//return new IGSCellInformation(this, text, new String[] {parser.getUnparsedXML()});
		return new IGSCellInformation(null, displayText, new String[] {mctl,crct,cooList});
	}

		
	/**
	 * Utility Function that constructs what should be displayed for plug info
	 * @param rowParser the data used to load the list
	 * @return <code>plug info</code> on success
	 */

	private String constructPlugInfo(MfsXMLParser rowParser) 
	{
		StringBuffer plugInfo = new StringBuffer(""); //$NON-NLS-1$
		try
		{
			plugInfo.append((rowParser.getFieldOnly("PLL1").trim()+ "    ").substring(0,4)); //$NON-NLS-1$ //$NON-NLS-2$
			plugInfo.append("-"); //$NON-NLS-1$
			plugInfo.append((rowParser.getFieldOnly("PLL2").trim()+ "    ").substring(0,4)); //$NON-NLS-1$ //$NON-NLS-2$
			plugInfo.append("-"); //$NON-NLS-1$
			plugInfo.append((rowParser.getFieldOnly("PLL3").trim()+ "    ").substring(0,4)); //$NON-NLS-1$ //$NON-NLS-2$
			plugInfo.append("-"); //$NON-NLS-1$
			plugInfo.append((rowParser.getFieldOnly("PLL4").trim()+ "    ").substring(0,4)); //$NON-NLS-1$ //$NON-NLS-2$
			plugInfo.append("-"); //$NON-NLS-1$
			plugInfo.append((rowParser.getFieldOnly("PLL5").trim()+ "    ").substring(0,4)); //$NON-NLS-1$ //$NON-NLS-2$
		}
		catch (MISSING_XML_TAG_EXCEPTION mte)
		{
			IGSMessageBox.showOkMB(this, null, null, mte);
		}

		return plugInfo.toString();
	}

	/**
	 * Invoked on a Choose COO Button Click 
	 * @return
	 */
	private void handleCooSelection()
	{
		if(this.cooListWithCountryNames.equals("")) //$NON-NLS-1$
		{
			RTV_CTRY rtvCTRY = new RTV_CTRY(this); //~02
			rtvCTRY.setInputPN(this.partNumber);
			if (rtvCTRY.execute())
			{
				if(rtvCTRY.getOutputCooList().equals("")) //$NON-NLS-1$
				{
					IGSMessageBox.showOkMB(this, null, "No Countries found for the P/N in SAPPPC10 file.", null); //$NON-NLS-1$
				}	
				else
				{	
					this.cooListWithCountryNames = rtvCTRY.getOutputCooList();
				}					
			}
			else
			{
				IGSMessageBox.showOkMB(this, null, rtvCTRY.getErrorMessage(), null);
			}			
		}
		if(!this.cooListWithCountryNames.equals("")) //$NON-NLS-1$
		{	
			
			MFSCooDialog cooJD = new MFSCooDialog(getParentFrame(), false);
			cooJD.loadCooListModel(this.cooListWithCountryNames);
			cooJD.setLocationRelativeTo(this);
			cooJD.setVisible(true);
			if (cooJD.getProceedSelected())
			{
				// set cooselection to the first 2 characters from the selection
				String cooselection = cooJD.getSelectedListOption().substring(0, 2);
				if (cooselection.equals("  ")) //$NON-NLS-1$
				{
					cooselection = QUESTION_MARKS;
				}
				//System.out.println(this.partsTable.getValueAt(this.partsTable.getSelectedRow(),2));
				IGSSortableTableModel tm = (IGSSortableTableModel)this.partsTable.getModel();
				tm.changeValue((Object)cooselection, this.partsTable.getSelectedRow(),2);
				tm.fireTableModelEvent(new TableModelEvent(tm));
			}			
		}		
	}
	/**
	 * Invoked when an action occurs.
	 * @param ae the <code>ActionEvent</code>
	 */
	public void actionPerformed(ActionEvent ae)
	{
		if (ae.getSource() == this.pbCancel)
		{
			this.dispose();
		}
		else if (ae.getSource() == this.pbMove)
		{	
			int selectedRowCount = this.partsTable.getSelectedRowCount();
			if(selectedRowCount == 0)
			{
				IGSMessageBox.showOkMB(this, null, "No Parts Selected", null);				 //$NON-NLS-1$
			}
			else
			{	
				int selectedArray[] = this.partsTable.getSelectedRows();
				int arrayIndex = 0;
				StringBuffer sb = new StringBuffer();
				boolean allIsWell = true;
				for(arrayIndex=0;arrayIndex<selectedRowCount && allIsWell;arrayIndex++)
				{
					//constructing a pseudo xml string so caller can get hit the ground running
					IGSCellInformation temp = (IGSCellInformation)this.partsTable.getModel().getValueAt(selectedArray[arrayIndex],0);
					String coocCellValue = (String)this.partsTable.getModel().getValueAt(selectedArray[arrayIndex],2);
					
					if( coocCellValue.indexOf(",") != -1 || coocCellValue.equals(QUESTION_MARKS)) //$NON-NLS-1$
					{	
						IGSMessageBox.showOkMB(this, null, "You Must Specify a Valid Country Code For All Parts Being Removed!", null); //$NON-NLS-1$
						allIsWell = false;
					}
					else
					{	
						if(coocCellValue.equals(NOT_APPLICABLE))
						{
							coocCellValue = "  ";  /*~1 prevent crcooc getting na*/ //$NON-NLS-1$
						}
						sb.append("<REC><MCTL>" + temp.getValue(0) + "</MCTL><CRCT>" + temp.getValue(1)+ "</CRCT><COOC>" + coocCellValue + "</COOC><ACTION>MOVE</ACTION></REC>"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
					}	
				}
				if(allIsWell)
				{	
					this.partsString = sb.toString();
					this.dispose();
				}
			}	
		}
		else if(ae.getSource() == this.pbCOOCList)
		{
			if(this.partsTable.getSelectedRowCount() == 0)
			{
				IGSMessageBox.showOkMB(this, null, "Please Select a Part First!", null); //$NON-NLS-1$
			}
			else if(this.partsTable.getSelectedRowCount() > 1)
			{
				IGSMessageBox.showOkMB(this, null, "Please Select only 1 Part!", null); //$NON-NLS-1$
			}
			else
			{	
				handleCooSelection();
			}	
		}

	}

	/**
	 * Invoked the first time a window is made visible.
	 * @param we the <code>WindowEvent</code>
	 */
	public void windowOpened(WindowEvent we)
	{
	}
}
