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

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

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
 * <code>MFSIncPlugCount</code> is the <code>MFSActionableDialog</code> used
 * to set a part's plug count.
 * @author The MFS Client Development Team
 */
public class MFSIncPlugCount
	extends MFSActionableDialog
{
	private static final long serialVersionUID = 1L;
	/** The input <code>JTextField</code>. */
	private JTextField tfInput = null;

	/** The Change <code>JButton</code>. */
	private JButton pbChange = createButton("Change", 'C');

	/** The Cancel <code>JButton</code>. */
	private JButton pbCancel = createButton("Cancel", 'n');
	
	/** The part number. */
	private String fieldPN;

	/** The serial number. */
	private String fieldSN;
	
	/** Set <code>true</code> iff the plug count is changed. */
	private boolean fieldMakeChange = false;

	/**
	 * Constructs a new <code>MFSIncPlugCount</code>.
	 * @param parent the <code>MFSFrame</code> that caused this
	 *        <code>MFSIncPlugCount</code> to be displayed
	 * @param pn the part number
	 * @param sn the serial number
	 */
	public MFSIncPlugCount(MFSFrame parent, String pn, String sn)
	{
		super(parent, "Plug Count Change");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		this.fieldPN = pn;
		this.fieldSN = sn;
		
		createLayout();
		addMyListeners();
	}

	/** Adds this dialog's <code>Component</code>s to the layout. */
	private void createLayout()
	{
		JLabel label = createNameLabel("Enter plug value to be added:");
		
		this.tfInput = createTextField(MFSConstants.SMALL_TF_COLS, 2, label);
		
		JPanel top = new JPanel(new FlowLayout());
		top.add(label);
		top.add(this.tfInput);
		
		JPanel contentPane = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 2, 1, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.NONE,
				new Insets(20, 20, 20, 20), 0, 0);

		contentPane.add(top, gbc);
		
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		contentPane.add(this.pbChange, gbc);
		
		gbc.gridx = 1;
		contentPane.add(this.pbCancel, gbc);

		setContentPane(contentPane);
		setButtonDimensions(this.pbChange, this.pbCancel);
		pack();
	}

	/** Adds the listeners to this dialog's <code>Component</code>s. */
	private void addMyListeners()
	{
		this.pbChange.addActionListener(this);
		this.pbCancel.addActionListener(this);

		this.pbChange.addKeyListener(this);
		this.pbCancel.addKeyListener(this);
		this.tfInput.addKeyListener(this);
	}
	
	/**
	 * Returns <code>true</code> iff the plug count was changed.
	 * @return <code>true</code> iff the plug count was changed
	 */
	public boolean isMakeChange() 
	{
		return this.fieldMakeChange;
	}
	
	/** Processes the input the user entered in the <code>JTextField</code>. */
	public void recvInput()
	{
		try
		{
			try
			{
				//Check to make sure it's an INT
				Integer.parseInt(this.tfInput.getText().toUpperCase());
				String cpci = (this.tfInput.getText().toUpperCase());
				
				MfsXMLDocument xml_data1 = new MfsXMLDocument("UPDT_PLUG"); //$NON-NLS-1$
				xml_data1.addOpenTag("DATA"); //$NON-NLS-1$
				xml_data1.addCompleteField("INPN", this.fieldPN); //$NON-NLS-1$
				xml_data1.addCompleteField("INSQ", this.fieldSN); //$NON-NLS-1$
				xml_data1.addCompleteField("USER", MFSConfig.getInstance().get8CharUser()); //$NON-NLS-1$
				xml_data1.addCompleteField("CPCI", cpci); //$NON-NLS-1$
				xml_data1.addCloseTag("DATA"); //$NON-NLS-1$
				xml_data1.finalizeXML();
				
				MFSTransaction updt_plug = new MFSXmlTransaction(xml_data1.toString());
				updt_plug.setActionMessage("Updating Plug Count, Please Wait...");
				MFSComm.getInstance().execute(updt_plug, this);

				if (updt_plug.getReturnCode() == 0)
				{
					this.fieldMakeChange = true;
					dispose(); //closes the Change Plug Diaglog
				}
				else
				{
					IGSMessageBox.showOkMB(this, null, updt_plug.getErms(), null);
				}
			}
			catch (NumberFormatException n)
			{
				String erms = "Invalid plug count value.";
				IGSMessageBox.showOkMB(this, null, erms, null);
				this.toFront();
				this.tfInput.setText(""); //$NON-NLS-1$
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
		try
		{
			final Object source = ae.getSource();
			if (source == this.pbCancel)
			{
				this.dispose();
			}
			else if (source == this.pbChange)
			{
				recvInput();
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
			if (source == this.tfInput)
			{
				recvInput();
			}
			else if (source == this.pbCancel)
			{
				this.pbCancel.requestFocusInWindow();
				this.pbCancel.doClick();
			}
			else if (source == this.pbChange)
			{
				this.pbChange.requestFocusInWindow();
				this.pbChange.doClick();
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
	 * the {@link #tfInput}.
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
