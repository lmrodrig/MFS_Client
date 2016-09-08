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

import java.util.Hashtable;

import com.ibm.rchland.mfgapps.client.utils.io.IGSPad;

/**
 * <code>MFSAssemblyTreeUserObject</code> is the <code>MFSUserObject</code>
 * for an assembly tree node.
 * @author The MFS Client Development Team
 */
public class MFSAssemblyTreeUserObject
	implements MFSUserObject
{

	/** The QD10 data for this node. */
	@SuppressWarnings("rawtypes")
	private Hashtable qd10Data = new Hashtable();

	/** The cached <code>String</code> representation of this node. */
	private String string = null;

	/** Constructs a new <code>MFSAssemblyTreeUserObject</code>. */
	public MFSAssemblyTreeUserObject()
	{
		super();
	}

	/**
	 * Adds the specified <code>elementData</code> for the XML element with
	 * the specified <code>elementName</code> to this
	 * <code>MFSUserObject</code>.
	 * @param elementName the name of the XML element
	 * @param elementData the data for the XML element
	 */
	@SuppressWarnings("unchecked")
	public void add(String elementName, String elementData)
	{
		this.qd10Data.put(elementName, elementData);
		//New information was added
		//Discard the cached String representation
		this.string = null;
	}

	/**
	 * Returns the QD10 data for this node
	 * @return the QD10 data for this node
	 */
	@SuppressWarnings("rawtypes")
	public Hashtable getQD10Data()
	{
		return this.qd10Data;
	}

	/**
	 * Returns the <code>String</code> representation of this
	 * <code>MFSAssemblyTreeUserObject</code>.
	 * @return the <code>String</code> representation
	 */
	public String toString()
	{
		// If toString was already called, and no new information was added
		// (i.e., the add method was not called), return the cached String
		// Otherwise (this.string == null), build the String
		if (this.string == null)
		{
			StringBuffer result = new StringBuffer();
			Object value = null;

			if ((value = this.qd10Data.get("MFGN")) != null //$NON-NLS-1$
					&& value.toString().trim().length() > 0)
			{
				result.append("MFGN=");
				result.append(value);
				result.append("; IDSS=");
				result.append(this.qd10Data.get("IDSS")); //$NON-NLS-1$
			}
			else
			{
				Object qdpn = this.qd10Data.get("QDPN"); //$NON-NLS-1$
				Object qdsq = this.qd10Data.get("QDSQ"); //$NON-NLS-1$

				if (qdpn != null && qdsq != null && qdpn.toString().trim().length() > 0
						&& qdsq.toString().trim().length() > 0)
				{
					result.append("PN=");
					result.append(qdpn);
					result.append("; SN=");
					result.append(IGSPad.pad(qdsq.toString(), 12));

					if ((value = this.qd10Data.get("CRMCTL")) != null //$NON-NLS-1$
							&& value.toString().trim().length() > 0)
					{
						result.append("; MCTL=");
						result.append(value);
					}
					else if ((value = this.qd10Data.get("MCTL")) != null) //$NON-NLS-1$
					{
						result.append("; MCTL=");
						result.append(value);
					}

					if ((value = this.qd10Data.get("CCIN")) != null) //$NON-NLS-1$
					{
						result.append("; CCIN=");
						result.append(value);
					}
					if ((value = this.qd10Data.get("CTLV")) != null) //$NON-NLS-1$
					{
						result.append("; TSLV=");
						result.append(value);
					}
				}
				else
				{
					result.append("MCTL=");
					result.append(this.qd10Data.get("MCTL")); //$NON-NLS-1$
				}
			}
			this.string = result.toString();
		}
		return this.string;
	}
}
