/* © Copyright IBM Corporation 2005, 2007. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2007-02-16      34242JR  R Prechel        -Java 5 version
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.dialog;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.ibm.rchland.mfgapps.client.utils.messagebox.IGSMessageBox;
import com.ibm.rchland.mfgapps.mfsclient.MFSDialog;
import com.ibm.rchland.mfgapps.mfsclient.MFSFrame;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSConstants;

/**
 * <code>MFSAcknowledgeInstructionDialog</code> is the <code>MFSDialog</code>
 * used to acknowledge a non-part instruction.
 * @author The MFS Client Development Team
 */
public class MFSAcknowledgeInstructionDialog
	extends MFSDialog
{
	private static final long serialVersionUID = 1L;
	/** The input <code>JTextField</code>. */
	private JTextField tfInput = createTextField(MFSConstants.SMALL_TF_COLS, 0);

	/** The log <code>JButton</code>. */
	private JButton pbLog = createButton("F2 = Log");

	/** The cancel <code>JButton</code>. */
	private JButton pbCancel = createButton("ESC = Cancel");

	/** Set <code>true</code> iff the log button is pressed. */
	private boolean fieldPressedLog = false;

	/**
	 * Constructs a new <code>MFSAcknowledgeInstructionDialog</code>.
	 * @param parent the <code>MFSFrame</code> that caused this
	 *        <code>MFSAcknowledgeInstructionDialog</code> to be displayed
	 */
	public MFSAcknowledgeInstructionDialog(MFSFrame parent)
	{
		super(parent, "Acknowledge Instruction");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		createLayout();
		addMyListeners();
	}
	
	/** Adds this dialog's <code>Component</code>s to the layout. */
	private void createLayout()
	{
		JPanel contentPane = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 2, 1, 1, 1,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, 
				new Insets(10, 4, 10, 4), 0, 0);
		contentPane.add(this.tfInput, gbc);

		gbc.gridy++;
		gbc.gridwidth = 1;
		gbc.insets = new Insets(10, 4, 10, 10);
		contentPane.add(this.pbLog, gbc);

		gbc.gridx = 1;
		gbc.insets = new Insets(10, 10, 10, 4);
		contentPane.add(this.pbCancel, gbc);

		this.setContentPane(contentPane);
		setButtonDimensions(this.pbLog, this.pbCancel);
		this.pack();
	}
	
	/** Adds the listeners to this dialog's <code>Component</code>s. */
	private void addMyListeners()
	{
		this.pbLog.addActionListener(this);
		this.pbCancel.addActionListener(this);
		this.pbLog.addKeyListener(this);
		this.pbCancel.addKeyListener(this);
		this.tfInput.addKeyListener(this);
	}

	/**
	 * Returns <code>true</code> iff the log button was pressed
	 * @return <code>true</code> iff the log button was pressed
	 */
	public boolean getPressedLog()
	{
		return this.fieldPressedLog;
	}

	/** Processes the input the user entered in the <code>JTextField</code>. */
	private void recvInput()
	{
		if (this.tfInput.getText().equalsIgnoreCase(MFSConstants.LOG_BARCODE)) //$NON-NLS-1$
		{
			this.tfInput.setText(""); //$NON-NLS-1$
			this.pbLog.requestFocusInWindow();
			this.pbLog.doClick();
		}
		else
		{
			IGSMessageBox.showEscapeMB(this, null, "Invalid Input", null);
			this.toFront();
			this.tfInput.setText(""); //$NON-NLS-1$
		}
	}

	/**
	 * Invoked when an action occurs.
	 * @param ae the <code>ActionEvent</code>
	 */
	public void actionPerformed(ActionEvent ae)
	{
		final Object source = ae.getSource();
		if (source == this.pbLog)
		{
			this.fieldPressedLog = true;
			this.dispose();
		}
		else if (source == this.pbCancel)
		{
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
			if (source == this.pbLog)
			{
				this.pbLog.requestFocusInWindow();
				this.pbLog.doClick();
			}
			else if (source == this.pbCancel)
			{
				this.pbCancel.requestFocusInWindow();
				this.pbCancel.doClick();
			}
			else if (source == this.tfInput)
			{
				recvInput();
			}
		} // end if(keyCode == KeyEvent.VK_ENTER)
		else if (keyCode == KeyEvent.VK_F2)
		{
			this.pbLog.requestFocusInWindow();
			this.pbLog.doClick();
		}
		else if (keyCode == KeyEvent.VK_ESCAPE)
		{
			this.pbCancel.requestFocusInWindow();
			this.pbCancel.doClick();
		}
	}

	/**
	 * Invoked the first time a window is made visible.
	 * <p>
	 * Requests the focus for the {@link #tfInput}.
	 * @param we the <code>WindowEvent</code>
	 */
	public void windowOpened(WindowEvent we)
	{
		if (we.getSource() == this)
		{
			this.tfInput.requestFocusInWindow();
		}
	}
}
