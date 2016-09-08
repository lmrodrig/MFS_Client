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

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.ScrollPaneConstants;

import com.ibm.rchland.mfgapps.client.utils.messagebox.IGSMessageBox;
import com.ibm.rchland.mfgapps.mfsclient.MFSDialog;
import com.ibm.rchland.mfgapps.mfsclient.MFSFrame;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSConstants;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSScroller;

/**
 * <code>MFSDX10Dialog</code> displays the options retrieved by RTV_DX10.
 * @author The MFS Client Development Team
 */
public class MFSDX10Dialog
	extends MFSDialog
{
	private static final long serialVersionUID = 1L;
	/** The search input <code>JTextField</code>. */
	private JTextField tfSearch = null;
	
	/** The <code>JList</code> that displays the options. */
	protected JList lstReason = createList();

	/** The <code>JScrollPane</code> that contains the list of options. */
	protected JScrollPane spReason = new JScrollPane(this.lstReason,
			ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
			ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

	/** The <code>JButton</code> the user selects to proceed with the selection of an option. */
	protected JButton pbEnter = createButton("Enter", 'E');

	/** The <code>JButton</code> the user selects to cancel and not select an option. */
	protected JButton pbCancel = createButton("Cancel", 'n');

	/** Set <code>true</code> when the enter button is pressed. */
	protected boolean fieldPressedEnter = false;

	/** Stores the list option the user selected. */
	protected String fieldReason = ""; //$NON-NLS-1$
	
	/**
	 * Constructs a new <code>MFSDX10Dialog</code>.
	 * @param parent the <code>MFSFrame</code> that caused this
	 *        <code>MFSDX10Dialog</code> to be displayed
	 * @param listModel the <code>DefaultListModel</code> for the <code>JList</code>
	 */
	public MFSDX10Dialog(MFSFrame parent, DefaultListModel listModel)
	{
		super(parent, "Part Checking Option - DX10");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.lstReason.setModel(listModel);
		createLayout();
		addMyListeners();
	}
	
	/** Adds this dialog's <code>Component</code>s to the layout. */
	protected void createLayout()
	{
		JLabel searchLabel = createLabel("Search:");
		this.tfSearch = createTextField(MFSConstants.SMALL_TF_COLS, 0, searchLabel);
		
		JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 30, 0));
		buttonPanel.add(this.pbEnter);
		buttonPanel.add(this.pbCancel);
		
		JPanel contentPane = new JPanel(new GridBagLayout());
		contentPane.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 5), 0, 0);

		contentPane.add(searchLabel, gbc);
		
		gbc.gridx = 1;
		gbc.weightx = 1.0;
		gbc.insets = new Insets(0, 0, 0, 0);
		contentPane.add(this.tfSearch, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 2;
		gbc.weighty = 1.0;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(20, 0, 0, 0);
		contentPane.add(this.spReason, gbc);

		gbc.gridy++;
		gbc.weighty = 0.0;
		gbc.fill = GridBagConstraints.NONE;
		contentPane.add(buttonPanel, gbc);

		setContentPane(contentPane);
		
		Dimension d = this.tfSearch.getPreferredSize();
		this.tfSearch.setMinimumSize(new Dimension(d));
		setSize(470, 460);
	}
	
	/** Adds the listeners to this dialog's <code>Component</code>s. */
	protected void addMyListeners()
	{
		this.pbEnter.addActionListener(this);
		this.pbCancel.addActionListener(this);

		this.pbEnter.addKeyListener(this);
		this.pbCancel.addKeyListener(this);
		this.tfSearch.addKeyListener(this);
		this.lstReason.addKeyListener(this);
		this.spReason.addKeyListener(this);
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
	 * Returns the reason the user selected.
	 * @return the reason the user selected
	 */
	public String getReason()
	{
		return this.fieldReason;
	}
	
	/** Searchs the list for the search text. */
	public void search() 
	{
		try
		{
			String match = this.tfSearch.getText().toUpperCase();
			int index = 1;
			boolean found = false;
			
			ListModel model = this.lstReason.getModel();
			
			while (index < model.getSize() && !found)
			{
				String listStr = (String)model.getElementAt(index);
				if (listStr.substring(0,10).trim().equals(match))
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
			this.lstReason.setSelectedIndex(index);
			this.lstReason.ensureIndexIsVisible(index);
			this.lstReason.requestFocusInWindow();
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(this, null, null, e);
		}
	}
	
	/**
	 * Sets the text of the search <code>JTextField</code>
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
		this.lstReason.setSelectedIndex(index);
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
			if (source == this.pbEnter)
			{
				this.fieldReason = (String) this.lstReason.getSelectedValue();
				this.fieldPressedEnter = true;
				this.dispose();
			}
			else if (source == this.pbCancel)
			{
				this.dispose();
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
			if (ke.getSource() != this.pbCancel)
			{
				if (this.tfSearch.getText().equals("") //$NON-NLS-1$
						&& !this.lstReason.getSelectedValue().equals("*NONE   ")) //$NON-NLS-1$
				{
					this.pbEnter.requestFocusInWindow();
					this.pbEnter.doClick();
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
			MFSScroller.scrollDown(this.spReason, this.lstReason);
			ke.consume();
		}
		else if (keyCode == KeyEvent.VK_PAGE_UP)
		{
			MFSScroller.scrollUp(this.spReason, this.lstReason);
			ke.consume();
		}
	}

	/**
	 * Invoked the first time a window is made visible. Requests the focus for
	 * {@link #tfSearch}.
	 * @param we the <code>WindowEvent</code>
	 */
	public void windowOpened(WindowEvent we)
	{	
		if (we.getSource() == this)
		{
			this.tfSearch.requestFocusInWindow();
		}
	}
}
