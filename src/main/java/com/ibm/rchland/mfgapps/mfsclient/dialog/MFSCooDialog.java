/* @ Copyright IBM Corporation 2005, 2010. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2007-03-13      34242JR  R Prechel        -Java 5 version
 * 2010-05-17 ~01  46870JL  Edgar Mercado    -Modify loadCooListModel method to get CNAMETXT and CNAME
 *                                            with the correct length.
 * 2010-05-20 ~02  46870JL  Toribio H        - PTR Openned to fix an error introduced by previous flag.
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.dialog;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListModel;

import com.ibm.rchland.mfgapps.client.utils.messagebox.IGSMessageBox;
import com.ibm.rchland.mfgapps.mfsclient.MFSFrame;
import com.ibm.rchland.mfgapps.mfsclient.MFSListDialog;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSConstants;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSScroller;

/**
 * <code>MFSCooDialog</code> is the <code>MFSListDialog</code> used to
 * select the country of origin.
 * @author The MFS Client Development Team
 */
public class MFSCooDialog
	extends MFSListDialog
{
	private static final long serialVersionUID = 1L;
	/** The blank country of origin. */
	private static final String BLANK_COO = 
		"       No Country                                     "; //$NON-NLS-1$
	
	//The search field is not always created.
	//Check against null before using
	/** The search input <code>JTextField</code>. */
	private JTextField tfSearch = null;

	/**
	 * Constructs a new <code>MFSCooDialog</code>.
	 * @param parent the <code>MFSFrame</code> that caused this
	 *        <code>MFSCooDialog</code> to be displayed
	 * @param searchable <code>true</code> if the list of countries is
	 *        searchable
	 */
	public MFSCooDialog(MFSFrame parent, boolean searchable)
	{
		super(parent, "Countries of Origin"); //$NON-NLS-1$
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		if(searchable)
		{
			this.tfSearch = createTextField(MFSConstants.LARGE_TF_COLS, 0);
		}
		
		createLayout();
		addMyListeners();
	}
	
	/** Adds this dialog's <code>Component</code>s to the layout. */
	protected void createLayout()
	{
		JPanel contentPane = new JPanel(new GridBagLayout());
		contentPane.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 2, 1, 1.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0);

		if (this.tfSearch != null)
		{
			contentPane.add(this.tfSearch, gbc);
			gbc.gridy++;
		}

		gbc.fill = GridBagConstraints.BOTH;
		gbc.weighty = 1.0;
		contentPane.add(this.spOptions, gbc);

		gbc.gridy++;
		gbc.gridwidth = 1;
		gbc.weighty = 0.0;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(20, 0, 0, 0);
		contentPane.add(this.pbProceed, gbc);

		gbc.gridx = 1;
		contentPane.add(this.pbCancel, gbc);

		setContentPane(contentPane);
		setButtonDimensions(this.pbProceed, this.pbCancel);
	}
	
	/** Adds the listeners to this dialog's <code>Component</code>s. */
	public void addMyListeners()
	{
		if (this.tfSearch != null)
		{
			this.tfSearch.addKeyListener(this);
		}
		super.addMyListeners();
	}

	/**
	 * Loads the Country of Origin list using the specified data.
	 * @param data the data used to load the Country of Origin list
	 */
	public void loadCooListModel(String data)
	{
		try
		{
			int start = 0;
			int end = 0;                                                               //~01C~02C
			/** This function is called by other classes different than FRU Dialog
			 *    previous record length was 47, After updating RTV_CNTRY is 107 
			 *    this fix is needed urgently because we need to move to UAT in a 
			 *    couple of hours. So I'm going to handle it as simple patch.
			 *    I recommend a more structured solution in the future. */
			int recordLength = 47;
			final int len = data.length();
			
			if(len % 47 != 0)
			{
				recordLength = 107;
			}
			DefaultListModel listModel = new DefaultListModel();
			this.lstOptions.setModel(listModel);

			end = recordLength;
			while (end < len)
			{
				String info = data.substring(start, end);
				listModel.addElement(info);
				start += recordLength;                                                  //~01C~02C
				end += recordLength;                                                    //~01C~02C
			}
			
			String info = data.substring(start, end);
			listModel.addElement(info);

			/* now load a blank country code */
			listModel.addElement(BLANK_COO);
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

	/** Searchs the country list for the search text. */
	public void search()
	{
		try
		{
			if (this.tfSearch != null && !this.tfSearch.getText().equals("")) //$NON-NLS-1$
			{
				String match = this.tfSearch.getText().toUpperCase();
				int index = 0;
				boolean found = false;

				final ListModel listModel = this.lstOptions.getModel();

				while (index < listModel.getSize() && !found)
				{
					String listStr = listModel.getElementAt(index).toString().toUpperCase();

					if (listStr.substring(7, 7 + match.length()).trim().equals(match))
					{
						found = true;
					}
					else
					{
						index++;
					}
				}

				if (!found)
				{
					index = 0;
				}
				this.tfSearch.setText(""); //$NON-NLS-1$
				this.lstOptions.setSelectedIndex(index);
				this.lstOptions.ensureIndexIsVisible(index);
				this.lstOptions.requestFocusInWindow();
			}
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(this, null, null, e);
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
			if (ke.getSource() != this.pbCancel)
			{
				if (this.tfSearch == null || this.tfSearch.getText().equals("")) //$NON-NLS-1$
				{
					this.pbProceed.requestFocusInWindow();
					this.pbProceed.doClick();
				}
				else
				{
					this.search();
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
	 * @param we the <code>WindowEvent</code>
	 */
	public void windowOpened(WindowEvent we)
	{
		if (we.getSource() == this)
		{
			this.lstOptions.setSelectedIndex(0);

			if (this.tfSearch != null)
			{
				this.tfSearch.requestFocusInWindow();
			}
			else
			{
				this.lstOptions.requestFocusInWindow();
			}
		}
	}
}
