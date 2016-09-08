/* © Copyright IBM Corporation 2005, 2007. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2007-02-21      34242JR  R Prechel        -Java 5 version
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.dialog;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import com.ibm.rchland.mfgapps.mfsclient.MFSDialog;
import com.ibm.rchland.mfgapps.mfsclient.MFSFrame;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSConstants;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSScroller;

/**
 * <code>MFSProdLineWIPDriverDialog</code> is the <code>MFSDialog</code>
 * used to select the product line used to create a new Part Movement work unit.
 * @author The MFS Client Development Team
 */
public class MFSProdLineWIPDriverDialog
	extends MFSDialog
{
	private static final long serialVersionUID = 1L;
	/** The <code>JList</code> that displays the product lines. */
	protected JList lstPrln = createList();

	/** The <code>JScrollPane</code> that contains the list of product lines. */
	protected JScrollPane spPrln = new JScrollPane(this.lstPrln,
			ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
			ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

	//Note: lstWipDrive/spWipDriver is not displayed on the GUI
	/** The <code>JList</code> for the WIP Driver options. */
	protected JList lstWipDriver = createList();

	/** The <code>JScrollPane</code> that contains the list of WIP Driver options. */
	protected JScrollPane spWipDriver = new JScrollPane(this.lstWipDriver,
			ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
			ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

	/** The Start WU <code>JButton</code>. */
	protected JButton pbStartWU = createButton("Start WU", 'S');

	/** The Cancel <code>JButton</code>. */
	protected JButton pbCancel = createButton("Cancel", 'n');

	/** Set <code>true</code> when the proceed button is selected. */
	protected boolean fieldPressedEnter = false;

	/** Stores the selected product line option. */
	protected String fieldPrln = ""; //$NON-NLS-1$

	/** Stores the selected WIP Driver option. */
	protected String fieldWIPDriver = ""; //$NON-NLS-1$

	/**
	 * Constructs a new <code>MFSProdLineWIPDriverDialog</code>.
	 * @param parent the <code>MFSFrame</code> that caused this
	 *        <code>MFSProdLineWIPDriverDialog</code> to be displayed
	 * @param prlnListModel the list model for the product line list
	 * @param wipDriverListModel the list model for the wip driver list
	 */
	public MFSProdLineWIPDriverDialog(MFSFrame parent, DefaultListModel prlnListModel,
										DefaultListModel wipDriverListModel)
	{
		super(parent, "Product Line Selection");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.lstPrln.setModel(prlnListModel);
		this.lstWipDriver.setModel(wipDriverListModel);
		createLayout();
		addMyListeners();
	}

	/** Adds this dialog's <code>Component</code>s to the layout. */
	protected void createLayout()
	{
		JLabel prlnLabel = createLabel("PRLN / DESCRIPTION List",
				MFSConstants.SMALL_DIALOG_FONT);

		JPanel contentPane = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 2, 1, 1, 0,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, 
				new Insets(40, 4, 4, 4), 0, 0);

		contentPane.add(prlnLabel, gbc);

		gbc.gridy++;
		gbc.weighty = 1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(4, 20, 4, 20);
		contentPane.add(this.spPrln, gbc);

		gbc.gridy++;
		gbc.gridwidth = 1;
		gbc.weighty = 0.0;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(20, 0, 30, 0);
		contentPane.add(this.pbStartWU, gbc);

		gbc.gridx = 1;
		contentPane.add(this.pbCancel, gbc);

		setContentPane(contentPane);
		setButtonDimensions(this.pbStartWU, this.pbCancel);
		pack();
	}

	/** Adds the listeners to this dialog's <code>Component</code>s. */
	protected void addMyListeners()
	{
		this.pbStartWU.addActionListener(this);
		this.pbCancel.addActionListener(this);

		this.pbStartWU.addKeyListener(this);
		this.pbCancel.addKeyListener(this);
		this.lstPrln.addKeyListener(this);
		this.spPrln.addKeyListener(this);
		this.lstWipDriver.addKeyListener(this);
		this.spWipDriver.addKeyListener(this);
	}

	/**
	 * Returns <code>true</code> iff the proceed button was selected.
	 * @return <code>true</code> iff the proceed button was selected
	 */
	public boolean getPressedEnter()
	{
		return this.fieldPressedEnter;
	}

	/**
	 * Returns the list option the user selected.
	 * @return the list option the user selected
	 */
	public String getPrln()
	{
		return this.fieldPrln;
	}

	/**
	 * Sets the default selection of the list.
	 * @param defaultSelection the default selection
	 */
	public void setDefaultSelection(String defaultSelection)
	{
		DefaultListModel listModel = (DefaultListModel) this.lstPrln.getModel();
		int index = listModel.indexOf(defaultSelection);
		if (index != -1)
		{
			this.lstPrln.setSelectedIndex(index);
			this.lstPrln.ensureIndexIsVisible(index);
			this.lstPrln.requestFocusInWindow();
		}
		else
		{
			this.lstPrln.setSelectedIndex(0);
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
			if (source == this.pbStartWU)
			{
				this.fieldPrln = (String) this.lstPrln.getSelectedValue();
				this.fieldWIPDriver = (String) this.lstWipDriver.getSelectedValue();
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
		final Object source = ke.getSource();
		if (keyCode == KeyEvent.VK_ENTER)
		{
			if (source == this.pbCancel)
			{
				this.pbCancel.requestFocusInWindow();
				this.pbCancel.doClick();
			}
			else
			{
				this.pbStartWU.requestFocusInWindow();
				this.pbStartWU.doClick();
			}
		}
		else if (keyCode == KeyEvent.VK_ESCAPE)
		{
			this.dispose();
		}
		else if (keyCode == KeyEvent.VK_PAGE_DOWN)
		{
			ke.consume();
			if (source == this.spWipDriver || source == this.lstWipDriver)
			{
				MFSScroller.scrollDown(this.spWipDriver, this.lstWipDriver);
			}
			else
			{
				MFSScroller.scrollDown(this.spPrln, this.lstPrln);
			}
		}
		else if (keyCode == KeyEvent.VK_PAGE_UP)
		{
			ke.consume();
			if (source == this.spWipDriver || source == this.lstWipDriver)
			{
				MFSScroller.scrollUp(this.spWipDriver, this.lstWipDriver);
			}
			else
			{
				MFSScroller.scrollUp(this.spPrln, this.lstPrln);
			}
		}
	}

	/**
	 * Invoked the first time a window is made visible.
	 * <p>
	 * Sets the selected index of {@link #lstWipDriver} to 0.
	 * @param we the <code>WindowEvent</code>
	 */
	public void windowOpened(WindowEvent we)
	{
		try
		{
			if (we.getSource() == this)
			{
				this.lstWipDriver.setSelectedIndex(0);
			}
		}
		catch (Throwable throwable)
		{
			throwable.printStackTrace();
		}
	}
}
