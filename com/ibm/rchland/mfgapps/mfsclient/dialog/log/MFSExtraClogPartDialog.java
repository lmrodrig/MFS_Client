/* © Copyright IBM Corporation 2006, 2010. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2004-05-21   ~1 27422PT                   UPDATE - get the CDES from the EXTRA_CRCL call
 * 2004-06-04   ~2                           UPDATE - set a few extra compRec fields - causing problems later on in rework
 * 2004-12-02   ~3 29712RB  Tou Lee Moua     UPDATE - added CTLV as returned field from EXTRA_CRCL
 * 2005-02-21   ~4 30431PT  Dave Fichtinger  UPDATE - added collection of MAC data for CLOG part
 * 2006-06-29   ~5 31801JM  R Prechel        -Prompt to collect MACID when missing on parts inside the assembly
 *                                           -Changed creation of MACIDJDialog
 *                                           -Removed unused variables, methods, and imports
 *                                           -Change updateMAC to return both the rc and errorString
 *                                           -Changed parent class to MFSLogPartDialog;
 *                                            inherited methods previously defined locally
 * 2006-08-29  ~06 34222BP  VH Avila         -Delete the updtViewInstPartsString() calls because
 *                                            the variable was deleted from Component_Rec class
 * 2007-02-27   ~7 34242JR  R Prechel        -Java 5 version
 *                                           -Deleted fieldComment, edit_loc, edit_comment
 * 2007-04-12   ~8 38332AP  R Prechel        -Fix initDisplay to correct PN label text index
 * 2007-08-27   ~9 38007SE  R Prechel        -MAC Address changes
 * 2007-09-06  ~10 39786JM  R Prechel        -MFSMacAddressUpdater.createDialog changes
 * 2007-10-18  ~11 40209RB  R Prechel        -Process correct error message.
 * 2008-01-14      39782JM	Martha Luna		 -Changes the name of updtMultilineDisplayString by updtIRDisplayString
 * 2008-09-29  ~12 41356MZ  Santiago SC      -New PNRI value to be used : 'N'
 * 2009-11-04  ~13 43835SM  Sayde A			 -Add RTV_WUBYPS transation to get WU data
 * 2010-03-20  ~14 47595MZ  Ray Perry		 -Shenzhen efficiency: remember last coo
 * 2010-11-02  ~15 49513JM  Toribio H.       -Improve Coo remembering
 ****************************************************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.dialog.log;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import mfsxml.MfsXMLDocument;
import mfsxml.MfsXMLParser;

import com.ibm.rchland.mfgapps.client.utils.exception.IGSTransactionException;
import com.ibm.rchland.mfgapps.client.utils.messagebox.IGSMessageBox;
import com.ibm.rchland.mfgapps.client.utils.xml.IGSXMLTransaction;
import com.ibm.rchland.mfgapps.mfsclient.MFSAbstractLogPartDialog;
import com.ibm.rchland.mfgapps.mfsclient.barcode.MFSBCBarcodeDecoder;
import com.ibm.rchland.mfgapps.mfsclient.barcode.MFSBCPartObject;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSGenericListDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSInputDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSMacIDDialogBase;
import com.ibm.rchland.mfgapps.mfsclient.panel.MFSDirectWorkPanel;
import com.ibm.rchland.mfgapps.mfsclient.struct.MFSComponentListModel;
import com.ibm.rchland.mfgapps.mfsclient.struct.MFSComponentRec;
import com.ibm.rchland.mfgapps.mfsclient.struct.MFSHeaderRec;
import com.ibm.rchland.mfgapps.mfsclient.struct.MFSIntStringPair;
import com.ibm.rchland.mfgapps.mfsclient.struct.MFSPartInformation;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSConstants;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSMacAddressUpdater;
import com.ibm.rchland.mfgapps.mfscommon.MFSConfig;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSComm;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSFixedTransaction;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSTransaction;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSXmlTransaction;
import com.ibm.rchland.mfgapps.mfscommon.comm.trx.RTV_SIGPN;
import com.ibm.rchland.mfgapps.mfscommon.comm.trx.RTV_WUBYPS;
import com.ibm.rchland.mfgapps.mfscommon.comm.trx.VRFY_PI;

/**
 * <code>MFSExtraClogPartDialog</code> is the
 * <code>MFSAbstractLogPartDialog</code> used by the
 * {@link MFSDirectWorkPanel#extra_clog_part()} method.
 * @author The MFS Client Development Team
 */
@SuppressWarnings("serial")
public class MFSExtraClogPartDialog
	extends MFSAbstractLogPartDialog
{
	/** The Log Part (F2) <code>JButton</code>. */
	private JButton pbLogPart = createButton("F2 = Log Part"); //$NON-NLS-1$

	/** The Cancel (Esc) <code>JButton</code>. */
	private JButton pbCancel = createButton("Esc = Cancel"); //$NON-NLS-1$

	/** The Get PN List <code>JButton</code>. */
	private JButton pbGetPNList = createButton("Get PN List"); //$NON-NLS-1$

	/** The LOC name <code>JLabel</code>. */
	private JLabel nlLoc = createNameLabel("LOC:"); //$NON-NLS-1$

	/** The LOC value <code>JLabel</code>. */
	private JLabel vlLoc = createValueLabel(this.nlLoc);

	/** The <code>MFSDirectWorkPanel</code> that uses this dialog. */
	private MFSDirectWorkPanel fieldDirWork; //set by constructor

	/** Set <code>true</code> if the Canel button is pressed. */
	private boolean fieldPressedCancel = false;

	/** Flag used by {@link #logPart()} to indicate if a component was added. */
	private int fieldComponentAdded = 0;

	/** The <code>MFSComponentListModel</code> for the work unit. */
	private MFSComponentListModel fieldCompListModel; //set by reinitialize

	/** The <code>MFSComponentRec</code> for the current component. */
	private MFSComponentRec fieldComp; //set by reinitialize

	/** The location. */
	private String fieldLoc; //set by reinitialize

	/** The scanned in Sequence Number used by {@link #choosePn()}. */
	private String fieldCurrSN = EMPTY_STRING;

	/** Stores the Part Number of the <code>MFSComponentRec</code>. */
	private String fieldRememberPN = EMPTY_STRING;

	/** The scanned in Part Number used by {@link #initDisplay()}. */
	private String fieldRecallPN = EMPTY_STRING;

	/** The scanned in Sequence Number used by {@link #initDisplay()}. */
	private String fieldRecallSN = EMPTY_STRING;

	/** The scanned in EC Number used by {@link #initDisplay()}. */
	private String fieldRecallEC = EMPTY_STRING;

	/** The scanned in Card Assembly used by {@link #initDisplay()}. */
	private String fieldRecallCA = EMPTY_STRING;

	/** The scanned in Machine Serial used by {@link #initDisplay()}. */
	private String fieldRecallMSN = EMPTY_STRING;

	/** The scanned in Control Number used by {@link #initDisplay()}. */
	private String fieldRecallWU = EMPTY_STRING;

	/** The scanned in Country used by {@link #initDisplay()}. */
	private String fieldRecallCO = EMPTY_STRING;

	/**
	 * Constructs a new <code>MFSExtraClogPartDialog</code>.
	 * @param dirWork the <code>MFSDirectWorkPanel</code> that uses this dialog
	 */
	public MFSExtraClogPartDialog(MFSDirectWorkPanel dirWork)
	{
		super(dirWork.getParentFrame(), "Extra Part"); //$NON-NLS-1$
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.fieldDirWork = dirWork;
		createLayout();
		addMyListeners();
	}

	/** Adds this dialog's <code>Component</code>s to the layout. */
	private void createLayout()
	{
		this.pbGetPNList.setEnabled(false);
		setLabelDimensions(this.vlLoc, 20);

		this.pnlButtons.add(this.pbLogPart);
		this.pnlButtons.add(this.pbCancel);

		createLayout(true);
	}

	/** Adds the listeners to this dialog's <code>Component</code>s. */
	private void addMyListeners()
	{
		this.pbLogPart.addActionListener(this);
		this.pbCancel.addActionListener(this);
		this.pbGetPNList.addActionListener(this);

		this.pbLogPart.addKeyListener(this);
		this.pbCancel.addKeyListener(this);
		
		this.tfInput.addKeyListener(this);
	}

	/**
	 * Hook method called when laying out this dialog's components; adds extra components.
	 * @param contentPane the <code>JPanel</code> that will become this
	 *        dialog's content pane
	 * @param gbc the <code>GridBagConstraints</code> used to layout the
	 *        content pane
	 */
	protected void addExtraComponents(JPanel contentPane, GridBagConstraints gbc)
	{ 
		//~5A new method to handle extra components on an MFSExtraClogPartDialog
		gbc.gridx = 0;
		gbc.gridy++;
		gbc.gridwidth = 2;
		gbc.gridheight = 1;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.insets = new Insets(4, 4, 4, 4);
		contentPane.add(this.pbGetPNList, gbc);

		gbc.gridy++;
		gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.EAST;
		gbc.insets = new Insets(5, 15, 5, 5);
		contentPane.add(this.nlLoc, gbc);

		gbc.gridx = 1;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(4, 4, 4, 4);
		contentPane.add(this.vlLoc, gbc);
	}

	/** Allows the user to select a part number based on the scanned in serial number. */
	private void choosePn()
	{
		int rc = 0;
		try
		{
			MfsXMLDocument xml_data1 = new MfsXMLDocument("RTV_QD10"); //$NON-NLS-1$
			xml_data1.addOpenTag("DATA"); //$NON-NLS-1$
			xml_data1.addCompleteField("BACK", "QDQDPN"); //$NON-NLS-1$ //$NON-NLS-2$
			xml_data1.addOpenTag("FLD"); //$NON-NLS-1$
			xml_data1.addCompleteField("OPRT", "="); //$NON-NLS-1$ //$NON-NLS-2$
			xml_data1.addCompleteField("NAME", "QDQDSQ"); //$NON-NLS-1$ //$NON-NLS-2$
			xml_data1.addCompleteField("VALU", "'" + this.fieldCurrSN + "'"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			xml_data1.addCloseTag("FLD"); //$NON-NLS-1$
			xml_data1.addCloseTag("DATA"); //$NON-NLS-1$
			xml_data1.finalizeXML();

			MFSTransaction rtv_qd10 = new MFSXmlTransaction(xml_data1.toString());
			rtv_qd10.setActionMessage("Retrieving List of Part Numbers, Please Wait..."); //$NON-NLS-1$
			MFSComm.getInstance().execute(rtv_qd10, this);
			rc = rtv_qd10.getReturnCode();

			if (rc != 0)
			{
				IGSMessageBox.showOkMB(this, null, rtv_qd10.getErms(), null);
			}
			else
			{
				MfsXMLParser xmlParser = new MfsXMLParser(rtv_qd10.getOutput());
				String tempPN = EMPTY_STRING;

				try
				{
					tempPN = xmlParser.getField("VALU"); //$NON-NLS-1$
				}
				catch (mfsxml.MISSING_XML_TAG_EXCEPTION e)
				{
					rc = 10;
					String erms = "No Parts Found for this Serial Number ?"; //$NON-NLS-1$
					IGSMessageBox.showOkMB(this, null, erms, null);
				}
				
				if (rc == 0)
				{
					StringBuffer tempPNCollector = new StringBuffer(tempPN);

					tempPN = xmlParser.getNextField("VALU"); //$NON-NLS-1$

					while (!tempPN.equals(EMPTY_STRING))
					{
						tempPNCollector.append(tempPN);
						tempPN = xmlParser.getNextField("VALU"); //$NON-NLS-1$
					}
					MFSGenericListDialog myQuestionJD = new MFSGenericListDialog(
							getParentFrame(), "Select A Part Number from the List", //$NON-NLS-1$
							"Pick A Part Number"); //$NON-NLS-1$
					myQuestionJD.setSizeSmall();
					myQuestionJD.loadAnswerListModel(tempPNCollector.toString(), 12);

					JLabel partLabel = this.getValueLabel(PART_NUMBER, 2);
					if (partLabel != null && !partLabel.getText().equals(EMPTY_STRING))
					{
						myQuestionJD.setDefaultSelection(partLabel.getText());
					}
					myQuestionJD.setLocationRelativeTo(this);
					myQuestionJD.setVisible(true);

					if (myQuestionJD.getProceedSelected() && partLabel != null)
					{						
						partLabel.setText(myQuestionJD.getSelectedListOption());
					}
				}
			}
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(this, null, null, e);
		}
	}

	/**
	 * Executes the RTV_FLAGS transaction to perform a data collection check
	 * @param pn the part number
	 * @return the error string from RTV_FLAGS
	 */
	private String dataCollectionCheck(String pn)
	{
		int rc = 0;
		String errorString = EMPTY_STRING;
		final MFSConfig config = MFSConfig.getInstance();

		/* we don't always want to adhere to the collection flags */
		/* as defined in the PD10 table */
		String cnfgData = "IGNOREDATACOLLECT," + this.fieldHeaderRec.getNmbr(); //$NON-NLS-1$

		String value = config.getConfigValue(cnfgData);
		if (value.equals(MFSConfig.NOT_FOUND))
		{
			cnfgData = "IGNOREDATACOLLECT,*ALL"; //$NON-NLS-1$
			value = config.getConfigValue(cnfgData);
		}

		//user does not want to ignore the data collection flags, so let's go get them
		if (value.equals(MFSConfig.NOT_FOUND))
		{
			StringBuffer data = new StringBuffer(31);
			data.append("RTV_FLAGS "); //$NON-NLS-1$
			data.append(this.fieldHeaderRec.getMctl());
			data.append(pn);
			data.append("F"); //$NON-NLS-1$

			MFSTransaction rtv_flags = new MFSFixedTransaction(data.toString());
			rtv_flags.setActionMessage("Retrieving Part Information, Please Wait..."); //$NON-NLS-1$
			MFSComm.getInstance().execute(rtv_flags, this);
			rc = rtv_flags.getReturnCode();

			if (rc == 0)
			{
				// not configured to ignore the data collection flags from
				// RTV_FLAGS so use the values passed back from RTV_FLAGS
				this.fieldComp.setEcri(rtv_flags.getOutput().substring(2, 3));
				this.fieldComp.setCcai(rtv_flags.getOutput().substring(3, 4));
				//ignore Machine Serial collection flag for now
				this.fieldComp.setCmti("0"); //$NON-NLS-1$
				this.fieldComp.setPari(rtv_flags.getOutput().substring(5, 6));
			}
			else
			{
				errorString = rtv_flags.getOutput();
			}
			
		}
		//data collection flags to be ignored
		else
		{
			/* configured to ignore the data collection flags */
			this.fieldComp.setEcri("0"); //$NON-NLS-1$
			this.fieldComp.setCcai("0"); //$NON-NLS-1$
			//ignore Machine Serial collection flag for now
			this.fieldComp.setCmti("0"); //$NON-NLS-1$
			this.fieldComp.setPari("0"); //$NON-NLS-1$
		}
		return errorString;
	}

	/**
	 * Returns the <code>MFSComponentRec</code> for the current component.
	 * @return the <code>MFSComponentRec</code>
	 */
	public MFSComponentRec getComp()
	{
		return this.fieldComp;
	}

	/**
	 * Returns the value of the component added flag set by {@link #logPart()}.
	 * @return the value of the component added flag
	 */
	public int getComponentAdded()
	{
		return this.fieldComponentAdded;
	}

	/**
	 * Returns <code>true</code> iff the Cancel button was pressed.
	 * @return <code>true</code> iff the Cancel button was pressed
	 */
	public boolean getPressedCancel()
	{
		return this.fieldPressedCancel;
	}

	/**
	 * Returns the remembered Part Number.
	 * @return the remembered Part Number
	 */
	public String getRememberPN()
	{
		return this.fieldRememberPN;
	}

	/** Initializes the appearance of the dialog. */
	public void initDisplay()
	{
		//~7A Use a String[][] array
		String[][] labelText = new String[7][2];
		for (int s = 0; s < 7; s++)
		{
			labelText[s][0] = EMPTY_STRING;
			labelText[s][1] = EMPTY_STRING;
		}

		int i = 0;
		MFSComponentRec compRec = this.fieldComp;
		if (!compRec.isPnriDoNotCollect())
		{
			labelText[i][0] = PART_NUMBER;
			if (this.fieldRecallPN.equals(EMPTY_STRING))
			{
				if (this.fieldRememberPN.length() > 0)
				{
					labelText[i][1] = this.fieldRememberPN;
				}
			}
			else
			{
				//~8C Change index from [i][0] to [i][1]
				labelText[i][1] = this.fieldRecallPN;
			}
			i++;
		}
		else
		{
			this.fieldRecallPN = EMPTY_STRING;
		}

		if (!compRec.isEcriDoNotCollect())
		{
			labelText[i][0] = EC_NUMBER;
			labelText[i][1] = this.fieldRecallEC;
			i++;
		}
		else
		{
			this.fieldRecallEC = EMPTY_STRING;
		}

		if (!compRec.isCsniDoNotCollect())
		{
			labelText[i][0] = SEQUENCE_NUMBER;
			labelText[i][1] = this.fieldRecallSN;
			i++;
		}
		else
		{
			this.fieldRecallSN = EMPTY_STRING;
		}

		if (!compRec.isCcaiDoNotCollect())
		{
			labelText[i][0] = CARD_ASSEMBLY;
			labelText[i][1] = this.fieldRecallCA;
			i++;
		}
		else
		{
			this.fieldRecallCA = EMPTY_STRING;
		}

		if (!compRec.isCmtiDoNotCollect())
		{
			labelText[i][0] = MACHINE_SERIAL;
			labelText[i][1] = this.fieldRecallMSN;
			i++;
		}
		else
		{
			this.fieldRecallMSN = EMPTY_STRING;
		}

		if (!compRec.isPariDoNotCollect())
		{
			labelText[i][0] = CONTROL_NUMBER;
			labelText[i][1] = this.fieldRecallWU;
			i++;
		}
		else
		{
			this.fieldRecallWU = EMPTY_STRING;
		}
		if (!compRec.isCooiDoNotCollect())
		{
			labelText[i][0] = COUNTRY;
			if (this.fieldRecallCO.equals(EMPTY_STRING))
			{
				if (compRec.getCooList().length() / 47 == 1)
				{
					labelText[i][1] = compRec.getCooList().substring(0, 2);
				}
				else
				{
					// ~14 - if we are efficient, look up the previously used coo for this part
					if (MFSConfig.getInstance().containsConfigEntry("EFFICIENCYON")) //$NON-NLS-1$
					{
						// must pass the following checks
						if (compRec.isCcaiDoNotCollect() && 	// not collecting ccai
							compRec.isEcriDoNotCollect())	  	// not collecting ecri
						{
							if (MFSConfig.getInstance().getCooHash().containsKey(compRec.getItem()))
							{
								labelText[i][1] = MFSConfig.getInstance().getCooHash().get(compRec.getItem());
							}
						}
					}
				}
			}
			else
			{
				labelText[i][1] = this.fieldRecallCO;
			}
			i++;
		}
		else
		{
			this.fieldRecallCO = EMPTY_STRING;
		}

		if (this.fieldLoc.length() > 20)
		{
			this.vlLoc.setText(this.fieldLoc.substring(0, 20));
		}

		initTextComponents(labelText); //~7C
	}

	/** Logs the scanned in part information to the <code>MFSComponentRec</code>. */
	public void logPart()
	{
		try
		{
			int rc = 0;

			/* get current date and time ~2 */
			SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-ddHH.mm.ss"); //$NON-NLS-1$
			Date currTime = new Date();
			String curDateTimeStamp = fmt.format(currTime);

			//~5C Use new verifyAllInputCollected method and parsePartInfo method
			if (verifyAllInputCollected())
			{
				MFSPartInformation record = parsePartInfo(false);

				String pn = record.pn;
				String ec = record.ec;
				String sn = record.sn;
				String ca = record.ca;
				String ms = record.ms;
				String co = record.co;
				String wu = record.wu;

				MFSComponentRec compRec = this.fieldComp;
				String errorString = EMPTY_STRING;

				compRec.setRec_changed(true);
				compRec.setItem(pn);
				compRec.setInpn(pn);

				if (!compRec.isEcriDoNotCollect())
				{
					compRec.setInec(ec);
				}

				if (!compRec.isCsniDoNotCollect())
				{
					compRec.setInsq(sn);
				}

				if (!compRec.isCcaiDoNotCollect())
				{
					compRec.setInca(ca);
				}

				if (!compRec.isPariDoNotCollect())
				{
					compRec.setCwun(wu);
				}

				if (compRec.getMcsn().substring(0, 1).equals("$") && (!compRec.isCmtiDoNotCollect())) //$NON-NLS-1$
				{
					compRec.setMspi(ms.substring(0, 2));
					compRec.setMcsn(ms.substring(2));
				}

				if (compRec.getCooc().equals("  ") && !compRec.isCooiDoNotCollect()) //$NON-NLS-1$
				{
					compRec.setCooc(co);
				}

				compRec.setIdsp("A"); //$NON-NLS-1$
				compRec.setShtp(" "); //$NON-NLS-1$

				compRec.updtDisplayString();
				compRec.updtIRDisplayString();
				compRec.updtInstalledParts();

				if ((!compRec.isEcriDoNotCollect())
						|| (!compRec.isCsniDoNotCollect())
						|| (!compRec.isCcaiDoNotCollect())
						|| (!compRec.isCmtiDoNotCollect())
						|| (!compRec.isPariDoNotCollect())
						|| (!compRec.isPnriDoNotCollect() && !compRec.getItem().equals(pn)))
				{
					if (!compRec.isCsniDoNotCollect())
					{
						int index = 0;
						boolean found = false;
						while (index < this.fieldCompListModel.size() && !found)
						{
							MFSComponentRec cmp = this.fieldCompListModel.getComponentRecAt(index);
							if (cmp.getInpn().equals(pn)
									&& cmp.getInsq().equals(sn)
									&& (cmp.getIdsp().equals("I") || cmp.getIdsp().equals("R"))) //$NON-NLS-1$ //$NON-NLS-2$
							{
								found = true;
								rc = 10;
								errorString = "Duplicate Part/Serial found."; //$NON-NLS-1$
							}
							else
							{
								index++;
							}
						}
					}

					if (rc == 0)
					{
						if (!compRec.isPariDoNotCollect())
						{
							int index = 0;
							boolean found = false;
							while (index < this.fieldCompListModel.size() && !found)
							{
								MFSComponentRec cmp = this.fieldCompListModel.getComponentRecAt(index);
								if (cmp.getCwun().equals(wu)
										&& (cmp.getIdsp().equals("I") || cmp.getIdsp().equals("R"))) //$NON-NLS-1$ //$NON-NLS-2$
								{
									found = true;
									rc = 10;
									errorString = "Duplicate Control Number found."; //$NON-NLS-1$
								}
								else
								{
									index++;
								}
							}
						}
					} // end good S/N check

					if (rc == 0)
					{
						if (!compRec.isCmtiDoNotCollect())
						{
							int index = 0;
							boolean found = false;
							while (index < this.fieldCompListModel.size() && !found)
							{
								MFSComponentRec cmp = this.fieldCompListModel.getComponentRecAt(index);
								// if it isn't the same component and it
								// collects MS compare the MCSN values
								if ((!cmp.getCrct().equals(compRec.getCrct()))
										&& (!cmp.isCmtiDoNotCollect())
										&& (cmp.getMatp().equals(compRec.getMatp()))
										&& (cmp.getMcsn().equals(ms.substring(2)) 
												&& (cmp.getIdsp().equals("I") //$NON-NLS-1$ 
														|| cmp.getIdsp().equals("R")))) //$NON-NLS-1$
								{
									found = true;
									rc = 10;
									errorString = "Duplicate Machine Serial Number found."; //$NON-NLS-1$
								}
								else
								{
									index++;
								}
							}
						}
					} // end good S/N and Child Work Unit check
				}

				if (rc != 0)
				{
					IGSMessageBox.showOkMB(this, null, errorString, null);

					this.toFront();
					this.tfInput.setText(EMPTY_STRING);
					this.tfInput.requestFocusInWindow();
				}
				else
				{

					MfsXMLDocument xml_data1 = new MfsXMLDocument("EXTRA_CRCL"); //$NON-NLS-1$
					xml_data1.addOpenTag("DATA"); //$NON-NLS-1$
					xml_data1.addCompleteField("MCTL", compRec.getMctl()); //$NON-NLS-1$
					xml_data1.addCompleteField("INPN", pn); //$NON-NLS-1$
					xml_data1.addCompleteField("INSQ", sn); //$NON-NLS-1$
					xml_data1.addCompleteField("MALC", this.fieldLoc.substring(0, 7)); //$NON-NLS-1$
					xml_data1.addCompleteField("MILC", this.fieldLoc.substring(8, 20)); //$NON-NLS-1$
					xml_data1.addCompleteField("CWUN", wu); //$NON-NLS-1$
					xml_data1.addCompleteField("INEC", ec); //$NON-NLS-1$
					xml_data1.addCompleteField("INCA", ca); //$NON-NLS-1$
					xml_data1.addCompleteField("MSPI", ms.substring(0, 2)); //$NON-NLS-1$
					xml_data1.addCompleteField("MCSN", ms.substring(2)); //$NON-NLS-1$
					xml_data1.addCompleteField("COOC", co); //$NON-NLS-1$
					xml_data1.addCompleteField("USER", MFSConfig.getInstance().get8CharUser()); //$NON-NLS-1$
					xml_data1.addCloseTag("DATA"); //$NON-NLS-1$
					xml_data1.finalizeXML();

					MFSTransaction extra_crcl = new MFSXmlTransaction(xml_data1.toString());
					extra_crcl.setActionMessage("Creating New Component Record, Please Wait..."); //$NON-NLS-1$
					MFSComm.getInstance().execute(extra_crcl, this);
					rc = extra_crcl.getReturnCode();

					MfsXMLParser xmlParser = new MfsXMLParser(extra_crcl.getOutput());

					//~9C Update handling of EXTRA_CRCL output
					if (rc == 0 || rc == 100 || rc == 200)
					{
						if (rc == 0 || rc == 100)
						{
							compRec.setCrct(xmlParser.getField("CRCT")); //$NON-NLS-1$
							compRec.setCtlv(xmlParser.getField("CTLV")); //$NON-NLS-1$
							if (xmlParser.getField("PFPN").equals("Y") //$NON-NLS-1$ //$NON-NLS-2$
									|| xmlParser.getField("PFPN").equals("y")) //$NON-NLS-1$ //$NON-NLS-2$
							{
								this.fieldRememberPN = compRec.getInpn();
							}
							else
							{
								this.fieldRememberPN = EMPTY_STRING;
							}
							compRec.setCdes(xmlParser.getField("CDES")); /* ~1 */ //$NON-NLS-1$
						}

						if (rc == 100 || rc == 200)
						{
							errorString = extra_crcl.getOutput();
						}
					}
					/* MAC collection requests from EXTRA_CRCL */
					else if (rc == 111 || rc == 222 || rc == 333 || rc == 444 || rc == 555) //~12C
					{
						if (rc == 111)
						{
							compRec.setPnri("M"); //$NON-NLS-1$
						}
						else if (rc == 222)
						{
							compRec.setPnri("T"); //$NON-NLS-1$
						}
						else if (rc == 333)
						{
							compRec.setPnri("U"); //$NON-NLS-1$
						}
						else if (rc == 444)
						{
							compRec.setPnri("V"); //$NON-NLS-1$
						}
						else if (rc == 555) //~12A
						{
							compRec.setPnri("N"); //$NON-NLS-1$
						}					
								
						//updateMac method collects mac values and recalls extra_crcl
						//~9C Pass MACQ and MFSIntStringPair to updateMAC
						String macq = xmlParser.getField("MACQ"); //$NON-NLS-1$
						MFSIntStringPair pair = new MFSIntStringPair(0, errorString);
						updateMAC(compRec, pn, sn, this.fieldLoc, wu, ec, ca, ms, co, macq, pair);
						rc = pair.fieldInt;
						errorString = pair.fieldString;	
						
						/* ~12A Asset tag collection/verification for CSC */
						if(rc == 0 && compRec.getPnri().equals("N")) //$NON-NLS-1$
						{
							MFSInputDialog inputDialog = new MFSInputDialog(this.getParentFrame(), "Asset Tag", 20, null, null); //$NON-NLS-1$
							inputDialog.setVisible(true);
							
							if(inputDialog.getProceedSelected())
							{	
								IGSXMLTransaction log_asset = new IGSXMLTransaction("LOG_ASSET"); //$NON-NLS-1$
								log_asset.startDocument();
								log_asset.addElement("MFGN", this.fieldHeaderRec.getMfgn()); //$NON-NLS-1$
								log_asset.addElement("INPN", pn); //$NON-NLS-1$
								log_asset.addElement("INSQ", sn); //$NON-NLS-1$
								log_asset.addElement("MCTL", compRec.getMctl()); //$NON-NLS-1$
								log_asset.addElement("ATAG", inputDialog.getInputValue()); //$NON-NLS-1$
								log_asset.addElement("USER", MFSConfig.getInstance().get8CharUser()); //$NON-NLS-1$								
								log_asset.endDocument();
								log_asset.run();
								
								if (log_asset.getReturnCode() != 0)
								{
									throw new IGSTransactionException(log_asset, false);
								}						
							}
							else
							{
								rc = 13;
								errorString = "Asset Tag not collected.";				 //$NON-NLS-1$
							}
						}								
					}					
					else if (rc == MFSMacAddressUpdater.NO_MAC_ID_IN_SUB)
					{ 
						//~5A Prompt to collect MACID when missing on parts inside the assembly
						String data = xmlParser.getField("MSG"); //$NON-NLS-1$
						MFSMacAddressUpdater.updateChildMacIDs(this, data);
					}
					else
					{
						errorString = xmlParser.getField("ERMS"); //$NON-NLS-1$
					}					

					if (rc == 0)
					{
						this.fieldComponentAdded = 0;
					}
					else if (rc == 100)
					{
						this.fieldComponentAdded = 1;
					}
					else
					{
						this.fieldComponentAdded = 2;
					}

					if ((rc == 0) || (rc == 100))
					{
						final MFSConfig config = MFSConfig.getInstance();

						compRec.setIdsp("I"); //$NON-NLS-1$
						compRec.setFqty(compRec.getQnty());
						compRec.setPrln(this.fieldDirWork.getHeaderRec().getPrln()); /* ~2 */
						compRec.setProd(this.fieldDirWork.getHeaderRec().getProd()); /* ~2 */
						compRec.setUser(config.get8CharUser());/* ~2 */
						compRec.setCell(config.get8CharCell());/* ~2 */
						compRec.setCtyp(config.get8CharCellType());/* ~2 */
						compRec.setCsds(curDateTimeStamp.substring(0, 10)); /* ~2 */
						compRec.setCsts(curDateTimeStamp.substring(10)); /* ~2 */
						compRec.setPll1("    ");/* ~2 */ //$NON-NLS-1$
						compRec.setPll2("    ");/* ~2 */ //$NON-NLS-1$
						compRec.setPll3("    ");/* ~2 */ //$NON-NLS-1$
						compRec.setPll4("    ");/* ~2 */ //$NON-NLS-1$
						compRec.setPll5("    ");/* ~2 */ //$NON-NLS-1$
						compRec.setMatp("    ");/* ~2 */ //$NON-NLS-1$
						compRec.setPrdc("0");/* ~2 */    //$NON-NLS-1$
						compRec.updtDisplayString();
						compRec.updtIRDisplayString();
						compRec.updtInstalledParts();

						this.fieldComp = compRec;
						this.dispose();
					}//end of good call to extra_crcl

					if (rc != 0)
					{
						if (rc == 100 || rc == 200)
						{
							this.fieldDirWork.showPNSNErrorList(errorString, 0); //~11C
						}
						/* ignore special error code from updateMac method */
						//~5A also check NO_MAC_ID_IN_SUB
						else if (rc != MFSMacAddressUpdater.RC_ERR_NO_MSG_NEEDED
								&& rc != MFSMacAddressUpdater.NO_MAC_ID_IN_SUB) 
						{
							IGSMessageBox.showOkMB(this, null, errorString, null);
						}

						this.toFront();
						this.tfInput.setText(EMPTY_STRING);
						this.tfInput.requestFocusInWindow();
					}
				}// end of no dup data collected
			}//end of all data collected
		}//end of try block
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(this, null, null, e);
			focusForInput(); //~9C
		}
	}

	/** {@inheritDoc} */
	protected void recvInput()
	{
		try
		{
			MFSComponentRec compRec = this.fieldComp;
			boolean found = false;
			String errorString = MFSBCBarcodeDecoder.INVALID_BARCODE_ERMS;
			String dataCollectionErrorString = EMPTY_STRING;
			int rc = 0;

			MFSBCBarcodeDecoder barcode = MFSBCBarcodeDecoder.getInstance();
			barcode.setMyBarcodeInput(this.tfInput.getText());
			barcode.setMyBCPartObject(new MFSBCPartObject());

			rc = decodeBarcode(this.fieldHeaderRec, compRec, barcode); //~7C
			found = noInputToCollect(compRec, barcode.getBCMyPartObject()); //~7C

			if (rc == 0 && !found)
			{
				/* PN */
				if (!barcode.getBCMyPartObject().getPN().equals(EMPTY_STRING))
				{
					found |= setValueLabelText(PART_NUMBER, 2, barcode.getBCMyPartObject().getPN()); //~7C ~15					
					this.fieldRecallPN = (barcode.getBCMyPartObject().getPN());
					compRec.setItem(barcode.getBCMyPartObject().getPN());

					//collect pari flag - find if we need to collect child work unit or not
					dataCollectionErrorString = dataCollectionCheck(barcode.getBCMyPartObject().getPN());
				}
				/* EC */
				if (!(barcode.getBCMyPartObject().getEC().equals(EMPTY_STRING)))
				{
					found |= setValueLabelText(EC_NUMBER, 3, barcode.getBCMyPartObject().getEC()); //~7 ~15
					this.fieldRecallEC = (barcode.getBCMyPartObject().getEC());
				}
				/* SN */
				if (!(barcode.getBCMyPartObject().getSN().equals(EMPTY_STRING)))
				{
					if (validateSequenceNumber(compRec, barcode.getBCMyPartObject()))  //~7C
					{
						if(found = setValueLabelText(SEQUENCE_NUMBER, 4, barcode.getBCMyPartObject().getSN())) //~15
						{
							this.fieldCurrSN = barcode.getBCMyPartObject().getSN();
						}
						this.fieldRecallSN = barcode.getBCMyPartObject().getSN();
						this.pbGetPNList.setEnabled(true);
					}
				}
				/* CA */
				if (!(barcode.getBCMyPartObject().getCA().equals(EMPTY_STRING)))
				{
					found |= setValueLabelText(CARD_ASSEMBLY, 5, barcode.getBCMyPartObject().getCA()); //~15					
					this.fieldRecallCA = barcode.getBCMyPartObject().getCA();
				}
				/* MS */
				if (!(barcode.getBCMyPartObject().getMSN().equals(EMPTY_STRING)))
				{
					JLabel msLabel = getValueLabel(MACHINE_SERIAL, 5); //~15
					if (msLabel != null)
					{
						if (validateMachineSerial(compRec, barcode.getBCMyPartObject())) {//~7C
							VRFY_PI vrfyPI = new VRFY_PI(this); //~15
							vrfyPI.setInputMSN(barcode.getBCMyPartObject().getMSN().substring(0, 2));
							vrfyPI.setInputMspi(compRec.getMspi());
							vrfyPI.setInputMatp(compRec.getMatp());
							vrfyPI.execute();
							
							rc = vrfyPI.getReturnCode();
							if (rc != 0)
							{
								errorString = vrfyPI.getErrorMessage();
							}
							else
							{
								msLabel.setText(barcode.getBCMyPartObject().getMSN()); //~7C
								found = true; 
								this.fieldRecallMSN = barcode.getBCMyPartObject().getMSN();
							}
						}
					}
				}				
				/**** ~13A ******** RTV_WUBYPS Transation ********************/
				if (!this.fieldRecallPN.equals(EMPTY_STRING) && 
					!this.fieldRecallSN.equals(EMPTY_STRING) &&
					compRec.getPari().equals("V")) //$NON-NLS-1$
				{
					RTV_WUBYPS rtvWUBYPS = new RTV_WUBYPS(this); //~15						
					rtvWUBYPS.setInputINPN(this.fieldRecallPN);
					rtvWUBYPS.setInputINSQ(this.fieldRecallSN);
					rtvWUBYPS.setInputLOCAL("0"); //$NON-NLS-1$							
					rtvWUBYPS.execute();						
																			
					rc = rtvWUBYPS.getReturnCode();					
					if (rc == 0)
					{
						barcode.getBCMyPartObject().setWU(rtvWUBYPS.getOutputMCTL());
					}
					else
					{
						errorString = rtvWUBYPS.getErrorMessage();								
					}			
				}
				/**** ~13A ******** End of RTV_WUBYPS Transation **************/				
				/* WU */
				if (!(barcode.getBCMyPartObject().getWU().equals(EMPTY_STRING)))
				{
					if (!compRec.isCooiDoNotCollect())
					{
						RTV_SIGPN rtvSigPN = new RTV_SIGPN(this); //~15
						rtvSigPN.setInputWU(barcode.getBCMyPartObject().getWU());
						rtvSigPN.execute();
						
						rc = rtvSigPN.getReturnCode();
						if (rc == 0)
						{
							String co = rtvSigPN.getOutputCOO();
							if (!co.equals(EMPTY_STRING))
							{
								barcode.getBCMyPartObject().setCO(co);
							}							
						}
						else
						{
							errorString = rtvSigPN.getErrorMessage();
						}
					}
					if (rc == 0)
					{
						found |= setValueLabelText(CONTROL_NUMBER, 6, barcode.getBCMyPartObject().getWU()); //~7C ~15
						this.fieldRecallWU = (barcode.getBCMyPartObject().getWU());
					} // end good return code from transaction
				}
				/* CO */
				if (!(barcode.getBCMyPartObject().getCO().equals(EMPTY_STRING)))
				{
					found |= setValueLabelText(COUNTRY, 7, barcode.getBCMyPartObject().getCO()); //~7C ~15
					this.fieldRecallCO = (barcode.getBCMyPartObject().getCO());
				}
			}
			//good return from dataCollectionCheck - so reset display for any new fields
			if (dataCollectionErrorString.length() == 0)
			{
				initDisplay();
			}
			else
			{
				IGSMessageBox.showEscapeMB(this, null, dataCollectionErrorString, null);
			}
			if ((rc != 0) || (!found && !this.tfInput.getText().equals(EMPTY_STRING)))
			{
				if (this.tfInput.getText().toUpperCase().equals(MFSConstants.LOG_BARCODE))
				{
					this.tfInput.setText(EMPTY_STRING);
					this.pbLogPart.requestFocusInWindow();
					this.pbLogPart.doClick();
				}
				else
				{
					String erms = barcode.getBCMyPartObject().getErrorString();
					if (erms.length() == 0)
					{
						erms = errorString;
					}
					IGSMessageBox.showEscapeMB(this, null, erms, null);
				}
			}
			
			/* auto log these extra clog parts - if all input has been collected, then logPart */
			if (rc == 0 && allInputCollected()) //~7C
			{
				logPart();
			}
		}
		catch (Exception e)
		{
			IGSMessageBox.showEscapeMB(this, null, null, e);
		}

		this.toFront();
		this.tfInput.setText(EMPTY_STRING);
	}

	//~7A New method
	/**
	 * Reinitializes the state of this <code>MFSExtraClogPartDialog</code>.
	 * @param headerRec the <code>MFSHeaderRec</code> for the work unit
	 * @param listModel the <code>MFSComponentListModel</code> for the work unit
	 * @param comp the <code>MFSComponentRec</code> for the current component
	 * @param loc the location
	 * @param pn the part number
	 */
	public void reinitialize(MFSHeaderRec headerRec, MFSComponentListModel listModel,
								MFSComponentRec comp, String loc, String pn)
	{
		this.fieldHeaderRec = headerRec;
		this.fieldCompListModel = listModel;
		this.fieldComp = comp;
		this.fieldLoc = loc;

		this.fieldRecallCA = EMPTY_STRING;
		this.fieldRecallEC = EMPTY_STRING;
		this.fieldRecallMSN = EMPTY_STRING;
		this.fieldRecallPN = EMPTY_STRING;
		this.fieldRecallSN = EMPTY_STRING;
		this.fieldRecallWU = EMPTY_STRING;

		if (pn.length() > 0)
		{
			this.fieldRememberPN = pn;
		}

		initDisplay();
	}

	//~5C Change updateMAC to return both the rc and errorString
	// Note: this is only called from logPart after a call to EXTRA_CRCL
	// logPart handles the NO_MAC_ID_IN_SUB return code
	// Thus, no changes were made to this method to handle NO_MAC_ID_IN_SUB
	//~9C Take MACQ and MFSIntStringPair as parameters
	/**
	 * Use this method to collect mac data as necessary 30431PT
	 * @param compRec the <code>MFSComponentRec</code>
	 * @param pn the part number
	 * @param sn the sequence number
	 * @param loc the location
	 * @param wu the work unit control number
	 * @param ec the EC number
	 * @param ca the card assembly
	 * @param ms the machine serial
	 * @param co the country
	 * @param macq the MAC quantity
	 * @param pair the <code>MFSIntStringPair</code> for the return code and
	 *        error message
	 */
	private void updateMAC(MFSComponentRec compRec, String pn, String sn, String loc,
							String wu, String ec, String ca, String ms, String co,
							String macq, MFSIntStringPair pair)
	{
		try
		{
			if (sn.trim().length() == 0)
			{
				pair.fieldInt = MFSMacAddressUpdater.RC_ERR_NO_MSG_NEEDED;
				String erms = "MAC ID collection is required.  Serial Number needs to be collected for this part as well!"; //$NON-NLS-1$
				IGSMessageBox.showOkMB(this, null, erms, null);
				focusForInput();
			}
			else
			{
				//~5C Changed creation of MFSMACIDDialog
				//~9C Use createDialog method to create the MFSMacIDDialogBase
				//~10C Change createDialog to display error and return null
				//     instead of throwing MFSException with error message
				MFSMacIDDialogBase macDialog = MFSMacAddressUpdater.createDialog(this, macq, compRec.getPnri());
				if(macDialog != null)
				{
					macDialog.setLocationRelativeTo(this);
					macDialog.setVisible(true);
				}
				
				//~10C Handle null dialog and cancel
				if(macDialog == null || macDialog.getDoneSelected() == false)
				{
					//If macDialog is null, an error message was already displayed
					//Otherwise, the user pressed cancel, so no error message is needed
					pair.fieldInt = MFSMacAddressUpdater.RC_ERR_NO_MSG_NEEDED;
				}
				else
				{
					MfsXMLDocument xml_data1 = new MfsXMLDocument("EXTRA_CRCL"); //$NON-NLS-1$
					xml_data1.addOpenTag("DATA"); //$NON-NLS-1$
					xml_data1.addCompleteField("MCTL", compRec.getMctl()); //$NON-NLS-1$
					xml_data1.addCompleteField("INPN", pn); //$NON-NLS-1$
					xml_data1.addCompleteField("INSQ", sn); //$NON-NLS-1$
					xml_data1.addCompleteField("MALC", loc.substring(0, 7)); //$NON-NLS-1$
					xml_data1.addCompleteField("MILC", loc.substring(8, 20)); //$NON-NLS-1$
					xml_data1.addCompleteField("CWUN", wu); //$NON-NLS-1$
					xml_data1.addCompleteField("INEC", ec); //$NON-NLS-1$
					xml_data1.addCompleteField("INCA", ca); //$NON-NLS-1$
					xml_data1.addCompleteField("MSPI", ms.substring(0, 2)); //$NON-NLS-1$
					xml_data1.addCompleteField("MCSN", ms.substring(2)); //$NON-NLS-1$
					xml_data1.addCompleteField("COOC", co); //$NON-NLS-1$
					xml_data1.addCompleteField("USER", MFSConfig.getInstance().get8CharUser()); //$NON-NLS-1$
					macDialog.addMacXmlToDocument(xml_data1); //~9C
					xml_data1.addCloseTag("DATA"); //$NON-NLS-1$
					xml_data1.finalizeXML();

					MFSTransaction extra_crcl = new MFSXmlTransaction(xml_data1.toString());
					extra_crcl.setActionMessage("Updating MAC Address ID's, Please Wait..."); //$NON-NLS-1$
					MFSComm.getInstance().execute(extra_crcl, this);

					MfsXMLParser xmlParser = new MfsXMLParser(extra_crcl.getOutput());
					pair.fieldInt = extra_crcl.getReturnCode();

					//~9C Update handling of EXTRA_CRCL output
					if (pair.fieldInt == 100 || pair.fieldInt == 200)
					{
						pair.fieldString = extra_crcl.getOutput();
					}
					else if (pair.fieldInt != 0)
					{
						pair.fieldString = xmlParser.getField("ERMS"); //$NON-NLS-1$
					}

					if (pair.fieldInt == 0 || pair.fieldInt == 100)
					{
						compRec.setCrct(xmlParser.getField("CRCT")); //$NON-NLS-1$
						compRec.setCtlv(xmlParser.getField("CTLV")); //$NON-NLS-1$
						if (xmlParser.getField("PFPN").equals("Y") //$NON-NLS-1$ //$NON-NLS-2$
								|| xmlParser.getField("PFPN").equals("y")) //$NON-NLS-1$ //$NON-NLS-2$
						{
							this.fieldRememberPN = (compRec.getInpn());
						}
						else
						{
							this.fieldRememberPN = EMPTY_STRING;
						}
						compRec.setCdes(xmlParser.getField("CDES")); /* ~1 */ //$NON-NLS-1$
					}
				}
			}//end of valid serial number
		}
		catch (Exception e)
		{
			//~9C Set return code to RC_ERR_NO_MSG_NEEDED
			pair.fieldInt = MFSMacAddressUpdater.RC_ERR_NO_MSG_NEEDED;
			IGSMessageBox.showOkMB(this, null, null, e);
			focusForInput();
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
			if (source == this.pbLogPart)
			{
				logPart();
			}
			else if (source == this.pbGetPNList)
			{
				choosePn();
			}
			else if (source == this.pbCancel)
			{
				this.fieldPressedCancel = true;
				dispose();
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
			if (source == this.pbLogPart)
			{
				this.pbLogPart.requestFocusInWindow();
				this.pbLogPart.doClick();
			}
			else if (source == this.pbCancel)
			{
				this.pbCancel.requestFocusInWindow();
				this.pbCancel.doClick();
			}
			else if (source == this.tfInput)
			{
				recvInput();
			}
		}
		else if (keyCode == KeyEvent.VK_F2)
		{
			this.pbLogPart.requestFocusInWindow();
			this.pbLogPart.doClick();
		}
		else if (keyCode == KeyEvent.VK_ESCAPE)
		{
			this.pbCancel.requestFocusInWindow();
			this.pbCancel.doClick();
		}
	}
}
