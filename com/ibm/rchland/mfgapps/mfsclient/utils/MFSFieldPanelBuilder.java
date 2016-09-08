/* © Copyright IBM Corporation 2010. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2010-03-04       42558JL Santiago SC      -Initial version, Java 5.0
 ******************************************************************************/

package com.ibm.rchland.mfgapps.mfsclient.utils;

import java.util.ArrayList;

import javax.swing.JFormattedTextField;

import com.ibm.rchland.mfgapps.client.utils.beans.IGSTextFieldPanelProperties;
import com.ibm.rchland.mfgapps.client.utils.panel.dynamic.IGSTextFieldPanel;
import com.ibm.rchland.mfgapps.mfsclient.struct.MFSLabelTrigInp;
import com.ibm.rchland.mfgapps.mfsclient.struct.MFSOnDemandKeyData;
import com.ibm.rchland.mfgapps.mfscommon.exception.MFSException;

/**
 * <code>MFSFieldPanelBuilder</code> wrapper for <code>IGSTextFieldPanel</code>
 * using <code>MFSOnDemandKeyData</code>.
 * @author The MFS Client Development Team
 */
public class MFSFieldPanelBuilder 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4663339197949945542L;
	
	/** A list of <code>MFSLabelTringInp</code>s. */
	private ArrayList<MFSLabelTrigInp> trigInpList;

	/** The <code>MFSTriggerData</code> containing all configurations for on demand printing */
	private MFSTriggerData triggerData = MFSTriggerData.getInstance();

	/** The <code>MFSOnDemandKeyData</code> for on demand printing */
	private MFSOnDemandKeyData odkData;

	/** The <code>IGSTextFieldPanel</code> created dynamically */
	private IGSTextFieldPanel tfp;
	
	/** The title for the <code>IGSTextFieldPanel</code> */
	private String title;
	
	/** The <code>IGSTextFieldPanelProperties</code> that contains alignment properties for the panel */
	private IGSTextFieldPanelProperties tfpp;

	/**
	 * Creates a new <code>MFSFieldPanelBuilder</code> which creates a
	 * <code>IGSTextFieldPanel</code> from  a <code>MFSOnDemandKeyData</code>
	 * @param title of the <code>IGSTextFieldPanel</code>
	 * @param odkData key data for on demand printing
	 * @throws MFSException
	 */
	public MFSFieldPanelBuilder(String title, MFSOnDemandKeyData odkData, 
			IGSTextFieldPanelProperties tfpp)
		throws MFSException 
	{
		this.title = title;
		this.odkData = odkData;
		this.tfpp = tfpp;
		
		initialize();
		createFieldPanel();
	}	
	
	/** Creates an instance of <code>IGSTextFieldPanel</code> and keeps the reference for 
	 * later use.
	 * @throws MFSException
	 */
	private void createFieldPanel() 
		throws MFSException 
	{
		try
		{
			// Sort Text Fields
			sortTriggerInputs();
			
			// Get the field names and make an array to build the IGSTextFieldPanel
			int inputListSize = trigInpList.size();
	
			String[] fieldNames = new String[inputListSize];
			Class<?>[] fieldTypes = new Class<?>[inputListSize];
	
			for (int inputNumber = 0; inputNumber < inputListSize; inputNumber++) 
			{
				MFSLabelTrigInp trigInp = trigInpList.get(inputNumber);
				
				String reqf = "";
	
				if (trigInp.isRequired()) 
				{
					// fields of type B = Barcode can't be required
					if(trigInp.getType().equals("B"))
					{
						StringBuffer sb = new StringBuffer();
						sb.append("Error: Trigger Input of Type 'B' can not be required\n");
						sb.append("LBTV = ");
						sb.append(trigInp.getValue());
						
						throw new MFSException(sb.toString(), false);
					}
					else
					{
						reqf = "* ";
					}
				}
	
				fieldNames[inputNumber] = reqf + trigInp.getText();
				fieldTypes[inputNumber] = JFormattedTextField.class;
			}
	
			// try to create the panel, may throw an exception
			tfp = new IGSTextFieldPanel(title, fieldNames, fieldTypes, tfpp);
		}
		catch(MFSException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			throw new MFSException(e.getMessage(), false);
		}
	}

	/**
	 * @return the <code>MFSOnDemandKeyData</code>
	 */
	public MFSOnDemandKeyData getOdkData() {
		return odkData;
	}

	/**
	 * @return the <code>IGSTextFieldPanel</code>
	 */
	public IGSTextFieldPanel getTextFieldPanel() {
		return tfp;
	}

	/**
	 * @return the trigInpList
	 */
	public ArrayList<MFSLabelTrigInp> getTrigInpList() {
		return trigInpList;
	}
	
	/* Initialize the field panel builder */
	private void initialize()
		throws MFSException
	{
		// Get the list of fields					
		trigInpList = triggerData.rtvTrigInputs(odkData.getTriggerSource(), odkData.getLabelType());	
	}
	
	/**
	 * Bubble Sort implementation to sort the list of trigger inputs.
	 */
	private void sortTriggerInputs()
	{
		int inputListSize = trigInpList.size();
		
		// Proceed if more than one triggers are received
		if(1 < inputListSize)
		{			
			MFSLabelTrigInp[] ordTrigInps = new MFSLabelTrigInp[inputListSize];
			ordTrigInps = trigInpList.toArray(ordTrigInps);
					
			for(int pass = 1; pass < ordTrigInps.length; pass++)
			{
				for(int index = 0; index < ordTrigInps.length - pass; index++)
				{
					if(ordTrigInps[index].getDisplayOrder() > ordTrigInps[index + 1].getDisplayOrder())
					{
						MFSLabelTrigInp trigInp = ordTrigInps[index];
						ordTrigInps[index] = ordTrigInps[index + 1];
						ordTrigInps[index + 1] = trigInp;
					}
				}
			}			
			
			// Clear the current list
			trigInpList.clear();
			
			// Add the ordered trigger inputs
			for(MFSLabelTrigInp trigInp : ordTrigInps)
			{
				trigInpList.add(trigInp);
			}			
		}		
	}
}
