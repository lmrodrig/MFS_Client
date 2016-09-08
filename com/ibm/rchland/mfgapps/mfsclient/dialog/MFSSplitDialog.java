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
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.ibm.rchland.mfgapps.client.utils.messagebox.IGSMessageBox;
import com.ibm.rchland.mfgapps.mfsclient.MFSDialog;
import com.ibm.rchland.mfgapps.mfsclient.MFSFrame;

/**
 * <code>MFSSplitDialog</code> is the <code>MFSDialog</code> used to enter a
 * split quantity.
 * @author The MFS Client Development Team
 */
public class MFSSplitDialog
	extends MFSDialog
{
	private static final long serialVersionUID = 1L;
	/** The input <code>JTextField</code>. */
	private JTextField tfInput = createTextField(5, 0);

	/** The OK <code>JButton</code>. */
	private JButton pbOkay = createButton("OK", 'O');

	/** The Cancel <code>JButton</code>. */
	private JButton pbCancel = createButton("Cancel", 'n');

	/** Set <code>true</code> when the OK button is pressed. */
	private boolean fieldPressedOk = false;

	/** The maximum quantity allowed. */
	private int fieldMaxQty = 0;

	/**
	 * Constructs a new <code>MFSSplitDialog</code>.
	 * @param parent the <code>MFSFrame</code> that caused this
	 *        <code>MFSSplitDialog</code> to be displayed
	 * @param maxQty the maximum quantity allowed
	 */
	public MFSSplitDialog(MFSFrame parent, int maxQty)
	{
		super(parent, "Split Quantity");
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.fieldMaxQty = maxQty;
		createLayout();
		addMyListeners();
	}

	/** Adds this dialog's <code>Component</code>s to the layout. */
	private void createLayout()
	{
		JLabel qtyLabel = createLabel("Quantity");

		JPanel contentPane = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.NONE, 
				new Insets(10, 30, 0, 10), 0, 0);

		contentPane.add(qtyLabel, gbc);

		gbc.gridx = 1;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(10, 10, 0, 30);
		contentPane.add(this.tfInput, gbc);

		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.insets = new Insets(30, 20, 15, 10);
		contentPane.add(this.pbOkay, gbc);

		gbc.gridx = 1;
		gbc.insets = new Insets(30, 10, 15, 20);
		contentPane.add(this.pbCancel, gbc);

		setContentPane(contentPane);
		setButtonDimensions(this.pbOkay, this.pbCancel);
		pack();
	}

	/** Adds the listeners to this dialog's <code>Component</code>s. */
	private void addMyListeners()
	{
		this.pbOkay.addActionListener(this);
		this.pbCancel.addActionListener(this);
		this.tfInput.addKeyListener(this);
	}

	/**
	 * Returns <code>true</code> iff the okay button was pressed.
	 * @return <code>true</code> iff the okay button was pressed
	 */
	public boolean getPressedOk()
	{
		return this.fieldPressedOk;
	}

	/**
	 * Returns the quantity.
	 * @return the quantity
	 */
	public String getQtyText()
	{
		return this.tfInput.getText();
	}

	/** Invoked when {@link #pbOkay} is selected. */
	private void process()
	{
		String qty_string = this.tfInput.getText();
		try
		{
			//FIXME 34242JR max qty logic
			if (Integer.parseInt(qty_string) > 0)
			{
				this.fieldPressedOk = true;
				this.dispose();
			}
			else if (Integer.parseInt(qty_string) + 1 >= this.fieldMaxQty)
			{
				String erms = "Split Quantity is Too High";
				IGSMessageBox.showOkMB(this, null, erms, null);

				this.tfInput.requestFocusInWindow();
				this.toFront();
			}
		}
		catch (NumberFormatException e)
		{
			String erms = "Invalid Quantity";
			IGSMessageBox.showOkMB(this, null, erms, null);

			this.tfInput.requestFocusInWindow();
			this.toFront();
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
			if (source == this.pbCancel)
			{
				dispose();
			}
			else if (source == this.pbOkay)
			{
				process();
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
		final Object source = ke.getSource();
		if (keyCode == KeyEvent.VK_ENTER)
		{
			if (source == this.tfInput || source == this.pbOkay)
			{
				this.pbOkay.requestFocusInWindow();
				this.pbOkay.doClick();
			}
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
