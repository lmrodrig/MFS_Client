/* @ Copyright IBM Corporation 2005, 2007. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2007-01-26      34242JR  R Prechel        -Java 5 version
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.dialog;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.ibm.rchland.mfgapps.mfsclient.MFSDialog;
import com.ibm.rchland.mfgapps.mfsclient.MFSFrame;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSConstants;

/**
 * <code>MFSManualMcpDialog</code> is displayed when the MCP Manual button is
 * selected on <code>MFSSelWrkExtFnctPanel</code>.
 * @author The MFS Client Development Team
 */
public class MFSManualMcpDialog
	extends MFSDialog
{
	private static final long serialVersionUID = 1L;
	/** The Part Number input <code>JTextField</code>. */
	private JTextField tfPartNum;

	/** The EC Level input <code>JTextField</code>. */
	private JTextField tfEcLevel;

	/** The Print <code>JButton</code>. */
	private JButton pbPrint = createButton("Print", 'P');

	/** The Cancel <code>JButton</code>. */
	private JButton pbCancel = createButton("ESC=Cancel");

	/**
	 * Constructs a new <code>MFSManualMcpDialog</code>.
	 * @param parent the <code>MFSFrame</code> that caused this
	 *        <code>MFSManualMcpDialog</code> to be displayed
	 */
	public MFSManualMcpDialog(MFSFrame parent)
	{
		super(parent, "Manual MCP Label entry");
		setModal(false);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		createLayout();
		addMyListeners();
	}

	/** Adds this dialog's <code>Component</code>s to the layout. */
	private void createLayout()
	{
		JLabel pnLabel = createNameLabel("Part Number");
		JLabel ecLevelLabel = createNameLabel("EC level");

		this.tfPartNum = createTextField(MFSConstants.MEDIUM_TF_COLS, 0, pnLabel);
		this.tfEcLevel = createTextField(MFSConstants.MEDIUM_TF_COLS, 0, ecLevelLabel);

		//Dimensions are mutable, and thus are not reused
		Dimension min = new Dimension(20, 0);
		Dimension preferred = new Dimension(20, 0);
		Dimension max = new Dimension(Short.MAX_VALUE, Short.MAX_VALUE);
		Box.Filler fillerOne = new Box.Filler(min, preferred, max);

		JPanel buttonPanel = new JPanel(null);
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
		buttonPanel.add(this.pbPrint);
		buttonPanel.add(fillerOne);
		buttonPanel.add(this.pbCancel);

		JPanel contentPane = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1, 1,
				GridBagConstraints.EAST, GridBagConstraints.NONE, 
				new Insets(30, 20, 10, 10), 0, 0);

		contentPane.add(pnLabel, gbc);
		gbc.gridy++;
		gbc.insets = new Insets(10, 20, 10, 10);
		contentPane.add(ecLevelLabel, gbc);

		gbc.gridx++;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(30, 10, 10, 20);

		contentPane.add(this.tfPartNum, gbc);
		gbc.gridy++;
		gbc.insets = new Insets(10, 10, 10, 20);
		contentPane.add(this.tfEcLevel, gbc);

		gbc.gridx--;
		gbc.gridy++;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(20, 20, 30, 20);
		contentPane.add(buttonPanel, gbc);

		this.setContentPane(contentPane);
		setButtonDimensions(this.pbPrint, this.pbCancel);
		this.pack();
	}

	/** Adds the listeners to this dialog's <code>Component</code>s. */
	private void addMyListeners()
	{
		this.pbCancel.addActionListener(this);
		this.pbPrint.addActionListener(this);

		this.addKeyListener(this);
		this.pbCancel.addKeyListener(this);
		this.pbPrint.addKeyListener(this);
	}

	/** Calls {@link MFSPrintingMethods#rochmcp}. */
	/*
	private void print()
	{
		try
		{
			MFSPrintingMethods.rochmcp(this.tfPartNum.getText(),
					this.tfEcLevel.getText(), 1, getParentFrame());
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(this, null, null, e);
		}
	}
	*/

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
				this.pbPrint.requestFocusInWindow();
				this.pbPrint.doClick();
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
	 * Requests the focus for the {@link #tfPartNum}.
	 * @param we the <code>WindowEvent</code>
	 */
	public void windowOpened(WindowEvent we)
	{
		if (we.getSource() == this)
		{
			this.tfPartNum.requestFocusInWindow();
		}
	}
}
