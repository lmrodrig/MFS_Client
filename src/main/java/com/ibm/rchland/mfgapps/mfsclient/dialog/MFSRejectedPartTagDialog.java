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
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.ibm.rchland.mfgapps.client.utils.messagebox.IGSMessageBox;
import com.ibm.rchland.mfgapps.mfsclient.MFSDialog;
import com.ibm.rchland.mfgapps.mfsclient.MFSFrame;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSConstants;

/**
 * <code>MFSRejectedPartTagDialog</code> is the <code>MFSDialog</code> used
 * to enter a rejected part tag number.
 * @author The MFS Client Development Team
 */
public class MFSRejectedPartTagDialog
	extends MFSDialog
{
	private static final long serialVersionUID = 1L;
	/** The input <code>JTextField</code>. */
	private JTextField tfInput = createTextField(MFSConstants.SMALL_TF_COLS, 0);

	/** The Enter <code>JButton</code>. */
	private JButton pbEnter = createButton("Enter", 'E');

	/** The Cancel <code>JButton</code>. */
	private JButton pbCancel = createButton("Cancel", 'n');

	/** The rejected part tag number obtained from the input. */
	private String fieldRejs = ""; //$NON-NLS-1$

	/** Set <code>true</code> iff the enter button is pressed. */
	private boolean fieldPressedEnter = false;

	/**
	 * Constructs a new <code>MFSRejectedPartTagDialog</code>.
	 * @param parent the <code>MFSFrame</code> that caused this
	 *        <code>MFSRejectedPartTagDialog</code> to be displayed
	 */
	public MFSRejectedPartTagDialog(MFSFrame parent)
	{
		super(parent, "Rejected Part Tag Number");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		createLayout();
		addMyListeners();
	}

	/** Adds this dialog's <code>Component</code>s to the layout. */
	private void createLayout()
	{
		JPanel contentPane = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 2, 1, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, 
				new Insets(20, 20, 10, 20), 0, 0);

		contentPane.add(this.tfInput, gbc);

		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.insets = new Insets(20, 30, 10, 20);
		contentPane.add(this.pbEnter, gbc);

		gbc.gridx = 1;
		gbc.insets = new Insets(20, 20, 10, 30);
		contentPane.add(this.pbCancel, gbc);

		setContentPane(contentPane);
		setButtonDimensions(this.pbEnter, this.pbCancel);
		pack();
	}

	/** Adds the listeners to this dialog's <code>Component</code>s. */
	private void addMyListeners()
	{
		this.pbEnter.addActionListener(this);
		this.pbCancel.addActionListener(this);

		this.pbEnter.addKeyListener(this);
		this.pbCancel.addKeyListener(this);
		this.tfInput.addKeyListener(this);
	}

	/** Invoked when {@link #pbEnter} is selected. */
	private void enter()
	{
		if (this.tfInput.getText().equals("")) //$NON-NLS-1$
		{
			String erms = "Invalid Rejected Part Tag Number Entered.";
			IGSMessageBox.showOkMB(this, null, erms, null);
			this.toFront();
			this.tfInput.requestFocusInWindow();
		}
		else
		{
			this.fieldPressedEnter = true;
			String rejectedSeqTag = "        " + this.tfInput.getText().toUpperCase(); //$NON-NLS-1$
			this.fieldRejs = rejectedSeqTag.substring(rejectedSeqTag.length() - 8);
			this.dispose();
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
	 * Returns the rejected part tag number.
	 * @return the rejected part tag number
	 */
	public String getRejs()
	{
		return this.fieldRejs;
	}

	/**
	 * Sets the rejected part tag number.
	 * @param rejs the rejected part tag number
	 */
	public void setRejs(String rejs)
	{
		this.fieldRejs = rejs;
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
			if (source == this.pbEnter)
			{
				enter();
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
			if (source == this.pbEnter || source == this.tfInput)
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
			if (this.fieldRejs.length() != 0)
			{
				this.tfInput.setText(this.fieldRejs);
				this.tfInput.selectAll();
			}
		}
	}
}
