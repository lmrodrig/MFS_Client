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
 * <code>MFSBuildAheadMfgnUO</code> is the <code>MFSBuildAheadUO</code>
 * for a manufacturing control number (MFGN) node.
 * @author The MFS Client Development Team
 */
public class MFSBuildAheadMfgnUO
	extends MFSBuildAheadUO
{
	/** The manufacturing control number. */
	String MFGN = null;

	/** Released. */
	String RLSED = null;

	/**
	 * Constructs a new <code>MFSBuildAheadMfgnUO</code>.
	 * @param mfgn the manufacturing control number
	 */
	public MFSBuildAheadMfgnUO(String mfgn)
	{
		super();
		this.MFGN = mfgn;
	}

	/** {@inheritDoc} */
	protected String createString(String type)
	{
		StringBuffer buffer = new StringBuffer(100);
		buffer.append(bundle.getString(type + "MFGN")); //$NON-NLS-1$
		buffer.append(this.MFGN);
		
		return buffer.toString();
	}

//    /** {@inheritDoc} */
//    public Color getColor()
//    {
//        if (this.RLSED.equals("N"))
//        {
//            return Color.BLUE;
//        }
//        else 
//        {
//            return Color.BLACK;
//        }
//    }

    /**
     * Sets the mfgns rlsed flag.
     * @param rlsed the mfgns rlsed flag.
     */
    public void setRlsed(String rlsed)
    {
        this.RLSED = rlsed;
        clearCachedStrings();
    }

    /**
     * Returns the mfgn.
     * @return the mfgn.
     */
    public String getMfgn()
    {
        return this.MFGN;
    }

    /**
     * Returns the released flag.
     * @return the released flag.
     */
    public String getRlsed()
    {
        return this.RLSED;
    }
}
