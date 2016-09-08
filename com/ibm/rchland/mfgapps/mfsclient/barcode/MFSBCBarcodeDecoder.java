/* © Copyright IBM Corporation 2005, 2011. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2006-08-22  ~01 34222BP  VH Avila         -Copy the PN field value to ITEM field  
 *                                            when the RTV_HDREC function need it
 * 2006-10-03  ~02 36494BP  VH Avila         -When the FillFieldItem field class from
 *                                            BCPartObject is true don't clear its fields
 * 2007-01-15   ~3 34242JR  R Prechel        -Java 5 version
 * 2007-05-24   ~4 37676JM  R Prechel        -Use new Comm model for
 * 2007-06-18   ~5 37556CD  T He			 -Moved logic in constructor to new method config()
 * 2007-11-06   ~6 40104PB  R Prechel        -Do not call config from constructor
 * 2008-11-04   ~7 41356MZ  Santiago SC      -Collect serial number for CSC orders CSNI='M' or 'L'
 * 2009-09-01  ~08 43940JM  Juan Mastachi    -Add support for captive parts CSNI='W'
 * 2010-03-17  ~09 47595MZ	Ray Perry		 -Save RTV_HDREC returns so we don't have to call every time
 * 2010-11-01  ~10 49513JM	Toribio H.   	 -Make RTV_HDREC Cacheable
 * 2010-11-22  ~11 48749JL  Santiago SC      -new initializeDecoder() method
 * 2011-11-28  ~12 50723FR  Toribio H.       -Support sequence retrieveal for CNSI ='G' (Grey Market Parts)
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.barcode;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.ListIterator;
import java.util.Vector;

import com.ibm.rchland.mfgapps.client.utils.io.IGSPad;
import com.ibm.rchland.mfgapps.client.utils.messagebox.IGSMessageBox;
import com.ibm.rchland.mfgapps.mfscommon.MFSActionable;
import com.ibm.rchland.mfgapps.mfscommon.MFSConfig;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSComm;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSFixedTransaction;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSTransaction;
import com.ibm.rchland.mfgapps.mfscommon.comm.trx.RTV_HDREC;

/**
 * <code>MFSBCBarcodeDecoder</code> decodes barcodes.
 * @author The MFS Client Development Team
 */
public class MFSBCBarcodeDecoder extends Thread
{
	boolean invalid_barcode = false; //if there is no match in the rules for

	// the barcode
	protected MFSBCPartObject myBCPartObject;

	protected MFSBCIndicatorValue myBCIndicatorValue;

	/** The barcode input to parse. */
	protected String myBarcodeInput;
	
	/** The <code>Vector</code> of <code>BCRule</code>s.
	 * The key is the bMax (Ai code size)
	 * The value is a Vector that contains the barcode rules that have same bMax value
	 */
	protected Hashtable<Integer, Vector<MFSBCRule>> bcRules = new Hashtable<Integer, Vector<MFSBCRule>>(); //~6C
	
	//~3A 34242JR Everything below here is new or changed
	/** <code>true</code> iff a decode request is being processed. */
	private boolean processingBarcode = false; //~3A

	/** The synchronization lock <code>Object</code>. */
	private final Object lock = new Object(); //~3A
	
	/** The action message displayed while a barcode is being decoded. */
	public static final String DECODING_BARCODE_AMSG = "Decoding Barcode, Please Wait..."; //$NON-NLS-1$
	
	/** The error message displayed when barcode decoding timed out. */
	public static final String DECODE_TIMED_OUT_ERMS = 
		"Barcode decoding timed out.  Please try again."; //$NON-NLS-1$
	
	/** The error message displayed when the user enters an invalid barcode. */
	public static final String INVALID_BARCODE_ERMS = "Invalid Barcode"; //$NON-NLS-1$
	
	/** The sole instance of <code>MFSBCBarcodeDecoder</code>. */
	private static final MFSBCBarcodeDecoder INSTANCE = startDecoder();

	/** Further barcodes need to be looked for in Rules and Subrules. */
	private static final int BCRULE_CHECK_CONTINUE = 0; //~10
	
	/** Skip subsequent Subrules check.. */
	private static final int BCRULE_CHECK_SKIP_SUB = 1; //~10
	
	/** Rules check is done. No further Check in rules and Subrules is needed. */
	private static final int BCRULE_CHECK_DONE =  2; //~10


	/** Serial Number (returns 'SN' object). */
	protected static final char INSQ_CODE = 'S';

	/** Part Number (returns 'PN' object) */
	protected static final char INPN_CODE = 'P';

	/** EC code (returns 'EC' object) */
	protected static final char INEC_CODE = 'E';

	/** Machine Serial Number (return 'MSN' object */
	protected static final char MCSN_CODE = 'M';

	/** Card Assembly Number > CA code (returns 'CA' object) */
	protected static final char INCA_CODE = 'C';

	/** Header code (returns 'HDR' code) */
	protected static final char HDR_CODE = 'H'; 

	/** Order Number (returns 'ON' object) */
	protected static final char ORNO_CODE = 'O';

	/** Container (returns 'CT' object) */
	protected static final char CNTR_CODE = 'N'; 

	/** PL Number (returns 'PL' object) */
	protected static final char PL_CODE = 'L'; 

	/** returns 'WU' object */
	protected static final char WU_CODE = 'W'; 

	/** Quantity (returns 'QT' object) */
	protected static final char QT_CODE = 'Q';

	/** Country Code */
	protected static final char CTRY_CODE = 'R'; 

	/** Model Number */
	protected static final char MMDL_CODE = 'D'; 

	protected static final String BASE32_CODE = "B"; //$NON-NLS-1$

	// next two codes are new - to control the decode output for 'MS' ai code
	/** Plant code ('10' for Roch.); 2 char (returns 'PC' object) */
	protected static final char MSPI_CODE = 'Z';

	/** Machine Type Code (returns 'MTC' object) ; 4 char long */
	protected static final char MATP_CODE = 'T'; // 

	/** Used for various different codes */
	protected static final char VARIABLE_CODE = 'V'; 

	/** MAC ID Number (returns MC object) */
	protected static final char MACID_CODE = 'A'; 

	/** 'Padding' Value */
	protected static final char PAD_VAL = '0'; 

	/** invalid return code (in the old C-code was '102') */
	protected static final int INVALID_CODE = 10;

	/** Good return code */
	protected static final int GOOD_RETURN = 0;
	
	/** Good Value*/
	private static final String GOOD_VALUE = "GOOD VALUE"; //$NON-NLS-1$
	//some of the LEN constants are not used any more
	//they are/were mostly used for calculating
	//how many zeros is the parsed barcode padded with
	protected static final int CR2_MSPI_LEN = 2;

	protected static final int CR2_MCSN_LEN = 7;

	protected static final int CR3_INPN_LEN = 12;

	protected static final int CRH2_ORNO_LEN = 6;

	protected static final int CR3_INSQ_LEN = 12;

	protected static final int CR5_CNTR_LEN = 10;

	protected static final int HDR_CODE_LEN = 5;

	protected static final int CR3_INEC_LEN = 12;

	protected static final int CR3_INCA_LEN = 12;

	protected static final int BASE32_LEN = 10;

	//the constant needed for MCSN case
	//instead of CR2_MCSN_LEN = 7
	protected static final int DEC_SN_SIZE = 9; 

	protected static final int MIN_BARCODE_LEN = 7;

	protected static final int CRH2_MACID_LEN = 12;

	protected static final int CRH2_MMDL_LEN = 3;

	//~3A
	/**
	 * Returns the sole instance of <code>MFSBCBarcodeDecoder</code>.
	 * @return the sole instance of <code>MFSBCBarcodeDecoder</code>
	 */
	public static MFSBCBarcodeDecoder getInstance()
	{
		return INSTANCE;
	}

	//~3A
	/**
	 * Creates a new <code>MFSBCBarcodeDecoder</code> and starts the barcode
	 * decoding <code>Thread</code>.
	 * @return the new <code>MFSBCBarcodeDecoder</code>
	 */
	private static MFSBCBarcodeDecoder startDecoder()
	{
		MFSBCBarcodeDecoder result = new MFSBCBarcodeDecoder();
		result.setDaemon(true);
		result.start();
		return result;
	}

	//~3A
	/**
	 * Constructs a new <code>MFSBCBarcodeDecoder</code>. This class implements
	 * the <cite>Singleton </cite> design pattern. To ensure only one instance
	 * of <code>MFSBCBarcodeDecoder</code> exists, the only constructor has
	 * <code>private</code> visibility.
	 */
	private MFSBCBarcodeDecoder()
	{
		super();
		//~6D Don't call config
	}

	/**
	 * Checks subrules and parses them
	 * @param barcode
	 * @param indVal
	 * @param currBcRule
	 * @param currBcSubRule
	 * @return 
	 * 	0: further barcodes need to be looked for in Rules and Subrules.
	 * 	1: skip subsequent Subrules check.
	 *  3: Rules check is done. No further Check in rules and Subrules is needed.
	 *  Changes:  	
	 *  ~12 -Support sequence retrieveal for CNSI ='G' (Grey Market Parts)	 
	 */
	private int checkSubRules(String barcode, MFSBCIndicatorValue indVal, 
									MFSBCRule currBcRule, MFSBCSubrule currBcSubRule) {
		int zeroPadLen = 0;
		int chkSubRule_rc = BCRULE_CHECK_CONTINUE;
		//if there IS substring
		String bcvalue;
		StringBuffer sb;
		 
		switch (currBcSubRule.tpe) 
		{
			case INSQ_CODE : //('S') >returns object Serial Number ('SN') 
				//if the 'rtv' value is NOT blank then return 'rtv'
				if (!currBcSubRule.rtv.trim().equals(""))  //$NON-NLS-1$
				{
					myBCPartObject.setSN(new String(currBcSubRule.rtv));
					chkSubRule_rc = BCRULE_CHECK_DONE;
				}
				else
				{
					zeroPadLen = CR3_INSQ_LEN - currBcSubRule.len;
					if (indVal.getCSNI().equals("1") || //'Csni == ON' //$NON-NLS-1$
						indVal.getCSNI().equals("W"))   // ~08C For captive parts //$NON-NLS-1$
					{
						//checks if the barcode starts with "11S" and 
						//its (full barcode length) length is 19 characters
						if (((barcode.substring(0, currBcRule.bcai.length()).equals("11S")) && //$NON-NLS-1$
								(barcode.length() == 19)))  
						{
							bcvalue = barcode.substring(currBcSubRule.str,
													currBcSubRule.str + CR2_MSPI_LEN);
							sb = new StringBuffer(bcvalue);
							//pad it with zeros (PAD_VAL = '0')
							for (int x = 0; x < zeroPadLen; x++) 
							{
								sb.append(PAD_VAL);
							}
							// add the rest of the barcode after you pad it with '0' (PAD_VAL)
							sb.append(barcode.substring(currBcSubRule.str + CR2_MSPI_LEN,
									currBcSubRule.str + currBcSubRule.len));
							myBCPartObject.setSN(sb.toString());
							chkSubRule_rc = BCRULE_CHECK_DONE;
						} // end of '11S'
						else
						{
							// still 'Csni == ON'   
							// not 11S 19 digit barcode 
							bcvalue = barcode.substring(currBcSubRule.str, 
													currBcSubRule.str + CR2_MSPI_LEN);
							sb = new StringBuffer(bcvalue);
							for (int x = 0; x < zeroPadLen; x++) 
							{
								sb.append(PAD_VAL);
							}
							sb.append(barcode.substring(currBcSubRule.str + CR2_MSPI_LEN));
							myBCPartObject.setSN(sb.toString());
							chkSubRule_rc = BCRULE_CHECK_DONE;
						} //end of 'not 11S 19 digit barcocode
					} //end of 'Csni == ON'
					else if (indVal.getCSNI().equals("P"))  //$NON-NLS-1$
					{
						sb = new StringBuffer(0);
						for (int x = 0; x < zeroPadLen; x++) 
						{
							sb.append(PAD_VAL);
						}
						sb.append(barcode.substring(currBcSubRule.str, 
								currBcSubRule.str + currBcSubRule.len));
						myBCPartObject.setSN(sb.toString());
						chkSubRule_rc = BCRULE_CHECK_DONE;
					}
					else if (indVal.getCSNI().equals("S") || indVal.getCSNI().equals("L") || //$NON-NLS-1$ //$NON-NLS-2$
							indVal.getCSNI().equals("M")) //~7C //$NON-NLS-1$
					{
						sb = new StringBuffer(0);
						sb.append(barcode.substring(currBcSubRule.str, 
								currBcSubRule.str + currBcSubRule.len));
						for (int x = currBcSubRule.len; x < CR3_INSQ_LEN; x++) 
						{
							sb.append(' ');
						}
						myBCPartObject.setSN(sb.toString());
						chkSubRule_rc = BCRULE_CHECK_DONE;
					}
					else if (indVal.getCSNI().equals("F")) //added for CHEETAH, 30732JP //$NON-NLS-1$
					{
						sb = new StringBuffer(0);
						sb.append(barcode.substring(currBcSubRule.str, 
								currBcSubRule.str + currBcSubRule.len));
						for (int x = currBcSubRule.len; x < CR3_INSQ_LEN; x++) 
						{
							sb.append(' ');
						}
						myBCPartObject.setSN(sb.toString());
						chkSubRule_rc = BCRULE_CHECK_DONE;
					}
					else if (indVal.getCSNI().equals("G")) //added for Grey market parts //$NON-NLS-1$
					{
						String rawSerial = barcode.substring(currBcSubRule.str, currBcSubRule.str + currBcSubRule.len);    
						myBCPartObject.setSN(rawSerial); 
						chkSubRule_rc = BCRULE_CHECK_DONE;
					}					
					else
					{
						//may be a 1S type of barcode that is MCSN, look for another barcode
						//subRuleNum = currRule.subrulesArray.length; //reset the subrules to 3
						chkSubRule_rc = BCRULE_CHECK_SKIP_SUB;
					}
				}				
				break;					
			case MCSN_CODE : //('M') >returns object Machine Serial Number ('MSN') 
				//if the 'rtv' value is NOT blank then return 'rtv'
				if (!currBcSubRule.rtv.trim().equals(""))  //$NON-NLS-1$
				{
					myBCPartObject.setMSN(new String(currBcSubRule.rtv));
					chkSubRule_rc = BCRULE_CHECK_DONE;
				}
				else
				{
					// if the machine type indicator is set to ON, then
					// extract the plant code and copy remaining portion as serial number data 
					if (indVal.getCMTI().equals("1") || indVal.getCMTI().equals("M")) //Cmti == ON @1 //$NON-NLS-1$ //$NON-NLS-2$
					{
						zeroPadLen = DEC_SN_SIZE - currBcSubRule.len;
						bcvalue = barcode.substring(currBcSubRule.str, 
								currBcSubRule.str + CR2_MSPI_LEN);
						sb = new StringBuffer(bcvalue);
						for (int x = 0; x < zeroPadLen; x++) 
						{
							sb.append(PAD_VAL);
						}
						sb.append(barcode.substring(currBcSubRule.str + CR2_MSPI_LEN, 
								currBcSubRule.str + currBcSubRule.len));
						myBCPartObject.setMSN(sb.toString());
						chkSubRule_rc = BCRULE_CHECK_DONE;
					} // end of '(Cmti == ON)'
					else 
					{
						//may be a 1S type of barcode that is INSQ, look for another barcode
						//subRuleNum = currRule.subrulesArray.length; //reset the subrules to 3
						chkSubRule_rc = BCRULE_CHECK_SKIP_SUB;
					}
				}
				break;				
			case INPN_CODE : //('P') >returns object Part Number ('PN') 
				//if the 'rtv' value is NOT blank then return 'rtv'
				if (!currBcSubRule.rtv.trim().equals(""))  //$NON-NLS-1$
				{
					myBCPartObject.setPN(new String(currBcSubRule.rtv));
					chkSubRule_rc = BCRULE_CHECK_DONE;
				}
				else
				{
					zeroPadLen = CR3_INPN_LEN - currBcSubRule.len;
					sb = new StringBuffer(0);
					for (int x = 0; x < zeroPadLen; x++)
					{
						sb.append(PAD_VAL);
					}
					sb.append(barcode.substring(currBcSubRule.str, 
							currBcSubRule.str + currBcSubRule.len));
					myBCPartObject.setPN(sb.toString());
					chkSubRule_rc = BCRULE_CHECK_DONE;
				}
				break;				
			case ORNO_CODE : //('O') >returns object Order Number ('ON') 
				//if the 'rtv' value is NOT blank then return 'rtv'
				if (!currBcSubRule.rtv.trim().equals(""))  //$NON-NLS-1$
				{
					myBCPartObject.setON(new String(currBcSubRule.rtv));
					chkSubRule_rc = BCRULE_CHECK_DONE;
				}
				else
				{
					zeroPadLen = CRH2_ORNO_LEN - currBcSubRule.len;
					sb = new StringBuffer(0);
					for (int x = 0; x < zeroPadLen; x++)
					{
						sb.append(PAD_VAL);
					}
					sb.append(barcode.substring(currBcSubRule.str, 
							currBcSubRule.str + currBcSubRule.len));
					myBCPartObject.setON(sb.toString());
					chkSubRule_rc = BCRULE_CHECK_DONE;
				}
				break;				
			case INEC_CODE : //('E') >returns object EC Number ('EC') 
				//if the 'rtv' value is NOT blank then return 'rtv'
				if (!currBcSubRule.rtv.trim().equals(""))  //$NON-NLS-1$
				{
					myBCPartObject.setEC(new String(currBcSubRule.rtv));
					chkSubRule_rc = BCRULE_CHECK_DONE;
				}
				else if (indVal.getECRI().equals("S"))  //$NON-NLS-1$
				{
					sb = new StringBuffer(0);
					sb.append(barcode.substring(currBcSubRule.str, 
							currBcSubRule.str + currBcSubRule.len));
					for (int x = currBcSubRule.len; x < CR3_INEC_LEN; x++) 
					{
						sb.append(' ');
					}
					myBCPartObject.setEC(sb.toString());
					chkSubRule_rc = BCRULE_CHECK_DONE;
				}
				else
				{
					zeroPadLen = CR3_INEC_LEN - currBcSubRule.len;
					sb = new StringBuffer(0);
					if (currBcSubRule.len < CR3_INEC_LEN)
					{
						sb.append("1"); //$NON-NLS-1$
					}
					if (currBcSubRule.len > 6)
					{
						for (int x=1;x<zeroPadLen;x++)
						{
							sb.append(PAD_VAL);
						}
						sb.append(barcode.substring(currBcSubRule.str, 
								currBcSubRule.str + currBcSubRule.len));
					}
					else
					{
						for (int x=2;x<zeroPadLen;x++)
						{
							sb.append(PAD_VAL);
						}
						sb.append(barcode.substring(currBcSubRule.str, 
								currBcSubRule.str + currBcSubRule.len));
						sb.append(" "); //$NON-NLS-1$
					}
					myBCPartObject.setEC(sb.toString());
					chkSubRule_rc = BCRULE_CHECK_DONE;
				}
				break;				
			case INCA_CODE : //('C') >returns object Card Assembly  Number ('CA') 	
				//if the 'rtv' value is NOT blank then return 'rtv'
				if (!currBcSubRule.rtv.trim().equals(""))  //$NON-NLS-1$
				{
					myBCPartObject.setCA(new String(currBcSubRule.rtv));
					chkSubRule_rc = BCRULE_CHECK_DONE;
				}
				else
				{
					zeroPadLen = CR3_INCA_LEN - currBcSubRule.len;
					sb = new StringBuffer(0);
					if (currBcSubRule.len < CR3_INCA_LEN)
					{
						sb.append("1"); //$NON-NLS-1$
					}
					if (currBcSubRule.len > 6)
					{
						for (int x=1;x<zeroPadLen;x++)
						{
							sb.append(PAD_VAL);
						}
						sb.append(barcode.substring(currBcSubRule.str, 
								currBcSubRule.str + currBcSubRule.len));
					}
					else
					{
						for (int x=2;x<zeroPadLen;x++)
						{
							sb.append(PAD_VAL);
						}
						sb.append(barcode.substring(currBcSubRule.str, 
								currBcSubRule.str + currBcSubRule.len));
						sb.append(" "); //$NON-NLS-1$
					}
					myBCPartObject.setCA(sb.toString());
					chkSubRule_rc = BCRULE_CHECK_DONE;
				}
				break;				
			case MSPI_CODE : //('Z') >returns object Plant Code ('PC') 
				//if the 'rtv' value is NOT blank then return 'rtv'
				if (!currBcSubRule.rtv.trim().equals(""))  //$NON-NLS-1$
				{
					myBCPartObject.setPC(new String(currBcSubRule.rtv));
					chkSubRule_rc = BCRULE_CHECK_DONE;
				}
				else
				{
					// Plant Code > 2 characters long
					if (barcode.length() == 13) 
					{
						sb = new StringBuffer(0);
						sb.append(barcode.substring(currBcSubRule.str, currBcSubRule.str + currBcSubRule.len));
						myBCPartObject.setPC(sb.toString());
						chkSubRule_rc = BCRULE_CHECK_DONE;
					}
				}
				break;			
			case MATP_CODE : // ('T') >returns object Machine Type Code ('MTC'); it is > 4 char long
				//if the 'rtv' value is NOT blank then return 'rtv'
				if (!currBcSubRule.rtv.trim().equals(""))  //$NON-NLS-1$
				{
					myBCPartObject.setMTC(new String(currBcSubRule.rtv));
					chkSubRule_rc = BCRULE_CHECK_DONE;
				}
				else
				{
					sb = new StringBuffer(0);
					sb.append(barcode.substring(currBcSubRule.str, currBcSubRule.str + currBcSubRule.len));
					myBCPartObject.setMTC(sb.toString());
					chkSubRule_rc = BCRULE_CHECK_DONE;
				}
				break;				
			case WU_CODE : // ('W') >returns object ('WU') 
				//if the 'rtv' value is NOT blank then return 'rtv'
				if (!currBcSubRule.rtv.trim().equals(""))  //$NON-NLS-1$
				{
					myBCPartObject.setWU(new String(currBcSubRule.rtv));
					chkSubRule_rc = BCRULE_CHECK_DONE;
				}
				else
				{
					sb = new StringBuffer(0);
					sb.append(barcode.substring(currBcSubRule.str, currBcSubRule.str + currBcSubRule.len));
					myBCPartObject.setWU(sb.toString());
					chkSubRule_rc = BCRULE_CHECK_DONE;
				}
				break;				
			case PL_CODE : // ('L') >returns object PL Number ('PL') 
				//if the 'rtv' value is NOT blank then return 'rtv'
				if (!currBcSubRule.rtv.trim().equals(""))  //$NON-NLS-1$
				{
					myBCPartObject.setPL(new String(currBcSubRule.rtv));
					chkSubRule_rc = BCRULE_CHECK_DONE;
				}
				else
				{
					sb = new StringBuffer(0);
					sb.append(barcode.substring(currBcSubRule.str, currBcSubRule.str + currBcSubRule.len));
					myBCPartObject.setPL(sb.toString());
					chkSubRule_rc = BCRULE_CHECK_DONE;
				}
				break;			
			case CNTR_CODE : // //('N') >returns object Container ('CT')
				//if the 'rtv' value is NOT blank then return 'rtv'
				if (!currBcSubRule.rtv.trim().equals(""))  //$NON-NLS-1$
				{
					myBCPartObject.setCT(new String(currBcSubRule.rtv));
					chkSubRule_rc = BCRULE_CHECK_DONE;
				}
				else
				{
					sb = new StringBuffer(0);
					sb.append(barcode.substring(currBcSubRule.str, currBcSubRule.str + currBcSubRule.len));
					myBCPartObject.setCT(sb.toString());
					chkSubRule_rc = BCRULE_CHECK_DONE;
				}
				break;				
			case QT_CODE : // ('Q') >returns object Quantity ('QT') 
				//if the 'rtv' value is NOT blank then return 'rtv'
				if (!currBcSubRule.rtv.trim().equals(""))  //$NON-NLS-1$
				{
					myBCPartObject.setQT(new String(currBcSubRule.rtv));
					chkSubRule_rc = BCRULE_CHECK_DONE;
				}
				else
				{
					sb = new StringBuffer(0);
					sb.append(barcode.substring(currBcSubRule.str, currBcSubRule.str + currBcSubRule.len));
					myBCPartObject.setQT(sb.toString());
					chkSubRule_rc = BCRULE_CHECK_DONE;
				}
				break;				
			case HDR_CODE : //('H') >returns object Header Code ('HDR') 
				//if the 'rtv' value is NOT blank then return 'rtv'
				if (!currBcSubRule.rtv.trim().equals(""))  //$NON-NLS-1$
				{
					myBCPartObject.setHDR(new String(currBcSubRule.rtv));
					chkSubRule_rc = BCRULE_CHECK_DONE;
				}
				else
				{
					// if the length is anything but 22, or if it is 22 (11S) and not an 11S-Z
					// then go ahead and decode the header
					if ((barcode.length() != 22) || ((barcode.length() == 22) &&
							(!barcode.substring(10,11).equals("Z")))) //$NON-NLS-1$
					{
						sb = new StringBuffer(0);
						sb.append(barcode.substring(currBcSubRule.str, currBcSubRule.str + currBcSubRule.len));
						myBCPartObject.setHDR(sb.toString());

						//Assuming that HDR_CODE ('H') is the last 'subrule' (A or B or C)
						//then I call the 'translateHDRCode' to finish processing for HDR_CODE
						translateHDRCode();
						chkSubRule_rc = BCRULE_CHECK_DONE;
					}
				}
				break;
			case CTRY_CODE : // ('R') >returns object country ('CO') 
				//if the 'rtv' value is NOT blank then return 'rtv'
				if (!currBcSubRule.rtv.trim().equals(""))  //$NON-NLS-1$
				{
					myBCPartObject.setCO(new String(currBcSubRule.rtv));
					myBCPartObject.setScannedCO(new String(currBcSubRule.rtv));
					chkSubRule_rc = BCRULE_CHECK_DONE;
				}
				else
				{
					sb = new StringBuffer(0);
					sb.append(barcode.substring(currBcSubRule.str, currBcSubRule.str + currBcSubRule.len));
					myBCPartObject.setCO(sb.toString());
					myBCPartObject.setScannedCO(sb.toString());
					chkSubRule_rc = BCRULE_CHECK_DONE;
				}
				break;				
			case MACID_CODE : //('A') >returns object MAC ID Number ('MC') 
				//if the 'rtv' value is NOT blank then return 'rtv'
				if (!currBcSubRule.rtv.trim().equals(""))  //$NON-NLS-1$
				{
					myBCPartObject.setMC(new String(currBcSubRule.rtv));
					chkSubRule_rc = BCRULE_CHECK_DONE;
				}
				else
				{
					zeroPadLen = CRH2_MACID_LEN - currBcSubRule.len;
					sb = new StringBuffer(0);
					for (int x = 0; x < zeroPadLen; x++)
					{
						sb.append(PAD_VAL);
					}
					sb.append(barcode.substring(currBcSubRule.str, 
							currBcSubRule.str + currBcSubRule.len));
					myBCPartObject.setMC(sb.toString());
					chkSubRule_rc = BCRULE_CHECK_DONE;
				}
				break;
			case MMDL_CODE : //('D') >returns object MMDL number ('MD') 
				//if the 'rtv' value is NOT blank then return 'rtv'
				if (!currBcSubRule.rtv.trim().equals(""))  //$NON-NLS-1$
				{
					myBCPartObject.setMD(new String(currBcSubRule.rtv));
					chkSubRule_rc = BCRULE_CHECK_DONE;
				}
				else
				{
					sb = new StringBuffer(0);
					sb.append(barcode.substring(currBcSubRule.str, currBcSubRule.str + currBcSubRule.len));
					myBCPartObject.setMD(sb.toString());
					chkSubRule_rc = BCRULE_CHECK_DONE;
				}
				break;				
			default : // >returns object Default ('DEF') 
				sb = new StringBuffer(0);
				sb.append(barcode.substring(currBcSubRule.str, currBcSubRule.str + currBcSubRule.len));
				myBCPartObject.setDEF(sb.toString());
				chkSubRule_rc = BCRULE_CHECK_DONE;
				break;				
		} //end of switch
		return chkSubRule_rc;
	}
	
	//~5A
	/**
	 * Creates a new <code>MFSBCBarcodeDecoder</code>. This class implements
	 * the <cite>Singleton </cite> design pattern. To ensure only one instance
	 * of <code>MFSBCBarcodeDecoder</code> exists, the only constructor has
	 * <code>private</code> visibility.
	 */
	public void config()
	{
		MFSTransaction rtv_bc = new MFSFixedTransaction("RTV_BC    "); //$NON-NLS-1$
		MFSComm.getInstance();
		MFSComm.execute(rtv_bc);		
		String outString = rtv_bc.getOutput();
		int length = outString.length();
		final int ruleLength = 128;

		this.bcRules.clear(); //~6C
		if (length < ruleLength)
		{
			String erms = "Error downloading barcode rules."; //$NON-NLS-1$
			IGSMessageBox.showOkMB(null, null, erms, null);
			System.exit(0);
		}
		else
		{
			int start = 0;
			int end = 0;
			MFSBCRule bcRule = null;
			Vector<MFSBCRule> sameMaxSizeRules = null;
			while (start < length)
			{
				//~3C Moved parsing logic to MFSBCRule and MFSBCSubrule
				end = start + ruleLength;
				bcRule = new MFSBCRule(outString.substring(start, end));
				sameMaxSizeRules = this.bcRules.get(bcRule.bmax);
				if(sameMaxSizeRules != null)
				{
					sameMaxSizeRules.add(bcRule);
				}
				else
				{
					sameMaxSizeRules = new Vector<MFSBCRule>();
					sameMaxSizeRules.add(bcRule);
					this.bcRules.put(bcRule.bmax, sameMaxSizeRules);
				}
				start += ruleLength;
			}
			for (Enumeration <Vector<MFSBCRule>>rulesEnum = this.bcRules.elements() ; rulesEnum.hasMoreElements() ;) 
			{
				rulesEnum.nextElement().trimToSize(); //~6A			
			}					
		}
	}
	
	//should check CMTI of 'M' as well @1
	/**
	 * Decodes the specified <code>String</code> based on the flags set
	 * in the specified <code>BCIndicatorValue</code> and the barcode rules
	 * downloaded from the server when the <code>MFSBCBarcodeDecoder</code> was created. 
	 * A <code>BCPartObject</code> is used to return the processed barcode, the return code,
	 * and an error string if an error occurred.
	 * @param barcode the barcode <code>String</code> to decode
	 * @param indVal the <code>BCIndicatorValue</code> used to decode the barcode
	 * @return a <code>BCPartObject</code> 
	 */
	private MFSBCPartObject decodeBarcode(String barcode, MFSBCIndicatorValue indVal) 
	{  
		int rc = 0;
		boolean done = false;
		
		//clears the result objects before it processes the next bacode
		myBCPartObject.clearFields();
		barcode = barcode.toUpperCase().trim();

		// GO THROUGH ALL THE RULES (based on AS/400 rules' table)
		// ~10 Only loop through the rule that have the same Max Input barcode Length
		Vector<MFSBCRule> sameMaxSizeRules = this.bcRules.get(barcode.length()); 
		if(sameMaxSizeRules != null) {
			ListIterator<MFSBCRule> sameMaxSizeRulesIt = sameMaxSizeRules.listIterator();
			
			while (!done && sameMaxSizeRulesIt.hasNext())
			{
				MFSBCRule currBcRule = sameMaxSizeRulesIt.next();
				//if we match on AI code and barcode length
				if (currBcRule.bcai.equals(barcode.substring(0, currBcRule.ailn)))
				{
					// COUNTINUES TO CHECK THROUGH ALL THE THREE SUBRULES (A,B,C)
					// loop through all three parsing rules (subrules A,B,C) to 
					// determine what the barcode consists of
					try
					{
						int chkSubRule_rc = BCRULE_CHECK_CONTINUE;
						SKIP_CHK_SUBRULES: //Label to exit further BC SubRules Check of current BC Rule
						for(int subRuleNum = 0; subRuleNum < currBcRule.subrulesArray.length; subRuleNum++)
						{
							MFSBCSubrule currBcSubRule = null;
							currBcSubRule = currBcRule.subrulesArray[subRuleNum];						
							if(currBcSubRule.len == 0)
							{
								break;
							}
							else
							{
								if (currBcSubRule.sln > 0)  //if substring length > 0
								{
									//checks if the substring is valid - if the substring match was found
									if (barcode.substring(currBcSubRule.sbp, currBcSubRule.sbp + currBcSubRule.sln).
												equals(currBcSubRule.sub.trim())) 
									{
										chkSubRule_rc = checkSubRules(barcode, indVal, currBcRule, currBcSubRule); //~10
									} // End Secondary If
									else 
									{
										/* if the substring ('sub') does not match on ANY of the subrules,
										then stop the barcode parsing and return asn error message> INVALID BARCODE
										> there is not matching rule */									
										invalid_barcode = true;
										break SKIP_CHK_SUBRULES;
									} //end of else	
								} // End Main If Statement
								else 
								{
									chkSubRule_rc = checkSubRules(barcode, indVal, currBcRule, currBcSubRule); //~10
								} // End of else - no substring
								if(chkSubRule_rc == BCRULE_CHECK_SKIP_SUB)
								{
									break SKIP_CHK_SUBRULES;
								} 
							}
						} // End of for - loop thru sub rules
						if (chkSubRule_rc == BCRULE_CHECK_DONE) 
						{
							done = true;
						}					
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
				} // End of if - match on AI code
			} //	End of while - loop thru rules
		}
		//*******************************ERROR HANDLING below this point******************************
		//********************************************************************************************
		//NO MATCHING RULE, PNRI indicator is set to 'P' 
		//(meaning there is no 'AI'code PN on the barcode> The barcode is just the part number (PN) 
		//(error routine right below the end of the switch stmt)
		if (!done) 
		{
			try
			{					
				String resultBC = barcode;
				if (indVal.getPNRI().equals("P"))  //$NON-NLS-1$
				{
					if(CR3_INPN_LEN > barcode.length()) 
					{
						resultBC = IGSPad.pad(barcode, CR3_INPN_LEN, ' ', IGSPad.LEADING);
					}
					myBCPartObject.setPN(resultBC);
					done = true;
				}
				//******************BASE32 BARCODE ROUTINE****************************************************	
				// if the barcode is invalid AND the CA indicator is set AND the length of the barcode
				// is base 32 length, then process the barcode as base 32
				else if ((barcode.length() == BASE32_LEN) && (indVal.getCCAI().equals(BASE32_CODE))) 
				{
					// Base 32 barcode -- return inputted barcode 
					// call the method 'from_barcode'
					rc = from_barcode(barcode, myBCPartObject); //calls the method 'from_barcode'
	
					if (rc == GOOD_RETURN)
					{	//GOOD_RETURN code is '0'
						// if the barcode is converted, copy the part number and serial number
						// into the consecutive barcode result fields and set the corresponding
						// types
						resultBC = myBCPartObject.getPN();
						if(CR3_INCA_LEN > barcode.length()) 
						{
							resultBC = IGSPad.pad(myBCPartObject.getPN(), CR3_INCA_LEN, ' ', IGSPad.LEADING);
						}						
						myBCPartObject.setCA(resultBC);
						done = true;
					} //end of (rc == GOOD_RETURN)
				} //end of ' if ((barcode.length() == BASE32_LEN) && (indVal.getCCAI() == BASE32_CODE))'
			}
			catch(Exception e)
			{e.printStackTrace();}
		}
		if (done)
		{
			/* we want to make sure that any serial number that was decoded does not have bad characters in it */
			if(myBCPartObject.getSN().length() > 0)
			{
				//call method to make sure the decoded Serial Number is ok
				String validValue = validateDecodedValue(myBCPartObject.getSN(), "Part Serial"); //$NON-NLS-1$
				
				if(validValue.equals(GOOD_VALUE))
				{
					myBCPartObject.setRC(GOOD_RETURN);
				}
				else
				{
					myBCPartObject.setRC(INVALID_CODE);
					myBCPartObject.setErrorString(validValue);
				}		
			}
			if(myBCPartObject.getMSN().length() > 0)
			{
				//call method to make sure the decoded Machine Serial Number is ok
				String validValue = validateDecodedValue(myBCPartObject.getMSN(), "Machine Serial"); //$NON-NLS-1$
				
				if(validValue.equals(GOOD_VALUE)) 
				{
					myBCPartObject.setRC(GOOD_RETURN);
				}
				else
				{
					myBCPartObject.setRC(INVALID_CODE);
					myBCPartObject.setErrorString(validValue);
				}	
			}				
		}
		else
		{
			myBCPartObject.setRC(INVALID_CODE);
			myBCPartObject.setErrorString("INVALID BARCODE - no matching rule"); //$NON-NLS-1$
		}			
		return myBCPartObject;
	} // End method
	
	//~3A
	/**
	 * Decodes a barcode for the specified <code>MFSActionable</code>.
	 * @param actionable the <code>MFSActionable</code> to update while the
	 *        barcode is decoded
	 * @return <code>true</code> if decoding completed; <code>false</code>
	 *         if decoding timed out
	 */
	public boolean decodeBarcodeFor(MFSActionable actionable)
	{
		final int MAX_ITERATION = 200;
		int iterationCount = 0;
		boolean notBarcodeThread = !Thread.currentThread().equals(this);
		
		actionable.startAction(DECODING_BARCODE_AMSG);
		
		this.submitRequest();
		while(this.isRequestComplete() == false && iterationCount++ < MAX_ITERATION)
		{
			actionable.updateAction(DECODING_BARCODE_AMSG, -1);
			if(notBarcodeThread)
			{
				try
				{
					Thread.sleep(10);
				}
				catch(InterruptedException ie)
				{
					ie.printStackTrace();
				}
			}
		}
		actionable.stopAction();
		
		return iterationCount < MAX_ITERATION;
	}
	private int from_barcode(String barcode_data, MFSBCPartObject partObj)
	{
		final String validChars = "0123456789BCDEFGHJKLMNPQRSTVWXYZ"; //$NON-NLS-1$
		// Check for barcode length equal to 10 
		if(barcode_data.length() != 10)
		 return 1;

		// Check for invalid characters
		for(int i=0; i< barcode_data.length(); i++) 
		   if(validChars.indexOf(barcode_data.charAt(i)) == -1)
		    return 1;

		String lCode = barcode_data.substring(1,5);
		String rCode = barcode_data.substring(5,10);
		int rVal=0,lVal=0;
		int leftVal=0, rightVal=0;
		
		for(int i=0; i < 4; i++) {
			lVal = validChars.indexOf(lCode.charAt(i));
			lVal = (int)Math.pow(32,3-i)*lVal;
			leftVal += lVal;
			rVal = validChars.indexOf(rCode.charAt(i));
			rVal = (int)Math.pow(32,3-i)*rVal;
			rightVal += rVal;
			if(leftVal > 999999 || rightVal > 999999)
				return INVALID_CODE;
		}

		String leftBase32 = String.valueOf(leftVal);
		String rightBase32 = String.valueOf(rightVal);
		
		StringBuffer partNum = new StringBuffer(7);
		for (int x = 0, len=leftBase32.length(); x < 6 - len; x++)
			leftBase32 = PAD_VAL +leftBase32;
		for (int x = 0, len=rightBase32.length(); x < 6 - len; x++)
			rightBase32 = PAD_VAL +rightBase32;

		partNum.append(leftBase32.substring(0,2));
		try
		{
		
		//partNum.append(barcode_data.charAt(0)+barcode_data.charAt(9));
		partNum.append(barcode_data.charAt(0));
		partNum.append(barcode_data.charAt(9));
		partNum.append(leftBase32.substring(2,5));
	
		StringBuffer serialNum = new StringBuffer(7);
		serialNum.append(leftBase32.charAt(5));
		
		serialNum.append(rightBase32);
		
		partObj.setPN(partNum.toString());
		
		//The serial number (SN) is commented out in C-code as well - but it is working correctly
		partObj.setSN(serialNum.toString());
		}
		catch(Exception e)
		{
			e.printStackTrace();	
		}
		return GOOD_RETURN;
	}
	/**
	 * Returns the <code>BCPartObject</code>.
	 * @return the <code>BCPartObject</code>
	 */
	public MFSBCPartObject getBCMyPartObject() 
	{
		return this.myBCPartObject;
	}
	/**
	 * Returns the <code>BCIndicatorValue</code>.
	 * @return the <code>BCIndicatorValue</code>
	 */
	public MFSBCIndicatorValue getMyBCIndicatorValue() 
	{
		return this.myBCIndicatorValue;
	}
	
	/**
	 * Calls RTV_HDREC for the scannedItem
	 * @param scannedItem the scanned item
	 */
	private void hdr_codeConvert(String scannedItem)
	{	
		String item = ""; //$NON-NLS-1$
		if(myBCIndicatorValue.getFillFieldItem())				
		{
			item = scannedItem;
		}
		else
		{
			item = myBCIndicatorValue.getITEM();
		}		
		// ~09 - if we are efficient, save the RTV_HDREC returns and then look there first
		//~4C Method changed to use MFSTransaction and new Comm model
		//Pass the following data: Prln, Item, Part Number (PN)
		//which is decoded barcode, header code
		//Sometimes the ITEM must be like the PN
		/**
		 * Convert RTV_HDREC to new cacheable Trx.
		 */
		RTV_HDREC rtvHdRec = new RTV_HDREC();
		rtvHdRec.setInputPRLN(myBCIndicatorValue.getPRLN());
		/*~01A*/
		rtvHdRec.setInputITEM(item);
		rtvHdRec.setInputINPN(scannedItem);
		rtvHdRec.setInputHDR(myBCPartObject.getHDR());
		rtvHdRec.setInputECRI(myBCIndicatorValue.getECRI());
		
		if (!rtvHdRec.execute()) 
		{
			System.out.println("Error, rc=" + rtvHdRec.getReturnCode() +  //$NON-NLS-1$
								", message=" + rtvHdRec.getErrorMessage() + //$NON-NLS-1$ 
								".\n");  //$NON-NLS-1$
			if(!myBCIndicatorValue.getFillFieldItem())			/*~02A*/
			{
				myBCPartObject.clearFields();
			}
			myBCPartObject.setRC(INVALID_CODE);
			myBCPartObject.setErrorString(rtvHdRec.getErrorMessage());
		} 		
		//return the objects only if correct result
		else
		{
			if (myBCPartObject.getPN().equals("")) //$NON-NLS-1$
			{
				myBCPartObject.setPN(rtvHdRec.getOutputINPN());
			}
			if (!getMyBCIndicatorValue().getECRI().equals(" ") && //$NON-NLS-1$
				!getMyBCIndicatorValue().getECRI().equals("0")) //$NON-NLS-1$
			{
				//'Ecri == ON'
				myBCPartObject.setEC(rtvHdRec.getOutputHWEC());
			}
			if (!getMyBCIndicatorValue().getCCAI().equals(" ") && //$NON-NLS-1$
				!getMyBCIndicatorValue().getCCAI().equals("0")) //$NON-NLS-1$
			{
				//'Ccai == ON'
				//if the first char of the decoded ca number is a blank, then it is probably
				//all blanks so we want to do a trim() to set the ca value to ""
				//-if we do a trim() all the time we could trim off important blank padding at
				//the end of the barcode
				if(rtvHdRec.getOutputHWEC().substring(0,1).equals(" ")) //$NON-NLS-1$
				{
					myBCPartObject.setCA(rtvHdRec.getOutputHWEC().trim());
				}
				else
				{
					myBCPartObject.setCA(rtvHdRec.getOutputHWEC());
				}
			}	
			if (!getMyBCIndicatorValue().getCOOI().equals(" ") && //$NON-NLS-1$
			  		!getMyBCIndicatorValue().getCOOI().equals("0")) //$NON-NLS-1$
			{			
		  		myBCPartObject.setCO(rtvHdRec.getOutputCOOC());
			}
		}
	}

	//~11A
	/**
	 * Initializes the decoder with default <code>MFSBCIndicatorValue</code> and default
	 * <code>MFSBCPartObject</code>.
	 */
	public void initializeDecoder()
	{
		/* Some reprint locations don't have the indicator initialized.
		 * The indicator below was copied from the MFSWorkUnitPNSNDialog */
		if(null == INSTANCE.getMyBCIndicatorValue())
		{
			// Setup barcode indicator values
			MFSBCIndicatorValue bcIndVal = new MFSBCIndicatorValue();
			bcIndVal.setPNRI("1"); //$NON-NLS-1$

			String sni = MFSConfig.getInstance().getConfigValue("DECODESN"); //$NON-NLS-1$
			if (!sni.equals(MFSConfig.NOT_FOUND))
			{
				bcIndVal.setCSNI(sni);
			}
			else
			{
				bcIndVal.setCSNI("1"); //$NON-NLS-1$
			}
			
			INSTANCE.setMyBCIndicatorValue(bcIndVal);
		}
		
		// Initialize the MFSBCPartObject when needed.
		if(null == INSTANCE.getBCMyPartObject())
		{
			INSTANCE.setMyBCPartObject(new MFSBCPartObject());
		}
	}

	//~3A
	/**
	 * Returns <code>true</code> iff the decoder is not processing a request.
	 * @return <code>true</code> iff the decoder is not processing a request
	 */
	public boolean isRequestComplete()
	{
		synchronized (this.lock)
		{
			return (this.processingBarcode == false);
		}
	}

	//~3A
	/**
	 * Returns the sole instance of <code>MFSBCBarcodeDecoder</code>. Used by
	 * Java serialization; ensures only one instance of
	 * <code>MFSBCBarcodeDecoder</code> exists.
	 * @return the sole instance of <code>MFSBCBarcodeDecoder</code>
	 */
	protected Object readResolve()
	{
		return INSTANCE;
	}
		
	//~3C Use methods local to MFSBCBarcodeDecoder instead of ThreadFlag
	/** Performs barcode decoding. */
	public void run()
	{
		while (true)
		{
			waitForRequest();
			if (isRequestComplete() == false)
			{
				decodeBarcode(this.myBarcodeInput, this.myBCIndicatorValue);
				setRequestComplete();
			}
		}
	}
	/**
	 * Sets the barcode input to decode.
	 * @param input the barcode input
	 */
	public void setMyBarcodeInput(String input) 
	{
		char[] holder = input.toCharArray();
		StringBuffer noBlanks = new StringBuffer();
		for (int i=0; i<input.length(); i++)
		{
			if (holder[i] != ' ')
			{
				noBlanks.append(holder[i]);	
			}
		}		
		this.myBarcodeInput = noBlanks.toString();
	}
	
	/**
	 * Sets the <code>BCIndicatorValue</code> used for decoding barcodes.
	 * @param indicatorValue the <code>BCIndicatorValue</code>
	 */
	public void setMyBCIndicatorValue(MFSBCIndicatorValue indicatorValue) 
	{
		this.myBCIndicatorValue = indicatorValue;
	}
	
	/**
	 * Sets the <code>BCPartObject</code> used for decoding barcodes.
	 * @param partObject the <code>BCPartObject</code>
	 */
	public void setMyBCPartObject(MFSBCPartObject partObject) 
	{
		this.myBCPartObject = partObject;
	}

	//~3A
	/** Signals the completion of a decode request. */
	void setRequestComplete()
	{
		synchronized (this.lock)
		{
			this.processingBarcode = false;
			this.lock.notify();
		}
	}

	//~3A
	/** Submits a decode request. */
	public void submitRequest()
	{
		synchronized (this.lock)
		{
			this.processingBarcode = true;
			this.lock.notify();
		}
	}
	
	private void translateHDRCode()
	{ 
		String scannedItem = new String();

//		**********************
//		Logic for 'SN'
//		if there is a Part Number > 'scannedItem' is a Part Numeber (PN)
		if (myBCPartObject.getPN() != "") //$NON-NLS-1$
//		if 1st character of 'Sn' = 'Y' 
//			if (myBCPartObject.getSN().charAt(0) =='Y')	//CHANGED!arranged with Nathan that not do it this way				
		{
			//scanned item  equals decoded Part Number (PN)
			scannedItem = myBCPartObject.getPN();	
		}
			else
			//if there is no Part Number > 'scannedItem' passed is blank
			{
			//if there is a Part Number (PN) and the 1st char of SN is not'Y'
				//pass in blanks
				scannedItem = "            "; //$NON-NLS-1$
			}		
			// Now we want to call go to the 400 if the ECRI or COOI is set or not			
			if ((!getMyBCIndicatorValue().getECRI().equals(" ") && //$NON-NLS-1$
				 !getMyBCIndicatorValue().getECRI().equals("0"))|| //$NON-NLS-1$
				(!getMyBCIndicatorValue().getCCAI().equals(" ")&& //$NON-NLS-1$
				 !getMyBCIndicatorValue().getCCAI().equals("0"))|| //$NON-NLS-1$
				(!getMyBCIndicatorValue().getCOOI().equals(" ")&& //$NON-NLS-1$
				 !getMyBCIndicatorValue().getCOOI().equals("0"))) //$NON-NLS-1$
			{
				hdr_codeConvert(scannedItem);
			}
		}
	

	/**
	 * Validates a decoded value. This method considers a value valid if it
	 * consists soley of characters, numbers, and blanks.
	 * @param decodedValue the value to test for validity
	 * @param typeOfValue the type of value (used in the error string)
	 */
	public String validateDecodedValue(String decodedValue, String typeOfValue) 
	{
		String retString = GOOD_VALUE;

		int char_index = 0;
		char [] tempSn = decodedValue.toCharArray();
		boolean invalidChar = false;
		
		while(char_index < decodedValue.length() && !invalidChar)
		{
			if(!java.lang.Character.isLetterOrDigit(tempSn[char_index]) && tempSn[char_index] != ' ')
				invalidChar = true;
			else
				char_index++;
		}
		
		if(invalidChar)
			retString = "INVALID BARCODE - Invalid Character = " + tempSn[char_index] + " Found in " + typeOfValue; //$NON-NLS-1$ //$NON-NLS-2$
		
		return retString;
	}

	//~3A
	/** Blocks until a decode request is received. */
	void waitForRequest()
	{
		synchronized (this.lock)
		{
			if (this.processingBarcode == false)
			{
				try
				{
					this.lock.wait();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
	}

	//~3A
	/** Blocks until the current decode request completes. */
	void waitForRequestCompletion()
	{
		synchronized (this.lock)
		{
			if (this.processingBarcode == true)
			{
				try
				{
					this.lock.wait();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
	}
}