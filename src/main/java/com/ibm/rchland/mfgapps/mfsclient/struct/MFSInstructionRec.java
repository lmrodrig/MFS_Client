/* @ Copyright IBM Corporation 2005, 2007. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2007-01-15   ~1 34242JR  R Prechel        -Java 5 version
 *                                           -Removed PropertyChangeSupport
 *                                           -Added getSsVectorElement method
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.struct;

import java.util.Vector;

/**
 * <code>MFSInstructionRec</code> is a data structure for instruction
 * information.
 * @author The MFS Client Development Team
 */
public class MFSInstructionRec
{
	/** The suffix number for the <code>MFSInstructionRec</code>. */
	private String fieldSuff = ""; //$NON-NLS-1$

	/** The sequence number for the <code>MFSInstructionRec</code>. */
	private String fieldIseq = ""; //$NON-NLS-1$

	/** The aggregate part list. */
	private String fieldAgragatePartList = ""; //$NON-NLS-1$

	/** The <code>Vector</code> of <code>MFSSubInstructionRec</code>s. */
	private Vector <MFSSubInstructionRec>fieldSsVector = new Vector<MFSSubInstructionRec>();

	/** The instruction class of the <code>MFSInstructionRec</code>. */
	private String fieldInstrClass = ""; //$NON-NLS-1$

	/** The completion status of the <code>MFSInstructionRec</code>. */
	private String fieldCompletionStatus = ""; //$NON-NLS-1$

	/** Set <code>true</code> if the <code>MFSInstructionRec</code> changed. */
	private boolean fieldChanged = false;

	/** The original completion status of the <code>MFSInstructionRec</code>. */
	private String fieldCompletionStatusOriginal = ""; //$NON-NLS-1$

	/** Constructs a new <code>MFSInstructionRec</code>. */
	public MFSInstructionRec()
	{
		super();
	}

	//~1A New method
	/**
	 * Returns the <code>MFSSubInstructionRec</code> at the specified index in
	 * the {@link #fieldSsVector ssVector}.
	 * @param index an index into the {@link #fieldSsVector ssVector}
	 * @return the <code>MFSSubInstructionRec</code> at the specified index
	 * @throws ArrayIndexOutOfBoundsException if <code>index</code> is
	 *         negative or is greater than or equal to the current size of the
	 *         {@link #fieldSsVector ssVector}
	 * @throws ClassCastException if the element at <code>index</code> is not
	 *         an <code>MFSSubInstructionRec</code>
	 */
	public MFSSubInstructionRec getSsVectorElement(int index)
	{
		return (MFSSubInstructionRec) this.fieldSsVector.get(index);
	}

	/**
	 * Returns the value of the agragatePartList property.
	 * @return the value of the agragatePartList property
	 */
	public String getAgragatePartList()
	{
		return this.fieldAgragatePartList;
	}

	/**
	 * Sets the value of the agragatePartList property.
	 * @param agragatePartList the new value of the agragatePartList property
	 */
	public void setAgragatePartList(String agragatePartList)
	{
		this.fieldAgragatePartList = agragatePartList;
	}

	/**
	 * Returns the value of the changed property.
	 * @return the value of the changed property
	 */
	public boolean getChanged()
	{
		return this.fieldChanged;
	}

	/**
	 * Sets the value of the changed property.
	 * @param changed the new value of the changed property
	 */
	public void setChanged(boolean changed)
	{
		this.fieldChanged = changed;
	}

	/**
	 * Returns the value of the completionStatus property.
	 * @return the value of the completionStatus property
	 */
	public String getCompletionStatus()
	{
		return this.fieldCompletionStatus;
	}

	/**
	 * Sets the value of the completionStatus property.
	 * @param completionStatus the new value of the completionStatus property
	 */
	public void setCompletionStatus(String completionStatus)
	{
		this.fieldCompletionStatus = completionStatus;
	}

	/**
	 * Returns the value of the completionStatusOriginal property.
	 * @return the value of the completionStatusOriginal property
	 */
	public String getCompletionStatusOriginal()
	{
		return this.fieldCompletionStatusOriginal;
	}

	/**
	 * Sets the value of the completionStatusOriginal property.
	 * @param completionStatusOriginal the new value of the
	 *        completionStatusOriginal property
	 */
	public void setCompletionStatusOriginal(String completionStatusOriginal)
	{
		this.fieldCompletionStatusOriginal = completionStatusOriginal;
	}

	/**
	 * Returns the value of the instrClass property.
	 * @return the value of the instrClass property
	 */
	public String getInstrClass()
	{
		return this.fieldInstrClass;
	}

	/**
	 * Sets the value of the instrClass property.
	 * @param instrClass the new value of the instrClass property
	 */
	public void setInstrClass(String instrClass)
	{
		this.fieldInstrClass = instrClass;
	}

	/**
	 * Returns the value of the iseq property.
	 * @return the value of the iseq property
	 */
	public String getIseq()
	{
		return this.fieldIseq;
	}

	/**
	 * Sets the value of the iseq property.
	 * @param iseq the new value of the iseq property
	 */
	public void setIseq(String iseq)
	{
		this.fieldIseq = iseq;
	}

	/**
	 * Returns the value of the ssVector property.
	 * @return the value of the ssVector property
	 */
	public Vector <MFSSubInstructionRec>getSsVector()
	{
		return this.fieldSsVector;
	}

	/**
	 * Sets the value of the ssVector property.
	 * @param ssVector the new value of the ssVector property
	 */
	public void setSsVector(Vector <MFSSubInstructionRec>ssVector)
	{
		this.fieldSsVector = ssVector;
	}

	/**
	 * Returns the value of the suff property.
	 * @return the value of the suff property
	 */
	public String getSuff()
	{
		return this.fieldSuff;
	}

	/**
	 * Sets the value of the suff property.
	 * @param suff the new value of the suff property
	 */
	public void setSuff(String suff)
	{
		this.fieldSuff = suff;
	}
}
