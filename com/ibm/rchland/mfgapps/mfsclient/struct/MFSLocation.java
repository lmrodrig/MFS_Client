/* © Copyright IBM Corporation 2008, 2010. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * Jan 25, 2008    37616JL  D Pietrasik      -Initial version
 * 2010-03-08 ~01  47595MZ	Ray Perry 		 -Shenzhen efficiencies
 *                          Toribio H.
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.struct;

/**
 * The MFSLocation class just holds the area, location, and description
 * information values together
 * @author pietrasi
 * @date Dec 30, 2007
 */
public class MFSLocation
{
	/** area */
	String area = "";

	/** location */
	String location = "";

	/** description */
	String desc = "";

	/** ship entity */
	String shpn = "";

	/**
	 * default constructor, keep it all blank
	 */
	public MFSLocation()
	{
		super();
	}

	/**
	 * Constructor when you have values
	 * @param a area
	 * @param l location
	 * @param d description
	 */
	public MFSLocation(String a, String l, String d, String s)
	{
		this.area = a;
		this.location = l;
		this.desc = d;
		this.shpn = s;
	}

	/**
	 * Compares MFSLocation objects based on area and location
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object anotherLoc)
	{
		boolean retval = true;

		if (anotherLoc == null)
		{
			return false;
		}

		if ((this.area == null) || (this.location == null))
		{
			retval = false;
		}
		retval = retval && this.area.equals(((MFSLocation) anotherLoc).area);
		retval = retval && this.location.equals(((MFSLocation) anotherLoc).location);
		retval = retval && this.shpn.equals(((MFSLocation) anotherLoc).shpn);

		return retval;
	}

	/**
	 * For displaying the info in a list
	 * @return string format of location info, 10 char for area, 10 for
	 *         location, then description
	 */
	public String toString()
	{
		StringBuffer retval = new StringBuffer("                                                      "); //$NON-NLS-1$
		retval.replace(0, this.area.length(), this.area);
		retval.replace(6, 6 + this.location.length(), this.location);
		retval.replace(16, 16 + this.desc.length(), this.desc);
		retval.append("  ");
		retval.append(this.shpn);

		return retval.toString();
	}

	/**
	 * Returns the value of the area property.
	 * @return the value of the area property
	 */
	public String getArea()
	{
		return this.area;
	}

	/**
	 * Returns the value of the desc property.
	 * @return the value of the desc property
	 */
	public String getDesc()
	{
		return this.desc;
	}

	/**
	 * Returns the value of the location property.
	 * @return the value of the location property
	 */
	public String getLocation()
	{
		return this.location;
	}

	/**
	 * Returns the value of the ship entity property.
	 * @return the value of the hip entity property
	 */
	public String getShpn()
	{
		return this.shpn;
	}
	/** ~01
	 * Sets the value of the ship entity property.
	 * @param the value of the hip entity property
	 */
	public void setShpn(String shpn)
	{
		this.shpn = shpn;
	}
}