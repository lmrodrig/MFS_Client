/* © Copyright IBM Corporation 2011-2012. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2011-11-07      588440   Santiago SC      -Java 5.0 , initial version   
 * 2011-12-06      50723FR  Toribio H        -Fixing QA bugs.
 * 2011-12-20 ~01  50723FR  Toribio H        -Create new Clear Button.
 * 2011-12-29 ~02  D630864  Edgar Mercado    -Set subInstalled property when sub is complete and dispose window
 * 2012-01-04 ~03  D629692  Edgar Mercado    -Add a new attribute CSNI to know if a part needs to be serialized.
 * 2012-01-21 ~04  E638153  Edgar Mercado    -Add new attributes MLRI and COO. MLRI is to know if COO should be collected.
 *                                           -Substitute parts support.
 * 2012-01-27 ~05  D615310  Edgar Mercado    -Add new attribute PNRI and support to collect COO using mouse selection row and
 *                                            cached hash COO for a part number.
 * 2012-02-06 ~06  D652349  Edgar Mercado    -Fix for Subassembly duplicate creation error
 *                 D652350                   -Fix for New Subassembly creation error with all parts installed but efficiency off
 *                 D652352                   -Change End button to F3 function key, Set Close button to F7, and Print button to F6
 *                 D652358                   -Activate the serialize button if only a part number is scanned to allow for serializing when auto serialize is not enabled
 *                 D652360                   -Activate print button only when a subassembly is retrieved for printing and all data collected
 *                 D652351                   -Auto select the coo or prompt when necessary when a part number with MLRI=1 and PNRI=1 is scanned to be installed
 *                 D652353                   -Grey out the sub sn box for parts not requiring serial collection
 *                 D652354                   -Make AutoPrint work for existing completed assemblies
 *                 D652355                   -Duplicate Serial Check working incorrectly
 *                 D652359                   -Adjust Columns 
 * 2012-02-23 ~07  D652357 Edgar Mercado     -GreyMarket: Allow user to clear the input fields using the End button (clearButton)
 *                 D662214                   -GreyMarket: Moving the cursor with the arrow keys doesn't update the index correctly (row cursor)
 *                 D652356                   -GreyMarket: Allow for changing a coo after all parts have been installed but the subassembly is not yet ended
 *                 D662209                   -GreyMarket: Assembly Top Level Serial barcode scan issue:  Client allows user to scan any serial 
 *                                            string equal to or less than 7 char from a 1S serial format. (Thus, users can create assemblies with
 *                                            junk serial data which is shorter than 7 char from the VendorSubParts Dialog function)
 *                 D662215                   -GreyMarket: The new VendorSubPart dialog is not being rendered correctly on some tester's computer screens, making
 *                                            it difficult to use for those users. 
 * 2012-03-05 ~08  D674625 Edgar Mercado     -Grey Market: END/Close buttons fail to close dialog in Direct Work
 * 2012-06-05 ~09  E692532 Edgar Mercado     -GreyMarket (CR783/RCQ00203226): Inventory Accuracy. Rework/Rebuild functionality
 *                 D714434                   -Vendor PN log cutting off the values in the Sub PN and Sub SN columns
 * 2012-07-10 ~10  D758767 Edgar Mercado     -Sub parts WUOPST status can not complete when they rework in grey market dialog.
 *                 D758761                   -COO field can not pop up automatically on GM dialog rework button.                 
 **********************************************************************************/

package com.ibm.rchland.mfgapps.mfsclient.dialog;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;                                                           // ~05A

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import com.ibm.rchland.mfgapps.client.utils.messagebox.IGSMessageBox;
import com.ibm.rchland.mfgapps.client.utils.table.IGSDocumentTableModel;
import com.ibm.rchland.mfgapps.mfsclient.MFSActionableDialog;
import com.ibm.rchland.mfgapps.mfsclient.MFSFrame;
import com.ibm.rchland.mfgapps.mfsclient.barcode.MFSBCBarcodeDecoder;
import com.ibm.rchland.mfgapps.mfsclient.barcode.MFSBCIndicatorValue;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSConstants;
import com.ibm.rchland.mfgapps.mfscommon.MFSConfig;
import com.ibm.rchland.mfgapps.mfscommon.struct.MFSPart;
import com.ibm.rchland.mfgapps.mfscommon.struct.MFSSubstitute;                          // ~04A
import com.ibm.rchland.mfgapps.mfscommon.struct.MFSCoo;                                 // ~05A

/**
 * The <code>MFSVendorSubPartsDialog</code> is a <code>MFSActionableDialog</code>
 * that implements the <code>DocumentListener</code> and <code>TableModelListener</code>
 * interfaces and is used to log the vendor sub parts required for Grey Market.
 * @author The MFS Client Development Team
 */
public class MFSVendorSubPartsDialog extends MFSActionableDialog implements MouseListener   // Add implements MouseListener  ~05C
{

	/**
	 * The enum class for this dialog Columns
	 * @author MFS Team
	 */
	private enum Columns
	{
		INPN("Sub PN", 0), //$NON-NLS-1$
		INSQ("Sub SN", 1), //$NON-NLS-1$
		CDES("Description", 2), //$NON-NLS-1$
		IDSP("IDSP", 3), //$NON-NLS-1$
		COOC("COO", 4), //$NON-NLS-1$     // ~03C
		CSNI("Csni", 5), //$NON-NLS-1$    // ~03A
		MLRI("Mlri", 6), //$NON-NLS-1$    // ~04A
		PNRI("Pnri",7),  //$NON-NLS-1$    // ~05A
		CRCT("Crct", 8),  //$NON-NLS-1$   // ~04A
		STAT("Stat", 9);                  // ~09A
		
		private int index;

		private String name;

		Columns(String name, int index)
		{
			this.name = name;
			this.index = index;
		}

		/**
		 * Get the column names from the Columns enum.
		 * @return a String representing the column names.
		 */
		public static String[] getColumnNames()
		{
			String[] columnNames = new String[Columns.values().length];
			
			for(int index = 0; index < columnNames.length; index++)
			{
				columnNames[index] = Columns.values()[index].getName();
			}
			
			return columnNames;
		}
		public int getIndex()
		{
			return index;
		}
		
		public String getName()
		{
			return name;
		}		
	}
	
	/**
	 * The VendorSubPartsCellRenderer extends from the <code>DefaultTableCellRenderer</code>
	 * to paint table rows like the <code>MFSComponentCellRenderer</code>.
	 * @author MFS Team
	 */
	private class VendorSubPartsCellRenderer extends DefaultTableCellRenderer
	{
		private static final long serialVersionUID = 5170580325744807665L;

		@Override
		/**
		 * {@inheritDoc}
		 */
		public Component getTableCellRendererComponent
	       (JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) 
	    {
	        Component cell = super.getTableCellRendererComponent(table, value, isSelected, 
	        														hasFocus, row, column);
	        
	        
	        // Take always the value of the IDSP column to determine the cell color
	        if(column != Columns.IDSP.getIndex())
	        {
	        	value = table.getModel().getValueAt(row, Columns.IDSP.getIndex());
        		//It is the COOC column, center the text ~06A
	        	if (column == Columns.COOC.getIndex())
	        	{
		        	((JLabel) cell).setHorizontalAlignment(SwingConstants.CENTER);
	        	}
	        }
	        else
	        {
	        	//It is the IDSP column, center the text
	        	((JLabel) cell).setHorizontalAlignment(SwingConstants.CENTER);
	        }
        	
	        if(value.equals("I")) //$NON-NLS-1$
	        {
				setBackground(isSelected ? Color.black : Color.cyan);
				setForeground(isSelected ? Color.cyan : Color.black);
	        } 
	        else
	        {
	        	value = table.getModel().getValueAt(row, Columns.CSNI.getIndex());  // ~03A
	        	Object value_mlri = table.getModel().getValueAt(row, Columns.MLRI.getIndex());     //~04A
	        	Object value_csni = table.getModel().getValueAt(row, Columns.CSNI.getIndex());     //~06A
        	
		        if(value.equals("1") || value_mlri.equals("1")) //$NON-NLS-1$       //~03A  //~04C
		        {	        	                                                    // ~03A
		        	setBackground(isSelected ? Color.black : Color.white);
					setForeground(isSelected ? Color.white : Color.black);
					
		        	if(column == Columns.INSQ.getIndex() && !value_csni.equals("1") )
		        	{//Set to gray if serial not need to be collected ~06A
		        		((JLabel) cell).setBackground(Color.LIGHT_GRAY);
		        	}						
		        }                                                                   // ~03A
		        else                                                                // ~03A
		        {                                                                   // ~03A
		        	setBackground(isSelected ? Color.black : Color.white);          // ~03A
					setForeground(isSelected ? Color.red : Color.red);		        // ~03A
		        }                                                                   // ~03A
	        }



	        return cell;
	    }
	}
	private static final long serialVersionUID = 1L;
	private JPanel containPanel = null;
	private JLabel titleLabel = null;
	private JLabel barCodeLabel = null;
	private JTextField barCodeTextField = null;
	private JLabel mctlLabel = null;
	private JTextField mctlTextField = null;
	private JLabel cooLabel = null;
	private JTextField cooTextField = null;
	private JLabel tipsLabel = null;
	private JLabel topLevelPNLabel = null;
	private JTextField topLevelPNTextField = null;
	private JLabel topLevelSNLabel = null;
	private JTextField topLevelSNTextField = null;
	private JScrollPane commoditiesScrollPane = null;
	private JTable commoditiesTable = null;
	private IGSDocumentTableModel commoditiesModel = null;
	private Hashtable<String, ArrayList<String>> commoditiesSubstitutes;           // ~04C
	private Hashtable<String, ArrayList<String>> commoditiesCoos;                  // ~05C
	private JButton printButton = null;
	private JButton serializeButton = null;
	private JButton closeButton = null;
	private JButton clearButton = null;
	private JButton reworkButton = null;                                           // ~09A
	private JButton rebuildButton = null;                                          // ~09A
	private JCheckBox autoPrintCheckBox = null;
	private JCheckBox autoSerializeCheckBox = null;
	private JCheckBox useVendorAsBuiltDataCheckBox = null;
	private JLabel autoPrintLabel = null;
	private JLabel autoSerializeLabel = null;
	/** The index of the active row. ~05A*/
	private int fieldActiveRow = -1;
	
	/* Needed for use through methods ~05A*/
	private String scannedMctl = null;
	private String scannedPN = null;
	private String scannedSN = null;
	private String scannedCO = null;
	private int emptyPartIndex = -1;
	private int partCompletedIndex = -1; // Initialize to -1 instead of 0 ~08C
	private int barcodeApplied = -1;
	private boolean reworkPerformed = false;                                                 //~10A
	
	/** A <code>Hashtable</code> to save coo values **/
	private Hashtable<String, String> cooHash = null;	

	private static String TIPS_TOPLEVEL = "Enter Top Level Part"; //$NON-NLS-1$
	private static String TIPS_COMMODITIES = "Enter 11S Commodities"; //$NON-NLS-1$
	private static String TIPS_SNNOTNEEDED = "No Need to Collect Serial Number"; //$NON-NLS-1$  ~04A
	private static String TIPS_COONOTNEEDED = "No Need to Collect COO"; //$NON-NLS-1$           ~04A	
	
	private JLabel useVendorAsBuiltDataLabel = null;
	
	/** The Logger used for vendor sub parts */
	private MFSVendorSubPartsController controller = null;
		
	/**
	 * This method initializes 
	 * 
	 */
	public MFSVendorSubPartsDialog(MFSFrame parent, MFSVendorSubPartsController logger) {
		super(parent, MFSVendorSubPartsDialog.class.getSimpleName());
		
		this.controller = logger;
		
		initialize();
		addMyListeners();
	}

	/**
	 * Invoked when an action occurs.
	 * @param ae the <code>ActionEvent</code>
	 */
	public void actionPerformed(ActionEvent ae)
	{
		final Object source = ae.getSource();

		if(source == printButton) {
			controller.print(mctlTextField.getText(), topLevelPNTextField.getText());
		}
		else if(source == serializeButton) {
			if(this.getTopLevelSNTextField().getText().length() == 0) {
				if(this.mctlTextField.getText().length() != 0 ||
						this.topLevelPNTextField.getText().length() != 0 ) {
					this.setAutoPrintSelected(true);  // Allow auto-printing when Serialize button pressed ~06A
					this.controller.rtvSerializeSubAssemblyPartsOffline(
							this.mctlTextField.getText(), 
							this.topLevelPNTextField.getText());
					this.barCodeTextField.requestFocusInWindow();
				}
			}				
		}
		else if(source == clearButton) {
			if(this.controller.isModifyParts()) 
			{
				if(countPartsToInstall() == 0 ) 
				{					
					// Check for EFFICIENCYON*LOGVENDORPARTS config entry    ~05A
					boolean efficiencyLog = false;
					if(MFSConfig.getInstance().containsConfigEntry("EFFICIENCYON*LOGVENDORPARTS"))//$NON-NLS-1$
					{
						efficiencyLog = true;
					}
					
					/*
					 *  - Complete MCTL since auto-end functionality is not enabled  ~05A
					 *  - If reworkPerformed is set, that means
					 */
					
					if (!efficiencyLog || this.reworkPerformed)
					{
						MFSPart partInfo = getSubPartFromTable(this.partCompletedIndex);
						if(this.controller.logSubAssemblyPart(partInfo.getMctl(), partInfo.getCrct(), 
								partInfo.getInpn(), partInfo.getInsq(), partInfo.getCooc(),partInfo.getStat(),true))   						                                                                        
						{   							
							if(this.controller.isOffline()) {
								resetDefaults(partInfo.getMctl());
							}							
							if(this.autoPrintCheckBox.isSelected())
							{
								this.controller.print(partInfo.getMctl(), partInfo.getInpn());        // Use INPN from partInfo instead of scanned ~05C
							}
							/* Process completion (sub complete)so reset value for partCompletedIndex ~08A*/
							this.partCompletedIndex = -1;
						}											
					}
					else
					{ // ~06A
						this.resetDefaults(null);
					}
					
				    this.controller.setSubComplete(true); //~02A
				    
				    // End dialog when sub is complete and we are on Direct Work (inline)  ~05A
				    if (!this.controller.isOffline())
				    {
				    	this.dispose();
				    }
				}
				else 
				{
					if(IGSMessageBox.showYesNoMB(this, this.getClass().getSimpleName(), 
							"Do you want to Clear un-installed commodity parts?", null)) { //$NON-NLS-1$
						this.controller.deleteSubAssemblyPart(this.mctlTextField.getText());
						this.resetDefaults(null);
					}									
				}
				this.barCodeTextField.requestFocusInWindow();
			}	
			else {
				/*
				 * - Execute VRFYPNPLUS to remove ACTIVE status for Work Unit  ~09A
				 * - Only Execute VRFYPNPLUS if not a Rework action            ~10C
				 */
				if ((!this.controller.getSubDataType().equals("N") && !this.controller.getSubDataType().equals(" ")) 
					&& !this.reworkPerformed)                                //~10C
				{
					MFSPart partInfo = getSubPartFromTable(0);
					this.controller.logSubAssemblyPart(partInfo.getMctl(), partInfo.getCrct(), 
							partInfo.getInpn(), partInfo.getInsq(), partInfo.getCooc(),partInfo.getStat(),false);   						                                                                        
									
				}
			    /* If working inline (Direct work), it means that we don't have input on Vendor
			     * dialog and since End button was pressed, we need to close it.           ~07A
			     */
				if (!this.controller.isOffline())
				{
					this.dispose();
				}
				else
				{
					this.resetDefaults(null);
				}					
			}			
		}
		else if(source == closeButton) {
			this.controller.setSubComplete(false);
			
			if(this.controller.isModifyParts()) {
				if(countPartsToInstall() == 0 ) {
					/* Since we are in modify state and parts to install equal to 0, that
					 * means the sub is complete collecting data and the close button was 
					 * pressed without end button. In that case, invoke end button to
					 * complete the sub properly                                   ~07A*/
					this.clearButton.doClick();                                    					
					this.dispose();                                              //~02A
				}
				else {
					if(IGSMessageBox.showYesNoMB(this, this.getClass().getSimpleName(), 
							"Do you want to Clear un-installed commodity parts?", null)) { //$NON-NLS-1$
						this.controller.deleteSubAssemblyPart(this.mctlTextField.getText()); 
						this.dispose();
					}									
				}
			}	
			else {
				/*
				 * - Execute VRFYPNPLUS to remove ACTIVE status for Work Unit  ~09A
				 * - Only Execute VRFYPNPLUS if not a Rework action            ~10C
				 */
				if (this.controller.getSubDataType().equals(" ") || this.controller.getSubDataType().equals("N") 
				    || this.reworkPerformed)	                             //~10C			     
				{
					this.reworkPerformed = false;                            //~10A					
					this.dispose();
				}
				else if (!this.controller.getSubDataType().equals("N"))                                //~10C
				{
					MFSPart partInfo = getSubPartFromTable(0);
					if(this.controller.logSubAssemblyPart(partInfo.getMctl(), partInfo.getCrct(), 
							partInfo.getInpn(), partInfo.getInsq(), partInfo.getCooc(),partInfo.getStat(),false))   						                                                                        
					{  
						this.dispose();
					}					
				}				
			}
		} // ~09A Add Rework option
		else if(source == reworkButton) {
			if(fieldActiveRow == -1)
			{
				IGSMessageBox.showOkMB(getParentFrame(), null, "No part selected", null);
			}
			else
			{
				String tempCSNI = null;
				String tempMLRI = null;
				
				tempCSNI = this.commoditiesModel.getValueAt(this.fieldActiveRow, Columns.CSNI.getIndex()).toString();  
			    tempMLRI = this.commoditiesModel.getValueAt(this.fieldActiveRow, Columns.MLRI.getIndex()).toString();
			    
			    if(!tempCSNI.equals("1") && !tempMLRI.equals("1"))
			    {
			    	IGSMessageBox.showOkMB(getParentFrame(), null, "No valid part to rework", null);
			    }
			    else
			    {
			    	this.controller.setModifyParts(true);
			    	clearSubassemblyPartData(fieldActiveRow);
			    }
			}
		}
		// ~09A Add Rebuild option
		else if(source == rebuildButton) {
			if(IGSMessageBox.showYesNoMB(this, this.getClass().getSimpleName(), 
					"Do you want to Rebuild the Subassembly?", null)) { //$NON-NLS-1$
				String mctl = this.getMctlTextField().getText();
				String topPN = this.getTopLevelPNTextField().getText();
				String topSN = this.getTopLevelSNTextField().getText();
				
				this.resetDefaults(null);
				this.controller.rtvRebuildSubAssemblyPartsOffline(mctl, topPN, topSN, true); 
			
			}						
		}
	}
	
	/**
	 * Invoked when a key is pressed.
	 * @param ke the <code>KeyEvent</code>
	 */
	public void keyPressed(KeyEvent ke)
	{
		final int keyCode = ke.getKeyCode();
		JTable tmpTable = null;
		
		if (keyCode == KeyEvent.VK_ENTER)
		{
			final Object source = ke.getSource();
			
			if(source instanceof JButton)
			{
				JButton button = (JButton) source;
				button.requestFocusInWindow();
				button.doClick();
			}
			else if(source == barCodeTextField)
			{
				this.rcvBcInput();
			}
		}
		else if (keyCode == KeyEvent.VK_F2)
		{
				this.serializeButton.doClick();				
		}
		else if (keyCode == KeyEvent.VK_F3)
		{
				this.clearButton.doClick();				
		}
		else if (keyCode == KeyEvent.VK_F4)
		{ //~09A
				this.reworkButton.doClick();				
		}
		else if (keyCode == KeyEvent.VK_F5)
		{ //~09A
				this.rebuildButton.doClick();				
		}
		else if (keyCode == KeyEvent.VK_F6)
		{ //~06A
				this.printButton.doClick();				
		}
		else if (keyCode == KeyEvent.VK_F7)
		{ //~06A
				this.closeButton.doClick();				
		} // ~07A Update row index selected if arrow up pressed 
		else if (keyCode == KeyEvent.VK_UP)
		{
			tmpTable = (JTable) ke.getSource();
			if (tmpTable.getSelectedRow() > 0)
			{
				fieldActiveRow = tmpTable.getSelectedRow() - 1;
			}		
		}// ~07A Update row index selected if arrow down pressed
		else if (keyCode == KeyEvent.VK_DOWN)
		{
			tmpTable = (JTable) ke.getSource();
			if (fieldActiveRow < (tmpTable.getRowCount() -1))
			{
				fieldActiveRow = tmpTable.getSelectedRow() + 1;
			}				
		}		
		else if (keyCode == KeyEvent.VK_ESCAPE)
		{
			this.closeButton.doClick();
		}
	}
	
	
	/** ~05C
	 * Invoked when the mouse has been pressed on a <code>Component</code>.
	 * @param me the <code>MouseEvent</code>
	 */
	public void mouseClicked(MouseEvent me)
	{      
		if (me.getClickCount() == 2)
		{
			JTable tmpTable = (JTable) me.getSource();
			
            fieldActiveRow = tmpTable.getSelectedRow();
		}
	}
	
	/** ~05A
	 * Invoked when the mouse has been pressed on a <code>Component</code>.
	 * Does nothing.
	 * @param me the <code>MouseEvent</code>
	 */
	public void mousePressed(MouseEvent me)
	{
		if (me.getClickCount() == 1)
		{
			JTable tmpTable = (JTable) me.getSource();
			
            fieldActiveRow = tmpTable.getSelectedRow();
		}
	}

	/** ~05A
	 * Invoked when the mouse has been released on a <code>Component</code>.
	 * Does nothing.
	 * @param me the <code>MouseEvent</code>
	 */
	public void mouseReleased(MouseEvent me)
	{
		//Does nothing
	}

	/** ~05A
	 * Invoked when the mouse enters a <code>Component</code>. Does nothing.
	 * @param me the <code>MouseEvent</code>
	 */
	public void mouseEntered(MouseEvent me)
	{
		//Does nothing
	}	
	
	/** ~05A
	 * Invoked when the mouse exits a <code>Component</code>. Does nothing.
	 * @param me the <code>MouseEvent</code>
	 */
	public void mouseExited(MouseEvent me)
	{
		//Does nothing
	}	

	/**
	 * Add this dialog listeners
	 */
	private void addMyListeners()
	{
		barCodeTextField.addKeyListener(this);		
		
		autoPrintCheckBox.addKeyListener(this);
		autoSerializeCheckBox.addKeyListener(this);
		useVendorAsBuiltDataCheckBox.addKeyListener(this);
				
		printButton.addActionListener(this);
		printButton.addKeyListener(this);
		
		serializeButton.addActionListener(this);
		serializeButton.addKeyListener(this);
		
		// ~09A Add listeners for reworkButton and rebuildButton
		reworkButton.addActionListener(this);
		reworkButton.addKeyListener(this);
		
		rebuildButton.addActionListener(this);
		rebuildButton.addKeyListener(this);

		clearButton.addActionListener(this);
		clearButton.addKeyListener(this);

		closeButton.addActionListener(this);
		closeButton.addKeyListener(this);
		
		// Add mouse listeners for commoditiesTable  ~05A
		commoditiesTable.addKeyListener(this);
		commoditiesTable.addMouseListener(this);
	}
	
	/**
	 * Remove this dialog listeners
	 */
	private void removeMyListeners()
	{
		barCodeTextField.removeKeyListener(this);
		
		autoPrintCheckBox.removeKeyListener(this);
		autoSerializeCheckBox.removeKeyListener(this);
		useVendorAsBuiltDataCheckBox.removeKeyListener(this);
		
		printButton.removeActionListener(this);
		printButton.removeKeyListener(this);
		
		serializeButton.removeActionListener(this);
		serializeButton.removeKeyListener(this);

		// ~09A Remove listeners for reworkButton and rebuildButton
		reworkButton.removeActionListener(this);
		reworkButton.removeKeyListener(this);
		
		rebuildButton.removeActionListener(this);
		rebuildButton.removeKeyListener(this);		
		
		clearButton.removeActionListener(this);
		clearButton.removeKeyListener(this);

		closeButton.removeActionListener(this);
		closeButton.removeKeyListener(this);
		
		// Remove mouse listeners for commoditiesTable  ~05A
		commoditiesTable.addKeyListener(this);
		commoditiesTable.addMouseListener(this);		
	}

	/**
	 * This method initializes containPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getContainPanel() {
		if (containPanel == null) {
	
			titleLabel = createLabel("Vendor SubAssembly Parts"); //$NON-NLS-1$
			titleLabel.setBounds(new Rectangle(50, 4, 575, 20));
			titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
			
			barCodeLabel = createSmallNameLabel("Enter Bar Code:"); //$NON-NLS-1$
			barCodeLabel.setBounds(new Rectangle(95, 45, 120, 20));
			
			tipsLabel = createSmallNameLabel(TIPS_TOPLEVEL); 
			tipsLabel.setBounds(new Rectangle(50, 80, 575, 20));
			tipsLabel.setFont(new Font("Dialog", Font.BOLD, 12)); //$NON-NLS-1$
			tipsLabel.setForeground(Color.red);
			tipsLabel.setHorizontalAlignment(SwingConstants.CENTER);
			tipsLabel.setToolTipText(tipsLabel.getText());
			
			mctlLabel = createSmallNameLabel("MCTL:"); //$NON-NLS-1$
			mctlLabel.setBounds(new Rectangle(55, 120, 120, 20));
			
			cooLabel = createSmallNameLabel("COO:"); //$NON-NLS-1$
			cooLabel.setBounds(new Rectangle(290, 120, 120, 20));
			
			topLevelPNLabel = createSmallNameLabel("Top Level PN:"); //$NON-NLS-1$
			topLevelPNLabel.setBounds(new Rectangle(55, 160, 120, 20));
			
			topLevelSNLabel = createSmallNameLabel("Top Level SN:"); //$NON-NLS-1$
			topLevelSNLabel.setBounds(new Rectangle(290, 160, 120, 20));
			
			autoPrintLabel = createSmallNameLabel("Auto Print:"); //$NON-NLS-1$
			autoPrintLabel.setBounds(new Rectangle(143, 360, 61, 20));
			
			autoSerializeLabel = createSmallNameLabel("Auto Serialize:"); //$NON-NLS-1$
			autoSerializeLabel.setBounds(new Rectangle(254, 360, 85, 20));
			
			useVendorAsBuiltDataLabel = createSmallNameLabel("Use Vendor As-Built Data:"); //$NON-NLS-1$
			useVendorAsBuiltDataLabel.setBounds(new Rectangle(380, 360, 150, 20));
	
			containPanel = new JPanel();
			containPanel.setSize(new Dimension(680, 500));
			containPanel.setLayout(null);
			containPanel.setPreferredSize(new Dimension(680, 500));
			containPanel.add(titleLabel, null);
			containPanel.add(barCodeLabel, null);
			containPanel.add(getBarCodeTextField(), null);
			containPanel.add(mctlLabel, null);
			containPanel.add(getMctlTextField(), null);
			containPanel.add(cooLabel, null);
			containPanel.add(getCooTextField(), null);
			containPanel.add(tipsLabel, null);
			containPanel.add(topLevelPNLabel, null);
			containPanel.add(getTopLevelPNTextField(), null);
			containPanel.add(topLevelSNLabel, null);
			containPanel.add(getTopLevelSNTextField(), null);
			containPanel.add(getCommoditiesScrollPane(), null);
			containPanel.add(getPrintButton(), null);
			containPanel.add(getSerializeButton(), null);
			containPanel.add(getReworkButton(), null);                 // ~09A
			containPanel.add(getRebuildButton(), null);	               // ~09A		
			containPanel.add(getClearButton(), null);
			containPanel.add(getCloseButton(), null);
			containPanel.add(getAutoPrintCheckBox(), null);
			containPanel.add(getAutoSerializeCheckBox(), null);
			containPanel.add(getUseVendorAsBuiltDataCheckBox(), null);
			containPanel.add(autoPrintLabel, null);
			containPanel.add(autoSerializeLabel, null);
			containPanel.add(useVendorAsBuiltDataLabel, null);
		}
		return containPanel;
	}

	/**
	 * This method initializes barCodeTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getBarCodeTextField() {
		if (barCodeTextField == null) {
			barCodeTextField = createTextField(38, 0);
			/* Use a fixed value for width and height instead of getPreferredSize to avoid
			 * that the text fields get overlapped with Chinese (double byte ??)      ~07C
			 */
			barCodeTextField.setBounds(new Rectangle(220, 45, 308, 24));
		}
		return barCodeTextField;
	}
	
	/**
	 * This method initializes mctlTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getMctlTextField() {
		if (mctlTextField == null) {
			mctlTextField = createTextField(18, 0);
			mctlTextField.setName("MCTL"); //$NON-NLS-1$
			mctlTextField.setEditable(false);
			/* Use a fixed value for width and height instead of getPreferredSize to avoid
			 * that the text fields get overlapped with Chinese (double byte ??)      ~07C
			 */
			mctlTextField.setBounds(new Rectangle(180, 120, 148,24));
		}
		return mctlTextField;
	}

	/**
	 * This method initializes topLevelPNTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTopLevelPNTextField() {
		if (topLevelPNTextField == null) {
			topLevelPNTextField = createTextField(18, 0);
			topLevelPNTextField.setName("PN"); //$NON-NLS-1$
			topLevelPNTextField.setEditable(false);
			/* Use a fixed value for width and height instead of getPreferredSize to avoid
			 * that the text fields get overlapped with Chinese (double byte ??)      ~07C
			 */		
			topLevelPNTextField.setBounds(new Rectangle(180, 160, 148, 24));
		}
		return topLevelPNTextField;
	}

	/**
	 * This method initializes topLevelSNTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTopLevelSNTextField() {
		if (topLevelSNTextField == null) {
			topLevelSNTextField = createTextField(18, 0);
			topLevelSNTextField.setName("SN"); //$NON-NLS-1$
			topLevelSNTextField.setEditable(false);
			/* Use a fixed value for width and height instead of getPreferredSize to avoid
			 * that the text fields get overlapped with Chinese (double byte ??)      ~07C
			 */					
			topLevelSNTextField.setBounds(new Rectangle(415, 160, 148, 24));
		}
		return topLevelSNTextField;
	}

	/**
	 * This method initializes cooTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getCooTextField() {
		if (cooTextField == null) {
			cooTextField = createTextField(5, 0);
			cooTextField.setName("COO"); //$NON-NLS-1$
			cooTextField.setEditable(false);
			/* Use a fixed value for width and height instead of getPreferredSize to avoid
			 * that the text fields get overlapped with Chinese (double byte ??)      ~07C
			 */								
			cooTextField.setBounds(new Rectangle(415, 120, 44, 24));
		}
		return cooTextField;
	}

	/**
	 * This method initializes commoditiesScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getCommoditiesScrollPane() {
		if (commoditiesScrollPane == null) {
			commoditiesScrollPane = new JScrollPane();
			// ~09A Change width from 575 to 652 to properly fit on the main panel
			commoditiesScrollPane.setBounds(new Rectangle(50, 200, 652, 150));
			commoditiesScrollPane.setViewportView(getCommoditiesTable());
		}
		return commoditiesScrollPane;
	}
	
	/**
	 * This method initializes commoditiesTable	
	 * 	
	 * @return javax.swing.JTable	
	 */
	private JTable getCommoditiesTable() {
		if (commoditiesTable == null) {
			
			String[] columnNames = Columns.getColumnNames();
			commoditiesModel = new IGSDocumentTableModel(columnNames);
			
			// Table
			commoditiesTable = new JTable(commoditiesModel);
			commoditiesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); 
			commoditiesTable.setFont(MFSConstants.MEDIUM_MONOSPACED_FONT);
			commoditiesTable.getTableHeader().setFont(MFSConstants.MEDIUM_DIALOG_FONT);
			commoditiesTable.setRowSelectionAllowed(true);                               // ~05A
			
			TableColumnModel tcm = commoditiesTable.getColumnModel();
			// Columns
			//Set Part Number column  ~06A
			TableColumn column = tcm.getColumn(tcm.getColumnIndex(Columns.INPN.getName()));
			// ~09A Change Width from 105 to 150 for Sub PN so the value is not cut off on the panel
			column.setMaxWidth(150);
			column.setPreferredWidth(150);			
			//Set Serial Number column  ~06A
			column = tcm.getColumn(tcm.getColumnIndex(Columns.INSQ.getName()));
			// ~09A Change Width from 105 to 150 for Sub SN so the value is not cut off on the panel			
			column.setMaxWidth(150);
			column.setPreferredWidth(150);
			//Set Description 
			column = tcm.getColumn(tcm.getColumnIndex(Columns.CDES.getName())); //~06C
			column.setPreferredWidth(150);
			//Change Max and Preferred from 65 to 45 ~06C
			column = tcm.getColumn(tcm.getColumnIndex(Columns.IDSP.getName()));
			column.setMaxWidth(45);
			column.setPreferredWidth(45);
			//Set COO column  ~06A
			column = tcm.getColumn(tcm.getColumnIndex(Columns.COOC.getName()));
			column.setMaxWidth(45);
			column.setPreferredWidth(45);

			// Apply the cell renderer
			for(String columnName : columnNames)
			{
				commoditiesTable.getColumn(columnName).setCellRenderer(new VendorSubPartsCellRenderer());
			}
			
			// Remove column from the view
			tcm.removeColumn(tcm.getColumn(tcm.getColumnIndex(Columns.CRCT.getName())));
			
			// Remove CSNI column from the view
			tcm.removeColumn(tcm.getColumn(tcm.getColumnIndex(Columns.CSNI.getName())));       //~03A
			// Remove MLRI column from the view
			tcm.removeColumn(tcm.getColumn(tcm.getColumnIndex(Columns.MLRI.getName())));       //~04A
			tcm.removeColumn(tcm.getColumn(tcm.getColumnIndex(Columns.PNRI.getName())));       //~04A
			// Remove STAT column from the view
			tcm.removeColumn(tcm.getColumn(tcm.getColumnIndex(Columns.STAT.getName())));       //~09A
			
		}
		return commoditiesTable;
	}

	/**
	 * This method initializes autoPrintCheckBox	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getAutoPrintCheckBox() {
		if (autoPrintCheckBox == null) {
			autoPrintCheckBox = new JCheckBox();
			autoPrintCheckBox.setBounds(new Rectangle(205, 360, 20, 20));
			autoPrintCheckBox.setPreferredSize(new Dimension(20, 20));
		}
		return autoPrintCheckBox;
	}

	/**
	 * This method initializes autoSerializeCheckBox	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getAutoSerializeCheckBox() {
		if (autoSerializeCheckBox == null) {
			autoSerializeCheckBox = new JCheckBox();
			autoSerializeCheckBox.setBounds(new Rectangle(340, 360, 20, 20));
			autoSerializeCheckBox.setPreferredSize(new Dimension(110, 20));
		}
		return autoSerializeCheckBox;
	}

	/**
	 * This method initializes useVendorAsBuiltDataCheckBox	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getUseVendorAsBuiltDataCheckBox() {
		if (useVendorAsBuiltDataCheckBox == null) {
			useVendorAsBuiltDataCheckBox = new JCheckBox();
			useVendorAsBuiltDataCheckBox.setPreferredSize(new Dimension(20, 20));
			useVendorAsBuiltDataCheckBox.setBounds(new Rectangle(532, 360, 20, 20));
		}
		return useVendorAsBuiltDataCheckBox;
	}

	/**
	 * This method initializes printButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getPrintButton() {
		if (printButton == null) {
			printButton = createButton("(F6) Print", 'P'); //$NON-NLS-1$  //~06C
			//~09C Change X position for printButton from 90 to 20
			printButton.setBounds(new Rectangle(20, 400, 100,
														printButton.getPreferredSize().height));
		}
		return printButton;
	}

	/**
	 * This method initializes serializeButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getSerializeButton() {
		if (serializeButton == null) {
			serializeButton = createButton("(F2) Serialize", 'S'); //$NON-NLS-1$
			//~09C Change X position for serializeButton from 208 to 123
			serializeButton.setBounds(new Rectangle(123, 400, 130, serializeButton.getPreferredSize().height));
			serializeButton.setEnabled(false);
		}
		return serializeButton;
	}

	/**
	 * This method initializes clearButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getClearButton() {
		if (clearButton == null) {
			clearButton = createButton("(F3) End", 'E'); //$NON-NLS-1$  //~05C Change Clear for End text
			//~09C Change X position for clearButton from 354 to 511
			clearButton.setBounds(new Rectangle(511, 400, 100,
										clearButton.getPreferredSize().height));
			/* Enable button so it can be used to clear the dialog window and/or 
			 * start/end a subassembly. This behavior is true for offline only, for
			 * direct work it should be activated only when sub is complete ~07C*/
			if (this.controller.isOffline())
			{
				clearButton.setEnabled(true);	
			}
			else
			{
				clearButton.setEnabled(false);
			}
		}
		return clearButton;
	}

	/**
	 * This method initializes closeButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getCloseButton() {
		if (closeButton == null) {
			closeButton = createButton("(F7) Close", 'C'); //$NON-NLS-1$  //~06C
			//~09C Change X position for closeButton from 472 to 613
			closeButton.setBounds(new Rectangle(613, 400, 120,
										closeButton.getPreferredSize().height));  //Change width ~06C
		}
		return closeButton;
	}

	/** ~09A
	 * This method initializes reworkButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getReworkButton() {
		if (reworkButton == null) {
			reworkButton = createButton("(F4) Rework", 'R'); //$NON-NLS-1$ 
			reworkButton.setBounds(new Rectangle(255, 400, 120,
					reworkButton.getPreferredSize().height));
			reworkButton.setEnabled(false);
		}
		return reworkButton;
	}
	
	/** ~09A
	 * This method initializes rebuildButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getRebuildButton() {
		if (rebuildButton == null) {
			rebuildButton = createButton("(F5) Rebuild", 'A'); //$NON-NLS-1$ 
			rebuildButton.setBounds(new Rectangle(378, 400, 130,
					rebuildButton.getPreferredSize().height));
			rebuildButton.setEnabled(false);
		}
		return rebuildButton;
	}		
	
	/**
	 * Checks if the sub-assembly is completed by verifying that all sub parts
	 * have their serials/coos scanned.
	 * @return true if top level part is completed.
	 */
	private int countPartsToInstall()
	{
		int partsToInstall = 0;
		int dataSize = commoditiesModel.getData().size();
		int columnIndex = Columns.INSQ.getIndex();
		int columnIndexcooc = Columns.COOC.getIndex();                                 //~04A		
		int columnIndexcnsi = Columns.CSNI.getIndex();                                 //~03A
		int columnIndexmlri = Columns.MLRI.getIndex();                                 //~04A
		int columnIndexidsp = Columns.IDSP.getIndex();                                 //~04A
		 
		String sn = null;
        String cooc = null;                                                            //~04A		
		String csni = null;                                                            //~03A
		String mlri = null;                                                            //~04A
		String idsp = null;                                                            //~04A
		
		for(int rowIndex = 0; rowIndex < dataSize; rowIndex++)
		{
			sn = commoditiesModel.getValueAt(rowIndex, columnIndex).toString();
			cooc = commoditiesModel.getValueAt(rowIndex, columnIndexcooc).toString();  //~04A
			csni = commoditiesModel.getValueAt(rowIndex, columnIndexcnsi).toString();  //~03A
			mlri = commoditiesModel.getValueAt(rowIndex, columnIndexmlri).toString();  //~04A			
			idsp = commoditiesModel.getValueAt(rowIndex, columnIndexidsp).toString();  //~04A			
			
			/*
			 * Change operator from || to && on the conditions to check idsp equals to 'A' 
			 * on csni and mlri.                                                      ~09C
			 */
			if(((null != csni && csni.trim().equals("1")) && //$NON-NLS-1$ //~03A
					((null != sn && sn.trim().length() == 0) && idsp.trim().equals("A")))  || 	
					((null != mlri && mlri.trim().equals("1")) &&
					((null != cooc && cooc.trim().length() == 0) && idsp.trim().equals("A")))) //$NON-NLS-1$ //~04A						
				{
					partsToInstall++;
				}				
		}
		return partsToInstall;
	}

	/** ~09A
	 * Checks if the sub-assembly has parts with Rework flag
	 * 
	 * @return number of parts to rework
	 */
	private int countPartsWithRework()
	{
		int partsToRework = 0;
		int dataSize = commoditiesModel.getData().size();
		int columnIndex = Columns.STAT.getIndex();
		int columnIndexidsp = Columns.IDSP.getIndex();                        
			 
		String stat = null;
		String idsp = null;                                                        
        	
		for(int rowIndex = 0; rowIndex < dataSize; rowIndex++)
		{
			stat = commoditiesModel.getValueAt(rowIndex, columnIndex).toString();
			idsp = commoditiesModel.getValueAt(rowIndex, columnIndexidsp).toString();

			if(null != stat && stat.trim().equals("R") && idsp.trim().equals("A")) 						
			{
				partsToRework++;
			}				
		}
		return partsToRework;
	}
	
	/**
	 * Gets the <code>MFSPart</code> for the given row index.
	 * @param rowIndex the commoditiesTable row index.
	 * @return the <code>MFSPart</code>.
	 */
	private MFSPart getSubPartFromTable(int rowIndex)
	{
		Object[] row = this.commoditiesModel.getData().get(rowIndex);
		MFSPart subPart = new MFSPart();
		subPart.setMctl(this.mctlTextField.getText()); // all sub-parts have the subassembly mctl
		subPart.setInpn(row[Columns.INPN.getIndex()].toString());
		subPart.setInsq(row[Columns.INSQ.getIndex()].toString());
		subPart.setDesc(row[Columns.CDES.getIndex()].toString());
		subPart.setIdsp(row[Columns.IDSP.getIndex()].toString());
		subPart.setCrct(row[Columns.CRCT.getIndex()].toString());
		subPart.setCsni(row[Columns.CSNI.getIndex()].toString());            //~03A
		subPart.setMlri(row[Columns.MLRI.getIndex()].toString());            //~04A
		subPart.setPnri(row[Columns.PNRI.getIndex()].toString());            //~05A
		subPart.setCooc(row[Columns.COOC.getIndex()].toString());            //~04A
		subPart.setStat(row[Columns.STAT.getIndex()].toString());            //~09A
		
		return subPart;
	}

	/**
	 * This method initializes this dialog
	 */
	private void initialize() {
		// ~09A Change width of main panel from 680 to 757 to make room for new buttons (Rework/Rebuild)		
        setPreferredSize(new Dimension(757, 500)); 
        // Creates the layout
        setContentPane(getContainPanel());
		setModal(true);
		pack();		
		// Set location in main screen
		setLocationRelativeTo(getParentFrame());		
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
	}

	/**
	 * Clears all the graphic components. Note this does not affect the check boxes.
	 */
	private void resetDefaults(String mctlCompleted)
	{
		if(mctlCompleted != null) {
			this.tipsLabel.setText("MCTL " + mctlCompleted + " is complete. " + TIPS_TOPLEVEL); //$NON-NLS-1$ //$NON-NLS-2$
		}
		else {
			this.tipsLabel.setText(TIPS_TOPLEVEL);
		}
		this.mctlTextField.setText(""); //$NON-NLS-1$
		this.topLevelPNTextField.setText(""); //$NON-NLS-1$
		this.topLevelSNTextField.setText(""); //$NON-NLS-1$
		this.cooTextField.setText(""); //$NON-NLS-1$
		
		this.commoditiesModel.clearData();
		this.serializeButton.setEnabled(false);

		this.printButton.setEnabled(false);   //~06A
		this.rebuildButton.setEnabled(false); //~09A
		this.reworkButton.setEnabled(false);  //~09A
		
		this.barCodeTextField.requestFocusInWindow();
	}

	/**
	 * Decodes the barcode field input.
	 */
	private void rcvBcInput()
	{	
		// Check for EFFICIENCYON*LOGVENDORPARTS config entry    ~06A
		boolean efficiencyClientLog= false;
		if(MFSConfig.getInstance().containsConfigEntry("EFFICIENCYON*LOGVENDORPARTS"))//$NON-NLS-1$
		{
			efficiencyClientLog = true;
		}
		
		try
		{
			removeMyListeners();
			
			MFSBCBarcodeDecoder barcode = MFSBCBarcodeDecoder.getInstance();
			String tempBarcodeText = this.barCodeTextField.getText();
			this.barCodeTextField.setText(""); //$NON-NLS-1$	
			barcode.setMyBarcodeInput(tempBarcodeText);			
			
			barcode.initializeDecoder();
			// Start decoding
			if(mctlTextField.getText().length() == 0) {
				/* if retrieving sub, allow decoding for G CSNI */
				MFSBCIndicatorValue bcIndVal = new MFSBCIndicatorValue();
					bcIndVal.setCSNI("G"); //$NON-NLS-1$
				barcode.setMyBCIndicatorValue(bcIndVal);
			}
			barcode.decodeBarcodeFor(this);
				
			// invalid Barcode
			if(0 != barcode.getBCMyPartObject().getRC())
			{
				throw new Exception(barcode.getBCMyPartObject().getErrorString());
			}	
			this.scannedMctl = barcode.getBCMyPartObject().getWU();
			this.scannedPN = barcode.getBCMyPartObject().getPN();
			this.scannedSN = barcode.getBCMyPartObject().getSN();
			this.scannedCO = barcode.getBCMyPartObject().getCO();
			
			/* Validate if the scanned barcode has data we require */
			boolean validScannedCode = false;
			
			if(this.scannedMctl.length() > 0) {
				validScannedCode = true;
			}
			if(this.scannedPN.length() > 0) {
				validScannedCode = true;
			}
			if(this.scannedSN.length() > 0 ) {
				/* Check if the serial number has the correct length and other validations ~07A*/
				if(validateSerialNumber(topLevelPNTextField.getText(),this.scannedSN))
				{
					validScannedCode = true;
				}
				else
				{
					throw new Exception("Error: Serial number specified is not valid"); //$NON-NLS-1$
				}
			}
			if(this.scannedCO.length() > 0 ) {                  //~04A. Allow to scan COO for commodities
				if(mctlTextField.getText().length() > 0 &&
						this.controller.isModifyParts())   //~04A 
				{                                          //~04A
					validScannedCode = true;               //~04A
				}                                          //~04A
			}			                                   //~04A
			else if(!validScannedCode) {
				if(this.scannedCO.length() > 0) {
					throw new Exception("Warning: Can NOT change the Country of Origin for a Top Part"); //$NON-NLS-1$  ~04C
				}
				else {
					throw new Exception("Invalid Barcode"); //$NON-NLS-1$
				}
			}
			
			int partsToScann = this.countPartsToInstall();   // ~06A
			int partsToRework = this.countPartsWithRework(); // ~09A
			/*
			 * Save status on ReWork for using when End/Close button
			 */
			if(partsToRework > 0)
			{
				reworkPerformed = true;
			}
			/* No Subassembly is present, so is a new Top Level part */
			if((mctlTextField.getText().length() == 0) ||
					 ((partsToScann == 0 ||
					 !this.controller.isModifyParts()) && (this.controller.getSubDataType().equals("C") || this.controller.getSubDataType().equals("E")) && efficiencyClientLog) ||
					 ((partsToScann == 0 ||
				     !this.controller.isModifyParts()) &&
					(this.controller.getSubDataType().equals("N") && this.controller.isSubComplete()) && efficiencyClientLog))    // ~06C
			{
				// Clear dialog ~06C
				if (!mctlTextField.getText().equals("") && !topLevelPNTextField.getText().equals("") && !topLevelSNTextField.getText().equals("") )
				{
					this.resetDefaults(null);                                       
				}

				/* Call verfypnplus to retrieve the Sub assembly if...*/
				if (this.scannedMctl.length() > 0){ 
					/* Scanned an MCTL */							
					this.controller.rtvSubAssemblyPartsOffline(this.scannedMctl, null, null);
				}
				else if (this.scannedPN.length() > 0 && this.scannedSN.length() > 0) { 
					/* Scanned both PN and SN in the same barcode */
					this.controller.rtvSubAssemblyPartsOffline(null, this.scannedPN, this.scannedSN);
				}
				else if (this.scannedPN.length() > 0) { 
					/* Only part number was scanned */
					if(this.getTopLevelSNTextField().getText().length() > 0) {
						/* Scanned PN and SN already scanned previously */
						this.controller.rtvSubAssemblyPartsOffline(null, this.scannedPN, this.getTopLevelSNTextField().getText());								
					}
					else {						
						if(this.isAutoSerializeSelected()) {
							this.setAutoPrintSelected(true);  // Allow auto-printing when auto-serialize enabled ~06A
							this.controller.rtvSubAssemblyPartsOffline(null, this.scannedPN, null);
						}
						else {
							/* Just save the part number */
							this.getTopLevelPNTextField().setText(this.scannedPN);
							//If offline, then enable Serialize button ~06A
							if (this.controller.isOffline())
							{
								this.serializeButton.setEnabled(true); 	
							}
						}
					}
				}
				else if (this.scannedSN.length() > 0) { 
					/* Only sequence number was scanned */
					if(this.getTopLevelPNTextField().getText().length() > 0) {
						/* already scanned PN and scanned SN */
						this.controller.rtvSubAssemblyPartsOffline(null, this.getTopLevelPNTextField().getText(), this.scannedSN);								
					}
					else {
						/* Just set the part number */
						this.getTopLevelSNTextField().setText(this.scannedSN);
					}
				}
				
				//Check if auto-print is checked for retrieved subassembly, in that case print label ~06A
				partsToScann = this.countPartsToInstall();
				if(this.controller.isOffline() && (partsToScann == 0 && !this.controller.isModifyParts()) &&
				  (!mctlTextField.getText().equals("") && !topLevelPNTextField.getText().equals("") && !topLevelSNTextField.getText().equals("")))
				{
					printButton.setEnabled(true);
					if(this.autoPrintCheckBox.isSelected())
					{
						this.controller.print(mctlTextField.getText().toString(), topLevelPNTextField.getText().toString());
					}				
				}				 
			}
			else {
				/* There is currently one Subassembly present */
				if(this.scannedMctl.length() > 0) {
					throw new Exception("Subassembly data is already retrieved."); //$NON-NLS-1$
				}
				else
				/************* Start   ~04A ***********************/
					/* Call verfypnplus to retrieve the Sub assembly if...*/
					/* Allow for just a part number (BC indicator = P), or just a serial number (BC indicator = SN,     */
					/* or a COO (BC indicator = 4L) to be entered and decoded for commodity part data collection.       */
					if ((this.scannedPN.length() > 0 || this.scannedSN.length() > 0 || this.scannedCO.length() > 0)  // ~04C 
						&& (!this.controller.isSubComplete() || partsToRework > 0)) /* Add condition to allow barcode collection only when Sub is not complete ~07C*/
						                                                            /* Also allow barcode collection when there is rework pending ~09C*/
                    { 
    					/* Find a valid Part and check serial */
    					/* Also check left parts to scan */
    					this.emptyPartIndex = -1;
    					this.barcodeApplied = -1;
    					int dataSize = commoditiesModel.getData().size();
    					String tempPN = null;
    					String tempSN = null;    					
    					String tempCO = null;                                   //~04A
    					String tempCSNI = null;                                 //~03A
    					String tempMLRI = null;                                 //~04A

    					
    					/* Determine if there is a Index different than -1 saved on fieldActiveRow. In such case use that to
    					 * update the row with the info received for the barcode   ~05A
    					 */
    					if (this.fieldActiveRow != -1 && this.scannedPN.length() == 0)
    					{
    						tempPN = this.commoditiesModel.getValueAt(this.fieldActiveRow, Columns.INPN.getIndex()).toString();
    						tempSN = this.commoditiesModel.getValueAt(this.fieldActiveRow, Columns.INSQ.getIndex()).toString();
    						tempCO = this.commoditiesModel.getValueAt(this.fieldActiveRow, Columns.COOC.getIndex()).toString();      						
    						tempCSNI = this.commoditiesModel.getValueAt(this.fieldActiveRow, Columns.CSNI.getIndex()).toString();  
    					    tempMLRI = this.commoditiesModel.getValueAt(this.fieldActiveRow, Columns.MLRI.getIndex()).toString();  
    					    
    						if ((this.commoditiesModel.getValueAt(this.fieldActiveRow, Columns.IDSP.getIndex()).toString().equals("A") &&
        							(tempCSNI.equals("1") || tempMLRI.equals("1"))) ||
        							(this.commoditiesModel.getValueAt(this.fieldActiveRow, Columns.IDSP.getIndex()).toString().equals("I") &&
                							(!this.scannedCO.equals(""))))
    						{
    							// Process the barcode received as input
    							processBarcodeScanned(this.fieldActiveRow,tempPN,null,tempSN,tempCO,tempCSNI,tempMLRI,dataSize,false);
        						/* Check if Row already has PN, SN an COO combination collected according to CSNI and MLRI */    						
        						verifyPartCollected(this.fieldActiveRow,tempPN,tempSN,tempCO,tempCSNI,tempMLRI);    					    													    							
    						}
    						else
    						{
    	    					throw new Exception("Error: Barcode not supported at this time for the part."); //$NON-NLS-1$   							
    						}
    					}
    					/* End fieldActiveRow != -1 ~05A*/
    					else
    					{
        					for(int rowIndex = 0; rowIndex < dataSize; rowIndex++)
        					{
        						tempPN = this.commoditiesModel.getValueAt(rowIndex, Columns.INPN.getIndex()).toString();
        						tempSN = this.commoditiesModel.getValueAt(rowIndex, Columns.INSQ.getIndex()).toString();
        						tempCO = this.commoditiesModel.getValueAt(rowIndex, Columns.COOC.getIndex()).toString();    //~04A   						
        						tempCSNI = this.commoditiesModel.getValueAt(rowIndex, Columns.CSNI.getIndex()).toString();  //~03A
        					    tempMLRI = this.commoditiesModel.getValueAt(rowIndex, Columns.MLRI.getIndex()).toString();  //~04A
        						
        						if ((this.commoditiesModel.getValueAt(rowIndex, Columns.IDSP.getIndex()).toString().equals("A") &&
        							(tempCSNI.equals("1") || tempMLRI.equals("1")))||
        							(this.commoditiesModel.getValueAt(rowIndex, Columns.IDSP.getIndex()).toString().equals("I") &&
                							(!this.scannedCO.equals(""))))
    	        						    
        						{
        							// Process the barcode received as input
        							processBarcodeScanned(rowIndex,tempPN,null,tempSN,tempCO,tempCSNI,tempMLRI,dataSize,false);	
        						}
        						/* Check if Row already has PN, SN an COO combination collected according to CSNI and MLRI */    						
        						verifyPartCollected(rowIndex,tempPN,tempSN,tempCO,tempCSNI,tempMLRI);    					    						
        					}
        					/* If barcodeApplied = -1, it means that PN was not found and we need to check */
        					/* on substitute parts list. If a match is found, then install it.             */
        					if (this.barcodeApplied == -1)
        					{
        						for(int rowIndex = 0; rowIndex < dataSize; rowIndex++)
            					{  				
        							ArrayList<String> pnList = new ArrayList<String>();
        							
            						tempPN = this.commoditiesModel.getValueAt(rowIndex, Columns.INPN.getIndex()).toString();
            						tempSN = this.commoditiesModel.getValueAt(rowIndex, Columns.INSQ.getIndex()).toString();
            						tempCO = this.commoditiesModel.getValueAt(rowIndex, Columns.COOC.getIndex()).toString();    //~04A   						
            						tempCSNI = this.commoditiesModel.getValueAt(rowIndex, Columns.CSNI.getIndex()).toString();  //~03A
            						tempMLRI = this.commoditiesModel.getValueAt(rowIndex, Columns.MLRI.getIndex()).toString();  //~04A
            						   
            						if (this.commoditiesModel.getValueAt(rowIndex, Columns.IDSP.getIndex()).toString().equals("A") &&
                							(tempCSNI.equals("1") || tempMLRI.equals("1")))
            						{
            							// Look for a part match on Substitute Hashtable
            							if(commoditiesSubstitutes.containsKey(tempPN) && this.barcodeApplied == -1)
            							{
            								pnList = commoditiesSubstitutes.get(tempPN);
            								
            								for (int i = 0; i < pnList.size() && this.barcodeApplied == -1; i++)
            								{
            	    							// Process the barcode received as input
            	    							processBarcodeScanned(rowIndex,pnList.get(i).toString(),tempPN,tempSN,tempCO,tempCSNI,tempMLRI,dataSize,true);	        									
            								}
            							}	
            						}
            						/* Check if Row already has PN, SN an COO combination collected according to CSNI and MLRI */    						
            						verifyPartCollected(rowIndex,tempPN,tempSN,tempCO,tempCSNI,tempMLRI);    					    						        						    					    						
            					}/************* End     ~04A ***********************/
        					}

    					}
    					if(this.emptyPartIndex == -1 && this.barcodeApplied == -1)    // Add barcodeApplied condition ~04C 
    					{
    						throw new Exception("Error: The PN was NOT found in the Sub PN or Substitutes list."); //$NON-NLS-1$  ~04C
    					}
    					if (this.emptyPartIndex != -1)
    					{
    						installPart(this.emptyPartIndex);        					
    					} 					
					}/* If the sub is complete, send an appropriate message ~07A*/
					else if (this.controller.isSubComplete() && this.controller.getIdssVsap().equals("VSAP")) // Add VSAP condition ~09C
					{
						throw new Exception("Subassembly is already complete and changes are not allowed"); //$NON-NLS-1$
					}
					else if (this.controller.isSubComplete() && !this.controller.getIdssVsap().equals("VSAP")) // Add condition for non-Vendor subs ~09A
					{
						throw new Exception("Only work units created through the Vendor SubAssembly Part function with IDSS=VSAP can be updated"); //$NON-NLS-1$
					}				
					else 
					{
						throw new Exception("Error: PN/SN/COO barcode is needed."); //$NON-NLS-1$	  ~04C				
					}				
			}
		}
		catch(Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, e.getMessage(), null);
		}
		finally
		{
			addMyListeners();
		}
	}
	
	private void updateSubassemblyPartData(int rowIndex, String sn) {
		Object[] row = new Object[Columns.values().length];         //Remove -1 from length  ~03C
		
		row[Columns.INPN.getIndex()] = this.commoditiesModel.getValueAt(rowIndex, Columns.INPN.getIndex()).toString();
		row[Columns.INSQ.getIndex()] = sn.toUpperCase();
		row[Columns.CDES.getIndex()] = this.commoditiesModel.getValueAt(rowIndex, Columns.CDES.getIndex()).toString();
		row[Columns.IDSP.getIndex()] = "I"; //$NON-NLS-1$
		row[Columns.CRCT.getIndex()] = this.commoditiesModel.getValueAt(rowIndex, Columns.CRCT.getIndex()).toString();  //~03A
		row[Columns.CSNI.getIndex()] = this.commoditiesModel.getValueAt(rowIndex, Columns.CSNI.getIndex()).toString();  //~03A
		row[Columns.MLRI.getIndex()] = this.commoditiesModel.getValueAt(rowIndex, Columns.MLRI.getIndex()).toString();  //~04A
		row[Columns.PNRI.getIndex()] = this.commoditiesModel.getValueAt(rowIndex, Columns.PNRI.getIndex()).toString();  //~05A		
		row[Columns.COOC.getIndex()] = this.commoditiesModel.getValueAt(rowIndex, Columns.COOC.getIndex()).toString();  //~04A
		/*
		 * Clear STAT field when it has 'R'. 'R' indicates that the part was set to rework ~09A
		 */
		if (this.commoditiesModel.getValueAt(rowIndex, Columns.STAT.getIndex()).toString().equals("R"))
		{
			row[Columns.STAT.getIndex()] = " "; //$NON-NLS-1$	
		}
		else
		{
			row[Columns.STAT.getIndex()] = this.commoditiesModel.getValueAt(rowIndex, Columns.STAT.getIndex()).toString();
		}
		
		//Put the entire row to repaint all cells
		this.commoditiesModel.setRow(row, rowIndex);
	}
	
	// ~04A
	/**
	 * Updates the information displayed in the dialog
	 * @param rowIndex the row position to update
	 * @param pn the new part number to write
	 * @param sn the new serial number to write 
	 * @param coo the new country of origin to write
	 * @param option which fields to update according the barcode specified
	 */	
	private void updateSubassemblyPartialPartData(int rowIndex, String pn, String sn, String coo, int option) {
		Object[] row = new Object[Columns.values().length];  
		
		// P Barcode
		if(option == 1)
		{
			row[Columns.INPN.getIndex()] = pn.toUpperCase();		
			row[Columns.INSQ.getIndex()] = this.commoditiesModel.getValueAt(rowIndex, Columns.INSQ.getIndex()).toString();
			row[Columns.COOC.getIndex()] = this.commoditiesModel.getValueAt(rowIndex, Columns.COOC.getIndex()).toString();
		} // SN Barcode
		else if (option == 2)
		{
			row[Columns.INSQ.getIndex()] = sn.toUpperCase();
			row[Columns.INPN.getIndex()] = this.commoditiesModel.getValueAt(rowIndex, Columns.INPN.getIndex()).toString();
			row[Columns.COOC.getIndex()] = this.commoditiesModel.getValueAt(rowIndex, Columns.COOC.getIndex()).toString();
		} // 4L Barcode
		else if (option == 3)
		{
			row[Columns.COOC.getIndex()] = coo.toUpperCase();
			row[Columns.INPN.getIndex()] = this.commoditiesModel.getValueAt(rowIndex, Columns.INPN.getIndex()).toString();
			row[Columns.INSQ.getIndex()] = this.commoditiesModel.getValueAt(rowIndex, Columns.INSQ.getIndex()).toString();
		} // 11S Barcode
		else if (option == 4)
		{
			row[Columns.INPN.getIndex()] = pn.toUpperCase();
			row[Columns.INSQ.getIndex()] = sn.toUpperCase();
			row[Columns.COOC.getIndex()] = this.commoditiesModel.getValueAt(rowIndex, Columns.COOC.getIndex()).toString();			
		}
		row[Columns.CDES.getIndex()] = this.commoditiesModel.getValueAt(rowIndex, Columns.CDES.getIndex()).toString();
		row[Columns.IDSP.getIndex()] = this.commoditiesModel.getValueAt(rowIndex, Columns.IDSP.getIndex()).toString();
		row[Columns.CRCT.getIndex()] = this.commoditiesModel.getValueAt(rowIndex, Columns.CRCT.getIndex()).toString(); 
	    row[Columns.CSNI.getIndex()] = this.commoditiesModel.getValueAt(rowIndex, Columns.CSNI.getIndex()).toString();
	    row[Columns.MLRI.getIndex()] = this.commoditiesModel.getValueAt(rowIndex, Columns.MLRI.getIndex()).toString();
		row[Columns.PNRI.getIndex()] = this.commoditiesModel.getValueAt(rowIndex, Columns.PNRI.getIndex()).toString(); 
		row[Columns.STAT.getIndex()] = this.commoditiesModel.getValueAt(rowIndex, Columns.STAT.getIndex()).toString(); // ~09A
		
		//Put the entire row to repaint all cells			
		this.commoditiesModel.setRow(row, rowIndex);
		//Highlight the row
		this.commoditiesTable.changeSelection(rowIndex, Columns.INPN.getIndex(), false, false);
	}	
	
	//~09A
	/**
	 * Clear the collected data for the row selected on the subassembly panel
	 * @param rowIndex the row position to update
	 * @param pn the new part number to write
	 * @param sn the new serial number to write 
	 * @param coo the new country of origin to write
	 * @param option which fields to update according the barcode specified
	 */	
	private void clearSubassemblyPartData(int rowIndex) {
		Object[] row = new Object[Columns.values().length];  
		
		row[Columns.INPN.getIndex()] = this.commoditiesModel.getValueAt(rowIndex, Columns.INPN.getIndex()).toString();
		row[Columns.INSQ.getIndex()] = "";                                                                              //~10C
		row[Columns.CDES.getIndex()] = this.commoditiesModel.getValueAt(rowIndex, Columns.CDES.getIndex()).toString();
		row[Columns.IDSP.getIndex()] = "A"; //$NON-NLS-1$
		row[Columns.CRCT.getIndex()] = this.commoditiesModel.getValueAt(rowIndex, Columns.CRCT.getIndex()).toString(); 
		row[Columns.CSNI.getIndex()] = this.commoditiesModel.getValueAt(rowIndex, Columns.CSNI.getIndex()).toString(); 
		row[Columns.MLRI.getIndex()] = this.commoditiesModel.getValueAt(rowIndex, Columns.MLRI.getIndex()).toString(); 
		row[Columns.PNRI.getIndex()] = this.commoditiesModel.getValueAt(rowIndex, Columns.PNRI.getIndex()).toString(); 		
		row[Columns.COOC.getIndex()] = "";	                                                                            //~10C
		row[Columns.STAT.getIndex()] = "R";
		//Put the entire row to repaint all cells
		this.commoditiesModel.setRow(row, rowIndex);

		//Highlight the row
		this.commoditiesTable.changeSelection(rowIndex, Columns.INPN.getIndex(), false, false);
	}
	
	/**
	 * Updates the information displayed in the components.
	 * @param message instructions message.
	 * @param topLevelPart the top level part information.
	 * @param subParts the sub parts information.
	 */
	public void setSubAssemblyData(String mctl, String inpn, String insq, String coo, List<MFSPart> subParts, MFSSubstitute substitutes, MFSCoo coos)    // Add substitutes ~04C. Add coos ~05C
	{	
		if(this.controller.isModifyParts()) {
			this.tipsLabel.setText(TIPS_COMMODITIES);
		}
		else {
			this.tipsLabel.setText(TIPS_TOPLEVEL);
		}
		this.mctlTextField.setText(mctl);
		this.topLevelPNTextField.setText(inpn);
		this.topLevelSNTextField.setText(insq);
		this.cooTextField.setText(coo);
		
		if(null != subParts)
		{					
			this.commoditiesModel.clearData();
			
		    /*Determine the order of the commodities on the table 
		     * 1 Red parts
		     * 2 Parts auto installed
		     * 3 MLRI not blank and PNRI and CSNI blank
		     * 4 PNRI not blank and CSNI blank
		     * 5 CSNI not blank
		     */
			List <MFSPart> parts1 = new Vector<MFSPart>();
			List <MFSPart> parts2 = new Vector<MFSPart>();
			List <MFSPart> parts3 = new Vector<MFSPart>();
			List <MFSPart> parts4 = new Vector<MFSPart>();
			List <MFSPart> parts5 = new Vector<MFSPart>();		
			
			/*Save the parts that meets the condition on a separate list so later we can add it 
			 *to the table in the correct order. 
			 */
			for(MFSPart part : subParts)
			{
				if (!part.getCsni().equals("1") && !part.getMlri().equals("1"))
				{
					parts1.add(part);
				}
				else if (part.getMlri().equals("1") && (!part.getCsni().equals("1") && !part.getPnri().equals("1")))
				{
					parts2.add(part);
				}
				else if (part.getMlri().equals("1") && (!part.getCsni().equals("1") && part.getPnri().equals("1")))
				{
					parts3.add(part);
				}				
				else if (part.getPnri().equals("1") && !part.getCsni().equals("1"))
				{
					parts4.add(part);
				}
				else if (part.getCsni().equals("1"))
				{
					parts5.add(part);
				}
			}
			
			/*Add the parts to the table. They are already ordered ~05A
			 * 
			 */
			for(MFSPart part : parts1)
			{
				this.commoditiesModel.addRow(
						new Object[] {part.getInpn(), part.getInsq(), part.getDesc(),
											part.getIdsp(), part.getCooc(), part.getCsni(),
											part.getMlri(), part.getPnri(), part.getCrct(),part.getStat()
					}); // Add Stat value ~09A
			}
			
			for(MFSPart part : parts2)
			{
				this.commoditiesModel.addRow(
						new Object[] {part.getInpn(), part.getInsq(), part.getDesc(),
											part.getIdsp(), part.getCooc(), part.getCsni(),
											part.getMlri(), part.getPnri(), part.getCrct(),part.getStat()
					});// Add Stat value ~09A
			}
			
			for(MFSPart part : parts3)
			{
				this.commoditiesModel.addRow(
						new Object[] {part.getInpn(), part.getInsq(), part.getDesc(),
											part.getIdsp(), part.getCooc(), part.getCsni(),
											part.getMlri(), part.getPnri(), part.getCrct(),part.getStat() 
					});// Add Stat value ~09A
			}
			
			for(MFSPart part : parts4)
			{
				this.commoditiesModel.addRow(
						new Object[] {part.getInpn(), part.getInsq(), part.getDesc(),
											part.getIdsp(), part.getCooc(), part.getCsni(),
											part.getMlri(), part.getPnri(), part.getCrct(),part.getStat()
					});// Add Stat value ~09A
			}
			
			for(MFSPart part : parts5)
			{
				this.commoditiesModel.addRow(
						new Object[] {part.getInpn(), part.getInsq(), part.getDesc(),
											part.getIdsp(), part.getCooc(), part.getCsni(),
											part.getMlri(), part.getPnri(), part.getCrct(),part.getStat()
					});// Add Stat value ~09A
			}
			/* End add parts to the table  ~05A
			 * 
			 */
		}
		// Get Substitutes Part List   ~04A
		if(null != substitutes)
		{
			this.commoditiesSubstitutes = new Hashtable<String, ArrayList<String>>();
		    this.commoditiesSubstitutes.clear();
		    
		    this.commoditiesSubstitutes = substitutes.getCalledPn_PnList();
		} // End Get Substitutes Part List  ~04A
		
		// Get Coos Part List        ~05A
		if(null != coos)
		{
			this.commoditiesCoos = new Hashtable<String, ArrayList<String>>();
		    this.commoditiesCoos.clear();
		    
		    this.commoditiesCoos = coos.getPn_cooList();
		} // End Get Coos Part List  ~05A				
		
		// Verify if there are parts that can be auto-installed  ~05A
		String tempPN = null;
		String tempCSNI = null;                           
		String tempMLRI = null;                          
		String tempPNRI = null; 
		String tempIDSP = null;
		int dataSize = this.commoditiesModel.getData().size();
		boolean partAutoInstallNotViable = false;
		
		for(int rowIndex = 0; rowIndex < dataSize; rowIndex++)
		{
			tempPN = this.commoditiesModel.getValueAt(rowIndex, Columns.INPN.getIndex()).toString();
			tempCSNI = this.commoditiesModel.getValueAt(rowIndex, Columns.CSNI.getIndex()).toString(); 
		    tempMLRI = this.commoditiesModel.getValueAt(rowIndex, Columns.MLRI.getIndex()).toString(); 
		    tempPNRI = this.commoditiesModel.getValueAt(rowIndex, Columns.PNRI.getIndex()).toString();
		    tempIDSP = this.commoditiesModel.getValueAt(rowIndex, Columns.IDSP.getIndex()).toString();
		    
		    if (!tempIDSP.equals("I") && (tempMLRI.equals("1") && (!tempCSNI.equals("1") && !tempPNRI.equals("1"))))
		    {
				// Check if COO is saved on the cache hash COO table
				if (!calculateCoo(rowIndex))
				{
					// Look for COO on the COOS XML returned on VRFYPNPLUS
					// Look for a part match on Substitute Hashtable
					if(commoditiesCoos.containsKey(tempPN))
					{
						ArrayList<String> cooList = new ArrayList<String>();
						cooList.clear();
						cooList = commoditiesCoos.get(tempPN);
						
						if (cooList.size() == 1)
						{
							this.updateSubassemblyPartialPartData(rowIndex, null, null, cooList.get(0).toString() ,3);
							partAutoInstallNotViable = false;
						}
						else
						{//Can't auto-determine COO
							partAutoInstallNotViable = true;
						}
					}
				}
				
				if (!partAutoInstallNotViable)
				{
					installPart(rowIndex);
				}					
		    }
		}
							
		if(this.controller.isOffline() && insq.length() == 0) {
			this.serializeButton.setEnabled(true);
		}
		else {
			this.serializeButton.setEnabled(false);
		}
		
		/* Verify if the subassembly retrieved is a Vendor one or not. Enable or disable
		 * rebuild/rework buttons if it is not.                                     ~09A  
		 */		
		if(this.controller.getIdssVsap().equals("VSAP") && this.controller.getSubPwun().equals("        ")) {
			this.rebuildButton.setEnabled(true);
			this.reworkButton.setEnabled(true);
		}
		else {
			this.rebuildButton.setEnabled(false);
			this.reworkButton.setEnabled(false);
		}
	}
	
	// ~05A
	/**
	 * Process the barcode specified on the input text
	 * @param index the current index (row position on the table)
	 * @param pn the part number pointed by index or the substitute
	 * @param pn_substituted the part number to be substituted and pointed by index on the table
	 * @param sn the serial number pointed by index
	 * @param co the country of origin pointed by index
	 * @param csni the csni attribute for part number
	 * @param mlri the mlri attribute for part number
	 * @param substitute the indicator to know if method was called from substitute logic
	 * 
	 */
	public void processBarcodeScanned(int index, String pn, String pn_substituted, String sn, String co, String csni,  
			                          String mlri,int dataSize, boolean substitute)
	{	
		String messageLabel = "";
		this.tipsLabel.setText("");
		
		try
		{
			if(this.scannedPN.length() > 0) 
			{
				if (pn.equalsIgnoreCase(this.scannedPN))
				{
					//Highlight the row
					this.commoditiesTable.changeSelection(index, Columns.INPN.getIndex(), false, false);
					/* When method is called from search pn instead of mouse click, then we need to save
					 * index on fieldActiveRow for later reference and apply other changes over that record.
					 */
					this.fieldActiveRow = index;					
					
					/* found a matching part */	
					if(this.scannedSN.length() == 0) 
					{
						if (!csni.equals("1"))
						{
							messageLabel = messageLabel + TIPS_SNNOTNEEDED;
						}
						else
						{
							messageLabel = messageLabel + "Enter Serial Number for Commodity Part";	
						}
					}
					else 
					{
						/* Check if the serial number has the correct length and other validations ~07A*/
						if (validateSerialNumber(pn,this.scannedSN))
						{
							String tempSN = null;
							String tempPN = null;                                                                           //~06A
	    					for(int tmpRowIndex = 0; tmpRowIndex < dataSize; tmpRowIndex++)
	    					{
	    						tempPN = this.commoditiesModel.getValueAt(tmpRowIndex, Columns.INPN.getIndex()).toString(); //~06A
	    						tempSN = this.commoditiesModel.getValueAt(tmpRowIndex, Columns.INSQ.getIndex()).toString();
	    						
	    						if ((tempSN.equalsIgnoreCase(this.scannedSN) && csni.equals("1")) && tempPN.equals(this.scannedPN)) //~06C
	    						{
	    							this.barcodeApplied = index;
	    							throw new Exception("Error: Duplicated SN, scan a different SN for the given PN."); //$NON-NLS-1$
	    						}
	    					}							
						}
						else
						{
							this.barcodeApplied = index;
							throw new Exception("Error: Serial number specified is not valid"); //$NON-NLS-1$
						}
					}									                            					                    					    
					if(co.length() == 0)
					{
						if (!mlri.equals("1"))
						{	
							messageLabel = messageLabel + TIPS_COONOTNEEDED;							    										
						}
						else
						{
							determineCooAutomatically(index,pn);
							//Get the COO in the table again to check if it was updated previously
							co = this.commoditiesModel.getValueAt(index, Columns.COOC.getIndex()).toString(); 
							if(co.length() == 0)
							{
								messageLabel = messageLabel + "Enter COO for Commodity Part";							
							}
						}	
					}
					//Put the entire row to repaint all cells
					if ((this.scannedPN.length() > 0 && this.scannedSN.length() > 0) && csni.equals("1"))
					{
						this.updateSubassemblyPartialPartData(index, this.scannedPN, this.scannedSN, this.scannedCO,4);
						// 11S Case. It does not include COO
						if (mlri.equals("1") && co.length() == 0)
						{
							determineCooAutomatically(index,this.scannedPN.toString());
							//Get the COO in the table again to check if it was updated previously
							co = this.commoditiesModel.getValueAt(index, Columns.COOC.getIndex()).toString(); 
							if(co.length() == 0)
							{
								messageLabel = messageLabel + ". Enter COO for Commodity Part";					
							}
						}                					    	
					}
					else
					{
						this.updateSubassemblyPartialPartData(index, this.scannedPN, this.scannedSN, this.scannedCO,1);
					}
					// Set message when a substitute is being used 
					if(substitute)
					{
						messageLabel = messageLabel + ". Part " + pn_substituted + " substituted for Part " + this.scannedPN;
					}
					
					this.scannedPN = "";
					this.scannedSN = "";                					    
					this.barcodeApplied = index;
				}
			}
			else if (this.scannedSN.length() > 0)
			{
				/* Check if the serial number has the correct length and other validations ~07A*/
				if (validateSerialNumber(pn,this.scannedSN))
				{
					if (!csni.equals("1"))                                                   //$NON-NLS-1$
					{   
						this.barcodeApplied = index;
						this.tipsLabel.setText("Enter only COO for Commodity Part");
						throw new Exception("No need to collect serial for this part. Scan a different PN."); //$NON-NLS-1$
					} 
					
					String tempSN = null;
					String tempPN = null;                                                                           //~06A
					for(int tmpRowIndex = 0; tmpRowIndex < dataSize; tmpRowIndex++)
					{
						tempPN = this.commoditiesModel.getValueAt(tmpRowIndex, Columns.INPN.getIndex()).toString(); //~06A
						tempSN = this.commoditiesModel.getValueAt(tmpRowIndex, Columns.INSQ.getIndex()).toString();
						
						if ((tempSN.equalsIgnoreCase(this.scannedSN) && csni.equals("1")) && tempPN.equals(pn))     //~06C
						{
							this.barcodeApplied = index;
							throw new Exception("Error: Duplicated SN, scan a different SN for the given PN."); //$NON-NLS-1$
						}
					}
					
					if(co.length() == 0)
					{
						if (!mlri.equals("1"))
						{		
							messageLabel = messageLabel + TIPS_COONOTNEEDED;            										
						}
						else
						{
							determineCooAutomatically(index,pn);
							//Get the COO in the table again to check if it was updated previously
							co = this.commoditiesModel.getValueAt(index, Columns.COOC.getIndex()).toString(); 
							if(co.length() == 0)
							{
								messageLabel = messageLabel + "Enter COO for Commodity Part";							
							}							
						}	
					}
					this.updateSubassemblyPartialPartData(index, this.scannedPN, this.scannedSN, this.scannedCO,2);
					this.scannedSN = "";
					this.barcodeApplied = index;        													
				}
				else
				{
					this.barcodeApplied = index;
					throw new Exception("Error: Serial number specified is not valid"); //$NON-NLS-1$
				}
			}
			else if(this.scannedCO.length() > 0)
			{
				if (!mlri.equals("1"))                                                   //$NON-NLS-1$ 
				{    
					this.barcodeApplied = index;
					this.tipsLabel.setText("Enter only Serial Number for Commodity Part");
					throw new Exception("No need to collect COO for this part. Scan a different PN."); //$NON-NLS-1$ 
				}                                                                                             									        							
				else if(sn.length() == 0) 
				{
					if (!csni.equals("1"))
					{
						messageLabel = messageLabel + TIPS_SNNOTNEEDED;
					}
					else
					{
						messageLabel = messageLabel + "Enter Serial Number for Commodity Part";
					}
				}
				this.updateSubassemblyPartialPartData(index, this.scannedPN, this.scannedSN, this.scannedCO,3);
				this.scannedCO = "";
				this.barcodeApplied = index;
			}
			this.tipsLabel.setText(messageLabel);
		}
		catch(Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, e.getMessage(), null);
		}
		finally
		{
			// No action
		}
	}
	
	// ~05A
	/**
	 * Verify if the part already has all the data collected (SN,COO)
	 * @param index the current index (row position on the table)
	 * @param pn the part number pointed by index
	 * @param sn the serial number pointed by index
	 * @param co the country of origin pointed by index
	 * @param csni the csni attribute for part number
	 * @param mlri the mlri attribute for part number
	 * 
	 */
	public void verifyPartCollected(int index, String pn, String sn, String co, String csni, String mlri)
	{	
		try
		{
			/* Check if Row already has PN, SN an COO combination collected according to CSNI and MLRI */
			if(this.barcodeApplied == index) 
			{
				pn = this.commoditiesModel.getValueAt(index, Columns.INPN.getIndex()).toString();
				sn = this.commoditiesModel.getValueAt(index, Columns.INSQ.getIndex()).toString();
				co = this.commoditiesModel.getValueAt(index, Columns.COOC.getIndex()).toString();
				csni = this.commoditiesModel.getValueAt(index, Columns.CSNI.getIndex()).toString();
				mlri = this.commoditiesModel.getValueAt(index, Columns.MLRI.getIndex()).toString();
			
				if (csni.equals("1") &&                                             
						mlri.equals("1"))
				{
					if (!pn.trim().equals("") && !sn.trim().equals("") && !co.trim().equals("") )
					{
						/* If not already found, save the index,
						 * otherwise just skip it */
						this.emptyPartIndex = index;
					}
				}
				else if (csni.equals("1") &&                                             
						!mlri.equals("1"))
				{
					if (!pn.trim().equals("") && !sn.trim().equals(""))
					{
						/* If not already found, save the index,
						 * otherwise just skip it */
						this.emptyPartIndex = index;
					}	    							
				}
				else if (!csni.equals("1") &&                                             
						mlri.equals("1"))
				{
					if (!pn.trim().equals("") && !co.trim().equals("") )
					{
						/* If not already found, save the index,
						 * otherwise just skip it */
						this.emptyPartIndex = index;
					}
				}
			}
		}
		finally
		{
			//No action
		}
	}
	
	//~05A
	/** We need to save Part and Coo
	 *  So when the Dialog is initiate we can default by the last coo used by the part.
	 */
	protected void saveCooInHash(String part, String coo) { 
		String cooValue = null;
		/* Save the last Coo used by the part (Coo by Part)
		 * This will work to pre populate the CO when the Vendor dialog is initiated.
		 */
		cooValue = this.getCooHash().get(part);
		if (cooValue == null || !cooValue.equals(coo))
		{
			this.getCooHash().put(part, coo);
		}
	}
	
	//~05A 
	/** get the Coo hashTable from the {MFSConfig} singleTon reference (Encapsulation fix) 
	 */
	protected Hashtable<String, String> getCooHash() {
		if(this.cooHash == null)
		{
			this.cooHash = MFSConfig.getInstance().getCooHash();
		}
		return this.cooHash;
	}
	
	// ~05A
	/**  
	 *  @return if found true, otherwise false
	 */
	protected boolean calculateCoo(int rowIndex)
	{	
		boolean found = false;

     	/* Get the Coo from the saved values */
		String savedCoo = getCooFromHash(rowIndex);
		if (null != savedCoo)
		{
			this.updateSubassemblyPartialPartData(rowIndex, this.scannedPN, this.scannedSN, savedCoo,3);
			found = true;
		}
		return found;		
	}

	// ~05A
	/** Gets the saved Coo value from the cooHash */ 
	private String getCooFromHash(int rowIndex)
	{
		String partColumn = this.commoditiesModel.getValueAt(rowIndex, Columns.INPN.getIndex()).toString();
		if(partColumn != null && !partColumn.equals(""))
		{
			String cooKey = partColumn;
			
			return this.getCooHash().get(cooKey);
		}
		return null;
	}
	
	// ~05A
	/**
	 * Verify if the part COO can be determined using cached values on coo hash
	 * and if not there, then look at the values returned by VRFYPNPLUS. If value
	 * returned is 1, then assign automatically to the part, if not, display a dialog
	 * so the user can choose from the list.                                   
	 * @param index the current index (row position on the table)
	 * @param pn the part number pointed by index
	 * 
	 */	
	public void determineCooAutomatically(int index, String pn)
	{	
		try
		{
			// Check if COO is saved on the cache hash COO table
			if (!calculateCoo(index))
			{
				// Look for COO on the COOS XML returned on VRFYPNPLUS
				// Look for a part match on Substitute Hashtable
				if(commoditiesCoos.containsKey(pn))
				{
					ArrayList<String> cooList = new ArrayList<String>();
					cooList.clear();
					cooList = commoditiesCoos.get(pn);
					
					if (cooList.size() == 1)
					{
						this.updateSubassemblyPartialPartData(index, this.scannedPN, this.scannedSN, cooList.get(0).toString() ,3);
					}
					else if (cooList.size() > 1)
					{
						String cooListInput = null;
						for (int i = 0; i < cooList.size(); i++)
						{
							if (cooListInput == null)
							{
								cooListInput = cooList.get(i).trim().toString() + 
								               "                                             ";
							}
							else
							{
								cooListInput = cooListInput +
								               cooList.get(i).trim().toString() +
								               "                                             ";	
							}
						} 
						
						String cooselection = "";

						MFSCooDialog cooDialog = new MFSCooDialog(getParentFrame(),	true);
						cooDialog.loadCooListModel(cooListInput);
						cooDialog.setLocationRelativeTo(this);
						cooDialog.setVisible(true);
						if (cooDialog.getProceedSelected())
						{
							cooselection = cooDialog.getSelectedListOption().substring(0, 2);
						}
						if (!cooselection.equals(""))
						{
							if (cooselection.equals("  ")) //$NON-NLS-1$
							{
								cooselection = "??"; //$NON-NLS-1$
							}
							else
							{
								this.updateSubassemblyPartialPartData(index, this.scannedPN, this.scannedSN, cooselection ,3);
							}
						}/* cooJD selected */        								
					}
				}									
			}			
		}
		finally
		{
			// Do nothing
		}
	}
	
	// ~05A
	/**
	 * Install the part and call VRFYPNPLUS to update TL10, WU10 and CR10 records
	 * upon conditions if the SUB is complete or not.
	 * @param index the current index (row position on the table)
	 * 
	 */		
	public void installPart(int index)
	{	
		// Check for EFFICIENCYON*LOGVENDORPARTS config entry    ~05A
		boolean efficiencyClientLog= false;
		if(MFSConfig.getInstance().containsConfigEntry("EFFICIENCYON*LOGVENDORPARTS"))//$NON-NLS-1$
		{
			efficiencyClientLog = true;
		}
		
		try
		{
	
			int partsToScann = this.countPartsToInstall();
			MFSPart partInfo = getSubPartFromTable(index);
	
			/*  - Verify if it is the last part to install and efficiencyClientLog config entry does not exist,
			 *    then only save the index of last part for later use on End button.                       ~05A
			 *  - Save index of last part also when there is a rework action pending over the part and
			 *    partsToScan <= 1.                                                                        ~09C
			 */
			if ((partsToScann <= 1 && !efficiencyClientLog) || (partsToScann <= 1 && partInfo.getStat().toString().equals("R")))
			{
				this.partCompletedIndex = index;
						
				/* For direct work, End button should be enabled when sub is complete ~07A*/
				if(!this.controller.isOffline())
				{
					this.clearButton.setEnabled(true);	
				} //~07A Enable print button only for Offline
				else
				{
					printButton.setEnabled(true); // Enable it since sub is complete ~06A
				}
			}   
	
			if(this.controller.logSubAssemblyPart(partInfo.getMctl(), partInfo.getCrct(), 
					partInfo.getInpn(), partInfo.getInsq(), partInfo.getCooc(), partInfo.getStat(),
					(partsToScann <= 1 && efficiencyClientLog) && !partInfo.getStat().toString().equals("R")))   // ~04C Use data from partInfo instead of scanned
		                                                           // ~05C Add efficiencyClientLog to know if MCTL can be completed
				                                                   // ~09C If part Stat has an R value, it means there is a Rework pending and it should not be allowed
				                                                   //      to close the dialog (even if it is the last part and MCTL should be set to 'Complete'
			{
				// if the part is not in the hash, add the part/coo to the hash  ~05A
				if (!partInfo.getInpn().trim().equals("") && 
					!partInfo.getCooc().trim().equals("")) 
				{		
					saveCooInHash(partInfo.getInpn().trim(),partInfo.getCooc().trim());
				}
		
				this.updateSubassemblyPartData(index, partInfo.getInsq());  // Use INSQ from partInfo instead of scanned ~04C

				/* 
				 *  If part Stat has an R value, it means there is a Rework pending and it should not be allowed
				 *  to close the dialog (even if it is the last part and MCTL should be set to 'Complete'. ~09C
				 */
				if((partsToScann <= 1 && efficiencyClientLog)&& !partInfo.getStat().toString().equals("R"))                              // ~05C Add && efficiencyClientLog to know if MCTL can be completed
				{
					/*partsToScann equals to 0 or 1 and efficiencyClientLog equals true, 
					 * it means Sub is already completed so set subComplete attribute ~06A
					 */
					this.controller.setSubComplete(true);
					
					
					if(this.controller.isOffline()) 
					{
						resetDefaults(partInfo.getMctl());
					} 

					if(this.autoPrintCheckBox.isSelected())
					{
						this.controller.print(partInfo.getMctl(), partInfo.getInpn());        // Use INPN from partInfo instead of scanned ~05C
					}
					
					/* In Direct mode, we need to close dialog window  ~08A */
					if (!this.controller.isOffline())
					{
						this.dispose();
					}
				}
			}
		}
		finally
		{
			//Do nothing
		}
	}
	
	// ~07A
	/**
	 * Validates the scanned in serial number.
	 * @param pn Part Number for which logging of the serial number is performed
	 * @param sn Serial Number scanned to be entered for the part
	 * @return true if the serial number is valid
	 */
	protected boolean validateSerialNumber(String pn, String sn)
	{
		boolean valid = true;
		
		char[] SNArray = sn.toCharArray();
		String alphaNumeric = " ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"; //$NON-NLS-1$

		/* SN must be alphanumeric */
		for (int i = 0; i < SNArray.length; i++)
		{
			if (alphaNumeric.indexOf(SNArray[i]) == -1)
			{
				valid = false;
			}
		}
		//SN length must be between 7 and 12
		//SN can't start with "SN" or "11S"
		//SN can't equal PN or 'P' followed by PN
		if (valid)
		{
			if (sn.length() < 7 || sn.length() > 12
					|| sn.substring(0, 2).equals("SN") //$NON-NLS-1$
					|| sn.substring(0, 3).equals("11S") //$NON-NLS-1$
					|| sn.substring(0, 7).equals(pn)
					|| (sn.length() >= 8 && sn.substring(0, 8).equals("P" + pn))) //$NON-NLS-1$
			{
				valid = false;
			}
		}
		return valid;
	}	

	/* 
	 * Checks if auto-print is selected.
	 * @return true if selected.
	 */
	public boolean isAutoPrintSelected()
	{
		return autoPrintCheckBox.isSelected();
	}

	/**
	 * Sets the auto-print selection.
	 * @param selected true if selected.
	 */
	public void setAutoPrintSelected(boolean selected)
	{
		autoPrintCheckBox.setSelected(selected);
	}

	/** 
	 * Checks if auto-serialize is selected.
	 * @return true if selected.
	 */
	public boolean isAutoSerializeSelected()
	{
		return autoSerializeCheckBox.isSelected();
	}

	/**
	 * Sets the auto-serialize selection.
	 * @param selected true if selected.
	 */
	public void setAutoSerializeSelected(boolean selected)
	{
		autoSerializeCheckBox.setSelected(selected);
	}

	/** 
	 * Checks if the 'use vendor as built data' box is selected.
	 * @return true if selected.
	 */
	public boolean isUseVendorAsBuiltDataSelected()
	{
		return useVendorAsBuiltDataCheckBox.isSelected();
	}
	
	/**
	 * Sets the 'use vendor as built data' box selection.
	 * @param selected true if selected.
	 */
	public void setUseVendorAsBuiltDataSelected(boolean selected)
	{
		useVendorAsBuiltDataCheckBox.setSelected(selected);
	}

	/**
	 * Enables/disables the auto-print box.
	 * @param enabled true or false to disable.
	 */
	public void setAutoPrintEnabled(boolean enabledCheckBox, boolean enabledPrintButton)
	{
		autoPrintCheckBox.setEnabled(enabledCheckBox);
		/*Don't enable printButton by default (offline), instead enable it when sub
		 * retrieved and all data collected. ~06C
		 */
		printButton.setEnabled(enabledPrintButton);
	}
	
	/**
	 * Enables/disables the auto-serialize box.
	 * @param enabled true or false to disable.
	 */
	public void setAutoSerializeEnabled(boolean enabled)
	{
		autoSerializeCheckBox.setEnabled(enabled);
	}
	
	/**
	 * Enables/disables 'use vendor as built data' box
	 * @param enabled true or false to disable.
	 */
	public void setUseVendorAsBuiltDataEnabled(boolean enabled)
	{
		useVendorAsBuiltDataCheckBox.setEnabled(enabled);
	}
}  //  @jve:decl-index=0:visual-constraint="10,10"