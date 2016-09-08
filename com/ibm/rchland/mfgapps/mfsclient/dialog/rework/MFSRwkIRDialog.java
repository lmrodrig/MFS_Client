/* © Copyright IBM Corporation 2004, 2007. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2007-01-22      34242JR  R Prechel        -Java 5 version
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.dialog.rework;

import java.awt.event.KeyEvent;

import javax.swing.DefaultListModel;

import com.ibm.rchland.mfgapps.client.utils.messagebox.IGSMessageBox;
import com.ibm.rchland.mfgapps.mfsclient.MFSFrame;
import com.ibm.rchland.mfgapps.mfsclient.MFSListDialog;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSScroller;

/**
 * <code>MFSRwkIRDialog</code> displays the IR options and allows the user to
 * select a single option.
 * @author The MFS Client Development Team
 */
public class MFSRwkIRDialog
	extends MFSListDialog
{
	private static final long serialVersionUID = 1L;
	/**
	 * Constructs a new <code>MFSRwkIRDialog</code>.
	 * @param parent the <code>MFSFrame</code> that caused this
	 *        <code>MFSRwkIRDialog</code> to be displayed
	 */
	public MFSRwkIRDialog(MFSFrame parent)
	{
		super(parent, "Enter IRcode");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		createLayout();
		addMyListeners();
	}

	/**
	 * Loads the IR options list using the specified data.
	 * @param data the data used to load the IR options list
	 */
	public void loadRwkIRListModel(String data)
	{
		try
		{
			int start = 0;
			int end = 64;
			final int len = data.length();

			DefaultListModel listModel = new DefaultListModel();
			this.lstOptions.setModel(listModel);

			while (end < len)
			{
				String curr = data.substring(start, end);
				if (curr.substring(5, 7).equals("00")) //$NON-NLS-1$
				{
					listModel.addElement(curr);
				}
				start += 64;
				end += 64;
			}

			String curr = data.substring(start);
			if (curr.substring(5, 7).equals("00")) //$NON-NLS-1$
			{
				listModel.addElement(curr);
			}
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
		finally
		{
			pack();
		}
	}

	/**
	 * Sets the default close operation to {@link #DO_NOTHING_ON_CLOSE}
	 * and disables {@link #pbCancel}.
	 */
	public void disableCancel()
	{
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.pbCancel.setEnabled(false);
	}

	/**
	 * Invoked when a key is pressed.
	 * @param ke the <code>KeyEvent</code>
	 */
	public void keyPressed(KeyEvent ke)
	{
		final int keyCode = ke.getKeyCode();
		if (keyCode == KeyEvent.VK_ENTER)
		{
			final Object source = ke.getSource();
			if (source == this.pbCancel)
			{
				if (this.pbCancel.isEnabled())
				{
					this.pbCancel.requestFocusInWindow();
					this.pbCancel.doClick();
				}
			}
			else
			{
				this.pbProceed.requestFocusInWindow();
				this.pbProceed.doClick();
			}
		}
		else if (keyCode == KeyEvent.VK_ESCAPE)
		{
			if (this.pbCancel.isEnabled())
			{
				this.dispose();
			}
		}
		else if (keyCode == KeyEvent.VK_PAGE_DOWN)
		{
			MFSScroller.scrollDown(this.spOptions, this.lstOptions);
			ke.consume();
		}
		else if (keyCode == KeyEvent.VK_PAGE_UP)
		{
			MFSScroller.scrollUp(this.spOptions, this.lstOptions);
			ke.consume();
		}
	}
}
