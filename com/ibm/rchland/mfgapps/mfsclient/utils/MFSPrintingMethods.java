/* © Copyright IBM Corporation 2003, 2010. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2005-09-27   ~1 31867EM                   -add software liscense label printing for new RSS products
 *                                            add method softKeycode
 * 2006-06-12   ~2 34414JJ  JL. Woodward     -New Printing Methods: rsspcpp, caseContent
 *                                           -Cleanup unused variables.
 * 2006-08-01   ~3 35789JM  JL Woodward      -Change error message being sent when
 *                                            trx rsspcpp failed 
 * 2006-08-23   ~4 36043JM  Dave Ficht       -ensure, we pass proper CNTR to rtv_iprsheet 
 * 2006-08-25   ~5 34544JM  MSBARTH          -New Printing Method: elevensDwnBin
 * 2006-09-08   ~6 35701DB  Luis M.          -New Printing Method: matpMdlSn
 * 2007-01-10   ~7 36302JM  MSBARTH          -New Printing Method: assemblyLbl
 * 2007-02-27   ~8 37452MZ  Toribio H        -New Printing Method: UIDLbl
 * 2007-03-06   ~9 35603JM  Toribio H        -Change Error Message in SysPassword PrintMethod
 * 2007-03-09      34242JR  R Prechel        -Java 5 version; Constructor private
 *                                           -Methods static; No Configuration parameter
 *                                           -Use IGSMessageBox.showOkMB
 * 2007-03-13  ~10 35208JM  R Prechel        -New Printing Method: keyInputBarcode
 *                                           -Note: Checked into CMVC with 34242JR
 * 2007-04-09  ~11 38167JM  R Prechel        -Create and display message dialogs on AWT Event Dispatch Thread
 * 2007-04-26  ~12 37241JM  R Prechel        -New Printing Methods: wusnlbls and matpmcsn2
 * 2007-05-14  ~13 38139JM  R Prechel        -ASSEMBLYTREE and PARTINFOSHEET changes
 * 2007-08-14  ~14 39572KM  R Prechel        -Remove width parameter from vinlabel method
 * 2007-07-24  ~14 38768JL  Martha Luna      -Update the RSSPCPP print method to be a generic pcpplabels 
 *                                            method with a new parameter to pass the brand
 * 2007-08-21  ~15 38131JL  VH Avila         -Add parameters to WUSNLBLS method
 *                                           -Add the new syscertlbls(...) method
 * 2007-09-24  ~16 39990JM  VH Avila         -Add the Auto parameter to the wusnlbls method
 * 2007-09-27  ~17 39990JM  R Prechel        -Use the auto parameter
 * 2007-11-08  ~18 40334JM  Toribio H.       -Add matpmcsn2 parameter to wusnlbls method
 * 2007-12-03  ~19 40405FR  VH Avila         -Add the new tcodMinutes method
 *                                           -Send the new TYPE parameter to the print on tcodBilling method
 * 2008-04-23  ~20 40764EM  Santiago D       -Support KEYCODESLONG long label for new hydra release
 * 2008-05-08  ~21 39568MZ  D Pietrasik      -add smartSerial, add check for Smart Serial in lprsheet
 * 2008-05-20  ~22 41268MZ  D Pietrasik      -update caseContent to accept mctl
 * 2008-06-20  ~23 38990JL  Santiago D       -New generic printlabel method
 * 2008-07-31  ~24 39375PP  Santiago D       -New cooCntr method for Country of Origin label 
 * 2008-10-07  ~25 42937JM  Santiago D       -Put qty inside the xmlString for the printlabel method
 * 2009-03-25      44544GB  Santiago D       -Remove mifManual, mifPrln and mifmctl methods
 * 2009-07-15  ~26 41330JL  Sayde Alcantara	 -New containerMESF method to print CASECONTENTMESF label
 * 2009-08-20  ~27 41901TL  Santiago D  	 -New generic printlabel method
 * 2009-12-15  ~28 43628JL  Santiago D       -dasdp() remake. Delegate work to generic printlabel method
 * 2010-02-18  ~29 46810JL  J Mastachi       -Modify parameters to warrantyCard method
 * 2010-02-18  ~30 42558JL  D Mathre		 -added new method to process On Demand label printing calls
 * 2010-03-04  ~31 46810JL  J Mastachi       -Do not run printlabel on a new thread for Option Warranty Cards
 * 2010-03-09  ~32 47595MZ  Toribio H        -Efficiency Changes
 * 2010-03-09  ~33 42558JL  Santiago SC      -clean onDemandLabel printing method
 * 2010-05-16  ~34 46870JL  Edgar Mercado    -Add cname parameter to carton method for CARTON label and
 *                                            this way to print COO legend.
 *                                           -Add cnametxt to FRUNUMBSN and FRUNUMBNOSN labels.
 * 2010-06-21  ~35 47613MZ  Mike Lawson      -Update SMARTSERIAL logic to not print unless ISS.
 * 2010-11-03  ~36 48873JM  Luis M.          -New FoD method for Feature on Demand.                                  
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.utils;

import java.awt.EventQueue;
import java.util.Hashtable;
import java.util.List;

import com.ibm.rchland.mfgapps.client.utils.messagebox.IGSMessageBox;
import com.ibm.rchland.mfgapps.client.utils.xml.IGSXMLTransaction;
import com.ibm.rchland.mfgapps.mfsclient.MFSFrame;
import com.ibm.rchland.mfgapps.mfsclient.struct.MFSOnDemandKeyData;
import com.ibm.rchland.mfgapps.mfscommon.MFSConfig;
import com.ibm.rchland.mfgapps.mfscommon.MFSPrintDriver;
import com.ibm.rchland.mfgapps.mfscommon.MFSTableModel;


/**
 * <code>MFSPrintingMethods</code> contains methods to print labels and sheets.
 * @author The MFS Client Development Team
 */
public class MFSPrintingMethods
{
	/**
	 * Constructs a new <code>MFSPrintingMethods</code>. This class only has
	 * static methods and does not have any instance variables or instance
	 * methods. Thus, there is no reason to create an instance of
	 * <code>MFSPrintingMethods</code>, so the only constructor is declared
	 * <code>private</code>.
	 */
	
	private MFSPrintingMethods()
	{
		super();
	}
	
	//~11A New Method
	/**
	 * Calls {@link IGSMessageBox#showOkMB} on the AWT Event Dispatch Thread.
	 * @param frame the <code>MFSFrame</code> from which the printing method is called
	 * @param message the message to display
	 */
	protected static void showOkMB(final MFSFrame frame, final String message)
	{
		if (EventQueue.isDispatchThread())
		{
			IGSMessageBox.showOkMB(frame, null, message, null);
		}
		else
		{
			EventQueue.invokeLater(new Runnable()
			{
				public void run()
				{
					IGSMessageBox.showOkMB(frame, null, message, null);
				}
			});
		}
	}
	/**  ~32
	 * Calls {@link IGSMessageBox#showOkMBTimer} on the AWT Event Dispatch Thread.
	 * @param frame the <code>MFSFrame</code> from which the printing method is called
	 * @param message the message to display
	 * @param timeForTimer the time for the timer
	 */
	protected static void showOkMBTimer(final MFSFrame frame, final String message, final String timeForTimer)
	{
		if (EventQueue.isDispatchThread())
		{
			IGSMessageBox.showOkMBTimer(frame, null, message, null, timeForTimer);
		}
		else
		{
			EventQueue.invokeLater(new Runnable()
			{
				public void run()
				{
					IGSMessageBox.showOkMBTimer(frame, null, message, null, timeForTimer);
				}
			});
		}
	}

	/**
	 * @param mfgn
	 * @param idss
	 * @param orno
	 * @param mctl
	 * @param qty
	 * @param parent
	 */
	public static void advFeatModel(final String mfgn, final String idss,
									final String orno, final String mctl, final int qty,
									final MFSFrame parent)
	{
		/* Instantiate Worker Thread */
		Thread t = new Thread()
		{
			public void run()
			{
				int rc = 0;
				MFSPrintDriver pd = new MFSPrintDriver();
				pd.addValue("MFGN", mfgn);
				pd.addValue("IDSS", idss);
				pd.addValue("ORNO", orno);
				pd.addValue("MCTL", mctl);
				rc = pd.print("ADVFEATMODEL", qty);

				if (rc != 0)
				{
					String erms = "Printer Error: Unable to print Advance Feature Model Sheet.";
					showOkMB(parent, erms); //~11C
				}
				pd.closeConnections();
			}
		};

		/* Spawn thread */
		t.start();
	}

	/**
	 * @param text
	 * @param qty
	 * @param parent
	 */
	public static void alltext(final String text, final int qty, final MFSFrame parent)
	{
		/* Instantiate Worker Thread */
		Thread t = new Thread()
		{
			public void run()
			{
				int rc = 0;
				MFSPrintDriver pd = new MFSPrintDriver();
				pd.addValue("TEXT", text);
				rc = pd.print("ALLTEXT", qty);

				if (rc != 0)
				{
					String erms = "Printer Error: Unable to print Text Label.";
					showOkMB(parent, erms); //~11C
				}

				pd.closeConnections();
			}
		};

		/* Spawn thread */
		t.start();
	}

	/**
	 * @param text
	 * @param text2
	 * @param qty
	 * @param parent
	 */
	public static void alltext2(final String text, final String text2, final int qty,
								final MFSFrame parent)
	{
		/* Instantiate Worker Thread */
		Thread t = new Thread()
		{
			public void run()
			{
				int rc = 0;
				MFSPrintDriver pd = new MFSPrintDriver();
				pd.addValue("TEXT", text);
				pd.addValue("TEXT2", text2);
				rc = pd.print("ALLTEXT2", qty);

				if (rc != 0)
				{
					String erms = "Printer Error: Unable to print Text Label.";
					showOkMB(parent, erms); //~11C
				}

				pd.closeConnections();
			}
		};

		/* Spawn thread */
		t.start();
	}

	//~7A
	/**
	 * Prints a new Assembly Label for the given MCTL.
	 * @param mctl
	 * @param qty
	 * @param parent
	 */
	public static void assemblyLbl(final String mctl, final int qty, final MFSFrame parent)
	{
		/* Instantiate Worker Thread */
		Thread t = new Thread()
		{
			public void run()
			{
				int rc = 0;
				MFSPrintDriver pd = new MFSPrintDriver();
				pd.addValue("MCTL", mctl); //$NON-NLS-1$
				rc = pd.print("ASSEMBLYLBL", qty); //$NON-NLS-1$

				if (rc != 0)
				{
					String erms = "Printer Error: Unable to print Assembly Label.";
					showOkMB(parent, erms); //~11C
				}

				pd.closeConnections();
			}
		};

		/* Spawn thread */
		t.start();
	}
	
	//~13C Remove transaction parameter; change TreeModel parameter to List
	/**
	 * @param qd10Data
	 * @param treeList
	 * @param qty
	 * @param parent
	 */
	@SuppressWarnings("rawtypes")
	public static void assemblyTree(final Hashtable qd10Data,
									final List treeList, final int qty,
									final MFSFrame parent)
	{
		/* Instantiate Worker Thread */
		Thread t = new Thread()
		{
			public void run()
			{
				int rc = 0;
				MFSPrintDriver pd = new MFSPrintDriver();
				pd.addValue("QD10Data", qd10Data);
				pd.addValue("TreeList", treeList); //~13C

				rc = pd.print("ASSEMBLYTREE", qty);

				// -1 return code means that rtv_lbldta returned ********* to
				// print server code
				if (rc == 666)
				{
					//Do not display a message if rc == 666
				}
				else if (rc == 0)
				{
					String erms = "Assembly Tree was Printed";
					showOkMB(parent, erms); //~11C
				}
				else
				{
					String erms = "Printer Error: Unable to print Assembly Tree";
					showOkMB(parent, erms); //~11C
				}

				pd.closeConnections();

				parent.update(parent.getGraphics());
			}
		};

		/* Spawn thread */
		t.start();
	}

	/**
	 * Prints the carrier label.
	 * @param mctl
	 * @param qty
	 * @param parent
	 */
	public static void carrier(final String mctl, final int qty, final MFSFrame parent)
	{
		/* Instantiate Worker Thread */
		Thread t = new Thread()
		{
			public void run()
			{
				int rc = 0;
				MFSPrintDriver pd = new MFSPrintDriver();
				pd.addValue("MCTL", mctl);
				rc = pd.print("CARRIER", qty);

				if (rc != 0)
				{
					String erms = "Printer Error: Unable to print CARRIER Label.";
					showOkMB(parent, erms); //~11C
				}

				pd.closeConnections();
			}
		};

		/* Spawn thread */
		t.start();
	}

	// ~23C
	/**
	 * @param mctl
	 * @param lbts
	 * @param qty
	 * @param parent
	 */
	public static void carriercomn(final String mctl, final String lbts, final int qty, final MFSFrame parent)
	{
		String lbldta = "<MCTL>"+mctl+"</MCTL>";
		
		printlabel("CARRIERCOMN", lbts, "", lbldta, "", qty, parent);		
	}
	
    // ~27A
	/**
	 * @param lbldta
	 * @param lbts
	 * @param lbtk
	 * @param qty
	 * @param plom
	 * @param parent
	 */
	public static int warrantyCard(final String lbldta, final String lbts, final String lbtk, final int qty, final String plom, final MFSFrame parent) //~29C~31C
	{
		int rc = 0; //~31A
		
		rc = printlabel("WARRANTYCARD", lbts, lbtk, lbldta, plom, qty, parent); //~29C
		
		return rc; //~31A
	}
	
    // ~27A
	/**
	 * @param mctl
	 * @param cntr
	 * @param lbts
	 * @param qty
	 * @param parent
	 */
	public static void weightLabel(final String mctl, final String cntr, final String lbts, final int qty, final MFSFrame parent)
	{
		String lbldta = "<MCTL>"+mctl+"</MCTL><CNTR>"+cntr+"</CNTR>";
		
		printlabel("WEIGHTLBL", lbts, "", lbldta, "", qty, parent);		
	}
	
	// ~23A PrintLabel
	// ~31C
	/**
	 * @param lblt: label type 
	 * @param lbts: label trigger source
	 * @param lbtk: label trigger key and label trigger value
	 * @param lbldta: label data
	 * @param plom: plant of manufacturing
	 * @param qty : quantity
	 * @param parent
	 * @return the return code
	 */
	
	// ~36A
	/**
	 * @param mfgn
	 * @param idss
	 * @param mctl
	 * @param lbts
	 * @param qty
	 * @param parent
	 */
	public static void fodLabel(final String mfgn, final String idss, final String mctl, final String lbts,
									final int qty, final MFSFrame parent)
	{
		/* Instantiate Worker Thread */
		Thread t = new Thread()
		{
			public void run()
			{
				int rc = 0;
				MFSPrintDriver pd = new MFSPrintDriver();
				pd.addValue("MFGN", mfgn);
				pd.addValue("IDSS", idss);
				pd.addValue("MCTL", mctl);
				pd.addValue("LBLT", "FODLABEL");
				pd.addValue("LBTS",lbts);

				rc = pd.print("FODLABEL", qty);

				if (rc != 0)
				{
					String erms = "Printer Error: Unable to print FoD Label.";
					showOkMB(parent, erms); 
				}

				pd.closeConnections();
			}
		};

		/* Spawn thread */
		t.start();
	}
	
    // ~36A
	
	public static int printlabel(final String lblt, final String lbts, final String lbtk, final String lbldta, final String plom, final int qty, final MFSFrame parent)
	{
		int rc = 0;
		  
		final StringBuffer xmlString = new StringBuffer();	//~25C	
		
		/* ~33A Start - Tags moved */
		xmlString.append("<LBLT>");
		xmlString.append(lblt);
		xmlString.append("</LBLT>");
		xmlString.append("<LBTS>");
		xmlString.append(lbts);
		xmlString.append("</LBTS>");
		xmlString.append("<QTY>");
		xmlString.append(qty);
		xmlString.append("</QTY>");
		/* ~33A  End - Tags moved */
				
		if(lbtk.compareTo("") != 0) //~33C Part Printing
		{  
			xmlString.append("<LBTK>");
			xmlString.append(lbtk);
			xmlString.append("</LBTK>");			
			
			if(lbldta.compareTo("") != 0) //~33A
			{
				xmlString.append("<LBLDTA>");
				xmlString.append(lbldta);
				xmlString.append("</LBLDTA>");		
			}

			if(plom.contains("MCTL")) // ~29C We might send an MCTL tag on the plom field for Option Warranty Cards
			{
				xmlString.append(plom);
			}
			else
			{
				xmlString.append("<PLOM>");
				xmlString.append(plom);
				xmlString.append("</PLOM>");
			}
		}
		else if(!lbldta.contains("MCTL") && !plom.equals("")) //~33A On Demand
		{
			xmlString.append("<LBLDTA>");
			xmlString.append(lbldta);
			xmlString.append("</LBLDTA>");	
			xmlString.append("<PLOM>");
			xmlString.append(plom);
			xmlString.append("</PLOM>");
		}
		else
		{
			xmlString.append("<LBLDTA>");
			xmlString.append(lbldta);
			xmlString.append("</LBLDTA>");				
		}
		
		if (lbts.equals("OPTWNTYPRINT"))
		{
			rc = labelprint(xmlString);
		}
		else
		{
			/* Instantiate Worker Thread */
			Thread t = new Thread()
			{
				public void run()
				{
					int rc = 0;
					
					rc = labelprint(xmlString);
					
					if (rc != 0)
					{
						String erms = "Printer Error: Unable to print "+lblt+" Label.";
						showOkMB(parent, erms);
					}
				}
			};

			/* Spawn thread */
			t.start();
		}
		return rc;
	}

	// ~31A
	/**
	 * Print Driver call for {@link #printlabel(String, String, String, String, String, int, MFSFrame)}
	 * @param xmlString 
	 * @return the return code
	 */
	private static int labelprint(StringBuffer xmlString)
	{
		int rc = 0;
		MFSPrintDriver pd = new MFSPrintDriver();
		pd.addValue("LBLTRIG", xmlString.toString());		
		rc = pd.print("PRINTLABEL", 1);
		pd.closeConnections();
		return rc;
	}
	
	/**
	 * Prints the Key Codes label.
	 * @param matp
	 * @param mmdl
	 * @param mspi
	 * @param mcsn
	 * @param part
	 * @param cooc
	 * @param qty
	 * @param parent
	 */
	public static void carton(final String matp, final String mmdl, final String mspi,
								final String mcsn, final String part, final String cooc,
								final String cname, final int qty, final MFSFrame parent)        //~34C
	{
		/* Instantiate Worker Thread */
		Thread t = new Thread()
		{
			public void run()
			{
				int rc = 0;
				MFSPrintDriver pd = new MFSPrintDriver();
				pd.addValue("MATP", matp);
				pd.addValue("MMDL", mmdl);
				pd.addValue("MSPI", mspi);
				pd.addValue("MCSN", mcsn);
				pd.addValue("PART", part);
				pd.addValue("COOC", cooc);
				pd.addValue("CNAME", cname);                                                     //~34A
				rc = pd.print("CARTON", qty);

				if (rc != 0)
				{
					String erms = "Printer Error: Unable to print CARTON Label.";
					showOkMB(parent, erms); //~11C
				}

				pd.closeConnections();
			}
		};

		/* Spawn thread */
		t.start();
	}//end CARTON label

	/**
	 * @param mctl
	 * @param qty
	 * @param parent
	 */
	public static void confnode(final String mctl, final int qty, final MFSFrame parent)
	{
		/* Instantiate Worker Thread */
		Thread t = new Thread()
		{
			public void run()
			{
				int rc = 0;
				MFSPrintDriver pd = new MFSPrintDriver();
				pd.addValue("MCTL", mctl);
				rc = pd.print("CONFNODE", qty);

				if (rc != 0)
				{
					String erms = "Printer Error: Unable to print Configured Node Label.";
					showOkMB(parent, erms); //~11C
				}

				pd.closeConnections();
			}
		};

		/* Spawn thread */
		t.start();
	}

	/**
	 * @param mctl
	 * @param qty
	 * @param parent
	 */
	public static void confnoder(final String mctl, final int qty, final MFSFrame parent)
	{
		/* Instantiate Worker Thread */
		Thread t = new Thread()
		{
			public void run()
			{
				int rc = 0;
				MFSPrintDriver pd = new MFSPrintDriver();
				pd.addValue("MCTL", mctl);
				rc = pd.print("CONFNODER", qty);

				if (rc != 0)
				{
					String erms = "Printer Error: Unable to print Configured Node (Regatta) Label.";
					showOkMB(parent, erms); //~11C
				}

				pd.closeConnections();
			}
		};

		/* Spawn thread */
		t.start();
	}

	/**
	 * @param prln
	 * @param who
	 * @param mctl
	 * @param qty
	 * @param parent
	 */
	public static void confseri(final String prln, final String who, final String mctl,
								final int qty, final MFSFrame parent)
	{
		/* Instantiate Worker Thread */
		Thread t = new Thread()
		{
			public void run()
			{
				int rc = 0;
				MFSPrintDriver pd = new MFSPrintDriver();
				pd.addValue("PRLN", prln);
				pd.addValue("WHO1", who);
				pd.addValue("MCTL", mctl);
				rc = pd.print("CONFSERI", qty);

				if (rc != 0)
				{
					String erms = "Printer Error: Unable to print Configured Serialized Node Label.";
					showOkMB(parent, erms); //~11C
				}

				pd.closeConnections();
			}
		};

		/* Spawn thread */
		t.start();
	}

	/**
	 * @param cntr
	 * @param mctl
	 * @param qty
	 * @param parent
	 */
	public static void container(final String cntr, final String mctl, final int qty,
									final MFSFrame parent)
	{
		/* Instantiate Worker Thread */
		Thread t = new Thread()
		{
			public void run()
			{
				int rc = 0;
				MFSPrintDriver pd = new MFSPrintDriver();
				pd.addValue("MCTL", cntr);
				pd.addValue("CNTR", mctl);
				rc = pd.print("RTVCTRLBL", qty);

				if (rc != 0)
				{
					String erms = "Printer Error: Unable to print Container Label.";
					showOkMB(parent, erms); //~11C
				}

				pd.closeConnections();
			}
		};

		/* Spawn thread */
		t.start();
	}
	
	// ~26A
	/**
	 * @param cntr
	 * @param mctl
	 * @param qty
	 * @param parent
	 */
	public static void containerMESF(final String cntr, final String mctl, final int qty,
									final MFSFrame parent)
	{
		/* Instantiate Worker Thread */
		Thread t = new Thread()
		{
			public void run()
			{
				int rc = 0;
				MFSPrintDriver pd = new MFSPrintDriver();
				pd.addValue("MCTL", mctl);
				pd.addValue("CNTR", cntr);
				rc = pd.print("CASECONTENTMESF", qty);

				if (rc != 0)
				{
					String erms = "Printer Error: Unable to print Container MESF Label.";
					showOkMB(parent, erms);
				}

				pd.closeConnections();
			}
		};

		/* Spawn thread */
		t.start();
	}	
	
	//~24A
	/**
	 * @param mctl
	 * @param cntr
	 * @param qty
	 * @param parent
	 */
	public static void cooCntr(final String mctl, final String cntr, final int qty, final MFSFrame parent)
	{
		/* Instantiate Worker Thread */
		Thread t = new Thread()
		{
			public void run()
			{
				int rc = 0;
				MFSPrintDriver pd = new MFSPrintDriver();
				pd.addValue("MCTL", mctl);
				pd.addValue("CNTR", cntr);
				rc = pd.print("COOCNTR", qty);

				if (rc != 0)
				{
					String erms = "Printer Error: Unable to print Country of Origin (CoO) Cntr Label.";
					showOkMB(parent, erms);
				}

				pd.closeConnections();
			}
		};

		/* Spawn thread */
		t.start();
	}		

	/**
	 * @param mctl
	 * @param qty
	 * @param parent
	 */
	public static void ctrlnumbr(final String mctl, final int qty, final MFSFrame parent)
	{
		/* Instantiate Worker Thread */
		Thread t = new Thread()
		{
			public void run()
			{
				int rc = 0;
				MFSPrintDriver pd = new MFSPrintDriver();
				pd.addValue("MCTL", mctl);
				rc = pd.print("CTRLNUMBR", qty);

				if (rc != 0)
				{
					String erms = "Printer Error: Unable to print Work Unit Label.";
					showOkMB(parent, erms); //~11C
				}

				pd.closeConnections();
			}
		};

		/* Spawn thread */
		t.start();
	}

	/**
	 * @param mfgn
	 * @param idss
	 * @param mcsn
	 * @param featureCode
	 * @param pccsn
	 * @param pccid
	 * @param activeQty
	 * @param orderQty
	 * @param activationCode
	 * @param orno
	 * @param activePartNumber
	 * @param resourceId
	 * @param demandType
	 * @param brandLine
	 * @param qty
	 * @param parent
	 */
	public static void cuodkey(final String mfgn, final String idss, final String mcsn,
								final String featureCode, final String pccsn,
								final String pccid, final String activeQty,
								final String orderQty, final String activationCode,
								final String orno, final String activePartNumber,
								final String resourceId, final String demandType,
								final String brandLine, final int qty,
								final MFSFrame parent)
	{
		/* Instantiate Worker Thread */
		Thread t = new Thread()
		{
			public void run()
			{
				int rc = 0;
				MFSPrintDriver pd = new MFSPrintDriver();
				pd.addValue("MFGN", mfgn);
				pd.addValue("IDSS", idss);
				pd.addValue("MCSN", mcsn);
				pd.addValue("FEATCODE", featureCode);
				pd.addValue("PCCSN", pccsn);
				pd.addValue("PCCID", pccid);
				pd.addValue("ACTIVEQTY", activeQty);
				pd.addValue("ORDERQTY", orderQty);
				pd.addValue("PACTCODE", activationCode);
				pd.addValue("ORNO", orno);
				pd.addValue("ACTIVEPARTNUM", activePartNumber);
				pd.addValue("RESOURCEID", resourceId);
				pd.addValue("DEMANDTYPE", demandType);
				pd.addValue("BRANDLINE", brandLine);
				pd.addValue("JUNK", "                        ");
				rc = pd.print("RTVCUODKY", qty);
				// 666 return code means that rtv_podkey returned ********* to
				// print server code
				if (rc == 666)
				{
					//Do not display a message if rc == 666
				}
				else if (rc != 0)
				{
					String erms = "Printer Error: Unable to print POD Key Sheet.";
					showOkMB(parent, erms); //~11C
				}

				pd.closeConnections();
			}
		};

		/* Spawn thread */
		t.start();
	}

	//~28C
	/**
	 * @param mctl
	 * @param pilotFlag
	 * @param qty
	 * @param parent
	 */
	public static void dasdp(final String mctl, final String pilotFlag, final int qty,
								final MFSFrame parent)
	{
		String lbldta = "<MCTL>"+mctl+"</MCTL><PILF>"+pilotFlag+"</PILF>";

		printlabel("DASDP", "                    ", "", lbldta, "", qty, parent);
	}

	/**
	 * @param mfgn
	 * @param idss
	 * @param orno
	 * @param mctl
	 * @param qty
	 * @param parent
	 */
	public static void deconfigLoc(final String mfgn, final String idss,
									final String orno, final String mctl, final int qty,
									final MFSFrame parent)
	{
		/* Instantiate Worker Thread */
		Thread t = new Thread()
		{
			public void run()
			{
				int rc = 0;
				MFSPrintDriver pd = new MFSPrintDriver();
				pd.addValue("MFGN", mfgn);
				pd.addValue("IDSS", idss);
				pd.addValue("ORNO", orno);
				pd.addValue("MCTL", mctl);
				rc = pd.print("DECONFIGLOC", qty);

				if (rc == 666)
				{
					//Do not display a message if rc == 666
				}
				else if (rc != 0)
				{
					String erms = "Printer Error: Unable to print Deconfig Loc Chart.";
					showOkMB(parent, erms); //~11C
				}

				pd.closeConnections();
			}
		};

		/* Spawn thread */
		t.start();
	}

	/**
	 * @param mctl
	 * @param qty
	 * @param parent
	 */
	public static void downlevellist(final String mctl, final int qty,
										final MFSFrame parent)
	{
		/* Instantiate Worker Thread */
		Thread t = new Thread()
		{
			public void run()
			{
				int rc = 0;
				MFSPrintDriver pd = new MFSPrintDriver();
				pd.addValue("MCTL", mctl);
				rc = pd.print("DOWNLIST", qty);

				if (rc != 0)
				{
					if (rc == 666)
					{
						String erms = "There are no Downlevel Parts for this Work Unit!";
						showOkMB(parent, erms); //~11C
					}
					else
					{
						String erms = "Printer Error: Unable to print Downlevel Parts List";
						showOkMB(parent, erms); //~11C
					}
				}

				pd.closeConnections();
			}
		};

		/* Spawn thread */
		t.start();
	}

	/**
	 * @param pn
	 * @param ec
	 * @param mctl
	 * @param title
	 * @param qty
	 * @param parent
	 */
	public static void downtag(final String pn, final String ec, final String mctl,
								final String title, final int qty, final MFSFrame parent)
	{
		/* Instantiate Worker Thread */
		Thread t = new Thread()
		{
			public void run()
			{
				int rc = 0;
				MFSPrintDriver pd = new MFSPrintDriver();
				pd.addValue("PN", pn);
				pd.addValue("EC", ec);
				pd.addValue("MCTL", mctl);
				pd.addValue("TITLE", title);
				rc = pd.print("DOWNTAG", qty);

				if (rc != 0)
				{
					if (title.substring(0, 1).equals(" "))
					{
						String erms = "Printer Error: Unable to print Work Unit Tag.";
						showOkMB(parent, erms); //~11C
					}
					else
					{
						String erms = "Printer Error: Unable to print Downlevel Tag.";
						showOkMB(parent, erms); //~11C
					}
				}

				pd.closeConnections();
			}
		};

		/* Spawn thread */
		t.start();
	}

	/**
	 * @param ec
	 * @param size
	 * @param qty
	 * @param parent
	 */
	public static void ecnum(final String ec, final int size, final int qty,
								final MFSFrame parent)
	{
		/* Instantiate Worker Thread */
		Thread t = new Thread()
		{
			public void run()
			{
				int rc = 0;
				MFSPrintDriver pd = new MFSPrintDriver();
				pd.addValue("ECNO", ec);
				pd.addValue("VARECNO", Integer.toString(size));
				rc = pd.print("STNDALNECNO", qty);

				if (rc != 0)
				{
					String erms = "Printer Error: Unable to print EC Number Label.";
					showOkMB(parent, erms); //~11C
				}

				pd.closeConnections();
			}
		};

		/* Spawn thread */
		t.start();
	}

	/**
	 * @param part
	 * @param mctl
	 * @param qty
	 * @param parent
	 */
	public static void elevens(final String part, final String mctl, final int qty,
								final MFSFrame parent)
	{
		/* Instantiate Worker Thread */
		Thread t = new Thread()
		{
			public void run()
			{
				int rc = 0;
				MFSPrintDriver pd = new MFSPrintDriver();
				pd.addValue("CELLTYPE", "FRU     ");
				pd.addValue("FFBM", part);
				pd.addValue("MCTL", mctl);
				pd.addValue("WHO1", "J");
				rc = pd.print("11SBAR", qty);

				if (rc != 0)
				{
					String erms = "Printer Error: Unable to print 11S Label.";
					showOkMB(parent, erms); //~11C
				}

				pd.closeConnections();
			}
		};

		/* Spawn thread */
		t.start();
	}

	//~5
	/**
	 * @param part
	 * @param snum
	 * @param qty
	 * @param parent
	 */
	public static void elevensDwnBin(final String part, final String snum, final int qty,
										final MFSFrame parent)
	{
		/* Instantiate Worker Thread */
		Thread t = new Thread()
		{
			public void run()
			{
				int rc = 0;
				MFSPrintDriver pd = new MFSPrintDriver();
				pd.addValue("PART", part);
				pd.addValue("SNUM", snum);
				rc = pd.print("11SDWNBIN", qty);

				if (rc != 0)
				{
					String erms = "Printer Error: Unable to print 11S DownBin barcode Label.";
					showOkMB(parent, erms); //~11C
				}

				pd.closeConnections();
			}
		};

		/* Spawn thread */
		t.start();
	}

	/**
	 * @param prln
	 * @param who
	 * @param mctl
	 * @param qty
	 * @param parent
	 */
	public static void elevenswu(final String prln, final String who, final String mctl,
									final int qty, final MFSFrame parent)
	{
		/* Instantiate Worker Thread */
		Thread t = new Thread()
		{
			public void run()
			{
				int rc = 0;
				MFSPrintDriver pd = new MFSPrintDriver();
				pd.addValue("PRLN", prln);
				pd.addValue("WHO", who);
				pd.addValue("MCTL", mctl);
				rc = pd.print("11SWU", qty);

				if (rc != 0)
				{
					String erms = "Printer Error: Unable to print 11S/Work Unit Label.";
					showOkMB(parent, erms); //~11C
				}

				pd.closeConnections();
			}
		};

		/* Spawn thread */
		t.start();
	}

	/**
	 * @param partdesc
	 * @param partnum
	 * @param nmbr
	 * @param systype
	 * @param ctlnum
	 * @param ircode1
	 * @param ircode2
	 * @param ircode3
	 * @param cell
	 * @param date
	 * @param sysnum
	 * @param user
	 * @param time
	 * @param comment
	 * @param qty
	 * @param parent
	 */
	public static void failreport(final String partdesc, final String partnum,
									final String nmbr, final String systype,
									final String ctlnum, final String ircode1,
									final String ircode2, final String ircode3,
									final String cell, final String date,
									final String sysnum, final String user,
									final String time, final String comment,
									final int qty, final MFSFrame parent)
	{
		/* Instantiate Worker Thread */
		Thread t = new Thread()
		{
			public void run()
			{
				int rc = 0;
				MFSPrintDriver pd = new MFSPrintDriver();
				pd.addValue("PARTDESC", partdesc);
				pd.addValue("PRLN", partnum);
				pd.addValue("OPCODE", nmbr);
				pd.addValue("SYSTYPE", systype);
				pd.addValue("CTLNUM", ctlnum);
				pd.addValue("IRCODE1", ircode1);
				pd.addValue("IRCODE2", ircode2);
				pd.addValue("IRCODE3", ircode3);
				pd.addValue("CELL", cell);
				pd.addValue("DATE", date);
				pd.addValue("SYSNUM", sysnum);
				pd.addValue("USER", user);
				pd.addValue("TIME", time);
				pd.addValue("COMMENT", comment);

				rc = pd.print("FAILREPORT", qty);

				if (rc != 0)
				{
					String erms = "Printer Error: Unable to print Fail Report Sheet.";
					showOkMB(parent, erms); //~11C
				}

				pd.closeConnections();
			}
		};

		/* Spawn thread */
		t.start();
	}

	/**
	 * @param partdesc
	 * @param partnum
	 * @param snum
	 * @param ecno
	 * @param systype
	 * @param ctlnum
	 * @param ircode
	 * @param flags
	 * @param mmdl
	 * @param machtype
	 * @param cell
	 * @param date
	 * @param sysnum
	 * @param serial
	 * @param user
	 * @param time
	 * @param comment
	 * @param qty
	 * @param parent
	 */
	public static void failtag(final String partdesc, final String partnum,
								final String snum, final String ecno,
								final String systype, final String ctlnum,
								final String ircode, final String flags,
								final String mmdl, final String machtype,
								final String cell, final String date,
								final String sysnum, final String serial,
								final String user, final String time,
								final String comment, final int qty, final MFSFrame parent)
	{
		/* Instantiate Worker Thread */
		Thread t = new Thread()
		{
			public void run()
			{
				int rc = 0;
				MFSPrintDriver pd = new MFSPrintDriver();
				pd.addValue("PARTDESC", partdesc);
				pd.addValue("PARTNUM", partnum);
				pd.addValue("SNUM", snum);
				pd.addValue("ECNO", ecno);
				pd.addValue("SYSTYPE", systype);
				pd.addValue("CTLNUM", ctlnum);
				pd.addValue("IRCODE", ircode);
				pd.addValue("FLAGS", flags);
				pd.addValue("MMDL", mmdl);
				pd.addValue("MACHTYPE", machtype);
				pd.addValue("CELL", cell);
				pd.addValue("DATE", date);
				pd.addValue("SYSNUM", sysnum);
				pd.addValue("SERIAL", serial);
				pd.addValue("USER", user);
				pd.addValue("TIME", time);
				pd.addValue("COMMENT", comment);

				rc = pd.print("FAILTAG", qty);

				if (rc != 0)
				{
					String erms = "Printer Error: Unable to print Fail Tag.";
					showOkMB(parent, erms); //~11C
				}

				pd.closeConnections();
			}
		};

		/* Spawn thread */
		t.start();
	}

	/**
	 * @param prln
	 * @param who
	 * @param mctl
	 * @param qty
	 * @param parent
	 */
	public static void frunumb(final String prln, final String who, final String mctl,
								final int qty, final MFSFrame parent) 
	{
		/* Instantiate Worker Thread */
		Thread t = new Thread()
		{
			public void run()
			{
				int rc = 0;
				MFSPrintDriver pd = new MFSPrintDriver();
				pd.addValue("PRLN", prln);
				pd.addValue("WHO1", who);
				pd.addValue("MCTL", mctl);
				pd.addValue("USER", MFSConfig.getInstance().getConfigValue("USER"));
				rc = pd.print("FRUNUMB", qty);

				if (rc != 0)
				{
					String erms = "Printer Error: Unable to print Fru Number Label.";
					showOkMB(parent, erms); //~11C
				}

				pd.closeConnections();
			}
		};

		/* Spawn thread */
		t.start();
	}

	/**
	 * @param item
	 * @param who
	 * @param cooc
	 * @param country
	 * @param qty
	 * @param parent
	 */
	public static void frunumbnosn(final String item, final String who,
									final String cooc, final String country,final String cnametxthead,
									final int qty, final MFSFrame parent)                //~34C
	{
		/* Instantiate Worker Thread */
		Thread t = new Thread()
		{
			public void run()
			{
				int rc = 0;
				MFSPrintDriver pd = new MFSPrintDriver();
				pd.addValue("PARTNUMBER", item);
				pd.addValue("WHO1", who);
				pd.addValue("COOC", cooc);
				pd.addValue("CNAM", country);
				pd.addValue("PHRASE", cnametxthead);                                     //~34A				
				pd.addValue("USER", MFSConfig.getInstance().getConfigValue("USER"));
				rc = pd.print("FRUNUMBNOSN", qty);

				if (rc != 0)
				{
					String erms = "Printer Error: Unable to print Fru Number Label.";
					showOkMB(parent, erms); //~11C
				}

				pd.closeConnections();
			}
		};

		/* Spawn thread */
		t.start();
	}

	/**
	 * @param item
	 * @param who
	 * @param serial
	 * @param cooc
	 * @param country
	 * @param qty
	 * @param parent
	 */
	public static void frunumbsn(final String item, final String who,
									final String serial, final String cooc,
									final String country, final String cnametxthead, final int qty,
									final MFSFrame parent)                               //~34C
	{
		/* Instantiate Worker Thread */
		Thread t = new Thread()
		{
			public void run()
			{
				int rc = 0;
				MFSPrintDriver pd = new MFSPrintDriver();
				pd.addValue("PARTNUMBER", item);
				pd.addValue("WHO1", who);
				pd.addValue("SERIALNUMBER", serial);
				pd.addValue("COOC", cooc);
				pd.addValue("CNAM", country);
				pd.addValue("PHRASE", cnametxthead);                                     //~34A				
				pd.addValue("USER", MFSConfig.getInstance().getConfigValue("USER"));
				rc = pd.print("FRUNUMBSN", qty);

				if (rc != 0)
				{
					String erms = "Printer Error: Unable to print Fru Number Label.";
					showOkMB(parent, erms); //~11C
				}

				pd.closeConnections();
			}
		};

		/* Spawn thread */
		t.start();
	}

	/**
	 * @param mctl
	 * @param qty
	 * @param parent
	 */
	public static void ipreport(final String mctl, final int qty, final MFSFrame parent)
	{
		/* Instantiate Worker Thread */
		Thread t = new Thread()
		{
			public void run()
			{
				int rc = 0;
				MFSPrintDriver pd = new MFSPrintDriver();
				pd.addValue("MCTL", mctl);
				pd.addValue("ALLF", "N");
				rc = pd.print("IPREPORT", qty);

				if (rc != 0)
				{
					String erms = "Printer Error: Unable to print Work Unit Components Report.";
					showOkMB(parent, erms); //~11C
				}

				pd.closeConnections();
			}
		};

		/* Spawn thread */
		t.start();
	}

	/**
	 * @param mfgn
	 * @param idss
	 * @param mctl
	 * @param cntr
	 * @param qty
	 * @param parent
	 */
	public static void iprsheet(final String mfgn, final String idss, final String mctl,
								final String cntr, final int qty, final MFSFrame parent)
	{
		/* Instantiate Worker Thread */
		Thread t = new Thread()
		{
			public void run()
			{
				int rc = 0;
				MFSPrintDriver pd = new MFSPrintDriver();
				pd.addValue("MFGN", mfgn);  //$NON-NLS-1$
				pd.addValue("IDSS", idss);  //$NON-NLS-1$
				pd.addValue("MCTL", mctl);  //$NON-NLS-1$

				//~4 tweak this logic to ensure only last 6 chars are sent to
				// the print
				//server for this label - change here because we the calling
				// classes were all locked
				if (cntr.length() >= 6)
				{
					pd.addValue("CNTR", cntr.substring((cntr.length() - 6)));  //$NON-NLS-1$
				}
				else
				{
					pd.addValue("CNTR", cntr);  //$NON-NLS-1$
				}

				// ~21A get the Smart Serial, too, if this is ISS 
				IGSXMLTransaction rtv_seqn = new IGSXMLTransaction("RTV_SEQN");  //$NON-NLS-1$
				rtv_seqn.startDocument();
				rtv_seqn.addElement("MFGN", mfgn);  //$NON-NLS-1$
				rtv_seqn.addElement("IDSS", idss);  //$NON-NLS-1$
				rtv_seqn.addElement("MCTL", mctl);  //$NON-NLS-1$
				rtv_seqn.addElement("CHECK", "ISS");  //$NON-NLS-1$  //$NON-NLS-2$
				rtv_seqn.endDocument();
				rtv_seqn.run();
				if (rtv_seqn.getReturnCode() != 0)
				{
					String erms = "Printer Error: Unable to check/get Smart Serial, " + rtv_seqn.getErms();
					showOkMB(parent, erms); 
				}
				else
				{
					String isISS = rtv_seqn.getNextElement("CHKD");  //$NON-NLS-1$
					if (isISS == null)
					{
						String erms = "Printer Error: Unable to determine if ISS.";
						showOkMB(parent, erms); 
					}
					else
					{
						String smrts = "";  //$NON-NLS-1$  //~35
						if (isISS.equals("Y"))  //$NON-NLS-1$
						{
							smrts = rtv_seqn.getFirstElement("SEQN");  //$NON-NLS-1$ 
						}
						pd.addValue("SMRTS", smrts);  //$NON-NLS-1$  //~35
						rc = pd.print("RTVIPRSHT", qty);  //$NON-NLS-1$  //~21C
					}
				}  // end of ~21A

				if (rc != 0)
				{
					String erms = "Printer Error: Unable to print IPR Sheet.";
					showOkMB(parent, erms); //~11C
				}

				pd.closeConnections();
			}
		};

		/* Spawn thread */
		t.start();
	}

	/**
	 * @param mfgn
	 * @param idss
	 * @param matp
	 * @param mmdl
	 * @param mspi
	 * @param mcsn
	 * @param orno
	 * @param mctl
	 * @param sqty
	 * @param lqty
	 * @param ktpe
	 * @param parent
	 */
	public static void keycodes(final String mfgn, final String idss, final String matp,
								final String mmdl, final String mspi, final String mcsn,
								final String orno, final String mctl, final String sqty,
								final String lqty, String ktpe,       final MFSFrame parent) // ~20C
	{
		if(ktpe.equals("N")) // No KEYCODES checked previously // ~20A
		{
			final MFSConfig config = MFSConfig.getInstance();
			String value = config.getConfigValue("KEYCODES");

			if (!value.equals(MFSConfig.NOT_FOUND))
			{
				ktpe = "S";
			}
			else
			{
				ktpe = (ktpe.equals("S")) ? "B" : "L";
			}

			if(ktpe.equals("N"))
			{
				String erms = "Printer Error: Unable to print KEYCODES Label.";
				showOkMB(parent, erms);
				return;
			}
		}
		final String ktpe_aux = ktpe; // ~20A
		/* Instantiate Worker Thread */
		Thread t = new Thread()
		{
			public void run()
			{
				int rc = 0;
				MFSPrintDriver pd = new MFSPrintDriver();
				pd.addValue("MFGN", mfgn);
				pd.addValue("IDSS", idss);
				pd.addValue("MATP", matp);
				pd.addValue("MSPI", mspi);
				pd.addValue("MMDL", mmdl);
				pd.addValue("MCSN", mcsn);
				pd.addValue("ORNO", orno);
				pd.addValue("MCTL", mctl);
				pd.addValue("SQTY", sqty); // ~20A
				pd.addValue("LQTY", lqty); // ~20A
				pd.addValue("KTPE", ktpe_aux); // ~20A
				rc = pd.print("KEYCODES", 1); // ~20C

				if (rc != 0)
				{
					String erms = "Printer Error: Unable to print KEYCODES Label.";
					showOkMB(parent, erms); //~11C
				}

				pd.closeConnections();
			}
		};

		/* Spawn thread */
		t.start();
	}


	//~10A New method
	/**
	 * Prints a barcode label for a modifier/input-key combination.
	 * @param bcText the human readable key input
	 * @param bcData the barcode equivalent of the key input
	 * @param qty the number of labels to print
	 * @param parent the <code>MFSFrame</code> from which the label is printed 
	 */
	public static void keyInputBarcode(final String bcText, final String bcData,
										final int qty, final MFSFrame parent)
	{
		/* Instantiate Worker Thread */
		Thread t = new Thread()
		{
			public void run()
			{
				int rc = 0;
				MFSPrintDriver pd = new MFSPrintDriver();
				pd.addValue("BCTEXT", bcText); //$NON-NLS-1$
				pd.addValue("BCDATA", bcData); //$NON-NLS-1$
				rc = pd.print("BCKEYS", qty); //$NON-NLS-1$

				if (rc != 0)
				{
					StringBuffer erms = new StringBuffer();
					erms.append("Printer Error: Unable to print barcode for ");
					erms.append(bcText);
					erms.append(".");
					showOkMB(parent, erms.toString()); //~11C
				}

				pd.closeConnections();
			}
		};

		/* Spawn thread */
		t.start();
	}
	
	/**
	 * @param prln
	 * @param who
	 * @param mctl
	 * @param qty
	 * @param parent
	 */
	public static void l3cache(final String prln, final String who, final String mctl,
								final int qty, final MFSFrame parent)
	{
		/* Instantiate Worker Thread */
		Thread t = new Thread()
		{
			public void run()
			{
				int rc = 0;
				MFSPrintDriver pd = new MFSPrintDriver();
				pd.addValue("PRLN", prln);
				pd.addValue("WHO", who);
				pd.addValue("MCTL", mctl);
				rc = pd.print("L3CACHE", qty);

				if (rc != 0)
				{
					String erms = "Printer Error: Unable to print L3 Cache Label.";
					showOkMB(parent, erms); //~11C
				}

				pd.closeConnections();
			}
		};

		/* Spawn thread */
		t.start();
	}

	/**
	 * @param part
	 * @param qty
	 * @param parent
	 */
	public static void loadsource(final String part, final int qty, final MFSFrame parent)
	{
		/* Instantiate Worker Thread */
		Thread t = new Thread()
		{
			public void run()
			{
				int rc = 0;
				MFSPrintDriver pd = new MFSPrintDriver();
				pd.addValue("PARTNUM", part);
				rc = pd.print("LOADSOURCE", qty);

				if (rc != 0)
				{
					String erms = "Printer Error: Unable to print Load Source Label.";
					showOkMB(parent, erms); //~11C
				}

				pd.closeConnections();
			}
		};

		/* Spawn thread */
		t.start();
	}

	/**
	 * @param mfgn
	 * @param idss
	 * @param orno
	 * @param mctl
	 * @param qty
	 * @param parent
	 */
	public static void machIndexCard(final String mfgn, final String idss,
										final String orno, final String mctl,
										final int qty, final MFSFrame parent)
	{
		/* Instantiate Worker Thread */
		Thread t = new Thread()
		{
			public void run()
			{
				int rc = 0;
				MFSPrintDriver pd = new MFSPrintDriver();
				pd.addValue("MFGN", mfgn);
				pd.addValue("IDSS", idss);
				pd.addValue("ORNO", orno);
				pd.addValue("MCTL", mctl);
				rc = pd.print("MACHINDEXCARD", qty);

				if (rc != 0)
				{
					String erms = "Printer Error: Unable to print Machine Index Card Sheet.";
					showOkMB(parent, erms); //~11C
				}

				pd.closeConnections();
			}
		};

		/* Spawn thread */
		t.start();
	}

	/**
	 * @param mcsn
	 * @param qty
	 * @param parent
	 */
	public static void machserial(final String mcsn, final int qty, final MFSFrame parent)
	{
		/* Instantiate Worker Thread */
		Thread t = new Thread()
		{
			public void run()
			{
				int rc = 0;
				MFSPrintDriver pd = new MFSPrintDriver();
				pd.addValue("MCSN", mcsn);
				rc = pd.print("MACHSERIAL", qty);

				if (rc != 0)
				{
					String erms = "Printer Error: Unable to print Machine Serial Label.";
					showOkMB(parent, erms); //~11C
				}

				pd.closeConnections();
			}
		};

		/* Spawn thread */
		t.start();
	}

	/**
	 * @param mctl
	 * @param qty
	 * @param parent
	 */
	public static void machtype(final String mctl, final int qty, final MFSFrame parent)
	{
		/* Instantiate Worker Thread */
		Thread t = new Thread()
		{
			public void run()
			{
				int rc = 0;
				MFSPrintDriver pd = new MFSPrintDriver();
				pd.addValue("MCTL", mctl);
				rc = pd.print("MACHTYPE", qty);

				if (rc != 0)
				{
					String erms = "Printer Error: Unable to print Machine Type Label.";
					showOkMB(parent, erms); //~11C
				}

				pd.closeConnections();
			}
		};

		/* Spawn thread */
		t.start();
	}

	/**
	 * @param mfgn
	 * @param idss
	 * @param orno
	 * @param mctl
	 * @param qty
	 * @param parent
	 */
	public static void macid(final String mfgn, final String idss, final String orno,
								final String mctl, final int qty, final MFSFrame parent)
	{
		/* Instantiate Worker Thread */
		Thread t = new Thread()
		{
			public void run()
			{
				int rc = 0;
				MFSPrintDriver pd = new MFSPrintDriver();
				pd.addValue("MFGN", mfgn);
				pd.addValue("IDSS", idss);
				pd.addValue("ORNO", orno);
				pd.addValue("MCTL", mctl);
				rc = pd.print("MACID", qty);

				if (rc == 666)
				{
					// Do nothing if return code is 666 for no print required.
				}
				else if (rc != 0)
				{
					String erms = "Printer Error: Unable to print MAC ID Label.";
					showOkMB(parent, erms); //~11C
				}

				pd.closeConnections();
			}
		};

		/* Spawn thread */
		t.start();
	}

	/**
	 * @param mctl
	 * @param qty
	 * @param parent
	 */
	public static void mainid(final String mctl, final int qty, final MFSFrame parent)
	{
		/* Instantiate Worker Thread */
		Thread t = new Thread()
		{
			public void run()
			{
				int rc = 0;
				MFSPrintDriver pd = new MFSPrintDriver();
				pd.addValue("MCTL", mctl);
				rc = pd.print("MAINID", qty);

				if (rc != 0)
				{
					String erms = "Printer Error: Unable to print Main ID Label.";
					showOkMB(parent, erms); //~11C
				}

				pd.closeConnections();
			}
		};

		/* Spawn thread */
		t.start();
	}

	/**
	 * @param matp
	 * @param model
	 * @param snum
	 * @param qty
	 * @param parent
	 */
	public static void matpmcsn(final String matp, final String model, final String snum,
								final int qty, final MFSFrame parent)
	{
		/* Instantiate Worker Thread */
		Thread t = new Thread()
		{
			public void run()
			{
				int rc = 0;
				MFSPrintDriver pd = new MFSPrintDriver();
				pd.addValue("MATP", matp);
				pd.addValue("MODEL", model);
				pd.addValue("SNUM", snum);
				rc = pd.print("MATPMCSN", qty);

				if (rc != 0)
				{
					String erms = "Printer Error: Unable to print MATP/MMDL/MCSN Label.";
					showOkMB(parent, erms); //~11C
				}

				pd.closeConnections();
			}
		};

		/* Spawn thread */
		t.start();
	}

	/**
	 * @param matp
	 * @param model
	 * @param mspi
	 * @param snum
	 * @param qty
	 * @param parent
	 */
	public static void matpmcsn1s(final String matp, final String model,
									final String mspi, final String snum, final int qty,
									final MFSFrame parent)
	{
		/* Instantiate Worker Thread */
		Thread t = new Thread()
		{
			public void run()
			{
				int rc = 0;
				MFSPrintDriver pd = new MFSPrintDriver();
				pd.addValue("MATP", matp);
				pd.addValue("MODEL", model);
				pd.addValue("MSPI", mspi);
				pd.addValue("SNUM", snum);
				rc = pd.print("MATPMCSN1S", qty);

				if (rc != 0)
				{
					String erms = "Printer Error: Unable to print MATP/MMDL/MSPI/MCSN 1S Label.";
					showOkMB(parent, erms); //~11C
				}

				pd.closeConnections();
			}
		};

		/* Spawn thread */
		t.start();
	}
	
	//~12A New method
	/**
	 * @param matp
	 * @param model
	 * @param snum
	 * @param qty
	 * @param parent
	 */
	public static void matpmcsn2(final String matp, final String model,
									final String snum, final int qty,
									final MFSFrame parent)
	{
		/* Instantiate Worker Thread */
		Thread t = new Thread()
		{
			public void run()
			{
				int rc = 0;
				MFSPrintDriver pd = new MFSPrintDriver();
				pd.addValue("MATP", matp);
				pd.addValue("MODEL", model);
				pd.addValue("SNUM", snum);
				rc = pd.print("MATPMCSN2", qty);

				if (rc != 0)
				{
					String erms = "Printer Error: Unable to print MATP/MMDL/MCSN Label.";
					showOkMB(parent, erms);
				}

				pd.closeConnections();
			}
		};

		/* Spawn thread */
		t.start();
	}

	/**
	 * @param matp
	 * @param model
	 * @param snum
	 * @param qty
	 * @param parent
	 */
	public static void matpmcsnlnx(final String matp, final String model,
									final String snum, final int qty,
									final MFSFrame parent)
	{
		/* Instantiate Worker Thread */
		Thread t = new Thread()
		{
			public void run()
			{
				int rc = 0;
				MFSPrintDriver pd = new MFSPrintDriver();
				pd.addValue("MATP", matp);
				pd.addValue("MODEL", model);
				pd.addValue("SNUM", snum);
				rc = pd.print("MATPMCSNLNX", qty);

				if (rc != 0)
				{
					String erms = "Printer Error: Unable to print Linux MATP/MMDL/MCSN Label.";
					showOkMB(parent, erms); //~11C
				}

				pd.closeConnections();
			}
		};

		/* Spawn thread */
		t.start();
	}

	/**
	 * @param mfgn
	 * @param idss
	 * @param orno
	 * @param mctl
	 * @param qty
	 * @param parent
	 */
	public static void mesShipGrpLbl(final String mfgn, final String idss,
										final String orno, final String mctl,
										final int qty, final MFSFrame parent)
	{
		/* Instantiate Worker Thread */
		Thread t = new Thread()
		{
			public void run()
			{
				int rc = 0;
				MFSPrintDriver pd = new MFSPrintDriver();
				pd.addValue("MFGN", mfgn);
				pd.addValue("IDSS", idss);
				pd.addValue("ORNO", orno);
				pd.addValue("MCTL", mctl);
				rc = pd.print("MESSHIPGRPLBL", qty);

				if (rc != 0)
				{
					String erms = "Printer Error: Unable to print MES Ship Group Label.";
					showOkMB(parent, erms); //~11C
				}

				pd.closeConnections();
			}
		};

		/* Spawn thread */
		t.start();
	}

	/**
	 * @param cell
	 * @param part
	 * @param mctl
	 * @param qty
	 * @param parent
	 */
	public static void mir(final String cell, final String part, final String mctl,
							final int qty, final MFSFrame parent)
	{
		/* Instantiate Worker Thread */
		Thread t = new Thread()
		{
			public void run()
			{
				int rc = 0;
				MFSPrintDriver pd = new MFSPrintDriver();
				pd.addValue("CELLTYPE", cell);
				pd.addValue("FFBM", part);
				pd.addValue("MCTL", mctl);
				rc = pd.print("ROCHMIRR", qty);

				// -1 return code means that rtv_lbldta returned ********* to
				// print server code
				if (rc == 666)
				{
					//Do not display a message if rc == 666
				}
				else if (rc == 0)
				{
					String erms = "MIR label was Printed";				
					String cnfgEfficiencyValue = MFSConfig.getInstance().getConfigValue("EFFICIENCYON");//$NON-NLS-1$
					if (!cnfgEfficiencyValue.equals(MFSConfig.NOT_FOUND))		//~32C			
					{		
						showOkMBTimer(parent, erms, cnfgEfficiencyValue);       //~32C
					}
					else
					{
						showOkMB(parent, erms); //~11C
					}
				}
				else
				{
					String erms = "Printer Error: Unable to print MIR label";
					showOkMB(parent, erms); //~11C
				}
				pd.closeConnections();
			}
		};

		/* Spawn thread */
		t.start();
	}

	/**
	 * @param tagSequence
	 * @param qty
	 * @param parent
	 */
	public static void ncmbigtag(final String tagSequence, final int qty,
									final MFSFrame parent)
	{
		/* Instantiate Worker Thread */
		Thread t = new Thread()
		{
			public void run()
			{
				int rc = 0;
				MFSPrintDriver pd = new MFSPrintDriver();
				pd.addValue("REJS", tagSequence);
				rc = pd.print("NCMBIGTAG", qty);

				if (rc != 0)
				{
					String erms = "Printer Error: Unable to print NCM Big Tag.";
					showOkMB(parent, erms); //~11C
				}

				pd.closeConnections();
			}
		};

		/* Spawn thread */
		t.start();
	}

	/**
	 * @param tagSequence
	 * @param qty
	 * @param parent
	 */
	public static void ncmsheet(final String tagSequence, final int qty,
								final MFSFrame parent)
	{
		/* Instantiate Worker Thread */
		Thread t = new Thread()
		{
			public void run()
			{
				int rc = 0;
				MFSPrintDriver pd = new MFSPrintDriver();
				pd.addValue("REJS", tagSequence);
				rc = pd.print("NCMSHEET", qty);

				if (rc != 0)
				{
					String erms = "Printer Error: Unable to print NCM Sheet.";
					showOkMB(parent, erms); //~11C
				}

				pd.closeConnections();
			}
		};

		/* Spawn thread */
		t.start();
	}

	/**
	 * @param tagSequence
	 * @param qty
	 * @param parent
	 */
	public static void ncmtag(final String tagSequence, final int qty,
								final MFSFrame parent)
	{
		/* Instantiate Worker Thread */
		Thread t = new Thread()
		{
			public void run()
			{
				int rc = 0;
				MFSPrintDriver pd = new MFSPrintDriver();
				pd.addValue("REJS", tagSequence);
				rc = pd.print("NCMTAG", qty);

				if (rc != 0)
				{
					String erms = "Printer Error: Unable to print NCM Tag.";
					showOkMB(parent, erms); //~11C
				}

				pd.closeConnections();
			}
		};

		/* Spawn thread */
		t.start();
	}

	 // ~33A
	/**
	 * @param lblt: label type 
	 * @param lbts: label trigger source
	 * @param lbldta: label data
	 * @param plom: plant of manufacturing
	 * @param qty
	 * @param parent
	 */
	public static void onDemandLabel(final MFSOnDemandKeyData odKeyData, final MFSFrame parent)
	{
		onDemandLabel(odKeyData.getLabelType(),
					odKeyData.getTriggerSource(),
					odKeyData.getLabelData(),
					odKeyData.getPlom(),
					odKeyData.getQty(),
					parent);
	}	
	
	 // ~30A // ~33C
	/**
	 * @param lblt: label type 
	 * @param lbts: label trigger source
	 * @param lbldta: label data
	 * @param plom: plant of manufacturing
	 * @param qty
	 * @param parent
	 */
	public static void onDemandLabel(final String lblt, final String lbts, final String lbldta, final String plom, final int qty, final MFSFrame parent)
	{
		printlabel(lblt, lbts, "", lbldta, plom, qty, parent);	//~33C	
	}
	
	//~13C Change parameter to MFSTableModel
	/**
	 * @param transaction
	 * @param qd10Info
	 * @param atm
	 * @param qty
	 * @param parent
	 */
	@SuppressWarnings("rawtypes")
	public static void partInfo(final String transaction, final Hashtable qd10Info,
								final MFSTableModel atm, final int qty, final MFSFrame parent)
	{
		/* Instantiate Worker Thread */
		Thread t = new Thread()
		{
			public void run()
			{
				//~13C Removed copying of data for history transaction
				
				int rc = 0;
				MFSPrintDriver pd = new MFSPrintDriver();
				pd.addValue("QD10Data", qd10Info);
				pd.addValue("TableModel", atm);
				pd.addValue("Transaction", transaction);
				rc = pd.print("PARTINFOSHEET", qty);

				if (rc == 666)
				{
					//Do not display a message if rc == 666
				}
				else if (rc == 0)
				{
					if (transaction.equals("detail"))
					{
						String erms = "Part Detail Information was Printed";
						showOkMB(parent, erms); //~11C
					}
					else
					{
						String erms = "Part History Information was Printed";
						showOkMB(parent, erms); //~11C
					}
				}
				else
				{
					if (transaction.equals("detail"))
					{
						String erms = "Printer Error: Unable to print Part Detail Information";
						showOkMB(parent, erms); //~11C
					}
					else
					{
						String erms = "Printer Error: Unable to print Part History Information";
						showOkMB(parent, erms); //~11C
					}
				}

				pd.closeConnections();
			}
		};

		/* Spawn thread */
		t.start();
	}

	/**
	 * @param part
	 * @param qty
	 * @param parent
	 */
	public static void partnum(final String part, final int qty, final MFSFrame parent)
	{
		/* Instantiate Worker Thread */
		Thread t = new Thread()
		{
			public void run()
			{
				int rc = 0;
				MFSPrintDriver pd = new MFSPrintDriver();
				pd.addValue("PARTNUM", part);
				rc = pd.print("STNDALNPARTNUM", qty);

				if (rc != 0)
				{
					String erms = "Printer Error: Unable to print Part Number Label.";
					showOkMB(parent, erms); //~11C
				}

				pd.closeConnections();
			}
		};

		/* Spawn thread */
		t.start();
	}

	/**
	 * @param part
	 * @param snum
	 * @param qty
	 * @param parent
	 */
	public static void plugtag(final String part, final String snum, final int qty,
								final MFSFrame parent)
	{
		/* Instantiate Worker Thread */
		Thread t = new Thread()
		{
			public void run()
			{
				int rc = 0;
				MFSPrintDriver pd = new MFSPrintDriver();
				pd.addValue("PART", part);
				pd.addValue("SNUM", snum);
				rc = pd.print("PLUGTAG", qty);

				if (rc != 0)
				{
					String erms = "Printer Error: Unable to print Plug Tag.";
					showOkMB(parent, erms); //~11C
				}

				pd.closeConnections();
			}
		};

		/* Spawn thread */
		t.start();
	}

	/**
	 * @param part
	 * @param mspi
	 * @param snum
	 * @param qty
	 * @param parent
	 */
	public static void pnmcsn1s(final String part, final String mspi, final String snum,
								final int qty, final MFSFrame parent)
	{
		/* Instantiate Worker Thread */
		Thread t = new Thread()
		{
			public void run()
			{
				int rc = 0;
				MFSPrintDriver pd = new MFSPrintDriver();
				pd.addValue("PART", part);
				pd.addValue("MSPI", mspi);
				pd.addValue("SNUM", snum);
				rc = pd.print("PNMCSN1S", qty);

				if (rc != 0)
				{
					String erms = "Printer Error: Unable to print PART/MSPI/MCSN 1S Label.";
					showOkMB(parent, erms); //~11C
				}

				pd.closeConnections();
			}
		};

		/* Spawn thread */
		t.start();
	}

	/**
	 * @param mfgn
	 * @param idss
	 * @param qty
	 * @param parent
	 */
	public static void podkey(final String mfgn, final String idss, final int qty,
								final MFSFrame parent)
	{
		/* Instantiate Worker Thread */
		Thread t = new Thread()
		{
			public void run()
			{
				int rc = 0;
				MFSPrintDriver pd = new MFSPrintDriver();
				pd.addValue("MFGN", mfgn);
				pd.addValue("IDSS", idss);
				rc = pd.print("RTVPODKEY", qty);
				// 666 return code means that rtv_podkey returned ********* to
				// print server code
				if (rc == 666)
				{
					//Do not display a message if rc == 666
				}
				else if (rc != 0)
				{
					String erms = "Printer Error: Unable to print POD Key Sheet.";
					showOkMB(parent, erms); //~11C
				}

				pd.closeConnections();
			}
		};

		/* Spawn thread */
		t.start();
	}

	/**
	 * @param mctl
	 * @param nmbr
	 * @param type
	 * @param ctyp
	 * @param qty
	 * @param parent
	 */
	public static void pokkit(final String mctl, final String nmbr, final String type,
								final String ctyp, final int qty, final MFSFrame parent)
	{
		/* Instantiate Worker Thread */
		Thread t = new Thread()
		{
			public void run()
			{
				int rc = 0;

				if (type.equals("D") || type.equals("B"))
				{
					MFSPrintDriver pd = new MFSPrintDriver();
					pd.addValue("MCTL", mctl);
					pd.addValue("NMBR", nmbr);
					pd.addValue("CTYP", ctyp);
					pd.addValue("SUMMARY", "D");

					rc = pd.print("POKKIT", qty);
					if (rc != 0)
					{
						String erms = "Printer Error: Unable to print Detail Kitting Report.";
						showOkMB(parent, erms); //~11C
					}

					pd.closeConnections();
				}

				if ((rc == 0) && (type.equals("S") || type.equals("B")))
				{
					MFSPrintDriver pd = new MFSPrintDriver();
					pd.addValue("MCTL", mctl);
					pd.addValue("NMBR", nmbr);
					pd.addValue("CTYP", ctyp);
					pd.addValue("SUMMARY", "S");

					rc = pd.print("POKKIT", qty);
					if (rc != 0)
					{
						String erms = "Printer Error: Unable to print Summary Kitting Report.";
						showOkMB(parent, erms); //~11C
					}

					pd.closeConnections();
				}
			}
		};

		/* Spawn thread */
		t.start();
	}

	/**
	 * @param mctl
	 * @param nmbr
	 * @param type
	 * @param ctyp
	 * @param qty
	 * @param parent
	 */
	public static void pokkit2(final String mctl, final String nmbr, final String type,
								final String ctyp, final int qty, final MFSFrame parent)
	{
		/* Instantiate Worker Thread */
		Thread t = new Thread()
		{
			public void run()
			{
				int rc = 0;

				if (type.equals("D") || type.equals("B"))
				{
					MFSPrintDriver pd = new MFSPrintDriver();
					pd.addValue("MCTL", mctl);
					pd.addValue("NMBR", nmbr);
					pd.addValue("CTYP", ctyp);
					pd.addValue("SUMMARY", "D");

					rc = pd.print("POKKIT2", qty);
					if (rc != 0)
					{
						String erms = "Printer Error: Unable to print Detail MES Kitting Report.";
						showOkMB(parent, erms); //~11C
					}

					pd.closeConnections();
				}

				if ((rc == 0) && (type.equals("S") || type.equals("B")))
				{
					MFSPrintDriver pd = new MFSPrintDriver();
					pd.addValue("MCTL", mctl);
					pd.addValue("NMBR", nmbr);
					pd.addValue("CTYP", ctyp);
					pd.addValue("SUMMARY", "S");

					rc = pd.print("POKKIT2", qty);
					if (rc != 0)
					{
						String erms = "Printer Error: Unable to print Summary MES Kitting Report.";
						showOkMB(parent, erms); //~11C
					}

					pd.closeConnections();
				}
			}
		};

		/* Spawn thread */
		t.start();
	}

	/**
	 * @param mfgn
	 * @param idss
	 * @param orno
	 * @param mctl
	 * @param qty
	 * @param parent
	 */
	public static void prodFeatCode(final String mfgn, final String idss,
									final String orno, final String mctl, final int qty,
									final MFSFrame parent)
	{
		/* Instantiate Worker Thread */
		Thread t = new Thread()
		{
			public void run()
			{
				int rc = 0;
				MFSPrintDriver pd = new MFSPrintDriver();
				pd.addValue("MFGN", mfgn);
				pd.addValue("IDSS", idss);
				pd.addValue("ORNO", orno);
				pd.addValue("MCTL", mctl);
				rc = pd.print("PRODFEATCODE", qty);

				if (rc != 0)
				{
					String erms = "Printer Error: Unable to print Product Feature Code Sheet.";
					showOkMB(parent, erms); //~11C
				}

				pd.closeConnections();
			}
		};

		/* Spawn thread */
		t.start();
	}

	/**
	 * @param mfgn
	 * @param idss
	 * @param orno
	 * @param mctl
	 * @param qty
	 * @param parent
	 */
	public static void prodPackLrg(final String mfgn, final String idss,
									final String orno, final String mctl, final int qty,
									final MFSFrame parent)
	{
		/* Instantiate Worker Thread */
		Thread t = new Thread()
		{
			public void run()
			{
				int rc = 0;
				MFSPrintDriver pd = new MFSPrintDriver();
				pd.addValue("MFGN", mfgn);
				pd.addValue("IDSS", idss);
				pd.addValue("ORNO", orno);
				pd.addValue("MCTL", mctl);
				rc = pd.print("PRODPACKLRG", qty);

				if (rc != 0)
				{
					String erms = "Printer Error: Unable to print Large Product Package Label.";
					showOkMB(parent, erms); //~11C
				}

				pd.closeConnections();
			}
		};

		/* Spawn thread */
		t.start();
	}

	/**
	 * @param mfgn
	 * @param idss
	 * @param orno
	 * @param mctl
	 * @param qty
	 * @param parent
	 */
	public static void prodPackSm(final String mfgn, final String idss,
									final String orno, final String mctl, final int qty,
									final MFSFrame parent)
	{
		/* Instantiate Worker Thread */
		Thread t = new Thread()
		{
			public void run()
			{
				int rc = 0;
				MFSPrintDriver pd = new MFSPrintDriver();
				pd.addValue("MFGN", mfgn);
				pd.addValue("IDSS", idss);
				pd.addValue("ORNO", orno);
				pd.addValue("MCTL", mctl);
				rc = pd.print("PRODPACKSM", qty);

				if (rc != 0)
				{
					String erms = "Printer Error: Unable to print Small Product Package Label.";
					showOkMB(parent, erms); //~11C
				}

				pd.closeConnections();
			}
		};

		/* Spawn thread */
		t.start();
	}

	/**
	 * @param prln
	 * @param who
	 * @param mctl
	 * @param qty
	 * @param parent
	 */
	public static void prodsub(final String prln, final String who, final String mctl,
								final int qty, final MFSFrame parent)
	{
		/* Instantiate Worker Thread */
		Thread t = new Thread()
		{
			public void run()
			{
				int rc = 0;
				MFSPrintDriver pd = new MFSPrintDriver();
				pd.addValue("PRLN", prln);
				pd.addValue("WHO1", who);
				pd.addValue("MCTL", mctl);
				rc = pd.print("PRODSUB", qty);

				if (rc != 0)
				{
					String erms = "Printer Error: Unable to print Prod Sub Label.";
					showOkMB(parent, erms); //~11C
				}

				pd.closeConnections();
			}
		};

		/* Spawn thread */
		t.start();
	}

	/**
	 * @param orno
	 * @param quantity
	 * @param parent
	 */
	public static void qualityCertificate(final String orno, final int quantity,
											final MFSFrame parent)
	{
		/* Instantiate Worker Thread */
		Thread t = new Thread()
		{
			public void run()
			{
				String who = "J";
				int rc = 0;
				MFSPrintDriver pd = new MFSPrintDriver();
				pd.addValue("ORNO", orno);
				pd.addValue("WHO1", who);
				rc = pd.print("QUALITYCERTIFICATE", quantity);

				if (rc != 0)
				{
					String erms = "Printer Error: Unable to print Quality Certificate.";
					showOkMB(parent, erms); //~11C
				}

				pd.closeConnections();
			}
		};

		/* Spawn thread */
		t.start();
	}

	/**
	 * @param mctl
	 * @param type
	 * @param qty
	 * @param parent
	 */
	public static void rchfcmes(final String mctl, final String type, final int qty,
								final MFSFrame parent)
	{
		/* Instantiate Worker Thread */
		Thread t = new Thread()
		{
			public void run()
			{
				int rc = 0;
				if (type.equals("D") || type.equals("B"))
				{
					MFSPrintDriver pd = new MFSPrintDriver();

					pd.addValue("MCTL", mctl);
					pd.addValue("TYPE", "D");
					rc = pd.print("RCHFCMES", qty);

					if (rc != 0)
					{
						String erms = "Printer Error: Unable to print FC MES Packing List.";
						showOkMB(parent, erms); //~11C
					}
					pd.closeConnections();
				}
				if (type.equals("S") || type.equals("B"))
				{
					MFSPrintDriver pd = new MFSPrintDriver();

					pd.addValue("MCTL", mctl);
					pd.addValue("TYPE", "S");
					rc = pd.print("RCHFCMES", qty);

					if (rc != 0)
					{
						String erms = "Printer Error: Unable to print FC MES Packing List.";
						showOkMB(parent, erms); //~11C
					}
					pd.closeConnections();
				}
			}
		};

		/* Spawn thread */
		t.start();
	}

	/**
	 * @param mctl
	 * @param nmbr
	 * @param type
	 * @param qty
	 * @param parent
	 */
	public static void rchkit(final String mctl, final String nmbr, final String type,
								final int qty, final MFSFrame parent)
	{
		/* Instantiate Worker Thread */
		Thread t = new Thread()
		{
			public void run()
			{
				int rc = 0;

				if (type.equals("D") || type.equals("B"))
				{
					MFSPrintDriver pd = new MFSPrintDriver();
					pd.addValue("MCTL", mctl);
					pd.addValue("NMBR", nmbr);
					pd.addValue("TYPE", "D");

					rc = pd.print("RCHKIT", qty);
					if (rc == 666)
					{
						//Do not display a message if rc == 666
					}
					else if (rc != 0)
					{
						String erms = "Printer Error: Unable to print Detail Kitting Report.";
						showOkMB(parent, erms); //~11C
					}

					pd.closeConnections();
				}

				if ((rc == 0) && (type.equals("S") || type.equals("B")))
				{
					MFSPrintDriver pd = new MFSPrintDriver();
					pd.addValue("MCTL", mctl);
					pd.addValue("NMBR", nmbr);
					pd.addValue("TYPE", "S");

					rc = pd.print("RCHKIT", qty);
					if (rc == 666)
					{
						//Do not display a message if rc == 666
					}
					else if (rc != 0)
					{
						String erms = "Printer Error: Unable to print Summary Kitting Report.";
						showOkMB(parent, erms); //~11C
					}

					pd.closeConnections();
				}
			}
		};

		/* Spawn thread */
		t.start();
	}

	/**
	 * @param mctl
	 * @param type
	 * @param qty
	 * @param parent
	 */
	public static void rchshpgrp(final String mctl, final String type, final int qty,
									final MFSFrame parent)
	{
		/* Instantiate Worker Thread */
		Thread t = new Thread()
		{
			public void run()
			{
				int rc = 0;
				if ((type.equals("D")) || (type.equals("B")))
				{
					MFSPrintDriver pd = new MFSPrintDriver();
					pd.addValue("MCTL", mctl);
					pd.addValue("TYPE", "D");
					rc = pd.print("RCHSHPGRP", qty);

					if (rc != 0)
					{
						String erms = "Printer Error: Unable to print Ship Group List.";
						showOkMB(parent, erms); //~11C
					}

					pd.closeConnections();
				}

				if ((type.equals("S")) || (type.equals("B")))
				{
					MFSPrintDriver pd = new MFSPrintDriver();
					pd.addValue("MCTL", mctl);
					pd.addValue("TYPE", "S");
					rc = pd.print("RCHSHPGRP", qty);

					if (rc != 0)
					{
						String erms = "Printer Error: Unable to print Ship Group List.";
						showOkMB(parent, erms); //~11C
					}

					pd.closeConnections();
				}

			}
		};

		/* Spawn thread */
		t.start();
	}

	/**
	 * @param mctl
	 * @param nmbr
	 * @param qty
	 * @param parent
	 */
	public static void recvpart(final String mctl, final String nmbr, final int qty,
								final MFSFrame parent)
	{
		/* Instantiate Worker Thread */
		Thread t = new Thread()
		{
			public void run()
			{
				int rc = 0;
				MFSPrintDriver pd = new MFSPrintDriver();
				pd.addValue("MCTL", mctl);
				pd.addValue("NMBR", nmbr);
				rc = pd.print("RECVPART", qty);

				if (rc != 0)
				{
					String erms = "Printer Error: Unable to print Part Number Labels.";
					showOkMB(parent, erms); //~11C
				}

				pd.closeConnections();
			}
		};

		/* Spawn thread */
		t.start();
	}

	/**
	 * @param mctl
	 * @param nmbr
	 * @param pn
	 * @param pnQty
	 * @param qty
	 * @param parent
	 */
	public static void recvpart1(final String mctl, final String nmbr, final String pn,
									final String pnQty, final int qty,
									final MFSFrame parent)
	{
		/* Instantiate Worker Thread */
		Thread t = new Thread()
		{
			public void run()
			{
				int rc = 0;
				MFSPrintDriver pd = new MFSPrintDriver();
				pd.addValue("MCTL", mctl);
				pd.addValue("NMBR", nmbr);
				pd.addValue("PartNumber", pn);
				pd.addValue("Quantity", pnQty);
				rc = pd.print("RECVPART", qty);

				if (rc != 0)
				{
					String erms = "Printer Error: Unable to print Part Number Label.";
					showOkMB(parent, erms); //~11C
				}

				pd.closeConnections();
			}
		};

		/* Spawn thread */
		t.start();
	}

	/**
	 * @param cntr
	 * @param mctl
	 * @param qty
	 * @param parent
	 */
	public static void rochcontain(final String cntr, final String mctl, final int qty,
									final MFSFrame parent)
	{
		/* Instantiate Worker Thread */
		Thread t = new Thread()
		{
			public void run()
			{
				int rc = 0;
				MFSPrintDriver pd = new MFSPrintDriver();
				pd.addValue("CNTR", cntr);
				pd.addValue("MCTL", mctl);
				rc = pd.print("RTVSHPSHT", qty);

				if (rc != 0)
				{
					String erms = "Printer Error: Unable to print Container Label.";
					showOkMB(parent, erms); //~11C
				}

				pd.closeConnections();
			}
		};

		/* Spawn thread */
		t.start();
	}

	/**
	 * @param celltype
	 * @param pn
	 * @param mctl
	 * @param who
	 * @param qty
	 * @param parent
	 */
	public static void rochfru(final String celltype, final String pn, final String mctl,
								final String who, final int qty, final MFSFrame parent)
	{
		/* Instantiate Worker Thread */
		Thread t = new Thread()
		{
			public void run()
			{
				int rc = 0;
				MFSPrintDriver pd = new MFSPrintDriver();
				pd.addValue("CELLTYPE", celltype);
				pd.addValue("FFBM", pn);
				pd.addValue("MCTL", mctl);
				pd.addValue("WHO1", who);
				rc = pd.print("ROCHFRU", qty);

				if (rc != 0)
				{
					String erms = "Printer Error: Unable to print Rochester FRU Label.";
					showOkMB(parent, erms); //~11C
				}

				pd.closeConnections();
			}
		};

		/* Spawn thread */
		t.start();
	}

	/**
	 * @param item
	 * @param eclevel
	 * @param qty
	 * @param parent
	 */
	public static void rochmcp(final String item, final String eclevel, final int qty,
								final MFSFrame parent)
	{
		/* Instantiate Worker Thread */
		Thread t = new Thread()
		{
			public void run()
			{
				int rc = 0;
				MFSPrintDriver pd = new MFSPrintDriver();
				pd.addValue("PARTNUMBER", item);
				pd.addValue("ECLEVEL", eclevel);
				rc = pd.print("ROCHMCP", qty);

				if (rc != 0)
				{
					String erms = "Printer Error: Unable to print MCP Label.";
					showOkMB(parent, erms); //~11C
				}

				pd.closeConnections();
			}
		};

		/* Spawn thread */
		t.start();
	}

	/**
	 * @param mctl
	 * @param mfgn
	 * @param idss
	 * @param qty
	 * @param parent
	 */
	public static void rochwu(final String mctl, final String mfgn, final String idss,
								final int qty, final MFSFrame parent)
	{
		/* Instantiate Worker Thread */
		Thread t = new Thread()
		{
			public void run()
			{
				int rc = 0;
				MFSPrintDriver pd = new MFSPrintDriver();
				pd.addValue("MCTL", mctl);
				pd.addValue("MFGN", mfgn);
				pd.addValue("IDSS", idss);
				rc = pd.print("ROCHWU", qty);

				if (rc != 0)
				{
					String erms = "Printer Error: Unable to print Work Unit Label.";
					showOkMB(parent, erms); //~11C
				}

				pd.closeConnections();
			}
		};

		/* Spawn thread */
		t.start();
	}

	/**
	 * @param mctl
	 * @param qty
	 * @param parent
	 */
	public static void rtvfab(final String mctl, final int qty, final MFSFrame parent)
	{
		/* Instantiate Worker Thread */
		Thread t = new Thread()
		{
			public void run()
			{
				int rc = 0;
				MFSPrintDriver pd = new MFSPrintDriver();
				pd.addValue("MCTL", mctl);
				rc = pd.print("RTVFAB", qty);

				if (rc != 0)
				{
					String erms = "Printer Error: Unable to print Packing List.";
					showOkMB(parent, erms); //~11C
				}

				pd.closeConnections();
			}
		};

		/* Spawn thread */
		t.start();
	}

	/**
	 * @param mctl
	 * @param type
	 * @param qty
	 * @param parent
	 */
	public static void rtvfcmes(final String mctl, final String type, final int qty,
								final MFSFrame parent)
	{
		/* Instantiate Worker Thread */
		Thread t = new Thread()
		{
			public void run()
			{
				if (type.equals("D") || type.equals("B"))
				{
					int rc = 0;
					MFSPrintDriver pd = new MFSPrintDriver();

					pd.addValue("MCTL", mctl);
					pd.addValue("TYPE", "D");
					rc = pd.print("RTVFCMES", qty);

					if (rc != 0)
					{
						String erms = "Printer Error: Unable to print FC MES Packing List.";
						showOkMB(parent, erms); //~11C
					}
					pd.closeConnections();
				}
				if (type.equals("S") || type.equals("B"))
				{
					int rc = 0;
					MFSPrintDriver pd = new MFSPrintDriver();

					pd.addValue("MCTL", mctl);
					pd.addValue("TYPE", "S");
					rc = pd.print("RTVFCMES", qty);

					if (rc != 0)
					{
						String erms = "Printer Error: Unable to print FC MES Packing List.";
						showOkMB(parent, erms); //~11C
					}
					pd.closeConnections();
				}
			}
		};

		/* Spawn thread */
		t.start();
	}

	/**
	 * @param mctl
	 * @param qty
	 * @param parent
	 */
	public static void rtvffbm(final String mctl, final int qty, final MFSFrame parent)
	{
		/* Instantiate Worker Thread */
		Thread t = new Thread()
		{
			public void run()
			{
				int rc = 0;
				MFSPrintDriver pd = new MFSPrintDriver();
				pd.addValue("MCTL", mctl);
				pd.addValue("WHO1", "J");
				rc = pd.print("RTVFFBM1", qty);

				if (rc != 0)
				{
					String erms = "Printer Error: Unable to print FFBM Label.";
					showOkMB(parent, erms); //~11C
				}

				pd.closeConnections();
			}
		};

		/* Spawn thread */
		t.start();
	}

	/**
	 * @param mctl
	 * @param qty
	 * @param parent
	 */
	public static void rtvffbm2(final String mctl, final int qty, final MFSFrame parent)
	{
		/* Instantiate Worker Thread */
		Thread t = new Thread()
		{
			public void run()
			{
				int rc = 0;
				MFSPrintDriver pd = new MFSPrintDriver();
				pd.addValue("MCTL", mctl);
				pd.addValue("WHO1", "J");
				rc = pd.print("RTVFFBM2", qty);

				if (rc != 0)
				{
					String erms = "Printer Error: Unable to print FFBM Label.";
					showOkMB(parent, erms); //~11C
				}

				pd.closeConnections();
			}
		};

		/* Spawn thread */
		t.start();
	}

	/**
	 * @param mctl
	 * @param qty
	 * @param parent
	 */
	public static void rtvscsi(final String mctl, final int qty, final MFSFrame parent)
	{
		/* Instantiate Worker Thread */
		Thread t = new Thread()
		{
			public void run()
			{
				int rc = 0;
				MFSPrintDriver pd = new MFSPrintDriver();
				pd.addValue("MCTL", mctl);
				rc = pd.print("RTVSCSI", qty);

				if (rc != 0)
				{
					String erms = "Printer Error: Unable to print SCSI Label.";
					showOkMB(parent, erms); //~11C
				}

				pd.closeConnections();
			}
		};

		/* Spawn thread */
		t.start();
	}

	/**
	 * @param mctl
	 * @param type
	 * @param qty
	 * @param parent
	 */
	public static void rtvshpgrp(final String mctl, final String type, final int qty,
									final MFSFrame parent)
	{
		/* Instantiate Worker Thread */
		Thread t = new Thread()
		{
			public void run()
			{
				if (type.equals("D") || type.equals("B"))
				{
					int rc = 0;
					MFSPrintDriver pd = new MFSPrintDriver();

					pd.addValue("MCTL", mctl);
					pd.addValue("TYPE", "D");
					rc = pd.print("RTVSHPGRP", qty);

					if (rc != 0)
					{
						String erms = "Printer Error: Unable to print Ship Group List.";
						showOkMB(parent, erms); //~11C
					}
					pd.closeConnections();
				}
				if (type.equals("S") || type.equals("B"))
				{
					int rc = 0;
					MFSPrintDriver pd = new MFSPrintDriver();

					pd.addValue("MCTL", mctl);
					pd.addValue("TYPE", "S");
					rc = pd.print("RTVSHPGRP", qty);

					if (rc != 0)
					{
						String erms = "Printer Error: Unable to print Ship Group List.";
						showOkMB(parent, erms); //~11C
					}
					pd.closeConnections();
				}
			}
		};

		/* Spawn thread */
		t.start();
	}

	/**
	 * @param mctl
	 * @param qty
	 * @param parent
	 */
	public static void rtvtravel(final String mctl, final int qty, final MFSFrame parent)
	{
		/* Instantiate Worker Thread */
		Thread t = new Thread()
		{
			public void run()
			{
				int rc = 0;
				MFSPrintDriver pd = new MFSPrintDriver();
				pd.addValue("MCTL", mctl);
				rc = pd.print("RTVTRAVEL", qty);

				if (rc != 0)
				{
					String erms = "Printer Error: Unable to print Travel Report.";
					showOkMB(parent, erms); //~11C
				}

				pd.closeConnections();
			}
		};

		/* Spawn thread */
		t.start();
	}

	/**
	 * @param serial_a
	 * @param serial_b
	 * @param qty
	 * @param parent
	 */
	public static void serialnumdouble(final String serial_a, final String serial_b,
										final int qty, final MFSFrame parent)
	{
		/* Instantiate Worker Thread */
		Thread t = new Thread()
		{
			public void run()
			{
				int rc = 0;
				MFSPrintDriver pd = new MFSPrintDriver();
				pd.addValue("SNUMA", serial_a);
				pd.addValue("SNUMB", serial_b);
				rc = pd.print("STNDALNDBLSNUM", qty);

				if (rc != 0)
				{
					String erms = "Printer Error: Unable to print Serial Number Label.";
					showOkMB(parent, erms); //~11C
				}

				pd.closeConnections();
			}
		};

		/* Spawn thread */
		t.start();
	}

	/**
	 * @param serial
	 * @param qty
	 * @param parent
	 */
	public static void serialnumsingle(final String serial, final int qty,
										final MFSFrame parent)
	{
		/* Instantiate Worker Thread */
		Thread t = new Thread()
		{
			public void run()
			{
				int rc = 0;
				MFSPrintDriver pd = new MFSPrintDriver();
				pd.addValue("SNUMA", serial);
				rc = pd.print("STNDALNSGLSNUM", qty);

				if (rc != 0)
				{
					String erms = "Printer Error: Unable to print Serial Number Label.";
					showOkMB(parent, erms); //~11C
				}

				pd.closeConnections();
			}
		};

		/* Spawn thread */
		t.start();
	}

	/**
	 * @param cntr
	 * @param mctl
	 * @param qty
	 * @param parent
	 */
	public static void shipgrp(final String cntr, final String mctl, final int qty,
								final MFSFrame parent)
	{
		/* Instantiate Worker Thread */
		Thread t = new Thread()
		{
			public void run()
			{
				int rc = 0;
				MFSPrintDriver pd = new MFSPrintDriver();
				pd.addValue("CNTR", cntr);
				pd.addValue("MCTL", mctl);
				rc = pd.print("SHIPGRP", qty);

				if (rc != 0)
				{
					String erms = "Printer Error: Unable to print Container Label.";
					showOkMB(parent, erms); //~11C
				}

				pd.closeConnections();
			}
		};

		/* Spawn thread */
		t.start();
	}

	/**
	 * @param mctl
	 * @param qty
	 * @param parent
	 */
	public static void siemensl(final String mctl, final int qty, final MFSFrame parent)
	{
		/* Instantiate Worker Thread */
		Thread t = new Thread()
		{
			public void run()
			{
				int rc = 0;
				MFSPrintDriver pd = new MFSPrintDriver();
				pd.addValue("MCTL", mctl);
				rc = pd.print("SIEMENSL", qty);

				if (rc == 666)
				{
					//Do not display a message if rc == 666
				}
				else if (rc != 0)
				{
					String erms = "Printer Error: Unable to print Large Draeger Label.";
					showOkMB(parent, erms); //~11C
				}

				pd.closeConnections();
			}
		};

		/* Spawn thread */
		t.start();
	}

	/**
	 * @param mctl
	 * @param qty
	 * @param parent
	 */
	public static void siemenss(final String mctl, final int qty, final MFSFrame parent)
	{
		/* Instantiate Worker Thread */
		Thread t = new Thread()
		{
			public void run()
			{
				int rc = 0;
				MFSPrintDriver pd = new MFSPrintDriver();
				pd.addValue("MCTL", mctl);
				rc = pd.print("SIEMENSS", qty);

				if (rc == 666)
				{
					//Do not display a message if rc == 666
				}
				else if (rc != 0)
				{
					String erms = "Printer Error: Unable to print Small Draeger Label.";
					showOkMB(parent, erms); //~11C
				}

				pd.closeConnections();
			}
		};

		/* Spawn thread */
		t.start();
	}

	/**
	 * @param mctl
	 * @param qty
	 * @param parent
	 */
	public static void smallwu(final String mctl, final int qty, final MFSFrame parent)
	{
		/* Instantiate Worker Thread */
		Thread t = new Thread()
		{
			public void run()
			{
				int rc = 0;
				MFSPrintDriver pd = new MFSPrintDriver();
				pd.addValue("MCTL", mctl);
				rc = pd.print("SMALLWU", qty);

				if (rc != 0)
				{
					String erms = "Printer Error: Unable to print Small Work Unit Label.";
					showOkMB(parent, erms); //~11C
				}

				pd.closeConnections();
			}
		};

		/* Spawn thread */
		t.start();
	}

	//~21A
	/**
	 * Prints a new Smart Serial Label for the given number.
	 * @param smartSer
	 * @param qty
	 * @param parent
	 */
	public static void smartSerial(final String smartSer, final int qty, final MFSFrame parent)
	{
		/* Instantiate Worker Thread */
		Thread t = new Thread()
		{
			public void run()
			{
				int rc = 0;
				MFSPrintDriver pd = new MFSPrintDriver();
				pd.addValue("SMRTS", smartSer); //$NON-NLS-1$
				rc = pd.print("SMRTSERIAL", qty); //$NON-NLS-1$

				if (rc != 0)
				{
					String erms = "Printer Error: Unable to print Smart Serial Label.";
					showOkMB(parent, erms); 
				}

				pd.closeConnections();
			}
		};

		/* Spawn thread */
		t.start();
	}
	

	/**
	 * @param mfgn
	 * @param idss
	 * @param mctl
	 * @param qty
	 * @param parent
	 */
	public static void softwarekey(final String mfgn, final String idss,
									final String mctl, final int qty,
									final MFSFrame parent)
	{
		/* Instantiate Worker Thread */
		Thread t = new Thread()
		{
			public void run()
			{
				int rc = 0;
				MFSPrintDriver pd = new MFSPrintDriver();
				pd.addValue("MFGN", mfgn);
				pd.addValue("IDSS", idss);
				pd.addValue("MCTL", mctl);
				rc = pd.print("RTVSFTKEY", qty);

				if (rc == 666)
				{
					//Do not display a message if rc == 666
				}
				else if (rc != 0)
				{
					String erms = "Printer Error: Unable to print Software License Key Sheet.";
					showOkMB(parent, erms); //~11C
				}

				pd.closeConnections();
			}
		};

		/* Spawn thread */
		t.start();
	}

	/**
	 * @param prln
	 * @param mctl
	 * @param qty
	 * @param parent
	 */
	public static void spd11s(final String prln, final String mctl, final int qty,
								final MFSFrame parent)
	{
		/* Instantiate Worker Thread */
		Thread t = new Thread()
		{
			public void run()
			{
				int rc = 0;
				MFSPrintDriver pd = new MFSPrintDriver();
				pd.addValue("PRLN", prln);
				pd.addValue("MCTL", mctl);
				rc = pd.print("SPD11S", qty);

				if (rc != 0)
				{
					String erms = "Printer Error: Unable to print SPD11S Label.";
					showOkMB(parent, erms); //~11C
				}

				pd.closeConnections();
			}
		};

		/* Spawn thread */
		t.start();
	}

	/**
	 * @param mctl
	 * @param qty
	 * @param parent
	 */
	public static void spd11stext(final String mctl, final int qty, final MFSFrame parent)
	{
		/* Instantiate Worker Thread */
		Thread t = new Thread()
		{
			public void run()
			{
				int rc = 0;
				MFSPrintDriver pd = new MFSPrintDriver();
				pd.addValue("MCTL", mctl);
				rc = pd.print("SPD11STEXT", qty);

				if (rc != 0)
				{
					String erms = "Printer Error: Unable to print SPD11STEXT Label.";
					showOkMB(parent, erms); //~11C
				}

				pd.closeConnections();
			}
		};

		/* Spawn thread */
		t.start();
	}

	/**
	 * @param elevens
	 * @param size
	 * @param qty
	 * @param parent
	 */
	public static void stndalnelevens(final String elevens, final int size,
										final int qty, final MFSFrame parent)
	{
		/* Instantiate Worker Thread */
		Thread t = new Thread()
		{
			public void run()
			{
				int rc = 0;
				MFSPrintDriver pd = new MFSPrintDriver();
				pd.addValue("DATA", elevens);
				pd.addValue("VARSIZE", Integer.toString(size));
				rc = pd.print("STNDALN11S", qty);

				if (rc != 0)
				{
					String erms = "Printer Error: Unable to print Part/Serial Label.";
					showOkMB(parent, erms); //~11C
				}

				pd.closeConnections();
			}
		};

		/* Spawn thread */
		t.start();
	}
	
	//~15A New method
	/**
	 * @param mctl
	 * @param qty
	 * @param parent
	 */
	public static void syscertlbls(final String mctl, final int qty, final MFSFrame parent)
	{
		/* Instantiate Worker Thread */
		Thread t = new Thread()
		{
			public void run()
			{
				int rc = 0;
				MFSPrintDriver pd = new MFSPrintDriver();
				pd.addValue("MCTL", mctl);
				
				rc = pd.print("SYSCERTLBL", qty);

				if (rc != 0)
				{
					String erms = "Printer Error: Work Unit System Agency Certification Label.";
					showOkMB(parent, erms);
				}

				pd.closeConnections();
			}
		};

		/* Spawn thread */
		t.start();
	}

	/**
	 * @param mctl
	 * @param qty
	 * @param parent
	 */
	public static void sysinfo(final String mctl, final int qty, final MFSFrame parent)
	{
		/* Instantiate Worker Thread */
		Thread t = new Thread()
		{
			public void run()
			{
				int rc = 0;
				MFSPrintDriver pd = new MFSPrintDriver();
				pd.addValue("MCTL", mctl);
				rc = pd.print("SYSINFO", qty);

				if (rc != 0)
				{
					String erms = "Printer Error: Unable to print System Info Label.";
					showOkMB(parent, erms); //~11C
				}

				pd.closeConnections();
			}
		};

		/* Spawn thread */
		t.start();
	}

	/**
	 * @param idss
	 * @param mfgn
	 * @param mctl
	 * @param matp
	 * @param mspi
	 * @param mcsn
	 * @param qty
	 * @param parent
	 */
	public static void sysmctl(final String idss, final String mfgn, final String mctl,
								final String matp, final String mspi, final String mcsn,
								final int qty, final MFSFrame parent)
	{
		/* Instantiate Worker Thread */
		Thread t = new Thread()
		{
			public void run()
			{
				int rc = 0;
				MFSPrintDriver pd = new MFSPrintDriver();
				pd.addValue("IDSS", idss);
				pd.addValue("MFGN", mfgn);
				pd.addValue("MCTL", mctl);
				pd.addValue("MATP", matp);
				pd.addValue("MSPI", mspi);
				pd.addValue("MCSN", mcsn);
				rc = pd.print("SYSMCTL", qty);

				if (rc != 0)
				{
					String erms = "Printer Error: Unable to print System MCTL Label.";
					showOkMB(parent, erms); //~11C
				}

				pd.closeConnections();
			}
		};

		/* Spawn thread */
		t.start();
	}

	/**
	 * @param mctl
	 * @param qty
	 * @param parent
	 */
	public static void sysnumb(final String mctl, final int qty, final MFSFrame parent)
	{
		/* Instantiate Worker Thread */
		Thread t = new Thread()
		{
			public void run()
			{
				int rc = 0;
				MFSPrintDriver pd = new MFSPrintDriver();
				pd.addValue("MCTL", mctl);
				rc = pd.print("SYSNUMB", qty);

				if (rc != 0)
				{
					String erms = "Printer Error: Unable to print System Number Label.";
					showOkMB(parent, erms); //~11C
				}

				pd.closeConnections();
			}
		};

		/* Spawn thread */
		t.start();
	}

	// not used anymore - print server will determine what needs to be printed
	// by making one call to systempassword
	/**
	 * @param mfgn
	 * @param idss
	 * @param qty
	 * @param parent
	 */
	public static void systemid(final String mfgn, final String idss, final int qty,
								final MFSFrame parent)
	{
		/* Instantiate Worker Thread */
		Thread t = new Thread()
		{
			public void run()
			{
				int rc = 0;
				MFSPrintDriver pd = new MFSPrintDriver();
				pd.addValue("MFGN", mfgn);
				pd.addValue("IDSS", idss);
				rc = pd.print("RTVSYSID", qty);

				if (rc != 0)
				{
					String erms = "Printer Error: Unable to print System ID Sheet.";
					showOkMB(parent, erms); //~11C
				}

				pd.closeConnections();
			}
		};

		/* Spawn thread */
		t.start();
	}

	/**
	 * @param mfgn
	 * @param idss
	 * @param mctl
	 * @param cmat
	 * @param mmdl
	 * @param qty
	 * @param parent
	 */
	public static void systempassword(final String mfgn, final String idss,
										final String mctl, final String cmat,
										final String mmdl, final int qty,
										final MFSFrame parent)
	{
		/* Instantiate Worker Thread */
		Thread t = new Thread()
		{
			public void run()
			{
				int rc = 0;
				MFSPrintDriver pd = new MFSPrintDriver();
				pd.addValue("MFGN", mfgn);
				pd.addValue("IDSS", idss);
				pd.addValue("CMAT", cmat);
				pd.addValue("CMDL", mmdl);
				pd.addValue("MCTL", mctl);
				rc = pd.print("RTVSYSPWD", qty);

				if (rc != 0)
				{
					//~9C Changed error message
					String erms = "Printer Error: Unable to print System Unique ID Sheet";
					showOkMB(parent, erms); //~11C
				}

				pd.closeConnections();
			}
		};

		/* Spawn thread */
		t.start();
	}

	/**
	 * @param mctl
	 * @param mfgn
	 * @param idss
	 * @param orno
	 * @param matp
	 * @param mmdl
	 * @param mspi
	 * @param mcsn
	 * @param qty
	 * @param parent
	 */
	public static void tcodBilling(final String mctl, final String mfgn,
									final String idss, final String orno,
									final String matp, final String mmdl,
									final String mspi, final String mcsn, final int qty,
									final MFSFrame parent)
	{
		/* Instantiate Worker Thread */
		Thread t = new Thread()
		{
			public void run()
			{
				int rc = 0;
				MFSPrintDriver pd = new MFSPrintDriver();
				pd.addValue("MCTL", mctl);
				pd.addValue("MFGN", mfgn);
				pd.addValue("IDSS", idss);
				pd.addValue("ORNO", orno);
				pd.addValue("MATP", matp);
				pd.addValue("MMDL", mmdl);
				pd.addValue("MSPI", mspi);
				pd.addValue("MCSN", mcsn);
				pd.addValue("TYPE","BILL");					/*~19A*/
				rc = pd.print("TCODBILLING", qty);

				if (rc == 666)
				{
					//Do not display a message if rc == 666
				}
				else if (rc != 0)
				{
					String erms = "Printer Error: Unable to print Temporary Capacity On Demand Billing Sheet.";
					showOkMB(parent, erms); //~11C
				}

				pd.closeConnections();
			}
		};

		/* Spawn thread */
		t.start();
	}
	
	//~19A New method
	/**
	 * @param mctl
	 * @param mfgn
	 * @param idss
	 * @param orno
	 * @param matp
	 * @param mmdl
	 * @param mspi
	 * @param mcsn
	 * @param qty
	 * @param parent
	 */
	public static void tcodMinutes(final String mctl, final String mfgn,
									final String idss, final String orno,
									final String matp, final String mmdl,
									final String mspi, final String mcsn, final int qty,
									final MFSFrame parent)
	{
		/* Instantiate Worker Thread */
		Thread t = new Thread()
		{
			public void run()
			{
				int rc = 0;
				MFSPrintDriver pd = new MFSPrintDriver();
				pd.addValue("MCTL", mctl);
				pd.addValue("MFGN", mfgn);
				pd.addValue("IDSS", idss);
				pd.addValue("ORNO", orno);
				pd.addValue("MATP", matp);
				pd.addValue("MMDL", mmdl);
				pd.addValue("MSPI", mspi);
				pd.addValue("MCSN", mcsn);
				pd.addValue("TYPE", "UPAY");
				rc = pd.print("TCODMINUTES", qty);

				if (rc == 666)
				{
					//Do not display a message if rc == 666
				}
				else if (rc != 0)
				{
					String erms = "Printer Error: Unable to print Utility On Demand Billing Sheet.";
					showOkMB(parent, erms);
				}

				pd.closeConnections();
			}
		};

		/* Spawn thread */
		t.start();
	}	


	/**
	 * @param mctl
	 * @param mfgn
	 * @param idss
	 * @param orno
	 * @param matp
	 * @param mmdl
	 * @param mspi
	 * @param mcsn
	 * @param qty
	 * @param parent
	 */
	public static void tcodPrepay(final String mctl, final String mfgn,
									final String idss, final String orno,
									final String matp, final String mmdl,
									final String mspi, final String mcsn, final int qty,
									final MFSFrame parent)
	{
		/* Instantiate Worker Thread */
		Thread t = new Thread()
		{
			public void run()
			{
				int rc = 0;
				MFSPrintDriver pd = new MFSPrintDriver();
				pd.addValue("MCTL", mctl);
				pd.addValue("MFGN", mfgn);
				pd.addValue("IDSS", idss);
				pd.addValue("ORNO", orno);
				pd.addValue("MATP", matp);
				pd.addValue("MMDL", mmdl);
				pd.addValue("MSPI", mspi);
				pd.addValue("MCSN", mcsn);
				rc = pd.print("TCODPREPAY", qty);

				if (rc == 666)
				{
					//Do not display a message if rc == 666
				}
				else if (rc != 0)
				{
					String erms = "Printer Error: Unable to print Temporary Capacity On Demand Pre-Pay Sheet.";
					showOkMB(parent, erms); //~11C
				}

				pd.closeConnections();
			}
		};

		/* Spawn thread */
		t.start();
	}

	/**
	 * @param prln
	 * @param who
	 * @param mctl
	 * @param qty
	 * @param parent
	 */
	public static void tracksub(final String prln, final String who, final String mctl,
								final int qty, final MFSFrame parent)
	{
		/* Instantiate Worker Thread */
		Thread t = new Thread()
		{
			public void run()
			{
				int rc = 0;
				MFSPrintDriver pd = new MFSPrintDriver();
				pd.addValue("PRLN", prln);
				pd.addValue("WHO1", who);
				pd.addValue("MCTL", mctl);
				rc = pd.print("TRACKSUB", qty);

				if (rc != 0)
				{
					String erms = "Printer Error: Unable to print Track Sub Label.";
					showOkMB(parent, erms); //~11C
				}

				pd.closeConnections();
			}
		};

		/* Spawn thread */
		t.start();
	}

	/**
	 * @param prln
	 * @param who
	 * @param mctl
	 * @param qty
	 * @param parent
	 */
	public static void tracksub2(final String prln, final String who, final String mctl,
									final int qty, final MFSFrame parent)
	{
		/* Instantiate Worker Thread */
		Thread t = new Thread()
		{
			public void run()
			{
				int rc = 0;
				MFSPrintDriver pd = new MFSPrintDriver();
				pd.addValue("PRLN", prln);
				pd.addValue("WHO1", who);
				pd.addValue("MCTL", mctl);
				rc = pd.print("TRACKSUB2", qty);

				if (rc != 0)
				{
					String erms = "Printer Error: Unable to print Track Sub Label.";
					showOkMB(parent, erms); //~11C
				}

				pd.closeConnections();
			}
		};

		/* Spawn thread */
		t.start();
	}

	/**
	 * @param mctl
	 * @param crct
	 * @param inpn
	 * @param cooc
	 * @param qty
	 * @param parent
	 */
	public static void tracksub3(final String mctl, final String crct, final String inpn,
									final String cooc, final int qty,
									final MFSFrame parent)
	{
		/* Instantiate Worker Thread */
		Thread t = new Thread()
		{
			public void run()
			{
				int rc = 0;
				MFSPrintDriver pd = new MFSPrintDriver();
				pd.addValue("MCTL", mctl);
				pd.addValue("CRCT", crct);
				pd.addValue("INPN", inpn);
				pd.addValue("COOC", cooc);
				rc = pd.print("TRACKSUB3", qty);

				if (rc != 0)
				{
					String erms = "Printer Error: Unable to print Tracksub3 Label.";
					showOkMB(parent, erms); //~11C
				}

				pd.closeConnections();
			}
		};

		/* Spawn thread */
		t.start();
	}

	//~14D Remove width parameter
	/**
	 * @param feature
	 * @param qty
	 * @param parent
	 */
	public static void vinlabel(final String feature, final int qty, final MFSFrame parent)
	{
		/* Instantiate Worker Thread */
		Thread t = new Thread()
		{
			public void run()
			{
				int rc = 0;
				MFSPrintDriver pd = new MFSPrintDriver();
				pd.addValue("FEATURE", feature);
				rc = pd.print("VINLABEL", qty);

				if (rc != 0)
				{
					String erms = "Printer Error: Unable to print VIN Number Label.";
					showOkMB(parent, erms); //~11C
				}

				pd.closeConnections();
			}
		};

		/* Spawn thread */
		t.start();
	}

	/**
	 * @param prln
	 * @param mctl
	 * @param cardLetter
	 * @param qty
	 * @param parent
	 */
	public static void vpd11s(final String prln, final String mctl,
								final String cardLetter, final int qty,
								final MFSFrame parent)
	{
		/* Instantiate Worker Thread */
		Thread t = new Thread()
		{
			public void run()
			{
				int rc = 0;
				MFSPrintDriver pd = new MFSPrintDriver();
				pd.addValue("MCTL", mctl);
				pd.addValue("PRLN", prln);
				pd.addValue("CARDTYPE", cardLetter);
				rc = pd.print("VPD11S", qty);

				if (rc != 0)
				{
					String erms = "Printer Error: Unable to print 11S Label.";
					showOkMB(parent, erms); //~11C
				}

				pd.closeConnections();
			}
		};

		/* Spawn thread */
		t.start();
	}

	/**
	 * @param prln
	 * @param mctl
	 * @param qty
	 * @param parent
	 */
	public static void vpdmif(final String prln, final String mctl, final int qty,
								final MFSFrame parent)
	{
		/* Instantiate Worker Thread */
		Thread t = new Thread()
		{
			public void run()
			{
				int rc = 0;
				MFSPrintDriver pd = new MFSPrintDriver();
				pd.addValue("MCTL", mctl);
				pd.addValue("PRLN", prln);
				rc = pd.print("VPDMIFF", qty);

				if (rc != 0)
				{
					String erms = "Printer Error: Unable to print Mif Label.";
					showOkMB(parent, erms); //~11C
				}

				pd.closeConnections();
			}
		};

		/* Spawn thread */
		t.start();
	}

	/**
	 * @param mctl
	 * @param burnOrUnburnFlag
	 * @param qty
	 * @param parent
	 */
	public static void vpdmif2(final String mctl, final String burnOrUnburnFlag,
								final int qty, final MFSFrame parent)
	{
		/* Instantiate Worker Thread */
		Thread t = new Thread()
		{
			public void run()
			{
				int rc = 0;
				MFSPrintDriver pd = new MFSPrintDriver();
				pd.addValue("MCTL", mctl);
				pd.addValue("FLAG", burnOrUnburnFlag);
				rc = pd.print("VPDMIF2", qty);

				if (rc != 0)
				{
					String erms = "Printer Error: Unable to print MIF2 Label.";
					showOkMB(parent, erms); //~11C
				}

				pd.closeConnections();
			}
		};

		/* Spawn thread */
		t.start();
	}

	/**
	 * @param mctl
	 * @param labt
	 * @param qty
	 * @param parent
	 */
	public static void vpdunburn(final String mctl, final String labt, final int qty,
									final MFSFrame parent)
	{
		/* Instantiate Worker Thread */
		Thread t = new Thread()
		{
			public void run()
			{
				int rc = 0;
				MFSPrintDriver pd = new MFSPrintDriver();
				pd.addValue("MCTL", mctl);
				if (labt.substring(0, 3).equals("11S") || labt.equals("BOTH"))
				{
					rc = pd.print("UBRN11S", qty);
				}
				if (labt.substring(0, 3).equals("MIF") || labt.equals("BOTH"))
				{
					rc = pd.print("UBRNMIF", qty);
				}
				if (!labt.substring(0, 3).equals("MIF")
						&& !labt.substring(0, 3).equals("11S")
						&& !labt.substring(0, 4).equals("BOTH"))
				{
					String erms = "Invalid Label Type in VD10 file";
					showOkMB(parent, erms); //~11C
				}

				if (rc != 0)
				{
					String erms = "Printer Error: Unable to print Unburn Label.";
					showOkMB(parent, erms); //~11C
				}

				pd.closeConnections();
			}
		};

		/* Spawn thread */
		t.start();
	}

	/**
	 * @param mctl
	 * @param qty
	 * @param parent
	 */
	public static void wuipid(final String mctl, final int qty, final MFSFrame parent)
	{
		/* Instantiate Worker Thread */
		Thread t = new Thread()
		{
			public void run()
			{
				int rc = 0;
				MFSPrintDriver pd = new MFSPrintDriver();
				pd.addValue("MCTL", mctl);
				rc = pd.print("WUIPID", qty);

				if (rc != 0)
				{
					String erms = "Printer Error: Unable to print Work Unit Label.";
					showOkMB(parent, erms); //~11C
				}

				pd.closeConnections();
			}
		};

		/* Spawn thread */
		t.start();
	}

	/**
	 * @param mctl
	 * @param qty
	 * @param parent
	 */
	public static void wuprint(final String mctl, final int qty, final MFSFrame parent)
	{
		/* Instantiate Worker Thread */
		Thread t = new Thread()
		{
			public void run()
			{
				int rc = 0;
				MFSPrintDriver pd = new MFSPrintDriver();
				pd.addValue("MCTL", mctl);
				pd.addValue("ALLF", "Y");
				rc = pd.print("WUPRINT", qty);

				if (rc != 0)
				{
					String erms = "Printer Error: Unable to print WUPRINT Report.";
					showOkMB(parent, erms); //~11C
				}

				pd.closeConnections();
			}
		};

		/* Spawn thread */
		t.start();
	}
	
	//~12A New method
	/**
	 * @param mctl
	 * @param qty
	 * @param ctrlnumbr
	 * @param machserial
	 * @param matpmcsn
	 * @param syscert
	 * @param auto
	 * @param parent
	 */
	public static void wusnlbls(final String mctl, final int qty, final String ctrlnumbr,		/*~15C*/		
								final String machserial, final String matpmcsn,					/*~15C*/
								final String matpmcsn2,                                         /*~18A*/
								final String syscert, final String auto, final MFSFrame parent)	/*~15C*/

	{
		/* Instantiate Worker Thread */
		Thread t = new Thread()
		{
			public void run()
			{
				int rc = 0;
				MFSPrintDriver pd = new MFSPrintDriver();
				pd.addValue("MCTL", mctl);
				pd.addValue("CTRLNUMBR", ctrlnumbr);		/*~15C*/
				pd.addValue("MACHSERIAL", machserial);		/*~15C*/
				pd.addValue("MATPMCSN", matpmcsn);			/*~15C*/
				pd.addValue("MATPMCSN2", matpmcsn2);		/*~18A*/
				pd.addValue("SYSCERT", syscert);			/*~15C*/
				pd.addValue("AUTO", auto);                  /*~17A*/
				rc = pd.print("WUSNLBL", qty);

				if (rc != 0)
				{
					String erms = "Printer Error: Work Unit Serial Number Labels.";
					showOkMB(parent, erms);
				}

				pd.closeConnections();
			}
		};

		/* Spawn thread */
		t.start();
	}

	/**
	 * @param matp
	 * @param mmdl
	 * @param mspi
	 * @param mcsn
	 * @param user
	 * @param autop
	 * @param qty
	 * @param parent
	 * @return the return code
	 */
	public static int wwid(final String matp, final String mmdl, final String mspi,
							final String mcsn, final String user, final String autop,
							final int qty, final MFSFrame parent)
	{
		int rc = 0;

		MFSPrintDriver pd = new MFSPrintDriver();
		pd.addValue("MATP", matp);
		pd.addValue("MMDL", mmdl);
		pd.addValue("MSPI", mspi);
		pd.addValue("MCSN", mcsn);
		pd.addValue("USER", user);
		pd.addValue("AUTOP", autop);
		rc = pd.print("WWID", qty);

		if (rc == 666)
		{
			//Do not display a message if rc == 666
		}
		else if (rc != 0)
		{
			String erms = "Printer Error: Unable to print WWID Label.";
			showOkMB(parent, erms); //~11C
		}

		pd.closeConnections();

		return rc;

	}

	//IPSR 31867EM for printing software keycode labes ~1
	/**
	 * @param mctl
	 * @param whom
	 * @param qty
	 * @param parent
	 * @return the return code
	 */
	public static int softKeycode(final String mctl, final String whom, final int qty,
									final MFSFrame parent)
	{
		int rc = 0;

		MFSPrintDriver pd = new MFSPrintDriver();
		pd.addValue("USER", MFSConfig.getInstance().get8CharUser());
		pd.addValue("MCTL", mctl);
		pd.addValue("WHOM", whom);
		rc = pd.print("SFTKEYCODE", qty);

		if (rc == 666)
		{
			//Do not display a message if rc == 666
		}
		else if (rc != 0)
		{
			String erms = "Printer Error: Unable to print SFTKEYCODE Label.";
			showOkMB(parent, erms); //~11C
		}

		pd.closeConnections();

		return rc;
	}

	/* Begin ~2A */
	/**
	 * @param cntr
	 * @param mctl
	 * @param source
	 * @param qty
	 * @param parent
	 * @param brand ~14A
	 */
	public static void pcpplabels(final String cntr, final String mctl, final String source,
								final int qty, final String brand, final MFSFrame parent)//~14C
	{
		/* Instantiate Worker Thread */
		Thread t = new Thread()
		{
			public void run()
			{
				int rc = 0;
				MFSPrintDriver pd = new MFSPrintDriver();
				pd.addValue("CNTR", cntr);
				pd.addValue("MCTL", mctl);
				pd.addValue("SOURCE", source);
				pd.addValue("BRAND", brand);      //~14A
				rc = pd.print("PCPPLABELS", qty); //~14C
				if (rc != 0)
				{
					/* ~3 Change error message */
					String erms = "Printer Error: Unable to Print PCPPLABELS Label."; //~14C
					showOkMB(parent, erms); //~11C
				}

				pd.closeConnections();
			}
		};

		/* Spawn thread */
		t.start();
	}

	/**
	 * @param cntr Can be blank if mctl is sent
	 * @param mctl Ignored if cntr is sent
	 * @param qty
	 * @param parent
	 */
	public static void caseContent(final String cntr, final String mctl, final int qty, 
	                               final MFSFrame parent)  // ~22C
	{
		/* Instantiate Worker Thread */
		Thread t = new Thread()
		{
			public void run()
			{
				int rc = 0;
				MFSPrintDriver pd = new MFSPrintDriver();
				if ((cntr != null) && !cntr.equals("")) // ~22A
				{
					pd.addValue("CNTR", cntr);
				}
				else if ((mctl != null) && !mctl.equals("")) // ~22A
				{
					pd.addValue("MCTL", mctl);               // ~22A
				}
				else  // ~22A
				{
					rc = 5;
					String erms = "Printer Error: CNTR or MCTL must be entered.";
					showOkMB(parent, erms);
				}

				if ((rc == 0) && (pd.print("CASECONTENT", qty) != 0))  // ~22C
				{
					String erms = "Printer Error: Unable to print Case Content.";
					showOkMB(parent, erms); //~11C
				}

				pd.closeConnections();
			}
		};

		/* Spawn thread */
		t.start();
	}

	/* End ~2A */

	//New printing Method to 1Slabel ~6A
	/**
	 * @param matp
	 * @param mmdl
	 * @param lqty
	 * @param qty
	 * @param parent
	 */
	public static void matpMdlSn(final String matp, final String mmdl, final String lqty,
									final int qty, final MFSFrame parent)
	{
		/* Instantiate Worker Thread */
		Thread t = new Thread()
		{
			public void run()
			{
				int rc = 0;
				MFSPrintDriver pd = new MFSPrintDriver();
				pd.addValue("MATP", matp);
				pd.addValue("MMDL", mmdl);
				pd.addValue("LQTY", lqty);
				rc = pd.print("MATPMDLSN1S", qty);

				if (rc != 0)
				{
					String erms = "Printer Error: Unable to print Case Content.";
					showOkMB(parent, erms); //~11C
				}

				pd.closeConnections();
			}
		};

		/* Spawn thread */
		t.start();
	}
	
	//~8A New method
	/**
	 * @param matp
	 * @param mcsn
	 * @param qty
	 * @param parent
	 */
	public static void UIDLbl(final String matp, final String mcsn, final int qty, final MFSFrame parent) 
	{
		/* Instantiate Worker Thread */
		Thread t = new Thread()
		{
			public void run()
			{
				int rc = 0;
				MFSPrintDriver pd = new MFSPrintDriver();
				pd.addValue("MATP", matp);
				pd.addValue("MCSN", mcsn);
				rc = pd.print("UIDLABEL", qty);

				if(rc == 666)
				{
					//Do not display a message if rc == 666
				}	
				else if(rc != 0)
				{
					String erms = "Printer Error: Unable to print UID Label.";
					showOkMB(parent, erms); //~11C
				}			
				pd.closeConnections();
			}
		};
		/* Spawn thread */	
		t.start();
	}
	
}
