/* © Copyright IBM Corporation 2006, 2007. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2006-06-28      31801JM  R Prechel        -Initial version
 * 2007-02-16      34242JR  R Prechel        -Java 5 version
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.struct;

import java.io.Serializable;

/**
 * <code>MFSIntStringPair</code> is a pure data structure consisting of an
 * <code>int</code> and a <code>String</code>. A common use of an
 * <code>MFSIntStringPair</code> is as a return <code>Object</code> from a
 * method that needs to return both a return code and an error message.
 * @author The MFS Client Development Team
 */
public class MFSIntStringPair
	implements Serializable
{
	/** Identifies the original class version for which this class 
	 * is capable of writing streams and from which it can read. */
	private static final long serialVersionUID = 1L;
	
	/** The <code>int</code>. */
	public int fieldInt;
	
	/** The <code>String</code>. */
	public String fieldString;
	
	/**
	 * Constructs a new <code>MFSIntStringPair</code>.
	 * @param i the initial value of the <code>int</code>
	 * @param s the initial value of the <code>String</code>
	 */
	public MFSIntStringPair(int i, String s)
	{
		this.fieldInt = i;
		this.fieldString = s;
	}
}
