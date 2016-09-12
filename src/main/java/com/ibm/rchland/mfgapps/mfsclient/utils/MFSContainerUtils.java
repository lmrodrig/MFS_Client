/* @ Copyright IBM Corporation 2010. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2010-12-22       48749JL Santiago SC      -Initial version, Java 5.0
 ******************************************************************************/

package com.ibm.rchland.mfgapps.mfsclient.utils;

import java.util.ArrayList;
import java.util.List;

import com.ibm.rchland.mfgapps.client.utils.messagebox.IGSMessageBox;
import com.ibm.rchland.mfgapps.mfsclient.MFSFrame;
import com.ibm.rchland.mfgapps.mfsclient.struct.MFSCntrNode;
import com.ibm.rchland.mfgapps.mfscommon.MFSActionable;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSComm;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSFixedTransaction;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSTransaction;

/**
 * The <code>MFSContainerUtils</code> contains utility methods to retrieve
 * containers from the server.
 * @author MFS Development Team
 */
public final class MFSContainerUtils 
{
	/**
	 * Retrieves an array of containers calling RTV_CINC transaction, this list includes
	 * parent(top level) and child containers.
	 * @param mctl
	 * @param mfgn
	 * @param idss
	 * @param frame
	 * @param actionable
	 * @return an array of <code>MFSCntrNode</code>s.
	 */
	public static MFSCntrNode[] rtvCntrInCntr(String mctl, String mfgn, String idss, 
														MFSFrame frame, MFSActionable actionable)
	{
		MFSCntrNode cntrArray[] = null;
		
		StringBuffer data = new StringBuffer(29);
		data.append("RTV_CINC  "); //$NON-NLS-1$
		data.append(mctl);
		data.append(mfgn);
		data.append(idss);

		MFSTransaction rtv_cinc = new MFSFixedTransaction(data.toString());
		rtv_cinc.setActionMessage("Retrieving List of Containers, Please Wait...");
		MFSComm.getInstance().execute(rtv_cinc, actionable);
		
		if(0 == rtv_cinc.getReturnCode())
		{
			try
			{				
				data = new StringBuffer(rtv_cinc.getOutput());
				/* first add all the cntr's to the root */
				int start = 0;
				int end = 64;
				final int len = data.length();
				final int siz = len / end;
				cntrArray = new MFSCntrNode[siz];

				int i = 0;
				while (end < len)
				{
					MFSCntrNode cntr = new MFSCntrNode(data.substring(start,end));
					cntrArray[i] = cntr;
					start += 64;
					end += 64;
					i++;
				}

				if (len > 0)
				{
					MFSCntrNode cntr = new MFSCntrNode(data.substring(start));
					cntrArray[i] = cntr;
				} 
			}
			catch (Exception e)
			{
				IGSMessageBox.showOkMB(frame, MFSContainerUtils.class.getSimpleName(), null, e);
			}
		}
		else
		{
			IGSMessageBox.showOkMB(frame, MFSContainerUtils.class.getSimpleName(), rtv_cinc.getErms(), null);
		}
		
		return cntrArray;
		
	}
	
	/**
	 * Retrieves the top level containers calling RTV_CINC transaction.
	 * @param mctl
	 * @param mfgn
	 * @param idss
	 * @param frame
	 * @param actionable
	 * @return a list of <code>String</code>s containing the containers.
	 */
	public static List<String> rtvTopLvlCntrs(String mctl, String mfgn, String idss, 
													MFSFrame frame, MFSActionable actionable)
	{
		List<String> topLvlCntrs = null;
		
		// Retrieve all containers: parents and childs
		MFSCntrNode[] cntrArray = rtvCntrInCntr(mctl, mfgn, idss, frame, actionable);
		
		if(null != cntrArray)
		{
			topLvlCntrs = new ArrayList<String>();
			
			// Filter top level containers 
			for (int cntrIndex = 0; cntrIndex < cntrArray.length; cntrIndex++)
			{
				if (cntrArray[cntrIndex].getParentCntr().equals("          ")) //$NON-NLS-1$
				{
					topLvlCntrs.add(cntrArray[cntrIndex].getCntr());
				}
			}
		}

		return topLvlCntrs;
	}
}
