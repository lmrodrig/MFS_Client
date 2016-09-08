/* © Copyright IBM Corporation 2005, 2007. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2007-01-15      34242JR  R Prechel        -Java 5 version
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.struct;

/**
 * <code>MFSCntrNode</code> is the user object data structure for a
 * <code>DefaultMutableTreeNode</code> in the container in container tree.
 * @author The MFS Client Development Team
 * @see com.ibm.rchland.mfgapps.mfsclient.dialog.MFSCntrInCntrDialog
 * @see javax.swing.tree.DefaultMutableTreeNode#DefaultMutableTreeNode(java.lang.Object)
 * @see javax.swing.tree.DefaultMutableTreeNode#getUserObject()
 */
public class MFSCntrNode
{
	/** The cntr value of the parent node. */
	private String fieldParentCntr;

	/** The mctl value of the parent node. */
	private String fieldParentMctl;

	/** The cntr value of this node. */
	private String fieldCntr;

	/** The mctl value of this node. */
	private String fieldMctl;

	/** The orno value of this node. */
	private String fieldOrno;

	/** The wtyp value of this node. */
	private String fieldWtyp;

	/**
	 * Constructs a new <code>MFSCntrNode</code>.
	 * @param data the data used to construct the node
	 */
	public MFSCntrNode(String data)
	{
		this.fieldParentMctl = (data.substring(0, 8));
		this.fieldParentCntr = (data.substring(8, 18));
		this.fieldMctl = (data.substring(18, 26));
		this.fieldCntr = (data.substring(26, 36));
		this.fieldOrno = (data.substring(36, 42));
		this.fieldWtyp = (data.substring(42, 43));
	}

	/**
	 * Gets the cntr property (String) value.
	 * @return The cntr property value.
	 * @see #setCntr
	 */
	public String getCntr()
	{
		return this.fieldCntr;
	}

	/**
	 * Gets the mctl property (String) value.
	 * @return The mctl property value.
	 * @see #setMctl
	 */
	public String getMctl()
	{
		return this.fieldMctl;
	}

	/**
	 * Gets the orno property (String) value.
	 * @return The orno property value.
	 * @see #setOrno
	 */
	public String getOrno()
	{
		return this.fieldOrno;
	}

	/**
	 * Gets the parentCntr property (String) value.
	 * @return The parentCntr property value.
	 * @see #setParentCntr
	 */
	public String getParentCntr()
	{
		return this.fieldParentCntr;
	}

	/**
	 * Gets the parentMctl property (String) value.
	 * @return The parentMctl property value.
	 * @see #setParentMctl
	 */
	public String getParentMctl()
	{
		return this.fieldParentMctl;
	}

	/**
	 * Gets the wtyp property (String) value.
	 * @return The wtyp property value.
	 * @see #setWtyp
	 */
	public String getWtyp()
	{
		return this.fieldWtyp;
	}

	/**
	 * Sets the cntr property (String) value.
	 * @param cntr The new value for the property.
	 * @see #getCntr
	 */
	public void setCntr(String cntr)
	{
		this.fieldCntr = cntr;
	}

	/**
	 * Sets the mctl property (String) value.
	 * @param mctl The new value for the property.
	 * @see #getMctl
	 */
	public void setMctl(String mctl)
	{
		this.fieldMctl = mctl;
	}

	/**
	 * Sets the orno property (String) value.
	 * @param orno The new value for the property.
	 * @see #getOrno
	 */
	public void setOrno(String orno)
	{
		this.fieldOrno = orno;
	}

	/**
	 * Sets the parentCntr property (String) value.
	 * @param parentCntr The new value for the property.
	 * @see #getParentCntr
	 */
	public void setParentCntr(String parentCntr)
	{
		this.fieldParentCntr = parentCntr;
	}

	/**
	 * Sets the parentMctl property (String) value.
	 * @param parentMctl The new value for the property.
	 * @see #getParentMctl
	 */
	public void setParentMctl(String parentMctl)
	{
		this.fieldParentMctl = parentMctl;
	}

	/**
	 * Sets the wtyp property (String) value.
	 * @param wtyp The new value for the property.
	 * @see #getWtyp
	 */
	public void setWtyp(String wtyp)
	{
		this.fieldWtyp = wtyp;
	}

	/**
	 * Returns the <code>String</code> representation of the node.
	 * @return the <code>String</code> representation of the node
	 */
	public String toString()
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append(this.fieldCntr);
		buffer.append("  "); //$NON-NLS-1$
		buffer.append(this.fieldOrno);
		buffer.append("  "); //$NON-NLS-1$
		buffer.append(this.fieldMctl);
		buffer.append("  "); //$NON-NLS-1$
		buffer.append(this.fieldWtyp);
		return buffer.toString();
	}
}
