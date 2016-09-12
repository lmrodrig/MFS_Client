/* @ Copyright IBM Corporation 2015. All rights reserved.
 * 
 * This class is the stand alone dialog that is used from the MFS Client
 * to enter and update part weight data into the FCSPPWT10 table.
 * 
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2015-09-03      1384186  Andy Williams    -Initial version
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.dialog;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.text.MessageFormat;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.ibm.rchland.mfgapps.client.utils.messagebox.IGSMessageBox;
import com.ibm.rchland.mfgapps.client.utils.xml.IGSXMLTransaction;
import com.ibm.rchland.mfgapps.mfsclient.MFSActionableDialog;
import com.ibm.rchland.mfgapps.mfsclient.MFSFrame;
import com.ibm.rchland.mfgapps.mfsclient.barcode.MFSBCBarcodeDecoder;
import com.ibm.rchland.mfgapps.mfsclient.barcode.MFSBCIndicatorValue;
import com.ibm.rchland.mfgapps.mfsclient.barcode.MFSBCPartObject;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSConstants;
import com.ibm.rchland.mfgapps.mfscommon.MFSActionable;
import com.ibm.rchland.mfgapps.mfscommon.MFSConfig;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSComm;

/**
 * <code>MFSStandAlonePartWeightDialog</code> is the <code>MFSActionableDialog</code>
 * used to collect and update part weight information.
 * @author The MFS Client Development Team
 */
public class MFSStandAlonePartWeightDialog
	extends MFSActionableDialog
{
	private static final long serialVersionUID = 1L;
	/** The part number input <code>JTextField</code>. */
	private JTextField tfPartNumber;

	/** The weight input <code>JTextField</code>. */
	private JTextField tfWeight;

	/** The weight unit of measure <code>JLabel</code>. */
	private JLabel lblWeightUnit; //$NON-NLS-1$

	/** The enter <code>JButton</code>. */
	private JButton pbEnter = createButton("Enter", 'E');
	
	/** The query <code>JButton</code>. */
	private JButton pbQuery = createButton("Query", 'q');

	/** The cancel <code>JButton</code>. */
	private JButton pbCancel = createButton("Cancel", 'n');

	/** Set <code>true</code> when the cancel button is selected. */
	private boolean fieldCancelSelected = false;
		

	/**
	 * Constructs a new <code>MFSStandAlonePartWeightDialog</code>.
	 * @param parent the <code>MFSFrame</code> that caused this
	 *        <code>MFSStandAlonePartWeightDialog</code> to be displayed
	 * @param actionable the <code>MFSActionable</code> that caused this
	 *        <code>MFSStandAlonePartWeightDialog</code> to be displayed
	 */
	public MFSStandAlonePartWeightDialog(MFSFrame parent, MFSActionable actionable)
	{
		super(parent, "Collect Part Weight Information");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		createLayout();
		addMyListeners();
	}

	/** Adds this dialog's <code>Component</code>s to the layout. */
	private void createLayout()
	{
		JLabel lblInstructions = createNameLabel("Enter part number and weight information here:");
		JLabel lblPartNumber = createNameLabel("Part Number:");
		JLabel lblWeight = createNameLabel("Weight:");
		
		if(!MFSConfig.getInstance().getConfigValue("PARTWEIGHTUOM").trim().equals("Not Found"))
		{
			lblWeightUnit = createNameLabel(MFSConfig.getInstance().getConfigValue("PARTWEIGHTUOM"));
		}
		else
		{
			lblWeightUnit = createNameLabel("KG");
		}
		
		this.tfPartNumber = createTextField(MFSConstants.SMALL_TF_COLS, 22, lblPartNumber);
		this.tfWeight = createTextField(MFSConstants.SMALL_TF_COLS, 7, lblWeight);

		JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 40, 0));
		buttonPanel.add(this.pbQuery);
		buttonPanel.add(this.pbEnter);
		buttonPanel.add(this.pbCancel);

		JPanel contentPane = new JPanel(new GridBagLayout());
		contentPane.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 0, 1.0,
				GridBagConstraints.EAST, GridBagConstraints.NONE, 
				new Insets(10, 0, 10, 10), 0, 0);

		gbc.gridx = 0;
		gbc.gridwidth = 3;
		contentPane.add(lblInstructions, gbc);
		gbc.gridwidth = 1;
		gbc.gridy++;
		gbc.insets.left = 30;
		contentPane.add(lblPartNumber, gbc);
		gbc.gridy++;
		contentPane.add(lblWeight, gbc);

		gbc.gridx++;
		gbc.gridy = 1;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets.right = 2;
		contentPane.add(this.tfPartNumber, gbc);
		
		gbc.gridy++;
		gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets.right = 2;
		contentPane.add(this.tfWeight, gbc);

		gbc.gridx++;
		gbc.gridy = 2;
		gbc.weightx = 1;
		gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.WEST;
		contentPane.add(this.lblWeightUnit, gbc);

		gbc.gridx = 0;
		gbc.gridy++;
		gbc.gridwidth = 3;
		gbc.insets.top = 20;
		gbc.anchor = GridBagConstraints.CENTER;
		contentPane.add(buttonPanel, gbc);

		setContentPane(contentPane);
		pack();
	}

	/** Adds the listeners to this dialog's <code>Component</code>s. */
	private void addMyListeners()
	{
		this.pbEnter.addActionListener(this);
		this.pbCancel.addActionListener(this);
		this.pbQuery.addActionListener(this); 	

		this.tfPartNumber.addKeyListener(this);
		this.tfWeight.addKeyListener(this);
		this.pbEnter.addKeyListener(this);
		this.pbCancel.addKeyListener(this);
		this.pbQuery.addKeyListener(this);
	}
	
	
	/**
	 * Displays the dialog for a single container.
	 * @param cntr the container
	 * @return 0 on success; nonzero on error
	 */
	public void display()
	{
		pack();
		setLocationRelativeTo(getParentFrame());
		setVisible(true);
		/* IGSXMLTransaction rtv_prtwgt = new IGSXMLTransaction("RTV_WGTDIM"); //$NON-NLS-1$
		rtv_prtwgt.setActionMessage("Retrieving Weight and Dimensions...");
		rtv_prtwgt.startDocument();
		rtv_prtwgt.addElement("MODE", "C"); //$NON-NLS-1$ //$NON-NLS-2$
		rtv_prtwgt.addElement("CNTR", cntr); //$NON-NLS-1$
		rtv_prtwgt.endDocument(); 
		return display(rtv_prtwgt); */ 
	}

	/**
	 * Validate part number string and pad if necessary.
	 * @param partNumber the original part number string
	 * @return String the 12 character part number.
	 */
	public String validatePartNumber(String partNumber)
	{
		if(partNumber.equals(""))
		{
			String title = "Part Number Required"; //$NON-NLS-1$
			String erms = "Please enter a part number!"; //$NON-NLS-1$
			erms = MessageFormat.format(erms, null);
			IGSMessageBox.showOkMB(getParentFrame(), title, erms, null);
			return "";
		}
		else
		{
			if(partNumber.length() == 7)
			{
				partNumber = "00000" + partNumber; //$NON-NLS-1$
			}
			else if(partNumber.length() != 12)
			{
				String title = "Invalid Part Number"; //$NON-NLS-1$
				String erms = "Part number must be either 7 or 12 characters long!";  //$NON-NLS-1$
				erms = MessageFormat.format(erms, null);
				IGSMessageBox.showOkMB(getParentFrame(), title, erms, null);
				return "";
			}
			
			// Update Part Number text field with the value we'll query
			tfPartNumber.setText(partNumber);
			tfWeight.requestFocus();
			return partNumber;
		}
	}

	/**
	 * Returns <code>true</code> if the cancel button was selected.
	 * @return <code>true</code> if the cancel button was selected
	 */
	public boolean getCancelSelected()
	{
		return this.fieldCancelSelected;
	}

	

	

	/**
	 * Invoked when an action occurs.
	 * @param ae the <code>ActionEvent</code>
	 */
	public void actionPerformed(ActionEvent ae)
	{
		String partNumber = "";
			
		try
		{
			final Object source = ae.getSource();
			if (source == this.pbEnter)
			{
				updatePartWeight();
			}
			else if (source == this.pbQuery)
			{
				receivePartInput();
				partNumber = validatePartNumber(tfPartNumber.getText().trim().toUpperCase());
				if(partNumber.length()<1)
				{
					// End
					return;
				}
				else
				{
					tfWeight.setText(queryPart(partNumber));
				}
			}
			else if (source == this.pbCancel)
			{
				this.fieldCancelSelected = true;
				dispose();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
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
			if (source == this.pbCancel)
			{
				this.pbCancel.requestFocusInWindow();
				this.pbCancel.doClick();
			}
			else if(source == this.pbQuery)
			{
				this.pbQuery.requestFocusInWindow();
				this.pbQuery.doClick();
			}
			else if (source == this.tfPartNumber)
			{
				if(receivePartInput())
				{
					tfWeight.setText(queryPart(tfPartNumber.getText()));
				}
			}
			/*else if (source == this.tfWeight)
			{
				updatePartWeight();
			}*/
			else	
			{
				this.pbEnter.requestFocusInWindow();
				this.pbEnter.doClick();
			}
		}
		else if (keyCode == KeyEvent.VK_ESCAPE)
		{
			this.pbCancel.requestFocusInWindow();
			this.pbCancel.doClick();
		}
	}
	
	private String queryPart(String partNumber)
	{
		IGSXMLTransaction rtv_prtwgt = new IGSXMLTransaction("RTV_PRTWGT"); //$NON-NLS-1$
		rtv_prtwgt.setActionMessage("Retrieving Part Weight...");
		rtv_prtwgt.startDocument();
		rtv_prtwgt.addElement("UOM", lblWeightUnit.getText().trim().toUpperCase()); //$NON-NLS-1$ //$NON-NLS-2$
		rtv_prtwgt.addElement("QUERYLEVEL", "1"); //$NON-NLS-1$
		rtv_prtwgt.addElement("BRANCHOFFICE", ""); //$NON-NLS-1$
		rtv_prtwgt.addElement("MCTL", ""); //$NON-NLS-1$
		rtv_prtwgt.addElement("PART", tfPartNumber.getText().trim()); //$NON-NLS-1$
		rtv_prtwgt.endDocument(); 
		System.out.println(rtv_prtwgt.toString());
		
		MFSComm.getInstance().execute(rtv_prtwgt, this);
		if (rtv_prtwgt.getReturnCode() == 0)
		{
			return(rtv_prtwgt.getNextElement("WEIGHT").trim());
		}
		else
		{
			String title = "Part Weight Record Not Found";
			IGSMessageBox.showOkMB(this, title, "No part weight record was found for the part number entered.", null);
			return(tfWeight.getText());
		}
	
	}
	
	public boolean receivePartInput()
	{
		try
		{
			if((!tfPartNumber.getText().trim().toUpperCase().startsWith("P")) && //$NON-NLS-1$
					(!tfPartNumber.getText().trim().toUpperCase().startsWith("1P")) && //$NON-NLS-1$
					(!tfPartNumber.getText().trim().toUpperCase().startsWith("11S")) ) //$NON-NLS-1$
			{
				/* Handle hand-typed part numbers */
				String partNumber = validatePartNumber(tfPartNumber.getText().trim().toUpperCase());
				if(partNumber.length()<1)
				{
					return false;
				}
				else
				{
					tfPartNumber.setText(partNumber);
					return true;
				}
				
			}
			else if(tfPartNumber.getText().length() != 12)
			{
				/* Decode scanned part number or hand-typed part number with prefix */
				boolean found = false;
				int rc = 0;
	
				MFSBCBarcodeDecoder barcode = MFSBCBarcodeDecoder.getInstance();
				barcode.setMyBarcodeInput(this.tfPartNumber.getText().toUpperCase());
				barcode.setMyBCPartObject(new MFSBCPartObject());
	
				/* setup barcode indicator values */
				MFSBCIndicatorValue bcIndVal = new MFSBCIndicatorValue();
				bcIndVal.setPNRI("1"); //$NON-NLS-1$
				barcode.setMyBCIndicatorValue(bcIndVal);
	
				if (barcode.decodeBarcodeFor(this))
				{
					rc = barcode.getBCMyPartObject().getRC();
				}
				else
				{
					rc = 10;
					barcode.getBCMyPartObject().setErrorString(
							MFSBCBarcodeDecoder.DECODE_TIMED_OUT_ERMS);
				}
	
				if (rc == 0 && !found)
				{
					/* PN */
					if (!(barcode.getBCMyPartObject().getPN().equals(""))) //$NON-NLS-1$
					{
						this.tfPartNumber.setText(barcode.getBCMyPartObject().getPN());
						found = true;
					}
				}
	
				if ((rc != 0) || (!found && !this.tfPartNumber.getText().equals(""))) //$NON-NLS-1$
				{
					String erms = barcode.getBCMyPartObject().getErrorString();
					if (erms.length() == 0)
					{
						erms = MFSBCBarcodeDecoder.INVALID_BARCODE_ERMS;
					}
					IGSMessageBox.showOkMB(this, null, erms, null);
				}
				if(found)
				{
					if(tfWeight.getText().trim().length() > 0)
					{
						updatePartWeight();
					}
					else
					{
						tfWeight.requestFocus();
					}
				}
				return true;
			}
			else
			{
				String title = "Invalid Part Number"; //$NON-NLS-1$
				String erms = "Part number must be either 7 or 12 characters long!";  //$NON-NLS-1$
				erms = MessageFormat.format(erms, null);
				IGSMessageBox.showOkMB(getParentFrame(), title, erms, null);
				return false;
			}
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(this, null, null, e);
			return false;
		}
	}
	
	private void updatePartWeight()
	{
		String partNumber = tfPartNumber.getText();
		String weight = "";
		double fWeight = 0.00;
		
		if(!receivePartInput() || partNumber.length()<1)
		{  //End
			return;
		}
		if(tfWeight.getText().trim().equals(""))
		{
			String title = "Weight Required"; //$NON-NLS-1$
			String erms = "Please enter the weight of the part!"; //$NON-NLS-1$
			erms = MessageFormat.format(erms, null);
			IGSMessageBox.showOkMB(getParentFrame(), title, erms, null);	
		}
		else
		{
			weight = tfWeight.getText().trim().toUpperCase();
			
			try
			{
				fWeight = Double.valueOf(weight);
				
				if(fWeight>9999.99 || fWeight<0.01)
				{
					IGSMessageBox.showOkMB(this, "Number Format Exception", "Weight must be between 0.01 and 9999.99 !", null);
					tfWeight.requestFocus();
					tfWeight.selectAll();
					return;
				}
			}
			catch(NumberFormatException nfe)
			{
				IGSMessageBox.showOkMB(this, "Number Format Exception", "Weight must be a number!", null);
				tfWeight.requestFocus();
				tfWeight.selectAll();
				return;
			}
			
			
			
			IGSXMLTransaction updtpwt10 = new IGSXMLTransaction("UPD_PWT10"); //$NON-NLS-1$
			updtpwt10.setActionMessage("Updating Part Weight..."); //$NON-NLS-1$
			updtpwt10.startDocument();
			updtpwt10.addElement("USER", MFSConfig.getInstance().get8CharUser().trim()); //$NON-NLS-1$
			updtpwt10.addElement("UOM", lblWeightUnit.getText().trim().toUpperCase()); //$NON-NLS-1$
			updtpwt10.startElement("PARTS"); //$NON-NLS-1$
			updtpwt10.startElement("PART"); //$NON-NLS-1$
			updtpwt10.addElement("INPN", tfPartNumber.getText()); //$NON-NLS-1$
			updtpwt10.addElement("WEIGHT", weight); //$NON-NLS-1$
			updtpwt10.endElement("PART"); //$NON-NLS-1$
			updtpwt10.endElement("PARTS"); //$NON-NLS-1$
			updtpwt10.endDocument(); 
			
			System.out.println(updtpwt10.toString());
			
			MFSComm.getInstance().execute(updtpwt10, this);
			if (updtpwt10.getReturnCode() == 0)
			{
				String title = "Update Successful";
				IGSMessageBox.showOkMB(this, title, "Update was successful!", null);
				tfPartNumber.setText("");
				tfPartNumber.requestFocus();
				tfWeight.setText("");
				tfWeight.revalidate();
				tfWeight.repaint();
			}
			else
			{
				String title = "Update Error";
				IGSMessageBox.showOkMB(this, title, updtpwt10.getErms(), null);
			}
		}
	}
}
