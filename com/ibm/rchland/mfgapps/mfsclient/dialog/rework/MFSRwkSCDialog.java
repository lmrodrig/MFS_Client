/* © Copyright IBM Corporation 2004, 2007. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2007-01-22      34242JR  R Prechel        -Java 5 version
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.dialog.rework;

import javax.swing.DefaultListModel;

import com.ibm.rchland.mfgapps.client.utils.messagebox.IGSMessageBox;
import com.ibm.rchland.mfgapps.mfsclient.MFSFrame;
import com.ibm.rchland.mfgapps.mfsclient.MFSListDialog;

/**
 * <code>MFSRwkSCDialog</code> displays the IR code options and allows the
 * user to select a single option.
 * @author The MFS Client Development Team
 */
public class MFSRwkSCDialog
	extends MFSListDialog
{
	private static final long serialVersionUID = 1L;
	/**
	 * Constructs a new <code>MFSRwkSCDialog</code>.
	 * @param parent the <code>MFSFrame</code> that caused this
	 *        <code>MFSRwkSCDialog</code> to be displayed
	 */
	public MFSRwkSCDialog(MFSFrame parent)
	{
		super(parent, "Specify Codes");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		createLayout();
		addMyListeners();
	}

	/**
	 * Loads the IR options list using the specified data.
	 * @param data the data used to load the IR options list
	 * @param key the IR group code key. The first three characters of each
	 *        block of data must match the key for the block of data to be added
	 *        to the list.
	 */
	public void loadRwkSCListModel(String data, String key)
	{
		try
		{
			int start = 0;
			int end = 64;
			final int len = data.length();

			DefaultListModel listModel = new DefaultListModel();
			this.lstOptions.setModel(listModel);

			while (end < len)
			{
				String curr = data.substring(start, end);
				if (curr.substring(0, 3).equals(key)
						&& !curr.substring(5, 7).equals("00"))
				{
					listModel.addElement(curr);
				}
				start += 64;
				end += 64;
			}

			String curr = data.substring(start);
			if (curr.substring(0, 3).equals(key) && !curr.substring(5, 7).equals("00"))
			{
				listModel.addElement(curr);
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
