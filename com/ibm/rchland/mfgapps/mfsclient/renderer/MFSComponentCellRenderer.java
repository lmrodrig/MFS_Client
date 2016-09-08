/* © Copyright IBM Corporation 2005, 2007. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2007-02-15   ~1 34242JR  R Prechel        -Java 5 version; combined 
 *                                            ComponentCellRenderer and
 *                                            ComponentCellMultiLineRenderer
 * 2007-03-28   ~2 19622JM  R Prechel        -Set color to pink instead of white
 *                                            if comp.getExceededPlugs() is true
 * 2007-01-11	~3 39782JM  Martha Luna		 -Support "multi-drop" cables for manufacturing.	
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.renderer;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JList;
import javax.swing.JTextArea;
import javax.swing.ListCellRenderer;

import com.ibm.rchland.mfgapps.mfsclient.MFSPartInstructionJPanel;
import com.ibm.rchland.mfgapps.mfsclient.struct.MFSComponentRec;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSConstants;

/**
 * <code>MFSComponentCellRenderer</code> is the default
 * <code>ListCellRenderer</code> for the part list of a
 * <code>MFSPartInstructionJPanel</code>.
 * @author The MFS Client Development Team
 */
public class MFSComponentCellRenderer
	extends JTextArea
	implements ListCellRenderer
{
	private static final long serialVersionUID = 1L;
	/**
	 * Indicates if the <code>MFSComponentCellRenderer</code> should support
	 * IR instructions. If <code>true</code>, IR instructions are supported
	 * and the {@link MFSComponentRec#getIRDisplayString()} method is used to
	 * determine the text displayed for the <code>MFSComponentRec</code>.
	 * Otherwise, IR instructions are not supported and the
	 * {@link MFSComponentRec#getDisplayString()} method is used.
	 */
	private boolean fieldSupportIR = false; //~1A
	
	/**
	 * The number of lines of display text for the current
	 * <code>MFSComponentRec</code>.
	 */
	private int fieldLines; //~1A

	//~1C Add multiLine parameter
	//~3C Change multiLine to supportIR
	/**
	 * Constructs a new <code>MFSComponentCellRenderer</code>.
	 * @param supportIR <code>true</code> if IR instructions should be
	 *        supported; <code>false</code> if IR instructions should not
	 *        supported
	 */
	public MFSComponentCellRenderer(boolean supportIR)
	{
		this.fieldSupportIR = supportIR;
		if (supportIR)
		{
			setBorder(BorderFactory.createLineBorder(Color.black));
		}
		setFont(MFSConstants.MEDIUM_MONOSPACED_FONT);
		setLineWrap(true);
		setOpaque(true);
	}

	/** {@inheritDoc} */
	public Component getListCellRendererComponent(JList list, Object value, int index,
													boolean isSelected,
													boolean cellHasFocus)
	{
		MFSComponentRec comp = (MFSComponentRec) value;
		//~1A text and number of lines depends on value of fieldSupportIR
		if (this.fieldSupportIR)
		{
			setText(comp.getIRDisplayString());
			final String idsp = comp.getIdsp();
			if ("R".equals(idsp) || "I".equals(idsp) || "8".equals(comp.getTpok())) //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			{
				this.fieldLines = 3;
			}
			else
			{
				//Each plug and the switch setting add an extra line of text
				this.fieldLines = 2 + comp.getNumberOfPlugs(); //~3C
				if (comp.hasSwitchSetting())
				{
					this.fieldLines++;
				}
			}
		}
		else
		{
			setText(comp.getDisplayString());
			final String idsp = comp.getIdsp();
			if ("R".equals(idsp) || "I".equals(idsp) || "8".equals(comp.getTpok())) //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			{
				this.fieldLines = 1;
			}
			else
			{
				// The first plug is displayed inline. Each additional plug and
				// the switch setting add an extra line of text.
				this.fieldLines = Math.max(1, comp.getNumberOfPlugs()); //~3C
				if (comp.hasSwitchSetting())
				{
					this.fieldLines++;
				}
			}
		}
		
		if(list.getParent().getName().startsWith("Deconfig")) //$NON-NLS-1$
		{
			deconfigSetup(comp, isSelected);
		}
		else
		{
			nonDeconfigSetup(comp, isSelected);
		}
		
		return this;
	}
	
	/**
	 * Helper method called by <code>getListCellRendererComponent</code>
	 * when rendering a list for a Deconfig panel.
	 * @param comp the <code>Component_Rec</code>
	 * @param isSelected <code>true</code> if the cell is selected
	 */
	private void deconfigSetup(MFSComponentRec comp, boolean isSelected)
	{
		if(comp.getScrp().equals("1")) //$NON-NLS-1$
		{
			setBackground(isSelected ? Color.black : Color.red);
			setForeground(isSelected ? Color.red : Color.black);
		}
		else if (comp.getItms().equals("1")) //$NON-NLS-1$
		{
			setBackground(isSelected ? Color.black : Color.orange);
			setForeground(isSelected ? Color.orange : Color.black);
		}
		else if(comp.getIdsp().equals("T")) //$NON-NLS-1$
		{
			setBackground(isSelected ? Color.black : Color.yellow);
			setForeground(isSelected ? Color.yellow : Color.black);			
		}		
		else if (comp.getDwnl().equals("1") || comp.getDwns().equals("1")) //$NON-NLS-1$  //$NON-NLS-2$
		{
			setBackground(isSelected ? Color.black : Color.pink);
			setForeground(isSelected ? Color.pink : Color.black);
		}		
		else if (comp.getIdsp().equals("R")) //$NON-NLS-1$
		{
			if (comp.getFqty().equals(comp.getQnty()))
			{
				//~2C Set color to pink instead of white if getExceededPlugs is true
				if (comp.getExceededPlugs())
				{
					setBackground(isSelected ? Color.black : Color.pink);
					setForeground(isSelected ? Color.pink : Color.black);
				}
				else
				{
					setBackground(isSelected ? Color.black : Color.white);
					setForeground(isSelected ? Color.white : Color.black);
				}
			}
			else
			{
				setBackground(isSelected ? Color.black : Color.magenta);
				setForeground(isSelected ? Color.magenta : Color.black);
			}
		}
	}
	
	/**
	 * Helper method called by <code>getListCellRendererComponent</code>
	 * when rendering a list for a panel that is not a Deconfig panel.
	 * @param comp the <code>Component_Rec</code>
	 * @param isSelected <code>true</code> if the cell is selected
	 */
	private void nonDeconfigSetup(MFSComponentRec comp, boolean isSelected)
	{
		/* M-CODE	 */
		if (comp.getRefp().equals("M-CODE      ")) //$NON-NLS-1$
		{
			setBackground(isSelected ? Color.black : Color.blue);
			setForeground(isSelected ? Color.blue : Color.white);
		}
		/* altered  (if an X idsp) */
		else if (comp.getIdsp().equals("X")) //$NON-NLS-1$
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
		//~1C Used conditions (idsp == A or P instead of idsp == A) from
		// ComponentCellRenderer for both single and multiline case
		/* mlr add - single line*/
		else if (comp.getIdsp().equals("A") || comp.getIdsp().equals("P")) //$NON-NLS-1$ //$NON-NLS-2$
		{
			if (comp.getFqty().equals("00000")) //$NON-NLS-1$
			{
				//~1C Used conditions (fsub == Y, B, or C instead of fsub == Y)
				// from ComponentCellRenderer for both single and multiline case
				if (comp.getFsub().equals("Y") || comp.getFsub().equals("B") || comp.getFsub().equals("C")) //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
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
			if(comp.getInterposerResetFlag().equals(">")) //$NON-NLS-1$
			{
				setBackground(isSelected ? Color.gray : Color.cyan);
				setForeground(isSelected ? Color.cyan : Color.gray);
			}
			else
			{
				if(comp.getBufferStatus().equals("2")) //$NON-NLS-1$
				{
					//~1C Used darkGray color from ComponentCellRenderer for
					// both single and multiline case
					setBackground(isSelected ? Color.darkGray : Color.cyan);
					setForeground(isSelected ? Color.cyan : Color.darkGray);
				}
				else
				{	
					setBackground(isSelected ? Color.black : Color.cyan);
					setForeground(isSelected ? Color.cyan : Color.black);
				}	
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
	}
	
	//~1A New method - Allows cells to determine their height
	/** 
	 * Returns this component's preferred size.
	 * @return this component's preferred size
	 */
	public Dimension getPreferredSize()
	{
		int rowHeight = getGraphics().getFontMetrics().getHeight();
		Insets i = getInsets();
		int myHeight = (this.fieldLines * rowHeight) + i.top + i.bottom; 
		return new Dimension(MFSPartInstructionJPanel.PART_COMP_WIDTH, myHeight);
	}
}
