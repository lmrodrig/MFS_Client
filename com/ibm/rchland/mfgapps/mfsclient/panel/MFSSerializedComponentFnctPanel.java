/* © Copyright IBM Corporation 2002, 2007. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2007-02-16      34242JR  R Prechel        -Java 5 version
 * 2007-04-02   ~1 38166JM  R Prechel        -Add setLocationRelativeTo for dialogs
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.panel;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.DefaultListModel;
import javax.swing.JButton;

import mfsxml.MISSING_XML_TAG_EXCEPTION;
import mfsxml.MfsXMLDocument;
import mfsxml.MfsXMLParser;

import com.ibm.rchland.mfgapps.client.utils.messagebox.IGSMessageBox;
import com.ibm.rchland.mfgapps.mfsclient.MFSFrame;
import com.ibm.rchland.mfgapps.mfsclient.MFSMenuButton;
import com.ibm.rchland.mfgapps.mfsclient.MFSMenuPanel;
import com.ibm.rchland.mfgapps.mfsclient.MFSPanel;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSClogPartCheckDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSDX10Dialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSGroupCommentDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSPNSNDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSPnSnChangeDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSRtvPrlnOperDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSRtvWatrNmbrDialog;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSButtonIterator;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSComm;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSTransaction;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSXmlTransaction;

/**
 * <code>MFSSerializedComponentFnctPanel</code> is the
 * <code>MFSMenuPanel</code> used to invoke serialized component functions.
 * @author The MFS Client Development Team
 */
public class MFSSerializedComponentFnctPanel
	extends MFSMenuPanel
{
	private static final long serialVersionUID = 1L;
	/** The default screen name of an <code>MFSSerializedComponentFnctPanel</code>. */
	public static final String SCREEN_NAME = "Serialized Component Functions";

	/** The Part Detail (F2) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbPartDetail = MFSMenuButton.createLargeButton("Part Detail",
			"partDetailF2.gif", "Part Detail", true); //$NON-NLS-1$

	/** The Part History (F3) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbPartHistory = MFSMenuButton.createLargeButton("Part History",
			"partHistoryF3.gif", "Part History", true); //$NON-NLS-1$

	/** The Change PN/SN (F4) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbChangePnSn = MFSMenuButton.createLargeButton("Change PN/SN",
			"PNSNChangeF4.gif", "Change PN/SN", true); //$NON-NLS-1$

	/** The Assembly Tree (F5) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbAssemblyTree = MFSMenuButton.createLargeButton(
			"Assembly Tree", "assemblyTreeF5.gif", "Assembly Tree", true); //$NON-NLS-2$

	/** The Plug Pictorial (F6) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbPlugPictorial = MFSMenuButton.createLargeButton(
			"Plug Pictorial", "plugPictorialF6.gif", "Plug Pictorial", true); //$NON-NLS-2$

	/** The Display WU (F7) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbDspWU = MFSMenuButton.createLargeButton("Display WU",
			"dspwuF7.gif", "Display WU", true); //$NON-NLS-1$

	/** The Display Flags (F8) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbDspFlags = MFSMenuButton.createLargeButton("Display Flags",
			"dspflagsF8.gif", "Display Flags", true); //$NON-NLS-1$

	/** The Add Group Comment (F10) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbAddComment = MFSMenuButton.createLargeButton("Add Grp Cmnt",
			"addCommentF10.gif", "Add Group Comment", true); //$NON-NLS-1$

	/** The Part Check (F12) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbPartCheck = MFSMenuButton.createLargeButton("Part Check",
			"dasdF12.gif", "Part Check", true); //$NON-NLS-1$
	

	/** The part number entered by the user. */
	private String fieldPart = ""; //$NON-NLS-1$

	/** The serial number entered by the user. */
	private String fieldSerial = ""; //$NON-NLS-1$

	/** The reason the user selected from the {@link MFSDX10Dialog}. */
	private String fieldPrevReason = ""; //$NON-NLS-1$

	/**
	 * Constructs a new <code>MFSSerializedComponentFnctPanel</code>.
	 * @param parent the <code>MFSFrame</code> used to display this panel
	 * @param source the <code>MFSPanel</code> that caused this panel to be
	 *        displayed
	 */
	public MFSSerializedComponentFnctPanel(MFSFrame parent, MFSPanel source)
	{
		super(parent, source, SCREEN_NAME, 6, 3);
		this.fieldButtonIterator = createMenuButtonIterator();
		createLayout();
		configureButtons();
		addMyListeners();
	}

	/**
	 * Creates an <code>MFSButtonIterator</code> for this panel's
	 * <code>MFSMenuButton</code>s.
	 * @return the <code>MFSButtonIterator</code>
	 */
	private MFSButtonIterator createMenuButtonIterator()
	{
		//The constructor param is the number of buttons
		MFSButtonIterator result = new MFSButtonIterator(9);
		result.add(this.pbPartDetail);
		result.add(this.pbPartHistory);
		result.add(this.pbChangePnSn);
		result.add(this.pbAssemblyTree);
		result.add(this.pbPlugPictorial);
		result.add(this.pbDspWU);
		result.add(this.pbDspFlags);
		result.add(this.pbAddComment);
		result.add(this.pbPartCheck);
		return result;
	}

	/** Invoked when {@link #pbAssemblyTree} is selected. */
	private void assemblyTree()
	{
		removeMyListeners();
		if (promptAndValidatePNSN())
		{
			MFSPartFunctionDisplayPanel partFunc = new MFSPartFunctionDisplayPanel(
					getParentFrame(), this, this.fieldPart, this.fieldSerial);
			partFunc.assemblyTree(false);
		}
		addMyListeners();
	}

	/** Invoked when {@link #pbAddComment} is selected. */
	private void changeComment()
	{
		removeMyListeners();
		MFSGroupCommentDialog GrpCmtD = new MFSGroupCommentDialog(getParentFrame());
		GrpCmtD.setLocationRelativeTo(getParentFrame());  //~1A
		GrpCmtD.setVisible(true);
		addMyListeners();
	}

	/** Invoked when {@link #pbChangePnSn} is selected. */
	private void changePnSn()
	{
		removeMyListeners();
		MFSPnSnChangeDialog myPnSnChangeDialog = new MFSPnSnChangeDialog(getParentFrame());
		myPnSnChangeDialog.setLocationRelativeTo(getParentFrame()); //~1A
		myPnSnChangeDialog.setVisible(true);
		addMyListeners();
	}

	/**
	 * Loads the <code>DefaultListModel</code> used by the
	 * <code>MFSDX10Dialog</code>.
	 * @param data the data used to load the model
	 * @param listModel the <code>DefaultListModel</code> to load
	 */
	private void loadReasonListModel(String data, DefaultListModel listModel)
	{
		try
		{
			MfsXMLParser xmlParser = new MfsXMLParser(data);
			xmlParser = new MfsXMLParser(xmlParser.getFieldOnly("DX10")); //$NON-NLS-1$
			MfsXMLParser reasonXML;

			try
			{
				listModel.addElement("*NONE     "); //$NON-NLS-1$
				while (true)
				{
					reasonXML = new MfsXMLParser(xmlParser.getNextField("DATA")); //$NON-NLS-1$
					listModel.addElement(reasonXML.getField("RSON") //$NON-NLS-1$
							+ reasonXML.getField("RDES")); //$NON-NLS-1$
				}
			}
			catch (MISSING_XML_TAG_EXCEPTION mt)
			{
				/* Do nothing. We just needed to escape the loop. */
			}
		}
		catch (MISSING_XML_TAG_EXCEPTION mt2)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, mt2);
		}
	}

	/** Invoked when {@link #pbPartCheck} is selected. */
	private void partCheck()
	{
		try
		{
			int rc = 0;
			String errorString = ""; //$NON-NLS-1$

			removeMyListeners();

			//FIXME 34242JR Remove PN and SN from RTV_DX10 call?
			MfsXMLDocument xmlInput = new MfsXMLDocument("RTV_DX10"); //$NON-NLS-1$
			xmlInput.addOpenTag("DATA"); //$NON-NLS-1$
			xmlInput.addCompleteField("INPN", this.fieldPart); //$NON-NLS-1$
			xmlInput.addCompleteField("INSQ", this.fieldSerial); //$NON-NLS-1$
			xmlInput.addCloseTag("DATA"); //$NON-NLS-1$
			xmlInput.finalizeXML();

			MFSTransaction rtv_dx10 = new MFSXmlTransaction(xmlInput.toString());
			rtv_dx10.setActionMessage("Retrieving list of options, Please Wait...");
			MFSComm.getInstance().execute(rtv_dx10, this);

			rc = rtv_dx10.getReturnCode();

			if (rc == 0)
			{
				DefaultListModel reasonListModel = new DefaultListModel();
				loadReasonListModel(rtv_dx10.getOutput(), reasonListModel);
				MFSDX10Dialog ReasonD = new MFSDX10Dialog(getParentFrame(),
						reasonListModel);
				if (this.fieldPrevReason.equals("")) //$NON-NLS-1$
				{
					ReasonD.setSelectedIndex(0);
				}
				else
				{
					//set up the prevReason to be the default choice
					ReasonD.setSearchText(this.fieldPrevReason.substring(0, 8).trim());
					ReasonD.search();
				}
				ReasonD.setLocationRelativeTo(getParentFrame()); //~1A
				ReasonD.setVisible(true);
				if (ReasonD.getPressedEnter())
				{
					if (ReasonD.getReason().trim().equals("*NONE")) //$NON-NLS-1$
					{
						errorString = "Please select an option other than *NONE.";
						rc = 10;
					}
					else
					{
						// set up the previous new work choice so the next time
						// we will start there in the list
						this.fieldPrevReason = ReasonD.getReason().substring(0, 10);
						
						
						MFSClogPartCheckDialog partCheckD = new MFSClogPartCheckDialog(
								getParentFrame(), this.fieldPrevReason);
						partCheckD.setLocationRelativeTo(getParentFrame()); //~1A
						partCheckD.setVisible(true);
					}
				}
			}
			else
			{
				errorString = rtv_dx10.getErms();
			}

			if (rc != 0)
			{
				IGSMessageBox.showOkMB(this, null, errorString, null);
			}
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(this, null, null, e);
		}

		addMyListeners();
	}

	/** Invoked when {@link #pbPartDetail} is selected. */
	private void partDetail()
	{
		removeMyListeners();
		if (promptAndValidatePNSN())
		{
			MFSPartFunctionDisplayPanel partFunc = new MFSPartFunctionDisplayPanel(
					getParentFrame(), this, this.fieldPart, this.fieldSerial);
			partFunc.partDetail(false, true);
		}
		addMyListeners();
	}

	/** Invoked when {@link #pbPartHistory} is selected. */
	private void partHistory()
	{
		removeMyListeners();
		if (promptAndValidatePNSN())
		{
			MFSPartFunctionDisplayPanel partFunc = new MFSPartFunctionDisplayPanel(
					getParentFrame(), this, this.fieldPart, this.fieldSerial);
			partFunc.partHistory(false);
		}
		addMyListeners();
	}

	/** Invoked when {@link #pbPlugPictorial} is selected. */
	private void plugPictorial()
	{
		this.removeMyListeners();
		if (promptAndValidatePNSN())
		{
			MFSPartFunctionDisplayPanel partFunc = new MFSPartFunctionDisplayPanel(
					getParentFrame(), this, this.fieldPart, this.fieldSerial);
			partFunc.plugPictorial(false);
		}
		this.addMyListeners();
	}

	/** Invoked when {@link #pbDspFlags} is selected. */
	private void prlnOperFlags()
	{
		removeMyListeners();
		MFSRtvPrlnOperDialog myRtvPrlnOperD = new MFSRtvPrlnOperDialog(getParentFrame());
		myRtvPrlnOperD.setLocationRelativeTo(getParentFrame()); //~1A
		myRtvPrlnOperD.setVisible(true);
		this.pbDspFlags.requestFocusInWindow();
		addMyListeners();
	}
	
	/**
	 * Prompts the user for a part and serial number.
	 * @return <code>true</code> iff the user entered a valid part and serial number;
	 */
	public boolean promptAndValidatePNSN()
	{
		this.fieldPart = ""; //$NON-NLS-1$
		this.fieldSerial = ""; //$NON-NLS-1$

		MFSPNSNDialog myPNSNDialog = new MFSPNSNDialog(getParentFrame());
		myPNSNDialog.setLocationRelativeTo(getParentFrame()); //~1A
		myPNSNDialog.setVisible(true);

		if (myPNSNDialog.getPressedEnter())
		{
			this.fieldPart = myPNSNDialog.getPNText();
			this.fieldSerial = myPNSNDialog.getSNText();
			if (this.fieldPart.equals("") || this.fieldSerial.equals("")) //$NON-NLS-1$ //$NON-NLS-2$
			{
				return false;
			}
			return true;
		}
		return false;
	}

	/** Invoked when {@link #pbDspWU}is selected. */
	private void wipDriverWU()
	{
		removeMyListeners();
		MFSRtvWatrNmbrDialog myRtvWatrNmbrD = new MFSRtvWatrNmbrDialog(getParentFrame());
		myRtvWatrNmbrD.setLocationRelativeTo(getParentFrame()); //~1A
		myRtvWatrNmbrD.setVisible(true);
		this.pbDspWU.requestFocusInWindow();
		addMyListeners();
	}
	
	/**
	 * Invoked when an action occurs.
	 * @param ae the <code>ActionEvent</code>
	 */
	public void actionPerformed(ActionEvent ae)
	{
		final Object source = ae.getSource();
		if (source == this.pbPartDetail)
		{
			partDetail();
		}
		else if (source == this.pbPartHistory)
		{
			partHistory();
		}
		else if (source == this.pbChangePnSn)
		{
			changePnSn();
		}
		else if (source == this.pbAssemblyTree)
		{
			assemblyTree();
		}
		else if (source == this.pbPlugPictorial)
		{
			plugPictorial();
		}
		else if (source == this.pbDspWU)
		{
			wipDriverWU();
		}
		else if (source == this.pbDspFlags)
		{
			prlnOperFlags();
		}
		else if (source == this.pbAddComment)
		{
			changeComment();
		}
		else if (source == this.pbPartCheck)
		{
			partCheck();
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
		else
		{
			if (keyCode == KeyEvent.VK_F2)
			{
				this.pbPartDetail.requestFocusInWindow();
				this.pbPartDetail.doClick();
			}
			else if (keyCode == KeyEvent.VK_F3)
			{
				this.pbPartHistory.requestFocusInWindow();
				this.pbPartHistory.doClick();
			}
			else if (keyCode == KeyEvent.VK_F4)
			{
				this.pbChangePnSn.requestFocusInWindow();
				this.pbChangePnSn.doClick();
			}
			else if (keyCode == KeyEvent.VK_F5)
			{
				this.pbAssemblyTree.requestFocusInWindow();
				this.pbAssemblyTree.doClick();
			}
			else if (keyCode == KeyEvent.VK_F6)
			{
				this.pbPlugPictorial.requestFocusInWindow();
				this.pbPlugPictorial.doClick();
			}
			else if (keyCode == KeyEvent.VK_F7)
			{
				this.pbDspWU.requestFocusInWindow();
				this.pbDspWU.doClick();
			}
			else if (keyCode == KeyEvent.VK_F8)
			{
				this.pbDspFlags.requestFocusInWindow();
				this.pbDspFlags.doClick();
			}
			else if (keyCode == KeyEvent.VK_F10)
			{
				this.pbAddComment.requestFocusInWindow();
				this.pbAddComment.doClick();
			}
			else if (keyCode == KeyEvent.VK_F12)
			{
				this.pbPartCheck.requestFocusInWindow();
				this.pbPartCheck.doClick();
			}
		}
	}
}
