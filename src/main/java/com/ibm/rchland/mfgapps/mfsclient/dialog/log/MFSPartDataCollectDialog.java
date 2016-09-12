/* @ Copyright IBM Corporation 2003, 2012. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2009-06-02      43813MC  Christopher O    -Initial version
 * 2009-06-24 ~01  45549SR  Christopher O    -Validate error Message when ESC key is pressed
 * 2009-06-25 ~02  45583EM  Christopher O    -Send Collected Data in COLL_REMOVE mode  
 *                                            when Collect on Remove flag is false 
 * 2009-06-26 ~03  45586JM  Christopher O    -Enhance error size message
 * 2009-06-30      45616EM  Christopher O    -Handle errors correctly in callCollectOrRemove Function
 * 2009-06-30      45609JM  Christopher O    -Validate collect on Remove in replace part
 * 2009-07-03 ~04  45652SR  Christopher O    -Validate values without prefix in validatePrefix Function
 * 2009-07-03 ~05  45654SR  Christopher O    -Add collectMode to getComponentCollection Function
 * 2012-03-06 ~06  001895   VH Avila 		 -MFSCollectionType's methods were updated, we need to use the new methods
 *******************************************************************************/

package com.ibm.rchland.mfgapps.mfsclient.dialog.log;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.ListIterator;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import com.ibm.rchland.mfgapps.client.utils.messagebox.IGSMessageBox;
import com.ibm.rchland.mfgapps.client.utils.xml.IGSXMLDocument;
import com.ibm.rchland.mfgapps.client.utils.xml.IGSXMLTransaction;
import com.ibm.rchland.mfgapps.mfsclient.MFSActionableDialog;
import com.ibm.rchland.mfgapps.mfsclient.MFSFrame;
import com.ibm.rchland.mfgapps.mfsclient.struct.MFSCollectionType;
import com.ibm.rchland.mfgapps.mfsclient.struct.MFSComponentCollection;
import com.ibm.rchland.mfgapps.mfsclient.struct.MFSDataCollection;
import com.ibm.rchland.mfgapps.mfsclient.struct.MFSHeaderRec;
import com.ibm.rchland.mfgapps.mfsclient.struct.MFSIntStringPair;			//~03A
import com.ibm.rchland.mfgapps.mfscommon.MFSConfig;

/** This Dialog will Collect, Remove or View all Collection Data from a given mctl, crct 
 * <code>MFSPartDataCollectDialog</code> 
 * @author The MFS Client Development Team
 */
@SuppressWarnings("serial")
public class MFSPartDataCollectDialog extends MFSActionableDialog
{
	/** Constant to set REMOVE on Collect*/
	public static final String COLL_REMOVE = "REMOVE"; //$NON-NLS-1$

	/** Constant to set to COLLECT Values*/
	public static final String COLL_COLLECT = "COLLECT";  //$NON-NLS-1$

	/** Constant to set to VIEW Collected Values*/
	public static final String COLL_VIEW = "VIEW"; //$NON-NLS-1$
	
	/** Constant to set to VIEW Collected Values*/
	public static final String COLL_REWORK_REMOVE =	"REWORK_REMOVE"; //$NON-NLS-1$
	
	/** The Log Values Button <code>JButton</code>. */
	private JButton btnLogValues = createButton("F2 = Log Values", 'o'); //$NON-NLS-1$

	/** The Cancel <code>JButton</code>. */
	private JButton btnCancel = createButton("Cancel", 'n'); //$NON-NLS-1$
	
	/** The input <code>JTextField</code>.MFSConstants.LARGE_TF_COLS */
	private JTextField textFieldCollectInput = createTextField(50, 50);
	
	/** The Dinamic Collect Message to prompt to user */
	private JLabel collectMessageLabel = createLabel("Collect Message "); //$NON-NLS-1$
	
	/** The <code>MFSHeaderRec</code> for which logging is being performed. */
	private MFSHeaderRec fieldHeaderRec = null;
	
	/** The <code>JScrollPane</code> used in the collect table */
	private JScrollPane scrollCollect;
	
	/** The <code>JTable</code> to show colected values from COLD10 and COLS10 */
    private JTable tableCollect;
            
    /** The table model to set*/
    private DefaultTableModel model;
	
    /** The MFSDataCollection Object */
    private MFSDataCollection dataCollection;
    
    /** The list of values to populate in JTable*/
    private ArrayList<MFSCollectTableValues> listOfValuesInTable;
    
    /** The list of values to Remove when Collect on Remove is False*/  
    private ArrayList<MFSCollectTableValues> listOfValuesToRemove;                                       //~02C
    
    /** The current crct*/
    private String crct;
    
    /** The current mctl*/
    private String mctl;
    
    /** The error message, If an error was found */ 
    private String erms;
    
	/** The return code from this dialog. */
	private int fieldReturnCode = 0;
	
	/** The Operation Mode for this Dialog*/
	private String collectMode;
	
	/** Is the flag to set if window was populated with data*/
	private boolean dialogDisplayed;
	/**
	 * constructor MFSPartDataCollectDialog <p>
	 * <p>
	 * This is the constructor of the MFSPartDataCollectDialog Class<p>   
	 *
	 */
	public MFSPartDataCollectDialog(MFSFrame parent, MFSHeaderRec headerRec, String collectMode, String crct, String mctl)
	{
		super(parent, ""); //$NON-NLS-1$
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.fieldHeaderRec = headerRec;	
		this.collectMode = collectMode;
		this.crct = crct;
		this.mctl = mctl;
		this.dataCollection = headerRec.getDataCollection();
		this.listOfValuesInTable = new ArrayList<MFSCollectTableValues>(10);
		this.listOfValuesToRemove = new ArrayList<MFSCollectTableValues>(10);                          //~02A
		//Initialize all components
		initComponents();		
		//Initialize display variables
		initDisplay();
		//Fill Data in table, If no rows was populated, no data is needed to collect 
		if(fillDataCollection(dataCollection, collectMode, crct))
		{
			this.dialogDisplayed = true;
			addMyListeners();
			setVisible(true);
		}
		//No data was found to populate in Dialog, set the dialog was not displayed 
		else if(collectMode.equals(COLL_VIEW))                                        //~01A
		{
			this.dialogDisplayed = false;
			setErms("There is no data collected for the selected part."); //$NON-NLS-1$
		}
	}
	/**This function will initialize all components, and set the coordinate in Dialog
	 *  
	 * @return 
	 */
	public void initComponents()
	{	
		JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 30, 0));	
		buttonPanel.add(btnLogValues);
		buttonPanel.add(btnCancel);		
		//Set log values button false as default
		btnLogValues.setEnabled(false);	
		JPanel contentPaneCenter = new JPanel(new GridBagLayout());
		contentPaneCenter.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(4, 4, 4, 4);
		//Set the Message Label coordinate
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;		
		contentPaneCenter.add(collectMessageLabel, gbc);		
		//Set the input text coodinate
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.weightx = 0.0;
		gbc.weighty = 0.0;	
		contentPaneCenter.add(textFieldCollectInput, gbc);					                   
		//Create the header names of the Table
        Object column[]={"Collect Type","Value ","Message"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        model = new DefaultTableModel(column,0);        
        //Set the Table Mode as not editable rows
        tableCollect = new JTable(model){
        	public boolean isCellEditable(int rowIndex, int colIndex) {
        		return false;   //Disallow the editing of any cell
            }  
        }; 	  
        //Set the table properties
        tableCollect.setBackground(new Color(255, 255, 255));       
        tableCollect.setFont(new Font("Arial", 0, 10)); //$NON-NLS-1$
        tableCollect.setModel(model);
        tableCollect.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);        
        //Set the properties for the columns
        TableColumnModel columnModel = tableCollect.getColumnModel();
		columnModel.getColumn(0).setPreferredWidth(260);
		columnModel.getColumn(1).setPreferredWidth(300);
		columnModel.getColumn(2).setPreferredWidth(400);					
		//Create a new ListSelectionListener to handle row selections 
		CollectTableSelectionListener listener = new CollectTableSelectionListener(tableCollect,this);
	    tableCollect.getSelectionModel().addListSelectionListener(listener);
	    tableCollect.getColumnModel().getSelectionModel().addListSelectionListener(listener);	
		tableCollect.setName("Collect Data Table"); //$NON-NLS-1$
		tableCollect.getTableHeader().setReorderingAllowed(false);	     
		//Set the scrollPane properties
        scrollCollect = new JScrollPane(tableCollect);
        scrollCollect.setPreferredSize(new Dimension(700, 200));
        scrollCollect.setViewportView(tableCollect);   	   
	    //Set the scroll panel coordinate
	    gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 3;
		gbc.gridheight = 1;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;	
	    contentPaneCenter.add(scrollCollect, gbc);
		//Set the panel of buttons coordinate
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridwidth = 4;
		gbc.gridheight = 1;
		gbc.weightx = 0.0;
		gbc.weighty = 0.0;		
		contentPaneCenter.add(buttonPanel, gbc);	
		//Set panel to container and dimensions
		setContentPane(contentPaneCenter);
		setLabelDimensions(collectMessageLabel, 18);
		pack();
	}
	
	/**This function will Sets the dialog's title.
	 * 
	 * @param 
	 * @return 
	 */
	public void initDisplay()
	{
		if (this.collectMode.equals(COLL_COLLECT))
		{
			setTitle("Collect Data");  //$NON-NLS-1$
		}
		else if (this.collectMode.equals(COLL_REMOVE) || this.collectMode.equals(COLL_REWORK_REMOVE))
		{
			setTitle("Remove Data Collected"); //$NON-NLS-1$
		}
		else if (this.collectMode.equals(COLL_VIEW))
		{
			setTitle("View Data to Collect"); //$NON-NLS-1$
			btnLogValues.setEnabled(false);
			btnCancel.setText("Close"); //$NON-NLS-1$
			btnCancel.setMnemonic('o');
			textFieldCollectInput.setEditable(false);
		}
	}
	/**This function will change the input message to display to the user for selected row
	 *  
	 * @param 
	 * @return Returns the string with the converted weight
	 */
	protected void setCollectMessage()
	{
		int rowNumber = tableCollect.getSelectedRow();
		if(rowNumber>=0)
		{
			collectMessageLabel.setText(tableCollect.getValueAt(rowNumber,0).toString());		
		}
	}
	/**This function will check if dialog was displayed with data in window
	 * 
	 * @param
	 * @return Returns true if dialog was displayed, false otherwise
	 */
	public boolean wasDialogDisplayed()
	{
		return this.dialogDisplayed;
	}
	
	/**This function will set an error if rc == 2 in the W_PARTCOL call,
	 * and will show the error in dialog. 
	 * 
	 * @param collType	
	 * @param collectCount
	 * @return 
	 */
	private void setCollectErrorInRow(String collType, String collectCount, String errorMessage)
	{
		int index = -1;
		ListIterator<MFSCollectTableValues> li = listOfValuesInTable.listIterator();
		if(!errorMessage.equals("OK")) //$NON-NLS-1$
		{
			try
			{
				while(li.hasNext())
				{
					MFSCollectTableValues tableValues = li.next();
					if(tableValues.getCollectType().toLowerCase().trim().equals(collType.toLowerCase())
					   && tableValues.getCollectCount().trim().equals(collectCount) )
					{
						tableValues.setCollectStatus(false);
						btnLogValues.setEnabled(false);
						index = li.previousIndex();
						break;
					}
				}
				if(index != -1)
				{
					tableCollect.setValueAt(errorMessage,index,2);
				}	
			}catch(Exception e)
			{
				//If error displaying message do not populate error
			}	
		}				
	}
	/**~02A
	 * This function will append the values from the MFSCollectTableValues to   
	 * the XML
	 * 
	 * @param w_partcoll is the XML to append the values.
	 * @param tableValues is the values to append in the XML
	 * @return Returns Zero if no error was found, different to zero otherwise
	 */
	private void appendElementsToXml(IGSXMLTransaction w_partcoll, MFSCollectTableValues tableValues)
	{
		w_partcoll.startElement("REC"); //$NON-NLS-1$
		w_partcoll.addElement("COLT",tableValues.getCollectType()); //$NON-NLS-1$
		w_partcoll.addElement("CVCT",tableValues.getCollectCount()); //$NON-NLS-1$
		w_partcoll.addElement("CVAL",tableValues.getCollectValue());   //$NON-NLS-1$
		w_partcoll.addElement("CORD",Integer.toString(tableValues.getCollectOrder())); //$NON-NLS-1$
		w_partcoll.addElement("CREM",tableValues.getCollectOnRemove()); //$NON-NLS-1$
		w_partcoll.endElement("REC"); //$NON-NLS-1$
	}
	
	private void insertCollectedComponent()
	{
		Hashtable<String, MFSComponentCollection> listOfComponentsCollected = new Hashtable<String, MFSComponentCollection>();
		ListIterator<MFSCollectTableValues> li = listOfValuesInTable.listIterator();
		
		while(li.hasNext())
		{
			MFSCollectTableValues tableValues = li.next();	
			if(listOfComponentsCollected.containsKey(tableValues.getCollectType()))
			{				
				//We have the collect type already inserted in the hashtable, so get the object and add the next value
				MFSComponentCollection componentCollected = (MFSComponentCollection) listOfComponentsCollected.get(tableValues.getCollectType());
				componentCollected.addValuesList(tableValues.getCollectValue(),tableValues.getCollectCount());
				listOfComponentsCollected.put(tableValues.getCollectType(),componentCollected);
			}
			else
			{
				//We don't have the collect type in the hashtable, insert the colltype and the first value.
				MFSComponentCollection componentCollected = new MFSComponentCollection();						
				componentCollected.setCollectionType(tableValues.getCollectType());
				componentCollected.setCrct(this.crct);
				componentCollected.setQuantity("-1");				 //$NON-NLS-1$
				componentCollected.setOrder(Integer.toString(tableValues.getCollectOrder()));						
				componentCollected.addValuesList(tableValues.getCollectValue(),tableValues.getCollectCount());
				listOfComponentsCollected.put(tableValues.getCollectType(),componentCollected);
			}	                  														
		}					
		//Add the new Collected Data to the header object.
		Enumeration<MFSComponentCollection> e = listOfComponentsCollected.elements();
		//Loop through all collect types sent to W_PARTCOL transaction, and insert to the header object
		while(e.hasMoreElements())
		{
			MFSComponentCollection componentCollected =(MFSComponentCollection) e.nextElement();
			dataCollection.addComponentCollection(componentCollected, this.mctl, MFSDataCollection.COLLECTED_DATA);
		}		
	}
	
	
	/**~02A
	 * This function will insert all collected values to the xml, and will handle the of call W_PARTCOL Transaction 
	 * in server. If an error occur will set the error in each row to be displayed to the user.
	 * If a hard error occur will show an error message dialog describing the error.
	 * 
	 * @param w_parcoll is the XML to append the values and run the transaction. 
	 * @return Returns Zero if no error was found, different to zero otherwise
	 */
	private int collectData(IGSXMLTransaction w_partcoll, String user)
	{
		int rc = 0;		
		ListIterator<MFSCollectTableValues> li = listOfValuesInTable.listIterator();		
		
		w_partcoll.addElement("COLL_TRX","UPDT_COLL"); //$NON-NLS-1$ //$NON-NLS-2$
		w_partcoll.addElement("MCTL", this.mctl);  //$NON-NLS-1$
		w_partcoll.addElement("CRCT",this.crct); //$NON-NLS-1$
		w_partcoll.addElement("USER",user); //$NON-NLS-1$
		w_partcoll.addElement("COLLECT_ACTION","COLLECT");                                    //$NON-NLS-1$ //$NON-NLS-2$
		w_partcoll.startElement("COLLECTION_TYPES"); //$NON-NLS-1$
		
		while(li.hasNext())
		{
			MFSCollectTableValues tableValues = li.next();	
			//Insert values from table to xml
			appendElementsToXml(w_partcoll,tableValues);                                         														
		}		
		//close the COLLECTION_TYPES tag, opened from callCollectOrRemoveData Function
		w_partcoll.endElement("COLLECTION_TYPES");											 //$NON-NLS-1$
		//If we have a Rework, append the stored REMOVE xml to the transaction. 
		if(this.fieldHeaderRec.getDataCollection().getW_partcoll_remove() != null)
		{
			IGSXMLTransaction w_partcoll_rework = new IGSXMLTransaction("W_PARTCOL");  //$NON-NLS-1$
			w_partcoll_rework.addData(this.fieldHeaderRec.getDataCollection().getW_partcoll_remove().toXMLString());
			if(w_partcoll_rework.getNextElement("COLLECT_ACTION").equals("REMOVE")) //$NON-NLS-1$ //$NON-NLS-2$
			{
				w_partcoll.addElement("COLLECT_ACTION","REMOVE"); //$NON-NLS-1$ //$NON-NLS-2$
				w_partcoll.startElement("COLLECTION_TYPES"); //$NON-NLS-1$
				w_partcoll.addData(w_partcoll_rework.stepIntoElement("COLLECTION_TYPES")); //$NON-NLS-1$
				w_partcoll.endElement("COLLECTION_TYPES"); //$NON-NLS-1$
			}																				
		}	
		//End the xml document and call W_PARTCOL transaction to Server
		w_partcoll.endDocument();
		w_partcoll.run();		
		rc = w_partcoll.getReturnCode();
		//If rc ==2, we have an error with some collected value.
		if (rc == 2)
		{
			IGSXMLDocument output = new IGSXMLDocument(w_partcoll.getOutput());	
			setErms(w_partcoll.getErms());		
			output.stepIntoElement("COLLECTION_TYPES"); //$NON-NLS-1$
			while(output.stepIntoElement("REC")!= null) //$NON-NLS-1$
			{
				String colt = output.getNextElement("COLT"); //$NON-NLS-1$
				String cvct = output.getNextElement("CVCT"); //$NON-NLS-1$
				String error = output.getNextElement("ERROR"); //$NON-NLS-1$
				if(colt != null && cvct != null && error!=null)
				{
					setCollectErrorInRow(colt.trim(),cvct.trim(),error.trim());
				}
				output.stepOutOfElement();	
			}
			output.stepOutOfElement();	
		}
		//If rc != 0, we have a hard error, then populate the error in dialog
		else if(rc != 0)
		{
			//Get the error message from output, and store in erms variable 
			setErms(w_partcoll.getErms());			        
		}		
		//If rc == 0 transaction ran successfully  
		else			
		{			
			//If we have a rework, Delete the collected data from the header object.
			if(this.fieldHeaderRec.getDataCollection().getW_partcoll_remove() != null)
			{
				fieldHeaderRec.getDataCollection().removeComponentCollection(this.mctl,this.crct);
				this.fieldHeaderRec.getDataCollection().setW_partcoll_remove(null);
			}
			insertCollectedComponent();
		}					
		return rc;
	}
	/**~02A
	 * This function will insert all collected values to the xml to be removed, 
	 * and will handle the of call W_PARTCOL Transaction with a REMOVE flag 
	 * in server. If a hard error occur will show an error message dialog describing the error.
	 * 
	 * @param w_parcoll is the XML to append the values and run the transaction. 
	 * @return Returns Zero if no error was found, different to zero otherwise
	 */
	private int removeData(IGSXMLTransaction w_partcoll, String user)
	{
		int rc = 0;
		ListIterator<MFSCollectTableValues> li = listOfValuesInTable.listIterator();
		
		w_partcoll.addElement("COLL_TRX","UPDT_COLL"); //$NON-NLS-1$ //$NON-NLS-2$
		w_partcoll.addElement("MCTL", this.mctl);  //$NON-NLS-1$
		w_partcoll.addElement("CRCT",this.crct); //$NON-NLS-1$
		w_partcoll.addElement("USER",user); //$NON-NLS-1$
		w_partcoll.addElement("COLLECT_ACTION","REMOVE");	                                                                  //$NON-NLS-1$ //$NON-NLS-2$
		w_partcoll.startElement("COLLECTION_TYPES"); //$NON-NLS-1$
		
		//Append the values with Collect On Remove == 'Y' retrieved from Dialog.
		while(li.hasNext())
		{
			MFSCollectTableValues tableValues = li.next();	
			appendElementsToXml(w_partcoll,tableValues);  
		}		
		//Check if we have values with Collect On Remove == 'N', and append to XML to be removed.
		li = listOfValuesToRemove.listIterator();
		while(li.hasNext())
		{
			MFSCollectTableValues tableValues = li.next();
			appendElementsToXml(w_partcoll,tableValues);                                        
		}
		//End the xml document and call W_PARTCOL transaction to Server
		w_partcoll.endElement("COLLECTION_TYPES");		 //$NON-NLS-1$
		w_partcoll.endDocument();
		
		//If is REWORK_REMOVE only keep the XML object to be used later.
		if(collectMode.equals(COLL_REWORK_REMOVE))
		{
			this.fieldHeaderRec.getDataCollection().setW_partcoll_remove(w_partcoll);	
		}
		//If Mode is REWORK, execute the transaction 
		else
		{
			w_partcoll.run();		
			rc = w_partcoll.getReturnCode();
			//If rc ==2, we have an error with some collected value.
			if(rc == 2)													                  
			{
				IGSXMLDocument output = new IGSXMLDocument(w_partcoll.getOutput());
				String error = null;    
				setErms(w_partcoll.getErms());	
				output.stepIntoElement("COLLECTION_TYPES"); //$NON-NLS-1$
				while(output.stepIntoElement("REC")!= null) //$NON-NLS-1$
				{
					String colt = output.getNextElement("COLT"); //$NON-NLS-1$
					String cvct = output.getNextElement("CVCT"); //$NON-NLS-1$
					error = output.getNextElement("ERROR"); //$NON-NLS-1$
					if(colt != null && cvct != null && error!=null)
					{
						setCollectErrorInRow(colt.trim(),cvct.trim(),error.trim());
					}
					output.stepOutOfElement();	
				}
				output.stepOutOfElement();					
			}
			//If rc != 0, we have a hard error, then populate the error in dialog
			else if(rc != 0)											                 
			{
				setErms(w_partcoll.getErms());									
			}
			//If rc == 0 transaction ran successfully
			else
			{
				//Remove the collection from the list.
				fieldHeaderRec.getDataCollection().removeComponentCollection(this.mctl,this.crct);								
			}	
		}					
		return rc;
	}	
		
	/**This function will call W_PARTCOL Transaction in server to set Collection Data 
	 * or Remove Collection Data from given mctl and crct 
	 * 
	 * @param 
	 * @return Returns Zero if no error was found, different to zero otherwise
	 */
	private int callCollectOrRemoveData()
	{
		int rc = 0;
		final MFSConfig config = MFSConfig.getInstance();
								
		try{																			//~02C
			
			IGSXMLTransaction w_partcoll = new IGSXMLTransaction("W_PARTCOL"); //$NON-NLS-1$
			w_partcoll.setActionMessage("Saving Collection Data, please wait...");	 //$NON-NLS-1$
			w_partcoll.startDocument();		
			
			if(this.collectMode.equals(COLL_COLLECT))									//~02C				
			{
				//call collectData to populate all elements in XML and Call W_PARTCOL transaction with COLLECT flag.
				rc = collectData(w_partcoll, config.get8CharUser());
			}
			else if(this.collectMode.equals(COLL_REMOVE) || this.collectMode.equals(COLL_REWORK_REMOVE))	//~02C
			{				
				//call removeData to populate all elements in XML and Call W_PARTCOL transaction with REMOVE flag.
				rc = removeData(w_partcoll, config.get8CharUser());
			}
						
			if(rc != 0)
			{	
				IGSMessageBox.showOkMB(this, null, getErms(), null);  
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}					
		return rc;
	}
	/**This function will validate if the size is allowed for given collect type
	 * 
	 * @param value 
	 * @param collType
	 * @return Returns true if is a valid size
	 */
	private boolean validateSize(String value, MFSCollectionType collType, MFSIntStringPair rcPair)		//~03C					
	{
		boolean rc = false;																				//~03C
		if(collType.isValidSize(value.trim(),value.trim().length()))									//~03C	~06C
		{
			rc = true;																					//~03C
		}
		else																							//~03C
		{
			String sizes = collType.getSizes(value.trim());									 			//~03C	~06C
			if(sizes != null)																			//~06A
			{																							//~06A
				rcPair.fieldString = "Invalid value size. Sizes expected: " + sizes; 					//~03C	 //$NON-NLS-1$
			}																							//~06A
			else																						//~06A
			{																							//~06A
				rcPair.fieldString = "Invalid value prefix";											//~06A
			}																							//~06A
		}				
		return rc;
	}
	/**This function will validate if the collect value is unique in Dialog
	 * 
	 * @param value is the value to check
	 * @param collType is the collect type name
	 * @return Returns true if was unique 
	 */
	private boolean validateUniqueValue(String value, MFSCollectionType collType, MFSIntStringPair rcPair)	//~03C
	{	
		boolean returnStatus = true;

		if(collType.isUnique())
		{			
			ListIterator<MFSCollectTableValues> li = listOfValuesInTable.listIterator();
			while(li.hasNext())
			{
				MFSCollectTableValues collValue = li.next();
				if(collValue.getCollectType().equals(collType.getCollectionType()))
				{
					if(collValue.getCollectValue().equals(value))
					{
						returnStatus = false;
						rcPair.fieldString = "Value is not unique";											//~03C //$NON-NLS-1$
						break;
					}	
				}				
			}
		}		
		return returnStatus;		
	}
	/**This function will check if value contains the given prefix
	 * 
	 * @param value is the value to check
	 * @param collType is the collect type name
	 * @return Returns true if is valid prefix or no prefix was found, false otherwise 
	 */
	private boolean validatePrefix(String value, MFSCollectionType collType, MFSIntStringPair rcPair)		//~03C
	{		
		boolean validPrefix = true;																			//~03C,~04C
		
		if(!collType.isValidPrefix(value))																	//~06C
		{
			validPrefix = false;																		//~03C, ~04C
			rcPair.fieldString = "Invalid value prefix";												//~03C, ~04C //$NON-NLS-1$
		}
		return validPrefix;
	}
	
	/**This function will check if the value has a correct type 
	 * 
	 * @param value is the value to check
	 * @param collType is the collect type name
	 * @return Returns true if is valid type , false otherwise 
	 */
	private boolean validateValueType(String value, MFSCollectionType collType, MFSIntStringPair rcPair)		//~03C
	{
		boolean validType = false ;
		String prefixValue; 
		
		if(!collType.isValidPrefix(value))																		//~06C
		{
			prefixValue = value;
		}
		else
		{			
			prefixValue = value.substring(collType.getPrefixLength(value));										//~06C		
		}
		
		if(collType.getType().equals("H")) //$NON-NLS-1$
		{
			validType = prefixValue.matches("\\p{XDigit}+"); //$NON-NLS-1$
		}
		else if(collType.getType().equals("A")) //$NON-NLS-1$
		{
			validType = prefixValue.matches("\\p{Alnum}+"); //$NON-NLS-1$
		}
		
		if(!validType)																							//~03A
		{
			rcPair.fieldString = "Invalid value type";															//~03A //$NON-NLS-1$
		}				
		return validType;
	}
	/**This function will check all the validations for the given collection Type
	 * 
	 * @param value is the value to check
	 * @param collectionType is the collection type name
	 * @return 
	 */
	private MFSIntStringPair validateValue(String value, String collectionType)			//~03C
	{
		MFSIntStringPair rcPair = new MFSIntStringPair(0,"");							//~03C //$NON-NLS-1$
		
		if(dataCollection.containsCollectionType(collectionType))
		{
			MFSCollectionType collType = dataCollection.getCollectionType(collectionType);
			
			if(!validateSize(value, collType, rcPair))									//~03C							
			{
				rcPair.fieldInt = 1;													//~03A				
			}
			else if(!validateUniqueValue(value,collType,rcPair))
			{
				rcPair.fieldInt = 1;													//~03A

			}
			else if(!validatePrefix(value,collType,rcPair))
			{
				rcPair.fieldInt = 1;													//~03A
				
			}
			else if(!validateValueType(value,collType,rcPair))
			{
				rcPair.fieldInt = 1;													//~03A				
			}						
		}
		else
		{
			rcPair.fieldInt  = 1;
			rcPair.fieldString = "Invalid Collection Type to validate \"" + collectionType +"\""; //$NON-NLS-1$ //$NON-NLS-2$
			IGSMessageBox.showOkMB(this, null, rcPair.fieldString, null);
		}		
		return rcPair; 
	}
	/**This function will validate all status of the table to enable the log Button 
	 * 
	 * @param value is the value to check
	 * @param collType is the collect type name
	 * @return Returns true if is valid prefix or no prefix was found, false otherwise 
	 */
	private boolean validateAllCollectStatus()
	{
		boolean rc = true;
		ListIterator<MFSCollectTableValues> li = listOfValuesInTable.listIterator();
		while(li.hasNext())
		{
			MFSCollectTableValues collValue = li.next();
			if(!collValue.isCollectStatusOk())
			{
				rc = false ;
				break;
			}
		}
		return rc;
	}
	
	/**~01C 
	 * This function will validate the input value when an enter key is pressed in COLL_COLLECT 	 
	 *  or COLL_REMOVE mode.
	 * @param 
	 * @return 
	 */
	private void validateFieldCollectInput()
	{
		if(!collectMode.equals(MFSPartDataCollectDialog.COLL_VIEW))
		{

			int rowNumber = tableCollect.getSelectedRow();
			if(rowNumber != -1)
			{
				if(!textFieldCollectInput.getText().trim().equals("")) //$NON-NLS-1$
				{									
					String inputValue = textFieldCollectInput.getText().trim().toUpperCase();
					
					MFSCollectTableValues tableValues = listOfValuesInTable.get(rowNumber);					
					
					if(this.collectMode.equals(COLL_REMOVE) || this.collectMode.equals(COLL_REWORK_REMOVE))
					{
						if(tableValues.getCollectValue().equals(inputValue))
						{
							//Change the status of this row as valid status
							tableValues.setCollectStatus(true);							
							//Set the Message to Ok.
							tableCollect.setValueAt("Ok",rowNumber,2); //$NON-NLS-1$
							//Get the next row number
							rowNumber++;
							//If exist next row
							if(rowNumber < tableCollect.getRowCount())
							{
								//Select next Row
								tableCollect.setRowSelectionInterval(rowNumber,rowNumber);
							}
							//Clean the input field
							textFieldCollectInput.setText(""); //$NON-NLS-1$
						}
						else
						{
							//The collected Value is different 
							erms = "Invalid value to remove, it shoud match the collected value"; //$NON-NLS-1$
							tableCollect.setValueAt(erms,rowNumber,2);
							//Change the status of this row as invalid status
							tableValues.setCollectStatus(false);
							textFieldCollectInput.selectAll();	
							textFieldCollectInput.requestFocusInWindow();
						}
					}
					else if(this.collectMode.equals(COLL_COLLECT))
					{
						MFSIntStringPair rcPair = validateValue(inputValue, tableValues.getCollectType()); 	//~03C
						
						if(rcPair.fieldInt != 0) 															//~03C
						{				
							//Error Found, set Error in Table
							tableCollect.setValueAt(rcPair.fieldString,rowNumber,2);   						//~03C		
							//Replace the value in Collection
							tableValues.setCollectValue(""); //$NON-NLS-1$
							//Change the status of this row as valid status
							tableValues.setCollectStatus(false);
							//Remove the Collection in the List
							listOfValuesInTable.remove(rowNumber);
							//and Replace with the new values
							listOfValuesInTable.add(rowNumber,tableValues);
							//Select the input value 
							textFieldCollectInput.selectAll();
							//Set the focus on the input field
							textFieldCollectInput.requestFocusInWindow();
						}
						else
						{
							//Replace the value in Collection
							tableValues.setCollectValue(inputValue);
							//Change the status of this row as valid status
							tableValues.setCollectStatus(true);
							//Remove the Collection in the List
							listOfValuesInTable.remove(rowNumber);
							//and Replace with the new values
							listOfValuesInTable.add(rowNumber,tableValues);
							//Set the Field Value in table with the retrieved value from input
							tableCollect.setValueAt(inputValue,rowNumber,1);
							//Set the Message to Ok.
							tableCollect.setValueAt("Ok",rowNumber,2); //$NON-NLS-1$
							//Get the next row number
							rowNumber++;
							//If exist next row
							if(rowNumber < tableCollect.getRowCount())
							{
								//Select next Row
								tableCollect.setRowSelectionInterval(rowNumber,rowNumber);
							}	
							//Clean the input field
							textFieldCollectInput.setText(""); //$NON-NLS-1$
						}	
					}													
				}
				else
				{
					//Set Value in Blank error 
					tableCollect.setValueAt("Please insert a value... ",rowNumber,2);		 //$NON-NLS-1$
				}
				if(validateAllCollectStatus())
				{
					btnLogValues.setEnabled(true);
					btnLogValues.requestFocusInWindow();
				}
				else
				{
					btnLogValues.setEnabled(false);
					textFieldCollectInput.requestFocusInWindow();
				}	
			}
		}		
	}
	/**This function will add one collected value with the current Collect Order in jtable
	 * 
	 * @param MFSCollectTableValues collTableValues
	 * @return  
	 */	
	private void addValuesInTable(MFSCollectTableValues collTableValues)
	{
		int index = -1;
		ListIterator<MFSCollectTableValues> li = listOfValuesInTable.listIterator();
		while(li.hasNext())
		{
			MFSCollectTableValues valuesInTable = li.next();
			index = li.previousIndex();
			if(collTableValues.getCollectOrder() == valuesInTable.getCollectOrder())
			{
				index = -1;
			}
			if(collTableValues.getCollectOrder() < valuesInTable.getCollectOrder())				
			{
				index = li.previousIndex();
				break;
			}
			if(collTableValues.getCollectOrder() > valuesInTable.getCollectOrder())
			{
				index =li.previousIndex()+ 1;
			}
		}
		if(index == -1)
		{
			listOfValuesInTable.add(collTableValues);
		}
		else
		{
			listOfValuesInTable.add(index , collTableValues);
		}
	}
	
	/**This function will fill all data in jtable, to be displayed to the user.
	 * 
	 * @param dataCollection 
	 * @param collectMode
	 * @param  crct
	 * @return Returns true when we have values to display in dialog, else will return false 
	 */	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private boolean fillDataCollection(MFSDataCollection dataCollection, String collectMode, String crct)
	{
		boolean showDialog = false;
		//Create a new List iterator 
		ListIterator<MFSComponentCollection> componentsIt;
		//List of the components retrieved by mctl and crct
		ArrayList listOfComponents;
		
		try                                                                              //~02A
		{
			//Check if we have values in data Collection 
		    if((dataCollection != null) && (dataCollection.containsComponentCollection(mctl,crct,collectMode))) //~05C
			{
		    	//Retrieve the list of components by mctl , crct
				listOfComponents = dataCollection.getComponentCollection(mctl,crct,collectMode);  //~05C
				//Create an iterator for the list of components.
				componentsIt = listOfComponents.listIterator();
				//Create a new instance of MFSCollectTAbleValues
				MFSCollectTableValues collTableValues;
				//loop thought components
				while(componentsIt.hasNext())
				{
					//Retrieve the component collection for given mctl crct
					MFSComponentCollection comp = componentsIt.next();
					//Retrieve the collection Type for crct
					MFSCollectionType collType = dataCollection.getCollectionType(comp.getCollectionType());
					//If W_PARTCOLL program returns values, the quantity is set to -1.
					if(comp.getQuantity() == -1)
					{							
						//We have values already collected
						if((collType!= null) &&( collectMode.equals(COLL_REMOVE)                       //~02C
								 ||	 collectMode.equals(COLL_REWORK_REMOVE)
								 ||	 collectMode.equals(COLL_VIEW)))					
						{
							//Create an iterator from values
							Collection c = comp.getValuesList().values();
							Iterator itValues = c.iterator();    
							//Loop thought iterator
							while(itValues.hasNext()) 
							{
								//Create a new MFSCollectTableValues
								collTableValues = new MFSCollectTableValues();
								//Retrieve the selected collection Type
								collTableValues.setCollectType(comp.getCollectionType());
								//Retrieve the current value 
								Object[] val = (Object[]) itValues.next();
								//set the value in MFSCollectTableValues object
								collTableValues.setCollectValue((String)val[0]);
								//set the count in the MFSCollectTableValues object
								collTableValues.setCollectCount((String)val[1]);
								//set if the collect type has collect on remove flag
								collTableValues.setCollectOnRemove(collType.isCollectOnRemove());
								//Set the collection order 
								collTableValues.setCollectOrder(comp.getOrder());
								//Build the Message to prompt to user
								String collectMessage = collType.getMessage();	
								//If we have more than one value
								if(comp.getValuesList().size()>1)
								{
									//Add the collect count to the message
									collectMessage+=" '" + collTableValues.getCollectCount()+ "'"; //$NON-NLS-1$ //$NON-NLS-2$
								}
								//IF the collect type has prefix
								String prefixes = "";															/*~06A*/
								prefixes = collType.getAllPrefixes();											/*~06A*/
								if(prefixes.length() > 0)														/*~06C*/
								{
									//Add the prefix to the message
									collectMessage+=" (" + prefixes + ")"; //$NON-NLS-1$ //$NON-NLS-2$			/*~06C*/
								}		
								collTableValues.setCollectMessage(collectMessage);
								//Start~02C If is Mode REMOVE or REWORK_REMOVE and Collect on Remove is False, 
								//add the value to the list of values to be removed when W_PARTCOL program is called
								if((collectMode.equals(COLL_REMOVE) || collectMode.equals(COLL_REWORK_REMOVE))   //~02A
								    && !collType.isCollectOnRemove()	)
								{
									listOfValuesToRemove.add(collTableValues);                                   //~02A
								}
								else                                                                             //~02A
								{
									//Add the MFSCollectTableValues object to the list of values in table
									addValuesInTable(collTableValues);                                           //~02C
								}//End ~02C												
							}		
						}
						//Start ~02A						
						else if(collType == null && (collectMode.equals(COLL_REMOVE)                       
								 				 ||	 collectMode.equals(COLL_REWORK_REMOVE))) 
				        {
							//Create an iterator from values
							Collection c = comp.getValuesList().values();
							Iterator itValues = c.iterator();    
							//Loop thought iterator
							while(itValues.hasNext()) 
							{
								//Create a new MFSCollectTableValues
								collTableValues = new MFSCollectTableValues();
								//Retrieve the selected collection Type
								collTableValues.setCollectType(comp.getCollectionType());
								//Retrieve the current value 
								Object[] val = (Object[]) itValues.next();
								//set the value in MFSCollectTableValues object
								collTableValues.setCollectValue((String)val[0]);
								//set the count in the MFSCollectTableValues object
								collTableValues.setCollectCount((String)val[1]);
								//set if the collect type has collect on remove flag
								collTableValues.setCollectOnRemove(false);
								//Set the collection order 
								collTableValues.setCollectOrder(comp.getOrder());
								//Add the collect type to the list to be removed.
								listOfValuesToRemove.add(collTableValues);                                  							
							}
						}//End ~02A
					}
					//We need to collect the values for the given collect type
					else
					{				
						//Collect values only in collect mode or view mode
						if((collType!= null) && ((collectMode.equals(COLL_COLLECT)) 
											 ||	(collectMode.equals(COLL_VIEW))))
						{
							//Get the quantity of values to retrieve
							for(int count = 0; count < comp.getQuantity(); count++)
							{
								//Create a new MFSCollectTableValues object to store values
								collTableValues = new MFSCollectTableValues();				
								//Set the collection Type
								collTableValues.setCollectType(comp.getCollectionType());
								//Set the collection count
								collTableValues.setCollectCount(Integer.toString(count+1));	
								//Set the collection quantity
								collTableValues.setCollectQuantity(comp.getQuantity());	
								//Set the collect on remove
								collTableValues.setCollectOnRemove(collType.isCollectOnRemove());
								//SEt the collection order
								collTableValues.setCollectOrder(comp.getOrder());
								//Set the collect Message
								String collectMessage = collType.getMessage();
								//Check if we have more than one value
								if(collTableValues.getCollectQuantity()>1)
								{
									//Add the collect count to the message
									collectMessage+=" '" + collTableValues.getCollectCount() + "'"; //$NON-NLS-1$ //$NON-NLS-2$
								}
								//IF the collect type has prefix
								String prefixes = null;													/*~06A*/
								prefixes = collType.getAllPrefixes();									/*~06A*/
								if(prefixes.length() > 0)												/*~06C*/
								{
									//Add the prefix to the message
									collectMessage+=" (" + prefixes + ")"; //$NON-NLS-1$ //$NON-NLS-2$	/*~06C*/
								}		
								collTableValues.setCollectMessage(collectMessage);
								//Add the MFSCollectTableValues object to the list of values in table
								addValuesInTable(collTableValues);
							}
						}																
					}
				}
				//Check if the list of values in table is not empty
				
			    if(!listOfValuesInTable.isEmpty())
			    {		 
			    	MFSCollectTableValues tableValues;
			    	//Create a list iterator for the list of values in table
			    	ListIterator<MFSCollectTableValues> tableValuesIt = listOfValuesInTable.listIterator();
			    	while(tableValuesIt.hasNext())
			    	{
			    		//Retrieve the next object into the MFSCollectTableValues object 
			    		tableValues = tableValuesIt.next();
			    		//Retrieve the message and the collect value objects
			    		Object[] valuesColl = {(Object)tableValues.getCollectMessage(),(Object)tableValues.getCollectValue(),""}; //$NON-NLS-1$
			    		//add the values in table
			    		model.addRow(valuesColl);
			    	}	  
				    
				    if(tableCollect.getRowCount()>0)
				    {
				    	//Select the first row
				    	tableCollect.setRowSelectionInterval(0,0);
				    }
				    showDialog = true;
			    }
			    //Start ~02A
			    else                                                                               
			    {
			    	//If is Collect on Remove and exist collected data with Collect On Remove equals to False
			    	if(collectMode.equals(COLL_REMOVE) || collectMode.equals(COLL_REWORK_REMOVE))   
			    	{
			    		//Check If we have collect on remove with flag in false, and callCollectOrRemoveData
			    		if(!listOfValuesToRemove.isEmpty())                                         
			    		{
			    			fieldReturnCode = callCollectOrRemoveData();                            
			    		}
			    	}
			    }//End ~02A			    
			}	
		}
		catch(Exception e)                                                           //~02A
		{
			e.printStackTrace();
		}							       		   
	    return showDialog;	    
	}	
	/**This function retrieve the return code of this Dialog
	 * 
	 * @return Returns the return code 
	 */	 
	public int getFieldReturnCode() {
		return fieldReturnCode;
	}
	/**This function will retrieve the error message 
	 * 
	 * @return Returns the error message 
	 */	
	public String getErms() {
		return erms;
	}
	/**This Function will set the error message
	 * @param erms The erms to Set.
	 */
	private void setErms(String erms) {
		this.erms = erms;
	}
	/** Adds the listeners to this dialog's <code>MFSPartDataCollectDialog</code>s. 
	 * 
	 * @return 
	 */	
	private void addMyListeners()
	{
		/**Add actionListeners */
		this.btnCancel.addActionListener(this);
		this.btnLogValues.addActionListener(this);		

		/**Add keyListeners */
		this.btnCancel.addKeyListener(this);
		this.tableCollect.addKeyListener(this);						//~01A
		this.textFieldCollectInput.addKeyListener(this);            //~01C
		this.btnLogValues.addKeyListener(this);
	}	
	
	/**This function is Invoked when a key is pressed.
	 * 
	 * @param ke the <code>KeyEvent</code> 
	 * @return Returns true if is valid prefix or no prefix was found, false otherwise 
	 */	
	public void keyPressed(KeyEvent ke)
	{
		final int keyCode = ke.getKeyCode();
		if (keyCode == KeyEvent.VK_ENTER)
		{
			final Object source = ke.getSource();
			if (source == this.textFieldCollectInput) 
			{
				validateFieldCollectInput();									//~01C
			}
			else if (source == this.btnLogValues)  
			{
				if(!collectMode.equals(MFSPartDataCollectDialog.COLL_VIEW))		//~01C
				{		
					this.btnLogValues.doClick();	
				}				
			}
			else if (source == this.btnCancel)
			{
				this.btnCancel.doClick();
			}
		}
		else if (keyCode == KeyEvent.VK_F2)  
		{
			if(!collectMode.equals(MFSPartDataCollectDialog.COLL_VIEW)) 		//~01C
			{
				this.btnLogValues.doClick();
			}

		}
		else if (keyCode == KeyEvent.VK_ESCAPE)
		{
			this.btnCancel.doClick();    			                            //~01A
		}
	}
	
	/**
	 * Invoked when an action occurs.
	 * @param ae the <code>ActionEvent</code>
	 */	
	public void actionPerformed(ActionEvent ae)
	{
		final Object source = ae.getSource();
		if (source == this.btnCancel)
		{
			this.fieldReturnCode = -44;
			this.dispose();
		}
		else if (source == this.btnLogValues)
		{
			this.fieldReturnCode = callCollectOrRemoveData();
			if(this.fieldReturnCode == 0)
			{
				this.dispose();
			}
		}
	}


	/**
	 * <code>MFSCollectTableValues</code><p> 
	 * Class Bean used to store all table values in window 
	 * 
	 * @author The MFS Client Development Team
	 */
	class MFSCollectTableValues{
		
		/**Variable to store the name of the collectType */
		private String collectType;
		/**The collected Value*/
		private String collectValue;
		/**Index of the collected value */
		private String collectCount;
		/**Quantity of Values to collect for this type*/
		private int collectQuantity;		
		/**Message to populate */
		private String collectMessage;
		/**Status of the retrieved Value, false if not validated or incorrect, and true if value is correct */
		private boolean collectStatus;	
		/**Collect on remove flag */
		private boolean collectOnRemove;
		/**The Collect Order */
		private int collectOrder;
		/**
		 * constructor MFSCollectTableValues <p>
		 * <p>
		 * This is the constructor of the MFSCollectTableValues Class<p>   
		 * will initialize the collect variables
		 */
		public MFSCollectTableValues()
		{
			//Initiaize the collect Type
			collectType = new String();
			//Initialize the collect Value
			collectValue = new String();
			//Initialize the collect Message			
			collectMessage = new String();
		}
				
		/**
		 * @return Returns collectCount.
		 */
		public String getCollectCount() {
			return collectCount;
		}
		/**
		 * @param collectCount the collectCount to Set.
		 */
		public void setCollectCount(String collectCount) {
			this.collectCount = collectCount;
		}
		/**
		 * @return Returns collectMessage.
		 */
		public String getCollectMessage() {
			return collectMessage;
		}
		/**
		 * @return Returns collectOnRemove.
		 */
		public String getCollectOnRemove() {
			return this.collectOnRemove ? "Y" : "N"; //$NON-NLS-1$ //$NON-NLS-2$
		}
		/**
		 * @param collectMessage the collectMessage to Set.
		 */
		public void setCollectMessage(String collectMessage) {
			this.collectMessage = collectMessage;
		}
		/**
		 * @return Returns collectOnRemove.
		 */
		public boolean isCollectOnRemove() {
			return collectOnRemove;
		}
		/**
		 * @param collectOnRemove the collectOnRemove to Set.
		 */
		public void setCollectOnRemove(boolean collectOnRemove) {
			this.collectOnRemove = collectOnRemove;
		}
		/**
		 * @return Returns collectQuantity.
		 */
		public int getCollectQuantity() {
			return collectQuantity;
		}
		/**
		 * @param collectQuantity the collectQuantity to Set.
		 */
		public void setCollectQuantity(int collectQuantity) {
			this.collectQuantity = collectQuantity;
		}
		/**
		 * @return Returns collectStatus.
		 */
		public boolean isCollectStatusOk() {
			return collectStatus;
		}
		/**
		 * @param collectStatus the collectStatus to Set.
		 */
		public void setCollectStatus(boolean collectStatus) {
			this.collectStatus = collectStatus;
		}
		/**
		 * @return Returns collectType.
		 */
		public String getCollectType() {
			return collectType;
		}
		/**
		 * @param collectType the collectType to Set.
		 */
		public void setCollectType(String collectType) {
			this.collectType = collectType;
		}
		/**
		 * @return Returns collectValue.
		 */
		public String getCollectValue() {
			return collectValue;
		}
		/**
		 * @param collectValue the collectValue to Set.
		 */
		public void setCollectValue(String collectValue) {
			this.collectValue = collectValue;
		}
			
		/**
		 * @return Returns collectOrder.
		 */
		public int getCollectOrder() {
			return collectOrder;
		}
		/**
		 * @param collectOrder the collectOrder to Set.
		 */
		public void setCollectOrder(String collectOrder) {
			this.collectOrder = Integer.parseInt(collectOrder);
		}
	}

	/**
	 * <code>CollectTableSelectionListener</code><p> 
	 * Class used to implement the ListSelectionListener for the table
	 * 
	 * @author The MFS Client Development Team
	 */
	class CollectTableSelectionListener implements ListSelectionListener {
		/** The parent collect Dialog*/
		private MFSPartDataCollectDialog collectDialog;
		/** The collect table to set the ListSelectionListener*/
		private JTable collectTable;
					
		/**
		 * constructor CollectTableSelectionListener <p>
		 * <p>
		 * This is the constructor of the CollectTableSelectionListener Class<p>   
		 */
		public CollectTableSelectionListener(JTable collectTable, MFSPartDataCollectDialog collectDialog){
			//Store the collect Table object
			this.collectTable  = collectTable;
			//Store the collection Dialog box
			this.collectDialog = collectDialog;
		}
		/**This function will be called when the selected row is changed
		 * 
		 * @param e is the ListSelectionEvent 
		 * @return 
		 */
		public void valueChanged(ListSelectionEvent e) {
			if (e.getSource() == collectTable.getSelectionModel() && 
		        collectTable.getRowSelectionAllowed())
			{
				//Set the Message to prompt to user when selected row is changed
				collectDialog.setCollectMessage();				
			}
		}		
	}	
}
