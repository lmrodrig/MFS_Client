/* @ Copyright IBM Corporation 2009. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2009-08-29      37550JL  Ray Perry        -Initial version
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.tree;

import java.util.LinkedHashMap;

import javax.swing.tree.DefaultMutableTreeNode;

import org.xml.sax.Attributes;

import com.ibm.rchland.mfgapps.client.utils.xml.IGSUserObject;
import com.ibm.rchland.mfgapps.client.utils.xml.IGSXMLTreeParseStrategy;

/**
 * <code>MFSBuildAheadParseStrategy</code> is the
 * <code>IGSXMLTreeParseStrategy</code> for a Build Ahead tree.
 * @author The MFS Client Development Team
 */
public class MFSBuildAheadParseStrategy
	implements IGSXMLTreeParseStrategy
{
	/** The {@link MFSBuildAheadShpnUO}s for the tree. */
	private LinkedHashMap<String, MFSBuildAheadUO> shipEntities = new LinkedHashMap<String, MFSBuildAheadUO>();

	/** Constructs a new <code>MFSBuildAheadParseStrategy</code>. */
	public MFSBuildAheadParseStrategy()
	{
		super();
	}

	/** {@inheritDoc} */
	public boolean isTreeNode(String element)
	{
        if ("ENTITIES".equals(element)) //$NON-NLS-1$
        {
            return true;
        }
		if ("ENTITY".equals(element)) //$NON-NLS-1$
		{
			return true;
		}
		else if ("CONFIG".equals(element)) //$NON-NLS-1$
		{
			return true;
		}
		else if ("SMFGN".equals(element)) //$NON-NLS-1$
		{
			return true;
		}
		return false;
	}

	/** {@inheritDoc} */
	public IGSUserObject createUserObject(String element, Attributes attr)
	{
        MFSBuildAheadUO result = null;
        if ("ENTITIES".equals(element)) //$NON-NLS-1$
        {
            /* Starting a new tree; clear containers from previous tree. */
            this.shipEntities.clear();
            result = new MFSBuildAheadTopUO();
        }
        else if ("ENTITY".equals(element)) //$NON-NLS-1$
		{
			String shpn = attr.getValue("SHPN"); //$NON-NLS-1$
			result = (MFSBuildAheadUO) this.shipEntities.get(shpn);
			if (result == null)
			{
				result = new MFSBuildAheadShpnUO(shpn);
				this.shipEntities.put(shpn, result);
			}
		}
		else if ("CONFIG".equals(element)) //$NON-NLS-1$
		{
			String item = attr.getValue("ITEM"); //$NON-NLS-1$
			result = new MFSBuildAheadCnfgUO(item);
		}
		else if ("SMFGN".equals(element)) //$NON-NLS-1$
		{
			String mfgn = attr.getValue("MFGN"); //$NON-NLS-1$
			result = new MFSBuildAheadMfgnUO(mfgn);
		}
		return result;
	}

	/** {@inheritDoc} */
	public boolean isSelectedNode(DefaultMutableTreeNode node)
	{
		return false;
	}
}
