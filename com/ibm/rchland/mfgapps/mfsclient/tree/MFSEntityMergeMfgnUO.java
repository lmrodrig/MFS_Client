/* © Copyright IBM Corporation 2008, 2011. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2008-03-15      37616JL  R Prechel        -Initial version
 * 2009-08-31 ~1   37550JL  Ray Perry        -Add config item for Build Ahead
 * 2010-01-05   ~2 46531TL  Santiago SC      -Add typz
 * 2011-10-19 ~3   00177780 Giovanni Toledo  -Add baFlag in createString method
 * 2011-10-19 ~4   00177780 Giovanni Toledo  -Add get methods to use in MFSEntityMergeTreeCellRenderer class
 * 2011-12-15 ~05  De623477 VH Avila         -Rename validateBlanks by hasAlterationsOrHolds.
 *                                           -Remake the validations of the holds, because never are blanks.
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.tree;

/**
 * <code>MFSEntityMergeMfgnUO</code> is the <code>MFSEntityMergeUO</code>
 * for a manufacturing control number (MFGN) node.
 * @author The MFS Client Development Team
 */
public class MFSEntityMergeMfgnUO
	extends MFSEntityMergeUO
{
	/** The manufacturing control number. */
	String mfgn = null;

	/** The system type. */
	String idss = null;

	/** The order status. */
	String cods = null;

	/** The primary product line. */
	String pprl = null;

	/** The system number hold flag. */
	String sysh = null;

	/** The general hold flag. */
	String genh = null;

	/** The scheduled ship date. */
	String schd = null;
	
	/** Alter indicator */
	char alti = ' ';

    /** ~1 The config item. */
    String item = null;
    
    /** ~2A Type of System */
    String typz = null;

	/**
	 * Constructs a new <code>MFSEntityMergeMfgnUO</code>.
	 * @param mfgn the manufacturing control number
	 */
	public MFSEntityMergeMfgnUO(String mfgn, String item, String pprl)
	{
		super();
		this.mfgn = mfgn;
		this.item = item;
		this.pprl = pprl;
	}

	/** {@inheritDoc} */
	protected String createString(String type)
	{
		StringBuffer buffer = new StringBuffer(100);
		String separator = bundle.getString(type + "Separator"); //$NON-NLS-1$
		buffer.append(bundle.getString(type + "MFGN")); //$NON-NLS-1$
		buffer.append(this.mfgn);
		
		Boolean baFlag = this.pprl.trim().equals("BAPRLN"); //~3 add baFlag and check if it's BAPRLN

		if (display(type, this.idss) && baFlag)//~3 add baFlag
		{
			buffer.append(separator);
			buffer.append(bundle.getString(type + "IDSS")); //$NON-NLS-1$
			buffer.append(this.idss);
		}

        // ~1 show config item
        if (display(type, this.item) && baFlag)//~3 add baFlag
        {
            buffer.append(separator);
            buffer.append(bundle.getString(type + "ITEM")); //$NON-NLS-1$
            buffer.append(this.item);
        }

		if (display(type, this.cods))
		{
			buffer.append(separator);
			buffer.append(bundle.getString(type + "CODS")); //$NON-NLS-1$
			buffer.append(this.cods);
		}

		if (display(type, this.pprl))
		{
			buffer.append(separator);
			buffer.append(bundle.getString(type + "PPRL")); //$NON-NLS-1$
			buffer.append(this.pprl);
		}

		if (display(type, this.sysh) && baFlag)//~3 add baFlag
		{
			buffer.append(separator);
			buffer.append(bundle.getString(type + "SYSH")); //$NON-NLS-1$
			buffer.append(this.sysh);
		}

		if (display(type, this.genh) && baFlag)//~3 add baFlag
		{
			buffer.append(separator);
			buffer.append(bundle.getString(type + "GENH")); //$NON-NLS-1$
			buffer.append(this.genh);
		}

		if (display(type, this.schd) && baFlag)//~3 add baFlag
		{
			buffer.append(separator);
			buffer.append(bundle.getString(type + "SCHD")); //$NON-NLS-1$
			buffer.append(this.schd);
		}
		
		//~2A
		if (display(type, this.typz))
		{
			buffer.append(separator);
			buffer.append(bundle.getString(type + "TYPZ")); //$NON-NLS-1$
			buffer.append(this.typz);
		}

		if(baFlag)//~3 add if(baFlag)
		{
			buffer.append(separator);
			buffer.append(bundle.getString(type + "ALTI")); //$NON-NLS-1$
			buffer.append(this.alti);
		}
		
		return buffer.toString();
	}

	/**
	 * Returns the manufacturing control number.
	 * @return the manufacturing control number
	 */
	public String getMfgn()
	{
		return this.mfgn;
	}

	/**
	 * Returns the system type.
	 * @return the system type
	 */
	public String getIdss()
	{
		return this.idss;
	}
	
	public String getPprl()//~4
	{
		return this.pprl;
	}
	/**
	 * Validates if alti, sysh and genh fields are blanks
	 * 
	 * @return boolean - true if alti, sysh and genh are blanks, false if at
	 *         least one of them contains information
	 */
	public boolean hasAlterationsOrHolds()//~4 validate alti, sysh and genh for blank fields
	{
		boolean holdOrAlter = false;
		
		if((this.alti != ' ') && (this.alti != '0'))
		{
			holdOrAlter = true;
		}
		if(!this.sysh.equals("") && !this.sysh.equals("0"))
		{
			holdOrAlter = true;
		}
		
		if(!this.genh.equals("") && !this.genh.equals("00"))
		{
			holdOrAlter = true;
		}
		
		return holdOrAlter;
	}
	
	/**
	 * Validates if typZ field is either 1 or 2
	 * 
	 * @return boolean - false if typz is 1 or 2, true if typz contains any other value
	 */
	public boolean validateTypZ()//~4
	{
		boolean typZ12 = true;
		
		if(this.typz.equals("1") || this.typz.equals("2"))
		{
			typZ12 = false;
		}
		
		return typZ12;
	}

	/**
	 * Returns the value of the alti property.
	 * @return the value of the alti property
	 */
	public char getAlti()
	{
		return this.alti;
	}

	public String getItem() {
		return item;
	}
	
	/**
	 * @return the value of the cods property.
	 */
	public String getCods() {
		return cods;
	}

	public void setItem(String item) {
		this.item = item;
	}
}