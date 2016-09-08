/* © Copyright IBM Corporation 2003, 2009. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2009-06-02      43813MC  Christopher O    -Initial version
 * 2009-07-03  ~01 45654SR  Christopher O    -Add collect mode param in getComponentCollection function.
 *******************************************************************************/

package com.ibm.rchland.mfgapps.mfsclient.struct;

import java.util.ArrayList;
import java.util.Hashtable;

import com.ibm.rchland.mfgapps.client.utils.xml.IGSXMLTransaction;
import com.ibm.rchland.mfgapps.mfsclient.dialog.log.MFSPartDataCollectDialog;    //~01A

/**
 * <code>MFSDataCollection</code><p> 
 * Class used to store <code>MFSComponentCollection</code> and <code>MFSColletionType</code> objects in 
 * the list of components and types
 * 
 * @author The MFS Client Development Team
 */
public class MFSDataCollection {
	/** */
	@SuppressWarnings("rawtypes")
	private Hashtable componentDataToCollect;
	/** */
	public static final String DATA_TO_COLLECT = "DATA_TO_COLLECT";
	/** */	
	@SuppressWarnings("rawtypes")
	private Hashtable componentCollectedData;
	/** */
	public static final String COLLECTED_DATA = "COLLECTED_DATA";
	/** */
	@SuppressWarnings("rawtypes")
	private Hashtable collectionTypes;	
	/** On Replace dialog, first we need to store the current transaction to remove, to be executed after collect new data */	
	private IGSXMLTransaction w_partcoll_remove;
		
	/**
	 * constructor MFSDataCollection <p>
	 * <p>
	 * This is the constructor will initialize the Hashtables to store 
	 * all <code>MFSComponentCollection</code> and <code>MFSCollectionType</code> 
	 * 
	 */
	@SuppressWarnings("rawtypes")
	public MFSDataCollection(){
		//Initialize the list to store all <code>MFSComponentCollection</code> data to Collect
		componentDataToCollect = new Hashtable();	
		//Initialize the list of all collection type's <code>MFSCollectionType</code> 
		collectionTypes = new Hashtable();
		//Initialize the list to store all <code>MFSComponentCollection</code> Collected Data
		componentCollectedData = new Hashtable();	
	}	
	
	/**This function is to add a <code>MFSComponentCollection</code> into the componentCollection
	 * Hashtables, this function will store the component collection by MCTL, and for each MCTL 
	 * will contain each CRCT for the retrieved data. 
	 * 
	 * @param MFSComponentCollection componentColl is the class to store all component collection data 
	 * @param mctl is the work unit
	 * @return 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void addComponentCollection(MFSComponentCollection componentColl, String mctl, String mode)
	{
		Hashtable componentCollection;
		
		if(mode.equals(DATA_TO_COLLECT))
		{
			componentCollection = componentDataToCollect;
		}
		//COLLECTED_DATA
		else
		{
			componentCollection = componentCollectedData;
		}
				
		//If exist the MCTL in the component collection list, retrieve the object and add the component coll inside that object
		if(componentCollection.containsKey(mctl))
		{			
			//Get the list of all collections by crct
			Hashtable listOfComponentsByCrct = (Hashtable)componentCollection.get(mctl);
			//Check if already exist the crct in the table
			if(listOfComponentsByCrct.containsKey(componentColl.getCrct()))
			{
				//Get the list of all MFSComponentCollection by crct
				ArrayList crctComponent =(ArrayList) listOfComponentsByCrct.get(componentColl.getCrct());
				//add the component collection to the array List
				crctComponent.add(componentColl);
				//Insert the array list again into the crct list, this will replace the current crct
				listOfComponentsByCrct.put(componentColl.getCrct(),crctComponent);
				//Insert and replace the list of components by mctl
				componentCollection.put(mctl,listOfComponentsByCrct);
			}
			//If not exist the crct in the table, create a new object and store it in the mctl
			else
			{
				//Create a new Array list to store all MFSComponentCollections
				ArrayList crctComponent = new ArrayList();
				//Add the current componet collection into the new Array List
				crctComponent.add(componentColl);
				//Insert the list by crct
				listOfComponentsByCrct.put(componentColl.getCrct(), crctComponent);
				//Insert and replace the list of crct's by mctl
				componentCollection.put(mctl,listOfComponentsByCrct);
			}
		}
		else
		{		
			//Create a new list of components by crct
			Hashtable listOfComponentsByCrct = new Hashtable();
			//Create a new Array List of MFSComponentCollections 
			ArrayList crctComponent = new ArrayList();
			//add the current component collection into the array list 
			crctComponent.add(componentColl);
			//add the list of MFSComponentCollections into the crct's list
			listOfComponentsByCrct.put(componentColl.getCrct(),crctComponent);
			//Insert the list of crct's by mctl into the component collections list
			componentCollection.put(mctl,listOfComponentsByCrct);
			
		}			
	}
	/**This function is to add a new collection type into the <code>MFSCollectionType</code> list.
	 * 
	 * @param collectionType <code>MFSCollectionType</code> 
	 */
	@SuppressWarnings("unchecked")
	public void addCollectionType(MFSCollectionType collectionType)
	{
		//Insert the collection type as key , and the collectionType object as value
		collectionTypes.put(collectionType.getCollectionType(),collectionType);				
	}
	/**This function will get Object of the given collection type name  
	 * 
	 * @param collectionType is the name of the collection Type 
	 * @return Returns the <code>MFSCollectionType</code> object for given name
	 */
	public MFSCollectionType getCollectionType(String collectionType)
	{
		return (MFSCollectionType) collectionTypes.get(collectionType);
	}

	/**This function retrieve the list of all <code>MFSComponentCollection</code> 
	 * for a given mctl and crct 
	 * 
	 * @param weight 
	 * @param unit
	 * @return Returns the string with the converted weight
	 */
	@SuppressWarnings("rawtypes")
	public ArrayList getComponentCollection(String mctl, String crct, String collectMode) //~01C
	{	
		boolean rc = false;
		ArrayList returnList = null;
		Hashtable componentCollect = null;                                    //~01C
		Hashtable listOfComponentsByCrct = null; 	
		                                       		
		//Begin ~01A
		if(collectMode.equals(MFSPartDataCollectDialog.COLL_COLLECT))
		{
			componentCollect = componentDataToCollect;
		}
		//COLL_REWORK_REPLACE, COLL_REMOVE, COLL_VIEW
		else
		{
			componentCollect = componentCollectedData;
		}
		//End ~01A
		
		if(componentCollect.containsKey(mctl))                                 //~01C
		{
			listOfComponentsByCrct =(Hashtable) componentCollect.get(mctl);    //~01C      
			if(listOfComponentsByCrct != null || !listOfComponentsByCrct.isEmpty())
			{
				if(listOfComponentsByCrct.containsKey(crct))
				{
					rc = true;
					returnList =(ArrayList) listOfComponentsByCrct.get(crct);
				}
			}	
		}
		
		//Crct was not found in the Collected Data List, If it's View Mode show Data to Collect in dialog  
		if(rc == false && collectMode.equals(MFSPartDataCollectDialog.COLL_VIEW)) //~01C
		{
			//Check the Data to Collect List
			if(componentDataToCollect.containsKey(mctl))
			{
				listOfComponentsByCrct =(Hashtable) componentDataToCollect.get(mctl);
				if(listOfComponentsByCrct != null || !listOfComponentsByCrct.isEmpty())
				{
					if(listOfComponentsByCrct.containsKey(crct))
					{
						returnList = (ArrayList) listOfComponentsByCrct.get(crct);
					}
				}						
			}
		}	
		return returnList;	
	}
	/**This function will remove a crct from the list 
	 * 
	 * @param mctl is the work unit 
	 * @param crct is the component 
	 * @return true if was removed, false otherwise
	 */
	@SuppressWarnings("rawtypes")
	public boolean removeComponentCollection(String mctl, String crct)
	{
		boolean rc = false;
		//Check if exist the mctl 
		if(componentCollectedData.containsKey(mctl))
		{
			//Retrieve the list of components of the mctl
			Hashtable listOfComponentsByCrct = (Hashtable) componentCollectedData.get(mctl);
			//Check if exist the crct in the mctl 
			if(listOfComponentsByCrct.containsKey(crct))
			{
				//If exist the crct, remove the collection 
				listOfComponentsByCrct.remove(crct);
				rc = true;
			}
		}
		return rc;
	}
	
	/**This will check if the collection type list contain a given collection type name
	 * 
	 * @param collectionType, is the name of the collection type
	 * @return Returns true if exist the collection type name
	 */
	public boolean containsCollectionType(String collectionType)
	{
		return collectionTypes.containsKey(collectionType);
	}
	
	/**This function will verify if exist the crct in the component collection list 
	 * for a given mctl
	 * 
	 * @param mctl 
	 * @param crct
	 * @return Returns true if the component collection exist for the given mctl, crct
	 */
	@SuppressWarnings("rawtypes")
	public boolean containsComponentCollection(String mctl, String crct, String collectMode) //~01C
	{
		boolean rc = false;
		Hashtable componentCollect = null;                                    //~01A
		Hashtable listOfComponentsByCrct = null; 
		
		//Begin ~01A
		if(collectMode.equals(MFSPartDataCollectDialog.COLL_COLLECT))
		{
			componentCollect = componentDataToCollect;
		}
		//COLL_REWORK_REPLACE, COLL_REMOVE, COLL_VIEW
		else
		{
			componentCollect = componentCollectedData;
		}
		//End ~01A
				
		if(componentCollect.containsKey(mctl))                                //~01C
		{
			listOfComponentsByCrct =(Hashtable) componentCollect.get(mctl);   //~01C
			if(listOfComponentsByCrct != null || !listOfComponentsByCrct.isEmpty())
			{
				rc = listOfComponentsByCrct.containsKey(crct);					
			}	
		}
		if(rc == false && collectMode.equals(MFSPartDataCollectDialog.COLL_VIEW)) //~01C
		{
			if(componentDataToCollect.containsKey(mctl))
			{
				listOfComponentsByCrct =(Hashtable) componentDataToCollect.get(mctl);
				if(listOfComponentsByCrct != null || !listOfComponentsByCrct.isEmpty())
				{
					rc = listOfComponentsByCrct.containsKey(crct);	
				}						
			}
		}	
		return rc;
	}
	/**This function will add a new component by mctl
	 * 
	 * @param mctl is the work unit
	 * @param componentByMctl is the component to add 
	 * @return 
	 */
	@SuppressWarnings("unchecked")
	public void addMctlComponentCollectedData(String mctl, @SuppressWarnings("rawtypes") Hashtable componentByMctl)
	{
		componentCollectedData.put(mctl,componentByMctl);
	}
	/**This function will check id component collected data contains the mctl
	 * 
	 * @param mctl is the work unit
	 * @return 
	 */
	public boolean containsMctlComponentCollectedData(String mctl)
	{
		return componentCollectedData.containsKey(mctl);
	}
	/**This function will retrieve the component collection by mctl 
	 * 
	 * @param mctl is the work unit to retrieve from the list
	 * @return Returns the componet collection for given mctl
	 */
	@SuppressWarnings("rawtypes")
	public Hashtable getMctlComponentCollectedData(String mctl)
	{
		return (Hashtable) componentCollectedData.get(mctl);
	}
	
	/**This function will add a new component by mctl
	 * 
	 * @param mctl is the work unit
	 * @param componentByMctl is the component to add 
	 * @return 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void addMctlComponentDataToCollect(String mctl, Hashtable componentByMctl)
	{
		componentDataToCollect.put(mctl,componentByMctl);
	}
	
	/**This function will if component data to collect contains the mctl
	 * 
	 * @param mctl is the work unit 
	 * @return 
	 */
	public boolean containsMctlComponentDataToCollect(String mctl)
	{
		return componentDataToCollect.containsKey(mctl);
	}
	
	/**This function will retrieve the component collection by mctl 
	 * 
	 * @param mctl is the work unit to retrieve from the list
	 * @return Returns the componet collection for given mctl
	 */
	@SuppressWarnings("rawtypes")
	public Hashtable getMctlComponentDataToCollect(String mctl)
	{
		return (Hashtable) componentDataToCollect.get(mctl);
	}	
	
	/**
	 * @return Returns w_partcoll_remove.
	 */
	public IGSXMLTransaction getW_partcoll_remove() {
		return w_partcoll_remove;
	}
	/**
	 * @param w_partcoll_remove the w_partcoll_remove to set.
	 */
	public void setW_partcoll_remove(IGSXMLTransaction w_partcoll_remove) {
		this.w_partcoll_remove = w_partcoll_remove;
	}
}
