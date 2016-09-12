/* @ Copyright IBM Corporation 2006, 2010. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2007-08-27      38007SE  R Prechel        -Rewrite to extend MFSMacIDDialogBase and
 *                                            only handle PNRI values of T or V
 * 2010-03-08   ~1 47596MZ  D Kloepping      -Automate done selection for Efficiency client
 * 											   when all MAC addresses collected                                           
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.dialog;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;

import mfsxml.MfsXMLDocument;

import com.ibm.rchland.mfgapps.client.utils.messagebox.IGSMessageBox;
import com.ibm.rchland.mfgapps.mfsclient.MFSFrame;

/**
 * <code>MFSMacIDDialog</code> is the <code>MFSMacIDDialogBase</code>
 * subclass used to prompt a user to enter two MAC Addresses that represent a
 * range of MAC Addresses for a part. An <code>MFSMacIDDialog</code> can
 * optionally display the part and sequence number for the part and require that
 * the user enter the part and sequence number in addition to the MAC Addresses.
 * @author The MFS Client Development Team
 */
public class MFSMacIDDialog
	extends MFSMacIDDialogBase
{
	private static final long serialVersionUID = 1L;
	/** The value <code>JLabel</code> for the scanned in low MAC ID. */
	private JLabel vlMacLow;

	/** The value <code>JLabel</code> for the scanned in high MAC ID. */
	private JLabel vlMacHigh;

	/**
	 * Constructs a new <code>MFSMacIDDialog</code>.
	 * @param parent the <code>MFSFrame</code> that caused this
	 *        <code>MFSMacIDDialog</code> to be displayed
	 * @param pnri the value of the PNRI field
	 * @param pn the part number
	 * @param sn the sequence number
	 * @param csni the value of the CSNI field
	 */
	public MFSMacIDDialog(MFSFrame parent, String pnri, String pn, String sn, String csni)
	{
		super(parent, pnri, pn, sn, csni);
		createLayout();
		addMyListeners();
	}

	/** Adds this dialog's <code>Component</code>s to the layout. */
	private void createLayout()
	{
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1, 1,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, 
				new Insets(10, 15, 10, 15), 0, 0);
		JPanel contentPaneCenter = new JPanel(new GridBagLayout());
		JPanel labelPanel = new JPanel(new GridLayout(0, 2, 10, 5));
		JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 5, 0));
		this.setContentPane(contentPaneCenter);

		if (this.fieldRequiresPNSN)
		{
			StringBuffer buffer = new StringBuffer(32);
			buffer.append("PN=");
			buffer.append(this.fieldPn);
			buffer.append("  SN=");
			buffer.append(this.fieldSn);

			gbc.insets = new Insets(10, 15, 3, 15);
			contentPaneCenter.add(createLabel("MAC Address missing for"), gbc);
			gbc.gridy++;
			gbc.insets = new Insets(0, 15, 10, 15);
			contentPaneCenter.add(createLabel(buffer.toString()), gbc);
			gbc.gridy++;
		}

		contentPaneCenter.add(this.tfInput, gbc);
		gbc.gridy++;

		if (this.fieldRequiresPNSN)
		{
			this.vlPN = addLabels(labelPanel, "Part Number");
			this.vlSN = addLabels(labelPanel, "Sequence Number");
		}

		this.vlMacLow = addLabels(labelPanel, "MAC Address 1");
		this.vlMacHigh = addLabels(labelPanel, "MAC Address 2");

		contentPaneCenter.add(labelPanel, gbc);
		gbc.gridy++;

		buttonPanel.add(this.pbDone);
		buttonPanel.add(this.pbCancel);
		contentPaneCenter.add(buttonPanel, gbc);
		setLabelDimensions(this.vlMacLow, 12);
		this.pack();
	}

	/** Adds the listeners to this dialog's <code>Component</code>s. */
	private void addMyListeners()
	{
		this.pbDone.addActionListener(this);
		this.pbCancel.addActionListener(this);
		this.pbDone.addKeyListener(this);
		this.pbCancel.addKeyListener(this);
		this.tfInput.addKeyListener(this);
	}

	/** {@inheritDoc} */
	public void addMacXmlToDocument(MfsXMLDocument xmlDocument)
	{
		xmlDocument.addOpenTag("ROW"); //$NON-NLS-1$
		xmlDocument.addCompleteField("MACF", this.vlMacLow.getText()); //$NON-NLS-1$
		xmlDocument.addCompleteField("MACL", this.vlMacHigh.getText()); //$NON-NLS-1$
		xmlDocument.addCloseTag("ROW"); //$NON-NLS-1$
	}

	/** {@inheritDoc} */
	protected void handleDoneButton()
	{
		if (this.vlMacLow.getText().length() == 0
				|| this.vlMacHigh.getText().length() == 0)
		{
			String title = "Missing MAC Address";
			String erms = "Please enter another MAC Address.";
			IGSMessageBox.showOkMB(this, title, erms, null);
		}
		else if (this.fieldRequiresPNSN && this.vlPN.getText().length() == 0)
		{
			String title = "Missing Part Number";
			String erms = "Please enter the Part Number.";
			IGSMessageBox.showOkMB(this, title, erms, null);
		}
		else if (this.fieldRequiresPNSN && this.vlSN.getText().length() == 0)
		{
			String title = "Missing Sequence Number";
			String erms = "Please enter the Sequence Number.";
			IGSMessageBox.showOkMB(this, title, erms, null);
		}
		else if (this.vlMacLow.getText().equals(this.vlMacHigh.getText()))
		{
			String title = "Invalid MAC Addresses";
			String erms = "Both MAC Addresses are the same.";
			IGSMessageBox.showOkMB(this, title, erms, null);
		}
		else
		{
			this.fieldDoneSelected = true;
			dispose();
			return;
		}

		this.toFront();
		this.tfInput.requestFocusInWindow();
	}

	/** {@inheritDoc} */
	protected void processMacInput(String macIDInput)
	{
		if (this.vlMacLow.getText().length() == 0)
		{
			this.vlMacLow.setText(macIDInput);
		}
		else
		{
			try
			{
				long firstMac = Long.parseLong(this.vlMacLow.getText(), 16);
				long inputMac = Long.parseLong(macIDInput, 16);
				if (firstMac > inputMac)
				{
					this.vlMacHigh.setText(this.vlMacLow.getText());
					this.vlMacLow.setText(macIDInput);
				}
				else
				{
					this.vlMacHigh.setText(macIDInput);
				}
			}
			catch (NumberFormatException nfe)
			{
				IGSMessageBox.showOkMB(this, null, null, nfe);
			}
		}
	}

	/**
	 * Invoked when an action occurs.
	 * @param ae the <code>ActionEvent</code>
	 */
	public void actionPerformed(ActionEvent ae)
	{
		final Object source = ae.getSource();
		if (source == this.pbDone)
		{
			this.handleDoneButton();
		}
		else if (source == this.pbCancel)
		{
			this.fieldDoneSelected = false;
			this.dispose();
		}
	}

	@Override
	//~1a
	protected void checkForMacCollectionCompletion() {
		if (this.vlMacLow.getText().length() != 0
				&& this.vlMacHigh.getText().length() != 0)
		{
			handleDoneButton();
		}
		
	}
}
