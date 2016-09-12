/* @ Copyright IBM Corporation 2007. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2007-02-21      34242JR  R Prechel        -Initial version
 * 2010-03-09      47595MZ  Ray Perry        -Shenzhen efficiencies
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.utils;

import javax.swing.JList;
import javax.swing.ListModel;
import javax.swing.text.JTextComponent;

import com.ibm.rchland.mfgapps.client.utils.messagebox.IGSMessageBox;

/**
 * <code>MFSListSearch</code> contains methods to search a list.
 * @author The MFS Client Development Team
 */
public class MFSListSearch
{
	/**
	 * Performs a case-insensitive exact match search for the text of
	 * <code>searchTextComponent</code> in <code>list</code>. If the search
	 * text is found, it is selected. Otherwise, the first element in the list
	 * is selected. The text of <code>searchTextComponent</code> is cleared
	 * after the search is complete.
	 * @param list the <code>JList</code> to search
	 * @param searchTextComponent the <code>JTextComponent</code> used to
	 *        enter the search text
	 */
	public static void search(JList list, JTextComponent searchTextComponent)
	{
		search(list, searchTextComponent.getText());
		searchTextComponent.setText(""); //$NON-NLS-1$
	}
	
	public static void search(JList list, String searchText, boolean setFocus)
	{
		try
		{
			final String match = searchText.toUpperCase();
			final ListModel listModel = list.getModel();
			int index = 0;
			for (index = 0; index < listModel.getSize(); index++)
			{
				String listStr = listModel.getElementAt(index).toString();
				if (listStr.trim().startsWith(match))
				{
					break;
				}
			}

			if (index == listModel.getSize())
			{
				index = 0;
			}
			list.setSelectedIndex(index);
			list.ensureIndexIsVisible(index);
			if (setFocus)
			{
				list.requestFocusInWindow();
			}
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(list, null, null, e);
		}
	}

	/**
	 * Performs a case-insensitive exact match search for
	 * <code>searchText</code> in <code>list</code>. If
	 * <code>searchText</code> is found, it is selected. Otherwise, the first
	 * element in the list is selected.
	 * @param list the <code>JList</code> to search
	 * @param searchText the search text
	 */
	public static void search(JList list, String searchText)
	{
		search(list, searchText, true);
	}

	/**
	 * Constructs a new <code>MFSListSearch</code>. This class only has
	 * static methods and does not have any instance variables or instance
	 * methods. Thus, there is no reason to create an instance of
	 * <code>MFSListSearch</code>, so the only constructor is declared
	 * <code>private</code>.
	 */
	private MFSListSearch()
	{
		super();
	}
}
