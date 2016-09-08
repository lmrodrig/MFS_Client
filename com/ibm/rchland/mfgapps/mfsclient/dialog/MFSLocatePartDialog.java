/* © Copyright IBM Corporation 2005, 2007. All rights reserved.
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
 * <code>MFSLocatePartDialog</code> is the <code>MFSActionableDialog</code>
 * used to prompt the user for the part and serial number of a part to locate.
 * @author The MFS Client Development Team
 */
public class MFSLocatePartDialog
	extends MFSActionableDialog
{
	private static final long serialVersionUID = 1L;
	/** The input <code>JTextField</code>. */
	private JTextField tfInput = createTextField(MFSConstants.LARGE_TF_COLS, 0);

	/** The part number value <code>JLabel</code>. */
	private JLabel vlPN;

	/** The serial number value <code>JLabel</code>. */
	private JLabel vlSN;

	/** The Enter <code>JButton</code>. */
	private JButton pbEnter = createButton("Enter", 'E');

	/** The Cancel <code>JButton</code>. */
	private JButton pbCancel = createButton("Cancel", 'n');

	/** Set <code>true</code> iff the enter button is pressed. */
	private boolean fieldPressedEnter = false;

	/**
	 * Constructs a new <code>MFSLocatePartDialog</code>.
	 * @param parent the <code>MFSFrame</code> that caused this
	 *        <code>MFSLocatePartDialog</code> to be displayed
	 */
	public MFSLocatePartDialog(MFSFrame parent)
	{
		super(parent, "Part Locator");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		createLayout();
		addMyListeners();
	}

	/** Adds this dialog's <code>Component</code>s to the layout. */
	private void createLayout()
	{
		JLabel partNameLabel = createSmallNameLabel("Part Number:");
		JLabel serialNameLabel = createSmallNameLabel("Serial Number:");

		this.vlPN = createSmallValueLabel(partNameLabel);
		this.vlSN = createSmallValueLabel(serialNameLabel);

		JPanel labelPanel = new JPanel(new GridLayout(2, 2, 10, 10));
		labelPanel.add(partNameLabel);
		labelPanel.add(this.vlPN);
		labelPanel.add(serialNameLabel);
		labelPanel.add(this.vlSN);

		JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 20, 0));
		buttonPanel.add(this.pbEnter);
		buttonPanel.add(this.pbCancel);

		JPanel contentPaneCenter = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1, 1,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, 
				new Insets(20, 50, 20, 50), 0, 0);
		
		contentPaneCenter.add(this.tfInput, gbc);

		gbc.gridy++;
		gbc.insets = new Insets(0, 40, 20, 40);
		contentPaneCenter.add(labelPanel, gbc);

		gbc.gridy++;
		contentPaneCenter.add(buttonPanel, gbc);

		this.setContentPane(contentPaneCenter);
		setLabelDimensions(this.vlPN, MFSConstants.MAX_PN_CHARACTERS);
		setLabelDimensions(this.vlSN, MFSConstants.MAX_SN_CHARACTERS);
		this.pack();
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
		if (!pnIsBlank() && !snIsBlank())
		{
			this.fieldPressedEnter = true;
			this.dispose();
		}
		else if (pnIsBlank() && !snIsBlank())
		{
			String erms = "No Valid Part Number Entered!";
			IGSMessageBox.showOkMB(getParentFrame(), null, erms, null);
			this.tfInput.requestFocusInWindow();
		}
		else
		{
			String erms = "Invalid Serial Number Entered";
			IGSMessageBox.showOkMB(getParentFrame(), null, erms, null);
			this.tfInput.requestFocusInWindow();
		}
	}

	/**
	 * Returns <code>true</code> iff the enter button was pressed.
	 * @return <code>true</code> iff the enter button was pressed
	 */
	public boolean getPressedEnter()
	{
		return this.fieldPressedEnter;
	}

	/**
	 * Returns the text of {@link #vlPN}.
	 * @return the part number text
	 */
	public String getPNText()
	{
		return this.vlPN.getText();
	}

	/**
	 * Returns the text of {@link #vlSN}.
	 * @return the serial number text
	 */
	public String getSNText()
	{
		return this.vlSN.getText();
	}

	/**
	 * Returns <code>true</code> iff the text of {@link #vlPN} is
	 * <code>null</code> or blank.
	 * @return <code>true</code> iff the text of {@link #vlPN} is
	 *         <code>null</code> or blank
	 */
	private boolean pnIsBlank()
	{
		String text = this.vlPN.getText();
		return (text == null) || text.equals(""); //$NON-NLS-1$
	}

	/** Processes the input the user entered in the <code>JTextField</code>. */
	private void recvInput()
	{
		try
		{
			if (this.tfInput.getText().equals("")) //$NON-NLS-1$
			{
				this.toFront();

				if (!pnIsBlank() && !snIsBlank())
				{
					this.fieldPressedEnter = true;
					this.dispose();
				}
				return;
			}

			boolean found = false;
			int rc = 0;

			MFSBCBarcodeDecoder barcode = MFSBCBarcodeDecoder.getInstance();
			barcode.setMyBarcodeInput(this.tfInput.getText().toUpperCase());
			barcode.setMyBCPartObject(new MFSBCPartObject());

			/* setup barcode indicator values */
			MFSBCIndicatorValue bcIndVal = new MFSBCIndicatorValue();
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
				if (!(barcode.getBCMyPartObject().getWU().equals("")))
				{
					String erms = "Invalid Barcode entered! Must be 11S, Part Number, or Serial Number Barcode.";
					IGSMessageBox.showOkMB(getParentFrame(), null, erms, null);
					this.tfInput.setText(""); //$NON-NLS-1$
				}
				if (!(barcode.getBCMyPartObject().getPN().equals(""))) //$NON-NLS-1$
				{
					this.vlPN.setText(barcode.getBCMyPartObject().getPN());
					found = true;
				}
				if (!(barcode.getBCMyPartObject().getSN().equals(""))) //$NON-NLS-1$
				{
					this.vlSN.setText(barcode.getBCMyPartObject().getSN());
					found = true;
				}

				if ((rc != 0) || (!found && !this.tfInput.getText().equals(""))) //$NON-NLS-1$
				{
					String erms = barcode.getBCMyPartObject().getErrorString();
					if (erms.length() == 0)
					{
						erms = MFSBCBarcodeDecoder.INVALID_BARCODE_ERMS;
					}
					IGSMessageBox.showOkMB(getParentFrame(), null, erms, null);
				}
				else if (!this.vlPN.getText().equals("") && !this.vlSN.getText().equals("")) //$NON-NLS-1$ //$NON-NLS-2$
				{
					this.pbEnter.requestFocusInWindow();
					this.pbEnter.doClick();
				}
			}
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}

		this.toFront();
		this.tfInput.setText(""); //$NON-NLS-1$
	}

	/**
	 * Returns <code>true</code> iff the text of {@link #vlSN} is
	 * <code>null</code> or blank.
	 * @return <code>true</code> iff the text of {@link #vlSN} is
	 *         <code>null</code> or blank
	 */
	private boolean snIsBlank()
	{
		String text = this.vlSN.getText();
		return (text == null) || text.equals(""); //$NON-NLS-1$
	}

	/**
	 * Invoked when an action occurs.
	 * @param ae the <code>ActionEvent</code>
	 */
	public void actionPerformed(ActionEvent ae)
	{
		final Object source = ae.getSource();
		if (source == this.pbEnter)
		{
			if (pnIsBlank() && snIsBlank())
			{
				recvInput();
			}
			enter();
		}
		else if (source == this.pbCancel)
		{
			dispose();
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
			if (source == this.tfInput)
			{
				recvInput();
			}
			else if (source == this.pbEnter)
			{
				this.pbEnter.requestFocusInWindow();
				this.pbEnter.doClick();
			}
			else if (source == this.pbCancel)
			{
				this.pbCancel.requestFocusInWindow();
				this.pbCancel.doClick();
			}
		}
		else if (keyCode == KeyEvent.VK_ESCAPE)
		{
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
