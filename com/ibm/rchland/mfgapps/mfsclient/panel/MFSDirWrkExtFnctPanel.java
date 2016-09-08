/* © Copyright IBM Corporation 2004, 2015. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag  IPSR/PTR Name             Details
 * ---------- ----  -------- ---------------- ----------------------------------
 * 2005-09-27   ~1  31867EM                   -add software liscense label printing for new RSS products
 *                                            -add softwareKeycode method
 * 2006-06-28   ~2  31801JM  R Prechel        -Used ActionJPanel.incrementValue method
 *                                            -Removed unused variables and getBuilderData
 * 2006-07-14   ~3           D Fichtinger     -Add new constructor and general cleanup
 * 2006-10-10   ~4  36523JM  M Barth          -Add reprint capability of downbin parts.
 * 2007-01-10   ~5  36302JM  M Barth          -Add reprint button for Assembly Label.
 * 2007-02-16   ~6  34242JR  R Prechel        -Java 5 version
 * 2007-04-02   ~7  38166JM  R Prechel        -Add setLocationRelativeTo for dialogs
 * 2007-04-05   ~8  34242JR  R Prechel        -Change enum to enumeration
 * 2007-12-03   ~9  40405FR  VH Avila         -Call the new MFSPrintingMethods.tcodMinutes method
 *                                            when the TCOD BILL button is pressed
 * 2008-04-23   ~10 40764EM  Santiago D       -Support KEYCODESLONG long label for new hydra release
 * 2008-07-31   ~11 39375PP  Santiago D       -Add Country of Origin (CoO) label printing capability
 * 2008-08-06      39375PPa  Santiago D       -Remove PRODPACKSM wu type validation 
 * 											  -Remove COOCNTR wu type validation
 * 2009-01-15   ~12 41303JL  Toribio H.       -Change MFSXMLTransaction class for IGSXMLTransaction class in some calls.
 * 2009-03-25   ~13 44544GB  Santiago D       -Remove pbMifLbl button
 * 2010-03-16   ~14 42558JL  Santiago SC      -Add onDemand button and functionality
 * 2011-02-04   ~15 50298KM  Luis M.          -Add Feature on Demand button and Functionality
 * 2011-04-15   ~16 50520RB  Luis M.          -Use the correct collect data values to remove logic in viewOps.
 * 2011-11-09   ~17 588480   Santiago SC      -Grey Market, new LOGVENDORPARTS button
 * 2015-09-23   ~18 1384186  Andy Williams    -Add new Part Weight Collection function
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.panel;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Enumeration;

import javax.swing.JOptionPane;
import javax.swing.ListModel;

import mfsxml.MfsXMLDocument;
import mfsxml.MfsXMLParser;

import com.ibm.rchland.mfgapps.client.utils.messagebox.IGSMessageBox;
import com.ibm.rchland.mfgapps.client.utils.xml.IGSXMLTransaction;
import com.ibm.rchland.mfgapps.mfsclient.MFSFrame;
import com.ibm.rchland.mfgapps.mfsclient.MFSMenuButton;
import com.ibm.rchland.mfgapps.mfsclient.MFSMenuPanel;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSGenericListDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSKittingOpDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSOperationsDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSRebrandDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSRecvPartDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSRtvMctl2Dialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSRtvMctlDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSStandAlonePartWeightDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSTracksubReprintDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSVendorSubPartsController;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSWorkUnitDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSWorkUnitLocDisplayDialog;
import com.ibm.rchland.mfgapps.mfsclient.struct.MFSComponentListModel;
import com.ibm.rchland.mfgapps.mfsclient.struct.MFSComponentRec;
import com.ibm.rchland.mfgapps.mfsclient.struct.MFSHeaderRec;
import com.ibm.rchland.mfgapps.mfsclient.struct.MFSOnDemandKeyData;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSButtonIterator;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSOnDemand;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSPrintingMethods;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSVPDProc;
import com.ibm.rchland.mfgapps.mfscommon.MFSConfig;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSComm;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSFixedTransaction;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSTransaction;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSXmlTransaction;

/**
 * <code>MFSDirWrkExtFnctPanel</code> is the <code>MFSMenuPanel</code> for
 * Direct Work Extended Functions.
 * @author The MFS Client Development Team
 */
public class MFSDirWrkExtFnctPanel
	extends MFSMenuPanel
{
	private static final long serialVersionUID = 1L;

	/** The default screen name of an <code>MFSDirWrkExtFnctPanel</code>. */
	public static final String SCREEN_NAME = "Direct Work Extended Functions"; //$NON-NLS-1$

	/** The View Ops (F2) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbViewOps = MFSMenuButton.createLargeButton("View Ops", //$NON-NLS-1$
			"ViewopF2.gif", null, //$NON-NLS-1$
			"BUTTON,DWEXTFUNC,VIEWOP"); //$NON-NLS-1$

	/** The VPD Label (F3) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbVpdLbl = MFSMenuButton.createLargeButton("VPD Label", //$NON-NLS-1$
			"F3VpdReprint.gif", null, //$NON-NLS-1$
			"BUTTON,DWEXTFUNC,VPDLBL"); //$NON-NLS-1$

	/** The DASDP Label (F3) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbDasdp = MFSMenuButton.createLargeButton("DASDP Label", //$NON-NLS-1$
			"dasdF3.gif", null, //$NON-NLS-1$
			"BUTTON,DWEXTFUNC,DASDPLBL"); //$NON-NLS-1$

	/** The Kitting (F4) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbKitting = MFSMenuButton.createLargeButton("Kitting", //$NON-NLS-1$
			"printF4.gif", null, //$NON-NLS-1$
			"BUTTON,DWEXTFUNC,KITTING"); //$NON-NLS-1$

	/** The Kitting (F4) <code>MFSMenuButton</code> for Rochester Kitting. */
	private MFSMenuButton pbRochKit = MFSMenuButton.createLargeButton("Kitting", //$NON-NLS-1$
			"printF4.gif", null, //$NON-NLS-1$
			"BUTTON,DWEXTFUNC,ROCHKIT"); //$NON-NLS-1$

	/** The FC MES (F5) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbRochMes = MFSMenuButton.createLargeButton("FC MES", //$NON-NLS-1$
			"printF5.gif", null, //$NON-NLS-1$
			"BUTTON,DWEXTFUNC,ROCHFCMES"); //$NON-NLS-1$

	/** The Pack List (F6) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbRochPackList = MFSMenuButton.createLargeButton("Pack List", //$NON-NLS-1$
			"printF6.gif", null, //$NON-NLS-1$
			"BUTTON,DWEXTFUNC,ROCHPACKLIST"); //$NON-NLS-1$

	/** The Rtv Mctl (F7) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbRtvMctl = MFSMenuButton.createLargeButton("Rtv Mctl", //$NON-NLS-1$
			"mctlF7.gif", null, //$NON-NLS-1$
			"BUTTON,DWEXTFUNC,RTVMCTL"); //$NON-NLS-1$

	/** The Rtv Mctl 2 (F7) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbRtvMctl2 = MFSMenuButton.createLargeButton("Rtv Mctl", //$NON-NLS-1$
			"mctlF7.gif", null, //$NON-NLS-1$
			"BUTTON,DWEXTFUNC,RTVMCTL2"); //$NON-NLS-1$

	/** The Soft Key (F8) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbSoftKey = MFSMenuButton.createLargeButton("Soft Key", //$NON-NLS-1$
			"SoftwareKeyF8.gif", null, //$NON-NLS-1$
			"BUTTON,DWEXTFUNC,SOFTKEY"); //$NON-NLS-1$

	/** The Rtv Label (F9) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbRtvLabel = MFSMenuButton.createLargeButton("Rtv Label", //$NON-NLS-1$
			"WuLblF9.gif", null, //$NON-NLS-1$
			"BUTTON,DWEXTFUNC,RTVLABEL"); //$NON-NLS-1$

	/** The Ship Tag (F10) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbShipTag = MFSMenuButton.createLargeButton("Ship Tag", //$NON-NLS-1$
			"ShipTagF10.gif", null, //$NON-NLS-1$
			"BUTTON,DWEXTFUNC,SHIPTAG"); //$NON-NLS-1$

	//~13C - The shortcut key and image is free to use
	/** The MIF Label (F11) , MifReprintF11.gif <code>MFSMenuButton</code>. */

	/** The MES MFI (F12) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbMesMFI = MFSMenuButton.createLargeButton("MES MFI", //$NON-NLS-1$
			"printF12.gif", null, //$NON-NLS-1$
			"BUTTON,DWEXTFUNC,TRACKSUB3"); //$NON-NLS-1$

	/** The Recv Part (SHFT + F1) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbRecvPart = MFSMenuButton.createLargeButton("Recv Part", //$NON-NLS-1$
			"printF13.gif", null, //$NON-NLS-1$
			"BUTTON,DWEXTFUNC,RECVPART"); //$NON-NLS-1$

	/** The Prod Sub (SHFT + F2) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbProdSub = MFSMenuButton.createLargeButton("Prod Sub", //$NON-NLS-1$
			"printF14.gif", null, //$NON-NLS-1$
			"BUTTON,DWEXTFUNC,PRODSUB"); //$NON-NLS-1$

	/** The Track Sub (SHFT + F3) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbTrackSub = MFSMenuButton.createLargeButton("Track Sub", //$NON-NLS-1$
			"printF15.gif", null, //$NON-NLS-1$
			"BUTTON,DWEXTFUNC,TRACKSUB"); //$NON-NLS-1$

	/** The Mach Type (SHFT + F4) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbMachType = MFSMenuButton.createLargeButton("Mach Type", //$NON-NLS-1$
			"printF16.gif", null, //$NON-NLS-1$
			"BUTTON,DWEXTFUNC,MACHTYPE"); //$NON-NLS-1$

	/** The Fru Label (SHFT + F5) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbFru = MFSMenuButton.createLargeButton("Fru Label", //$NON-NLS-1$
			"printF17.gif", null, //$NON-NLS-1$
			"BUTTON,DWEXTFUNC,FRUNUMB"); //$NON-NLS-1$

	/** The Cnfg Node (SHFT + F6) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbConfNode = MFSMenuButton.createLargeButton("Cnfg Node", //$NON-NLS-1$
			"printF18.gif", null, //$NON-NLS-1$
			"BUTTON,DWEXTFUNC,CNFGNODE"); //$NON-NLS-1$

	/** The Serial Node (SHFT + F7) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbConfSeri = MFSMenuButton.createLargeButton("Serial Node", //$NON-NLS-1$
			"printF19.gif", null, //$NON-NLS-1$
			"BUTTON,DWEXTFUNC,SERIALNODE"); //$NON-NLS-1$

	/** The Fab List (SHFT + F8) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbFabList = MFSMenuButton.createLargeButton("Fab List", //$NON-NLS-1$
			"printF20.gif", null, //$NON-NLS-1$
			"BUTTON,DWEXTFUNC,FABLIST"); //$NON-NLS-1$

	/** The Travel (SHFT + F9) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbTravel = MFSMenuButton.createLargeButton("Travel", //$NON-NLS-1$
			"printF21.gif", null, //$NON-NLS-1$
			"BUTTON,DWEXTFUNC,TRAVEL"); //$NON-NLS-1$

	/** The SCSI Label (SHFT + F10) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbScsi = MFSMenuButton.createLargeButton("SCSI Label", //$NON-NLS-1$
			"printF22.gif", null, //$NON-NLS-1$
			"BUTTON,DWEXTFUNC,SCSI"); //$NON-NLS-1$

	/** The MIR (SHFT + F11) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbMir = MFSMenuButton.createLargeButton("MIR", //$NON-NLS-1$
			"printF23.gif", null, //$NON-NLS-1$
			"BUTTON,DWEXTFUNC,MIR"); //$NON-NLS-1$

	/** The Sys Key (SHFT + F12) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbSysPWD = MFSMenuButton.createLargeButton("Sys Key", //$NON-NLS-1$
			"SyskeyF24.gif", null, //$NON-NLS-1$
			"BUTTON,DWEXTFUNC,SYSTEMPWD"); //$NON-NLS-1$	

	/** The CnfgNode R (CTRL + F1) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbConfNodeR = MFSMenuButton.createLargeButton("CnfgNode R", //$NON-NLS-1$
			"printCTRLF1.gif", null, //$NON-NLS-1$
			"BUTTON,DWEXTFUNC,CNFGNODER"); //$NON-NLS-1$

	/** The POD Sheet (CTRL + F2) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbPodKey = MFSMenuButton.createLargeButton("POD Sheet", //$NON-NLS-1$
			"podkeyCTRLF2.gif", null, //$NON-NLS-1$
			"BUTTON,DWEXTFUNC,RTVPODKEY"); //$NON-NLS-1$

	/** The Draeger (CTRL + F3) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbSiemens = MFSMenuButton.createLargeButton("Draeger", //$NON-NLS-1$
			"printCTRLF3.gif", null, //$NON-NLS-1$
			"BUTTON,DWEXTFUNC,SIEMENS"); //$NON-NLS-1$

	/** The Bill Notify (CTRL + F4) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbTCODBilling = MFSMenuButton.createLargeButton("Bill Notify", //$NON-NLS-1$
			"tcodBillingCTRLF4.gif", null, //$NON-NLS-1$
			"BUTTON,DWEXTFUNC,TCODBILLING"); //$NON-NLS-1$

	/** The Prepay Sheet (CTRL + F5) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbTCODPrepay = MFSMenuButton.createLargeButton("Prepay Sheet", //$NON-NLS-1$
			"tcodPrePayCTRLF5.gif", null, //$NON-NLS-1$
			"BUTTON,DWEXTFUNC,TCODPREPAY"); //$NON-NLS-1$

	/** The Adv. Feature (CTRL + F6) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbAdvFeatModel = MFSMenuButton.createLargeButton(
			"Adv. Feature", "advFeatModelCTRLF6.gif", null,  //$NON-NLS-1$//$NON-NLS-2$
			"BUTTON,DWEXTFUNC,ADVFEATMODEL"); //$NON-NLS-1$

	/** The Prod Feat Code (CTRL + F7) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbProdFeatCode = MFSMenuButton.createLargeButton(
			"Prod Feat Code", "prodFeatCodeCTRLF7.gif", null,  //$NON-NLS-1$//$NON-NLS-2$
			"BUTTON,DWEXTFUNC,PRODFEATCODE"); //$NON-NLS-1$

	/** The Loc Chart (CTRL + F8) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbDeconfigLocChart = MFSMenuButton.createLargeButton(
			"Loc Chart", "locChartCTRLF8.gif", null,  //$NON-NLS-1$//$NON-NLS-2$
			"BUTTON,DWEXTFUNC,DECONFIGLOC"); //$NON-NLS-1$

	/** The Prod Pack Lrg (CTRL + F9) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbProdPackLarge = MFSMenuButton.createLargeButton(
			"Prod Pack Lrg", "prodPackLargeCTRLF9.gif", null,  //$NON-NLS-1$//$NON-NLS-2$
			"BUTTON,DWEXTFUNC,PRODPACKLRG"); //$NON-NLS-1$

	/** The Prod Pack Sm. (CTRL + F10) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbProdPackSmall = MFSMenuButton.createLargeButton(
			"Prod Pack Sm.", "prodPackSmallCTRLF10.gif", null,  //$NON-NLS-1$//$NON-NLS-2$
			"BUTTON,DWEXTFUNC,PRODPACKSM"); //$NON-NLS-1$

	/** The Mach Index (CTRL + F11) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbMachineIndexCard = MFSMenuButton.createLargeButton(
			"Mach Index", "machineIndexCTRLF11.gif", null,  //$NON-NLS-1$//$NON-NLS-2$
			"BUTTON,DWEXTFUNC,MACHINDEXCARD"); //$NON-NLS-1$

	/** The MES Ship Group (CTRL + F12) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbMESShipGroup = MFSMenuButton.createLargeButton(
			"MES Ship Group", "mesShipGrpCTRLF12.gif", null,  //$NON-NLS-1$//$NON-NLS-2$
			"BUTTON,DWEXTFUNC,MESSHIPGRPLBL"); //$NON-NLS-1$

	/** The WWID (CTRL + A) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbWWID = MFSMenuButton.createLargeButton("WWID", //$NON-NLS-1$
			"WWIDCTRLA.gif", null, //$NON-NLS-1$
			"BUTTON,DWEXTFUNC,WWID"); //$NON-NLS-1$

	/** The MAC ID (CTRL + B) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbMACID = MFSMenuButton.createLargeButton("MAC ID", //$NON-NLS-1$
			"MACIDCTRLB.gif", null, //$NON-NLS-1$
			"BUTTON,DWEXTFUNC,MACID"); //$NON-NLS-1$
	//~17A
	/** Vendor Parts Logging (CTRL + E) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbVendorPartsLogging = MFSMenuButton.createLargeButton(
			"Vendor PN Log", "vendorPartLog.gif", null,  //$NON-NLS-1$//$NON-NLS-2$
			"BUTTON,DWEXTFUNC,LOGVENDORPARTS"); //$NON-NLS-1$
	
	//~15A
	/** The Feature On Demand Label (CTRL + H) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbFeatureOnDemand = MFSMenuButton.createLargeButton(
			"FoD", "printCTRLH.gif", null,  //$NON-NLS-1$//$NON-NLS-2$
			"BUTTON,DWEXTFUNC,FODLABEL"); //$NON-NLS-1$
	
	//~14A
	/** The On Demand Label (CTRL + L) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbOnDemand = MFSMenuButton.createLargeButton(
			"On Demand", "printCTRLL.gif", null,  //$NON-NLS-1$//$NON-NLS-2$
			"BUTTON,DWEXTFUNC,ONDEMANDLBL"); //$NON-NLS-1$
	
	//~11A
	/** The CoO Cntr label (CTRL + P) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbCooCntr = MFSMenuButton.createLargeButton("CoO Cntr", //$NON-NLS-1$
			"printCTRLP.gif", null, //$NON-NLS-1$
			"BUTTON,DWEXTFUNC,COOCNTR"); //$NON-NLS-1$	

	/** The SPD11S (CTRL + 1) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbSpd11s = MFSMenuButton.createLargeButton("SPD11S", //$NON-NLS-1$
			"printCTRL1.gif", null, //$NON-NLS-1$
			"BUTTON,DWEXTFUNC,SPD11S"); //$NON-NLS-1$

	/** The MAINID (CTRL + 2) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbMainID = MFSMenuButton.createLargeButton("MAINID", //$NON-NLS-1$
			"mainIdCTRL2.gif", null, //$NON-NLS-1$
			"BUTTON,DWEXTFUNC,MAINID"); //$NON-NLS-1$

	/** The MachTyp/SN (CTRL + 3) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbMatpMcsn = MFSMenuButton.createLargeButton("MachTyp/SN", //$NON-NLS-1$
			"printCTRL3.gif", null, //$NON-NLS-1$
			"BUTTON,DWEXTFUNC,MATPMCSN"); //$NON-NLS-1$

	/** The Display Locs (CTRL + 4) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbDisplayLoc = MFSMenuButton.createLargeButton("Display Locs", //$NON-NLS-1$
			"locDisplayCTRL4.gif", null, //$NON-NLS-1$
			"BUTTON,DWEXTFUNC,LOCDISPLAY"); //$NON-NLS-1$

	/** The KeyCodes (CTRL + 5) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbKeyCodes = MFSMenuButton.createLargeButton("KeyCodes", //$NON-NLS-1$
			"printCTRL5.gif", null, //$NON-NLS-1$
			"BUTTON,DWEXTFUNC,KEYCODES"); //$NON-NLS-1$

	/** The SPD11SText (CTRL + 6) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbSpd11sText = MFSMenuButton.createLargeButton("SPD11SText", //$NON-NLS-1$
			"printCTRL6.gif", null, //$NON-NLS-1$
			"BUTTON,DWEXTFUNC,SPD11STEXT"); //$NON-NLS-1$

	//~1A
	/** The SW Keys (CTRL + 7) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbSoftwareKeycode = MFSMenuButton.createLargeButton("SW Keys", //$NON-NLS-1$
			"swKeysCTRL7.gif", null, //$NON-NLS-1$
			"BUTTON,DWEXTFUNC,SFTKEYCODE"); //$NON-NLS-1$

	//~4A
	/** The DwnBin11S (CTRL + 8) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbDwnBin11s = MFSMenuButton.createLargeButton("DwnBin11S", //$NON-NLS-1$
			"printCTRL8.gif", null, //$NON-NLS-1$
			"BUTTON,DWEXTFUNC,DWNBIN11S"); //$NON-NLS-1$

	//~5A
	/** The Assembly Label (CTRL + 9) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbAssemblyLabel = MFSMenuButton.createLargeButton(
			"Assembly Label", "printCTRL9.gif", null,  //$NON-NLS-1$//$NON-NLS-2$
			"BUTTON,DWEXTFUNC,ASSEMBLYLBL"); //$NON-NLS-1$
	
	//~18A
	/** The Part Weight (CTRL + W) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbPartWeight = MFSMenuButton.createLargeButton("Part Weight",
			"partweightCTRLW.gif", null, //$NON-NLS-1$
			"BUTTON,DWEXTFUNC,PARTWEIGHT"); //$NON-NLS-1$

	/** The <code>MFSDirectWorkPanel</code> that caused this panel to be displayed. */
	private MFSDirectWorkPanel fieldDirWork;
	
	/** The <code>MFSHeaderRec</code> for the work unit. */
	private MFSHeaderRec fieldHeaderRec = null; //Set by constructor

	/** The <code>MFSComponentListModel</code>. */
	private MFSComponentListModel fieldCompListModel = null; //Set by constructor

	/** The mctl for the work unit. */
	private String fieldCurrMctl = null; //Set by constructor

	/** The list of operations last displayed by {@link #showOps}. */
	private String fieldViewOpsData = ""; //$NON-NLS-1$

	/** The operations list index last displayed by {@link #showOps}. */
	private int fieldViewOpsIdx = 0;

	/**
	 * Constructs a new <code>MFSDirWrkExtFnctPanel</code>.
	 * @param parent the <code>MFSFrame</code> used to display this panel
	 * @param source the <code>MFSDirectWorkPanel</code> that caused this panel to be displayed
	 * @param headerRec the <code>MFSHeaderRec</code> for the work unit
	 * @param listModel the <code>MFSComponentListModel</code> for the work unit
	 * @param currMctl the MCTL for the current work unit
	 */
	public MFSDirWrkExtFnctPanel(MFSFrame parent, MFSDirectWorkPanel source, MFSHeaderRec headerRec, MFSComponentListModel listModel, String currMctl)  //lm change
	{
		super(parent, source, SCREEN_NAME, 6, 5);
		this.fieldDirWork = source;
		this.fieldHeaderRec = headerRec;
		this.fieldCompListModel = listModel;
		this.fieldCurrMctl = currMctl;
		
		this.fieldButtonIterator = createMenuButtonIterator();
		createLayout();
		configureButtons();
		addMyListeners();
	}

	//~6A new method
	/**
	 * Creates an <code>MFSButtonIterator</code> for this panel's
	 * <code>MFSMenuButton</code>s.
	 * @return the <code>MFSButtonIterator</code>
	 */
	private MFSButtonIterator createMenuButtonIterator()
	{
		//The constructor param is the number of buttons
		MFSButtonIterator result = new MFSButtonIterator(52); // ~13C //~14A //~17A
		result.add(this.pbViewOps);
		result.add(this.pbVpdLbl);
		result.add(this.pbDasdp);
		result.add(this.pbKitting);
		result.add(this.pbRochKit);
		result.add(this.pbRochMes);
		result.add(this.pbRochPackList);
		result.add(this.pbRtvMctl);
		result.add(this.pbRtvMctl2);
		result.add(this.pbSoftKey);
		result.add(this.pbRtvLabel);
		result.add(this.pbShipTag);
		result.add(this.pbMesMFI);
		result.add(this.pbRecvPart);
		result.add(this.pbProdSub);
		result.add(this.pbTrackSub);
		result.add(this.pbMachType);
		result.add(this.pbFru);
		result.add(this.pbConfNode);
		result.add(this.pbConfSeri);
		result.add(this.pbFabList);
		result.add(this.pbTravel);
		result.add(this.pbScsi);
		result.add(this.pbMir);
		result.add(this.pbSysPWD);
		result.add(this.pbConfNodeR);
		result.add(this.pbPodKey);
		result.add(this.pbSiemens);
		result.add(this.pbTCODBilling);
		result.add(this.pbTCODPrepay);
		result.add(this.pbAdvFeatModel);
		result.add(this.pbProdFeatCode);
		result.add(this.pbDeconfigLocChart);
		result.add(this.pbProdPackLarge);
		result.add(this.pbProdPackSmall);
		result.add(this.pbMachineIndexCard);
		result.add(this.pbMESShipGroup);
		result.add(this.pbWWID);
		result.add(this.pbMACID);
		result.add(this.pbSpd11s);
		result.add(this.pbMainID);
		result.add(this.pbMatpMcsn);
		result.add(this.pbDisplayLoc);
		result.add(this.pbKeyCodes);
		result.add(this.pbSpd11sText);
		result.add(this.pbSoftwareKeycode);
		result.add(this.pbDwnBin11s);
		result.add(this.pbAssemblyLabel);
		result.add(this.pbCooCntr); //~11A
		result.add(this.pbOnDemand); //~14A
		result.add(this.pbFeatureOnDemand); //~15A
		result.add(this.pbVendorPartsLogging); //~17A
		result.add(this.pbPartWeight); //~18A
		return result;
	}

	//~6C Redone based on super.configureButtons
	//which uses MFSMenuButton.isActive()
	/** Determines which buttons are displayed in the menu. */
	protected void configureButtons()
	{
		MFSConfig config = MFSConfig.getInstance();
		boolean mesMFIActive = false;
		if (config.containsConfigEntry("BUTTON,DWEXTFUNC,TRACKSUB3")) //$NON-NLS-1$
		{
			/* now check if we should configure the reprint button */
			String cnfgData1 = "TRACKSUB3," + this.fieldHeaderRec.getNmbr(); //$NON-NLS-1$	
			String cnfgData2 = "TRACKSUB3,*ALL"; //$NON-NLS-1$

			if (config.containsConfigEntry(cnfgData1)
					|| config.containsConfigEntry(cnfgData2))
			{
				/* we have a config file entry to print the tracksub3 label */
				mesMFIActive = true;
			}
		}
		this.pbMesMFI.setActive(mesMFIActive);

		super.configureButtons();
	}

	/** Invoked when {@link #pbAdvFeatModel} is selected. */
	private void advFeatModel()
	{
		try
		{
			MFSPrintingMethods.advFeatModel(this.fieldHeaderRec.getMfgn(),
					this.fieldHeaderRec.getIdss(), this.fieldHeaderRec.getOrno(),
					this.fieldHeaderRec.getMctl(), 1, getParentFrame());
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
	}

	//~5A
	/**
	 * Reprints an Assembly Label with the MCTL from the Header Record. Invoked
	 * when {@link #pbAssemblyLabel} is selected.
	 */
	private void assemblyLbl()
	{
		try
		{
			MFSPrintingMethods.assemblyLbl(this.fieldCurrMctl, 1, getParentFrame());
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
	}

	/** Invoked when {@link #pbConfNode} is selected. */
	private void confnode()
	{
		try
		{
			MFSPrintingMethods.confnode(this.fieldCurrMctl, 1, getParentFrame());
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
	}

	/** Invoked when {@link #pbConfNodeR} is selected. */
	private void confnoder()
	{
		try
		{
			MFSPrintingMethods.confnoder(this.fieldCurrMctl, 1, getParentFrame());
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
	}

	/** Invoked when {@link #pbConfSeri} is selected. */
	private void confseri()
	{
		try
		{
			MFSPrintingMethods.confseri(this.fieldHeaderRec.getPrln(), "J", //$NON-NLS-1$
					this.fieldCurrMctl, 1, getParentFrame());
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
	}
	
	//~11A
	/** Invoked when {@link #pbCoO} is selected. */
	private void cooCntr()
	{
		try
		{
			this.removeMyListeners();
			
			/* Call to UPDT_CRWC tx */
			int rc = this.fieldDirWork.updt_crwc();
			
			if(rc ==0)
			{
				final MFSConfig config = MFSConfig.getInstance();
				final String BLANK_CNTR = "          ";			 //$NON-NLS-1$
				final int CNTR_LENGTH = 10;

				/* Call to RTV_CNTR tx */
				String data = "RTV_CNTR  " + this.fieldCurrMctl + "L" + //$NON-NLS-1$ //$NON-NLS-2$
						BLANK_CNTR + config.get8CharUser();

				MFSTransaction rtv_cntr = new MFSFixedTransaction(data);
				rtv_cntr.setActionMessage("Retrieving List of Containers, Please Wait..."); //$NON-NLS-1$
				
				MFSComm.getInstance().execute(rtv_cntr, this);
				
				rc = rtv_cntr.getReturnCode();

				if (rc == 0)
				{			
					data = rtv_cntr.getOutput();
					
					if(data.trim().length() > 0)
					{
						/* Creates the Container Dialog */
						MFSGenericListDialog cntrDialog = new MFSGenericListDialog(getParentFrame(),
								"Containers", "Select a Container", "Print", "Done"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
						
						/* Fills the Container List in the Container Dialog */
						cntrDialog.initializeDefaultList();
						
						while(data.length() != 0 && data.length() >= CNTR_LENGTH)
						{
							String cntr = data.substring(0,CNTR_LENGTH);
							cntrDialog.addListItem(cntr);
							data = data.substring(CNTR_LENGTH);
						}
						
						cntrDialog.setSizeSmall();
						cntrDialog.setDefaultSelection("FIRST"); //$NON-NLS-1$
						
						do
						{
							cntrDialog.setProceedSelected(false);
							cntrDialog.setLocationRelativeTo(getParentFrame());
							cntrDialog.setVisible(true);
							
							if(cntrDialog.getProceedSelected())
							{
								/* Call RTVCOOCNTR tx */
								IGSXMLTransaction rtvCooCntr = new IGSXMLTransaction("RTVCOOCNTR"); //$NON-NLS-1$
								rtvCooCntr.setActionMessage("Determining if all parts are installed..."); //$NON-NLS-1$
								rtvCooCntr.startDocument();
								rtvCooCntr.addElement("MCTL", this.fieldCurrMctl); //$NON-NLS-1$
								rtvCooCntr.addElement("CNTR", cntrDialog.getSelectedListOption()); //$NON-NLS-1$								
								rtvCooCntr.addElement("MODE", "IDSP"); //$NON-NLS-1$ //$NON-NLS-2$
								rtvCooCntr.endDocument();	
								
								MFSComm.getInstance().execute(rtvCooCntr, this);
								
								rc = rtvCooCntr.getReturnCode(); //~1A			
								
								if(rc == 0)
								{
									String complete = rtvCooCntr.getNextElement("COMPLETE"); //$NON-NLS-1$
									
									/* if all parts installed/complete */
									if(complete.compareToIgnoreCase("NO") == 0) //$NON-NLS-1$
									{
										if(IGSMessageBox.showYesNoMB(getParentFrame(),"Warning", //$NON-NLS-1$
												"Not all components are installed. Do you still want to print?", null)) //$NON-NLS-1$
										{
											MFSPrintingMethods.cooCntr(this.fieldCurrMctl, cntrDialog.getSelectedListOption(), 
													1, getParentFrame());
										}
									}
									else
									{
										MFSPrintingMethods.cooCntr(this.fieldCurrMctl, cntrDialog.getSelectedListOption(), 
												1, getParentFrame());
									}
								}
								else
								{
									IGSMessageBox.showOkMB(getParentFrame(), null, rtvCooCntr.getErms(), null);	
								}
							} // if proceedSelected
						}
						while(!cntrDialog.getCancelSelected());
					}
					else // data length 
					{
						IGSMessageBox.showOkMB(getParentFrame(), null, "No Containers were found.", null);					 //$NON-NLS-1$
					}
				} // if RTV_CNTR no error
				else
				{
					IGSMessageBox.showOkMB(getParentFrame(), null, rtv_cntr.getErms(), null);					
				}
			} // if UPDT_CRWC no error
			else
			{
				String erms = "ERROR: Can not update the component(s)."; //$NON-NLS-1$
				IGSMessageBox.showOkMB(getParentFrame(), null, erms, null);												
			}
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
		finally
		{
			this.addMyListeners();
		}
	}	

	/** Invoked when {@link #pbDasdp} is selected. */
	private void dasdp()
	{
		try
		{
			String pilotFlag = "N"; //$NON-NLS-1$
			if (MFSConfig.getInstance().containsConfigEntry("DASDPILOT")) //$NON-NLS-1$
			{
				pilotFlag = "Y"; //$NON-NLS-1$
			}

			MFSPrintingMethods.dasdp(this.fieldHeaderRec.getMctl(), pilotFlag, 1, getParentFrame());
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
	}

	/** Invoked when {@link #pbDeconfigLocChart} is selected. */
	private void deconfigLoc()
	{
		try
		{
			MFSPrintingMethods.deconfigLoc(this.fieldHeaderRec.getMfgn(),
					this.fieldHeaderRec.getIdss(), this.fieldHeaderRec.getOrno(),
					this.fieldHeaderRec.getMctl(), 1, getParentFrame());
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
	}

	/** Invoked when {@link #pbDisplayLoc} is selected. */
	private void displayLocs()
	{
		try
		{
			int rc = 0;

			MfsXMLDocument xml_data1 = new MfsXMLDocument("RTV_RACKWU"); //$NON-NLS-1$
			xml_data1.addOpenTag("DATA"); //$NON-NLS-1$
			xml_data1.addCompleteField("MCTL", this.fieldCurrMctl); //$NON-NLS-1$
			xml_data1.addCompleteField("OLEV", this.fieldHeaderRec.getOlev()); //$NON-NLS-1$
			xml_data1.addCloseTag("DATA"); //$NON-NLS-1$
			xml_data1.finalizeXML();

			MFSTransaction rtv_rackwu = new MFSXmlTransaction(xml_data1.toString());
			MFSComm.getInstance().execute(rtv_rackwu, this);
			rc = rtv_rackwu.getReturnCode();

			if (rc != 0)
			{
				IGSMessageBox.showOkMB(getParentFrame(), null, rtv_rackwu.getErms(), null);
			}
			else
			{
				MfsXMLParser xmlParser = new MfsXMLParser(rtv_rackwu.getOutput());
				MFSWorkUnitLocDisplayDialog myLocD = new MFSWorkUnitLocDisplayDialog(getParentFrame());

				if (myLocD.loadList(xmlParser.getUnparsedXML()))
				{
					myLocD.setLocationRelativeTo(getParentFrame());
					myLocD.setVisible(true);
				}
				else
				{
					String erms = "No Locs to Display for this Work Unit!"; //$NON-NLS-1$
					IGSMessageBox.showOkMB(getParentFrame(), null, erms, null);
				}
			}//end of good call to RTV_RACKWU

			removeMyListeners();
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
		addMyListeners();
		this.pbDisplayLoc.requestFocusInWindow();
	}

	//~4A
	/** Calls the 11SDWNBIN printing method. Invoked when {@link #pbDwnBin11s} is selected. */
	private void dwnBin11s()
	{
		String new_pn, new_sn, pflg;
		int qty = 1; // Default should always be 1
		try
		{
			if (this.fieldDirWork.getDwnBinList().isEmpty())
			{
				//Call RTVDOWNBIN again, passing the current mctl.
				int rc = 0;

		        // ~12 Change to IGSXMLTransaction
				IGSXMLTransaction rtvdownbin = new IGSXMLTransaction("RTVDOWNBIN"); //$NON-NLS-1$
				rtvdownbin.startDocument();
				rtvdownbin.startElement("DATA");  //$NON-NLS-1$
				rtvdownbin.addElement("MCTN", this.fieldCurrMctl);  //$NON-NLS-1$
				rtvdownbin.endElement("DATA");  //$NON-NLS-1$		
				rtvdownbin.endDocument();
				rtvdownbin.setActionMessage("Retrieving DownBin Info, Please Wait..."); //$NON-NLS-1$
				rtvdownbin.run();
				
				rc = rtvdownbin.getReturnCode();

				/* Print flag(PFLG) used to verify down bin parts found */
				if (rc == 0)
				{
					while(null != rtvdownbin.stepIntoElement("RCD")) //$NON-NLS-1$
					{
						// Retrieve New PN, New SN, and Print Indicator from returned XML string.
						new_pn = rtvdownbin.getNextElement("PNNW"); //$NON-NLS-1$
						new_sn = rtvdownbin.getNextElement("SNEW"); //$NON-NLS-1$
						pflg = rtvdownbin.getNextElement("PFLG"); //$NON-NLS-1$
						// If print flag is "Y", then print 11SDWNBIN label
						if (pflg.equals("Y")) //$NON-NLS-1$
						{
							this.fieldDirWork.setDwnBinList(new_pn, new_sn); // ~9
							MFSPrintingMethods.elevensDwnBin(new_pn, new_sn, qty, getParentFrame());
						}
						rtvdownbin.stepOutOfElement();
					} //end while
				} //end if (rc == 0)
				else
				{
					IGSMessageBox.showOkMB(getParentFrame(), null, rtvdownbin.getErms(), null);
				}
			}
			else
			{
				//~8C Change enum to enumeration
				//DownBin parts not committed, print from hashtable.
				@SuppressWarnings("rawtypes")
				Enumeration enumeration = this.fieldDirWork.getDwnBinList().keys();
				while (enumeration.hasMoreElements())
				{
					new_pn = enumeration.nextElement().toString();
					new_sn = this.fieldDirWork.getDwnBinList().get(new_pn).toString();
					MFSPrintingMethods.elevensDwnBin(new_pn, new_sn, qty, getParentFrame());
				}
			}
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
	}

	/** Invoked when {@link #pbFabList} is selected. */
	private void fablist()
	{
		try
		{
			MFSPrintingMethods.rtvfab(this.fieldCurrMctl, 1, getParentFrame());
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
	}

	/** Invoked when {@link #pbFru} is selected. */
	private void frunumb()
	{
		try
		{
			MFSPrintingMethods.frunumb(this.fieldHeaderRec.getPrln(), "J", //$NON-NLS-1$
					this.fieldCurrMctl, 1, getParentFrame());
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
	}

	/** Invoked when {@link #pbKeyCodes} is selected. */
	private void keycodes()
	{
		try
		{
			MFSPrintingMethods.keycodes(this.fieldHeaderRec.getMfgn(),
					this.fieldHeaderRec.getIdss(), this.fieldHeaderRec.getMatp(),
					this.fieldHeaderRec.getMmdl(), this.fieldHeaderRec.getMspi(),
					this.fieldHeaderRec.getMcsn(), this.fieldHeaderRec.getOrno(),
					this.fieldHeaderRec.getMctl(), "1", "1", "N", getParentFrame()); // ~10C //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
	}

	/** Invoked when {@link #pbKitting} is selected. */
	private void kitting()
	{
		try
		{
			int rc = 0;
			String errorString = ""; //$NON-NLS-1$

			removeMyListeners();

			MFSWorkUnitDialog myWrkUnitD = new MFSWorkUnitDialog(getParentFrame());
			myWrkUnitD.setTFWorkUnitText(this.fieldCurrMctl);
			myWrkUnitD.setLocationRelativeTo(getParentFrame()); //~7A
			myWrkUnitD.setVisible(true);

			if (myWrkUnitD.getPressedEnter())
			{
				String data = "RTV_MO    " + myWrkUnitD.getMctl(); //$NON-NLS-1$
				MFSTransaction rtv_mo = new MFSFixedTransaction(data);
				rtv_mo.setActionMessage("Retrieving List of Operations, Please Wait..."); //$NON-NLS-1$
				MFSComm.getInstance().execute(rtv_mo, this);
				rc = rtv_mo.getReturnCode();

				if (rc == 0)
				{
					MFSKittingOpDialog kittOpD = new MFSKittingOpDialog(getParentFrame());
					kittOpD.loadKittOpListModel(rtv_mo.getOutput());
					kittOpD.setLocationRelativeTo(getParentFrame()); //~7A
					kittOpD.setVisible(true);

					if (kittOpD.getPressedPrint())
					{
						int index = 0;
						final MFSConfig config = MFSConfig.getInstance();
						while (rc == 0 && index < kittOpD.getKittOpListModelSize())
						{
							String listData = (String) kittOpD.getKittOpListModelElementAt(index);
							String type = listData.substring(13, 14);
							if (!type.equals(" ")) //$NON-NLS-1$
							{
								getParentFrame().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
								String nmbr = listData.substring(0, 4);
								if (config.containsConfigEntry("POKKIT")) //$NON-NLS-1$
								{
									MFSPrintingMethods.pokkit(myWrkUnitD.getMctl(), nmbr,
											type, config.get8CharCellType(), 1,
											getParentFrame());
								}
								if (config.containsConfigEntry("POKKIT2")) //$NON-NLS-1$
								{
									MFSPrintingMethods.pokkit2(myWrkUnitD.getMctl(),
											nmbr, type, config.get8CharCellType(), 1,
											getParentFrame());
								}
							}
							index++;
						}
						getParentFrame().setCursor(Cursor.getDefaultCursor());
					}
				}
				else
				{
					errorString = rtv_mo.getErms();
				}
			}

			if (rc != 0)
			{
				IGSMessageBox.showOkMB(getParentFrame(), null, errorString, null);
			}
		}
		catch (Exception e)
		{
			getParentFrame().setCursor(Cursor.getDefaultCursor());
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
		finally
		{
			addMyListeners();
		}
	}

	/** Invoked when {@link #pbMachineIndexCard} is selected. */
	private void machIndexCard()
	{
		try
		{
			MFSPrintingMethods.machIndexCard(this.fieldHeaderRec.getMfgn(),
					this.fieldHeaderRec.getIdss(), this.fieldHeaderRec.getOrno(),
					this.fieldHeaderRec.getMctl(), 1, getParentFrame());
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
	}

	/** Invoked when {@link #pbMachType} is selected. */
	private void machtype()
	{
		try
		{
			MFSPrintingMethods.machtype(this.fieldHeaderRec.getMctl(), 1,
					getParentFrame());
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
	}

	/** Invoked when {@link #pbMACID} is selected. */
	private void macId()
	{
		try
		{
			MFSPrintingMethods.macid(this.fieldHeaderRec.getMfgn(),
					this.fieldHeaderRec.getIdss(), this.fieldHeaderRec.getOrno(),
					this.fieldHeaderRec.getMctl(), 1, getParentFrame());

		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
	}

	/** Invoked when {@link #pbMainID} is selected. */
	private void mainid()
	{
		try
		{
			MFSPrintingMethods.mainid(this.fieldHeaderRec.getMctl(), 1, getParentFrame());
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
	}

	/** Invoked when {@link #pbMatpMcsn} is selected. */
	private void matpmcsn()
	{
		try
		{
			final MFSConfig config = MFSConfig.getInstance();
			MFSComponentRec next;
			int index = 0;
			boolean print1 = false;
			boolean print2 = false;
			boolean rebrand = false;
			int rc = 0;
			String errorString = ""; //$NON-NLS-1$
			String matp = ""; //$NON-NLS-1$
			String mmdl = ""; //$NON-NLS-1$
			String mspi = ""; //$NON-NLS-1$
			String mcsn = ""; //$NON-NLS-1$
			String prln = ""; //$NON-NLS-1$
			String mctl = ""; //$NON-NLS-1$

			/* Check config entry if we should print the REBRAND labels */
			StringBuffer cnfgDatRebrandPrint = new StringBuffer();
			cnfgDatRebrandPrint.append("REBRAND,"); //$NON-NLS-1$
			cnfgDatRebrandPrint.append(this.fieldHeaderRec.getNmbr());
			cnfgDatRebrandPrint.append(","); //$NON-NLS-1$
			cnfgDatRebrandPrint.append(this.fieldHeaderRec.getPrln().trim());

			String valRebrandPrint = config.getConfigValue(cnfgDatRebrandPrint.toString());
			if (valRebrandPrint.equals(MFSConfig.NOT_FOUND))
			{
				String cnfgDatRebrand2 = "REBRAND," + this.fieldHeaderRec.getNmbr() + ",*ALL"; //$NON-NLS-1$ //$NON-NLS-2$
				valRebrandPrint = config.getConfigValue(cnfgDatRebrand2);
			}

			if (!valRebrandPrint.equals(MFSConfig.NOT_FOUND))
			{
				rebrand = true;
				int qty = 1;
				if (!valRebrandPrint.equals("")) //$NON-NLS-1$
				{
					qty = Integer.parseInt(valRebrandPrint);
				}

				/* Call RTVREBRAND transaction to get rebrand data */
				MfsXMLDocument xml_rebranddata = new MfsXMLDocument("RTVREBRAND"); //$NON-NLS-1$
				xml_rebranddata.addOpenTag("DATA"); //$NON-NLS-1$
				prln = this.fieldHeaderRec.getPrln();
				xml_rebranddata.addCompleteField("PRLN", prln); //$NON-NLS-1$
				xml_rebranddata.addCloseTag("DATA"); //$NON-NLS-1$
				xml_rebranddata.finalizeXML();

				MFSTransaction rtvrebrand = new MFSXmlTransaction(xml_rebranddata.toString());
				MFSComm.getInstance().execute(rtvrebrand, this);
				rc = rtvrebrand.getReturnCode();
				
				MfsXMLParser xmlParser = new MfsXMLParser(rtvrebrand.getOutput());

				try
				{
					// Get the error message if a bad rc is returned from the transaction.
					if (rc != 0)
					{
						errorString = xmlParser.getField("ERMS"); //$NON-NLS-1$
					}
					// Else it's a good rc
					else
					{
						// Get the Rebrand MachineType and Model values.
						matp = xmlParser.getField("MATP").trim(); //$NON-NLS-1$
						mmdl = xmlParser.getField("MMDL").trim(); //$NON-NLS-1$

						// Get the MCTL to get the cooc value
						mctl = this.fieldHeaderRec.getMctl().trim();

						/* Then display dialog box to scan in 1S barcode */
						MFSRebrandDialog myRD = new MFSRebrandDialog(getParentFrame(),
								mmdl, matp, prln, mctl, qty);
						myRD.setLocationRelativeTo(getParentFrame()); //~7A
						myRD.setVisible(true);

					}/* end of good rc */
				}
				catch (mfsxml.MISSING_XML_TAG_EXCEPTION e)
				{
					rc = 10;
					errorString = "Missing XML Tag Error in REBRAND Label Print"; //$NON-NLS-1$
				}

				// If not a good rc, then display the error message
				if (rc != 0)
				{
					IGSMessageBox.showOkMB(getParentFrame(), null, errorString, null);
				}
			}// end if REBRAND is configured

			/* If the rebrand labels aren't printed, then print the normal labels */
			if (!rebrand)
			{
				/* Check if print config entries are setup */
				if (config.containsConfigEntry("MATPMCSN")) //$NON-NLS-1$
				{
					print1 = true;
				}
				if (config.containsConfigEntry("MATPMCSN1S")) //$NON-NLS-1$
				{
					print2 = true;
				}

				/* Loop thru components and check for AMSI flag = blank or zero */
				while (index < this.fieldDirWork.getComponentListModelSize())
				{
					next = this.fieldDirWork.getComponentListModelCompRecAt(index);
					if (!next.getAmsi().equals(" ") & !next.getAmsi().equals("0")) //$NON-NLS-1$ //$NON-NLS-2$
					{
						if (print1)
						{
							// print configured node label with given matp, model, & snum
							MFSPrintingMethods.matpmcsn(next.getMatp(), next.getMmdl(), 
									next.getMspi() + "-" + next.getMcsn().substring(2, 7), //$NON-NLS-1$
									1, getParentFrame());
						}
						if (print2)
						{
							// print 2nd configured node label with given matp,model,mspi & snum
							MFSPrintingMethods.matpmcsn1s(next.getMatp(), next.getMmdl(),
									next.getMspi(), next.getMcsn(), 1, getParentFrame());
						}
					}
					index++;
				}

				// Now print labels for additional serialized parts if there are
				// any returned from the RTV_IMSN transaction.
				MfsXMLDocument xml_data1 = new MfsXMLDocument("RTV_IMSN"); //$NON-NLS-1$
				xml_data1.addOpenTag("DATA"); //$NON-NLS-1$
				xml_data1.addCompleteField("MCTL", this.fieldHeaderRec.getMctl()); //$NON-NLS-1$
				xml_data1.addCompleteField("INPN", "            "); //$NON-NLS-1$ //$NON-NLS-2$
				xml_data1.addCompleteField("INSQ", "            "); //$NON-NLS-1$ //$NON-NLS-2$
				xml_data1.addCloseTag("DATA");//$NON-NLS-1$
				xml_data1.finalizeXML();

				MFSTransaction rtv_imsn = new MFSXmlTransaction(xml_data1.toString());
				MFSComm.getInstance().execute(rtv_imsn, this);
				rc = rtv_imsn.getReturnCode();

				// Parse the data returned
				MfsXMLParser xmlParser = new MfsXMLParser(rtv_imsn.getOutput());

				try
				{
					// Get the error message if a bad rc is returned
					if (rc != 0)
					{
						errorString = xmlParser.getField("ERMS"); //$NON-NLS-1$
					}
					// Else it's a good rc
					else
					{
						// Parse out the data returned for each serialized part
						MfsXMLParser serialParser = new MfsXMLParser(xmlParser.getNextField("REC")); //$NON-NLS-1$

						while (!serialParser.getUnparsedXML().equals("")) //$NON-NLS-1$
						{
							matp = serialParser.getField("MATP"); //$NON-NLS-1$
							mmdl = serialParser.getField("MMDL"); //$NON-NLS-1$
							mspi = serialParser.getField("MSPI"); //$NON-NLS-1$
							mcsn = serialParser.getField("MCSN"); //$NON-NLS-1$

							if (print1)
							{
								// print first configured node label with given matp,model,mspi & snum 
								MFSPrintingMethods.matpmcsn(matp, mmdl, 
										mspi + "-" + mcsn.substring(2, 7), //$NON-NLS-1$
										1, getParentFrame());
							}

							if (print2)
							{
								// print 2nd configured node label with given matp,model,mspi & snum
								MFSPrintingMethods.matpmcsn1s(matp, mmdl, mspi, mcsn, 1, getParentFrame());
							}

							// Get the next record
							serialParser = new mfsxml.MfsXMLParser(xmlParser.getNextField("REC")); //$NON-NLS-1$
						}
					}/* end of good rc */
				}
				catch (mfsxml.MISSING_XML_TAG_EXCEPTION e)
				{
					rc = 10;
					errorString = "Missing XML Tag Error in MATPMCSN1S Label Print"; //$NON-NLS-1$
				}

				// If not a good rc, then display the error message
				if (rc != 0)
				{
					IGSMessageBox.showOkMB(getParentFrame(), null, errorString, null);
				}
			}//end if not rebrand
			getParentFrame().setCursor(Cursor.getDefaultCursor());
		}//end try
		catch (Exception e)
		{
			getParentFrame().setCursor(Cursor.getDefaultCursor());
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
	}

	/** Invoked when {@link #pbMesMFI} is selected. */
	private void mesmfi()
	{
		// create a MFSTracksubReprintDialog, and fill the component list model
		// with those parts that are valid to reprint the Tracksub3 label
		MFSTracksubReprintDialog myReprintJD = new MFSTracksubReprintDialog(getParentFrame());
		MFSComponentRec next;
		boolean found = false;

		for (int index = 0; index < this.fieldCompListModel.size(); index++)
		{
			next = this.fieldCompListModel.getComponentRecAt(index);
			if ((next.getExpi().equals("1")) && (next.getIdsp().equals("I"))) //$NON-NLS-1$ //$NON-NLS-2$
			{
				myReprintJD.getReprintCompListModel().addElement(next);
				found = true;
			}
		}
		if (!found)
		{
			String erms = "You cannot reprint any MESMFI labels at this time"; //$NON-NLS-1$
			IGSMessageBox.showOkMB(getParentFrame(), null, erms, null);
		}
		else
		{
			/* now show the tracksubreprint dialog */
			myReprintJD.loadReprintListModel();
			myReprintJD.setLocationRelativeTo(getParentFrame()); //~7A
			myReprintJD.setVisible(true);
		}
	}

	/** Invoked when {@link #pbMESShipGroup} is selected. */
	private void mesShipGrpLbl()
	{
		try
		{
			MFSPrintingMethods.mesShipGrpLbl(this.fieldHeaderRec.getMfgn(),
					this.fieldHeaderRec.getIdss(), this.fieldHeaderRec.getOrno(),
					this.fieldHeaderRec.getMctl(), 1, getParentFrame());
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
	}

	/** Invoked when {@link #pbMir} is selected. */
	private void mir()
	{
		try
		{
			MFSPrintingMethods.mir(MFSConfig.getInstance().get8CharCellType(),
					"00000" + this.fieldHeaderRec.getPrln().substring(0, 7), //$NON-NLS-1$ 
					this.fieldHeaderRec.getMctl(), 1, getParentFrame());

		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
	}
	
	//~14A
	/** Invoked when {@link #pbOnDemand} is selected. */
	private void onDemandReprint()
	{
		try
		{
			this.removeMyListeners();
			
			// Create key data for label printing
			MFSOnDemandKeyData odKeyData = new MFSOnDemandKeyData();
			odKeyData.setTriggerSource("DWEXTFUNC"); //$NON-NLS-1$
			odKeyData.setTriggerKey("*NONE"); //$NON-NLS-1$
			odKeyData.setDataSource(this.fieldHeaderRec);
			
			// Start label printing process
			MFSOnDemand odLabel = new MFSOnDemand(getParentFrame(), odKeyData);
			odLabel.labelReprint();
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
		finally
		{
			this.addMyListeners();
		}
	}	
	
	//~15A
	/** Invoked when {@link #pbFodLabel} is selected. */
	private void fodLabel()
	{
		try
		{
			MFSPrintingMethods.fodLabel(this.fieldHeaderRec.getMfgn(),this.fieldHeaderRec.getIdss(),
					                    this.fieldCurrMctl,"SAPRINTING",1,getParentFrame());  //$NON-NLS-1$
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
	}

	/** Invoked when {@link #pbPodKey} is selected. */
	private void podkey()
	{
		try
		{
			MFSPrintingMethods.podkey(this.fieldHeaderRec.getMfgn(), 
					this.fieldHeaderRec.getIdss(), 1, getParentFrame());
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
	}

	/** Invoked when {@link #pbProdFeatCode} is selected. */
	private void prodFeatCode()
	{
		try
		{
			MFSPrintingMethods.prodFeatCode(this.fieldHeaderRec.getMfgn(),
					this.fieldHeaderRec.getIdss(), this.fieldHeaderRec.getOrno(),
					this.fieldHeaderRec.getMctl(), 1, getParentFrame());
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
	}

	/** Invoked when {@link #pbProdPackLarge} is selected. */
	private void prodPackLrg()
	{
		try
		{
			MFSPrintingMethods.prodPackLrg(this.fieldHeaderRec.getMfgn(),
					this.fieldHeaderRec.getIdss(), this.fieldHeaderRec.getOrno(),
					this.fieldHeaderRec.getMctl(), 1, getParentFrame());
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
	}

	/** Invoked when {@link #pbProdPackSmall} is selected. */
	private void prodPackSm()
	{
		try
		{   
			MFSPrintingMethods.prodPackSm(this.fieldHeaderRec.getMfgn(),
				this.fieldHeaderRec.getIdss(), this.fieldHeaderRec.getOrno(),
				this.fieldHeaderRec.getMctl(), 1, getParentFrame());
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
	}

	/** Invoked when {@link #pbProdSub} is selected. */
	private void prodsub()
	{
		try
		{
			final MFSConfig config = MFSConfig.getInstance();
			if (config.containsConfigEntry("PRODSUB")) //$NON-NLS-1$
			{
				MFSPrintingMethods.prodsub(this.fieldHeaderRec.getPrln(), "J", //$NON-NLS-1$
						this.fieldCurrMctl, 1, getParentFrame());
			}
			if (config.containsConfigEntry("L3CACHE")) //$NON-NLS-1$
			{
				MFSPrintingMethods.l3cache(this.fieldHeaderRec.getPrln(), "J", //$NON-NLS-1$
						this.fieldCurrMctl, 1, getParentFrame());
			}
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
	}

	/** Invoked when {@link #pbRecvPart} is selected. */
	private void recvpart()
	{
		try
		{
			removeMyListeners();

			String data = "RTV_LB90  " + this.fieldCurrMctl + "*ALL"; //$NON-NLS-1$ //$NON-NLS-2$
			MFSTransaction rtv_lb90 = new MFSFixedTransaction(data);
			rtv_lb90.setActionMessage("Retrieving List of Parts, Please Wait..."); //$NON-NLS-1$
			MFSComm.getInstance().execute(rtv_lb90, this);

			if (rtv_lb90.getReturnCode() == 0)
			{
				if (!rtv_lb90.getOutput().equals("")) //$NON-NLS-1$
				{
					MFSRecvPartDialog recvPartJD = new MFSRecvPartDialog(getParentFrame());
					recvPartJD.loadRecvPartListModel(rtv_lb90.getOutput());
					recvPartJD.setLocationRelativeTo(getParentFrame()); //~7A
					recvPartJD.setVisible(true);

					if (recvPartJD.getPressedPrint())
					{
						ListModel model = recvPartJD.getLSTRecvPart().getModel();
						int index = 0;
						while (index < model.getSize())
						{
							if (recvPartJD.getLSTRecvPart().isSelectedIndex(index))
							{
								String listData = (String) model.getElementAt(index);
								String pn = listData.substring(3, 10);
								String qty = listData.substring(13, 16);

								getParentFrame().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
								MFSPrintingMethods.recvpart1(this.fieldCurrMctl,
										this.fieldHeaderRec.getNmbr(), pn, qty, 1,
										getParentFrame());
							}
							index++;
						}
						getParentFrame().setCursor(Cursor.getDefaultCursor());
					}
				}
				else
				{
					String erms = "No valid Part Numbers found."; //$NON-NLS-1$
					IGSMessageBox.showOkMB(getParentFrame(), null, erms, null);
				}
			}
			else
			{
				IGSMessageBox.showOkMB(getParentFrame(), null, rtv_lb90.getErms(), null);
			}
		}
		catch (Exception e)
		{
			getParentFrame().setCursor(Cursor.getDefaultCursor());
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
		finally
		{
			addMyListeners();
		}
	}

	/** Invokes <code>showOps(fieldViewOpsData, fieldViewOpsIdx);</code>. */
	public void reshowOps()
	{
		showOps(this.fieldViewOpsData, this.fieldViewOpsIdx);
	}

	/** Invoked when {@link #pbRochMes} is selected. */
	private void rochFCMes()
	{
		try
		{
			Object[] options = {"Summary", "Detail", "Both"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			Object n = JOptionPane.showInputDialog(getParentFrame(), "Choose One", //$NON-NLS-1$
					"Summary, Detail, or Both", JOptionPane.INFORMATION_MESSAGE, null, //$NON-NLS-1$
					options, options[1]);

			String type;
			if (n == options[0])
			{
				type = "S"; //$NON-NLS-1$
			}
			else if (n == options[1])
			{
				type = "D"; //$NON-NLS-1$
			}
			else
			{
				type = "B"; //$NON-NLS-1$
			}
			MFSPrintingMethods.rchfcmes(this.fieldCurrMctl, type, 1, getParentFrame());
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
	}

	/** Invoked when {@link #pbRochKit} is selected. */
	private void rochKit()
	{
		try
		{
			int rc = 0;
			String errorString = ""; //$NON-NLS-1$

			/* used for UPDT_PQ transaction, 155 astrix and 100 blanks */
			String astrix = "**********************************************************************************************************************************************************"; //$NON-NLS-1$
			String oprm = "                                                                                                    "; //$NON-NLS-1$
			removeMyListeners();

			MFSWorkUnitDialog myWrkUnitD = new MFSWorkUnitDialog(getParentFrame());
			myWrkUnitD.setTFWorkUnitText(this.fieldCurrMctl);
			myWrkUnitD.setLocationRelativeTo(getParentFrame()); //~7A
			myWrkUnitD.setVisible(true);

			if (myWrkUnitD.getPressedEnter())
			{
				String data = "RTV_MO    " + myWrkUnitD.getMctl(); //$NON-NLS-1$
				MFSTransaction rtv_mo = new MFSFixedTransaction(data);
				rtv_mo.setActionMessage("Retrieving List of Operations, Please Wait..."); //$NON-NLS-1$
				MFSComm.getInstance().execute(rtv_mo, this);
				rc = rtv_mo.getReturnCode();

				if (rc == 0)
				{
					MFSKittingOpDialog kittOpD = new MFSKittingOpDialog(getParentFrame());
					kittOpD.loadKittOpListModel(rtv_mo.getOutput());
					kittOpD.setLocationRelativeTo(getParentFrame()); //~7A
					kittOpD.setVisible(true);

					if (kittOpD.getPressedPrint())
					{
						int index = 0;
						while (rc == 0 && index < kittOpD.getKittOpListModelSize())
						{
							String listData = (String) kittOpD.getKittOpListModelElementAt(index);
							String type = listData.substring(13, 14);
							String nmbr = listData.substring(0, 4);
							if (!type.equals(" ")) //$NON-NLS-1$
							{
								/* now get the kittdflt entry for that operation */
								String default_value = MFSConfig.getInstance().getConfigValue("KITTDFLT," + nmbr); //$NON-NLS-1$
								if (!default_value.equals(MFSConfig.NOT_FOUND))
								{
									// look in the kittdflt entry for a 2nd comma, if found check for the loc or remote
									String loc_or_remote = ""; //$NON-NLS-1$
									int first_comma = default_value.indexOf(","); //$NON-NLS-1$
									int loc_index = default_value.indexOf(",", first_comma + 1); //$NON-NLS-1$
									/* if no loc found, use L to print locally */
									if (loc_index == -1)
									{
										loc_or_remote = "L"; //$NON-NLS-1$
									}
									else
									{
										loc_or_remote = default_value.substring(loc_index + 1, loc_index + 2);
									}

									if (loc_or_remote.equals("L") || loc_or_remote.equals("B")) //$NON-NLS-1$ //$NON-NLS-2$
									{
										getParentFrame().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
										MFSPrintingMethods.rchkit(myWrkUnitD.getMctl(),
												nmbr, type, 1, getParentFrame());
									}
									if (loc_or_remote.equals("R") || loc_or_remote.equals("B")) //$NON-NLS-1$ //$NON-NLS-2$
									{
										/* trigger kitting2 on host */
										final MFSConfig config = MFSConfig.getInstance();
										StringBuffer updtPqInput = new StringBuffer(384);
										updtPqInput.append("UPDT_PQ   "); //$NON-NLS-1$
										updtPqInput.append(astrix);
										updtPqInput.append("       "); //$NON-NLS-1$
										updtPqInput.append("    "); //$NON-NLS-1$
										updtPqInput.append(myWrkUnitD.getMctl());
										updtPqInput.append("        "); //$NON-NLS-1$
										updtPqInput.append(nmbr);
										updtPqInput.append("00001"); //$NON-NLS-1$
										updtPqInput.append("KITTING2 "); //$NON-NLS-1$
										updtPqInput.append(config.get8CharCell());
										updtPqInput.append(config.getConfigValue("USER").concat("           ").substring(0,10)); //$NON-NLS-1$ //$NON-NLS-2$
										updtPqInput.append(type);
										updtPqInput.append(oprm);

										MFSTransaction updt_pq = new MFSFixedTransaction(updtPqInput.toString());
										updt_pq.setActionMessage("Updating FCSPPQ10, Please Wait..."); //$NON-NLS-1$
										MFSComm.getInstance().execute(updt_pq, this);
										rc = updt_pq.getReturnCode();
										if (rc != 0)
										{
											errorString = updt_pq.getErms();
										}
										getParentFrame().setCursor(Cursor.getDefaultCursor());
									}
								} /* end check for not found - kittdflt */
								else
								{
									StringBuffer erms = new StringBuffer(64);
									erms.append("KITTDFLT not found for operation: "); //$NON-NLS-1$
									erms.append(nmbr);
									erms.append("in the configuration file"); //$NON-NLS-1$
									IGSMessageBox.showOkMB(getParentFrame(), null, erms.toString(), null);
								}
							}/* end non-blank type */
							index++;
						}
						getParentFrame().setCursor(Cursor.getDefaultCursor());
					}
				}
				else
				{
					errorString = rtv_mo.getErms();
				}
			}
			if (rc != 0)
			{
				IGSMessageBox.showOkMB(getParentFrame(), null, errorString, null);
			}
		}
		catch (Exception e)
		{
			getParentFrame().setCursor(Cursor.getDefaultCursor());
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
		finally
		{
			addMyListeners();
		}
	}

	/** Invoked when {@link #pbRochPackList} is selected. */
	private void rochPackList()
	{
		try
		{
			Object[] options = {"Summary", "Detail", "Both"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			Object n = JOptionPane.showInputDialog(getParentFrame(), "Choose One", //$NON-NLS-1$
					"Summary, Detail, or Both", JOptionPane.INFORMATION_MESSAGE, null, //$NON-NLS-1$
					options, options[1]);

			String type;
			if (n == options[0])
			{
				type = "S"; //$NON-NLS-1$
			}
			else if (n == options[1])
			{
				type = "D"; //$NON-NLS-1$
			}
			else
			{
				type = "B"; //$NON-NLS-1$
			}
			MFSPrintingMethods.rchshpgrp(this.fieldCurrMctl, type, 1, getParentFrame());
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
	}

	/** Invoked when {@link #pbRtvLabel} is selected. */
	private void rtvLabel()
	{
		try
		{
			final MFSConfig config = MFSConfig.getInstance();
			if (config.containsConfigEntry("WUIPID")) //$NON-NLS-1$
			{
				MFSPrintingMethods.wuipid(this.fieldCurrMctl, 1, getParentFrame());
			}
			else if (config.containsConfigEntry("RTVFFBM1")) //$NON-NLS-1$
			{
				MFSPrintingMethods.rtvffbm(this.fieldCurrMctl, 1, getParentFrame());
			}
			else if (config.containsConfigEntry("ROCHWU")) //$NON-NLS-1$
			{
				MFSPrintingMethods.rochwu(this.fieldCurrMctl, this.fieldHeaderRec.getMfgn(),
						this.fieldHeaderRec.getIdss(), 1, getParentFrame());
			}
			else if (config.containsConfigEntry("SYSMCTL")) //$NON-NLS-1$
			{
				MFSPrintingMethods.sysmctl(this.fieldHeaderRec.getIdss(),
						this.fieldHeaderRec.getMfgn(), this.fieldCurrMctl,
						this.fieldHeaderRec.getMatp(), this.fieldHeaderRec.getMspi(),
						this.fieldHeaderRec.getMcsn(), 1, getParentFrame());
			}
			else if (config.containsConfigEntry("11SWU")) //$NON-NLS-1$
			{
				if (config.containsConfigEntry("SMALLWU") //$NON-NLS-1$
						&& this.fieldHeaderRec.getAwsa().equalsIgnoreCase("Y")) //$NON-NLS-1$
				{
					MFSPrintingMethods.smallwu(this.fieldCurrMctl, 1, getParentFrame());
				}
				else
				{
					MFSPrintingMethods.elevenswu(this.fieldHeaderRec.getPrln(), "J", //$NON-NLS-1$
							this.fieldCurrMctl, 1, getParentFrame());
				}
			}
			else if (config.containsConfigEntry("SMALLWU")) //$NON-NLS-1$
			{
				MFSPrintingMethods.smallwu(this.fieldCurrMctl, 1, getParentFrame());
			}
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
	}

	/** Invoked when {@link #pbRtvMctl} is selected. */
	private void rtvMctl()
	{
		try
		{
			removeMyListeners();
			MFSRtvMctlDialog myRtvMctlJD = new MFSRtvMctlDialog(getParentFrame());
			myRtvMctlJD.setLocationRelativeTo(getParentFrame()); //~7A
			myRtvMctlJD.setVisible(true);
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
		finally
		{
			addMyListeners();
			this.pbRtvMctl.requestFocusInWindow();
		}
	}

	/** Invoked when {@link #pbRtvMctl2} is selected. */
	private void rtvMctl2()
	{
		try
		{
			removeMyListeners();
			MFSRtvMctl2Dialog myRtvMctl2D = new MFSRtvMctl2Dialog(getParentFrame());
			myRtvMctl2D.setLocationRelativeTo(getParentFrame()); //~7A
			myRtvMctl2D.setVisible(true);
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
		finally
		{
			addMyListeners();
			this.pbRtvMctl2.requestFocusInWindow();
		}
	}

	/** Invoked when {@link #pbScsi} is selected. */
	private void scsilabel()
	{
		try
		{
			MFSPrintingMethods.rtvscsi(this.fieldCurrMctl, 1, getParentFrame());
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
	}

	/** Invoked when {@link #pbShipTag} is selected. */
	private void shiptag()
	{
		try
		{
			MFSPrintingMethods.sysinfo(this.fieldCurrMctl, 1, getParentFrame());
			MFSPrintingMethods.sysnumb(this.fieldCurrMctl, 1, getParentFrame());
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
	}

	/**
	 * Shows the instructions for an operation.
	 * @param data the list of operations (RTV_OPS transaction output)
	 * @param index the selected list index
	 */
	private void showOps(String data, int index)
	{
		try
		{
			MFSOperationsDialog operJD = new MFSOperationsDialog(getParentFrame());
			operJD.loadOperListModel(data, this.fieldDirWork.getHeaderRec().getNmbr());
			if (operJD.getListSize() == 0)
			{
				String message = "Current Operation is the Only Operation that Exists."; //$NON-NLS-1$
				IGSMessageBox.showOkMB(getParentFrame(), null, message, null);
			}
			else
			{
				operJD.setSelectedIndex(index);
				operJD.setLocationRelativeTo(getParentFrame()); //~7A
				operJD.setVisible(true);

				if (operJD.getProceedSelected())
				{
					this.fieldViewOpsData = data;
					this.fieldViewOpsIdx = operJD.getSelectedIndex();

					StringBuffer data1 = new StringBuffer(32);
					data1.append("RTV_WUCR  "); //$NON-NLS-1$
					data1.append(this.fieldCurrMctl);
					data1.append(operJD.getSelectedListOption());
					data1.append("J"); //$NON-NLS-1$

					MFSTransaction rtv_wucr = new MFSFixedTransaction(data1.toString());
					rtv_wucr.setActionMessage("Retrieving Component Records, Please Wait..."); //$NON-NLS-1$
					MFSComm.getInstance().execute(rtv_wucr, this);

					if (rtv_wucr.getReturnCode() == 0)
					{
						MFSViewOpsPanel viewOpsPanel = new MFSViewOpsPanel(getParentFrame(), this);
						MFSHeaderRec viewOpsHeadRec = viewOpsPanel.loadListModel(rtv_wucr.getOutput());
						
						
						viewOpsPanel.loadPartsModels();
						/* Get correct collection data in order to use them at remove step  ~16A*/
						if(this.fieldHeaderRec.isCollectRequired())
						{
							viewOpsHeadRec.setCollectRequired("Y"); //$NON-NLS-1$
						}
						viewOpsHeadRec.setDataCollection(this.fieldHeaderRec.getDataCollection());
						MFSConfig config = MFSConfig.getInstance();

						int rc = 0;
						
						// ~12 do not call RTV_INSTR
						if (!config.containsConfigEntry("EFFICIENCYON*DUMBCLIENT")) //$NON-NLS-1$
						{
							MfsXMLDocument xml_data2 = new MfsXMLDocument("RTV_INSTR"); //$NON-NLS-1$
							xml_data2.addOpenTag("DATA"); //$NON-NLS-1$
							xml_data2.addCompleteField("MCTL", this.fieldCurrMctl); //$NON-NLS-1$
							xml_data2.addCompleteField("NMBR", viewOpsHeadRec.getNmbr()); //$NON-NLS-1$
							xml_data2.addCompleteField("PRLN", //$NON-NLS-1$
									viewOpsHeadRec.getPrln().concat("        ").substring(0, 8)); //$NON-NLS-1$
							xml_data2.addCompleteField("PROD", //$NON-NLS-1$
									viewOpsHeadRec.getProd().concat("        ").substring(0, 8)); //$NON-NLS-1$
							xml_data2.addCompleteField("USER", config.get8CharUser());//$NON-NLS-1$
							xml_data2.addCompleteField("CELL", config.get8CharCell()); //$NON-NLS-1$
							xml_data2.addCompleteField("CTYP", config.get8CharCellType()); //$NON-NLS-1$
							xml_data2.addCompleteField("DSPO", "Y"); //$NON-NLS-1$ //$NON-NLS-2$
							xml_data2.addCompleteField("OLEV", viewOpsHeadRec.getOlev()); //$NON-NLS-1$
							xml_data2.addCloseTag("DATA"); //$NON-NLS-1$
							xml_data2.finalizeXML();
	
							MFSTransaction rtv_instr = new MFSXmlTransaction(xml_data2.toString());
							rtv_instr.setActionMessage("Retrieving Instructions, Please Wait..."); //$NON-NLS-1$
							MFSComm.getInstance().execute(rtv_instr, this);
							rc = rtv_instr.getReturnCode();
								
							if (rc == 0)
							{
								MfsXMLParser xmlParser = new MfsXMLParser(rtv_instr.getOutput());
								viewOpsPanel.loadInstructions(xmlParser);
							}
							//Bad return code from RTV_INSTR
							else
							{
								IGSMessageBox.showOkMB(getParentFrame(), null, rtv_instr.getErms(), null);
								this.pbViewOps.requestFocusInWindow();
							}
						}
						
						if (rc == 0)
						{
							viewOpsPanel.setupPartInstPanel();
							viewOpsPanel.configureButtons();
							viewOpsPanel.prepareForDisplay(this.fieldCurrMctl);

							viewOpsPanel.disableInstructionScrollRectToVisible();
							getParentFrame().displayMFSPanel(viewOpsPanel);
							viewOpsPanel.enableInstructionScrollRectToVisible();
						}
					}
					//Bad return code from rtv_wucr
					else
					{
						IGSMessageBox.showOkMB(getParentFrame(), null, rtv_wucr.getErms(), null);
						this.pbViewOps.requestFocusInWindow();
					}
				}
			}
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
	}
	
	/** Invoked when {@link #pbSiemens} is selected. */
	private void siemens()
	{
		try
		{
			final MFSConfig config = MFSConfig.getInstance();
			if (config.containsConfigEntry("SIEMENSS")) //$NON-NLS-1$
			{
				MFSPrintingMethods.siemenss(this.fieldHeaderRec.getMctl(), 1, getParentFrame());
			}
			if (config.containsConfigEntry("SIEMENSL")) //$NON-NLS-1$
			{
				MFSPrintingMethods.siemensl(this.fieldHeaderRec.getMctl(), 1, getParentFrame());
			}
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
	}

	/** Invoked when {@link #pbSoftKey} is selected. */
	private void softwareKey()
	{
		try
		{
			MFSPrintingMethods.softwarekey(this.fieldHeaderRec.getMfgn(),
					this.fieldHeaderRec.getIdss(), this.fieldHeaderRec.getMctl(), 1,
					getParentFrame());
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
	}

	//~1A
	/** Invoked when {@link #pbSoftwareKeycode} is selected. */
	private void softwareKeycode()
	{
		try
		{
			MFSPrintingMethods.softKeycode(this.fieldHeaderRec.getMctl(), "R", 1, getParentFrame()); //$NON-NLS-1$
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
	}

	/** Invoked when {@link #pbSpd11s} is selected. */
	private void spd11s()
	{
		try
		{
			MFSPrintingMethods.spd11s(this.fieldHeaderRec.getPrln(), 
					this.fieldHeaderRec.getMctl(), 1, getParentFrame());
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
	}

	/** Invoked when {@link #pbSpd11sText} is selected. */
	private void spd11stext()
	{
		try
		{
			MFSPrintingMethods.spd11stext(this.fieldHeaderRec.getMctl(), 1, getParentFrame());
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
	}

	/** Invoked when {@link #pbSysPWD} is selected. */
	private void sysPWD()
	{
		try
		{
			MFSPrintingMethods.systempassword(this.fieldHeaderRec.getMfgn(),
					this.fieldHeaderRec.getIdss(), this.fieldHeaderRec.getMctl(),
					this.fieldHeaderRec.getCmat(), this.fieldHeaderRec.getMmdl(), 1,
					getParentFrame());

		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
	}

	/** Invoked when {@link #pbTCODBilling} is selected. */
	private void tcodBilling()
	{
		try
		{
			MFSPrintingMethods.tcodBilling(this.fieldHeaderRec.getMctl(),
					this.fieldHeaderRec.getMfgn(), this.fieldHeaderRec.getIdss(),
					this.fieldHeaderRec.getOrno(), this.fieldHeaderRec.getMatp(),
					this.fieldHeaderRec.getMmdl(), this.fieldHeaderRec.getMspi(),
					this.fieldHeaderRec.getMcsn(), 1, getParentFrame());
			
			MFSPrintingMethods.tcodMinutes(this.fieldHeaderRec.getMctl(),			/*~9A*/
					this.fieldHeaderRec.getMfgn(), this.fieldHeaderRec.getIdss(),	/*~9A*/
					this.fieldHeaderRec.getOrno(), this.fieldHeaderRec.getMatp(),	/*~9A*/
					this.fieldHeaderRec.getMmdl(), this.fieldHeaderRec.getMspi(),	/*~9A*/
					this.fieldHeaderRec.getMcsn(), 1, getParentFrame());			/*~9A*/
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
	}

	/** Invoked when {@link #pbTCODPrepay} is selected. */
	private void tcodPrepay()
	{
		try
		{
			MFSPrintingMethods.tcodPrepay(this.fieldHeaderRec.getMctl(),
					this.fieldHeaderRec.getMfgn(), this.fieldHeaderRec.getIdss(),
					this.fieldHeaderRec.getOrno(), this.fieldHeaderRec.getMatp(),
					this.fieldHeaderRec.getMmdl(), this.fieldHeaderRec.getMspi(),
					this.fieldHeaderRec.getMcsn(), 1, getParentFrame());
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
	}

	/** Invoked when {@link #pbTrackSub} is selected. */
	private void tracksub()
	{
		try
		{
			MFSPrintingMethods.tracksub(this.fieldHeaderRec.getPrln(), "J", //$NON-NLS-1$
					this.fieldCurrMctl, 1, getParentFrame());
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
	}

	/** Invoked when {@link #pbTravel} is selected. */
	private void travel()
	{
		try
		{
			MFSPrintingMethods.rtvtravel(this.fieldCurrMctl, 1, getParentFrame());
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
	}
	
	//~17A
	/** Invoked when {@link #pbVendorPartsLogging} is selected */
	private void vendorPartsLogging()
	{
		try
		{
			removeMyListeners();
			MFSVendorSubPartsController vspLogging = new MFSVendorSubPartsController(getParentFrame(), false);
			vspLogging.displayVendorSubassemblyPartsDialog(null, null);
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
		finally
		{
			addMyListeners();
		}
	}
	/** Invoked when {@link #pbViewOps} is selected. */
	private void viewOps()
	{
		try
		{
			removeMyListeners();

			MfsXMLDocument xml_data1 = new MfsXMLDocument("RTV_OPS"); //$NON-NLS-1$
			xml_data1.addOpenTag("DATA"); //$NON-NLS-1$
			xml_data1.addCompleteField("MCTL", this.fieldCurrMctl); //$NON-NLS-1$
			xml_data1.addCloseTag("DATA"); //$NON-NLS-1$
			xml_data1.finalizeXML();

			MFSTransaction rtv_ops = new MFSXmlTransaction(xml_data1.toString());
			rtv_ops.setActionMessage("Retrieving List of Operations, Please Wait..."); //$NON-NLS-1$
			MFSComm.getInstance().execute(rtv_ops, this);

			if (rtv_ops.getReturnCode() == 0)
			{
				showOps(rtv_ops.getOutput(), 0);
			}
			else
			{
				IGSMessageBox.showOkMB(getParentFrame(), null, rtv_ops.getErms(), null);
			}
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
		finally
		{
			addMyListeners();
			this.pbViewOps.requestFocusInWindow();
		}
	}

	/** Invoked when {@link #pbVpdLbl} is selected. */
	private void vpdLbl()
	{
		/*
		 * this method will be executed almost exactly the same as the vpdBurn
		 * function from the DirectWork class, the only difference will be there
		 * will be no call to Mike's dll, we will only print the labels
		 */
		try
		{
			getParentFrame().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			MFSVPDProc mifprocessor = new MFSVPDProc(this.fieldHeaderRec, getParentFrame(), this);
			mifprocessor.processMifReprint();
			getParentFrame().setCursor(Cursor.getDefaultCursor());
		}
		catch (Exception e)
		{
			getParentFrame().setCursor(Cursor.getDefaultCursor());
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
	}

	/** Invoked when {@link #pbWWID} is selected. */
	private void wwid()
	{
		try
		{
			int rc = MFSPrintingMethods.wwid(this.fieldHeaderRec.getMatp(),
					this.fieldHeaderRec.getMmdl(), this.fieldHeaderRec.getMspi(),
					this.fieldHeaderRec.getMcsn().substring(2), 
					MFSConfig.getInstance().get8CharUser(), 
					"N", 1, getParentFrame()); //$NON-NLS-1$

			if (rc == 0)
			{
				this.fieldHeaderRec.setWwid("WWIDAssigned    "); //$NON-NLS-1$
			}
			else if (rc == 666)
			{
				this.fieldHeaderRec.setWwid("WWIDNotNeeded   "); //$NON-NLS-1$
			}
			else
			{
				this.fieldHeaderRec.setWwid("WWIDError       "); //$NON-NLS-1$
			}
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
	}

	//~18A
	/** Invoked when {@link #pbPartWeight} is selected. */
	private void partWeight()
	{		
		
		try
		{
			removeMyListeners();
			MFSStandAlonePartWeightDialog partWeightDialog = new MFSStandAlonePartWeightDialog(getParentFrame(), this);
			partWeightDialog.display();
			
			
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
		finally
		{
			addMyListeners();
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
			if (source == this.pbViewOps)
			{
				viewOps();
			}
			else if (source == this.pbVpdLbl)
			{
				vpdLbl();
			}
			else if (source == this.pbDasdp)
			{
				dasdp();
			}
			else if (source == this.pbKitting)
			{
				kitting();
			}
			else if (source == this.pbRochKit)
			{
				rochKit();
			}
			else if (source == this.pbRochMes)
			{
				rochFCMes();
			}
			else if (source == this.pbRochPackList)
			{
				rochPackList();
			}
			else if (source == this.pbRtvMctl)
			{
				rtvMctl();
			}
			else if (source == this.pbRtvMctl2)
			{
				rtvMctl2();
			}
			else if (source == this.pbSoftKey)
			{
				softwareKey();
			}
			else if (source == this.pbRtvLabel)
			{
				rtvLabel();
			}
			else if (source == this.pbShipTag)
			{
				shiptag();
			}
			else if (source == this.pbMesMFI)
			{
				mesmfi();
			}
			else if (source == this.pbRecvPart)
			{
				recvpart();
			}
			else if (source == this.pbProdSub)
			{
				prodsub();
			}
			else if (source == this.pbTrackSub)
			{
				tracksub();
			}
			else if (source == this.pbMachType)
			{
				machtype();
			}
			else if (source == this.pbFru)
			{
				frunumb();
			}
			else if (source == this.pbConfNode)
			{
				confnode();
			}
			else if (source == this.pbConfSeri)
			{
				confseri();
			}
			else if (source == this.pbFabList)
			{
				fablist();
			}
			else if (source == this.pbTravel)
			{
				travel();
			}
			else if (source == this.pbScsi)
			{
				scsilabel();
			}
			else if (source == this.pbMir)
			{
				mir();
			}
			else if (source == this.pbSysPWD)
			{
				sysPWD();
			}
			else if (source == this.pbConfNodeR)
			{
				confnoder();
			}
			else if (source == this.pbPodKey)
			{
				podkey();
			}
			else if (source == this.pbSiemens)
			{
				siemens();
			}
			else if (source == this.pbTCODBilling)
			{
				tcodBilling();
			}
			else if (source == this.pbTCODPrepay)
			{
				tcodPrepay();
			}
			else if (source == this.pbAdvFeatModel)
			{
				advFeatModel();
			}
			else if (source == this.pbProdFeatCode)
			{
				prodFeatCode();
			}
			else if (source == this.pbDeconfigLocChart)
			{
				deconfigLoc();
			}
			else if (source == this.pbProdPackLarge)
			{
				prodPackLrg();
			}
			else if (source == this.pbProdPackSmall)
			{
				prodPackSm();
			}
			else if (source == this.pbMachineIndexCard)
			{
				machIndexCard();
			}
			else if (source == this.pbMESShipGroup)
			{
				mesShipGrpLbl();
			}
			else if (source == this.pbWWID)
			{
				wwid();
			}
			else if (source == this.pbMACID)
			{
				macId();
			}
			else if (source == this.pbSpd11s)
			{
				spd11s();
			}
			else if (source == this.pbMainID)
			{
				mainid();
			}
			else if (source == this.pbMatpMcsn)
			{
				matpmcsn();
			}
			else if (source == this.pbDisplayLoc)
			{
				displayLocs();
			}
			else if (source == this.pbKeyCodes)
			{
				keycodes();
			}
			else if (source == this.pbSpd11sText)
			{
				spd11stext();
			}
			else if (source == this.pbSoftwareKeycode)
			{
				softwareKeycode(); //~1A
			}
			else if (source == this.pbDwnBin11s)
			{
				dwnBin11s(); //~4A
			}
			else if (source == this.pbAssemblyLabel)
			{
				assemblyLbl(); //~5A
			}
			else if (source == this.pbCooCntr)
			{
				cooCntr(); //~11A
			}
			else if (source == this.pbOnDemand)
			{
				onDemandReprint(); //~14A
			}
			else if (source == this.pbFeatureOnDemand) // ~15A
			{
				fodLabel();
			}
			else if (source == this.pbVendorPartsLogging) //~17A
			{
				vendorPartsLogging();
			}
			else if (source == this.pbPartWeight) //~18A
			{
				partWeight();
			}
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
	}

	/**
	 * Invoked when a key is pressed.
	 * @param ke the <code>KeyEvent</code>
	 */
	public void keyPressed(KeyEvent ke)
	{
		final int keyCode = ke.getKeyCode();
		if (keyCode == KeyEvent.VK_ESCAPE)
		{
			getParentFrame().restorePreviousScreen(this);
		}
		else if (keyCode == KeyEvent.VK_ENTER && ke.getSource() instanceof MFSMenuButton)
		{
			MFSMenuButton button = (MFSMenuButton) ke.getSource();
			button.requestFocusInWindow();
			button.doClick();
		}
		/* F1 section */
		else if (keyCode == KeyEvent.VK_F1)
		{
			if (ke.isShiftDown())
			{
				this.pbRecvPart.requestFocusInWindow();
				this.pbRecvPart.doClick();
			}
			else if (ke.isControlDown())
			{
				this.pbConfNodeR.requestFocusInWindow();
				this.pbConfNodeR.doClick();
			}
		}
		/* F2 section */
		else if (keyCode == KeyEvent.VK_F2)
		{
			if (ke.isShiftDown())
			{
				this.pbProdSub.requestFocusInWindow();
				this.pbProdSub.doClick();
			}
			else if (ke.isControlDown())
			{
				this.pbPodKey.requestFocusInWindow();
				this.pbPodKey.doClick();
			}
			else
			{
				this.pbViewOps.requestFocusInWindow();
				this.pbViewOps.doClick();
			}
		}
		/* F3 section */
		else if (keyCode == KeyEvent.VK_F3)
		{
			if (ke.isShiftDown())
			{
				this.pbTrackSub.requestFocusInWindow();
				this.pbTrackSub.doClick();
			}
			else if (ke.isControlDown())
			{
				this.pbSiemens.requestFocusInWindow();
				this.pbSiemens.doClick();
			}
			else
			{
				if (this.pbVpdLbl.isEnabled())
				{
					this.pbVpdLbl.requestFocusInWindow();
					this.pbVpdLbl.doClick();
				}
				else
				{
					this.pbDasdp.requestFocusInWindow();
					this.pbDasdp.doClick();
				}
			}
		}
		/* F4 section */
		else if (keyCode == KeyEvent.VK_F4)
		{
			if (ke.isShiftDown())
			{
				this.pbMachType.requestFocusInWindow();
				this.pbMachType.doClick();
			}
			else if (ke.isControlDown())
			{
				this.pbTCODBilling.requestFocusInWindow();
				this.pbTCODBilling.doClick();
			}
			else
			{
				if (this.pbKitting.isEnabled())
				{
					this.pbKitting.requestFocusInWindow();
					this.pbKitting.doClick();
				}
				else
				{
					this.pbRochKit.requestFocusInWindow();
					this.pbRochKit.doClick();
				}
			}
		}
		/* F5 section */
		else if (keyCode == KeyEvent.VK_F5)
		{
			if (ke.isShiftDown())
			{
				this.pbFru.requestFocusInWindow();
				this.pbFru.doClick();
			}
			else if (ke.isControlDown())
			{
				this.pbTCODPrepay.requestFocusInWindow();
				this.pbTCODPrepay.doClick();
			}
			else
			{
				this.pbRochMes.requestFocusInWindow();
				this.pbRochMes.doClick();
			}
		}
		/* F6 section */
		else if (keyCode == KeyEvent.VK_F6)
		{
			if (ke.isShiftDown())
			{
				this.pbConfNode.requestFocusInWindow();
				this.pbConfNode.doClick();
			}
			else if (ke.isControlDown())
			{
				this.pbAdvFeatModel.requestFocusInWindow();
				this.pbAdvFeatModel.doClick();
			}
			else
			{
				this.pbRochPackList.requestFocusInWindow();
				this.pbRochPackList.doClick();
			}
		}
		/* F7 section */
		else if (keyCode == KeyEvent.VK_F7)
		{
			if (ke.isShiftDown())
			{
				this.pbConfSeri.requestFocusInWindow();
				this.pbConfSeri.doClick();
			}
			else if (ke.isControlDown())
			{
				this.pbProdFeatCode.requestFocusInWindow();
				this.pbProdFeatCode.doClick();
			}
			else
			{
				if (this.pbRtvMctl.isEnabled())
				{
					this.pbRtvMctl.requestFocusInWindow();
					this.pbRtvMctl.doClick();
				}
				else
				{
					this.pbRtvMctl2.requestFocusInWindow();
					this.pbRtvMctl2.doClick();
				}
			}
		}
		/* F8 section */
		else if (keyCode == KeyEvent.VK_F8)
		{
			if (ke.isShiftDown())
			{
				this.pbFabList.requestFocusInWindow();
				this.pbFabList.doClick();
			}
			else if (ke.isControlDown())
			{
				this.pbDeconfigLocChart.requestFocusInWindow();
				this.pbDeconfigLocChart.doClick();
			}
			else
			{
				this.pbSoftKey.requestFocusInWindow();
				this.pbSoftKey.doClick();
			}
		}
		/* F9 section */
		else if (keyCode == KeyEvent.VK_F9)
		{
			if (ke.isShiftDown())
			{
				this.pbTravel.requestFocusInWindow();
				this.pbTravel.doClick();
			}
			else if (ke.isControlDown())
			{
				this.pbProdPackLarge.requestFocusInWindow();
				this.pbProdPackLarge.doClick();
			}
			else
			{
				this.pbRtvLabel.requestFocusInWindow();
				this.pbRtvLabel.doClick();
			}
		}
		/* F10 section */
		else if (keyCode == KeyEvent.VK_F10)
		{
			if (ke.isShiftDown())
			{
				this.pbScsi.requestFocusInWindow();
				this.pbScsi.doClick();
			}
			else if (ke.isControlDown())
			{
				this.pbProdPackSmall.requestFocusInWindow();
				this.pbProdPackSmall.doClick();
			}
			else
			{
				this.pbShipTag.requestFocusInWindow();
				this.pbShipTag.doClick();
			}
		}
		/* F11 section */
		else if (keyCode == KeyEvent.VK_F11)
		{
			if (ke.isShiftDown())
			{
				this.pbMir.requestFocusInWindow();
				this.pbMir.doClick();
			}
			else if (ke.isControlDown())
			{
				this.pbMachineIndexCard.requestFocusInWindow();
				this.pbMachineIndexCard.doClick();
			}
		}
		/* F12 section */
		else if (keyCode == KeyEvent.VK_F12)
		{
			if (ke.isShiftDown())
			{
				this.pbSysPWD.requestFocusInWindow();
				this.pbSysPWD.doClick();
			}
			else if (ke.isControlDown())
			{
				this.pbMESShipGroup.requestFocusInWindow();
				this.pbMESShipGroup.doClick();
			}
			else
			{
				this.pbMesMFI.requestFocusInWindow();
				this.pbMesMFI.doClick();
			}
		}
		else if (ke.isControlDown())
		{
			/* CTRL A section */
			if (keyCode == KeyEvent.VK_A)
			{
				this.pbWWID.requestFocusInWindow();
				this.pbWWID.doClick();
			}
			/* CTRL B section */
			else if (keyCode == KeyEvent.VK_B)
			{
				this.pbMACID.requestFocusInWindow();
				this.pbMACID.doClick();
			}
			/* CTRL P section */
			else if (keyCode == KeyEvent.VK_P)
			{
				//~11A
				this.pbCooCntr.requestFocusInWindow();
				this.pbCooCntr.doClick();
			}
			/* CTRL 1 section */
			else if (keyCode == KeyEvent.VK_1)
			{
				this.pbSpd11s.requestFocusInWindow();
				this.pbSpd11s.doClick();
			}
			/* CTRL 2 section */
			else if (keyCode == KeyEvent.VK_2)
			{
				this.pbMainID.requestFocusInWindow();
				this.pbMainID.doClick();
			}
			/* CTRL 3 section */
			else if (keyCode == KeyEvent.VK_3)
			{
				this.pbMatpMcsn.requestFocusInWindow();
				this.pbMatpMcsn.doClick();
			}
			/* CTRL 4 section */
			else if (keyCode == KeyEvent.VK_4)
			{
				this.pbDisplayLoc.requestFocusInWindow();
				this.pbDisplayLoc.doClick();
			}
			/* CTRL 5 section */
			else if (keyCode == KeyEvent.VK_5)
			{
				this.pbKeyCodes.requestFocusInWindow();
				this.pbKeyCodes.doClick();
			}
			/* CTRL 6 section */
			else if (keyCode == KeyEvent.VK_6)
			{
				this.pbSpd11sText.requestFocusInWindow();
				this.pbSpd11sText.doClick();
			}
			/* CTRL 7 section ~1A */
			else if (keyCode == KeyEvent.VK_7)
			{
				this.pbSoftwareKeycode.requestFocusInWindow();
				this.pbSoftwareKeycode.doClick();
			}
			/* CTRL 8 section ~4A */
			else if (keyCode == KeyEvent.VK_8)
			{
				this.pbDwnBin11s.requestFocusInWindow();
				this.pbDwnBin11s.doClick();
			}
			/* CTRL 9 section ~5A */
			else if (keyCode == KeyEvent.VK_9)
			{
				this.pbAssemblyLabel.requestFocusInWindow();
				this.pbAssemblyLabel.doClick();
			}
			/* CTRL E section ~17A */
			else if (keyCode == KeyEvent.VK_E)
			{
				this.pbVendorPartsLogging.requestFocusInWindow();
				this.pbVendorPartsLogging.doClick();
			}
			/* CTRL L section ~14A */
			else if (keyCode == KeyEvent.VK_L)
			{
				this.pbOnDemand.requestFocusInWindow();
				this.pbOnDemand.doClick();
			}
			/* CTRL H section */  
			else if (keyCode == KeyEvent.VK_H) //~15A
			{
				this.pbFeatureOnDemand.requestFocusInWindow();
				this.pbFeatureOnDemand.doClick();
			}
			/* CTRL W section */  
			else if (keyCode == KeyEvent.VK_W) //~18A
			{
				this.pbPartWeight.requestFocusInWindow();
				this.pbPartWeight.doClick();
			}
		}
	}
}
