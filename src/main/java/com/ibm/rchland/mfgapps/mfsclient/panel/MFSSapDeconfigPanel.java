/* @Copyright IBM Corporation 2016. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag Story    Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2016-07-05               Luis M.         -Initial version. */

package com.ibm.rchland.mfgapps.mfsclient.panel;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.ListModel;

import mfsxml.MISSING_XML_TAG_EXCEPTION;
import mfsxml.MfsXMLDocument;
import mfsxml.MfsXMLParser;

import com.ibm.rchland.mfgapps.client.utils.layout.IGSGridLayout;
import com.ibm.rchland.mfgapps.client.utils.messagebox.IGSMessageBox;
import com.ibm.rchland.mfgapps.mfsclient.MFSFrame;
import com.ibm.rchland.mfgapps.mfsclient.MFSInstructionsPanel;
import com.ibm.rchland.mfgapps.mfsclient.MFSMenuButton;
import com.ibm.rchland.mfgapps.mfsclient.MFSPanel;
import com.ibm.rchland.mfgapps.mfsclient.MFSPartInstructionJPanel;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSAcknowledgeInstructionDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSChangeStatusDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSGenericListDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSIQDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSPartNumDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSRejectedPartTagDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSSplitDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSSuspendCodeDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSVpdValidateDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSVrfyFabSerialDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.log.MFSExtraPartDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.log.MFSLogSapDeconfigPartDialog;
import com.ibm.rchland.mfgapps.mfsclient.struct.MFSComponentListModel;
import com.ibm.rchland.mfgapps.mfsclient.struct.MFSComponentRec;
import com.ibm.rchland.mfgapps.mfsclient.struct.MFSInstructionRec;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSButtonIterator;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSCP500Comparator;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSPrintingMethods;
import com.ibm.rchland.mfgapps.mfscommon.MFSConfig;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSComm;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSFixedTransaction;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSTransaction;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSXmlTransaction;
import com.ibm.rchland.mfgapps.mfscommon.comm.trx.RTV_IQ;

/**
 * <code>MFSSAPDeconfigPanel</code> is the <code>MFSInstructionsPanel</code> used to
 * view <code>MFSPartInstructionJPanel</code>s for SAPDeconfig
 * @author The MFS Client Development Team
 */
@SuppressWarnings("serial")
public class MFSSapDeconfigPanel extends MFSInstructionsPanel implements MouseListener {
	/** The "SAP Deconfig" screen name of a <code>MFSSelectWorkPanel</code>. */
	public static final String SAP_DECONFIG_SCREEN_NAME = "SAP Deconfig"; //$NON-NLS-1$

	/** The "Prep" screen name of a <code>MFSSelectWorkPanel</code>. */
	public static final String PREP_SCREEN_NAME = "Prep"; //$NON-NLS-1$

	//~8C Change button images from DCFGFx.gif to smFx.gif
	/** The End Job (F3) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbEnd = MFSMenuButton.createSmallButton("End Job", //$NON-NLS-1$
			"smF3.gif", null, "BUTTON,SAPDECONFIG,ENDJOB"); //$NON-NLS-1$ //$NON-NLS-2$

	/** The Suspend (F7) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbSuspend = MFSMenuButton.createSmallButton("Suspend", //$NON-NLS-1$
			"smF7.gif", null, "BUTTON,SAPDECONFIG,SUSPEND"); //$NON-NLS-1$ //$NON-NLS-2$

	/** The Srch Rm (F9) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbSearchRemove = MFSMenuButton.createSmallButton("Srch Rm", //$NON-NLS-1$
			"smF9.gif", null, "BUTTON,SAPDECONFIG,SEARCHREM"); //$NON-NLS-1$ //$NON-NLS-2$

	/** The Rem All (F10) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbProcAll = MFSMenuButton.createSmallButton("Rem All", //$NON-NLS-1$
			"smF10.gif", null, false); //$NON-NLS-1$

	/** The Rem P/N (F11) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbRemPart = MFSMenuButton.createSmallButton("Rem P/N", //$NON-NLS-1$
			"smF11.gif", null, "BUTTON,SAPDECONFIG,REMPART"); //$NON-NLS-1$ //$NON-NLS-2$

	/** The Split (SHFT + F1) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbSplit = MFSMenuButton.createSmallButton("Split", //$NON-NLS-1$
			"smF13.gif", null, "BUTTON,SAPDECONFIG,SPLITPART"); //$NON-NLS-1$ //$NON-NLS-2$

	/** The Extra (SHFT + F2) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbExtra = MFSMenuButton.createSmallButton("Extra", //$NON-NLS-1$
			"smF14.gif", null, false); //$NON-NLS-1$ //~4C

	/** The Tag (SHFT + F3) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbTagReprint = MFSMenuButton.createSmallButton("Tag", //$NON-NLS-1$
			"smF15.gif", null, "BUTTON,SAPDECONFIG,REPRINTTAG"); //$NON-NLS-1$ //$NON-NLS-2$

	/** The Dwn List (SHFT + F4) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbDownListReprint = MFSMenuButton.createSmallButton("Dwn List", //$NON-NLS-1$
			"smF16.gif", null, "BUTTON,SAPDECONFIG,REPRINTLIST"); //$NON-NLS-1$ //$NON-NLS-2$

	/** The Chg Sts (SHFT + F5) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbChangeStatus = MFSMenuButton.createSmallButton("Chg Sts", //$NON-NLS-1$
			"smF17.gif", null, "BUTTON,SAPDECONFIG,CHANGESTATUS"); //$NON-NLS-1$ //$NON-NLS-2$

	/** The NCM Tag (SHFT + F6) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbNCMReprint = MFSMenuButton.createSmallButton("NCM Tag", //$NON-NLS-1$
			"smF18.gif", null, "BUTTON,SAPDECONFIG,NCMREPRINT"); //$NON-NLS-1$ //$NON-NLS-2$

	/** The Hide/Show Instructions (SHFT + F11) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbHide = MFSMenuButton.createSmallButton("Hide Ins", //$NON-NLS-1$
			"smF23.gif", "Hide Instructions", false); //$NON-NLS-1$ //$NON-NLS-2$

	/** The <code>MFSButtonIterator</code> for this panel. */
	protected MFSButtonIterator fieldButtonIterator;

	/** The MCTL of the current work unit. */
	protected String fieldCurrMctl = ""; //$NON-NLS-1$

	/** The end code. Used to determine the next action to perform. */
	protected int fieldEndCode = 0;

	/** The row <code>Vector</code> when instructions are shown. */
	@SuppressWarnings("rawtypes")
	protected Vector fieldShowRowVector = new Vector();

	/** The row <code>Vector</code> when instructions are not shown. */
	@SuppressWarnings("rawtypes")
	protected Vector fieldHideRowVector = new Vector();
	
	/** The End/Suspend location. */
	protected String fieldEndOrSuspendLoc = ""; //$NON-NLS-1$

	/**
	 * Constructs a new <code>MFSSAPDeconfigPanel</code>.
	 * @param parent the <code>MFSFrame</code> used to display this panel
	 * @param source the <code>MFSPanel</code> that caused this panel to be displayed
	 * @param screenName the screen name of this panel
	 */
	public MFSSapDeconfigPanel(MFSFrame parent, MFSPanel source, String screenName)
	{
		super(parent, source, screenName);
		this.fieldButtonIterator = createMenuButtonIterator();
		this.pnlButtons.setLayout(new IGSGridLayout(5, 4, 2, 1));
		this.pnlButtons.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
		addMyListeners();
	}
	
	/** Adds the listeners to this panel's <code>Component</code>s. */
	protected void addMyListeners()
	{
		this.fieldButtonIterator.reset();
		while (this.fieldButtonIterator.hasNext())
		{
			JButton next = this.fieldButtonIterator.nextJButton();
			next.addActionListener(this);
			next.addKeyListener(this);
		}

		for (int i = 0; i < this.fieldRowVector.size(); i++)
		{
			getRowVectorElementAt(i).getPNList().addKeyListener(this);
		}

		this.spPartsInst.addKeyListener(this);
	}

	/** {@inheritDoc} */
	public void assignFocus()
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
						break;
					}
				}

				if (this.pbSearchRemove.isEnabled())
				{
					this.pbSearchRemove.requestFocusInWindow();
				}
				else if (this.pbRemPart.isEnabled())
				{
					this.pbRemPart.requestFocusInWindow();
				}
				else
				{
					this.pbEnd.requestFocusInWindow();
				}
			}
		}
		else
		{
			if (this.pbSearchRemove.isEnabled())
			{
				this.pbSearchRemove.requestFocusInWindow();
			}
			else if (this.pbRemPart.isEnabled())
			{
				this.pbRemPart.requestFocusInWindow();
			}
			else
			{
				this.pbEnd.requestFocusInWindow();
			}
		}
	}

	/** Changes the status of the selected component. */
	public void changeStatus()
	{
		int rc = 0;
		String error = ""; //$NON-NLS-1$

		try
		{
			removeMyListeners();

			MFSPartInstructionJPanel pip = getRowVectorElementAt(this.fieldActiveRow);
			JList tmpList = pip.getPNList();
			MFSComponentListModel tmpLm = pip.getPNListModel();
			MFSComponentRec cmp = tmpLm.getComponentRecAt(tmpList.getSelectedIndex());

			int index = tmpList.getSelectedIndex();

			if (!cmp.getIdsp().equals("T")) //$NON-NLS-1$
			{
				rc = 2;
				error = "Not Removed yet!"; //$NON-NLS-1$
			}
			else if (cmp.getItms().equals("1")) //$NON-NLS-1$
			{
				rc = 3;
				error = "Obsolete Part"; //$NON-NLS-1$
			}
			else if (cmp.getScrp().equals("1")) //$NON-NLS-1$
			{
				rc = 4;
				error = "Auto Scrap Part"; //$NON-NLS-1$
			}
			else if (cmp.getPrtd().equals("D")) //$NON-NLS-1$
			{
				rc = 5;
				error = "NCM'd Part"; //$NON-NLS-1$
			}

			if (rc != 0)
			{
				String erms = "Cannot Change Status for this Part - " + error; //$NON-NLS-1$
				IGSMessageBox.showOkMB(getParentFrame(), null, erms, null);
			}
			else
			{
				MFSChangeStatusDialog csd = new MFSChangeStatusDialog(getParentFrame());
				csd.setLocationRelativeTo(getParentFrame());
				csd.setVisible(true);

				if (csd.getPressedOkay())
				{
					if (csd.isResetSelected())
					{
						//cannot be coming from scrap and orig_idsp is T
						if ((cmp.getPrtd().equals("S") //$NON-NLS-1$ 
								|| cmp.getPrtd().equals("P") //$NON-NLS-1$
								|| cmp.getPrtd().equals("E")) //$NON-NLS-1$
							&& cmp.getOrg_idsp().equals("T")) //$NON-NLS-1$
						{
							String erms = "Cannot Reset this Part - Already Processed through Inventory!"; //$NON-NLS-1$
							IGSMessageBox.showOkMB(getParentFrame(), null, erms, null);
						}
						else
						{
							// for now we will just set the idsp to an 'R' and
							// the filled quantity to the qnty and force them to
							// act on it before they can suspend or end
							cmp.setFqty(cmp.getQnty());
							cmp.setIdsp("R"); //$NON-NLS-1$
							cmp.updtDisplayString();
							cmp.updtIRDisplayString();
							cmp.updtInstalledParts();
							cmp.setRec_changed(true);
							cmp.setStatusChange(true);
						}
					} // end of Reset Selected

					if (csd.isMissingSelected())
					{
						if (!cmp.getPrtd().equals("K")) //$NON-NLS-1$
						{
							String erms = "Part Was Not Kept!"; //$NON-NLS-1$
							IGSMessageBox.showOkMB(getParentFrame(), null, erms, null);
						}
						else
						{
							cmp.setPrtd("M"); //$NON-NLS-1$
							cmp.setRec_changed(true);
							cmp.updtInstalledParts();
						}
					} // end of Missing Selected

					if (csd.isKeptSelected())
					{
						if (!cmp.getPrtd().equals("M")) //$NON-NLS-1$
						{
							String erms = "Part Was Not Missing!"; //$NON-NLS-1$
							IGSMessageBox.showOkMB(getParentFrame(), null, erms, null);
						}
						else
						{
							cmp.setPrtd("K"); //$NON-NLS-1$
							cmp.setRec_changed(true);
							cmp.updtInstalledParts();
						}
					} // end of Kept Selected

					if (csd.isScrapSelected())
					{
						if (cmp.getOrg_idsp().equals("T")) //$NON-NLS-1$
						{
							String erms = "Cannot Scrap this Part - Already Processed through Inventory!"; //$NON-NLS-1$
							IGSMessageBox.showOkMB(getParentFrame(), null, erms, null);
						}
						else
						{
							if (MFSConfig.getInstance().getConfigValue("SCRP").equals("Y")) //$NON-NLS-1$ //$NON-NLS-2$
							{
								Double cost = new Double(cmp.getCost());

								if (cost.doubleValue() * Integer.parseInt(cmp.getQnty()) > 150.0)
								{
									String erms = "Part Cost is greater than $150.  You Cannot Scrap it"; //$NON-NLS-1$
									IGSMessageBox.showOkMB(getParentFrame(), null, erms, null);
								}
								else
								{
									cmp.setPrtd("S"); //$NON-NLS-1$
									cmp.setRec_changed(true);
									cmp.updtInstalledParts();
								}
							}
							else
							{
								String erms = "Not Authorized to Scrap Parts!"; //$NON-NLS-1$
								IGSMessageBox.showOkMB(getParentFrame(), null, erms, null);
							}
						}
					} // end of Scrap Selected

					if (csd.isUnScrapSelected())
					{
						if (!cmp.getPrtd().equals("S")) //$NON-NLS-1$
						{
							String erms = "Part Was Not Scrapped!"; //$NON-NLS-1$
							IGSMessageBox.showOkMB(getParentFrame(), null, erms, null);
						}
						else
						{
							if (MFSConfig.getInstance().getConfigValue("SCRP").equals("Y")) //$NON-NLS-1$ //$NON-NLS-2$
							{
								String erms = "Not Authorized to Unscrap!"; //$NON-NLS-1$
								IGSMessageBox.showOkMB(getParentFrame(), null, erms, null);
							}
							else if (cmp.getOrg_idsp().equals("T")) //$NON-NLS-1$
							{
								String erms = "Cannot Unscrap this Part - Already Processed through Inventory!"; //$NON-NLS-1$
								IGSMessageBox.showOkMB(getParentFrame(), null, erms, null);
							}
							else
							{
								cmp.setPrtd("P"); //$NON-NLS-1$
								cmp.setRec_changed(true);
								cmp.updtInstalledParts();
							}
						}
					} // end of UnScrap Selected

					if (cmp.getRec_changed())
					{
						tmpList.clearSelection();
						tmpList.setSelectedIndex(index);
						tmpList.ensureIndexIsVisible(index);
						getParentFrame().validate();
					}
				} // end of pressed Ok button
			} // end of good part to change staus
		} // end of try
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
		addMyListeners();
		getRowVectorElementAt(this.fieldActiveRow).getPNList().requestFocusInWindow();
	}

	public void configureButtons()
	{
		/* get rid of all of the buttons before adding any */
		this.pnlButtons.removeAll();

		final MFSConfig config = MFSConfig.getInstance();
		
		//~4A Only add this.pbExtra if EXDCF is Y
		if (config.containsConfigEntry("BUTTON,SAPDECONFIG,EXTRAPART") //$NON-NLS-1$
				&& config.getConfigValue("EXDCF").equals("Y")) //$NON-NLS-1$ //$NON-NLS-2$
		{
			this.pbExtra.setActive(true);
		}
		else
		{
			this.pbExtra.setActive(false);
		}

		//~4C Change logic to mirror pbExtra logic
		if (config.containsConfigEntry("BUTTON,SAPDECONFIG,PROCALL") //$NON-NLS-1$
				&& config.getConfigValue("TDSU").equals("Y")) //$NON-NLS-1$ //$NON-NLS-2$
		{
			this.pbProcAll.setActive(true);
		}
		else
		{
			this.pbProcAll.setActive(false);
		}

		if (this.fieldHeaderRec.getBlvl().toUpperCase().equals("E") //$NON-NLS-1$
				|| this.fieldHeaderRec.getBlvl().toUpperCase().equals("H")) //$NON-NLS-1$
		{
			this.pbHide.setActive(true);
		}
		else
		{
			this.pbHide.setActive(false);
		}

		this.fieldButtonIterator.reset();
		while (this.fieldButtonIterator.hasNext())
		{
			MFSMenuButton next = this.fieldButtonIterator.nextMenuButton();
			if (next.isActive())
			{
				this.pnlButtons.add(next);
				next.setEnabled(true);
			}
			else
			{
				next.setEnabled(false);
			}
		}
	}

	/**
	 * Creates an <code>MFSButtonIterator</code> for this panel's
	 * <code>MFSMenuButton</code>s.
	 * @return the <code>MFSButtonIterator</code>
	 */
	private MFSButtonIterator createMenuButtonIterator()
	{
		//The constructor param is the number of buttons
		MFSButtonIterator result = new MFSButtonIterator(12);
		result.add(this.pbEnd);
		result.add(this.pbSuspend);
		result.add(this.pbSearchRemove);
		result.add(this.pbProcAll);
		result.add(this.pbRemPart);
		result.add(this.pbSplit);
		result.add(this.pbExtra);
		result.add(this.pbTagReprint);
		result.add(this.pbDownListReprint);
		result.add(this.pbChangeStatus);
		result.add(this.pbNCMReprint);
		result.add(this.pbHide);
		return result;
	}


	/** Validates the work unit can be ended and invokes the correct method to end it. */
	public void end_work()
	{
		//Variable to determine if we will continue after Inspection questions are asked.
		//Initialize to true in case there are no Inspection questions.
		boolean iq_continue = true;

		try
		{
			removeMyListeners();

			int rc = 0;
			int index = 0;
			boolean complete = true;
			boolean mandatoryInstNotComplete = false;

			//look for instructions that are incomplete but mandatory
			int instIndex = 0;
			while (instIndex < this.fieldInstVector.size())
			{
				MFSInstructionRec instr = getInstVectorElementAt(index);
				if ((instr.getInstrClass().equals("M") || instr.getInstrClass().equals("A")) //$NON-NLS-1$ //$NON-NLS-2$
						&& !instr.getCompletionStatus().equals("C")) //$NON-NLS-1$
				{
					mandatoryInstNotComplete = true;
				}
				instIndex++;
			}

			while (index < this.fieldComponentListModel.size())
			{
				MFSComponentRec cmp = getComponentListModelCompRecAt(index);

				//only prevent ending if the part isn't obsolete or scrappable
				if (cmp.getIdsp().equals("R")) //$NON-NLS-1$
				{
					if (cmp.getItms().equals("1") || cmp.getScrp().equals("1")) //$NON-NLS-1$ //$NON-NLS-2$
					{
						break;
					}
					else if (cmp.getMlri().equals(" ") || cmp.getMlri().equals("0")) //$NON-NLS-1$ //$NON-NLS-2$
					{
						break;
					}
					else if (cmp.getQnty().equals("00000")) //$NON-NLS-1$
					{
						break;
					}
					else
					{
						complete = false;
					}
				}
				index++;
			}

			if (!complete)
			{
				String erms = "Cannot End this Operation until Work Unit is Complete!"; //$NON-NLS-1$
				IGSMessageBox.showOkMB(getParentFrame(), null, erms, null);
				addMyListeners();
				getRowVectorElementAt(this.fieldActiveRow).getPNList().requestFocusInWindow();
			}
			else if (mandatoryInstNotComplete)
			{
				String erms = "Cannot End this Operation until All Instructions Are Complete!"; //$NON-NLS-1$
				IGSMessageBox.showOkMB(getParentFrame(), null, erms, null);
				addMyListeners();
				getRowVectorElementAt(this.fieldActiveRow).getPNList().requestFocusInWindow();
			}
			else
			{
				/*
				 * perform the logic for retrieving inspection questions for
				 * this operation. If the Ispq flag in the header is set to "B"
				 * for both or "A" for start, then we will go to the server to
				 * find the inspection questions
				 */
				if (this.fieldHeaderRec.getIspq().equals("B") //$NON-NLS-1$
						|| this.fieldHeaderRec.getIspq().equals("A")) //$NON-NLS-1$
				{
					RTV_IQ rtvIQ = new RTV_IQ(this); 
					rtvIQ.setInputNmbr(this.fieldHeaderRec.getNmbr());
					rtvIQ.setInputPrln(this.fieldHeaderRec.getPrln());
					rtvIQ.setInputProd(this.fieldHeaderRec.getProd());
					rtvIQ.setInputX("2"); //$NON-NLS-1$
					rtvIQ.execute();					
					rc = rtvIQ.getReturnCode();

					if (rc != 0)
					{
						// don't allow the program to get by the inspection questions
						iq_continue = false;
						IGSMessageBox.showOkMB(getParentFrame(), null, rtvIQ.getErrorMessage(), null);
					}
					else
					{
						MFSIQDialog iq = new MFSIQDialog(getParentFrame());
						iq.loadIQ(rtvIQ.getOutputIQ());
						iq.setLocationRelativeTo(getParentFrame()); //~5A
						iq.setVisible(true);
						if (iq.getPressedCancel())
						{
							iq_continue = false;
						}
					}
				} // end of Ispq = 'B' or 'S'

				if (iq_continue)
				{
					final MFSConfig config = MFSConfig.getInstance();
					final String nmbrPrln = this.fieldHeaderRec.getNmbr() + "," //$NON-NLS-1$
							+ this.fieldHeaderRec.getPrln().trim();
					final String nmbrAll = this.fieldHeaderRec.getNmbr() + ",*ALL"; //$NON-NLS-1$

					/* Check if we need to print a POK track sub label */
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
						MFSPrintingMethods.tracksub2(this.fieldHeaderRec.getPrln(), "J", //$NON-NLS-1$
								getCurrMctl(), qty, getParentFrame());
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
						myVFSJD.setLocationRelativeTo(getParentFrame()); //~5A
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
						myVPDJD.setLocationRelativeTo(getParentFrame()); //~5A
						myVPDJD.setVisible(true);
						pressCancelValidateVPD = myVPDJD.getPressedCancel();
					}

					if ((!pressCancelFabSerial) && (!pressCancelValidateVPD))
					{
						rc = locIt("E"); //$NON-NLS-1$

						if (rc == 0)
						{
							rc = updt_crwc();
						}

						if (rc == 0)
						{
							rc = updt_instr("C"); //$NON-NLS-1$
							if (rc == 0)
							{
								rc = end_wu("C", "00", "0000"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
								this.fieldEndCode = rc;

								if (rc == 30 || rc == 31)
								{
									addMyListeners();
									restorePreviousScreen();
								}
								else
								{
									addMyListeners();
									getRowVectorElementAt(this.fieldActiveRow).getPNList().requestFocusInWindow();
								}
							} //end of good updt_instr
						}
						else
						{
							addMyListeners();
							getRowVectorElementAt(this.fieldActiveRow).getPNList().requestFocusInWindow();
						}
					} // end !pressedCancel
					else
					{
						addMyListeners();
						getRowVectorElementAt(this.fieldActiveRow).getPNList().requestFocusInWindow();
					}
				} // end of iq_continue check
				else
				{
					addMyListeners();
					getRowVectorElementAt(this.fieldActiveRow).getPNList().requestFocusInWindow();
				}
			} /* end of complete */
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);

			addMyListeners();
			getRowVectorElementAt(this.fieldActiveRow).getPNList().requestFocusInWindow();
		}
	}


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
		final MFSConfig config = MFSConfig.getInstance();

		try
		{
			/* start the END_WU transaction thread */
			String who = "J"; //$NON-NLS-1$

			//we will use the existance of the Defectloc config entry to trigger NCM logic
			String defectloc = config.getConfigValue("DEFECTLOC").concat("                         ").substring(0, 25); //$NON-NLS-1$ //$NON-NLS-2$

			if (!defectloc.substring(0, 9).equals(MFSConfig.NOT_FOUND))
			{
				who = "N"; //$NON-NLS-1$
			}

			StringBuffer endData = new StringBuffer();

			//all but PREP use END_DCFG 
			if (!this.fieldHeaderRec.getNmbr().equals("PREP")) //$NON-NLS-1$
			{
				endData.append("END_DCFG  "); //$NON-NLS-1$
				endData.append(ecod);
				endData.append(qcod);
				endData.append(config.get8CharCellType());
				endData.append(config.get8CharCell());
				endData.append(getCurrMctl());
				endData.append(config.get8CharUser());
				endData.append(this.fieldHeaderRec.getOlev());
				endData.append(this.fieldHeaderRec.getNmbr());
				endData.append(oemo);
				endData.append(who);
				endData.append(this.fieldEndOrSuspendLoc);
			}
			//now use end_prep
			else
			{
				endData.append("ENDSAPPREP"); //$NON-NLS-1$
				endData.append(ecod);
				endData.append(qcod);
				endData.append(config.get8CharCellType());
				endData.append(config.get8CharCell());
				endData.append(getCurrMctl());
				endData.append(config.get8CharUser());
				endData.append(this.fieldHeaderRec.getOlev());
				endData.append(this.fieldHeaderRec.getNmbr());
				endData.append(oemo);
				endData.append(who);
				endData.append(this.fieldEndOrSuspendLoc);
			}

			String amsg = null;
			if (ecod.equals("S")) //$NON-NLS-1$
			{
				amsg = "Suspending the Work Unit, Please Wait..."; //$NON-NLS-1$
			}
			else if (ecod.equals("F")) //$NON-NLS-1$
			{
				amsg = "Failing the Work Unit, Please Wait..."; //$NON-NLS-1$
			}
			else
			{
				amsg = "Ending the Work Unit, Please Wait..."; //$NON-NLS-1$
			}

			MFSTransaction transaction = new MFSFixedTransaction(endData.toString());
			transaction.setActionMessage(amsg);
			MFSComm.getInstance().execute(transaction, this);
			rc = transaction.getReturnCode();

			if ((ecod.equals("C") && (rc == 30 || rc == 31))) //$NON-NLS-1$
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

					/* print fcmes packing List */
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

					/* print fcmes packing List */
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
			}

			if ((ecod.equals("C") && rc != 30 && rc != 31) //$NON-NLS-1$
					|| ((ecod.equals("S")) && (rc != 30) && (rc != 31))) //$NON-NLS-1$
			{
				IGSMessageBox.showOkMB(getParentFrame(), null, transaction.getErms(), null);
			}
		}
		catch (Exception e)
		{
			rc = 10;
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
		return rc;
	}

	/** Adds an extra part. */
	public void extra_part()
	{
		int rc = 0;
		try
		{
			removeMyListeners();
			MFSPartNumDialog partnumdialog = new MFSPartNumDialog(getParentFrame(),
					this.fieldHeaderRec.getPrln());
			partnumdialog.setLocationRelativeTo(getParentFrame()); //~5A
			partnumdialog.setVisible(true);

			if (partnumdialog.getPressedEnter())
			{
				StringBuffer data = new StringBuffer();
				data.append("RTV_FLAGS "); //$NON-NLS-1$
				data.append(this.fieldHeaderRec.getMctl());
				data.append(partnumdialog.getPartNum());

				MFSTransaction rtv_flags = new MFSFixedTransaction(data.toString());
				rtv_flags.setActionMessage("Retrieving Part Information, Please Wait..."); //$NON-NLS-1$
				MFSComm.getInstance().execute(rtv_flags, this);
				rc = rtv_flags.getReturnCode();

				if (rc == 0)
				{
					final String output = rtv_flags.getOutput();
					MFSComponentRec newCr = new MFSComponentRec();

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
					newCr.setCdes(output.substring(6, 30));
					newCr.setCost(output.substring(30, 44));
					newCr.setMctl(this.fieldHeaderRec.getMctl());
					newCr.setItem(partnumdialog.getPartNum());
					newCr.setInpn(partnumdialog.getPartNum());
					newCr.setFamc("     "); //$NON-NLS-1$
					newCr.setQnty(partnumdialog.getQty());
					newCr.setFqty(partnumdialog.getQty());
					newCr.setCntr("          "); //$NON-NLS-1$
					newCr.setInsq("            "); //$NON-NLS-1$
					newCr.setInec("            "); //$NON-NLS-1$
					newCr.setInca("            "); //$NON-NLS-1$
					newCr.setCwun("        "); //$NON-NLS-1$
					newCr.setCooc("  "); //$NON-NLS-1$
					newCr.setIdsp("R"); //$NON-NLS-1$
					newCr.setPrtd("E"); //$NON-NLS-1$
					newCr.setNmbr("DCFG"); //$NON-NLS-1$
					newCr.setMspi(this.fieldHeaderRec.getMspi());
					newCr.setMcsn(this.fieldHeaderRec.getMcsn());

					MFSExtraPartDialog myExtraPartD = new MFSExtraPartDialog(
							getParentFrame(), this.fieldHeaderRec, newCr);
					myExtraPartD.setLocationRelativeTo(this.pnlButtons);
					myExtraPartD.initDisplay();
					myExtraPartD.setVisible(true);
					if (myExtraPartD.getComponentAdded())
					{
						//add the part to the bottom of the list model
						this.fieldComponentListModel.add(this.fieldComponentListModel.getSize(), myExtraPartD.getComp());
						setupExtraPartRow(myExtraPartD.getComp());
						this.validate();
					}
				}
				else
				{
					IGSMessageBox.showOkMB(getParentFrame(), null, rtv_flags.getErms(), null);
				}
			}
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}

		addMyListeners();
		if (this.fieldRowVector.size() > 0)
		{
			getRowVectorElementAt(this.fieldActiveRow).getPNList().requestFocusInWindow();
		}
		else
		{
			this.pbExtra.requestFocusInWindow();
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

				//handle all the shorted parts
				if (next.isShortPart())
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

				//handle different suffix sequences here
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
			final String nmbrPrln = nmbr + "," + prln; //$NON-NLS-1$

			// Check to see if we should be loc'ing this work unit
			// Use actual nmbr, actual prln, actual end/suspend flag
			String cnfgDat = "LOC," + nmbrPrln + "," + endOrSuspendFlag; //$NON-NLS-1$ //$NON-NLS-2$
			String val = config.getConfigValue(cnfgDat);
			if (val.equals(MFSConfig.NOT_FOUND))
			{
				// Check to see if we should be loc'ing this work unit
				// Use actual nmbr, actual prln, 'B'oth for end/suspend flag
				cnfgDat = "LOC," + nmbrPrln + ",B"; //$NON-NLS-1$ //$NON-NLS-2$
				val = config.getConfigValue(cnfgDat);
			}
			if (val.equals(MFSConfig.NOT_FOUND))
			{
				// Check to see if we should be loc'ing this work unit
				// Use actual nmbr, *all prln, actual end/suspend flag
				cnfgDat = "LOC," + nmbr + ",*ALL," + endOrSuspendFlag; //$NON-NLS-1$ //$NON-NLS-2$
				val = config.getConfigValue(cnfgDat);
			}
			if (val.equals(MFSConfig.NOT_FOUND))
			{
				// Check to see if we should be loc'ing this work unit
				// Use actual nmbr, *all prln, 'B'oth for end/suspend flag
				cnfgDat = "LOC," + nmbr + ",*ALL,B"; //$NON-NLS-1$ //$NON-NLS-2$
				val = config.getConfigValue(cnfgDat);
			}
			if (val.equals(MFSConfig.NOT_FOUND))
			{
				// Check to see if we should be loc'ing this work unit
				// Use *all nmbr, actual prln, actual end/suspend flag
				cnfgDat = "LOC,*ALL," + prln + "," + endOrSuspendFlag; //$NON-NLS-1$ //$NON-NLS-2$
				val = config.getConfigValue(cnfgDat);
			}
			if (val.equals(MFSConfig.NOT_FOUND))
			{
				// Check to see if we should be loc'ing this work unit
				// Use *all nmbr, actual prln, 'B'oth for end/suspend flag
				cnfgDat = "LOC,*ALL," + prln + ",B"; //$NON-NLS-1$ //$NON-NLS-2$
				val = config.getConfigValue(cnfgDat);
			}
			if (val.equals(MFSConfig.NOT_FOUND))
			{
				// Check to see if we should be loc'ing this work unit
				// Use *all nmbr, *all prln, actual end/suspend flag
				cnfgDat = "LOC,*ALL,*ALL," + endOrSuspendFlag; //$NON-NLS-1$
				val = config.getConfigValue(cnfgDat);
			}
			if (val.equals(MFSConfig.NOT_FOUND))
			{
				// Check to see if we should be loc'ing this work unit
				// Use *all nmbr, *all prln, 'B'oth for end/suspend flag
				cnfgDat = "LOC,*ALL,*ALL,B"; //$NON-NLS-1$
				val = config.getConfigValue(cnfgDat);
			}

			if (!val.equals(MFSConfig.NOT_FOUND))
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
						tempLocCollector
								.append("NONE      No Location to Specify                  "); //$NON-NLS-1$

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
								this.fieldEndOrSuspendLoc = myLocJD
										.getSelectedListOption().substring(0, 4);
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
	 */
	public void logBlueInstruction(int row)
	{
		MFSPartInstructionJPanel pip = getRowVectorElementAt(row);

		MFSAcknowledgeInstructionDialog ackD = 
			new MFSAcknowledgeInstructionDialog(getParentFrame());
		ackD.setLocationRelativeTo(pip);
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


	/** Invoked when {@link #pbNCMReprint} is selected to reprint an ncm tag. */
	public void ncmreprint()
	{
		try
		{
			removeMyListeners();

			MFSPartInstructionJPanel pip = getRowVectorElementAt(this.fieldActiveRow);
			JList tmpList = pip.getPNList();
			MFSComponentListModel tmpLm = pip.getPNListModel();
			MFSComponentRec cmp = tmpLm.getComponentRecAt(tmpList.getSelectedIndex());

			MFSRejectedPartTagDialog myRPTD = new MFSRejectedPartTagDialog(getParentFrame());
			if (!cmp.getRejs().equals("")) //$NON-NLS-1$
			{
				myRPTD.setRejs(cmp.getRejs());
			}
			myRPTD.setLocationRelativeTo(getParentFrame()); //~5A
			myRPTD.setVisible(true);

			if (myRPTD.getPressedEnter())
			{
				final MFSConfig config = MFSConfig.getInstance();
				boolean foundNCMconfig = false;

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
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}

		addMyListeners();
		getRowVectorElementAt(this.fieldActiveRow).getPNList().requestFocusInWindow();
	}


	/** Invoked when {@link #pbProcAll} is selected to remove all components. */
	public void removeall()
	{
		try
		{
			removeMyListeners();
			Object[] options = {"YES", "CANCEL"}; //$NON-NLS-1$ //$NON-NLS-2$

			int n = JOptionPane.showOptionDialog(
							getParent(),
							"Do you really want to remove all the components without collecting any data?", //$NON-NLS-1$
							"Remove All Components?", JOptionPane.YES_NO_OPTION, //$NON-NLS-1$
							JOptionPane.WARNING_MESSAGE, null, options, options[1]);

			if (n == JOptionPane.YES_OPTION)
			{
				int index = 0;
				while (index < this.fieldComponentListModel.size())
				{
					MFSComponentRec next = getComponentListModelCompRecAt(index);
					if (!next.getIdsp().equals("T")) //$NON-NLS-1$
					{
						next.setRec_changed(true);

						if (!this.fieldHeaderRec.getNmbr().equals("PREP")) //$NON-NLS-1$
						{
							next.setIdsp("T"); //$NON-NLS-1$
						}
						else
						{
							next.setIdsp("D"); //$NON-NLS-1$
						}

						if (next.getScrp().equals("1")) //$NON-NLS-1$
						{
							next.setPrtd("S"); //$NON-NLS-1$
						}
						else if (next.getItms().equals("1")) //$NON-NLS-1$
						{
							next.setPrtd("O"); //$NON-NLS-1$
						}
						else
						{
							next.setPrtd("P"); //$NON-NLS-1$
						}

						next.setFqty("00000"); //$NON-NLS-1$
						next.updtDisplayString();
						next.updtIRDisplayString();
						next.updtInstalledParts();

						//put in for Pemstar Deconfig 
						if (MFSConfig.getInstance().containsConfigEntry("SKIPDCFGTAG") == false) //$NON-NLS-1$
						{

							if ((!next.getPari().equals("0") && !next.getPari().equals(" ")) //$NON-NLS-1$ //$NON-NLS-2$
									|| next.getDwnl().equals("1")) //$NON-NLS-1$
							{
								String ecValue = null;
								if (!next.getCcai().equals(" ") //$NON-NLS-1$
										&& !next.getCcai().equals("0")) //$NON-NLS-1$
								{
									ecValue = next.getInca();
								}
								else
								{
									ecValue = next.getInec();
								}

								String cwun = null;
								if (!next.getCwun().equals("")) //$NON-NLS-1$
								{
									cwun = next.getCwun();
								}
								else
								{
									cwun = "        "; //$NON-NLS-1$
								}

								/* now print the DownLevel Tag if applicable */
								if (next.getDwns().equals("1")) //$NON-NLS-1$
								{
									/* print downlevel subassembly tag */
									MFSPrintingMethods.downtag(next.getInpn(), ecValue,
											cwun, "Downlevel Assembly", 1, //$NON-NLS-1$
											getParentFrame());
								}
								else if (next.getDwnl().equals("1")) //$NON-NLS-1$
								{
									/* print downlevel subassembly tag */
									MFSPrintingMethods.downtag(next.getInpn(), ecValue,
											cwun, "Downlevel Part    ", 1, //$NON-NLS-1$
											getParentFrame());
								}
								else
								{
									/* no downlevel parts */
									MFSPrintingMethods.downtag(next.getInpn(), ecValue,
											cwun, "                  ", 1, //$NON-NLS-1$
											getParentFrame());
								}
							}
						} // end of SKIPDCFGTAG found in config file
					} //end not T idsp
					index++;
				}

				MFSPartInstructionJPanel pip = getRowVectorElementAt(this.fieldActiveRow);
				JList tmpList = pip.getPNList();
				MFSComponentListModel tmpLm = pip.getPNListModel();

				tmpList.clearSelection();
				tmpList.setSelectedIndex(tmpLm.size() - 1);
				tmpList.ensureIndexIsVisible(tmpLm.size() - 1);
				getParentFrame().validate();
				//~3A Add update call so the colors change
				update(getGraphics());
			}
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
		addMyListeners();
		getRowVectorElementAt(this.fieldActiveRow).getPNList().requestFocusInWindow();
	}

	/**
	 * Called to remove a part.
	 * @param logType the log type for the <code>MFSLogDeconfigPartDialog</code>
	 */
	public void removepn(String logType)
	{
		try
		{
			removeMyListeners();

			MFSComponentListModel tempLm = null;
			MFSComponentRec next;
			boolean found = false;

			/* start at activeRow and work way to the bottom looking for a part */
			int row = this.fieldActiveRow;
			int index = 0;

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
					/*@1 check for PREP */
					if ((next.getIdsp().equals("R") && !next.getQnty().equals("00000")) //$NON-NLS-1$ //$NON-NLS-2$
							|| next.getIdsp().equals("R") //$NON-NLS-1$
							&& this.fieldHeaderRec.getNmbr().equals("PREP"))//$NON-NLS-1$ 
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
						 /* @1 check for PREP */
						if ((next.getIdsp().equals("R") && !next.getQnty().equals("00000")) //$NON-NLS-1$ //$NON-NLS-2$
								|| next.getIdsp().equals("R") //$NON-NLS-1$
								&& this.fieldHeaderRec.getNmbr().equals("PREP")) //$NON-NLS-1$
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
			}

			if (found)
			{
				JList list = getRowVectorElementAt(row).getPNList();
				list.setSelectedIndex(index);
				list.ensureIndexIsVisible(index);

				JList dList = getRowVectorElementAt(this.fieldActiveRow).getPNList();
				MFSLogSapDeconfigPartDialog myLogDeconfigPartD = new MFSLogSapDeconfigPartDialog(
						this, logType, this.fieldHeaderRec, dList);

				// give the log Part dialog the rowVector. Should be able to get
				// at everything it needs from the rowVector (part lists and instructions)
				myLogDeconfigPartD.setRowInfo(this.fieldRowVector, this.fieldActiveRow);
				myLogDeconfigPartD.setLocationRelativeTo(this.pnlButtons);
				myLogDeconfigPartD.initDisplay();

				myLogDeconfigPartD.setVisible(true);
			}
			else
			{
				String erms = "There are No Parts That Need to be Removed"; //$NON-NLS-1$
				IGSMessageBox.showOkMB(getParentFrame(), null, erms, null);
			}
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}

		addMyListeners();
		getRowVectorElementAt(this.fieldActiveRow).getPNList().requestFocusInWindow();
	}

	/** Invoked when {@link #pbDownListReprint} is selected to reprint the down level list. */
	public void reprintdownlist()
	{
		try
		{
			removeMyListeners();
			MFSPrintingMethods.downlevellist(this.fieldHeaderRec.getMctl(), 1, getParentFrame());
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
		addMyListeners();
		getRowVectorElementAt(this.fieldActiveRow).getPNList().requestFocusInWindow();
	}

	/** Invoked when {@link #pbTagReprint} is selected to reprint the down tag. */
	public void reprintdowntag()
	{
		try
		{
			removeMyListeners();

			MFSPartInstructionJPanel pip = getRowVectorElementAt(this.fieldActiveRow);
			JList tmpList = pip.getPNList();
			MFSComponentListModel tmpLm = pip.getPNListModel();
			MFSComponentRec cmp = tmpLm.getComponentRecAt(tmpList.getSelectedIndex());

			if ((!cmp.getPari().equals("0") && !cmp.getPari().equals(" ")) //$NON-NLS-1$ //$NON-NLS-2$
					|| cmp.getDwnl().equals("1")) //$NON-NLS-1$
			{
				String ecValue = null;
				if (!cmp.getCcai().equals(" ") && !cmp.getCcai().equals("0")) //$NON-NLS-1$ //$NON-NLS-2$
				{
					ecValue = cmp.getInca();
				}
				else
				{
					ecValue = cmp.getInec();
				}

				String cwun = null;
				if (!cmp.getCwun().equals("")) //$NON-NLS-1$
				{
					cwun = cmp.getCwun();
				}
				else
				{
					cwun = "        "; //$NON-NLS-1$
				}

				/* now print the DownLevel Tag if applicable */
				if (cmp.getDwns().equals("1")) //$NON-NLS-1$
				{
					/* assembly is downlevel or has a downlevel part inside it */
					MFSPrintingMethods.downtag(cmp.getInpn(), ecValue, cwun,
							"Downlevel Assembly", 1, getParentFrame()); //$NON-NLS-1$
				}
				else if (cmp.getDwnl().equals("1")) //$NON-NLS-1$
				{
					/* print downlevel subassembly tag */
					MFSPrintingMethods.downtag(cmp.getInpn(), ecValue, cwun,
							"Downlevel Part    ", 1, getParentFrame()); //$NON-NLS-1$
				}
				else
				{
					/* no downlevel parts */
					MFSPrintingMethods.downtag(cmp.getInpn(), ecValue, cwun,
							"                  ", 1, getParentFrame()); //$NON-NLS-1$
				}

			}

			else
			{
				String erms = "Invalid Selection - Component is not Downlevel and is not an Assembly"; //$NON-NLS-1$
				IGSMessageBox.showOkMB(getParentFrame(), null, erms, null);
			}
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}

		addMyListeners();
		getRowVectorElementAt(this.fieldActiveRow).getPNList().requestFocusInWindow();
	}

	/**
	 * Clears the contents of the panel before calling
	 * {@link MFSFrame#restorePreviousScreen(MFSPanel)}.
	 */
	public void restorePreviousScreen()
	{
		this.pnlRowHolder.removeAll();
		this.fieldLmVector.removeAllElements();

		this.fieldRowVector.removeAllElements();
		this.fieldInstVector.removeAllElements();
		this.fieldComponentListModel.removeAllElements();

		getParentFrame().restorePreviousScreen(this);
	}

	/**
	 * Sets up an extra row.
	 * @param extraComp the <code>MFSComponentRec</code> for the extra row
	 */
	@SuppressWarnings("unchecked")
	public void setupExtraPartRow(MFSComponentRec extraComp)
	{
		try
		{
			boolean someInstructions = false;
			MFSInstructionRec extraInst = null;
			MFSComponentListModel extraLm = null;

			if (this.fieldInstVector.size() > 0
					|| getRowVectorElementAt(0).getName().equals("RowShort")) //$NON-NLS-1$
			{
				someInstructions = true;
			}

			extraInst = new MFSInstructionRec();
			extraInst.setSuff("EXTRAPARTS"); //$NON-NLS-1$
			extraInst.setIseq("BTTOM"); //$NON-NLS-1$

			// remove the row and add it back later
			MFSPartInstructionJPanel lastPip = getRowVectorElementAt(this.fieldRowVector.size() - 1);
			if (lastPip.getName().equals("DeconfigRowEXTRAPARTSBTTOM")) //$NON-NLS-1$
			{
				extraLm = lastPip.getPNListModel();
				extraLm.add(extraLm.size(), extraComp);
				this.fieldRowVector.remove(this.fieldRowVector.size() - 1);
				this.fieldShowRowVector.remove(this.fieldShowRowVector.size() - 1);
				this.fieldHideRowVector.remove(this.fieldHideRowVector.size() - 1);

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

			GridBagConstraints tmpConstraints = createPipConstraints(true,
					this.fieldRowVector.size());

			MFSPartInstructionJPanel pip = new MFSPartInstructionJPanel(this);
			String blvl = this.fieldHeaderRec.getBlvl();
			pip.configure(extraLm, extraInst, someInstructions, blvl);
			pip.setName("DeconfigRowEXTRAPARTSBTTOM"); //$NON-NLS-1$

			this.fieldRowVector.addElement(pip);
			this.fieldHideRowVector.addElement(pip);
			this.fieldShowRowVector.addElement(pip);

			pip.getPNList().addListSelectionListener(this);

			this.pnlRowHolder.add(pip, tmpConstraints);
			this.pnlRowHolder.validate();
			this.validate();
			pip.getPNList().setSelectedIndex(pip.getPNListModel().size() - 1);
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
	@SuppressWarnings("unchecked")
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
				//~7C Rename listPip to pip
				MFSPartInstructionJPanel pip = getRowVectorElementAt(this.fieldActiveRow);
				JList tmpList = pip.getPNList();
				MFSComponentListModel tmpLm = pip.getPNListModel();
				MFSComponentRec cmp = tmpLm.getComponentRecAt(tmpList.getSelectedIndex());

				if (cmp.getFqty().equals("00000") //$NON-NLS-1$ 
						|| cmp.getFqty().equals("00001") //$NON-NLS-1$
						|| !(cmp.getIdsp().equals("R"))) //$NON-NLS-1$
				{
					String erms = "Invalid part to split."; //$NON-NLS-1$
					IGSMessageBox.showOkMB(getParentFrame(), null, erms, null);
				}
				else if (cmp.getScrp().equals("1")) //$NON-NLS-1$
				{
					String erms = "Cannot Split Scrap Parts!"; //$NON-NLS-1$
					IGSMessageBox.showOkMB(getParentFrame(), null, erms, null);
				}
				else
				{
					MFSComponentRec newCr = split_cr(cmp);

					if (newCr != null)
					{
						boolean someInstructions = false;

						if (this.fieldInstVector.size() > 0)
						{
							someInstructions = true;
						}

						//~7C Use pip from above
						GridBagConstraints tmpConstraints = 
							((GridBagLayout) this.pnlRowHolder.getLayout()).getConstraints(pip);
						pip.getPNList().removeKeyListener(this);
						pip.getPNList().removeListSelectionListener(this);
						MFSInstructionRec tmpInst = new MFSInstructionRec();
						tmpInst = pip.getInstructionRec();

						this.pnlRowHolder.remove(pip);
						int rememberActiveRow = this.fieldActiveRow;
						this.fieldRowVector.removeElementAt(rememberActiveRow);

						tmpLm.add(tmpList.getSelectedIndex() + 1, newCr);
						MFSPartInstructionJPanel pip2 = new MFSPartInstructionJPanel(this);
						pip2.configure(tmpLm, tmpInst, someInstructions,
								this.fieldHeaderRec.getBlvl());

						pip2.setName(pip.getName());

						pip2.getPNList().addListSelectionListener(this);
						pip2.getPNList().addKeyListener(this);

						this.fieldRowVector.add(rememberActiveRow, pip2);
						this.pnlRowHolder.add(pip2, tmpConstraints);
						//~7C Moved setSelectedIndex to after pnlRowHolder.add so
						// MFSComponentCellRenderer.getPreferredSize has a Graphics object
						pip2.getPNList().setSelectedIndex(tmpList.getSelectedIndex() + 1);
						pip2.validate();
						this.pnlRowHolder.validate();
						this.validate();
					}
				}
			}
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}

		addMyListeners();
		getRowVectorElementAt(this.fieldActiveRow).getPNList().requestFocusInWindow();
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
			String zeros = "00000"; //$NON-NLS-1$

			MFSSplitDialog splitD = new MFSSplitDialog(getParentFrame(), Integer.parseInt(cmp.getQnty()));
			splitD.setLocationRelativeTo(getParentFrame()); //~5A
			splitD.setVisible(true);

			if (splitD.getPressedOk())
			{
				/*
				 * we will split off the quantity input, but the SPLIT_CR
				 * program works a little bit differently, so
				 * we will subtract the quantity input to the SplitDialog from
				 * the total quantity first
				 */
				int input_qty = Integer.parseInt(splitD.getQtyText());
				String split_string = zeros.substring(0, 5 - splitD.getQtyText().length()) + input_qty;

				/* start the SPLIT_CR transaction thread */
				StringBuffer data = new StringBuffer();
				data.append("SPLIT_CR  "); //$NON-NLS-1$
				data.append(cmp.getMctl());
				data.append(cmp.getCrct());
				data.append(split_string);
				data.append("S"); //$NON-NLS-1$
				data.append(cmp.getFqty());

				MFSTransaction split_cr = new MFSFixedTransaction(data.toString());
				split_cr.setActionMessage("Splitting Component Record, Please Wait..."); //$NON-NLS-1$
				MFSComm.getInstance().execute(split_cr, this);
				rc = split_cr.getReturnCode();

				if (rc == 0)
				{
					MFSComponentRec newCr = new MFSComponentRec(cmp);
					cmp.setRec_changed(true);

					String qty = String.valueOf(Integer.parseInt(cmp.getQnty()) - Integer.parseInt(split_string));
					cmp.setQnty(zeros.substring(0, 5 - qty.length()) + qty);

					if (Integer.parseInt(cmp.getFqty()) - Integer.parseInt(split_string) == 0)
					{
						cmp.setFqty("00000"); //$NON-NLS-1$
						if (!this.fieldHeaderRec.getNmbr().equals("PREP")) //$NON-NLS-1$
						{
							cmp.setIdsp("T"); //$NON-NLS-1$
						}
						else
						{
							cmp.setIdsp("D"); //$NON-NLS-1$
						}
						cmp.setShtp(" "); //$NON-NLS-1$
						cmp.updtDisplayString();
						cmp.updtIRDisplayString();
					}
					else
					{
						String fqty = String.valueOf(Integer.parseInt(cmp.getFqty()) - Integer.parseInt(split_string));
						cmp.setFqty(zeros.substring(0, 5 - fqty.length()) + fqty);
						cmp.setIdsp("R"); //$NON-NLS-1$
						cmp.setShtp(" "); //$NON-NLS-1$
						cmp.updtDisplayString();
						cmp.updtIRDisplayString();
					}

					newCr.setRec_changed(true);
					newCr.setCrct(split_cr.getOutput().substring(0, 4));
					newCr.setQnty(split_string);
					newCr.setFqty(split_string);
//~7D					newCr.setInpn("            "); //$NON-NLS-1$
					newCr.setCntr("          "); //$NON-NLS-1$
					newCr.setCooc("  "); //$NON-NLS-1$
					newCr.setIdsp("R"); //$NON-NLS-1$
					newCr.updtDisplayString();
					newCr.updtIRDisplayString();
					newCr.updtInstalledParts();
					this.fieldComponentListModel.add(0, newCr);
					return newCr;
				}
				
				IGSMessageBox.showOkMB(getParentFrame(), null, split_cr.getErms(), null);
			} //end of pressedOk
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
		return null;
	}


	/**
	 * Suspends a work unit.
	 * ~2C added the EFFICIENCYON label, when present skip the RTV_QC call and default the suspend code to blanks
	 * @param ecod the end code. Should be 'S' for suspend
	 * @param oemo the operation ending mode
	 */
	public void suspend(String ecod, String oemo)
	{
		final MFSConfig config = MFSConfig.getInstance();  //~9a
		String suspendCode="";	//~9a //$NON-NLS-1$
		try
		{
			int rc = 0;
			removeMyListeners();

			/* make sure no components have had a status change */
			int index = 0;
			boolean bummer = false;
			MFSComponentRec cmp;
			String statusChangePartNumber = ""; //$NON-NLS-1$

			while (index < this.fieldComponentListModel.size() && !bummer)
			{
				cmp = getComponentListModelCompRecAt(index);

				if (cmp.getStatusChange())
				{
					bummer = true;
					statusChangePartNumber = cmp.getItem();
				}
				index++;
			}
			if (bummer)
			{
				StringBuffer erms = new StringBuffer();
				erms.append("Part "); //$NON-NLS-1$
				erms.append(statusChangePartNumber);
				erms.append(" has Changed Status - Please Handle"); //$NON-NLS-1$

				IGSMessageBox.showOkMB(getParentFrame(), null, erms.toString(), null);

				getRowVectorElementAt(this.fieldActiveRow).getPNList().requestFocusInWindow();
			}
			else
			{
				if (!config.containsConfigEntry("EFFICIENCYON")) //~9a //$NON-NLS-1$
				{
					DefaultListModel suspendCodeListModel = new DefaultListModel();
					rc = MFSSuspendCodeDialog.loadSuspendCodes(suspendCodeListModel, this, getParentFrame());

					if (rc == 0)
					{
						MFSSuspendCodeDialog suspendCodeJD = new MFSSuspendCodeDialog(
								getParentFrame(), suspendCodeListModel);
						suspendCodeJD.setLocationRelativeTo(getParentFrame()); //~5A
						suspendCodeJD.setVisible(true);

						if (suspendCodeJD.getProceedSelected())
						{
							suspendCode = suspendCodeJD.getSelectedListOption().substring(0, 2);
						}
						else
						{
							if (this.fieldRowVector.size() > 0)
							{
								getRowVectorElementAt(this.fieldActiveRow).getPNList().requestFocusInWindow();
							}
							else
							{
								this.pbSuspend.requestFocusInWindow();
							}
						}

					}
					else
					{
						getRowVectorElementAt(this.fieldActiveRow).getPNList().requestFocusInWindow();
					}

				}
				else
				{
					suspendCode = "  "; //$NON-NLS-1$
				}
				
				if (rc == 0)
				{
					rc = locIt("S"); //$NON-NLS-1$
					if (rc == 0)
					{
						rc = updt_crwc();
					}
					if (rc == 0)
					{
						rc = updt_instr("S"); //$NON-NLS-1$
						if (rc == 0)
						{
							rc = end_wu(ecod, suspendCode, oemo);
							if (rc == 30 || rc == 31)
							{
								this.fieldEndCode = 0;
								restorePreviousScreen();
							}
							else
							{
								if (this.fieldRowVector.size() > 0)
								{
									getRowVectorElementAt(this.fieldActiveRow).getPNList().requestFocusInWindow();
								}
								else
								{
									this.pbSuspend.requestFocusInWindow();
								}
							}
						}
					}				
				}
			} // end of no status change problems
		} // end of try block
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}

		addMyListeners();
	} //end suspend()


	/** Toggles the display of the instructions. */
	@SuppressWarnings("unchecked")
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
					pip = (MFSPartInstructionJPanel) this.fieldShowRowVector.elementAt(yPos);

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
					pip = (MFSPartInstructionJPanel) this.fieldHideRowVector.elementAt(index);

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
		if (this.fieldRowVector.size() > 0)
		{
			getRowVectorElementAt(this.fieldActiveRow).getPNList().requestFocusInWindow();
		}
		else
		{
			this.pbHide.requestFocusInWindow();
		}

		this.setCursor(Cursor.getDefaultCursor());
		this.repaint();
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
			int index = 0;
			StringBuffer data = new StringBuffer();
			StringBuffer head = new StringBuffer();
			MFSComponentRec cmp;
			boolean changed = false;

			String BLANK26 = "                          "; //$NON-NLS-1$
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
								}
							}
							index++;
						}
					} //part instruction
					row++;
				} //while more rows
			} //at least one row in the row vector

			index = 0;
			while (index < this.fieldComponentListModel.size())
			{
				cmp = getComponentListModelCompRecAt(index);
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
					data.append("S"); //$NON-NLS-1$
					data.append(cmp.getCooc());
					data.append(cmp.getPrtd());
					data.append(BLANK26);
				}
				index++;
			}

			if (changed)
			{

				/*
				 * we have found a part that has changed, so we will automatically
				 * update the Scrap parts and the parts with no itemasa record
				 */
				index = 0;
				while (index < this.fieldComponentListModel.size())
				{
					cmp = getComponentListModelCompRecAt(index);
					/*
					 * we already added the parts that have "changed" up above -
					 * we will look for those that have not changed down here
					 * and are scrap or items missing parts - first check to
					 * make sure that they haven't been sent already by checking
					 * the org_idsp value
					 */
					if (cmp.getOrg_idsp().equals("R")) //$NON-NLS-1$
					{
						if (!cmp.getRec_changed()
								&& (cmp.getScrp().equals("1") //$NON-NLS-1$ 
										|| cmp.getItms().equals("1"))) //$NON-NLS-1$
						{
							if (cmp.getScrp().equals("1")) //$NON-NLS-1$
							{
								cmp.setPrtd("S"); //$NON-NLS-1$
							}
							else if (cmp.getItms().equals("1")) //$NON-NLS-1$
							{
								cmp.setPrtd("O"); //$NON-NLS-1$
							}
							else
							{
								cmp.setPrtd("P"); //$NON-NLS-1$
							}

							cmp.setFqty("00000"); //$NON-NLS-1$
							if (!this.fieldHeaderRec.getNmbr().equals("PREP")) //$NON-NLS-1$
							{
								cmp.setIdsp("T"); //$NON-NLS-1$
							}
							else
							{
								cmp.setIdsp("D"); //$NON-NLS-1$
							}
							cmp.updtDisplayString();
							cmp.updtIRDisplayString();

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
							data.append("S"); //$NON-NLS-1$
							data.append(cmp.getCooc());
							data.append(cmp.getPrtd());
							data.append(BLANK26);
						}
					}
					index++;
				}

				/* get current date and time */
				SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-ddHH.mm.ss");  //$NON-NLS-1$
				Date currTime = new Date();
				String dat = fmt.format(currTime);

				final MFSConfig config = MFSConfig.getInstance();

				head.append("UPDT_CRWC ");  //$NON-NLS-1$
				head.append(config.get8CharUser());
				head.append(config.get8CharCell());
				head.append(dat);
				head.append(config.get8CharCellType());
				head.append(BLANK76);

				String errorString = "";  //$NON-NLS-1$
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
			final MFSConfig config = MFSConfig.getInstance();
			int index = 0;

			// ~9 do not call UPDT_INSTR
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
	
						String data = xml_header.toString() + xml_data1.toString();
	
						MFSTransaction updt_instr = new MFSXmlTransaction(data);
						updt_instr.setActionMessage("Updating Instructions, Please Wait..."); //$NON-NLS-1$
						MFSComm.getInstance().execute(updt_instr, this);
						rc = updt_instr.getReturnCode();
	
						if (rc != 0)
						{
							IGSMessageBox.showOkMB(getParentFrame(), null, updt_instr.getErms(), null);
						}
						counter = 0;
						xml_data1 = new MfsXMLDocument();
					} //end of time to send it
				} //end of while loop
			}
		}
		catch (Exception e)
		{
			rc = 11;
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}

		return rc;
	}

	
	
	/** Removes the listeners from this panel's <code>Component</code>s. */
	protected void removeMyListeners()
	{
		this.fieldButtonIterator.reset();
		while (this.fieldButtonIterator.hasNext())
		{
			JButton next = this.fieldButtonIterator.nextJButton();
			next.removeActionListener(this);
			next.removeKeyListener(this);
		}

		for (int i = 0; i < this.fieldRowVector.size(); i++)
		{
			getRowVectorElementAt(i).getPNList().removeKeyListener(this);
		}

		this.spPartsInst.removeKeyListener(this);
	}
	
	/** Sorts the <code>MFSComponentListModel</code>. */
	public void sortListModel()
	{
		this.fieldComponentListModel.sort();
	}
	
	/**
	 * Prepares the panel for display by setting the text of text components;
	 * storing the value of the mctl; and resetting the end code, the active row
	 * index, and the blue row index.
	 * @param mctl the mctl of the work unit for which instructions are being displayed
	 */
	public void prepareForDisplay(String mctl)
	{
		this.fieldCurrMctl = mctl;
		this.fieldEndCode = 0;
		this.fieldActiveRow = 0;
		this.fieldBlueRow = -1;

		this.tpInstalledParts.setText(""); //$NON-NLS-1$
		this.lblUser.setText("User:  " + MFSConfig.getInstance().getConfigValue("USER"));  //$NON-NLS-1$//$NON-NLS-2$
		this.lblMctl.setText("Mctl:  " + mctl); //$NON-NLS-1$
		initSapsAndSapoLabels();
		updt_cntr(this.fieldHeaderRec.getCntr());
		initHeaderLabels();
	}
	
	/** Initializes the text of {@link #lblSaps}and {@link #lblSapo}. */
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
	
	/**
	 * Returns <code>true</code> iff the instruction text is hidden.
	 * @return <code>true</code> iff in hide mode
	 */
	public boolean isHideMode()
	{
		//If hide button says show, then panel is in hide mode
		return this.pbHide.isEnabled() && this.pbHide.getText().startsWith("Show"); //$NON-NLS-1$
	}
	
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
	 * Invoked when the mouse has been pressed on a <code>Component</code>.
	 * @param me the <code>MouseEvent</code>
	 */
	public void mouseClicked(MouseEvent me)
	{
		if (me.getClickCount() == 2)
		{
			removepn(MFSLogSapDeconfigPartDialog.LT_REMOVE);
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	
/** {@inheritDoc} */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void setupPartInstPanel()
	{
		try
		{
			boolean someInstructions = false;
			MFSInstructionRec shortInst = null;
			MFSInstructionRec tempInst = new MFSInstructionRec();
			Vector rowVector = new Vector();
			String curListSuff = ""; //$NON-NLS-1$
			String curListSeq = ""; //$NON-NLS-1$
			int instIndex = 0;
			boolean found = false;
			boolean nonPartInstFound = false;
			int yPos = 0;
			final MFSCP500Comparator comparator = new MFSCP500Comparator(); //~3A
			final String builderLevel = this.fieldHeaderRec.getBlvl(); //~3A

			getSource().startAction(this.SETUP_PART_INST_MSG); //~6C

			// make sure row panel has nuttin in it
			this.pnlRowHolder.removeAll();

			//remove all traces of hide and show elements
			this.fieldHideRowVector.removeAllElements();
			this.fieldShowRowVector.removeAllElements();

			//if no instructions, use single line MFSComponentCellRenderer
			// otherwise use a multiLine MFSComponentCellRenderer
			if (this.fieldInstVector.size() != 0)
			{
				someInstructions = true;
			}

			int listIndex = 0;
			while (listIndex < this.fieldLmVector.size())
			{
				MFSComponentListModel listModel = getLmVectorElementAt(listIndex);

				nonPartInstFound = false;
				curListSuff = listModel.getSuff();
				curListSeq = listModel.getNmsq();

				//if short need to use multiLine renderer
				if (listModel.getIsShort())
				{
					someInstructions = true;
					found = true;
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

				MFSPartInstructionJPanel pip = new MFSPartInstructionJPanel(this);

				if (!curListSuff.equals("          ") || !curListSeq.equals("     ")) //$NON-NLS-1$ //$NON-NLS-2$
				{
					//see if we can find a matching suffix seq
					//if we find one that has a 'smaller' sequence, throw that on the screen
					nonPartInstFound = false;
					found = false;

					for (int i = instIndex; i < this.fieldInstVector.size() && !nonPartInstFound; i++)
					{
						//~3C Use MFSCP500Comparator to perform comparison
						MFSInstructionRec iRecI = getInstVectorElementAt(i);
						if (comparator.compare(iRecI, curListSeq + curListSuff) < 0)
						{
							instIndex = i + 1;
							nonPartInstFound = true;
							tempInst = iRecI;
						}
					}

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
				} //only do the nonPart check if we are not blank suffix and seq

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
					pip.setName("DeconfigRow" + curListSuff + curListSeq + "Short"); //$NON-NLS-1$ //$NON-NLS-2$
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
						pip.setName("DeconfigRow" + iRecName.getSuff() + iRecName.getIseq()); //$NON-NLS-1$
					}
					else
					{
						pip.configure(getLmVectorElementAt(listIndex), tempInst, someInstructions, builderLevel);
						pip.setName("DeconfigRow" + curListSuff + curListSeq); //$NON-NLS-1$
						listIndex++;
					}
				}
				rowVector.addElement(pip);

				pip.getPNList().addListSelectionListener(this);
				pip.getPNList().addKeyListener(this);

				this.pnlRowHolder.add(pip, tmpConstraints);
			} /* end of while loop */

			// clean up any other instructions from the instrVector
			// there may be more instructions that have not been inserted into the display panel
			for (int i = instIndex; i < this.fieldInstVector.size(); i++)
			{
				tempInst = getInstVectorElementAt(i);
				MFSPartInstructionJPanel pip = new MFSPartInstructionJPanel(this);
				MFSComponentListModel emptyLm = new MFSComponentListModel();

				pip.setIsNonPartInstruction(true);
				pip.configure(emptyLm, tempInst, someInstructions, builderLevel);

				pip.setName("DeconfigRow" + tempInst.getSuff() + tempInst.getIseq()); //$NON-NLS-1$

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

		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
		finally
		{
			getSource().stopAction(); //~6C
		}
	}


	/**
	 * Invoked when an action occurs.
	 * @param ae the <code>ActionEvent</code>
	 */
	@Override
	public void actionPerformed(ActionEvent ae)
	{
		try
		{
			final Object source = ae.getSource();
			if (source == this.pbEnd)
			{
				end_work();
			}
			else if (source == this.pbSuspend)
			{
				suspend("S", "0000"); //$NON-NLS-1$ //$NON-NLS-2$
			}
			else if (source == this.pbSplit)
			{
				split_button();
			}
			else if (source == this.pbExtra)
			{
				extra_part();
			}
			else if (source == this.pbDownListReprint)
			{
				reprintdownlist();
			}
			else if (source == this.pbTagReprint)
			{
				reprintdowntag();
			}
			else if (source == this.pbChangeStatus)
			{
				changeStatus();
			}
			else if (source == this.pbRemPart)
			{
				removepn(MFSLogSapDeconfigPartDialog.LT_REMOVE);
			}
			else if (source == this.pbProcAll)
			{
				removeall();
			}
			else if (source == this.pbSearchRemove)
			{
				removepn(MFSLogSapDeconfigPartDialog.LT_SEARCHREMOVE);
			}
			else if (source == this.pbNCMReprint)
			{
				this.ncmreprint();
			}
			else if (source == this.pbHide)
			{
				this.toggleInstructions();
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
	@Override
	public void keyPressed(KeyEvent ke) {
		final int keyCode = ke.getKeyCode();
		final Object source = ke.getSource();

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
		}
		else if (keyCode == KeyEvent.VK_F2)
		{
			if (ke.isShiftDown())
			{
				this.pbExtra.requestFocusInWindow();
				this.pbExtra.doClick();
			}
			else
			{
				//if there is a blue row, F2 will log the instruction
				if (this.fieldBlueRow != -1
						&& !getRowVectorElementAt(this.fieldBlueRow).getInstructionRec()
								.getCompletionStatus().equals("C")) //$NON-NLS-1$
				{
					logBlueInstruction(this.fieldBlueRow);
				}
			}
		}
		else if (keyCode == KeyEvent.VK_F3)
		{
			if (ke.isShiftDown())
			{
				this.pbTagReprint.requestFocusInWindow();
				this.pbTagReprint.doClick();
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
				this.pbDownListReprint.requestFocusInWindow();
				this.pbDownListReprint.doClick();
			}
		}
		else if (keyCode == KeyEvent.VK_F5)
		{
			if (ke.isShiftDown())
			{
				this.pbChangeStatus.requestFocusInWindow();
				this.pbChangeStatus.doClick();
			}
		}
		else if (keyCode == KeyEvent.VK_F6)
		{
			if (ke.isShiftDown())
			{
				this.pbNCMReprint.requestFocusInWindow();
				this.pbNCMReprint.doClick();
			}
		}
		else if (keyCode == KeyEvent.VK_F7)
		{
			this.pbSuspend.requestFocusInWindow();
			this.pbSuspend.doClick();
		}
		else if (keyCode == KeyEvent.VK_F9)
		{
			this.pbSearchRemove.requestFocusInWindow();
			this.pbSearchRemove.doClick();
		}
		else if (keyCode == KeyEvent.VK_F10)
		{
			this.pbProcAll.requestFocusInWindow();
			this.pbProcAll.doClick();
		}
		else if (keyCode == KeyEvent.VK_F11)
		{
			//~3C Add isShiftDown logic for pbHide
			if(ke.isShiftDown())
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
		else if (keyCode == KeyEvent.VK_PAGE_DOWN)
		{
			ke.consume();
			this.spPartsInst.requestFocusInWindow();

			if (this.fieldBlueRow != -1)
			{
				int index = this.fieldBlueRow;
				//if index >= rowVectorSize - 1
				//then the current PartInstructionJPanel is in the bottom row
				//so there is nothing to do
				if (index < this.fieldRowVector.size() - 1)
				{
					MFSPartInstructionJPanel pip = getRowVectorElementAt(index);
					pip.unDoChangeColor();

					pip = getRowVectorElementAt(index + 1);
					int yPos = (int) pip.getBounds().getY();
					this.spPartsInst.getVerticalScrollBar().setValue(yPos);
					if (pip.getIsNonPartInstruction())
					{
						this.fieldBlueRow = index + 1;
						pip.changeColor(MFSPartInstructionJPanel.BLUE_ROW_COLOR);
					}
					else
					{
						this.fieldBlueRow = -1;
						//Highlight the first part
						pip.getPNList().setSelectedIndex(0);
					}
				}
			}
			//no blue row is currently set --> nothing to undo
			else
			{
				int index = this.fieldActiveRow;
				//if index >= rowVectorSize - 1
				//then the current PartInstructionJPanel is in the bottom row
				//so there is nothing to do
				if (index < this.fieldRowVector.size() - 1)
				{
					MFSPartInstructionJPanel pip = getRowVectorElementAt(index + 1);
					int yPos = (int) pip.getBounds().getY();
					this.spPartsInst.getVerticalScrollBar().setValue(yPos);
					if (pip.getIsNonPartInstruction())
					{
						this.fieldBlueRow = index + 1;
						pip.changeColor(MFSPartInstructionJPanel.BLUE_ROW_COLOR);
					}
					else
					{
						//Highlight the first part
						pip.getPNList().setSelectedIndex(0);
					}
				}
			}
		}
		else if (keyCode == KeyEvent.VK_PAGE_UP)
		{
			ke.consume();
			this.spPartsInst.requestFocusInWindow();

			//Blue Row
			if (this.fieldBlueRow != -1)
			{
				int index = this.fieldBlueRow;
				//if index <= 0
				//then the current PartInstructionJPanel is in the top row
				//so there is nothing to do
				if (index > 0)
				{
					MFSPartInstructionJPanel pip = getRowVectorElementAt(index);
					pip.unDoChangeColor();

					pip = getRowVectorElementAt(index - 1);
					int yPos = (int) pip.getBounds().getY();
					this.spPartsInst.getVerticalScrollBar().setValue(yPos);
					if (pip.getIsNonPartInstruction())
					{
						this.fieldBlueRow = index - 1;
						pip.changeColor(MFSPartInstructionJPanel.BLUE_ROW_COLOR);
					}
					else
					{
						this.fieldBlueRow = -1;
						//Highlight the first part
						pip.getPNList().setSelectedIndex(0);
					}
				}
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
					MFSPartInstructionJPanel pip = getRowVectorElementAt(index - 1);
					int yPos = (int) pip.getBounds().getY();
					this.spPartsInst.getVerticalScrollBar().setValue(yPos);
					if (pip.getIsNonPartInstruction())
					{
						this.fieldBlueRow = index - 1;
						pip.changeColor(MFSPartInstructionJPanel.BLUE_ROW_COLOR);
					}
					else
					{
						//Highlight the first part
						pip.getPNList().setSelectedIndex(0);
					}
				}
			}
		}
		else if (keyCode == KeyEvent.VK_DOWN)
		{
			JList tempList = getRowVectorElementAt(this.fieldActiveRow).getPNList();
			tempList.requestFocusInWindow();
			if (this.fieldBlueRow != -1)
			{
				ke.consume();
				getRowVectorElementAt(this.fieldBlueRow).unDoChangeColor();
				this.fieldBlueRow = -1;
				tempList.setSelectedIndex(0);
			}
			else if (tempList.getSelectedIndex() == tempList.getModel().getSize() - 1)
			{
				ke.consume();
				for (int i = this.fieldActiveRow + 1; i < this.fieldRowVector.size(); i++)
				{
					MFSPartInstructionJPanel pip = getRowVectorElementAt(i);
					if (pip.getIsNonPartInstruction() == false)
					{
						JList tmpList = pip.getPNList();
						tmpList.setSelectedIndex(0);
						tmpList.requestFocusInWindow();
						tmpList.ensureIndexIsVisible(0);
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
				ke.consume();
				getRowVectorElementAt(this.fieldBlueRow).unDoChangeColor();
				this.fieldBlueRow = -1;
				tempList.setSelectedIndex(tempList.getModel().getSize() - 1);
			}
			else if (tempList.getSelectedIndex() == 0)
			{
				ke.consume();
				for (int i = this.fieldActiveRow - 1; i >= 0; i--)
				{
					MFSPartInstructionJPanel pip = getRowVectorElementAt(i);
					if (pip.getIsNonPartInstruction() == false)
					{
						JList tmpList = pip.getPNList();
						ListModel tmpLm = pip.getPNListModel();
						int selectedIndex = tmpLm.getSize() - 1;
						tmpList.setSelectedIndex(selectedIndex);
						tmpList.requestFocusInWindow();
						tmpList.ensureIndexIsVisible(selectedIndex);
						break;
					}
				}
			}
		}

	}

}
