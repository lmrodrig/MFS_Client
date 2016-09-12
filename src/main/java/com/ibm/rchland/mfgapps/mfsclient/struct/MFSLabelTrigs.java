/* @ Copyright IBM Corporation 2010. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2010-01-18      42558JL  D Mathre          -Initial version
 * 2010-03-04      42558JL  Santiago SC       -cleanup , Java 5.0 version
 *******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.struct;

import java.util.ArrayList;

/**
 * <code>MFSLabelTrigs</code> is a data structure for storing trigger source, label and data values,
  * @author The MFS Client Development Team
 */
public class MFSLabelTrigs
{
	/** The label trigger source. */
	private String source;

	/** The label type. */
	private ArrayList<String> labelTypes;

	/** The label trigger key. */
	private String keyElement;
	
	/** The key element data. */
	private String elementData;

	/**
	 * @param labelType the labelType to add
	 */
	public void addLabelType(String labelType) {
		this.labelTypes.add(labelType);
	}

	/**
	 * @return the elementData
	 */
	public String getElementData() {
		return elementData;
	}

	/**
	 * @return the keyElement
	 */
	public String getKeyElement() {
		return keyElement;
	}

	/**
	 * @return the labelTypes
	 */
	public ArrayList<String> getLabelTypes() {
		return labelTypes;
	}
	
	/**
	 * @return the source
	 */
	public String getSource() {
		return source;
	}

	/**
	 * @param elementData the elementData to set
	 */
	public void setElementData(String elementData) {
		this.elementData = elementData;
	}

	/**
	 * @param keyElement the keyElement to set
	 */
	public void setKeyElement(String keyElement) {
		this.keyElement = keyElement;
	}

	/**
	 * @param labelTypes the labelTypes to set
	 */
	public void setLabelTypes(ArrayList<String> labelTypes) {
		this.labelTypes = labelTypes;
	}

	/**
	 * @param source the source to set
	 */
	public void setSource(String source) {
		this.source = source;
	}
	

}

