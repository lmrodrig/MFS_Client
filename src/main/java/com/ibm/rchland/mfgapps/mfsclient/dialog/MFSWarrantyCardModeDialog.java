/* @ Copyright IBM Corporation 2010. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2010-02-18      46810JL  J Mastachi       -Initial version
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.dialog;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JLabel;

import com.ibm.rchland.mfgapps.mfsclient.MFSDialog;
import com.ibm.rchland.mfgapps.mfsclient.MFSFrame;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSConstants;

/**
 * <code>MFSWarrantyCardModeDialog</code> is displayed when the Warranty Card
 * Printing button is selected on <code>MFSSelWrkExtFnctPanel</code>.
 * @author The MFS Client Development Team
 */
public class MFSWarrantyCardModeDialog extends MFSDialog
{
	private static final long serialVersionUID = 1L;
	/** The PRC Warranty Card Mode <code>JButton</code>. */
	private JButton pbPRCMode = createButton("PRC Warranty Card Mode", 'P');
	
	/** The Japan Warranty Card Mode <code>JButton</code>. */
	private JButton pbJPNMode = createButton("Japan Warranty Card Mode", 'J');
	
	/**
	 * Constructs a new <code>MFSWarrantyCardModeDialog</code>.
	 * @param parent the <code>MFSFrame</code> that caused this
	 *        <code>MFSWarrantyCardModeDialog</code> to be displayed
	 */
	public MFSWarrantyCardModeDialog(MFSFrame parent)
	{
		super(parent, "Warranty Card Printing");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		createLayout();
		addMyListeners();
	}
	
	/** Adds this dialog's <code>Component</code>s to the layout. */
	private void createLayout()
	{
		JLabel titleLabel = createLabel("Please select a Warranty Card data entry mode@");
		titleLabel.setForeground(MFSConstants.PRIMARY_FOREGROUND_HIGHLIGHT_COLOR);
		titleLabel.setFont(MFSConstants.MEDIUM_DIALOG_FONT);
		
		JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 0, 30));
		buttonPanel.setBackground(MFSConstants.PRIMARY_BACKGROUND_COLOR);
		
		buttonPanel.add(this.pbPRCMode);
		buttonPanel.add(this.pbJPNMode);
		
		JPanel contentPane = new JPanel(new GridBagLayout());
		contentPane.setBackground(MFSConstants.PRIMARY_BACKGROUND_COLOR);
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(50, 50, 15, 50);
		
		gbc.gridy++;
		contentPane.add(titleLabel, gbc);
		
		gbc.gridy++;
		gbc.insets.top = 15;
		gbc.insets.bottom = 50;
		contentPane.add(buttonPanel, gbc);
		
		setContentPane(contentPane);
		pack();
	}
	
	/** Adds the listeners to this dialog's <code>Component</code>s. */
	private void addMyListeners()
	{
		this.addKeyListener(this);
		this.pbPRCMode.addActionListener(this);
		this.pbPRCMode.addKeyListener(this);
		this.pbJPNMode.addActionListener(this);
		this.pbJPNMode.addKeyListener(this);
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
			if (source == this.pbPRCMode)
			{
				this.dispose();
				MFSWarrantyCardPRCDialog myPRCD = new MFSWarrantyCardPRCDialog(getParentFrame());
				myPRCD.setLocationRelativeTo(getParentFrame());
				myPRCD.setVisible(true);
			}
			else if (source == this.pbJPNMode)
			{
				this.dispose();
				MFSWarrantyCardJapanDialog myJPND = new MFSWarrantyCardJapanDialog(getParentFrame());
				myJPND.setLocationRelativeTo(getParentFrame());
				myJPND.setVisible(true);
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
			if (source == this.pbPRCMode)
			{
				this.pbPRCMode.requestFocusInWindow();
				this.pbPRCMode.doClick();
			}
			else if (source == this.pbJPNMode)
			{
				this.pbJPNMode.requestFocusInWindow();
				this.pbJPNMode.doClick();
			}
		}
		if (keyCode == KeyEvent.VK_ESCAPE)
		{
			this.dispose();
		}
	}
}
