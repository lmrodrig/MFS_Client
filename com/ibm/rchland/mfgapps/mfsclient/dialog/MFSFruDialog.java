/* © Copyright IBM Corporation 2005, 2010. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2007-01-27      34242JR  R Prechel        -Java 5 version
 * 2010-05-17 ~01  46780JL  Edgar Mercado    -Define a new private variable and method to retrieve
 *                                            CNAMETXT COO rule value.
 * 2010-11-01  ~02 49513JM	Toribio H.   	 -Make RTV_CTRY Cacheable                                            
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.dialog;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.ibm.rchland.mfgapps.client.utils.messagebox.IGSMessageBox;
import com.ibm.rchland.mfgapps.mfsclient.MFSActionableDialog;
import com.ibm.rchland.mfgapps.mfsclient.MFSFrame;
import com.ibm.rchland.mfgapps.mfsclient.barcode.MFSBCBarcodeDecoder;
import com.ibm.rchland.mfgapps.mfsclient.barcode.MFSBCIndicatorValue;
import com.ibm.rchland.mfgapps.mfsclient.barcode.MFSBCPartObject;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSConstants;
import com.ibm.rchland.mfgapps.mfscommon.MFSConfig;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSComm;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSFixedTransaction;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSTransaction;
import com.ibm.rchland.mfgapps.mfscommon.comm.trx.RTV_CTRY;

/**
 * <code>MFSFruDialog</code> is the <code>MFSActionableDialog</code> used to
 * prompt the user for the input for a Fru label.
 * @author The MFS Client Development Team
 */
@SuppressWarnings("serial")
public class MFSFruDialog
	extends MFSActionableDialog
{
	/** The input <code>JTextField</code>. */
	private JTextField tfInput = createTextField(MFSConstants.LARGE_TF_COLS, 0);

	/** The part number value <code>JLabel</code>. */
	private JLabel vlPN;

	/** The serial number value <code>JLabel</code>. */
	private JLabel vlSN;

	/** The country value <code>JLabel</code>. */
	private JLabel vlCnty;

	/** The Print <code>JButton</code>. */
	private JButton pbPrint = createButton("F2 - Print"); //$NON-NLS-1$

	/** The List Countries <code>JButton</code>. */
	private JButton pbCountry = createButton("F3 - List Countries"); //$NON-NLS-1$

	/** The Cancel <code>JButton</code>. */
	private JButton pbCancel = createButton("Esc - Cancel"); //$NON-NLS-1$

	/** The country name. */
	private String fieldCountryName = ""; //$NON-NLS-1$
	
	/** The coo header. */                                                          //~01A
	private String fieldCNametxt = "";                                              //~01A	 //$NON-NLS-1$
	
	/** Set <code>true</code> when the print button is selected. */
	private boolean fieldPressedPrint = false;

	/**
	 * Constructs a new <code>MFSFruDialog</code>.
	 * @param parent the <code>MFSFrame</code> that caused this
	 *        <code>MFSFruDialog</code> to be displayed
	 */
	public MFSFruDialog(MFSFrame parent)
	{
		super(parent, "Fru Labels"); //$NON-NLS-1$
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		createLayout();
		addMyListeners();
	}

	/** Adds this dialog's <code>Component</code>s to the layout. */
	private void createLayout()
	{
		JLabel pnLabel = createSmallNameLabel("Part Number"); //$NON-NLS-1$
		this.vlPN = createSmallValueLabel(pnLabel);

		JLabel snLabel = createSmallNameLabel("Serial Number"); //$NON-NLS-1$
		this.vlSN = createSmallValueLabel(snLabel);

		JLabel ctryLabel = createSmallNameLabel("Country"); //$NON-NLS-1$
		this.vlCnty = createSmallValueLabel(ctryLabel);

		JPanel labelPanel = new JPanel(new GridLayout(3, 3, 20, 10));
		labelPanel.add(pnLabel);
		labelPanel.add(this.vlPN);
		labelPanel.add(snLabel);
		labelPanel.add(this.vlSN);
		labelPanel.add(ctryLabel);
		labelPanel.add(this.vlCnty);

		JPanel contentPane = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 2, 1, 1, 1,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, 
				new Insets(30, 0, 0, 0), 0, 0);

		contentPane.add(this.tfInput, gbc);

		gbc.gridy++;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		contentPane.add(labelPanel, gbc);

		gbc.gridy++;
		gbc.gridwidth = 1;
		gbc.weighty = 0;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(30, 40, 0, 5);
		contentPane.add(this.pbPrint, gbc);

		gbc.gridx = 1;
		gbc.insets = new Insets(30, 5, 0, 40);
		contentPane.add(this.pbCountry, gbc);

		gbc.gridx = 0;
		gbc.gridy++;
		gbc.gridwidth = 2;
		gbc.insets = new Insets(10, 40, 30, 40);
		contentPane.add(this.pbCancel, gbc);

		setButtonDimensions(this.pbPrint, this.pbCountry, this.pbCancel);
		setContentPane(contentPane);
		pack();
	}

	/** Adds the listeners to this dialog's <code>Component</code>s. */
	private void addMyListeners()
	{
		this.pbPrint.addActionListener(this);
		this.pbCountry.addActionListener(this);
		this.pbCancel.addActionListener(this);

		this.pbPrint.addKeyListener(this);
		this.pbCountry.addKeyListener(this);
		this.pbCancel.addKeyListener(this);
		this.tfInput.addKeyListener(this);
	}

	/**
	 * Returns the text of the country value <code>JLabel</code>.
	 * @return the text of the country value <code>JLabel</code>
	 */
	public String getCntyInputText()
	{
		return this.vlCnty.getText();
	}

	/**
	 * Returns the country name.
	 * @return the country name
	 */
	public String getCountryName()
	{
		return this.fieldCountryName;
	}

	/**                                        
	 * Returns the country name.                   
	 * @return the country name 
	 */                                                                          //~01A
	public String getCNametxt()                                                  //~01A
	{                                                                            //~01A
		return this.fieldCNametxt;                                               //~01A
	}                                                                            //~01A
	
	/**
	 * Returns the text of the part number value <code>JLabel</code>.
	 * @return the text of the part number value <code>JLabel</code>
	 */
	public String getPNInputText()
	{
		return this.vlPN.getText();
	}

	/**
	 * Returns <code>true</code> iff the print button was selected.
	 * @return <code>true</code> iff the print button was selected
	 */
	public boolean getPressedPrint()
	{
		return this.fieldPressedPrint;
	}

	/**
	 * Returns the text of the serial number value <code>JLabel</code>.
	 * @return the text of the serial number value <code>JLabel</code>
	 */
	public String getSNInputText()
	{
		return this.vlSN.getText();
	}

	/** Invoked when {@link #pbCountry} is selected. */
	private void listCoo()
	{
		try
		{
			String pn = this.vlPN.getText();
			if (!pn.equals("")) //$NON-NLS-1$
			{
				RTV_CTRY rtvCTRY = new RTV_CTRY(this); //~02
				rtvCTRY.setInputPN(pn);
				if (rtvCTRY.execute())
				{
					String in_data = rtvCTRY.getOutputCooList();
					if (in_data.length() > 0)
					{
						MFSCooDialog cooJD = new MFSCooDialog(getParentFrame(), false);
						cooJD.loadCooListModel(in_data);
						cooJD.setLocationRelativeTo(this);
						cooJD.setVisible(true);
						if (cooJD.getProceedSelected())
						{
							// set cooselection to the first 2 characters from the selection
							String cooselection = cooJD.getSelectedListOption().substring(0, 2);
							if (cooselection.equals("  ")) //$NON-NLS-1$
							{
								cooselection = "??"; //$NON-NLS-1$
							}
							this.vlCnty.setText(cooselection);
							this.fieldCountryName = cooJD.getSelectedListOption().substring(7, 57);
							this.fieldCNametxt = cooJD.getSelectedListOption().substring(57, 107);
						}
					}
					else
					{
						String erms = "No Countries found for the P/N in SAPPPC10 file."; //$NON-NLS-1$
						IGSMessageBox.showOkMB(this, null, erms, null);
					}
				}
				else
				{
					IGSMessageBox.showOkMB(this, null, rtvCTRY.getErrorMessage(), null);
				}
			}// end non-blank part number text field
			else
			{
				String erms = "Enter a Part Number before selecting 'List Countries'"; //$NON-NLS-1$
				IGSMessageBox.showOkMB(this, null, erms, null);
			}

		}// end try
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(this, null, null, e);
		}
		this.toFront();
		this.tfInput.requestFocusInWindow();
	}

	/** Invoked when {@link #pbPrint} is selected. */
	private void print()
	{
		try
		{
			int rc = 0;
			if (this.vlPN.getText().equals("")) //$NON-NLS-1$
			{
				String erms = "Part Number must be filled in to print the FRU label"; //$NON-NLS-1$
				IGSMessageBox.showOkMB(this, null, erms, null);

				this.toFront();
				this.tfInput.requestFocusInWindow();
			}
			else if (this.vlCnty.getText().equals("")) //$NON-NLS-1$
			{
				String erms = "Country must be filled in to print the FRU label"; //$NON-NLS-1$
				IGSMessageBox.showOkMB(this, null, erms, null);

				this.toFront();
				this.tfInput.requestFocusInWindow();
			}
			else
			{
				String data = "RTV_CSNI  " + this.vlPN.getText(); //$NON-NLS-1$
				MFSTransaction rtv_csni = new MFSFixedTransaction(data);
				rtv_csni.setActionMessage("Retrieving CSNI indicator, Please Wait..."); //$NON-NLS-1$
				MFSComm.getInstance().execute(rtv_csni, this);
				rc = rtv_csni.getReturnCode();

				if (rc == 0)
				{
					String commData = rtv_csni.getOutput();
					String csni = commData.substring(0, 1);
					String serp = commData.substring(1, 5);

					if (csni.equals(" ")) //$NON-NLS-1$ 
					{
						this.fieldPressedPrint = true;
						this.dispose();
					}
					else
					{
						if (this.vlSN.getText().equals("")) //$NON-NLS-1$
						{
							String erms = "Serial Number required for this part"; //$NON-NLS-1$
							IGSMessageBox.showOkMB(this, null, erms, null);

							this.toFront();
							this.tfInput.requestFocusInWindow();
						}
					}

					if (serp.equals("    ") && (rc == 0)) //$NON-NLS-1$
					{
						this.fieldPressedPrint = true;
						this.dispose();
					}
					else if (!serp.equals("    ") && (rc == 0)) //$NON-NLS-1$
					{
						StringBuffer data1 = new StringBuffer();
						data1.append("VRFY_MSCOO"); //$NON-NLS-1$
						data1.append(this.vlPN.getText());
						data1.append(this.vlSN.getText());
						data1.append(this.vlCnty.getText());

						MFSTransaction vrfy_mscoo = new MFSFixedTransaction(data1.toString());
						vrfy_mscoo.setActionMessage("Verifying COOC, Please Wait..."); //$NON-NLS-1$
						MFSComm.getInstance().execute(vrfy_mscoo, this);
						rc = vrfy_mscoo.getReturnCode();

						if (rc == 0)
						{
							this.fieldPressedPrint = true;
							this.dispose();
						}
						else
						{
							IGSMessageBox.showOkMB(this, null, vrfy_mscoo.getErms(), null);

							this.toFront();
							this.tfInput.requestFocusInWindow();
						}
					}
				} // end good call to rtv_csni
				else
				{
					IGSMessageBox.showOkMB(this, null, rtv_csni.getErms(), null);

					this.toFront();
					this.tfInput.requestFocusInWindow();
				}
			}
		}// end try
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(this, null, null, e);
		}
	}

	/** Processes the input the user entered in the <code>JTextField</code>. */
	private void recvInput()
	{
		try
		{
			boolean found = false;
			int rc = 0;

			MFSBCBarcodeDecoder barcode = MFSBCBarcodeDecoder.getInstance();
			barcode.setMyBarcodeInput(this.tfInput.getText().toUpperCase());
			barcode.setMyBCPartObject(new MFSBCPartObject());

			/* setup barcode indicator values */
			MFSBCIndicatorValue bcIndVal = new MFSBCIndicatorValue();

			String sni = MFSConfig.getInstance().getConfigValue("DECODESN"); //$NON-NLS-1$
			if (!sni.equals(MFSConfig.NOT_FOUND))
			{
				bcIndVal.setCSNI(sni);
			}
			else
			{
				bcIndVal.setCSNI("1"); //$NON-NLS-1$
			}
			bcIndVal.setPRLN("        "); //$NON-NLS-1$

			String input = this.tfInput.getText().toUpperCase();
			String elevenS = input.substring(0, 3);

			if (elevenS.equals("11S") && input.length() > 10) //$NON-NLS-1$
			{
				bcIndVal.setITEM("00000" + input.substring(3, 10)); //$NON-NLS-1$
			}
			bcIndVal.setPNRI("1"); //$NON-NLS-1$
			bcIndVal.setCOOI("1"); //$NON-NLS-1$

			barcode.setMyBCIndicatorValue(bcIndVal);

			if (barcode.decodeBarcodeFor(this) == false)
			{
				rc = 10;
				barcode.getBCMyPartObject().setErrorString(
						MFSBCBarcodeDecoder.DECODE_TIMED_OUT_ERMS);
			}
			else
			{
				rc = barcode.getBCMyPartObject().getRC();
			}

			if (rc == 0 && !found)
			{
				/* PN */
				if (!(barcode.getBCMyPartObject().getPN().equals(""))) //$NON-NLS-1$
				{
					this.vlPN.setText(barcode.getBCMyPartObject().getPN());
					found = true;
				}

				/* SN */
				if (!(barcode.getBCMyPartObject().getSN().equals(""))) //$NON-NLS-1$
				{
					this.vlSN.setText(barcode.getBCMyPartObject().getSN());
					found = true;
				}

				/* Country */
				if (!(barcode.getBCMyPartObject().getCO().equals(""))) //$NON-NLS-1$
				{
					this.vlCnty.setText(barcode.getBCMyPartObject().getCO());
					found = true;
				}
			}

			if ((rc != 0) || (!found && !this.tfInput.getText().equals(""))) //$NON-NLS-1$
			{
				String erms = barcode.getBCMyPartObject().getErrorString();
				if (erms.length() == 0)
				{
					erms = MFSBCBarcodeDecoder.INVALID_BARCODE_ERMS;
				}
				IGSMessageBox.showOkMB(this, null, erms, null);
			}
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(this, null, null, e);
		}

		this.toFront();
		this.tfInput.setText(""); //$NON-NLS-1$
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
			else if (source == this.pbCountry)
			{
				listCoo();
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
			if (source instanceof JButton)
			{
				JButton button = (JButton) source;
				button.requestFocusInWindow();
				button.doClick();
			}
			else if (source == this.tfInput)
			{
				recvInput();
			}
		}
		else if (keyCode == KeyEvent.VK_F2)
		{
			this.pbPrint.requestFocusInWindow();
			this.pbPrint.doClick();
		}
		else if (keyCode == KeyEvent.VK_F3)
		{
			this.pbCountry.requestFocusInWindow();
			this.pbCountry.doClick();
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
