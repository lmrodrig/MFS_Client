/* © Copyright IBM Corporation 2001, 2010. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2004-05-17   ~1 25455KK  Dan Kloepping    -Added WWNN rescan info
 * 2004-06-14   ~2 27298KF  TL Moua          -Remove Multiple parts with one set of IR codes
 * 2004-08-09   ~3 27505RS  Dave Fichtinger  -Use header Nmbr instead of component nmbr
 * 2004-12      ~4 29264JM                   -Use rwkOp variable
 * 2006-04-30   ~5 34647EM  Toribioh         -Change Input capacity for JTextFieldLen and
 *                                            validate input size and Barcode Rules in WWNNDialog class
 * 2007-02-19      34242JR  R Prechel        -Java 5 version
 *                                           -Removed rework_MultParts
 * 2007-04-02   ~6 38166JM  R Prechel        -Add setLocationRelativeTo for dialogs
 * 2007-08-08   ~7 38834RS  Martha Luna      -Validate remove option when is called UPDT_RA tx
 * 2007-09-12   ~8 39000RS  JL Woodward      -Call UPDT_RA always together with UPDT_CRWC, need to move code in 
 *                                            rework method, calling to UPDT_CRWC, and UPDT_RA will be now inside
 *                                            the RTV_IRCD transaction block
 * 2007-10-16  ~09 40076EM  Toribio H.       -Change mcsn to default as in UPDT_MS function called in UPDT_CRWC
 *                                            after UPDT_CRWC is called
 * 2007-11-30  ~10 40223PB  Martha Luna      -validate part detail error, don't display Rework dialog
 * 2009-06-03  ~11 43813MZ  Christopher O    -Call MFSPartDataCollectDialog if Data Collection is required
 * 2009-06-24  ~12 45549SR  Christopher O    -Validate error Message in Data Collect Dialog
 * 2009-07-03  ~13 45654SR  Christopher O    -Add collectMode param in containsComponentCollection function.                                         
 * 2009-07-09  ~14 45691EM  Christopher O    -Validate null object before call getDataCollection function
 * 2010-11-01  ~15 49513JM	Toribio H.   	 -Make RTV_IRCD Cacheable 
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JList;

import com.ibm.rchland.mfgapps.client.utils.messagebox.IGSMessageBox;
import com.ibm.rchland.mfgapps.mfsclient.MFSFrame;
import com.ibm.rchland.mfgapps.mfsclient.MFSPanel;
import com.ibm.rchland.mfgapps.mfsclient.MFSPartInstructionJPanel;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSGenericListDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSWWNNDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.log.MFSLogPartDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.log.MFSPartDataCollectDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.rework.MFSRwkCCDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.rework.MFSRwkDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.rework.MFSRwkIRDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.rework.MFSRwkPrintDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.rework.MFSRwkSCDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.rework.MFSRwkSubAssmDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.rework.MFSRwkSubDialog;
import com.ibm.rchland.mfgapps.mfsclient.panel.MFSDirectWorkPanel;
import com.ibm.rchland.mfgapps.mfsclient.struct.MFSComponentListModel;
import com.ibm.rchland.mfgapps.mfsclient.struct.MFSComponentRec;
import com.ibm.rchland.mfgapps.mfsclient.struct.MFSHeaderRec;
import com.ibm.rchland.mfgapps.mfscommon.MFSConfig;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSComm;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSFixedTransaction;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSTransaction;
import com.ibm.rchland.mfgapps.mfscommon.comm.trx.RTV_IRCD;

/**
 * <code>MFSRework</code> contains methods for reworking parts.
 * @author The MFS Client Development Team
 */
public class MFSRework
{
	/** The <code>MFSPanel</code> using the <code>MFSRework</code>. */
	private MFSPanel fieldSource;
	
	/** The parent <code>MFSFrame</code> of the <code>MFSPanel</code>. */
	private MFSFrame fieldParent;
	
	/** The <code>JList</code> for the {@link MFSLogPartDialog}. */
	private JList fieldCmpJList;
	
	/** The <code>MFSComponentListModel</code> for the {@link MFSLogPartDialog}. */
	private MFSComponentListModel fieldCmpListModel;
	
	/** The <code>MFSHeaderRec</code> for the work unit. */
	private MFSHeaderRec fieldHeaderRec;
	
	/** The <code>MFSComponentRec</code> for the component. */
	private MFSComponentRec fieldCmpRec;
	
	/** The <code>MFSPartInstructionJPanel</code> to update when a part is removed. */
	private MFSPartInstructionJPanel partInstruction;

	/**
	 * Constructs a new <code>MFSRework</code>.
	 * @param source the <code>MFSPanel</code> using this <code>MFSRework</code>
	 * @param list the <code>JList</code> for the {@link MFSLogPartDialog}
	 * @param model the <code>MFSComponentListModel</code> for the {@link MFSLogPartDialog}
	 * @param headerRec the <code>MFSHeaderRec</code> for the work unit
	 * @param cmpRec the <code>MFSComponentRec</code> for the component
	 */
	public MFSRework(MFSPanel source, JList list, MFSComponentListModel model, MFSHeaderRec headerRec, MFSComponentRec cmpRec)
	{
		this.fieldSource = source;
		this.fieldParent = source.getParentFrame();
		this.fieldCmpJList = list;
		this.fieldCmpListModel = model;
		this.fieldHeaderRec = headerRec;
		this.fieldCmpRec = cmpRec;
	}
	
	/**
	 * Sets the <code>MFSPartInstructionJPanel</code> to update when a part is removed.
	 * @param partInstruction the <code>MFSPartInstructionJPanel</code>
	 */
	public void setPartInstruction(MFSPartInstructionJPanel partInstruction)
	{
		this.partInstruction = partInstruction;
	}
	
	/** Updates the <code>MFSPartInstructionJPanel</code>. */
	public void handleInstructionUpdate() 
	{
		try
		{
			if (this.partInstruction != null)
			{
				int index = 0;
				MFSComponentRec cmp;

				if (!this.partInstruction.getIsNonPartInstruction())
				{
					boolean foundActionReqParts = false;
					boolean foundInstalledParts = false;

					//look for parts that need action
					MFSComponentListModel tempLm = this.partInstruction.getPNListModel();

					while (index < tempLm.size())
					{
						cmp = tempLm.getComponentRecAt(index);
						if (!cmp.getMlri().equals("0") //$NON-NLS-1$
								&& !cmp.getMlri().equals(" ") //$NON-NLS-1$
								&& (cmp.getIdsp().equals("A") //$NON-NLS-1$
										|| cmp.getIdsp().equals("X") //$NON-NLS-1$ 
										|| cmp.getIdsp().equals("R"))) //$NON-NLS-1$
						{
							foundActionReqParts = true;
						}
						if (cmp.getIdsp().equals("I")) //$NON-NLS-1$
						{
							foundInstalledParts = true;
						}
						index++;
					}

					// found a component that needs to be added or removed yet,
					// so set to partial
					if (foundActionReqParts && foundInstalledParts)
					{
						this.partInstruction.setCompletionStatus(MFSPartInstructionJPanel.PARTIALLY_COMPLETE);
						this.partInstruction.setInstPanelBackground_NotComplete();
					}
					else if (!foundInstalledParts)
					{
						this.partInstruction.setCompletionStatus(MFSPartInstructionJPanel.INCOMPLETE);
						this.partInstruction.validateChangedStatus();
					}
				}//end of non part instruction check
			}
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(this.fieldParent, null, null, e);
		}
	}
	
	/**
	 * Executes the rework logic.
	 * @param loadFlag the load flag for the {@link MFSRwkDialog}
	 */
	public void rework(String loadFlag)
	{
		try
		{
			final MFSConfig config = MFSConfig.getInstance();
			int rc = 0;
			int remove = 0; //~7A
			boolean rwk_sub = false;
			boolean ir_codes = false;   		//~8A
			String irCodesString = "       ";  	//~8A //$NON-NLS-1$
			String comment = "";				//~8A //$NON-NLS-1$
			String ifix = " ";					//~8A //$NON-NLS-1$
			String defectloc  = "";				//~8A //$NON-NLS-1$
			String	ircde = "";					//~8A //$NON-NLS-1$
			String  sccde = "";					//~8A //$NON-NLS-1$
			String  cccde = "";					//~8A //$NON-NLS-1$
			String 	ircode1 = "";				//~8A //$NON-NLS-1$
			String 	ircode2 = "";				//~8A //$NON-NLS-1$
			String 	ircode3 = "";				//~8A //$NON-NLS-1$
			String badPartInd = "N"; //$NON-NLS-1$
			String IBMDefect = "N"; //$NON-NLS-1$
			boolean readyToCallUPDT_RA = false;
			String NCMTagSequence = ""; //$NON-NLS-1$

			String rwkOp = ""; /* ~3 */ //$NON-NLS-1$

			/* save current component rec info */
			MFSComponentRec rwkCmp = new MFSComponentRec(this.fieldCmpRec);

			/*
			 * pull from the directWork screen - should always have a populated
			 * op in the header, but play it safe and check it. if its length is
			 * 0 for some reason, then use the MFSHeaderRec from this class ~3
			 */
			final MFSDirectWorkPanel dirWork = this.fieldParent.getDirectWorkPanel();
			if (dirWork.getHeaderRec().getNmbr().length() > 0)
			{
				rwkOp = dirWork.getHeaderRec().getNmbr();
			}
			else if (this.fieldHeaderRec.getNmbr().length() > 0)
			{
				rwkOp = this.fieldHeaderRec.getNmbr();
			}
			else
			{
				rwkOp = rwkCmp.getNmbr();
			}

			/* get current date and time */
			SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-ddHH.mm.ss"); //$NON-NLS-1$
			Date currTime = new Date();
			String dat = fmt.format(currTime);

			boolean done = false;
			while (!done)
			{
				MFSRwkDialog myRwkD = new MFSRwkDialog(this.fieldParent, loadFlag);
				myRwkD.setLocationRelativeTo(this.fieldParent); //~6A
				myRwkD.setVisible(true);
				if (myRwkD.getProceedSelected())
				{
					rc = 0;

					final String reworkSelection = myRwkD.getSelectedListOption();
					if (reworkSelection.equals(MFSRwkDialog.REPLACE_CURRENT))
					{
						if (!this.fieldCmpRec.getPari().equals(" ") //$NON-NLS-1$
								&& !this.fieldCmpRec.getPari().equals("0")) //$NON-NLS-1$
						{

							MFSRwkSubAssmDialog myRSAD = new MFSRwkSubAssmDialog(this.fieldParent);
							myRSAD.setLocationRelativeTo(this.fieldParent); //~6A
							myRSAD.setVisible(true);
							if (myRSAD.getProceedSelected())
							{
								if (myRSAD.getSelectedListOption().equals(MFSRwkSubAssmDialog.REWORK_PARTS))
								{
									rwk_sub = true;
									subRework(this.fieldCmpRec.getCwun());
									rc = 1; //exit below
									done = true;
								}
							}
							else
							{
								rc = 1; //exit below
							}
						}

						if (rc == 0 && !rwk_sub)
						{
							/*Begin	~11A*/
							if((this.fieldHeaderRec.getDataCollection() != null) && (this.fieldHeaderRec.isCollectRequired()))	//~14C						
							{																	
								if(this.fieldHeaderRec.getDataCollection().containsComponentCollection(rwkCmp.getMctl(),rwkCmp.getCrct(),MFSPartDataCollectDialog.COLL_REWORK_REMOVE)) //~13C	
								{																		
									MFSPartDataCollectDialog collectDialog;						
									collectDialog = new MFSPartDataCollectDialog(this.fieldParent, this.fieldHeaderRec, MFSPartDataCollectDialog.COLL_REWORK_REMOVE, rwkCmp.getCrct(),rwkCmp.getMctl()); 					
									rc = collectDialog.getFieldReturnCode();					
									if(rc != 0 )													
									{																
										//Exit from loop while (!done)
										break;											
									}																														
								}																	
							}																							
							/*End ~11A*/
							
							MFSLogPartDialog myLogPartD = new MFSLogPartDialog(
									dirWork, MFSLogPartDialog.LT_REWORK, this.fieldHeaderRec, 
									this.fieldCmpJList, this.fieldCmpListModel); 
							myLogPartD.initDisplay();
							myLogPartD.setLocationRelativeTo(this.fieldParent); //~6A
							myLogPartD.setVisible(true);

							if (!myLogPartD.getRwkPartLogged())
							{
								rc = 10;
							}
							if (myLogPartD.getRwkErrorDetail()) //~10A
							{
								done = true;				   //~10A
							}
						}
					}
					
					else if((reworkSelection.equals(MFSRwkDialog.REMOVE_UNFKIT)) ||
							   (reworkSelection.equals(MFSRwkDialog.REMOVE_CURRENT)))                      //~7A
					{
						remove = 1;     //~7A
					}
					
					if(rc==0)
					{
						//~8 Now updt_crwc and updt_ra will be called inside the rtv_ircd stuff
						RTV_IRCD rtvIRCD = new RTV_IRCD(this.fieldSource); //~15
						rtvIRCD.setInputCellType(MFSConfig.getInstance().get8CharCellType());					
						rtvIRCD.execute();
						
						rc = rtvIRCD.getReturnCode();
						if (rc != 0)
						{
							IGSMessageBox.showOkMB(this.fieldParent, null, rtvIRCD.getErrorMessage(), null);
							done = true;
						}
						else
						{
							boolean irDone = false;
							if (!rtvIRCD.getOutputIRData().equals("")) //$NON-NLS-1$
							{
								while (!irDone)
								{
									MFSRwkIRDialog myRwkIRD = new MFSRwkIRDialog(this.fieldParent);
									myRwkIRD.loadRwkIRListModel(rtvIRCD.getOutputIRData());
									if (reworkSelection.equals(MFSRwkDialog.REPLACE_CURRENT)
											|| reworkSelection.equals(MFSRwkDialog.REMOVE_CURRENT)
											|| reworkSelection.equals(MFSRwkDialog.REMOVE_UNFKIT))
									{
										myRwkIRD.disableCancel();
									}
									myRwkIRD.setLocationRelativeTo(this.fieldParent); //~6A
									myRwkIRD.setVisible(true);

									if (myRwkIRD.getProceedSelected())
									{
										boolean scDone = false;

										while (!scDone)
										{
											MFSRwkSCDialog myRwkSCD = new MFSRwkSCDialog(this.fieldParent);
											myRwkSCD.loadRwkSCListModel(rtvIRCD.getOutputIRData(),
													myRwkIRD.getSelectedListOption().substring(0, 3));
											myRwkSCD.setLocationRelativeTo(this.fieldParent); //~6A
											myRwkSCD.setVisible(true);

											if (myRwkSCD.getProceedSelected())
											{
												MFSRwkCCDialog myRwkCCD = new MFSRwkCCDialog(this.fieldParent, rwkCmp.getDisplayString());
												myRwkCCD.loadRwkCCListModel(rtvIRCD.getOutputCCData(), myRwkIRD.getSelectedListOption().substring(0, 3));
												myRwkCCD.setLocationRelativeTo(this.fieldParent); //~6A
												myRwkCCD.setVisible(true);

												if (myRwkCCD.getProceedSelected())
												{
													// pad comment with blanks to 80 chars
													comment = myRwkCCD.getCommentText();
													while (comment.length() < 80)
													{
														comment += " "; //$NON-NLS-1$
													}

													// get the defect loc from the config file
													defectloc = MFSConfig.getInstance().getConfigValue("DEFECTLOC").concat("                         ").substring(0, 25); //$NON-NLS-1$ //$NON-NLS-2$

													if (defectloc.substring(0, 9).equals(MFSConfig.NOT_FOUND))
													{
														defectloc = "                         "; //$NON-NLS-1$
														readyToCallUPDT_RA = true;
													}
													else
													{
														if (rwkCmp.getQnty().equals("00000")) //$NON-NLS-1$
														{
															readyToCallUPDT_RA = true;
														}
														else
														{
															MFSGenericListDialog myQuestionD = new MFSGenericListDialog(
																	this.fieldParent, "Bad Part?",  //$NON-NLS-1$
																	"Is this part defective?"); //$NON-NLS-1$
															myQuestionD.setSizeSmall();
															myQuestionD.loadAnswerListModel("NO YES", 3); //$NON-NLS-1$
															myQuestionD.setDefaultSelection("NO "); //$NON-NLS-1$
															myQuestionD.setLocationRelativeTo(this.fieldParent); //~6A
															myQuestionD.setVisible(true);

															if (myQuestionD.getProceedSelected())
															{

																if (myQuestionD.getSelectedListOption().substring(0, 1).equals("Y")) //$NON-NLS-1$
																{
																	badPartInd = "Y"; //$NON-NLS-1$
																	MFSGenericListDialog myQuestionD2 = new MFSGenericListDialog(
																			this.fieldParent, "IBM Product Defect?", //$NON-NLS-1$
																			"Is this an IBM Caused Defect?"); //$NON-NLS-1$
																	myQuestionD2.setSizeSmall();
																	myQuestionD2.loadAnswerListModel("NO YES", 3); //$NON-NLS-1$
																	myQuestionD2.setDefaultSelection("NO "); //$NON-NLS-1$
																	myQuestionD2.setLocationRelativeTo(this.fieldParent); //~6A
																	myQuestionD2.setVisible(true);
																	
																	if (myQuestionD2.getProceedSelected())
																	{
																		readyToCallUPDT_RA = true;
																		if (myQuestionD2.getSelectedListOption().substring(0, 1).equals("Y")) //$NON-NLS-1$
																		{
																			IBMDefect = "Y"; //$NON-NLS-1$
																		}
																		else
																		{
																			IBMDefect = "N"; //$NON-NLS-1$
																		}
																	}
																}
																else
																{
																	badPartInd = "N"; //$NON-NLS-1$
																	IBMDefect = "N"; //$NON-NLS-1$
																	readyToCallUPDT_RA = true;
																}
															}//end of pressedEnter
														}//end non-zero quantity
													}
													if (readyToCallUPDT_RA)
													{
														// ~8A Just populate the ir codes, call updt_ra at the end
														//try to protect from index out of bounds on the IR,SC and CC values.
														if (myRwkIRD.getSelectedListOption().length() >= 3)
														{
															ircde = myRwkIRD.getSelectedListOption().substring(0, 3);
														}
														else
														{
															ircde = "   "; //$NON-NLS-1$
														}
														if (myRwkSCD.getSelectedListOption().length() >= 7)
														{
															sccde = myRwkSCD.getSelectedListOption().substring(5, 7);
														}
														else
														{
															sccde = "  "; //$NON-NLS-1$
														}
														if (myRwkCCD.getSelectedListOption().length() >= 2)
														{
															cccde = myRwkCCD.getSelectedListOption().substring(0, 2);
														}
														else
														{
															cccde = "  "; //$NON-NLS-1$
														}
														ircode1 = myRwkIRD.getSelectedListOption();
														ircode2 = myRwkSCD.getSelectedListOption();
														ircode3 = myRwkCCD.getSelectedListOption();
														irCodesString = ircde + sccde + cccde;	//~8A										
														scDone = true;
														irDone = true;
														ir_codes = true;	//~8A
														ifix = "1";			//~8A //$NON-NLS-1$
													}//readyToCallUPDTRA is true
												}// end of myRwkCCD.getProceedSelected()
											}// end of myRwkSCD.getProceedSelected()
											else
											{
												scDone = true;
											}
										}
									}
									else
									{
										irDone = true;
									}
								}
							}
							//no ir data
							else
							{
								/* pad comment with blanks to 80 chars */
								while (comment.length() < 80)
								{
									comment += " "; //$NON-NLS-1$
								}

								// get the defect loc from the config file
								defectloc = config.getConfigValue("DEFECTLOC").concat("                         ").substring(0, 25); //$NON-NLS-1$ //$NON-NLS-2$
								if (defectloc.substring(0, 9).equals(MFSConfig.NOT_FOUND))
								{
									defectloc = "                         "; //$NON-NLS-1$
									readyToCallUPDT_RA = true;
								}
								else
								{
									if (rwkCmp.getQnty().equals("00000")) //$NON-NLS-1$
									{
										readyToCallUPDT_RA = true;
									}
									else
									{
										MFSGenericListDialog myQuestionJD = new MFSGenericListDialog(
												this.fieldParent, "Bad Part?", "Is this part defective?"); //$NON-NLS-1$ //$NON-NLS-2$
										myQuestionJD.setSizeSmall();
										myQuestionJD.loadAnswerListModel("NO YES", 3); //$NON-NLS-1$
										myQuestionJD.setDefaultSelection("NO "); //$NON-NLS-1$
										myQuestionJD.setLocationRelativeTo(this.fieldParent); //~6A
										myQuestionJD.setVisible(true);

										if (myQuestionJD.getProceedSelected())
										{
											if (myQuestionJD.getSelectedListOption().substring(0, 1).equals("Y")) //$NON-NLS-1$
											{
												badPartInd = "Y"; //$NON-NLS-1$
												MFSGenericListDialog myQuestionJD2 = new MFSGenericListDialog(
														this.fieldParent, "IBM Product Defect?", "Is this an IBM Caused Defect?"); //$NON-NLS-1$ //$NON-NLS-2$
												myQuestionJD2.setSizeSmall();
												myQuestionJD2.loadAnswerListModel("NO YES", 3); //$NON-NLS-1$
												myQuestionJD2.setDefaultSelection("NO "); //$NON-NLS-1$
												myQuestionJD2.setLocationRelativeTo(this.fieldParent); //~6A
												myQuestionJD2.setVisible(true);
												
												if (myQuestionJD2.getProceedSelected())
												{
													readyToCallUPDT_RA = true;
													if (myQuestionJD2.getSelectedListOption().substring(0, 1).equals("Y")) //$NON-NLS-1$
													{
														IBMDefect = "Y"; //$NON-NLS-1$
													}
													else
													{
														IBMDefect = "N"; //$NON-NLS-1$
													}
												}
											}
											else
											{
												badPartInd = "N"; //$NON-NLS-1$
												IBMDefect = "N"; //$NON-NLS-1$
												readyToCallUPDT_RA = true;
											}
										}
									}//end of non zero quantity	
								}// end of no defectloc found		
							}// end of no IR codes
						}
					}
					
					//~8A Now call check_plug, updt_crwc, updt_ra 	together, at the end of all previous screens
					//    and validations
					if (readyToCallUPDT_RA)
					{
						String errorString = ""; //$NON-NLS-1$
						/* ~1 add */
						if ((remove==1)&&rwkCmp.getPnri().equals("W")) //$NON-NLS-1$
						{
							MFSWWNNDialog wwnnD = new MFSWWNNDialog(this.fieldParent, rwkCmp, MFSWWNNDialog.LT_REMOVE);
							wwnnD.initDisplay();
							wwnnD.setLocationRelativeTo(this.fieldParent); //~6A
							wwnnD.setVisible(true);
							rc = wwnnD.getReturnCode();
						}
						/* ~1 end */
						
						/*Begin	~11A*/
						if((rc == 0) && (remove == 1) && (this.fieldHeaderRec.getDataCollection() != null) && (this.fieldHeaderRec.isCollectRequired())) //~14C 							
						{																	
							if(this.fieldHeaderRec.getDataCollection().containsComponentCollection(rwkCmp.getMctl(),rwkCmp.getCrct(),MFSPartDataCollectDialog.COLL_REMOVE)) //~13C	
							{																		
								MFSPartDataCollectDialog collectDialog;						
								collectDialog = new MFSPartDataCollectDialog(this.fieldParent, this.fieldHeaderRec, MFSPartDataCollectDialog.COLL_REMOVE, rwkCmp.getCrct(),rwkCmp.getMctl()); 					
								rc = collectDialog.getFieldReturnCode();					
								if(rc != 0 )													//~12C
								{																				
									//Exit from loop 
									done = true;							
								}	
							}																	
						}																							
						/*End ~11A*/
						
						/* ~1C */
						if ((remove==1)&&!this.fieldCmpRec.getOrg_idsp().equals("A") && (rc == 0)) //$NON-NLS-1$
						{
							/*
							 * new trx for checking plugged parts for ipsr
							 * 18841JM 8/22/01
							 */
							if (!this.fieldCmpRec.getCsni().equals(" ") //$NON-NLS-1$
									&& !this.fieldCmpRec.getCsni().equals("0")) //$NON-NLS-1$
							{
								/* build check plug info string */
								StringBuffer data = new StringBuffer();
								data.append("CHCK_PLUG "); //$NON-NLS-1$
								data.append(this.fieldCmpRec.getInpn());
								data.append(this.fieldCmpRec.getInsq());
								data.append(this.fieldCmpRec.getMspi());
								data.append(this.fieldCmpRec.getMcsn());
								data.append(this.fieldCmpRec.getMctl());
								data.append(this.fieldCmpRec.getPrln());
								data.append(this.fieldCmpRec.getNmbr());
								data.append(MFSConfig.getInstance().get8CharUser());
								data.append(this.fieldCmpRec.getCrct());

								MFSTransaction chck_plug = new MFSFixedTransaction(data.toString());
								chck_plug.setActionMessage("Checking Plug Limit, Please Wait..."); //$NON-NLS-1$
								MFSComm.getInstance().execute(chck_plug, this.fieldSource);
								rc = chck_plug.getReturnCode();
								errorString = chck_plug.getOutput();
								
								// rc = 50, part to be ncm'd, 
								// rc=100 part has one plug left warning
								if ((rc == 50) || (rc == 100))
								{
									IGSMessageBox.showOkMB(this.fieldParent, null, errorString, null);
									rc = 0;
								}
							} /* end-if serial part check */

							if ((remove==1)&&rc == 0) /* added for check_qd10 trx 18841JM */
							{
								String BLANK25 = "                         "; //$NON-NLS-1$
								String BLANK76 = "                                                                            "; //$NON-NLS-1$

								String unFkitFlag = " "; //$NON-NLS-1$
								if (reworkSelection.equals(MFSRwkDialog.REMOVE_UNFKIT))
								{
									unFkitFlag = "U"; //$NON-NLS-1$
								}

								String idsp = "N"; //$NON-NLS-1$
								if (this.fieldHeaderRec.getWtyp().equals("T")) //$NON-NLS-1$
								{
									idsp = "D"; //$NON-NLS-1$
								}

								StringBuffer data = new StringBuffer(320);
								data.append("UPDT_CRWC "); //$NON-NLS-1$
								data.append(config.get8CharUser());
								data.append(config.get8CharCell());
								data.append(dat);
								data.append(config.get8CharCellType());
								data.append(BLANK76);
								data.append(this.fieldCmpRec.getMctl());
								data.append(this.fieldCmpRec.getCrct());
								data.append(this.fieldCmpRec.getInpn());
								data.append("            "); //$NON-NLS-1$
								data.append(this.fieldCmpRec.getInsq());
								data.append("            "); //$NON-NLS-1$
								data.append(this.fieldCmpRec.getAmsi());
								data.append(this.fieldCmpRec.getMspi());
								data.append(this.fieldCmpRec.getMcsn());
								data.append(this.fieldCmpRec.getCwun());
								data.append(idsp);
								data.append("          "); //$NON-NLS-1$
								data.append("00000"); //$NON-NLS-1$
								data.append(" "); //$NON-NLS-1$
								data.append(this.fieldCmpRec.getShtp());
								data.append(this.fieldCmpRec.getMlri());
								data.append(this.fieldCmpRec.getPnri());
								data.append("J"); //$NON-NLS-1$
								data.append("  "); //$NON-NLS-1$
								data.append(" "); //$NON-NLS-1$
								data.append(unFkitFlag);
								data.append(BLANK25);

								MFSTransaction updt_crwc = new MFSFixedTransaction(data.toString());
								updt_crwc.setActionMessage("Updating Component, Please Wait..."); //$NON-NLS-1$
								MFSComm.getInstance().execute(updt_crwc, this.fieldSource);
								rc = updt_crwc.getReturnCode();
								if (rc != 0)
								{
									errorString = updt_crwc.getOutput();
								}
							}
						} //end-if part previously installed or not orig idsp = a						
						if (remove==1 && rc==0)
						{
							this.fieldCmpRec.setInpn("            "); //$NON-NLS-1$
							this.fieldCmpRec.setInsq("            "); //$NON-NLS-1$
							this.fieldCmpRec.setInec("            "); //$NON-NLS-1$
							this.fieldCmpRec.setInca("            "); //$NON-NLS-1$
							//don't want to blank out cwun, field is permanent
							this.fieldCmpRec.setCooc("  "); //$NON-NLS-1$

							if (!this.fieldCmpRec.getCmti().equals(" ") //$NON-NLS-1$
									&& !this.fieldCmpRec.getCmti().equals("0")) //$NON-NLS-1$
							{
								/* ~09 Set default mcsn instead of $000000*/
								/* this.fieldCmpRec.setMcsn("$000000");*/
								this.fieldCmpRec.setMcsn("$" + this.fieldHeaderRec.getOrno());//$NON-NLS-1$
							}

							this.fieldCmpRec.setFqty("00000"); //$NON-NLS-1$
							if (this.fieldHeaderRec.getWtyp().equals("T")) //$NON-NLS-1$
							{
								this.fieldCmpRec.setIdsp("D"); //$NON-NLS-1$
							}
							else
							{
								this.fieldCmpRec.setIdsp("A"); //$NON-NLS-1$
							}
							this.fieldCmpRec.setBufferStatus("-1"); //$NON-NLS-1$
							this.fieldCmpRec.setRec_changed(false);
							this.fieldCmpRec.updtDisplayString();
							this.fieldCmpRec.updtIRDisplayString();
							this.fieldCmpRec.updtInstalledParts();

							//update the instruction since we now have "missing" parts
							this.handleInstructionUpdate();
						}
						
						if (rc==0)
						{
							StringBuffer ra_data = new StringBuffer(1024);
							ra_data.append("UPDT_RA   "); //$NON-NLS-1$
							/* pad with *'s to 512 chars */
							while (ra_data.length() < 512)
							{
								ra_data.append("*"); //$NON-NLS-1$
							}

							ra_data.append(this.fieldHeaderRec.getMfgn());
							ra_data.append(this.fieldHeaderRec.getIdss());
							ra_data.append(rwkCmp.getMctl());
							ra_data.append("        ");// epnm //$NON-NLS-1$
							ra_data.append("        ");// esnm //$NON-NLS-1$
							ra_data.append(this.fieldHeaderRec.getTypz());
							ra_data.append(this.fieldHeaderRec.getCmdl());
							ra_data.append(rwkCmp.getPrln());
							ra_data.append(rwkCmp.getProd());
							ra_data.append(rwkCmp.getMatp());
							ra_data.append(rwkCmp.getMspi());
							ra_data.append(rwkCmp.getMcsn());
							ra_data.append(this.fieldHeaderRec.getMmdl());
							ra_data.append(rwkOp);/* ~3 */
							ra_data.append(config.get8CharUser());
							ra_data.append(config.get8CharCellType());
							ra_data.append(config.get8CharCell());
							ra_data.append(dat);
							ra_data.append(rwkCmp.getCsds());
							ra_data.append(rwkCmp.getCsts());
							ra_data.append(rwkCmp.getNmbr());
							ra_data.append(rwkCmp.getUser());
							ra_data.append(rwkCmp.getCtyp());
							ra_data.append(rwkCmp.getCell());
							ra_data.append(rwkCmp.getInpn());
							ra_data.append(rwkCmp.getInsq());
							ra_data.append(rwkCmp.getInec());
							ra_data.append(rwkCmp.getInca());
							ra_data.append(rwkCmp.getCwun());
							ra_data.append(rwkCmp.getCdes());
							ra_data.append(rwkCmp.getPrtd());
							ra_data.append(rwkCmp.getPrdc());
							ra_data.append("        ");// rprd //$NON-NLS-1$
							ra_data.append(rwkCmp.getPll1());
							ra_data.append(rwkCmp.getPll2());
							ra_data.append(rwkCmp.getPll3());
							ra_data.append(rwkCmp.getPll4());
							ra_data.append(rwkCmp.getPll5());
							ra_data.append(irCodesString);
							ra_data.append(ifix);// ifix
							ra_data.append(comment);
							ra_data.append(badPartInd);
							ra_data.append(IBMDefect);
							ra_data.append(rwkCmp.getCrct());
							ra_data.append(defectloc);
							if((reworkSelection.equals(MFSRwkDialog.REPLACE_CURRENT)) ||
							   (reworkSelection.equals(MFSRwkDialog.REPLACE_CURRENT)))                      //~7A
							{
							  remove = 1;     //~7A
							}
							ra_data.append(remove);//~7A

							// pad with blanks to 1024 chars
							while (ra_data.length() < 1024)
							{
								ra_data.append(" "); //$NON-NLS-1$
							}

							MFSTransaction updt_ra = new MFSFixedTransaction(ra_data.toString());
							updt_ra.setActionMessage("Updating Rwk Action Info, Please Wait..."); //$NON-NLS-1$
							MFSComm.getInstance().execute(updt_ra, this.fieldSource);
							rc = updt_ra.getReturnCode();
							if (rc != 0)
							{
								IGSMessageBox.showOkMB(this.fieldParent, null, updt_ra.getErms(), null);
							}
							else
							{
								// print out the NCM tags if they are configured
								if (badPartInd.equals("Y")) //$NON-NLS-1$
								{
									NCMTagSequence = updt_ra.getOutput().substring(0, 8);
									this.fieldCmpRec.setRejs(NCMTagSequence);

									if (config.containsConfigEntry("NCMTAG")) //$NON-NLS-1$
									{
										MFSPrintingMethods.ncmtag(NCMTagSequence, 1, this.fieldParent);
									}
									if (config.containsConfigEntry("NCMSHEET")) //$NON-NLS-1$
									{
										MFSPrintingMethods.ncmsheet(NCMTagSequence, 1, this.fieldParent);
									}
									if ((ir_codes)&&config.containsConfigEntry("NCMBIGTAG")) //$NON-NLS-1$
									{
										MFSPrintingMethods.ncmbigtag(
												NCMTagSequence, 1,
												this.fieldParent);
									}
								}
								else if (rwkCmp.getQnty().equals("00000")) //$NON-NLS-1$
								{
									String erms = "This is a 0 quantity Part.  No NCM tag will be printed."; //$NON-NLS-1$
									IGSMessageBox.showOkMB(this.fieldParent, null, erms, null);
								}
								
								if(ir_codes)
								{
									MFSRwkPrintDialog myRwkPrintD = new MFSRwkPrintDialog(this.fieldParent);
									if (config.containsConfigEntry("FAILTAG")) //$NON-NLS-1$
									{
										myRwkPrintD.setPBFailTagEnabled(true);
									}
									if (config.containsConfigEntry("FAILREPORT")) //$NON-NLS-1$
									{
										myRwkPrintD.setPBFailReportEnabled(true);
									}
									myRwkPrintD.setLocationRelativeTo(this.fieldParent); //~6A
									myRwkPrintD.setVisible(true);

									if (myRwkPrintD.getPressedTag())
									{
										String ircode = ircde + sccde + cccde;

										MFSPrintingMethods.failtag(
												rwkCmp.getCdes(),
												rwkCmp.getInpn(),
												rwkCmp.getInsq(),
												rwkCmp.getInec(),
												this.fieldHeaderRec.getIdss(),
												this.fieldHeaderRec.getMctl(),
												ircode,
												rwkCmp.getPrtd(),
												this.fieldHeaderRec.getMmdl(),
												rwkCmp.getMatp(),
												config.get8CharCell(),
												dat.substring(0, 10),
												this.fieldHeaderRec.getMfgn(),
												rwkCmp.getMcsn(),
												config.get8CharUser() + "  ", //$NON-NLS-1$
												dat.substring(10,18),
												comment,
												1,
												this.fieldParent);
									}
									else if (myRwkPrintD.getPressedReport())
									{
										MFSPrintingMethods.failreport(
												rwkCmp.getCdes(),
												this.fieldHeaderRec.getPrln(),
												this.fieldHeaderRec.getNmbr(),
												this.fieldHeaderRec.getIdss(),
												this.fieldHeaderRec.getMctl(),
												ircode1,
												ircode2,
												ircode3,
												config.get8CharCell(),
												dat.substring(0,10),
												this.fieldHeaderRec.getMfgn(),
												config.getConfigValue("USER").concat("          ").substring(0, 10), //$NON-NLS-1$ //$NON-NLS-2$
												dat.substring(10, 18),
												comment,
												1,
												this.fieldParent);
									}
								} // end if ir Codes logic applied
								
								done = true;
							} //end UPDT_RA successful
						}
						/* ~1C */
						else if (rc != -44)
						{
							IGSMessageBox.showOkMB(this.fieldParent, null, errorString, null);
						} /* end if for resetting client back to idsp = 'a' */
						
					}// end of readyToCallUPDTRA
				}
				else
				{
					done = true;
					//Begin ~11
					if(this.fieldHeaderRec.getDataCollection() != null)                //~14A
					{
						//Check if the remove transaction was canceled before collect new data, and restore the data that wasn't removed
						if(fieldHeaderRec.getDataCollection().getW_partcoll_remove() != null )
						{					
							fieldHeaderRec.getDataCollection().setW_partcoll_remove(null);
						}	
					}					
					//End ~11
				}
			}
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(this.fieldParent, null, null, e);
		}
	}
	
	/**
	 * Executes the subassembly rework logic.
	 * @param cwun the mctl for the child work unit
	 */
	public void subRework(String cwun)
	{
		int rc = 0;
	
		//~4 rwkOp keeps track of at what operation this rework is occuring
		//Set the rwkOp value to the current operation on the directWork screen
		String rwkOp = "";  /*~4*/ //$NON-NLS-1$
		MFSDirectWorkPanel dirWork = this.fieldSource.getParentFrame().getDirectWorkPanel();
		if(dirWork.getHeaderRec().getNmbr().length() > 0)/*~4*/
		{
			rwkOp = dirWork.getHeaderRec().getNmbr();/*~4*/
		}
		else if(this.fieldHeaderRec.getNmbr().length() > 0)/*~4*/
		{
			rwkOp = this.fieldHeaderRec.getNmbr();/*~4*/
		}
		
		StringBuffer input = new StringBuffer();
		input.append("RTV_IP    "); //$NON-NLS-1$
		input.append("           "); //mfgn/idss //$NON-NLS-1$
		input.append(cwun);
		input.append("UC"); //$NON-NLS-1$
		
		MFSTransaction rtv_ip = new MFSFixedTransaction(input.toString());
		rtv_ip.setActionMessage("Retrieving Subassembly Info, Please Wait..."); //$NON-NLS-1$
		MFSComm.getInstance().execute(rtv_ip, this.fieldSource);
		rc = rtv_ip.getReturnCode();

		if (rc == 0)
		{
			/* load the list model */
			MFSComponentListModel myListModel = new MFSComponentListModel();
			MFSHeaderRec myHeadRec = new MFSHeaderRec();
			myListModel.loadListModel(rtv_ip.getOutput(), myHeadRec);
			if(myHeadRec.isCollectRequired())							//~11A
			{															//~11A
				if(myListModel.loadDataCollection(myHeadRec, cwun, this.fieldParent) == 0)
				{
					if(this.fieldHeaderRec.getDataCollection() != null) //~14A 
					{
						if(myHeadRec.getDataCollection().containsMctlComponentCollectedData(cwun))	//~11A
						{
							fieldHeaderRec.getDataCollection().addMctlComponentCollectedData(cwun, myHeadRec.getDataCollection().getMctlComponentCollectedData(cwun));  //~11A
						}
						if(myHeadRec.getDataCollection().containsMctlComponentDataToCollect(cwun))	//~11A
						{
							fieldHeaderRec.getDataCollection().addMctlComponentDataToCollect(cwun, myHeadRec.getDataCollection().getMctlComponentDataToCollect(cwun));  //~11A
						}	
					}					
				}					
			}															//~11A			
	
			MFSRwkSubDialog myRSD = new MFSRwkSubDialog(this.fieldParent, rwkOp, this.fieldHeaderRec, myListModel);  		
			myRSD.setTitle("  Rework Parts in Subassembly - Child Work Unit = " + cwun); //$NON-NLS-1$
			myRSD.setLocationRelativeTo(this.fieldParent); //~6A
			myRSD.setVisible(true);
		}
		else
		{
			IGSMessageBox.showOkMB(this.fieldParent, null, rtv_ip.getErms(), null);
		}
	}
}
