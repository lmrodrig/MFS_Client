/* © Copyright IBM Corporation 2005, 2007. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2007-02-22      34242JR  R Prechel        -Java 5 version
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.dialog;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

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
import com.ibm.rchland.mfgapps.mfscommon.MFSConfig;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSComm;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSFixedTransaction;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSTransaction;

/**
 * <code>MFSVrfyFabSerialDialog</code> is the <code>MFSActionableDialog</code>
 * used to call the SAP_SERCHK transaction to verify the fab serial number.
 * @author The MFS Client Development Team
 */
public class MFSVrfyFabSerialDialog
	extends MFSActionableDialog
{
	private static final long serialVersionUID = 1L;
	/** The input <code>JTextField</code>. */
	private JTextField tfInput = createTextField(MFSConstants.LARGE_TF_COLS, 0);

	/** The Part Number value <code>JLabel</code>. */
	private JLabel vlPart;

	/** The Serial Number value <code>JLabel</code>. */
	private JLabel vlSerial;

	/** The EC Number value <code>JLabel</code>. */
	private JLabel vlEC;

	/** The Verify <code>JButton</code>. */
	private JButton pbVerify = createButton("F2 - Verify", 'V');

	/** The Cancel <code>JButton</code>. */
	private JButton pbCancel = createButton("Cancel", 'n');

	/** Set <code>true</code> when the Cancel button is selected. */
	private boolean fieldPressedCancel;

	/** The <code>MFSHeaderRec</code> for the work unit. */
	private MFSHeaderRec fieldHeaderRec;

	/** The MCTL for the work unit. */
	private String fieldMctl;

	/**
	 * Constructs a new <code>MFSVrfyFabSerialDialog</code>.
	 * @param parent the <code>MFSFrame</code> that caused this
	 *        <code>MFSVrfyFabSerialDialog</code> to be displayed
	 * @param headerRec the <code>MFSHeaderRec</code> for the work unit
	 * @param mctl the MCTL for the work unit
	 */
	public MFSVrfyFabSerialDialog(MFSFrame parent, MFSHeaderRec headerRec, String mctl)
	{
		super(parent, "Verify Fab Serial");
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.fieldHeaderRec = headerRec;
		this.fieldMctl = mctl;
		createLayout();
		addMyListeners();
	}

	/** Adds this dialog's <code>Component</code>s to the layout. */
	private void createLayout()
	{
		JLabel partLabel = createNameLabel("Part Number");
		this.vlPart = createValueLabel(partLabel);

		JLabel serialLabel = createNameLabel("Serial Number");
		this.vlSerial = createValueLabel(serialLabel);

		JLabel ecLabel = createNameLabel("EC Number");
		this.vlEC = createValueLabel(ecLabel);

		JPanel labelPanel = new JPanel(new GridLayout(3, 2, 8, 10));
		labelPanel.add(partLabel);
		labelPanel.add(this.vlPart);
		labelPanel.add(serialLabel);
		labelPanel.add(this.vlSerial);
		labelPanel.add(ecLabel);
		labelPanel.add(this.vlEC);

		JPanel contentPane = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 2, 1, 1, 1,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, 
				new Insets(40, 0, 0, 0), 0, 0);

		contentPane.add(this.tfInput, gbc);

		gbc.gridy++;
		gbc.insets = new Insets(30, 25, 30, 25);
		contentPane.add(labelPanel, gbc);

		gbc.gridy++;
		gbc.gridwidth = 1;
		gbc.weighty = 0.0;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(0, 0, 10, 0);
		contentPane.add(this.pbVerify, gbc);

		gbc.gridx = 1;
		contentPane.add(this.pbCancel, gbc);

		setContentPane(contentPane);
		setButtonDimensions(this.pbVerify, this.pbCancel);
		setLabelDimensions(this.vlPart, MFSConstants.MAX_PN_CHARACTERS);
		setLabelDimensions(this.vlSerial, MFSConstants.MAX_SN_CHARACTERS);
		pack();
	}

	/** Adds the listeners to this dialog's <code>Component</code>s. */
	private void addMyListeners()
	{
		this.pbVerify.addActionListener(this);
		this.pbCancel.addActionListener(this);

		this.pbVerify.addKeyListener(this);
		this.pbCancel.addKeyListener(this);
		this.tfInput.addKeyListener(this);
	}

	/**
	 * Returns <code>true</code> iff the Cancel button was selected.
	 * @return <code>true</code> iff the Cancel button was selected
	 */
	public boolean getPressedCancel()
	{
		return this.fieldPressedCancel;
	}

	/** Processes the input the user entered in the <code>JTextField</code>. */
	private void recvInput()
	{
		try
		{
			boolean found = false;
			int rc = 0;

			MFSBCBarcodeDecoder barcode = MFSBCBarcodeDecoder.getInstance();
			barcode.setMyBarcodeInput(this.tfInput.getText());
			barcode.setMyBCPartObject(new MFSBCPartObject());

			/* setup barcode indicator values */
			MFSBCIndicatorValue bcIndVal = new MFSBCIndicatorValue();

			String sni = MFSConfig.getInstance().getConfigValue("DECODESN"); //$NON-NLS-1$
			if (!sni.equals(MFSConfig.NOT_FOUND))
			{
				bcIndVal.setCSNI(sni);
			}
			else
			{
				bcIndVal.setCSNI("1"); //$NON-NLS-1$
			}

			String eci = MFSConfig.getInstance().getConfigValue("DECODEEC"); //$NON-NLS-1$
			if (!eci.equals(MFSConfig.NOT_FOUND))
			{
				bcIndVal.setECRI(eci);
			}
			else
			{
				bcIndVal.setECRI("1"); //$NON-NLS-1$
			}

			bcIndVal.setPNRI("1"); //$NON-NLS-1$
			bcIndVal.setPRLN(this.fieldHeaderRec.getPrln());
			bcIndVal.setITEM("00000" + this.fieldHeaderRec.getPrln().substring(0, 7)); //$NON-NLS-1$

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
					this.vlPart.setText(barcode.getBCMyPartObject().getPN());
					found = true;
				}

				/* SN */
				if (!(barcode.getBCMyPartObject().getSN().equals(""))) //$NON-NLS-1$
				{
					this.vlSerial.setText(barcode.getBCMyPartObject().getSN());
					found = true;
				}

				/* EC */
				if (!(barcode.getBCMyPartObject().getEC().equals(""))) //$NON-NLS-1$
				{
					this.vlEC.setText(barcode.getBCMyPartObject().getEC());
					found = true;
				}
			}

			if ((rc != 0) || (!found && !this.tfInput.getText().equals(""))) //$NON-NLS-1$
			{
				String erms = barcode.getBCMyPartObject().getErrorString();
				if (erms.length() == 0)
				{
					erms = MFSBCBarcodeDecoder.INVALID_BARCODE_ERMS;
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

	/** Invoked when {@link #pbVerify} is selected. Calls SAP_SERCHK. */
	private void verify()
	{
		try
		{
			int rc = 0;
			String errorString = ""; //$NON-NLS-1$

			if (this.vlPart.getText().equals("")) //$NON-NLS-1$
			{
				errorString = "Please Enter a Valid Part Number.";
				rc = 10;
			}
			else if (this.vlSerial.getText().equals("")) //$NON-NLS-1$
			{
				errorString = "Please Enter a Valid Serial Number.";
				rc = 10;
			}

			if (rc == 0)
			{
				/* start the SAP_SERCHK transaction thread */
				StringBuffer data = new StringBuffer();
				data.append("SAP_SERCHK"); //$NON-NLS-1$
				data.append(this.fieldMctl);
				data.append(this.vlPart.getText()
						.concat("             ").substring(0, 12)); //$NON-NLS-1$
				data.append(this.vlSerial.getText()
						.concat("             ").substring(0, 12)); //$NON-NLS-1$

				if (this.vlEC.getText().equals("")) //$NON-NLS-1$
				{
					data.append("            "); //$NON-NLS-1$
				}
				else
				{
					data.append(this.vlEC.getText()
							.concat("             ").substring(0, 12)); //$NON-NLS-1$
				}

				MFSTransaction sap_serchk = new MFSFixedTransaction(data.toString());
				sap_serchk.setActionMessage("Retrieving Work Unit, Please Wait...");
				MFSComm.getInstance().execute(sap_serchk, this);
				rc = sap_serchk.getReturnCode();

				if (rc == 0)
				{
					String msg = "Serial Number has been Verified.";
					IGSMessageBox.showOkMB(getParentFrame(), null, msg, null);
				}
				else
				{
					errorString = sap_serchk.getErms();
				}
			}

			if (rc != 0)
			{
				IGSMessageBox.showOkMB(this, null, errorString, null);
				this.toFront();
				this.tfInput.requestFocusInWindow();
			}
			else
			{
				dispose();
			}
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(this, null, null, e);
			this.toFront();
			this.tfInput.requestFocusInWindow();
		}
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
			if (source == this.pbVerify)
			{
				verify();
			}
			else if (source == this.pbCancel)
			{
				this.fieldPressedCancel = false;
				this.dispose();
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
			if (source == this.pbVerify)
			{
				this.pbVerify.requestFocusInWindow();
				this.pbVerify.doClick();
			}
			else if (source == this.pbCancel)
			{
				this.pbCancel.requestFocusInWindow();
				this.pbCancel.doClick();
			}
			else if (source == this.tfInput)
			{
				recvInput();
			}
		}
		else if (keyCode == KeyEvent.VK_F2)
		{
			this.pbVerify.requestFocusInWindow();
			this.pbVerify.doClick();
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
	 * Requests the focus for the {@link #tfInput}.
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
