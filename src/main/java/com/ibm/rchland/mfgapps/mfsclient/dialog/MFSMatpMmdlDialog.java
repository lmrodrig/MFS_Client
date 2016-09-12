/* @ Copyright IBM Corporation 2006, 2007. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2006-09-13      35701DB  lmrodrig         -Initial version
 * 2007-01-28   ~1 34242JR  R Prechel        -Java 5 version
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.dialog;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.ibm.rchland.mfgapps.client.utils.messagebox.IGSMessageBox;
import com.ibm.rchland.mfgapps.mfsclient.MFSDialog;
import com.ibm.rchland.mfgapps.mfsclient.MFSFrame;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSConstants;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSScroller;

/**
 * <code>MFSMatpMmdlDialog</code> is the <code>MFSDialog</code> used to
 * print a 1S label.
 * @author The MFS Client Development Team
 */
public class MFSMatpMmdlDialog
	extends MFSDialog
	implements ListSelectionListener
{
	private static final long serialVersionUID = 1L;
	/** The <code>JList</code> that displays the machine types. */
	private JList lstMatp = createList();

	/** The <code>JScrollPane</code> that contains the list of machine types. */
	private JScrollPane spMatp = new JScrollPane(this.lstMatp,
			ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
			ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

	/** The <code>JList</code> that displays the models. */
	private JList lstMdl = createList();

	/** The <code>JScrollPane</code> that contains the list of models. */
	private JScrollPane spMdl = new JScrollPane(this.lstMdl,
			ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
			ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

	/** The Label Quantity input <code>JTextField</code>. */
	private JTextField tfQty;

	/** The Print <code>JButton</code>. */
	private JButton pbPrint = createButton("Print", 'P');

	/** The Cancel <code>JButton</code>. */
	private JButton pbCancel = createButton("Cancel", 'n');

	/** The <code>Vector</code> of Machine Type/Model data. */
	@SuppressWarnings("rawtypes")
	private Vector fieldMatpMdlVector = new Vector();

	/** Set <code>true</code> when the Print button is selected. */
	private boolean fieldPressedPrint = false;

	/**
	 * Constructs a new <code>MFSMatpMmdlDialog</code>.
	 * @param parent the <code>MFSFrame</code> that caused this
	 *        <code>MFSMatpMmdlDialog</code> to be displayed
	 * @param data the machine type/model data
	 */
	public MFSMatpMmdlDialog(MFSFrame parent, String data)
	{
		super(parent, "1S Label Selection");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.lstMatp.setModel(new DefaultListModel());
		this.lstMdl.setModel(new DefaultListModel());
		parseData(data);
		createLayout();
		addMyListeners();
	}

	/** Adds this dialog's <code>Component</code>s to the layout. */
	private void createLayout()
	{
		JLabel matpLabel = createLabel("Machine Type:", MFSConstants.SMALL_DIALOG_FONT);
		JLabel mdlLabel = createLabel("Model:", MFSConstants.SMALL_DIALOG_FONT);
		JLabel qtyLabel = createLabel("Label Quantity: ", MFSConstants.SMALL_DIALOG_FONT);

		this.spMatp.setPreferredSize(new Dimension(100, 110));
		this.spMdl.setPreferredSize(new Dimension(100, 110));

		this.tfQty = createTextField(MFSConstants.SMALL_TF_COLS, 2, qtyLabel);
		this.tfQty.setText("1");

		JPanel contentPane = new JPanel(new GridBagLayout());
		contentPane.setBorder(BorderFactory.createEmptyBorder(45, 50, 30, 50));
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(0, 14, 0, 14), 0, 0);

		contentPane.add(matpLabel, gbc);

		gbc.gridx = 1;
		contentPane.add(mdlLabel, gbc);

		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		contentPane.add(this.spMatp, gbc);

		gbc.gridx = 1;
		contentPane.add(this.spMdl, gbc);

		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(25, 5, 0, 10);
		contentPane.add(qtyLabel, gbc);

		gbc.gridx = 1;
		contentPane.add(this.tfQty, gbc);

		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.insets = new Insets(5, 10, 0, 10);
		contentPane.add(this.pbPrint, gbc);

		gbc.gridx = 1;
		contentPane.add(this.pbCancel, gbc);

		setContentPane(contentPane);
		this.pbCancel.setMargin(new Insets(2, 20, 2, 20));
		setButtonDimensions(this.pbPrint, this.pbCancel);
		pack();
	}

	/** Adds the listeners to this dialog's <code>Component</code>s. */
	private void addMyListeners()
	{
		this.pbPrint.addActionListener(this);
		this.pbCancel.addActionListener(this);
		this.tfQty.addActionListener(this);

		this.pbPrint.addKeyListener(this);
		this.pbCancel.addKeyListener(this);
		this.lstMatp.addKeyListener(this);
		this.spMatp.addKeyListener(this);
		this.lstMdl.addKeyListener(this);
		this.spMdl.addKeyListener(this);
		this.tfQty.addKeyListener(this);

		this.lstMatp.addListSelectionListener(this);
	}

	/**
	 * Returns <code>true</code> iff the Print button was selected.
	 * @return <code>true</code> iff the Print button was selected
	 */
	public boolean getPressedPrint()
	{
		return this.fieldPressedPrint;
	}

	/**
	 * Returns the selected value of {@link #lstMatp}.
	 * @return the selected value of {@link #lstMatp}
	 */
	public String getSelectedMatp()
	{
		return this.lstMatp.getSelectedValue().toString();
	}

	/**
	 * Returns the selected value of {@link #lstMdl}.
	 * @return the selected value of {@link #lstMdl}
	 */
	public String getSelectedMdl()
	{
		return this.lstMdl.getSelectedValue().toString();
	}

	/**
	 * Returns the text of {@link #tfQty}.
	 * @return the text of {@link #tfQty}
	 */
	public String getQuantityText()
	{
		return this.tfQty.getText();
	}

	/**
	 * Parses the machine type/model data.
	 * @param data the machine type/model data
	 */
	@SuppressWarnings("unchecked")
	private void parseData(String data)
	{
		try
		{
			int start = 0;
			int end = 8;
			final int len = data.length();

			while (end < len)
			{
				this.fieldMatpMdlVector.addElement(data.substring(start, end));
				start += 128;
				end += 128;
			}

			DefaultListModel matpListModel = (DefaultListModel) this.lstMatp.getModel();
			for (int i = 0; i < this.fieldMatpMdlVector.size(); i++)
			{
				String matp = this.fieldMatpMdlVector.elementAt(i).toString().substring(0, 4);
				if (!matpListModel.contains(matp))
				{
					matpListModel.addElement(matp);
				}
			}
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(this, null, null, e);
		}
	}

	/** Invoked when {@link #pbPrint} is selected. */
	private void print()
	{

		int value = 0;
		String errorString = ""; //$NON-NLS-1$
		int rc = 0;

		try
		{
			// if a valid qty is entered, set it for
			// mcsn and matp_model labels
			if (!this.tfQty.getText().equals("")) //$NON-NLS-1$
			{
				value = Integer.parseInt(this.tfQty.getText());
			}
		}
		catch (java.lang.NumberFormatException e)
		{
			rc = 1;
			errorString = "An Invalid Quantity was entered.  Please Enter a valid Quantity";
		}
		if ((rc == 0) && (value == 0))
		{
			rc = 2;
			errorString = "0 is an Invalid Quantity.  Please Enter a valid Quantity";
		}
		if ((rc == 0) && (this.lstMatp.isSelectionEmpty()))
		{
			rc = 3;
			errorString = "Please Select a Machine Type";
		}
		if ((rc == 0) && (this.lstMdl.isSelectionEmpty()))
		{
			rc = 4;
			errorString = "Please Select a Model";
		}

		if (rc != 0)
		{
			IGSMessageBox.showOkMB(this, null, errorString, null);
			this.toFront();

			// find the faulty field entered, clear it, and give it the focus
			if ((rc == 1) || (rc == 2))
			{
				this.tfQty.setText(""); //$NON-NLS-1$
				this.tfQty.requestFocusInWindow();
			}
			else if (rc == 3)
			{
				this.lstMatp.requestFocusInWindow();
			}
			else
			{
				this.lstMdl.requestFocusInWindow();
			}
		}
		else
		{
			this.fieldPressedPrint = true;
			this.dispose();
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
				this.dispose();
			}
			else if (source == this.pbPrint)
			{
				print();
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
				this.pbPrint.requestFocusInWindow();
				this.pbPrint.doClick();
			}
		}
		else if (keyCode == KeyEvent.VK_ESCAPE)
		{
			this.dispose();
		}
		else if (keyCode == KeyEvent.VK_PAGE_DOWN)
		{
			if (source == this.spMdl || source == this.lstMdl)
			{
				MFSScroller.scrollDown(this.spMdl, this.lstMdl);
				ke.consume();
			}
			else
			{
				MFSScroller.scrollDown(this.spMatp, this.lstMatp);
				ke.consume();
			}
		}
		else if (keyCode == KeyEvent.VK_PAGE_UP)
		{
			if (source == this.spMdl || source == this.lstMdl)
			{
				MFSScroller.scrollUp(this.spMdl, this.lstMdl);
				ke.consume();
			}
			else
			{
				MFSScroller.scrollUp(this.spMatp, this.lstMatp);
				ke.consume();
			}
		}
	}

	/**
	 * Invoked when the value of a list selection changes.
	 * @param e the <code>ListSelectionEvent</code>
	 */
	public void valueChanged(ListSelectionEvent e)
	{
		try
		{
			if (e.getValueIsAdjusting())
			{
				return;
			}

			DefaultListModel mdlListModel = (DefaultListModel) this.lstMdl.getModel();
			mdlListModel.clear();

			//~1A Added selection empty check to account for initial focus and
			// to prevent NullPointerException on getSelectedValue().toString()
			if (this.lstMatp.isSelectionEmpty())
			{
				return;
			}

			final String selectedMatp = this.lstMatp.getSelectedValue().toString();
			for (int i = 0; i < this.fieldMatpMdlVector.size(); i++)
			{
				String matp = this.fieldMatpMdlVector.elementAt(i).toString().substring(0, 4);
				String mmdl = this.fieldMatpMdlVector.elementAt(i).toString().substring(4, 8);
				if (!mdlListModel.contains(mmdl) && matp.equals(selectedMatp))
				{
					mdlListModel.addElement(mmdl);
				}
			}
		}
		catch (Throwable throwable)
		{
			IGSMessageBox.showOkMB(this, null, null, throwable);
		}
	}
}
