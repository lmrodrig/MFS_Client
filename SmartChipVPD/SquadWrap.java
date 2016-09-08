/* © Copyright IBM Corporation 2005, 2008. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2007-01-15      34242JR  R Prechel        -Java 5 version
 * 2008-02-01	~1 40852PT  Dave Fichtinger	 -add voltage field and modify native call to pass it
 * 2008-02-08   ~2 40852PT  R Prechel        -Remove extra methods.
 ******************************************************************************/
package SmartChipVPD;

/**
 * <code>SquadWrap</code> is a Java bean wrapper around the squadwrap DLL.
 * @author The MFS Client Development Team
 */
public class SquadWrap
	extends Thread
{
	private String fieldErr = ""; //$NON-NLS-1$
	private int fieldRc = 0;
	private String fieldRcStr = ""; //$NON-NLS-1$
	final private String fieldElevens; //$NON-NLS-1$
	final private String fieldBport; //$NON-NLS-1$
	final private String fieldEcid; //$NON-NLS-1$
	final private String fieldFile; //$NON-NLS-1$
	final private String fieldMode; //$NON-NLS-1$
	final private String fieldFormat; //$NON-NLS-1$
	final private String fieldPartNum; //$NON-NLS-1$
	final private String fieldRedundantFlag; //$NON-NLS-1$
	final private String fieldVoltage; //$NON-NLS-1$  //~1

	//~1A added voltage parameter for specifically setting in the underlying
	// dll parameters
	/**
	 * Constructs a new <code>SquadWrap</code>.
	 * @param mode
	 * @param file
	 * @param bprt
	 * @param elevens
	 * @param format
	 * @param ecid
	 * @param pn
	 * @param redundantFlag
	 * @param voltage
	 */
	public SquadWrap(String mode, String file, String bprt, String elevens,
						String format, String ecid, String pn, String redundantFlag,
						String voltage)
	{
		//~2C Remove set methods
		this.fieldMode = mode;
		this.fieldFile = file;
		this.fieldBport = bprt;
		this.fieldElevens = elevens;
		this.fieldFormat = format;
		this.fieldEcid = ecid;
		this.fieldPartNum = pn;
		this.fieldRedundantFlag = redundantFlag;
		this.fieldVoltage = voltage; //~1A
		loaddll();
	}

	/**
	 * Returns the err property value.
	 * @return the err property value
	 */
	public String getErr()
	{
		return this.fieldErr;
	}

	/**
	 * Returns the rc property value.
	 * @return the rc property value.
	 */
	public int getRc()
	{
		return this.fieldRc;
	}

	/**
	 * Returns the rcStr property value.
	 * @return The rcStr property value
	 */
	public String getRcStr()
	{
		return this.fieldRcStr;
	}

	/** Loads the squadwrap DLL. */
	public void loaddll()
	{
		try
		{
			System.loadLibrary("squadwrap"); //$NON-NLS-1$
		}
		catch (Throwable t)
		{
			this.fieldRc = 10;
			this.fieldErr = "Error loading SquadWrap dll";
		}
	}

	/**
	 * Performs the native VPD Burn Squad method.
	 * @param Mode
	 * @param file
	 * @param elevens
	 * @param BurnPort
	 * @param Format
	 * @param ecid
	 * @param partNum
	 * @param redundantFlag
	 * @param voltage
	 * @param ErrorMsg
	 * @return the return code and message <code>String</code>
	 */
	public native String MfsVpdBurnSquad(String Mode, String file, String elevens,
											String BurnPort, String Format, String ecid,
											String partNum, String redundantFlag,
											String voltage, String ErrorMsg);

	/** Executes {@link #MfsVpdBurnSquad}. */
	public void run()
	{
		//~2C Remove get methods; access fields directly
		String return_string = MfsVpdBurnSquad(this.fieldMode, this.fieldFile,
				this.fieldElevens, this.fieldBport, this.fieldFormat, this.fieldEcid,
				this.fieldPartNum, this.fieldRedundantFlag, this.fieldVoltage,
				this.fieldErr);
		int delimIndex = return_string.indexOf('|');
		this.fieldRc = Integer.parseInt(return_string.substring(0, delimIndex));
		this.fieldRcStr = return_string.substring(delimIndex);
	}
}
