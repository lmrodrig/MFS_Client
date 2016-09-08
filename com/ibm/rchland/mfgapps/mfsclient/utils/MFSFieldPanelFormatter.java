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

import javax.swing.JLabel;
import javax.swing.JTextField;

import com.ibm.rchland.mfgapps.client.utils.IGSMaxLengthDocument;
import com.ibm.rchland.mfgapps.client.utils.panel.dynamic.IGSTextFieldPanel;
import com.ibm.rchland.mfgapps.mfsclient.struct.MFSLabelTrigInp;
import com.ibm.rchland.mfgapps.mfsclient.struct.MFSOnDemandKeyData;
import com.ibm.rchland.mfgapps.mfscommon.exception.MFSException;

/**
 * <code>MFSFieldPanelFormatter</code> wrapper for <code>MFSFieldPanelBuilder</code>
 * @author The MFS Client Development Team
 */
public class MFSFieldPanelFormatter
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4663339197949945542L;
	
	/** A <code>MFSFieldPanelBuilder</code> that contains a <code>IGSTextFieldPanel</code> */
	private MFSFieldPanelBuilder fpb;

	/** The <code>MFSTriggerData</code> containing all configurations for on demand printing */
	private MFSTriggerData triggerData = MFSTriggerData.getInstance();
	
	/** The <code>StringBuffer</code> containing the input data like xml */
	private StringBuffer sbInputData;

	/** The numeric regex for validations */
	public final String NUMERIC = "[0-9]*";
	
	/**
	 * Creates a new <code>MFSFieldPanelFormatter</code> which is a wrapper for
	 * <code>IGSTextFieldPanel</code>
	 * @param title of the <code>IGSTextFieldPanel</code>
	 * @param odkData key data for on demand printing
	 * @throws MFSException 
	 */
	public MFSFieldPanelFormatter(MFSFieldPanelBuilder fpb) 
		throws MFSException
	{
		this.fpb = fpb;
		
		formatFieldPanel();
	}	
	
	/**
	 * Format all fields in the <code>IGSTextFieldPanel</code>
	 * @param trigInpList the list containing the format for all text fields
	 * @throws MFSException
	 */
	private void formatFieldPanel()
		throws MFSException 
	{				
		final JLabel[] fieldLabels = fpb.getTextFieldPanel().getLabels();
		final JTextField[] textFields = fpb.getTextFieldPanel().getTextFields();
		final ArrayList<MFSLabelTrigInp> trigInpList = fpb.getTrigInpList();
		final MFSOnDemandKeyData odkData = fpb.getOdkData();
	
		// Get each text Field in the IGSTextFieldPanel and gives specific
		// format
		for (int index = 0; index < textFields.length; index++) 
		{
			MFSLabelTrigInp trigInp = trigInpList.get(index);

			// Change the name and look of the text fields
			textFields[index].setName(trigInp.getValue());
			textFields[index].setFont(MFSConstants.MEDIUM_MONOSPACED_FONT);
			
			/* Change the max length
			 * Types A and D are display only and the values are retrieved automatically,
			 * need no validations. Type D is display only and the value is taken
			 * from a decoded barcode, U is taken from decoded and input so the max length 
			 * is not needed here...but will be checked later.
			 */
			if(trigInp.getType().equals("N") || trigInp.getType().equals("T"))
			{
				textFields[index].setDocument(new IGSMaxLengthDocument(trigInp.getMaxLength()));
			}

			/* Special logic for types B,N and T is not needed. 
			 * B = Barcode Input to scan and decode.
			 * U = User can input or decoded value from barcode input field
			 * D = Decoded value only from barcode input field	 
			 * N = Numeric user input value only
			 * T = Text (Alphanumeric) user input value only
			 */
			if(trigInp.getType().equals("D") || trigInp.getText().equals("U"))
			{
				if (trigInp.getType().equals("D")) 
				{
					textFields[index].setEditable(false);
				} 
				
				// try to Retrieve sourceData method from TDEF to validate it exists
				triggerData.rtvDataSourcePath(odkData.getTriggerSource(), trigInp.getValue());					
			}
			else if (trigInp.getType().equals("R") || trigInp.getType().equals("A"))
			{
				/* R = Retrieve data from client using TDEF10 configuration and
				 * display only to user in dialog box. 
				 */
				if (trigInp.getType().equals("R")) 
				{
					textFields[index].setEditable(false);
				} 
				/* A = Automatically pass value retrieved from the client using
				 * the TDEF10 configuration without displaying to the user in a dialog box. 
				 */
				else 
				{
					textFields[index].setVisible(false);
					fieldLabels[index].setVisible(false);
				}
				
				// Retrieve dataSource from TDEF
				String dataSourcePath = triggerData.rtvDataSourcePath(odkData.getTriggerSource(),
																			trigInp.getValue());
				// Get methodName
				int methodIndex = -1;
				
				if(-1 != (methodIndex = dataSourcePath.lastIndexOf(".")))
				{
					String methodName = dataSourcePath.substring(methodIndex + 1);
					
					// Retrieve sourceData from method defined in TDEF
					String sourceData = triggerData.rtvSourceData(odkData.getDataSource(), methodName);
					textFields[index].setText(sourceData);
				}
				else
				{
					String erms = "Error: Data structure in TDEF file for field DATF" +
							"should be like 'Class.Method'";
					
					throw new MFSException(erms, false);
				}				
			}
		} // end for
	}
	
	/**
	 * @return the <code>MFSOnDemandKeyData</code>
	 */
	public MFSOnDemandKeyData getOdkData() {
		return fpb.getOdkData();
	}

	/**
	 * @return the <code>IGSTextFieldPanel</code>
	 */
	public IGSTextFieldPanel getTextFieldPanel() {
		return fpb.getTextFieldPanel();
	}
	
	/**
	 * @return the trigInpList
	 */
	public ArrayList<MFSLabelTrigInp> getTrigInpList() {
		return fpb.getTrigInpList();
	}
	
	/**
	 * Check if the given fieldName is of barcode type.
	 * @param fieldName the <code>JTextField</code> name
	 * @return true if is input type is 'B', else false
	 */
	public boolean isBarcodeField(String fieldName)
	{
		boolean isBarcode = false;
		
		final ArrayList<MFSLabelTrigInp> trigInpList = fpb.getTrigInpList();
		
		for(MFSLabelTrigInp trigInp : trigInpList)
		{		
			if(trigInp.getValue().equals(fieldName) && trigInp.getType().equals("B"))
			{
				isBarcode = true;
				break;
			}
		}
		
		return isBarcode;
	}	
	
	/**
	 * Validate all input values in the <code>IGSTextFieldPanel</code> and if no
	 * exception is thrown, returns the input data
	 * @return the <code>String</code> in xml.
	 * @throws MFSException
	 */
	public String rtvInputData() 
		throws MFSException
	{
		validateInputData();
		
		return sbInputData.toString();
	}
	
	/**
	 * The source that contains decoded values to populate all types of
	 * barcode fields. The source is originally a <code>MFSBCPartObject</code>, but
	 * could be a different source.
	 * @param source the object that contains decoded values.
	 * @throws MFSException
	 */
	public void setBCDecodedValues(Object source)
		throws MFSException
	{
		final JTextField[] textFields = fpb.getTextFieldPanel().getTextFields();
		final ArrayList<MFSLabelTrigInp> trigInpList = fpb.getTrigInpList();
		final MFSOnDemandKeyData odkData = fpb.getOdkData();
			
		//Only input fields of type D and U are populated here
		for(int index = 0; index < textFields.length; index++)
		{
			MFSLabelTrigInp trigInp = trigInpList.get(index);
			
			if(trigInp.getType().equals("D") || trigInp.getType().equals("U"))
			{
				String dataSourcePath = triggerData.rtvDataSourcePath(odkData.getTriggerSource(),
																			trigInp.getValue());				
				//Add the barcode package
				dataSourcePath = "barcode." + dataSourcePath;
								
				// Get the className and methodName
				int methodIndex = -1;
				
				if(-1 != (methodIndex = dataSourcePath.lastIndexOf(".")))
				{
					String clazzName = dataSourcePath.substring(0, methodIndex);
					String methodName = dataSourcePath.substring(methodIndex + 1);

					/* The source can't be stored in the MFSOnDemandKeyData because a 
					 * MFSBCBarcode was created within the component that called this method. 
					 * The source received as paremeter should be used instead.
					 */
					if(MFSInvoker.isInstance(source, clazzName))
					{
						String sourceData = triggerData.rtvSourceData(source, methodName);
						
						// Set the decoded value only if it really changed
						if(!sourceData.equals(""))
						{
							textFields[index].setText(sourceData);		
						}
					}
				}
				else
				{
					String erms = "Error: Data structure in TDEF file for field DATF = " +
							dataSourcePath + " should be like 'Class.Method'";
					
					throw new MFSException(erms, false);
				}
			}
		}
	}	
	
	/**
	 * Validate all field values in the <code>IGSTextFieldPanel</code> with the
	 * specific <code>MFSLabelTrigInp</code> data.
	 * @throws MFSException
	 */
	private void validateInputData()
		throws MFSException
	{
		final String[] fieldValues = fpb.getTextFieldPanel().getTextFieldValues();
		final ArrayList<MFSLabelTrigInp> trigInpList = fpb.getTrigInpList();
		
		String erms = null;
		
		//create the input data buffer
		sbInputData = new StringBuffer("");
		
		for(int index = 0; index < fieldValues.length; index++)
		{
			MFSLabelTrigInp trigInp = trigInpList.get(index);			
			String fieldValue = fieldValues[index].trim();
			
			// Only input fields need validation
			if(trigInp.getType().equals("U") || trigInp.getType().equals("D") ||
					trigInp.getType().equals("N") || trigInp.getType().equals("T"))
			{
				int fieldLength = fieldValue.length();
				
				if(trigInp.isRequired())
				{
					if(fieldValue.equals(""))
					{
						erms = "Error: Field '" + trigInp.getText() + "' is required";							
						throw new MFSException(erms, false);
					}
				}
				
				// Check min and max length, then numeric
				if(!fieldValue.equals(""))
				{
					if(!(trigInp.getMinLength() <= fieldLength &&
							fieldLength <= trigInp.getMaxLength()))
					{
						erms = "Error: min and/or max length don't match for '" +
									trigInp.getText() + "' field";
						throw new MFSException(erms, false);
					}	
					
					if(trigInp.getType().equals("N") && !fieldValue.matches(NUMERIC))
					{

						erms = "Error: value must be numeric for '" + trigInp.getText() + "' field";						
						throw new MFSException(erms, false);					
					}
				}				
			}
			
			// Data is good to print	
			if(!trigInp.getType().equals("B"))
			{
				sbInputData.append("<"); 
				sbInputData.append(trigInp.getTag()); 
				sbInputData.append(">");
				sbInputData.append(fieldValue);
				sbInputData.append("</"); 
				sbInputData.append(trigInp.getTag()); 
				sbInputData.append(">");
			}
		}		
	}	
}