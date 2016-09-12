/* @ Copyright IBM Corporation 2005, 2007. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2007-02-22      34242JR  R Prechel        -Java 5 version
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.dialog;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import mfsxml.MfsXMLParser;

import com.ibm.rchland.mfgapps.client.utils.messagebox.IGSMessageBox;
import com.ibm.rchland.mfgapps.mfsclient.MFSDialog;
import com.ibm.rchland.mfgapps.mfsclient.MFSFrame;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSConstants;

/**
 * <code>MFSWorkUnitLocDisplayDialog</code> is the <code>MFSDialog</code>
 * used to display work unit locations.
 * @author The MFS Client Development Team
 */
public class MFSWorkUnitLocDisplayDialog
	extends MFSDialog
{
	private static final long serialVersionUID = 1L;
	/** The OK <code>JButton</code>. */
	private JButton pbOk = createButton("OK", 'O');

	/** The <code>JList</code> that displays the work unit locations. */
	private JList lstWorkUnitLoc = new JList();

	/** The <code>JScrollPane</code> that contains the list of work unit locations. */
	private JScrollPane spWorkUnitLoc = new JScrollPane(this.lstWorkUnitLoc);

	/**
	 * Constructs a new <code>MFSWorkUnitLocDisplayDialog</code>.
	 * @param parent the <code>MFSFrame</code> that caused this
	 *        <code>MFSWorkUnitLocDisplayDialog</code> to be displayed
	 */
	public MFSWorkUnitLocDisplayDialog(MFSFrame parent)
	{
		super(parent, "Work Unit Locations");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		createLayout();
		addMyListeners();
	}

	/** Adds this dialog's <code>Component</code>s to the layout. */
	private void createLayout()
	{
		JLabel label = new JLabel("MCTL       LOC    AREA");
		label.setFont(MFSConstants.MEDIUM_MONOSPACED_FONT);

		this.lstWorkUnitLoc.setFont(MFSConstants.MEDIUM_MONOSPACED_FONT);
		this.lstWorkUnitLoc.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.lstWorkUnitLoc.setPrototypeCellValue("MCTLWWWW   LOCW   AREAWWWW    "); //$NON-NLS-1$

		JPanel contentPane = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, 
				new Insets(10, 4, 0, 4), 0, 0);

		contentPane.add(label, gbc);

		gbc.gridy = 1;
		gbc.weighty = 1.0;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(0, 4, 20, 4);
		contentPane.add(this.spWorkUnitLoc, gbc);

		gbc.gridy = 2;
		gbc.weighty = 0.0;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(0, 4, 5, 4);
		contentPane.add(this.pbOk, gbc);

		this.setContentPane(contentPane);
	}

	/** Adds the listeners to this dialog's <code>Component</code>s. */
	private void addMyListeners()
	{
		this.pbOk.addActionListener(this);
	}

	/**
	 * Loads the work unit location list using the specified data.
	 * @param inputData the data used to load the list
	 * @return <code>true</code> on success; <code>false</code> on failure
	 */
	public boolean loadList(String inputData)
	{
		boolean result = true;
		try
		{
			DefaultListModel listModel = new DefaultListModel();
			this.lstWorkUnitLoc.setModel(listModel);

			String curRcd = ""; //$NON-NLS-1$

			MfsXMLParser xmlParser = new MfsXMLParser(inputData);

			try
			{
				curRcd = xmlParser.getField("REC"); //$NON-NLS-1$
			}
			catch (mfsxml.MISSING_XML_TAG_EXCEPTION e)
			{
				result = false;
			}
			while (!curRcd.equals("")) //$NON-NLS-1$
			{
				MfsXMLParser xmlSubParser = new MfsXMLParser(curRcd);
				StringBuffer row = new StringBuffer();
				row.append(xmlSubParser.getField("MCTL")); //$NON-NLS-1$
				row.append("   "); //$NON-NLS-1$
				row.append(xmlSubParser.getField("WLOC")); //$NON-NLS-1$
				row.append("   "); //$NON-NLS-1$
				row.append(xmlSubParser.getField("CTYP")); //$NON-NLS-1$
				listModel.addElement(row.toString());
				curRcd = xmlParser.getNextField("REC"); //$NON-NLS-1$
			}
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(this, null, null, e);
			result = false;
		}
		finally
		{
			pack();
		}
		return result;
	}

	/**
	 * Invoked when an action occurs.
	 * @param ae the <code>ActionEvent</code>
	 */
	public void actionPerformed(ActionEvent ae)
	{
		if (ae.getSource() == this.pbOk)
		{
			this.dispose();
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
			this.lstWorkUnitLoc.setSelectedIndex(0);
		}
	}
}
