/* © Copyright IBM Corporation 2010. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 * 
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- --------------------------------------
 * 2010-01-12      42558JL	D Mathre 		 -Initial version
 * 2010-03-10      42558JL  Santiago SC      -cleanup
 * *******************************************************************************/
 
package com.ibm.rchland.mfgapps.mfsclient.utils;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

import com.ibm.rchland.mfgapps.client.utils.xml.IGSXMLTransaction;
import com.ibm.rchland.mfgapps.mfsclient.struct.MFSLabelTrigDef;
import com.ibm.rchland.mfgapps.mfsclient.struct.MFSLabelTrigInp;
import com.ibm.rchland.mfgapps.mfsclient.struct.MFSLabelTrigs;
import com.ibm.rchland.mfgapps.mfscommon.MFSConfig;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSComm;
import com.ibm.rchland.mfgapps.mfscommon.exception.MFSException;

/**
 * <code>MFSTrigData</code> contains methods to call RTVLBLTRIG to retrieve On Demand Label Trigger
 * Tables information.
 * @author The MFS Client Development Team
 */
public class MFSTriggerData
{
    /** Label Trigger table */
    private Hashtable<String, ArrayList<MFSLabelTrigs>> triggerTable = 
    										new Hashtable<String, ArrayList<MFSLabelTrigs>>();

    /** Label Trigger Definition table */
    private Hashtable<String, ArrayList<MFSLabelTrigDef>> defTable = 
    										new Hashtable<String, ArrayList<MFSLabelTrigDef>>();

    /** Label Trigger Input table */
    private Hashtable<String, ArrayList<MFSLabelTrigInp>> inputTable = 
    										new Hashtable<String, ArrayList<MFSLabelTrigInp>>();

	/** The sole instance of <code>MFSTriggerData</code>. */
	private static final MFSTriggerData INSTANCE = new MFSTriggerData();

	
	/**
	 * Returns the sole instance of <code>MFSTriggerData</code>.
	 * @return the sole instance of <code>MFSTriggerData</code>
	 */
	public static MFSTriggerData getInstance()
	{
		return INSTANCE;
	}

	/**
	 * Constructs a new <code>MFSConfig</code>. This class implements the
	 * <cite>Singleton </cite> design pattern. To ensure only one instance of
	 * <code>MFSTriggerData</code> exists, the only constructor has
	 * <code>private</code> visibility.
	 */
	private MFSTriggerData()
	{
		super();
	}
	
	/**
	 * Check if if the label trigger table contains the triggerSource.
	 * @param triggerSource the trigger source
	 * @return true if the triggerSource was found false otherwise.
	 */
	public boolean containsTrigger(String triggerSource)
	{
		return triggerTable.containsKey(triggerSource);
	}	

	/**
	 * Get the <code>String</code> representing the method to be executed to
	 * retrieve the sourceData.
	 * @param triggerSource the On Demand trigger source from TDEF file
	 * @param keyElement the On Demand key from TDEF file
	 * @return a <code>String</code> representing the method to be executed
	 * @throws MFSException
	 */
	public String rtvDataSourcePath(String triggerSource, String keyElement)
		throws MFSException
	{
		// Get the dataSource (method to invoke)
		MFSLabelTrigDef ltd = rtvTrigDef(triggerSource, keyElement);
		
		if (null == ltd) 
		{
			StringBuffer sb = new StringBuffer();
			sb.append("No trigger definition found in TDEF file for \n");
			sb.append("LBTS = ");
			sb.append(triggerSource);
			sb.append(", LBTK = ");
			sb.append(keyElement);
			
			throw new MFSException(sb.toString(), false);
		}
				
		return ltd.getDataSource();
	}
	
	/**
	 * Retrieves an <code>ArrayList</code> of valid labels. Labels returned
	 * must have the same configuration in CONF and TRIG files.
	 * @param triggerSource the trigger source
	 * @param triggerKey the trigger key
	 * @return the list of valid labels
	 */
	public ArrayList<String> rtvLabels(String triggerSource, String triggerKey)
	{
		final MFSConfig configs = MFSConfig.getInstance();
		
		ArrayList<String> trigLabels = rtvTrigLabels(triggerSource, triggerKey);
		
		ArrayList<String> validLabels = null; 
		
		if(null != trigLabels)
		{
			validLabels = new ArrayList<String>();
			
			for(String label : trigLabels)
			{
				StringBuffer configEntry = new StringBuffer();
				configEntry.append(label);
				configEntry.append(",");
				configEntry.append(triggerSource);
				configEntry.append(",");
				configEntry.append(triggerKey);
				
				if(configs.containsConfigEntry(configEntry.toString()))
				{
					validLabels.add(label);
				}
			}
			
			if(validLabels.isEmpty())
			{
				validLabels = null;
			}
		}
		
		return validLabels;
	}
		
	/**
	 * Retrieve the source data <code>String</code> invoking the method of the given
	 * object. Delegate this work to <code>MFSInvoker</code>.
	 * @param object whose method will be invoked
	 * @param method to invoke
	 * @return a <code>String</code> with the sourceData
	 * @throws MFSException
	 */
	public String rtvSourceData(Object source, String method)
		throws MFSException
	{
		Object sourceData = null;

		sourceData = MFSInvoker.invokeGetter(source, method);

		if(null == sourceData)
		{
			// Warning: sourceData is null!!, switching to blanks...
			sourceData = "";
		}

		return sourceData.toString().trim();
	}

	/** 
	 * Retrieve Trigger table info based on computername
	 * and what is in CONFIG table for the computername.
	 * Calls the RTVLBLTRIG transaction. 
	 * 
	 * @param computer name the <code>String</code> used to 
	 * 			retrieve trigger data  
	 * @return 0 on success; nonzero on error
	 */
	public void rtvTrigData(String computerName)throws MFSException
	{
		String trigSrc = "";
		String label = "";
		int rc = 0;
				
		try
		{
			IGSXMLTransaction rtvLblTrig = new IGSXMLTransaction("RTVLBLTRIG"); 
			rtvLblTrig.setActionMessage("Retrieving On Demand Labelinformation...");
			rtvLblTrig.startDocument();
			rtvLblTrig.addElement("COMPNAME", computerName); 
			rtvLblTrig.endDocument();

			MFSComm.execute(rtvLblTrig);
			
			rc = rtvLblTrig.getReturnCode();
			
			if (0 == rc) 
			{
				rtvLblTrig.stepIntoElement("TRIGS");	
				while (null != rtvLblTrig.stepIntoElement("TRIGSRC"))
				{						
					trigSrc = rtvLblTrig.getFirstElement("LBTS"); 
					
					ArrayList<MFSLabelTrigs> triggerList = new ArrayList<MFSLabelTrigs>();
					
					while (null != rtvLblTrig.stepIntoElement("TRIG"))
					{	
						MFSLabelTrigs lblTrigger = new MFSLabelTrigs();
						lblTrigger.setSource(trigSrc);
						lblTrigger.setKeyElement(rtvLblTrig.getNextElement("LBTK").trim());	
						
						ArrayList<String> labelTypes = new ArrayList<String>();
						rtvLblTrig.getElements(labelTypes,"LABELS","LBLT");
						labelTypes.trimToSize();
						lblTrigger.setLabelTypes(labelTypes);
						
						// step out of TRIG tag
						rtvLblTrig.stepOutOfElement();
						
						// add the lblTrigger to the triggerList
						triggerList.add(lblTrigger);
					}	
					// step out of TRIGSRC tag
					rtvLblTrig.stepOutOfElement();
					
					triggerList.trimToSize();
					
					// add the triggerList to the triggerTable
					triggerTable.put(trigSrc, triggerList);
				}
				// step out of TRIGS tag
				rtvLblTrig.stepOutOfElement();
							
				
				rtvLblTrig.stepIntoElement("TDEFS");	
				while (null != rtvLblTrig.stepIntoElement("TRIGSRC"))
				{
					trigSrc = rtvLblTrig.getNextElement("LBTS").trim(); 
					
		    		ArrayList<MFSLabelTrigDef> defList = new ArrayList<MFSLabelTrigDef>();		    	   	
					
					while (null != rtvLblTrig.stepIntoElement("TDEF"))
					{
						MFSLabelTrigDef triggerDef = new MFSLabelTrigDef();
						triggerDef.setKeyElement(rtvLblTrig.getNextElement("LBTK").trim());
						triggerDef.setDataSource(rtvLblTrig.getNextElement("DATF").trim());
						
						// step out of TDEF tag
						rtvLblTrig.stepOutOfElement();
						
						// add the triggerDef to the definition list
						defList.add(triggerDef);						
					}
					// step out of LBTS tag
					rtvLblTrig.stepOutOfElement();
					
					defList.trimToSize();
					
					// put the definition list in the definition table
					defTable.put(trigSrc, defList);
				}
				rtvLblTrig.stepOutOfElement();
						
				rtvLblTrig.stepIntoElement("TINPS");	
				// we are going to use our inputTable Hashtable
				while (null != rtvLblTrig.stepIntoElement("TINP"))
				{	
					trigSrc = rtvLblTrig.getNextElement("LBTS").trim(); 
				       
					while (null != rtvLblTrig.stepIntoElement("LBL"))
					{
						label = rtvLblTrig.getNextElement("LBLT");	
				    	  	
			    		/* create the ArrayList of MFSLabelTrigInp , would be a lot better
			    		 * to have a <VALS> tag and create that inside but is ok.
			    		 */
			    		ArrayList<MFSLabelTrigInp> inputList = new ArrayList<MFSLabelTrigInp>();
			    	   	
			    		while (null != rtvLblTrig.stepIntoElement("VAL"))
			    		{										
			       			MFSLabelTrigInp inputField = new MFSLabelTrigInp();
			       			inputField.setValue(rtvLblTrig.getNextElement("INPV").trim());
			       			inputField.setType(rtvLblTrig.getNextElement("INPT").trim());

				       		String tempField = rtvLblTrig.getNextElement("REQF").trim();				       
				       		inputField.setRequired(((tempField.equals("Y"))? true : false));
				       			
				       		tempField = rtvLblTrig.getNextElement("MAXL").trim();
				       		tempField = (0 == tempField.length())? "0" : tempField;
				       		inputField.setMaxLength(Integer.parseInt(tempField));
				       			
				       		tempField = rtvLblTrig.getNextElement("MINL").trim();
				       		tempField = (0 == tempField.length())? "0" : tempField;
				       		inputField.setMinLength(Integer.parseInt(tempField));

			       			inputField.setText(rtvLblTrig.getNextElement("ITXT").trim());
			       			inputField.setTag(rtvLblTrig.getNextElement("ITAG").trim());
			       			
			       			tempField = rtvLblTrig.getNextElement("IORD").trim();
			       			tempField = (0 == tempField.length())? "100" : tempField;
			       			inputField.setDisplayOrder(Integer.parseInt(tempField));
			       			
			       			// step out of VAL tag
			       			rtvLblTrig.stepOutOfElement();
			       			
			       			// Put the object int the list
			       			inputList.add(inputField);				       			
				    	}
			    		// step out of LBL tag
			       		rtvLblTrig.stepOutOfElement();
			       		
			       		inputList.trimToSize();
			       		
			       		// Put the object in the inputTable Hashtable
			       		String key = trigSrc + "." + label;
			       		inputTable.put(key, inputList);
					}
					// step out of TINP tag
					rtvLblTrig.stepOutOfElement();
				}	
				// step out of TINPS tag
				rtvLblTrig.stepOutOfElement(); 
			}
			else
			{
				if(0 > rc)
				{
					// hard error
					throw new MFSException("Retrieval of Trigger Data not valid. (RTVLBLTRIG)");
				}
				else if(0 < rc)
				{
					// log error
					System.out.println(rtvLblTrig.getErms());
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			
			throw new MFSException(e.getMessage());
		}
	}
	
	/** 
	 * Retrieve Trigger Definition object (TDEF10) based on trigger source.
	 */
	public MFSLabelTrigDef rtvTrigDef(String triggerSource, String keyElement)
	{
		MFSLabelTrigDef ltd = null;
		
		ArrayList<MFSLabelTrigDef> defList = rtvTrigDefs(triggerSource);
		
		if(null != defList)
		{
			Iterator<MFSLabelTrigDef> defListIter = defList.iterator();
			
			while(defListIter.hasNext())
			{
				MFSLabelTrigDef ltdTmp = defListIter.next();
				
				if(ltdTmp.getKeyElement().equals(keyElement))
				{
					ltd = ltdTmp;
					break;
				}
			}
		}
		
		return ltd;
	}
	
	/** 
	 * Retrieve Trigger Definition table info (TDEF10) based on trigger source.
	 * Store the object in ArrayList<MFSLabelTrigInp>. Returns null
	 * if no match is found.
	 */
	public ArrayList<MFSLabelTrigDef> rtvTrigDefs(String triggerSource)
	{
		return defTable.get(triggerSource);
	}		
	
	/** 
	 * Retrieve triggers from TRIG10 table info based on trigger source
	 * Store the object in ArrayList. Returns null if no match is found.
	 * @throws MFSException 
	 */
	public ArrayList<MFSLabelTrigs> rtvTriggers(String triggerSource)
		throws MFSException
	{
		// Get the dataSource (method to invoke)
		ArrayList<MFSLabelTrigs> lblTrigs = rtvTrigLabelList(triggerSource);
		
		if (null == lblTrigs) 
		{
			StringBuffer sb = new StringBuffer();
			sb.append("No triggers found in TRIG file for \n");
			sb.append("LBTS = ");
			sb.append(triggerSource);
			
			throw new MFSException(sb.toString(), false);
		}
		
		return lblTrigs;
	}
	
	/** 
	 * Retrieve Trigger Input table info (TINP10) based on trigger source
	 * and label type. Store the object in ArrayList<MFSLabelTrigInp>. Returns
	 * null if no match is found.
	 */
	public ArrayList<MFSLabelTrigInp> rtvTrigInput(String triggerSource, String labelType)
	{
		return inputTable.get(triggerSource + "." + labelType);
	}		
	
	/**
	 * This method is similar to rtvTrigInput but throws an exception in case the
	 * <code>MFSLabelTrigInp</code> list is null.
	 * @param triggerSource the trigger source
	 * @param labelType the label type
	 * @return the list of <code>MFSLabelTrigInp</code> fields
	 * @throws MFSException if no input fields were found
	 */
	public ArrayList<MFSLabelTrigInp> rtvTrigInputs(String triggerSource, String labelType) 
		throws MFSException
	{
		// Get the list of fields
		ArrayList<MFSLabelTrigInp> trigInpList = rtvTrigInput(triggerSource, labelType);
	
		// validate the list
		if (null == trigInpList) 
		{
			StringBuffer sb = new StringBuffer();
			sb.append("No input fields found in TINP file for \n");
			sb.append("LBTS = ");
			sb.append(triggerSource);
			sb.append(", LBLT = ");
			sb.append(labelType);
			
			throw new MFSException(sb.toString(), false);
		}			
		
		return trigInpList;
	}
	
	/** 
	 * Retrieve Trigger Labels from TRIG10 table info based on trigger source
	 * Store the object in ArrayList. Returns null if no match is found.
	 */
	public ArrayList<MFSLabelTrigs> rtvTrigLabelList(String triggerSource)
	{
		return triggerTable.get(triggerSource);
	}	
	
	/** 
	 * Retrieve Trigger Labels from TRIG10 table info based on trigger source
	 * Store the object in ArrayList. 
	 * 
	 * @param trigger source the <code>String</code> used to 
	 * 			retrieve trigger labels  
	 * @return ArrayList on sucess, null if failed.
	 */
	public ArrayList<String> rtvTrigLabels(String triggerSource, String keyElement)
	{
		ArrayList<String> valLabels = null;
		ArrayList<MFSLabelTrigs> triggerList = null;
		
		if(null != (triggerList = rtvTrigLabelList(triggerSource)))
		{
			Iterator<MFSLabelTrigs> triggerListIter = triggerList.iterator();
			
			while(triggerListIter.hasNext())
			{
				MFSLabelTrigs lblTrigger = triggerListIter.next();	
				
				if(lblTrigger.getKeyElement().equals(keyElement))
				{
					valLabels = lblTrigger.getLabelTypes();
					break;
				}
			}
		}
		
		return valLabels;
	}
}
