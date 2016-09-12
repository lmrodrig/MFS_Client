/* @ Copyright IBM Corporation 2007. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2007-01-23      34242JR  R Prechel        -Initial version
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.utils;

import javax.swing.JList;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

/**
 * <code>MFSScroller</code> contains methods to scroll the selected index of a
 * <code>JList</code> in a <code>JScrollPane</code> up or down.
 * @author The MFS Client Development Team
 */
public class MFSScroller
{
	/**
	 * Causes the specified <code>scrollPane</code> containing the specified
	 * <code>list</code> to scroll down by a block increment or to the bottom
	 * depending on the current scroll value.
	 * @param scrollPane the <code>JScrollPane</code> containing the
	 *        <code>list</code>
	 * @param list the <code>JList</code>
	 */
	public static void scrollDown(JScrollPane scrollPane, JList list)
	{
		if(list.getSelectedIndex() == list.getLastVisibleIndex())
		{
			final JScrollBar vert = scrollPane.getVerticalScrollBar();
			int value = vert.getValue() + vert.getBlockIncrement(1);
			if(value >= vert.getMaximum())
			{
				vert.setValue(vert.getMaximum());
			}
			else
			{
				vert.setValue(value);
			}
		}
		
		list.setSelectedIndex(list.getLastVisibleIndex());
		if(list.getSelectedIndex() == -1)
		{
			list.setSelectedIndex(list.getModel().getSize() - 1);
		}
		list.ensureIndexIsVisible(list.getSelectedIndex());
	}
	
	/**
	 * Causes the specified <code>scrollPane</code> containing the specified
	 * <code>list</code> to scroll up by a block increment or to the top
	 * depending on the current scroll value.
	 * @param scrollPane the <code>JScrollPane</code> containing the
	 *        <code>list</code>
	 * @param list the <code>JList</code>
	 */
	public static void scrollUp(JScrollPane scrollPane, JList list)
	{
		if(list.getSelectedIndex() == list.getFirstVisibleIndex())
		{
			final JScrollBar vert = scrollPane.getVerticalScrollBar();
			int value = vert.getValue() - vert.getBlockIncrement(-1);
			if(value <= 0)
			{
				vert.setValue(0);
			}
			else
			{
				vert.setValue(value);
			}
		}
		
		list.setSelectedIndex(list.getFirstVisibleIndex());
		if(list.getSelectedIndex() == -1)
		{
			list.setSelectedIndex(0);
		}
		list.ensureIndexIsVisible(list.getSelectedIndex());
	}
}
