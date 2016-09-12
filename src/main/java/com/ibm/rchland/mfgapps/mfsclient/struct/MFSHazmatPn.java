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

/**
 * The <code>MFSHazmatPn</code> holds information for a specific label part number
 * for hazardous materials.
 */
public class MFSHazmatPn 
{
	/** The label part number */
	private String pn;	
	
	/** Timestamp string when the label part numbe was verified */
	private String verifiedDate = "";
	
	/** Verification status, default is 0 = verification required */
	private int status;
	
	/** True if he part number was updated */
	private boolean changed;

	/**
	 * @return the pn
	 */
	public String getPn() {
		return pn;
	}

	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @return the verifiedDate
	 */
	public String getVerifiedDate() {
		return verifiedDate;
	}

	/**
	 * @return the changed
	 */
	public boolean hasChanged() {
		return changed;
	}

	/**
	 * @param changed the changed to set
	 */
	public void setChanged(boolean changed) {
		this.changed = changed;
	}

	/**
	 * @param pn the pn to set
	 */
	public void setPn(String pn) {
		this.pn = pn;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * @param verifiedDate the verifiedDate to set
	 */
	public void setVerifiedDate(String verifiedDate) {
		this.verifiedDate = verifiedDate;
	}
	
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder(getPn());
		sb.append(",");
		sb.append(getVerifiedDate());
		sb.append(",");
		sb.append(getStatus());
		sb.append(",");
		sb.append(hasChanged());
		return sb.toString();
	}
	
}
