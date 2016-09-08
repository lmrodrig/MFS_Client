/* © Copyright IBM Corporation 2007, 2008. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ------------------------------------------
 * 2007-12-06      37616JL  D Pietrasik      -Initial version
 * 2009-09-01      37550JL  Brian Becker     -added method to accept two input values.
 **************************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.dialog;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

//import com.ibm.rchland.mfgapps.client.utils.io.IGSPad;
import com.ibm.rchland.mfgapps.client.utils.messagebox.IGSMessageBox;
import com.ibm.rchland.mfgapps.mfsclient.MFSActionableDialog;
import com.ibm.rchland.mfgapps.mfsclient.MFSFrame;
//import com.ibm.rchland.mfgapps.mfsclient.barcode.MFSBCBarcodeDecoder;
//import com.ibm.rchland.mfgapps.mfsclient.barcode.MFSBCIndicatorValue;
//import com.ibm.rchland.mfgapps.mfsclient.barcode.MFSBCPartObject;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSConstants;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFStfParser;
import com.ibm.rchland.mfgapps.mfscommon.exception.MFSException;

/**
 * Generic dialog class to get one value and perform an action
 * @author The MFS Client Development Team
 */
public class MFSGetValuesDialog extends MFSActionableDialog {
	private static final long serialVersionUID = 1L;
	/** label asking for input */
	private JLabel lblInput1 = null;
	private JLabel lblInput2 = null;

	/** The input <code>JTextField</code>. */
	private JTextField tfInput1 = null;
	private JTextField tfInput2 = null;

	/** value that was input */
	private String fieldInputValue1 = null;
	private String fieldInputValue2 = null;

	/** for parsing the input field */
	private MFStfParser textParser1 = null;
	private MFStfParser textParser2 = null;

	/** The OK <code>JButton</code>, hopefully gets better text */
	private JButton pbProceed = createButton("OK", 'o');

	/** The Cancel <code>JButton</code>. */
	private JButton pbCancel = createButton("Cancel", 'n');
	
	/** Set <code>true</code> when the proceed button is selected. */
	private boolean fieldProceedSelected = false;


	/**
	 * Constructs a new <code>MFSGetValueDialog</code>.
	 * @param parent the <code>MFSFrame</code> that caused this to be
	 *        displayed
	 * @param prompt the text used to ask for a value
	 * @param label label in front of text field
	 * @param okButton Name for ok button
	 * @param mnem Mnemonic for ok button
	 */
	public MFSGetValuesDialog(MFSFrame parent, String prompt, String label1, String label2, 
		String okButton, char mnem) {
		
		super(parent, prompt);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		if (okButton != null) {
			if (mnem == 'n') { /* make sure we don't duplicate the mnemonic */
				this.pbCancel = createButton("Cancel", 'c');
			}
			this.pbProceed = createButton(okButton, mnem);
		}
		
		if ((label1 == null) || (label1.length() == 0)) {
			label1 = "Value1";
		}
		if ((label2 == null) || (label2.length() == 0)) {
			label2 = "Value2";
		}

		this.lblInput1 = createSmallNameLabel(label1 + ":"); //$NON-NLS-1$
		this.lblInput2 = createSmallNameLabel(label2 + ":"); //$NON-NLS-1$

		System.out.println("starting createLayout");
		createLayout();
		System.out.println("starting addMyListeners");
		addMyListeners();
	}

	
	/** 
	 * Adds this dialog's <code>Component</code>s to the layout. 
	 */
	private void createLayout() {
		JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 30, 0));
		buttonPanel.add(this.pbProceed);
		buttonPanel.add(this.pbCancel);

		JPanel contentPane = new JPanel(new GridBagLayout());
		contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(10, 0, 5, 5), 0, 0);

		// gridx = column, gridy = row
		// CNTR
		gbc.gridx = 0;
		gbc.gridy = 0;
		contentPane.add(this.lblInput1, gbc);
		this.tfInput1 = createTextField(MFSConstants.LARGE_TF_COLS, 0, this.lblInput1);
		gbc.gridx = 1;
		gbc.gridy = 0;
		contentPane.add(this.tfInput1, gbc);

		
		// ITEM
		gbc.gridx = 0;
		gbc.gridy = 1;
		contentPane.add(this.lblInput2, gbc);
		this.tfInput2 = createTextField(MFSConstants.LARGE_TF_COLS, 0, this.lblInput2);
		gbc.gridx = 1;
		gbc.gridy = 1;
		contentPane.add(this.tfInput2, gbc);

		// BUTTONS
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.insets = new Insets(10, 0, 10, 0);
		contentPane.add(buttonPanel, gbc);

		setContentPane(contentPane);
		pack();
	}

	
	/** 
	 * Adds the listeners to this dialog's <code>Component</code>s. 
	 */
	private void addMyListeners() {
		this.pbProceed.addActionListener(this);
		this.pbCancel.addActionListener(this);

		this.addKeyListener(this);
		this.tfInput1.addKeyListener(this);
		this.tfInput2.addKeyListener(this);
		this.pbProceed.addKeyListener(this);
		this.pbCancel.addKeyListener(this);
	}

	
	/**
	 * The the MFStfParser to use parsing the text field
	 * @param parse parser to use
	 */
	public void setTextParser1(MFStfParser parse) {
		this.textParser1 = parse;
	}
	public void setTextParser2(MFStfParser parse) {
		this.textParser2 = parse;
	}
	

	/**
	 * @return Returns the inputValue.
	 */
	public String getInputValue1() {
		return this.fieldInputValue1;
	}
	public String getInputValue2() {
		return this.fieldInputValue2;
	}

	
	/**
	 * Set a default value for the input field, and select it so it can be easily overridden
	 * @param dValue default value
	 */
	public void setDefaultValue1(String dValue) {
		if (dValue != null) {
			this.tfInput1.setText(dValue);
			this.tfInput1.selectAll();
		}
	}
	public void setDefaultValue2(String dValue) {
		if (dValue != null) {
			this.tfInput2.setText(dValue);
			this.tfInput2.selectAll();
		}
	}
	
	
	/**
	 * Returns <code>true</code> iff the proceed button was selected.
	 * @return <code>true</code> iff the proceed button was selected
	 */
	public boolean getProceedSelected() {
		return this.fieldProceedSelected;
	}

	
	/**
	 * Invoked when an action occurs.
	 * @param ae the <code>ActionEvent</code>
	 */
	public void actionPerformed(ActionEvent ae) {
		try {
			final Object source = ae.getSource();
			if (source == this.pbProceed) {
				if (rcvInputs()) {
					this.fieldProceedSelected = true;
					dispose();
				}
			} else if (source == this.pbCancel) {
				this.fieldInputValue1 = null;
				this.fieldInputValue2 = null;
				this.fieldProceedSelected = false;
				dispose();
			}
		} catch (Throwable throwable) {
			throwable.printStackTrace();
		}
	}

	
	/** 
	 * Parse out the input and say if we got something
	 */
	private boolean rcvInputs() throws MFSException {
		System.out.println("Starting rcvInputs");
		boolean retval = true;
		
		this.fieldInputValue1 = this.textParser1.recvInput(this.tfInput1.getText());
		this.fieldInputValue2 = this.textParser2.recvInput(this.tfInput2.getText());
		System.out.println("Starting rcvInputs - fieldInputValue1:"+this.fieldInputValue1);
		System.out.println("Starting rcvInputs - fieldInputValue2:"+this.fieldInputValue2);

		if ( this.fieldInputValue1 == null )
			this.fieldInputValue1 = "";
		if ( this.fieldInputValue2 == null )
			this.fieldInputValue2 = "";
		
		// both fields empty
		if ( this.fieldInputValue1.equals("") || this.fieldInputValue1.length() == 0 ) {
			if ( this.fieldInputValue2.equals("") || this.fieldInputValue2.length() == 0 ) {
				String title = "Entry Error";
				String erms = "You must enter a value in one of the input fields.";
				IGSMessageBox.showOkMB(getParent(), title, erms, null);
				retval = false;
			}
		}
		
		// both fields have values
		if ( (null != this.fieldInputValue1) && (this.fieldInputValue1.length() > 0) &&
			 (null != this.fieldInputValue2) && (this.fieldInputValue2.length() > 0) ) {
			String title = "Entry Error";
			String erms = "You must enter only one value.";
			IGSMessageBox.showOkMB(getParent(), title, erms, null);
			retval = false;
		}
		return retval;
	}
	
	
	/**
	 * Invoked when a key is pressed.
	 * @param ke the <code>KeyEvent</code>
	 */
	public void keyPressed(KeyEvent ke) {
		final int keyCode = ke.getKeyCode();
		if (keyCode == KeyEvent.VK_ENTER) {
			final Object source = ke.getSource();
			if (source == this.pbCancel) {
				this.pbCancel.requestFocusInWindow();
				this.pbCancel.doClick();
			} else {
				this.pbProceed.requestFocusInWindow();
				this.pbProceed.doClick();
			}
		} else if (keyCode == KeyEvent.VK_ESCAPE) {
			this.pbCancel.requestFocusInWindow();
			this.pbCancel.doClick();
		}
	}

	
	/**
	 * Invoked the first time a window is made visible.
	 * <p>
	 * Requests the focus for the {@link #tfInput}.
	 * @param we the <code>WindowEvent</code>
	 */
	public void windowOpened(WindowEvent we) {
		if (we.getSource() == this) {
			this.tfInput1.requestFocusInWindow();
		}
	}
	
	
	/**
	 * Invoked when the user attempts to close the window from the window's
	 * system menu. Does nothing.
	 * @param we the <code>WindowEvent</code>
	 */
	public void windowClosing(WindowEvent we) {
		this.fieldProceedSelected = false;
		super.windowClosing(we);
	}
}