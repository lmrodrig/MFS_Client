/* @ Copyright IBM Corporation 2006, 2015. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2006-06-28   ~1 31801JM  R Prechel        -Used ActionJPanel.incrementValue method
 *                                           -Removed unused variables and getBuilderData
 * 2006-07-14   ~2 			D Fichtinger	 -Add new constructor and general cleanup
 * 2006-09-15	~3 35696DF	A Williams		 -Make memory improvement changes. 
 * 2006-09-04   ~4 35701DB  Luis M.          -Add a new Button to print 1S labels
 * 2007-02-26   ~5 37452MZ  Toribio H.       -Add print UID Label button.
 * 2007-03-13   ~6 34242JR  R Prechel        -Java 5 version
 * 2007-04-02   ~7 38166JM  R Prechel        -Add setLocationRelativeTo for dialogs
 * 2008-03-03   ~8 37616JL  D Pietrasik      -Add entity view function
 * 2008-06-20   ~9 38990JL  Santiago D       -New PARTPRINTING button
 * 2008-09-10  ~10 38133JL  Santiago D       -New partPrinting 11SPARTSEC label
 * 2009-03-25  ~11 44544GB  Santiago D       -Remove pbMifManual and pbMifPrln
 * 2010-01-18  ~12 43628JL  Santiago D       -New partPrinting LPROCLABEL label
 * 2010-02-18  ~13 46810JL  J Mastachi       -New WARRANTYCARDPRINT button
 * 2010-02-23  ~14 42558JL  D Mathre         -New On Demand Label button and printing
 * 2010-03-08  ~15 42558JL  Santiago SC      -call to getPlomConfigs inside of partPrinting()
 * 2010-05-17  ~16 46870JL  Edgar Mercado    -Modify processing for FRUNUMBSN and FRUNUMBNOSN labels
 *                                            to get CNAMETXT and CNAME from rules if defined.
 * 2010-10-01  ~17 48749JL  Santiago SC      -Hazmat labeling
 * 2011-11-09  ~18 588480   Santiago SC      -Grey Market, new LOGVENDORPARTS button
 * 2015-11-13  ~19 1425231  Miguel Rivas     -Add logic to also check for INDUSTRYIO label type.
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.panel;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Hashtable;

import mfsxml.MfsXMLDocument;
import mfsxml.MfsXMLParser;

import com.ibm.rchland.mfgapps.client.utils.messagebox.IGSMessageBox;
import com.ibm.rchland.mfgapps.client.utils.xml.IGSXMLTransaction;
import com.ibm.rchland.mfgapps.mfsclient.MFSFrame;
import com.ibm.rchland.mfgapps.mfsclient.MFSMenuButton;
import com.ibm.rchland.mfgapps.mfsclient.MFSMenuPanel;
import com.ibm.rchland.mfgapps.mfsclient.MFSPanel;
import com.ibm.rchland.mfgapps.mfsclient.barcode.MFSBCBarcodeDecoder;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSDe10Dialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSFruDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSGenericListDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSInputDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSKittingOpDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSManualMcpDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSMatpMcsnMmdlDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSMatpMmdlDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSOperationsDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSOrderNumberDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSRtvMctl2Dialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSRtvMctlDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSSelectLabelDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSSelectPartDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSStandAloneDblWideDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSStandAloneMSDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSUIDLabelDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSVendorSubPartsController;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSVinLabelDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSWarrantyCardModeDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSWorkUnitDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSWorkUnitPNSNDialog;
import com.ibm.rchland.mfgapps.mfsclient.struct.MFSHeaderRec;
import com.ibm.rchland.mfgapps.mfsclient.struct.MFSOnDemandKeyData;
import com.ibm.rchland.mfgapps.mfsclient.struct.MFSPlom;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSButtonIterator;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSHazmatLabeling;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSOnDemand;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSPrintingMethods;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSTriggerData;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFStfParser;
import com.ibm.rchland.mfgapps.mfscommon.MFSConfig;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSComm;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSFixedTransaction;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSTransaction;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSXmlTransaction;

/**
 * <code>MFSSelWrkExtFnctPanel</code> is the <code>MFSMenuPanel</code> for
 * extended functions not found on the primary <code>MFSSelectWorkPanel</code>.
 * @author The MFS Client Development Team
 */
public class MFSSelWrkExtFnctPanel
	extends MFSMenuPanel
{
	private static final long serialVersionUID = 1L;

	/** The default screen name of a <code>MFSSelWrkExtFnctPanel</code>. */
	public static final String SCREEN_NAME = "Select Work Extended Functions"; //$NON-NLS-1$

	//~11C - The shortcut key and image is free to use
	/** The MIF Manual (F2) , printF2.gif <code>MFSMenuButton</code>. */

	//~11C - The shortcut key and image is free to use
	/** The MIF Prodln (F3) , pringF3.gif <code>MFSMenuButton</code>. */

	/** The Kitting (F4) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbKitting = MFSMenuButton.createLargeButton("Kitting", //$NON-NLS-1$
			"printF4.gif", "Kitting", "BUTTON,SWEXTFUNC,KITTING"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

	/** The Rochester Kitting (F4) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbRochKit = MFSMenuButton.createLargeButton("Kitting", //$NON-NLS-1$
			"printF4.gif", "Kitting", "BUTTON,SWEXTFUNC,ROCHKIT"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

	/** The MCP Manual (F5) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbMcp = MFSMenuButton.createLargeButton("MCP Manual", //$NON-NLS-1$
			"printF5.gif", "MCP Manual", "BUTTON,SWEXTFUNC,MCPMANUAL"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

	/** The Fru (F6) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbPokFru = MFSMenuButton.createLargeButton("Fru", //$NON-NLS-1$
			"printF6.gif", "Fru", "BUTTON,SWEXTFUNC,POKFRU"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

	/** The Rtv Mctl (F7) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbRtvMctl = MFSMenuButton.createLargeButton("Rtv Mctl", //$NON-NLS-1$
			"mctlF7.gif", "Rtv Mctl", "BUTTON,SWEXTFUNC,RTVMCTL"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

	/** The Rtv Mctl 2 (F7) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbRtvMctl2 = MFSMenuButton.createLargeButton("Rtv Mctl", //$NON-NLS-1$
			"mctlF7.gif", "Rtv Mctl", "BUTTON,SWEXTFUNC,RTVMCTL2"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

	/** The Labels (F8) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbMsLabel = MFSMenuButton.createLargeButton("Labels", //$NON-NLS-1$
			"printF8.gif", "Labels", "BUTTON,SWEXTFUNC,MSLABELS"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

	/** The Print IPBC (F9) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbPrtIPBC = MFSMenuButton.createLargeButton("Prt IPBC", //$NON-NLS-1$
			"pipbcF9.gif", "Prt IPBC", "BUTTON,SWEXTFUNC,PRTIPBC"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

	/** The Double Wide (F10) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbDblWideLabel = MFSMenuButton.createLargeButton("Double Wide", //$NON-NLS-1$
			"dblwideF10.gif", "Double Wide", "BUTTON,SWEXTFUNC,DOUBLEWIDELABELS"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

	/** The VIN Labels (F11) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbVinLabel = MFSMenuButton.createLargeButton("VIN Labels", //$NON-NLS-1$
			"VINF11.gif", "VIN Labels", "BUTTON,SWEXTFUNC,VINLABELS"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

	/** The Quality Cert (F12) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbQualityCertificate = MFSMenuButton.createLargeButton(
			"Quality Cert", "qualityCertificateF12.gif", "Quality Certificate",  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
			"BUTTON,SWEXTFUNC,QLTYCERT"); //$NON-NLS-1$

	/** The View Ops (Shift + F2) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbViewOps = MFSMenuButton.createLargeButton("View Ops", //$NON-NLS-1$
			"Viewopf14.gif", "View Ops", "BUTTON,SWEXTFUNC,VIEWOPS"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

	/** The Deconfig Receipt (Shift + F3) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbDeconfigSetup = MFSMenuButton.createLargeButton(
			"Deconfig Receipt", "deconfigSetupF15.gif", "Deconfig Receipt",  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
			"BUTTON,SWEXTFUNC,DECONFIGSETUP"); //$NON-NLS-1$

	/** The WWID (Shift + F4) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbWwid = MFSMenuButton.createLargeButton("WWID", //$NON-NLS-1$
			"WWIDShftF4.gif", "WWID", "BUTTON,SWEXTFUNC,WWID"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

	//~4A New button
	/** The 1S Label (Shift + F5) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbPrint1S = MFSMenuButton.createLargeButton("1S Label", //$NON-NLS-1$
			"printF17.gif", "1S Label", "BUTTON,SELWORK,MCSNLBL"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	
	//~8A New button
	/** The Entity View(Shift + F6) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbEntityView = MFSMenuButton.createLargeButton("Entity View", //$NON-NLS-1$
			"mergeF18.gif", "Entity View", "BUTTON,SWEXTFUNC,ENTVIEW"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

	//~5A New button
	/** The UID Label (Shift + F7) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbUIDLabel = MFSMenuButton.createLargeButton("UID Label", //$NON-NLS-1$
			"printF19.gif", "UID Label", "BUTTON,SWEXTFUNC,UIDLABEL"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

	//~9A New button
	/** The PARTPRINTING (CTRL + F4) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbPartPrinting = MFSMenuButton.createLargeButton(
			"Part Printing", "printCTRLF4.gif", null,  //$NON-NLS-1$//$NON-NLS-2$
			"BUTTON,SWEXTFUNC,PARTPRINTING"); //$NON-NLS-1$
	
	//~13A New button
	/** The Warranty Card (CTRL + 2) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbWarrantyCard = MFSMenuButton.createLargeButton("Warranty Card", //$NON-NLS-1$
			"warranty.gif", "Warranty Card Printing", //$NON-NLS-1$ //$NON-NLS-2$
			"BUTTON,SWEXTFUNC,WARRANTYCARDPRINT"); //$NON-NLS-1$
	
	//~18A
	/** Vendor Parts Logging (CTRL + E) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbVendorPartsLogging = MFSMenuButton.createLargeButton(
			"Vendor PN Log", "vendorPartLog.gif", null,  //$NON-NLS-1$//$NON-NLS-2$
			"BUTTON,SWEXTFUNC,LOGVENDORPARTS"); //$NON-NLS-1$
	
	//~17A
	/** Hazmat Labeling Verification (CTRL + H) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbVfyHazmat = MFSMenuButton.createLargeButton(
			"Vfy Hazmat", "printCTRLH.gif", null,  //$NON-NLS-1$//$NON-NLS-2$
			"BUTTON,SWEXTFUNC,HAZMATVFY"); //$NON-NLS-1$
	
	//~14A
	/** The On Demand Label (CTRL + L) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbOnDemand = MFSMenuButton.createLargeButton(
			"On Demand", "printCTRLL.gif", null,  //$NON-NLS-1$//$NON-NLS-2$
			"BUTTON,SWEXTFUNC,ONDEMANDLBL"); //$NON-NLS-1$
	
	/** The current work unit control number. */
	private String fieldCurrMctl = ""; //$NON-NLS-1$

	/** The list of operations last displayed by {@link #showOps}. */
	private String fieldViewOpsData = ""; //$NON-NLS-1$

	/** The operations list index last displayed by {@link #showOps}. */
	private int fieldViewOpsIdx = 0;

	/**
	 * Constructs a new <code>MFSSelWrkExtFnctPanel</code>.
	 * @param parent the <code>MFSFrame</code> used to display this panel
	 * @param source the <code>MFSPanel</code> that caused this panel to be displayed
	 */
	public MFSSelWrkExtFnctPanel(MFSFrame parent, MFSPanel source)
	{
		super(parent, source, SCREEN_NAME, 6, 4);
		this.fieldButtonIterator = createMenuButtonIterator();
		createLayout();
		configureButtons();
		addMyListeners();
	}

	/**
	 * Invoked when an action occurs.
	 * @param ae the <code>ActionEvent</code>
	 */
	public void actionPerformed(ActionEvent ae)
	{
		final Object source = ae.getSource();

		if (source == this.pbKitting) //~11C
		{
			kitting();
		}
		else if (source == this.pbRochKit)
		{
			rochKit();
		}
		else if (source == this.pbMcp)
		{
			manualMcp();
		}
		else if (source == this.pbPokFru)
		{
			pokFru();
		}
		else if (source == this.pbRtvMctl)
		{
			rtvMctl();
		}
		else if (source == this.pbRtvMctl2)
		{
			rtvMctl2();
		}
		else if (source == this.pbMsLabel)
		{
			standAloneMS();
		}
		else if (source == this.pbPrtIPBC)
		{
			prtIPBC();
		}
		else if (source == this.pbDblWideLabel)
		{
			standAloneDoubleWide();
		}
		else if (source == this.pbVinLabel)
		{
			vinLabels();
		}
		else if (source == this.pbQualityCertificate)
		{
			qualityCertificate();
		}
		else if (source == this.pbViewOps)
		{
			viewOps();
		}
		else if (source == this.pbDeconfigSetup)
		{
			deconfigSetup();
		}
		else if (source == this.pbWwid)
		{
			wwid();
		}
		//~4A pbPrint1S and print1SLbl()
		else if (source == this.pbPrint1S)
		{
			print1SLbl();
		}
		//~5A pbUIDLabel and uidLabel()
		else if (source == this.pbUIDLabel)
		{
			uidLabel();
		}
		//~8A 
		else if (source == this.pbEntityView)
		{
			entityView();
		}
		//~9A
		else if (source == this.pbPartPrinting)
		{
			partPrinting();
		}
		//~13A
		else if (source == this.pbWarrantyCard)
		{
			warrantyCardPrinting();
		}
		//~14A
		else if (source == this.pbOnDemand)
		{
			onDemandReprint();
		}
		//~17A
		else if (source == this.pbVfyHazmat)
		{
			vfyHazmatLabeling();
		}
		//~18A
		else if (source == this.pbVendorPartsLogging)
		{
			vendorPartsLogging();
		}
	}

	//~6A new method
	/**
	 * Creates an <code>MFSButtonIterator</code> for this panel's option
	 * <code>JButton</code>.
	 * @return the <code>MFSButtonIterator</code>
	 */
	private MFSButtonIterator createMenuButtonIterator()
	{
		//The constructor param is the number of buttons
		MFSButtonIterator result = new MFSButtonIterator(22); /* ~11C ~13C ~14C ~17C ~18C*/
		result.add(this.pbKitting);
		result.add(this.pbRochKit);
		result.add(this.pbMcp);
		result.add(this.pbPokFru);
		result.add(this.pbRtvMctl);
		result.add(this.pbRtvMctl2);
		result.add(this.pbMsLabel);
		result.add(this.pbPrtIPBC);
		result.add(this.pbDblWideLabel);
		result.add(this.pbVinLabel);
		result.add(this.pbQualityCertificate);
		result.add(this.pbViewOps);
		result.add(this.pbDeconfigSetup);
		result.add(this.pbWwid);
		result.add(this.pbPrint1S);
		result.add(this.pbEntityView);
		result.add(this.pbUIDLabel);
		result.add(this.pbPartPrinting); // ~9A
		result.add(this.pbWarrantyCard); // ~13A
		result.add(this.pbOnDemand); //~14A
		result.add(this.pbVfyHazmat); //~17A
		result.add(this.pbVendorPartsLogging); //~18A
		return result;
	}

	/** Invoked when {@link #pbDeconfigSetup} is selected. */
	private void deconfigSetup()
	{
		try
		{
			removeMyListeners();

			MfsXMLDocument xml_data1 = new MfsXMLDocument("HANDLEDCFG"); //$NON-NLS-1$
			xml_data1.addOpenTag("DATA"); //$NON-NLS-1$
			xml_data1.addCompleteField("ACTN", "QUERY"); //$NON-NLS-1$ //$NON-NLS-2$
			xml_data1.addCloseTag("DATA"); //$NON-NLS-1$
			xml_data1.finalizeXML();

			MFSTransaction handledcfg = new MFSXmlTransaction(xml_data1.toString());
			handledcfg.setActionMessage("Retrieving List of Deconfig Entries, Please Wait..."); //$NON-NLS-1$
			MFSComm.getInstance().execute(handledcfg, this);

			if (handledcfg.getReturnCode() == 0)
			{
				MFSDe10Dialog myDe10JD = new MFSDe10Dialog(getParentFrame());
				myDe10JD.loadOrderList(handledcfg.getOutput());
				myDe10JD.setLocationRelativeTo(getParentFrame()); //~7A
				myDe10JD.setVisible(true);
			}
			else
			{
				IGSMessageBox.showOkMB(getParentFrame(), null, handledcfg.getErms(), null);
			}
		} //end of try block
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
		finally
		{
			addMyListeners();
			this.pbDeconfigSetup.requestFocusInWindow();
		}
	}

	//~8A New method
	/**
	 * Invoked when {@link #pbEntityView} is selected to display an
	 * <code>MFSEntityMergePanel</code> for a shipping entity, view only.
	 */
	private void entityView()
	{
		try
		{
			removeMyListeners();
			MFSEntityMergePanel emp = new MFSEntityMergePanel(getParentFrame(),
							this, MFSEntityMergePanel.VIEW_ONLY);
			if (emp.retrieveContainerInfo(this))
			{
				getParentFrame().displayMFSPanel(emp);
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
		else if (keyCode == KeyEvent.VK_F2)
		{
			if (ke.isShiftDown())
			{
				this.pbViewOps.requestFocusInWindow();
				this.pbViewOps.doClick();
			}
		}
		else if (keyCode == KeyEvent.VK_F3)
		{
			if (ke.isShiftDown())
			{
				this.pbDeconfigSetup.requestFocusInWindow();
				this.pbDeconfigSetup.doClick();
			}
		}
		else if (keyCode == KeyEvent.VK_F4)
		{
			if (ke.isShiftDown())
			{
				this.pbWwid.requestFocusInWindow();
				this.pbWwid.doClick();
			}
			else if (ke.isControlDown()) //~9A PartPrinting
			{
				this.pbPartPrinting.requestFocusInWindow();
				this.pbPartPrinting.doClick();
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
		else if (keyCode == KeyEvent.VK_F5)
		{
			if (ke.isShiftDown())
			{
				this.pbPrint1S.requestFocusInWindow();
				this.pbPrint1S.doClick();
			}
			else
			{
				this.pbMcp.requestFocusInWindow();
				this.pbMcp.doClick();
			}
		}
		else if (keyCode == KeyEvent.VK_F6)
		{
			if (ke.isShiftDown())
			{		/* ~8C */
				this.pbEntityView.requestFocusInWindow();
				this.pbEntityView.doClick();
			}
			else
			{
				this.pbPokFru.requestFocusInWindow();
				this.pbPokFru.doClick();
			}	
		}
		else if (keyCode == KeyEvent.VK_F7)
		{
			if (ke.isShiftDown()) //~8A
			{
				this.pbUIDLabel.requestFocusInWindow();
				this.pbUIDLabel.doClick();
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
		else if (keyCode == KeyEvent.VK_F8)
		{
			this.pbMsLabel.requestFocusInWindow();
			this.pbMsLabel.doClick();			
		}
		else if (keyCode == KeyEvent.VK_F9)
		{
			this.pbPrtIPBC.requestFocusInWindow();
			this.pbPrtIPBC.doClick();
		}
		else if (keyCode == KeyEvent.VK_F10)
		{
			this.pbDblWideLabel.requestFocusInWindow();
			this.pbDblWideLabel.doClick();
		}
		else if (keyCode == KeyEvent.VK_F11)
		{
			this.pbVinLabel.requestFocusInWindow();
			this.pbVinLabel.doClick();
		}
		else if (keyCode == KeyEvent.VK_F12)
		{
			this.pbQualityCertificate.requestFocusInWindow();
			this.pbQualityCertificate.doClick();
		}
		// ~13A BEGIN
		else if (keyCode == KeyEvent.VK_2)
		{
			if (ke.isControlDown())
			{
				this.pbWarrantyCard.requestFocusInWindow();
				this.pbWarrantyCard.doClick();
			}
		} // ~13A END
		/* Control Down section */
		else if (ke.isControlDown()) //~15A
		{
			if (keyCode == KeyEvent.VK_L) //~15A
			{
				this.pbOnDemand.requestFocusInWindow();
				this.pbOnDemand.doClick();
			}
			else if (keyCode == KeyEvent.VK_H) //~17A
			{
				this.pbVfyHazmat.requestFocusInWindow();
				this.pbVfyHazmat.doClick();
			}
			else if (keyCode == KeyEvent.VK_E) //~18A
			{
				this.pbVendorPartsLogging.requestFocusInWindow();
				this.pbVendorPartsLogging.doClick();
			}
		}
	}

	/** Invoked when {@link #pbKitting} is selected. */
	private void kitting()
	{
		try
		{
			removeMyListeners();

			MFSWorkUnitDialog myWrkUnitD = new MFSWorkUnitDialog(getParentFrame());
			myWrkUnitD.setLocationRelativeTo(getParentFrame()); //~7A
			myWrkUnitD.setVisible(true);

			if (myWrkUnitD.getPressedEnter())
			{
				String data = "RTV_MO    " + myWrkUnitD.getMctl(); //$NON-NLS-1$
				MFSTransaction rtv_mo = new MFSFixedTransaction(data);
				rtv_mo.setActionMessage("Retrieving List of Operations, Please Wait..."); //$NON-NLS-1$
				MFSComm.getInstance().execute(rtv_mo, this);

				if (rtv_mo.getReturnCode() != 0)
				{
					IGSMessageBox.showOkMB(getParentFrame(), null, rtv_mo.getErms(), null);
				}
				else
				{
					MFSKittingOpDialog kittOpJD = new MFSKittingOpDialog(getParentFrame());
					kittOpJD.loadKittOpListModel(rtv_mo.getOutput());
					kittOpJD.setLocationRelativeTo(getParentFrame()); //~7A
					kittOpJD.setVisible(true);

					if (kittOpJD.getPressedPrint())
					{
						final MFSConfig config = MFSConfig.getInstance();
						int index = 0;
						while (index < kittOpJD.getKittOpListModelSize())
						{
							String listData = (String) kittOpJD.getKittOpListModelElementAt(index);
							String type = listData.substring(13, 14);
							if (!type.equals(" ")) //$NON-NLS-1$
							{
								getParentFrame().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
								String nmbr = listData.substring(0, 4);
								if (config.containsConfigEntry("POKKIT")) //$NON-NLS-1$
								{
									MFSPrintingMethods.pokkit(myWrkUnitD.getMctl(), nmbr,
											type, config.get8CharCellType(), 1, getParentFrame());
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

	/** Invoked when {@link #pbMcp} is selected. */
	private void manualMcp()
	{
		try
		{
			removeMyListeners();
			MFSManualMcpDialog myManualMcpD = new MFSManualMcpDialog(getParentFrame());
			myManualMcpD.setLocationRelativeTo(getParentFrame()); //~7A
			myManualMcpD.setVisible(true);
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

	//~14A //~15C
	/** Invoked when {@link #pbOnDemand} is selected. */
	private void onDemandReprint()
	{
		try
		{
			this.removeMyListeners();
			
			// Create key data for label printing
			MFSOnDemandKeyData odKeyData = new MFSOnDemandKeyData();
			odKeyData.setTriggerSource("SWEXTFUNC"); //$NON-NLS-1$
			odKeyData.setTriggerKey("*NONE"); //$NON-NLS-1$
			
			/* WARNING!! currently there is no dataSource at this point. Input field of types
			 * R and A will fail if configured, probably throwing a null pointer exception.
			 */
			
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

	// ~9A PartPrinting
	/** Invoked when {@link #pbPartPrinting} is selected. */
	private void partPrinting()
	{
		final String triggerSource = "SWEXTFUNC"; //~15A //$NON-NLS-1$
		final String triggerKey = "PARTSELECT"; //~15A //$NON-NLS-1$
		
		try
		{
			this.removeMyListeners(); // ~10A

			
			MFSSelectLabelDialog myLabelTypeDialog = new MFSSelectLabelDialog(
					getParentFrame(), "Plant of Manufacture and Label Type", //$NON-NLS-1$
					"Select the PLOM and label type to print", "PLOM : "); //$NON-NLS-1$ //$NON-NLS-2$
			
			/* Get plom's here*/
			ArrayList<String> plomConfigs = MFSConfig.getInstance().getPlomConfigs(); //~15C
			
			/* If found PLOM configs */
			if(null != plomConfigs) //~15C
			{	
				/* Process plom's here*/
				for(String plomConfig : plomConfigs) //~15C
				{
					MFSPlom plomObject = new MFSPlom(plomConfig);
					myLabelTypeDialog.addComboItem(plomObject.getPlom() + " " + plomObject.getPlnt()); //$NON-NLS-1$
				}				
				
				/* ~15A - Get labels for part printing */
				MFSTriggerData triggerData = MFSTriggerData.getInstance();
				ArrayList<String> validLabels = triggerData.rtvLabels(triggerSource, triggerKey);
								
				if(null != validLabels)
				{
					/* Initialize the default list (visible) in the SelectLabel dialog */
					myLabelTypeDialog.initializeDefaultList();
					
					/* Populate the labels list in the SelectLabel dialog */					
					myLabelTypeDialog.addListItems(validLabels.toArray()); //~15A
										
					myLabelTypeDialog.sortListAlphaNumerically(); //~15A
					
					myLabelTypeDialog.setDefaultSelection("FIRST"); //$NON-NLS-1$
					
					/* Display labelType Dialog */
					myLabelTypeDialog.setSize(420, 384);		
					myLabelTypeDialog.setLocationRelativeTo(getParentFrame()); 
					myLabelTypeDialog.setMultipleSelection(false);
					myLabelTypeDialog.setVisible(true);
		
					if (myLabelTypeDialog.getProceedSelected())
					{
						String plom = myLabelTypeDialog.getSelectedComboOption();
						plom = plom.substring(0,3); //Just get the plom not the plant name.
						
						String labelType = myLabelTypeDialog.getSelectedListOption();
						/*
						String triggerSource = "SELWRKEXTFNC";
						String triggerKey = "PARTSELECT"; // ~10C
						*/
						String labelData = ""; // ~10A //$NON-NLS-1$
						
						IGSXMLTransaction rtvLblTrig = new IGSXMLTransaction("RTVLBLTRIG");  //$NON-NLS-1$
						rtvLblTrig.setActionMessage("Retrieving Part and Label Part Numbers..."); //$NON-NLS-1$
						rtvLblTrig.startDocument();
						rtvLblTrig.addElement("LBLT", labelType);  //$NON-NLS-1$
						rtvLblTrig.addElement("LBTS", triggerSource); //$NON-NLS-1$
						rtvLblTrig.addElement("LBTK", triggerKey); //$NON-NLS-1$
						rtvLblTrig.addElement("PLOM", plom); //$NON-NLS-1$
						rtvLblTrig.endDocument();
			
						MFSComm.getInstance().execute(rtvLblTrig, this);
						
						if (rtvLblTrig.getReturnCode() == 0)
						{	
							// partNumber = label Trigger Value (LBTV)
							String triggerValue = ""; //$NON-NLS-1$
							
							MFSSelectPartDialog myPartDialog = new MFSSelectPartDialog(
									getParentFrame(), labelType, "Select the Part Number to print", //$NON-NLS-1$
									"Print", "Done"); //$NON-NLS-1$ //$NON-NLS-2$
							myPartDialog.initializeDefaultList();					
							
							while ((triggerValue = rtvLblTrig.getNextElement("LBTV")) != null) //$NON-NLS-1$
							{	
								myPartDialog.addListItem(triggerValue);
							}
							
							myPartDialog.sortListAlphaNumerically();
							myPartDialog.setSizeLarge();	
							myPartDialog.setDefaultSelection("FIRST"); //$NON-NLS-1$
		
							do
							{
								myPartDialog.setLocationRelativeTo(getParentFrame()); 
								myPartDialog.initializeFields();	
								myPartDialog.setVisible(true);
								
								if (myPartDialog.getProceedSelected())
								{
									if(myPartDialog.isValidNumInput(1, 99)) // 1 to 99 labels
									{
										/* Comment -> triggerValue = partNumber */
										triggerValue = myPartDialog.getSelectedListOption();
										
										if(labelType.equals("11SPARTSEC") || labelType.equals("LPROCLABEL") || //~10A,~12C //$NON-NLS-1$ //$NON-NLS-2$
										    labelType.equals("INDUSTRYIO") ) //~19C
										{
											labelData = rtvVendor(triggerValue);
											
											if(labelData != null)
											{
												MFSPrintingMethods.printlabel(labelType, triggerSource, 
													"<"+triggerKey+">"+triggerValue+"</"+triggerKey+">", labelData, plom, //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
													Integer.parseInt(myPartDialog.getQuantity()), getParentFrame());											
											}
										}
										else
										{
											MFSPrintingMethods.printlabel(labelType, triggerSource, 
												"<"+triggerKey+">"+triggerValue+"</"+triggerKey+">", "", plom, //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
												Integer.parseInt(myPartDialog.getQuantity()), getParentFrame());
										}
									}
									else
									{
										String msg = "Error: The Quantity must be a valid number between 1 and 99"; //$NON-NLS-1$
										IGSMessageBox.showOkMB(this, null, msg, null);									
									}
								}
							}
							while(!myPartDialog.getCancelSelected());						
						}
						else
						{
							String title = "Error Retrieving Part Numbers"; //$NON-NLS-1$
							IGSMessageBox.showOkMB(this, title, rtvLblTrig.getErms(), null);
						}
					} // end if myLabelTypeDialog proceed selected
				}
				else
				{
					IGSMessageBox.showOkMB(this, null, "No Labels were found. (RTVLBLTRIG)", null);	 //$NON-NLS-1$
				}
			} // end if PLOM's found
			else
			{
				String title = "Error Retrieving PLOM configs"; //$NON-NLS-1$
				IGSMessageBox.showOkMB(this, title, "No PLOM configurations were loaded. (RTV_PLOM)", null);				 //$NON-NLS-1$
			}
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
		finally
		{
			this.addMyListeners(); // ~10A
		}
	}

	/** Invoked when {@link #pbPokFru} is selected. */
	private void pokFru()
	{
		try
		{
			removeMyListeners();

			MFSFruDialog myFruD = new MFSFruDialog(getParentFrame());
			myFruD.setLocationRelativeTo(getParentFrame()); //~7A
			myFruD.setVisible(true);

			if (myFruD.getPressedPrint())
			{
				String pn = myFruD.getPNInputText();
				String sn = myFruD.getSNInputText();
				String cooc = myFruD.getCntyInputText();
				String countryName = myFruD.getCountryName();                //~16C
				String cnametxt = myFruD.getCNametxt();                      //~16A

				if (!sn.equals("")) //$NON-NLS-1$
				{
					MFSPrintingMethods.frunumbsn(pn, "J", sn, cooc, countryName,cnametxt, 1, getParentFrame()); //$NON-NLS-1$  ~16C
				}
				else
				{
					MFSPrintingMethods.frunumbnosn(pn, "J", cooc, countryName,cnametxt,1, getParentFrame()); //$NON-NLS-1$     ~16C
				}
			} // end pressed print
		} // end try
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
		finally
		{
			addMyListeners();
		}
	}

	//~4A New method
	/** Invoked when {@link #pbPrint1S} is selected. */
	private void print1SLbl()
	{
		try
		{
			int qty = 1;// Default should always be 1

			removeMyListeners();

			String data = "FIND_RU10 " + "MATPMDLSN"; //$NON-NLS-1$ //$NON-NLS-2$
			MFSTransaction find_ru10 = new MFSFixedTransaction(data);
			find_ru10.setActionMessage("Retrieving List of Machine Types && Models, Please Wait..."); //$NON-NLS-1$
			MFSComm.getInstance().execute(find_ru10, this);

			if (find_ru10.getReturnCode() == 0)
			{
				MFSMatpMmdlDialog matpmdlJD = new MFSMatpMmdlDialog(getParentFrame(), find_ru10.getOutput());
				matpmdlJD.setLocationRelativeTo(getParentFrame()); //~7A
				matpmdlJD.setVisible(true);
				
				if (matpmdlJD.getPressedPrint())
				{
					MFSPrintingMethods.matpMdlSn(matpmdlJD.getSelectedMatp(), 
							matpmdlJD.getSelectedMdl(), matpmdlJD.getQuantityText(),
							qty, getParentFrame());
				}
			}
			else
			{
				IGSMessageBox.showOkMB(getParentFrame(), null, find_ru10.getErms(), null);
			}
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
		finally
		{
			addMyListeners();
			this.pbPrint1S.requestFocusInWindow();
		}
	}

	/** Invoked when {@link #pbPrtIPBC} is selected. */
	private void prtIPBC()
	{
		try
		{
			MFSWorkUnitPNSNDialog myWrkUnitD = new MFSWorkUnitPNSNDialog(getParentFrame());
			myWrkUnitD.setLocationRelativeTo(getParentFrame()); //~7A
			myWrkUnitD.setVisible(true);

			if (myWrkUnitD.getPressedEnter())
			{
				MFSPrintingMethods.ipreport(myWrkUnitD.getMctl().trim(), 1, getParentFrame());
			}
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
	}

	/** Invoked when {@link #pbQualityCertificate} is selected. */
	private void qualityCertificate()
	{
		MFSOrderNumberDialog myOrderNumberJDialog = new MFSOrderNumberDialog(getParentFrame());
		myOrderNumberJDialog.setLocationRelativeTo(getParentFrame()); //~7A
		myOrderNumberJDialog.setVisible(true);

		if (myOrderNumberJDialog.getPressedEnter())
		{
			String orderNumber = myOrderNumberJDialog.getOrderNumber();
			
			if (!orderNumber.equals("")) //$NON-NLS-1$
			{
				getActionIndicator().startAction("Printing Quality Certificate, Please Wait..."); //$NON-NLS-1$
				this.update(getGraphics());

				MFSPrintingMethods.qualityCertificate(orderNumber, 1, getParentFrame());

				//~1C Changed while loop to use incrementValue
				while (getActionIndicator().incrementValue(5));
				getActionIndicator().stopAction();
				this.update(this.getGraphics()); //~1A
			}
		}
	}

	//~6A New method
	/** Invokes <code>showOps(fieldViewOpsData, fieldViewOpsIdx);</code>. */
	public void reshowOps()
	{
		showOps(this.fieldViewOpsData, this.fieldViewOpsIdx);
	}	
	
	
	/** Invoked when {@link #pbRochKit} is selected. */
	private void rochKit()
	{
		try
		{
			int rc = 0;
			String errorString = ""; //$NON-NLS-1$

			removeMyListeners();

			MFSWorkUnitDialog myWrkUnitD = new MFSWorkUnitDialog(getParentFrame());
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
					MFSKittingOpDialog kittOpJD = new MFSKittingOpDialog(getParentFrame());
					kittOpJD.loadKittOpListModel(rtv_mo.getOutput());
					kittOpJD.setLocationRelativeTo(getParentFrame()); //~7A
					kittOpJD.setVisible(true);

					if (kittOpJD.getPressedPrint())
					{
						int index = 0;
						while (rc == 0 && index < kittOpJD.getKittOpListModelSize())
						{
							String listData = (String) kittOpJD.getKittOpListModelElementAt(index);
							String type = listData.substring(13, 14);
							if (!type.equals(" ")) //$NON-NLS-1$
							{
								getParentFrame().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
								String nmbr = listData.substring(0, 4);
								MFSPrintingMethods.rchkit(myWrkUnitD.getMctl(), nmbr,
										type, 1, getParentFrame());
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

	/** Invoked when {@link #pbRtvMctl} is selected. */
	private void rtvMctl()
	{
		removeMyListeners();
		MFSRtvMctlDialog myRtvMctlD = new MFSRtvMctlDialog(getParentFrame());
		myRtvMctlD.setLocationRelativeTo(getParentFrame()); //~7A
		myRtvMctlD.setVisible(true);
		addMyListeners();
		this.pbRtvMctl.requestFocusInWindow();
	}

	/** Invoked when {@link #pbRtvMctl2} is selected. */
	private void rtvMctl2()
	{
		removeMyListeners();
		MFSRtvMctl2Dialog myRtvMctl2JD = new MFSRtvMctl2Dialog(getParentFrame());
		myRtvMctl2JD.setLocationRelativeTo(getParentFrame()); //~7A
		myRtvMctl2JD.setVisible(true);
		addMyListeners();
		this.pbRtvMctl2.requestFocusInWindow();
	}
	
	// ~10A
	/**
	 * Retrieves de Vendor info releated to a partNumber.
	 * @param partNumber = the part number related to a Vendor
	 * @return vendorData = related to the given partNumber
	 */
	private String rtvVendor(String partNumber)
	{
		String vendorData = null;
		
		try
		{
			StringBuffer sbVendorData = new StringBuffer(""); //$NON-NLS-1$
			
			IGSXMLTransaction rtv_vendor = new IGSXMLTransaction("RTV_VENDOR");  //$NON-NLS-1$
			rtv_vendor.setActionMessage("Retrieving Vendors..."); //$NON-NLS-1$
			rtv_vendor.startDocument();
			rtv_vendor.addElement("PN", partNumber);  //$NON-NLS-1$
			rtv_vendor.endDocument();	
			
			MFSComm.getInstance().execute(rtv_vendor, this);
			
			if (rtv_vendor.getReturnCode() == 0)
			{	
				String vendorRec = ""; //$NON-NLS-1$
				
				Hashtable<String, String> vendorTable = new Hashtable<String, String>(); //~17C
				
				MFSGenericListDialog myVendorDialog = new MFSGenericListDialog(
						getParentFrame(), "Vendor Selection", //$NON-NLS-1$
						"Vendor Name              Header EC Code          EC Level          " //$NON-NLS-1$
						, "Print", "Cancel"); //$NON-NLS-1$ //$NON-NLS-2$
				
				myVendorDialog.initializeDefaultList();			
				
				MfsXMLParser recParser = new MfsXMLParser();
				
				while ((vendorRec = rtv_vendor.getNextElement("REC")) != null) //$NON-NLS-1$
				{		
					recParser.setUnparsedXML(vendorRec);			
					recParser.setIndex(0);
					
					sbVendorData.delete(0, sbVendorData.length());				
					sbVendorData.append(recParser.getNextField("VEND")); //$NON-NLS-1$
					sbVendorData.append("     "); //$NON-NLS-1$
					sbVendorData.append(recParser.getNextField("HCDE")); //$NON-NLS-1$
					sbVendorData.append("          "); //$NON-NLS-1$
					sbVendorData.append(recParser.getNextField("HWEC")); //$NON-NLS-1$
					
					myVendorDialog.addListItem(sbVendorData.toString());
					
					vendorTable.put(sbVendorData.toString(), vendorRec);
				}				
				
				if(vendorTable.size() == 1)
				{
					vendorData = (String) vendorTable.get(sbVendorData.toString());
				}
				else
				{
					myVendorDialog.setSizeLarge();	
					myVendorDialog.sortListAlphaNumerically();
					myVendorDialog.setDefaultSelection("FIRST");					 //$NON-NLS-1$
					myVendorDialog.setLocationRelativeTo(getParentFrame()); 	
					myVendorDialog.setVisible(true);	
				
					if(myVendorDialog.getProceedSelected())
					{
						vendorData = (String) vendorTable.get(myVendorDialog.getSelectedListOption());	
					}
				}
				
				// Trim all spaces
				if(vendorData != null)
				{
					recParser.setUnparsedXML(vendorData);			
					recParser.setIndex(0);					
					
					sbVendorData.delete(0, sbVendorData.length());
					sbVendorData.append("<VEND>"); //$NON-NLS-1$
					sbVendorData.append(recParser.getNextField("VEND").trim()); //$NON-NLS-1$
					sbVendorData.append("</VEND>"); //$NON-NLS-1$
					sbVendorData.append("<HCDE>"); //$NON-NLS-1$
					sbVendorData.append(recParser.getNextField("HCDE").trim()); //$NON-NLS-1$
					sbVendorData.append("</HCDE>"); //$NON-NLS-1$
					sbVendorData.append("<HWEC>"); //$NON-NLS-1$
					sbVendorData.append(recParser.getNextField("HWEC").trim()); //$NON-NLS-1$
					sbVendorData.append("</HWEC>");					 //$NON-NLS-1$
					
					vendorData = sbVendorData.toString();
				}
			}
			else
			{
				IGSMessageBox.showOkMB(getParentFrame(), null, rtv_vendor.getErms(), null);	
			}
		}
		catch(Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
		
		return vendorData;
	}
	
	/**
	 * Shows the instructions for an operation.
	 * @param data the list of operations (RTV_OPS transaction output)
	 * @param idx the selected list index
	 */
	private void showOps(String data, int idx)
	{
		try
		{
			MFSOperationsDialog operJD = new MFSOperationsDialog(getParentFrame());
			operJD.loadOperListModel(data, "    "); //$NON-NLS-1$
			if (operJD.getListSize() == 0)
			{
				String message = "No Operations Found For Work Unit " + this.fieldCurrMctl; //$NON-NLS-1$
				IGSMessageBox.showOkMB(getParentFrame(), null, message, null);
			}
			else
			{
				operJD.setSelectedIndex(idx);
				operJD.setLocationRelativeTo(getParentFrame()); //~7A
				operJD.setVisible(true);

				if (operJD.getProceedSelected())
				{
					this.fieldViewOpsData = data;
					this.fieldViewOpsIdx = operJD.getSelectedIndex();

					StringBuffer data1 = new StringBuffer();
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

						MFSConfig config = MFSConfig.getInstance();

						int rc = 0;
						MfsXMLParser xmlParser = new MfsXMLParser();
						if (!config.containsConfigEntry("EFFICIENCYON*DUMBCLIENT")) //$NON-NLS-1$
						{
							/* Try to get the instructions for this work unit/operation. */
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
								xmlParser = new MfsXMLParser(rtv_instr.getOutput());
							}
							//Bad return code from rtv_instr
							else
							{
								IGSMessageBox.showOkMB(getParentFrame(), null, rtv_instr.getErms(), null);
								this.pbViewOps.requestFocusInWindow();
							}
						}
						
						if (rc == 0)
						{
							viewOpsPanel.loadInstructions(xmlParser);
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

	/** Invoked when {@link #pbDblWideLabel} is selected. */
	private void standAloneDoubleWide()
	{
		try
		{
			removeMyListeners();
			MFSStandAloneDblWideDialog myDWD = new MFSStandAloneDblWideDialog(getParentFrame());
			myDWD.setLocationRelativeTo(getParentFrame()); //~7A
			myDWD.setVisible(true);
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
	
	/** Invoked when {@link #pbMsLabel} is selected. */
	private void standAloneMS()
	{
		try
		{
			removeMyListeners();
			MFSStandAloneMSDialog myMSD = new MFSStandAloneMSDialog(getParentFrame());
			myMSD.setLocationRelativeTo(getParentFrame()); //~7A
			myMSD.setVisible(true);
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
	
	//~5A New method
	/** Invoked when {@link #pbUIDLabel} is selected. */
	private void uidLabel() 
	{
		try
		{
			removeMyListeners();
			MFSUIDLabelDialog dialog = new MFSUIDLabelDialog(getParentFrame());
			dialog.setLocationRelativeTo(getParentFrame()); //~7A
			dialog.setVisible(true);
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
	
	//~18A
	/** Invoked when {@link #pbVendorPartsLogging} is selected. */
	private void vendorPartsLogging()
	{
		try
		{
			this.removeMyListeners();
			
			MFSVendorSubPartsController vspLogging = new MFSVendorSubPartsController(this.getParentFrame(), true);
			vspLogging.displayVendorSubassemblyPartsDialog("BUTTON,SWEXTFUNC,LOGVENDORPARTS", null); //$NON-NLS-1$
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

	//~17A
	/** Invoked when {@link #pbVfyHazmat} is selected. */
	private void vfyHazmatLabeling()
	{
		try
		{
			this.removeMyListeners();
			
			// Get the MCTL or Container
			MFSBCBarcodeDecoder barcode = MFSBCBarcodeDecoder.getInstance();
			barcode.setMyBCIndicatorValue(null);
			
			MFSInputDialog inputDialog = new MFSInputDialog(getParentFrame(), "Hazmat Labeling",  //$NON-NLS-1$
															"Input Container", barcode); //$NON-NLS-1$
			inputDialog.setVisible(true);
			
			if(inputDialog.getProceedSelected())
			{
				boolean isCntr = false;
				String inputValue = null;
					
				// 1st Try to get the MCTL
				inputValue = barcode.getBCMyPartObject().getWU();
				
				if(null == inputValue || inputValue.trim().equals("")) //$NON-NLS-1$
				{
					// 2nd Try to get the CNTR
					MFStfParser parser = new MFStfParser.MFStfCntrParser(this);					
					inputValue = parser.recvInput(inputDialog.getInputValue());
										
					isCntr = true;
				}
				
				// Hazmat Labeling checkup			
				String mode = ""; // display mode				 //$NON-NLS-1$
				MFSHazmatLabeling hazmatLbl = new MFSHazmatLabeling(getParentFrame(), this);
				hazmatLbl.setVerificationEnabled(false);
					
				// FFBM Subassembly
				if(!isCntr)
				{
					hazmatLbl.hazmatLabelingForFFBM(inputValue, mode);
				}
				else
				{
					hazmatLbl.hazmatLabeling(inputValue, mode);
				}
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
	
	/** Invoked when {@link #pbViewOps} is selected. */
	private void viewOps()
	{
		try
		{
			removeMyListeners();

			MFSWorkUnitPNSNDialog myWrkUnitPNSND = new MFSWorkUnitPNSNDialog(getParentFrame());
			myWrkUnitPNSND.setLocationRelativeTo(getParentFrame()); //~7A
			myWrkUnitPNSND.setVisible(true);

			if (myWrkUnitPNSND.getPressedEnter())
			{
				this.fieldCurrMctl = (myWrkUnitPNSND.getMctl());
				MfsXMLDocument xml_data1 = new MfsXMLDocument("RTV_OPS"); //$NON-NLS-1$
				xml_data1.addOpenTag("DATA"); //$NON-NLS-1$
				xml_data1.addCompleteField("MCTL", myWrkUnitPNSND.getMctl()); //$NON-NLS-1$
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

	/** Invoked when {@link #pbVinLabel} is selected. */
	private void vinLabels()
	{
		try
		{
			removeMyListeners();
			MFSVinLabelDialog myVinJD = new MFSVinLabelDialog(getParentFrame());
			myVinJD.setLocationRelativeTo(getParentFrame()); //~7A
			myVinJD.setVisible(true);
		} // end try
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
		finally
		{
			addMyListeners();
		}
	}

	//~13A Warranty Card Printing
	/** Invoked when {@link #pbWarrantyCard} is selected. */
	private void warrantyCardPrinting()
	{
		try
		{
			removeMyListeners();
			MFSWarrantyCardModeDialog myWCMD = new MFSWarrantyCardModeDialog(getParentFrame());
			myWCMD.setLocationRelativeTo(getParentFrame());
			myWCMD.setVisible(true);
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
		finally
		{
			addMyListeners();
			this.pbWarrantyCard.requestFocusInWindow();
		}
	}

	/** Invoked when {@link #pbWwid} is selected. */
	private void wwid()
	{
		try
		{
			removeMyListeners();
			MFSMatpMcsnMmdlDialog myMMMD = new MFSMatpMcsnMmdlDialog(getParentFrame());
			myMMMD.setLocationRelativeTo(getParentFrame()); //~7A
			myMMMD.setVisible(true);
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
		finally
		{
			addMyListeners();
			this.pbWwid.requestFocusInWindow();
		}
	}
}
