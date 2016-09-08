/* © Copyright IBM Corporation 2005, 2007. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2007-02-20      34242JR  R Prechel        -Java 5 version
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
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import com.ibm.rchland.mfgapps.mfsclient.MFSDialog;
import com.ibm.rchland.mfgapps.mfsclient.MFSFrame;

/**
 * <code>MFSChangeStatusDialog</code> is the <code>MFSDialog</code> used to
 * change the status of a part.
 * @author The MFS Client Development Team
 */
public class MFSChangeStatusDialog
	extends MFSDialog
{
	private static final long serialVersionUID = 1L;
	/** The Reset Status <code>JRadioButton</code>. */
	private JRadioButton rbReset = createRadioButton("Reset Status", true, 'R');

	/** The Part Missing <code>JRadioButton</code>. */
	private JRadioButton rbMissing = createRadioButton("Part Missing", false, 'M');

	/** The Customer Kept <code>JRadioButton</code>. */
	private JRadioButton rbKept = createRadioButton("Customer Kept", false, 'K');

	/** The Scrap It <code>JRadioButton</code>. */
	private JRadioButton rbScrap = createRadioButton("Scrap It", false, 'S');

	/** The Un Scrap It <code>JRadioButton</code>. */
	private JRadioButton rbUnScrap = createRadioButton("Un Scrap It", false, 'U');

	/** The OK <code>JButton</code>. */
	private JButton pbOk = createButton("OK", 'O');

	/** The Cancel <code>JButton</code>. */
	private JButton pbCancel = createButton("Cancel", 'n');

	/** Set <code>true</code> iff the OK button is pressed. */
	private boolean fieldPressedOkay = false;

	/**
	 * Constructs a new <code>MFSChangeStatusDialog</code>.
	 * @param parent the <code>MFSFrame</code> that caused this
	 *        <code>MFSChangeStatusDialog</code> to be displayed
	 */
	public MFSChangeStatusDialog(MFSFrame parent)
	{
		super(parent, "Specify New Status");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		createLayout();
		addMyListeners();
	}

	/** Adds this dialog's <code>Component</code>s to the layout. */
	private void createLayout()
	{
		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(this.rbReset);
		buttonGroup.add(this.rbMissing);
		buttonGroup.add(this.rbKept);
		buttonGroup.add(this.rbScrap);
		buttonGroup.add(this.rbUnScrap);

		JPanel radioButtonPanel = new JPanel(new GridLayout(5, 1));
		radioButtonPanel.add(this.rbReset);
		radioButtonPanel.add(this.rbMissing);
		radioButtonPanel.add(this.rbKept);
		radioButtonPanel.add(this.rbScrap);
		radioButtonPanel.add(this.rbUnScrap);

		JPanel contentPane = new JPanel(new GridBagLayout());
		contentPane.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 2, 1, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0);

		contentPane.add(radioButtonPanel, gbc);

		gbc.gridy++;
		gbc.gridwidth = 1;
		gbc.weighty = 0.0;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(20, 0, 0, 10);
		contentPane.add(this.pbOk, gbc);

		gbc.gridx = 1;
		gbc.insets = new Insets(20, 10, 0, 0);
		contentPane.add(this.pbCancel, gbc);

		setContentPane(contentPane);
		setButtonDimensions(this.pbOk, this.pbCancel);
		pack();
	}

	/** Adds the listeners to this dialog's <code>Component</code>s. */
	private void addMyListeners()
	{
		this.pbOk.addActionListener(this);
		this.pbCancel.addActionListener(this);

		this.pbOk.addKeyListener(this);
		this.pbCancel.addKeyListener(this);

		this.rbReset.addKeyListener(this);
		this.rbMissing.addKeyListener(this);
		this.rbKept.addKeyListener(this);
		this.rbScrap.addKeyListener(this);
		this.rbUnScrap.addKeyListener(this);
	}

	/**
	 * Creates a new <code>JRadioButton</code>.
	 * @param text the text of the <code>JRadioButton</code>
	 * @param selected if <code>true</code>, the button is initially
	 *        selected; otherwise, the button is initially unselected
	 * @param mnemonic the mnemonic for the <code>JRadioButton</code>
	 * @return the new <code>JRadioButton</code>
	 */
	private JRadioButton createRadioButton(String text, boolean selected, char mnemonic)
	{
		JRadioButton result = new JRadioButton(text, selected);
		result.setMnemonic(mnemonic);
		return result;
	}
	
	/**
	 * Returns <code>true</code> iff {@link #rbKept} is selected.
	 * @return <code>true</code> iff {@link #rbKept} is selected
	 */
	public boolean isKeptSelected()
	{
		return this.rbKept.isSelected();
	}
	
	/**
	 * Returns <code>true</code> iff {@link #rbMissing} is selected.
	 * @return <code>true</code> iff {@link #rbMissing} is selected
	 */
	public boolean isMissingSelected()
	{
		return this.rbMissing.isSelected();
	}
	
	/**
	 * Returns <code>true</code> iff {@link #rbReset} is selected.
	 * @return <code>true</code> iff {@link #rbReset} is selected
	 */
	public boolean isResetSelected()
	{
		return this.rbReset.isSelected();
	}
	
	/**
	 * Returns <code>true</code> iff {@link #rbScrap} is selected.
	 * @return <code>true</code> iff {@link #rbScrap} is selected
	 */
	public boolean isScrapSelected()
	{
		return this.rbScrap.isSelected();
	}
	
	/**
	 * Returns <code>true</code> iff {@link #rbUnScrap} is selected.
	 * @return <code>true</code> iff {@link #rbUnScrap} is selected
	 */
	public boolean isUnScrapSelected()
	{
		return this.rbUnScrap.isSelected();
	}

	/**
	 * Returns <code>true</code> iff the OK button was pressed.
	 * @return <code>true</code> iff the OK button was pressed
	 */
	public boolean getPressedOkay()
	{
		return this.fieldPressedOkay;
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
			else if (source == this.pbOk)
			{
				this.fieldPressedOkay = true;
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
		final Object source = ke.getSource();
		if (keyCode == KeyEvent.VK_ENTER)
		{
			if (source == this.pbCancel)
			{
				this.pbCancel.requestFocusInWindow();
				this.pbCancel.doClick();
			}
			else
			{
				this.pbOk.requestFocusInWindow();
				this.pbOk.doClick();
			}
		}
		/* Arrow up or left -> go up the radio buttons */
		else if (keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_LEFT)
		{
			if (source == this.rbReset)
			{
				this.rbUnScrap.requestFocusInWindow();
				this.rbUnScrap.setSelected(true);
			}
			else if (source == this.rbMissing)
			{
				this.rbReset.requestFocusInWindow();
				this.rbReset.setSelected(true);
			}
			else if (source == this.rbKept)
			{
				this.rbMissing.requestFocusInWindow();
				this.rbMissing.setSelected(true);
			}
			else if (source == this.rbScrap)
			{
				this.rbKept.requestFocusInWindow();
				this.rbKept.setSelected(true);
			}
			else if (source == this.rbUnScrap)
			{
				this.rbScrap.requestFocusInWindow();
				this.rbScrap.setSelected(true);
			}
		}
		/* Arrow down or right -> go down the radio buttons */
		else if (keyCode == KeyEvent.VK_DOWN || keyCode == KeyEvent.VK_RIGHT)
		{
			if (source == this.rbReset)
			{
				this.rbMissing.requestFocusInWindow();
				this.rbMissing.setSelected(true);
			}
			else if (source == this.rbMissing)
			{
				this.rbKept.requestFocusInWindow();
				this.rbKept.setSelected(true);
			}
			else if (source == this.rbKept)
			{
				this.rbScrap.requestFocusInWindow();
				this.rbScrap.setSelected(true);
			}
			else if (source == this.rbScrap)
			{
				this.rbUnScrap.requestFocusInWindow();
				this.rbUnScrap.setSelected(true);
			}
			else if (source == this.rbUnScrap)
			{
				this.rbReset.requestFocusInWindow();
				this.rbReset.setSelected(true);
			}
		}
	}

	/**
	 * Invoked the first time a window is made visible.
	 * <p>
	 * Requests the focus for the {@link #pbOk}.
	 * @param we the <code>WindowEvent</code>
	 */
	public void windowOpened(WindowEvent we)
	{
		if (we.getSource() == this)
		{
			this.pbOk.requestFocusInWindow();
		}
	}
}
