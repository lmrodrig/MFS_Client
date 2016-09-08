/* © Copyright IBM Corporation 2006, 2007. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2006-06-29      31801JM  R Prechel        -Initial version
 * 2007-01-23   ~1 34242JR  R Prechel        -Java 5 version
 * 2007-08-27   ~2 38007SE  R Prechel        -Support both MAC ID collection dialogs
 * 2007-09-06   ~3 39786JM  R Prechel        -createDialog changes
 * 2007-09-07   ~4 39786JM  R Prechel        -Fix parsing of VRFY_C output
 * 2008-09-29   ~5 41356MZ  Santiago SC      -New PNRI value to be used : 'N'
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.utils;

import mfsxml.MfsXMLDocument;

import com.ibm.rchland.mfgapps.client.utils.messagebox.IGSMessageBox;
import com.ibm.rchland.mfgapps.mfsclient.MFSActionableDialog;
import com.ibm.rchland.mfgapps.mfsclient.MFSDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSMacIDDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSMacIDDialogBase;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSMacIDListDialog;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSComm;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSTransaction;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSXmlTransaction;
import com.ibm.rchland.mfgapps.mfscommon.exception.MFSTransactionException;

/**
 * <code>MFSMacAddressUpdater</code> contains methods to update MAC Address information.
 * @author The MFS Client Development Team
 */
public class MFSMacAddressUpdater
{
	//~1C Moved return code constants here from MFSReturnCodes
	/** Return code used to indicate no error message needs to be displayed. */
	public static final int RC_ERR_NO_MSG_NEEDED = -44;

	/**
	 * Error code returned by <code>VRFY_C</code> when MAC ID information is
	 * missing on parts inside assemblies.
	 */
	public static final int NO_MAC_ID_IN_SUB = 112;

	/**
	 * Constructs a new <code>MFSMacAddressUpdater</code>. This class only
	 * has static variables and static methods and does not have any instance
	 * variables or instance methods. Thus, there is no reason to create an
	 * instance of <code>MFSMacAddressUpdater</code>, so the only constructor
	 * is declared <code>private</code>.
	 */
	private MFSMacAddressUpdater()
	{
		super();
	}

	//~2A New method
	/**
	 * Creates an instance of <code>MFSMacIDDialogBase</code> that is
	 * appropriate for handling a part with the specified <code>macq</code>
	 * and <code>pnri</code> values.
	 * @param parent the <code>MFSDialog</code> that will display the
	 *        <code>MFSMacIDDialogBase</code>
	 * @param macq the value of the MACQ field
	 * @param pnri the value of the PNRI field
	 * @return the instance of <code>MFSMacIDDialogBase</code>;
	 *         <code>null</code> if the values of <code>macq</code> or
	 *         <code>pnri</code> are invalid
	 */
	public static MFSMacIDDialogBase createDialog(MFSDialog parent, String macq,
													String pnri)
	{
		return createDialog(parent, macq, pnri, null, null, null);
	}

	//~3C Change createDialog to display error and return null
	//    instead of throwing MFSException with error message

	//~2A New method
	/**
	 * Creates an instance of <code>MFSMacIDDialogBase</code> that is
	 * appropriate for handling a part with the specified <code>macq</code>
	 * and <code>pnri</code> values.
	 * @param parent the <code>MFSDialog</code> that will display the
	 *        <code>MFSMacIDDialogBase</code>
	 * @param macq the value of the MACQ field
	 * @param pnri the value of the PNRI field
	 * @param pn the part number
	 * @param sn the sequence number
	 * @param csni the value of the CSNI field
	 * @return the instance of <code>MFSMacIDDialogBase</code>;
	 *         <code>null</code> if the values of <code>macq</code> or
	 *         <code>pnri</code> are invalid
	 */
	public static MFSMacIDDialogBase createDialog(MFSDialog parent, String macq,
													String pnri, String pn, String sn,
													String csni)
	{
		if (pnri.equals("M") || pnri.equals("U") || 
				pnri.equals("N")) //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //~5C
		{
			try
			{
				return new MFSMacIDListDialog(parent.getParentFrame(), macq, pnri, pn, sn, csni);
			}
			catch (IllegalArgumentException iae)
			{
				String title = "Invalid MACQ";
				String erms = "Invalid MAC Address Quantity. Please contact engineering support.";
				IGSMessageBox.showOkMB(parent, title, erms, null);
				return null;
			}
		}
		else if (pnri.equals("T") || pnri.equals("V")) //$NON-NLS-1$ //$NON-NLS-2$ //~5C 
		{
			return new MFSMacIDDialog(parent.getParentFrame(), pnri, pn, sn, csni);
		}
		else
		{
			String title = "Invalid PNRI";
			String erms = "Invalid Part Collection Flag. Please contact engineering support.";
			IGSMessageBox.showOkMB(parent, title, erms, null);
			return null;
		}
	}

	/**
	 * Prompts to collect MAC Address Information for parts inside an assembly
	 * when the information is missing.
	 * @param dialog the <code>MFSActionableDialog</code> calling this method
	 * @param data the return data from a transaction that called
	 *        <code>VFRY_C</code> and failed with a {@link #NO_MAC_ID_IN_SUB}
	 *        return code
	 */
	public static void updateChildMacIDs(MFSActionableDialog dialog, String data)
	{
		final int REC_SIZE = 32;
		int index = 0;
		boolean error = false;

		while (data.length() >= index + REC_SIZE)
		{
			String rec = data.substring(index, index + REC_SIZE); //~4C
			String pn = rec.substring(0, 12);
			String sn = rec.substring(12, 24);
			String pnri = rec.substring(24, 25);
			String csni = rec.substring(25, 26);
			String macq = rec.substring(26, 28);

			//~2C Use createDialog and add try catch for MFSException
			//~3C Change createDialog to display error and return null
			//    instead of throwing MFSException with error message
			MFSMacIDDialogBase macIDDialog = createDialog(dialog, macq, pnri, pn, sn, csni);
			if (macIDDialog != null)
			{
				do
				{
					try
					{
						error = false;
						updateMac(dialog, macIDDialog, pn, sn);
					}
					catch (MFSTransactionException te)
					{
						error = true;
					}
				}
				while (error);
			}

			index += REC_SIZE;
		}
	}

	//~2C Use MFSMacIDDialogBase as a parameter instead of MFSMacIDDialog
	/**
	 * Calls the <code>UPDT_MAC</code> server transaction.
	 * @param dialog the <code>MFSActionableDialog</code> that called
	 *        {@link #updateChildMacIDs}
	 * @param macDialog the <code>MFSMacIDDialogBase</code> used to prompt for
	 *        information
	 * @param pn the part number
	 * @param sn the sequence number
	 * @return 0 if UPDT_MAC transaction was successful;
	 *         {@link #RC_ERR_NO_MSG_NEEDED} if the user canceled the call to UPDT_MAC
	 * @throws MFSTransactionException if the UPDT_MAC transaction failed
	 */
	public static int updateMac(MFSActionableDialog dialog, MFSMacIDDialogBase macDialog,
								String pn, String sn)
		throws MFSTransactionException
	{
		int rc = RC_ERR_NO_MSG_NEEDED;

		macDialog.setLocationRelativeTo(dialog);
		macDialog.setVisible(true);

		if (macDialog.getDoneSelected())
		{
			MfsXMLDocument xmlData = new MfsXMLDocument("UPDT_MAC"); //$NON-NLS-1$
			xmlData.addOpenTag("DATA"); //$NON-NLS-1$
			xmlData.addCompleteField("INPN", pn); //$NON-NLS-1$
			xmlData.addCompleteField("INSQ", sn); //$NON-NLS-1$
			macDialog.addMacXmlToDocument(xmlData); //~2C
			xmlData.addCloseTag("DATA"); //$NON-NLS-1$
			xmlData.finalizeXML();

			//~1C Use MFSTransaction
			MFSTransaction updt_mac = new MFSXmlTransaction(xmlData.toString());
			updt_mac.setActionMessage("Updating MAC Address ID's, Please Wait...");
			MFSComm.getInstance().execute(updt_mac, dialog);
			rc = updt_mac.getReturnCode();

			if (rc != 0)
			{
				//~1C Use IGSMessageBox and getErms
				IGSMessageBox.showOkMB(dialog, null, updt_mac.getErms(), null);
				throw new MFSTransactionException(
						"UPDT_MAC", updt_mac.getInput(), updt_mac.getOutput(), rc); //$NON-NLS-1$
			}
		}
		return rc;
	}
}
