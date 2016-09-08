/* © Copyright IBM Corporation 2005, 2007. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2007-01-26      34242JR  R Prechel        -Java 5 version
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.dialog;

import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.ScrollPaneConstants;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

import mfsxml.MISSING_XML_TAG_EXCEPTION;
import mfsxml.MfsXMLDocument;
import mfsxml.MfsXMLParser;

import com.ibm.rchland.mfgapps.client.utils.messagebox.IGSMessageBox;
import com.ibm.rchland.mfgapps.mfsclient.MFSDialog;
import com.ibm.rchland.mfgapps.mfsclient.MFSFrame;
import com.ibm.rchland.mfgapps.mfsclient.renderer.MFSViewPartOpsTreeCellRenderer;
import com.ibm.rchland.mfgapps.mfscommon.MFSActionable;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSComm;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSTransaction;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSXmlTransaction;

/**
 * <code>MFSViewPartOpsDialog</code> is the <code>MFSDialog</code> used to
 * display the part operations tree.
 * @author The MFS Client Development Team
 */
public class MFSViewPartOpsDialog
	extends MFSDialog
{
	private static final long serialVersionUID = 1L;
	/** The enter <code>JButton</code>. */
	private JButton pbExit = createButton("Exit", 'x');
	
	/** The <code>JScrollPane</code> that contains the list of part ops. */
	private JScrollPane spVPO = new JScrollPane(null, 
			ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, 
			ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

	/** The <code>MFSActionable</code> using this <code>MFSViewPartOpsDialog</code>. */
	private MFSActionable fieldActionable;
	
	/** The CCIN value for which a tree is created. */
	private String fieldCCIN;

	/**
	 * Constructs a new <code>MFSViewPartOpsDialog</code>.
	 * @param parent the <code>MFSFrame</code> that caused this
	 *        <code>MFSViewPartOpsDialog</code> to be displayed
	 * @param actionable the <code>MFSActionable</code> using this
	 *        <code>MFSViewPartOpsDialog</code>
	 * @param ccin the CCIN value for which a tree is created
	 */
	public MFSViewPartOpsDialog(MFSFrame parent, MFSActionable actionable, String ccin)
	{
		super(parent, "Part Operations Screen");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.fieldActionable = actionable;
		this.fieldCCIN = ccin;
		createLayout();
		addMyListeners();
		setSize(706, 382);
	}
	
	/** Adds this dialog's <code>Component</code>s to the layout. */
	private void createLayout()
	{
		JPanel contentPane = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
				GridBagConstraints.NORTH, GridBagConstraints.BOTH,
				new Insets(20, 20, 10, 10), 0, 0);

		contentPane.add(this.spVPO, gbc);
		
		gbc.gridx = 1;
		gbc.weightx = 0.0;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(20, 10, 10, 20);
		
		contentPane.add(this.pbExit, gbc);
		
		setContentPane(contentPane);
	}

	/** Adds the listeners to this dialog's <code>Component</code>s. */
	private void addMyListeners()
	{
		this.pbExit.addActionListener(this);
		this.pbExit.addKeyListener(this);
		this.spVPO.addKeyListener(this);
	}

	/**
	 * Builds the part ops tree from the specified <code>xmlData</code>
	 * @param xmlData the data used to build the tree
	 * @return the root node
	 */
	private DefaultMutableTreeNode buildTree(String xmlData)
	{
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("CCIN:  " + this.fieldCCIN);
		try
		{
			boolean done = false;
			boolean done2 = false;
			DefaultMutableTreeNode flaggrp_branch = null;
			DefaultMutableTreeNode flagid_branch = null;
			MfsXMLParser xmlParser = new MfsXMLParser(xmlData);
			MfsXMLParser ccin = new MfsXMLParser(xmlParser.getFieldLast("CCIN"));
			while (!done)
			{
				try
				{
					MfsXMLParser rh10 = new MfsXMLParser(ccin.getWholeNextField("RH10"));
					
					StringBuffer nodeText = new StringBuffer();
					nodeText.append("Flag Group: ");
					nodeText.append(rh10.getFieldOnly("RHFGRP"));
					nodeText.append("    Test Level: ");
					nodeText.append(rh10.getFieldOnly("RHTLEV"));
					
					flaggrp_branch = new DefaultMutableTreeNode(nodeText.toString());
					root.add(flaggrp_branch);

					done2 = false;
					while (!done2)
					{
						try
						{
							MfsXMLParser ri10 = new MfsXMLParser(rh10.getWholeNextField("RI10"));
							
							nodeText = new StringBuffer();
							nodeText.append("Flag ID: ");
							nodeText.append(ri10.getFieldOnly("RIFGID"));
							nodeText.append("    Value: ");
							nodeText.append(ri10.getFieldOnly("RIFVAL"));
							
							flagid_branch = new DefaultMutableTreeNode(nodeText.toString());
							flaggrp_branch.add(flagid_branch);
						}
						catch (MISSING_XML_TAG_EXCEPTION mg)
						{
							done2 = true;
						}
					}
				}
				catch (MISSING_XML_TAG_EXCEPTION mt)
				{
					done = true;
				}
			}
		}
		catch (Exception e)
		{
			this.setCursor(Cursor.getDefaultCursor());
			IGSMessageBox.showOkMB(this, null, null, e);
		}
		return root;
	}
	
	/** 
	 * Call RTVCCINFLG to get the XML data used to create the tree.
	 * @return the xml data 
	 */
	private String getXMLdata()
	{
		String downloadedXML = null;
		try
		{
			// Calls TRX to get RH10 and RI10 entries from SERVER
			MfsXMLDocument xml_data = new MfsXMLDocument("RTVCCINFLG"); //$NON-NLS-1$
			xml_data.addOpenTag("DATA"); //$NON-NLS-1$
			xml_data.addCompleteField("CCIN", this.fieldCCIN); //$NON-NLS-1$
			xml_data.addCloseTag("DATA"); //$NON-NLS-1$
			xml_data.finalizeXML();
			
			MFSTransaction rtvccinflg = new MFSXmlTransaction(xml_data.toString());
			rtvccinflg.setActionMessage("Downloading operations, Please Wait...");
			MFSComm.getInstance().execute(rtvccinflg, this.fieldActionable);

			downloadedXML = rtvccinflg.getOutput();

			if (rtvccinflg.getReturnCode() != 0)
			{
				IGSMessageBox.showOkMB(getParentFrame(), null, rtvccinflg.getErms(), null);
				return null;
			}
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
			return null;
		}
		return downloadedXML;
	}
	
	/** Loads the part ops tree. */
	public void loadTree() {
		try
		{
			String downloadedXML = getXMLdata();
			if (downloadedXML != null)
			{
				//Build tree based on XML data received 
				JTree vpoTree = new JTree(buildTree(downloadedXML));
				vpoTree.setCellRenderer(new MFSViewPartOpsTreeCellRenderer());
				vpoTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
				vpoTree.putClientProperty("JTree.lineStyle", "Angled"); //$NON-NLS-1$ //$NON-NLS-2$
				this.spVPO.setViewportView(vpoTree);
				
				//make sure the tree has a key listener. 
				vpoTree.addKeyListener(this);
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
			if (source == this.pbExit)
			{
				this.dispose();
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
		if (keyCode == KeyEvent.VK_ESCAPE
				|| (keyCode == KeyEvent.VK_ENTER && ke.getSource() == this.pbExit))
		{
			this.pbExit.requestFocusInWindow();
			this.pbExit.doClick();
		}
	}
}
