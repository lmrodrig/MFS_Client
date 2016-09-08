/* © Copyright IBM Corporation 2007. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2007-11-27      33401JM  R Prechel        -Initial version
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.dialog;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import mfsxml.MfsXMLDocument;

import com.ibm.rchland.mfgapps.client.utils.messagebox.IGSMessageBox;
import com.ibm.rchland.mfgapps.mfsclient.MFSActionableDialog;
import com.ibm.rchland.mfgapps.mfsclient.MFSFrame;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSConstants;
import com.ibm.rchland.mfgapps.mfscommon.MFSConfig;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSComm;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSTransaction;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSXmlTransaction;

/**
 * <code>MFSChangePwdDialog</code> is the <code>MFSActionableDialog</code>
 * used to change a user's password.
 * @author The MFS Client Development Team
 */
public class MFSChangePwdDialog
	extends MFSActionableDialog
{
	private static final long serialVersionUID = 1L;
	/** The User ID <code>JLabel</code>. */
	private JLabel lblUserID;

	/** The Current Password <code>JTextField</code>. */
	private JTextField tfCurPwd;

	/** The New Password <code>JTextField</code>. */
	private JTextField tfNewPwd;

	/** The Verify New Password <code>JTextField</code>. */
	private JTextField tfVerifyPwd;

	/** The Change <code>JButton</code>. */
	protected JButton pbChange = createButton("Change", 'h');

	/** The Clear <code>JButton</code>. */
	protected JButton pbClear = createButton("Clear", 'C');

	/** The Cancel <code>JButton</code>. */
	private JButton pbCancel = createButton("Cancel", 'n');

	/**
	 * Constructs a new <code>MFSChangePwdDialog</code>.
	 * @param parent the <code>MFSFrame</code> that caused this
	 *        <code>MFSChangePwdDialog</code> to be displayed
	 * @param required <code>true</code> if the user is required to change
	 *        his/her password; <code>false</code> otherwise
	 */
	public MFSChangePwdDialog(MFSFrame parent, boolean required)
	{
		super(parent, "Change Password");
		setDefaultCloseOperation(required ? DO_NOTHING_ON_CLOSE : DISPOSE_ON_CLOSE);
		createLayout();
		if (required)
		{
			this.pbCancel.setEnabled(false);
		}
		addMyListeners();
	}

	/** Adds this dialog's <code>Component</code>s to the layout. */
	private void createLayout()
	{
		JLabel userIDLabel = createLabel("User ID");
		JLabel currentPwdLabel = createLabel("Current Password");
		JLabel newPwdLabel = createLabel("New Password");
		JLabel verifyPwdLabel = createLabel("Verify New Password");

		this.lblUserID = createLabel(MFSConfig.getInstance().getConfigValue("USER")); //$NON-NLS-1$
		final int COLUMNS = MFSConstants.MEDIUM_TF_COLS;
		this.tfCurPwd = createPasswordField(COLUMNS, 8, currentPwdLabel);
		this.tfNewPwd = createPasswordField(COLUMNS, 8, newPwdLabel);
		this.tfVerifyPwd = createPasswordField(COLUMNS, 8, verifyPwdLabel);

		JPanel buttonPanel = new JPanel(null);
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
		buttonPanel.add(this.pbChange);
		buttonPanel.add(Box.createHorizontalGlue());
		buttonPanel.add(this.pbClear);
		buttonPanel.add(Box.createHorizontalGlue());
		buttonPanel.add(this.pbCancel);

		JPanel contentPaneCenter = new JPanel(new GridBagLayout());
		contentPaneCenter.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1, 1,
				GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(4, 20, 4, 20), 0, 0);

		contentPaneCenter.add(userIDLabel, gbc);
		gbc.gridy++;
		contentPaneCenter.add(currentPwdLabel, gbc);
		gbc.gridy++;
		contentPaneCenter.add(newPwdLabel, gbc);
		gbc.gridy++;
		contentPaneCenter.add(verifyPwdLabel, gbc);

		gbc.gridx++;
		gbc.gridy = 0;

		contentPaneCenter.add(this.lblUserID, gbc);
		gbc.gridy++;
		contentPaneCenter.add(this.tfCurPwd, gbc);
		gbc.gridy++;
		contentPaneCenter.add(this.tfNewPwd, gbc);
		gbc.gridy++;
		contentPaneCenter.add(this.tfVerifyPwd, gbc);

		gbc.gridx--;
		gbc.gridy++;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(20, 20, 0, 20);
		contentPaneCenter.add(buttonPanel, gbc);

		setContentPane(contentPaneCenter);
		setButtonDimensions(this.pbChange, this.pbClear, this.pbCancel);
		pack();
	}

	/** Adds the listeners to this dialog's <code>Component</code>s. */
	protected void addMyListeners()
	{
		this.pbChange.addActionListener(this);
		this.pbClear.addActionListener(this);
		this.pbCancel.addActionListener(this);

		this.pbChange.addKeyListener(this);
		this.pbClear.addKeyListener(this);
		this.pbCancel.addKeyListener(this);

		this.tfCurPwd.addKeyListener(this);
		this.tfNewPwd.addKeyListener(this);
		this.tfVerifyPwd.addKeyListener(this);
	}

	/** Removes the listeners from this dialog's <code>Component</code>s. */
	protected void removeMyListeners()
	{
		this.pbChange.removeActionListener(this);
		this.pbClear.removeActionListener(this);
		this.pbCancel.removeActionListener(this);

		this.pbChange.removeKeyListener(this);
		this.pbClear.removeKeyListener(this);
		this.pbCancel.removeKeyListener(this);

		this.tfCurPwd.removeKeyListener(this);
		this.tfNewPwd.removeKeyListener(this);
		this.tfVerifyPwd.removeKeyListener(this);
	}

	/**
	 * Invoked when {@link #pbChange} is selected. Executes function CHGPWD of
	 * W_MFSAUTF to change the user's password.
	 */
	private void changePassword()
	{
		try
		{
			removeMyListeners();

			String user = this.lblUserID.getText().toUpperCase();
			String password = this.tfCurPwd.getText().toUpperCase();
			String newPW = this.tfNewPwd.getText().toUpperCase();
			String verifyPW = this.tfVerifyPwd.getText().toUpperCase();

			if (password.length() == 0 || newPW.length() == 0 || verifyPW.length() == 0)
			{
				String title = "Invalid Input";
				String erms = "You must fill in all of the password fields.";
				IGSMessageBox.showOkMB(this, title, erms, null);
			}
			else if (!newPW.equals(verifyPW))
			{
				String title = "Invalid Input";
				String erms = "Passwords do not match.  Please re-type passwords.";
				IGSMessageBox.showOkMB(this, title, erms, null);
				this.tfNewPwd.setText(""); //$NON-NLS-1$
				this.tfVerifyPwd.setText(""); //$NON-NLS-1$
			}
			else
			{
				MfsXMLDocument input = new MfsXMLDocument("W_MFSAUTF"); //$NON-NLS-1$
				input.addOpenTag("DATA"); //$NON-NLS-1$
				input.addCompleteField("WHO", "MFSCLIENT"); //$NON-NLS-1$ //$NON-NLS-2$
				input.addCompleteField("FUNC", "CHGPWD"); //$NON-NLS-1$ //$NON-NLS-2$
				input.addCompleteField("USER", user); //$NON-NLS-1$ //$NON-NLS-2$
				input.addCompleteField("PWD", password); //$NON-NLS-1$ //$NON-NLS-2$
				input.addCompleteField("NEW", newPW); //$NON-NLS-1$ //$NON-NLS-2$
				input.finalizeXML();

				MFSTransaction w_mfsautf = new MFSXmlTransaction(input.toString());
				w_mfsautf.setActionMessage("Changing Password, Please Wait...");
				MFSComm.getInstance().execute(w_mfsautf, this);

				if (w_mfsautf.getReturnCode() == 0)
				{
					String title = "Success Changing Password";
					String msg = "Your password has been changed.";
					IGSMessageBox.showOkMB(this, title, msg, null);
					dispose();
				}
				else
				{
					String title = "Error Changing Password";
					IGSMessageBox.showOkMB(this, title, w_mfsautf.getErms(), null);
					clear();
				}
			}
		}
		catch (Exception e)
		{
			String title = "Error Changing Password";
			IGSMessageBox.showOkMB(this, title, null, e);
			clear();
		}
		addMyListeners();
	}

	/** Clears the password fields and requests the focus for the top password field. */
	private void clear()
	{
		this.tfCurPwd.setText(""); //$NON-NLS-1$
		this.tfNewPwd.setText(""); //$NON-NLS-1$
		this.tfVerifyPwd.setText(""); //$NON-NLS-1$
		toFront();
		this.tfCurPwd.requestFocusInWindow();
	}

	/**
	 * Invoked when an action occurs.
	 * <p>
	 * Invokes the {@link #changePassword()} method if {@link #pbChange} was the
	 * event source. Invokes the {@link #clear()} method if {@link #pbClear} was
	 * the event source. Invokes the {@link #dispose()} method if
	 * {@link #pbCancel} was the event source.
	 * @param ae the <code>ActionEvent</code>
	 */
	public void actionPerformed(ActionEvent ae)
	{
		try
		{
			final Object source = ae.getSource();
			if (this.pbChange == source)
			{
				changePassword();
			}
			else if (this.pbClear == source)
			{
				clear();
			}
			else if (this.pbCancel == source)
			{
				dispose();
			}
		}
		catch (Throwable throwable)
		{
			throwable.printStackTrace();
		}
	}

	/**
	 * Invoked when a key is pressed.
	 * <p>
	 * Invokes the {@link JButton#doClick()} method of the appropriate
	 * <code>JButton</code> when the enter key is pressed. Invokes the
	 * {@link #dispose()} method when the escape key is pressed if the user is
	 * not required to change his/her password.
	 * @param ke the <code>KeyEvent</code>
	 */
	public void keyPressed(KeyEvent ke)
	{
		final int keyCode = ke.getKeyCode();
		if (keyCode == KeyEvent.VK_ENTER)
		{
			final Object source = ke.getSource();
			if (source == this.pbClear)
			{
				this.pbClear.requestFocusInWindow();
				this.pbClear.doClick();
			}
			else if (source == this.pbCancel)
			{
				this.pbCancel.requestFocusInWindow();
				this.pbCancel.doClick();
			}
			else
			{
				this.pbChange.requestFocusInWindow();
				this.pbChange.doClick();
			}
		}
		else if (keyCode == KeyEvent.VK_ESCAPE && this.pbCancel.isEnabled())
		{
			this.dispose();
		}
	}

	/**
	 * Invoked the first time a window is made visible.
	 * <p>
	 * Requests the focus for {@link #tfCurPwd}.
	 * @param we the <code>WindowEvent</code>
	 */
	public void windowOpened(WindowEvent we)
	{
		if (we.getSource() == this)
		{
			this.tfCurPwd.requestFocusInWindow();
		}
	}
}
