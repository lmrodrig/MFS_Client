/* © Copyright IBM Corporation 2006. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2006-04-26   ~1 29595PT  Luis M.          -Add two new columns to display the QD10 Level
 *                                            and Comp fields and remove Add PN button 
 * 2007-01-25      34242JR  R Prechel        -Java 5 version
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.dialog;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
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
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import mfsxml.MISSING_XML_TAG_EXCEPTION;
import mfsxml.MfsXMLDocument;
import mfsxml.MfsXMLParser;

import com.ibm.rchland.mfgapps.client.utils.messagebox.IGSMessageBox;
import com.ibm.rchland.mfgapps.mfsclient.MFSActionableDialog;
import com.ibm.rchland.mfgapps.mfsclient.MFSFrame;
import com.ibm.rchland.mfgapps.mfsclient.barcode.MFSBCBarcodeDecoder;
import com.ibm.rchland.mfgapps.mfsclient.barcode.MFSBCIndicatorValue;
import com.ibm.rchland.mfgapps.mfsclient.barcode.MFSBCPartObject;
import com.ibm.rchland.mfgapps.mfsclient.renderer.MFSClogPartCheckListRenderer;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSConstants;
import com.ibm.rchland.mfgapps.mfscommon.MFSConfig;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSComm;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSTransaction;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSXmlTransaction;

/**
 * <code>MFSClogPartCheckDialog</code> is the <code>MFSActionableDialog</code>
 * used to perform the part check operation.
 * @author The MFS Client Development Team
 */
public class MFSClogPartCheckDialog
	extends MFSActionableDialog
{
	private static final long serialVersionUID = 1L;
	/** The <code>JList</code>. */
	private JList lstPNSN = createList();

	/** The <code>JScrollPane</code> that contains the list. */
	private JScrollPane spPNSN = new JScrollPane(this.lstPNSN,
			ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
			ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

	/** The input <code>JTextField</code>. */
	private JTextField tfInput = createTextField(MFSConstants.MEDIUM_TF_COLS, 22);

	/** The part number value <code>JLabel</code>. */
	private JLabel vlPN;

	/** The serial number value <code>JLabel</code>. */
	private JLabel vlSN;

	/** The Remove <code>JButton</code>. */
	private JButton pbRemove = createButton("Remove PN", 'R');

	/** The Check <code>JButton</code>. */
	private JButton pbCheck = createButton("Check", 'C');

	/** The Cancel/Exit <code>JButton</code>. */
	private JButton pbCancel = createButton("Cancel/Exit", 'n');

	/** The <code>ListModel</code> for {@link #lstPNSN}. */
	private DefaultListModel fieldPNSNListModel = new DefaultListModel();
	
	/** The option the user selected on the <code>MFSDX10Dialog</code>. */
	private String fieldOption;

	/**
	 * Constructs a new <code>MFSClogPartCheckDialog</code>.
	 * @param parent the <code>MFSFrame</code> that caused this
	 *        <code>MFSClogPartCheckDialog</code> to be displayed
	 * @param option option the user selected on the <code>MFSDX10Dialog</code>
	 */
	public MFSClogPartCheckDialog(MFSFrame parent, String option)
	{
		super(parent, "Part Check");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		this.fieldOption = option;
		this.lstPNSN.setModel(this.fieldPNSNListModel);
		this.lstPNSN.setCellRenderer(new MFSClogPartCheckListRenderer());
		
		createLayout();
		addMyListeners();
	}

	/** Adds this dialog's <code>Component</code> s to the layout. */
	private void createLayout()
	{
		JLabel descPrevLabel = createSmallNameLabel("DX           QD");
		JLabel descLabel = createSmallNameLabel("Part Number             Serial Number        CCIN       Level       Level    Comp       Flag           Value");

		this.lstPNSN.setVisibleRowCount(22);
		
		JLabel partLabel = createSmallNameLabel("  Part:");
		JLabel serialLabel = createSmallNameLabel("Serial:");
		this.vlPN = createSmallValueLabel(partLabel);
		this.vlSN = createSmallValueLabel(serialLabel);
		
		JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 20, 20));
		buttonPanel.add(this.pbRemove);
		buttonPanel.add(this.pbCheck);
		buttonPanel.add(this.pbCancel);

		JPanel contentPane = new JPanel(new GridBagLayout());

		//~1A Add descPrevLabel
		GridBagConstraints constraintsDescJLabelPrev = new GridBagConstraints();
		constraintsDescJLabelPrev.gridx = 0;
		constraintsDescJLabelPrev.gridy = 0;
		constraintsDescJLabelPrev.anchor = GridBagConstraints.WEST;
		constraintsDescJLabelPrev.insets = new Insets(11, 288, 0, 4);
		contentPane.add(descPrevLabel, constraintsDescJLabelPrev);

		GridBagConstraints constraintsDescJLabel = new GridBagConstraints();
		constraintsDescJLabel.gridx = 0;
		constraintsDescJLabel.gridy = 1;
		constraintsDescJLabel.anchor = GridBagConstraints.NORTHWEST;
		constraintsDescJLabel.insets = new Insets(0, 17, 0, 120);
		contentPane.add(descLabel, constraintsDescJLabel);

		GridBagConstraints constraintsCMNTScrollpane = new GridBagConstraints();
		constraintsCMNTScrollpane.gridx = 0;
		constraintsCMNTScrollpane.gridy = 2;
		constraintsCMNTScrollpane.gridheight = 3;
		constraintsCMNTScrollpane.fill = GridBagConstraints.BOTH;
		constraintsCMNTScrollpane.weightx = 1.0;
		constraintsCMNTScrollpane.weighty = 1.0;
		constraintsCMNTScrollpane.insets = new Insets(0, 15, 8, 4);
		contentPane.add(this.spPNSN, constraintsCMNTScrollpane);

		GridBagConstraints constraintsInputTextField = new GridBagConstraints();
		constraintsInputTextField.gridx = 1;
		constraintsInputTextField.gridy = 1;
		constraintsInputTextField.gridwidth = 2;
		constraintsInputTextField.fill = GridBagConstraints.HORIZONTAL;
		constraintsInputTextField.weightx = 1.0;
		constraintsInputTextField.insets = new Insets(0, 10, 0, 10);
		contentPane.add(this.tfInput, constraintsInputTextField);

		GridBagConstraints constraintsPNJLabel = new GridBagConstraints();
		constraintsPNJLabel.gridx = 1;
		constraintsPNJLabel.gridy = 2;
		constraintsPNJLabel.insets = new Insets(4, 10, 4, 4);
		contentPane.add(partLabel, constraintsPNJLabel);

		GridBagConstraints constraintsPNValueJLabel = new GridBagConstraints();
		constraintsPNValueJLabel.gridx = 2;
		constraintsPNValueJLabel.gridy = 2;
		constraintsPNValueJLabel.anchor = GridBagConstraints.WEST;
		constraintsPNValueJLabel.insets = new Insets(4, 4, 4, 4);
		contentPane.add(this.vlPN, constraintsPNValueJLabel);

		GridBagConstraints constraintsSNJLabel = new GridBagConstraints();
		constraintsSNJLabel.gridx = 1;
		constraintsSNJLabel.gridy = 3;
		constraintsSNJLabel.insets = new Insets(4, 10, 4, 4);
		contentPane.add(serialLabel, constraintsSNJLabel);

		GridBagConstraints constraintsSNValueJLabel = new GridBagConstraints();
		constraintsSNValueJLabel.gridx = 2;
		constraintsSNValueJLabel.gridy = 3;
		constraintsSNValueJLabel.anchor = GridBagConstraints.WEST;
		constraintsSNValueJLabel.insets = new Insets(4, 4, 4, 4);
		contentPane.add(this.vlSN, constraintsSNValueJLabel);

		GridBagConstraints constraintsButPanel = new GridBagConstraints();
		constraintsButPanel.gridx = 1;
		constraintsButPanel.gridy = 4;
		constraintsButPanel.gridwidth = 2;
		constraintsButPanel.weightx = 1.0;
		constraintsButPanel.weighty = 1.0;
		constraintsButPanel.insets = new Insets(4, 4, 8, 4);
		contentPane.add(buttonPanel, constraintsButPanel);

		setContentPane(contentPane);
		pack();
		
		//Prevent spPNSN from changing size
		Dimension d = this.spPNSN.getPreferredSize();
		this.spPNSN.setPreferredSize(d);
	}

	/** Adds the listeners to this dialog's <code>Component</code>s. */
	private void addMyListeners()
	{
		this.pbRemove.addActionListener(this);
		this.pbCheck.addActionListener(this);
		this.pbCancel.addActionListener(this);

		this.tfInput.addKeyListener(this);
	}

	/** Clears parts with blank part numbers from the list. */ 
	private void clearData()
	{
		String PnSn_data;
		int index = 0;
		while (index < this.fieldPNSNListModel.size())
		{
			PnSn_data = (String) this.fieldPNSNListModel.getElementAt(index);
			if (PnSn_data.substring(0, 12).equals("            ")) //$NON-NLS-1$
			{
				this.fieldPNSNListModel.remove(index);
			}
			else
			{
				PnSn_data = PnSn_data.substring(0, 28);
				this.fieldPNSNListModel.setElementAt(PnSn_data, index);
			}
			index++;
		}
	}

	/** Called by {@link #recvInput()} to add the part to the list. */
	private void processAddButton()
	{
		try
		{
			String BLANK12 = "            "; //$NON-NLS-1$
			int rc = 0;
			if (!this.vlSN.getText().equals("") && !this.vlPN.getText().equals(""))
			{
				int index = searchPNSNListModel(this.vlPN.getText().trim(), this.vlSN.getText().trim());

				if (index == -1) //if pn/sn already in list
				{
					MfsXMLDocument xml_data1 = new MfsXMLDocument("RTV_QD10"); //$NON-NLS-1$
					xml_data1.addOpenTag("DATA"); //$NON-NLS-1$ //$NON-NLS-1$
					xml_data1.addCompleteField("BACK", "QDQDPN"); //$NON-NLS-1$ //$NON-NLS-2$
					//Construct QDQDPN = XXXXXXXXXXXX
					xml_data1.addOpenTag("FLD"); //$NON-NLS-1$
					xml_data1.addCompleteField("OPRT", "="); //$NON-NLS-1$ //$NON-NLS-2$
					xml_data1.addCompleteField("NAME", "QDQDPN"); //$NON-NLS-1$ //$NON-NLS-2$
					xml_data1.addCompleteField("VALU", "'" + this.vlPN.getText() + "'");  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					xml_data1.addCloseTag("FLD"); //$NON-NLS-1$
					//Put in the AND string
					xml_data1.addOpenTag("FLD"); //$NON-NLS-1$
					xml_data1.addCompleteField("OPRT", "AND"); //$NON-NLS-1$ //$NON-NLS-2$
					xml_data1.addCloseTag("FLD"); //$NON-NLS-1$
					//Construct QDQDSQ = XXXXXXXXXXXX
					xml_data1.addOpenTag("FLD"); //$NON-NLS-1$
					xml_data1.addCompleteField("OPRT", "="); //$NON-NLS-1$ //$NON-NLS-2$
					xml_data1.addCompleteField("NAME", "QDQDSQ"); //$NON-NLS-1$ //$NON-NLS-2$
					xml_data1.addCompleteField("VALU", "'" + this.vlSN.getText() + "'"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					xml_data1.addCloseTag("FLD"); //$NON-NLS-1$
					xml_data1.addCloseTag("DATA"); //$NON-NLS-1$
					xml_data1.finalizeXML();

					MFSTransaction rtv_qd10 = new MFSXmlTransaction(xml_data1.toString());
					rtv_qd10.setActionMessage("Verifying PN/SN in QD10, Please Wait...");
					MFSComm.getInstance().execute(rtv_qd10, this);
					rc = rtv_qd10.getReturnCode();


					MfsXMLParser xmlParser = new MfsXMLParser(rtv_qd10.getOutput());

					//If no error with SQL statements and returned something */
					if (rc == 0 && xmlParser.fieldExists("NAME")) //$NON-NLS-1$
					{
						String PN_X = this.vlPN.getText() + BLANK12;
						String SN_X = this.vlSN.getText() + BLANK12;
						String PN_12 = PN_X.substring(0, 12);
						String SN_12 = SN_X.substring(0, 12);
						this.fieldPNSNListModel.addElement(PN_12 + "    " + SN_12);
					}
					else
					{
						String erms = "Part and serial number not in QD10";
						IGSMessageBox.showOkMB(this, null, erms, null);
					}
				}
				else
				{
					String erms = "Part and serial already in list.";
					IGSMessageBox.showOkMB(this, null, erms, null);
				}
			}
			else
			{
				String erms = "Part number and serial number are both required!";
				IGSMessageBox.showOkMB(this, null, erms, null);
			}

		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(this, null, null, e);
		}
		this.toFront();
		this.tfInput.setText(""); //$NON-NLS-1$
		this.tfInput.requestFocusInWindow();
	}

	/** Invoked when {@link #pbCheck} is selected. */
	private void processCheckBut()
	{
		try
		{
			boolean isFirst = false; //~1A
			int rc = 0;
			String PnSn_data;
			boolean done = false;
			int index = 0; //List Model index
			
			clearData();
			while (index < this.fieldPNSNListModel.size())
			{
				PnSn_data = (String) this.fieldPNSNListModel.getElementAt(index);
				this.lstPNSN.setSelectedIndex(index);
				if (!PnSn_data.substring(0, 12).equals("            ")) //$NON-NLS-1$
				{
					/* start the AddQD10Cmnt transaction thread */
					MfsXMLDocument xml_data1 = new MfsXMLDocument("RTV_STAT"); //$NON-NLS-1$
					xml_data1.addOpenTag("DATA"); //$NON-NLS-1$
					// PULL PN/SN from List Model and XMLize them
					xml_data1.addCompleteField("RSON", this.fieldOption); //$NON-NLS-1$
					xml_data1.addCompleteField("INPN", PnSn_data.substring(0, 12)); //$NON-NLS-1$
					xml_data1.addCompleteField("INSQ", PnSn_data.substring(16)); //$NON-NLS-1$
					xml_data1.addCloseTag("DATA"); //$NON-NLS-1$
					xml_data1.finalizeXML();

					MFSTransaction rtv_stat = new MFSXmlTransaction(xml_data1.toString());
					rtv_stat.setActionMessage("Retrieving part infomation, Please Wait...");
					MFSComm.getInstance().execute(rtv_stat, this);
					
					rc = rtv_stat.getReturnCode();

					MfsXMLParser xmlParser = new MfsXMLParser(rtv_stat.getOutput());

					if (rc == 0)
					{
						try
						{
							MfsXMLParser status = new MfsXMLParser(xmlParser.getFieldLast("STATUS")); //$NON-NLS-1$
							done = false;
							isFirst = false; //~1A
							while (!done)
							{
								try
								{
									MfsXMLParser data = new MfsXMLParser(status.getWholeNextField("DATA")); //$NON-NLS-1$
									if (!isFirst) //~1A
									{ //~1A
										PnSn_data += "   " + status.getFieldOnly("CCIN") + //~1C
												"   " + status.getFieldOnly("TLEV")
												+ "   " + status.getFieldOnly("QDLEV") + //~1A
												"   " + status.getFieldOnly("COMP") + //~1A
												"   " + data.getFieldOnly("FGID") + "   "
												+ data.getFieldOnly("FVAL");
										isFirst = true; //~1A
									} //~1A
									else
									//~1A
									{ //~1A
										PnSn_data += "                             " //~1A
												+ data.getFieldOnly("FGID") + //~1A
												"   " + data.getFieldOnly("FVAL"); //~1A
									} //~1A
								}
								catch (MISSING_XML_TAG_EXCEPTION mt1)
								{
									done = true;
								}
								this.fieldPNSNListModel.setElementAt(PnSn_data, index);
								PnSn_data += "\n                            ";
							}
						}
						catch (MISSING_XML_TAG_EXCEPTION mt2)
						{
							//System.err.println(mt.getMessage());
							//done = true;
						}
					}
					else
					{
						rc = 1;
						String erms = xmlParser.getField("ERMS"); //$NON-NLS-1$
						IGSMessageBox.showOkMB(this, null, erms, null);
						this.toFront();
						this.tfInput.requestFocusInWindow();
					}
				}
				index++;
			}
		}
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
			boolean PN_found = false;
			boolean SN_found = false;
			int rc = 0;

			MFSBCBarcodeDecoder barcode = MFSBCBarcodeDecoder.getInstance();
			barcode.setMyBarcodeInput(this.tfInput.getText().toUpperCase());
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
				bcIndVal.setCSNI("1");
			}

			barcode.setMyBCIndicatorValue(bcIndVal);

			if (barcode.decodeBarcodeFor(this) == false)
			{
				rc = 10;
				barcode.getBCMyPartObject().setErrorString(MFSBCBarcodeDecoder.DECODE_TIMED_OUT_ERMS);
			}
			else
			{
				rc = barcode.getBCMyPartObject().getRC();
			}

			if (rc != 0) //if Error
			{
				String erms = barcode.getBCMyPartObject().getErrorString();
				if(erms.trim().length() == 0)
				{
					erms = MFSBCBarcodeDecoder.INVALID_BARCODE_ERMS;
				}
				IGSMessageBox.showOkMB(this, null, erms, null);
			}
			
			if (!(barcode.getBCMyPartObject().getPN().equals("")))
			{
				this.vlPN.setText(barcode.getBCMyPartObject().getPN());
				PN_found = true;
			}
			if (!(barcode.getBCMyPartObject().getSN().equals("")))
			{
				this.vlSN.setText(barcode.getBCMyPartObject().getSN());
				SN_found = true;
			}
			/* Start ~1A */
			if (PN_found && SN_found) 
			{
				try
				{
					this.processAddButton();
				}
				catch (Throwable throwable)
				{ 
					IGSMessageBox.showOkMB(this, null, null, throwable);
				}
			}
			/* End ~1A */
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(this, null, null, e);
		}
		this.toFront();
		this.tfInput.setText(""); //$NON-NLS-1$
	}

	/** Removes the selected element from the list. */
	public void removeFromList()
	{
		this.fieldPNSNListModel.remove(this.lstPNSN.getSelectedIndex());
	}

	/**
	 * Searches the list for a part.
	 * @param inPN the part number
	 * @param inSN the sequence number
	 * @return the index of the part in the list
	 */
	private int searchPNSNListModel(String inPN, String inSN)
	{
		String PnSn_data;
		int index = 0;
		while (index < this.fieldPNSNListModel.size())
		{
			PnSn_data = (String) this.fieldPNSNListModel.getElementAt(index);
			if (PnSn_data.substring(0, 12).trim().equals(inPN)
					&& PnSn_data.substring(16, 28).trim().equals(inSN))
			{
				return index;
			}
			index++;
		}
		return -1;
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
			else if (source == this.pbRemove)
			{
				removeFromList();
			}
			else if (source == this.pbCheck)
			{
				processCheckBut();
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
		if ((ke.getKeyCode() == KeyEvent.VK_ENTER) && (ke.getSource() == this.tfInput))
		{
			recvInput();
		}
	}

	/**
	 * Invoked the first time a window is made visible. 
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
