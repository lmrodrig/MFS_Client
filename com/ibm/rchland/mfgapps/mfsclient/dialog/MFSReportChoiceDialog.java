/* © Copyright IBM Corporation 2005, 2007. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2007-01-25      34242JR  R Prechel        -Java 5 version
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
 * <code>MFSReportChoiceDialog</code> is an <code>MFSDialog</code> that allows
 * the user to select the type of report to print.
 * @author The MFS Client Development Team
 */
public class MFSReportChoiceDialog
	extends MFSDialog
{
	private static final long serialVersionUID = 1L;
	/** The summary <code>JRadioButton</code>. */
	private JRadioButton rbSummary = new JRadioButton("Summary", true);
	
	/** The detail <code>JRadioButton</code>. */
	private JRadioButton rbDetail = new JRadioButton("Detail");
	
	/** The both <code>JRadioButton</code>. */
	private JRadioButton rbBoth = new JRadioButton("Both");
	
	/** The print <code>JButton</code>. */
	private JButton pbPrint = createButton("Print", 'P');

	/** The cancel <code>JButton</code>. */
	private JButton pbCancel = createButton("Cancel", 'n');
	
	/** Set <code>true</code> iff the print button is pressed. */
	private boolean fieldPressedPrint = false;
	
	/**
	 * Constructs a new <code>MFSReportChoiceDialog</code>.
	 * @param parent the <code>MFSFrame</code> that caused this
	 *        <code>MFSReportChoiceDialog</code> to be displayed
	 */
	public MFSReportChoiceDialog(MFSFrame parent)
	{
		super(parent, "Specify Report Type");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		createLayout();
		addMyListeners();
	}

	/** Adds this dialog's <code>Component</code>s to the layout. */
	private void createLayout()
	{
		this.rbSummary.setMnemonic('S');
		this.rbDetail.setMnemonic('D');
		this.rbBoth.setMnemonic('B');
		
		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(this.rbSummary);
		buttonGroup.add(this.rbDetail);
		buttonGroup.add(this.rbBoth);
		
		JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 5, 0));
		buttonPanel.add(this.rbSummary);
		buttonPanel.add(this.rbDetail);
		buttonPanel.add(this.rbBoth);
		
		JPanel contentPane = new JPanel(new GridBagLayout());
		contentPane.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 2, 1, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0);

		contentPane.add(buttonPanel, gbc);
		
		gbc.gridy++;
		gbc.gridwidth = 1;
		gbc.weighty = 0.0;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(20, 0, 0, 10);
		contentPane.add(this.pbPrint, gbc);
		
		gbc.gridx = 1;
		gbc.insets = new Insets(20, 10, 0, 0);
		contentPane.add(this.pbCancel, gbc);
		
		setContentPane(contentPane);
		setButtonDimensions(this.pbPrint, this.pbCancel);
		pack();
	}

	/** Adds the listeners to this dialog's <code>Component</code>s. */
	private void addMyListeners()
	{
		this.pbPrint.addActionListener(this);
		this.pbCancel.addActionListener(this);
		
		this.pbPrint.addKeyListener(this);
		this.pbCancel.addKeyListener(this);
		this.rbBoth.addKeyListener(this);
		this.rbDetail.addKeyListener(this);
		this.rbSummary.addKeyListener(this);
	}
	
	/**
	 * Returns <code>true</code> iff the print button was pressed.
	 * @return <code>true</code> iff the print button was pressed
	 */
	public boolean getPressedPrint()
	{
		return this.fieldPressedPrint;
	}
	
	/**
	 * Returns "S", "D", or "B" to indicate which <code>JRadioButton</code> was selected.
	 * @return "S", "D", or "B"
	 */
	public String getSelectionType()
	{
		String type;
		if (this.rbSummary.isSelected())
		{
			type = "S"; //$NON-NLS-1$
		}
		else if (this.rbDetail.isSelected())
		{
			type = "D"; //$NON-NLS-1$
		}
		else
		{
			type = "B"; //$NON-NLS-1$
		}
		return type;
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
			if(source == this.pbPrint)
			{
				this.fieldPressedPrint = true;
				dispose();
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
		final Object source = ke.getSource();
		if(keyCode == KeyEvent.VK_ENTER)
		{
			if(source == this.pbCancel)
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
		/* Arrow up or left -> go up the radio buttons */
		else if (keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_LEFT)
		{
			if(source == this.rbSummary)
			{
				this.rbBoth.requestFocusInWindow();
				this.rbBoth.setSelected(true);
			}
			else if(source == this.rbDetail)
			{
				this.rbSummary.requestFocusInWindow();
				this.rbSummary.setSelected(true);
			}
			else if (source == this .rbBoth)
			{
				this.rbDetail.requestFocusInWindow();
				this.rbDetail.setSelected(true);
			}
		}
		/* Arrow down or right -> go down the radio buttons */
		else if (keyCode == KeyEvent.VK_DOWN || keyCode == KeyEvent.VK_RIGHT)
		{
			if(source == this.rbSummary)
			{
				this.rbDetail.requestFocusInWindow();
				this.rbDetail.setSelected(true);
			}
			else if(source == this.rbDetail)
			{
				this.rbBoth.requestFocusInWindow();
				this.rbBoth.setSelected(true);
			}
			else if (source == this .rbBoth)
			{
				this.rbSummary.requestFocusInWindow();
				this.rbSummary.setSelected(true);
			}
		}
	}
	
	/**
	 * Invoked the first time a window is made visible.
	 * <p>
	 * Requests the focus for {@link #pbPrint}.
	 * @param we the <code>WindowEvent</code>
	 */
	public void windowOpened(WindowEvent we)
	{
		if (we.getSource() == this)
		{
			this.pbPrint.requestFocusInWindow();
		}
	}
}
