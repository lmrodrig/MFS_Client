/* � Copyright IBM Corporation 2003, 2016. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2003-10-16   ~1 24594DK  E. Engelbert     -Added TcodBrln field, getter, and setter
 * 2004-12-02   ~2 29712RB  Tou Lee Moua     -Added CTLV field
 * 2006-08-24   ~3 34222BP  VH Avila         -Delete ViewInstPartsString field and methods
 * 2007-02-26   ~4 34242JR  R Prechel        -Java 5 version
 *                                           -Does not implement Comparator (replaced by MFSCP500Comparator)
 *                                           -Removed compare, initialize, and handleException methods
 *                                           -Removed getter for PropertyChangeSupport
 *                                           -Add hasBlankSequenceAndSuffix, 
 *                                            hasPlugToOrSwitchSetting, and isShortPart methods
 *                                           -Add methods to check collection flags.
 * 2007-03-28   ~5 19622JM  R Prechel        -Add fieldExceededPlugs and fieldLoanerPart
 *                                           -Don't show the PH message (fieldPhrase) twice
 * 2008-01-10   ~6 39782JM	Martha Luna		 -Support "multi-drop" cables for manufacturing
 *                                           -Don't fire PropertyChangeEvents for properties without listeners
 * 2008-08-07   ~7 31091JM  Dave Fichtinger  -change the way Drawers and Bladers are displayed
 * 2008-10-09					dev ptr for same thing - don't drive off ADRAWER/BDRAWER - use pari == 'D' instead   
 * 2016-07-28  ~08 1566154  Luis M.          -Display always 10 chars for PNs.             
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.struct;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import mfsxml.MISSING_XML_TAG_EXCEPTION;
import mfsxml.MfsXMLParser;

/**
 * <code>MFSComponentRec</code> is a data structure for component information.
 * @author The MFS Client Development Team
 */
public class MFSComponentRec
{
	//~4C Construct PropertyChangeSupport and remove getter method
	/**
	 * The <code>PropertyChangeSupport</code> for this
	 * <code>MFSComponentRec</code>.
	 */
	protected transient PropertyChangeSupport propertyChange = new PropertyChangeSupport(this);
	
	/** The <code>Map</code> of plugs. */
	@SuppressWarnings("rawtypes")
	private Map fieldPlugMap = new LinkedHashMap(); //~6A
	
	private String fieldItem = ""; //$NON-NLS-1$
	private String fieldFamc = ""; //$NON-NLS-1$
	private String fieldCdes = ""; //$NON-NLS-1$
	private String fieldQnty = ""; //$NON-NLS-1$
	private String fieldDfsw = ""; //$NON-NLS-1$
	private String fieldCrct = ""; //$NON-NLS-1$
	private String fieldPari = ""; //$NON-NLS-1$
	private String fieldCwun = ""; //$NON-NLS-1$
	private String fieldCmti = ""; //$NON-NLS-1$
	private String fieldAmsi = ""; //$NON-NLS-1$
	private String fieldMmdl = ""; //$NON-NLS-1$
	private String fieldMatp = ""; //$NON-NLS-1$
	private String fieldMspi = ""; //$NON-NLS-1$
	private String fieldMcsn = ""; //$NON-NLS-1$
	private String fieldMlri = ""; //$NON-NLS-1$
	private String fieldPrtd = ""; //$NON-NLS-1$
	private String fieldPrdc = ""; //$NON-NLS-1$
	private String fieldIdsp = ""; //$NON-NLS-1$
	private String fieldIpit = ""; //$NON-NLS-1$
	private String fieldCell = ""; //$NON-NLS-1$
	private String fieldCtyp = ""; //$NON-NLS-1$
	private String fieldCsds = ""; //$NON-NLS-1$
	private String fieldCsts = ""; //$NON-NLS-1$
	private String fieldInpn = ""; //$NON-NLS-1$
	private String fieldInec = ""; //$NON-NLS-1$
	private String fieldInsq = ""; //$NON-NLS-1$
	private String fieldInca = ""; //$NON-NLS-1$
	private String fieldCsni = ""; //$NON-NLS-1$
	private String fieldPnri = ""; //$NON-NLS-1$
	private String fieldEcri = ""; //$NON-NLS-1$
	private String fieldCcai = ""; //$NON-NLS-1$
	private String fieldUser = ""; //$NON-NLS-1$
	private String fieldEcno = ""; //$NON-NLS-1$
	private String fieldLupn = ""; //$NON-NLS-1$
	private String fieldMctl = ""; //$NON-NLS-1$
	private String fieldProd = ""; //$NON-NLS-1$
	private String fieldPrln = ""; //$NON-NLS-1$
	private String fieldNmbr = ""; //$NON-NLS-1$
	private String fieldPll1 = ""; //$NON-NLS-1$
	private String fieldPll2 = ""; //$NON-NLS-1$
	private String fieldPll3 = ""; //$NON-NLS-1$
	private String fieldPll4 = ""; //$NON-NLS-1$
	private String fieldPll5 = ""; //$NON-NLS-1$
	private String fieldAlis = ""; //$NON-NLS-1$
	private String fieldFqty = ""; //$NON-NLS-1$
	private String fieldCntr = ""; //$NON-NLS-1$
	private String fieldLocn = ""; //$NON-NLS-1$
	private String fieldTpok = ""; //$NON-NLS-1$
	private String fieldDisplayString = ""; //$NON-NLS-1$
	private String fieldInstalledParts = ""; //$NON-NLS-1$
	private boolean fieldRec_changed = false;
	private String fieldShtp = ""; //$NON-NLS-1$
	private String fieldFsub = ""; //$NON-NLS-1$
	private String fieldOrg_idsp = ""; //$NON-NLS-1$
	private String fieldRefp = ""; //$NON-NLS-1$
	private String fieldCooi = ""; //$NON-NLS-1$
	private String fieldCooc = ""; //$NON-NLS-1$
	private String fieldCooList = ""; //$NON-NLS-1$
	private String fieldPrte = ""; //$NON-NLS-1$
	private String fieldCfrf = ""; //$NON-NLS-1$
	private String fieldExpi = ""; //$NON-NLS-1$
	private String fieldCost = ""; //$NON-NLS-1$
	private String fieldItms = ""; //$NON-NLS-1$
	private String fieldScrp = ""; //$NON-NLS-1$
	private String fieldDwnl = ""; //$NON-NLS-1$
	private String fieldDwns = ""; //$NON-NLS-1$
	private boolean fieldStatusChange = false;
	private String fieldRejs = ""; //$NON-NLS-1$
	private String fieldBmnm = ""; //$NON-NLS-1$
	private String fieldAmbi = ""; //$NON-NLS-1$
	private String fieldEcnm = ""; //$NON-NLS-1$
	private String fieldSuff = ""; //$NON-NLS-1$
	private String fieldNmsq = ""; //$NON-NLS-1$
	private String fieldPseq = ""; //$NON-NLS-1$
	private String fieldIRDisplayString = ""; //$NON-NLS-1$
	private String fieldPlphtp = ""; //$NON-NLS-1$
	private boolean fieldIsTcodPart = false;
	private String fieldTcodPart = ""; //$NON-NLS-1$
	private String fieldTcodOdri = ""; //$NON-NLS-1$
	private String fieldTcodAcqy = ""; //$NON-NLS-1$
	private String fieldTcodBrln = ""; //$NON-NLS-1$	//~1A
	private String fieldInterposerResetFlag = ""; //$NON-NLS-1$
	private String fieldInvs = ""; //$NON-NLS-1$
	public String fieldPhrase;
	private String fieldBufferStatus = ""; //$NON-NLS-1$
	public String fieldCtlv;
	private boolean fieldExceededPlugs = false; //~5A
	private boolean fieldLoanerPart = false; //~5A
	public String loadError = new String("");
	
	/** Constructs a new <code>MFSComponentRec</code>. */
	public MFSComponentRec()
	{
		super();
	}

	/**
	 * Constructs a new <code>MFSComponentRec</code>.
	 * @param xml <code>MfsXMLParser</code> to pull out the data
	 */
	public MFSComponentRec(MfsXMLParser parser)
	{
		super();
		
		loadByParse(parser);
			
		
	}
	private void loadByParse(MfsXMLParser parser) 
	{
		try
		{
			if(!parser.contains("C1"))
			{
				this.loadError = "Missing C1 Data!";
			}
			else if(!parser.contains("C2"))
			{
				this.loadError = "Missing C2 Data!";
			}	
			if(!parser.contains("C3"))
			{
				this.loadError = "Missing C3 Data!";
			}
			else if(!parser.contains("C4"))
			{
				this.loadError = "Missing C4 Data!";
			}	
			if(!parser.contains("C5"))
			{
				this.loadError = "Missing C5 Data!";
			}
			else if(!parser.contains("C6"))
			{
				this.loadError = "Missing C6 Data!";
			}	
			
			if(this.loadError.equals(""))
			{	
				this.load_C1(parser.getFieldOnly("C1"));
				this.load_C2(parser.getFieldOnly("C2"));
				this.load_C3(parser.getFieldOnly("C3"));
				this.load_C4(parser.getFieldOnly("C4"));
				this.load_C5(parser.getFieldOnly("C5"));
				this.load_C6(parser.getFieldOnly("C6"));
			}	
			
			if(parser.contains("PL"))
			{
				String tempPL = parser.getField("PL");
				while(tempPL!="")
				{
					this.load_plphtp(tempPL);
					tempPL = parser.getNextField("PL");
				}
			}
			if(parser.contains("PH"))
			{
				String tempPH = parser.getField("PH");
				while(tempPH!="")
				{
					this.load_plphtp(tempPH);
					tempPH = parser.getNextField("PH");
				}
			}
			if(parser.contains("CO"))
			{
				String tempCO = parser.getField("CO");
				while(tempCO!="")
				{
					this.load_CO(tempCO);
					tempCO = parser.getNextField("CO");
				}
			}
			
			setRec_changed(false);
			updtInstalledParts();
			updtDisplayString();
			updtIRDisplayString();
			
		}
		
		catch (MISSING_XML_TAG_EXCEPTION mte)
		{
			this.loadError = "Component Record Parsing Error!";
		}	
	}

	/**
	 * Constructs a new <code>MFSComponentRec</code>.
	 * @param cmp the <code>MFSComponentRec</code> to copy
	 */

	@SuppressWarnings("unchecked")
	public MFSComponentRec(MFSComponentRec cmp)
	{
		super();

		setItem(cmp.getItem());
		setFamc(cmp.getFamc());
		setCdes(cmp.getCdes());
		setQnty(cmp.getQnty());
		setDfsw(cmp.getDfsw());
		setCrct(cmp.getCrct());
		setPari(cmp.getPari());
		setCwun(cmp.getCwun());
		setCmti(cmp.getCmti());
		setAmsi(cmp.getAmsi());
		setMmdl(cmp.getMmdl());
		setMatp(cmp.getMatp());
		setMspi(cmp.getMspi());
		setMcsn(cmp.getMcsn());
		setMlri(cmp.getMlri());
		setPrtd(cmp.getPrtd());
		setPrdc(cmp.getPrdc());
		setIdsp(cmp.getIdsp());
		setIpit(cmp.getIpit());
		setCell(cmp.getCell());
		setCtyp(cmp.getCtyp());
		setCsds(cmp.getCsds());
		setCsts(cmp.getCsts());
		setInpn(cmp.getInpn());
		setInec(cmp.getInec());
		setInsq(cmp.getInsq());
		setInca(cmp.getInca());
		setCsni(cmp.getCsni());
		setPnri(cmp.getPnri());
		setEcri(cmp.getEcri());
		setCcai(cmp.getCcai());
		setUser(cmp.getUser());
		setEcno(cmp.getEcno());
		setLupn(cmp.getLupn());
		setMctl(cmp.getMctl());
		setProd(cmp.getProd());
		setPrln(cmp.getPrln());
		setNmbr(cmp.getNmbr());
		setPll1(cmp.getPll1());
		setPll2(cmp.getPll2());
		setPll3(cmp.getPll3());
		setPll4(cmp.getPll4());
		setPll5(cmp.getPll5());
		setAlis(cmp.getAlis());
		setFqty(cmp.getFqty());
		setCntr(cmp.getCntr());
		setLocn(cmp.getLocn());
		setFsub(cmp.getFsub());
		setShtp(cmp.getShtp());
		setPlphtp(cmp.getPlphtp());
		setTpok(cmp.getTpok());
		this.fieldDisplayString = cmp.getDisplayString();
		this.fieldInstalledParts = cmp.getInstalledParts();
		this.fieldIRDisplayString = cmp.getIRDisplayString();
		setRec_changed(cmp.getRec_changed());
		setCooi(cmp.getCooi());
		setCooc(cmp.getCooc());
		setCooList(cmp.getCooList());
		setCfrf(cmp.getCfrf());
		setScrp(cmp.getScrp());
		setItms(cmp.getItms());
		setDwnl(cmp.getDwnl());
		setDwns(cmp.getDwns());
		setCost(cmp.getCost());
		setBmnm(cmp.getBmnm());
		setAmbi(cmp.getAmbi());
		setEcnm(cmp.getEcnm());
		setSuff(cmp.getSuff());
		setNmsq(cmp.getNmsq());
		setPseq(cmp.getPseq());
		setRejs(cmp.getRejs());
		setExceededPlugs(cmp.getExceededPlugs()); //~5A
		setLoanerPart(cmp.getLoanerPart()); //~5A
		this.fieldPlugMap.putAll(cmp.fieldPlugMap);//~6A
	}

	/**
	 * Adds a <code>PropertyChangeListener</code> to the listener list.
	 * @param listener the <code>PropertyChangeListener</code> to add
	 */
	public synchronized void addPropertyChangeListener(PropertyChangeListener listener)
	{
		//~4C Access propertyChange directly
		this.propertyChange.addPropertyChangeListener(listener);
	}

	/**
	 * Reports a boolean bound property update to any registered listeners.
	 * @param propertyName the programmatic name of the property that was changed
	 * @param oldValue the old value of the property
	 * @param newValue the new value of the property
	 */
	public void firePropertyChange(String propertyName, Object oldValue, Object newValue)
	{
		//~4C Access propertyChange directly
		this.propertyChange.firePropertyChange(propertyName, oldValue, newValue);
	}

	/**
	 * Gets the alis property (String) value.
	 * @return The alis property value.
	 * @see #setAlis
	 */
	public String getAlis()
	{
		return this.fieldAlis;
	}

	/**
	 * Gets the ambi property (String) value.
	 * @return The ambi property value.
	 * @see #setAmbi
	 */
	public String getAmbi()
	{
		return this.fieldAmbi;
	}

	/**
	 * Gets the amsi property (String) value.
	 * @return The amsi property value.
	 * @see #setAmsi
	 */
	public String getAmsi()
	{
		return this.fieldAmsi;
	}

	/**
	 * Gets the bmnm property (String) value.
	 * @return The bmnm property value.
	 * @see #setBmnm
	 */
	public String getBmnm()
	{
		return this.fieldBmnm;
	}

	/**
	 * Gets the bufferStatus property (String) value.
	 * @return The bufferStatus property value.
	 * @see #setBufferStatus
	 */
	public String getBufferStatus()
	{
		return this.fieldBufferStatus;
	}

	/**
	 * Gets the ccai property (String) value.
	 * @return The ccai property value.
	 * @see #setCcai
	 */
	public String getCcai()
	{
		return this.fieldCcai;
	}

	/**
	 * Gets the cdes property (String) value.
	 * @return The cdes property value.
	 * @see #setCdes
	 */
	public String getCdes()
	{
		return this.fieldCdes;
	}

	/**
	 * Gets the cell property (String) value.
	 * @return The cell property value.
	 * @see #setCell
	 */
	public String getCell()
	{
		return this.fieldCell;
	}

	/**
	 * Gets the cfrf property (String) value.
	 * @return The cfrf property value.
	 * @see #setCfrf
	 */
	public String getCfrf()
	{
		return this.fieldCfrf;
	}

	/**
	 * Gets the cmti property (String) value.
	 * @return The cmti property value.
	 * @see #setCmti
	 */
	public String getCmti()
	{
		return this.fieldCmti;
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
	 * Gets the cooc property (String) value.
	 * @return The cooc property value.
	 * @see #setCooc
	 */
	public String getCooc()
	{
		return this.fieldCooc;
	}

	/**
	 * Gets the cooi property (String) value.
	 * @return The cooi property value.
	 * @see #setCooi
	 */
	public String getCooi()
	{
		return this.fieldCooi;
	}

	/**
	 * Gets the cooList property (String) value.
	 * @return The cooList property value.
	 * @see #setCooList
	 */
	public String getCooList()
	{
		return this.fieldCooList;
	}

	/**
	 * Gets the cost property (String) value.
	 * @return The cost property value.
	 * @see #setCost
	 */
	public String getCost()
	{
		return this.fieldCost;
	}

	/**
	 * Gets the crct property (String) value.
	 * @return The crct property value.
	 * @see #setCrct
	 */
	public String getCrct()
	{
		return this.fieldCrct;
	}

	/**
	 * Gets the csds property (String) value.
	 * @return The csds property value.
	 * @see #setCsds
	 */
	public String getCsds()
	{
		return this.fieldCsds;
	}

	/**
	 * Gets the csni property (String) value.
	 * @return The csni property value.
	 * @see #setCsni
	 */
	public String getCsni()
	{
		return this.fieldCsni;
	}

	/**
	 * Gets the csts property (String) value.
	 * @return The csts property value.
	 * @see #setCsts
	 */
	public String getCsts()
	{
		return this.fieldCsts;
	}

	/**
	 * Gets the ctlv property (String) value.
	 * @return The ctlv property value.
	 * @see #setCtlv
	 */
	public String getCtlv()
	{
		return this.fieldCtlv;
	}

	/**
	 * Gets the ctyp property (String) value.
	 * @return The ctyp property value.
	 * @see #setCtyp
	 */
	public String getCtyp()
	{
		return this.fieldCtyp;
	}

	/**
	 * Gets the cwun property (String) value.
	 * @return The cwun property value.
	 * @see #setCwun
	 */
	public String getCwun()
	{
		return this.fieldCwun;
	}

	/**
	 * Gets the dfsw property (String) value.
	 * @return The dfsw property value.
	 * @see #setDfsw
	 */
	public String getDfsw()
	{
		return this.fieldDfsw;
	}

	/**
	 * Gets the displayString property (String) value.
	 * @return The displayString property value.
	 */
	public String getDisplayString()
	{
		return this.fieldDisplayString;
	}

	/**
	 * Gets the dwnl property (String) value.
	 * @return The dwnl property value.
	 * @see #setDwnl
	 */
	public String getDwnl()
	{
		return this.fieldDwnl;
	}

	/**
	 * Gets the dwns property (String) value.
	 * @return The dwns property value.
	 * @see #setDwns
	 */
	public String getDwns()
	{
		return this.fieldDwns;
	}

	/**
	 * Gets the ecnm property (String) value.
	 * @return The ecnm property value.
	 * @see #setEcnm
	 */
	public String getEcnm()
	{
		return this.fieldEcnm;
	}

	/**
	 * Gets the ecno property (String) value.
	 * @return The ecno property value.
	 * @see #setEcno
	 */
	public String getEcno()
	{
		return this.fieldEcno;
	}

	/**
	 * Gets the ecri property (String) value.
	 * @return The ecri property value.
	 * @see #setEcri
	 */
	public String getEcri()
	{
		return this.fieldEcri;
	}
	
	//~5A New method
	/**
	 * Gets the ExceededPlugs property (boolean) value.
	 * @return The ExceededPlugs property value.
	 * @see #setExceededPlugs
	 */
	public boolean getExceededPlugs()
	{
		return this.fieldExceededPlugs;
	}

	/**
	 * Gets the expi property (String) value.
	 * @return The expi property value.
	 * @see #setExpi
	 */
	public String getExpi()
	{
		return this.fieldExpi;
	}

	/**
	 * Gets the famc property (String) value.
	 * @return The famc property value.
	 * @see #setFamc
	 */
	public String getFamc()
	{
		return this.fieldFamc;
	}

	/**
	 * Gets the fqty property (String) value.
	 * @return The fqty property value.
	 * @see #setFqty
	 */
	public String getFqty()
	{
		return this.fieldFqty;
	}

	/**
	 * Gets the fsub property (String) value.
	 * @return The fsub property value.
	 * @see #setFsub
	 */
	public String getFsub()
	{
		return this.fieldFsub;
	}

	/**
	 * Gets the idsp property (String) value.
	 * @return The idsp property value.
	 * @see #setIdsp
	 */
	public String getIdsp()
	{
		return this.fieldIdsp;
	}

	/**
	 * Gets the inca property (String) value.
	 * @return The inca property value.
	 * @see #setInca
	 */
	public String getInca()
	{
		return this.fieldInca;
	}

	/**
	 * Gets the inec property (String) value.
	 * @return The inec property value.
	 * @see #setInec
	 */
	public String getInec()
	{
		return this.fieldInec;
	}

	/**
	 * Gets the inpn property (String) value.
	 * @return The inpn property value.
	 * @see #setInpn
	 */
	public String getInpn()
	{
		return this.fieldInpn;
	}

	/**
	 * Gets the insq property (String) value.
	 * @return The insq property value.
	 * @see #setInsq
	 */
	public String getInsq()
	{
		return this.fieldInsq;
	}

	/**
	 * Gets the installedParts property (String) value.
	 * @return The installedParts property value.
	 */
	public String getInstalledParts()
	{
		return this.fieldInstalledParts;
	}

	/**
	 * Gets the interposerResetFlag property (String) value.
	 * @return The interposerResetFlag property value.
	 * @see #setInterposerResetFlag
	 */
	public String getInterposerResetFlag()
	{
		return this.fieldInterposerResetFlag;
	}

	/**
	 * Gets the invs property (String) value.
	 * @return The invs property value.
	 * @see #setInvs
	 */
	public String getInvs()
	{
		return this.fieldInvs;
	}

	/**
	 * Gets the ipit property (String) value.
	 * @return The ipit property value.
	 * @see #setIpit
	 */
	public String getIpit()
	{
		return this.fieldIpit;
	}

	/**
	 * Gets the fieldIRDisplayString property (String) value.
	 * @return The IRDisplayString property value.
	 */
	public String getIRDisplayString()
	{
		return this.fieldIRDisplayString;
	}

	/**
	 * Gets the isTcodPart property (boolean) value.
	 * @return The isTcodPart property value.
	 * @see #setIsTcodPart
	 */
	public boolean getIsTcodPart()
	{
		return this.fieldIsTcodPart;
	}

	/**
	 * Gets the item property (String) value.
	 * @return The item property value.
	 * @see #setItem
	 */
	public String getItem()
	{
		return this.fieldItem;
	}

	/**
	 * Gets the itms property (String) value.
	 * @return The itms property value.
	 * @see #setItms
	 */
	public String getItms()
	{
		return this.fieldItms;
	}
	
	//~5A New method
	/**
	 * Gets the LoanerPart property (boolean) value.
	 * @return The LoanerPart property value.
	 * @see #setLoanerPart
	 */
	public boolean getLoanerPart()
	{
		return this.fieldLoanerPart;
	}

	/**
	 * Gets the locn property (String) value.
	 * @return The locn property value.
	 * @see #setLocn
	 */
	public String getLocn()
	{
		return this.fieldLocn;
	}

	/**
	 * Gets the lupn property (String) value.
	 * @return The lupn property value.
	 * @see #setLupn
	 */
	public String getLupn()
	{
		return this.fieldLupn;
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
	 * Gets the mlri property (String) value.
	 * @return The mlri property value.
	 * @see #setMlri
	 */
	public String getMlri()
	{
		return this.fieldMlri;
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
	 * Gets the nmsq property (String) value.
	 * @return The nmsq property value.
	 * @see #setNmsq
	 */
	public String getNmsq()
	{
		return this.fieldNmsq;
	}

	//~6A New method
	/**
	 * Returns the number of plug locations.
	 * @return the number of plug locations
	 */
	public int getNumberOfPlugs()
	{
		return this.fieldPlugMap.size();
	}

	/**
	 * Gets the org_idsp property (String) value.
	 * @return The org_idsp property value.
	 * @see #setOrg_idsp
	 */
	public String getOrg_idsp()
	{
		return this.fieldOrg_idsp;
	}

	/**
	 * Gets the pari property (String) value.
	 * @return The pari property value.
	 * @see #setPari
	 */
	public String getPari()
	{
		return this.fieldPari;
	}

	/**
	 * Gets the phrase property (String) value.
	 * @return String The phrase property value.
	 * @see #getPhrase()
	 */
	public String getPhrase()
	{
		return this.fieldPhrase;
	}

	/**
	 * Gets the pll1 property (String) value.
	 * @return The pll1 property value.
	 * @see #setPll1
	 */
	public String getPll1()
	{
		return this.fieldPll1;
	}

	/**
	 * Gets the pll2 property (String) value.
	 * @return The pll2 property value.
	 * @see #setPll2
	 */
	public String getPll2()
	{
		return this.fieldPll2;
	}

	/**
	 * Gets the pll3 property (String) value.
	 * @return The pll3 property value.
	 * @see #setPll3
	 */
	public String getPll3()
	{
		return this.fieldPll3;
	}

	/**
	 * Gets the pll4 property (String) value.
	 * @return The pll4 property value.
	 * @see #setPll4
	 */
	public String getPll4()
	{
		return this.fieldPll4;
	}

	/**
	 * Gets the pll5 property (String) value.
	 * @return The pll5 property value.
	 * @see #setPll5
	 */
	public String getPll5()
	{
		return this.fieldPll5;
	}

	/**
	 * Gets the plphtp property (String) value.
	 * @return The plphtp property value.
	 * @see #setPlphtp
	 */
	public String getPlphtp()
	{
		return this.fieldPlphtp;
	}

	//~6A New method
	/**
	 * Returns the plug information for the specified plug name.
	 * @param plug the plug name
	 * @return the plug information for the specified plug name.
	 */
	public String getPlug(String plug)
	{
		return (String) this.fieldPlugMap.get(plug);
	}

	/**
	 * Gets the pnri property (String) value.
	 * @return The pnri property value.
	 * @see #setPnri
	 */
	public String getPnri()
	{
		return this.fieldPnri;
	}

	/**
	 * Gets the prdc property (String) value.
	 * @return The prdc property value.
	 * @see #setPrdc
	 */
	public String getPrdc()
	{
		return this.fieldPrdc;
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
	 * Gets the prtd property (String) value.
	 * @return The prtd property value.
	 * @see #setPrtd
	 */
	public String getPrtd()
	{
		return this.fieldPrtd;
	}

	/**
	 * Gets the prte property (String) value.
	 * @return The prte property value.
	 * @see #setPrte
	 */
	public String getPrte()
	{
		return this.fieldPrte;
	}

	/**
	 * Gets the pseq property (String) value.
	 * @return The pseq property value.
	 * @see #setPseq
	 */
	public String getPseq()
	{
		return this.fieldPseq;
	}

	/**
	 * Gets the qnty property (String) value.
	 * @return The qnty property value.
	 * @see #setQnty
	 */
	public String getQnty()
	{
		return this.fieldQnty;
	}

	/**
	 * Gets the rec_changed property (boolean) value.
	 * @return The rec_changed property value.
	 * @see #setRec_changed
	 */
	public boolean getRec_changed()
	{
		return this.fieldRec_changed;
	}

	/**
	 * Gets the refp property (String) value.
	 * @return The refp property value.
	 * @see #setRefp
	 */
	public String getRefp()
	{
		return this.fieldRefp;
	}

	/**
	 * Gets the rejs property (String) value.
	 * @return The rejs property value.
	 * @see #setRejs
	 */
	public String getRejs()
	{
		return this.fieldRejs;
	}

	/**
	 * Gets the scrp property (String) value.
	 * @return The scrp property value.
	 * @see #setScrp
	 */
	public String getScrp()
	{
		return this.fieldScrp;
	}

	/**
	 * Gets the shtp property (String) value.
	 * @return The shtp property value.
	 * @see #setShtp
	 */
	public String getShtp()
	{
		return this.fieldShtp;
	}

	/**
	 * Gets the statusChange property (boolean) value.
	 * @return The statusChange property value.
	 * @see #setStatusChange
	 */
	public boolean getStatusChange()
	{
		return this.fieldStatusChange;
	}

	/**
	 * Gets the suff property (String) value.
	 * @return The suff property value.
	 * @see #setSuff
	 */
	public String getSuff()
	{
		return this.fieldSuff;
	}

	/**
	 * Gets the tcodAcqy property (String) value.
	 * @return The tcodAcqy property value.
	 * @see #setTcodAcqy
	 */
	public String getTcodAcqy()
	{
		return this.fieldTcodAcqy;
	}

	/**
	 * Gets the tcodBrln property (String) value.
	 * @return The tcodBrln property value.
	 * @see #setTcodBrln
	 */
	public String getTcodBrln()
	{
		return this.fieldTcodBrln;
	}

	/**
	 * Gets the tcodOdri property (String) value.
	 * @return The tcodOdri property value.
	 * @see #setTcodOdri
	 */
	public String getTcodOdri()
	{
		return this.fieldTcodOdri;
	}

	/**
	 * Gets the tcodPart property (String) value.
	 * @return The tcodPart property value.
	 * @see #setTcodPart
	 */
	public String getTcodPart()
	{
		return this.fieldTcodPart;
	}

	/**
	 * Gets the tpok property (String) value.
	 * @return The tpok property value.
	 * @see #setTpok
	 */
	public String getTpok()
	{
		return this.fieldTpok;
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
	
	//~4A New method
	/**
	 * Returns <code>true</code> iff nmsq is 5 spaces and suff is 10 spaces.
	 * @return <code>true</code> iff nmsq is 5 spaces and suff is 10 spaces
	 */
	public boolean hasBlankSequenceAndSuffix()
	{
		return this.getNmsq().equals("     ") //$NON-NLS-1$ 
				&& this.getSuff().equals("          "); //$NON-NLS-1$
	}

	//~6A New method
	/**
	 * Returns <code>true</code> if dfsw is not two spaces.
	 * @return <code>true</code> if dfsw is not two spaces
	 */
	public boolean hasSwitchSetting()
	{
		return !this.fieldDfsw.equals("  "); //$NON-NLS-1$ //$NON-NLS-2$
	}

	//~4A New method
	/**
	 * @return <code>true</code> iff the ccai field indicates the card
	 *         assembly number should not be collected.
	 */
	public boolean isCcaiDoNotCollect()
	{
		return this.fieldCcai.equals(" ") || this.fieldCcai.equals("0"); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	//~4A New method
	/**
	 * @return <code>true</code> iff the cmti field indicates the machine type
	 *         serial should not be collected.
	 */
	public boolean isCmtiDoNotCollect()
	{
		return this.fieldCmti.equals(" ") || this.fieldCmti.equals("0"); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	//~4A New method
	/**
	 * @return <code>true</code> iff the cooi field indicates the country
	 *         should not be collected.
	 */
	public boolean isCooiDoNotCollect()
	{
		return this.fieldCooi.equals(" ") || this.fieldCooi.equals("0"); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	//~4A New method
	/**
	 * @return <code>true</code> iff the csni field indicates the part
	 *         sequence number should not be collected.
	 */
	public boolean isCsniDoNotCollect()
	{
		return this.fieldCsni.equals(" ") || this.fieldCsni.equals("0"); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	//~4A New method
	/**
	 * @return <code>true</code> iff the ecri field indicates the EC Number
	 *         should not be collected.
	 */
	public boolean isEcriDoNotCollect()
	{
		return this.fieldEcri.equals(" ") || this.fieldEcri.equals("0"); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	//~4A New method
	/**
	 * @return <code>true</code> iff the pari field indicates the control
	 *         number should not be collected.
	 */
	public boolean isPariDoNotCollect()
	{
		return this.fieldPari.equals(" ") || this.fieldPari.equals("0"); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	//~4A New method
	/**
	 * @return <code>true</code> iff the pnri field indicates the part number
	 *         should not be collected.
	 */
	public boolean isPnriDoNotCollect()
	{
		return this.fieldPnri.equals(" ") || this.fieldPnri.equals("0"); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	//~4A New method
	/**
	 * Returns <code>true</code> iff the record is for a short part.
	 * @return <code>true</code> iff the short part indicator is neither 0 nor
	 *         a single space
	 */
	public boolean isShortPart()
	{
		return !this.fieldShtp.equals("0") //$NON-NLS-1$ 
				&& !this.fieldShtp.equals(" "); //$NON-NLS-1$
	}

	/**
	 * Loads the component record 1 data.
	 * @param c1 the component record 1 data
	 */
	public void load_C1(String c1)
	{
		setItem(c1.substring(2, 14));
		setFamc(c1.substring(14, 19));
		setCdes(c1.substring(19, 43));
		setQnty(c1.substring(43, 48));
		setDfsw(c1.substring(48, 50));
		setCrct(c1.substring(50, 54));
		setPari(c1.substring(54, 55));
		setCwun(c1.substring(55, 63));
		setPrte(c1.substring(63, 64));
	}

	/**
	 * Loads the component record 2 data.
	 * @param c2 the component record 2 data
	 */
	public void load_C2(String c2)
	{
		setCmti(c2.substring(2, 3));
		setAmsi(c2.substring(3, 4));
		setMmdl(c2.substring(4, 8));
		setMatp(c2.substring(8, 12));
		setMspi(c2.substring(12, 14));
		setMcsn(c2.substring(14, 21));
		setMlri(c2.substring(21, 22));
		setPrtd(c2.substring(22, 23));
		setPrdc(c2.substring(23, 24));
		setIdsp(c2.substring(24, 25));
		setIpit(c2.substring(25, 26));
		setCell(c2.substring(26, 34));
		setCtyp(c2.substring(34, 42));
		setCsds(c2.substring(42, 52));
		setCsts(c2.substring(52, 60));
		setCtlv(c2.substring(60, 64)); /* ~2A */
		setOrg_idsp(getIdsp());
	}

	/**
	 * Loads the component record 3 data.
	 * @param c3 the component record 3 data
	 */
	public void load_C3(String c3)
	{
		setInpn(c3.substring(2, 14));
		setInec(c3.substring(14, 26));
		setInsq(c3.substring(26, 38));
		setInca(c3.substring(38, 50));
		setCsni(c3.substring(50, 51));
		setPnri(c3.substring(51, 52));
		setEcri(c3.substring(52, 53));
		setCcai(c3.substring(53, 54));
		setUser(c3.substring(54, 62));
	}

	/**
	 * Loads the component record 4 data.
	 * @param c4 the component record 4 data
	 */
	public void load_C4(String c4)
	{
		setEcno(c4.substring(2, 14));
		setLupn(c4.substring(14, 15));
		setMctl(c4.substring(15, 23));
		setProd(c4.substring(23, 31));
		setPrln(c4.substring(31, 39));
		setNmbr(c4.substring(39, 43));
		setPll1(c4.substring(43, 47));
		setPll2(c4.substring(47, 51));
		setPll3(c4.substring(51, 55));
		setPll4(c4.substring(55, 59));
		setPll5(c4.substring(59, 63));
		setTpok(c4.substring(63, 64));
	}

	/**
	 * Loads the component record 5 data.
	 * @param c5 the component record 5 data
	 */
	public void load_C5(String c5)
	{
		setAlis(c5.substring(2, 18));

		if (c5.substring(18, 23).equals("     "))
		{
			if (getIdsp().equals("R"))
			{
				setFqty(getQnty());
			}
			else
			{
				setFqty("00000");
			}
		}
		else
		{
			setFqty(c5.substring(18, 23));
		}

		if ((getMlri().equals(" ") || getMlri().equals("0")) && getIdsp().equals("I"))
		{
			setFqty(getQnty());
		}

		setCntr(c5.substring(23, 33));
		//setLocn(c5.substring(33,40));
		setFsub(c5.substring(40, 41));
		setShtp(c5.substring(41, 42));
		setRefp(c5.substring(42, 54));

		//using refp for fkit loc as well, old locn value isn't long enough
		setLocn(c5.substring(42, 54));

		setCooi(c5.substring(54, 55));
		setCooc(c5.substring(55, 57));
		setCfrf(c5.substring(57, 58));
		setExpi(c5.substring(58, 59));
		setInvs(c5.substring(59, 60));
	}

	/**
	 * Loads the component record 6 data.
	 * @param c6 the component record 6 data
	 */
	public void load_C6(String c6)
	{
		setAmbi(c6.substring(2, 3));
		setBmnm(c6.substring(3, 15));
		setEcnm(c6.substring(15, 27));
		setSuff(c6.substring(27, 37));
		setNmsq(c6.substring(37, 42));
		setPseq(c6.substring(42, 47));
		
		//~5A Start: Set exceeded plugs and loaner part
		String exceededP = c6.substring(47, 48);
		if (exceededP.equalsIgnoreCase("Y")) //$NON-NLS-1$
		{
			setExceededPlugs(true);
		}

		String loanerP = c6.substring(48, 49);
		if (loanerP.equalsIgnoreCase("Y")) //$NON-NLS-1$
		{
			setLoanerPart(true);
		}
		//~5A End: Set exceeded plugs and loaner part
	}

	/**
	 * Perform the load_CO method.
	 * @param co String
	 */
	public void load_CO(String co)
	{
		String temp = getCooList();
		setCooList(temp + co.substring(2, 49));
	}

	/**
	 * Perform the load_plphtp method.
	 * @param data String
	 */
	@SuppressWarnings("unchecked")
	public void load_plphtp(String data)
	{
		if (getPlphtp().length() == 0)
		{
			setPlphtp(data.substring(2));
		}
		else
		{
			setPlphtp(getPlphtp() + "\n" + data.substring(2)); //$NON-NLS-1$
		}
		
		// Parse the Plug information.
		// PLPF 7014-02L01 8204- LM13 8204-   P1 8204-   C4 CABL-   TO     
		// becomes: PF 2L01-LM13-  P1-  C4-  TO
		if (data.startsWith("PL")) //$NON-NLS-1$
		{
			StringBuffer buffer = new StringBuffer(); //~6C
			String plug = data.substring(2, 4); //~6A
			int hyphenIndex = data.indexOf("-"); //$NON-NLS-1$

			while (hyphenIndex != -1)
			{
				buffer.append('-'); //~6C
				buffer.append(data.substring(hyphenIndex + 2, hyphenIndex + 6)); //~6C
				hyphenIndex = data.indexOf("-", hyphenIndex + 1); //$NON-NLS-1$
			}
			// trim off first hyphen
			this.fieldPlugMap.put(plug, plug + " " + buffer.substring(1)); //~6A
		}
		else if (data.startsWith("PH")) //$NON-NLS-1$
		{
			if (getPhrase() == null)
			{
				setPhrase("PH: " + data.substring(2).trim());
			}
			else
			{
				setPhrase(getPhrase() + " " + data.substring(2).trim());
			}
		}
	}

	/**
	 * Perform the load_TD method.
	 * @param td
	 * @param nmbr
	 */
	public void load_TD(String td, String nmbr)
	{
		setItms(td.substring(2, 3));
		//if the part is an obsolete part, we will want to set the idsp to a
		// 'T' and the prtd to an 'O' right away
		if (!getItms().equals(" ") && !getItms().equals("0") && !nmbr.equals("PREP"))
		{
			setFqty("00000");
			setIdsp("T");
			setPrtd("O");
			updtDisplayString();
			updtIRDisplayString();
			updtInstalledParts();
		}

		if (!nmbr.equals("PREP")) /* don't want to do scrap stuff in PREP */
		{
			setScrp(td.substring(3, 4));
		}
		else
		{
			setScrp(" ");
		}

		//if the part is a scrap part, we will want to set the idsp to a 'T'
		// and the prtd to an 'S' right away
		if (!getScrp().equals(" ") && !getScrp().equals("0") && !nmbr.equals("PREP"))
		{
			setFqty("00000");
			setIdsp("T");
			setPrtd("S");
			updtDisplayString();
			updtIRDisplayString();
			updtInstalledParts();
		}

		setDwnl(td.substring(4, 5));
		setDwns(td.substring(5, 6));
		setCost(td.substring(6, 20));
	}

	/**
	 * Removes a <code>PropertyChangeListener</code> from the listener list.
	 * @param listener the <code>PropertyChangeListener</code> to remove
	 */
	public synchronized void removePropertyChangeListener(PropertyChangeListener listener)
	{
		//~4C Access propertyChange directly
		this.propertyChange.removePropertyChangeListener(listener);
	}

	/**
	 * Sets the alis property (String) value.
	 * @param alis The new value for the property.
	 * @see #getAlis
	 */
	public void setAlis(String alis)
	{
		this.fieldAlis = alis;
	}

	/**
	 * Sets the ambi property (String) value.
	 * @param ambi The new value for the property.
	 * @see #getAmbi
	 */
	public void setAmbi(String ambi)
	{
		this.fieldAmbi = ambi;
	}

	/**
	 * Sets the amsi property (String) value.
	 * @param amsi The new value for the property.
	 * @see #getAmsi
	 */
	public void setAmsi(String amsi)
	{
		this.fieldAmsi = amsi;
	}

	/**
	 * Sets the bmnm property (String) value.
	 * @param bmnm The new value for the property.
	 * @see #getBmnm
	 */
	public void setBmnm(String bmnm)
	{
		this.fieldBmnm = bmnm;
	}

	/**
	 * Sets the bufferStatus property (String) value.
	 * @param bufferStatus The new value for the property.
	 * @see #getBufferStatus
	 */
	public void setBufferStatus(String bufferStatus)
	{
		this.fieldBufferStatus = bufferStatus;
	}

	/**
	 * Sets the ccai property (String) value.
	 * @param ccai The new value for the property.
	 * @see #getCcai
	 */
	public void setCcai(String ccai)
	{
		this.fieldCcai = ccai;
	}

	/**
	 * Sets the cdes property (String) value.
	 * @param cdes The new value for the property.
	 * @see #getCdes
	 */
	public void setCdes(String cdes)
	{
		this.fieldCdes = cdes;
	}

	/**
	 * Sets the cell property (String) value.
	 * @param cell The new value for the property.
	 * @see #getCell
	 */
	public void setCell(String cell)
	{
		this.fieldCell = cell;
	}

	/**
	 * Sets the cfrf property (String) value.
	 * @param cfrf The new value for the property.
	 * @see #getCfrf
	 */
	public void setCfrf(String cfrf)
	{
		this.fieldCfrf = cfrf;
	}

	/**
	 * Sets the cmti property (String) value.
	 * @param cmti The new value for the property.
	 * @see #getCmti
	 */
	public void setCmti(String cmti)
	{
		this.fieldCmti = cmti;
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

	/**
	 * Sets the cooc property (String) value.
	 * @param cooc The new value for the property.
	 * @see #getCooc
	 */
	public void setCooc(String cooc)
	{
		this.fieldCooc = cooc;
	}

	/**
	 * Sets the cooi property (String) value.
	 * @param cooi The new value for the property.
	 * @see #getCooi
	 */
	public void setCooi(String cooi)
	{
		this.fieldCooi = cooi;
	}

	/**
	 * Sets the cooList property (String) value.
	 * @param cooList The new value for the property.
	 * @see #getCooList
	 */
	public void setCooList(String cooList)
	{
		this.fieldCooList = cooList;
	}

	/**
	 * Sets the cost property (String) value.
	 * @param cost The new value for the property.
	 * @see #getCost
	 */
	public void setCost(String cost)
	{
		this.fieldCost = cost;
	}

	/**
	 * Sets the crct property (String) value.
	 * @param crct The new value for the property.
	 * @see #getCrct
	 */
	public void setCrct(String crct)
	{
		this.fieldCrct = crct;
	}

	/**
	 * Sets the csds property (String) value.
	 * @param csds The new value for the property.
	 * @see #getCsds
	 */
	public void setCsds(String csds)
	{
		this.fieldCsds = csds;
	}

	/**
	 * Sets the csni property (String) value.
	 * @param csni The new value for the property.
	 * @see #getCsni
	 */
	public void setCsni(String csni)
	{
		this.fieldCsni = csni;
	}

	/**
	 * Sets the csts property (String) value.
	 * @param csts The new value for the property.
	 * @see #getCsts
	 */
	public void setCsts(String csts)
	{
		this.fieldCsts = csts;
	}

	/**
	 * Sets the ctlv property (String) value.
	 * @param ctlv The new value for the property.
	 * @see #getCtlv()
	 */
	public void setCtlv(String ctlv)
	{
		this.fieldCtlv = ctlv;
	}

	/**
	 * Sets the ctyp property (String) value.
	 * @param ctyp The new value for the property.
	 * @see #getCtyp
	 */
	public void setCtyp(String ctyp)
	{
		this.fieldCtyp = ctyp;
	}

	/**
	 * Sets the cwun property (String) value.
	 * @param cwun The new value for the property.
	 * @see #getCwun
	 */
	public void setCwun(String cwun)
	{
		this.fieldCwun = cwun;
	}

	/**
	 * Sets the dfsw property (String) value.
	 * @param dfsw The new value for the property.
	 * @see #getDfsw
	 */
	public void setDfsw(String dfsw)
	{
		this.fieldDfsw = dfsw;
	}

	/**
	 * Sets the dwnl property (String) value.
	 * @param dwnl The new value for the property.
	 * @see #getDwnl
	 */
	public void setDwnl(String dwnl)
	{
		this.fieldDwnl = dwnl;
	}

	/**
	 * Sets the dwns property (String) value.
	 * @param dwns The new value for the property.
	 * @see #getDwns
	 */
	public void setDwns(String dwns)
	{
		this.fieldDwns = dwns;
	}

	/**
	 * Sets the ecnm property (String) value.
	 * @param ecnm The new value for the property.
	 * @see #getEcnm
	 */
	public void setEcnm(String ecnm)
	{
		this.fieldEcnm = ecnm;
	}

	/**
	 * Sets the ecno property (String) value.
	 * @param ecno The new value for the property.
	 * @see #getEcno
	 */
	public void setEcno(String ecno)
	{
		this.fieldEcno = ecno;
	}

	/**
	 * Sets the ecri property (String) value.
	 * @param ecri The new value for the property.
	 * @see #getEcri
	 */
	public void setEcri(String ecri)
	{
		this.fieldEcri = ecri;
	}
	
	//~5A New method
	/**
	 * Sets the ExceededPlugs property (boolean) value.
	 * @param exceededPlugs The new value for the property.
	 * @see #getExceededPlugs
	 */
	public void setExceededPlugs(boolean exceededPlugs)
	{
		this.fieldExceededPlugs = exceededPlugs;
	}

	/**
	 * Sets the expi property (String) value.
	 * @param expi The new value for the property.
	 * @see #getExpi
	 */
	public void setExpi(String expi)
	{
		this.fieldExpi = expi;
	}

	/**
	 * Sets the famc property (String) value.
	 * @param famc The new value for the property.
	 * @see #getFamc
	 */
	public void setFamc(String famc)
	{
		this.fieldFamc = famc;
	}

	/**
	 * Sets the fqty property (String) value.
	 * @param fqty The new value for the property.
	 * @see #getFqty
	 */
	public void setFqty(String fqty)
	{
		this.fieldFqty = fqty;
	}

	/**
	 * Sets the fsub property (String) value.
	 * @param fsub The new value for the property.
	 * @see #getFsub
	 */
	public void setFsub(String fsub)
	{
		this.fieldFsub = fsub;
	}

	/**
	 * Sets the idsp property (String) value.
	 * @param idsp The new value for the property.
	 * @see #getIdsp
	 */
	public void setIdsp(String idsp)
	{
		this.fieldIdsp = idsp;
	}

	/**
	 * Sets the inca property (String) value.
	 * @param inca The new value for the property.
	 * @see #getInca
	 */
	public void setInca(String inca)
	{
		this.fieldInca = inca;
	}

	/**
	 * Sets the inec property (String) value.
	 * @param inec The new value for the property.
	 * @see #getInec
	 */
	public void setInec(String inec)
	{
		this.fieldInec = inec;
	}

	/**
	 * Sets the inpn property (String) value.
	 * @param inpn The new value for the property.
	 * @see #getInpn
	 */
	public void setInpn(String inpn)
	{
		this.fieldInpn = inpn;
	}

	/**
	 * Sets the insq property (String) value.
	 * @param insq The new value for the property.
	 * @see #getInsq
	 */
	public void setInsq(String insq)
	{
		this.fieldInsq = insq;
	}

	/**
	 * Sets the interposerResetFlag property (String) value.
	 * @param interposerResetFlag The new value for the property.
	 * @see #getInterposerResetFlag
	 */
	public void setInterposerResetFlag(String interposerResetFlag)
	{
		this.fieldInterposerResetFlag = interposerResetFlag;
	}

	/**
	 * Sets the invs property (String) value.
	 * @param invs The new value for the property.
	 * @see #getInvs
	 */
	public void setInvs(String invs)
	{
		this.fieldInvs = invs;
	}

	/**
	 * Sets the ipit property (String) value.
	 * @param ipit The new value for the property.
	 * @see #getIpit
	 */
	public void setIpit(String ipit)
	{
		this.fieldIpit = ipit;
	}

	/**
	 * Sets the isTcodPart property (boolean) value.
	 * @param isTcodPart The new value for the property.
	 * @see #getIsTcodPart
	 */
	public void setIsTcodPart(boolean isTcodPart)
	{
		this.fieldIsTcodPart = isTcodPart;
	}

	/**
	 * Sets the item property (String) value.
	 * @param item The new value for the property.
	 * @see #getItem
	 */
	public void setItem(String item)
	{
		this.fieldItem = item;
	}

	/**
	 * Sets the itms property (String) value.
	 * @param itms The new value for the property.
	 * @see #getItms
	 */
	public void setItms(String itms)
	{
		this.fieldItms = itms;
	}
	
	//~5A New method
	/**
	 * Sets the LoanerPart property (boolean) value.
	 * @param loanerPart The new value for the property.
	 * @see #getLoanerPart
	 */
	public void setLoanerPart(boolean loanerPart)
	{
		this.fieldLoanerPart = loanerPart;
	}

	/**
	 * Sets the locn property (String) value.
	 * @param locn The new value for the property.
	 * @see #getLocn
	 */
	public void setLocn(String locn)
	{
		this.fieldLocn = locn;
	}

	/**
	 * Sets the lupn property (String) value.
	 * @param lupn The new value for the property.
	 * @see #getLupn
	 */
	public void setLupn(String lupn)
	{
		this.fieldLupn = lupn;
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
	 * Sets the mlri property (String) value.
	 * @param mlri The new value for the property.
	 * @see #getMlri
	 */
	public void setMlri(String mlri)
	{
		this.fieldMlri = mlri;
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
	 * Sets the nmsq property (String) value.
	 * @param nmsq The new value for the property.
	 * @see #getNmsq
	 */
	public void setNmsq(String nmsq)
	{
		this.fieldNmsq = nmsq;
	}

	/**
	 * Sets the org_idsp property (String) value.
	 * @param org_idsp The new value for the property.
	 * @see #getOrg_idsp
	 */
	public void setOrg_idsp(String org_idsp)
	{
		this.fieldOrg_idsp = org_idsp;
	}

	/**
	 * Sets the pari property (String) value.
	 * @param pari The new value for the property.
	 * @see #getPari
	 */
	public void setPari(String pari)
	{
		this.fieldPari = pari;
	}

	/**
	 * Sets the phrase property value.
	 * @param phrase The new value for the property.
	 */
	public void setPhrase(String phrase)
	{
		this.fieldPhrase = phrase;
	}

	/**
	 * Sets the pll1 property (String) value.
	 * @param pll1 The new value for the property.
	 * @see #getPll1
	 */
	public void setPll1(String pll1)
	{
		this.fieldPll1 = pll1;
	}

	/**
	 * Sets the pll2 property (String) value.
	 * @param pll2 The new value for the property.
	 * @see #getPll2
	 */
	public void setPll2(String pll2)
	{
		this.fieldPll2 = pll2;
	}

	/**
	 * Sets the pll3 property (String) value.
	 * @param pll3 The new value for the property.
	 * @see #getPll3
	 */
	public void setPll3(String pll3)
	{
		this.fieldPll3 = pll3;
	}

	/**
	 * Sets the pll4 property (String) value.
	 * @param pll4 The new value for the property.
	 * @see #getPll4
	 */
	public void setPll4(String pll4)
	{
		this.fieldPll4 = pll4;
	}

	/**
	 * Sets the pll5 property (String) value.
	 * @param pll5 The new value for the property.
	 * @see #getPll5
	 */
	public void setPll5(String pll5)
	{
		this.fieldPll5 = pll5;
	}

	/**
	 * Sets the plphtp property (String) value.
	 * @param plphtp The new value for the property.
	 * @see #getPlphtp
	 */
	public void setPlphtp(String plphtp)
	{
		//MFSRwkSubDialog listeners for plphtp property change events
		String oldValue = this.fieldPlphtp;
		this.fieldPlphtp = plphtp;
		firePropertyChange("plphtp", oldValue, plphtp); //$NON-NLS-1$
	}

	/**
	 * Sets the pnri property (String) value.
	 * @param pnri The new value for the property.
	 * @see #getPnri
	 */
	public void setPnri(String pnri)
	{
		this.fieldPnri = pnri;
	}

	/**
	 * Sets the prdc property (String) value.
	 * @param prdc The new value for the property.
	 * @see #getPrdc
	 */
	public void setPrdc(String prdc)
	{
		this.fieldPrdc = prdc;
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
	 * Sets the prtd property (String) value.
	 * @param prtd The new value for the property.
	 * @see #getPrtd
	 */
	public void setPrtd(String prtd)
	{
		this.fieldPrtd = prtd;
	}

	/**
	 * Sets the prte property (String) value.
	 * @param prte The new value for the property.
	 * @see #getPrte
	 */
	public void setPrte(String prte)
	{
		this.fieldPrte = prte;
	}

	/**
	 * Sets the pseq property (String) value.
	 * @param pseq The new value for the property.
	 * @see #getPseq
	 */
	public void setPseq(String pseq)
	{
		this.fieldPseq = pseq;
	}

	/**
	 * Sets the qnty property (String) value.
	 * @param qnty The new value for the property.
	 * @see #getQnty
	 */
	public void setQnty(String qnty)
	{
		this.fieldQnty = qnty;
	}

	/**
	 * Sets the rec_changed property (boolean) value.
	 * @param rec_changed The new value for the property.
	 * @see #getRec_changed
	 */
	public void setRec_changed(boolean rec_changed)
	{
		this.fieldRec_changed = rec_changed;
	}

	/**
	 * Sets the refp property (String) value.
	 * @param refp The new value for the property.
	 * @see #getRefp
	 */
	public void setRefp(String refp)
	{
		this.fieldRefp = refp;
	}

	/**
	 * Sets the rejs property (String) value.
	 * @param rejs The new value for the property.
	 * @see #getRejs
	 */
	public void setRejs(String rejs)
	{
		this.fieldRejs = rejs;
	}

	/**
	 * Sets the scrp property (String) value.
	 * @param scrp The new value for the property.
	 * @see #getScrp
	 */
	public void setScrp(String scrp)
	{
		this.fieldScrp = scrp;
	}

	/**
	 * Sets the shtp property (String) value.
	 * @param shtp The new value for the property.
	 * @see #getShtp
	 */
	public void setShtp(String shtp)
	{
		this.fieldShtp = shtp;
	}

	/**
	 * Sets the statusChange property (boolean) value.
	 * @param statusChange The new value for the property.
	 * @see #getStatusChange
	 */
	public void setStatusChange(boolean statusChange)
	{
		this.fieldStatusChange = statusChange;
	}

	/**
	 * Sets the suff property (String) value.
	 * @param suff The new value for the property.
	 * @see #getSuff
	 */
	public void setSuff(String suff)
	{
		this.fieldSuff = suff;
	}

	/**
	 * Sets the tcodAcqy property (String) value.
	 * @param tcodAcqy The new value for the property.
	 * @see #getTcodAcqy
	 */
	public void setTcodAcqy(String tcodAcqy)
	{
		this.fieldTcodAcqy = tcodAcqy;
	}

	/**
	 * Sets the tcodBrln property (String) value.
	 * @param tcodBrln The new value for the property.
	 * @see #getTcodBrln
	 */
	public void setTcodBrln(String tcodBrln)
	{
		this.fieldTcodBrln = tcodBrln;
	}

	/**
	 * Sets the tcodOdri property (String) value.
	 * @param tcodOdri The new value for the property.
	 * @see #getTcodOdri
	 */
	public void setTcodOdri(String tcodOdri)
	{
		this.fieldTcodOdri = tcodOdri;
	}

	/**
	 * Sets the tcodPart property (String) value.
	 * @param tcodPart The new value for the property.
	 * @see #getTcodPart
	 */
	public void setTcodPart(String tcodPart)
	{
		this.fieldTcodPart = tcodPart;
	}

	/**
	 * Sets the tpok property (String) value.
	 * @param tpok The new value for the property.
	 * @see #getTpok
	 */
	public void setTpok(String tpok)
	{
		this.fieldTpok = tpok;
	}

	/**
	 * Sets the user property (String) value.
	 * @param user The new value for the property.
	 * @see #getUser
	 */
	public void setUser(String user)
	{
		this.fieldUser = user;
	}

	/**
	 * Returns the ViewInstPartsString
	 * @return the ViewInstPartsString
	 */
	public String toString()
	{ 
		//~3 Create String that had been saved in fieldViewInstPartsString
		//~4 Use a StringBuffer to create String
		StringBuffer buffer = new StringBuffer(210);
		buffer.append(" ");
		buffer.append(getFamc());
		buffer.append("   ");
		buffer.append(getInpn());
		buffer.append("   ");
		buffer.append(getInsq());
		buffer.append("   ");
		buffer.append(getMcsn());
		buffer.append("   ");
		buffer.append(getCdes());
		buffer.append("   ");
		buffer.append(getInec());
		buffer.append("   ");
		buffer.append(getInca());
		buffer.append("   ");
		buffer.append(getCwun());
		buffer.append("   ");
		buffer.append(getCntr());
		buffer.append("   ");
		buffer.append(getNmbr());
		buffer.append("   ");
		buffer.append(getUser());
		buffer.append("   ");
		buffer.append(getPlphtp());
		return buffer.toString();
	}

	//28663DI - add logic to display P idsp - for transaction buffering
	/** Updates the value of the DisplayString property. */
	@SuppressWarnings("rawtypes")
	public void updtDisplayString()
	{
		final String heading = determineDisplayHeading(" ");
		final String idsp = determineDisplayIDSP(heading);
		final String blanks = "                                 ";
		//~6C Used a StringBuffer to create displayString
		//instead of calling setDisplayString multiple times.
		final StringBuffer displayStringBuffer = new StringBuffer(); //~6A

		
		if(getPari().equals("D"))
		{	
			String plugFrom = getPlug("PF");
			if (plugFrom == null)
			{
				plugFrom = "PF                               ";
			}
			else
			{
				plugFrom = (plugFrom + blanks).substring(0, 33);
			}
			
			displayStringBuffer.append(heading);
			displayStringBuffer.append(getItem().substring(2));
			displayStringBuffer.append("  MM:" + getMatp().concat(blanks).substring(0,4));
			displayStringBuffer.append("-" + getMmdl().concat(blanks).substring(0,4));
			displayStringBuffer.append("DRW:" + getCwun().concat(blanks).substring(0,8) + " ");
			displayStringBuffer.append(plugFrom);
			displayStringBuffer.append(" QT:");
			displayStringBuffer.append(getQnty());
			displayStringBuffer.append(" FQT:");
			displayStringBuffer.append(getFqty());
			displayStringBuffer.append(" IDSP:");
			displayStringBuffer.append(idsp);
			
		}		
		
		
		else if (getIdsp().equals("R") || getIdsp().equals("I"))
		{
			final String inpnInsq;
			if (!getInpn().equals("            ") && getInsq().equals("            "))
			{
				inpnInsq = "IPN:" + getInpn().substring(2);		//~08C
			}
			else if (!getInpn().equals("            ")
					&& !getInsq().equals("            "))
			{
				inpnInsq = "IPN:" + getInpn().substring(2) + " ISQ:" + getInsq();
			}
			else
			{
				inpnInsq = "     " + getInpn().substring(2) + "      " + getInsq();
			}

			displayStringBuffer.append(heading);
			displayStringBuffer.append(getItem().substring(2));
			displayStringBuffer.append("  ");
			displayStringBuffer.append(getCdes());
			displayStringBuffer.append(" ");
			displayStringBuffer.append(inpnInsq.concat(blanks).substring(0, 33));
			displayStringBuffer.append(" QT:");
			displayStringBuffer.append(getQnty());
			displayStringBuffer.append(" FQT:");
			displayStringBuffer.append(getFqty());
			displayStringBuffer.append(" IDSP:");
			displayStringBuffer.append(idsp);
		}
		else if (getTpok().equals("8"))
		{
			displayStringBuffer.append(heading);
			displayStringBuffer.append(getItem().substring(2));
			displayStringBuffer.append("  ");
			displayStringBuffer.append(getCdes());
			displayStringBuffer.append(" LOC:");
			displayStringBuffer.append((getLocn().trim() + "        ").substring(0, 8));
			displayStringBuffer.append(" QT:");
			displayStringBuffer.append(getQnty());
			displayStringBuffer.append(" FQT:");
			displayStringBuffer.append(getFqty());
			displayStringBuffer.append(" IDSP:");
			displayStringBuffer.append(idsp);
		}
		else
		{
			//~6C Change how plugFrom String is created
			String plugFrom = getPlug("PF");
			if (plugFrom == null)
			{
				plugFrom = "PF                               ";
			}
			else
			{
				plugFrom = (plugFrom + blanks).substring(0, 33);
			}

			displayStringBuffer.append(heading);
			displayStringBuffer.append(getItem().substring(2));
			displayStringBuffer.append("  ");
			displayStringBuffer.append(getCdes());
			displayStringBuffer.append(" ");
			displayStringBuffer.append(plugFrom);
			displayStringBuffer.append(" QT:");
			displayStringBuffer.append(getQnty());
			displayStringBuffer.append(" FQT:");
			displayStringBuffer.append(getFqty());
			displayStringBuffer.append(" IDSP:");
			displayStringBuffer.append(idsp);

			//~6A Loop through the plug data
			Iterator iterator = this.fieldPlugMap.entrySet().iterator();
			while (iterator.hasNext())
			{
				Map.Entry next = (Map.Entry) iterator.next();
				String plugName = next.getKey().toString();
				String plugValue = next.getValue().toString();

				if (!plugName.equals("PF")) //$NON-NLS-1$
				{
					displayStringBuffer.append("\n");
					displayStringBuffer.append("                                      ");
					displayStringBuffer.append(plugValue);
				}
			}

			if (!getDfsw().equals("  ") && getDfsw().length() > 0) //~6C
			{
				displayStringBuffer.append("\n");
				displayStringBuffer.append(" Set DFCI Switch - ");
				displayStringBuffer.append(getDfsw());
			}
		}

		//~6A Set displayString and fire property change
		String oldValue = this.fieldDisplayString;
		this.fieldDisplayString = displayStringBuffer.toString();
		firePropertyChange("displayString", oldValue, this.fieldDisplayString); //$NON-NLS-1$
	}

	/** Updates the value of the InstalledParts property. */
	@SuppressWarnings("rawtypes")
	public void updtInstalledParts()
	{
		//~6C Used a StringBuffer to create installedParts
		//instead of calling setInstalledParts multiple times.
		final StringBuffer installedPartsBuffer = new StringBuffer(); //~6A

		if (!getMlri().equals(" ") && !getMlri().equals("0"))
		{
			installedPartsBuffer.append("MLRI=");
			installedPartsBuffer.append(getMlri());
			installedPartsBuffer.append("  ");
		}
		if (!getCmti().equals(" ") && !getCmti().equals("0")
				&& (getMcsn().charAt(0) != '$'))
		{
			installedPartsBuffer.append("MS=");
			installedPartsBuffer.append(getMcsn());
			installedPartsBuffer.append("  ");
		}
		if (!(getCwun().equals("        ")))
		{
			installedPartsBuffer.append("CN=");
			installedPartsBuffer.append(getCwun());
			installedPartsBuffer.append("  ");
		}
		if (getIdsp().equals("I") || getIdsp().equals("R")
				|| (getIdsp().equals("A") && !getFqty().equals("00000")))
		{
			if (!(getInpn().equals("            ")))
			{
				installedPartsBuffer.append("PN=");
				installedPartsBuffer.append(getInpn());
				installedPartsBuffer.append("  ");
			}
			if (!(getInec().equals("            ")))
			{
				installedPartsBuffer.append("EC=");
				installedPartsBuffer.append(getInec());
			}

			installedPartsBuffer.append("\n");

			if (!(getInsq().equals("            ")))
			{
				installedPartsBuffer.append("SN=");
				installedPartsBuffer.append(getInsq());
				installedPartsBuffer.append("  ");
			}
			if (!(getInca().equals("            ")))
			{
				installedPartsBuffer.append("CA=");
				installedPartsBuffer.append(getInca());
				installedPartsBuffer.append("  ");
			}
			if (!(getCntr().equals("          ")))
			{
				installedPartsBuffer.append("CNTR=");
				installedPartsBuffer.append(getCntr());
				installedPartsBuffer.append("  ");
			}
			if (!(getCooi().equals(" ")) && !(getCooi().equals("0")))
			{
				installedPartsBuffer.append("CC=");
				installedPartsBuffer.append(getCooc());
			}
			
			if (getCost().length() > 0)
			{
				installedPartsBuffer.append("\n");
				installedPartsBuffer.append(getPlphtp());
			}
			else
			{
				//~6A Loop through the plug data
				Iterator iterator = this.fieldPlugMap.entrySet().iterator();
				boolean previousIsFrom = false;
				while (iterator.hasNext())
				{
					Map.Entry next = (Map.Entry) iterator.next();
					String plugName = next.getKey().toString();
					String plugValue = next.getValue().toString();
					if (plugName.charAt(1) == 'F')
					{
						installedPartsBuffer.append("\n");
						installedPartsBuffer.append(plugValue);
						previousIsFrom = true;
					}
					else
					{
						//If the previous plug is a from plug,
						//display the to plug on the same line.
						if (previousIsFrom)
						{
							installedPartsBuffer.append("    ");
						}
						else
						{
							installedPartsBuffer.append("\n                               ");
						}
						installedPartsBuffer.append(plugValue);
						previousIsFrom = false;
					}
				}
			}
			
			if (!getDfsw().equals("  ") && getDfsw().length() > 0)
			{
				installedPartsBuffer.append("\n");
				installedPartsBuffer.append("Set DFCI Switch - ");
				installedPartsBuffer.append(getDfsw());
			}
		}
		else if (getIdsp().equals("T"))
		{
			installedPartsBuffer.append("\n");
			if (getPrtd().equals("S"))
			{
				if (getNmbr().equals("DCFG"))
				{
					installedPartsBuffer.append("Extra and Scrapped");
					installedPartsBuffer.append("  ");
				}
				else
				{
					installedPartsBuffer.append("Scrapped");
					installedPartsBuffer.append("  ");
				}
			}
			else if (getPrtd().equals("K"))
			{
				installedPartsBuffer.append("Customer Kept");
				installedPartsBuffer.append("  ");
			}
			else if (getPrtd().equals("M"))
			{
				installedPartsBuffer.append("Missing");
				installedPartsBuffer.append("  ");
			}
			else if (getPrtd().equals("P"))
			{
				installedPartsBuffer.append("Processed");
				installedPartsBuffer.append("  ");
			}
			else if (getPrtd().equals("O"))
			{
				installedPartsBuffer.append("Obsolete");
				installedPartsBuffer.append("  ");
			}
			else if (getPrtd().equals("D"))
			{
				installedPartsBuffer.append("Downlevel Part Processed or Part NCM'd");
				installedPartsBuffer.append("  ");
			}
			else if (getPrtd().equals("E"))
			{
				installedPartsBuffer.append("Extra Part");
				installedPartsBuffer.append("  ");
			}
			else
			{
				installedPartsBuffer.append("Unknown S&U Status");
				installedPartsBuffer.append("  ");
			}
		}
		if ((getPhrase() != null) && (getCost().length() == 0)) //~5C
		{
			installedPartsBuffer.append("\n");
			installedPartsBuffer.append(getPhrase());
		}

		//~5A Start: exceeded plugs and loaner part
		if (getExceededPlugs() && !getDwnl().equals("1") && !getDwns().equals("1"))
		{
			installedPartsBuffer.append("\nThe number of plugs by this part was exceeded");
		}
		if (getLoanerPart())
		{
			installedPartsBuffer.append("\nPreviously used on a loaner order. Please apply a loaner sticker");
		}
		//~5A End: exceeded plugs and loaner part

		//~6A Set installedParts and fire property change
		String oldValue = this.fieldInstalledParts;
		this.fieldInstalledParts = installedPartsBuffer.toString(); //6A
		firePropertyChange("installedParts", oldValue, this.fieldInstalledParts); //$NON-NLS-1$
	}

	/** Updates the value of the IRDisplayString property. */
	@SuppressWarnings("rawtypes")
	public void updtIRDisplayString()
	{
		final String heading = determineDisplayHeading("");
		final String idsp = determineDisplayIDSP(heading);
		final String blanks = "                                 ";

		//~6C Used a StringBuffer to create irDisplayString
		//instead of calling setIRDisplayString multiple times.
		final StringBuffer irDisplayBuffer = new StringBuffer(); //~6A

		if(getPari().equals("D"))
		{
			irDisplayBuffer.append(heading);
			irDisplayBuffer.append(getItem().substring(2));
			irDisplayBuffer.append("   MM:" + getMatp().concat(blanks).substring(0,4));
			irDisplayBuffer.append("-" + getMmdl().concat(blanks).substring(0,4));
			irDisplayBuffer.append("\n");
			irDisplayBuffer.append("IDSP:");
			irDisplayBuffer.append(idsp);
			irDisplayBuffer.append("   DRW:" + getCwun().concat(blanks).substring(0,8) + " ");
			//~6A Loop through the plug data
			Iterator iterator = this.fieldPlugMap.entrySet().iterator();
			while (iterator.hasNext())
			{
				Map.Entry next = (Map.Entry) iterator.next();
				irDisplayBuffer.append("\n");
				irDisplayBuffer.append(next.getValue().toString());
			}


		}
		else if (getIdsp().equals("R") || getIdsp().equals("I"))
		{
			irDisplayBuffer.append(heading);
			irDisplayBuffer.append(getItem().substring(2));
			irDisplayBuffer.append(" QT:");
			irDisplayBuffer.append(getQnty());
			irDisplayBuffer.append(" FQT:");
			irDisplayBuffer.append(getFqty());
			irDisplayBuffer.append("\n");
			irDisplayBuffer.append("IDSP:");
			irDisplayBuffer.append(idsp);
			irDisplayBuffer.append("  ");
			irDisplayBuffer.append(getCdes());
			irDisplayBuffer.append("\n");

			if (!getInpn().equals("            "))
			{
				irDisplayBuffer.append("IPN:");
				irDisplayBuffer.append(getInpn().substring(2));
			}
			if (!getInsq().equals("            "))
			{
				irDisplayBuffer.append(" ISQ:");
				irDisplayBuffer.append(getInsq());
			}
		}
		else if (getTpok().equals("8"))
		{
			irDisplayBuffer.append(heading);
			irDisplayBuffer.append(getItem().substring(2));
			irDisplayBuffer.append(" QT:");
			irDisplayBuffer.append(getQnty());
			irDisplayBuffer.append(" FQT:");
			irDisplayBuffer.append(getFqty());
			irDisplayBuffer.append("\n");
			irDisplayBuffer.append("IDSP:");
			irDisplayBuffer.append(idsp);
			irDisplayBuffer.append("  ");
			irDisplayBuffer.append(getCdes());
			irDisplayBuffer.append("\n");
			irDisplayBuffer.append("LOC:");
			irDisplayBuffer.append(getLocn().trim());
		}
		else
		{
			irDisplayBuffer.append(heading);
			irDisplayBuffer.append(getItem().substring(2));
			irDisplayBuffer.append(" QT:");
			irDisplayBuffer.append(getQnty());
			irDisplayBuffer.append(" FQT:");
			irDisplayBuffer.append(getFqty());
			irDisplayBuffer.append("\n");
			irDisplayBuffer.append("IDSP:");
			irDisplayBuffer.append(idsp);
			irDisplayBuffer.append("  ");
			irDisplayBuffer.append(getCdes());

			//~6A Loop through the plug data
			Iterator iterator = this.fieldPlugMap.entrySet().iterator();
			while (iterator.hasNext())
			{
				Map.Entry next = (Map.Entry) iterator.next();
				irDisplayBuffer.append("\n");
				irDisplayBuffer.append(next.getValue().toString());
			}

			if (!getDfsw().equals("  ") && getDfsw().length() > 0) //~6C
			{
				irDisplayBuffer.append("\n");
				irDisplayBuffer.append("Set DFCI Switch - ");
				irDisplayBuffer.append(getDfsw());
			}

		}

		//~6A Set irDisplayString and fire property change
		String oldValue = this.fieldIRDisplayString;
		this.fieldIRDisplayString = irDisplayBuffer.toString();
		firePropertyChange("irDisplayString", oldValue, this.fieldIRDisplayString); //$NON-NLS-1$
	}

	/**
	 * Determines the heading <code>String</code> used by the
	 * {@link #updtDisplayString()}and {@link #updtIRDisplayString()}methods.
	 * @param defaultHeading the default heading <code>String</code>
	 * @return the heading <code>String</code>
	 */
	private String determineDisplayHeading(String defaultHeading)
	{
		final String heading;
		if (!getShtp().equals("0") && !getShtp().equals(" "))
		{
			heading = "*";
			setMlri("1");
		}
		else if (!getInterposerResetFlag().equals(" ")
				&& !getInterposerResetFlag().equals(""))
		{
			heading = getInterposerResetFlag();
		}
		else
		{
			heading = defaultHeading;
		}
		return heading;
	}

	/**
	 * Determines the IDSP value used by the {@link #updtDisplayString()} and
	 * {@link #updtIRDisplayString()}.
	 * @param heading the heading <code>String</code>
	 * @return the IDSP value
	 */
	private String determineDisplayIDSP(String heading)
	{
		final String idsp;
		/* if M-CODED part, change idsp character to 'M' */
		if ((getRefp().equals("M-CODE      ")) && (!getIdsp().equals("R")))
		{
			idsp = "M";
			setRec_changed(true);
		}
		//fkit shorted parts
		else if (getTpok().equals("8") && heading.equals("*") && !getIdsp().equals("P"))
		{
			idsp = "S";
		}
		else if (getBufferStatus().equals("2"))
		{
			idsp = "P";
		}
		else
		{
			idsp = getIdsp();
		}
		return idsp;
	}
}
