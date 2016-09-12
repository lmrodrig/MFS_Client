/* @ Copyright IBM Corporation 2002, 2015. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 12/09/02  |    |21751RB  |Andy Williams  |-Added partFunctions() method
 *           |    |         |               |-Added check for PARTFUNCTIONS config to configButtons()
 *           |    |         |               |-Added part and serial fields as well as corresponding getter and setter methods.
 *           |    |         |               |-Added getPNSN method.
 * 12/01/04  |    |29309AT  |Tou Lee Moua   |-Changed showPNSNErrorList()
 * 2006-04-30|  @3|34647EM  |Toribioh       |-Change Input capacity for JTextFieldLen
 *           |    |         |               | and validate inuput size and Barcode Rules
 *           |    |         |               | in WWNNDialog class
 * 2006-05-08|  ~4|34647EM  |DFICHT         |-PNRI not needed in WWNNJDialog constructor
 * 2006-06-09|  ~5|         |R Prechel      |-PartInstructionJPanel constructor change
 *           |    |         |               |-Removed unused variables, methods, and imports
 *           |    |         |               |-Implemented PartInstructionDisplayer
 * 2006-06-12|  ~6|34414JJ  |JL. Woodward   |-New method updatePartContainers, call UPDT_CRCB trx
 *           |    |         |               |-New button "New Container"
 *           |    |         |               |-Cleanup unused variables.
 * 2006-06-21|  ~7|30377PT  |A. Williams    |-Add sending of MALC/MILC/PRLN to the XML string for UPDT_ICNT
 * 2006-08-10|  ~8|35887JM  |JL Woodward    |-When creating a new container, do not send the
 *           |    |         |               | newly created cntr, instead, send the previously assigned one.
 * 2006-08-14|  ~9|35904JM  |JL Woodward    |-Send COOC as well to updt_Crcb server trx.
 * 2006-08-18| ~10|35958JM  |M. Barth       |-Save myComm data before Rebrand and restore data into
 *           |    |         |               | myComm after Rebrand has completed.
 * 2006-08-24| ~11|34222BP  |VH Avila       |-Delete the updtViewInstPartsString() calls
 *           |    |         |               | the variable was deleted from Component_Rec class and
 *           |    |         |               | set the focus on the TFInput object
 * 2006-09-19| ~12|35696DF  |A Williams     |-New constructor.
 * 2006-10-10| ~13|36523JM  |M. Barth       |-Add reprint capability of downbin parts.
 * 2006-10-20| ~14|35927JM  |R Prechel      |-Client lock up workaround
 *           |    |         |               |-Removed ConnEtoMx methods
 *           |    |         |               |-Removed unused constructor and main method
 *           |    |         |               |-Consume F7 key presses
 * 2006-10-27| ~15|35927JM  |R Prechel      |-Enhance log messages
 *           |    |         |               |-Inherit from MFSJPanel
 * 2007-03-13| ~16|34242JR  |R Prechel      |-Java 5 version
 *           |    |         |               |-Remove PTR35927JM code (PTR37428JL)
 * 2007-04-02| ~17|38166JM  |R Prechel      |-Add setLocationRelativeTo for dialogs
 * 2007-04-09| ~18|38176JM  |R Prechel      |-Fix MFS Client hang
 * 2007-06-29| ~19|27794JR  |Toribio Hdez.	|-Add RemoveAllNonSerialized parts button
 * 2007-07-23| ~20|38768JL  |Martha Luna    |-Add these(SYSX,SPD,SYSZ,IPSYS) new config checks at end work unit and
 *           |    |         |         	    | change rsspcpp method by generic pcpplabels method
 * 2007-10-18| ~21|39955JR  |R Prechel      | -Fix Remove All Non Serialized button
 * 2007-10-23| ~22|40262JR  |Toribio Hdez.  |-Check if is not Non Part Instruction before getting
 *           |    |         |               | the part and serial from the Component Rec
 * 2008-01-14|    |39782JM	|Martha Luna	|-Change name of updtMultilineDisplayString to updtIRDisplayString
 * 2008-02-20| ~23|37616JL  |R Prechel      |-Change button images.
 * 2008-04-03| ~24|37616JL  |R Prechel      |-Update container weight/dimensions
 * 2008-04-07| ~25|37616JL  |D Pietrasik    |-add user to rtv_cntr
 * 2008-05-20| ~26|41268MZ  |D Pietrasik    |-print caseContent if configured in end_wu
 * 2008-05-20| ~27|41674JM  |Santiago SC    |-Use PackCode insted of DimCode for Weights and Dims, Acknowledge
 * 2008-07-29| ~28|38990JL  |Santiago SC    |-Pass label trigger source (LBTS) to carriercom printing method
 * 2008-07-31| ~29|39375PP  |Santiago SC    |-Add Country of Origin (CoO) trigger at end_wu
 * 2008-08-01| ~30|42330JM  |Santiago SC    |-Remove MFSAcknowledgeDialog for updateWeightandDims
 * 2008-08-06| ~31|39375PPa |Santiago SC    |-Send blanks for container in end_wu/carriercomn  print method
 *           |    |         |               |-Remove WUTYPE validaton for CoO Cntr Label
 * 2008-08-18| ~32|42581AP  |Santiago SC    |-Use CARRIERCOMNEND instead of CARRIERCOMN config entry
 * 2008-12-17| ~33|43541DK  |Santiago SC    |-Need to prevent CSNI of L parts from being removed or reworked (CSC)
 * 2009-03-06| ~34|41303JL  |Toribio H.     |-Change updt_wusn call to be by IGSXMLTransaction
 * 2009-04-08| ~35|40039JL  |VH Avila Rocha |-Create new methods to handle the new MASSKITINSTALL logic
 * 2009-06-03| ~36|43813MZ  |Christopher O  |-Add a new View Collection Data Button
 * 2009-06-30| ~37|43269JL  |Jaime G.       |-Add a new loadId parameter to the RTV_DASDP transaction. 
 * 2009-07-15| ~38|41330JL  |Sayde Alcantara|-if configured CASECONTENTMEF, print label
 * 2009-08-20| ~39|41901TL  |Santiago SC    |-Add trigger for Warranty/Weight Labels
 * 2009-11-06| ~40|46742PH  |Edgar Mercado  |-Allow printing of PRODPACK label for MES orders when END_WU.
 * 2010-03-03| ~41|47596MZ  |Toribio H.	&	|-Efficiency changes
 * 			 |	  |			|D Kloepping    |   do not retreive suspension codes, default qual code value
 * 			 |	  |			|				|   moved software_validaiton() to MFSSelectWorkPanel()
 * 2010-02-18| ~42|46810JL  |J Mastachi     |-Update due to modified parameters in warrantyCard method
 * 2010-03-16| ~43|42558JL  |Santiago SC    |-New onDemandTrigger,onDemandReprint methods
 * 2010-08-31| ~44|46704EM  |Edgar V        |-Fix containsNmbrPrlnFlagEntry calls to handle string return
 * 2010-11-01  ~45 49513JM	Toribio H.   	 -Make RTV_IRCD Cacheable 
 * 2010-11-22| ~46|48749JL  |Santiago SC    |-Hazmat labeling
 * 2010-11-28| ~47|49722AZ  |Edgar V        |-Print TopLvlCntrs for MES orders
 * 2012-08-24| ~48|RCQ00215583| VH Avila    |-Add validation to know if we have to call RTV_DASDP or RTV_DASDPI transaction
 * 2015-09-09| ~49|1387927  | Luis M Rguez  |-Stop Rework action for BA Parts
 * 2015-09-15| ~50|1371023  | Luis M Rguez  |-Add call to Part Weight Dialog 
 * 2015-11-10| ~51|1421142  | Miguel Rivas  |-CDE Defer issue in MFS Client Direct Work screen
 * 2016-03-15| ~52|1485772  | VH Avila      |-Losing cursor when extraClog logic when re-display dialog with Java 8
 *******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.panel;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.ListModel;

import mfsxml.MISSING_XML_TAG_EXCEPTION;
import mfsxml.MfsXMLDocument;
import mfsxml.MfsXMLParser;
import SmartChipVPD.DasdWrap;

import com.ibm.rchland.mfgapps.client.utils.io.IGSFileUtils;
import com.ibm.rchland.mfgapps.client.utils.layout.IGSGridLayout;
import com.ibm.rchland.mfgapps.client.utils.messagebox.IGSMessageBox;
import com.ibm.rchland.mfgapps.client.utils.xml.IGSXMLDocument;
import com.ibm.rchland.mfgapps.client.utils.xml.IGSXMLTransaction;
import com.ibm.rchland.mfgapps.mfsclient.MFSFrame;
import com.ibm.rchland.mfgapps.mfsclient.MFSInstructionsPanel;
import com.ibm.rchland.mfgapps.mfsclient.MFSMenuButton;
import com.ibm.rchland.mfgapps.mfsclient.MFSPanel;
import com.ibm.rchland.mfgapps.mfsclient.MFSPartInstructionJPanel;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSAcknowledgeInstructionDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSCntrDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSCntrInCntrDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSErrorMsgListDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSFkitUserIDCellDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSGenericListDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSIQDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSLocatePartDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSMultiPartWeightDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSOperationsDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSPNSNDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSPartNumDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSRebrandDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSRejectedPartTagDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSRemNonSerTDDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSRemoveMultPartDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSShowPartinErrorDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSSuspendCodeDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSTextInputDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSVpdValidateDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSVrfyFabSerialDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSWWNNDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSWeightDimDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.log.MFSExtraClogPartDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.log.MFSExtraPartDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.log.MFSLogPartDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.log.MFSPartDataCollectDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.rework.MFSRwkDialog;
import com.ibm.rchland.mfgapps.mfsclient.struct.MFSComponentListModel;
import com.ibm.rchland.mfgapps.mfsclient.struct.MFSComponentRec;
import com.ibm.rchland.mfgapps.mfsclient.struct.MFSHeaderRec;
import com.ibm.rchland.mfgapps.mfsclient.struct.MFSInstructionRec;
import com.ibm.rchland.mfgapps.mfsclient.struct.MFSIntStringPair;
import com.ibm.rchland.mfgapps.mfsclient.struct.MFSOnDemandKeyData;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSButtonIterator;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSCP500Comparator;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSComponentSearch;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSComponentSearchCriteria;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSConstants;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSContainerUtils;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSDetermineLabel;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSHazmatLabeling;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSOnDemand;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSPrintingMethods;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSRework;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSTransactionUtils;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSVPDProc;
import com.ibm.rchland.mfgapps.mfscommon.MFSConfig;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSComm;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSFixedTransaction;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSTransaction;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSXmlTransaction;
import com.ibm.rchland.mfgapps.mfscommon.comm.trx.RTV_IQ;
import com.ibm.rchland.mfgapps.mfscommon.exception.MFSException;
import common.DasdComm;

/**
 * <code>MFSDirectWorkPanel</code> is the <code>MFSInstructionsPanel</code>
 * used to view the <code>MFSPartInstructionJPanel</code>s for building a
 * work unit and for FKIT.
 * @author The MFS Client Development Team
 */

@SuppressWarnings("serial")
public class MFSDirectWorkPanel
	extends MFSInstructionsPanel
	implements MouseListener
{
	/** The default screen name of an <code>MFSDirectWorkPanel</code>. */
	public static final String SCREEN_NAME = "Direct Work"; //~16A //$NON-NLS-1$

	/** The "FKIT" screen name of an <code>MFSDirectWorkPanel</code>. */
	public static final String FKIT_SCREEN_NAME = "FKIT"; //~16A //$NON-NLS-1$

	/** for position based transactions */
	private static final String BLANK_CNTR = "          "; //$NON-NLS-1$
	
	/** The empty <code>String</code>. */
	protected static final String EMPTY_STRING = ""; //$NON-NLS-1$

	/** The Complete Check Complete value. */
	public static final int LT_COMPLETE_CHECK_COMPLETE = 0; 
	
	/** The Complete Check Remove value. */
	public static final int LT_COMPLETE_CHECK_REMOVE = 1; 

	/** The Complete Check Mandatory Instruction Not Complete value. */
	public static final int LT_COMPLETE_CHECK_MANDATORY = 2;
	
	//~23C Change button images from DWFx.gif to smFx.gif
	/** The Search Log (F1) <code>MFSMenuButton</code>. */
	public MFSMenuButton pbSrchLog = MFSMenuButton.createSmallButton("SrchLog",  //~33 //$NON-NLS-1$
			"smF1.gif", "Search Log", false); //$NON-NLS-1$ //$NON-NLS-2$

	/** The Add Part (F2) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbAdd = MFSMenuButton.createSmallButton("Add P/N", //$NON-NLS-1$
			"smF2.gif", "Add Part", false); //$NON-NLS-1$ //$NON-NLS-2$

	/** The End This Operation (F3) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbEnd = MFSMenuButton.createSmallButton("End Job", //$NON-NLS-1$
			"smF3.gif", "End This Operation", false); //$NON-NLS-1$ //$NON-NLS-2$

	/** The Automatic Logging (F4) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbAutoLog = MFSMenuButton.createSmallButton("AutoLog", //$NON-NLS-1$
			"smF4.gif", "Automatic Logging", false); //$NON-NLS-1$ //$NON-NLS-2$

	/** The Extended Functions (F5) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbExtFunc = MFSMenuButton.createSmallButton("Ext Func", //$NON-NLS-1$
			"smF5.gif", "Extended Functions", false); //$NON-NLS-1$ //$NON-NLS-2$

	/** The Rework Part (F6) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbRework = MFSMenuButton.createSmallButton("Rework", //$NON-NLS-1$
			"smF6.gif", "Rework Part", false); //$NON-NLS-1$ //$NON-NLS-2$

	/** The Suspend this Operation (F7) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbSuspend = MFSMenuButton.createSmallButton("Suspend", //$NON-NLS-1$
			"smF7.gif", "Suspend this Operation", false); //$NON-NLS-1$ //$NON-NLS-2$

	/** The View Installed Parts (F8) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbViewIns = MFSMenuButton.createSmallButton("View Ins", //$NON-NLS-1$
			"smF8.gif", "View Installed Parts", false); //$NON-NLS-1$ //$NON-NLS-2$

	/** The Container Function (F9) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbCntr = MFSMenuButton.createSmallButton("Contain", //$NON-NLS-1$
			"smF9.gif", "Container Function", false); //$NON-NLS-1$ //$NON-NLS-2$

	/** The Burn VPD Card (F9) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbBurn = MFSMenuButton.createSmallButton("Burn", //$NON-NLS-1$
			"smF9.gif", "Burn VPD Card", false); //$NON-NLS-1$ //$NON-NLS-2$

	/** The Personalize DASD (F9) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbDasdPersonalize = MFSMenuButton.createSmallButton("DASD Per", //$NON-NLS-1$
			"smF9.gif", "Personalize DASD", false); //$NON-NLS-1$ //$NON-NLS-2$

	/** The Search Remove (F9) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbSearchRem = MFSMenuButton.createSmallButton("Srch Rm", //$NON-NLS-1$
			"smF9.gif", "Search Remove", false); //$NON-NLS-1$ //$NON-NLS-2$

	/** The Move Work Unit (F10) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbMove = MFSMenuButton.createSmallButton("Move", //$NON-NLS-1$
			"smF10.gif", "Move Work Unit", false); //$NON-NLS-1$ //$NON-NLS-2$

	/** The Remove Part (F11) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbRemPart = MFSMenuButton.createSmallButton("Rem P/N", //$NON-NLS-1$
			"smF11.gif", "Remove Part", false); //$NON-NLS-1$ //$NON-NLS-2$

	/** The Container in Container (F12) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbCInC = MFSMenuButton.createSmallButton("C in C", //$NON-NLS-1$
			"smF12.gif", "Container in Container", false); //$NON-NLS-1$ //$NON-NLS-2$

	/** The Remove Multiple Parts (F12) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbRmvParts = MFSMenuButton.createSmallButton("RmvParts", //$NON-NLS-1$
			"smF12.gif", "Remove Multiple Parts", false); //$NON-NLS-1$ //$NON-NLS-2$

	/** The Split Part in Multiple Rows (SHFT + F1) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbSplit = MFSMenuButton.createSmallButton("Split P/N", //$NON-NLS-1$
			"smF13.gif", "Split Part in Multiple Rows", false); //$NON-NLS-1$ //$NON-NLS-2$

	/** The Short this Part (SHFT + F2) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbShort = MFSMenuButton.createSmallButton("Short", //$NON-NLS-1$
			"smF14.gif", "Short this Part", false); //$NON-NLS-1$ //$NON-NLS-2$

	/** The Unburn VPD Card (SHFT + F3) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbUnburn = MFSMenuButton.createSmallButton("Unburn", //$NON-NLS-1$
			"smF15.gif", "Unburn VPD Card", false); //$NON-NLS-1$ //$NON-NLS-2$

	/** The Unpersonalize DASD (SHFT + F3) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbDasdUnpersonalize = MFSMenuButton.createSmallButton("Unper.", //$NON-NLS-1$
			"smF15.gif", "Unpersonalize DASD", false); //$NON-NLS-1$ //$NON-NLS-2$

	/** The Reprint NCM Tag (SHFT + F4) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbReprintNCMTag = MFSMenuButton.createSmallButton("NCM Tag", //$NON-NLS-1$
			"smF16.gif", "Reprint NCM Tag", false); //$NON-NLS-1$ //$NON-NLS-2$

	/** The Extra Shipping Instruction Part (SHFT + F5) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbExtra = MFSMenuButton.createSmallButton("Extra", //$NON-NLS-1$
			"smF17.gif", "Extra Shipping Instruction Part", false); //$NON-NLS-1$ //$NON-NLS-2$ 

	/** The Extra Clog Part (SHFT + F5) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbExtraClogPart = MFSMenuButton.createSmallButton("Extra", //$NON-NLS-1$
			"smF17.gif", "Extra Clog Part", false); //$NON-NLS-1$ //$NON-NLS-2$

	/** The Reset Interposer Count (SHFT + F6) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbResetInterposer = MFSMenuButton.createSmallButton(
			"Reset Int.", "smF18.gif", "Reset Interposer Count", false);  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$

	/** The Add Debug or Golden Part to Kit (SHFT + F6) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbDebugGolden = MFSMenuButton.createSmallButton("DBG/GLD", //$NON-NLS-1$
			"smF18.gif", "Add Debug or Golden Part to Kit", false); //$NON-NLS-1$ //$NON-NLS-2$

	/** The Skip Part (SHFT + F7) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbSkipPart = MFSMenuButton.createSmallButton("Skip Part", //$NON-NLS-1$
			"smF19.gif", "Ignore this Part this time", false); //$NON-NLS-1$ //$NON-NLS-2$

	/** The Verify Pending Parts (SHFT + F7) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbVerify = MFSMenuButton.createSmallButton("Verify", //$NON-NLS-1$
			"smF19.gif", "Verify Pending Parts", false); //$NON-NLS-1$ //$NON-NLS-2$

	/** The Set Location (SHFT + F8) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbEditLoc = MFSMenuButton.createSmallButton("LOC", //$NON-NLS-1$
			"smF20.gif", "Set Location", false); //$NON-NLS-1$ //$NON-NLS-2$

	/** The Set Comment (SHFT + F9) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbEditComment = MFSMenuButton.createSmallButton("Comment", //$NON-NLS-1$
			"smF21.gif", "Set Comment", false); //$NON-NLS-1$ //$NON-NLS-2$

	/** The Toggle Material Movements (SHFT + F10) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbMovement = MFSMenuButton.createSmallButton("Set Move", //$NON-NLS-1$
			"smF22.gif", "Toggle Material Movements", false); //$NON-NLS-1$ //$NON-NLS-2$

	/** The Hide/Show Instructions (SHFT + F11) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbHide = MFSMenuButton.createSmallButton("Hide Ins", //$NON-NLS-1$
			"smF23.gif", "Hide Instructions", false); //$NON-NLS-1$ //$NON-NLS-2$

	/** The Part Functions (SHFT + F12) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbPartFunctions = MFSMenuButton.createSmallButton("Part Fnct", //$NON-NLS-1$
			"smF24.gif", "Part Functions", false); //$NON-NLS-1$ //$NON-NLS-2$
	//~6A
	//~23C Change button images from DWCTRLFx.gif to smCTRLFx.gif
	/** The New Container (CTRL + F1) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbNewCntr = MFSMenuButton.createSmallButton("New Cntr", //$NON-NLS-1$
			"smCTRLF1.gif", "New Container", false); //$NON-NLS-1$ //$NON-NLS-2$
	
	//~19A ~21C
	/** The Remove All Non Serialized (CTRL + F2) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbRemNonSer = MFSMenuButton.createSmallButton("Rem Non", //$NON-NLS-1$
			"smCTRLF2.gif", "Remove All Non Serialized", false); //$NON-NLS-1$ //$NON-NLS-2$
	
	//~36A
	/** The View Data Collection Button (CTRL + F3) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbViewCollData = MFSMenuButton.createSmallButton("View Coll", //$NON-NLS-1$
			"smCTRLF3.gif", "View Data Collection", false); //$NON-NLS-1$ //$NON-NLS-2$
	
	//~43A
	/** The On Demand Label (CTRL + L) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbOnDemand = MFSMenuButton.createSmallButton("Labels",  //$NON-NLS-1$
			"smCTRLL.gif", "On Demand Label Printing", false); //$NON-NLS-1$ //$NON-NLS-2$

	/** The MCTL of the current work unit. */
	private String fieldCurrMctl = ""; //$NON-NLS-1$

	/** The end code. Used to determine the next action to perform. */
	private int fieldEndCode = 0;

	/** @see #editComment() */
	private String fieldComment = ""; //$NON-NLS-1$

	/** @see #editLoc() */
	private String fieldLoc = ""; //$NON-NLS-1$
	
	/** @see #editMovement() */
	private String fieldMovementOn = ""; //$NON-NLS-1$
	
	/** The data last displayed by {@link #showPNSNErrorList(String, int)}. */
	private String fieldPNSNErrorData = ""; //$NON-NLS-1$

	/** The index last selected in {@link #showPNSNErrorList(String, int)}. */
	private int fieldPNSNErrorIndex = 0;
	
	/** The number of <code>MFSComponentRec</code>s with an IDSP of I. */
	private int fieldCounter = 0;

	/** The row <code>Vector</code> when instructions are shown. */
	private Vector<MFSPartInstructionJPanel> fieldShowRowVector = new Vector<MFSPartInstructionJPanel>();

	/** The row <code>Vector</code> when instructions are not shown. */
	private Vector<MFSPartInstructionJPanel> fieldHideRowVector = new Vector<MFSPartInstructionJPanel>();
	
	/** The index of the active row in the part number list. */
	private int fieldActiveRowIndex = 0;
	
	/** The End/Suspend location. */
	private String fieldEndOrSuspendLoc = ""; //$NON-NLS-1$
	
	/** Stores the down bin parts (key: part number; value: serial number). */
	private Hashtable<String, String> fieldDownBinList = new Hashtable<String, String>(); // ~13;

	/**
	 * Stores a reference to the next <code>MFSPanel</code> that is displayed
	 * so the {@link #assignFocus()}method performs the correct logic when this
	 * <code>MFSDirectWorkPanel</code> is redisplayed.
	 */
	private MFSPanel fieldChildPanel = null; //~16A
	
	/** The <code>MFSButtonIterator</code> for this panel. */
	private MFSButtonIterator fieldButtonIterator = null; //~16A

	/** The <code>boolean</code> autologged flag indicator. */
	public boolean autoLoggedFlag = false; //~41
	
	/**
	 * Constructs a new <code>MFSDirectWorkPanel</code>.
	 * @param parent the <code>MFSFrame</code> used to display this panel
	 * @param source the <code>MFSPanel</code> that caused this panel to be displayed
	 */
	public MFSDirectWorkPanel(MFSFrame parent, MFSPanel source)
	{
		super(parent, source, SCREEN_NAME);
		this.fieldButtonIterator = createMenuButtonIterator();
		this.pnlButtons.setLayout(new IGSGridLayout(5, 4, 2, 1));
		this.pnlButtons.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
		this.addMyListeners();
	}
	
	/** Adds the listeners to this panel's <code>Component</code>s. */
	protected void addMyListeners()
	{
		//~16 Redone to use button iterator and remove PTR35927JM code
		this.fieldButtonIterator.reset();
		while (this.fieldButtonIterator.hasNext())
		{
			JButton next = this.fieldButtonIterator.nextJButton();
			next.addActionListener(this);
			next.addKeyListener(this);
		}

		for (int i = 0; i < this.fieldRowVector.size(); i++)
		{
			JList pipPNList = getRowVectorElementAt(i).getPNList();
			pipPNList.addKeyListener(this);
			pipPNList.addMouseListener(this);
		}

		this.spPartsInst.addKeyListener(this);
	}

	/**
	 * Called to add a part.
	 * @param logType the log type for the <code>MFSLogPartDialog</code>
	 */
	public void addpn(String logType)
	{
		try
		{
			removeMyListeners();
			int rc = 0;
			boolean nonPartInstrFound = false;

			if (this.fieldComponentListModel.size() == 0)
			{
				/* display error to user */
				rc = 10;
				String erms = "No Parts to Install"; //$NON-NLS-1$
				IGSMessageBox.showOkMB(getParentFrame(), null, erms, null);
			}
			else if (logType.equals(MFSLogPartDialog.LT_ADD))
			{
				if (this.fieldBlueRow != -1
						&& getRowVectorElementAt(this.fieldActiveRow).getPNList().getSelectedIndex() == -1)
				{
					if (!getRowVectorElementAt(this.fieldBlueRow).getInstructionRec().getCompletionStatus().equals("C")) //$NON-NLS-1$
					{
						logBlueInstruction(this.fieldBlueRow, "keyPressed"); //$NON-NLS-1$
						nonPartInstrFound = true;
					}
					else
					{
						/* display error to user */
						rc = 10;
						String erms = "Instruction Already Complete!"; //$NON-NLS-1$
						IGSMessageBox.showOkMB(getParentFrame(), null, erms, null);
					}
				}//end of blue row
				else
				{
					if (this.fieldCurrentCompRec == null)
					{
						/* display error to user */
						rc = 10;
						String erms = "No Parts Selected!"; //$NON-NLS-1$
						IGSMessageBox.showOkMB(getParentFrame(), null, erms, null);
					}
				}
			}//end of plain Add part
			else if (logType.equals(MFSLogPartDialog.LT_AUTO_LOG)
					|| logType.equals(MFSLogPartDialog.LT_SEARCH_LOG))
			{
				MFSComponentSearch componentFinder = new MFSComponentSearch(
						this.fieldRowVector,
						new MFSComponentSearchCriteria.MFSAutoLogSearchCriteria());
				/*
				 * start at activeRow or blueRow (which ever is higher) and work
				 * way to the bottom looking for a part
				 */
				int row = this.fieldActiveRow;
				int index = 0;
				if (this.fieldBlueRow != -1 && this.fieldBlueRow < row)
				{
					row = this.fieldBlueRow;
				}
				JList tempList = getRowVectorElementAt(row).getPNList();
				index = tempList.getSelectedIndex();
				componentFinder.setSearchStartPosition(row, index);  //~41
				componentFinder.setSearchNonPartInstruction(true);   //~41

				if(componentFinder.findComponent())   //~41
				{
					
					row = componentFinder.getRow();
					index = componentFinder.getIndex();
					if(componentFinder.getFieldMatchingInstructionRec() == null)
					{					
						getRowVectorElementAt(row).getPNList().setSelectedIndex(index);
						getRowVectorElementAt(row).getPNList().ensureIndexIsVisible(index);
						this.fieldBlueRow = -1;
					}
					else
					{
						nonPartInstrFound = true;						
						MFSPartInstructionJPanel pip = getRowVectorElementAt(row);
						this.fieldBlueRow = row;
						pip.changeColor(MFSPartInstructionJPanel.BLUE_ROW_COLOR);
						int yBounds = (int) pip.getBounds().getY();					
						this.spPartsInst.getVerticalScrollBar().setValue(yBounds);
						this.scrollToPip(pip);
						
						//un-highlight parts
						this.fieldActiveRowIndex = index;
						getRowVectorElementAt(row).getPNList().clearSelection();

						logBlueInstruction(row, logType);
					}
				}
				else
				{
					/* display error to user */
					rc = 10;
					String erms = "All Parts have been Installed"; //$NON-NLS-1$
					IGSMessageBox.showOkMB(getParentFrame(), null, erms, null);
				}
			}
			if (rc == 0 && !nonPartInstrFound)
			{
				//call method to display the logPart dialog
				showLogPart(logType);
			}//end of rc == 0 check
		}
		catch (Exception e)
		{
			this.setCursor(Cursor.getDefaultCursor());
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}

		addMyListeners();
		focusPNListOrButton(this.pbAdd);
		if(this.autoLoggedFlag)
		{
			if (MFSConfig.getInstance().containsConfigEntry("EFFICIENCYON")) //~1a			 //$NON-NLS-1$
			{
				boolean[] completeArray = new boolean[3];
				this.checkIfComplete(completeArray);
				if(completeArray[MFSDirectWorkPanel.LT_COMPLETE_CHECK_COMPLETE] &&
				   !completeArray[MFSDirectWorkPanel.LT_COMPLETE_CHECK_REMOVE] &&
				   ! completeArray[MFSDirectWorkPanel.LT_COMPLETE_CHECK_MANDATORY])
				{
					this.end_work();
				}
			}
		}	
	}

	//~15A New method
	//~16C Add logic for use in Java 5 version of client 
	/** {@inheritDoc} */
	public void assignFocus()
	{
		// If scrollRectToVisible is not enabled, the panel is displaying
		// the MFSPartInstructionJPanels for a work unit for the first time.
		// Thus, the appropriate initial focus method is called.
		// Otherwise, the panel caused a different panel (fieldChildPanel)
		// to be displayed and is being redisplayed.
		if (this.pnlRowHolder.isScrollRectToVisibleEnabled() == false)
		{
			if (getScreenName().equals(FKIT_SCREEN_NAME))
			{
				setInitialFocusFKIT();
			}
			else
			{
				setInitialFocusNonFKIT();
			}
		}
		else
		{
			if (this.fieldRowVector.size() > 0)
			{
				getRowVectorElementAt(this.fieldActiveRow).getPNList().requestFocusInWindow();
			}
			else
			{
				if (this.fieldChildPanel instanceof MFSViewInstPartsPanel)
				{
					this.pbViewIns.requestFocusInWindow();
				}
				else if (this.fieldChildPanel instanceof MFSDirWrkExtFnctPanel)
				{
					this.pbExtFunc.requestFocusInWindow();
				}
				else if (this.fieldChildPanel instanceof MFSPartFunctionDisplayPanel)
				{
					this.pbPartFunctions.requestFocusInWindow();
				}
				else
				{
					new RuntimeException("Bad condition: Child Panel = " //$NON-NLS-1$
							+ this.fieldChildPanel).printStackTrace();
				}
			}
			this.fieldChildPanel = null;
		}
	}
	
	/**
	 * @return <code>true</code> iff there is a <code>MFSComponentRec</code>
	 *         with a buffer status of 2
	 */
	public boolean checkForPending()
	{
		int i = 0;
		boolean found = false;

		while (i < this.fieldComponentListModel.size() && !found)
		{
			MFSComponentRec next = this.fieldComponentListModel.getComponentRecAt(i);
			if (next.getBufferStatus().equals("2")) //$NON-NLS-1$
			{
				found = true;
			}
			i++;
		}
		return found;
	}
	
	
    //~35A New method
	/** Check that all Parts are available to MASSKITINSTALL logic **/
	public boolean checkPartsforMassKit()
	{
		int index = 0;
		boolean allowMassKit = true;
		
		while (index < this.fieldComponentListModel.size() && allowMassKit)
		{
			MFSComponentRec next = this.fieldComponentListModel.getComponentRecAt(index);
			if(((next.getTpok().equals("2")) && (next.getIdsp().equals("I"))) || //$NON-NLS-1$ //$NON-NLS-2$
			   ((!next.getCooi().equals(" ")) && (!next.getCooi().equals("0")))) //$NON-NLS-1$ //$NON-NLS-2$
			{
				allowMassKit = false;
			}
			
			index++;
		}
		
		return allowMassKit;
	}


	//~16A New method
	/** Removes all elements from the instruction <code>Vector</code>. */
	public void clearInstVector()
	{
		this.fieldInstVector.clear();
	}
	
	//~16A New method
	/** Calls {@link JButton#doClick()} on the edit loc button. */
	public void clickEditLocButton()
	{
		this.pbEditLoc.doClick();
	}

	/**
	 * Computes the number of installed parts, sets the text of
	 * {@link #lblCount}, and sets the value of {@link #fieldCounter}.
	 */
	public void computeCounter()
	{
		int index = 0;
		int tmpCounter = 0;

		while (index < this.fieldComponentListModel.size())
		{
			MFSComponentRec next = this.fieldComponentListModel.getComponentRecAt(index);
			if (next.getIdsp().equals("I")) //$NON-NLS-1$
			{
				tmpCounter++;
			}
			index++;
		}

		this.lblCount.setText("COUNT: " + tmpCounter); //$NON-NLS-1$
		this.fieldCounter = tmpCounter;
	}

	/** Determines which buttons are displayed in the menu. */
	public void configureButtons()
	{
		//Remove all of the buttons from the button panel before adding any
		this.pnlButtons.removeAll();

		//~16C Disable all of the buttons first, instead of just a few.
		this.fieldButtonIterator.reset();
		while(this.fieldButtonIterator.hasNext())
		{
			this.fieldButtonIterator.nextJButton().setEnabled(false);
		}

		//Remove unburned, burned, personalized, and unpersonalized from config
		final MFSConfig config = MFSConfig.getInstance();
		config.removeConfigEntry("UNBURNED"); //$NON-NLS-1$
		config.removeConfigEntry("BURNED"); //$NON-NLS-1$
		config.removeConfigEntry("PERSONALIZED"); //$NON-NLS-1$
		config.removeConfigEntry("UNPERSONALIZED"); //$NON-NLS-1$

		if (config.containsConfigEntry("BUTTON,DIRWORK,AUTOZAP")) //$NON-NLS-1$
		{
			this.pnlButtons.add(this.pbSrchLog);
			this.pbSrchLog.setEnabled(true);
		}

		if (config.containsConfigEntry("BUTTON,DIRWORK,ADDPART")) //$NON-NLS-1$
		{
			this.pnlButtons.add(this.pbAdd);
			this.pbAdd.setEnabled(true);
		}

		if (config.containsConfigEntry("BUTTON,DIRWORK,ENDJOB")) //$NON-NLS-1$
		{
			this.pnlButtons.add(this.pbEnd);
			this.pbEnd.setEnabled(true);
		}

		if (config.containsConfigEntry("BUTTON,DIRWORK,AUTOLOG")) //$NON-NLS-1$
		{
			this.pnlButtons.add(this.pbAutoLog);
			this.pbAutoLog.setEnabled(true);
		}

		if (config.containsConfigEntry("BUTTON,DIRWORK,EXTFUNC")) //$NON-NLS-1$
		{
			this.pnlButtons.add(this.pbExtFunc);
			this.pbExtFunc.setEnabled(true);
		}

		if (config.containsConfigEntry("BUTTON,DIRWORK,REWORK")) //$NON-NLS-1$
		{
			this.pnlButtons.add(this.pbRework);
			this.pbRework.setEnabled(true);
		}

		if (config.containsConfigEntry("BUTTON,DIRWORK,SUSPEND")) //$NON-NLS-1$
		{
			this.pnlButtons.add(this.pbSuspend);
			this.pbSuspend.setEnabled(true);
		}

		if (config.containsConfigEntry("BUTTON,DIRWORK,VIEWINST")) //$NON-NLS-1$
		{
			this.pnlButtons.add(this.pbViewIns);
			this.pbViewIns.setEnabled(true);
		}

		if (config.containsConfigEntry("BUTTON,DIRWORK,CONTAIN")) //$NON-NLS-1$
		{
			this.pnlButtons.add(this.pbCntr);
			this.pbCntr.setEnabled(true);
		}
		else if (config.containsConfigEntry("BUTTON,DIRWORK,VPDBURN")) //$NON-NLS-1$
		{
			/* this will check the value in the config file for the current */
			/* op, or for nothing and will enable the button in this case */
			final String vpdBurn = config.getConfigValue("BUTTON,DIRWORK,VPDBURN"); //$NON-NLS-1$
			if (vpdBurn.equals(getHeaderRec().getNmbr()) || (vpdBurn.equals(""))) //$NON-NLS-1$
			{
				this.pnlButtons.add(this.pbBurn);
				this.pbBurn.setEnabled(true);

				/* add a value to the config file for an burn flag */
				config.setConfigValue("BURNED", "NO"); //$NON-NLS-1$ //$NON-NLS-2$
			}
		}
		else if (config.containsConfigEntry("BUTTON,DIRWORK,PERSONALIZE")) //$NON-NLS-1$
		{
			/* this will check the value in the config file for the current */
			/* op, or for nothing and will enable the button in this case */
			final String personalize = config.getConfigValue("BUTTON,DIRWORK,PERSONALIZE"); //$NON-NLS-1$
			if (personalize.equals(getHeaderRec().getNmbr()) || (personalize.equals(""))) //$NON-NLS-1$
			{
				this.pnlButtons.add(this.pbDasdPersonalize);
				this.pbDasdPersonalize.setEnabled(true);

				/* add a value to the config file for a personalized flag */
				config.setConfigValue("PERSONALIZED", "NO"); //$NON-NLS-1$ //$NON-NLS-2$
			}
		}

		if (config.containsConfigEntry("BUTTON,DIRWORK,MOVE")) //$NON-NLS-1$
		{
			this.pnlButtons.add(this.pbMove);
			this.pbMove.setEnabled(true);
		}

		if (config.containsConfigEntry("BUTTON,DIRWORK,REMPART")) //$NON-NLS-1$
		{
			this.pnlButtons.add(this.pbRemPart);
			this.pbRemPart.setEnabled(true);
		}

		if (config.containsConfigEntry("BUTTON,DIRWORK,CINC")) //$NON-NLS-1$
		{
			this.pnlButtons.add(this.pbCInC);
			this.pbCInC.setEnabled(true);
		}

		if (config.containsConfigEntry("BUTTON,DIRWORK,RMVPARTS")) //$NON-NLS-1$
		{
			this.pnlButtons.add(this.pbRmvParts);
			this.pbRmvParts.setEnabled(true);
		}

		if (config.containsConfigEntry("BUTTON,DIRWORK,SPLIT")) //$NON-NLS-1$
		{
			this.pnlButtons.add(this.pbSplit);
			this.pbSplit.setEnabled(true);
		}

		if (config.containsConfigEntry("BUTTON,DIRWORK,SHORT")) //$NON-NLS-1$
		{
			this.pnlButtons.add(this.pbShort);
			this.pbShort.setEnabled(true);
		}

		if (config.containsConfigEntry("BUTTON,DIRWORK,VPDUNBURN")) //$NON-NLS-1$
		{
			/* this will check the value in the config file for the current */
			/* op, or for nothing and will enable the button in this case */
			final String unburn = config.getConfigValue("BUTTON,DIRWORK,VPDUNBURN"); //$NON-NLS-1$
			if (unburn.equals(getHeaderRec().getNmbr()) || unburn.equals("")) //$NON-NLS-1$
			{
				this.pnlButtons.add(this.pbUnburn);
				this.pbUnburn.setEnabled(true);

				/* add a value to the config file for an unburn flag */
				config.setConfigValue("UNBURNED", "NO"); //$NON-NLS-1$ //$NON-NLS-2$
			}
		}

		if (config.containsConfigEntry("BUTTON,DIRWORK,NCMREPRINT")) //$NON-NLS-1$
		{
			this.pnlButtons.add(this.pbReprintNCMTag);
			this.pbReprintNCMTag.setEnabled(true);
		}
		
		if (config.containsConfigEntry("BUTTON,DIRWORK,EXTRAPART")) //$NON-NLS-1$
		{
			final String typz = this.fieldHeaderRec.getTypz();
			if (typz.equals("B") || typz.equals("D")) //$NON-NLS-1$ //$NON-NLS-2$
			{
				this.pnlButtons.add(this.pbExtra);
				this.pbExtra.setEnabled(true);
			}
			else
			{
				final String wtyp = this.fieldHeaderRec.getWtyp();
				if (wtyp.equals("T") || wtyp.equals("C")) //$NON-NLS-1$ //$NON-NLS-2$
				{
					this.pnlButtons.add(this.pbExtraClogPart);
					this.pbExtraClogPart.setEnabled(true);

					//only want to add button if CLOG Extra button is there
					//AND the config value for the button tells us to do so.
					if (config.containsConfigEntry("BUTTON,DIRWORK,RESETINTERPOSER")) //$NON-NLS-1$
					{
						String interposer = config.getConfigValue("BUTTON,DIRWORK,RESETINTERPOSER").trim(); //$NON-NLS-1$

						if (interposer.equals(this.fieldHeaderRec.getPrln() + "," + this.fieldHeaderRec.getNmbr()) //$NON-NLS-1$
								|| interposer.equals(this.fieldHeaderRec.getPrln() + ",*ALL") //$NON-NLS-1$
								|| interposer.equals("*ALL," + this.fieldHeaderRec.getNmbr()) //$NON-NLS-1$
								|| interposer.equals("*ALL,*ALL") //$NON-NLS-1$
								|| interposer.equals("")) //$NON-NLS-1$
						{
							this.pnlButtons.add(this.pbResetInterposer);
							this.pbResetInterposer.setEnabled(true);

							//also want to disable the suspend button for these.
							if (this.pbSuspend.isEnabled())
							{
								this.pbSuspend.setEnabled(false);
							}
						}
					}

					//Get the skip button to show up in the right spot
					if (config.containsConfigEntry("BUTTON,DIRWORK,SKIPPART"))  //$NON-NLS-1$
					{
						this.pnlButtons.add(this.pbSkipPart);
						this.pbSkipPart.setEnabled(true);
					}

					this.pnlButtons.add(this.pbEditLoc);
					this.pbEditLoc.setEnabled(true);

					this.pnlButtons.add(this.pbEditComment);
					this.pbEditComment.setEnabled(true);

					this.pnlButtons.add(this.pbMovement);
					this.pbMovement.setEnabled(true);
				}
			}
		}
		//Get the skip button to show up in the right spot
		if (!this.pbSkipPart.isEnabled())
		{
			if (config.containsConfigEntry("BUTTON,DIRWORK,SKIPPART")) //$NON-NLS-1$
			{
				this.pnlButtons.add(this.pbSkipPart);
				this.pbSkipPart.setEnabled(true);
			}
		}

		final String blvl = this.fieldHeaderRec.getBlvl().toUpperCase();
		if (blvl.equals("E") || blvl.equals("H")) //$NON-NLS-1$ //$NON-NLS-2$
		{
			this.pnlButtons.add(this.pbHide);
			this.pbHide.setEnabled(true);
		}

		if (config.containsConfigEntry("TRXBUFFER")) //$NON-NLS-1$
		{
			this.pnlButtons.add(this.pbVerify);
			this.pbVerify.setEnabled(true);
		}

		if (config.containsConfigEntry("BUTTON,DIRWORK,PARTFUNCTIONS")) //$NON-NLS-1$
		{
			this.pnlButtons.add(this.pbPartFunctions);
			this.pbPartFunctions.setEnabled(true);
		}

		// Begin ~6A
		if (config.containsConfigEntry("BUTTON,DIRWORK,NEWCNTR")) //$NON-NLS-1$
		{
			this.pnlButtons.add(this.pbNewCntr);
			this.pbNewCntr.setEnabled(true);
		}
		// End ~6A

		if (config.containsConfigEntry("BUTTON,DIRWORK,VIEWCOLLECTION")) //$NON-NLS-1$	//~36A
		{
			this.pnlButtons.add(this.pbViewCollData);									//~36A
			this.pbViewCollData.setEnabled(true);										//~36A	
		}		
		
		//~43A
		if(config.containsConfigEntry("BUTTON,DIRWORK,ONDEMANDLBL")) //$NON-NLS-1$
		{
			this.pnlButtons.add(this.pbOnDemand);								
			this.pbOnDemand.setEnabled(true);	
		}
	}

	//~16A New method
	/** Configures the panel to display the FKIT screen. */
	public void configureFKIT()
	{
		configureFKITButtons();
		this.fieldBlueRow = -1;
		this.fieldActiveRowIndex = -1;
	}

	/** Determines which buttons are displayed in the menu for FKIT. */
	public void configureFKITButtons()
	{
		//Remove all of the buttons from the button panel before adding any
		this.pnlButtons.removeAll();

		//~16C Disable all of the buttons first, instead of just a few.
		this.fieldButtonIterator.reset();
		while(this.fieldButtonIterator.hasNext())
		{
			this.fieldButtonIterator.nextJButton().setEnabled(false);
		}

		this.pnlButtons.add(this.pbSrchLog);
		this.pbSrchLog.setEnabled(true);

		this.pnlButtons.add(this.pbAdd);
		this.pbAdd.setEnabled(true);

		this.pnlButtons.add(this.pbEnd);
		this.pbEnd.setEnabled(true);

		this.pnlButtons.add(this.pbSearchRem);
		this.pbSearchRem.setEnabled(true);

		this.pnlButtons.add(this.pbRemPart);
		this.pbRemPart.setEnabled(true);

		if (MFSConfig.getInstance().getConfigValue("DBGL").equals("Y")) //$NON-NLS-1$ //$NON-NLS-2$
		{
			this.pnlButtons.add(this.pbDebugGolden);
			this.pbDebugGolden.setEnabled(true);
		}
		// ~19A
		if (MFSConfig.getInstance().containsConfigEntry("BUTTON,FKIT,RMVNONSER")) //$NON-NLS-1$
		{
			//~21C Use MFSLogOnPanel.RMVALLFKIT constant
			if(MFSConfig.getInstance().getConfigValue(MFSLogOnPanel.RMVALLFKIT).equals("Y")) //$NON-NLS-1$
			{
				this.pnlButtons.add(this.pbRemNonSer);
				this.pbRemNonSer.setEnabled(true);			
			}
		}

	}

	//~16A New method
	/**
	 * Configures the panel to display the Direct Work or Tear Down screen
	 * depending on the value of <code>who</code>.
	 * @param who J for Direct Work; Z for Tear Down
	 */
	public void configureNonFKIT(String who)
	{
		String malc = this.fieldHeaderRec.getMalc();
		if (!malc.equals("       ")) //$NON-NLS-1$
		{
			this.nlLoc.setText("LOC:"); //$NON-NLS-1$
			StringBuffer loc = new StringBuffer();
			loc.append(this.fieldHeaderRec.getMalc());
			loc.append("/"); //$NON-NLS-1$
			loc.append(this.fieldHeaderRec.getMilc());
			this.fieldLoc = loc.toString();
			this.vlLoc.setText(this.fieldLoc);
		}
		else
		{
			this.nlLoc.setText(""); //$NON-NLS-1$
			this.fieldLoc = ""; //$NON-NLS-1$
			this.vlLoc.setText(""); //$NON-NLS-1$
		}
		
		this.fieldPNSNErrorData = ""; //$NON-NLS-1$
		this.fieldPNSNErrorIndex = 0;

		if (who.equals("Z")) //$NON-NLS-1$
		{
			configureTearDownButtons();
		}
		else
		{
			configureButtons();
		}

		this.fieldComment = ""; //$NON-NLS-1$
		this.fieldActiveRow = 0;
		this.fieldBlueRow = -1;
		this.fieldActiveRowIndex = -1;
		this.fieldMovementOn = ""; //$NON-NLS-1$

		if (this.fieldHeaderRec.getWtyp().equals("T") //$NON-NLS-1$
				|| this.fieldHeaderRec.getWtyp().equals("C")) //$NON-NLS-1$
		{
			computeCounter();
		}
		
		this.pbHide.setText("Hide Ins"); //$NON-NLS-1$
		this.pbHide.setToolTipText("Hide Instructions"); //$NON-NLS-1$
	}

	/** Determines which buttons are displayed in the menu for tear down. */
	public void configureTearDownButtons()
	{
		//~16A Remove all of the buttons from the button panel before adding any
		this.pnlButtons.removeAll();

		//~16A Disable all of the buttons first
		this.fieldButtonIterator.reset();
		while(this.fieldButtonIterator.hasNext())
		{
			this.fieldButtonIterator.nextJButton().setEnabled(false);
		}
		
		final MFSConfig config = MFSConfig.getInstance();
		if (config.containsConfigEntry("BUTTON,TEARDOWN,ENDJOB")) //$NON-NLS-1$
		{
			this.pnlButtons.add(this.pbEnd);
			this.pbEnd.setEnabled(true);
		}

		if (config.containsConfigEntry("BUTTON,TEARDOWN,SUSPEND")) //$NON-NLS-1$
		{
			this.pnlButtons.add(this.pbSuspend);
			this.pbSuspend.setEnabled(true);
		}

		if (config.containsConfigEntry("BUTTON,TEARDOWN,REMPART")) //$NON-NLS-1$
		{
			this.pnlButtons.add(this.pbRemPart);
			this.pbRemPart.setEnabled(true);
		}

		if (config.containsConfigEntry("BUTTON,TEARDOWN,SEARCHREM")) //$NON-NLS-1$
		{
			this.pnlButtons.add(this.pbSearchRem);
			this.pbSearchRem.setEnabled(true);
		}
		// ~19A
		if(config.containsConfigEntry("BUTTON,TEARDOWN,RMVNONSER")) //$NON-NLS-1$
		{
			//~21C Use MFSLogOnPanel.RMVALLTEAR constant
			if(config.getConfigValue(MFSLogOnPanel.RMVALLTEAR).equals("Y")) //$NON-NLS-1$
			{
				this.pnlButtons.add(this.pbRemNonSer);
				this.pbRemNonSer.setEnabled(true);
			}
		}
	}

//	~16A New method
	/**
	 * Creates an <code>MFSButtonIterator</code> for this panel's
	 * <code>MFSMenuButton</code>s.
	 * @return the <code>MFSButtonIterator</code>
	 */
	private MFSButtonIterator createMenuButtonIterator()
	{
		//The constructor param is the number of buttons
		MFSButtonIterator result = new MFSButtonIterator(36); //~36C //~43C
		result.add(this.pbSrchLog);
		result.add(this.pbAdd);
		result.add(this.pbEnd);
		result.add(this.pbAutoLog);
		result.add(this.pbExtFunc);
		result.add(this.pbRework);
		result.add(this.pbSuspend);
		result.add(this.pbViewIns);
		result.add(this.pbCntr);
		result.add(this.pbBurn);
		result.add(this.pbDasdPersonalize);
		result.add(this.pbSearchRem);
		result.add(this.pbMove);
		result.add(this.pbRemPart);
		result.add(this.pbCInC);
		result.add(this.pbRmvParts);
		result.add(this.pbSplit);
		result.add(this.pbShort);
		result.add(this.pbUnburn);
		result.add(this.pbDasdUnpersonalize);
		result.add(this.pbReprintNCMTag);
		result.add(this.pbExtra);
		result.add(this.pbExtraClogPart);
		result.add(this.pbResetInterposer);
		result.add(this.pbDebugGolden);
		result.add(this.pbSkipPart);
		result.add(this.pbVerify);
		result.add(this.pbEditLoc);
		result.add(this.pbEditComment);
		result.add(this.pbMovement);
		result.add(this.pbHide);
		result.add(this.pbPartFunctions);
		result.add(this.pbNewCntr);
		result.add(this.pbRemNonSer);//~19A 
		result.add(this.pbViewCollData);//~36A
		result.add(this.pbOnDemand); //~43A
		return result;
	}
	
	/** Performs DASD personalization. */
	public void dasdPersonalization()
	{
		int rc = 0;
		String errorString = ""; //$NON-NLS-1$
		final MFSConfig config = MFSConfig.getInstance();

		try
		{
			removeMyListeners();

			String pilotFlag = "N"; //$NON-NLS-1$
			//check for DASDPILOT config entry, used to determine if we are
			if (config.containsConfigEntry("DASDPILOT")) //$NON-NLS-1$
			{
				pilotFlag = "Y"; //$NON-NLS-1$
			}

			//call handle_dfc to figure out what slot we need to plug this dasd into
			MfsXMLDocument xml_data1 = new MfsXMLDocument("HANDLE_DFC"); //$NON-NLS-1$
			xml_data1.addOpenTag("DATA"); //$NON-NLS-1$
			xml_data1.addCompleteField("CELL", config.get8CharCell()); //$NON-NLS-1$
			xml_data1.addCompleteField("ACTN", "Q"); //$NON-NLS-1$ //$NON-NLS-2$
			xml_data1.addCloseTag("DATA"); //$NON-NLS-1$
			xml_data1.finalizeXML();

			StringBuffer amsg = new StringBuffer(64);
			amsg.append("Retrieving Slot Information for CELL = "); //$NON-NLS-1$
			amsg.append(config.get8CharCell());
			amsg.append(", Please Wait..."); //$NON-NLS-1$

			MFSTransaction handle_dfc = new MFSXmlTransaction(xml_data1.toString());
			handle_dfc.setActionMessage(amsg.toString());
			MFSComm.getInstance().execute(handle_dfc, this);
			rc = handle_dfc.getReturnCode();

			if (rc != 0)
			{
				errorString = handle_dfc.getErms();
			}
			else
			{
				MfsXMLParser xmlParser = new MfsXMLParser(handle_dfc.getOutput());

				//pull out SLOT and SCSI tags to tell the user where to plug the DASD
				String literalSlot = xmlParser.getField("SLOT"); //$NON-NLS-1$
				String scsiSlot = xmlParser.getField("SCSI"); //$NON-NLS-1$
				Object[] options = {"OK", "CANCEL"}; //$NON-NLS-1$ //$NON-NLS-2$
				int n = JOptionPane.showOptionDialog(this, "Please Plug DASD into Slot " //$NON-NLS-1$
						+ new Integer(literalSlot), "Information", //$NON-NLS-1$
						JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
						null, options, options[0]);

				//if n == 0, user pressed OK button
				if (n != 0)
				{
					this.lblCntr.setText("                "); //$NON-NLS-1$
				}
				else
				{
					//display the slot on the DirectWork Screen where the
					// container would normally display
					this.lblCntr.setText("SLOT: " + literalSlot + "       "); //$NON-NLS-1$ //$NON-NLS-2$

					//now we need to execute the next step, RTV_DS10 to get setup data
					MfsXMLDocument xml_data2 = new MfsXMLDocument("RTV_DS10"); //$NON-NLS-1$
					xml_data2.addOpenTag("DATA"); //$NON-NLS-1$
					xml_data2.addCompleteField("ASPN", //$NON-NLS-1$ 
							("00000" + this.fieldHeaderRec.getPrln()).substring(0, 12)); //$NON-NLS-1$
					xml_data2.addCompleteField("MCTL", this.fieldHeaderRec.getMctl()); //$NON-NLS-1$
					xml_data2.addCompleteField("PILF", pilotFlag); //$NON-NLS-1$
					xml_data2.addCompleteField("PORU", "P"); //$NON-NLS-1$ //$NON-NLS-2$
					xml_data2.addCloseTag("DATA"); //$NON-NLS-1$
					xml_data2.finalizeXML();

					MFSTransaction rtv_ds10 = new MFSXmlTransaction(xml_data2.toString());
					rtv_ds10.setActionMessage("Retrieving DASD Setup Information, Please Wait..."); //$NON-NLS-1$
					MFSComm.getInstance().execute(rtv_ds10, this);
					rc = rtv_ds10.getReturnCode();

					if (rc != 0)
					{
						errorString = rtv_ds10.getErms();
					}
					else
					{
						MfsXMLParser xmlParser2 = new MfsXMLParser(rtv_ds10.getOutput());

						//define the strings we will get back from RTV_DS10,
						// outside of try block for better scope
						String genericPn = ""; //$NON-NLS-1$
						String finalDrivePn = ""; //$NON-NLS-1$
						String intermediatePn = ""; //$NON-NLS-1$
						String codeName = ""; //$NON-NLS-1$
						String ipkt = ""; //$NON-NLS-1$
						String finalChangeDef = ""; //$NON-NLS-1$
						String speedInfo = ""; //$NON-NLS-1$
						String loadId = ""; //$NON-NLS-1$
						String revisionLevel = ""; //$NON-NLS-1$
						String codeLevel = ""; //$NON-NLS-1$
						String unlockSupported = ""; //$NON-NLS-1$
						try
						{
							genericPn = xmlParser2.getField("GNPN"); //$NON-NLS-1$
							finalDrivePn = xmlParser2.getField("FDPN"); //$NON-NLS-1$
							intermediatePn = xmlParser2.getField("ITPN"); //$NON-NLS-1$
							codeName = xmlParser2.getField("CODN"); //$NON-NLS-1$
							ipkt = xmlParser2.getField("IPKT"); //$NON-NLS-1$
							finalChangeDef = xmlParser2.getField("FCDC"); //$NON-NLS-1$
							speedInfo = xmlParser2.getField("SPDI"); //$NON-NLS-1$
							loadId = xmlParser2.getField("LDID"); //$NON-NLS-1$
							revisionLevel = xmlParser2.getField("RVLV"); //$NON-NLS-1$
							codeLevel = xmlParser2.getField("CDLV"); //$NON-NLS-1$
							unlockSupported = xmlParser2.getField("UNLS"); //$NON-NLS-1$
						}
						catch (MISSING_XML_TAG_EXCEPTION e)
						{
							//will display this error later in the code
							rc = 10;
							System.out.println("Program Exception: " + e); //$NON-NLS-1$
							e.printStackTrace();
							errorString = "Missing Tags returned from RTV_DS10 call!"; //$NON-NLS-1$
						}

						if (rc == 0)
						{
							errorString = writeMicroCodeFile(codeName,loadId);    //~37C
							if (errorString.length() > 0)
							{
								rc = -11;
							}
							else
							{
								/*
								 * set up a error string that will contain error
								 * information returned from mike's dll
								 */
								String dasdpError = new String(""); //$NON-NLS-1$

								//~16C Use getDadspPath
								final String path = getDasdpPath(codeName.trim());

								//~16C Replaced "c:\\mfsexe40\\dasdp\\" + codeName.trim()+".bin" with path
								DasdWrap dasdw = new DasdWrap(
										this.fieldHeaderRec.getSapn().substring(6, 12),
										genericPn.substring(5), intermediatePn.substring(5),
										finalDrivePn.substring(5), speedInfo,
										(new Integer(ipkt)).intValue(), 
										finalChangeDef.substring(0, 2),
										finalChangeDef.substring(2, 4), 
										finalChangeDef.substring(4, 6), 
										path, (new Integer(scsiSlot)).intValue(), loadId,
										revisionLevel, codeLevel, unlockSupported, dasdpError);

								/* make sure dll was loaded correctly */
								errorString = dasdw.getErr();
								rc = dasdw.getRc();
								if (rc != 0)
								{
									//will display the error lower in the code
								}
								else
								{
									//~16C Use executeRunnable
									executeRunnable(dasdw, "Personalizing DASD, Please Wait..."); //$NON-NLS-1$
									
									errorString = dasdw.getRcStr();
									rc = dasdw.getRc();

									//dasdwrap failed
									if (rc != 0)
									{
										//will display the error lower in the code
									}
									else
									{
										ImageIcon iconic = new ImageIcon(getClass().getResource("/images/handshake.gif")); //$NON-NLS-1$

										JLabel lbl = new JLabel();
										lbl.setFont(MFSConstants.MEDIUM_DIALOG_FONT);
										lbl.setForeground(MFSConstants.PRIMARY_FOREGROUND_COLOR);
										lbl.setText("The Drive Was Successfully Personalized!"); //$NON-NLS-1$
										JOptionPane.showMessageDialog(this, lbl,
												"Successful Personalization", //$NON-NLS-1$
												JOptionPane.INFORMATION_MESSAGE, iconic);

										/*
										 * change the value in the config file for a personalized flag
										 * to YES because we have successfully personalized
										 */
										config.setConfigValue("PERSONALIZED", "YES"); //$NON-NLS-1$ //$NON-NLS-2$

										//trigger a dasdp label
										MFSPrintingMethods.dasdp(this.fieldHeaderRec.getMctl(),
												pilotFlag, 1, getParentFrame());

										//increment plug count on dasd personalization fixture
										MfsXMLDocument xml_data3 = new MfsXMLDocument("HANDLE_DFC"); //$NON-NLS-1$
										xml_data3.addOpenTag("DATA"); //$NON-NLS-1$
										xml_data3.addCompleteField("CELL", config.get8CharCell()); //$NON-NLS-1$
										xml_data3.addCompleteField("ACTN", "I"); //$NON-NLS-1$ //$NON-NLS-2$
										xml_data3.addCompleteField("SLOT", literalSlot); //$NON-NLS-1$
										xml_data3.addCloseTag("DATA"); //$NON-NLS-1$
										xml_data3.finalizeXML();

										StringBuffer amsg3 = new StringBuffer();
										amsg3.append("Incrementing Count on  Slot "); //$NON-NLS-1$
										amsg3.append(literalSlot);
										amsg3.append(" for CELL = "); //$NON-NLS-1$
										amsg3.append(config.get8CharCell());
										amsg3.append(", Please Wait..."); //$NON-NLS-1$

										MFSTransaction handle_dfc3 = new MFSXmlTransaction(xml_data3.toString());
										handle_dfc3.setActionMessage(amsg3.toString());
										MFSComm.getInstance().execute(handle_dfc3, this);
										rc = handle_dfc3.getReturnCode();

										if (rc != 0)
										{
											errorString = handle_dfc3.getErms();
										}
										else
										{
											//incremented plug count successfully, now update the microcode
											//level on the FCSPWU10 table
											MfsXMLDocument xml_data4 = new MfsXMLDocument("UPDT_MCLV"); //$NON-NLS-1$
											xml_data4.addOpenTag("DATA"); //$NON-NLS-1$
											xml_data4.addCompleteField("MCTL", this.fieldHeaderRec.getMctl()); //$NON-NLS-1$
											xml_data4.addCompleteField("MCLV", codeLevel); //$NON-NLS-1$
											xml_data4.addCloseTag("DATA"); //$NON-NLS-1$
											xml_data4.finalizeXML();

											StringBuffer amsg4 = new StringBuffer();
											amsg4.append("Updating Microcode Field on FCSPWU10 for MCTL = "); //$NON-NLS-1$
											amsg4.append(this.fieldHeaderRec.getMctl());
											amsg4.append(", Please Wait..."); //$NON-NLS-1$

											MFSTransaction updt_mclv = new MFSXmlTransaction(xml_data4.toString());
											updt_mclv.setActionMessage(amsg4.toString());
											MFSComm.getInstance().execute(updt_mclv, this);
											rc = updt_mclv.getReturnCode();

											if (rc != 0)
											{
												errorString = updt_mclv.getErms();
											}
											else
											{
												//good returns from updt_mclvl,
												//all complete with dasd personalization
											}
										}//good return from increment call to handle_dfc
									}
								}//end of good dasdwrap dll load
							}//end of good microcode file write
						}//end of rc == 0, all tags found in returned data from RTV_DS10
					}//end of good return from RTV_DS10
				}//end of pressed OK on Slot Information Dialog
			}//end of good call to HANDLE_DFC

			//Display errors generated from the above code.
			if (rc != 0)
			{
				IGSMessageBox.showOkMB(getParentFrame(), null, errorString, null);
			}
		}// end of try
		catch (Exception e)
		{
			setCursor(Cursor.getDefaultCursor());
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}

		addMyListeners();
		focusPNListOrButton(this.pbDasdPersonalize);
	}

	/** Performs DASD unpersonalization. */
	public void dasdUnPersonalization()
	{
		int rc = 0;
		String errorString = ""; //$NON-NLS-1$
		final MFSConfig config = MFSConfig.getInstance();

		try
		{
			removeMyListeners();

			String pilotFlag = "N"; //$NON-NLS-1$
			//check for DASDPILOT config entry, used to determine if we are
			if (config.containsConfigEntry("DASDPILOT")) //$NON-NLS-1$
			{
				pilotFlag = "Y"; //$NON-NLS-1$
			}

			//call handle_dfc to figure out what slot we need to plug this dasd into
			MfsXMLDocument xml_data1 = new MfsXMLDocument("HANDLE_DFC"); //$NON-NLS-1$
			xml_data1.addOpenTag("DATA"); //$NON-NLS-1$
			xml_data1.addCompleteField("CELL", config.get8CharCell()); //$NON-NLS-1$
			xml_data1.addCompleteField("ACTN", "Q"); //$NON-NLS-1$ //$NON-NLS-2$
			xml_data1.addCloseTag("DATA"); //$NON-NLS-1$
			xml_data1.finalizeXML();

			StringBuffer amsg = new StringBuffer(64);
			amsg.append("Retrieving Slot Information for CELL = "); //$NON-NLS-1$
			amsg.append(config.get8CharCell());
			amsg.append(", Please Wait..."); //$NON-NLS-1$

			MFSTransaction handle_dfc = new MFSXmlTransaction(xml_data1.toString());
			handle_dfc.setActionMessage(amsg.toString());
			MFSComm.getInstance().execute(handle_dfc, this);
			rc = handle_dfc.getReturnCode();

			if (rc != 0)
			{
				errorString = handle_dfc.getErms();
			}
			else
			{
				MfsXMLParser xmlParser = new MfsXMLParser(handle_dfc.getOutput());

				//pull out SLOT and SCSI tags to tell the user where to plug the DASD
				String literalSlot = xmlParser.getField("SLOT"); //$NON-NLS-1$
				String scsiSlot = xmlParser.getField("SCSI"); //$NON-NLS-1$
				Object[] options = {"OK", "CANCEL"}; //$NON-NLS-1$ //$NON-NLS-2$
				int n = JOptionPane.showOptionDialog(this, "Please Plug DASD into Slot " //$NON-NLS-1$
						+ new Integer(literalSlot), "Information", //$NON-NLS-1$
						JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
						null, options, options[0]);

				//if n == 0, user pressed OK button
				if (n != 0)
				{
					this.lblCntr.setText("                "); //$NON-NLS-1$
				}
				else
				{
					//display the slot on the DirectWork Screen where the
					// container would normally display
					this.lblCntr.setText("SLOT: " + literalSlot + "       "); //$NON-NLS-1$ //$NON-NLS-2$

					//now we need to execute the next step, RTV_DS10 to get setup data
					MfsXMLDocument xml_data2 = new MfsXMLDocument("RTV_DS10"); //$NON-NLS-1$
					xml_data2.addOpenTag("DATA"); //$NON-NLS-1$
					xml_data2.addCompleteField("ASPN",  //$NON-NLS-1$
							("00000" + this.fieldHeaderRec.getPrln()).substring(0, 12)); //$NON-NLS-1$
					xml_data2.addCompleteField("MCTL", this.fieldHeaderRec.getMctl()); //$NON-NLS-1$
					xml_data2.addCompleteField("PILF", pilotFlag); //$NON-NLS-1$
					xml_data2.addCompleteField("PORU", "U"); //$NON-NLS-1$ //$NON-NLS-2$
					xml_data2.addCloseTag("DATA"); //$NON-NLS-1$
					xml_data2.finalizeXML();

					MFSTransaction rtv_ds10 = new MFSXmlTransaction(xml_data2.toString());
					rtv_ds10.setActionMessage("Retrieving DASD Setup Information, Please Wait..."); //$NON-NLS-1$
					MFSComm.getInstance().execute(rtv_ds10, this);
					rc = rtv_ds10.getReturnCode();

					if (rc != 0)
					{
						errorString = rtv_ds10.getErms();
					}
					else
					{
						MfsXMLParser xmlParser2 = new MfsXMLParser(rtv_ds10.getOutput());

						//define the strings we will get back from RTV_DS10,
						// outside of try block for better scope
						String genericPn = ""; //$NON-NLS-1$
						String finalDrivePn = ""; //$NON-NLS-1$
						String intermediatePn = ""; //$NON-NLS-1$
						String codeName = ""; //$NON-NLS-1$
						String ipkt = ""; //$NON-NLS-1$
						String finalChangeDef = ""; //$NON-NLS-1$
						String speedInfo = ""; //$NON-NLS-1$
						String loadId = ""; //$NON-NLS-1$
						String revisionLevel = ""; //$NON-NLS-1$
						String codeLevel = ""; //$NON-NLS-1$
						String unlockSupported = ""; //$NON-NLS-1$
						try
						{
							genericPn = xmlParser2.getField("GNPN"); //$NON-NLS-1$
							finalDrivePn = xmlParser2.getField("FDPN"); //$NON-NLS-1$
							intermediatePn = xmlParser2.getField("ITPN"); //$NON-NLS-1$
							codeName = xmlParser2.getField("CODN"); //$NON-NLS-1$
							ipkt = xmlParser2.getField("IPKT"); //$NON-NLS-1$
							finalChangeDef = xmlParser2.getField("FCDC"); //$NON-NLS-1$
							speedInfo = xmlParser2.getField("SPDI"); //$NON-NLS-1$
							loadId = xmlParser2.getField("LDID"); //$NON-NLS-1$
							revisionLevel = xmlParser2.getField("RVLV"); //$NON-NLS-1$
							codeLevel = xmlParser2.getField("CDLV"); //$NON-NLS-1$
							unlockSupported = xmlParser2.getField("UNLS"); //$NON-NLS-1$
						}
						catch (MISSING_XML_TAG_EXCEPTION e)
						{
							//will display this error later in the code
							rc = 10;
							System.out.println("Program Exception: " + e); //$NON-NLS-1$
							e.printStackTrace();
							errorString = "Missing Tags returned from RTV_DS10 call!"; //$NON-NLS-1$
						}

						if (rc == 0)
						{
							errorString = writeMicroCodeFile(codeName,loadId);//~37C
							if (errorString.length() > 0)
							{
								rc = -11;
							}
							else
							{
								/*
								 * set up a error string that will contain error
								 * information returned from mike's dll
								 */
								String dasdpError = new String(""); //$NON-NLS-1$

								//~16C Use getDadspPath
								final String path = getDasdpPath(codeName.trim());

								//~16C Replaced "c:\\mfsexe40\\dasdp\\" + codeName.trim()+".bin" with path
								DasdWrap dasdw = new DasdWrap(
										this.fieldHeaderRec.getSapn().substring(6, 12), 
										genericPn.substring(5), intermediatePn.substring(5),
										finalDrivePn.substring(5), speedInfo,
										(new Integer(ipkt)).intValue(), 
										finalChangeDef.substring(0, 2), 
										finalChangeDef.substring(2, 4), 
										finalChangeDef.substring(4, 6), 
										path, (new Integer(scsiSlot)).intValue(), loadId,
										revisionLevel, codeLevel, unlockSupported, dasdpError);

								/* make sure dll was loaded correctly */
								errorString = dasdw.getErr();
								rc = dasdw.getRc();
								if (rc != 0)
								{
									//will display the error lower in the code
								}
								else
								{
									//~16C Use executeRunnable
									executeRunnable(dasdw, "Unpersonalizing DASD, Please Wait..."); //$NON-NLS-1$
									
									errorString = dasdw.getRcStr();
									rc = dasdw.getRc();

									//dasdwrap failed
									if (rc != 0)
									{
										//will display the error lower in the code
									}
									else
									{
										JLabel lbl = new JLabel();
										lbl.setFont(MFSConstants.MEDIUM_DIALOG_FONT);
										lbl.setForeground(MFSConstants.PRIMARY_FOREGROUND_COLOR);
										lbl.setText("The Drive Was Successfully Unpersonalized!"); //$NON-NLS-1$
										JOptionPane.showMessageDialog(this, lbl,
												"Successful UnPersonalization", //$NON-NLS-1$
												JOptionPane.INFORMATION_MESSAGE, null);

										/*
										 * change the value in the config file for an unpersonalized flag
										 * to YES because we have successfully unpersonalized
										 */
										config.setConfigValue("UNPERSONALIZED", "YES"); //$NON-NLS-1$ //$NON-NLS-2$

										//trigger a dasdp label
										MFSPrintingMethods.dasdp(this.fieldHeaderRec.getMctl(), 
												pilotFlag, 1, getParentFrame());

										//increment plug count on dasd personalization fixture
										MfsXMLDocument xml_data3 = new MfsXMLDocument("HANDLE_DFC"); //$NON-NLS-1$
										xml_data3.addOpenTag("DATA"); //$NON-NLS-1$
										xml_data3.addCompleteField("CELL", config.get8CharCell()); //$NON-NLS-1$
										xml_data3.addCompleteField("ACTN", "I"); //$NON-NLS-1$ //$NON-NLS-2$
										xml_data3.addCompleteField("SLOT", literalSlot); //$NON-NLS-1$
										xml_data3.addCloseTag("DATA"); //$NON-NLS-1$
										xml_data3.finalizeXML();

										StringBuffer amsg3 = new StringBuffer();
										amsg3.append("Incrementing Count on  Slot "); //$NON-NLS-1$
										amsg3.append(literalSlot);
										amsg3.append(" for CELL = "); //$NON-NLS-1$
										amsg3.append(config.get8CharCell());
										amsg3.append(", Please Wait..."); //$NON-NLS-1$

										MFSTransaction handle_dfc3 = new MFSXmlTransaction(xml_data3.toString());
										handle_dfc3.setActionMessage(amsg3.toString());
										MFSComm.getInstance().execute(handle_dfc3, this);
										rc = handle_dfc3.getReturnCode();

										if (rc != 0)
										{
											errorString = handle_dfc3.getErms();
										}
										else
										{
											//incremented plug count successfully, now update the microcode
											//level on the FCSPWU10 table
											MfsXMLDocument xml_data4 = new MfsXMLDocument("UPDT_MCLV"); //$NON-NLS-1$
											xml_data4.addOpenTag("DATA"); //$NON-NLS-1$
											xml_data4.addCompleteField("MCTL", this.fieldHeaderRec.getMctl()); //$NON-NLS-1$
											xml_data4.addCompleteField("MCLV", codeLevel); //$NON-NLS-1$
											xml_data4.addCloseTag("DATA"); //$NON-NLS-1$
											xml_data4.finalizeXML();

											StringBuffer amsg4 = new StringBuffer();
											amsg4.append("Updating Microcode Field on FCSPWU10 for MCTL = "); //$NON-NLS-1$
											amsg4.append(this.fieldHeaderRec.getMctl());
											amsg4.append(", Please Wait..."); //$NON-NLS-1$

											MFSTransaction updt_mclv = new MFSXmlTransaction(xml_data4.toString());
											updt_mclv.setActionMessage(amsg4.toString());
											MFSComm.getInstance().execute(updt_mclv, this);
											rc = updt_mclv.getReturnCode();

											if (rc != 0)
											{
												errorString = updt_mclv.getErms();
											}
											else
											{
												//good returns from updt_mclvl,
												//all complete with dasd unpersonalization
											}
										}//good return from increment call to handle_dfc
									}
								}//end of good dasdwrap dll load
							}//end of good microcode file write
						}//end of rc == 0, all tags found in returned data from RTV_DS10
					}//end of good return from RTV_DS10
				}//end of pressed OK on Slot Information Dialog
			}//end of good call to HANDLE_DFC

			//Display errors generated from the above code.
			if (rc != 0)
			{
				IGSMessageBox.showOkMB(getParentFrame(), null, errorString, null);
			}
		}// end of try
		catch (Exception e)
		{
			setCursor(Cursor.getDefaultCursor());
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}

		addMyListeners();
		focusPNListOrButton(this.pbDasdPersonalize);
	}

	/** Displays a dialog to let the user edit the comment text. */
	public void editComment()
	{
		try
		{
			removeMyListeners();

			MFSTextInputDialog commentInputJD = new MFSTextInputDialog(getParentFrame(),
					"Persistent Comment", 80); //$NON-NLS-1$
			commentInputJD.setTextAreaText(this.fieldComment);
			commentInputJD.setLocationRelativeTo(this);
			commentInputJD.setVisible(true);

			if (commentInputJD.getPressedEnter())
			{
				this.fieldComment = commentInputJD.getComment();
			}
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}

		addMyListeners();
		focusPNListOrButton(this.pbEditComment);
	}

	/**
	 * Executes the RTV_RM10 transaction and allows the user to select a
	 * location given by the output of the transaction.
	 */
	public void editLoc()
	{
		int rc = 0;
		String errorString = ""; //$NON-NLS-1$
		try
		{
			removeMyListeners();
			
			if (this.fieldLoc.length() > 0)
			{
				int index = 0;
				while (index < this.fieldComponentListModel.size())
				{
					MFSComponentRec cmp = this.fieldComponentListModel.getComponentRecAt(index);
					if (cmp.getIdsp().equals("I")) //$NON-NLS-1$
					{
						rc = 10;
						String erms = "Cannot Reset the Loc Value"; //$NON-NLS-1$
						IGSMessageBox.showOkMB(getParentFrame(), null, erms, null);
						break;
					}
					index++;
				}
			}

			if (rc == 0)
			{
				MfsXMLDocument xml_data1 = new MfsXMLDocument("RTV_RM10"); //$NON-NLS-1$
				xml_data1.addOpenTag("DATA"); //$NON-NLS-1$
				xml_data1.addCompleteField("BACK", "RMMALC"); //$NON-NLS-1$ //$NON-NLS-2$
				xml_data1.addCompleteField("BACK", "RMMILC"); //$NON-NLS-1$ //$NON-NLS-2$
				xml_data1.addCompleteField("BACK", "RMCMNT"); //$NON-NLS-1$ //$NON-NLS-2$

				xml_data1.addOpenTag("FLD"); //$NON-NLS-1$
				xml_data1.addCompleteField("OPRT", "="); //$NON-NLS-1$ //$NON-NLS-2$
				xml_data1.addCompleteField("NAME", "RMPRLN"); //$NON-NLS-1$ //$NON-NLS-2$
				xml_data1.addCompleteField("VALU", "'" + this.fieldHeaderRec.getPrln() + "'"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				xml_data1.addCloseTag("FLD"); //$NON-NLS-1$

				xml_data1.addOpenTag("FLD"); //$NON-NLS-1$
				xml_data1.addCompleteField("OPRT", "AND"); //$NON-NLS-1$ //$NON-NLS-2$
				xml_data1.addCompleteField("NAME", ""); //$NON-NLS-1$ //$NON-NLS-2$
				xml_data1.addCompleteField("VALU", ""); //$NON-NLS-1$ //$NON-NLS-2$
				xml_data1.addCloseTag("FLD"); //$NON-NLS-1$

				xml_data1.addOpenTag("FLD"); //$NON-NLS-1$
				xml_data1.addCompleteField("OPRT", "="); //$NON-NLS-1$ //$NON-NLS-2$
				xml_data1.addCompleteField("NAME", "RMUSGE"); //$NON-NLS-1$ //$NON-NLS-2$
				xml_data1.addCompleteField("VALU", "'DISPLAY'"); //$NON-NLS-1$ //$NON-NLS-2$
				xml_data1.addCloseTag("FLD"); //$NON-NLS-1$

				xml_data1.addOpenTag("FLD"); //$NON-NLS-1$
				xml_data1.addCompleteField("OPRT", "AND"); //$NON-NLS-1$ //$NON-NLS-2$
				xml_data1.addCompleteField("NAME", ""); //$NON-NLS-1$ //$NON-NLS-2$
				xml_data1.addCompleteField("VALU", ""); //$NON-NLS-1$ //$NON-NLS-2$
				xml_data1.addCloseTag("FLD"); //$NON-NLS-1$

				xml_data1.addOpenTag("FLD"); //$NON-NLS-1$
				xml_data1.addCompleteField("OPRT", "IN"); //$NON-NLS-1$ //$NON-NLS-2$
				xml_data1.addCompleteField("NAME", "RMNMBR"); //$NON-NLS-1$ //$NON-NLS-2$
				xml_data1.addCompleteField("VALU", "('*ALL','" + this.fieldHeaderRec.getNmbr() + "')");  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				xml_data1.addCloseTag("FLD"); //$NON-NLS-1$

				xml_data1.addCompleteField("ORBY", "RMCMNT"); //$NON-NLS-1$ //$NON-NLS-2$
				xml_data1.addCloseTag("DATA"); //$NON-NLS-1$
				xml_data1.finalizeXML();

				MFSTransaction rtv_rm10 = new MFSXmlTransaction(xml_data1.toString());
				rtv_rm10.setActionMessage("Retrieving List of Macro/Micro Locations Lines, Please Wait..."); //$NON-NLS-1$
				MFSComm.getInstance().execute(rtv_rm10, this);
				rc = rtv_rm10.getReturnCode();

				if (rc != 0)
				{
					errorString = rtv_rm10.getErms();
					IGSMessageBox.showOkMB(getParentFrame(), null, errorString, null);
				}
				else
				{
					MfsXMLParser xmlParser = new MfsXMLParser(rtv_rm10.getOutput());
					String tempRCD = ""; //$NON-NLS-1$

					try
					{
						tempRCD = xmlParser.getField("RCD"); //$NON-NLS-1$
						if (tempRCD.length() == 0)
						{
							rc = 10;
							String erms = "No Locations found ?"; //$NON-NLS-1$
							IGSMessageBox.showOkMB(getParentFrame(), null, erms, null);
						}
					}
					catch (MISSING_XML_TAG_EXCEPTION e)
					{
						rc = 10;
						String erms = "No Locations found ?"; //$NON-NLS-1$
						IGSMessageBox.showOkMB(getParentFrame(), null, erms, null);
					}
					if (rc == 0)
					{
						StringBuffer tempLocCollector = new StringBuffer();
						while (!tempRCD.equals("")) //$NON-NLS-1$
						{
							MfsXMLParser xmlParser1 = new MfsXMLParser(tempRCD);
							StringBuffer tempLocCollector1 = new StringBuffer(xmlParser1.getField("VALU")); //$NON-NLS-1$
							tempLocCollector1.append("/"); //$NON-NLS-1$
							tempLocCollector1.append(xmlParser1.getNextField("VALU")); //$NON-NLS-1$
							tempLocCollector1.append(" -> "); //$NON-NLS-1$
							tempLocCollector1.append((xmlParser1.getNextField("VALU")).substring(4)); //$NON-NLS-1$

							tempLocCollector.append(tempLocCollector1);

							tempRCD = xmlParser.getNextField("RCD"); //$NON-NLS-1$
						}
						MFSGenericListDialog myLocJD = new MFSGenericListDialog(
								getParentFrame(), "Select A Location from the List", //$NON-NLS-1$
								"Pick A Location"); //$NON-NLS-1$
						myLocJD.setSizeLarge();
						myLocJD.loadAnswerListModel(tempLocCollector.toString(), 100);
						myLocJD.setDefaultSelection(this.fieldLoc);
						myLocJD.setLocationRelativeTo(this);
						myLocJD.setVisible(true);

						if (myLocJD.getProceedSelected())
						{
							this.fieldLoc = myLocJD.getSelectedListOption();
							this.nlLoc.setText("LOC:"); //$NON-NLS-1$
							this.vlLoc.setText(this.fieldMovementOn + myLocJD.getSelectedListOption().substring(0, 20));
						}
					}
				}
			}// end of loc not already set
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}

		addMyListeners();
		focusPNListOrButton(this.pbEditLoc);

		//if they have a loc set, then automatically click the extra part button
		if (!this.nlLoc.getText().equals("")) //$NON-NLS-1$
		{
			this.pbExtraClogPart.doClick();
		}
	}

	/** Displays a dialog that lets the user toggle part movements on or off. */
	public void editMovement()
	{
		try
		{
			removeMyListeners();

			MFSGenericListDialog myMovementJD = new MFSGenericListDialog(
					getParentFrame(), "Turn Part Movements On/Off", "Choose On or Off"); //$NON-NLS-1$ //$NON-NLS-2$
			myMovementJD.setSizeSmall();
			myMovementJD.loadAnswerListModel("Movements On Movements Off", 13); //$NON-NLS-1$
			if (this.fieldMovementOn.equals("*")) //$NON-NLS-1$
			{
				myMovementJD.setDefaultSelection("Movements Off"); //$NON-NLS-1$
			}
			else
			{
				myMovementJD.setDefaultSelection("Movements On"); //$NON-NLS-1$
			}
			myMovementJD.setLocationRelativeTo(this);
			myMovementJD.setVisible(true);

			if (myMovementJD.getProceedSelected())
			{
				if (myMovementJD.getSelectedListOption().equals("Movements Off")) //$NON-NLS-1$
				{
					this.fieldMovementOn = "*"; //$NON-NLS-1$
				}
				else
				{
					this.fieldMovementOn = " "; //$NON-NLS-1$
				}

				//Tou Lee changed 5/11/2004
				if (0 < this.fieldLoc.length())
				{
					this.vlLoc.setText(this.fieldMovementOn
							+ this.fieldLoc.substring(0, 20));
				}
				else
				{
					this.vlLoc.setText(this.fieldMovementOn);
				}
			}
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}

		addMyListeners();
		focusPNListOrButton(this.pbMovement);
	}
	/**
	 * Ends FKIT.
	 * @param kitted_short <code>true</code> if the kit is short
	 * @return 0 on success; nonzero on failure
	 */
	public int end_fkit(boolean kitted_short)
	{
		int rc = 0;

		try
		{
			final MFSConfig config = MFSConfig.getInstance();

			String short_flag = "N"; //$NON-NLS-1$
			if (kitted_short)
			{
				short_flag = "Y"; //$NON-NLS-1$
			}

			StringBuffer data1 = new StringBuffer(43);
			data1.append("FKEND_RTR "); //$NON-NLS-1$
			data1.append(getCurrMctl());
			data1.append(config.get8CharCell());
			data1.append(config.get8CharCellType());
			data1.append(config.get8CharUser());
			data1.append(short_flag);

			StringBuffer amsg = new StringBuffer(46);
			amsg.append("Ending the Work Unit: "); //$NON-NLS-1$
			amsg.append(getCurrMctl());
			amsg.append(", Please Wait..."); //$NON-NLS-1$

			MFSTransaction fkend_rtr = new MFSFixedTransaction(data1.toString());
			fkend_rtr.setActionMessage(amsg.toString());
			MFSComm.getInstance().execute(fkend_rtr, this);
			rc = fkend_rtr.getReturnCode();

			if (rc == 0)
			{
				final String nmbrPrln = this.fieldHeaderRec.getNmbr() + "," //$NON-NLS-1$
						+ this.fieldHeaderRec.getPrln().trim();

				final String nmbrAll = this.fieldHeaderRec.getNmbr() + ",*ALL"; //$NON-NLS-1$

				/* Check if we need to print a Fab List */
				String cnfgDat1 = "FABLIST," + nmbrPrln; //$NON-NLS-1$
				String val = config.getConfigValue(cnfgDat1);
				if (val.equals(MFSConfig.NOT_FOUND))
				{
					String cnfgDat2 = "FABLIST," + nmbrAll; //$NON-NLS-1$
					val = config.getConfigValue(cnfgDat2);
				}
				if (!val.equals(MFSConfig.NOT_FOUND))
				{
					int qty = 1;
					if (!val.equals("")) //$NON-NLS-1$
					{
						qty = Integer.parseInt(val);
					}

					/* print Fab List */
					MFSPrintingMethods.rtvfab(getCurrMctl(), qty, getParentFrame());
				}

				/* Check if we need to print a Scsi Label */
				String cnfgDataSL = "SCSILBL," + nmbrPrln; //$NON-NLS-1$
				String val2 = config.getConfigValue(cnfgDataSL);
				if (val2.equals(MFSConfig.NOT_FOUND))
				{
					String cnfgDataSL2 = "SCSILBL," + nmbrAll; //$NON-NLS-1$
					val2 = config.getConfigValue(cnfgDataSL2);
				}
				if (!val2.equals(MFSConfig.NOT_FOUND))
				{
					int qty = 1;
					if (!val2.equals("")) //$NON-NLS-1$
					{
						qty = Integer.parseInt(val2);
					}

					/* print SCSI label */
					MFSPrintingMethods.rtvscsi(getCurrMctl(), qty, getParentFrame());
				}

				/* Check if we need to print a shpgrp */
				String cnfgDatSG = "RTVSHPGRP," + nmbrPrln; //$NON-NLS-1$
				String valSG = config.getConfigValue(cnfgDatSG);
				if (valSG.equals(MFSConfig.NOT_FOUND))
				{
					String cnfgDatSG2 = "RTVSHPGRP," + nmbrAll; //$NON-NLS-1$
					valSG = config.getConfigValue(cnfgDatSG2);
				}
				if (!valSG.equals(MFSConfig.NOT_FOUND))
				{
					valSG += "        "; //$NON-NLS-1$
					int qty = 1;
					String type = valSG.substring(0, 1);
					if (!valSG.substring(2, 6).equals("    ")) //$NON-NLS-1$
					{
						qty = Integer.parseInt(valSG.substring(2, 6).trim());
					}

					/* print shipgroup List */
					MFSPrintingMethods.rtvshpgrp(getCurrMctl(), type, qty, getParentFrame());
				}

				/* Check if we need to print a fcmes packing list */
				String cnfgDatFC = "RTVFCMES," + nmbrPrln; //$NON-NLS-1$
				String valFC = config.getConfigValue(cnfgDatFC);
				if (valFC.equals(MFSConfig.NOT_FOUND))
				{
					String cnfgDatFC2 = "RTVFCMES," + nmbrAll; //$NON-NLS-1$
					valFC = config.getConfigValue(cnfgDatFC2);
				}
				if (!valFC.equals(MFSConfig.NOT_FOUND))
				{
					valFC += "        "; //$NON-NLS-1$
					int qty = 1;
					String type = valFC.substring(0, 1);

					if (!valFC.substring(2, 6).equals("    ")) //$NON-NLS-1$
					{
						qty = Integer.parseInt(valFC.substring(2, 6).trim());
					}

					/* print fcmes packing list */
					MFSPrintingMethods.rtvfcmes(getCurrMctl(), type, qty, getParentFrame());
				}

				/* Check if we need to print a rochester shpgrp */
				String cnfgDatRSG = "RCHSHPGRP," + nmbrPrln; //$NON-NLS-1$
				String valRSG = config.getConfigValue(cnfgDatRSG);
				if (valRSG.equals(MFSConfig.NOT_FOUND))
				{
					String cnfgDatRSG2 = "RCHSHPGRP," + nmbrAll; //$NON-NLS-1$
					valRSG = config.getConfigValue(cnfgDatRSG2);
				}
				if (!valRSG.equals(MFSConfig.NOT_FOUND))
				{
					valRSG += "        "; //$NON-NLS-1$
					int qty = 1;
					String type = valRSG.substring(0, 1);
					if (!valRSG.substring(2, 6).equals("    ")) //$NON-NLS-1$
					{
						qty = Integer.parseInt(valRSG.substring(2, 6).trim());
					}

					/* print shipgroup List */
					MFSPrintingMethods.rchshpgrp(getCurrMctl(), type, qty,getParentFrame());
				}

				/* Check if we need to print a rochester fcmes packing list */
				String cnfgDatRFC = "RCHFCMES," + nmbrPrln; //$NON-NLS-1$
				String valRFC = config.getConfigValue(cnfgDatRFC);
				if (valRFC.equals(MFSConfig.NOT_FOUND))
				{
					String cnfgDatRFC2 = "RCHFCMES," + nmbrAll; //$NON-NLS-1$
					valRFC = config.getConfigValue(cnfgDatRFC2);
				}
				if (!valRFC.equals(MFSConfig.NOT_FOUND))
				{
					valRFC += "        "; //$NON-NLS-1$
					int qty = 1;
					String type = valRFC.substring(0, 1);

					if (!valRFC.substring(2, 6).equals("    ")) //$NON-NLS-1$
					{
						qty = Integer.parseInt(valRFC.substring(2, 6).trim());
					}

					/* print fcmes packing list */
					MFSPrintingMethods.rchfcmes(getCurrMctl(), type, qty, getParentFrame());
				}
				
				/* Check if we need to print a rochester ffbm label */
				String cnfgDatFB = "RTVFFBMLBL," + nmbrPrln; //$NON-NLS-1$
				String valFB = config.getConfigValue(cnfgDatFB);
				if (valFB.equals(MFSConfig.NOT_FOUND))
				{
					String cnfgDatFB2 = "RTVFFBMLBL," + nmbrAll; //$NON-NLS-1$
					valFB = config.getConfigValue(cnfgDatFB2);
				}
				if (!valFB.equals(MFSConfig.NOT_FOUND))
				{
					int qty = 1;
					if (!valFB.equals("")) //$NON-NLS-1$
					{
						qty = Integer.parseInt(valFB);
					}

					/* print ffbm label */
					MFSPrintingMethods.rtvffbm2(getCurrMctl(), qty, getParentFrame());
				}

				/* Check if we need to print a POK fru label */
				String cnfgDatPFL = "POKFRULBL," + nmbrPrln; //$NON-NLS-1$
				String valPFL = config.getConfigValue(cnfgDatPFL);
				if (valPFL.equals(MFSConfig.NOT_FOUND))
				{
					String cnfgDatPFL2 = "POKFRULBL," + nmbrAll; //$NON-NLS-1$
					valPFL = config.getConfigValue(cnfgDatPFL2);
				}
				if (!valPFL.equals(MFSConfig.NOT_FOUND))
				{
					int qty = 1;
					if (!valPFL.equals("")) //$NON-NLS-1$
					{
						qty = Integer.parseInt(valPFL);
					}

					/* print pok fru label */
					MFSPrintingMethods.frunumb(this.fieldHeaderRec.getPrln(), "J", //$NON-NLS-1$
							getCurrMctl(), qty, getParentFrame());
				}

				/* Check if we need to print a Roch fru label */
				String cnfgDatRFL = "ROCHFRULBL," + nmbrPrln; //$NON-NLS-1$
				String valRFL = config.getConfigValue(cnfgDatRFL);
				if (valRFL.equals(MFSConfig.NOT_FOUND))
				{
					String cnfgDatRFL2 = "ROCHFRULBL," + nmbrAll; //$NON-NLS-1$
					valRFL = config.getConfigValue(cnfgDatRFL2);
				}
				if (!valRFL.equals(MFSConfig.NOT_FOUND))
				{
					int qty = 1;
					if (!valRFL.equals("")) //$NON-NLS-1$
					{
						qty = Integer.parseInt(valRFL);
					}

					/* print roch fru label */
					String pn = "00000" + this.fieldHeaderRec.getPrln().trim(); //$NON-NLS-1$
					MFSPrintingMethods.rochfru("FRU     ", pn, getCurrMctl(), "J", qty, getParentFrame());  //$NON-NLS-1$ //$NON-NLS-2$
				}

				/* Check if we need to print a Roch MIR label */
				String cnfgDatMRR = "ROCHMIRRLBL," + nmbrPrln; //$NON-NLS-1$
				String valMRR = config.getConfigValue(cnfgDatMRR);
				if (valMRR.equals(MFSConfig.NOT_FOUND))
				{
					String cnfgDatMRR2 = "ROCHMIRRLBL," + nmbrAll; //$NON-NLS-1$
					valMRR = config.getConfigValue(cnfgDatMRR2);
				}
				if (!valMRR.equals(MFSConfig.NOT_FOUND))
				{
					int qty = 1;
					if (!valMRR.equals("")) //$NON-NLS-1$
					{
						qty = Integer.parseInt(valMRR);
					}

					/* print roch mir label */
					String part = "00000" + this.fieldHeaderRec.getPrln().substring(0, 7); //$NON-NLS-1$
					MFSPrintingMethods.mir(config.get8CharCellType(), part,
							getCurrMctl(), qty, getParentFrame());
				}

				/* Check if we need to print an 11s barcode label */
				String cnfgDat11S = "11SBARLBL," + nmbrPrln; //$NON-NLS-1$
				String val11S = config.getConfigValue(cnfgDat11S);
				if (val11S.equals(MFSConfig.NOT_FOUND))
				{
					String cnfgDat11S2 = "11SBARLBL," + nmbrAll; //$NON-NLS-1$
					val11S = config.getConfigValue(cnfgDat11S2);
				}
				if (!val11S.equals(MFSConfig.NOT_FOUND))
				{
					int qty = 1;
					if (!val11S.equals("")) //$NON-NLS-1$
					{
						qty = Integer.parseInt(val11S);
					}

					/* print 11s barcode label */
					String part = "00000" + this.fieldHeaderRec.getPrln().substring(0, 7); //$NON-NLS-1$
					MFSPrintingMethods.elevens(part, getCurrMctl(), qty, getParentFrame());
				}
			}
			else
			{
				IGSMessageBox.showOkMB(getParentFrame(), null, fkend_rtr.getErms(), null);
			}
		}
		catch (Exception e)
		{
			rc = 10;
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);

			addMyListeners();
			focusPNListOrButton(this.pbEnd);
		}
		return rc;
	}
	/* ~41
	 * checks if mctl is complete in current operation
	 * @param completeArray array of <code>boolean</code>
	 * 0 is for complete
	 * 1 remove
	 * 2 mandatoryInstNotComplete
	 */
	public void checkIfComplete(boolean completeArray[])
	{
		//look for instructions that are incomplete and mandatory
		int instIndex = 0;
		int index = 0;
		boolean complete = true;
		boolean remove = false;
		boolean mandatoryInstNotComplete = false;
		boolean hideMode = this.isHideMode();
		MFSComponentRec cmp;
		
		while (instIndex < this.fieldInstVector.size())
		{
			MFSInstructionRec instr = getInstVectorElementAt(instIndex);
			final String instClass = instr.getInstrClass();
			if ((instClass.equals("M") || instClass.equals("A")) //$NON-NLS-1$ //$NON-NLS-2$
					&& !instr.getCompletionStatus().equals("C")) //$NON-NLS-1$
			{
				mandatoryInstNotComplete = true;
				break; //~24A
			}
			instIndex++;
		}

		//look for non part instructions that are incomplete (if in show mode)
		if (!hideMode)
		{
			int row = 0;
			while (row < this.fieldRowVector.size())
			{
				MFSPartInstructionJPanel pip = getRowVectorElementAt(row);
				if (pip.getIsNonPartInstruction()
						&& !pip.getInstructionRec().getCompletionStatus().equals("C")) //$NON-NLS-1$
				{
					mandatoryInstNotComplete = true;
					break; //~24A
				}
				row++;
			}
		}

		while (index < this.fieldComponentListModel.size())
		{
			cmp = this.fieldComponentListModel.getComponentRecAt(index);
			final String idsp = cmp.getIdsp();
			if (!(idsp.equals("I") || idsp.equals("D") //$NON-NLS-1$ //$NON-NLS-2$
					|| (idsp.equals("A") && cmp.getMlri().equals(" ")) //$NON-NLS-1$ //$NON-NLS-2$
					|| (idsp.equals("A") && cmp.getMlri().equals("0")))) //$NON-NLS-1$ //$NON-NLS-2$
			{
				complete = false;
				if (idsp.equals("R")) //$NON-NLS-1$
				{
					remove = true;
				}
			}
			index++;
		}
		completeArray[LT_COMPLETE_CHECK_COMPLETE] = complete;
		completeArray[LT_COMPLETE_CHECK_REMOVE] = remove;
		completeArray[LT_COMPLETE_CHECK_MANDATORY] = mandatoryInstNotComplete;
	}
	
	/*~50A Changes for Part Weight Dialog 
	 * Call RTV_PRTWGT for getting the parts to be collected
	 */
	public int retrievePartWeight() throws MFSException
	{
		int rc=0;		
		String lblWeightUnit="";
		Vector<String> parts;
		int blanksWgt=0;
		String hardStop=""; 
		Boolean isHardStop = false;
		
		MFSConfig.getInstance().removeConfigEntry("WEIGHTSCOMPLETE"); //~51A
		
		if(!MFSConfig.getInstance().getConfigValue("PARTWEIGHTUOM").trim().equals("Not Found"))
		{
			lblWeightUnit = (MFSConfig.getInstance().getConfigValue("PARTWEIGHTUOM"));
		}
		else
		{
			lblWeightUnit = "KG";
		}
		
		IGSXMLTransaction rtv_prtwgt = new IGSXMLTransaction("RTV_PRTWGT"); //$NON-NLS-1$
		rtv_prtwgt.setActionMessage("Retrieving Part Weight...");
		rtv_prtwgt.startDocument();
		rtv_prtwgt.addElement("UOM", lblWeightUnit.trim().toUpperCase()); //$NON-NLS-1$ //$NON-NLS-2$
		rtv_prtwgt.addElement("QUERYLEVEL", "0"); //$NON-NLS-1$
		rtv_prtwgt.addElement("BRANCHOFFICE", this.fieldHeaderRec.getBrof()); //$NON-NLS-1$
		rtv_prtwgt.addElement("MCTL",getCurrMctl()); //$NON-NLS-1$
		rtv_prtwgt.endDocument(); 
		System.out.println(rtv_prtwgt.toString());
		
		MFSComm.getInstance().execute(rtv_prtwgt, this);
		if (rtv_prtwgt.getReturnCode() == 0)
		{
			parts = new Vector<String>();
			
			rtv_prtwgt.getElements(parts,"PARTS","ITEM");
			parts.trimToSize();
			try
			{
				blanksWgt=Integer.parseInt(rtv_prtwgt.getNextElement("BLANKWEIGHTS").trim());
			}catch (NumberFormatException e){
				e.printStackTrace();
				throw new MFSException(e.getMessage());
			}
	        	
			if(blanksWgt > 0)
			{
				hardStop=rtv_prtwgt.getNextElement("HDSTP").trim();
				if(hardStop.trim().equals("1"))
				{
					isHardStop= true;
				}
				try
				{
					//removeMyListeners();
					MFSMultiPartWeightDialog multiPartWeightDialog = new MFSMultiPartWeightDialog(getParentFrame(), this,parts);
					multiPartWeightDialog.display();
					
					if(!MFSConfig.getInstance().getConfigValue("WEIGHTSCOMPLETE").trim().equals("TRUE"))
					{
						if(isHardStop)
						{
							rc=1;
							MFSConfig.getInstance().removeConfigEntry("WEIGHTSCOMPLETE");
						}
					}
				}
				catch (Exception e)
				{
					IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
				}
				/*finally
				{
					addMyListeners();
				}*/
			}
		}
		else
		{
			String erms =rtv_prtwgt.getErms();
			String title = "Part Weight Error!";
			IGSMessageBox.showOkMB(this, title, erms, null);
			rc=1;
		}
		return rc;
	
	}
	
	/*
	 * Dave Fichtinger - add call to updt_wusn if header_rec.awsa is 'Y'
	 * Dave Fichtinger - add listeners back if verifyPendingParts() fails - PTR 30486DL
	 * Dave Fichtinger - add logic to split the partsOrders off - 29845MZ @1
	 * Dave Fichtinger - redo partsOrder splitting logic - 31201MZ @2
	 */
	/** Validates the work unit can be ended and invokes the correct method to end it. */
	public void end_work()
	{
		//Variable to determine if we will continue after Inspection questions are asked.
		//Initialize to true in case there are no Inspection questions
		boolean iq_continue = true;
		boolean kitted_short = false;
		boolean cancelRequest = false;  //~41
		boolean[] completeArray = new boolean[3];
		boolean complete = true;
		boolean remove = false;
		boolean mandatoryInstNotComplete = false;		
		final MFSConfig config = MFSConfig.getInstance();
		int rc = 0;
		
		try
		{
			removeMyListeners();
			//Efficiency Changes, we will reusue this checkIfComplete function ~41
			checkIfComplete(completeArray);

			complete = completeArray[LT_COMPLETE_CHECK_COMPLETE];
			remove = completeArray[LT_COMPLETE_CHECK_REMOVE];
			mandatoryInstNotComplete = completeArray[LT_COMPLETE_CHECK_MANDATORY];
			    
			if (remove)
			{
				if (this.fieldHeaderRec.getNmbr().equals("FKIT")) //$NON-NLS-1$
				{
					int n = JOptionPane.showConfirmDialog(
							this,
							"Some Components Were Not Removed.  Do you still want to end this KIT ??", //$NON-NLS-1$
							"End Work", JOptionPane.YES_NO_OPTION); //$NON-NLS-1$
					if (n == JOptionPane.YES_OPTION)
					{
						remove = false;
					}
					else
					{
						cancelRequest = true;
						addMyListeners();
						focusPNListOrButton(this.pbEnd);
					}
				}
				else
				{
					String erms = "Cannot End this Operation, Parts need to be REMOVED!"; //$NON-NLS-1$
					IGSMessageBox.showOkMB(getParentFrame(), null, erms, null);

					addMyListeners();
					focusPNListOrButton(this.pbEnd);
				}
			}

			if (!remove)
			{
				//if we are complete (all parts installed), but mandatory
				// instructions incomplete, then give error
				if (mandatoryInstNotComplete)
				{
					String erms = "Cannot End this Operation until All Mandatory Instructions Are Complete!"; //$NON-NLS-1$
					IGSMessageBox.showOkMB(getParentFrame(), null, erms, null);

					focusPNListOrButton(this.pbEnd);
				}
				else if (!complete && this.fieldHeaderRec.getNmbr().equals("FKIT")) //$NON-NLS-1$
				{
					int n = JOptionPane.showConfirmDialog(
							this,
							"Some Components Were Not Installed.  Do you want to complete this KIT short ??", //$NON-NLS-1$
							"End Work", JOptionPane.YES_NO_OPTION); //$NON-NLS-1$
					if (n == JOptionPane.YES_OPTION)
					{
						kitted_short = true;
						complete = true;
					}
					else
					{
						cancelRequest = true;
					}
				}
				else if (!complete
						&& (!this.fieldHeaderRec.getAllp().equals("Y") //$NON-NLS-1$ 
								|| this.fieldHeaderRec.getTypz().equals("O"))) //$NON-NLS-1$
				{
					int n = JOptionPane.showConfirmDialog(
							this,
							"Work Unit is NOT Complete.  Do you really want to END this Work Unit?", //$NON-NLS-1$
							"End Work", JOptionPane.YES_NO_OPTION); //$NON-NLS-1$
					if (n == JOptionPane.YES_OPTION)
					{
						int index = 0;
						int shortRow = 0;
						MFSComponentListModel tmpLm;
						MFSComponentRec cmp;
						while (shortRow < this.fieldRowVector.size())
						{
							MFSPartInstructionJPanel pip = getRowVectorElementAt(shortRow);
							if (!pip.getIsNonPartInstruction())
							{
								index = 0;
								tmpLm = pip.getPNListModel();
								while (index < tmpLm.size())
								{
									cmp = tmpLm.getComponentRecAt(index);
									if (this.fieldHeaderRec.getTypz().equals("O")) //$NON-NLS-1$
									{
										//part is in parts order and mlr part,
										//need to set those to 'D' idsp too
										//if they are partially installed
										//handle later
										int fqty = Integer.parseInt(cmp.getFqty());

										if (cmp.getIdsp().equals("A") //$NON-NLS-1$
												|| cmp.getIdsp().equals("X")) //$NON-NLS-1$
										{
											if (fqty == 0)
											{
												cmp.setRec_changed(true);
												cmp.setIdsp("D"); //$NON-NLS-1$
												cmp.setShtp(" "); //$NON-NLS-1$
												cmp.updtDisplayString();
												cmp.updtIRDisplayString();
												this.redoLayout(shortRow);
											}
											else
											{
												MFSComponentRec newCr = split_cr(cmp);
												if (newCr != null)
												{
													newCr.setIdsp("D"); //$NON-NLS-1$
													newCr.setShtp(" "); //$NON-NLS-1$
													newCr.updtDisplayString();
													newCr.updtIRDisplayString();
													tmpLm.add(index + 1, newCr);
													this.redoLayout(shortRow);
												}
											}
										}
									}//end of typz == 'O'

									else if (!(cmp.getIdsp().equals("I") || cmp.getIdsp().equals("D") //$NON-NLS-1$ //$NON-NLS-2$
											|| cmp.getShtp().equals("1") //$NON-NLS-1$
											|| (cmp.getIdsp().equals("A") && cmp.getMlri().equals(" ")) //$NON-NLS-1$ //$NON-NLS-2$ 
											|| (cmp.getIdsp().equals("A") && cmp.getMlri().equals("0")))) //$NON-NLS-1$ //$NON-NLS-2$
									{
										this.fieldActiveRow = shortRow;
										short_cr(cmp, index);
										this.redoLayout(shortRow);
									}
									index++;
								}
							}
							shortRow++;
						}
						complete = true;
					}
					else
					{
						cancelRequest = true;
					}
				}

				if (!cancelRequest && !complete && this.fieldHeaderRec.getAllp().equals("Y")) //$NON-NLS-1$
				{
					String erms = "Cannot End this Operation until Work Unit is Complete!"; //$NON-NLS-1$
					IGSMessageBox.showOkMB(getParentFrame(), null, erms, null);

					addMyListeners();
					focusPNListOrButton(this.pbEnd);
				}
				else if (!cancelRequest && complete && !mandatoryInstNotComplete)
				{
					/* now check if we needed to unburn, that we did unburn */
					if (this.pbUnburn.isEnabled()
							&& (config.getConfigValue("UNBURNED").equals("NO"))) //$NON-NLS-1$ //$NON-NLS-2$
					{
						String erms = "You must Unburn before you can end this operation"; //$NON-NLS-1$
						IGSMessageBox.showOkMB(getParentFrame(), null, erms, null);
						addMyListeners();
						focusPNListOrButton(this.pbEnd);
					}
					else if (this.pbBurn.isEnabled()
							&& (config.getConfigValue("BURNED").equals("NO"))) //$NON-NLS-1$ //$NON-NLS-2$
					{
						String erms = "You must Burn before you can end this operation"; //$NON-NLS-1$
						IGSMessageBox.showOkMB(getParentFrame(), null, erms, null);
						addMyListeners();
						focusPNListOrButton(this.pbEnd);
					}
					else if (this.pbDasdPersonalize.isEnabled()
							&& (config.getConfigValue("PERSONALIZED").equals("NO"))) //$NON-NLS-1$ //$NON-NLS-2$
					{
						String erms = "You must Personalize the DASD before you can end this operation"; //$NON-NLS-1$
						IGSMessageBox.showOkMB(getParentFrame(), null, erms, null);
						addMyListeners();
						focusPNListOrButton(this.pbEnd);
					}
					else if (this.pbDasdUnpersonalize.isEnabled()
							&& (config.getConfigValue("UNPERSONALIZED").equals("NO"))) //$NON-NLS-1$ //$NON-NLS-2$
					{
						String erms = "You must Unpersonalize the DASD before you can end this operation"; //$NON-NLS-1$
						IGSMessageBox.showOkMB(getParentFrame(), null, erms, null);
						addMyListeners();
						focusPNListOrButton(this.pbEnd);
					}
					else
					{
						if (this.pbResetInterposer.isEnabled())
						{
							int i = 0;
							boolean found = false;
							while (i < this.fieldComponentListModel.size() && !found)
							{
								MFSComponentRec next = this.fieldComponentListModel.getComponentRecAt(i);
								if (next.getIdsp().equals("I") //$NON-NLS-1$
										&& !next.getInterposerResetFlag().equals(">")) //$NON-NLS-1$
								{
									found = true;
								}
								i++;
							}
							if (found)
							{
								rc = 10;
								String erms = "Interposer Count Has Not Been Reset For All Parts!"; //$NON-NLS-1$
								IGSMessageBox.showOkMB(getParentFrame(), null, erms, null);
								addMyListeners();
								focusPNListOrButton(this.pbEnd);
							}
						}

						if (rc == 0)
						{
							if (checkForPending())
							{
								rc = verifyPendingParts();
								/* need to put listeners back in if this fails */
								if (rc != 0)
								{
									addMyListeners();
									focusPNListOrButton(this.pbEnd);
								}
							}
						}

						/* check to see what WWID field is set to */
						if (rc == 0 && this.fieldHeaderRec.getWwid().equals("WWIDError       ")) //$NON-NLS-1$
						{
							rc = 10;
							String erms = "WWID Has Not Been Assigned!"; //$NON-NLS-1$
							IGSMessageBox.showOkMB(getParentFrame(), null, erms, null);
							addMyListeners();
							focusPNListOrButton(this.pbEnd);
						}
                        
                        
						if (rc == 0)
						{
							/*
							 * Perform the logic for retrieving inspection
							 * questions for this operation. If the Ispq flag in
							 * the header is set to "B" for both or "A" for
							 * start, then we will go to the server to find the
							 * inspection questions.
							 */
							if (this.fieldHeaderRec.getIspq().equals("B") //$NON-NLS-1$
									|| this.fieldHeaderRec.getIspq().equals("A")) //$NON-NLS-1$
							{
								RTV_IQ rtvIQ = new RTV_IQ(this); //~45
								rtvIQ.setInputNmbr(this.fieldHeaderRec.getNmbr());
								rtvIQ.setInputPrln(this.fieldHeaderRec.getPrln());
								rtvIQ.setInputProd(this.fieldHeaderRec.getProd());
								rtvIQ.setInputX("2"); //$NON-NLS-1$
								rtvIQ.execute();					
								rc = rtvIQ.getReturnCode();
								
								if (rc != 0)
								{
									//don't allow the program to get by the inspection questions
									iq_continue = false;
									IGSMessageBox.showOkMB(getParentFrame(), null, rtvIQ.getErrorMessage(), null);
								}
								else
								{
									MFSIQDialog iq = new MFSIQDialog(getParentFrame());
									iq.loadIQ(rtvIQ.getOutputIQ());
									iq.setLocationRelativeTo(getParentFrame()); //~17A
									iq.setVisible(true);
									if (iq.getPressedCancel())
									{
										iq_continue = false;
									}
								}
							} // end of Ispq = 'B' or 'S'

							if (iq_continue)
							{
								final String nmbrPrln = this.fieldHeaderRec.getNmbr() + "," //$NON-NLS-1$
									+ this.fieldHeaderRec.getPrln().trim();

								final String nmbrAll = this.fieldHeaderRec.getNmbr() + ",*ALL"; //$NON-NLS-1$
								
								/* Check if we need to print a POK track sub label*/
								String cnfgDatTS = "TRACKSUB2LBL," + nmbrPrln; //$NON-NLS-1$
								String valTS = config.getConfigValue(cnfgDatTS);
								if (valTS.equals(MFSConfig.NOT_FOUND))
								{
									String cnfgDatTS2 = "TRACKSUB2LBL," + nmbrAll; //$NON-NLS-1$
									valTS = config.getConfigValue(cnfgDatTS2);
								}
								if (!valTS.equals(MFSConfig.NOT_FOUND))
								{
									int qty = 1;
									if (!valTS.equals("")) //$NON-NLS-1$
									{
										qty = Integer.parseInt(valTS);
									}

									/* print tracksub label */
									MFSPrintingMethods.tracksub2(this.fieldHeaderRec.getPrln(),
											"J", getCurrMctl(), qty, getParentFrame()); //$NON-NLS-1$
								}

								/* Check if we need to verify the fab serial */
								boolean pressCancelFabSerial = false;
								String cnfgData1 = "VERIFYFABSERIAL," + nmbrPrln; //$NON-NLS-1$
								String value = config.getConfigValue(cnfgData1);
								if (value.equals(MFSConfig.NOT_FOUND))
								{
									String cnfgData2 = "VERIFYFABSERIAL," + nmbrAll; //$NON-NLS-1$
									value = config.getConfigValue(cnfgData2);
								}
								if (!value.equals(MFSConfig.NOT_FOUND))
								{
									MFSVrfyFabSerialDialog myVFSJD = new MFSVrfyFabSerialDialog(
											getParentFrame(), this.fieldHeaderRec, getCurrMctl());
									myVFSJD.setLocationRelativeTo(getParentFrame()); //~17A
									myVFSJD.setVisible(true);
									pressCancelFabSerial = myVFSJD.getPressedCancel();
								}

								/* Check if we need to validate VPD label */
								boolean pressCancelValidateVPD = false;
								String cnfgData3 = "VALIDATEVPDLBL," + nmbrPrln; //$NON-NLS-1$
								String value2 = config.getConfigValue(cnfgData3);
								if (value2.equals(MFSConfig.NOT_FOUND))
								{
									String cnfgData4 = "VALIDATEVPDLBL," + nmbrAll; //$NON-NLS-1$
									value2 = config.getConfigValue(cnfgData4);
								}
								if (!value2.equals(MFSConfig.NOT_FOUND))
								{
									MFSVpdValidateDialog myVPDJD = new MFSVpdValidateDialog(
											getParentFrame(), this.fieldHeaderRec, getCurrMctl());
									myVPDJD.setLocationRelativeTo(getParentFrame());  //~17A
									myVPDJD.setVisible(true);
									pressCancelValidateVPD = myVPDJD.getPressedCancel();
								}

								if ((!pressCancelFabSerial) && (!pressCancelValidateVPD))
								{
									if (this.fieldHeaderRec.getNmbr().equals("FKIT")) //$NON-NLS-1$
									{
										rc = end_fkit(kitted_short);
										this.fieldEndCode = rc;
										if (rc == 0)
										{
											addMyListeners();
											restorePreviousScreen();
										}
									}
									/* not fkit */
									else
									{
										//~24A Call updateWeightDim
										rc = updateWeightDim("E"); //$NON-NLS-1$
										if(rc == 0)
										{
											rc = locIt("E"); //$NON-NLS-1$
										}
										if (rc == 0)
										{
											rc = updt_crwc();
										}
										if (rc == 0)
										{
											rc = updt_instr("C"); //$NON-NLS-1$
										}
										if (rc == 0)
										{
											if (this.fieldHeaderRec.getAwsa().equals("Y")) //$NON-NLS-1$
											{
												rc = updt_wusn();
											}
										}
										/*~50A Changes for Part Weight Dialog 
										 * Call RTV_PRTWGT for getting the parts to be collected
										*/
										if (rc== 0) 
						                {
				                        	rc= retrievePartWeight();
				                        	if(rc != 0)
				                        	{
				                        		IGSMessageBox.showOkMB(getParentFrame(), "All weights must be entered", 
				                        				"All part weights must be entered before ending the work unit!", null);
				                        	}
				                        }
										
										if (rc == 0)
										{
											rc = end_wu("C", "00", "0000"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
											this.fieldEndCode = rc;
											if (rc == 30 || rc == 31)
											{
												addMyListeners();
												this.setCurrentCompRec(null);

												restorePreviousScreen();
											}
											else
											{
												addMyListeners();
												focusPNListOrButton(this.pbEnd);
											}
										}/* end of rc check */
										else
										{
											addMyListeners();
											focusPNListOrButton(this.pbEnd);
										}
									}//end of not fkit
								}// end !pressedCancel
								else
								{
									addMyListeners();
									focusPNListOrButton(this.pbEnd);
								}
							} // end of iq_continue check
							else
							{
								addMyListeners();
								focusPNListOrButton(this.pbEnd);
							}
						}//end of good rc == 0 check
					} /* got past unburn check */
				}//end of complete and no mandatoryInstructions Left to acknowledge
				else
				{
					addMyListeners();
					focusPNListOrButton(this.pbEnd);
				}
			}//end of parts need to be removed yet
		}
		catch (Exception e)
		{
			this.setCursor(Cursor.getDefaultCursor());
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);

			addMyListeners();
			focusPNListOrButton(this.pbEnd);
		}
	}

	/* 11/17/2004 | PTR 29318PT | TL Moua  @1 |-Display a list of error msgs when rc=201 */
	/**
	 * Ends the work unit.
	 * @param ecod the end code ('C'omplete, 'F'ail, or 'S'uspend)
	 * @param qcod
	 * @param oemo the operation ending mode
	 * @return the return code
	 */
	public int end_wu(String ecod, String qcod, String oemo)
	{
		int rc = 0;
		boolean warning = false;

		try
		{
			//~46A - Start
			if(!vfyHazmatLabeling(ecod))
			{
				if(ecod.equals("C"))
				{
					String msg = "Error: Verify all hazmat label part numbers to end the operation.";
					IGSMessageBox.showOkMB(getParentFrame(), null, msg, null);
					return -1;
				}		
			}
			//~46A - End
			
			final MFSConfig config = MFSConfig.getInstance();

			/* Clear down bin parts list */
			this.fieldDownBinList.clear();

			/* start the END_WU transaction thread */
			String who = ""; //$NON-NLS-1$
			if (this.fieldHeaderRec.getNmbr().equals("TEAR")) //$NON-NLS-1$
			{
				who = "Z"; //$NON-NLS-1$
			}
			else
			{
				who = "J"; //$NON-NLS-1$
			}

			String cmt = (this.fieldComment + "                                                                                ").substring(0, 80); //$NON-NLS-1$
			String loc = (this.fieldLoc + "                    ").substring(0, 20); //$NON-NLS-1$
			String movement = (this.fieldMovementOn + " ").substring(0, 1); //$NON-NLS-1$
			if (movement.equals("*")) //$NON-NLS-1$
			{
				movement = "Y"; //$NON-NLS-1$
			}

			StringBuffer buffer = new StringBuffer();
			buffer.append("END_WU    "); //$NON-NLS-1$
			buffer.append(ecod);
			buffer.append(qcod);
			buffer.append(config.get8CharCellType());
			buffer.append(config.get8CharCell());
			buffer.append(getCurrMctl());
			buffer.append(config.get8CharUser());
			buffer.append(this.fieldHeaderRec.getOlev());
			buffer.append(this.fieldHeaderRec.getNmbr());
			buffer.append(oemo);
			buffer.append(who);
			buffer.append(cmt);
			buffer.append(loc.substring(0, 7));
			buffer.append(loc.substring(8, 20));
			buffer.append(movement);
			buffer.append("    "); //$NON-NLS-1$
			buffer.append("    "); //$NON-NLS-1$
			buffer.append(this.fieldEndOrSuspendLoc);

			MFSTransaction end_wu = new MFSFixedTransaction(buffer.toString());
			if (ecod.equals("S")) //$NON-NLS-1$
			{
				end_wu.setActionMessage("Suspending the Work Unit, Please Wait..."); //$NON-NLS-1$
			}
			else if (ecod.equals("F")) //$NON-NLS-1$
			{
				end_wu.setActionMessage("Failing the Work Unit, Please Wait..."); //$NON-NLS-1$
			}
			else
			{
				end_wu.setActionMessage("Ending the Work Unit, Please Wait..."); //$NON-NLS-1$
			}
			MFSComm.getInstance().execute(end_wu, this);
			rc = end_wu.getReturnCode();

			/* @1A */
			if (rc == 130 || rc == 131)
			{
				rc = rc - 100;
				warning = true;
			}

			if (ecod.equals("C") && ((rc == 30) || (rc == 31) || (rc == 100))) //$NON-NLS-1$
			{
				if (warning) /* (rc == 100) @1C */
				{
					showPNSNErrorList(end_wu.getOutput(), 0);
				}

				final String nmbrPrln = this.fieldHeaderRec.getNmbr() + "," //$NON-NLS-1$
						+ this.fieldHeaderRec.getPrln().trim();

				final String nmbrAll = this.fieldHeaderRec.getNmbr() + ",*ALL"; //$NON-NLS-1$

				/* Check if we need to print a Fab List */
				String cnfgDat1 = "FABLIST," + nmbrPrln; //$NON-NLS-1$
				String val = config.getConfigValue(cnfgDat1);
				if (val.equals(MFSConfig.NOT_FOUND))
				{
					String cnfgDat2 = "FABLIST," + nmbrAll; //$NON-NLS-1$
					val = config.getConfigValue(cnfgDat2);
				}
				if (!val.equals(MFSConfig.NOT_FOUND))
				{
					int qty = 1;
					if (!val.equals("")) //$NON-NLS-1$
					{
						qty = Integer.parseInt(val);
					}

					/* print Fab List */
					MFSPrintingMethods.rtvfab(getCurrMctl(), qty, getParentFrame());
				}

				/* Check if we need to print a Scsi Label */
				String cnfgDataSL = "SCSILBL," + nmbrPrln; //$NON-NLS-1$
				String val2 = config.getConfigValue(cnfgDataSL);
				if (val2.equals(MFSConfig.NOT_FOUND))
				{
					String cnfgDataSL2 = "SCSILBL," + nmbrAll; //$NON-NLS-1$
					val2 = config.getConfigValue(cnfgDataSL2);
				}
				if (!val2.equals(MFSConfig.NOT_FOUND))
				{
					int qty = 1;
					if (!val2.equals("")) //$NON-NLS-1$
					{
						qty = Integer.parseInt(val2);
					}

					/* print SCSI label */
					MFSPrintingMethods.rtvscsi(getCurrMctl(), qty, getParentFrame());
				}

				/* Check if we need to print a shpgrp */
				String cnfgDatSG = "RTVSHPGRP," + nmbrPrln; //$NON-NLS-1$
				String valSG = config.getConfigValue(cnfgDatSG);
				if (valSG.equals(MFSConfig.NOT_FOUND))
				{
					String cnfgDatSG2 = "RTVSHPGRP," + nmbrAll; //$NON-NLS-1$
					valSG = config.getConfigValue(cnfgDatSG2);
				}
				if (!valSG.equals(MFSConfig.NOT_FOUND))
				{
					valSG += "        "; //$NON-NLS-1$
					int qty = 1;
					String type = valSG.substring(0, 1);
					if (!valSG.substring(2, 6).equals("    ")) //$NON-NLS-1$
					{
						qty = Integer.parseInt(valSG.substring(2, 6).trim());
					}

					/* print shipgroup List */
					MFSPrintingMethods.rtvshpgrp(getCurrMctl(), type, qty, getParentFrame());
				}

				/* Check if we need to print a fcmes packing list */
				String cnfgDatFC = "RTVFCMES," + nmbrPrln; //$NON-NLS-1$
				String valFC = config.getConfigValue(cnfgDatFC);
				if (valFC.equals(MFSConfig.NOT_FOUND))
				{
					String cnfgDatFC2 = "RTVFCMES," + nmbrAll; //$NON-NLS-1$
					valFC = config.getConfigValue(cnfgDatFC2);
				}
				if (!valFC.equals(MFSConfig.NOT_FOUND))
				{
					valFC += "        "; //$NON-NLS-1$
					int qty = 1;
					String type = valFC.substring(0, 1);
					if (!valFC.substring(2, 6).equals("    ")) //$NON-NLS-1$
					{
						qty = Integer.parseInt(valFC.substring(2, 6).trim());
					}

					/* print fcmes packing list */
					MFSPrintingMethods.rtvfcmes(getCurrMctl(), type, qty, getParentFrame());
				}

				/* Check if we need to print a rochester shpgrp */
				String cnfgDatRSG = "RCHSHPGRP," + nmbrPrln; //$NON-NLS-1$
				String valRSG = config.getConfigValue(cnfgDatRSG);
				if (valRSG.equals(MFSConfig.NOT_FOUND))
				{
					String cnfgDatRSG2 = "RCHSHPGRP," + nmbrAll; //$NON-NLS-1$
					valRSG = config.getConfigValue(cnfgDatRSG2);
				}
				if (!valRSG.equals(MFSConfig.NOT_FOUND))
				{
					valRSG += "        "; //$NON-NLS-1$
					int qty = 1;
					String type = valRSG.substring(0, 1);
					if (!valRSG.substring(2, 6).equals("    ")) //$NON-NLS-1$
					{
						qty = Integer.parseInt(valRSG.substring(2, 6).trim());
					}

					/* print shipgroup List */
					MFSPrintingMethods.rchshpgrp(getCurrMctl(), type, qty, getParentFrame());
				}

				/* Check if we need to print a rochester fcmes packing list */
				String cnfgDatRFC = "RCHFCMES," + nmbrPrln; //$NON-NLS-1$
				String valRFC = config.getConfigValue(cnfgDatRFC);
				if (valRFC.equals(MFSConfig.NOT_FOUND))
				{
					String cnfgDatRFC2 = "RCHFCMES," + nmbrAll; //$NON-NLS-1$
					valRFC = config.getConfigValue(cnfgDatRFC2);
				}
				if (!valRFC.equals(MFSConfig.NOT_FOUND))
				{
					valRFC += "        "; //$NON-NLS-1$
					int qty = 1;
					String type = valRFC.substring(0, 1);

					if (!valRFC.substring(2, 6).equals("    ")) //$NON-NLS-1$
					{
						qty = Integer.parseInt(valRFC.substring(2, 6).trim());
					}

					/* print fcmes packing list */
					MFSPrintingMethods.rchfcmes(getCurrMctl(), type, qty, getParentFrame());
				}

				/* Check if we need to print a rochester ffbm label */
				String cnfgDatFB = "RTVFFBMLBL," + nmbrPrln; //$NON-NLS-1$
				String valFB = config.getConfigValue(cnfgDatFB);
				if (valFB.equals(MFSConfig.NOT_FOUND))
				{
					String cnfgDatFB2 = "RTVFFBMLBL," + nmbrAll; //$NON-NLS-1$
					valFB = config.getConfigValue(cnfgDatFB2);
				}
				if (!valFB.equals(MFSConfig.NOT_FOUND))
				{
					int qty = 1;
					if (!valFB.equals("")) //$NON-NLS-1$
					{
						qty = Integer.parseInt(valFB);
					}

					/* print ffbm label */
					MFSPrintingMethods.rtvffbm2(getCurrMctl(), qty, getParentFrame());
				}

				/* Check if we need to print a POK fru label */
				String cnfgDatPFL = "POKFRULBL," + nmbrPrln; //$NON-NLS-1$
				String valPFL = config.getConfigValue(cnfgDatPFL);
				if (valPFL.equals(MFSConfig.NOT_FOUND))
				{
					String cnfgDatPFL2 = "POKFRULBL," + nmbrAll; //$NON-NLS-1$
					valPFL = config.getConfigValue(cnfgDatPFL2);
				}
				if (!valPFL.equals(MFSConfig.NOT_FOUND))
				{
					int qty = 1;
					if (!valPFL.equals("")) //$NON-NLS-1$
					{
						qty = Integer.parseInt(valPFL);
					}

					/* print pok fru label */
					MFSPrintingMethods.frunumb(this.fieldHeaderRec.getPrln(), "J", //$NON-NLS-1$
							getCurrMctl(), qty, getParentFrame());
				}

				/* Check if we need to print a Roch fru label */
				String cnfgDatRFL = "ROCHFRULBL," + nmbrPrln; //$NON-NLS-1$
				String valRFL = config.getConfigValue(cnfgDatRFL);
				if (valRFL.equals(MFSConfig.NOT_FOUND))
				{
					String cnfgDatRFL2 = "ROCHFRULBL," + nmbrAll; //$NON-NLS-1$
					valRFL = config.getConfigValue(cnfgDatRFL2);
				}
				if (!valRFL.equals(MFSConfig.NOT_FOUND))
				{
					int qty = 1;
					if (!valRFL.equals("")) //$NON-NLS-1$
					{
						qty = Integer.parseInt(valRFL);
					}

					/* print roch fru label */
					String pn = "00000" + this.fieldHeaderRec.getPrln().trim(); //$NON-NLS-1$
					MFSPrintingMethods.rochfru("FRU     ", pn, getCurrMctl(), "J", qty, getParentFrame()); //$NON-NLS-1$ //$NON-NLS-2$
				}

				/* Check if we need to print a Roch MIR label */
				String cnfgDatMRR = "ROCHMIRRLBL," + nmbrPrln; //$NON-NLS-1$
				String valMRR = config.getConfigValue(cnfgDatMRR);
				if (valMRR.equals(MFSConfig.NOT_FOUND))
				{
					String cnfgDatMRR2 = "ROCHMIRRLBL," + nmbrAll; //$NON-NLS-1$
					valMRR = config.getConfigValue(cnfgDatMRR2);
				}
				if (!valMRR.equals(MFSConfig.NOT_FOUND))
				{
					int qty = 1;
					if (!valMRR.equals("")) //$NON-NLS-1$
					{
						qty = Integer.parseInt(valMRR);
					}

					/* print roch mir label */
					String part = "00000" + this.fieldHeaderRec.getPrln().substring(0, 7); //$NON-NLS-1$
					MFSPrintingMethods.mir(config.get8CharCellType(), part,
							getCurrMctl(), qty, getParentFrame());
				}

				/* Check if we need to print an 11s barcode label */
				String cnfgDat11S = "11SBARLBL," + nmbrPrln; //$NON-NLS-1$
				String val11S = config.getConfigValue(cnfgDat11S);
				if (val11S.equals(MFSConfig.NOT_FOUND))
				{
					String cnfgDat11S2 = "11SBARLBL," + nmbrAll; //$NON-NLS-1$
					val11S = config.getConfigValue(cnfgDat11S2);
				}
				if (!val11S.equals(MFSConfig.NOT_FOUND))
				{
					int qty = 1;
					if (!val11S.equals("")) //$NON-NLS-1$
					{
						qty = Integer.parseInt(val11S);
					}

					/* print 11s barcode label */
					String part = "00000" + this.fieldHeaderRec.getPrln().substring(0, 7); //$NON-NLS-1$
					MFSPrintingMethods.elevens(part, getCurrMctl(), qty, getParentFrame());
				}
				
				/* Check if we need to print the Carrier label */
				String cnfgDatCarrier = "CARRIER," + nmbrPrln; //$NON-NLS-1$
				String valCarrier = config.getConfigValue(cnfgDatCarrier);
				if (valCarrier.equals(MFSConfig.NOT_FOUND))
				{
					String cnfgDatCarrier2 = "CARRIER," + nmbrAll; //$NON-NLS-1$
					valCarrier = config.getConfigValue(cnfgDatCarrier2);
				}
				if (!valCarrier.equals(MFSConfig.NOT_FOUND))
				{
					int qty = 1;
					if (!valCarrier.equals("")) //$NON-NLS-1$
					{
						qty = Integer.parseInt(valCarrier);
					}

					/* print Carrier barcode label */
					MFSPrintingMethods.carrier(this.fieldHeaderRec.getMctl(), qty, getParentFrame());
				}

				/* Check if we need to print the Carriercomn label */
				String cnfgDatCarriercomn = "CARRIERCOMNEND," + nmbrPrln; //$NON-NLS-1$ // ~32C
				String valCarriercomn = config.getConfigValue(cnfgDatCarriercomn);
				if (valCarriercomn.equals(MFSConfig.NOT_FOUND))
				{
					String cnfgDatCarriercomn2 = "CARRIERCOMNEND," + nmbrAll; //$NON-NLS-1$ // ~32C
					valCarriercomn = config.getConfigValue(cnfgDatCarriercomn2);
				}
				if (!valCarriercomn.equals(MFSConfig.NOT_FOUND))
				{
					int qty = 1;
					if (!valCarriercomn.equals("")) //$NON-NLS-1$
					{
						qty = Integer.parseInt(valCarriercomn);
					}

					//~28A
					/* print Carriercomn label */
					MFSPrintingMethods.carriercomn(this.fieldHeaderRec.getMctl(), "ENDWU", qty, getParentFrame()); //$NON-NLS-1$
				}
				
				/* ~39A BEGIN - Check if we need to print the Warranty Card label */
				String cnfgDatWarranty = "WARRANTYEND," + nmbrPrln;  //$NON-NLS-1$
				String valWarranty = config.getConfigValue(cnfgDatWarranty);
				if (valWarranty.equals(MFSConfig.NOT_FOUND))
				{
					String cnfgDatWarranty2 = "WARRANTYEND," + nmbrAll;  //$NON-NLS-1$
					valWarranty = config.getConfigValue(cnfgDatWarranty2);
				}
				if (!valWarranty.equals(MFSConfig.NOT_FOUND))
				{
					int qty = 1;
					if (!valWarranty.equals(""))  //$NON-NLS-1$
					{
						qty = Integer.parseInt(valWarranty);
					}

					/* print Warranty Card label */
					StringBuffer lbldta = new StringBuffer("<MCTL>"); //~42A //$NON-NLS-1$
					lbldta.append(this.fieldHeaderRec.getMctl()); //~42A
					lbldta.append("</MCTL>"); //~42A //$NON-NLS-1$
					MFSPrintingMethods.warrantyCard(lbldta.toString(), "ENDWU", "", qty, "", getParentFrame()); //~42C //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				}

				/* Check if we need to print the Weight label */
				String cnfgDatWeight = "WEIGHTLBLEND," + nmbrPrln;  //$NON-NLS-1$
				String valWeight = config.getConfigValue(cnfgDatWeight);
				if (valWeight.equals(MFSConfig.NOT_FOUND))
				{
					String cnfgDatWeight2 = "WEIGHTLBLEND," + nmbrAll;  //$NON-NLS-1$
					valWeight = config.getConfigValue(cnfgDatWeight2);
				}
				if (!valWeight.equals(MFSConfig.NOT_FOUND))
				{
					int qty = 1;
					if (!valWeight.equals(""))  //$NON-NLS-1$
					{
						qty = Integer.parseInt(valWeight);
					}

					/* print Weight label */
					MFSPrintingMethods.weightLabel(this.fieldHeaderRec.getMctl(),
							this.fieldHeaderRec.getCntr(), "ENDWU", qty, getParentFrame()); //$NON-NLS-1$
				}
				/* ~39A END */

				/* Check if we need to print the CaseContent label */  // ~26A
				String cnfgDatCaseContent = "CASECONTENT," + nmbrPrln; //$NON-NLS-1$
				String valCaseContent = config.getConfigValue(cnfgDatCaseContent);
				if (valCaseContent.equals(MFSConfig.NOT_FOUND))
				{
					cnfgDatCaseContent = "CASECONTENT," + nmbrAll; //$NON-NLS-1$
					valCaseContent = config.getConfigValue(cnfgDatCaseContent);
				}
				if (!valCaseContent.equals(MFSConfig.NOT_FOUND))
				{
					int qty = 1;
					if (!valCaseContent.equals("")) //$NON-NLS-1$
					{
						qty = Integer.parseInt(valCaseContent);
					}

					/* print Carriercomn label */
					MFSPrintingMethods.caseContent(null, getCurrMctl(), qty, getParentFrame());
				}

				/* Check if we need to print the CaseContentMESF label */  // ~38A
				String cnfgDatCaseContentMESF = "CASECONTENTMESF," + nmbrPrln; //$NON-NLS-1$
				String valCaseContentMESF = config.getConfigValue(cnfgDatCaseContentMESF);
				if (valCaseContentMESF.equals(MFSConfig.NOT_FOUND))
				{
					cnfgDatCaseContentMESF = "CASECONTENTMESF," + nmbrAll; //$NON-NLS-1$
					valCaseContentMESF = config.getConfigValue(cnfgDatCaseContentMESF);
				}
				if (!valCaseContentMESF.equals(MFSConfig.NOT_FOUND) && 
						(this.fieldHeaderRec.getTypz().equals("1") //$NON-NLS-1$
						|| this.fieldHeaderRec.getTypz().equals("2"))) //$NON-NLS-1$
				{
					int qty = 1;
					if (!valCaseContentMESF.equals("")) //$NON-NLS-1$
					{
						qty = Integer.parseInt(valCaseContentMESF);
					}
					
                    /* ~47A List all TopLevelContainers */
					List<String> cntrList = MFSContainerUtils.rtvTopLvlCntrs(getCurrMctl(), fieldHeaderRec.getMfgn(), fieldHeaderRec.getIdss(), getParentFrame(), this);
					for(String cntrValue : cntrList)
					{
					  /* print Carriercomn label */
					  MFSPrintingMethods.containerMESF(cntrValue,getCurrMctl(), qty, getParentFrame());/* ~47C */ 
										
					}
				}
				
				/* Check if we need to print the WUPrint label */
				String cnfgDatWUPrint = "WUPRINT," + nmbrPrln; //$NON-NLS-1$
				String valWUPrint = config.getConfigValue(cnfgDatWUPrint);
				if (valWUPrint.equals(MFSConfig.NOT_FOUND))
				{
					String cnfgDatWUPrint2 = "WUPRINT," + nmbrAll; //$NON-NLS-1$
					valWUPrint = config.getConfigValue(cnfgDatWUPrint2);
				}
				if (!valWUPrint.equals(MFSConfig.NOT_FOUND))
				{
					int qty = 1;
					if (!valWUPrint.equals("")) //$NON-NLS-1$
					{
						qty = Integer.parseInt(valWUPrint);
					}

					/* print WUPrint label */
					MFSPrintingMethods.wuprint(this.fieldHeaderRec.getMctl(), qty, getParentFrame());
				}

				/* Check if we need to print the MACID label */
				String cnfgDatMACPrint = "MACIDEND," + nmbrPrln; //$NON-NLS-1$
				String valMACPrint = config.getConfigValue(cnfgDatMACPrint);
				if (valMACPrint.equals(MFSConfig.NOT_FOUND))
				{
					String cnfgDatWUPrint2 = "MACIDEND," + nmbrAll; //$NON-NLS-1$
					valMACPrint = config.getConfigValue(cnfgDatWUPrint2);
				}
				if (!valMACPrint.equals(MFSConfig.NOT_FOUND))
				{
					int qty = 1;
					if (!valMACPrint.equals("")) //$NON-NLS-1$
					{
						qty = Integer.parseInt(valMACPrint);
					}

					/* print MACID label */
					MFSPrintingMethods.macid(this.fieldHeaderRec.getMfgn(),
							this.fieldHeaderRec.getIdss(), this.fieldHeaderRec.getOrno(),
							this.fieldHeaderRec.getMctl(), qty, getParentFrame());
				}//end if MACID is configured

				//~6A Check if we need to print the RSS Product Package / Part Container labels
				String cnfgDatRSSPrint = "RSSPCPP," + nmbrPrln;  //$NON-NLS-1$
				String valRSSPrint = config.getConfigValue(cnfgDatRSSPrint);
				if (valRSSPrint.equals(MFSConfig.NOT_FOUND))
				{
					String cnfgDatRSSPrint2 = "RSSPCPP," + nmbrAll; //$NON-NLS-1$
					valRSSPrint = config.getConfigValue(cnfgDatRSSPrint2);
				}
				if (!valRSSPrint.equals(MFSConfig.NOT_FOUND))
				{
					int qty = 1;
					if (!valRSSPrint.equals("")) //$NON-NLS-1$
					{
						qty = Integer.parseInt(valRSSPrint);
					}
					/* print RSSPCPP labels */
			
					MFSPrintingMethods.pcpplabels(this.fieldHeaderRec.getCntr(),
								this.fieldHeaderRec.getMctl(), "END", qty, //$NON-NLS-1$
                                                "RSS", getParentFrame()); //$NON-NLS-1$    ~20A


				}// ~6A end if RSSPCPP is configured
				//~20A Check if we need to print the SYSX,SPD,SYSZ and IPSYS Product Package / Part Container labels
		        String cnfgDatSYSXPrint = "SYSXPCPP," + nmbrPrln;  //$NON-NLS-1$
		        String valSYSXPrint = config.getConfigValue(cnfgDatSYSXPrint);
		        if (valSYSXPrint.equals(MFSConfig.NOT_FOUND))
		        {
			       String cnfgDatSYSXPrint2 = "SYSXPCPP," + nmbrAll; //$NON-NLS-1$
			       valSYSXPrint = config.getConfigValue(cnfgDatSYSXPrint2);
		        }
		        if (!valSYSXPrint.equals(MFSConfig.NOT_FOUND))
		        {
			       int qty = 1;
			       if (!valSYSXPrint.equals("")) //$NON-NLS-1$
			       {
				      qty = Integer.parseInt(valSYSXPrint);
			       }
			       /* print SYSXPCPP labels */

				   MFSPrintingMethods.pcpplabels(this.fieldHeaderRec.getCntr(),
						this.fieldHeaderRec.getMctl(), "END", qty, //$NON-NLS-1$
						"SYSX", getParentFrame()); //$NON-NLS-1$
		        }
		
                String cnfgDatSPDPrint = "SPDPCPP," + nmbrPrln;  //$NON-NLS-1$
		        String valSPDPrint = config.getConfigValue(cnfgDatSPDPrint);
		        if (valSPDPrint.equals(MFSConfig.NOT_FOUND))
		        {
			       String cnfgDatSPDPrint2 = "SPDPCPP," + nmbrAll; //$NON-NLS-1$
			       valSPDPrint = config.getConfigValue(cnfgDatSPDPrint2);
		        }
		        if (!valSPDPrint.equals(MFSConfig.NOT_FOUND))
		        {
			       int qty = 1;
			       if (!valSPDPrint.equals("")) //$NON-NLS-1$
			       {
				      qty = Integer.parseInt(valSPDPrint);
			       }
			       /* print SPDPCPP labels */

			       MFSPrintingMethods.pcpplabels(this.fieldHeaderRec.getCntr(),
						this.fieldHeaderRec.getMctl(), "END", qty, //$NON-NLS-1$
						"SPD", getParentFrame()); //$NON-NLS-1$

		        }
		
                String cnfgDatSYSZPrint = "SYSZPCPP," + nmbrPrln;  //$NON-NLS-1$
		        String valSYSZPrint = config.getConfigValue(cnfgDatSYSZPrint);
		        if (valSYSZPrint.equals(MFSConfig.NOT_FOUND))
		        {
			       String cnfgDatSYSZPrint2 = "SYSZPCPP," + nmbrAll; //$NON-NLS-1$
			       valSYSZPrint = config.getConfigValue(cnfgDatSYSZPrint2);
		        }
		        if (!valSYSZPrint.equals(MFSConfig.NOT_FOUND))
		        {
			       int qty = 1;
			       if (!valSYSZPrint.equals("")) //$NON-NLS-1$
			       {
				      qty = Integer.parseInt(valSYSZPrint);
			       }
			       /* print SYSZPCPP labels */

				   MFSPrintingMethods.pcpplabels(this.fieldHeaderRec.getCntr(),
						this.fieldHeaderRec.getMctl(), "END", qty, //$NON-NLS-1$
						"SYSZ", getParentFrame()); //$NON-NLS-1$

		        }

	            String cnfgDatIPSYSPrint = "IPSYSPCPP," + nmbrPrln;  //$NON-NLS-1$
		        String valIPSYSPrint = config.getConfigValue(cnfgDatIPSYSPrint);
		        if (valIPSYSPrint.equals(MFSConfig.NOT_FOUND))
		        {
			       String cnfgDatIPSYSPrint2 = "IPSYSPCPP," + nmbrAll; //$NON-NLS-1$
			       valIPSYSPrint = config.getConfigValue(cnfgDatIPSYSPrint2);
		        }
		        if (!valIPSYSPrint.equals(MFSConfig.NOT_FOUND))
		        {
			       int qty = 1;
			       if (!valIPSYSPrint.equals("")) //$NON-NLS-1$
			       {
				      qty = Integer.parseInt(valIPSYSPrint);
			       }
			       /* print IPSYSPCPP labels */

				   MFSPrintingMethods.pcpplabels(this.fieldHeaderRec.getCntr(),
						this.fieldHeaderRec.getMctl(), "END", qty, //$NON-NLS-1$
						"IPSYS", getParentFrame()); //$NON-NLS-1$

		        }
	            //~20 end
		        
		        //~29 start
	            String cnfgDatCooCntr = "COOCNTR," + nmbrPrln;  //$NON-NLS-1$
		        String valCooCntr = config.getConfigValue(cnfgDatCooCntr);
		        if (valCooCntr.equals(MFSConfig.NOT_FOUND))
		        {
		        	cnfgDatCooCntr = "COOCNTR," + nmbrAll; //$NON-NLS-1$
		        	valCooCntr = config.getConfigValue(cnfgDatCooCntr);
		        }
		        if (!valCooCntr.equals(MFSConfig.NOT_FOUND))
		        {
			       int qty = 1;
			       if (!valCooCntr.equals("")) //$NON-NLS-1$
			       {
				      qty = Integer.parseInt(valCooCntr);
			       }
			       /* print COOCNTR Country of Origin Container labels */
		       		//~31C
		       		MFSPrintingMethods.cooCntr(this.fieldHeaderRec.getMctl(),
		       				"          ",qty, getParentFrame()); //$NON-NLS-1$
		        }	
		        //~29 end
		        			
				/* Check if we need to print the REBRAND labels */
				String cnfgDatRebrandPrint = "REBRAND," + nmbrPrln; //$NON-NLS-1$
				String valRebrandPrint = config.getConfigValue(cnfgDatRebrandPrint);
				if (valRebrandPrint.equals(MFSConfig.NOT_FOUND))
				{
					String cnfgDatRebrand2 = "REBRAND," + nmbrAll; //$NON-NLS-1$
					valRebrandPrint = config.getConfigValue(cnfgDatRebrand2);
				}
				if (!valRebrandPrint.equals(MFSConfig.NOT_FOUND))
				{
					int qty = 1;
					if (!valRebrandPrint.equals("")) //$NON-NLS-1$
					{
						qty = Integer.parseInt(valRebrandPrint);
					}

					/* Call RTVREBRAND transaction to get rebrand data */
					//Declare and initialize variables.
					int rebrand_rc = 0;
					String errorString = ""; //$NON-NLS-1$
					String matp = ""; //$NON-NLS-1$
					String mmdl = ""; //$NON-NLS-1$
					String prln = ""; //$NON-NLS-1$
					String mctl = ""; //$NON-NLS-1$

					//Setup the xml string to call the transaction with necessary parameters.
					MfsXMLDocument xml_rebranddata = new MfsXMLDocument("RTVREBRAND"); //$NON-NLS-1$
					xml_rebranddata.addOpenTag("DATA"); //$NON-NLS-1$
					prln = this.fieldHeaderRec.getPrln();
					xml_rebranddata.addCompleteField("PRLN", prln); //$NON-NLS-1$
					xml_rebranddata.addCloseTag("DATA"); //$NON-NLS-1$
					xml_rebranddata.finalizeXML();

					MFSTransaction rtvrebrand = new MFSXmlTransaction(xml_rebranddata.toString());
					MFSComm.getInstance().execute(rtvrebrand, this);

					// Get the return code from the transaction
					rebrand_rc = rtvrebrand.getReturnCode();

					// Parse the data returned
					MfsXMLParser xmlParser = new MfsXMLParser(rtvrebrand.getOutput());

					try
					{
						//Get the error message if a bad rc is returned from the transaction.
						if (rebrand_rc != 0)
						{
							errorString = xmlParser.getField("ERMS"); //$NON-NLS-1$
						}
						// Else it's a good rc
						else
						{
							// Get the Rebrand MachineType and Model values.
							matp = xmlParser.getField("MATP").trim(); //$NON-NLS-1$
							mmdl = xmlParser.getField("MMDL").trim(); //$NON-NLS-1$

							/* Get the MCTL value required for getting the COOC */
							mctl = this.fieldHeaderRec.getMctl().trim();

							/* Then display dialog box to scan in 1S barcode */
							MFSRebrandDialog myRJD = new MFSRebrandDialog(
									getParentFrame(), mmdl, matp, prln, mctl, qty);
							myRJD.setLocationRelativeTo(getParentFrame()); //~17A
							myRJD.setVisible(true);
						}/* end of good rebrand_rc */
					}
					catch (MISSING_XML_TAG_EXCEPTION e)
					{
						rebrand_rc = 10;
						errorString = "Missing XML Tag Error in REBRAND Label Print"; //$NON-NLS-1$
					}

					// If not a good rc, then display the error message
					if (rebrand_rc != 0)
					{
						IGSMessageBox.showOkMB(getParentFrame(), null, errorString, null);
					}
				}//end if Rebrand is configured
				
				//~43A On Demand Label printing
				onDemandTrigger(this.fieldHeaderRec, "ENDWU"); //~43A //$NON-NLS-1$
				
			}//end if ECOD=C and rc = 30, 31, or 100

			if ((ecod.equals("C") && rc != 30 && rc != 100 && rc != 200) //$NON-NLS-1$
					|| ((ecod.equals("S")) && (rc != 30) && (rc != 31))) //$NON-NLS-1$
			{
				/* display msg to user */
				if (this.fieldHeaderRec.getWtyp().equals("T") && this.fieldCounter == 0) //$NON-NLS-1$
				{
					//Nothing to do
				}
				else
				{
					/* @1 */
					String erms = ""; //$NON-NLS-1$
					if (warning)
					{
						erms = end_wu.getOutput().substring(0, 80);
					}
					else
					{
						erms = end_wu.getOutput();
					}
					String cnfgEfficiencyValue = MFSConfig.getInstance().getConfigValue("EFFICIENCYON");//$NON-NLS-1$
					if (!cnfgEfficiencyValue.equals(MFSConfig.NOT_FOUND))		//~41		
					{								
						IGSMessageBox.showOkMBTimer(getParentFrame(), null, erms, null, cnfgEfficiencyValue);	//~41	
					}
					else
					{
						IGSMessageBox.showOkMB(getParentFrame(), null, erms, null);						
					}
				}
			}
			else if (ecod.equals("C") && (rc == 200)) //$NON-NLS-1$
			{
				showPNSNErrorList(end_wu.getOutput(), 0);
			}
		}
		catch (Exception e)
		{
			setCursor(Cursor.getDefaultCursor());
			rc = 10;
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
		return rc;
	}
	
	//~16A New method
	/**
	 * Executes the specified <code>runnable</code> in a new
	 * <code>Thread</code>, calling <code>updateAction</code>
	 * until the <code>Thread</code> dies.
	 * @param runnable the <code>Runnable</code> to execute
	 * @param actionMessage the action message to display
	 */
	private void executeRunnable(Runnable runnable, String actionMessage)
	{
		startAction(actionMessage);
		Thread thread = new Thread(runnable);
		thread.start();
		while (thread.isAlive())
		{
			updateAction(actionMessage, -1);
			try
			{
				Thread.sleep(100);
			}
			catch (InterruptedException ie)
			{
				ie.printStackTrace();
			}
		}
		stopAction();
	}

	/** Selects the PN list element corresponding to {@link #fieldActiveRowIndex}. */
	public void ensureActiveRowIndexIsHighlighted()
	{
		if (this.fieldActiveRowIndex != -1)
		{
			getRowVectorElementAt(this.fieldActiveRow).getPNList().setSelectedIndex(this.fieldActiveRowIndex);
			this.fieldActiveRowIndex = -1;
		}
	}

	/** Displays an <code>MFSDirWrkExtFnctPanel</code>. */
	public void extendedFunctions()
	{
		this.fieldChildPanel = new MFSDirWrkExtFnctPanel(getParentFrame(), this,
				this.fieldHeaderRec, this.fieldComponentListModel, getCurrMctl());
		getParentFrame().displayMFSPanel(this.fieldChildPanel);
	}

	/** Displays an <code>MFSExtraClogPartDialog</code> to add an extra clog part. */
	public void extra_clog_part()
	{
		boolean done = false;
		String rememberPN = ""; //$NON-NLS-1$
		try
		{
			removeMyListeners();

			if (this.fieldLoc.length() == 0)
			{
				String erms = "Please set the Location "; //$NON-NLS-1$
				IGSMessageBox.showOkMB(getParentFrame(), null, erms, null);
			}
			else
			{
				while (!done)
				{
					MFSExtraClogPartDialog myExtraPartD = new MFSExtraClogPartDialog(this);
					
					System.out.println("Creating component record inside extra_clog_part..."); //$NON-NLS-1$
					MFSComponentRec newCr = new MFSComponentRec();

					newCr.setRec_changed(true);

					newCr.setPnri("1"); //$NON-NLS-1$
					String sni = MFSConfig.getInstance().getConfigValue("DECODESN"); //$NON-NLS-1$
					if (!sni.equals(MFSConfig.NOT_FOUND))
					{
						newCr.setCsni(sni);
					}
					else
					{
						newCr.setCsni("1"); //$NON-NLS-1$
					}
					newCr.setEcri("0"); //$NON-NLS-1$
					newCr.setCcai("0"); //$NON-NLS-1$
					newCr.setCmti("0"); //$NON-NLS-1$
					newCr.setPari("0"); //$NON-NLS-1$

					newCr.setAmsi(" "); //$NON-NLS-1$
					newCr.setCooi(" "); //$NON-NLS-1$
					newCr.setMlri("1"); //$NON-NLS-1$
					newCr.setMctl(this.fieldHeaderRec.getMctl());
					newCr.setFamc("     "); //$NON-NLS-1$
					newCr.setQnty("00001"); //$NON-NLS-1$
					newCr.setFqty("00000"); //$NON-NLS-1$
					newCr.setCntr("          "); //$NON-NLS-1$
					newCr.setInsq("            "); //$NON-NLS-1$
					newCr.setInec("            "); //$NON-NLS-1$
					newCr.setInca("            "); //$NON-NLS-1$
					newCr.setCwun("        "); //$NON-NLS-1$
					newCr.setCooc("  "); //$NON-NLS-1$
					newCr.setIdsp("A"); //$NON-NLS-1$
					newCr.setPrtd(" "); //$NON-NLS-1$
					newCr.setCdes("BRAND NEW PART          "); //$NON-NLS-1$
					newCr.setNmbr(this.fieldHeaderRec.getNmbr());
					newCr.setMspi(this.fieldHeaderRec.getMspi());
					newCr.setMcsn(this.fieldHeaderRec.getMcsn());

					myExtraPartD.reinitialize(this.fieldHeaderRec,
							this.fieldComponentListModel, newCr, this.fieldLoc,
							rememberPN);
					myExtraPartD.setLocationRelativeTo(this.pnlButtons);
					myExtraPartD.setVisible(true);

					if (((myExtraPartD.getComponentAdded() == 0) || (myExtraPartD.getComponentAdded() == 1))
							&& !(myExtraPartD.getPressedCancel()))
					{
						//add the part to the bottom of the list model
						this.fieldComponentListModel.add(this.fieldComponentListModel.getSize(), myExtraPartD.getComp());

						setupExtraPartRow(myExtraPartD.getComp());
						rememberPN = myExtraPartD.getRememberPN();

						if (this.fieldHeaderRec.getWtyp().equals("T") //$NON-NLS-1$
								|| this.fieldHeaderRec.getWtyp().equals("C")) //$NON-NLS-1$
						{
							computeCounter();
						}
						if (myExtraPartD.getComponentAdded() == 1)
						{
							done = true;
						}
					}
					else if (myExtraPartD.getComponentAdded() == 2)
					{
						done = true;
					}

					if (myExtraPartD.getPressedCancel())
					{
						done = true;
					}
				}
			}// loc set
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}

		addMyListeners();
		focusPNListOrButton(this.pbExtraClogPart);
	}

	/** Displays an <code>MFSExtraPartDialog</code> to add an extra Debug/Golden part. */
	public void extra_debugGolden_part()
	{
		int rc = 0;
		try
		{
			removeMyListeners();

			String user = ""; //$NON-NLS-1$
			String cell = ""; //$NON-NLS-1$
			boolean done = false;
			boolean pressedCancel = false;

			while (!done)
			{
				MFSFkitUserIDCellDialog myUserCellJD = new MFSFkitUserIDCellDialog(getParentFrame());
				myUserCellJD.setLocationRelativeTo(getParentFrame()); //~17A
				myUserCellJD.setVisible(true);

				if (myUserCellJD.getPressedCancel())
				{
					done = true;
					pressedCancel = true;
				}
				else
				{
					done = true;
					if (myUserCellJD.getUserText().trim().length() != 0)
					{
						user = myUserCellJD.getUserText().toUpperCase().concat("        ").substring(0, 8); //$NON-NLS-1$

						String data = "CHECK_AUTH" + user; //$NON-NLS-1$
						MFSTransaction check_auth = new MFSFixedTransaction(data);
						check_auth.setActionMessage("Checking User ID, Please Wait..."); //$NON-NLS-1$
						MFSComm.getInstance().execute(check_auth, this);
						rc = check_auth.getReturnCode();

						if (rc != 0)
						{
							IGSMessageBox.showOkMB(getParentFrame(), null, check_auth.getOutput(), null);
							done = false;
						}
					}
					else
					{
						String erms = "Invalid User Id!"; //$NON-NLS-1$
						IGSMessageBox.showOkMB(getParentFrame(), null, erms, null);
						done = false;
					}

					if (done)
					{
						if (myUserCellJD.getCellText().trim().length() != 0)
						{
							cell = myUserCellJD.getCellText().toUpperCase().concat("        ").substring(0, 8); //$NON-NLS-1$
						}
						else
						{
							String erms = "Invalid Cell Name!"; //$NON-NLS-1$
							IGSMessageBox.showOkMB(getParentFrame(), null, erms, null);
							done = false;
						}
					}
				}
			}

			if (!pressedCancel)
			{
				MFSGenericListDialog myGoldenDebugJD = new MFSGenericListDialog(
						getParentFrame(), "Golden or Debug Part?", //$NON-NLS-1$
						"Select Golden or Debug from the List"); //$NON-NLS-1$
				myGoldenDebugJD.setSizeSmall();
				myGoldenDebugJD.loadAnswerListModel("GoldenDebug ", 6); //$NON-NLS-1$
				myGoldenDebugJD.setLocationRelativeTo(getParentFrame()); //~17A
				myGoldenDebugJD.setVisible(true);

				if (myGoldenDebugJD.getProceedSelected())
				{
					MFSPartNumDialog partnumdialog = new MFSPartNumDialog(
							getParentFrame(), this.fieldHeaderRec.getPrln());
					partnumdialog.setLocationRelativeTo(getParentFrame()); //~17A
					partnumdialog.setVisible(true);

					if (partnumdialog.getPressedEnter())
					{
						StringBuffer data = new StringBuffer();
						data.append("RTV_FLAGS "); //$NON-NLS-1$
						data.append(this.fieldHeaderRec.getMctl());
						data.append(partnumdialog.getPartNum());
						data.append("F"); //$NON-NLS-1$

						MFSTransaction rtv_flags = new MFSFixedTransaction(data.toString());
						rtv_flags.setActionMessage("Retrieving Part Information, Please Wait..."); //$NON-NLS-1$
						MFSComm.getInstance().execute(rtv_flags, this);
						rc = rtv_flags.getReturnCode();

						if (rc == 0)
						{
							System.out.println("Creating component record inside extra_debugGolden..."); //$NON-NLS-1$
							MFSComponentRec newCr = new MFSComponentRec();

							final String output = rtv_flags.getOutput();
							newCr.setRec_changed(true);
							newCr.setPnri(output.substring(0, 1));
							newCr.setCsni(output.substring(1, 2));
							newCr.setEcri(output.substring(2, 3));
							newCr.setCcai(output.substring(3, 4));
							newCr.setCmti(output.substring(4, 5));
							newCr.setPari(output.substring(5, 6));
							
							newCr.setAmsi(" "); //$NON-NLS-1$
							newCr.setCooi(" "); //$NON-NLS-1$
							newCr.setMlri("1"); //$NON-NLS-1$
							if (myGoldenDebugJD.getSelectedListOption().substring(0, 1).equals("G")) //$NON-NLS-1$
							{
								newCr.setCdes("GOLDEN PART             "); //$NON-NLS-1$
							}
							else
							{
								newCr.setCdes("DEBUG PART              "); //$NON-NLS-1$
							}
							
							newCr.setMctl(this.fieldHeaderRec.getMctl());
							newCr.setItem(partnumdialog.getPartNum());
							newCr.setInpn(partnumdialog.getPartNum());
							
							newCr.setFamc("     "); //$NON-NLS-1$
							newCr.setQnty(partnumdialog.getQty());
							newCr.setFqty(partnumdialog.getQty());
							newCr.setCntr("          "); //$NON-NLS-1$
							if (partnumdialog.getHiddenSN().length() > 0)
							{
								newCr.setInsq(partnumdialog.getHiddenSN());
							}
							else
							{
								newCr.setInsq("            "); //$NON-NLS-1$
							}
							newCr.setInec("            "); //$NON-NLS-1$
							newCr.setInca("            "); //$NON-NLS-1$
							newCr.setCwun("        "); //$NON-NLS-1$
							newCr.setCooc("  "); //$NON-NLS-1$
							newCr.setIdsp("A"); //$NON-NLS-1$
							newCr.setPrtd(" "); //$NON-NLS-1$
							newCr.setNmbr("FKIT"); //$NON-NLS-1$
							newCr.setMspi(this.fieldHeaderRec.getMspi());
							newCr.setMcsn(this.fieldHeaderRec.getMcsn());
							newCr.setSuff("        "); //$NON-NLS-1$
							newCr.setNmsq("     "); //$NON-NLS-1$

							MFSExtraPartDialog myExtraPartD = new MFSExtraPartDialog(
									getParentFrame(), this.fieldHeaderRec, newCr);
							myExtraPartD.setExtraType(myGoldenDebugJD.getSelectedListOption());
							myExtraPartD.setFkitUserCell(user, cell);
							myExtraPartD.setLocationRelativeTo(this.pnlButtons);
							myExtraPartD.initDisplay();

							myExtraPartD.setVisible(true);
							if (myExtraPartD.getComponentAdded())
							{
								this.fieldComponentListModel.add(this.fieldComponentListModel.getSize(), myExtraPartD.getComp());
								//now find a place to put the part within the group of lists
								setupExtraPartRow(myExtraPartD.getComp());
								this.validate();
							}
						}
						else
						{
							IGSMessageBox.showOkMB(getParentFrame(), null, rtv_flags.getOutput(), null);
						}
					}// end of pressedEnter on Part number dialog
				}// end of pressedEnter on debug/golden parts selection
			}//end of valid cell and user input
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}

		addMyListeners();
		focusPNListOrButton(this.pbExtra);
	}

	/** Adds an extra part. */
	public void extra_part()
	{
		int rc = 0;
		final MFSConfig config = MFSConfig.getInstance();
		try
		{
			removeMyListeners();
			MFSPartNumDialog partnumdialog = new MFSPartNumDialog(getParentFrame(),
					this.fieldHeaderRec.getPrln());
			partnumdialog.setLocationRelativeTo(getParentFrame()); //~17A
			partnumdialog.setVisible(true);
			if (partnumdialog.getPressedEnter())
			{
				StringBuffer data = new StringBuffer();
				data.append("EXTRA_CRSI"); //$NON-NLS-1$
				data.append(this.fieldHeaderRec.getMctl());
				data.append(partnumdialog.getPartNum());
				data.append(partnumdialog.getQty());
				data.append(config.get8CharUser());
				data.append(config.get8CharCell());
				data.append(config.get8CharCellType());

				MFSTransaction extra_crsi = new MFSFixedTransaction(data.toString());
				extra_crsi.setActionMessage("Creating Extra Part, Please Wait..."); //$NON-NLS-1$
				MFSComm.getInstance().execute(extra_crsi, this);
				rc = extra_crsi.getReturnCode();

				if (rc == 0)
				{
					System.out.println("Creating component record inside extra_part..."); //$NON-NLS-1$
					MFSComponentRec newCr = new MFSComponentRec();
					newCr.setRec_changed(true);
					newCr.setPnri("1"); //$NON-NLS-1$
					newCr.setCooi(" "); //$NON-NLS-1$
					newCr.setMlri("1"); //$NON-NLS-1$
					newCr.setCrct(extra_crsi.getOutput().substring(0, 4));
					newCr.setCdes(extra_crsi.getOutput().substring(4, 28));
					newCr.setMctl(this.fieldHeaderRec.getMctl());
					newCr.setItem(partnumdialog.getPartNum());
					newCr.setQnty(partnumdialog.getQty());
					newCr.setCsni(" "); //$NON-NLS-1$
					newCr.setEcri(" "); //$NON-NLS-1$
					newCr.setCcai(" "); //$NON-NLS-1$
					newCr.setCmti(" "); //$NON-NLS-1$
					newCr.setPari(" "); //$NON-NLS-1$
					newCr.setAmsi(" "); //$NON-NLS-1$
					newCr.setFqty("00000"); //$NON-NLS-1$
					newCr.setCntr("          "); //$NON-NLS-1$
					newCr.setInsq("            "); //$NON-NLS-1$
					newCr.setInec("            "); //$NON-NLS-1$
					newCr.setInca("            "); //$NON-NLS-1$
					newCr.setInpn("            "); //$NON-NLS-1$
					newCr.setCwun("        "); //$NON-NLS-1$
					newCr.setMcsn("       "); //$NON-NLS-1$
					newCr.setMspi("  "); //$NON-NLS-1$
					newCr.setShtp(" "); //$NON-NLS-1$
					newCr.setFamc("     "); //$NON-NLS-1$
					newCr.setCooc("US"); //$NON-NLS-1$
					newCr.setIdsp("A"); //$NON-NLS-1$
					newCr.setNmbr("SISI"); //$NON-NLS-1$
					newCr.updtDisplayString();
					newCr.updtIRDisplayString();
					newCr.updtInstalledParts();

					this.fieldComponentListModel.add(this.fieldComponentListModel.getSize(), newCr);
					setupExtraPartRow(newCr);
					this.validate();
				}
				else
				{
					IGSMessageBox.showOkMB(getParentFrame(), null, extra_crsi.getOutput(), null);
				}
			}
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}

		addMyListeners();
		focusPNListOrButton(this.pbExtraClogPart);
	}

	//~16A New method
	/**
	 * Requests the foucs for the part number list of the active row or the
	 * specified <code>MFSMenuButton</code> if the row vector is empty.
	 * @param menuButton the <code>MFSMenuButton</code> to give the focus if
	 *        the row vector is empty
	 */
	private void focusPNListOrButton(MFSMenuButton menuButton)
	{
		if (this.fieldRowVector.size() > 0)
		{
			getRowVectorElementAt(this.fieldActiveRow).getPNList().requestFocusInWindow();
		}
		else
		{
			menuButton.requestFocusInWindow();
		}
	}
	
	/**
	 * Returns the MCTL (Work Unit Control Number) for the current work unit.
	 * @return the MCTL for the current work unit
	 */
	public String getCurrMctl()
	{
		return this.fieldCurrMctl;
	}

	//~16A New method
	/**
	 * Returns the absolute path for the DASD personalization file for the given code name.
	 * @param codeName the code name for the DASD personalization file
	 * @return the absolute path of the DASD personalization file
	 */
	private String getDasdpPath(String codeName)
	{
		File dasdpDirectory = IGSFileUtils.getFile("dasdp"); //$NON-NLS-1$
		if (!dasdpDirectory.exists())
		{
			/* The dasdp directory does not exist. So create one. */
			dasdpDirectory.mkdir();
		}
		return new File(dasdpDirectory, codeName + ".bin").getAbsolutePath(); //$NON-NLS-1$
	}

	//~13
	/**
	 * Gets the list of printable down bin'd parts.
	 * @return The downbin hashtable.
	 * @see #setDwnBinList(String, String)
	 */
	public Hashtable<String, String> getDwnBinList()
	{
		return this.fieldDownBinList;
	}

	/**
	 * Returns the end code.
	 * @return the end code
	 */
	public int getEndCode()
	{
		return this.fieldEndCode;
	}

	//~16A New method
	/** Initializes the text of {@link #lblSaps} and {@link #lblSapo}. */
	protected void initSapsAndSapoLabels()
	{
		String saps = this.fieldHeaderRec.getSaps();
		if (!saps.equals("            ")) //$NON-NLS-1$
		{
			this.lblSaps.setText("S Order: " + saps); //$NON-NLS-1$
		}
		else
		{
			this.lblSaps.setText(""); //$NON-NLS-1$
		}

		String sapo = this.fieldHeaderRec.getSapo();
		if (!sapo.equals("            ")) //$NON-NLS-1$
		{
			this.lblSapo.setText("P Order: " + this.fieldHeaderRec.getSapo()); //$NON-NLS-1$
		}
		else
		{
			this.lblSapo.setText(""); //$NON-NLS-1$
		}
	}

	//~16A New method
	/**
	 * Returns <code>true</code> iff the instruction text is hidden.
	 * @return <code>true</code> iff in hide mode
	 */
	public boolean isHideMode()
	{
		//If hide button says show, then panel is in hide mode
		return this.pbHide.isEnabled() && this.pbHide.getText().startsWith("Show"); //$NON-NLS-1$
	}

	/** {@inheritDoc} */
	public void loadPartsModels()
	{
		try
		{
			//make sure we have none from earlier ops
			this.fieldLmVector.clear();

			if (this.fieldComponentListModel.size() > 0)
			{
				int index = 0;
				boolean shorted = false;
				MFSComponentListModel curLm;
				String curSuffix = "          "; //$NON-NLS-1$
				String curSeq = "     "; //$NON-NLS-1$
				String curNmbr = "    "; //$NON-NLS-1$

				MFSComponentRec next = getComponentListModelCompRecAt(index);

				// handle all the shorted parts
				if (next.isShortPart() && !this.fieldHeaderRec.getNmbr().equals("FKIT")) //$NON-NLS-1$
				{
					shorted = true;
					curLm = new MFSComponentListModel();
					curLm.addElement(next);

					curLm.setIsShort(true);
					curSuffix = next.getSuff();
					curSeq = next.getNmsq();
					curLm.setSuff(curSuffix);
					curLm.setNmsq(curSeq);
					curNmbr = next.getNmbr();
					index++;
					while (index < this.fieldComponentListModel.size() && shorted)
					{
						next = getComponentListModelCompRecAt(index);
						if (next.isShortPart())
						{
							if (next.getSuff().equals(curSuffix)
									&& next.getNmsq().equals(curSeq)
									&& next.getNmbr().equals(curNmbr))
							{
								curLm.addElement(next);
							}
							else
							{
								curSuffix = next.getSuff();
								curSeq = next.getNmsq();
								curNmbr = next.getNmbr();
								this.fieldLmVector.addElement(curLm);
								curLm = new MFSComponentListModel();
								curLm.setSuff(curSuffix);
								curLm.setNmsq(curSeq);
								curLm.setIsShort(true);
								curLm.addElement(next);
							}
							index++;
						}
						else
						{
							shorted = false;
						}
					}
					this.fieldLmVector.addElement(curLm);
				}

				// handle different suffix sequences here
				if (index < this.fieldComponentListModel.size())
				{
					curSuffix = next.getSuff();
					curSeq = next.getNmsq();
					curLm = new MFSComponentListModel();
					curLm.setSuff(curSuffix);
					curLm.setNmsq(curSeq);

					//curLm is blank, add some elements to it
					while (index < this.fieldComponentListModel.size())
					{
						next = getComponentListModelCompRecAt(index);
						if (next.getSuff().equals(curSuffix)
								&& next.getNmsq().equals(curSeq))
						{
							curLm.addElement(next);
						}
						else
						{
							curSuffix = next.getSuff();
							curSeq = next.getNmsq();
							this.fieldLmVector.addElement(curLm);
							curLm = new MFSComponentListModel();
							curLm.setSuff(curSuffix);
							curLm.setNmsq(curSeq);
							curLm.addElement(next);
						}
						index++;
					}
					//add last lm to the vector
					this.fieldLmVector.addElement(curLm);
				}
			}// end size() > 0
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
	}

	/**
	 * Loads the short instructions.
	 * @param xml the <code>MfsXMLParser</code> containing the instructions
	 */
	public void loadShortInstructions(MfsXMLParser xml)
	{
		try
		{
			getSource().startAction(this.LOADING_INST_MSG); //~18C
			
			String path = MFSConfig.getInstance().getConfigValue("IRPATH"); //$NON-NLS-1$
			if (path.equals(MFSConfig.NOT_FOUND))
			{
				path = xml.getField("PATH"); //$NON-NLS-1$
			}
			path = path.trim();

			String curSqRec = null;
			try
			{
				curSqRec = xml.getField("SQ"); //$NON-NLS-1$
			}
			catch (MISSING_XML_TAG_EXCEPTION mxte)
			{
				curSqRec = null;
			}

			if (curSqRec != null)
			{
				MFSInstructionRec curInst = new MFSInstructionRec();
				MfsXMLParser curSqXML = new MfsXMLParser(curSqRec);

				loadInstruction(curInst, curSqXML, path, false);

				String curAgRec = null;
				try
				{
					curAgRec = xml.getField("AG"); //$NON-NLS-1$
				}
				catch (MISSING_XML_TAG_EXCEPTION e)
				{
					curAgRec = null;
				}
				if (curAgRec != null)
				{
					while (!curAgRec.equals("")) //$NON-NLS-1$
					{
						MfsXMLParser curAGXML = new MfsXMLParser(curAgRec);

						String item = curAGXML.getField("ITEM"); //$NON-NLS-1$
						String qnty = curAGXML.getField("QNTY"); //$NON-NLS-1$

						StringBuffer agList = new StringBuffer();
						agList.append(curInst.getAgragatePartList());
						agList.append("PN: "); //$NON-NLS-1$
						agList.append(item.substring(2));
						agList.append("  QTY: "); //$NON-NLS-1$
						agList.append(qnty);
						agList.append("  "); //$NON-NLS-1$
						curInst.setAgragatePartList(agList.toString());

						curAgRec = xml.getNextField("AG"); //$NON-NLS-1$
					}//while more agragate parts records
				}//some agragates were found
				this.fieldInstVector.addElement(curInst);
			}//end of some instructions found
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
		finally
		{
			getSource().stopAction(); //~18C
		}
	}

	/**
	 * If an appropriate LOC config entry is set, calls the RTV_WULOCS
	 * transaction to present the user with a list of locations. The selected
	 * location is stored in {@link #fieldEndOrSuspendLoc}.
	 * @param endOrSuspendFlag "E" for end, "S" for suspend
	 * @return zero on success; nonzero on failure
	 */
	public int locIt(String endOrSuspendFlag)
	{
		int rc = 0;
		final MFSConfig config = MFSConfig.getInstance();
		try
		{
			//reset endOrSuspendLoc to nothing
			this.fieldEndOrSuspendLoc = "    "; //$NON-NLS-1$
			final String nmbr = this.fieldHeaderRec.getNmbr();
			final String prln = this.fieldHeaderRec.getPrln().trim();
			//~24C Call containsNmbrPrlnFlagEntry
			if (config.containsNmbrPrlnFlagEntry("LOC", nmbr, prln, endOrSuspendFlag) != null) //$NON-NLS-1$//~44C
			{
				MfsXMLDocument xml_data1 = new MfsXMLDocument("RTV_WULOCS"); //$NON-NLS-1$
				xml_data1.addOpenTag("DATA"); //$NON-NLS-1$
				xml_data1.addCompleteField("CTYP", config.get8CharCellType()); //$NON-NLS-1$
				xml_data1.addCompleteField("MCTL", this.fieldHeaderRec.getMctl()); //$NON-NLS-1$
				xml_data1.addCompleteField("PROD", this.fieldHeaderRec.getProd()); //$NON-NLS-1$
				xml_data1.addCompleteField("OPER", this.fieldHeaderRec.getNmbr()); //$NON-NLS-1$
				xml_data1.addCompleteField("WHO", endOrSuspendFlag); //$NON-NLS-1$
				xml_data1.addCloseTag("DATA"); //$NON-NLS-1$
				xml_data1.finalizeXML();

				StringBuffer amsg = new StringBuffer(67);
				amsg.append("Retrieving List of Locations for Celltype = "); //$NON-NLS-1$
				amsg.append(config.get8CharCellType());
				amsg.append(" Please Wait..."); //$NON-NLS-1$

				MFSTransaction rtv_wulocs = new MFSXmlTransaction(xml_data1.toString());
				rtv_wulocs.setActionMessage(amsg.toString());
				MFSComm.getInstance().execute(rtv_wulocs, this);
				rc = rtv_wulocs.getReturnCode();

				if (rc != 0)
				{
					String erms = rtv_wulocs.getErms();
					IGSMessageBox.showOkMB(getParentFrame(), null, erms, null);
				}
				else
				{
					MfsXMLParser xmlParser = new MfsXMLParser(rtv_wulocs.getOutput());
					String tempLoc = ""; //$NON-NLS-1$
					String tempDesc = ""; //$NON-NLS-1$
					try
					{
						tempLoc = xmlParser.getField("WLOC"); //$NON-NLS-1$
						tempDesc = xmlParser.getField("ADES"); //$NON-NLS-1$
						if (tempLoc.length() == 0)
						{
							rc = 10;
							String erms = "No Locations found?"; //$NON-NLS-1$
							IGSMessageBox.showOkMB(getParentFrame(), null, erms, null);
						}
					}
					catch (MISSING_XML_TAG_EXCEPTION e)
					{
						rc = 10;
						String erms = "No Locations found?"; //$NON-NLS-1$
						IGSMessageBox.showOkMB(getParentFrame(), null, erms, null);
					}
					if (rc == 0)
					{
						StringBuffer tempLocCollector = new StringBuffer();
						tempLocCollector.append("NONE      No Location to Specify                  "); //$NON-NLS-1$

						while (!tempLoc.equals("")) //$NON-NLS-1$
						{
							tempLocCollector.append((tempLoc + "0000").substring(0, 4)); //$NON-NLS-1$
							tempLocCollector.append("      "); //$NON-NLS-1$
							tempLocCollector.append(tempDesc);

							tempLoc = xmlParser.getNextField("WLOC"); //$NON-NLS-1$
							tempDesc = xmlParser.getNextField("ADES"); //$NON-NLS-1$
						}

						MFSGenericListDialog myLocJD = new MFSGenericListDialog(
								getParentFrame(), "Select A Location from the List", //$NON-NLS-1$
								"Pick A Location"); //$NON-NLS-1$
						myLocJD.setSizeLarge();
						myLocJD.loadAnswerListModel(tempLocCollector.toString(), 50);
						myLocJD.setDefaultSelection("TURKEY"); //$NON-NLS-1$
						myLocJD.setLocationRelativeTo(this);
						myLocJD.setVisible(true);

						if (myLocJD.getProceedSelected())
						{
							if (!myLocJD.getSelectedListOption().equals("NONE")) //$NON-NLS-1$
							{
								this.fieldEndOrSuspendLoc = myLocJD.getSelectedListOption().substring(0, 4);
							}
							else
							{
								this.fieldEndOrSuspendLoc = "    "; //$NON-NLS-1$
							}
						}
						else
						{
							rc = 10;
						}
					}//end of valid data sent to client
				}//end of good call to RTV_WULOCS
			}//end of configured to LOC this work unit on a suspend or end
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}

		if (this.fieldRowVector.size() > 0)
		{
			getRowVectorElementAt(this.fieldActiveRow).getPNList().requestFocusInWindow();
		}
		else
		{
			if (endOrSuspendFlag.equals("S")) //$NON-NLS-1$
			{
				this.pbSuspend.requestFocusInWindow();
			}
			else
			{
				this.pbEnd.requestFocusInWindow();
			}
		}
		return rc;
	}

	/**
	 * Logs a blue row instruction.
	 * @param row the index of the row
	 * @param trigger either keyPressed or a log type
	 */
	public void logBlueInstruction(int row, String trigger)
	{
		boolean found = false;
		boolean foundNPI = false;
		int newRow = 0;
		int index = 0;
		JList tempList = null;
		MFSComponentListModel tempLm = null;
		boolean pressedCancel = false;

		MFSPartInstructionJPanel pip = getRowVectorElementAt(row);

		MFSAcknowledgeInstructionDialog ackD = new MFSAcknowledgeInstructionDialog(getParentFrame());
		ackD.setLocationRelativeTo(this.pnlButtons);
		ackD.setVisible(true);
		if (ackD.getPressedLog() == false)
		{
			pip.unDoChangeColor();
			pressedCancel = true;
			this.fieldBlueRow = -1;
		}
		else
		{
			//completed instruction
			pip.setCompletionStatus(MFSPartInstructionJPanel.COMPLETE);
			pip.setNoPartPanelBackground(Color.cyan);
		}

		//find the next row to work on
		newRow = row + 1;
		MFSComponentRec next;

		while (newRow < this.fieldRowVector.size() && !found && !foundNPI)
		{
			MFSPartInstructionJPanel newPip = getRowVectorElementAt(newRow);
			if (!newPip.getIsNonPartInstruction())
			{
				index = 0;
				tempList = newPip.getPNList();
				tempLm = newPip.getPNListModel();

				while (index < tempLm.size() && !found)
				{
					next = tempLm.getComponentRecAt(index);

					if (next.getIdsp().equals("X") //$NON-NLS-1$
							|| (next.getIdsp().equals("A") //$NON-NLS-1$
									&& !next.getMlri().equals(" ") //$NON-NLS-1$
									&& !next.getMlri().equals("0"))) //$NON-NLS-1$
					{
						found = true;
					}
					else
					{
						index++;
					}
				}
			}//end of if non-part

			// we are progressing to a non-part row.
			//If we are not complete we will stop here
			else
			{
				if (!newPip.getInstructionRec().getCompletionStatus().equals("C")) //$NON-NLS-1$
				{
					foundNPI = true;
				}
			}

			if (!found && !foundNPI)
			{
				newRow++;
			}
		}//end of while loop

		//now start at top and look for a row
		if (!found && !foundNPI)
		{
			newRow = 0;
			while (newRow < row && !found && !foundNPI)
			{
				MFSPartInstructionJPanel newPip = getRowVectorElementAt(newRow);
				if (!newPip.getIsNonPartInstruction())
				{
					index = 0;
					tempList = newPip.getPNList();
					tempLm = newPip.getPNListModel();

					while (index < tempLm.size() && !found)
					{
						next = tempLm.getComponentRecAt(index);

						if (next.getIdsp().equals("X") //$NON-NLS-1$
								|| (next.getIdsp().equals("A") //$NON-NLS-1$
										&& !next.getMlri().equals(" ") //$NON-NLS-1$ 
										&& !next.getMlri().equals("0"))) //$NON-NLS-1$
						{
							found = true;
						}
						else
						{
							index++;
						}
					}
				}//end of if non-part

				//we are progressing to a non-part row. 
				//If we are not complete we will stop here
				else
				{
					if (!newPip.getInstructionRec().getCompletionStatus().equals("C")) //$NON-NLS-1$
					{
						foundNPI = true;
					}
				}

				if (!found && !foundNPI)
				{
					newRow++;
				}
			}//end of while loop
		}

		//we are done now, let's do something based on what we found
		//if we found a NonPart Instruction we will make it the next blueRow
		if (foundNPI)
		{
			this.fieldBlueRow = newRow;
			this.spPartsInst.requestFocusInWindow();
			MFSPartInstructionJPanel pip2 = getRowVectorElementAt(newRow);
			this.spPartsInst.getVerticalScrollBar().setValue((int) pip2.getBounds().getY());
			pip2.changeColor(MFSPartInstructionJPanel.BLUE_ROW_COLOR);
			
			if (!trigger.equals("keyPressed") && !pressedCancel) //$NON-NLS-1$
			{
				logBlueInstruction(newRow, trigger);
			}
		}
		//if we found a part to be added, we will make it the next activeRow
		else if (found)
		{
			this.spPartsInst.requestFocusInWindow();
			MFSPartInstructionJPanel pip2 = getRowVectorElementAt(newRow);
			this.spPartsInst.getVerticalScrollBar().setValue((int) pip2.getBounds().getY());
			tempList.setSelectedIndex(index);
			tempList.ensureIndexIsVisible(index);
			tempList.requestFocusInWindow();
			
			if (!trigger.equals("keyPressed") && !pressedCancel) //$NON-NLS-1$
			{
				showLogPart(trigger);
			}
		}
		else //move screen to top
		{
			this.spPartsInst.requestFocusInWindow();
			MFSPartInstructionJPanel pip2 = getRowVectorElementAt(0);
			this.spPartsInst.getVerticalScrollBar().setValue((int) pip2.getBounds().getY());
		}
	}

	/** Invoked when {@link #pbMove} is selected to move a work unit. */
	public void move()
	{
		try
		{
			int rc = 0;
			removeMyListeners();

			/* start the RTV_OEMO transaction thread */
			StringBuffer data = new StringBuffer();
			data.append("RTV_OEMO  "); //$NON-NLS-1$
			data.append(this.fieldHeaderRec.getPrln());
			data.append(this.fieldHeaderRec.getNmbr());
			data.append(this.fieldHeaderRec.getProd());

			MFSTransaction rtv_oemo = new MFSFixedTransaction(data.toString());
			rtv_oemo.setActionMessage("Retrieving List of Operations, Please Wait..."); //$NON-NLS-1$
			MFSComm.getInstance().execute(rtv_oemo, this);
			rc = rtv_oemo.getReturnCode();

			if (rc == 0 && rtv_oemo.getOutput().length() != 0)
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

				MFSOperationsDialog operJD = new MFSOperationsDialog(getParentFrame());
				operJD.loadOperListModel(ops, "0000"); //$NON-NLS-1$
				operJD.setSelectedIndex(0);
				operJD.setLocationRelativeTo(getParentFrame()); //~17A
				operJD.setVisible(true);
				if (operJD.getProceedSelected())
				{
					rc = updt_crwc();
					if (rc == 0)
					{
						rc = end_wu("F", "00", operJD.getSelectedListOption()); //$NON-NLS-1$ //$NON-NLS-2$
						this.fieldEndCode = rc;
						if (rc == 30 || rc == 31)
						{
							addMyListeners();
							restorePreviousScreen();
						}
						else
						{
							addMyListeners();
							focusPNListOrButton(this.pbMove);
						}
					}
				}
				else
				{
					addMyListeners();
					focusPNListOrButton(this.pbMove);
				}
			}
			else if (rtv_oemo.getOutput().length() == 0)
			{
				String erms = "No Operation Ending modes are available"; //$NON-NLS-1$
				IGSMessageBox.showOkMB(getParentFrame(), null, erms, null);

				addMyListeners();
				focusPNListOrButton(this.pbMove);
			}
			else
			{
				IGSMessageBox.showOkMB(getParentFrame(), null, rtv_oemo.getErms(), null);

				addMyListeners();
				focusPNListOrButton(this.pbMove);
			}
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);

			addMyListeners();
			focusPNListOrButton(this.pbMove);
		}
	}


	/** Invoked when {@link #pbReprintNCMTag} is selected to reprint an NCM Tag. */
	public void ncmreprint()
	{
		try
		{
			removeMyListeners();

			MFSRejectedPartTagDialog myRPTD = new MFSRejectedPartTagDialog(getParentFrame());

			JList tmpList = getRowVectorElementAt(this.fieldActiveRow).getPNList();
			MFSComponentListModel tmpLm = (MFSComponentListModel) tmpList.getModel();

			if (tmpLm.size() != 0)
			{
				MFSComponentRec cmp = tmpLm.getComponentRecAt(tmpList.getSelectedIndex());

				if (!cmp.getRejs().equals("")) //$NON-NLS-1$
				{
					myRPTD.setRejs(cmp.getRejs());
				}
			}
			
			myRPTD.setLocationRelativeTo(getParentFrame()); //~17A
			myRPTD.setVisible(true);

			if (myRPTD.getPressedEnter())
			{
				boolean foundNCMconfig = false;
				final MFSConfig config = MFSConfig.getInstance();

				if (config.containsConfigEntry("NCMTAG")) //$NON-NLS-1$
				{
					MFSPrintingMethods.ncmtag(myRPTD.getRejs(), 1, getParentFrame());
					foundNCMconfig = true;
				}
				if (config.containsConfigEntry("NCMSHEET")) //$NON-NLS-1$
				{
					MFSPrintingMethods.ncmsheet(myRPTD.getRejs(), 1, getParentFrame());
					foundNCMconfig = true;
				}
				if (config.containsConfigEntry("NCMBIGTAG")) //$NON-NLS-1$
				{
					MFSPrintingMethods.ncmbigtag(myRPTD.getRejs(), 1, getParentFrame());
					foundNCMconfig = true;
				}
				if (!foundNCMconfig)
				{
					String erms = "No NCM labels are configured!"; //$NON-NLS-1$
					IGSMessageBox.showOkMB(getParentFrame(), null, erms, null);
				}
			}
		}//end of try
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}

		addMyListeners();
		focusPNListOrButton(this.pbReprintNCMTag);
	}

	//~6A new method
	/** Creates a new container. */
	private void newContainer()
	{
		int rc = 0;

		rc = updatePartContainers();
		if (rc == 0)
		{
			// Now creates the container
			Object[] options = {"YES", "NO"}; //$NON-NLS-1$ //$NON-NLS-2$

			int n = JOptionPane.showOptionDialog(getParentFrame(),
					"Do you really want to create a new Container?", //$NON-NLS-1$
					"Create New Container", JOptionPane.YES_NO_OPTION, //$NON-NLS-1$
					JOptionPane.WARNING_MESSAGE, null, options, options[1]);

			if (n == JOptionPane.YES_OPTION)
			{
				/* include 10 blank character container number to get user in the right place */
				String data = "RTV_CNTR  " + getCurrMctl() + "N" + //$NON-NLS-1$ //$NON-NLS-2$
				         BLANK_CNTR + MFSConfig.getInstance().get8CharUser();  /* ~25C */

				MFSTransaction rtv_cntr = new MFSFixedTransaction(data);
				rtv_cntr.setActionMessage("Retrieving Container Info, Please Wait..."); //$NON-NLS-1$
				MFSComm.getInstance().execute(rtv_cntr, this);
				rc = rtv_cntr.getReturnCode();

				if (rc == 0)
				{
					/*~8 Save currently assigned container, to send it to the rsspcpp trx later*/
					String prevCntr = this.fieldHeaderRec.getCntr();
					
					/* ~43A On Demand Printing - send the header rec with the old container */
					onDemandTrigger(this.fieldHeaderRec, "NEWCNTR"); //$NON-NLS-1$
					
					updt_cntr(rtv_cntr.getOutput());
					this.update(getGraphics());
					/* ~8 send the prevCntr instead of the new one */
					MFSPrintingMethods.pcpplabels(prevCntr, this.fieldHeaderRec.getMctl(), "NEW", 1,       							 //$NON-NLS-1$
							"NONE",getParentFrame()); //~20C					 //$NON-NLS-1$
				}
			}
		}

	}
	
	//~43A
	/** Invoked when {@link #pbOnDemand} is selected. */
	private void onDemandReprint()
	{
		try
		{
			this.removeMyListeners();
			
			// Create key data for label printing
			MFSOnDemandKeyData odKeyData = new MFSOnDemandKeyData();
			odKeyData.setTriggerSource("DIRWORK"); //$NON-NLS-1$
			odKeyData.setTriggerKey("*NONE"); //$NON-NLS-1$
			odKeyData.setDataSource(this.fieldHeaderRec);
			
			// Start label printing process
			MFSOnDemand odLabel = new MFSOnDemand(getParentFrame(), odKeyData);
			odLabel.labelReprint();
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, e.getMessage(), null);
		}
		finally
		{
			this.addMyListeners();
		}		
	}
	
	
	//~43A
	/** 
	 * Creates a <code>MFSOnDemand</code> object to print on demand labels.
	 * @param Object source to retrieve data from
	 */
	private void onDemandTrigger(Object source, String triggerSource)
	{
		try
		{
			// Create key data for label printing
			MFSOnDemandKeyData odKeyData = new MFSOnDemandKeyData();
			odKeyData.setTriggerSource(triggerSource);
			odKeyData.setDataSource(source);
			
			// Start label printing process
			MFSOnDemand odLabel = new MFSOnDemand(getParentFrame(), odKeyData);
			odLabel.labelTrigger();
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, e.getMessage(), null);
		}
	}

	/**
	 * The method gathers the part number and serial number of the selected part
	 * and gets the current part detail for that part. It gives the user the
	 * opportunity to change the pn/sn of the part for which he/she wants detail
	 * before proceeding.
	 */
	public void partFunctions()
	{
		try
		{
			//~16A Redone to move getPNSN logic into this function and
			// use the MFSPartFunctionDisplayPanel.partDetail method
			removeMyListeners();
			String part = ""; //$NON-NLS-1$
			String serial = ""; //$NON-NLS-1$

			if (!this.fieldRowVector.isEmpty())
			{   // ~22 Check if it is not a NonPartInstruction before trying to get part and serial
				MFSPartInstructionJPanel tmpPanel = getRowVectorElementAt(this.fieldActiveRow);
				if(!tmpPanel.getIsNonPartInstruction())
				{
					JList tmpList = tmpPanel.getPNList();
					MFSComponentListModel tmpLm = (MFSComponentListModel) tmpList.getModel();

					if (tmpList.getSelectedIndex() != -1)
					{
						MFSComponentRec cmp = tmpLm.getComponentRecAt(tmpList.getSelectedIndex());
						/* Make sure the part is installed */
						if (cmp.getIdsp().equals("I") || cmp.getIdsp().equals("R")) //$NON-NLS-1$ //$NON-NLS-2$
						{
							part = cmp.getInpn();
							serial = cmp.getInsq();
						}
					}
				}
			}

			MFSPNSNDialog myPNSNDialog = new MFSPNSNDialog(getParentFrame());
			myPNSNDialog.setPNText(part);
			myPNSNDialog.setSNText(serial);
			myPNSNDialog.setLocationRelativeTo(getParentFrame()); //~17A
			myPNSNDialog.setVisible(true);

			if (myPNSNDialog.getPressedEnter())
			{
				part = myPNSNDialog.getPNText();
				serial = myPNSNDialog.getSNText();

				if (!part.trim().equals("") && !serial.trim().equals("")) //$NON-NLS-1$ //$NON-NLS-2$
				{
					MFSPartFunctionDisplayPanel partFunc = new MFSPartFunctionDisplayPanel(
							getParentFrame(), this, part, serial);
					this.fieldChildPanel = partFunc;
					partFunc.partDetail(false, false);
				}
				else
				{
					String erms = "Part information incomplete!  Please make sure the part is installed."; //$NON-NLS-1$
					IGSMessageBox.showOkMB(getParentFrame(), null, erms, null);
				}
			}
		}//end of try
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}

		addMyListeners();
		focusPNListOrButton(this.pbPartFunctions);
	}

	//WHEN			WHO				PTR/IPSR	DESC
	//05/17/2004	Dan Kloepping	PTR25455KK 	INITIAL CREATION
	//06/01/2004	Dan Kloepping				We don't necessarily have any components when the rescan of wwnn takes place
	//											and since only mctl is needed for a rescan, we'll creat a generic comp_rec
	//											and set the mctl to the mctl of the header then call the trx.
	//06/01/2004	Dan Kloepping				Moved the logic for calling the mfsutilities.MfsTransaction into the WWNNJDialog
	/** 
	 * Displays an <code>MFSWWNNDialog</code>.
	 * @return 0 on success; nonzero on error
	 */
	public int prcsWWNN()
	{
		int rc = 0;

		try
		{
			System.out.println("Creating component record inside prcsWWNN()..."); //$NON-NLS-1$
			MFSComponentRec compRec = new MFSComponentRec();
			compRec.setMctl(this.fieldHeaderRec.getMctl());

			MFSWWNNDialog wwnnJD = new MFSWWNNDialog(getParentFrame(), compRec, MFSWWNNDialog.LT_RESCAN);
			wwnnJD.initDisplay();
			wwnnJD.setLocationRelativeTo(getParentFrame()); //~17A
			wwnnJD.setVisible(true);
			rc = wwnnJD.getReturnCode();
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
		return rc;
	}

	//~16A new method
	/**
	 * Prepares the panel for display by setting the text of text components,
	 * storing the value of the mctl, and resetting the end code to 0.
	 * @param mctl the mctl of the work unit
	 */
	public void prepareForDisplay(String mctl)
	{
		this.fieldCurrMctl = mctl;
		this.fieldEndCode = 0;

		this.tpInstalledParts.setText(""); //$NON-NLS-1$
		this.lblUser.setText("User:  " + MFSConfig.getInstance().getConfigValue("USER"));  //$NON-NLS-1$//$NON-NLS-2$
		this.lblMctl.setText("Mctl:  " + mctl); //$NON-NLS-1$
		initSapsAndSapoLabels();
		updt_cntr(this.fieldHeaderRec.getCntr());
		initHeaderLabels();
	}

	/**
	 * Determines if processors match by comparing part numbers.
	 * @param inpn the part number
	 * @param item the item
	 * @return the error string
	 */
	public String processorMatch(String inpn, String item)
	{
		String rcStr = ""; //$NON-NLS-1$
		try
		{
			int row = 0;
			int index = 0;
			if (this.fieldRowVector.size() > 0)
			{
				while (row < this.fieldRowVector.size())
				{
					MFSPartInstructionJPanel pip = getRowVectorElementAt(row);
					if (!pip.getIsNonPartInstruction())
					{
						index = 0;
						MFSComponentListModel tempLm = pip.getPNListModel();
						while (index < tempLm.size())
						{
							MFSComponentRec cmp = tempLm.getComponentRecAt(index);
							if (cmp.getItem().equals(item)
									&& (cmp.getIdsp().equals("I") //$NON-NLS-1$ 
											|| cmp.getIdsp().equals("R"))) //$NON-NLS-1$
							{
								if (!cmp.getInpn().equals(inpn))
								{
									rcStr = "Invalid Part to Add - Please use " + cmp.getInpn(); //$NON-NLS-1$
								}
							}
							index++;
						}
					}//part instruction
					row++;
				}//while more rows
			}//at least one row in the row vector
		}
		catch (Exception e)
		{
			rcStr = "Exception Occurred " + e; //$NON-NLS-1$
			System.out.println("Program Exception: " + e); //$NON-NLS-1$
			e.printStackTrace(System.out);
		}
		return rcStr;
	}

	/**
	 * Determines if processors match by calling the MPA_CHECK transaction.
	 * @param inpn the part number
	 * @param item the item
	 * @return the error string
	 */
	public String processorMatchMPARule(String inpn, String item)
	{
		String rcStr = ""; //$NON-NLS-1$
		try
		{
			int row = 0;
			int index = 0;
			boolean done = false;
			int rc = 0;

			if (this.fieldRowVector.size() > 0)
			{
				while (row < this.fieldRowVector.size() && !done)
				{
					MFSPartInstructionJPanel pip = getRowVectorElementAt(row);
					if (!pip.getIsNonPartInstruction())
					{
						index = 0;
						MFSComponentListModel tempLm = pip.getPNListModel();
						while (index < tempLm.size() && !done)
						{
							MFSComponentRec cmp = tempLm.getComponentRecAt(index);
							if (cmp.getItem().equals(item)
									&& (cmp.getIdsp().equals("I") //$NON-NLS-1$ 
											|| cmp.getIdsp().equals("R"))) //$NON-NLS-1$
							{
								//found an installed part with the same called out part. We want to make sure
								//the installed part we found and the one we are trying to install are in the same
								//processor group (as defined by the MPA rules on the server)
								done = true;

								MfsXMLDocument xml_data1 = new MfsXMLDocument("MPA_CHECK"); //$NON-NLS-1$
								xml_data1.addOpenTag("DATA"); //$NON-NLS-1$
								xml_data1.addCompleteField("IPN1", inpn); //$NON-NLS-1$
								xml_data1.addCompleteField("IPN2", cmp.getInpn()); //$NON-NLS-1$
								xml_data1.addCloseTag("DATA"); //$NON-NLS-1$
								xml_data1.finalizeXML();

								MFSTransaction mpa_check = new MFSXmlTransaction(xml_data1.toString());
								MFSComm.getInstance().execute(mpa_check, this);
								rc = mpa_check.getReturnCode();

								if (rc != 0)
								{
									MfsXMLParser xmlParser = new MfsXMLParser(mpa_check.getOutput());
									rcStr = xmlParser.getField("ERMS"); //$NON-NLS-1$
								}
							}//end of cmp found

							else
							{
								index++;
							}
						}
					}//part instruction

					row++;

				}//while more rows
			}//at least one row in the row vector
		}
		catch (Exception e)
		{
			rcStr = "Exception Occurred in processorMatchMPARule " + e; //$NON-NLS-1$
			System.out.println("Program Exception: " + e); //$NON-NLS-1$
			e.printStackTrace(System.out);
		}
		return rcStr;
	}
	
	
	//~35A New method
	/** Perform the processVrfyKit method. */
	public void processVrfyKit()
	{
		String rcPart = ""; //$NON-NLS-1$
		String inpnPart = ""; //$NON-NLS-1$
		String insqPart = ""; //$NON-NLS-1$
		String inecPart = ""; //$NON-NLS-1$
		String incaPart = ""; //$NON-NLS-1$
		String mspiPart = ""; //$NON-NLS-1$
		String mcsnPart = ""; //$NON-NLS-1$
		String crctPart = ""; //$NON-NLS-1$
		String qntyPart = ""; //$NON-NLS-1$
		String cwunPart = ""; //$NON-NLS-1$
		IGSXMLDocument wrongParts = new IGSXMLDocument();
		boolean crctFound = false;
		int index = 0;
		
		IGSXMLTransaction vrfy_kit = new IGSXMLTransaction("VRFY_KIT"); //$NON-NLS-1$
		vrfy_kit.startDocument(); 
		vrfy_kit.addElement("MFGN", this.fieldHeaderRec.getMfgn()); //$NON-NLS-1$
		vrfy_kit.addElement("IDSS", this.fieldHeaderRec.getIdss()); //$NON-NLS-1$
		vrfy_kit.addElement("MCTL", this.fieldHeaderRec.getMctl()); //$NON-NLS-1$
		vrfy_kit.addElement("OPNM", this.fieldHeaderRec.getNmbr()); //$NON-NLS-1$
		vrfy_kit.addElement("PRLN", this.fieldHeaderRec.getPrln()); //$NON-NLS-1$
		vrfy_kit.addElement("CTYP", MFSConfig.getInstance().get8CharCellType()); //$NON-NLS-1$
		vrfy_kit.endDocument(); 
		vrfy_kit.setActionMessage("Retrieving Kitting Parts, Please Wait..."); //$NON-NLS-1$
		
		MFSComm.getInstance().execute(vrfy_kit, this);
		
		if(vrfy_kit.getReturnCode() == 0)
		{			
			while(vrfy_kit.stepIntoElement("RCD") != null) //$NON-NLS-1$
			{
				rcPart = vrfy_kit.getNextElement("RC"); //$NON-NLS-1$
				
				if(rcPart.equals("0")) //$NON-NLS-1$
				{
					index = 0;
					inpnPart = vrfy_kit.getNextElement("INPN"); //$NON-NLS-1$
					insqPart = vrfy_kit.getNextElement("INSQ"); //$NON-NLS-1$
					inecPart = vrfy_kit.getNextElement("INEC"); //$NON-NLS-1$
					incaPart = vrfy_kit.getNextElement("INCA"); //$NON-NLS-1$
					mspiPart = vrfy_kit.getNextElement("MSPI"); //$NON-NLS-1$
					mcsnPart = vrfy_kit.getNextElement("MCSN"); //$NON-NLS-1$
					crctPart = vrfy_kit.getNextElement("CRCT"); //$NON-NLS-1$
					qntyPart = vrfy_kit.getNextElement("QNTY"); //$NON-NLS-1$
					cwunPart = vrfy_kit.getNextElement("CWUN"); //$NON-NLS-1$
					crctFound = false;
					
					while((index < this.fieldComponentListModel.size()) && (crctFound == false))
					{
						MFSComponentRec compRec = getComponentListModelCompRecAt(index);
						
						if(compRec.getCrct().equals(crctPart))
						{
							compRec.setRec_changed(true);
							crctFound = true;

							int fqty = Integer.parseInt(compRec.getFqty()) + Integer.parseInt(qntyPart);
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
								compRec.setInec(inecPart);
							}

							if (!compRec.isPnriDoNotCollect())
							{
								compRec.setInpn(inpnPart);
							}
							else
							{
								compRec.setInpn(compRec.getItem());
							}

							if (!compRec.isCsniDoNotCollect())
							{
								compRec.setInsq(insqPart);
							}

							if (!compRec.isCcaiDoNotCollect())
							{
								compRec.setInca(incaPart);
							}

							if (!compRec.isPariDoNotCollect())
							{
								compRec.setCwun(cwunPart);
							}
							
							if (!compRec.isCmtiDoNotCollect())
							{
								if (compRec.getMcsn().substring(0, 1).equals("$") //$NON-NLS-1$
										|| compRec.getCmti().toUpperCase().equals("M")) //$NON-NLS-1$
								{
									compRec.setMspi(mspiPart);
									compRec.setMcsn(mcsnPart.substring(2));
								}
							}

							compRec.setCntr(this.fieldHeaderRec.getCntr());

							compRec.updtDisplayString();
							compRec.updtIRDisplayString();
							compRec.updtInstalledParts();

							// print out the tracksub3 label, before we increment
							// through to the next part to add
							// this will make sure we are printing the label for the
							// correct part
							String cnfgData1 = "TRACKSUB3," + this.fieldHeaderRec.getNmbr(); //$NON-NLS-1$

							String value = MFSConfig.getInstance().getConfigValue(cnfgData1);
							if (value.equals(MFSConfig.NOT_FOUND))
							{
								String cnfgData2 = "TRACKSUB3,*ALL"; //$NON-NLS-1$
								value = MFSConfig.getInstance().getConfigValue(cnfgData2);
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
							
							/* ******************* ~35A *********************************** */
							if(compRec.getIsTcodPart())
							{
								String errorString = EMPTY_STRING;
								errorString = tcodTrxLogic(compRec);
							
								if(errorString.length() > 0)
								{
									String title = "Error processing TCOD trxs"; //$NON-NLS-1$
									IGSMessageBox.showOkMB(getParentFrame(), title, errorString, null);
								}
							}	
						}
						index++;
					}
				}
				else if(rcPart.equals("100")) //$NON-NLS-1$
				{
					wrongParts.startElement("RCD"); //$NON-NLS-1$
					wrongParts.addElement("INPN",vrfy_kit.getNextElement("INPN")); //$NON-NLS-1$ //$NON-NLS-2$
					wrongParts.addElement("INSQ",vrfy_kit.getNextElement("INSQ")); //$NON-NLS-1$ //$NON-NLS-2$
					wrongParts.addElement("MCTL",this.fieldHeaderRec.getMctl()); //$NON-NLS-1$
					wrongParts.addElement("WOHE","2"); //$NON-NLS-1$ //$NON-NLS-2$
					wrongParts.addElement("ERMS",vrfy_kit.getNextElement("ERMS")); //$NON-NLS-1$ //$NON-NLS-2$
					wrongParts.endElement("RCD"); //$NON-NLS-1$
				}
					
				vrfy_kit.stepOutOfElement();
			}
		}
		else
		{
			if(vrfy_kit.getReturnCode() != 100)
			{
				String title = "MassKIT Installs that could not be Completed"; //$NON-NLS-1$
				IGSMessageBox.showOkMB(getParentFrame(), title, vrfy_kit.getErms(), null);
				vrfy_kit.getReturnCode();
			}
		}
		/* Remove mas Kit Install Message for parts that where not installed 
		if(wrongParts.getLength() != 0)
		{
			this.showPNSNErrorList(wrongParts.toString(),0);
		}*/
	}

	/**
	 * Redoes the layout after the <code>MFSPartInstructionJPanel</code> at
	 * the specified <code>row</code> in {@link #pnlRowHolder} changed.
	 * @param row the row of the <code>MFSPartInstructionJPanel</code>
	 */
	public void redoLayout(int row)
	{
		//~16C This method used to remove the row from pnlRowHolder, change its
		// display properties and GridBagConstraints, and add it back in to
		// change how the row was displayed. With the change in the layout of
		// the rows, this should no longer be necessary. Instead, invalidate
		// validate, and update are called to redo the layout.
		invalidate();
		validate();
		update(getGraphics());
	}

	/** Removes the listeners from this panel's <code>Component</code>s. */
	protected void removeMyListeners()
	{
		//~16 Redone to use button iterator and remove PTR35927JM code
		this.fieldButtonIterator.reset();
		while (this.fieldButtonIterator.hasNext())
		{
			JButton next = this.fieldButtonIterator.nextJButton();
			next.removeActionListener(this);
			next.removeKeyListener(this);
		}

		for (int i = 0; i < this.fieldRowVector.size(); i++)
		{
			JList pipPNList = getRowVectorElementAt(i).getPNList();
			pipPNList.removeKeyListener(this);
			pipPNList.removeMouseListener(this);
		}
		
		this.spPartsInst.removeKeyListener(this);
	}

	/**
	 * Called to remove a part.
	 * @param logType the log type for the <code>MFSLogPartDialog</code>
	 */
	public void removepn(String logType)
	{
		try
		{
			removeMyListeners();
			/* start at activeRow and work way to the bottom looking for a part */
			/* still now found so start at top and work down to activeRow */
			//~19A
			MFSComponentSearch componentFinder = new MFSComponentSearch(
					this.fieldRowVector,
					new MFSComponentSearchCriteria.
						MFSPartToRemoveSearchCriteria(this.fieldHeaderRec));

			JList tempList = 
				getRowVectorElementAt(this.fieldActiveRow).getPNList();
			
			componentFinder.setSearchStartPosition(this.fieldActiveRow,
					tempList.getSelectedIndex());

			if(componentFinder.findComponent())
			{
				if (this.fieldHeaderRec.getNmbr().equals("FKIT") //$NON-NLS-1$
						&& this.fieldCurrentCompRec.getIdsp().equals("I") //$NON-NLS-1$
						&& this.fieldCurrentCompRec.getInvs().equals("Z")) //$NON-NLS-1$
				{
					String erms = "Invalid Part to Remove - Already Installed in a Machine!"; //$NON-NLS-1$
					IGSMessageBox.showOkMB(getParentFrame(), null, erms, null);
				}
				else
				{
					//~33A - Need to prevent CSNI of L parts from being removed or reworked. (CSC)
					if (this.fieldCurrentCompRec.getCsni().equals("L")) //$NON-NLS-1$
					{
						String erms = "Invalid Part to Remove"; //$NON-NLS-1$
						IGSMessageBox.showOkMB(getParentFrame(), null, erms, null);
					}					
					else
					{
						MFSPartInstructionJPanel pip = 
							getRowVectorElementAt(componentFinder.getRow());
						pip.getPNList().setSelectedIndex(componentFinder.getIndex());
						pip.getPNList().ensureIndexIsVisible(componentFinder.getIndex());
						//getting a bit tricky here. set the logpart dialog
						// partlist to the active row part list
						// and the list model to the overall list model (inclusive
						// of all parts) from the DW panel
						JList list = getRowVectorElementAt(this.fieldActiveRow).getPNList();
						MFSLogPartDialog myLogPartD = new MFSLogPartDialog(this, logType,
								this.fieldHeaderRec, list, this.fieldComponentListModel);    
	
						// give the log Part dialog the rowVector. Should be able to
						// get at everything it needs from the rowVector
						// (part lists and instructions)
						myLogPartD.setRowInfo(this.fieldRowVector, this.fieldActiveRow);
						myLogPartD.setLocationRelativeTo(this.pnlButtons);
						myLogPartD.initDisplay();
						myLogPartD.setVisible(true);
					}
				}
			}
			else
			{
				String erms = "There are No Parts That Can Be Removed"; //$NON-NLS-1$
				IGSMessageBox.showOkMB(getParentFrame(), null, erms, null);
			}
			if (this.fieldHeaderRec.getWtyp().equals("T") //$NON-NLS-1$
					|| this.fieldHeaderRec.getWtyp().equals("C")) //$NON-NLS-1$
			{
				computeCounter();
			}
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}

		addMyListeners();
		focusPNListOrButton(this.pbRemPart);
	}
	//~19A
	/**
	 * Performs the Remove Non-Serialized Part process
	 * @param compRec for the <code>MFSComponentRec</code>
	 * @param partInErrorDialog for the <code>MFSShowPartinErrorDialog</code>
	 * @return <code>true</code> iff an error occurred
	 */
	private boolean removeNonSerProc(MFSComponentRec compRec,
			MFSShowPartinErrorDialog partInErrorDialog)
	{		
		MFSIntStringPair rcPair = MFSLogPartDialog.validateComponentRec(
				Integer.parseInt(compRec.getFqty()),compRec, 
				compRec.getInpn(),compRec.getInsq(), 
				compRec.getInec(),compRec.getInca(), 
				compRec.getCwun());	
		if ( rcPair.fieldInt == 0)
		{
			rcPair = MFSLogPartDialog.updateMCode(compRec,
					compRec.getInpn(),compRec.getInsq(),this);
		}
		if (rcPair.fieldInt == 0)
		{
			rcPair = MFSLogPartDialog.fkitRemoveRTR(compRec,
					this.fieldHeaderRec.getNmbr(),this);
		}
		if (rcPair.fieldInt == 0)
		{
			MFSLogPartDialog.updateComponentRecRemoved(true,  
					Integer.parseInt(compRec.getFqty()),  
					compRec, this.fieldHeaderRec.getNmbr());
		}
		//	Add Error to the Message list
		if(rcPair.fieldInt != 0)
		{
			partInErrorDialog.addMessage(
				compRec.getCrct(),compRec.getInpn(),rcPair.fieldString);
			return true;
		}
		computeCounter();
		return false;
	}
	//~19A
	/** 
	 * Performs the Remove Non-Serialized Parts method for FKit
	 * @param componentFinder for the <code>MFSComponentSearch</code>
	 * @param partInErrorDialog for the <code>MFSShowPartinErrorDialog</code>
	 * @return <code>true</code> iff an error occurred
	 */
	@SuppressWarnings("rawtypes")
	private boolean removeNonSerTearDown(MFSComponentSearch componentFinder ,
			MFSShowPartinErrorDialog partInErrorDialog)
	{
		boolean errorOccurred = false;
		int partsProccesed = 0;
		
		MFSRemNonSerTDDialog partListDialog = 
			new MFSRemNonSerTDDialog(getParentFrame()); 

		/* Loop while we find a component */
		while(componentFinder.findComponent())
		{				
			partListDialog.addComponentRec(
					componentFinder.getMatchingComponentRec());

			partsProccesed ++;
			componentFinder .incrementIndex();
		}
		if(partsProccesed == 0)
		{
			String title = "Error"; //$NON-NLS-1$
			String erms = "No suitable parts found to remove."; //$NON-NLS-1$
			IGSMessageBox.showOkMB(this, title, erms, null);
		}
		else
		{
			// Display part List to remove
			partListDialog.pack();
			partListDialog.setLocationRelativeTo(getParentFrame());
			partListDialog.setVisible(true);
			
			if(partListDialog.getProceedSelected())
			{
				List compRecList = partListDialog.getSelectedComponents();
				for (int i = 0; i < compRecList.size(); i++)
				{
					if(removeNonSerProc((MFSComponentRec)compRecList.get(i),
							partInErrorDialog))
					{
						errorOccurred = true;
					}					
				}			
			}
		}
		return errorOccurred;
	}
	//~19A
	/** 
	 * Performs the Remove Non-Serialized Parts method for FKit
	 * @param componentFinder for the <code>MFSComponentSearch</code>
	 * @param partInErrorDialog for the <code>MFSShowPartinErrorDialog</code>
	 * @return <code>true</code> iff an error occurred
	 */
	private boolean removeNonSerFKit(MFSComponentSearch componentFinder ,
			MFSShowPartinErrorDialog partInErrorDialog)
	{
		boolean errorOccurred = false;
		int partsProccesed = 0;
			
		/* Loop while we find a component */
		while(componentFinder.findComponent())
		{
			if(removeNonSerProc(componentFinder.getMatchingComponentRec(),
					partInErrorDialog))
			{
				errorOccurred = true;
			}
			partsProccesed ++;
			componentFinder.incrementIndex();
		}
		if(partsProccesed == 0)
		{
			String title = "Error"; //$NON-NLS-1$
			String erms = "No suitable parts found to remove."; //$NON-NLS-1$
			IGSMessageBox.showOkMB(this, title, erms, null);
		}
		return errorOccurred;
	}
	//~19A
	/** 
	 * Performs the Remove Non-Serialized Parts method  
	 */
	private void removeNonSer()
	{
		boolean errorOccurred = false;
		
		try
		{
			getParentFrame().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			removeMyListeners();

			MFSComponentSearch componentFinder = new MFSComponentSearch(
					this.fieldRowVector,
					new MFSComponentSearchCriteria.
					MFSNonSerPartToRemoveSearchCriteria(this.fieldHeaderRec));
								
			MFSShowPartinErrorDialog partInErrorDialog = 
				new MFSShowPartinErrorDialog(getParentFrame()); 

			if(this.fieldHeaderRec.getNmbr().equals("FKIT")) //$NON-NLS-1$
			{
				String msg = "Continue to remove all non-serialized components?."; //$NON-NLS-1$

				if(IGSMessageBox.showYesNoMB(this, null, msg, null))
				{				
					errorOccurred = 
						removeNonSerFKit(componentFinder,partInErrorDialog);
				}
			}
			else if(this.fieldHeaderRec.getNmbr().equals("TEAR")) //$NON-NLS-1$
			{
				errorOccurred = 
					removeNonSerTearDown(componentFinder,partInErrorDialog);					
			}
			if(errorOccurred) 
			{
				// Display Part in Error Dialog
				partInErrorDialog.pack();
				partInErrorDialog.setLocationRelativeTo(getParentFrame());
				partInErrorDialog.setVisible(true);
			}
			getParentFrame().setCursor(Cursor.getDefaultCursor());
		}
		catch (Exception e)
		{
			getParentFrame().setCursor(Cursor.getDefaultCursor());
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
		update(getGraphics());
		addMyListeners();		
		focusPNListOrButton(this.pbRemNonSer);
	}
	
	/** Calls the UPDT_ICNT transaction to reset the Interposer Count. */
	public void resetInterposer()
	{
		try
		{
			int rc = 0;
			boolean foundSome = false;

			removeMyListeners();

			MFSComponentRec next = null;
			int index = 0;
			if (this.fieldComponentListModel.size() == 0)
			{
				/* display error to user */
				rc = 10;
				String erms = "No Parts in Work Unit Yet!"; //$NON-NLS-1$
				IGSMessageBox.showOkMB(getParentFrame(), null, erms, null);
			}
			while (index < this.fieldComponentListModel.size() && rc == 0)
			{
				next = this.fieldComponentListModel.getComponentRecAt(index);

				if (next.getIdsp().equals("I") //$NON-NLS-1$
						&& !next.getInterposerResetFlag().equals(">")) //$NON-NLS-1$
				{
					foundSome = true;

					final int slashIndex = this.fieldLoc.indexOf("/"); //$NON-NLS-1$
					MfsXMLDocument xml_data = new MfsXMLDocument("UPDT_ICNT"); //$NON-NLS-1$
					xml_data.addOpenTag("DATA"); //$NON-NLS-1$
					xml_data.addCompleteField("INPN", next.getInpn()); //$NON-NLS-1$
					xml_data.addCompleteField("INSQ", next.getInsq()); //$NON-NLS-1$
					xml_data.addCompleteField("MALC", //$NON-NLS-1$ 
							this.fieldLoc.substring(0, slashIndex)); /* ~7A */  
					xml_data.addCompleteField("MILC", //$NON-NLS-1$
							this.fieldLoc.substring(slashIndex + 1, slashIndex + 13).trim()); /* ~7A */
					xml_data.addCompleteField("NMBR", this.fieldHeaderRec.getNmbr()); /* ~7A */ //$NON-NLS-1$
					xml_data.addCompleteField("PRLN", next.getPrln()); /* ~7A */ //$NON-NLS-1$
					xml_data.addCompleteField("USER", MFSConfig.getInstance().getConfigValue("USER")); //$NON-NLS-1$ //$NON-NLS-2$
					xml_data.addCloseTag("DATA"); //$NON-NLS-1$
					xml_data.finalizeXML();

					StringBuffer amsg = new StringBuffer();
					amsg.append("Resetting Interposer Count for "); //$NON-NLS-1$
					amsg.append(next.getInpn());
					amsg.append(" / "); //$NON-NLS-1$
					amsg.append(next.getInsq());
					amsg.append(".  Please Wait..."); //$NON-NLS-1$

					MFSTransaction updt_icnt = new MFSXmlTransaction(xml_data.toString());
					updt_icnt.setActionMessage(amsg.toString());
					MFSComm.execute(updt_icnt);
					rc = updt_icnt.getReturnCode();

					if (rc != 0 && rc != 100)
					{
						IGSMessageBox.showOkMB(getParentFrame(), null, updt_icnt.getErms(), null);
					}//end of bad return from UPDT_ICNT
					else
					{
						next.setInterposerResetFlag(">"); //$NON-NLS-1$
						next.updtDisplayString();
						next.updtIRDisplayString();
						next.updtInstalledParts();

						this.repaint();
					}
				}//end of component should be reset - idsp = 'I' and not
				// already reset check
				index++;
			}//end of while loop

			if (!foundSome && rc == 0)
			{
				String erms = "No Parts to Reset!"; //$NON-NLS-1$
				IGSMessageBox.showOkMB(getParentFrame(), null, erms, null);
			}
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}

		addMyListeners();
		focusPNListOrButton(this.pbResetInterposer);
	}

	//~16A New method
	/** Invokes <code>showPNSNErrorList(fieldPNSNErrorData, fieldPNSNErrorIndex)</code>. */
	public void reshowPNSNErrorList()
	{
		if (this.fieldPNSNErrorData.length() > 0)
		{
			showPNSNErrorList(this.fieldPNSNErrorData, this.fieldPNSNErrorIndex);
		}
	}

	//~16C Rename of fireHandleRestorePrevScreen
	/**
	 * Clears the contents of the panel before calling
	 * {@link MFSFrame#restorePreviousScreen(MFSPanel)}.
	 */
	public void restorePreviousScreen()
	{
		this.pnlRowHolder.removeAll();
		this.fieldLmVector.removeAllElements();

		// Clear references to the MFSPartInstructionJPanels in HideRowVector
		this.fieldHideRowVector.removeAllElements();
		// Clear references to the MFSPartInstructionJPanels in ShowRowVector
		this.fieldShowRowVector.removeAllElements();
		// Clear the MFSPartInstructionJPanels
		this.fieldRowVector.removeAllElements();
		this.fieldInstVector.removeAllElements();

		getParentFrame().restorePreviousScreen(this);
	}

	/** Invokes the RTV_CINC transaction. */
	public void rtv_cinc()
	{
		int rc = 0;

		try
		{
			removeMyListeners();

			StringBuffer data = new StringBuffer(29);
			data.append("RTV_CINC  "); //$NON-NLS-1$
			data.append(getCurrMctl());
			data.append(this.fieldHeaderRec.getMfgn());
			data.append(this.fieldHeaderRec.getIdss());

			MFSTransaction rtv_cinc = new MFSFixedTransaction(data.toString());
			rtv_cinc.setActionMessage("Retrieving List of Containers, Please Wait..."); //$NON-NLS-1$
			MFSComm.getInstance().execute(rtv_cinc, this);
			rc = rtv_cinc.getReturnCode();

			if (rc == 0)
			{
				// ~6 Use new constructor
				MFSCntrInCntrDialog cincJD = new MFSCntrInCntrDialog(getParentFrame(),
						this.fieldHeaderRec, this, this.fieldComponentListModel);
				cincJD.loadTree(rtv_cinc.getOutput());
				cincJD.setLocationRelativeTo(getParentFrame()); //~17A
				cincJD.setVisible(true);
			}
			else
			{
				IGSMessageBox.showOkMB(getParentFrame(), null, rtv_cinc.getErms(), null);
			}
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}

		addMyListeners();
		focusPNListOrButton(this.pbCInC);
	}

	/**
	 * Invokes the RTV_CNTR transaction, displays an <code>MFSCntrDialog</code>,
	 * and handles the user's selection.
	 */
	public void rtv_cntr()
	{
		int rc = 0;

		try
		{
			removeMyListeners();
			String errorString = ""; //$NON-NLS-1$
			final MFSConfig config = MFSConfig.getInstance();

			String data = "RTV_CNTR  " + getCurrMctl() + "L" + //$NON-NLS-1$ //$NON-NLS-2$
					BLANK_CNTR + config.get8CharUser();

			MFSTransaction rtv_cntr = new MFSFixedTransaction(data);
			rtv_cntr.setActionMessage("Retrieving List of Containers, Please Wait..."); //$NON-NLS-1$
			MFSComm.getInstance().execute(rtv_cntr, this);
			rc = rtv_cntr.getReturnCode();
			MFSDetermineLabel label = new MFSDetermineLabel(this.fieldRowVector); /* ~38A */

			if (rc == 0)
			{
				MFSCntrDialog cntrJD = new MFSCntrDialog(getParentFrame());
				cntrJD.loadCntrListModel(rtv_cntr.getOutput());
				cntrJD.setSelectedIndex(0);
				cntrJD.setLocationRelativeTo(getParentFrame()); //~17A
				cntrJD.setVisible(true);
				
				if (cntrJD.getButtonPressed().equals("Select")) //$NON-NLS-1$
				{
					boolean go = true;
					boolean newCntr = false;
					StringBuffer data2 = new StringBuffer();
					data2.append("RTV_CNTR  "); //$NON-NLS-1$
					data2.append(getCurrMctl());

					if (cntrJD.getSelectedCntr().equals("NEW       ")) //$NON-NLS-1$
					{
						Object[] options = {"YES", "NO"}; //$NON-NLS-1$ //$NON-NLS-2$
						int n = JOptionPane.showOptionDialog(getParentFrame(),
								"Do you really want to create a new Container?", //$NON-NLS-1$
								"Create New Container", JOptionPane.YES_NO_OPTION, //$NON-NLS-1$
								JOptionPane.WARNING_MESSAGE, null, options, options[1]);

						if (n == JOptionPane.YES_OPTION)
						{
							newCntr = true;
							data2.append("N"); //$NON-NLS-1$
							data2.append(BLANK_CNTR);  /* ~25A */
							data2.append(config.get8CharUser());  /* ~25A */

						}
						else
						{
							go = false;
						}
					}
					else if (!cntrJD.getSelectedCntr().equals(this.fieldHeaderRec.getCntr()))
					{
						data2.append("A"); //$NON-NLS-1$
						data2.append(cntrJD.getSelectedCntr());
						data2.append(config.get8CharUser());  /* ~25A */
					}
					else
					{
						go = false;
					}

					if (go)
					{
						MFSTransaction rtv_cntr2 = new MFSFixedTransaction(data2.toString());
						rtv_cntr2.setActionMessage("Retrieving Container Info, Please Wait..."); //$NON-NLS-1$
						MFSComm.getInstance().execute(rtv_cntr2, this);
						rc = rtv_cntr2.getReturnCode();

						if (rc == 0)
						{
							updt_cntr(rtv_cntr2.getOutput());
							this.update(getGraphics());

							if (newCntr)
							{
								/* check to see if our country has an override quantity */
								int qty = 1;
								String overrideCfgQty = "CONTAINER," + this.fieldHeaderRec.getCtry().trim();  //$NON-NLS-1$
								String overrideqty = config.getConfigValue(overrideCfgQty);
								if (!overrideqty.equals(MFSConfig.NOT_FOUND))
								{
									if (!overrideqty.equals("")) //$NON-NLS-1$
									{
										qty = Integer.parseInt(overrideqty);
									}
								}

								label.determineContainerLabel(this.fieldHeaderRec,getCurrMctl(), /* ~38A */
										rtv_cntr2.getOutput(),qty,getParentFrame(), false, this);
							}//end newcntr
						}
						else
						{
							errorString = rtv_cntr2.getOutput();
						}
					}
				}
				else if (cntrJD.getButtonPressed().equals("Delete")) //$NON-NLS-1$
				{
					MFSComponentRec cmp;
					int index = 0;
					boolean empty = true;

					if (cntrJD.getSelectedCntr().equals("NEW       ")) //$NON-NLS-1$
					{
						errorString = "Invalid Container to Delete."; //$NON-NLS-1$
						rc = 10;
					}
					else
					{
						while ((index < this.fieldComponentListModel.size()) && empty)
						{
							cmp = this.fieldComponentListModel.getComponentRecAt(index);
							if ((cmp.getIdsp().equals("I") || cmp.getIdsp().equals("R")) //$NON-NLS-1$ //$NON-NLS-2$
									&& (cmp.getCntr().equals(cntrJD.getSelectedCntr())))
							{
								empty = false;
							}
							index++;
						}

						if (empty)
						{
							StringBuffer data3 = new StringBuffer();
							data3.append("RTV_CNTR  "); //$NON-NLS-1$
							data3.append(getCurrMctl());
							data3.append("D"); //$NON-NLS-1$
							data3.append(cntrJD.getSelectedCntr());
							data3.append(config.get8CharUser());  /* ~25A */



							MFSTransaction rtv_cntr3 = new MFSFixedTransaction(data3.toString());
							rtv_cntr3.setActionMessage("Deleting Container, Please Wait..."); //$NON-NLS-1$
							MFSComm.getInstance().execute(rtv_cntr3, this);
							rc = rtv_cntr3.getReturnCode();

							if (rc == 0)
							{
								if (cntrJD.getSelectedCntr().equals(this.fieldHeaderRec.getCntr())
										&& !rtv_cntr3.getOutput().equals("")) //$NON-NLS-1$
								{
									StringBuffer data4 = new StringBuffer();
									data4.append("RTV_CNTR  "); //$NON-NLS-1$
									data4.append(getCurrMctl());
									data4.append("A"); //$NON-NLS-1$
									data4.append(rtv_cntr3.getOutput());
									data4.append(config.get8CharUser());  /* ~25A */

									MFSTransaction rtv_cntr4 = new MFSFixedTransaction(data4.toString());
									rtv_cntr4.setActionMessage("Assigning Container, Please Wait..."); //$NON-NLS-1$
									MFSComm.getInstance().execute(rtv_cntr4, this);
									rc = rtv_cntr4.getReturnCode();

									if (rc == 0)
									{
										updt_cntr(rtv_cntr4.getOutput());
										this.update(getGraphics());
									}
									else
									{
										errorString = rtv_cntr4.getOutput();
									}
								}
								else if (cntrJD.getSelectedCntr().equals(this.fieldHeaderRec.getCntr()))
								{
									updt_cntr("          "); //$NON-NLS-1$
									this.update(getGraphics());
								}
							}
							else
							{
								errorString = rtv_cntr3.getOutput();
							}
						}
						else
						{
							errorString = "You cannot Delete this Container because there are parts installed in it."; //$NON-NLS-1$
							rc = 10;
						}
					}
				}
				else if (cntrJD.getButtonPressed().equals("Reprint")) //$NON-NLS-1$
				{
					if (cntrJD.getSelectedCntr().equals("NEW       ")) //$NON-NLS-1$
					{
						errorString = "Invalid Container to Reprint."; //$NON-NLS-1$
						rc = 10;
					}
					else
					{
						/* check to see if our country has an override quantity */
						int qty = 1;
						String overrideCfgQty = "CONTAINER," + this.fieldHeaderRec.getCtry().trim(); //$NON-NLS-1$
						String overrideqty = config.getConfigValue(overrideCfgQty);
						if (!overrideqty.equals(MFSConfig.NOT_FOUND))
						{
							if (!overrideqty.equals("")) //$NON-NLS-1$
							{
								qty = Integer.parseInt(overrideqty);
							}
						}

						label.determineContainerLabel(this.fieldHeaderRec,getCurrMctl(), /* ~38A */
								cntrJD.getSelectedCntr(),qty,getParentFrame(), true, this);						
					}
				}
			}
			else
			{
				errorString = rtv_cntr.getOutput();
			}

			if (rc != 0)
			{
				IGSMessageBox.showOkMB(getParentFrame(), null, errorString, null);
			}
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}

		addMyListeners();
		focusPNListOrButton(this.pbCntr);
	}

	/** Displays an <code>MFSRemoveMultPartDialog</code> to rework multiple parts. */
	public void rwk_MultParts()
	{
		try
		{
			removeMyListeners();

			if (this.fieldHeaderRec.getWtyp().equals("T")) //$NON-NLS-1$
			{
				MFSRemoveMultPartDialog remMultPn = new MFSRemoveMultPartDialog(
						getParentFrame(), this.fieldHeaderRec);
				remMultPn.loadInstallPNList(this.fieldComponentListModel);
				remMultPn.setLocationRelativeTo(getParentFrame()); //~17A
				remMultPn.setVisible(true);
			}
			else
			{
				String erms = "Can only use this option on T type work unit"; //$NON-NLS-1$
				IGSMessageBox.showOkMB(getParentFrame(), null, erms, null);
			}
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}

		addMyListeners();
	}

	/** Reworks a part. */
	public void rwk_part()
	{
		try
		{
			removeMyListeners();
			boolean okToProceed = false;
					
			if (this.fieldComponentListModel.size() == 0)
			{
				String erms = "No Parts to Rework"; //$NON-NLS-1$
				IGSMessageBox.showOkMB(getParentFrame(), null, erms, null);
			}
			else
			{
				if (this.fieldHeaderRec.getWtyp().equals("T")) //$NON-NLS-1$
				{
					MFSLocatePartDialog myPNLocator = new MFSLocatePartDialog(getParentFrame());
					myPNLocator.setLocationRelativeTo(this.pnlButtons);
					myPNLocator.setVisible(true);

					//user pressedEnter on the button
					if (myPNLocator.getPressedEnter())
					{
						String decPN = myPNLocator.getPNText();
						String decSN = myPNLocator.getSNText();

						MFSComponentListModel tempLm = null;
						MFSComponentRec next;
						boolean found = false;

						/* start at activeRow and work way to the bottom looking for a part */
						int row = this.fieldActiveRow;
						int index = 0;

						//look for the part in the list
						while (row < this.fieldRowVector.size() && !found)
						{
							if (row == this.fieldActiveRow)
							{
								index = getRowVectorElementAt(row).getPNList().getSelectedIndex();
							}
							else
							{
								index = 0;
							}
							tempLm = getRowVectorElementAt(row).getPNListModel();

							while (index < tempLm.size() && !found)
							{
								next = tempLm.getComponentRecAt(index);
								if (next.getInpn().equals(decPN) && next.getInsq().equals(decSN))
								{
									found = true;
								}
								else
								{
									index++;
								}
							}
							if (!found)
							{
								row++;
							}
						}

						/* still now found so start at top and work down to activeRow */
						if (!found) // search entire list
						{
							row = 0;
							while (row <= this.fieldActiveRow && !found)
							{
								tempLm = getRowVectorElementAt(row).getPNListModel();
								index = 0;
								while (index < tempLm.size() && !found)
								{
									next = tempLm.getComponentRecAt(index);
									if (next.getInpn().equals(decPN) && next.getInsq().equals(decSN))
									{
										found = true;
									}
									else
									{
										index++;
									}
								}
								if (!found)
								{
									row++;
								}
							}
						}//end of !found after first looking

						if (found)
						{
							JList pnList = getRowVectorElementAt(row).getPNList();
							pnList.setSelectedIndex(index);
							pnList.ensureIndexIsVisible(index);

							if (!this.fieldCurrentCompRec.getIdsp().equals("I") //$NON-NLS-1$
									|| this.fieldCurrentCompRec.getMlri().equals(" ") //$NON-NLS-1$
									|| this.fieldCurrentCompRec.getMlri().equals("0") //$NON-NLS-1$
									|| this.fieldCurrentCompRec.getCsni().equals("L")) //$NON-NLS-1$ //~33A
							{
								String erms = "Invalid Part to Rework"; //$NON-NLS-1$
								IGSMessageBox.showOkMB(getParentFrame(), null, erms, null);
							}
							else
							{
								okToProceed = true;
							}
						}//end of found condition

						//part was not found, tell the user so
						else
						{
							StringBuffer erms = new StringBuffer();
							erms.append("PN/SN = "); //$NON-NLS-1$
							erms.append(decPN);
							erms.append("/"); //$NON-NLS-1$
							erms.append(decSN);
							erms.append(" not found! "); //$NON-NLS-1$

							IGSMessageBox.showOkMB(getParentFrame(), null, erms.toString(), null);
						}
					}//end of pressedEnter
				}//end of WUTYP == 'T'
				//~49 stop the rework action if the part is BA
				else if(this.fieldCurrentCompRec.getPnri().equals("B"))
				{
				  String erms = "Build Ahead Part Number cannot be removed, already been applied. Contact engineering support for additional details.!";
				  IGSMessageBox.showOkMB(getParentFrame(), null, erms, null);
				}
				
				//not work unit type of 'T', so use old logic here.
				else if (!this.fieldCurrentCompRec.getIdsp().equals("I") //$NON-NLS-1$
						|| this.fieldCurrentCompRec.getMlri().equals(" ") //$NON-NLS-1$
						|| this.fieldCurrentCompRec.getMlri().equals("0") //$NON-NLS-1$
						|| this.fieldCurrentCompRec.getCsni().equals("L")) //$NON-NLS-1$ //~33A					
				{
					String erms = "Invalid Part to Rework"; //$NON-NLS-1$
					IGSMessageBox.showOkMB(getParentFrame(), null, erms, null);
				}
				else
				{
					okToProceed = true;
				}

				if (okToProceed)
				{
					//getting a bit tricky here. set the logpart dialog
					// partlist to the active row part list
					// and the list model to the overall list model (inclusive
					// of all parts) from the DW panel
					JList list = getRowVectorElementAt(this.fieldActiveRow).getPNList();
					MFSRework rwk = new MFSRework(this, list,
							this.fieldComponentListModel, this.fieldHeaderRec,
							this.fieldCurrentCompRec);
					rwk.setPartInstruction(getRowVectorElementAt(this.fieldActiveRow));
					rwk.rework(MFSRwkDialog.LF_FULL);

					if (this.fieldHeaderRec.getWtyp().equals("T") //$NON-NLS-1$
							|| this.fieldHeaderRec.getWtyp().equals("C")) //$NON-NLS-1$
					{
						computeCounter();
					}
				}
			}
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}

		addMyListeners();
		focusPNListOrButton(this.pbRework);
	}

	//~16A New method
	/**
	 * Scrolls the scroll pane used to display the part instructions so that the
	 * specified <code>MFSPartInstructionJPanel</code> is visible.
	 * @param pip the <code>MFSPartInstructionJPanel</code> to make visible
	 */
	public void scrollToPip(MFSPartInstructionJPanel pip)
	{
		this.spPartsInst.requestFocusInWindow();
		if (this.fieldRowVector.contains(pip))
		{
			int y = (int) pip.getBounds().getY();
			this.spPartsInst.getVerticalScrollBar().setValue(y);
		}
	}

	/** 
	 * Sets the value of the blue row.
	 * @param blueRow the new blue row
	 */
	public void setBlueRow(int blueRow)
	{
		this.fieldBlueRow = blueRow;
	}
	
	//~13
	/**
	 * Populates the list of printable down bin'd parts.
	 * @param db_pn The new part number
	 * @param db_sn The new serial number
	 * @see #getDwnBinList
	 */
	public void setDwnBinList(String db_pn, String db_sn)
	{
		this.fieldDownBinList.put(db_pn, db_sn);
	}

	//~16A New method
	/** Called by {@link #assignFocus()} to set the initial focus. */
	public void setInitialFocusFKIT()
	{
		this.spPartsInst.getVerticalScrollBar().setValue(0);
		int index = 0;
		boolean found = false;
		while (index < this.fieldRowVector.size() && !found)
		{
			MFSPartInstructionJPanel pip = getRowVectorElementAt(index);
			if (pip.getIsNonPartInstruction() == false)
			{
				pip.getPNList().setSelectedIndex(0);
				pip.getPNList().requestFocusInWindow();
				found = true;
			}
			index++;
		}

		if (found == false)
		{
			this.pbEnd.requestFocusInWindow();
		}
	}

	//~16A New method
	/** Called by {@link #assignFocus()} to set the initial focus. */
	public void setInitialFocusNonFKIT()
	{

		this.spPartsInst.requestFocusInWindow();
		this.spPartsInst.getVerticalScrollBar().setValue(0);

		//If first row is a part instruction, select and focus the first part.
		//If first row is a nonpart instruction,
		//-set the blue row
		//-set the activeRow to the first part instruction
		//-assign the initial focus to a button
		//If no rows, assign the initial focus to a button
		if (this.fieldRowVector.size() > 0)
		{
			MFSPartInstructionJPanel pip = getRowVectorElementAt(0);
			if (pip.getIsNonPartInstruction() == false)
			{
				pip.getPNList().setSelectedIndex(0);
				pip.getPNList().requestFocusInWindow();
			}
			else
			{
				this.fieldBlueRow = 0;
				pip.changeColor(MFSPartInstructionJPanel.BLUE_ROW_COLOR);
				for (int i = 0; i < this.fieldRowVector.size(); i++)
				{
					MFSPartInstructionJPanel pip2 = getRowVectorElementAt(i);
					if (pip2.getIsNonPartInstruction() == false)
					{
						this.fieldActiveRow = i;
						this.fieldActiveRowIndex = 0;
						break;
					}
				}

				if (this.pbAutoLog.isEnabled())
				{
					this.pbAutoLog.requestFocusInWindow();
				}
				else
				{
					this.pbEnd.requestFocusInWindow();
				}
			}
		}
		else
		{
			if (this.pbAutoLog.isEnabled())
			{
				this.pbAutoLog.requestFocusInWindow();
			}
			else
			{
				this.pbEnd.requestFocusInWindow();
			}
		}
	}

	/*
	 * Dave Fichtinger - 28418DF @1 take out code to add a key listener to the
	 * JList we are adding to the screen. We are inside a method that is called
	 * from DirectWorkPanel.extra_part(), extra_clog_part() or
	 * extra_debugGolden_part()- each method removes listeners and then adds
	 * back in, so we are between removes and adds, so if we add one now, we'll
	 * get a second one added for this JList when addMyListeners gets called at
	 * the end of extra_* method - that is not what we want, 1 too many key
	 * listeners
	 */
	/**
	 * Sets up an extra row.
	 * @param extraComp the <code>MFSComponentRec</code> for the extra row
	 */
	public void setupExtraPartRow(MFSComponentRec extraComp)
	{
		try
		{
			boolean someInstructions = false;
			MFSInstructionRec extraInst = null;
			MFSComponentListModel extraLm = null;

			if (this.fieldInstVector.size() > 0
					|| (this.fieldRowVector.size() > 0 && getRowVectorElementAt(0).getName().equals("RowShort")) //$NON-NLS-1$
					&& !this.fieldHeaderRec.getNmbr().equals("FKIT")) //$NON-NLS-1$
			{
				someInstructions = true;
			}

			extraInst = new MFSInstructionRec();
			extraInst.setSuff("EXTRAPARTS"); //$NON-NLS-1$
			extraInst.setIseq("BTTOM"); //$NON-NLS-1$

			//should prevent updt_instr from thinking it has to update it
			extraInst.setCompletionStatusOriginal("C"); //$NON-NLS-1$
			extraInst.setCompletionStatus("C"); //$NON-NLS-1$

			// remove the row and add it back later
			if (this.fieldRowVector.size() > 0
					&& (getRowVectorElementAt(this.fieldRowVector.size() - 1).getName().equals("RowEXTRAPARTSBTTOM"))) //$NON-NLS-1$
			{
				extraLm = getRowVectorElementAt(this.fieldRowVector.size() - 1).getPNListModel();
				//Add the new component to the end of the list.
				extraLm.add(extraLm.size(), extraComp);
				this.fieldRowVector.remove(this.fieldRowVector.size() - 1);
				this.pnlRowHolder.remove(this.fieldRowVector.size());
			}
			else
			{
				this.pnlRowHolder.invalidate();
				extraLm = new MFSComponentListModel();
				extraLm.setSuff("EXTRAPARTS"); //$NON-NLS-1$
				extraLm.setNmsq("BTTOM"); //$NON-NLS-1$
				extraLm.addElement(extraComp);
			}

			/* Set up some constraints for our new row. */
			GridBagConstraints tmpConstraints = createPipConstraints(true,
					this.fieldRowVector.size());

			/* Create a new Part Instruction Panel. */
			System.out.println("Creating new MFSPartInstructionPanel in setupExtraPartRow()..."); //$NON-NLS-1$

			MFSPartInstructionJPanel pip = new MFSPartInstructionJPanel(this);
			String blvl = this.fieldHeaderRec.getBlvl();
			pip.configure(extraLm, extraInst, someInstructions, blvl);
			pip.setName("RowEXTRAPARTSBTTOM"); //$NON-NLS-1$
			pip.setInstructionRec(extraInst);

			this.fieldRowVector.addElement(pip);

			pip.getPNList().addListSelectionListener(this);
			//pip.getPNList().addKeyListener(this); //@1

			this.pnlRowHolder.add(pip, tmpConstraints);
			this.pnlRowHolder.validate();
			pip.getPNList().setSelectedIndex((pip.getPNListModel()).size() - 1);
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
	}

	/**
	 * Sets up the hide row <code>Vector</code>. Invoked by
	 * {@link #toggleInstructions()} if the hide row <code>Vector</code> is empty.
	 */
	private void setupHideRowVector()
	{
		try
		{
			MFSPartInstructionJPanel pip;
			MFSPartInstructionJPanel newPip;
			boolean someInstructions = false;

			MFSComponentListModel tmpLm;
			MFSInstructionRec tmpInst;

			int rowIndex = 0;
			while (rowIndex < this.fieldRowVector.size())
			{
				pip = getRowVectorElementAt(rowIndex);
				System.out.println("Creating new PIP in setupHideRowVector()..."); //$NON-NLS-1$
				newPip = new MFSPartInstructionJPanel(this);
				newPip.setName(pip.getName());

				final String instrClass = pip.getInstructionRec().getInstrClass();
				if (instrClass.equals("M") || instrClass.equals("A")) //$NON-NLS-1$ //$NON-NLS-2$
				{
					someInstructions = true;
					tmpInst = pip.getInstructionRec();
				}
				else
				{
					someInstructions = false;
					tmpInst = pip.getInstructionRec();
				}

				if (pip.getIsNonPartInstruction())
				{
					tmpLm = new MFSComponentListModel();
					newPip.setIsNonPartInstruction(true);
				}
				else
				{
					tmpLm = pip.getPNListModel();
					newPip.setIsNonPartInstruction(false);
				}

				String blvl = this.fieldHeaderRec.getBlvl();
				newPip.configure(tmpLm, tmpInst, someInstructions, blvl);

				if (!newPip.getIsNonPartInstruction())
				{
					newPip.getPNList().addListSelectionListener(this);
				}

				if (!someInstructions)
				{
					newPip.setInstructionRec(pip.getInstructionRec());
				}

				this.fieldHideRowVector.addElement(newPip);
				rowIndex++;
			}
		}//end of try block
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
	}

	/** {@inheritDoc} */
	public void setupPartInstPanel()
	{
		try
		{
			boolean someInstructions = false;
			MFSInstructionRec shortInst = null;
			MFSInstructionRec tempInst = new MFSInstructionRec();
			Vector<MFSPartInstructionJPanel> rowVector = new Vector<MFSPartInstructionJPanel>();
			String curListSuff = ""; //$NON-NLS-1$
			String curListSeq = ""; //$NON-NLS-1$
			int instIndex = 0;
			boolean found = false;
			boolean nonPartInstFound = false;
			int yPos = 0;
			final MFSCP500Comparator comparator = new MFSCP500Comparator(); //~16A
			final String builderLevel = this.fieldHeaderRec.getBlvl(); //~16A

			getSource().startAction(this.SETUP_PART_INST_MSG); //~18C

			// make sure row panel has nuttin in it
			this.pnlRowHolder.removeAll();
			// make sure row vector has nuttin in it
			this.fieldRowVector.removeAllElements();

			//remove all traces of hide and show elements
			this.fieldHideRowVector.removeAllElements();
			this.fieldShowRowVector.removeAllElements();

			// if no instructions, use single line MFSComponentCellRenderer
			// otherwise use a multiLine MFSComponentCellRenderer
			if (this.fieldInstVector.size() != 0)
			{
				someInstructions = true;
			}

			//loop thru the vector of List Models and match up with the instructions in the instruction vector
			//2 indexs: listIndex and instIndex - we match up by suffix and sequence values
			int listIndex = 0;
			while (listIndex < this.fieldLmVector.size())
			{
				MFSComponentListModel listModel = getLmVectorElementAt(listIndex);

				nonPartInstFound = false;
				curListSuff = listModel.getSuff();
				curListSeq = listModel.getNmsq();

				//if short need to use multiLine renderer - see if we can find a matching instruction
				//in the instruction vector - if we don't find an instruction, we'll set up a dummy one with
				//blank suffix and sequence values.
				if (listModel.getIsShort() && !this.fieldHeaderRec.getNmbr().equals("FKIT")) //$NON-NLS-1$
				{
					someInstructions = true;
					found = false;
					for (int j = instIndex; j < this.fieldInstVector.size() && !found; j++)
					{
						MFSInstructionRec instRec = getInstVectorElementAt(j);
						if (instRec.getSuff().equals(curListSuff)
								&& instRec.getIseq().equals(curListSeq))
						{
							shortInst = instRec;
							instIndex = j + 1;
							found = true;
						}
					}
					if (!found)
					{
						shortInst = new MFSInstructionRec();
						shortInst.setSuff("          "); //$NON-NLS-1$
						shortInst.setIseq("     "); //$NON-NLS-1$
					}
				}

				if (curListSuff.equals("          ") && curListSeq.equals("     ")) //$NON-NLS-1$ //$NON-NLS-2$
				{
					if (someInstructions)
					{
						tempInst = new MFSInstructionRec();
						tempInst.setSuff("          "); //$NON-NLS-1$
						tempInst.setIseq("     "); //$NON-NLS-1$
					}
				}

				//create oursleves a new MFSPartInstructionJPanel
				//System.out.println("Creating new PartInstructionJPanel in setupPartInstPanel()...");
				MFSPartInstructionJPanel pip = new MFSPartInstructionJPanel(this);

				if ((!curListSuff.equals("          ") || !curListSeq.equals("     ")) //$NON-NLS-1$ //$NON-NLS-2$
						&& !getLmVectorElementAt(listIndex).getIsShort())
				{
					// We now have a suffix/sequence for the listModel. We will
					// first check to see if there are any non Part Instructions
					// that need to be displayed first, so we compare our list
					// Suffix/Seq and look thru instruction vector for one less
					// than that one
					nonPartInstFound = false;
					found = false;

					for (int i = instIndex; i < this.fieldInstVector.size() && !nonPartInstFound; i++)
					{
						//~16C Use MFSCP500Comparator to perform comparison
						MFSInstructionRec iRecI = getInstVectorElementAt(i);
						if (comparator.compare(iRecI, curListSeq + curListSuff) < 0)
						{
							//make sure this suffix/sequence isn't already
							// listed in the short parts section
							boolean alreadyListed = false;
							for (int q = 0; q < this.fieldLmVector.size() && !alreadyListed; q++)
							{
								MFSComponentListModel compLMQ = getLmVectorElementAt(q);
								String tmpSuff = compLMQ.getSuff();
								String tmpSeq = compLMQ.getNmsq();

								if (tmpSuff.equals(iRecI.getSuff())
										&& tmpSeq.equals(iRecI.getIseq())
										&& compLMQ.getIsShort())
								{
									alreadyListed = true;
								}
							}

							if (!alreadyListed)
							{
								instIndex = i + 1;
								nonPartInstFound = true;
								tempInst = iRecI;
							}
						}
					}

					//we didn't find a non part instruction so we'll now look
					//for a match on suffix sequence from the instruction vector
					if (!nonPartInstFound)
					{
						for (int j = instIndex; j < this.fieldInstVector.size() && !found; j++)
						{
							MFSInstructionRec iRecJ = getInstVectorElementAt(j);
							if (iRecJ.getSuff().equals(curListSuff)
									&& iRecJ.getIseq().equals(curListSeq))
							{
								tempInst = iRecJ;
								instIndex = j + 1;
								found = true;
							}
						}
						if (!found)
						{
							tempInst = new MFSInstructionRec();
							tempInst.setSuff(curListSuff);
							tempInst.setIseq(curListSeq);
						}
					}
				}//only do the nonPart check if we are not blank suffix and seq

				boolean last = false;
				if (listIndex == this.fieldLmVector.size() - 1
						&& instIndex == this.fieldInstVector.size())
				{
					last = true;
				}
				GridBagConstraints tmpConstraints = createPipConstraints(last, yPos++);

				if (getLmVectorElementAt(listIndex).getIsShort())
				{
					pip.configure(getLmVectorElementAt(listIndex), shortInst,
							someInstructions, builderLevel);
					pip.setName("Row" + curListSuff + curListSeq + "Short"); //$NON-NLS-1$ //$NON-NLS-2$
					listIndex++;
				}
				else
				{
					if (nonPartInstFound)
					{
						MFSComponentListModel emptyLm = new MFSComponentListModel();
						pip.setIsNonPartInstruction(true);
						pip.configure(emptyLm, tempInst, someInstructions, builderLevel);

						MFSInstructionRec iRecName = getInstVectorElementAt(instIndex - 1);
						pip.setName("Row" + iRecName.getSuff() + iRecName.getIseq()); //$NON-NLS-1$
					}
					else
					{
						pip.configure(getLmVectorElementAt(listIndex), tempInst,
								someInstructions, builderLevel);
						pip.setName("Row" + curListSuff + curListSeq); //$NON-NLS-1$
						listIndex++;
					}
				}
				//add this row to our RowVector
				rowVector.addElement(pip);

				//add listeners to the list
				pip.getPNList().addListSelectionListener(this);
				pip.getPNList().addKeyListener(this);
				pip.getPNList().addMouseListener(this);

				this.pnlRowHolder.add(pip, tmpConstraints);
			}/* end of while loop */

			// clean up any other instructions from the instrVector
			// there may be more instructions that have not been inserted into the display panel
			for (int i = instIndex; i < this.fieldInstVector.size(); i++)
			{
				tempInst = getInstVectorElementAt(i);
				MFSPartInstructionJPanel pip = new MFSPartInstructionJPanel(this);
				MFSComponentListModel emptyLm = new MFSComponentListModel();

				pip.setIsNonPartInstruction(true);
				pip.configure(emptyLm, tempInst, someInstructions, builderLevel);

				pip.setName("Row" + tempInst.getSuff() + tempInst.getIseq()); //$NON-NLS-1$

				boolean last = false;
				if (i == this.fieldInstVector.size() - 1)
				{
					last = true;
				}

				GridBagConstraints tmpConstraints = createPipConstraints(last, yPos++);
				rowVector.addElement(pip);
				this.pnlRowHolder.add(pip, tmpConstraints);
			}

			this.fieldRowVector = rowVector;
			this.fieldShowRowVector = rowVector;
		}
		catch (Exception e)
		{
			this.setCursor(Cursor.getDefaultCursor());
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
		finally
		{
			getSource().stopAction(); //~18C
		}
	}

	/** Invoked when {@link #pbShort} is selected to short a part. */
	public void short_button()
	{
		try
		{
			removeMyListeners();

			if (this.fieldComponentListModel.size() == 0)
			{
				String erms = "No Parts to Short"; //$NON-NLS-1$
				IGSMessageBox.showOkMB(getParentFrame(), null, erms, null);
			}
			else
			{
				MFSPartInstructionJPanel pip = getRowVectorElementAt(this.fieldActiveRow);
				JList tmpList = pip.getPNList();
				MFSComponentListModel tmpLm = pip.getPNListModel();
				MFSComponentRec cmp = tmpLm.getComponentRecAt(tmpList.getSelectedIndex());

				if (!cmp.getIdsp().equals("A") || cmp.getQnty().equals("00000")) //$NON-NLS-1$  //$NON-NLS-2$
				{
					String erms = "Invalid part to short."; //$NON-NLS-1$
					IGSMessageBox.showOkMB(getParentFrame(), null, erms, null);
				}
				else
				{
					short_cr(cmp, tmpList.getSelectedIndex());
					redoLayout(this.fieldActiveRow);
				}
			}
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}

		addMyListeners();
		focusPNListOrButton(this.pbShort);
	}

	/* Dave Fichtinger - if typz == 0, we want to set it to a 'D' - 31201MZ @1 */
	/**
	 * Shorts an <code>MFSComponentRec</code>.
	 * @param cmp the <code>MFSComponentRec</code>
	 * @param index the list index of the <code>MFSComponentRec</code>
	 */
	public void short_cr(MFSComponentRec cmp, int index)
	{
		try
		{
			MFSComponentListModel tmpLm = getRowVectorElementAt(this.fieldActiveRow).getPNListModel();

			if (!cmp.getFqty().equals("00000")) //$NON-NLS-1$
			{
				MFSComponentRec newCr = split_cr(cmp);

				if (newCr != null)
				{
					if (this.fieldHeaderRec.getTypz().equals("O")) //$NON-NLS-1$
					{
						newCr.setIdsp("D"); //$NON-NLS-1$
						newCr.setShtp(" "); //$NON-NLS-1$
					}
					else
					{
						newCr.setShtp("1"); //$NON-NLS-1$
					}
					newCr.updtIRDisplayString();
					newCr.updtDisplayString();
					tmpLm.add(index + 1, newCr);
				}
			}
			else
			{
				if (this.fieldHeaderRec.getTypz().equals("O")) //$NON-NLS-1$
				{
					cmp.setIdsp("D"); //$NON-NLS-1$
				}
				else
				{
					cmp.setShtp("1"); //$NON-NLS-1$
				}
			}
			cmp.setRec_changed(true);
			cmp.updtIRDisplayString();
			cmp.updtDisplayString();
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
	}

	/**
	 * Shows an <code>MFSLogPartDialog</code>.
	 * @param logType the log type for the <code>MFSLogPartDialog</code>
	 */
	public void showLogPart(String logType)
	{

		if (!(this.fieldCurrentCompRec.getIdsp().equals("X") //$NON-NLS-1$ 
				|| this.fieldCurrentCompRec.getIdsp().equals("A"))) //$NON-NLS-1$
		{
			String erms = "Invalid Part to Add"; //$NON-NLS-1$
			IGSMessageBox.showOkMB(getParentFrame(), null, erms, null);
		}
		else if (!this.fieldCurrentCompRec.getCsni().equals(" ") //$NON-NLS-1$
				&& !this.fieldCurrentCompRec.getCsni().equals("0") //$NON-NLS-1$
				&& !this.fieldCurrentCompRec.getQnty().equals("00000") //$NON-NLS-1$
				&& !this.fieldCurrentCompRec.getQnty().equals("00001")) //$NON-NLS-1$
		{
			String erms = "Cannot Collect S/N on Part with Qty > 1."; //$NON-NLS-1$
			IGSMessageBox.showOkMB(getParentFrame(), null, erms, null);
		}
		else
		{
			if (this.fieldCurrentCompRec.getMlri().equals(" ") //$NON-NLS-1$
					|| this.fieldCurrentCompRec.getMlri().equals("0")) //$NON-NLS-1$
			{
				this.fieldCurrentCompRec.setMlri("1"); //$NON-NLS-1$
			}

			//set the logpart dialog partlist to the active row part list
			// and the list model to the overall list model (inclusive of all
			// parts) from the DW panel
			JList list = getRowVectorElementAt(this.fieldActiveRow).getPNList();
			MFSLogPartDialog myLogPartD = new MFSLogPartDialog(this, logType,
					this.fieldHeaderRec, list, this.fieldComponentListModel); 

			// give the log Part dialog the rowVector. Should be able to get at
			// everything it needs from the rowVector (part lists and instructions)
			myLogPartD.setRowInfo(this.fieldRowVector, this.fieldActiveRow);
			myLogPartD.setLocationRelativeTo(this.pnlButtons);
			myLogPartD.initDisplay();
			myLogPartD.setVisible(true);
		}

		if (this.fieldHeaderRec.getWtyp().equals("T") //$NON-NLS-1$
				|| this.fieldHeaderRec.getWtyp().equals("C")) //$NON-NLS-1$
		{
			computeCounter();
		}
	}

	/**
	 * Shows an <code>MFSErrorMsgListDialog</code> and displays the part
	 * detail screen if the user selects the part detail button.
	 * @param listData the data for the list
	 * @param index the list index to select
	 */
	public void showPNSNErrorList(String listData, int index)
	{
		this.fieldPNSNErrorData = listData;

		MFSErrorMsgListDialog msgBoxLrg = new MFSErrorMsgListDialog(getParentFrame());
		//~16C Switched order of loadList and setListSelectedIndex
		msgBoxLrg.loadList(listData);
		msgBoxLrg.setListSelectedIndex(index);
		msgBoxLrg.setLocationRelativeTo(this);
		msgBoxLrg.setVisible(true);

		if (msgBoxLrg.getPressedPartDetail())
		{
			this.fieldPNSNErrorIndex = msgBoxLrg.getSelectedIndex();

			String pn = msgBoxLrg.getSelectedPNSN().substring(0, 12);
			String sn = msgBoxLrg.getSelectedPNSN().substring(14, 26);

			//~16C Create a new MFSPartFunctionDisplayPanel and call partDetail
			MFSPartFunctionDisplayPanel partFunc = 
				new MFSPartFunctionDisplayPanel(getParentFrame(), this, pn, sn);
			this.fieldChildPanel = partFunc;
			partFunc.partDetail(false, true);
		}
	}

	/** Invoked when {@link #pbSkipPart} is selected to skip a part. */
	public void skip_part()
	{
		try
		{
			removeMyListeners();

			if (this.fieldComponentListModel.size() == 0)
			{
				String erms = "No Parts to Skip"; //$NON-NLS-1$
				IGSMessageBox.showOkMB(getParentFrame(), null, erms, null);
			}
			else
			{
				if (this.fieldCurrentCompRec.getIdsp().equals("S")) //$NON-NLS-1$
				{
					this.fieldCurrentCompRec.setIdsp("I"); //$NON-NLS-1$
					this.fieldCurrentCompRec.setRec_changed(true);
					this.fieldCurrentCompRec.updtDisplayString();
					this.fieldCurrentCompRec.updtIRDisplayString();
					this.repaint();
				}
				else if (this.fieldCurrentCompRec.getIdsp().equals("I")) //$NON-NLS-1$
				{
					this.fieldCurrentCompRec.setIdsp("S"); //$NON-NLS-1$
					this.fieldCurrentCompRec.setRec_changed(true);
					this.fieldCurrentCompRec.updtDisplayString();
					this.fieldCurrentCompRec.updtIRDisplayString();
					this.repaint();
				}
				else
				{
					String erms = "Error:  Cannot skip a part that is not installed."; //$NON-NLS-1$
					IGSMessageBox.showOkMB(getParentFrame(), null, erms, null);
				}
			}//end of parts in our list
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}

		addMyListeners();
		focusPNListOrButton(this.pbSkipPart);
	}


	/** Invoked when {@link #pbSplit} is selected to split a part. */
	public void split_button()
	{
		try
		{
			removeMyListeners();

			if (this.fieldComponentListModel.size() == 0)
			{
				String erms = "No Parts to Split"; //$NON-NLS-1$
				IGSMessageBox.showOkMB(getParentFrame(), null, erms, null);
			}
			else
			{
				MFSPartInstructionJPanel pip = getRowVectorElementAt(this.fieldActiveRow);
				JList tmpList = pip.getPNList();
				MFSComponentListModel tmpLm = pip.getPNListModel();
				MFSComponentRec cmp = tmpLm.getComponentRecAt(tmpList.getSelectedIndex());

				if (cmp.getFqty().equals("00000") || cmp.getQnty().equals(cmp.getFqty()) //$NON-NLS-1$
						|| !(cmp.getIdsp().equals("A") || cmp.getIdsp().equals("X"))) //$NON-NLS-1$ //$NON-NLS-2$
				{
					String erms = "Invalid part to split."; //$NON-NLS-1$
					IGSMessageBox.showOkMB(getParentFrame(), null, erms, null);
				}
				else
				{
					MFSComponentRec newCr = split_cr(cmp);

					if (newCr != null)
					{
						newCr.updtIRDisplayString();
						newCr.updtDisplayString();
						tmpLm.add(tmpList.getSelectedIndex() + 1, newCr);
						this.redoLayout(this.fieldActiveRow);
					}
				}
			}
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}

		addMyListeners();
		focusPNListOrButton(this.pbSplit);
	}

	/**
	 * Calls the SPLIT_CR transaction to split an <code>MFSComponentRec</code>.
	 * @param cmp the <code>MFSComponentRec</code>
	 * @return the new <code>MFSComponentRec</code>
	 */
	public MFSComponentRec split_cr(MFSComponentRec cmp)
	{
		try
		{
			int rc = 0;

			StringBuffer data = new StringBuffer();
			data.append("SPLIT_CR  "); //$NON-NLS-1$
			data.append(cmp.getMctl());
			data.append(cmp.getCrct());
			data.append(cmp.getFqty());

			MFSTransaction split_cr = new MFSFixedTransaction(data.toString());
			split_cr.setActionMessage("Splitting Component Record, Please Wait..."); //$NON-NLS-1$
			MFSComm.getInstance().execute(split_cr, this);
			rc = split_cr.getReturnCode();

			if (rc == 0)
			{
				System.out.println("Creating new Component Record in split_cr()..."); //$NON-NLS-1$
				MFSComponentRec newCr = new MFSComponentRec(cmp);

				cmp.setRec_changed(true);
				cmp.setQnty(cmp.getFqty());
				cmp.setIdsp("I"); //$NON-NLS-1$
				cmp.setShtp(" "); //$NON-NLS-1$
				cmp.updtDisplayString();
				cmp.updtIRDisplayString();

				newCr.setRec_changed(true);
				newCr.setCrct(split_cr.getOutput().substring(0, 4));
				newCr.setQnty(split_cr.getOutput().substring(4));
				newCr.setFqty("00000"); //$NON-NLS-1$
				newCr.setInpn("            "); //$NON-NLS-1$
				newCr.setCntr("          "); //$NON-NLS-1$
				newCr.setCooc("  "); //$NON-NLS-1$
				newCr.setIdsp("A"); //$NON-NLS-1$
				newCr.updtDisplayString();
				newCr.updtIRDisplayString();
				newCr.updtInstalledParts();
				this.fieldComponentListModel.add(0, newCr);
				return newCr;
			}
			
			IGSMessageBox.showOkMB(getParentFrame(), null, split_cr.getErms(), null);
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
		return null;
	}

	/**
	 * Suspends a work unit.
	 * @param ecod the end code. Should be 'S' for suspend
	 * @param oemo the operation ending mode
	 */
	public void suspend(String ecod, String oemo)
	{
		final MFSConfig config = MFSConfig.getInstance();  //~41a
		boolean stillVisible = true; //~24A
		String suspendCode="";	//~41a //$NON-NLS-1$
		boolean continueSuspend = false; //~41a
		int rc = 0;
		try
		{
			removeMyListeners();
			if (checkForPending())
			{
				rc = verifyPendingParts();
			}
			if (rc == 0)
			{
				if (!config.containsConfigEntry("EFFICIENCYON")) //~41a //$NON-NLS-1$
				{
					DefaultListModel suspendCodeListModel = new DefaultListModel();
					rc = MFSSuspendCodeDialog.loadSuspendCodes(suspendCodeListModel, this,
							getParentFrame());

					if (rc == 0)
					{
						MFSSuspendCodeDialog suspendCodeJD = new MFSSuspendCodeDialog(
								getParentFrame(), suspendCodeListModel);
						suspendCodeJD.setLocationRelativeTo(getParentFrame()); //~17A
						suspendCodeJD.setVisible(true);

						if (suspendCodeJD.getProceedSelected())
						{
							suspendCode = suspendCodeJD.getSelectedListOption().substring(0, 2);
							continueSuspend = true; //~41a
						}
					}
				}
				else
				{
					suspendCode = "  "; //$NON-NLS-1$
					continueSuspend = true; //~41a
					}

				if (continueSuspend) //~41a
				{
					//~24A Call updateWeightDim
					rc = updateWeightDim("S"); //$NON-NLS-1$
					if (rc == 0)
					{
						rc = locIt("S"); //$NON-NLS-1$
						if (rc == 0)
						{
							rc = updt_crwc();
							if (rc == 0)
							{
								rc = updt_instr("S"); //$NON-NLS-1$
								if (rc == 0)
								{
									rc = end_wu(ecod, suspendCode, oemo);
									if (rc == 30 || rc == 31)
									{
										this.fieldEndCode = 0;
										this.setCurrentCompRec(null);
										this.fieldInstVector.removeAllElements();
										this.fieldComponentListModel.removeAllElements();
										restorePreviousScreen();
										stillVisible = false; //~24A
									}
								}
							}
						}
					}
				} //end if continueSuspend
			}
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}

		//~24C Call focusPNListOrButton only if still visible
		if (stillVisible)
		{
			focusPNListOrButton(this.pbSuspend);
		}
		addMyListeners();
	}

	/**
	 * Suspends a work unit.
	 * @param ecod the end code ('F'ail or 'S'uspend)
	 * @param oemo the operation ending mode
	 */
	public void suspendNoQC(String ecod, String oemo)
	{
		try
		{
			int rc = 0;
			removeMyListeners();
			getParentFrame().setCursor(Cursor.getDefaultCursor());

			rc = updt_crwc();
			if (rc == 0)
			{
				rc = updt_instr("S"); //$NON-NLS-1$
				if (rc == 0)
				{
					rc = end_wu(ecod, "ZZ", oemo); //$NON-NLS-1$
					if (rc == 30 || rc == 31)
					{
						this.fieldEndCode = 0;
						this.setCurrentCompRec(null);
						this.fieldInstVector.removeAllElements();
						this.fieldComponentListModel.removeAllElements();
						restorePreviousScreen();
					}
					else
					{
						focusPNListOrButton(this.pbSuspend);
					}
				}
				else
				{
					focusPNListOrButton(this.pbSuspend);
				}
			}
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}

		addMyListeners();
	}

	/**
	 * Prints the spd11stext label if it is configured.
	 * @param headRec the <code>MFSHeaderRec</code> for the work unit
	 */
	public void swSPD11STextLabel(MFSHeaderRec headRec)
	{
		final MFSConfig config = MFSConfig.getInstance();
		String cnfgData1 = "SPD11STEXT," + headRec.getNmbr() + "," //$NON-NLS-1$ //$NON-NLS-2$
				+ headRec.getPrln().trim();
		String value = config.getConfigValue(cnfgData1);
		if (value.equals(MFSConfig.NOT_FOUND))
		{
			String cnfgData2 = "SPD11STEXT," + headRec.getNmbr() + ",*ALL"; //$NON-NLS-1$ //$NON-NLS-2$
			value = config.getConfigValue(cnfgData2);
		}
		if (!value.equals(MFSConfig.NOT_FOUND))
		{
			int qty = 1;
			if (!value.equals("")) //$NON-NLS-1$
			{
				qty = Integer.parseInt(value);
			}

			for (int i = 0; i < qty; i++)
			{
				MFSPrintingMethods.spd11stext(headRec.getMctl(), 1, getParentFrame());
			}
		}
	}

	//~35A New method
	/** Calls all the trx needed when the MCTL is a TCOD **/
	@SuppressWarnings({ "rawtypes" })
	public String tcodTrxLogic(MFSComponentRec compRec)
	{
		int rc = 0;
		String errorString = EMPTY_STRING;
		
		StringBuffer data = new StringBuffer(54);
		data.append("RTV_VPDLOG"); //$NON-NLS-1$
		data.append(compRec.getInpn());
		data.append(compRec.getInsq());
		data.append(compRec.getCwun());
		data.append(compRec.getTcodPart());

		MFSTransaction rtv_vpdlog = new MFSFixedTransaction(data.toString());
		rtv_vpdlog.setActionMessage("Retrieving VPD Info, Please Wait..."); //$NON-NLS-1$
		MFSComm.getInstance().execute(rtv_vpdlog, this);
		rc = rtv_vpdlog.getReturnCode();

		if (rc == 666)
		{
			rc = 0;
		}
			
		if (rc == 0) /* CALL TO RTV_VPDLOG WAS SUCCESSFULL */
		{
			String ccin = rtv_vpdlog.getOutput().substring(0, 4);
			String ccsn = rtv_vpdlog.getOutput().substring(4, 14);
			String ccid = rtv_vpdlog.getOutput().substring(14, 30);

			String TcodOdriUnparsed = compRec.getTcodOdri();
			String TcodOdriParsed = EMPTY_STRING;
			String TcodAcqyUnparsed = compRec.getTcodAcqy();
			String TcodAcqyParsed = EMPTY_STRING;
			String TcodBrlnUnparsed = compRec.getTcodBrln(); /* ~2 */
			String TcodBrlnParsed = EMPTY_STRING; /* ~2 */

			/* we put this in a loop as there might be multiple activations per part */
			while (TcodOdriUnparsed.trim().length() > 0 && rc == 0)
			{
				TcodOdriParsed = TcodOdriUnparsed.substring(0, TcodOdriUnparsed.indexOf(','));
				TcodOdriUnparsed = TcodOdriUnparsed.substring(TcodOdriUnparsed.indexOf(',') + 1);
				TcodAcqyParsed = TcodAcqyUnparsed.substring(1, TcodAcqyUnparsed.indexOf(','));
				TcodAcqyUnparsed = TcodAcqyUnparsed.substring(TcodAcqyUnparsed.indexOf(',') + 1);
				TcodBrlnParsed = TcodBrlnUnparsed.substring(0, TcodBrlnUnparsed.indexOf(',')); /* ~2 */
				TcodBrlnUnparsed = TcodBrlnUnparsed.substring(TcodBrlnUnparsed.indexOf(',') + 1); /* ~2 */

				String sequence = "0000"; /* ~2, ~14 *///$NON-NLS-1$
				if (TcodBrlnParsed.trim().equals("iSeries")) /* ~2 *///$NON-NLS-1$
				{
					sequence = "0020"; /* ~2 *///$NON-NLS-1$
				}
				else if (TcodBrlnParsed.trim().equals("pSeries")) /* ~2 *///$NON-NLS-1$
				{
					sequence = "0001"; /* ~2 *///$NON-NLS-1$
				}

				StringBuffer data2 = new StringBuffer(62);
				data2.append("RTV_CUODAC"); //$NON-NLS-1$
				data2.append(compRec.getMctl());
				data2.append(ccin);
				data2.append(ccsn);
				data2.append(ccid);
				data2.append(TcodOdriParsed);
				data2.append(TcodAcqyParsed);
				data2.append(sequence); /* ~2 */
				data2.append("XX"); //$NON-NLS-1$

				MFSTransaction rtv_cuodac = new MFSFixedTransaction(data2.toString());
				rtv_cuodac.setActionMessage("Retrieving Activation Info, Please Wait..."); //$NON-NLS-1$
				MFSComm.getInstance().execute(rtv_cuodac, this);
				rc = rtv_cuodac.getReturnCode();
					
				if (rc != 0)
				{
					errorString = rtv_cuodac.getOutput();
				}
			}

			if (rc == 0) /* CALL TO RTV_CUODAC WAS SUCCESSFULL */
			{
				StringBuffer data3 = new StringBuffer(21);
				data3.append("RTV_CUODKY"); //$NON-NLS-1$
				data3.append(this.fieldHeaderRec.getMfgn());
				data3.append(this.fieldHeaderRec.getIdss());

				MFSTransaction rtv_cuodky = new MFSFixedTransaction(data3.toString());
				rtv_cuodky.setActionMessage("Retrieving VPD Key Info, Please Wait..."); //$NON-NLS-1$
				MFSComm.getInstance().execute(rtv_cuodky, this);
				rc = rtv_cuodky.getReturnCode();

				if (rc != 0)
				{
					errorString = rtv_cuodky.getOutput();
				}
				else
				{
					//~18C Use parseRTV_CUODKY to parse output
					Vector myRows = MFSTransactionUtils.parseRTV_CUODKY(rtv_cuodky);

					for (int index = 0; index < myRows.size(); index++)
					{
						Hashtable rowData = (Hashtable) myRows.elementAt(index);

						final String code = (String) rowData.get("CODE"); //$NON-NLS-1$
						if (code.substring(0, 1).equals("*") //$NON-NLS-1$
								|| code.equals("                                  ")) //$NON-NLS-1$
						{
							//Ignore asterisk activation codes.
							//Ignore blank activation codes.
						}
						else
						{
							/* Activation code is populated so print sheet */
							int qty = 1;
							MFSPrintingMethods.cuodkey(this.fieldHeaderRec.getMfgn(),
									this.fieldHeaderRec.getIdss(), 
									((String) rowData.get("SSN")), //$NON-NLS-1$ 
									((String) rowData.get("CCIN")), //$NON-NLS-1$
									((String) rowData.get("CCSN")), //$NON-NLS-1$ 
									((String) rowData.get("CCID")), //$NON-NLS-1$ 
									((String) rowData.get("ACQY")), //$NON-NLS-1$ 
									((String) rowData.get("TACT")), //$NON-NLS-1$
									((String) rowData.get("CODE")), //$NON-NLS-1$ 
									((String) rowData.get("ORNO")), //$NON-NLS-1$ 
									((String) rowData.get("PART")), //$NON-NLS-1$ 
									((String) rowData.get("ODRI")), //$NON-NLS-1$
									((String) rowData.get("ODTN")), //$NON-NLS-1$ 
									((String) rowData.get("BRLN")), //$NON-NLS-1$ 
									qty, getParentFrame());
						}
					} /* end for loop */
				} /* end-if rc=0 from rtv_cuodky call */
			}
		} /* end if rtv_vpdlog == 0 */
		else
		{
			errorString = rtv_vpdlog.getOutput();
		}
		
		return errorString;
	}
	
	
	/** Toggles the display of the instructions. */
	public void toggleInstructions()
	{
		try
		{
			removeMyListeners();

			//only the 'M'andatory instructions will remain on the screen.
			//Set someInstructions boolean accordingly so the configure method
			//will remove the instructions from the screen
			int yPos = 0;
			MFSPartInstructionJPanel pip;

			this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			getActionIndicator().startAction("Toggling Instructions Now, Please Wait..."); //$NON-NLS-1$
			this.update(getGraphics());

			if (this.fieldBlueRow != -1)
			{
				getRowVectorElementAt(this.fieldBlueRow).unDoChangeColor();
				this.fieldBlueRow = -1;
			}

			// make sure row panel has nothing in it
			this.pnlRowHolder.removeAll();

			//If hide button says show, then panel is in hide mode.
			//fieldShowRowVector should already have the right stuff in it.
			if (this.pbHide.getText().substring(0, 4).equals("Show")) //$NON-NLS-1$
			{
				for (yPos = 0; yPos < this.fieldShowRowVector.size(); yPos++)
				{
					pip = this.fieldShowRowVector.elementAt(yPos);

					boolean last = false;
					if (yPos == this.fieldShowRowVector.size() - 1)
					{
						last = true;
					}

					GridBagConstraints tmpConstraints = createPipConstraints(last, yPos);
					pip.ensureCompletionStatus();
					this.pnlRowHolder.add(pip, tmpConstraints);
				}

				this.fieldRowVector = this.fieldShowRowVector;
				this.pbHide.setText("Hide Ins"); //$NON-NLS-1$
				this.pbHide.setToolTipText("Hide Instructions"); //$NON-NLS-1$
			}
			else
			{
				//check for a size, if its 0, we'll assume its the first time
				// thru, so we have to setup fieldHideRowVector
				if (this.fieldHideRowVector.size() == 0)
				{
					setupHideRowVector();
				}

				int index = 0;
				yPos = 0;
				while (index < this.fieldHideRowVector.size())
				{
					pip = this.fieldHideRowVector.elementAt(index);

					if (!pip.getIsNonPartInstruction()
							|| pip.getInstructionRec().getInstrClass().equals("M") //$NON-NLS-1$
							|| pip.getInstructionRec().getInstrClass().equals("A")) //$NON-NLS-1$
					{

						boolean last = false;
						if (index == this.fieldHideRowVector.size() - 1)
						{
							last = true;
						}

						GridBagConstraints tmpConstraints = createPipConstraints(last, yPos++);
						this.pnlRowHolder.add(pip, tmpConstraints);
					}
					index++;
				}
				//set current row vector to the showHideVector
				this.fieldRowVector = this.fieldHideRowVector;
				this.pbHide.setText("Show Ins"); //$NON-NLS-1$
				this.pbHide.setToolTipText("Show Instructions"); //$NON-NLS-1$
			}

			//select top part in first row
			int index = 0;
			boolean stop = false;
			boolean topRowSet = false;
			JList tmpList;
			MFSComponentRec next;
			if (this.fieldRowVector.size() > 0)
			{
				while (index < this.fieldRowVector.size() && !stop)
				{
					pip = getRowVectorElementAt(index);
					if (!pip.getIsNonPartInstruction())
					{
						tmpList = pip.getPNList();
						//select something in case everything is already finished
						if (!topRowSet)
						{
							tmpList.setSelectedIndex(0);
							topRowSet = true;
						}

						int i = 0;
						while (i < tmpList.getModel().getSize() && !stop)
						{
							next = ((MFSComponentListModel) tmpList.getModel()).getComponentRecAt(i);
							if (next.getIdsp().equals("X") //$NON-NLS-1$
									|| next.getIdsp().equals("R") //$NON-NLS-1$
									|| (next.getIdsp().equals("A") //$NON-NLS-1$
											&& !next.getMlri().equals(" ") //$NON-NLS-1$ 
											&& !next.getMlri().equals("0"))) //$NON-NLS-1$
							{
								tmpList.setSelectedIndex(i);
								this.spPartsInst.requestFocusInWindow();
								tmpList.ensureIndexIsVisible(i);
								stop = true;
							}
							i++;
						}//end of while loop on component list
					}//end of nonPartInstruction check
					index++;
				}//end of while loop on rowVector
			}//end of rowVector size > 0
		}
		catch (Exception e)
		{
			this.setCursor(Cursor.getDefaultCursor());
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}

		getActionIndicator().stopAction();

		getParentFrame().validate();
		this.spPartsInst.requestFocusInWindow();

		addMyListeners();
		focusPNListOrButton(this.pbHide);

		this.setCursor(Cursor.getDefaultCursor());
		this.repaint();
	}

	//~6A New method
	/** 
	 * Calls the UPDT_CRCB transaction. 
	 * @return 0 on success; nonzero on error
	 */
	private int updatePartContainers()
	{
		int rc = 0;
		try
		{
			boolean changed = false;
			int index = 0;
			MFSComponentRec cmp;
			Vector<String> xmlVector = new Vector<String>();
			int counter = 0, row = 0;
			int max_recs = 0;
			index = 0;

			if (this.fieldRowVector.size() > 0)
			{
				MFSComponentListModel tempLm = null;

				MfsXMLDocument xml_data = new MfsXMLDocument("UPDT_CRCB"); //$NON-NLS-1$
				xml_data.addOpenTag("DATA"); //$NON-NLS-1$
				while (row < this.fieldRowVector.size())
				{
					MFSPartInstructionJPanel pip = getRowVectorElementAt(row);
					if (!pip.getIsNonPartInstruction())
					{
						index = 0;
						tempLm = pip.getPNListModel();
						while (index < tempLm.size())
						{
							cmp = tempLm.getComponentRecAt(index);
							if (cmp.getRec_changed())
							{
								changed = true;
								xml_data.addOpenTag("RCD"); //$NON-NLS-1$
								xml_data.addCompleteField("MCTL", cmp.getMctl()); //$NON-NLS-1$
								xml_data.addCompleteField("CRCT", cmp.getCrct()); //$NON-NLS-1$
								xml_data.addCompleteField("CNTR", cmp.getCntr()); //$NON-NLS-1$
								xml_data.addCompleteField("IDSP", cmp.getIdsp()); //$NON-NLS-1$
								xml_data.addCompleteField("COOC", cmp.getCooc()); // ~2A //$NON-NLS-1$
								xml_data.addCloseTag("RCD"); //$NON-NLS-1$
								counter++;
							}

							/*
							 * If first record, get the length so we can know
							 * the maximum number of records sent by
							 * transaction, limited by the buffer size
							 */
							if (counter == 1)
							{
								String strXMLdata = xml_data.toString();
								int len = strXMLdata.toString().length();
								max_recs = MFSConstants.BUFFER_SIZE / len;
							}

							if (changed && counter >= max_recs)
							{
								xml_data.addCloseTag("DATA"); //$NON-NLS-1$
								xml_data.finalizeXML();
								xmlVector.addElement(xml_data.toString());
								xml_data = new mfsxml.MfsXMLDocument("UPDT_CRCB"); //$NON-NLS-1$
								xml_data.addOpenTag("DATA"); //$NON-NLS-1$
								counter = 0;
							}

							index++;
						}//end of loop thru this part list
					}//part instruction

					row++;

				}//while more rows

				if (counter > 0)
				{
					xml_data.addCloseTag("DATA"); //$NON-NLS-1$
					xml_data.finalizeXML();
					xmlVector.addElement(xml_data.toString());
				}

				if (changed)
				{
					//now loop thru the vector of xml documents and run the transactions
					int vectorIndex = 0;
					int overAllRC = 0;
					String erms = null;
					while (vectorIndex < xmlVector.size())
					{
						String data = (xmlVector.elementAt(vectorIndex));
						MFSTransaction updt_crcb = new MFSXmlTransaction(data);
						updt_crcb.setActionMessage("Updating Component Record	/ Container Content Please Wait..."); //$NON-NLS-1$
						MFSComm.getInstance().execute(updt_crcb, this);

						if (updt_crcb.getReturnCode() != 0)
						{
							overAllRC = updt_crcb.getReturnCode();
							erms = updt_crcb.getErms();
						}
						vectorIndex++;
					}//xml vector loop

					if (overAllRC != 0)
					{
						rc = 1;
						IGSMessageBox.showOkMB(getParentFrame(), null, erms, null);
					}
				}//need To Run the updt_crcb transaction
			}//at least one row in the row vector
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
		return rc;
	}

	/**
	 * Calls the UPDT_CRWC transaction.
	 * @return 0 on success; nonzero on error
	 */
	public int updt_crwc()
	{
		int rc = 0;

		try
		{
			final MFSConfig config = MFSConfig.getInstance();
			int index = 0;
			StringBuffer data = new StringBuffer();
			StringBuffer head = new StringBuffer();
			MFSComponentRec cmp;
			boolean changed = false;

			String BLANK27 = "                           "; //$NON-NLS-1$
			String BLANK76 = "                                                                            "; //$NON-NLS-1$

			int row = 0;
			index = 0;

			if (this.fieldRowVector.size() > 0)
			{
				while (row < this.fieldRowVector.size())
				{
					MFSPartInstructionJPanel pip = getRowVectorElementAt(row);
					if (!pip.getIsNonPartInstruction())
					{
						index = 0;
						MFSComponentListModel tempLm = pip.getPNListModel();
						while (index < tempLm.size())
						{
							cmp = tempLm.getComponentRecAt(index);
							int fqty = Integer.parseInt(cmp.getFqty());
							int qnty = Integer.parseInt(cmp.getQnty());
							if ((cmp.getIdsp().equals("A") //$NON-NLS-1$ 
									|| cmp.getIdsp().equals("X") //$NON-NLS-1$
									|| cmp.getIdsp().equals("R")) //$NON-NLS-1$
									&& (fqty != 0) && (qnty != fqty))
							{
								MFSComponentRec newCr = split_cr(cmp);
								if (newCr != null)
								{
									tempLm.add(index + 1, newCr);
									this.redoLayout(row);
								}
							}
							index++;
						}
					}//part instruction

					row++;

				}//while more rows
			}//at least one row in the row vector

			index = 0;
			while (index < this.fieldComponentListModel.size())
			{
				cmp = this.fieldComponentListModel.getComponentRecAt(index);
				if (cmp.getRec_changed())
				{
					changed = true;
					data.append(cmp.getMctl());
					data.append(cmp.getCrct());
					data.append(cmp.getInpn());
					data.append(cmp.getInec());
					data.append(cmp.getInsq());
					data.append(cmp.getInca());
					data.append(cmp.getAmsi());
					data.append(cmp.getMspi());
					data.append(cmp.getMcsn());
					data.append(cmp.getCwun());
					data.append(cmp.getIdsp());
					data.append(cmp.getCntr());
					data.append(cmp.getFqty());
					data.append(" "); //$NON-NLS-1$
					data.append(cmp.getShtp());
					data.append(cmp.getMlri());
					data.append(cmp.getPnri());
					data.append("J"); //$NON-NLS-1$
					data.append(cmp.getCooc());
					data.append(BLANK27);
				}
				index++;
			}

			if (changed)
			{
				/* get current date and time */
				SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-ddHH.mm.ss"); //$NON-NLS-1$
				Date currTime = new Date();
				String dat = fmt.format(currTime);

				head.append("UPDT_CRWC "); //$NON-NLS-1$
				head.append(config.get8CharUser());
				head.append(config.get8CharCell());
				head.append(dat);
				head.append(config.get8CharCellType());
				head.append(BLANK76);

				String errorString = ""; //$NON-NLS-1$
				int start = 0;
				/* up to 30 components can be updated per transaction */
				int tranlen = 128 * 30; 
				int end = tranlen;
				int len = data.length();
				while (rc == 0 && len > 0)
				{
					String components = ""; //$NON-NLS-1$
					if (data.substring(start).length() < tranlen)
					{
						components = data.substring(start);
					}
					else
					{
						components = data.substring(start, end);
					}

					start += tranlen;
					end += tranlen;
					len -= tranlen;

					MFSTransaction updt_crwc = new MFSFixedTransaction(head + components);
					updt_crwc.setActionMessage("Updating Work Unit Components, Please Wait..."); //$NON-NLS-1$
					MFSComm.getInstance().execute(updt_crwc, this);
					rc = updt_crwc.getReturnCode();

					if (rc != 0)
					{
						errorString = updt_crwc.getOutput();
					}
				}

				if (rc != 0)
				{
					IGSMessageBox.showOkMB(getParentFrame(), null, errorString, null);
				}
			}
		}
		catch (Exception e)
		{
			this.setCursor(Cursor.getDefaultCursor());
			rc = 11;
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
		return rc;
	}

	/**
	 * Calls the UPDT_INSTR transaction
	 * @param status 'C'omplete or 'S'uspend
	 * @return 0 on success; nonzero on error
	 */
	public int updt_instr(String status)
	{
		int rc = 0;

		try
		{
			int index = 0;
			final MFSConfig config = MFSConfig.getInstance();

			// ~41 do not call UPDT_INSTR
			if (!config.containsConfigEntry("EFFICIENCYON*DUMBCLIENT")) //$NON-NLS-1$
			{
				boolean hideMode = isHideMode();
	
				MfsXMLDocument xml_header = new MfsXMLDocument("UPDT_INSTR"); //$NON-NLS-1$
				xml_header.addOpenTag("DATA"); //$NON-NLS-1$
				xml_header.addCompleteField("MCTL", this.fieldHeaderRec.getMctl()); //$NON-NLS-1$
				xml_header.addCompleteField("NMBR", this.fieldHeaderRec.getNmbr()); //$NON-NLS-1$
				xml_header.addCompleteField("USER", config.get8CharUser()); //$NON-NLS-1$
				xml_header.addCompleteField("CELL", config.get8CharCell()); //$NON-NLS-1$
				xml_header.addCompleteField("CTYP", config.get8CharCellType()); //$NON-NLS-1$
	
				index = 0;
				int counter = 0;
	
				MfsXMLDocument xml_data1 = new MfsXMLDocument();
	
				while (index < this.fieldRowVector.size() && rc == 0)
				{
					MFSPartInstructionJPanel pip = getRowVectorElementAt(index);
					// if the status has changed or (we are complete && (we have
					// only red parts left || we have a non part
					// instruction and we are in hide mode and the instruction class
					// is blank) )
					if (pip.getInstructionRec().getChanged() ||
						(status.equals("C") && //$NON-NLS-1$
						((pip.onlyRedPartsLeft() && !pip.getInstructionRec().getCompletionStatusOriginal().equals("C")) || //$NON-NLS-1$
						(pip.getIsNonPartInstruction() && hideMode && pip.getInstructionRec().getInstrClass().equals(" "))))) //$NON-NLS-1$
					{
						xml_data1.addOpenTag("RCD"); //$NON-NLS-1$
						xml_data1.addCompleteField("INSX", pip.getInstructionRec().getSuff()); //$NON-NLS-1$
						xml_data1.addCompleteField("NMSQ", pip.getInstructionRec().getIseq()); //$NON-NLS-1$
	
						//if we only have red parts left then I'm going to call it complete
						if ((pip.onlyRedPartsLeft() && status.equals("C")) //$NON-NLS-1$
								|| (pip.getIsNonPartInstruction() && hideMode && 
										pip.getInstructionRec().getInstrClass().equals(" "))) //$NON-NLS-1$
						{
							xml_data1.addCompleteField("ECOD", "C"); //$NON-NLS-1$ //$NON-NLS-2$
						}
						else
						{
							xml_data1.addCompleteField("ECOD", pip.getInstructionRec().getCompletionStatus()); //$NON-NLS-1$
						}
						xml_data1.addCompleteField("SCOD", " "); //$NON-NLS-1$ //$NON-NLS-2$
						xml_data1.addCloseTag("RCD"); //$NON-NLS-1$
						counter++;
					}
					index++;
	
					//if we run into our max buffer size of 47, or we just need to send the first time, we will
					//actually send an entirely new transactions. So we keep the header in each send
					if (counter == 47 || index == this.fieldRowVector.size() && counter > 0)
					{
						xml_data1.addCloseTag("DATA"); //$NON-NLS-1$
						xml_data1.finalizeXML();
	
						MFSTransaction updt_instr = new MFSXmlTransaction(xml_header.toString() + xml_data1.toString());
						updt_instr.setActionMessage("Updating Instructions, Please Wait..."); //$NON-NLS-1$
						MFSComm.getInstance().execute(updt_instr, this);
						rc = updt_instr.getReturnCode();
	
						if (rc != 0)
						{
							IGSMessageBox.showOkMB(getParentFrame(), null, updt_instr.getErms(), null);
						}
	
						counter = 0;
						xml_data1 = new MfsXMLDocument();
					}//end of time to send it
				}
			}
		}
		catch (Exception e)
		{
			rc = 11;
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
		return rc;
	}

	/**
	 * Calls the UPDT_WUSN transaction
	 * @return 0 on success; nonzero on error
	 */
	public int updt_wusn()
	{
		int rc = 0;

		try
		{
			String pilotFlag = "N"; //$NON-NLS-1$
			//check for DASDPILOT config entry, used to determine if we are
			if (MFSConfig.getInstance().containsConfigEntry("DASDPILOT")) //$NON-NLS-1$
			{
				pilotFlag = "Y"; //$NON-NLS-1$
			}
			/* Perform the updt_wusn method. */
			/* Chande to IGSXML ~34 */ 
			IGSXMLTransaction updt_wusn = new IGSXMLTransaction("UPDT_WUSN"); //$NON-NLS-1$
			updt_wusn.startDocument();
			updt_wusn.startElement("DATA");  //$NON-NLS-1$
			updt_wusn.addElement("MCTL", this.fieldHeaderRec.getMctl());  //$NON-NLS-1$
			updt_wusn.addElement("PILF", pilotFlag); //$NON-NLS-1$			
			updt_wusn.endElement("DATA");  //$NON-NLS-1$		
			updt_wusn.endDocument();
			updt_wusn.setActionMessage("Updating Work Unit Serial Number, Please Wait..."); //$NON-NLS-1$
			updt_wusn.run();
			
			rc = updt_wusn.getReturnCode();

			if (rc != 0)
			{
				IGSMessageBox.showOkMB(getParentFrame(), null, updt_wusn.getErms(), null);
			}
		}
		catch (Exception e)
		{
			rc = 11;
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
		return rc;
	}

	//~24A New method //~30C removed MFSAcknowledgeDialog
	/**
	 * Updates the weight and dimensions of all containers for this work unit.
	 * @param endOrSuspendFlag "E" for end, "S" for suspend
	 * @return 0 on success; nonzero on error
	 */
	private int updateWeightDim(String endOrSuspendFlag)
	{
		int rc = 0;
		final MFSConfig config = MFSConfig.getInstance();
		final String nmbr = this.fieldHeaderRec.getNmbr();
		final String prln = this.fieldHeaderRec.getPrln().trim();
		String configValue = null;//~44A
		if ((configValue = config.containsNmbrPrlnFlagEntry("ENTITYWGT", nmbr, prln, endOrSuspendFlag)) != null ) //$NON-NLS-1$//~44C
		{
			MFSWeightDimDialog dialog = new MFSWeightDimDialog(getParentFrame(), this);
			rc = dialog.displayForWorkUnit(getCurrMctl(),configValue);//~44C
			if (rc == 0 && dialog.getCancelSelected())
			{
				rc = 10;
			}
		}
		return rc;
	}
	
	//~46A
	/**
	 * Verfify the Hazmat Labeling Part Numbers
	 */
	private boolean vfyHazmatLabeling(String ecod)
	{
		boolean proceed = true;
		
		try
		{
			if(MFSHazmatLabeling.isHazmatTrigger(fieldHeaderRec.getNmbr(), fieldHeaderRec.getPrln()))
			{	
				String hazmatTriggerValue = MFSHazmatLabeling.getHazmatTriggerValue(fieldHeaderRec.getNmbr(),
																					fieldHeaderRec.getPrln());
				String mode = "PP"; // ProdPack	
				
				MFSHazmatLabeling hazmatLbl = new MFSHazmatLabeling(getParentFrame(), this);
				
				//Subassembly
				if(fieldHeaderRec.getWtyp().equals("S"))
				{
					hazmatLbl.setTriggerSource("SUBASSEMBLY");
					proceed = hazmatLbl.hazmatLabelingForFFBM(getCurrMctl(), mode);
				}
				else
				{
					StringBuilder paramBuffer = new StringBuilder();
					paramBuffer.append("<CMCT>"); 
					paramBuffer.append(getCurrMctl()); 
					paramBuffer.append("</CMCT>");
					paramBuffer.append("<ECOD>");
					paramBuffer.append(ecod);
					paramBuffer.append("</ECOD>");
					paramBuffer.append("<NMBR>"); 
					paramBuffer.append(this.fieldHeaderRec.getNmbr());
					paramBuffer.append("</NMBR>"); 
					paramBuffer.append("<OLEV>");
					paramBuffer.append(this.fieldHeaderRec.getOlev());
					paramBuffer.append("</OLEV>");
					
					hazmatLbl.setTriggerSource("ENDWU");
					hazmatLbl.setOptionalParameters(paramBuffer.toString());
					
					// System level get all top level cntrs
					if(fieldHeaderRec.getOlev().equals("S") || hazmatTriggerValue.equals("S"))
					{
						// Pallet verification
						String palletOpNmbr = MFSConfig.getInstance().getConfigValue("PALLET");
						
						if(!palletOpNmbr.equals(MFSConfig.NOT_FOUND))
						{
							mode = "OP"; // OverPack
						}
						
						proceed = hazmatLbl.hazmatLabeling(getCurrMctl(), 
															fieldHeaderRec.getMfgn(), 
															fieldHeaderRec.getIdss(), 
															mode);
					}
					else // Unit level
					{
						Set<String> cntrSet = new HashSet<String>();
						
						int componentSize = this.fieldComponentListModel.getSize();
						
						for(int componentIndex = 0; componentIndex < componentSize; componentIndex++)
						{
							MFSComponentRec component = this.fieldComponentListModel.getComponentRecAt(componentIndex);
							
							if(null != component.getCntr() && !component.getCntr().trim().equals(""))
							{
								cntrSet.add(component.getCntr());
							}
						}
						
						// case no components are installed in this operation but a cntr is assigned
						if(null != fieldHeaderRec.getCntr() && !fieldHeaderRec.getCntr().trim().equals(""))
						{
							cntrSet.add(fieldHeaderRec.getCntr());
						}
						
						String[] cntrs = new String[cntrSet.size()];
						proceed = hazmatLbl.hazmatLabeling(Arrays.asList(cntrSet.toArray(cntrs)), mode);
					}
				}
			}
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, e.getMessage(), null);
			proceed = false;
		}	
		
		return proceed;
	}
	
	/**
	 * Calls the VFYMLTPART transaction
	 * @return 0 on success; nonzero on error
	 */
	public int verifyPendingParts()
	{
		int rc = 0;

		try
		{
			int index = 0;
			MFSComponentRec cmp;

			Vector <String> xmlVector = new Vector<String>();
			int counter = 0;

			int row = 0;
			index = 0;
			boolean needToRun = false;

			if (this.fieldRowVector.size() > 0)
			{
				MfsXMLDocument xml_header = new MfsXMLDocument("VFYMLTPART"); //$NON-NLS-1$
				xml_header.addOpenTag("DATA"); //$NON-NLS-1$
				xml_header.addCompleteField("MFGN", this.fieldHeaderRec.getMfgn()); //$NON-NLS-1$
				xml_header.addCompleteField("IDSS", this.fieldHeaderRec.getIdss()); //$NON-NLS-1$
				xml_header.addCompleteField("TYPZ", this.fieldHeaderRec.getTypz()); //$NON-NLS-1$
				xml_header.addCompleteField("PRLN", this.fieldHeaderRec.getPrln()); //$NON-NLS-1$
				xml_header.addCompleteField("MCTL", this.fieldHeaderRec.getMctl()); //$NON-NLS-1$
				xml_header.addCompleteField("EBLD", this.fieldHeaderRec.getUnpr()); //$NON-NLS-1$
				xml_header.addCompleteField("CTYP", MFSConfig.getInstance().get8CharCellType()); //$NON-NLS-1$
				xml_header.addCompleteField("NMBR", this.fieldHeaderRec.getNmbr()); //$NON-NLS-1$
				xml_header.addCompleteField("MALC", "       "); //$NON-NLS-1$ //$NON-NLS-2$
				xml_header.addCompleteField("MILC", "            "); //$NON-NLS-1$ //$NON-NLS-2$
				xml_header.addCompleteField("RCON", this.fieldHeaderRec.getRcon()); //$NON-NLS-1$

				MFSComponentListModel tempLm = null;
				MfsXMLDocument xml_data = new MfsXMLDocument();

				while (row < this.fieldRowVector.size())
				{
					MFSPartInstructionJPanel pip = getRowVectorElementAt(row);
					if (!pip.getIsNonPartInstruction())
					{
						index = 0;
						tempLm = pip.getPNListModel();
						while (index < tempLm.size())
						{
							cmp = tempLm.getComponentRecAt(index);
							if (cmp.getBufferStatus().equals("2")) //$NON-NLS-1$
							{
								xml_data.addOpenTag("PART"); //$NON-NLS-1$
								xml_data.addCompleteField("ITEM", cmp.getItem()); //$NON-NLS-1$
								xml_data.addCompleteField("INPN", cmp.getInpn()); //$NON-NLS-1$
								if (cmp.getInec().equals("            ")) //$NON-NLS-1$
								{
									xml_data.addCompleteField("INEC", "*NONE       "); //$NON-NLS-1$ //$NON-NLS-2$
								}
								else
								{
									xml_data.addCompleteField("INEC", cmp.getInec()); //$NON-NLS-1$
								}
								xml_data.addCompleteField("INSQ", cmp.getInsq()); //$NON-NLS-1$
								xml_data.addCompleteField("CWUN", cmp.getCwun()); //$NON-NLS-1$
								xml_data.addCompleteField("MMDL", cmp.getMmdl()); //$NON-NLS-1$
								xml_data.addCompleteField("PLL1", cmp.getPll1()); //$NON-NLS-1$
								xml_data.addCompleteField("PLL2", cmp.getPll2()); //$NON-NLS-1$
								xml_data.addCompleteField("PLL3", cmp.getPll3()); //$NON-NLS-1$
								xml_data.addCompleteField("PLL4", cmp.getPll4()); //$NON-NLS-1$
								xml_data.addCompleteField("PLL5", cmp.getPll5()); //$NON-NLS-1$
								xml_data.addCompleteField("PARI", cmp.getPari()); //$NON-NLS-1$
								xml_data.addCompleteField("CRCT", cmp.getCrct()); //$NON-NLS-1$
								xml_data.addCompleteField("MATP", cmp.getMatp()); //$NON-NLS-1$
								xml_data.addCompleteField("MSPI", cmp.getMspi()); //$NON-NLS-1$
								xml_data.addCompleteField("MCSN", cmp.getMcsn()); //$NON-NLS-1$
								xml_data.addCompleteField("AMSI", cmp.getAmsi()); //$NON-NLS-1$
								xml_data.addCompleteField("COOC", cmp.getCooc()); //$NON-NLS-1$
								xml_data.addCloseTag("PART"); //$NON-NLS-1$
								needToRun = true;

								counter++;
								if (counter == 10)
								{
									xml_data.finalizeXML();
									xmlVector.addElement(xml_header.toString() + xml_data.toString());
									xml_data = new MfsXMLDocument();
									counter = 0;
								}
							}
							index++;
						}//end of loop thru this part list
					}//part instruction

					row++;

				}//while more rows

				if (counter > 0)
				{
					xml_data.finalizeXML();
					xmlVector.addElement(xml_header.toString() + xml_data.toString());
				}

				if (!needToRun)
				{
					String erms = "No Parts To Verify"; //$NON-NLS-1$
					IGSMessageBox.showOkMB(getParentFrame(), null, erms, null);
				}
				else
				{
					//now loop thru the vector of xml documents and run the transactions
					int vectorIndex = 0;
					int overAllRC = 0;
					MfsXMLDocument errorList = new MfsXMLDocument();
					while (vectorIndex < xmlVector.size())
					{
						String data = (String) (xmlVector.elementAt(vectorIndex));
						MFSTransaction vfymlpart = new MFSXmlTransaction(data);
						vfymlpart.setActionMessage("Verifying Pending Parts, Please Wait..."); //$NON-NLS-1$
						MFSComm.getInstance().execute(vfymlpart, this);

						if (vfymlpart.getReturnCode() != 0)
						{
							overAllRC = vfymlpart.getReturnCode();
						}

						//now we are going to loop thru the components again and update the results
						row = 0;
						String tmpPart = ""; //$NON-NLS-1$
						String tmpCrct = ""; //$NON-NLS-1$
						String tmpRret = ""; //$NON-NLS-1$
						String tmpCoo = ""; //$NON-NLS-1$
						String tmpErms = ""; //$NON-NLS-1$
						rc = 0;

						while (row < this.fieldRowVector.size() && rc == 0)
						{
							MFSPartInstructionJPanel pip = getRowVectorElementAt(row);
							if (!pip.getIsNonPartInstruction())
							{
								index = 0;
								tempLm = pip.getPNListModel();
								while (index < tempLm.size())
								{
									cmp = tempLm.getComponentRecAt(index);
									if (cmp.getBufferStatus().equals("2")) //$NON-NLS-1$
									{
										//figure out if good return code for this part or not
										boolean found = false;
										MfsXMLParser xmlParser = new MfsXMLParser(vfymlpart.getOutput());

										//look thru the RCD XML tags, to see if
										// we can find the results for this
										try
										{
											tmpPart = xmlParser.getField("RCD"); //$NON-NLS-1$
										}
										catch (MISSING_XML_TAG_EXCEPTION e)
										{
											tmpPart = ""; //$NON-NLS-1$
											rc = 10;
											String erms = "VFYMLTPART returned No RCD tags!"; //$NON-NLS-1$
											IGSMessageBox.showOkMB(getParentFrame(), null, erms, null);
										}

										while (!found && !tmpPart.equals("") && rc == 0) //$NON-NLS-1$
										{
											MfsXMLParser xmlRcd = new MfsXMLParser(tmpPart);
											try
											{
												tmpCrct = xmlRcd.getField("CRCT"); //$NON-NLS-1$
											}
											catch (MISSING_XML_TAG_EXCEPTION e)
											{
												rc = 10;
												String erms = "Tag(s) Missing from returned RCD!"; //$NON-NLS-1$
												IGSMessageBox.showOkMB(getParentFrame(), null, erms, null);
											}
											if (rc == 0)
											{
												try
												{
													tmpErms = xmlRcd.getField("ERMS"); //$NON-NLS-1$
												}
												catch (MISSING_XML_TAG_EXCEPTION e)
												{
													// no problem
												}
											}
											if (rc == 0)
											{
												try
												{
													tmpCoo = xmlRcd.getField("COOC"); //$NON-NLS-1$
												}
												catch (MISSING_XML_TAG_EXCEPTION e)
												{
													rc = 10;
													String erms = "Tag(s) Missing from returned RCD!"; //$NON-NLS-1$
													IGSMessageBox.showOkMB(getParentFrame(), null, erms, null);
												}
											}
											if (rc == 0)
											{
												try
												{
													tmpRret = xmlRcd.getField("RRET"); //$NON-NLS-1$
												}
												catch (MISSING_XML_TAG_EXCEPTION e)
												{
													rc = 10;
													String erms = "Tag(s) Missing from returned RCD!"; //$NON-NLS-1$
													IGSMessageBox.showOkMB(getParentFrame(), null, erms, null);
												}
											}
											if (tmpCrct.equals(cmp.getCrct()) && rc == 0)
											{
												found = true;

												if (tmpRret.equals("00000")) //$NON-NLS-1$
												{
													cmp.setBufferStatus("3"); //$NON-NLS-1$
													if (!cmp.getPari().equals("0") && !cmp.getPari().equals(" "))  //$NON-NLS-1$ //$NON-NLS-2$
													{
														cmp.setCooc(tmpCoo);
													}
													cmp.updtInstalledParts();
													cmp.updtDisplayString();
													cmp.updtIRDisplayString();
												}
												else
												{
													errorList.addOpenTag("RCD"); //$NON-NLS-1$
													errorList.addCompleteField("INPN", cmp.getInpn()); //$NON-NLS-1$
													errorList.addCompleteField("INSQ", cmp.getInsq()); //$NON-NLS-1$
													errorList.addCompleteField("MCTL", cmp.getMctl()); //$NON-NLS-1$
													errorList.addCompleteField("WOHE", tmpRret); //$NON-NLS-1$
													errorList.addCompleteField("ERMS", tmpErms); //$NON-NLS-1$
													errorList.addCloseTag("RCD"); //$NON-NLS-1$
													
													cmp.setBufferStatus("-1"); //$NON-NLS-1$
													cmp.setIdsp("A"); //$NON-NLS-1$
													cmp.setFqty("00000"); //$NON-NLS-1$
													cmp.setInpn("            "); //$NON-NLS-1$
													cmp.setInsq("            "); //$NON-NLS-1$
													cmp.setInca("            "); //$NON-NLS-1$
													cmp.setInec("            "); //$NON-NLS-1$
													cmp.setCwun("        "); //$NON-NLS-1$
													cmp.updtDisplayString();
													cmp.updtIRDisplayString();
													cmp.updtInstalledParts();
													cmp.setRec_changed(false);
												}
											}//good retreival of CRCT and RRET
											tmpPart = xmlParser.getNextField("RCD"); //$NON-NLS-1$
										}
										//shouldn't happen I don't think, but we'll keep track
										if (!found)
										{
											System.out.println("Couldn't Find Result for CRCT = " + cmp.getCrct()); //$NON-NLS-1$
										}
									} //is a buffered transaction part that needs to be verified
									index++;
								}//index loop on the part list
							}//part instruction
							row++;
						}//while more rows
						vectorIndex++;
					}//xml vector loop

					if (overAllRC != 0)
					{
						rc = 10;
						//String erms = "Errors Occurred - Some Parts Have Failed Verification";
						//IGSMessageBox.showOkMB(getParentFrame(), null, erms, null);
						showPNSNErrorList(errorList.toString(), 0);
					}
				}//need To Run the verify transaction
			}//at least one row in the row vector
		}
		catch (Exception e)
		{
			rc = 11;
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
		this.repaint();
		return rc;
	}
	
	//~36A
	/** 
	 * Performs View Data Collection Dialog <code> MFSPartDataCollectionDialog </code>
	 * Show all data collections by CRCT 
	 */
	private void viewDataCollection()
	{
		JList tmpList = getRowVectorElementAt(this.fieldActiveRow).getPNList();		
		MFSComponentListModel tmpLm = (MFSComponentListModel) tmpList.getModel();
		
		if (tmpLm.size() != 0)
		{
				MFSComponentRec cmp = tmpLm.getComponentRecAt(tmpList.getSelectedIndex());
				MFSPartDataCollectDialog collectionDialog;
				collectionDialog = new MFSPartDataCollectDialog(this.getParentFrame(),this.fieldHeaderRec,MFSPartDataCollectDialog.COLL_VIEW,cmp.getCrct(),cmp.getMctl());
				if(!collectionDialog.wasDialogDisplayed())
				{
					IGSMessageBox.showOkMB(getParentFrame(), null, collectionDialog.getErms(), null);
				}							
		}
	}
	

	/** Displays an <code>MFSViewInstPartsPanel</code> with the installed parts. */
	public void viewInstParts()
	{
		try
		{
			removeMyListeners();

			int rc = 0;

			/* start the RTV_IP transaction thread */
			StringBuffer data = new StringBuffer();
			data.append("RTV_IP    "); //$NON-NLS-1$
			data.append(this.fieldHeaderRec.getMfgn());
			data.append(this.fieldHeaderRec.getIdss());
			data.append(getCurrMctl());
			data.append(this.fieldHeaderRec.getOlev());
			data.append("J"); //$NON-NLS-1$

			MFSTransaction rtv_ip = new MFSFixedTransaction(data.toString());
			rtv_ip.setActionMessage("Retrieving Installed Parts, Please Wait..."); //$NON-NLS-1$
			MFSComm.getInstance().execute(rtv_ip, this);
			rc = rtv_ip.getReturnCode();

			if (rc == 0)
			{
				MFSViewInstPartsPanel viewInst = new MFSViewInstPartsPanel(
						getParentFrame(), this, this.fieldHeaderRec);
				int size = viewInst.loadListModel(rtv_ip.getOutput());

				if (size == 0)
				{
					String erms = "No Installed Parts Found From Other Operations"; //$NON-NLS-1$
					IGSMessageBox.showOkMB(getParentFrame(), null, erms, null);
				}
				else
				{
					this.fieldChildPanel = viewInst;
					getParentFrame().displayMFSPanel(viewInst);
				}
			}
			else
			{
				IGSMessageBox.showOkMB(getParentFrame(), null, rtv_ip.getErms(), null);
			}
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}

		addMyListeners();
		focusPNListOrButton(this.pbViewIns);
	}

	/** Perform the vpdBurn method. */
	public void vpdBurn()
	{
		int rc = 0;
		String errorString = ""; //$NON-NLS-1$

		try
		{
			final MFSConfig config = MFSConfig.getInstance();
			removeMyListeners();
			getParentFrame().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

			if (config.containsConfigEntry("VPDSRV") //$NON-NLS-1$
					&& config.containsConfigEntry("VPDRTR")) //$NON-NLS-1$
			{
				//~16C Moved file logic to vpdCreateFile
				vpdCreateFile();
			}
			else
			{
				rc = 10;
				errorString = "Missing Config File Entries: VPDSRV and/or VPDRTR"; //$NON-NLS-1$
			}

			if (rc == 0)
			{
				/* declare instance of new VPDproc class */
				/* pass into the constructor all of the neccessary properties */
				/* this will get the mifprocessor set up the way it needs to be */
				/* then perform the burn of the card, we will check the return code */
				MFSVPDProc mifprocessor = new MFSVPDProc(this.fieldHeaderRec, getParentFrame(), this);
				rc = mifprocessor.processMifBurn();

				if (rc == 0)
				{
					/* change the value in the config file for an burn flag */
					/* to YES because we have successfully burned */
					config.setConfigValue("BURNED", "YES"); //$NON-NLS-1$ //$NON-NLS-2$
				}// end of good return code from processMif()
			}// end of check for VPDSRV
			else
			{
				IGSMessageBox.showOkMB(getParentFrame(), null, errorString, null);
			}
		}// end of try
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, errorString, null);
		}

		getParentFrame().setCursor(Cursor.getDefaultCursor());
		addMyListeners();
		focusPNListOrButton(this.pbBurn);
	}

	//~16A New method
	/**
	 * Creates a file for Mike's dll, called addrfile, which contains:
	 * <pre>       
	 * MFSRTRWK  C01PROD            3050
	 * MFSRTRAU  C01PROD            3050
	 * MFSRTRMC  C01PROD            3050
	 * MFSRTRSC  C01PROD            3050
	 *  </pre>
	 * @throws IOException if the file could not be created
	 */
	private void vpdCreateFile()
		throws IOException
	{
		final MFSConfig config = MFSConfig.getInstance();

		File out = new File("addrfile"); //$NON-NLS-1$
		FileWriter fwtr = new FileWriter(out);
		BufferedWriter bwtr = new BufferedWriter(fwtr);

		final String vpdsrv = config.getConfigValue("VPDSRV"); //$NON-NLS-1$
		final String vpdrtr = config.getConfigValue("VPDRTR"); //$NON-NLS-1$
		final int period = vpdsrv.indexOf("."); //$NON-NLS-1$
		final String info = vpdsrv.substring(0, period) + "            " + vpdrtr; //$NON-NLS-1$

		String addrfileInfo = "MFSRTRWK  " + info; //$NON-NLS-1$
		bwtr.write(addrfileInfo, 0, addrfileInfo.length());
		bwtr.newLine();
		bwtr.flush();

		addrfileInfo = "MFSRTRAU  " + info; //$NON-NLS-1$
		bwtr.write(addrfileInfo, 0, addrfileInfo.length());
		bwtr.newLine();
		bwtr.flush();

		addrfileInfo = "MFSRTRMC  " + info; //$NON-NLS-1$
		bwtr.write(addrfileInfo, 0, addrfileInfo.length());
		bwtr.newLine();
		bwtr.flush();

		addrfileInfo = "MFSRTRSC  " + info; //$NON-NLS-1$
		bwtr.write(addrfileInfo, 0, addrfileInfo.length());
		bwtr.newLine();
		bwtr.flush();

		bwtr.close();
	}
	
	/** Perform the vpdUnburn method. */
	public void vpdUnburn()
	{

		/* Perform the vpdUnburn method. */
		/* this method will be executed almost exactly the same as the */
		/* vpdBurn function, the only difference will be the mode passed */
		/* to Mike's dll, and there will be no labels printed on an Unburn */
		int rc = 0;
		String errorString = ""; //$NON-NLS-1$

		try
		{
			final MFSConfig config = MFSConfig.getInstance();
			removeMyListeners();
			getParentFrame().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

			if (config.containsConfigEntry("VPDSRV") //$NON-NLS-1$
					&& config.containsConfigEntry("VPDRTR")) //$NON-NLS-1$
			{
				//~16C Moved file logic to vpdCreateFile
				vpdCreateFile();
			}
			else
			{
				rc = 10;
				errorString = "Missing Config File Entries: VPDSRV and/or VPDRTR"; //$NON-NLS-1$
			}

			if (rc == 0)
			{

				/* declare instance of new VPDproc class */
				/* pass into the constructor all of the neccessary properties */
				/* this will get the mifprocessor set up the way it needs to be */
				/* then perform the burn of the card, we will check the return code */
				MFSVPDProc mifprocessor = new MFSVPDProc(this.fieldHeaderRec, getParentFrame(), this);
				rc = mifprocessor.processMifUnBurn();

				if (rc == 0)
				{
					/* change the value in the config file for an unburn flag */
					/* to YES because we have successfully unburned */
					config.setConfigValue("UNBURNED", "YES"); //$NON-NLS-1$ //$NON-NLS-2$
				}
			}//end of valid vpdsrv and vpdrtr found
			else
			{
				IGSMessageBox.showOkMB(getParentFrame(), null, errorString, null);
			}
		}// end of try
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
		getParentFrame().setCursor(Cursor.getDefaultCursor());
		addMyListeners();
		focusPNListOrButton(this.pbUnburn);
	}

	/**
	 * Writes the microcode file for DASD personalization/unpersonaliztaion
	 * @param codeName the code name returned by RTV_DS10
	 * @param loadId the load if of the file name returned by RTV_DS10
	 * @return 0 on success; nonzero on failure
	 */
	public String writeMicroCodeFile(String codeName, String loadId)
	{
		String rcStr = ""; //$NON-NLS-1$
		String transaction = "";				/*~48A*/
		int rc = 0;
		try
		{
			getParentFrame().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

			/* start the RTV_DASDP transaction thread */
			if (MFSConfig.getInstance().getConfigValue("IPROXY").equalsIgnoreCase("YES"))		/*~48A*/
			{																					/*~48A*/
				transaction = "RTV_DASDPI" + codeName + loadId;									/*~48A*/
			}																					/*~48A*/
			else																				/*~48A*/
			{																					/*~48A*/
				transaction = "RTV_DASDP " + codeName + loadId;									/*~48A*/
			}																					/*~48A*/
			
			DasdComm myDasdComm = new DasdComm(MFSConfig.getInstance(), transaction); //~37C ~48C//$NON-NLS-1$	
			Thread myCommThread = new Thread(myDasdComm);
			myCommThread.start();

			/* wait for RTV_DASDP transaction thread to finish */
			while (myCommThread.isAlive())
			{
				getActionIndicator().startAction("Retrieving Microcode File " + codeName.trim() + ".  Please Wait..."); //$NON-NLS-1$ //$NON-NLS-2$
				this.update(getGraphics());
				try
				{
					Thread.sleep(100);
				}
				catch (InterruptedException e)
				{
					rcStr = "Interrupted Exception!"; //$NON-NLS-1$
				}
			}
			getActionIndicator().stopAction();
			rc = myDasdComm.getRc();
			getParentFrame().setCursor(Cursor.getDefaultCursor());

			if (rc != 0 && rc != 100)
			{
				rcStr = myDasdComm.getData();
			}//end of bad return from RTV_DASDP
			else
			{
				getActionIndicator().startAction("Creating Local Microcode File " + codeName.trim() + ".bin .  Please Wait..."); //$NON-NLS-1$ //$NON-NLS-2$

				//~16C Used getDasdpPath instead of c:\\mfsexe40\\dasdp
				FileOutputStream fos = new FileOutputStream(getDasdpPath(codeName.trim()));
				fos.write(myDasdComm.getByteData());
				fos.flush();
				fos.close();
				getActionIndicator().stopAction();
			}//end of good rc from RTV_DASDP
		}
		catch (Exception e)
		{
			rcStr = "Program Exception: " + e; //$NON-NLS-1$
			getParentFrame().setCursor(Cursor.getDefaultCursor());
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
		return rcStr;
	}
	/**
	 * Calls AutoSearchLog as a separate thread
	 */
	public void startAutoSearchLog()
	{
		boolean complete = false;
		boolean[] completeArray = new boolean[3];
		this.checkIfComplete(completeArray);
		if(completeArray[MFSDirectWorkPanel.LT_COMPLETE_CHECK_COMPLETE] &&
		   !completeArray[MFSDirectWorkPanel.LT_COMPLETE_CHECK_REMOVE] &&
		   ! completeArray[MFSDirectWorkPanel.LT_COMPLETE_CHECK_MANDATORY])
		{
			complete = true;
		}
		
        if(this.pbSrchLog.isEnabled() && this.fieldComponentListModel.size() != 0 && !complete)
        {
    		Thread t = new Thread()
    		{
    			public void run()
    			{
    				//sometimes actionlisteners are not on, so replacing with call to method
    	        	//pbSrchLog.doClick();
					addpn(MFSLogPartDialog.LT_SEARCH_LOG);
    			}
    		};
    		t.start();	        	
        }
    }															
	/**
	 * Invoked when an action occurs.
	 * @param ae the <code>ActionEvent</code>
	 */
	public void actionPerformed(ActionEvent ae)
	{
		//~14A Added try/catch
		try
		{
			final Object source = ae.getSource();
			if (source != this.pbAdd)
			{
				this.ensureActiveRowIndexIsHighlighted();
				if (this.fieldBlueRow != -1)
				{
					getRowVectorElementAt(this.fieldBlueRow).unDoChangeColor();
				}
			}

			//~14C Removed ConnEtoMx methods
			//~14C Converted if/if/if/etc. to if/else if/else if/etc.
			if (source == this.pbSrchLog)
			{
				this.addpn(MFSLogPartDialog.LT_SEARCH_LOG);
			}
			else if (source == this.pbAdd)
			{
				this.addpn(MFSLogPartDialog.LT_ADD);
			}
			else if (source == this.pbEnd)
			{
				this.end_work();
			}
			else if (source == this.pbAutoLog)
			{
				this.addpn(MFSLogPartDialog.LT_AUTO_LOG);
			}
			else if (source == this.pbExtFunc)
			{
				this.extendedFunctions();
			}
			else if (source == this.pbRework)
			{
				this.rwk_part();
			}
			else if (source == this.pbViewIns)
			{
				this.viewInstParts();
			}
			else if (source == this.pbCntr)
			{
				this.rtv_cntr();
			}
			else if (source == this.pbCInC)
			{
				this.rtv_cinc();
			}
			else if (source == this.pbSplit)
			{
				this.split_button();
			}
			else if (source == this.pbShort)
			{
				this.short_button();
			}
			else if (source == this.pbMove)
			{
				this.move();
			}
			else if (source == this.pbUnburn)
			{
				this.vpdUnburn();
			}
			else if (source == this.pbBurn)
			{
				this.vpdBurn();
			}
			else if (source == this.pbSuspend)
			{
				this.suspend("S", "0000"); //$NON-NLS-1$ //$NON-NLS-2$
			}
			else if (source == this.pbReprintNCMTag)
			{
				this.ncmreprint();
			}
			else if (source == this.pbSearchRem)
			{
				this.removepn(MFSLogPartDialog.LT_SEARCH_REMOVE);
			}
			else if (source == this.pbRemPart)
			{
				this.removepn(MFSLogPartDialog.LT_REMOVE);
			}
			else if (source == this.pbExtra)
			{
				this.extra_part();
			}
			else if (source == this.pbExtraClogPart)
			{
				this.extra_clog_part();
			}
			else if (source == this.pbEditComment)
			{
				this.editComment();
			}
			else if (source == this.pbEditLoc)
			{
				this.editLoc();
			}
			else if (source == this.pbDebugGolden)
			{
				this.extra_debugGolden_part();
			}
			else if (source == this.pbMovement)
			{
				this.editMovement();
			}
			else if (source == this.pbSkipPart)
			{
				this.skip_part();
			}
			else if (source == this.pbPartFunctions)
			{
				this.partFunctions();
			}
			else if (source == this.pbHide)
			{
				this.toggleInstructions();
			}
			else if (source == this.pbResetInterposer)
			{
				this.resetInterposer();
			}
			else if (source == this.pbRmvParts)
			{
				this.rwk_MultParts();
			}
			else if (source == this.pbDasdPersonalize)
			{
				this.dasdPersonalization();
			}
			else if (source == this.pbDasdUnpersonalize)
			{
				this.dasdUnPersonalization();
			}
			else if (source == this.pbVerify)
			{
				this.verifyPendingParts();
			}
			else if (source == this.pbNewCntr) // ~6A
			{
				newContainer(); // ~6A
			}
			else if (source == this.pbRemNonSer) // ~19A
			{
				removeNonSer(); // ~19A
			}
			else if (source == this.pbViewCollData)  // ~36A
			{
				viewDataCollection(); //~36A
			}
			else if (source == this.pbOnDemand) //~43A
			{
				onDemandReprint();
			}
		}
		//~14A Added try/catch
		//~16C Modified try/catch to remove PTR35927JM code
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
			removeMyListeners();
			addMyListeners();
		}
	}

	/**
	 * Invoked when a key is pressed.
	 * @param ke the <code>KeyEvent</code>
	 */
	public void keyPressed(KeyEvent ke)
	{
		final int keyCode = ke.getKeyCode();
		final Object source = ke.getSource();
		
		//Make sure that before we do much of anything,
		//we have a part highlighted in the active row.
		//Only exception is when they press F2 (with no shift).
		if ((keyCode == KeyEvent.VK_F2 && !ke.isShiftDown() && !ke.isControlDown()) == false)
		{
			if (keyCode != KeyEvent.VK_PAGE_UP && keyCode != KeyEvent.VK_PAGE_DOWN)
			{
				this.ensureActiveRowIndexIsHighlighted();
				if (this.fieldBlueRow != -1)
				{
					getRowVectorElementAt(this.fieldBlueRow).unDoChangeColor();
				}
			}
		}

		if (keyCode == KeyEvent.VK_ENTER)
		{
			if (source instanceof MFSMenuButton)
			{
				MFSMenuButton button = (MFSMenuButton) source;
				button.requestFocusInWindow();
				button.doClick();
			}
		}
		else if (keyCode == KeyEvent.VK_F1)
		{
			if (ke.isShiftDown())
			{
				this.pbSplit.requestFocusInWindow();
				this.pbSplit.doClick();
			}
			else if (ke.isControlDown())
			{
				this.pbNewCntr.requestFocusInWindow();
				this.pbNewCntr.doClick();
			}
			else
			{
				this.pbSrchLog.requestFocusInWindow();
				this.pbSrchLog.doClick();
			}
		}
		else if (keyCode == KeyEvent.VK_F2)
		{
			if (ke.isShiftDown())
			{
				this.pbShort.requestFocusInWindow();
				this.pbShort.doClick();
			}
			else if (ke.isControlDown()) //~19A
			{	
				this.pbRemNonSer.requestFocusInWindow();
				this.pbRemNonSer.doClick();
			}
			else
			{
				this.pbAdd.requestFocusInWindow();
				this.pbAdd.doClick();
			}
		}
		else if (keyCode == KeyEvent.VK_F3)
		{
			if (ke.isShiftDown())
			{
				if (this.pbUnburn.isEnabled())
				{
					this.pbUnburn.requestFocusInWindow();
					this.pbUnburn.doClick();
				}				
				else
				{
					this.pbDasdUnpersonalize.requestFocusInWindow();
					this.pbDasdUnpersonalize.doClick();
				}
			}
			else if (ke.isControlDown()) 					//~36A
			{	
				this.pbViewCollData.requestFocusInWindow();	//~36A
				this.pbViewCollData.doClick();				//~36A
			}
			else
			{
				this.pbEnd.requestFocusInWindow();
				this.pbEnd.doClick();
			}
		}
		else if (keyCode == KeyEvent.VK_F4)
		{
			if (ke.isShiftDown())
			{
				this.pbReprintNCMTag.requestFocusInWindow();
				this.pbReprintNCMTag.doClick();
			}
			else
			{
				this.pbAutoLog.requestFocusInWindow();
				this.pbAutoLog.doClick();
			}
		}
		else if (keyCode == KeyEvent.VK_F5)
		{
			if (ke.isShiftDown())
			{
				if (this.pbExtra.isEnabled())
				{
					this.pbExtra.requestFocusInWindow();
					this.pbExtra.doClick();
				}
				else
				{
					this.pbExtraClogPart.requestFocusInWindow();
					this.pbExtraClogPart.doClick();
				}
			}
			else
			{
				this.pbExtFunc.requestFocusInWindow();
				this.pbExtFunc.doClick();
			}
		}
		else if (keyCode == KeyEvent.VK_F6)
		{
			if (ke.isShiftDown())
			{
				if (this.pbDebugGolden.isEnabled())
				{
					this.pbDebugGolden.requestFocusInWindow();
					this.pbDebugGolden.doClick();
				}
				else
				{
					this.pbResetInterposer.requestFocusInWindow();
					this.pbResetInterposer.doClick();
				}
			}
			else
			{
				this.pbRework.requestFocusInWindow();
				this.pbRework.doClick();
			}
		}
		else if (keyCode == KeyEvent.VK_F7)
		{
			if (ke.isShiftDown())
			{
				if (this.pbSkipPart.isEnabled())
				{
					this.pbSkipPart.requestFocusInWindow();
					this.pbSkipPart.doClick();
				}
				else
				{
					this.pbVerify.requestFocusInWindow();
					this.pbVerify.doClick();
				}
			}
			else
			{
				this.pbSuspend.requestFocusInWindow();
				this.pbSuspend.doClick();
			}
		}
		else if (keyCode == KeyEvent.VK_F8)
		{
			if (ke.isShiftDown())
			{
				this.pbEditLoc.requestFocusInWindow();
				this.pbEditLoc.doClick();
			}
			else
			{
				this.pbViewIns.requestFocusInWindow();
				this.pbViewIns.doClick();
			}
		}
		else if (keyCode == KeyEvent.VK_F9)
		{
			if (ke.isShiftDown())
			{
				this.pbEditComment.requestFocusInWindow();
				this.pbEditComment.doClick();
			}
			else
			{
				if (this.pbCntr.isEnabled())
				{
					this.pbCntr.requestFocusInWindow();
					this.pbCntr.doClick();
				}
				else if (this.pbBurn.isEnabled())
				{
					this.pbBurn.requestFocusInWindow();
					this.pbBurn.doClick();
				}
				else if (this.pbDasdPersonalize.isEnabled())
				{
					this.pbDasdPersonalize.requestFocusInWindow();
					this.pbDasdPersonalize.doClick();
				}
				else if (this.pbSearchRem.isEnabled())
				{
					this.pbSearchRem.requestFocusInWindow();
					this.pbSearchRem.doClick();
				}
			}
		}
		else if (keyCode == KeyEvent.VK_F10)
		{
			if (ke.isShiftDown())
			{
				this.pbMovement.requestFocusInWindow();
				this.pbMovement.doClick();
			}
			else
			{
				this.pbMove.requestFocusInWindow();
				this.pbMove.doClick();
			}
		}
		else if (keyCode == KeyEvent.VK_F11)
		{
			if (ke.isShiftDown())
			{
				this.pbHide.requestFocusInWindow();
				this.pbHide.doClick();
			}
			else
			{
				this.pbRemPart.requestFocusInWindow();
				this.pbRemPart.doClick();
			}
		}
		else if (keyCode == KeyEvent.VK_F12)
		{
			if (ke.isShiftDown())
			{
				this.pbPartFunctions.requestFocusInWindow();
				this.pbPartFunctions.doClick();
			}
			else
			{
				if (this.pbCInC.isEnabled())
				{
					this.pbCInC.requestFocusInWindow();
					this.pbCInC.doClick();
				}
				else if (this.pbRmvParts.isEnabled())
				{
					this.pbRmvParts.requestFocusInWindow();
					this.pbRmvParts.doClick();
				}
			}
		}
		else if (keyCode == KeyEvent.VK_PAGE_DOWN)
		{
			ke.consume();
			this.spPartsInst.requestFocusInWindow();

			final int rowVectorSize = this.fieldRowVector.size();
			final boolean hideMode = isHideMode();

			//Blue Row
			if (this.fieldBlueRow != -1)
			{
				int index = this.fieldBlueRow;

				//if index >= rowVectorSize - 1
				//then the current PartInstructionJPanel is in the bottom row
				if (index >= rowVectorSize - 1)
				{
					getRowVectorElementAt(index).changeColor(
							MFSPartInstructionJPanel.BLUE_ROW_COLOR);
				}
				else
				{
					MFSPartInstructionJPanel pip = getRowVectorElementAt(index);
					pip.unDoChangeColor();

					index++;
					while (index < rowVectorSize)
					{
						pip = getRowVectorElementAt(index);

						if ((pip.getIsNonPartInstruction() && !hideMode)
								|| (hideMode && pip.getIsNonPartInstruction() 
										&& (pip.getInstructionRec().getInstrClass().equals("A") //$NON-NLS-1$ 
												|| pip.getInstructionRec().getInstrClass().equals("M")))) //$NON-NLS-1$
						{
							this.fieldBlueRow = index;
							pip.changeColor(MFSPartInstructionJPanel.BLUE_ROW_COLOR);
							this.spPartsInst.getVerticalScrollBar().setValue((int) pip.getBounds().getY());
							break;
						}
						else if (!pip.getIsNonPartInstruction())
						{
							this.fieldBlueRow = -1;
							this.spPartsInst.getVerticalScrollBar().setValue((int) pip.getBounds().getY());
							pip.getPNList().setSelectedIndex(0);
							pip.getPNList().requestFocusInWindow();
							break;
						}
						index++;
					}//end of while loop

					//try to unhighlight the highlighted parts
					JList activePipPNList = getRowVectorElementAt(this.fieldActiveRow).getPNList();
					if (this.fieldBlueRow != -1)
					{
						this.fieldActiveRowIndex = activePipPNList.getSelectedIndex();
						activePipPNList.clearSelection();
					}
					activePipPNList.requestFocusInWindow();
				}//end of not bottom
			}
			//no blue row is currently set --> nothing to undo
			else
			{
				int index = this.fieldActiveRow;
				//if index >= rowVectorSize - 1
				//then the current PartInstructionJPanel is in the bottom row
				//so there is nothing to do
				if (index < rowVectorSize - 1)
				{
					index++;
					while (index < rowVectorSize)
					{
						MFSPartInstructionJPanel pip = getRowVectorElementAt(index);
						if ((pip.getIsNonPartInstruction() && !hideMode)
								|| (hideMode && pip.getIsNonPartInstruction() 
										&& (pip.getInstructionRec().getInstrClass().equals("A") //$NON-NLS-1$ 
												|| pip.getInstructionRec().getInstrClass().equals("M")))) //$NON-NLS-1$
						{
							this.fieldBlueRow = index;
							pip.changeColor(MFSPartInstructionJPanel.BLUE_ROW_COLOR);
							this.spPartsInst.getVerticalScrollBar().setValue((int) pip.getBounds().getY());
							break;
						}
						else if (!pip.getIsNonPartInstruction())
						{
							this.spPartsInst.getVerticalScrollBar().setValue((int) pip.getBounds().getY());
							pip.getPNList().setSelectedIndex(0);
							pip.getPNList().requestFocusInWindow();
							break;
						}
						index++;
					}//end of while loop

					//try to unhighlight the highlighted parts
					JList activePipPNList = getRowVectorElementAt(this.fieldActiveRow).getPNList();
					if (this.fieldBlueRow != -1)
					{
						this.fieldActiveRowIndex = activePipPNList.getSelectedIndex();
						activePipPNList.clearSelection();
					}
					activePipPNList.requestFocusInWindow();
				}//end of not bottom of screen
			}
		}
		else if (keyCode == KeyEvent.VK_PAGE_UP)
		{
			ke.consume();
			this.spPartsInst.requestFocusInWindow();

			final boolean hideMode = isHideMode();

			if (this.fieldBlueRow != -1)
			{
				int index = this.fieldBlueRow;
				if (index <= 0)
				{
					getRowVectorElementAt(index).changeColor(MFSPartInstructionJPanel.BLUE_ROW_COLOR);
				}
				else
				{
					MFSPartInstructionJPanel pip = getRowVectorElementAt(index);
					pip.unDoChangeColor();

					index--;
					while (index >= 0)
					{
						pip = getRowVectorElementAt(index);
						if ((pip.getIsNonPartInstruction() && !hideMode)
								|| (hideMode && pip.getIsNonPartInstruction() 
										&& (pip.getInstructionRec().getInstrClass().equals("A") //$NON-NLS-1$ 
												|| pip.getInstructionRec().getInstrClass().equals("M")))) //$NON-NLS-1$

						{
							this.fieldBlueRow = index;
							this.spPartsInst.getVerticalScrollBar().setValue((int) pip.getBounds().getY());
							pip.changeColor(MFSPartInstructionJPanel.BLUE_ROW_COLOR);
							break;
						}
						else if (!pip.getIsNonPartInstruction())
						{
							this.fieldBlueRow = -1;
							this.spPartsInst.getVerticalScrollBar().setValue((int) pip.getBounds().getY());
							pip.getPNList().setSelectedIndex(0);
							pip.getPNList().requestFocusInWindow();
							break;
						}
						index--;
					}//end of loop

					//try to unhighlight the highlighted parts
					JList activePipPNList = getRowVectorElementAt(this.fieldActiveRow).getPNList();
					if (this.fieldBlueRow != -1)
					{
						this.fieldActiveRowIndex = activePipPNList.getSelectedIndex();
						activePipPNList.clearSelection();
					}
					activePipPNList.requestFocusInWindow();
				}//end of top at top
			}
			//no blue row set --> nothing to undo
			else
			{
				int index = this.fieldActiveRow;
				//if index <= 0
				//then the current PartInstructionJPanel is in the top row
				//so there is nothing to do
				if (index > 0)
				{
					index--;
					while (index >= 0)
					{
						MFSPartInstructionJPanel pip = getRowVectorElementAt(index);

						if ((pip.getIsNonPartInstruction() && !hideMode)
								|| (hideMode && pip.getIsNonPartInstruction() 
										&& (pip.getInstructionRec().getInstrClass().equals("A") //$NON-NLS-1$ 
												|| pip.getInstructionRec().getInstrClass().equals("M")))) //$NON-NLS-1$
						{
							this.fieldBlueRow = index;
							this.spPartsInst.getVerticalScrollBar().setValue((int) pip.getBounds().getY());
							pip.changeColor(MFSPartInstructionJPanel.BLUE_ROW_COLOR);
							break;
						}
						else if (!pip.getIsNonPartInstruction())
						{
							this.spPartsInst.getVerticalScrollBar().setValue((int) pip.getBounds().getY());
							pip.getPNList().setSelectedIndex(0);
							pip.getPNList().requestFocusInWindow();
							break;
						}
						index--;
					}//end of while loop

					//try to unhighlight the highlighted parts
					JList activePipPNList = getRowVectorElementAt(this.fieldActiveRow).getPNList();
					if (this.fieldBlueRow != -1)
					{
						this.fieldActiveRowIndex = activePipPNList.getSelectedIndex();
						activePipPNList.clearSelection();
					}
					activePipPNList.requestFocusInWindow();
				}//end of not at top of list
			}
		}
		else if (keyCode == KeyEvent.VK_DOWN)
		{
			JList tempList = getRowVectorElementAt(this.fieldActiveRow).getPNList();
			tempList.requestFocusInWindow();

			if (this.fieldBlueRow != -1)
			{
				getRowVectorElementAt(this.fieldBlueRow).unDoChangeColor();
				this.fieldBlueRow = -1;
				tempList.setSelectedIndex(0);
				ke.consume();
			}
			else if (tempList.getSelectedIndex() == tempList.getModel().getSize() - 1)
			{
				ke.consume();
				for (int i = this.fieldActiveRow + 1; i < this.fieldRowVector.size(); i++)
				{
					MFSPartInstructionJPanel pip = getRowVectorElementAt(i);
					if (pip.getIsNonPartInstruction() == false)
					{
						pip.getPNList().setSelectedIndex(0);
						pip.getPNList().requestFocusInWindow();
						pip.getPNList().ensureIndexIsVisible(0);
						break;
					}
				}
			}
		}
		else if (keyCode == KeyEvent.VK_UP)
		{
			JList tempList = getRowVectorElementAt(this.fieldActiveRow).getPNList();
			tempList.requestFocusInWindow();

			if (this.fieldBlueRow != -1)
			{
				getRowVectorElementAt(this.fieldBlueRow).unDoChangeColor();
				this.fieldBlueRow = -1;
				//highlight last part in part list and consume this event
				tempList.setSelectedIndex(tempList.getModel().getSize() - 1);
				ke.consume();
			}

			else if (tempList.getSelectedIndex() == 0)
			{
				for (int i = this.fieldActiveRow - 1; i >= 0; i--)
				{
					ke.consume();
					MFSPartInstructionJPanel pip = getRowVectorElementAt(i);
					if (pip.getIsNonPartInstruction() == false)
					{
						ListModel tmpLm = pip.getPNList().getModel();
						int selectedIndex = tmpLm.getSize() - 1;
						pip.getPNList().setSelectedIndex(selectedIndex);
						pip.getPNList().requestFocusInWindow();
						pip.getPNList().ensureIndexIsVisible(selectedIndex);
						break;
					}
				}
			}
		}
		//~43A - control down block
		else if(ke.isControlDown())
		{
			if(keyCode == KeyEvent.VK_L)
			{
				this.pbOnDemand.requestFocusInWindow();
				this.pbOnDemand.doClick();
			}
		}
	}

	/**
	 * Invoked when the mouse has been pressed on a <code>Component</code>.
	 * @param me the <code>MouseEvent</code>
	 */
	public void mouseClicked(MouseEvent me)
	{
		this.ensureActiveRowIndexIsHighlighted();
		if (this.fieldBlueRow != -1)
		{
			getRowVectorElementAt(this.fieldBlueRow).unDoChangeColor();
		}
		if (me.getClickCount() == 2)
		{
			JList tmpList = (JList) me.getSource();
			MFSComponentListModel tmpLm = (MFSComponentListModel) tmpList.getModel();
			MFSComponentRec cmp = tmpLm.getComponentRecAt(tmpList.getSelectedIndex());
			if (cmp.getIdsp().equals("R")) //$NON-NLS-1$
			{
				removepn(MFSLogPartDialog.LT_REMOVE);
			}
			else
			{
				addpn(MFSLogPartDialog.LT_ADD);
			}
		}
	}

	/**
	 * Invoked when the mouse has been pressed on a <code>Component</code>.
	 * Does nothing.
	 * @param me the <code>MouseEvent</code>
	 */
	public void mousePressed(MouseEvent me)
	{
		//Does nothing
	}

	/**
	 * Invoked when the mouse has been released on a <code>Component</code>.
	 * Does nothing.
	 * @param me the <code>MouseEvent</code>
	 */
	public void mouseReleased(MouseEvent me)
	{
		//Does nothing
	}

	/**
	 * Invoked when the mouse enters a <code>Component</code>. Does nothing.
	 * @param me the <code>MouseEvent</code>
	 */
	public void mouseEntered(MouseEvent me)
	{
		//Does nothing
	}

	/**
	 * Invoked when the mouse exits a <code>Component</code>. Does nothing.
	 * @param me the <code>MouseEvent</code>
	 */
	public void mouseExited(MouseEvent me)
	{
		//Does nothing
	}
}
