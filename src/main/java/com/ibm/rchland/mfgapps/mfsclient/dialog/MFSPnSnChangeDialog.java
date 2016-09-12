/* @ Copyright IBM Corporation 2005, 2007. All rights reserved.
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
 * <code>MFSPnSnChangeDialog</code> is the <code>MFSActionableDialog</code>
 * used to change a part's part number and/or serial number.
 * @author The MFS Client Development Team
 */
public class MFSPnSnChangeDialog
	extends MFSActionableDialog
{
	private static final long serialVersionUID = 1L;
	/** The current data <code>JTextField</code>. */
	private JTextField tfCurrent = createTextField(MFSConstants.MEDIUM_TF_COLS, 0);

	/** The current part number value <code>JLabel</code>. */
	private JLabel vlCurrentPN;

	/** The current serial number value <code>JLabel</code>. */
	private JLabel vlCurrentSN;

	/** The new data <code>JTextField</code>. */
	private JTextField tfNew = createTextField(MFSConstants.MEDIUM_TF_COLS, 0);

	/** The new part number value <code>JLabel</code>. */
	private JLabel vlNewPN;

	/** The new serial number value <code>JLabel</code>. */
	private JLabel vlNewSN;

	/** The Make Change <code>JButton</code>. */
	private JButton pbChange = createButton("F2 = Make Change");

	/** The Cancel <code>JButton</code>. */
	private JButton pbCancel = createButton("Esc = Cancel");

	/**
	 * Constructs a new <code>MFSPnSnChangeDialog</code>.
	 * @param parent the <code>MFSFrame</code> that caused this
	 *        <code>MFSPnSnChangeDialog</code> to be displayed
	 */
	public MFSPnSnChangeDialog(MFSFrame parent)
	{
		super(parent, "Change PN/SN");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		createLayout();
		addMyListeners();
	}

	/** Adds this dialog's <code>Component</code> s to the layout. */
	private void createLayout()
	{
		JLabel currentLabel = createSmallNameLabel("Current Part Info:");
		JLabel currentPNLabel = createSmallNameLabel("Current PN:");
		JLabel currentSNLabel = createSmallNameLabel("Current SN:");

		JLabel newLabel = createSmallNameLabel("New Part Info:");
		JLabel newPNLabel = createSmallNameLabel("New PN:");
		JLabel newSNLabel = createSmallNameLabel("New SN:");

		this.vlCurrentPN = createValueLabel(currentPNLabel);
		this.vlCurrentSN = createValueLabel(currentSNLabel);

		this.vlNewPN = createValueLabel(newPNLabel);
		this.vlNewSN = createValueLabel(newSNLabel);

		JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 20, 0));
		buttonPanel.add(this.pbChange);
		buttonPanel.add(this.pbCancel);

		JPanel contentPane = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(40, 10, 14,
						4), 0, 0);

		contentPane.add(currentLabel, gbc);

		gbc.gridy++;
		gbc.insets = new Insets(4, 10, 4, 4);
		contentPane.add(currentPNLabel, gbc);

		gbc.gridy++;
		contentPane.add(currentSNLabel, gbc);

		gbc.gridy++;
		gbc.insets = new Insets(40, 10, 14, 4);
		contentPane.add(newLabel, gbc);

		gbc.gridy++;
		gbc.insets = new Insets(4, 10, 4, 4);
		contentPane.add(newPNLabel, gbc);

		gbc.gridy++;
		contentPane.add(newSNLabel, gbc);

		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(40, 4, 14, 10);
		contentPane.add(this.tfCurrent, gbc);

		gbc.gridy++;
		gbc.insets = new Insets(4, 4, 4, 10);
		contentPane.add(this.vlCurrentPN, gbc);

		gbc.gridy++;
		contentPane.add(this.vlCurrentSN, gbc);

		gbc.gridy++;
		gbc.insets = new Insets(40, 4, 14, 10);
		contentPane.add(this.tfNew, gbc);

		gbc.gridy++;
		gbc.insets = new Insets(4, 4, 4, 10);
		contentPane.add(this.vlNewPN, gbc);

		gbc.gridy++;
		contentPane.add(this.vlNewSN, gbc);

		gbc.gridx = 0;
		gbc.gridy++;
		gbc.gridwidth = 2;
		gbc.weighty = 1;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(20, 10, 20, 10);

		contentPane.add(buttonPanel, gbc);
		
		setContentPane(contentPane);
		setLabelDimensions(this.vlCurrentPN, MFSConstants.MAX_PN_CHARACTERS);
		setLabelDimensions(this.vlCurrentSN, MFSConstants.MAX_SN_CHARACTERS);
		setLabelDimensions(this.vlNewPN, MFSConstants.MAX_PN_CHARACTERS);
		setLabelDimensions(this.vlNewSN, MFSConstants.MAX_SN_CHARACTERS);
		pack();
	}

	/** Adds the listeners to this dialog's <code>Component</code>s. */
	private void addMyListeners()
	{
		this.pbCancel.addActionListener(this);
		this.pbChange.addActionListener(this);
		
		this.tfCurrent.addKeyListener(this);
		this.tfNew.addKeyListener(this);
		this.pbCancel.addKeyListener(this);
		this.pbChange.addKeyListener(this);
	}

	/**
	 * Executes RTV_QD10 to retrieve a list of part numbers from which the user
	 * can select a part number.
	 * @param tense the tense of the part number to select {New, Current}
	 * @return the selected part number
	 */
	private String choosePn(String tense)
	{
		try
		{
			MfsXMLDocument xml_data1 = new MfsXMLDocument("RTV_QD10"); //$NON-NLS-1$
			xml_data1.addOpenTag("DATA"); //$NON-NLS-1$
			xml_data1.addCompleteField("BACK", "QDQDPN"); //$NON-NLS-1$ //$NON-NLS-2$
			xml_data1.addOpenTag("FLD"); //$NON-NLS-1$
			xml_data1.addCompleteField("OPRT", "="); //$NON-NLS-1$ //$NON-NLS-2$
			xml_data1.addCompleteField("NAME", "QDQDSQ"); //$NON-NLS-1$ //$NON-NLS-2$
			xml_data1.addCompleteField("VALU", "'" + this.vlCurrentSN.getText().trim() + "'"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
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

	/** Changes the PN and SN for the part. */
	private void makePnSnChange()
	{
		/* Verify that we have enough data to make the change */
		int rc = verifyData();
		if (rc == 0)
		{
			MfsXMLDocument xml_data = new MfsXMLDocument("CHG_PNSN"); //$NON-NLS-1$
			xml_data.addOpenTag("DATA"); //$NON-NLS-1$
			xml_data.addOpenTag("CURR"); //$NON-NLS-1$
			xml_data.addCompleteField("PN", this.vlCurrentPN.getText().toUpperCase().trim()); //$NON-NLS-1$
			xml_data.addCompleteField("SN", this.vlCurrentSN.getText().toUpperCase().trim()); //$NON-NLS-1$
			xml_data.addCloseTag("CURR"); //$NON-NLS-1$
			xml_data.addOpenTag("NEW"); //$NON-NLS-1$
			xml_data.addCompleteField("PN", this.vlNewPN.getText().toUpperCase().trim()); //$NON-NLS-1$
			xml_data.addCompleteField("SN", this.vlNewSN.getText().toUpperCase().trim()); //$NON-NLS-1$
			xml_data.addCloseTag("NEW"); //$NON-NLS-1$
			xml_data.addCompleteField("USR", MFSConfig.getInstance().getConfigValue("USER")); //$NON-NLS-1$ //$NON-NLS-2$
			xml_data.addCloseTag("DATA"); //$NON-NLS-1$
			xml_data.finalizeXML();

			MFSTransaction chg_pnsn = new MFSXmlTransaction(xml_data.toString());
			chg_pnsn.setActionMessage("Changing PN/SN, Please Wait...");
			MFSComm.getInstance().execute(chg_pnsn, this);
			rc = chg_pnsn.getReturnCode();

			if (rc == 0)
			{
				String msg = "Part information successfully changed.";
				IGSMessageBox.showOkMB(this, null, msg, null);
			}
			else
			{
				IGSMessageBox.showOkMB(this, null, chg_pnsn.getErms(), null);
			}
		}
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

	/**
	 * Verifies the data entered by the user.
	 * @return 0 on success, nonzero on failure
	 */
	private int verifyData()
	{
		if (this.vlCurrentSN.getText().trim().equals("")) //$NON-NLS-1$
		{
			String erms = "You must enter a Current serial number to continue!";
			IGSMessageBox.showOkMB(this, null, erms, null);
			return 1;
		}
		else if (this.vlNewSN.getText().trim().equals("")) //$NON-NLS-1$
		{
			String erms = "You must enter a New serial number to continue!";
			IGSMessageBox.showOkMB(this, null, erms, null);
			return 1;
		}
		else
		{
			if (this.vlCurrentPN.getText().trim().equals("")) //$NON-NLS-1$
			{
				//No part number was entered.
				//Provide the user with a list of parts to pick from.
				String part = choosePn("Current");
				if (part != null)
				{
					this.vlCurrentPN.setText(part);
				}
			}
			if (this.vlNewPN.getText().trim().equals("")) //$NON-NLS-1$
			{
				//No part number was entered.
				//Provide the user with a list of parts to pick from.
				String part = choosePn("New");

				if (part != null)
				{
					this.vlNewPN.setText(part);
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
				makePnSnChange();
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
			else if (source == this.tfCurrent)
			{
				recvInput(this.tfCurrent, this.vlCurrentPN, this.vlCurrentSN);
			}
			else if (source == this.tfNew)
			{
				recvInput(this.tfNew, this.vlNewPN, this.vlNewSN);
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
	 * the {@link #tfCurrent}.
	 * @param we the <code>WindowEvent</code>
	 */
	public void windowOpened(WindowEvent we)
	{
		if (we.getSource() == this)
		{
			this.tfCurrent.requestFocusInWindow();
		}
	}
}
