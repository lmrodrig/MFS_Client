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

import java.awt.Color;

/**
 * <code>MFSBuildAheadShpnUO</code> is the <code>MFSBuildAheadUO</code>
 * for a ship entity (SHPN) node.
 * @author The MFS Client Development Team
 */
public class MFSBuildAheadShpnUO
	extends MFSBuildAheadUO
{
	/** The ship entity number. */
	String SHPN = null;

	/**
	 * The ship entity's plom.
	 */
	String PLOM;

	/**
	 * The ship entity scheduled ship date.
	 */
	String SCHD;
    
    /**
     * The color of the node.
     */
    Color color = Color.BLACK;
    
    /**
     * The number of unreleased MFGNs on this ship entity
     */
    int unreleasedOrderQty = 0;

	/**
	 * Constructs a new <code>MFSBuildAheadShpnUO</code>.
	 * @param shpn the ship entity number
	 */
	public MFSBuildAheadShpnUO(String shpn)
	{
		super();
		this.SHPN = shpn;
	}

	/** {@inheritDoc} */
	protected String createString(String type)
	{
		StringBuffer buffer = new StringBuffer(60);
		buffer.append(bundle.getString(type + "SHPN")); //$NON-NLS-1$
		buffer.append(this.SHPN);

		return buffer.toString();
	}

    /**
     * Returns the ship entity number.
     * @return the ship entity number.
     */
    public String getShpn()
    {
        return this.SHPN;
    }

    /**
     * Set the ship entity number.
     * @param the ship entity number.
     */
    public void setSHPN(String shpn)
    {
        this.SHPN = shpn;
    }

    /**
     * Returns the ship entity plom.
     * @return the ship entity plom.
     */
    public String getPlom()
    {
        return this.PLOM;
    }

    /**
     * Set the ship entity plom.
     * @param the ship entity plom.
     */
    public void setPLOM(String plom)
    {
        this.PLOM = plom;
    }

    /**
     * Returns the ship entity number.
     * @return the ship entity number.
     */
    public String getSchd()
    {
        return this.SCHD;
    }

    /**
     * Set the scheduled ship date.
     * @param the scheduled ship date.
     */
    public void setSCHD(String schd)
    {
        this.SCHD = schd;
    }

    /** {@inheritDoc} */
    public Color getColor()
    {
        return this.color;
    }

    /**
     * Set the node color.
     * @param color the node color.
     */
    public void setColor(Color color)
    {
        this.color = color;
    }

    /**
     * Returns the unreleased order quantity.
     * @return the unreleased order quantity.
     */
    public int getUnreleasedOrderQuantity()
    {
        return this.unreleasedOrderQty;
    }

    /**
     * Set the unreleased order quantity.
     * @param unreleasedOrderQty the unreleased order quantity.
     */
    public void setUnreleasedOrderQty(int unreleasedOrderQty)
    {
        this.unreleasedOrderQty = unreleasedOrderQty;
        clearCachedStrings();
    }
}
