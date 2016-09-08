/* © Copyright IBM Corporation 2007. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2007-01-17      34242JR  R Prechel        -Initial version
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.tree;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * <code>MFSXmlParseStrategy</code> determines the names of the root node,
 * child node, and leaf node XML elements and creates the user objects used by
 * the root node, child nodes, and leaf nodes of an XML based tree.
 * @author The MFS Client Development Team
 */
public interface MFSXmlParseStrategy
{
	/**
	 * Returns the name of the root XML element.
	 * @return the name of the root XML element
	 */
	public String getRootElementName();

	/**
	 * Returns the name of a child XML element.
	 * @return the name of a child XML element
	 */
	public abstract String getChildElementName();

	/**
	 * Returns the name of a leaf XML element.
	 * @return the name of a leaf XML element
	 */
	public String getLeafElementName();

	/**
	 * Creates the <code>MFSUserObject</code> that will become the user object
	 * of the root tree node. This method must return <code>null</code> if the
	 * user object should be of type <code>String</code>.
	 * @return the <code>MFSUserObject</code> that will become the user object
	 *         of the root tree node or <code>null</code>
	 */
	public MFSUserObject createRootNodeUserObject();

	/**
	 * Creates the <code>MFSUserObject</code> that will become the user object
	 * of a child tree node. This method must return <code>null</code> if the
	 * user object should be of type <code>String</code>.
	 * @return the <code>MFSUserObject</code> that will become the user object
	 *         of a child tree node or <code>null</code>
	 */
	public MFSUserObject createChildNodeUserObject();

	/**
	 * Creates the <code>MFSUserObject</code> that will become the user object
	 * of a leaf tree node. This method must return <code>null</code> if the
	 * user object should be of type <code>String</code>.
	 * @return the <code>MFSUserObject</code> that will become the user object
	 *         of a leaf tree node or <code>null</code>
	 */
	public MFSUserObject createLeafNodeUserObject();

	/**
	 * Returns <code>true</code> iff the specified <code>node</code> should
	 * be the initially selected node.
	 * @param node the <code>DefaultMutableTreeNode</code> to check
	 * @return <code>true</code> iff the specified <code>node</code> should
	 *         be the initially selected node
	 */
	public boolean isSelectedNode(DefaultMutableTreeNode node);

}
