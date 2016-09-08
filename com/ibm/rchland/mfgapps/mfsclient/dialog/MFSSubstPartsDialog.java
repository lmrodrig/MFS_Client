/* © Copyright IBM Corporation 2005, 2007. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2007-02-22      34242JR  R Prechel        -Java 5 version
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.dialog;

import javax.swing.DefaultListModel;

import com.ibm.rchland.mfgapps.client.utils.messagebox.IGSMessageBox;
import com.ibm.rchland.mfgapps.mfsclient.MFSFrame;
import com.ibm.rchland.mfgapps.mfsclient.MFSListDialog;

/**
 * <code>MFSSubstPartsDialog</code> is the <code>MFSListDialog</code> used
 * to display the list of substitute parts.
 * @author The MFS Client Development Team
 */
public class MFSSubstPartsDialog
	extends MFSListDialog
{
	private static final long serialVersionUID = 1L;
	/**
	 * Constructs a new <code>MFSSubstPartsDialog</code>.
	 * @param parent the <code>MFSFrame</code> that caused this
	 *        <code>MFSSubstPartsDialog</code> to be displayed
	 */
	public MFSSubstPartsDialog(MFSFrame parent)
	{
		super(parent, "Substitute Parts");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		createLayout();
		addMyListeners();
	}

	/**
	 * Loads the substitute part list using the specified data.
	 * @param data the data used to load the substitute part list
	 */
	public void loadSubstPartsListModel(String data)
	{
		try
		{
			int start = 0;
			int end = 44;
			final int len = data.length();

			DefaultListModel listModel = new DefaultListModel();
			this.lstOptions.setModel(listModel);

			while (end <= len)
			{
				StringBuffer info = new StringBuffer(50);
				info.append(data.substring(start, start + 12));
				info.append("   "); //$NON-NLS-1$
				info.append(data.substring(start + 36, start + 44));
				info.append("   "); //$NON-NLS-1$
				info.append(data.substring(start + 12, start + 36));

				listModel.addElement(info.toString());
				start += 44;
				end += 44;
			}
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
		finally
		{
			pack();
		}
	}
}
