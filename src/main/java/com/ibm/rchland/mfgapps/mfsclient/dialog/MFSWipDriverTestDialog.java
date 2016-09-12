/* @ Copyright IBM Corporation 2002, 2010. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2007-02-22      34242JR  R Prechel        -Java 5 version
 * 2007-04-02   ~1 38166JM  R Prechel        -Add setLocationRelativeTo for dialog
 * 2010-03-11	~2 47595MZ  Ray Perry        -Shenzhen efficiencies
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.dialog;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;

import mfsxml.MfsXMLDocument;
import mfsxml.MfsXMLParser;

import com.ibm.rchland.mfgapps.client.utils.messagebox.IGSMessageBox;
import com.ibm.rchland.mfgapps.mfsclient.MFSActionableDialog;
import com.ibm.rchland.mfgapps.mfsclient.MFSFrame;
import com.ibm.rchland.mfgapps.mfsclient.barcode.MFSBCBarcodeDecoder;
import com.ibm.rchland.mfgapps.mfsclient.barcode.MFSBCIndicatorValue;
import com.ibm.rchland.mfgapps.mfsclient.barcode.MFSBCPartObject;
import com.ibm.rchland.mfgapps.mfsclient.panel.MFSSelectWorkPanel;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSConstants;
import com.ibm.rchland.mfgapps.mfscommon.MFSConfig;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSComm;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSFixedTransaction;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSTransaction;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSXmlTransaction;

/**
 * <code>MFSWipDriverTestDialog</code> is the <code>MFSActionableDialog</code>
 * used to work with the work units for a WIP Driver.
 * @author The MFS Client Development Team
 */
public class MFSWipDriverTestDialog
	extends MFSActionableDialog
{
	private static final long serialVersionUID = 1L;
	/** The <code>JList</code> that displays the work units. */
	protected JList lstWorkUnit = new JList();

	/** The <code>JScrollPane</code> that contains the list of work units. */
	protected JScrollPane spWorkUnit = new JScrollPane(this.lstWorkUnit,
			ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
			ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

	/** The input <code>JTextField</code>. */
	private JTextField tfInput = createTextField(MFSConstants.LARGE_TF_COLS, 0);

	/** The Part Number name <code>JLabel</code>. */
	private JLabel nlPN = createSmallNameLabel(""); //$NON-NLS-1$

	/** The Part Number value <code>JLabel</code>. */
	private JLabel vlPN = createSmallValueLabel(this.nlPN);

	/** The Sequence Number name <code>JLabel</code>. */
	private JLabel nlSN = createSmallNameLabel(""); //$NON-NLS-1$

	/** The Sequence Number value <code>JLabel</code>. */
	private JLabel vlSN = createSmallValueLabel(this.nlSN);

	/** The ASSOCIATE <code>JButton</code>. */
	private JButton pbAssociate = createButton("ASSOCIATE", 'A');

	/** The DISASSOCIATE <code>JButton</code>. */
	private JButton pbDisassociate = createButton("DISASSOCIATE", 'D');

	/** The REMOVE <code>JButton</code>. */
	private JButton pbRemove = createButton("REMOVE", 'R');

	/** The CANCEL <code>JButton</code>. */
	private JButton pbCancel = createButton("CANCEL", 'N');

	/** The <code>MFSSelectWorkPanel</code> using this dialog. */
	private MFSSelectWorkPanel fieldSelectWork;

	/** The WIP Driver. */
	private String fieldWipDriver;

	/**
	 * Constructs a new <code>MFSWipDriverTestDialog</code>.
	 * @param parent the <code>MFSFrame</code> that caused this
	 *        <code>MFSWipDriverTestDialog</code> to be displayed
	 * @param selectWork the <code>MFSSelectWorkPanel</code> using this dialog
	 * @param wipDriver the WIP Driver
	 */
	public MFSWipDriverTestDialog(MFSFrame parent, MFSSelectWorkPanel selectWork,
									String wipDriver)
	{
		super(parent, "Work Unit Selection");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.fieldSelectWork = selectWork;
		this.fieldWipDriver = wipDriver;
		createLayout();
		addMyListeners();
	}

	/** Adds this dialog's <code>Component</code>s to the layout. */
	private void createLayout()
	{
		JLabel headerLabel = createLabel("Work Units for " + this.fieldWipDriver);

		JPanel pnSnPanel = new JPanel(new GridLayout(2, 2, 10, 20));
		pnSnPanel.add(this.nlPN);
		pnSnPanel.add(this.vlPN);
		pnSnPanel.add(this.nlSN);
		pnSnPanel.add(this.vlSN);

		JPanel buttonPanel = new JPanel(new GridLayout(4, 1, 0, 10));
		buttonPanel.add(this.pbAssociate);
		buttonPanel.add(this.pbDisassociate);
		buttonPanel.add(this.pbRemove);
		buttonPanel.add(this.pbCancel);

		this.lstWorkUnit.setFont(new Font("Dialog", Font.PLAIN, 12)); //$NON-NLS-1$
		this.lstWorkUnit.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.lstWorkUnit.setVisibleRowCount(10);
		this.lstWorkUnit.setPrototypeCellValue("WWWWWWWWW"); //$NON-NLS-1$

		JPanel contentPane = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, 
				new Insets(20, 4, 20, 4), 0, 0);

		contentPane.add(headerLabel, gbc);

		gbc.gridy++;
		gbc.weightx = 1;
		gbc.insets = new Insets(0, 4, 15, 4);
		contentPane.add(this.tfInput, gbc);

		gbc.gridy++;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		contentPane.add(pnSnPanel, gbc);

		gbc.gridy++;
		gbc.gridwidth = 1;
		gbc.weighty = 1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(0, 40, 20, 10);
		contentPane.add(this.spWorkUnit, gbc);

		gbc.gridx = 1;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(0, 10, 20, 40);
		contentPane.add(buttonPanel, gbc);

		setContentPane(contentPane);
		setLabelDimensions(this.vlPN, MFSConstants.MAX_PN_CHARACTERS);
		setLabelDimensions(this.vlSN, MFSConstants.MAX_SN_CHARACTERS);
		this.nlSN.setText("Sequence Number");
		pack();
		this.nlSN.setText(""); //$NON-NLS-1$
	}

	/** Adds the listeners to this dialog's <code>Component</code>s. */
	private void addMyListeners()
	{
		this.pbAssociate.addActionListener(this);
		this.pbDisassociate.addActionListener(this);
		this.pbRemove.addActionListener(this);
		this.pbCancel.addActionListener(this);

		this.tfInput.addKeyListener(this);
		this.pbAssociate.addKeyListener(this);
		this.pbDisassociate.addKeyListener(this);
		this.pbRemove.addKeyListener(this);
		this.pbCancel.addKeyListener(this);
	}

	/** Invoked whean {@link #pbAssociate}is selected. */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void associateWorkUnits()
	{
		// Add logic to only pass those work units
		// that are not already associated @1
		try
		{
			int index = 0;
			int rc = 0;
			boolean foundNewWU = false; /* @1 */

			final DefaultListModel wuLM = (DefaultListModel) this.lstWorkUnit.getModel();
			if (wuLM.size() == 0)
			{
				String erms = "Add Some Work Units to the List First";
				IGSMessageBox.showOkMB(this, null, erms, null);
			}
			else
			{
				Vector wuVector = new Vector();

				MfsXMLDocument xml_data = new MfsXMLDocument("ASSOC_WIPD"); //$NON-NLS-1$
				xml_data.addOpenTag("DATA"); //$NON-NLS-1$

				while (index < wuLM.size())
				{
					/*
					 * check the first char from the list to determine if its
					 * new or already associated @1
					 */
					if (((String) wuLM.getElementAt(index)).substring(0, 1).equals(" ")) //$NON-NLS-1$
					{
						foundNewWU = true;
						String mctl = ((String) wuLM.getElementAt(index)).substring(1);
						xml_data.addCompleteField("MCTL", mctl); //$NON-NLS-1$
						wuVector.addElement(mctl);
					}
					index++;
				}
				xml_data.addCompleteField("WIPD", //$NON-NLS-1$ 
						this.fieldWipDriver.concat("            ").substring(0, 12)); //$NON-NLS-1$
				xml_data.addCompleteField("ACTN", "U"); //$NON-NLS-1$ //$NON-NLS-2$
				xml_data.addCloseTag("DATA"); //$NON-NLS-1$
				xml_data.finalizeXML();

				/* @1 */
				if (!foundNewWU)
				{
					String erms = "All Work Units Associated to this Wip Driver Already";
					IGSMessageBox.showOkMB(this, null, erms, null);
				}
				else
				{
					MFSTransaction assoc_wipd = new MFSXmlTransaction(xml_data.toString());
					assoc_wipd.setActionMessage("Associating List of Work Units, Please Wait...");
					MFSComm.getInstance().execute(assoc_wipd, this);
					rc = assoc_wipd.getReturnCode();

					if (rc != 0)
					{
						IGSMessageBox.showOkMB(this, null, assoc_wipd.getErms(), null);
					}
					else
					{
						MfsXMLParser xmlParser = new MfsXMLParser(assoc_wipd.getOutput());
						index = 0;
						while (index < wuLM.size())
						{
							//add the '*' to denote work unit has been associated
							String tmpMctl = "*" + ((String) wuLM.getElementAt(index)).substring(1);
							wuLM.setElementAt(tmpMctl, index);
							index++;
						}
						String prln = xmlParser.getField("PRLN"); //$NON-NLS-1$
						String prod = xmlParser.getField("PROD"); //$NON-NLS-1$
						String nmbr = xmlParser.getField("NMBR"); //$NON-NLS-1$

						/* start the RTV_OEMO transaction thread */
						String data = "RTV_OEMO  " + prln + nmbr + prod; //$NON-NLS-1$
						MFSTransaction rtv_oemo = new MFSFixedTransaction(data);
						rtv_oemo.setActionMessage("Retrieving List of Operations, Please Wait...");
						MFSComm.getInstance().execute(rtv_oemo, this);
						rc = rtv_oemo.getReturnCode();

						if (rc != 0)
						{
							IGSMessageBox.showOkMB(this, null, rtv_oemo.getErms(), null);
						}
						else if (rtv_oemo.getOutput().length() == 0)
						{
							StringBuffer erms = new StringBuffer();
							erms.append("No Ending Modes found for PRLN=");
							erms.append(prln);
							erms.append(" PROD=");
							erms.append(prod);
							erms.append(" NMBR=");
							erms.append(nmbr);

							IGSMessageBox.showOkMB(this, null, erms.toString(), null);
						}
						else
						{
							String ops = ""; //$NON-NLS-1$
							int start = 0;
							int end = 4;
							final int len = rtv_oemo.getOutput().length();

							while (end < len)
							{
								ops += rtv_oemo.getOutput().substring(start, end);
								start += 64;
								end += 64;
							}

							boolean done = false;
							MFSOperationsDialog operJD = new MFSOperationsDialog(getParentFrame());
							operJD.loadOperListModel(ops, "0000"); //$NON-NLS-1$

							while (!done)
							{
								operJD.setSelectedIndex(0);
								operJD.setLocationRelativeTo(getParentFrame()); //~1A 
								operJD.setVisible(true);
								if (operJD.getProceedSelected())
								{
									done = true;
								}
							}
							if (done)
							{
								int idx = 0;
								while (idx < wuVector.size())
								{
									StringBuffer data3 = new StringBuffer();
									data3.append("REMAP     "); //$NON-NLS-1$
									data3.append(wuVector.elementAt(idx));
									data3.append("U"); //$NON-NLS-1$
									data3.append(operJD.getSelectedListOption());
									data3.append(MFSConfig.getInstance().get8CharCellType());

									StringBuffer amsg = new StringBuffer();
									amsg.append("Remapping ");
									amsg.append(wuVector.elementAt(idx));
									amsg.append(", Please Wait...");

									MFSTransaction remap = new MFSFixedTransaction(data3.toString());
									remap.setActionMessage(amsg.toString());
									MFSComm.getInstance().execute(remap, this);
									rc = remap.getReturnCode();

									if (rc != 0 && rc != 30)
									{
										IGSMessageBox.showOkMB(this, null, remap.getErms(), null);
									}
									idx++;
								}//end of while loop

								// ~42 do not call VRFY_SYS_C
								if (!MFSConfig.getInstance().containsConfigEntry("EFFICIENCYON*DUMBCLIENT")) //$NON-NLS-1$
								{
									MfsXMLDocument xml_data2 = new MfsXMLDocument("VRFY_SYS_C"); //$NON-NLS-1$
									xml_data2.addOpenTag("DATA"); //$NON-NLS-1$
									xml_data2.addCompleteField("MCTL", //$NON-NLS-1$ 
											(String) wuVector.elementAt(0)); //$NON-NLS-1$
									xml_data2.addCompleteField("WHOM", "C"); //$NON-NLS-1$ //$NON-NLS-2$
									xml_data2.addCloseTag("DATA"); //$NON-NLS-1$
									xml_data2.finalizeXML();
	
									MFSTransaction vrfy_sys_c = new MFSXmlTransaction(xml_data2.toString());
									vrfy_sys_c.setActionMessage("Verifying System Parts, Please Wait...");
									MFSComm.getInstance().execute(vrfy_sys_c, this);
									rc = vrfy_sys_c.getReturnCode();
	
									if (rc == 100 || rc == 200)
									{
										this.dispose();
	
										/* String curDisplayRow = ""; */
										String curMctl = "";
	
										MfsXMLParser xmlParser2 = new MfsXMLParser(vrfy_sys_c.getOutput());
	
										String curRcd = xmlParser2.getField("RCD"); //$NON-NLS-1$
	
										while (!curRcd.equals("")) //$NON-NLS-1$
										{
											MfsXMLParser xmlSubParser = new MfsXMLParser(curRcd);
	
											if (curMctl.equals(xmlSubParser.getField("MCTL"))) //$NON-NLS-1$
											{
												//curDisplayRow = "Same MCTL";
											}
											else
											{
												curMctl = xmlSubParser.getField("MCTL"); //$NON-NLS-1$
												//curDisplayRow = "New MCTL: " + curMctl;
											}
	
											curRcd = xmlParser2.getNextField("RCD");
										}
	
										this.fieldSelectWork.setPrevWipTestDriver(this.fieldWipDriver);
										this.fieldSelectWork.showPNSNErrorList(vrfy_sys_c.getOutput(), 0);
										//set rc to 0, if 100, only warning
										if (rc == 100)
										{
											rc = 0;
										}
									}
								}
							}
						}
					}//end of good assoc_wipd call
				}//end of found New Work Unit Check
			}//end of list has elements checks
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(this, null, null, e);
		}

		this.toFront();
		this.tfInput.setText(""); //$NON-NLS-1$
	}

	/** Invoked whean {@link #pbDisassociate} is selected. */
	public void disassociateWorkUnit()
	{
		try
		{
			int rc = 0;

			final DefaultListModel wuLM = (DefaultListModel) this.lstWorkUnit.getModel();
			if (wuLM.size() == 0 || this.lstWorkUnit.getSelectedIndex() == -1)
			{
				String erms = "No Work Unit selected!";
				IGSMessageBox.showOkMB(this, null, erms, null);
			}
			else
			{
				MfsXMLDocument xml_data = new MfsXMLDocument("DASSC_WIPD"); //$NON-NLS-1$
				xml_data.addOpenTag("DATA"); //$NON-NLS-1$
				String mctl = (String) wuLM.getElementAt(this.lstWorkUnit.getSelectedIndex()); //$NON-NLS-1$
				xml_data.addCompleteField("MCTL", mctl.substring(1)); //$NON-NLS-1$
				xml_data.addCloseTag("DATA"); //$NON-NLS-1$
				xml_data.finalizeXML();

				StringBuffer amsg = new StringBuffer();
				amsg.append("Disassociating ");
				amsg.append(mctl);
				amsg.append(" from Wip Driver ");
				amsg.append(this.fieldWipDriver);
				amsg.append(", Please Wait...");

				MFSXmlTransaction dassc_wipd = new MFSXmlTransaction(xml_data.toString());
				dassc_wipd.setActionMessage(amsg.toString());
				MFSComm.getInstance().execute(dassc_wipd, this);
				rc = dassc_wipd.getReturnCode();

				if (rc != 0)
				{
					IGSMessageBox.showOkMB(this, null, dassc_wipd.getErms(), null);
				}
				else
				{
					wuLM.remove(this.lstWorkUnit.getSelectedIndex());
					if (wuLM.size() > 0)
					{
						this.lstWorkUnit.setSelectedIndex(0);
					}
				}
			}//entries in the list
		}// end of try block
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(this, null, null, e);
		}
		this.toFront();
		this.tfInput.setText(""); //$NON-NLS-1$
	}

	/**
	 * Calls RTV_WIPDWU to retrieve the work units for the WIP Driver. The
	 * output of the transaction is used to populate the list of work units.
	 */
	public void loadCurrentWorkUnits()
	{
		try
		{
			DefaultListModel wuLM = new DefaultListModel();
			this.lstWorkUnit.setModel(wuLM);

			MfsXMLDocument xml_data = new MfsXMLDocument("RTV_WIPDWU"); //$NON-NLS-1$
			xml_data.addOpenTag("DATA"); //$NON-NLS-1$
			xml_data.addCompleteField("WIPD", //$NON-NLS-1$
					this.fieldWipDriver.concat("            ").substring(0, 12)); //$NON-NLS-1$
			xml_data.addCloseTag("DATA"); //$NON-NLS-1$
			xml_data.finalizeXML();

			MFSTransaction rtv_wipdwu = new MFSXmlTransaction(xml_data.toString());
			rtv_wipdwu.setActionMessage("Retrieving List of Work Units, Please Wait...");
			MFSComm.getInstance().execute(rtv_wipdwu, this.fieldSelectWork);

			if (rtv_wipdwu.getReturnCode() != 0)
			{
				IGSMessageBox.showOkMB(getParentFrame(), null, rtv_wipdwu.getErms(), null);
			}
			else
			{
				MfsXMLParser xmlParser = new MfsXMLParser(rtv_wipdwu.getOutput());
				String tempMctl = ""; //$NON-NLS-1$
				try
				{
					tempMctl = xmlParser.getField("MCTL"); //$NON-NLS-1$
				}
				catch (mfsxml.MISSING_XML_TAG_EXCEPTION e)
				{
					// make the following loop skip right away
					tempMctl = ""; //$NON-NLS-1$
				}
				while (!tempMctl.equals("")) //$NON-NLS-1$
				{
					wuLM.addElement("*" + tempMctl); //$NON-NLS-1$
					tempMctl = xmlParser.getNextField("MCTL"); //$NON-NLS-1$
				}
			}
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}

		this.toFront();
		this.tfInput.setText(""); //$NON-NLS-1$
	}

	/** Processes the input the user entered in the <code>JTextField</code>. */
	public void recvInput()
	{
		try
		{
			boolean found = false;
			String errorString = MFSBCBarcodeDecoder.INVALID_BARCODE_ERMS;
			int rc = 0;

			MFSBCBarcodeDecoder barcode = MFSBCBarcodeDecoder.getInstance();
			barcode.setMyBarcodeInput(this.tfInput.getText().toUpperCase());
			barcode.setMyBCPartObject(new MFSBCPartObject());

			/* setup barcode indicator values */
			MFSBCIndicatorValue bcIndVal = new MFSBCIndicatorValue();
			bcIndVal.setPNRI("1"); //$NON-NLS-1$

			String sni = MFSConfig.getInstance().getConfigValue("DECODESN"); //$NON-NLS-1$
			if (!sni.equals(MFSConfig.NOT_FOUND))
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
					DefaultListModel lm = (DefaultListModel) this.lstWorkUnit.getModel();
					String wu = barcode.getBCMyPartObject().getWU();
					if (lm.indexOf(" " + wu) == -1 && lm.indexOf("*" + wu) == -1) //$NON-NLS-1$ //$NON-NLS-2$
					{
						lm.addElement(" " + wu); //$NON-NLS-1$
						found = true;
					}
					else
					{
						errorString = "Work Unit " + wu + " already in list";
					}
					this.nlPN.setText(""); //$NON-NLS-1$
					this.vlPN.setText(""); //$NON-NLS-1$
					this.nlSN.setText(""); //$NON-NLS-1$
					this.vlSN.setText(""); //$NON-NLS-1$

				}
				if (!(barcode.getBCMyPartObject().getPN().equals(""))) //$NON-NLS-1$
				{
					if (this.nlPN.getText().equals("")) //$NON-NLS-1$
					{
						this.nlPN.setText("Part Number");
					}
					this.vlPN.setText(barcode.getBCMyPartObject().getPN());
					found = true;
				}
				if (!(barcode.getBCMyPartObject().getSN().equals(""))) //$NON-NLS-1$
				{
					if (this.nlSN.getText().equals("")) //$NON-NLS-1$
					{
						this.nlSN.setText("Sequence Number");
					}
					this.vlSN.setText(barcode.getBCMyPartObject().getSN());
					found = true;
				}
				if (!this.vlSN.getText().equals("") && !this.vlPN.getText().equals("")) //$NON-NLS-1$ //$NON-NLS-2$
				{
					MfsXMLDocument xml_data1 = new MfsXMLDocument("FIND_WU"); //$NON-NLS-1$
					xml_data1.addOpenTag("DATA"); //$NON-NLS-1$
					xml_data1.addCompleteField("INPN", this.vlPN.getText()); //$NON-NLS-1$
					xml_data1.addCompleteField("INSQ", this.vlSN.getText()); //$NON-NLS-1$
					xml_data1.addCloseTag("DATA"); //$NON-NLS-1$
					xml_data1.finalizeXML();

					MFSTransaction find_wu = new MFSXmlTransaction(xml_data1.toString());
					find_wu.setActionMessage("Retrieving Work Unit, Please Wait...");
					MFSComm.getInstance().execute(find_wu, this);
					rc = find_wu.getReturnCode();

					if (rc == 0)
					{
						MfsXMLParser xmlParser = new MfsXMLParser(find_wu.getOutput());
						DefaultListModel lm = (DefaultListModel) this.lstWorkUnit.getModel();
						final String mctl = xmlParser.getField("MCTL"); //$NON-NLS-1$
						if (lm.indexOf(" " + mctl) == -1 && lm.indexOf("*" + mctl) == -1) //$NON-NLS-1$ //$NON-NLS-2$
						{
							lm.addElement(" " + mctl); //$NON-NLS-1$
							found = true;
						}
						else
						{
							rc = 10;
							errorString = "Work Unit " + mctl + " already in list";
						}
						this.nlPN.setText(""); //$NON-NLS-1$
						this.vlPN.setText(""); //$NON-NLS-1$
						this.nlSN.setText(""); //$NON-NLS-1$
						this.vlSN.setText(""); //$NON-NLS-1$
					}
					else
					{
						errorString = find_wu.getErms();
					}
				}
			}
			if ((rc != 0) || (!found && !this.tfInput.getText().equals(""))) //$NON-NLS-1$
			{
				String erms = barcode.getBCMyPartObject().getErrorString();
				if (erms.equals("")) //$NON-NLS-1$
				{
					erms = errorString;
				}
				IGSMessageBox.showOkMB(this, null, erms, null);
			}
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(this, null, null, e);
		}

		this.toFront();
		this.tfInput.setText("");
	}

	/** Invoked when {@link #pbRemove} is selected. */
	public void removeFromList()
	{
		final DefaultListModel wuLM = (DefaultListModel) this.lstWorkUnit.getModel();
		if (wuLM.size() == 0)
		{
			String erms = "No Work Units to Disassociate!";
			IGSMessageBox.showOkMB(this, null, erms, null);
		}
		else
		{
			if (this.lstWorkUnit.getSelectedIndex() == -1)
			{
				String erms = "No Work Units Selected!";
				IGSMessageBox.showOkMB(this, null, erms, null);
			}
			else
			{
				String mctl = (String) wuLM.getElementAt(this.lstWorkUnit.getSelectedIndex());
				if (mctl.substring(0, 1).equals("*")) //$NON-NLS-1$
				{
					String erms = "Cannont Remove Work Unit That is Already Associated.  Must Disassociate!";
					IGSMessageBox.showOkMB(this, null, erms, null);
				}
				else
				{
					wuLM.remove(this.lstWorkUnit.getSelectedIndex());
				}
			}//end of work unit selected
		}//end of list size
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
				dispose();
			}
			else if (source == this.pbAssociate)
			{
				associateWorkUnits();
			}
			else if (source == this.pbDisassociate)
			{
				disassociateWorkUnit();
			}
			else if (source == this.pbRemove)
			{
				removeFromList();
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
		if (ke.getKeyCode() == KeyEvent.VK_ENTER && ke.getSource() == this.tfInput)
		{
			recvInput();
		}
	}

	/**
	 * Invoked the first time a window is made visible.
	 * <p>
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
