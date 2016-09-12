/* @ Copyright IBM Corporation 2005, 2007. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2007-01-15      34242JR  R Prechel        -Java 5 version
 ******************************************************************************/
package SmartChipVPD;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * <code>DasdWrap</code> is a Java bean wrapper around the dasdwrap DLL.
 * @author The MFS Client Development Team
 */
public class DasdWrap
	extends Thread
{
	/** The <code>PropertyChangeSupport</code> for this Java bean. */
	protected transient PropertyChangeSupport propertyChange;

	private String fieldErr = ""; //$NON-NLS-1$
	private int fieldRc = 0;
	private String fieldRcStr = ""; //$NON-NLS-1$
	private int fieldBprt = 0;
	private String fieldSerialNum = ""; //$NON-NLS-1$
	private String fieldGenericPartNum = ""; //$NON-NLS-1$
	private String fieldIntermediatePartNum = ""; //$NON-NLS-1$
	private String fieldFinalDrivePartNum = ""; //$NON-NLS-1$
	private String fieldSpeedInfo = ""; //$NON-NLS-1$
	private String fieldChangeDef1 = ""; //$NON-NLS-1$
	private String fieldChangeDef2 = ""; //$NON-NLS-1$
	private String fieldChangeDef3 = ""; //$NON-NLS-1$
	private String fieldLocalFileName = ""; //$NON-NLS-1$
	private String fieldLoadId = ""; //$NON-NLS-1$
	private String fieldRevisionLevel = ""; //$NON-NLS-1$
	private String fieldCodeLevel = ""; //$NON-NLS-1$
	private String fieldUnlockSupported = ""; //$NON-NLS-1$
	private int fieldScsiSlot = 0;
	private int fieldPwdIndex = 0;

	/** Constructs a new <code>DasdWrap</code>. */
	public DasdWrap()
	{
		loaddll();
	}

	/**
	 * Constructs a new <code>DasdWrap</code>.
	 * @param serNum
	 * @param gnpn
	 * @param itpn
	 * @param fdpn
	 * @param spdInfo
	 * @param pwdIndex
	 * @param cd1
	 * @param cd2
	 * @param cd3
	 * @param fileName
	 * @param scsiID
	 * @param loadID
	 * @param revlvl
	 * @param codeLevel
	 * @param unlockSupported
	 * @param err
	 */
	public DasdWrap(String serNum, String gnpn, String itpn, String fdpn, String spdInfo,
					int pwdIndex, String cd1, String cd2, String cd3, String fileName,
					int scsiID, String loadID, String revlvl, String codeLevel,
					String unlockSupported, String err)
	{
		setSerialNum(serNum);
		setGenericPartNum(gnpn);
		setIntermediatePartNum(itpn);
		setFinalDrivePartNum(fdpn);
		setSpeedInfo(spdInfo);
		setPwdIndex(pwdIndex);
		setChangeDef1(cd1);
		setChangeDef2(cd2);
		setChangeDef3(cd3);
		setLocalFileName(fileName);
		setScsiSlot(scsiID);
		setLoadId(loadID);
		setRevisionLevel(revlvl);
		setCodeLevel(codeLevel);
		setUnlockSupported(unlockSupported);
		setErr(err);
		loaddll();
	}

	/**
	 * Adds a <code>PropertyChangeListener</code> to the listener list.
	 * @param listener the <code>PropertyChangeListener</code> to add
	 */
	public synchronized void addPropertyChangeListener(PropertyChangeListener listener)
	{
		getPropertyChange().addPropertyChangeListener(listener);
	}

	/**
	 * Performs the native DASD personalize method.
	 * @param serNum
	 * @param gnpn
	 * @param itpn
	 * @param fdpn
	 * @param speedInfo
	 * @param pwdIndex
	 * @param cd1
	 * @param cd2
	 * @param cd3
	 * @param fileName
	 * @param scsiID
	 * @param loadID
	 * @param revlvl
	 * @param codelvl
	 * @param unlockSupported
	 * @param ErrorMsg
	 * @return the return code and message <code>String</code>
	 */
	public native String DASDPersonalize(String serNum, String gnpn, String itpn,
											String fdpn, String speedInfo, int pwdIndex,
											String cd1, String cd2, String cd3,
											String fileName, int scsiID, String loadID,
											String revlvl, String codelvl,
											String unlockSupported, String ErrorMsg);

	/**
	 * Reports a boolean bound property update to any registered listeners.
	 * @param propertyName the programmatic name of the property that was
	 *        changed
	 * @param oldValue the old value of the property
	 * @param newValue the new value of the property
	 */
	public void firePropertyChange(String propertyName, Object oldValue, Object newValue)
	{
		getPropertyChange().firePropertyChange(propertyName, oldValue, newValue);
	}

	/**
	 * Gets the changeDef1 property (String) value.
	 * @return The changeDef1 property value.
	 * @see #setChangeDef1
	 */
	public String getChangeDef1()
	{
		return this.fieldChangeDef1;
	}

	/**
	 * Gets the changeDef2 property (String) value.
	 * @return The changeDef2 property value.
	 * @see #setChangeDef2
	 */
	public String getChangeDef2()
	{
		return this.fieldChangeDef2;
	}

	/**
	 * Gets the changeDef3 property (String) value.
	 * @return The changeDef3 property value.
	 * @see #setChangeDef3
	 */
	public String getChangeDef3()
	{
		return this.fieldChangeDef3;
	}

	/**
	 * Gets the codeLevel property (String) value.
	 * @return The codeLevel property value.
	 * @see #setCodeLevel
	 */
	public String getCodeLevel()
	{
		return this.fieldCodeLevel;
	}

	/**
	 * Gets the err property (String) value.
	 * @return The err property value.
	 * @see #setErr
	 */
	public String getErr()
	{
		return this.fieldErr;
	}

	/**
	 * Gets the finalDrivePartNum property (String) value.
	 * @return The finalDrivePartNum property value.
	 * @see #setFinalDrivePartNum
	 */
	public String getFinalDrivePartNum()
	{
		return this.fieldFinalDrivePartNum;
	}

	/**
	 * Gets the genericPartNum property (String) value.
	 * @return The genericPartNum property value.
	 * @see #setGenericPartNum
	 */
	public String getGenericPartNum()
	{
		return this.fieldGenericPartNum;
	}

	/**
	 * Gets the intermediatePartNum property (String) value.
	 * @return The intermediatePartNum property value.
	 * @see #setIntermediatePartNum
	 */
	public String getIntermediatePartNum()
	{
		return this.fieldIntermediatePartNum;
	}

	/**
	 * Gets the loadId property (String) value.
	 * @return The loadId property value.
	 * @see #setLoadId
	 */
	public String getLoadId()
	{
		return this.fieldLoadId;
	}

	/**
	 * Gets the localFileName property (String) value.
	 * @return The localFileName property value.
	 * @see #setLocalFileName
	 */
	public String getLocalFileName()
	{
		return this.fieldLocalFileName;
	}

	/**
	 * Returns the <code>PropertyChangeSupport</code> for this Java bean.
	 * @return the <code>PropertyChangeSupport</code> for this Java bean
	 */
	protected PropertyChangeSupport getPropertyChange()
	{
		if (this.propertyChange == null)
		{
			this.propertyChange = new PropertyChangeSupport(this);
		}
		return this.propertyChange;
	}

	/**
	 * Gets the pwdIndex property (int) value.
	 * @return The pwdIndex property value.
	 * @see #setPwdIndex
	 */
	public int getPwdIndex()
	{
		return this.fieldPwdIndex;
	}

	/**
	 * Gets the rc property (int) value.
	 * @return The rc property value.
	 * @see #setRc
	 */
	public int getRc()
	{
		return this.fieldRc;
	}

	/**
	 * Gets the rcStr property (String) value.
	 * @return The rcStr property value.
	 * @see #setRcStr
	 */
	public String getRcStr()
	{
		return this.fieldRcStr;
	}

	/**
	 * Gets the revisionLevel property (String) value.
	 * @return The revisionLevel property value.
	 * @see #setRevisionLevel
	 */
	public String getRevisionLevel()
	{
		return this.fieldRevisionLevel;
	}

	/**
	 * Gets the scsiSlot property (int) value.
	 * @return The scsiSlot property value.
	 * @see #setScsiSlot
	 */
	public int getScsiSlot()
	{
		return this.fieldScsiSlot;
	}

	/**
	 * Gets the serialNum property (String) value.
	 * @return The serialNum property value.
	 * @see #setSerialNum
	 */
	public String getSerialNum()
	{
		return this.fieldSerialNum;
	}

	/**
	 * Gets the speedInfo property (String) value.
	 * @return The speedInfo property value.
	 * @see #setSpeedInfo
	 */
	public String getSpeedInfo()
	{
		return this.fieldSpeedInfo;
	}

	/**
	 * Gets the unlockSupported property (String) value.
	 * @return The unlockSupported property value.
	 * @see #setUnlockSupported
	 */
	public String getUnlockSupported()
	{
		return this.fieldUnlockSupported;
	}

	/** Loads the dasdwrap DLL. */
	public void loaddll()
	{
		try
		{
			System.loadLibrary("dasdwrap"); //$NON-NLS-1$
		}
		catch (Throwable t)
		{
			setRc(10);
			setErr("Error loading dasdwrap.dll");
		}
	}

	/**
	 * Removes a <code>PropertyChangeListener</code> from the listener list.
	 * @param listener the <code>PropertyChangeListener</code> to remove
	 */
	public synchronized void removePropertyChangeListener(PropertyChangeListener listener)
	{
		getPropertyChange().removePropertyChangeListener(listener);
	}

	/** Executes {@link #DASDPersonalize}. */
	public void run()
	{
		String return_string = DASDPersonalize(getSerialNum(), getGenericPartNum(),
				getIntermediatePartNum(), getFinalDrivePartNum(), getSpeedInfo(),
				getPwdIndex(), getChangeDef1(), getChangeDef2(), getChangeDef3(),
				getLocalFileName(), getScsiSlot(), getLoadId(), getRevisionLevel(),
				getCodeLevel(), getUnlockSupported(), getErr());

		int delimIndex = return_string.indexOf('|');
		setRc(Integer.parseInt(return_string.substring(0, delimIndex)));
		setRcStr(return_string.substring(delimIndex));
	}

	/**
	 * Sets the changeDef1 property (String) value.
	 * @param changeDef1 The new value for the property.
	 * @see #getChangeDef1
	 */
	public void setChangeDef1(String changeDef1)
	{
		String oldValue = this.fieldChangeDef1;
		this.fieldChangeDef1 = changeDef1;
		firePropertyChange("changeDef1", oldValue, changeDef1); //$NON-NLS-1$
	}

	/**
	 * Sets the changeDef2 property (String) value.
	 * @param changeDef2 The new value for the property.
	 * @see #getChangeDef2
	 */
	public void setChangeDef2(String changeDef2)
	{
		String oldValue = this.fieldChangeDef2;
		this.fieldChangeDef2 = changeDef2;
		firePropertyChange("changeDef2", oldValue, changeDef2); //$NON-NLS-1$
	}

	/**
	 * Sets the changeDef3 property (String) value.
	 * @param changeDef3 The new value for the property.
	 * @see #getChangeDef3
	 */
	public void setChangeDef3(String changeDef3)
	{
		String oldValue = this.fieldChangeDef3;
		this.fieldChangeDef3 = changeDef3;
		firePropertyChange("changeDef3", oldValue, changeDef3); //$NON-NLS-1$
	}

	/**
	 * Sets the codeLevel property (String) value.
	 * @param codeLevel The new value for the property.
	 * @see #getCodeLevel
	 */
	public void setCodeLevel(String codeLevel)
	{
		String oldValue = this.fieldCodeLevel;
		this.fieldCodeLevel = codeLevel;
		firePropertyChange("codeLevel", oldValue, codeLevel); //$NON-NLS-1$
	}

	/**
	 * Sets the err property (String) value.
	 * @param err The new value for the property.
	 * @see #getErr
	 */
	public void setErr(String err)
	{
		String oldValue = this.fieldErr;
		this.fieldErr = err;
		firePropertyChange("err", oldValue, err); //$NON-NLS-1$
	}

	/**
	 * Sets the finalDrivePartNum property (String) value.
	 * @param finalDrivePartNum The new value for the property.
	 * @see #getFinalDrivePartNum
	 */
	public void setFinalDrivePartNum(String finalDrivePartNum)
	{
		String oldValue = this.fieldFinalDrivePartNum;
		this.fieldFinalDrivePartNum = finalDrivePartNum;
		firePropertyChange("finalDrivePartNum", oldValue, finalDrivePartNum); //$NON-NLS-1$
	}

	/**
	 * Sets the genericPartNum property (String) value.
	 * @param genericPartNum The new value for the property.
	 * @see #getGenericPartNum
	 */
	public void setGenericPartNum(String genericPartNum)
	{
		String oldValue = this.fieldGenericPartNum;
		this.fieldGenericPartNum = genericPartNum;
		firePropertyChange("genericPartNum", oldValue, genericPartNum); //$NON-NLS-1$
	}

	/**
	 * Sets the intermediatePartNum property (String) value.
	 * @param intermediatePartNum The new value for the property.
	 * @see #getIntermediatePartNum
	 */
	public void setIntermediatePartNum(String intermediatePartNum)
	{
		String oldValue = this.fieldIntermediatePartNum;
		this.fieldIntermediatePartNum = intermediatePartNum;
		firePropertyChange("intermediatePartNum", oldValue, intermediatePartNum); //$NON-NLS-1$
	}

	/**
	 * Sets the loadId property (String) value.
	 * @param loadId The new value for the property.
	 * @see #getLoadId
	 */
	public void setLoadId(String loadId)
	{
		String oldValue = this.fieldLoadId;
		this.fieldLoadId = loadId;
		firePropertyChange("loadId", oldValue, loadId); //$NON-NLS-1$
	}

	/**
	 * Sets the localFileName property (String) value.
	 * @param localFileName The new value for the property.
	 * @see #getLocalFileName
	 */
	public void setLocalFileName(String localFileName)
	{
		String oldValue = this.fieldLocalFileName;
		this.fieldLocalFileName = localFileName;
		firePropertyChange("localFileName", oldValue, localFileName); //$NON-NLS-1$
	}

	/**
	 * Sets the pwdIndex property (int) value.
	 * @param pwdIndex The new value for the property.
	 * @see #getPwdIndex
	 */
	public void setPwdIndex(int pwdIndex)
	{
		int oldValue = this.fieldPwdIndex;
		this.fieldPwdIndex = pwdIndex;
		firePropertyChange("pwdIndex", new Integer(oldValue), new Integer(pwdIndex)); //$NON-NLS-1$
	}

	/**
	 * Sets the rc property (int) value.
	 * @param rc The new value for the property.
	 * @see #getRc
	 */
	public void setRc(int rc)
	{
		int oldValue = this.fieldRc;
		this.fieldRc = rc;
		firePropertyChange("rc", new Integer(oldValue), new Integer(rc)); //$NON-NLS-1$
	}

	/**
	 * Sets the rcStr property (String) value.
	 * @param rcStr The new value for the property.
	 * @see #getRcStr
	 */
	public void setRcStr(String rcStr)
	{
		String oldValue = this.fieldRcStr;
		this.fieldRcStr = rcStr;
		firePropertyChange("rcStr", oldValue, rcStr); //$NON-NLS-1$
	}

	/**
	 * Sets the revisionLevel property (String) value.
	 * @param revisionLevel The new value for the property.
	 * @see #getRevisionLevel
	 */
	public void setRevisionLevel(String revisionLevel)
	{
		String oldValue = this.fieldRevisionLevel;
		this.fieldRevisionLevel = revisionLevel;
		firePropertyChange("revisionLevel", oldValue, revisionLevel); //$NON-NLS-1$
	}

	/**
	 * Sets the scsiSlot property (int) value.
	 * @param scsiSlot The new value for the property.
	 * @see #getScsiSlot
	 */
	public void setScsiSlot(int scsiSlot)
	{
		int oldValue = this.fieldScsiSlot;
		this.fieldScsiSlot = scsiSlot;
		firePropertyChange("scsiSlot", new Integer(oldValue), new Integer(scsiSlot)); //$NON-NLS-1$
	}

	/**
	 * Sets the serialNum property (String) value.
	 * @param serialNum The new value for the property.
	 * @see #getSerialNum
	 */
	public void setSerialNum(String serialNum)
	{
		String oldValue = this.fieldSerialNum;
		this.fieldSerialNum = serialNum;
		firePropertyChange("serialNum", oldValue, serialNum); //$NON-NLS-1$
	}

	/**
	 * Sets the speedInfo property (String) value.
	 * @param speedInfo The new value for the property.
	 * @see #getSpeedInfo
	 */
	public void setSpeedInfo(String speedInfo)
	{
		String oldValue = this.fieldSpeedInfo;
		this.fieldSpeedInfo = speedInfo;
		firePropertyChange("speedInfo", oldValue, speedInfo); //$NON-NLS-1$
	}

	/**
	 * Sets the unlockSupported property (String) value.
	 * @param unlockSupported The new value for the property.
	 * @see #getUnlockSupported
	 */
	public void setUnlockSupported(String unlockSupported)
	{
		String oldValue = this.fieldUnlockSupported;
		this.fieldUnlockSupported = unlockSupported;
		firePropertyChange("unlockSupported", oldValue, unlockSupported); //$NON-NLS-1$
	}
}
