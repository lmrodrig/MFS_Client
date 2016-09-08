/* © Copyright IBM Corporation 2005, 2007. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2007-02-21      34242JR  R Prechel        -Java 5 version
 * 2010-03-09	   47595MZ	Ray Perry		 -Shenzhen Efficiencies
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
import com.ibm.rchland.mfgapps.client.utils.messagebox.IGSMessageBox;
import com.ibm.rchland.mfgapps.mfsclient.MFSFrame;
import com.ibm.rchland.mfgapps.mfsclient.MFSListDialog;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSConstants;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSListSearch;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSScroller;

/**
 * <code>MFSProdLineDialog</code> is the <code>MFSListDialog</code> used to
 * select a product line.
 * @author The MFS Client Development Team
 */
public class MFSProdLineDialog
	extends MFSListDialog
{
	private static final long serialVersionUID = 1L;
	/** The *NONE list option. */
	public final String OPTION_NONE = "*NONE   ";

	/** The search input <code>JTextField</code>. */
	private JTextField tfSearch;

	/**
	 * Constructs a new <code>MFSProdLineDialog</code>.
	 * @param parent the <code>MFSFrame</code> that caused this
	 *        <code>MFSProdLineDialog</code> to be displayed
	 */
	public MFSProdLineDialog(MFSFrame parent)
	{
		super(parent, "Product Line");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		createLayout();
		addMyListeners();
	}

	/** Adds this dialog's <code>Component</code>s to the layout. */
	protected void createLayout()
	{
		JLabel searchLabel = createNameLabel("Search:");
		this.tfSearch = createTextField(MFSConstants.SMALL_TF_COLS, 0, searchLabel);

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
		gbc.insets = new Insets(20, 0, 0, 0);
		contentPane.add(this.spOptions, gbc);

		gbc.gridy++;
		gbc.gridwidth = 1;
		gbc.weighty = 0.0;
		gbc.fill = GridBagConstraints.NONE;
		this.pbProceed.setText("Enter (F2)");
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
	 * Loads the product line list model using the specified data.
	 * @param data the data used to load the product line list model
	 */
	public void loadPrlnListModel(String data)
	{
		try
		{
			DefaultListModel prlnListModel = new DefaultListModel();
			this.lstOptions.setModel(prlnListModel);

			prlnListModel.addElement(this.OPTION_NONE);
			int start = 0;
			int end = 8;
			final int len = data.length();

			while (end < len)
			{
				prlnListModel.addElement(data.substring(start, end));
				start += 8;
				end += 8;
			}
			prlnListModel.addElement(data.substring(start));
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
		finally
		{
			pack();
		}
	}
	
	/** Searchs the list for the search text. */
	public void search()
	{
		MFSListSearch.search(this.lstOptions, this.tfSearch);
	}

	/**
	 * Sets the text of the search <code>JTextField</code>.
	 * @param text the search text
	 */
	public void setSearchText(String text)
	{
		this.tfSearch.setText(text);
	}

	/**
	 * Sets the selected index of the list.
	 * @param index the index
	 */
	public void setSelectedIndex(int index)
	{
		this.lstOptions.setSelectedIndex(index);
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

			if (ke.getSource() == this.lstOptions)
			{
				this.pbProceed.requestFocusInWindow();
			}
			else if (ke.getSource() == this.pbCancel)
			{
				this.pbCancel.requestFocusInWindow();
				this.pbCancel.doClick();
			}
			else 
			{
				if (this.tfSearch.getText().equals("") //$NON-NLS-1$ 
						&& !this.lstOptions.getSelectedValue().equals(this.OPTION_NONE))
				{
					this.pbProceed.requestFocusInWindow();
					this.pbProceed.doClick();
				}
				else
				{
					search();
				}
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
		else if (keyCode == KeyEvent.VK_F2)
		{
			this.pbProceed.requestFocusInWindow();
			this.pbProceed.doClick();
		}
		else
		{
			if (ke.getSource() == this.tfSearch)
			{
				if (keyCode == KeyEvent.VK_BACK_SPACE)
				{
					MFSListSearch.search(this.lstOptions, this.tfSearch.getText().substring(0,this.tfSearch.getText().length()-1), false);
				}
				else
				{
					MFSListSearch.search(this.lstOptions, this.tfSearch.getText()+ke.getKeyChar(), false);
				}
			}
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
				if (this.lstOptions.getSelectedValue().equals(this.OPTION_NONE))
				{
					this.lstOptions.requestFocusInWindow();
				}
				else
				{
					this.pbProceed.requestFocusInWindow();
				}
			}
		}
		catch (Throwable throwable)
		{
			throwable.printStackTrace();
		}
	}
}
