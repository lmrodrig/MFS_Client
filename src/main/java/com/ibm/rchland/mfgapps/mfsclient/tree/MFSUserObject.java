/* @ Copyright IBM Corporation 2007. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2007-01-17      34242JR  R Prechel        -Initial version
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.tree;

/**
 * <code>MFSUserObject</code> is an interface for non- <code>String</code>
 * classes that serve as the user <code>Object</code> for a tree node.
 * @author The MFS Client Development Team
 */
public interface MFSUserObject
{
	/**
	 * Adds the specified <code>elementData</code> for the XML element with
	 * the specified <code>elementName</code> to this
	 * <code>MFSUserObject</code>.
	 * @param elementName the name of the XML element
	 * @param elementData the data for the XML element
	 */
	public void add(String elementName, String elementData);
}
