/* @ Copyright IBM Corporation 2008, 2016. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2008-03-18      37616JL  R Prechel        -Initial version
 *                          D Pietrasik
 * 2016-02-18  ~1  1471226  Miguel Rivas     -getSelectedValues deprecated
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.dialog;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.List; //~1A

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import com.ibm.rchland.mfgapps.client.utils.messagebox.IGSMessageBox;
import com.ibm.rchland.mfgapps.mfsclient.MFSActionableDialog;
import com.ibm.rchland.mfgapps.mfsclient.MFSFrame;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSConstants;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSScroller;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFStfParser;
import com.ibm.rchland.mfgapps.mfscommon.exception.MFSException;

/**
 * <code>MFSGetMultiValueDialog</code> is the <code>MFSDialog</code> used to
 * prompt the user to enter a set of values.
 * @author The MFS Client Development Team
 */
public class MFSGetMultiValueDialog
	extends MFSActionableDialog
{
	private static final long serialVersionUID = 1L;
	/** The input <code>JTextField</code>. */
	private final JTextField tfInput = createTextField(MFSConstants.MEDIUM_TF_COLS, 0);

	/** The <code>JList</code> that displays the options. */
	private final JList lstOptions = createList();

	/** The <code>JScrollPane</code> that contains the list of options. */
	private final JScrollPane spOptions = new JScrollPane(this.lstOptions,
			ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
			ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

	/** The Proceed <code>JButton</code>. */
	private final JButton pbProceed;

	/** The Remove <code>JButton</code>. */
	private final JButton pbDelete = createButton("Remove", 'D');

	/** The Cancel <code>JButton</code>. */
	private final JButton pbCancel = createButton("Cancel", 'n');

	/** The <code>DefaultListModel</code> for {@link #lstOptions}. */
	private final DefaultListModel fieldListModel = new DefaultListModel();

	/** Set <code>true</code> when the proceed button is selected. */
	private boolean fieldProceedSelected = false;

	/** For parsing the text field entry */
	private MFStfParser textParser = null;

	/**
	 * Constructs a new <code>MFSGetMultiValueDialog</code>.
	 * @param parent the <code>MFSFrame</code> that caused this
	 *        <code>MFSDialog</code> to be displayed
	 * @param title the <code>String</code> to display in the title bar
	 * @param label the label text for the input <code>JTextField</code>
	 * @param buttonText the text for the proceed button
	 * @param buttonMnemonic the mnemonic for the proceed button
	 */
	public MFSGetMultiValueDialog(MFSFrame parent, String title, String label,
									String buttonText, char buttonMnemonic)
	{
		super(parent, title);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.pbProceed = createButton(buttonText, buttonMnemonic);
		this.lstOptions.setModel(this.fieldListModel);
		createLayout(label);
		addMyListeners();
	}

	/**
	 * Adds this dialog's <code>Component</code> s to the layout.
	 * @param labelText the label text for the input <code>JTextField</code>
	 */
	protected void createLayout(String labelText)
	{
		JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 20, 0));
		buttonPanel.add(this.pbProceed);
		buttonPanel.add(this.pbDelete);
		buttonPanel.add(this.pbCancel);

		JPanel contentPane = new JPanel(new GridBagLayout());
		contentPane.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 5), 0, 0);

		gbc.anchor = GridBagConstraints.EAST;
		contentPane.add(createNameLabel(labelText), gbc);

		gbc.gridx++;
		gbc.weightx = 1.0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets.right = 0;
		contentPane.add(this.tfInput, gbc);

		gbc.gridx--;
		gbc.gridy++;
		gbc.gridwidth = 2;
		gbc.weighty = 1.0;
		gbc.insets.top = 5;
		contentPane.add(this.spOptions, gbc);

		gbc.gridy++;
		gbc.weighty = 0.0;
		gbc.insets.top = 20;
		contentPane.add(buttonPanel, gbc);

		setContentPane(contentPane);
	}

	/** Adds the listeners to this dialog's <code>Component</code>s. */
	protected void addMyListeners()
	{
		this.pbProceed.addActionListener(this);
		this.pbDelete.addActionListener(this);
		this.pbCancel.addActionListener(this);

		this.pbProceed.addKeyListener(this);
		this.pbDelete.addKeyListener(this);
		this.pbCancel.addKeyListener(this);
		this.tfInput.addKeyListener(this);
		this.lstOptions.addKeyListener(this);
		this.spOptions.addKeyListener(this);
	}

	/**
	 * Validates and parses the user input.
	 * @return <code>true</code> if the input was valid; <code>false</code>
	 *         otherwise
	 */
	private boolean parseInput()
	{
		boolean result = true;
		try
		{
			String input = this.textParser.recvInput(this.tfInput.getText());
			if ((input != null) && (input.length() > 0))
			{
				if (!this.fieldListModel.contains(input))
				{
					this.fieldListModel.addElement(input);
				}
				else
				{
					result = false;
					String title = "Invalid Input";
					String erms = "''{0}'' is already in the list.";
					Object[] parms = {
						input
					};
					erms = MessageFormat.format(erms, parms);
					IGSMessageBox.showOkMB(this, title, erms, null);
				}

				if (result)
				{
					this.tfInput.setText(""); //$NON-NLS-1$
				}
			}
		}
		catch (MFSException ex)
		{
			String title = "Invalid Input";
			IGSMessageBox.showOkMB(this, title, ex.getMessage(), null);
			result = false;
		}
		return result;
	}

	/** Clears the values entered by the user. */
	public void clearInputValues()
	{
		this.fieldListModel.clear();
	}

	/**
	 * Returns an <code>Enumeration</code> over the values entered by the
	 * user.
	 * @return the <code>Enumeration</code>
	 */
	@SuppressWarnings("rawtypes")
	public Enumeration getInputValues()
	{
		return this.fieldListModel.elements();
	}

	/**
	 * Returns <code>true</code> iff the proceed button was selected.
	 * @return <code>true</code> iff the proceed button was selected
	 */
	public boolean getProceedSelected()
	{
		return this.fieldProceedSelected;
	}

	/**
	 * Sets the preferred number of rows in the list that can be displayed
	 * without a scrollbar.
	 * @param visibleRowCount the preferred number of visible rows
	 */
	public void setVisibleRowCount(int visibleRowCount)
	{
		this.lstOptions.setVisibleRowCount(visibleRowCount);
	}

	/**
	 * Sets the prototype cell value for the <code>JList</code>.
	 * @param value the prototype cell value
	 * @see JList#setPrototypeCellValue(Object)
	 */
	public void setPrototypeCellValue(Object value)
	{
		this.lstOptions.setPrototypeCellValue(value);
	}

	/**
	 * The the MFStfParser to use parsing the text field
	 * @param parse parser to use
	 */
	public void setTextParser(MFStfParser parse)
	{
		this.textParser = parse;
	}
	

	/**
	 * Invoked when an action occurs.
	 * @param ae the <code>ActionEvent</code>
	 */
	public void actionPerformed(ActionEvent ae)
	{
		final Object source = ae.getSource();
		if (source == this.pbProceed)
		{
			if (parseInput())
			{
				if (this.fieldListModel.size() == 0)
				{
					String title = "Selection Error";
					String erms = "Please enter at least one value or Cancel.";
					IGSMessageBox.showOkMB(getParent(), title, erms, null);
				}
				else
				{
					this.fieldProceedSelected = true;
					dispose();
				}
			}
		}
		else if (source == this.pbDelete)
		{
			/* ~1
			  
			Object selected[] = this.lstOptions.getSelectedValues();
			for (int i = 0; i < selected.length; i++)
			{
				this.fieldListModel.removeElement(selected[i]);
			}
			*/
			List selected = this.lstOptions.getSelectedValuesList();
			for (int i = 0; i < selected.size(); i++)
			{
				this.fieldListModel.removeElement(selected.get(i));
			}
		}
		else if (source == this.pbCancel)
		{
			this.fieldProceedSelected = false;
			dispose();
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
			if (source == this.tfInput)
			{
				parseInput();
			}
			else if (source instanceof JButton)
			{
				JButton button = (JButton) source;
				button.requestFocusInWindow();
				button.doClick();
			}
		}
		else if (keyCode == KeyEvent.VK_ESCAPE)
		{
			this.fieldProceedSelected = false;
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
	 * Requests the focus for {@link #tfInput}.
	 * @param we the <code>WindowEvent</code>
	 */
	public void windowOpened(WindowEvent we)
	{
		if (we.getSource() == this)
		{
			this.tfInput.requestFocusInWindow();
		}
	}

	/**
	 * Invoked when the user attempts to close the window from the window's
	 * system menu. Does nothing.
	 * @param we the <code>WindowEvent</code>
	 */
	public void windowClosing(WindowEvent we)
	{
		this.fieldProceedSelected = false;
		super.windowClosing(we);
	}
}
