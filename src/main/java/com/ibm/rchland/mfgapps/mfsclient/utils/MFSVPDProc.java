/* @ Copyright IBM Corporation 2003, 2008. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2003-02-04   ~1 22135JM  D. Kloepping     -Added calls to convertbyteData() method,logVPDInfo() method
 * 2003-10-10   ~2 24594DK  E. Engelbert     -Changed logic used to retrieve CCID
 * 2003-10-23   ~3          DJF              -Don't include | because it will throw stuff off by 1
 * 2004-01-08   ~4 25669JM  Dave Fichtinger  -Initial version of logVPDInfoSquadron
 * 2006-06-19   ~5 35246GV  Dave Fichtinger  -Rewrote the VPD Mif parser, because of faulty assumptions about B9 location
 *                                           -General clean up
 * 2007-02-19   ~6 34242JR  R Prechel        -Java 5 version
 * 2008-02-11   ~7 40852PT  R Prechel        -Convert RTV_VD to XML; add voltage parameter to SquadWrap
 * 2008-05-07   ~8 41632PT  Martha Luna      -Redundant flag indicator for VPD Burn Process
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.utils;

import java.awt.Cursor;
import java.io.File;
import java.io.FileOutputStream;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import SmartChipVPD.FuseWrap;
import SmartChipVPD.SmartWrap;
import SmartChipVPD.SmartWrapKeyWord;
import SmartChipVPD.SquadWrap;

import com.ibm.rchland.mfgapps.client.utils.io.IGSFileUtils;
import com.ibm.rchland.mfgapps.client.utils.messagebox.IGSMessageBox;
import com.ibm.rchland.mfgapps.client.utils.xml.IGSXMLTransaction;
import com.ibm.rchland.mfgapps.mfsclient.MFSFrame;
import com.ibm.rchland.mfgapps.mfsclient.struct.MFSHeaderRec;
import com.ibm.rchland.mfgapps.mfscommon.MFSActionable;
import com.ibm.rchland.mfgapps.mfscommon.MFSConfig;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSComm;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSFixedTransaction;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSTransaction;
import common.Comm;

/**
 * <code>MFSVPDProc</code> calls the VPD dll wrapper functions.
 * @author The MFS Client Development Team
 */
public class MFSVPDProc
{
	static final int VPD_BYTE_LENGTH = 2; /* ~5 */
	static final int VPD_KEYWORD_LENGTH = 4; /* ~5 */
	static final int VPD_RECORD_TYPE_LENGTH = 4; /* ~5 */
	static final int VPD_RECORD_NAME_LENGTH = 8; /* ~5 */
	static final int VPD_CCID_LENGTH = 16; /* ~5 */
	
	private String fieldDllMifdata = ""; //$NON-NLS-1$
	private String fieldMifCardLetter = ""; //$NON-NLS-1$
	
	/** The <code>MFSHeaderRec</code> for the work unit. */
	private final MFSHeaderRec fieldHeadRec;
	//~6C Replaced ActionJPanel field with MFSActionable
	/** The <code>MFSActionable</code> from which this <code>MFSVPDProc</code> will be used. */
	private final MFSActionable fieldActionable;
	/** The <code>MFSFrame</code> from which this <code>MFSVPDProc</code> will be used. */
	private final MFSFrame fieldParent;
	/** The <code>MFSVPDRec</code> for the vital product data. */
	private MFSVPDRec fieldVPDRec; //~7A

	//~6C MFSActionable param instead of ActionJPanel; no Configuration param
	/**
	 * Constructs a new <code>MFSVPDProc</code>.
	 * @param headerRec the <code>MFSHeaderRec</code> for the work unit
	 * @param parent the <code>MFSFrame</code> from which this
	 *        <code>MFSVPDProc</code> will be used
	 * @param actionable the <code>MFSActionable</code> from which this
	 *        <code>MFSVPDProc</code> will be used
	 */
	public MFSVPDProc(MFSHeaderRec headerRec, MFSFrame parent, MFSActionable actionable)
	{
		super();
		this.fieldHeadRec = headerRec;
		this.fieldParent = parent;
		this.fieldActionable = actionable;
	}

	//~6A New method
	/**
	 * Executes the specified <code>runnable</code> in a new
	 * <code>Thread</code>, updating {@link #fieldActionable} with
	 * <code>actionMessage</code> until the <code>Thread</code> dies.
	 * @param runnable the <code>Runnable</code> to execute
	 * @param actionMessage the action message to display
	 */
	private void executeRunnable(Runnable runnable, String actionMessage)
	{
		Cursor cursor = this.fieldParent.getCursor();
		this.fieldParent.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		this.fieldActionable.startAction(actionMessage);
		Thread thread = new Thread(runnable);
		thread.start();
		while (thread.isAlive())
		{
			this.fieldActionable.updateAction(actionMessage, -1);
			try
			{
				Thread.sleep(100);
			}
			catch (InterruptedException ie)
			{
				ie.printStackTrace();
			}
		}
		this.fieldActionable.stopAction();
		this.fieldParent.setCursor(cursor);
	}

	//~6A New method
	/**
	 * Displays the status of a successful burn.
	 * @param action the burn action {REBURN, WRITE, UNBURN}
	 */
	private void displayBurnStatusMessageDialog(String action)
	{
		String title;
		ImageIcon iconic;
		JLabel lbl = new JLabel();
		lbl.setFont(MFSConstants.MEDIUM_DIALOG_FONT);
		lbl.setForeground(MFSConstants.PRIMARY_FOREGROUND_COLOR);
		if (action.equals("REBURN")) //$NON-NLS-1$
		{
			title = "Successful Burn";
			lbl.setText("The Card Was Successfully Re - Burned!");
			iconic = new ImageIcon(getClass().getResource("/images/firetruck.gif")); //$NON-NLS-1$
		}
		else if (action.equals("WRITE")) //$NON-NLS-1$
		{
			title = "Successful Burn";
			lbl.setText("The Card Was Successfully Burned!");
			iconic = new ImageIcon(getClass().getResource("/images/animated_flame.gif")); //$NON-NLS-1$
		}
		else
		{
			title = "Successful UnBurn";
			lbl.setText("The Card Was Successfully Unburned!");
			iconic = new ImageIcon(getClass().getResource("/images/fireman.gif")); //$NON-NLS-1$
		}

		JOptionPane.showMessageDialog(this.fieldParent, lbl, title,
				JOptionPane.INFORMATION_MESSAGE, iconic);
	}

	//~5 removed extraneous parms
	/**
	 * Creation date: (12/11/2003 2:16:57 PM)
	 * @param mifdta
	 * @param action
	 * @return the return code
	 */
	private int eightyByteOldDLL(String mifdta, String action)
	{
		final MFSConfig config = MFSConfig.getInstance();
		int rc = 0;
		String errorString = "";
		//PERFORM BURN STUFF HERE
		String insq = this.fieldHeadRec.getSapn();
		/* get the first 2 characters following the YL in the serial */
		String plant_id = insq.substring(2, 4);

		/* Take the data just gathered and slap it together to call the dll */
		/* the order of the data to be sent to the dll is this: */
		/* PLANT_ID|Mifdata in hexidecimal|Port in hexidecimal|COM1|Insq */
		//~7C Use fieldVPDRec and directly set fieldDllMifdata
		if (!action.equals("UNBURN"))
		{
			this.fieldDllMifdata = "000000" + plant_id + "|" + mifdta + "|";
		}

		this.fieldDllMifdata += (this.fieldVPDRec.portHexString + "|");

		/* now add in the burn port field */
		if (this.fieldVPDRec.burnPort.substring(0, 3).equals("LPT"))
		{
			this.fieldDllMifdata += "COM1|";
		}
		else
		{
			this.fieldDllMifdata += (this.fieldVPDRec.burnPort + "|");
		}

		/* get the user for passing to VPD burn function */
		/* dll needs a character array I think */
		String user = config.getConfigValue("USER") + "          ";

		/* finally add in the serial number - after this the dllMifdata will be ready for the dll */
		this.fieldDllMifdata += ("0" + insq.substring(5, 12) + "|");

		/* set up a short that contains a number, i.e. 1001, 1005,..., which */
		/* tells the dll what to do with the card, burn, unburn... */
		int burn_option;
		if (action.equals("WRITE") || action.equals("REBURN"))
		{
			burn_option = 1001;
		}
		else
		{
			burn_option = 1005;
		}

		//Create an instance of a VPD DLL wrapper class,
		//which will be used to perform the burn
		SmartWrap smartie = new SmartWrap(burn_option, user.substring(0, 10),
				this.fieldVPDRec.mifFlags, this.fieldVPDRec.burnPort, this.fieldDllMifdata,
				new String());

		/* make sure dll was loaded correctly */
		errorString = smartie.getErr();
		rc = smartie.getRc();
		if(rc != 0)
		{
			this.fieldParent.setCursor(Cursor.getDefaultCursor());
			IGSMessageBox.showOkMB(this.fieldParent, null, errorString, null);
		}
		else
		{
			if (action.equals("UNBURN")) //$NON-NLS-1$
			{
				//~6C Moved threading and update logic to executeRunnable
				executeRunnable(smartie, "Erasing Card, Please Wait...");
			}
			else
			{
				//~6C Moved threading and update logic to executeRunnable
				executeRunnable(smartie, "Writing to Card, Please Wait...");
			}
			errorString = smartie.getRcStr();
			rc = smartie.getRc();

			if (rc == 0)
			{
				/* get the part number for passing to logVPDinfo function */
				String partNum = "00000" + this.fieldHeadRec.getPrln().substring(0, 7);

				if (action.equals("UNBURN"))
				{
					StringBuffer data2 = new StringBuffer();
					data2.append("LOG_VPDINF");
					data2.append("R");
					data2.append(partNum.trim());
					data2.append(insq.trim());
					data2.append("    ");
					data2.append("          ");
					data2.append("                ");
					data2.append(config.get8CharUser());

					MFSTransaction log_vpdinf = new MFSFixedTransaction(data2.toString());
					log_vpdinf.setActionMessage("Removing VPD info...");
					MFSComm.getInstance().execute(log_vpdinf, this.fieldActionable);
					rc = log_vpdinf.getReturnCode();

					if (rc != 0)
					{
						IGSMessageBox.showOkMB(this.fieldParent, null,
								log_vpdinf.getErms(), null);
					}
				}
				else
				{
					//~5 removed extraneous parms /*~1A*/
					rc = logVPDInfo80Byte('L', partNum, insq, errorString);
				}

				this.fieldParent.setCursor(Cursor.getDefaultCursor());
				
				//~6C Moved message display logic to new method
				displayBurnStatusMessageDialog(action);

				if (action.equals("UNBURN") || action.equals("REBURN"))
				{
					/* Check if we need to print an Unburn Label */
					String cnfgUnburn = "UNBURNLBL,"
							+ this.fieldHeadRec.getPrln().trim();

					String valUnburn = config.getConfigValue(cnfgUnburn);
					if (valUnburn.equals(MFSConfig.NOT_FOUND))
					{
						String cnfgUnburn2 = "UNBURNLBL," + "*ALL";
						valUnburn = config.getConfigValue(cnfgUnburn2);
					}

					if (!valUnburn.equals(MFSConfig.NOT_FOUND))
					{
						int qty = 1;
						if (!valUnburn.equals(""))
						{
							qty = Integer.parseInt(valUnburn);
						}

						if (config.containsConfigEntry("VPDMIF2"))
						{
							MFSPrintingMethods.vpdmif2(this.fieldHeadRec.getMctl(),
									"U", 1, this.fieldParent);
						}
						else
						{
							MFSPrintingMethods.vpdunburn(this.fieldHeadRec.getMctl(),
									this.fieldVPDRec.mifLabelType, qty, this.fieldParent);
						}
					}
				}
				else
				{
					if (config.containsConfigEntry("VPDMIF2"))
					{

						MFSPrintingMethods.vpdmif2(this.fieldHeadRec.getMctl(), "B", 1,
								this.fieldParent);
					}
					else
					{
						/*
						 * now that the card has been successfully burned, use
						 * the field retrieved from the VD10 to determine which
						 * type of label to print, MIF or 11S
						 */
						if (this.fieldVPDRec.mifLabelType.equals("MIF ")
								|| this.fieldVPDRec.mifLabelType.equals("BOTH"))
						{
							MFSPrintingMethods.vpdmif(this.fieldHeadRec.getPrln(),
									this.fieldHeadRec.getMctl(), 1, this.fieldParent);
						}
						if (this.fieldVPDRec.mifLabelType.equals("11S ")
								|| this.fieldVPDRec.mifLabelType.equals("BOTH"))
						{
							if (this.fieldMifCardLetter.trim().equals(""))
							{
								MFSPrintingMethods.vpd11s(this.fieldHeadRec.getPrln(),
										this.fieldHeadRec.getMctl(), "      ", 1,
										this.fieldParent);
							}
							else
							{
								MFSPrintingMethods.vpd11s(this.fieldHeadRec.getPrln(),
										this.fieldHeadRec.getMctl(), "Card "
												+ this.fieldMifCardLetter, 1,
										this.fieldParent);
							}
						}
					}
				}
				// fix the funny cursor problem
				this.fieldParent.setCursor(Cursor.getDefaultCursor());
			}
			else
			{
				this.fieldParent.setCursor(Cursor.getDefaultCursor());
				StringBuffer erms = new StringBuffer();
				erms.append("Error(80 Byte Old DLL): RC - ");
				erms.append(rc);
				erms.append(" ");
				erms.append(errorString);
				IGSMessageBox.showOkMB(this.fieldParent, null,
						erms.toString(), null);
			}
		}// end of good dll load
		return rc;
	}

	/**
	 * Creation date: (12/11/2003 2:16:57 PM)
	 * @param mifdta
	 * @param ecid_hex
	 * @param action
	 * @return the return code
	 */
	private int eightyByteSquadronDLL(String mifdta, String ecid_hex,
										String action)
	{
		final MFSConfig config = MFSConfig.getInstance();
		int rc = 0;
		String errorString = "";

		//~7C Use fieldVPDRec
		String file = writeVPDFile80Byte(mifdta, this.fieldVPDRec.mifPN.substring(5, 12));

		//PERFORM BURN STUFF HERE
		String insq = this.fieldHeadRec.getSapn().substring(0, 12);

		/* tells dll if it is erasing or writing */
		String mode;
		if (action.equals("UNBURN"))
		{
			mode = "ERASE";
		}
		else
		{
			mode = "WRITE";
		}

		/* get the part number for passing to VPD burn function */
		String partNum = "00000" + this.fieldHeadRec.getPrln().substring(0, 7);

		/* build an 11S string */
		String elevens = "11S" + partNum.substring(5, 12) + insq;

		/* figure out the format parm required for the black box dll */
		String format;
		if (this.fieldVPDRec.usage.equals("A"))
		{
			format = "CAS";
		}
		else
		{
			format = "CRS";
		}

		//Create an instance of a VPD DLL wrapper class,
		//which will be used to perform the burn
		SquadWrap squadie = new SquadWrap(mode, file, this.fieldVPDRec.burnPort, elevens,
				format, ecid_hex, partNum, "N", this.fieldVPDRec.voltage);

		/* make sure dll was loaded correctly */
		errorString = squadie.getErr();
		rc = squadie.getRc();
		if (rc != 0)
		{
			this.fieldParent.setCursor(Cursor.getDefaultCursor());
			IGSMessageBox.showOkMB(this.fieldParent, null, errorString, null);
		}
		else
		{
			if (action.equals("UNBURN")) //$NON-NLS-1$
			{
				//~6C Moved threading and update logic to executeRunnable
				executeRunnable(squadie, "Erasing Card, Please Wait...");
			}
			else
			{
				//~6C Moved threading and update logic to executeRunnable
				executeRunnable(squadie, "Writing to Card, Please Wait...");
			}
			errorString = squadie.getRcStr();
			rc = squadie.getRc();

			if (rc == 0)
			{
				/* ~1A , first parm is L for log */
				if (action.equals("UNBURN"))
				{
					/* call the LOG_VPDINF trx */
					StringBuffer data2 = new StringBuffer();
					data2.append("LOG_VPDINF");
					data2.append("R");
					data2.append(partNum.trim());
					data2.append(insq.trim());
					data2.append("    ");
					data2.append("          ");
					data2.append("                ");
					data2.append(config.get8CharUser());

					MFSTransaction log_vpdinf = new MFSFixedTransaction(data2.toString());
					log_vpdinf.setActionMessage("Removing VPD info...");
					MFSComm.getInstance().execute(log_vpdinf, this.fieldActionable);
					rc = log_vpdinf.getReturnCode();

					if (rc != 0)
					{
						IGSMessageBox.showOkMB(this.fieldParent, null,
								log_vpdinf.getErms(), null);
					}
				}
				else
				{/* ~1 */
					rc = logVPDInfoSquadron('L', partNum, insq, errorString); /* ~1A */
				}

				if (rc == 0)
				{
					this.fieldParent.setCursor(Cursor.getDefaultCursor());

					//~6C Moved message display logic to new method
					displayBurnStatusMessageDialog(action);

					if (action.equals("UNBURN") || action.equals("REBURN"))
					{
						/* Check if we need to print an Unburn Label */
						String cnfgUnburn = "UNBURNLBL,"
								+ this.fieldHeadRec.getPrln().trim();

						String valUnburn = config.getConfigValue(cnfgUnburn);
						if (valUnburn.equals(MFSConfig.NOT_FOUND))
						{
							String cnfgUnburn2 = "UNBURNLBL," + "*ALL";
							valUnburn = config.getConfigValue(cnfgUnburn2);
						}

						if (!valUnburn.equals(MFSConfig.NOT_FOUND))
						{
							int qty = 1;
							if (!valUnburn.equals(""))
							{
								qty = Integer.parseInt(valUnburn);
							}

							if (config.containsConfigEntry("VPDMIF2"))
							{
								MFSPrintingMethods.vpdmif2(this.fieldHeadRec.getMctl(),
										"U", 1, this.fieldParent);
							}
							else
							{
								MFSPrintingMethods.vpdunburn(this.fieldHeadRec.getMctl(), 
										this.fieldVPDRec.mifLabelType, qty, this.fieldParent);
							}
						}
					}
					else
					{
						if (config.containsConfigEntry("VPDMIF2"))
						{
							MFSPrintingMethods.vpdmif2(this.fieldHeadRec.getMctl(),
									"B", 1, this.fieldParent);
						}
						else
						{
							/*
							 * now that the card has been successfully burned,
							 * use the field retrieved from the VD10 to
							 * determine the type of label to print, MIF or 11S
							 */
							if (this.fieldVPDRec.mifLabelType.equals("MIF ")
									|| this.fieldVPDRec.mifLabelType.equals("BOTH"))
							{
								MFSPrintingMethods.vpdmif(this.fieldHeadRec.getPrln(),
										this.fieldHeadRec.getMctl(), 1,
										this.fieldParent);
							}
							if (this.fieldVPDRec.mifLabelType.equals("11S ")
									|| this.fieldVPDRec.mifLabelType.equals("BOTH"))
							{
								if (this.fieldMifCardLetter.trim().equals(""))
								{
									MFSPrintingMethods.vpd11s(this.fieldHeadRec.getPrln(), 
											this.fieldHeadRec.getMctl(),
											"      ", 1, this.fieldParent);
								}
								else
								{
									MFSPrintingMethods.vpd11s(this.fieldHeadRec.getPrln(), 
											this.fieldHeadRec.getMctl(),
											"Card " + this.fieldMifCardLetter, 1,
											this.fieldParent);
								}
							}
						}
					}
					// fix the funny cursor problem
					this.fieldParent.setCursor(Cursor.getDefaultCursor());
				}//end of good logVPD return
			}//end of good burn sequence
			else
			{
				this.fieldParent.setCursor(Cursor.getDefaultCursor());

				StringBuffer erms = new StringBuffer();
				erms.append("Error(80 Byte): RC - ");
				erms.append(rc);
				erms.append(" ");
				erms.append(errorString);
				IGSMessageBox.showOkMB(this.fieldParent, null, erms.toString(), null);
			}
		}// end of good smartwrap dll load
		return rc;
	}

	/**
	 * Creation date: (12/11/2003 1:24:26 PM)
	 * @param fuseNum
	 * @return the return code
	 */
	private int fuseWrite(int fuseNum)
	{
		final MFSConfig config = MFSConfig.getInstance();
		int rc = 0;
		String errorString = "";

		String mcmPartNum2 = "MCMPARTNUM";
		String mcmPartNumValue2 = config.getConfigValue(mcmPartNum2);
		if (mcmPartNumValue2.equals(MFSConfig.NOT_FOUND))
		{
			rc = 10;
			String erms = "MCM Part Number not known!  PFS Rule Needs to be Set Up!";
			IGSMessageBox.showOkMB(this.fieldParent, null, erms, null);
		}
		else
		{
			StringBuffer data3 = new StringBuffer();
			data3.append("RTV_FUSE  ");
			data3.append(mcmPartNumValue2.substring(5));
			data3.append(this.fieldHeadRec.getSapn().substring(0, 12));

			MFSTransaction rtv_fuse = new MFSFixedTransaction(data3.toString());
			rtv_fuse.setActionMessage("Retrieving Fuse Data, Please Wait...");
			MFSComm.getInstance().execute(rtv_fuse, this.fieldActionable);
			rc = rtv_fuse.getReturnCode();
			
			if (rc != 0)
			{
				errorString = rtv_fuse.getErms();
				IGSMessageBox.showOkMB(this.fieldParent, null, errorString, null);
			}
			else
			{
				final String rtvFuseOuput = rtv_fuse.getOutput();
				String fus_a = rtvFuseOuput.substring(0, 243) + "0";
				String fus_b = rtvFuseOuput.substring(243, 486) + "0";
				String fus_c = rtvFuseOuput.substring(486, 729) + "0";
				String fus_d = rtvFuseOuput.substring(729) + "0";

				/*
				 * pad the 243 chars out to 320 chars with ASCII zeros - this
				 * will then convert down to 160 bytes
				 */
				String zeros_76 = "0000000000000000000000000000000000000000000000000000000000000000000000000000";
				fus_a += zeros_76;
				fus_b += zeros_76;
				fus_c += zeros_76;
				fus_d += zeros_76;

				int mode = 4001;
				long flagBits = 0x00000080;

				/* call another DLL to load the FUSE data to the card */
				//~7C Use fieldVPDRec
				FuseWrap fusewrapper = new FuseWrap(mode, flagBits, fuseNum,
						this.fieldVPDRec.burnPort, this.fieldVPDRec.portInt, fus_a,
						fus_b, fus_c, fus_d, new String());

				/* make sure dll was loaded correctly */
				errorString = fusewrapper.getErr();
				rc = fusewrapper.getRc();
				if (rc != 0)
				{
					this.fieldParent.setCursor(Cursor.getDefaultCursor());
					IGSMessageBox.showOkMB(this.fieldParent, null, errorString, null);
				}
				else
				{
					//~6C Moved threading and update logic to executeRunnable
					executeRunnable(fusewrapper, "Writing Fuse Data to Card, Please Wait...");
					errorString = fusewrapper.getRcStr();
					rc = fusewrapper.getRc();

					if (rc == 0)
					{
						this.fieldParent.setCursor(Cursor.getDefaultCursor());
						ImageIcon iconic2 = new ImageIcon(getClass().getResource("/images/lightning.gif"));
						JLabel lbl2 = new JLabel();
						lbl2.setFont(MFSConstants.MEDIUM_DIALOG_FONT);
						lbl2.setForeground(MFSConstants.PRIMARY_FOREGROUND_COLOR);
						lbl2.setText("The Fuse Data Was Successfully Written!");
						JOptionPane.showMessageDialog(this.fieldParent, lbl2,
								"Successful Burn", JOptionPane.INFORMATION_MESSAGE,
								iconic2);
					}
					else
					{
						this.fieldParent.setCursor(Cursor.getDefaultCursor());

						StringBuffer erms = new StringBuffer();
						erms.append("Error in LoadFuseData(): RC - ");
						erms.append(rc);
						erms.append(" Error - ");
						erms.append(errorString);
						IGSMessageBox.showOkMB(this.fieldParent, null, erms.toString(), null);
					}
				}//successfully loaded the fusewrap dll
			}// end of good return from RTV_FUSE
		}//end MCMPARTNUM found in config file
		return rc;
	}

	//~5 removed extraneous parms
	/**
	 * Creation date: (12/11/2003 2:16:57 PM)
	 * @param byte_array
	 * @param ecid_hex
	 * @param fuseNum
	 * @param partNum
	 * @param insq
	 * @param elevens
	 * @param action
	 * @return the return code
	 */
	private int keywordOldDLL(byte[] byte_array, String ecid_hex, int fuseNum,
								String partNum, String insq, String elevens, String action)
	{
		final MFSConfig config = MFSConfig.getInstance();
		int rc = 0;
		String errorString = "";

		//PERFORM BURN STUFF HERE

		/* set up a int that contains a number, i.e. 1001, 1005,..., which */
		/* tells the dll what to do with the card, burn, unburn... */
		int burn_option;
		if (action.equals("WRITE") || action.equals("REBURN"))
		{
			burn_option = 1101;
		}
		else
		{
			burn_option = 1105;
		}

		/* get the user for passing to VPD burn function */
		/* dll needs a character array I think */
		String user = config.getConfigValue("USER") + "          ";

		//Create an instance of a VPD DLL wrapper class,
		//which will be used to perform the burn
		//~7C Use fieldVPDRec
		SmartWrapKeyWord smartie = new SmartWrapKeyWord(burn_option,
				user.substring(0, 10), this.fieldVPDRec.mifFlags, partNum, elevens,
				this.fieldVPDRec.burnPort, this.fieldVPDRec.portInt, byte_array,
				this.fieldDllMifdata.length(), ecid_hex, new String());

		/* make sure dll was loaded correctly */
		errorString = smartie.getErr();
		rc = smartie.getRc();
		if (rc != 0)
		{
			this.fieldParent.setCursor(Cursor.getDefaultCursor());
			IGSMessageBox.showOkMB(this.fieldParent, null, errorString, null);
		}
		else
		{
			if (action.equals("UNBURN")) //$NON-NLS-1$
			{
				//~6C Moved threading and update logic to executeRunnable
				executeRunnable(smartie, "Erasing Card, Please Wait...");
			}
			else
			{
				//~6C Moved threading and update logic to executeRunnable
				executeRunnable(smartie, "Writing to Card, Please Wait...");
			}
			errorString = smartie.getRcStr();
			rc = smartie.getRc();

			if (rc == 0)
			{
				/* ~1A , first parm is L for log */
				if (action.equals("UNBURN"))
				{
					StringBuffer data2 = new StringBuffer();
					data2.append("LOG_VPDINF");
					data2.append("R");
					data2.append(partNum.trim());
					data2.append(insq.trim());
					data2.append("    ");
					data2.append("          ");
					data2.append("                ");
					data2.append(config.get8CharUser());

					MFSTransaction log_vpdinf = new MFSFixedTransaction(data2.toString());
					log_vpdinf.setActionMessage("Removing VPD info...");
					MFSComm.getInstance().execute(log_vpdinf, this.fieldActionable);
					rc = log_vpdinf.getReturnCode();

					if (rc != 0)
					{
						IGSMessageBox.showOkMB(this.fieldParent, null,
								log_vpdinf.getErms(), null);
					}
				}
				else
				{
					//~5 removed extraneous pamrs
					rc = logVPDInfo('L', partNum, insq, errorString);
				}

				if (rc == 0)
				{
					this.fieldParent.setCursor(Cursor.getDefaultCursor());
					
					//~6C Moved message display logic to new method
					displayBurnStatusMessageDialog(action);

					if (action.equals("UNBURN") || action.equals("REBURN"))
					{
						/* Check if we need to print an Unburn Label */
						String cnfgUnburn = "UNBURNLBL,"
								+ this.fieldHeadRec.getPrln().trim();

						String valUnburn = config.getConfigValue(cnfgUnburn);
						if (valUnburn.equals(MFSConfig.NOT_FOUND))
						{
							String cnfgUnburn2 = "UNBURNLBL," + "*ALL";
							valUnburn = config.getConfigValue(cnfgUnburn2);
						}

						if (!valUnburn.equals(MFSConfig.NOT_FOUND))
						{
							int qty = 1;
							if (!valUnburn.equals(""))
							{
								qty = Integer.parseInt(valUnburn);
							}
							if (config.containsConfigEntry("VPDMIF2"))
							{

								MFSPrintingMethods.vpdmif2(this.fieldHeadRec.getMctl(),
										"U", 1, this.fieldParent);
							}
							else
							{
								MFSPrintingMethods.vpdunburn(this.fieldHeadRec.getMctl(), 
										this.fieldVPDRec.mifLabelType, qty, this.fieldParent);
							}
						}
					}//reburn printing

					else
					{
						if (config.containsConfigEntry("VPDMIF2"))
						{

							MFSPrintingMethods.vpdmif2(this.fieldHeadRec.getMctl(),
									"B", 1, this.fieldParent);
						}
						else
						{
							/*
							 * now that the card has been successfully burned,
							 * use the field retrieved from the VD10 to
							 * determine the type of label to print, MIF or 11S
							 */
							if (this.fieldVPDRec.mifLabelType.equals("MIF ")
									|| this.fieldVPDRec.mifLabelType.equals("BOTH"))
							{
								MFSPrintingMethods.vpdmif(this.fieldHeadRec.getPrln(),
										this.fieldHeadRec.getMctl(), 1,
										this.fieldParent);
							}
							if (this.fieldVPDRec.mifLabelType.equals("11S ")
									|| this.fieldVPDRec.mifLabelType.equals("BOTH"))
							{
								if (this.fieldMifCardLetter.trim().equals(""))
								{
									MFSPrintingMethods.vpd11s(this.fieldHeadRec.getPrln(), 
											this.fieldHeadRec.getMctl(),
											"      ", 1, this.fieldParent);
								}
								else
								{
									MFSPrintingMethods.vpd11s(this.fieldHeadRec.getPrln(), 
											this.fieldHeadRec.getMctl(),
											"Card " + this.fieldMifCardLetter, 1,
											this.fieldParent);
								}
							}
						}
					}//end of burn label printing

					// fix the funny cursor problem
					this.fieldParent.setCursor(Cursor.getDefaultCursor());

					// only execute this logic if FUSE data is needed
					if (fuseNum > 0)
					{
						rc = fuseWrite(fuseNum);
					}// end of fuseNum > 0
				}//end of good logVPD return
			}//end of good burn sequence
			else
			{
				this.fieldParent.setCursor(Cursor.getDefaultCursor());
				StringBuffer erms = new StringBuffer();
				erms.append("Error(Keyword Old DLL): RC - ");
				erms.append(rc);
				erms.append(" ");
				erms.append(errorString);
				IGSMessageBox.showOkMB(this.fieldParent, null, erms.toString(), null);
			}
		}// end of good smartwrap dll load
		return rc;
	}

	/**
	 * Creation date: (12/11/2003 2:16:57 PM)
	 * @param byte_array
	 * @param ecid_hex
	 * @param fuseNum
	 * @param partNum
	 * @param insq
	 * @param elevens
	 * @param action
	 * @param isSquadronMif
	 * @return the return code
	 */
	private int keywordSquadronDLL(byte[] byte_array, String ecid_hex,
									int fuseNum, String partNum,
									String insq, String elevens, String action,
									boolean isSquadronMif)
	{
		final MFSConfig config = MFSConfig.getInstance();
		int rc = 0;
		String errorString = "";
		//~7C Use fieldVPDRec
		String file = writeVPDFileKeyWord(byte_array, this.fieldVPDRec.mifPN.substring(5, 12));

		//PERFORM BURN STUFF HERE
		/* tells dll if it is erasing or writing */
		String mode;
		if (action.equals("UNBURN"))
		{
			mode = "ERASE";
		}
		else
		{
			mode = "WRITE";
		}

		/* figure out the format parm required for the black box dll */
		String format;
		if (isSquadronMif)
		{
			format = "SQU";
		}
		else if (this.fieldVPDRec.usage.equals("A"))
		{
			format = "CAS";
		}
		else
		{
			format = "CRS";
		}		

		//Create an instance of a VPD DLL wrapper class,
		//which will be used to perform the burn
		SquadWrap squadie = new SquadWrap(mode, file, this.fieldVPDRec.burnPort, elevens,
				format, ecid_hex, partNum, this.fieldVPDRec.redflagind, this.fieldVPDRec.voltage); //~8C

		/* make sure dll was loaded correctly */
		errorString = squadie.getErr();
		rc = squadie.getRc();
		if (rc != 0)
		{
			this.fieldParent.setCursor(Cursor.getDefaultCursor());
			IGSMessageBox.showOkMB(this.fieldParent, null, squadie.getErr(), null);
		}
		else
		{
			if (action.equals("UNBURN")) //$NON-NLS-1$
			{
				//~6C Moved threading and update logic to executeRunnable
				executeRunnable(squadie, "Erasing Card, Please Wait...");
			}
			else
			{
				//~6C Moved threading and update logic to executeRunnable
				executeRunnable(squadie, "Writing to Card, Please Wait...");
			}
			errorString = squadie.getRcStr();
			rc = squadie.getRc();

			if (rc == 0)
			{
				/* ~1A , first parm is L for log */
				if (action.equals("UNBURN"))
				{
					/* call the LOG_VPDINF trx */
					StringBuffer data2 = new StringBuffer();
					data2.append("LOG_VPDINF");
					data2.append("R");
					data2.append(partNum.trim());
					data2.append(insq.trim());
					data2.append("    ");
					data2.append("          ");
					data2.append("                ");
					data2.append(config.get8CharUser());

					MFSTransaction log_vpdinf = new MFSFixedTransaction(data2.toString());
					log_vpdinf.setActionMessage("Removing VPD info...");
					MFSComm.getInstance().execute(log_vpdinf, this.fieldActionable);
					rc = log_vpdinf.getReturnCode();

					if (rc != 0)
					{
						IGSMessageBox.showOkMB(this.fieldParent, null,
								log_vpdinf.getErms(), null);
					}
				}
				else
				{/* ~1 */
					rc = logVPDInfoSquadron('L', partNum, insq, errorString); /* ~1A */
				}

				if (rc == 0)
				{
					this.fieldParent.setCursor(Cursor.getDefaultCursor());
					
					//~6C Moved message display logic to new method
					displayBurnStatusMessageDialog(action);

					if (action.equals("UNBURN") || action.equals("REBURN"))
					{
						/* Check if we need to print an Unburn Label */
						String cnfgUnburn = "UNBURNLBL,"
								+ this.fieldHeadRec.getPrln().trim();

						String valUnburn = config.getConfigValue(cnfgUnburn);
						if (valUnburn.equals(MFSConfig.NOT_FOUND))
						{
							String cnfgUnburn2 = "UNBURNLBL," + "*ALL";
							valUnburn = config.getConfigValue(cnfgUnburn2);
						}

						if (!valUnburn.equals(MFSConfig.NOT_FOUND))
						{
							int qty = 1;
							if (!valUnburn.equals(""))
							{
								qty = Integer.parseInt(valUnburn);
							}
							if (config.containsConfigEntry("VPDMIF2"))
							{
								MFSPrintingMethods.vpdmif2(this.fieldHeadRec.getMctl(),
										"U", 1, this.fieldParent);
							}
							else
							{
								MFSPrintingMethods.vpdunburn(this.fieldHeadRec.getMctl(), 
										this.fieldVPDRec.mifLabelType, qty, this.fieldParent);
							}
						}
					}
					else
					{
						if (config.containsConfigEntry("VPDMIF2"))
						{
							MFSPrintingMethods.vpdmif2(this.fieldHeadRec.getMctl(),
									"B", 1, this.fieldParent);
						}

						else
						{
							/* 
							 * now that the card has been successfully burned,
							 * use the field retrieved from the VD10 to
							 * determine which type of label to print, MIF or 11S
							 */
							if (this.fieldVPDRec.mifLabelType.equals("MIF ")
									|| this.fieldVPDRec.mifLabelType.equals("BOTH"))
							{
								MFSPrintingMethods.vpdmif(this.fieldHeadRec.getPrln(),
										this.fieldHeadRec.getMctl(), 1,
										this.fieldParent);
							}
							if (this.fieldVPDRec.mifLabelType.equals("11S ")
									|| this.fieldVPDRec.mifLabelType.equals("BOTH"))
							{
								if (this.fieldMifCardLetter.trim().equals(""))
								{
									MFSPrintingMethods.vpd11s(this.fieldHeadRec.getPrln(), 
											this.fieldHeadRec.getMctl(),
											"      ", 1, this.fieldParent);
								}
								else
								{
									MFSPrintingMethods.vpd11s(this.fieldHeadRec.getPrln(), 
											this.fieldHeadRec.getMctl(),
											"Card " + this.fieldMifCardLetter, 1,
											this.fieldParent);
								}
							}
						}
					}
					// fix the funny cursor problem
					this.fieldParent.setCursor(Cursor.getDefaultCursor());

					// only execute this logic if FUSE data is needed
					if (fuseNum > 0)
					{
						rc = fuseWrite(fuseNum);

					}// end of fuseNum > 0
				}//end of good logVPD return
			}//end of good burn sequence
			else
			{
				this.fieldParent.setCursor(Cursor.getDefaultCursor());
				StringBuffer erms = new StringBuffer();
				erms.append("Error(Keyword or Squadron): RC - ");
				erms.append(rc);
				erms.append(" ");
				erms.append(errorString);
				IGSMessageBox.showOkMB(this.fieldParent, null, erms.toString(), null);
			}
		}// end of good smartwrap dll load
		return rc;
	}

	/**
	 * This method calls the convertebytedata method and the server trx to log
	 * or remove data from the OH10 based on what's passed in the actn indicator
	 * @param actn action to perform for server trx L=log, R=remove
	 * @param inpn part number
	 * @param insq serial number
	 * @param byteDataHex string of filled mif data from dll, used to parse out
	 *        ccin,ccid,ccsn
	 * @return the return code
	 */
	private int logVPDInfo(char actn, String inpn, String insq, String byteDataHex)
	{
		int rc = 0;
		try
		{
			String ccin = "";
			String ccsn = "";
			String ccid = "";

			/* Begin ~2 to find correct CCID */
			String SomeError = "";

			//~3 don't include | because it will throw stuff off by 1 char
			if (byteDataHex.substring(0, 1).equals("|"))
			{
				byteDataHex = byteDataHex.substring(1);
			}

			int MIFLength = byteDataHex.length();

			int Loop = 0;
			int TempEcc = 0;
			boolean CCIDFound = false;
			String byteDataCCIDHex = "";

			while ((0 == rc) && (TempEcc + 10 < MIFLength) && (!CCIDFound))
			{
				/* 'MF', length, etc */
				if (byteDataHex.substring(TempEcc, TempEcc + 10).equals("4d4602000b")) 
				{
					/* Skip over three-byte keyword, and its data length. */
					TempEcc = TempEcc + 10;

					/* Look for 'SM' keyword that must follow immediately. */
					if (byteDataHex.substring(TempEcc, TempEcc + 4).equals("534d")) /* 'SM' */
					{
						/* Get length of SM field. */
						int byteLen = (Integer.valueOf(byteDataHex.substring(TempEcc + 4, TempEcc + 6))).intValue();
						Loop = TempEcc + 6 + (byteLen * 2); /* each byte = 2 char */
						TempEcc = TempEcc + 6; /* skip 3 bytes ('S','M',length) */

						/* Search for CS keywords for length of SM field. */
						while ((TempEcc < Loop) && (!CCIDFound))
						{
							if (byteDataHex.substring(TempEcc, TempEcc + 4).equals("4353")) /* 'CS' */
							{
								/* Skip 'C' and 'S' and update field. */
								CCIDFound = true;
								byteDataCCIDHex = byteDataHex.substring(TempEcc + 4, TempEcc + 20);
							}
							/* SM format in 10 byte entries (each byte = 2 char) */
							TempEcc = TempEcc + 20; 
						}
					} /* if SM format */
					else
					{
						rc = 426;
						SomeError = "'SM' keyword must follow MF 0x000B format in Keyword MIF.";
						System.out.println(SomeError);
					} /* else error in MIF format */
				} /* if MF format */
				else
				{
					TempEcc = TempEcc + 2;
				}
			} /* while parsing MIF */
			/* End ~2 */

			try
			{
				String byteDataBlockHex = byteDataHex.substring(byteDataHex.indexOf("84"), (byteDataHex.lastIndexOf("79") + 2));

				//get ccin string prepped for trx
				try
				{
					String byteDataCCINHex = byteDataBlockHex.substring((byteDataBlockHex.indexOf("4343") + 6),
							(byteDataBlockHex.indexOf("4343") + 14));
					int hexCCINLength = byteDataCCINHex.length();
					char[] arrayCCIN = new char[hexCCINLength / 2];
					for (int j = 0, k = 2, l = 0; j < hexCCINLength; j += 2, k += 2, l++)
					{
						arrayCCIN[l] = (char) Integer.parseInt(byteDataCCINHex.substring(j, k), 16);
					}
					ccin = new String(arrayCCIN);
				}
				catch (java.lang.StringIndexOutOfBoundsException ex)
				{
					String erms = "Possible data missing - check output data from DLL for 4343";
					IGSMessageBox.showOkMB(this.fieldParent, null, erms, null);
				}

				//get ccsn string prepped for trx
				try
				{
					String byteDataCCSNHex = byteDataBlockHex.substring((byteDataBlockHex.indexOf("534e") + 6),
							(byteDataBlockHex.indexOf("534e") + 30));
					int hexCCSNLength = byteDataCCSNHex.length();
					char[] arrayCCSN = new char[hexCCSNLength / 2];
					for (int j = 0, k = 2, l = 0; j < hexCCSNLength; j += 2, k += 2, l++)
					{
						arrayCCSN[l] = (char) Integer.parseInt(byteDataCCSNHex.substring(j, k), 16);
					}
					ccsn = new String(arrayCCSN);
					//~7C Use fieldVPDRec
					if (this.fieldVPDRec.iOrP.equals("P"))
					{
						ccsn = ccsn.substring(3, 5) + "-" + ccsn.substring(5);
					}
					else
					{
						ccsn = ccsn.substring(2, 4) + "-" + ccsn.substring(5);
					}
				}
				catch (java.lang.StringIndexOutOfBoundsException ex)
				{
					String erms = "Possible data missing - check output data from DLL for 534e";
					IGSMessageBox.showOkMB(this.fieldParent, null, erms, null);
				}
			}
			catch (java.lang.StringIndexOutOfBoundsException ex)
			{
				String erms = "Possible data missing - check output data from DLL for 84 or 79";
				IGSMessageBox.showOkMB(this.fieldParent, null, erms, null);
			}

			//prep ccid string for the trx
			ccid = byteDataCCIDHex; /* no conversion necessary */

			//*****************************************************************

			/* call the LOG_VPDINF trx */
			StringBuffer data = new StringBuffer();
			data.append("LOG_VPDINF");
			data.append(actn);
			data.append(inpn);
			data.append(insq);
			data.append(ccin);
			data.append(ccsn);
			data.append(ccid);
			data.append(MFSConfig.getInstance().get8CharUser());

			MFSTransaction log_vpdinf = new MFSFixedTransaction(data.toString());
			log_vpdinf.setActionMessage("Logging VPD info...");
			MFSComm.getInstance().execute(log_vpdinf, this.fieldActionable);
			rc = log_vpdinf.getReturnCode();

			if (rc != 0)
			{
				IGSMessageBox.showOkMB(this.fieldParent, null, log_vpdinf.getErms(), null);
			}
		}/* end try - now catch */
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(this.fieldParent, null, null, e);
		}
		return rc;

	}

	/**
	 * This method calls the convertebytedata method and the server trx to log
	 * or remove data from the OH10 based on what's passed in the actn
	 * indicator.
	 * @param actn action to perform for server trx L=log, R=remove
	 * @param inpn part no
	 * @param insq serial no
	 * @param byteData string of filled mif data from dll, used to parse out
	 *        ccin,ccid,ccsn
	 * @return the return code
	 */
	private int logVPDInfo80Byte(char actn, String inpn, String insq, String byteData)
	{
		int rc = 0;
		try
		{
			String ccin = "";
			String ccsn = "";
			String ccid = "";
			String byteDataHex = "";

			byteDataHex = byteData.substring(1);

			System.out.println("byteDataHex is |" + byteDataHex + "|");

			String byteDataCCINHex = byteDataHex.substring(0, 8);
			System.out.println("byeDataCCINHex |" + byteDataCCINHex + "|");

			String byteDataSerialHex = byteDataHex.substring(40, 56);
			System.out.println("byeDataSerialHex |" + byteDataSerialHex + "|");
			String byteDataMfgIdHex = byteDataHex.substring(56, 64);
			System.out.println("byeDataMfgIdHex |" + byteDataMfgIdHex + "|");

			String byteDataCCIDHex = byteDataHex.substring(96, 112);
			System.out.println("byeDataCCIDHex |" + byteDataCCIDHex + "|");

			String byteCCINandCCSN = "";
			byteCCINandCCSN = byteCCINandCCSN + byteDataCCINHex + byteDataSerialHex;
			System.out.println("byteCCINandCCSN |" + byteCCINandCCSN + "|");
			String byteCCINandCCSNcvt = "";

			for (int j = 0, k = 2; j < byteCCINandCCSN.length(); j += 2, k += 2)
			{
				int val = Integer.parseInt(byteCCINandCCSN.substring(j, k), 16);
				switch (val)
				{
					case 129:
					case 193:
						byteCCINandCCSNcvt = byteCCINandCCSNcvt.concat("A");
						break;
					case 130:
					case 194:
						byteCCINandCCSNcvt = byteCCINandCCSNcvt.concat("B");
						break;
					case 131:
					case 195:
						byteCCINandCCSNcvt = byteCCINandCCSNcvt.concat("C");
						break;
					case 132:
					case 196:
						byteCCINandCCSNcvt = byteCCINandCCSNcvt.concat("D");
						break;
					case 133:
					case 197:
						byteCCINandCCSNcvt = byteCCINandCCSNcvt.concat("E");
						break;
					case 134:
					case 198:
						byteCCINandCCSNcvt = byteCCINandCCSNcvt.concat("F");
						break;
					case 135:
					case 199:
						byteCCINandCCSNcvt = byteCCINandCCSNcvt.concat("G");
						break;
					case 136:
					case 200:
						byteCCINandCCSNcvt = byteCCINandCCSNcvt.concat("H");
						break;
					case 137:
					case 201:
						byteCCINandCCSNcvt = byteCCINandCCSNcvt.concat("I");
						break;
					case 145:
					case 209:
						byteCCINandCCSNcvt = byteCCINandCCSNcvt.concat("J");
						break;
					case 146:
					case 210:
						byteCCINandCCSNcvt = byteCCINandCCSNcvt.concat("K");
						break;
					case 147:
					case 211:
						byteCCINandCCSNcvt = byteCCINandCCSNcvt.concat("L");
						break;
					case 148:
					case 212:
						byteCCINandCCSNcvt = byteCCINandCCSNcvt.concat("M");
						break;
					case 149:
					case 213:
						byteCCINandCCSNcvt = byteCCINandCCSNcvt.concat("N");
						break;
					case 150:
					case 214:
						byteCCINandCCSNcvt = byteCCINandCCSNcvt.concat("O");
						break;
					case 151:
					case 215:
						byteCCINandCCSNcvt = byteCCINandCCSNcvt.concat("P");
						break;
					case 152:
					case 216:
						byteCCINandCCSNcvt = byteCCINandCCSNcvt.concat("Q");
						break;
					case 153:
					case 217:
						byteCCINandCCSNcvt = byteCCINandCCSNcvt.concat("R");
						break;
					case 162:
					case 226:
						byteCCINandCCSNcvt = byteCCINandCCSNcvt.concat("S");
						break;
					case 163:
					case 227:
						byteCCINandCCSNcvt = byteCCINandCCSNcvt.concat("T");
						break;
					case 164:
					case 228:
						byteCCINandCCSNcvt = byteCCINandCCSNcvt.concat("U");
						break;
					case 165:
					case 229:
						byteCCINandCCSNcvt = byteCCINandCCSNcvt.concat("V");
						break;
					case 166:
					case 230:
						byteCCINandCCSNcvt = byteCCINandCCSNcvt.concat("W");
						break;
					case 167:
					case 231:
						byteCCINandCCSNcvt = byteCCINandCCSNcvt.concat("X");
						break;
					case 168:
					case 232:
						byteCCINandCCSNcvt = byteCCINandCCSNcvt.concat("Y");
						break;
					case 169:
					case 233:
						byteCCINandCCSNcvt = byteCCINandCCSNcvt.concat("Z");
						break;
					case 240:
						byteCCINandCCSNcvt = byteCCINandCCSNcvt.concat("0");
						break;
					case 241:
						byteCCINandCCSNcvt = byteCCINandCCSNcvt.concat("1");
						break;
					case 242:
						byteCCINandCCSNcvt = byteCCINandCCSNcvt.concat("2");
						break;
					case 243:
						byteCCINandCCSNcvt = byteCCINandCCSNcvt.concat("3");
						break;
					case 244:
						byteCCINandCCSNcvt = byteCCINandCCSNcvt.concat("4");
						break;
					case 245:
						byteCCINandCCSNcvt = byteCCINandCCSNcvt.concat("5");
						break;
					case 246:
						byteCCINandCCSNcvt = byteCCINandCCSNcvt.concat("6");
						break;
					case 247:
						byteCCINandCCSNcvt = byteCCINandCCSNcvt.concat("7");
						break;
					case 248:
						byteCCINandCCSNcvt = byteCCINandCCSNcvt.concat("8");
						break;
					case 249:
						byteCCINandCCSNcvt = byteCCINandCCSNcvt.concat("9");
						break;
				}
			}

			System.out.println("converted string is " + byteCCINandCCSNcvt);

			ccin = byteCCINandCCSNcvt.substring(0, 4);
			//ccsn = byteCCINandCCSNcvt.substring(4);
			ccid = byteDataCCIDHex; /* no conversion necessary */

			System.out.println("ccin = " + "|" + ccin + "|");
			//System.out.println("ccsn = " + ccsn);
			System.out.println("ccid = " + "|" + ccid + "|");
			ccsn = ccsn + byteDataMfgIdHex.substring(6) + "-" + byteCCINandCCSNcvt.substring(5);
			System.out.println("ccsn = " + "|" + ccsn + "|");

			//*****************************************************************

			/* call the LOG_VPDINF trx */
			StringBuffer data = new StringBuffer();
			data.append("LOG_VPDINF");
			data.append(actn);
			data.append(inpn);
			data.append(insq.trim());
			data.append(ccin.trim());
			data.append(ccsn.trim());
			data.append(ccid.trim());
			data.append(MFSConfig.getInstance().get8CharUser());

			MFSTransaction log_vpdinf = new MFSFixedTransaction(data.toString());
			log_vpdinf.setActionMessage("Logging VPD info...");
			MFSComm.getInstance().execute(log_vpdinf, this.fieldActionable);
			rc = log_vpdinf.getReturnCode();

			if (rc != 0)
			{
				IGSMessageBox.showOkMB(this.fieldParent, null, log_vpdinf.getErms(), null);
			}
		}/* end try - now catch */
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(this.fieldParent, null, null, e);
		}
		return rc;

	}

	/**
	 * @param actn
	 * @param inpn
	 * @param insq
	 * @param byteDataHex
	 * @return the return code
	 */
	private int logVPDInfoSquadron(char actn, String inpn, String insq,
									String byteDataHex)
	{
		int rc = 0;
		try
		{
			String ccin = null; //variable for the CCIN number
			String ccsn = null; //variable for the CCSN number
			String ccid = null; //variable for the CCID number

			String erms = "";

			//10-23-2003 we don't want to include the vertical bar because it
			// will throw stuff off by 1
			//char - DJF
			if (byteDataHex.substring(0, 1).equals("|"))
			{
				byteDataHex = byteDataHex.substring(1);
			}

			//This crazy logic was generated using some MIF documentation provided to me (DJF) 
			//by Steve Igel.  Mike Holst helped me interpret the documentation.  I placed the 
			//documents into the MFS Team DB under the VPD->Documentation section: VPD MIF Documentation
			
			//GENERAL LOGIC PLAN		
			//The first part of the VPDMif is called a VPD Header and it contains a pointer to VTOC
			//(VPD Table of Contents) - so first we locate the VTOC and the VTOC maps out a pointer to the 
			//VPD Record we are interested in, the VINI record. The VTOC also lists the size of the VINI record.
			//So we take the VINI offset from the VTOC, and then jump to the VINI Offset and then start looking 
			//for the B9 keyword.  The offsets referenced in here start from the beginning of the MIF
		
			//*NOTES*   
			//little ENDIAN - low order byte 1st instead of high order byte 1st...so if 3700 is the offset
			//its really 0037, so we parse out the int size by taking the 3 thru 4 + 1 thru 2 positions
			//0037 to us is 55 in decimal, and we have 2 chars per byte - so 110 chars of our byteDataHex String
			//56544f43 is VTOC in ASCII
			//56494e49 is VINI in ASCII
			//4239 is the B9 keyword
			//4353 is the CS keyword
		
		
			//VTOC record is located in the header, and it should be of the format as defined here:
			//Record Name	            		"VTOC"	ASCII 
			//   Record Type	               	'0000'x	Hex      (for smart chips)
			//   Record Offset	               	'0000'x	Hex 
			//   Record Length	               	'0000'x	Hex 
			//   ECC Offset	               		'0000'x	Hex 
			//   ECC Length	               		'0000'x	Hex
		  
			//So based on the above information about the VTOC record: 
			//The Data located should be "VTOC" in hex digits, so 8 chars, then 4 hex digits of Record Type
			//Then we have the record offset, we'll run a calculation here to find the table of contents
			//We test to make sure we can find any VTOC here, big problem if we can't find it
			int VTOCOffsetIndex;
			int VTOCIndex = byteDataHex.indexOf("56544f43");
			if (VTOCIndex == -1)
			{
				rc = -10;
				erms = "Unable to locate VTOC Record in the MIF Data!!";
				throw new Exception(erms);
			}
			//move the index past the VTOC itself and the Record Type info
			VTOCIndex += VPD_RECORD_NAME_LENGTH + VPD_RECORD_TYPE_LENGTH;

			//This index will take us to the beginning of the VTOC
			//little endian here, see notes above regarding little endian and
			// length calculations
			VTOCOffsetIndex = (Integer.parseInt(byteDataHex.substring(VTOCIndex + VPD_BYTE_LENGTH, VTOCIndex + (VPD_BYTE_LENGTH * 2)) +
					byteDataHex.substring(VTOCIndex, VTOCIndex + VPD_BYTE_LENGTH), 16) * VPD_BYTE_LENGTH);

			//look for first instance of VINI after the VTOC index to get the table of contents info for the VINI record
			//TOC record for the VINI record should be as follows:
			//Record Name	            		"VINI"	ASCII 
			//   Record Type	               	'0000'x	Hex  (for smart chips)
			//   Record Offset	               	'0000'x	Hex 
			//   Record Length	               	'0000'x	Hex 
			//   ECC Offset	               		'0000'x	Hex 
			//   ECC Length	               		'0000'x	Hex 
			//So based on the above information about the VINI TOC record: 
			//The Data located should be "VINI" in hex digits, so 8 chars, then 4 hex digits of Record Type
			//Then we have the record offset, we'll run a calculation here to find the table of contents
			//We test to make sure we can find any TOC VINI here, big problem if we can't find it

			int VINIOffsetIndex;
			int VTOCVINIIndex = byteDataHex.indexOf("56494e49", VTOCOffsetIndex);
			if (VTOCVINIIndex == -1)
			{
				rc = -11;
				erms = "Unable to locate the VINI record in the Table of Contents of the MIF Data!!";
				throw new Exception(erms);

			}
			//move the index past the TOC VINI and the Record Type info
			VTOCVINIIndex += VPD_RECORD_NAME_LENGTH + VPD_RECORD_TYPE_LENGTH;

			//VINI offset is little endian, see comment above
			//Offset is defined as the number of chars from the beginning of the byteDataHex
			VINIOffsetIndex = (Integer.parseInt(byteDataHex.substring(VTOCVINIIndex + VPD_BYTE_LENGTH, VTOCVINIIndex + (VPD_BYTE_LENGTH * 2))
					+ byteDataHex.substring(VTOCVINIIndex, VTOCVINIIndex + VPD_BYTE_LENGTH), 16)) * VPD_BYTE_LENGTH;

			//calculate VINI record size - it is defined in little endian here,
			// see comment above
			int VINISize = (Integer.parseInt(byteDataHex.substring(VTOCVINIIndex + (VPD_BYTE_LENGTH * 3), VTOCVINIIndex + (VPD_BYTE_LENGTH * 4))
					+ byteDataHex.substring(VTOCVINIIndex + (VPD_BYTE_LENGTH * 2), VTOCVINIIndex + (VPD_BYTE_LENGTH * 3)), 16)) * VPD_BYTE_LENGTH;

			//VINIRecord will be now used from here on out
			//Jump to the VINI Offset and substring out the VINISize record
			String VINIRecord = byteDataHex.substring(VINIOffsetIndex, VINIOffsetIndex + VINISize);

			
			//This is a picture of the layout of the VINI record...this does NOT show the complete picture just a general
			//picture, DR, FN, PN, SN are various keywords here, B9 comes a bit later.
			//Large Resource	'84'x	Hex
			//Length	    'xxxx'x	Hex   (little endian)
			//	  Keyword	        "RT"                Record Name	ASCII
			//		  Length	            '04'x	Hex
			//		  Data	            "VINI"	ASCII 
			//	  Keyword	        "DR"                   Description	ASCII
			//		  Length	            '10'x 	Hex
			//		  Data	            "DESCRIPTION     " 	ASCII 
			//	  Keyword	        "FN"                   Field Part #	ASCII
			//		  Length	            '7'x 	Hex
			//		  Data	            "1234567" 	ASCII 
			//	  Keyword	        "PN"   Card Assembly Part #	ASCII
			//		  Length	            '7'x 	Hex
			//		  Data	            "1234567"	ASCII 
			//	  Keyword	        "SN"                Serial Number	ASCII
			//		  Length	            '0C'x	Hex
			//		  Data	            "YL1022123456"	ASCII
		
			//General Idea of this algorithm, we'll step thru the keywords looking for B9, CC, and SN
			//when we find one, we'll dump it to a variable and process it a bit later 
			String B9Data = null; //raw B9 data pulled from the VINI Record
			String cc = null; //raw CC keyword pulled from the VINI Record
			String sn = null; //raw SN keyword pulled from the VINI Record
			int tempInt;
			int workingIndex = VPD_BYTE_LENGTH + VPD_KEYWORD_LENGTH; //move past the '84' and Length
			int incrementor = 0;

			while (workingIndex + VPD_KEYWORD_LENGTH < VINISize)
			{
				//incrementor is the number of chars to move - based on the
				// current keyword's length
				incrementor = (Integer.parseInt(VINIRecord.substring(workingIndex + (VPD_KEYWORD_LENGTH), workingIndex + (VPD_KEYWORD_LENGTH + VPD_BYTE_LENGTH)), 16)) * VPD_BYTE_LENGTH;

				//tempInt is just a handy way to define the start of this keyword's data, move past the
				//keyword and length
				tempInt = workingIndex + VPD_KEYWORD_LENGTH + VPD_BYTE_LENGTH;

				if (VINIRecord.substring(workingIndex, workingIndex + VPD_KEYWORD_LENGTH).equals("4239")) /* B9 */
				{
					B9Data = VINIRecord.substring(tempInt, tempInt + incrementor);
				}
				else if (VINIRecord.substring(workingIndex, workingIndex + VPD_KEYWORD_LENGTH).equals("4343")) /* CC */
				{
					cc = VINIRecord.substring(tempInt, tempInt + incrementor);
				}
				else if (VINIRecord.substring(workingIndex, workingIndex + VPD_KEYWORD_LENGTH).equals("534e")) /* SN */
				{
					sn = VINIRecord.substring(tempInt, tempInt + incrementor);
				}

				//jump to the start of the next keyword
				workingIndex = tempInt + incrementor;
			}

			if (B9Data == null)
			{
				rc = -12;
				erms = "Unable to locate the B9 Keyword Record in the VINI Record!!";
				throw new Exception(erms);
			}
			if (cc == null)
			{
				rc = -13;
				erms = "Unable to locate the CC Keyword Record in the VINI Record!!";
				throw new Exception(erms);
			}
			if (sn == null)
			{
				rc = -14;
				erms = "Unable to locate the SN Keyword Record in the VINI Record!!";
				throw new Exception(erms);
			}

			//we are looking for the CS keyword inside of the B9 Data
			//need to assign the ccid variable, we do this by stepping thru the B9 data 2 chars at a time
			workingIndex = 0;
			while (workingIndex + VPD_KEYWORD_LENGTH + VPD_CCID_LENGTH < B9Data.length() && ccid == null)
			{
				if (B9Data.substring(workingIndex, workingIndex + VPD_KEYWORD_LENGTH).equals("4353"))
				{
					ccid = B9Data.substring(workingIndex + VPD_KEYWORD_LENGTH, workingIndex + VPD_KEYWORD_LENGTH + VPD_CCID_LENGTH);
				}
				else
				{
					workingIndex += VPD_BYTE_LENGTH;
				}
			}

			if (ccid == null)
			{
				rc = -15;
				erms = "Unable to locate the CS Keyword Record in the B9 Record!!";
				throw new Exception(erms);
			}

			//CC is 8 hex digits and converting those into integers (2 hex chars = 1 integer) 
			//and then casting each of those integers into characters...
			//example: 37343031 yields a CCIN=7401
			char[] arrayCCIN = new char[4];
			for (int j = 0; j < 8; j += 2)
			{
				arrayCCIN[j / 2] = (char) Integer.parseInt(cc.substring(j, j + 2), 16);
			}
			ccin = new String(arrayCCIN);

			//SN is 24 hex digits and converting those into integers (2 hex chars = 1 integer) 
			//and then casting each of those integers into characters...
			//example: 594C31303032303030313136 yields a CCSN=YL1002000116
			char[] arraySN = new char[12];
			for (int j = 0; j < 24; j += 2)
			{
				arraySN[j / 2] = (char) Integer.parseInt(sn.substring(j, j + 2), 16);
			}
			ccsn = new String(arraySN);

			//~7C Use fieldVPDRec
			if (this.fieldVPDRec.iOrP.equals("P"))
			{
				ccsn = ccsn.substring(3, 5) + "-" + ccsn.substring(5);
			}
			else
			{
				ccsn = ccsn.substring(2, 4) + "-" + ccsn.substring(5);
			}

			//all required data has been gleened from the returned data, now run
			//trx to log it all to the FCSPBI10 table in MFS

			if (rc == 0)
			{
				/* call the LOG_VPDINF trx */
				StringBuffer data = new StringBuffer();
				data.append("LOG_VPDINF");
				data.append(actn);
				data.append(inpn);
				data.append(insq);
				data.append(ccin);
				data.append(ccsn);
				data.append(ccid);
				data.append(MFSConfig.getInstance().get8CharUser());

				MFSTransaction log_vpdinf = new MFSFixedTransaction(data.toString());
				log_vpdinf.setActionMessage("Logging VPD info...");
				MFSComm.getInstance().execute(log_vpdinf, this.fieldActionable);
				rc = log_vpdinf.getReturnCode();

				if (rc != 0)
				{
					IGSMessageBox.showOkMB(this.fieldParent, null, log_vpdinf.getErms(), null);
				}
			}//end of good data gathered

		}/* end try - now catch */
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(this.fieldParent, null, null, e);
		}
		return rc;

	}

	/**
	 * @param mifdata
	 * @return the MIF Data
	 */
	private String massage80ByteMifData(String mifdata)
	{
		String finish_mif = "";
		try
		{

			int index = 0;
			int valLen = 0;
			String processed_dta = "";

			// We will strip off the 1st (8 hex bytes worth)4 characters of each line.
			// Each line of mif data is 256 mif characters long which translates to 512 hex bytes long
			// 		   8 hex bytes which represents the length of valid mif data on this line.
			//  	+504 hex bytes of "good" data 
			//      ____
			//       512 total hex bytes per line of mif data
			while ((index + 512) <= mifdata.length())
			{
				valLen = Integer.parseInt(mifdata.substring(index, index + 8), 16);
				processed_dta += mifdata.substring(index + 8, index + 8 + valLen * 2);
				index += (512);
			}

			// now processed_dta contains the "valid" mif data from the AS/400 converted to Hex 
			// we now need to pull the header part off - to do this, we will loop until we find 
			// a 160 bytes section that starts with *DATA* (5CC4C1E3C15C).  The "useful" mif data
			// follows this 160 bytes.
			index = 0;
			while (!processed_dta.substring(index, index + 12).equals("5CC4C1E3C15C"))
			{
				index += 160;
			}

			finish_mif = processed_dta.substring(index + 160);

			/* determine which letter to use for the 11S label */
			String tempCardLetter = finish_mif.substring(64, 66);
			if (tempCardLetter.equals("81"))
			{
				this.fieldMifCardLetter = "A";
			}
			else if (tempCardLetter.equals("82"))
			{
				this.fieldMifCardLetter = "B";
			}
			else
			{
				this.fieldMifCardLetter = " ";
			}
		}/* end try - now catch */
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(this.fieldParent, null, null, e);
		}
		return finish_mif;
	}

	/**
	 * @return the return code
	 */
	public int processMifBurn()
	{
		int rc = 0;
		try
		{
			/* this sends the the product line plus some zeros to the AS/400 to */
			/* read the VD10 records - this will return all of those fields from the VD10 */
			/* -most importantly it will return the MIF_PN which will be used to get the MIF */
			/* data from CPPCP010/MIF */
			String errorString = "";
			String ecid_hex = "";
			String bkpn = "00000" + this.fieldHeadRec.getPrln().substring(0, 7);
			//~7C Convert RTV_VD to XML and use MFSVPDRec
			IGSXMLTransaction rtv_vd = callRTV_VD(bkpn);
			rc = rtv_vd.getReturnCode();

			if (rc != 0)
			{
				IGSMessageBox.showOkMB(this.fieldParent, null, rtv_vd.getErms(), null);
			}
			else
			{
				this.fieldVPDRec = new MFSVPDRec(rtv_vd, true);

				final MFSConfig config = MFSConfig.getInstance();
				String data1 = "RTV_MIF   " + this.fieldVPDRec.mifPN.substring(5);
				common.VPDComm myVPDComm = new common.VPDComm(config, data1);
				if (config.containsConfigEntry("VPDSRV") && config.containsConfigEntry("VPDRTR"))
				{
					myVPDComm.setServer(config.getConfigValue("VPDSRV"));
					myVPDComm.setPort(config.getConfigValue("VPDRTR"));

					//~6C Moved threading and update logic to executeRunnable
					executeRunnable(myVPDComm, "Retrieving MIF Data, Please Wait...");
					
					rc = myVPDComm.getRc();
					if (rc != 0)
					{
						errorString = myVPDComm.getData();
					}
				}
				else
				{
					rc = 10;
					errorString = "Missing Config File Entries: VPDSRV and/or VPDRTR";
				}

				if (rc != 0)
				{
					IGSMessageBox.showOkMB(this.fieldParent, null, errorString, null);
				}
				else // successfully retrieved mif data
				{
					String mifdta = "";
					boolean keywordMif = false;
					boolean squadronMif = false;
					if (myVPDComm.getData().substring(0, 3).equals("KEY"))
					{
						keywordMif = true;
					}
					else if (myVPDComm.getData().substring(0, 3).equals("SQU"))
					{
						squadronMif = true;
					}

					if (keywordMif || squadronMif)
					{
						int fuseNum = Integer.parseInt(myVPDComm.getData().substring(3, 4));

						this.fieldDllMifdata = myVPDComm.getData().substring(4);

						byte[] byte_array = new byte[myVPDComm.getByteData().length - 4];

						for (int byteIndex = 0; byteIndex < byte_array.length; byteIndex++)
						{
							byte_array[byteIndex] = myVPDComm.getByteData()[byteIndex + 4];
						}

						//set up a default ecid string in case the ecid flag is turned off
						ecid_hex = "0000000000000000";

						if (this.fieldVPDRec.ecid.equals("Y"))
						{
							String mcmPartNum = "MCMPARTNUM";
							String mcmPartNumValue = config.getConfigValue(mcmPartNum);
							if (mcmPartNumValue.equals(MFSConfig.NOT_FOUND))
							{
								rc = 10;
								errorString = "MCM Part Number not known!  PFS Rule Needs to be Set Up!";
							}
							else
							{
								StringBuffer data2 = new StringBuffer();
								data2.append("RTV_ECID  ");
								data2.append(mcmPartNumValue.substring(5, 12));
								data2.append(this.fieldHeadRec.getSapn().substring(0, 12));

								MFSTransaction rtv_ecid = new MFSFixedTransaction(data2.toString());
								rtv_ecid.setActionMessage("Retrieving ECID Data, Please Wait...");
								MFSComm.getInstance().execute(rtv_ecid, this.fieldActionable);
								rc = rtv_ecid.getReturnCode();
								
								if (rc != 0)
								{
									errorString = rtv_ecid.getOutput();
								}
								else
								{
									ecid_hex = rtv_ecid.getOutput().substring(0, 16);
								}

							}//end MCMPARTNUM config entry found
						}// end of ECID flag set to 'Y'

						if (rc != 0)
						{
							this.fieldParent.setCursor(Cursor.getDefaultCursor());
							IGSMessageBox.showOkMB(this.fieldParent, null, errorString, null);
						}
						else
						{
							/* setup serial number string */
							String insq = this.fieldHeadRec.getSapn().substring(0, 12);

							/* get the part number for passing to VPD burn function */
							String partNum = "00000" + this.fieldHeadRec.getPrln().substring(0, 7);

							/* build an 11S string */
							String elevens = "11S" + partNum.substring(5, 12) + insq;

							//default is to use black box for now - if NOBLACKBOX use old DLL
							if (config.containsConfigEntry("NOBLACKBOX"))
							{
								if (squadronMif)
								{
									rc = 100;
									String erms = "Client Not Configured to handle Squadron Format!";
									IGSMessageBox.showOkMB(this.fieldParent, null, erms, null);
									this.fieldParent.setCursor(Cursor.getDefaultCursor());
								}
								else
								{
									//~5 removed extraneous parms
									rc = keywordOldDLL(byte_array, ecid_hex, fuseNum,
											partNum, insq, elevens, "WRITE");
								}
							}
							else
							{
								rc = keywordSquadronDLL(byte_array, ecid_hex, fuseNum,
										partNum, insq, elevens, "WRITE", squadronMif);
							}
						}// end of good rtv_ecid call
					}// end of keyword mif
					else // 80 byte mif
					{
						mifdta = massage80ByteMifData(myVPDComm.getData().substring(4));
						//default is to use black box for now - if NOBLACKBOX use old DLL
						if (config.containsConfigEntry("NOBLACKBOX"))
						{
							//~5 removed extraneous parms
							rc = eightyByteOldDLL(mifdta, "WRITE");
						}
						else
						{
							//~5 removed extraneous parms
							rc = eightyByteSquadronDLL(mifdta, ecid_hex, "WRITE");
						}
					} // end 80 byte mif
				}// end of good rtv_mif call */
			}// end of good return from rtv_vpddata

		}/* end try - now catch */
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(this.fieldParent, null, null, e);
		}
		return rc;
	}

	/**
	 * @return the return code
	 */
	public int processMifReburn()
	{
		int rc = 0;
		try
		{
			/* first get the PNSN to reburn to */
			String data = "RTV_RBRNPS" + this.fieldHeadRec.getMctl();

			MFSTransaction rtv_rbrnps = new MFSFixedTransaction(data);
			rtv_rbrnps.setActionMessage("Retrieving Reburn Part/Serial, Please Wait...");
			MFSComm.getInstance().execute(rtv_rbrnps, this.fieldActionable);
			rc = rtv_rbrnps.getReturnCode();

			if (rc != 0)
			{
				IGSMessageBox.showOkMB(this.fieldParent, null, rtv_rbrnps.getErms(), null);
			}
			else
			{
				// successful call to RTV_RBRNPS, 
				//dump its return into some local variables
				
				/* get the part number for passing to VPD burn function */
				String partNum = rtv_rbrnps.getOutput().substring(0, 12);
				String insq = rtv_rbrnps.getOutput().substring(12, 24);
				/* build an 11S string */
				String elevens = "11S" + partNum.substring(5, 12) + insq;

				/* this sends the the product line plus some zeros to the AS/400 to */
				/* read the VD10 records - this will return all of those fields from the VD10 */
				/* -most importantly it will return the MIF_PN which will be used to get the MIF */
				/* data from CPPCP010/MIF */
				String errorString = "";
				String ecid_hex = "";
				//~7C Convert RTV_VD to XML and use MFSVPDRec
				IGSXMLTransaction rtv_vd = callRTV_VD(partNum);
				rc = rtv_vd.getReturnCode();

				if (rc != 0)
				{
					IGSMessageBox.showOkMB(this.fieldParent, null, rtv_vd.getErms(), null);
				}
				else
				{
					this.fieldVPDRec = new MFSVPDRec(rtv_vd, true);

					final MFSConfig config = MFSConfig.getInstance();
					String data1 = "RTV_MIF   " + this.fieldVPDRec.mifPN.substring(5);
					common.VPDComm myVPDComm = new common.VPDComm(config, data1);
					if (config.containsConfigEntry("VPDSRV") && config.containsConfigEntry("VPDRTR"))
					{
						myVPDComm.setServer(config.getConfigValue("VPDSRV"));
						myVPDComm.setPort(config.getConfigValue("VPDRTR"));

						//~6C Moved threading and update logic to executeRunnable
						executeRunnable(myVPDComm, "Retrieving MIF Data, Please Wait...");
						
						rc = myVPDComm.getRc();
						if (rc != 0)
						{
							errorString = myVPDComm.getData();
						}
					}
					else
					{
						rc = 10;
						errorString = "Missing Config File Entries: VPDSRV and/or VPDRTR";
					}

					if (rc != 0)
					{
						IGSMessageBox.showOkMB(this.fieldParent, null, errorString, null);
					}
					else // successfully retrieved mif data
					{
						String mifdta = "";
						boolean keywordMif = false;
						boolean squadronMif = false;
						if (myVPDComm.getData().substring(0, 3).equals("KEY"))
						{
							keywordMif = true;
						}
						else if (myVPDComm.getData().substring(0, 3).equals("SQU"))
						{
							squadronMif = true;
						}

						if (keywordMif || squadronMif)
						{
							int fuseNum = Integer.parseInt(myVPDComm.getData().substring(3, 4));

							this.fieldDllMifdata = myVPDComm.getData().substring(4);

							byte[] byte_array = new byte[myVPDComm.getByteData().length - 4];

							for (int byteIndex = 0; byteIndex < byte_array.length; byteIndex++)
							{
								byte_array[byteIndex] = myVPDComm.getByteData()[byteIndex + 4];
							}

							//set up a default ecid string in case the ecid
							// flag is turned off
							ecid_hex = "0000000000000000";

							if (this.fieldVPDRec.ecid.equals("Y"))
							{
								String mcmPartNum = "MCMPARTNUM";
								String mcmPartNumValue = config.getConfigValue(mcmPartNum);
								if (mcmPartNumValue.equals(MFSConfig.NOT_FOUND))
								{
									rc = 10;
									errorString = "MCM Part Number not known!  PFS Rule Needs to be Set Up!";
								}
								else
								{
									StringBuffer data2 = new StringBuffer();
									data2.append("RTV_ECID  ");
									data2.append(mcmPartNumValue.substring(5, 12));
									data2.append(this.fieldHeadRec.getSapn().substring(0, 12));

									MFSTransaction rtv_ecid = new MFSFixedTransaction(data2.toString());
									rtv_ecid.setActionMessage("Retrieving ECID Data, Please Wait...");
									MFSComm.getInstance().execute(rtv_ecid, this.fieldActionable);

									rc = rtv_ecid.getReturnCode();
									if (rc != 0)
									{
										errorString = rtv_ecid.getOutput();
									}
									else
									{
										ecid_hex = rtv_ecid.getOutput().substring(0, 16);
									}

								}//end MCMPARTNUM config entry found
							}// end of ECID flag set to 'Y'

							if (rc != 0)
							{
								this.fieldParent.setCursor(Cursor.getDefaultCursor());
								IGSMessageBox.showOkMB(this.fieldParent, null, errorString, null);
							}
							else
							{
								//default is to use black box for now - if NOBLACKBOX use old DLL
								if (config.containsConfigEntry("NOBLACKBOX"))
								{
									if (squadronMif)
									{
										this.fieldParent.setCursor(Cursor.getDefaultCursor());
										rc = 100;
										String erms = "Client Not Configured to handle Squadron Format!";
										IGSMessageBox.showOkMB(this.fieldParent, null, erms, null);
									}
									else
									{
										//~5 removed extraneous parms
										rc = keywordOldDLL(byte_array, ecid_hex, fuseNum,
												partNum, insq, elevens, "REBURN");
									}
								}
								else
								{
									rc = keywordSquadronDLL(byte_array, ecid_hex,
											fuseNum, partNum, insq, elevens, "REBURN",
											squadronMif);
								}
							}// end of good rtv_ecid call
						}// end of keyword mif
						else // 80 byte mif
						{
							mifdta = massage80ByteMifData(myVPDComm.getData().substring(4));
							//default is to use black box for now - if NOBLACKBOX use old DLL
							if (config.containsConfigEntry("NOBLACKBOX"))
							{
								//~5 removed extraneous parms
								rc = eightyByteOldDLL(mifdta, "REBURN");
							}
							else
							{
								//~5 removed extraneous parms
								rc = eightyByteSquadronDLL(mifdta, ecid_hex, "REBURN");
							}
						} // end 80 byte mif
					}// end of good rtv_mif call
				}//end of good call to rtv_vd
			}// end of good return from rtv_rbrnps

		}/* end try - now catch */
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(this.fieldParent, null, null, e);
		}
		return rc;

	}

	/**
	 * @return the return code
	 */
	public int processMifReprint()
	{
		final MFSConfig config = MFSConfig.getInstance();
		int rc = 0;
		try
		{
			/* this sends the the product line plus some zeros to the AS/400 to */
			/* read the VD10 records - this will return all of those fields from the VD10 */
			/* -most importantly it will return the MIF_PN which will be used to get the MIF */
			/* data from CPPCP010/MIF */
			String errorString = "";
			String bkpn = "00000" + this.fieldHeadRec.getPrln().substring(0, 7);
			//~7C Convert RTV_VD to XML and use MFSVPDRec
			IGSXMLTransaction rtv_vd = callRTV_VD(bkpn);
			rc = rtv_vd.getReturnCode();

			if (rc != 0)
			{
				IGSMessageBox.showOkMB(this.fieldParent, null, rtv_vd.getErms(), null);
			}
			else
			{
				this.fieldVPDRec = new MFSVPDRec(rtv_vd, true);

				String data1 = "RTV_MIF   " + this.fieldVPDRec.mifPN.substring(5);
				Comm myComm1 = new Comm(config, data1);

				myComm1.setInput(data1);
				if (config.containsConfigEntry("VPDSRV") && config.containsConfigEntry("VPDRTR"))
				{
					myComm1.setServer(config.getConfigValue("VPDSRV"));
					myComm1.setPort(config.getConfigValue("VPDRTR"));

					//~6C Moved threading and update logic to executeRunnable
					executeRunnable(myComm1, "Retrieving MIF Data, Please Wait...");
					
					rc = myComm1.getRc();
					if (rc != 0)
					{
						errorString = myComm1.getData();
					}
				}
				else
				{
					rc = 10;
					errorString = "Missing Config File Entries: VPDSRV and/or VPDRTR";
				}

				if (rc != 0)
				{
					IGSMessageBox.showOkMB(this.fieldParent, null, errorString, null);
				}
				else
				// successfully retrieved mif data
				{
					boolean keywordMif = false;
					boolean squadronMif = false;
					if (myComm1.getData().substring(0, 3).equals("KEY"))
					{
						keywordMif = true;
					}
					else if (myComm1.getData().substring(0, 3).equals("SQU"))
					{
						squadronMif = true;
					}
					if (config.containsConfigEntry("VPDMIF2"))
					{
						MFSPrintingMethods.vpdmif2(this.fieldHeadRec.getMctl(), "B", 1, this.fieldParent);
					}
					else
					{
						if (keywordMif || squadronMif)
						{
							this.fieldDllMifdata = myComm1.getData().substring(4);

							/* now that the card has been successfully burned, use the field retrieved from the VD10 to */
							/* determine which type of label to print, MIF or 11S                                       */
							if (this.fieldVPDRec.mifLabelType.equals("MIF ")
									|| this.fieldVPDRec.mifLabelType.equals("BOTH"))
							{
								MFSPrintingMethods.vpdmif(this.fieldHeadRec.getPrln(),
										this.fieldHeadRec.getMctl(), 1,
										this.fieldParent);
							}
							if (this.fieldVPDRec.mifLabelType.equals("11S ")
									|| this.fieldVPDRec.mifLabelType.equals("BOTH"))
							{
								if (this.fieldMifCardLetter.trim().equals(""))
								{
									MFSPrintingMethods.vpd11s(this.fieldHeadRec.getPrln(), 
											this.fieldHeadRec.getMctl(),
											"      ", 1, this.fieldParent);
								}
								else
								{
									MFSPrintingMethods.vpd11s(this.fieldHeadRec.getPrln(), 
											this.fieldHeadRec.getMctl(),
											"Card " + this.fieldMifCardLetter, 1,
											this.fieldParent);
								}
							}
						}// end of keyword mif
						else // 80 byte mif
						{
							//don't care about the returned data from here, the massage80ByteMifData sets up the mifCardLetter
							//field, that's all we care about here ~5
							massage80ByteMifData(myComm1.getData().substring(4));

							if (this.fieldVPDRec.mifLabelType.equals("MIF ")
									|| this.fieldVPDRec.mifLabelType.equals("BOTH"))
							{
								MFSPrintingMethods.vpdmif(this.fieldHeadRec.getPrln(),
										this.fieldHeadRec.getMctl(), 1,
										this.fieldParent);
							}
							if (this.fieldVPDRec.mifLabelType.equals("11S ")
									|| this.fieldVPDRec.mifLabelType.equals("BOTH"))
							{
								if (this.fieldMifCardLetter.trim().equals(""))
								{
									MFSPrintingMethods.vpd11s(this.fieldHeadRec.getPrln(), 
											this.fieldHeadRec.getMctl(),
											"      ", 1, this.fieldParent);
								}
								else
								{
									MFSPrintingMethods.vpd11s(this.fieldHeadRec.getPrln(), 
											this.fieldHeadRec.getMctl(),
											"Card " + this.fieldMifCardLetter, 1,
											this.fieldParent);
								}
							}
							// fix the funny cursor problem
							this.fieldParent.setCursor(Cursor.getDefaultCursor());

						} // end 80 byte mif
					}
				}//end of good retrieve of mif data
			}// end of good return from rtv_vpddata
		}/* end try - now catch */
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(this.fieldParent, null, null, e);
		}
		return rc;
	}

	/**
	 * @return the return code
	 */
	public int processMifUnBurn()
	{
		final MFSConfig config = MFSConfig.getInstance();
		int rc = 0;
		try
		{
			/* this sends the the product line plus some zeros to the AS/400 to */
			/* read the VD10 records - this will return all of those fields from the VD10 */
			/* -most importantly it will return the MIF_PN which will be used to get the MIF */
			/* data from CPPCP010/MIF */
			String errorString = "";
			String ecid_hex = "";
			String bkpn = "00000" + this.fieldHeadRec.getPrln().substring(0, 7);
			//~7C Convert RTV_VD to XML and use MFSVPDRec
			IGSXMLTransaction rtv_vd = callRTV_VD(bkpn);
			rc = rtv_vd.getReturnCode();

			if (rc != 0)
			{
				IGSMessageBox.showOkMB(this.fieldParent, null, rtv_vd.getErms(), null);
			}
			else
			{
				//For UNBURN, use flag bits of 0 (false for calcFlags). 
				//We don't care what it was, we just want to erase it.
				this.fieldVPDRec = new MFSVPDRec(rtv_vd, false);
				if (this.fieldVPDRec.reburn.equals("Y"))
				{
					processMifReburn();
				}
				else
				{
					String data1 = "RTV_MIF   " + this.fieldVPDRec.mifPN.substring(5);
					common.Comm myComm1 = new Comm(config, data1);
					myComm1.setInput(data1);
					if (config.containsConfigEntry("VPDSRV") && config.containsConfigEntry("VPDRTR"))
					{
						myComm1.setServer(config.getConfigValue("VPDSRV"));
						myComm1.setPort(config.getConfigValue("VPDRTR"));

						//~6C Moved threading and update logic to executeRunnable
						executeRunnable(myComm1, "Retrieving MIF Data, Please Wait...");
						
						rc = myComm1.getRc();
						if (rc != 0)
						{
							errorString = myComm1.getData();
						}
					}
					else
					{
						rc = 10;
						errorString = "Missing Config File Entries: VPDSRV and/or VPDRTR";
					}

					if (rc != 0)
					{
						IGSMessageBox.showOkMB(this.fieldParent, null, errorString, null);
					}
					else // successfully retrieved mif data
					{
						String mifdta = "";
						boolean keywordMif = false;
						boolean squadronMif = false;
						if (myComm1.getData().substring(0, 3).equals("KEY"))
						{
							keywordMif = true;
						}
						else if (myComm1.getData().substring(0, 3).equals("SQU"))
						{
							squadronMif = true;
						}

						if (keywordMif || squadronMif)
						{
							//mifdta = processKeyWordMif(myComm1.getData().substring(3));
							this.fieldDllMifdata = myComm1.getData().substring(3);

							byte[] byte_array = this.fieldDllMifdata.getBytes();

							//PERFORM BURN STUFF HERE
							/* setup serial number string */
							String insq = this.fieldHeadRec.getSapn().substring(0, 12);

							/* get the part number for passing to VPD burn function */
							String partNum = "00000" + this.fieldHeadRec.getPrln().substring(0, 7);

							/* build an 11S string */
							String elevens = "11S" + partNum.substring(5, 12) + insq;

							/* build an ECID string, just a dummy for an erase */
							ecid_hex = "0000000000000000";

							//default is to use black box for now - if NOBLACKBOX use old DLL
							if (config.containsConfigEntry("NOBLACKBOX"))
							{
								if (squadronMif)
								{
									this.fieldParent.setCursor(Cursor.getDefaultCursor());
									rc = 100;
									String erms = "Client Not Configured to handle Squadron Format!";
									IGSMessageBox.showOkMB(this.fieldParent, null, erms, null);
								}
								else
								{
									//~5 removed extraneous parms
									rc = keywordOldDLL(byte_array,
											ecid_hex, 0, partNum, insq, elevens,
											"UNBURN");
								}
							}
							else
							{
								rc = keywordSquadronDLL(byte_array, ecid_hex, 0, partNum,
										insq, elevens, "UNBURN", squadronMif);
							}

						}// end of keyword mif
						else // 80 byte mif
						{
							//default is to use black box for now - if NOBLACKBOX use old DLL
							if (config.containsConfigEntry("NOBLACKBOX"))
							{
								//~5 removed extraneous parms
								rc = eightyByteOldDLL(mifdta, "UNBURN");
							}
							else
							{
								//~5 removed extraneous parms
								rc = eightyByteSquadronDLL(mifdta, ecid_hex, "UNBURN");
							}
						} // end 80 byte mif
					}//end of good retrieve of mif data
				}// end of not reburn
			}// end of good return from rtv_vpddata
		}/* end try - now catch */
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(this.fieldParent, null, null, e);
		}
		return rc;
	}

	/**
	 * Creation date: (12/10/2003 12:40:03 PM)
	 * @param data the data to write
	 * @param pn the part number
	 * @return the filename
	 */
	private String writeVPDFile80Byte(String data, String pn)
	{
		char[] char_array = new char[data.length() / 2];

		//~6A Used getMifsDirectory
		File mifsDirectory = getMifsDirectory();

		for (int i = 0; i < data.length(); i += 2)
		{
			int intie = (Integer.valueOf(data.substring(i, i + 2), 16)).intValue();
			char_array[i / 2] = (char) intie;
		}
		String data2 = new String(char_array);

		String fileName = "A" + pn.trim() + ".vpd";
		try
		{
			//~6A Used new File(mifsDirectory, fileName)
			FileOutputStream fos = new FileOutputStream(new File(mifsDirectory, fileName));
			fos.write(data2.getBytes());
			fos.flush();
			fos.close();
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(this.fieldParent, null, null, e);
		}
		return fileName;
	}

	/**
	 * Creation date: (12/10/2003 12:40:03 PM)
	 * @param byteArray the data to write
	 * @param pn the part number
	 * @return the filename
	 */
	private String writeVPDFileKeyWord(byte[] byteArray, String pn)
	{
		//~6A Used getMifsDirectory
		File mifsDirectory = getMifsDirectory();

		String fileName = "A" + pn.trim() + ".vpd";
		try
		{
			//~6A Used new File(mifsDirectory, fileName)
			FileOutputStream fos = new FileOutputStream(new File(mifsDirectory, fileName));
			fos.write(byteArray);
			fos.flush();
			fos.close();
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(this.fieldParent, null, null, e);
		}
		return fileName;
	}

	//~6A New method
	/**
	 * Returns the mifs directory.
	 * @return a <code>File</code> for the mifs directory
	 */
	private File getMifsDirectory()
	{
		File mifsDirectory = IGSFileUtils.getFile("mifs");
		if (!mifsDirectory.exists())
		{
			// The mifs directory does not exist, so create it.
			mifsDirectory.mkdir();
		}
		return mifsDirectory;
	}
	
	/**
	 * Calls the RTV_VD transaction.
	 * <p>
	 * Note: The input for RTV_VD is a Book Part Number (BKPN), which is the
	 * same as 00000 + the first 7 characters of the Product Line (PRLN).
	 * @param bkpn the book part number
	 * @return the <code>IGSXMLTransaction</code> for RTV_VD
	 */
	private IGSXMLTransaction callRTV_VD(String bkpn)
	{
		IGSXMLTransaction rtv_vd = new IGSXMLTransaction("RTV_VD"); //$NON-NLS-1$
		rtv_vd.startDocument();
		rtv_vd.addElement("BKPN", bkpn); //$NON-NLS-1$
		rtv_vd.endDocument();
		rtv_vd.setActionMessage("Retrieving VD Data, Please Wait...");
		MFSComm.getInstance().execute(rtv_vd, this.fieldActionable);
		return rtv_vd;
	}
	
	//~7A New class to handle return XML from RTV_VD
	/**
	 * <code>MFSVPDRec</code> is a data structure for the vital product data
	 * information retrieved from the FCSPVD10 by RTV_VD.
	 * @author The MFS Client Development Team
	 */
	public static class MFSVPDRec
	{
		/** MIF Part Number */
		final String mifPN;

		/** MIF Label Type */
		final String mifLabelType;

		/** Card Usage */
		final String usage;

		/** SPCN Port Number */
		final String port;

		/** SPCN Port Number (as an int) */
		final int portInt;

		/** SPCN Port Number (as a HEX String) */
		final String portHexString;

		/** CHIPSN Type */
		final String chipType;

		/** CHIPSN to Use */
		final String chips;

		/** CHIPSN Write Data */
		final String chipw;

		/** Burn Port */
		final String burnPort;

		/** ECID Data Required Indicator */
		final String ecid;

		/** Reburn Indicator */
		final String reburn;

		/** iSeries or pSeries */
		final String iOrP;

		/** Voltage */
		final String voltage;
		
		/** redundancy flag indicator */
		final String redflagind;

		/** MIF Flags Bitmask (calculated from other fields) */
		final long mifFlags;

		
		/**
		 * Constructs a new <code>MFSVPDRec</code>.
		 * @param rtv_vd the <code>IGSXMLTransaction</code> for RTV_VD
		 * @param calcFlags <code>true</code> if the MIF Flags Bitmask should
		 *        be calculated; <code>false</code> if the MIF Flags Bitmask
		 *        should be 0
		 */
		public MFSVPDRec(IGSXMLTransaction rtv_vd, boolean calcFlags)
		{
			super();
			this.mifPN = rtv_vd.getNextElement("MIFP"); //$NON-NLS-1$
			this.mifLabelType = rtv_vd.getNextElement("LABT"); //$NON-NLS-1$
			this.usage = rtv_vd.getNextElement("CRDU"); //$NON-NLS-1$
			this.port = rtv_vd.getNextElement("PORT"); //$NON-NLS-1$
			this.portInt = new Integer(this.port).intValue();
			String string = Integer.toHexString(this.portInt).toUpperCase();
			this.portHexString = "00".substring(0, 2 - string.length()) + string; //$NON-NLS-1$
			this.chipType = rtv_vd.getNextElement("CHPT"); //$NON-NLS-1$
			this.chips = rtv_vd.getNextElement("CHPS"); //$NON-NLS-1$
			this.chipw = rtv_vd.getNextElement("CHPW"); //$NON-NLS-1$
			this.burnPort = rtv_vd.getNextElement("BRNP"); //$NON-NLS-1$);
			this.ecid = rtv_vd.getNextElement("EDRI"); //$NON-NLS-1$);
			this.reburn = rtv_vd.getNextElement("RBRN"); //$NON-NLS-1$
			this.iOrP = rtv_vd.getNextElement("IORP"); //$NON-NLS-1$
			this.voltage = rtv_vd.getNextElement("VOLT"); //$NON-NLS-1$
			this.redflagind = rtv_vd.getNextElement("REFI"); //$NON-NLS-1$ // ~8A

			long flags = 0;
			if (calcFlags)
			{
				/*
				 * Depending on the values retrieved from the VD10, set the
				 * proper bitwise fields - logic stolen from OS/2 code -
				 * function VPD_TEST
				 */
				if (this.usage.equals("R")) /* RS/6000 *///$NON-NLS-1$
				{
					flags |= 0x00000001;
				}
				if (this.chipType.equals("01")) //$NON-NLS-1$
				{
					flags |= 0x00000010;
				}
				if (this.chipw.equals("1")) //$NON-NLS-1$
				{
					flags |= 0x00000040;
				}
				if (this.chips.equals("1")) //$NON-NLS-1$
				{
					flags |= 0x00000080;
				}
			}
			this.mifFlags = flags;
		}
	}
}
