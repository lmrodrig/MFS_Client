/* © Copyright IBM Corporation 2005, 2007. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2007-02-20      34242JR  R Prechel        -Java 5 version
 ******************************************************************************/
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

import com.ibm.rchland.mfgapps.client.utils.messagebox.IGSMessageBox;
import com.ibm.rchland.mfgapps.mfsclient.MFSActionableDialog;
import com.ibm.rchland.mfgapps.mfsclient.MFSFrame;
import com.ibm.rchland.mfgapps.mfsclient.barcode.MFSBCBarcodeDecoder;
import com.ibm.rchland.mfgapps.mfsclient.barcode.MFSBCIndicatorValue;
import com.ibm.rchland.mfgapps.mfsclient.barcode.MFSBCPartObject;
import com.ibm.rchland.mfgapps.mfsclient.struct.MFSHeaderRec;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSConstants;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSComm;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSFixedTransaction;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSTransaction;

/**
 * <code>MFSCFReportDialog</code> is the <code>MFSActionableDialog</code>
 * used to validate a CF Report Control Number.
 * @author The MFS Client Development Team
 */
public class MFSCFReportDialog
	extends MFSActionableDialog
{
	private static final long serialVersionUID = 1L;
	/** The input <code>JTextField</code>. */
	private JTextField tfInput = createTextField(MFSConstants.LARGE_TF_COLS, 0);

	/** The value <code>JLabel</code> for the scanned in CF Report Number. */
	private JLabel vlCfrn = null;

	/** The validate <code>JButton</code>. */
	private JButton pbValidate = createButton("F2 = Validate");

	/** The cancel <code>JButton</code>. */
	private JButton pbCancel = createButton("Esc = Cancel");

	/** Set <code>true</code> if CF Report Number validation passed. */
	private boolean fieldPassedValidation = false;

	/** The <code>MFSHeaderRec</code> for which logging is being performed. */
	private MFSHeaderRec fieldHeadRec;

	/**
	 * Constructs a new <code>MFSCFReportDialog</code>.
	 * @param parent the <code>MFSFrame</code> that caused this
	 *        <code>MFSCFReportDialog</code> to be displayed
	 * @param rec the <code>MFSHeaderRec</code> for which logging is being performed
	 */
	public MFSCFReportDialog(MFSFrame parent, MFSHeaderRec rec)
	{
		super(parent, "CF Report Validation");
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.fieldHeadRec = rec;
		createLayout();
		addMyListeners();
	}

	/** Adds this dialog's <code>Component</code>s to the layout. */
	private void createLayout()
	{
		JLabel cfrnNameLabel = createNameLabel("CF Report Number");
		this.vlCfrn = createValueLabel(cfrnNameLabel);

		JPanel labelPanel = new JPanel(new GridLayout(1, 2, 10, 0));
		labelPanel.add(cfrnNameLabel);
		labelPanel.add(this.vlCfrn);

		JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 30, 0));
		buttonPanel.add(this.pbValidate);
		buttonPanel.add(this.pbCancel);

		JPanel contentPaneCenter = new JPanel(new GridBagLayout());
		contentPaneCenter.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));

		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.NONE,
				new Insets(4, 4, 4, 4), 0, 0);
		contentPaneCenter.add(this.tfInput, gbc);

		gbc.gridy = 1;
		gbc.weightx = 0.0;
		gbc.weighty = 1.0;
		contentPaneCenter.add(labelPanel, gbc);

		gbc.gridy = 2;
		gbc.weightx = 1.0;
		gbc.insets = new Insets(20, 4, 4, 4);
		contentPaneCenter.add(buttonPanel, gbc);

		setContentPane(contentPaneCenter);
		pack();
	}

	/** Adds the listeners to this dialog's <code>Component</code>s. */
	private void addMyListeners()
	{
		this.pbCancel.addActionListener(this);
		this.pbValidate.addActionListener(this);
		this.pbCancel.addKeyListener(this);
		this.pbValidate.addKeyListener(this);
		this.tfInput.addKeyListener(this);
	}

	/**
	 * Returns <code>true</code> iff CF Report Number validation passed.
	 * @return <code>true</code> iff CF Report Number validation passed
	 */
	public boolean getPassedValidation()
	{
		return this.fieldPassedValidation;
	}

	/** Handles the selection of the validate button. */
	public void cfValidate()
	{
		try
		{
			this.fieldPassedValidation = false;
			if (this.vlCfrn.getText().length() == 0)
			{
				String errorString = "Please wand in the CF Report Control Number";
				IGSMessageBox.showOkMB(this, null, errorString, null);

				this.toFront();
				this.tfInput.setText(""); //$NON-NLS-1$
				this.tfInput.requestFocusInWindow();
			}
			else
			{
				String cfrn = this.vlCfrn.getText() + "                    "; //$NON-NLS-1$
				StringBuffer data = new StringBuffer();
				data.append("VLDT_CFRPT"); //$NON-NLS-1$
				data.append(this.fieldHeadRec.getMfgn());
				data.append(this.fieldHeadRec.getIdss());
				data.append(this.fieldHeadRec.getOrno());
				data.append(cfrn.substring(0, 20));

				MFSTransaction vldt_cfrpt = new MFSFixedTransaction(data.toString());
				vldt_cfrpt.setActionMessage("Validating CF Report Control Number, Please Wait...");
				MFSComm.getInstance().execute(vldt_cfrpt, this);

				if (vldt_cfrpt.getReturnCode() == 0)
				{
					this.fieldPassedValidation = true;
					dispose();
				}
				else
				{
					IGSMessageBox.showOkMB(this, null, vldt_cfrpt.getErms(), null);

					this.toFront();
					this.tfInput.setText(""); //$NON-NLS-1$
					this.tfInput.requestFocusInWindow();
				}
			}
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(this, null, null, e);

			this.toFront();
			this.tfInput.setText(""); //$NON-NLS-1$
			this.tfInput.requestFocusInWindow();
		}
	}

	/** Processes the user input. */
	public void recvInput()
	{
		try
		{
			int rc = 0;
			String errorString = MFSBCBarcodeDecoder.INVALID_BARCODE_ERMS;

			MFSBCBarcodeDecoder decoder = MFSBCBarcodeDecoder.getInstance();
			decoder.setMyBarcodeInput(this.tfInput.getText().toUpperCase());
			decoder.setMyBCPartObject(new MFSBCPartObject());
			/* setup barcode indicator values */
			MFSBCIndicatorValue bcIndVal = new MFSBCIndicatorValue();
			bcIndVal.setPNRI("0"); //$NON-NLS-1$
			bcIndVal.setECRI("0"); //$NON-NLS-1$
			bcIndVal.setCSNI("0"); //$NON-NLS-1$
			bcIndVal.setCCAI("0"); //$NON-NLS-1$
			bcIndVal.setCMTI("0"); //$NON-NLS-1$
			bcIndVal.setPRLN("        "); //$NON-NLS-1$
			bcIndVal.setITEM("            "); //$NON-NLS-1$
			bcIndVal.setCOOI("0"); //$NON-NLS-1$

			decoder.setMyBCIndicatorValue(bcIndVal);

			if (this.tfInput.getText().length() <= 3)
			{
				errorString = "You must wand in a valid CF Report Control Number";
				rc = 1;
			}
			// something was wanded in, so let's check if the AI code matches
			// the 12S
			else
			{
				if ((!this.tfInput.getText().substring(0, 3).equals("12S")) //$NON-NLS-1$
						&& (this.tfInput.getText().length() == 8))
				{
					this.vlCfrn.setText(this.tfInput.getText());
				}
				else
				{
					if (decoder.decodeBarcodeFor(this) == false)
					{
						rc = 10;
						decoder.getBCMyPartObject().setErrorString(
								MFSBCBarcodeDecoder.DECODE_TIMED_OUT_ERMS);
					}
					else
					{
						rc = decoder.getBCMyPartObject().getRC();
					}

					if (rc == 0)
					{
						this.vlCfrn.setText(decoder.getBCMyPartObject().getDEF());
					}
				} // end decode the barcode
			}

			if (rc != 0)
			{
				String erms = decoder.getBCMyPartObject().getErrorString();
				if (erms.equals("")) //$NON-NLS-1$
				{
					erms = errorString;
				}
				IGSMessageBox.showOkMB(this, null, erms, null);
			}
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(this, null, null, e);
		}
		this.toFront();
		this.tfInput.setText(""); //$NON-NLS-1$
	}

	/**
	 * Invoked when an action occurs.
	 * @param ae the <code>ActionEvent</code>
	 */
	public void actionPerformed(ActionEvent ae)
	{
		final Object source = ae.getSource();
		if (source == this.pbCancel)
		{
			this.dispose();
		}
		if (source == this.pbValidate)
		{
			this.cfValidate();
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
			if (source == this.pbValidate)
			{
				this.pbValidate.requestFocusInWindow();
				this.pbValidate.doClick();
			}
			else if (source == this.tfInput)
			{
				this.recvInput();
			}
		}
		else if (keyCode == KeyEvent.VK_F2)
		{
			this.pbValidate.requestFocusInWindow();
			this.pbValidate.doClick();
		}
		else if (keyCode == KeyEvent.VK_ESCAPE)
		{
			this.pbCancel.requestFocusInWindow();
			this.pbCancel.doClick();
		}
	}

	/**
	 * Invoked the first time a window is made visible.
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
