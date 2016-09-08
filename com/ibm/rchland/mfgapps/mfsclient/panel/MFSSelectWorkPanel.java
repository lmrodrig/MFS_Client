/* @ Copyright IBM Corporation 2003, 2016. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2003-10-22 @SW1          D Fichtinger     -Added additional logic to set a good activeRow - find first real part
 * 2004-5-17  @SW2 25455KK  Dan Kloepping    -Add WWNN rescan option
 * 2004-08-26 @SW3 27178RS  Dave Fichtinger  -add trigger of SPD11STEXT label
 * 2004-12-02      29776PT                   -Add APPLY to DCNFG_FAB params - method reapply
 *                                           -Add TEAR to DCNFG_FAB params - method teardown
 * 2005-03-30   @1 30686JM                   -Add target MCTL to DCFNG_FAB parms - method reapply
 * 2005-07-01   @2 25900JM                   -add reapply prep code
 *                                           -Take out BEST_MATCH choice - method reapply
 * 2005-09-08   @3 32178BP                   -add ctyp to brut_force and dcnfg_fab calls
 * 2005-09-09   @4 32263JM                   -make sure rowVector has a size
 * 2005-09-27   @5 31867EM                   -add software liscense label printing for new RSS products
 *                                           -add swSoftwareKeycodeTrx method
 * 2006-05-19 @SW4 29807JM  Blanca Aceves    -don't validate against IDSS anymore, removed IDSS parameter
 * 2006-06-21   ~6 34414JJ                   -Change how labels are fired in swContainerLabel method.
 *                                           -make some clean up with variables that are never used.
 * 2006-08-25   ~7 34544JM                   -Automate downbin process during reapply before str_wu.
 * 2006-09-19   ~8 35696FR  A Williams       -Performance changes along with general cleanup.
 * 2006-10-10   ~9 36523JM  M Barth          -Add reprint capability of downbin parts.
 * 2006-10-27  ~10 36704KM  R Prechel        -Fix comm usage in newWork
 *                                           -Inherit from MFSJPanel
 *                                           -Single constructor and removed main method
 *                                           -Removed initConnections and addMyKeyListeners
 *                                           -Use ButtonIterator
 * 2006-10-27  ~11 35927JM  R Prechel        -Remove DirectWorkJPanel.setConfig(getConfig()) calls
 * 2006-12-04  ~12 36977KM  M Barth          -Add check for return code=100 in downbin process
 * 2007-01-10  ~13 36302JM  M Barth          -New start work unit procedure(swAssemblyLabel).
 *                                           -Add call to swAssemblyLabel to startWorkUnitTransactions.
 * 2007-02-21  ~14 35531JR  VH Avila         -Add Dialog panel to ask if want continue with the FKIT process
 *                                            even if the WU has alteration pending
 * 2007-02-28  ~15 35603JM  Toribioh         -Instead of triggering the SYSTEMPWD call only the first  
 *                                            time thru an operation, make it fire on every time thru
 *                                            an operation
 * 2007-03-13  ~16 34242JR  R Prechel        -Java 5 version
 * 2007-04-02  ~17 38166JM  R Prechel        -Add setLocationRelativeTo for dialogs
 * 2007-04-02  ~18 38143JM  R Prechel        -Add call to swAssignIOSN to startWorkUnitTransactions
 * 2007-05-24  ~19 37676JM  R Prechel        -Execute LOGNETM on signoff for network measurement logging
 * 2007-07-12  ~20 39238JM  R Prechel        -Change network measurement logging message.
 * 2007-08-10  ~21 39464JM  R Prechel        -Fix parsing of RTV_CUODKY output
 * 2007-08-22  ~22 38131JL  VH Avila         -Print automatically the SYSCERT and CTRLNUMBR labels
 * 2008-01-08  ~23 40405FR  R Prechel        -Print automatically the TCODMINUTES Sheet
 * 2008-02-04  ~24 33401JM  R Prechel        -Convert F2 to change password
 * 2008-02-17  ~25 37616JL  D Pietrasik      -Add entity merge
 * 2008-04-07  ~26 37616JL  D Pietrasik      -Add user to rtv_cntr
 * 2008-04-22  ~27 40764EM  Santiago D       -Support for KEYCODELONG for hydra release 
 * 2008-06-24  ~28 41477EM  Martha Luna      -Added warning error into reapply when genereting keycodes failed.
 * 2008-07-14  ~29 38990JL  Santiago D       -Print automatically the CARRIERCOMN label
 * 2008-07-31  ~30 39375PP  Santiago D       -PRODPACKSM wu type validation
 * 2008-08-01  ~31 42330JM  Santiago D       -Add new Acknowledge container button and method
 * 2008-08-06      39375PPa  Santiago D       -Remove PRODPACKSM wu type validation    
 * 2008-08-18  ~32 42581AP  Santiago D       -Use CARRIERCOMNSTART instead of CARRIERCOMN config entry
 * 2008-08-06  ~33 31091JM  Dave Fichtinger   -Add Tilt Reduction Button
 * 2008-10-06  ~34 42925JM  Dave Fichtinger  -kick off container label auto generate for tilt reduction  
 * 2009-01-15  ~35 41303JL  Toribio H.       -Change MFSXMLTransaction class for IGSXMLTransaction class in some calls.
 * 2009-03-25      44544GB  Santiago D       -Remove swMifLabel method and all calls to it
 * 2009-03-16  ~36 39097MC  Santiago D       -CHECKHANDLING for special handling codes in str_wu and entityMerge
 * 2009-03-26  ~37 43106MR  Santiago D       -Pass USER to ACK_CNTR 
 * 2009-04-20  ~38 40039JL  VH Avila Rocha   -Add the new MASSKITINSTALL logic after to call the STR_WU trx
 * 2009-07-20  ~39 41330JL	Sayde Alcantara	 -New CASECONTENTMEF label in str_wu
 * 2009-08-18  ~40 37550JL  Drew Erickson    -New Build Ahead Apply and Release buttons
 * 2009-08-20  ~41 41901TL  Santiago D       -Add trigger for Warranty/Weight Labels
 * 2010-03-03  ~42 47596MZ  Toribio H.		 -Efficiency changes
 * 							D Kloepping			change for supsend questions prompting, default for efficiency label
 * 2010-02-18  ~43 46810JL  J Mastachi       -Update due to modified parameters in warrantyCard method
 * 2010-03-13  ~44 42558JL  Santiago SC      -New onDemandTrigger,onDemandReprint methods
 * 2010-11-01  ~45 49513JM	Toribio H.   	 -Make RTV_PRLN and RTV_IQ Cacheable 
 * 2010-11-12  ~46 48873JM  Luis M.          - Add trigger for FoD Labels
 * 2011-02-09  ~47 50322KM  Luis M.          -Do not trigger FOD if TLCONS is not set to "F"
 * 2011-06-15  ~48 50244JR  Edgar Mercado    -Tear Down enhancements
 * 2011-07-07  ~49 50759JR  Vicente Esteban  -Suppress PFS rule pop-op window  for post completion event
 *                                            WX01 Subassembly work units.
 * 2011-07-27  ~50 50759JR  Vicente Esteban  -Obtain MCTL from this.fieldCurrMctl instead of myHeadRec.getMctl()
 *                 Defect 538196   
 * 2014-01-03  ~51 00208431 Edgar Mercado    -Client Function to automatically archive all data related to a workunit and update FCSPWU25 table  
 * 2014-07-07  ~52 104150   LiquidChallenger -Restore Work Unit to Build Queue  - MFS Client  
 * 2015-09-03  ~53 1384186  Andy Williams    - Add Part Weight Button   
 * 2016-06-21  ~54 1543318  Luis M.          - Add SAP Deconfig Button            
 * 2016-07-05  ~55 1543328  Luis M.          - Add new SAP deconfig Panel and call proper deconfig server programs to support dcfg on SPD/SAP sites.                                                        
 * 2016-07-20  ~56 1562655  Luis M.          - Add SAP Apply Prep Button            
  ******************************************************************************/

package com.ibm.rchland.mfgapps.mfsclient.panel;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.border.TitledBorder;

import mfsxml.MISSING_XML_TAG_EXCEPTION;
import mfsxml.MfsXMLDocument;
import mfsxml.MfsXMLParser;

import com.ibm.rchland.mfgapps.client.utils.messagebox.IGSMessageBox;
import com.ibm.rchland.mfgapps.client.utils.xml.IGSXMLTransaction;
import com.ibm.rchland.mfgapps.mfsclient.MFSFrame;
import com.ibm.rchland.mfgapps.mfsclient.MFSMenuButton;
import com.ibm.rchland.mfgapps.mfsclient.MFSMenuPanel;
import com.ibm.rchland.mfgapps.mfsclient.MFSPanel;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSCSUDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSChangePwdDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSCreateBCKeysDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSErrorMsgListDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSGetValueDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSIQDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSStandAlonePartWeightDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSProdLineDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSProdLineWIPDriverDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSReapplyTypeDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSSerialNumDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSValidateSoftDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSWipDriverDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSWipDriverTestDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSWorkUnitLocDisplayDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSWorkUnitPNSNDialog;
import com.ibm.rchland.mfgapps.mfsclient.struct.MFSComponentListModel;
import com.ibm.rchland.mfgapps.mfsclient.struct.MFSComponentRec;
import com.ibm.rchland.mfgapps.mfsclient.struct.MFSHeaderRec;
import com.ibm.rchland.mfgapps.mfsclient.struct.MFSOnDemandKeyData;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSButtonIterator;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSConstants;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSDetermineLabel;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSOnDemand;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSPrintingMethods;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSTransactionUtils;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFStfParser;
import com.ibm.rchland.mfgapps.mfscommon.MFSConfig;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSComm;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSCommLogger;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSFixedTransaction;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSTransaction;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSXmlTransaction;
import com.ibm.rchland.mfgapps.mfscommon.comm.trx.AGE_MFGN;
import com.ibm.rchland.mfgapps.mfscommon.comm.trx.RTV_IQ;
import com.ibm.rchland.mfgapps.mfscommon.comm.trx.RTV_PRLN;
import com.ibm.rchland.mfgapps.mfscommon.comm.trx.VLDT_SERCOL;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSTearConfirmationDialog; //~48A
import com.ibm.rchland.mfgapps.mfscommon.comm.trx.DCNFG_FAB; //~48A
import com.ibm.rchland.mfgapps.mfscommon.exception.MFSTransactionException;

/**
 * <code>MFSSelectWorkPanel</code> is the main <code>MFSMenuPanel</code> for the
 * <i>MFS Client</i>.
 * 
 * @author The MFS Client Development Team
 */
@SuppressWarnings("serial")
public class MFSSelectWorkPanel extends MFSMenuPanel {
	/** The default screen name of an <code>MFSSelectWorkPanel</code>. */
	public static final String SCREEN_NAME = "Select Work"; //$NON-NLS-1$

	/** The a useful variable for determining source of activity ~34 */
	public static final String DIRECT_WORK_SOURCE = "DW"; //$NON-NLS-1$

	/** The a useful variable for determining source of activity ~34 */
	public static final String REDUCTION_SOURCE = "RE"; //$NON-NLS-1$

	// ~24C Change button from "MFS Authorization" to "Change Password"
	/** The Change Password (F2) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbAuth = MFSMenuButton.createLargeButton("Chg Pwd", //$NON-NLS-1$
			"AuthF2.gif", "Change Password", "BUTTON,SELWORK,PASSWORD"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

	/** The Sign Off (F3) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbSignoff = MFSMenuButton.createLargeButton("Sign Off", //$NON-NLS-1$
			"OffF3.gif", "Sign Off", "BUTTON,SELWORK,SIGNOFF"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

	/** The Existing Work (F4) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbExistingWork = MFSMenuButton.createLargeButton("Exist Work", //$NON-NLS-1$
			"ExistF4.gif", "Existing Work", "BUTTON,SELWORK,EXISTING"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

	/** The New Work (F5) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbNewWork = MFSMenuButton.createLargeButton("New Work", //$NON-NLS-1$
			"newwkF5.gif", "New Work", "BUTTON,SELWORK,NEWWORK"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

	/** The Extended Functions (F6) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbExtFunctions = MFSMenuButton.createLargeButton("Ext Func", //$NON-NLS-1$
			"ExtF6.gif", "Extended Functions", "BUTTON,SELWORK,EXTFUNC"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

	/** The Re-Apply (F7) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbReapply = MFSMenuButton.createLargeButton("Re-Apply", //$NON-NLS-1$
			"ReapF7.gif", "Re-Apply", "BUTTON,SELWORK,REAPPLY"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

	/** The Tear Down (F8) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbTearDown = MFSMenuButton.createLargeButton("Tear Down", //$NON-NLS-1$
			"TeardnF8.gif", "Tear Down", "BUTTON,SELWORK,TEARDOWN"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

	/** The Stand Alone Printing (F9) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbStandAlone = MFSMenuButton.createLargeButton("Printing", //$NON-NLS-1$
			"printF9.gif", "Stand Alone Printing", "BUTTON,SELWORK,PRINTING"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

	/** The Unburn (F10) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbUnburn = MFSMenuButton.createLargeButton("Unburn", //$NON-NLS-1$
			"VpdunburnF10.gif", "Unburn", "BUTTON,SELWORK,UNBURN"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

	/** The Deconfig (F11) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbDeconfig = MFSMenuButton.createLargeButton("Deconfig", //$NON-NLS-1$
			"deconfigF11.gif", "Deconfig", "BUTTON,SELWORK,DECONFIG"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

	/** The Start FKIT (F12) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbFkit = MFSMenuButton.createLargeButton("STR FKIT", //$NON-NLS-1$
			"fkitF12.gif", "Start FKIT", "BUTTON,SELWORK,FKIT"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

	/** The Part Movement (Shift + F1) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbFuncNewWork = MFSMenuButton.createLargeButton("Part Movement", "clognewwkF13.gif", //$NON-NLS-1$//$NON-NLS-2$
			"Part Movement", //$NON-NLS-1$
			"BUTTON,SELWORK,CLOGNEWWORK"); //$NON-NLS-1$

	/** The Part Functions (Shift + F2) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbPartFunctions = MFSMenuButton.createLargeButton("Part Functions", "pfuncF14.gif", //$NON-NLS-1$//$NON-NLS-2$
			"Part Functions", //$NON-NLS-1$
			"BUTTON,SELWORK,PARTFUNCTIONS"); //$NON-NLS-1$

	/** The WIP Driver Test (Shift + F3) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbTestWIPDriver = MFSMenuButton.createLargeButton("WIP Driver Test", "wipdtestF15.gif", //$NON-NLS-1$//$NON-NLS-2$
			"WIP Driver Test", //$NON-NLS-1$
			"BUTTON,SELWORK,WIPDTEST"); //$NON-NLS-1$

	/** The Apply Prep (Shift + F4) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbReapplyPrep = MFSMenuButton.createLargeButton("Apply Prep", //$NON-NLS-1$
			"ReapplyPrepF16.gif", "Apply Prep", "BUTTON,SELWORK,REAPPLYPREP"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

	// ~16A New button
	/** The Print Barcode for Key (Shift + F5) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbCreateBCKeys = MFSMenuButton.createLargeButton("BC Keys", //$NON-NLS-1$
			"printF17.gif", "Print Barcode for Key", "BUTTON,SELWORK,BC4KEYS"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

	// ~25A New button
	/** The Entity Merge (Shift + F6) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbEntityMerge = MFSMenuButton.createLargeButton("Entity Merge", //$NON-NLS-1$
			"mergeF18.gif", "Entity Merge", "BUTTON,SELWORK,ENTMERGE"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

	// ~31A
	/** Acknowledge Container (Shift + F7) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbAckCntr = MFSMenuButton.createLargeButton("Ack Cntr", //$NON-NLS-1$
			"ackF19.gif", "Ack Container", "BUTTON,SELWORK,ACKCNTR"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

	// ~33A
	/** Acknowledge Container (Shift + F8) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbReduction = MFSMenuButton.createLargeButton("Tilt Reduction", //$NON-NLS-1$
			"reduceButtonF20.gif", "Tilt Reduction", "BUTTON,SELWORK,REDUCTION"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

	// ~40A
	/** Release Build Ahead (Shift + F9) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbReleaseBuildAhead = MFSMenuButton.createLargeButton("Release BA", //$NON-NLS-1$
			"releaseBaButtonF21.gif", "Release BA", "BUTTON,SELWORK,RLSEBA"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

	// ~40A
	/** Apply Build Ahead (Shift + F10) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbApplyBuildAhead = MFSMenuButton.createLargeButton("Apply BA", //$NON-NLS-1$
			"applyBaButtonF22.gif", "Apply BA", "BUTTON,SELWORK,APPLYBA"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

	// ~44A
	/** The On Demand Label (CTRL + L) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbOnDemand = MFSMenuButton.createLargeButton("On Demand", //$NON-NLS-1$
			"printCTRLL.gif", "On Demand Label Printing", "BUTTON,SELWORK,ONDEMANDLBL"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

	// ~51A
	/** Archive a Work Unit (CTRL + N) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbArchive = MFSMenuButton.createLargeButton("Archive", //$NON-NLS-1$
			"archiveCTRLN.gif", "Archive a Work Unit", "BUTTON,SELWORK,ARCHIVE"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

	// ~53A
	/** The Part Weight (CTRL + W) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbPartWeight = MFSMenuButton.createLargeButton("Part Weight", "partweightCTRLW.gif", null, //$NON-NLS-2$
			"BUTTON,SELWORK,PARTWEIGHT"); //$NON-NLS-1$
	// ~54A
	/** The SAP (CTRL + D) <code>MFSMenuButton</code>. ~54A*/
	private MFSMenuButton pbSAPDeconfig = MFSMenuButton.createLargeButton("SAP Deconfig", "ckadDeconfigCTRLD.gif", "DCFG", //$NON-NLS-2$
			"BUTTON,SELWORK,SAPDECONFIG"); //$NON-NLS-1$

	/** The Apply Prep (CTRL + P) <code>MFSMenuButton</code>.  ~55A*/
	private MFSMenuButton pbSapApplyPrep = MFSMenuButton.createLargeButton("SAP PREP App", //$NON-NLS-1$
			"SapApplyPrepCTRLP.gif", "SAP Apply Prep", "BUTTON,SELWORK,SAPAPPLYPREP"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

	/** The MCTL of the current work unit. */
	private String fieldCurrMctl = ""; //$NON-NLS-1$

	/** The PRLN last selected in {@link #newWork()}. */
	private String fieldPrevNewWorkChoice = ""; //$NON-NLS-1$

	/** The data last displayed by {@link #showPNSNErrorList(String, int)}. */
	private String fieldPNSNErrorData = ""; //$NON-NLS-1$

	/** The index last selected in {@link #showPNSNErrorList(String, int)}. */
	private int fieldPNSNErrorIndex = 0;

	/** The Previous WIP Test Driver. */
	private String fieldPrevWipTestDriver = ""; //$NON-NLS-1$

	/**
	 * The SourceButton to save last pushed button <code>MFSMenuButton</code>.
	 */
	public MFSMenuButton pbSourceButton = null; // ~42

	/**
	 * The efficiency flag to activate efficiency logic <code>boolean</code>.
	 */
	public boolean cnfgEfficiencyOn = false; // ~42

	/**
	 * The efficiency dumb client flag to activate efficiency logic
	 * <code>boolean</code>.
	 */
	public boolean cnfgEfficiencyDumbClientOn = false; // ~42

	/**
	 * Constructs a new <code>MFSSelectWorkPanel</code>.
	 * 
	 * @param parent
	 *            the <code>MFSFrame</code> used to display this panel
	 * @param source
	 *            the <code>MFSPanel</code> that caused this panel to be
	 *            displayed
	 */
	public MFSSelectWorkPanel(MFSFrame parent, MFSPanel source) {
		super(parent, source, SCREEN_NAME, 6, 4);
		this.fieldButtonIterator = createMenuButtonIterator();
		createLayout();
		configureButtons();
		addMyListeners();

		String cnfgEfficiency = "EFFICIENCYON"; //$NON-NLS-1$
		if (MFSConfig.getInstance().containsConfigEntry(cnfgEfficiency)) // ~42
		{
			this.cnfgEfficiencyOn = true;
		}

		String cnfgEfficiencyDumbClientOn = "EFFICIENCYON*DUMBCLIENT"; //$NON-NLS-1$
		if (MFSConfig.getInstance().containsConfigEntry(cnfgEfficiencyDumbClientOn)) // ~42
		{
			this.cnfgEfficiencyDumbClientOn = true;
		}
	}

	// ~31A
	/**
	 * Invoked when {@link #pbAckCntr} is selected to acknowledge a container.
	 */
	private void ackCntr() {
		try {
			this.removeMyListeners();

			MFSGetValueDialog gvd = new MFSGetValueDialog(this.getParentFrame(), "Enter the container value", "CNTR", //$NON-NLS-1$ //$NON-NLS-2$
					"Acknowledge", 'k'); //$NON-NLS-1$
			MFStfParser parser = new MFStfParser.MFStfCntrParser(this);
			gvd.setTextParser(parser);

			do {
				gvd.setLocationRelativeTo(getParentFrame());
				gvd.setVisible(true);

				if (gvd.getProceedSelected()) {
					String cntr = gvd.getInputValue();

					if (cntr.length() != 0) /* make future checks easier */
					{
						if (cntr.length() == 10) {
							cntr = cntr.substring(4, 10);
						}

						cntr += "    "; //$NON-NLS-1$

						IGSXMLTransaction ackCntr = new IGSXMLTransaction("ACK_CNTR"); //$NON-NLS-1$
						ackCntr.setActionMessage("Sending Acknowledge..."); //$NON-NLS-1$
						ackCntr.startDocument();
						ackCntr.addElement("CNTR", cntr); //$NON-NLS-1$
						ackCntr.addElement("USER", MFSConfig.getInstance().get8CharUser()); // ~37A //$NON-NLS-1$
						ackCntr.endDocument();

						MFSComm.getInstance().execute(ackCntr, this);

						// No matter what return code is , we need to display
						// all msgs
						ackCntr.stepIntoElement("RCD"); //$NON-NLS-1$

						StringBuffer ackErms = new StringBuffer();

						ackErms.append("Container: "); //$NON-NLS-1$
						ackErms.append(cntr);

						String element = ackCntr.getErms();
						if (element != null) {
							ackErms.append("\n"); //$NON-NLS-1$
							ackErms.append(element);
						}

						element = ackCntr.getNextElement("ERMS2"); //$NON-NLS-1$
						if (element != null) {
							ackErms.append("\n"); //$NON-NLS-1$
							ackErms.append(element);
						}

						element = ackCntr.getNextElement("ERMS3"); //$NON-NLS-1$
						if (element != null) {
							ackErms.append("\n"); //$NON-NLS-1$
							ackErms.append(element);
						}

						String title = "Acknowledge Status"; //$NON-NLS-1$
						IGSMessageBox.showOkMB(getParentFrame(), title, ackErms.toString(), null);
					} else {
						IGSMessageBox.showOkMB(getParentFrame(), null, "Please type a container.", null); //$NON-NLS-1$
					}
					gvd.setDefaultValue(""); //$NON-NLS-1$
				}
			} while (gvd.getProceedSelected());
		} catch (Exception e) {
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		} finally {
			this.addMyListeners();
		}
	}

	// ~7
	/**
	 * Perform the automated down binning method.
	 * 
	 * @param oldMctl
	 *            the old work unit control number
	 * @param newMctl
	 *            the new work unit control number
	 */
	private void autoDownBin(String oldMctl, String newMctl) {
		int rc = 0;
		// ~35 Change to IGSXMLTransaction
		IGSXMLTransaction rtvdownbin = new IGSXMLTransaction("RTVDOWNBIN"); //$NON-NLS-1$
		rtvdownbin.startDocument();
		rtvdownbin.startElement("DATA"); //$NON-NLS-1$
		rtvdownbin.addElement("MCTO", oldMctl); //$NON-NLS-1$
		rtvdownbin.addElement("MCTN", newMctl); //$NON-NLS-1$
		rtvdownbin.endElement("DATA"); //$NON-NLS-1$
		rtvdownbin.endDocument();
		rtvdownbin.setActionMessage("Downbinning, Please Wait..."); //$NON-NLS-1$
		rtvdownbin.run();

		rc = rtvdownbin.getReturnCode();

		/* Print flag(PFLG) used to verify down bin parts found */
		if (rc == 0 || rc == 100) // ~12
		{
			String new_pn, new_sn, pflg;
			int qty = 1; // Default should always be 1
			MFSDirectWorkPanel dirWorkPanel = getParentFrame().getDirectWorkPanel();

			// Be sure the downbin hashtable is empty.
			if (!dirWorkPanel.getDwnBinList().isEmpty()) {
				dirWorkPanel.getDwnBinList().clear();
			}

			while (null != rtvdownbin.stepIntoElement("RCD")) //$NON-NLS-1$
			{
				// Retrieve New PN, New SN, and Print Indicator from returned
				// XML string.
				new_pn = rtvdownbin.getNextElement("PNNW"); //$NON-NLS-1$
				new_sn = rtvdownbin.getNextElement("SNEW"); //$NON-NLS-1$
				pflg = rtvdownbin.getNextElement("PFLG"); //$NON-NLS-1$
				// If print flag is "Y", then print 11SDWNBIN label
				if (pflg.equals("Y")) //$NON-NLS-1$
				{
					dirWorkPanel.setDwnBinList(new_pn, new_sn); // ~9
					MFSPrintingMethods.elevensDwnBin(new_pn, new_sn, qty, getParentFrame());
				}
				rtvdownbin.stepOutOfElement();
			} // end while
		} // end if (rc == 0 || rc == 100)
		else {
			IGSMessageBox.showOkMB(getParentFrame(), null, rtvdownbin.getErms(), null);
		}
	}

	// ~36A
	/**
	 * Check entry CHECKHANDLING to indicate, based on operation number, if we
	 * need to check for special handling.
	 * 
	 * @param mode
	 *            for the special handling 'C' for container, 'M' for mctl.
	 * @param modeValue
	 *            the container/mctl value.
	 * @param opNmbr
	 *            the container/mctl operation number.
	 */
	private void checkHandling(String mode, String modeValue, String opNmbr) {
		try {
			/*
			 * Check entry CHECKHANDLING to indicate, based on operation number,
			 * if we need to check for special handling.
			 */
			String cnfgDatCheckHandling = "CHECKHANDLING," + opNmbr; //$NON-NLS-1$

			if (MFSConfig.getInstance().containsConfigEntry(cnfgDatCheckHandling)) {
				IGSXMLTransaction rtvShmsg = new IGSXMLTransaction("RTV_SHMSG"); //$NON-NLS-1$
				rtvShmsg.setActionMessage("Retrieving Special Handling codes..."); //$NON-NLS-1$
				rtvShmsg.startDocument();
				rtvShmsg.addElement("MODE", mode); //$NON-NLS-1$

				if (mode.equals("M")) //$NON-NLS-1$
				{
					rtvShmsg.addElement("MCTL", modeValue); //$NON-NLS-1$
				} else if (mode.equals("C")) //$NON-NLS-1$
				{
					rtvShmsg.addElement("CNTR", modeValue); //$NON-NLS-1$
				}

				rtvShmsg.endDocument();

				MFSComm.getInstance().execute(rtvShmsg, this);

				if (rtvShmsg.getReturnCode() != 0) {
					String title = "Error Retrieving Special Handling codes."; //$NON-NLS-1$
					IGSMessageBox.showOkMB(getParentFrame(), title, rtvShmsg.getErms(), null);
				} else {
					String title = "Special Handling codes"; //$NON-NLS-1$
					StringBuffer sphmsg = new StringBuffer();

					while (rtvShmsg.stepIntoElement("RCD") != null) //$NON-NLS-1$
					{
						sphmsg.append("Order Number: "); //$NON-NLS-1$
						sphmsg.append(rtvShmsg.getNextElement("ORNO")); //$NON-NLS-1$
						sphmsg.append("\n"); //$NON-NLS-1$
						sphmsg.append(rtvShmsg.getNextElement("HMSG")); //$NON-NLS-1$
						sphmsg.append("\n"); //$NON-NLS-1$

						rtvShmsg.stepOutOfElement();
					}

					if (sphmsg.length() > 0) {
						IGSMessageBox.showOkMB(getParentFrame(), title, sphmsg.toString(), null);
					}
				}
			}
		} catch (Exception e) {
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
	}

	// ~16A New method for IPSR35208JM
	/**
	 * Calls the RTV_RULEX to download the FKE entries and displays an
	 * <code>MFSCreateBCKeysDialog</code> to print a key input barcode.
	 */
	private void createBarcodeForKeys() {
		try {
			removeMyListeners();
			MfsXMLDocument xmlData = new MfsXMLDocument("RTV_RULEX"); //$NON-NLS-1$
			xmlData.addOpenTag("DATA"); //$NON-NLS-1$
			xmlData.addCompleteField("KEY", "FKE"); //$NON-NLS-1$ //$NON-NLS-2$
			xmlData.addCloseTag("DATA"); //$NON-NLS-1$
			xmlData.finalizeXML();

			MFSTransaction rtv_rulex = new MFSXmlTransaction(xmlData.toString());
			rtv_rulex.setActionMessage("Retrieving Barcode Key Options, Please Wait..."); //$NON-NLS-1$
			MFSComm.getInstance().execute(rtv_rulex, this);

			if (rtv_rulex.getReturnCode() != 0) {
				IGSMessageBox.showOkMB(getParentFrame(), null, rtv_rulex.getErms(), null);
			} else {
				MFSCreateBCKeysDialog dialog = new MFSCreateBCKeysDialog(getParentFrame(), rtv_rulex);
				dialog.setLocationRelativeTo(getParentFrame()); // ~17A
				dialog.setVisible(true);
			}
		} catch (Exception e) {
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		} finally {
			addMyListeners();
		}
	}

	// ~16A New method
	/**
	 * Creates an <code>MFSButtonIterator</code> for this panel's
	 * <code>MFSMenuButton</code>s.
	 * 
	 * @return the <code>MFSButtonIterator</code>
	 */
	private MFSButtonIterator createMenuButtonIterator() {
		// The constructor param is the number of buttons
		MFSButtonIterator iterator = new MFSButtonIterator(22); // ~33C //~44C
		iterator.add(this.pbAuth);
		iterator.add(this.pbSignoff);
		iterator.add(this.pbExistingWork);
		iterator.add(this.pbNewWork);
		iterator.add(this.pbExtFunctions);
		iterator.add(this.pbReapply);
		iterator.add(this.pbTearDown);
		iterator.add(this.pbStandAlone);
		iterator.add(this.pbUnburn);
		iterator.add(this.pbDeconfig);
		iterator.add(this.pbFkit);
		iterator.add(this.pbFuncNewWork);
		iterator.add(this.pbPartFunctions);
		iterator.add(this.pbTestWIPDriver);
		iterator.add(this.pbReapplyPrep);
		iterator.add(this.pbSapApplyPrep);  //~55A
		iterator.add(this.pbCreateBCKeys);
		iterator.add(this.pbEntityMerge); // ~25A
		iterator.add(this.pbAckCntr); // ~31A
		iterator.add(this.pbReduction); // ~33A
		iterator.add(this.pbReleaseBuildAhead); // ~40A
		iterator.add(this.pbApplyBuildAhead); // ~40A
		iterator.add(this.pbOnDemand); // ~44A
		iterator.add(this.pbArchive); // ~51A
		iterator.add(this.pbPartWeight); // ~53A
		iterator.add(this.pbSAPDeconfig); // ~54A
		return iterator;
	}

	// ~16A New method
	/**
	 * Creates the <code>TitledBorder</code> for this menu panel.
	 * 
	 * @return the <code>TitledBorder</code> for this menu panel
	 */
	protected TitledBorder createTitledBorder() {
		return BorderFactory.createTitledBorder(BorderFactory.createLineBorder(TITLE_BORDER_COLOR), "Welcome To MFS...", //$NON-NLS-1$
				TitledBorder.LEFT, TitledBorder.TOP,
				MFSConstants.LARGE_PLAIN_DIALOG_FONT, TITLE_BORDER_COLOR);
	}

	/** Invoked when {@link #pbDeconfig} is selected to deconfig a work unit. */
	private void deconfig() {
		boolean autoRepeatTransaction = this.cnfgEfficiencyOn; // ~42
		try {
			int rc = 0;
			removeMyListeners();

			do {
				MFSWorkUnitPNSNDialog myWrkUnitPNSND = new MFSWorkUnitPNSNDialog(getParentFrame());
				myWrkUnitPNSND.setLocationRelativeTo(getParentFrame()); // ~17A
				myWrkUnitPNSND.setVisible(true);

				if (myWrkUnitPNSND.getPressedEnter()) {
					this.fieldCurrMctl = myWrkUnitPNSND.getMctl();

					MfsXMLDocument xml_data1 = new MfsXMLDocument("VLDT_DCFWU"); //$NON-NLS-1$
					xml_data1.addOpenTag("DATA"); //$NON-NLS-1$
					xml_data1.addCompleteField("MCTL", this.fieldCurrMctl); //$NON-NLS-1$
					xml_data1.addCompleteField("VTYP", "DCFG"); //$NON-NLS-1$ //$NON-NLS-2$
					xml_data1.addCloseTag("DATA"); //$NON-NLS-1$
					xml_data1.finalizeXML();

					MFSTransaction vldt_dcfwu = new MFSXmlTransaction(xml_data1.toString());
					MFSComm.getInstance().execute(vldt_dcfwu, this);
					rc = vldt_dcfwu.getReturnCode();

					if (rc != 0) {
						IGSMessageBox.showOkMB(getParentFrame(), null, vldt_dcfwu.getErms(), null);
					} else {
						StringBuffer data2 = new StringBuffer();
						data2.append("REMAP     "); //$NON-NLS-1$
						data2.append(this.fieldCurrMctl);
						data2.append("U"); //$NON-NLS-1$
						data2.append("DCFG"); //$NON-NLS-1$
						data2.append(MFSConfig.getInstance().get8CharCellType());

						MFSTransaction remap = new MFSFixedTransaction(data2.toString());
						remap.setActionMessage("Remapping Work Unit, Please Wait..."); //$NON-NLS-1$
						MFSComm.getInstance().execute(remap, this);
						rc = remap.getReturnCode();

						if (rc == 30 || rc == 0) {
							str_val();
							autoRepeatTransaction = false;
						} else {
							IGSMessageBox.showOkMB(getParentFrame(), null, remap.getErms(), null);
						}
					} /* end of rc */
				} /* work unit entered */
				else {
					autoRepeatTransaction = false;
				}
			} while (autoRepeatTransaction);
		} catch (Exception e) {
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
		addMyListeners();
	}

	/**
	 * Calls the RTV_RACKWU transaction to retrieve rack work unit locations and
	 * displays the results using an <code>MFSWorkUnitLocDisplayDialog</code>.
	 * 
	 * @param headRec
	 *            the <code>MFSHeaderRec</code> for the work unit
	 * @return 0 on success; nonzero on failure
	 */
	private int displayWorkUnitLocations(MFSHeaderRec headRec) {
		int rc = 0;
		try {
			String cnfgData1 = "LOCDISPLAY," + headRec.getNmbr() + "," + headRec.getPrln().trim(); //$NON-NLS-1$ //$NON-NLS-2$
			String value = MFSConfig.getInstance().getConfigValue(cnfgData1);
			if (value.equals(MFSConfig.NOT_FOUND)) {
				String cnfgData2 = "LOCDISPLAY," + headRec.getNmbr() + ",*ALL"; //$NON-NLS-1$ //$NON-NLS-2$
				value = MFSConfig.getInstance().getConfigValue(cnfgData2);
			}
			if (value.equals(MFSConfig.NOT_FOUND)) {
				String cnfgData3 = "LOCDISPLAY,*ALL,*ALL"; //$NON-NLS-1$
				value = MFSConfig.getInstance().getConfigValue(cnfgData3);
			}
			if (!value.equals(MFSConfig.NOT_FOUND)) {
				MfsXMLDocument xml_data1 = new MfsXMLDocument("RTV_RACKWU"); //$NON-NLS-1$
				xml_data1.addOpenTag("DATA"); //$NON-NLS-1$
				xml_data1.addCompleteField("MCTL", this.fieldCurrMctl); //$NON-NLS-1$
				xml_data1.addCompleteField("OLEV", headRec.getOlev()); //$NON-NLS-1$
				xml_data1.addCloseTag("DATA"); //$NON-NLS-1$
				xml_data1.finalizeXML();

				MFSTransaction rtv_rackwu = new MFSXmlTransaction(xml_data1.toString());
				MFSComm.getInstance().execute(rtv_rackwu, this);
				rc = rtv_rackwu.getReturnCode();

				if (rc != 0) {
					IGSMessageBox.showOkMB(getParentFrame(), null, rtv_rackwu.getErms(), null);
				} else {
					MfsXMLParser xmlParser = new MfsXMLParser(rtv_rackwu.getOutput());
					MFSWorkUnitLocDisplayDialog myLocD = new MFSWorkUnitLocDisplayDialog(getParentFrame());

					if (myLocD.loadList(xmlParser.getUnparsedXML())) {
						myLocD.setLocationRelativeTo(getParentFrame());
						myLocD.setVisible(true);
					}
				} // end of good call to RTV_RACKWU
			}
		} catch (Exception e) {
			rc = 11;
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
		return rc;
	}

	// ~25A New method
	/**
	 * Invoked when {@link #pbEntityMerge} is selected to display an
	 * <code>MFSEntityMergePanel</code> for a shipping entity.
	 */
	private void entityMerge() {
		boolean autoRepeatTransaction = this.cnfgEfficiencyOn; // ~42

		removeMyListeners();

		do {
			try {
				MFSEntityMergePanel emp = new MFSEntityMergePanel(getParentFrame(), this, MFSEntityMergePanel.MERGING);
				if (emp.retrieveContainerInfo(this)) {
					/*
					 * ~36A - Check entry CHECKHANDLING to indicate, based on
					 * operation number, if we need to check for special
					 * handling.
					 */
					checkHandling("C", emp.getCntr(), emp.getOpNmbr()); //$NON-NLS-1$

					getParentFrame().displayMFSPanel(emp);
					autoRepeatTransaction = false;
				} else {
					autoRepeatTransaction = false;
				}

			} catch (Exception e) {
				IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
			}
		} while (autoRepeatTransaction);
		addMyListeners();
	}

	/**
	 * Invoked when {@link #pbExistingWork}is selected to display an
	 * <code>MFSDirectWorkPanel</code> for an existing work unit.
	 */
	private void existingWork() {
		int rc = 0;
		boolean autoRepeatTransaction = this.cnfgEfficiencyOn; // ~42
		try {
			removeMyListeners();
			do {
				MFSWorkUnitPNSNDialog myWrkUnitPNSND = new MFSWorkUnitPNSNDialog(getParentFrame());
				myWrkUnitPNSND.setLocationRelativeTo(getParentFrame()); // ~17A
				myWrkUnitPNSND.setVisible(true);

				if (myWrkUnitPNSND.getPressedEnter()) {
					this.fieldCurrMctl = myWrkUnitPNSND.getMctl();
					rc = str_wu("J"); //$NON-NLS-1$
					/* Call search Log automatically after Mass Kit Install */
					if (rc == 0) {
						autoRepeatTransaction = false;
					}
				} else {
					autoRepeatTransaction = false;
				}
			} while (autoRepeatTransaction);
		} catch (Exception e) {
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		} finally {
			addMyListeners();
		}
	}

	/** Invoked when {@link #pbNewWork} is selected. */
	private void newWork() {
		int rc = 0;
		String errorString = null;
		boolean autoRepeatTransaction = this.cnfgEfficiencyOn; // ~42

		try {
			removeMyListeners();
			// if we are not efficient OR we need to get the list again
			RTV_PRLN rtvPrln = new RTV_PRLN(this); // ~45
			rtvPrln.setInputCellType(MFSConfig.getInstance().get8CharCellType());
			rtvPrln.execute();

			rc = rtvPrln.getReturnCode();
			errorString = rtvPrln.getErrorMessage();
			if (rc == 0) {
				do {
					MFSProdLineDialog prlnD = new MFSProdLineDialog(getParentFrame());
					prlnD.loadPrlnListModel(rtvPrln.getOutputPrlnList());
					if (this.fieldPrevNewWorkChoice.equals("")) //$NON-NLS-1$
					{
						prlnD.setSelectedIndex(0);
					} else
					// set up the prevNewWorkChoice to be the default choice
					{
						prlnD.setSearchText(this.fieldPrevNewWorkChoice.substring(0, 8).trim());
						prlnD.search();
					}
					prlnD.setLocationRelativeTo(getParentFrame()); // ~17A
					prlnD.setVisible(true);

					if (prlnD.getProceedSelected()) {
						if (prlnD.getSelectedListOption().equals(prlnD.OPTION_NONE)) {
							errorString = "Invalid Product Line Selected."; //$NON-NLS-1$
							rc = 10;
						} else {
							// set up the previous new work choice so the next
							// time
							// we will start there in the list
							String selectedPRLN = prlnD.getSelectedListOption();
							this.fieldPrevNewWorkChoice = selectedPRLN;

							if (!this.cnfgEfficiencyOn) {
								/* start the RTV_NXT_WU transaction thread */
								String data2 = "RTV_NXT_WU" + this.fieldPrevNewWorkChoice; //$NON-NLS-1$
								MFSTransaction rtv_nxt_wu = new MFSFixedTransaction(data2);
								rtv_nxt_wu.setActionMessage("Retrieving Next Work Unit Number, Please Wait..."); //$NON-NLS-1$
								MFSComm.getInstance().execute(rtv_nxt_wu, this);
								rc = rtv_nxt_wu.getReturnCode();

								if (rc == 0) {
									this.fieldCurrMctl = rtv_nxt_wu.getOutput();
									selectedPRLN = ""; //$NON-NLS-1$
								} else {
									errorString = rtv_nxt_wu.getErms();
								}
							} else {
								this.fieldCurrMctl = ""; //$NON-NLS-1$
							}
							if (rc == 0) {
								rc = str_wu("J", selectedPRLN); //$NON-NLS-1$
								if (rc != 0 && this.fieldCurrMctl.length() > 0) {
									// Get UserId from config object ~52
									final MFSConfig config = MFSConfig.getInstance();
									/* reset the work unit back to New */
									String data3 = "RST_NEW_WU" + this.fieldCurrMctl; //$NON-NLS-1$
									data3 = data3.concat(config.get8CharUser()); // ~52A
									MFSTransaction rst_new_wu = new MFSFixedTransaction(data3);
									rst_new_wu.setActionMessage("Resetting Work Unit back to NEW, Please Wait..."); //$NON-NLS-1$
									MFSComm.getInstance().execute(rst_new_wu, this);

									// ~10A Start sysout RST_NEW_WU error
									if (rst_new_wu.getReturnCode() != 0) {
										System.out.print("Error in RST_NEW_WU: "); //$NON-NLS-1$
										System.out.println(rst_new_wu.getOutput());
									}
									// ~10A End sysout RST_NEW_WU error
								} else {
									autoRepeatTransaction = false;
								}
								rc = 0;
							}
						}
						if (rc != 0) {
							IGSMessageBox.showOkMB(getParentFrame(), null, errorString, null);
						}
					} else {
						autoRepeatTransaction = false;
					}
				} while (autoRepeatTransaction);
			} else {
				IGSMessageBox.showOkMB(getParentFrame(), null, errorString, null);
			}
		} catch (Exception e) {
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
		addMyListeners();
	}

	// ~44A
	/** Invoked when {@link #pbOnDemand} is selected. */
	private void onDemandReprint() {
		try {
			this.removeMyListeners();

			// Create key data for label printing
			MFSOnDemandKeyData odKeyData = new MFSOnDemandKeyData();
			odKeyData.setTriggerSource("SELWORK"); //$NON-NLS-1$
			odKeyData.setTriggerKey("*NONE"); //$NON-NLS-1$

			/*
			 * WARNING!! currently there is no dataSource at this point. Input
			 * field of types R and A will fail if configured, probably throwing
			 * a null pointer exception.
			 */

			// Start label printing process
			MFSOnDemand odLabel = new MFSOnDemand(getParentFrame(), odKeyData);
			odLabel.labelReprint();
		} catch (Exception e) {
			IGSMessageBox.showOkMB(getParentFrame(), null, e.getMessage(), null);
		} finally {
			this.addMyListeners();
		}
	}

	// ~44A
	/**
	 * Creates a <code>MFSOnDemand</code> object to print on demand labels.
	 * 
	 * @param Object
	 *            source to retrieve data from
	 */
	private void onDemandTrigger(Object source, String triggerSource) {
		try {
			// Create key data for label printing
			MFSOnDemandKeyData odKeyData = new MFSOnDemandKeyData();
			odKeyData.setTriggerSource(triggerSource);
			odKeyData.setDataSource(source);

			// Start label printing process
			MFSOnDemand odLabel = new MFSOnDemand(getParentFrame(), odKeyData);
			odLabel.labelTrigger();
		} catch (Exception e) {
			IGSMessageBox.showOkMB(getParentFrame(), null, e.getMessage(), null);
		}
	}

	// ~51A
	/** Invoked when {@link #pbArchive} is selected. */
	private void archiveWU() {
		MFSConfig config = MFSConfig.getInstance();
		int rc = 0;
		boolean autoRepeatTransaction = this.cnfgEfficiencyOn;

		try {
			removeMyListeners();
			do {
				MFSWorkUnitPNSNDialog myWrkUnitPNSND = new MFSWorkUnitPNSNDialog(getParentFrame());
				myWrkUnitPNSND.setLocationRelativeTo(getParentFrame());
				myWrkUnitPNSND.setVisible(true);

				if (myWrkUnitPNSND.getPressedEnter()) {
					this.fieldCurrMctl = myWrkUnitPNSND.getMctl();

					AGE_MFGN age_mfgn = new AGE_MFGN(this);
					age_mfgn.setInputWU(this.fieldCurrMctl);
					age_mfgn.setInputUserId(config.get8CharUser());

					if (age_mfgn.execute()) {
						rc = age_mfgn.getReturnCode();
						IGSMessageBox.showOkMB(this, null, age_mfgn.getMessage(), null, false);
					} else {
						IGSMessageBox.showOkMB(this, null, age_mfgn.getErrorMessage(), null, false);
					}

					/* Call 'Archive' automatically after previous done */
					if (rc == 0) {
						autoRepeatTransaction = false;
					}
				} else {
					autoRepeatTransaction = false;
				}
			} while (autoRepeatTransaction);
		} catch (Exception e) {
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		} finally {
			addMyListeners();
		}
	}

	// ~53A
	/** Invoked when {@link #pbPartWeight} is selected. */
	private void partWeight() {

		try {
			removeMyListeners();
			MFSStandAlonePartWeightDialog partWeightDialog = new MFSStandAlonePartWeightDialog(getParentFrame(), this);
			partWeightDialog.display();

		} catch (Exception e) {
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		} finally {
			addMyListeners();
		}
	}

	// ~54A && ~55A
	/** Invoked when {@link #pbSAPDeconfig} is selected. */
	private void sapDeconfig() {
		boolean autoRepeatTransaction = this.cnfgEfficiencyOn;
		try {
			int rc = 0;
			removeMyListeners();

			do {
				MFSWorkUnitPNSNDialog myWrkUnitPNSND = new MFSWorkUnitPNSNDialog(getParentFrame());
				myWrkUnitPNSND.setLocationRelativeTo(getParentFrame());
				myWrkUnitPNSND.setVisible(true);

				if (myWrkUnitPNSND.getPressedEnter()) {
					this.fieldCurrMctl = myWrkUnitPNSND.getMctl();

					DCNFG_FAB dcnfg_fab = new DCNFG_FAB(this);

					dcnfg_fab.setInputMCTL(this.fieldCurrMctl);
					dcnfg_fab.setInputUSER(MFSConfig.getInstance().get8CharUser());
					dcnfg_fab.setInputPGM("TEAR ");
					dcnfg_fab.setInputTMCTL("*NONE   ");
					dcnfg_fab.setInputCTYP(MFSConfig.getInstance().get8CharCellType());

					dcnfg_fab.execute();

					rc = dcnfg_fab.getReturnCode();

					if (rc != 0) { // ~48C Use getErrorMessage instead of
									// getErms method
						IGSMessageBox.showOkMB(getParentFrame(), null, dcnfg_fab.getErrorMessage(), null);
					} else {
						StringBuffer data2 = new StringBuffer();
						data2.append("REMAP     "); //$NON-NLS-1$
						data2.append(this.fieldCurrMctl);
						data2.append("U"); //$NON-NLS-1$
						data2.append("DCFG"); //$NON-NLS-1$
						data2.append(MFSConfig.getInstance().get8CharCellType());

						MFSTransaction remap = new MFSFixedTransaction(data2.toString());
						remap.setActionMessage("Remapping Work Unit, Please Wait..."); //$NON-NLS-1$
						MFSComm.getInstance().execute(remap, this);
						rc = remap.getReturnCode();

						if (rc == 30 || rc == 0) {
							str_CKADdcfg();
							autoRepeatTransaction = false;
						} else {
							IGSMessageBox.showOkMB(getParentFrame(), null, remap.getErms(), null);
						}
					} /* end of rc */
				} /* work unit entered */
				else {
					autoRepeatTransaction = false;
				}
			} while (autoRepeatTransaction);
		} catch (Exception e) {
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		} finally {
			addMyListeners();
		}
	}

	// ~55A
	private int str_CKADdcfg() {
		// TODO Auto-generated method stub
		int rc = 0;
		try {
			final MFSConfig config = MFSConfig.getInstance();

			StringBuffer data = new StringBuffer();
			data.append("STR_DCFG  "); //$NON-NLS-1$
			data.append(this.fieldCurrMctl);
			data.append(config.get8CharCell());
			data.append(config.get8CharCellType());
			data.append(config.get8CharUser());

			MFSTransaction str_dcfg = new MFSFixedTransaction(data.toString());
			str_dcfg.setActionMessage("Starting the Work Unit, Please Wait..."); //$NON-NLS-1$
			MFSComm.getInstance().execute(str_dcfg, this);
			rc = str_dcfg.getReturnCode();

			if (rc == 0) {
				MFSSapDeconfigPanel sapDcfg = new MFSSapDeconfigPanel(getParentFrame(), this,
						MFSSapDeconfigPanel.SAP_DECONFIG_SCREEN_NAME);
				MFSHeaderRec myHeadRec = sapDcfg.loadListModel(str_dcfg.getOutput());
				sapDcfg.sortListModel();
				sapDcfg.loadPartsModels();

				if (!this.cnfgEfficiencyDumbClientOn) // ~42
				{
					MfsXMLDocument xml_data1 = new MfsXMLDocument("RTV_INSTR"); //$NON-NLS-1$
					xml_data1.addOpenTag("DATA"); //$NON-NLS-1$
					xml_data1.addCompleteField("MCTL", this.fieldCurrMctl); //$NON-NLS-1$
					xml_data1.addCompleteField("NMBR", myHeadRec.getNmbr()); //$NON-NLS-1$
					xml_data1.addCompleteField("PRLN", //$NON-NLS-1$
							myHeadRec.getPrln().concat("        ").substring(0, 8)); //$NON-NLS-1$
					xml_data1.addCompleteField("PROD", //$NON-NLS-1$
							myHeadRec.getProd().concat("        ").substring(0, 8)); //$NON-NLS-1$
					xml_data1.addCompleteField("USER", config.get8CharUser()); //$NON-NLS-1$
					xml_data1.addCompleteField("CELL", config.get8CharCell()); //$NON-NLS-1$
					xml_data1.addCompleteField("CTYP", config.get8CharCellType()); //$NON-NLS-1$
					xml_data1.addCompleteField("DSPO", "N"); //$NON-NLS-1$ //$NON-NLS-2$
					xml_data1.addCompleteField("OLEV", myHeadRec.getOlev()); //$NON-NLS-1$
					xml_data1.addCloseTag("DATA"); //$NON-NLS-1$
					xml_data1.finalizeXML();

					MFSTransaction rtv_instr = new MFSXmlTransaction(xml_data1.toString());
					rtv_instr.setActionMessage("Retrieving Instructions, Please Wait..."); //$NON-NLS-1$
					MFSComm.getInstance().execute(rtv_instr, this);
					rc = rtv_instr.getReturnCode();

					if (rc == 0) {
						MfsXMLParser xmlParser = new MfsXMLParser(rtv_instr.getOutput());
						sapDcfg.loadInstructions(xmlParser);
					}
					// else bad return code from RTV_INSTR
					else {
						IGSMessageBox.showOkMB(getParentFrame(), null, rtv_instr.getErms(), null);
					}
				}

				if (rc == 0) {
					sapDcfg.setupPartInstPanel();
					sapDcfg.prepareForDisplay(this.fieldCurrMctl);
					sapDcfg.configureButtons();

					startValTransactions(myHeadRec);

					sapDcfg.disableInstructionScrollRectToVisible();
					getParentFrame().displayMFSPanel(sapDcfg);
					sapDcfg.enableInstructionScrollRectToVisible();
				}
			} // end good return code from str_val
				// else bad return code from str_val
			else {
				IGSMessageBox.showOkMB(getParentFrame(), null, str_dcfg.getErms(), null);
			}
		} catch (Exception e) {
			rc = 11;
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
		return rc;

	}

	/**
	 * Invoked when {@link #pbFuncNewWork} is selected. Creates a new CLOG work
	 * unit and calls {@link #str_wu(String)}.
	 */
	private void pfuncNewWork() {
		try {
			int rc = 0;
			String errorString = ""; //$NON-NLS-1$

			removeMyListeners();

			MfsXMLDocument xml_data1 = new MfsXMLDocument("RTV_CLGWIP"); //$NON-NLS-1$
			xml_data1.addOpenTag("DATA"); //$NON-NLS-1$
			xml_data1.addCompleteField("CTYP", MFSConfig.getInstance().get8CharCellType()); //$NON-NLS-1$
			xml_data1.addCompleteField("LTYP", "B"); //$NON-NLS-1$ //$NON-NLS-2$
			xml_data1.addCloseTag("DATA"); //$NON-NLS-1$
			xml_data1.finalizeXML();

			MFSTransaction rtv_clgwip = new MFSXmlTransaction(xml_data1.toString());
			rtv_clgwip.setActionMessage("Retrieving List of Product Lines, Please Wait..."); //$NON-NLS-1$
			MFSComm.getInstance().execute(rtv_clgwip, this);
			rc = rtv_clgwip.getReturnCode();

			MfsXMLParser xmlParser = new MfsXMLParser(rtv_clgwip.getOutput());
			if (rc != 0) {
				errorString = xmlParser.getField("ERMS"); //$NON-NLS-1$
			} else {
				String tempPrln = ""; //$NON-NLS-1$
				String tempPlds = ""; //$NON-NLS-1$
				String tempWipd = ""; //$NON-NLS-1$
				DefaultListModel prlnListModel = new DefaultListModel();

				try {
					tempPrln = xmlParser.getField("PRLN"); //$NON-NLS-1$
					tempPlds = xmlParser.getField("PLDS"); //$NON-NLS-1$
				} catch (MISSING_XML_TAG_EXCEPTION e) {
					rc = 10;
					String erms = "No Valid Product Lines found for this Celltype?"; //$NON-NLS-1$
					IGSMessageBox.showOkMB(getParentFrame(), null, erms, null);
				}

				if (rc == 0) {
					prlnListModel.addElement(tempPrln + " -> " + tempPlds); //$NON-NLS-1$

					tempPrln = xmlParser.getNextField("PRLN"); //$NON-NLS-1$
					tempPlds = xmlParser.getNextField("PLDS"); //$NON-NLS-1$

					while (!tempPrln.equals("")) //$NON-NLS-1$
					{
						prlnListModel.addElement(tempPrln + " -> " + tempPlds); //$NON-NLS-1$
						tempPrln = xmlParser.getNextField("PRLN"); //$NON-NLS-1$
						tempPlds = xmlParser.getNextField("PLDS"); //$NON-NLS-1$
					}
				}

				if (rc == 0) {
					DefaultListModel wipdListModel = new DefaultListModel();
					wipdListModel.addElement("*NONE"); //$NON-NLS-1$

					try {
						tempWipd = xmlParser.getField("WIPD"); //$NON-NLS-1$
					} catch (MISSING_XML_TAG_EXCEPTION e) {
						// make the following loop skip right away
						tempWipd = ""; //$NON-NLS-1$
					}

					while (!tempWipd.equals("")) //$NON-NLS-1$
					{
						wipdListModel.addElement(tempWipd);
						tempWipd = xmlParser.getNextField("WIPD"); //$NON-NLS-1$
						while (!tempWipd.equals("")) //$NON-NLS-1$
						{
							wipdListModel.addElement(tempWipd);
							tempWipd = xmlParser.getNextField("WIPD"); //$NON-NLS-1$
						}
					}
					if (rc == 0) {
						MFSProdLineWIPDriverDialog prlnWipJD = new MFSProdLineWIPDriverDialog(getParentFrame(),
								prlnListModel, wipdListModel);
						if (this.fieldPrevNewWorkChoice.equals("")) //$NON-NLS-1$
						{
							prlnWipJD.setDefaultSelection("TURKEY"); //$NON-NLS-1$
						} else {
							prlnWipJD.setDefaultSelection(this.fieldPrevNewWorkChoice);
						}
						prlnWipJD.setLocationRelativeTo(getParentFrame()); // ~17A
						prlnWipJD.setVisible(true);

						if (prlnWipJD.getPressedEnter()) {
							final MFSConfig config = MFSConfig.getInstance();
							// ~35 Change to IGSXMLTransaction
							IGSXMLTransaction crtpfuncwu = new IGSXMLTransaction("CRTPFUNCWU"); //$NON-NLS-1$
							crtpfuncwu.startDocument();
							crtpfuncwu.addElement("CTYP", config.get8CharCellType()); //$NON-NLS-1$
							crtpfuncwu.addElement("CELL", config.get8CharCell()); //$NON-NLS-1$
							crtpfuncwu.addElement("USER", config.get8CharCell()); //$NON-NLS-1$
							crtpfuncwu.addElement("PRLN", prlnWipJD.getPrln().substring(0, 8)); //$NON-NLS-1$
							crtpfuncwu.addElement("WIPD", "*NONE"); //$NON-NLS-1$ //$NON-NLS-2$
							crtpfuncwu.endDocument();
							crtpfuncwu.setActionMessage("Creating Work Unit, Please Wait..."); //$NON-NLS-1$
							crtpfuncwu.run();
							rc = crtpfuncwu.getReturnCode();
							if (rc != 0) {
								errorString = crtpfuncwu.getErms();
							} else {
								this.fieldCurrMctl = crtpfuncwu.getFirstElement("MCTL"); //$NON-NLS-1$
								this.fieldPrevNewWorkChoice = prlnWipJD.getPrln();
								str_wu("J"); //$NON-NLS-1$
							}
						} // pressedEnter on dialog box
					} /* end of good rc */
				} // end of good rc
			}
			if (rc != 0) {
				IGSMessageBox.showOkMB(getParentFrame(), null, errorString, null);
			}
		} catch (Exception e) {
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
		addMyListeners();
	}

	/** Invoked when {@link #pbReapply} is selected to reapply a work unit. */
	private void reapply() {
		boolean autoRepeatTransaction = this.cnfgEfficiencyOn; // ~42
		try {
			int rc = 0;
			String errorString = null;
			boolean go = true;
			String newMctl = ""; //$NON-NLS-1$
			String prln = ""; //$NON-NLS-1$

			removeMyListeners();

			do {
				MFSWorkUnitPNSNDialog myWrkUnitPNSND = new MFSWorkUnitPNSNDialog(getParentFrame());
				myWrkUnitPNSND.setTitle("Reapply - From Work Unit"); //$NON-NLS-1$
				myWrkUnitPNSND.setLocationRelativeTo(getParentFrame()); // ~17A
				myWrkUnitPNSND.setVisible(true);

				if (myWrkUnitPNSND.getPressedEnter()) {
					MFSReapplyTypeDialog myRTJD = new MFSReapplyTypeDialog(getParentFrame());
					myRTJD.setLocationRelativeTo(getParentFrame()); // ~17A
					myRTJD.setVisible(true);
					if (myRTJD.getProceedSelected()) {
						if (myRTJD.getSelectedListOption().equals(myRTJD.BRUTE_FORCE_MCTL)) {
							MFSWorkUnitPNSNDialog myNewWrkUnitPNSND = new MFSWorkUnitPNSNDialog(getParentFrame());
							myNewWrkUnitPNSND.setTitle("Reapply - To Work Unit"); //$NON-NLS-1$
							myNewWrkUnitPNSND.setLocationRelativeTo(getParentFrame()); // ~17A
							myNewWrkUnitPNSND.setVisible(true);

							if (myNewWrkUnitPNSND.getPressedEnter()) {
								newMctl = myNewWrkUnitPNSND.getMctl();
							} else {
								go = false;
							}
						} else {
							newMctl = "*NONE   "; // used for //$NON-NLS-1$
													// dcnfg_fab later @1

							String data = "RTV_PRLN  " + MFSConfig.getInstance().get8CharCellType(); //$NON-NLS-1$
							MFSTransaction rtv_prln = new MFSFixedTransaction(data);
							rtv_prln.setActionMessage("Retrieving List of Product Lines, Please Wait..."); //$NON-NLS-1$
							MFSComm.getInstance().execute(rtv_prln, this);
							rc = rtv_prln.getReturnCode();

							if (rc == 0) {
								MFSProdLineDialog prlnJD = new MFSProdLineDialog(getParentFrame());
								prlnJD.loadPrlnListModel(rtv_prln.getOutput());
								prlnJD.setSelectedIndex(0);
								prlnJD.setLocationRelativeTo(getParentFrame()); // ~17A
								prlnJD.setVisible(true);

								if (prlnJD.getProceedSelected()) {
									prln = prlnJD.getSelectedListOption();
								} else {
									go = false;
								}
							} else {
								errorString = rtv_prln.getErms();
							}
						}

						if (rc == 0 && go) {
							// Change the way to invoke DCNFG_FAB (Through a
							// class) ~48C
							DCNFG_FAB dcnfg_fab = new DCNFG_FAB(this); // ~48A

							dcnfg_fab.setInputMCTL(myWrkUnitPNSND.getMctl()); // ~48A
							dcnfg_fab.setInputUSER(MFSConfig.getInstance().get8CharUser());
							// ~48A
							dcnfg_fab.setInputPGM("APPLY"); // ~48A
							dcnfg_fab.setInputTMCTL(newMctl); // ~48A
							dcnfg_fab.setInputCTYP(MFSConfig.getInstance().get8CharCellType()); // ~48A

							dcnfg_fab.execute(); // ~48A

							rc = dcnfg_fab.getReturnCode();

							if (rc != 0) {
								errorString = dcnfg_fab.getErrorMessage();
							} else {
								// @3 add celltype to BRUT_FORCE
								StringBuffer data3 = new StringBuffer();
								if (myRTJD.getSelectedListOption().equals(myRTJD.BRUTE_FORCE)) {
									data3.append("BRUT_FORCE"); //$NON-NLS-1$
									data3.append(myWrkUnitPNSND.getMctl());
									data3.append(prln);
									data3.append("J"); //$NON-NLS-1$
									data3.append("*NONE   "); //$NON-NLS-1$
									data3.append(MFSConfig.getInstance().get8CharCellType());
								} else if (myRTJD.getSelectedListOption().equals(myRTJD.BRUTE_FORCE_MCTL)) {
									data3.append("BRUT_FORCE"); //$NON-NLS-1$
									data3.append(myWrkUnitPNSND.getMctl());
									data3.append("        "); //$NON-NLS-1$
									data3.append("J"); //$NON-NLS-1$
									data3.append(newMctl);
									data3.append(MFSConfig.getInstance().get8CharCellType());
								}

								MFSTransaction brut_force = new MFSFixedTransaction(data3.toString());
								brut_force.setActionMessage("Reapplying Work Unit, Please Wait..."); //$NON-NLS-1$
								MFSComm.getInstance().execute(brut_force, this);
								rc = brut_force.getReturnCode();

								if (rc == 555) // ~28A
								{
									errorString = "Warnings generated during reapply. Please check EL10 for details."; //$NON-NLS-1$
									IGSMessageBox.showOkMB(getParentFrame(), null, errorString, null);
									rc = 0;
								}
								if (rc == 0) {
									String old_mctl = myWrkUnitPNSND.getMctl();
									String new_mctl = brut_force.getOutput().substring(0, 8);
									this.fieldCurrMctl = new_mctl;
									// ~7 Automate DownBinning RTVDOWNBIN
									if (MFSConfig.getInstance().containsConfigEntry("DOWNBIN")) //$NON-NLS-1$
									{
										autoDownBin(old_mctl, new_mctl);
									}
									str_wu("J"); //$NON-NLS-1$
									autoRepeatTransaction = false;
								} else {
									errorString = brut_force.getErms();
								}
							} /* good return from DCNFG_FAB */
						} /* end of good return and go */
					} /* end of pressed enter on reapply choice dialog */
				} /* end of pressed enter on work unit dialog */
				else {
					autoRepeatTransaction = false;
				}
				if (rc != 0) {
					IGSMessageBox.showOkMB(getParentFrame(), null, errorString, null);
				}
			} while (autoRepeatTransaction);
		} /* end of try */
		catch (Exception e) {
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
		addMyListeners();
	}

	/**
	 * Invoked when {@link #pbReapplyPrep} is selected to prepare a work unit
	 * for reapply.
	 */
	private void reapplyPrep() {
		boolean autoRepeatTransaction = this.cnfgEfficiencyOn; // ~42
		try {
			final MFSConfig config = MFSConfig.getInstance();
			int rc = 0;
			removeMyListeners();

			do {
				MFSWorkUnitPNSNDialog myWrkUnitPNSND = new MFSWorkUnitPNSNDialog(getParentFrame());
				myWrkUnitPNSND.setLocationRelativeTo(getParentFrame()); // ~17A
				myWrkUnitPNSND.setVisible(true);

				if (myWrkUnitPNSND.getPressedEnter()) {
					this.fieldCurrMctl = myWrkUnitPNSND.getMctl();

					StringBuffer data = new StringBuffer();
					data.append("REMAP     "); //$NON-NLS-1$
					data.append(this.fieldCurrMctl);
					data.append("U"); //$NON-NLS-1$
					data.append("PREP"); //$NON-NLS-1$
					data.append(config.get8CharCellType());

					MFSTransaction remap = new MFSFixedTransaction(data.toString());
					remap.setActionMessage("Remapping Work Unit, Please Wait..."); //$NON-NLS-1$
					MFSComm.getInstance().execute(remap, this);
					rc = remap.getReturnCode();

					if (rc == 30 || rc == 0) {
						StringBuffer data2 = new StringBuffer();
						data2.append("STR_PREP  "); //$NON-NLS-1$
						data2.append(this.fieldCurrMctl);
						data2.append("PREP"); //$NON-NLS-1$
						data2.append(config.get8CharUser());
						data2.append(config.get8CharCell());
						data2.append("J"); //$NON-NLS-1$

						MFSTransaction str_prep = new MFSFixedTransaction(data2.toString());
						str_prep.setActionMessage("Starting Preparation Operation, Please Wait..."); //$NON-NLS-1$
						MFSComm.getInstance().execute(str_prep, this);
						rc = str_prep.getReturnCode();

						if (rc == 0) {
							MFSDeconfigPanel deconfig = new MFSDeconfigPanel(getParentFrame(), this,
									MFSDeconfigPanel.PREP_SCREEN_NAME);
							MFSHeaderRec myHeadRec = deconfig.loadListModel(str_prep.getOutput());
							deconfig.sortListModel();
							deconfig.loadPartsModels();

							if (!this.cnfgEfficiencyDumbClientOn) {
								/*
								 * now go try to get the instructions for this
								 * work unit/operation
								 */
								MfsXMLDocument xml_dataRI = new MfsXMLDocument("RTV_INSTR"); //$NON-NLS-1$
								xml_dataRI.addOpenTag("DATA"); //$NON-NLS-1$
								xml_dataRI.addCompleteField("MCTL", this.fieldCurrMctl); //$NON-NLS-1$
								xml_dataRI.addCompleteField("NMBR", myHeadRec.getNmbr()); //$NON-NLS-1$
								xml_dataRI.addCompleteField("PRLN", //$NON-NLS-1$
										myHeadRec.getPrln().concat("        ").substring(0, 8)); //$NON-NLS-1$
								xml_dataRI.addCompleteField("PROD", //$NON-NLS-1$
										myHeadRec.getProd().concat("        ").substring(0, 8)); //$NON-NLS-1$
								xml_dataRI.addCompleteField("USER", config.get8CharUser()); //$NON-NLS-1$
								xml_dataRI.addCompleteField("CELL", config.get8CharCell()); //$NON-NLS-1$
								xml_dataRI.addCompleteField("CTYP", config.get8CharCellType()); //$NON-NLS-1$
								xml_dataRI.addCompleteField("DSPO", "N"); //$NON-NLS-1$ //$NON-NLS-2$
								xml_dataRI.addCompleteField("OLEV", myHeadRec.getOlev()); //$NON-NLS-1$
								xml_dataRI.addCloseTag("DATA"); //$NON-NLS-1$
								xml_dataRI.finalizeXML();

								MFSTransaction rtv_instr = new MFSXmlTransaction(xml_dataRI.toString());
								rtv_instr.setActionMessage("Retrieving Instructions, Please Wait..."); //$NON-NLS-1$
								MFSComm.getInstance().execute(rtv_instr, this);
								rc = rtv_instr.getReturnCode();

								if (rc == 0) {
									MfsXMLParser xmlParserRI = new MfsXMLParser(rtv_instr.getOutput());
									deconfig.loadInstructions(xmlParserRI);
								}
								// else bad return code from RTV_INSTR
								else {
									IGSMessageBox.showOkMB(getParentFrame(), null, rtv_instr.getErms(), null);
								}
							}

							if (rc == 0) {
								deconfig.setupPartInstPanel();
								deconfig.prepareForDisplay(this.fieldCurrMctl);
								deconfig.configureButtons();

								deconfig.disableInstructionScrollRectToVisible();
								getParentFrame().displayMFSPanel(deconfig);
								deconfig.enableInstructionScrollRectToVisible();
								autoRepeatTransaction = false;
							}
						} /* end of good rc from STR_PREP */
						else {
							IGSMessageBox.showOkMB(getParentFrame(), null, str_prep.getErms(), null);
						}
					} else {
						IGSMessageBox.showOkMB(getParentFrame(), null, remap.getErms(), null);
					}
				} /* work unit entered */
				else {
					autoRepeatTransaction = false;
				}
			} while (autoRepeatTransaction);
		} catch (Exception e) {
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
		addMyListeners();
	}


	/** ~55A
	 * Invoked when {@link #pbSapApplyPrep} is selected to prepare a work unit
	 * for reapply.
	 */
	private void sapApplyPrep() {
		boolean autoRepeatTransaction = this.cnfgEfficiencyOn; // ~42
		try {
			final MFSConfig config = MFSConfig.getInstance();
			int rc = 0;
			removeMyListeners();

			do {
				MFSWorkUnitPNSNDialog myWrkUnitPNSND = new MFSWorkUnitPNSNDialog(getParentFrame());
				myWrkUnitPNSND.setLocationRelativeTo(getParentFrame()); // ~17A
				myWrkUnitPNSND.setVisible(true);

				if (myWrkUnitPNSND.getPressedEnter()) {
					this.fieldCurrMctl = myWrkUnitPNSND.getMctl();

					StringBuffer data = new StringBuffer();
					data.append("REMAP     "); //$NON-NLS-1$
					data.append(this.fieldCurrMctl);
					data.append("U"); //$NON-NLS-1$
					data.append("PREP"); //$NON-NLS-1$
					data.append(config.get8CharCellType());

					MFSTransaction remap = new MFSFixedTransaction(data.toString());
					remap.setActionMessage("Remapping Work Unit, Please Wait..."); //$NON-NLS-1$
					MFSComm.getInstance().execute(remap, this);
					rc = remap.getReturnCode();

					if (rc == 30 || rc == 0) {
						StringBuffer data2 = new StringBuffer();
						data2.append("STRSAPPREP"); //$NON-NLS-1$
						data2.append(this.fieldCurrMctl);
						data2.append("PREP"); //$NON-NLS-1$
						data2.append(config.get8CharUser());
						data2.append(config.get8CharCell());
						data2.append("J"); //$NON-NLS-1$

						MFSTransaction strsapprep = new MFSFixedTransaction(data2.toString());
						strsapprep.setActionMessage("Starting Preparation Operation, Please Wait..."); //$NON-NLS-1$
						MFSComm.getInstance().execute(strsapprep, this);
						rc = strsapprep.getReturnCode();

						if (rc == 0) {
							MFSSapDeconfigPanel deconfig = new MFSSapDeconfigPanel(getParentFrame(), this,
									MFSSapDeconfigPanel.PREP_SCREEN_NAME);
							MFSHeaderRec myHeadRec = deconfig.loadListModel(strsapprep.getOutput());
							deconfig.sortListModel();
							deconfig.loadPartsModels();

							if (!this.cnfgEfficiencyDumbClientOn) {
								/*
								 * now go try to get the instructions for this
								 * work unit/operation
								 */
								MfsXMLDocument xml_dataRI = new MfsXMLDocument("RTV_INSTR"); //$NON-NLS-1$
								xml_dataRI.addOpenTag("DATA"); //$NON-NLS-1$
								xml_dataRI.addCompleteField("MCTL", this.fieldCurrMctl); //$NON-NLS-1$
								xml_dataRI.addCompleteField("NMBR", myHeadRec.getNmbr()); //$NON-NLS-1$
								xml_dataRI.addCompleteField("PRLN", //$NON-NLS-1$
										myHeadRec.getPrln().concat("        ").substring(0, 8)); //$NON-NLS-1$
								xml_dataRI.addCompleteField("PROD", //$NON-NLS-1$
										myHeadRec.getProd().concat("        ").substring(0, 8)); //$NON-NLS-1$
								xml_dataRI.addCompleteField("USER", config.get8CharUser()); //$NON-NLS-1$
								xml_dataRI.addCompleteField("CELL", config.get8CharCell()); //$NON-NLS-1$
								xml_dataRI.addCompleteField("CTYP", config.get8CharCellType()); //$NON-NLS-1$
								xml_dataRI.addCompleteField("DSPO", "N"); //$NON-NLS-1$ //$NON-NLS-2$
								xml_dataRI.addCompleteField("OLEV", myHeadRec.getOlev()); //$NON-NLS-1$
								xml_dataRI.addCloseTag("DATA"); //$NON-NLS-1$
								xml_dataRI.finalizeXML();

								MFSTransaction rtv_instr = new MFSXmlTransaction(xml_dataRI.toString());
								rtv_instr.setActionMessage("Retrieving Instructions, Please Wait..."); //$NON-NLS-1$
								MFSComm.getInstance().execute(rtv_instr, this);
								rc = rtv_instr.getReturnCode();

								if (rc == 0) {
									MfsXMLParser xmlParserRI = new MfsXMLParser(rtv_instr.getOutput());
									deconfig.loadInstructions(xmlParserRI);
								}
								// else bad return code from RTV_INSTR
								else {
									IGSMessageBox.showOkMB(getParentFrame(), null, rtv_instr.getErms(), null);
								}
							}

							if (rc == 0) {
								deconfig.setupPartInstPanel();
								deconfig.prepareForDisplay(this.fieldCurrMctl);
								deconfig.configureButtons();

								deconfig.disableInstructionScrollRectToVisible();
								getParentFrame().displayMFSPanel(deconfig);
								deconfig.enableInstructionScrollRectToVisible();
								autoRepeatTransaction = false;
							}
						} /* end of good rc from STR_PREP */
						else {
							IGSMessageBox.showOkMB(getParentFrame(), null, strsapprep.getErms(), null);
						}
					} else {
						IGSMessageBox.showOkMB(getParentFrame(), null, remap.getErms(), null);
					}
				} /* work unit entered */
				else {
					autoRepeatTransaction = false;
				}
			} while (autoRepeatTransaction);
		} catch (Exception e) {
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
		addMyListeners();
	}

	// ~29A new method to start reduction operation
	/**
	 * Invoked when {@link #pbReduction} is selected to prepare a work unit for
	 * reapply.
	 */
	private void reduction() {
		boolean autoRepeatTransaction = this.cnfgEfficiencyOn; // ~42
		try {
			final MFSConfig config = MFSConfig.getInstance();
			int rc = 0;
			removeMyListeners();

			do {
				MFSWorkUnitPNSNDialog myWrkUnitPNSND = new MFSWorkUnitPNSNDialog(getParentFrame());
				myWrkUnitPNSND.setLocationRelativeTo(getParentFrame()); // ~17A
				myWrkUnitPNSND.setVisible(true);

				if (myWrkUnitPNSND.getPressedEnter()) {
					this.fieldCurrMctl = myWrkUnitPNSND.getMctl();

					StringBuffer data = new StringBuffer();
					data.append("REMAP     "); //$NON-NLS-1$
					data.append(this.fieldCurrMctl);
					data.append("U"); //$NON-NLS-1$
					data.append("RRUM"); //$NON-NLS-1$
					data.append(config.get8CharCellType());

					MFSTransaction remap = new MFSFixedTransaction(data.toString());
					remap.setActionMessage("Remapping Work Unit, Please Wait..."); //$NON-NLS-1$
					MFSComm.getInstance().execute(remap, this);
					rc = remap.getReturnCode();

					if (rc != 30 && rc != 0) {
						IGSMessageBox.showOkMB(getParentFrame(), null, remap.getErms(), null);
					} else {

						StringBuffer data2 = new StringBuffer();
						data2.append("STR_REDUCE"); //$NON-NLS-1$
						data2.append(config.get8CharCell());
						data2.append(config.get8CharCellType());
						data2.append(config.get8CharUser());
						data2.append(this.fieldCurrMctl);

						MFSTransaction str_reduce = new MFSFixedTransaction(data2.toString());
						str_reduce.setActionMessage("Starting Reduction Operation, Please Wait..."); //$NON-NLS-1$
						MFSComm.getInstance().execute(str_reduce, this);
						rc = str_reduce.getReturnCode();
						if (rc != 0) {
							IGSMessageBox.showOkMB(getParentFrame(), null, str_reduce.getErms(), null);
						}

						else {
							MFSReductionPanel reduction = new MFSReductionPanel(getParentFrame(), this);
							MFSHeaderRec myHeadRec = reduction.loadListModel(str_reduce.getOutput());

							reduction.loadPartsModels();

							if (!this.cnfgEfficiencyDumbClientOn) // ~42
							{
								/*
								 * now go try to get the instructions for this
								 * work unit/operation
								 */
								MfsXMLDocument xml_dataRI = new MfsXMLDocument("RTV_INSTR"); //$NON-NLS-1$
								xml_dataRI.addOpenTag("DATA"); //$NON-NLS-1$
								xml_dataRI.addCompleteField("MCTL", myHeadRec.getMctl()); //$NON-NLS-1$
								xml_dataRI.addCompleteField("NMBR", myHeadRec.getNmbr()); //$NON-NLS-1$
								xml_dataRI.addCompleteField("PRLN", //$NON-NLS-1$
										myHeadRec.getPrln().concat("        ").substring(0, 8)); //$NON-NLS-1$
								xml_dataRI.addCompleteField("PROD", //$NON-NLS-1$
										myHeadRec.getProd().concat("        ").substring(0, 8)); //$NON-NLS-1$
								xml_dataRI.addCompleteField("USER", config.get8CharUser()); //$NON-NLS-1$
								xml_dataRI.addCompleteField("CELL", config.get8CharCell()); //$NON-NLS-1$
								xml_dataRI.addCompleteField("CTYP", config.get8CharCellType()); //$NON-NLS-1$
								xml_dataRI.addCompleteField("DSPO", "N"); //$NON-NLS-1$ //$NON-NLS-2$
								xml_dataRI.addCompleteField("OLEV", myHeadRec.getOlev()); //$NON-NLS-1$
								xml_dataRI.addCloseTag("DATA"); //$NON-NLS-1$
								xml_dataRI.finalizeXML();

								MFSTransaction rtv_instr = new MFSXmlTransaction(xml_dataRI.toString());
								rtv_instr.setActionMessage("Retrieving Instructions, Please Wait..."); //$NON-NLS-1$
								MFSComm.getInstance().execute(rtv_instr, this);
								rc = rtv_instr.getReturnCode();

								if (rc == 0) {
									MfsXMLParser xmlParserRI = new MfsXMLParser(rtv_instr.getOutput());
									reduction.loadInstructions(xmlParserRI);
								} else {
									IGSMessageBox.showOkMB(getParentFrame(), null, rtv_instr.getErms(), null);
								}
							}

							if (rc == 0) {
								autoRepeatTransaction = false;

								reduction.setupPartInstPanel();
								startReduceTransactions(myHeadRec); /* ~34 */

								reduction.prepareForDisplay(myHeadRec.getMctl());
								reduction.configureReduction();

								reduction.disableInstructionScrollRectToVisible();
								getParentFrame().displayMFSPanel(reduction);
								reduction.enableInstructionScrollRectToVisible();
							} /* end of good rc from rtv_instr */
						} /* end of good str_reduce */
					} /* end of good remap */
				} /* end of pressedEnter */
				else {
					autoRepeatTransaction = false;
				}
			} while (autoRepeatTransaction);
		} catch (Exception e) {
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		} finally {
			addMyListeners();
		}
	}

	// ~16A New method
	/**
	 * Invokes
	 * <code>showPNSNErrorList(fieldPNSNErrorData, fieldPNSNErrorIndex)</code>.
	 */
	public void reshowPNSNErrorList() {
		if (this.fieldPNSNErrorData.length() > 0) {
			showPNSNErrorList(this.fieldPNSNErrorData, this.fieldPNSNErrorIndex);
		}
	}

	/**
	 * Sets the value of the previous WIP Test Driver.
	 * 
	 * @param prevWipTestDriver
	 *            the previous WIP Test Driver
	 */
	public void setPrevWipTestDriver(String prevWipTestDriver) {
		this.fieldPrevWipTestDriver = prevWipTestDriver;
	}

	/**
	 * Shows an <code>MFSErrorMsgListDialog</code>. Displays the part detail
	 * screen if the user selects the part detail button; calls
	 * {@link #wipTestRecreate(String)} otherwise.
	 * 
	 * @param listData
	 *            the data for the list
	 * @param index
	 *            the list index to select
	 */
	public void showPNSNErrorList(String listData, int index) {
		this.fieldPNSNErrorData = listData;

		MFSErrorMsgListDialog msgBoxLrg = new MFSErrorMsgListDialog(getParentFrame());
		// ~16C Switched order of loadList and setListSelectedIndex
		msgBoxLrg.loadList(listData);
		msgBoxLrg.setListSelectedIndex(index);
		msgBoxLrg.setLocationRelativeTo(this);
		msgBoxLrg.setVisible(true);

		if (msgBoxLrg.getPressedPartDetail()) {
			this.fieldPNSNErrorIndex = msgBoxLrg.getSelectedIndex();

			String pn = msgBoxLrg.getSelectedPNSN().substring(0, 12);
			String sn = msgBoxLrg.getSelectedPNSN().substring(14, 26);

			// ~16C Create a new MFSPartFunctionDisplayPanel and call partDetail
			MFSPartFunctionDisplayPanel partFunc = new MFSPartFunctionDisplayPanel(getParentFrame(), this, pn, sn);
			partFunc.partDetail(false, true);
		} else {
			this.wipTestRecreate(this.fieldPrevWipTestDriver);
		}
	}

	// ~19A New method to call LOGNETM during signoff
	/**
	 * Invoked when {@link #pbSignoff} is selected to signoff the MFS Client.
	 */
	private void signoff() {
		String xml = MFSCommLogger.createLOGNETM_XML("MFSCLIENT"); //$NON-NLS-1$
		if (xml != null) {
			MFSTransaction lognetm = new MFSXmlTransaction(xml);
			lognetm.setActionMessage("Signing Off..."); // ~20C //$NON-NLS-1$
			MFSComm.getInstance().execute(lognetm, this);

			if (lognetm.getReturnCode() != 0) {
				IGSMessageBox.showOkMB(getParentFrame(), null, lognetm.getErms(), null);
			}
		}

		getParentFrame().restorePreviousScreen(this);
	}

	/**
	 * Invoked when {@link #pbStandAlone} is selected to display an
	 * <code>MFSStandAlonePanel</code>.
	 */
	private void standAlonePrinting() {
		try {
			removeMyListeners();

			MFSWorkUnitPNSNDialog myWrkUnitPNSND = new MFSWorkUnitPNSNDialog(getParentFrame());
			myWrkUnitPNSND.setLocationRelativeTo(getParentFrame()); // ~17A
			myWrkUnitPNSND.setVisible(true);

			if (myWrkUnitPNSND.getPressedEnter()) {
				String data = "RTV_HDRS  " + myWrkUnitPNSND.getMctl(); //$NON-NLS-1$
				MFSTransaction rtv_hdrs = new MFSFixedTransaction(data);
				rtv_hdrs.setActionMessage("Retrieving Work Unit's Header Information, Please Wait..."); //$NON-NLS-1$
				MFSComm.getInstance().execute(rtv_hdrs, this);

				if (rtv_hdrs.getReturnCode() != 0) {
					IGSMessageBox.showOkMB(getParentFrame(), null, rtv_hdrs.getErms(), null);
				} else {
					MFSStandAlonePanel standAlone = new MFSStandAlonePanel(getParentFrame(), this);
					standAlone.loadWorkUnitInfo(rtv_hdrs.getOutput(), myWrkUnitPNSND.getMctl());
					getParentFrame().displayMFSPanel(standAlone);
				}
			} // end pressed enter on inputing work unit
		} catch (Exception e) {
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		} finally {
			addMyListeners();
		}
	}

	/**
	 * Called by {@link #str_val()} to invoke the appropriate printing methods
	 * before the <code>MFSDeconfigPanel</code> is displayed.
	 * 
	 * @param headRec
	 *            the <code>MFSHeaderRec</code> for the work unit
	 */
	private void startValTransactions(MFSHeaderRec headRec) {
		try {
			/* Check if we need to print a Downlevel parts list Sheet */
			if (headRec.getFopr().equals("Y")) //$NON-NLS-1$
			{
				String cnfgData1 = "DOWNLEVELLIST," + headRec.getNmbr(); //$NON-NLS-1$
				String value = MFSConfig.getInstance().getConfigValue(cnfgData1);
				if (!value.equals(MFSConfig.NOT_FOUND)) {
					int qty = 1;
					if (!value.equals("")) //$NON-NLS-1$
					{
						qty = Integer.parseInt(value);
					}
					MFSPrintingMethods.downlevellist(headRec.getMctl(), qty, getParentFrame());
				}
			}

		} // end try
		catch (Exception e) {
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
	}

	/**
	 * Called by {@link #str_reduce} to invoke the appropriate printing methods
	 * before the <code>MFSReductionPanel</code> is displayed. ~34
	 * 
	 * @param headRec
	 *            the <code>MFSHeaderRec</code> for the work unit
	 */
	private void startReduceTransactions(MFSHeaderRec headRec) {
		try {
			final String nmbrPrln = headRec.getNmbr() + "," + headRec.getPrln().trim(); //$NON-NLS-1$
			final String nmbrAll = headRec.getNmbr() + ",*ALL"; //$NON-NLS-1$
			if (headRec.getFopr().equals("Y")) //$NON-NLS-1$
			{
				/* Check if we need to create and print a container label */
				swContainerLabel(headRec, nmbrPrln, nmbrAll, REDUCTION_SOURCE);

			} // end of fopr = 'Y'

		} // end try
		catch (Exception e) {
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
	}

	private void releaseBuildAhead() {
		try {
			removeMyListeners();
			MFSBuildAheadPanel rbap = new MFSBuildAheadPanel(getParentFrame(), this, MFSBuildAheadPanel.BARELEASE);

			if (rbap.retrieveBAInfo(this, "RLS")) { //$NON-NLS-1$
				getParentFrame().displayMFSPanel(rbap);
			}
		} catch (Exception e) {
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		} finally {
			addMyListeners();
		}
	}

	private void applyBuildAhead() {
		try {
			removeMyListeners();
			MFSBuildAheadPanel abap = new MFSBuildAheadPanel(getParentFrame(), this, MFSBuildAheadPanel.BAAPPLY);

			if (abap.retrieveBAInfo(this, "APY")) { //$NON-NLS-1$
				getParentFrame().displayMFSPanel(abap);
			}
		} catch (Exception e) {
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		} finally {
			addMyListeners();
		}
	}

	/**
	 * Called by {@link #str_wu} to invoke the appropriate printing methods
	 * before the <code>MFSDirectWorkPanel</code> is displayed.
	 * 
	 * @param headRec
	 *            the <code>MFSHeaderRec</code> for the work unit
	 */
	private void startWorkUnitTransactions(MFSHeaderRec headRec) {
		try {
			// ~16C Pass nmbrPrln and nmbrAll to each of the sw methods
			// instead of recreating the Strings in each sw method
			final String nmbrPrln = headRec.getNmbr() + "," + headRec.getPrln().trim(); //$NON-NLS-1$
			final String nmbrAll = headRec.getNmbr() + ",*ALL"; //$NON-NLS-1$
			if (headRec.getFopr().equals("Y")) //$NON-NLS-1$
			{
				if (headRec.getCons().equals("F")) // ~47A
				{
					/* Check if we need to print a FoD label */
					swFoDLabel(headRec, nmbrPrln, nmbrAll); // ~46A
				}
				/* Check if we need to print a Weight label */
				swWeightLabel(headRec, nmbrPrln, nmbrAll); // ~41A

				/* Check if we need to print a Warranty Card label */
				swWarrantyCard(headRec, nmbrPrln, nmbrAll); // ~41A

				/* Check if we need to print a carriercomn label */
				swCarrierComnLabel(headRec, nmbrPrln, nmbrAll); // ~29A

				/* Check if we need to create and print a container label */
				swContainerLabel(headRec, nmbrPrln, nmbrAll, DIRECT_WORK_SOURCE); /* ~34 */

				/* Check if we need to print a system number label */
				swSiemensLabels(headRec, nmbrPrln, nmbrAll);

				/* Check if we need to print a system number label */
				swSystemNumberLabel(headRec, nmbrPrln, nmbrAll);

				/* Check if we need to print a system info label */
				swSystemInfoLabel(headRec, nmbrPrln, nmbrAll);

				/* Check if we need to print a work unit label */
				swWorkUnitLabel(headRec, nmbrPrln, nmbrAll);

				/* Check if we need to print part number labels */
				swPartNumberLabel(headRec, nmbrPrln, nmbrAll);

				/* Check if we need to print a prodsub label */
				swProdSubLabel(headRec, nmbrPrln, nmbrAll);

				/* Check if we need to print a POK machtype */
				swL3cacheLabel(headRec, nmbrPrln, nmbrAll);

				/* Check if we need to print a tracksub label */
				swTrackSubLabel(headRec, nmbrPrln, nmbrAll);

				/* Check if we need to print a Roch FFBM Label */
				swFfbmLabel(headRec, nmbrPrln, nmbrAll);

				/* check if we need either of the configured Node labels */
				swConfNodeLabels(headRec, nmbrPrln, nmbrAll);

				/*
				 * Check if we need to print a configured serialized node label
				 */
				swConfSeriLabel(headRec, nmbrPrln, nmbrAll);

				/* Check if we need to print a Travel Sheet */
				swTravelLabel(headRec, nmbrPrln, nmbrAll);

				/* Check if we need to print a POK Kitting Report */
				swKittingLabels(headRec, nmbrPrln, nmbrAll);

				/* Check if we need to print a MIR Label */
				swMirLabel(headRec, nmbrPrln, nmbrAll);

				/* Check if we need to print a Work Unit Label */
				swRochWuLabel(headRec, nmbrPrln, nmbrAll);

				/* Check if we need to print a Software License Key Sheet */
				swSoftKeyLabel(headRec, nmbrPrln, nmbrAll);

				/* Check if we need to print a POD Sheet */
				swCuodLabels(headRec, nmbrPrln, nmbrAll);

				/* Check if we need to print a POK machtype */
				swMachTypeLabel(headRec, nmbrPrln, nmbrAll);

				/* Check if we need to print an 11S Work Unit label */
				swElevenSWULabel(headRec, nmbrPrln, nmbrAll);

				/* Check if we need to print an 11S Work Unit label */
				swSmallWULabel(headRec, nmbrPrln, nmbrAll);

				/*
				 * Check if we need to print a TCOD Billing Notification Sheet
				 */
				swTcodLabels(headRec, nmbrPrln, nmbrAll);

				/* Check if we need to print an advanced feature model Sheet */
				swAdvFeatModelLabel(headRec, nmbrPrln, nmbrAll);

				/* Check if we need to print a deconfig location chart */
				swDeconfigLocLabel(headRec, nmbrPrln, nmbrAll);

				/* Check if we need to print a prod feature code Sheet */
				swProdFeatCodeLabel(headRec, nmbrPrln, nmbrAll);

				/* Check if we need to print any prod pack Sheets */
				swProdPackLabels(headRec, nmbrPrln, nmbrAll);

				/* Check if we need to print any machine index card labels */
				swMachineIndexCardLabels(headRec, nmbrPrln, nmbrAll);

				/* Check if we need to print MAC ID sheets */
				swMACIDLabel(headRec, nmbrPrln, nmbrAll);

				/* Check if we need to print SPD11S label */
				swSPD11SLabel(headRec, nmbrPrln, nmbrAll);

				/* Check if the Rebrand labels are configured */
				/*
				 * Rebrand labels now not printed here since don't have the coo
				 * value yet
				 */
				/* swRebrandLabels(config,headRec) */

				/* Check if we need to print MATPMCSN label */
				swMATPMCSNLabel(headRec, nmbrPrln, nmbrAll);

				/* Check if we need to print MATPMCSN1S label */
				swMATPMCSN1SLabel(headRec, nmbrPrln, nmbrAll);

				/* Check if we need to print KEYCODES label */
				swKeyCodesLabel(headRec, nmbrPrln, nmbrAll);

				/*
				 * Call the main ID printing trx whenever a workunit is
				 * started @1
				 */
				swMainID(headRec, nmbrPrln, nmbrAll);

				/* Check if we need to print ASSEMBLY label ~13 */
				swAssemblyLabel(headRec, nmbrPrln, nmbrAll);

				/* Check if we need to print a CTRLNUMBR label ~22A */
				swCTRLNUMBRLabel(headRec, nmbrPrln, nmbrAll); /* ~22A */
				/* Check if we need to print a SYSCERT label ~22A */
				swSYSCERTLabel(headRec, nmbrPrln, nmbrAll); /* ~22A */

				/* On Demand Label printing */
				onDemandTrigger(headRec, "STARTWU"); // ~44A //$NON-NLS-1$

			} // end of fopr = 'Y'

			/* Check if we need to print any machine index card labels */
			swWWIDLabel(headRec, nmbrPrln, nmbrAll);

			/* Call the tcod printing trx whenever a workunit is started @1 */
			swTcodTrx(headRec);

			/*
			 * Call the software keycode printing trx whenever a workunit is
			 * started @5
			 */
			swSoftwareKeycodeTrx(headRec, nmbrPrln, nmbrAll);

			// ~15C Moved from between swKittingLabels/swMirLabel to here
			// so swSystemPasswordLabel is called every time a workunit is
			// started
			/* Check if we need to print a SYSTEM PWD Sheet */
			swSystemPasswordLabel(headRec, nmbrPrln, nmbrAll);

			swAssignIOSN(headRec, nmbrPrln, nmbrAll); // ~18A
		} // end try
		catch (Exception e) {
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
	}

	/** Invoked when {@link #pbFkit} is selected to start an FKIT. */
	private void str_fkit() {
		// Continue after Inspection questions are asked
		// Initialize to true in case there are no Inspection questions to ask
		boolean iq_continue = true;
		int rc = 0;
		String forceStart = "          "; // ~14A //$NON-NLS-1$
		boolean showError = true; // ~14A
		boolean autoRepeatTransaction = this.cnfgEfficiencyOn; // ~42

		try {
			removeMyListeners();
			do {
				MFSWorkUnitPNSNDialog myWrkUnitPNSND = new MFSWorkUnitPNSNDialog(getParentFrame());
				myWrkUnitPNSND.setFkitFlag("Y"); //$NON-NLS-1$
				myWrkUnitPNSND.setLocationRelativeTo(getParentFrame()); // ~17A
				myWrkUnitPNSND.setVisible(true);

				if (myWrkUnitPNSND.getPressedEnter()) {
					this.fieldCurrMctl = myWrkUnitPNSND.getMctl();

					final MFSConfig config = MFSConfig.getInstance();
					StringBuffer data = new StringBuffer();
					data.append("FKSTR_RTR "); //$NON-NLS-1$
					data.append(this.fieldCurrMctl);
					data.append(config.get8CharCell());
					data.append(config.get8CharCellType());
					data.append(config.get8CharUser());
					data.append(forceStart); // ~14A

					MFSTransaction fkstr_rtr = new MFSFixedTransaction(data.toString());
					fkstr_rtr.setActionMessage("Starting FKIT for Work Unit = " //$NON-NLS-1$
							+ this.fieldCurrMctl + ", Please Wait..."); //$NON-NLS-1$
					MFSComm.getInstance().execute(fkstr_rtr, this);
					rc = fkstr_rtr.getReturnCode();

					// ******************************* ~14 START
					// ***************************************
					if (rc == 4) {
						forceStart = "FORCESTART"; //$NON-NLS-1$

						// ~16C Use IGSMessageBox.showYesNoMB
						StringBuffer forceMsg = new StringBuffer();
						forceMsg.append(fkstr_rtr.getOutput().trim());
						forceMsg.append("\nWould you like to continue starting the Work Unit anyway?"); //$NON-NLS-1$
						boolean force = IGSMessageBox.showYesNoMB(getParentFrame(), null, forceMsg.toString(), null);

						if (force) {
							data = new StringBuffer();
							data.append("FKSTR_RTR "); //$NON-NLS-1$
							data.append(this.fieldCurrMctl);
							data.append(config.get8CharCell());
							data.append(config.get8CharCellType());
							data.append(config.get8CharUser());
							data.append(forceStart);

							fkstr_rtr = new MFSFixedTransaction(data.toString());
							fkstr_rtr.setActionMessage("Starting FKIT for Work Unit = " //$NON-NLS-1$
									+ this.fieldCurrMctl + ", Please Wait..."); //$NON-NLS-1$
							MFSComm.getInstance().execute(fkstr_rtr, this);
							rc = fkstr_rtr.getReturnCode();
						} else {
							showError = false;
						}
					}
					// ******************************* ~14 END
					// **************************************

					if (rc == 0) {
						MFSDirectWorkPanel dirWorkPanel = getParentFrame().getDirectWorkPanel();
						dirWorkPanel.setScreenName(MFSDirectWorkPanel.FKIT_SCREEN_NAME);
						dirWorkPanel.setSource(this);

						MFSHeaderRec myHeadRec = dirWorkPanel.loadListModel(fkstr_rtr.getOutput());
						dirWorkPanel.loadPartsModels();
						dirWorkPanel.clearInstVector();

						dirWorkPanel.setupPartInstPanel();
						dirWorkPanel.prepareForDisplay(this.fieldCurrMctl);
						dirWorkPanel.configureFKIT();

						dirWorkPanel.disableInstructionScrollRectToVisible();
						getParentFrame().displayMFSPanel(dirWorkPanel);
						dirWorkPanel.enableInstructionScrollRectToVisible();

						if (myHeadRec.getIspq().equals("B") //$NON-NLS-1$
								|| myHeadRec.getIspq().equals("S")) //$NON-NLS-1$
						{
							RTV_IQ rtvIQ = new RTV_IQ(this); // ~45
							rtvIQ.setInputNmbr(myHeadRec.getNmbr());
							rtvIQ.setInputPrln(myHeadRec.getPrln());
							rtvIQ.setInputProd(myHeadRec.getProd());
							rtvIQ.setInputX("1"); //$NON-NLS-1$
							rtvIQ.execute();
							rc = rtvIQ.getReturnCode();

							if (rc != 0) {
								// Don't allow the program to get by the
								// inspection questions
								iq_continue = false;
								IGSMessageBox.showOkMB(getParentFrame(), null, rtvIQ.getErrorMessage(), null);
							} else {
								MFSIQDialog iq = new MFSIQDialog(getParentFrame());
								iq.loadIQ(rtvIQ.getOutputIQ());
								iq.setLocationRelativeTo(getParentFrame()); // ~17A
								iq.setVisible(true);

								if (iq.getPressedCancel()) {
									iq_continue = false;
									// user hit the cancel key, so suspend the
									// work unit
									dirWorkPanel.suspendNoQC("S", "0000"); //$NON-NLS-1$ //$NON-NLS-2$
								}
							}
						} // end of Ispq = 'B' or 'S'

						if (iq_continue) {
							if (myHeadRec.getPfsn().equals("Y")) //$NON-NLS-1$
							{
								/*
								 * regardless of the value in the WUSAPN field,
								 * we need to prompt for this in case they have
								 * suspended this workunit - I need to get the
								 * MCM Assembly Part Number from the 11S barcode
								 * they wand in
								 */

								MFSSerialNumDialog serialNumD = new MFSSerialNumDialog(dirWorkPanel,
										myHeadRec.getMctl());

								if (showWUSerialNumberPrompt(
										this.fieldCurrMctl)) { /*
																 * ~49A Validate
																 * if the
																 * MFSSerialNumDialog
																 * must be
																 * shown.
																 */
									/*
									 * ~50C Use this.fieldCurrMctl instead
									 * myHeadRec.getMctl()
									 */

									serialNumD.setLocationRelativeTo(getParentFrame()); // ~17A
									serialNumD.setVisible(true);

									myHeadRec.setSapn(serialNumD.getSerialNum());

									if (serialNumD.getPressedSuspend()) {
										// user hit the cancel key, so suspend
										// the work unit
										dirWorkPanel.suspendNoQC("S", "0000"); //$NON-NLS-1$ //$NON-NLS-2$
									} else {
										/*
										 * add MCM Part Number to configuration
										 * data
										 */
										config.setConfigValue("MCMPARTNUM", serialNumD.getPartNum()); //$NON-NLS-1$
									}
								} /* ~49A */

							} // end of 'Y' in PFSN field
						} // end of iq_continue
						autoRepeatTransaction = false;
						if (this.cnfgEfficiencyOn) // ~42
						{
							dirWorkPanel.startAutoSearchLog();
						}
					} // end good return code from str_wu
						// else bad return code from str_wu
					else {
						// ~14A Add showError check
						if (showError) {
							IGSMessageBox.showOkMB(getParentFrame(), null, fkstr_rtr.getErms(), null);
						}
					}
				} // end of pressed Enter on WorkUnit Dialog
				else {
					autoRepeatTransaction = false;
				}
			} while (autoRepeatTransaction);
		} catch (Exception e) {
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
		addMyListeners();
	}

	/**
	 * Calls the STR_VAL and RTV_INSTR transactions. If the transactions are
	 * successful, displays an <code>MFSDeconfigPanel</code>.
	 * 
	 * @return 0 on success; nonzero on error
	 */
	private int str_val() {
		int rc = 0;
		try {
			final MFSConfig config = MFSConfig.getInstance();

			StringBuffer data = new StringBuffer();
			data.append("STR_VAL   "); //$NON-NLS-1$
			data.append(this.fieldCurrMctl);
			data.append(config.get8CharCell());
			data.append(config.get8CharCellType());
			data.append(config.get8CharUser());

			MFSTransaction str_val = new MFSFixedTransaction(data.toString());
			str_val.setActionMessage("Starting the Work Unit, Please Wait..."); //$NON-NLS-1$
			MFSComm.getInstance().execute(str_val, this);
			rc = str_val.getReturnCode();

			if (rc == 0) {
				MFSDeconfigPanel deconfig = new MFSDeconfigPanel(getParentFrame(), this,
						MFSDeconfigPanel.DECONFIG_SCREEN_NAME);
				MFSHeaderRec myHeadRec = deconfig.loadListModel(str_val.getOutput());
				deconfig.sortListModel();
				deconfig.loadPartsModels();

				if (!this.cnfgEfficiencyDumbClientOn) // ~42
				{
					MfsXMLDocument xml_data1 = new MfsXMLDocument("RTV_INSTR"); //$NON-NLS-1$
					xml_data1.addOpenTag("DATA"); //$NON-NLS-1$
					xml_data1.addCompleteField("MCTL", this.fieldCurrMctl); //$NON-NLS-1$
					xml_data1.addCompleteField("NMBR", myHeadRec.getNmbr()); //$NON-NLS-1$
					xml_data1.addCompleteField("PRLN", //$NON-NLS-1$
							myHeadRec.getPrln().concat("        ").substring(0, 8)); //$NON-NLS-1$
					xml_data1.addCompleteField("PROD", //$NON-NLS-1$
							myHeadRec.getProd().concat("        ").substring(0, 8)); //$NON-NLS-1$
					xml_data1.addCompleteField("USER", config.get8CharUser()); //$NON-NLS-1$
					xml_data1.addCompleteField("CELL", config.get8CharCell()); //$NON-NLS-1$
					xml_data1.addCompleteField("CTYP", config.get8CharCellType()); //$NON-NLS-1$
					xml_data1.addCompleteField("DSPO", "N"); //$NON-NLS-1$ //$NON-NLS-2$
					xml_data1.addCompleteField("OLEV", myHeadRec.getOlev()); //$NON-NLS-1$
					xml_data1.addCloseTag("DATA"); //$NON-NLS-1$
					xml_data1.finalizeXML();

					MFSTransaction rtv_instr = new MFSXmlTransaction(xml_data1.toString());
					rtv_instr.setActionMessage("Retrieving Instructions, Please Wait..."); //$NON-NLS-1$
					MFSComm.getInstance().execute(rtv_instr, this);
					rc = rtv_instr.getReturnCode();

					if (rc == 0) {
						MfsXMLParser xmlParser = new MfsXMLParser(rtv_instr.getOutput());
						deconfig.loadInstructions(xmlParser);
					}
					// else bad return code from RTV_INSTR
					else {
						IGSMessageBox.showOkMB(getParentFrame(), null, rtv_instr.getErms(), null);
					}
				}

				if (rc == 0) {
					deconfig.setupPartInstPanel();
					deconfig.prepareForDisplay(this.fieldCurrMctl);
					deconfig.configureButtons();

					startValTransactions(myHeadRec);

					deconfig.disableInstructionScrollRectToVisible();
					getParentFrame().displayMFSPanel(deconfig);
					deconfig.enableInstructionScrollRectToVisible();
				}
			} // end good return code from str_val
				// else bad return code from str_val
			else {
				IGSMessageBox.showOkMB(getParentFrame(), null, str_val.getErms(), null);
			}
		} catch (Exception e) {
			rc = 11;
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
		return rc;
	}

	/**
	 * Invoked to start a work unit.
	 * 
	 * @param who
	 *            "Z" for tear down, "J" otherwise
	 * @return 0 on success; nonzero on error
	 */
	public int str_wu(String who) {
		return str_wu(who, ""); //$NON-NLS-1$
	}

	/**
	 * Invoked to start a work unit.
	 * 
	 * @param who
	 *            "Z" for tear down, "J" otherwise
	 * @param prln
	 *            if mctl is blank and prln is populated we will do subassembly
	 *            logic to get next work unit on server
	 * @return 0 on success; nonzero on error
	 */
	public int str_wu(String who, String prln) {
		int rc = 0;
		final MFSConfig config = MFSConfig.getInstance();

		// Continue after Inspection questions are asked
		// Initialize to true in case there are no Inspection questions to ask
		boolean iq_continue = true;

		// Continue after software validation is performed
		// Initialize to true in case there is no validation to be done
		boolean software_continue = true;

		// Continue after the CSU validation is performed
		boolean csu_continue = true;

		// Continue after the WWNN validation is performed
		boolean wwnn_rescan_continue = true;

		// boolean for suspend pressed from serial num dialog
		boolean pressedSuspend = false;

		try {
			StringBuffer data = new StringBuffer(53);
			data.append("STR_WU    "); //$NON-NLS-1$
			data.append(config.get8CharCell());
			data.append(config.get8CharCellType());
			data.append(config.get8CharUser());
			if (this.fieldCurrMctl.trim().length() > 0) {
				data.append(this.fieldCurrMctl);
			} else {
				data.append("        "); //$NON-NLS-1$
			}
			data.append("1"); //$NON-NLS-1$
			data.append(who);
			if (this.cnfgEfficiencyOn) {
				String paddedPrln = prln + "        "; //$NON-NLS-1$
				paddedPrln = paddedPrln.substring(0, 8);
				data.append(paddedPrln);
				if (this.cnfgEfficiencyDumbClientOn) {
					data.append("Y"); //$NON-NLS-1$
				} else {
					data.append("N"); //$NON-NLS-1$
				}
			}

			MFSFixedTransaction str_wu = new MFSFixedTransaction(data.toString());
			str_wu.setActionMessage("Starting the Work Unit, Please Wait..."); //$NON-NLS-1$
			MFSComm.getInstance().execute(str_wu, this);
			rc = str_wu.getReturnCode();
			String output = str_wu.getOutput();

			if (rc == 0 || rc == 100 || rc == 200) {
				MFSDirectWorkPanel dirWorkPanel = getParentFrame().getDirectWorkPanel();
				dirWorkPanel.autoLoggedFlag = false;
				dirWorkPanel.setScreenName(MFSDirectWorkPanel.SCREEN_NAME);
				dirWorkPanel.setSource(this);

				MFSHeaderRec myHeadRec = dirWorkPanel.loadListModel(output);
				dirWorkPanel.loadPartsModels();
				dirWorkPanel.clearInstVector();

				// We want to set fieldCurrMctl based on the data that comes
				// back from STR_WU
				// but ONLY if we don't know the mctl yet
				if (this.fieldCurrMctl.trim().length() == 0) {
					this.fieldCurrMctl = myHeadRec.getMctl();
				}

				rc = 0;
				int shortIndex = 0;
				boolean nonShortFound = false;
				while (shortIndex < dirWorkPanel.getLMVectorSize() && !nonShortFound && rc == 0) {
					MFSComponentListModel tempLm = dirWorkPanel.getLmVectorElementAt(shortIndex);
					if (tempLm.getIsShort() == false) {
						nonShortFound = true;
					} else {
						MFSComponentRec cmp = tempLm.getComponentRecAt(0);
						MfsXMLDocument xml_data1 = new MfsXMLDocument("RTV_SPCINT"); //$NON-NLS-1$
						xml_data1.addOpenTag("DATA"); //$NON-NLS-1$
						xml_data1.addCompleteField("MCTL", this.fieldCurrMctl); //$NON-NLS-1$
						xml_data1.addCompleteField("NMBR", cmp.getNmbr()); //$NON-NLS-1$
						xml_data1.addCompleteField("INSX", tempLm.getSuff()); //$NON-NLS-1$
						xml_data1.addCompleteField("NMSQ", tempLm.getNmsq()); //$NON-NLS-1$
						xml_data1.addCompleteField("USER", config.get8CharUser()); //$NON-NLS-1$
						xml_data1.addCompleteField("CELL", config.get8CharCell()); //$NON-NLS-1$
						xml_data1.addCompleteField("CTYP", config.get8CharCellType()); //$NON-NLS-1$
						xml_data1.addCloseTag("DATA"); //$NON-NLS-1$
						xml_data1.finalizeXML();

						MFSTransaction rtv_spcint = new MFSXmlTransaction(xml_data1.toString());
						rtv_spcint.setActionMessage("Retrieving Instructions, Please Wait..."); //$NON-NLS-1$
						MFSComm.getInstance().execute(rtv_spcint, this);
						rc = rtv_spcint.getReturnCode();

						if (rc == 0) {
							MfsXMLParser xmlParser = new MfsXMLParser(rtv_spcint.getOutput());
							dirWorkPanel.loadShortInstructions(xmlParser);
						} else {
							IGSMessageBox.showOkMB(getParentFrame(), null, rtv_spcint.getErms(), null);
						}
					}
					shortIndex++;
				}

				if (rc == 0) {
					if (!this.cnfgEfficiencyDumbClientOn) // ~42
					{
						MfsXMLDocument xml_data1 = new MfsXMLDocument("RTV_INSTR"); //$NON-NLS-1$
						xml_data1.addOpenTag("DATA"); //$NON-NLS-1$
						xml_data1.addCompleteField("MCTL", this.fieldCurrMctl); //$NON-NLS-1$
						xml_data1.addCompleteField("NMBR", myHeadRec.getNmbr()); //$NON-NLS-1$
						xml_data1.addCompleteField("PRLN", //$NON-NLS-1$
								myHeadRec.getPrln().concat("        ").substring(0, 8)); //$NON-NLS-1$
						xml_data1.addCompleteField("PROD", //$NON-NLS-1$
								myHeadRec.getProd().concat("        ").substring(0, 8)); //$NON-NLS-1$
						xml_data1.addCompleteField("USER", config.get8CharUser()); //$NON-NLS-1$
						xml_data1.addCompleteField("CELL", config.get8CharCell()); //$NON-NLS-1$
						xml_data1.addCompleteField("CTYP", config.get8CharCellType());//$NON-NLS-1$
						xml_data1.addCompleteField("DSPO", "N"); //$NON-NLS-1$ //$NON-NLS-2$
						xml_data1.addCompleteField("OLEV", myHeadRec.getOlev()); //$NON-NLS-1$
						xml_data1.addCloseTag("DATA"); //$NON-NLS-1$
						xml_data1.finalizeXML();

						MFSTransaction rtv_instr = new MFSXmlTransaction(xml_data1.toString());
						rtv_instr.setActionMessage("Retrieving Instructions, Please Wait..."); //$NON-NLS-1$
						MFSComm.getInstance().execute(rtv_instr, this);
						rc = rtv_instr.getReturnCode();

						if (rc == 0) {
							MfsXMLParser xmlParser = new MfsXMLParser(rtv_instr.getOutput());
							dirWorkPanel.loadInstructions(xmlParser);
						}
						// else bad return code from RTV_INSTR
						else {
							IGSMessageBox.showOkMB(getParentFrame(), null, rtv_instr.getErms(), null);
						}
					}

					if (rc == 0) {
						dirWorkPanel.setupPartInstPanel();
						dirWorkPanel.prepareForDisplay(this.fieldCurrMctl);
						dirWorkPanel.configureNonFKIT(who);

						/*
						 * ~36A - Check entry CHECKHANDLING to indicate, based
						 * on operation number, if we need to check for special
						 * handling.
						 */
						checkHandling("M", this.fieldCurrMctl, myHeadRec.getNmbr()); //$NON-NLS-1$

						startWorkUnitTransactions(myHeadRec);

						dirWorkPanel.disableInstructionScrollRectToVisible();
						getParentFrame().displayMFSPanel(dirWorkPanel);
						dirWorkPanel.enableInstructionScrollRectToVisible();

						/*
						 * now we need to check if this operation requires us to
						 * perform a software validation look in the config file
						 * for a software validation entry
						 */
						String cnfgdata = "VALIDATESOFT," + myHeadRec.getNmbr() + "," //$NON-NLS-1$ //$NON-NLS-2$
								+ myHeadRec.getPrln().trim();
						String value = config.getConfigValue(cnfgdata);
						if (value.equals(MFSConfig.NOT_FOUND)) {
							cnfgdata = "VALIDATESOFT," + myHeadRec.getNmbr() + ",*ALL"; //$NON-NLS-1$ //$NON-NLS-2$
							value = config.getConfigValue(cnfgdata);
						}
						if (!value.equals(MFSConfig.NOT_FOUND)) {
							String data2 = "VRFY_SOFT " + myHeadRec.getMfgn() + myHeadRec.getIdss(); //$NON-NLS-1$
							MFSTransaction vrfy_soft = new MFSFixedTransaction(data2);
							MFSComm.getInstance().execute(vrfy_soft, this);
							rc = vrfy_soft.getReturnCode();

							// we only need to handle the bad return code case
							// if a good return, then we just finish thru the
							// code
							if (rc != 0) {
								software_continue = false;
								MFSValidateSoftDialog mySoftValJD = new MFSValidateSoftDialog(getParentFrame(),
										myHeadRec);
								mySoftValJD.setLocationRelativeTo(getParentFrame()); // ~17A
								mySoftValJD.setVisible(true);

								if (mySoftValJD.getPressedSuspend()) {
									dirWorkPanel.suspendNoQC("S", "0000"); //$NON-NLS-1$ //$NON-NLS-2$
								} else {
									if (!mySoftValJD.getOemo().equals("")) //$NON-NLS-1$
									{
										dirWorkPanel.suspendNoQC("F", "SWEC"); //$NON-NLS-1$ //$NON-NLS-2$
									}
								}
							}
						} // config file entry found

						/*
						 * perform the logic for retrieving inspection questions
						 * for this operation if the Ispq flag in the header, is
						 * set to "B" for both or "S" for start, then we will go
						 * to the server to find the inspection questions
						 */
						if (software_continue) {
							if (myHeadRec.getIspq().equals("B") //$NON-NLS-1$
									|| myHeadRec.getIspq().equals("S")) //$NON-NLS-1$
							{
								RTV_IQ rtvIQ = new RTV_IQ(this); // ~45
								rtvIQ.setInputNmbr(myHeadRec.getNmbr());
								rtvIQ.setInputPrln(myHeadRec.getPrln());
								rtvIQ.setInputProd(myHeadRec.getProd());
								rtvIQ.setInputX("1"); //$NON-NLS-1$
								rtvIQ.execute();
								rc = rtvIQ.getReturnCode();

								if (rc != 0) {
									iq_continue = false;
									// don't allow the program to get by the
									// inspection questions
									IGSMessageBox.showOkMB(getParentFrame(), null, rtvIQ.getErrorMessage(), null);
								} else {
									MFSIQDialog iq = new MFSIQDialog(getParentFrame());
									iq.loadIQ(rtvIQ.getOutputIQ());
									iq.setLocationRelativeTo(getParentFrame()); // ~17A
									iq.setVisible(true);
									if (iq.getPressedCancel()) {
										iq_continue = false;
										// user hit the cancel key, so suspend
										// the work unit
										dirWorkPanel.suspendNoQC("S", "0000"); //$NON-NLS-1$ //$NON-NLS-2$
									}
								}
							} // end of Ispq = 'B' or 'S'
						} // end software_continue is true

						if (iq_continue && software_continue) {
							// determine if we are configured for verifying CSU
							// (CUII) instructions. Use the top level product
							// line, because likely this will be for a shipgrp
							// work unit, but we don't want to configure for all
							// shipgroups.
							String cnfgData1 = "VERIFYCSU," + myHeadRec.getNmbr() + "," //$NON-NLS-1$ //$NON-NLS-2$
									+ myHeadRec.getPprl().trim();
							String CSUvalue = config.getConfigValue(cnfgData1);
							if (CSUvalue.equals(MFSConfig.NOT_FOUND)) {
								String cnfgData2 = "VERIFYCSU," + myHeadRec.getNmbr() + ",*ALL"; //$NON-NLS-1$ //$NON-NLS-2$
								CSUvalue = config.getConfigValue(cnfgData2);
							}
							if (!CSUvalue.equals(MFSConfig.NOT_FOUND)) {
								int rret = verifyCSU(myHeadRec.getMfgn()); /* @SW4C */

								while ((rret == 0) || (rret == -1)) {
									// wrong mfgn scanned in @SW4C
									if (rret == 0) {
										/*
										 * change messaging slightly
										 * here...because we aren't matching
										 * against IDSS anymore 02/14/2004
										 */
										StringBuffer msg = new StringBuffer();
										msg.append("Current MFGN: "); //$NON-NLS-1$
										msg.append(myHeadRec.getMfgn());
										msg.append(" does not match scanned in value"); //$NON-NLS-1$
										IGSMessageBox.showOkMB(getParentFrame(), null, msg.toString(), null);
									} else {
										String erms = "Invalid CSU barcode scanned in"; //$NON-NLS-1$
										IGSMessageBox.showOkMB(getParentFrame(), null, erms, null);
									}
									rret = verifyCSU(myHeadRec.getMfgn()); /* @SW4C */
								}
								if (rret == 1) {
									csu_continue = true;
								} else if (rret == 2) {
									// user hit the suspend key, so suspend the
									// work unit
									csu_continue = false;
									dirWorkPanel.suspendNoQC("S", "0000"); //$NON-NLS-1$ //$NON-NLS-2$
								}
							} // end configured for verifying CUII
						} // end iq_continue && software_continue

						/* @SW2 start */
						if (iq_continue && software_continue && csu_continue) {

							String cnfgData = "WWNNRESCAN," + myHeadRec.getNmbr() + "," //$NON-NLS-1$ //$NON-NLS-2$
									+ myHeadRec.getPrln().trim();
							String wwnnValue = config.getConfigValue(cnfgData);
							if (wwnnValue.equals(MFSConfig.NOT_FOUND)) {
								cnfgData = "WWNNRESCAN," + myHeadRec.getNmbr() + ",*ALL"; //$NON-NLS-1$ //$NON-NLS-2$
								wwnnValue = config.getConfigValue(cnfgData);
								if (wwnnValue.equals(MFSConfig.NOT_FOUND)) {
									cnfgData = "WWNNRESCAN,*ALL," + myHeadRec.getPrln().trim(); //$NON-NLS-1$
									wwnnValue = config.getConfigValue(cnfgData);
									if (wwnnValue.equals(MFSConfig.NOT_FOUND)) {
										cnfgData = "WWNNRESCAN,*ALL,*ALL"; //$NON-NLS-1$
										wwnnValue = config.getConfigValue(cnfgData);
									}
								}
							}
							if (!wwnnValue.equals(MFSConfig.NOT_FOUND)) {
								int rret = dirWorkPanel.prcsWWNN();

								if (rret == -44) {
									// user hit the cancel key, so suspend the
									// work unit
									dirWorkPanel.suspendNoQC("S", "0000"); //$NON-NLS-1$ //$NON-NLS-2$
									wwnn_rescan_continue = false;
								}
							}
							/* @SW2* end */
						} // end if(iq_continue && software_continue &&
							// csu_continue)

						/* @SW2C */
						if (iq_continue && software_continue && csu_continue && wwnn_rescan_continue) {
							if (myHeadRec.getPfsn().equals("Y")) //$NON-NLS-1$
							{
								/*
								 * regardless of the value in the WUSAPN field,
								 * we need to prompt for this in case they have
								 * suspended this work unit - I need to get the
								 * MCM Assembly Part Number from the 11S barcode
								 * they wand in
								 */

								MFSSerialNumDialog serialNumD = new MFSSerialNumDialog(dirWorkPanel,
										myHeadRec.getMctl());

								if (showWUSerialNumberPrompt(
										this.fieldCurrMctl)) { /*
																 * ~49A Validate
																 * if the
																 * MFSSerialNumDialog
																 * must be
																 * shown.
																 */
									/*
									 * ~50C Use this.fieldCurrMctl instead
									 * myHeadRec.getMctl()
									 */

									serialNumD.setLocationRelativeTo(getParentFrame()); // ~17A
									serialNumD.setVisible(true);

									myHeadRec.setSapn(serialNumD.getSerialNum());

									if (serialNumD.getPressedSuspend()) {
										pressedSuspend = true;
										// user hit the cancel key, so suspend
										// the work unit
										dirWorkPanel.suspendNoQC("S", "0000"); //$NON-NLS-1$ //$NON-NLS-2$
									} else {
										/*
										 * add MCM Part Number to configuration
										 * data
										 */
										config.setConfigValue("MCMPARTNUM", serialNumD.getPartNum()); //$NON-NLS-1$
									}
								} /* ~49A */
							} // end of 'Y' in PFSN field

							if (!pressedSuspend) // 41a if suspend pressed in
													// serial dialog don't run
													// following code
							{
								// ~6C. Parameter config is never used
								displayWorkUnitLocations(myHeadRec);

								if (myHeadRec.getFopr().equals("Y")) //$NON-NLS-1$
								{
									dirWorkPanel.swSPD11STextLabel(myHeadRec);
								}

								if (!this.cnfgEfficiencyDumbClientOn) {
									// call VRFY_SYS_C here to make sure all
									// parts are kosher.
									dirWorkPanel.removeMyListeners();

									MfsXMLDocument xml_data2 = new MfsXMLDocument("VRFY_SYS_C"); //$NON-NLS-1$
									xml_data2.addOpenTag("DATA"); //$NON-NLS-1$
									xml_data2.addCompleteField("MCTL", this.fieldCurrMctl); //$NON-NLS-1$
									xml_data2.addCompleteField("WHOM", "C"); //$NON-NLS-1$ //$NON-NLS-2$
									xml_data2.addCloseTag("DATA"); //$NON-NLS-1$
									xml_data2.finalizeXML();

									/*
									 * start the VRFY_SYS_C transaction thread
									 */
									MFSTransaction vrfy_sys_c = new MFSXmlTransaction(xml_data2.toString());
									vrfy_sys_c.setActionMessage("Verifying System Parts, Please Wait..."); //$NON-NLS-1$
									MFSComm.getInstance().execute(vrfy_sys_c, this);
									rc = vrfy_sys_c.getReturnCode();

									if (rc == 100 || rc == 200) {
										dirWorkPanel.showPNSNErrorList(vrfy_sys_c.getOutput(), 0);
										// set rc to 0, if 100, only warning
										if (rc == 100) {
											rc = 0;
										}
									}

									if (rc != 0) {
										IGSMessageBox.showOkMB(getParentFrame(), null, vrfy_sys_c.getErms(), null);
									}

									dirWorkPanel.addMyListeners();
								}

								String cnfgLoc = "AUTOLOC," + myHeadRec.getNmbr() + "," //$NON-NLS-1$ //$NON-NLS-2$
										+ myHeadRec.getPrln().trim();
								String valueLoc = config.getConfigValue(cnfgLoc);
								if (valueLoc.equals(MFSConfig.NOT_FOUND)) {
									cnfgLoc = "AUTOLOC," + myHeadRec.getNmbr() + ",*ALL"; //$NON-NLS-1$ //$NON-NLS-2$
									valueLoc = config.getConfigValue(cnfgLoc);
								}
								if (!valueLoc.equals(MFSConfig.NOT_FOUND)
										&& dirWorkPanel.getHeaderRec().getWtyp().equals("T") //$NON-NLS-1$
										&& myHeadRec.getMalc().trim().equals("")) //$NON-NLS-1$
								{
									dirWorkPanel.clickEditLocButton();
								}
							} // end not pressedSuspend 41a
						} // end of iq_continue, software_continue and
							// csu_continue and wwnn_rescan_continue
					} // end of good return from RTV_INSTR
				} // end of good returns from retreiving shorted instructions

				// 41a if you suspended in the serial dialog do not run
				// MASSKITINSTALL
				if ((rc == 0) && (!pressedSuspend)) {
					/*
					 * **************************** ~38
					 * **************************
					 */
					/*
					 * Check the MASSKITINSTALL config entry to allow the new
					 * logic
					 */

					String cnfgentrydata = "MASSKITINSTALL"; //$NON-NLS-1$
					String entryvalue = config.getConfigValue(cnfgentrydata);

					if (!entryvalue.equals(MFSConfig.NOT_FOUND)) {
						if (dirWorkPanel.checkPartsforMassKit()) {
							dirWorkPanel.processVrfyKit();
						}
					}
					if (this.cnfgEfficiencyOn) {
						dirWorkPanel.startAutoSearchLog();
					}
				}
			} // end good return code from str_wu
			else {
				IGSMessageBox.showOkMB(getParentFrame(), null, output, null);
			}
		} catch (Exception e) {
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
		return rc;
	}

	/**
	 * Calls the {@link MFSPrintingMethods#advFeatModel} method if configured.
	 * 
	 * @param headRec
	 *            the <code>MFSHeaderRec</code> for the work unit
	 * @param nmbrPrln
	 *            <code>headRec.getNmbr() + "," + headRec.getPrln().trim()</code>
	 * @param nmbrAll
	 *            <code>headRec.getNmbr() + ",*ALL"</code>
	 */
	private void swAdvFeatModelLabel(MFSHeaderRec headRec, String nmbrPrln, String nmbrAll) {
		final MFSConfig config = MFSConfig.getInstance();
		String cnfgData1 = "ADVFEATMODEL," + nmbrPrln; //$NON-NLS-1$
		String value = config.getConfigValue(cnfgData1);
		if (value.equals(MFSConfig.NOT_FOUND)) {
			String cnfgData2 = "ADVFEATMODEL," + nmbrAll; //$NON-NLS-1$
			value = config.getConfigValue(cnfgData2);
		}
		if (!value.equals(MFSConfig.NOT_FOUND)) {
			int qty = 1;
			if (!value.equals("")) //$NON-NLS-1$
			{
				qty = Integer.parseInt(value);
			}

			/* print advanced feature model Sheet */
			MFSPrintingMethods.advFeatModel(headRec.getMfgn(), headRec.getIdss(), headRec.getOrno(), headRec.getMctl(),
					qty, getParentFrame());
		}
	}

	// ~13A
	/**
	 * Calls the {@link MFSPrintingMethods#assemblyLbl} method if configured.
	 * 
	 * @param headRec
	 *            the <code>MFSHeaderRec</code> for the work unit
	 * @param nmbrPrln
	 *            <code>headRec.getNmbr() + "," + headRec.getPrln().trim()</code>
	 * @param nmbrAll
	 *            <code>headRec.getNmbr() + ",*ALL"</code>
	 */
	private void swAssemblyLabel(MFSHeaderRec headRec, String nmbrPrln, String nmbrAll) {
		final MFSConfig config = MFSConfig.getInstance();
		String cnfgData1 = "ASSEMBLYLBL," + nmbrPrln; //$NON-NLS-1$
		String value = config.getConfigValue(cnfgData1);
		if (value.equals(MFSConfig.NOT_FOUND)) {
			String cnfgData2 = "ASSEMBLYLBL," + nmbrAll; //$NON-NLS-1$
			value = config.getConfigValue(cnfgData2);
		}
		if (!value.equals(MFSConfig.NOT_FOUND)) {
			int qty = 1;
			if (!value.equals("")) //$NON-NLS-1$
			{
				qty = Integer.parseInt(value);
			}

			/* print configured node label */
			MFSPrintingMethods.assemblyLbl(headRec.getMctl(), qty, getParentFrame());
		}
	}

	// ~18A New Method
	/**
	 * Calls the ASGN_IOSN server transaction if configured.
	 * 
	 * @param headRec
	 *            the <code>MFSHeaderRec</code> for the work unit
	 * @param nmbrPrln
	 *            <code>headRec.getNmbr() + "," + headRec.getPrln().trim()</code>
	 * @param nmbrAll
	 *            <code>headRec.getNmbr() + ",*ALL"</code>
	 */
	private void swAssignIOSN(MFSHeaderRec headRec, String nmbrPrln, String nmbrAll) {
		final MFSConfig config = MFSConfig.getInstance();
		String cnfgData1 = "ASGNIOSN," + nmbrPrln; //$NON-NLS-1$
		String value = config.getConfigValue(cnfgData1);
		if (value.equals(MFSConfig.NOT_FOUND)) {
			String cnfgData2 = "ASGNIOSN," + nmbrAll; //$NON-NLS-1$
			value = config.getConfigValue(cnfgData2);
		}
		if (!value.equals(MFSConfig.NOT_FOUND)) {
			try {
				MfsXMLDocument xml_data1 = new MfsXMLDocument("ASGN_IOSN"); //$NON-NLS-1$
				xml_data1.addOpenTag("DATA"); //$NON-NLS-1$
				xml_data1.addCompleteField("USER", config.get8CharUser()); //$NON-NLS-1$
				xml_data1.addCompleteField("PDW", ""); //$NON-NLS-1$ //$NON-NLS-2$
				xml_data1.addCompleteField("MFGN", headRec.getMfgn()); //$NON-NLS-1$
				xml_data1.addCompleteField("IDSS", headRec.getIdss()); //$NON-NLS-1$
				xml_data1.addCompleteField("MCTL", headRec.getMctl()); //$NON-NLS-1$
				xml_data1.addCloseTag("DATA"); //$NON-NLS-1$
				xml_data1.finalizeXML();

				MFSTransaction asgn_iosn = new MFSXmlTransaction(xml_data1.toString());
				asgn_iosn.setActionMessage("Executing Assign I/O Serial Numbers, Please Wait..."); //$NON-NLS-1$
				MFSComm.getInstance().execute(asgn_iosn, this);

				if (asgn_iosn.getReturnCode() != 0) {
					IGSMessageBox.showOkMB(getParentFrame(), null, asgn_iosn.getErms(), null);
				}
			} catch (Exception e) {
				IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
			}
		}
	}

	/**
	 * Calls the {@link MFSPrintingMethods#confnode} or the
	 * {@link MFSPrintingMethods#confnoder} method if configured.
	 * 
	 * @param headRec
	 *            the <code>MFSHeaderRec</code> for the work unit
	 * @param nmbrPrln
	 *            <code>headRec.getNmbr() + "," + headRec.getPrln().trim()</code>
	 * @param nmbrAll
	 *            <code>headRec.getNmbr() + ",*ALL"</code>
	 */
	private void swConfNodeLabels(MFSHeaderRec headRec, String nmbrPrln, String nmbrAll) {
		final MFSConfig config = MFSConfig.getInstance();
		String cnfgData1 = "CONFNODELBL," + nmbrPrln; //$NON-NLS-1$
		String value1 = config.getConfigValue(cnfgData1);
		if (value1.equals(MFSConfig.NOT_FOUND)) {
			String cnfgData2 = "CONFNODELBL," + nmbrAll; //$NON-NLS-1$
			value1 = config.getConfigValue(cnfgData2);
		}
		if (!value1.equals(MFSConfig.NOT_FOUND)) {
			int qty = 1;
			if (!value1.equals("")) //$NON-NLS-1$
			{
				qty = Integer.parseInt(value1);
			}
			/* print configured node label */
			MFSPrintingMethods.confnode(headRec.getMctl(), qty, getParentFrame());
		}

		String cnfgDataR1 = "CONFNODERLBL," + nmbrPrln; //$NON-NLS-1$
		String valueR1 = config.getConfigValue(cnfgDataR1);
		if (valueR1.equals(MFSConfig.NOT_FOUND)) {
			String cnfgDataR2 = "CONFNODERLBL," + nmbrAll; //$NON-NLS-1$
			valueR1 = config.getConfigValue(cnfgDataR2);
		}
		if (!valueR1.equals(MFSConfig.NOT_FOUND)) {
			int qty = 1;
			if (!valueR1.equals("")) //$NON-NLS-1$
			{
				qty = Integer.parseInt(valueR1);
			}

			/* print configured node label */
			MFSPrintingMethods.confnoder(headRec.getMctl(), qty, getParentFrame());
		}
	}

	/**
	 * Calls the {@link MFSPrintingMethods#confseri} method if configured.
	 * 
	 * @param headRec
	 *            the <code>MFSHeaderRec</code> for the work unit
	 * @param nmbrPrln
	 *            <code>headRec.getNmbr() + "," + headRec.getPrln().trim()</code>
	 * @param nmbrAll
	 *            <code>headRec.getNmbr() + ",*ALL"</code>
	 */
	private void swConfSeriLabel(MFSHeaderRec headRec, String nmbrPrln, String nmbrAll) {
		final MFSConfig config = MFSConfig.getInstance();
		String cnfgData1 = "CONFSERILBL," + nmbrPrln; //$NON-NLS-1$
		String value = config.getConfigValue(cnfgData1);
		if (value.equals(MFSConfig.NOT_FOUND)) {
			String cnfgData2 = "CONFSERILBL," + nmbrAll; //$NON-NLS-1$
			value = config.getConfigValue(cnfgData2);
		}
		if (!value.equals(MFSConfig.NOT_FOUND)) {
			int qty = 1;
			if (!value.equals("")) //$NON-NLS-1$
			{
				qty = Integer.parseInt(value);
			}

			/* print configured serialized node label */
			MFSPrintingMethods.confseri(headRec.getPrln(), "J", headRec.getMctl(), qty, getParentFrame()); //$NON-NLS-1$
		}
	}

	// ~29A CarrierComn label
	/**
	 * Calls the {@link MFSPrintingMethods#carriercomn} method if configured.
	 * 
	 * @param headRec
	 *            the <code>MFSHeaderRec</code> for the work unit
	 * @param nmbrPrln
	 *            <code>headRec.getNmbr() + "," + headRec.getPrln().trim()</code>
	 * @param nmbrAll
	 *            <code>headRec.getNmbr() + ",*ALL"</code>
	 */
	private void swCarrierComnLabel(MFSHeaderRec headRec, String nmbrPrln, String nmbrAll) {
		final MFSConfig config = MFSConfig.getInstance();
		String cnfgData = "CARRIERCOMNSTART," + nmbrPrln; //$NON-NLS-1$ // ~32C
		String value = config.getConfigValue(cnfgData);
		if (value.equals(MFSConfig.NOT_FOUND)) {
			cnfgData = "CARRIERCOMNSTART," + nmbrAll; //$NON-NLS-1$ // ~32C
			value = config.getConfigValue(cnfgData);
		}
		if (!value.equals(MFSConfig.NOT_FOUND)) {
			int qty = 1;
			if (!value.equals("")) //$NON-NLS-1$
			{
				qty = Integer.parseInt(value);
			}

			/* print CarrierComn Label */
			MFSPrintingMethods.carriercomn(headRec.getMctl(), "STARTWU", qty, getParentFrame()); //$NON-NLS-1$
		}
	}

	/**
	 * Calls the RTV_CNTR transaction and the appropriate printing methods if
	 * configured.
	 * 
	 * @param headRec
	 *            the <code>MFSHeaderRec</code> for the work unit
	 * @param nmbrPrln
	 *            <code>headRec.getNmbr() + "," + headRec.getPrln().trim()</code>
	 * @param nmbrAll
	 *            <code>headRec.getNmbr() + ",*ALL"</code>
	 */
	private void swContainerLabel(MFSHeaderRec headRec, String nmbrPrln, String nmbrAll, String source) {
		final MFSConfig config = MFSConfig.getInstance();
		if (headRec.getCntr().equals("          ")) //$NON-NLS-1$
		{
			String cnfgData1 = "CONTAINER," + nmbrPrln; //$NON-NLS-1$
			String value = config.getConfigValue(cnfgData1);
			if (value.equals(MFSConfig.NOT_FOUND)) {
				String cnfgData2 = "CONTAINER," + nmbrAll; //$NON-NLS-1$
				value = config.getConfigValue(cnfgData2);
			}
			if (!value.equals(MFSConfig.NOT_FOUND)) {
				int rc = 0;
				int qty = 1;
				// now we need to check to see if there is a country label
				// quantity override*/
				String overrideCfgQty = "CONTAINER," + headRec.getCtry().trim(); //$NON-NLS-1$
				String overrideqty = config.getConfigValue(overrideCfgQty);
				if (overrideqty.equals(MFSConfig.NOT_FOUND)) {
					if (!value.equals("")) //$NON-NLS-1$
					{
						qty = Integer.parseInt(value);
					}
				} else {
					if (!overrideqty.equals("")) //$NON-NLS-1$
					{
						qty = Integer.parseInt(overrideqty);
					}
				}

				/*
				 * need to use 10 blanks for cntr num to get user in right place
				 */
				String data = "RTV_CNTR  " + headRec.getMctl() + "N" + //$NON-NLS-1$ //$NON-NLS-2$
						"          " + MFSConfig.getInstance().get8CharUser(); /* ~26A */ //$NON-NLS-1$
				MFSTransaction rtv_cntr = new MFSFixedTransaction(data);
				rtv_cntr.setActionMessage("Retrieving Container Info, Please Wait..."); //$NON-NLS-1$
				MFSComm.getInstance().execute(rtv_cntr, this);
				rc = rtv_cntr.getReturnCode();

				String errorString = ""; //$NON-NLS-1$
				if (rc == 0) {
					/* print container label */
					if (source.equals(DIRECT_WORK_SOURCE)) /* ~34 */
						getParentFrame().getDirectWorkPanel().updt_cntr(rtv_cntr.getOutput());
					else if (source.equals(REDUCTION_SOURCE)) /* ~34 */
						headRec.setCntr(rtv_cntr.getOutput());

					MFSDetermineLabel label = new MFSDetermineLabel(); /* ~39A */
					label.determineContainerLabel(headRec, headRec.getMctl(), /* ~39A */
							rtv_cntr.getOutput(), qty, getParentFrame(), false, this);
				} else {
					errorString = rtv_cntr.getErms();
				}

				if (rc != 0) {
					IGSMessageBox.showOkMB(getParentFrame(), null, errorString, null);
				}
			}
		} // end of blank container
	}

	/**
	 * Calls the {@link MFSPrintingMethods#podkey} method if configured.
	 * 
	 * @param headRec
	 *            the <code>MFSHeaderRec</code> for the work unit
	 * @param nmbrPrln
	 *            <code>headRec.getNmbr() + "," + headRec.getPrln().trim()</code>
	 * @param nmbrAll
	 *            <code>headRec.getNmbr() + ",*ALL"</code>
	 */
	private void swCuodLabels(MFSHeaderRec headRec, String nmbrPrln, String nmbrAll) {
		final MFSConfig config = MFSConfig.getInstance();
		String cnfgData1 = "RTVCUODKY," + nmbrPrln; //$NON-NLS-1$
		String value = config.getConfigValue(cnfgData1);
		if (value.equals(MFSConfig.NOT_FOUND)) {
			String cnfgData2 = "RTVCUODKY," + nmbrAll; //$NON-NLS-1$
			value = config.getConfigValue(cnfgData2);
			if (value.equals(MFSConfig.NOT_FOUND)) {
				String cnfgData3 = "RTVPODKEY," + nmbrPrln; //$NON-NLS-1$
				value = config.getConfigValue(cnfgData3);
				if (value.equals(MFSConfig.NOT_FOUND)) {
					String cnfgData4 = "RTVPODKEY," + nmbrAll; //$NON-NLS-1$
					value = config.getConfigValue(cnfgData4);
				}
			}
		}
		if (!value.equals(MFSConfig.NOT_FOUND)) {
			int qty = 1;
			if (!value.equals("")) //$NON-NLS-1$
			{
				qty = Integer.parseInt(value);
			}

			/* print pod Sheet */
			MFSPrintingMethods.podkey(headRec.getMfgn(), headRec.getIdss(), qty, getParentFrame());
		}
	}

	/**
	 * New method ~22 Calls the {@link MFSPrintingMethods#ctrlnumbr} method if
	 * configured.
	 * 
	 * @param headRec
	 *            the <code>MFSHeaderRec</code> for the work unit
	 * @param nmbrPrln
	 *            <code>headRec.getNmbr() + "," + headRec.getPrln().trim()</code>
	 * @param nmbrAll
	 *            <code>headRec.getNmbr() + ",*ALL"</code>
	 */
	private void swCTRLNUMBRLabel(MFSHeaderRec headRec, String nmbrPrln, String nmbrAll) {
		final MFSConfig config = MFSConfig.getInstance();
		String cnfgData1 = "CTRLNUMBR," + nmbrPrln; //$NON-NLS-1$
		String value = config.getConfigValue(cnfgData1);
		if (value.equals(MFSConfig.NOT_FOUND)) {
			String cnfgData2 = "CTRLNUMBR," + nmbrAll; //$NON-NLS-1$
			value = config.getConfigValue(cnfgData2);
		}
		if (!value.equals(MFSConfig.NOT_FOUND)) {
			int qty = 1;
			if (!value.equals("")) //$NON-NLS-1$
			{
				qty = Integer.parseInt(value);
			}

			/* Print the Control number label */
			MFSPrintingMethods.ctrlnumbr(headRec.getMctl(), qty, getParentFrame());
		}
	}

	/**
	 * Calls the {@link MFSPrintingMethods#deconfigLoc} method if configured.
	 * 
	 * @param headRec
	 *            the <code>MFSHeaderRec</code> for the work unit
	 * @param nmbrPrln
	 *            <code>headRec.getNmbr() + "," + headRec.getPrln().trim()</code>
	 * @param nmbrAll
	 *            <code>headRec.getNmbr() + ",*ALL"</code>
	 */
	private void swDeconfigLocLabel(MFSHeaderRec headRec, String nmbrPrln, String nmbrAll) {
		final MFSConfig config = MFSConfig.getInstance();
		String cnfgData1 = "DECONFIGLOC," + nmbrPrln; //$NON-NLS-1$
		String value = config.getConfigValue(cnfgData1);
		if (value.equals(MFSConfig.NOT_FOUND)) {
			String cnfgData2 = "DECONFIGLOC," + nmbrAll; //$NON-NLS-1$
			value = config.getConfigValue(cnfgData2);
		}
		if (!value.equals(MFSConfig.NOT_FOUND)) {
			int qty = 1;
			if (!value.equals("")) //$NON-NLS-1$
			{
				qty = Integer.parseInt(value);
			}

			/* print deconfig Location Sheet */
			MFSPrintingMethods.deconfigLoc(headRec.getMfgn(), headRec.getIdss(), headRec.getOrno(), headRec.getMctl(),
					qty, getParentFrame());
		}
	}

	/**
	 * Calls the {@link MFSPrintingMethods#smallwu} or the
	 * {@link MFSPrintingMethods#elevenswu} method if configured.
	 * 
	 * @param headRec
	 *            the <code>MFSHeaderRec</code> for the work unit
	 * @param nmbrPrln
	 *            <code>headRec.getNmbr() + "," + headRec.getPrln().trim()</code>
	 * @param nmbrAll
	 *            <code>headRec.getNmbr() + ",*ALL"</code>
	 */
	private void swElevenSWULabel(MFSHeaderRec headRec, String nmbrPrln, String nmbrAll) {
		final MFSConfig config = MFSConfig.getInstance();
		String cnfgData1 = "11SWU," + nmbrPrln; //$NON-NLS-1$
		String value = config.getConfigValue(cnfgData1);
		if (value.equals(MFSConfig.NOT_FOUND)) {
			String cnfgData2 = "11SWU," + nmbrAll; //$NON-NLS-1$
			value = config.getConfigValue(cnfgData2);
		}
		if (!value.equals(MFSConfig.NOT_FOUND)) {
			int qty = 1;
			if (!value.equals("")) //$NON-NLS-1$
			{
				qty = Integer.parseInt(value);
			}

			/* print 11S work Unit label */
			if (headRec.getAwsa().equals("Y") || headRec.getAwsa().equals("y")) //$NON-NLS-1$ //$NON-NLS-2$
			{
				MFSPrintingMethods.smallwu(headRec.getMctl(), qty, getParentFrame());
			} else {
				MFSPrintingMethods.elevenswu(headRec.getPrln(), "J", headRec.getMctl(), qty, getParentFrame()); //$NON-NLS-1$
			}
		}
	}

	/**
	 * Calls the {@link MFSPrintingMethods#rtvffbm} method if configured.
	 * 
	 * @param headRec
	 *            the <code>MFSHeaderRec</code> for the work unit
	 * @param nmbrPrln
	 *            <code>headRec.getNmbr() + "," + headRec.getPrln().trim()</code>
	 * @param nmbrAll
	 *            <code>headRec.getNmbr() + ",*ALL"</code>
	 */
	private void swFfbmLabel(MFSHeaderRec headRec, String nmbrPrln, String nmbrAll) {
		final MFSConfig config = MFSConfig.getInstance();
		String cnfgData1 = "RTVFFBMLBL," + nmbrPrln; //$NON-NLS-1$
		String value = config.getConfigValue(cnfgData1);
		if (value.equals(MFSConfig.NOT_FOUND)) {
			String cnfgData2 = "RTVFFBMLBL," + nmbrAll; //$NON-NLS-1$
			value = config.getConfigValue(cnfgData2);
		}
		if (!value.equals(MFSConfig.NOT_FOUND)) {
			int qty = 1;
			if (!value.equals("")) //$NON-NLS-1$
			{
				qty = Integer.parseInt(value);
			}
			/* print FFBM/TrackSub label */
			MFSPrintingMethods.rtvffbm(headRec.getMctl(), qty, getParentFrame());
		}
	}

	/**
	 * Calls the {@link MFSPrintingMethods#keycodes} method if configured.
	 * 
	 * @param headRec
	 *            the <code>MFSHeaderRec</code> for the work unit
	 * @param nmbrPrln
	 *            <code>headRec.getNmbr() + "," + headRec.getPrln().trim()</code>
	 * @param nmbrAll
	 *            <code>headRec.getNmbr() + ",*ALL"</code>
	 */
	private void swKeyCodesLabel(MFSHeaderRec headRec, String nmbrPrln, String nmbrAll) {
		final MFSConfig config = MFSConfig.getInstance();
		String cnfgData1 = "KEYCODES," + nmbrPrln; //$NON-NLS-1$
		String cnfgData2 = "KEYCODESLONG," + nmbrPrln; // ~27A //$NON-NLS-1$
		String value = config.getConfigValue(cnfgData1);
		String value2 = config.getConfigValue(cnfgData2);
		String ktpe = "N"; // key type // ~27A //$NON-NLS-1$
		String sqty = "1"; // KEYCODES quantity // ~27A //$NON-NLS-1$
		String lqty = "1"; // KEYCODELONG quantity // ~27A //$NON-NLS-1$

		if (value.equals(MFSConfig.NOT_FOUND)) {
			cnfgData1 = "KEYCODES," + nmbrAll; //$NON-NLS-1$ // ~27C
			value = config.getConfigValue(cnfgData1); // ~27C
		}

		if (value2.equals(MFSConfig.NOT_FOUND)) // ~27A
		{
			cnfgData2 = "KEYCODESLONG," + nmbrAll; //$NON-NLS-1$
			value2 = config.getConfigValue(cnfgData2);
		}

		if (!value.equals(MFSConfig.NOT_FOUND)) {
			ktpe = "S"; // ~27A //$NON-NLS-1$
			if (!value.equals("")) //$NON-NLS-1$
			{
				sqty = value; // ~27A
			}
		}

		if (!value2.equals(MFSConfig.NOT_FOUND)) // ~27A
		{
			ktpe = (ktpe == "S") ? "B" : "L"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			if (!value2.equals("")) //$NON-NLS-1$
			{
				lqty = value2;
			}
		}

		if (!ktpe.equals("N")) // ~27A //$NON-NLS-1$
		{
			/* print configured node label */
			MFSPrintingMethods.keycodes(headRec.getMfgn(), headRec.getIdss(), headRec.getMatp(), headRec.getMmdl(),
					headRec.getMspi(), headRec.getMcsn(), headRec.getOrno(), headRec.getMctl(), sqty, lqty, ktpe,
					getParentFrame());
		}
	}

	/**
	 * Calls the appropriate kitting print method or the UPDT_PQ transaction if
	 * configured.
	 * 
	 * @param headRec
	 *            the <code>MFSHeaderRec</code> for the work unit
	 * @param nmbrPrln
	 *            <code>headRec.getNmbr() + "," + headRec.getPrln().trim()</code>
	 * @param nmbrAll
	 *            <code>headRec.getNmbr() + ",*ALL"</code>
	 */
	@SuppressWarnings("rawtypes")
	private void swKittingLabels(MFSHeaderRec headRec, String nmbrPrln, String nmbrAll) {
		final MFSConfig config = MFSConfig.getInstance();
		/* used for UPDT_PQ transaction, 155 astrix and 100 blanks */
		String astrix = "**********************************************************************************************************************************************************"; //$NON-NLS-1$
		String oprm = "                                                                                                    "; //$NON-NLS-1$

		String cnfgData1 = "KITTING," + nmbrPrln; //$NON-NLS-1$
		String value = config.getConfigValue(cnfgData1);
		if (value.equals(MFSConfig.NOT_FOUND)) {
			String cnfgData2 = "KITTING," + nmbrAll; //$NON-NLS-1$
			value = config.getConfigValue(cnfgData2);
		}
		if (!value.equals(MFSConfig.NOT_FOUND)) {
			// ~16C Use getConfigLabels method
			Enumeration e = config.getConfigLabels();
			while (e.hasMoreElements()) {
				String configLabel = (String) e.nextElement();
				if (configLabel != null) {
					String name = configLabel + "          "; //$NON-NLS-1$
					if (name.substring(0, 8).equals("KITTDFLT")) //$NON-NLS-1$
					{
						// ~16C Use getConfigValue
						String val = config.getConfigValue(configLabel) + "          "; //$NON-NLS-1$

						/*
						 * look in the kittdflt entry for a 2nd comma, if found
						 * check for the loc or remote
						 */
						String loc_or_remote = ""; //$NON-NLS-1$
						int first_comma = val.indexOf(","); //$NON-NLS-1$
						int loc_index = val.indexOf(",", first_comma + 1); //$NON-NLS-1$
						if (loc_index == -1) {
							// if no loc found, use L to print locally
							loc_or_remote = "L"; //$NON-NLS-1$
						} else {
							loc_or_remote = val.substring(loc_index + 1, loc_index + 2);
						}

						String nmbr = name.substring(9, 13);
						String type = val.substring(0, 1);
						int qty = 1;

						/*
						 * if loc_index is not -1, then we know that no loc or
						 * remote was specified, so just substring to the end
						 */
						if (loc_index == -1) {
							String Sqty = val.substring(first_comma + 1).trim();
							qty = Integer.parseInt(Sqty);
						}

						/*
						 * there is a loc or remote specified, substring between
						 * the comma's
						 */
						else {
							/*
							 * if there is something between the commas, convert
							 * it to an int
							 */
							if (!val.substring(first_comma + 1, loc_index).equals("    ")) //$NON-NLS-1$
							{
								String Sqty = val.substring(first_comma + 1, loc_index).trim();
								qty = Integer.parseInt(Sqty);
							}
						}

						/* print POK Kitting Report */
						/*
						 * decide which POK kitting report to print, POKKIT or
						 * POKKIT2
						 */
						if (config.containsConfigEntry("POKKIT")) //$NON-NLS-1$
						{
							MFSPrintingMethods.pokkit(headRec.getMctl(), nmbr, type, config.get8CharCellType(), qty,
									getParentFrame());
						}
						if (config.containsConfigEntry("POKKIT2")) //$NON-NLS-1$
						{
							MFSPrintingMethods.pokkit2(headRec.getMctl(), nmbr, type, config.get8CharCellType(), qty,
									getParentFrame());
						} else if (config.containsConfigEntry("RCHKIT")) //$NON-NLS-1$
						{
							if (loc_or_remote.equals("L") || loc_or_remote.equals("B")) //$NON-NLS-1$ //$NON-NLS-2$
							{
								MFSPrintingMethods.rchkit(headRec.getMctl(), nmbr, type, qty, getParentFrame());
							}
							if (loc_or_remote.equals("R") || loc_or_remote.equals("B")) //$NON-NLS-1$ //$NON-NLS-2$
							{
								int rc = 0;
								/* trigger kitting2 on host */
								StringBuffer data = new StringBuffer();
								data.append("UPDT_PQ   "); //$NON-NLS-1$
								data.append(astrix);
								data.append("       "); //$NON-NLS-1$
								data.append("    ");//$NON-NLS-1$
								data.append(headRec.getMctl());
								data.append("        ");//$NON-NLS-1$
								data.append(nmbr);
								data.append("00001"); //$NON-NLS-1$
								data.append("KITTING2 "); //$NON-NLS-1$
								data.append(config.get8CharCell());
								data.append(config.getConfigValue("USER").concat("           ").substring(0, 10)); //$NON-NLS-1$ //$NON-NLS-2$
								data.append(type);
								data.append(oprm);

								MFSTransaction updt_pq = new MFSFixedTransaction(data.toString());
								updt_pq.setActionMessage("Updating FCSPPQ10, Please Wait..."); //$NON-NLS-1$
								MFSComm.getInstance().execute(updt_pq, this);
								rc = updt_pq.getReturnCode();

								if (rc != 0) {
									IGSMessageBox.showOkMB(getParentFrame(), null, updt_pq.getErms(), null);
								}
							}
						}
					}
				} // end cv != null
			} // end while e.hasMoreElements
		} // end !value.equals(MFSConfig.NOT_FOUND)
	}

	/**
	 * Calls the {@link MFSPrintingMethods#l3cache} method if configured.
	 * 
	 * @param headRec
	 *            the <code>MFSHeaderRec</code> for the work unit
	 * @param nmbrPrln
	 *            <code>headRec.getNmbr() + "," + headRec.getPrln().trim()</code>
	 * @param nmbrAll
	 *            <code>headRec.getNmbr() + ",*ALL"</code>
	 */
	private void swL3cacheLabel(MFSHeaderRec headRec, String nmbrPrln, String nmbrAll) {
		final MFSConfig config = MFSConfig.getInstance();
		String cnfgData1 = "L3CACHE," + nmbrPrln; //$NON-NLS-1$
		String value = config.getConfigValue(cnfgData1);
		if (value.equals(MFSConfig.NOT_FOUND)) {
			String cnfgData2 = "L3CACHE," + nmbrAll; //$NON-NLS-1$
			value = config.getConfigValue(cnfgData2);
		}
		if (!value.equals(MFSConfig.NOT_FOUND)) {
			int qty = 1;
			if (!value.equals("")) //$NON-NLS-1$
			{
				qty = Integer.parseInt(value);
			}

			/* print l3cache label */
			MFSPrintingMethods.l3cache(headRec.getPrln(), "J", headRec.getMctl(), qty, getParentFrame()); //$NON-NLS-1$
		}
	}

	/**
	 * Calls the {@link MFSPrintingMethods#machIndexCard} method if configured.
	 * 
	 * @param headRec
	 *            the <code>MFSHeaderRec</code> for the work unit
	 * @param nmbrPrln
	 *            <code>headRec.getNmbr() + "," + headRec.getPrln().trim()</code>
	 * @param nmbrAll
	 *            <code>headRec.getNmbr() + ",*ALL"</code>
	 */
	private void swMachineIndexCardLabels(MFSHeaderRec headRec, String nmbrPrln, String nmbrAll) {
		final MFSConfig config = MFSConfig.getInstance();
		String cnfgDataL1 = "MACHINDEXCARD," + nmbrPrln; //$NON-NLS-1$
		String valueL = config.getConfigValue(cnfgDataL1);
		if (valueL.equals(MFSConfig.NOT_FOUND)) {
			String cnfgDataL2 = "MACHINDEXCARD," + nmbrAll; //$NON-NLS-1$
			valueL = config.getConfigValue(cnfgDataL2);
		}
		if (!valueL.equals(MFSConfig.NOT_FOUND)) {
			int qty = 1;
			if (!valueL.equals("")) //$NON-NLS-1$
			{
				qty = Integer.parseInt(valueL);
			}

			/* print prod feature code Sheet */
			MFSPrintingMethods.machIndexCard(headRec.getMfgn(), headRec.getIdss(), headRec.getOrno(), headRec.getMctl(),
					qty, getParentFrame());
		}
	}

	/**
	 * Calls the {@link MFSPrintingMethods#machtype} method if configured.
	 * 
	 * @param headRec
	 *            the <code>MFSHeaderRec</code> for the work unit
	 * @param nmbrPrln
	 *            <code>headRec.getNmbr() + "," + headRec.getPrln().trim()</code>
	 * @param nmbrAll
	 *            <code>headRec.getNmbr() + ",*ALL"</code>
	 */
	private void swMachTypeLabel(MFSHeaderRec headRec, String nmbrPrln, String nmbrAll) {
		final MFSConfig config = MFSConfig.getInstance();
		String cnfgData1 = "MACHTYPE," + nmbrPrln; //$NON-NLS-1$
		String value = config.getConfigValue(cnfgData1);
		if (value.equals(MFSConfig.NOT_FOUND)) {
			String cnfgData2 = "MACHTYPE," + nmbrAll; //$NON-NLS-1$
			value = config.getConfigValue(cnfgData2);
		}
		if (!value.equals(MFSConfig.NOT_FOUND)) {
			int qty = 1;
			if (!value.equals("")) //$NON-NLS-1$
			{
				qty = Integer.parseInt(value);
			}

			/* print machtype label */
			MFSPrintingMethods.machtype(headRec.getMctl(), qty, getParentFrame());
		}
	}

	/**
	 * Calls the {@link MFSPrintingMethods#macid} method if configured.
	 * 
	 * @param headRec
	 *            the <code>MFSHeaderRec</code> for the work unit
	 * @param nmbrPrln
	 *            <code>headRec.getNmbr() + "," + headRec.getPrln().trim()</code>
	 * @param nmbrAll
	 *            <code>headRec.getNmbr() + ",*ALL"</code>
	 */
	private void swMACIDLabel(MFSHeaderRec headRec, String nmbrPrln, String nmbrAll) {
		final MFSConfig config = MFSConfig.getInstance();
		String cnfgData1 = "MACID," + nmbrPrln; //$NON-NLS-1$
		String value = config.getConfigValue(cnfgData1);
		if (value.equals(MFSConfig.NOT_FOUND)) {
			String cnfgData2 = "MACID," + nmbrAll; //$NON-NLS-1$
			value = config.getConfigValue(cnfgData2);
		}
		if (!value.equals(MFSConfig.NOT_FOUND)) {
			int qty = 1;
			if (!value.equals("")) //$NON-NLS-1$
			{
				qty = Integer.parseInt(value);
			}

			MFSPrintingMethods.macid(headRec.getMfgn(), headRec.getIdss(), headRec.getOrno(), headRec.getMctl(), qty,
					getParentFrame());
		}
	}

	/**
	 * Calls the {@link MFSPrintingMethods#mainid} method if configured.
	 * 
	 * @param headRec
	 *            the <code>MFSHeaderRec</code> for the work unit
	 * @param nmbrPrln
	 *            <code>headRec.getNmbr() + "," + headRec.getPrln().trim()</code>
	 * @param nmbrAll
	 *            <code>headRec.getNmbr() + ",*ALL"</code>
	 */
	private void swMainID(MFSHeaderRec headRec, String nmbrPrln, String nmbrAll) {
		final MFSConfig config = MFSConfig.getInstance();
		String cnfgData1 = "MAINID," + nmbrPrln; //$NON-NLS-1$
		String value = config.getConfigValue(cnfgData1);
		if (value.equals(MFSConfig.NOT_FOUND)) {
			String cnfgData2 = "MAINID," + nmbrAll; //$NON-NLS-1$
			value = config.getConfigValue(cnfgData2);
		}
		if (!value.equals(MFSConfig.NOT_FOUND)) {
			int qty = 1;
			if (!value.equals("")) //$NON-NLS-1$
			{
				qty = Integer.parseInt(value);
			}

			/* print main id label */
			MFSPrintingMethods.mainid(headRec.getMctl(), qty, getParentFrame());
		}
	}

	/**
	 * Calls the RTV_IMSN transaction and the
	 * {@link MFSPrintingMethods#matpmcsn1s} method if configured.
	 * 
	 * @param headRec
	 *            the <code>MFSHeaderRec</code> for the work unit
	 * @param nmbrPrln
	 *            <code>headRec.getNmbr() + "," + headRec.getPrln().trim()</code>
	 * @param nmbrAll
	 *            <code>headRec.getNmbr() + ",*ALL"</code>
	 */
	private void swMATPMCSN1SLabel(MFSHeaderRec headRec, String nmbrPrln, String nmbrAll) {
		final MFSConfig config = MFSConfig.getInstance();
		String cnfgData1 = "MATPMCSN1S," + nmbrPrln; //$NON-NLS-1$
		String value = config.getConfigValue(cnfgData1);
		if (value.equals(MFSConfig.NOT_FOUND)) {
			String cnfgData2 = "MATPMCSN1S," + nmbrAll; //$NON-NLS-1$
			value = config.getConfigValue(cnfgData2);
		}

		// If the label is configured to print at the operation.
		if (!value.equals(MFSConfig.NOT_FOUND)) {
			// Get the number of copies to print. Default to 1.
			int qty = 1;
			if (!value.equals("")) //$NON-NLS-1$
			{
				qty = Integer.parseInt(value);
			}

			MFSComponentRec next;
			int index = 0;
			MFSDirectWorkPanel dirWorkPanel = getParentFrame().getDirectWorkPanel();
			/* Loop thru components and check for AMSI flag = blank or zero */
			while (index < dirWorkPanel.getComponentListModelSize()) {
				next = dirWorkPanel.getComponentListModelCompRecAt(index);
				if (!next.getAmsi().equals(" ") & !next.getAmsi().equals("0")) //$NON-NLS-1$ //$NON-NLS-2$
				{
					/*
					 * print configured node label with given matp,model,mspi &
					 * snum
					 */
					MFSPrintingMethods.matpmcsn1s(next.getMatp(), next.getMmdl(), next.getMspi(), next.getMcsn(), qty,
							getParentFrame());
				}
				index++;
			}

			// Now print labels for additional serialized parts if there are any
			// returned from the RTV_IMSN transaction
			try {
				int rc = 0;
				String errorString = ""; //$NON-NLS-1$

				// Setup the xml string to call the transaction with necessary
				// parameters.
				MfsXMLDocument xml_data1 = new MfsXMLDocument("RTV_IMSN"); //$NON-NLS-1$
				xml_data1.addOpenTag("DATA"); //$NON-NLS-1$
				xml_data1.addCompleteField("MCTL", headRec.getMctl()); //$NON-NLS-1$
				xml_data1.addCompleteField("INPN", "            "); //$NON-NLS-1$ //$NON-NLS-2$
				xml_data1.addCompleteField("INSQ", "            "); //$NON-NLS-1$ //$NON-NLS-2$
				xml_data1.addCloseTag("DATA"); //$NON-NLS-1$
				xml_data1.finalizeXML();

				MFSTransaction rtv_imsn = new MFSXmlTransaction(xml_data1.toString());
				MFSComm.getInstance().execute(rtv_imsn, this);
				rc = rtv_imsn.getReturnCode();

				// Parse the data returned
				MfsXMLParser xmlParser = new MfsXMLParser(rtv_imsn.getOutput());

				try {
					// Get the error message if a bad rc is returned from the
					// transaction.
					if (rc != 0) {
						errorString = xmlParser.getField("ERMS"); //$NON-NLS-1$
					}
					// Else it's a good rc
					else {

						// Parse out the data returned for each serialized part
						MfsXMLParser serialParser = new MfsXMLParser(xmlParser.getNextField("REC")); //$NON-NLS-1$

						while (!serialParser.getUnparsedXML().equals("")) //$NON-NLS-1$
						{
							String matp = serialParser.getField("MATP"); //$NON-NLS-1$
							String mmdl = serialParser.getField("MMDL"); //$NON-NLS-1$
							String mspi = serialParser.getField("MSPI"); //$NON-NLS-1$
							String mcsn = serialParser.getField("MCSN"); //$NON-NLS-1$

							/*
							 * print configured node label with given
							 * matp,model,mspi & snum
							 */
							MFSPrintingMethods.matpmcsn1s(matp, mmdl, mspi, mcsn, qty, getParentFrame());

							// Get the next record
							serialParser = new mfsxml.MfsXMLParser(xmlParser.getNextField("REC")); //$NON-NLS-1$
						}
					} /* end of good rc */
				} catch (mfsxml.MISSING_XML_TAG_EXCEPTION e) {
					rc = 10;
					errorString = "Missing XML Tag Error in MATPMCSN1S Label Print"; //$NON-NLS-1$
				}

				// If not a good rc, then display the error message
				if (rc != 0) {
					IGSMessageBox.showOkMB(getParentFrame(), null, errorString, null);
				}
			} catch (Exception e) {
				IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
			}
		}
	}

	/**
	 * Calls the {@link MFSPrintingMethods#matpmcsn} method if configured.
	 * 
	 * @param headRec
	 *            the <code>MFSHeaderRec</code> for the work unit
	 * @param nmbrPrln
	 *            <code>headRec.getNmbr() + "," + headRec.getPrln().trim()</code>
	 * @param nmbrAll
	 *            <code>headRec.getNmbr() + ",*ALL"</code>
	 */
	private void swMATPMCSNLabel(MFSHeaderRec headRec, String nmbrPrln, String nmbrAll) {
		final MFSConfig config = MFSConfig.getInstance();
		String cnfgData1 = "MATPMCSN," + nmbrPrln; //$NON-NLS-1$
		String value = config.getConfigValue(cnfgData1);
		if (value.equals(MFSConfig.NOT_FOUND)) {
			String cnfgData2 = "MATPMCSN," + nmbrAll; //$NON-NLS-1$
			value = config.getConfigValue(cnfgData2);
		}
		if (!value.equals(MFSConfig.NOT_FOUND)) {
			int qty = 1;
			if (!value.equals("")) //$NON-NLS-1$
			{
				qty = Integer.parseInt(value);
			}

			MFSComponentRec next;
			int index = 0;
			MFSDirectWorkPanel dirWorkPanel = getParentFrame().getDirectWorkPanel();
			/* Loop thru components and check for AMSI flag = blank or zero */
			while (index < dirWorkPanel.getComponentListModelSize()) {
				next = dirWorkPanel.getComponentListModelCompRecAt(index);
				if (!next.getAmsi().equals(" ") & !next.getAmsi().equals("0")) //$NON-NLS-1$ //$NON-NLS-2$
				{
					/*
					 * print configured node label with given matp,model, & snum
					 */
					MFSPrintingMethods.matpmcsn(next.getMatp(), next.getMmdl(),
							next.getMspi() + "-" + next.getMcsn().substring(2, 7), //$NON-NLS-1$
							qty, getParentFrame());
				}
				index++;
			}
		}
	}

	/**
	 * Calls the {@link MFSPrintingMethods#mir} method if configured.
	 * 
	 * @param headRec
	 *            the <code>MFSHeaderRec</code> for the work unit
	 * @param nmbrPrln
	 *            <code>headRec.getNmbr() + "," + headRec.getPrln().trim()</code>
	 * @param nmbrAll
	 *            <code>headRec.getNmbr() + ",*ALL"</code>
	 */
	private void swMirLabel(MFSHeaderRec headRec, String nmbrPrln, String nmbrAll) {
		final MFSConfig config = MFSConfig.getInstance();
		String cnfgData1 = "MIR," + nmbrPrln; //$NON-NLS-1$
		String value = config.getConfigValue(cnfgData1);
		if (value.equals(MFSConfig.NOT_FOUND)) {
			String cnfgData2 = "MIR," + nmbrAll; //$NON-NLS-1$
			value = config.getConfigValue(cnfgData2);
		}
		if (!value.equals(MFSConfig.NOT_FOUND)) {
			int qty = 1;
			if (!value.equals("")) //$NON-NLS-1$
			{
				qty = Integer.parseInt(value);
			}

			/* print MIR label */
			MFSPrintingMethods.mir(config.get8CharCellType(), "00000" + headRec.getPrln().substring(0, 7), //$NON-NLS-1$
					headRec.getMctl(), qty, getParentFrame());
		}
	}

	/**
	 * Calls the {@link MFSPrintingMethods#recvpart} method if configured.
	 * 
	 * @param headRec
	 *            the <code>MFSHeaderRec</code> for the work unit
	 * @param nmbrPrln
	 *            <code>headRec.getNmbr() + "," + headRec.getPrln().trim()</code>
	 * @param nmbrAll
	 *            <code>headRec.getNmbr() + ",*ALL"</code>
	 */
	private void swPartNumberLabel(MFSHeaderRec headRec, String nmbrPrln, String nmbrAll) {
		final MFSConfig config = MFSConfig.getInstance();
		String cnfgData1 = "PARTNUMBER," + nmbrPrln; //$NON-NLS-1$
		String value = config.getConfigValue(cnfgData1);
		if (value.equals(MFSConfig.NOT_FOUND)) {
			String cnfgData2 = "PARTNUMBER," + nmbrAll; //$NON-NLS-1$
			value = config.getConfigValue(cnfgData2);
		}
		if (!value.equals(MFSConfig.NOT_FOUND)) {
			int qty = 1;
			if (!value.equals("")) //$NON-NLS-1$
			{
				qty = Integer.parseInt(value);
			}

			/* print part number labels */
			MFSPrintingMethods.recvpart(headRec.getMctl(), headRec.getNmbr(), qty, getParentFrame());
		}
	}

	/**
	 * Calls the {@link MFSPrintingMethods#prodFeatCode} method if configured.
	 * 
	 * @param headRec
	 *            the <code>MFSHeaderRec</code> for the work unit
	 * @param nmbrPrln
	 *            <code>headRec.getNmbr() + "," + headRec.getPrln().trim()</code>
	 * @param nmbrAll
	 *            <code>headRec.getNmbr() + ",*ALL"</code>
	 */
	private void swProdFeatCodeLabel(MFSHeaderRec headRec, String nmbrPrln, String nmbrAll) {
		final MFSConfig config = MFSConfig.getInstance();
		String cnfgData1 = "PRODFEATCODE," + nmbrPrln; //$NON-NLS-1$
		String value = config.getConfigValue(cnfgData1);
		if (value.equals(MFSConfig.NOT_FOUND)) {
			String cnfgData2 = "PRODFEATCODE," + nmbrAll; //$NON-NLS-1$
			value = config.getConfigValue(cnfgData2);
		}
		if (!value.equals(MFSConfig.NOT_FOUND)) {
			int qty = 1;
			if (!value.equals("")) //$NON-NLS-1$
			{
				qty = Integer.parseInt(value);
			}

			/* print prod feature code Sheet */
			MFSPrintingMethods.prodFeatCode(headRec.getMfgn(), headRec.getIdss(), headRec.getOrno(), headRec.getMctl(),
					qty, getParentFrame());
		}
	}

	/**
	 * Calls the {@link MFSPrintingMethods#prodPackLrg} and the
	 * {@link MFSPrintingMethods#prodPackSm} methods if configured.
	 * 
	 * @param headRec
	 *            the <code>MFSHeaderRec</code> for the work unit
	 * @param nmbrPrln
	 *            <code>headRec.getNmbr() + "," + headRec.getPrln().trim()</code>
	 * @param nmbrAll
	 *            <code>headRec.getNmbr() + ",*ALL"</code>
	 */
	private void swProdPackLabels(MFSHeaderRec headRec, String nmbrPrln, String nmbrAll) {
		final MFSConfig config = MFSConfig.getInstance();
		String cnfgDataL1 = "PRODPACKLRG," + nmbrPrln; //$NON-NLS-1$
		String valueL = config.getConfigValue(cnfgDataL1);
		if (valueL.equals(MFSConfig.NOT_FOUND)) {
			String cnfgDataL2 = "PRODPACKLRG," + nmbrAll; //$NON-NLS-1$
			valueL = config.getConfigValue(cnfgDataL2);
		}
		if (!valueL.equals(MFSConfig.NOT_FOUND)) {
			int qty = 1;
			if (!valueL.equals("")) //$NON-NLS-1$
			{
				qty = Integer.parseInt(valueL);
			}

			/* print prod feature code Sheet */
			MFSPrintingMethods.prodPackLrg(headRec.getMfgn(), headRec.getIdss(), headRec.getOrno(), headRec.getMctl(),
					qty, getParentFrame());
		}

		String cnfgDataS1 = "PRODPACKSM," + nmbrPrln; //$NON-NLS-1$
		String valueS = config.getConfigValue(cnfgDataS1);
		if (valueS.equals(MFSConfig.NOT_FOUND)) {
			String cnfgDataS2 = "PRODPACKSM," + nmbrAll; //$NON-NLS-1$
			valueS = config.getConfigValue(cnfgDataS2);
		}
		if (!valueS.equals(MFSConfig.NOT_FOUND)) {
			int qty = 1;
			if (!valueS.equals("")) //$NON-NLS-1$
			{
				qty = Integer.parseInt(valueS);
			}

			/* print prod feature code Sheet */
			MFSPrintingMethods.prodPackSm(headRec.getMfgn(), headRec.getIdss(), headRec.getOrno(), headRec.getMctl(),
					qty, getParentFrame());
		}
	}

	/**
	 * Calls the {@link MFSPrintingMethods#prodsub} method if configured.
	 * 
	 * @param headRec
	 *            the <code>MFSHeaderRec</code> for the work unit
	 * @param nmbrPrln
	 *            <code>headRec.getNmbr() + "," + headRec.getPrln().trim()</code>
	 * @param nmbrAll
	 *            <code>headRec.getNmbr() + ",*ALL"</code>
	 */
	private void swProdSubLabel(MFSHeaderRec headRec, String nmbrPrln, String nmbrAll) {
		final MFSConfig config = MFSConfig.getInstance();
		String cnfgData1 = "PRODSUBLBL," + nmbrPrln; //$NON-NLS-1$
		String value = config.getConfigValue(cnfgData1);
		if (value.equals(MFSConfig.NOT_FOUND)) {
			String cnfgData2 = "PRODSUBLBL," + nmbrAll; //$NON-NLS-1$
			value = config.getConfigValue(cnfgData2);
		}
		if (!value.equals(MFSConfig.NOT_FOUND)) {
			int qty = 1;
			if (!value.equals("")) //$NON-NLS-1$
			{
				qty = Integer.parseInt(value);
			}

			/* print prodsub label */
			MFSPrintingMethods.prodsub(headRec.getPrln(), "J", headRec.getMctl(), qty, getParentFrame()); //$NON-NLS-1$
		}
	}

	/**
	 * Calls the {@link MFSPrintingMethods#rochwu} method if configured.
	 * 
	 * @param headRec
	 *            the <code>MFSHeaderRec</code> for the work unit
	 * @param nmbrPrln
	 *            <code>headRec.getNmbr() + "," + headRec.getPrln().trim()</code>
	 * @param nmbrAll
	 *            <code>headRec.getNmbr() + ",*ALL"</code>
	 */
	private void swRochWuLabel(MFSHeaderRec headRec, String nmbrPrln, String nmbrAll) {
		final MFSConfig config = MFSConfig.getInstance();
		String cnfgData1 = "ROCHWULBL," + nmbrPrln; //$NON-NLS-1$
		String value = config.getConfigValue(cnfgData1);
		if (value.equals(MFSConfig.NOT_FOUND)) {
			String cnfgData2 = "ROCHWULBL," + nmbrAll; //$NON-NLS-1$
			value = config.getConfigValue(cnfgData2);
		}
		if (!value.equals(MFSConfig.NOT_FOUND)) {
			int qty = 1;
			if (!value.equals("")) //$NON-NLS-1$
			{
				qty = Integer.parseInt(value);
			}

			/* print Travel Sheet */
			MFSPrintingMethods.rochwu(headRec.getMctl(), headRec.getMfgn(), headRec.getIdss(), qty, getParentFrame());
		}
	}

	/**
	 * Calls the {@link MFSPrintingMethods#siemenss} and the
	 * {@link MFSPrintingMethods#siemensl} methods if configured.
	 * 
	 * @param headRec
	 *            the <code>MFSHeaderRec</code> for the work unit
	 * @param nmbrPrln
	 *            <code>headRec.getNmbr() + "," + headRec.getPrln().trim()</code>
	 * @param nmbrAll
	 *            <code>headRec.getNmbr() + ",*ALL"</code>
	 */
	private void swSiemensLabels(MFSHeaderRec headRec, String nmbrPrln, String nmbrAll) {
		final MFSConfig config = MFSConfig.getInstance();
		String cnfgDataS1 = "SIEMENSS," + nmbrPrln; //$NON-NLS-1$
		String valueS = config.getConfigValue(cnfgDataS1);
		if (valueS.equals(MFSConfig.NOT_FOUND)) {
			String cnfgDataS2 = "SIEMENSS," + nmbrAll; //$NON-NLS-1$
			valueS = config.getConfigValue(cnfgDataS2);
		}
		if (!valueS.equals(MFSConfig.NOT_FOUND)) {
			int qty = 1;
			if (!valueS.equals("")) //$NON-NLS-1$
			{
				qty = Integer.parseInt(valueS);
			}

			/* print system number label */
			MFSPrintingMethods.siemenss(headRec.getMctl(), qty, getParentFrame());
		}

		String cnfgDataL1 = "SIEMENSL," + nmbrPrln; //$NON-NLS-1$
		String valueL = config.getConfigValue(cnfgDataL1);
		if (valueL.equals(MFSConfig.NOT_FOUND)) {
			String cnfgDataL22 = "SIEMENSL," + nmbrAll; //$NON-NLS-1$
			valueL = config.getConfigValue(cnfgDataL22);
		}
		if (!valueL.equals(MFSConfig.NOT_FOUND)) {
			int qty = 1;
			if (!valueL.equals("")) //$NON-NLS-1$
			{
				qty = Integer.parseInt(valueL);
			}

			/* print system number label */
			MFSPrintingMethods.siemensl(headRec.getMctl(), qty, getParentFrame());
		}
	}

	/**
	 * Calls the {@link MFSPrintingMethods#smallwu} method if configured.
	 * 
	 * @param headRec
	 *            the <code>MFSHeaderRec</code> for the work unit
	 * @param nmbrPrln
	 *            <code>headRec.getNmbr() + "," + headRec.getPrln().trim()</code>
	 * @param nmbrAll
	 *            <code>headRec.getNmbr() + ",*ALL"</code>
	 */
	private void swSmallWULabel(MFSHeaderRec headRec, String nmbrPrln, String nmbrAll) {
		final MFSConfig config = MFSConfig.getInstance();
		String cnfgData1 = "SMALLWU," + nmbrPrln; //$NON-NLS-1$
		String value = config.getConfigValue(cnfgData1);
		if (value.equals(MFSConfig.NOT_FOUND)) {
			String cnfgData2 = "SMALLWU," + nmbrAll; //$NON-NLS-1$
			value = config.getConfigValue(cnfgData2);
		}
		if (!value.equals(MFSConfig.NOT_FOUND)) {
			int qty = 1;
			if (!value.equals("")) //$NON-NLS-1$
			{
				qty = Integer.parseInt(value);
			}

			MFSPrintingMethods.smallwu(headRec.getMctl(), qty, getParentFrame());
		}
	}

	/**
	 * Calls the {@link MFSPrintingMethods#softwarekey} method if configured.
	 * 
	 * @param headRec
	 *            the <code>MFSHeaderRec</code> for the work unit
	 * @param nmbrPrln
	 *            <code>headRec.getNmbr() + "," + headRec.getPrln().trim()</code>
	 * @param nmbrAll
	 *            <code>headRec.getNmbr() + ",*ALL"</code>
	 */
	private void swSoftKeyLabel(MFSHeaderRec headRec, String nmbrPrln, String nmbrAll) {
		final MFSConfig config = MFSConfig.getInstance();
		String cnfgData1 = "RTVSFTKEY," + nmbrPrln; //$NON-NLS-1$
		String value = config.getConfigValue(cnfgData1);
		if (value.equals(MFSConfig.NOT_FOUND)) {
			String cnfgData2 = "RTVSFTKEY," + nmbrAll; //$NON-NLS-1$
			value = config.getConfigValue(cnfgData2);
		}
		if (!value.equals(MFSConfig.NOT_FOUND)) {
			int qty = 1;
			if (!value.equals("")) //$NON-NLS-1$
			{
				qty = Integer.parseInt(value);
			}

			MFSPrintingMethods.softwarekey(headRec.getMfgn(), headRec.getIdss(), headRec.getMctl(), qty,
					getParentFrame());
		}
	}

	// ADDED METHOD 31867EM
	/**
	 * Calls the {@link MFSPrintingMethods#softKeycode} method if configured.
	 * 
	 * @param headRec
	 *            the <code>MFSHeaderRec</code> for the work unit
	 * @param nmbrPrln
	 *            <code>headRec.getNmbr() + "," + headRec.getPrln().trim()</code>
	 * @param nmbrAll
	 *            <code>headRec.getNmbr() + ",*ALL"</code>
	 */
	private void swSoftwareKeycodeTrx(MFSHeaderRec headRec, String nmbrPrln, String nmbrAll) {
		final MFSConfig config = MFSConfig.getInstance();
		String cnfgData = "SFTKEYCODE," + nmbrPrln; //$NON-NLS-1$
		String value = config.getConfigValue(cnfgData);
		if (value.equals(MFSConfig.NOT_FOUND)) {
			cnfgData = "SFTKEYCODE," + nmbrAll; //$NON-NLS-1$
			value = config.getConfigValue(cnfgData);
			if (value.equals(MFSConfig.NOT_FOUND)) {
				cnfgData = "SFTKEYCODE,*ALL," + headRec.getPrln().trim(); //$NON-NLS-1$
				value = config.getConfigValue(cnfgData);
				if (value.equals(MFSConfig.NOT_FOUND)) {
					cnfgData = "SFTKEYCODE,*ALL,*ALL"; //$NON-NLS-1$
					value = config.getConfigValue(cnfgData);
				}
			}
		}
		if (!value.equals(MFSConfig.NOT_FOUND)) {
			int qty = 1;
			if (!value.equals("")) //$NON-NLS-1$
			{
				qty = Integer.parseInt(value);
			}

			MFSPrintingMethods.softKeycode(headRec.getMctl(), " ", qty, getParentFrame()); //$NON-NLS-1$
		}
	}

	/**
	 * Calls the {@link MFSPrintingMethods#spd11s} method if configured.
	 * 
	 * @param headRec
	 *            the <code>MFSHeaderRec</code> for the work unit
	 * @param nmbrPrln
	 *            <code>headRec.getNmbr() + "," + headRec.getPrln().trim()</code>
	 * @param nmbrAll
	 *            <code>headRec.getNmbr() + ",*ALL"</code>
	 */
	private void swSPD11SLabel(MFSHeaderRec headRec, String nmbrPrln, String nmbrAll) {
		final MFSConfig config = MFSConfig.getInstance();
		String cnfgData1 = "SPD11S," + nmbrPrln; //$NON-NLS-1$
		String value = config.getConfigValue(cnfgData1);
		if (value.equals(MFSConfig.NOT_FOUND)) {
			String cnfgData2 = "SPD11S," + nmbrAll; //$NON-NLS-1$
			value = config.getConfigValue(cnfgData2);
		}
		if (!value.equals(MFSConfig.NOT_FOUND)) {
			int qty = 1;
			if (!value.equals("")) //$NON-NLS-1$
			{
				qty = Integer.parseInt(value);
			}

			/* print configured node label */
			MFSPrintingMethods.spd11s(headRec.getPrln(), headRec.getMctl(), qty, getParentFrame());
		}
	}

	/**
	 * New method ~22 Calls the {@link MFSPrintingMethods#syscert} method if
	 * configured.
	 * 
	 * @param headRec
	 *            the <code>MFSHeaderRec</code> for the work unit
	 * @param nmbrPrln
	 *            <code>headRec.getNmbr() + "," + headRec.getPrln().trim()</code>
	 * @param nmbrAll
	 *            <code>headRec.getNmbr() + ",*ALL"</code>
	 */
	private void swSYSCERTLabel(MFSHeaderRec headRec, String nmbrPrln, String nmbrAll) {
		final MFSConfig config = MFSConfig.getInstance();
		String cnfgData1 = "SYSTEMCERT," + nmbrPrln; //$NON-NLS-1$
		String value = config.getConfigValue(cnfgData1);
		if (value.equals(MFSConfig.NOT_FOUND)) {
			String cnfgData2 = "SYSTEMCERT," + nmbrAll; //$NON-NLS-1$
			value = config.getConfigValue(cnfgData2);
		}
		if (!value.equals(MFSConfig.NOT_FOUND)) {
			int qty = 1;
			if (!value.equals("")) //$NON-NLS-1$
			{
				qty = Integer.parseInt(value);
			}
			/* Print the System Agency Certification label */
			MFSPrintingMethods.syscertlbls(headRec.getMctl(), qty, getParentFrame());
		}
	}

	/**
	 * Calls the {@link MFSPrintingMethods#sysinfo} method if configured.
	 * 
	 * @param headRec
	 *            the <code>MFSHeaderRec</code> for the work unit
	 * @param nmbrPrln
	 *            <code>headRec.getNmbr() + "," + headRec.getPrln().trim()</code>
	 * @param nmbrAll
	 *            <code>headRec.getNmbr() + ",*ALL"</code>
	 */
	private void swSystemInfoLabel(MFSHeaderRec headRec, String nmbrPrln, String nmbrAll) {
		final MFSConfig config = MFSConfig.getInstance();
		String cnfgData1 = "SYSTEMINFO," + nmbrPrln; //$NON-NLS-1$
		String value = config.getConfigValue(cnfgData1);
		if (value.equals(MFSConfig.NOT_FOUND)) {
			String cnfgData2 = "SYSTEMINFO," + nmbrAll; //$NON-NLS-1$
			value = config.getConfigValue(cnfgData2);
		}
		if (!value.equals(MFSConfig.NOT_FOUND)) {
			int qty = 1;
			if (!value.equals("")) //$NON-NLS-1$
			{
				qty = Integer.parseInt(value);
			}

			/* print system info label */
			MFSPrintingMethods.sysinfo(headRec.getMctl(), qty, getParentFrame());
		}
	}

	/**
	 * Calls the {@link MFSPrintingMethods#sysnumb} method if configured.
	 * 
	 * @param headRec
	 *            the <code>MFSHeaderRec</code> for the work unit
	 * @param nmbrPrln
	 *            <code>headRec.getNmbr() + "," + headRec.getPrln().trim()</code>
	 * @param nmbrAll
	 *            <code>headRec.getNmbr() + ",*ALL"</code>
	 */
	private void swSystemNumberLabel(MFSHeaderRec headRec, String nmbrPrln, String nmbrAll) {
		final MFSConfig config = MFSConfig.getInstance();
		String cnfgData1 = "SYSTEMNUMBER," + nmbrPrln; //$NON-NLS-1$
		String value = config.getConfigValue(cnfgData1);
		if (value.equals(MFSConfig.NOT_FOUND)) {
			String cnfgData2 = "SYSTEMNUMBER," + nmbrAll; //$NON-NLS-1$
			value = config.getConfigValue(cnfgData2);
		}
		if (!value.equals(MFSConfig.NOT_FOUND)) {
			int qty = 1;
			if (!value.equals("")) //$NON-NLS-1$
			{
				qty = Integer.parseInt(value);
			}

			/* print system number label */
			MFSPrintingMethods.sysnumb(headRec.getMctl(), qty, getParentFrame());
		}
	}

	/**
	 * Calls the {@link MFSPrintingMethods#systempassword} method if configured.
	 * 
	 * @param headRec
	 *            the <code>MFSHeaderRec</code> for the work unit
	 * @param nmbrPrln
	 *            <code>headRec.getNmbr() + "," + headRec.getPrln().trim()</code>
	 * @param nmbrAll
	 *            <code>headRec.getNmbr() + ",*ALL"</code>
	 */
	private void swSystemPasswordLabel(MFSHeaderRec headRec, String nmbrPrln, String nmbrAll) {
		final MFSConfig config = MFSConfig.getInstance();
		String cnfgData1 = "SYSTEMPWD," + nmbrPrln; //$NON-NLS-1$
		String value = config.getConfigValue(cnfgData1);
		if (value.equals(MFSConfig.NOT_FOUND)) {
			String cnfgData2 = "SYSTEMPWD," + nmbrAll; //$NON-NLS-1$
			value = config.getConfigValue(cnfgData2);
		}
		if (!value.equals(MFSConfig.NOT_FOUND)) {
			int qty = 0; // ~15C
			if (!value.equals("")) //$NON-NLS-1$
			{
				qty = Integer.parseInt(value); // ~15C
			}

			/* print System ID Sheet */
			// ~15C Use qty instead of 1
			MFSPrintingMethods.systempassword(headRec.getMfgn(), headRec.getIdss(), headRec.getMctl(),
					headRec.getCmat(), headRec.getMmdl(), qty, getParentFrame());
		}
	}

	/**
	 * Calls the {@link MFSPrintingMethods#tcodBilling},
	 * {@link MFSPrintingMethods#tcodMinutes}, and the
	 * {@link MFSPrintingMethods#tcodPrepay} methods if configured.
	 * 
	 * @param headRec
	 *            the <code>MFSHeaderRec</code> for the work unit
	 * @param nmbrPrln
	 *            <code>headRec.getNmbr() + "," + headRec.getPrln().trim()</code>
	 * @param nmbrAll
	 *            <code>headRec.getNmbr() + ",*ALL"</code>
	 */
	private void swTcodLabels(MFSHeaderRec headRec, String nmbrPrln, String nmbrAll) {
		final MFSConfig config = MFSConfig.getInstance();
		String cnfgDataB = "TCODBILLING," + nmbrPrln; //$NON-NLS-1$
		String valueQtyB = config.getConfigValue(cnfgDataB);
		if (valueQtyB.equals(MFSConfig.NOT_FOUND)) {
			String cnfgDataB2 = "TCODBILLING," + nmbrAll; //$NON-NLS-1$
			valueQtyB = config.getConfigValue(cnfgDataB2);
		}
		if (!valueQtyB.equals(MFSConfig.NOT_FOUND)) {
			int qty = 1;
			if (!valueQtyB.equals("")) //$NON-NLS-1$
			{
				qty = Integer.parseInt(valueQtyB);
			}

			MFSPrintingMethods.tcodBilling(headRec.getMctl(), headRec.getMfgn(), headRec.getIdss(), headRec.getOrno(),
					headRec.getMatp(), headRec.getMmdl(), headRec.getMspi(), headRec.getMcsn(), qty, getParentFrame());
		}

		/* ~23A Start call MFSPrintingMethods.tcodMinutes if configured. */
		String cnfgDataM = "TCODMINUTES," + nmbrPrln; //$NON-NLS-1$
		String valueQtyM = config.getConfigValue(cnfgDataM);
		if (valueQtyM.equals(MFSConfig.NOT_FOUND)) {
			String cnfgDataM2 = "TCODMINUTES," + nmbrAll; //$NON-NLS-1$
			valueQtyM = config.getConfigValue(cnfgDataM2);
		}
		if (!valueQtyM.equals(MFSConfig.NOT_FOUND)) {
			int qty = 1;
			if (!valueQtyM.equals("")) //$NON-NLS-1$
			{
				qty = Integer.parseInt(valueQtyM);
			}

			MFSPrintingMethods.tcodMinutes(headRec.getMctl(), headRec.getMfgn(), headRec.getIdss(), headRec.getOrno(),
					headRec.getMatp(), headRec.getMmdl(), headRec.getMspi(), headRec.getMcsn(), qty, getParentFrame());
		}
		/* ~23A End call MFSPrintingMethods.tcodMinutes if configured. */

		String cnfgDataP = "TCODPREPAY," + nmbrPrln; //$NON-NLS-1$
		String valueQtyP = config.getConfigValue(cnfgDataP);
		if (valueQtyP.equals(MFSConfig.NOT_FOUND)) {
			String cnfgDataP2 = "TCODPREPAY," + nmbrAll; //$NON-NLS-1$
			valueQtyP = config.getConfigValue(cnfgDataP2);
		}
		if (!valueQtyP.equals(MFSConfig.NOT_FOUND)) {
			int qty = 1;
			if (!valueQtyP.equals("")) //$NON-NLS-1$
			{
				qty = Integer.parseInt(valueQtyP);
			}

			MFSPrintingMethods.tcodPrepay(headRec.getMctl(), headRec.getMfgn(), headRec.getIdss(), headRec.getOrno(),
					headRec.getMatp(), headRec.getMmdl(), headRec.getMspi(), headRec.getMcsn(), qty, getParentFrame());
		}
	}

	// 10/16/03 - PTR #24594DK - Emily Engelbert - Added call to new
	// setTcodBrln() method
	// 10/28/04 - PTR #28663KI - Dave Fichtinger - Set bufferStatus field to 0
	// if this is a tcod part
	// ~6C Parameter config is never used
	/**
	 * Calls the RTV_CUODKY transaction and processes the results if configured.
	 * 
	 * @param headRec
	 *            the <code>MFSHeaderRec</code> for the work unit
	 */
	@SuppressWarnings({ "rawtypes" })
	private void swTcodTrx(MFSHeaderRec headRec) {
		final MFSConfig config = MFSConfig.getInstance();
		String cnfgData1 = "TCODSTATION," //$NON-NLS-1$
				+ headRec.getPrln().trim() + "," //$NON-NLS-1$
				+ headRec.getNmbr();
		String value = config.getConfigValue(cnfgData1);
		if (value.equals(MFSConfig.NOT_FOUND)) {
			cnfgData1 = "TCODSTATION," //$NON-NLS-1$
					+ headRec.getPrln().trim() + ",*ALL"; //$NON-NLS-1$
			value = config.getConfigValue(cnfgData1);
			if (value.equals(MFSConfig.NOT_FOUND)) {
				cnfgData1 = "TCODSTATION,*ALL," + headRec.getNmbr(); //$NON-NLS-1$
				value = config.getConfigValue(cnfgData1);
				if (value.equals(MFSConfig.NOT_FOUND)) {
					cnfgData1 = "TCODSTATION,*ALL,*ALL"; //$NON-NLS-1$
					value = config.getConfigValue(cnfgData1);
				}
			}
		}

		if (!value.equals(MFSConfig.NOT_FOUND)) {
			String data = "RTV_CUODKY" + headRec.getMfgn() + headRec.getIdss(); //$NON-NLS-1$

			MFSTransaction rtv_cuodky = new MFSFixedTransaction(data);
			rtv_cuodky.setActionMessage("Retrieving Capcacity Card Info, Please Wait..."); //$NON-NLS-1$
			MFSComm.getInstance().execute(rtv_cuodky, this);
			int rc = rtv_cuodky.getReturnCode();

			if (rc == 0 || rc == 666) {
				// ~21C Use parseRTV_CUODKY to parse output
				Vector myRows = MFSTransactionUtils.parseRTV_CUODKY(rtv_cuodky);

				// Data has been parsed into hash tables
				// Check each record to determine what needs to be done
				for (int index = 0; index < myRows.size(); index++) {
					Hashtable rowData = (Hashtable) myRows.elementAt(index);

					/*
					 * ~21C Ignore the record if there is an activation code or
					 * the activation code contains asterisks. Both conditions
					 * can be checked by comparing the activation to 34 blanks.
					 */
					if (rowData.get("CODE").equals("                                  ")) //$NON-NLS-1$ //$NON-NLS-2$
					{
						// Process the blank activation codes.
						MFSDirectWorkPanel dirWorkPanel = getParentFrame().getDirectWorkPanel();
						for (int compIndex = 0; compIndex < dirWorkPanel.getComponentListModelSize(); compIndex++) {
							MFSComponentRec next = dirWorkPanel.getComponentListModelCompRecAt(compIndex);

							if (next.getItem().equals((rowData.get("FFBM"))) //$NON-NLS-1$
									|| next.getItem().equals((rowData.get("INPN")))) //$NON-NLS-1$
							{
								next.setIsTcodPart(true);

								// If it is a IsTcodPart, can't buffer the
								// transaction
								if (config.containsConfigEntry("TRXBUFFER")) //$NON-NLS-1$
								{
									next.setBufferStatus("0"); //$NON-NLS-1$
								} else {
									next.setBufferStatus(" "); //$NON-NLS-1$
								}

								next.setTcodPart(((String) rowData.get("INPN"))); //$NON-NLS-1$
								next.setTcodAcqy(next.getTcodAcqy() + ((String) rowData.get("ACQY")) + ','); //$NON-NLS-1$
								next.setTcodOdri(next.getTcodOdri() + ((String) rowData.get("ODRI")) + ','); //$NON-NLS-1$
								next.setTcodBrln(next.getTcodBrln() + ((String) rowData.get("BRLN")) + ','); //$NON-NLS-1$
								break;
							}
						} /* end for loop */
					} // end if CODE == blanks
				} /* end for loop */
			} else // rtv_cuodky failed
			{
				String title = "RTV_CUODKY Error"; //$NON-NLS-1$
				String erms = rtv_cuodky.getErms();
				IGSMessageBox.showOkMB(getParentFrame(), title, erms, null);
			}
		} // end of config entry found
	}

	/**
	 * Calls the {@link MFSPrintingMethods#tracksub} method if configured.
	 * 
	 * @param headRec
	 *            the <code>MFSHeaderRec</code> for the work unit
	 * @param nmbrPrln
	 *            <code>headRec.getNmbr() + "," + headRec.getPrln().trim()</code>
	 * @param nmbrAll
	 *            <code>headRec.getNmbr() + ",*ALL"</code>
	 */
	private void swTrackSubLabel(MFSHeaderRec headRec, String nmbrPrln, String nmbrAll) {
		final MFSConfig config = MFSConfig.getInstance();
		String cnfgData1 = "TRACKSUBLBL," + nmbrPrln; //$NON-NLS-1$
		String value = config.getConfigValue(cnfgData1);
		if (value.equals(MFSConfig.NOT_FOUND)) {
			String cnfgData2 = "TRACKSUBLBL," + nmbrAll; //$NON-NLS-1$
			value = config.getConfigValue(cnfgData2);
		}
		if (!value.equals(MFSConfig.NOT_FOUND)) {
			int qty = 1;
			if (!value.equals("")) //$NON-NLS-1$
			{
				qty = Integer.parseInt(value);
			}

			/* print tracksub label */
			MFSPrintingMethods.tracksub(headRec.getPrln(), "J", headRec.getMctl(), qty, getParentFrame()); //$NON-NLS-1$
		}
	}

	/**
	 * Calls the {@link MFSPrintingMethods#rtvtravel} method if configured.
	 * 
	 * @param headRec
	 *            the <code>MFSHeaderRec</code> for the work unit
	 * @param nmbrPrln
	 *            <code>headRec.getNmbr() + "," + headRec.getPrln().trim()</code>
	 * @param nmbrAll
	 *            <code>headRec.getNmbr() + ",*ALL"</code>
	 */
	private void swTravelLabel(MFSHeaderRec headRec, String nmbrPrln, String nmbrAll) {
		final MFSConfig config = MFSConfig.getInstance();
		String cnfgData1 = "TRAVEL," + nmbrPrln; //$NON-NLS-1$
		String value = config.getConfigValue(cnfgData1);
		if (value.equals(MFSConfig.NOT_FOUND)) {
			String cnfgData2 = "TRAVEL," + nmbrAll; //$NON-NLS-1$
			value = config.getConfigValue(cnfgData2);
		}
		if (!value.equals(MFSConfig.NOT_FOUND)) {
			int qty = 1;
			if (!value.equals("")) //$NON-NLS-1$
			{
				qty = Integer.parseInt(value);
			}

			/* print Travel Sheet */
			MFSPrintingMethods.rtvtravel(headRec.getMctl(), qty, getParentFrame());
		}
	}

	/**
	 * Calls the {@link MFSPrintingMethods#wuipid} method if configured.
	 * 
	 * @param headRec
	 *            the <code>MFSHeaderRec</code> for the work unit
	 * @param nmbrPrln
	 *            <code>headRec.getNmbr() + "," + headRec.getPrln().trim()</code>
	 * @param nmbrAll
	 *            <code>headRec.getNmbr() + ",*ALL"</code>
	 */
	private void swWorkUnitLabel(MFSHeaderRec headRec, String nmbrPrln, String nmbrAll) {
		final MFSConfig config = MFSConfig.getInstance();
		String cnfgData1 = "WORKUNIT," + nmbrPrln; //$NON-NLS-1$
		String value = config.getConfigValue(cnfgData1);
		if (value.equals(MFSConfig.NOT_FOUND)) {
			String cnfgData2 = "WORKUNIT," + nmbrAll; //$NON-NLS-1$
			value = config.getConfigValue(cnfgData2);
		}
		if (!value.equals(MFSConfig.NOT_FOUND)) {
			int qty = 1;
			if (!value.equals("")) //$NON-NLS-1$
			{
				qty = Integer.parseInt(value);
			}

			/* print workunit label */
			MFSPrintingMethods.wuipid(headRec.getMctl(), qty, getParentFrame());
		}
	}

	/**
	 * Calls the {@link MFSPrintingMethods#wwid} method if configured.
	 * 
	 * @param headRec
	 *            the <code>MFSHeaderRec</code> for the work unit
	 * @param nmbrPrln
	 *            <code>headRec.getNmbr() + "," + headRec.getPrln().trim()</code>
	 * @param nmbrAll
	 *            <code>headRec.getNmbr() + ",*ALL"</code>
	 */
	private void swWWIDLabel(MFSHeaderRec headRec, String nmbrPrln, String nmbrAll) {
		final MFSConfig config = MFSConfig.getInstance();
		String cnfgData1 = "WWIDLBL," + nmbrPrln; //$NON-NLS-1$
		String value1 = config.getConfigValue(cnfgData1);
		if (value1.equals(MFSConfig.NOT_FOUND)) {
			String cnfgData2 = "WWIDLBL," + nmbrAll; //$NON-NLS-1$
			value1 = config.getConfigValue(cnfgData2);
		}
		if (!value1.equals(MFSConfig.NOT_FOUND)) {
			int qty = 1;
			if (!value1.equals("")) //$NON-NLS-1$
			{
				qty = Integer.parseInt(value1);
			}

			int rc = MFSPrintingMethods.wwid(headRec.getMatp(), headRec.getMmdl(), headRec.getMspi(),
					headRec.getMcsn().substring(2), config.get8CharUser(), "Y", qty, getParentFrame()); //$NON-NLS-1$

			if (rc == 0) {
				headRec.setWwid("WWIDAssigned    "); //$NON-NLS-1$
			} else if (rc == 666) {
				headRec.setWwid("WWIDNotNeeded   "); //$NON-NLS-1$
			} else {
				headRec.setWwid("WWIDError       "); //$NON-NLS-1$
			}
		}
	}

	// ~41A Weight label
	/**
	 * Calls the {@link MFSPrintingMethods#weightLabel} method if configured.
	 * 
	 * @param headRec
	 *            the <code>MFSHeaderRec</code> for the work unit
	 * @param nmbrPrln
	 *            <code>headRec.getNmbr() + "," + headRec.getPrln().trim()</code>
	 * @param nmbrAll
	 *            <code>headRec.getNmbr() + ",*ALL"</code>
	 */
	private void swWeightLabel(MFSHeaderRec headRec, String nmbrPrln, String nmbrAll) {
		final MFSConfig config = MFSConfig.getInstance();
		String cnfgData = "WEIGHTLBLSTART," + nmbrPrln; //$NON-NLS-1$
		String value = config.getConfigValue(cnfgData);
		if (value.equals(MFSConfig.NOT_FOUND)) {
			cnfgData = "WEIGHTLBLSTART," + nmbrAll; //$NON-NLS-1$
			value = config.getConfigValue(cnfgData);
		}
		if (!value.equals(MFSConfig.NOT_FOUND)) {
			int qty = 1;
			if (!value.equals("")) //$NON-NLS-1$
			{
				qty = Integer.parseInt(value);
			}

			/* print Weight Label */
			MFSPrintingMethods.weightLabel(headRec.getMctl(), headRec.getCntr(), "STARTWU", qty, getParentFrame()); //$NON-NLS-1$
		}
	}

	// ~41A Warranty Card label
	/**
	 * Calls the {@link MFSPrintingMethods#warranty} method if configured.
	 * 
	 * @param headRec
	 *            the <code>MFSHeaderRec</code> for the work unit
	 * @param nmbrPrln
	 *            <code>headRec.getNmbr() + "," + headRec.getPrln().trim()</code>
	 * @param nmbrAll
	 *            <code>headRec.getNmbr() + ",*ALL"</code>
	 */
	private void swWarrantyCard(MFSHeaderRec headRec, String nmbrPrln, String nmbrAll) {
		final MFSConfig config = MFSConfig.getInstance();
		String cnfgData = "WARRANTYSTART," + nmbrPrln; //$NON-NLS-1$
		String value = config.getConfigValue(cnfgData);
		if (value.equals(MFSConfig.NOT_FOUND)) {
			cnfgData = "WARRANTYSTART," + nmbrAll; //$NON-NLS-1$
			value = config.getConfigValue(cnfgData);
		}
		if (!value.equals(MFSConfig.NOT_FOUND)) {
			int qty = 1;
			if (!value.equals("")) //$NON-NLS-1$
			{
				qty = Integer.parseInt(value);
			}

			/* print Warranty Card Label */
			StringBuffer lbldta = new StringBuffer("<MCTL>"); // ~43A //$NON-NLS-1$
			lbldta.append(headRec.getMctl()); // ~43A
			lbldta.append("</MCTL>"); // ~43A //$NON-NLS-1$
			MFSPrintingMethods.warrantyCard(lbldta.toString(), "STARTWU", "", qty, "", getParentFrame()); // ~43C //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
	}

	// ~45A Feature on Demand label
	/**
	 * Calls the {@link MFSPrintingMethods#fodLabel} method if configured.
	 * 
	 * @param headRec
	 *            the <code>MFSHeaderRec</code> for the work unit
	 * @param nmbrPrln
	 *            <code>headRec.getNmbr() + "," + headRec.getPrln().trim()</code>
	 * @param nmbrAll
	 *            <code>headRec.getNmbr() + ",*ALL"</code>
	 */
	private void swFoDLabel(MFSHeaderRec headRec, String nmbrPrln, String nmbrAll) {
		int qty = 1;
		final MFSConfig config = MFSConfig.getInstance();
		String cnfgData = "FODSTART," + nmbrPrln;
		String value = config.getConfigValue(cnfgData);
		if (value.equals(MFSConfig.NOT_FOUND)) {
			cnfgData = "FODSTART," + nmbrAll;
			value = config.getConfigValue(cnfgData);
		}
		if (!value.equals(MFSConfig.NOT_FOUND)) {
			if (!value.equals("")) {
				qty = Integer.parseInt(value);
			}

			/* print FoD Label */
			MFSPrintingMethods.fodLabel(headRec.getMfgn(), headRec.getIdss(), headRec.getMctl(), "STARTWU", qty,
					getParentFrame());

			/* Check if we need to print a Roch MIR label */
			String cnfgDatMRR = "ROCHMIRRLBL," + nmbrPrln; //$NON-NLS-1$
			String valMRR = config.getConfigValue(cnfgDatMRR);
			if (valMRR.equals(MFSConfig.NOT_FOUND)) {
				String cnfgDatMRR2 = "ROCHMIRRLBL," + nmbrAll; //$NON-NLS-1$
				valMRR = config.getConfigValue(cnfgDatMRR2);
			}
			if (!valMRR.equals(MFSConfig.NOT_FOUND)) {
				if (!valMRR.equals("")) //$NON-NLS-1$
				{
					qty = Integer.parseInt(valMRR);
				}

				/* print roch mir label */
				String part = "00000" + headRec.getPrln().substring(0, 7); //$NON-NLS-1$
				MFSPrintingMethods.mir(config.get8CharCellType(), part, headRec.getMctl(), qty, getParentFrame());
			}
		}
	}

	/**
	 * Invoked when {@link #pbTearDown} is selected to tear down a work unit.
	 */
	private void teardown() {
		boolean autoRepeatTransaction = this.cnfgEfficiencyOn; // ~42
		try {
			final MFSConfig config = MFSConfig.getInstance();
			int rc = 0;
			boolean cont = false;
			removeMyListeners();

			do {
				MFSWorkUnitPNSNDialog myWrkUnitPNSND = new MFSWorkUnitPNSNDialog(getParentFrame());
				myWrkUnitPNSND.setLocationRelativeTo(getParentFrame()); // ~17A
				myWrkUnitPNSND.setVisible(true);

				if (myWrkUnitPNSND.getPressedEnter()) {
					this.fieldCurrMctl = myWrkUnitPNSND.getMctl();

					MFSTearConfirmationDialog myTearWrkUnitD = new MFSTearConfirmationDialog(getParentFrame(),
							this.fieldCurrMctl); // ~48A;

					if (myTearWrkUnitD.getErrorCode() == 0) // ~48A
					{ // ~48A
						myTearWrkUnitD.setLocationRelativeTo(getParentFrame()); // ~48A
						myTearWrkUnitD.setVisible(true); // ~48A

						if (myTearWrkUnitD.getPressedYes()) // ~48A
						{
							cont = true;
						}
					} // ~48A
				} else {
					autoRepeatTransaction = false;
				}
				if (!cont) /* @1A */
				{
					autoRepeatTransaction = false;
				} else {
					DCNFG_FAB dcnfg_fab = new DCNFG_FAB(this); // ~48A

					dcnfg_fab.setInputMCTL(this.fieldCurrMctl); // ~48A
					dcnfg_fab.setInputUSER(config.get8CharUser()); // ~48A
					dcnfg_fab.setInputPGM("TEAR "); // ~48A
					dcnfg_fab.setInputTMCTL("*NONE   "); // ~48A
					dcnfg_fab.setInputCTYP(config.get8CharCellType()); // ~48C
					/* @3 Add celltype to DCNFG_FAB */

					dcnfg_fab.execute(); // ~48A

					rc = dcnfg_fab.getReturnCode();

					if (rc != 0) { // ~48C Use getErrorMessage instead of
									// getErms method
						IGSMessageBox.showOkMB(getParentFrame(), null, dcnfg_fab.getErrorMessage(), null);
					} else {
						StringBuffer data2 = new StringBuffer();
						data2.append("REMAP     "); //$NON-NLS-1$
						data2.append(this.fieldCurrMctl);
						data2.append("U"); //$NON-NLS-1$
						data2.append("TEAR"); //$NON-NLS-1$
						data2.append(config.get8CharCellType());

						MFSTransaction remap = new MFSFixedTransaction(data2.toString());
						remap.setActionMessage("Remapping Work Unit, Please Wait..."); //$NON-NLS-1$
						MFSComm.getInstance().execute(remap, this);
						rc = remap.getReturnCode();

						if (rc == 30 || rc == 0) {
							rc = str_wu("Z"); //$NON-NLS-1$
							if (rc == 0) {
								autoRepeatTransaction = false;
							}
						} else {
							IGSMessageBox.showOkMB(getParentFrame(), null, remap.getErms(), null);
						}
					} /* end of good DCNFG_FAB call */
				} /* end of pressed enter on work unit dialog */
			} while (autoRepeatTransaction); /*
												 * End autoRepeattransaction check
												 */
		} /* end of try */
		catch (Exception e) {
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
		addMyListeners();
	}

	/** Invoked when {@link #pbUnburn} is selected. */
	private void unburn() {
		try {
			int rc = 0;

			removeMyListeners();

			/* Get the work unit to be unburned */
			MFSWorkUnitPNSNDialog myWrkUnitPNSND = new MFSWorkUnitPNSNDialog(getParentFrame());
			myWrkUnitPNSND.setLocationRelativeTo(getParentFrame()); // ~17A
			myWrkUnitPNSND.setVisible(true);

			if (myWrkUnitPNSND.getPressedEnter()) {
				this.fieldCurrMctl = myWrkUnitPNSND.getMctl();

				StringBuffer data = new StringBuffer();
				data.append("REMAP     "); //$NON-NLS-1$
				data.append(this.fieldCurrMctl);
				data.append("U"); //$NON-NLS-1$
				data.append("UBRN"); //$NON-NLS-1$
				data.append(MFSConfig.getInstance().get8CharCellType());

				MFSTransaction remap = new MFSFixedTransaction(data.toString());
				remap.setActionMessage("Remapping Work Unit, Please Wait..."); //$NON-NLS-1$
				MFSComm.getInstance().execute(remap, this);
				rc = remap.getReturnCode();

				if (rc == 30 || rc == 0) {
					str_wu("J"); //$NON-NLS-1$
				} else {
					IGSMessageBox.showOkMB(getParentFrame(), null, remap.getErms(), null);
				}
			}
		} catch (Exception e) {
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
		addMyListeners();
	}

	/*
	 * CHANGED - 02/14/2004 for PTR for Pat Reuvers - don't validate against
	 * IDSS anymore CHANGED - 05/19/2006 for PTR 29807JM Blanca Aceves - don't
	 * validate against IDSS anymore, removed IDSS parameter @2
	 */
	/**
	 * Displays an <code>MFSCSUDialog</code> to perform CSU System Barcode
	 * Verification.
	 * 
	 * @param currMfgn
	 *            the mfgn of the work unit.
	 * @return one of the following return codes:
	 *         <ul>
	 *         <li>-1: invalid scan</li>
	 *         <li>0: scanned value does not match mfgn</li>
	 *         <li>1: correct mfgn scanned</li>
	 *         <li>2: user selected suspend</li>
	 *         </ul>
	 */
	private int verifyCSU(String currMfgn) {
		int rc = 0;

		MFSCSUDialog csuDialog = new MFSCSUDialog(getParentFrame());
		csuDialog.setLocationRelativeTo(getParentFrame()); // ~17A
		csuDialog.setVisible(true);

		// if correct scanned in mfgn when user hits enter, pass back a 1 @2C
		// otherwise it failed, so send a 0 return code - error message handled
		// in calling function
		if (csuDialog.getPressedEnter()) {
			int parseret = csuDialog.parseCSU(currMfgn); /* @2C Start */
			if (parseret == 1) {
				rc = 1; /* Correct mfgn scanned */
			} else if (parseret == 0) {
				rc = 0; /* scanned value doesn't match mfgn */
			} else {
				rc = -1; /* invalid scan */
			} /* @2C End */
		}
		// if user hits the suspend button instead of the enter, send back a 2
		else if (csuDialog.getPressedSuspend()) {
			rc = 2;
		}
		return rc;
	}

	/** Invoked when {@link #pbTestWIPDriver} is selected. */
	private void wipTest() {
		try {
			int rc = 0;
			String errorString = ""; //$NON-NLS-1$

			removeMyListeners();

			MfsXMLDocument xml_data1 = new MfsXMLDocument("RTV_CLGWIP"); //$NON-NLS-1$
			xml_data1.addOpenTag("DATA"); //$NON-NLS-1$
			xml_data1.addCompleteField("CTYP", MFSConfig.getInstance().get8CharCellType()); //$NON-NLS-1$
			xml_data1.addCompleteField("LTYP", "W"); //$NON-NLS-1$ //$NON-NLS-2$
			xml_data1.addCloseTag("DATA"); //$NON-NLS-1$
			xml_data1.finalizeXML();

			MFSTransaction rtv_clgwip = new MFSXmlTransaction(xml_data1.toString());
			rtv_clgwip.setActionMessage("Retrieving List of WIP Drivers Lines, Please Wait..."); //$NON-NLS-1$
			MFSComm.getInstance().execute(rtv_clgwip, this);
			rc = rtv_clgwip.getReturnCode();

			if (rc != 0) {
				errorString = rtv_clgwip.getErms();
			} else {
				MfsXMLParser xmlParser = new MfsXMLParser(rtv_clgwip.getOutput());
				String tempWipd = ""; //$NON-NLS-1$

				DefaultListModel wipdListModel = new DefaultListModel();

				try {
					tempWipd = xmlParser.getField("WIPD"); //$NON-NLS-1$
				} catch (mfsxml.MISSING_XML_TAG_EXCEPTION e) {
					rc = 10;
					errorString = "No WIP Drivers Found in MFS "; //$NON-NLS-1$
				}
				if (rc == 0) {
					wipdListModel.addElement(tempWipd);

					tempWipd = xmlParser.getNextField("WIPD"); //$NON-NLS-1$

					while (!tempWipd.equals("")) //$NON-NLS-1$
					{
						wipdListModel.addElement(tempWipd);
						tempWipd = xmlParser.getNextField("WIPD"); //$NON-NLS-1$
					}

					MFSWipDriverDialog wipJD = new MFSWipDriverDialog(getParentFrame(), wipdListModel);
					wipJD.setLocationRelativeTo(getParentFrame()); // ~17A
					wipJD.setVisible(true);
					if (wipJD.getProceedSelected()) {
						if (wipJD.getSelectedListOption() != null) {
							MFSWipDriverTestDialog wipTestJD = new MFSWipDriverTestDialog(getParentFrame(), this,
									wipJD.getSelectedListOption());
							wipTestJD.loadCurrentWorkUnits();
							wipTestJD.setLocationRelativeTo(getParentFrame()); // ~17A
							wipTestJD.setVisible(true);
						} else {
							String erms = "No WIP Driver selected"; //$NON-NLS-1$
							IGSMessageBox.showOkMB(getParentFrame(), null, erms, null);
						}
					}
				}
			} /* end of good rc */
			if (rc != 0) {
				IGSMessageBox.showOkMB(getParentFrame(), null, errorString, null);
			}
		} catch (Exception e) {
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}

		addMyListeners();
	}

	/**
	 * Displays an <code>MFSWipDriverTestDialog</code> using the specified
	 * <code>wipDriver</code>.
	 * 
	 * @param wipDriver
	 *            the the WIP Driver used to display the
	 *            <code>MFSWipDriverTestDialog</code>
	 */
	private void wipTestRecreate(String wipDriver) {
		try {
			MFSWipDriverTestDialog wipTestJD = new MFSWipDriverTestDialog(getParentFrame(), this, wipDriver);
			wipTestJD.loadCurrentWorkUnits();
			wipTestJD.setLocationRelativeTo(getParentFrame()); // ~17A
			wipTestJD.setVisible(true);
		} catch (Exception e) {
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
	}

	/**
	 * Threaded Call to repeat last transaction call
	 */
	public void repeatTransaction() {
		if (this.cnfgEfficiencyOn && this.pbSourceButton != null) {
			this.repaint();
			Thread t = new Thread() {
				public void run() {
					MFSMenuButton tempButton = pbSourceButton;
					pbSourceButton = null;
					tempButton.doClick();
				}
			};
			t.start();
		}
	}

	/**
	 * Invoked when an action occurs.
	 * 
	 * @param ae
	 *            the <code>ActionEvent</code>
	 */
	public void actionPerformed(ActionEvent ae) {
		final Object source = ae.getSource();

		if (source == this.pbExistingWork) {
			if (this.cnfgEfficiencyOn) // ~42
			{
				this.pbSourceButton = (MFSMenuButton) source;
			}
			existingWork();
		} else if (source == this.pbNewWork) {
			if (this.cnfgEfficiencyOn) // ~42
			{
				this.pbSourceButton = (MFSMenuButton) source;
			}
			newWork();
		} else if (source == this.pbExtFunctions) {
			// ~16C Removed extendedFunctions method
			MFSFrame parent = getParentFrame();
			parent.displayMFSPanel(new MFSSelWrkExtFnctPanel(parent, this));
		} else if (source == this.pbSignoff) {
			signoff(); // ~19C
		} else if (source == this.pbReapply) {
			if (this.cnfgEfficiencyOn) // ~42
			{
				this.pbSourceButton = (MFSMenuButton) source;
			}
			reapply();
		} else if (source == this.pbTearDown) {
			if (this.cnfgEfficiencyOn) // ~42
			{
				this.pbSourceButton = (MFSMenuButton) source;
			}
			teardown();
		} else if (source == this.pbStandAlone) {
			standAlonePrinting();
		} else if (source == this.pbAuth) {
			// ~16C Removed authorization method
			// ~24C Display MFSChangePwdDialog instead of MFSAuthPanel
			MFSFrame parent = getParentFrame();
			MFSChangePwdDialog dialog = new MFSChangePwdDialog(parent, false);
			dialog.setLocationRelativeTo(parent);
			dialog.setVisible(true);
		} else if (source == this.pbUnburn) {
			unburn();
		} else if (source == this.pbDeconfig) {
			if (this.cnfgEfficiencyOn) // ~42
			{
				this.pbSourceButton = (MFSMenuButton) source;
			}
			deconfig();
		} else if (source == this.pbFkit) {
			if (this.cnfgEfficiencyOn) // ~42
			{
				this.pbSourceButton = (MFSMenuButton) source;
			}
			str_fkit();
		} else if (source == this.pbFuncNewWork) {
			pfuncNewWork();
		} else if (source == this.pbPartFunctions) {
			// ~16C Removed partFunctions method
			MFSFrame parent = getParentFrame();
			parent.displayMFSPanel(new MFSSerializedComponentFnctPanel(parent, this));
		} else if (source == this.pbTestWIPDriver) {
			wipTest();
		} else if (source == this.pbReapplyPrep) {
			if (this.cnfgEfficiencyOn) // ~42
			{
				this.pbSourceButton = (MFSMenuButton) source;
			}
			reapplyPrep();
		}
		else if (source == this.pbSapApplyPrep) { // ~55
			if (this.cnfgEfficiencyOn) 
			{
				this.pbSourceButton = (MFSMenuButton) source;
			}
			sapApplyPrep();
		}
		// ~16A New button and method for IPSR35208JM
		else if (source == this.pbCreateBCKeys) {
			createBarcodeForKeys();
		}
		// ~25A New button and method for Entity Merge
		else if (source == this.pbEntityMerge) {
			if (this.cnfgEfficiencyOn) // ~42
			{
				this.pbSourceButton = (MFSMenuButton) source;
			}
			entityMerge();
		}
		// ~31A New Acknowledge Container button
		else if (source == this.pbAckCntr) {
			ackCntr();
		}
		// ~33A Reduction Button
		else if (source == this.pbReduction) {
			reduction();
		}
		// ~40A Build Ahead Buttons
		else if (source == this.pbReleaseBuildAhead) {
			releaseBuildAhead();
		} else if (source == this.pbApplyBuildAhead) {
			applyBuildAhead();
		}
		// ~44A
		else if (source == this.pbOnDemand) {
			onDemandReprint();
		}
		// ~51A
		else if (source == this.pbArchive) {
			archiveWU();
		}
		// ~53A
		else if (source == this.pbPartWeight) {
			partWeight();
		}
		// ~53A
		else if (source == this.pbSAPDeconfig) {
			sapDeconfig();
		}
	}

	/**
	 * Invoked when a key is pressed.
	 * 
	 * @param ke
	 *            the <code>KeyEvent</code>
	 */
	public void keyPressed(KeyEvent ke) {
		final int keyCode = ke.getKeyCode();
		if (keyCode == KeyEvent.VK_ENTER && ke.getSource() instanceof MFSMenuButton) {
			MFSMenuButton button = (MFSMenuButton) ke.getSource();
			button.requestFocusInWindow();
			button.doClick();
		} else if (keyCode == KeyEvent.VK_F1) {
			if (ke.isShiftDown()) {
				this.pbFuncNewWork.requestFocusInWindow();
				this.pbFuncNewWork.doClick();
			}
		} else if (keyCode == KeyEvent.VK_F2) {
			if (ke.isShiftDown()) {
				this.pbPartFunctions.requestFocusInWindow();
				this.pbPartFunctions.doClick();
			} else {
				this.pbAuth.requestFocusInWindow();
				this.pbAuth.doClick();
			}
		} else if (keyCode == KeyEvent.VK_F3) {
			if (ke.isShiftDown()) {
				this.pbTestWIPDriver.requestFocusInWindow();
				this.pbTestWIPDriver.doClick();
			} else {
				this.pbSignoff.requestFocusInWindow();
				this.pbSignoff.doClick();
			}
		} else if (keyCode == KeyEvent.VK_F4) {
			if (ke.isShiftDown()) {
				this.pbReapplyPrep.requestFocusInWindow();
				this.pbReapplyPrep.doClick();
			} else {
				this.pbExistingWork.requestFocusInWindow();
				this.pbExistingWork.doClick();
			}
		} else if (keyCode == KeyEvent.VK_F5) {
			if (ke.isShiftDown()) {
				// ~16A New button for IPSR35208JM
				this.pbCreateBCKeys.requestFocusInWindow();
				this.pbCreateBCKeys.doClick();
			} else {
				this.pbNewWork.requestFocusInWindow();
				this.pbNewWork.doClick();
			}
		} else if (keyCode == KeyEvent.VK_F6) {
			if (ke.isShiftDown()) {
				// ~25A New button for entity merge
				this.pbEntityMerge.requestFocusInWindow();
				this.pbEntityMerge.doClick();
			} else {
				this.pbExtFunctions.requestFocusInWindow();
				this.pbExtFunctions.doClick();
			}
		} else if (keyCode == KeyEvent.VK_F7) {
			if (ke.isShiftDown()) {
				// ~31A New button
				this.pbAckCntr.requestFocusInWindow();
				this.pbAckCntr.doClick();
			} else {
				this.pbReapply.requestFocusInWindow();
				this.pbReapply.doClick();
			}
		} else if (keyCode == KeyEvent.VK_F8) {
			if (ke.isShiftDown()) {
				// ~33A New button
				this.pbReduction.requestFocusInWindow();
				this.pbReduction.doClick();
			} else {
				this.pbTearDown.requestFocusInWindow();
				this.pbTearDown.doClick();
			}
		} else if (keyCode == KeyEvent.VK_F9) {
			if (ke.isShiftDown()) {
				// F21
				// ~40A - Build Ahead Release button
				this.pbReleaseBuildAhead.requestFocusInWindow();
				this.pbReleaseBuildAhead.doClick();
			} else {
				this.pbStandAlone.requestFocusInWindow();
				this.pbStandAlone.doClick();
			}
		} else if (keyCode == KeyEvent.VK_F10) {
			if (ke.isShiftDown()) {
				// F22
				// ~40A - Build Ahead Apply button
				this.pbApplyBuildAhead.requestFocusInWindow();
				this.pbApplyBuildAhead.doClick();
			} else {
				this.pbUnburn.requestFocusInWindow();
				this.pbUnburn.doClick();
			}
		} else if (keyCode == KeyEvent.VK_F11) {
			this.pbDeconfig.requestFocusInWindow();
			this.pbDeconfig.doClick();
		} else if (keyCode == KeyEvent.VK_F12) {
			this.pbFkit.requestFocusInWindow();
			this.pbFkit.doClick();
		}
		// ~44A Control down section
		else if (ke.isControlDown()) {
			if (keyCode == KeyEvent.VK_L) {
				this.pbOnDemand.requestFocusInWindow();
				this.pbOnDemand.doClick();
			} /* ~51A CTRL + N for Archive */
			if (keyCode == KeyEvent.VK_N) {
				this.pbPartWeight.requestFocusInWindow();
				this.pbPartWeight.doClick();
			}
			/* ~53A CTRL + W for Part Weight */
			if (keyCode == KeyEvent.VK_W) {
				this.pbPartWeight.requestFocusInWindow();
				this.pbPartWeight.doClick();
			}
			/* ~54A CTRL + D for CKAD Deconfig */
			if (keyCode == KeyEvent.VK_D) {
				this.pbSAPDeconfig.requestFocusInWindow();
				this.pbSAPDeconfig.doClick();
			}
			if (keyCode == KeyEvent.VK_P) {  //~55
				this.pbSapApplyPrep.requestFocusInWindow();
				this.pbSapApplyPrep.doClick();
			}		
			
		}
	}

	/* ~49A Start: New method showSerialNumberPrompt */
	/**
	 * Calls VLDTSERCOLLECT function of RTV_MFSDTA transaction to determine if
	 * pop up window must be shown or not for the WU being processed
	 *
	 * @return true if VLDTSERCOLLECT returns 1, otherwise returns false.
	 */
	private boolean showWUSerialNumberPrompt(String MCTL) throws MFSTransactionException {
		MFSConfig config = MFSConfig.getInstance();
		VLDT_SERCOL vldtSerCollect = new VLDT_SERCOL(this);

		vldtSerCollect.setInputAPP("MFS");
		vldtSerCollect.setInputPGM("MFSCLIENT");
		vldtSerCollect.setInputUSER(config.get8CharUser());
		vldtSerCollect.setInputCELL(config.get8CharCellType());
		vldtSerCollect.setInputMCTL(MCTL);

		vldtSerCollect.execute();

		int rc = vldtSerCollect.getReturnCode();

		/*
		 * Validate that the transaction executed correctly, otherwise throws an
		 * MFSTransactionException with the corresponding information
		 */
		if (rc != 0) {
			MFSTransactionException ex = new MFSTransactionException("RTV_MFSDTA", "VLDTSERCOLLECT " + MCTL,
					vldtSerCollect.getOutputDATA(), rc);
			throw ex;
		}

		return vldtSerCollect.getOutputCOLLECTSERIAL().equals("1");
	}
	/* ~49A End: New method showSerialNumberPrompt */
}
