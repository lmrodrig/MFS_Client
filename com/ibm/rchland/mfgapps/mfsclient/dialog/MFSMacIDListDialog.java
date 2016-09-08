/* © Copyright IBM Corporation 2007,2016. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2007-08-27      38007SE  R Prechel        -Initial version
 * 2007-09-06   ~1 39786JM  R Prechel        -Validate MACQ
 * 2010-03-08   ~2 47596MZ  D Kloepping      -Automate done selection for Efficiency client
 * 											   when all MAC addresses collected
 * 2016-02-18   ~3 1471226  Miguel Rivas     -getSelectedValues deprecated
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.dialog;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.Enumeration;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;

import mfsxml.MfsXMLDocument;

import com.ibm.rchland.mfgapps.client.utils.messagebox.IGSMessageBox;
import com.ibm.rchland.mfgapps.mfsclient.MFSFrame;

/**
 * <code>MFSMacIDListDialog</code> is the <code>MFSMacIDDialogBase</code>
 * subclass used to prompt a user to enter a list of MAC Addresses for a part.
 * An <code>MFSMacIDListDialog</code> can optionally display the part and
 * sequence number for the part and require that the user enter the part and
 * sequence number in addition to the MAC Addresses.
 * @author The MFS Print Server Development Team
 */
public class MFSMacIDListDialog
	extends MFSMacIDDialogBase
{
	private static final long serialVersionUID = 1L;
	/** The unknown MACQ (MAC Quantity) value. */
	public static final String UNKNOWN_MACQ = "**"; //$NON-NLS-1$

	/** The <code>JList</code> that displays the scanned in MAC IDs. */
	private JList lstMacID = createList();

	/** The <code>JScrollPane</code> that contains the list of MAC IDs. */
	private JScrollPane spMacID = new JScrollPane(this.lstMacID,
			ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
			ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

	/** The <code>DefaultListModel</code> for {@link #lstMacID}. */
	private DefaultListModel fieldListModel = new DefaultListModel();

	/** The delete <code>JButton</code>. */
	private JButton pbDelete = createButton("Delete", 'e');

	/** The MACQ (MAC Quantity) field value. */
	private String fieldMacq;

	/** The MACQ (MAC Quantity) field value as an <code>int</code>. */
	private int fieldMacqInt;

	/**
	 * Constructs a new <code>MFSMacIDListDialog</code>.
	 * @param parent the <code>MFSFrame</code> that caused this
	 *        <code>MFSMacIDListDialog</code> to be displayed
	 * @param macq the value of the MACQ field
	 * @param pnri the value of the PNRI field
	 * @param pn the part number
	 * @param sn the sequence number
	 * @param csni the value of the CSNI field
	 * @throws IllegalArgumentException if <code>macq</code> is invalid
	 */
	public MFSMacIDListDialog(MFSFrame parent, String macq, String pnri, String pn,
								String sn, String csni)
		throws IllegalArgumentException
	{
		super(parent, pnri, pn, sn, csni);
		this.fieldMacq = macq;

		//~1C If macq is not ** or 01-99, throw IllegalArgumentException
		//(or NumberFormatException which is an IllegalArgumentException)
		if (UNKNOWN_MACQ.equals(macq))
		{
			this.fieldMacqInt = 99;
		}
		else
		{
			this.fieldMacqInt = Integer.parseInt(macq);
		}

		if (this.fieldMacqInt <= 0)
		{
			throw new IllegalArgumentException("MACQ: " + macq); //$NON-NLS-1$
		}

		//A MAC Address is 12 characters. The list uses a monospaced font.
		//Thus, setting the size using 14 M's yields 2 characters of padding.
		this.lstMacID.setPrototypeCellValue("MMMMMMMMMMMMMM"); //$NON-NLS-1$
		this.lstMacID.setModel(this.fieldListModel);
		this.lstMacID.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		createLayout();
		addMyListeners();
	}

	/**
	 * Creates the <code>JPanel</code> for the left side of the
	 * <code>MFSMacIDListDialog</code>.
	 * @return the <code>JPanel</code>
	 */
	private JPanel createLeftPanel()
	{
		JPanel result = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1, 0,
				GridBagConstraints.NORTH, GridBagConstraints.NONE,
				new Insets(0, 0, 3, 0), 0, 0);

		StringBuffer scanLabelText = new StringBuffer();
		if (UNKNOWN_MACQ.equals(this.fieldMacq))
		{
			scanLabelText.append("Scan in MAC Addresses");
		}
		else if (this.fieldMacqInt == 1)
		{
			scanLabelText.append("Scan in 1 MAC Address");
		}
		else
		{
			scanLabelText.append("Scan in ");
			scanLabelText.append(this.fieldMacqInt);
			scanLabelText.append(" MAC Addresses");
		}

		if (this.fieldRequiresPNSN)
		{
			result.add(createLabel(scanLabelText.append(',').toString()), gbc);

			gbc.gridy++;
			result.add(createLabel("Part Number, and Sequence Number"), gbc);
		}
		else
		{
			result.add(createLabel(scanLabelText.toString()), gbc);
		}

		gbc.gridy++;
		gbc.weighty = 1;
		gbc.insets.bottom = 15;
		result.add(this.tfInput, gbc);

		if (this.fieldRequiresPNSN)
		{
			JPanel labelPanel = new JPanel(new GridLayout(2, 2, 10, 5));
			this.vlPN = addLabels(labelPanel, "Part Number");
			this.vlSN = addLabels(labelPanel, "Sequence Number");

			gbc.gridy++;
			gbc.anchor = GridBagConstraints.CENTER;
			result.add(labelPanel, gbc);
		}

		JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 5, 0));
		buttonPanel.add(this.pbDone);
		buttonPanel.add(this.pbDelete);
		buttonPanel.add(this.pbCancel);

		gbc.gridy++;
		gbc.anchor = GridBagConstraints.SOUTH;
		gbc.insets.bottom = 0;
		result.add(buttonPanel, gbc);

		return result;
	}

	/** Adds this dialog's <code>Component</code>s to the layout. */
	private void createLayout()
	{
		JPanel contentPaneCenter = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1, 1,
				GridBagConstraints.CENTER, GridBagConstraints.VERTICAL,
				new Insets(10, 15, 10, 15), 0, 0);

		if (this.fieldRequiresPNSN)
		{
			StringBuffer buffer = new StringBuffer(64);
			buffer.append("MAC Address missing for PN=");
			buffer.append(this.fieldPn);
			buffer.append("  SN=");
			buffer.append(this.fieldSn);

			gbc.gridwidth = 2;
			contentPaneCenter.add(createLabel(buffer.toString()), gbc);
			gbc.gridy++;
			gbc.gridwidth = 1;
		}

		contentPaneCenter.add(createLeftPanel(), gbc);

		gbc.gridx = 1;
		contentPaneCenter.add(this.spMacID, gbc);

		setContentPane(contentPaneCenter);
		pack();
	}

	/** Adds the listeners to this dialog's <code>Component</code>s. */
	private void addMyListeners()
	{
		this.pbDone.addActionListener(this);
		this.pbDelete.addActionListener(this);
		this.pbCancel.addActionListener(this);
		this.pbDone.addKeyListener(this);
		this.pbDelete.addKeyListener(this);
		this.pbCancel.addKeyListener(this);
		this.tfInput.addKeyListener(this);
	}

	/** {@inheritDoc} */
	@SuppressWarnings("rawtypes")
	public void addMacXmlToDocument(MfsXMLDocument xmlDocument)
	{
		Enumeration elements = this.fieldListModel.elements();
		while (elements.hasMoreElements())
		{
			String mac = elements.nextElement().toString();
			xmlDocument.addOpenTag("ROW"); //$NON-NLS-1$
			xmlDocument.addCompleteField("MACF", mac); //$NON-NLS-1$
			xmlDocument.addCloseTag("ROW"); //$NON-NLS-1$
		}
	}

	/** {@inheritDoc} */
	protected void handleDoneButton()
	{
		final int macCollected = this.fieldListModel.size();
		if (UNKNOWN_MACQ.equals(this.fieldMacq) == false
				&& macCollected != this.fieldMacqInt)
		{
			int remaining = this.fieldMacqInt - macCollected;
			String title = "Missing MAC Address";
			String erms;
			if (remaining == 1)
			{
				erms = "Please enter 1 more MAC Address.";
			}
			else
			{
				erms = "Please enter " + remaining + " more MAC Addresses.";
			}
			IGSMessageBox.showOkMB(this, title, erms, null);
		}
		else if (UNKNOWN_MACQ.equals(this.fieldMacq) && macCollected == 0)
		{
			String title = "Missing MAC Address";
			String erms = "Please enter at least 1 MAC Address.";
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
		if (this.fieldListModel.contains(macIDInput))
		{
			String title = "Invalid MAC Address";
			StringBuffer erms = new StringBuffer();
			erms.append("MAC Address ");
			erms.append(macIDInput);
			erms.append(" was already collected.");
			IGSMessageBox.showOkMB(this, title, erms.toString(), null);
		}
		else if (this.fieldListModel.size() == this.fieldMacqInt)
		{
			String title = "Invalid Input";
			String erms = "All MAC Addresses have been collected.";
			IGSMessageBox.showOkMB(this, title, erms, null);
		}
		else
		{
			this.fieldListModel.addElement(macIDInput);
		}
	}

	/** {@inheritDoc}
	 * class to be used in MFSMacIDDialogBase to automatically issue the Done()
	 * for the mac collection dialog when all mac adresses that are called out
	 * are collected 
	 * ~2a */
	protected void checkForMacCollectionCompletion()
	{
		final int macCollected = this.fieldListModel.size();
		if (macCollected == this.fieldMacqInt)
		{
			handleDoneButton();
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
			handleDoneButton();
		}
		else if (source == this.pbDelete)
		{
			/* ~3C
			Object selected[] = this.lstMacID.getSelectedValues();
			for (int i = 0; i < selected.length; i++)
			{
				this.fieldListModel.removeElement(selected[i]);
			}
			*/
			List selected = this.lstMacID.getSelectedValuesList();
			for (int i = 0; i < selected.size(); i++)
			{
				this.fieldListModel.removeElement(selected.get(i));
			}
		}
		else if (source == this.pbCancel)
		{
			this.fieldDoneSelected = false;
			this.dispose();
		}
	}
}
