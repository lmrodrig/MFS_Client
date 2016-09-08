/* © Copyright IBM Corporation 2005, 2007. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2007-01-28      34242JR  R Prechel        -Java 5 version
 * 2007-10-24| ~01|33507SE |Toribio Hdez.   |-Change params when Calling RTV_WUBYSP to
 *           |    |        |                | be xml and add new LOCAL param as 0
 * 2010-11-01  ~02 49513JM  Toribio H.       -Update RTV_WUBYPS to use new ServerTransaction class            
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.dialog;

import java.awt.Cursor;

import com.ibm.rchland.mfgapps.client.utils.messagebox.IGSMessageBox;
import com.ibm.rchland.mfgapps.mfsclient.MFSFrame;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSPrintingMethods;
import com.ibm.rchland.mfgapps.mfscommon.MFSConfig;
import com.ibm.rchland.mfgapps.mfscommon.comm.trx.RTV_WUBYPS;

/**
 * <code>MFSRtvMctl2Dialog</code> is the Retrieve Work Unit Control Number
 * <code>MFSActionableDialog</code> that executes the RTV_WUBYPS transaction.
 * @author The MFS Client Development Team
 */
@SuppressWarnings("serial")
public class MFSRtvMctl2Dialog
	extends MFSRtvMctlDialog
{
	// Note: Except for the logic in the retrieve method, this dialog is
	// identical to MFSRtvMctlDialog. Instead of repeating the code from
	// MFSRtvMctlDialog, this dialog extends MFSRtvMctlDialog and overrides the
	// retrieve method.

	/**
	 * Constructs a new <code>MFSRtvMctl2Dialog</code>.
	 * @param parent the <code>MFSFrame</code> that caused this
	 *        <code>MFSRtvMctl2Dialog</code> to be displayed
	 */
	public MFSRtvMctl2Dialog(MFSFrame parent)
	{
		super(parent);
	}

	/** Executes the retrieve transaction and prints the appropriate labels. */
	public void retrieve()
	{
		try
		{
			RTV_WUBYPS rtvWUBYPS = new RTV_WUBYPS(this); //~02							
			rtvWUBYPS.setInputINPN(this.vlPN.getText().trim());
			rtvWUBYPS.setInputINSQ(this.vlSN.getText().trim());
			rtvWUBYPS.setInputLOCAL("0"); //$NON-NLS-1$							
			if (rtvWUBYPS.execute())
			{ 
				String mctl = rtvWUBYPS.getOutputMCTL();
				String prln = rtvWUBYPS.getOutputPRLN();
				
				if (MFSConfig.getInstance().containsConfigEntry("ROCHWU")) //$NON-NLS-1$
				{
					String tmpMctlPrln = mctl + prln;
					MFSPrintingMethods.rochwu(tmpMctlPrln, "       ", "    ", //$NON-NLS-1$ //$NON-NLS-2$
												1, getParentFrame()); 
				}
				else
				{
					MFSPrintingMethods.prodsub(prln,"J", mctl, 1, getParentFrame()); //$NON-NLS-1$
				}
			}
			else
			{
				IGSMessageBox.showOkMB(this, null, rtvWUBYPS.getErrorMessage(), null);
				this.toFront();
			}
		}
		catch (Exception e)
		{
			this.setCursor(Cursor.getDefaultCursor());
			IGSMessageBox.showOkMB(this, null, null, e);
			this.toFront();
		}
		clear();
	}
}