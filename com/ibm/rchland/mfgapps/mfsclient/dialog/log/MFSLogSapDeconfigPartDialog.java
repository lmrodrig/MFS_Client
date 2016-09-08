/* @Copyright IBM Corporation 2016. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag Story    Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 08-03-2016      1566093  Luis M.          -Initial Version 
 */
package com.ibm.rchland.mfgapps.mfsclient.dialog.log;


import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;

import com.ibm.rchland.mfgapps.client.utils.messagebox.IGSMessageBox;
import com.ibm.rchland.mfgapps.mfsclient.MFSAbstractLogPartDialog;
import com.ibm.rchland.mfgapps.mfsclient.MFSPartInstructionJPanel;
import com.ibm.rchland.mfgapps.mfsclient.barcode.MFSBCBarcodeDecoder;
import com.ibm.rchland.mfgapps.mfsclient.barcode.MFSBCIndicatorValue;
import com.ibm.rchland.mfgapps.mfsclient.barcode.MFSBCPartObject;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSAcknowledgeInstructionDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSCoaEntryDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSDropDownDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSGenericListDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSTextInputDialog;
import com.ibm.rchland.mfgapps.mfsclient.panel.MFSSapDeconfigPanel; 
import com.ibm.rchland.mfgapps.mfsclient.struct.MFSComponentListModel;
import com.ibm.rchland.mfgapps.mfsclient.struct.MFSComponentRec;
import com.ibm.rchland.mfgapps.mfsclient.struct.MFSHeaderRec;
import com.ibm.rchland.mfgapps.mfsclient.struct.MFSPartInformation;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSConstants;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSPrintingMethods;
import com.ibm.rchland.mfgapps.mfscommon.MFSConfig;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSComm;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSFixedTransaction;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSTransaction;
import com.ibm.rchland.mfgapps.mfscommon.comm.trx.RTV_SIGPN;
import com.ibm.rchland.mfgapps.mfscommon.comm.trx.VRFY_PI;


public class MFSLogSapDeconfigPartDialog extends MFSAbstractLogPartDialog {
	
	/** The Remove log type. */
	public static final String LT_REMOVE = "REMOVE"; //$NON-NLS-1$

	/** The Search Remove log type. */
	public static final String LT_SEARCHREMOVE = "SEARCHREMOVE"; //$NON-NLS-1$

	/** The Process (F2) <code>JButton</code>. */
	private JButton pbProcess = createButton("F2 = Process"); //$NON-NLS-1$

	/** The Cancel (Esc) <code>JButton</code>. */
	private JButton pbCancel = createButton("Esc = Cancel"); //$NON-NLS-1$
	/** The NCM (F9) <code>JButton</code>. */
	private JButton pbNCM = createButton("F9 = NCM"); //$NON-NLS-1$

	/** The <code>MFSDeconfigPanel</code> that uses this dialog. ~12A */
	private MFSSapDeconfigPanel fieldSAPDeconfig; //set by constructor
	
	/** The log type. */
	private String fieldLogType; //set by constructor

	/** The <code>JList</code> that displays the parts. */
	private JList fieldJList; //set by constructor

	/** The <code>Vector</code> of <code>MFSPartInstructionJPanel</code>s. */
	@SuppressWarnings({ "rawtypes" })
	private Vector fieldRowVector = new Vector();

	/** The index of the active row in {@link #fieldRowVector}. */
	private int fieldActiveRow = 0;

	
	/** 
	 * Constructs a new <code>MFSLogDeconfigPartDialog</code>.
	 * @param deconfig the <code>MFSSAPDeconfigPanel</code> that uses this dialog
	 * @param logType the log type
	 * @param headerRec the <code>MFSHeaderRec</code> for the work unit
	 * @param list the <code>JList</code> that displays the parts
	 */
	public MFSLogSapDeconfigPartDialog(MFSSapDeconfigPanel deconfig, String logType,MFSHeaderRec headerRec, JList list) 
	{
		//Title set by initDisplay
		super(deconfig.getParentFrame(), null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.fieldSAPDeconfig = deconfig;
		this.fieldLogType = logType;
		this.fieldHeaderRec = headerRec;
		this.fieldJList = list;
		createLayout();  				
		addMyListeners(); 				
	}

	/** Adds this dialog's <code>Component</code>s to the layout. ~13A*/
	private void createLayout()
	{
		//~4C Moved code from old configButtons method
		final MFSConfig config = MFSConfig.getInstance();
		final boolean prep = (this.fieldHeaderRec.getNmbr().equals("PREP")); //$NON-NLS-1$

		this.pnlButtons.add(this.pbProcess);
		this.pnlButtons.add(this.pbCancel);

		if (config.containsConfigEntry("DEFECTLOC") && !prep) //$NON-NLS-1$
		{
			this.pnlButtons.add(this.pbNCM);
			this.pbNCM.setEnabled(true);
		}
		else
		{
			this.pbNCM.setEnabled(false);
		}

		createLayout(false);
	}

	/** Adds the listeners to this dialog's <code>Component</code>s. ~13A */
	private void addMyListeners()
	{
		this.pbProcess.addActionListener(this);
		this.pbCancel.addActionListener(this);
		this.pbNCM.addActionListener(this);

		this.pbProcess.addKeyListener(this);
		this.pbCancel.addKeyListener(this);
		this.pbNCM.addKeyListener(this);

		this.tfInput.addKeyListener(this);
	}
	//~4A New method
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
	 * Focuses and sets the completion status of the specified
	 * <code>MFSPartInstructionJPanel</code>.
	 * @param pip the <code>MFSPartInstructionJPanel</code>
	 */
	private void handleInstruction(MFSPartInstructionJPanel pip)
	{
		try
		{
			int index = 0;
			boolean found = false;
			MFSComponentRec cmp = null;

			boolean hideMode = false;  //~12A
			
			
			hideMode = this.fieldSAPDeconfig.isHideMode();
			this.fieldSAPDeconfig.scrollToPip(pip);
			
			if (!pip.getInstructionRec().getCompletionStatus().equals("C")) //$NON-NLS-1$
			{
				if (pip.getInstructionRec().getSsVector().size() > 0)
				{
					//we are going to attempt to "highlight" the right side of
					// the pip. We will do that by setting the background
					//color to a blueish color, but we need to remember what
					// color it was initially, so if user decides not to "claim"
					//the instruction, we can put it back.

					pip.changeColor(MFSPartInstructionJPanel.BLUE_ROW_COLOR);

					if (pip.getIsNonPartInstruction())
					{
						//we are hidden and this instruction is not a mandatory
						// one, so we will flag it as complete
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
							if (!cmp.getMlri().equals("0") //$NON-NLS-1$
									&& !cmp.getMlri().equals(" ") //$NON-NLS-1$
									&& (cmp.getIdsp().equals("A") //$NON-NLS-1$
											|| cmp.getIdsp().equals("X") //$NON-NLS-1$ 
											|| cmp.getIdsp().equals("R"))) //$NON-NLS-1$
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
								ackD.setLocationRelativeTo(this); //~6A
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
			IGSMessageBox.showOkMB(this, null, null, e);
		}
	}

	/** Initializes the appearance of the dialog. */
	public void initDisplay()
	{
		//~4 Use log type constant
		if (this.fieldLogType.equals(LT_SEARCHREMOVE))
		{
			setTitle("  Search Remove"); //$NON-NLS-1$
		}
		else
		{
			setTitle("  Remove Part"); //$NON-NLS-1$
		}

		//~4C Use a String[][] array
		String[][] labelText = new String[7][2];
		for (int s = 0; s < 7; s++)
		{
			labelText[s][0] = EMPTY_STRING; 
			labelText[s][1] = EMPTY_STRING;
		}

		int i = 0;
		MFSComponentRec compRec = (MFSComponentRec) this.fieldJList.getSelectedValue();
		if (!compRec.isPnriDoNotCollect())
		{
			labelText[i][0] = (PART_NUMBER);
			i++;
		}
		if (!compRec.isEcriDoNotCollect())
		{
			labelText[i][0] = (EC_NUMBER);
			i++;
		}
		if (!compRec.isCsniDoNotCollect() && !compRec.getCsni().equals("C")) //$NON-NLS-1$
		{
			labelText[i][0] = (SEQUENCE_NUMBER);
			i++;
		}
		if (!compRec.isCcaiDoNotCollect())
		{
			labelText[i][0] = (CARD_ASSEMBLY);
			i++;
		}
		if (!compRec.isCmtiDoNotCollect())
		{
			labelText[i][0] = (MACHINE_SERIAL);
			i++;
		}
		/* don't want to collect control numbers here */
		if (!compRec.isCooiDoNotCollect())
		{
			labelText[i][0] = (COUNTRY);
			i++;
		}

		initTextComponents(labelText); //~4C
	}

	/** Logs the scanned in part information to the <code>MFSComponentRec</code>. */
	public void logPart()
	{
		try
		{
			/* Perform the logPart method. */
			int rc = 0;
			int coa_okay = 0;/* 31138JM */

			//~3C Use new verifyAllInputCollected method and parsePartInfo method
			if (verifyAllInputCollected())
			{
				MFSPartInformation record = parsePartInfo(true);

				String pn = record.pn;
				String ec = record.ec;
				String sn = record.sn;
				String ca = record.ca;
				int qt = record.qt;
				boolean overrideQty = record.overrideQty;

				MFSComponentRec compRec = (MFSComponentRec) this.fieldJList.getSelectedValue();
				String errorString = EMPTY_STRING;

				if (compRec.getCsni().equals("C")) //$NON-NLS-1$
				{
					/* now pop up the CF report control number validation screen */
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

				if (coa_okay == 0)
				{
					if (!compRec.isEcriDoNotCollect() && !compRec.getInec().equals(ec))
					{
						rc = 10;
						errorString = "Invalid EC Number"; //$NON-NLS-1$
					}
					if (!compRec.isPnriDoNotCollect() && !compRec.getInpn().equals(pn))
					{
						rc = 10;
						errorString = "Invalid Part Number"; //$NON-NLS-1$
					}
					if (!compRec.isCsniDoNotCollect() && !compRec.getInsq().equals(sn))
					{
						rc = 10;
						errorString = "Invalid Serial Number"; //$NON-NLS-1$
					}
					if (!compRec.isCcaiDoNotCollect() && !compRec.getInca().equals(ca))
					{
						rc = 10;
						errorString = "Invalid Card Assembly Number"; //$NON-NLS-1$
					}

					/* ~1 */
					if (Integer.parseInt(compRec.getFqty()) < qt
							&& !this.fieldHeaderRec.getNmbr().equals("PREP")) //$NON-NLS-1$
					{
						rc = 10;
						errorString = "Invalid Part Quantity"; //$NON-NLS-1$
					}

					if (rc == 0)
					{
						//~5D Removed CHCK_PLUG transaction call
						final MFSConfig config = MFSConfig.getInstance();
					
						//we will use the existance of the Defectloc config entry to trigger NCM logic
						String defectloc = config.getConfigValue("DEFECTLOC") //$NON-NLS-1$
								.concat("                         ").substring(0, 25); //$NON-NLS-1$

						if (compRec.getDwnl().equals("1") //$NON-NLS-1$
								|| compRec.getDwns().equals("1") //$NON-NLS-1$
								|| compRec.getExceededPlugs()) 
						{
							if (!defectloc.substring(0, 9).equals(MFSConfig.NOT_FOUND))
							{
								if (compRec.getQnty().equals("00000")) //$NON-NLS-1$
								{
									//34242JR Display Error message?
									//34242JR Start comment out code
									//mfs.ErrorMsgBox msgBox = new mfs.ErrorMsgBox((java.awt.Frame)this.getParent());
									//msgBox.fmtMsg("This is a 0 quantity Part.  No NCM tag will be printed.");
									//msgBox.setLocationRelativeTo(this);
									//34242JR End comment out code
								}
								else
								{
									String ncmQty = "00000" + qt; //$NON-NLS-1$
									ncmQty = ncmQty.substring(ncmQty.length() - 5);
									String ecvalue;
									if (compRec.getInec().length() != 0)
									{
										ecvalue = compRec.getInec();
									}
									else if (compRec.getInca().length() != 0)
									{
										ecvalue = compRec.getInca();
									}
									else
									{
										ecvalue = "            "; //$NON-NLS-1$
									}

									String defectType = "Downlevel Part                      "; //$NON-NLS-1$
									StringBuffer blank240 = new StringBuffer(240);
									for (int i = 0; i < 240; i++)
									{
										blank240.append(" "); //$NON-NLS-1$
									}
									
									//~5C Changed comment from "Downlevel Part" to compRec.getPhrase()
									//~7C Changed comment to "Part exceed plug count" 
									//    if compRec.getExceededPlugs() is true
									String comment;
									if(compRec.getExceededPlugs())
									{
										comment = "Part exceed plug count".concat(blank240.toString()).substring(0, 240); //$NON-NLS-1$
									}
									else
									{
										comment = compRec.getPhrase().concat(blank240.toString()).substring(0, 240);
									}
										

									/* build NCM_INTF string */
									StringBuffer data1 = new StringBuffer(704);
									data1.append("NCM_INTF  "); //$NON-NLS-1$
									data1.append("C"); //$NON-NLS-1$
									data1.append("J"); //$NON-NLS-1$
									data1.append(compRec.getInpn());
									data1.append(compRec.getInsq());
									data1.append("  "); //$NON-NLS-1$
									data1.append(" "); //$NON-NLS-1$
									data1.append(ecvalue);
									data1.append(this.fieldHeaderRec.getMctl());
									data1.append(this.fieldHeaderRec.getMatp());
									data1.append(this.fieldHeaderRec.getMmdl());
									data1.append(this.fieldHeaderRec.getMfgn());
									data1.append(this.fieldHeaderRec.getIdss());
									data1.append("   "); //$NON-NLS-1$
									data1.append(this.fieldHeaderRec.getMcsn());
									data1.append(defectloc);
									data1.append(" "); //$NON-NLS-1$
									data1.append("      "); //$NON-NLS-1$
									data1.append("      "); //$NON-NLS-1$
									data1.append("                         "); //$NON-NLS-1$
									data1.append("10"); //$NON-NLS-1$
									data1.append(compRec.getQnty());
									data1.append("        "); //$NON-NLS-1$
									data1.append("Normal Part       "); //$NON-NLS-1$
									data1.append("Y"); //$NON-NLS-1$
									data1.append(defectType);
									data1.append("        "); //$NON-NLS-1$
									data1.append("        "); //$NON-NLS-1$
									data1.append("        "); //$NON-NLS-1$
									data1.append("        "); //$NON-NLS-1$
									data1.append("        "); //$NON-NLS-1$
									data1.append("        "); //$NON-NLS-1$
									data1.append("        "); //$NON-NLS-1$
									data1.append("        "); //$NON-NLS-1$
									data1.append("        "); //$NON-NLS-1$
									data1.append("        "); //$NON-NLS-1$
									data1.append("        "); //$NON-NLS-1$
									data1.append("    "); //$NON-NLS-1$
									data1.append("    "); //$NON-NLS-1$
									data1.append("        "); //$NON-NLS-1$
									data1.append("    "); //$NON-NLS-1$
									data1.append(comment);
									data1.append(this.fieldHeaderRec.getNmbr());
									data1.append("        "); //$NON-NLS-1$
									data1.append("    "); //$NON-NLS-1$
									data1.append("          "); //$NON-NLS-1$
									data1.append("        "); //$NON-NLS-1$
									data1.append("    "); //$NON-NLS-1$
									data1.append(config.get8CharUser());
									data1.append("        "); //$NON-NLS-1$
									data1.append("                       "); //$NON-NLS-1$

									MFSTransaction ncm_intf = new MFSFixedTransaction(data1.toString());
									ncm_intf.setActionMessage("Executing NCM logic, Please Wait..."); //$NON-NLS-1$
									MFSComm.getInstance().execute(ncm_intf, this);
									rc = ncm_intf.getReturnCode();

									if (rc != 0)
									{
										errorString = ncm_intf.getOutput();
									}
									else
									{
										String NCMTagSequence = ncm_intf.getOutput().substring(1, 9);
										compRec.setRejs(NCMTagSequence);

										if (config.containsConfigEntry("NCMTAG")) //$NON-NLS-1$
										{
											MFSPrintingMethods.ncmtag(NCMTagSequence, 1, getParentFrame());
										}
										if (config.containsConfigEntry("NCMSHEET")) //$NON-NLS-1$
										{
											MFSPrintingMethods.ncmsheet(NCMTagSequence, 1, getParentFrame());
										}
										if (config.containsConfigEntry("NCMBIGTAG")) //$NON-NLS-1$
										{
											MFSPrintingMethods.ncmbigtag(NCMTagSequence, 1, getParentFrame());
										}
									}
								}// end of nonzero quantity
							} // end of defectloc configured
							if (rc == 0)
							{
								compRec.setPrtd("D"); //$NON-NLS-1$
							}

						}//end if downlevel part or sub
						else
						{
							compRec.setPrtd("P"); //$NON-NLS-1$
						}
						
						
						if((rc == 0) && (this.fieldHeaderRec.getDataCollection() != null) && (fieldHeaderRec.isCollectRequired()))
						{																	
							if(fieldHeaderRec.getDataCollection().containsComponentCollection(compRec.getMctl(),compRec.getCrct(),MFSPartDataCollectDialog.COLL_REMOVE))
							{																
								MFSPartDataCollectDialog collectDialog;						
								collectDialog = new MFSPartDataCollectDialog(this.getParentFrame(), this.fieldHeaderRec, MFSPartDataCollectDialog.COLL_REMOVE, compRec.getCrct(),compRec.getMctl()); 					
								rc = collectDialog.getFieldReturnCode();
							}																	
						} 
						
						if (rc == 0)
						{
							compRec.setRec_changed(true);
							//~5A Display message if loaner
							if(compRec.getLoanerPart())
							{
								String msg = "Apply Loaner Sticker - Was a Loaner"; //$NON-NLS-1$
								IGSMessageBox.showOkMB(this, null, msg, null);
							}

							int fqty = Integer.parseInt(compRec.getFqty()) - qt;

							if (config.containsConfigEntry("PARTIALQUANTITY") == false && !overrideQty) //$NON-NLS-1$
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
								if (!this.fieldHeaderRec.getNmbr().equals("PREP")) //$NON-NLS-1$
								{
									compRec.setIdsp("T"); //$NON-NLS-1$
								}
								else
								{
									compRec.setIdsp("D"); //$NON-NLS-1$
								}
								compRec.setShtp(" "); //$NON-NLS-1$
								if (compRec.getStatusChange())
								{
									compRec.setStatusChange(false);
								}
							}

							String Fqty = "00000" + fqty; //$NON-NLS-1$
							compRec.setFqty(Fqty.substring(Fqty.length() - 5));

							compRec.updtDisplayString();
							compRec.updtInstalledParts();
							compRec.updtIRDisplayString();

							String ecValue;
							if (!compRec.isCcaiDoNotCollect())
							{
								ecValue = compRec.getInca();
							}
							else
							{
								ecValue = compRec.getInec();
							}

							String cwun;
							if (!compRec.getCwun().equals(EMPTY_STRING))
							{
								cwun = compRec.getCwun();
							}
							else
							{
								cwun = "        "; //$NON-NLS-1$
							}

							//if defectloc is found, only print tags for assemblies
							if (!defectloc.substring(0, 9).equals(MFSConfig.NOT_FOUND))
							{
								if (!compRec.isPariDoNotCollect()
										&& !compRec.getPari().equals("D")) //$NON-NLS-1$
								{

									if (compRec.getDwnl().equals("1") //$NON-NLS-1$
											|| compRec.getDwns().equals("1")) //$NON-NLS-1$
									{
										/* no downlevel parts */
										MFSPrintingMethods.downtag(
												compRec.getInpn(), ecValue, cwun,
												"Downlevel Assembly", 1, //$NON-NLS-1$
												getParentFrame());
									}
									else
									{
										/* no downlevel parts */
										MFSPrintingMethods.downtag(
												compRec.getInpn(), ecValue, cwun,
												"                  ", 1, //$NON-NLS-1$
												getParentFrame());
									}
								}//end of pari check
							}
							else
							// old logic remains in tact
							{
								/* skip for 'D' pari == Drawers ~1 */
								if ((!compRec.isPariDoNotCollect() && !compRec
										.getPari().equals("D")) //$NON-NLS-1$
										|| compRec.getDwnl().equals("1")) //$NON-NLS-1$
								{
									/* now print the DownLevel Tag if applicable */
									if (compRec.getDwns().equals("1")) //$NON-NLS-1$
									{
										/* print downlevel subassembly tag */
										MFSPrintingMethods.downtag(
												compRec.getInpn(), ecValue, cwun,
												"Downlevel Assembly", 1, //$NON-NLS-1$
												getParentFrame());
									}
									else if (compRec.getDwnl().equals("1")) //$NON-NLS-1$
									{
										/* print downlevel subassembly tag */
										MFSPrintingMethods.downtag(
												compRec.getInpn(), ecValue, cwun,
												"Downlevel Part    ", 1, //$NON-NLS-1$
												getParentFrame());
									}
									else
									{
										/* no downlevel parts */
										MFSPrintingMethods.downtag(
												compRec.getInpn(), ecValue, cwun,
												"                  ", 1, //$NON-NLS-1$
												getParentFrame());
									}
								}
							}

							boolean found = false;
							boolean foundNPI = false;

							MFSComponentRec next;

							// update instruction for this row - update to
							// partial completion or total completion
							handleInstruction(getRowVectorElementAt(this.fieldActiveRow));
							int row = this.fieldActiveRow;

							//now loop thru the current row to see what we've got left in this part list
							//if none found skip down to the next row adn check that row for more parts to be added
							//if we run into a non-part instruction we will prompt the builder to complete it if we
							//are in autolog. Otherwise we will stop right there.
							MFSPartInstructionJPanel pip = getRowVectorElementAt(row);
							JList tempList = pip.getPNList();
							MFSComponentListModel tempLm = pip.getPNListModel();
							int index = tempList.getSelectedIndex();

							while (index < tempLm.size() && !found)
							{
								next = tempLm.getComponentRecAt(index);
								if (next.getIdsp().equals("R") //$NON-NLS-1$
										|| (this.fieldHeaderRec.getNmbr().equals("FKIT") //$NON-NLS-1$
												&& next.getIdsp().equals("I"))) //$NON-NLS-1$
								{
									found = true;
								}
								else
								{
									index++;
								}
							}

							//start at active row and move along to end of rows
							if (!found) // search entire list
							{
								index = 0;
								while (row < this.fieldRowVector.size() && !found && !foundNPI)
								{
									MFSPartInstructionJPanel pipAtRow = getRowVectorElementAt(row);
									if (!pipAtRow.getIsNonPartInstruction())
									{
										index = 0;
										tempList = pipAtRow.getPNList();
										tempLm = pipAtRow.getPNListModel();

										while (index < tempLm.size() && !found)
										{
											next = tempLm.getComponentRecAt(index);

											if (next.getIdsp().equals("R") //$NON-NLS-1$
													&& !next.getQnty().equals("00000")) //$NON-NLS-1$
											{
												found = true;
											}
											else
											{
												index++;
											}
										}
									}//end of part list

									// we are progressing to a non-part row. If we are autolog, we will prompt
									// user to fly thru it otherwise we will stop here
									else
									{
										if (!pipAtRow.getInstructionRec().getCompletionStatus().equals("C")) //$NON-NLS-1$
										{
											foundNPI = true;
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
									MFSPartInstructionJPanel pipAtRow = getRowVectorElementAt(row);
									if (!pipAtRow.getIsNonPartInstruction())
									{
										index = 0;
										tempList = pipAtRow.getPNList();
										tempLm = pipAtRow.getPNListModel();

										while (index < tempLm.size() && !found)
										{
											next = tempLm.getComponentRecAt(index);

											if (next.getIdsp().equals("R") //$NON-NLS-1$
													&& !next.getQnty().equals("00000")) //$NON-NLS-1$
											{
												found = true;
											}
											else
											{
												index++;
											}
										}
									}//end of part list

									// we are progressing to a non-part row. If we are autolog, we will prompt
									// user to fly thru it - otherwise we will stop here
									else
									{
										if (!pipAtRow.getInstructionRec().getCompletionStatus().equals("C")) //$NON-NLS-1$
										{
											foundNPI = true;
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
								this.fieldActiveRow = row;
								this.fieldJList.clearSelection();
								dispose();
							}
							else if (found)
							{
								/* ~1 make sure we set the activeRow */
								this.fieldActiveRow = row;
								this.fieldJList.clearSelection();
								tempList.setSelectedIndex(index);
								tempList.ensureIndexIsVisible(index);
								this.fieldJList = tempList;
								initDisplay();
							}
							else
							{
								dispose();
							}
						}// end of good NCM check
					} // end of good input values
				}//end of coa_okay

				if (rc != 0 && rc != -44)                                          //~09C
				{
					IGSMessageBox.showOkMB(this, null, errorString, null);

					this.toFront();
					this.tfInput.setText(EMPTY_STRING);
					this.tfInput.requestFocusInWindow();
				}
			}//end of all data collected
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(this, null, null, e);

			this.toFront();
			this.tfInput.setText(EMPTY_STRING);
			this.tfInput.requestFocusInWindow();
		}
	}

	/** Invoked when {@link #pbNCM} is selected. */
	private void ncm()
	{
		try
		{
			final MFSConfig config = MFSConfig.getInstance();
			int rc = 0;
			boolean finishedNCM = false;
			int coa_okay = 0; /* 31138JM */
			/* verify all input has been collected */
			if (verifyAllInputCollected())
			{
				MFSPartInformation record = parsePartInfo(true);

				String pn = record.pn;
				String ec = record.ec;
				String sn = record.sn;
				String ca = record.ca;
				int qt = record.qt;
				boolean overrideQty = record.overrideQty;

				MFSComponentRec compRec = (MFSComponentRec) this.fieldJList.getSelectedValue();
				String errorString = EMPTY_STRING;

				if (compRec.getCsni().equals("C")) //$NON-NLS-1$
				{
					/* now pop up the COA collection screen */
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
				if (coa_okay == 0)
				{
					//~5D Delete downlevel check to allow the PF9 function even if the part will be NCM'd
					if (rc == 0)
					{
						if (!compRec.isEcriDoNotCollect()
								&& !compRec.getInec().equals(ec))
						{
							rc = 10;
							errorString = "Invalid EC Number"; //$NON-NLS-1$
						}
						if (!compRec.isPnriDoNotCollect()
								&& !compRec.getInpn().equals(pn))
						{
							rc = 10;
							errorString = "Invalid Part Number"; //$NON-NLS-1$
						}
						if (!compRec.isCsniDoNotCollect()
								&& !compRec.getInsq().equals(sn))
						{
							rc = 10;
							errorString = "Invalid Serial Number"; //$NON-NLS-1$
						}
						if (!compRec.isCcaiDoNotCollect()
								&& !compRec.getInca().equals(ca))
						{
							rc = 10;
							errorString = "Invalid Card Assembly Number"; //$NON-NLS-1$
						}

						if (Integer.parseInt(compRec.getFqty()) < qt)
						{
							rc = 10;
							errorString = "Invalid Part Quantity"; //$NON-NLS-1$
						}
					}
					if (rc == 0)
					{
						if (compRec.getQnty().equals("00000")) //$NON-NLS-1$
						{
							finishedNCM = true;
						}
						else
						{
							MFSGenericListDialog myQuestionJD = new MFSGenericListDialog(
									getParentFrame(), "IBM Product Defect?", //$NON-NLS-1$
									"Is this an IBM Caused Defect?"); //$NON-NLS-1$
							myQuestionJD.setSizeSmall();
							myQuestionJD.loadAnswerListModel("NO YES", 3); //$NON-NLS-1$
							myQuestionJD.setDefaultSelection("NO "); //$NON-NLS-1$
							myQuestionJD.setLocationRelativeTo(this);
							myQuestionJD.setVisible(true);

							if (myQuestionJD.getProceedSelected())
							{
								/*~8C Add MCTL to RTV_DRPDWN string */
								StringBuffer data = new StringBuffer();
								data.append("RTV_DRPDWN"); //$NON-NLS-1$
								data.append("DEFTYPE   "); //$NON-NLS-1$
								data.append(this.fieldHeaderRec.getMctl());

								MFSTransaction rtv_drpdwn = new MFSFixedTransaction(data.toString());
								rtv_drpdwn.setActionMessage("Retrieving Defect Type List, Please Wait..."); //$NON-NLS-1$
								MFSComm.getInstance().execute(rtv_drpdwn, this);
								rc = rtv_drpdwn.getReturnCode();

								if (rc != 0)
								{
									errorString = rtv_drpdwn.getOutput();
								}
								else if (rtv_drpdwn.getOutput().length() == 0)
								{
									rc = 10;
									errorString = "No Defect Types found in NCMPDD10 table!"; //$NON-NLS-1$
								}
								else
								{
									MFSDropDownDialog defTypeJD = new MFSDropDownDialog(getParentFrame());
									defTypeJD.loadAnswerListModel(rtv_drpdwn.getOutput());
									defTypeJD.setLocationRelativeTo(this);
									defTypeJD.setVisible(true);

									if (defTypeJD.getProceedSelected())
									{
										MFSTextInputDialog commentInputJD = new MFSTextInputDialog(
												getParentFrame(),
												"Additional NCM Comments", 240); //$NON-NLS-1$
										commentInputJD.setLocationRelativeTo(this);
										commentInputJD.setVisible(true);

										if (commentInputJD.getPressedEnter())
										{
											String ecvalue;
											if (compRec.getInec().length() != 0)
											{
												ecvalue = compRec.getInec();
											}
											else if (compRec.getInca().length() != 0)
											{
												ecvalue = compRec.getInca();
											}
											else
											{
												ecvalue = "            "; //$NON-NLS-1$
											}

											String defectloc = config.getConfigValue("DEFECTLOC") //$NON-NLS-1$
													.concat("                         ").substring(0, 25); //$NON-NLS-1$
											if (defectloc.substring(0, 9).equals(MFSConfig.NOT_FOUND))
											{
												defectloc = "*DEFAULT                 "; //$NON-NLS-1$
											}

											//make sure comment is 240 long
											StringBuffer cmnt = new StringBuffer(commentInputJD.getComment());
											int cmntLength = cmnt.length();
											for (int i = cmntLength; i < 240; i++)
											{
												cmnt.append(" "); //$NON-NLS-1$
											}

											/* build NCM_INTF string */
											StringBuffer data1 = new StringBuffer(704);
											data1.append("NCM_INTF  "); //$NON-NLS-1$
											data1.append("C"); //$NON-NLS-1$
											data1.append("J"); //$NON-NLS-1$
											data1.append(compRec.getInpn());
											data1.append(compRec.getInsq());
											data1.append("  "); //$NON-NLS-1$
											data1.append(" "); //$NON-NLS-1$
											data1.append(ecvalue);
											data1.append(this.fieldHeaderRec.getMctl());
											data1.append(this.fieldHeaderRec.getMatp());
											data1.append(this.fieldHeaderRec.getMmdl());
											data1.append(this.fieldHeaderRec.getMfgn());
											data1.append(this.fieldHeaderRec.getIdss());
											data1.append("   "); //$NON-NLS-1$
											data1.append(this.fieldHeaderRec.getMcsn());
											data1.append(defectloc);
											data1.append(" "); //$NON-NLS-1$
											data1.append("      "); //$NON-NLS-1$
											data1.append("      "); //$NON-NLS-1$
											data1.append("                         "); //$NON-NLS-1$
											data1.append("10"); //$NON-NLS-1$
											data1.append(compRec.getQnty());
											data1.append("        "); //$NON-NLS-1$
											data1.append("Normal Part       "); //$NON-NLS-1$
											data1.append(myQuestionJD.getSelectedListOption().substring(0, 1));
											data1.append(defTypeJD.getSelectedListOption());
											data1.append("        "); //$NON-NLS-1$
											data1.append("        "); //$NON-NLS-1$
											data1.append("        "); //$NON-NLS-1$
											data1.append("        "); //$NON-NLS-1$
											data1.append("        "); //$NON-NLS-1$
											data1.append("        "); //$NON-NLS-1$
											data1.append("        "); //$NON-NLS-1$
											data1.append("        "); //$NON-NLS-1$
											data1.append("        "); //$NON-NLS-1$
											data1.append("        "); //$NON-NLS-1$
											data1.append("        "); //$NON-NLS-1$
											data1.append("    "); //$NON-NLS-1$
											data1.append("    "); //$NON-NLS-1$
											data1.append("        "); //$NON-NLS-1$
											data1.append("    "); //$NON-NLS-1$
											data1.append(cmnt);
											data1.append(this.fieldHeaderRec.getNmbr());
											data1.append("        "); //$NON-NLS-1$
											data1.append("    "); //$NON-NLS-1$
											data1.append("          "); //$NON-NLS-1$
											data1.append("        "); //$NON-NLS-1$
											data1.append("    "); //$NON-NLS-1$
											data1.append(config.get8CharUser());
											data1.append("        "); //$NON-NLS-1$
											data1.append("                       "); //$NON-NLS-1$

											MFSTransaction ncm_intf = new MFSFixedTransaction(data1.toString());
											ncm_intf.setActionMessage("Executing NCM program, Please Wait..."); //$NON-NLS-1$
											MFSComm.getInstance().execute(ncm_intf, this);
											rc = ncm_intf.getReturnCode();

											if (rc != 0)
											{
												errorString = ncm_intf.getOutput();
											}
											else
											{
												finishedNCM = true;
												String NCMTagSequence = ncm_intf.getOutput().substring(1, 9);
												compRec.setRejs(NCMTagSequence);

												if (config.containsConfigEntry("NCMTAG")) //$NON-NLS-1$
												{
													MFSPrintingMethods.ncmtag(NCMTagSequence, 1, getParentFrame());
												}

												if (config.containsConfigEntry("NCMSHEET")) //$NON-NLS-1$
												{
													MFSPrintingMethods.ncmsheet(NCMTagSequence, 1, getParentFrame());
												}
												if (config.containsConfigEntry("NCMBIGTAG")) //$NON-NLS-1$
												{
													MFSPrintingMethods.ncmbigtag(NCMTagSequence, 1, getParentFrame());
												}
											}
										}//end of pressedEnter on Comment
									}//end of pressedEnter from Defect Type dialog
								}// end of good return from drop down retrieval
							}// end of pressedEnter from IBM defect question

							if (rc != 0)
							{
								IGSMessageBox.showOkMB(this, null, errorString, null);

								this.toFront();
								this.tfInput.setText(EMPTY_STRING);
								this.tfInput.requestFocusInWindow();
							}
						}// end of non_zero quantity

						if (rc == 0 && finishedNCM)
						{
							compRec.setRec_changed(true);

							int fqty = Integer.parseInt(compRec.getFqty()) - qt;

							if (config.containsConfigEntry("PARTIALQUANTITY") == false && !overrideQty) //$NON-NLS-1$
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
								if (!this.fieldHeaderRec.getNmbr().equals("PREP")) //$NON-NLS-1$
								{
									compRec.setIdsp("T"); //$NON-NLS-1$
								}
								else
								{
									compRec.setIdsp("D"); //$NON-NLS-1$
								}
								compRec.setShtp(" "); //$NON-NLS-1$
								if (compRec.getStatusChange())
								{
									compRec.setStatusChange(false);
								}
							}

							String Fqty = "00000" + fqty; //$NON-NLS-1$
							compRec.setFqty(Fqty.substring(Fqty.length() - 5));

							compRec.setPrtd("D"); //$NON-NLS-1$
							compRec.updtDisplayString();
							compRec.updtInstalledParts();
							compRec.updtIRDisplayString();

							if (!compRec.isPariDoNotCollect())
							{
								String ecValue;
								if (!compRec.isCcaiDoNotCollect())
								{
									ecValue = compRec.getInca();
								}
								else
								{
									ecValue = compRec.getInec();
								}

								String cwun;
								if (!compRec.getCwun().equals(EMPTY_STRING))
								{
									cwun = compRec.getCwun();
								}
								else
								{
									cwun = "        "; //$NON-NLS-1$
								}

								/* no downlevel parts */
								MFSPrintingMethods.downtag(compRec.getInpn(), ecValue,
										cwun, "                  ", 1, getParentFrame()); //$NON-NLS-1$

							}

							boolean found = false;
							boolean foundNPI = false;

							MFSComponentRec next;

							// update instruction for this row - update to
							// partial completion or total completion
							handleInstruction(getRowVectorElementAt(this.fieldActiveRow));
							int row = this.fieldActiveRow;

							//now loop thru the current row to see what we've got left in this part list
							//if none found skip down to the next row adn check that row for more parts to be added
							//if we run into a non-part instruction we will prompt the builder to complete it if we
							//are in autolog. Otherwise we will stop right there.
							MFSPartInstructionJPanel pip = getRowVectorElementAt(row);
							JList tempList = pip.getPNList();
							MFSComponentListModel tempLm = pip.getPNListModel();
							int index = tempList.getSelectedIndex();

							while (index < tempLm.size() && !found)
							{
								next = tempLm.getComponentRecAt(index);
								if (next.getIdsp().equals("R") //$NON-NLS-1$
										|| (this.fieldHeaderRec.getNmbr().equals("FKIT") //$NON-NLS-1$ 
												&& next.getIdsp().equals("I"))) //$NON-NLS-1$
								{
									found = true;
								}
								else
								{
									index++;
								}
							}

							//start at active row and move along to end of rows
							if (!found) // search entire list
							{
								index = 0;
								while (row < this.fieldRowVector.size() && !found && !foundNPI)
								{
									MFSPartInstructionJPanel pipAtRow = getRowVectorElementAt(row);
									if (!pipAtRow.getIsNonPartInstruction())
									{
										index = 0;
										tempList = pipAtRow.getPNList();
										tempLm = pipAtRow.getPNListModel();

										while (index < tempLm.size() && !found)
										{
											next = tempLm.getComponentRecAt(index);

											if (next.getIdsp().equals("R") //$NON-NLS-1$
													&& !next.getQnty().equals("00000")) //$NON-NLS-1$
											{
												found = true;
											}
											else
											{
												index++;
											}
										}
									}//end of part list

									// we are progressing to a non-part row. If we are autolog, we will prompt
									// user to fly thru it otherwise we will stop here
									else
									{
										if (!pipAtRow.getInstructionRec().getCompletionStatus().equals("C")) //$NON-NLS-1$
										{
											foundNPI = true;
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
									MFSPartInstructionJPanel pipAtRow = getRowVectorElementAt(row);
									if (!pipAtRow.getIsNonPartInstruction())
									{
										index = 0;
										tempList = pipAtRow.getPNList();
										tempLm = pipAtRow.getPNListModel();

										while (index < tempLm.size() && !found)
										{
											next = tempLm.getComponentRecAt(index);

											if (next.getIdsp().equals("R") //$NON-NLS-1$
													&& !next.getQnty().equals("00000")) //$NON-NLS-1$
											{
												found = true;
											}
											else
											{
												index++;
											}
										}
									}//end of part list

									// we are progressing to a non-part row. If we are autolog, we will prompt
									// user to fly thru it - otherwise we will stop here
									else
									{
										if (!pipAtRow.getInstructionRec().getCompletionStatus().equals("C")) //$NON-NLS-1$
										{
											foundNPI = true;
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
								this.fieldActiveRow = row;
								this.fieldJList.clearSelection();
								dispose();
							}
							else if (found)
							{
							    this.fieldActiveRow = row;                  /*~11A*/
								this.fieldJList.clearSelection();
								tempList.setSelectedIndex(index);
								tempList.ensureIndexIsVisible(index);
								this.fieldJList = tempList;
								initDisplay();
							}
							else
							{
								dispose();
							}
						}// end of good rc check
					}//end good match of wanded in values
				}//end of coa_okay
			}//all input collected
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(this, null, null, e);

			this.toFront();
			this.tfInput.setText(EMPTY_STRING);
			this.tfInput.requestFocusInWindow();
		}
	}

	/** {@inheritDoc} */
	protected void recvInput()
	{
		try
		{
			MFSComponentRec compRec = (MFSComponentRec) this.fieldJList.getSelectedValue();
			boolean found = false;
			String errorString = MFSBCBarcodeDecoder.INVALID_BARCODE_ERMS;
			int rc = 0; 

			MFSBCBarcodeDecoder barcode = MFSBCBarcodeDecoder.getInstance();
			barcode.setMyBarcodeInput(this.tfInput.getText());

			if (this.fieldLogType.equals(LT_SEARCHREMOVE))
			{
				barcode.setMyBCPartObject(new MFSBCPartObject());

				MFSBCIndicatorValue bcIndVal = new MFSBCIndicatorValue();
				bcIndVal.setPNRI("1"); //$NON-NLS-1$
				bcIndVal.setECRI("0"); //$NON-NLS-1$
				bcIndVal.setCSNI("1"); //$NON-NLS-1$
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
				MFSComponentListModel tempLm = null;

				if ((!BCPN.equals(EMPTY_STRING) && !BCPN.equals(compRec.getInpn()))
						|| (BCPN.equals(compRec.getInpn()) && scanQty > recQty)
						|| (BCPN.equals(compRec.getInpn()) && !BCSN.equals(EMPTY_STRING) && !BCSN.equals(compRec.getInsq())))
				{
					MFSComponentRec next;
					found = false;
					int row = this.fieldActiveRow;
					int index = this.fieldJList.getSelectedIndex() + 1;
					tempLm = getRowVectorElementAt(row).getPNListModel();
					while (index < tempLm.size() && !found)
					{
						next = tempLm.getComponentRecAt(index);
						recQty = Integer.parseInt(next.getFqty());
						BCPN = barcode.getBCMyPartObject().getPN();
						BCSN = barcode.getBCMyPartObject().getSN();

						if ((next.getIdsp().equals("R") //$NON-NLS-1$
								&& !next.getQnty().equals("00000") //$NON-NLS-1$
								&& next.getInpn().equals(BCPN) && scanQty <= recQty))
						{
							if (BCSN.equals(EMPTY_STRING))
							{
								found = true;
							}
							else if (BCSN.equals(next.getInsq()))
							{
								found = true;
							}
							else
							{
								index++;
							}
						}
						else
						{
							index++;
						}
					}

					if (!found) // search entire list
					{
						while (row < this.fieldRowVector.size() && !found)
						{
							MFSPartInstructionJPanel pip = getRowVectorElementAt(row);
							if (!pip.getIsNonPartInstruction())
							{
								index = 0;
								tempLm = pip.getPNListModel();

								while (index < tempLm.size() && !found)
								{
									next = tempLm.getComponentRecAt(index);
									recQty = Integer.parseInt(next.getFqty());
									BCPN = barcode.getBCMyPartObject().getPN();
									BCSN = barcode.getBCMyPartObject().getSN();

									if ((next.getIdsp().equals("R") //$NON-NLS-1$
											&& !next.getQnty().equals("00000") //$NON-NLS-1$
											&& next.getInpn().equals(BCPN) && scanQty <= recQty))
									{
										if (BCSN.equals(EMPTY_STRING))
										{
											found = true;
										}
										else if (BCSN.equals(next.getInsq()))
										{
											found = true;
										}
										else
										{
											index++;
										}
									}
									else
									{
										index++;
									}
								}

							}// is a part instruction
							if (!found)
							{
								row++;
							}
						}//end of current Row to end of rows
					}
					//still not found start at the top and work way down to active row
					if (!found)
					{
						row = 0;
						while (row < this.fieldRowVector.size() && !found)
						{
							MFSPartInstructionJPanel pip = getRowVectorElementAt(row);
							if (!pip.getIsNonPartInstruction())
							{
								index = 0;
								tempLm = pip.getPNListModel();

								while (index < tempLm.size() && !found)
								{
									next = tempLm.getComponentRecAt(index);
									recQty = Integer.parseInt(next.getFqty());
									BCPN = barcode.getBCMyPartObject().getPN();
									BCSN = barcode.getBCMyPartObject().getSN();

									if ((next.getIdsp().equals("R") //$NON-NLS-1$
											&& !next.getQnty().equals("00000") //$NON-NLS-1$
											&& next.getInpn().equals(BCPN) && scanQty <= recQty))
									{
										if (BCSN.equals(EMPTY_STRING))
										{
											found = true;
										}
										else if (BCSN.equals(next.getInsq()))
										{
											found = true;
										}
										else
										{
											index++;
										}
									}
									else
									{
										index++;
									}
								}

							}// is a part instruction
							if (!found)
							{
								row++;
							}
						}
					}// end of !found

					if (found)
					{
						JList pipPNList = getRowVectorElementAt(row).getPNList();
						pipPNList.setSelectedIndex(index);
						pipPNList.ensureIndexIsVisible(index);
						compRec = (MFSComponentRec) pipPNList.getSelectedValue();
						this.fieldActiveRow = row;
						this.fieldJList = pipPNList;
						initDisplay();
						if (!compRec.isCsniDoNotCollect()
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
			}

			if (rc == 0)
			{
				barcode.setMyBCPartObject(new MFSBCPartObject());
				rc = decodeBarcode(this.fieldHeaderRec, compRec, barcode); 
			}
			found = noInputToCollect(compRec, barcode.getBCMyPartObject()); 
			if (rc == 0 && !found)
			{
				/* QT */
				if (!barcode.getBCMyPartObject().getQT().equals(EMPTY_STRING))
				{		
					if (null == this.getValueLabel(PART_QUANTITY, 1)) 
					{
						this.pushLabel(PART_QUANTITY, 1); 
					}
					found = this.setValueLabelText(PART_QUANTITY, 1, barcode.getBCMyPartObject().getQT());
				}
				/* PN */
				if (!barcode.getBCMyPartObject().getPN().equals(EMPTY_STRING))
				{
					found |= setValueLabelText(PART_NUMBER, 2, barcode.getBCMyPartObject().getPN()); 
					/* substitute scanned in - blank out country */
					if (!barcode.getBCMyPartObject().getPN().equals(compRec.getItem()))
					{
						clearValueLabelText(COUNTRY); //~4C
					}
				}
				/* EC */
				if (!(barcode.getBCMyPartObject().getEC().equals(EMPTY_STRING)))
				{
					found |= setValueLabelText(EC_NUMBER, 3, barcode.getBCMyPartObject().getEC());  
				}
				/* SN */
				if (!(barcode.getBCMyPartObject().getSN().equals(EMPTY_STRING)))
				{
					if (validateSequenceNumber(compRec, barcode.getBCMyPartObject())) {
						found |= setValueLabelText(SEQUENCE_NUMBER, 4, barcode.getBCMyPartObject().getSN());  	
					}					
				}
				/* CA */
				if (!(barcode.getBCMyPartObject().getCA().equals(EMPTY_STRING)))
				{
					found |= setValueLabelText(CARD_ASSEMBLY, 5, barcode.getBCMyPartObject().getCA());  					
				}
				/* MS */
				if (!(barcode.getBCMyPartObject().getMSN().equals(EMPTY_STRING)))
				{
					JLabel msLabel = getValueLabel(MACHINE_SERIAL, 5);  
					if (null != msLabel) 
					{
						if (validateMachineSerial(compRec, barcode.getBCMyPartObject())) { 						
							VRFY_PI vrfyPI = new VRFY_PI(this); 
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
								msLabel.setText(barcode.getBCMyPartObject().getMSN()); 
								found = true; 
							}
						}
					}
				}
				/* WU */
				if (!(barcode.getBCMyPartObject().getWU().equals(EMPTY_STRING)))
				{
					if (!compRec.isCooiDoNotCollect())
					{
						RTV_SIGPN rtvSigPN = new RTV_SIGPN(this); 
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
						found |= setValueLabelText(CONTROL_NUMBER, 6, barcode.getBCMyPartObject().getWU()); 
					} // end good return code from transaction
				}
				/* CO */
				if (!(barcode.getBCMyPartObject().getCO().equals(EMPTY_STRING)))
				{
					found |= setValueLabelText(COUNTRY, 7, barcode.getBCMyPartObject().getCO()); 
				}
			}
			if ((rc != 0) || (!found && !this.tfInput.getText().equals(EMPTY_STRING)))
			{
				if (this.tfInput.getText().toUpperCase().equals(MFSConstants.LOG_BARCODE))
				{
					this.tfInput.setText(EMPTY_STRING);
					this.pbProcess.requestFocusInWindow();
					this.pbProcess.doClick();
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
		}
		catch (Exception e)
		{
			IGSMessageBox.showEscapeMB(this, null, null, e);
		}

		this.toFront();
		this.tfInput.setText(EMPTY_STRING);
	}

	/**
	 * Sets the row information. Each row is a
	 * <code>MFSPartInstructionJPanel</code>.
	 * @param rowVector the row <code>Vector</code>
	 * @param activeRow the active row
	 */
	@SuppressWarnings({ "rawtypes" })
	public void setRowInfo(Vector rowVector, int activeRow)
	{
		this.fieldRowVector = rowVector;
		this.fieldActiveRow = activeRow;
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
			if (source == this.pbProcess)
			{
				logPart();
			}
			else if (source == this.pbCancel)
			{
				dispose();
			}
			else if (source == this.pbNCM)
			{
				ncm();
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
			this.pbProcess.requestFocusInWindow();
			this.pbProcess.doClick();
		}
		else if (keyCode == KeyEvent.VK_ESCAPE)
		{
			this.pbCancel.requestFocusInWindow();
			this.pbCancel.doClick();
		}
		else if (keyCode == KeyEvent.VK_F9)
		{
			if (this.pbNCM.isEnabled())
			{
				this.pbNCM.requestFocusInWindow();
				this.pbNCM.doClick();
			}
		}
	}

}
