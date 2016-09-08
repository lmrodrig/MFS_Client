/* © Copyright IBM Corporation 2005, 2010. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2004-06-14   ~1 27298KF  TL Moua          -Remove Multiple parts with one set of IR codes
 * 2006-04-30   ~2 34647EM  Toribioh         -Change Input capacity for JTextFieldLen
 *                                            and validate inuput size and Barcode Rules
 *                                            in WWNNDialog class
 * 2007-02-22      34242JR  R Prechel        -Java 5 version
 * 2007-04-02   ~3 38166JM  R Prechel        -Add setLocationRelativeTo for dialogs
 * 2007-08-08   ~4 38834RS  Martha Luna      -Validate remove option when is called UPDT_RA tx
 * 2007-10-16  ~05 40076EM  Toribio H.       -Change mcsn to default as in UPDT_MS function called in UPDT_CRWC
 *                                            after UPDT_CRWC is called
 * 2008-01-14      39782JM	Martha Luna		 -Changes the name of updtMultilineDisplayString by updtIRDisplayString
 * 2008-12-17   ~6 43541DK  Santiago SC      -Need to prevent CSNI of L parts from being removed or reworked (CSC)
 * 2009-06-05  ~07 43813MZ  Christopher O    -Call MFSPartDataCollectDialog if Data Collection is required
 * 2009-06-24  ~08 45549SR  Christopher O    -Validate error Message in Data Collect Dialog
 * 2009-07-03  ~09 45654SR  Christopher O    -Add collectMode param in containsComponentCollection function.
 * 2009-07-09  ~10 45691EM  Christopher O    -Validate null object before call getDataCollection function
 * 2010-11-01  ~11 49513JM  Toribio H.		 -Use new RTV_IRCD cached server trx
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.dialog;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.WindowConstants;

import com.ibm.rchland.mfgapps.client.utils.messagebox.IGSMessageBox;
import com.ibm.rchland.mfgapps.mfsclient.MFSActionableDialog;
import com.ibm.rchland.mfgapps.mfsclient.MFSFrame;
import com.ibm.rchland.mfgapps.mfsclient.barcode.MFSBCBarcodeDecoder;
import com.ibm.rchland.mfgapps.mfsclient.barcode.MFSBCIndicatorValue;
import com.ibm.rchland.mfgapps.mfsclient.barcode.MFSBCPartObject;
import com.ibm.rchland.mfgapps.mfsclient.dialog.log.MFSPartDataCollectDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.rework.MFSRwkCCDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.rework.MFSRwkIRDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.rework.MFSRwkSCDialog;
import com.ibm.rchland.mfgapps.mfsclient.struct.MFSComponentListModel;
import com.ibm.rchland.mfgapps.mfsclient.struct.MFSComponentRec;
import com.ibm.rchland.mfgapps.mfsclient.struct.MFSHeaderRec;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSConstants;
import com.ibm.rchland.mfgapps.mfscommon.MFSConfig;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSComm;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSFixedTransaction;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSTransaction;
import com.ibm.rchland.mfgapps.mfscommon.comm.trx.RTV_IRCD;

/**
 * <code>MFSRemoveMultPartDialog</code> is the
 * <code>MFSActionableDialog</code> used to remove multiple parts.
 * @author The MFS Client Development Team
 */
@SuppressWarnings("serial")
public class MFSRemoveMultPartDialog
	extends MFSActionableDialog
{
	/** The input <code>JTextField</code>. */
	private JTextField tfInput = createTextField(MFSConstants.MEDIUM_TF_COLS, 22);

	/** The part number value <code>JLabel</code>. */
	private JLabel vlPN = new JLabel("  "); //$NON-NLS-1$

	/** The serial number value <code>JLabel</code>. */
	private JLabel vlSN = new JLabel("  "); //$NON-NLS-1$

	/** The <code>JList</code> of installed part numbers. */
	private JList lstInstallPN = new JList();

	/** The <code>JList</code> of selected part numbers. */
	private JList lstSelectedPN = new JList();

	/** The <code>JScrollPane</code> for the list of installed part numbers. */
	private JScrollPane spInstall = new JScrollPane(this.lstInstallPN,
			ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
			ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

	/** The <code>JScrollPane</code> for the list of selected part numbers. */
	private JScrollPane spSelected = new JScrollPane(this.lstSelectedPN,
			ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
			ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

	/** The Add Part <code>JButton</code>. */
	private JButton pbAdd = new JButton("== Add Part ==>"); //$NON-NLS-1$

	/** The Remove Part <code>JButton</code>. */
	private JButton pbRemove = new JButton("<== Remove Part =="); //$NON-NLS-1$

	/** The Select All <code>JButton</code>. */
	private JButton pbSelectAll = new JButton("== Select All ==>"); //$NON-NLS-1$

	/** The Deselect All <code>JButton</code>. */
	private JButton pbDeselectAll = new JButton("<== Deselect All =="); //$NON-NLS-1$

	/** The Rework <code>JButton</code>. */
	private JButton pbRework = new JButton("Rework (F2)"); //$NON-NLS-1$

	/** The Cancel <code>JButton</code>. */
	private JButton pbCancel = new JButton("Cancel/Exit"); //$NON-NLS-1$

	/** The <code>MFSComponentListModel</code> for installed parts. */
	private MFSComponentListModel fieldInstallCompListModel = new MFSComponentListModel();

	/** The <code>MFSComponentListModel</code> for selected parts. */
	private MFSComponentListModel fieldSelectedCompListModel = new MFSComponentListModel();

	/** The <code>MFSHeaderRec</code> for the work unit. */
	private MFSHeaderRec fieldHeaderRec;

	/**
	 * Constructs a new <code>MFSRemoveMultPartDialog</code>.
	 * @param parent the <code>MFSFrame</code> that caused this
	 *        <code>MFSRemoveMultPartDialog</code> to be displayed
	 * @param headerRec the <code>MFSHeaderRec</code> for the work unit
	 */
	public MFSRemoveMultPartDialog(MFSFrame parent, MFSHeaderRec headerRec)
	{
		super(parent, "Remove Multiple Parts"); //$NON-NLS-1$
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.fieldHeaderRec = headerRec;
		this.lstInstallPN.setModel(new DefaultListModel());
		this.lstSelectedPN.setModel(new DefaultListModel());
		createLayout();
		addMyListeners();
	}

	/** Adds this dialog's <code>Component</code>s to the layout. */
	private void createLayout()
	{
		this.tfInput.setToolTipText("Enter/Scan in part and serial"); //$NON-NLS-1$
		this.vlPN.setToolTipText("Part Number"); //$NON-NLS-1$

		JLabel installListDescLabel = new JLabel("INSTALLED PARTS"); //$NON-NLS-1$
		JLabel installDescLabel = new JLabel("Part Number              Serial Number          Test Lvl"); //$NON-NLS-1$

		JLabel selectedListDescLabel = new JLabel("REWORK PARTS"); //$NON-NLS-1$
		JLabel selectedDescLabel = new JLabel("Part Number              Serial Number          Test Lvl"); //$NON-NLS-1$

		this.lstInstallPN.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		JPanel buttonPanel = new JPanel(new GridLayout(6, 1, 20, 20));
		buttonPanel.add(this.pbAdd);
		buttonPanel.add(this.pbRemove);
		buttonPanel.add(this.pbSelectAll);
		buttonPanel.add(this.pbDeselectAll);
		buttonPanel.add(this.pbRework);
		buttonPanel.add(this.pbCancel);

		JPanel contentPane = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints(1, 0, 1, 1, 1, 0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, 
				new Insets(4, 0, 4, 4), 0, 0);

		contentPane.add(this.tfInput, gbc);

		gbc.gridy = 1;
		gbc.anchor = GridBagConstraints.WEST;
		contentPane.add(this.vlPN, gbc);

		gbc.gridy = 2;
		contentPane.add(this.vlSN, gbc);

		gbc.gridx = 0;
		gbc.weightx = 0;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(10, 15, 0, 44);
		contentPane.add(installListDescLabel, gbc);

		gbc.gridx = 2;
		contentPane.add(selectedListDescLabel, gbc);

		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.anchor = GridBagConstraints.WEST;
		contentPane.add(installDescLabel, gbc);

		gbc.gridx = 2;
		contentPane.add(selectedDescLabel, gbc);

		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(8, 15, 4, 4);
		contentPane.add(this.spInstall, gbc);

		gbc.gridx = 1;
		gbc.insets = new Insets(8, 4, 4, 4);
		contentPane.add(buttonPanel, gbc);

		gbc.gridx = 2;
		contentPane.add(this.spSelected, gbc);

		setContentPane(contentPane);
		pack();
	}

	/** Adds the listeners to this dialog's <code>Component</code>s. */
	private void addMyListeners()
	{
		this.pbAdd.addActionListener(this);
		this.pbRemove.addActionListener(this);
		this.pbSelectAll.addActionListener(this);
		this.pbDeselectAll.addActionListener(this);
		this.pbRework.addActionListener(this);
		this.pbCancel.addActionListener(this);

		this.pbAdd.addKeyListener(this);
		this.pbRemove.addKeyListener(this);
		this.pbSelectAll.addKeyListener(this);
		this.pbDeselectAll.addKeyListener(this);
		this.pbRework.addKeyListener(this);
		this.pbCancel.addKeyListener(this);

		this.tfInput.addKeyListener(this);
	}

	/**
	 * Loads the list model of the installed part number list.
	 * @param listModel the <code>MFSComponentListModel</code> containing a
	 *        work unit's components
	 */
	public void loadInstallPNList(MFSComponentListModel listModel)
	{
		int index = 0;
		DefaultListModel installPNListModel = (DefaultListModel) this.lstInstallPN.getModel();

		while (listModel.size() > index)
		{
			MFSComponentRec next = listModel.getComponentRecAt(index);
			if (next.getIdsp().equals("I") || next.getIdsp().equals("R")) //$NON-NLS-1$ //$NON-NLS-2$
			{
				StringBuffer element = new StringBuffer(64);
				element.append(next.getInpn());
				element.append("        "); //$NON-NLS-1$
				element.append(next.getInsq());
				element.append("         "); //$NON-NLS-1$
				element.append(next.getCtlv());

				installPNListModel.addElement(element.toString());
				this.fieldInstallCompListModel.addElement(next);
			}
			index++;
		}
		if (index == 0)
		{
			String erms = "There are no parts that can be removed"; //$NON-NLS-1$
			IGSMessageBox.showOkMB(getParentFrame(), null, erms, null);
		}
	}

	/** Invoked when {@link #pbAdd} is selected. */
	private void processAddButton()
	{
		DefaultListModel installPNListModel = (DefaultListModel) this.lstInstallPN.getModel();
		DefaultListModel selectedPNListModel = (DefaultListModel) this.lstSelectedPN.getModel();

		if (0 < installPNListModel.size())
		{
			int selectedIndex = this.lstInstallPN.getSelectedIndex();
			if (selectedIndex > -1)
			{
				selectedPNListModel.addElement(installPNListModel.getElementAt(selectedIndex));
				this.fieldSelectedCompListModel.addElement(this.fieldInstallCompListModel.getComponentRecAt(selectedIndex));
				installPNListModel.remove(selectedIndex);
				this.fieldInstallCompListModel.remove(selectedIndex);
			}
			else
			{
				String erms = "Please select a part to be added"; //$NON-NLS-1$
				IGSMessageBox.showOkMB(this, null, erms, null);
			}
		}
		else
		{
			String erms = "There are no parts to be added"; //$NON-NLS-1$
			IGSMessageBox.showOkMB(this, null, erms, null);
		}
	}

	/** Invoked when {@link #pbDeselectAll} is selected. */
	private void processDeselectAllButton()
	{
		DefaultListModel installPNListModel = (DefaultListModel) this.lstInstallPN.getModel();
		DefaultListModel selectedPNListModel = (DefaultListModel) this.lstSelectedPN.getModel();

		int index = 0;
		int selected_size = selectedPNListModel.size();
		while (index < selected_size)
		{
			installPNListModel.addElement(selectedPNListModel.getElementAt(0));
			this.fieldInstallCompListModel.addElement(this.fieldSelectedCompListModel.getComponentRecAt(0));
			selectedPNListModel.remove(0);
			this.fieldSelectedCompListModel.remove(0);
			index++;
		}
	}

	/** Invoked when {@link #pbRemove} is selected. */
	private void processRemoveButton()
	{
		DefaultListModel installPNListModel = (DefaultListModel) this.lstInstallPN.getModel();
		DefaultListModel selectedPNListModel = (DefaultListModel) this.lstSelectedPN.getModel();

		if (0 < selectedPNListModel.size())
		{
			int selectedIndex = this.lstSelectedPN.getSelectedIndex();
			if (selectedIndex > -1)
			{
				installPNListModel.addElement(selectedPNListModel.getElementAt(selectedIndex));
				this.fieldInstallCompListModel.addElement(this.fieldSelectedCompListModel.getComponentRecAt(selectedIndex));
				selectedPNListModel.remove(selectedIndex);
				this.fieldSelectedCompListModel.remove(selectedIndex);
			}
			else
			{
				String erms = "Please select a part to be removed from the selected list"; //$NON-NLS-1$
				IGSMessageBox.showOkMB(this, null, erms, null);
			}
		}
		else
		{
			String erms = "There are not parts to be removed from the selected list"; //$NON-NLS-1$
			IGSMessageBox.showOkMB(this, null, erms, null);
		}
	}

	/** Invoked when {@link #pbRework} is selected. */
	private void processReworkButton()
	{
		try
		{
			int rc = 0;
			String badPartInd = "N"; //$NON-NLS-1$
			String IBMDefect = "N"; //$NON-NLS-1$
			boolean readyToCallUPDT_RA = false;
			String cccde = ""; //$NON-NLS-1$
			String sccde = ""; //$NON-NLS-1$
			String ircde = ""; //$NON-NLS-1$

			final MFSConfig config = MFSConfig.getInstance();

			String errorString = ""; //$NON-NLS-1$
			String comment = ""; //$NON-NLS-1$

			// get the defect loc from the config file
			String defectloc = config.getConfigValue("DEFECTLOC") //$NON-NLS-1$
					.concat("                         ").substring(0, 25);//$NON-NLS-1$
			MFSComponentRec rwkCmp;
			MFSComponentRec cmpRec;
			int index = 0;

			//GET IR CODES
			if (0 < this.fieldSelectedCompListModel.size())
			{
				RTV_IRCD rtvIRCD = new RTV_IRCD(this); //~11
				rtvIRCD.setInputCellType(MFSConfig.getInstance().get8CharCellType());					
				rtvIRCD.execute();
				
				rc = rtvIRCD.getReturnCode();
				if (rc != 0)
				{
					IGSMessageBox.showOkMB(this, null, rtvIRCD.getErrorMessage(), null);
				}
				else
				{
					boolean irDone = false;
					if (!rtvIRCD.getOutputIRData().equals("")) //$NON-NLS-1$
					{
						while (!irDone)
						{
							MFSRwkIRDialog myRwkIRJD = new MFSRwkIRDialog(getParentFrame());
							myRwkIRJD.loadRwkIRListModel(rtvIRCD.getOutputIRData());
							myRwkIRJD.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
							myRwkIRJD.setLocationRelativeTo(getParentFrame()); //~3A
							myRwkIRJD.setVisible(true);
							if (myRwkIRJD.getProceedSelected())
							{
								boolean scDone = false;

								while (!scDone)
								{
									MFSRwkSCDialog myRwkSCJD = new MFSRwkSCDialog(getParentFrame());
									myRwkSCJD.loadRwkSCListModel(rtvIRCD.getOutputIRData(), myRwkIRJD.getSelectedListOption().substring(0, 3));
									myRwkSCJD.setLocationRelativeTo(getParentFrame()); //~3A
									myRwkSCJD.setVisible(true);
									if (myRwkSCJD.getProceedSelected())
									{
										MFSRwkCCDialog myRwkCCJD = new MFSRwkCCDialog(getParentFrame(), ""); //$NON-NLS-1$
										myRwkCCJD.loadRwkCCListModel(rtvIRCD.getOutputCCData(), myRwkIRJD.getSelectedListOption().substring(0, 3));
										myRwkCCJD.setLocationRelativeTo(getParentFrame());
										myRwkCCJD.setVisible(true);
										if (myRwkCCJD.getProceedSelected())
										{
											/* pad comment with blanks to 80 chars */
											comment = myRwkCCJD.getCommentText();
											while (comment.length() < 80)
											{
												comment += " "; //$NON-NLS-1$
											}

											if (defectloc.substring(0, 9).equals(MFSConfig.NOT_FOUND))
											{
												defectloc = "                         "; //$NON-NLS-1$
												readyToCallUPDT_RA = true;
											}
											else
											{
												readyToCallUPDT_RA = true;
											}

											if (readyToCallUPDT_RA)
											{
												//protect against index out of bounds on the IR,SC and CC values.
												if (myRwkIRJD.getSelectedListOption().length() >= 3)
												{
													ircde = myRwkIRJD.getSelectedListOption().substring(0, 3);
												}
												else
												{
													ircde = "   ";//$NON-NLS-1$
												}
												if (myRwkSCJD.getSelectedListOption().length() >= 7)
												{
													sccde = myRwkSCJD.getSelectedListOption().substring(5, 7);
												}
												else
												{
													sccde = "  ";//$NON-NLS-1$
												}
												if (myRwkCCJD.getSelectedListOption().length() >= 2)
												{
													cccde = myRwkCCJD.getSelectedListOption().substring(0, 2);
												}
												else
												{
													cccde = "  ";//$NON-NLS-1$
												}
											}
											scDone = true;
											irDone = true;
										}// end of myRwkCCJD.getProceedSelected()
									}// end of myRwkSCJD.getProceedSelected()
									else
									{
										scDone = true;
									}
								}
							}
							else
							{
								irDone = true;
							}
						}
					}
					//no ir data
					else
					{
						/* pad comment with blanks to 80 chars */
						comment = ""; //$NON-NLS-1$
						while (comment.length() < 80)
						{
							comment += " ";//$NON-NLS-1$
						}

						if (defectloc.substring(0, 9).equals(MFSConfig.NOT_FOUND))
						{
							defectloc = "                         ";//$NON-NLS-1$
							readyToCallUPDT_RA = true;
						}
						else
						{
							readyToCallUPDT_RA = true;
						}// end of no defectloc found
					}// end of no IR codes
				}
			}

			//GO THROUGH COMPONENTS AND REMOVE THEM
			SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-ddHH.mm.ss");//$NON-NLS-1$
			Date currTime;
			String dat;

			index = 0;
			int comp_size = this.fieldSelectedCompListModel.size();
			int get_index = 0;
			while (index < comp_size && readyToCallUPDT_RA)
			{
				currTime = new java.util.Date();
				dat = fmt.format(currTime);

				//Go through the component list
				rwkCmp = this.fieldSelectedCompListModel.getComponentRecAt(get_index);
				this.lstSelectedPN.setSelectedIndex(get_index);
				cmpRec = rwkCmp;

				//~6A - Need to prevent CSNI of L parts from being removed or reworked. (CSC)
				if (rc == 0 && cmpRec.getCsni().equals("L")) //$NON-NLS-1$
				{
					errorString = "Invalid Part to Rework"; //$NON-NLS-1$
					rc = -1;
				}
				
				if ((rc == 0) && (rwkCmp.getPnri().equals("W"))) //$NON-NLS-1$
				{
					MFSWWNNDialog wwnnJD = new MFSWWNNDialog(getParentFrame(), rwkCmp, MFSWWNNDialog.LT_REMOVE);
					wwnnJD.initDisplay();
					wwnnJD.setLocationRelativeTo(getParentFrame()); //~3A
					wwnnJD.setVisible(true);
					rc = wwnnJD.getReturnCode();
				}

				if((rc == 0) && (this.fieldHeaderRec.getDataCollection() != null) && (this.fieldHeaderRec.isCollectRequired()))			//~07A ~10C
				{																	//~07A	
					// Check if the Retrieved data contains the mctl and crct values to collect
					if(this.fieldHeaderRec.getDataCollection().containsComponentCollection(cmpRec.getMctl(),cmpRec.getCrct(),MFSPartDataCollectDialog.COLL_REMOVE))	//~07A ,~09C
					{																
						MFSPartDataCollectDialog collectDialog;						//~07A
						collectDialog = new MFSPartDataCollectDialog(this.getParentFrame(), this.fieldHeaderRec, MFSPartDataCollectDialog.COLL_REMOVE, cmpRec.getCrct(),cmpRec.getMctl()); //~07A
						//Get the return code from <code>MFSPartDataCollectDialog</code>
						rc = collectDialog.getFieldReturnCode();					//~07A
						if(rc != 0)     											//~07A ~08C
						{															//~07A							
							//Stop removing parts 	
							break;													//~07A
						}															//~07A
					}																//~07A													
				}																	//~07A
				
				/* new trx for checking plugged parts for ipsr 18841JM 8/22/01 */
				if ((rc == 0) && !cmpRec.getCsni().equals(" ") && !cmpRec.getCsni().equals("0") && //$NON-NLS-1$ //$NON-NLS-2$   //~07C
						!cmpRec.getCsni().equals("L")) //~6A //$NON-NLS-1$
					
				{
					/* build check plug info string */
					StringBuffer data = new StringBuffer();
					data.append("CHCK_PLUG "); //$NON-NLS-1$
					data.append(cmpRec.getInpn());
					data.append(cmpRec.getInsq());
					data.append(cmpRec.getMspi());
					data.append(cmpRec.getMcsn());
					data.append(cmpRec.getMctl());
					data.append(cmpRec.getPrln());
					data.append(cmpRec.getNmbr());
					data.append(config.get8CharUser());
					data.append(cmpRec.getCrct());

					MFSTransaction chck_plug = new MFSFixedTransaction(data.toString());
					chck_plug.setActionMessage("Checking Plug Limit, Please Wait..."); //$NON-NLS-1$
					MFSComm.getInstance().execute(chck_plug, this);
					rc = chck_plug.getReturnCode();

					errorString = chck_plug.getOutput();
					//rc = 50, part to be ncm'd,
					//rc=100 part has one plug left warning
					if ((rc == 50) || (rc == 100))
					{
						IGSMessageBox.showOkMB(this, null, errorString, null);
						rc = 0;
					}
				} /* end-if serial part check */

				if (rc == 0) /* added for check_qd10 trx 18841JM */
				{
					String BLANK25 = "                         ";//$NON-NLS-1$
					String BLANK76 = "                                                                            ";//$NON-NLS-1$
					String unFkitFlag = " ";//$NON-NLS-1$
					String idsp = "N";//$NON-NLS-1$
					if (this.fieldHeaderRec.getWtyp().equals("T"))//$NON-NLS-1$
					{
						idsp = "D";//$NON-NLS-1$
					}

					StringBuffer data = new StringBuffer(512);
					data.append("UPDT_CRWC ");//$NON-NLS-1$
					data.append(config.get8CharUser());
					data.append(config.get8CharCell());
					data.append(dat);
					data.append(config.get8CharCellType());
					data.append(BLANK76);
					data.append(cmpRec.getMctl());
					data.append(cmpRec.getCrct());
					data.append(cmpRec.getInpn());
					data.append("            ");//$NON-NLS-1$
					data.append(cmpRec.getInsq());
					data.append("            ");//$NON-NLS-1$
					data.append(cmpRec.getAmsi());
					data.append(cmpRec.getMspi());
					data.append(cmpRec.getMcsn());
					data.append(cmpRec.getCwun());
					data.append(idsp);
					data.append("          ");//$NON-NLS-1$
					data.append("00000"); //$NON-NLS-1$
					data.append(" ");//$NON-NLS-1$
					data.append(cmpRec.getShtp());
					data.append(cmpRec.getMlri());
					data.append(cmpRec.getPnri());
					data.append("J");//$NON-NLS-1$
					data.append("  ");//$NON-NLS-1$
					data.append(" ");//$NON-NLS-1$
					data.append(unFkitFlag);
					data.append(BLANK25);

					MFSTransaction updt_crwc = new MFSFixedTransaction(data.toString());
					updt_crwc.setActionMessage("Updating Component, Please Wait..."); //$NON-NLS-1$
					MFSComm.getInstance().execute(updt_crwc, this);
					rc = updt_crwc.getReturnCode();
					if (rc != 0)
					{
						errorString = updt_crwc.getOutput();
					}
				}

				if (rc == 0)
				{
					//Remove from comp list
					this.fieldSelectedCompListModel.remove(get_index);
					((DefaultListModel) this.lstSelectedPN.getModel()).remove(get_index);
					get_index = 0;

					if (readyToCallUPDT_RA)
					{
						StringBuffer ra_data = new StringBuffer(1024);
						ra_data.append("UPDT_RA   ");//$NON-NLS-1$
						/* pad with *'s to 512 chars */
						while (ra_data.length() < 512)
						{
							ra_data.append("*");//$NON-NLS-1$
						}

						ra_data.append(this.fieldHeaderRec.getMfgn());
						ra_data.append(this.fieldHeaderRec.getIdss());
						ra_data.append(rwkCmp.getMctl());
						ra_data.append("        "); // epnm //$NON-NLS-1$
						ra_data.append("        "); // esnm //$NON-NLS-1$
						ra_data.append(this.fieldHeaderRec.getTypz());
						ra_data.append(this.fieldHeaderRec.getCmdl());
						ra_data.append(rwkCmp.getPrln());
						ra_data.append(rwkCmp.getProd());
						ra_data.append(rwkCmp.getMatp());
						ra_data.append(rwkCmp.getMspi());
						ra_data.append(rwkCmp.getMcsn());
						ra_data.append(this.fieldHeaderRec.getMmdl());
						ra_data.append(rwkCmp.getNmbr());
						ra_data.append(config.get8CharUser());
						ra_data.append(config.get8CharCellType());
						ra_data.append(config.get8CharCell());
						ra_data.append(dat);
						ra_data.append(rwkCmp.getCsds());
						ra_data.append(rwkCmp.getCsts());
						ra_data.append(rwkCmp.getNmbr());
						ra_data.append(rwkCmp.getUser());
						ra_data.append(rwkCmp.getCtyp());
						ra_data.append(rwkCmp.getCell());
						ra_data.append(rwkCmp.getInpn());
						ra_data.append(rwkCmp.getInsq());
						ra_data.append(rwkCmp.getInec());
						ra_data.append(rwkCmp.getInca());
						ra_data.append(rwkCmp.getCwun());
						ra_data.append(rwkCmp.getCdes());
						ra_data.append(rwkCmp.getPrtd());
						ra_data.append(rwkCmp.getPrdc());
						ra_data.append("        "); // rprd //$NON-NLS-1$
						ra_data.append(rwkCmp.getPll1());
						ra_data.append(rwkCmp.getPll2());
						ra_data.append(rwkCmp.getPll3());
						ra_data.append(rwkCmp.getPll4());
						ra_data.append(rwkCmp.getPll5());
						ra_data.append(ircde);
						ra_data.append(sccde);
						ra_data.append(cccde);
						ra_data.append("1"); // ifix //$NON-NLS-1$
						ra_data.append(comment);
						ra_data.append(badPartInd);
						ra_data.append(IBMDefect);
						ra_data.append(rwkCmp.getCrct());
						ra_data.append(defectloc);
						ra_data.append(1); //~4A
						/* pad with blanks to 1024 chars */
						while (ra_data.length() < 1024)
						{
							ra_data.append(" "); //$NON-NLS-1$
						}

						MFSTransaction updt_ra = new MFSFixedTransaction(ra_data.toString());
						updt_ra.setActionMessage("Updating Rwk Action Info, Please Wait..."); //$NON-NLS-1$
						MFSComm.getInstance().execute(updt_ra, this);
						rc = updt_ra.getReturnCode();

						if (rc != 0)
						{
							IGSMessageBox.showOkMB(this, null, updt_ra.getErms(), null);
						}
					}//readyToCallUPDTRA is true
					cmpRec.setInpn("            "); //$NON-NLS-1$
					cmpRec.setInsq("            "); //$NON-NLS-1$
					cmpRec.setInec("            "); //$NON-NLS-1$
					cmpRec.setInca("            "); //$NON-NLS-1$
					//don't want to blank out cwun, field is permanent
					cmpRec.setCooc("  ");//$NON-NLS-1$

					if (!cmpRec.getCmti().equals(" ") && //$NON-NLS-1$
							!cmpRec.getCmti().equals("0"))//$NON-NLS-1$
					{
						/* ~05 Set default mcsn instead of $000000*/
						/* cmpRec.setMcsn("$000000");//$NON-NLS-1$ */
						cmpRec.setMcsn("$" + this.fieldHeaderRec.getOrno());//$NON-NLS-1$
					}

					cmpRec.setFqty("00000");//$NON-NLS-1$
					if (this.fieldHeaderRec.getWtyp().equals("T"))//$NON-NLS-1$
					{
						cmpRec.setIdsp("D");//$NON-NLS-1$
					}
					else
					{
						cmpRec.setIdsp("A");//$NON-NLS-1$
					}
					cmpRec.setRec_changed(false);
					cmpRec.updtDisplayString();
					cmpRec.updtIRDisplayString();
					cmpRec.updtInstalledParts();
				}
				else if (rc != -44)
				{
					get_index++;
					IGSMessageBox.showOkMB(this, null, errorString, null);
				} /* end if for resetting client back to idsp = 'a' */
				index++;
			} /* end while */
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(this, null, null, e);
		}
	}

	/** Invoked when {@link #pbSelectAll} is selected. */
	private void processSelectAllButton()
	{
		DefaultListModel installPNListModel = (DefaultListModel) this.lstInstallPN.getModel();
		DefaultListModel selectedPNListModel = (DefaultListModel) this.lstSelectedPN.getModel();

		int index = 0;
		int install_size = installPNListModel.size();
		while (index < install_size)
		{
			selectedPNListModel.addElement(installPNListModel.getElementAt(0));
			this.fieldSelectedCompListModel.addElement(this.fieldInstallCompListModel.getComponentRecAt(0));
			installPNListModel.remove(0);
			this.fieldInstallCompListModel.remove(0);
			index++;
		}
	}

	/** Processes the input the user entered in the <code>JTextField</code>. */
	private void recvInput()
	{
		try
		{
			int rc = 0;

			MFSBCBarcodeDecoder barcode = MFSBCBarcodeDecoder.getInstance();
			barcode.setMyBarcodeInput(this.tfInput.getText().toUpperCase());
			barcode.setMyBCPartObject(new MFSBCPartObject());

			/* setup barcode indicator values */
			MFSBCIndicatorValue bcIndVal = new MFSBCIndicatorValue();
			bcIndVal.setPNRI("1"); //$NON-NLS-1$
			String sni = MFSConfig.getInstance().getConfigValue("DECODESN"); //$NON-NLS-1$
			if (!sni.equals(MFSConfig.NOT_FOUND)) //DECONDESN not found
			{
				bcIndVal.setCSNI(sni);
			}
			else
			{
				bcIndVal.setCSNI("1"); //$NON-NLS-1$
			}
			barcode.setMyBCIndicatorValue(bcIndVal);

			if (barcode.decodeBarcodeFor(this))
			{
				rc = barcode.getBCMyPartObject().getRC();
			}
			else
			{
				rc = 10;
				barcode.getBCMyPartObject().setErrorString(
						MFSBCBarcodeDecoder.DECODE_TIMED_OUT_ERMS);
			}

			if (rc != 0)
			{
				String erms = barcode.getBCMyPartObject().getErrorString();
				if (erms.length() == 0)
				{
					erms = MFSBCBarcodeDecoder.INVALID_BARCODE_ERMS;
				}
				IGSMessageBox.showOkMB(this, null, erms, null);
			}
			if (!(barcode.getBCMyPartObject().getPN().equals(""))) //$NON-NLS-1$
			{
				this.vlPN.setText("PN: " + barcode.getBCMyPartObject().getPN()); //$NON-NLS-1$
			}
			if (!(barcode.getBCMyPartObject().getSN().equals(""))) //$NON-NLS-1$
			{
				this.vlSN.setText("SN: " + barcode.getBCMyPartObject().getSN()); //$NON-NLS-1$
			}
			if (!this.vlSN.getText().trim().equals("") && !this.vlPN.getText().trim().equals("")) //$NON-NLS-1$ //$NON-NLS-2$
			{
				int index = searchInstallPNList(this.vlPN.getText().substring(4).trim(),
						this.vlSN.getText().substring(4).trim());
				if (index != -1)
				{
					DefaultListModel installPNListModel = (DefaultListModel) this.lstInstallPN.getModel();
					DefaultListModel selectedPNListModel = (DefaultListModel) this.lstSelectedPN.getModel();

					selectedPNListModel.addElement(installPNListModel.getElementAt(index));
					this.fieldSelectedCompListModel.addElement(this.fieldInstallCompListModel.getComponentRecAt(index));
					installPNListModel.remove(index);
					this.fieldInstallCompListModel.remove(index);
				}
				else
				{
					StringBuffer erms = new StringBuffer(128);
					erms.append("Part="); //$NON-NLS-1$
					erms.append(this.vlPN.getText().substring(4));
					erms.append(" and serial="); //$NON-NLS-1$
					erms.append(this.vlSN.getText().substring(4));
					erms.append(" entered not found in the installed list"); //$NON-NLS-1$
					IGSMessageBox.showOkMB(this, null, erms.toString(), null);
				}
				this.vlPN.setText("  "); //$NON-NLS-1$
				this.vlSN.setText("  "); //$NON-NLS-1$
			}
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(this, null, null, e);
		}

		this.toFront();
		this.tfInput.setText(""); //$NON-NLS-1$
	}

	/**
	 * Searches the installed part number list for a part and serial number.
	 * @param PN the part number to search for
	 * @param SN the serial number to search for
	 * @return the index of the part and serial number in the list, or -1 if
	 *         they were not found
	 */
	public int searchInstallPNList(String PN, String SN)
	{
		int index = 0;
		int list_size = this.fieldInstallCompListModel.size();
		while (list_size > index)
		{
			MFSComponentRec next = this.fieldInstallCompListModel.getComponentRecAt(index);
			if (next.getInpn().trim().equals(PN) && next.getInsq().trim().equals(SN))
			{
				return index;
			}
			index++;
		}
		return -1;
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
			else if (source == this.pbAdd)
			{
				processAddButton();
			}
			else if (source == this.pbRemove)
			{
				processRemoveButton();
			}
			else if (source == this.pbRework)
			{
				processReworkButton();
			}
			else if (source == this.pbSelectAll)
			{
				processSelectAllButton();
			}
			else if (source == this.pbDeselectAll)
			{
				processDeselectAllButton();
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
			else if (source == this.pbRework)
			{
				processReworkButton();
			}
			else if (source == this.pbCancel)
			{
				this.pbCancel.requestFocusInWindow();
				this.pbCancel.doClick();
			}
		}
		else if (keyCode == KeyEvent.VK_F2)
		{
			processReworkButton();
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
