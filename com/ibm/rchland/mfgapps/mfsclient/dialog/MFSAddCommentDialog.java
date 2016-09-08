/* © Copyright IBM Corporation 2005, 2007. All rights reserved.
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
import mfsxml.MfsXMLParser;

import com.ibm.rchland.mfgapps.client.utils.IGSMaxLengthDocument;
import com.ibm.rchland.mfgapps.client.utils.messagebox.IGSMessageBox;
import com.ibm.rchland.mfgapps.mfsclient.MFSActionableDialog;
import com.ibm.rchland.mfgapps.mfsclient.MFSFrame;
import com.ibm.rchland.mfgapps.mfsclient.panel.MFSPartFunctionDisplayPanel;
import com.ibm.rchland.mfgapps.mfscommon.MFSConfig;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSComm;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSTransaction;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSXmlTransaction;

/**
 * <code>MFSAddCommentDialog</code> is the <code>MFSActionableDialog</code>
 * used to enter a PN/SN comment flag.
 * @author The MFS Client Development Team
 */
public class MFSAddCommentDialog
	extends MFSActionableDialog
{
	private static final long serialVersionUID = 1L;
	/** The part number value <code>JLabel</code>. */
	private JLabel vlPN;

	/** The serial number value <code>JLabel</code>. */
	private JLabel vlSN;

	/** The comment input <code>JTextArea</code>. */
	private JTextArea taComment = new JTextArea(new IGSMaxLengthDocument(80), "", 6, 14); //$NON-NLS-1$

	/** The add comment <code>JButton</code>. */
	private JButton pbAdd = createButton("Add Comment", 'A');

	/** The cancel <code>JButton</code>. */
	private JButton pbCancel = createButton("Cancel", 'n');

	/** Set <code>true</code> iff the add comment button is pressed. */
	private boolean fieldPressedEnter = false;

	/** The part number. */
	private String fieldPN;

	/** The serial number. */
	private String fieldSN;

	/**
	 * The <code>MFSPartFunctionDisplayPanel</code> using this
	 * <code>MFSAddCommentDialog</code>.
	 */
	private MFSPartFunctionDisplayPanel fieldPanel;

	/**
	 * Constructs a new <code>MFSAddCommentDialog</code>.
	 * @param parent the <code>MFSFrame</code> that caused this
	 *        <code>MFSAddCommentDialog</code> to be displayed
	 * @param pn the part number
	 * @param sn the serial number
	 * @param panel the <code>MFSPartFunctionDisplayPanel</code> using this
	 *        <code>MFSAddCommentDialog</code>
	 */
	public MFSAddCommentDialog(MFSFrame parent, String pn, String sn,
								MFSPartFunctionDisplayPanel panel)
	{
		super(parent, "Add PN/SN Flag");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		this.fieldPN = pn;
		this.fieldSN = sn;
		this.fieldPanel = panel;

		createLayout();
		addMyListeners();
	}

	/** Adds this dialog's <code>Component</code>s to the layout. */
	private void createLayout()
	{
		JLabel partNameLabel = createSmallNameLabel("PN:");
		JLabel serialNameLabel = createSmallNameLabel("SN:");
		JLabel commentLabel = createSmallNameLabel("Comment:");

		this.vlPN = createSmallValueLabel(partNameLabel);
		this.vlPN.setText(this.fieldPN);
		this.vlSN = createSmallValueLabel(serialNameLabel);
		this.vlSN.setText(this.fieldSN);

		this.taComment.setLineWrap(true);

		JPanel contentPane = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(10, 10, 0, 5), 0, 0);

		contentPane.add(partNameLabel, gbc);

		gbc.gridy++;
		contentPane.add(serialNameLabel, gbc);

		gbc.gridy++;
		contentPane.add(commentLabel, gbc);

		gbc.gridy++;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.insets = new Insets(10, 10, 20, 5);
		contentPane.add(this.pbAdd, gbc);

		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(10, 0, 0, 10);
		contentPane.add(this.vlPN, gbc);

		gbc.gridy++;
		contentPane.add(this.vlSN, gbc);
		
		gbc.gridy++;
		contentPane.add(this.taComment, gbc);

		gbc.gridy++;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.insets = new Insets(10, 0, 20, 10);
		contentPane.add(this.pbCancel, gbc);

		setContentPane(contentPane);
		setButtonDimensions(this.pbAdd, this.pbCancel);
		commentLabel.setPreferredSize(new Dimension(this.taComment.getPreferredSize()));
		pack();
	}

	/** Adds the listeners to this dialog's <code>Component</code>s. */
	private void addMyListeners()
	{
		this.pbAdd.addActionListener(this);
		this.pbCancel.addActionListener(this);

		this.pbAdd.addKeyListener(this);
		this.pbCancel.addKeyListener(this);
		this.taComment.addKeyListener(this);
	}

	/** Executes the ADD_QDCMNT transaction. */
	public void addComment()
	{
		try
		{
			this.fieldPressedEnter = true;

			MfsXMLDocument xml_data1 = new MfsXMLDocument("ADD_QDCMNT"); //$NON-NLS-1$
			xml_data1.addOpenTag("DATA"); //$NON-NLS-1$
			xml_data1.addCompleteField("INPN", this.fieldPN.trim().toUpperCase()); //$NON-NLS-1$
			xml_data1.addCompleteField("INSQ", this.fieldSN.trim().toUpperCase()); //$NON-NLS-1$
			xml_data1.addCompleteField("CMMT", this.taComment.getText().trim().toUpperCase()); //$NON-NLS-1$
			xml_data1.addCompleteField("USER", MFSConfig.getInstance().get8CharUser()); //$NON-NLS-1$
			xml_data1.addCloseTag("DATA"); //$NON-NLS-1$
			xml_data1.finalizeXML();

			MFSTransaction add_qdcmnt = new MFSXmlTransaction(xml_data1.toString());
			add_qdcmnt.setActionMessage("Updating PN/SN Comment, Please Wait...");
			MFSComm.getInstance().execute(add_qdcmnt, this);
			this.fieldPanel.setDownloadedXML(xml_data1.toString());

			MfsXMLParser xmlParser = new MfsXMLParser(add_qdcmnt.getOutput());

			if (add_qdcmnt.getReturnCode() == 0)
			{
				this.pbAdd.requestFocusInWindow();
				this.dispose();
			}
			else
			{
				String erms = xmlParser.getField("ERMS");
				IGSMessageBox.showOkMB(this, null, erms, null);
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
	 * Returns <code>true</code> iff the add comment button was pressed.
	 * @return <code>true</code> iff the add comment button was pressed
	 */
	public boolean getPressedEnter()
	{
		return this.fieldPressedEnter;
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
			if (source == this.pbAdd)
			{
				addComment();
			}
			else if (source == this.pbCancel)
			{
				this.dispose();
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
			else if (source == this.pbAdd)
			{
				this.fieldPressedEnter = true;
				this.pbAdd.requestFocusInWindow();
				this.pbAdd.doClick();
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
