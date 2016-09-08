/* © Copyright IBM Corporation 2005, 2007. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2007-02-21      34242JR  R Prechel        -Java 5 version
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.dialog;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import mfsxml.MfsXMLDocument;
import mfsxml.MfsXMLParser;

import com.ibm.rchland.mfgapps.client.utils.messagebox.IGSMessageBox;
import com.ibm.rchland.mfgapps.mfsclient.MFSActionableDialog;
import com.ibm.rchland.mfgapps.mfsclient.MFSFrame;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSConstants;
import com.ibm.rchland.mfgapps.mfscommon.MFSConfig;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSComm;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSTransaction;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSXmlTransaction;

/**
 * <code>MFSCoaEntryDialog</code> is the <code>MFSActionableDialog</code>
 * used to retrieve the alias serial number for a part.
 * @author The MFS Client Development Team
 */
public class MFSCoaEntryDialog
	extends MFSActionableDialog
{
	private static final long serialVersionUID = 1L;
	/** The input <code>JTextField</code>. */
	private JTextField tfInput = createTextField(MFSConstants.LARGE_TF_COLS, 0);

	/** The value <code>JLabel</code> for the scanned in Serial Number. */
	private JLabel vlSerialNum = null;

	/** The enter <code>JButton</code>. */
	private JButton pbEnter = createButton("Enter", 'E');

	/** The cancel <code>JButton</code>. */
	private JButton pbCancel = createButton("Cancel", 'n');

	/** Set <code>true</code> iff the enter button is pressed. */
	private boolean fieldPressedEnter = false;

	/** Stores the serial number passed into the constructor. */
	private String fieldInpn = ""; //$NON-NLS-1$

	/** Stores the alias serial number. */
	private String fieldAliasSN = ""; //$NON-NLS-1$

	/**
	 * Constructs a new <code>MFSCoaEntryDialog</code>.
	 * @param parent the <code>MFSFrame</code> that caused this
	 *        <code>MFSCoaEntryDialog</code> to be displayed
	 * @param inpn the part number
	 */
	public MFSCoaEntryDialog(MFSFrame parent, String inpn)
	{
		super(parent, "Scan in Serial Number");
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.fieldInpn = inpn;
		createLayout();
		addMyListeners();
	}

	/** Adds this dialog's <code>Component</code>s to the layout. */
	private void createLayout()
	{
		JLabel serialNumNL = createSmallNameLabel("Serial Number");
		this.vlSerialNum = createSmallValueLabel(serialNumNL);
		setLabelDimensions(this.vlSerialNum, 32);

		JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 30, 0));
		buttonPanel.add(this.pbEnter);
		buttonPanel.add(this.pbCancel);

		JPanel contentPaneCenter = new JPanel(new GridBagLayout());
		contentPaneCenter.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, 
				new Insets(4, 4, 4, 4), 0, 0);
		contentPaneCenter.add(this.tfInput, gbc);

		gbc.gridy = 1;
		gbc.weighty = 1.0;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.NONE;
		contentPaneCenter.add(serialNumNL, gbc);

		gbc.gridx = 1;
		contentPaneCenter.add(this.vlSerialNum, gbc);

		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridwidth = 2;
		gbc.insets = new Insets(20, 4, 20, 4);
		contentPaneCenter.add(buttonPanel, gbc);

		setContentPane(contentPaneCenter);
		pack();
	}

	/** Adds the listeners to this dialog's <code>Component</code>s. */
	private void addMyListeners()
	{
		this.pbCancel.addActionListener(this);
		this.pbEnter.addActionListener(this);
		this.pbCancel.addKeyListener(this);
		this.pbEnter.addKeyListener(this);
		this.tfInput.addKeyListener(this);
	}

	/**
	 * Returns the alias serial number.
	 * @return the alias serial number
	 */
	public String getAliasSN()
	{
		return this.fieldAliasSN;
	}

	/**
	 * Returns <code>true</code> iff the enter button was pressed.
	 * @return <code>true</code> iff the enter button was pressed
	 */
	public boolean getPressedEnter()
	{
		return this.fieldPressedEnter;
	}

	/** Invoked when the enter button is pressed. */
	private void enter()
	{
		if (!this.vlSerialNum.getText().trim().equals(""))
		{
			this.fieldPressedEnter = true;
			this.dispose();
		}
		else
		{
			IGSMessageBox.showOkMB(this, null, "Invalid Serial Number!", null);
			this.tfInput.requestFocusInWindow();
		}
	}

	/** Processes the input the user entered in the <code>JTextField</code>. */
	private void recvInput()
	{
		try
		{
			this.vlSerialNum.setText(this.tfInput.getText().toUpperCase().trim());
			this.tfInput.setText(""); //$NON-NLS-1$
			this.update(getGraphics());

			MfsXMLDocument xml_data1 = new MfsXMLDocument("RTVCOAALAS"); //$NON-NLS-1$
			xml_data1.addOpenTag("DATA"); //$NON-NLS-1$
			xml_data1.addCompleteField("INPN", this.fieldInpn); //$NON-NLS-1$
			xml_data1.addCompleteField("COAV", this.vlSerialNum.getText().trim().concat("                                ").substring(0, 32)); //$NON-NLS-1$ //$NON-NLS-2$										
			xml_data1.addCompleteField("USER", MFSConfig.getInstance().get8CharUser()); //$NON-NLS-1$
			xml_data1.addCompleteField("CELL", MFSConfig.getInstance().get8CharCell()); //$NON-NLS-1$
			xml_data1.addCloseTag("DATA"); //$NON-NLS-1$
			xml_data1.finalizeXML();

			MFSTransaction rtvcoaalas = new MFSXmlTransaction(xml_data1.toString());
			rtvcoaalas.setActionMessage("Retrieving Alias, Please Wait...");
			MFSComm.getInstance().execute(rtvcoaalas, this);

			if (rtvcoaalas.getReturnCode() == 0)
			{
				MfsXMLParser xmlParser = new MfsXMLParser(rtvcoaalas.getOutput());
				this.fieldAliasSN = xmlParser.getField("ALIAS"); //$NON-NLS-1$
				this.pbEnter.requestFocusInWindow();
				this.pbEnter.doClick();
			}
			else
			{
				IGSMessageBox.showOkMB(this, null, rtvcoaalas.getErms(), null);
				this.vlSerialNum.setText(""); //$NON-NLS-1$
				this.toFront();
				this.tfInput.requestFocusInWindow();
			}
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(this, null, null, e);
		}
	}

	/**
	 * Invoked when an action occurs.
	 * @param ae the <code>ActionEvent</code>
	 */
	public void actionPerformed(ActionEvent ae)
	{
		final Object source = ae.getSource();
		if (source == this.pbCancel)
		{
			this.dispose();
		}
		else if (source == this.pbEnter)
		{
			enter();
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
			if (source == this.tfInput)
			{
				this.recvInput();
			}
			else if (source == this.pbCancel)
			{
				this.pbCancel.requestFocusInWindow();
				this.pbCancel.doClick();
			}
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
