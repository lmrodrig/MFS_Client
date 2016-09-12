/* @ Copyright IBM Corporation 2007. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2007-03-14      35208JM  R Prechel        -Initial version
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.dialog;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.ibm.rchland.mfgapps.client.utils.messagebox.IGSMessageBox;
import com.ibm.rchland.mfgapps.mfsclient.MFSDialog;
import com.ibm.rchland.mfgapps.mfsclient.MFSFrame;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSConstants;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSPrintingMethods;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSTransaction;
import com.ibm.rchland.mfgapps.mfscommon.exception.MFSException;

/**
 * <code>MFSCreateBCKeysDialog</code> is the <code>MFSDialog</code> used to
 * print a key input barcode.
 * @author The MFS Client Development Team
 */
public class MFSCreateBCKeysDialog
	extends MFSDialog
	implements ItemListener
{
	private static final long serialVersionUID = 1L;
	/** The Modifiers <code>JComboBox</code>. */
	private JComboBox cbModifiers = null;

	/** The Input Keys <code>JComboBox</code>. */
	private JComboBox cbInputKeys = null;

	/**
	 * The <code>JLabel</code> that displays the human readable text of the
	 * barcode that will be printed for the current combo box selections.
	 */
	private JLabel lblSelectedText = createLabel(""); //$NON-NLS-1$

	/** The Quantity <code>JTextField</code>. */
	private JTextField tfQuantity = null;

	/** The Print <code>JButton</code>. */
	private JButton pbPrint = createButton("Print - F2", 'P');

	/** The Quit <code>JButton</code>. */
	private JButton pbQuit = createButton("Quit - F3", 'Q');

	/** The <code>HashMap</code> of rule data. */
	@SuppressWarnings("rawtypes")
	private HashMap fieldMap = new HashMap();

	/** The barcode starting delimiter. */
	private String fieldStartDelim = null;

	/** The barcode ending delimiter. */
	private String fieldEndDelim = null;

	/** The modifier combo box option for no modifier. */
	private final String NO_MODIFIER = "NONE";

	/** The input key combo box option for no input key. */
	private final String NO_INPUT_KEY = " ";

	/**
	 * Constructs a new <code>MFSCreateBCKeysDialog</code>.
	 * @param parent the <code>MFSFrame</code> that caused this
	 *        <code>MFSCreateBCKeysDialog</code> to be displayed
	 * @param transaction the <code>MFSTransaction</code> used to retrieve the
	 *        rule data used to load the combo boxes
	 * @throws MFSException as thrown by {@link #loadComboBoxes(MFSTransaction)}
	 */
	public MFSCreateBCKeysDialog(MFSFrame parent, MFSTransaction transaction)
		throws MFSException
	{
		super(parent, "Print Barcode for Key");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		loadComboBoxes(transaction);
		createLayout();
		addMyListeners();
	}

	/**
	 * Creates and loads the combo boxes.
	 * @param transaction the <code>MFSTransaction</code> used to retrieve the
	 *        rule data used to load the combo boxes
	 * @throws MFSException if the output of <code>transaction</code> is
	 *         missing required information
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void loadComboBoxes(MFSTransaction transaction)
		throws MFSException
	{
		String data = transaction.getOutput();
		Vector modifiers = new Vector();
		Vector inputKeys = new Vector();

		// Setup the vectors so NO_MODIFIER and NO_INPUT_KEY are the first
		// elements in the JComboBoxes. NO_MODIFIER is a valid condition,
		// so add the corresponding entry into the HashMap of rule data.
		modifiers.add(this.NO_MODIFIER);
		this.fieldMap.put(this.NO_MODIFIER, "0"); //$NON-NLS-1$

		inputKeys.add(this.NO_INPUT_KEY);

		//data should contain a set of FKE rules
		int beginOpenTag = data.indexOf("<FKE"); //$NON-NLS-1$
		while (beginOpenTag != -1)
		{
			try
			{
				//For each FKE rule, find the XML element name and value
				int endOpenTag = data.indexOf(">", beginOpenTag); //$NON-NLS-1$
				String elementName = data.substring(beginOpenTag + 1, endOpenTag);
				int beginCloseTag = data.indexOf("</" + elementName + ">", endOpenTag); //$NON-NLS-1$ //$NON-NLS-2$
				String elementValue = data.substring(endOpenTag + 1, beginCloseTag);

				//FKEP elements are for KEY_PRESSED input keys
				if (elementName.startsWith("FKEP")) //$NON-NLS-1$
				{
					String inputKey = elementValue.toUpperCase();
					this.fieldMap.put(inputKey, elementName.substring(3));
					inputKeys.add(inputKey);
				}
				//FKET elements are for KEY_TYPED input keys
				else if (elementName.startsWith("FKET")) //$NON-NLS-1$
				{
					String inputKey = elementValue.toUpperCase();
					this.fieldMap.put(inputKey, elementName.substring(3));
					inputKeys.add(inputKey);
				}
				//FKER elements are for KEY_RELEASED input keys
				else if (elementName.startsWith("FKER")) //$NON-NLS-1$
				{
					String inputKey = elementValue.toUpperCase();
					this.fieldMap.put(inputKey, elementName.substring(3));
					inputKeys.add(inputKey);
				}
				//FKEMOD elements are for modifier keys
				else if (elementName.startsWith("FKEMOD")) //$NON-NLS-1$
				{
					String modifierName = elementName.substring(6).toUpperCase();
					this.fieldMap.put(modifierName, elementValue);
					modifiers.add(modifierName);
				}
				//FKECONV elements are for conversion rules
				else if (elementName.startsWith("FKECONV")) //$NON-NLS-1$
				{
					//Skip these rules
				}
				//FKEDELIMSTART is the start delimiter
				else if (elementName.equals("FKEDELIMSTART")) //$NON-NLS-1$
				{
					this.fieldStartDelim = elementValue;
				}
				//FKEDELIMEND is the end delimiter
				else if (elementName.equals("FKEDELIMEND")) //$NON-NLS-1$
				{
					this.fieldEndDelim = elementValue;
				}
			}
			catch (IndexOutOfBoundsException ioe)
			{
				//Bad FKE rule
				ioe.printStackTrace();
			}

			beginOpenTag = data.indexOf("<FKE", beginOpenTag + 1); //$NON-NLS-1$
		}

		if (this.fieldStartDelim == null)
		{
			throw new MFSException("No start delimiter found in rule data.");
		}
		if (this.fieldEndDelim == null)
		{
			throw new MFSException("No end delimiter found in rule data.");
		}

		this.cbModifiers = new JComboBox(modifiers);
		this.cbInputKeys = new JComboBox(inputKeys);
	}

	/** Adds this dialog's <code>Component</code>s to the layout. */
	private void createLayout()
	{
		JLabel modifiersLabel = createLabel("Modifiers:", MFSConstants.SMALL_DIALOG_FONT);
		JLabel inputKeysLabel = createLabel("Input Keys:", MFSConstants.SMALL_DIALOG_FONT);
		this.lblSelectedText.setHorizontalAlignment(SwingConstants.CENTER);
		this.lblSelectedText.setHorizontalTextPosition(SwingConstants.CENTER);

		JLabel quantityLabel = createNameLabel("Quantity:");
		this.tfQuantity = createTextField(MFSConstants.SMALL_TF_COLS, 2, quantityLabel);
		this.tfQuantity.setText("1");

		JPanel contentPane = new JPanel(new GridBagLayout());
		contentPane.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1, 1,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, 
				new Insets(0, 10, 0, 10), 0, 0);

		contentPane.add(modifiersLabel, gbc);

		gbc.gridx = 1;
		contentPane.add(inputKeysLabel, gbc);

		gbc.gridx = 0;
		gbc.gridy++;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		contentPane.add(this.cbModifiers, gbc);

		gbc.gridx = 1;
		contentPane.add(this.cbInputKeys, gbc);

		gbc.gridx = 0;
		gbc.gridy++;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(20, 2, 0, 2);
		contentPane.add(this.lblSelectedText, gbc);

		gbc.gridy++;
		gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.EAST;
		gbc.insets = new Insets(40, 2, 0, 2);
		contentPane.add(quantityLabel, gbc);

		gbc.gridx = 1;
		gbc.anchor = GridBagConstraints.WEST;
		contentPane.add(this.tfQuantity, gbc);

		gbc.gridx = 0;
		gbc.gridy++;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.insets = new Insets(20, 10, 0, 10);
		contentPane.add(this.pbPrint, gbc);

		gbc.gridx = 1;
		contentPane.add(this.pbQuit, gbc);

		setContentPane(contentPane);
		setButtonDimensions(this.pbPrint, this.pbQuit);
		setLabelDimensions(this.lblSelectedText, 20);
		pack();
	}

	/** Adds the listeners to this dialog's <code>Component</code>s. */
	private void addMyListeners()
	{
		this.pbPrint.addActionListener(this);
		this.pbQuit.addActionListener(this);

		this.cbInputKeys.addItemListener(this);
		this.cbModifiers.addItemListener(this);

		this.pbPrint.addKeyListener(this);
		this.pbQuit.addKeyListener(this);
		this.cbModifiers.addKeyListener(this);
		this.cbInputKeys.addKeyListener(this);
		this.tfQuantity.addKeyListener(this);
	}

	/** Invoked when {@link #pbPrint} is selected. */
	private void print()
	{
		String qtyString = this.tfQuantity.getText();
		try
		{
			int qty = Integer.parseInt(qtyString);

			if (1 > qty)
			{
				StringBuffer erms = new StringBuffer();
				erms.append("\"");
				erms.append(qtyString);
				erms.append("\" is not a valid quantity.");
				IGSMessageBox.showOkMB(this, null, erms.toString(), null);
			}
			else if (this.cbInputKeys.getSelectedItem().equals(this.NO_INPUT_KEY))
			{
				String erms = "You must select an input key before printing.";
				IGSMessageBox.showOkMB(this, null, erms, null);
				this.cbInputKeys.requestFocusInWindow();
			}
			else
			{
				String bcText = this.lblSelectedText.getText();
				StringBuffer bcData = new StringBuffer();
				bcData.append(this.fieldStartDelim);
				bcData.append(this.fieldMap.get(this.cbModifiers.getSelectedItem()));
				bcData.append(this.fieldMap.get(this.cbInputKeys.getSelectedItem()));
				bcData.append(this.fieldEndDelim);

				MFSPrintingMethods.keyInputBarcode(bcText, bcData.toString(), qty,
						getParentFrame());
			}
		}
		catch (NumberFormatException nfe)
		{
			StringBuffer erms = new StringBuffer();
			erms.append("\"");
			erms.append(qtyString);
			erms.append("\" is not a valid quantity.");
			IGSMessageBox.showOkMB(this, null, erms.toString(), null);
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
			if (source == this.pbQuit)
			{
				dispose();
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
	 * Invoked when an item has been selected or deselected by the user.
	 * <p>
	 * Sets up the text of {@link #lblSelectedText}.
	 * @param ie the <code>ItemEvent</code>
	 */
	public void itemStateChanged(ItemEvent ie)
	{
		if (ie.getStateChange() == ItemEvent.SELECTED)
		{
			String modifier = (String) this.cbModifiers.getSelectedItem();
			if (modifier == null || modifier.equals(this.NO_MODIFIER))
			{
				modifier = ""; //$NON-NLS-1$
			}

			String inputKey = (String) this.cbInputKeys.getSelectedItem();
			if (inputKey == null || inputKey.equals(this.NO_INPUT_KEY))
			{
				inputKey = ""; //$NON-NLS-1$
			}

			if (modifier.length() > 0 && inputKey.length() > 0)
			{
				this.lblSelectedText.setText(modifier + "+" + inputKey);
			}
			else
			{
				this.lblSelectedText.setText(modifier + inputKey);
			}
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
			if (source == this.pbPrint)
			{
				this.pbPrint.requestFocusInWindow();
				this.pbPrint.doClick();
			}
			else if (source == this.pbQuit)
			{
				this.pbQuit.requestFocusInWindow();
				this.pbQuit.doClick();
			}
		}
		else if (keyCode == KeyEvent.VK_F2)
		{
			this.pbPrint.requestFocusInWindow();
			this.pbPrint.doClick();
		}
		else if (keyCode == KeyEvent.VK_F3)
		{
			this.pbQuit.requestFocusInWindow();
			this.pbQuit.doClick();
		}
		else if (keyCode == KeyEvent.VK_ESCAPE)
		{
			this.pbQuit.requestFocusInWindow();
			this.pbQuit.doClick();
		}
	}

	/**
	 * Invoked the first time a window is made visible.
	 * <p>
	 * Sets the initial selections of the combo boxes and requests the focus for
	 * the {@link #cbModifiers}.
	 * @param we the <code>WindowEvent</code>
	 */
	public void windowOpened(WindowEvent we)
	{
		if (we.getSource() == this)
		{
			this.cbModifiers.setSelectedItem(this.NO_MODIFIER);
			this.cbInputKeys.setSelectedItem(this.NO_INPUT_KEY);
			this.cbInputKeys.requestFocusInWindow();
		}
	}
}
