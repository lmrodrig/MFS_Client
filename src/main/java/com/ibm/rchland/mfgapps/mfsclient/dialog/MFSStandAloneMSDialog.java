/* @ Copyright IBM Corporation 2005, 2016. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2007-01-27      34242JR  R Prechel        -Java 5 version
 * 2007-04-26   ~1 37241JM  R Prechel        -Add drop down combo to select auto or manual printing
 *                                           -Change print method for wusnlbls and matpmcsn2
 * 2007-06-20   ~2 38872JM  R Prechel        -Print MATPMCSN every time manual is selected
 * 2007-08-21   ~3 38131JL  VH Avila         -The MS Barcode will not be used anymore
 * 2007-09-27      39990JM  R Prechel        -Redone; no change flags
 * 2007-11-06  ~04 40334JM  Toribio H.       -Allow for the user to be able to enter a MS or S barcodes
 *                                            for manual Print. Added some checks. 
 *                                            Added Small Destructible Manual Print Option
 * 2016-02-18  ~05 1471226  Miguel Rivas     -getSelectedValues deprecated                                           
 *******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.dialog;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;

import com.ibm.rchland.mfgapps.client.utils.messagebox.IGSMessageBox;
import com.ibm.rchland.mfgapps.mfsclient.MFSActionableDialog;
import com.ibm.rchland.mfgapps.mfsclient.MFSFrame;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSConstants;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSPrintingMethods;

/**
 * <code>MFSStandAloneMSDialog</code> is an <code>MFSActionableDialog</code>
 * used to collect input for and print several different labels.
 * @author The MFS Client Development Team
 */
public class MFSStandAloneMSDialog
	extends MFSActionableDialog
{
	private static final long serialVersionUID = 1L;
	/** The MCTL input <code>JTextField</code>. */
	private JTextField tfMctl;

	/** The Model input <code>JTextField</code>. */
	private JTextField tfModel;  //~04A

	/** The Quantity input <code>JTextField</code>. */
	private JTextField tfQty;

	/** The print type <code>JList</code>. */
	private JList lstPrintType = createList();

	/** The <code>JScrollPane</code> that contains the list of print types. */
	private JScrollPane spPrintType = new JScrollPane(this.lstPrintType,
			ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
			ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

	/** The Auto Print <code>JButton</code>. */
	private JButton pbAuto = createButton("Auto Print", 'A');

	/** The Manual Print <code>JButton</code>. */
	private JButton pbManual = createButton("Manual Print", 'M');

	/** The Cancel <code>JButton</code>. */
	private JButton pbCancel = createButton("Esc = Cancel", 'n');

	/** The validated MCTL. */
	private String fieldMctl;

	/** The validated MODEL. */
	private String fieldModel; //~04A

	/** The validated MATP. */
	private String fieldMatp; //~04A
	
	/** The validated SNUM. */
	private String fieldSnum; //~04A
	
	/** The validated MCSN. */
	private String fieldMcsn; //~04A

	/** The validated quantity. */
	private int fieldQuantity;

	/** Used when an Exception error has ocurred. */
	private static final int EXCEPTION_ERROR = -1; //~04A

	/** Used when a Quantity input error has ocurred. */
	private static final int QTY_ERROR = 1; //~04A

	/** Used when a Mctl input error has ocurred. */
	private static final int MCTL_ERROR = 2; //~04A

	/** Used when a Model input error has ocurred. */
	private static final int MODEL_ERROR = 3; //~04A

	/** Used when a Label input error has ocurred. */
	private static final int LABEL_ERROR = 4; //~04A

	/** Control number Label option. */ //~04A
	private static final String LABEL_CONTROL_NUMBER = "Control Number"; //$NON-NLS-1$

	/** Machine Serial Label option. */ //~04A
	private static final String LABEL_MACHINE_SERIAL = "Machine Serial"; //$NON-NLS-1$

	/** Large Destructable Label option. */ //~04A
	private static final String LABEL_SMALL_DESTRUCTIBLE = "Small Destructible"; //$NON_NLS-1$

	/** Large Destructable Label option. */ //~04A
	private static final String LABEL_LARGE_DESTRUCTIBLE = "Large Destructible"; //$NON_NLS-1$

	/** System Certification Label option. */ //~04A
	private static final String LABEL_SYSTEM_CERTIFICATION = "System Certification"; //$NON_NLS-1$

	/**
	 * Constructs a new <code>MFSStandAloneMSDialog</code>.
	 * @param parent the <code>MFSFrame</code> that caused this
	 *        <code>MFSStandAloneMSDialog</code> to be displayed
	 */
	public MFSStandAloneMSDialog(MFSFrame parent)
	{
		super(parent, "Enter Label Data");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		createLayout();
		addMyListeners();
	}

	/** Adds this dialog's <code>Component</code>s to the layout. */
	private void createLayout()
	{
		String[] listData = {
				LABEL_CONTROL_NUMBER, LABEL_MACHINE_SERIAL,
				LABEL_SMALL_DESTRUCTIBLE,LABEL_LARGE_DESTRUCTIBLE,
				LABEL_SYSTEM_CERTIFICATION
		}; //~04C
		this.lstPrintType.setListData(listData);
		this.lstPrintType.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		this.lstPrintType.setVisibleRowCount(listData.length);

		JLabel cn1Label = createLabel("Enter an 8 character Control Number,");
		
		JLabel cn2Label = createLabel("'S' or 'MS' Machine Serial Number");
		this.tfMctl = createTextField(MFSConstants.LARGE_TF_COLS, 0, cn2Label);

		JLabel modelLabel = createNameLabel("Model"); //~04A
		this.tfModel = createTextField(MFSConstants.SMALL_TF_COLS / 2, 0, modelLabel);

		JLabel qtyLabel = createNameLabel("Quantity");
		this.tfQty = createTextField(MFSConstants.SMALL_TF_COLS / 2, 0, qtyLabel);

		JLabel manualLabel = createLabel("Manual Print Only");
		manualLabel.setFont(MFSConstants.SMALL_DIALOG_FONT);

		JLabel selectLabel = createLabel("Select Labels to Print");
		selectLabel.setFont(MFSConstants.SMALL_DIALOG_FONT);

		JLabel clickLabel = createLabel("CTRL + Click to Select Multiple Labels");
		clickLabel.setFont(MFSConstants.SMALL_DIALOG_FONT);

		JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 10, 0));
		buttonPanel.add(this.pbAuto);
		buttonPanel.add(this.pbManual);
		buttonPanel.add(this.pbCancel);

		JPanel contentPane = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 2, 1, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, 
				new Insets(30, 5, 0, 5), 0, 0);

		gbc.gridy++;
		contentPane.add(cn1Label, gbc);

		gbc.gridy++;
		gbc.insets.top = 0;
		gbc.insets.bottom = 0;
		contentPane.add(cn2Label, gbc); //~04A

		gbc.gridy++;
		gbc.insets.top = 0;
		gbc.insets.bottom = 10;
		contentPane.add(this.tfMctl, gbc);

		gbc.gridy++;
		gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.EAST;
		contentPane.add(modelLabel, gbc); //~04A

		gbc.gridx = 1;
		gbc.anchor = GridBagConstraints.WEST;
		contentPane.add(this.tfModel, gbc); //~04A

		gbc.gridy++;
		gbc.gridx = 0;
		gbc.anchor = GridBagConstraints.EAST;
		contentPane.add(qtyLabel, gbc);

		gbc.gridx = 1;
		gbc.anchor = GridBagConstraints.WEST;
		contentPane.add(this.tfQty, gbc);

		gbc.gridx = 0;
		gbc.gridy++;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.insets.top = 0;
		gbc.insets.bottom = 0;
		contentPane.add(manualLabel, gbc);

		gbc.gridy++;
		contentPane.add(selectLabel, gbc);

		gbc.gridy++;
		contentPane.add(this.spPrintType, gbc);

		gbc.gridy++;
		gbc.insets.top = 5;
		contentPane.add(clickLabel, gbc);

		gbc.gridy++;
		gbc.insets = new Insets(30, 20, 30, 20);
		contentPane.add(buttonPanel, gbc);

		setContentPane(contentPane);
		pack();
	}
	/** Adds the listeners to this dialog's <code>Component</code>s. */
	private void addMyListeners()
	{
		this.pbAuto.addActionListener(this);
		this.pbManual.addActionListener(this);
		this.pbCancel.addActionListener(this);

		this.pbAuto.addKeyListener(this);
		this.pbManual.addKeyListener(this);
		this.pbCancel.addKeyListener(this);
		this.tfMctl.addKeyListener(this);
		this.tfModel.addKeyListener(this);
		this.tfQty.addKeyListener(this);
	}
	
	//	~04A
	/** Resets input <code>JTextField</code>s to initial values. */	
	private void resetInputValues()
	{
		this.tfQty.setText(""); //$NON-NLS-1$
		this.tfMctl.setText(""); //$NON-NLS-1$
		this.tfModel.setText(""); //$NON-NLS-1$
		this.tfMctl.requestFocusInWindow();		
	}
	
	/** Invoked when {@link #pbAuto} is selected. */
	private void autoPrint()
	{
		try
		{
			MFSPrintingMethods.wusnlbls(this.fieldMctl, this.fieldQuantity, 
					"N", //$NON-NLS-1$
					"N", //$NON-NLS-1$
					"N", //$NON-NLS-1$
					"N", //$NON-NLS-1$
					"N", //$NON-NLS-1$
					"Y", //$NON-NLS-1$
					getParentFrame());
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(this, null, null, e);
		}
		this.resetInputValues();
	}
	
	/** Invoked when {@link #pbManual} is selected. */
	private void manualPrint()
	{
		try
		{
			if(this.fieldMctl.length() == 0)
			{   /* Machine Serial has been entered */
				if(this.isValueSelected(LABEL_MACHINE_SERIAL))
				{
					MFSPrintingMethods.machserial(this.fieldMcsn, 
							this.fieldQuantity, getParentFrame());
				}
				if(this.isValueSelected(LABEL_SMALL_DESTRUCTIBLE))
				{
					MFSPrintingMethods.matpmcsn2(this.fieldMatp, this.fieldModel, 
							this.fieldSnum, this.fieldQuantity, getParentFrame());
				}
				if(this.isValueSelected(LABEL_LARGE_DESTRUCTIBLE))
				{
					MFSPrintingMethods.matpmcsn(this.fieldMatp, this.fieldModel, 
							this.fieldSnum, this.fieldQuantity, getParentFrame());
				}						
			}
			else /* Control Number has been entered */
			{
				String ctrlnumbr = "N"; //$NON-NLS-1$
				String machserial = "N"; //$NON-NLS-1$
				String matpmcsn = "N"; //$NON-NLS-1$
				String matpmcsn2 = "N"; //$NON-NLS-1$
				String syscert = "N"; //$NON-NLS-1$
				String auto = "N"; //$NON-NLS-1$
				/* Check all options selected from the JList to update the flags */				
				if(this.isValueSelected(LABEL_CONTROL_NUMBER))
				{
					ctrlnumbr = "Y"; //$NON-NLS-1$
				}
				if(this.isValueSelected(LABEL_MACHINE_SERIAL))
				{
					machserial = "Y"; //$NON-NLS-1$
				}
				if(this.isValueSelected(LABEL_SMALL_DESTRUCTIBLE)) //~04A
				{
					matpmcsn2 = "Y"; //$NON-NLS-1$
				}
				if(this.isValueSelected(LABEL_LARGE_DESTRUCTIBLE))
				{
					matpmcsn = "Y"; //$NON-NLS-1$
				}
				if(this.isValueSelected(LABEL_SYSTEM_CERTIFICATION))
				{
					syscert = "Y"; //$NON-NLS-1$
				}
				MFSPrintingMethods.wusnlbls(this.fieldMctl, this.fieldQuantity,
						ctrlnumbr, machserial, matpmcsn, matpmcsn2,		//~04C 
						syscert, auto, getParentFrame());
			}	
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(this, null, null, e);
		}
		this.resetInputValues();
	}
	
	//	~04A
	/**
	 * Checks if given value is selected in <code>JList</code>.
	 * @param value <code>String</code>
	 * @return <code>true</code> if value is selected;
	 */
	private boolean isValueSelected(String value)
	{
		/* ~05C
		Object[] selValues = null;
		selValues = this.lstPrintType.getSelectedValues();
		
		for(int i = 0; i < selValues.length; i++)
		{
			if(selValues[i].equals(value))
			{
				return true;
			}			
		}*/
		
		List selValues = null;
		selValues = this.lstPrintType.getSelectedValuesList();
		
		for(int i = 0; i < selValues.size(); i++)
		{
			if(selValues.get(i).equals(value))
			{
				return true;
			}			
		}
		return false;
	}

	//	~04C	
	/**
	 * Validate the control number and quantity entered by the user.
	 * @param isAutoPrint <code>boolean</code>
	 * @return 0 if validation was successful; nonzero if validation failed
	 */
	private int validateValues(boolean isAutoPrint)
	{
		int rc = 0;
		try
		{
			String errorTitle = null;
			String errorString = null;
			String inputValue = null;
			
			inputValue = this.tfMctl.getText().trim().toUpperCase();
			this.tfMctl.setText(inputValue);
			
			this.fieldQuantity = 1;
			this.fieldMctl = ""; //$NON-NLS-1$
			this.fieldMatp = ""; //$NON-NLS-1$
			this.fieldSnum = ""; //$NON-NLS-1$
			this.fieldMcsn = ""; //$NON-NLS-1$
		
			// Check for valid inputValue and Parse if needed
			if(inputValue.length() == 8)
			{
				this.fieldMctl = inputValue;
			}
			else if(inputValue.length() >= 1 && inputValue.substring(0,1).equals("S"))
			{
				if(inputValue.length() == 14)
				{
					this.fieldMatp = inputValue.substring(1, 5);
					this.fieldSnum = inputValue.substring(5, 7) + "-" + inputValue.substring(9, 14); //$NON-NLS-1$
					this.fieldMcsn = inputValue.substring(0, 7) + inputValue.substring(9, 14);
				}
				else if(inputValue.length() == 12)
				{
					this.fieldMatp = inputValue.substring(1, 5);
					this.fieldSnum = inputValue.substring(5, 7) + "-" + inputValue.substring(7, 12); //$NON-NLS-1$
					this.fieldMcsn = inputValue.substring(0, 12);
				}
				else
				{
					rc = MCTL_ERROR;
					errorTitle = "Invalid Machine Serial"; //$NON-NLS-1$
					errorString = "You must enter a valid 'S' Machine Serial."; //$NON-NLS-1$					
				}
			}
			else if(inputValue.length() >= 2 && inputValue.substring(0,2).equals("MS"))
			{
				if(inputValue.length() == 15)
				{
					this.fieldMatp = inputValue.substring(2, 6);
					this.fieldSnum = inputValue.substring(6, 8) + "-" + inputValue.substring(10, 15); //$NON-NLS-1$
					this.fieldMcsn = inputValue.substring(1, 8) + inputValue.substring(10, 15);					
				}
				else if(inputValue.length() == 13)
				{
					this.fieldMatp = inputValue.substring(2, 6);
					this.fieldSnum = inputValue.substring(6, 8) + "-" + inputValue.substring(8, 13); //$NON-NLS-1$
					this.fieldMcsn = inputValue.substring(1, 13);
				}
				else
				{
					rc = MCTL_ERROR;
					errorTitle = "Invalid Machine Serial";
					errorString = "You must enter a valid 'MS' Machine Serial.";					
				}
			}
			else
			{
				rc = MCTL_ERROR;
				errorTitle = "Invalid value"; //$NON-NLS-1$
				errorString = "You must enter a valid Control Number, 'S' or 'MS' Machine Serial."; //$NON-NLS-1$									
			}
			if(rc == 0 && this.fieldMctl.length() == 0)
			{   //Checks Model when MCTL is empty and Machine Serial is entered
				this.fieldModel = this.tfModel.getText().trim().toUpperCase();
				this.tfModel.setText(this.fieldModel);
				if(this.fieldModel.length() == 0)
				{
					rc = MODEL_ERROR;
					errorTitle = "Invalid Model"; //$NON-NLS-1$
					errorString = "Model Number can not be empty. Please enter a Model Number."; //$NON-NLS-1$						
				}				
			}
			if(rc == 0)
			{	// Check valid qty
				try
				{
					final String qty = this.tfQty.getText();
					if (qty.length() != 0)
					{
						this.fieldQuantity = Integer.parseInt(qty);
					}
				}
				catch (NumberFormatException e)
				{
					rc = QTY_ERROR;
					errorTitle = "Invalid Quantity"; //$NON-NLS-1$
					errorString = "An invalid quantity was entered. Please enter a valid quantity."; //$NON-NLS-1$
				}
				if (this.fieldQuantity <= 0)
				{
					rc = QTY_ERROR;
					errorTitle = "Invalid Quantity"; //$NON-NLS-1$
					errorString = "Quantity must be higher than zero. Please enter a valid quantity."; //$NON-NLS-1$
				}
			}
			if(rc == 0)
			{
				if(isAutoPrint)
				{
					if(this.fieldMctl.length() == 0)
					{
						rc = MCTL_ERROR;
						errorTitle = "Invalid Print Option"; //$NON-NLS-1$
						errorString = "Auto Print can only be used for Control Numbers. Please enter a valid Control Number or select Manual Print."; //$NON-NLS-1$												
					}
				}
				else 
				{   // Check Label selection when Manual Print
					if((this.lstPrintType.isSelectionEmpty() == true))
					{
						rc = LABEL_ERROR;
						errorTitle = "Invalid Label Selection"; //$NON-NLS-1$
						errorString = "Select at least one label type to print."; //$NON-NLS-1$
					}	
					else if(this.fieldMctl.length() == 0)
					{
						if(this.isValueSelected(LABEL_CONTROL_NUMBER))
						{
							rc = LABEL_ERROR;
							errorTitle = "Invalid Label Selection"; //$NON-NLS-1$
							errorString = LABEL_CONTROL_NUMBER + 
								" label is only for Control Numbers. Please remove from list."; //$NON-NLS-1$
						}
						else if(this.isValueSelected(LABEL_SYSTEM_CERTIFICATION))
						{
							rc = LABEL_ERROR;
							errorTitle = "Invalid Label Selection"; //$NON-NLS-1$
							errorString = LABEL_SYSTEM_CERTIFICATION + 
								" label is only for Control Numbers. Please remove from list."; //$NON-NLS-1$
						}					
					}
				}
			}
			if (rc != 0)
			{
				IGSMessageBox.showOkMB(this, errorTitle, errorString, null);
				this.toFront();

				// find the faulty field and give it the focus
				if (rc == QTY_ERROR)
				{
					this.tfQty.selectAll();
					this.tfQty.requestFocusInWindow();
				}
				else if (rc == MCTL_ERROR)
				{
					this.tfMctl.selectAll();
					this.tfMctl.requestFocusInWindow();
				}
				else if(rc == MODEL_ERROR)
				{
					this.tfModel.selectAll();
					this.tfModel.requestFocusInWindow();
				}
				else if(rc == LABEL_ERROR)
				{
					this.lstPrintType.requestFocusInWindow();					
				}
			}
		}
		catch (Exception e)
		{
			rc = EXCEPTION_ERROR;
			IGSMessageBox.showOkMB(this, null, null, e);
			this.toFront();
			this.resetInputValues();			
		}
		return rc;
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
			if (source == this.pbCancel)
			{
				dispose();
			}
			else if (source == this.pbAuto)
			{
				if (validateValues(true) == 0)
				{
					autoPrint();
				}
			}
			else if (source == this.pbManual)
			{
				if (validateValues(false) == 0)
				{
					manualPrint();
				}
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
		if (keyCode == KeyEvent.VK_ENTER)
		{
			final Object source = ke.getSource();
			if (source instanceof JButton)
			{
				JButton button = (JButton) source;
				button.requestFocusInWindow();
				button.doClick();
			}
		}
		else if (keyCode == KeyEvent.VK_ESCAPE)
		{
			this.pbCancel.requestFocusInWindow();
			this.pbCancel.doClick();
		}
	}
	/**
	 * Invoked the first time a window is made visible.
	 * <p>
	 * Requests the focus for {@link #tfMctl}.
	 * @param we the <code>WindowEvent</code>
	 */
	public void windowOpened(WindowEvent we)
	{
		if (we.getSource() == this)
		{
			this.tfMctl.requestFocusInWindow();
		}
	}
}