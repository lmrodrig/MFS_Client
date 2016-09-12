/* @ Copyright IBM Corporation 2010. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2010-03-14       42558JL Santiago SC      -Initial version, Java 5.0
 * 2012-01-10 ~01   D634496 Edgar Mercado    -Allow printing for trigger function *NONE
 ******************************************************************************/

package com.ibm.rchland.mfgapps.mfsclient.utils;

import java.util.ArrayList;

import com.ibm.rchland.mfgapps.client.utils.messagebox.IGSMessageBox;
import com.ibm.rchland.mfgapps.mfsclient.MFSFrame;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSOnDemandDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSSelectLabelDialog;
import com.ibm.rchland.mfgapps.mfsclient.struct.MFSLabelTrigInp;
import com.ibm.rchland.mfgapps.mfsclient.struct.MFSLabelTrigs;
import com.ibm.rchland.mfgapps.mfsclient.struct.MFSOnDemandKeyData;
import com.ibm.rchland.mfgapps.mfsclient.struct.MFSPlom;
import com.ibm.rchland.mfgapps.mfscommon.MFSConfig;
import com.ibm.rchland.mfgapps.mfscommon.exception.MFSException;

/**
 * <code>MFSFieldPanelBuilder</code> wrapper for <code>IGSTextFieldPanel</code>
 * using <code>MFSOnDemandKeyData</code>.
 * @author The MFS Client Development Team
 */
public class MFSOnDemand 
{	
	/** The <code>MFSOnDemandKeyData</code> to print labels*/
	private MFSOnDemandKeyData odKeyData;
	
	/** The <code>JFrame</code> to display the components */
	private MFSFrame parentFrame;
	
	/** The <code>MFSTriggerData</code> util */
	final MFSTriggerData triggerData = MFSTriggerData.getInstance();
	
	/**
	 * Creates a new <code>MFSOnDemand</code> for on demand operations.
	 * @param parentFrame the parent frame that will display the error msgs
	 * @param odKeyData
	 */
	public MFSOnDemand(MFSFrame parentFrame, MFSOnDemandKeyData odKeyData)
	{
		this.parentFrame = parentFrame;
		this.odKeyData = odKeyData;
	}
	
	/**
	 * Automatic Printing for <code>MFSOnDemandKeyData</code> based on TINP file.
	 * @param odKeyData the on demand key data
	 * @throws MFSException
	 */
	public void automaticPrinting() 
		throws MFSException
	{
		StringBuffer labelData = new StringBuffer();
		
		// Get all trigger inputs
		ArrayList<MFSLabelTrigInp> trigInpList = triggerData.rtvTrigInputs(odKeyData.getTriggerSource(),
																			odKeyData.getLabelType());		
		
		for(MFSLabelTrigInp trigInp : trigInpList)
		{
			String dataSourcePath = triggerData.rtvDataSourcePath(odKeyData.getTriggerSource(),
																			trigInp.getValue());
			// Get methodName				
			int methodIndex = -1;
			
			if(-1 != (methodIndex = dataSourcePath.lastIndexOf(".")))
			{
				String methodName = dataSourcePath.substring(methodIndex + 1);

				// Retrieve sourceData from method defined in TDEF
				String sourceData = triggerData.rtvSourceData(odKeyData.getDataSource(), 
																			methodName);
				// create data tag
				labelData.append("<"); 
				labelData.append(trigInp.getTag()); 
				labelData.append(">");
				labelData.append(sourceData);
				labelData.append("</"); 
				labelData.append(trigInp.getTag()); 
				labelData.append(">");					
			}
			else
			{
				String erms = "Error: Data structure in TDEF file for field DATF" +
							dataSourcePath + " should be like 'Class.Method'";
				
				throw new MFSException(erms, false);
			}			
		}
		// Get the qty
		int qty = getLabelQty(odKeyData.getLabelType(), odKeyData.getTriggerSource(), 
														odKeyData.getTriggerKey());		
		
		// The necessary info was collected for printing
		odKeyData.setQty(qty);			
		odKeyData.setLabelData(labelData.toString());
		
		// Print here
		MFSPrintingMethods.onDemandLabel(odKeyData, getParentFrame());	
	}
	
	/**
	 * Get the label quantity of the config entry
	 * @param labelType the label type
	 * @param triggerSource the trigger source
	 * @param keyElement the key element
	 * @return an int for the label quantity
	 */
	public int getLabelQty(String labelType, String triggerSource, String keyElement)
	{
		final MFSConfig config = MFSConfig.getInstance();
		
		int qty = 1;
	
		String configHeader = labelType + "," + triggerSource;
		
		String value = config.getConfigValue(configHeader + "," + keyElement);
		
		if (value.equals(MFSConfig.NOT_FOUND))
		{
			value = config.getConfigValue(configHeader + ",*ALL");
		}
		
		if (value.equals(MFSConfig.NOT_FOUND))
		{
			value = config.getConfigValue(configHeader + ",*NONE");
		}
		
		if (value.equals(MFSConfig.NOT_FOUND))
		{
			value = config.getConfigValue(configHeader);
		}

		if (!value.equals(MFSConfig.NOT_FOUND))
		{
			if (!value.equals("")) //$NON-NLS-1$
			{
				qty = Integer.parseInt(value);
			}
		}
		
		return qty;
	}
	
	/**
	 * @return the odKeyData
	 */
	public MFSOnDemandKeyData getOdkData() {
		return odKeyData;
	}
	
	/**
	 * @return the parentFrame
	 */
	public MFSFrame getParentFrame() {
		return parentFrame;
	}
	
	/**
	 * Check  all input field types for a given trigger source and label type and 
	 * determines if input is required. If all input field types are type A then input
	 * is not required.
	 * @param triggerSource the label trigger source
	 * @param labelType the label type
	 * @return true if input is required, else false
	 */
	public boolean isInputRequired(String triggerSource, String labelType)
	{
		boolean isRequired = false;
		
		ArrayList<MFSLabelTrigInp> trigInpList = triggerData.rtvTrigInput(triggerSource, labelType);
		
		if(null != trigInpList)
		{
			for(MFSLabelTrigInp trigInp : trigInpList)
			{
				// if ONE input field is not type A, then input is required.
				if(!trigInp.getType().equals("A"))
				{
					isRequired = true;
					break;
				}				
			}
		}
		
		return isRequired;
	}
	
	/**
	 * Label reprint process for on demand printing.
	 * @throws MFSException
	 */
	public void labelReprint() 
		throws MFSException
	{
		final String triggerSource = odKeyData.getTriggerSource();
		final String triggerKey = odKeyData.getTriggerKey();
		
		MFSSelectLabelDialog labelDialog = new MFSSelectLabelDialog(
				getParentFrame(), "Plant of Manufacture and Label Type",
				"Select the PLOM and label type to print", "PLOM : ");			
		
		/* Get plom's here*/
		ArrayList<String> plomConfigs = MFSConfig.getInstance().getPlomConfigs();
		
		/* If found PLOM configs */
		if(null != plomConfigs)
		{	
			/* Process plom's here*/
			for(String plomConfig : plomConfigs)
			{
				MFSPlom plomObject = new MFSPlom(plomConfig);
				labelDialog.addComboItem(plomObject.getPlom() + " " + plomObject.getPlnt());
			}				
			
			/* This panel supports reprint, so we don't have a triggerKey.
			 * Use *NONE as the default triggerKey.
			 */
			ArrayList<String> validLabels = triggerData.rtvLabels(triggerSource, triggerKey);
							
			if(null != validLabels)
			{
				/* Initialize the default list (visible) in the SelectLabel dialog */
				labelDialog.initializeDefaultList();
				
				/* Populate the labels list in the SelectLabel dialog */					
				labelDialog.addListItems(validLabels.toArray());
				
				labelDialog.sortListAlphaNumerically();

				/* Configure the SelectLabel dialog */				
				labelDialog.setSize(420, 384);		
				labelDialog.setDefaultSelection("FIRST");
				
				do
				{
					labelDialog.setProceedSelected(false);
					labelDialog.setLocationRelativeTo(getParentFrame());
					labelDialog.setVisible(true);
					
					if(labelDialog.getProceedSelected())
					{
						String plom = labelDialog.getSelectedComboOption();
						plom = plom.substring(0,3); //Just get the plom not the plant name.
						
						/* The necessary info was collected. */
						odKeyData.setLabelType(labelDialog.getSelectedListOption());
						odKeyData.setPlom(plom);
						
						// Check if input is required
						if(!isInputRequired(triggerSource, odKeyData.getLabelType()))
						{
							automaticPrinting();
						}
						else
						{
							/* Pass the keyData reference to the dialog */
							MFSOnDemandDialog odDialog = new MFSOnDemandDialog(getParentFrame(), 
																				"On Demand Label Printing",
																				odKeyData);
							odDialog.setVisible(true);
							
							if(odDialog.isPrintRequired())
							{
								/* The MFSOnDemandKeyData has been changed by the MFSOnDemandDialog @*/
								MFSPrintingMethods.onDemandLabel(odKeyData, getParentFrame());		
							}							
						}
					} // if proceedSelected
				}
				while(!labelDialog.getCancelSelected());
			}
			else // labels
			{
				throw new MFSException("No Labels were found. (RTVLBLTRIG)", false);		
			}
		} // end if PLOM's found
		else
		{
			throw new MFSException("No PLOM configurations were loaded. (RTV_PLOM)", false);				
		}
	}		
	
	/**
	 * Label Trigger process for on demand printing.
	 */
	public void labelTrigger() throws MFSException
	{
		final String triggerSource = odKeyData.getTriggerSource();		
		final MFSTriggerData triggerData = MFSTriggerData.getInstance();
		final MFSConfig configs = MFSConfig.getInstance();
		
		// Check if there are triggers
		if(triggerData.containsTrigger(triggerSource))
		{				
			// Get the triggers, because the keyElements are needed
			ArrayList<MFSLabelTrigs> lblTrigList = triggerData.rtvTriggers(triggerSource);
			
			for(MFSLabelTrigs lblTrig : lblTrigList)			
			{						
				try
				{
					// Retrieve dataSource from TDEF for the keyElement
					String dataSourcePath = triggerData.rtvDataSourcePath(triggerSource, 
																		lblTrig.getKeyElement());												
					// Get methodName				
					int methodIndex = -1;
					
					if((-1 != (methodIndex = dataSourcePath.lastIndexOf("."))) || dataSourcePath.equals("*NONE"))  //~01C
					{
						String methodName = dataSourcePath.substring(methodIndex + 1);
		                
						String sourceData;                     //~01A
						if (dataSourcePath.equals("*NONE"))    //~01A
						{                                      //~01A
							sourceData = "*NONE";              //~01A
						}                                      //~01A
						else                                   //~01A
						{                                      //~01A
							// Retrieve sourceData from method defined in TDEF
							sourceData = triggerData.rtvSourceData(odKeyData.getDataSource(), 
																						methodName);	//~01C						
						}                                       //~01A

		
						/* Check if the config exists in CONF file before start printing.
						 * labelType,triggerSource,keyElement
						 */										
						ArrayList<String> labelTypes = lblTrig.getLabelTypes();				
						
						for(String labelType : labelTypes)
						{
							StringBuffer configEntry = new StringBuffer();
							configEntry.append(labelType);
							configEntry.append(",");
							configEntry.append(triggerSource);
							configEntry.append(",");
							configEntry.append(sourceData);
							
							// only if the config is found in CONF file try to print the label
							if(configs.containsConfigEntry(configEntry.toString()))
							{
								// The necessary info was collected for printing
								odKeyData.setLabelType(labelType);
								odKeyData.setTriggerKey(lblTrig.getKeyElement());					
								
								// Check if input is required
								if(!isInputRequired(triggerSource, odKeyData.getLabelType()))
								{
									automaticPrinting();
								}
								else
								{
									/* Pass the keyData reference to the dialog */
									MFSOnDemandDialog odDialog = new MFSOnDemandDialog(getParentFrame(), 
																						"On Demand Label Printing",
																						odKeyData);
									odDialog.setVisible(true);
									
									if(odDialog.isPrintRequired())
									{
										/* The MFSOnDemandKeyData has been changed by the MFSOnDemandDialog @*/
										MFSPrintingMethods.onDemandLabel(odKeyData, getParentFrame());		
									}							
								}							
							}
						}								
					}
					else
					{
						String erms = "Error: Data structure in TDEF file for field DATF" +
									dataSourcePath + " should be like 'Class.Method'";
						
						throw new MFSException(erms, false);
					}				
				}
				catch(Exception e)
				{
					IGSMessageBox.showOkMB(getParentFrame(), null, e.getMessage(), null);
				}
			}
		}	// end for triggers
	}	// end check triggers	
}
