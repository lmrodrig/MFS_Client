/* @ Copyright IBM Corporation 2005, 2009. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2006-06-22   ~1 34144JJ  JL. Woodward     -Add new logic to support the new config file entry PALLET
 *                                           -Changed updtcinc logic a little bit. Added support for RSSPCPP printing method
 *                                           -Cleanup code, unused variables and stuff.
 * 2006-08-03   ~2 35789JM  JL. Woodward     -Change the source param to "REPRINT" when
 *                                            calling the rsspcpp printing method.
 * 2006-08-07   ~3 35859JM  JL. Woodward     -Do not call any printing method when creating a new
 *                                            container if pallet logic is active.
 * 2006-09-18   ~4 34987JM  MSBARTH          -Add new logic to disable unwanted buttons
 * 2006-10-19   ~5 35927JM  R Prechel        -Removed unused constructors.
 *                                           -Removed setMyDWJP and main methods.
 * 2007-01-25      34242JR  R Prechel        -Java 5 version
 * 2007-04-05   ~6 34242JR  R Prechel        -Change enum to enumeration
 * 2007-07-20   ~7 38768JL  Martha Luna      -Change rsspcpp method by generic pcpplabels method
 * 2007-10-30   ~8 39624JM  Martha Luna      -Inicializate cincCount with 1
 * 2008-04-07   ~9 37616JL  D Pietrasik      -Add user to rtv_cntr
 * 2008-05-20  ~10 41268MZ  D Pietrasik      -Support MFSPrintingMethods.caseContent change
 * 2009-07-20  ~11 41330JL	Sayde Alcantara	 -New CASECONTENTMEF label
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.dialog;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.util.Enumeration;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.plaf.basic.BasicTreeUI;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import com.ibm.rchland.mfgapps.client.utils.messagebox.IGSMessageBox;
import com.ibm.rchland.mfgapps.mfsclient.MFSActionableDialog;
import com.ibm.rchland.mfgapps.mfsclient.MFSFrame;
import com.ibm.rchland.mfgapps.mfsclient.barcode.MFSBCBarcodeDecoder;
import com.ibm.rchland.mfgapps.mfsclient.barcode.MFSBCIndicatorValue;
import com.ibm.rchland.mfgapps.mfsclient.barcode.MFSBCPartObject;
import com.ibm.rchland.mfgapps.mfsclient.panel.MFSDirectWorkPanel;
import com.ibm.rchland.mfgapps.mfsclient.struct.MFSCntrNode;
import com.ibm.rchland.mfgapps.mfsclient.struct.MFSComponentListModel;
import com.ibm.rchland.mfgapps.mfsclient.struct.MFSComponentRec;
import com.ibm.rchland.mfgapps.mfsclient.struct.MFSHeaderRec;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSConstants;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSDetermineLabel;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSPrintingMethods;
import com.ibm.rchland.mfgapps.mfscommon.MFSConfig;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSComm;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSFixedTransaction;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSTransaction;

/**
 * <code>MFSCntrInCntrDialog</code> is an <code>MFSActionableDialog</code>
 * that displays the container in container tree.
 * @author The MFS Client Development Team
 */
public class MFSCntrInCntrDialog
	extends MFSActionableDialog
	implements TreeExpansionListener
{
	private static final long serialVersionUID = 1L;
	/** for use in fixed position transactions */
	private static final String BLANK_CNTR = "          "; //$NON-NLS-1$
	
	/** The container <code>JTree</code>. */
	private JTree cntrTree = createJTree();

	/** The <code>JScrollPane</code> that contains the tree. */
	protected JScrollPane spCntr = new JScrollPane(this.cntrTree,
			ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
			ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

	/** The Add Child (F2) <code>JButton</code>. */
	private JButton pbAddChild = createButton("F2=Add Child");

	/** The Exit (F3) <code>JButton</code>. */
	private JButton pbExit = createButton("F3 = Exit");

	/** The New Pallet/Cntr (F4) <code>JButton</code>. */
	private JButton pbNew;

	/** The Undo Child (F7) <code>JButton</code>. */
	private JButton pbUndoChild = createButton("F7=Undo Child");

	/** The Print/Reprint (F9) <code>JButton</code>. */
	private JButton pbReprint;

	/** The Dlt Pallet/Cntr (F11) <code>JButton</code>. */
	private JButton pbDelete;

	/** <code>true</code> iff the "PALLET" config entry is set. */
	final private boolean fieldPalletLogic = MFSConfig.getInstance().containsConfigEntry("PALLET"); //$NON-NLS-1$
	
	/** The root <code>DefaultMutableTreeNode</code>. */
	private DefaultMutableTreeNode fieldRootNode;

	/** The <code>MFSHeaderRec</code> for the container. */
	private MFSHeaderRec fieldHeaderRec;

	/** The <code>MFSDirectWorkPanel</code> that caused this dialog to be displayed. */
	private MFSDirectWorkPanel fieldDirWork;
	
	/** The <code>MFSComponentListModel</code> for the <code>MFSDirectWorkPanel</code>. */
	private MFSComponentListModel fieldCompListModel;

	
	/**
	 * Constructs a new <code>MFSCntrInCntrDialog</code>.
	 * @param parent the <code>MFSFrame</code> that caused this
	 *        <code>MFSCntrInCntrDialog</code> to be displayed
	 * @param headerRec the <code>MFSHeaderRec</code> for the container
	 */
	public MFSCntrInCntrDialog(MFSFrame parent, MFSHeaderRec headerRec)
	{
		this(parent, headerRec, new MFSDirectWorkPanel(parent, null), null);
	}
	
	/**
	 * Constructs a new <code>MFSCntrInCntrDialog</code>.
	 * @param parent the <code>MFSFrame</code> that caused this
	 *        <code>MFSCntrInCntrDialog</code> to be displayed
	 * @param headerRec the <code>MFSHeaderRec</code> for the container
	 * @param dirWork the <code>MFSDirectWorkPanel</code> that caused this dialog to be displayed
	 * @param clm the <code>MFSComponentListModel</code> for the <code>MFSDirectWorkPanel</code>
	 */
	public MFSCntrInCntrDialog(MFSFrame parent, MFSHeaderRec headerRec, MFSDirectWorkPanel dirWork, MFSComponentListModel clm)
	{
		super(parent, "Container In Container");
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

		this.fieldHeaderRec = headerRec;
		this.fieldDirWork = dirWork;
		this.fieldCompListModel = clm;
		
		if (this.fieldPalletLogic) //$NON-NLS-1$
		{
			this.pbNew = createButton("F4=New Pallet");
			this.pbReprint = createButton("F9=Print");
			this.pbDelete = createButton("F11=Dlt Pallet");
		}
		else
		{
			this.pbNew = createButton("F4=New Cntr");
			this.pbReprint = createButton("F9=Reprint");
			this.pbDelete = createButton("F11=Dlt Cntr");
		}

		createLayout();
		addMyListeners();
	}

	/** Adds this dialog's <code>Component</code>s to the layout. */
	private void createLayout()
	{
		JLabel label1 = new JLabel("Container   Order  Work Unit Type");
		label1.setFont(MFSConstants.MEDIUM_MONOSPACED_FONT);
		
		JLabel label2 = new JLabel("XXXXXXXXXX  XXXXXX  XXXXXXXX  X",
				new ImageIcon(getClass().getResource("/images/boxFull.gif")), //$NON-NLS-1$
				SwingConstants.LEADING);
		label2.setFont(MFSConstants.MEDIUM_MONOSPACED_FONT);
		label2.setOpaque(true);
		label2.setBackground(Color.white);
		label2.setForeground(MFSConstants.PRIMARY_FOREGROUND_COLOR);
		
		JLabel exampleLabel = new JLabel("Example:");
		exampleLabel.setFont(MFSConstants.MEDIUM_DIALOG_FONT);
		exampleLabel.setForeground(MFSConstants.PRIMARY_FOREGROUND_COLOR);
		
		GridBagConstraints gbc = new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0,
				GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0);
		
		JPanel labelPanel = new JPanel(new GridBagLayout());
		
		labelPanel.add(label1, gbc);
		gbc.ipadx = 15;
		gbc.gridy++;
		labelPanel.add(label2, gbc);
		
		gbc.gridx = 0;
		gbc.ipadx = 5;
		labelPanel.add(exampleLabel, gbc);
		
		JPanel buttonPanel = new JPanel(new GridLayout(2, 3, 75, 10));
		buttonPanel.add(this.pbAddChild);
		buttonPanel.add(this.pbExit);
		buttonPanel.add(this.pbNew);
		buttonPanel.add(this.pbUndoChild);
		buttonPanel.add(this.pbReprint);
		buttonPanel.add(this.pbDelete);
		
		JPanel contentPane = new JPanel(new GridBagLayout());
		contentPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0.0;
		gbc.weighty = 0.0;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(10, 0, 0, 0);
		
		contentPane.add(labelPanel, gbc);
		
		gbc.gridy = 2;
		contentPane.add(buttonPanel, gbc);
		
		gbc.gridy = 1;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		contentPane.add(this.spCntr, gbc);
		contentPane.add(this.spCntr, gbc);

		setContentPane(contentPane);
		this.spCntr.setPreferredSize(new Dimension(675, 350));
		pack();
	}

	/** 
	 * Creates a new <code>JTree</code>.
	 * @return the <code>JTree</code>
	 */
	private JTree createJTree()
	{
		UIDefaults uid = UIManager.getDefaults();
		uid.put("Tree.hash", Color.black); //$NON-NLS-1$
		this.fieldRootNode = new DefaultMutableTreeNode();

		JTree result = new JTree(this.fieldRootNode);
		result.setRowHeight(40);
		result.setRootVisible(false);
		result.setFont(MFSConstants.MEDIUM_MONOSPACED_FONT);
		result.putClientProperty("JTree.lineStyle", "Angled"); //$NON-NLS-1$ //$NON-NLS-2$
		result.getSelectionModel().setSelectionMode(
				TreeSelectionModel.SINGLE_TREE_SELECTION);

		DefaultTreeCellRenderer cr = (DefaultTreeCellRenderer) result.getCellRenderer();
		cr.setOpenIcon(new ImageIcon(getClass().getResource("/images/boxFull.gif"))); //$NON-NLS-1$
		cr.setClosedIcon(new ImageIcon(getClass().getResource("/images/boxFull.gif"))); //$NON-NLS-1$
		cr.setLeafIcon(new ImageIcon(getClass().getResource("/images/boxFull.gif"))); //$NON-NLS-1$
		cr.setBackgroundSelectionColor(Color.lightGray);

		BasicTreeUI ui = (BasicTreeUI) result.getUI();
		ui.setRightChildIndent(50);
		ui.setExpandedIcon(null);

		return result;
	}

	/** Adds the listeners to this dialog's <code>Component</code>s. */
	private void addMyListeners()
	{
		this.pbAddChild.addActionListener(this);
		this.pbExit.addActionListener(this);
		this.pbNew.addActionListener(this);
		this.pbUndoChild.addActionListener(this);
		this.pbReprint.addActionListener(this);
		this.pbDelete.addActionListener(this);

		this.cntrTree.addTreeExpansionListener(this);

		this.pbAddChild.addKeyListener(this);
		this.pbExit.addKeyListener(this);
		this.pbNew.addKeyListener(this);
		this.pbUndoChild.addKeyListener(this);
		this.pbReprint.addKeyListener(this);
		this.pbDelete.addKeyListener(this);

		this.cntrTree.addKeyListener(this);
		this.spCntr.addKeyListener(this);
	}

	/** Invoked when {@link #pbAddChild} is selected. */
	private void addChild()
	{
		try
		{
			DefaultMutableTreeNode selection = (DefaultMutableTreeNode) this.cntrTree
					.getSelectionPath().getLastPathComponent();
			MFSCntrNode selObj = (MFSCntrNode) selection.getUserObject();
			int row = findRow(selObj.getCntr());

			String child = JOptionPane.showInputDialog(this,
					"Scan container to put inside of " + selObj.getCntr(),
					"Add Child Container", JOptionPane.QUESTION_MESSAGE);
			if (child != null)
			{
				MFSBCBarcodeDecoder barcode = MFSBCBarcodeDecoder.getInstance();
				barcode.setMyBarcodeInput(child);
				barcode.setMyBCPartObject(new MFSBCPartObject());
				/* setup barcode indicator values */
				MFSBCIndicatorValue bcIndVal = new MFSBCIndicatorValue();
				barcode.setMyBCIndicatorValue(bcIndVal);
				barcode.decodeBarcodeFor(this);

				/* CNTR */
				if (!(barcode.getBCMyPartObject().getCT().equals("")))
				{
					child = barcode.getBCMyPartObject().getCT();
				}
				/* PN - 62 AI code */
				else if (!(barcode.getBCMyPartObject().getPN().equals("")))
				{
					child = barcode.getBCMyPartObject().getPN();
				}

				child = "00000000000" + child.toUpperCase();
				child = child.substring(child.length() - 10);
				if (child.equals(selObj.getCntr()))
				{
					JOptionPane.showMessageDialog(this,
							"Cannot Add Container to itself.", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
				else
				{
					DefaultMutableTreeNode childNode = findNode(child);
					if (childNode == null)
					{
						JOptionPane.showMessageDialog(this, "Child Container Not Found.",
								"Error", JOptionPane.ERROR_MESSAGE);
					}
					else
					{
						boolean child_cntr_okay = true;
						/*
						 * now make sure that it doesn't exist somewhere in the
						 * path already - i.e. guarantee that we don't have a
						 * circular parentage - get the selection path and step
						 * through it one node at time to check each cntr
						 * against the one scanned in - if a cntr is in the
						 * path, don't allow it to be added as a child
						 */
						TreePath path = this.cntrTree.getSelectionPath();
						int path_count = 1;
						while (path_count < path.getPathCount() && child_cntr_okay)
						{

							DefaultMutableTreeNode curr_component = (DefaultMutableTreeNode) path
									.getPathComponent(path_count);
							MFSCntrNode curCntr = (MFSCntrNode) curr_component
									.getUserObject();
							if (child.equals(curCntr.getCntr()))
							{
								JOptionPane.showMessageDialog(
												this,
												"Circular Parentage Found - Child Container Cannot be an Ancestor of Itself.",
												"Error", JOptionPane.ERROR_MESSAGE);
								child_cntr_okay = false;
							}
							else
							{
								path_count++;
							}
						}

						if (child_cntr_okay)
						{
							MFSCntrNode cn = (MFSCntrNode) childNode.getUserObject();
							cn.setParentCntr(selObj.getCntr());
							cn.setParentMctl(selObj.getMctl());
							selection.add(childNode);
						}
					}
				}
			}

			refreshTree();

			this.toFront();
			this.cntrTree.setSelectionRow(row);
			this.cntrTree.requestFocusInWindow();
		}
		catch (Exception e)
		{
			this.setCursor(Cursor.getDefaultCursor());
			IGSMessageBox.showOkMB(this, null, null, e);

			refreshTree();

			this.toFront();
			this.cntrTree.requestFocusInWindow();
		}
	}

	/** Invoked when {@link #pbDelete} is selected. */
	private void deleteCntr()
	{
		try
		{
			int rc = 0;
			MFSComponentRec cmp;
			int index = 0;
			boolean empty = true;
			String errorString = ""; //$NON-NLS-1$

			DefaultMutableTreeNode selection = (DefaultMutableTreeNode) this.cntrTree
					.getSelectionPath().getLastPathComponent();
			MFSCntrNode cn = (MFSCntrNode) selection.getUserObject();

			if (selection.isLeaf())
			{
				while ((index < this.fieldCompListModel.size()) && empty)
				{
					cmp = this.fieldCompListModel.getComponentRecAt(index);
					if ((cmp.getIdsp().equals("I") || cmp.getIdsp().equals("R")) //$NON-NLS-1$ //$NON-NLS-2$
							&& (cmp.getCntr().equals(cn.getCntr())))
					{
						empty = false;
					}
					index++;
				}

				if (empty)
				{
					StringBuffer data = new StringBuffer();
					data.append("RTV_CNTR  "); //$NON-NLS-1$
					data.append(this.fieldDirWork.getCurrMctl());
					data.append("D"); //$NON-NLS-1$
					data.append(cn.getCntr());
					data.append(MFSConfig.getInstance().get8CharUser());  /* ~9A */

					MFSTransaction rtv_cntr = new MFSFixedTransaction(data.toString());
					rtv_cntr.setActionMessage("Deleting Container, Please Wait...");
					MFSComm.getInstance().execute(rtv_cntr, this);
					rc = rtv_cntr.getReturnCode();

					if (rc == 0)
					{
						/* delete the cntr from the tree */
						selection.removeFromParent();
						refreshTree();

						// if deleted cntr is the current cntr, then assign a new one 
						if (cn.getCntr().equals(this.fieldHeaderRec.getCntr())
								&& !rtv_cntr.getOutput().equals("")) //$NON-NLS-1$
						{
							data = new StringBuffer();
							data.append("RTV_CNTR  "); //$NON-NLS-1$
							data.append(this.fieldDirWork.getCurrMctl());
							data.append("A"); //$NON-NLS-1$
							data.append(rtv_cntr.getOutput());
							data.append(MFSConfig.getInstance().get8CharUser());  /* ~9A */

							MFSTransaction rtv_cntr2 = new MFSFixedTransaction(data.toString());
							rtv_cntr2.setActionMessage("Assigning Container, Please Wait...");
							MFSComm.getInstance().execute(rtv_cntr2, this);
							rc = rtv_cntr2.getReturnCode();

							if (rc == 0)
							{
								this.fieldDirWork.updt_cntr(rtv_cntr2.getOutput());
								this.fieldDirWork.update(getGraphics());
								this.update(getGraphics());
							}
							else
							{
								errorString = rtv_cntr2.getErms();
							}
						}
					}
					else
					{
						errorString = rtv_cntr.getErms();
					}
				}
				else
				{
					errorString = "You cannot Delete this Container because it is not empty.";
					rc = 10;
				}
			}
			else
			{
				errorString = "You cannot Delete this Container because it is not empty.";
				rc = 10;
			}

			if (rc != 0)
			{
				IGSMessageBox.showOkMB(this, null, errorString, null);
			}
		}
		catch (Exception e)
		{
			this.setCursor(Cursor.getDefaultCursor());
			IGSMessageBox.showOkMB(this, null, null, e);
		}

		this.toFront();
		this.cntrTree.setSelectionRow(0);
		this.cntrTree.scrollRowToVisible(0);
		this.cntrTree.requestFocusInWindow();
	}
	
	//~4
	/**
	 * This procedure disables a configured button.
	 * <p>
	 * Available buttons to disable {Add,Delete,New,Reprint,Undo}
	 * @param button the name of the button to disable
	 */
	public void disableButton(String button)
	{
		if (button.equalsIgnoreCase("add")) //$NON-NLS-1$
		{
			this.pbAddChild.removeActionListener(this);
			this.pbAddChild.setEnabled(false);
		}
		else if (button.equalsIgnoreCase("delete")) //$NON-NLS-1$
		{
			this.pbDelete.removeActionListener(this);
			this.pbDelete.setEnabled(false);
		}
		else if (button.equalsIgnoreCase("new")) //$NON-NLS-1$
		{
			this.pbNew.removeActionListener(this);
			this.pbNew.setEnabled(false);
		}
		else if (button.equalsIgnoreCase("reprint")) //$NON-NLS-1$
		{
			this.pbReprint.removeActionListener(this);
			this.pbReprint.setEnabled(false);
		}
		else if (button.equalsIgnoreCase("undo")) //$NON-NLS-1$
		{
			this.pbUndoChild.removeActionListener(this);
			this.pbUndoChild.setEnabled(false);
		}
	}
	
	//~1 Renamed from executeUPDTCINC to executeSingleUpdtCinc
	/**
	 * Execute a transaction using the specified data.
	 * @param data the data for the transaction call
	 * @return the return code
	 */
	private int executeSingleUpdtCinc(String data) 
	{
		int rc = 0;
		
		MFSTransaction updt_cinc = new MFSFixedTransaction(data);
		updt_cinc.setActionMessage("Updating Container Info, Please Wait...");
		MFSComm.getInstance().execute(updt_cinc, this);
		rc = updt_cinc.getReturnCode();
			
		if (rc != 0)
		{
			IGSMessageBox.showOkMB(this, null, updt_cinc.getErms(), null);
		}
		return rc;
	}

	//~1 New method, updtcinc
	/** Updates the container. */
	@SuppressWarnings("rawtypes")
	private void updtcinc()
	{
		try
		{
			int rc = 0;
			int cincCount = 1; //~8C
			final String blank64 = "                                                                "; //$NON-NLS-1$
			StringBuffer dataBuffer = new StringBuffer(128);
			dataBuffer.append("UPDT_CINC D"); //$NON-NLS-1$
			dataBuffer.append(this.fieldHeaderRec.getMfgn());
			dataBuffer.append(this.fieldHeaderRec.getIdss());
			dataBuffer.append(blank64);
			
			StringBuffer data = new StringBuffer(128);
			data.append(dataBuffer.substring(0, 64));

			/* set Cursor to waiting */
			this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		
			//~6C Change enum to enumeration
			Enumeration enumeration = this.fieldRootNode.breadthFirstEnumeration();
			while (enumeration.hasMoreElements() && rc == 0)
			{
				DefaultMutableTreeNode tNode = (DefaultMutableTreeNode)enumeration.nextElement();
				MFSCntrNode cntr = (MFSCntrNode)tNode.getUserObject();
				if (cntr != null && !cntr.getParentCntr().equals("          ")) //$NON-NLS-1$
				{
					StringBuffer dat = new StringBuffer();
					dat.append(cntr.getParentCntr());
					dat.append(cntr.getParentMctl());
					dat.append(cntr.getCntr());
					dat.append(cntr.getMctl());
					dat.append(blank64);
					
					data.append(dat.substring(0, 64));
					cincCount++;
				}
				//need to send separate buffers here, 63 is the max number
				if(cincCount == 62)
				{
					rc = executeSingleUpdtCinc(data.toString());	
					if(rc == 0)
					{
						//done with one, now reset some data
						//subsequent calls should be done without the D parameter
						cincCount = 0;
						dataBuffer = new StringBuffer(128);
						dataBuffer.append("UPDT_CINC  "); //$NON-NLS-1$
						dataBuffer.append(this.fieldHeaderRec.getMfgn());
						dataBuffer.append(this.fieldHeaderRec.getIdss());
						dataBuffer.append(blank64);
						
						data = new StringBuffer(128);
						data.append(dataBuffer.substring(0, 64));				
					}
				}//end of cincCount maxed out
			}//end of while loop
			
			//now make sure we've called updtcinc at least once
			if(cincCount > 0)
			{
				rc = executeSingleUpdtCinc(data.toString());
			}
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(this, null, null, e);
		}
		finally
		{
			this.setCursor(Cursor.getDefaultCursor());
		}
	}
	//~1 End new method updtcinc

	//~1 This method will now call updtcinc, which does pretty much the same logic.
	/** Invoked when {@link #pbExit} is selected. */
	private void exit() 
	{
		updtcinc();
		this.dispose();
	}
	
	/**
	 * Returns the node with a cntr value that matches the specified value. 
	 * @param cntrValue the cntr value of the desired node
	 * @return the desired node or <code>null</code>
	 */
	@SuppressWarnings("rawtypes")
	private DefaultMutableTreeNode findNode(String cntrValue) 
	{
		boolean found = false;
		DefaultMutableTreeNode treeNode = null;
		
		Enumeration e = this.fieldRootNode.breadthFirstEnumeration();
		while (!found && e.hasMoreElements())
		{
			DefaultMutableTreeNode tNode = (DefaultMutableTreeNode)e.nextElement();
			MFSCntrNode cntr = (MFSCntrNode)tNode.getUserObject();
			if (cntr != null)
			{
				if (cntr.getCntr().equals(cntrValue))
				{
					treeNode = tNode;
					found = true;			
				}
			}
		}
		return treeNode;
	}
	
	/**
	 * Returns the number of the row with a cntr value that matches the
	 * specified value.
	 * @param cntr the cntr value of the desired row
	 * @return the row number
	 */
	@SuppressWarnings("rawtypes")
	private int findRow(String cntr) 
	{
		boolean found = false;
		int row = 0;
		
		Enumeration e = this.fieldRootNode.preorderEnumeration();
		while (!found && e.hasMoreElements())
		{
			DefaultMutableTreeNode tNode = (DefaultMutableTreeNode)e.nextElement();
			MFSCntrNode cn = (MFSCntrNode)tNode.getUserObject();
			if (cn != null)
			{
				if (cn.getCntr().equals(cntr))
				{
					found = true;			
				}
				else
				{
					row++;
				}
			}
		}
		return row;
	}
	
	/** 
	 * Constructs the tree using the specified <code>data</code>
	 * @param data the data used to construct the tree
	 */
	public void loadTree(String data) 
	{
		try
		{
			/* first add all the cntr's to the root */
			int start = 0;
			int end = 64;
			final int len = data.length();
			final int siz = len / end;
			MFSCntrNode cntrArray[] = new MFSCntrNode[siz];

			int i = 0;
			while (end < len)
			{
				MFSCntrNode cntr = new MFSCntrNode(data.substring(start,end));
				cntrArray[i] = cntr;
				DefaultMutableTreeNode tnode = new DefaultMutableTreeNode(cntr);
				this.fieldRootNode.add(tnode);
				start += 64;
				end += 64;
				i++;
			}

			if (len > 0)
			{
				MFSCntrNode cntr = new MFSCntrNode(data.substring(start));
				cntrArray[i] = cntr;
				DefaultMutableTreeNode tnode = new DefaultMutableTreeNode(cntr);
				this.fieldRootNode.add(tnode);
			} 

			/* set up parent/child relationship */
			for (i=0; i<siz; i++)
			{
				if (!cntrArray[i].getParentCntr().equals("          ")) //$NON-NLS-1$
				{
					DefaultMutableTreeNode child = findNode(cntrArray[i].getCntr());
					DefaultMutableTreeNode parent = findNode(cntrArray[i].getParentCntr());
					parent.add(child);
				}
			}

			refreshTree();
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(this, null, null, e);
		}
	}
	
	/** Invoked when {@link #pbNew} is selected. */
	private void newCntr() 
	{
		try
		{
			int rc = 0;
			String errorString = ""; //$NON-NLS-1$
			
			DefaultMutableTreeNode selection =
					(DefaultMutableTreeNode)this.cntrTree.getSelectionPath().getLastPathComponent();
			MFSCntrNode cn = (MFSCntrNode)selection.getUserObject();
			int row = findRow(cn.getCntr());
			
			Object[] options = {"YES","NO"};
			
			// ~1 Check palletLogic to change the verbiage of the confirmation dialog
			String strOptionDialog;
			if(this.fieldPalletLogic)
			{
				strOptionDialog = new String("Do you really want to create a new Pallet?");
			}
			else
			{
				strOptionDialog = new String("Do you really want to create a new Container?");
			}
			
			int n = JOptionPane.showOptionDialog(
				getParent(),strOptionDialog,
				"Create New Container", JOptionPane.YES_NO_OPTION,
				JOptionPane.WARNING_MESSAGE,null,options,options[1]);
					
			if (n == JOptionPane.YES_OPTION) 
			{
				String data = "RTV_CNTR  " + this.fieldDirWork.getCurrMctl() + "N" + //$NON-NLS-1$ //$NON-NLS-2$
							BLANK_CNTR + MFSConfig.getInstance().get8CharUser();  /* ~9C */
				MFSTransaction rtv_cntr = new MFSFixedTransaction(data);
				rtv_cntr.setActionMessage("Retrieving Container Info, Please Wait...");
				MFSComm.getInstance().execute(rtv_cntr, this);
				rc = rtv_cntr.getReturnCode();
				
				if (rc == 0)
				{
					/* if it is a system level operation, use the DWJPanel's current MCTL b/c header MCTL will */
					/* be blank - and also label the cntr as an 'O'verpack container */
					StringBuffer cntrData = new StringBuffer();
					if(this.fieldHeaderRec.getOlev().equals("S")) //$NON-NLS-1$
					{
						
						cntrData.append("                  "); //$NON-NLS-1$
						cntrData.append(this.fieldDirWork.getCurrMctl());
						cntrData.append(rtv_cntr.getOutput());
						cntrData.append(this.fieldHeaderRec.getOrno()); 
						cntrData.append("0"); //$NON-NLS-1$
					}
					else
					{	
						cntrData.append("                  "); //$NON-NLS-1$
						cntrData.append(this.fieldHeaderRec.getMctl());
						cntrData.append(rtv_cntr.getOutput());
						cntrData.append(this.fieldHeaderRec.getOrno());
						cntrData.append(this.fieldHeaderRec.getWtyp());
					}
						
					MFSCntrNode cntr = new MFSCntrNode(cntrData.toString());
					DefaultMutableTreeNode tnode = new DefaultMutableTreeNode(cntr);
					this.fieldRootNode.add(tnode);
					row = findRow(cntr.getCntr());
					refreshTree();

					this.fieldDirWork.updt_cntr(rtv_cntr.getOutput());
					this.fieldDirWork.update(getGraphics());
					this.update(getGraphics());

					/* check to see if our country has an override quantity */
					MFSConfig config = MFSConfig.getInstance();
					int qty = 1;
					String overrideCfgQty = "CONTAINER," + this.fieldHeaderRec.getCtry().trim(); //$NON-NLS-1$
				 	String overrideqty = config.getConfigValue(overrideCfgQty);
				 	if(!overrideqty.equals(MFSConfig.NOT_FOUND))
				 	{
						if (!overrideqty.equals(""))
						{
							qty = Integer.parseInt(overrideqty);
						}
				 	}
					
					// ~3 Only call printing methods when not creating a pallet.
					if(!this.fieldPalletLogic)
					{
						MFSDetermineLabel label = new MFSDetermineLabel(); /* ~11A */
						label.determineContainerLabel(this.fieldHeaderRec,this.fieldHeaderRec.getMctl(), /* ~11A */
								rtv_cntr.getOutput(),qty,getParentFrame(), false, this);						
					}					
				}
				else
				{
					errorString = rtv_cntr.getErms();
				}

				if (rc != 0)
				{
					IGSMessageBox.showOkMB(this, null, errorString, null);
				}
			}

			this.toFront();
			this.cntrTree.setSelectionRow(row);

			/* scroll - need to do twice to get to bottom */
			this.cntrTree.scrollRowToVisible(row);
			this.cntrTree.scrollRowToVisible(row);
			this.cntrTree.requestFocusInWindow();
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(this, null, null, e);
		}
	}
	
	/** Refreshes the tree display. */
	private void refreshTree()
	{
		((DefaultTreeModel) this.cntrTree.getModel()).reload();

		for (int i = 0; i < this.cntrTree.getRowCount(); i++)
		{
			this.cntrTree.expandRow(i);
		}
	}
	
	/** Invoked when {@link #pbReprint} is selected. */
	private void reprint() 
	{
		try
		{
			DefaultMutableTreeNode selection =
					(DefaultMutableTreeNode)this.cntrTree.getSelectionPath().getLastPathComponent();
			MFSCntrNode selObj = (MFSCntrNode)selection.getUserObject();
			int row = findRow(selObj.getCntr());
			
			/* check to see if our country has an override quantity */
			MFSConfig config = MFSConfig.getInstance();
			int qty = 1;
			String overrideCfgQty = "CONTAINER," + this.fieldHeaderRec.getCtry().trim(); //$NON-NLS-1$
		 	String overrideqty = config.getConfigValue(overrideCfgQty);
		 	if(!overrideqty.equals(MFSConfig.NOT_FOUND))
		 	{
				if (!overrideqty.equals("")) //$NON-NLS-1$
				{
					qty = Integer.parseInt(overrideqty);
				}
		 	}
			
			// ~1 some extra logic when container is actually a pallet.
			if(this.fieldPalletLogic && selection.getChildCount()!=0)
			{
				updtcinc();
				MFSPrintingMethods.caseContent(selObj.getCntr(), null, qty, getParentFrame());  // ~10C
			}
			else //if not a pallet
			{
				MFSDetermineLabel label = new MFSDetermineLabel(); /* ~11A */
				label.determineContainerLabel(this.fieldHeaderRec,selObj.getMctl(), /* ~11A */
						selObj.getCntr(),qty,getParentFrame(), true, this);
			}					
			
			this.toFront();
			this.cntrTree.setSelectionRow(row);
			this.cntrTree.requestFocusInWindow();
		}
		catch (Exception e)
		{
			this.setCursor(Cursor.getDefaultCursor());
			IGSMessageBox.showOkMB(this, null, null, e);
		}
	}
	
	/** Invoked when {@link #pbUndoChild} is selected. */
	private void undoChild() 
	{
		DefaultMutableTreeNode selection =
				(DefaultMutableTreeNode)this.cntrTree.getSelectionPath().getLastPathComponent();

		int row = 0;
		
		if (selection.getParent() != this.fieldRootNode)
		{
			MFSCntrNode cn = (MFSCntrNode)selection.getUserObject();
			cn.setParentCntr("          "); //$NON-NLS-1$
			cn.setParentMctl("        "); //$NON-NLS-1$
			this.fieldRootNode.add(selection);
			row = findRow(cn.getCntr());
		}

		refreshTree();
		
		this.toFront();
		this.cntrTree.setSelectionRow(row);
		this.cntrTree.scrollRowToVisible(row);
		this.cntrTree.requestFocusInWindow();
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
			if (source == this.pbAddChild)
			{
				addChild();
			}
			else if (source == this.pbExit)
			{
				exit();
			}
			else if (source == this.pbNew)
			{
				newCntr();
			}
			else if (source == this.pbUndoChild)
			{
				undoChild();
			}
			else if (source == this.pbReprint)
			{
				reprint();
			}
			else if (source == this.pbDelete)
			{
				deleteCntr();
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
		if (keyCode == KeyEvent.VK_ENTER && ke.getSource() instanceof JButton)
		{
			JButton button = (JButton) ke.getSource();
			button.requestFocusInWindow();
			button.doClick();
		}
		else if(keyCode == KeyEvent.VK_F2)
		{
			this.pbAddChild.requestFocusInWindow();
			this.pbAddChild.doClick();
		}
		else if(keyCode == KeyEvent.VK_F3)
		{
			this.pbExit.requestFocusInWindow();
			this.pbExit.doClick();
		}
		else if(keyCode == KeyEvent.VK_F4)
		{
			this.pbNew.requestFocusInWindow();
			this.pbNew.doClick();
		}
		else if(keyCode == KeyEvent.VK_F7)
		{
			this.pbUndoChild.requestFocusInWindow();
			this.pbUndoChild.doClick();
		}
		else if(keyCode == KeyEvent.VK_F9)
		{
			this.pbReprint.requestFocusInWindow();
			this.pbReprint.doClick();
		}
		else if(keyCode == KeyEvent.VK_F11)
		{
			this.pbDelete.requestFocusInWindow();
			this.pbDelete.doClick();
		}
		else if (keyCode == KeyEvent.VK_PAGE_DOWN)
		{
			int value = this.spCntr.getVerticalScrollBar().getValue();
			if ((value + 2*this.spCntr.getVerticalScrollBar().getBlockIncrement(1)) >
					this.spCntr.getVerticalScrollBar().getMaximum())
			{
				this.spCntr.getVerticalScrollBar().setValue(
						this.spCntr.getVerticalScrollBar().getMaximum());
			}
			else
			{
				value += this.spCntr.getVerticalScrollBar().getBlockIncrement(1);
				this.spCntr.getVerticalScrollBar().setValue(value);
			}
			ke.consume();
		}
		else if (keyCode == KeyEvent.VK_PAGE_UP)
		{
			int value =this.spCntr.getVerticalScrollBar().getValue();
			value -= this.spCntr.getVerticalScrollBar().getBlockIncrement(1);
			if (value < 0)
			{
				this.spCntr.getVerticalScrollBar().setValue(0);
			}
			else
			{
				this.spCntr.getVerticalScrollBar().setValue(value);
			}
			ke.consume();
		}
		else if (keyCode == KeyEvent.VK_RIGHT)
		{
			int value = this.spCntr.getHorizontalScrollBar().getValue();
			if ((value + 100) >
					this.spCntr.getHorizontalScrollBar().getMaximum())
			{
				this.spCntr.getHorizontalScrollBar().setValue(
						this.spCntr.getHorizontalScrollBar().getMaximum());
			}
			else
			{
				value += 50;
				this.spCntr.getHorizontalScrollBar().setValue(value);
			}
			ke.consume();
		}
		else if (keyCode == KeyEvent.VK_LEFT)
		{
			int value = this.spCntr.getHorizontalScrollBar().getValue();
			value -= 50;
			if (value < 0)
			{
				this.spCntr.getHorizontalScrollBar().setValue(0);
			}
			else
			{
				this.spCntr.getHorizontalScrollBar().setValue(value);
			}
			ke.consume();
		}
	}

	/**
	 * Invoked when an item in the tree has been collapsed.
	 * @param event the <code>TreeExpansionEvent</code>
	 */
	public void treeCollapsed(TreeExpansionEvent event)
	{
		this.cntrTree.expandPath(event.getPath());
	}
	
	/**
	 * Invoked when an item in the tree has been expanded.
	 * @param event the <code>TreeExpansionEvent</code>
	 */
	public void treeExpanded(TreeExpansionEvent event)
	{
		// Does nothing
	}
	
	/**
	 * Invoked the first time a window is made visible.
	 * <p>
	 * Sets the selected row of {@link #cntrTree} to 0 and requests the focus
	 * for {@link #cntrTree}.
	 * @param we the <code>WindowEvent</code>
	 */
	public void windowOpened(WindowEvent we)
	{
		if (we.getSource() == this)
		{
			this.cntrTree.setSelectionRow(0);
			this.cntrTree.requestFocusInWindow();
		}
	}
}
