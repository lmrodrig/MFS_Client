/* © Copyright IBM Corporation 2007. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2007-01-23      34242JR  R Prechel        -Initial version
 * 2013-12-06	~1 00267733	Andrea Cabrera	 -Migration to Java 1.5 and adjustment of blink 
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Cursor;

import javax.swing.JPanel;

import com.ibm.rchland.mfgapps.mfscommon.MFSActionable;

/**
 * <code>MFSActionableDialog</code> is an abstract subclass of
 * {@link com.ibm.rchland.mfgapps.mfsclient.MFSDialog} that implements
 * {@link com.ibm.rchland.mfgapps.mfscommon.MFSActionable} and displays an
 * {@link com.ibm.rchland.mfgapps.mfsclient.MFSActionJPanel}. The
 * {@link javax.swing.JDialog#setContentPane(java.awt.Container)} method is
 * overridden to add the
 * {@link com.ibm.rchland.mfgapps.mfsclient.MFSActionJPanel} to the bottom of
 * the dialog.
 * @author The MFS Client Development Team
 */
public abstract class MFSActionableDialog
	extends MFSDialog
	implements MFSActionable
{
	private static final long serialVersionUID = 1L;
	/**
	 * The <code>MFSActionJPanel</code> used by this
	 * <code>MFSActionableDialog</code>.
	 */
	private MFSActionJPanel actionIndicator = new MFSActionJPanel();

	/**
	 * Constructs a new modal <code>MFSActionableDialog</code> with the
	 * specified <code>title</code> and the specified <code>parent</code> as
	 * owner.
	 * @param parent the <code>MFSFrame</code> that caused this
	 *        <code>MFSActionableDialog</code> to be displayed
	 * @param title the <code>String</code> to display in the title bar
	 * @see MFSDialog#MFSDialog(MFSFrame, String) for default settings
	 */
	public MFSActionableDialog(MFSFrame parent, String title)
	{
		super(parent, title);
	}

	/**
	 * Sets the content pane of the <code>MFSActionableDialog</code> to a new
	 * <code>Container</code> using a <code>BorderLayout</code> with the
	 * specified <code>Container</code> in the center and the
	 * <code>MFSActionJPanel</code> in the south.
	 * @param contentPaneCenter the <code>Container</code> to add to the
	 *        center of the content pane
	 */
	public void setContentPane(Container contentPaneCenter)
	{
		JPanel contentPane = new JPanel(new BorderLayout());
		contentPane.add(contentPaneCenter, BorderLayout.CENTER);
		contentPane.add(this.actionIndicator, BorderLayout.SOUTH);
		super.setContentPane(contentPane);
	}

	/**
	 * Returns the <code>MFSActionJPanel</code> used by this dialog.
	 * @return the <code>MFSActionJPanel</code> used by this dialog
	 */
	public final MFSActionJPanel getActionIndicator()
	{
		return this.actionIndicator;
	}

	/** {@inheritDoc} */
	public void startAction(String message)
	{
		final Cursor waitCursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);
		this.getParentFrame().setCursor(waitCursor);
		this.setCursor(waitCursor);
		this.actionIndicator.startAction(message);
		//~1 This is changed to dispose only the status bar 2D graphics 
		//in order to avoid the blink from the entire parent panel graphics
		//this.update(this.getGraphics());
		if (this.actionIndicator.getGraphics()!= null ){
			this.update(this.getGraphics());
		}
	}

	/** {@inheritDoc} */
	public void updateAction(String message, int progress)
	{
		this.actionIndicator.startAction(message);
		//~1 This is changed to dispose only the status bar 2D graphics 
		//in order to avoid the blink from the entire parent panel graphics
		//this.update(this.getGraphics());
		if (this.actionIndicator.getGraphics()!= null){
			this.actionIndicator.update(this.actionIndicator.getGraphics());
		}
	}

	/** {@inheritDoc} */
	public void stopAction()
	{
		final Cursor defaultCursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
		this.actionIndicator.stopAction();
		this.setCursor(defaultCursor);
		this.getParentFrame().setCursor(defaultCursor);
		//~1 This is changed to dispose only the status bar 2D graphics 
		//in order to avoid the blink from the entire parent panel graphics
		//this.update(this.getGraphics());
		if (this.actionIndicator.getGraphics()!= null){
			this.actionIndicator.update(this.actionIndicator.getGraphics());
		}
	}
}
