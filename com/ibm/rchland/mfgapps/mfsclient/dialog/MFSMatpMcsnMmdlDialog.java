/* © Copyright IBM Corporation 2003, 2007. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2007-01-28      34242JR  R Prechel        -Java 5 version
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
import com.ibm.rchland.mfgapps.mfscommon.MFSConfig;

/**
 * <code>MFSMatpMcsnMmdlDialog</code> is the <code>MFSActionableDialog</code>
 * used to print a WWID label.
 * @author The MFS Client Development Team
 */
public class MFSMatpMcsnMmdlDialog
	extends MFSActionableDialog
{
	private static final long serialVersionUID = 1L;
	/** The input <code>JTextField</code>. */
	private JTextField tfInput = createTextField(MFSConstants.LARGE_TF_COLS, 22);

	/** The Machine Type value <code>JLabel</code>. */
	private JLabel vlMatp;

	/** The Model value <code>JLabel</code>. */
	private JLabel vlMmdl;

	/** The Plant Code value <code>JLabel</code>. */
	private JLabel vlPC;

	/** The Machine Serial value <code>JLabel</code>. */
	private JLabel vlMcsn;

	/** The Print <code>JButton</code>. */
	private JButton pbPrint = createButton("PRINT", 'P');

	/** The Exit <code>JButton</code>. */
	private JButton pbExit = createButton("EXIT", 'x');

	/**
	 * Constructs a new <code>MFSMatpMcsnMmdlDialog</code>.
	 * @param parent the <code>MFSFrame</code> that caused this
	 *        <code>MFSMatpMcsnMmdlDialog</code> to be displayed
	 */
	public MFSMatpMcsnMmdlDialog(MFSFrame parent)
	{
		super(parent, "Enter or Scan in 1S Barcode");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		createLayout();
		addMyListeners();
	}

	/** Adds this dialog's <code>Component</code>s to the layout. */
	private void createLayout()
	{
		JLabel matpLabel = createSmallNameLabel("Machine Type");
		JLabel mmdlLabel = createSmallNameLabel("Model");
		JLabel pcLabel = createSmallNameLabel("Plant Code");
		JLabel mcsnLabel = createSmallNameLabel("Machine Serial");

		this.vlMatp = createSmallValueLabel(matpLabel);
		this.vlMmdl = createSmallValueLabel(mmdlLabel);
		this.vlPC = createSmallValueLabel(pcLabel);
		this.vlMcsn = createSmallValueLabel(mcsnLabel);

		this.pbPrint.setMargin(new Insets(2, 28, 2, 28));
		this.pbExit.setMargin(new Insets(2, 28, 2, 28));
		JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 50, 0));
		buttonPanel.add(this.pbPrint);
		buttonPanel.add(this.pbExit);

		JPanel contentPane = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, 
				new Insets(30, 35, 15, 35), 0, 0);

		contentPane.add(this.tfInput, gbc);

		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.EAST;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(4, 30, 4, 4);
		contentPane.add(matpLabel, gbc);

		gbc.gridy = 2;
		contentPane.add(mmdlLabel, gbc);

		gbc.gridy = 3;
		contentPane.add(pcLabel, gbc);

		gbc.gridy = 4;
		contentPane.add(mcsnLabel, gbc);

		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.weightx = 1;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(4, 4, 4, 4);
		contentPane.add(this.vlMatp, gbc);

		gbc.gridy = 2;
		contentPane.add(this.vlMmdl, gbc);

		gbc.gridy = 3;
		contentPane.add(this.vlPC, gbc);

		gbc.gridy = 4;
		contentPane.add(this.vlMcsn, gbc);

		gbc.gridx = 0;
		gbc.gridy = 5;
		gbc.gridwidth = 2;
		gbc.weightx = 0;
		gbc.weighty = 1;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(30, 30, 20, 30);
		contentPane.add(buttonPanel, gbc);

		setContentPane(contentPane);
		pack();
	}

	/** Adds the listeners to this dialog's <code>Component</code>s. */
	private void addMyListeners()
	{
		this.pbPrint.addActionListener(this);
		this.pbExit.addActionListener(this);

		this.pbPrint.addKeyListener(this);
		this.pbExit.addKeyListener(this);
		this.tfInput.addKeyListener(this);
	}

	/** Invoked when {@link #pbPrint} is selected. */
	private void printLabel()
	{
		if (this.vlMcsn.getText().trim().equals("")) //$NON-NLS-1$
		{
			String erms = "Invalid Machine Serial Number!";
			IGSMessageBox.showOkMB(this, null, erms, null);
			this.tfInput.requestFocusInWindow();
			return;
		}
		else if (this.vlMatp.getText().trim().equals("")) //$NON-NLS-1$
		{
			String erms = "Invalid Machine Type!";
			IGSMessageBox.showOkMB(this, null, erms, null);
			this.tfInput.requestFocusInWindow();
			return;
		}
		else if (this.vlPC.getText().trim().equals("")) //$NON-NLS-1$
		{
			String erms = "Invalid Plant Code!";
			IGSMessageBox.showOkMB(this, null, erms, null);
			this.tfInput.requestFocusInWindow();
			return;
		}
		else if (this.vlMmdl.getText().trim().equals("")) //$NON-NLS-1$
		{
			String erms = "Invalid Model Number!";
			IGSMessageBox.showOkMB(this, null, erms, null);
			this.tfInput.requestFocusInWindow();
			return;
		}
		else
		{
			// Verify the length of the decoded MCSN (ASSIGNWWID requires last 5 chars of MCSN)
			String mcsnprint = this.vlMcsn.getText().trim().toUpperCase();
			if (mcsnprint.length() >= 5)
			{
				// Grab the last 5 chars of the MCSN if at least 5 long
				mcsnprint = mcsnprint.substring(mcsnprint.length() - 5);
			}
			else
			{
				// Else append zeros so that a null pointer error isn't thrown
				// when substring is called.
				mcsnprint = (mcsnprint + "00000").substring(0, 5);
			}

			MFSPrintingMethods.wwid(
					this.vlMatp.getText().trim().toUpperCase().concat("    ").substring(0, 4), //$NON-NLS-1$
					this.vlMmdl.getText().trim().toUpperCase().concat("    ").substring(0, 4), //$NON-NLS-1$
					this.vlPC.getText().trim().toUpperCase().concat("  ").substring(0, 2), //$NON-NLS-1$
					mcsnprint, MFSConfig.getInstance().get8CharUser(), "N", 1, getParentFrame()); //$NON-NLS-1$
		}
	}

	/** Processes the input the user entered in the <code>JTextField</code>. */
	private void recvInput()
	{
		try
		{
			boolean found = false;
			String errorString = MFSBCBarcodeDecoder.INVALID_BARCODE_ERMS;
			int rc = 0;

			MFSBCBarcodeDecoder barcode = MFSBCBarcodeDecoder.getInstance();
			barcode.setMyBarcodeInput(this.tfInput.getText().toUpperCase());
			barcode.setMyBCPartObject(new MFSBCPartObject());

			/* setup barcode indicator values */
			MFSBCIndicatorValue bcIndVal = new MFSBCIndicatorValue();

			bcIndVal.setCMTI("1"); //$NON-NLS-1$
			bcIndVal.setMMDL("1"); //$NON-NLS-1$

			barcode.setMyBCIndicatorValue(bcIndVal);

			if (barcode.decodeBarcodeFor(this) == false)
			{
				rc = 10;
				errorString = MFSBCBarcodeDecoder.DECODE_TIMED_OUT_ERMS;
			}
			else
			{
				rc = barcode.getBCMyPartObject().getRC();
			}

			if (rc == 0 && !found)
			{
				//	If the M/T was decoded, then set it.
				if (!barcode.getBCMyPartObject().getMTC().equals("")) //$NON-NLS-1$
				{
					this.vlMatp.setText(barcode.getBCMyPartObject().getMTC());
				}
				//	If the Model is decoded, then set it.
				if (!(barcode.getBCMyPartObject().getMD().equals(""))) //$NON-NLS-1$
				{
					this.vlMmdl.setText(barcode.getBCMyPartObject().getMD());
				}
				//	If the Machine Serial is decoded, then set it.
				if (!(barcode.getBCMyPartObject().getMSN().equals(""))) //$NON-NLS-1$
				{
					// Set the machine serial
					String mcsn = barcode.getBCMyPartObject().getMSN();
					this.vlMcsn.setText(mcsn.substring(mcsn.length() - 7));
					this.vlPC.setText(mcsn.substring(0, 2));
				}
				//If the Plant Code is decoded, then set it.
				if (!(barcode.getBCMyPartObject().getPC().equals(""))) //$NON-NLS-1$
				{
					// Set the Plant Code
					this.vlPC.setText(barcode.getBCMyPartObject().getPC());
				}
				// If all values were decoded, then print the label automatically
				if (!this.vlMcsn.getText().equals("") //$NON-NLS-1$
						&& !this.vlMatp.getText().equals("") //$NON-NLS-1$
						&& !this.vlPC.getText().equals("") //$NON-NLS-1$
						&& !this.vlMmdl.getText().equals("")) //$NON-NLS-1$
				{
					printLabel();
				}

			} /* No non-responsive server error. */
			else
			{
				IGSMessageBox.showOkMB(this, null, errorString, null);
			}
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(this, null, null, e);
		}

		this.toFront();
		this.tfInput.setText(""); //$NON-NLS-1$
		this.update(getGraphics());
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
			if (source == this.pbExit)
			{
				dispose();
			}
			else if (source == this.pbPrint)
			{
				printLabel();
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
			if (source == this.pbExit)
			{
				this.pbExit.requestFocusInWindow();
				this.pbExit.doClick();
			}
			else if (source == this.pbPrint)
			{
				this.pbPrint.requestFocusInWindow();
				this.pbPrint.doClick();
			}
			else if (source == this.tfInput)
			{
				recvInput();
			}
		}
		else if (keyCode == KeyEvent.VK_ESCAPE)
		{
			this.pbExit.requestFocusInWindow();
			this.pbExit.doClick();
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
