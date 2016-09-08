/* © Copyright IBM Corporation 2007, 2008. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       RCQ00XXXXXX/Flag	Name             	Details
 * ---------- ----------------	--------------		----------------------------------
 * 2013-11-13 RCQ00267733 		HMRIVAS				-Initial version
 ******************************************************************************/

package com.ibm.rchland.mfgapps.mfsclient.dialog;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.ibm.rchland.mfgapps.client.utils.messagebox.IGSMessageBox;
import com.ibm.rchland.mfgapps.mfsclient.MFSActionableDialog;
import com.ibm.rchland.mfgapps.mfsclient.MFSFrame;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSConstants;
//import com.ibm.rchland.mfgapps.mfsclient.utils.MFStfParser;
import com.ibm.rchland.mfgapps.mfscommon.exception.MFSException;

/**
 * Generic dialog class to get one value and perform an action
 * 
 * @author The MFS Client Development Team
 */
public class MFSGetOneValueDialog extends MFSActionableDialog {
	private static final long serialVersionUID = 1L;

	/** label asking for input */
	private JLabel lblInput = null;

	/** The input <code>JTextField</code>. */
	private JTextField tfInput = null;

	/** value that was input */
	private String fieldInputValue = null;

	/** The OK <code>JButton</code>, hopefully gets better text */
	private JButton pbProceed = createButton("OK", 'o');

	/** The Cancel <code>JButton</code>. */
	private JButton pbCancel = createButton("Cancel", 'n');

	/** Set <code>true</code> when the proceed button is selected. */
	private boolean fieldProceedSelected = false;

	/**
	 * Constructs a new <code>MFSGetValueDialog</code>.
	 * 
	 * @param parent
	 *            the <code>MFSFrame</code> that caused this to be displayed
	 * @param prompt
	 *            the text used to ask for a value
	 * @param label
	 *            label in front of text field
	 * @param okButton
	 *            Name for ok button
	 * @param mnem
	 *            Mnemonic for ok button
	 */
	public MFSGetOneValueDialog(MFSFrame parent, String prompt, String label, String okButton, char mnem) {
		super(parent, prompt);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		if (okButton != null) {
			//this.pbCancel = createButton("Cancel", 'c');
			this.pbProceed = createButton(okButton, 'o');
		}

		if ((label == null) || (label.length() == 0)) {
			label = "Value";
		}

		this.lblInput = createSmallNameLabel(label + ":"); //$NON-NLS-1$

		createLayout();
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

		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(10, 0, 5, 5), 0, 0);
		contentPane.add(this.lblInput, gbc);

		this.tfInput = createTextField(MFSConstants.LARGE_TF_COLS, 0, this.lblInput);
		gbc.gridx = 1;
		contentPane.add(this.tfInput, gbc);

		gbc.gridx = 0;
		gbc.gridy = 3;
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
		this.tfInput.addKeyListener(this);
		this.pbProceed.addKeyListener(this);
		this.pbCancel.addKeyListener(this);
	}

	/**
	 * @return Returns the inputValue.
	 */
	public String getInputValue() {
		return this.fieldInputValue;
	}

	/**
	 * Returns <code>true</code> if the proceed button was selected.
	 * 
	 * @return <code>true</code> if the proceed button was selected
	 */
	public boolean getProceedSelected() {
		return this.fieldProceedSelected;
	}

	/**
	 * Validate if input is alphanumeric
	 * 
	 * @param input
	 * 			Input to validate
	 * @return 
	 * 			True if input is alphanumeric
	 */
	public boolean validateAlphanum(String input) {

		boolean result = false;
		Pattern pat = Pattern.compile("[a-zA-Z0-9]{7}");
	    Matcher mat = pat.matcher(input);
	    result = mat.matches();
	    
	    return result;
	}

	/**
	 * Parse out the input and say if we got something
	 */
	private boolean rcvInput() throws MFSException {
		boolean retval = true;

		String input = this.tfInput.getText().trim();

		if (validateAlphanum(input)) {
			retval = true;
			this.fieldInputValue = this.tfInput.getText().trim();

		} else {
			this.tfInput.setText("");
			String title = "Entry Error";
			String erms = "You must enter ten alphanumeric value.";
			IGSMessageBox.showOkMB(getParent(), title, erms, null);
			this.tfInput.requestFocusInWindow();
			retval = false;
		}

		return retval;
	}

	/**
	 * Invoked when an action occurs.
	 * 
	 * @param ae
	 *            the <code>ActionEvent</code>
	 */
	public void actionPerformed(ActionEvent ae) {
		try {
			final Object source = ae.getSource();
			if (source == this.pbProceed) {
				if (rcvInput()) {
					this.fieldProceedSelected = true;
					dispose();
				}
			} else if (source == this.pbCancel) {
				this.fieldInputValue = null;
				this.fieldProceedSelected = false;
				dispose();
			}
		} catch (Throwable throwable) {
			throwable.printStackTrace();
		}
	}

	/**
	 * Invoked when a key is pressed.
	 * 
	 * @param ke
	 *            the <code>KeyEvent</code>
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
	 * 
	 * @param we
	 *            the <code>WindowEvent</code>
	 */
	public void windowOpened(WindowEvent we) {
		if (we.getSource() == this) {
			this.tfInput.requestFocusInWindow();
		}
	}

	/**
	 * Invoked when the user attempts to close the window from the window's system menu. Does nothing.
	 * 
	 * @param we
	 *            the <code>WindowEvent</code>
	 */
	public void windowClosing(WindowEvent we) {
		this.fieldProceedSelected = false;
		super.windowClosing(we);
	}

}
