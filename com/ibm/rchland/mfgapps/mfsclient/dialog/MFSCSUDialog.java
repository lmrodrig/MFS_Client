/* © Copyright IBM Corporation 2005, 2007. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2006-05-19   ~1 29807JM  Blanca Aceves    -Don't validate against IDSS
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

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.ibm.rchland.mfgapps.mfsclient.MFSDialog;
import com.ibm.rchland.mfgapps.mfsclient.MFSFrame;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSConstants;

/**
 * <code>MFSCSUDialog</code> is the <code>MFSDialog</code> used to perform
 * CSU System Barcode Verification.
 * @author The MFS Client Development Team
 */
public class MFSCSUDialog
	extends MFSDialog
{
	private static final long serialVersionUID = 1L;
	/** The input <code>JTextField</code>. */
	private JTextField tfInput = null;

	/** The Suspend (F2) <code>JButton</code>. */
	private JButton pbSuspend = createButton("F2 = Suspend");

	/** The Enter <code>JButton</code>. */
	private JButton pbEnter = createButton("Enter = OK");

	/** Set <code>true</code> iff the enter button is pressed. */
	private boolean fieldPressedEnter = false;

	/** Set <code>true</code> iff the suspend button is pressed. */
	private boolean fieldPressedSuspend = false;

	/**
	 * Constructs a new <code>MFSCSUDialog</code>.
	 * @param parent the <code>MFSFrame</code> that caused this
	 *        <code>MFSCSUDialog</code> to be displayed
	 */
	public MFSCSUDialog(MFSFrame parent)
	{
		super(parent, "CSU Guide Verification");
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		createLayout();
		addMyListeners();
	}

	/** Adds this dialog's <code>Component</code>s to the layout. */
	private void createLayout()
	{
		JLabel label = createLabel("CSU System Barcode");
		this.tfInput = createTextField(MFSConstants.MEDIUM_TF_COLS, 0, label);

		JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 30, 0));
		buttonPanel.add(this.pbSuspend);
		buttonPanel.add(this.pbEnter);

		JPanel contentPane = new JPanel(new GridBagLayout());
		contentPane.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, 
				new Insets(20, 4, 20, 4), 0, 0);

		contentPane.add(label, gbc);

		gbc.gridx = 1;
		contentPane.add(this.tfInput, gbc);

		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 2;

		contentPane.add(buttonPanel, gbc);

		setContentPane(contentPane);
		pack();
	}

	/** Adds the listeners to this dialog's <code>Component</code>s. */
	private void addMyListeners()
	{
		this.pbEnter.addActionListener(this);
		this.pbSuspend.addActionListener(this);
		this.pbEnter.addKeyListener(this);
		this.pbSuspend.addKeyListener(this);
		this.tfInput.addKeyListener(this);
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
	 * Returns <code>true</code> iff the suspend button was pressed.
	 * @return <code>true</code> iff the suspend button was pressed
	 */
	public boolean getPressedSuspend()
	{
		return this.fieldPressedSuspend;
	}

	/**
	 * Searches the input text for the specified <code>mfgn</code>.
	 * @param mfgn the mfgn to search for
	 * @return -1 if the input text is not long enough; 0 if the mfgn is not
	 *         found in the input text; 1 if the mfgn is found in the input text
	 */
	public int parseCSU(String mfgn)
	{
		int result = 0;
		/* ~1C Start */
		if (this.tfInput.getText().length() < 7)
		{
			result = -1; /* error, it should be at least 7 char long */
		}
		else if (-1 == this.tfInput.getText().toUpperCase().indexOf(mfgn))
		{
			result = 0; /* error, mfgn not found in string */
		}
		else
		{
			result = 1; /* correct mfgn found in the string */
		}
		/* ~1C End */

		return result;
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
			this.dispose();
			this.fieldPressedEnter = true;
		}
		if (source == this.pbSuspend)
		{
			this.dispose();
			this.fieldPressedSuspend = true;
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
			if (source == this.pbSuspend)
			{
				this.pbSuspend.requestFocusInWindow();
				this.pbSuspend.doClick();
			}
			else
			{
				this.pbEnter.requestFocusInWindow();
				this.pbEnter.doClick();
			}
		}
		else if (keyCode == KeyEvent.VK_F2)
		{
			this.pbSuspend.requestFocusInWindow();
			this.pbSuspend.doClick();
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
