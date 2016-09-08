/* © Copyright IBM Corporation 2004, 2007. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2007-01-15   ~1 34242JR  R Prechel        -Java 5 version
 *                                           -Removed PropertyChangeSupport
 *                                           -Add isSISytemType method
 * 2009-06-03  ~02 43813MZ  Christopher O    -Add a new loadH5 Function
 * 2010-03-02  ~-3 42558JL  D Mathre	     -Add new mthods for On Demand printing
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.struct;

/**
 * <code>MFSHeaderRec</code> is a data structure for work unit information.
 * @author The MFS Client Development Team
 */
public class MFSHeaderRec
{
	private String fieldMfgn = ""; //$NON-NLS-1$

	private String fieldIdss = ""; //$NON-NLS-1$

	private String fieldCmat = ""; //$NON-NLS-1$

	private String fieldCmdl = ""; //$NON-NLS-1$

	private String fieldSchd = ""; //$NON-NLS-1$

	private String fieldTopi = ""; //$NON-NLS-1$

	private String fieldCeci = ""; //$NON-NLS-1$

	private String fieldPrec = ""; //$NON-NLS-1$

	private String fieldSysl = ""; //$NON-NLS-1$

	private String fieldTypz = ""; //$NON-NLS-1$

	private String fieldRcon = ""; //$NON-NLS-1$

	private String fieldUnpr = ""; //$NON-NLS-1$

	private String fieldEmri = ""; //$NON-NLS-1$

	private String fieldTwrc = ""; //$NON-NLS-1$

	private String fieldOlev = ""; //$NON-NLS-1$

	private String fieldCons = ""; //$NON-NLS-1$

	private String fieldCtry = ""; //$NON-NLS-1$

	private String fieldBrof = ""; //$NON-NLS-1$

	private String fieldMmdl = ""; //$NON-NLS-1$

	private String fieldNmbr = ""; //$NON-NLS-1$

	private String fieldMctl = ""; //$NON-NLS-1$

	private String fieldWtyp = ""; //$NON-NLS-1$

	private String fieldProd = ""; //$NON-NLS-1$

	private String fieldPrln = ""; //$NON-NLS-1$

	private String fieldOrno = ""; //$NON-NLS-1$

	private String fieldMatp = ""; //$NON-NLS-1$

	private String fieldMspi = ""; //$NON-NLS-1$

	private String fieldMcsn = ""; //$NON-NLS-1$

	private String fieldWuft = ""; //$NON-NLS-1$

	private String fieldWunm = ""; //$NON-NLS-1$

	private String fieldCntr = ""; //$NON-NLS-1$

	private boolean fieldLupn_flag = false;

	private String fieldSaps = ""; //$NON-NLS-1$

	private String fieldSapo = ""; //$NON-NLS-1$

	private String fieldAllp = ""; //$NON-NLS-1$

	private String fieldFopr = ""; //$NON-NLS-1$

	private String fieldPprl = ""; //$NON-NLS-1$

	private String fieldSapn = ""; //$NON-NLS-1$

	private String fieldIspq = ""; //$NON-NLS-1$

	private String fieldPfsn = ""; //$NON-NLS-1$

	private String fieldPath = ""; //$NON-NLS-1$

	private String fieldBlvl = ""; //$NON-NLS-1$

	private String fieldWwid = ""; //$NON-NLS-1$

	public String fieldAwsa;

	public String fieldMalc;

	public String fieldMilc;
	
	private String fieldSep = "+";
	
	/** Object to store data collections retrieved from W_PARTCOLL Program */
	private MFSDataCollection dataCollection;         						//~02
	
	/** Flag to set if Collect Data is required for this MCTL */
	private boolean fieldCollectRequired;     								//~02

	/** Constructs a new <code>MFSHeaderRec</code>. */
	public MFSHeaderRec()
	{
		super();
	}
	
	//~1A
	/**
	 * Returns <code>true</code> iff the system type is an SI type
	 * (i.e., typz == A, B, C, or D)
	 * @return <code>true</code> iff the system type is an SI type
	 */
	public boolean isSISytemType()
	{
		return this.fieldTypz.equals("A") //$NON-NLS-1$ 
				|| this.fieldTypz.equals("B") //$NON-NLS-1$
				|| this.fieldTypz.equals("C") //$NON-NLS-1$
				|| this.fieldTypz.equals("D"); //$NON-NLS-1$
	}

	//~3A
	/**
	 * Returns <code>NUMBR+PRLN</code> concatenates the fieldNumbr
	 * and fieldPrln
	 * @return <code>NUMBR+PRLN</code> 
	 */
	public String getNumbrPrln()
	{
		return this.getNmbr()+ this.fieldSep + this.getPrln(); //$NON-NLS-1$ 
				
	}
	
	//~3B
	/**
	 * Returns <code>NUM+*ALL</code> concatenates the fieldNum
	 * and fieldPrln
	 * @return <code>NUM+*ALL</code> 
	 */
	public String getNumbrAllPrln()
	{
		return this.getNmbr()+ this.fieldSep + "*ALL"; //$NON-NLS-1$ 
				
	}
	/**
	 * Gets the allp property (String) value.
	 * @return The allp property value.
	 * @see #setAllp
	 */
	public String getAllp()
	{
		return this.fieldAllp;
	}

	/**
	 * Gets the awsa property (String) value.
	 * @return The awsa property value.
	 * @see #setAwsa
	 */
	public String getAwsa()
	{
		return this.fieldAwsa;
	}

	/**
	 * Gets the blvl property (String) value.
	 * @return The blvl property value.
	 * @see #setBlvl
	 */
	public String getBlvl()
	{
		return this.fieldBlvl;
	}

	/**
	 * Gets the brof property (String) value.
	 * @return The brof property value.
	 * @see #setBrof
	 */
	public String getBrof()
	{
		return this.fieldBrof;
	}

	/**
	 * Gets the ceci property (String) value.
	 * @return The ceci property value.
	 * @see #setCeci
	 */
	public String getCeci()
	{
		return this.fieldCeci;
	}

	/**
	 * Gets the cmat property (String) value.
	 * @return The cmat property value.
	 * @see #setCmat
	 */
	public String getCmat()
	{
		return this.fieldCmat;
	}

	/**
	 * Gets the cmdl property (String) value.
	 * @return The cmdl property value.
	 * @see #setCmdl
	 */
	public String getCmdl()
	{
		return this.fieldCmdl;
	}

	/**
	 * Gets the cntr property (String) value.
	 * @return The cntr property value.
	 * @see #setCntr
	 */
	public String getCntr()
	{
		return this.fieldCntr;
	}

	/**
	 * Gets the cons property (String) value.
	 * @return The cons property value.
	 * @see #setCons
	 */
	public String getCons()
	{
		return this.fieldCons;
	}

	/**
	 * Gets the ctry property (String) value.
	 * @return The ctry property value.
	 * @see #setCtry
	 */
	public String getCtry()
	{
		return this.fieldCtry;
	}

	/** ~02
	 * Gets the data collection.
	 * @return MFSDataCollection object
	 * @see #setDataCollection
	 */
	public MFSDataCollection getDataCollection()
	{
		return this.dataCollection;
	}
	
	/**
	 * Gets the emri property (String) value.
	 * @return The emri property value.
	 * @see #setEmri
	 */
	public String getEmri()
	{
		return this.fieldEmri;
	}

	/**
	 * Gets the fopr property (String) value.
	 * @return The fopr property value.
	 * @see #setFopr
	 */
	public String getFopr()
	{
		return this.fieldFopr;
	}

	/**
	 * Gets the idss property (String) value.
	 * @return The idss property value.
	 * @see #setIdss
	 */
	public String getIdss()
	{
		return this.fieldIdss;
	}

	/**
	 * Gets the ispq property (String) value.
	 * @return The ispq property value.
	 * @see #setIspq
	 */
	public String getIspq()
	{
		return this.fieldIspq;
	}

	/**
	 * Gets the lupn_flag property (boolean) value.
	 * @return The lupn_flag property value.
	 * @see #setLupn_flag
	 */
	public boolean getLupn_flag()
	{
		return this.fieldLupn_flag;
	}

	/**
	 * Gets the malc property (String) value.
	 * @return The malc property value.
	 * @see #setMalc
	 */
	public String getMalc()
	{
		return this.fieldMalc;
	}

	/**
	 * Gets the matp property (String) value.
	 * @return The matp property value.
	 * @see #setMatp
	 */
	public String getMatp()
	{
		return this.fieldMatp;
	}

	/**
	 * Gets the mcsn property (String) value.
	 * @return The mcsn property value.
	 * @see #setMcsn
	 */
	public String getMcsn()
	{
		return this.fieldMcsn;
	}

	/**
	 * Gets the mctl property (String) value.
	 * @return The mctl property value.
	 * @see #setMctl
	 */
	public String getMctl()
	{
		return this.fieldMctl;
	}

	/**
	 * Gets the mfgn property (String) value.
	 * @return The mfgn property value.
	 * @see #setMfgn
	 */
	public String getMfgn()
	{
		return this.fieldMfgn;
	}

	/**
	 * Gets the milc property (String) value.
	 * @return The milc property value.
	 * @see #setMilc
	 */
	public String getMilc()
	{
		return this.fieldMilc;
	}

	/**
	 * Gets the mmdl property (String) value.
	 * @return The mmdl property value.
	 * @see #setMmdl
	 */
	public String getMmdl()
	{
		return this.fieldMmdl;
	}

	/**
	 * Gets the mspi property (String) value.
	 * @return The mspi property value.
	 * @see #setMspi
	 */
	public String getMspi()
	{
		return this.fieldMspi;
	}

	/**
	 * Gets the nmbr property (String) value.
	 * @return The nmbr property value.
	 * @see #setNmbr
	 */
	public String getNmbr()
	{
		return this.fieldNmbr;
	}

	/**
	 * Gets the olev property (String) value.
	 * @return The olev property value.
	 * @see #setOlev
	 */
	public String getOlev()
	{
		return this.fieldOlev;
	}

	/**
	 * Gets the orno property (String) value.
	 * @return The orno property value.
	 * @see #setOrno
	 */
	public String getOrno()
	{
		return this.fieldOrno;
	}

	/**
	 * Gets the path property (String) value.
	 * @return The path property value.
	 * @see #setPath
	 */
	public String getPath()
	{
		return this.fieldPath;
	}

	/**
	 * Gets the pfsn property (String) value.
	 * @return The pfsn property value.
	 * @see #setPfsn
	 */
	public String getPfsn()
	{
		return this.fieldPfsn;
	}

	/**
	 * Gets the pprl property (String) value.
	 * @return The pprl property value.
	 * @see #setPprl
	 */
	public String getPprl()
	{
		return this.fieldPprl;
	}

	/**
	 * Gets the prec property (String) value.
	 * @return The prec property value.
	 * @see #setPrec
	 */
	public String getPrec()
	{
		return this.fieldPrec;
	}

	/**
	 * Gets the prln property (String) value.
	 * @return The prln property value.
	 * @see #setPrln
	 */
	public String getPrln()
	{
		return this.fieldPrln;
	}

	/**
	 * Gets the prod property (String) value.
	 * @return The prod property value.
	 * @see #setProd
	 */
	public String getProd()
	{
		return this.fieldProd;
	}

	/**
	 * Gets the rcon property (String) value.
	 * @return The rcon property value.
	 * @see #setRcon
	 */
	public String getRcon()
	{
		return this.fieldRcon;
	}

	/**
	 * Gets the sapn property (String) value.
	 * @return The sapn property value.
	 * @see #setSapn
	 */
	public String getSapn()
	{
		return this.fieldSapn;
	}

	/**
	 * Gets the sapo property (String) value.
	 * @return The sapo property value.
	 * @see #setSapo
	 */
	public String getSapo()
	{
		return this.fieldSapo;
	}

	/**
	 * Gets the saps property (String) value.
	 * @return The saps property value.
	 * @see #setSaps
	 */
	public String getSaps()
	{
		return this.fieldSaps;
	}

	/**
	 * Gets the schd property (String) value.
	 * @return The schd property value.
	 * @see #setSchd
	 */
	public String getSchd()
	{
		return this.fieldSchd;
	}

	/**
	 * Gets the sysl property (String) value.
	 * @return The sysl property value.
	 * @see #setSysl
	 */
	public String getSysl()
	{
		return this.fieldSysl;
	}

	/**
	 * Gets the topi property (String) value.
	 * @return The topi property value.
	 * @see #setTopi
	 */
	public String getTopi()
	{
		return this.fieldTopi;
	}

	/**
	 * Gets the twrc property (String) value.
	 * @return The twrc property value.
	 * @see #setTwrc
	 */
	public String getTwrc()
	{
		return this.fieldTwrc;
	}

	/**
	 * Gets the typz property (String) value.
	 * @return The typz property value.
	 * @see #setTypz
	 */
	public String getTypz()
	{
		return this.fieldTypz;
	}

	/**
	 * Gets the unpr property (String) value.
	 * @return The unpr property value.
	 * @see #setUnpr
	 */
	public String getUnpr()
	{
		return this.fieldUnpr;
	}

	/**
	 * Gets the wtyp property (String) value.
	 * @return The wtyp property value.
	 * @see #setWtyp
	 */
	public String getWtyp()
	{
		return this.fieldWtyp;
	}

	/**
	 * Gets the wuft property (String) value.
	 * @return The wuft property value.
	 * @see #setWuft
	 */
	public String getWuft()
	{
		return this.fieldWuft;
	}

	/**
	 * Gets the wunm property (String) value.
	 * @return The wunm property value.
	 * @see #setWunm
	 */
	public String getWunm()
	{
		return this.fieldWunm;
	}

	/**
	 * Gets the wwid property (String) value.
	 * @return The wwid property value.
	 * @see #setWwid
	 */
	public String getWwid()
	{
		return this.fieldWwid;
	}
	

	/** ~02
	 * check if collect is required
	 * @return True if required, otherwise False 
	 * @see #setCollectRequired
	 */
	public boolean isCollectRequired()
	{
		return this.fieldCollectRequired;
	}
	
	/**
	 * Loads the header record 1 data.
	 * @param h1 the header record 1 data
	 */
	public void load_H1(String h1)
	{
		setMfgn(h1.substring(2, 9));
		setIdss(h1.substring(9, 13));
		setCmat(h1.substring(13, 17));
		setCmdl(h1.substring(17, 21));
		setSchd(h1.substring(21, 31));
		setTopi(h1.substring(31, 32));
		setCeci(h1.substring(32, 33));
		setPrec(h1.substring(33, 34));
		setSysl(h1.substring(34, 35));
		setTypz(h1.substring(35, 36));
		setRcon(h1.substring(36, 37));
		setUnpr(h1.substring(37, 38));
		setEmri(h1.substring(38, 39));
		//setTwrc(h1.substring(39,41));
		setOlev(h1.substring(41, 42));
		setCons(h1.substring(42, 43));
		setCtry(h1.substring(43, 53));
		setBrof(h1.substring(53, 56));
		setMmdl(h1.substring(56, 60));
	}

	/**
	 * Loads the header record 2 data.
	 * @param h2 the header record 2 data
	 */
	public void load_H2(String h2)
	{
		setNmbr(h2.substring(2, 6));
		setMctl(h2.substring(6, 14));
		setWtyp(h2.substring(14, 15));
		setProd(h2.substring(15, 23));
		setPrln(h2.substring(23, 31));
		setOrno(h2.substring(31, 37));
		setMatp(h2.substring(37, 41));
		setMspi(h2.substring(41, 43));
		setMcsn(h2.substring(43, 50));
		setWuft(h2.substring(50, 51));
		//setWunm(h2.substring(51,53));
		setCntr(h2.substring(53, 63));
	}

	/**
	 * Loads the header record 3 data.
	 * @param h3 the header record 3 data
	 */
	public void load_H3(String h3)
	{
		setSaps(h3.substring(2, 14));
		setSapo(h3.substring(14, 26));
		setAllp(h3.substring(26, 27));
		setFopr(h3.substring(27, 28));
		setPprl(h3.substring(28, 36));
		setSapn(h3.substring(36, 50));
		setIspq(h3.substring(50, 51));
	}

	/**
	 * Loads the header record 4 data.
	 * @param h4 the header record 4 data
	 */
	public void load_H4(String h4)
	{
		setTwrc(h4.substring(2, 6));
		setWunm(h4.substring(6, 10));
		setPfsn(h4.substring(10, 11));
		setAwsa(h4.substring(11, 12));
		setMalc(h4.substring(12, 19));
		setMilc(h4.substring(19, 31));
	}

	/** ~02
	 * Loads the header record 5 data.
	 * @param h5 the header record 5 data
	 */
	public void load_H5(String h5)
	{
		setCollectRequired(h5.substring(2, 3));		
	}
	
	/**
	 * Sets the allp property (String) value.
	 * @param allp The new value for the property.
	 * @see #getAllp
	 */
	public void setAllp(String allp)
	{
		this.fieldAllp = allp;
	}

	/**
	 * Sets the awsa property (String) value.
	 * @param awsa The new value for the property.
	 * @see #getAwsa
	 */
	public void setAwsa(String awsa)
	{
		this.fieldAwsa = awsa;
	}

	/**
	 * Sets the blvl property (String) value.
	 * @param blvl The new value for the property.
	 * @see #getBlvl
	 */
	public void setBlvl(String blvl)
	{
		this.fieldBlvl = blvl;
	}

	/**
	 * Sets the brof property (String) value.
	 * @param brof The new value for the property.
	 * @see #getBrof
	 */
	public void setBrof(String brof)
	{
		this.fieldBrof = brof;
	}

	/**
	 * Sets the ceci property (String) value.
	 * @param ceci The new value for the property.
	 * @see #getCeci
	 */
	public void setCeci(String ceci)
	{
		this.fieldCeci = ceci;
	}

	/**
	 * Sets the cmat property (String) value.
	 * @param cmat The new value for the property.
	 * @see #getCmat
	 */
	public void setCmat(String cmat)
	{
		this.fieldCmat = cmat;
	}

	/**
	 * Sets the cmdl property (String) value.
	 * @param cmdl The new value for the property.
	 * @see #getCmdl
	 */
	public void setCmdl(String cmdl)
	{
		this.fieldCmdl = cmdl;
	}

	/**
	 * Sets the cntr property (String) value.
	 * @param cntr The new value for the property.
	 * @see #getCntr
	 */
	public void setCntr(String cntr)
	{
		this.fieldCntr = cntr;
	}
	
	/** ~02
	 * Sets the collect property 
	 * @param collectRequired The String value Y/N if collect is required
	 * @see #isCollectRequired 
	 */
	public void setCollectRequired(String collectRequired)
	{
		if(collectRequired.equals("Y"))
		{
			this.fieldCollectRequired = true;
		}
		else
		{
			this.fieldCollectRequired = false;
		}
	}
	
	/**
	 * Sets the cons property (String) value.
	 * @param cons The new value for the property.
	 * @see #getCons
	 */
	public void setCons(String cons)
	{
		this.fieldCons = cons;
	}

	/**
	 * Sets the ctry property (String) value.
	 * @param ctry The new value for the property.
	 * @see #getCtry
	 */
	public void setCtry(String ctry)
	{
		this.fieldCtry = ctry;
	}

	/** ~02
	 * Sets the ctry property (String) value.
	 * @param ctry The new value for the property.
	 * @see #getCtry
	 */
	public void setDataCollection(MFSDataCollection dataCollection)
	{
		this.dataCollection = dataCollection;
	}
	
	/**
	 * Sets the emri property (String) value.
	 * @param emri The new value for the property.
	 * @see #getEmri
	 */
	public void setEmri(String emri)
	{
		this.fieldEmri = emri;
	}

	/**
	 * Sets the fopr property (String) value.
	 * @param fopr The new value for the property.
	 * @see #getFopr
	 */
	public void setFopr(String fopr)
	{
		this.fieldFopr = fopr;
	}

	/**
	 * Sets the idss property (String) value.
	 * @param idss The new value for the property.
	 * @see #getIdss
	 */
	public void setIdss(String idss)
	{
		this.fieldIdss = idss;
	}

	/**
	 * Sets the ispq property (String) value.
	 * @param ispq The new value for the property.
	 * @see #getIspq
	 */
	public void setIspq(String ispq)
	{
		this.fieldIspq = ispq;
	}

	/**
	 * Sets the lupn_flag property (boolean) value.
	 * @param lupn_flag The new value for the property.
	 * @see #getLupn_flag
	 */
	public void setLupn_flag(boolean lupn_flag)
	{
		this.fieldLupn_flag = lupn_flag;
	}

	/**
	 * Sets the malc property (String) value.
	 * @param malc The new value for the property.
	 * @see #getMalc
	 */
	public void setMalc(String malc)
	{
		this.fieldMalc = malc;
	}

	/**
	 * Sets the matp property (String) value.
	 * @param matp The new value for the property.
	 * @see #getMatp
	 */
	public void setMatp(String matp)
	{
		this.fieldMatp = matp;
	}

	/**
	 * Sets the mcsn property (String) value.
	 * @param mcsn The new value for the property.
	 * @see #getMcsn
	 */
	public void setMcsn(String mcsn)
	{
		this.fieldMcsn = mcsn;
	}

	/**
	 * Sets the mctl property (String) value.
	 * @param mctl The new value for the property.
	 * @see #getMctl
	 */
	public void setMctl(String mctl)
	{
		this.fieldMctl = mctl;
	}

	/**
	 * Sets the mfgn property (String) value.
	 * @param mfgn The new value for the property.
	 * @see #getMfgn
	 */
	public void setMfgn(String mfgn)
	{
		this.fieldMfgn = mfgn;
	}

	/**
	 * Sets the milc property (String) value.
	 * @param milc The new value for the property.
	 * @see #getMilc
	 */
	public void setMilc(String milc)
	{
		this.fieldMilc = milc;
	}

	/**
	 * Sets the mmdl property (String) value.
	 * @param mmdl The new value for the property.
	 * @see #getMmdl
	 */
	public void setMmdl(String mmdl)
	{
		this.fieldMmdl = mmdl;
	}

	/**
	 * Sets the mspi property (String) value.
	 * @param mspi The new value for the property.
	 * @see #getMspi
	 */
	public void setMspi(String mspi)
	{
		this.fieldMspi = mspi;
	}

	/**
	 * Sets the nmbr property (String) value.
	 * @param nmbr The new value for the property.
	 * @see #getNmbr
	 */
	public void setNmbr(String nmbr)
	{
		this.fieldNmbr = nmbr;
	}

	/**
	 * Sets the olev property (String) value.
	 * @param olev The new value for the property.
	 * @see #getOlev
	 */
	public void setOlev(String olev)
	{
		this.fieldOlev = olev;
	}

	/**
	 * Sets the orno property (String) value.
	 * @param orno The new value for the property.
	 * @see #getOrno
	 */
	public void setOrno(String orno)
	{
		this.fieldOrno = orno;
	}

	/**
	 * Sets the path property (String) value.
	 * @param path The new value for the property.
	 * @see #getPath
	 */
	public void setPath(String path)
	{
		this.fieldPath = path;
	}

	/**
	 * Sets the pfsn property (String) value.
	 * @param pfsn The new value for the property.
	 * @see #getPfsn
	 */
	public void setPfsn(String pfsn)
	{
		this.fieldPfsn = pfsn;
	}

	/**
	 * Sets the pprl property (String) value.
	 * @param pprl The new value for the property.
	 * @see #getPprl
	 */
	public void setPprl(String pprl)
	{
		this.fieldPprl = pprl;
	}

	/**
	 * Sets the prec property (String) value.
	 * @param prec The new value for the property.
	 * @see #getPrec
	 */
	public void setPrec(String prec)
	{
		this.fieldPrec = prec;
	}

	/**
	 * Sets the prln property (String) value.
	 * @param prln The new value for the property.
	 * @see #getPrln
	 */
	public void setPrln(String prln)
	{
		this.fieldPrln = prln;
	}

	/**
	 * Sets the prod property (String) value.
	 * @param prod The new value for the property.
	 * @see #getProd
	 */
	public void setProd(String prod)
	{
		this.fieldProd = prod;
	}

	/**
	 * Sets the rcon property (String) value.
	 * @param rcon The new value for the property.
	 * @see #getRcon
	 */
	public void setRcon(String rcon)
	{
		this.fieldRcon = rcon;
	}

	/**
	 * Sets the sapn property (String) value.
	 * @param sapn The new value for the property.
	 * @see #getSapn
	 */
	public void setSapn(String sapn)
	{
		this.fieldSapn = sapn;
	}

	/**
	 * Sets the sapo property (String) value.
	 * @param sapo The new value for the property.
	 * @see #getSapo
	 */
	public void setSapo(String sapo)
	{
		this.fieldSapo = sapo;
	}

	/**
	 * Sets the saps property (String) value.
	 * @param saps The new value for the property.
	 * @see #getSaps
	 */
	public void setSaps(String saps)
	{
		this.fieldSaps = saps;
	}

	/**
	 * Sets the schd property (String) value.
	 * @param schd The new value for the property.
	 * @see #getSchd
	 */
	public void setSchd(String schd)
	{
		this.fieldSchd = schd;
	}

	/**
	 * Sets the sysl property (String) value.
	 * @param sysl The new value for the property.
	 * @see #getSysl
	 */
	public void setSysl(String sysl)
	{
		this.fieldSysl = sysl;
	}

	/**
	 * Sets the topi property (String) value.
	 * @param topi The new value for the property.
	 * @see #getTopi
	 */
	public void setTopi(String topi)
	{
		this.fieldTopi = topi;
	}

	/**
	 * Sets the twrc property (String) value.
	 * @param twrc The new value for the property.
	 * @see #getTwrc
	 */
	public void setTwrc(String twrc)
	{
		this.fieldTwrc = twrc;
	}

	/**
	 * Sets the typz property (String) value.
	 * @param typz The new value for the property.
	 * @see #getTypz
	 */
	public void setTypz(String typz)
	{
		this.fieldTypz = typz;
	}

	/**
	 * Sets the unpr property (String) value.
	 * @param unpr The new value for the property.
	 * @see #getUnpr
	 */
	public void setUnpr(String unpr)
	{
		this.fieldUnpr = unpr;
	}

	/**
	 * Sets the wtyp property (String) value.
	 * @param wtyp The new value for the property.
	 * @see #getWtyp
	 */
	public void setWtyp(String wtyp)
	{
		this.fieldWtyp = wtyp;
	}

	/**
	 * Sets the wuft property (String) value.
	 * @param wuft The new value for the property.
	 * @see #getWuft
	 */
	public void setWuft(String wuft)
	{
		this.fieldWuft = wuft;
	}

	/**
	 * Sets the wunm property (String) value.
	 * @param wunm The new value for the property.
	 * @see #getWunm
	 */
	public void setWunm(String wunm)
	{
		this.fieldWunm = wunm;
	}

	/**
	 * Sets the wwid property (String) value.
	 * @param wwid The new value for the property.
	 * @see #getWwid
	 */
	public void setWwid(String wwid)
	{
		this.fieldWwid = wwid;
	}
}
