/* @ Copyright IBM Corporation 2005, 2007. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2007-01-25      34242JR  R Prechel        -Java 5 version
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.dialog;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import mfsxml.MfsXMLDocument;

import com.ibm.rchland.mfgapps.client.utils.IGSMaxLengthDocument;
import com.ibm.rchland.mfgapps.client.utils.messagebox.IGSMessageBox;
import com.ibm.rchland.mfgapps.mfsclient.MFSActionableDialog;
import com.ibm.rchland.mfgapps.mfsclient.MFSFrame;
import com.ibm.rchland.mfgapps.mfscommon.MFSConfig;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSComm;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSTransaction;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSXmlTransaction;

/**
 * <code>MFSRemoveFlagDialog</code> is the <code>MFSActionableDialog</code>
 * used to remove a PN/SN flag.
 * @author The MFS Client Development Team
 */
public class MFSRemoveFlagDialog
	extends MFSActionableDialog
{
	private static final long serialVersionUID = 1L;
	/** The part number value <code>JLabel</code>. */
	private JLabel vlPN;

	/** The serial number value <code>JLabel</code>. */
	private JLabel vlSN;

	/** The Flag ID value <code>JLabel</code>. */
	private JLabel vlFlagID;

	/** The Flag Value value <code>JLabel</code>. */
	private JLabel vlFlagVal;

	/** The comment input <code>JTextArea</code>. */
	private JTextArea taComment = new JTextArea(new IGSMaxLengthDocument(80), "", 6, 14); //$NON-NLS-1$

	/** The Remove Flag <code>JButton</code>. */
	private JButton pbRemove = createButton("Remove Flag", 'F');

	/** The Cancel <code>JButton</code>. */
	private JButton pbCancel = createButton("Cancel", 'n');

	/** Set <code>true</code> iff the remove flag button is pressed. */
	private boolean fieldPressedEnter = false;

	/** The part number. */
	private String fieldPN;

	/** The serial number. */
	private String fieldSN;

	/** The flag ID (substring(0,8)) and value (substring(8, 16)). */
	private String fieldFlagString;

	/**
	 * Constructs a new <code>MFSRemoveFlagDialog</code>.
	 * @param parent the <code>MFSFrame</code> that caused this
	 *        <code>MFSRemoveFlagDialog</code> to be displayed
	 * @param pn the part number
	 * @param sn the serial number
	 * @param flag the flag to remove
	 */
	public MFSRemoveFlagDialog(MFSFrame parent, String pn, String sn, String flag)
	{
		super(parent, "Change PN/SN Flag");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		this.fieldPN = pn;
		this.fieldSN = sn;
		this.fieldFlagString = flag;

		createLayout();
		addMyListeners();
	}

	/** Adds this dialog's <code>Component</code>s to the layout. */
	private void createLayout()
	{
		JLabel partNameLabel = createSmallNameLabel("PN:");
		JLabel serialNameLabel = createSmallNameLabel("SN:");
		JLabel flagIDLabel = createSmallNameLabel("Flag ID:");
		JLabel flagValLabel = createSmallNameLabel("Flag Value:");
		JLabel commentLabel = createSmallNameLabel("Comment:");

		this.vlPN = createSmallValueLabel(partNameLabel);
		this.vlPN.setText(this.fieldPN);

		this.vlSN = createSmallValueLabel(serialNameLabel);
		this.vlSN.setText(this.fieldSN);

		this.vlFlagID = createSmallValueLabel(flagIDLabel);
		this.vlFlagID.setText(this.fieldFlagString.substring(0, 8));

		this.vlFlagVal = createSmallValueLabel(flagValLabel);
		this.vlFlagVal.setText(this.fieldFlagString.substring(8, 16));

		this.taComment.setLineWrap(true);

		JPanel contentPane = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(10, 10, 0, 5), 0, 0);

		contentPane.add(partNameLabel, gbc);

		gbc.gridy++;
		gbc.insets = new Insets(2, 10, 0, 5);
		contentPane.add(serialNameLabel, gbc);

		gbc.gridy++;
		gbc.insets = new Insets(10, 10, 0, 5);
		contentPane.add(flagIDLabel, gbc);

		gbc.gridy++;
		gbc.insets = new Insets(2, 10, 0, 5);
		contentPane.add(flagValLabel, gbc);

		gbc.gridy++;
		gbc.insets = new Insets(4, 10, 0, 5);
		contentPane.add(commentLabel, gbc);

		gbc.gridy++;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.insets = new Insets(10, 10, 20, 5);
		contentPane.add(this.pbRemove, gbc);

		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(10, 0, 0, 10);
		contentPane.add(this.vlPN, gbc);

		gbc.gridy++;
		gbc.insets = new Insets(2, 0, 0, 10);
		contentPane.add(this.vlSN, gbc);

		gbc.gridy++;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(10, 0, 0, 10);
		contentPane.add(this.vlFlagID, gbc);

		gbc.gridy++;
		gbc.insets = new Insets(2, 0, 0, 10);
		contentPane.add(this.vlFlagVal, gbc);

		gbc.gridy++;
		gbc.insets = new Insets(4, 0, 0, 10);
		contentPane.add(this.taComment, gbc);

		gbc.gridy++;
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.insets = new Insets(10, 0, 20, 10);
		contentPane.add(this.pbCancel, gbc);

		setContentPane(contentPane);
		setButtonDimensions(this.pbRemove, this.pbCancel);
		commentLabel.setPreferredSize(new Dimension(this.taComment.getPreferredSize()));
		pack();
	}

	/** Adds the listeners to this dialog's <code>Component</code>s. */
	private void addMyListeners()
	{
		this.pbRemove.addActionListener(this);
		this.pbCancel.addActionListener(this);

		this.pbRemove.addKeyListener(this);
		this.pbCancel.addKeyListener(this);
		this.taComment.addKeyListener(this);
	}

	/**
	 * Returns <code>true</code> iff the remove flag button was pressed.
	 * @return <code>true</code> iff the remove flag button was pressed
	 */
	public boolean getPressedEnter()
	{
		return this.fieldPressedEnter;
	}

	/** Calls CHG_PNSNF to remove a flag. */
	public void removeFlag()
	{
		try
		{
			this.fieldPressedEnter = true;

			String taCommentText = this.taComment.getText();
			if (taCommentText.indexOf("\n") != -1) //$NON-NLS-1$
			{
				taCommentText = taCommentText.replace('\n', ' ');
			}

			MfsXMLDocument xml_data1 = new MfsXMLDocument("CHG_PNSNF"); //$NON-NLS-1$
			xml_data1.addOpenTag("DATA"); //$NON-NLS-1$
			xml_data1.addCompleteField("INPN", this.fieldPN.trim().toUpperCase()); //$NON-NLS-1$
			xml_data1.addCompleteField("INSQ", this.fieldSN.trim().toUpperCase()); //$NON-NLS-1$
			xml_data1.addCompleteField("FGID", this.fieldFlagString.substring(0, 8).trim().toUpperCase()); //$NON-NLS-1$
			xml_data1.addCompleteField("FVAL", this.fieldFlagString.substring(8, 16).trim().toUpperCase()); //$NON-NLS-1$
			xml_data1.addOpenTag("NEWF"); //$NON-NLS-1$
			xml_data1.addCloseTag("NEWF"); //$NON-NLS-1$
			xml_data1.addCompleteField("CMMT", taCommentText.trim().toUpperCase()); //$NON-NLS-1$
			xml_data1.addCompleteField("UACT", "R"); //$NON-NLS-1$ //$NON-NLS-2$
			xml_data1.addCompleteField("USER", MFSConfig.getInstance().get8CharUser()); //$NON-NLS-1$
			xml_data1.addCloseTag("DATA"); //$NON-NLS-1$
			xml_data1.finalizeXML();

			MFSTransaction chg_pnsnf = new MFSXmlTransaction(xml_data1.toString());
			chg_pnsnf.setActionMessage("Updating PN/SN Flag, Please Wait...");
			MFSComm.getInstance().execute(chg_pnsnf, this);

			if (chg_pnsnf.getReturnCode() == 0)
			{
				this.pbRemove.requestFocusInWindow();
				this.dispose();
			}
			else
			{
				IGSMessageBox.showOkMB(this, null, chg_pnsnf.getErms(), null);
				this.toFront();
			}
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(this, null, null, e);
		}
		this.toFront();
	}

	/**
	 * Invoked when an action occurs.
	 * @param ae the <code>ActionEvent</code>
	 */
	public void actionPerformed(ActionEvent ae)
	{
		try
		{
			final Object source = ae.getSource();
			if (source == this.pbCancel)
			{
				this.dispose();
			}
			else if (source == this.pbRemove)
			{
				removeFlag();
			}
		}
		catch (Throwable throwable)
		{
			throwable.printStackTrace();
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
			if (source == this.pbCancel)
			{
				this.pbCancel.requestFocusInWindow();
				this.pbCancel.doClick();
			}
			else if (source == this.pbRemove)
			{
				this.fieldPressedEnter = true;
				this.pbRemove.requestFocusInWindow();
				this.pbRemove.doClick();
			}
		}
		else if (keyCode == KeyEvent.VK_ESCAPE)
		{
			this.pbCancel.requestFocusInWindow();
			this.pbCancel.doClick();
		}
	}

	/**
	 * Invoked the first time a window is made visible. Requests the focus for
	 * the {@link #taComment}.
	 * @param we the <code>WindowEvent</code>
	 */
	public void windowOpened(WindowEvent we)
	{
		if (we.getSource() == this)
		{
			this.taComment.requestFocusInWindow();
		}
	}
}
