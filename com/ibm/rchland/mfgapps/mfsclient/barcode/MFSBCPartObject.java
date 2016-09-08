/* © Copyright IBM Corporation 2005, 2007. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2007-01-15   ~1 34242JR  R Prechel        -Java 5 version
 * 2010-11-15  ~02 49513JM  Toribio H.       -Save Coo that was scanned
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.barcode;

/**
 * <code>BCPartObject</code> is a data structure that contains the parts of a
 * decoded barcode and the barcode decoding return code and error string.
 * @author The MFS Client Development Team
 */
public class MFSBCPartObject
{
	/** The default value for a <code>String</code> field. */
	private static final String DEFAULT_STRING = ""; //$NON-NLS-1$

	/** Part Number */
	private String fieldPN = DEFAULT_STRING;

	/** Serial Number */
	private String fieldSN = DEFAULT_STRING;

	/** Engineering Code */
	private String fieldEC = DEFAULT_STRING;

	/** Card Assembly Number */
	private String fieldCA = DEFAULT_STRING;

	/** PL Number */
	private String fieldPL = DEFAULT_STRING;

	/** Order Number */
	private String fieldON = DEFAULT_STRING;

	/** Default */
	private String fieldDEF = DEFAULT_STRING;

	/** Machine Serial Number */
	private String fieldMSN = DEFAULT_STRING;

	/** Plant Code */
	private String fieldPC = DEFAULT_STRING;

	/** Machine Type Code */
	private String fieldMTC = DEFAULT_STRING;

	/** Model Number */
	private String fieldMD = DEFAULT_STRING;

	/** Work Unit (MCTL) */
	private String fieldWU = DEFAULT_STRING;

	/** Container */
	private String fieldCT = DEFAULT_STRING;

	/** Quantity */
	private String fieldQT = DEFAULT_STRING;

	/** Header Code */
	private String fieldHDR = DEFAULT_STRING;

	/** Country of Origin */
	private String fieldCO = DEFAULT_STRING;

	/** Scanned Country of Origin */
	private String fieldScannedCO = DEFAULT_STRING;

	/** MAC ID */
	private String fieldMC = DEFAULT_STRING;

	/** Error Return Code */
	private int fieldRC;

	/** Error String */
	private String fieldErrorString = DEFAULT_STRING;

	/** Constructs a new <code>BCPartObject</code>. */
	public MFSBCPartObject()
	{
		super();
	}

	/** This method resets all of the fields to the default value. */
	public void clearFields()
	{
		this.fieldPN = DEFAULT_STRING;
		this.fieldSN = DEFAULT_STRING;
		this.fieldEC = DEFAULT_STRING;
		this.fieldCA = DEFAULT_STRING;
		this.fieldPL = DEFAULT_STRING;
		this.fieldON = DEFAULT_STRING;
		this.fieldDEF = DEFAULT_STRING;
		this.fieldMSN = DEFAULT_STRING;
		this.fieldPC = DEFAULT_STRING;
		this.fieldMTC = DEFAULT_STRING;
		this.fieldMD = DEFAULT_STRING;
		this.fieldWU = DEFAULT_STRING;
		this.fieldCT = DEFAULT_STRING;
		this.fieldQT = DEFAULT_STRING;
		this.fieldHDR = DEFAULT_STRING;
		this.fieldCO = DEFAULT_STRING;
		this.fieldMC = DEFAULT_STRING; //~1A fieldMC was not cleared
		this.fieldRC = 0;
		this.fieldErrorString = DEFAULT_STRING;
	}

	/**
	 * Gets the Card Assembly Number property value.
	 * @return the Card Assembly Number property value
	 */
	public String getCA()
	{
		return this.fieldCA;
	}

	/**
	 * Gets the Country of Origin property value.
	 * @return the Country of Origin property value
	 */
	public String getCO()
	{
		return this.fieldCO;
	}

	/**
	 * Gets the Actual Coo of origin scanned
	 * @return the scannedCoo
	 */
	public String getScannedCO() {
		return fieldScannedCO;
	}

	/**
	 * Gets the Container property value.
	 * @return the Container property value
	 */
	public String getCT()
	{
		return this.fieldCT;
	}

	/**
	 * Gets the Default property value.
	 * @return the Default property value
	 */
	public String getDEF()
	{
		return this.fieldDEF;
	}

	/**
	 * Gets the Engineering Code property value.
	 * @return the Engineering Code property value
	 */
	public String getEC()
	{
		return this.fieldEC;
	}

	/**
	 * Gets the errorString property value.
	 * @return the errorString property value
	 */
	public String getErrorString()
	{
		return this.fieldErrorString;
	}

	/**
	 * Gets the Header Code property value.
	 * @return the Header Code property value
	 */
	public String getHDR()
	{
		return this.fieldHDR;
	}

	/**
	 * Gets the MAC ID property value.
	 * @return the MAC ID property value
	 */
	public java.lang.String getMC()
	{
		return this.fieldMC;
	}

	/**
	 * Gets the Machine Serial Number property value.
	 * @return the Machine Serial Number property value
	 */
	public String getMSN()
	{
		return this.fieldMSN;
	}

	/**
	 * Gets the Machine Type Code property value.
	 * @return the Machine Type Code property value
	 */
	public String getMTC()
	{
		return this.fieldMTC;
	}

	/**
	 * Gets the Model Number property value.
	 * @return the Model Number property value
	 */
	public String getMD()
	{
		return this.fieldMD;
	}

	/**
	 * Gets the Order Number property value.
	 * @return the Order Number property value
	 */
	public String getON()
	{
		return this.fieldON;
	}

	/**
	 * Gets the Plant Code property value.
	 * @return the PC property value
	 */
	public String getPC()
	{
		return this.fieldPC;
	}

	/**
	 * Gets the PL Number property value.
	 * @return The PL Number property value.
	 */
	public String getPL()
	{
		return this.fieldPL;
	}

	/**
	 * Gets the Part Number property value.
	 * @return the Part Number property value
	 */
	public String getPN()
	{
		return this.fieldPN;
	}

	/**
	 * Gets the Quantity property value.
	 * @return the Quantity property value
	 */
	public String getQT()
	{
		return this.fieldQT;
	}

	/**
	 * Gets the Return Code property value.
	 * @return the Return Code property value
	 */
	public int getRC()
	{
		return this.fieldRC;
	}

	/**
	 * Gets the Serial Number property value.
	 * @return the Serial Number property value
	 */
	public String getSN()
	{
		return this.fieldSN;
	}

	/**
	 * Gets the Work Unit property value
	 * @return the Work Unit property value
	 */
	public String getWU()
	{
		return this.fieldWU;
	}

	/**
	 * Sets the Card Assembly Number property value.
	 * @param CA The new value for the property.
	 */
	public void setCA(String CA)
	{
		this.fieldCA = CA;
	}

	/**
	 * Sets the Country of Origin property value.
	 * @param CO The new value for the property.
	 */
	public void setCO(String CO)
	{
		this.fieldCO = CO;
	}

	public void setScannedCO(String fieldScannedCO) {
		this.fieldScannedCO = fieldScannedCO;
	}

	/**
	 * Sets the Container property value.
	 * @param CT The new value for the property.
	 */
	public void setCT(String CT)
	{
		this.fieldCT = CT;
	}

	/**
	 * Sets the Default property value.
	 * @param DEF The new value for the property.
	 */
	public void setDEF(String DEF)
	{
		this.fieldDEF = DEF;
	}

	/**
	 * Sets the Engineering Code property value.
	 * @param EC The new value for the property.
	 */
	public void setEC(String EC)
	{
		this.fieldEC = EC;
	}

	/**
	 * Sets the Error String property value.
	 * @param errorString The new value for the property.
	 */
	public void setErrorString(String errorString)
	{
		this.fieldErrorString = errorString;
	}

	/**
	 * Sets the Header Code property value.
	 * @param HDR The new value for the property.
	 */
	public void setHDR(String HDR)
	{
		this.fieldHDR = HDR;
	}

	/**
	 * Sets the MAC ID property value.
	 * @param MC The new value for the property.
	 */
	public void setMC(java.lang.String MC)
	{
		this.fieldMC = MC;
	}

	/**
	 * Sets the Machine Serial Number property value.
	 * @param MSN The new value for the property.
	 */
	public void setMSN(String MSN)
	{
		this.fieldMSN = MSN;
	}

	/**
	 * Sets the Machine Type Code property value.
	 * @param MTC The new value for the property.
	 */
	public void setMTC(String MTC)
	{
		this.fieldMTC = MTC;
	}

	/**
	 * Sets the Model Number property value.
	 * @param MD The new value for the property.
	 */
	public void setMD(String MD)
	{
		this.fieldMD = MD;
	}

	/**
	 * Sets the Order Number property value.
	 * @param ON The new value for the property.
	 */
	public void setON(String ON)
	{
		this.fieldON = ON;
	}

	/**
	 * Sets the Plant Code property value.
	 * @param PC The new value for the property.
	 */
	public void setPC(String PC)
	{
		this.fieldPC = PC;
	}

	/**
	 * Sets the PL Number property value.
	 * @param PL The new value for the property.
	 */
	public void setPL(String PL)
	{
		this.fieldPL = PL;
	}

	/**
	 * Sets the Part Number property value.
	 * @param PN The new value for the property.
	 */
	public void setPN(String PN)
	{
		this.fieldPN = PN;
	}

	/**
	 * Sets the Quantity property value.
	 * @param QT The new value for the property.
	 */
	public void setQT(String QT)
	{
		this.fieldQT = QT;
	}

	/**
	 * Sets the Return Code property value.
	 * @param rc The new value for the property.
	 */
	public void setRC(int rc)
	{
		this.fieldRC = rc;
	}

	/**
	 * Sets the Serial Number property value.
	 * @param SN The new value for the property.
	 */
	public void setSN(String SN)
	{
		this.fieldSN = SN;
	}

	/**
	 * Sets the Work Unit property value.
	 * @param WU The new value for the property.
	 */
	public void setWU(String WU)
	{
		this.fieldWU = WU;
	}

	/**
	 * Returns a <code>String</code> representation of a
	 * <code>BCPartObject</code>.
	 * @return a <code>String</code> representation of a
	 *         <code>BCPartObject</code>
	 */
	public String toString()
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append("\n\nPart Number (PN)\t=\t"); //$NON-NLS-1$
		buffer.append(this.fieldPN);
		buffer.append("\nCard Assembly Number (CA)\t=\t"); //$NON-NLS-1$
		buffer.append(this.fieldCA);
		buffer.append("\nEC Number (EC)\t=\t"); //$NON-NLS-1$
		buffer.append(this.fieldEC);
		buffer.append("\nSerial Number (SN)\t=\t"); //$NON-NLS-1$
		buffer.append(this.fieldSN);
		buffer.append("\nPL Number (PL)\t=\t"); //$NON-NLS-1$
		buffer.append(this.fieldPL);
		buffer.append("\nOrder Number (ON)\t=\t"); //$NON-NLS-1$
		buffer.append(this.fieldON);
		buffer.append("\nDefault (DEF)\t=\t"); //$NON-NLS-1$
		buffer.append(this.fieldDEF);
		buffer.append("\nMachine Serial Number (MSN)\t=\t"); //$NON-NLS-1$
		buffer.append(this.fieldMSN);
		buffer.append("\nPlant Code (PC)\t=\t"); //$NON-NLS-1$
		buffer.append(this.fieldPC);
		buffer.append("\nMachine Type Code (MTC)\t=\t"); //$NON-NLS-1$
		buffer.append(this.fieldMTC);
		buffer.append("\nModel Number (MD)\t=\t"); //$NON-NLS-1$
		buffer.append(this.fieldMD);
		buffer.append("\nWU\t=\t"); //$NON-NLS-1$
		buffer.append(this.fieldWU);
		buffer.append("\nContainer (CT)\t=\t"); //$NON-NLS-1$
		buffer.append(this.fieldCT);
		buffer.append("\nQuantity (QT)\t=\t"); //$NON-NLS-1$
		buffer.append(this.fieldQT);
		buffer.append("\nHeader Code (HDR)\t=\t"); //$NON-NLS-1$
		buffer.append(this.fieldHDR);
		buffer.append("\nCountry of Origin (CO)\t=\t"); //$NON-NLS-1$
		buffer.append(this.fieldCO);
		buffer.append("\nMAC ID Number (MC)\t=\t"); //$NON-NLS-1$
		buffer.append(this.fieldMC);
		buffer.append("\nError Return Code(RC)\t=\t"); //$NON-NLS-1$
		buffer.append(this.fieldRC);
		buffer.append("\nError String\t=\t"); //$NON-NLS-1$
		buffer.append(this.fieldErrorString);
		return buffer.toString();
	}
}