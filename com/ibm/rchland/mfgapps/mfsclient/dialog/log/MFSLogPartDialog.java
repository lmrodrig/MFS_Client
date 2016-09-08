/* © Copyright IBM Corporation 2002, 2013. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 *           |  ~1|26157CD |David Fichtinger|-Blank out compRec.inpn to fix prb
 *           |    |        |                | when adding back into kit
 * 2003-10-16|  ~2|24594DK |E Engelbert     |-Changed value of seq passed to RTV_CUODAC
 *           |    |        |                | to be determined by BRLN value.
 * 2004-05-17|  ~3|25455KK |Dan Kloepping   |-INITIAL WRITE
 * 2004-05-17|  ~4|26632JM |Dan Kloepping   |-WWNN call addition
 * 2004-05-17|  ~5|25455KK |Dan Kloepping   |-WWNN call addition
 * 2004-06-01|    |        |Dan Kloepping   |-Moved the logic for calling the 
 *           |    |        |                | mfsutilities.MfsTransaction into the WWNNJDialog
 * 2004-06-03|    |        |                |-Replace the two wwnn calls with one, 
 *           |    |        |                | and let the dialog handle it
 * 2004-08-25|  ~6|27105JM |Dave Fichtinger |-Processor Matching logic
 *           |    |        |                |-FSUB checks added
 * 2004-09-02|  ~7|28934PT |Tou Lee Moua    |-Pass blank malc/milc into VRFY_PART
 * 2004-10-28|  ~8|28663DI |Dave Fichtinger |-Skip VRFY_PART for certain parts
 * 2006-05-03|  ~9|34818RB |JL Woodward     |-Add check of PX10 logic. Call VRFY_PX10 trx.
 * 2006-04-30| ~10|34647EM |Toribioh        |-Change Input capacity for JTextFieldLen
 *           |    |        |                | and validate input size and Barcode Rules
 *           |    |        |                | in WWNNDialog class
 * 2006-05-08| ~11|34647EM |DFICHT          |-WWNN Constructor doesn't need PNRI
 * 2006-06-29| ~12|31801JM |R Prechel       |-Prompt to collect MACID when missing
 *           |    |        |                | on parts inside the assembly
 *           |    |        |                |-Changed parent class to MFSLogPartDialog;
 *           |    |        |                | inherited methods previously defined locally
 *           |    |        |                |-Removed unnecessary methods, variables, parameters, and imports
 *           |    |        |                |-Moved VRFY_PART call to new method
 *           |    |        |                |-Changed creation of MACIDJDialog
 *           |    |        |                |-Changed class to have an ActionListener
 * 2006-06-29| ~13|34222BP |VH Avila        |-Delete the updtViewInstPartsString() calls becasue
 *           |    |        |                | the variable was deleted from Component_Rec class
 * 2007-01-09| ~14|36769FR |Toribioh        |-Change sequence var init values to "0000" in tcodTrx method
 * 2007-02-26| ~15|34242JR |R Prechel       |-Java 5 version
 * 2007-03-30| ~16|38166JM |R Prechel       |-Add initDisplay and setLocationRelativeTo for MFSWWNNDialog
 * 2007-07-27| ~17|27794JR |Toribio Hdez.   |-Create static methods from LogPartRemove
 *           |    |        |                | to be able to call them from other classes
 * 2007-08-10| ~18|39464JM |R Prechel       |-Fix parsing of RTV_CUODKY output
 * 2007-08-27| ~19|38007SE |R Prechel       |-MAC Address changes
 * 2007-09-06| ~20|39786JM |R Prechel       |-MFSMacAddressUpdater.createDialog changes
 * 2007-10-24| ~21|33507SE |Toribio Hdez.   |-Change params when Calling RTV_WUBYSP to
 *           |    |        |                | be xml and add new LOCAL param as 0
 * 2007-11-21| ~22|40223PB |Martha Luna     |-Validate into logPartAdd method if part detail button was pressed
 * 2007-01-08| ~23|38033JM |Luis M.         |-Allow rework using a differente MS when collection set
 * 2008-01-14|    |39782JM |Martha Luna		|-Changes the name of updtMultilineDisplayString by updtIRDisplayString
 * 2008-01-22| ~24|30635SE |Martha Luna		|-Add new call of dialog SEQN when is requeried
 * 2008-05-08| ~25|39568MZ |D Pietrasik     |-Get and print smart serial for ISS order
 * 2008-06-09| ~26|42086JM |M Barth         |-Trim 'ISS' SAPO field
 * 2008-10-21| ~27|41356MZ |Santiago SC     |-Asset tag collection/verification
 * 2009-05-05| ~28|40039JL |VH Avila        |-tcodTrx main logic was moved out to DirectWorkPanel
 * 2009-06-03| ~29|43813MZ |Christopher O   |-Call MFSPartDataCollectDialog if Data Collection is required
 * 2009-06-24|    |45549SR |Christopher O   |-Validate error Message in Data Collect Dialog
 * 2009-07-03| ~30|45654SR |Christopher O   |-Add collectMode param in containsComponentCollection function.
 * 2009-07-09| ~31|45691EM |Christopher O   |-Validate null object before call getDataCollection function
 * 2009-07-10| ~32|45703EM |Christopher O   |-Move the collectDialog after fkitRemoveRTR() within logPartRemove Function
 *           |    |        |                |-move the collectDialog after call CHCK_PLUG Transaction in logPartRework
 * 2010-03-06| ~33|47595MZ |Toribio H.      |-Client Efficiency changes
 * 2010-11-01| ~34|49513JM |Toribio H.      |-Remember selected/scanned Coo when scanned Barcode 
 *           |    |        |                | does not retrieve it (or the transactions triggered by it)
 * 2011-11-14| ~35|588480  |Santiago SC     |-Grey Market, new VRFYPNPLUS logic
 * 2011-11-29| ~36|50723FR |Toribio H       |-Grey Market, new VRFYPNPLUS logic
 * 2011-12-20| ~37|50723FR |Toribio H       |-Grey Market, If pn and sn are scanned update autoserial flag.
 * *****************************************************************************
 * 2012-04-12| ~38|RCQ00200225|Edgar V.     |-Verify the Machine Model.
 * 2013-03-08| ~39|RCQ00231649|Edgar V.     |-Send a new parameter when SrchLog.
 * 2013-11-13| ~40|RCQ00267733|Miguel Rivas |-Verify build ahead part
 * 2014-06-09| ~41|RCQ00281828|Efraín Mota |-MFS client needs updates to the APPLYUNIT trx call, add parameters to trx and InstallBuildAhead fixes
 * 2014-06-18| ~42|Dft1165621 |VH AVila     |-MFS Client needs to get a new XML response from APPLYUNIT trx
 * 2014-09-02| ~43|Story1194108|VH Avila    |-Set AMSI field to 1 after that APPLYUNIT is called for the BA part record.
 *******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.dialog.log;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.MissingResourceException;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;

import mfsxml.MfsXMLDocument;
import mfsxml.MfsXMLParser;

import com.ibm.rchland.mfgapps.client.utils.exception.IGSException;
import com.ibm.rchland.mfgapps.client.utils.exception.IGSTransactionException;
import com.ibm.rchland.mfgapps.client.utils.messagebox.IGSMessageBox;
import com.ibm.rchland.mfgapps.client.utils.xml.IGSXMLTransaction;
import com.ibm.rchland.mfgapps.mfsclient.MFSAbstractLogPartDialog;
import com.ibm.rchland.mfgapps.mfsclient.MFSPartInstructionJPanel;
import com.ibm.rchland.mfgapps.mfsclient.barcode.MFSBCBarcodeDecoder;
import com.ibm.rchland.mfgapps.mfsclient.barcode.MFSBCIndicatorValue;
import com.ibm.rchland.mfgapps.mfsclient.barcode.MFSBCPartObject;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSAcknowledgeInstructionDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSCFReportDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSCoaEntryDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSCooDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSGetOneValueDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSInputDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSMacIDDialogBase;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSSEQNDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSSubstPartsDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSVendorSubPartsController;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSWWNNDialog;
import com.ibm.rchland.mfgapps.mfsclient.panel.MFSDirectWorkPanel;
import com.ibm.rchland.mfgapps.mfsclient.struct.MFSComponentListModel;
import com.ibm.rchland.mfgapps.mfsclient.struct.MFSComponentRec;
import com.ibm.rchland.mfgapps.mfsclient.struct.MFSHeaderRec;
import com.ibm.rchland.mfgapps.mfsclient.struct.MFSIntStringPair;
import com.ibm.rchland.mfgapps.mfsclient.struct.MFSPartInformation;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSComponentSearch;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSComponentSearchCriteria;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSConstants;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSMacAddressUpdater;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSPrintingMethods;
import com.ibm.rchland.mfgapps.mfscommon.MFSActionable;
import com.ibm.rchland.mfgapps.mfscommon.MFSConfig;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSComm;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSFixedTransaction;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSTransaction;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSXmlTransaction;
import com.ibm.rchland.mfgapps.mfscommon.comm.trx.RTV_CTRY;
import com.ibm.rchland.mfgapps.mfscommon.comm.trx.RTV_SIGPN;
import com.ibm.rchland.mfgapps.mfscommon.comm.trx.RTV_WUBYPS;
import com.ibm.rchland.mfgapps.mfscommon.comm.trx.VRFYPNPLUS;
import com.ibm.rchland.mfgapps.mfscommon.comm.trx.VRFY_PART;
import com.ibm.rchland.mfgapps.mfscommon.comm.trx.VRFY_PI;
import com.ibm.rchland.mfgapps.mfscommon.exception.MFSTransactionException;

/**
 * <code>MFSLogPartJDialog</code> is the <code>MFSAbstractLogPartDialog</code>
 * used to log a part.
 * @author The MFS Client Development Team
 */
@SuppressWarnings("serial")
public class MFSLogPartDialog
	extends MFSAbstractLogPartDialog
{
	//~15A Added log type constants.
	/** The AutoLog log type. */
	public static final String LT_AUTO_LOG = "AutoLog"; //$NON-NLS-1$

	/** The Add log type. */
	public static final String LT_ADD = "Add"; //$NON-NLS-1$

	/** The Remove log type. */
	public static final String LT_REMOVE = "Remove"; //$NON-NLS-1$

	/** The Rework log type. */
	public static final String LT_REWORK = "Rework"; //$NON-NLS-1$

	/** The SrchLog log type. */
	public static final String LT_SEARCH_LOG = "SrchLog"; //$NON-NLS-1$

	/** The Search Remove log type. */
	public static final String LT_SEARCH_REMOVE = "Search Remove"; //$NON-NLS-1$
	
	/*Return code for build ahead*/
	public static final int BA_FAILURE=-1; //~41

	//~17A
	/** 
	 * Calls the CHCK_PLUG transaction.
	 * @param compRec the <code>MFSComponentRec</code>
	 * @param actionable the <code>MFSActionable</code>
	 * @return <code>MFSIntStringPair</code>
	 */
	public static MFSIntStringPair checkPlug(MFSComponentRec compRec,
													MFSActionable actionable)
	{
		MFSIntStringPair rcPair = new MFSIntStringPair(0, EMPTY_STRING);
		
		/* new trx for checking plugged parts for ipsr 18841JM 8/22/01 */
		if (!compRec.isCsniDoNotCollect())
		{
			/* build check plug info string */
			StringBuffer data = new StringBuffer(75);
			data.append("CHCK_PLUG "); //$NON-NLS-1$
			data.append(compRec.getInpn());
			data.append(compRec.getInsq());
			data.append(compRec.getMspi());
			data.append(compRec.getMcsn());
			data.append(compRec.getMctl());
			data.append(compRec.getPrln());
			data.append(compRec.getNmbr());
			data.append(MFSConfig.getInstance().get8CharUser());
			data.append(compRec.getCrct());

			MFSTransaction chck_plug = new MFSFixedTransaction(data.toString());
			chck_plug.setActionMessage("Checking Plug Limit, Please Wait..."); //$NON-NLS-1$
			MFSComm.getInstance().execute(chck_plug, actionable);
			rcPair.fieldInt = chck_plug.getReturnCode();
			rcPair.fieldString = chck_plug.getOutput();
		}/* end-if serial part check */
		/* end trx for checking plugged parts for ipsr 18841JM 8/22/01 */
		
		return rcPair;
	}

	//~17A
	/**
	 * Calls the FKRMV_RTR transaction.
	 * @param compRec the <code>MFSComponentRec</code>
	 * @param nmbr the operation number
	 * @param actionable the <code>MFSActionable</code>
	 * @return <code>MFSIntStringPair</code>
	 */	
	public static MFSIntStringPair fkitRemoveRTR(MFSComponentRec compRec,
			String nmbr, MFSActionable actionable)
	{
		MFSIntStringPair rcPair = new MFSIntStringPair(0, EMPTY_STRING);
		
		// update fkit realtime
		if (nmbr.equals("FKIT")) //$NON-NLS-1$
		{
			/* build fkrmv_rtr transaction string */
			MFSConfig config = MFSConfig.getInstance();
			StringBuffer data = new StringBuffer(46);
			data.append("FKRMV_RTR "); //$NON-NLS-1$
			data.append(compRec.getMctl());
			data.append(config.get8CharCell());
			data.append(config.get8CharCellType());
			data.append(config.get8CharUser());
			data.append(compRec.getCrct());

			MFSTransaction fkrmv_rtr = new MFSFixedTransaction(data.toString());
			fkrmv_rtr.setActionMessage("Removing part from Kit, Please Wait..."); //$NON-NLS-1$
			MFSComm.getInstance().execute(fkrmv_rtr, actionable);
			rcPair.fieldInt = fkrmv_rtr.getReturnCode();
			rcPair.fieldString = fkrmv_rtr.getOutput();
		}// end of FKIT check

		return rcPair;
	}

	//~17A
	/**
	 * Updates the removed <code>MFSComponentRec</code>.
	 * @param overrideQty the override quantity flag
	 * @param qt the quantity
	 * @param compRec the <code>MFSComponentRec</code>
	 * @param nmbr the operation number
	 */
	public static void updateComponentRecRemoved(boolean overrideQty, int qt, 
			MFSComponentRec compRec, String nmbr)
	{
		compRec.setRec_changed(true);

		int fqty = Integer.parseInt(compRec.getFqty()) - qt;

		if (MFSConfig.getInstance().containsConfigEntry("PARTIALQUANTITY") == false && !overrideQty) //$NON-NLS-1$
		{
			fqty = 0;
		}
		int qnty = Integer.parseInt(compRec.getQnty());

		if (qnty == 0)
		{
			fqty = 0;
		}
		if (fqty == 0)
		{
			if (nmbr.equals("FKIT")) //$NON-NLS-1$
			{
				compRec.setIdsp("A"); //$NON-NLS-1$
				/*
				 * ~1 set inpn to blanks here, to prevent problems
				 * when adding this part back into the kit - PTR
				 * 26157CD - leave the rest of the fields as is for
				 * now, but should probably blank them out too
				 */
				compRec.setInpn("            "); //$NON-NLS-1$
			}
			else
			{
				compRec.setIdsp("D"); //$NON-NLS-1$
			}
			compRec.setShtp(" "); //$NON-NLS-1$
		}

		String Fqty = "00000" + fqty; //$NON-NLS-1$
		compRec.setFqty(Fqty.substring(Fqty.length() - 5));

		compRec.updtDisplayString();
		compRec.updtInstalledParts();
		compRec.updtIRDisplayString();
	}

	//~17A
	/**
	 * Calls the UPDT_MCODE transaction.
	 * @param compRec the <code>MFSComponentRec</code>
	 * @param pn the part number
	 * @param sn the sequence number
	 * @param actionable the <code>MFSActionable</code>
	 * @return <code>MFSIntStringPair</code>
	 */
	public static MFSIntStringPair updateMCode(MFSComponentRec compRec,
			String pn, String sn, MFSActionable actionable)
	{
		MFSIntStringPair rcPair = new MFSIntStringPair(0, EMPTY_STRING);
		
		// now we will check if the part is an m-coded part
		// for m-coded parts, we want to call updt_mcode transaction to
		// handle SAP process
		if (compRec.getRefp().trim().equals("M-CODE")) //$NON-NLS-1$
		{
			/* build updt_mcode transaction string */
			StringBuffer data = new StringBuffer(58);
			data.append("UPDT_MCODE"); //$NON-NLS-1$
			data.append(pn);
			data.append(sn);
			data.append(compRec.getItem());
			data.append(compRec.getMctl());
			data.append(compRec.getCrct());

			MFSTransaction updt_mcode = new MFSFixedTransaction(data.toString());
			updt_mcode.setActionMessage("Updating M-Coded part information, Please Wait..."); //$NON-NLS-1$
			MFSComm.getInstance().execute(updt_mcode, actionable);
			rcPair.fieldInt = updt_mcode.getReturnCode();
			rcPair.fieldString = updt_mcode.getOutput();
		}// end of m-code check
		
		return rcPair;		
	}

	//~17A
	/** 
	 * Validates the remove values against the <code>MFSComponentRec</code>.
	 * @param qt the quantity
	 * @param compRec the <code>MFSComponentRec</code>
	 * @param pn the part number
	 * @param sn the sequence number
	 * @param ec the EC number
	 * @param ca the card assembly
	 * @param wu the work unit control number
	 * @return <code>MFSIntStringPair</code>
	 */
	public static MFSIntStringPair validateComponentRec(
			int qt, MFSComponentRec compRec,
			String pn, String sn, String ec, String ca, String wu)
	{
		MFSIntStringPair rcPair = new MFSIntStringPair(0, EMPTY_STRING);

		if (!compRec.isEcriDoNotCollect() && !compRec.getInec().equals(ec))
		{
			// for serialized M-Coded parts, we don't want to fail this
			// check - we will be calling some special
			// logic to handle removing the M-Coded parts
			if (!compRec.getRefp().trim().equals("M-CODE")) //$NON-NLS-1$
			{
				rcPair.fieldInt = 10;
				rcPair.fieldString = "Invalid EC Number"; //$NON-NLS-1$
			}
		}
		if (!compRec.isPnriDoNotCollect() && !compRec.getInpn().equals(pn))
		{
			// for serialized M-Coded parts, we don't want to fail this
			// check - we will be calling some special
			// logic to handle removing the M-Coded parts
			if (!compRec.getRefp().trim().equals("M-CODE")) //$NON-NLS-1$
			{
				rcPair.fieldInt = 10;
				rcPair.fieldString = "Invalid Part Number"; //$NON-NLS-1$
			}
		}
		if (!compRec.isCsniDoNotCollect() && !compRec.getInsq().equals(sn))
		{
			// for serialized M-Coded parts, we don't want to fail this
			// check - we will be calling some special
			// logic to handle removing the M-Coded parts
			if (!compRec.getRefp().trim().equals("M-CODE")) //$NON-NLS-1$
			{
				rcPair.fieldInt = 10;
				rcPair.fieldString = "Invalid Serial Number"; //$NON-NLS-1$
			}
		}
		if (!compRec.isCcaiDoNotCollect() && !compRec.getInca().equals(ca))
		{
			// for serialized M-Coded parts, we don't want to fail this
			// check - we will be calling some special
			// logic to handle removing the M-Coded parts
			if (!compRec.getRefp().trim().equals("M-CODE")) //$NON-NLS-1$
			{
				rcPair.fieldInt = 10;
				rcPair.fieldString = "Invalid Card Assembly Number"; //$NON-NLS-1$
			}
		}
		if (!compRec.isPariDoNotCollect() && !compRec.getCwun().equals(wu))
		{
			// for serialized M-Coded parts, we don't want to fail this
			// check - we will be calling some special
			// logic to handle removing the M-Coded parts
			if (!compRec.getRefp().trim().equals("M-CODE")) //$NON-NLS-1$
			{
				rcPair.fieldInt = 10;
				rcPair.fieldString = "Invalid Child Work Unit Number";//$NON-NLS-1$
			}
		}
		if (Integer.parseInt(compRec.getFqty()) < qt)
		{
			// ptr (19661DL) for reapply problem - allow remove of 0 qty part
			if (Integer.parseInt(compRec.getFqty()) != 0
					|| Integer.parseInt(compRec.getQnty()) != 0)
			{
				rcPair.fieldInt = 10;
				rcPair.fieldString = "Invalid Part Quantity"; //$NON-NLS-1$
			}
		}

		return rcPair;
	}

	/** The Log Part (F2) <code>JButton</code>. */
	private JButton pbLogPart = createButton("F2 = Log Part"); //$NON-NLS-1$

	/** The Substitute (F4) <code>JButton</code>. */
	private JButton pbSubPart = createButton("F4 = Substitute"); //$NON-NLS-1$

	/** The Country (F6) <code>JButton</code>. */
	private JButton pbCountry = createButton("F6 = Country"); //$NON-NLS-1$

	/** The Cancel (Esc) <code>JButton</code>. */
	private JButton pbCancel = createButton("Esc = Cancel"); //$NON-NLS-1$
	
	/** The <code>MFSDirectWorkPanel</code> that uses this dialog. */
	private MFSDirectWorkPanel fieldDirWork; //set by constructor

	/** The log type. */
	private String fieldLogType; //set by constructor

	/** The <code>JList</code> that displays the parts. */
	private JList fieldJList; //set by constructor
	
	/** The <code>MFSComponentListModel</code> for the work unit. */
	private MFSComponentListModel fieldCompListModel; //set by constructor
	
	/** Set <code>true</code> if a rework part is logged. */
	private boolean fieldRwkPartLogged = false;

	/** * Set <code>true</code> if a 100 or 200 error occurs when a rework part is logged. */
	private boolean fieldRwkErrorDetail = false;       //~22

	/** The <code>Vector</code> of <code>MFSPartInstructionJPanel</code>s. */
	private Vector<MFSPartInstructionJPanel> fieldRowVector = new Vector<MFSPartInstructionJPanel>();

	/** The index of the active row in {@link #fieldRowVector}. */
	private int fieldActiveRow = 0;

	/** Single Coo pre-populated */
	private boolean singleCoo = false;

	/**
	 * Constructs a new <code>MFSLogPartDialog</code>.
	 * @param dirWork the <code>MFSDirectWorkPanel</code> that uses this dialog
	 * @param logType the log type
	 * @param headerRec the <code>MFSHeaderRec</code> for the work unit
	 * @param list the <code>JList</code> that displays the parts
	 * @param model the <code>MFSComponentListModel</code> for the work unit
	 */
	public MFSLogPartDialog(MFSDirectWorkPanel dirWork, String logType,
							MFSHeaderRec headerRec, JList list,
							MFSComponentListModel model)				
	{
		//Title set by initDisplay
		super(dirWork.getParentFrame(), null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		this.fieldDirWork = dirWork;
		this.fieldLogType = logType;
		this.fieldHeaderRec = headerRec;
		this.fieldJList = list;
		this.fieldCompListModel = model;

		createLayout();
		addMyListeners();
	}

	/**
	 * Invoked when an action occurs.
	 * @param ae the <code>ActionEvent</code>
	 */
	public void actionPerformed(ActionEvent ae)
	{
		final Object source = ae.getSource();
		if (source.equals(this.pbLogPart))
		{
			logPart();
		}
		else if (source.equals(this.pbCancel))
		{
			removeReworkCollectedData();                                     //~30A
			dispose();
		}
		else if (source.equals(this.pbSubPart))
		{
			listSubs();
		}
		else if (source.equals(this.pbCountry))
		{
			listCoo();
		}
	}
	
	/** Adds the listeners to this dialog's <code>Component</code>s. */
	private void addMyListeners()
	{
		this.pbLogPart.addActionListener(this);
		this.pbSubPart.addActionListener(this);
		this.pbCountry.addActionListener(this);
		this.pbCancel.addActionListener(this);

		this.pbLogPart.addKeyListener(this);
		this.pbSubPart.addKeyListener(this); //~15A
		this.pbCountry.addKeyListener(this);
		this.pbCancel.addKeyListener(this);

		this.tfInput.addKeyListener(this);
	}

	/**
	 * Returns <code>true</code> iff the country value is filled.
	 * @return <code>true</code> iff the country value is filled
	 */
	public boolean countryFilled()
	{
		boolean found = false;
		JLabel countryLabel = getValueLabel(COUNTRY, 0);
		if (countryLabel != null)
		{
			if (!countryLabel.getText().trim().equals(EMPTY_STRING))
			{
				found = true;
			}
		}
		return found;
	}

	/** Adds this dialog's <code>Component</code>s to the layout. */
	private void createLayout()
	{
		this.pnlButtons.add(this.pbLogPart);
		this.pnlButtons.add(this.pbCancel);
		this.pnlButtons.add(this.pbSubPart);
		this.pnlButtons.add(this.pbCountry);
		createLayout(false);
	}

	/**
	 * Executes the FKSUBS_RTR transaction to check if the scanned in part is a
	 * substitutable part.
	 * @param bcpn the scanned in part number
	 * @param scanQty the scanned in quantity
	 * @return the <code>MFSComponentRec</code> for the substitute
	 */
	public MFSComponentRec fkitSubsCheck(String bcpn, int scanQty)
	{
		String errorString = EMPTY_STRING;
		int rc = 0;
		int index = 0;
		int row = this.fieldActiveRow;
		boolean found = false;
		
		MFSComponentRec compRec = (MFSComponentRec) this.fieldJList.getSelectedValue();
		try
		{
			/* run the FKSUBS_RTR trx to get the parts the scanned in pn is substitutable for */
			MfsXMLDocument xml_data1 = new MfsXMLDocument("FKSUBS_RTR"); //$NON-NLS-1$
			xml_data1.addOpenTag("DATA"); //$NON-NLS-1$
			xml_data1.addCompleteField("PART", bcpn); //$NON-NLS-1$
			xml_data1.addCompleteField("PRLN", this.fieldHeaderRec.getPrln()); //$NON-NLS-1$
			xml_data1.addCompleteField("MFGN", this.fieldHeaderRec.getMfgn()); //$NON-NLS-1$
			xml_data1.addCompleteField("IDSS", this.fieldHeaderRec.getIdss()); //$NON-NLS-1$
			xml_data1.addCompleteField("MCTL", this.fieldHeaderRec.getMctl()); //$NON-NLS-1$
			xml_data1.addCloseTag("DATA"); //$NON-NLS-1$
			xml_data1.finalizeXML();

			MFSTransaction fksubs_rtr = new MFSXmlTransaction(xml_data1.toString());
			MFSComm.getInstance().execute(fksubs_rtr, this);
			rc = fksubs_rtr.getReturnCode();
			
			if (rc != 0)
			{
				errorString = fksubs_rtr.getErms();
			}
			else
			{
				MfsXMLParser xmlParser = new MfsXMLParser(fksubs_rtr.getOutput());
				String tempSubPart = xmlParser.getField("PART"); //$NON-NLS-1$

				while (!tempSubPart.equals(EMPTY_STRING) && !found)
				{
					row = this.fieldActiveRow;
					index = this.fieldJList.getSelectedIndex();

					//we have a legit substituable part, tempSubPart,
					//now lets see if its called out in this kit
					MFSComponentSearch componentFinder = 
						new MFSComponentSearch(this.fieldRowVector,
							new MFSComponentSearchCriteria.MFSAutoLogSearchCriteria(tempSubPart));  //~33
					
					componentFinder.setSearchStartPosition(row, index);                      //~33
					componentFinder.setSearchNonPartInstruction(false);                      //~33

					if(componentFinder.findComponent())
					{
						found = true;
						JList pipPNList = getRowVectorElementAt(componentFinder.getRow()).getPNList();
						pipPNList.setSelectedIndex(componentFinder.getIndex());
						pipPNList.ensureIndexIsVisible(componentFinder.getIndex());
						
						compRec = (MFSComponentRec) pipPNList.getSelectedValue();
						this.fieldActiveRow = componentFinder.getRow();
						this.fieldJList = pipPNList;
						this.fieldCompListModel = getRowVectorElementAt(componentFinder.getRow()).getPNListModel();
						initDisplay();
						if (!compRec.getCsni().equals(" ") && //$NON-NLS-1$
								!compRec.getCsni().equals("0") && //$NON-NLS-1$
								!compRec.getQnty().equals("00000") && //$NON-NLS-1$
								!compRec.getQnty().equals("00001")) //$NON-NLS-1$
						{
							/* display error to user */
							dispose();
							rc = 10;
							errorString = "Cannot Collect S/N on Part with Qty > 1."; //$NON-NLS-1$
						}
					}
					tempSubPart = xmlParser.getNextField("PART"); //$NON-NLS-1$
				}//end of while loop
			}//end of good call to FKSUBS_RTR

			if (rc != 0)
			{
				System.out.println(errorString);
				compRec = null;
			}
		}
		catch (Exception e)
		{
			IGSMessageBox.showEscapeMB(this, null, null, e);
		}
		return compRec;
	}

	//~15A New method
	/**
	 * Returns the <code>MFSPartInstructionJPanel</code> at the specified
	 * index in {@link #fieldRowVector}.
	 * @param index an index into {@link #fieldRowVector}
	 * @return the <code>MFSPartInstructionJPanel</code> at the specified index
	 * @throws ArrayIndexOutOfBoundsException if the <code>index</code> is
	 *         negative or not less than the current size of the vector
	 * @throws ClassCastException if the element at <code>index</code> is not
	 *         an <code>MFSPartInstructionJPanel</code>
	 */
	private MFSPartInstructionJPanel getRowVectorElementAt(int index)
	{
		return (MFSPartInstructionJPanel) this.fieldRowVector.elementAt(index);
	}
	
	/**
	 * Returns <code>true</code> if a 100 or 200 error occurs when a rework
	 * part is logged.
	 * @return <code>true</code> if a 100 or 200 error occurs when a rework
	 * part is logged.
	 */
	public boolean getRwkErrorDetail()
	{
		return this.fieldRwkErrorDetail;
	}		

	/**
	 * Returns <code>true</code> if a rework part was logged.
	 * @return <code>true</code> if a rework part was logged
	 */
	public boolean getRwkPartLogged()
	{
		return this.fieldRwkPartLogged;
	}
	
	/**
	 * Focuses and sets the completion status of the specified
	 * <code>MFSPartInstructionJPanel</code>.
	 * @param pip the <code>MFSPartInstructionJPanel</code>
	 * @param efficientClient <code>true</code> if running efficient client
	 */
	public void handleInstruction(MFSPartInstructionJPanel pip, boolean efficientClient)
	{
		try
		{
			int index = 0;
			boolean found = false;
			MFSComponentRec cmp = null;

			boolean hideMode = this.fieldDirWork.isHideMode();
			this.fieldDirWork.scrollToPip(pip);

			if (!pip.getInstructionRec().getCompletionStatus().equals("C")) //$NON-NLS-1$
			{
				if (pip.getInstructionRec().getSsVector().size() > 0)
				{
					//we are going to attempt to "highlight" the right side of
					// the pip. We will do that by setting the background
					//color to a blueish color, but we need to remember what
					//color it was initially, so if user decides not to "claim"
					//the instruction, we can put it back.
					pip.changeColor(MFSPartInstructionJPanel.BLUE_ROW_COLOR);

					if (pip.getIsNonPartInstruction())
					{
						//we are hidden and this instruction is not mandatory,
						//so we will flag it as complete
						if (hideMode && pip.getInstructionRec().getInstrClass().equals(" ")) //$NON-NLS-1$
						{
							pip.setCompletionStatus(MFSPartInstructionJPanel.COMPLETE);
						}
						else
						{
							MFSAcknowledgeInstructionDialog ackD = 
								new MFSAcknowledgeInstructionDialog(getParentFrame());
							ackD.setLocationRelativeTo(this);
							ackD.setVisible(true);
							
							if (ackD.getPressedLog())
							{
								pip.setCompletionStatus(MFSPartInstructionJPanel.COMPLETE);
								pip.setNoPartPanelBackground(Color.white);
							}
							else
							{
								pip.unDoChangeColor();
							}
						}
					}// non part instruction
					else
					{
						MFSComponentListModel tempLm = pip.getPNListModel();

						while (index < tempLm.size() && !found)
						{
							cmp = tempLm.getComponentRecAt(index);
							//don't care if mlr or not 
							if (cmp.getIdsp().equals("A") //$NON-NLS-1$ 
									|| cmp.getIdsp().equals("X") //$NON-NLS-1$
									|| cmp.getIdsp().equals("R")) //$NON-NLS-1$
							{
								found = true;
							}
							index++;
						}

						// found a component that needs to be added or removed
						// yet, so set to partial
						if (found)
						{
							pip.setCompletionStatus(MFSPartInstructionJPanel.PARTIALLY_COMPLETE);
							pip.unDoChangeColor();
						}
						else
						{
							if (pip.getInstructionRec().getInstrClass().equals(" ")) //$NON-NLS-1$
							{
								pip.setCompletionStatus(MFSPartInstructionJPanel.COMPLETE);
							}
							else
							{
								MFSAcknowledgeInstructionDialog ackD = 
									new MFSAcknowledgeInstructionDialog(getParentFrame());
								ackD.setLocationRelativeTo(this);
								ackD.setVisible(true);
								
								if (ackD.getPressedLog())
								{
									pip.setCompletionStatus(MFSPartInstructionJPanel.COMPLETE);
								}// acknowledged by the builder
								else
								{
									pip.unDoChangeColor();
								}
							}// non blank instruction class
						}//no more records to be added
					}//part instructions
				}// end of instructions present for this PIP
			}//end of instruction not already complete
		}
		catch (Exception e)
		{
			this.setCursor(Cursor.getDefaultCursor());
			IGSMessageBox.showOkMB(this, null, null, e, efficientClient);	// ~33
		}
	}

	/** Initializes the appearance of the dialog. */
	public void initDisplay()
	{
		//~15 Use log type constants
		/* set the title of the dialog */
		if (this.fieldLogType.equals(LT_AUTO_LOG))
		{
			setTitle("Autologging"); //$NON-NLS-1$
		}
		else if (this.fieldLogType.equals(LT_ADD))
		{
			setTitle("Add Part"); //$NON-NLS-1$
		}
		else if (this.fieldLogType.equals(LT_REMOVE))
		{
			setTitle("Remove Part"); //$NON-NLS-1$
			this.pbSubPart.setEnabled(false);
		}
		else if (this.fieldLogType.equals(LT_REWORK))
		{
			setTitle("Replace Part"); //$NON-NLS-1$
		}
		else if (this.fieldLogType.equals(LT_SEARCH_LOG))
		{
			setTitle("Search Log"); //$NON-NLS-1$
			this.pbSubPart.setEnabled(false);
		}
		else if (this.fieldLogType.equals(LT_SEARCH_REMOVE))
		{
			setTitle("Search Remove"); //$NON-NLS-1$
			this.pbSubPart.setEnabled(false);
		}

		//~15C Use a String[][] array
		String[][] labelText = new String[7][2];
		for (int s = 0; s < 7; s++)
		{
			labelText[s][0] = EMPTY_STRING;
			labelText[s][1] = EMPTY_STRING;
		}

		int i = 0;
		final MFSComponentRec compRec = (MFSComponentRec) this.fieldJList.getSelectedValue();
		if (!compRec.isPnriDoNotCollect())
		{
			labelText[i][0] = PART_NUMBER;
			i++;
		}
		if (!compRec.isEcriDoNotCollect())
		{
			labelText[i][0] = EC_NUMBER;
			i++;
		}
		if (!compRec.isCsniDoNotCollect() && !compRec.getCsni().equals("C")) //$NON-NLS-1$
		{
			labelText[i][0] = SEQUENCE_NUMBER;
			i++;
		}
		if (!compRec.isCcaiDoNotCollect())
		{
			labelText[i][0] = CARD_ASSEMBLY;
			i++;
		}
		if (!compRec.isCmtiDoNotCollect())
		{
			labelText[i][0] = MACHINE_SERIAL;
			i++;
		}
		if (!compRec.isPariDoNotCollect())
		{
			labelText[i][0] = CONTROL_NUMBER;
			i++;
		}		
		if (!compRec.isCooiDoNotCollect())
		{
			labelText[i][0] = (COUNTRY);
			this.singleCoo = false;			
			if (compRec.getCooList().length() / 47 == 1)
			{
				labelText[i][1] = (compRec.getCooList().substring(0, 2));
				this.singleCoo = true;				
			}
			else
			{
				// ~33 - if we are efficient, look up the previously used coo for this part
				if (MFSConfig.getInstance().containsConfigEntry("EFFICIENCYON")) //$NON-NLS-1$
				{
					String cooValue = this.getCooHash().get(compRec.getItem());
					if (cooValue != null)
					{
						labelText[i][1] = cooValue;
					}
				}
			}
			i++;
		}
		// if country not required, disable the countries button
		else
		{
			this.pbCountry.setEnabled(false);
		}
		initTextComponents(labelText); //~15C
	}
	//~35A
	/**
	 * Verify if the given component is a Grey Market Part.
	 * @param compRec the component record.
	 * @return true if grey part.
	 */
	private boolean isGreyPart(MFSComponentRec compRec)
	{
		return compRec.getCsni().equals("G"); //$NON-NLS-1$
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
				((JButton) source).requestFocusInWindow();
				((JButton) source).doClick();
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
		else if (keyCode == KeyEvent.VK_F4)
		{
			this.pbSubPart.requestFocusInWindow();
			this.pbSubPart.doClick();
		}
		else if (keyCode == KeyEvent.VK_ESCAPE)
		{
			this.pbCancel.requestFocusInWindow();
			this.pbCancel.doClick();
		}
		else if (keyCode == KeyEvent.VK_F6)
		{
			this.pbCountry.requestFocusInWindow();
			this.pbCountry.doClick();
		}
	}
	/** Invoked when {@link #pbCountry} is selected. */
	private void listCoo()
	{
		try
		{			
			int rc = 0;
			String in_data = EMPTY_STRING;
			
			boolean efficientClient = false;	// ~33
			if(MFSConfig.getInstance().containsConfigEntry("EFFICIENCYON"))//$NON-NLS-1$
			{
				efficientClient = true;
			}
			MFSComponentRec compRec = (MFSComponentRec) this.fieldJList.getSelectedValue();

			if (compRec.getPnri().equals("1")) //$NON-NLS-1$
			{
				// if there is a substitute part scanned in or
				// we are an SI call the RTV_CTRY transaction
				String pn = EMPTY_STRING;
				JLabel partLabel = this.getValueLabel(PART_NUMBER, 2);
				if(partLabel != null) {
					pn = partLabel.getText();
				}
				if((!pn.equals(EMPTY_STRING) && !pn.equals(compRec.getItem())) ||
						this.fieldHeaderRec.isSISytemType()) {				
					// pn might not be scanned in yet for SI orders, A,B,C, or D
					// typzs - so default it to the called out item
					if (pn.equals(EMPTY_STRING))
					{
						pn = compRec.getItem();
					}
					RTV_CTRY rtvCTRY = new RTV_CTRY(this); //~34
					rtvCTRY.setInputPN(pn);
					rtvCTRY.setInputTypz(this.fieldHeaderRec.getTypz());
					if (rtvCTRY.execute())
					{
						in_data = rtvCTRY.getOutputCooList();
					}
					else
					{
						IGSMessageBox.showOkMB(this, null, rtvCTRY.getErrorMessage(), null, efficientClient);// ~33
					}
					rc = rtvCTRY.getReturnCode();
				} /* end substitute found */
				else
				{
					in_data = compRec.getCooList();
				}
			}/* end pnri = '1' */
			else
			{
				in_data = compRec.getCooList();
			}
			if (rc == 0)
			{
				/* compute the number of countries retrieved, if > 1 then load
				 * the list and display the countries list, otherwise default
				 * using the only country */
				int num_countries = in_data.length() / 47;

				if (num_countries == 0)
				{
					String message = "No Countries found for the P/N in SAPPPC10 file."; //$NON-NLS-1$
					IGSMessageBox.showOkMB(this, null, message, null, efficientClient);	// ~33
				}
				else if (num_countries == 1)
				{
					//~15D if(num_countries == 0) condition since num_countries == 1
					this.setScannedCoo(in_data.substring(0, 2));
					setValueLabelText(COUNTRY, 0, in_data.substring(0, 2));
					
				}
				else
				{
					String cooselection = EMPTY_STRING;

					MFSCooDialog cooDialog = new MFSCooDialog(getParentFrame(),	this.fieldHeaderRec.isSISytemType());
					cooDialog.loadCooListModel(in_data);
					cooDialog.setLocationRelativeTo(this);
					cooDialog.setVisible(true);
					if (cooDialog.getProceedSelected())
					{
						cooselection = cooDialog.getSelectedListOption().substring(0, 2);
					}
					if (!cooselection.equals(EMPTY_STRING))
					{
						if (cooselection.equals("  ")) //$NON-NLS-1$
						{
							cooselection = "??"; //$NON-NLS-1$
						}
						else
						{
							this.setScannedCoo(cooselection);
						}
						setValueLabelText(COUNTRY, 0, cooselection);
					}/* cooJD selected */
				} /* end of else (list size >=  2 (one country + one for no country)) */
			} /* end of rc == 0 */
		}/* end try */
		catch (Exception e)
		{
			this.setCursor(Cursor.getDefaultCursor());
			IGSMessageBox.showOkMB(this, null, null, e);
		}
		this.toFront();
		this.tfInput.requestFocusInWindow();
	}
	/** Invoked when {@link #pbSubPart} is selected. */
	private void listSubs()
	{
		try
		{			
			int rc = 0;
			String who;
			
			boolean efficientClient = false;	// ~33
			if(MFSConfig.getInstance().containsConfigEntry("EFFICIENCYON"))//$NON-NLS-1$
			{
				efficientClient = true;
			}

			if (this.fieldHeaderRec.getNmbr().equals("FKIT")) //$NON-NLS-1$
			{
				who = "F"; //$NON-NLS-1$
			}
			else
			{
				who = "J"; //$NON-NLS-1$
			}

			/* start the RTV_SUBS transaction thread */
			MFSComponentRec compRec = (MFSComponentRec) this.fieldJList.getSelectedValue();

			/* IPSR32331MZ always pass the WHO, mctl, and crct */
			StringBuffer data = new StringBuffer(43);
			data.append("RTV_SUBS  "); //$NON-NLS-1$
			data.append(compRec.getItem());
			data.append(compRec.getPrln());
			data.append(who);
			data.append(compRec.getMctl());
			data.append(compRec.getCrct());
			
			MFSTransaction rtv_subs = new MFSFixedTransaction(data.toString());
			rtv_subs.setActionMessage("Retrieving Substitutes, Please Wait..."); //$NON-NLS-1$
			MFSComm.getInstance().execute(rtv_subs, this);
			rc = rtv_subs.getReturnCode();

			if (rc == 0)
			{
				MFSSubstPartsDialog subsJD = new MFSSubstPartsDialog(getParentFrame());
				subsJD.loadSubstPartsListModel(rtv_subs.getOutput());
				subsJD.setLocationRelativeTo(this);
				subsJD.setVisible(true);

				if (subsJD.getProceedSelected())
				{
					if (compRec.getMlri().equals("0") || compRec.getMlri().equals(" ")) //$NON-NLS-1$ //$NON-NLS-2$
					{
						compRec.setMlri("1"); //$NON-NLS-1$
					}
					if (compRec.getPnri().equals("0") || compRec.getPnri().equals(" ")) //$NON-NLS-1$ //$NON-NLS-2$
					{
						compRec.setPnri("1"); //$NON-NLS-1$
					}
					if(null == getValueLabel(PART_NUMBER, 2)) //~34
					{
						if(null == getValueLabel(PART_QUANTITY, 1)) //~34
						{
							/* shift labels 1 thru 5 down to 2 thru 6 */
							pushLabel(PART_NUMBER, 1); // ~34							
						}
						else
						{
							/* shift labels 2 thru 5 down to 3 thru 6 */
							pushLabel(PART_NUMBER, 2); // ~34
						}
					}
					setValueLabelText(PART_NUMBER, 2, subsJD.getSelectedListOption().substring(0, 12)); //~34
					/* now blank out COO because we just got a substitute */
					clearValueLabelText(COUNTRY);
				}
			}
			else
			{
				IGSMessageBox.showOkMB(this, null, rtv_subs.getErms(), null, efficientClient);	// ~33
			}
		}
		catch (Exception e)
		{
			this.setCursor(Cursor.getDefaultCursor());
			IGSMessageBox.showOkMB(this, null, null, e);
		}

		this.toFront();
		this.tfInput.requestFocusInWindow();
	}
	//~27A
	/**
	 * Displays a <code>MFSInputDialog</code> to execute the get an asset tag.
	 * @param mfgn the manufacturing control number
	 * @param pn the part number
	 * @param sn the sequence number
	 * @param mctl the work unit
	 * @param user the user requesting the tx
	 */
	private MFSIntStringPair logAsset(String mfgn, String pn, String sn, String mctl, String user) 
	{
		int rc = 0;
		String errorString = ""; //$NON-NLS-1$
		
		MFSInputDialog inputDialog = new MFSInputDialog(this.getParentFrame(), "Asset Tag", 20, null, null); //$NON-NLS-1$
		inputDialog.setVisible(true);
		
		if(inputDialog.getProceedSelected())
		{	
			IGSXMLTransaction log_asset = new IGSXMLTransaction("LOG_ASSET"); //$NON-NLS-1$
			log_asset.startDocument();
			log_asset.addElement("MFGN", this.fieldHeaderRec.getMfgn()); //$NON-NLS-1$
			log_asset.addElement("INPN", pn); //$NON-NLS-1$
			log_asset.addElement("INSQ", sn); //$NON-NLS-1$
			log_asset.addElement("MCTL", mctl); //$NON-NLS-1$
			log_asset.addElement("ATAG", inputDialog.getInputValue()); //$NON-NLS-1$
			log_asset.addElement("USER", MFSConfig.getInstance().get8CharUser()); //$NON-NLS-1$
			log_asset.endDocument();
			log_asset.run();
			
			rc = log_asset.getReturnCode();
			
			if (rc != 0)
			{
				errorString = log_asset.getErms();
			}						
		}
		else
		{
			rc = 13;
			errorString = "Asset Tag not collected.";					 //$NON-NLS-1$
		}
		
		return new MFSIntStringPair(rc, errorString);
	}
	/** Logs the scanned in part information to the <code>MFSComponentRec</code>. */
	public void logPart()
	{
		try
		{
			int cf_okay = 0;
			int coa_okay = 0;
			
			boolean efficientClient = false;	// ~33
			if(MFSConfig.getInstance().containsConfigEntry("EFFICIENCYON"))//$NON-NLS-1$
			{
				efficientClient = true;
			}
			if (verifyAllInputCollected())
			{
				MFSPartInformation record = parsePartInfo(true);

				String pn = record.pn;
				String ec = record.ec;
				String sn = record.sn;
				String ca = record.ca;
				String ms = record.ms;
				String co = record.co;
				String wu = record.wu;
				int qt = record.qt;
				boolean overrideQty = record.overrideQty;

				MFSComponentRec compRec = (MFSComponentRec) this.fieldJList.getSelectedValue();

				// If we are efficient, we will save off the country for this part to our local hash
				if (efficientClient)
				{
					// if the part is not in the hash, add the part/coo to the hash
					if (!pn.trim().equals(EMPTY_STRING) && 
						!co.trim().equals(EMPTY_STRING)) 
					{		
						saveCooInHash(pn, co);
					}
				}				
				if (compRec.getCsni().equals("C")) //$NON-NLS-1$
				{
					MFSCoaEntryDialog coaDialog = new MFSCoaEntryDialog(getParentFrame(), pn);
					coaDialog.setLocationRelativeTo(this);
					coaDialog.setVisible(true);
					
					if (coaDialog.getPressedEnter())
					{
						sn = coaDialog.getAliasSN();
					}
					else
					{
						coa_okay = 1;
					}
				}

				/* perform validation of CF report for Add, AutoLog, SrchLog, and Rework */
				{
					if (compRec.getCfrf().equals("A")) //$NON-NLS-1$
					{
						/* now pop up the CF report control number validation screen */
						MFSCFReportDialog cfDialog = new MFSCFReportDialog(
								getParentFrame(), this.fieldHeaderRec);
						cfDialog.setLocationRelativeTo(this);
						cfDialog.setVisible(true);
						if (cfDialog.getPassedValidation() == false)
						{
							cf_okay = 1;
						}
					}
				}

				/* if trying to add a part */
				if (this.fieldLogType.equals(LT_ADD)
						|| this.fieldLogType.equals(LT_AUTO_LOG)
						|| this.fieldLogType.equals(LT_SEARCH_LOG))
				{
					if (cf_okay == 0 && coa_okay == 0)
					{
						logPartAdd(overrideQty, qt, compRec, pn, sn, ec, ca, ms, wu, co, efficientClient);	// ~33

					} /* end good return code from CFReport validation */
				}/* end add part */

				/* if trying to remove a part */
				else if (this.fieldLogType.equals(LT_REMOVE)
						|| this.fieldLogType.equals(LT_SEARCH_REMOVE))
				{
					if (coa_okay == 0)
					{
						logPartRemove(overrideQty, qt, compRec, pn, sn, ec, ca, wu, efficientClient);	// ~33
					}
				}/* end-if for remove part */

				/* if trying to rework a part */
				else if (this.fieldLogType.equals(LT_REWORK))
				{
					if (cf_okay == 0 && coa_okay == 0)
					{
						logPartRework(compRec, pn, sn, ec, ca, ms, wu, co, efficientClient);	// ~33

					} /* end not CF_OKAY */
				}// end of rework logic

				if (cf_okay == 1 || coa_okay == 1)
				{
					this.tfInput.requestFocusInWindow();
				}
			}//end of all fields filled in
		}//end of try block
		catch (Exception e)
		{
			this.setCursor(Cursor.getDefaultCursor());
			IGSMessageBox.showOkMB(this, null, null, e);

			this.toFront();
			this.tfInput.setText(EMPTY_STRING);
			this.tfInput.requestFocusInWindow();
		}
	}
	
	/**
	 * ~40
	 * Method for invoke APPLYUNIT transaction
	 * 
	 * @param custMFGN
	 * @param baMFGN
	 * @return result of APPLYUNIT transaction
	 */
	private int  applyUnitTrx(MFSComponentRec compRec, String custMFGN, String custIDSS,		/*~42C*/ 
								String baMFGN, boolean efficientClient) {
		int rc = 0;
		try {
			String user = (String)MFSConfig.getInstance().getConfigValue(MFSConfig.USER);
			String cell = MFSConfig.getInstance().get8CharCell();        //~41
			String ctype = MFSConfig.getInstance().get8CharCellType();   //~41
			
			IGSXMLTransaction applyUnit = new IGSXMLTransaction("APPLYUNIT");
			applyUnit.startDocument();
			applyUnit.addElement("MFGN", custMFGN); 
			applyUnit.addElement("IDSS", custIDSS);
			applyUnit.addElement("BAMFGN", baMFGN);
			applyUnit.addElement("BAIDSS", "IPRC");
			applyUnit.addElement("USER", user);
			applyUnit.addElement("CELL", cell);							//~41
			applyUnit.addElement("CTYP", ctype);						//~41
			applyUnit.endDocument();
			applyUnit.run();
			
			rc = applyUnit.getReturnCode();
			if (rc != 0)
			{
				rc = MFSLogPartDialog.BA_FAILURE;
				throw new IGSTransactionException(applyUnit, false);
			}
			else														/*~42C*/
			{
				compRec.setCwun(applyUnit.getNextElement("BARACKWU"));
				compRec.setAmsi("1");									/*~43A*/
			}
		} catch (Exception e) {
			this.setCursor(Cursor.getDefaultCursor());
			IGSMessageBox.showOkMB(this, null, null, e, efficientClient);
			this.toFront();
			this.tfInput.setText(EMPTY_STRING);
			this.tfInput.requestFocusInWindow();
		}
		return rc;
	}

	/**
	 * ~40
	 * Method for input IPGR MGN Value
	 * 
	 * @param custMFGN
	 * @param efficientClient
	 */
	private int  installBuildAhead(MFSComponentRec compRec, String custMFGN, String custIDSS,		/*~42C*/
			  						boolean efficientClient) throws IGSException {						

		String baMFGN = null;
		int rc = MFSLogPartDialog.BA_FAILURE; //invalid return code ~41
		
		try {
			MFSGetOneValueDialog gvd = new MFSGetOneValueDialog(this.getParentFrame(),
					"Enter IPR MFGN", "IPR MFGN", "Apply",'o');

			gvd.setLocationRelativeTo(getParentFrame());
			gvd.setVisible(true);

			baMFGN = gvd.getInputValue();

			if (baMFGN != null) {
				rc = applyUnitTrx(compRec, custMFGN, custIDSS, baMFGN, efficientClient);			/*~42C*/
			}
		} catch (MissingResourceException me) {
			throw new IGSException("Error open file properties");
		}
		return rc;
	}


	/**
	 * Helper method called by {@link #logPart()} when adding a part.
	 * @param overrideQty the override quantity flag
	 * @param qt the quantity
	 * @param compRec the <code>MFSComponentRec</code>
	 * @param pn the part number
	 * @param sn the sequence number
	 * @param ec the EC number
	 * @param ca the card assembly
	 * @param ms the machine serial
	 * @param wu the work unit control number
	 * @param co the country
	 * @param efficientClient <code>true</code> if running efficient client
	 */
	private void logPartAdd(boolean overrideQty, int qt, MFSComponentRec compRec,
							String pn, String sn, String ec, String ca, String ms,
							String wu, String co, boolean efficientClient)
	{
		int rc = 0;
		String errorString = EMPTY_STRING;	
		try
		{
			MFSConfig config = MFSConfig.getInstance(); //~33 					
			
			this.fieldDirWork.autoLoggedFlag = false;  //~33
			/* ~8 */
			if (!compRec.getItem().equals(pn) && !compRec.isPnriDoNotCollect())
			{
				compRec.setBufferStatus("0"); //$NON-NLS-1$
			}			
			int remQty = Integer.parseInt(compRec.getQnty()) - Integer.parseInt(compRec.getFqty());

			if (config.containsConfigEntry("PARTIALQUANTITY") == false && !overrideQty) //$NON-NLS-1$
			{
				qt = remQty;
			}
			if (remQty < qt && !compRec.getQnty().equals("00000")) //$NON-NLS-1$
			{
				rc = 10;
				errorString = "Invalid Part Quantity"; //$NON-NLS-1$
			}
			else if (!compRec.isPnriDoNotCollect()
					&& !compRec.getInpn().equals("            ") //$NON-NLS-1$
					&& !compRec.getInpn().equals(pn))
			{
				rc = 10;
				errorString = "Cannot substitute P/N for partial qty."; //$NON-NLS-1$
			}
			/* ~6 add B and C check */
			else if ((compRec.getFsub().equals("Y") //$NON-NLS-1$ 
						|| compRec.getFsub().equals("B") //$NON-NLS-1$ 
						|| compRec.getFsub().equals("C")) //$NON-NLS-1$
					&& compRec.getItem().equals(pn))
			{
				rc = 10;
				errorString = "You must install a substitute for this part"; //$NON-NLS-1$
			}
			else if ((compRec.getFsub().equals("M") //$NON-NLS-1$ 
						|| compRec.getFsub().equals("B")) //$NON-NLS-1$
					&& this.fieldDirWork.processorMatch(pn, compRec.getItem()).length() != 0)
			{
				rc = 10;
				errorString = this.fieldDirWork.processorMatch(pn, compRec.getItem());
			}
			else if (this.fieldHeaderRec.isSISytemType() && !compRec.getItem().equals(pn))
			{
				rc = 10;
				errorString = "You cannot install a substitute part for SI orders"; //$NON-NLS-1$
			}
			/* ~6 we need to check to make sure our processors match */
			else if (compRec.getFsub().equals("C") || compRec.getFsub().equals("R")) //$NON-NLS-1$ //$NON-NLS-2$
			{
				errorString = this.fieldDirWork.processorMatchMPARule(pn, compRec.getItem());
				if (errorString.trim().length() > 0)
				{
					rc = 10;
				}
			}
            if(rc == 0) //~33 Reorganized check Duplicates (Part/Serial, Control Number, Machine Serial Number */
            {
				MFSComponentRec tempCmp;
				int index = 0;
						
				while ((rc == 0) && (index < this.fieldCompListModel.size()))
				{
					tempCmp = this.fieldCompListModel.getComponentRecAt(index);
					if(tempCmp.getIdsp().equals("I") || tempCmp.getIdsp().equals("R")) //$NON-NLS-1$ //$NON-NLS-2$
					{
						if (!compRec.isCsniDoNotCollect()) 
						{						
							if (tempCmp.getInpn().equals(pn) && tempCmp.getInsq().equals(sn)) 
							{
								rc = 10;
								errorString = "Duplicate Part/Serial found."; //$NON-NLS-1$
							}
						} // end good S/N check
						if (!compRec.isPariDoNotCollect())
						{
							if (tempCmp.getCwun().equals(wu))
							{
								rc = 10;
								errorString = "Duplicate Control Number found."; //$NON-NLS-1$
							}						
						} // end good S/N and Child Work Unit check
						if (!compRec.isCmtiDoNotCollect())
						{   // if it isn't the same component and it
							// collects MS compare the MCSN values
							if (!tempCmp.getCrct().equals(compRec.getCrct())
							&&  !tempCmp.isCmtiDoNotCollect()
							&&  tempCmp.getMatp().equals(compRec.getMatp())
							&&  tempCmp.getMcsn().equals(ms.substring(2)))
							{
								rc = 10;
								errorString = "Duplicate Machine Serial Number found."; //$NON-NLS-1$
							}						
						}
					}
					index++;
				}				
            }
            if(rc == 0) //~33 Reorganized, Separated from the Duplicate Checks
            {
				/* 10-22-2003 add TPOK = 2 logic we want to call vrfy_part if tpok = 2 */
				if ((!compRec.isEcriDoNotCollect())
				||  (!compRec.isCsniDoNotCollect())
				||  (!compRec.isCcaiDoNotCollect())
				||  (!compRec.isCmtiDoNotCollect())
				||  (!compRec.isPariDoNotCollect())
				||  (!compRec.isPnriDoNotCollect() && !compRec.getItem().equals(pn)) 
				||  (compRec.getTpok().equals("2")) //$NON-NLS-1$
				||  (this.fieldHeaderRec.getNmbr().equals("FKIT"))) //$NON-NLS-1$
				{
					// only run vrfy_part if good rc and not an SI typz
					if (!this.fieldHeaderRec.isSISytemType())
					{
						/* set Cursor to waiting and display comm msg */
						this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

						if (this.fieldHeaderRec.getNmbr().equals("FKIT"))//$NON-NLS-1$
						{
							//call FKADD_RTR instead of VRFY_PART
							StringBuffer data = new StringBuffer(150);
							data.append("FKADD_RTR "); //$NON-NLS-1$
							data.append(this.fieldHeaderRec.getMfgn());
							data.append(this.fieldHeaderRec.getIdss());
							data.append(compRec.getMctl());
							data.append(config.get8CharCell());
							data.append(config.get8CharCellType());
							data.append(config.get8CharUser());
							data.append(this.fieldHeaderRec.getPrln());
							data.append(this.fieldHeaderRec.getNmbr());
							data.append(this.fieldHeaderRec.getTypz());
							data.append(compRec.getItem());
							data.append(compRec.getCrct());
							data.append(compRec.getPnri());
							data.append(pn);
							data.append(compRec.getCsni());
							data.append(sn);
							data.append(compRec.getEcri());
							data.append(ec);
							data.append(compRec.getCcai());
							data.append(ca);
							data.append(compRec.getPari());
							data.append(wu);
							data.append(" ");//$NON-NLS-1$
							data.append(compRec.getQnty());

							MFSTransaction fkadd_rtr = new MFSFixedTransaction(data.toString());
							fkadd_rtr.setActionMessage("Adding Part to Kit, Please Wait..."); //$NON-NLS-1$
							MFSComm.getInstance().execute(fkadd_rtr, this);

							rc = fkadd_rtr.getReturnCode();
							errorString = fkadd_rtr.getOutput();
						}
						else if (compRec.getBufferStatus().equals("1")) //$NON-NLS-1$
						{   //end of bufferStatus check
							compRec.setBufferStatus("2"); //$NON-NLS-1$
						}
						else if(!compRec.getCsni().equals("G")) //~35A the verification was done previously //$NON-NLS-1$
						{	//~12C Moved VRFY_PART call to new method
							MFSIntStringPair pair = verifyPart(null, compRec, pn, sn, ca, ec, ms, wu, co, efficientClient);	// ~33
							rc = pair.fieldInt;
							errorString = pair.fieldString;
						}// end of vrfy_part
					}//end of good rc and typz not a,b,c, or d
				}// end of vrfy_part call required
				/* Begin ~9A Add PX10 Logic checking for parts out of vrfy_part logic */
				else if (!compRec.getMlri().equals(" ") //$NON-NLS-1$
						&& !compRec.getMlri().equals("0")) //$NON-NLS-1$
				{
					if (compRec.getBufferStatus().equals("1")) //$NON-NLS-1$
					{
						compRec.setBufferStatus("2"); //$NON-NLS-1$
					}
					else if (!compRec.getCrct().equals("MRGE")) //$NON-NLS-1$
					{
						/* VRFY_PART should handle PX10 logic for substitute
						 * parts so its safe to use Item here, instead of Inpn */
						// ~33 - do not need to call if the scanned in part number is
						// the same as the called for part number and efficiecy is on.
						boolean needToCallVrfyPX10 = true;	
						if (efficientClient && compRec.getItem().equals(pn) && pn.trim().length()!=0)
						{
							needToCallVrfyPX10 = false;
						}
						if(needToCallVrfyPX10) //~33
						{
							rc = vrfyPX10Logic(compRec.getMctl(), compRec.getCrct(), compRec.getItem(), efficientClient);	// ~33
						}
					}
				}
				/* End ~9A */
            }
			/* ~4 */
			if (rc == 0)  /* 33 Converted the if's to if else's for the PNRI comparing  and Reorganized Data Collection */
			{  
				// ~41 installBuildAhead before to perform any logic
				if(compRec.getPnri().equals("B"))
				{
					rc = installBuildAhead(compRec, this.fieldHeaderRec.getMfgn(),this.fieldHeaderRec.getIdss(), efficientClient);
				}
				else if(compRec.getPnri().equals("W")) //$NON-NLS-1$
				{
					rc = prcsWWNN(compRec, "COLLECT "); //$NON-NLS-1$
				}				
				/* ~27A Asset tag collection/verification */
				else if ((compRec.getPnri().equals("X") || compRec.getPnri().equals("N"))) //$NON-NLS-1$ //$NON-NLS-2$
				{
					MFSIntStringPair pair = logAsset(this.fieldHeaderRec.getMfgn(), pn, sn, compRec.getMctl(), 
							config.get8CharUser());
					rc = pair.fieldInt;
					errorString = pair.fieldString;
				}
				/* ~24A */
				else if (compRec.getPnri().equals("Z")) //$NON-NLS-1$
				{
					/* ~25A,~26A */
					if (this.fieldHeaderRec.getSapo().trim().equals("ISS")) //$NON-NLS-1$
					{
						IGSXMLTransaction rtv_seqn = new IGSXMLTransaction("RTV_SEQN"); //$NON-NLS-1$
						rtv_seqn.startDocument();
						rtv_seqn.addElement("MFGN", this.fieldHeaderRec.getMfgn()); //$NON-NLS-1$
						rtv_seqn.addElement("IDSS", this.fieldHeaderRec.getIdss()); //$NON-NLS-1$
						rtv_seqn.addElement("MCTL", compRec.getMctl()); //$NON-NLS-1$
						rtv_seqn.endDocument();
						rtv_seqn.run();
						if (rtv_seqn.getReturnCode() != 0)
						{
							throw new IGSTransactionException(rtv_seqn, false);
						}
						String smartSerial = rtv_seqn.getNextElement("SEQN"); //$NON-NLS-1$
						if (smartSerial == null)
						{
							rc = 12;
							errorString = "No Smart Serial number returned for ISS order."; //$NON-NLS-1$
						}
						else
						{
							int qty = 1;
							MFSPrintingMethods.smartSerial(smartSerial, qty, getParentFrame());
						}
					} /* end of ~25A */
					if (rc == 0)	/* ~25C */
					{
						rc = validateSEQN(compRec, this.fieldHeaderRec.getMfgn(),this.fieldHeaderRec.getIdss());
					}
				}
				if((rc == 0) && (this.fieldHeaderRec.getDataCollection() != null) && (fieldHeaderRec.isCollectRequired()))	//~29A ~31C
				{																	//~29A
					if(fieldHeaderRec.getDataCollection().containsComponentCollection(compRec.getMctl(),
							                                                          compRec.getCrct(),
							                                                          MFSPartDataCollectDialog.COLL_COLLECT))//~29A ,~30C
					{																//~29A
						MFSPartDataCollectDialog collectDialog;						//~29A
						collectDialog = new MFSPartDataCollectDialog(this.getParentFrame(), 
								                                     this.fieldHeaderRec, 
								                                     MFSPartDataCollectDialog.COLL_COLLECT, 
								                                     compRec.getCrct(),
								                                     compRec.getMctl()); //~29A					
						rc = collectDialog.getFieldReturnCode();					//~29A
					}																//~29A						
				}																	//~29A
			}
			if (rc == 0)
			{
				compRec.setRec_changed(true);

				int fqty = Integer.parseInt(compRec.getFqty()) + qt;
				int qnty = Integer.parseInt(compRec.getQnty());

				if (qnty == 0)
				{
					fqty = 0;
				}
				if (fqty == qnty)
				{
					compRec.setIdsp("I"); //$NON-NLS-1$
					compRec.setShtp(" "); //$NON-NLS-1$
				}
				String Fqty = "00000" + fqty; //$NON-NLS-1$
				compRec.setFqty(Fqty.substring(Fqty.length() - 5));

				if (!compRec.isEcriDoNotCollect())
				{
					compRec.setInec(ec);
				}
				if (!compRec.isPnriDoNotCollect())
				{
					compRec.setInpn(pn);
				}
				else
				{
					compRec.setInpn(compRec.getItem());
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
				if (!compRec.isCmtiDoNotCollect())
				{
					if (compRec.getMcsn().substring(0, 1).equals("$") //$NON-NLS-1$
							|| compRec.getCmti().toUpperCase().equals("M")) //$NON-NLS-1$
					{
						compRec.setMspi(ms.substring(0, 2));
						compRec.setMcsn(ms.substring(2));
					}
				}
				if (compRec.getCooc().equals("  ") //$NON-NLS-1$
						&& !compRec.isCooiDoNotCollect())
				{
					compRec.setCooc(co);
				}
				compRec.setCntr(this.fieldHeaderRec.getCntr());

				compRec.updtDisplayString();
				compRec.updtIRDisplayString();
				compRec.updtInstalledParts();
				
				// print out the tracksub3 label, before we increment
				// through to the next part to add
				// this will make sure we are printing the label for the
				// correct part
				String cnfgData = "TRACKSUB3," + this.fieldHeaderRec.getNmbr(); //$NON-NLS-1$

				String value = config.getConfigValue(cnfgData);
				if (value.equals(MFSConfig.NOT_FOUND))
				{
					cnfgData = "TRACKSUB3,*ALL"; //$NON-NLS-1$
					value = config.getConfigValue(cnfgData);
				}
				/* we have a config file entry to print the tracksub3 label */
				if (!value.equals(MFSConfig.NOT_FOUND))
				{
					int qty = 1;
					if (!value.equals(EMPTY_STRING))
					{
						qty = Integer.parseInt(value);
					}
					/* if the part has been exploded, we need to print the label */
					if ((compRec.getExpi().equals("1")) //$NON-NLS-1$
					&&  (compRec.getFqty().equals(compRec.getQnty())))
					{
						MFSPrintingMethods.tracksub3(this.fieldHeaderRec.getMctl(),
													compRec.getCrct(), 
													compRec.getInpn(), 
													compRec.getCooc(), 
													qty, 
													getParentFrame());
					}
				}/* end of config file entry for tracksub3 found */
				tcodTrx(compRec, efficientClient);	// ~33
			}			
			if(rc == 0)
			{
				boolean found = false;
				boolean foundNPI = false;
				int row = this.fieldActiveRow;
				
				// update instruction for this row - update to partial
				// completion or total completion
				
				handleInstruction(getRowVectorElementAt(row), efficientClient);	// ~33

				//now loop thru the current row to see what we've got left in this part list
				//if none found skip down to the next row and check that row for more parts to be added
				//if we run into a non-part instruction we will prompt the builder to complete it if we
				//are in autolog. Otherwise we will stop right there.
				JList tempList = getRowVectorElementAt(row).getPNList();
				MFSComponentListModel tempLm = (MFSComponentListModel) tempList.getModel();
				MFSComponentRec next;
				
				int index = tempList.getSelectedIndex();
				int activeIndex = index;
				
				MFSComponentSearchCriteria.MFSAutoLogSearchCriteria autoLogSearchCriteria = 
					new MFSComponentSearchCriteria.MFSAutoLogSearchCriteria();  //~33


				while (index < tempLm.size() && !found)
				{
					next = tempLm.getComponentRecAt(index);
					if (autoLogSearchCriteria.match(next))  //~33
					{
						found = true;
					}
					else
					{
						index++;
					}
				}
				//start at top of blue teal
				if (!found && (this.fieldLogType.equals(LT_ADD) 
						|| this.fieldLogType.equals(LT_SEARCH_LOG)))
				{
					index = 0;
					while (index < activeIndex && !found)
					{											
						next = tempLm.getComponentRecAt(index);
						if (autoLogSearchCriteria.match(next))  //~33
						{
							found = true;
						}
						else
						{
							index++;
						}
					}
				}
				//start at first row after active and move along to end of rows
				if (!found) // search entire list
				{
					index = 0;
					row++;
					while (row < this.fieldRowVector.size() && !found && !foundNPI)
					{
						if (!getRowVectorElementAt(row).getIsNonPartInstruction())
						{
							index = 0;
							tempList = getRowVectorElementAt(row).getPNList();
							tempLm = (MFSComponentListModel) tempList.getModel();

							while (index < tempLm.size() && !found)
							{
								next = tempLm.getComponentRecAt(index);

								if (autoLogSearchCriteria.match(next)) //~33
								{
									found = true;
								}
								else
								{
									index++;
								}
							}//end of while loop

							//if this row has no more parts to act on, then
							// we will handle the instruction if we are autolog.
							if (!found && this.fieldLogType.equals(LT_AUTO_LOG) || this.fieldLogType.equals(LT_SEARCH_LOG))
							{
								// update instruction for this row - update
								// to partial completion or total completion
								handleInstruction(getRowVectorElementAt(row), efficientClient);	// ~33
							}
						}//end of if non-part instructions

						// we are progressing to a non-part row. If we are
						// autolog, we will prompt user to fly thru it
						// otherwise we will stop here
						else
						{
							if (this.fieldLogType.equals(LT_AUTO_LOG) || this.fieldLogType.equals(LT_SEARCH_LOG))
							{
								// update instruction for this row - update
								// to partial completion or total completion
								handleInstruction(getRowVectorElementAt(row), efficientClient);	// ~33
							}
							else if (this.fieldLogType.equals(LT_ADD))
							{
								if (!getRowVectorElementAt(row).getInstructionRec().getCompletionStatus().equals("C")) //$NON-NLS-1$
								{
									foundNPI = true;
								}
							}
						}
						if (!found && !foundNPI)
						{
							row++;
						}
					}//end of while loop
				}//end of !found

				if (!found && !foundNPI) // search entire list
				{
					index = 0;
					row = 0;
					while (row < this.fieldActiveRow && !found && !foundNPI)
					{
						if (!getRowVectorElementAt(row).getIsNonPartInstruction())
						{
							index = 0;
							tempList = getRowVectorElementAt(row).getPNList();
							tempLm = (MFSComponentListModel) tempList.getModel();

							while (index < tempLm.size() && !found)
							{
								next = tempLm.getComponentRecAt(index);
								if (autoLogSearchCriteria.match(next)) //~33
								{
									found = true;
								}
								else
								{
									index++;
								}
							}
							//if this row has no more parts to act on, then
							//we will handle the instruction if we are autolog.
							if (!found && this.fieldLogType.equals(LT_AUTO_LOG)|| this.fieldLogType.equals(LT_SEARCH_LOG))
							{
								// update instruction for this row - update
								// to partial completion or total completion
								handleInstruction(getRowVectorElementAt(row), efficientClient);	// ~33
							}
						}//end of if non-part

						// we are progressing to a non-part row. If we are
						// autolog, we will prompt user to fly thru it -
						// otherwise we will stop here
						else
						{
							if (this.fieldLogType.equals(LT_AUTO_LOG)|| this.fieldLogType.equals(LT_SEARCH_LOG))
							{
								// update instruction for this row - update
								// to partial completion or total completion
								handleInstruction(getRowVectorElementAt(row), efficientClient);	// ~33
							}
							else if (this.fieldLogType.equals(LT_ADD))
							{
								if (!getRowVectorElementAt(row).getInstructionRec().getCompletionStatus().equals("C")) //$NON-NLS-1$
								{
									foundNPI = true;
								}
							}
						}
						if (!found && !foundNPI)
						{
							row++;
						}
					}//end of while loop
				}//end of !found
				if (foundNPI)
				{
					this.fieldJList.clearSelection();
					this.fieldDirWork.setBlueRow(row);
					getRowVectorElementAt(row).changeColor(
							MFSPartInstructionJPanel.BLUE_ROW_COLOR);
					dispose();
				}
				else if (found)
				{
					this.fieldActiveRow = (row);
					this.fieldJList.clearSelection();

					//move the thing, but make sure the index we are
					// interested in is visible too
					MFSPartInstructionJPanel pip = getRowVectorElementAt(row);
					this.fieldDirWork.scrollToPip(pip);

					tempList.setSelectedIndex(index);
					tempList.ensureIndexIsVisible(index);
					this.fieldJList = (tempList);
					initDisplay();
					compRec = (MFSComponentRec) tempList.getSelectedValue();

					boolean partial = false;
					if (!compRec.getIdsp().equals("I")) //$NON-NLS-1$
					{
						partial = true;
					}
					if (this.fieldLogType.equals(LT_ADD) && !partial)
					{
						dispose();
					}
					else if (!compRec.getCsni().equals(" ") //$NON-NLS-1$
							&& !compRec.getCsni().equals("0") //$NON-NLS-1$
							&& !compRec.getQnty().equals("00000") //$NON-NLS-1$
							&& !compRec.getQnty().equals("00001")) //$NON-NLS-1$
					{
						/* display error to user */
						dispose();
						rc = 10;
						errorString = "Cannot Collect S/N on Part with Qty > 1."; //$NON-NLS-1$
					}
				}
				else
				{
					this.fieldDirWork.autoLoggedFlag = true; //~33
					dispose();						
				}
			}/* end valid quantity */
			if (rc == 100 || rc == 200)
			{
				dispose();											 //~22C
				this.fieldDirWork.showPNSNErrorList(errorString, 0); //~22C
				rc = 0;												 //~22C
			}
			/* ~41 Error handling for BA*/
			else if(rc == MFSLogPartDialog.BA_FAILURE) //If something comes bad in installBuildAhead, return to direct work screen
			{
				dispose();
			} /*end of ~41*/
			else if(rc != 0)
			{
				
				if ((rc != MFSMacAddressUpdater.RC_ERR_NO_MSG_NEEDED)) /* ~9C */						
				{
					IGSMessageBox.showOkMB(this, null, errorString, null, efficientClient);	// ~33
				}
				this.toFront();
				this.tfInput.setText(EMPTY_STRING);
				this.tfInput.requestFocusInWindow();
			}
			/* ~41 install build ahead is performed before */
//			else if (rc == 0) // ~40
//			{
//				if (compRec.getPnri().equals("B")) 
//				{
//					installBuildAhead(this.fieldHeaderRec.getMfgn(),this.fieldHeaderRec.getIdss(), efficientClient);
//				}
//			}
			/* set cursor back to normal */
			this.setCursor(Cursor.getDefaultCursor());			
		}
		catch (Exception e)
		{
			/* set cursor back to normal */			
			this.setCursor(Cursor.getDefaultCursor());
			IGSMessageBox.showOkMB(this, null, null, e, efficientClient);	// ~33
			this.toFront();
			this.tfInput.setText(EMPTY_STRING);
			this.tfInput.requestFocusInWindow();
		}
	}

	/**
	 * Helper method called by {@link #logPart()} when removing a part.
	 * @param overrideQty the override quantity flag
	 * @param qt the quantity
	 * @param compRec the <code>MFSComponentRec</code>
	 * @param pn the part number
	 * @param sn the sequence number
	 * @param ec the EC number
	 * @param ca the card assembly
	 * @param wu the work unit control number
	 * @param efficientClient <code>true</code> if running efficient client
	 */
	private void logPartRemove(boolean overrideQty, int qt, MFSComponentRec compRec,
								String pn, String sn, String ec, String ca, String wu, boolean efficientClient)
	{
		try
		{
			MFSIntStringPair rcPair = checkPlug(compRec,this);  //~17A
			
			// rc=50,plugs exhausted, ncm print sheet,
			// rc=100, warning on last plug
			if ((rcPair.fieldInt == 50) || (rcPair.fieldInt == 100))
			{
				IGSMessageBox.showOkMB(this, null, rcPair.fieldString, null, efficientClient);	// ~33

				this.toFront();
				this.tfInput.setText(EMPTY_STRING);
				this.tfInput.requestFocusInWindow();
				rcPair.fieldInt = 0;
			}
			/* ~5 */
			if ((rcPair.fieldInt == 0) && (compRec.getPnri().equals("W"))) //$NON-NLS-1$
			{
				rcPair.fieldInt = prcsWWNN(compRec, "REMOVE  "); //$NON-NLS-1$
			}
			if (rcPair.fieldInt == 0) /* added for check_plug trx 18841JM */
			{
				rcPair = validateComponentRec(qt, compRec, pn, sn, ec, ca, wu);//~17A

				if (rcPair.fieldInt == 0)
				{
					rcPair = updateMCode(compRec,pn,sn,this);//~17A
				}
				if (rcPair.fieldInt == 0)
				{
					rcPair = fkitRemoveRTR(compRec,this.fieldHeaderRec.getNmbr(), 
							this);//~17A
				}
				//~32C Move MfsPartDataCollectDialog function
				if((rcPair.fieldInt == 0) && (this.fieldHeaderRec.getDataCollection() != null) && (fieldHeaderRec.isCollectRequired()))	//~29A ~31C
				{																	//~29A
					if(fieldHeaderRec.getDataCollection().containsComponentCollection(compRec.getMctl(),compRec.getCrct(),MFSPartDataCollectDialog.COLL_REMOVE))//~29A, ~30A
					{																//~29A
						MFSPartDataCollectDialog collectDialog;						//~29A
						collectDialog = new MFSPartDataCollectDialog(this.getParentFrame(), this.fieldHeaderRec, MFSPartDataCollectDialog.COLL_REMOVE, compRec.getCrct(),compRec.getMctl()); //~29A					
						rcPair.fieldInt = collectDialog.getFieldReturnCode();		//~29A
					}																//~29A						
				}																	//~29A				
				if (rcPair.fieldInt == 0)
				{
					updateComponentRecRemoved(overrideQty,qt, 
							compRec, this.fieldHeaderRec.getNmbr());
									
					// update instruction for this row - update to partial
					// completion or total completion
					handleInstruction(getRowVectorElementAt(this.fieldActiveRow), efficientClient);	// ~33

					//~17A
					MFSComponentSearch componentFinder = new MFSComponentSearch(
							this.fieldRowVector,
							new MFSComponentSearchCriteria.
							MFSPartToRemoveSearchCriteria(this.fieldHeaderRec));

					JList tempList = 
						getRowVectorElementAt(this.fieldActiveRow).getPNList();
					
					componentFinder.setSearchStartPosition(this.fieldActiveRow,
							tempList.getSelectedIndex());
					
					//now loop thru the current row to see what we've got left in this part list
					//if none found skip down to the next row adn check that row for more parts to be added
					//if we run into a non-part instruction we will prompt the builder to complete it if we
					//are in autolog. Otherwise we will stop right there.
					if(componentFinder.findComponent())
					{
						this.fieldActiveRow = componentFinder.getRow();
						this.fieldJList.clearSelection();
						//move the thing, but make sure the index we are 
						//interested in is visible too
						MFSPartInstructionJPanel pip = 
							getRowVectorElementAt(this.fieldActiveRow);
						this.fieldDirWork.scrollToPip(pip);

						tempList = 
							getRowVectorElementAt(this.fieldActiveRow).getPNList();
						tempList.setSelectedIndex(componentFinder.getIndex());
						tempList.ensureIndexIsVisible(componentFinder.getIndex());
						this.fieldJList = (tempList);
						initDisplay();
					}
					else
					{
						dispose();
					}
				}
			} /* end-if rc ==0 for chck_plug trx */
			if (rcPair.fieldInt != 0)
			{
				if (rcPair.fieldInt == 100 || rcPair.fieldInt == 200)
				{
					this.setCursor(Cursor.getDefaultCursor());					//~22C
					this.fieldDirWork.showPNSNErrorList(rcPair.fieldString, 0);	//~22C
					rcPair.fieldInt = 0;										//~22C
				}
				else if (rcPair.fieldInt != MFSMacAddressUpdater.RC_ERR_NO_MSG_NEEDED) /* ~5C ~9C */
				{
					/* set cursor back to normal */
					this.setCursor(Cursor.getDefaultCursor());
					
					IGSMessageBox.showOkMB(this, null, rcPair.fieldString, null, efficientClient);	// ~33
				}
				this.toFront();
				this.tfInput.setText(EMPTY_STRING);
				this.tfInput.requestFocusInWindow();
			}
		}
		catch (Exception e)
		{
			this.setCursor(Cursor.getDefaultCursor());
			IGSMessageBox.showOkMB(this, null, null, e, efficientClient);	// ~33
			this.toFront();
			this.tfInput.setText(EMPTY_STRING);
			this.tfInput.requestFocusInWindow();
		}
	}
	
    /**
	 * Helper method called by {@link #logPart()} when reworking a part
	 * @param compRec the <code>MFSComponentRec</code>
	 * @param pn the part number
	 * @param sn the sequence number
	 * @param ec the EC number
	 * @param ca the card assembly
	 * @param ms the machine serial
	 * @param wu the work unit control number
	 * @param co the country
	 * @param efficientClient <code>true</code> if running efficient client
	 */
	private void logPartRework(MFSComponentRec compRec, String pn, String sn, String ec,
								String ca, String ms, String wu, String co, boolean efficientClient)
	{
		try
		{
			int rc = 0;
			String errorString = EMPTY_STRING;	        
			
			if ((!compRec.isEcriDoNotCollect()) 
					|| (!compRec.isCsniDoNotCollect())
					|| (!compRec.isCcaiDoNotCollect())
					|| (!compRec.isCmtiDoNotCollect())
					|| (!compRec.isPariDoNotCollect())
					|| (!compRec.isPnriDoNotCollect() && !compRec.getItem().equals(pn))
					|| compRec.getTpok().equals("2") //$NON-NLS-1$
					|| this.fieldHeaderRec.getNmbr().equals("FKIT")) //$NON-NLS-1$
			{
				if (!compRec.isCsniDoNotCollect())
				{
					MFSComponentRec cmp;
					int index = 0;
					boolean found = false;
					while (index < this.fieldCompListModel.size() && !found)
					{
						cmp = this.fieldCompListModel.getComponentRecAt(index);
						if (cmp.getInpn().equals(pn) && cmp.getInsq().equals(sn)
								&& (cmp.getIdsp().equals("I") //$NON-NLS-1$ 
										|| cmp.getIdsp().equals("R")) //$NON-NLS-1$
								&& !cmp.getCrct().equals(compRec.getCrct()))
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
						MFSComponentRec cmp;
						int index = 0;
						boolean found = false;
						while (index < this.fieldCompListModel.size() && !found)
						{
							cmp = this.fieldCompListModel.getComponentRecAt(index);
							if (cmp.getCwun().equals(wu) && (cmp.getIdsp().equals("I") //$NON-NLS-1$ 
										|| cmp.getIdsp().equals("R")) //$NON-NLS-1$
									&& !cmp.getCrct().equals(compRec.getCrct()))
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
						MFSComponentRec cmp;
						int index = 0;
						boolean found = false;
						while (index < this.fieldCompListModel.size() && !found)
						{
							cmp = this.fieldCompListModel.getComponentRecAt(index);
							// if it isn't the same component and it collects MS
							// compare the MCSN values
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

				if (rc == 0)
				{
					/* ~6 add B and C check */
					if ((compRec.getFsub().equals("Y") //$NON-NLS-1$ 
							|| compRec.getFsub().equals("B") //$NON-NLS-1$
							|| compRec.getFsub().equals("C")) //$NON-NLS-1$
						&& compRec.getItem().equals(pn))
					{
						rc = 10;
						errorString = "You must install a substitute for this part"; //$NON-NLS-1$
					}
				}
				if (rc == 0)
				{
					if ((compRec.getFsub().equals("M") //$NON-NLS-1$ 
							|| compRec.getFsub().equals("B")) //$NON-NLS-1$
							&& this.fieldDirWork.processorMatch(pn, compRec.getItem()).length() != 0)
					{
						rc = 10;
						errorString = this.fieldDirWork.processorMatch(pn, compRec.getItem());
					}
				}

				if (rc == 0)
				{
					/* set Cursor to waiting */
					this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
					//~12C Moved VRFY_PART call to new method
					MFSIntStringPair pair = verifyPart(null, compRec, pn, sn, ca, ec, ms, wu, co, efficientClient);	// ~33
					rc = pair.fieldInt;
					errorString = pair.fieldString;
				}//end of good rc
				
				
				/* ~27A Asset tag collection/verification */
				if ((rc == 0) && (compRec.getPnri().equals("X") || compRec.getPnri().equals("N"))) //$NON-NLS-1$ //$NON-NLS-2$
				{
					MFSIntStringPair pair = logAsset(this.fieldHeaderRec.getMfgn(), pn, sn, compRec.getMctl(), 
							MFSConfig.getInstance().get8CharUser());
					
					rc = pair.fieldInt;
					errorString = pair.fieldString;
				}				

			} /* end-if fields are blank */
			
			/* Begin ~9A Add PX10 Logic checking for parts out of vrfy_part logic*/
			else if (!compRec.getMlri().equals(" ") && !compRec.getMlri().equals("0")) //$NON-NLS-1$ //$NON-NLS-2$
			{
				/*
				 * VRFY_PART should handle PX10 logic for substitute parts so
				 * its safe to use Item here, instead of Inpn */
				if (!compRec.getCrct().equals("MRGE")) //$NON-NLS-1$
				{
					rc = vrfyPX10Logic(compRec.getMctl(), compRec.getCrct(), compRec.getItem(), efficientClient);	// ~33
				}
			}
			/* End ~9A */

			/* ~4 */
			if ((rc == 0) && (compRec.getPnri().equals("W"))) //$NON-NLS-1$
			{
				rc = prcsWWNN(compRec, "REPLACE "); //$NON-NLS-1$
			}
			/* ~4 */																			
			if (rc == 0) /* update component info for replaced part */
			{
				/* new trx for checking plugged parts for ipsr 18841JM 8/22/01 */
				if (!compRec.isCsniDoNotCollect())
				{
					/* build verify part info string */
					StringBuffer data = new StringBuffer(75);
					data.append("CHCK_PLUG "); //$NON-NLS-1$
					data.append(compRec.getInpn());
					data.append(compRec.getInsq());
					data.append(compRec.getMspi());
					data.append(compRec.getMcsn());
					data.append(compRec.getMctl());
					data.append(compRec.getPrln());
					data.append(compRec.getNmbr());
					data.append(MFSConfig.getInstance().get8CharUser());
					data.append(compRec.getCrct());

					MFSTransaction chck_plug = new MFSFixedTransaction(data.toString());
					chck_plug.setActionMessage("Checking Plug Limit, Please Wait..."); //$NON-NLS-1$
					MFSComm.getInstance().execute(chck_plug, this);

					rc = chck_plug.getReturnCode();
					errorString = chck_plug.getOutput();

					if (rc == 50 || rc == 100)
					{
						this.setCursor(Cursor.getDefaultCursor());
						
						/* display plug limit message and print plug limit NCM label */
						IGSMessageBox.showOkMB(this, null, errorString, null, efficientClient);	// ~33
						this.toFront();
						this.tfInput.setText(EMPTY_STRING);
						this.tfInput.requestFocusInWindow();
						rc = 0;
					}
				} /* end-if serial part check */
				/* end trx for checking plugged parts for ipsr 18841JM 8/22/01 */

				//~32 Move MfsPartDataCollectDialog after CHCK_PLUG Transaction
				if((rc == 0) && (this.fieldHeaderRec.getDataCollection() != null) && (fieldHeaderRec.isCollectRequired()))	//~29A ~31C 
				{																	//~29A
					if(fieldHeaderRec.getDataCollection().containsComponentCollection(compRec.getMctl(),compRec.getCrct(),MFSPartDataCollectDialog.COLL_COLLECT))//~29A, ~30C
					{																//~29A
						MFSPartDataCollectDialog collectDialog;						//~29A
						collectDialog = new MFSPartDataCollectDialog(this.getParentFrame(), this.fieldHeaderRec, MFSPartDataCollectDialog.COLL_COLLECT, compRec.getCrct(),compRec.getMctl()); //~29A					
						rc = collectDialog.getFieldReturnCode();					//~29A													
					}																//~29A						
				}	
				
				if (rc == 0) /* added for check_plug trx 18841JM */
				{
                	if (!compRec.getOrg_idsp().equals("A")) //$NON-NLS-1$
					{
						String BLANK27 = "                           "; //$NON-NLS-1$
						String BLANK76 = "                                                                            "; //$NON-NLS-1$
						
						/* get current date and time */
						SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-ddHH.mm.ss"); //$NON-NLS-1$
						Date currTime = new Date();
						String dat = fmt.format(currTime);

						MFSConfig config = MFSConfig.getInstance();
						StringBuffer data = new StringBuffer(384);
						data.append("UPDT_CRWC "); //$NON-NLS-1$
						data.append(config.get8CharUser());
						data.append(config.get8CharCell());
						data.append(dat);
						data.append(config.get8CharCellType());
						data.append(BLANK76);
						data.append(compRec.getMctl());
						data.append(compRec.getCrct());
						data.append(compRec.getInpn());
						data.append(compRec.getInec());
						data.append(compRec.getInsq());
						data.append(compRec.getInca());
						data.append(compRec.getAmsi());
						data.append(compRec.getMspi());
						data.append(compRec.getMcsn());
						data.append(compRec.getCwun());
						data.append("N"); //$NON-NLS-1$
						data.append(compRec.getCntr());
						data.append(compRec.getFqty());
						data.append(" "); //$NON-NLS-1$
						data.append(compRec.getShtp());
						data.append(compRec.getMlri());
						data.append(compRec.getPnri());
						data.append("J"); //$NON-NLS-1$
						data.append(co);
						data.append(BLANK27);

						if (ec.equals("*NONE       ")) //$NON-NLS-1$
						{
							ec = "            "; //$NON-NLS-1$
						}
						if (pn.equals("            ")) //$NON-NLS-1$
						{
							pn = compRec.getItem();
						}
						/* ~23A  Use the scanned in MS if an MS was scanned in. */
						final String updateMS;
						if (ms.trim().length()!=0) 
						{
							updateMS=ms;                                        		
						}
						else                                                    
						{                                                       
							updateMS = compRec.getMspi() + compRec.getMcsn();   
						}   
						data.append(compRec.getMctl());
						data.append(compRec.getCrct());
						data.append(pn);
						data.append(ec);
						data.append(sn);
						data.append(ca);
						data.append(compRec.getAmsi());
						data.append(updateMS);          //~23C				 
						data.append(wu);
						data.append("I"); //$NON-NLS-1$
						data.append(this.fieldHeaderRec.getCntr());
						data.append(compRec.getFqty());
						data.append(" "); //$NON-NLS-1$
						data.append(compRec.getShtp());
						data.append(compRec.getMlri());
						data.append(compRec.getPnri());
						data.append("J"); //$NON-NLS-1$
						data.append(co);
						data.append(BLANK27);

						MFSTransaction updt_crwc = new MFSFixedTransaction(data.toString());
						updt_crwc.setActionMessage("Updating Component, Please Wait..."); //$NON-NLS-1$
						MFSComm.getInstance().execute(updt_crwc, this);
						rc = updt_crwc.getReturnCode();
						errorString = updt_crwc.getOutput();
						
						this.setCursor(Cursor.getDefaultCursor());
					} /* end-if comp rec != 'A' */
				} /* end-if rc ==0 for chck_plug trx */
			} /* end-if update component info for replaced part */
			if (rc == 0)
			{
				compRec.setIdsp("I"); //$NON-NLS-1$
				this.fieldRwkPartLogged = true;
				compRec.setRec_changed(true);

				if (!compRec.isEcriDoNotCollect())
				{
					compRec.setInec(ec);
				}

				if (!compRec.isPnriDoNotCollect())
				{
					compRec.setInpn(pn);
				}
				else
				{
					compRec.setInpn(compRec.getItem());
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
				if (!compRec.isCooiDoNotCollect())
				{
					compRec.setCooc(co);
				}
				/*~23A Update the MS if an MS was scanned in. */ 
				if (ms.trim().length()!=0)              
				{                                               
					compRec.setMspi(ms.substring(0, 2));  
					compRec.setMcsn(ms.substring(2));     
				}                                               

				compRec.setCntr(this.fieldHeaderRec.getCntr());
				compRec.setBufferStatus("-1"); //$NON-NLS-1$
				compRec.updtDisplayString();
				compRec.updtInstalledParts();
				compRec.updtIRDisplayString();
				/* compRec.updtViewInstPartsString(); ~13 */

				// now print out the tracksub3 label if we have it configured
				String cnfgData = "TRACKSUB3," + this.fieldHeaderRec.getNmbr(); //$NON-NLS-1$

				String value = MFSConfig.getInstance().getConfigValue(cnfgData);
				if (value.equals(MFSConfig.NOT_FOUND))
				{
					cnfgData = "TRACKSUB3,*ALL"; //$NON-NLS-1$
					value = MFSConfig.getInstance().getConfigValue(cnfgData);
				}
				/* we have a config file entry to print the tracksub3 label */
				if (!value.equals(MFSConfig.NOT_FOUND))
				{
					int qty = 1;
					if (!value.equals(EMPTY_STRING))
					{
						qty = Integer.parseInt(value);
					}

					/* if the part has been exploded, we need to print the label */
					if ((compRec.getExpi().equals("1")) //$NON-NLS-1$
							&& (compRec.getFqty().equals(compRec.getQnty())))
					{
						MFSPrintingMethods.tracksub3(this.fieldHeaderRec.getMctl(),
								compRec.getCrct(), compRec.getInpn(), 
								compRec.getCooc(), qty, getParentFrame());
					}
				}/* end of config file entry for tracksub3 found */

				/* call the tcod trx */
				tcodTrx(compRec, efficientClient);	// ~33

				dispose();
			}
			if (rc != 0)
			{
				/* set cursor back to normal */
				this.setCursor(Cursor.getDefaultCursor());

				if (rc == 100 || rc == 200)
				{
					/* display error to user */
					dispose();											//~22C
					this.fieldRwkErrorDetail = true;			        //~22C
					this.fieldDirWork.showPNSNErrorList(errorString, 0);//~22C
					rc = 0;												//~22C
				}
				//special return code from updtMac ~9C
				else if (rc != MFSMacAddressUpdater.RC_ERR_NO_MSG_NEEDED)	
				{
					IGSMessageBox.showOkMB(this, null, errorString, null, efficientClient);	// ~33
				}
				/* ~41 Build ahead is not executed in rework*/
//				else if(rc == 0) // ~40
//				{
//					if (compRec.getPnri().equals("B")) 
//					{
//						installBuildAhead(this.fieldHeaderRec.getMfgn(),this.fieldHeaderRec.getIdss(), efficientClient);
//					}
//				}

				this.toFront();
				this.tfInput.setText(EMPTY_STRING);
				this.tfInput.requestFocusInWindow();
			}
		}
		catch (Exception e)
		{
			this.setCursor(Cursor.getDefaultCursor());
			IGSMessageBox.showOkMB(this, null, null, e, efficientClient);	// ~33

			this.toFront();
			this.tfInput.setText(EMPTY_STRING);
			this.tfInput.requestFocusInWindow();
		}
	} 
	
	/**
	 * Displays an <code>MFSWWNNDialog</code> to execute the PROCSSWWNN transaction.
	 * @param compRec the <code>MFSComponentRec</code>
	 * @param wwnnType the type for the <code>MFSWWNNDialog</code>
	 * @return the return code of the <code>MFSWWNNDialog</code>
	 */
	private int prcsWWNN(MFSComponentRec compRec, String wwnnType)
	{
		int rc = 0;
		try
		{
			MFSWWNNDialog wwnnDialog = new MFSWWNNDialog(getParentFrame(), compRec, wwnnType);
			wwnnDialog.initDisplay(); //~16A
			wwnnDialog.setLocationRelativeTo(this); //~16A
			wwnnDialog.setVisible(true);
			rc = wwnnDialog.getReturnCode();
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(this, null, null, e);

			this.toFront();
			this.tfInput.setText(EMPTY_STRING);
			this.tfInput.requestFocusInWindow();
		}
		return rc;
	}

	/** {@inheritDoc} */
	protected void recvInput()
	{
		try
		{
			boolean efficientClient = false;	// ~33
			if(MFSConfig.getInstance().containsConfigEntry("EFFICIENCYON"))//$NON-NLS-1$
			{
				efficientClient = true;
			}
			
			MFSComponentRec compRec = (MFSComponentRec) this.fieldJList.getSelectedValue();
			boolean found = false;
			String errorString = MFSBCBarcodeDecoder.INVALID_BARCODE_ERMS;
			int rc = 0;

			MFSBCBarcodeDecoder barcode = MFSBCBarcodeDecoder.getInstance();
			barcode.setMyBarcodeInput(this.tfInput.getText());

			if (this.fieldLogType.equals(LT_SEARCH_LOG))
			{
				//try to determine if we've already decoded a pn AND we are
				// collecting machine serial AND we are using
				// a 1S barcode to do so.
				JLabel partLabel = this.getValueLabel(PART_NUMBER, 2);
				if (!compRec.getCmti().equals(" ") //$NON-NLS-1$
						&& !compRec.getCmti().equals("0") //$NON-NLS-1$
						&& this.tfInput.getText().length() > 1
						&& this.tfInput.getText().substring(0, 2).equals("1S") //$NON-NLS-1$
						&& (partLabel != null && !partLabel.getText().equals(EMPTY_STRING)))
				{
					//Nothing to do
				}
				else
				{
					barcode.setMyBCPartObject(new MFSBCPartObject());
					MFSBCIndicatorValue bcIndVal = new MFSBCIndicatorValue();

					bcIndVal.setPNRI("1"); //$NON-NLS-1$
					bcIndVal.setECRI("0"); //$NON-NLS-1$
					bcIndVal.setCSNI("0"); //$NON-NLS-1$
					bcIndVal.setCCAI("0"); //$NON-NLS-1$
					bcIndVal.setCMTI("0"); //$NON-NLS-1$
					bcIndVal.setPRLN(this.fieldHeaderRec.getPrln());
					bcIndVal.setITEM("            "); //$NON-NLS-1$
					bcIndVal.setCOOI("0"); //$NON-NLS-1$

					barcode.setMyBCIndicatorValue(bcIndVal);
					barcode.decodeBarcodeFor(this);

					String BCPN = barcode.getBCMyPartObject().getPN();

					int scanQty = 1;
					if (!barcode.getBCMyPartObject().getQT().equals(EMPTY_STRING))
					{
						scanQty = Integer.parseInt(barcode.getBCMyPartObject().getQT());
					}
					int recQty = Integer.parseInt(compRec.getQnty())
									- Integer.parseInt(compRec.getFqty());

					MFSComponentSearch componentFinder = 
						new MFSComponentSearch(this.fieldRowVector,
							new MFSComponentSearchCriteria.MFSAutoLogSearchCriteria(BCPN));  //~33

					int row = this.fieldActiveRow;
					int index = this.fieldJList.getSelectedIndex();
					
					componentFinder.setSearchStartPosition(row, index);                      //~33
					componentFinder.setSearchNonPartInstruction(false);                      //~33
					
					// if we have a pn decoded and we do not match the currently
					// selected component, look thru the list
					// to see if we have another one that matches up
					if ((!BCPN.equals(EMPTY_STRING) && !BCPN.equals(compRec.getItem()))
							|| (BCPN.equals(compRec.getItem()) && scanQty > recQty))
					{
						if(componentFinder.findComponent())		                     //~33				
						{
							row = componentFinder.getRow();
							index = componentFinder.getIndex();
													
							JList pipPNList = getRowVectorElementAt(row).getPNList();
							pipPNList.setSelectedIndex(index);
							pipPNList.ensureIndexIsVisible(index);
							compRec = componentFinder.getMatchingComponentRec();
							this.fieldActiveRow = (row);
							this.fieldJList = pipPNList;
							this.fieldCompListModel = getRowVectorElementAt(row).getPNListModel();
							
							initDisplay();
							if (!compRec.getCsni().equals(" ") //$NON-NLS-1$
									&& !compRec.getCsni().equals("0") //$NON-NLS-1$
									&& !compRec.getQnty().equals("00000") //$NON-NLS-1$
									&& !compRec.getQnty().equals("00001")) //$NON-NLS-1$
							{
								/* display error to user */
								dispose();
								rc = 10;
								errorString = "Cannot Collect S/N on Part with Qty > 1."; //$NON-NLS-1$
							}
						}
						else
						{
							/*
							 * if we are in fkit mode, we should check to see if
							 * this part is a substitute for any of of the
							 * called out parts in our kit...
							 */
							if (this.fieldHeaderRec.getNmbr().equals("FKIT")) //$NON-NLS-1$
							{
								compRec = fkitSubsCheck(barcode.getBCMyPartObject().getPN(), scanQty);
								if (compRec == null)
								{
									rc = 10;
									errorString = "Part Number Not Found!"; //$NON-NLS-1$
									compRec = (MFSComponentRec) this.fieldJList.getSelectedValue();
								}
							}
							else
							{
								/* display error to user */
								rc = 10;
								errorString = "Part Number Not Found!"; //$NON-NLS-1$
							}
						}
					}//end of PN decoded and not matching the active record
				}//end of pn/ms/1S check
			}//end of srchlog
			else if (this.fieldLogType.equals(LT_SEARCH_REMOVE))
			{
				barcode.setMyBCPartObject(new MFSBCPartObject());
				MFSBCIndicatorValue bcIndVal = new MFSBCIndicatorValue();

				bcIndVal.setPNRI("1"); //$NON-NLS-1$
				bcIndVal.setECRI("0"); //$NON-NLS-1$
				String sni = MFSConfig.getInstance().getConfigValue("DECODESN"); //$NON-NLS-1$
				if (!sni.equals(MFSConfig.NOT_FOUND))
				{
					bcIndVal.setCSNI(sni);
				}
				else
				{
					bcIndVal.setCSNI("1"); //$NON-NLS-1$
				}
				bcIndVal.setCCAI("0"); //$NON-NLS-1$
				bcIndVal.setCMTI("0"); //$NON-NLS-1$
				bcIndVal.setPRLN(this.fieldHeaderRec.getPrln());
				bcIndVal.setITEM("            "); //$NON-NLS-1$
				bcIndVal.setCOOI("0"); //$NON-NLS-1$

				barcode.setMyBCIndicatorValue(bcIndVal);
				barcode.decodeBarcodeFor(this);

				String BCPN = barcode.getBCMyPartObject().getPN();
				String BCSN = barcode.getBCMyPartObject().getSN();

				int scanQty = 1;
				if (!barcode.getBCMyPartObject().getQT().equals(EMPTY_STRING))
				{
					scanQty = Integer.parseInt(barcode.getBCMyPartObject().getQT());
				}

				int recQty = Integer.parseInt(compRec.getFqty());

				if ((!BCPN.equals(EMPTY_STRING) && !BCPN.equals(compRec.getInpn()))
						|| (BCPN.equals(compRec.getInpn()) && scanQty > recQty)
						|| (BCPN.equals(compRec.getInpn()) && !BCSN.equals(EMPTY_STRING)
								&& !BCSN.equals(compRec.getInsq()))
						|| (BCPN.equals(compRec.getInpn()) && !BCSN.equals(EMPTY_STRING)
								&& BCSN.equals(compRec.getInsq()) && compRec.getInvs().equals("Z"))) //$NON-NLS-1$
				{
					/* ~34 Update to simplified component search logic */
					MFSComponentSearch componentFinder = 
						new MFSComponentSearch(this.fieldRowVector,
							new MFSComponentSearchCriteria.
								MFSPartToRemoveSearchLogCriteria(this.fieldHeaderRec, BCPN, BCSN, scanQty)); 

					int row = this.fieldActiveRow;
					int index = this.fieldJList.getSelectedIndex();
					
					componentFinder.setSearchStartPosition(row, index);                      
					componentFinder.setSearchNonPartInstruction(false);                      

					if(componentFinder.findComponent())		                     		
					{
						row = componentFinder.getRow();
						index = componentFinder.getIndex();
						
						JList pipPNList = getRowVectorElementAt(row).getPNList();
						pipPNList.setSelectedIndex(index);
						pipPNList.ensureIndexIsVisible(index);
						compRec = componentFinder.getMatchingComponentRec();
						this.fieldActiveRow = (row);
						this.fieldJList = pipPNList;
						this.fieldCompListModel = getRowVectorElementAt(row).getPNListModel();
												
						initDisplay();
						if (!compRec.getCsni().equals(" ") //$NON-NLS-1$
								&& !compRec.getCsni().equals("0") //$NON-NLS-1$
								&& !compRec.getQnty().equals("00000") //$NON-NLS-1$
								&& !compRec.getQnty().equals("00001")) //$NON-NLS-1$
						{
							/* display error to user */
							dispose();
							rc = 10;
							errorString = "Cannot Collect S/N on Part with Qty > 1."; //$NON-NLS-1$
						}
					}
					else
					{
						/* display error to user */
						rc = 10;
						errorString = "Part Number Not Found!"; //$NON-NLS-1$
					}
				}
			}//end of search remove
			if (rc == 0)
			{
				barcode.setMyBCPartObject(new MFSBCPartObject());
				rc = decodeBarcode(this.fieldHeaderRec, compRec, barcode);				
			}
			found = noInputToCollect(compRec, barcode.getBCMyPartObject());

			if (rc == 0 && !found)
			{
				boolean pn_change = false;
				boolean sn_change = false;
				
				/* QT */
				if (!barcode.getBCMyPartObject().getQT().equals(EMPTY_STRING)
						&& !this.fieldLogType.equals(LT_REWORK))
				{
					if (null == this.getValueLabel(PART_QUANTITY, 1))
					{
						/* shift labels 1 thru 5 down to 2 thru 6 */
						pushLabel(PART_QUANTITY, 1); // ~10
					}
					found = setValueLabelText(PART_QUANTITY, 1, barcode.getBCMyPartObject().getQT());
				}
				/* PN */
				if (!barcode.getBCMyPartObject().getPN().equals(EMPTY_STRING))
				{
					if(found = setValueLabelText(PART_NUMBER, 2, barcode.getBCMyPartObject().getPN())) //~34
					{
						pn_change = true;
					}
					/* substitute scanned in - blank out country */
					if (!barcode.getBCMyPartObject().getPN().equals(compRec.getItem()))
					{
						clearValueLabelText(COUNTRY);
					}
				}
				/* EC */
				if (!(barcode.getBCMyPartObject().getEC().equals(EMPTY_STRING)))
				{
					found |= setValueLabelText(EC_NUMBER, 3, barcode.getBCMyPartObject().getEC()); //~34 
				}
				/* SN */
				if (!(barcode.getBCMyPartObject().getSN().equals(EMPTY_STRING)))
				{
					if (validateSequenceNumber(compRec, barcode.getBCMyPartObject())) //~15C
					{		
						if(found = setValueLabelText(SEQUENCE_NUMBER, 4, barcode.getBCMyPartObject().getSN())) //~34
						{
							sn_change = true;
						}
					}
				}
				/* CA */
				if (!(barcode.getBCMyPartObject().getCA().equals(EMPTY_STRING)))
				{
					found |= setValueLabelText(CARD_ASSEMBLY, 5, barcode.getBCMyPartObject().getCA()); //~34
				}
				/* MS */
				if (!(barcode.getBCMyPartObject().getMSN().equals(EMPTY_STRING)))
				{
										
					
					// ~38A 1S Part ie. 1S2076ZZZ78FE151 (1S - 2076 - ZZZ - 78FE151)
					if (tfInput.getText().substring(0, 2).equalsIgnoreCase("1S"))
					{
						
						// Mmdl from current part is the same scanned. */
						/*
						String partMmdl = compRec.getMmdl().toString().trim();
						String scannedMmdl = barcode.getBCMyPartObject().getMD().toString().trim();
						*/
						if(!compRec.getMmdl().toString().trim().equalsIgnoreCase(barcode.getBCMyPartObject().getMD().toString().trim()))
						{
							
							// Throw an error if 1S Part and current part mmdl and mmdl scanned mismatch
							rc = 1;
							errorString = "The Part Machine Model(mmdl) does not match with scanned value.";
						}						
					}// if (tfInput.getText().substring(0, 2).equalsIgnoreCase("1S"))
					
					if(rc == 0)//~38A
					{          //~38A 
						
						JLabel msLabel = getValueLabel(MACHINE_SERIAL, 5); //~34
						
						if (null != msLabel) //~34
						{
							if (validateMachineSerial(compRec, barcode)) 
							{//~15C,~23C						
								VRFY_PI vrfyPI = new VRFY_PI(this); //~34
								vrfyPI.setInputMSN(barcode.getBCMyPartObject().getMSN().substring(0, 2));
								vrfyPI.setInputMspi(compRec.getMspi());
								vrfyPI.setInputMatp(compRec.getMatp());		
								vrfyPI.execute();
								
								rc = vrfyPI.getReturnCode();
								if (rc != 0)
								{
									errorString = vrfyPI.getErrorMessage();
								}// if (rc != 0)
								else
								{
									msLabel.setText(barcode.getBCMyPartObject().getMSN()); //~34 
									found = true; 
								}// else -> if (rc != 0)
							}// if (validateMachineSerial(compRec, barcode))
						}// if (null != msLabel)
					}// if(rc == 0) ~38A
				}// if (!(barcode.getBCMyPartObject().getMSN().equals(EMPTY_STRING)))						
				
				/* if the pari for this part is a V, then we need to call a
				 * transaction to gather the MCTL to pre-populate. */ 
				/* Added exclusion for grey parts, because they call  
				 * VRFYPNPLUS which includes RTV_WUBYPS logic. ~36 */				
				if (compRec.getPari().equals("V") && !this.isGreyPart(compRec)) //$NON-NLS-1$
				{
					// one of the pn or sn has been changed, so find out if both
					// PN and SN have been wanded
					// if so, then run the RTV_WUBYPS transaction to determine
					// if we should prepopulate the WU
					if (pn_change || sn_change)
					{
						String pn = EMPTY_STRING;
						String sn = EMPTY_STRING;
						
						JLabel labelContainer = null;
						labelContainer = getValueLabel(PART_NUMBER, 2);
						if(null != labelContainer && labelContainer.getText().length() != 0) {
							pn = labelContainer.getText();
						}
						labelContainer = getValueLabel(SEQUENCE_NUMBER, 4);
						if(null != labelContainer && labelContainer.getText().length() != 0) {
							sn = labelContainer.getText();
						}
						// if both pn and sn have been collected, run the
						// RTV_WUBYPS transaction
						if (!pn.equals(EMPTY_STRING) && !sn.equals(EMPTY_STRING))
						{
							// ~21 Change params as XML and add New LOCAL param with 0
							RTV_WUBYPS rtvWUBYPS = new RTV_WUBYPS(this); //~34							
							rtvWUBYPS.setInputINPN(pn);
							rtvWUBYPS.setInputINSQ(sn);
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
						}// both pn and sn have been collected
					}// end one or both of pn/sn have been wanded this time around
				}//	end of pari = 'V'				
				/* WU */	
				String wuCoo = null;
				if (!(barcode.getBCMyPartObject().getWU().equals(EMPTY_STRING)))
				{
					/* Added exclusion for grey parts, because they call  
					 * VRFYPNPLUS which includes RTV_SIGPN logic. ~36 */									
					if (!compRec.isCooiDoNotCollect() && !this.isGreyPart(compRec)) //~36
					{
						if (compRec.getBufferStatus().equals("1") && //$NON-NLS-1$
							!this.fieldLogType.equals(LT_REWORK) &&
							countryFilled())
						{
							//we will get this info later - buffer this call for now
						}
						else
						{
							RTV_SIGPN rtvSigPN = new RTV_SIGPN(this); //~34
							
							rtvSigPN.setInputWU(barcode.getBCMyPartObject().getWU());
							
							// Handle the new parameter when Search Log button is pressed              ~39A 
							if (this.fieldLogType.equals(LT_SEARCH_LOG) &&                           //~39A
								MFSConfig.getInstance().containsConfigEntry("ENABLESRCHLOGCOOLOGIC"))//~39A
							{                                       //~39A
								rtvSigPN.setInputSearchLogFlag("S");//~39A
							}                                       //~39A
							
							rtvSigPN.execute();
							
							rc = rtvSigPN.getReturnCode();
							if (rc == 0)
							{
								wuCoo = rtvSigPN.getOutputCOO().trim();//~39C
							}
							else
							{
								errorString = rtvSigPN.getErrorMessage();
							}
						}//free to call this based on TRXBUFFER not being configured
					}
					if (rc == 0)
					{
						found |= setValueLabelText(CONTROL_NUMBER, 6, barcode.getBCMyPartObject().getWU()); //~34						
					} // end good return code from transaction
				}
				/* Header */
				if (!barcode.getBCMyPartObject().getHDR().equals(EMPTY_STRING)) 
				{
					if(!this.singleCoo && efficientClient)
					{   /* If not Single Coo prepopulated, 
						 * we will check if Coo is valide for this header */					
						chkCooForNewHeader(barcode.getBCMyPartObject().getHDR());
					}
					this.setScannedHeader(barcode.getBCMyPartObject().getHDR());
				}
				/* Scanned CO */
				else if (!barcode.getBCMyPartObject().getScannedCO().equals(EMPTY_STRING))
				{					
					this.setScannedCoo(barcode.getBCMyPartObject().getScannedCO());
				}
				/* Add the Grey Market part check logic after doing the rest of the barcode retrievals.
				 *    Why After? Because we avoided calling RTV_WUBYPS and RTV_SIGPN. And may need
				 *    some of the scanned values to call VRFYPNPLUS.
				 *    We engage VRFYPNPLUS only if a Part Number/Serial Number or MCTL
				 *    values are collected. 
				 *  ~36 */
				if(this.isGreyPart(compRec))
				{
					/* Check if a 1S was scanned. We actually expect a 
					 * 1S but could be any BC that retrieves PN and Serial
					 *  - or -
					 * If no Part Number and serial were scanned:
					 * Check if the WU was scanned */
					if(pn_change || sn_change || (barcode.getBCMyPartObject().getWU().length() > 0)) {
						// Get the needed values from the fieldLabels or its default values if those are not required
						MFSPartInformation record = parsePartInfo(true);
						
						if(barcode.getBCMyPartObject().getWU().length() != 0) {
							record.wu = barcode.getBCMyPartObject().getWU(); 
						}		
						if(record.wu.trim().length() > 0 || 
								(record.pn.trim().length() > 0 && 
										record.sn.trim().length() > 0)) {
							/* Only call trx if mctl exists or pn and serial exist */
							VRFYPNPLUS vrfyPNPlus = new VRFYPNPLUS(this);
							/* Call verify part transaction that is overloaded with VRFYPNPLUS */
							MFSIntStringPair rcPair = 
								verifyPart(vrfyPNPlus, compRec, record.pn, record.sn, record.ca, record.ec,  
											record.ms, record.wu, record.co, efficientClient);						
							rc = rcPair.fieldInt;
							errorString = rcPair.fieldString;							
							
							if (rc == 0 || rc == VRFYPNPLUS.COLLECT_SUB_PART_DATA) {
								/* Update Tag label with retrieved values (Simulate tha scann or the )*/
								this.setValueLabelText(CONTROL_NUMBER, 6, vrfyPNPlus.getOutputMctl());
								this.setValueLabelText(PART_NUMBER, 2, vrfyPNPlus.getOutputInpn());								
								this.setValueLabelText(SEQUENCE_NUMBER, 4, vrfyPNPlus.getOutputInsq());
								
								if(vrfyPNPlus.getOutputCoo().trim().length() != 0) {
									wuCoo = vrfyPNPlus.getOutputCoo();
								}							
							}		
							if(rc == VRFYPNPLUS.COLLECT_SUB_PART_DATA){
								MFSVendorSubPartsController vendorLogging = new MFSVendorSubPartsController(getParentFrame(), false);
								rc = vendorLogging.displayVendorSubassemblyPartsDialog(null, vrfyPNPlus);
								
								/* Check if the dialog had an exception or the Subassembly is not complete */
								if(rc != 0 || !vendorLogging.isSubComplete()) {
									/* Clear out scanned values from the log Dialog,  
									 *  so the part can't be installed.
									 */
									this.setValueLabelText(CONTROL_NUMBER, 6, ""); //$NON-NLS-1$
									this.setValueLabelText(PART_NUMBER, 2, "");	//$NON-NLS-1$
									this.setValueLabelText(SEQUENCE_NUMBER, 4, ""); //$NON-NLS-1$
								}								
							}							
						}
					}
				}				
				/* Calculate Coo logic, if Scanned or cached */
				if(calculateCoo(wuCoo, barcode.getBCMyPartObject()))
				{	/* If a new Coo was scanned or Retrieved */
					this.singleCoo = false; /* Deactivate single prepopulated Coo */
					found = true;
				}				
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
					IGSMessageBox.showEscapeMB(this, null, erms, null, efficientClient);	// ~33
				}
			}
			/* SRCHLOG - if all input has been collected, then logPart */
			if (rc == 0 &&
				(this.fieldLogType.equals(LT_SEARCH_LOG) || 
					this.fieldLogType.equals(LT_SEARCH_REMOVE)) &&
				(allInputCollected()))
			{
				logPart();
			}
		}
		catch (Exception e)
		{
			this.setCursor(Cursor.getDefaultCursor());
			IGSMessageBox.showEscapeMB(this, null, null, e);
		}
		this.toFront();
		this.tfInput.setText(EMPTY_STRING);
	}

	/**~30A
	 * This function will remove the xml retrieved from rework when ESC key 
	 * or cancel has been pressed.  
	 * @param 
	 * @return 
	 */
	private void removeReworkCollectedData()
	{
		if((this.fieldHeaderRec.getDataCollection() != null) && (fieldHeaderRec.getDataCollection().getW_partcoll_remove() != null))
		{
			fieldHeaderRec.getDataCollection().setW_partcoll_remove(null);
		}
	}

	//~15A New method
	/**
	 * Sets the row information. Each row is a
	 * <code>MFSPartInstructionJPanel</code>.
	 * @param vector the row <code>Vector</code>
	 * @param activeRow the active row
	 */
	public void setRowInfo(Vector<MFSPartInstructionJPanel> vector, int activeRow)
	{
		this.fieldRowVector = vector;
		this.fieldActiveRow = activeRow;
	}
	
	/**
	 * Executes the RTV_VPDLOG, RTV_CUODAC, and RTV_CUODKY transactions and
	 * calls {@link MFSPrintingMethods#cuodkey} if all of the transactions were
	 * successful.
	 * @param compRec the <code>MFSComponentRec</code>
	 * @param efficientClient <code>true</code> if running efficient client
	 */
	public void tcodTrx(MFSComponentRec compRec, boolean efficientClient)
	{
		String errorString = EMPTY_STRING;

		if (compRec.getIsTcodPart())
		{
			/* ******************* ~28C ****************** */
			/* Main logic was moved out to DirectWorkPanel */
			errorString = this.fieldDirWork.tcodTrxLogic(compRec);

			if (errorString.length() > 0)
			{
				IGSMessageBox.showOkMB(this, null, errorString, null, efficientClient);	// ~33
				this.toFront();
				this.tfInput.setText(EMPTY_STRING);
				this.tfInput.requestFocusInWindow();
			}
		} /* end istcodpart */
	}
	
	//~19C Add parameter MACQ 
	/**
	 * Updates the MAC Address information
	 * @param compRec the <code>MFSComponentRec</code>
	 * @param pn the part number
	 * @param sn the sequence number
	 * @param macq the MAC quantity
	 * @param efficientClient <code>true</code> if running efficient client
	 * @return 0 on success; otherwise
	 *         {@link MFSMacAddressUpdater#RC_ERR_NO_MSG_NEEDED}
	 */
	public int updateMAC(MFSComponentRec compRec, String pn, String sn, String macq, boolean efficientClient)
	{
		//~20C Return RC_ERR_NO_MSG_NEEDED unless 
		//     MFSMacAddressUpdater.updateMac returns 0
		int rc = MFSMacAddressUpdater.RC_ERR_NO_MSG_NEEDED;

		try
		{
			if (sn.trim().length() == 0)
			{
				String erms = "MAC ID collection is required.  Serial Number needs to be collected for this part as well!"; //$NON-NLS-1$
				IGSMessageBox.showOkMB(this, null, erms, null, efficientClient);	// ~33
			}
			else
			{
				//~12C Moved UPDT_MAC call to new method
				//~19C Use createDialog method to create the MFSMacIDDialogBase
				//~20C Change createDialog to display error and return null
				//     instead of throwing MFSException with error message
				MFSMacIDDialogBase macDialog = 
					MFSMacAddressUpdater.createDialog(this, macq, compRec.getPnri());
				if(macDialog != null)
				{
					rc = MFSMacAddressUpdater.updateMac(this, macDialog, pn, sn);
				}
			}//end of valid serial number
		}
		catch (MFSTransactionException te)
		{
			rc = MFSMacAddressUpdater.RC_ERR_NO_MSG_NEEDED;
		}
		catch (Exception e)
		{
			//~19C Set return code to RC_ERR_NO_MSG_NEEDED
			rc = MFSMacAddressUpdater.RC_ERR_NO_MSG_NEEDED;
			IGSMessageBox.showOkMB(this, null, null, e, efficientClient);	// ~33
		}
		return rc;
	}

	//~23A New method
	/**
	 * Validates the scanned in machine serial.
	 * @param compRec the <code>MFSComponentRec</code> for which logging is
	 *        being performed
	 * @param barcode the <code>MFSBCBarcodeDecoder</code> used to decode the machine serial barcode
	 * @return <code>true</code> if the machine serial is valid
	 */ 
	protected boolean validateMachineSerial(MFSComponentRec compRec, MFSBCBarcodeDecoder barcode)
	{   
		boolean valid=false;
		final String cmti = barcode.getMyBCIndicatorValue().getCMTI();
		if (cmti.equals("M"))  //$NON-NLS-1$
		{
			valid=true;
		}
		else if (this.fieldLogType.equals(LT_REWORK))
		{
			valid=true;
		}
		else
		{
			valid=validateMachineSerial(compRec, barcode.getBCMyPartObject());
		}
		return valid;
	}
	
	//~24A New Method
	/**
	 * Displays an <code>MFSSEQNDialog</code> to execute the VLDT_SEQN transaction.
	 * @param compRec the <code>MFSComponentRec</code>
	 * @param mfgn the type for the <code>MFSHeaderRec</code>
	 * @param idss the type for the <code>MFSHeaderRec</code>
	 * @return the return code of the <code>MFSWWNNDialog</code>
	 */
	private int validateSEQN(MFSComponentRec compRec, String mfgn, String idss)
	{
		int rc = 0;
		try
		{
			MFSSEQNDialog seqnDialog = new MFSSEQNDialog(getParentFrame(), compRec, mfgn, idss);
			seqnDialog.initDisplay(); 
			seqnDialog.setLocationRelativeTo(this); 
			seqnDialog.setVisible(true);
			rc = seqnDialog.getReturnCode();
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(this, null, null, e);

			this.toFront();
			this.tfInput.setText(EMPTY_STRING);
			this.tfInput.requestFocusInWindow();
		}
		return rc;
	}

	/**
	 * Executes the VRFY_PART transaction to verify the scanned in part information.
	 * @param compRec the <code>MFSComponentRec</code>
	 * @param pn the part number
	 * @param sn the sequence number
	 * @param ca the card assembly
	 * @param ec the EC number
	 * @param ms the machine serial
	 * @param wu the work unit control number
	 * @param co the country
	 * @param efficientClient <code>true</code> if running efficient client
	 * @return an <code>MFSIntStringPair</code> containing the return code and
	 *         error message
	 */
	private MFSIntStringPair verifyPart(VRFY_PART vrfy_part, MFSComponentRec compRec, String pn, 
										String sn, String ca, String ec, String ms, String wu,
										String co, boolean efficientClient)
	{
		if(vrfy_part == null) {
			vrfy_part = new VRFY_PART(this);
		}		
		vrfy_part.setInputPrln(this.fieldHeaderRec.getPrln());
		vrfy_part.setInputItem(compRec.getItem());
		vrfy_part.setPnriDoNotCollect(compRec.isPnriDoNotCollect());
		vrfy_part.setInputInpn(pn);
		vrfy_part.setInputInca(ca);
		vrfy_part.setInputInec(ec);
		vrfy_part.setInputInsq(sn);
		vrfy_part.setInputUnpr(this.fieldHeaderRec.getUnpr());
		vrfy_part.setInputMctl(compRec.getMctl());
		vrfy_part.setInputCwun(wu);
		vrfy_part.setInputMmdl(compRec.getMmdl());
		vrfy_part.setInputPll1(compRec.getPll1());
		vrfy_part.setInputPll2(compRec.getPll2());
		vrfy_part.setInputPll3(compRec.getPll3());
		vrfy_part.setInputPll4(compRec.getPll4());
		vrfy_part.setInputPll5(compRec.getPll5());
		vrfy_part.setInputPari(compRec.getPari());
		vrfy_part.setInputMfgn(this.fieldHeaderRec.getMfgn());
		vrfy_part.setInputIdss(this.fieldHeaderRec.getIdss());
		vrfy_part.setInputCrct(compRec.getCrct());
		vrfy_part.setInputRcon(this.fieldHeaderRec.getRcon());
		vrfy_part.setInputMatp(compRec.getMatp());
		vrfy_part.setInputMspi(compRec.getMspi());
		vrfy_part.setInputMs(ms);
		vrfy_part.setInputMcsn(compRec.getMcsn());		
		vrfy_part.setInputTypz(this.fieldHeaderRec.getTypz());
		vrfy_part.setInputAmsi(compRec.getAmsi());
		vrfy_part.setInputCooc(co);
		vrfy_part.setInputNmbr(compRec.getNmbr());
		//~7 Pass blank malc/milc into VRFY_PART
		vrfy_part.setInputMalc("       "); //$NON-NLS-1$
		vrfy_part.setInputMilc("            "); //$NON-NLS-1$
		
		vrfy_part.execute();		
		int rc = vrfy_part.getReturnCode();
		
		/* rc==100, is a warning; print the messages, then reset rc=0 */
		if (rc == 100)
		{
			this.setCursor(Cursor.getDefaultCursor());
			this.fieldDirWork.showPNSNErrorList(vrfy_part.getOutput(), 0);
			rc = 0;
		}
		else if (rc == 111)
		{
			//~19C Pass MACQ (vrfy_part.getOutput) to updateMAC
			rc = updateMAC(compRec, pn, sn, vrfy_part.getOutput(), efficientClient);	// ~33
		}
		else if (rc == MFSMacAddressUpdater.NO_MAC_ID_IN_SUB)
		{
			//~12A Prompt to collect MACID when missing on parts inside the
			// assembly
			MFSMacAddressUpdater.updateChildMacIDs(this, vrfy_part.getOutput());				
			rc = MFSMacAddressUpdater.RC_ERR_NO_MSG_NEEDED;

			/*
			 * Note: Since rc is set to RC_ERR_NO_MSG_NEEDED, verifing a part
			 * will always fail if VRFY_PART returns NO_MAC_ID_IN_SUB. This
			 * requires the assembly to be verified again after MAC Address
			 * information is added to parts inside the assembly
			 */	
		}
		return new MFSIntStringPair(rc, vrfy_part.getOutput());
	}
	
	/* Begin ~9A New method to call VRFY_PX10 trx */
	/**
	 * Executes the VRFY_PX10 transaction to check for part exceptions.
	 * @param mctl the work unit control number
	 * @param crct the crct
	 * @param inpn the part number
	 * @param efficientClient <code>true</code> if running efficient client
	 * @return the return code from the VRFY_PX10 transaction
	 */
	private int vrfyPX10Logic(String mctl, String crct, String inpn, boolean efficientClient)
	{
		int rc = 0;

		MfsXMLDocument xml_data1 = new MfsXMLDocument("VRFY_PX10"); //$NON-NLS-1$
		xml_data1.addOpenTag("DATA"); //$NON-NLS-1$
		xml_data1.addCompleteField("MCTL", mctl); //$NON-NLS-1$
		xml_data1.addCompleteField("CRCT", crct); //$NON-NLS-1$
		xml_data1.addCompleteField("INPN", inpn); //$NON-NLS-1$
		xml_data1.addCloseTag("DATA"); //$NON-NLS-1$
		xml_data1.finalizeXML();

		MFSTransaction vrfy_px10 = new MFSXmlTransaction(xml_data1.toString());
		vrfy_px10.setActionMessage("Checking for Part Exceptions, Please Wait..."); //$NON-NLS-1$
		MFSComm.getInstance().execute(vrfy_px10, this);
		rc = vrfy_px10.getReturnCode();

		if (rc != 0)
		{
			//use a special return code from this method so we know not to
			// display a message in the calling pgm
			rc = MFSMacAddressUpdater.RC_ERR_NO_MSG_NEEDED; /* ~9C */
			IGSMessageBox.showOkMB(this, null, vrfy_px10.getErms(), null, efficientClient);	// ~33
		}
		return rc;
	}
}
