/* @ Copyright IBM Corporation 2003, 2012. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2009-06-02      43813MC  Christopher O    -Initial version
 * 2009-06-30 ~01  45586JM  Christopher O    -Modify the getSize function to return 
 * 											  complete size from COLS10
 * 2012-03-05 ~02  00189533 VH Avila 		 -Save all ALT_CPFX rules for COLT value into
 * 											  ArrayList of new Prefix object
 *******************************************************************************/

package com.ibm.rchland.mfgapps.mfsclient.struct;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;

/**
 * <code>MFSCollectionType</code><p> 
 * Class Bean used to store all values from a given collection type, 
 * this data was retrieved from COLS10 File
 * 
 * @author The MFS Client Development Team
 */
/**
 * @author Administrator
 *
 */
/**
 * @author Administrator
 *
 */
public class MFSCollectionType {
	/** The name of the collection type COLT*/
	private String collectionType;
	/** The ArrayList of Prefixes taken from ALT_CPFX rules*/
	@SuppressWarnings("rawtypes")
	private ArrayList prefixes;
	/** The quantity of values to collect*/
	private int quantity;
	/** The type of the collection type, can be Hexadecimal or Alphanumeric CVTY*/
	private String type;
	/** Flag to check if is a unique colltype value CVUK*/
	private boolean unique;
	/** Message to prompt to user to insert collection type*/
	private String message;
	/** Flag to check if is needed to show collect type dialog on remove*/
	private boolean collectOnRemove;
	
	/**
	 * constructor MFSCollectionType <p>
	 * <p>This constructor will initialize the Array list of the allowed sizes 
	 * from the collection type, and will initialize the message string.  
	 */
	@SuppressWarnings("rawtypes")
	public MFSCollectionType()
	{
		//Initialize the sizes list
		prefixes = new ArrayList();									//~02A
		//Initialize the message string
		message = new String();
	}
	
	/** ~02 New
	 * This function searches for a match of the strPrefix
	 * @param strPrefix
	 * @return Prefix
	 */
	@SuppressWarnings("rawtypes")
	private Prefix getPrefix(String strPrefix)
	{
		Prefix prefix = null;
		
		Iterator iterPrefixes = prefixes.iterator();
		while(iterPrefixes.hasNext())
		{
			Prefix tmpPrefix = (Prefix)iterPrefixes.next();
			if(strPrefix.trim().toUpperCase().startsWith(tmpPrefix.getPrefix().trim().toUpperCase()))
			{
				prefix = tmpPrefix;
				break;
			}
		}
		
		return prefix;
	}
	
	/**
	 * ~02 New - Returns back the prefix's length
	 * @param strPrefix
	 * @return
	 */
	public int getPrefixLength(String strPrefix)
	{
		int length = 0;
		Prefix prefix = getPrefix(strPrefix);
		if(prefix != null)
		{
			length = prefix.getPrefix().length();
		}
		
		return length;
	}
	
	/** ~02 New - Returns back all prefixes recollected from ALT_CPFX rule 
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public String getAllPrefixes()
	{
		StringBuffer  strPrefixes = new StringBuffer();
		Iterator prefixesIt = prefixes.iterator();
		Prefix prefix = null;
		
		while(prefixesIt.hasNext())
		{
			prefix = (Prefix)prefixesIt.next();
			strPrefixes.append(prefix.getPrefix());
			if(prefixesIt.hasNext())
			{
				strPrefixes = strPrefixes.append(",");
			}
		}
		
		return strPrefixes.toString();
	}
	
	/** ~02 New - Validates if we have a rule for the Prefix 
	 * 
	 * @param strPrefix
	 * @return
	 */
	public boolean isValidPrefix(String strPrefix)
	{
		boolean validPrefix = false;
		
		if(getPrefix(strPrefix) != null)
		{
			validPrefix = true;
		}
		
		return validPrefix;
	}
	
	/**
	 * @return Returns collectionType.
	 */
	public String getCollectionType() {
		return collectionType;
	}
	/**
	 * @param collectionType The collectionType to set.
	 */
	public void setCollectionType(String collectionType) {
		this.collectionType = collectionType.trim();
	}
	/**
	 * @return Returns collectOnRemove.
	 */
	public boolean isCollectOnRemove() {
		return collectOnRemove;
	}
	/**
	 * @param collectOnRemove The collectOnRemove to set.
	 */
	public void setCollectOnRemove(String collOnRemove) {
		if(collOnRemove.trim().toLowerCase().equals("y"))
		{
			this.collectOnRemove = true;
		}
		else
		{
			this.collectOnRemove = false;
		}	
	}
	/**
	 * @return Returns message.
	 */
	public String getMessage() {
		return message;
	}
	/**
	 * @param message The message to set.
	 */
	public void setMessage(String message) {
		this.message = message.trim();
	}
	
	/**
	 * ~02 New - This function creates a new Prefix object and add it to ArrayList's prefixes
	 * @param strPrefix
	 * @param sizes
	 */
	@SuppressWarnings("unchecked")
	public void addPrefix(String strPrefix, String sizes)
	{
		Prefix prefix = new Prefix();
		prefix.setPrefix(strPrefix);
		prefix.setSizes(sizes);
		
		prefixes.add(prefix);
	}
	
	/**
	 * ~02 New - This function searches the Prefix object and returns the Sizes 
	 * @return Returns sizes.
	 */
	public String getSizes(String strPrefix) {
		String sizes = null;
		
		Prefix prefix = this.getPrefix(strPrefix);
		
		if(prefix != null)
		{
			sizes = prefix.getSizes();
		}
		
		return sizes;
	}
		
	/**
	 * ~02 New -This function searches the Prefix  object and validates size
	 * @param strPrefix
	 * @param size
	 * @return
	 */
	public boolean isValidSize(String strPrefix, int size)
	{
		boolean validSize = false;
		
		Prefix prefix = getPrefix(strPrefix);
		
		if(prefix != null && prefix.isValidSize(size))
		{
			validSize = true;
		}
			
		return validSize; 
	}	

	/**
	 * @return Returns type.
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type The type to set.
	 */
	public void setType(String type) {
		this.type = type.trim();
	}
	/**
	 * @return Returns unique.
	 */
	public boolean isUnique() {
		return unique;
	}
	/**
	 * @param unique The unique to set.
	 */
	public void setUnique(String unique) {
		if(unique.trim().toLowerCase().equals("y"))
		{
			this.unique = true;
		}
		else
		{
			this.unique = false;
		}
	}		
	/**
	 * @return Returns quantity.
	 */
	public int getQuantity() {
		return quantity;
	}
	/**
	 * @param quantity The quantity to set.
	 */
	public void setQuantity(String quantity) {
		this.quantity = Integer.parseInt(quantity);
	}
	
	private class Prefix{
		/** The prefix of the collection type CPFX*/
		private String prefix;
		/** The complete string of sizes from CSZS*/
		private String sizes;
		/** The allowed sizes CSZS*/
		@SuppressWarnings("rawtypes")
		private ArrayList sizesList;
		
		/**
		 * constructor MFSCollectionType <p>
		 * <p>This constructor will initialize the Array list of the allowed sizes 
		 * from the collection type, and will initialize the message string.  
		 */
		@SuppressWarnings("rawtypes")
		public Prefix()
		{
			//Initialize all members
			sizesList = new ArrayList();
			sizes = new String();
			prefix = new String();
		}
		
		/**
		 * @return Returns prefix.
		 */
		public String getPrefix() {
			return prefix;
		}
		
		/**
		 * @param prefix The prefix to set.
		 */
		public void setPrefix(String prefix) {
			if(!prefix.trim().equals(""))
			{
				this.prefix = prefix.trim();	
			}		
		}
		
		/**
		 * @return Returns sizes.
		 */
		public String getSizes() {
			return sizes;
		}
		
		/**
		 * @param sizes The sizes to set.
		 */
		@SuppressWarnings("unchecked")
		public void setSizes(String sizes) {
			StringTokenizer tokens;
			
			this.sizes = sizes;
			if(sizes.contains(","))
			{
				tokens = new StringTokenizer(sizes,",");	
			}
			else 
			{
				tokens = new StringTokenizer(sizes);
			}
					
			while(tokens.hasMoreTokens())
			{
				String token = tokens.nextToken();
				this.sizesList.add(token);
			}
		}
		
		public boolean isValidSize(int size)
		{
			boolean validSize = false;
			
			String stringSize = Integer.toString(size);
			
			if(sizesList.contains(stringSize))
			{
				validSize = true;
			}
				
			return validSize; 
		}		
	}
}
