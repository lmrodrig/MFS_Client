/* @ Copyright IBM Corporation 2005, 2007. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2007-02-21      34242JR  R Prechel        -Java 5 version
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.dialog;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import mfsxml.MfsXMLParser;

import com.ibm.rchland.mfgapps.client.utils.messagebox.IGSMessageBox;
import com.ibm.rchland.mfgapps.mfsclient.MFSDialog;
import com.ibm.rchland.mfgapps.mfsclient.MFSFrame;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSConstants;

/**
 * <code>MFSErrorTableDialog</code> is the <code>MFSDialog</code> used to
 * display a list of part errors.
 * @author The MFS Client Development Team
 */
public class MFSErrorMsgListDialog
	extends MFSDialog
{
	private static final long serialVersionUID = 1L;
	/** The OK <code>JButton</code>. */
	private JButton pbOk = createButton("OK", 'O');

	/** The Part Detail <code>JButton</code>. */
	private JButton pbPartDetail = createButton("Part Detail", 'P');

	/** The <code>JList</code> that displays the errors. */
	private JList lstError = createList();

	/** The <code>JScrollPane</code> that contains the errors list. */
	private JScrollPane spError = new JScrollPane(this.lstError,
			ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
			ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

	/** The index of the PN/SN of the selected error. */
	private int selectedIndex;

	/** The PN/SN text of the selected error. */
	private String selectedPNSN;

	/** Set <code>true</code> if the part detail button is pressed. */
	private boolean pressedPartDetail;

	/**
	 * Constructs a new <code>MFSErrorTableDialog</code>.
	 * @param parent the <code>MFSFrame</code> that caused this
	 *        <code>MFSErrorMsgListDialog</code> to be displayed
	 */
	public MFSErrorMsgListDialog(MFSFrame parent)
	{
		super(parent, "Error Message");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		createLayout();
		addMyListeners();
	}

	/** Adds this dialog's <code>Component</code>s to the layout. */
	private void createLayout()
	{
		JLabel label = new JLabel("PART          SERIAL        MCTL      SEV  REASON");
		label.setFont(MFSConstants.MEDIUM_MONOSPACED_FONT);
		this.spError.setColumnHeaderView(label);

		JPanel contentPane = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 2, 1, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, 
				new Insets(20, 4, 0, 4), 0, 0);

		contentPane.add(this.spError, gbc);

		gbc.gridy++;
		gbc.gridwidth = 1;
		gbc.weighty = 0.0;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(20, 4, 20, 4);
		contentPane.add(this.pbOk, gbc);

		gbc.gridx = 1;
		contentPane.add(this.pbPartDetail, gbc);

		setContentPane(contentPane);
		setButtonDimensions(this.pbOk, this.pbPartDetail);
	}

	/** Adds the listeners to this dialog's <code>Component</code>s. */
	private void addMyListeners()
	{
		this.pbPartDetail.addActionListener(this);
		this.pbOk.addActionListener(this);
	}

	/**
	 * Loads the error list using the specified data.
	 * @param inputData the data used to load the error list
	 */
	public void loadList(String inputData)
	{
		try
		{
			DefaultListModel listModel = new DefaultListModel();
			this.lstError.setModel(listModel);

			int rc = 0;
			String errorString = ""; //$NON-NLS-1$
			String curRcd = ""; //$NON-NLS-1$
			String curPN = ""; //$NON-NLS-1$
			String curSN = ""; //$NON-NLS-1$
			String curMctl = ""; //$NON-NLS-1$

			MfsXMLParser xmlParser = new MfsXMLParser(inputData);

			try
			{
				curRcd = xmlParser.getField("RCD"); //$NON-NLS-1$
			}
			catch (mfsxml.MISSING_XML_TAG_EXCEPTION e)
			{
				rc = 10;
				errorString = "No Data Returned ? ";
			}
			while (rc == 0 && !curRcd.equals("")) //$NON-NLS-1$
			{
				MfsXMLParser xmlSubParser = new MfsXMLParser(curRcd);

				StringBuffer row = new StringBuffer();
				String tmpPN = xmlSubParser.getField("INPN"); //$NON-NLS-1$ 
				String tmpSN = xmlSubParser.getField("INSQ"); //$NON-NLS-1$
				String tmpMctl = xmlSubParser.getField("MCTL"); //$NON-NLS-1$
				if (curPN.equals(tmpPN) && curSN.equals(tmpSN) && curMctl.equals(tmpMctl))
				{
					row.append("            "); //$NON-NLS-1$ //PN
					row.append("  "); //$NON-NLS-1$
					row.append("            "); //$NON-NLS-1$ //SN
					row.append("  "); //$NON-NLS-1$
					row.append("        "); //$NON-NLS-1$ //MCTL
				}
				else
				{
					curPN = tmpPN;
					curSN = tmpSN;
					curMctl = tmpMctl;

					row.append(tmpPN);
					row.append("  "); //$NON-NLS-1$
					row.append(tmpSN);
					row.append("  "); //$NON-NLS-1$
					row.append(tmpMctl);
				}
				row.append("   "); //$NON-NLS-1$
				row.append(xmlSubParser.getField("WOHE")); //$NON-NLS-1$
				row.append("   "); //$NON-NLS-1$
				row.append(xmlSubParser.getField("ERMS")); //$NON-NLS-1$

				listModel.addElement(row.toString());

				curRcd = xmlParser.getNextField("RCD"); //$NON-NLS-1$
			}

			if (this.selectedIndex != -1)
			{
				this.lstError.setSelectedIndex(this.selectedIndex);
			}
			else
			{
				this.lstError.setSelectedIndex(0);
			}

			if (rc != 0)
			{
				IGSMessageBox.showOkMB(this, null, errorString, null);
			}
			Toolkit.getDefaultToolkit().beep();
			Dimension d = this.spError.getPreferredSize();
			int width = d.width;
			if (width > 700)
			{
				width = 700;
			}
			this.spError.setPreferredSize(new Dimension(width, d.height));
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(this, null, null, e);
		}
		finally
		{
			pack();
		}
	}

	/**
	 * Returns <code>true</code> iff the part detail button was pressed.
	 * @return <code>true</code> iff the part detail button was pressed
	 */
	public boolean getPressedPartDetail()
	{
		return this.pressedPartDetail;
	}

	/**
	 * Returns the value of the selected index.
	 * @return the value of the selected index
	 */
	public int getSelectedIndex()
	{
		return this.selectedIndex;
	}

	/**
	 * Returns the value of the selected PNSN.
	 * @return the value of the selected PNSN
	 */
	public String getSelectedPNSN()
	{
		return this.selectedPNSN;
	}

	/**
	 * Sets the selected index of the error list.
	 * @param index the index to select
	 */
	public void setListSelectedIndex(int index)
	{
		this.lstError.setSelectedIndex(index);
	}

	/**
	 * Invoked when an action occurs.
	 * @param ae the <code>ActionEvent</code>
	 */
	public void actionPerformed(ActionEvent ae)
	{
		final Object source = ae.getSource();
		if (source == this.pbOk)
		{
			this.dispose();
		}
		if (source == this.pbPartDetail)
		{
			/* start at hightlighted row and work up to find the PN/SN */
			for (int index = this.lstError.getSelectedIndex(); index >= 0; index--)
			{
				String row = this.lstError.getModel().getElementAt(index).toString();
				if (row.substring(0, 12).equals("            ") == false) //$NON-NLS-1$
				{
					this.selectedIndex = index;
					this.selectedPNSN = row;
					break;
				}
			}
			this.pressedPartDetail = true;
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
			this.lstError.requestFocusInWindow();
			this.spError.getHorizontalScrollBar().setValue(0);
		}
	}
}