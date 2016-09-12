/* @ Copyright IBM Corporation 2003, 2009. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2009-06-02      43813MC  Christopher O    -Initial version
 *******************************************************************************/

package com.ibm.rchland.mfgapps.mfsclient.struct;

import java.util.TreeMap;

/**
 * <code>MFSComponentCollection</code><p> 
 * Class Bean used to save all collected values of a component Collection retrieved from COLD10 File
 * 
 * @author The MFS Client Development Team
 */
public class MFSComponentCollection {
	/** The component count*/
	private String crct;
	/** The name of the collection Type*/
	private String collectionType;
	/** The quantity of values to collect*/
	private int quantity;
	/** The order of the collection Type in window*/
	private String order;
	/** The list of values in order*/
	@SuppressWarnings("rawtypes")
	private TreeMap valuesList; 
	
	/**
	 * constructor MFSComponentCollection <p>
	 * <p>
	 * This is the constructor of the <code>MFSComponentCollection</code><p>   
	 * It will initialize the list sorted of values collected in dialog. 

	 */	
	@SuppressWarnings("rawtypes")
	public MFSComponentCollection()
	{
		valuesList = new TreeMap();
	}
	
	/**
	 * @return Returns collectionType.
	 */
	public String getCollectionType() {
		return collectionType;
	}
	/**
	 * @param collectionType the collectionType to set.
	 */
	public void setCollectionType(String collectionType) {
		this.collectionType = collectionType.trim();
	}
	/**
	 * @return Returns crct.
	 */
	public String getCrct() {
		return crct;
	}
	/**
	 * @param crct the crct to set.
	 */
	public void setCrct(String crct) {
		this.crct = crct.trim();
	}
	/**
	 * @return Returns valuesList.
	 */
	@SuppressWarnings("rawtypes")
	public TreeMap getValuesList() {
		return valuesList;
	}
	/**
	 * @param valuesList the valuesList to set.
	 */
	@SuppressWarnings("unchecked")
	public void addValuesList(String cval, String cvct) {
		//Add Collected value and Collected Count to Values list
		Object[] value = new Object[2];
		//Store the value
		value[0] = cval.trim();
		//Store the value count
		value[1] = cvct.trim();
		//Add the values to the sorted list.
		valuesList.put(cvct.trim(),value);		
	}
	
	public boolean isEmptyValuesList ()
	{
		return valuesList.isEmpty();		
	}

	/**
	 * @return Returns quantity.
	 */
	public int getQuantity() {
		return quantity;
	}
	/**
	 * @param quantity the quantity to set.
	 */
	public void setQuantity(String quantity) {
		if(quantity != null)
		{
			//Convert the quantity to integer
			this.quantity = Integer.parseInt(quantity);	
		}		
		else
		{
			//If the quantity is null, set quantity to -1 to be used as flag in <code>MFSPartDataCollectionDialog</code>
			this.quantity = -1;
		}
	}
	/**
	 * @return Returns order.
	 */
	public String getOrder() {
		return order;
	}
	/**
	 * @param order the order to set.
	 */
	public void setOrder(String order) {
		this.order = order;
	}
}
