/* � Copyright IBM Corporation 2009. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR    Name             Details
 * ---------- ---- ----------- ---------------- ----------------------------------
 * 2009-08-28      37550JL     Ray Perry        -Initial version
 * 2009-08-31      37550JL     Brian Becker     -Add rtvBaData method
 * 2009-11-18      37550JL     Ray Perry        -Version 2 of the screen
 * 2010-02-02      37550JL     Ray Perry        -Fix up some stuff based on what China wants
 * 2010-02-05      37550JL     Ray Perry        -Buggy ride
 * 2011-10-25 ~01  RCQ00177780 VH Avila			-We have to set the Focus on the new JTextField object
 *                                               within MFSEntityMergePanel
 * 2014-03-05 ~02  RCQ00267733
 * 				   INT011REG   Efrain Mota		-Fix for release/unrelease build ahead shipentities                                            
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.panel;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.PipedInputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JTree;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.ToolTipManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import com.ibm.rchland.mfgapps.client.utils.exception.IGSException;
import com.ibm.rchland.mfgapps.client.utils.exception.IGSTransactionException;
import com.ibm.rchland.mfgapps.client.utils.layout.IGSGridLayout;
import com.ibm.rchland.mfgapps.client.utils.messagebox.IGSMessageBox;
import com.ibm.rchland.mfgapps.client.utils.xml.IGSXMLTransaction;
import com.ibm.rchland.mfgapps.client.utils.xml.IGSXMLTreeHandler;
import com.ibm.rchland.mfgapps.mfsclient.MFSFrame;
import com.ibm.rchland.mfgapps.mfsclient.MFSMenuButton;
import com.ibm.rchland.mfgapps.mfsclient.MFSPanel;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSGetValueDialog;
import com.ibm.rchland.mfgapps.mfsclient.renderer.MFSBuildAheadTreeCellRenderer;
import com.ibm.rchland.mfgapps.mfsclient.tree.MFSBuildAheadCnfgUO;
import com.ibm.rchland.mfgapps.mfsclient.tree.MFSBuildAheadCoverageUO;
import com.ibm.rchland.mfgapps.mfsclient.tree.MFSBuildAheadMfgnUO;
import com.ibm.rchland.mfgapps.mfsclient.tree.MFSBuildAheadParseStrategy;
import com.ibm.rchland.mfgapps.mfsclient.tree.MFSBuildAheadReleasedUO;
import com.ibm.rchland.mfgapps.mfsclient.tree.MFSBuildAheadSchdUO;
import com.ibm.rchland.mfgapps.mfsclient.tree.MFSBuildAheadShpnUO;
import com.ibm.rchland.mfgapps.mfsclient.tree.MFSBuildAheadTopUO;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSButtonIterator;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSConstants;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFStfParser;
import com.ibm.rchland.mfgapps.mfscommon.MFSActionable;
import com.ibm.rchland.mfgapps.mfscommon.MFSConfig;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSComm;

/**
 * <code>MFSBuildAheadPanel</code> is the <code>MFSPanel</code> used to
 * perform Build Ahead operations.
 * @author The MFS Client Development Team
 */
public class MFSBuildAheadPanel extends MFSPanel implements TreeSelectionListener {
	private static final long serialVersionUID = 1L;
	/** The default screen ID of an <code>MFSBuildAheadPanel</code>. */
	public static final String SCREEN_ID = "BUILDAHEAD"; 

	/** The default screen name of an <code>MFSBuildAheadPanel</code>. */
	public static final String SCREEN_NAME = "BuildAhead";

	/** The mode controls what users see */
	public static final char BARELEASE = 'R';
	public static final char BAAPPLY = 'A';

	/** The Find Container (F2) <code>MFSMenuButton</code>. */
	private final MFSMenuButton pbFind = MFSMenuButton.createSmallButton(
			"Find", "smF2.gif", "Find Ship Entity", true); 

	/** The End Entity (F3) <code>MFSMenuButton</code>. */
	private final MFSMenuButton pbEnd = MFSMenuButton.createSmallButton(
			"End", "smF3.gif", "End Build Ahead", true); 

	/** The Expand Tree (F4) <code>MFSMenuButton</code>. */
	private final MFSMenuButton pbExpand = MFSMenuButton.createSmallButton(
			"Expand", "smF4.gif", "Expand Trees", true); 

	/** The Collapse Trees (F5) <code>MFSMenuButton</code>. */
	private final MFSMenuButton pbCollapse = MFSMenuButton.createSmallButton(
			"Collapse", "smF5.gif", "Collapse Trees", true); 

    /** The Release BuildAhead (F6) <code>MFSMenuButton</code>. */
    private final MFSMenuButton pbRelease = MFSMenuButton.createSmallButton(
            "Release", "smF6.gif", "Release Build Ahead", true); 

    /** The Entity Merge (F6) <code>MFSMenuButton</code>. */
    private final MFSMenuButton pbEntityMerge = MFSMenuButton.createSmallButton(
            "Merge", "smF6.gif", "Entity Merge", true); 
    
    /** The Unrelease BuildAhead (F7) <code>MFSMenuButton</code>. */
    private final MFSMenuButton pbUnRelease = MFSMenuButton.createSmallButton(
            "Un-Rel", "smF7.gif", "Un-Release Build Ahead", true); 
    
	/** The <code>JPanel</code> containing the <code>MFSMenuButton</code>s. */
	private final JPanel pnlButtons = new JPanel(new IGSGridLayout(3, 4, 2, 1));

	/** The user <code>JLabel</code>. */
	private final JLabel lblUser = createLabel("User: " + MFSConfig.getInstance().get8CharUser());

    /** The user <code>JLabel</code>. */
    //                                                                                     XXXXXXX   XXXXX       XXXXX       XXXXX       XXXXXXX       XXXXX                 
    private final JLabel lblColumnHeadings = createLabel("                                 Part      Order Qty   Available   Stock       Location      Loc Qty");

	/** The tree node information <code>JTextPane</code>. */
	private final JTextPane taNodeInfo = createInfoTextPane();

	/** The <code>JScrollPane</code> for {@link #taNodeInfo}. */
	private final JScrollPane spNodeInfo = createScrollPane(this.taNodeInfo);

	/** The <code>DefaultTreeModel</code> for {@link #trLeftTree}. */
	private final DefaultTreeModel fieldLeftTreeModel = new DefaultTreeModel(null, false);

	/** The <code>JTree</code> on the left side of the panel. */
	private final JTree trLeftTree = createTree(this.fieldLeftTreeModel);

	/** The <code>JScrollPane</code> for {@link #trLeftTree}. */
	private final JScrollPane spLeftTree = createScrollPane(this.trLeftTree);

	/** The <code>MFSButtonIterator</code> for the {@link MFSMenuButton}s. */
	private MFSButtonIterator fieldButtonIterator = createMenuButtonIterator();

	/** The <code>Map</code> of container tree nodes. */
	@SuppressWarnings("rawtypes")
	private Map fieldNodeMap = new HashMap();

	/** View only or merging mode. */
	private final char fieldMode;
	
	private String locked = "N";

	/**
	 * Constructs a new <code>MFSBuildAheadPanel</code>.
	 * @param parent the <code>MFSFrame</code> used to display this panel
	 * @param source the <code>MFSPanel</code> that caused this panel to be
	 *        displayed
	 * @param mode {@link #BARELEASE} or {@link #BAAPPLY}
	 */
	public MFSBuildAheadPanel(MFSFrame parent, MFSPanel source, char mode) {
		super(parent, source, SCREEN_NAME, SCREEN_ID, new GridBagLayout());
		this.fieldMode = mode;
		createLayout();
		configureButtons();
		addMyListeners();
	}

	
	/** Adds the listeners to this panel's <code>Component</code>s. */
	private void addMyListeners() {
		this.addKeyListener(this);
        this.spLeftTree.addKeyListener(this);
        this.trLeftTree.addKeyListener(this);
		this.pnlButtons.addKeyListener(this);
		this.spNodeInfo.addKeyListener(this);
		this.taNodeInfo.addKeyListener(this);
        this.trLeftTree.addTreeSelectionListener(this);

		this.fieldButtonIterator.reset();
		while (this.fieldButtonIterator.hasNext()) {
			JButton next = this.fieldButtonIterator.nextMenuButton();
			next.addActionListener(this);
			next.addKeyListener(this);
		}
	}

	
	/** Removes the listeners from this panel's <code>Component</code>s. */
	protected void removeMyListeners() {
		this.removeKeyListener(this);
		this.spLeftTree.removeKeyListener(this);
		this.trLeftTree.removeKeyListener(this);
		this.pnlButtons.removeKeyListener(this);
		this.spNodeInfo.removeKeyListener(this);
		this.taNodeInfo.removeKeyListener(this);
		this.trLeftTree.removeTreeSelectionListener(this);

		this.fieldButtonIterator.reset();
		while (this.fieldButtonIterator.hasNext()) {
			JButton next = this.fieldButtonIterator.nextMenuButton();
			next.removeActionListener(this);
			next.removeKeyListener(this);
		}
	}

	
	/** Adds this panel's <code>Component</code>s to the layout. */
	private void createLayout() {
		this.pnlButtons.setBackground(MFSConstants.PRIMARY_BACKGROUND_COLOR);
		this.pnlButtons.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

		JPanel buttonPanelHolder = new JPanel(new FlowLayout());
		buttonPanelHolder.setBorder(BorderFactory
				.createLineBorder(MFSConstants.PRIMARY_FOREGROUND_COLOR));
		buttonPanelHolder.setBackground(MFSConstants.PRIMARY_BACKGROUND_COLOR);
		buttonPanelHolder.add(this.pnlButtons);

		JPanel headerPanel = new JPanel(null);
		headerPanel.setBorder(new EmptyBorder(0, 0, 0, 5));
		BoxLayout layout = new BoxLayout(headerPanel, BoxLayout.LINE_AXIS);
		headerPanel.setLayout(layout);
		headerPanel.setBackground(MFSConstants.PRIMARY_BACKGROUND_COLOR);
		headerPanel.add(this.lblUser);

        JPanel columnHeadingsPanel = new JPanel(null);
        columnHeadingsPanel.setBorder(new EmptyBorder(0, 0, 0, 5));
        BoxLayout columnHeadingLayout = new BoxLayout(columnHeadingsPanel, BoxLayout.LINE_AXIS);
        columnHeadingsPanel.setLayout(columnHeadingLayout);
        columnHeadingsPanel.setBackground(MFSConstants.PRIMARY_BACKGROUND_COLOR);
        columnHeadingsPanel.add(this.lblColumnHeadings);

        GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 2, 0.0, 0.0,
				GridBagConstraints.NORTH, GridBagConstraints.BOTH,
				new Insets(1, 1, 1, 1), 0, 0);
		
		add(buttonPanelHolder, gbc);

		gbc.gridx = 1;
		gbc.gridheight = 1;
		gbc.weightx = 1.0;
		gbc.fill = GridBagConstraints.HORIZONTAL;

		add(headerPanel, gbc);

		gbc.gridy = 1;
		gbc.anchor = GridBagConstraints.SOUTH;
		gbc.fill = GridBagConstraints.BOTH;

		add(this.spNodeInfo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.SOUTH;
        gbc.fill = GridBagConstraints.BOTH;

        add(columnHeadingsPanel, gbc);

		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.gridwidth = 2;
		gbc.weighty = 1.0;
		gbc.anchor = GridBagConstraints.SOUTH;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(4, 4, 4, 4);

		add(spLeftTree, gbc);
	}

	
	/**
	 * Creates an <code>MFSButtonIterator</code> for this panel's
	 * <code>MFSMenuButton</code>s.
	 * @return the <code>MFSButtonIterator</code>
	 */
	private MFSButtonIterator createMenuButtonIterator() {
		// The constructor param is the number of buttons
		MFSButtonIterator result = new MFSButtonIterator(12); 
		result.add(this.pbFind);
		result.add(this.pbEnd);
		result.add(this.pbExpand);
		result.add(this.pbCollapse);
        result.add(this.pbRelease);
        result.add(this.pbEntityMerge);
        result.add(this.pbUnRelease);
		return result;
	}

	
	/**
	 * Creates a header <code>JLabel</code>.
	 * @param text the initial text displayed by the <code>JLabel</code>
	 * @return the <code>JLabel</code>
	 */
	private JLabel createLabel(String text) {
		JLabel result = new JLabel(text, null, SwingConstants.LEFT);
		result.setFont(MFSConstants.MEDIUM_MONOSPACED_FONT);
		result.setForeground(MFSConstants.PRIMARY_FOREGROUND_COLOR);
		return result;
	}

	
	/**
	 * Creates an information <code>JTextPane</code>.
	 * @return the <code>JTextPane</code>
	 */
	private JTextPane createInfoTextPane() {
		JTextPane result = new JTextPane();
		result.setEditable(false);
		result.setFont(MFSConstants.MEDIUM_MONOSPACED_FONT);
		result.setRequestFocusEnabled(false);
		SimpleAttributeSet attributeSet = new SimpleAttributeSet();
		StyleConstants.setLineSpacing(attributeSet, -0.2f);
		result.setParagraphAttributes(attributeSet, false);
		return result;
	}

	
	/**
	 * Creates a <code>JScrollPane</code> for a <code>JComponent</code>.
	 * @param component the <code>JComponent</code>
	 * @return the <code>JScrollPane</code>
	 */
	private JScrollPane createScrollPane(JComponent component) {
		JScrollPane result = new JScrollPane(component,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		result.setRequestFocusEnabled(false);
		return result;
	}
	

	/**
	 * Creates a <code>JTree</code>.
	 * @param treeModel the <code>TreeModel</code> for the <code>JTree</code>
	 * @return the <code>JTree</code>
	 */
	private JTree createTree(TreeModel treeModel) {
		JTree result = new JTree(treeModel);
		result.setCellRenderer(new MFSBuildAheadTreeCellRenderer());
		ToolTipManager.sharedInstance().registerComponent(result);
		result.setRootVisible(true);
		result.setShowsRootHandles(true);
		DefaultTreeSelectionModel selModel = new DefaultTreeSelectionModel();
		selModel.setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		result.setSelectionModel(selModel);
		return result;
	}

	
	/** Determines which <code>MFSMenuButton</code>s are displayed. */
	private void configureButtons() {
		this.pnlButtons.removeAll();
		this.pnlButtons.add(this.pbFind);
		this.pbFind.setEnabled(true);
		this.pnlButtons.add(this.pbEnd);
		this.pbEnd.setEnabled(true);
		this.pnlButtons.add(this.pbExpand);
		this.pbExpand.setEnabled(true);
		this.pnlButtons.add(this.pbCollapse);
		this.pbCollapse.setEnabled(true);
        if (this.fieldMode==BARELEASE) {
            this.pnlButtons.add(this.pbRelease);
            this.pbRelease.setEnabled(true);
            this.pnlButtons.add(this.pbUnRelease);
            this.pbUnRelease.setEnabled(true);
        } else if (this.fieldMode==BAAPPLY) {
            this.pnlButtons.add(this.pbEntityMerge);
            this.pbEntityMerge.setEnabled(true);
        }
	}
    
    /**
     * Get the information on the entity based on a container number.
     * @param actionable the <code>MFSActionable</code> to update
     * @return <code>true</code> if the panel should be displayed
     * @throws IGSException if an error occurs
     */
    public boolean retrieveBAInfo(MFSActionable actionable, String type)
        throws IGSException {
        boolean retval = false;
        
        final MFSConfig config = MFSConfig.getInstance();
        IGSXMLTransaction strBA = new IGSXMLTransaction("STR_BA");
        strBA.setActionMessage("Retrieving build ahead information...");
        strBA.startDocument();
        strBA.addElement("TYPE", type); 
        strBA.addElement("CELL", config.get8CharCell()); 
        strBA.addElement("CTYP", config.get8CharCellType()); 
        strBA.addElement("USER", config.get8CharUser()); 
        strBA.endDocument();
        
        // need this reset method so that screen gets updated
        makeServerCallWithReset(strBA, actionable);
        locked = strBA.getFirstElement("LOCKED");
        String currentUser = strBA.getFirstElement("CURRENTUSER");
        
        this.pbRelease.setEnabled(this.isLocked());
        this.pbUnRelease.setEnabled(this.isLocked());
        
        // do order quantity calcs and update node colors
        setupInitialQuantityAndColorsAndOrder();
        
        if (this.fieldMode==BARELEASE && this.locked.equals("N"))
        {
            IGSMessageBox.showOkMB(this, "Build Ahead Release Screen is Locked", "You are in view only mode. Build Ahead Release\nScreen locked by user: " + currentUser + ".", null);
        }

        retval = true;
        
        return retval;
    }

    /**
     * Runs an <code>IGSXMLTransaction</code> that will refresh the trees if
     * the transaction runs successfully.
     * @param tran the <code>IGSXMLTransaction</code> to run
     * @param actionable the <code>MFSActionable</code> that will display the
     *        action message
     * @throws IGSException if an error occurred running the transaction or
     *         parsing the output
     */
    @SuppressWarnings("rawtypes")
	private void makeServerCallWithReset(IGSXMLTransaction tran, MFSActionable actionable)
        throws IGSException {
        try {
            PipedInputStream in = tran.setupPipedStreams("ENTITIES"); 
            MFSBuildAheadParseStrategy strategy = new MFSBuildAheadParseStrategy();
            Map forParsing = new HashMap((int) (this.fieldNodeMap.size() * 1.35));
            IGSXMLTreeHandler handler = new IGSXMLTreeHandler(in, strategy,
                    "SHPN", "SHPN", forParsing);  
            tran.setPipedRunnable(handler);
            MFSComm.getInstance().execute(tran, actionable);

            if (tran.getReturnCode() != 0) {
                if (handler.getException() != null) {
                    handler.getException().printStackTrace();
                } 
                throw new IGSTransactionException(tran, false);
            } else if (handler.getException() != null) {
                throw new IGSException(handler.getException(), false);
            } else {
                this.fieldNodeMap = forParsing;
                DefaultMutableTreeNode node = handler.getRootNode();
                this.fieldLeftTreeModel.setRoot(node);
            }
        } catch (IOException e) {
            throw new IGSException("Error setting up streams.", e, true);
        }
    }
    
    /**
     * This method does the inital calculations for order quantity,
     * unreleased order quantity, allocated quantity and sets up the
     * colors correctly. 
     * @param root The root of the tree
     */
    @SuppressWarnings("rawtypes")
	private void setupInitialQuantityAndColorsAndOrder() {
        DefaultMutableTreeNode root = (DefaultMutableTreeNode)this.fieldLeftTreeModel.getRoot();
        
        // For Apply screen, we don't want to show the un-released nodes 
        // so this Hashtable will store the nodes we want to remove from 
        // the screen. The reason we do it this way instead of deleting 
        // them as we go is because the loops get all screwed up if we
        // delete them as we go.
        Vector<Integer> shpnNodesToDelete = new Vector<Integer>();
        
        // This Hashtable will store the number of each config to be 
        // released. It's used for keeping the allocated quantity up
        // to date.
        Hashtable<String, Integer> configsReleased = new Hashtable<String, Integer>();
        
        // loop through ship entities
        int shpnIndex = 0;
        for (Enumeration shpnEnum = root.children(); shpnEnum.hasMoreElements();) 
        {
            DefaultMutableTreeNode shpnNode = (DefaultMutableTreeNode) shpnEnum.nextElement();
            Vector<Integer> cnfgNodesToDelete = new Vector<Integer>();
            
            // loop through configs
            int cnfgIndex = 0;
            for (Enumeration cnfgEnum = shpnNode.children(); cnfgEnum.hasMoreElements();) {
                DefaultMutableTreeNode cnfgNode = (DefaultMutableTreeNode) cnfgEnum.nextElement();
                MFSBuildAheadCnfgUO cnfgUO = (MFSBuildAheadCnfgUO) cnfgNode.getUserObject();
                Vector<Integer> mfgnNodesToDelete = new Vector<Integer>();

                int numberOfMfgnsOnCnfg = 0;
                int numberOfUnreleasedMfgnsOnCnfg = 0;
                
                // loop through mfgns
                int mfgnIndex = 0;
                for (Enumeration mfgnEnum = cnfgNode.children(); mfgnEnum.hasMoreElements();) 
                {
                    DefaultMutableTreeNode mfgnNode = (DefaultMutableTreeNode) mfgnEnum.nextElement();
                    MFSBuildAheadMfgnUO mfgnUO = (MFSBuildAheadMfgnUO) mfgnNode.getUserObject();
                    
                    numberOfMfgnsOnCnfg++;

                    if (mfgnUO.getRlsed().equals("N"))
                    {
                        numberOfUnreleasedMfgnsOnCnfg++;
                        
                        // If the MFGN is not released and we are on 
                        // the Apply screen, we don't want to see it.
                        // So add it to the list of nodes to be 
                        // removed from the tree.
                        if (fieldMode==BAAPPLY)
                        {
                            mfgnNodesToDelete.add(mfgnIndex);
                            numberOfMfgnsOnCnfg--;
                            numberOfUnreleasedMfgnsOnCnfg--;
                        }
                    }
                    mfgnIndex++;
                }

                // Keep the allocated Hashtable up to date
                Object quantityObj = configsReleased.get(cnfgUO.getItem());
                if (quantityObj!=null) 
                {
                    int quantityInt = (Integer)quantityObj;
                    configsReleased.remove(cnfgUO.getItem());
                    configsReleased.put(cnfgUO.getItem(), quantityInt+numberOfMfgnsOnCnfg-numberOfUnreleasedMfgnsOnCnfg);
                }
                else
                {
                    configsReleased.put(cnfgUO.getItem(), numberOfMfgnsOnCnfg-numberOfUnreleasedMfgnsOnCnfg);
                }
                // End of keep the allocated Hashtable up to date
                
                // Remove the MFGN nodes we don't want to see.
                // This will be empty if we are on the Release screen.
                // We go backwards so the node index will be accurate.
                for (int y=mfgnNodesToDelete.size()-1; y>=0; y--)
                {
                    cnfgNode.remove((Integer)mfgnNodesToDelete.elementAt(y));
                }
                
                // If there are no MFGNs on this config node,
                // we don't want to show the config either.
                if (fieldMode==BAAPPLY && cnfgNode.getChildCount()<1)
                {
                    cnfgNodesToDelete.add(cnfgIndex);
                }

                // Update some quantity values
                cnfgUO.setOrderQty(numberOfMfgnsOnCnfg);
                cnfgUO.setUnreleasedOrderQty(numberOfUnreleasedMfgnsOnCnfg);
                
                cnfgIndex++;
            }
            
            // Remove the empty config nodes from the tree. 
            // We go backwards so the node index will be accurate.
            for (int y=cnfgNodesToDelete.size()-1; y>=0; y--)
            {
                shpnNode.remove((Integer)cnfgNodesToDelete.elementAt(y));
            }

            // If there are no configs on this ship entity node,
            // we don't want to show the ship entity either.
            if (fieldMode==BAAPPLY && shpnNode.getChildCount()<1)
            {
                shpnNodesToDelete.add(shpnIndex);
            }
            
            shpnIndex++;
        }

        // Remove the empty ship entity nodes from the tree. 
        // We go backwards so the node index will be accurate.
        for (int y=shpnNodesToDelete.size()-1; y>=0; y--)
        {
            root.remove((Integer)shpnNodesToDelete.elementAt(y));
        }
        
        // add the released/unreleased nodes into the structure 
        root = addReleasedNodes(root);
        
        // add the schd nodes into the structure
        root = addSchdNodes(root);

        // when we get here, we have a whole new tree, so update the screen
        this.fieldLeftTreeModel.setRoot(root);
        
        // Get allocated quantity set up everywhere.
        updateAllocatedQuantity(root, configsReleased, "Y");
        
        // add the schd nodes into the structure
        root = addCoverageNodes(root);

        // Now lets get the order right
        root = setNodeOrder(root);
        
        if (this.fieldMode==BARELEASE)
        {
            // Finally, let's expand all the unreleased nodes
            expandUnreleased(root);
        }
        else
        {
            expandToShpnNodes(root);
        }
    }
    
    /**
     * Add the released nodes in
     * @param root
     * @return
     */
    @SuppressWarnings("rawtypes")
	private DefaultMutableTreeNode addReleasedNodes(DefaultMutableTreeNode root)
    {
        MFSBuildAheadTopUO topUO = new MFSBuildAheadTopUO();
        DefaultMutableTreeNode newRoot = new DefaultMutableTreeNode(topUO);
        
        for (Enumeration shpnEnum = root.children(); shpnEnum.hasMoreElements();) 
        {
            DefaultMutableTreeNode shpnNode = (DefaultMutableTreeNode) shpnEnum.nextElement();

            DefaultMutableTreeNode newShpnNode = copyShpnNode(shpnNode);

            MFSBuildAheadReleasedUO releasedUO = new MFSBuildAheadReleasedUO(true);
            DefaultMutableTreeNode releasedNode = new DefaultMutableTreeNode(releasedUO);

            MFSBuildAheadReleasedUO unreleasedUO = new MFSBuildAheadReleasedUO(false);
            DefaultMutableTreeNode unreleasedNode = new DefaultMutableTreeNode(unreleasedUO);

            boolean releasedCnfgsOnShpn = false;
            boolean unreleasedCnfgsOnShpn = false;
            
            for (Enumeration cnfgEnum = shpnNode.children(); cnfgEnum.hasMoreElements();) 
            {
                DefaultMutableTreeNode cnfgNode = (DefaultMutableTreeNode) cnfgEnum.nextElement();

                DefaultMutableTreeNode releasedCnfgNode = copyCnfgNode(cnfgNode, true);
                DefaultMutableTreeNode unreleasedCnfgNode = copyCnfgNode(cnfgNode, false);
                
                boolean releasedMfgnsOnCnfg = false;
                boolean unreleasedMfgnsOnCnfg = false;
                for (Enumeration mfgnEnum = cnfgNode.children(); mfgnEnum.hasMoreElements();) 
                {
                    DefaultMutableTreeNode mfgnNode = (DefaultMutableTreeNode) mfgnEnum.nextElement();
                    MFSBuildAheadMfgnUO mfgnUO = (MFSBuildAheadMfgnUO) mfgnNode.getUserObject();

                    MFSBuildAheadMfgnUO newMfgnUO = new MFSBuildAheadMfgnUO(mfgnUO.getMfgn());
                    newMfgnUO.setRlsed(mfgnUO.getRlsed());
                    DefaultMutableTreeNode newMfgnNode = new DefaultMutableTreeNode(newMfgnUO);
                    
                    if (mfgnUO.getRlsed().equals("Y"))
                    {
                        releasedMfgnsOnCnfg = true;
                        releasedCnfgNode.add(newMfgnNode);
                    }
                    else
                    {
                        unreleasedMfgnsOnCnfg = true;
                        unreleasedCnfgNode.add(newMfgnNode);
                    }
                }
                
                if (releasedMfgnsOnCnfg)
                {
                    releasedCnfgsOnShpn = true;
                    releasedNode.add(releasedCnfgNode);
                }
                if (unreleasedMfgnsOnCnfg)
                {
                    unreleasedCnfgsOnShpn = true;
                    unreleasedNode.add(unreleasedCnfgNode);
                }
            }

            if (releasedCnfgsOnShpn)
            {
                newShpnNode.add(releasedNode);
            }
            if (unreleasedCnfgsOnShpn)
            {
                newShpnNode.add(unreleasedNode);
            }
            if (releasedCnfgsOnShpn || unreleasedCnfgsOnShpn)
            {
                newRoot.add(newShpnNode);
            }
        }
        
        return newRoot;
    }
    
    /**
     * Add the schd nodes in
     * @param root
     * @return
     */
    @SuppressWarnings("rawtypes")
	private DefaultMutableTreeNode addSchdNodes(DefaultMutableTreeNode root)
    {
        // create a hash of scheduled ship dates
        Hashtable<String, DefaultMutableTreeNode> shipDates = new Hashtable<String, DefaultMutableTreeNode>();
        for (Enumeration shpnEnum = root.children(); shpnEnum.hasMoreElements();) 
        {
            DefaultMutableTreeNode shpnNode = (DefaultMutableTreeNode) shpnEnum.nextElement();
            MFSBuildAheadShpnUO shpnUO = (MFSBuildAheadShpnUO) shpnNode.getUserObject();
            
            DefaultMutableTreeNode newShpnNode = copyShpnNode(shpnNode);

            for (Enumeration releasedEnum = shpnNode.children(); releasedEnum.hasMoreElements();) 
            {
                DefaultMutableTreeNode releasedNode = (DefaultMutableTreeNode) releasedEnum.nextElement();
                DefaultMutableTreeNode newReleasedNode = copyReleasedNode(releasedNode);

                for (Enumeration cnfgEnum = releasedNode.children(); cnfgEnum.hasMoreElements();) 
                {
                    DefaultMutableTreeNode cnfgNode = (DefaultMutableTreeNode) cnfgEnum.nextElement();
                    DefaultMutableTreeNode newCnfgNode = copyCnfgNode(cnfgNode, true);

                    for (Enumeration mfgnEnum = cnfgNode.children(); mfgnEnum.hasMoreElements();) 
                    {
                        DefaultMutableTreeNode mfgnNode = (DefaultMutableTreeNode) mfgnEnum.nextElement();
                        DefaultMutableTreeNode newMfgnNode = copyMfgnNode(mfgnNode);
                        
                        newCnfgNode.add(newMfgnNode);
                    }
                    
                    newReleasedNode.add(newCnfgNode);
                }
                
                newShpnNode.add(newReleasedNode);
            }
            
            if (shipDates.containsKey(shpnUO.getSchd()))
            {
                DefaultMutableTreeNode schdNode = (DefaultMutableTreeNode)shipDates.get(shpnUO.getSchd());
                schdNode.add(newShpnNode);
                
                shipDates.put(shpnUO.getSchd(), schdNode);
            }
            else
            {
                MFSBuildAheadSchdUO schdUO = new MFSBuildAheadSchdUO(shpnUO.getSchd());
                DefaultMutableTreeNode schdNode = new DefaultMutableTreeNode(schdUO);
                
                schdNode.add(newShpnNode);
                
                shipDates.put(shpnUO.getSchd(), schdNode);
            }
        }

        // now add everthing to a new root
        MFSBuildAheadTopUO topUO = new MFSBuildAheadTopUO();
        DefaultMutableTreeNode newRoot = new DefaultMutableTreeNode(topUO);
        
        for (Enumeration keysEnum = shipDates.keys(); keysEnum.hasMoreElements();)
        {
            newRoot.add(shipDates.get((String) keysEnum.nextElement()));
        }
        
        return newRoot;
    }
    
    /**
     * Add the coverage nodes into the tree. If there is enough in stock to cover the orders,
     * node will say coverage. If not enough stock, node will say No Coverage. If the order
     * has already been released, the node says Released to alleviate confusion.
     * @param root
     * @return
     */
    @SuppressWarnings("rawtypes")
	private DefaultMutableTreeNode addCoverageNodes(DefaultMutableTreeNode root)
    {
        MFSBuildAheadTopUO topUO = new MFSBuildAheadTopUO();
        DefaultMutableTreeNode newRoot = new DefaultMutableTreeNode(topUO);

        for (Enumeration schdEnum = root.children(); schdEnum.hasMoreElements();) 
        {
            DefaultMutableTreeNode schdNode = (DefaultMutableTreeNode) schdEnum.nextElement();
            DefaultMutableTreeNode newSchdNode = copySchdNode(schdNode);
            
            for (Enumeration shpnEnum = schdNode.children(); shpnEnum.hasMoreElements();) 
            {
                DefaultMutableTreeNode shpnNode = (DefaultMutableTreeNode) shpnEnum.nextElement();
                DefaultMutableTreeNode newShpnNode = copyShpnNode(shpnNode);
    
                for (Enumeration releasedEnum = shpnNode.children(); releasedEnum.hasMoreElements();) 
                {
                    DefaultMutableTreeNode releasedNode = (DefaultMutableTreeNode) releasedEnum.nextElement();
                    DefaultMutableTreeNode newReleasedNode = copyReleasedNode(releasedNode);
                    
                    MFSBuildAheadReleasedUO releasedUO = (MFSBuildAheadReleasedUO)newReleasedNode.getUserObject();
                    
                    boolean haveCoverageCfgns = false;
                    boolean haveNoCoverageCfgns = false;
                    
                    MFSBuildAheadCoverageUO newCoverageUO = new MFSBuildAheadCoverageUO(true, releasedUO.isReleased());
                    DefaultMutableTreeNode newCoverageNode = new DefaultMutableTreeNode(newCoverageUO);
                    
                    MFSBuildAheadCoverageUO newNoCoverageUO = new MFSBuildAheadCoverageUO(false, releasedUO.isReleased());
                    DefaultMutableTreeNode newNoCoverageNode = new DefaultMutableTreeNode(newNoCoverageUO);
                    
                    for (Enumeration cnfgEnum = releasedNode.children(); cnfgEnum.hasMoreElements();) 
                    {
                        DefaultMutableTreeNode cnfgNode = (DefaultMutableTreeNode) cnfgEnum.nextElement();
                        MFSBuildAheadCnfgUO cnfgUO = (MFSBuildAheadCnfgUO)cnfgNode.getUserObject();
                        cnfgUO.setIsReleased(releasedUO.isReleased());
                        DefaultMutableTreeNode newCnfgNode = copyCnfgNode(cnfgNode, releasedUO.isReleased());
    
                        int numberOfMfgnsOnCnfg = 0;
                        for (Enumeration mfgnEnum = cnfgNode.children(); mfgnEnum.hasMoreElements();) 
                        {
                            DefaultMutableTreeNode mfgnNode = (DefaultMutableTreeNode) mfgnEnum.nextElement();
                            DefaultMutableTreeNode newMfgnNode = copyMfgnNode(mfgnNode);
                            newCnfgNode.add(newMfgnNode);
                            numberOfMfgnsOnCnfg++;
                        }
                        
                        MFSBuildAheadCnfgUO newCnfgUO = (MFSBuildAheadCnfgUO)newCnfgNode.getUserObject();
                        newCnfgUO.setOrderQty(numberOfMfgnsOnCnfg);
                        
                        if (newCnfgUO.haveCoverage())
                        {
                            haveCoverageCfgns = true;
                            newCoverageNode.add(newCnfgNode);
                        }
                        else
                        {
                            haveNoCoverageCfgns = true;
                            newNoCoverageNode.add(newCnfgNode);
                        }
                    }
                    
                    if (haveCoverageCfgns)
                    {
                        newReleasedNode.add(newCoverageNode);
                    }
                    if (haveNoCoverageCfgns)
                    {
                        newReleasedNode.add(newNoCoverageNode);
                    }
                    newShpnNode.add(newReleasedNode);
                }
                newSchdNode.add(newShpnNode);
            }
            newRoot.add(newSchdNode);
        }
        return newRoot;
    }
    
    private DefaultMutableTreeNode copySchdNode(DefaultMutableTreeNode schdNode)
    {
        MFSBuildAheadSchdUO schdUO = (MFSBuildAheadSchdUO) schdNode.getUserObject();
        MFSBuildAheadSchdUO newSchdUO = new MFSBuildAheadSchdUO(schdUO.getSchd());
        DefaultMutableTreeNode newSchdNode = new DefaultMutableTreeNode(newSchdUO);
        
        return newSchdNode;
    }
    
    private DefaultMutableTreeNode copyReleasedNode(DefaultMutableTreeNode releasedNode)
    {
        MFSBuildAheadReleasedUO releasedUO = (MFSBuildAheadReleasedUO) releasedNode.getUserObject();
        MFSBuildAheadReleasedUO newReleasedUO = new MFSBuildAheadReleasedUO(releasedUO.isReleased());
        DefaultMutableTreeNode newReleasedNode = new DefaultMutableTreeNode(newReleasedUO);
        
        return newReleasedNode;
    }
    
    private DefaultMutableTreeNode copyShpnNode(DefaultMutableTreeNode shpnNode)
    {
        MFSBuildAheadShpnUO shpnUO = (MFSBuildAheadShpnUO) shpnNode.getUserObject();
        MFSBuildAheadShpnUO newShpnUO = new MFSBuildAheadShpnUO(shpnUO.getShpn());
        newShpnUO.setPLOM(shpnUO.getPlom());
        newShpnUO.setSCHD(shpnUO.getSchd());
        newShpnUO.setUnreleasedOrderQty(shpnUO.getUnreleasedOrderQuantity());
        DefaultMutableTreeNode newShpnNode = new DefaultMutableTreeNode(newShpnUO);
        
        return newShpnNode;
    }
    
    private DefaultMutableTreeNode copyCnfgNode(DefaultMutableTreeNode cnfgNode, boolean isReleased)
    {
        MFSBuildAheadCnfgUO cnfgUO = (MFSBuildAheadCnfgUO) cnfgNode.getUserObject();
        MFSBuildAheadCnfgUO newCnfgUO = new MFSBuildAheadCnfgUO(cnfgUO.getItem());
        newCnfgUO.setAllocatedQuantity(cnfgUO.getAllocatedQuantity());
        newCnfgUO.setEXISTS(cnfgUO.getEXISTS());
        newCnfgUO.setLOC(cnfgUO.getLOC());
        newCnfgUO.setLQTY(cnfgUO.getLQTY());
        newCnfgUO.setOrderQty(0);//cnfgUO.getOrderQty());
        newCnfgUO.setUnreleasedOrderQty(cnfgUO.getUnreleasedOrderQuantity());
        newCnfgUO.setWHS(cnfgUO.getWHS());
        newCnfgUO.setWQTY(cnfgUO.getWQTY());
        newCnfgUO.setIsReleased(isReleased);
        DefaultMutableTreeNode newCnfgNode = new DefaultMutableTreeNode(newCnfgUO);
        
        return newCnfgNode;
    }
    

    private DefaultMutableTreeNode copyMfgnNode(DefaultMutableTreeNode mfgnNode)
    {
        MFSBuildAheadMfgnUO mfgnUO = (MFSBuildAheadMfgnUO) mfgnNode.getUserObject();
        MFSBuildAheadMfgnUO newMfgnUO = new MFSBuildAheadMfgnUO(mfgnUO.getMfgn());
        newMfgnUO.setRlsed(mfgnUO.getRlsed());
        DefaultMutableTreeNode newMfgnNode = new DefaultMutableTreeNode(newMfgnUO);
        
        return newMfgnNode;
    }
    
    /**
     * This will order the nodes...
     * All the released stuff goes to the bottom (sorted by ship date)
     * Everthing else is sorted by ship date
     * @param root The tree root
     */
    @SuppressWarnings("rawtypes")
	private DefaultMutableTreeNode setNodeOrder(DefaultMutableTreeNode root)
    {
        // These are the unsorted vectors used for splitting up
        // ship entities based on whether they have unreleased
        // orders or not.
        Vector<DefaultMutableTreeNode> schdsWithSomeUnrlsedQty = new Vector<DefaultMutableTreeNode>();
        Vector<DefaultMutableTreeNode> schdsWithZeroUnrlsedQty = new Vector<DefaultMutableTreeNode>();

        for (Enumeration schdEnum = root.children(); schdEnum.hasMoreElements();) 
        {
            DefaultMutableTreeNode schdNode = (DefaultMutableTreeNode) schdEnum.nextElement();
            
            boolean haveUnreleasedMfgns = false;
            for (Enumeration shpnEnum = schdNode.children(); shpnEnum.hasMoreElements();) 
            {
                DefaultMutableTreeNode shpnNode = (DefaultMutableTreeNode) shpnEnum.nextElement();
                MFSBuildAheadShpnUO shpnUO = (MFSBuildAheadShpnUO) shpnNode.getUserObject();
                
                if (shpnUO.getUnreleasedOrderQuantity()!=0)
                {
                    haveUnreleasedMfgns = true;
                }
            }
            
            if (haveUnreleasedMfgns)
            {
                schdsWithSomeUnrlsedQty.add(schdNode);
            }
            else
            {
                schdsWithZeroUnrlsedQty.add(schdNode);
            }
        }
    
        // Now we sort those lists separately by ship entity
        Vector orderedSchdsWithSomeUnrlsedQty = mergeSort(schdsWithSomeUnrlsedQty);
        Vector orderedSchdsWithZeroUnrlsedQty = mergeSort(schdsWithZeroUnrlsedQty);

        MFSBuildAheadTopUO topUO = new MFSBuildAheadTopUO();
        DefaultMutableTreeNode newRoot = new DefaultMutableTreeNode(topUO);
        
        for (int x=0; x<orderedSchdsWithSomeUnrlsedQty.size(); x++)
        {
            DefaultMutableTreeNode schdNodeFromVector = (DefaultMutableTreeNode)orderedSchdsWithSomeUnrlsedQty.elementAt(x);
            MFSBuildAheadSchdUO schdUOFromVector = (MFSBuildAheadSchdUO) schdNodeFromVector.getUserObject();
            
            boolean found = false;
            for (Enumeration schdEnum = root.children(); schdEnum.hasMoreElements() && !found;) 
            {
                DefaultMutableTreeNode schdNode = (DefaultMutableTreeNode) schdEnum.nextElement();
                MFSBuildAheadSchdUO schdUO = (MFSBuildAheadSchdUO) schdNode.getUserObject();
   
                if (schdUOFromVector.getSchd().equals(schdUO.getSchd()))
                {
                    found = true;
                    newRoot.add(schdNode);
                }
            }
        }
       
        for (int x=0; x<orderedSchdsWithZeroUnrlsedQty.size(); x++)
        {
            DefaultMutableTreeNode schdNodeFromVector = (DefaultMutableTreeNode)orderedSchdsWithZeroUnrlsedQty.elementAt(x);
            MFSBuildAheadSchdUO schdUOFromVector = (MFSBuildAheadSchdUO) schdNodeFromVector.getUserObject();
             
            boolean found = false;
            for (Enumeration schdEnum = root.children(); schdEnum.hasMoreElements() && !found;) 
            {
                DefaultMutableTreeNode schdNode = (DefaultMutableTreeNode) schdEnum.nextElement();
                MFSBuildAheadSchdUO schdUO = (MFSBuildAheadSchdUO) schdNode.getUserObject();
   
                if (schdUOFromVector.getSchd().equals(schdUO.getSchd()))
                {
                    found = true;
                    newRoot.add(schdNode);
                }
            }
        }
        
        // when we get here, we have a whole new tree, so update the screen
        this.fieldLeftTreeModel.setRoot(newRoot);
        
        return newRoot;
    }
    

    @SuppressWarnings("rawtypes")
	private void expandUnreleased(DefaultMutableTreeNode root)
    {
        int row = 0;
        // loop through schd nodes
        for (Enumeration schdEnum = root.children(); schdEnum.hasMoreElements();) 
        {
            DefaultMutableTreeNode schdNode = (DefaultMutableTreeNode) schdEnum.nextElement();

            row++;
            int schdRow = row;
            boolean isSchdExpanded = false;

            // loop through ship entity nodes
            for (Enumeration shpnEnum = schdNode.children(); shpnEnum.hasMoreElements();) 
            {
                DefaultMutableTreeNode shpnNode = (DefaultMutableTreeNode) shpnEnum.nextElement();

                row++;
                int shpnRow = row; 
                boolean isShpnExpanded = false;
                
                // loop through released nodes
                for (Enumeration releasedEnum = shpnNode.children(); releasedEnum.hasMoreElements();) 
                {
                    DefaultMutableTreeNode releasedNode = (DefaultMutableTreeNode) releasedEnum.nextElement();
                    MFSBuildAheadReleasedUO releasedUO = (MFSBuildAheadReleasedUO) releasedNode.getUserObject();
                    
                    row++;
                    int releasedRow = row;
                    boolean isReleasedExpanded = false;
    
                    if (!releasedUO.isReleased())
                    {
                        // loop through coverage
                        for (Enumeration coverageEnum = releasedNode.children(); coverageEnum.hasMoreElements();) 
                        {
                            DefaultMutableTreeNode coverageNode = (DefaultMutableTreeNode) coverageEnum.nextElement();

                            row++;
                            int coverageRow = row; 
                            boolean isCoverageExpanded = false;
                            
                            // loop through configs
                            for (Enumeration cnfgEnum = coverageNode.children(); cnfgEnum.hasMoreElements();) 
                            {
                                DefaultMutableTreeNode cnfgNode = (DefaultMutableTreeNode) cnfgEnum.nextElement();
                                
                                row++;
                                int cnfgRow = row; 
                                boolean isCnfgExpanded = false;
        
                                // loop through mfgns
                                for (Enumeration mfgnEnum = cnfgNode.children(); mfgnEnum.hasMoreElements();) 
                                {
                                    DefaultMutableTreeNode mfgnNode = (DefaultMutableTreeNode) mfgnEnum.nextElement();
                                    MFSBuildAheadMfgnUO mfgnUO = (MFSBuildAheadMfgnUO) mfgnNode.getUserObject();
                                    
                                    row++;
                                    
                                    if (mfgnUO.getRlsed().equals("N"))
                                    {
                                        isCnfgExpanded = true;
                                        isCoverageExpanded = true;
                                        isReleasedExpanded = true;
                                        isShpnExpanded = true;
                                        isSchdExpanded = true;
                                    }
                                }
                                
                                if (isCnfgExpanded)
                                {
                                    // if any MFGN on this config node is not 
                                    // released, expand all the nodes under it
                                    trLeftTree.expandRow(schdRow);
                                    trLeftTree.expandRow(shpnRow);
                                    trLeftTree.expandRow(releasedRow);
                                    trLeftTree.expandRow(coverageRow);
                                    for (int x=cnfgRow; x<=row; x++)
                                    {
                                        trLeftTree.expandRow(x);
                                    }
                                }
                                else
                                {
                                    row = cnfgRow;
                                }
                            }
                            
                            if (!isCoverageExpanded)
                            {
                                row = coverageRow;
                            }
                        }
                    }
                    
                    if (!isReleasedExpanded)
                    {
                        row = releasedRow;
                    }
                }
                
                if (!isShpnExpanded)
                {
                    row = shpnRow;
                }
            }
            
            if (!isSchdExpanded)
            {
                row = schdRow;
            }
        }
    }
    
    @SuppressWarnings("rawtypes")
	private void expandToShpnNodes(DefaultMutableTreeNode root)
    {
        int row = 0;
        trLeftTree.expandRow(row);
        
        // loop through schd nodes
        for (Enumeration schdEnum = root.children(); schdEnum.hasMoreElements();) 
        {
            DefaultMutableTreeNode schdNode = (DefaultMutableTreeNode) schdEnum.nextElement();

            row++;
            trLeftTree.expandRow(row);

            // loop through ship entity nodes
            for (Enumeration shpnEnum = schdNode.children(); shpnEnum.hasMoreElements();shpnEnum.nextElement()) 
            {
                row++;
            }
        }
    }    

	/**
	 * Collapses the trees down to the roots.
	 * <p>
	 * Invoked when {@link #pbCollapse} is selected.
	 */
	private void collapseTrees() { 
		collapseTree(this.trLeftTree);
	}

	
	/**
	 * Ends the build ahead by performing the following steps:
	 * <ol>
     * <li>Calls END_BA.</li>
	 * </ol>
	 * Invoked when {@link #pbEnd} is selected.
	 */
	private void endBuildAhead() {
		boolean cancel = false;
		try {
			removeMyListeners();
			if (!cancel) {
                if (this.fieldMode==BARELEASE && this.locked.equals("Y")) {
                    submitEndBuildAhead("Ending Build Ahead...");
                }
				getParentFrame().restorePreviousScreen(this);
			}
		} catch (Exception e) {
			String title = "End Build Ahead Error";
			IGSMessageBox.showOkMB(getParentFrame(), title, null, e);
		} finally {
			addMyListeners();
		}
	}


	/**
	 * Expands the trees.
	 * <p>
	 * Invoked when {@link #pbExpand} is selected.
	 */
	private void expandTrees() {
		expandTree(this.trLeftTree);
	}
	
	
	/**
	 * Prompts the user for a container number and finds that node, highlighting
	 * it and displaying it in the tree.
	 * <p>
	 * Invoked when {@link #pbFind} is selected.
	 */
	private void findNode() {
		try {
			removeMyListeners();
			String shpnNum = getShipEntity("Find", 'f');
			if (shpnNum != null) {
				findNode(shpnNum);
			}
		} catch (Exception e) {
			String title = "Error Finding Ship Entity";
			IGSMessageBox.showOkMB(getParentFrame(), title, null, e);
		} finally {
			this.trLeftTree.repaint();
			addMyListeners();
		}
	}

	
	/**
	 * Finds and highlights the container given by <code>shpnNum</code>.
	 * @param shpnNum the ship entity to find and highlight
	 * @return true if the ship entity was found
	 */
	@SuppressWarnings("rawtypes")
	private boolean findNode(String shpnNum) {
		boolean retval = true;
		this.trLeftTree.clearSelection();

		DefaultMutableTreeNode shpnNode = getNode(shpnNum);
		if (shpnNode == null) {
			retval = false;
		} else {
			collapseTree(this.trLeftTree);

            // This is a bit confusing...
            // First find the actual ship entity node and expand it
            MFSBuildAheadShpnUO shpnUO = (MFSBuildAheadShpnUO) shpnNode.getUserObject();
			this.taNodeInfo.setText(shpnUO.toString());

			DefaultMutableTreeNode[] fndNodes = findNodeInTree(
					(DefaultMutableTreeNode) this.fieldLeftTreeModel.getRoot(),
                    shpnUO.getShpn());
			for (int i = 0; i < fndNodes.length; i++) {
				this.trLeftTree.makeVisible(new TreePath(fndNodes[i].getPath()));
                this.trLeftTree.setSelectionPath(new TreePath(fndNodes[i].getPath()));
                this.trLeftTree.expandPath(new TreePath(fndNodes[i].getPath()));
			}

            // Then expand the config children of that ship entity
            for (Enumeration cnfgEnum = shpnNode.children(); cnfgEnum.hasMoreElements();) {
                DefaultMutableTreeNode cnfgNode = (DefaultMutableTreeNode) cnfgEnum.nextElement();
                
                this.trLeftTree.expandPath(new TreePath(cnfgNode.getPath()));

                // Now expand the mfgn children of that config
                for (Enumeration mfgnEnum = cnfgNode.children(); mfgnEnum.hasMoreElements();) {
                    DefaultMutableTreeNode mfgnNode = (DefaultMutableTreeNode) mfgnEnum.nextElement();
                    this.trLeftTree.expandPath(new TreePath(mfgnNode.getPath()));
                }
            }
		}
		return retval;
	}	

	
    /**
     * Release the Build Ahead order
     * <p>
     * Invoked when {@link #pbRelease} is selected.
     */
    @SuppressWarnings("rawtypes")
	private void release() {
        // This Hashtable will store the number of each config to be released
        Hashtable<String, Integer> configsReleased = new Hashtable<String, Integer>();
        
        try {
            removeMyListeners();

            TreePath defPath = this.trLeftTree.getSelectionPath();
            if (defPath==null) 
            {
                throw new Exception("Please choose a ship entity to release.");
            } 
            else
            {
                DefaultMutableTreeNode defNode = (DefaultMutableTreeNode) defPath.getLastPathComponent();
                Object obj  = defNode.getUserObject();
                if (obj instanceof MFSBuildAheadTopUO) {
                    throw new Exception("Please choose a ship entity to release.");
                }
                else if (defPath.getPathCount() <= 1) 
                {
                    throw new Exception("Error finding selected node.");
                }
                else 
                {
                    final MFSConfig config = MFSConfig.getInstance();
                    IGSXMLTransaction updBA = new IGSXMLTransaction("UPD_BA"); 
                    updBA.setActionMessage("Releasing build ahead ...");
                    updBA.startDocument();
                    updBA.addElement("CELL", config.get8CharCell()); 
                    updBA.addElement("CTYP", config.get8CharCellType());
                    updBA.addElement("USER", config.get8CharUser());
                    updBA.addElement("ACTION", "RELEASE");
                    updBA.startElement("MFGNS");
    
                    if (obj instanceof MFSBuildAheadSchdUO) {
                        // If a shpn node was clicked, release all the MFGNs on that SHPN
                        // loop through ship entities
                        for (Enumeration shpnEnum = defNode.children(); shpnEnum.hasMoreElements();) {
                            DefaultMutableTreeNode shpnNode = (DefaultMutableTreeNode) shpnEnum.nextElement();
                            MFSBuildAheadShpnUO shpnUO = (MFSBuildAheadShpnUO) shpnNode.getUserObject();
                            
                            // loop through released nodes
                            for (Enumeration releasedEnum = shpnNode.children(); releasedEnum.hasMoreElements();) {
                                DefaultMutableTreeNode releasedNode = (DefaultMutableTreeNode) releasedEnum.nextElement();

                                // loop through coverage nodes
                                for (Enumeration coverageEnum = releasedNode.children(); coverageEnum.hasMoreElements();) {
                                    DefaultMutableTreeNode coverageNode = (DefaultMutableTreeNode) coverageEnum.nextElement();

                                    // loop through configs
                                    for (Enumeration cnfgEnum = coverageNode.children(); cnfgEnum.hasMoreElements();) {
                                        DefaultMutableTreeNode cnfgNode = (DefaultMutableTreeNode) cnfgEnum.nextElement();
                                        MFSBuildAheadCnfgUO cnfgUO = (MFSBuildAheadCnfgUO) cnfgNode.getUserObject();

                                        int releasedConfigQuantity = 0;
                                        
                                        // loop through mfgns
                                        for (Enumeration mfgnEnum = cnfgNode.children(); mfgnEnum.hasMoreElements();) {                                
                                            DefaultMutableTreeNode mfgnNode = (DefaultMutableTreeNode) mfgnEnum.nextElement();
                                            MFSBuildAheadMfgnUO mfgnUO = (MFSBuildAheadMfgnUO) mfgnNode.getUserObject();
                                            
                                            if (mfgnUO.getRlsed().equals("N")) {
                                                releasedConfigQuantity++;
                                                
                                                updBA.startElement("SMFGN");
                                                updBA.addElement("SHPN", shpnUO.getShpn()); 
                                                updBA.addElement("MFGN", mfgnUO.getMfgn()); 
                                                updBA.addElement("ITEM", cnfgUO.getItem()); 
                                                updBA.endElement("SMFGN");
                                            }
                                        }

                                        // Add this config with the number of MFGNs released
                                        if (configsReleased.containsKey(cnfgUO.getItem()))
                                        {
                                            int num = configsReleased.get(cnfgUO.getItem());
                                            num = num + releasedConfigQuantity;
                                            
                                            configsReleased.remove(cnfgUO.getItem());
                                            configsReleased.put(cnfgUO.getItem(), num);
                                        }
                                        else
                                        {
                                            configsReleased.put(cnfgUO.getItem(), releasedConfigQuantity);
                                        }
                                    }
                                }
                            }
                        }
                    }
                    else if (obj instanceof MFSBuildAheadShpnUO) {
                        // If a shpn node was clicked, release all the MFGNs on that SHPN
                        MFSBuildAheadShpnUO shpnUO = (MFSBuildAheadShpnUO) defNode.getUserObject();
                        
                        // loop through released nodes
                        for (Enumeration releasedEnum = defNode.children(); releasedEnum.hasMoreElements();) {
                            DefaultMutableTreeNode releasedNode = (DefaultMutableTreeNode) releasedEnum.nextElement();

                            // loop through coverage nodes
                            for (Enumeration coverageEnum = releasedNode.children(); coverageEnum.hasMoreElements();) {
                                DefaultMutableTreeNode coverageNode = (DefaultMutableTreeNode) coverageEnum.nextElement();

                                // loop through configs
                                for (Enumeration cnfgEnum = coverageNode.children(); cnfgEnum.hasMoreElements();) {
                                    DefaultMutableTreeNode cnfgNode = (DefaultMutableTreeNode) cnfgEnum.nextElement();
                                    MFSBuildAheadCnfgUO cnfgUO = (MFSBuildAheadCnfgUO) cnfgNode.getUserObject();
                                    
                                    int releasedConfigQuantity = 0;
                                    
                                    // loop through mfgns
                                    for (Enumeration mfgnEnum = cnfgNode.children(); mfgnEnum.hasMoreElements();) {                                
                                        DefaultMutableTreeNode mfgnNode = (DefaultMutableTreeNode) mfgnEnum.nextElement();
                                        MFSBuildAheadMfgnUO mfgnUO = (MFSBuildAheadMfgnUO) mfgnNode.getUserObject();
                                        
                                        if (mfgnUO.getRlsed().equals("N")) {
                                            releasedConfigQuantity++;
                                            
                                            updBA.startElement("SMFGN");
                                            updBA.addElement("SHPN", shpnUO.getShpn()); 
                                            updBA.addElement("MFGN", mfgnUO.getMfgn()); 
                                            updBA.addElement("ITEM", cnfgUO.getItem()); 
                                            updBA.endElement("SMFGN");
                                        }
                                    }
        
                                    // Add this config with the number of MFGNs released
                                    if (configsReleased.containsKey(cnfgUO.getItem()))
                                    {
                                        int num = configsReleased.get(cnfgUO.getItem());
                                        num = num + releasedConfigQuantity;
                                        
                                        configsReleased.remove(cnfgUO.getItem());
                                        configsReleased.put(cnfgUO.getItem(), num);
                                    }
                                    else
                                    {
                                        configsReleased.put(cnfgUO.getItem(), releasedConfigQuantity);
                                    }
                                }
                            }
                        }
                    }
                    else if (obj instanceof MFSBuildAheadReleasedUO) {
                        // If a released node was clicked, release all the MFGNs on that node
                        DefaultMutableTreeNode shpnNode = (DefaultMutableTreeNode)defNode.getParent();
                        MFSBuildAheadShpnUO shpnUO = (MFSBuildAheadShpnUO) shpnNode.getUserObject();
                        
                        // loop through coverage nodes
                        for (Enumeration coverageEnum = defNode.children(); coverageEnum.hasMoreElements();) {
                            DefaultMutableTreeNode coverageNode = (DefaultMutableTreeNode) coverageEnum.nextElement();

                            // loop through configs
                            for (Enumeration cnfgEnum = coverageNode.children(); cnfgEnum.hasMoreElements();) {
                                DefaultMutableTreeNode cnfgNode = (DefaultMutableTreeNode) cnfgEnum.nextElement();
                                MFSBuildAheadCnfgUO cnfgUO = (MFSBuildAheadCnfgUO) cnfgNode.getUserObject();
                                
                                int releasedConfigQuantity = 0;
                                
                                // loop through mfgns
                                for (Enumeration mfgnEnum = cnfgNode.children(); mfgnEnum.hasMoreElements();) {                                
                                    DefaultMutableTreeNode mfgnNode = (DefaultMutableTreeNode) mfgnEnum.nextElement();
                                    MFSBuildAheadMfgnUO mfgnUO = (MFSBuildAheadMfgnUO) mfgnNode.getUserObject();
                                    
                                    if (mfgnUO.getRlsed().equals("N")) {
                                        releasedConfigQuantity++;
                                        
                                        updBA.startElement("SMFGN");
                                        updBA.addElement("SHPN", shpnUO.getShpn()); 
                                        updBA.addElement("MFGN", mfgnUO.getMfgn()); 
                                        updBA.addElement("ITEM", cnfgUO.getItem()); 
                                        updBA.endElement("SMFGN");
                                    }
                                }

                                // Add this config with the number of MFGNs released
                                if (configsReleased.containsKey(cnfgUO.getItem()))
                                {
                                    int num = configsReleased.get(cnfgUO.getItem());
                                    num = num + releasedConfigQuantity;
                                    
                                    configsReleased.remove(cnfgUO.getItem());
                                    configsReleased.put(cnfgUO.getItem(), num);
                                }
                                else
                                {
                                    configsReleased.put(cnfgUO.getItem(), releasedConfigQuantity);
                                }
                            }
                        }
                    }
                    else if (obj instanceof MFSBuildAheadCoverageUO) {
                        // If a coverage node was clicked, release all the MFGNs on that node
                        DefaultMutableTreeNode shpnNode = (DefaultMutableTreeNode)defNode.getParent().getParent();
                        MFSBuildAheadShpnUO shpnUO = (MFSBuildAheadShpnUO) shpnNode.getUserObject();
                        
                        // loop through configs
                        for (Enumeration cnfgEnum = defNode.children(); cnfgEnum.hasMoreElements();) {
                            DefaultMutableTreeNode cnfgNode = (DefaultMutableTreeNode) cnfgEnum.nextElement();
                            MFSBuildAheadCnfgUO cnfgUO = (MFSBuildAheadCnfgUO) cnfgNode.getUserObject();
                            
                            int releasedConfigQuantity = 0;
                            
                            // loop through mfgns
                            for (Enumeration mfgnEnum = cnfgNode.children(); mfgnEnum.hasMoreElements();) {                                
                                DefaultMutableTreeNode mfgnNode = (DefaultMutableTreeNode) mfgnEnum.nextElement();
                                MFSBuildAheadMfgnUO mfgnUO = (MFSBuildAheadMfgnUO) mfgnNode.getUserObject();
                                
                                if (mfgnUO.getRlsed().equals("N")) {
                                    releasedConfigQuantity++;
                                    
                                    updBA.startElement("SMFGN");
                                    updBA.addElement("SHPN", shpnUO.getShpn()); 
                                    updBA.addElement("MFGN", mfgnUO.getMfgn()); 
                                    updBA.addElement("ITEM", cnfgUO.getItem()); 
                                    updBA.endElement("SMFGN");
                                }
                            }

                            // Add this config with the number of MFGNs released
                            if (configsReleased.containsKey(cnfgUO.getItem()))
                            {
                                int num = configsReleased.get(cnfgUO.getItem());
                                num = num + releasedConfigQuantity;
                                
                                configsReleased.remove(cnfgUO.getItem());
                                configsReleased.put(cnfgUO.getItem(), num);
                            }
                            else
                            {
                                configsReleased.put(cnfgUO.getItem(), releasedConfigQuantity);
                            }
                        }
                    }
                    else if (obj instanceof MFSBuildAheadCnfgUO) {
                        // If a config node was clicked, release all the MFGNs on that config
                        int releasedConfigQuantity = 0;
                        
                        DefaultMutableTreeNode shpnNode = (DefaultMutableTreeNode)defNode.getParent().getParent().getParent();
                        MFSBuildAheadShpnUO shpnUO = (MFSBuildAheadShpnUO) shpnNode.getUserObject();
                        
                        MFSBuildAheadCnfgUO cnfgUO = (MFSBuildAheadCnfgUO) defNode.getUserObject();
                        
                        // loop through mfgns
                        for (Enumeration mfgnEnum = defNode.children(); mfgnEnum.hasMoreElements();) {
                            DefaultMutableTreeNode mfgnNode = (DefaultMutableTreeNode) mfgnEnum.nextElement();
                            MFSBuildAheadMfgnUO mfgnUO = (MFSBuildAheadMfgnUO) mfgnNode.getUserObject();
                            
                            if (mfgnUO.getRlsed().equals("N")) {
                                releasedConfigQuantity++;
                                
                                updBA.startElement("SMFGN");
                                updBA.addElement("SHPN", shpnUO.getShpn()); 
                                updBA.addElement("MFGN", mfgnUO.getMfgn()); 
                                updBA.addElement("ITEM", cnfgUO.getItem());
                                updBA.endElement("SMFGN");
                            }
                        }

                        // Add this config with the number of MFGNs released
                        configsReleased.put(cnfgUO.getItem(), releasedConfigQuantity);
                    }  else if (obj instanceof MFSBuildAheadMfgnUO) {
                        // If an MFGN node was clicked, release only that MFGN
                        DefaultMutableTreeNode shpnNode = (DefaultMutableTreeNode)defNode.getParent().getParent().getParent().getParent();
                        MFSBuildAheadShpnUO shpnUO = (MFSBuildAheadShpnUO) shpnNode.getUserObject();
                        
                        DefaultMutableTreeNode cnfgNode = (DefaultMutableTreeNode)defNode.getParent();
                        MFSBuildAheadCnfgUO cnfgUO = (MFSBuildAheadCnfgUO) cnfgNode.getUserObject();

                        MFSBuildAheadMfgnUO mfgnUO = (MFSBuildAheadMfgnUO) defNode.getUserObject();
                        
                        if (mfgnUO.getRlsed().equals("N")) {
                            updBA.startElement("SMFGN");
                            updBA.addElement("SHPN", shpnUO.getShpn()); 
                            updBA.addElement("MFGN", mfgnUO.getMfgn()); 
                            updBA.addElement("ITEM", cnfgUO.getItem());
                            updBA.endElement("SMFGN");

                            // Add this config with the number of MFGNs released
                            configsReleased.put(cnfgUO.getItem(), 1);
                        }
                    }
                    
                    updBA.endElement("MFGNS");
                    updBA.endDocument();

                    MFSComm.getInstance().execute(updBA, this);
                    if (updBA.getReturnCode() != 0) {
                        throw new IGSTransactionException(updBA, true);
                    }
        
                    // If we got here, the release was 
                    // successful, so update the screen.
                    updateMfgnReleasedStatus(configsReleased, defNode, "Y");
                }
            }
        }  catch (Exception e) {
            String title = "Release Build Ahead Error";
            IGSMessageBox.showOkMB(getParentFrame(), title, null, e);
        } finally {
            addMyListeners();
        }
    }

    
    /**
     * Un-Release the Build Ahead order
     * <p>
     * Invoked when {@link #pbUnRelease} is selected.
     */
    @SuppressWarnings("rawtypes")
	private void unRelease() {
        // This Hashtable will store the number of each config to be released
        Hashtable<String, Integer> configsUnReleased = new Hashtable<String, Integer>();
        
        try {
            removeMyListeners();

            TreePath defPath = this.trLeftTree.getSelectionPath();
            if (defPath==null) 
            {
                throw new Exception("Please choose a ship entity to un-release.");
            }
            else
            {      
                DefaultMutableTreeNode defNode = (DefaultMutableTreeNode) defPath.getLastPathComponent();
                Object obj  = defNode.getUserObject();
                if (obj instanceof MFSBuildAheadTopUO) {
                    throw new Exception("Please choose a ship entity to release.");
                }
                else if (defPath.getPathCount() <= 1) 
                {
                    throw new Exception("Error finding selected node.");
                }
                else
                {
                    final MFSConfig config = MFSConfig.getInstance();
                    IGSXMLTransaction updBA = new IGSXMLTransaction("UPD_BA"); 
                    updBA.setActionMessage("Un-releasing build ahead ...");
                    updBA.startDocument();
                    updBA.addElement("CELL", config.get8CharCell()); 
                    updBA.addElement("CTYP", config.get8CharCellType());
                    updBA.addElement("USER", config.get8CharUser());
                    updBA.addElement("ACTION", "UNRELEASE");
                    updBA.startElement("MFGNS");
                    
                    if (obj instanceof MFSBuildAheadSchdUO) {
                        // If a shpn node was clicked, release all the MFGNs on that SHPN
                        // loop through ship entities
                        for (Enumeration shpnEnum = defNode.children(); shpnEnum.hasMoreElements();) {
                            DefaultMutableTreeNode shpnNode = (DefaultMutableTreeNode) shpnEnum.nextElement();
                            MFSBuildAheadShpnUO shpnUO = (MFSBuildAheadShpnUO) shpnNode.getUserObject();
                            
                            // loop through released nodes
                            for (Enumeration releasedEnum = shpnNode.children(); releasedEnum.hasMoreElements();) {
                                DefaultMutableTreeNode releasedNode = (DefaultMutableTreeNode) releasedEnum.nextElement();
    
                                // loop through coverage nodes
                                for (Enumeration coverageEnum = releasedNode.children(); coverageEnum.hasMoreElements();) {
                                    DefaultMutableTreeNode coverageNode = (DefaultMutableTreeNode) coverageEnum.nextElement();
    
                                    // loop through configs
                                    for (Enumeration cnfgEnum = coverageNode.children(); cnfgEnum.hasMoreElements();) {
                                        DefaultMutableTreeNode cnfgNode = (DefaultMutableTreeNode) cnfgEnum.nextElement();
                                        MFSBuildAheadCnfgUO cnfgUO = (MFSBuildAheadCnfgUO) cnfgNode.getUserObject();
    
                                        int releasedConfigQuantity = 0;
                                        
                                        // loop through mfgns
                                        for (Enumeration mfgnEnum = cnfgNode.children(); mfgnEnum.hasMoreElements();) {                                
                                            DefaultMutableTreeNode mfgnNode = (DefaultMutableTreeNode) mfgnEnum.nextElement();
                                            MFSBuildAheadMfgnUO mfgnUO = (MFSBuildAheadMfgnUO) mfgnNode.getUserObject();
                                            
                                            if (mfgnUO.getRlsed().equals("Y")) {
                                                releasedConfigQuantity++;
                                                
                                                updBA.startElement("SMFGN");
                                                updBA.addElement("SHPN", shpnUO.getShpn()); 
                                                updBA.addElement("MFGN", mfgnUO.getMfgn()); 
                                                updBA.addElement("ITEM", cnfgUO.getItem()); 
                                                updBA.endElement("SMFGN");
                                            }
                                        }
    
                                        // Add this config with the number of MFGNs released
                                        if (configsUnReleased.containsKey(cnfgUO.getItem()))
                                        {
                                            int num = configsUnReleased.get(cnfgUO.getItem());
                                            num = num + releasedConfigQuantity;
                                            
                                            configsUnReleased.remove(cnfgUO.getItem());
                                            configsUnReleased.put(cnfgUO.getItem(), num);
                                        }
                                        else
                                        {
                                            configsUnReleased.put(cnfgUO.getItem(), releasedConfigQuantity);
                                        }
                                    }
                                }
                            }
                        }
                    }
                    else if (obj instanceof MFSBuildAheadShpnUO) {
                        // If a shpn node was clicked, release all the MFGNs on that SHPN
                        MFSBuildAheadShpnUO shpnUO = (MFSBuildAheadShpnUO) defNode.getUserObject();
                        
                        // loop through released nodes
                        for (Enumeration releasedEnum = defNode.children(); releasedEnum.hasMoreElements();) {
                            DefaultMutableTreeNode releasedNode = (DefaultMutableTreeNode) releasedEnum.nextElement();
    
                            // loop through coverage nodes
                            for (Enumeration coverageEnum = releasedNode.children(); coverageEnum.hasMoreElements();) {
                                DefaultMutableTreeNode coverageNode = (DefaultMutableTreeNode) coverageEnum.nextElement();
    
                                // loop through configs
                                for (Enumeration cnfgEnum = coverageNode.children(); cnfgEnum.hasMoreElements();) {
                                    DefaultMutableTreeNode cnfgNode = (DefaultMutableTreeNode) cnfgEnum.nextElement();
                                    MFSBuildAheadCnfgUO cnfgUO = (MFSBuildAheadCnfgUO) cnfgNode.getUserObject();
                                    
                                    int releasedConfigQuantity = 0;
                                    
                                    // loop through mfgns
                                    for (Enumeration mfgnEnum = cnfgNode.children(); mfgnEnum.hasMoreElements();) {                                
                                        DefaultMutableTreeNode mfgnNode = (DefaultMutableTreeNode) mfgnEnum.nextElement();
                                        MFSBuildAheadMfgnUO mfgnUO = (MFSBuildAheadMfgnUO) mfgnNode.getUserObject();
                                        
                                        if (mfgnUO.getRlsed().equals("Y")) {
                                            releasedConfigQuantity++;
                                            
                                            updBA.startElement("SMFGN");
                                            updBA.addElement("SHPN", shpnUO.getShpn()); 
                                            updBA.addElement("MFGN", mfgnUO.getMfgn()); 
                                            updBA.addElement("ITEM", cnfgUO.getItem()); 
                                            updBA.endElement("SMFGN");
                                        }
                                    }
        
                                    // Add this config with the number of MFGNs released
                                    if (configsUnReleased.containsKey(cnfgUO.getItem()))
                                    {
                                        int num = configsUnReleased.get(cnfgUO.getItem());
                                        num = num + releasedConfigQuantity;
                                        
                                        configsUnReleased.remove(cnfgUO.getItem());
                                        configsUnReleased.put(cnfgUO.getItem(), num);
                                    }
                                    else
                                    {
                                        configsUnReleased.put(cnfgUO.getItem(), releasedConfigQuantity);
                                    }
                                }
                            }
                        }
                    }
                    else if (obj instanceof MFSBuildAheadReleasedUO) {
                        // If a released node was clicked, release all the MFGNs on that node
                        DefaultMutableTreeNode shpnNode = (DefaultMutableTreeNode)defNode.getParent();
                        MFSBuildAheadShpnUO shpnUO = (MFSBuildAheadShpnUO) shpnNode.getUserObject();
                        
                        // loop through coverage nodes
                        for (Enumeration coverageEnum = defNode.children(); coverageEnum.hasMoreElements();) {
                            DefaultMutableTreeNode coverageNode = (DefaultMutableTreeNode) coverageEnum.nextElement();
    
                            // loop through configs
                            for (Enumeration cnfgEnum = coverageNode.children(); cnfgEnum.hasMoreElements();) {
                                DefaultMutableTreeNode cnfgNode = (DefaultMutableTreeNode) cnfgEnum.nextElement();
                                MFSBuildAheadCnfgUO cnfgUO = (MFSBuildAheadCnfgUO) cnfgNode.getUserObject();
                                
                                int releasedConfigQuantity = 0;
                                
                                // loop through mfgns
                                for (Enumeration mfgnEnum = cnfgNode.children(); mfgnEnum.hasMoreElements();) {                                
                                    DefaultMutableTreeNode mfgnNode = (DefaultMutableTreeNode) mfgnEnum.nextElement();
                                    MFSBuildAheadMfgnUO mfgnUO = (MFSBuildAheadMfgnUO) mfgnNode.getUserObject();
                                    
                                    if (mfgnUO.getRlsed().equals("Y")) {
                                        releasedConfigQuantity++;
                                        
                                        updBA.startElement("SMFGN");
                                        updBA.addElement("SHPN", shpnUO.getShpn()); 
                                        updBA.addElement("MFGN", mfgnUO.getMfgn()); 
                                        updBA.addElement("ITEM", cnfgUO.getItem()); 
                                        updBA.endElement("SMFGN");
                                    }
                                }
    
                                // Add this config with the number of MFGNs released
                                if (configsUnReleased.containsKey(cnfgUO.getItem()))
                                {
                                    int num = configsUnReleased.get(cnfgUO.getItem());
                                    num = num + releasedConfigQuantity;
                                    
                                    configsUnReleased.remove(cnfgUO.getItem());
                                    configsUnReleased.put(cnfgUO.getItem(), num);
                                }
                                else
                                {
                                    configsUnReleased.put(cnfgUO.getItem(), releasedConfigQuantity);
                                }
                            }
                        }
                    }
                    else if (obj instanceof MFSBuildAheadCoverageUO) {
                        // If a coverage node was clicked, release all the MFGNs on that node
                        DefaultMutableTreeNode shpnNode = (DefaultMutableTreeNode)defNode.getParent().getParent();
                        MFSBuildAheadShpnUO shpnUO = (MFSBuildAheadShpnUO) shpnNode.getUserObject();                        
                        // loop through configs
                        for (Enumeration cnfgEnum = defNode.children(); cnfgEnum.hasMoreElements();) {
                            DefaultMutableTreeNode cnfgNode = (DefaultMutableTreeNode) cnfgEnum.nextElement();
                            MFSBuildAheadCnfgUO cnfgUO = (MFSBuildAheadCnfgUO) cnfgNode.getUserObject();
                            
                            int releasedConfigQuantity = 0;
                            
                            // loop through mfgns
                            for (Enumeration mfgnEnum = cnfgNode.children(); mfgnEnum.hasMoreElements();) {                                
                                DefaultMutableTreeNode mfgnNode = (DefaultMutableTreeNode) mfgnEnum.nextElement();
                                MFSBuildAheadMfgnUO mfgnUO = (MFSBuildAheadMfgnUO) mfgnNode.getUserObject();
                                
                                if (mfgnUO.getRlsed().equals("Y")) {
                                    releasedConfigQuantity++;
                                    
                                    updBA.startElement("SMFGN");
                                    updBA.addElement("SHPN", shpnUO.getShpn()); 
                                    updBA.addElement("MFGN", mfgnUO.getMfgn()); 
                                    updBA.addElement("ITEM", cnfgUO.getItem()); 
                                    updBA.endElement("SMFGN");
                                }
                            }
    
                            // Add this config with the number of MFGNs released
                            if (configsUnReleased.containsKey(cnfgUO.getItem()))
                            {
                                int num = configsUnReleased.get(cnfgUO.getItem());
                                num = num + releasedConfigQuantity;
                                
                                configsUnReleased.remove(cnfgUO.getItem());
                                configsUnReleased.put(cnfgUO.getItem(), num);
                            }
                            else
                            {
                                configsUnReleased.put(cnfgUO.getItem(), releasedConfigQuantity);
                            }
                        }
                    }
                    else if (obj instanceof MFSBuildAheadCnfgUO) {
                        // If a config node was clicked, release all the MFGNs on that config
                        int releasedConfigQuantity = 0;
                        
                        DefaultMutableTreeNode shpnNode = (DefaultMutableTreeNode)defNode.getParent().getParent().getParent();
                        MFSBuildAheadShpnUO shpnUO = (MFSBuildAheadShpnUO) shpnNode.getUserObject();
                        
                        MFSBuildAheadCnfgUO cnfgUO = (MFSBuildAheadCnfgUO) defNode.getUserObject();
                        
                        // loop through mfgns
                        for (Enumeration mfgnEnum = defNode.children(); mfgnEnum.hasMoreElements();) {
                            DefaultMutableTreeNode mfgnNode = (DefaultMutableTreeNode) mfgnEnum.nextElement();
                            MFSBuildAheadMfgnUO mfgnUO = (MFSBuildAheadMfgnUO) mfgnNode.getUserObject();
                            
                            if (mfgnUO.getRlsed().equals("Y")) {
                                releasedConfigQuantity++;
                                
                                updBA.startElement("SMFGN");
                                updBA.addElement("SHPN", shpnUO.getShpn()); 
                                updBA.addElement("MFGN", mfgnUO.getMfgn()); 
                                updBA.addElement("ITEM", cnfgUO.getItem());
                                updBA.endElement("SMFGN");
                            }
                        }
    
                        // Add this config with the number of MFGNs released
                        configsUnReleased.put(cnfgUO.getItem(), releasedConfigQuantity);
                    }  else if (obj instanceof MFSBuildAheadMfgnUO) {
                        // If an MFGN node was clicked, release only that MFGN
                        DefaultMutableTreeNode shpnNode = (DefaultMutableTreeNode)defNode.getParent().getParent().getParent().getParent();
                        MFSBuildAheadShpnUO shpnUO = (MFSBuildAheadShpnUO) shpnNode.getUserObject();
                        
                        DefaultMutableTreeNode cnfgNode = (DefaultMutableTreeNode)defNode.getParent();
                        MFSBuildAheadCnfgUO cnfgUO = (MFSBuildAheadCnfgUO) cnfgNode.getUserObject();
    
                        MFSBuildAheadMfgnUO mfgnUO = (MFSBuildAheadMfgnUO) defNode.getUserObject();
                        
                        if (mfgnUO.getRlsed().equals("Y")) {
                            updBA.startElement("SMFGN");
                            updBA.addElement("SHPN", shpnUO.getShpn()); 
                            updBA.addElement("MFGN", mfgnUO.getMfgn()); 
                            updBA.addElement("ITEM", cnfgUO.getItem());
                            updBA.endElement("SMFGN");
    
                            // Add this config with the number of MFGNs released
                            configsUnReleased.put(cnfgUO.getItem(), 1);
                        }
                    }
                    
                    updBA.endElement("MFGNS");
                    updBA.endDocument();
    
                    MFSComm.getInstance().execute(updBA, this);
                    if (updBA.getReturnCode() != 0) {
                        throw new IGSTransactionException(updBA, true);
                    }
        
                    // If we got here, the release was 
                    // successful, so update the screen.
                    updateMfgnReleasedStatus(configsUnReleased, defNode, "N");
                }
            }
        }  catch (Exception e) {
            String title = "Un-Release Build Ahead Error";
            IGSMessageBox.showOkMB(getParentFrame(), title, null, e);
        } finally {
            addMyListeners();
        }
    }
    
    
    /**
     * Update each mfgn node status to released (or unreleased) so 
     * that it displays properly on the screen 
     * @param configsReleased
     */
    @SuppressWarnings("rawtypes")
	private void updateMfgnReleasedStatus(Hashtable configsReleased, DefaultMutableTreeNode selectedNode, String releaseValue) {
        DefaultMutableTreeNode root = (DefaultMutableTreeNode)this.fieldLeftTreeModel.getRoot();
        boolean selectedTreeItemIsCnfgNode = false;
        
        // First, walk through the tree and update all the released values properly
        Object obj  = selectedNode.getUserObject();
        if (obj instanceof MFSBuildAheadSchdUO) {
            // If a schd node was clicked, update all the MFGNs on that SCHD
            // loop through shpn nodes
            for (Enumeration shpnEnum = selectedNode.children(); shpnEnum.hasMoreElements();) {
                DefaultMutableTreeNode shpnNode = (DefaultMutableTreeNode) shpnEnum.nextElement();

                // loop through released nodes
                for (Enumeration releasedEnum = shpnNode.children(); releasedEnum.hasMoreElements();) {
                    DefaultMutableTreeNode releasedNode = (DefaultMutableTreeNode) releasedEnum.nextElement();

                    // loop through coverage nodes
                    for (Enumeration coverageEnum = releasedNode.children(); coverageEnum.hasMoreElements();) {
                        DefaultMutableTreeNode coverageNode = (DefaultMutableTreeNode) coverageEnum.nextElement();
                        
                        // loop through configs
                        for (Enumeration cnfgEnum = coverageNode.children(); cnfgEnum.hasMoreElements();) {
                            DefaultMutableTreeNode cnfgNode = (DefaultMutableTreeNode) cnfgEnum.nextElement();
                            
                            // loop through mfgns
                            for (Enumeration mfgnEnum = cnfgNode.children(); mfgnEnum.hasMoreElements();) {
                                DefaultMutableTreeNode mfgnNode = (DefaultMutableTreeNode) mfgnEnum.nextElement();
                                MFSBuildAheadMfgnUO mfgnUO = (MFSBuildAheadMfgnUO) mfgnNode.getUserObject();
                                
                                mfgnUO.setRlsed(releaseValue);
                            }
                        }
                    }
                }
            }
        } 
        else if (obj instanceof MFSBuildAheadShpnUO) {
            // If a shpn node was clicked, update all the MFGNs on that SHPN
            // loop through released nodes
            for (Enumeration releasedEnum = selectedNode.children(); releasedEnum.hasMoreElements();) {
                DefaultMutableTreeNode releasedNode = (DefaultMutableTreeNode) releasedEnum.nextElement();

                // loop through coverage nodes
                for (Enumeration coverageEnum = releasedNode.children(); coverageEnum.hasMoreElements();) {
                    DefaultMutableTreeNode coverageNode = (DefaultMutableTreeNode) coverageEnum.nextElement();
                    
                    // loop through configs
                    for (Enumeration cnfgEnum = coverageNode.children(); cnfgEnum.hasMoreElements();) {
                        DefaultMutableTreeNode cnfgNode = (DefaultMutableTreeNode) cnfgEnum.nextElement();
                        
                        // loop through mfgns
                        for (Enumeration mfgnEnum = cnfgNode.children(); mfgnEnum.hasMoreElements();) {
                            DefaultMutableTreeNode mfgnNode = (DefaultMutableTreeNode) mfgnEnum.nextElement();
                            MFSBuildAheadMfgnUO mfgnUO = (MFSBuildAheadMfgnUO) mfgnNode.getUserObject();
                            
                            mfgnUO.setRlsed(releaseValue);
                        }
                    }
                }
            }
        }  
        else if (obj instanceof MFSBuildAheadReleasedUO) {
            // If a shpn node was clicked, update all the MFGNs on that released node
            // loop through coverage nodes
            for (Enumeration coverageEnum = selectedNode.children(); coverageEnum.hasMoreElements();) {
                DefaultMutableTreeNode coverageNode = (DefaultMutableTreeNode) coverageEnum.nextElement();

                // loop through configs
                for (Enumeration cnfgEnum = coverageNode.children(); cnfgEnum.hasMoreElements();) {
                    DefaultMutableTreeNode cnfgNode = (DefaultMutableTreeNode) cnfgEnum.nextElement();
                    
                    // loop through mfgns
                    for (Enumeration mfgnEnum = cnfgNode.children(); mfgnEnum.hasMoreElements();) {
                        DefaultMutableTreeNode mfgnNode = (DefaultMutableTreeNode) mfgnEnum.nextElement();
                        MFSBuildAheadMfgnUO mfgnUO = (MFSBuildAheadMfgnUO) mfgnNode.getUserObject();
                        
                        mfgnUO.setRlsed(releaseValue);
                    }
                }
            }
        } 
        else if (obj instanceof MFSBuildAheadCoverageUO) {
            // If a shpn node was clicked, update all the MFGNs on that released node
            // loop through configs
            for (Enumeration cnfgEnum = selectedNode.children(); cnfgEnum.hasMoreElements();) {
                DefaultMutableTreeNode cnfgNode = (DefaultMutableTreeNode) cnfgEnum.nextElement();
                
                // loop through mfgns
                for (Enumeration mfgnEnum = cnfgNode.children(); mfgnEnum.hasMoreElements();) {
                    DefaultMutableTreeNode mfgnNode = (DefaultMutableTreeNode) mfgnEnum.nextElement();
                    MFSBuildAheadMfgnUO mfgnUO = (MFSBuildAheadMfgnUO) mfgnNode.getUserObject();
                    
                    mfgnUO.setRlsed(releaseValue);
                }
            }
        } else if (obj instanceof MFSBuildAheadCnfgUO) {
            // If a config node was clicked, release all the MFGNs on that config
            selectedTreeItemIsCnfgNode = true;
            
            // loop through mfgns
            for (Enumeration mfgnEnum = selectedNode.children(); mfgnEnum.hasMoreElements();) {
                DefaultMutableTreeNode mfgnNode = (DefaultMutableTreeNode) mfgnEnum.nextElement();
                MFSBuildAheadMfgnUO mfgnUO = (MFSBuildAheadMfgnUO) mfgnNode.getUserObject();

                mfgnUO.setRlsed(releaseValue);
            }
        } else if (obj instanceof MFSBuildAheadMfgnUO) {
            // If an MFGN node was clicked, release only that MFGN
            MFSBuildAheadMfgnUO mfgnUO = (MFSBuildAheadMfgnUO) selectedNode.getUserObject();

            mfgnUO.setRlsed(releaseValue);
            
            // update info panel
            taNodeInfo.setText(mfgnUO.toString());
        }
        
        // Now update the quantities properly
        updateAllocatedQuantity(root, configsReleased, releaseValue);

        // Now update the released/unreleased nodes
        root = updateReleasedNodes(root);
        
        // We have to combine the configs to recalculate the coverage
        // and to do that it's easiest to just remove and then add them back
        root = removeCoverageNodes(root);
        
        root = combineConfigNodes(root);
        
        root = addCoverageNodes(root);
        
        // Update the order - this will move released entities to the bottom of the list
        root = setNodeOrder(root);
        
        // Expand all the unreleased configs
        expandUnreleased(root);

        // update info panel for config node
        if (selectedTreeItemIsCnfgNode) {
            MFSBuildAheadCnfgUO cnfgUO = (MFSBuildAheadCnfgUO) selectedNode.getUserObject();
            taNodeInfo.setText(cnfgUO.toString());
        }
    }
    
    /**
     * Set Released flag appropriately
     * @param root
     * @return
     */
    @SuppressWarnings("rawtypes")
	private DefaultMutableTreeNode updateReleasedNodes(DefaultMutableTreeNode root)
    {
        // We will be building an entire new tree so create the new root
        MFSBuildAheadTopUO topUO = new MFSBuildAheadTopUO();
        DefaultMutableTreeNode newRoot = new DefaultMutableTreeNode(topUO);

        for (Enumeration schdEnum = root.children(); schdEnum.hasMoreElements();) 
        {
            DefaultMutableTreeNode schdNode = (DefaultMutableTreeNode) schdEnum.nextElement();
            
            // Create the new schd node as a copy of the old one
            DefaultMutableTreeNode newSchdNode = copySchdNode(schdNode);
            
            boolean shpnsOnSchd = false;
            
            for (Enumeration shpnEnum = schdNode.children(); shpnEnum.hasMoreElements();) 
            {
                DefaultMutableTreeNode shpnNode = (DefaultMutableTreeNode) shpnEnum.nextElement();
  
                // Create the new shpn node. New released/unreleased nodes will be added to this
                DefaultMutableTreeNode newShpnNode = copyShpnNode(shpnNode);
    
                // Create the new released/unreleased nodes here because stuff might be switching between those two
                MFSBuildAheadReleasedUO newReleasedUO = new MFSBuildAheadReleasedUO(true);
                DefaultMutableTreeNode newReleasedNode = new DefaultMutableTreeNode(newReleasedUO);
    
                MFSBuildAheadReleasedUO newUnreleasedUO = new MFSBuildAheadReleasedUO(false);
                DefaultMutableTreeNode newUnreleasedNode = new DefaultMutableTreeNode(newUnreleasedUO);

                // Create the new coverage/no coverage nodes here because stuff might be switching between those three
                MFSBuildAheadCoverageUO newReleasedCoverageUO = new MFSBuildAheadCoverageUO(true, true);
                DefaultMutableTreeNode newReleasedCoverageNode = new DefaultMutableTreeNode(newReleasedCoverageUO);

                MFSBuildAheadCoverageUO newUnreleasedCoverageUO = new MFSBuildAheadCoverageUO(true, false);
                DefaultMutableTreeNode newUnreleasedCoverageNode = new DefaultMutableTreeNode(newUnreleasedCoverageUO);

                MFSBuildAheadCoverageUO newUnreleasedNoCoverageUO = new MFSBuildAheadCoverageUO(false, false);
                DefaultMutableTreeNode newUnreleasedNoCoverageNode = new DefaultMutableTreeNode(newUnreleasedNoCoverageUO);

                // These 3 hashtables store the cnfg nodes... 
                // one hashtable for released (coverage doesn't matter)
                // one for unreleased with coverage
                // one for unreleased with no coverage
                Hashtable<String,DefaultMutableTreeNode> releasedCnfgs = new Hashtable<String,DefaultMutableTreeNode>();
                Hashtable<String,DefaultMutableTreeNode> unreleasedCoverageCnfgs = new Hashtable<String,DefaultMutableTreeNode>();
                Hashtable<String,DefaultMutableTreeNode> unreleasedNoCoverageCnfgs = new Hashtable<String,DefaultMutableTreeNode>();
                
                // Loop through the released nodes on the old tree
                for (Enumeration releasedEnum = shpnNode.children(); releasedEnum.hasMoreElements();) 
                {
                    DefaultMutableTreeNode releasedNode = (DefaultMutableTreeNode) releasedEnum.nextElement();

                    // Loop through the config nodes on the old tree
                    for (Enumeration coverageEnum = releasedNode.children(); coverageEnum.hasMoreElements();) 
                    {
                        DefaultMutableTreeNode coverageNode = (DefaultMutableTreeNode) coverageEnum.nextElement();
                        
                        // Loop through the config nodes on the old tree
                        for (Enumeration cnfgEnum = coverageNode.children(); cnfgEnum.hasMoreElements();) 
                        {
                            DefaultMutableTreeNode cnfgNode = (DefaultMutableTreeNode) cnfgEnum.nextElement();
                            MFSBuildAheadCnfgUO cnfgUO = (MFSBuildAheadCnfgUO) cnfgNode.getUserObject();
                            
                            // Loop through the mfgn nodes on the old tree
                            for (Enumeration mfgnEnum = cnfgNode.children(); mfgnEnum.hasMoreElements();) 
                            {
                                DefaultMutableTreeNode mfgnNode = (DefaultMutableTreeNode) mfgnEnum.nextElement();
                                MFSBuildAheadMfgnUO mfgnUO = (MFSBuildAheadMfgnUO) mfgnNode.getUserObject();
                                
                                // Copy the mfgn node so we can add it to the new tree 
                                MFSBuildAheadMfgnUO newMfgnUO = new MFSBuildAheadMfgnUO(mfgnUO.getMfgn());
                                newMfgnUO.setRlsed(mfgnUO.getRlsed());
                                DefaultMutableTreeNode newMfgnNode = new DefaultMutableTreeNode(newMfgnUO);
                                
                                if (mfgnUO.getRlsed().equals("Y"))
                                {
                                    // Look for this config on the released node 
                                    DefaultMutableTreeNode newReleasedCnfgNode = null;
                                    if (releasedCnfgs.containsKey(cnfgUO.getItem()))
                                    {
                                        newReleasedCnfgNode = (DefaultMutableTreeNode)releasedCnfgs.get(cnfgUO.getItem());
                                    }
                                    else
                                    {
                                        // Create this cnfg node 
                                        newReleasedCnfgNode = copyCnfgNode(cnfgNode, true);
                                    }
                                    
                                    // Update order quantity
                                    MFSBuildAheadCnfgUO newReleasedCnfgUO = (MFSBuildAheadCnfgUO)newReleasedCnfgNode.getUserObject();
                                    newReleasedCnfgUO.setOrderQty(newReleasedCnfgUO.getOrderQty()+1);
    
                                    // Add the new mfgn node to the new released cnfg node and save it back to the released hashtable
                                    newReleasedCnfgNode.add(newMfgnNode);
                                    releasedCnfgs.put(cnfgUO.getItem(), newReleasedCnfgNode);
                                }
                                else
                                {
                                    if (cnfgUO.haveCoverage())
                                    {
                                        // Look for this config on the unreleased coverage node
                                        DefaultMutableTreeNode newUnreleasedCnfgNode = null;
                                        if (unreleasedCoverageCnfgs.containsKey(cnfgUO.getItem()))
                                        {
                                            newUnreleasedCnfgNode = (DefaultMutableTreeNode)unreleasedCoverageCnfgs.get(cnfgUO.getItem());
                                        }
                                        else
                                        {
                                            // Create this cnfg node 
                                            newUnreleasedCnfgNode = copyCnfgNode(cnfgNode, false);
                                        }
                                        
                                        // Update order quantity
                                        MFSBuildAheadCnfgUO newUnreleasedCnfgUO = (MFSBuildAheadCnfgUO)newUnreleasedCnfgNode.getUserObject();
                                        newUnreleasedCnfgUO.setOrderQty(newUnreleasedCnfgUO.getOrderQty()+1);
                                        
                                        // Add the new mfgn node to the new unreleased coverage cnfg node and save it back to the unreleased coverage hashtable 
                                        newUnreleasedCnfgNode.add(newMfgnNode);
                                        unreleasedCoverageCnfgs.put(cnfgUO.getItem(), newUnreleasedCnfgNode);
                                    }
                                    else
                                    {
                                        // Look for this config on the unreleased no coverage node
                                        DefaultMutableTreeNode newUnreleasedCnfgNode = null;
                                        if (unreleasedNoCoverageCnfgs.containsKey(cnfgUO.getItem()))
                                        {
                                            newUnreleasedCnfgNode = (DefaultMutableTreeNode)unreleasedNoCoverageCnfgs.get(cnfgUO.getItem());
                                        }
                                        else
                                        {
                                            // Create this cnfg node 
                                            newUnreleasedCnfgNode = copyCnfgNode(cnfgNode, false);
                                        }
                                        
                                        // Update order quantity
                                        MFSBuildAheadCnfgUO newUnreleasedCnfgUO = (MFSBuildAheadCnfgUO)newUnreleasedCnfgNode.getUserObject();
                                        newUnreleasedCnfgUO.setOrderQty(newUnreleasedCnfgUO.getOrderQty()+1);
                                        
                                        // Add the new mfgn node to the new unreleased no coverage cnfg node and save it back to the unreleased no coverage hashtable 
                                        newUnreleasedCnfgNode.add(newMfgnNode);
                                        unreleasedNoCoverageCnfgs.put(cnfgUO.getItem(), newUnreleasedCnfgNode);
                                    }
                                }
                            }
                        }
                    }
                }

                // So we know what nodes to add to the shpn
                boolean releasedCnfgsOnShpn = false;
                boolean unreleasedCoverageCnfgsOnShpn = false;
                boolean unreleasedNoCoverageCnfgsOnShpn = false;

                // Add all the released configs to the coverage node
                for (Enumeration releasedEnum = releasedCnfgs.elements(); releasedEnum.hasMoreElements();)
                {
                    releasedCnfgsOnShpn = true;
                    DefaultMutableTreeNode releasedNode = (DefaultMutableTreeNode)releasedEnum.nextElement();
                    if (releasedNode!=null)
                    {
                        newReleasedCoverageNode.add(releasedNode);
                    }
                }

                // Add all the unreleased coverage configs to the coverage node
                for (Enumeration unreleasedEnum = unreleasedCoverageCnfgs.elements(); unreleasedEnum.hasMoreElements();)
                {
                    unreleasedCoverageCnfgsOnShpn = true;
                    DefaultMutableTreeNode unreleasedNode = (DefaultMutableTreeNode)unreleasedEnum.nextElement();
                    if (unreleasedNode!=null)
                    {
                        newUnreleasedCoverageNode.add(unreleasedNode);
                    }
                }
                // Add all the unreleased no coverage configs to the no coverage node
                for (Enumeration unreleasedEnum = unreleasedNoCoverageCnfgs.elements(); unreleasedEnum.hasMoreElements();)
                {
                    unreleasedNoCoverageCnfgsOnShpn = true;
                    DefaultMutableTreeNode unreleasedNode = (DefaultMutableTreeNode)unreleasedEnum.nextElement();
                    if (unreleasedNode!=null)
                    {
                        newUnreleasedNoCoverageNode.add(unreleasedNode);
                    }
                }

                // Add the coverage node to the released node
                if (releasedCnfgsOnShpn)
                {
                    newReleasedNode.add(newReleasedCoverageNode);
                }
                
                // Add the coverage node to the unreleased node
                if (unreleasedCoverageCnfgsOnShpn)
                {
                    newUnreleasedNode.add(newUnreleasedCoverageNode);
                }
                
                // Add the no coverage node to the unreleased node
                if (unreleasedNoCoverageCnfgsOnShpn)
                {
                    newUnreleasedNode.add(newUnreleasedNoCoverageNode);
                }

                // add the released nodes to the ship entity node
                if (releasedCnfgsOnShpn)
                {
                    newShpnNode.add(newReleasedNode);
                }
                if (unreleasedCoverageCnfgsOnShpn || unreleasedNoCoverageCnfgsOnShpn)
                {
                    newShpnNode.add(newUnreleasedNode);
                }
                
                // Add the shpn node to the schd node if necessary
                if (releasedCnfgsOnShpn || unreleasedCoverageCnfgsOnShpn || unreleasedNoCoverageCnfgsOnShpn)
                {
                    shpnsOnSchd = true;
                    newSchdNode.add(newShpnNode);
                }
            }
            
            // Finally add the new schd to the new root
            if (shpnsOnSchd)
            {
                newRoot.add(newSchdNode);
            }
        }

        // Update the tree
        this.fieldLeftTreeModel.setRoot(newRoot);
        
        // return the new root
        return newRoot;
    }
    
    /**
     * Remove all the coverage nodes
     * @param root
     * @return
     */
    @SuppressWarnings("rawtypes")
	private DefaultMutableTreeNode removeCoverageNodes(DefaultMutableTreeNode root)
    {
        // We will be building an entire new tree so create the new root
        MFSBuildAheadTopUO topUO = new MFSBuildAheadTopUO();
        DefaultMutableTreeNode newRoot = new DefaultMutableTreeNode(topUO);

        for (Enumeration schdEnum = root.children(); schdEnum.hasMoreElements();) 
        {
            DefaultMutableTreeNode schdNode = (DefaultMutableTreeNode) schdEnum.nextElement();
            
            // Create the new schd node as a copy of the old one
            DefaultMutableTreeNode newSchdNode = copySchdNode(schdNode);
            
            boolean shpnsOnSchd = false;
            
            for (Enumeration shpnEnum = schdNode.children(); shpnEnum.hasMoreElements();) 
            {
                DefaultMutableTreeNode shpnNode = (DefaultMutableTreeNode) shpnEnum.nextElement();
  
                // Copy the shpn node
                DefaultMutableTreeNode newShpnNode = copyShpnNode(shpnNode);
                
                // Loop through the released nodes on the old tree
                for (Enumeration releasedEnum = shpnNode.children(); releasedEnum.hasMoreElements();) 
                {
                    DefaultMutableTreeNode releasedNode = (DefaultMutableTreeNode) releasedEnum.nextElement();
                    
                    // Copy the release/unreleased node
                    DefaultMutableTreeNode newReleasedNode = copyReleasedNode(releasedNode);

                    // Loop through the config nodes on the old tree
                    for (Enumeration coverageEnum = releasedNode.children(); coverageEnum.hasMoreElements();) 
                    {
                        DefaultMutableTreeNode coverageNode = (DefaultMutableTreeNode) coverageEnum.nextElement();
                        
                        // Loop through the config nodes on the old tree
                        for (Enumeration cnfgEnum = coverageNode.children(); cnfgEnum.hasMoreElements();) 
                        {
                            DefaultMutableTreeNode cnfgNode = (DefaultMutableTreeNode) cnfgEnum.nextElement();
                            MFSBuildAheadCnfgUO cnfgUO = (MFSBuildAheadCnfgUO)cnfgNode.getUserObject();
                            
                            // Copy the config node
                            DefaultMutableTreeNode newCnfgNode = copyCnfgNode(cnfgNode, cnfgUO.isReleased());
                            
                            // Loop through the mfgn nodes on the old tree
                            for (Enumeration mfgnEnum = cnfgNode.children(); mfgnEnum.hasMoreElements();) 
                            {
                                DefaultMutableTreeNode mfgnNode = (DefaultMutableTreeNode) mfgnEnum.nextElement();
                                
                                // Copy the mfgn node so we can add it to the new tree 
                                DefaultMutableTreeNode newMfgnNode = copyMfgnNode(mfgnNode);
                                
                                newCnfgNode.add(newMfgnNode);
                            }
                            
                            newReleasedNode.add(newCnfgNode);
                        }
                    }
                    
                    newShpnNode.add(newReleasedNode);
                }
                
                newSchdNode.add(newShpnNode);
                shpnsOnSchd = true;
            }
            
            // Finally add the new schd to the new root
            if (shpnsOnSchd)
            {
                newRoot.add(newSchdNode);
            }
        }

        // Update the tree
        this.fieldLeftTreeModel.setRoot(newRoot);
        
        // return the new root
        return newRoot;
    }
    
    /**
     * Combine config nodes on the same release/not released node 
     * @param root
     * @return
     */
    @SuppressWarnings("rawtypes")
	private DefaultMutableTreeNode combineConfigNodes(DefaultMutableTreeNode root)
    {
        // We will be building an entire new tree so create the new root
        MFSBuildAheadTopUO topUO = new MFSBuildAheadTopUO();
        DefaultMutableTreeNode newRoot = new DefaultMutableTreeNode(topUO);

        for (Enumeration schdEnum = root.children(); schdEnum.hasMoreElements();) 
        {
            DefaultMutableTreeNode schdNode = (DefaultMutableTreeNode) schdEnum.nextElement();
            
            // Create the new schd node as a copy of the old one
            DefaultMutableTreeNode newSchdNode = copySchdNode(schdNode);
            
            boolean shpnsOnSchd = false;
            
            for (Enumeration shpnEnum = schdNode.children(); shpnEnum.hasMoreElements();) 
            {
                DefaultMutableTreeNode shpnNode = (DefaultMutableTreeNode) shpnEnum.nextElement();
  
                // Copy the shpn node
                DefaultMutableTreeNode newShpnNode = copyShpnNode(shpnNode);
                
                // Loop through the released nodes on the old tree
                for (Enumeration releasedEnum = shpnNode.children(); releasedEnum.hasMoreElements();) 
                {
                    DefaultMutableTreeNode releasedNode = (DefaultMutableTreeNode) releasedEnum.nextElement();
                    
                    // Copy the release/unreleased node
                    DefaultMutableTreeNode newReleasedNode = copyReleasedNode(releasedNode);
                    
                    Hashtable<String, DefaultMutableTreeNode> configs = new Hashtable<String, DefaultMutableTreeNode>();

                    // Loop through the config nodes on the old tree
                    for (Enumeration cnfgEnum = releasedNode.children(); cnfgEnum.hasMoreElements();) 
                    {
                        DefaultMutableTreeNode cnfgNode = (DefaultMutableTreeNode) cnfgEnum.nextElement();
                        MFSBuildAheadCnfgUO cnfgUO = (MFSBuildAheadCnfgUO)cnfgNode.getUserObject();
                        
                        // Copy the config node
                        DefaultMutableTreeNode newCnfgNode = null;

                        if (configs.containsKey(cnfgUO.getItem()))
                        {
                            newCnfgNode = (DefaultMutableTreeNode)configs.remove(cnfgUO.getItem());
                        }
                        else
                        {
                            newCnfgNode = copyCnfgNode(cnfgNode, cnfgUO.isReleased());
                        }
                        
                        // Loop through the mfgn nodes on the old tree
                        for (Enumeration mfgnEnum = cnfgNode.children(); mfgnEnum.hasMoreElements();) 
                        {
                            DefaultMutableTreeNode mfgnNode = (DefaultMutableTreeNode) mfgnEnum.nextElement();
                            
                            // Copy the mfgn node so we can add it to the new tree 
                            DefaultMutableTreeNode newMfgnNode = copyMfgnNode(mfgnNode);
                            
                            newCnfgNode.add(newMfgnNode);
                        }
                        
                        configs.put(cnfgUO.getItem(), newCnfgNode);
                    }

                    for (Enumeration configsEnum = configs.elements(); configsEnum.hasMoreElements();) 
                    {
                        DefaultMutableTreeNode configNode = (DefaultMutableTreeNode)configsEnum.nextElement();
                        
                        newReleasedNode.add(configNode);
                    }
                    
                    newShpnNode.add(newReleasedNode);
                }
                
                newSchdNode.add(newShpnNode);
                shpnsOnSchd = true;
            }
            
            // Finally add the new schd to the new root
            if (shpnsOnSchd)
            {
                newRoot.add(newSchdNode);
            }
        }

        // Update the tree
        this.fieldLeftTreeModel.setRoot(newRoot);
        
        // return the new root
        return newRoot;
    }
    
    /**
     * Update the allocated quantities on each config node
     * @param topNode
     * @param configsReleased
     * @param releaseValue "Y" if releasing; "N" if un-releasing
     */
    @SuppressWarnings("rawtypes")
	private void updateAllocatedQuantity(DefaultMutableTreeNode root, Hashtable configsReleased, String releaseValue) {
        // loop through ship entities
        for (Enumeration schdEnum = root.children(); schdEnum.hasMoreElements();) {
            DefaultMutableTreeNode schdNode = (DefaultMutableTreeNode) schdEnum.nextElement();

            // loop through schd nodes
            for (Enumeration shpnEnum = schdNode.children(); shpnEnum.hasMoreElements();) 
            {
                DefaultMutableTreeNode shpnNode = (DefaultMutableTreeNode) shpnEnum.nextElement();
                MFSBuildAheadShpnUO shpnUO = (MFSBuildAheadShpnUO) shpnNode.getUserObject();
                int numberOfUnreleasedMfgnsOnShpn = 0;
                
                // loop through released nodes
                for (Enumeration releasedEnum = shpnNode.children(); releasedEnum.hasMoreElements();) 
                {
                    DefaultMutableTreeNode releasedNode = (DefaultMutableTreeNode) releasedEnum.nextElement();
                    
                    // loop through configs
                    for (Enumeration cnfgEnum = releasedNode.children(); cnfgEnum.hasMoreElements();) 
                    {
                        // Update the allocated quantity
                        DefaultMutableTreeNode cnfgNode = (DefaultMutableTreeNode) cnfgEnum.nextElement();
                        
                        Vector<DefaultMutableTreeNode> cnfgVector = new Vector<DefaultMutableTreeNode>();
                        try
                        {
                        	/*
                        	 * MFSBuildAheadCnfgUO cnfgUO = (MFSBuildAheadCnfgUO) 
                        	 */
                            cnfgNode.getUserObject();
                            cnfgVector.add(cnfgNode);
                        }
                        catch (Exception e)
                        {
                            // loop through configs... really this time
                            for (Enumeration realCnfgEnum = cnfgNode.children(); realCnfgEnum.hasMoreElements();) 
                            {
                                DefaultMutableTreeNode realCnfgNode = (DefaultMutableTreeNode) realCnfgEnum.nextElement();
                                cnfgVector.add(realCnfgNode);
                            }
                        }

                        for (int x=0; x<cnfgVector.size(); x++)
                        {
                            DefaultMutableTreeNode cnfgNodeTemp = (DefaultMutableTreeNode)cnfgVector.elementAt(x);
                            /*
                             * ~02 To prevent a wrong casting of object when it is not MFSBuildAheadCnfgUO type
                             *  avoiding the incompatibility exception its needed to manage a validation using instanceof
                             */
                            if( cnfgNodeTemp.getUserObject() instanceof MFSBuildAheadCnfgUO)
                            {
	                            MFSBuildAheadCnfgUO cnfgUO = (MFSBuildAheadCnfgUO)cnfgNodeTemp.getUserObject();
	                            
	                            Object quantityObj = configsReleased.get(cnfgUO.getItem());
	                            if (quantityObj!=null) {
	                                int quantityInt = (Integer)quantityObj;
	                                if (releaseValue.equals("Y"))
	                                {
	                                    cnfgUO.setAllocatedQuantity(cnfgUO.getAllocatedQuantity()+quantityInt);
	                                }
	                                else if (releaseValue.equals("N"))
	                                {
	                                    cnfgUO.setAllocatedQuantity(cnfgUO.getAllocatedQuantity()-quantityInt);   
	                                }
	                            }
	            
	                            // count up unreleased mfgns
	                            int numberOfUnreleasedMfgnsOnCnfg = 0;
	                            for (Enumeration mfgnEnum = cnfgNodeTemp.children(); mfgnEnum.hasMoreElements();) 
	                            {
	                                // Update the allocated quantity
	                                DefaultMutableTreeNode mfgnNode = (DefaultMutableTreeNode) mfgnEnum.nextElement();
	                                MFSBuildAheadMfgnUO mfgnUO = (MFSBuildAheadMfgnUO) mfgnNode.getUserObject();
	                                
	                                if (mfgnUO.getRlsed().equals("N"))
	                                {
	                                    numberOfUnreleasedMfgnsOnCnfg++;
	                                    numberOfUnreleasedMfgnsOnShpn++;
	                                }
	                            }
	                            cnfgUO.setUnreleasedOrderQty(numberOfUnreleasedMfgnsOnCnfg);
                            } //enf ~02
                        }
                    }
                    shpnUO.setUnreleasedOrderQty(numberOfUnreleasedMfgnsOnShpn);
                }
            }
        }
    }
    
    /**
     * Call the Entity Merge Screen
     * <p>
     * Invoked when {@link #pbEntityMerge} is selected.
     */
    private void entityMerge() {
        try {
            removeMyListeners();
            
            // Get the currently selected ship entity
            String shpn = null;
            TreePath defPath = this.trLeftTree.getSelectionPath();
            if ((defPath != null) && (defPath.getPathCount() > 1)) {
                DefaultMutableTreeNode defNode = (DefaultMutableTreeNode) defPath.getLastPathComponent();
                Object obj  = defNode.getUserObject();
                if (obj instanceof MFSBuildAheadSchdUO) {
                    throw new Exception("Please select a ship entity to merge.");
                }
                if (obj instanceof MFSBuildAheadShpnUO) {
                }
                if (obj instanceof MFSBuildAheadReleasedUO) {
                    defNode = (DefaultMutableTreeNode)defNode.getParent();
                }
                if (obj instanceof MFSBuildAheadCoverageUO) {
                    defNode = (DefaultMutableTreeNode)defNode.getParent().getParent();
                }
                if (obj instanceof MFSBuildAheadCnfgUO) {
                    defNode = (DefaultMutableTreeNode)defNode.getParent().getParent().getParent();
                }
                if (obj instanceof MFSBuildAheadMfgnUO) {
                    defNode = (DefaultMutableTreeNode)defNode.getParent().getParent().getParent().getParent();
                }
                
                MFSBuildAheadShpnUO defObj = (MFSBuildAheadShpnUO) defNode.getUserObject();
                shpn = defObj.getShpn();
            }
            
            if (shpn==null || shpn.trim().equals("")) {
                throw new Exception("Please select a ship entity to merge.");
            } else {
                // Start up the entity merge screen
                MFSEntityMergePanel emp = new MFSEntityMergePanel(getParentFrame(),
                        this, MFSEntityMergePanel.MERGING);
                if (emp.retrieveContainerInfo(this, "", shpn)) {
                    // I'm not sure if we need this or not
                    /* ~36A - Check entry CHECKHANDLING to indicate, based on operation number, 
                     * if we need to check for special handling.                                 */
                    //checkHandling("C", "00006KBGXY", "0900");
                    
                    getParentFrame().displayMFSPanel(emp);
                }
            }
        } catch (Exception e) {
            String title = "Entity Merge Error";
            IGSMessageBox.showOkMB(getParentFrame(), title, null, e);
        } finally {
            addMyListeners();
        }
    }    
    
	/**
	 * Prompts the user for a container number.
	 * @param buttonText the text for the proceed button
	 * @param buttonMnem the mnemonic for that button
	 * @return the container number as a <code>String</code>
	 * @throws IGSException if an error occurs
	 */
	private String getShipEntity(String buttonText, char buttonMnem)
		throws IGSException {
		String retval = null;

		MFSGetValueDialog gvd = new MFSGetValueDialog(this.getParentFrame(),
				"Enter the ship entity", "SHPN", buttonText, buttonMnem);
		MFStfParser parser = new MFStfParser.MFStfCntrParser(this);
		gvd.setTextParser(parser);
		gvd.setLocationRelativeTo(getParentFrame());
		gvd.setVisible(true);
		if (gvd.getProceedSelected()) {
			retval = gvd.getInputValue();
			if (retval.length() == 0) {
				retval = null;
			}
		} 
		if (retval!=null && retval.length()>3) {
            retval = retval.substring(3);
        }
		return retval;
	}

	
    /**
     * Searches through the tree and returns the nodes that match.
     * @param root root of (sub)tree to search
     * @param value value to find
     * @return array of nodes that match, empty array if nothing found
     */
    @SuppressWarnings("rawtypes")
	private DefaultMutableTreeNode[] findNodeInTree(DefaultMutableTreeNode root, String value) {
        ArrayList<DefaultMutableTreeNode> retval = new ArrayList<DefaultMutableTreeNode>();
        DefaultMutableTreeNode[] retvalArray = new DefaultMutableTreeNode[0];
        DefaultMutableTreeNode thisNode = null;
        Object obj = null;

        if (value != null) {
            Enumeration nodes = root.depthFirstEnumeration();
            while (nodes.hasMoreElements()) {
                thisNode = (DefaultMutableTreeNode) nodes.nextElement();
                obj = thisNode.getUserObject();
                if (obj instanceof MFSBuildAheadShpnUO) {
                    if (((MFSBuildAheadShpnUO) obj).getShpn().equals(value)) {
                        retval.add(thisNode);
                    }
                }
                if (obj instanceof MFSBuildAheadCnfgUO) {
                    if (((MFSBuildAheadCnfgUO) obj).getItem().equals(value)) {
                        retval.add(thisNode);
                    }
                }
                if (obj instanceof MFSBuildAheadMfgnUO) {
                    if (((MFSBuildAheadMfgnUO) obj).getMfgn().equals(value)) {
                        retval.add(thisNode);
                    }
                }
            }
        }

        if (!retval.isEmpty()) {
            retvalArray = new DefaultMutableTreeNode[retval.size()];
            retval.toArray(retvalArray);
        }
        return retvalArray;
    }

    
	/**
	 * Submit the END_BA server transaction.
	 * @param msg the action message for transaction
	 * @throws IGSException if an error occurs
	 */
	private void submitEndBuildAhead(String msg) throws IGSException {
		IGSXMLTransaction endBA = new IGSXMLTransaction("END_BA"); 
        endBA.setActionMessage(msg);
        endBA.startDocument();
        endBA.addElement("CELL", MFSConfig.getInstance().get8CharCell()); 
        endBA.addElement("CTYP", MFSConfig.getInstance().get8CharCellType()); 
        endBA.addElement("USER", MFSConfig.getInstance().get8CharUser()); 
        endBA.endDocument();
        
		MFSComm.getInstance().execute(endBA, this);
		if (endBA.getReturnCode() != 0) {
			throw new IGSTransactionException(endBA, true);
		}
	}

	
	/**
	 * Returns the <code>DefaultMutableTreeNode</code> for the specified
	 * container and displays an error message if a tree node could not be found
	 * for the container. Does not display an error message and returns
	 * <code>null</code> if <code>cntrNum</code> is <code>null</code> or
	 * blank.
	 * @param cntrNum the container
	 * @return the node for the container or <code>null</code> if the
	 *         container is not in the tree
	 */
	private DefaultMutableTreeNode getNode(String shpnNum) {
		DefaultMutableTreeNode result = null;
		if (shpnNum != null && shpnNum.length() > 0) {
			result = (DefaultMutableTreeNode) this.fieldNodeMap.get(shpnNum);
			if (result == null) {
				String title = "Ship Entity Not Found";
				String erms = "Ship Entity {0} is not in the current tree.";
				String arguments[] = {
                        shpnNum
				};
				erms = MessageFormat.format(erms, (Object[])arguments);
				IGSMessageBox.showOkMB(getParentFrame(), title, erms, null);
			}
		}
		return result;
	}
	
	
    /**
     * Returns if we have a lock or not.
     * @return <code>true</code> if we have a lock.
     */
	private boolean isLocked() {
        if (locked.equals("Y")) {
            return true;
        } else {
            return false;
        }
	}

	
	/**
	 * Collapse down the tree except the first level.
	 * @param tree <code>JTree</code> to collapse
	 */
	private void collapseTree(JTree tree) {
		for (int i = tree.getRowCount() - 1; i >= 1; i--) {
			tree.collapseRow(i);
		}
	}

	
	/**
	 * Expand out the tree.
	 * @param tree <code>JTree</code> to expand
	 */
	private void expandTree(JTree tree) {
		for (int i = 0; i < tree.getRowCount(); i++) {
			tree.expandRow(i);
		}
	}

	
	/**
	 * Invoked when an action occurs.
	 * @param ae the <code>ActionEvent</code>
	 */
	public void actionPerformed(ActionEvent ae) {
		final Object source = ae.getSource();
		if (source == this.pbFind) {
			findNode();
		} else if (source == this.pbEnd) {
            endBuildAhead();
		} else if (source == this.pbExpand) {
			expandTrees();
		} else if (source == this.pbCollapse) {
			collapseTrees();
		} else if (source == this.pbRelease) {
            release();
        } else if (source == this.pbEntityMerge) {
            entityMerge();
        } else if (source == this.pbUnRelease) {
            unRelease();
        }
	}

	
	/**
	 * Invoked when a key is pressed.
	 * @param ke the <code>KeyEvent</code>
	 */
	public void keyPressed(KeyEvent ke) {
		final int keyCode = ke.getKeyCode();
		if (keyCode == KeyEvent.VK_ENTER) {
			final Object source = ke.getSource();
			if (source instanceof MFSMenuButton) {
				MFSMenuButton button = (MFSMenuButton) source;
				button.requestFocusInWindow();
				button.doClick();
			}
		} else if (keyCode == KeyEvent.VK_F2) {
			this.pbFind.requestFocusInWindow();
			this.pbFind.doClick();
		} else if (keyCode == KeyEvent.VK_F3) {
            this.pbEnd.requestFocusInWindow();
            this.pbEnd.doClick();
        } else if (keyCode == KeyEvent.VK_F4) {
			this.pbExpand.requestFocusInWindow();
			this.pbExpand.doClick();
		} else if (keyCode == KeyEvent.VK_F5) {
            this.pbCollapse.requestFocusInWindow();
            this.pbCollapse.doClick();
        } else if (keyCode == KeyEvent.VK_F6) {
            if (this.fieldMode==BARELEASE) {
                this.pbRelease.requestFocusInWindow();
                this.pbRelease.doClick();
            } else if (this.fieldMode==BAAPPLY) {
                this.pbEntityMerge.requestFocusInWindow();
                this.pbEntityMerge.doClick();
            }
        } else if (keyCode == KeyEvent.VK_F7) {
            if (this.fieldMode==BARELEASE) {
                this.pbUnRelease.requestFocusInWindow();
                this.pbUnRelease.doClick();
            }
        }
	}

	
	/**
	 * Invoked when the value of the tree selection changes.
	 * @param tse the <code>TreeSelectionEvent</code>
	 */
	public void valueChanged(TreeSelectionEvent tse) {
		try {
			TreePath path = tse.getNewLeadSelectionPath();
			if (path != null) {
				this.taNodeInfo.setText(path.getLastPathComponent().toString());
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
    
    /**
     * Mergesort a vector of ship entity nodes by scheduled ship date
     * @param unsortedList unsorted <code>Vector</code> of <code>DefaultMutableTreeNode</code>
     * @return sorted <code>Vector</code> of <code>DefaultMutableTreeNode</code>
     */
    private Vector<DefaultMutableTreeNode> mergeSort(Vector<DefaultMutableTreeNode> unsortedList)
    {
        if (unsortedList.size() <= 1)
        {
            return unsortedList;
        }
        Vector<DefaultMutableTreeNode> left = new Vector<DefaultMutableTreeNode>();
        Vector<DefaultMutableTreeNode> right = new Vector<DefaultMutableTreeNode>();
        Vector<DefaultMutableTreeNode> result = new Vector<DefaultMutableTreeNode>();

        int middle = unsortedList.size()/2;
        for (int x=0; x<middle; x++)
        {
            left.add((DefaultMutableTreeNode)unsortedList.elementAt(x));
        }
        for (int x=middle; x<unsortedList.size(); x++)
        {
            right.add((DefaultMutableTreeNode)unsortedList.elementAt(x));
        }
        
        left = mergeSort(left);
        right = mergeSort(right);
        
        // Grab the max left SCHD and the min right SCHD 
        DefaultMutableTreeNode leftSchdNode = (DefaultMutableTreeNode) left.elementAt(left.size()-1);
        MFSBuildAheadSchdUO leftSchdUO = (MFSBuildAheadSchdUO) leftSchdNode.getUserObject();
        DefaultMutableTreeNode rightSchdNode = (DefaultMutableTreeNode) right.elementAt(0);
        MFSBuildAheadSchdUO rightSchdUO = (MFSBuildAheadSchdUO) rightSchdNode.getUserObject();
        // if left SCHD > right SCHD
        if (leftSchdUO.getSchd().compareTo(rightSchdUO.getSchd())>0)
        {
            result = merge(left, right);
        }
        else
        {
            for (int x=0; x<left.size(); x++)
            {
                result.add(left.elementAt(x));
            }
            for (int x=0; x<right.size(); x++)
            {
                result.add(right.elementAt(x));
            }
        }
        
        return result;
    }
    
    /**
     * Merge function for mergesort
     * @param left
     * @param right
     * @return
     */
    private Vector<DefaultMutableTreeNode> merge(Vector<DefaultMutableTreeNode> left, Vector<DefaultMutableTreeNode> right)
    {
        Vector<DefaultMutableTreeNode> result = new Vector<DefaultMutableTreeNode>();
        
        while (left.size() > 0 && right.size() > 0)
        {
            // Grab the first left SCHD and the first right SCHD 
            DefaultMutableTreeNode leftSchdNode = (DefaultMutableTreeNode) left.elementAt(0);
            MFSBuildAheadSchdUO leftSchdUO = (MFSBuildAheadSchdUO) leftSchdNode.getUserObject();
            DefaultMutableTreeNode rightSchdNode = (DefaultMutableTreeNode) right.elementAt(0);
            MFSBuildAheadSchdUO rightSchdUO = (MFSBuildAheadSchdUO) rightSchdNode.getUserObject();
            // if left SCHD <= right SCHD
            if (leftSchdUO.getSchd().compareTo(rightSchdUO.getSchd())<=0)
            {
                result.add((DefaultMutableTreeNode)left.elementAt(0));
                left.remove(0);
            }
            else
            {
                result.add((DefaultMutableTreeNode)right.elementAt(0));
                right.remove(0);
            }
        }
        
        if (left.size() > 0)
        {
            for (int x=0; x<left.size(); x++)
            {
                result.add((DefaultMutableTreeNode)left.elementAt(x));
            }
        }
        else
        {
            for (int x=0; x<right.size(); x++)
            {
                result.add((DefaultMutableTreeNode)right.elementAt(x));
            }
        }
            
        return result;
    }
}