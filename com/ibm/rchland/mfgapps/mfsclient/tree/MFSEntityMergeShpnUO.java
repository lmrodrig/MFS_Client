/* © Copyright IBM Corporation 2008, 2011. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR    Name             Details
 * ---------- ---- --------    ---------------- ----------------------------------
 * 2008-03-15      37616JL     R Prechel        -Initial version
 * 2011-10-01  ~01 RCQ00177780 VH Avila         -Added new setSchd() method
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.tree;

/**
 * <code>MFSEntityMergeShpnUO</code> is the <code>MFSEntityMergeUO</code>
 * for a ship entity (SHPN) node.
 * @author The MFS Client Development Team
 */
public class MFSEntityMergeShpnUO
	extends MFSEntityMergeUO
{
	/** The ship entity number. */
	String shpn = null;

	/** The selected container. */
	String sctr = null;

	/** The scheduled ship date. */
	String schd = null;

	/**
	 * Constructs a new <code>MFSEntityMergeShpnUO</code>.
	 * @param shpn the ship entity number
	 */
	public MFSEntityMergeShpnUO(String shpn)
	{
		super();
		this.shpn = shpn;
	}

	/** {@inheritDoc} */
	protected String createString(String type)
	{
		StringBuffer buffer = new StringBuffer(50);
		String separator = bundle.getString(type + "Separator"); //$NON-NLS-1$
		buffer.append(bundle.getString(type + "SHPN")); //$NON-NLS-1$
		buffer.append(this.shpn);
		buffer.append(separator);
		buffer.append(bundle.getString(type + "SCHD")); //$NON-NLS-1$
		buffer.append(this.schd);
		return buffer.toString();
	}

	/**
	 * Returns the ship entity number.
	 * @return the ship entity number
	 */
	public String getShpn()
	{
		return this.shpn;
	}

	/**
	 * Returns the selected container.
	 * @return the selected container
	 */
	public String getSctr()
	{
		return this.sctr;
	}

	/**
	 * Returns the scheduled ship date.
	 * @return the scheduled ship date
	 */
	public String getSchd()
	{
		return this.schd;
	}
	
	/**
	 * @return the scheduled ship date ~01A
	 */
	public void setSchd(String schd)
	{
		this.schd = schd;
	}
}
