/* © Copyright IBM Corporation 2005, 2007. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2007-01-28      34242JR  R Prechel        -Java 5 version
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.dialog;

import java.awt.Cursor;
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
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSConstants;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSPrintingMethods;
import com.ibm.rchland.mfgapps.mfscommon.MFSConfig;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSComm;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSFixedTransaction;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSTransaction;

/**
 * <code>MFSRtvMctlDialog</code> is the Retrieve Work Unit Control Number
 * <code>MFSActionableDialog</code> that executes the RTV_CWUN transaction.
 * @author The MFS Client Development Team
 */
public class MFSRtvMctlDialog
	extends MFSActionableDialog
{
	private static final long serialVersionUID = 1L;
	/** The input <code>JTextField</code>. */
	private JTextField tfInput = createTextField(MFSConstants.LARGE_TF_COLS, 0);

	/** The Part Number value <code>JLabel</code>. */
	protected JLabel vlPN;

	/** The Sequence Number value <code>JLabel</code>. */
	protected JLabel vlSN;

	/** The retrieve <code>JButton</code>. */
	private JButton pbRetrieve = createButton("F2 - Retrieve");

	/** The cancel <code>JButton</code>. */
	private JButton pbCancel = createButton("Cancel", 'n');

	/**
	 * Constructs a new <code>MFSRtvMctlDialog</code>.
	 * @param parent the <code>MFSFrame</code> that caused this
	 *        <code>MFSRtvMctlDialog</code> to be displayed
	 */
	public MFSRtvMctlDialog(MFSFrame parent)
	{
		super(parent, "Retrieve Work Unit Control Number");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		createLayout();
		addMyListeners();
	}

	/** Adds this dialog's <code>Component</code>s to the layout. */
	private void createLayout()
	{
		JLabel pnLabel = createNameLabel("Part Number");
		JLabel snLabel = createNameLabel("Sequence Number");

		this.vlPN = createValueLabel(pnLabel);
		this.vlSN = createValueLabel(snLabel);

		JPanel labelPanel = new JPanel(new GridLayout(2, 2, 15, 20));
		labelPanel.add(pnLabel);
		labelPanel.add(this.vlPN);
		labelPanel.add(snLabel);
		labelPanel.add(this.vlSN);

		JPanel contentPane = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 2, 1, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, 
				new Insets(40, 15, 10, 15), 0, 0);

		contentPane.add(this.tfInput, gbc);

		gbc.gridy = 1;
		gbc.insets = new Insets(20, 15, 10, 15);
		contentPane.add(labelPanel, gbc);

		gbc.gridy = 2;
		gbc.gridwidth = 1;
		gbc.insets = new Insets(20, 20, 10, 20);

		contentPane.add(this.pbRetrieve, gbc);

		gbc.gridx = 1;
		contentPane.add(this.pbCancel, gbc);

		setContentPane(contentPane);
		setLabelDimensions(this.vlPN, MFSConstants.MAX_PN_CHARACTERS);
		setLabelDimensions(this.vlSN, MFSConstants.MAX_SN_CHARACTERS);
		setButtonDimensions(this.pbRetrieve, this.pbCancel);
		pack();
	}

	/** Adds the listeners to this dialog's <code>Component</code>s. */
	private void addMyListeners()
	{
		this.pbRetrieve.addActionListener(this);
		this.pbCancel.addActionListener(this);

		this.pbRetrieve.addKeyListener(this);
		this.pbCancel.addKeyListener(this);
		this.tfInput.addKeyListener(this);
	}

	/** Clears the text components and requests the focus for the text field. */
	public void clear()
	{
		this.vlPN.setText(""); //$NON-NLS-1$
		this.vlSN.setText(""); //$NON-NLS-1$
		this.tfInput.setText(""); //$NON-NLS-1$
		this.tfInput.requestFocusInWindow();
	}

	/** Processes the input the user entered in the <code>JTextField</code>. */
	public void recvInput()
	{
		try
		{
			/* Perform the recvInput method. */
			boolean found = false;
			int rc = 0;

			MFSBCBarcodeDecoder barcode = MFSBCBarcodeDecoder.getInstance();
			barcode.setMyBarcodeInput(this.tfInput.getText().toUpperCase());
			barcode.setMyBCPartObject(new MFSBCPartObject());

			/* setup barcode indicator values */
			MFSBCIndicatorValue bcIndVal = new MFSBCIndicatorValue();
			bcIndVal.setPNRI("1"); //$NON-NLS-1$

			/* setup SN barcode indicator values */
			String sni = MFSConfig.getInstance().getConfigValue("DECODESN"); //$NON-NLS-1$
			if (!sni.equals(MFSConfig.NOT_FOUND))
			{
				bcIndVal.setCSNI(sni);
			}
			else
			{
				bcIndVal.setCSNI("1"); //$NON-NLS-1$
			}
			barcode.setMyBCIndicatorValue(bcIndVal);

			if (barcode.decodeBarcodeFor(this) == false)
			{
				rc = 10;
				barcode.getBCMyPartObject().setErrorString(
						MFSBCBarcodeDecoder.DECODE_TIMED_OUT_ERMS);
			}
			else
			{
				rc = barcode.getBCMyPartObject().getRC();
			}

			if (rc == 0 && !found)
			{
				/* PN */
				if (!(barcode.getBCMyPartObject().getPN().equals(""))) //$NON-NLS-1$
				{
					this.vlPN.setText(barcode.getBCMyPartObject().getPN());
					found = true;
				}

				/* SN */
				if (!(barcode.getBCMyPartObject().getSN().equals(""))) //$NON-NLS-1$
				{
					this.vlSN.setText(barcode.getBCMyPartObject().getSN());
					found = true;
				}
			}

			if ((rc != 0) || (!found && !this.tfInput.getText().equals(""))) //$NON-NLS-1$
			{
				String erms = barcode.getBCMyPartObject().getErrorString();
				if (erms.trim().length() == 0)
				{
					erms = MFSBCBarcodeDecoder.INVALID_BARCODE_ERMS;
				}
				IGSMessageBox.showOkMB(this, null, erms, null);
			}
		}
		catch (Exception e)
		{
			getParentFrame().setCursor(Cursor.getDefaultCursor());
			IGSMessageBox.showOkMB(this, null, null, e);
		}

		this.toFront();
		this.tfInput.setText(""); //$NON-NLS-1$
	}

	/** Executes the retrieve transaction and prints the appropriate labels. */
	public void retrieve()
	{
		try
		{
			StringBuffer input = new StringBuffer();
			input.append("RTV_CWUN  "); //$NON-NLS-1$
			input.append(this.vlPN.getText().trim());
			input.append(this.vlSN.getText().trim());

			MFSTransaction rtv_cwun = new MFSFixedTransaction(input.toString());
			rtv_cwun.setActionMessage("Retrieving Work Unit, Please Wait...");
			MFSComm.getInstance().execute(rtv_cwun, this);

			if (rtv_cwun.getReturnCode() == 0)
			{
				if (MFSConfig.getInstance().containsConfigEntry("WUIPID")) //$NON-NLS-1$
				{
					MFSPrintingMethods.wuipid(rtv_cwun.getOutput(), 1, getParentFrame());
				}
				else
				{
					MFSPrintingMethods.rochwu(rtv_cwun.getOutput(),
							"       ", "    ", 1, getParentFrame()); //$NON-NLS-1$ //$NON-NLS-2$
				}
			}
			else
			{
				IGSMessageBox.showOkMB(this, null, rtv_cwun.getErms(), null);
				this.toFront();
			}
		}
		catch (Exception e)
		{
			this.setCursor(java.awt.Cursor.getDefaultCursor());
			IGSMessageBox.showOkMB(this, null, null, e);
			this.toFront();
		}
		clear();
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
			if (source == this.pbRetrieve)
			{
				retrieve();
			}
			else if (source == this.pbCancel)
			{
				dispose();
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
			if (source == this.pbRetrieve)
			{
				this.pbRetrieve.requestFocusInWindow();
				this.pbRetrieve.doClick();
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
			this.pbRetrieve.requestFocusInWindow();
			this.pbRetrieve.doClick();
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
