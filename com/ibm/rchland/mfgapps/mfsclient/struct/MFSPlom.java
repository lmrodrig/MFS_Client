/* © Copyright IBM Corporation 2008. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2008-07-29      38990JL  Santiago D       -Java 5 version
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.struct;

/**
 * <code>MFSPlom</code> is a data structure for plant of manufacturing information.
 * @author The MFS Client Development Team
 */
public class MFSPlom {
	
	private String fieldRec;	
	
	/* Plant of Manufacture */
	private String fieldPlom;
	
	/* SAP Plant */
	private String fieldSplt;
	
	/* Plant Name */
	private String fieldPlnt;
	
	/* World Trade Country Code */
	private String fieldWtcc;
	
	/* Environment Code */
	private String fieldEnvc;
	
	/* PlntDisp */
	private String fieldDisp;

	/** Constructs a new <code>MFSPlom</code>. */
	public MFSPlom()
	{
		super();
	}	
	
	/** Constructs a new <code>MFSPlom</code>. 
	 *  @param xmlRecord String:
	 *  <REC>
	 *     <PLOM>xxx</PLOM> (3L)
	 *     <SPLT>xxxx</SPLT> (4L)
	 *     <PLNT>xxxxxxxx</PLNT> (30L)
	 *     <WTCC>xxxx</WTCC> (3L)
	 *     <ENVC>xxx</ENVC> (1L)
	 *     <DISP>xxxxxx</DISP> (30L)
	 *  </REC>
	 * */
	public MFSPlom(String xmlRecord)
	{
		fieldRec = xmlRecord;
		
		int tagSize = 6;
		
		StringBuffer sb = new StringBuffer();
		
		sb.append(fieldRec);
		
		fieldPlom = sb.substring(sb.indexOf("<PLOM>")+tagSize, sb.indexOf("</PLOM>"));
		
		fieldSplt = sb.substring(sb.indexOf("<SPLT>")+tagSize, sb.indexOf("</SPLT>"));
		
		fieldPlnt = sb.substring(sb.indexOf("<PLNT>")+tagSize, sb.indexOf("</PLNT>"));
		
		fieldWtcc = sb.substring(sb.indexOf("<WTCC>")+tagSize, sb.indexOf("</WTCC>"));
		
		fieldEnvc = sb.substring(sb.indexOf("<ENVC>")+tagSize, sb.indexOf("</ENVC>"));
		
		fieldDisp = sb.substring(sb.indexOf("<DISP>")+tagSize, sb.indexOf("</DISP>"));
	}		
	
	/** Constructs a new <code>MFSPlom</code>.
	 * @param plom
	 * @param splt
	 * @param plnt
	 * @param wtcc
	 * @param envc
	 * @param disp
	 */
	public MFSPlom(String plom, String splt, String plnt, String wtcc, String envc, String disp)
	{
		fieldPlom = plom;
		fieldSplt = splt;
		fieldPlnt = plnt;
		fieldWtcc = wtcc;
		fieldEnvc = envc;
		fieldDisp = disp;
		
		StringBuffer sb = new StringBuffer();
		
		sb.append("<REC>");
		sb.append("<PLOM>"); sb.append(plom); sb.append("</PLOM>");
		sb.append("<SPLT>"); sb.append(splt); sb.append("</SPLT>");
		sb.append("<PLNT>"); sb.append(plnt); sb.append("</PLNT>");
		sb.append("<WTCC>"); sb.append(wtcc); sb.append("</WTCC>");
		sb.append("<ENVC>"); sb.append(envc); sb.append("</ENVC>");
		sb.append("<DISP>"); sb.append(disp); sb.append("</DISP>");
		sb.append("</REC>");
		
		fieldRec = sb.toString();
	}	
	
	/**
	 * Gets the plom property (String) value.
	 * @return The plom property value.
	 * @see #setPlom
	 */
	public String getPlom()
	{
		return this.fieldPlom;
	}	
	
	/**
	 * Gets the splt property (String) value.
	 * @return The splt property value.
	 * @see #setSplt
	 */
	public String getSplt()
	{
		return this.fieldSplt;
	}	
	
	/**
	 * Gets the plnt property (String) value.
	 * @return The plnt property value.
	 * @see #setPlnt
	 */
	public String getPlnt()
	{
		return this.fieldPlnt;
	}	
	
	/**
	 * Gets the wtcc property (String) value.
	 * @return The wtcc property value.
	 * @see #setWtcc
	 */
	public String getWtcc()
	{
		return this.fieldWtcc;
	}
	
	/**
	 * Gets the envc property (String) value.
	 * @return The envc property value.
	 * @see #setEnvc
	 */
	public String getEnvc()
	{
		return this.fieldEnvc;
	}	
	
	/**
	 * Gets the disp property (String) value.
	 * @return The disp property value.
	 * @see #setDisp
	 */
	public String getDisp()
	{
		return this.fieldDisp;
	}	
	
	/**
	 * Gets the rec property (String) value.
	 * @return The rec property value.
	 * @see #setRec
	 */
	public String getRec()
	{
		return this.fieldRec;
	}	
	
	/**
	 * Sets the plom property (String) value.
	 * @param plom The new value for the property.
	 * @see #getplom
	 */
	public void setPlom(String plom)
	{
		this.fieldPlom = plom;
	}	
	
	/**
	 * Sets the splt property (String) value.
	 * @param splt The new value for the property.
	 * @see #getSplt
	 */
	public void setSplt(String splt)
	{
		this.fieldSplt = splt;
	}	
	
	/**
	 * Sets the plnt property (String) value.
	 * @param plnt The new value for the property.
	 * @see #getPlnt
	 */
	public void setPlnt(String plnt)
	{
		this.fieldPlnt = plnt;
	}	
	
	/**
	 * Sets the wtcc property (String) value.
	 * @param wtcc The new value for the property.
	 * @see #getWtcc
	 */
	public void setWtcc(String wtcc)
	{
		this.fieldWtcc = wtcc;
	}	
	
	/**
	 * Sets the envc property (String) value.
	 * @param envc The new value for the property.
	 * @see #getEnvc
	 */
	public void setEnvc(String envc)
	{
		this.fieldEnvc = envc;
	}	
	
	/**
	 * Sets the disp property (String) value.
	 * @param disp The new value for the property.
	 * @see #getDisp
	 */
	public void setDisp(String disp)
	{
		this.fieldDisp = disp;
	}	
	
	/**
	 * Sets the rec property (String) value.
	 * @param rec The new value for the property.
	 * @see #getRec
	 */
	public void setRec(String rec)
	{
		this.fieldRec = rec;
	}		
}
