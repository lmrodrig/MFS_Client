/* © Copyright IBM Corporation 2003, 2008. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2005-09-29   ~1 31867EM                   -add software liscense label printing for new RSS products
 *                                           -add softwareKeycode method
 * 2006-06-28   ~2 31801JM  R Prechel        -Used ActionJPanel.incrementValue method
 *                                           -Removed unused variables and getBuilderData
 * 2006-08-03   ~3 35789JM  JL. Woodward     -Add new button to print the Prod Pack Label calling the
 *                                            rsspcpp printing method with "SAPRINTING" as the source.
 * 2006-07-14   ~4 			D Fichtinger	 -Add new constructor and general cleanup
 * 2006-09-14   ~5 34987JM  M Barth          -Add new buttons to print the SHIPGROUP,1S,
 *                                            Container,and CNFGNODER labels.
 * 2006-09-26   ~6 35696DF  M Barth          -Correct key listener initialization and
 *                                            removed initConnections procedure
 * 2007-02-20   ~7 34242JR  R Prechel        -Java 5 version
 * 2007-04-02   ~8 38166JM  R Prechel        -Add setLocationRelativeTo for dialogs
 * 2007-07-24   ~9 38768JL  Martha Luna      -Change rsspcpp method by generic pcpplabels method
 * 2008-04-23  ~10 40764EM  Santiago D       -Support KEYCODESLONG long label for new hydra release
 * 2008-05-08  ~11 39568MZ  D Pietrasik      -Add new button for SMRTSERIAL
 * 2008-05-20  ~12 41268MZ  D Pietrasik      -Add new button for CASECONTENT
 * 2008-07-29  ~13 38990JL  Santiago D		 -Pass label trigger source (LBTS) to carriercom printing method
 * 2008-07-31  ~14 39375PP  Santiago D       -Add Country of Origin (CoO) label printing capability
 * 2008-08-06     39375PPa  Santiago D       -Remove PRODPACKSM wu type validation
 * 											 -Remove COOCNTR wu type validation
 * 2009-03-25  ~15 44544GB  Santiago D       -Remove pbMifLbl button
 * 2009-08-20  ~16 41901TL  Santiago D       -Add new buttons for WARRANTYCARD and WEIGHT
 * 2010-02-18  ~17 46810JL  J Mastachi       -Update due to modified parameters in warrantyCard method
 * 2010-02-25  ~18 42558JL  D Mathre	     -Add new button for On Demand label printing
 * 2010-03-13  ~19 42558JL  Santiago SC      -Changes to onDemand method
 * 2010-11-03  ~20 48873JM	Luis M.          -Add new button for Feature on Demand
 * 2015-09-03  ~21 1384186  Andy Williams    -Add Part Weight button
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.panel;

import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.ibm.rchland.mfgapps.client.utils.messagebox.IGSMessageBox;
import com.ibm.rchland.mfgapps.client.utils.xml.IGSXMLTransaction;
import com.ibm.rchland.mfgapps.mfsclient.MFSFrame;
import com.ibm.rchland.mfgapps.mfsclient.MFSMenuButton;
import com.ibm.rchland.mfgapps.mfsclient.MFSMenuPanel;
import com.ibm.rchland.mfgapps.mfsclient.MFSPanel;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSCntrInCntrDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSGenericListDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSKittingOpDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSReportChoiceDialog;
import com.ibm.rchland.mfgapps.mfsclient.struct.MFSComponentListModel;
import com.ibm.rchland.mfgapps.mfsclient.struct.MFSComponentRec;
import com.ibm.rchland.mfgapps.mfsclient.struct.MFSHeaderRec;
import com.ibm.rchland.mfgapps.mfsclient.struct.MFSOnDemandKeyData;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSButtonIterator;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSConstants;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSOnDemand;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSPrintingMethods;
import com.ibm.rchland.mfgapps.mfscommon.MFSConfig;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSComm;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSFixedTransaction;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSTransaction;

/**
 * <code>MFSStandAlonePanel</code> is the <code>MFSMenuPanel</code> used to
 * perform standalone printing.
 * @author The MFS Client Development Team
 */
public class MFSStandAlonePanel
	extends MFSMenuPanel
{
	private static final long serialVersionUID = 1L;
	/** The default screen name of an <code>MFSStandAlonePanel</code>. */
	public static final String SCREEN_NAME = "Printing for Work Unit: ";
	
	/** The MIR (F2) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbMir = MFSMenuButton.createLargeButton("MIR",
			"printF2.gif", null, //$NON-NLS-1$
			"BUTTON,SAPRINTING,MIR"); //$NON-NLS-1$

	/** The Sys PWD (F3) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbSysPWD = MFSMenuButton.createLargeButton("Sys PWD",
			"syskeyF3.gif", null, //$NON-NLS-1$
			"BUTTON,SAPRINTING,SYSTEMPWD"); //$NON-NLS-1$

	/** The Kitting (F4) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbKitting = MFSMenuButton.createLargeButton("Kitting",
			"printF4.gif", null, //$NON-NLS-1$
			"BUTTON,SAPRINTING,KITTING"); //$NON-NLS-1$

	/** The Kitting (F4) <code>MFSMenuButton</code> for Rochester Kitting. */
	private MFSMenuButton pbRochKit = MFSMenuButton.createLargeButton("Kitting",
			"printF4.gif", null, //$NON-NLS-1$
			"BUTTON,SAPRINTING,ROCHKIT"); //$NON-NLS-1$

	/** The Track Sub (F5) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbTrackSub = MFSMenuButton.createLargeButton("Track Sub",
			"printF5.gif", null, //$NON-NLS-1$
			"BUTTON,SAPRINTING,TRACKSUB"); //$NON-NLS-1$

	/** The FFBM (F5) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbFFBM = MFSMenuButton.createLargeButton("FFBM",
			"printF5.gif", null, //$NON-NLS-1$
			"BUTTON,SAPRINTING,FFBM"); //$NON-NLS-1$

	/** The Track+CTY (F6) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbTrackSub2 = MFSMenuButton.createLargeButton("Track+CTY",
			"printF6.gif", null, //$NON-NLS-1$
			"BUTTON,SAPRINTING,TRACKSUB2"); //$NON-NLS-1$

	/** The FFBM+CTY (F6) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbFFBM2 = MFSMenuButton.createLargeButton("FFBM+CTY",
			"printF6.gif", null, //$NON-NLS-1$
			"BUTTON,SAPRINTING,FFBM2"); //$NON-NLS-1$

	/** The Work Unit (F7) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbGenericWu = MFSMenuButton.createLargeButton("Work Unit",
			"printF7.gif", null, null); //$NON-NLS-1$

	/** The FRU (F8) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbRochFru = MFSMenuButton.createLargeButton("FRU",
			"printF8.gif", null, //$NON-NLS-1$
			"BUTTON,SAPRINTING,ROCHFRU"); //$NON-NLS-1$

	/** The FC MES (F9) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbRochMes = MFSMenuButton.createLargeButton("FC MES",
			"printF9.gif", null, //$NON-NLS-1$
			"BUTTON,SAPRINTING,ROCHFCMES"); //$NON-NLS-1$

	/** The Pack List (F10) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbRochPackList = MFSMenuButton.createLargeButton("Pack List",
			"printF10.gif", null, //$NON-NLS-1$
			"BUTTON,SAPRINTING,ROCHPACKLIST"); //$NON-NLS-1$

	//~15C - The shortcut key and image is free to use
	/** The MIF Label (F11) , MifReprintF11.gif <code>MFSMenuButton</code>. */

	/** The Travel (F12) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbTravel = MFSMenuButton.createLargeButton("Travel",
			"printF12.gif", null, //$NON-NLS-1$
			"BUTTON,SAPRINTING,TRAVEL"); //$NON-NLS-1$

	/** The Serial Node (SHFT + F1) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbConfSeri = MFSMenuButton.createLargeButton("Serial Node",
			"printF13.gif", null, //$NON-NLS-1$
			"BUTTON,SAPRINTING,SERIALNODE"); //$NON-NLS-1$

	/** The Prod Sub (SHFT + F2) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbProdSub = MFSMenuButton.createLargeButton("Prod Sub",
			"printF14.gif", null, //$NON-NLS-1$
			"BUTTON,SAPRINTING,PRODSUB"); //$NON-NLS-1$

	/** The Cnfg Node (SHFT + F3) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbConfNode = MFSMenuButton.createLargeButton("Cnfg Node",
			"printF15.gif", null, //$NON-NLS-1$
			"BUTTON,SAPRINTING,CNFGNODE"); //$NON-NLS-1$

	/** The Sys Info (SHFT + F4) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbSystemInfo = MFSMenuButton.createLargeButton("Sys Info",
			"printF16.gif", null, //$NON-NLS-1$
			"BUTTON,SAPRINTING,SYSINFO"); //$NON-NLS-1$

	/** The Adv. Feature (SHFT + F5) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbAdvFeatModel = MFSMenuButton.createLargeButton(
			"Adv. Feature", "advFeatModelF17.gif", null, //$NON-NLS-2$
			"BUTTON,SAPRINTING,ADVFEATMODEL"); //$NON-NLS-1$

	/** The Prod Feat Code (SHFT + F6) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbProdFeatCode = MFSMenuButton.createLargeButton(
			"Prod Feat Code", "prodFeatCodeF18.gif", null, //$NON-NLS-2$
			"BUTTON,SAPRINTING,PRODFEATCODE"); //$NON-NLS-1$

	/** The Loc Chart (SHFT + F7) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbDeconfigLocChart = MFSMenuButton.createLargeButton("Loc Chart",
			"locChartF19.gif", null, //$NON-NLS-1$
			"BUTTON,SAPRINTING,DECONFIGLOC"); //$NON-NLS-1$

	/** The Prod Pack Lrg (SHFT + F8) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbProdPackLarge = MFSMenuButton.createLargeButton(
			"Prod Pack Lrg", "prodPackLargeF20.gif", null, //$NON-NLS-2$
			"BUTTON,SAPRINTING,PRODPACKLRG"); //$NON-NLS-1$

	/** The Prod Pack Sm. (SHFT + F9) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbProdPackSmall = MFSMenuButton.createLargeButton(
			"Prod Pack Sm.", "prodPackSmallF21.gif", null, //$NON-NLS-2$
			"BUTTON,SAPRINTING,PRODPACKSM"); //$NON-NLS-1$

	/** The Mach Index (SHFT + F10) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbMachineIndexCard = MFSMenuButton.createLargeButton(
			"Mach Index", "machineIndexF22.gif", null, //$NON-NLS-2$
			"BUTTON,SAPRINTING,MACHINDEXCARD"); //$NON-NLS-1$

	/** The MES Ship Group (SHFT + F11) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbMESShipGroup = MFSMenuButton.createLargeButton(
			"MES Ship Group", "mesShipGrpF23.gif", null, //$NON-NLS-2$
			"BUTTON,SAPRINTING,MESSHIPGRPLBL"); //$NON-NLS-1$

	/** The Carrier (SHFT + F12) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbCarrier = MFSMenuButton.createLargeButton("Carrier",
			"printF24.gif", null, //$NON-NLS-1$
			"BUTTON,SAPRINTING,CARRIER"); //$NON-NLS-1$

	/** The SPD11SText (CTRL + F1) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbSPD11SText = MFSMenuButton.createLargeButton("SPD11SText",
			"printCTRLF1.gif", null, //$NON-NLS-1$
			"BUTTON,SAPRINTING,SPD11STEXT"); //$NON-NLS-1$

	/** The WU Print (CTRL + F2) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbWUPrint = MFSMenuButton.createLargeButton("WU Print",
			"podkeyCTRLF2.gif", null, //$NON-NLS-1$
			"BUTTON,SAPRINTING,WUPRINT"); //$NON-NLS-1$

	/** The Key Codes (CTRL + F3) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbKeyCodes = MFSMenuButton.createLargeButton("Key Codes",
			"printCTRLF3.gif", null, //$NON-NLS-1$
			"BUTTON,SAPRINTING,KEYCODES"); //$NON-NLS-1$

	/** The CARRIERCOMN (CTRL + F4) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbCarrierComn = MFSMenuButton.createLargeButton(
			"CARRIERCOMN", "printCTRLF4.gif", null, //$NON-NLS-2$
			"BUTTON,SAPRINTING,CARRIERCOMN"); //$NON-NLS-1$

	/** The MAIN ID (CTRL + F5) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbMainID = MFSMenuButton.createLargeButton("MAIN ID",
			"mainIdCTRLF5.gif", null, //$NON-NLS-1$
			"BUTTON,SAPRINTING,MAINID"); //$NON-NLS-1$

	//~1
	/** The SW Keys (CTRL + F6) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbSoftwareKeycode = MFSMenuButton.createLargeButton("SW Keys",
			"swKeysCTRLF6.gif", null, //$NON-NLS-1$
			"BUTTON,SAPRINTING,SFTKEYCODE"); //$NON-NLS-1$

	//~3
	/** The Prod Pack (CTRL + F7) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbProdPack = MFSMenuButton.createLargeButton("Prod Pack",
			"printCTRLF7.gif", null, //$NON-NLS-1$
			"BUTTON,SAPRINTING,PRODPACK"); //$NON-NLS-1$

	//~5A
	/** The ShpGrpLst (CTRL + F8) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbRtvShpGrp = MFSMenuButton.createLargeButton("ShpGrpLst",
			"printCTRLF8.gif", null, //$NON-NLS-1$
			"BUTTON,SAPRINTING,RTVSHPGRP"); //$NON-NLS-1$

	//~5A
	/** The 1S (CTRL + F9) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbMatpmcsn1s = MFSMenuButton.createLargeButton("1S",
			"printCTRLF9.gif", null, //$NON-NLS-1$
			"BUTTON,SAPRINTING,MATPMCSN1S"); //$NON-NLS-1$

	//~5A
	/** The Container (CTRL + F10) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbContainer = MFSMenuButton.createLargeButton("Container",
			"printCTRLF10.gif", null, //$NON-NLS-1$
			"BUTTON,SAPRINTING,CONTAINER"); //$NON-NLS-1$

	//~5A
	/** The CnfgNodeR (CTRL + F11) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbCnfgNodeR = MFSMenuButton.createLargeButton("CnfgNodeR",
			"printCTRLF11.gif", null, //$NON-NLS-1$
			"BUTTON,SAPRINTING,CNFGNODER"); //$NON-NLS-1$
	
	//~11A
	/** The Smart Serial (CTRL + F12) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbSmartSerial = MFSMenuButton.createLargeButton("SmartSerial",
			"printCTRLF12.gif", null,  //$NON-NLS-1$
			"BUTTON,SAPRINTING,SMRTSERIAL"); //$NON-NLS-1$

	//~12A
	/** The Case Content (CTRL + 1) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbCaseContent = MFSMenuButton.createLargeButton("CaseContent",
			"printCTRL1.gif", null,  //$NON-NLS-1$
			"BUTTON,SAPRINTING,CASECONTENT"); //$NON-NLS-1$
	
    //~16A
	/** The Warranty Card label (CTRL + 2) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbWarrCard = MFSMenuButton.createLargeButton("Warranty Card",
			"warranty.gif", null, //$NON-NLS-1$
			"BUTTON,SAPRINTING,WARRANTYCARD"); //$NON-NLS-1$		

	//~16A
	/** The Weight label (CTRL + 3) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbWeightLbl = MFSMenuButton.createLargeButton("Weight Label",
			"weight.gif", null, //$NON-NLS-1$
			"BUTTON,SAPRINTING,WEIGHTLABEL"); //$NON-NLS-1$		
	
	//~14A
	/** The CoO Cntr label (CTRL + P) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbCooCntr = MFSMenuButton.createLargeButton("CoO Cntr",
			"printCTRLP.gif", null, //$NON-NLS-1$
			"BUTTON,SAPRINTING,COOCNTR"); //$NON-NLS-1$
	
	//~18A
	/** The On Demand Label (CTRL + L) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbOnDemand = MFSMenuButton.createLargeButton(
			"On Demand", "printCTRLL.gif", null, //$NON-NLS-2$
			"BUTTON,SAPRINTING,ONDEMANDLBL"); //$NON-NLS-1$

	//~20A
	/** The FOD label (CTRL + 4) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbFoD = MFSMenuButton.createLargeButton("FoD Label",
			"FoD.gif", null, //$NON-NLS-1$
			"BUTTON,SAPRINTING,FODLABEL"); //$NON-NLS-1$
	
	//~21A
	/** The Part Weight (CTRL + W) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbPartWeight = MFSMenuButton.createLargeButton("Part Weight",
			"partweightCTRLW.gif", null, //$NON-NLS-1$
			"BUTTON,SAPRINTING,PARTWEIGHT"); //$NON-NLS-1$
	
	/** The work unit mctl value <code>JLabel</code>. */
	private JLabel lblWrkUnit = createLabel(""); //$NON-NLS-1$

	/** The {@link MFSHeaderRec#getMfgn()} value <code>JLabel</code>. */
	private JLabel lblMfgn = createLabel(""); //$NON-NLS-1$

	/** The {@link MFSHeaderRec#getOrno()} value <code>JLabel</code>. */
	private JLabel lblOrno = createLabel(""); //$NON-NLS-1$

	/** The {@link MFSHeaderRec#getCtry()} value <code>JLabel</code>. */
	private JLabel lblCtry = createLabel(""); //$NON-NLS-1$

	/** The {@link MFSHeaderRec#getBrof()} value <code>JLabel</code>. */
	private JLabel lblBrof = createLabel(""); //$NON-NLS-1$

	/** The {@link MFSHeaderRec#getProd()} value <code>JLabel</code>. */
	private JLabel lblProd = createLabel(""); //$NON-NLS-1$

	/** The {@link MFSHeaderRec#getMspi()} + {@link MFSHeaderRec#getMcsn()} value <code>JLabel</code>. */
	private JLabel lblMspiMcsn = createLabel(""); //$NON-NLS-1$

	/** The {@link MFSHeaderRec#getMmdl()} value <code>JLabel</code>. */
	private JLabel lblMmdl = createLabel(""); //$NON-NLS-1$

	/** The {@link MFSHeaderRec#getNmbr()} value <code>JLabel</code>. */
	private JLabel lblNmbr = createLabel(""); //$NON-NLS-1$

	/** The {@link MFSHeaderRec#getSchd()} value <code>JLabel</code>. */
	private JLabel lblSchd = createLabel(""); //$NON-NLS-1$

	/** The <code>MFSHeaderRec</code> for the work unit. */
	private MFSHeaderRec fieldHeaderRec = new MFSHeaderRec();

	/** The mctl for the work unit. */
	private String fieldCurrMctl;
	
	/**
	 * Constructs a new <code>MFSStandAlonePanel</code>.
	 * @param parent the <code>MFSFrame</code> used to display this panel
	 * @param source the <code>MFSPanel</code> that caused this panel to be displayed
	 */
	public MFSStandAlonePanel(MFSFrame parent, MFSPanel source)
	{
		super(parent, source, SCREEN_NAME, 6, 5);
		this.fieldButtonIterator = createMenuButtonIterator();
		createLayout();
		configureButtons();
		addMyListeners(); //~6
	}
	
	//~7A New method
	/**
	 * Creates an <code>MFSButtonIterator</code> for this panel's
	 * <code>MFSMenuButton</code>s.
	 * @return the <code>MFSButtonIterator</code>
	 */
	private MFSButtonIterator createMenuButtonIterator()
	{
		//The constructor param is the number of buttons
		MFSButtonIterator result = new MFSButtonIterator(42);  //~18C
		result.add(this.pbMir);
		result.add(this.pbSysPWD);
		result.add(this.pbKitting);
		result.add(this.pbRochKit);
		result.add(this.pbTrackSub);
		result.add(this.pbFFBM);
		result.add(this.pbTrackSub2);
		result.add(this.pbFFBM2);
		result.add(this.pbGenericWu);
		result.add(this.pbRochFru);
		result.add(this.pbRochMes);
		result.add(this.pbRochPackList);
		result.add(this.pbTravel);
		result.add(this.pbConfSeri);
		result.add(this.pbProdSub);
		result.add(this.pbConfNode);
		result.add(this.pbSystemInfo);
		result.add(this.pbAdvFeatModel);
		result.add(this.pbProdFeatCode);
		result.add(this.pbDeconfigLocChart);
		result.add(this.pbProdPackLarge);
		result.add(this.pbProdPackSmall);
		result.add(this.pbMachineIndexCard);
		result.add(this.pbMESShipGroup);
		result.add(this.pbCarrier);
		result.add(this.pbSPD11SText);
		result.add(this.pbWUPrint);
		result.add(this.pbKeyCodes);
		result.add(this.pbCarrierComn);
		result.add(this.pbMainID);
		result.add(this.pbSoftwareKeycode);
		result.add(this.pbProdPack);
		result.add(this.pbRtvShpGrp);
		result.add(this.pbMatpmcsn1s);
		result.add(this.pbContainer);
		result.add(this.pbCnfgNodeR);
		result.add(this.pbSmartSerial);  // ~11A
		result.add(this.pbCaseContent);  // ~12A
		result.add(this.pbCooCntr); // ~14A
		result.add(this.pbWarrCard); // ~16A
		result.add(this.pbWeightLbl); // ~16A
		result.add(this.pbOnDemand);//~18A
		result.add(this.pbFoD);     //~20A
		result.add(this.pbPartWeight);     //~21A
		return result;
	}

	//~7A New method
	/**
	 * Creates a new <code>JLabel</code>.
	 * @param text the text for the <code>JLabel</code>
	 * @return the new <code>JLabel</code>
	 */
	private JLabel createLabel(String text)
	{
		JLabel result = new JLabel(text);
		result.setFont(MFSConstants.MEDIUM_DIALOG_FONT);
		result.setHorizontalAlignment(SwingConstants.CENTER);
		result.setForeground(MFSConstants.PRIMARY_FOREGROUND_COLOR);
		return result;
	}

	//~7A New method
	/** Adds this panel's <code>Component</code>s to the layout. */
	protected void createLayout()
	{
		JLabel mctlLabel = createLabel("MCTL");
		JPanel topHeaderPanel = new JPanel(new GridLayout(2, 1));
		topHeaderPanel.setBackground(MFSConstants.PRIMARY_BACKGROUND_COLOR);
		topHeaderPanel.add(mctlLabel);
		topHeaderPanel.add(this.lblWrkUnit);

		JLabel sysNumLabel = createLabel("Sys Num");
		JLabel orderLabel = createLabel("Order");
		JLabel countryLabel = createLabel("Country");
		JLabel brofLabel = createLabel("Brof");
		JLabel prodIDLabel = createLabel("Prod ID");
		JLabel machNumLabel = createLabel("Mach Num");
		JLabel modelLabel = createLabel("Model");
		JLabel operLabel = createLabel("Oper");
		JLabel shipDateLabel = createLabel("Ship Date");

		JPanel bottomHeaderPanel = new JPanel(new GridLayout(2, 9));
		bottomHeaderPanel.setBackground(MFSConstants.PRIMARY_BACKGROUND_COLOR);
		bottomHeaderPanel.add(sysNumLabel);
		bottomHeaderPanel.add(orderLabel);
		bottomHeaderPanel.add(countryLabel);
		bottomHeaderPanel.add(brofLabel);
		bottomHeaderPanel.add(prodIDLabel);
		bottomHeaderPanel.add(machNumLabel);
		bottomHeaderPanel.add(modelLabel);
		bottomHeaderPanel.add(operLabel);
		bottomHeaderPanel.add(shipDateLabel);

		bottomHeaderPanel.add(this.lblMfgn);
		bottomHeaderPanel.add(this.lblOrno);
		bottomHeaderPanel.add(this.lblCtry);
		bottomHeaderPanel.add(this.lblBrof);
		bottomHeaderPanel.add(this.lblProd);
		bottomHeaderPanel.add(this.lblMspiMcsn);
		bottomHeaderPanel.add(this.lblMmdl);
		bottomHeaderPanel.add(this.lblNmbr);
		bottomHeaderPanel.add(this.lblSchd);

		JPanel borderPanel = new JPanel(new GridBagLayout());
		borderPanel.setBackground(MFSConstants.PRIMARY_BACKGROUND_COLOR);
		borderPanel.setBorder(createTitledBorder());

		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.NORTH, GridBagConstraints.NONE, 
				new Insets(0, 10, 10, 10), 0, 0);

		borderPanel.add(topHeaderPanel, gbc);

		gbc.gridy++;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		borderPanel.add(bottomHeaderPanel, gbc);

		gbc.gridy++;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.fill = GridBagConstraints.NONE;
		borderPanel.add(this.buttonPanel, gbc);

		this.add(borderPanel);
	}
	
	//~7C Redone based on super.configureButtons
	//which uses MFSMenuButton.isActive()
	/** {@inheritDoc} */
	protected void configureButtons()
	{
		MFSConfig config = MFSConfig.getInstance();
		if (config.containsConfigEntry("BUTTON,SAPRINTING,ROCHWU") //$NON-NLS-1$
				|| config.containsConfigEntry("BUTTON,SAPRINTING,SMALLWU") //$NON-NLS-1$
				|| config.containsConfigEntry("BUTTON,SAPRINTING,CTRLNUMBR")) //$NON-NLS-1$
		{
			this.pbGenericWu.setActive(true);
		}
		
		super.configureButtons();
	}

	//~7A New method
	/** {@inheritDoc} */
	public String getMFSPanelTitle()
	{
		return SCREEN_NAME + this.fieldCurrMctl;
	}

	//~7A New method
	/**
	 * Loads the work unit information used to populate this panel's
	 * <code>MFSHeaderRec</code> and sets the text of the <code>JLabel</code>s
	 * that display the work unit information.
	 * @param data the data used to populate the <code>MFSHeaderRec</code>
	 * @param mctl the work unit mctl
	 */
	public void loadWorkUnitInfo(String data, String mctl)
	{
		final int inc = 64;
		this.fieldHeaderRec.load_H1(data.substring(0, inc));
		this.fieldHeaderRec.load_H2(data.substring(inc, 2 * inc));
		this.fieldHeaderRec.load_H3(data.substring(2 * inc, 3 * inc));
		this.fieldHeaderRec.load_H4(data.substring(3 * inc, 4 * inc));

		this.fieldCurrMctl = mctl;

		this.lblMfgn.setText(this.fieldHeaderRec.getMfgn());
		this.lblOrno.setText(this.fieldHeaderRec.getOrno());
		this.lblCtry.setText(this.fieldHeaderRec.getCtry());
		this.lblProd.setText(this.fieldHeaderRec.getProd());
		this.lblMspiMcsn.setText(this.fieldHeaderRec.getMspi() + this.fieldHeaderRec.getMcsn());
		this.lblMmdl.setText(this.fieldHeaderRec.getMmdl());
		this.lblNmbr.setText(this.fieldHeaderRec.getNmbr());
		this.lblSchd.setText(this.fieldHeaderRec.getSchd());
		this.lblBrof.setText(this.fieldHeaderRec.getBrof());
		this.lblWrkUnit.setText(mctl);
	}

	/** Invoked when {@link #pbAdvFeatModel} is selected. */
	private void advFeatModel()
	{
		try
		{
			MFSPrintingMethods.advFeatModel(this.fieldHeaderRec.getMfgn(),
					this.fieldHeaderRec.getIdss(), this.fieldHeaderRec.getOrno(),
					this.fieldCurrMctl, 1, getParentFrame());
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
	}

	/** Invoked when {@link #pbCarrier} is selected. */
	private void carrier()
	{
		try
		{
			MFSPrintingMethods.carrier(this.fieldHeaderRec.getMctl(), 1, getParentFrame());
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
	}

	// ~13C
	/** Invoked when {@link #pbCarrierComn} is selected. */
	private void carriercomn()
	{
		try
		{
			MFSPrintingMethods.carriercomn(this.fieldHeaderRec.getMctl(), "SAPRINTING", 1, getParentFrame());
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
	}

	/** Invoked when {@link #pbCaseContent} is selected. */
	private void caseContent()
	{
		try
		{
			MFSPrintingMethods.caseContent(null, this.fieldCurrMctl, 1, getParentFrame());
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
	}

	/** Invoked when {@link #pbConfNode} is selected. */
	private void confNode()
	{
		try
		{
			getActionIndicator().startAction("Printing Configured Node Label, Please Wait...");
			this.update(this.getGraphics());

			MFSPrintingMethods.confnode(this.fieldCurrMctl, 1, getParentFrame());

			getActionIndicator().stopAction();
		}
		catch (Exception e)
		{
			getActionIndicator().stopAction();
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
	}
	
	//~5A
	/** Invoked when {@link #pbCnfgNodeR} is selected. */
	private void cnfgnoder()
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
			getActionIndicator().startAction("Printing Serial Node Label, Please Wait...");
			this.update(this.getGraphics());

			MFSPrintingMethods.prodsub(this.fieldHeaderRec.getPrln(), "J", //$NON-NLS-1$
					this.fieldCurrMctl, 1, getParentFrame());

			getActionIndicator().stopAction();
		}
		catch (Exception e)
		{
			getActionIndicator().stopAction();
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
	}

	//~5A
	/** Invoked when {@link #pbContainer} is selected. */
	private void container()
	{
		try
		{
			removeMyListeners();

			MFSTransaction rtv_cinc = new MFSFixedTransaction("RTV_CINC  " + this.fieldCurrMctl); //$NON-NLS-1$
			rtv_cinc.setActionMessage("Retrieving List of Containers, Please Wait...");
			MFSComm.getInstance().execute(rtv_cinc, this);

			if (rtv_cinc.getReturnCode() == 0)
			{
				MFSCntrInCntrDialog cincDialog = new MFSCntrInCntrDialog(
						getParentFrame(), this.fieldHeaderRec);
				cincDialog.disableButton("Add"); //$NON-NLS-1$
				cincDialog.disableButton("Delete"); //$NON-NLS-1$
				cincDialog.disableButton("New"); //$NON-NLS-1$
				cincDialog.disableButton("Undo"); //$NON-NLS-1$
				cincDialog.loadTree(rtv_cinc.getOutput());
				cincDialog.setLocationRelativeTo(getParentFrame()); //~8A
				cincDialog.setVisible(true);
			}
			else
			{
				IGSMessageBox.showOkMB(this, null, rtv_cinc.getErms(), null);
			}
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
	
	//~14A Contry of Origin container label
	/** Invoked when {@link #pbCoO} is selected. */
	private void cooCntr()
	{
		try
		{
			this.removeMyListeners();
			
			int rc = 0;
			
			final MFSConfig config = MFSConfig.getInstance();
			final String BLANK_CNTR = "          ";			
			final int CNTR_LENGTH = 10;

			/* Call to RTV_CNTR tx */
			String data = "RTV_CNTR  " + this.fieldCurrMctl + "L" + //$NON-NLS-1$ //$NON-NLS-2$
					BLANK_CNTR + config.get8CharUser();

			MFSTransaction rtv_cntr = new MFSFixedTransaction(data);
			rtv_cntr.setActionMessage("Retrieving List of Containers, Please Wait...");
			
			MFSComm.getInstance().execute(rtv_cntr, this);
			
			rc = rtv_cntr.getReturnCode();

			if (rc == 0)
			{			
				data = rtv_cntr.getOutput();
				
				if(data.trim().length() > 0)
				{
					/* Creates the Container Dialog */
					MFSGenericListDialog cntrDialog = new MFSGenericListDialog(getParentFrame(),
							"Containers", "Select a Container", "Print", "Done");
					
					/* Fills the Container List in the Container Dialog */
					cntrDialog.initializeDefaultList();
					
					while(data.length() != 0 && data.length() >= CNTR_LENGTH)
					{
						String cntr = data.substring(0,CNTR_LENGTH);
						cntrDialog.addListItem(cntr);
						data = data.substring(CNTR_LENGTH);
					}
					
					cntrDialog.setSizeSmall();
					cntrDialog.setDefaultSelection("FIRST");
					
					do
					{
						cntrDialog.setProceedSelected(false);
						cntrDialog.setLocationRelativeTo(getParentFrame());
						cntrDialog.setVisible(true);
						
						if(cntrDialog.getProceedSelected())
						{
							/* Call RTVCOOCNTR tx */
							IGSXMLTransaction rtvCooCntr = new IGSXMLTransaction("RTVCOOCNTR"); //$NON-NLS-1$
							rtvCooCntr.setActionMessage("Determining if all parts are installed...");
							rtvCooCntr.startDocument();
							rtvCooCntr.addElement("MCTL", this.fieldCurrMctl);
							rtvCooCntr.addElement("CNTR", cntrDialog.getSelectedListOption()); //$NON-NLS-1$								
							rtvCooCntr.addElement("MODE", "IDSP"); //$NON-NLS-1$ //$NON-NLS-2$
							rtvCooCntr.endDocument();	
							
							MFSComm.getInstance().execute(rtvCooCntr, this);
							
							rc = rtvCooCntr.getReturnCode(); //~1A			
							
							if(rc == 0)
							{
								String complete = rtvCooCntr.getNextElement("COMPLETE");
								
								/* if all parts installed/complete */
								if(complete.compareToIgnoreCase("NO") == 0)
								{
									if(IGSMessageBox.showYesNoMB(getParentFrame(),"Warning",
											"Not all components are installed. Do you still want to print?", null))
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
					IGSMessageBox.showOkMB(getParentFrame(), null, "No Containers were found.", null);					
				}
			} // if RTV_CNTR no error
			else
			{
				IGSMessageBox.showOkMB(getParentFrame(), null, rtv_cntr.getErms(), null);					
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

	
    //~16A
	/** Invoked when {@link #pbWarrCard} is selected. */
	private void warrantyCard()
	{
		try
		{
			StringBuffer lbldta = new StringBuffer("<MCTL>"); //~17A
			lbldta.append(this.fieldCurrMctl); //~17A
			lbldta.append("</MCTL>"); //~17A
			MFSPrintingMethods.warrantyCard(lbldta.toString(), "SAPRINTING", "", 1, "", getParentFrame()); //~17C
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
	}
	
    //~16A
	/** Invoked when {@link #pbWeightLbl} is selected. */
	private void weightLabel()
	{
		try
		{
			MFSPrintingMethods.weightLabel(this.fieldCurrMctl,
					this.fieldHeaderRec.getCntr(), "SAPRINTING", 1, getParentFrame());
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
	}
	//~20A
	/** Invoked when {@link #pbFodLabel} is selected. */
	private void fodLabel()
	{
		try
		{
			MFSPrintingMethods.fodLabel(this.fieldHeaderRec.getMfgn(),this.fieldHeaderRec.getIdss(),
					                    this.fieldCurrMctl,"SAPRINTING",1,getParentFrame()); 
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
	}
	//~21A
	/** Invoked when {@link #pbFodLabel} is selected. */
	private void partWeight()
	{
		try
		{
			
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
					this.fieldCurrMctl, 1, getParentFrame());
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
	}

	/** Invoked when {@link #pbFFBM} is selected. */
	private void ffbm()
	{
		try
		{
			getActionIndicator().startAction("Printing FFBM label, Please Wait...");
			this.update(this.getGraphics());

			MFSPrintingMethods.rtvffbm(this.fieldCurrMctl, 1, getParentFrame());

			getActionIndicator().stopAction();
		}
		catch (Exception e)
		{
			getActionIndicator().stopAction();
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
	}

	/** Invoked when {@link #pbFFBM2} is selected. */
	private void ffbm2()
	{
		try
		{
			getActionIndicator().startAction("Printing FFBM label, Please Wait...");
			this.update(this.getGraphics());

			MFSPrintingMethods.rtvffbm2(this.fieldCurrMctl, 1, getParentFrame());

			getActionIndicator().stopAction();
		}
		catch (Exception e)
		{
			getActionIndicator().stopAction();
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
	}

	/** Invoked when {@link #pbGenericWu} is selected. */
	private void genericWU()
	{
		try
		{
			MFSConfig config = MFSConfig.getInstance();
			if (config.containsConfigEntry("SMALLWU")) //$NON-NLS-1$
			{
				//print the small work unit label
				getActionIndicator().startAction("Printing Small Work Unit, Please Wait...");
				this.update(getGraphics());
				MFSPrintingMethods.smallwu(this.fieldCurrMctl, 1, getParentFrame());
			}
			if (config.containsConfigEntry("ROCHWU")) //$NON-NLS-1$
			{
				/* Perform the rochWU method. */
				getActionIndicator().startAction("Printing Work Unit, Please Wait...");
				this.update(getGraphics());
				MFSPrintingMethods.rochwu(this.fieldCurrMctl, this.fieldHeaderRec.getMfgn(),
						this.fieldHeaderRec.getIdss(), 1, getParentFrame());
			}
			if (config.containsConfigEntry("CTRLNUMBR")) //$NON-NLS-1$
			{
				/* Perform the ctrlnumbr method. */
				getActionIndicator().startAction("Printing Control Number Label, Please Wait...");
				this.update(getGraphics());
				MFSPrintingMethods.ctrlnumbr(this.fieldCurrMctl, 1, getParentFrame());
			}

			getActionIndicator().stopAction();
		}
		catch (Exception e)
		{
			getActionIndicator().stopAction();
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
			removeMyListeners();

			String input = "RTV_MO    " + this.fieldCurrMctl; //$NON-NLS-1$
			MFSTransaction rtv_mo = new MFSFixedTransaction(input);
			rtv_mo.setActionMessage("Retrieving List of Operations, Please Wait...");
			MFSComm.getInstance().execute(rtv_mo, this);

			if (rtv_mo.getReturnCode() != 0)
			{
				IGSMessageBox.showOkMB(this, null, rtv_mo.getErms(), null);
			}
			else
			{
				MFSKittingOpDialog kittOpD = new MFSKittingOpDialog(getParentFrame());
				kittOpD.loadKittOpListModel(rtv_mo.getOutput());
				kittOpD.setLocationRelativeTo(getParentFrame()); //~8A
				kittOpD.setVisible(true);

				if (kittOpD.getPressedPrint())
				{
					final MFSConfig config = MFSConfig.getInstance();
					int index = 0;
					while (index < kittOpD.getKittOpListModelSize())
					{
						String listData = (String) kittOpD.getKittOpListModelElementAt(index);
						String type = listData.substring(13, 14);
						if (!type.equals(" ")) //$NON-NLS-1$
						{
							/* set Cursor to waiting */
							getParentFrame().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
							String nmbr = listData.substring(0, 4);
							if (config.containsConfigEntry("POKKIT")) //$NON-NLS-1$
							{
								MFSPrintingMethods.pokkit(this.fieldCurrMctl, nmbr, type,
										config.get8CharCellType(), 1, getParentFrame());
							}
							if (config.containsConfigEntry("POKKIT2")) //$NON-NLS-1$
							{
								MFSPrintingMethods.pokkit2(this.fieldCurrMctl, nmbr,
										type, config.get8CharCellType(), 1, getParentFrame());
							}
						}
						index++;
					}
					getParentFrame().setCursor(Cursor.getDefaultCursor());
				}
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
					this.fieldCurrMctl, 1, getParentFrame());
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
	}

	/** Invoked when {@link #pbMainID} is selected. */
	private void mainID()
	{
		try
		{
			MFSPrintingMethods.mainid(this.fieldCurrMctl, 1, getParentFrame());
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
	}

	//~5A
	/** Invoked when {@link #pbMatpmcsn1s} is selected. */
	private void matpmcsn1s()
	{
		try
		{
			String saPrint1s = MFSConfig.getInstance().getConfigValue("SAPRINT1S"); //$NON-NLS-1$
			if (!saPrint1s.equals(MFSConfig.NOT_FOUND))
			{
				removeMyListeners();

				int rc = 0;
				int qty = 1;
				int index = 0;
				MFSComponentRec next;

				StringBuffer data = new StringBuffer();
				data.append("RTV_IP    "); //$NON-NLS-1$
				data.append(this.fieldHeaderRec.getMfgn());
				data.append(this.fieldHeaderRec.getIdss());
				data.append(this.fieldCurrMctl);
				data.append(this.fieldHeaderRec.getOlev());
				data.append("J"); //$NON-NLS-1$

				MFSTransaction rtv_ip = new MFSFixedTransaction(data.toString());
				rtv_ip.setActionMessage("Retrieving Installed Parts, Please Wait...");
				MFSComm.getInstance().execute(rtv_ip, this);
				rc = rtv_ip.getReturnCode();

				if (rc == 0)
				{
					boolean noPartsFound = true;

					/* load the list model */
					MFSComponentListModel myListModel = new MFSComponentListModel();
					myListModel.loadListModel(rtv_ip.getOutput(), new MFSHeaderRec());

					// Loop thru components; check for AMSI flag = blank or zero
					while (index < myListModel.size())
					{
						next = myListModel.getComponentRecAt(index);

						if (!next.getAmsi().equals(" ") & !next.getAmsi().equals("0")) //$NON-NLS-1$ //$NON-NLS-2$
						{
							if (saPrint1s.equalsIgnoreCase("*ALL") //$NON-NLS-1$
									|| saPrint1s.equalsIgnoreCase(next.getInpn()))
							{
								MFSPrintingMethods.matpmcsn1s(next.getMatp(), 
										next.getMmdl(), next.getMspi(), next.getMcsn(), qty,
										getParentFrame());
								noPartsFound = false;
							}
						}
						index++;
					}
					/* No parts found to be printed nor *ALL defined */
					if (noPartsFound)
					{
						StringBuffer erms = new StringBuffer();
						erms.append("Specified Part Number, ");
						erms.append(saPrint1s);
						erms.append(", Not Found with AMSI = 1");
						IGSMessageBox.showOkMB(this, null, erms.toString(), null);
					}
				}
				else
				{
					/* (rc!=0) display error to user */
					IGSMessageBox.showOkMB(this, null, rtv_ip.getErms(), null);
				}
			}
			else
			{
				String erms = "Missing config entry for 1S Stand Alone Printing(SAPRINT1S)";
				IGSMessageBox.showOkMB(this, null, erms, null);
			}
		}
		catch (Exception e)
		{
			String erms = "Unable to print 1S label, exception occurred: ";
			IGSMessageBox.showOkMB(this, null, erms, e);
		}
		finally
		{
			addMyListeners();
		}

	}

	/** Invoked when {@link #pbMESShipGroup} is selected. */
	private void mesShipGrpLbl()
	{
		try
		{
			MFSPrintingMethods.mesShipGrpLbl(this.fieldHeaderRec.getMfgn(),
					this.fieldHeaderRec.getIdss(), this.fieldHeaderRec.getOrno(),
					this.fieldCurrMctl, 1, getParentFrame());
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
	}

	//~18A //~19C
	/** Invoked when {@link #pbOnDemand} is selected. */
	private void onDemandReprint()
	{
		try
		{
			this.removeMyListeners();
			
			// Create key data for label printing
			MFSOnDemandKeyData odKeyData = new MFSOnDemandKeyData();
			odKeyData.setTriggerSource("SAPRINTING");
			odKeyData.setTriggerKey("*NONE");
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
	
	/** Invoked when {@link #pbMir} is selected. */
	private void mir()
	{
		try
		{
			getActionIndicator().startAction("Printing Mir Label, Please Wait...");
			this.update(this.getGraphics());

			String celltype = MFSConfig.getInstance().get8CharCellType();
			String part = "00000" + this.fieldHeaderRec.getPrln().substring(0, 7); //$NON-NLS-1$
			MFSPrintingMethods.mir(celltype, part, this.fieldHeaderRec.getMctl(), 1,
					getParentFrame());

			getActionIndicator().stopAction();
		}
		catch (Exception e)
		{
			getActionIndicator().stopAction();
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
					this.fieldCurrMctl, 1, getParentFrame());
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
	}

	//~3A
	/** Invoked when {@link #pbProdPack} is selected. */
	private void prodpack()
	{
		try
		{
			MFSPrintingMethods.pcpplabels(this.fieldHeaderRec.getCntr(), this.fieldCurrMctl,
					"SAPRINTING", 1,"NONE", getParentFrame()); //$NON-NLS-1$ ~9

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
					this.fieldCurrMctl, 1, getParentFrame());
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
					this.fieldCurrMctl, 1, getParentFrame());			
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
	}

	/** Invoked when {@link #pbProdSub} is selected. */
	private void prodSub()
	{
		try
		{
			MFSConfig config = MFSConfig.getInstance();
			if (config.containsConfigEntry("PRODSUB")) //$NON-NLS-1$
			{
				getActionIndicator().startAction("Printing ProdSub, Please Wait...");
				this.update(getGraphics());

				MFSPrintingMethods.prodsub(this.fieldHeaderRec.getPrln(), "J", //$NON-NLS-1$
						this.fieldCurrMctl, 1, getParentFrame());

				getActionIndicator().stopAction();
			}
			if (config.containsConfigEntry("L3CACHE")) //$NON-NLS-1$
			{
				getActionIndicator().startAction("Printing L3 Cache, Please Wait...");
				this.update(getGraphics());

				MFSPrintingMethods.l3cache(this.fieldHeaderRec.getPrln(), "J", //$NON-NLS-1$
						this.fieldCurrMctl, 1, getParentFrame());

				getActionIndicator().stopAction();
			}
		}
		catch (Exception e)
		{
			getActionIndicator().stopAction();
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
	}

	/** Invoked when {@link #pbRochMes} is selected. */
	private void rochFCMes()
	{
		try
		{
			String type = ""; //$NON-NLS-1$
			MFSReportChoiceDialog rcd = new MFSReportChoiceDialog(getParentFrame());
			rcd.setLocationRelativeTo(getParentFrame()); //~8A
			rcd.setVisible(true);

			if (rcd.getPressedPrint())
			{
				type = rcd.getSelectionType();
			}

			MFSPrintingMethods.rchfcmes(this.fieldCurrMctl, type, 1, getParentFrame());
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
	}

	/** Invoked when {@link #pbRochFru} is selected. */
	private void rochFru()
	{
		try
		{
			getActionIndicator().startAction("Printing Fru label, Please Wait...");
			this.update(this.getGraphics());

			MFSPrintingMethods.rochfru("FRU     ", //$NON-NLS-1$
					"00000" + this.fieldHeaderRec.getPrln().trim(), //$NON-NLS-1$ 
					this.fieldCurrMctl, "J", 1, //$NON-NLS-1$
					getParentFrame());

			getActionIndicator().stopAction();
		}
		catch (Exception e)
		{
			getActionIndicator().stopAction();
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

			String input = "RTV_MO    " + this.fieldCurrMctl; //$NON-NLS-1$
			MFSTransaction rtv_mo = new MFSFixedTransaction(input);
			rtv_mo.setActionMessage("Retrieving List of Operations, Please Wait...");
			MFSComm.getInstance().execute(rtv_mo, this);
			rc = rtv_mo.getReturnCode();

			if (rc == 0)
			{
				MFSKittingOpDialog kittOpD = new MFSKittingOpDialog(getParentFrame());
				kittOpD.loadKittOpListModel(rtv_mo.getOutput());
				kittOpD.setLocationRelativeTo(getParentFrame()); //~8A
				kittOpD.setVisible(true);

				if (kittOpD.getPressedPrint())
				{
					final MFSConfig config = MFSConfig.getInstance();
					int index = 0;
					while (rc == 0 && index < kittOpD.getKittOpListModelSize())
					{
						String listData = (String) kittOpD.getKittOpListModelElementAt(index);
						String type = listData.substring(13, 14);
						String nmbr = listData.substring(0, 4);

						if (!type.equals(" ")) //$NON-NLS-1$
						{
							/* now get the kittdflt entry for that operation */
							String default_value = config.getConfigValue("KITTDFLT," + nmbr); //$NON-NLS-1$
							if (!default_value.equals(MFSConfig.NOT_FOUND))
							{
								// look in the kittdflt entry for a 3rd comma,
								// if found check for the loc or remote
								String loc_or_remote = ""; //$NON-NLS-1$
								int first_comma = default_value.indexOf(","); //$NON-NLS-1$
								int loc_index = default_value.indexOf(",", first_comma + 1); //$NON-NLS-1$
								if (loc_index == -1)
								{
									/* if no loc found, use L to print locally */
									loc_or_remote = "L"; //$NON-NLS-1$
								}
								else
								{
									loc_or_remote = default_value.substring(loc_index + 1, loc_index + 2);
								}

								if (loc_or_remote.equals("L") //$NON-NLS-1$
										|| loc_or_remote.equals("B")) //$NON-NLS-1$
								{
									/* set Cursor to waiting */
									getParentFrame().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
									MFSPrintingMethods.rchkit(this.fieldCurrMctl, nmbr,
											type, 1, getParentFrame());
								}
								if (loc_or_remote.equals("R") //$NON-NLS-1$
										|| loc_or_remote.equals("B")) //$NON-NLS-1$
								{
									/* trigger kitting2 on host */
									/* set Cursor to waiting */
									getParentFrame().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

									/* start the UPDT_PQ transaction thread */
									StringBuffer holder = new StringBuffer();
									holder.append("UPDT_PQ   "); //$NON-NLS-1$
									holder.append(astrix);
									holder.append("       "); //$NON-NLS-1$
									holder.append("    "); //$NON-NLS-1$
									holder.append(this.fieldCurrMctl);
									holder.append("        "); //$NON-NLS-1$
									holder.append(nmbr);
									holder.append("00001"); //$NON-NLS-1$
									holder.append("KITTING2 "); //$NON-NLS-1$
									holder.append(config.get8CharCell());
									holder.append(config.getConfigValue("USER").concat("           ").substring(0,10)); //$NON-NLS-1$ //$NON-NLS-2$
									holder.append(type);
									holder.append(oprm);

									MFSTransaction updt_pq = new MFSFixedTransaction(holder.toString());
									updt_pq.setActionMessage("Updating FCSPPQ10, Please Wait...");
									MFSComm.getInstance().execute(updt_pq, this);
									rc = updt_pq.getReturnCode();
									if (rc != 0)
									{
										errorString = updt_pq.getErms();
									}
									getParentFrame().setCursor(Cursor.getDefaultCursor());
								} /* end loc_or_remote = 'R' or 'B' */
							} /* end check for not found - kittdflt */
							else
							{
								StringBuffer erms = new StringBuffer();
								erms.append("KITTDFLT not found for operation: ");
								erms.append(nmbr);
								erms.append(" in the configuration file");
								IGSMessageBox.showOkMB(this, null, erms.toString(), null);
							}

						} /* end non-blank type */
						index++;
					}
					getParentFrame().setCursor(java.awt.Cursor.getDefaultCursor());
				}
			}
			else
			{
				errorString = rtv_mo.getErms();
			}

			if (rc != 0)
			{
				IGSMessageBox.showOkMB(this, null, errorString, null);
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
			String type = ""; //$NON-NLS-1$
			MFSReportChoiceDialog rcd = new MFSReportChoiceDialog(getParentFrame());
			rcd.setLocationRelativeTo(getParentFrame()); //~8A
			rcd.setVisible(true);

			if (rcd.getPressedPrint())
			{
				type = rcd.getSelectionType();
			}

			MFSPrintingMethods.rchshpgrp(this.fieldCurrMctl, type, 1, getParentFrame());
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
	}

	//~5A
	/** Invoked when {@link #pbRtvShpGrp} is selected. */
	private void rtvShpGrp()
	{
		try
		{
			String rtvShpGrp = MFSConfig.getInstance().getConfigValue("BUTTON,SAPRINTING,RTVSHPGRP"); //$NON-NLS-1$
			if (rtvShpGrp != null && !rtvShpGrp.equals("")) //$NON-NLS-1$
			{
				MFSPrintingMethods.rtvshpgrp(this.fieldHeaderRec.getMctl(), 
						rtvShpGrp.substring(0, 1), 1, getParentFrame());
			}
			else
			{
				String erms = "Missing config value (S=Summary,D=Detail,B=Both)";
				IGSMessageBox.showOkMB(this, null, erms, null);
			}
		}
		catch (Exception e)
		{
			String erms = "Unable to print RTVSHPGRP sheet, exception occurred: ";
			IGSMessageBox.showOkMB(this, null, erms, e);
		}
	}

	//~11A
	/** Invoked when {@link #pbSmartSerial} is selected. */
	private void smartSerial()
	{
		try
		{
			removeMyListeners();

			IGSXMLTransaction rtv_seqn = new IGSXMLTransaction("RTV_SEQN"); //$NON-NLS-1$
			rtv_seqn.startDocument();
			rtv_seqn.addElement("MFGN", this.fieldHeaderRec.getMfgn()); //$NON-NLS-1$
			rtv_seqn.addElement("IDSS", this.fieldHeaderRec.getIdss()); //$NON-NLS-1$
			rtv_seqn.addElement("MCTL", this.fieldCurrMctl); //$NON-NLS-1$
			rtv_seqn.addElement("CHECK", "ISS"); //$NON-NLS-1$ //$NON-NLS-2$
			rtv_seqn.setActionMessage("Retrieving Smart Serial number...");
			rtv_seqn.endDocument();
			rtv_seqn.run();

			if (rtv_seqn.getReturnCode() == 0)
			{
				String isISS = rtv_seqn.getFirstElement("CHKD"); //$NON-NLS-1$
				if (isISS == null)
				{
					IGSMessageBox.showOkMB(this, null, "Unable to determine if ISS order", null);
				}
				else if (isISS.equals("N")) //$NON-NLS-1$
				{
					IGSMessageBox.showOkMB(this, null, "MCSN order, no Smart Serial found", null);
				}
				else
				{
					String smrts = rtv_seqn.getFirstElement("SEQN"); //$NON-NLS-1$
					MFSPrintingMethods.smartSerial(smrts, 1, getParentFrame());
				}
			}
			else
			{
				IGSMessageBox.showOkMB(this, null, rtv_seqn.getErms(), null);
			}
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

	//~1
	/** Invoked when {@link #pbSoftwareKeycode} is selected. */
	private void softwareKeycode()
	{
		try
		{
			MFSPrintingMethods.softKeycode(this.fieldHeaderRec.getMctl(), "R", 1, getParentFrame());
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
	}

	/** Invoked when {@link #pbSPD11SText} is selected. */
	private void spd11stext()
	{
		try
		{
			MFSPrintingMethods.spd11stext(this.fieldHeaderRec.getMctl(), 1,
					getParentFrame());
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
	}

	/** Invoked when {@link #pbSysPWD} is selected. */
	private void sysPwd()
	{
		try
		{
			getActionIndicator().startAction("Printing System Password, Please Wait...");
			this.update(this.getGraphics());

			MFSPrintingMethods.systempassword(this.fieldHeaderRec.getMfgn(),
					this.fieldHeaderRec.getIdss(), this.fieldHeaderRec.getMctl(),
					this.fieldHeaderRec.getCmat(), this.fieldHeaderRec.getMmdl(), 1,
					getParentFrame());

			getActionIndicator().stopAction();
		}
		catch (Exception e)
		{
			getActionIndicator().stopAction();
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
	}

	/** Invoked when {@link #pbSystemInfo} is selected. */
	private void systemInfo()
	{
		try
		{
			getActionIndicator().startAction("Printing System Info Label, Please Wait...");
			this.update(this.getGraphics());

			MFSPrintingMethods.sysinfo(this.fieldCurrMctl, 1, getParentFrame());

			getActionIndicator().stopAction();
		}
		catch (Exception e)
		{
			getActionIndicator().stopAction();
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
	}

	/** Invoked when {@link #pbTrackSub} is selected. */
	private void trackSub()
	{
		try
		{
			getActionIndicator().startAction("Printing TrackSub, Please Wait...");
			this.update(this.getGraphics());

			MFSPrintingMethods.tracksub(this.fieldHeaderRec.getPrln(), "J", //$NON-NLS-1$
					this.fieldCurrMctl, 1, getParentFrame());

			getActionIndicator().stopAction();
		}
		catch (Exception e)
		{
			getActionIndicator().stopAction();
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
	}

	/** Invoked when {@link #pbTrackSub2} is selected. */
	private void trackSub2()
	{
		try
		{
			getActionIndicator().startAction("Printing TrackSub, Please Wait...");
			this.update(this.getGraphics());

			MFSPrintingMethods.tracksub2(this.fieldHeaderRec.getPrln(), "J", //$NON-NLS-1$
					this.fieldCurrMctl, 1, getParentFrame());

			getActionIndicator().stopAction();
		}
		catch (Exception e)
		{
			getActionIndicator().stopAction();
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
	}

	/** Invoked when {@link #pbTravel} is selected. */
	private void travel()
	{
		try
		{
			getActionIndicator().startAction("Printing Travel Report, Please Wait...");
			this.update(this.getGraphics());

			MFSPrintingMethods.rtvtravel(this.fieldCurrMctl, 1, getParentFrame());

			getActionIndicator().stopAction();
		}
		catch (Exception e)
		{
			getActionIndicator().stopAction();
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
	}

	/** Invoked when {@link #pbWUPrint} is selected. */
	private void wuprint()
	{
		try
		{
			MFSPrintingMethods.wuprint(this.fieldHeaderRec.getMctl(), 1, getParentFrame());
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
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
			if (source == this.pbMir)
			{
				mir();
			}
			else if (source == this.pbSysPWD)
			{
				sysPwd();
			}
			else if (source == this.pbKitting)
			{
				kitting();
			}
			else if (source == this.pbRochKit)
			{
				rochKit();
			}
			else if (source == this.pbTrackSub)
			{
				trackSub();
			}
			else if (source == this.pbFFBM)
			{
				ffbm();
			}
			else if (source == this.pbTrackSub2)
			{
				trackSub2();
			}
			else if (source == this.pbFFBM2)
			{
				ffbm2();
			}
			else if (source == this.pbGenericWu)
			{
				genericWU();
			}
			else if (source == this.pbRochFru)
			{
				rochFru();
			}
			else if (source == this.pbRochMes)
			{
				rochFCMes();
			}
			else if (source == this.pbRochPackList)
			{
				rochPackList();
			}
			else if (source == this.pbTravel)
			{
				travel();
			}
			else if (source == this.pbConfSeri)
			{
				confseri();
			}
			else if (source == this.pbProdSub)
			{
				prodSub();
			}
			else if (source == this.pbConfNode)
			{
				confNode();
			}
			else if (source == this.pbSystemInfo)
			{
				systemInfo();
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
			else if (source == this.pbCarrier)
			{
				carrier();
			}
			else if (source == this.pbSPD11SText)
			{
				spd11stext();
			}
			else if (source == this.pbWUPrint)
			{
				wuprint();
			}
			else if (source == this.pbKeyCodes)
			{
				keycodes();
			}
			else if (source == this.pbCarrierComn)
			{
				carriercomn();
			}
			else if (source == this.pbMainID)
			{
				mainID();
			}
			else if (source == this.pbSoftwareKeycode) //~1
			{
				softwareKeycode();
			}
			else if (source == this.pbProdPack) //~3
			{
				prodpack();
			}
			else if (source == this.pbRtvShpGrp) //~5A
			{
				rtvShpGrp();
			}
			else if (source == this.pbMatpmcsn1s) //~5A
			{
				matpmcsn1s();
			}
			else if (source == this.pbContainer) //~5A
			{
				container();
			}
			else if (source == this.pbCnfgNodeR) //~5A
			{
				cnfgnoder();
			}
			else if (source == this.pbSmartSerial) // ~11A
			{
				smartSerial();
			}
			else if (source == this.pbCaseContent) // ~12A
			{
				caseContent();
			}
			else if (source == this.pbCooCntr) // ~14A
			{
				cooCntr();
			}
			else if (source == this.pbWarrCard) // ~16A
			{
				warrantyCard();
			}
			else if (source == this.pbWeightLbl) // ~16A
			{
				weightLabel();
			}
			else if (source == this.pbOnDemand) // ~18A
			{
				onDemandReprint();
			}
			else if (source == this.pbFoD) // ~20A
			{
				fodLabel();
			}
			else if (source == this.pbPartWeight) // ~21A
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
		else if (keyCode == KeyEvent.VK_ENTER && ke.getSource() instanceof JButton)
		{
			JButton button = (JButton) ke.getSource();
			button.requestFocusInWindow();
			button.doClick();
		}
		/* F1 section */
		else if (keyCode == KeyEvent.VK_F1)
		{
			if (ke.isShiftDown())
			{
				this.pbConfSeri.requestFocusInWindow();
				this.pbConfSeri.doClick();
			}
			else if (ke.isControlDown())
			{
				this.pbSPD11SText.requestFocusInWindow();
				this.pbSPD11SText.doClick();
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
				this.pbWUPrint.requestFocusInWindow();
				this.pbWUPrint.doClick();
			}
			else
			{
				this.pbMir.requestFocusInWindow();
				this.pbMir.doClick();
			}
		}
		/* F3 section */
		else if (keyCode == KeyEvent.VK_F3)
		{
			if (ke.isShiftDown())
			{
				this.pbConfNode.requestFocusInWindow();
				this.pbConfNode.doClick();
			}
			else if (ke.isControlDown())
			{
				this.pbKeyCodes.requestFocusInWindow();
				this.pbKeyCodes.doClick();
			}
			else
			{
				this.pbSysPWD.requestFocusInWindow();
				this.pbSysPWD.doClick();
			}
		}
		/* F4 section */
		else if (keyCode == KeyEvent.VK_F4)
		{
			if (ke.isShiftDown())
			{
				this.pbSystemInfo.requestFocusInWindow();
				this.pbSystemInfo.doClick();
			}
			else if (ke.isControlDown())
			{
				this.pbCarrierComn.requestFocusInWindow();
				this.pbCarrierComn.doClick();
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
				this.pbAdvFeatModel.requestFocusInWindow();
				this.pbAdvFeatModel.doClick();
			}
			else if (ke.isControlDown())
			{
				this.pbMainID.requestFocusInWindow();
				this.pbMainID.doClick();
			}
			else
			{
				if (this.pbTrackSub.isEnabled())
				{
					this.pbTrackSub.requestFocusInWindow();
					this.pbTrackSub.doClick();
				}
				else
				{
					this.pbFFBM.requestFocusInWindow();
					this.pbFFBM.doClick();
				}
			}
		}
		/* F6 section */
		else if (keyCode == KeyEvent.VK_F6)
		{
			if (ke.isShiftDown())
			{
				this.pbProdFeatCode.requestFocusInWindow();
				this.pbProdFeatCode.doClick();
			}
			else if (ke.isControlDown())
			{
				this.pbSoftwareKeycode.requestFocusInWindow();
				this.pbSoftwareKeycode.doClick();
			}
			else
			{
				if (this.pbTrackSub2.isEnabled())
				{
					this.pbTrackSub2.requestFocusInWindow();
					this.pbTrackSub2.doClick();
				}
				else
				{
					this.pbFFBM2.requestFocusInWindow();
					this.pbFFBM2.doClick();
				}
			}
		}
		/* F7 section */
		else if (keyCode == KeyEvent.VK_F7)
		{
			if (ke.isShiftDown())
			{
				this.pbDeconfigLocChart.requestFocusInWindow();
				this.pbDeconfigLocChart.doClick();
			}
			else if (ke.isControlDown())
			{
				this.pbProdPack.requestFocusInWindow();
				this.pbProdPack.doClick();
			}
			else
			{
				this.pbGenericWu.requestFocusInWindow();
				this.pbGenericWu.doClick();
			}
		}
		/* F8 section */
		else if (keyCode == KeyEvent.VK_F8)
		{
			if (ke.isShiftDown())
			{
				this.pbProdPackLarge.requestFocusInWindow();
				this.pbProdPackLarge.doClick();
			}
			else if (ke.isControlDown())
			{
				this.pbRtvShpGrp.requestFocusInWindow();
				this.pbRtvShpGrp.doClick();
			}
			else
			{
				this.pbRochFru.requestFocusInWindow();
				this.pbRochFru.doClick();
			}
		}
		/* F9 section */
		else if (keyCode == KeyEvent.VK_F9)
		{
			if (ke.isShiftDown())
			{
				this.pbProdPackSmall.requestFocusInWindow();
				this.pbProdPackSmall.doClick();
			}
			else if (ke.isControlDown())
			{
				this.pbMatpmcsn1s.requestFocusInWindow();
				this.pbMatpmcsn1s.doClick();
			}
			else
			{
				this.pbRochMes.requestFocusInWindow();
				this.pbRochMes.doClick();
			}
		}
		/* F10 section */
		else if (keyCode == KeyEvent.VK_F10)
		{
			if (ke.isShiftDown())
			{
				this.pbMachineIndexCard.requestFocusInWindow();
				this.pbMachineIndexCard.doClick();
			}
			else if (ke.isControlDown())
			{
				this.pbContainer.requestFocusInWindow();
				this.pbContainer.doClick();
			}
			else
			{
				this.pbRochPackList.requestFocusInWindow();
				this.pbRochPackList.doClick();
			}
		}
		/* F11 section */
		else if (keyCode == KeyEvent.VK_F11)
		{
			if (ke.isShiftDown())
			{
				this.pbMESShipGroup.requestFocusInWindow();
				this.pbMESShipGroup.doClick();
			}
			else if (ke.isControlDown())
			{
				this.pbCnfgNodeR.requestFocusInWindow();
				this.pbCnfgNodeR.doClick();
			}
		}
		/* F12 section */
		else if (keyCode == KeyEvent.VK_F12)
		{
			if (ke.isShiftDown())
			{
				this.pbCarrier.requestFocusInWindow();
				this.pbCarrier.doClick();
			}
			else if (ke.isControlDown())  // ~11A
			{
				this.pbSmartSerial.requestFocusInWindow();
				this.pbSmartSerial.doClick();
			}
			else
			{
				this.pbTravel.requestFocusInWindow();
				this.pbTravel.doClick();
			}
		}
		/* 1 section */
		else if (keyCode == KeyEvent.VK_1)  // ~12A
		{
			if (ke.isControlDown())  // ~12A
			{
				this.pbCaseContent.requestFocusInWindow();
				this.pbCaseContent.doClick();
			}
		}
		else if (ke.isControlDown()) // ~14A
		{
			/* CTRL P section */
			if (keyCode == KeyEvent.VK_P)
			{
				//~14A
				this.pbCooCntr.requestFocusInWindow();
				this.pbCooCntr.doClick();
			}	
			/* CTRL L section */
			else if (keyCode == KeyEvent.VK_L) //~19A
			{
				this.pbOnDemand.requestFocusInWindow();
				this.pbOnDemand.doClick();
			}
			/* CTRL W section  */
			else if (keyCode == KeyEvent.VK_W) //~21A
			{
				this.pbPartWeight.requestFocusInWindow();
				this.pbPartWeight.doClick();
			}
			/* CTRL 2 section */  
			else if (keyCode == KeyEvent.VK_2) //~16A
			{
				this.pbWarrCard.requestFocusInWindow();
				this.pbWarrCard.doClick();
			}
			/* CTRL 3 section */  
			else if (keyCode == KeyEvent.VK_3) //~16A
			{
				this.pbWeightLbl.requestFocusInWindow();
				this.pbWeightLbl.doClick();
			}
			/* CTRL 4 section */  
			else if (keyCode == KeyEvent.VK_4) //~20A
			{
				this.pbFoD.requestFocusInWindow();
				this.pbFoD.doClick();
			}
		}
	}
}
