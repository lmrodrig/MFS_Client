/* @ Copyright IBM Corporation 2010. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2010-09-06       48749JL Santiago SC      -Initial version, Java 5.0
 ******************************************************************************/

package com.ibm.rchland.mfgapps.mfsclient.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import com.ibm.rchland.mfgapps.client.utils.messagebox.IGSMessageBox;
import com.ibm.rchland.mfgapps.mfsclient.MFSActionableDialog;
import com.ibm.rchland.mfgapps.mfsclient.MFSFrame;
import com.ibm.rchland.mfgapps.mfsclient.barcode.MFSBCBarcodeDecoder;
import com.ibm.rchland.mfgapps.mfsclient.struct.MFSHazmatCntr;
import com.ibm.rchland.mfgapps.mfsclient.struct.MFSHazmatPn;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSConstants;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSHazmatLabeling;
import com.ibm.rchland.mfgapps.mfscommon.MFSConfig;

/**
 * The <code>MFSHazmatDialog</code> used to verify a <code>MFSHazmatCntr</code>
 */
public class MFSHazmatDialog extends MFSActionableDialog
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5318427466543940163L;
	
	/** The accept/complete button */
	private JButton pbAccept;	
	
	/** The enter button for label part number verification */
	private JButton pbEnter;

	/** The cancel button */
	private JButton pbCancel;
	
	/** The field used to decode barcodes */
	private JTextField tfBarcode;

	/** The hazmat label part number information */
	private Object[] hazmatCols = {"Hazmat Label PN", "Verification"};

	/** The panel holding the dialogs title */
	private JPanel headerPanel;
	
	/** The <code>MFSHazmatCntr</code> to be verified */
	private MFSHazmatCntr hazmatCntr;
	
	/** The table that displays the label part numbers */
	private JTable hazmatTable;
	
	/** The label part number completed status */
	private final String COMPLETED = "COMPLETED";
	
	/** The label part number incomplete status */
	private final String INCOMPLETE = "INCOMPLETE";	
	
	/** The label part number remove/overlay status */
	private final String REMOVE_REQUIRED= "REMOVE/OVERLAY";
	
	/** The label part number successfully removed status */
	private final String REMOVED = "SUCCESSFULLY REMOVED";
	
	/** True if accept/complete button is pressed */
	private boolean isCompleteSelected;
	
	/** If false, hides the verification and decoder field */
	private boolean displayOnly;

	/**
	 * Creates a new <code>MFSHazmatDialog<code> with a <code>MFSFrame</code> as its parent
	 * and the a <code>MFSHazmatCntr</code> to verify.
	 * @param parent the frame that owns this dialog.
	 * @param hazmatCntr the cntr to be verified.
	 */
	public MFSHazmatDialog(MFSFrame parent, MFSHazmatCntr hazmatCntr, boolean displayOnly)
	{
		super(parent, "");

		this.hazmatCntr = hazmatCntr;
		this.displayOnly = displayOnly;
		
		createLayout();
		
		fillHazmatTable();

		addMyListeners();
	}

	/**
	 * Invoked when an action occurs.
	 * @param ae the <code>ActionEvent</code>
	 */
	public void actionPerformed(ActionEvent ae)
	{
		final Object source = ae.getSource();
		
		if(source == pbAccept)
		{
			setCompleteSelected(true);
			this.dispose();
		}
		else if(source == pbEnter)
		{
			verifyPN(rcvBcInput());
		}
		else if(source == pbCancel)
		{
			setCompleteSelected(false);
			this.dispose();
		}
	}

	/**
	 * Add this dialog listeners
	 */
	private void addMyListeners()
	{
		tfBarcode.addKeyListener(this);
				
		pbEnter.addActionListener(this);
		pbEnter.addKeyListener(this);
		
		pbAccept.addActionListener(this);
		pbAccept.addKeyListener(this);
		
		pbCancel.addActionListener(this);
		pbCancel.addKeyListener(this);
	}

	/**
	 * Creates the layout for this dialog.
	 */
	protected void createLayout()
	{
		// header panel
		headerPanel = new JPanel(new BorderLayout());
		
		JLabel titleLabel = createLabel("Hazmat Label(s) Required");
		titleLabel.setFont(MFSConstants.LARGE_PLAIN_DIALOG_FONT);
		
		JPanel titlePanel = new JPanel(new FlowLayout());
		titlePanel.add(titleLabel);
		headerPanel.add(titlePanel, BorderLayout.NORTH);
		
		String cntrLbl = (hazmatCntr.isFFBM())? "MCTL: " : "Container: ";
		JLabel cntrLabel = createLabel(cntrLbl + hazmatCntr.getCntr());
		
		JPanel cntrPanel = new JPanel(new FlowLayout());
		cntrPanel.add(cntrLabel);
		headerPanel.add(cntrPanel, BorderLayout.CENTER);
		
		JPanel headerSouthPanel = new JPanel(new FlowLayout());
		headerSouthPanel.add(createLabel("Scan Hazmat Label P/N's:"));
		
		tfBarcode = createTextField(18, 0);
		headerSouthPanel.add(tfBarcode);
		
		pbEnter = createButton("Enter", 'e');
		headerSouthPanel.add(pbEnter);
		
		if(displayOnly)
		{
			headerPanel.add(headerSouthPanel, BorderLayout.SOUTH);
		}
		
		// Table
		hazmatTable = new JTable(new DefaultTableModel(hazmatCols, 0)){

			/**
			 * 
			 */
			private static final long serialVersionUID = 6465157888851617704L;

			@Override
        	public boolean isCellEditable(int rowIndex, int colIndex)
			{
        		return false;   //Disallow the editing of any cell
            }  
		};
		
		
		hazmatTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); 
		hazmatTable.setFont(MFSConstants.MEDIUM_MONOSPACED_FONT);
		hazmatTable.getTableHeader().setFont(MFSConstants.MEDIUM_DIALOG_FONT);
		
		TableColumnModel tcm = hazmatTable.getColumnModel();
		tcm.getColumn(tcm.getColumnIndex(hazmatCols[0])).setPreferredWidth(20);
		tcm.getColumn(tcm.getColumnIndex(hazmatCols[1])).setPreferredWidth(20);
		
		// Special renderer for Location and Status columns
		hazmatTable.getColumn(hazmatCols[1]).setCellRenderer(new DefaultTableCellRenderer(){

			/**
			 * 
			 */
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
		        
		        if(value.equals(COMPLETED) || value.equals(REMOVED))
		        {
		        	cell.setForeground(Color.GREEN);
		        }
		        else if(value.equals(INCOMPLETE))
		        {
		        	cell.setForeground(Color.ORANGE);
		        } 
		        else if(value.equals(REMOVE_REQUIRED))
		        {
		        	cell.setForeground(Color.RED);
		        }
		        
		        return cell;
		    }
			
		});
		
		// button panel
		JPanel buttonPanel = new JPanel(new FlowLayout());
		
		pbAccept = createButton("Complete", 'c');
		
		if(displayOnly)
		{
			buttonPanel.add(pbAccept);
		}
		
		pbCancel = createButton("Suspend", 'n');
		buttonPanel.add(pbCancel);

		// main panel
		JPanel contentPane = new JPanel(new BorderLayout());
		contentPane.add(headerPanel, BorderLayout.NORTH);
		contentPane.add(new JScrollPane(hazmatTable), BorderLayout.CENTER);
		contentPane.add(buttonPanel, BorderLayout.SOUTH);
		
		//EF Document Dialog settings
		setTitle(getClass().getSimpleName());
		setContentPane(contentPane);
		setPreferredSize(new Dimension(540, 480)); 
		setModal(true);

		pack();		
		
		// Set location in main screen
		this.setLocationRelativeTo(getParentFrame());		
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}
	
	/**
	 * Fills the hazmat table with the <code>MFSHazmatCntr</code> data.
	 */
	private void fillHazmatTable()
	{
		Collection<MFSHazmatPn> hazmatPns = hazmatCntr.getHazmatPns().values();
		
		DefaultTableModel dtm = (DefaultTableModel) hazmatTable.getModel();
		
		String verification = "";
		
		for(MFSHazmatPn hazmatPn : hazmatPns)
		{
			switch(hazmatPn.getStatus())
			{
				case MFSHazmatLabeling.STATUS_RR: 
					verification = REMOVE_REQUIRED;
					break;
					
				case MFSHazmatLabeling.STATUS_VR:
					verification = INCOMPLETE;
					break;
					
				case MFSHazmatLabeling.STATUS_SV:
					verification = COMPLETED;
					break;
					
				// STATUS_SR should never reach here, the part must be removed already
			}

			dtm.addRow(new Object[]{hazmatPn.getPn(), verification});
		}
	}
	
	/**
	 * Gets the label part number row position
	 * @param pn the label part number
	 * @return the row number of the given part number.
	 */
	protected int getPnRowIndex(String pn)
	{
		Set<String> hazmatPns = hazmatCntr.getHazmatPns().keySet();
		
		int rowIndex = 0;
		
		for(String hazmatPn : hazmatPns)
		{
			if(pn.equals(hazmatPn))
			{
				return rowIndex;
			}
			
			rowIndex++;
		}
		
		return -1;
	}
	
	/**
	 * @return the isCompleteSelected
	 */
	public boolean isCompleteSelected() {
		return isCompleteSelected;
	}

	/**
	 * Invoked when a key is pressed.
	 * @param ke the <code>KeyEvent</code>
	 */
	public void keyPressed(KeyEvent ke)
	{
		final int keyCode = ke.getKeyCode();
		
		if (keyCode == KeyEvent.VK_ENTER)
		{
			final Object source = ke.getSource();
			
			if(source instanceof JButton)
			{
				JButton button = (JButton) source;
				button.requestFocusInWindow();
				button.doClick();
			}
			else if(source instanceof JTextField)
			{
				//this.pbEnter.requestFocusInWindow();
				this.pbEnter.doClick();
			}
		}
		else if (keyCode == KeyEvent.VK_ESCAPE)
		{
			this.pbCancel.requestFocusInWindow();
			this.pbCancel.doClick();
		}
	}
	
	/**
	 * Decodes the barcode field input.
	 * @return the part number to be verified.
	 */
	public String rcvBcInput()
	{
		String pn = null;
		
		try
		{
			removeMyListeners();
			
			MFSBCBarcodeDecoder barcode = MFSBCBarcodeDecoder.getInstance();
			barcode.setMyBarcodeInput(tfBarcode.getText());			
			barcode.initializeDecoder();
			// Start decoding
			barcode.decodeBarcodeFor(this);
			
			tfBarcode.setText("");
				
			// part object
			int rc = barcode.getBCMyPartObject().getRC();

			// invalid
			if(0 != rc)
			{
				throw new Exception(barcode.getBCMyPartObject().getErrorString());
			}

			// all good
			pn = barcode.getBCMyPartObject().getPN();
		}
		catch(Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, e.getMessage(), null);
		}
		finally
		{
			addMyListeners();
		}
		
		return pn;
	}
	
	/**
	 * Remove this dialog listeners
	 */
	private void removeMyListeners()
	{
		tfBarcode.removeKeyListener(this);
		
		pbEnter.removeActionListener(this);
		pbEnter.removeKeyListener(this);
		
		pbAccept.removeActionListener(this);
		pbAccept.removeKeyListener(this);

		pbCancel.removeActionListener(this);
		pbCancel.removeKeyListener(this);
	}
	
	/**
	 * @param isCompleteSelected the isCompleteSelected to set
	 */
	public void setCompleteSelected(boolean isCompleteSelected) {
		this.isCompleteSelected = isCompleteSelected;
	}
	
	/**
	 * Verifies the given label part number.
	 * @param pn the label part number.
	 */
	public void verifyPN(String pn)
	{
		if(null != pn)
		{
			if(hazmatCntr.getHazmatPns().containsKey(pn))
			{
				Calendar cal = Calendar.getInstance();
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH.mm.ss.SSSSSS");
				DefaultTableModel dtm = (DefaultTableModel) hazmatTable.getModel();
				StringBuilder sb = null;
				
				MFSHazmatPn hazmatPn = hazmatCntr.getHazmatPns().get(pn);
				
				switch(hazmatPn.getStatus())
				{
					case MFSHazmatLabeling.STATUS_SR:
						sb = new StringBuilder("PN: ");
						sb.append(pn);
						sb.append(" was removed at ");
						sb.append(hazmatPn.getVerifiedDate());
						IGSMessageBox.showOkMB(getParentFrame(), null, sb.toString(), null);
						break;
					
					case MFSHazmatLabeling.STATUS_RR:
						hazmatPn.setVerifiedDate(df.format(cal.getTime()));
						hazmatPn.setStatus(MFSHazmatLabeling.STATUS_SR);	
						hazmatPn.setChanged(true);
						dtm.setValueAt(REMOVED, getPnRowIndex(pn), 1);
						break;
						
					case MFSHazmatLabeling.STATUS_VR:
						hazmatPn.setVerifiedDate(df.format(cal.getTime()));
						hazmatPn.setStatus(MFSHazmatLabeling.STATUS_SV);						
						dtm.setValueAt(COMPLETED, getPnRowIndex(pn), 1);
						hazmatPn.setChanged(true);
						break;
				
					case MFSHazmatLabeling.STATUS_SV:
						sb = new StringBuilder("PN: ");
						sb.append(pn);
						sb.append(" was already verified at ");
						sb.append(hazmatPn.getVerifiedDate());
						IGSMessageBox.showOkMB(getParentFrame(), null, sb.toString(), null);
						break;
												
					default:
						IGSMessageBox.showOkMB(getParentFrame(), null, 
										"UNKONWN status : " + hazmatPn.getStatus(), null);
				}
				
				if(MFSConfig.getInstance().containsConfigEntry("EFFICIENCYON") && 
						MFSHazmatLabeling.isCntrCompleted(hazmatCntr))
				{
					pbAccept.requestFocusInWindow();
					pbAccept.doClick();
				}
			}
			else
			{
				IGSMessageBox.showOkMB(getParentFrame(), null, "PN: " + pn + " NOT found for container/mctl.", null);
			}
		}
	}
}
