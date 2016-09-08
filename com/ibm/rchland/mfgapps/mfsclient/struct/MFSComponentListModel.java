/* © Copyright IBM Corporation 2005, 2012. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2006-06-22   ~1 34144JJ  JL. Woodward     -Turn parts in 0202 merge operation to Red Parts.
 *                                           -Cleanup code, unused variables and stuff.
 * 2006-08-22   ~2 34222BP  VH Avila         -Delete the updtViewInstPartsString() calls because
 *                                            the variable was deleted from Component_Rec class
 * 2007-02-15   ~3 34242JR  R Prechel        -Java 5 version
 *                                           -Remove PropertyChangeSupport
 *                                           -Added getComponentRecAt method
 *                                           -Use MFSCP500Comparator to sort MFSComponentRecs
 * 2008-01-14   ~4 39782JM  Martha Luna      -Changes the name of updtMultilineDisplayString by updtIRDisplayString
 *                                           -Use a delegate ListModel
 * 2008-09-29   ~5 41356MZ  Santiago SC      -New PNRI value to be used : 'N'
 * 2009-06-03  ~06 43813MZ  Christopher O    -Add a new H5 section and call loadH5 in MFSHeaderRec
 * 2012-03-06  ~07 00189533 VH Avila         -Implement new PREFIXES tag handle for multiples ALT_CPFX rules
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.struct;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import com.ibm.rchland.mfgapps.client.utils.messagebox.IGSMessageBox;
import com.ibm.rchland.mfgapps.client.utils.xml.IGSXMLTransaction;
import com.ibm.rchland.mfgapps.mfsclient.MFSFrame;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSCP500Comparator;
import com.ibm.rchland.mfgapps.mfscommon.MFSConfig;

/**
 * <code>MFSComponentListModel</code> is the <code>DefaultListModel</code> for
 * a list of <code>MFSComponentRec</code>s.
 * @author The MFS Client Development Team
 */
public class MFSComponentListModel
	implements ListModel, PropertyChangeListener
{
	/** The suffix number for the <code>MFSComponentListModel</code>. */
	private String fieldSuff = ""; //$NON-NLS-1$
	
	/** The sequence number for the <code>MFSComponentListModel</code>. */
	private String fieldNmsq = ""; //$NON-NLS-1$
	
	/** <code>true</code> if the <code>MFSComponentListModel</code> is for short parts. */
	private boolean fieldIsShort = false;
	
	/** The delegate <code>ListModel</code>. */
	private final DefaultListModel fieldListModel = new DefaultListModel(); //~
	
	/** Constructs a new <code>MFSComponentListModel</code>. */
	public MFSComponentListModel()
	{
		super();
	}
	
	//~3A New method
	/**
	 * Returns the <code>MFSComponentRec</code> at the specified index.
	 * @param index an index into the <code>ListModel</code>
	 * @return the <code>MFSComponentRec</code> at the specified index
	 * @throws ArrayIndexOutOfBoundsException if the <code>index</code> is
	 *         negative or is greater than or equal to the current size of the
	 *         <code>ListModel</code>
	 * @throws ClassCastException if the element at <code>index</code> is not
	 *         an <code>MFSComponentRec</code>
	 */
	public MFSComponentRec getComponentRecAt(int index)
	{
		return (MFSComponentRec) getElementAt(index);
	}

	/**
	 * Returns the value of the isShort property.
	 * @return the value of the isShort property
	 */
	public boolean getIsShort()
	{
		return this.fieldIsShort;
	}
	
	/**
	 * Returns the value of the nmsq property.
	 * @return the value of the nmsq property
	 */
	public String getNmsq()
	{
		return this.fieldNmsq;
	}
	
	/**
	 * Returns the value of the suff property.
	 * @return the value of the suff property
	 */
	public String getSuff()
	{
		return this.fieldSuff;
	}
	
	//~3A New method that uses a comparator to sort MFSComponentRecs
	/**
	 * Inserts the <code>MFSComponentRec</code> into the right spot in the <code>Vector</code>.
	 * <code>MFSComponentRec</code>s with blank sequence/suffix are at the front.
	 * Otherwise, the order is given by the <code>Comparator</code>. 
	 * @param cmp the <code>MFSComponentRec</code>
	 * @param vector the <code>Vector</code>
	 * @param comparator the <code>Comparator</code>
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void insertSorted(MFSComponentRec cmp, Vector vector, Comparator comparator)
	{
		//insert this component into right spot in the vector 
		//if blank insert at end of blanks
		boolean done = false;
		if (cmp.hasBlankSequenceAndSuffix())
		{
			if (vector.size() == 0)
			{
				vector.add(0, cmp);
			}
			else
			{
				for (int i = 0; i < vector.size() && !done; i++)
				{
					MFSComponentRec compAtI = (MFSComponentRec) vector.elementAt(i);
					if (compAtI.hasBlankSequenceAndSuffix() == false)
					{
						done = true;
						vector.add(i, cmp);
					}
				}
				if (!done)
				{
					vector.add(cmp);
				}
			}
		}
		else
		{
			// Add cmp before first MFSComponentRec after blanks that is greater than cmp
			if (vector.size() == 0)
			{
				vector.add(0, cmp);
			}
			else
			{
				for (int i = 0; i < vector.size() && !done; i++)
				{
					MFSComponentRec compAtI = (MFSComponentRec) vector.elementAt(i);
					if (compAtI.hasBlankSequenceAndSuffix() == false)
					{
						if (comparator.compare(cmp, compAtI) < 0)
						{
							done = true;
							vector.add(i, cmp);
						}
					}
				}//end of for loop
				if (!done)
				{
					vector.add(cmp);
				}
			}
		}//non blank suff/seq
	}
	
	/** ~06A 
	 * This function will parse the output from W_PARTCOL pgm 
     *  and will store all retrieved Collection Data in the header rec
     * 
     * @param method This is the Method name to call in the object
     * @return
     * @throws 
     */
	public int loadDataCollection(MFSHeaderRec headerRec,String mctl, MFSFrame parentFrame)
	{
		int rc = 0;
		
		IGSXMLTransaction w_partcoll = new IGSXMLTransaction("W_PARTCOL");
		w_partcoll.startDocument();
		w_partcoll.addElement("COLL_TRX","RTVCOLLECT");
		w_partcoll.addElement("MCTL", mctl); //$NON-NLS-1$
		w_partcoll.endDocument();
		w_partcoll.run();		
		rc = w_partcoll.getReturnCode();

		if (rc == 0)
		{			
			MFSDataCollection dataCollection = new MFSDataCollection();
			
			while (w_partcoll.stepIntoElement("REC") != null)
			{
				String crct = w_partcoll.getNextElement("CRCT"); 
				if (w_partcoll.stepIntoElement("COLLECT_DATA") != null) 
				{
					while (w_partcoll.stepIntoElement("REC_DATA") != null)
					{

						MFSComponentCollection collection= new MFSComponentCollection();
						collection.setCrct(crct);
						collection.setCollectionType(w_partcoll.getNextElement("COLT"));
						collection.setOrder(w_partcoll.getNextElement("CORD"));
						while(w_partcoll.stepIntoElement("VALUE") != null)
						{
							collection.addValuesList(w_partcoll.getNextElement("CVAL"),w_partcoll.getNextElement("CVCT"));
							w_partcoll.stepOutOfElement();
						}
						collection.setQuantity(w_partcoll.getNextElement("CQTY"));				
						dataCollection.addComponentCollection(collection, mctl,MFSDataCollection.COLLECTED_DATA);												
						w_partcoll.stepOutOfElement();
					}					
					w_partcoll.stepOutOfElement();
				}	
				if (w_partcoll.stepIntoElement("COLLECT_BIND") != null) 
				{
					while(w_partcoll.stepIntoElement("REC_BIND") != null)
					{
						MFSComponentCollection collection= new MFSComponentCollection();
						collection.setCrct(crct);
						collection.setCollectionType(w_partcoll.getNextElement("COLT"));
						collection.setOrder(w_partcoll.getNextElement("CORD"));
						collection.setQuantity(w_partcoll.getNextElement("CQTY"));										
						dataCollection.addComponentCollection(collection, mctl,MFSDataCollection.DATA_TO_COLLECT);
						
						w_partcoll.stepOutOfElement();
					}
					w_partcoll.stepOutOfElement();
				}			
				w_partcoll.stepOutOfElement();
			}
						
			while(w_partcoll.stepIntoElement("COLLECTION_TYPE") != null)
			{
				MFSCollectionType collType = new MFSCollectionType();
				
				collType.setCollectionType(w_partcoll.getNextElement("COLT"));
				
				/* ~07 New - Read all PREFIX elements from PREFIXES tag */
				if(w_partcoll.stepIntoElement("PREFIXES") != null)
				{
					while(w_partcoll.stepIntoElement("PREFIX") != null)
					{
						collType.addPrefix(w_partcoll.getNextElement("CPFX"), w_partcoll.getNextElement("CSZS"));
						w_partcoll.stepOutOfElement();
					}
					w_partcoll.stepOutOfElement();
				}
				
				collType.setQuantity(w_partcoll.getNextElement("CQTY"));
				collType.setType(w_partcoll.getNextElement("CVTY"));
				collType.setUnique(w_partcoll.getNextElement("CVUK"));
				collType.setMessage(w_partcoll.getNextElement("CMSG"));
				collType.setCollectOnRemove(w_partcoll.getNextElement("CREM"));								
				dataCollection.addCollectionType(collType);
				w_partcoll.stepOutOfElement();				
			}
			headerRec.setDataCollection(dataCollection);			
		}
		else
		{
			String errorString = w_partcoll.getErms();	
			String title = "Error retrieving collected data";
			IGSMessageBox.showOkMB(parentFrame.getContentPane(), title, errorString, null);
			//Prevent access to dataCollection object if wasn't correctly filled
			headerRec.setCollectRequired("N");
		}		
		return rc;
	}
	
	
	/**
	 * Sets the fields of the <code>MFSHeaderRec</code> and loads the list
	 * model with <code>MFSComponentRec</code>s based on the specified header
	 * and component <code>data</code>.
	 * @param data the data used to populate the <code>MFSHeaderRec</code> and
	 *        create the <code>MFSComponentRec</code>s
	 * @param headerRec the <code>MFSHeaderRec</code> for the components
	 */
	public void loadListModel(String data, MFSHeaderRec headerRec) 
	{
		//~3C Removed config from parameter list
		MFSConfig config = MFSConfig.getInstance();
		
		try
		{
			data += " "; //$NON-NLS-1$
			
			MFSComponentRec compRec = new MFSComponentRec();
			int start = 0;
			int end = 64;
			int inc = 64;
			int len = data.length();
			int compCount = 0;
			

			if (len > end)
			{
				headerRec.setLupn_flag(false);
				/* load header */
				if (data.substring(start, start + 2).equals("H1")) //$NON-NLS-1$
				{
					headerRec.load_H1(data.substring(start, end));
					start += inc;
					end += inc;
				}
				if (data.substring(start, start + 2).equals("H2")) //$NON-NLS-1$
				{
					headerRec.load_H2(data.substring(start, end));
					start += inc;
					end += inc;
				}
				if (data.substring(start, start + 2).equals("H3")) //$NON-NLS-1$
				{
					headerRec.load_H3(data.substring(start, end));
					start += inc;
					end += inc;
				}
				if (data.substring(start, start + 2).equals("H4")) //$NON-NLS-1$
				{
					headerRec.load_H4(data.substring(start, end));
					start += inc;
					end += inc;
				}
				if (data.substring(start, start + 2).equals("H5")) //$NON-NLS-1$    //~06
				{																	//~06
					headerRec.load_H5(data.substring(start, end));					//~06
					start += inc;													//~06
					end += inc;														//~06
				}																	//~06	
			}
			
			/* load components */
			while (end < len)
			{
				if (data.substring(start, start + 2).equals("C1")) //$NON-NLS-1$
				{
					if (compCount > 0)
					{
						compRec.setRec_changed(false);
						compRec.updtInstalledParts();
						compRec.updtDisplayString();
						compRec.updtIRDisplayString();
						this.addElement(compRec);
						compRec = new MFSComponentRec();
					}
					compCount++;
					compRec.load_C1(data.substring(start, end));
				}
				else if (data.substring(start, start + 2).equals("C2")) //$NON-NLS-1$
				{
					compRec.load_C2(data.substring(start, end));
				}
				else if (data.substring(start, start + 2).equals("C3")) //$NON-NLS-1$
				{
					compRec.load_C3(data.substring(start, end));
				}
				else if (data.substring(start, start + 2).equals("C4")) //$NON-NLS-1$
				{
					compRec.load_C4(data.substring(start, end));
					if ((headerRec.getLupn_flag() == false)
							&& !(compRec.getLupn().equals(" ")) //$NON-NLS-1$
							&& !(compRec.getLupn().equals("0"))) //$NON-NLS-1$
					{
						headerRec.setLupn_flag(true);
					}
				}
				else if (data.substring(start, start + 2).equals("C5")) //$NON-NLS-1$
				{
					compRec.load_C5(data.substring(start, end));
				}
				else if (data.substring(start, start + 2).equals("C6")) //$NON-NLS-1$
				{
					compRec.load_C6(data.substring(start, end));
				}
				else if ((data.substring(start, start + 2).equals("PL")) //$NON-NLS-1$
						|| (data.substring(start, start + 2).equals("PH")) //$NON-NLS-1$
						|| (data.substring(start, start + 2).equals("T1")) //$NON-NLS-1$
						|| (data.substring(start, start + 2).equals("T2"))) //$NON-NLS-1$
				{
					compRec.load_plphtp(data.substring(start, end));
				}
				else if (data.substring(start, start + 2).equals("CO")) //$NON-NLS-1$
				{
					compRec.load_CO(data.substring(start, end));
				}
				else if (data.substring(start, start + 2).equals("TD")) //$NON-NLS-1$
				{
					compRec.load_TD(data.substring(start, end), headerRec.getNmbr());
				}	

				// going to override the data collection flags here - so the parts display
				// properly on the direct work screen.
				/* for SI orders (TYPZ = 'A', 'B', 'C' or 'D' - turn data collection off except COO */
				if (headerRec.getTypz().equals("A") //$NON-NLS-1$ 
						|| headerRec.getTypz().equals("B") //$NON-NLS-1$
						|| headerRec.getTypz().equals("C") //$NON-NLS-1$
						|| headerRec.getTypz().equals("D")) //$NON-NLS-1$
				{	
					compRec.setPnri("1"); //$NON-NLS-1$
					compRec.setEcri(" "); //$NON-NLS-1$
					compRec.setCsni(" "); //$NON-NLS-1$
					compRec.setCcai(" "); //$NON-NLS-1$
					compRec.setCmti(" "); //$NON-NLS-1$
					compRec.setPari(" "); //$NON-NLS-1$
					compRec.setMlri("1"); //$NON-NLS-1$
					compRec.setFsub(" "); //$NON-NLS-1$
				}

				if (config.containsConfigEntry("TRXBUFFER")) //$NON-NLS-1$
				{
					compRec.setBufferStatus("1"); //$NON-NLS-1$

					//set up exceptions to the transaction buffer config entry
					if (compRec.getFsub().equals("B") //$NON-NLS-1$ 
							|| compRec.getFsub().equals("C") //$NON-NLS-1$
							|| compRec.getFsub().equals("M") //$NON-NLS-1$
							|| compRec.getFsub().equals("R")) //$NON-NLS-1$
					{
						compRec.setBufferStatus("0"); //$NON-NLS-1$
					}

					if (compRec.getPnri().equals("M") //$NON-NLS-1$ 
							|| compRec.getPnri().equals("T") //$NON-NLS-1$
							|| compRec.getPnri().equals("W") //$NON-NLS-1$
							|| compRec.getPnri().equals("N")) //$NON-NLS-1$  //~5C 
					{
						compRec.setBufferStatus("0"); //$NON-NLS-1$
					}

					if (compRec.getIdsp().equals("R")) //$NON-NLS-1$
					{
						compRec.setBufferStatus("0"); //$NON-NLS-1$
					}

					if (compRec.getCsni().equals("C") || compRec.getCsni().equals("F"))  //$NON-NLS-1$ //$NON-NLS-2$
					{
						compRec.setBufferStatus("0"); //$NON-NLS-1$
					}
				}
				else
				{
					compRec.setBufferStatus(" "); //$NON-NLS-1$
				}

				start += inc;
				end += inc;
			}

			if (compCount > 0)
			{
				compRec.setRec_changed(false);
				compRec.updtInstalledParts();
				compRec.updtDisplayString();
				compRec.updtIRDisplayString();

				if (config.containsConfigEntry("TRXBUFFER")) //$NON-NLS-1$
				{
					compRec.setBufferStatus("1"); //$NON-NLS-1$

					//set up exceptions to the transaction buffer config entry
					if (compRec.getFsub().equals("B") //$NON-NLS-1$ 
							|| compRec.getFsub().equals("C") //$NON-NLS-1$
							|| compRec.getFsub().equals("M") //$NON-NLS-1$
							|| compRec.getFsub().equals("R")) //$NON-NLS-1$
					{
						compRec.setBufferStatus("0"); //$NON-NLS-1$
					}

					if (compRec.getPnri().equals("M") //$NON-NLS-1$ 
							|| compRec.getPnri().equals("T") //$NON-NLS-1$
							|| compRec.getPnri().equals("W") //$NON-NLS-1$
							|| compRec.getPnri().equals("N")) //$NON-NLS-1$  //~5C 
					{
						compRec.setBufferStatus("0"); //$NON-NLS-1$
					}

					if (compRec.getIdsp().equals("R")) //$NON-NLS-1$
					{
						compRec.setBufferStatus("0"); //$NON-NLS-1$
					}

					if (compRec.getCfrf().equals("A")) //$NON-NLS-1$
					{
						compRec.setBufferStatus("0"); //$NON-NLS-1$
					}
					if (compRec.getCsni().equals("C") || compRec.getCsni().equals("F")) //$NON-NLS-1$ //$NON-NLS-2$
					{
						compRec.setBufferStatus("0"); //$NON-NLS-1$
					}
				}
				else
				{
					compRec.setBufferStatus(" "); //$NON-NLS-1$
				}

				this.addElement(compRec);
			}

			// loop through the components to add the Dfsw field to the plphtp field
			// ~1 If REDMERGE config entry found, put blanks in PNRI and MLRI for components 
			// with CRCT = 'MRGE'
			boolean redMerge = config.containsConfigEntry("REDMERGE"); //$NON-NLS-1$
			
			for (int i = 0; i < compCount; i++)
			{
				compRec = (MFSComponentRec) this.getElementAt(i);
				if (!compRec.getDfsw().equals("  ")) //$NON-NLS-1$
				{
					compRec.setPlphtp(compRec.getPlphtp() + "\nSet DFCI Switch - "
							+ compRec.getDfsw());
				}

				// ~1A
				if (redMerge && compRec.getCrct().equals("MRGE")) //$NON-NLS-1$
				{
					compRec.setPnri(" "); //$NON-NLS-1$
					compRec.setMlri(" "); //$NON-NLS-1$
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Sets the value of the isShort property.
	 * @param isShort the new value of the isShort property
	 */
	public void setIsShort(boolean isShort)
	{
		this.fieldIsShort = isShort;
	}
	
	/**
	 * Sets the value of the nmsq property.
	 * @param nmsq the new value of the nmsq property
	 */
	public void setNmsq(String nmsq)
	{
		this.fieldNmsq = nmsq;
	}
	
	/**
	 * Sets the value of the suff property.
	 * @param suff the new value of the suff property
	 */
	public void setSuff(String suff)
	{
		this.fieldSuff = suff;
	}
	
	/** Sorts the <code>MFSComponentRec</code>s in the list model. */
	@SuppressWarnings("rawtypes")
	public void sort() 
	{
		try
		{
			int index = 0;
			Vector shortVector = new Vector();
			Vector regVector = new Vector();
			MFSComponentRec cmp = null;
			final MFSCP500Comparator comparator = new MFSCP500Comparator(); //~3A
			
			while(index < this.fieldListModel.size()) //~4C
			{
				cmp = getComponentRecAt(index);
				if(cmp.getShtp().equals("1")) //$NON-NLS-1$
				{
					//~3C Use insertSorted
					insertSorted(cmp, shortVector, comparator);
				}
				else
				{
					//~3C Use insertSorted
					insertSorted(cmp, regVector, comparator);
				}
				index++;	
			}

			//now we have two sorted vectors (with blanks on top).
			//remove all the elements from this listModel and them 
			this.fieldListModel.removeAllElements(); //~4C
			for(int i = 0;i<shortVector.size();i++)
			{
				this.fieldListModel.addElement(shortVector.elementAt(i)); //~4C
			}
			for(int i = 0;i<regVector.size();i++)
			{
				this.fieldListModel.addElement(regVector.elementAt(i)); //~4C
			}
		}
		catch(Exception e)
		{
			String erms = "list sort exception: ";
			IGSMessageBox.showOkMB(null, null, erms, e);
		}		
	}

	//~4A Add methods to implement ListModel
	/** {@inheritDoc} */
	public int getSize()
	{
		return this.fieldListModel.getSize();
	}

	/** {@inheritDoc} */
	public Object getElementAt(int index)
	{
		return this.fieldListModel.getElementAt(index);
	}

	/** {@inheritDoc} */
	public void addListDataListener(ListDataListener l)
	{
		this.fieldListModel.addListDataListener(l);
	}

	/** {@inheritDoc} */
	public void removeListDataListener(ListDataListener l)
	{
		this.fieldListModel.removeListDataListener(l);
	}

	//~4A Add methods used from DefaultListModel
	/**
	 * Returns the number of <code>MFSComponentRec</code>s in the list model.
	 * @return the number of <code>MFSComponentRec</code>s in the list model
	 */
	public int size()
	{
		return this.fieldListModel.size();
	}

	/**
	 * Inserts the specified <code>MFSComponentRec</code> at the specified
	 * position in the list model.
	 * @param index the index at which the specified
	 *        <code>MFSComponentRec</code> is to be inserted
	 * @param compRec the <code>MFSComponentRec</code> to insert
	 * @throws ArrayIndexOutOfBoundsException if the index is out of range
	 */
	public void add(int index, MFSComponentRec compRec)
	{
		compRec.removePropertyChangeListener(this);
		compRec.addPropertyChangeListener(this);
		this.fieldListModel.add(index, compRec);
	}

	/**
	 * Adds the specified <code>MFSComponentRec</code> to the end of the list
	 * model.
	 * @param compRec the <code>MFSComponentRec</code> to add
	 */
	public void addElement(MFSComponentRec compRec)
	{
		compRec.removePropertyChangeListener(this);
		compRec.addPropertyChangeListener(this);
		this.fieldListModel.addElement(compRec);
	}

	/**
	 * Removes the <code>MFSComponentRec</code> at the specified position in
	 * the list model.
	 * @param index the index of the <code>MFSComponentRec</code> to remove
	 * @return the <code>MFSComponentRec</code> that was removed
	 * @throws ArrayIndexOutOfBoundsException if the index is out of range
	 */
	public Object remove(int index)
	{
		MFSComponentRec compRec = (MFSComponentRec) this.fieldListModel.remove(index);
		compRec.removePropertyChangeListener(this);
		return compRec;
	}

	/** Removes all of the <code>MFSComponentRec</code>s from the list model. */
	@SuppressWarnings("rawtypes")
	public void removeAllElements()
	{
		Enumeration elements = this.fieldListModel.elements();
		while (elements.hasMoreElements())
		{
			((MFSComponentRec) elements.nextElement()).removePropertyChangeListener(this);
		}
		this.fieldListModel.removeAllElements();
	}

	//~4A Listen for PropertyChangeEvents that require a ListDataEvent to be
	// fired so that the appearance of the JList is updated
	/**
	 * Invoked when a property is changed.
	 * @param evt the <code>PropertyChangeEvent</code>
	 */
	public void propertyChange(PropertyChangeEvent evt)
	{
		final String propertyName = evt.getPropertyName();
		if ("displayString".equals(propertyName) //$NON-NLS-1$
				|| "irDisplayString".equals(propertyName)) //$NON-NLS-1$
		{
			ListDataListener[] listeners = (ListDataListener[]) this.fieldListModel
					.getListeners(ListDataListener.class);
			ListDataEvent lde = new ListDataEvent(this.fieldListModel,
					ListDataEvent.CONTENTS_CHANGED, 0, this.fieldListModel.size());
			for (int i = listeners.length - 1; i >= 0; i--)
			{
				listeners[i].contentsChanged(lde);
			}
		}
	}
}
