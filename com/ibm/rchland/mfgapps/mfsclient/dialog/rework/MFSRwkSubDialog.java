/* © Copyright IBM Corporation 2005, 2010. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2006-06-28   ~1 31801JM  R Prechel        -Changed LogPartJDialog constructor call
 *                                           -Removed unused constructors
 *                                           -Removed unused variables and methods
 *                                           -Uncommented handleException output
 * 2007-02-22   ~2 34242JR  R Prechel        -Java 5 version
 * 2007-03-30   ~3 38166JM  R Prechel        -Add setLocationRelativeTo for dialogs
 * 2007-08-08   ~4 38834RS  Martha Luna      -Validate remove option when is called UPDT_RA tx
 * 2009-06-05  ~05 43813MZ  Christopher O    -Call MFSPartDataCollectDialog if Data Collection is required
 * 2009-06-24      45549SR  Christopher O    -Validate error Message in Data Collect Dialog
 * 2009-07-03  ~06 45654SR  Christopher O    -Add collectMode param in containsComponentCollection function.
 * 2009-07-09  ~07 45691EM  Christopher O    -Validate null object before call getDataCollection function
 * 2010-11-01  ~08 49513JM	Toribio H.   	 -Make RTV_IRCD Cacheable 
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.dialog.rework;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.ibm.rchland.mfgapps.client.utils.messagebox.IGSMessageBox;
import com.ibm.rchland.mfgapps.mfsclient.MFSActionableDialog;
import com.ibm.rchland.mfgapps.mfsclient.MFSFrame;
import com.ibm.rchland.mfgapps.mfsclient.MFSMenuButton;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSGenericListDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.log.MFSLogPartDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.log.MFSPartDataCollectDialog;
import com.ibm.rchland.mfgapps.mfsclient.panel.MFSDirectWorkPanel;
import com.ibm.rchland.mfgapps.mfsclient.renderer.MFSComponentCellRendererRwkSub;
import com.ibm.rchland.mfgapps.mfsclient.struct.MFSComponentListModel;
import com.ibm.rchland.mfgapps.mfsclient.struct.MFSComponentRec;
import com.ibm.rchland.mfgapps.mfsclient.struct.MFSHeaderRec;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSConstants;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSPrintingMethods;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSScroller;
import com.ibm.rchland.mfgapps.mfscommon.MFSConfig;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSComm;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSFixedTransaction;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSTransaction;
import com.ibm.rchland.mfgapps.mfscommon.comm.trx.RTV_IRCD;

/**
 * <code>MFSRwkSubDialog</code> is the <code>MFSActionableDialog</code> used
 * to rework a subassembly.
 * @author The MFS Client Development Team
 */
@SuppressWarnings("serial")
public class MFSRwkSubDialog
	extends MFSActionableDialog
	implements ListSelectionListener, PropertyChangeListener
{
	/**
	 * The <code>JTextPane</code> that displays the plphtp property for the
	 * current <code>MFSComponentRec</code>.
	 */
	private JTextPane tpPhrase = createDisplayTextPane();

	/** The <code>JScrollPane</code> for {@link #tpPhrase}. */
	private JScrollPane spPhrase = new JScrollPane(this.tpPhrase,
			ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
			ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

	/**
	 * The <code>JTextPane</code> that displays the installed parts property
	 * for the current <code>MFSComponentRec</code>.
	 */
	private JTextPane tpInsParts = createDisplayTextPane();

	/** The <code>JScrollPane</code> for {@link #tpInsParts}. */
	private JScrollPane spInsParts = new JScrollPane(this.tpInsParts,
			ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
			ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

	/** The <code>JList</code> of <code>MFSComponentRec</code>s. */
	private JList lstComponents = createComponentsList();

	/** The <code>JScrollPane</code> for {@link #lstComponents}. */
	private JScrollPane spParts = new JScrollPane(this.lstComponents,
			ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
			ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

	/** The Rework (F6) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbRework = MFSMenuButton.createMediumButton("Rework", //$NON-NLS-1$
			"rewrkF6.gif", "Rework", null); //$NON-NLS-1$ //$NON-NLS-2$

	/** The Return (F12) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbReturn = MFSMenuButton.createMediumButton("Return", //$NON-NLS-1$
			"viewF12.gif", "Return", null); //$NON-NLS-1$ //$NON-NLS-2$

	/** The current <code>MFSComponentRec</code>. */
	private MFSComponentRec fieldCurrentCompRec;

	/** The <code>MFSHeaderRec</code> for the work unit. */
	private MFSHeaderRec fieldHeaderRec;

	/** The rework operation. */
	private String fieldRwkOp;

	/**
	 * Constructs a new <code>MFSRwkSubDialog</code>.
	 * @param parent the <code>MFSFrame</code> that caused this
	 *        <code>MFSRwkSubDialog</code> to be displayed
	 * @param rwkOp the rework operation
	 * @param headerRec the <code>MFSHeaderRec</code> for the work unit
	 * @param model the <code>MFSComponentListModel</code> displayed by the list
	 */
	public MFSRwkSubDialog(MFSFrame parent, String rwkOp, MFSHeaderRec headerRec,
							MFSComponentListModel model)     	 
	{
		super(parent, "Rework Parts in Subassembly"); //$NON-NLS-1$
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.fieldRwkOp = rwkOp;
		this.fieldHeaderRec = headerRec;
		this.lstComponents.setModel(model);
		createLayout();
		addMyListeners();

		this.spParts.getVerticalScrollBar().setValue(0);
		this.lstComponents.setSelectedIndex(0);
	}

	/** Adds this dialog's <code>Component</code>s to the layout. */
	private void createLayout()
	{
		this.spPhrase.setRequestFocusEnabled(false);
		this.spPhrase.setMinimumSize(new Dimension(540, 80));
		this.spPhrase.setPreferredSize(new Dimension(540, 80));

		this.spInsParts.setRequestFocusEnabled(false);
		this.spInsParts.setMinimumSize(new Dimension(540, 50));
		this.spInsParts.setPreferredSize(new Dimension(540, 50));

		this.spParts.setMinimumSize(new Dimension(540, 268));
		this.spParts.setPreferredSize(new Dimension(540, 268));

		this.pbRework.setEnabled(true);
		this.pbReturn.setEnabled(true);

		JPanel buttonPanel = new JPanel(new FlowLayout());
		buttonPanel.setBorder(BorderFactory.createLineBorder(MFSConstants.PRIMARY_FOREGROUND_COLOR));
		buttonPanel.setBackground(MFSConstants.PRIMARY_BACKGROUND_COLOR);
		buttonPanel.setMinimumSize(new Dimension(120, 420));
		buttonPanel.setPreferredSize(new Dimension(120, 420));
		buttonPanel.add(this.pbRework);
		buttonPanel.add(this.pbReturn);

		JPanel labelPanel = new JPanel(new GridBagLayout());
		labelPanel.setBackground(MFSConstants.PRIMARY_BACKGROUND_COLOR);
		
		GridBagConstraints labelGbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0),
				0, 0);
		
		labelPanel.add(new JLabel("FAMC"), labelGbc); //$NON-NLS-1$
		
		labelGbc.gridx++;
		labelGbc.weightx = 2.7;
		labelPanel.add(new JLabel("ITEM"), labelGbc); //$NON-NLS-1$
		
		labelGbc.gridx++;
		labelGbc.weightx = 6.1;
		labelPanel.add(new JLabel("DESC"), labelGbc); //$NON-NLS-1$
		
		labelGbc.gridx++;
		labelGbc.weightx = 0.9;
		labelPanel.add(new JLabel("QNTY"), labelGbc); //$NON-NLS-1$
		
		labelGbc.gridx++;
		labelGbc.weightx = 0.5;
		labelPanel.add(new JLabel("FQTY"), labelGbc); //$NON-NLS-1$
		
		labelGbc.gridx++;
		labelGbc.weightx = 0.1;
		labelPanel.add(new JLabel("IDSP"), labelGbc); //$NON-NLS-1$
		
		JPanel contentPane = new JPanel(new GridBagLayout());
		contentPane.setBackground(MFSConstants.PRIMARY_BACKGROUND_COLOR);
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, 
				new Insets(10, 15, 5, 10), 0, 0);

		contentPane.add(this.spPhrase, gbc);

		gbc.gridy = 1;
		gbc.insets = new Insets(0, 15, 5, 10);
		contentPane.add(this.spInsParts, gbc);

		gbc.gridy = 2;
		gbc.insets = new Insets(0, 30, 5, 20);
		contentPane.add(labelPanel, gbc);

		gbc.gridy = 3;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(0, 15, 5, 10);
		contentPane.add(this.spParts, gbc);

		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.gridheight = 4;
		gbc.weightx = 0.0;
		gbc.weighty = 1.0;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.VERTICAL;
		gbc.insets = new Insets(10, 0, 5, 15);
		contentPane.add(buttonPanel, gbc);

		setContentPane(contentPane);
		pack();
		
		//~2A The height calculation has to be done after 
		// setContentPane or getGraphics will return null.
		int height = this.lstComponents.getGraphics().getFontMetrics().getHeight();
		this.lstComponents.setFixedCellHeight(height * 2);
	}

	/**
	 * Creates a new <code>JList</code> that will be used to display the
	 * <code>MFSComponentRec</code>s.
	 * @return the new <code>JList</code>
	 */
	private JList createComponentsList()
	{
		JList result = new JList();
		result.setCellRenderer(new MFSComponentCellRendererRwkSub());
		result.setSelectedIndex(0);
		result.setFont(MFSConstants.MEDIUM_MONOSPACED_FONT);
		result.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		return result;
	}

	/**
	 * Creates a new <code>JTextPane</code> that will be used to display
	 * information about the current <code>MFSComponentRec</code>.
	 * @return the new <code>JTextPane</code>
	 */
	private JTextPane createDisplayTextPane()
	{
		JTextPane result = new JTextPane();
		result.setEditable(false);
		result.setFont(MFSConstants.MEDIUM_MONOSPACED_FONT);
		result.setRequestFocusEnabled(false);
		return result;
	}

	/** Adds the listeners to this dialog's <code>Component</code>s. */
	private void addMyListeners()
	{
		this.pbReturn.addActionListener(this);
		this.pbRework.addActionListener(this);

		this.pbReturn.addKeyListener(this);
		this.pbRework.addKeyListener(this);
		this.lstComponents.addKeyListener(this);

		this.lstComponents.addListSelectionListener(this);
		//~2D Do not add property change listener to this or lstComponents
		//because there is no MFSComponentListModel property
	}

	/** Executes the rework logic. */
	public void rework()
	{
		try
		{
			final MFSConfig config = MFSConfig.getInstance();
			int rc = 0;
			String badPartInd = "N"; //$NON-NLS-1$
			String IBMDefect = "N"; //$NON-NLS-1$
			boolean readyToCallUPDT_RA = false;
			String NCMTagSequence = ""; //$NON-NLS-1$
			boolean goForward = false; /* ~1 */

			/* save current component rec info */
			MFSComponentRec rwkCmp = new MFSComponentRec(this.fieldCurrentCompRec);

			/* ~1 add recursion logic here, so that we can keep drilling down */
			if (!rwkCmp.getPari().equals(" ") //$NON-NLS-1$
					&& !rwkCmp.getPari().equals("0")) //$NON-NLS-1$
			{
				MFSRwkSubAssmDialog myRSAD = new MFSRwkSubAssmDialog(getParentFrame());
				myRSAD.setLocationRelativeTo(getParentFrame()); //~3A
				myRSAD.setVisible(true);
				if (myRSAD.getProceedSelected())
				{
					if (!myRSAD.getSelectedListOption().equals(MFSRwkSubAssmDialog.REWORK_PARTS))
					{
						goForward = true;
					}
					else
					{
						StringBuffer data = new StringBuffer();
						data.append("RTV_IP    "); //$NON-NLS-1$
						data.append("           "); //$NON-NLS-1$
						data.append(rwkCmp.getCwun());
						data.append("UC"); //$NON-NLS-1$
						
						MFSTransaction rtv_ip = new MFSFixedTransaction(data.toString());
						rtv_ip.setActionMessage("Retrieving Subassembly Info, Please Wait..."); //$NON-NLS-1$
						MFSComm.getInstance().execute(rtv_ip, this);
						rc = rtv_ip.getReturnCode();

						if (rc == 0)
						{
							/* load the list model */
							MFSComponentListModel myListModel = new MFSComponentListModel();
							MFSHeaderRec myHeadRec = new MFSHeaderRec();
							myListModel.loadListModel(rtv_ip.getOutput(), myHeadRec);
							if(myHeadRec.isCollectRequired())							//~05A
							{															//~05A
								if(myListModel.loadDataCollection(myHeadRec, rwkCmp.getCwun(),this.getParentFrame()) == 0)	//~05A
								{														//~05A
									if(this.fieldHeaderRec.getDataCollection() != null) //~07A
									{
										if(myHeadRec.getDataCollection().containsMctlComponentCollectedData(rwkCmp.getCwun()))  //~5A)
										{
											fieldHeaderRec.getDataCollection().addMctlComponentCollectedData(rwkCmp.getCwun(), myHeadRec.getDataCollection().getMctlComponentCollectedData(rwkCmp.getCwun()));  //~05A		
										}
										if(myHeadRec.getDataCollection().containsMctlComponentDataToCollect(rwkCmp.getCwun()))	//~05A
										{
											fieldHeaderRec.getDataCollection().addMctlComponentDataToCollect(rwkCmp.getCwun(), myHeadRec.getDataCollection().getMctlComponentDataToCollect(rwkCmp.getCwun()));  //~05A
										}
									}
								}														//~05A
							}															//~05A
							
							/*
							 * ~1 remember the rework Op as taken from the
							 * current operation of the upper most parent
							 */
							MFSRwkSubDialog myRSD = new MFSRwkSubDialog(getParentFrame(),
									this.fieldRwkOp, this.fieldHeaderRec, myListModel);  						
							myRSD.setTitle("Rework Parts in Subassembly - Child Work Unit = " + rwkCmp.getCwun()); //$NON-NLS-1$
							myRSD.setLocationRelativeTo(getParentFrame()); //~3A
							myRSD.setVisible(true);
						}
					}//end of chose second level rwk
				}
				else
				{
					goForward = false;
				}
			}
			else
			{
				goForward = true;
			}

			/*Begin	~05A*/
			if((goForward == true) && (this.fieldHeaderRec.getDataCollection() != null) && (this.fieldHeaderRec.isCollectRequired())) //~07C							
			{										 							
				if(this.fieldHeaderRec.getDataCollection().containsComponentCollection(rwkCmp.getMctl(),rwkCmp.getCrct(), MFSPartDataCollectDialog.COLL_REWORK_REMOVE)) //~06C	
				{																		
					MFSPartDataCollectDialog collectDialog;						
					collectDialog = new MFSPartDataCollectDialog(this.getParentFrame(), this.fieldHeaderRec, MFSPartDataCollectDialog.COLL_REWORK_REMOVE, rwkCmp.getCrct(),rwkCmp.getMctl()); 					
					rc = collectDialog.getFieldReturnCode();					
					if(rc != 0 )													
					{																
						goForward = false;									
					}																														
				}																	
			}																							
			/*End ~05A*/
			
			/* goForward means we've finally found the part we want to rework */
			if (goForward)
			{
				/* get current date and time */
				SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-ddHH.mm.ss"); //$NON-NLS-1$
				Date currTime = new Date();
				String dat = fmt.format(currTime);
				MFSComponentListModel model = (MFSComponentListModel) this.lstComponents.getModel();
				MFSDirectWorkPanel dirWork = getParentFrame().getDirectWorkPanel();
				MFSLogPartDialog myLogPartD = new MFSLogPartDialog(dirWork,
						MFSLogPartDialog.LT_REWORK,
						this.fieldHeaderRec, this.lstComponents, model);
				myLogPartD.setLocationRelativeTo(this);
				myLogPartD.initDisplay();
				myLogPartD.setVisible(true);

				if (myLogPartD.getRwkPartLogged())
				{
					RTV_IRCD rtvIRCD = new RTV_IRCD(this); //~08
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
								MFSRwkIRDialog myRwkIRD = new MFSRwkIRDialog(getParentFrame());
								myRwkIRD.loadRwkIRListModel(rtvIRCD.getOutputIRData());
								myRwkIRD.disableCancel();
								myRwkIRD.setLocationRelativeTo(this); //~3A
								myRwkIRD.setVisible(true);
								if (myRwkIRD.getProceedSelected())
								{
									boolean scDone = false;

									while (!scDone)
									{
										MFSRwkSCDialog myRwkSCD = new MFSRwkSCDialog(getParentFrame());
										myRwkSCD.loadRwkSCListModel(rtvIRCD.getOutputIRData(), myRwkIRD.getSelectedListOption().substring(0, 3));
										myRwkSCD.setLocationRelativeTo(this);
										myRwkSCD.setVisible(true);

										if (myRwkSCD.getProceedSelected())
										{
											MFSRwkCCDialog myRwkCCD = new MFSRwkCCDialog(getParentFrame(), rwkCmp.getDisplayString());
											myRwkCCD.loadRwkCCListModel(rtvIRCD.getOutputCCData(), myRwkIRD.getSelectedListOption().substring(0, 3));
											myRwkCCD.setLocationRelativeTo(this);
											myRwkCCD.setVisible(true);

											if (myRwkCCD.getProceedSelected())
											{
												/* pad comment with blanks to 80 chars */
												String comment = myRwkCCD.getCommentText();
												while (comment.length() < 80)
												{
													comment += " "; //$NON-NLS-1$
												}

												// get the defect loc from the config file
												String defectloc = config.getConfigValue("DEFECTLOC") //$NON-NLS-1$
														.concat("                         ").substring(0, 25); //$NON-NLS-1$
												if (defectloc.substring(0, 9).equals(MFSConfig.NOT_FOUND))
												{
													defectloc = "                         "; //$NON-NLS-1$
													readyToCallUPDT_RA = true;
												}
												else
												{
													if (rwkCmp.getQnty().equals("00000")) //$NON-NLS-1$
													{
														readyToCallUPDT_RA = true;
													}
													else
													{
														MFSGenericListDialog myQuestionJD = new MFSGenericListDialog(
																getParentFrame(),
																"Bad Part?", //$NON-NLS-1$
																"Is this part defective?"); //$NON-NLS-1$
														myQuestionJD.setSizeSmall();
														myQuestionJD.loadAnswerListModel("NO YES", 3); //$NON-NLS-1$
														myQuestionJD.setDefaultSelection("NO "); //$NON-NLS-1$
														myQuestionJD.setLocationRelativeTo(this);
														myQuestionJD.setVisible(true);

														if (myQuestionJD.getProceedSelected())
														{
															if (myQuestionJD.getSelectedListOption().substring(0, 1).equals("Y")) //$NON-NLS-1$
															{
																badPartInd = "Y"; //$NON-NLS-1$
																MFSGenericListDialog myQuestionJD2 = new MFSGenericListDialog(
																		getParentFrame(),
																		"IBM Product Defect?", //$NON-NLS-1$
																		"Is this an IBM defective part?"); //$NON-NLS-1$
																myQuestionJD2.setSizeSmall();
																myQuestionJD2.loadAnswerListModel("NO YES", 3); //$NON-NLS-1$
																myQuestionJD2.setDefaultSelection("NO "); //$NON-NLS-1$
																myQuestionJD2.setLocationRelativeTo(this);
																myQuestionJD2.setVisible(true);

																if (myQuestionJD2.getProceedSelected())
																{
																	readyToCallUPDT_RA = true;
																	if (myQuestionJD2.getSelectedListOption().substring(0, 1).equals("Y")) //$NON-NLS-1$
																	{
																		IBMDefect = "Y"; //$NON-NLS-1$
																	}
																	else
																	{
																		IBMDefect = "N"; //$NON-NLS-1$
																	}
																}
															}
															else
															{
																badPartInd = "N"; //$NON-NLS-1$
																IBMDefect = "N"; //$NON-NLS-1$
																readyToCallUPDT_RA = true;
															}
														}
													}//end of non-zero quantity
												}// end of defect loc found

												if (readyToCallUPDT_RA)
												{
													StringBuffer ra_data = new StringBuffer(1024);
													ra_data.append("UPDT_RA   "); //$NON-NLS-1$
													/* pad with *'s to 512 chars */
													while (ra_data.length() < 512)
													{
														ra_data.append("*"); //$NON-NLS-1$
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
													ra_data.append(this.fieldRwkOp);
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
													ra_data.append(myRwkIRD.getSelectedListOption().substring(0, 3));
													ra_data.append(myRwkSCD.getSelectedListOption().substring(5, 7));
													ra_data.append(myRwkCCD.getSelectedListOption().substring(0, 2));
													ra_data.append("1"); // ifix //$NON-NLS-1$
													ra_data.append(comment);
													ra_data.append(badPartInd);
													ra_data.append(IBMDefect);
													ra_data.append(rwkCmp.getCrct());
													ra_data.append(defectloc);
													ra_data.append(0); //~4A
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
														IGSMessageBox.showOkMB(getParentFrame(), null, updt_ra.getErms(), null);
													}
													else
													{
														// print out the NCM tags if they are configured
														if (badPartInd.equals("Y")) //$NON-NLS-1$
														{
															NCMTagSequence = updt_ra.getOutput().substring(0, 8);
															this.fieldCurrentCompRec.setRejs(NCMTagSequence);
															if (config.containsConfigEntry("NCMTAG")) //$NON-NLS-1$
															{
																MFSPrintingMethods.ncmtag(NCMTagSequence,1,getParentFrame());
															}
															if (config.containsConfigEntry("NCMSHEET")) //$NON-NLS-1$
															{
																MFSPrintingMethods.ncmsheet(NCMTagSequence,1,getParentFrame());
															}
															if (config.containsConfigEntry("NCMBIGTAG")) //$NON-NLS-1$
															{
																MFSPrintingMethods.ncmbigtag(NCMTagSequence,1,getParentFrame());
															}
														}
														else if (rwkCmp.getQnty().equals("00000")) //$NON-NLS-1$
														{
															String erms = "This is a 0 quantity Part.  No NCM tag will be printed."; //$NON-NLS-1$
															IGSMessageBox.showOkMB(getParentFrame(), null, erms, null);
														}

														MFSRwkPrintDialog myRwkPrintD = new MFSRwkPrintDialog(getParentFrame());
														if (config.containsConfigEntry("FAILTAG")) //$NON-NLS-1$
														{
															myRwkPrintD.setPBFailTagEnabled(true);
														}
														if (config.containsConfigEntry("FAILREPORT")) //$NON-NLS-1$
														{
															myRwkPrintD.setPBFailReportEnabled(true);
														}
														myRwkPrintD.setLocationRelativeTo(this);
														myRwkPrintD.setVisible(true);

														if (myRwkPrintD.getPressedTag())
														{
															String ircode = myRwkIRD.getSelectedListOption().substring(0, 3)
																	+ myRwkSCD.getSelectedListOption().substring(5,7)
																	+ myRwkCCD.getSelectedListOption().substring(0,2);

															MFSPrintingMethods.failtag(
																	rwkCmp.getCdes(),
																	rwkCmp.getInpn(),
																	rwkCmp.getInsq(),
																	rwkCmp.getInec(),
																	this.fieldHeaderRec.getIdss(),
																	this.fieldHeaderRec.getMctl(),
																	ircode,
																	rwkCmp.getPrtd(),
																	this.fieldHeaderRec.getMmdl(),
																	rwkCmp.getMatp(),
																	config.get8CharCell(),
																	dat.substring(0,10),
																	this.fieldHeaderRec.getMfgn(),
																	rwkCmp.getMcsn(),
																	config.getConfigValue("USER").concat("          ").substring(0,10), //$NON-NLS-1$ //$NON-NLS-2$
																	dat.substring(10,18),
																	comment,
																	1,
																	getParentFrame());
														}
														else if (myRwkPrintD.getPressedReport())
														{
															String ircode1 = myRwkIRD.getSelectedListOption();
															String ircode2 = myRwkSCD.getSelectedListOption();
															String ircode3 = myRwkCCD.getSelectedListOption();

															MFSPrintingMethods.failreport(
																	rwkCmp.getCdes(),
																	this.fieldHeaderRec.getPrln(),
																	this.fieldHeaderRec.getNmbr(),
																	this.fieldHeaderRec.getIdss(),
																	this.fieldHeaderRec.getMctl(),
																	ircode1,
																	ircode2,
																	ircode3,
																	config.get8CharCell(),
																	dat.substring(0, 10),
																	this.fieldHeaderRec.getMfgn(),
																	config.getConfigValue("USER").concat("          ").substring(0, 10), //$NON-NLS-1$ //$NON-NLS-2$
																	dat.substring(10, 18),
																	comment,
																	1,
																	getParentFrame());
														}

														scDone = true;
														irDone = true;
													}//good return from UPDT_RA
												}//end of readyToCallUPDTRA
											}// end of if(myRwkCCJD.getPressedLog())
										}// end of if(myRwkSCJD.getPressedEnter())
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
						else
						{
							/* pad comment with blanks to 80 chars */
							String comment = ""; //$NON-NLS-1$
							while (comment.length() < 80)
							{
								comment += " "; //$NON-NLS-1$
							}

							// get the defect loc from the config file
							String defectloc = config.getConfigValue("DEFECTLOC") //$NON-NLS-1$
									.concat("                         ").substring(0, 25); //$NON-NLS-1$
							if (defectloc.substring(0, 9).equals(MFSConfig.NOT_FOUND))
							{
								defectloc = "                         "; //$NON-NLS-1$
								readyToCallUPDT_RA = true;
							}
							else
							{
								if (rwkCmp.getQnty().equals("00000")) //$NON-NLS-1$
								{
									readyToCallUPDT_RA = true;
								}
								else
								{
									MFSGenericListDialog myQuestionJD = new MFSGenericListDialog(
											getParentFrame(), "Bad Part?", //$NON-NLS-1$
											"Is this part defective?"); //$NON-NLS-1$
									myQuestionJD.setSizeSmall();
									myQuestionJD.loadAnswerListModel("NO YES", 3); //$NON-NLS-1$
									myQuestionJD.setDefaultSelection("NO "); //$NON-NLS-1$
									myQuestionJD.setLocationRelativeTo(this);
									myQuestionJD.setVisible(true);

									if (myQuestionJD.getProceedSelected())
									{

										if (myQuestionJD.getSelectedListOption().substring(0, 1).equals("Y")) //$NON-NLS-1$
										{
											badPartInd = "Y"; //$NON-NLS-1$
											MFSGenericListDialog myQuestionJD2 = new MFSGenericListDialog(
													getParentFrame(),
													"IBM Product Defect?", //$NON-NLS-1$
													"Is this an IBM defective part?"); //$NON-NLS-1$
											myQuestionJD2.setSizeSmall();
											myQuestionJD2.loadAnswerListModel("NO YES", 3); //$NON-NLS-1$
											myQuestionJD2.setDefaultSelection("NO "); //$NON-NLS-1$
											myQuestionJD2.setLocationRelativeTo(this);
											myQuestionJD2.setVisible(true);

											if (myQuestionJD2.getProceedSelected())
											{
												readyToCallUPDT_RA = true;
												if (myQuestionJD2.getSelectedListOption().substring(0, 1).equals("Y")) //$NON-NLS-1$
												{
													IBMDefect = "Y"; //$NON-NLS-1$
												}
												else
												{
													IBMDefect = "N"; //$NON-NLS-1$
												}
											}
										}
										else
										{
											badPartInd = "N"; //$NON-NLS-1$
											IBMDefect = "N"; //$NON-NLS-1$
											readyToCallUPDT_RA = true;
										}
									}
								}//end of non-zero quantity
							}
							
							if (readyToCallUPDT_RA)
							{
								StringBuffer ra_data = new StringBuffer(1024);
								ra_data.append("UPDT_RA   "); //$NON-NLS-1$
								/* pad with *'s to 512 chars */
								while (ra_data.length() < 512)
								{
									ra_data.append("*"); //$NON-NLS-1$
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
								ra_data.append(this.fieldRwkOp);
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
								ra_data.append("       "); //$NON-NLS-1$
								ra_data.append(" "); // ifix //$NON-NLS-1$
								ra_data.append(comment);
								ra_data.append(badPartInd);
								ra_data.append(IBMDefect);
								ra_data.append(rwkCmp.getCrct());
								ra_data.append(defectloc);
								ra_data.append(0); //~4A
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
									IGSMessageBox.showOkMB(getParentFrame(), null, updt_ra.getErms(), null);
								}
								else
								{
									// print out the NCM tags if they are configured
									if (badPartInd.equals("Y")) //$NON-NLS-1$
									{
										NCMTagSequence = updt_ra.getOutput().substring(0, 8);
										this.fieldCurrentCompRec.setRejs(NCMTagSequence);
										if (config.containsConfigEntry("NCMTAG")) //$NON-NLS-1$
										{
											MFSPrintingMethods.ncmtag(NCMTagSequence, 1, getParentFrame());
										}

										if (config.containsConfigEntry("NCMSHEET")) //$NON-NLS-1$
										{
											MFSPrintingMethods.ncmsheet(NCMTagSequence, 1, getParentFrame());
										}
									}
									else if (rwkCmp.getQnty().equals("00000")) //$NON-NLS-1$
									{
										String erms = "This is a 0 quantity Part.  No NCM tag will be printed."; //$NON-NLS-1$
										IGSMessageBox.showOkMB(getParentFrame(), null, erms, null);
									}
								}
							}//end of readyToCallUPDTRA
						}//end of no IRCodes
					}//end of good return from RTV_IRCD
				}//end of if (myLogPartD.getRwkPartLogged())
				else
				{
								
					if(this.fieldHeaderRec.getDataCollection() != null) //~07A
					{
						//Begin ~05A
						//Check if the remove transaction was canceled before collect new data, and restore the data that wasn't removed
						if(fieldHeaderRec.getDataCollection().getW_partcoll_remove() != null )
						{					
							fieldHeaderRec.getDataCollection().setW_partcoll_remove(null);
						}
						//End ~05A
					}					
				}
			}
		}//end of try
		catch (Exception e)
		{
			this.setCursor(Cursor.getDefaultCursor());
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}

		this.toFront();
		this.lstComponents.requestFocusInWindow();
	}

	/**
	 * If the specified <code>MFSComponentRec</code> is not the current
	 * <code>MFSComponentRec</code>:
	 * <ol>
	 * <li>unregisters this dialog as a <code>PropertyChangeListener</code>
	 * for the current <code>MFSComponentRec</code></li>
	 * <li>sets the specified <code>MFSComponentRec</code> as the current
	 * <code>MFSComponentRec</code></li>
	 * <li>registers this dialog as a <code>PropertyChangeListener</code> for
	 * the specified <code>MFSComponentRec</code></li>
	 * <li>sets the text of the phrase <code>JTextPane</code></li>
	 * <li>sets the text of the installed parts <code>JTextPane</code></li>
	 * </ol>
	 * @param componentRec the new current <code>MFSComponentRec</code>
	 */
	protected void setCurrentCompRec(MFSComponentRec componentRec)
	{
		if (this.fieldCurrentCompRec != componentRec)
		{
			try
			{
				/* Stop listening for events from the current MFSComponentRec */
				if (this.fieldCurrentCompRec != null)
				{
					this.fieldCurrentCompRec.removePropertyChangeListener(this);
				}

				this.fieldCurrentCompRec = componentRec;

				/* Listen for events from the new MFSComponentRec */
				if (this.fieldCurrentCompRec != null)
				{
					this.fieldCurrentCompRec.addPropertyChangeListener(this);
					this.tpPhrase.setText(this.fieldCurrentCompRec.getPlphtp());
					this.tpInsParts.setText(this.fieldCurrentCompRec.getInstalledParts());
				}
			}
			catch (Throwable t)
			{
				t.printStackTrace();
			}
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
			if (source == this.pbReturn)
			{
				this.dispose();
			}
			else if (source == this.pbRework)
			{
				this.rework();
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
			if (source == this.pbRework)
			{
				this.pbRework.requestFocusInWindow();
				this.pbRework.doClick();
			}
			else if (source == this.pbReturn)
			{
				this.pbReturn.requestFocusInWindow();
				this.pbReturn.doClick();
			}
		}
		else if (keyCode == KeyEvent.VK_F6)
		{
			this.pbRework.requestFocusInWindow();
			this.pbRework.doClick();
		}
		else if (keyCode == KeyEvent.VK_F12)
		{
			this.pbReturn.requestFocusInWindow();
			this.pbReturn.doClick();
		}
		else if (keyCode == KeyEvent.VK_PAGE_DOWN)
		{
			MFSScroller.scrollDown(this.spParts, this.lstComponents);
			ke.consume();
		}
		else if (keyCode == KeyEvent.VK_PAGE_UP)
		{
			MFSScroller.scrollUp(this.spParts, this.lstComponents);
			ke.consume();
		}
	}

	/**
	 * Invoked when a bound property is changed.
	 * @param evt the <code>PropertyChangeEvent</code>
	 */
	public void propertyChange(PropertyChangeEvent evt)
	{
		if (evt.getSource() == this.fieldCurrentCompRec)
		{
			if (evt.getPropertyName().equals("plphtp")) //$NON-NLS-1$
			{
				this.tpPhrase.setText(this.fieldCurrentCompRec.getPlphtp());
			}
			else if (evt.getPropertyName().equals("installedParts")) //$NON-NLS-1$
			{
				this.tpInsParts.setText(this.fieldCurrentCompRec.getInstalledParts());
			}
		}
		//~2D Removed MFSComponentListModel property related changes
	}

	/**
	 * Invoked when the value of a list selection changes.
	 * @param e the <code>ListSelectionEvent</code>
	 */
	public void valueChanged(ListSelectionEvent e)
	{
		if (e.getSource() == this.lstComponents)
		{
			setCurrentCompRec((MFSComponentRec) this.lstComponents.getSelectedValue());
		}
	}

	/**
	 * Invoked the first time a window is made visible.
	 * <p>
	 * Requests the focus for the {@link #lstComponents}.
	 * @param we the <code>WindowEvent</code>
	 */
	public void windowOpened(WindowEvent we)
	{
		if (we.getSource() == this)
		{
			this.lstComponents.requestFocusInWindow();
		}
	}
}
