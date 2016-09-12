/* @ Copyright IBM Corporation 2005, 2007. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2006-08-17  ~01 34222BP  VH Avila         -Add new fillFieldItem variable and its
 *                                            get and set functions   
 * 2007-01-15   ~2 34242JR  R Prechel        -Java 1.5 version
 *                                           -Correct setMATP
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.barcode;

/**
 * <code>BCIndicatorValue</code> contains a set of indicator flags that
 * determine what barcodes types are valid when decoding a barcode.
 * @author The MFS Client Development Team
 */
public class MFSBCIndicatorValue
{
	//An indicator field is set to check for 
	//that barcode type when decoding a barcode.
	
	/** Collect Machine Type Serial */
	private String fieldCMTI = " "; //$NON-NLS-1$

	/** Collect Part Sequence Number */
	private String fieldCSNI = " "; //$NON-NLS-1$

	/** Collect Card Assembly Number */
	private String fieldCCAI = " "; //$NON-NLS-1$

	/** Part Number Required Indicator */
	private String fieldPNRI = " "; //$NON-NLS-1$

	/** EC Level Required Indicator */
	private String fieldECRI = " "; //$NON-NLS-1$

	/** Country of Origin Indicator */
	private String fieldCOOI = " "; //$NON-NLS-1$

	/** Machine Type Model */
	private String fieldMMDL = " "; //$NON-NLS-1$

	/** Machine Type */
	private String fieldMATP = " "; //$NON-NLS-1$

	/** Machine Serial Number */
	private String fieldMCSN = " "; //$NON-NLS-1$

	//This is for the HDR_CODE - later will be send in
	/** Product Line */
	private String fieldPRLN = new String();

	/** Ordered Part Number */
	private String fieldITEM = new String();

	/** Header Code */
	private String fieldHDR_CODE = new String();

	/** MAC ID Indicator */
	private java.lang.String fieldMACI = " "; //$NON-NLS-1$

	private boolean fillFieldItem = false; /* ~01A */

	/** Constructs a new <code>BCIndicatorValue</code>. */
	public MFSBCIndicatorValue()
	{
		super();
	}

	/**
	 * Gets the CCAI property value.
	 * @return The CCAI property value.
	 */
	public String getCCAI()
	{
		return this.fieldCCAI;
	}

	/**
	 * Gets the CMTI property value.
	 * @return The CMTI property value.
	 */
	public String getCMTI()
	{
		return this.fieldCMTI;
	}

	/**
	 * Gets the COOI property value.
	 * @return The COOI property value.
	 */
	public String getCOOI()
	{
		return this.fieldCOOI;
	}

	/**
	 * Gets the CSNI property value.
	 * @return The CSNI property value.
	 */
	public String getCSNI()
	{
		return this.fieldCSNI;
	}

	/**
	 * Gets the ECRI property value.
	 * @return The ECRI property value.
	 */
	public String getECRI()
	{
		return this.fieldECRI;
	}

	//	 New method ~01A
	/**
	 * Gets the fillFieldItem flag
	 * @return The fillFieldItem flag property
	 */
	public boolean getFillFieldItem()
	{
		return this.fillFieldItem;
	}

	/**
	 * Gets the HDR_CODE property value.
	 * @return The HDR_CODE property value.
	 */
	public String getHDR_CODE()
	{
		return this.fieldHDR_CODE;
	}

	/**
	 * Gets the ITEM property value.
	 * @return The ITEM property value.
	 */
	public String getITEM()
	{
		return this.fieldITEM;
	}

	/**
	 * Get the MACI property value.
	 * @return The MACI property value.
	 */
	public String getMACI()
	{
		return this.fieldMACI;
	}

	/**
	 * Get the MATP property value.
	 * @return The MATP property value.
	 */
	public String getMATP()
	{
		return this.fieldMATP;
	}

	/**
	 * Get the MCSN property value.
	 * @return The MCSN property value.
	 */
	public String getMCSN()
	{
		return this.fieldMCSN;
	}

	/**
	 * Get the MMDL property value.
	 * @return The MMDL property value.
	 */
	public String getMMDL()
	{
		return this.fieldMMDL;
	}

	/**
	 * Gets the PNRI property value.
	 * @return The PNRI property value.
	 */
	public String getPNRI()
	{
		return this.fieldPNRI;
	}

	/**
	 * Gets the PRLN property value.
	 * @return The PRLN property value.
	 */
	public String getPRLN()
	{
		return this.fieldPRLN;
	}

	/**
	 * Sets the CCAI property value.
	 * @param CCAI The new value for the property.
	 */
	public void setCCAI(String CCAI)
	{
		this.fieldCCAI = CCAI;
	}

	/**
	 * Sets the CMTI property value.
	 * @param CMTI The new value for the property.
	 */
	public void setCMTI(String CMTI)
	{
		this.fieldCMTI = CMTI;
	}

	/**
	 * Sets the COOI property value.
	 * @param COOI The new value for the property.
	 */
	public void setCOOI(String COOI)
	{
		this.fieldCOOI = COOI;
	}

	/**
	 * Sets the CSNI property value.
	 * @param CSNI The new value for the property.
	 */
	public void setCSNI(String CSNI)
	{
		this.fieldCSNI = CSNI;
	}

	/**
	 * Sets the ECRI property value.
	 * @param ECRI The new value for the property.
	 */
	public void setECRI(String ECRI)
	{
		this.fieldECRI = ECRI;
	}

	//	New method ~01A
	/**
	 * Sets the fillFieldItem flag property value
	 * @param fill The new value for the flag property
	 */
	public void setFillFieldItem(boolean fill)
	{
		this.fillFieldItem = fill;
	}

	/**
	 * Sets the HDR_CODE property value.
	 * @param HDR_CODE The new value for the property.
	 */
	public void setHDR_CODE(String HDR_CODE)
	{
		this.fieldHDR_CODE = HDR_CODE;
	}

	/**
	 * Sets the ITEM property value.
	 * @param ITEM The new value for the property.
	 */
	public void setITEM(String ITEM)
	{
		this.fieldITEM = ITEM;
	}

	/**
	 * Sets the MACI property value.
	 * @param MACI The new value for the property.
	 */
	public void setMACI(String MACI)
	{
		this.fieldMACI = MACI;
	}

	/**
	 * Sets the MATP property value.
	 * @param MATP The new value for the property.
	 */
	public void setMATP(String MATP)
	{
		//fieldMMDL = MATP; //~2D
		this.fieldMATP = MATP; //~2A
	}

	/**
	 * Sets the MCSN property value.
	 * @param MCSN The new value for the property.
	 */
	public void setMCSN(String MCSN)
	{
		this.fieldMCSN = MCSN;
	}

	/**
	 * Sets the MMDL property value.
	 * @param MMDL The new value for the property.
	 */
	public void setMMDL(String MMDL)
	{
		this.fieldMMDL = MMDL;
	}

	/**
	 * Sets the PNRI property value.
	 * @param PNRI The new value for the property.
	 */
	public void setPNRI(String PNRI)
	{
		this.fieldPNRI = PNRI;
	}

	/**
	 * Sets the PRLN property value.
	 * @param PRLN The new value for the property.
	 */
	public void setPRLN(String PRLN)
	{
		this.fieldPRLN = PRLN;
	}
}
