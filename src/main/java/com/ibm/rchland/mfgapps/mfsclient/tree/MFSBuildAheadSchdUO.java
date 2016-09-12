/* @ Copyright IBM Corporation 2009. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2009-08-29      37550JL  Ray Perry        -Initial version
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.tree;

/**
 * <code>MFSBuildAheadCnfgUO</code> is the <code>MFSBuildAheadUO</code>
 * for a Build Ahead configuration (ITEM)
 * @author The MFS Client Development Team
 */
public class MFSBuildAheadSchdUO
	extends MFSBuildAheadUO
{
    String schd = "";

	/**
	 * Constructs a new <code>MFSBuildAheadCnfgUO</code>.
	 * @param item the configuration item
	 */
	public MFSBuildAheadSchdUO(String schd)
	{
		super();
        this.schd = schd;
	}

	/** {@inheritDoc} */
	protected String createString(String type)
	{
		StringBuffer buffer = new StringBuffer(100);
        
        buffer.append(bundle.getString(type + "SCHD")); //$NON-NLS-1$
        buffer.append(this.schd);
        
		return buffer.toString();
	}

    public void setSchd(String schd)
    {
        this.schd = schd;
    }

    public String getSchd()
    {
        return this.schd;
    }
}
