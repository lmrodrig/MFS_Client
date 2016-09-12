/* @ Copyright IBM Corporation 2005, 2010. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2007-02-22      34242JR  R Prechel        -Java 5 version
 * 2007-04-02   ~1 38166JM  R Prechel        -Add setLocationRelativeTo for dialog
 * 2010-03-09   ~2 47596MZ  D Kloepping      -EFFICIENCYON config present, do not lookup suspension
 * 											   codes default value. 
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
import com.ibm.rchland.mfgapps.mfsclient.barcode.MFSBCBarcodeDecoder;
import com.ibm.rchland.mfgapps.mfsclient.barcode.MFSBCIndicatorValue;
import com.ibm.rchland.mfgapps.mfsclient.barcode.MFSBCPartObject;
import com.ibm.rchland.mfgapps.mfsclient.panel.MFSDirectWorkPanel;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSConstants;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSComm;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSFixedTransaction;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSTransaction;

/**
 * <code>MFSSerialNumDialog</code> is the <code>MFSActionableDialog</code>
 * used to prompt the user for a part number and serial number when a work unit
 * with a pfsn value of "Y" is started.
 * @author The MFS Client Development Team
 */
public class MFSSerialNumDialog
	extends MFSActionableDialog
{
	private static final long serialVersionUID = 1L;
	/** The input text field. */
	private JTextField tfInput = createTextField(MFSConstants.LARGE_TF_COLS, 0);

	/** The value <code>JLabel</code> used to display the part number. */
	private JLabel vlPart = null;

	/** The value <code>JLabel</code> used to display the serial number. */
	private JLabel vlSerial = null;

	/** The validate (F2) <code>JButton</code>. */
	private JButton pbValidate = createButton("F2 = Validate");

	/** The suspend (F7) button. */
	private JButton pbSuspend = createButton("F7 = Suspend");

	/** Stores a reference to the <code>MFSDirectWorkPanel</code>. */
	/*
	private MFSDirectWorkPanel fieldDirWork;
*/
	/** Stores the work unit control number. */
	private String fieldMctl;

	/** Stores the scanned in serial number. */
	private String fieldSerialNum = ""; //$NON-NLS-1$

	/** Stores the scanned in part number. */
	private String fieldPartNum = ""; //$NON-NLS-1$

	/** Stores the prln barcode decode flag. */
	private String fieldPrln = ""; //$NON-NLS-1$
	
	/** Set <code>true</code> if the suspend button is pressed. */
	private boolean fieldPressedSuspend = false;

	/**
	 * Constructs a new <code>MFSSerialNumDialog</code>.
	 * @param dirWork the <code>MFSDirectWorkPanel</code>
	 * @param mctl the work unit control number
	 */
	public MFSSerialNumDialog(MFSDirectWorkPanel dirWork, String mctl)
	{
		super(dirWork.getParentFrame(), "Work Unit Serial Number");
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.fieldMctl = mctl;
		/* this.fieldDirWork = dirWork; */
		createLayout();
		addMyListeners();
	}

	/** Adds this dialog's <code>Component</code>s to the layout. */
	private void createLayout()
	{
		JLabel partNL = createNameLabel("Part Number");
		this.vlPart = createValueLabel(partNL);
		JLabel serialNL = createNameLabel("Serial Number");
		this.vlSerial = createValueLabel(serialNL);

		JPanel labelPanel = new JPanel(new GridLayout(2, 2, 10, 20));
		labelPanel.add(partNL);
		labelPanel.add(this.vlPart);
		labelPanel.add(serialNL);
		labelPanel.add(this.vlSerial);

		JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 20, 0));
		buttonPanel.add(this.pbValidate);
		buttonPanel.add(this.pbSuspend);

		JPanel contentPaneCenter = new JPanel(new GridBagLayout());
		contentPaneCenter.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1, 1,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, 
				new Insets(15, 30, 15, 30), 0, 0);

		contentPaneCenter.add(this.tfInput, gbc);

		gbc.gridy++;
		contentPaneCenter.add(labelPanel, gbc);

		gbc.gridy++;
		contentPaneCenter.add(buttonPanel, gbc);

		this.setContentPane(contentPaneCenter);
		setLabelDimensions(this.vlPart, MFSConstants.MAX_PN_CHARACTERS);
		setLabelDimensions(this.vlSerial, MFSConstants.MAX_SN_CHARACTERS);
		this.pack();
	}

	/** Adds the listeners to this dialog's <code>Component</code>s. */
	private void addMyListeners()
	{
		this.pbValidate.addActionListener(this);
		this.pbSuspend.addActionListener(this);

		this.pbValidate.addKeyListener(this);
		this.pbSuspend.addKeyListener(this);
		this.tfInput.addKeyListener(this);
	}

	/** Invoked when the {@link #pbValidate} is selected. */
	private void enter()
	{
		if (this.vlPart.getText().length() == 0)
		{
			IGSMessageBox.showOkMB(this, null, "Invalid Part Number Entered.", null);

			this.toFront();
			this.tfInput.requestFocusInWindow();
		}
		else if (this.vlSerial.getText().length() == 0)
		{
			IGSMessageBox.showOkMB(this, null, "Invalid Serial Number Entered.", null);

			this.toFront();
			this.tfInput.requestFocusInWindow();
		}
		else
		{
			String data = new String("VLDT_WUSN " + this.fieldMctl + this.vlSerial.getText());

			MFSTransaction vldt_wusn = new MFSFixedTransaction(data);
			vldt_wusn.setActionMessage("Validating Work Unit Serial Number, Please Wait...");
			MFSComm.getInstance().execute(vldt_wusn, this);

			if (vldt_wusn.getReturnCode() != 0)
			{
				IGSMessageBox.showOkMB(this, null, vldt_wusn.getErms(), null);

				this.toFront();
				this.tfInput.requestFocusInWindow();
			}
			else
			{
				this.fieldSerialNum = this.vlSerial.getText();
				this.fieldPartNum = this.vlPart.getText();
				this.dispose();
			}
		}
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
	 * Returns the scanned in serial number.
	 * @return the scanned in serial number
	 */
	public String getSerialNum()
	{
		return this.fieldSerialNum;
	}

	/** Processes the input the user entered in the <code>JTextField</code>. */
	public void recvInput()
	{
		try
		{
			boolean found = false;
			int rc = 0;

			MFSBCBarcodeDecoder decoder = MFSBCBarcodeDecoder.getInstance();
			decoder.setMyBarcodeInput(this.tfInput.getText().toUpperCase());
			decoder.setMyBCPartObject(new MFSBCPartObject());

			/* setup barcode indicator values */
			MFSBCIndicatorValue bcIndVal = new MFSBCIndicatorValue();
			bcIndVal.setPRLN(this.fieldPrln);
			bcIndVal.setPNRI("1"); //$NON-NLS-1$
			bcIndVal.setCSNI("1"); //$NON-NLS-1$

			decoder.setMyBCIndicatorValue(bcIndVal);

			if (decoder.decodeBarcodeFor(this))
			{
				rc = decoder.getBCMyPartObject().getRC();
			}
			else
			{
				rc = 10;
				decoder.getBCMyPartObject().setErrorString(
						MFSBCBarcodeDecoder.DECODE_TIMED_OUT_ERMS);
			}

			if (rc == 0)
			{
				/* PN */
				if (!(decoder.getBCMyPartObject().getPN().equals(""))) //$NON-NLS-1$
				{
					this.vlPart.setText(decoder.getBCMyPartObject().getPN());
					found = true;
				}
				/* SN */
				if (!(decoder.getBCMyPartObject().getSN().equals(""))) //$NON-NLS-1$
				{
					this.vlSerial.setText(decoder.getBCMyPartObject().getSN());
					found = true;
				}
			}

			if ((rc != 0) || (!found && !this.tfInput.getText().equals(""))) //$NON-NLS-1$
			{
				String erms = decoder.getBCMyPartObject().getErrorString();
				if (erms.trim().length() == 0)
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
			if (source == this.pbValidate)
			{
				enter();
			}
			else if (source == this.pbSuspend)
			{
				this.fieldPressedSuspend = true; //~2a
				this.dispose(); //~2a
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
			if (source == this.pbValidate)
			{
				this.pbValidate.requestFocusInWindow();
				this.pbValidate.doClick();
			}
			else if (source == this.pbSuspend)
			{
				this.pbSuspend.requestFocusInWindow();
				this.pbSuspend.doClick();
			}
			else if (source == this.tfInput)
			{
				recvInput();
			}
		}
		else if (keyCode == KeyEvent.VK_F2)
		{
			this.pbValidate.requestFocusInWindow();
			this.pbValidate.doClick();
		}
		else if (keyCode == KeyEvent.VK_F7)
		{
			this.pbSuspend.requestFocusInWindow();
			this.pbSuspend.doClick();
		}
		else if (keyCode == KeyEvent.VK_ESCAPE)
		{
			this.dispose();
		}
	}
	/**
	 * Returns <code>true</code> if the suspend button was pressed.
	 * @return <code>true</code> if the suspend button was pressed
	 */
	public boolean getPressedSuspend()
	{
		return this.fieldPressedSuspend;
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
