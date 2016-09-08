/* © Copyright IBM Corporation 2005, 2010. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2007-02-21      34242JR  R Prechel        -Java 5 version
 * 2010-10-04   ~1 48749JL  Santiago SC      -New Methods for dialog reuse
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.dialog;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.util.List;

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
import com.ibm.rchland.mfgapps.mfsclient.MFSActionableDialog;
import com.ibm.rchland.mfgapps.mfsclient.MFSFrame;
import com.ibm.rchland.mfgapps.mfsclient.barcode.MFSBCBarcodeDecoder;
import com.ibm.rchland.mfgapps.mfsclient.barcode.MFSBCIndicatorValue;
import com.ibm.rchland.mfgapps.mfsclient.barcode.MFSBCPartObject;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSConstants;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSScroller;

/**
 * <code>MFSCntrDialog</code> is the <code>MFSActionableDialog</code> used
 * to select, reprint, and/or delete a container.
 * @author The MFS Client Development Team
 */
public class MFSCntrDialog
	extends MFSActionableDialog
{
	private static final long serialVersionUID = 1L;
	/** The Search <code>JTextField</code>. */
	private JTextField tfSearch;

	/** The <code>JList</code> that displays the options. */
	protected JList lstCntr = createList();

	/** The <code>JScrollPane</code> that contains the list of options. */
	protected JScrollPane spCntr = new JScrollPane(this.lstCntr,
			ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
			ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

	/** The Select <code>JButton</code>. */
	protected JButton pbSelect = createButton("Select", 'S');

	/** The Delete <code>JButton</code>. */
	protected JButton pbDelete = createButton("Delete", 'D');

	/** The Cancel <code>JButton</code>. */
	protected JButton pbCancel = createButton("Cancel", 'n');

	/** The Reprint <code>JButton</code>. */
	protected JButton pbReprint = createButton("Reprint", 'R');

	/** Stores the name of the button that was pressed. */
	protected String fieldButtonPressed = ""; //$NON-NLS-1$

	/** Stores the list option the user selected. */
	protected String fieldSelectedCntr = ""; //$NON-NLS-1$

	/**
	 * Constructs a new <code>MFSCntrDialog</code>.
	 * @param parent the <code>MFSFrame</code> that caused this
	 *        <code>MFSCntrDialog</code> to be displayed
	 */
	public MFSCntrDialog(MFSFrame parent)
	{
		super(parent, "Select the Container");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		createLayout();
		addMyListeners();
	}
	
	//~1A
	/**
	 * Sets visible a specific button in the dialog.
	 * @param buttonText the name of the button to show/hide
	 * @param visible either true or false
	 */
	public void setVisibleButton(String buttonText, boolean visible)
	{
		if(pbSelect.getText().equals(buttonText))
		{
			pbSelect.setVisible(visible);
		}
		else if(pbDelete.getText().equals(buttonText))
		{
			pbDelete.setVisible(visible);
		}
		else if(pbReprint.getText().equals(buttonText))
		{
			pbReprint.setVisible(visible);
		}
		else if(pbCancel.getText().equals(buttonText))
		{
			pbCancel.setVisible(visible);
		}
	}
	
	//~1A
	/**
	 * Loads the CNTR list model using the specified data.
	 * @param data the data used to load the CNTR list model
	 */
	public void loadCntrListModel(List<String> cntrList)
	{
		DefaultListModel listModel = new DefaultListModel();
		
		for(String cntr : cntrList)
		{
			listModel.addElement(cntr);
		}
		
		this.lstCntr.setModel(listModel);
		
		pack();
	}

	/** Adds this dialog's <code>Component</code>s to the layout. */
	protected void createLayout()
	{
		JLabel searchLabel = createLabel("Search:");
		this.tfSearch = createTextField(MFSConstants.SMALL_TF_COLS, 0, searchLabel);

		this.lstCntr.setVisibleRowCount(6);

		JPanel contentPane = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
				GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(20, 50, 0, 0), 0, 0);

		contentPane.add(searchLabel, gbc);

		gbc.gridx = 1;
		gbc.gridwidth = 2;
		gbc.weightx = 1.0;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(20, 5, 0, 50);
		contentPane.add(this.tfSearch, gbc);

		gbc.gridx = 0;
		gbc.gridy++;
		gbc.gridwidth = 3;
		gbc.weighty = 1.0;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(20, 50, 20, 50);
		contentPane.add(this.spCntr, gbc);

		gbc.gridy++;
		gbc.gridwidth = 1;
		gbc.weighty = 0.0;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(0, 5, 20, 5);
		contentPane.add(this.pbSelect, gbc);

		gbc.gridx = 1;
		contentPane.add(this.pbDelete, gbc);

		gbc.gridx = 2;
		contentPane.add(this.pbCancel, gbc);

		gbc.gridx = 1;
		gbc.gridy++;
		contentPane.add(this.pbReprint, gbc);

		setContentPane(contentPane);

		Dimension[] d = {
				this.pbSelect.getPreferredSize(), this.pbDelete.getPreferredSize(),
				this.pbCancel.getPreferredSize(), this.pbReprint.getPreferredSize()
		};

		int maxWidth = 0;
		int maxHeight = 0;
		for (int i = 0; i < 4; i++)
		{
			if (d[i].width > maxWidth)
			{
				maxWidth = d[i].width;
			}
			if (d[i].height > maxHeight)
			{
				maxHeight = d[i].height;
			}
		}

		this.pbSelect.setPreferredSize(new Dimension(maxWidth, maxHeight));
		this.pbDelete.setPreferredSize(new Dimension(maxWidth, maxHeight));
		this.pbCancel.setPreferredSize(new Dimension(maxWidth, maxHeight));
		this.pbReprint.setPreferredSize(new Dimension(maxWidth, maxHeight));
	}

	/** Adds the listeners to this dialog's <code>Component</code>s. */
	protected void addMyListeners()
	{
		this.pbSelect.addActionListener(this);
		this.pbDelete.addActionListener(this);
		this.pbCancel.addActionListener(this);
		this.pbReprint.addActionListener(this);

		this.pbSelect.addKeyListener(this);
		this.pbDelete.addKeyListener(this);
		this.pbCancel.addKeyListener(this);
		this.pbReprint.addKeyListener(this);
		this.lstCntr.addKeyListener(this);
		this.spCntr.addKeyListener(this);
		this.tfSearch.addKeyListener(this);
	}

	/**
	 * Returns the name of the button that was pressed.
	 * @return the name of the button that was pressed
	 */
	public String getButtonPressed()
	{
		return this.fieldButtonPressed;
	}

	/**
	 * Returns the list option the user selected.
	 * @return the list option the user selected
	 */
	public String getSelectedCntr()
	{
		return this.fieldSelectedCntr;
	}

	/**
	 * Loads the CNTR list model using the specified data.
	 * @param data the data used to load the CNTR list model
	 */
	public void loadCntrListModel(String data)
	{
		try
		{
			DefaultListModel listModel = new DefaultListModel();
			this.lstCntr.setModel(listModel);
			listModel.addElement("NEW       ");

			int start = 0;
			int end = 10;
			final int len = data.length();

			while (end < len)
			{
				listModel.addElement(data.substring(start, end));
				start += 10;
				end += 10;
			}

			if (len > 0)
			{
				listModel.addElement(data.substring(start));
			}
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
	private void search()
	{
		try
		{
			MFSBCBarcodeDecoder barcode = MFSBCBarcodeDecoder.getInstance();
			barcode.setMyBarcodeInput(this.tfSearch.getText().toUpperCase());
			barcode.setMyBCPartObject(new MFSBCPartObject());
			/* setup barcode indicator values */
			MFSBCIndicatorValue bcIndVal = new MFSBCIndicatorValue();
			barcode.setMyBCIndicatorValue(bcIndVal);

			barcode.decodeBarcodeFor(this);
			String match = "0000000"; //$NON-NLS-1$

			/* CNTR */
			if (!(barcode.getBCMyPartObject().getCT().equals(""))) //$NON-NLS-1$
			{
				match += barcode.getBCMyPartObject().getCT();
			}
			/* PN - 62 AI code */
			else if (!(barcode.getBCMyPartObject().getPN().equals(""))) //$NON-NLS-1$
			{
				match += barcode.getBCMyPartObject().getPN();
			}
			else
			{
				match += this.tfSearch.getText().toUpperCase();
			}

			/* match on last 6 chars */
			match = match.substring(match.length() - 6);

			int index = 0;
			boolean found = false;

			ListModel listModel = this.lstCntr.getModel();

			while (index < listModel.getSize() && !found)
			{
				String listStr = (String) listModel.getElementAt(index);
				if (listStr.substring(4).equals(match))
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
			this.lstCntr.setSelectedIndex(index);
			this.lstCntr.ensureIndexIsVisible(index);
			this.lstCntr.requestFocusInWindow();
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(this, null, null, e);
		}
	}

	/**
	 * Sets the selected index of the list.
	 * @param index the index to select
	 */
	public void setSelectedIndex(int index)
	{
		this.lstCntr.setSelectedIndex(index);
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
			if (source == this.pbSelect)
			{
				this.fieldSelectedCntr = (String) this.lstCntr.getSelectedValue();
				this.fieldButtonPressed = "Select"; //$NON-NLS-1$
				this.dispose();
			}
			else if (source == this.pbDelete)
			{
				this.fieldSelectedCntr = (String) this.lstCntr.getSelectedValue();
				this.fieldButtonPressed = "Delete"; //$NON-NLS-1$
				this.dispose();
			}
			else if (source == this.pbCancel)
			{
				this.fieldButtonPressed = "Cancel"; //$NON-NLS-1$
				this.dispose();
			}
			else if (source == this.pbReprint)
			{
				this.fieldSelectedCntr = (String) this.lstCntr.getSelectedValue();
				this.fieldButtonPressed = "Reprint"; //$NON-NLS-1$
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
			final Object source = ke.getSource();
			if (source == this.pbCancel)
			{
				this.pbCancel.requestFocusInWindow();
				this.pbCancel.doClick();
			}
			else if (source == this.pbDelete)
			{
				this.pbDelete.requestFocusInWindow();
				this.pbDelete.doClick();
			}
			else if (source == this.pbReprint)
			{
				this.pbReprint.requestFocusInWindow();
				this.pbReprint.doClick();
			}
			else if (source == this.tfSearch)
			{
				search();
			}
			else
			{
				this.pbSelect.requestFocusInWindow();
				this.pbSelect.doClick();
			}
		}
		else if (keyCode == KeyEvent.VK_ESCAPE)
		{
			this.dispose();
		}
		else if (keyCode == KeyEvent.VK_PAGE_DOWN)
		{
			MFSScroller.scrollDown(this.spCntr, this.lstCntr);
			ke.consume();
		}
		else if (keyCode == KeyEvent.VK_PAGE_UP)
		{
			MFSScroller.scrollUp(this.spCntr, this.lstCntr);
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
