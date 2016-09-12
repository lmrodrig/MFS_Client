/* @ Copyright IBM Corporation 2005, 2007. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2007-02-22      34242JR  R Prechel        -Java 5 version
 * ---------- ---- ----------- ---------------- -------------------------------
 * 2012-07-23  ~01 RCQ00207520 Edgar V.         - Change the message. 
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.dialog;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.ibm.rchland.mfgapps.client.utils.messagebox.IGSMessageBox;
import com.ibm.rchland.mfgapps.mfsclient.MFSActionableDialog;
import com.ibm.rchland.mfgapps.mfsclient.MFSFrame;
import com.ibm.rchland.mfgapps.mfsclient.struct.MFSHeaderRec;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSComm;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSFixedTransaction;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSTransaction;

/**
 * <code>MFSValidateSoftDialog</code> is the <code>MFSActionableDialog</code>
 * displayed when a system failed software validation (i.e., the return code of
 * the VRFY_SOFT transaction was nonzero) and allows the user to suspend or fail
 * the system.
 * @author The MFS Client Development Team
 */
public class MFSValidateSoftDialog
	extends MFSActionableDialog
{
	private static final long serialVersionUID = 1L;
	/** The <code>MFSHeaderRec</code> for the work unit. */
	private MFSHeaderRec fieldHeadRec;

	/** Set <code>true</code> iff the suspend button is pressed. */
	private boolean fieldPressedSuspend = false;

	/** The other ending mode. */
	private String fieldOemo = ""; //$NON-NLS-1$

	/** The Suspend (F7) <code>JButton</code>. */
	private JButton pbSuspend = new JButton("F7 = Suspend");

	/** The Fail (F1) <code>JButton</code>. */
	private JButton pbFail = new JButton("F1 = Fail");

	/**
	 * Constructs a new <code>MFSValidateSoftDialog</code>.
	 * @param parent the <code>MFSFrame</code> that caused this
	 *        <code>MFSValidateSoftDialog</code> to be displayed
	 * @param rec the <code>MFSHeaderRec</code> for the work unit
	 */
	public MFSValidateSoftDialog(MFSFrame parent, MFSHeaderRec rec)
	{
		super(parent, "System Validation Error");
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.fieldHeadRec = rec;
		createLayout();
		addMyListeners();
	}

	/** Adds this dialog's <code>Component</code>s to the layout. */
	private void createLayout()
	{
		Font font = new Font("Dialog", Font.BOLD, 18); //$NON-NLS-1$
		JLabel label = createLabel("System Failed Installed Parts Check (S06 Rule lookup). Contact Engineering.", font);//~01C

		JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 20, 0));
		buttonPanel.add(this.pbSuspend);
		buttonPanel.add(this.pbFail);

		JPanel contentPaneCenter = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1, 1,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, 
				new Insets(20, 20, 20, 20), 0, 0);
		contentPaneCenter.add(label, gbc);

		gbc.gridy++;
		contentPaneCenter.add(buttonPanel, gbc);

		this.setContentPane(contentPaneCenter);
		this.pack();
	}

	/** Adds the listeners to this dialog's <code>Component</code>s. */
	private void addMyListeners()
	{
		this.pbSuspend.addActionListener(this);
		this.pbFail.addActionListener(this);
		this.pbSuspend.addKeyListener(this);
		this.pbFail.addKeyListener(this);
	}

	/**
	 * Returns the value of the other ending mode property.
	 * @return the value of the other ending mode property
	 */
	public String getOemo()
	{
		return this.fieldOemo;
	}

	/**
	 * Returns <code>true</code> iff the suspend button was pressed.
	 * @return <code>true</code> iff the suspend button was pressed
	 */
	public boolean getPressedSuspend()
	{
		return this.fieldPressedSuspend;
	}

	/**
	 * Invoked when {@link #pbFail} is selected.
	 * @return the return code
	 */
	public int fail()
	{
		int rc = 0;
		try
		{
			StringBuffer data = new StringBuffer();
			data.append("RTV_OEMO  "); //$NON-NLS-1$
			data.append(this.fieldHeadRec.getPrln());
			data.append(this.fieldHeadRec.getNmbr());
			data.append(this.fieldHeadRec.getProd());

			MFSTransaction rtv_oemo = new MFSFixedTransaction(data.toString());
			rtv_oemo.setActionMessage("Retrieving Other Ending Modes, Please Wait...");
			MFSComm.getInstance().execute(rtv_oemo, this);
			rc = rtv_oemo.getReturnCode();

			if (rc != 0)
			{
				IGSMessageBox.showOkMB(this, null, rtv_oemo.getErms(), null);
				this.toFront();
			}
			// other end modes were found; look for the SWEC end mode
			else
			{
				int index = 0;
				boolean found = false;
				while ((index < rtv_oemo.getOutput().length()) && (!found))
				{
					if (rtv_oemo.getOutput().substring(index, index + 4).equals("SWEC")) //$NON-NLS-1$
					{
						found = true;
					}
					else
					{
						index *= 64;
					}
				}
				if (found)
				{
					this.fieldOemo = "SWEC"; //$NON-NLS-1$
					this.dispose();
				}
				else
				{
					rc = 10;
					String erms = "No SWEC ending mode found, you must suspend";
					IGSMessageBox.showOkMB(this, null, erms, null);
					this.toFront();
				}
			}// end other end modes found
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(this, null, null, e);
			this.toFront();
		}
		return rc;
	}

	/**
	 * Invoked when an action occurs.
	 * @param ae the <code>ActionEvent</code>
	 */
	public void actionPerformed(ActionEvent ae)
	{
		final Object source = ae.getSource();
		if (source == this.pbFail)
		{
			fail();
		}
		else if (source == this.pbSuspend)
		{
			this.fieldPressedSuspend = true;
			this.dispose();
		}
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
			if (source == this.pbSuspend)
			{
				this.pbSuspend.requestFocusInWindow();
				this.pbSuspend.doClick();
			}
			else if (source == this.pbFail)
			{
				this.pbFail.requestFocusInWindow();
				this.pbFail.doClick();
			}
		}
		else if (keyCode == KeyEvent.VK_F7)
		{
			this.pbSuspend.requestFocusInWindow();
			this.pbSuspend.doClick();
		}
		else if (keyCode == KeyEvent.VK_F1)
		{
			this.pbFail.requestFocusInWindow();
			this.pbFail.doClick();
		}
	}

	/**
	 * Invoked the first time a window is made visible.
	 * @param we the <code>WindowEvent</code>
	 */
	public void windowOpened(WindowEvent we)
	{
		if (we.getSource() == this)
		{
			this.pbSuspend.requestFocusInWindow();
		}
	}
}
