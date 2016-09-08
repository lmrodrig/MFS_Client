/* © Copyright IBM Corporation 2007. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2007-01-23      34242JR  R Prechel        -Initial version; derived from MFSListCellRenderer
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.renderer;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JList;
import javax.swing.JTextArea;
import javax.swing.ListCellRenderer;

/**
 * <code>MFSClogPartCheckListRenderer</code> is the
 * <code>ListCellRenderer</code> for the CLOG Part Check list.
 * @author The MFS Client Development Team
 */
public class MFSClogPartCheckListRenderer
	extends JTextArea
	implements ListCellRenderer
{
	private static final long serialVersionUID = 1L;
	/** The <code>Font</code> used by this <code>ListCellRenderer</code>. */
	private final Font FONT = new Font("Monospaced", Font.PLAIN, 12); //$NON-NLS-1$

	/** Constructs a new <code>MFSClogPartCheckListRenderer</code>. */
	public MFSClogPartCheckListRenderer()
	{
		super();
	}

	/** {@inheritDoc} */
	public Component getListCellRendererComponent(JList list, Object value, int index,
													boolean isSelected,
													boolean cellHasFocus)
	{
		String PnSn_data = value.toString();
		setText(PnSn_data);
		setFont(this.FONT);
		if (PnSn_data.length() > 52)
		{
			String dxLevel = PnSn_data.substring(38, 42);
			String qdLevel = PnSn_data.substring(45, 49);
			
			if ((PnSn_data.charAt(52) == 'G') && (0 < dxLevel.compareTo(qdLevel)))
			{
				setBackground(isSelected ? Color.yellow : Color.white);
				setForeground(isSelected ? Color.red.brighter() : Color.black);
			}
			else if ((PnSn_data.charAt(52) == 'L') && (0 > dxLevel.compareTo(qdLevel)))
			{
				setBackground(isSelected ? Color.yellow : Color.white);
				setForeground(isSelected ? Color.red.brighter() : Color.black);
			}
		}
		else
		{
			setBackground(isSelected ? Color.yellow : Color.white);
			setForeground(Color.blue);
		}
		return this;
	}
}
