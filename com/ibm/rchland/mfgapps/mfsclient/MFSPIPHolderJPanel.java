/* © Copyright IBM Corporation 2007. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2007-02-19      34242JR  R Prechel        -Initial version
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.Rectangle;

import javax.swing.JPanel;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;

import com.ibm.rchland.mfgapps.mfsclient.utils.MFSConstants;

/**
 * <code>MFSPIPHolderJPanel</code> is a subclass of <code>JPanel</code> that
 * implements <code>Scrollable</code> and is designed to hold the
 * {@link MFSPartInstructionJPanel}s that are displayed by an
 * {@link MFSInstructionsPanel}.
 * @author The MFS Client Development Team
 */
public class MFSPIPHolderJPanel
	extends JPanel
	implements Runnable, Scrollable
{
	private static final long serialVersionUID = 1L;
	/** If <code>false</code>, {@link #scrollRectToVisible(Rectangle)} does nothing. */
	private volatile boolean fieldScrollRectToVisibleEnabled = false;
	
	/** Constructs a new <code>MFSPIPHolderJPanel</code>. */
	public MFSPIPHolderJPanel()
	{
		super(new GridBagLayout());
		this.setBackground(MFSConstants.PRIMARY_BACKGROUND_COLOR);
	}
	
	/** Disables the {@link #scrollRectToVisible(Rectangle)} method. */
	public void disableScrollRectToVisible()
	{
		this.fieldScrollRectToVisibleEnabled = false;
	}

	/**
	 * Returns the preferred size of the viewport for this
	 * <code>Component</code>.
	 * @return the preferred size of the viewport
	 */
	public Dimension getPreferredScrollableViewportSize()
	{
		return getPreferredSize();
	}

	/**
	 * Returns the unit increment for scrolling in the specified
	 * <code>orientation</code> and <code>direction</code>.
	 * @param visibleRect the view area visible within the viewport
	 * @param orientation the scroll orientation (either
	 *        {@link SwingConstants#VERTICAL} or
	 *        {@link SwingConstants#HORIZONTAL}
	 * @param direction the scroll direction. Less than zero to scroll up/left;
	 *        greater than zero to scroll down/right
	 * @return the unit increment for scrolling
	 */
	public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction)
	{
		switch (orientation)
		{
			case SwingConstants.VERTICAL:
				return visibleRect.height / 10;
			case SwingConstants.HORIZONTAL:
				return visibleRect.width / 10;
			default:
				throw new IllegalArgumentException("Invalid orientation: " + orientation); //$NON-NLS-1$
		}
	}

	/**
	 * Returns the block increment for scrolling in the specified
	 * <code>orientation</code> and <code>direction</code>.
	 * @param visibleRect the view area visible within the viewport
	 * @param orientation the scroll orientation (either
	 *        {@link SwingConstants#VERTICAL} or
	 *        {@link SwingConstants#HORIZONTAL}
	 * @param direction the scroll direction. Less than zero to scroll up/left;
	 *        greater than zero to scroll down/right
	 * @return the unit increment for scrolling
	 */
	public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction)
	{
		switch (orientation)
		{
			case SwingConstants.VERTICAL:
				return visibleRect.height;
			case SwingConstants.HORIZONTAL:
				return visibleRect.width;
			default:
				throw new IllegalArgumentException("Invalid orientation: " + orientation); //$NON-NLS-1$
		}
	}

	/**
	 * Returns <code>true</code> if a viewport should force the width of this
	 * <code>Scrollable</code> to match the width of the viewport.
	 * @return <code>true</code>
	 */
	public boolean getScrollableTracksViewportWidth()
	{
		return true;
	}

	/**
	 * Returns <code>true</code> if a viewport should force the height of this
	 * <code>Scrollable</code> to match the height of the viewport.
	 * @return <code>false</code>
	 */
	public boolean getScrollableTracksViewportHeight()
	{
		return false;
	}
	
	/**
	 * Returns <code>true</code> iff {@link #scrollRectToVisible(Rectangle)}
	 * is enabled.
	 * @return <code>true</code> iff {@link #scrollRectToVisible(Rectangle)}
	 *         is enabled
	 */
	public boolean isScrollRectToVisibleEnabled()
	{
		return this.fieldScrollRectToVisibleEnabled;
	}
	
	/** Enables the {@link #scrollRectToVisible(Rectangle)} method. */
	public void run()
	{
		this.fieldScrollRectToVisibleEnabled = true;
	}
	
	/**
	 * If enabled, forwards the <code>scrollRectToVisible()</code> message to
	 * the <code>MFSPIPHolderJPanel</code>'s parent.
	 * <p>
	 * When an {@link MFSPartInstructionJPanel} is first displayed, the
	 * {@link javax.swing.text.DefaultCaret} of the
	 * {@link javax.swing.text.JTextComponent}s in the
	 * <code>MFSPartInstructionJPanel</code> cause this method to be invoked.
	 * If this method is not disabled when the <code>MFSPIPHolderJPanel</code>
	 * is first displayed by an {@link MFSInstructionsPanel}, the instructions
	 * scroll to the bottom. Therefore, this method is disabled before the
	 * <code>MFSPIPHolderJPanel</code> is displayed for the first time for a
	 * work unit, and is enabled after the <code>MFSPIPHolderJPanel</code> has
	 * been displayed for a work unit.
	 * @param aRect the visible <code>Rectangle</code>
	 * @see javax.swing.JViewport
	 */
	public void scrollRectToVisible(Rectangle aRect)
	{
		if(this.fieldScrollRectToVisibleEnabled)
		{
			super.scrollRectToVisible(aRect);
		}
	}
}