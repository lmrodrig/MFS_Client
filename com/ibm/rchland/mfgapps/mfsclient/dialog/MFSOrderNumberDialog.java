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
 * <code>MFSOrderNumberDialog</code> is the <code>MFSDialog</code> used to
 * enter an order number.
 * @author The MFS Client Development Team
 */
public class MFSOrderNumberDialog
	extends MFSDialog
{
	private static final long serialVersionUID = 1L;
	/** The input <code>JTextField</code>. */
	private JTextField tfInput = createTextField(MFSConstants.SMALL_TF_COLS, 0);

	/** The Enter <code>JButton</code>. */
	private JButton pbEnter = createButton("Enter", 'E');

	/** The Cancel <code>JButton</code>. */
	private JButton pbCancel = createButton("Cancel", 'n');

	/** Set <code>true</code> iff the enter button is pressed. */
	private boolean fieldPressedEnter = false;

	/** The order number the user entered. */
	private String fieldOrderNumber = ""; //$NON-NLS-1$

	/**
	 * Constructs a new <code>MFSOrderNumberDialog</code>.
	 * @param parent the <code>MFSFrame</code> that caused this
	 *        <code>MFSOrderNumberDialog</code> to be displayed
	 */
	public MFSOrderNumberDialog(MFSFrame parent)
	{
		super(parent, "Order Number?");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		createLayout();
		addMyListeners();
	}

	/** Adds this dialog's <code>Component</code>s to the layout. */
	private void createLayout()
	{
		JPanel contentPane = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 2, 1, 1, 1,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, 
				new Insets(25, 50, 25, 50), 0, 0);
		
		contentPane.add(this.tfInput, gbc);

		gbc.gridy++;
		gbc.gridwidth = 1;
		gbc.insets = new Insets(0, 40, 10, 15);
		contentPane.add(this.pbEnter, gbc);

		gbc.gridx = 1;
		gbc.insets = new Insets(0, 15, 10, 40);
		contentPane.add(this.pbCancel, gbc);

		this.setContentPane(contentPane);
		setButtonDimensions(this.pbEnter, this.pbCancel);
		this.pack();
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
			String erms = "Invalid Order Number Entered.";
			IGSMessageBox.showOkMB(this, null, erms, null);
			this.toFront();
			this.tfInput.requestFocusInWindow();
		}
		else
		{
			this.fieldPressedEnter = true;
			this.fieldOrderNumber = this.tfInput.getText().trim().toUpperCase();
			this.dispose();
		}
	}

	/**
	 * Returns the order number the user entered.
	 * @return the order number the user entered
	 */
	public String getOrderNumber()
	{
		return this.fieldOrderNumber;
	}

	/**
	 * Returns <code>true</code> iff the Enter button was pressed
	 * @return <code>true</code> iff the Enter button was pressed
	 */
	public boolean getPressedEnter()
	{
		return this.fieldPressedEnter;
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
			enter();
		}
		else if (source == this.pbCancel)
		{
			this.dispose();
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
		}
	}
}
