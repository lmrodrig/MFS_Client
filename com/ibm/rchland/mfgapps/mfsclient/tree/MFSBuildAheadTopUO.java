/* © Copyright IBM Corporation 2009. All rights reserved.
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
 * <code>MFSBuildAheadTopUO</code> is the <code>MFSBuildAheadUO</code>
 * for the top level of the Build Ahead tree
 * @author The MFS Client Development Team
 */
public class MFSBuildAheadTopUO
	extends MFSBuildAheadUO
{
	/**
	 * Constructs a new <code>MFSBuildAheadTopUO</code>.
	 */
	public MFSBuildAheadTopUO()
	{
        super();
	}

    /** {@inheritDoc} */
    protected String createString(String type)
    {
        StringBuffer buffer = new StringBuffer(60);
        buffer.append("Ship Entities");
        
        return buffer.toString();
    }
}
