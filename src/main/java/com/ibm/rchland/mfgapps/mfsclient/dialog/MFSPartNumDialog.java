/* @ Copyright IBM Corporation 2005, 2007. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2007-02-21      34242JR  R Prechel        -Java 5 version
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
import com.ibm.rchland.mfgapps.mfscommon.MFSConfig;

/**
 * <code>MFSPartNumDialog</code> is the <code>MFSActionableDialog</code>
 * used to prompt the user for a part number and quantity.
 * @author The MFS Client Development Team
 */
public class MFSPartNumDialog
	extends MFSActionableDialog
{
	private static final long serialVersionUID = 1L;
	/** The input <code>JTextField</code>. */
	private JTextField tfInput = createTextField(MFSConstants.LARGE_TF_COLS, 0);

	/** The Part Number value <code>JLabel</code>. */
	private JLabel vlPart;

	/** The Quantity value <code>JLabel</code>. */
	private JLabel vlQuantity;

	/** The Enter <code>JButton</code>. */
	private JButton pbEnter = createButton("F2 - Enter", 'E');

	/** The Cancel <code>JButton</code>. */
	private JButton pbCancel = createButton("Cancel", 'n');

	/** Set <code>true</code> when the Enter button is selected. */
	private boolean fieldPressedEnter = false;

	/** The scanned in serial number. */
	private String fieldHiddenSN = ""; //$NON-NLS-1$

	/** The scanned in part number. */
	private String fieldPartNum = ""; //$NON-NLS-1$

	/** The quantity. */
	private String fieldQty = ""; //$NON-NLS-1$

	/** The prln flag for the barcode decoder. */
	private String fieldPrln;

	/**
	 * Constructs a new <code>MFSPartNumDialog</code>.
	 * @param parent the <code>MFSFrame</code> that caused this
	 *        <code>MFSPartNumDialog</code> to be displayed
	 * @param prln the prln flag for the barcode decoder
	 */
	public MFSPartNumDialog(MFSFrame parent, String prln)
	{
		super(parent, "Part Number");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.fieldPrln = prln;
		createLayout();
		addMyListeners();
	}

	/** Adds this dialog's <code>Component</code>s to the layout. */
	private void createLayout()
	{
		JLabel partLabel = createSmallNameLabel("Part Number");
		this.vlPart = createSmallValueLabel(partLabel);
		JLabel qtyLabel = createSmallNameLabel("Quantity");
		this.vlQuantity = createSmallValueLabel(qtyLabel);
		this.vlQuantity.setText("1");

		JPanel labelPanel = new JPanel(new GridLayout(2, 2, 5, 10));
		labelPanel.add(partLabel);
		labelPanel.add(this.vlPart);
		labelPanel.add(qtyLabel);
		labelPanel.add(this.vlQuantity);

		JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 20, 0));
		buttonPanel.add(this.pbEnter);
		buttonPanel.add(this.pbCancel);

		JPanel contentPane = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, 
				new Insets(20, 30, 20, 30), 0, 0);

		contentPane.add(this.tfInput, gbc);

		gbc.gridy++;
		gbc.insets = new Insets(0, 10, 10, 10);
		contentPane.add(labelPanel, gbc);

		gbc.gridy++;
		gbc.insets = new java.awt.Insets(10, 30, 25, 30);
		contentPane.add(buttonPanel, gbc);

		setContentPane(contentPane);
		setLabelDimensions(this.vlPart, MFSConstants.MAX_PN_CHARACTERS);
		pack();
	}

	/** Adds the listeners to this dialog's <code>Component</code>s. */
	private void addMyListeners()
	{
		this.pbCancel.addActionListener(this);
		this.pbEnter.addActionListener(this);

		this.pbCancel.addKeyListener(this);
		this.pbEnter.addKeyListener(this);
		this.tfInput.addKeyListener(this);
	}

	/** Invoked when {@link #pbEnter} is selected. */
	private void enter()
	{
		if (this.vlPart.getText().equals("")) //$NON-NLS-1$
		{
			String erms = "Invalid Part Number Entered.";
			IGSMessageBox.showOkMB(this, null, erms, null);
			this.toFront();
			this.tfInput.requestFocusInWindow();
		}
		else
		{
			this.fieldPressedEnter = true;
			this.fieldPartNum = this.vlPart.getText();
			if (this.vlQuantity.getText().equals("")) //$NON-NLS-1$
			{
				this.fieldQty = "00001"; //$NON-NLS-1$
			}
			else
			{
				String zeros = "00000"; //$NON-NLS-1$
				this.fieldQty = zeros.substring(0, 5 - this.vlQuantity.getText().length())
						+ this.vlQuantity.getText();
			}
			this.dispose();
		}
	}

	/**
	 * Returns the scanned in serial number.
	 * @return the scanned in serial number
	 */
	public String getHiddenSN()
	{
		return this.fieldHiddenSN;
	}

	/**
	 * Returns the scanned in part number.
	 * @return the scanned in part number
	 */
	public String getPartNum()
	{
		return this.fieldPartNum;
	}

	/**
	 * Returns <code>true</code> iff the Enter button was selected.
	 * @return <code>true</code> iff the Enter button was selected
	 */
	public boolean getPressedEnter()
	{
		return this.fieldPressedEnter;
	}

	/**
	 * Returns the quantity.
	 * @return the quantity
	 */
	public String getQty()
	{
		return this.fieldQty;
	}

	/** Processes the input the user entered in the <code>JTextField</code>. */
	public void recvInput()
	{
		try
		{
			boolean found = false;
			int rc = 0;

			MFSBCBarcodeDecoder barcode = MFSBCBarcodeDecoder.getInstance();
			barcode.setMyBarcodeInput(this.tfInput.getText().toUpperCase());
			barcode.setMyBCPartObject(new MFSBCPartObject());

			/* setup barcode indicator values */
			MFSBCIndicatorValue bcIndVal = new MFSBCIndicatorValue();
			bcIndVal.setPRLN(this.fieldPrln);
			bcIndVal.setPNRI("1"); //$NON-NLS-1$
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
				if (!(barcode.getBCMyPartObject().getQT().equals(""))) //$NON-NLS-1$
				{
					this.vlQuantity.setText(barcode.getBCMyPartObject().getQT());
					found = true;
				}

				this.fieldHiddenSN = ""; //$NON-NLS-1$
				if (!(barcode.getBCMyPartObject().getSN().equals(""))) //$NON-NLS-1$
				{
					this.fieldHiddenSN = barcode.getBCMyPartObject().getSN();
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
			else if (source == this.pbEnter)
			{
				enter();
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
			if (source == this.pbEnter)
			{
				this.pbEnter.requestFocusInWindow();
				this.pbEnter.doClick();
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
			this.pbEnter.requestFocusInWindow();
			this.pbEnter.doClick();
		}
		else if (keyCode == KeyEvent.VK_ESCAPE)
		{
			dispose();
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
