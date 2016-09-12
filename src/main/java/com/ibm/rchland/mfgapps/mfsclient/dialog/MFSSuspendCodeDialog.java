/* @ Copyright IBM Corporation 2005, 2007. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2007-02-22      34242JR  R Prechel        -Java 5 version
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.dialog;

import java.awt.Component;

import javax.swing.DefaultListModel;

import com.ibm.rchland.mfgapps.client.utils.messagebox.IGSMessageBox;
import com.ibm.rchland.mfgapps.mfsclient.MFSFrame;
import com.ibm.rchland.mfgapps.mfsclient.MFSListDialog;
import com.ibm.rchland.mfgapps.mfscommon.MFSActionable;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSComm;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSFixedTransaction;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSTransaction;

/**
 * <code>MFSSuspendCodeDialog</code> is the <code>MFSListDialog</code> used
 * to display the list of suspend codes.
 * @author The MFS Client Development Team
 */
public class MFSSuspendCodeDialog
	extends MFSListDialog
{
	private static final long serialVersionUID = 1L;
	/**
	 * Constructs a new <code>MFSSuspendCodeDialog</code>.
	 * @param parent the <code>MFSFrame</code> that caused this
	 *        <code>MFSSuspendCodeDialog</code> to be displayed
	 * @param model the populated list model for the suspend code list
	 */
	public MFSSuspendCodeDialog(MFSFrame parent, DefaultListModel model)
	{
		super(parent, "Suspend Codes");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.lstOptions.setModel(model);
		createLayout();
		addMyListeners();
		pack();
	}

	/**
	 * Loads the suspend codes into the <code>DefaultListModel</code>.
	 * @param listModel the <code>DefaultListModel</code> to load
	 * @param actionable the <code>MFSActionable</code> used to execute the
	 *        RTV_QC transaction
	 * @param parent the parent <code>Component</code> for an
	 *        <code>IGSMessageBox</code>
	 * @return zero on success, nonzero on error
	 */
	public static int loadSuspendCodes(DefaultListModel listModel,
										MFSActionable actionable, Component parent)
	{
		int rc = 0;
		try
		{
			MFSTransaction rtv_qc = new MFSFixedTransaction("RTV_QC    ");
			rtv_qc.setActionMessage("Retrieving List of Suspend Codes, Please Wait...");
			MFSComm.getInstance().execute(rtv_qc, actionable);
			rc = rtv_qc.getReturnCode();

			if (rc == 0)
			{
				final String data = rtv_qc.getOutput();

				int start = 0;
				int end = 64;
				final int len = data.length();

				while (end < len)
				{
					listModel.addElement(data.substring(start, end));
					start += 64;
					end += 64;
				}

				listModel.addElement(data.substring(start));
			}
			else
			{
				IGSMessageBox.showOkMB(parent, null, rtv_qc.getErms(), null);
			}
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(parent, null, null, e);
			rc = 10;
		}
		return rc;
	}
}
