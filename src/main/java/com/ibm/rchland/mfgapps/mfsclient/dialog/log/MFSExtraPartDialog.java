/* @ Copyright IBM Corporation 2006, 2010. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2006-06-29   ~1 31801JM  R Prechel        -Removed unnecessary methods and variables
 *                                           -Changed parent class to MFSLogPartDialog;
 *                                            inherited methods previously defined locally
 * 2007-02-27   ~2 34242JR  R Prechel        -Java 5 version
 * 2007-09-27   ~3 39988KM  R Prechel        -Pass MCTL to RTV_DRPDWN
 * 2008-01-14      39782JM	Martha Luna		 -Changes the name of updtMultilineDisplayString by updtIRDisplayString
 * 2008-01-25	~4 40529SM	Martha Luna		 -send correct output to showPNSNErrorList method when rc is equal to 200
 * 2010-03-20	~5 47595MZ	Ray Perry		 -Shenzhen efficiency: remember last coo
 * 2010-11-02  ~06 49513JM  Toribio H.       -Improve Coo remembering
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.dialog.log;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JLabel;

import mfsxml.MfsXMLParser;

import com.ibm.rchland.mfgapps.client.utils.messagebox.IGSMessageBox;
import com.ibm.rchland.mfgapps.mfsclient.MFSAbstractLogPartDialog;
import com.ibm.rchland.mfgapps.mfsclient.MFSFrame;
import com.ibm.rchland.mfgapps.mfsclient.barcode.MFSBCBarcodeDecoder;
import com.ibm.rchland.mfgapps.mfsclient.barcode.MFSBCPartObject;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSDropDownDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSGenericListDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSTextInputDialog;
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

/**
 * <code>MFSExtraPartDialog</code> is the
 * <code>MFSAbstractLogPartDialog</code> used to log an extra part.
 * @author The MFS Client Development Team
 */
@SuppressWarnings("serial")
public class MFSExtraPartDialog extends MFSAbstractLogPartDialog
{
	//~2A Added extra type constants.
	/** The Debug extra type. */
	public static final String ET_DEBUG = "Debug  "; //$NON-NLS-1$

	/** The Golden extra type. */
	public static final String ET_GOLDEN = "Golden"; //$NON-NLS-1$

	/** The Log Part (F2) <code>JButton</code>. */
	private JButton pbLogPart = createButton("F2 = Log Part"); //$NON-NLS-1$

	/** The Cancel (Esc) <code>JButton</code>. */
	private JButton pbCancel = createButton("Esc = Cancel"); //$NON-NLS-1$

	/** The Scrap (F4) <code>JButton</code>. */
	private JButton pbScrap = createButton("F4 = Scrap"); //$NON-NLS-1$

	/** The NCM (F9) <code>JButton</code>. */
	private JButton pbNCM = createButton("F9 = NCM"); //$NON-NLS-1$

	/** The <code>MFSComponentRec</code> for the current component. */
	private MFSComponentRec fieldComp; //set by constructor

	/** The extra type. */
	private String fieldExtraType = EMPTY_STRING;

	/** The fkit user. */
	private String fieldFkitUser = EMPTY_STRING;

	/** The fkit cell. */
	private String fieldFkitCell = EMPTY_STRING;

	/** Flag used by {@link #logPart()} to indicate if a component was added. */
	private boolean fieldComponentAdded = false;

	/**
	 * Constructs a new <code>MFSExtraPartDialog</code>.
	 * @param parent the <code>MFSFrame</code> that caused this
	 *        <code>MFSExtraPartDialog</code> to be displayed
	 * @param headerRec the <code>MFSHeaderRec</code> for the work unit
	 * @param compRec the <code>MFSComponentRec</code> for the extra part
	 */
	public MFSExtraPartDialog(MFSFrame parent, MFSHeaderRec headerRec, MFSComponentRec compRec)
	{
		//Title set by initDisplay
		super(parent, null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.fieldHeaderRec = headerRec;
		this.fieldComp = compRec;
		createLayout();
		addMyListeners();
	}

	/** Adds this dialog's <code>Component</code>s to the layout. */
	private void createLayout()
	{
		this.pnlButtons.add(this.pbLogPart);
		this.pnlButtons.add(this.pbCancel);

		//~2C Moved code from old configButtons method
		if (this.fieldHeaderRec.getNmbr().equals("FKIT")) //$NON-NLS-1$
		{
			this.pbScrap.setEnabled(false);
			this.pbNCM.setEnabled(false);
		}
		else
		{
			final MFSConfig config = MFSConfig.getInstance();

			if (config.getConfigValue("SCRP").equals("Y")) //$NON-NLS-1$ //$NON-NLS-2$
			{
				this.pnlButtons.add(this.pbScrap);
				this.pbScrap.setEnabled(true);
			}

			if (config.containsConfigEntry("DEFECTLOC")) //$NON-NLS-1$
			{
				this.pnlButtons.add(this.pbNCM);
				this.pbNCM.setEnabled(true);
			}
		}

		createLayout(true);
	}

	/** Adds the listeners to this dialog's <code>Component</code>s. */
	private void addMyListeners()
	{
		this.pbLogPart.addActionListener(this);
		this.pbCancel.addActionListener(this);
		this.pbScrap.addActionListener(this);
		this.pbNCM.addActionListener(this);

		this.pbLogPart.addKeyListener(this);
		this.pbCancel.addKeyListener(this);
		this.pbScrap.addKeyListener(this);
		this.pbNCM.addKeyListener(this); //~2A
		
		this.tfInput.addKeyListener(this);
	}

	/**
	 * Returns the <code>MFSComponentRec</code> for the extra part.
	 * @return the <code>MFSComponentRec</code>
	 */
	public MFSComponentRec getComp()
	{
		return this.fieldComp;
	}

	/**
	 * Returns the value of the component added flag set by {@link #logPart()}.
	 * @return the value of the component added flag
	 */
	public boolean getComponentAdded()
	{
		return this.fieldComponentAdded;
	}

	/** Initializes the appearance of the dialog. */
	public void initDisplay()
	{
		//~2 Use extra type constants
		if (this.fieldExtraType.equals(ET_DEBUG))
		{
			setTitle("Extra Debug Part"); //$NON-NLS-1$
		}
		else if (this.fieldExtraType.equals(ET_GOLDEN))
		{
			setTitle("Extra Golden Part"); //$NON-NLS-1$
		}
		else
		{
			setTitle("Extra Part"); //$NON-NLS-1$
		}

		//~2C Use a String[][] array
		String[][] labelText = new String[7][2];
		for (int s = 0; s < 7; s++)
		{
			labelText[s][0] = EMPTY_STRING;
			labelText[s][1] = EMPTY_STRING;
		}

		int i = 0;
		MFSComponentRec compRec = this.fieldComp;
		if (!compRec.isPnriDoNotCollect())
		{
			labelText[i][0] = PART_NUMBER;
			labelText[i][1] = compRec.getInpn();
			i++;
		}
		if (!compRec.isEcriDoNotCollect())
		{
			labelText[i][0] = EC_NUMBER;
			i++;
		}
		if (!compRec.isCsniDoNotCollect())
		{
			labelText[i][0] = SEQUENCE_NUMBER;
			if (compRec.getInsq().trim().length() > 0)
			{
				labelText[i][1] = compRec.getInsq();
			}
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
			labelText[i][0] = COUNTRY;
			if (compRec.getCooList().length() / 47 == 1)
			{
				labelText[i][1] = compRec.getCooList().substring(0, 2);
			}
			else
			{
				// ~4 - if we are efficient, look up the previously used coo for this part
				if (MFSConfig.getInstance().containsConfigEntry("EFFICIENCYON")) //$NON-NLS-1$
				{
					// must pass the following checks
					if (compRec.isCcaiDoNotCollect() && 	// not collecting ccai
						compRec.isEcriDoNotCollect())  	// not collecting ecri
					{
						if (MFSConfig.getInstance().getCooHash().containsKey(compRec.getItem()))
						{
							labelText[i][1] = MFSConfig.getInstance().getCooHash().get(compRec.getItem());
						}
					}
				}
			}
			i++;
		}

		initTextComponents(labelText); //~2C
	}

	/** Logs the scanned in part information to the <code>MFSComponentRec</code>. */
	public void logPart()
	{
		try
		{
			int rc = 0;
			final MFSConfig config = MFSConfig.getInstance();

			//~1C Use new verifyAllInputCollected method and parsePartInfo method
			if (verifyAllInputCollected())
			{
				MFSPartInformation record = parsePartInfo(false);

				String pn = record.pn;
				String ec = record.ec;
				String sn = record.sn;
				String ca = record.ca;
				String ms = record.ms;
				String co = record.co;
				String wu = record.wu;
				int qt = record.qt;
				boolean overrideQty = record.overrideQty;

				MFSComponentRec compRec = this.fieldComp;
				String errorString = EMPTY_STRING;

				compRec.setRec_changed(true);

				if (!compRec.isPnriDoNotCollect())
				{
					// should be the same, I default the part to what was wanded
					// in first - if not a match error out
					if (!pn.equals(compRec.getInpn()))
					{
						String erms = "Invalid Part Number wanded in!"; //$NON-NLS-1$
						IGSMessageBox.showOkMB(this, null, erms, null);
						rc = 10;
						this.toFront();
						this.tfInput.setText(EMPTY_STRING);
						this.tfInput.requestFocusInWindow();
					}
				}
				else
				{
					compRec.setInpn(compRec.getItem());
				}

				if (rc == 0)
				{
					if (!compRec.isEcriDoNotCollect())
					{
						compRec.setInec(ec);
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

					if (compRec.getMcsn().substring(0, 1).equals("$") //$NON-NLS-1$
							&& (!compRec.isCmtiDoNotCollect()))
					{
						compRec.setMspi(ms.substring(0, 2));
						compRec.setMcsn(ms.substring(2));
					}

					if (compRec.getCooc().equals("  ") && !compRec.isCooiDoNotCollect()) //$NON-NLS-1$
					{
						compRec.setCooc(co);
					}

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
						if (this.fieldHeaderRec.getNmbr().equals("FKIT")) //$NON-NLS-1$
						{
							compRec.setIdsp("I"); //$NON-NLS-1$
						}
						else
						{
							compRec.setIdsp("T"); //$NON-NLS-1$
						}
					}
					compRec.setShtp(" "); //$NON-NLS-1$

					String Fqty = "00000" + fqty; //$NON-NLS-1$
					compRec.setFqty(Fqty.substring(Fqty.length() - 5));

					compRec.updtDisplayString();
					compRec.updtIRDisplayString();
					compRec.updtInstalledParts();

					String type = " "; //$NON-NLS-1$
					if (this.fieldHeaderRec.getNmbr().equals("FKIT")) //$NON-NLS-1$
					{
						if (this.fieldExtraType.substring(0, 1).equals("G")) //$NON-NLS-1$
						{
							type = "5"; //$NON-NLS-1$
						}
						else
						{
							type = "4"; //$NON-NLS-1$
						}
					}

					//we are debug or golden so set cell and user differently
					String user = EMPTY_STRING;
					String cell = EMPTY_STRING;

					if (type != " ") //$NON-NLS-1$
					{
						user = this.fieldFkitUser;
						cell = this.fieldFkitCell;
					}
					else
					{
						user = config.get8CharUser();
						cell = config.get8CharCell();
					}

					StringBuffer data = new StringBuffer(150);
					if (this.fieldHeaderRec.getNmbr().equals("FKIT")) //$NON-NLS-1$
					{
						// call FKADD_RTR with type of debug or golden
						data.append("FKADD_RTR "); //$NON-NLS-1$
						data.append(this.fieldHeaderRec.getMfgn());
						data.append(this.fieldHeaderRec.getIdss());
						data.append(compRec.getMctl());
						data.append(cell);
						data.append(config.get8CharCellType());
						data.append(user);
						data.append(this.fieldHeaderRec.getPrln());
						data.append(this.fieldHeaderRec.getNmbr());
						data.append(this.fieldHeaderRec.getTypz());
						data.append(compRec.getItem());
						data.append("0000"); //$NON-NLS-1$
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
						data.append(type);
						data.append(compRec.getQnty());
					}
					else
					{
						// call EXTRA_CRSU which will write a CR10 record and
						// SU20 record for the new component. It will also
						// return the new CRCT value to the client and flags for
						// downlevel,downlevel assembly,obsolete,scrap and it
						// will also return SU0X messages to be displayed
						data.append("EXTRA_CRSU"); //$NON-NLS-1$
						data.append(this.fieldHeaderRec.getMctl());
						data.append(compRec.getItem());
						data.append(compRec.getQnty());
						data.append(compRec.getInsq());
						data.append(compRec.getInec());
						data.append(compRec.getInca());
						data.append(compRec.getCwun());
						data.append(config.get8CharUser());
						data.append(config.get8CharCell());
						data.append(config.get8CharCellType());
					}

					MFSTransaction transaction = new MFSFixedTransaction(data.toString());
					transaction.setActionMessage("Creating New Component Record, Please Wait..."); //$NON-NLS-1$
					MFSComm.getInstance().execute(transaction, this);
					rc = transaction.getReturnCode();

					if (rc != 0)
					{
						errorString = transaction.getOutput();
					}
					
					/* rc==100, is a warning, we will print the messages, then set reset rc=0 */
					if (rc == 100)
					{
						getParentFrame().getDirectWorkPanel().showPNSNErrorList(transaction.getOutput(), 0);
						rc = 0;
					}

					if (rc == 0)
					{
						if (this.fieldHeaderRec.getNmbr().equals("FKIT")) //$NON-NLS-1$
						{
							MfsXMLParser xmlParser = new MfsXMLParser(transaction.getOutput());
							compRec.setCrct(xmlParser.getField("CRCT")); //$NON-NLS-1$
							compRec.setItms(" "); //$NON-NLS-1$
							compRec.setScrp(" "); //$NON-NLS-1$
							compRec.setDwns(" "); //$NON-NLS-1$
							compRec.setDwnl(" "); //$NON-NLS-1$
							compRec.setPlphtp(EMPTY_STRING);

							//print a goldenParts report
							triggerDebugGoldenReport(compRec.getInpn(), compRec.getInsq());
						}
						else
						{
							final String output = transaction.getOutput();
							compRec.setCrct(output.substring(0, 4));
							compRec.setItms(output.substring(4, 5));
							compRec.setScrp(output.substring(5, 6));
							compRec.setDwns(output.substring(6, 7));
							compRec.setDwnl(output.substring(7, 8));

							if (compRec.getPlphtp().equals(EMPTY_STRING))
							{
								compRec.setPlphtp(output.substring(8, 70));
							}
							else
							{
								compRec.setPlphtp(compRec.getPlphtp() + "\n" //$NON-NLS-1$
										+ output.substring(8, 70));
							}
						}

						//we will use the existance of the Defectloc config entry to trigger NCM logic
						String defectloc = config.getConfigValue("DEFECTLOC") //$NON-NLS-1$
								.concat("                         ").substring(0, 25); //$NON-NLS-1$

						if (compRec.getDwnl().equals("1") //$NON-NLS-1$
								|| compRec.getDwns().equals("1")) //$NON-NLS-1$
						{
							if (!defectloc.substring(0, 9).equals(MFSConfig.NOT_FOUND))
							{
								if (compRec.getQnty().equals("00000")) //$NON-NLS-1$
								{
									//FIXME 34242JR Display Error message?
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

									String comment = "Downlevel Part".concat(blank240.toString()).substring(0, 240); //$NON-NLS-1$

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
							if (this.fieldHeaderRec.getNmbr().equals("FKIT")) //$NON-NLS-1$
							{
								compRec.setPrtd(" "); //$NON-NLS-1$
							}
							else
							{
								compRec.setPrtd("P"); //$NON-NLS-1$
							}
						}

						if (rc == 0)
						{
							compRec.updtDisplayString();
							compRec.updtIRDisplayString();
							compRec.updtInstalledParts();
							this.fieldComp = compRec;
							this.fieldComponentAdded = true;

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
								if (!compRec.isPariDoNotCollect())
								{

									if (compRec.getDwnl().equals("1") //$NON-NLS-1$
											|| compRec.getDwns().equals("1")) //$NON-NLS-1$
									{
										/* no downlevel parts */
										MFSPrintingMethods.downtag(compRec.getInpn(),
												ecValue, cwun, "Downlevel Assembly", 1, //$NON-NLS-1$
												getParentFrame());
									}
									else
									{
										/* no downlevel parts */
										MFSPrintingMethods.downtag(compRec.getInpn(),
												ecValue, cwun, "                  ", 1, //$NON-NLS-1$
												getParentFrame());
									}
								}//end of pari check
							}
							//old logic remains in tact
							else
							{
								if ((!compRec.isPariDoNotCollect())
										|| compRec.getDwnl().equals("1")) //$NON-NLS-1$
								{
									/* now print the DownLevel Tag if applicable */
									if (compRec.getDwns().equals("1")) //$NON-NLS-1$
									{
										/* print downlevel subassembly tag */
										MFSPrintingMethods.downtag(compRec.getInpn(),
												ecValue, cwun, "Downlevel Assembly", 1, //$NON-NLS-1$
												getParentFrame());
									}
									else if (compRec.getDwnl().equals("1")) //$NON-NLS-1$
									{
										/* print downlevel subassembly tag */
										MFSPrintingMethods.downtag(compRec.getInpn(),
												ecValue, cwun, "Downlevel Part    ", 1, //$NON-NLS-1$
												getParentFrame());
									}
									else
									{
										/* no downlevel parts */
										MFSPrintingMethods.downtag(compRec.getInpn(),
												ecValue, cwun, "                  ", 1, //$NON-NLS-1$
												getParentFrame());
									}
								}
							}//end of old logic
						}//end of good rc

						this.dispose();
					}//end of good call to extra_crsu
					if (rc == 200)
					{
						this.dispose();																		 // ~4A
						getParentFrame().getDirectWorkPanel().showPNSNErrorList(transaction.getOutput(), 0); // ~4C
						
					}
					else if (rc != 0)
					{
						IGSMessageBox.showOkMB(this, null, errorString, null);

						this.toFront();
						this.tfInput.setText(EMPTY_STRING);
						this.tfInput.requestFocusInWindow();
					}
				}//end good match of scanned in pn
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
			int rc = 0;
			final MFSConfig config = MFSConfig.getInstance();
			boolean finishedNCM = false;

			//~1C Use new verifyAllInputCollected method and parsePartInfo
			// method
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

				MFSComponentRec compRec = this.fieldComp;
				String errorString = EMPTY_STRING;
				if (!compRec.isPnriDoNotCollect())
				{
					// should be the same, I default the part to what was wanded
					// in first - if not a match error out
					if (!pn.equals(compRec.getInpn()))
					{
						String erms = "Invalid Part Number wanded in!"; //$NON-NLS-1$
						IGSMessageBox.showOkMB(this, null, erms, null);

						rc = 10;
						this.toFront();
						this.tfInput.setText(EMPTY_STRING);
						this.tfInput.requestFocusInWindow();
					}
				}
				else
				{
					compRec.setInpn(compRec.getItem());
				}

				if (rc == 0)
				{
					if (!compRec.isEcriDoNotCollect())
					{
						compRec.setInec(ec);
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

					if (compRec.getMcsn().substring(0, 1).equals("$") //$NON-NLS-1$
							&& (!compRec.isCmtiDoNotCollect()))
					{
						compRec.setMspi(ms.substring(0, 2));
						compRec.setMcsn(ms.substring(2));
					}

					if (compRec.getCooc().equals("  ") && !compRec.isCooiDoNotCollect()) //$NON-NLS-1$
					{
						compRec.setCooc(co);
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

						compRec.setIdsp("T"); //$NON-NLS-1$
						compRec.setShtp(" "); //$NON-NLS-1$
						if (compRec.getStatusChange())
						{
							compRec.setStatusChange(false);
						}
					}

					String Fqty = "00000" + fqty; //$NON-NLS-1$
					compRec.setFqty(Fqty.substring(Fqty.length() - 5));

					compRec.updtDisplayString();
					compRec.updtIRDisplayString();
					compRec.updtInstalledParts();

					// call EXTRA_CRSU which will write a CR10 record and SU20
					// record for the new component. It will also return the new
					// CRCT value to the client and flags for
					// downlevel,downlevel assembly,obsolete, scrap and it will
					// also return SU0X messages to be displayed
					StringBuffer data = new StringBuffer(100);
					data.append("EXTRA_CRSU"); //$NON-NLS-1$
					data.append(this.fieldHeaderRec.getMctl());
					data.append(compRec.getItem());
					data.append(compRec.getQnty());
					data.append(compRec.getInsq());
					data.append(compRec.getInec());
					data.append(compRec.getInca());
					data.append(compRec.getCwun());
					data.append(config.get8CharUser());
					data.append(config.get8CharCell());
					data.append(config.get8CharCellType());

					MFSTransaction extra_crsu = new MFSFixedTransaction(data.toString());
					extra_crsu.setActionMessage("Creating New Component Record, Please Wait..."); //$NON-NLS-1$
					MFSComm.getInstance().execute(extra_crsu, this);
					rc = extra_crsu.getReturnCode();

					if (rc != 0)
					{
						IGSMessageBox.showOkMB(this, null, extra_crsu.getOutput(), null);

						this.toFront();
						this.tfInput.setText(EMPTY_STRING);
						this.tfInput.requestFocusInWindow();
					}
					else
					{
						final String output = extra_crsu.getOutput();
						compRec.setCrct(output.substring(0, 4));
						compRec.setItms(output.substring(4, 5));
						compRec.setScrp(output.substring(5, 6));
						compRec.setDwns(output.substring(6, 7));
						compRec.setDwnl(output.substring(7, 8));

						if (compRec.getPlphtp().equals(EMPTY_STRING))
						{
							compRec.setPlphtp(output.substring(8, 70));
						}
						else
						{
							compRec.setPlphtp(compRec.getPlphtp() + "\n" //$NON-NLS-1$
									+ output.substring(8, 70));
						}
					}
				}
				if (rc == 0)
				{
					//we will use the existance of the Defectloc config entry to trigger NCM logic
					String defectloc = config.getConfigValue("DEFECTLOC") //$NON-NLS-1$
							.concat("                         ").substring(0, 25); //$NON-NLS-1$

					if (!defectloc.substring(0, 9).equals(MFSConfig.NOT_FOUND))
					{
						if (compRec.getQnty().equals("00000")) //$NON-NLS-1$
						{
							//FIXME 34242JR Display Error message?
							//34242JR Start comment out code
							//mfs.ErrorMsgBox msgBox = new mfs.ErrorMsgBox((java.awt.Frame)this.getParent());
							//msgBox.fmtMsg("This is a 0 quantity Part.  No NCM tag will be printed.");
							//msgBox.setLocationRelativeTo(this);
							//34242JR End comment out code

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
								/*~3C Add MCTL to RTV_DRPDWN string */
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
						}// end of non_zero quantity
					}//defectloc not found
					else
					{
						finishedNCM = true;
					}

					if (rc == 0 && finishedNCM)
					{
						compRec.setRec_changed(true);

						compRec.setPrtd("D"); //$NON-NLS-1$
						compRec.updtDisplayString();
						compRec.updtIRDisplayString();
						compRec.updtInstalledParts();

						this.fieldComp = compRec;
						this.fieldComponentAdded = true;

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
							MFSPrintingMethods.downtag(compRec.getInpn(), ecValue, cwun,
									"                  ", 1, getParentFrame()); //$NON-NLS-1$
						}

						dispose();
					}// end of good rc check
				}//end good match of wanded in values
				if (rc != 0)
				{
					IGSMessageBox.showOkMB(this, null, errorString, null);

					this.toFront();
					this.tfInput.setText(EMPTY_STRING);
					this.tfInput.requestFocusInWindow();
				}
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
			MFSComponentRec compRec = this.fieldComp;
			boolean found = false;
			String errorString = MFSBCBarcodeDecoder.INVALID_BARCODE_ERMS;
			int rc = 0; 

			MFSBCBarcodeDecoder barcode = MFSBCBarcodeDecoder.getInstance();
			barcode.setMyBarcodeInput(this.tfInput.getText());
			barcode.setMyBCPartObject(new MFSBCPartObject());

			rc = decodeBarcode(this.fieldHeaderRec, compRec, barcode); //~2C
			found = noInputToCollect(compRec, barcode.getBCMyPartObject()); //~2C

			if (rc == 0 && !found)
			{
				/* PN */
				if (!barcode.getBCMyPartObject().getPN().equals(EMPTY_STRING))
				{
					found |= setValueLabelText(PART_NUMBER, 2, barcode.getBCMyPartObject().getPN()); //~2C ~06
				}
				/* EC */
				if (!(barcode.getBCMyPartObject().getEC().equals(EMPTY_STRING)))
				{
					found |= setValueLabelText(EC_NUMBER, 3, barcode.getBCMyPartObject().getEC()); //~2 ~06
				}
				/* SN */
				if (!(barcode.getBCMyPartObject().getSN().equals(EMPTY_STRING)))
				{
					if (validateSequenceNumber(compRec, barcode.getBCMyPartObject())) {
						found |= setValueLabelText(SEQUENCE_NUMBER, 4, barcode.getBCMyPartObject().getSN()); //~2 ~06	
					}
				}
				/* CA */
				if (!(barcode.getBCMyPartObject().getCA().equals(EMPTY_STRING)))
				{
					found |= setValueLabelText(CARD_ASSEMBLY, 5, barcode.getBCMyPartObject().getCA()); //~2 ~06
				}
				/* MS */
				if (!(barcode.getBCMyPartObject().getMSN().equals(EMPTY_STRING)))
				{
					JLabel msLabel = getValueLabel(MACHINE_SERIAL, 5); //~06
					if (null != msLabel) //~06
					{
						if (validateMachineSerial(compRec, barcode.getBCMyPartObject())) { //~2C
							VRFY_PI vrfyPI = new VRFY_PI(this); //~06
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
								msLabel.setText(barcode.getBCMyPartObject().getMSN()); //~06
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
						RTV_SIGPN rtvSigPN = new RTV_SIGPN(this); //~06
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
						found |= setValueLabelText(CONTROL_NUMBER, 6, barcode.getBCMyPartObject().getWU()); //~06
					} // end good return code from transaction
				}
				/* CO */
				if (!(barcode.getBCMyPartObject().getCO().equals(EMPTY_STRING)))
				{
					found |= setValueLabelText(COUNTRY, 7, barcode.getBCMyPartObject().getCO()); //~2C ~06
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

	/** Invoked when {@link #pbScrap} is selected. */
	private void scrap()
	{
		try
		{
			int rc = 0;
			final MFSConfig config = MFSConfig.getInstance();

			//~1C Use new verifyAllInputCollected method and parsePartInfo method
			if (verifyAllInputCollected() == false)
			{
				rc = 10;
			}

			MFSPartInformation record = parsePartInfo(false);

			String pn = record.pn;
			String ec = record.ec;
			String sn = record.sn;
			String ca = record.ca;
			String ms = record.ms;
			String co = record.co;
			String wu = record.wu;
			int qt = record.qt;
			boolean overrideQty = record.overrideQty;

			MFSComponentRec compRec = this.fieldComp;

			Double cost = new Double(compRec.getCost());

			if (cost.doubleValue() * Integer.parseInt(compRec.getFqty()) > 150.0)
			{
				rc = 10;
				String erms = "Part Cost is greater than $150.  You Cannot Scrap it"; //$NON-NLS-1$
				IGSMessageBox.showOkMB(this, null, erms, null);

				this.toFront();
				this.tfInput.setText(EMPTY_STRING);
				this.tfInput.requestFocusInWindow();
			}
			if (rc == 0)
			{
				compRec.setRec_changed(true);

				if (!compRec.isPnriDoNotCollect())
				{
					// should be the same, I default the part to what was wanded
					// in first - if not a match error out
					if (!pn.equals(compRec.getInpn()))
					{
						String erms = "Invalid Part Number wanded in!"; //$NON-NLS-1$
						IGSMessageBox.showOkMB(this, null, erms, null);

						rc = 10;
						this.toFront();
						this.tfInput.setText(EMPTY_STRING);
						this.tfInput.requestFocusInWindow();
					}
				}
				else
				{
					compRec.setInpn(compRec.getItem());
				}

				if (rc == 0)
				{
					if (!compRec.isEcriDoNotCollect())
					{
						compRec.setInec(ec);
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

					if (compRec.getMcsn().substring(0, 1).equals("$") //$NON-NLS-1$
							&& (!compRec.isCmtiDoNotCollect()))
					{
						compRec.setMspi(ms.substring(0, 2));
						compRec.setMcsn(ms.substring(2));
					}

					if (compRec.getCooc().equals("  ") && !compRec.isCooiDoNotCollect()) //$NON-NLS-1$
					{
						compRec.setCooc(co);
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
					}

					//set prtd to scrap for this part
					compRec.setPrtd("S"); //$NON-NLS-1$

					String Fqty = "00000" + fqty; //$NON-NLS-1$
					compRec.setFqty(Fqty.substring(Fqty.length() - 5));

					compRec.updtDisplayString();
					compRec.updtIRDisplayString();
					compRec.updtInstalledParts();

					//call EXTRA_CRSU which will write a CR10 record and SU20
					// record for the new component. It will also return the new
					// CRCT value to the client and flags for
					// downlevel,downlevel assembly,obsolete, scrap and it will
					// also return SU0X messages to be displayed
					StringBuffer data = new StringBuffer(100);
					data.append("EXTRA_CRSU"); //$NON-NLS-1$
					data.append(this.fieldHeaderRec.getMctl());
					data.append(compRec.getItem());
					data.append(compRec.getQnty());
					data.append(compRec.getInsq());
					data.append(compRec.getInec());
					data.append(compRec.getInca());
					data.append(compRec.getCwun());
					data.append(config.get8CharUser());
					data.append(config.get8CharCell());
					data.append(config.get8CharCellType());

					MFSTransaction extra_crsu = new MFSFixedTransaction(data.toString());
					extra_crsu.setActionMessage("Creating New Component Record, Please Wait..."); //$NON-NLS-1$
					MFSComm.getInstance();
					MFSComm.execute(extra_crsu);
					rc = extra_crsu.getReturnCode();

					if (rc != 0)
					{
						IGSMessageBox.showOkMB(this, null, extra_crsu.getOutput(), null);
					}
					else
					{
						final String output = extra_crsu.getOutput();
						compRec.setCrct(output.substring(0, 4));
						compRec.setItms(output.substring(4, 5));
						compRec.setScrp(output.substring(5, 6));
						compRec.setDwns(output.substring(6, 7));
						compRec.setDwnl(output.substring(7, 8));

						if (compRec.getPlphtp().equals(EMPTY_STRING))
						{
							compRec.setPlphtp(output.substring(8, 70));
						}
						else
						{
							compRec.setPlphtp(compRec.getPlphtp() + "\n" //$NON-NLS-1$
									+ output.substring(8, 70));
						}

						this.fieldComp = compRec;
						this.fieldComponentAdded = true;

						// print out a tag
						if ((!compRec.isPariDoNotCollect()) || compRec.getDwnl().equals("1")) //$NON-NLS-1$
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

							/* now print the DownLevel Tag if applicable */
							if (compRec.getDwns().equals("1")) //$NON-NLS-1$
							{
								/* print downlevel subassembly tag */
								MFSPrintingMethods.downtag(compRec.getInpn(), ecValue,
										cwun, "Downlevel Assembly", 1, getParentFrame()); //$NON-NLS-1$
							}
							else if (compRec.getDwnl().equals("1")) //$NON-NLS-1$
							{
								/* print downlevel subassembly tag */
								MFSPrintingMethods.downtag(compRec.getInpn(), ecValue,
										cwun, "Downlevel Part    ", 1, getParentFrame()); //$NON-NLS-1$
							}
							else
							{
								/* no downlevel parts */
								MFSPrintingMethods.downtag(compRec.getInpn(), ecValue,
										cwun, "                  ", 1, getParentFrame()); //$NON-NLS-1$
							}
						}
						this.dispose();
					}
				}//end of part number check
			}// end of $150.00 check
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(this, null, null, e);

			this.toFront();
			this.tfInput.setText(EMPTY_STRING);
			this.tfInput.requestFocusInWindow();
		}
	}

	/**
	 * Sets the value of the extra type flag.
	 * @param extraType the new value of the extra type flag
	 */
	public void setExtraType(String extraType)
	{
		this.fieldExtraType = extraType;
	}

	//~2A New method
	/**
	 * Sets the fkit user and cell information
	 * @param user the fkit user
	 * @param cell the fkit cell
	 */
	public void setFkitUserCell(String user, String cell)
	{
		this.fieldFkitUser = user;
		this.fieldFkitCell = cell;
	}

	/**
	 * Executes the UPDT_PQ to update the FCSPPQ10.
	 * @param pn the part number
	 * @param sn the sequence number
	 */
	public void triggerDebugGoldenReport(String pn, String sn)
	{
		try
		{
			/* used for UPDT_PQ transaction, 155 astrix and 100 blanks */
			String astrix = "**********************************************************************************************************************************************************"; //$NON-NLS-1$
			String blanks = "                                                                                                    "; //$NON-NLS-1$
			String oprm = (pn + sn + blanks).substring(0, 100);

			final MFSConfig config = MFSConfig.getInstance();

			String cnfgData1 = "DEBUGGOLDEN," //$NON-NLS-1$ 
					+ this.fieldHeaderRec.getNmbr() + "," //$NON-NLS-1$
					+ this.fieldHeaderRec.getPrln().trim();

			String value = config.getConfigValue(cnfgData1);
			if (value.equals(MFSConfig.NOT_FOUND))
			{
				String cnfgData2 = "DEBUGGOLDEN," //$NON-NLS-1$
						+ this.fieldHeaderRec.getNmbr() + ",*ALL"; //$NON-NLS-1$
				value = config.getConfigValue(cnfgData2);
			}

			if (!value.equals(MFSConfig.NOT_FOUND))
			{
				String loc_or_remote;
				int first_comma = value.indexOf(","); //$NON-NLS-1$
				int loc_index = value.indexOf(",", first_comma + 1); //$NON-NLS-1$
				if (loc_index == -1)
				{
					/* if no loc found, use L to print locally */
					loc_or_remote = "L"; //$NON-NLS-1$
				}
				else
				{
					loc_or_remote = value.substring(loc_index + 1, loc_index + 2);
				}

				/*
				 * if loc_index is not -1, then we know that no loc or remote
				 * was specified, so just substring to the end
				 */
				if (loc_index == -1)
				{
//					String Sqty = value.substring(first_comma + 1).trim();
				}

				/* there is a loc or remote specified, substring between the comma's */
				else
				{
					/* if there is something between the commas, convert it to an int */
					if (!value.substring(first_comma + 1, loc_index).equals("    ")) //$NON-NLS-1$
					{

					}
				}

				if (loc_or_remote.equals("L") || loc_or_remote.equals("B")) //$NON-NLS-1$ //$NON-NLS-2$
				{
					String erms = "Local printing of the Golden/Debug Report is not supported at this time"; //$NON-NLS-1$
					IGSMessageBox.showOkMB(this, null, erms, null);
				}
				if (loc_or_remote.equals("R") || loc_or_remote.equals("B")) //$NON-NLS-1$ //$NON-NLS-2$
				{
					int rc = 0;
					/* trigger kitting on host */
					StringBuffer data = new StringBuffer(330);
					data.append("UPDT_PQ   "); //$NON-NLS-1$
					data.append(astrix);
					data.append("       "); //$NON-NLS-1$
					data.append("    "); //$NON-NLS-1$
					data.append(this.fieldHeaderRec.getMctl());
					data.append("        "); //$NON-NLS-1$
					data.append("*ALL"); //$NON-NLS-1$
					data.append("00001"); //$NON-NLS-1$
					data.append("KITINGGLD"); //$NON-NLS-1$
					data.append(config.get8CharCell());
					data.append(config.getConfigValue("USER") //$NON-NLS-1$
							.concat("           ").substring(0, 10)); //$NON-NLS-1$
					data.append("G"); //$NON-NLS-1$
					data.append(oprm);

					MFSTransaction updt_pq = new MFSFixedTransaction(data.toString());
					updt_pq.setActionMessage("Updating FCSPPQ10, Please Wait..."); //$NON-NLS-1$
					MFSComm.getInstance().execute(updt_pq, this);
					rc = updt_pq.getReturnCode();

					if (rc != 0)
					{
						IGSMessageBox.showOkMB(this, null, updt_pq.getOutput(), null);
					}
				}
			} // end !value.equals(MFSConfig.NOT_FOUND)
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(this, null, null, e);

			this.toFront();
			this.tfInput.setText(EMPTY_STRING);
			this.tfInput.requestFocusInWindow();
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
			if (source == this.pbLogPart)
			{
				logPart();
			}
			else if (source == this.pbCancel)
			{
				dispose();
			}
			else if (source == this.pbScrap)
			{
				scrap();
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
				JButton button = (JButton) source;
				button.requestFocusInWindow();
				button.doClick();
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
			if (this.pbScrap.isEnabled())
			{
				this.pbScrap.requestFocusInWindow();
				this.pbScrap.doClick();
			}
		}
		else if (keyCode == KeyEvent.VK_F9)
		{
			if (this.pbNCM.isEnabled())
			{
				this.pbNCM.requestFocusInWindow();
				this.pbNCM.doClick();
			}
		}
		else if (keyCode == KeyEvent.VK_ESCAPE)
		{
			this.pbCancel.requestFocusInWindow();
			this.pbCancel.doClick();
		}
	}
}
