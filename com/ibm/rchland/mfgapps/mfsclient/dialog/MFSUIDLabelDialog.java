/* © Copyright IBM Corporation 2007. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2007-02-26      37452MZ  Toribio H.       -Initial version
 * 2007-03-13      34242JR  R Prechel        -Java 5 version
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
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSConstants;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSPrintingMethods;

/**
 * <code>MFSUIDLabelDialog</code> is the <code>MFSActionableDialog</code>
 * used to collect input for and call {@link MFSPrintingMethods#UIDLbl}.
 * @author The MFS Client Development Team
 */
public class MFSUIDLabelDialog
	extends MFSActionableDialog
{
	private static final long serialVersionUID = 1L;
	/** The maximum number of characters allowed in {@link #tfMATP}. */
	private static final int MAX_MATP_LENGTH = 32;

	/** The maximum number of characters allowed in {@link #tfMCSN}. */
	private static final int MAX_MCSN_LENGTH = 30;

	/** The minimum number of characters in a Machine Type. */
	private static final int MIN_MATP_LENGTH = 4;

	/** The minimum number of characters in a Machine Serial Number. */
	private static final int MIN_MCSN_LENGTH = 7;

	/** The input <code>JTextField</code>. */
	private JTextField tfInput = createTextField(MFSConstants.LARGE_TF_COLS, 60);

	/** The Machine Type value <code>JTextField</code>. */
	private JTextField tfMATP = null;

	/** The Machine Serial Number value <code>JTextField</code>. */
	private JTextField tfMCSN = null;

	/** The Print <code>JButton</code>. */
	private JButton pbPrint = createButton("Print", 'P');

	/** The Cancel <code>JButton</code>. */
	private JButton pbCancel = createButton("Cancel", 'n');

	/**
	 * Constructs a new <code>MFSUIDLabelDialog</code>.
	 * @param parent the <code>MFSFrame</code> that caused this
	 *        <code>MFSUIDLabelDialog</code> to be displayed
	 */
	public MFSUIDLabelDialog(MFSFrame parent)
	{
		super(parent, "Enter MATP and MCSN or Scan in 1S Barcode");
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		createLayout();
		addMyListeners();
	}

	/** Adds this dialog's <code>Component</code>s to the layout. */
	private void createLayout()
	{
		JLabel matpLabel = createSmallNameLabel("Machine Type:");
		this.tfMATP = createTextField(MFSConstants.LARGE_TF_COLS, MAX_MATP_LENGTH, matpLabel);
		JLabel mcsnLabel = createSmallNameLabel("Machine Serial Number:");
		this.tfMCSN = createTextField(MFSConstants.LARGE_TF_COLS, MAX_MCSN_LENGTH, mcsnLabel);

		JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 30, 0));
		buttonPanel.add(this.pbPrint);
		buttonPanel.add(this.pbCancel);

		JPanel contentPane = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 2, 1, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, 
				new Insets(10, 0, 5, 0), 0, 0);

		contentPane.add(this.tfInput, gbc);

		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.EAST;
		gbc.insets = new Insets(10, 40, 0, 4);

		contentPane.add(matpLabel, gbc);

		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(10, 4, 0, 20);

		contentPane.add(this.tfMATP, gbc);

		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.anchor = GridBagConstraints.EAST;
		gbc.insets = new Insets(10, 40, 0, 4);
		contentPane.add(mcsnLabel, gbc);

		gbc.gridx = 1;
		gbc.gridy = 2;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(10, 4, 0, 20);

		contentPane.add(this.tfMCSN, gbc);

		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.insets = new Insets(10, 0, 10, 0);
		contentPane.add(buttonPanel, gbc);

		setContentPane(contentPane);
		pack();
	}

	/** Adds the listeners to this dialog's <code>Component</code>s. */
	private void addMyListeners()
	{
		this.pbPrint.addActionListener(this);
		this.pbCancel.addActionListener(this);

		this.tfInput.addKeyListener(this);
		this.tfMATP.addKeyListener(this);
		this.tfMCSN.addKeyListener(this);
		this.pbPrint.addKeyListener(this);
		this.pbCancel.addKeyListener(this);
	}

	/** Invoked when {@link #pbPrint} is selected. */
	private void print()
	{
		if (this.tfInput.getText().trim().length() != 0)
		{
			recvInput();
			this.tfInput.requestFocusInWindow();
		}
		else
		{
			final String matp = this.tfMATP.getText().trim();
			final String mcsn = this.tfMCSN.getText().trim();
			final MFSBCBarcodeDecoder barcode = MFSBCBarcodeDecoder.getInstance();

			//Check to make sure values are wanded in correctly
			if (matp.length() < MIN_MATP_LENGTH)
			{
				StringBuffer erms = new StringBuffer();
				erms.append("MATP needs at least ");
				erms.append(MIN_MATP_LENGTH);
				erms.append(" chars. Please Fix.");
				IGSMessageBox.showOkMB(this, null, erms.toString(), null);
			}
			else if (mcsn.length() < MIN_MCSN_LENGTH)
			{
				StringBuffer erms = new StringBuffer();
				erms.append("MCSN needs at least ");
				erms.append(MIN_MCSN_LENGTH);
				erms.append(" chars. Please Fix.");
				IGSMessageBox.showOkMB(this, null, erms.toString(), null);
			}
			else if ("GOOD VALUE".equals(barcode.validateDecodedValue(matp, "MATP")) == false) //$NON-NLS-1$ //$NON-NLS-2$
			{
				String erms = "MATP has invalid chars. Please Fix.";
				IGSMessageBox.showOkMB(this, null, erms, null);
			}
			else if ("GOOD VALUE".equals(barcode.validateDecodedValue(mcsn, "MCSN")) == false) //$NON-NLS-1$ //$NON-NLS-2$
			{
				String erms = "MCSN has invalid chars. Please Fix.";
				IGSMessageBox.showOkMB(this, null, erms, null);
			}
			else
			{
				try
				{
					MFSPrintingMethods.UIDLbl(matp, mcsn, 1, getParentFrame());
				}
				catch (Exception e)
				{
					IGSMessageBox.showOkMB(this, null, null, e);
				}

				this.tfInput.setText(""); //$NON-NLS-1$
				this.tfMATP.setText(""); //$NON-NLS-1$
				this.tfMCSN.setText(""); //$NON-NLS-1$
				this.tfInput.requestFocusInWindow();
			}
		}
	}

	/** Processes the input the user entered in the <code>JTextField</code>. */
	private void recvInput()
	{
		try
		{
			if (this.tfInput.getText().trim().length() != 0)
			{
				MFSBCBarcodeDecoder barcode = MFSBCBarcodeDecoder.getInstance();
				barcode.setMyBarcodeInput(this.tfInput.getText().trim().toUpperCase());
				barcode.setMyBCPartObject(new MFSBCPartObject());

				/* setup barcode indicator values */
				MFSBCIndicatorValue bcIndVal = new MFSBCIndicatorValue();
				bcIndVal.setCMTI("1"); //$NON-NLS-1$
				bcIndVal.setMATP("1"); //$NON-NLS-1$
				bcIndVal.setMCSN("1"); //$NON-NLS-1$
				barcode.setMyBCIndicatorValue(bcIndVal);

				if (barcode.decodeBarcodeFor(this))
				{
					if (0 == barcode.getBCMyPartObject().getRC())
					{
						this.tfMATP.setText(barcode.getBCMyPartObject().getMTC());
						this.tfMCSN.setText(barcode.getBCMyPartObject().getMSN());
					}
					else
					{
						StringBuffer erms = new StringBuffer();
						erms.append(MFSBCBarcodeDecoder.INVALID_BARCODE_ERMS);
						erms.append(": MTYP/MSNM");
						IGSMessageBox.showOkMB(this, null, erms.toString(), null);
					}
				}
				else
				{
					IGSMessageBox.showOkMB(this, null,
							MFSBCBarcodeDecoder.DECODE_TIMED_OUT_ERMS, null);
				}
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
		try
		{
			final Object source = ae.getSource();
			if (source == this.pbPrint)
			{
				print();
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
			if (source == this.pbCancel)
			{
				this.pbCancel.requestFocusInWindow();
				this.pbCancel.doClick();
			}
			else
			{
				this.pbPrint.requestFocusInWindow();
				this.pbPrint.doClick();
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
