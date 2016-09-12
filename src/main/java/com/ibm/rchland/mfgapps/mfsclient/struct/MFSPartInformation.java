/* @ Copyright IBM Corporation 2006. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2006-06-28      31801JM  R Prechel        -Initial version
 * 2007-01-15      34242JR  R Prechel        -Java 5 version
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.struct;

/**
 * <code>PartInformation</code> is a pure data structure for part information.
 * @author The MFS Client Development Team
 */
public class MFSPartInformation
{
	/**
	 * The default value of the {@link MFSPartInformation#ec ec} field when
	 * <code>setECNone</code> is <code>true</code>.
	 */
	public static final String EC_NONE = "*NONE       "; //$NON-NLS-1$

	/** The part number. */
	public String pn = "            "; //$NON-NLS-1$

	/** The EC number. */
	public String ec = "            "; //$NON-NLS-1$

	/** The sequence number. */
	public String sn = "            "; //$NON-NLS-1$

	/** The card assembly number. */
	public String ca = "            "; //$NON-NLS-1$

	/** The machine serial number. */
	public String ms = "       "; //$NON-NLS-1$

	/** The country code. */
	public String co = "  "; //$NON-NLS-1$

	/** The control number. */
	public String wu = "        "; //$NON-NLS-1$

	/** The part quantity. */
	public int qt = 1;

	/** <code>true</code> if the part quantity was explicitly set by the user. */
	public boolean overrideQty = false;

	/**
	 * Constructs a new <code>PartInformation</code>.
	 * @param setECNone <code>true</code> if the default value of the
	 *        {@link #ec ec}field should be {@link #EC_NONE EC_NONE}
	 */
	public MFSPartInformation(boolean setECNone)
	{
		if (setECNone)
		{
			this.ec = EC_NONE;
		}
	}
}
