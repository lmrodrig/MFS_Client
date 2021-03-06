/* � Copyright IBM Corporation 2005, 2007. All rights reserved.
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
 * <code>SmartWrap</code> is a Java bean wrapper around the smartwrap DLL.
 * @author The MFS Client Development Team
 */
public class SmartWrap
	extends Thread
{
	/** The <code>PropertyChangeSupport</code> for this Java bean. */
	protected transient PropertyChangeSupport propertyChange;

	private int fieldMode = 0;
	private String fieldMifdta = ""; //$NON-NLS-1$
	private long fieldFlags = 0;
	private String fieldUser = ""; //$NON-NLS-1$
	private String fieldErr = ""; //$NON-NLS-1$
	private int fieldRc = 0;
	private String fieldRcStr = ""; //$NON-NLS-1$
	private int fieldBprt = 0;
	private String fieldBport = ""; //$NON-NLS-1$

	/** Constructs a new <code>SmartWrap</code>. */
	public SmartWrap()
	{
		loaddll();
	}

	/**
	 * Constructs a new <code>SmartWrap</code>.
	 * @param mode
	 * @param user
	 * @param flags
	 * @param bprt
	 * @param mifdta
	 * @param err
	 */
	public SmartWrap(int mode, String user, long flags, String bprt, String mifdta,
						String err)
	{
		setMode(mode);
		setUser(user);
		setFlags(flags);
		setBport(bprt);
		setMifdta(mifdta);
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
	 * Gets the bport property (String) value.
	 * @return The bport property value.
	 * @see #setBport
	 */
	public String getBport()
	{
		return this.fieldBport;
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
	 * Gets the mifdta property (String) value.
	 * @return The mifdta property value.
	 * @see #setMifdta
	 */
	public String getMifdta()
	{
		return this.fieldMifdta;
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
	 * Gets the user property (String) value.
	 * @return The user property value.
	 * @see #setUser
	 */
	public String getUser()
	{
		return this.fieldUser;
	}

	/** Loads the smartwrap DLL. */
	public void loaddll()
	{
		try
		{
			System.loadLibrary("smartwrap"); //$NON-NLS-1$
		}
		catch (Throwable t)
		{
			setRc(10);
			setErr("Error loading SmartWrap dll");
		}
	}

	/**
	 * Performs the native VPD Burn method.
	 * @param Mode
	 * @param CallerID
	 * @param FlagBits
	 * @param BurnPort
	 * @param ByteData
	 * @param ErrorMsg
	 * @return the return code and message <code>String</code>
	 */
	public native String MfsVpdBurn(int Mode, String CallerID, long FlagBits,
									String BurnPort, String ByteData, String ErrorMsg);

	/**
	 * Removes a <code>PropertyChangeListener</code> from the listener list.
	 * @param listener the <code>PropertyChangeListener</code> to remove
	 */
	public synchronized void removePropertyChangeListener(PropertyChangeListener listener)
	{
		getPropertyChange().removePropertyChangeListener(listener);
	}

	/** Executes {@link #MfsVpdBurn}. */
	public void run()
	{
		String return_string = MfsVpdBurn(getMode(), getUser(), getFlags(), getBport(),
				getMifdta(), getErr());

		int delimIndex = return_string.indexOf('|');
		setRc(Integer.parseInt(return_string.substring(0, delimIndex)));
		setRcStr(return_string.substring(delimIndex));
	}

	/**
	 * Sets the bport property (String) value.
	 * @param bport The new value for the property.
	 * @see #getBport
	 */
	public void setBport(String bport)
	{
		String oldValue = this.fieldBport;
		this.fieldBport = bport;
		firePropertyChange("bport", oldValue, bport); //$NON-NLS-1$
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
	 * Sets the mifdta property (String) value.
	 * @param mifdta The new value for the property.
	 * @see #getMifdta
	 */
	public void setMifdta(String mifdta)
	{
		String oldValue = this.fieldMifdta;
		this.fieldMifdta = mifdta;
		firePropertyChange("mifdta", oldValue, mifdta); //$NON-NLS-1$
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
	 * Sets the user property (String) value.
	 * @param user The new value for the property.
	 * @see #getUser
	 */
	public void setUser(String user)
	{
		String oldValue = this.fieldUser;
		this.fieldUser = user;
		firePropertyChange("user", oldValue, user); //$NON-NLS-1$
	}
}
