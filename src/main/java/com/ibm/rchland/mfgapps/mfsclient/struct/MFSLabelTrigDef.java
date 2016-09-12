/* @ Copyright IBM Corporation 2010. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2010-01-13      42558JL  D Mathre          -Initial version
 * 2010-03-04      42558JL  Santiago SC       -cleanup , Java 5.0 version
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.struct;

/**
 * <code>MFSLabelTrigDef</code> is a data structure for trigger definitions.
 * @author The MFS Client Development Team
 */
public class MFSLabelTrigDef
{
	/** The label trigger key element. */
	private String keyElement;
	
	/** The label trigger data source. */
	private String dataSource;

	/**
	 * @return the dataSource
	 */
	public String getDataSource() {
		return dataSource;
	}

	/**
	 * @return the keyElement
	 */
	public String getKeyElement() {
		return keyElement;
	}

	/**
	 * @param dataSource the dataSource to set
	 */
	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}

	/**
	 * @param keyElement the keyElement to set
	 */
	public void setKeyElement(String keyElement) {
		this.keyElement = keyElement;
	}

}

