/* © Copyright IBM Corporation 2010. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2010-03-17      47595MZ  Toribio H.       -initial version
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
 * <code>MFSEntityLocationPrintDialog </code> is the <code>MFSListDialog</code>
 * used to select the location for merged entities when suspended.
 * @author The MFS Client Development Team
 */
@SuppressWarnings("serial")
public class MFSEntityLocationPrintDialog 
extends MFSDialog
{
	/** The Reset Status <code>JRadioButton</code>. */
	private JRadioButton rbOnePerLoc = createRadioButton("Per Location", true, 'L');

	/** The Part Missing <code>JRadioButton</code>. */
	private JRadioButton rbOnePerCntr = createRadioButton("Per Container", false, 'C');

	/** The OK <code>JButton</code>. */
	private JButton pbOk = createButton("Print", 'P');

	/** The Cancel <code>JButton</code>. */
	private JButton pbCancel = createButton("Skip", 'S');
	
	/** Set <code>true</code> iff the OK button is pressed. */
	private boolean fieldPressedOkay = false;
	
	public MFSEntityLocationPrintDialog(MFSFrame parent)
	{
		super(parent, "Print Location Label");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		createLayout();
		addMyListeners();
	}
	/** Adds this dialog's <code>Component</code>s to the layout. */
	private void createLayout()
	{
		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(this.rbOnePerLoc);
		buttonGroup.add(this.rbOnePerCntr);

		JPanel radioButtonPanel = new JPanel(new GridLayout(2, 1));
		radioButtonPanel.add(this.rbOnePerLoc);
		radioButtonPanel.add(this.rbOnePerCntr);

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
	
	/** Adds the listeners to this dialog's <code>Component</code>s. */
	private void addMyListeners()
	{
		this.pbOk.addActionListener(this);
		this.pbCancel.addActionListener(this);

		this.pbOk.addKeyListener(this);
		this.pbCancel.addKeyListener(this);

		this.rbOnePerLoc.addKeyListener(this);
		this.rbOnePerCntr.addKeyListener(this);
	}
	/**
	 * Returns <code>true</code> iff {@link #rbKept} is selected.
	 * @return <code>true</code> iff {@link #rbKept} is selected
	 */
	public boolean isOnePerLocSelected()
	{
		return this.rbOnePerLoc.isSelected();
	}
	
	/**
	 * Returns <code>true</code> iff {@link #rbMissing} is selected.
	 * @return <code>true</code> iff {@link #rbMissing} is selected
	 */
	public boolean isrbOnePerCntrSelected()
	{
		return this.rbOnePerCntr.isSelected();
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
			this.rbOnePerLoc.requestFocusInWindow();
			this.rbOnePerLoc.setSelected(true);
		}
		/* Arrow down or right -> go down the radio buttons */
		else if (keyCode == KeyEvent.VK_DOWN || keyCode == KeyEvent.VK_RIGHT)
		{
			this.rbOnePerCntr.requestFocusInWindow();
			this.rbOnePerCntr.setSelected(true);
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