/* @ Copyright IBM Corporation 2005, 2007. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2007-02-22      34242JR  R Prechel        -Java 5 version
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.dialog;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.ibm.rchland.mfgapps.mfsclient.MFSFrame;
import com.ibm.rchland.mfgapps.mfsclient.MFSListDialog;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSConstants;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSListSearch;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSScroller;

/**
 * <code>MFSWipDriverDialog</code> is the <code>MFSListDialog</code> used to
 * display WIP drivers.
 * @author The MFS Client Development Team
 */
public class MFSWipDriverDialog
	extends MFSListDialog
{
	private static final long serialVersionUID = 1L;
	/** The search input <code>JTextField</code>. */
	private JTextField tfSearch;

	/**
	 * Constructs a new <code>MFSWipDriverDialog</code>.
	 * @param parent the <code>MFSFrame</code> that caused this
	 *        <code>MFSWipDriverDialog</code> to be displayed
	 * @param listModel the populated <code>DefaultListModel</code> used by
	 *        this dialog's <code>JList</code>
	 */
	public MFSWipDriverDialog(MFSFrame parent, DefaultListModel listModel)
	{
		super(parent, "WIP Driver");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.lstOptions.setModel(listModel);
		this.lstOptions.setSelectedIndex(0);
		createLayout();
		addMyListeners();
		pack();
	}

	/** Adds this dialog's <code>Component</code>s to the layout. */
	protected void createLayout()
	{
		JLabel searchLabel = createNameLabel("Search:");
		this.tfSearch = createTextField(MFSConstants.SMALL_TF_COLS, 12, searchLabel);

		JPanel contentPane = new JPanel(new GridBagLayout());
		contentPane.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.NONE, 
				new Insets(0, 2, 0, 2), 0, 0);

		contentPane.add(searchLabel, gbc);

		gbc.gridx = 1;
		gbc.anchor = GridBagConstraints.WEST;
		contentPane.add(this.tfSearch, gbc);

		gbc.gridx = 0;
		gbc.gridy++;
		gbc.gridwidth = 2;
		gbc.weighty = 1.0;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(10, 0, 0, 0);
		contentPane.add(this.spOptions, gbc);

		gbc.gridy++;
		gbc.gridwidth = 1;
		gbc.weighty = 0.0;
		gbc.fill = GridBagConstraints.NONE;
		contentPane.add(this.pbProceed, gbc);

		gbc.gridx = 1;
		contentPane.add(this.pbCancel, gbc);

		setContentPane(contentPane);
		setButtonDimensions(this.pbProceed, this.pbCancel);
	}

	/** Adds the listeners to this dialog's <code>Component</code>s. */
	protected void addMyListeners()
	{
		this.tfSearch.addKeyListener(this);
		super.addMyListeners();
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
			if (ke.getSource() != this.pbCancel)
			{
				if (this.tfSearch.getText().equals("")) //$NON-NLS-1$ 

				{
					this.pbProceed.requestFocusInWindow();
					this.pbProceed.doClick();
				}
				else
				{
					MFSListSearch.search(this.lstOptions, this.tfSearch);
				}
			}
			else
			{
				this.pbCancel.requestFocusInWindow();
				this.pbCancel.doClick();
			}
		}
		else if (keyCode == KeyEvent.VK_ESCAPE)
		{
			this.dispose();
		}
		else if (keyCode == KeyEvent.VK_PAGE_DOWN)
		{
			MFSScroller.scrollDown(this.spOptions, this.lstOptions);
			ke.consume();
		}
		else if (keyCode == KeyEvent.VK_PAGE_UP)
		{
			MFSScroller.scrollUp(this.spOptions, this.lstOptions);
			ke.consume();
		}
	}

	/**
	 * Invoked the first time a window is made visible.
	 * <p>
	 * Requests the focus for {@link #tfSearch}.
	 * @param we the <code>WindowEvent</code>
	 */
	public void windowOpened(WindowEvent we)
	{
		try
		{
			if (we.getSource() == this)
			{
				this.tfSearch.requestFocusInWindow();
			}
		}
		catch (Throwable throwable)
		{
			throwable.printStackTrace();
		}
	}
}
