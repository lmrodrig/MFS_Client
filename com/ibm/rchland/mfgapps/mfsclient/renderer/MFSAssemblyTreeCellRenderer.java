/* © Copyright IBM Corporation 2007. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2007-01-23      34242JR  R Prechel        -Initial version; derived from MFSTreeCellRenderer
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.renderer;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

/**
 * <code>MFSAssemblyTreeCellRenderer</code> is the
 * <code>TreeCellRenderer</code> for an assembly tree.
 * @author The MFS Client Development Team
 */
public class MFSAssemblyTreeCellRenderer
	extends DefaultTreeCellRenderer
{
	private static final long serialVersionUID = 1L;
	/** The <code>Font</code> used by this <code>TreeCellRenderer</code>. */
	private final Font FONT = new Font("Monospaced", Font.PLAIN, 11); //$NON-NLS-1$

	/** Constructs a new <code>MFSAssemblyTreeCellRenderer</code>. */
	public MFSAssemblyTreeCellRenderer()
	{
		super();
	}

	/** {@inheritDoc} */
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel,
													boolean expanded, boolean leaf,
													int row, boolean hasFocus)
	{
		//The super call configures the DefaultTreeCellRenderer
		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row,
				hasFocus);

		String treeCellString = value.toString();
		setText(treeCellString);
		setFont(this.FONT);

		if ((treeCellString.indexOf("PN=*NONE") == -1) //$NON-NLS-1$
				&& (treeCellString.indexOf("SN=  ") != -1)) //$NON-NLS-1$
		{
			setBackgroundNonSelectionColor(Color.white);
			setForeground(Color.blue);
		}
		else
		{
			setBackgroundNonSelectionColor(Color.white);
			setForeground(Color.black);
		}
		setBackgroundSelectionColor(Color.cyan);
		return this;
	}
}
