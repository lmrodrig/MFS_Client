/* @ Copyright IBM Corporation 2005, 2010. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2005-07-20      31096SA  J Rebhahn        -Initial version
 * 2007-02-21      34242JR  R Prechel        -Java 5 version
 * 2010-05-16  ~01 46870JL  Edgar Mercado    -Modify CARTON label to get CNAMETXT and CNAME rules
 *                                            to print COO legend.
 * 2010-11-01  ~02 49513JM	Toribio H.   	 -Make RTV_SIGPN Cacheable
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.dialog;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
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
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSPrintingMethods;
import com.ibm.rchland.mfgapps.mfscommon.MFSConfig;
import com.ibm.rchland.mfgapps.mfscommon.comm.trx.RTV_SIGPN;

/**
 * <code>MFSRebrandDialog</code> is the <code>MFSActionableDialog</code>
 * used to print a rebrand label.
 * @author The MFS Client Development Team
 */
@SuppressWarnings("serial")
public class MFSRebrandDialog
	extends MFSActionableDialog
{
	/** The input <code>JTextField</code>. */
	private JTextField tfInput = createTextField(MFSConstants.LARGE_TF_COLS, 22);

	/** The machine type value <code>JLabel</code>. */
	private JLabel vlMatp;

	/** The  machine type model value <code>JLabel</code>. */
	private JLabel vlMmdl;

	/** The plant code value <code>JLabel</code>. */
	private JLabel vlPlantCode;

	/** The machine serial value <code>JLabel</code>. */
	private JLabel vlMcsn;
	
	/** The print <code>JButton</code>. */
	private JButton pbPrint = createButton("Print", 'P'); //$NON-NLS-1$

	/** The exit <code>JButton</code>. */
	private JButton pbExit = createButton("Exit", 'x'); //$NON-NLS-1$
	
	/** Stores the machine type model passed into the constructor. */
	private String rebrandMmdl = null;

	/** Stores the machine type passed into the constructor. */
	private String rebrandMatp = null;

	/** Stores the product line passed into the constructor. */
	private String rebrandPrln = null;

	/** Stores the work unit control number passed into the constructor. */
	private String rebrandMctl = null;

	/** The quantity to print. */
	private int printqty = 1;

	/**
	 * Constructs a new <code>MFSRebrandDialog</code>.
	 * @param parent the <code>MFSFrame</code> that caused this
	 *        <code>MFSRebrandDialog</code> to be displayed
	 * @param mmdl the machine type model
	 * @param matp the machine type
	 * @param prln the product line
	 * @param mctl the work unit control number
	 * @param qty the quantity to print
	 */
	public MFSRebrandDialog(MFSFrame parent, String mmdl, String matp, String prln,
							String mctl, int qty)
	{
		super(parent, "Scan in 1S Barcode for Rebrand"); //$NON-NLS-1$
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.rebrandMmdl = mmdl;
		this.rebrandMatp = matp;
		this.rebrandPrln = prln;
		this.rebrandMctl = mctl;
		this.printqty = qty;
		createLayout();
		addMyListeners();
	}
	
	/** Adds this dialog's <code>Component</code>s to the layout. */
	private void createLayout()
	{
		JLabel matpLabel = createSmallNameLabel("Machine Type"); //$NON-NLS-1$
		this.vlMatp = createSmallValueLabel(matpLabel);
		JLabel mmdlLabel = createSmallNameLabel("Model"); //$NON-NLS-1$
		this.vlMmdl = createSmallValueLabel(mmdlLabel);
		JLabel pcLabel = createSmallNameLabel("Plant Code"); //$NON-NLS-1$
		this.vlPlantCode = createSmallValueLabel(pcLabel);
		JLabel mcsnLabel = createSmallNameLabel("Machine Serial"); //$NON-NLS-1$
		this.vlMcsn = createSmallValueLabel(mcsnLabel);

		JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 50, 0));
		buttonPanel.add(this.pbPrint);
		buttonPanel.add(this.pbExit);

		JPanel contentPaneCenter = new JPanel(new GridBagLayout());
		contentPaneCenter.setBorder(BorderFactory.createEmptyBorder(20, 40, 10, 40));
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(4, 4, 4, 4);
		contentPaneCenter.add(this.tfInput, gbc);

		gbc.gridy = 1;
		gbc.weightx = 0.0;
		gbc.gridwidth = 1;
		contentPaneCenter.add(matpLabel, gbc);

		gbc.gridy++;
		contentPaneCenter.add(mmdlLabel, gbc);

		gbc.gridy++;
		contentPaneCenter.add(pcLabel, gbc);

		gbc.gridy++;
		contentPaneCenter.add(mcsnLabel, gbc);

		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.weightx = 1.0;
		contentPaneCenter.add(this.vlMatp, gbc);

		gbc.gridy++;
		contentPaneCenter.add(this.vlMmdl, gbc);

		gbc.gridy++;
		contentPaneCenter.add(this.vlPlantCode, gbc);

		gbc.gridy++;
		contentPaneCenter.add(this.vlMcsn, gbc);

		gbc.gridx = 0;
		gbc.gridy++;
		gbc.gridwidth = 2;
		gbc.weighty = 1.0;
		gbc.insets = new Insets(30, 4, 4, 4);
		contentPaneCenter.add(buttonPanel, gbc);

		setContentPane(contentPaneCenter);
		pack();
	}
	
	/** Adds the listeners to this dialog's <code>Component</code>s. */
	private void addMyListeners()
	{
		this.pbPrint.addActionListener(this);
		this.pbExit.addActionListener(this);
		this.pbPrint.addKeyListener(this);
		this.pbExit.addKeyListener(this);
		this.tfInput.addKeyListener(this);
	}

	/** Prints the label(s) if the input is valid. */
	public void printLabel()
	{
		// If bad machine serial number
		if (this.vlMcsn.getText().trim().equals("")) //$NON-NLS-1$
		{
			IGSMessageBox.showOkMB(this, null, "Invalid Machine Serial Number!", null); //$NON-NLS-1$
			this.tfInput.requestFocusInWindow();
			return;
		}
		// If bad machine type
		else if (this.vlMatp.getText().trim().equals("")) //$NON-NLS-1$
		{
			IGSMessageBox.showOkMB(this, null, "Invalid Machine Type!", null); //$NON-NLS-1$
			this.tfInput.requestFocusInWindow();
			return;
		}
		// If bad plant code
		else if (this.vlPlantCode.getText().trim().equals("")) //$NON-NLS-1$
		{
			IGSMessageBox.showOkMB(this, null, "Invalid Plant Code!", null); //$NON-NLS-1$
			this.tfInput.requestFocusInWindow();
			return;
		}
		// If bad model
		else if (this.vlMmdl.getText().trim().equals("")) //$NON-NLS-1$
		{
			IGSMessageBox.showOkMB(this, null, "Invalid Model Number!", null); //$NON-NLS-1$
			this.tfInput.requestFocusInWindow();
			return;
		}
		// If data is good to print
		else
		{
			// Get the 2 char plant code
			String plantcode = this.vlPlantCode.getText().toUpperCase().trim().substring(0, 2);
			// Get the last 5 chars of the machine serial number
			String machserial = this.vlMcsn.getText().toUpperCase().trim();
			machserial = machserial.substring(machserial.length() - 5);

			//If Rebrand MATP is blank, then use scanned in MATP
			if (this.rebrandMatp.trim().equals("")) //$NON-NLS-1$
			{
				this.rebrandMatp = this.vlMatp.getText().toUpperCase().trim();
			}

			final MFSConfig config = MFSConfig.getInstance();

			// Print each label that is configured
			if (config.containsConfigEntry("MATPMCSN")) //$NON-NLS-1$
			{
				MFSPrintingMethods.matpmcsn(this.rebrandMatp, this.rebrandMmdl, 
						plantcode + "-" + machserial, this.printqty, getParentFrame()); //$NON-NLS-1$
			}

			if (config.containsConfigEntry("MATPMCSN1S")) //$NON-NLS-1$
			{
				MFSPrintingMethods.matpmcsn1s(this.rebrandMatp, this.rebrandMmdl,
						plantcode, machserial, this.printqty, getParentFrame());
			}

			if (config.containsConfigEntry("PNMCSN1S")) //$NON-NLS-1$
			{
				MFSPrintingMethods.pnmcsn1s(this.rebrandPrln, plantcode, machserial,
						this.printqty, getParentFrame());
			}
			if (config.containsConfigEntry("CARTON")) //$NON-NLS-1$
			{
				// Need to call RTV_SIGPN to get the COOC value
				RTV_SIGPN rtvSigPN = new RTV_SIGPN(this); //~02
				rtvSigPN.setInputWU(this.rebrandMctl);
				//If good rc, then get the cooc value and print
				if (rtvSigPN.execute())
				{
					MFSPrintingMethods.carton(this.rebrandMatp, this.rebrandMmdl,
							plantcode, machserial, this.rebrandPrln, 
							rtvSigPN.getOutputCOO(), 
							rtvSigPN.getOutputCNAME(), 
							this.printqty, getParentFrame());                                                    //~01C
				}
				else
				//Else bad rc, so display error message string
				{
					String errorString = rtvSigPN.getErrorMessage();
					// If no error message returned, then set it.
					if (errorString.trim().equals("")) //$NON-NLS-1$
					{
						errorString = "A significant part COO value was not found for work unit " //$NON-NLS-1$
								+ this.rebrandMctl + " for Rebrand Carton label"; //$NON-NLS-1$
					}
					IGSMessageBox.showEscapeMB(this, null, errorString, null);
				}
			} //end if CARTON label is configured
		} //end if barcode decoded correctly to print
	}
	
	/** Processes the user input. */
	public void recvInput()
	{
		try
		{
			/* Perform the recvInput method. */
			boolean found = false;
			String errorString = MFSBCBarcodeDecoder.INVALID_BARCODE_ERMS;
			int rc = 0;
			
			MFSBCBarcodeDecoder decoder = MFSBCBarcodeDecoder.getInstance();
			decoder.setMyBarcodeInput(this.tfInput.getText().toUpperCase());
			decoder.setMyBCPartObject(new MFSBCPartObject());

			/* setup barcode indicator values */
			MFSBCIndicatorValue bcIndVal = new MFSBCIndicatorValue();
			// Set the indicators required to decode the barcode
			bcIndVal.setCMTI("1"); //$NON-NLS-1$
			bcIndVal.setMMDL("1"); //$NON-NLS-1$
			decoder.setMyBCIndicatorValue(bcIndVal);
			
			if (decoder.decodeBarcodeFor(this))
			{
				rc = decoder.getBCMyPartObject().getRC();
			}
			else
			{
				rc = 10;
				errorString = MFSBCBarcodeDecoder.DECODE_TIMED_OUT_ERMS;
			}

			if (rc == 0 && !found)
			{
				//If the M/T was decoded, then set it.
				if (!decoder.getBCMyPartObject().getMTC().equals("")) //$NON-NLS-1$
				{
					this.vlMatp.setText(decoder.getBCMyPartObject().getMTC());
				}
				//If the Model is decoded, then set it.
				if (!(decoder.getBCMyPartObject().getMD().equals(""))) //$NON-NLS-1$
				{
					this.vlMmdl.setText(decoder.getBCMyPartObject().getMD());
				}
				//If the Machine Serial is decoded, then set it.
				if (!(decoder.getBCMyPartObject().getMSN().equals(""))) //$NON-NLS-1$
				{
					// Set the machine serial
					String mcsn = decoder.getBCMyPartObject().getMSN();
					this.vlMcsn.setText(mcsn.substring(mcsn.length() - 7));
					this.vlPlantCode.setText(mcsn.substring(0, 2));
				}
				//If the Plant Code is decoded, then set it.
				if (!(decoder.getBCMyPartObject().getPC().equals(""))) //$NON-NLS-1$
				{
					// Set the Plant Code
					this.vlPlantCode.setText(decoder.getBCMyPartObject().getPC());
				}

				// If all 4 values have been decoded successfully, then call print.
				if (!this.vlMcsn.getText().equals("") //$NON-NLS-1$
					&& !this.vlMatp.getText().equals("") //$NON-NLS-1$
					&& !this.vlPlantCode.getText().equals("") //$NON-NLS-1$
					&& !this.vlMmdl.getText().equals("")) //$NON-NLS-1$
				{
					printLabel();
				}
			}
			else
			{
				IGSMessageBox.showOkMB(this, null, errorString, null);
			}
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(this, null, null, e);
		}

		this.toFront();
		this.tfInput.setText(""); //$NON-NLS-1$
		this.update(getGraphics());
	}

	/**
	 * Invoked when an action occurs.
	 * @param ae the <code>ActionEvent</code>
	 */
	public void actionPerformed(ActionEvent ae)
	{
		final Object source = ae.getSource();
		if (source == this.pbExit)
		{
			this.dispose();
		}
		else if (source == this.pbPrint)
		{
			printLabel();
		}
	}

	/**
	 * Invoked when a key is pressed.
	 * @param ke the <code>KeyEvent</code>
	 */
	public void keyPressed(KeyEvent ke)
	{
		final int keyCode = ke.getKeyCode();
		if(keyCode == KeyEvent.VK_ENTER)
		{
			final Object source = ke.getSource();
			if(source == this.pbExit)
			{
				this.pbExit.requestFocusInWindow();
				this.pbExit.doClick();
			}
			else if(source == this.pbPrint)
			{
				this.pbPrint.requestFocusInWindow();
				this.pbPrint.doClick();
			}
			else if(source == this.tfInput)
			{
				recvInput();
			}
		}
		else if(keyCode == KeyEvent.VK_ESCAPE)
		{
			this.pbExit.requestFocusInWindow();
			this.pbExit.doClick();
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
			this.tfInput.requestFocusInWindow();
		}
	}
}
