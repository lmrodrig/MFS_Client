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
 * <code>MFSPNSNDialog</code> is the <code>MFSActionableDialog</code> used
 * to prompt the user for a part number and serial number.
 * @author The MFS Client Development Team
 */
public class MFSPNSNDialog
	extends MFSActionableDialog
{
	private static final long serialVersionUID = 1L;

	/** The input <code>JTextField</code>. */
	private JTextField tfInput = createTextField(MFSConstants.LARGE_TF_COLS, 0);

	/** The Get PN List <code>JButton</code>. */
	private JButton pbGetPnList = createButton("Get PN List", 'G');

	/** The part number value <code>JLabel</code>. */
	private JLabel vlPN;

	/** The serial number value <code>JLabel</code>. */
	private JLabel vlSN;

	/** The Enter <code>JButton</code>. */
	private JButton pbEnter = createButton("Enter", 'E');

	/** The Cancel <code>JButton</code>. */
	private JButton pbCancel = createButton("Cancel", 'n');

	/** Set <code>true</code> iff the enter button is pressed. */
	private boolean fieldPressedEnter = false;

	/**
	 * Constructs a new <code>MFSPNSNDialog</code>.
	 * @param parent the <code>MFSFrame</code> that caused this
	 *        <code>MFSPNSNDialog</code> to be displayed
	 */
	public MFSPNSNDialog(MFSFrame parent)
	{
		super(parent, "Enter PN/SN Information");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		createLayout();
		addMyListeners();
	}

	/** Adds this dialog's <code>Component</code>s to the layout. */
	private void createLayout()
	{
		this.pbGetPnList.setEnabled(false);

		JLabel partNameLabel = createNameLabel("Part Number:");
		JLabel serialNameLabel = createNameLabel("Serial Number:");

		this.vlPN = createValueLabel(partNameLabel);
		this.vlSN = createValueLabel(serialNameLabel);

		JPanel labelPanel = new JPanel(new GridLayout(2, 2, 10, 0));
		labelPanel.add(partNameLabel);
		labelPanel.add(this.vlPN);
		labelPanel.add(serialNameLabel);
		labelPanel.add(this.vlSN);

		JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 20, 0));
		buttonPanel.add(this.pbEnter);
		buttonPanel.add(this.pbCancel);

		JPanel contentPane = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, 
				new Insets(20, 30, 10, 30), 0, 0);

		contentPane.add(this.tfInput, gbc);

		gbc.gridy++;
		gbc.insets = new Insets(0, 30, 10, 30);
		contentPane.add(this.pbGetPnList, gbc);

		gbc.gridy++;
		contentPane.add(labelPanel, gbc);

		gbc.gridy++;
		contentPane.add(buttonPanel, gbc);

		setContentPane(contentPane);
		setLabelDimensions(this.vlPN, MFSConstants.MAX_PN_CHARACTERS);
		setLabelDimensions(this.vlSN, MFSConstants.MAX_SN_CHARACTERS);
		pack();
	}

	/** Adds the listeners to this dialog's <code>Component</code>s. */
	private void addMyListeners()
	{
		this.pbEnter.addActionListener(this);
		this.pbCancel.addActionListener(this);
		this.pbGetPnList.addActionListener(this);

		this.tfInput.addKeyListener(this);
		this.pbEnter.addKeyListener(this);
		this.pbCancel.addKeyListener(this);
	}

	/**
	 * Executes RTV_QD10 to retrieve a list of part numbers from which the user
	 * can select a part number.
	 * @return the selected part number
	 */
	private String choosePn()
	{
		try
		{
			MfsXMLDocument xmlData = new MfsXMLDocument("RTV_QD10"); //$NON-NLS-1$
			xmlData.addOpenTag("DATA"); //$NON-NLS-1$
			xmlData.addCompleteField("BACK", "QDQDPN"); //$NON-NLS-1$ //$NON-NLS-2$
			xmlData.addOpenTag("FLD"); //$NON-NLS-1$
			xmlData.addCompleteField("OPRT", "="); //$NON-NLS-1$ //$NON-NLS-2$
			xmlData.addCompleteField("NAME", "QDQDSQ"); //$NON-NLS-1$ //$NON-NLS-2$
			xmlData.addCompleteField("VALU", "'" + this.vlSN.getText().trim() + "'"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			xmlData.addCloseTag("FLD"); //$NON-NLS-1$
			xmlData.addCloseTag("DATA"); //$NON-NLS-1$
			xmlData.finalizeXML();

			MFSTransaction rtv_qd10 = new MFSXmlTransaction(xmlData.toString());
			rtv_qd10.setActionMessage("Retrieving List of Part Numbers, Please Wait...");
			MFSComm.getInstance().execute(rtv_qd10, this);

			if (rtv_qd10.getReturnCode() != 0)
			{
				IGSMessageBox.showOkMB(getParentFrame(), null, rtv_qd10.getErms(), null);
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

					String title = "Select a Part Number From the List";
					String question = "Pick a Part Number";
					MFSGenericListDialog myQuestionD = new MFSGenericListDialog(getParentFrame(), title, question);
					myQuestionD.setSizeSmall();

					myQuestionD.loadAnswerListModelNoDups(tempPNCollector.toString(), 12);
					myQuestionD.setLocationRelativeTo(this);
					myQuestionD.setVisible(true);

					if (myQuestionD.getProceedSelected())
					{
						this.vlPN.setText(myQuestionD.getSelectedListOption());
						update(getGraphics());
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

	/** Invoked when {@link #pbEnter} is pressed. */
	private void enter()
	{
		if (!pnIsBlank() && !snIsBlank())
		{
			this.fieldPressedEnter = true;
			this.dispose();
		}
		else if (pnIsBlank() && !snIsBlank())
		{
			String erms = "No Valid Part Number Entered!";
			IGSMessageBox.showOkMB(this, null, erms, null);
			this.tfInput.requestFocusInWindow();
		}
		else
		{
			String erms = "Invalid Serial Number Entered";
			IGSMessageBox.showOkMB(this, null, erms, null);
			this.tfInput.requestFocusInWindow();
		}
	}

	/**
	 * Returns <code>true</code> iff the enter button was pressed.
	 * @return <code>true</code> iff the enter button was pressed
	 */
	public boolean getPressedEnter()
	{
		return this.fieldPressedEnter;
	}

	/**
	 * Returns the text of {@link #vlPN}.
	 * @return the part number text
	 */
	public String getPNText()
	{
		return this.vlPN.getText();
	}

	/**
	 * Returns the text of {@link #vlSN}.
	 * @return the serial number text
	 */
	public String getSNText()
	{
		return this.vlSN.getText();
	}

	/**
	 * Returns <code>true</code> iff the text of {@link #vlPN} is
	 * <code>null</code> or blank.
	 * @return <code>true</code> iff the text of {@link #vlPN} is
	 *         <code>null</code> or blank
	 */
	private boolean pnIsBlank()
	{
		String text = this.vlPN.getText();
		return (text == null) || text.equals(""); //$NON-NLS-1$
	}

	/** Processes the input the user entered in the <code>JTextField</code>. */
	private void recvInput()
	{
		try
		{
			if (this.tfInput.getText().equals("")) //$NON-NLS-1$
			{
				this.toFront();

				if (!pnIsBlank() && !snIsBlank())
				{
					this.fieldPressedEnter = true;
					this.dispose();
				}
				return;
			}
			
			boolean found = false;
			int rc = 0;

			MFSBCBarcodeDecoder barcode = MFSBCBarcodeDecoder.getInstance();
			barcode.setMyBarcodeInput(this.tfInput.getText().toUpperCase());
			barcode.setMyBCPartObject(new MFSBCPartObject());

			/* setup barcode indicator values */
			MFSBCIndicatorValue bcIndVal = new MFSBCIndicatorValue();
			bcIndVal.setPNRI("1"); //$NON-NLS-1$
			String sni = MFSConfig.getInstance().getConfigValue("DECODESN"); //$NON-NLS-1$
			if (!sni.equals("Not Found")) //$NON-NLS-1$
			{
				bcIndVal.setCSNI(sni);
			}
			else
			{
				bcIndVal.setCSNI("1"); //$NON-NLS-1$
			}
			barcode.setMyBCIndicatorValue(bcIndVal);

			if (barcode.decodeBarcodeFor(this))
			{
				rc = barcode.getBCMyPartObject().getRC();
			}
			else
			{
				rc = 10;
				barcode.getBCMyPartObject().setErrorString(
						MFSBCBarcodeDecoder.DECODE_TIMED_OUT_ERMS);
			}

			if (rc == 0 && !found)
			{
				if (!(barcode.getBCMyPartObject().getWU().equals(""))) //$NON-NLS-1$
				{
					String erms = "Invalid Barcode entered! Must be 11S, Part Number, or Serial Number Barcode.";
					IGSMessageBox.showOkMB(this, null, erms, null);
					this.tfInput.setText(""); //$NON-NLS-1$	
				}
				if (!(barcode.getBCMyPartObject().getPN().equals(""))) //$NON-NLS-1$
				{
					this.vlPN.setText(barcode.getBCMyPartObject().getPN());
					found = true;
				}
				if (!(barcode.getBCMyPartObject().getSN().equals(""))) //$NON-NLS-1$
				{
					this.vlSN.setText(barcode.getBCMyPartObject().getSN());
					this.pbGetPnList.setEnabled(true);
					found = true;
				}
			}
			
			if ((rc != 0) || (!found && !this.tfInput.getText().equals(""))) //$NON-NLS-1$
			{
				String erms = barcode.getBCMyPartObject().getErrorString();
				if (erms.trim().length() == 0)
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
	 * Sets the text of {@link #vlPN}.
	 * @param pn the part number text
	 */
	public void setPNText(String pn)
	{
		this.vlPN.setText(pn);
	}

	/**
	 * Sets the text of {@link #vlSN}.
	 * @param sn the serial number text
	 */
	public void setSNText(String sn)
	{
		this.vlSN.setText(sn);
	}

	/**
	 * Returns <code>true</code> iff the text of {@link #vlSN} is
	 * <code>null</code> or blank.
	 * @return <code>true</code> iff the text of {@link #vlSN} is
	 *         <code>null</code> or blank
	 */
	private boolean snIsBlank()
	{
		String text = this.vlSN.getText();
		return (text == null) || text.equals(""); //$NON-NLS-1$
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
			if (source == this.pbEnter)
			{
				if (pnIsBlank() && snIsBlank())
				{
					recvInput();
				}
				enter();
			}
			else if (source == this.pbCancel)
			{
				this.dispose();
			}
			else if (source == this.pbGetPnList)
			{
				choosePn();
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
			if (source == this.tfInput)
			{
				recvInput();
			}
			else if (source == this.pbEnter)
			{
				this.pbEnter.requestFocusInWindow();
				this.pbEnter.doClick();
			}
			else if (source == this.pbCancel)
			{
				this.pbCancel.requestFocusInWindow();
				this.pbEnter.doClick();
			}
		}
		else if (keyCode == KeyEvent.VK_ESCAPE)
		{
			this.dispose();
		}
	}

	/**
	 * Invoked the first time a window is made visible.
	 * Requests the focus for the {@link #tfInput}.
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
