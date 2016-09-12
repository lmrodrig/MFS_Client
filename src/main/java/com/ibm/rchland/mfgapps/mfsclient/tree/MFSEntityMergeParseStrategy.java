/* @ Copyright IBM Corporation 2007, 2011. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2008-03-15      37616JL  R Prechel        -Initial version
 *                          D Pietrasik
 * 2011-10-18 ~01  00177780 Giovanni Toledo	 -Added NITEM conditions to create MFSEntityMergeItemUO object type                          
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.tree;

import java.util.LinkedHashMap;

import javax.swing.tree.DefaultMutableTreeNode;

import org.xml.sax.Attributes;

import com.ibm.rchland.mfgapps.client.utils.xml.IGSUserObject;
import com.ibm.rchland.mfgapps.client.utils.xml.IGSXMLTreeParseStrategy;

/**
 * <code>MFSEntityMergeParseStrategy</code> is the
 * <code>IGSXMLTreeParseStrategy</code> for an Entity Merge tree.
 * @author The MFS Client Development Team
 */
public class MFSEntityMergeParseStrategy
	implements IGSXMLTreeParseStrategy
{
	/** The {@link MFSEntityMergeCntrUO}s for the tree. */
	@SuppressWarnings("rawtypes")
	private LinkedHashMap containers = new LinkedHashMap();
	
	/** The {@link MFSEntityMergeItemUO}s for the tree. */
	@SuppressWarnings("rawtypes")
	private LinkedHashMap items = new LinkedHashMap(); // ~01A

	/** The user object for the root node of the left tree. */
	private MFSEntityMergeUO leftUserObject = null;

	/** The user object for the root node of the right tree. */
	private MFSEntityMergeUO rightUserObject = null;

	/** Constructs a new <code>MFSEntityMergeParseStrategy</code>. */
	public MFSEntityMergeParseStrategy()
	{
		super();
	}

	/** {@inheritDoc} */
	public boolean isTreeNode(String element)
	{
		if ("NCNTR".equals(element)) //$NON-NLS-1$
		{
			return true;
		}
		else if ("NMCTL".equals(element)) //$NON-NLS-1$
		{
			return true;
		}
		else if ("NMFGN".equals(element)) //$NON-NLS-1$
		{
			return true;
		}
		else if ("NLEFT".equals(element)) //$NON-NLS-1$
		{
			return true;
		}
		else if ("NRIGHT".equals(element)) //$NON-NLS-1$
		{
			return true;
		}
		else if ("NSHPN".equals(element)) //$NON-NLS-1$
		{
			return true;
		}
		else if ("NITEM".equals(element)) //$NON-NLS-1$ ~01A Added NITEM condition
		{
			return true;
		}
		return false;
	}

	/** {@inheritDoc} */
	@SuppressWarnings("unchecked")
	public IGSUserObject createUserObject(String element, Attributes attr)
	{
		MFSEntityMergeUO result = null;
		if ("NCNTR".equals(element)) //$NON-NLS-1$
		{
			String cntr = attr.getValue("cntr"); //$NON-NLS-1$
			result = (MFSEntityMergeUO) this.containers.get(cntr);
			if (result == null)
			{
				result = new MFSEntityMergeCntrUO(cntr);
				this.containers.put(cntr, result);
			}
		}
		else if ("NMCTL".equals(element)) //$NON-NLS-1$
		{
			String mctl = attr.getValue("mctl"); //$NON-NLS-1$
			result = new MFSEntityMergeMctlUO(mctl);
		}
		else if ("NMFGN".equals(element)) //$NON-NLS-1$
		{
			String mfgn = attr.getValue("mfgn"); //$NON-NLS-1$
			String item = attr.getValue("item"); //$NON-NLS-1$ ~01A
			String pprl = attr.getValue("pprl"); //$NON-NLS-1$ ~01A
			result = new MFSEntityMergeMfgnUO(mfgn,item,pprl);
			
			if(pprl.trim().equals("BAPRLN"))// ~01A
			{
				MFSEntityMergeItemUO itemNode = (MFSEntityMergeItemUO) this.items.get(item);
				itemNode.addMfgn((MFSEntityMergeMfgnUO)result);
			}
		}
		else if ("NLEFT".equals(element)) //$NON-NLS-1$
		{
			result = this.leftUserObject;
		}
		else if ("NRIGHT".equals(element)) //$NON-NLS-1$
		{
			result = this.rightUserObject;
		}
		else if ("NSHPN".equals(element)) //$NON-NLS-1$
		{
			/* Starting a new tree; clear containers from previous tree. */
			this.containers.clear();
			String shpn = attr.getValue("shpn"); //$NON-NLS-1$
			result = new MFSEntityMergeShpnUO(shpn);
			this.leftUserObject = result;
			result = new MFSEntityMergeShpnUO(shpn); // ~01A
			this.rightUserObject = result;
		}
		else if ("NITEM".equals(element)) //$NON-NLS-1$
		{
			String item = attr.getValue("item");
			
			result = (MFSEntityMergeUO) this.items.get(item);
			if (result == null)
			{
				result = new MFSEntityMergeItemUO(item);
				this.items.put(item, result);
			}
		}/* ~01 Added NITEM condition to create MFSEntityMergeItemUO object */
		
		return result;
	}

	/** {@inheritDoc} */
	public boolean isSelectedNode(DefaultMutableTreeNode node)
	{
		return false;
	}
}
