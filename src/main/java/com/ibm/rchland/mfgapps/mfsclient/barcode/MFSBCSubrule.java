/* @ Copyright IBM Corporation 2005, 2007. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2007-01-15   ~1 34242JR  R Prechel        -Java 5 version
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.barcode;

/**
 * <code>BCSubrule</code> is a data structure that contains information for a
 * single barcode subrule. Barcode subrules are used to determine how to process
 * parts of a barcode. A barcode rule has three subrules which are labeled A, B,
 * and C, but a barcode rule may not use all three subrules. A barcode rule will
 * either use an A subrule, an A and a B subrule, or an A, B, and C subrule,
 * depending on the amount of information encoded in the barcode.
 * <p>
 * Each subrule specifies the starting position, length, and type of information
 * for part of a barcode. In addition, if the subrule specifies a substring and
 * a substring match occurs, the substring will be replaced with the substring
 * return value.
 * @author The MFS Client Development Team
 */
public class MFSBCSubrule
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

	/** Starting Position */
	protected int str;

	/** Length */
	protected int len;

	/** Type */
	protected char tpe;

	/** Substring */
	protected String sub;

	/** Substring Position */
	protected int sbp;

	/** Substring Length */
	protected int sln;

	/** Substring Return Value */
	protected String rtv;

	/**
	 * Constructs a new <code>BCSubrule</code> from <code>data</code>.
	 * <code>data</code> must start with the 28 character long subrule data as
	 * returned by the RTV_BC transaction.
	 * <ol>
	 * <li>2 characters - Starting Position</li>
	 * <li>2 characters - Length</li>
	 * <li>1 character - Type</li>
	 * <li>8 characters - Substring</li>
	 * <li>2 characters - Substring Position</li>
	 * <li>1 character - Substring Length</li>
	 * <li>12 characters - Substring Return Value</li>
	 * </ol>
	 * @param data the data used to construct the <code>BCSubrule</code>
	 */
	public MFSBCSubrule(String data)
	{
		//~1 Moved logic here from MFSBCBarcodeDecoder
		
		this.str = Integer.parseInt(data.substring(0, 2).trim());
		this.len = Integer.parseInt(data.substring(2, 4).trim());
		this.tpe = data.charAt(4);
		this.sub = data.substring(5, 13);
		this.sbp = Integer.parseInt(data.substring(13, 15).trim());
		this.sln = Integer.parseInt(data.substring(15, 16).trim());
		this.rtv = data.substring(16, 28);
	}
}
