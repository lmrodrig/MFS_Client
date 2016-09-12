/* @ Copyright IBM Corporation 2007,2010. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2007-08-27      38007SE  R Prechel        -Initial version
 * 2010-03-08   ~1 47596MZ  D Kloepping      -Automate done selection for Efficiency client
 * 											   when all MAC addresses collected
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.dialog;

import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;

import mfsxml.MfsXMLDocument;

import com.ibm.rchland.mfgapps.client.utils.messagebox.IGSMessageBox;
import com.ibm.rchland.mfgapps.mfsclient.MFSActionableDialog;
import com.ibm.rchland.mfgapps.mfsclient.MFSFrame;
import com.ibm.rchland.mfgapps.mfsclient.barcode.MFSBCBarcodeDecoder;
import com.ibm.rchland.mfgapps.mfsclient.barcode.MFSBCIndicatorValue;
import com.ibm.rchland.mfgapps.mfsclient.barcode.MFSBCPartObject;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSConstants;
import com.ibm.rchland.mfgapps.mfscommon.MFSConfig;

/**
 * <code>MFSMacIDDialogBase</code> is the base
 * <code>MFSActionableDialog</code> for the MAC ID collection dialogs.
 * @author The MFS Print Server Development Team
 */
public abstract class MFSMacIDDialogBase
	extends MFSActionableDialog
{
	private static final long serialVersionUID = 1L;
	/** The input <code>JTextField</code>. */
	protected JTextField tfInput = createTextField(MFSConstants.LARGE_TF_COLS, 0);

	/** The value <code>JLabel</code> displaying the entered part number. */
	protected JLabel vlPN;

	/** The value <code>JLabel</code> displaying the entered sequence number. */
	protected JLabel vlSN;

	/** The done <code>JButton</code>. */
	protected JButton pbDone = createButton("Done", 'D');

	/** The cancel <code>JButton</code>. */
	protected JButton pbCancel = createButton("Cancel", 'n');

	/** Set <code>true</code> iff the done button is selected. */
	protected boolean fieldDoneSelected = false;

	/** The PNRI field value. */
	protected String fieldPnri;

	/** The part number the user must enter. */
	protected String fieldPn;

	/** The sequence number the user must enter. */
	protected String fieldSn;

	/** The CSNI field value. */
	protected String fieldCsni;

	/**
	 * <code>true</code> if the user must enter the part and sequence numbers;
	 * <code>false</code> if the user must enter only MAC Address information.
	 */
	protected boolean fieldRequiresPNSN;

	/**
	 * Constructs a new <code>MFSMacIDDialogBase</code>.
	 * @param parent the <code>MFSFrame</code> that caused this
	 *        <code>MFSMacIDDialogBase</code> to be displayed
	 * @param pnri the value of the PNRI field
	 * @param pn the part number
	 * @param sn the sequence number
	 * @param csni the value of the CSNI field
	 */
	public MFSMacIDDialogBase(MFSFrame parent, String pnri, String pn, String sn,
								String csni)
	{
		super(parent, "MAC Address");
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.fieldPnri = pnri;
		this.fieldPn = pn;
		this.fieldSn = sn;
		this.fieldCsni = csni;
		this.fieldRequiresPNSN = (pn != null && sn != null);
	}

	/**
	 * Returns <code>true</code> iff the done button was selected.
	 * @return <code>true</code> iff the done button was selected
	 */
	public boolean getDoneSelected()
	{
		return this.fieldDoneSelected;
	}

	/**
	 * Adds two <code>JLabel</code>s to the specified <code>component</code>.
	 * The first label displays the specified <code>text</code> (which
	 * describes a value the user must enter). The second label will display the
	 * value entered by the user.
	 * @param component the <code>JComponent</code> to which the labels are added
	 * @param text the text for the first <code>JLabel</code>
	 * @return the <code>JLabel</code> that will display the user entered value
	 */
	protected static JLabel addLabels(JComponent component, String text)
	{
		JLabel label = createSmallNameLabel(text);
		JLabel result = createSmallValueLabel(label);
		component.add(label);
		component.add(result);
		return result;
	}

	/**
	 * Adds the MAC ID XML to an <code>MfsXMLDocument</code>.
	 * @param xmlDocument the <code>MfsXMLDocument</code> to which the MAC ID
	 *        XML will be added
	 */
	public abstract void addMacXmlToDocument(MfsXMLDocument xmlDocument);

	/** Invoked when the done button is selected. */
	protected abstract void handleDoneButton();

	/**
	 * Helper method called during barcode parsing to check for MAC ID scan completion.
	 * ~1a
	 */
	protected abstract void checkForMacCollectionCompletion();
	
	/**
	 * Helper method called during barcode parsing to process MAC ID input.
	 * @param macIDInput the MAC ID input
	 */
	protected abstract void processMacInput(String macIDInput);

	/** Validates and parses the user input. */
	private void parseInput()
	{
		String input = this.tfInput.getText().toUpperCase();
		this.tfInput.setText(""); //$NON-NLS-1$

		if (input.equals(MFSConstants.LOG_BARCODE)) //$NON-NLS-1$
		{
			handleDoneButton();
		}
		else
		{
			MFSBCBarcodeDecoder barcodeDecoder = MFSBCBarcodeDecoder.getInstance();
			barcodeDecoder.setMyBarcodeInput(input);
			setupBarcodeDecoder();

			if (barcodeDecoder.decodeBarcodeFor(this) == false)
			{
				String errorMessage = MFSBCBarcodeDecoder.DECODE_TIMED_OUT_ERMS;
				barcodeDecoder.getBCMyPartObject().setErrorString(errorMessage);
				IGSMessageBox.showOkMB(this, null, errorMessage, null);
			}
			else if (barcodeDecoder.getBCMyPartObject().getRC() == 0)
			{
				MFSBCPartObject partObject = barcodeDecoder.getBCMyPartObject();
				if (partObject.getMC().length() != 0)
				{
					processMacInput(partObject.getMC());
				}
				//Note: A user can enter a PN and an SN using one barcode
				else if (partObject.getPN().length() != 0
						|| partObject.getSN().length() != 0)
				{
					boolean noError = true;
					if (partObject.getPN().length() != 0)
					{
						String erms = "Invalid Part Number.";
						noError = validateInput(partObject.getPN(), this.fieldPn, erms, this.vlPN);
					}
					if (noError && partObject.getSN().length() != 0)
					{
						String erms = "Invalid Sequence Number.";
						validateInput(partObject.getSN(), this.fieldSn, erms, this.vlSN);
					}
				}
				else
				{
					String erms = MFSBCBarcodeDecoder.INVALID_BARCODE_ERMS;
					IGSMessageBox.showOkMB(this, null, erms, null);
				}
			}
			else
			{
				String errorMessage = barcodeDecoder.getBCMyPartObject().getErrorString();
				if (errorMessage.trim().length() == 0)
				{
					errorMessage = MFSBCBarcodeDecoder.INVALID_BARCODE_ERMS;
				}
				IGSMessageBox.showOkMB(this, null, errorMessage, null);
			}
			this.update(this.getGraphics());
		}
	}

	/**
	 * Helper method called by {@link #parseInput()} to prepare the
	 * <code>MFSBCBarcodeDecoder</code>.
	 */
	protected void setupBarcodeDecoder()
	{
		MFSBCIndicatorValue indicatorValue = new MFSBCIndicatorValue();
		if (this.fieldRequiresPNSN)
		{
			indicatorValue.setPNRI(this.fieldPnri);
			indicatorValue.setCSNI(this.fieldCsni);
		}
		indicatorValue.setMACI("1"); //$NON-NLS-1$
		MFSBCBarcodeDecoder barcodeDecoder = MFSBCBarcodeDecoder.getInstance();
		barcodeDecoder.setMyBCIndicatorValue(indicatorValue);
		barcodeDecoder.setMyBCPartObject(new MFSBCPartObject());
	}

	/**
	 * Helper method called by {@link #parseInput()} to validate the user input
	 * and set the text of the specified <code>label</code> if the user input
	 * is valid.
	 * @param input the user input
	 * @param validInput the valid input
	 * @param erms the error message to display if the input is not valid
	 * @param label the value <code>JLabel</code> that will display the input
	 *        if it is valid
	 * @return <code>true</code> iff the specified <code>input</code> is valid
	 */
	private boolean validateInput(String input, String validInput, String erms, JLabel label)
	{
		boolean result;
		if (input.equals(validInput))
		{
			label.setText(input);
			result = true;
		}
		else
		{
			IGSMessageBox.showOkMB(this, null, erms, null);
			result = false;
		}
		return result;
	}

	/**
	 * Invoked when a key has been pressed.
	 * @param ke the <code>KeyEvent</code>
	 */
	public void keyPressed(KeyEvent ke)
	{
		final int keyCode = ke.getKeyCode();
		final MFSConfig config = MFSConfig.getInstance();  //~1a
		if (keyCode == KeyEvent.VK_ENTER)
		{
			final Object source = ke.getSource();
			if (source == this.tfInput)
			{
				this.parseInput();
				if (config.containsConfigEntry("EFFICIENCYON")) //~1a
				{
					checkForMacCollectionCompletion();
				}

			}
			else if (source instanceof JButton)
			{
				JButton button = (JButton) source;
				button.requestFocusInWindow();
				button.doClick();
			}
		}
		else if (keyCode == KeyEvent.VK_ESCAPE)
		{
			this.fieldDoneSelected = false;
			this.dispose();
		}
	}

	/**
	 * Invoked the first time a window is made visible.
	 * <p>
	 * Requests the focus for {@link #tfInput}.
	 * @param we the <code>WindowEvent</code>
	 */
	public void windowOpened(WindowEvent we)
	{
		if (we.getSource() == this)
		{
			this.tfInput.requestFocusInWindow();
		}
	}
}
