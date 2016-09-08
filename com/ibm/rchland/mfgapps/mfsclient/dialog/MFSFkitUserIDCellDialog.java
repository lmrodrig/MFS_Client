/* © Copyright IBM Corporation 2003, 2007. All rights reserved.
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

import com.ibm.rchland.mfgapps.mfsclient.MFSDialog;
import com.ibm.rchland.mfgapps.mfsclient.MFSFrame;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSConstants;

/**
 * <code>MFSFkitUserIDCellDialog</code> is the <code>MFSDialog</code> used
 * to enter USER and CELL information for FKIT operations.
 * @author The MFS Client Development Team
 */
public class MFSFkitUserIDCellDialog
	extends MFSDialog
{
	private static final long serialVersionUID = 1L;
	/** The User input <code>JTextField</code>. */
	private JTextField tfUser;

	/** The Cell input <code>JTextField</code>. */
	private JTextField tfCell;

	/** The Enter <code>JButton</code>. */
	private JButton pbEnter = createButton("Enter", 'E');

	/** The Cancel <code>JButton</code>. */
	private JButton pbCancel = createButton("Cancel", 'n');

	/** Set <code>true</code> when the Cancel button is selected. */
	private boolean fieldPressedCancel = false;

	/**
	 * Constructs a new <code>MFSFkitUserIDCellDialog</code>.
	 * @param parent the <code>MFSFrame</code> that caused this
	 *        <code>MFSFkitUserIDCellDialog</code> to be displayed
	 */
	public MFSFkitUserIDCellDialog(MFSFrame parent)
	{
		super(parent, "USER ID AND CELL");
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		createLayout();
		addMyListeners();
	}

	/** Adds this dialog's <code>Component</code>s to the layout. */
	private void createLayout()
	{
		JLabel userLabel = createSmallNameLabel("USER:");
		this.tfUser = createTextField(MFSConstants.SMALL_TF_COLS, 8, userLabel);

		JLabel cellLabel = createSmallNameLabel("CELL:");
		this.tfCell = createTextField(MFSConstants.SMALL_TF_COLS, 8, cellLabel);

		JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 20, 0));
		buttonPanel.add(this.pbEnter);
		buttonPanel.add(this.pbCancel);

		JPanel contentPane = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.NONE, 
				new Insets(20, 40, 0, 10), 0, 0);

		contentPane.add(userLabel, gbc);

		gbc.gridy++;
		gbc.insets = new Insets(10, 40, 0, 10);
		contentPane.add(cellLabel, gbc);

		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(20, 0, 0, 0);
		contentPane.add(this.tfUser, gbc);

		gbc.gridy++;
		gbc.insets = new Insets(10, 0, 0, 0);
		contentPane.add(this.tfCell, gbc);

		gbc.gridx = 0;
		gbc.gridy++;
		gbc.gridwidth = 2;
		gbc.weighty = 1;
		gbc.insets = new Insets(10, 10, 10, 10);
		contentPane.add(buttonPanel, gbc);

		setContentPane(contentPane);
		pack();
	}

	/** Adds the listeners to this dialog's <code>Component</code>s. */
	private void addMyListeners()
	{
		this.pbCancel.addActionListener(this);
		this.pbEnter.addActionListener(this);

		this.tfCell.addKeyListener(this);
		this.tfUser.addKeyListener(this);
		this.pbCancel.addKeyListener(this);
		this.pbEnter.addKeyListener(this);
	}

	/**
	 * Returns the text of {@link #tfCell}.
	 * @return the text of {@link #tfCell}
	 */
	public String getCellText()
	{
		return this.tfCell.getText();
	}

	/**
	 * Returns the text of {@link #tfUser}.
	 * @return the text of {@link #tfUser}
	 */
	public String getUserText()
	{
		return this.tfUser.getText();
	}

	/**
	 * Returns <code>true</code> iff the Cancel button was selected.
	 * @return <code>true</code> iff the Cancel button was selected
	 */
	public boolean getPressedCancel()
	{
		return this.fieldPressedCancel;
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
				this.fieldPressedCancel = true;
				this.dispose();
			}
			else if (source == this.pbEnter)
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
				this.pbEnter.requestFocusInWindow();
				this.pbEnter.doClick();
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
	 * Requests the focus for the {@link #tfUser}.
	 * @param we the <code>WindowEvent</code>
	 */
	public void windowOpened(WindowEvent we)
	{
		if (we.getSource() == this)
		{
			this.tfUser.requestFocusInWindow();
		}
	}
}
