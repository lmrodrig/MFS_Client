/* @ Copyright IBM Corporation 2008, 2009. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR    Name             Details
 * ---------- ---- ----------- ---------------- ----------------------------------
 * 2011-10-18      RCQ00177780 Giovanni Toledo  -Initial version
 * 
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.tree;

import java.util.Vector;

/**
 * <code>MFSEntityMergeItemUO</code> is the <code>MFSEntityMergeUO</code>
 * for a manufacturing control number (MFGN) node.
 * @author The MFS Client Development Team
 */
public class MFSEntityMergeItemUO
	extends MFSEntityMergeUO
{

    /** The config item. */
    String item = null;
    
    /** Quantity of items */
    int quantity = 0;
    
    /** Current MFGN in use */
    MFSEntityMergeMfgnUO currentMfgn = null;
    
    /** Vector containing all the mfgn's */
    Vector<MFSEntityMergeMfgnUO> mfgns = null;
    

	/**
	 * Constructs a new <code>MFSEntityMergeItemUO</code>.
	 * @param entityMfgn the entity of the item
	 */
	public MFSEntityMergeItemUO(String item)
	{
		super();
		this.item = item;
		mfgns = new Vector<MFSEntityMergeMfgnUO>();
	}

	/** {@inheritDoc} */
	protected String createString(String type)
	{
		StringBuffer buffer = new StringBuffer(100);
		quantity = getMfgns().size();
		String separator = bundle.getString(type + "Separator"); //$NON-NLS-1$

        if (display(type, this.item))
        {
            buffer.append(bundle.getString(type + "ITEM")); //$NON-NLS-1$
            buffer.append(this.item);
        }

		buffer.append(separator);
		buffer.append("Quantity="); //$NON-NLS-1$
		buffer.append(this.quantity);
		
		return buffer.toString();
	}

	

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public MFSEntityMergeMfgnUO getCurrentMfgn() {
		return currentMfgn;
	}

	public void setCurrentMfgn(MFSEntityMergeMfgnUO currentMfgn) {
		this.currentMfgn = currentMfgn;
	}

	public Vector<MFSEntityMergeMfgnUO> getMfgns() {
		return mfgns;
	}

	public void setMfgns(Vector<MFSEntityMergeMfgnUO> mfgns) {
		this.mfgns = mfgns;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}
	
	public void addMfgn(MFSEntityMergeMfgnUO mfgn)
	{
		if(mfgns == null)
		{
			mfgns = new Vector<MFSEntityMergeMfgnUO>();
		}
		
		mfgns.add(mfgn);
	}
	
	public void removeMfgn()
	{
		if(mfgns != null)
		{
			mfgns.remove(0);
		}
		
		quantity = quantity - 1;
	}
}