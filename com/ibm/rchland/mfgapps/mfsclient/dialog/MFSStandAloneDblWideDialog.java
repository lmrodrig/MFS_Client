/* © Copyright IBM Corporation 2005, 2007. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2007-01-28      34242JR  R Prechel        -Java 5 version
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.dialog;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import com.ibm.rchland.mfgapps.client.utils.messagebox.IGSMessageBox;
import com.ibm.rchland.mfgapps.mfsclient.MFSDialog;
import com.ibm.rchland.mfgapps.mfsclient.MFSFrame;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSConstants;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSPrintingMethods;

/**
 * <code>MFSStandAloneDblWideDialog</code> is an <code>MFSDialog</code> used
 * to collect input for and print several double wide labels.
 * @author The MFS Client Development Team
 */
public class MFSStandAloneDblWideDialog
	extends MFSDialog
{
	private static final long serialVersionUID = 1L;
	/** The input <code>JTextField</code>. */
	private JTextField tfInput = createTextField(MFSConstants.LARGE_TF_COLS, 0);

	/** The input 2 <code>JTextField</code>. */
	private JTextField tfInput2 = createTextField(MFSConstants.LARGE_TF_COLS, 0);

	/** The Quantity input <code>JTextField</code>. */
	private JTextField tfQty = null;

	/** The Part Number <code>JRadioButton</code>. */
	private JRadioButton rbPartNum = new JRadioButton("Part Number", true);

	/** The Load Source <code>JRadioButton</code>. */
	private JRadioButton rbLoadSource = new JRadioButton("Load Source");

	/** The Text (1 Line) <code>JRadioButton</code>. */
	private JRadioButton rbText = new JRadioButton("Text (1 Line)");

	/** The Text (2 Lines) <code>JRadioButton</code>. */
	private JRadioButton rbText2 = new JRadioButton("Text (2 Lines)");

	/** The EC Number <code>JRadioButton</code>. */
	private JRadioButton rbEcNum = new JRadioButton("EC Number");

	/** The Serial Number <code>JRadioButton</code>. */
	private JRadioButton rbSerialNum = new JRadioButton("Serial Number");

	/** The Part/Serial <code>JRadioButton</code>. */
	private JRadioButton rbElevenS = new JRadioButton("Part/Serial");

	/** The Print <code>JButton</code>. */
	private JButton pbPrint = createButton("Print", 'P');

	/** The Cancel <code>JButton</code>. */
	private JButton pbCancel = createButton("Esc = Cancel", 'n');

	/**
	 * Constructs a new <code>MFSStandAloneDblWideDialog</code>.
	 * @param parent the <code>MFSFrame</code> that caused this
	 *        <code>MFSStandAloneDblWideDialog</code> to be displayed
	 */
	public MFSStandAloneDblWideDialog(MFSFrame parent)
	{
		super(parent, "Type or Scan in Input and Select Label");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		createLayout();
		addMyListeners();
	}

	/** Adds this dialog's <code>Component</code>s to the layout. */
	private void createLayout()
	{
		JLabel label = new JLabel("(2 Line Text Only)");
		label.setForeground(new Color(255, 102, 102));

		JLabel qtyLabel = createNameLabel("Quantity");
		this.tfQty = createTextField(MFSConstants.SMALL_TF_COLS / 2, 0, qtyLabel);

		JPanel qtyPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
		qtyPanel.add(qtyLabel);
		qtyPanel.add(this.tfQty);

		JPanel rbPanel = new JPanel(new GridLayout(4, 2, 20, 6));
		rbPanel.add(this.rbPartNum);
		rbPanel.add(this.rbEcNum);
		rbPanel.add(this.rbLoadSource);
		rbPanel.add(this.rbSerialNum);
		rbPanel.add(this.rbText);
		rbPanel.add(this.rbElevenS);
		rbPanel.add(this.rbText2);

		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(this.rbPartNum);
		buttonGroup.add(this.rbEcNum);
		buttonGroup.add(this.rbLoadSource);
		buttonGroup.add(this.rbSerialNum);
		buttonGroup.add(this.rbText);
		buttonGroup.add(this.rbElevenS);
		buttonGroup.add(this.rbText2);

		JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 50, 0));
		buttonPanel.add(this.pbPrint);
		buttonPanel.add(this.pbCancel);

		JPanel contentPane = new JPanel(new GridBagLayout());
		contentPane.setBorder(BorderFactory.createEmptyBorder(20, 50, 10, 50));
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, 
				new Insets(0, 0, 10, 0), 0, 0);

		contentPane.add(this.tfInput, gbc);

		gbc.gridy++;
		gbc.insets = new Insets(0, 0, 0, 0);
		contentPane.add(label, gbc);

		gbc.gridy++;
		gbc.insets = new Insets(0, 0, 15, 0);
		contentPane.add(this.tfInput2, gbc);

		gbc.gridy++;
		gbc.insets = new Insets(0, 0, 20, 0);
		contentPane.add(qtyPanel, gbc);

		gbc.gridy++;
		gbc.insets = new Insets(0, 0, 20, 0);
		contentPane.add(rbPanel, gbc);

		gbc.gridy++;
		gbc.insets = new Insets(0, 0, 20, 0);
		contentPane.add(buttonPanel, gbc);

		setContentPane(contentPane);
		pack();
	}

	/** Adds the listeners to this dialog's <code>Component</code>s. */
	private void addMyListeners()
	{
		this.rbPartNum.addActionListener(this);
		this.rbLoadSource.addActionListener(this);
		this.rbText.addActionListener(this);
		this.rbText2.addActionListener(this);
		this.rbEcNum.addActionListener(this);
		this.rbSerialNum.addActionListener(this);
		this.rbElevenS.addActionListener(this);

		this.pbPrint.addActionListener(this);
		this.pbCancel.addActionListener(this);

		this.pbPrint.addKeyListener(this);
		this.pbCancel.addKeyListener(this);
		this.tfInput.addKeyListener(this);
		this.tfInput2.addKeyListener(this);
		this.tfQty.addKeyListener(this);
	}

	/** Invoked when {@link #pbPrint} is selected. */
	private void print()
	{
		try
		{
			String input = this.tfInput.getText();
			String input2 = this.tfInput2.getText();
			String qty = this.tfQty.getText();

			int rc = 0;
			String errorString = ""; //$NON-NLS-1$

			// If both input fields are blank, then return error
			if (input.equals("") & input2.equals("")) //$NON-NLS-1$ //$NON-NLS-2$
			{
				rc = 2;
				errorString = "Invalid Input";
			}
			//Else if Line 2 input is entered, but 2 Line Text isn't selected
			else if ((!input2.trim().equals("")) & (!this.rbText2.isSelected())) //$NON-NLS-1$
			{
				rc = 3;
				errorString = "Line 2 input only valid when Text(2 Lines) Label is selected";
			}
			//Else valid input
			else
			{
				int quantity = 1;
				// Get and verify the quantity entered
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

				//If valid quantity is also entered
				if (rc == 0)
				{
					// find out which radio button is selected and then try to print out the label
					// If Part Number button
					if (this.rbPartNum.isSelected())
					{
						if (input.length() != 7)
						{
							rc = 2;
							errorString = "Part Number must be 7 long";
						}
						else
						{
							if (quantity % 2 == 1)
							{
								quantity++;
							}
							MFSPrintingMethods.partnum("P" + input, (quantity / 2), getParentFrame()); //$NON-NLS-1$
						}
					}// end part number labels
					// Else if EC Number button
					else if (this.rbEcNum.isSelected())
					{
						if (input.length() != 6 && input.length() != 7)
						{
							rc = 2;
							errorString = "EC Number must be 6 or 7 long";
						}
						else
						{
							if (quantity % 2 == 1)
							{
								quantity++;
							}
							MFSPrintingMethods.ecnum("2P" + input, input.length() + 2, (quantity / 2), getParentFrame()); //$NON-NLS-1$
						}
					}// end ec number labels
					// Else if Load Source button
					else if (this.rbLoadSource.isSelected())
					{
						if (input.length() != 7)
						{
							rc = 2;
							errorString = "Part Number must be 7 long";
						}
						else
						{
							MFSPrintingMethods.loadsource("P" + input, quantity, getParentFrame()); //$NON-NLS-1$
						}
					}// end load source labels
					// Else serial number button
					else if (this.rbSerialNum.isSelected())
					{
						if (input.length() != 9)
						{
							rc = 2;
							errorString = "Serial Number must be 9 long";
						}
						else
						{
							// first capitalize any of the alphabetic characters
							this.tfInput.setText(input.toUpperCase());

							char[] charInput = input.toCharArray();
							boolean alpha_found = false;
							String tweaked_sn = ""; //$NON-NLS-1$
							String zeros = "000000000"; //$NON-NLS-1$

							// determine if and where the first alphabetic
							// character is
							int i = 0;
							while (i < input.length() && !alpha_found)
							{
								if (!Character.isDigit(charInput[i]))
								{
									alpha_found = true;
								}
								else
								{
									i++;
								}
							}
							// if there is one, set the following chars/digits to 0's
							// and move valid stuff to end of the SN
							if (alpha_found)
							{
								// now build a string with a numeric SN
								tweaked_sn = zeros.substring(i, input.length()) + input.substring(0, i);
							}
							else
							{
								tweaked_sn = input;
							}

							// now if there are multiples, we will increment the now numeric SN
							String snuma = "";
							String snumb = "";
							int label_count = 0;
							int sn_length = 0;
							boolean single_serial = false;

							// when we execute this transition, we lose any 0's
							// on the front of tweaked_sn
							Long tweaked_number = new Long(tweaked_sn);

							if (quantity % 2 == 1)
							{
								single_serial = true;
							}

							if (single_serial)
							{
								//sn_length is the number of significant digits in the tweaked_number
								//convert it to a string and then count the length - we need to know how many zeros
								//to put back into the serial number
								sn_length = Long.toString(tweaked_number.longValue()).length();

								// now concat a SN and the proper number of
								// zeros to the beginning of the SN
								snuma = "SN" + zeros.substring(0, 9 - sn_length) + Long.toString(tweaked_number.longValue()); //$NON-NLS-1$
								label_count++;

								MFSPrintingMethods.serialnumsingle(snuma, 1, getParentFrame());
							}

							while (label_count < quantity)
							{
								//sn_length is the number of significant digits
								// in the tweaked_number
								//convert it to a string and then count the
								// length - we need to know how many zeros
								//to put back into the serial number
								sn_length = Long.toString(tweaked_number.longValue() + label_count).length();
								snuma = "SN" + zeros.substring(0, 9 - sn_length) + Long.toString(tweaked_number.longValue() + label_count); //$NON-NLS-1$

								sn_length = Long.toString(tweaked_number.longValue() + (label_count + 1)).length();
								snumb = "SN" + zeros.substring(0, 9 - sn_length) + Long.toString(tweaked_number.longValue() + (label_count + 1)); //$NON-NLS-1$

								MFSPrintingMethods.serialnumdouble(snuma, snumb, 1, getParentFrame());
								label_count += 2;
							}
						} // end of valid SN input
					}// end serial number labels

					// Else if Text (1 Line) button
					else if (this.rbText.isSelected())
					{
						if (input.length() > 20)
						{
							rc = 2;
							errorString = "1 Line Text cannot be longer than 20";
						}
						else
						{
							if (quantity % 2 == 1)
							{
								quantity++;
							}
							int index = 0;

							index = input.indexOf("&", index); //$NON-NLS-1$
							while (index != -1)
							{
								input = input.substring(0, index) + "/" + input.substring(index); //$NON-NLS-1$
								index = input.indexOf("&", index + 2); //$NON-NLS-1$
							}
							index = 0;
							index = input.indexOf("=", index); //$NON-NLS-1$
							while (index != -1)
							{
								input = input.substring(0, index) + "/" + input.substring(index); //$NON-NLS-1$
								index = input.indexOf("=", index + 2); //$NON-NLS-1$
							}

							MFSPrintingMethods.alltext(input, (quantity / 2), getParentFrame());
						}
					}// end 1 Line Text label
					// Else if Text (2 Lines) button
					else if (this.rbText2.isSelected())
					{
						// Check if first line is too long
						if (input.length() > 30)
						{
							rc = 2;
							errorString = "2 Line Text cannot be longer than 30";
						}
						// Check if second line is too long
						else if (input2.length() > 30)
						{
							rc = 3;
							errorString = "2 Line Text cannot be longer than 30";
						}
						// Else valid input to print
						else
						{
							if (quantity % 2 == 1)
							{
								quantity++;
							}

							// Update both input lines for zpl to read correctly
							int index = 0;
							index = input.indexOf("&", index); //$NON-NLS-1$
							while (index != -1)
							{
								input = input.substring(0, index) + "/" + input.substring(index); //$NON-NLS-1$
								index = input.indexOf("&", index + 2); //$NON-NLS-1$
							}
							index = 0;
							index = input.indexOf("=", index); //$NON-NLS-1$
							while (index != -1)
							{
								input = input.substring(0, index) + "/" + input.substring(index); //$NON-NLS-1$
								index = input.indexOf("=", index + 2); //$NON-NLS-1$
							}
							// Line 2
							index = 0;
							index = input2.indexOf("&", index); //$NON-NLS-1$
							while (index != -1)
							{
								input2 = input2.substring(0, index) + "/" + input2.substring(index); //$NON-NLS-1$
								index = input2.indexOf("&", index + 2); //$NON-NLS-1$
							}
							index = 0;
							index = input2.indexOf("=", index); //$NON-NLS-1$
							while (index != -1)
							{
								input2 = input2.substring(0, index) + "/" + input2.substring(index); //$NON-NLS-1$
								index = input2.indexOf("=", index + 2); //$NON-NLS-1$
							}

							MFSPrintingMethods.alltext2(input, input2, (quantity / 2), getParentFrame());
						}
					}// end 1 Line Text label
					// Else eleven S label
					else
					{
						if (input.length() != 19 && input.length() != 22)
						{
							rc = 2;
							errorString = "Part/Serial must be 19 or 22";
						}
						else
						{
							if (quantity % 2 == 1)
							{
								quantity++;
							}
							MFSPrintingMethods.stndalnelevens(input, input.length(), quantity / 2, getParentFrame());
						}
					}// end eleven S labels
				}// end good return code
			}// end valid input

			if (rc != 0)
			{
				IGSMessageBox.showOkMB(this, null, errorString, null);
				this.toFront();

				// find the faulty field entered, clear it, and give it the focus
				// If bad quantity entered
				if (rc == 1)
				{
					this.tfQty.selectAll();
					this.tfQty.requestFocusInWindow();
				}
				//Else if bad Line 2 inputed entered
				else if (rc == 3)
				{
					this.tfInput2.selectAll();
					this.tfInput2.requestFocusInWindow();
				}
				//Else bad Line 1 input entered
				else
				{
					this.tfInput.selectAll();
					this.tfInput.requestFocusInWindow();
				}
			} // end bad return code
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(this, null, null, e);
			this.toFront();

			this.tfQty.setText(""); //$NON-NLS-1$
			this.tfInput.setText(""); //$NON-NLS-1$
			this.tfInput2.setText(""); //$NON-NLS-1$
			this.tfInput.requestFocusInWindow();
		}
	}//end Print() method

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
}
