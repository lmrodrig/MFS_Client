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
 * <code>MFSBuildAheadCnfgUO</code> is the <code>MFSBuildAheadUO</code>
 * for a Build Ahead configuration (ITEM)
 * @author The MFS Client Development Team
 */
public class MFSBuildAheadReleasedUO
	extends MFSBuildAheadUO
{
    boolean isReleased = false;


	/**
	 * Constructs a new <code>MFSBuildAheadCnfgUO</code>.
	 * @param item the configuration item
	 */
	public MFSBuildAheadReleasedUO(boolean isReleased)
	{
		super();
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
        {
            buffer.append("Unreleased");
            
        }

		return buffer.toString();
	}

    public void setReleased(boolean isReleased)
    {
        this.isReleased = isReleased;
    }

    public boolean isReleased()
    {
        return this.isReleased;
    }
}
