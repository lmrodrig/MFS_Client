/* @ Copyright IBM Corporation 2005, 2007. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2007-02-15      34242JR  R Prechel        -Java 5 version
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.renderer;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JList;
import javax.swing.JTextArea;
import javax.swing.ListCellRenderer;

import com.ibm.rchland.mfgapps.mfsclient.struct.MFSComponentRec;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSConstants;

/**
 * <code>MFSComponentCellRendererRwkSub</code> is the default
 * <code>ListCellRenderer</code> for the part list of a
 * <code>MFSRwkSubDialog</code>.
 * @author The MFS Client Development Team
 */
public class MFSComponentCellRendererRwkSub
	extends JTextArea
	implements ListCellRenderer
{
	private static final long serialVersionUID = 1L;
	/** Constructs a new <code>MFSComponentCellRendererRwkSub</code>. */
	public MFSComponentCellRendererRwkSub()
	{
		super();
	}

	/** {@inheritDoc} */
	public Component getListCellRendererComponent(JList list, Object value, int index,
													boolean isSelected,
													boolean cellHasFocus)
	{
		MFSComponentRec comp = (MFSComponentRec) value;
		setText(comp.getDisplayString());
		setLineWrap(true);
		setFont(MFSConstants.MEDIUM_MONOSPACED_FONT);
		setBorder(BorderFactory.createLineBorder(Color.black));

		/* M-CODE */
		if (comp.getRefp().equals("M-CODE      ")) //$NON-NLS-1$
		{
			setBackground(isSelected ? Color.black : Color.blue);
			setForeground(isSelected ? Color.blue : Color.white);
		}
		/* altered */
		else if (comp.getIdsp().equals("X")) // if an X idsp //$NON-NLS-1$
		{
			if (comp.getFsub().equals("Y")) //$NON-NLS-1$
			{
				setBackground(isSelected ? Color.black : Color.orange);
				setForeground(isSelected ? Color.orange : Color.black);
			}
			else
			{
				setBackground(isSelected ? Color.black : Color.lightGray);
				setForeground(isSelected ? Color.lightGray : Color.white);
			}
		}
		/* non-mlr */
		else if (comp.getMlri().equals(" ") || comp.getMlri().equals("0")) //$NON-NLS-1$ //$NON-NLS-2$
		{
			setBackground(isSelected ? Color.black : Color.white);
			setForeground(Color.red);
		}
		/* mlr add */
		else if (comp.getIdsp().equals("A")) //$NON-NLS-1$
		{
			if (comp.getFqty().equals("00000")) //$NON-NLS-1$
			{
				if (comp.getFsub().equals("Y")) //$NON-NLS-1$
				{
					setBackground(isSelected ? Color.black : Color.orange);
					setForeground(isSelected ? Color.orange : Color.black);
				}

				else
				{
					setBackground(isSelected ? Color.black : Color.white);
					setForeground(isSelected ? Color.white : Color.black);
				}
			}
			else // partial
			{
				setBackground(isSelected ? Color.black : Color.green);
				setForeground(isSelected ? Color.green : Color.black);
			}
		}
		/* mlr installed */
		else if (comp.getIdsp().equals("I")) //$NON-NLS-1$
		{
			if (comp.getInterposerResetFlag().equals(">")) //$NON-NLS-1$
			{
				setBackground(isSelected ? Color.gray : Color.cyan);
				setForeground(isSelected ? Color.cyan : Color.gray);
			}
			else
			{
				setBackground(isSelected ? Color.black : Color.cyan);
				setForeground(isSelected ? Color.cyan : Color.black);
			}
		}
		/* mlr to be removed */
		else if (comp.getIdsp().equals("R")) //$NON-NLS-1$
		{
			if (comp.getFqty().equals(comp.getQnty()))
			{
				setBackground(isSelected ? Color.black : Color.white);
				setForeground(isSelected ? Color.white : Color.black);
			}
			else
			{
				setBackground(isSelected ? Color.black : Color.magenta);
				setForeground(isSelected ? Color.magenta : Color.black);
			}
		}
		/* mlr deleted */
		else if (comp.getIdsp().equals("D")) //$NON-NLS-1$
		{
			setBackground(isSelected ? Color.black : Color.yellow);
			setForeground(isSelected ? Color.yellow : Color.black);
		}

		else if (comp.getIdsp().equals("S")) //$NON-NLS-1$
		{
			setBackground(isSelected ? Color.black : Color.yellow);
			setForeground(isSelected ? Color.yellow : Color.blue);
		}
		return this;
	}
}
