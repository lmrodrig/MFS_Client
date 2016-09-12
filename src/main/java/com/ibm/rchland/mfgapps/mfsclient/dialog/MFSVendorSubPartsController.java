/* @ Copyright IBM Corporation 2011-2012. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2011-11-14      588440   Santiago SC      -Initial version, Java 5.0
 * 2011-12-29 ~01  D625628  Edgar Mercado    -Add a new attribute to class to know when
 *                                            deleteSubAssemblyPart() has been called and this
 *                                            way avoid conflicts between vrfypnplus instance there
 *                                            with the one on displayVendorSubassemblyPartsDialog()
 *                                            and send correct return code to caller.
 *                                           -Add a new attribute to know when sub is complete (same case than for deleteSub)
 * 2012-01-03 ~02  D629693  Edgar Mercado    -Add user, cell and celltype on the call to VRFYPNPLUS (logSubAssemblyPart())
 * 2012-01-09 ~03  D634496  Toribio H.       -Add support for OnDemandPrinting with custom Trigger
 * 2012-01-23 ~04  E638153  Edgar Mercado    -Add support for substitute parts
 * 2012-01-29 ~05  D615310  Edgar Mercado    -Add support for COO values   
 * 2012-02-06 ~06  D652349  Edgar Mercado    -Save subdata type to know on MFSVendorSubPartsDialog if working with a new or current
 *                 D652354  Edgar Mercado    -Make AutoPrint work for existing completed assemblies
 * 2012-02-23 ~07  D652357  Edgar Mercado    -GreyMarket: Allow user to clear the input fields using the End button (clearButton)
 *                 D662211                   -GreyMarket: PLOM issue:  If 'DEFAULTPLOM' client configuration entry is not set up, 
 *                                            a garbage value of 'Non' is written in the PLOM fields for the Work Unit created by
 *                                            VendorSubPart dialog in the Standalone (SWEXTFUNC) mode. Send blank if config not found
 * 2012-03-05 ~08  D674625  Edgar Mercado    -Grey Market: END/Close buttons fail to close dialog in Direct Work
 * 2012-05-29 ~09  E692532  Edgar Mercado    -GreyMarket (CR783/RCQ00203226): Inventory Accuracy. Rework/Reapply functionality                                                                                      
 ******************************************************************************/

package com.ibm.rchland.mfgapps.mfsclient.dialog;

import com.ibm.rchland.mfgapps.client.utils.exception.IGSException;
import com.ibm.rchland.mfgapps.client.utils.messagebox.IGSMessageBox;
import com.ibm.rchland.mfgapps.mfsclient.MFSFrame;
import com.ibm.rchland.mfgapps.mfsclient.struct.MFSHeaderRec;
import com.ibm.rchland.mfgapps.mfsclient.struct.MFSOnDemandKeyData;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSOnDemand;
import com.ibm.rchland.mfgapps.mfscommon.MFSConfig;
import com.ibm.rchland.mfgapps.mfscommon.comm.trx.VRFYPNPLUS;

/**
 * <code>MFSVendorSubPartsLogging</code> is used to handle the vendor sub part
 * logging for Grey Market through a <code>MFSVendorSubPartsDialog</code>.
 * @author The MFS Client Development Team
 */
public class MFSVendorSubPartsController 
{
	/** Code that indicates in sub part data should be collected */
	public static final int COLLECT_SUB_PART_DATA = 777;
	
	/** The frame that requires vendor parts loggging */
	private MFSFrame parentFrame;
	
	/** Determines if the offline flag should be passed to the tx */
	private boolean offline = false;
	
	/** Auto-Print flag */
	private boolean autoPrint = false;
	
	/** Auto-Serialize flag */
	private boolean autoSerialize = false;
	
	/** Use Vendor As-Built Data flag */
	private boolean useVendorAsBuiltData = false;

	/** The dialog use for vendor subpart logging */
	private MFSVendorSubPartsDialog vspDialog = null;
	
	/** */
	private boolean modifyParts = false;
	
	/** ~01A If set, then it means that sub is complete (verified on countPartsToInstall() -
	 * MFSVendorSubPartsDialog*/
    private boolean subComplete = false;
    /*  ~06A 
     *  ~09C Initialize to blank
     */
    private String subDataType = " ";
    /*  ~09A Save the IDSS value from work unit to know if it is a vendor sub
     * 
     */
    private String idssVsap;
    
    /*  ~09A Save the parent work unit to know if the subassembly is installed
     * 
     */
    private String subPwun;    
    
	/**
	 * Creates a new <code>MFSVendorSubPartsLogging</code> with a <code>MFSFrame</code>
	 * as its parent.
	 * @param parentFrame
	 */
	public MFSVendorSubPartsController(MFSFrame parentFrame, boolean offline)
	{
		this.parentFrame = parentFrame;
		/**
		 * Sets the offline mode to true to indicate the dialog is being triggered
		 * without starting a work unit, false otherwise. Note that this will also
		 * determine the if the Auto-Print checkbox is enabled. The default offline
		 * value is false.
		 */	
		this.offline = offline;
		initialize();
	}
	
	/**
	 * Initializes this controller components.
	 */
	private void initialize()
	{	
		vspDialog = new MFSVendorSubPartsDialog(this.parentFrame, this);
	}

	/**
	 * Configures a new <code>MFSVendorSubPartsDialog</code> used for logging.
	 * @throws IGSException 
	 */
	private void configureVendorSubPartsDialog(boolean enablePartLogging) throws IGSException
	{		
		// MFSSelWkrExtFunc is offline
		if(offline)
		{
			vspDialog.setAutoPrintEnabled(true,false);  //Add second parm ~06C
			vspDialog.setAutoPrintSelected(autoPrint);		
		}
		else
		{
			vspDialog.setAutoPrintEnabled(false,false); //Add second parm ~06C
			vspDialog.setAutoPrintSelected(false);
		}
		
		if(enablePartLogging)
		{
			vspDialog.setAutoSerializeEnabled(true);
			vspDialog.setAutoSerializeSelected(this.autoSerialize);
			vspDialog.setUseVendorAsBuiltDataEnabled(true);	
			vspDialog.setUseVendorAsBuiltDataSelected(useVendorAsBuiltData);
		}
		else
		{
			vspDialog.setAutoSerializeEnabled(false);
			vspDialog.setAutoSerializeSelected(false);
			vspDialog.setUseVendorAsBuiltDataEnabled(false);	
			vspDialog.setUseVendorAsBuiltDataSelected(true);
		}
	}
	
	/**
	 * @return the offline
	 */
	public boolean isOffline() {
		return offline;
	}

	/**
	 * ~09A
	 */
	public void setModifyParts(boolean modifyParts) {
		this.modifyParts = modifyParts;
	}
	
	/**
	 * @return the modifyParts
	 */
	public boolean isModifyParts() {
		return modifyParts;
	}

	/**
	 * ~01A
	 */
	public void setSubComplete(boolean subInstalled) {
		this.subComplete = subInstalled;
	}
	
	/**
	 * ~01A
	 */	
	public boolean isSubComplete() {
		return subComplete;
	}
	
	/**
	 * ~06A
	 */	
	public String getSubDataType() {
		return subDataType;
	}
	
	/**
	 * ~09A
	 */	
	public String getIdssVsap() {
		return idssVsap;
	}

	/**
	 * ~09A
	 */	
	public String getSubPwun() {
		return subPwun;
	}	
	
	/**
	 * Retrieves a Subassembly and its sub-parts
	 * @return true if the part was logged/retrieved successfully, false otherwise
	 */
	public boolean rtvSubAssemblyPartsOffline(String mctl, String inpn, String insq) {
		return rtvSubAssemblyPartsOffline(mctl, inpn, insq, false, false); //~09C
	}

	/**
	 * Retrieves a Subassembly and its sub-parts
	 * @return true if the part was logged/retrieved successfully, false otherwise
	 */
	public boolean rtvSerializeSubAssemblyPartsOffline(String mctl, String inpn) {
		return rtvSubAssemblyPartsOffline(mctl, inpn, "", true, false); //$NON-NLS-1$ //~09C
	}
	
	/**
	 * Retrieves a Subassembly and its sub-parts
	 * @param installFlag. Used to specify that Rebuild option was selected
	 * @return true if the part was logged/retrieved successfully, false otherwise
	 */
	public boolean rtvRebuildSubAssemblyPartsOffline(String mctl, String inpn, String insq, boolean installFlag) {
		return rtvSubAssemblyPartsOffline(mctl, inpn, insq, false, installFlag);
	}	

	/**
	 * Retrieves a Subassembly and its sub-parts
	 * @param rebuild. Used to specify that Rebuild option was selected  ~09A
	 * @return true if the part was logged/retrieved successfully, false otherwise
	 */
	private boolean rtvSubAssemblyPartsOffline(String mctl, String inpn, String insq, boolean serialize, boolean rebuild)
	{
		VRFYPNPLUS vrfypnplus = new VRFYPNPLUS(vspDialog);
		
		vrfypnplus.setInputCwun(mctl); 
		vrfypnplus.setInputInpn(inpn);
		vrfypnplus.setInputInsq(insq);
		
		vrfypnplus.setCell(MFSConfig.getInstance().get8CharCell());      //~09A
		vrfypnplus.setUser(MFSConfig.getInstance().get8CharUser());      //~09A
		
		/* Send blank value for PLOM if config DEFAULTPLOM not found ~07A*/
		if (MFSConfig.getInstance().getConfigValue("DEFAULTPLOM").equals("Not Found"))
		{
			vrfypnplus.setPlom(""); //$NON-NLS-1$
		}
		else
		{
			vrfypnplus.setPlom(MFSConfig.getInstance().getConfigValue("DEFAULTPLOM")); //$NON-NLS-1$
		}
		
		vrfypnplus.setOffline(VRFYPNPLUS.YES_FLAG); 
		if(serialize && insq.length() == 0) {
			vrfypnplus.setAutoSerialize(VRFYPNPLUS.YES_FLAG);
		}
		else {
			vrfypnplus.setAutoSerialize((vspDialog.isAutoSerializeSelected())? VRFYPNPLUS.YES_FLAG : VRFYPNPLUS.NO_FLAG);
		}		
		vrfypnplus.setUseVendorAsBuiltData((vspDialog.isUseVendorAsBuiltDataSelected())? VRFYPNPLUS.YES_FLAG : VRFYPNPLUS.NO_FLAG);		
		
		/* Set installFlag to Rebuild if that button was pressed on the dialog ~09C
		 * 
		 */
		if(rebuild)
		{
			vrfypnplus.setInstall(VRFYPNPLUS.REBUILD_FLAG);	
		}
		else
		{
			vrfypnplus.setInstall(VRFYPNPLUS.NO_FLAG);
		}	

		
		vrfypnplus.execute();				
		try
		{
			if(0 == vrfypnplus.getReturnCode())
			{			
				throw new IGSException("Fatal Error, retrieving Subassembly data."); //$NON-NLS-1$
			}
			else if(MFSVendorSubPartsController.COLLECT_SUB_PART_DATA == vrfypnplus.getReturnCode())
			{
				/*
				 * Get IDSS from VRFYPNPLUS output before calling setSubAssemblyData to know if
				 * rebuild/rework buttons should be enabled.                               ~09A
				 */
				this.idssVsap = vrfypnplus.getOutputIdss().toString();                //~09A
				this.subPwun = vrfypnplus.getOutputPwun().toString();                 //~09A
				this.vspDialog.setSubAssemblyData(vrfypnplus.getOutputMctl(), 
						vrfypnplus.getOutputInpn(), 
						vrfypnplus.getOutputInsq(), 
						vrfypnplus.getOutputCoo(), 
						vrfypnplus.getOutputSubParts(),
						vrfypnplus.getOutputSubstitutes(),
						vrfypnplus.getOutputCoos()); // Add Substitutes	 ~04A, Add Coos ~05A
				this.modifyParts =(vrfypnplus.getOutputSubData().equals(VRFYPNPLUS.SUBDATA_NEW));
				this.subDataType = vrfypnplus.getOutputSubData().toString();          //~06A
				this.subComplete = (vrfypnplus.getOutputSubData().equals(VRFYPNPLUS.SUBDATA_NEW)?false:true);
			}
			else
			{
				throw new IGSException(vrfypnplus.getErrorMessage());
			}				
			return true;
		}
		catch(Exception e)
		{
			IGSMessageBox.showOkMB(this.parentFrame, this.getClass().getSimpleName(), e.getMessage(), null);
			return false;
		}
	}
	
	/**
	 * Logs a top level part or a sub-parts
	 * @param vendorPart a top level or sub part
	 * @return true if the part was logged/retrieved successfully, false otherwise
	 */
	public boolean logSubAssemblyPart(String mctl, String crct, String pn, String sn, String cooc, String stat, boolean lastPart)  // Add cooc parm ~04C
	{                                                                                                                              // Add stat parm ~09C
		VRFYPNPLUS vrfypnplus = new VRFYPNPLUS(vspDialog);
		
		vrfypnplus.setInputMctl(mctl);
		vrfypnplus.setInputInpn(pn);
		vrfypnplus.setInputInsq(sn);
		vrfypnplus.setInputCooc(cooc);                                   //~04A
		vrfypnplus.setInputCrct(crct);
		/*
		 * When part stat equals to 'R', send and Update operation to VRFYPNPLUS ~09A
		 */
		if(stat.equals("R"))
		{
			vrfypnplus.setInstall(VRFYPNPLUS.INSTALL_UPDATE);
		}
		else if(!this.getSubDataType().equals("N") && !this.isModifyParts())
		{/*
		 * Set installFlag to SET_ACTIVE_INACTIVE to DESACTIVATE Work Unit  ~09A
		 */
				
			vrfypnplus.setInstall(VRFYPNPLUS.SET_ACTIVE_INACTIVE);
		}
		else
		{
			vrfypnplus.setInstall((lastPart ? VRFYPNPLUS.INSTALL_COMPLETE : VRFYPNPLUS.YES_FLAG));
		}
		
		
		vrfypnplus.setCell(MFSConfig.getInstance().get8CharCell());      //~02A
		vrfypnplus.setUser(MFSConfig.getInstance().get8CharUser());      //~02A
				
		vrfypnplus.execute();		
		try
		{
			if(0 == vrfypnplus.getReturnCode())
			{			
				// all good here, the subpart was verified
				/* Set modifyParts to false when lastPart equal to true since subassembly is  
				 * complete.                                                            ~08A
				 */
				if (lastPart)
				{
					this.modifyParts = false;	
				}
			}
			else
			{
				throw new IGSException(vrfypnplus.getErrorMessage());
			}				
			return true;
		}
		catch(Exception e)
		{
			IGSMessageBox.showOkMB(this.parentFrame, this.getClass().getSimpleName(), e.getMessage(), null);
		}
		return false;
	}
	
	/**
	 * Deletes Un-installed sub-parts from mctl
	 * @param vendorPart a top level or sub part
	 */
	public boolean deleteSubAssemblyPart(String mctl)
	{
		VRFYPNPLUS vrfypnplus = new VRFYPNPLUS(vspDialog);
		
		vrfypnplus.setInputMctl(mctl);
		vrfypnplus.setInstall(VRFYPNPLUS.INSTALL_DELETE);
		vrfypnplus.setCell(MFSConfig.getInstance().get8CharCell());      //~07A
		vrfypnplus.setUser(MFSConfig.getInstance().get8CharUser());      //~07A		
		
		vrfypnplus.execute();		
		try
		{
			if(0 == vrfypnplus.getReturnCode())
			{			
				// all good here, the subpart was verified
				/* Set modifyParts to false since subassembly is not in create/collect mode 
				 * after it has been deleted (cleared) and user can start over.        ~07A
				 */
				this.modifyParts = false;
			}
			else
			{
				throw new IGSException(vrfypnplus.getErrorMessage());
			}				
			return true;
		}
		catch(Exception e)
		{
			IGSMessageBox.showOkMB(this.parentFrame, this.getClass().getSimpleName(), e.getMessage(), null);
		}
		return false;
	}

	/**
	 * Displays a new <code>MFSVendorSubPartsDialog</code> ready for logging. The
	 * dialog sub part list will be empty.
	 */
	public int displayVendorSubassemblyPartsDialog(String triggerSource, VRFYPNPLUS vrfypnplus)
	{
		int rc = 0;
		
		try
		{
			boolean enablePartLogging = false;
			if(triggerSource != null) {
				/**
				 * Configures the logging dialog options for the specific trigger. Note that 
				 * depending on the trigger value the auto-serialize and the as-build boxes
				 * will be enabled.
				 * @param trigger the configuration entry or button that triggered the logging.
				 */
			    enablePartLogging = MFSConfig.getInstance().getConfigValue(triggerSource).equals("ON"); //$NON-NLS-1$
			}
			if(triggerSource != null) {
				this.loadPreferences();
			}			
			this.configureVendorSubPartsDialog(enablePartLogging);
			if(vrfypnplus != null) {
				// Load the data retrieved
				this.modifyParts = (vrfypnplus.getOutputSubData().equals(VRFYPNPLUS.SUBDATA_NEW));
				this.idssVsap = vrfypnplus.getOutputIdss().toString();                      //~09A
				this.subPwun = vrfypnplus.getOutputPwun().toString();                       //~09A
				
				this.vspDialog.setSubAssemblyData(vrfypnplus.getOutputMctl(), 
						vrfypnplus.getOutputInpn(), 
						vrfypnplus.getOutputInsq(), 
						vrfypnplus.getOutputCoo(), 
						vrfypnplus.getOutputSubParts(),
						vrfypnplus.getOutputSubstitutes(),
						vrfypnplus.getOutputCoos()); // Add Substitutes	 ~04A, Add Coos ~05A
				this.subDataType = vrfypnplus.getOutputSubData().toString();          //~07A
				this.subComplete = (vrfypnplus.getOutputSubData().equals(VRFYPNPLUS.SUBDATA_NEW)?false:true); //~07A

			}			
			this.vspDialog.setVisible(true);
		}
		catch(Exception e)
		{
			rc = -1;
			IGSMessageBox.showOkMB(this.parentFrame, this.getClass().getSimpleName(), e.getMessage(), null);
		}
		finally
		{
	    	this.autoPrint = this.vspDialog.isAutoPrintSelected();
	    	this.autoSerialize = this.vspDialog.isAutoSerializeSelected();
	    	this.useVendorAsBuiltData = this.vspDialog.isUseVendorAsBuiltDataSelected();
	    	
	    	if(triggerSource != null) {
	    		this.savePreferences();
	    	}
		}		
		return rc;
	}
		
	/**
	 * ~03 Support print on demmnad with VENDSUBPARTS trigger 
	 */
	public void print(String mctl, String topPN)
	{	
		if(mctl.length() != 0) {
			// All sub parts are complete now trigger the ROCHMIRR label
			/* MFSPrintingMethods.mir(MFSConfig.getInstance().get8CharCellType(), 
									topPN, mctl, 1, this.parentFrame); ~03D */			
			MFSOnDemandKeyData odKeyData = new MFSOnDemandKeyData();
			odKeyData.setTriggerSource("VENDSUBPARTS"); //$NON-NLS-1$
			
			MFSHeaderRec headerRec = new MFSHeaderRec();
			headerRec.setMctl(mctl);
			headerRec.setSapn(topPN);
			odKeyData.setDataSource(headerRec);
			
			// Start label printing process
			MFSOnDemand odLabel = new MFSOnDemand(this.parentFrame, odKeyData);
			try {
				odLabel.labelTrigger();
			}
			catch (Exception e) {
				IGSMessageBox.showOkMB(this.parentFrame, null, e.getMessage(), null);
			}
		}
	}
	
	/**
	 * Loads the user preferences.
	 * @param offline: set the offline mode on or off
	 */			
	private void loadPreferences()
	{	
		MFSConfig config = MFSConfig.getInstance();
		Object selected = null;
		
		if(null != (selected = config.getConfigObject("LOGVENDORPARTS.AUTOPRINT"))) //$NON-NLS-1$
		{
			autoPrint = (Boolean) selected;
		}
		if(null != (selected = config.getConfigObject("LOGVENDORPARTS.AUTOSERIAL"))) //$NON-NLS-1$
		{
			autoSerialize = (Boolean) selected;
		}
		if(null != (selected = config.getConfigObject("LOGVENDORPARTS.USEVENDOR"))) //$NON-NLS-1$
		{
			useVendorAsBuiltData = (Boolean) selected;
		}	
	}

	/**
	 * Save the user preferences.
	 */
	private void savePreferences()
	{
		MFSConfig config = MFSConfig.getInstance();
		config.setConfigValue("LOGVENDORPARTS.AUTOPRINT", autoPrint); //$NON-NLS-1$
		config.setConfigValue("LOGVENDORPARTS.AUTOSERIAL", autoSerialize); //$NON-NLS-1$
		config.setConfigValue("LOGVENDORPARTS.USEVENDOR", useVendorAsBuiltData);				 //$NON-NLS-1$
	}
}