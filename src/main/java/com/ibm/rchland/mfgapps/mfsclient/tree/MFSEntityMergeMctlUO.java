/* @ Copyright IBM Corporation 2008. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2008-03-15      37616JL  R Prechel        -Initial version
 * 2010-01-20 ~1   37550JL  D Kloepping      -Changed cntr element to mctlCntr, being
 * 											  confused with cntr attribute
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.tree;

/**
 * <code>MFSEntityMergeMctlUO</code> is the <code>MFSEntityMergeUO</code>
 * for a work unit control number (MCTL) node.
 * @author The MFS Client Development Team
 */
public class MFSEntityMergeMctlUO
	extends MFSEntityMergeUO
{
	/** The manufacturing control number. */
	String mctl = null;

    /** The work unit type. */
    String wtyp = null;

	/** The product line. */
	String prln = null;

	/** The product ID. */
	String prod = null;

	/** The order status. */
	String cods = null;

	/** The operation number. */
	String nmbr = null;

	/** The cell. */
	String cell = null;

    /** The container. */
    String mctlCntr = null; //~1C//

	/**
	 * Constructs a new <code>MFSEntityMergeMctlUO</code>.
	 * @param mctl the manufacturing control number
	 */
	public MFSEntityMergeMctlUO(String mctl)
	{
		super();
		this.mctl = mctl;
	}

	/** {@inheritDoc} */
	protected String createString(String type)
	{
		StringBuffer buffer = new StringBuffer(100);
		String separator = bundle.getString(type + "Separator"); //$NON-NLS-1$
		buffer.append(bundle.getString(type + "MCTL")); //$NON-NLS-1$
		buffer.append(this.mctl);

		if (display(type, this.prln))
		{
			buffer.append(separator);
			buffer.append(bundle.getString(type + "PRLN")); //$NON-NLS-1$
			buffer.append(this.prln);
		}

		if (display(type, this.prod))
		{
			buffer.append(separator);
			buffer.append(bundle.getString(type + "PROD")); //$NON-NLS-1$
			buffer.append(this.prod);
		}

		if (display(type, this.cods))
		{
			buffer.append(separator);
			buffer.append(bundle.getString(type + "CODS")); //$NON-NLS-1$
			buffer.append(this.cods);
		}

		if (display(type, this.nmbr))
		{
			buffer.append(separator);
			buffer.append(bundle.getString(type + "NMBR")); //$NON-NLS-1$
			buffer.append(this.nmbr);
		}

		if (display(type, this.cell))
		{
			buffer.append(separator);
			buffer.append(bundle.getString(type + "CELL")); //$NON-NLS-1$
			buffer.append(this.cell);
		}
		return buffer.toString();
	}
    
    public String getMctl()
    {
        return this.mctl;
    }
    
    public String getWtyp()
    {
        return this.wtyp;
    }
    
    public String getPrln()
    {
        return this.prln;
    }
    
    public String getNmbr()
    {
        return this.nmbr;
    }
    
    public String getCntr()
    {
        String container = this.mctlCntr; //~1C//
        if (this.mctlCntr==null)		  //~1C//
        {
            container =  "";
        }
        return container;
    }
}
