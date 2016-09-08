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

import java.awt.Cursor;
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

import mfsxml.MfsXMLDocument;
import mfsxml.MfsXMLParser;

import com.ibm.rchland.mfgapps.client.utils.messagebox.IGSMessageBox;
import com.ibm.rchland.mfgapps.mfsclient.MFSActionableDialog;
import com.ibm.rchland.mfgapps.mfsclient.MFSFrame;
import com.ibm.rchland.mfgapps.mfsclient.barcode.MFSBCBarcodeDecoder;
import com.ibm.rchland.mfgapps.mfsclient.barcode.MFSBCIndicatorValue;
import com.ibm.rchland.mfgapps.mfsclient.barcode.MFSBCPartObject;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSConstants;
import com.ibm.rchland.mfgapps.mfscommon.MFSConfig;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSComm;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSTransaction;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSXmlTransaction;

/**
 * <code>MFSSwapPlugCountDialog</code> is the <code>MFSActionableDialog</code>
 * used to swap plug counts.
 * @author The MFS Client Development Team
 */
public class MFSSwapPlugCountDialog
	extends MFSActionableDialog
{
	private static final long serialVersionUID = 1L;
	/** The from data <code>JTextField</code>. */
	private JTextField tfFrom = createTextField(MFSConstants.MEDIUM_TF_COLS, 0);

	/** The from part number value <code>JLabel</code>. */
	private JLabel vlFromPN;

	/** The from serial number value <code>JLabel</code>. */
	private JLabel vlFromSN;

	/** The to data <code>JTextField</code>. */
	private JTextField tfTo = createTextField(MFSConstants.MEDIUM_TF_COLS, 0);

	/** The to part number value <code>JLabel</code>. */
	private JLabel vlToPN;

	/** The to serial number value <code>JLabel</code>. */
	private JLabel vlToSN;

	/** The Make Change <code>JButton</code>. */
	private JButton pbChange = createButton("F2 = Make Change");

	/** The Cancel <code>JButton</code>. */
	private JButton pbCancel = createButton("Esc = Cancel");
	
	/** Set <code>true</code> iff PN/SN information is changed. */
	private boolean fieldChangeMake = false;
	
	/** The part number. */
	private String fieldPN;

	/** The serial number. */
	private String fieldSN;

	/**
	 * Constructs a new <code>MFSSwapPlugCountDialog</code>.
	 * @param parent the <code>MFSFrame</code> that caused this
	 *        <code>MFSSwapPlugCountDialog</code> to be displayed
	 * @param pn the part number
	 * @param sn the serial number
	 */
	public MFSSwapPlugCountDialog(MFSFrame parent, String pn, String sn)
	{
		super(parent, "Swap Plug Count");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.fieldPN = pn;
		this.fieldSN = sn;
		createLayout();
		addMyListeners();
	}

	/** Adds this dialog's <code>Component</code>s to the layout. */
	private void createLayout()
	{
		JLabel fromLabel = createSmallNameLabel("From Part:");
		JLabel fromPNLabel = createSmallNameLabel("PN:");
		JLabel fromSNLabel = createSmallNameLabel("SN:");

		JLabel toLabel = createSmallNameLabel("To Part:");
		JLabel toPNLabel = createSmallNameLabel("PN:");
		JLabel toSNLabel = createSmallNameLabel("SN:");

		this.vlFromPN = createValueLabel(fromPNLabel);
		this.vlFromPN.setText(this.fieldPN);
		
		this.vlFromSN = createValueLabel(fromSNLabel);
		this.vlFromSN.setText(this.fieldSN);

		this.vlToPN = createValueLabel(toPNLabel);
		this.vlToSN = createValueLabel(toSNLabel);

		JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 20, 0));
		buttonPanel.add(this.pbChange);
		buttonPanel.add(this.pbCancel);

		JPanel contentPane = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.NONE, 
				new Insets(40, 10, 14, 4), 0, 0);

		contentPane.add(fromLabel, gbc);

		gbc.gridy++;
		gbc.insets = new Insets(4, 10, 4, 4);
		contentPane.add(fromPNLabel, gbc);

		gbc.gridy++;
		contentPane.add(fromSNLabel, gbc);

		gbc.gridy++;
		gbc.insets = new Insets(40, 10, 14, 4);
		contentPane.add(toLabel, gbc);

		gbc.gridy++;
		gbc.insets = new Insets(4, 10, 4, 4);
		contentPane.add(toPNLabel, gbc);

		gbc.gridy++;
		contentPane.add(toSNLabel, gbc);

		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(40, 4, 14, 10);
		contentPane.add(this.tfFrom, gbc);

		gbc.gridy++;
		gbc.insets = new Insets(4, 4, 4, 10);
		contentPane.add(this.vlFromPN, gbc);

		gbc.gridy++;
		contentPane.add(this.vlFromSN, gbc);

		gbc.gridy++;
		gbc.insets = new Insets(40, 4, 14, 10);
		contentPane.add(this.tfTo, gbc);

		gbc.gridy++;
		gbc.insets = new Insets(4, 4, 4, 10);
		contentPane.add(this.vlToPN, gbc);

		gbc.gridy++;
		contentPane.add(this.vlToSN, gbc);

		gbc.gridx = 0;
		gbc.gridy++;
		gbc.gridwidth = 2;
		gbc.weighty = 1;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(20, 10, 20, 10);

		contentPane.add(buttonPanel, gbc);

		setContentPane(contentPane);
		setLabelDimensions(this.vlFromPN, MFSConstants.MAX_PN_CHARACTERS);
		setLabelDimensions(this.vlFromSN, MFSConstants.MAX_SN_CHARACTERS);
		setLabelDimensions(this.vlToPN, MFSConstants.MAX_PN_CHARACTERS);
		setLabelDimensions(this.vlToSN, MFSConstants.MAX_SN_CHARACTERS);
		pack();
	}

	/** Adds the listeners to this dialog's <code>Component</code>s. */
	private void addMyListeners()
	{
		this.pbCancel.addActionListener(this);
		this.pbChange.addActionListener(this);

		this.tfFrom.addKeyListener(this);
		this.tfTo.addKeyListener(this);
		this.pbCancel.addKeyListener(this);
		this.pbChange.addKeyListener(this);
	}

	/**
	 * Executes RTV_QD10 to retrieve a list of part numbers from which the user
	 * can select a part number.
	 * @param tense the tense of the part number to select {New, Current}
	 * @return the selected part number
	 */
	public String choosePn(String tense)
	{
		try
		{
			MfsXMLDocument xml_data1 = new MfsXMLDocument("RTV_QD10"); //$NON-NLS-1$
			xml_data1.addOpenTag("DATA"); //$NON-NLS-1$
			xml_data1.addCompleteField("BACK", "QDQDPN"); //$NON-NLS-1$ //$NON-NLS-2$
			xml_data1.addOpenTag("FLD"); //$NON-NLS-1$
			xml_data1.addCompleteField("OPRT", "="); //$NON-NLS-1$ //$NON-NLS-2$
			xml_data1.addCompleteField("NAME", "QDQDSQ"); //$NON-NLS-1$ //$NON-NLS-2$
			xml_data1.addCompleteField("VALU", "'" + this.vlToSN.getText().trim() + "'"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			xml_data1.addCloseTag("FLD"); //$NON-NLS-1$
			xml_data1.addCloseTag("DATA"); //$NON-NLS-1$
			xml_data1.finalizeXML();
			
			MFSTransaction rtv_qd10 = new MFSXmlTransaction(xml_data1.toString());
			rtv_qd10.setActionMessage("Retrieving List of Part Numbers, Please Wait...");
			MFSComm.getInstance().execute(rtv_qd10, this);

			if (rtv_qd10.getReturnCode() != 0)
			{
				IGSMessageBox.showOkMB(this, null, rtv_qd10.getErms(), null);
			}

			else
			{
				MfsXMLParser xmlParser = new MfsXMLParser(rtv_qd10.getOutput());
				String tempPN = ""; //$NON-NLS-1$
				int rc = 0;

				try
				{
					tempPN = xmlParser.getField("VALU"); //$NON-NLS-1$
				}
				catch (mfsxml.MISSING_XML_TAG_EXCEPTION e)
				{
					rc = 10;
					String erms = "No Parts Found for this Serial Number!";
					IGSMessageBox.showOkMB(this, null, erms, null);
				}
				if (rc == 0)
				{
					StringBuffer tempPNCollector = new StringBuffer(tempPN);
					tempPN = xmlParser.getNextField("VALU"); //$NON-NLS-1$

					while (!tempPN.equals("")) //$NON-NLS-1$
					{
						tempPNCollector.append(tempPN);
						tempPN = xmlParser.getNextField("VALU"); //$NON-NLS-1$
					}
					
					String title = "Select the " + tense + " Part Number From the List";
					String question = "Pick the " + tense + " Part Number";
					MFSGenericListDialog myQuestionD = new MFSGenericListDialog(getParentFrame(), title, question);
					myQuestionD.setSizeSmall();
					myQuestionD.loadAnswerListModel(tempPNCollector.toString(), 12);
					myQuestionD.setLocationRelativeTo(this);
					myQuestionD.setVisible(true);

					if (myQuestionD.getProceedSelected())
					{
						return myQuestionD.getSelectedListOption();
					}
				}
			}
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(this, null, null, e);
		}
		return null;
	}
	
	/**
	 * Returns the text of the To PN <code>JLabel</code>.
	 * @return the text of the To PN <code>JLabel</code>
	 */
	public String getToPNLabelText()
	{
		return this.vlToPN.getText();
	}
	
	/**
	 * Returns the text of the To SN <code>JLabel</code>.
	 * @return the text of the To SN <code>JLabel</code>
	 */
	public String getToSNLabelText()
	{
		return this.vlToSN.getText();
	}
	
	/**
	 * Returns <code>true</code> iff the PN/SN information was changed.
	 * @return <code>true</code> iff the PN/SN information was changed
	 */
	public boolean isChangeMake() 
	{
		return this.fieldChangeMake;
	}
	
	/**
	 * Processes the input the user entered in the <code>textField</code>.
	 * @param textField the <code>JTextField</code>
	 * @param pnValueLabel the PN value <code>JLabel</code> to populate
	 * @param snValueLabel the SN value <code>JLabel</code> to populate
	 */
	private void recvInput(JTextField textField, JLabel pnValueLabel, JLabel snValueLabel)
	{
		try
		{
			boolean found = false;
			int rc = 0;

			MFSBCBarcodeDecoder barcode = MFSBCBarcodeDecoder.getInstance();
			barcode.setMyBarcodeInput(textField.getText().toUpperCase());
			barcode.setMyBCPartObject(new MFSBCPartObject());

			/* setup barcode indicator values */
			MFSBCIndicatorValue bcIndVal = new MFSBCIndicatorValue();
			bcIndVal.setPNRI("1"); //$NON-NLS-1$

			String sni = MFSConfig.getInstance().getConfigValue("DECODESN"); //$NON-NLS-1$
			if (!sni.equals("Not Found")) //DECONDESN not found //$NON-NLS-1$
			{
				bcIndVal.setCSNI(sni);
			}
			else
			{
				bcIndVal.setCSNI("1"); //$NON-NLS-1$
			}

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
				if (!(barcode.getBCMyPartObject().getPN().equals(""))) //$NON-NLS-1$
				{
					pnValueLabel.setText(barcode.getBCMyPartObject().getPN());
					found = true;
				}
				if (!(barcode.getBCMyPartObject().getSN().equals(""))) //$NON-NLS-1$
				{
					snValueLabel.setText(barcode.getBCMyPartObject().getSN());
					found = true;
				}
				
				if ((rc != 0) || (!found && !textField.getText().equals(""))) //$NON-NLS-1$
				{
					String erms = barcode.getBCMyPartObject().getErrorString();
					if (erms.trim().length() == 0)
					{
						erms = MFSBCBarcodeDecoder.INVALID_BARCODE_ERMS;
					}
					IGSMessageBox.showOkMB(this, null, erms, null);
				}
			}
		}
		catch (Exception e)
		{
			this.setCursor(Cursor.getDefaultCursor());
			IGSMessageBox.showOkMB(this, null, null, e);
		}

		this.toFront();
		textField.setText(""); //$NON-NLS-1$
	}
	
	/** Changes the PN/SN information. */
	public void swapPlug() 
	{
		/* Verify that we have enough data to make the change */
		int rc = verifyData();
		if(rc == 0)
		{

			MfsXMLDocument xml_data = new MfsXMLDocument("SWAP_CPCI"); //$NON-NLS-1$
			xml_data.addOpenTag("DATA"); //$NON-NLS-1$	
			xml_data.addCompleteField("FRPN",this.vlFromPN.getText().toUpperCase().trim()); //$NON-NLS-1$
			xml_data.addCompleteField("FRSQ",this.vlFromSN.getText().toUpperCase().trim()); //$NON-NLS-1$
			xml_data.addCompleteField("TOPN",this.vlToPN.getText().toUpperCase().trim()); //$NON-NLS-1$
			xml_data.addCompleteField("TOSQ",this.vlToSN.getText().toUpperCase().trim()); //$NON-NLS-1$
			xml_data.addCompleteField("USER",MFSConfig.getInstance().getConfigValue("USER")); //$NON-NLS-1$ //$NON-NLS-2$
			xml_data.addCloseTag("DATA"); //$NON-NLS-1$
			xml_data.finalizeXML();

			MFSTransaction swap_cpci = new MFSXmlTransaction(xml_data.toString());
			swap_cpci.setActionMessage("Changing PN/SN, Please Wait...");
			MFSComm.getInstance().execute(swap_cpci, this);
			rc = swap_cpci.getReturnCode();
			
			if (rc == 0)
			{
				this.fieldChangeMake = true;

				String msg = "Part information successfully changed.";
				IGSMessageBox.showOkMB(this, null, msg, null);
			}
			else
			{
				IGSMessageBox.showOkMB(this, null, swap_cpci.getErms(), null);
			}
		}
	}
	
	/**
	 * Verifies the data entered by the user.
	 * @return 0 on success, nonzero on failure
	 */
	private int verifyData()
	{
		if (this.vlFromSN.getText().trim().equals("")) //$NON-NLS-1$
		{
			String erms = "You must enter a Current serial number to continue!";
			IGSMessageBox.showOkMB(this, null, erms, null);
			return 1;
		}
		else if (this.vlToSN.getText().trim().equals("")) //$NON-NLS-1$
		{
			String erms = "You must enter a New serial number to continue!";
			IGSMessageBox.showOkMB(this, null, erms, null);
			return 1;
		}
		else
		{
			if (this.vlFromPN.getText().trim().equals("")) //$NON-NLS-1$
			{
				//No part number was entered.
				//Provide the user with a list of parts to pick from.
				String part = choosePn("Current");
				if (part != null)
				{
					this.vlFromPN.setText(part);
				}
			}
			if (this.vlToPN.getText().trim().equals("")) //$NON-NLS-1$
			{
				//No part number was entered.
				//Provide the user with a list of parts to pick from.
				String part = choosePn("New");

				if (part != null)
				{
					this.vlToPN.setText(part);
				}
			}
		}
		this.update(getGraphics());
		return 0;
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
			if (source == this.pbChange)
			{
				swapPlug();
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
			final Object source = ke.getSource();
			if (source == this.pbChange)
			{
				this.pbChange.requestFocusInWindow();
				this.pbChange.doClick();
			}
			else if (source == this.pbCancel)
			{
				this.pbCancel.requestFocusInWindow();
				this.pbCancel.doClick();
			}
			else if (source == this.tfFrom)
			{
				recvInput(this.tfFrom, this.vlFromPN, this.vlFromSN);
			}
			else if (source == this.tfTo)
			{
				recvInput(this.tfTo, this.vlToPN, this.vlToSN);
			}
		}
		else if (keyCode == KeyEvent.VK_F2)
		{
			this.pbChange.requestFocusInWindow();
			this.pbChange.doClick();
		}
		else if (keyCode == KeyEvent.VK_ESCAPE)
		{
			this.pbCancel.requestFocusInWindow();
			this.pbCancel.doClick();
		}
	}

	/**
	 * Invoked the first time a window is made visible. Requests the focus for
	 * the {@link #tfFrom}.
	 * @param we the <code>WindowEvent</code>
	 */
	public void windowOpened(WindowEvent we)
	{
		if (we.getSource() == this)
		{
			this.tfFrom.requestFocusInWindow();
		}
	}
}
