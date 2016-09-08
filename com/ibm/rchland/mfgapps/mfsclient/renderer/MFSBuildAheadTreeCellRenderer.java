/* © Copyright IBM Corporation 2009. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2009-08-29      37550JL  Ray Perry        -Initial version
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.renderer;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;

import com.ibm.rchland.mfgapps.mfsclient.tree.MFSBuildAheadUO;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSConstants;

/**
 * <code>MFSBuildAheadTreeCellRenderer</code> is the
 * <code>TreeCellRenderer</code> used to render the <code>TreeNode</code>'s
 * of a Build Ahead <code>JTree</code>.
 * @author The MFS Client Development Team
 */
public class MFSBuildAheadTreeCellRenderer
	extends JLabel
	implements TreeCellRenderer
{
	private static final long serialVersionUID = 1L;
	/** Constructs a new <code>MFSBuildAheadTreeCellRenderer</code>. */
	public MFSBuildAheadTreeCellRenderer()
	{
		super();
	}

	/** {@inheritDoc} */
	public Component getTreeCellRendererComponent(JTree tree, Object value,
													boolean selected, boolean expanded,
													boolean leaf, int row,
													boolean hasFocus)
	{
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
		MFSBuildAheadUO userObj = (MFSBuildAheadUO) node.getUserObject();
		setText(userObj.getTreeString());
		setBackground(userObj.getBackground(selected));
		setForeground(userObj.getForeground(selected));
		setComponentOrientation(tree.getComponentOrientation());
        setFont(MFSConstants.MEDIUM_MONOSPACED_FONT);
		return this;
	}

	/** {@inheritDoc} */
	public void paint(Graphics g)
	{
		Color bColor = getBackground();
		int imageOffset = -1;
		if (bColor != null)
		{
			imageOffset = getLabelStart();
			g.setColor(bColor);
			if (getComponentOrientation().isLeftToRight())
			{
				g.fillRect(imageOffset, 0, getWidth() - imageOffset, getHeight());
			}
			else
			{
				g.fillRect(0, 0, getWidth() - imageOffset, getHeight());
			}
		}
		super.paint(g);
	}

	/**
	 * Retuns the location where the label text should start.
	 * @return the location
	 */
	private int getLabelStart()
	{
		Icon currentI = getIcon();
		if (currentI != null && getText() != null)
		{
			return currentI.getIconWidth() + Math.max(0, getIconTextGap() - 1);
		}
		return 0;
	}

	/** {@inheritDoc} */
	public Dimension getPreferredSize()
	{
		Dimension dim = super.getPreferredSize();
		if (dim != null)
		{
			dim = new Dimension(dim.width + 3, dim.height);
		}
		return dim;
	}

	/** Overridden for performance reasons. Does nothing. */
	public void validate()
	{
		//Does nothing
	}

	/** Overridden for performance reasons. Does nothing. */
	public void invalidate()
	{
		//Does nothing
	}

	/** Overridden for performance reasons. Does nothing. */
	public void revalidate()
	{
		//Does nothing
	}

	/**
	 * Overridden for performance reasons. Does nothing. {@inheritDoc}
	 */
	public void repaint(long tm, int x, int y, int width, int height)
	{
		//Does nothing
	}

	/**
	 * Overridden for performance reasons. Does nothing. {@inheritDoc}
	 */
	public void repaint(Rectangle r)
	{
		//Does nothing
	}

	/** Overridden for performance reasons. Does nothing. */
	public void repaint()
	{
		//Does nothing
	}

	/**
	 * Overridden for performance reasons. Fires "text" property changes only.
	 * {@inheritDoc}
	 */
	protected void firePropertyChange(String propertyName, Object oldValue,
										Object newValue)
	{
		// Strings get interned so == can be used instead of equals()
		if (propertyName == "text") //$NON-NLS-1$
		{
			super.firePropertyChange(propertyName, oldValue, newValue);
		}
	}

	/**
	 * Overridden for performance reasons. Does nothing. {@inheritDoc}
	 */
	public void firePropertyChange(String propertyName, byte oldValue, byte newValue)
	{
		//Does nothing
	}

	/**
	 * Overridden for performance reasons. Does nothing. {@inheritDoc}
	 */
	public void firePropertyChange(String propertyName, char oldValue, char newValue)
	{
		//Does nothing
	}

	/**
	 * Overridden for performance reasons. Does nothing. {@inheritDoc}
	 */
	public void firePropertyChange(String propertyName, short oldValue, short newValue)
	{
		//Does nothing
	}

	/**
	 * Overridden for performance reasons. Does nothing. {@inheritDoc}
	 */
	public void firePropertyChange(String propertyName, int oldValue, int newValue)
	{
		//Does nothing
	}

	/**
	 * Overridden for performance reasons. Does nothing. {@inheritDoc}
	 */
	public void firePropertyChange(String propertyName, long oldValue, long newValue)
	{
		//Does nothing
	}

	/**
	 * Overridden for performance reasons. Does nothing. {@inheritDoc}
	 */
	public void firePropertyChange(String propertyName, float oldValue, float newValue)
	{
		//Does nothing
	}

	/**
	 * Overridden for performance reasons. Does nothing. {@inheritDoc}
	 */
	public void firePropertyChange(String propertyName, double oldValue, double newValue)
	{
		//Does nothing
	}

	/**
	 * Overridden for performance reasons. Does nothing. {@inheritDoc}
	 */
	public void firePropertyChange(String propertyName, boolean oldValue, boolean newValue)
	{
		//Does nothing
	}
}
