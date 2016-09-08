/* © Copyright IBM Corporation 2005, 2010. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2007-01-15   ~1 34242JR  R Prechel        -Java 5 version
 * 2010-11-03  ~02 49513JM	Toribio H. 		-Add Trim when setting the BC Rules (bcai field).
 *                                           It was done everywhere it was used.
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.barcode;

/**
 * <code>BCRule</code> is a data structure that contains information for a
 * single barcode rule. Barcode rules are used to determine how to process a
 * barcode. A barcode rule consists of several header fields and three barcode
 * subrules. The subrules are stored as an array of <code>BCSubrule</code>s.
 * @author The MFS Client Development Team
 */
public class MFSBCRule
{
	/*
	 * The barcode rules are retrieved from the server via the RTV_BC
	 * transaction. The RTV_BC transaction returns all of the barcode rules as a
	 * single String and the barcode rules are parsed sequentially from this
	 * String. Each barcode rule is 128 characters long (38 header + 28 * 3
	 * subrule + 6 padding). The various fields of each rule and its subrules
	 * are parsed from this String based on position, with the integer fields
	 * converted using the Integer.parseInt() method.
	 */
	
	/** Barcode Sequence */
	protected int bcsq;

	/** Barcode Sort */
	protected int bsrt;

	/** Barcode AI Code */
	protected String bcai;

	/** AI Code Length */
	protected int ailn;

	/** Maximum Barcode Length */
	protected int bmax;

	/** A barcode rule has three subrules: A, B, and C. */
	protected static final int SUBRULES_QTY = 3;

	/** The array of subrules. */
	protected MFSBCSubrule[] subrulesArray = new MFSBCSubrule[SUBRULES_QTY];

	/**
	 * Constructs a new <code>BCRule</code> from <code>data</code>.
	 * <code>data</code> must start with the 128 character long rule data as
	 * returned by the RTV_BC transaction.
	 * <ol>
	 * <li>5 characters - Barcode Sequence</li>
	 * <li>5 characters - Barcode Sort</li>
	 * <li>24 characters - Barcode AI Code</li>
	 * <li>2 characters - AI Code Length</li>
	 * <li>2 characters - Maximum Barcode Length</li>
	 * <li>28 characters - Subrule A</li>
	 * <li>28 characters - Subrule B</li>
	 * <li>28 characters - Subrule C</li>
	 * <li>6 characters - padding</li>
	 * </ol>
	 * @param data the data used to construct the <code>BCRule</code>
	 */
	public MFSBCRule(String data)
	{
		//~1 Moved logic here from MFSBCBarcodeDecoder
		
		// bcsq and bsrt are not used by the MFS Client.
		// Do not waste time parsing them
		//this.bcsq = Integer.parseInt(data.substring(0, 5).trim());
		//this.bsrt = Integer.parseInt(data.substring(5, 10).trim());
		this.bcai = data.substring(10, 34).trim();
		this.ailn = Integer.parseInt(data.substring(34, 36).trim());
		this.bmax = Integer.parseInt(data.substring(36, 38).trim());
		this.subrulesArray[0] = new MFSBCSubrule(data.substring(38, 66));
		this.subrulesArray[1] = new MFSBCSubrule(data.substring(66, 94));
		this.subrulesArray[2] = new MFSBCSubrule(data.substring(94, 122));
		//The last 6 characters are padding --> ignore
	}
}