/* © Copyright IBM Corporation 2010. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2010-04-05       42558JL Santiago SC      -Initial version, Java 5.0
 ******************************************************************************/

package com.ibm.rchland.mfgapps.mfsclient.struct;

/**
 * <code>MFSOnDemandKeyData</code> stores the key data for on demand label printing.
 * @author The MFS Client Development Team
 */
public class MFSOnDemandKeyData
{
	/** Label Type */
	private String labelType = "";
	 
	/** Label Trigger Source */
	private String triggerSource = "";
	
	/** Label Trigger Key */
	private String triggerKey = "";
	
	/** Label Trigger Value */
	private String triggerValue = "";
	
	/** Label Data */
	private String labelData = "";
	
	/** Plant of Manufacture */
	private String plom = "";
	
	/** Label Data Source */
	private Object dataSource;
	
	/** Print quantity */
	private int qty;

	/**
	 * @return the config entry CONF file
	 */
	public String getConfig()
	{
		return this.labelType + this.triggerSource + this.triggerKey;
	}

	/**
	 * @return the dataSource
	 */
	public Object getDataSource() {
		return dataSource;
	}

	/**
	 * @return the labelData
	 */
	public String getLabelData() {
		return labelData;
	}

	/**
	 * @return the labelType
	 */
	public String getLabelType() {
		return labelType;
	}

	/**
	 * @return the plom
	 */
	public String getPlom() {
		return plom;
	}

	/**
	 * @return the qty
	 */
	public int getQty() {
		return qty;
	}

	/**
	 * @return the triggerKey
	 */
	public String getTriggerKey() {
		return triggerKey;
	}

	/**
	 * @return the triggerSource
	 */
	public String getTriggerSource() {
		return triggerSource;
	}

	/**
	 * @return the triggerValue
	 */
	public String getTriggerValue() {
		return triggerValue;
	}

	/**
	 * @param dataSource the dataSource to set
	 */
	public void setDataSource(Object dataSource) {
		this.dataSource = dataSource;
	}

	/**
	 * @param labelData the labelData to set
	 */
	public void setLabelData(String labelData) {
		this.labelData = labelData;
	}

	/**
	 * @param labelType the labelType to set
	 */
	public void setLabelType(String labelType) {
		this.labelType = labelType;
	}

	/**
	 * @param plom the plom to set
	 */
	public void setPlom(String plom) {
		this.plom = plom;
	}

	/**
	 * @param qty the qty to set
	 */
	public void setQty(int qty) {
		this.qty = qty;
	}

	/**
	 * @param triggerKey the triggerKey to set
	 */
	public void setTriggerKey(String triggerKey) {
		this.triggerKey = triggerKey;
	}

	/**
	 * @param triggerSource the triggerSource to set
	 */
	public void setTriggerSource(String triggerSource) {
		this.triggerSource = triggerSource;
	}
	
	/**
	 * @param triggerValue the triggerValue to set
	 */
	public void setTriggerValue(String triggerValue) {
		this.triggerValue = triggerValue;
	}
}
