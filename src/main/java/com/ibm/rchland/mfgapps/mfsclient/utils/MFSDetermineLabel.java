/* @ Copyright IBM Corporation 2009. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 * 
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- --------------------------------------
 * 2009-07-21      41330JL	Sayde Alcantara	 -Initial version
 * 2015-06-30  ~01 1333476	Luis M. Rodriguez IPR Part Support TYPZ='O'
 * 2015-10-16               D Kloepping      -comment out code in MFSDetermineLabel,
 *                                            that's causing and issue in VAC-Storage-SAP
 * *******************************************************************************/
 
package com.ibm.rchland.mfgapps.mfsclient.utils;

import java.util.Vector;

import mfsxml.MfsXMLDocument;

import com.ibm.rchland.mfgapps.client.utils.messagebox.IGSMessageBox;
import com.ibm.rchland.mfgapps.mfsclient.MFSFrame;
import com.ibm.rchland.mfgapps.mfsclient.MFSPartInstructionJPanel;
import com.ibm.rchland.mfgapps.mfsclient.struct.MFSComponentListModel;
import com.ibm.rchland.mfgapps.mfsclient.struct.MFSComponentRec;
import com.ibm.rchland.mfgapps.mfsclient.struct.MFSHeaderRec;
import com.ibm.rchland.mfgapps.mfscommon.MFSActionable;
import com.ibm.rchland.mfgapps.mfscommon.MFSConfig;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSComm;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSTransaction;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSXmlTransaction;

/**
 * <code>MFSDetermineLabel</code> contains methods to determine the type of continer
 * label to be printed.
 * @author The MFS Client Development Team
 */
public class MFSDetermineLabel{

	/** fieldRowVector is used to look for part datas */
	@SuppressWarnings("rawtypes")
	private Vector fieldRowVector;

	@SuppressWarnings("rawtypes")
	public MFSDetermineLabel() {
		fieldRowVector = new Vector();
	}

	@SuppressWarnings("rawtypes")
	public MFSDetermineLabel(Vector rowVector) {
		this.fieldRowVector = rowVector;
	}

	private MFSPartInstructionJPanel getRowVectorElementAt(int index)
	{
		return (MFSPartInstructionJPanel) this.fieldRowVector.elementAt(index);
	}
	
	/**
	 * According to config entry and type of order,
	 * determine the type of container label should be printed
	 * 
	 * @param headRec the <code>MFSHeaderRec</code> for the work unit
	 * @param mctl of the work unit
	 * @param cntr the container
	 * @param qty the quantity
	 * @param parent the <code>MFSFrame</code> used to display
	 * @param partData indicate if part data is available for printing (True/False)
	 * @param actionable the <code>MFSActionable</code> that indicates the
	 *        transaction is executing
	 */
	public void determineContainerLabel(final MFSHeaderRec headerRec, String mctl, String cntr, 
			                                  int qty, MFSFrame parent, boolean partData, MFSActionable actionable)
	{
		int rc = 0;
		MFSActionable fieldActionable = actionable;
		final MFSConfig config = MFSConfig.getInstance();
		
		 // Change how labels are triggered, check for IPR orders first
		 /*	if (headerRec.getTypz().equals("6") || headerRec.getTypz().equals("O")) //~01
			commented this out from 44.01 caused an unwanted issue in storage sap as they have 
			part orders */
		if (headerRec.getTypz().equals("6"))
		{
			MFSPrintingMethods.iprsheet(headerRec.getMfgn(),headerRec.getIdss(),
										mctl, cntr, qty, parent);
		}
		// if MES Order and container mesf label is configured print it
		else if((headerRec.getTypz().equals("1") 
				 || headerRec.getTypz().equals("2")) 
	 	         && config.containsConfigEntry("CASECONTENTMESF"))
		{
			if(partData)
			{
				rc = updatePartContainers(parent, fieldActionable);
				if (rc == 0)
				{
					MFSPrintingMethods.containerMESF(cntr, mctl, qty, parent);
				}
			}
		}
		// if rochester container label is configured print it
		else if (config.containsConfigEntry("RTVSHPSHT")) //$NON-NLS-1$
		{
			MFSPrintingMethods.rochcontain(cntr, mctl, qty, parent);
		}
		// if poughkeepsie container label is configured print it
		else if (config.containsConfigEntry("SHIPGRP")) //$NON-NLS-1$
		{
			MFSPrintingMethods.shipgrp(cntr, mctl, qty, parent);
		}
		// if the converged container label is configured print it
		// ~6 If MES order, then call the container printing method.
		else if ((headerRec.getTypz().equals("1") //$NON-NLS-1$ 
				|| headerRec.getTypz().equals("2")) //$NON-NLS-1$
				|| (config.containsConfigEntry("PRODPACK") == false //$NON-NLS-1$ 
						&& config.containsConfigEntry("PARTCONTAINER") == false)) //$NON-NLS-1$
		{
			MFSPrintingMethods.container(mctl, cntr.substring(4, 10), qty, parent);
		}
		else
		{
			if(partData)
			{			
				//~6 call new trx to update parts and send the new printing method
				rc = updatePartContainers(parent, fieldActionable);
				if (rc == 0)
				{
				MFSPrintingMethods.pcpplabels(cntr, mctl, "REPRINT", qty,"NONE", parent); //$NON-NLS-1$
				}
			}
		}		
	}
	
	/** 
	 * Update part datas if it is needed for printing container label.
	 * ( This method has been copied from MFSReductionPanel.java )
	 * Calls the UPDT_CRCB transaction. 
	 * 
	 * @param parent the <code>MFSFrame</code> used to display
	 * @param actionable the <code>MFSActionable</code> that indicates the
	 *        transaction is executing
	 * @return 0 on success; nonzero on error
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public int updatePartContainers(MFSFrame parent, MFSActionable actionable)
	{
		int rc = 0;
		try
		{
			boolean changed = false;
			int index = 0;
			MFSComponentRec cmp;
			Vector xmlVector = new Vector();
			int counter = 0, row = 0;
			int max_recs = 0;
			index = 0;

			if (this.fieldRowVector.size() > 0)
			{
				MFSComponentListModel tempLm = null;

				MfsXMLDocument xml_data = new MfsXMLDocument("UPDT_CRCB"); //$NON-NLS-1$
				xml_data.addOpenTag("DATA"); //$NON-NLS-1$
				while (row < this.fieldRowVector.size())
				{
					MFSPartInstructionJPanel pip = getRowVectorElementAt(row);
					if (!pip.getIsNonPartInstruction())
					{
						index = 0;
						tempLm = pip.getPNListModel();
						while (index < tempLm.size())
						{
							cmp = tempLm.getComponentRecAt(index);
							if (cmp.getRec_changed())
							{
								changed = true;
								xml_data.addOpenTag("RCD"); //$NON-NLS-1$
								xml_data.addCompleteField("MCTL", cmp.getMctl()); //$NON-NLS-1$
								xml_data.addCompleteField("CRCT", cmp.getCrct()); //$NON-NLS-1$
								xml_data.addCompleteField("CNTR", cmp.getCntr()); //$NON-NLS-1$
								xml_data.addCompleteField("IDSP", cmp.getIdsp()); //$NON-NLS-1$
								xml_data.addCompleteField("COOC", cmp.getCooc()); // ~2A //$NON-NLS-1$
								xml_data.addCloseTag("RCD"); //$NON-NLS-1$
								counter++;
							}

							/*
							 * If first record, get the length so we can know
							 * the maximum number of records sent by
							 * transaction, limited by the buffer size
							 */
							if (counter == 1)
							{
								String strXMLdata = xml_data.toString();
								int len = strXMLdata.toString().length();
								max_recs = MFSConstants.BUFFER_SIZE / len;
							}

							if (changed && counter >= max_recs)
							{
								xml_data.addCloseTag("DATA"); //$NON-NLS-1$
								xml_data.finalizeXML();
								xmlVector.addElement(xml_data.toString());
								xml_data = new mfsxml.MfsXMLDocument("UPDT_CRCB"); //$NON-NLS-1$
								xml_data.addOpenTag("DATA"); //$NON-NLS-1$
								counter = 0;
							}

							index++;
						}//end of loop thru this part list
					}//part instruction

					row++;

				}//while more rows

				if (counter > 0)
				{
					xml_data.addCloseTag("DATA"); //$NON-NLS-1$
					xml_data.finalizeXML();
					xmlVector.addElement(xml_data.toString());
				}

				if (changed)
				{
					//now loop thru the vector of xml documents and run the transactions
					int vectorIndex = 0;
					int overAllRC = 0;
					String erms = null;
					while (vectorIndex < xmlVector.size())
					{
						String data = (String) (xmlVector.elementAt(vectorIndex));
						MFSTransaction updt_crcb = new MFSXmlTransaction(data);
						updt_crcb.setActionMessage("Updating Component Record	/ Container Content Please Wait...");
						MFSComm.getInstance().execute(updt_crcb, actionable);

						if (updt_crcb.getReturnCode() != 0)
						{
							overAllRC = updt_crcb.getReturnCode();
							erms = updt_crcb.getErms();
						}
						vectorIndex++;
					}//xml vector loop

					if (overAllRC != 0)
					{
						rc = 1;
						IGSMessageBox.showOkMB(parent, null, erms, null);
					}
				}//need To Run the updt_crcb transaction
			}//at least one row in the row vector
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(parent, null, null, e);
		}
		return rc;
	}
	
}
