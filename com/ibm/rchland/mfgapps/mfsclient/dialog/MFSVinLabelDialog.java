/* © Copyright IBM Corporation 2005, 2007. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2007-01-28      34242JR  R Prechel        -Java 5 version
 * 2007-08-14   ~1 39572KM  R Prechel        -Remove label stock selection radio buttons
 *                                           -Fix clear button
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

import com.ibm.rchland.mfgapps.client.utils.messagebox.IGSMessageBox;
import com.ibm.rchland.mfgapps.mfsclient.MFSDialog;
import com.ibm.rchland.mfgapps.mfsclient.MFSFrame;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSConstants;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSPrintingMethods;

/**
 * <code>MFSVinLabelDialog</code> is the <code>MFSDialog</code> used to
 * collect input for and print the VIN Number Label.
 * @author The MFS Client Development Team
 */
public class MFSVinLabelDialog
	extends MFSDialog
{
	private static final long serialVersionUID = 1L;
	/** The Feature Number input <code>JTextField</code>. */
	private JTextField tfFeat;

	/** The Quantity input <code>JTextField</code>. */
	private JTextField tfQty;

	/** The Print <code>JButton</code>. */
	private JButton pbPrint = createButton("Print", 'P');

	/** The Clear <code>JButton</code>. */
	private JButton pbClear = createButton("Clear", 'C'); //~1C

	/** The Cancel <code>JButton</code>. */
	private JButton pbCancel = createButton("Cancel", 'n');

	/**
	 * Constructs a new <code>MFSVinLabelDialog</code>.
	 * @param parent the <code>MFSFrame</code> that caused this
	 *        <code>MFSVinLabelDialog</code> to be displayed
	 */
	public MFSVinLabelDialog(MFSFrame parent)
	{
		super(parent, "Print VIN Labels");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		createLayout();
		addMyListeners();
	}

	/** Adds this dialog's <code>Component</code>s to the layout. */
	private void createLayout()
	{
		JLabel featNumLabel = createLabel("Feature Number");
		this.tfFeat = createTextField(MFSConstants.SMALL_TF_COLS / 2, 0, featNumLabel);

		JLabel qtyLabel = createLabel("Quantity");
		this.tfQty = createTextField(MFSConstants.SMALL_TF_COLS / 2, 0, qtyLabel);

		//~1D Removed label stock selection radio buttons
		
		JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 30, 0));
		buttonPanel.add(this.pbPrint);
		buttonPanel.add(this.pbClear);
		buttonPanel.add(this.pbCancel);

		JPanel contentPane = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, 
				new Insets(20, 80, 10, 0), 0, 0);

		contentPane.add(featNumLabel, gbc);

		gbc.gridy++;
		gbc.insets = new Insets(0, 80, 10, 0);
		contentPane.add(qtyLabel, gbc);

		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.insets = new Insets(20, 10, 10, 70);
		contentPane.add(this.tfFeat, gbc);

		gbc.gridy++;
		gbc.insets = new Insets(0, 10, 10, 70);
		contentPane.add(this.tfQty, gbc);

		gbc.gridx = 0;
		gbc.gridy++;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.insets = new Insets(10, 20, 20, 20);
		contentPane.add(buttonPanel, gbc);

		setContentPane(contentPane);
		pack();
	}

	/** Adds the listeners to this dialog's <code>Component</code>s. */
	private void addMyListeners()
	{
		this.pbPrint.addActionListener(this);
		this.pbClear.addActionListener(this);
		this.pbCancel.addActionListener(this);

		this.pbPrint.addKeyListener(this);
		this.pbClear.addKeyListener(this); //~1A
		this.pbCancel.addKeyListener(this);
		this.tfFeat.addKeyListener(this);
		this.tfQty.addKeyListener(this);
	}

	/** Clears the text fields and requests the focus for the top text field. */
	private void clear()
	{
		this.tfQty.setText(""); //$NON-NLS-1$
		this.tfFeat.setText(""); //$NON-NLS-1$
		this.tfFeat.requestFocusInWindow();
	}

	/** Invoked when {@link #pbPrint} is selected. */
	private void print()
	{
		try
		{
			String feat = this.tfFeat.getText().trim();
			String qty = this.tfQty.getText();
			int rc = 0;
			String errorString = ""; //$NON-NLS-1$

			//~1C Use || instead of |
			if (feat.equals("") || feat.length() < 4 || feat.length() > 6) //$NON-NLS-1$
			{
				rc = 2;
				errorString = "Invalid Feature Number";
			}
			else
			{
				int quantity = 1;
				try
				{
					if (!qty.equals("")) //$NON-NLS-1$
					{
						quantity = java.lang.Integer.parseInt(this.tfQty.getText());
					}
				}
				catch (java.lang.NumberFormatException e)
				{
					rc = 1;
					errorString = "An Invalid Quantity was entered.  Please Enter a valid Quantity";
				}
				if (quantity == 0)
				{
					rc = 1;
					errorString = "0 is an Invalid Quantity.  Please Enter a valid Quantity";
				}

				if (rc == 0)
				{
					//~1D Remove label stock selection logic
					MFSPrintingMethods.vinlabel(feat, quantity, getParentFrame());
				}// end good return code
			}// end valid input

			if (rc != 0)
			{
				IGSMessageBox.showOkMB(this, null, errorString, null);
				this.toFront();

				// find the faulty field entered, clear it, and give it the focus
				if (rc == 1)
				{
					this.tfQty.selectAll();
					this.tfQty.requestFocusInWindow();
				}
				else
				{
					this.tfFeat.selectAll();
					this.tfFeat.requestFocusInWindow();
				}
			} // end bad return code
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(this, null, null, e);
			this.toFront();
			clear();
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
			else if (source == this.pbPrint)
			{
				print();
			}
			else if (source == this.pbClear)
			{
				clear();
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
			//~1A Check for pbClear
			else if (source == this.pbClear)
			{
				this.pbClear.requestFocusInWindow();
				this.pbClear.doClick();
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
	 * Requests the focus for {@link #tfFeat}.
	 * @param we the <code>WindowEvent</code>
	 */
	public void windowOpened(WindowEvent we)
	{
		if (we.getSource() == this)
		{
			this.tfFeat.requestFocusInWindow();
		}
	}
}
