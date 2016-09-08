/* © Copyright IBM Corporation 2005, 2007. All rights reserved.
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
 * <code>FuseWrap</code> is a Java bean wrapper around the fusewrap DLL.
 * @author The MFS Client Development Team
 */
public class FuseWrap
	extends Thread
{
	/** The <code>PropertyChangeSupport</code> for this Java bean. */
	protected transient PropertyChangeSupport propertyChange;

	private String fieldRcStr = ""; //$NON-NLS-1$
	private int fieldRc = 0;
	private String fieldErr = ""; //$NON-NLS-1$
	private String fieldBPort = ""; //$NON-NLS-1$
	private String fieldFuseA = ""; //$NON-NLS-1$
	private String fieldFuseB = ""; //$NON-NLS-1$
	private String fieldFuseC = ""; //$NON-NLS-1$
	private String fieldFuseD = ""; //$NON-NLS-1$
	private String fieldSpcnPort = ""; //$NON-NLS-1$
	private String fieldBurnPort = ""; //$NON-NLS-1$
	private long fieldFlags = 0;
	private int fieldMode = 0;
	private int fieldFuseCount = 0;
	private int fieldSPort = 0;

	/** Constructs a new <code>FuseWrap</code>. */
	public FuseWrap()
	{
		loaddll();
	}

	/**
	 * Constructs a new <code>FuseWrap</code>.
	 * @param mod
	 * @param flagbits
	 * @param fuseCnt
	 * @param bPort
	 * @param spcnport
	 * @param fuseA
	 * @param fuseB
	 * @param fuseC
	 * @param fuseD
	 * @param error
	 */
	public FuseWrap(int mod, long flagbits, int fuseCnt, String bPort, int spcnport,
					String fuseA, String fuseB, String fuseC, String fuseD, String error)
	{
		setMode(mod);
		setFlags(flagbits);
		setFuseCount(fuseCnt);
		setBurnPort(bPort);
		setSPort(spcnport);
		setFuseA(fuseA);
		setFuseB(fuseB);
		setFuseC(fuseC);
		setFuseD(fuseD);
		setErr(error);

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
	 * Gets the burnPort property (String) value.
	 * @return The burnPort property value.
	 * @see #setBurnPort
	 */
	public String getBurnPort()
	{
		return this.fieldBurnPort;
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
	 * Gets the flags property (long) value.
	 * @return The flags property value.
	 * @see #setFlags
	 */
	public long getFlags()
	{
		return this.fieldFlags;
	}

	/**
	 * Gets the fuseA property (String) value.
	 * @return The fuseA property value.
	 * @see #setFuseA
	 */
	public String getFuseA()
	{
		return this.fieldFuseA;
	}

	/**
	 * Gets the fuseB property (String) value.
	 * @return The fuseB property value.
	 * @see #setFuseB
	 */
	public String getFuseB()
	{
		return this.fieldFuseB;
	}

	/**
	 * Gets the fuseC property (String) value.
	 * @return The fuseC property value.
	 * @see #setFuseC
	 */
	public String getFuseC()
	{
		return this.fieldFuseC;
	}

	/**
	 * Gets the fuseCount property (int) value.
	 * @return The fuseCount property value.
	 * @see #setFuseCount
	 */
	public int getFuseCount()
	{
		return this.fieldFuseCount;
	}

	/**
	 * Gets the fuseD property (String) value.
	 * @return The fuseD property value.
	 * @see #setFuseD
	 */
	public String getFuseD()
	{
		return this.fieldFuseD;
	}

	/**
	 * Gets the mode property (int) value.
	 * @return The mode property value.
	 * @see #setMode
	 */
	public int getMode()
	{
		return this.fieldMode;
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
	 * Gets the sPort property (int) value.
	 * @return The sPort property value.
	 * @see #setSPort
	 */
	public int getSPort()
	{
		return this.fieldSPort;
	}

	/** Loads the fusewrap DLL. */
	public void loaddll()
	{
		try
		{
			System.loadLibrary("fusewrap"); //$NON-NLS-1$
		}
		catch (Throwable t)
		{
			setRc(10);
			setErr("Error loading fusewrap dll");
		}
	}

	/**
	 * Performs the native load fuse data method.
	 * @param Mode
	 * @param flagBits
	 * @param fuseCount
	 * @param bPort
	 * @param spcnPort
	 * @param fuseA
	 * @param fuseB
	 * @param fuseC
	 * @param fuseD
	 * @param error
	 * @return the return code and message <code>String</code>
	 */
	public native String LoadFuseData(int Mode, long flagBits, int fuseCount,
										String bPort, int spcnPort, String fuseA,
										String fuseB, String fuseC, String fuseD,
										String error);

	/**
	 * Removes a <code>PropertyChangeListener</code> from the listener list.
	 * @param listener the <code>PropertyChangeListener</code> to remove
	 */
	public synchronized void removePropertyChangeListener(PropertyChangeListener listener)
	{
		getPropertyChange().removePropertyChangeListener(listener);
	}

	/** Executes {@link #LoadFuseData}. */
	public void run()
	{
		String return_string = LoadFuseData(getMode(), getFlags(), getFuseCount(),
				getBurnPort(), getSPort(), getFuseA(), getFuseB(), getFuseC(),
				getFuseD(), getErr());

		int delimIndex = return_string.indexOf('|');
		setRc(Integer.parseInt(return_string.substring(0, delimIndex)));
		setRcStr(return_string.substring(delimIndex));
	}

	/**
	 * Sets the burnPort property (String) value.
	 * @param burnPort The new value for the property.
	 * @see #getBurnPort
	 */
	public void setBurnPort(String burnPort)
	{
		String oldValue = this.fieldBurnPort;
		this.fieldBurnPort = burnPort;
		firePropertyChange("burnPort", oldValue, burnPort); //$NON-NLS-1$
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
	 * Sets the flags property (long) value.
	 * @param flags The new value for the property.
	 * @see #getFlags
	 */
	public void setFlags(long flags)
	{
		long oldValue = this.fieldFlags;
		this.fieldFlags = flags;
		firePropertyChange("flags", new Long(oldValue), new Long(flags)); //$NON-NLS-1$
	}

	/**
	 * Sets the fuseA property (String) value.
	 * @param fuseA The new value for the property.
	 * @see #getFuseA
	 */
	public void setFuseA(String fuseA)
	{
		String oldValue = this.fieldFuseA;
		this.fieldFuseA = fuseA;
		firePropertyChange("fuseA", oldValue, fuseA); //$NON-NLS-1$
	}

	/**
	 * Sets the fuseB property (String) value.
	 * @param fuseB The new value for the property.
	 * @see #getFuseB
	 */
	public void setFuseB(String fuseB)
	{
		String oldValue = this.fieldFuseB;
		this.fieldFuseB = fuseB;
		firePropertyChange("fuseB", oldValue, fuseB); //$NON-NLS-1$
	}

	/**
	 * Sets the fuseC property (String) value.
	 * @param fuseC The new value for the property.
	 * @see #getFuseC
	 */
	public void setFuseC(String fuseC)
	{
		String oldValue = this.fieldFuseC;
		this.fieldFuseC = fuseC;
		firePropertyChange("fuseC", oldValue, fuseC); //$NON-NLS-1$
	}

	/**
	 * Sets the fuseCount property (int) value.
	 * @param fuseCount The new value for the property.
	 * @see #getFuseCount
	 */
	public void setFuseCount(int fuseCount)
	{
		int oldValue = this.fieldFuseCount;
		this.fieldFuseCount = fuseCount;
		firePropertyChange("fuseCount", new Integer(oldValue), new Integer(fuseCount)); //$NON-NLS-1$
	}

	/**
	 * Sets the fuseD property (String) value.
	 * @param fuseD The new value for the property.
	 * @see #getFuseD
	 */
	public void setFuseD(String fuseD)
	{
		String oldValue = this.fieldFuseD;
		this.fieldFuseD = fuseD;
		firePropertyChange("fuseD", oldValue, fuseD); //$NON-NLS-1$
	}

	/**
	 * Sets the mode property (int) value.
	 * @param mode The new value for the property.
	 * @see #getMode
	 */
	public void setMode(int mode)
	{
		int oldValue = this.fieldMode;
		this.fieldMode = mode;
		firePropertyChange("mode", new Integer(oldValue), new Integer(mode)); //$NON-NLS-1$
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
	 * Sets the sPort property (int) value.
	 * @param sPort The new value for the property.
	 * @see #getSPort
	 */
	public void setSPort(int sPort)
	{
		int oldValue = this.fieldSPort;
		this.fieldSPort = sPort;
		firePropertyChange("sPort", new Integer(oldValue), new Integer(sPort)); //$NON-NLS-1$
	}
}
