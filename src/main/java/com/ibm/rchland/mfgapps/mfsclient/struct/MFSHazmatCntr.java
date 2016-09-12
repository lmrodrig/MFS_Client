/* @ Copyright IBM Corporation 2010. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2010-09-06       48749JL Santiago SC      -Initial version, Java 5.0
 ******************************************************************************/

package com.ibm.rchland.mfgapps.mfsclient.struct;

import java.util.Map;

/**
 * The <code>MFSHazmatCntr</code> is cntr that holds parts labeled as hazardous materials.
 */
public class MFSHazmatCntr
{
	/** The cntr number or mctl number in case of subassemly */
	private String cntr;
	
	/** The is subasembly variable */
	private boolean isFFBM;
	
	/** A map of label part numbers */
	private Map<String, MFSHazmatPn> hazmatPns;
	
	/** 
	 * Creates a new <code>MFSHazamtCntr</code> with the given cntr number
	 * @param cntr the cntr number
	 */
	public MFSHazmatCntr(String cntr) 
	{
		this.cntr = cntr;
	}
	
	/**
	 * Creates a new <code>MFSHazamtCntr</code> with the given cntr number and a map
	 * of label part numbers <code>MFSHazmatPn</code>
	 * @param cntr the cntr number
	 * @param hazmatPns
	 */
	public MFSHazmatCntr(String cntr, Map<String, MFSHazmatPn> hazmatPns)
	{
		this.cntr = cntr;
		this.hazmatPns = hazmatPns;
	}

	/**
	 * @return the cntr
	 */
	public String getCntr() {
		return cntr;
	}

	/**
	 * @return the hazmatPns
	 */
	public Map<String, MFSHazmatPn> getHazmatPns() {
		return hazmatPns;
	}

	/**
	 * @return the isFFBM
	 */
	public boolean isFFBM() {
		return isFFBM;
	}

	/**
	 * @param cntr the cntr to set
	 */
	public void setCntr(String cntr) {
		this.cntr = cntr;
	}

	/**
	 * @param isFFBM the isFFBM to set
	 */
	public void setFFBM(boolean isFFBM) {
		this.isFFBM = isFFBM;
	}

	/**
	 * @param hazmatPns the hazmatPns to set
	 */
	public void setHazmatPns(Map<String, MFSHazmatPn> hazmatPns) {
		this.hazmatPns = hazmatPns;
	}
	
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder(getCntr());
		sb.append(",");
		sb.append(isFFBM());
		sb.append(",PNs:");
		sb.append(hazmatPns);
		return sb.toString();
	}

}
