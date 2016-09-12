/* @ Copyright IBM Corporation 2007. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2007-02-19      34242JR  R Prechel        -Initial version
 * 2007-11-06   ~1 40104PB  R Prechel        -Add screen ID
 * 2013-12-06	~2 00267733	Andrea Cabrera	 -Migration to Java 1.5 and adjustment of blink 
 * 2016-02-18   ~3 1471226  Miguel Rivas     -MFSPanel(parent, source, screenName, layout) deprecated
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import com.ibm.rchland.mfgapps.mfsclient.utils.MFSConstants;
import com.ibm.rchland.mfgapps.mfscommon.MFSActionable;
import com.ibm.rchland.mfgapps.mfscommon.MFSConfig;

/**
 * <code>MFSPanel</code> is the abstract base class for all panels displayed
 * as the main panel of the <code>MFSFrame</code> by the MFS Client.
 * @author The MFS Client Development Team
 */
public abstract class MFSPanel
	extends JPanel
	implements ActionListener, KeyListener, MFSActionable
{
	private static final long serialVersionUID = 1L;
	/** The <code>Color</code> of the <code>TitledBorder</code>. */
	protected static final Color TITLE_BORDER_COLOR = Color.blue;

	/** The <code>MFSFrame</code> used to display this <code>MFSPanel</code>. */
	private MFSFrame fieldParent = null;

	/** The <code>MFSPanel</code> that caused this panel to be displayed. */
	private MFSPanel fieldSource = null;

	//~1A
	/**
	 * The screen ID of this <code>MFSPanel</code>. The screen ID should be
	 * short, consist of only uppercase letters, and be the same as the
	 * identifier used in BUTTON configuration entries.
	 */
	private String fieldScreenID = null;

	/** The screen name of this <code>MFSPanel</code>. */
	private String fieldScreenName = null;

	/** The <code>MFSActionJPanel</code> used by this <code>MFSPanel</code>. */
	private MFSActionJPanel fieldActionIndicator = new MFSActionJPanel();

	/**
	 * Constructs a new <code>MFSPanel</code>.
	 * @param parent the <code>MFSFrame</code> used to display this panel
	 * @param source the <code>MFSPanel</code> that caused this panel to be displayed
	 * @param screenName the screen name of this panel
	 * @param layout the <code>LayoutManager</code> used by this panel
	 * @deprecated Replaced by
	 *             {@link MFSPanel#MFSPanel(MFSFrame, MFSPanel, String, String, LayoutManager)} ~3C
	 */
	/*public MFSPanel(MFSFrame parent, MFSPanel source, String screenName,
					LayoutManager layout)
	{
		//~1C Use constructor chaining
		this(parent, source, screenName, "", layout); //$NON-NLS-1$
	}*/

	//~1A
	/**
	 * Constructs a new <code>MFSPanel</code>.
	 * @param parent the <code>MFSFrame</code> used to display this panel
	 * @param source the <code>MFSPanel</code> that caused this panel to be displayed
	 * @param screenName the screen name of this panel
	 * @param screenID the screen ID of this panel
	 * @param layout the <code>LayoutManager</code> used by this panel
	 */
	public MFSPanel(MFSFrame parent, MFSPanel source, String screenName, String screenID,
					LayoutManager layout)
	{
		super(layout);
		this.fieldParent = parent;
		this.fieldSource = source;
		this.fieldScreenName = screenName;
		this.fieldScreenID = screenID;
		this.setBackground(MFSConstants.PRIMARY_BACKGROUND_COLOR);
	}

	/**
	 * A hook method that should request focus for the default
	 * <code>Component</code> of the <code>MFSPanel</code>.
	 */
	public void assignFocus()
	{
		requestFocusInWindow();
	}

	/**
	 * Creates the <code>TitledBorder</code> for this panel.
	 * @return the <code>TitledBorder</code> for this panel
	 */
	protected TitledBorder createTitledBorder()
	{
		return BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder(TITLE_BORDER_COLOR),
				"Press Esc to Exit",
				TitledBorder.LEFT, TitledBorder.TOP,
				MFSConstants.MEDIUM_DIALOG_FONT,
				TITLE_BORDER_COLOR);
	}

	/**
	 * Returns the <code>MFSActionJPanel</code> used by this panel.
	 * @return the <code>MFSActionJPanel</code> used by this panel
	 */
	public final MFSActionJPanel getActionIndicator()
	{
		return this.fieldActionIndicator;
	}

	/**
	 * Returns the title for the <code>MFSFrame</code> when this
	 * <code>MFSPanel</code> is displayed.
	 * @return the title for the <code>MFSFrame</code> when this
	 *         <code>MFSPanel</code> is displayed
	 */
	public String getMFSPanelTitle()
	{
		MFSConfig config = MFSConfig.getInstance();
		StringBuffer buffer = new StringBuffer(150);
		buffer.append("MFS (");
		buffer.append(config.getConfigValue("VERSION")); //$NON-NLS-1$
		buffer.append(")  ");
		buffer.append(this.fieldScreenName);
		buffer.append(" - Connected to: ");
		buffer.append(config.getConfigValue("MFSSRV")); //$NON-NLS-1$
		buffer.append(" port: ");
		buffer.append(config.getConfigValue("MFSRTR")); //$NON-NLS-1$
		buffer.append("   Cell Type:  ");
		buffer.append(config.getConfigValue("CELLTYPE")); //$NON-NLS-1$

		return buffer.toString();
	}

	/**
	 * Returns the <code>MFSFrame</code> used to display this panel.
	 * @return the <code>MFSFrame</code> used to display this panel
	 */
	public final MFSFrame getParentFrame()
	{
		return this.fieldParent;
	}

	//~1A
	/**
	 * Returns the screen ID of this panel.
	 * @return the screen ID of this panel
	 */
	public final String getScreenID()
	{
		return this.fieldScreenID;
	}

	/**
	 * Returns the screen name of this panel.
	 * @return the screen name of this panel
	 */
	public final String getScreenName()
	{
		return this.fieldScreenName;
	}

	/**
	 * Returns the <code>MFSPanel</code> that caused this panel to be displayed.
	 * @return the <code>MFSPanel</code> that caused this panel to be displayed
	 */
	public final MFSPanel getSource()
	{
		return this.fieldSource;
	}

	//~1A
	/**
	 * Sets the screen ID of this panel.
	 * @param screenID the new screen ID of this panel
	 */
	public void setScreenID(String screenID)
	{
		this.fieldScreenID = screenID;
	}

	/**
	 * Sets the screen name of this panel.
	 * @param screenName the new screen name of this panel
	 */
	public void setScreenName(String screenName)
	{
		this.fieldScreenName = screenName;
	}

	/**
	 * Sets the source of this panel.
	 * @param source the <code>MFSPanel</code> that caused this panel to be displayed
	 */
	public void setSource(MFSPanel source)
	{
		this.fieldSource = source;
	}

	/** {@inheritDoc} */
	public void startAction(String message)
	{
		final Cursor waitCursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);
		this.fieldParent.setCursor(waitCursor);
		this.setCursor(waitCursor);
		this.fieldActionIndicator.startAction(message);
		//~2 This is changed to dispose only the status bar 2D graphics 
		//in order to avoid the blink from the entire parent panel graphics
		//this.fieldParent.update(this.fieldParent.getGraphics());
		if (this.fieldActionIndicator.getGraphics()!= null){
			this.fieldActionIndicator.update(this.fieldActionIndicator.getGraphics());
		}
		
	}

	/** {@inheritDoc} */
	public void updateAction(String message, int progress)
	{
		this.fieldActionIndicator.startAction(message);
		//~2 This is changed to dispose only the status bar 2D graphics 
		//in order to avoid the blink from the entire parent panel graphics
		//this.fieldParent.update(this.fieldParent.getGraphics());
		if (this.fieldActionIndicator.getGraphics()!= null){
			this.fieldActionIndicator.update(this.fieldActionIndicator.getGraphics());
		}
	}

	/** {@inheritDoc} */
	public void stopAction()
	{
		final Cursor defaultCursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
		this.fieldActionIndicator.stopAction();
		this.setCursor(defaultCursor);
		this.fieldParent.setCursor(defaultCursor);
		//~2 This is changed to dispose only the status bar 2D graphics 
		//in order to avoid the blink from the entire parent panel graphics
		//this.fieldParent.update(this.fieldParent.getGraphics());
		if (this.fieldActionIndicator.getGraphics()!= null){
			this.fieldActionIndicator.update(this.fieldActionIndicator.getGraphics());
		}
	}

	/**
	 * Invoked when an action occurs.
	 * @param ae the <code>ActionEvent</code>
	 */
	public abstract void actionPerformed(ActionEvent ae);

	/**
	 * Invoked when a key is pressed.
	 * @param ke the <code>KeyEvent</code>
	 */
	public abstract void keyPressed(KeyEvent ke);

	/**
	 * Invoked when a key is released. Does nothing.
	 * @param ke the <code>KeyEvent</code>
	 */
	public void keyReleased(KeyEvent ke)
	{
		//Does nothing
	}

	/**
	 * Invoked when a key is typed. Does nothing.
	 * @param ke the <code>KeyEvent</code>
	 */
	public void keyTyped(KeyEvent ke)
	{
		//Does nothing
	}
}
