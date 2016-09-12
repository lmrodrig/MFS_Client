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

import java.awt.Cursor;

import javax.swing.JEditorPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import com.ibm.rchland.mfgapps.mfsclient.media.MFSMediaHandler;

/**
 * <code>MFSHyperlinkListener</code> is a <code>HyperlinkListener</code>
 * that changes the cursor for ENTERED and EXITED hyperlink events and calls
 * {@link MFSMediaHandler#handleResource(java.net.URL)} for all other events.
 * @author The MFS Client Development Team
 */
public class MFSHyperlinkListener
	implements HyperlinkListener
{
	/** The <code>JComponent</code> used to display the hyperlink. */
	private JEditorPane fieldPane;

	/**
	 * Constructs a new <code>MFSHyperlinkListener</code>.
	 * @param pane the <code>JEditorPane</code> used to display the hyperlink
	 */
	public MFSHyperlinkListener(JEditorPane pane)
	{
		this.fieldPane = pane;
	}
	
	/** {@inheritDoc} */
	public void hyperlinkUpdate(HyperlinkEvent he)
	{
		final HyperlinkEvent.EventType type = he.getEventType();
		
		if(type == HyperlinkEvent.EventType.ENTERED)
		{
			this.fieldPane.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		}
		else if(type == HyperlinkEvent.EventType.EXITED)
		{
			this.fieldPane.setCursor(Cursor.getDefaultCursor());
		}
		else
		{
			try
			{
				MFSMediaHandler.handleResource(he.getURL());
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
}
