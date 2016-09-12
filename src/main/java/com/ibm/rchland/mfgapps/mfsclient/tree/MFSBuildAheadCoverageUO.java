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
public class MFSBuildAheadCoverageUO
	extends MFSBuildAheadUO
{

    boolean coverage = false;
    boolean isReleased = false;


	/**
	 * Constructs a new <code>MFSBuildAheadCnfgUO</code>.
	 * @param item the configuration item
	 */
	public MFSBuildAheadCoverageUO(boolean coverage, boolean isReleased)
	{
		super();
        this.coverage = coverage;
        this.isReleased = isReleased;
	}

	/** {@inheritDoc} */
	protected String createString(String type)
	{
		StringBuffer buffer = new StringBuffer(100);
        
        if (isReleased)
        {
            buffer.append("Released");
        }
        else 
            if (coverage)
        {
            buffer.append("Coverage");
        }
        else
        {
            buffer.append("No Coverage");
            
        }

		return buffer.toString();
	}
}
