/* @ Copyright IBM Corporation 2010. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2010-09-06       48749JL Santiago SC      -Initial version, Java 5.0
 ******************************************************************************/

package com.ibm.rchland.mfgapps.mfsclient.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.ibm.rchland.mfgapps.client.utils.messagebox.IGSMessageBox;
import com.ibm.rchland.mfgapps.client.utils.xml.IGSXMLTransaction;
import com.ibm.rchland.mfgapps.mfsclient.MFSFrame;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSCntrDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSHazmatDialog;
import com.ibm.rchland.mfgapps.mfsclient.struct.MFSHazmatCntr;
import com.ibm.rchland.mfgapps.mfsclient.struct.MFSHazmatPn;
import com.ibm.rchland.mfgapps.mfscommon.MFSActionable;
import com.ibm.rchland.mfgapps.mfscommon.MFSConfig;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSComm;

/**
 * <code>MFSHazmatLabeling</code> is used to display the hazardous materials label
 * status for containers.
 * @author The MFS Client Development Team
 */
public class MFSHazmatLabeling 
{
	/** Status verification required */
	public final static int STATUS_VR = 0;
	
	/** Status successfully verified */
	public final static int STATUS_SV = 1;
	
	/** Status remove required */
	public final static int STATUS_RR = -1;
	
	/** Status successfully removed */
	public final static int STATUS_SR = -2;
	
	/**
	 * Finds the Hazmat trigger configuration
	 * @param nmbr the operation number
	 * @param prln the product line
	 * @return the hazmat trigger else null
	 */
	public static String findHazmatTrigger(String nmbr, String prln)
	{
		boolean isTrigger = true;
		
		StringBuilder hazmatTrigger = new StringBuilder("HAZMAT,");
		hazmatTrigger.append(nmbr.trim());
		hazmatTrigger.append(",");
		hazmatTrigger.append(prln.trim());
		
		if(!MFSConfig.getInstance().containsConfigEntry(hazmatTrigger.toString()))
		{
			System.out.println("Trigger: " + hazmatTrigger.toString() + " NOT found.");
			
			hazmatTrigger = new StringBuilder("HAZMAT,");
			hazmatTrigger.append(nmbr.trim());
			hazmatTrigger.append(",*ALL");

			if(!MFSConfig.getInstance().containsConfigEntry(hazmatTrigger.toString()))
			{
				System.out.println("Trigger: " + hazmatTrigger.toString() + " NOT found.");
				isTrigger = false;
			}
		}
		
		return (isTrigger)? hazmatTrigger.toString() : null;
	}
	
	/**
	 * Gets the hazmat trigger value
	 * @param nmbr the operation number
	 * @param prln the product line
	 * @return the value of the hazmat trigger
	 */
	public static String getHazmatTriggerValue(String nmbr, String prln)
	{
		String trigger = null;
		
		if(null != (trigger = findHazmatTrigger(nmbr, prln)))
		{
			trigger = MFSConfig.getInstance().getConfigValue(trigger);
		}
		
		return trigger;
	}
	
	/**
	 * Checks if the <code>MFSHazmatCntr</code> is completed when all its parts are in the
	 * correct status.
	 * @param hazmatCntr
	 * @return true if the container is completed
	 */
	public static boolean isCntrCompleted(MFSHazmatCntr hazmatCntr)
	{
		Collection<MFSHazmatPn> pns = hazmatCntr.getHazmatPns().values();
		
		for(MFSHazmatPn pn : pns)
		{
			switch(pn.getStatus())
			{
				case STATUS_VR: // falling
				case STATUS_RR: 	
					return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Check for the Hazmat Trigger location
	 * @param nmbr the operation number
	 * @param prln the product line
	 * @return boolean if the trigger was found
	 */
	public static boolean isHazmatTrigger(String nmbr, String prln)
	{
		return (null == findHazmatTrigger(nmbr, prln))? false : true;
	}

	/** Enables verification for hazmat labeling */
	private boolean verificationEnabled = true;	

	/** The frame that requires hazmat labeling verification */
	private MFSFrame parentFrame;

	/** The actionable that displays the progress bar */
	private MFSActionable actionable;
	
	/** The label trigger source */
	private String triggerSource;
	
	/** Optional parameters for RTV_HAZCTR */
	private String optionalParameters;

	/**
	 * Creates a new <code>MFSHazmatLabeling</code> checkup
	 * @param parentFrame
	 * @param actionable for progress bar indicator
	 * @param dialogDisplayOnly true if dialog is diaplay only, false if verification
	 */
	public MFSHazmatLabeling(MFSFrame parentFrame, MFSActionable actionable)
	{
		this.parentFrame = parentFrame;
		this.actionable = actionable;
	}

	/**
	 * @return the optionalParameters
	 */
	public String getOptionalParameters() {
		return optionalParameters;
	}

	/**
	 * @return the parentFrame
	 */
	public MFSFrame getParentFrame() 
	{
		return parentFrame;
	}

	/**
	 * @return the triggerSource
	 */
	public String getTriggerSource() {
		return triggerSource;
	}

	/**
	 * Retrieves the hazmat label part numbers for each top level container to verify 
	 * all hazmat containers.
	 * @param cntrList the top level containers
	 * @param mode to retrieve the hazmat label part numbers
	 * @return true if hazmat labeling verification was completed
	 */
	public boolean hazmatLabeling(List<String> cntrList, String mode)
	{
		boolean proceed = false;
		
		if(null != cntrList)
		{
			if(cntrList.isEmpty())
			{
				String msg = "No Container(s) Assigned for Hazmat Analysis!\n";	
				IGSMessageBox.showOkMB(getParentFrame(), getClass().getSimpleName(), msg, null);
			}
			// At least one cntr is needed when hazmat verification is triggered
			else
			{
				// Retrieve the hazmat cntr PN's, description and verified dates
				Map<String, MFSHazmatCntr> hazmatCntrMap = rtvHazmatCntrs(cntrList, mode);
					
				// if null an error was detected previously
				if(null != hazmatCntrMap)
				{
					// if empty , there are some cntrs but no verification is required
					if(hazmatCntrMap.isEmpty())
					{
						proceed = true;
					}
					else
					{
						proceed = true; // in case all cntrs are completed
						// Save the old cntrs status
						String[] oldCntrsStatus = new String[hazmatCntrMap.size()];
						MFSHazmatCntr[] hazmatCntrs = new MFSHazmatCntr[hazmatCntrMap.size()];
						hazmatCntrs = hazmatCntrMap.values().toArray(hazmatCntrs);
						
						for(int index = 0; index < hazmatCntrs.length; index++)
						{
							oldCntrsStatus[index] = hazmatCntrs[index].toString();
						}
						
						// If a single container is not verified the verifyCntrs will return false, the user
						// can suspend the work unit but can't end the operation. Verify the cntrs.
						boolean areCntrsCompleted = vrfyHazCntrs(hazmatCntrMap);
						
						// Check if the cntr status has changed
						for(int index = 0; index < hazmatCntrs.length; index++)
						{
							if(oldCntrsStatus[index].equals(hazmatCntrs[index].toString()))
							{
								// Just leave containers that actually changed
								hazmatCntrMap.remove(hazmatCntrs[index].getCntr());
							}
						}
					
						if(!hazmatCntrMap.isEmpty() && isVerificationEnabled())
						{
							// Log/verify the Hazmat Part info
							proceed = logHazCntr(hazmatCntrMap);
						}
					
						if(!areCntrsCompleted)
						{
							String msg = "Hazmat labeling verification is incomplete for some containers!\n";	
							IGSMessageBox.showOkMB(getParentFrame(), getClass().getSimpleName(), msg, null);
							proceed = false;
						}
					}
				}	
			}
		}

		return proceed;
	}
	
	/**
	 * Retrieves the hazmat label part numbers for the given container and verifies the container.
	 * @param cntr the container to be verified
	 * @param mode to retrieve the hazmat label part numbers
	 * @return true if hazmat labeling verification was completed
	 */
	public boolean hazmatLabeling(String cntr, String mode)
	{		
		boolean proceed = false;
		
		// Retrieve the Hazmat container
		Map<String, MFSHazmatCntr> hazmatCntrMap = rtvHazmatCntr(cntr, mode);
		
		// if null an error was detected previously
		if(null != hazmatCntrMap)
		{
			// if empty , there are some cntrs but no verification is required
			if(hazmatCntrMap.isEmpty())
			{
				proceed = true;
			}
			else
			{
				proceed = true; // in case the cntr is completed
				MFSHazmatCntr hazmatCntr = hazmatCntrMap.get(cntr);
				
				boolean isCntrCompleted = MFSHazmatLabeling.isCntrCompleted(hazmatCntr);
			
				// Save the old cntr status
				String oldCntrStatus = hazmatCntr.toString();
				// Verify the Hazmat Container
				if(!isVerificationEnabled() || !isCntrCompleted)
				{
					isCntrCompleted = vrfyHazCntr(hazmatCntr);
				}
				// Check if the cntr status has changed
				if(!oldCntrStatus.equals(hazmatCntr.toString()) && isVerificationEnabled())
				{
					// Log/verify the Hazmat Part info
					proceed = logHazCntr(hazmatCntr);
				}
	
				if(!isCntrCompleted)
				{
					//String msg = "Hazmat label parts in CNTR : " + cntr + " need to be verified";	
					//IGSMessageBox.showOkMB(getParentFrame(), getClass().getSimpleName(), msg, null);
					proceed = false;
				}			
			}
		}
		
		if((null == hazmatCntrMap || hazmatCntrMap.isEmpty()) && !isVerificationEnabled())
		{
			String msg = "NO hazmat label parts were found for CNTR : " + cntr;	
			IGSMessageBox.showOkMB(getParentFrame(), getClass().getSimpleName(), msg, null);
		}
		
		return proceed;
	}
	
	/**
	 * Retrieves the top level containers for the mctl and then retrieves the hazmat label part 
	 * numbers for each hazmat container to verify all hazmat containers.
	 * @param mctl the work unit
	 * @param mfgn the sytem number
	 * @param idss
	 * @param mode to retrieve the hazmat label part numbers
	 * @return true if hazmat labeling verification was completed
	 */
	public boolean hazmatLabeling(String mctl, String mfgn, String idss, String mode)
	{	
		// Retrieve the top level container list
		List<String> cntrList = MFSContainerUtils.rtvTopLvlCntrs(mctl, mfgn, idss, getParentFrame(), actionable);

		return hazmatLabeling(cntrList, mode);
	}

	/**
	 * Retrieves the hazmat label part numbers for the given subassembly with the specified mode.
	 * @param mctl the subassembly to be verified
	 * @param mode to retrieve the hazmat label part numbers
	 * @param displayMode display mode only
	 * @return true if hazmat labeling verification was completed
	 */
	public boolean hazmatLabelingForFFBM(String mctl, String mode)
	{
		boolean proceed = false;
		
		// Retrieve the Hazmat container
		Map<String, MFSHazmatCntr> hazmatCntrMap = rtvHazmatCntrForFFBM(mctl, mode);
		
		// if null an error was detected previously
		if(null != hazmatCntrMap)
		{
			// This happens when the MODE = "", because the WTYP can't be determined in this panel
			if(hazmatCntrMap.containsKey("SUBASSEMBLY"))
			{
				mode = "SUBASSEMBLY";
				hazmatCntrMap.remove(mode);
			}
			
			// if empty , there are some cntrs but no verification is required
			if(hazmatCntrMap.isEmpty())
			{
				proceed = true;
			}
			else
			{
				proceed = true; // in case the cntr is completed
				MFSHazmatCntr hazmatCntr = hazmatCntrMap.get(mctl);
				
				boolean isCntrCompleted = MFSHazmatLabeling.isCntrCompleted(hazmatCntr);
				hazmatCntr.setFFBM(true);
				
				// Save the old cntr status
				String oldCntrStatus = hazmatCntr.toString();
				
				// Verify the Hazmat Container
				if(!isVerificationEnabled() || !isCntrCompleted)
				{
					isCntrCompleted = vrfyHazCntr(hazmatCntr);
				}
				// Check if the cntr status has changed
				if(!oldCntrStatus.equals(hazmatCntr.toString()) && isVerificationEnabled())
				{
					// Log/verify the Hazmat Part info
					proceed = logHazCntr(hazmatCntr);
				}
	
				if(!isCntrCompleted)
				{
					//String msg = "Hazmat label parts in MCTL : " + mctl + " need to be verified";	
					//IGSMessageBox.showOkMB(getParentFrame(), getClass().getSimpleName(), msg, null);
					proceed = false;
				}	
			}
		}
		
		if(null != hazmatCntrMap && !isVerificationEnabled())
		{
			String msg = null;
			
			if((mode.equals("SUBASSEMBLY") && hazmatCntrMap.isEmpty()))
			{
				msg = "NO hazmat label parts were found for MCTL : " + mctl;	
			}
			else if(!mode.equals("SUBASSEMBLY") && hazmatCntrMap.isEmpty())
			{
				msg = "Please input the container number \nif the workunit type is not equal to 'S'";
			}
			
			if(null != msg)
			{
				IGSMessageBox.showOkMB(getParentFrame(), getClass().getSimpleName(), msg, null);
			}
		}
		
		return proceed;
	}

	/**
	 * @return the verificationEnabled
	 */
	public boolean isVerificationEnabled() {
		return verificationEnabled;
	}
	
	/**
	 * Verifies the <code>MFSHazmatPn</code>s of the <code>MFSHazmatCntr</code>s calling the VFY_HAZPRT.
	 * @param plom
	 * @param typz
	 * @param hazmatCntr the hazmat containter which labeling parts are going to be verified
	 * @return true if no error was found in the trx verification
	 */
	private boolean logHazCntr(Map<String, MFSHazmatCntr> hazmatCntrMap)
	{
		boolean verified = false;
		
		try
		{
			IGSXMLTransaction vfy_hazPrt = new IGSXMLTransaction("VFY_HAZPRT"); 
			vfy_hazPrt.setActionMessage("Verifying Hazmat Label Codes/PN's...");
			vfy_hazPrt.startDocument();
			vfy_hazPrt.startElement("HAZINFO");			
			vfy_hazPrt.addElement("USER", MFSConfig.getInstance().get8CharUser());
			
			Collection<MFSHazmatCntr> hazmatCntrs =  hazmatCntrMap.values();
			
			for(MFSHazmatCntr hazmatCntr : hazmatCntrs)
			{
				vfy_hazPrt.startElement("HAZCNTR");
				String tag = hazmatCntr.isFFBM()? "MCTL" : "CNTR";
				vfy_hazPrt.addElement(tag, hazmatCntr.getCntr());
				vfy_hazPrt.startElement("LABELS");
				
				Collection<MFSHazmatPn> hazmatPns = hazmatCntr.getHazmatPns().values();
				
				for(MFSHazmatPn hazmatPn : hazmatPns)
				{
					if(hazmatPn.hasChanged())
					{
						vfy_hazPrt.startElement("LABEL");
						vfy_hazPrt.addElement("PART", hazmatPn.getPn());
						vfy_hazPrt.addElement("STMP", hazmatPn.getVerifiedDate());
					
						switch(hazmatPn.getStatus())
						{
							case STATUS_SR: vfy_hazPrt.addElement("ACTN", "D"); break;
							case STATUS_VR: vfy_hazPrt.addElement("ACTN", "A"); break;
							case STATUS_SV: vfy_hazPrt.addElement("ACTN", "I"); break;
						}
						
						vfy_hazPrt.endElement("LABEL");
					}
				}
				
				vfy_hazPrt.endElement("LABELS");
				vfy_hazPrt.endElement("HAZCNTR");
			}
			
			vfy_hazPrt.endElement("HAZINFO");
			vfy_hazPrt.endDocument();

			MFSComm.execute(vfy_hazPrt);

			if (0 == vfy_hazPrt.getReturnCode()) 
			{	
				verified = true;
			}
			else
			{
				IGSMessageBox.showOkMB(getParentFrame(), getClass().getSimpleName(), vfy_hazPrt.getErms(), null);
			}
		}
		catch(Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), getClass().getSimpleName(), e.getMessage(), null);
		}
		
		return verified;
	}
	
	/**
	 * Verifies the <code>MFSHazmatPn</code>s of the <code>MFSHazmatCntr</code> calling the VFY_HAZPRT.
	 * @param plom
	 * @param typz
	 * @param hazmatCntr the hazamat containter which labeling parts are going to be verified
	 * @return true if no error was found in the trx verification
	 */
	public boolean logHazCntr(MFSHazmatCntr hazmatCntr)
	{
		Map<String, MFSHazmatCntr> hazmatCntrMap = new LinkedHashMap<String, MFSHazmatCntr>();
		hazmatCntrMap.put(hazmatCntr.getCntr(), hazmatCntr);
		return logHazCntr(hazmatCntrMap);
	}
	
	/**
	 * Retrieve a specific Hazmat Container
	 * @param cntr the container number
	 * @param mode (over pack or prod pack)
	 * @return the <code>Map</code>
	 */
	public Map<String, MFSHazmatCntr> rtvHazmatCntr(String cntr, String mode)
	{				
		ArrayList<String> cntrList = new ArrayList<String>();
		cntrList.add(cntr);
		
		// Retrieve the Hazmat PN's for this container
		return rtvHazmatCntrs(cntrList, mode);
	}
	
	/**
	 * Retrieve the Hazmat Containers for a FFBM subassembly
	 * @param mctl the work unit number
	 * @param mode (over pack or prod pack)
	 * @return the <code>Map</code>
	 */
	public Map<String, MFSHazmatCntr> rtvHazmatCntrForFFBM(String mctl, String mode)
	{
		StringBuilder sb = new StringBuilder("<MCTL>");
		sb.append(mctl); 
		sb.append("</MCTL>");

		// Retrieve the Hazmat PN's for this container
		return rtvHazmatCntrs(sb.toString(), mode);
	}
	
	/**
	 * Retrieve the Hazmat Containers
	 * @param cntrList the container list
	 * @param mode (over pack or prod pack)
	 * @return a map of <code>MFSHazmatCntr</code>
	 */
	public Map<String, MFSHazmatCntr> rtvHazmatCntrs(List<String> cntrList, String mode)
	{
		StringBuilder sb = new StringBuilder("<CNTRS>");
		
		for(String cntr : cntrList)
		{
			sb.append("<CNTR>"); sb.append(cntr); sb.append("</CNTR>");
		}
		
		sb.append("</CNTRS>");
		
		// Retrieve the Hazmat PN's of each container
		return rtvHazmatCntrs(sb.toString(), mode);
	}

	/**
	 * Executes the RTV_HAZCNTR tx to retrieve a map of <code>MFSHazmatCntr</code>s
	 * @param cntrs the container(s) xml string
	 * @param mode (over pack or prod pack)
	 * @return a map of <code>MFSHazmatCntr</code>s
	 */
	private Map<String, MFSHazmatCntr> rtvHazmatCntrs(String hazInfo, String mode)
	{
		Map<String, MFSHazmatCntr> hazmatCntrMap = null;
		
		try
		{
			IGSXMLTransaction rtvHazCntr = new IGSXMLTransaction("RTV_HAZCTR"); 
			rtvHazCntr.setActionMessage("Retrieving Hazmat Label Codes/PN's...");
			rtvHazCntr.startDocument();
			rtvHazCntr.addElement("MODE", mode);
			
			if(null != getTriggerSource())
			{
				rtvHazCntr.addElement("LBTS", getTriggerSource());
			}
			
			if(null != getOptionalParameters())
			{
				rtvHazCntr.append(getOptionalParameters());
			}
			
			rtvHazCntr.append(hazInfo);
			rtvHazCntr.endDocument();

			MFSComm.execute(rtvHazCntr);

			if (0 == rtvHazCntr.getReturnCode()) 
			{			
				// New Map of hazmat Cntrs
				hazmatCntrMap = new LinkedHashMap<String, MFSHazmatCntr>();
				
				boolean isFFBM = false;
				
				// Parse the cntrs and create a map with cntr as the key
				rtvHazCntr.stepIntoReqElement("HAZINFO");

				while(null != rtvHazCntr.stepIntoElement("HAZCNTR"))
				{
					String temp = rtvHazCntr.getNextElement("CNTR");
					
					if(null == temp)
					{
						temp = rtvHazCntr.getNextElement("MCTL");
						
						if(mode.equals("") && rtvHazCntr.getNextElement("WTYP").trim().equals("S"))
						{
							isFFBM = true;
						}
					}
					
					temp = temp.trim();
				
					MFSHazmatCntr hazmatCntr = new MFSHazmatCntr(temp);
					
					// New Map of hazmat PN's/labels
					Map<String, MFSHazmatPn> hazmatPnMap = new HashMap<String, MFSHazmatPn>();
					
					while(null != rtvHazCntr.stepIntoElement("LABEL"))
					{
						MFSHazmatPn hazmatPn = new MFSHazmatPn();
						hazmatPn.setPn(rtvHazCntr.getNextReqElement("PART").trim());	
						hazmatPn.setVerifiedDate(rtvHazCntr.getNextReqElement("STMP"));
						
						temp = rtvHazCntr.getNextReqElement("ACTN");
						
						if(temp.equals("R"))
						{
							hazmatPn.setStatus(STATUS_RR);
						}
						else if(temp.equals("I"))
						{
							hazmatPn.setStatus(STATUS_SV);
						}
						else if(temp.equals("A"))
						{
							hazmatPn.setStatus(STATUS_VR);
						}
						
						// Put the hazmat PN into the pn map
						hazmatPnMap.put(hazmatPn.getPn(), hazmatPn);
						
						rtvHazCntr.stepOutOfElement();
					}
					
					if(!hazmatPnMap.isEmpty())
					{
						hazmatCntr.setHazmatPns(hazmatPnMap);
						// Put the hazmat Cntr into the cntr map only if it has PNs
						hazmatCntrMap.put(hazmatCntr.getCntr(), hazmatCntr);
					}
					
					if(isFFBM)
					{
						hazmatCntrMap.put("SUBASSEMBLY", null);
					}
					
					rtvHazCntr.stepOutOfElement();
				}
			}
			else
			{
				IGSMessageBox.showOkMB(getParentFrame(), getClass().getSimpleName(), rtvHazCntr.getErms(), null);
			}
		}
		catch(Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), getClass().getSimpleName(), e.getMessage(), null);
		}
		
		return hazmatCntrMap;
	}

	/**
	 * @param optionalParameters the optionalParameters to set
	 */
	public void setOptionalParameters(String optionalParameters) {
		this.optionalParameters = optionalParameters;
	}
	
	/**
	 * @param triggerSource the triggerSource to set
	 */
	public void setTriggerSource(String triggerSource) {
		this.triggerSource = triggerSource;
	}

	/**
	 * To verify hazmat label part numbers
	 * @param verificationEnabled the verificationEnabled to set
	 */
	public void setVerificationEnabled(boolean verificationEnabled) 
	{
		this.verificationEnabled = verificationEnabled;
	}
	
	/**
	 * Verify a single <code>MFSHazmatCntr</code> displaying a <code>MFSHazmatDialog</code>
	 * @param hazmatCntr the hazmat container to be verified
	 * @return true if the container verification was completed
	 */
	public boolean vrfyHazCntr(MFSHazmatCntr hazmatCntr)
	{
		boolean isHazmatCntrCompleted = false;
		
		MFSHazmatDialog hazmatDialog = new MFSHazmatDialog(getParentFrame(), hazmatCntr, isVerificationEnabled());			
		
		do
		{
			hazmatDialog.setVisible(true);
			
			// Get here if the user canceled or suspended the dialog
			isHazmatCntrCompleted = MFSHazmatLabeling.isCntrCompleted(hazmatCntr);
			
			if(hazmatDialog.isCompleteSelected() && !isHazmatCntrCompleted)
			{
				String msg = "CNTR: " + hazmatCntr.getCntr() + " is NOT completed, verify all Hazmat label parts to proceed.";
				IGSMessageBox.showOkMB(getParentFrame(), this.getClass().getSimpleName(), msg, null);
			}
		}
		while(hazmatDialog.isCompleteSelected() && !isHazmatCntrCompleted);
		
		return isHazmatCntrCompleted;
	}
	
	/**
	 * Verify multiple <code>MFSHazmatCntr</code>s displaying a dialog with a list of incomplete
	 * non verified containers where the user can select a single container to be displayed in
	 * a <code>MFSHazmatDialog</code>.
	 * @param hazmatCntrMap the hazmat containers
	 * @return 
	 */
	public boolean vrfyHazCntrs(Map<String, MFSHazmatCntr> hazmatCntrMap)
	{
		// For each container display the hazmat dialog to validate the hazmat label(s) required
		Collection<MFSHazmatCntr> hazmatCntrs = hazmatCntrMap.values();
		
		// Get the incomplete/not verified cntrs
		ArrayList<String> incompleteCntrs = new ArrayList<String>();
				
		for(MFSHazmatCntr hazmatCntr : hazmatCntrs)
		{
			if(!MFSHazmatLabeling.isCntrCompleted(hazmatCntr))
			{
				incompleteCntrs.add(hazmatCntr.getCntr());
			}
		}
		
		if(1 == incompleteCntrs.size())
		{
			MFSHazmatCntr hazmatCntr = hazmatCntrMap.get(incompleteCntrs.get(0));
			
			// Verify the container
			if(vrfyHazCntr(hazmatCntr))
			{
				incompleteCntrs.clear();
			}
		}
		// Just loop thru the incomplete/not verified cntrs
		else if(!incompleteCntrs.isEmpty())
		{
			// The container dialog
			MFSCntrDialog cntrJD = new MFSCntrDialog(getParentFrame());
			cntrJD.setTitle("Select the Hazmat Container");
			cntrJD.setVisibleButton("Delete", false);
			cntrJD.setVisibleButton("Reprint", false);
			
			do
			{
				cntrJD.loadCntrListModel(incompleteCntrs);
				cntrJD.setSelectedIndex(0);
				cntrJD.setLocationRelativeTo(getParentFrame());
				cntrJD.setVisible(true);				
				
				if (cntrJD.getButtonPressed().equals("Select"))
				{
					MFSHazmatCntr hazmatCntr = hazmatCntrMap.get(cntrJD.getSelectedCntr());
					
					// Verify the container
					if(vrfyHazCntr(hazmatCntr))
					{
						incompleteCntrs.remove(cntrJD.getSelectedCntr());
					}
				}
				else
				{
					// The user closed the dialog or pressed the cancel button
					break;
				}
			}
			while(cntrJD.getButtonPressed().equals("Select") && !incompleteCntrs.isEmpty());
		}
		
		return incompleteCntrs.isEmpty();
	}
}
