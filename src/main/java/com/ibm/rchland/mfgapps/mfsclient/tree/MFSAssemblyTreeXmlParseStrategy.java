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

import java.util.Hashtable;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * <code>MFSAssemblyTreeXmlParseStrategy</code> determines the names of the
 * root node, child node, and leaf node XML elements and creates the user
 * objects used by the root node, child nodes, and leaf nodes of an XML based
 * assembly tree.
 * @author The MFS Client Development Team
 */
public class MFSAssemblyTreeXmlParseStrategy
	implements MFSXmlParseStrategy
{
	/** The name of the root, child, and leaf XML elements. */
	private static final String NODE_NAME = "PART"; //$NON-NLS-1$

	/** The part number of the initially selected <code>TreeNode</code>. */
	private String pn;

	/** The sequence number of the initially selected <code>TreeNode</code>. */
	private String sn;

	/**
	 * Constructs a new <code>MFSAssemblyTreeXmlParseStrategy</code>.
	 * @param pn the part number of the initially selected <code>TreeNode</code>
	 * @param sn the sequence number of the initially selected
	 *        <code>TreeNode</code>
	 */
	public MFSAssemblyTreeXmlParseStrategy(String pn, String sn)
	{
		this.pn = pn;
		this.sn = sn;
	}

	/**
	 * Returns the name of the root XML element.
	 * @return {@link #NODE_NAME}
	 */
	public String getRootElementName()
	{
		return NODE_NAME;
	}

	/**
	 * Returns the name of a child XML element.
	 * @return {@link #NODE_NAME}
	 */
	public String getChildElementName()
	{
		return NODE_NAME;
	}

	/**
	 * Returns the name of a leaf XML element.
	 * @return {@link #NODE_NAME}
	 */
	public String getLeafElementName()
	{
		return NODE_NAME;
	}

	/**
	 * Creates the <code>MFSUserObject</code> that will become the user object
	 * of the root tree node.
	 * @return a new {@link MFSAssemblyTreeUserObject}
	 */
	public MFSUserObject createRootNodeUserObject()
	{
		return new MFSAssemblyTreeUserObject();
	}

	/**
	 * Creates the <code>MFSUserObject</code> that will become the user object
	 * of a child tree node.
	 * @return a new {@link MFSAssemblyTreeUserObject}
	 */
	public MFSUserObject createChildNodeUserObject()
	{
		return new MFSAssemblyTreeUserObject();
	}

	/**
	 * Creates the <code>MFSUserObject</code> that will become the user object
	 * of a leaf tree node.
	 * @return a new {@link MFSAssemblyTreeUserObject}
	 */
	public MFSUserObject createLeafNodeUserObject()
	{
		return new MFSAssemblyTreeUserObject();
	}

	/** {@inheritDoc} */
	@SuppressWarnings("rawtypes")
	public boolean isSelectedNode(DefaultMutableTreeNode node)
	{
		boolean result = false;
		Object obj = node.getUserObject();
		if (obj instanceof MFSAssemblyTreeUserObject)
		{
			Hashtable data = ((MFSAssemblyTreeUserObject) obj).getQD10Data();
			result = this.pn.equals(data.get("QDPN")) //$NON-NLS-1$
					&& this.sn.equals(data.get("QDSQ")); //$NON-NLS-1$
		}
		return result;
	}
}
