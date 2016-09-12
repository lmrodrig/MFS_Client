/* @ Copyright IBM Corporation 2003, 2007. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2007-01-28      34242JR  R Prechel        -Java 5 version
 * 2007-04-02   ~1 38166JM  R Prechel        -Add setLocationRelativeTo for dialog
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.dialog;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;

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
 * <code>MFSDe10Dialog</code> is an <code>MFSActionableDialog</code> used to
 * display and work with FCSPDE10 entries.
 * @author The MFS Client Development Team
 */
public class MFSDe10Dialog
	extends MFSActionableDialog
{
	private static final long serialVersionUID = 1L;
	/** The Search input <code>JTextField</code>. */
	private JTextField tfSearch;

	/** The <code>JList</code> of DE10 records. */
	private JList lstDE10 = new JList();

	/** The <code>JScrollPane</code> that contains the list of DE10 records. */
	private JScrollPane spDE10 = new JScrollPane(this.lstDE10,
			ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
			ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

	/** The Add <code>JButton</code>. */
	private JButton pbAdd = createButton("Add", 'A');

	/** The Accept <code>JButton</code>. */
	private JButton pbAccept = createButton("Accept", 'c');

	/** The Reject <code>JButton</code>. */
	private JButton pbReject = createButton("Reject", 'R');

	/** The Refresh <code>JButton</code>. */
	private JButton pbRefresh = createButton("Refresh", 'f');

	/** The Exit <code>JButton</code>. */
	private JButton pbExit = createButton("Exit", 'x');

	/**
	 * Constructs a new <code>MFSDe10Dialog</code>.
	 * @param parent the <code>MFSFrame</code> that caused this
	 *        <code>MFSDe10Dialog</code> to be displayed
	 */
	public MFSDe10Dialog(MFSFrame parent)
	{
		super(parent, "Work With FCSPDE10 Entries");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.lstDE10.setModel(new DefaultListModel());
		createLayout();
		addMyListeners();
	}

	/** Adds this dialog's <code>Component</code>s to the layout. */
	private void createLayout()
	{
		JLabel searchLabel = createLabel("SEARCH:", MFSConstants.SMALL_DIALOG_FONT);
		this.tfSearch = createTextField(MFSConstants.SMALL_TF_COLS, 0, searchLabel);

		JLabel headingLabel = new JLabel(
				"MFGN     IDSS    MCTL       ORNO    STAT    Rec Date    Rec Time    Date          Time       USER");
		headingLabel.setFont(MFSConstants.SMALL_MONOSPACED_FONT);

		this.lstDE10.setFont(new Font("Monospaced", Font.PLAIN, 12)); //$NON-NLS-1$
		this.lstDE10.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.lstDE10.setVisibleRowCount(12);

		JPanel buttonPanel = new JPanel(new GridLayout(1, 5, 30, 0));
		buttonPanel.add(this.pbAdd);
		buttonPanel.add(this.pbAccept);
		buttonPanel.add(this.pbReject);
		buttonPanel.add(this.pbRefresh);
		buttonPanel.add(this.pbExit);

		JPanel contentPane = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, 
				new Insets(20, 10, 4, 4), 0, 0);

		contentPane.add(searchLabel, gbc);

		gbc.gridx = 1;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(20, 4, 4, 4);
		contentPane.add(this.tfSearch, gbc);

		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(4, 15, 4, 10);
		contentPane.add(headingLabel, gbc);

		gbc.gridy = 2;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(4, 10, 0, 10);
		contentPane.add(this.spDE10, gbc);

		gbc.gridy = 3;
		gbc.weighty = 0;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(10, 10, 20, 10);
		contentPane.add(buttonPanel, gbc);

		setContentPane(contentPane);
		pack();
	}

	/** Adds the listeners to this dialog's <code>Component</code>s. */
	private void addMyListeners()
	{
		this.pbAdd.addActionListener(this);
		this.pbAccept.addActionListener(this);
		this.pbReject.addActionListener(this);
		this.pbRefresh.addActionListener(this);
		this.pbExit.addActionListener(this);

		this.tfSearch.addKeyListener(this);
		this.pbAdd.addKeyListener(this);
		this.pbAccept.addKeyListener(this);
		this.pbReject.addKeyListener(this);
		this.pbRefresh.addKeyListener(this);
		this.pbExit.addKeyListener(this);
	}

	/** Removes the listeners from this dialog's <code>Component</code>s. */
	private void removeMyListeners()
	{
		this.pbAdd.removeActionListener(this);
		this.pbAccept.removeActionListener(this);
		this.pbReject.removeActionListener(this);
		this.pbRefresh.removeActionListener(this);
		this.pbExit.removeActionListener(this);

		this.tfSearch.removeKeyListener(this);
		this.pbAdd.removeKeyListener(this);
		this.pbAccept.removeKeyListener(this);
		this.pbReject.removeKeyListener(this);
		this.pbRefresh.removeKeyListener(this);
		this.pbExit.removeKeyListener(this);
	}

	/** Invoked when {@link #pbAccept} is selected. */
	private void acceptRecord()
	{
		try
		{
			int rc = 0;
			removeMyListeners();

			if (this.lstDE10.getSelectedIndex() == -1)
			{
				String erms = "Please Select a Record to Accept";
				IGSMessageBox.showOkMB(this, null, erms, null);
				this.toFront();
				this.tfSearch.setText(""); //$NON-NLS-1$
				this.tfSearch.requestFocusInWindow();
			}
			else
			{
				DefaultListModel orderListModel = (DefaultListModel) this.lstDE10.getModel();
				String activeRow = ((String) orderListModel.getElementAt(this.lstDE10.getSelectedIndex()));

				if (activeRow.substring(38, 39).equals("A")) //$NON-NLS-1$
				{
					String erms = "Invalid Record to Accept - Status = " + activeRow.substring(38, 39);
					IGSMessageBox.showOkMB(this, null, erms, null);
					this.toFront();
					this.tfSearch.setText(""); //$NON-NLS-1$
					this.tfSearch.requestFocusInWindow();
				}
				else
				{
					/* Logic to display return types - 28462CT */
					MfsXMLDocument xml_data1 = new MfsXMLDocument("RTVRETTYPE"); //$NON-NLS-1$
					xml_data1.addOpenTag("DATA"); //$NON-NLS-1$
					xml_data1.addCompleteField("MCTL", activeRow.substring(17, 25)); //$NON-NLS-1$
					xml_data1.addCloseTag("DATA"); //$NON-NLS-1$
					xml_data1.finalizeXML();

					MFSTransaction rtvrettype = new MFSXmlTransaction(xml_data1.toString());
					rtvrettype.setActionMessage("Building Return Type List., Please Wait...");
					MFSComm.getInstance().execute(rtvrettype, this);
					rc = rtvrettype.getReturnCode();

					/* Verify the call to the RTVRETTYPE worked correctly */
					if (rc == 0)
					{
						/* Get the Type List and the Type of the MCTL */
						MfsXMLParser xmlParser = new MfsXMLParser(rtvrettype.getOutput());
						String typeList = xmlParser.getField("LIST"); //$NON-NLS-1$
						String type = xmlParser.getField("TYPE"); //$NON-NLS-1$

						MFSGenericListDialog mytypeList = new MFSGenericListDialog(
								getParentFrame(), 
								"Work Unit Deconfig Type List",
								"Type List");
						mytypeList.setSizeSmall();
						mytypeList.loadAnswerListModel(typeList, 8);
						mytypeList.setDefaultSelection(type);
						mytypeList.setLocationRelativeTo(this);
						mytypeList.setVisible(true);

						if (mytypeList.getProceedSelected())
						{
							/* Call program to Set WU flags */
							String selectedType = mytypeList.getSelectedListOption();

							MfsXMLDocument xml_data2 = new MfsXMLDocument("SET_FLGGRP"); //$NON-NLS-1$
							xml_data2.addOpenTag("DATA"); //$NON-NLS-1$
							xml_data2.addCompleteField("MCTL", activeRow.substring(17, 25)); //$NON-NLS-1$
							xml_data2.addCompleteField("TYPE", selectedType); //$NON-NLS-1$
							xml_data2.addCloseTag("DATA"); //$NON-NLS-1$
							xml_data2.finalizeXML();

							MFSTransaction set_flggrp = new MFSXmlTransaction(xml_data2.toString());
							set_flggrp.setActionMessage("Accepting DeConfig .., Please Wait...");
							MFSComm.getInstance().execute(set_flggrp, this);
							rc = set_flggrp.getReturnCode();

							if (rc == 0)
							{
								/* Process the accept */
								MfsXMLDocument xml_data3 = new MfsXMLDocument("HANDLEDCFG"); //$NON-NLS-1$
								xml_data3.addOpenTag("DATA"); //$NON-NLS-1$	
								xml_data3.addCompleteField("ACTN", "ACCEPT"); //$NON-NLS-1$ //$NON-NLS-2$
								xml_data3.addCompleteField("MFGN", activeRow.substring(0, 7)); //$NON-NLS-1$
								xml_data3.addCompleteField("IDSS", activeRow.substring(10, 14)); //$NON-NLS-1$
								xml_data3.addCompleteField("MCTL", activeRow.substring(17, 25)); //$NON-NLS-1$
								xml_data3.addCompleteField("ORNO", activeRow.substring(28, 34)); //$NON-NLS-1$
								xml_data3.addCompleteField("USER", MFSConfig.getInstance().get8CharUser()); //$NON-NLS-1$
								xml_data3.addCloseTag("DATA"); //$NON-NLS-1$
								xml_data3.finalizeXML();

								MFSTransaction handledcfg = new MFSXmlTransaction(xml_data3.toString());
								handledcfg.setActionMessage("Accepting Deconfig Entry, Please Wait...");
								MFSComm.getInstance().execute(handledcfg, this);
								rc = handledcfg.getReturnCode();
								if (rc == 0)
								{
									refreshList(false);
									search(activeRow.substring(0, 34));
								}
								else
								{
									IGSMessageBox.showOkMB(this, null, handledcfg.getErms(), null);
								}
							}
							else
							{
								IGSMessageBox.showOkMB(this, null, set_flggrp.getErms(), null);
							}
						}
					}
					else
					{
						IGSMessageBox.showOkMB(this, null, rtvrettype.getErms(), null);
					}
				}
			}
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(this, null, null, e);
			this.toFront();
			this.tfSearch.setText(""); //$NON-NLS-1$
			this.tfSearch.requestFocusInWindow();
		}
		finally
		{
			addMyListeners();
		}
	}

	/** Invoked when {@link #pbAdd} is selected. */
	private void addRecord()
	{
		try
		{
			removeMyListeners();

			//collect mctl from the user
			/*-- Changed IPSR28462CT --*/
			MFSWorkUnitPNSNDialog myWrkUnitPNSND = new MFSWorkUnitPNSNDialog(getParentFrame());
			myWrkUnitPNSND.setLocationRelativeTo(getParentFrame()); //~1A
			myWrkUnitPNSND.setVisible(true);
			if (myWrkUnitPNSND.getPressedEnter())
			{
				MfsXMLDocument xml_data1 = new MfsXMLDocument("HANDLEDCFG"); //$NON-NLS-1$
				xml_data1.addOpenTag("DATA"); //$NON-NLS-1$
				xml_data1.addCompleteField("ACTN", "ADD   "); //$NON-NLS-1$ //$NON-NLS-2$
				xml_data1.addCompleteField("MCTL", myWrkUnitPNSND.getMctl()); //$NON-NLS-1$
				xml_data1.addCompleteField("USER", MFSConfig.getInstance().get8CharUser()); //$NON-NLS-1$
				xml_data1.addCloseTag("DATA"); //$NON-NLS-1$
				xml_data1.finalizeXML();

				MFSTransaction handledcfg = new MFSXmlTransaction(xml_data1.toString());
				handledcfg.setActionMessage("Adding Deconfig Entry, Please Wait...");
				MFSComm.getInstance().execute(handledcfg, this);
				
				if (handledcfg.getReturnCode() == 0)
				{
					refreshList(false);
					search(myWrkUnitPNSND.getMctl());
				}
				else
				{
					IGSMessageBox.showOkMB(this, null, handledcfg.getErms(), null);
				}
			}
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(this, null, null, e);
			this.toFront();
			this.tfSearch.setText(""); //$NON-NLS-1$
			this.tfSearch.requestFocusInWindow();
		}
		finally
		{
			addMyListeners();
		}
	}

	/**
	 * Loads the DE10 list using the specified data.
	 * @param data the data used to load the DE10 list
	 */
	public void loadOrderList(String data)
	{
		try
		{
			DefaultListModel listModel = (DefaultListModel) this.lstDE10.getModel();

			MfsXMLParser xmlParser = new MfsXMLParser(data);
			String tempRCD = null;
			boolean empty = false;

			try
			{
				tempRCD = xmlParser.getField("RCD"); //$NON-NLS-1$
			}
			catch (mfsxml.MISSING_XML_TAG_EXCEPTION e)
			{
				empty = true;
			}

			if (!empty)
			{
				while (!tempRCD.equals("")) //$NON-NLS-1$
				{
					MfsXMLParser xmlSubParser = new MfsXMLParser(tempRCD);
					StringBuffer displayString = new StringBuffer();
					displayString.append(xmlSubParser.getField("MFGN")); //$NON-NLS-1$
					displayString.append("   "); //$NON-NLS-1$
					displayString.append(xmlSubParser.getField("IDSS")); //$NON-NLS-1$
					displayString.append("   "); //$NON-NLS-1$
					displayString.append(xmlSubParser.getField("MCTL")); //$NON-NLS-1$
					displayString.append("   "); //$NON-NLS-1$
					displayString.append(xmlSubParser.getField("ORNO")); //$NON-NLS-1$
					displayString.append("    "); //$NON-NLS-1$
					displayString.append(xmlSubParser.getField("PRST")); //$NON-NLS-1$
					displayString.append("     "); //$NON-NLS-1$
					displayString.append(xmlSubParser.getField("RCVD")); //$NON-NLS-1$
					displayString.append("   "); //$NON-NLS-1$
					displayString.append(xmlSubParser.getField("RCVT")); //$NON-NLS-1$
					displayString.append("   "); //$NON-NLS-1$
					displayString.append(xmlSubParser.getField("CSDS")); //$NON-NLS-1$
					displayString.append("   "); //$NON-NLS-1$
					displayString.append(xmlSubParser.getField("CSTS")); //$NON-NLS-1$
					displayString.append("   "); //$NON-NLS-1$
					displayString.append(xmlSubParser.getField("USER")); //$NON-NLS-1$

					listModel.addElement(displayString.toString());
					tempRCD = xmlParser.getNextField("RCD"); //$NON-NLS-1$
				}
			}
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
			this.toFront();
			this.tfSearch.setText(""); //$NON-NLS-1$
			this.tfSearch.requestFocusInWindow();
		}
		finally
		{
			pack();
		}
	}

	/**
	 * Updates the contents of the DE10 list.
	 * @param rmvListeners <code>true</code> to remove the listeners from this
	 *        dialog's <code>Component</code>s before updating the list
	 * @return 0 on success; nonzero on failure
	 */
	private int refreshList(boolean rmvListeners)
	{
		int rc = 0;
		try
		{
			if (rmvListeners)
			{
				removeMyListeners();
			}

			MfsXMLDocument xml_data1 = new MfsXMLDocument("HANDLEDCFG"); //$NON-NLS-1$
			xml_data1.addOpenTag("DATA"); //$NON-NLS-1$
			xml_data1.addCompleteField("ACTN", "QUERY"); //$NON-NLS-1$ //$NON-NLS-2$	
			xml_data1.addCloseTag("DATA"); //$NON-NLS-1$
			xml_data1.finalizeXML();

			MFSTransaction handledcfg = new MFSXmlTransaction(xml_data1.toString());
			handledcfg.setActionMessage("Refreshing List of Deconfig Entries, Please Wait...");
			MFSComm.getInstance().execute(handledcfg, this);
			rc = handledcfg.getReturnCode();

			if (rc == 0)
			{
				((DefaultListModel) this.lstDE10.getModel()).removeAllElements();
				loadOrderList(handledcfg.getOutput());
			}
			else
			{
				IGSMessageBox.showOkMB(this, null, handledcfg.getErms(), null);
			}
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(this, null, null, e);
			this.toFront();
			this.tfSearch.setText(""); //$NON-NLS-1$
			this.tfSearch.requestFocusInWindow();
		}
		finally
		{
			if (rmvListeners)
			{
				addMyListeners();
			}
		}
		return rc;
	}

	/** Invoked when {@link #pbReject} is selected. */
	private void rejectRecord()
	{
		try
		{
			removeMyListeners();

			if (this.lstDE10.getSelectedIndex() == -1)
			{
				String erms = "Please Select a Record to Reject";
				IGSMessageBox.showOkMB(this, null, erms, null);
				this.toFront();
				this.tfSearch.setText(""); //$NON-NLS-1$
				this.tfSearch.requestFocusInWindow();
			}
			else
			{
				DefaultListModel orderListModel = (DefaultListModel) this.lstDE10.getModel();
				String activeRow = ((String) orderListModel.getElementAt(this.lstDE10.getSelectedIndex()));

				if (!activeRow.substring(38, 39).equals(" ")) //$NON-NLS-1$
				{
					String erms = "Invalid Record to Reject - Status = " + activeRow.substring(38, 39);
					IGSMessageBox.showOkMB(this, null, erms, null);
					this.toFront();
					this.tfSearch.setText(""); //$NON-NLS-1$
					this.tfSearch.requestFocusInWindow();
				}
				else
				{
					MfsXMLDocument xml_data1 = new MfsXMLDocument("HANDLEDCFG");//$NON-NLS-1$
					xml_data1.addOpenTag("DATA"); //$NON-NLS-1$
					xml_data1.addCompleteField("ACTN", "REJECT"); //$NON-NLS-1$ //$NON-NLS-2$
					xml_data1.addCompleteField("MFGN", activeRow.substring(0, 7)); //$NON-NLS-1$
					xml_data1.addCompleteField("IDSS", activeRow.substring(10, 14)); //$NON-NLS-1$
					xml_data1.addCompleteField("MCTL", activeRow.substring(17, 25)); //$NON-NLS-1$
					xml_data1.addCompleteField("ORNO", activeRow.substring(28, 34)); //$NON-NLS-1$
					xml_data1.addCompleteField("USER", MFSConfig.getInstance().get8CharUser()); //$NON-NLS-1$
					xml_data1.addCloseTag("DATA"); //$NON-NLS-1$
					xml_data1.finalizeXML();

					MFSTransaction handledcfg = new MFSXmlTransaction(xml_data1.toString());
					handledcfg.setActionMessage("Rejecting Deconfig Entry, Please Wait...");
					MFSComm.getInstance().execute(handledcfg, this);

					if (handledcfg.getReturnCode() == 0)
					{
						refreshList(false);
						search(activeRow.substring(0, 34));
					}
					else
					{
						IGSMessageBox.showOkMB(this, null, handledcfg.getErms(), null);
					}
				}/* blank status */
			}/* record selected */
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(this, null, null, e);
			this.toFront();
			this.tfSearch.setText(""); //$NON-NLS-1$
			this.tfSearch.requestFocusInWindow();
		}
		finally
		{
			addMyListeners();
		}
	}

	/** Searches the list for the text in {@link #tfSearch}. */
	public void search()
	{
		search(this.tfSearch.getText().toUpperCase().trim());
	}

	/** 
	 * Searches the list for the specified <code>searchString</code>.
	 * @param searchString the search criteria
	 */
	public void search(String searchString)
	{
		try
		{
			if (searchString.length() == 0)
			{
				String erms = "Invalid Search Criteria Entered";
				IGSMessageBox.showOkMB(this, null, erms, null);
				this.toFront();
				this.tfSearch.setText(""); //$NON-NLS-1$
				this.tfSearch.requestFocusInWindow();
			}
			else
			{
				DefaultListModel orderListModel = (DefaultListModel) this.lstDE10.getModel();
				int index = 0;
				if (this.lstDE10.getSelectedIndex() != -1
						&& this.lstDE10.getSelectedIndex() != orderListModel.size() - 1)
				{
					index = this.lstDE10.getSelectedIndex() + 1;
				}

				boolean found = false;
				while (index < orderListModel.size() && !found)
				{
					if (((String) orderListModel.getElementAt(index)).indexOf(searchString) != -1)
					{
						this.lstDE10.requestFocusInWindow();
						this.lstDE10.ensureIndexIsVisible(index);
						this.lstDE10.setSelectedIndex(index);
						this.tfSearch.requestFocusInWindow();
						found = true;
					}
					else
					{
						index++;
					}
				}
			}
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(this, null, null, e);
			this.toFront();
			this.tfSearch.setText(""); //$NON-NLS-1$
			this.tfSearch.requestFocusInWindow();
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
			if (source == this.pbExit)
			{
				this.dispose();
			}
			else if (source == this.pbReject)
			{
				rejectRecord();
			}
			else if (source == this.pbRefresh)
			{
				if (refreshList(true) == 0)
				{
					this.update(this.getGraphics());
				}
			}
			else if (source == this.pbAccept)
			{
				acceptRecord();
			}
			else if (source == this.pbAdd)
			{
				addRecord();
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
			if (source instanceof JButton)
			{
				JButton button = (JButton) source;
				button.requestFocusInWindow();
				button.doClick();
			}
			else if (source == this.tfSearch)
			{
				search();
			}
		}
		else if (keyCode == KeyEvent.VK_ESCAPE)
		{
			this.pbExit.requestFocusInWindow();
			this.pbExit.doClick();
		}
	}

	/**
	 * Invoked the first time a window is made visible.
	 * <p>
	 * Requests the focus for {@link #tfSearch}.
	 * @param we the <code>WindowEvent</code>
	 */
	public void windowOpened(WindowEvent we)
	{
		if (we.getSource() == this)
		{
			this.tfSearch.requestFocusInWindow();
		}
	}
}
