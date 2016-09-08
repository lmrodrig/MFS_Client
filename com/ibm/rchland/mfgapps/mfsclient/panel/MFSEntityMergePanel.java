/* © Copyright IBM Corporation 2007, 2013. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 12-01-07        37616JL  R Prechel        -Initial version
 *                          D Pietrasik
 * 05-23-08   ~1   41927JM  Santiago SC      -allow the alteration pending status when doing an unmerge
 * 05-28-08   ~2   41795JL  D Pietrasik      -comment out remove button
 * 05-29-08   ~3   41268MZ  D Pietrasik      -handle change to print caseContent
 * 07-02-08   ~4   41674JM  Santiago SC      -Use PackCode insted of DimCode for Weights and Dims, Acknowledge
 * 07-28-08   ~5   42330JM  Santiago SC      -Remove MFSAcknowledgeDialog for updateWeightandDims
 * 09-01-15   ~6   39097MC  Santiago SC      -Backup initial container for CHECKHANDLING within MFSSelectWorkPanel 
 * 09-08-31   ~7   37550JL  Ray Perry        -New logic for Build Ahead on Entity Merge screen
 * 09-09-01   ~8   37550JL  Brian Becker     -add ITEM to Find popup screen           
 * 09-09-14   ~9   37550JL  Ray Perry        -Apply Build Ahead logic
 * 10-03-03	  ~10  47595MZ  Ray Perry		 -Shenzhen Efficiencies 
 * 							Toribio H.
 * 2010-08-31 ~11  46704EM  Edgar V          -Compare with not null
 * 2010-10-01 ~12  48749JL  Santiago SC      -Hazmat labeling
 * 2011-02-01 ~13  50166JL  Alma N. P.       -Verify hazmat labeling only when entity cntr exist
 * 2011-02-08 ~14  49514JM  Santiago SC      -Allow unmerge for completed containers
 * 2011-10-18 ~15  00177780 VH Avila         -Rearrange Entity Merge screen & change the BA process logic
 * 2011-11-14 ~16  De602455 VH Avila    	 -Change the font color to White, this way we'll hide the chars.
 * 2012-01-06 ~17  De633739 VH Avila         -Validate that just MFGNs with blank TLCODS are considered by BA,
 *                                            MFGNs with TLCODS = 'BA' must be pulled out from MFGNs Vector
 * 2012-02-08 ~18  00198560 VH Avila         -Fix the issues that were found on Production after rearrange IPSR was installed                                                                                        
 * 2012-03-19 ~19  00202399 Toribio H.       -Make BA Efficiency Changes, BA Apply pending containers
 * 2012-05-06 ~20  00205293 Edgar V.         -Validate to create a new entity container
 * 2012-08-21 ~21  00203833 Edgar Mercado    -(MCI-DAD CR810.BA1) - BA Eliminate Swap Order Process from BA Apply
 * 2012-08-21 ~22  DE803040 Edgar Mercado    -RCQ00203833INT001- Verify a valid BA Order is applied to a valid Customer Order
 * 2012-10-22 ~23  00205020 Edgar Mercado    -RCQ00205020 (MCI-DAD CR810.BA2) -BA Pallet Apply for Build Ahead Process (MFS)
 * 2013-03-19 ~24  DE911205 Edgar Mercado    -Entity level container is not automatically completed after pallet apply
 * 2013-04-16 ~25  DE930380 Edgar Mercado    -'1Y+MFGN' barcode of BA order in PACKPROD label is not recognized by MFS client
 *                 DE930522 Edgar Mercado    -Case content label should printed by entity level container after a successful pallet apply
 * 2014-06-24 ~26  STY1145821 VH Avila       -Read and handle new IPRCONTAINERS XML tag.                 
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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.*;
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
import com.ibm.rchland.mfgapps.mfsclient.MFSDialog;
import com.ibm.rchland.mfgapps.mfsclient.MFSFrame;
import com.ibm.rchland.mfgapps.mfsclient.MFSMenuButton;
import com.ibm.rchland.mfgapps.mfsclient.MFSPanel;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSEntityLocationDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSEntityLocationPrintDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSGenericListDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSGetMultiValueDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSGetValueDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSGetValuesDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSTiedContainerDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSWeightDimDialog;
import com.ibm.rchland.mfgapps.mfsclient.renderer.MFSEntityMergeTreeCellRenderer;
import com.ibm.rchland.mfgapps.mfsclient.struct.MFSLocation;
import com.ibm.rchland.mfgapps.mfsclient.tree.MFSEntityMergeCntrUO;
import com.ibm.rchland.mfgapps.mfsclient.tree.MFSEntityMergeMfgnUO;
import com.ibm.rchland.mfgapps.mfsclient.tree.MFSEntityMergeItemUO;
import com.ibm.rchland.mfgapps.mfsclient.tree.MFSEntityMergeParseStrategy;
import com.ibm.rchland.mfgapps.mfsclient.tree.MFSEntityMergeShpnUO;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSButtonIterator;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSConstants;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSHazmatLabeling;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSPrintingMethods;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFStfParser;
import com.ibm.rchland.mfgapps.mfscommon.MFSActionable;
import com.ibm.rchland.mfgapps.mfscommon.MFSConfig;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSComm;
import com.ibm.rchland.mfgapps.client.utils.xml.IGSXMLDocument;                  /*~23A*/

/**
 * <code>MFSEntityMergePanel</code> is the <code>MFSPanel</code> used to
 * perform Ship Entity Merge operations.
 * @author The MFS Client Development Team
 */

/**
 * @author Administrator
 *
 */
@SuppressWarnings("serial")
public class MFSEntityMergePanel
	extends MFSPanel
	implements TreeSelectionListener /*,MouseListener*/
{
	/** The default screen ID of an <code>MFSEntityMergePanel</code>. */
	public static final String SCREEN_ID = "ENTMERGE"; //$NON-NLS-1$

	/** The default screen name of an <code>MFSEntityMergePanel</code>. */
	public static final String SCREEN_NAME = "Entity Merge"; //$NON-NLS-1$

	/** The CNTR type for the <code>MFSGetValueDialog</code>. ~19*/
	public static final int PROMT_DIALOG_TYPE_SIZE_CNTR = 0;
	
	/** The MFGN type for the <code>MFSGetValueDialog</code>. ~19*/
	public static final int PROMT_DIALOG_TYPE_SIZE_MFGN_PALLETID = 13; // ~23C Change size from 7 to 13 and add PALLETID to the name

	/** View only mode. */
	public static final char VIEW_ONLY = 'V';

	/**
	 * Merging mode, which allows the user to perform Ship Entity Merge
	 * operations.
	 */
	public static final char MERGING = 'M';

	/** The length of a container number. */
//	private static final int CNTR_SIZE = 10;

	/** END_ENT suspend mode. */
	private static final char END_MODE_SUSPEND = 'S';

	/** END_ENT end mode. */
	private static final char END_MODE_END = 'E';

	/** END_ENT complete container mode. */
	private static final char END_MODE_COMPLETE = 'C';

	/** UPDT_ENT delete container mode. */
	private static final char UPDT_DELETE = 'D';

	/** UPDT_ENT new container mode. */
	private static final char UPDT_NEW = 'N';

	/** UPDT_ENT merge containers mode. */
	private static final char UPDT_MERGE = 'M';

	/** UPDT_ENT unmerge containers mode. */
	private static final char UPDT_UNMERGE = 'U';

	/** UPDT_ENT refresh containers Tree mode. */
	/* private static final char UPDT_REFRESH = 'R'; */
	
	/** Parm to send to newContainer and endEntity methods to specify not PalletID call*/
	public static final String BLANKS8 = "        "; //$NON-NLS-1$

	/** The Find Container (F2) <code>MFSMenuButton</code>. */
	private final MFSMenuButton pbFind = MFSMenuButton.createSmallButton(
			"Find", "smF2.gif", "Find Container", true);  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$

	/** The End Entity (F3) <code>MFSMenuButton</code>. */
	private final MFSMenuButton pbEnd = MFSMenuButton.createSmallButton(
			"End", "smF3.gif", "End Entity", true);  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$

	/** The Exit (F3) <code>MFSMenuButton</code>. */
	private final MFSMenuButton pbExit = MFSMenuButton.createSmallButton(
			"Exit", "smF3.gif", "Exit", false);  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$

	/** The Delete Container (F4) <code>MFSMenuButton</code>. */
	private final MFSMenuButton pbDelete = MFSMenuButton.createSmallButton(
			"Delete", "smF4.gif", "Delete Container", true);  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$

	/** The Merge Container (F5) <code>MFSMenuButton</code>. */
	private final MFSMenuButton pbMerge = MFSMenuButton.createSmallButton(
			"Merge", "smF5.gif", "Merge Container", true);  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$

	/** The Unmerge Container (F6) <code>MFSMenuButton</code>. */
	private final MFSMenuButton pbUnmerge = MFSMenuButton.createSmallButton(
			"Unmerge", "smF6.gif", "Unmerge Container", true);  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$

	/** The Suspend Entity (F7) <code>MFSMenuButton</code>. */
	private final MFSMenuButton pbSuspend = MFSMenuButton.createSmallButton(
			"Suspend", "smF7.gif", "Suspend Entity", true);  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$

	/** The Print Container Label (F8) <code>MFSMenuButton</code>. */
	private final MFSMenuButton pbPrint = MFSMenuButton.createSmallButton(
			"Print", "smF8.gif", "Print Container Label", true);  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$

	/** The New Container (F9) <code>MFSMenuButton</code>. */
	private final MFSMenuButton pbNew = MFSMenuButton.createSmallButton(
			"New", "smF9.gif", "New Container", true);  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$

	/** The Complete Container (F10) <code>MFSMenuButton</code>. */
	private final MFSMenuButton pbComplete = MFSMenuButton.createSmallButton(
			"Complete", "smF10.gif", "Complete Container", true);  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$

	/** The Remove from Entity (F11) <code>MFSMenuButton</code>. */
	private final MFSMenuButton pbRemove = MFSMenuButton.createSmallButton(
			"Remove", "smF11.gif", "Remove from Entity", true);  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$

	/** The Expand Tree (F12) <code>MFSMenuButton</code>. */
	private final MFSMenuButton pbExpand = MFSMenuButton.createSmallButton(
			"Expand", "smF12.gif", "Expand Trees", true);  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$

	/** The Collapse Trees (F13) <code>MFSMenuButton</code>. */
	private final MFSMenuButton pbCollapse = MFSMenuButton.createSmallButton(
			"Collapse", "smF13.gif", "Collapse Trees", true);  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$

    /** The Apply BA (F14) <code>MFSMenuButton</code>. */
    private final MFSMenuButton pbApply = MFSMenuButton.createSmallButton(
            "Apply", "smF14.gif", "Apply Build Ahead", true);  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$

	/** The <code>JPanel</code> containing the <code>MFSMenuButton</code>s. */
	private final JPanel pnlButtons = new JPanel(new IGSGridLayout(3, 4, 2, 1));

	/** The user <code>JLabel</code>. */
	private final JLabel lblUser = createLabel("User: " //$NON-NLS-1$
			+ MFSConfig.getInstance().get8CharUser());

	/** The ship entity <code>JLabel</code>. */
	private final JLabel lblShpn = createLabel(null);

	/** The scheduled ship date <code>JLabel</code>. */
	private final JLabel lblSchd = createLabel(null);

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

	/** The <code>DefaultTreeModel</code> for {@link #trRightTree}. */
	private final DefaultTreeModel fieldRightTreeModel = new DefaultTreeModel(null, false);

	/** The <code>JTree</code> on the right side of the panel. */
	private final JTree trRightTree = createTree(this.fieldRightTreeModel);

	/** The <code>JScrollPane</code> for {@link #trRightTree}. */
	private final JScrollPane spRightTree = createScrollPane(this.trRightTree);

	/** The <code>MFSButtonIterator</code> for the {@link MFSMenuButton}s. */
	private MFSButtonIterator fieldButtonIterator = createMenuButtonIterator();

	/** The <code>Map</code> of container tree nodes. */
	@SuppressWarnings("rawtypes")
	private Map fieldNodeMap = new HashMap();

	@SuppressWarnings("rawtypes")
	private Map MfgnNodeMap = new HashMap();

	/** View only or merging mode. */
	private final char fieldMode;
	
	/** ~6A - Container who displayed this MFSEntityMergePanel */
	private String fieldCntr = ""; //$NON-NLS-1$
	
	/** operation number for checking loc and dim configs */
	private String opNmbr = "*ALL"; //$NON-NLS-1$
	
	/**
	 * Constructs a new <code>MFSEntityMergePanel</code>.
	 * @param parent the <code>MFSFrame</code> used to display this panel
	 * @param source the <code>MFSPanel</code> that caused this panel to be
	 *        displayed
	 * @param mode {@link #VIEW_ONLY} or {@link #MERGING}
	 */
	public MFSEntityMergePanel(MFSFrame parent, MFSPanel source, char mode)
	{
		super(parent, source, SCREEN_NAME, SCREEN_ID, new GridBagLayout());
		this.fieldMode = mode;
		createLayout();
		configureButtons();
		addMyListeners();
	}

	/**
	 * Invoked when an action occurs.
	 * @param ae the <code>ActionEvent</code>
	 */
	public void actionPerformed(ActionEvent ae)
	{
		final Object source = ae.getSource();
		if (source == this.pbFind)
		{
			findNode();
		}
		else if (source == this.pbNew)
		{
			newContainer(BLANKS8);                      /*Add a blank string as parm ~23C*/
		}
		else if (source == this.pbDelete)
		{
			deleteContainer();
		}
		else if (source == this.pbMerge)
		{
			mergeContainer();
		}
		else if (source == this.pbUnmerge)
		{
			unmergeContainer();
		}
		else if (source == this.pbPrint)
		{
			printContainerLabel();
		}
		else if (source == this.pbSuspend)
		{
			suspendEntity();
		}
		else if (source == this.pbComplete)
		{
			completeContainer();
		}
		else if (source == this.pbEnd)
		{
			endEntity(BLANKS8);                         /*Add a blank string as parm ~24C*/
		}
		else if (source == this.pbRemove)
		{
			removeFromEntity();
		}
		else if (source == this.pbExit)
		{
			getParentFrame().restorePreviousScreen(this);
		}
		else if (source == this.pbExpand)
		{
			expandTrees();
		}
		else if (source == this.pbCollapse)
		{
			collapseTrees();
		}
        else if (source == this.pbApply)
        {
        	removeMyListeners();
            applyBuildAhead();
            addMyListeners();
        }
	}

	/** Adds the listeners to this panel's <code>Component</code>s. */
	private void addMyListeners()
	{
		this.addKeyListener(this);
		this.spLeftTree.addKeyListener(this);
		this.trLeftTree.addKeyListener(this);
		this.spRightTree.addKeyListener(this);
		this.trRightTree.addKeyListener(this);
		this.pnlButtons.addKeyListener(this);
		this.spNodeInfo.addKeyListener(this);
		this.taNodeInfo.addKeyListener(this);
		this.trLeftTree.addTreeSelectionListener(this);
		this.trRightTree.addTreeSelectionListener(this);

		this.fieldButtonIterator.reset();
		while (this.fieldButtonIterator.hasNext())
		{
			JButton next = this.fieldButtonIterator.nextMenuButton();
			next.addActionListener(this);
			next.addKeyListener(this);
		}
	}

	/**
     * Apply Build Ahead
     * <p>
     * Invoked when {@link #pbApply} is selected.
     */
	private void applyBuildAhead()
    {       
        MFSEntityMergeShpnUO shpnUO = null;              /*~21A*/
        String iprItem = ""; //$NON-NLS-1$

        try
        {                                
            String mfgn_pallet_id = getContainerNumber("Enter the IPR MFGN or Pallet ID to apply", "MFGN/PALLET ID", "Apply", 'a', PROMT_DIALOG_TYPE_SIZE_MFGN_PALLETID); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ // ~23C
            String shpn = "       ";          //$NON-NLS-1$   /*~21A*/
            String entityCntr = "          "; //$NON-NLS-1$
            final MFSConfig config = MFSConfig.getInstance(); /*~23M*/
            String iprCntrs = null;                            /*~23M*/
            
            /* Extract SHPN and ENTITYCNTR since it will be needed for MFGN or PALLETID processing  ~23A*/
            if (mfgn_pallet_id!=null)
            {    
            	entityCntr = getEntityCntr();                   //~23A           
				shpnUO = getShpnUO();                           //~21A
				shpn = shpnUO.getShpn(); //$NON-NLS-1$          //~21A            	
            	
            }
            
            /* If mfgn_pallet_id equals to 7, then follow processing for MFGN ~23C*/
            if (mfgn_pallet_id!=null && mfgn_pallet_id.trim().length() == 7) /*~23C*/
            {    
                IGSXMLTransaction apyBA = new IGSXMLTransaction("APPLY1BA");  //$NON-NLS-1$ /*~21C*/
	                
                apyBA.setActionMessage("Applying build ahead ..."); //$NON-NLS-1$
                apyBA.startDocument();
                apyBA.addElement("CELL", config.get8CharCell());  //$NON-NLS-1$
                apyBA.addElement("CTYP", config.get8CharCellType());  //$NON-NLS-1$
                apyBA.addElement("USER", config.get8CharUser());  //$NON-NLS-1$
                
                apyBA.addElement("IPRMFGN", mfgn_pallet_id.substring(0, 7)); //$NON-NLS-1$ /*~23C*/
                apyBA.addElement("SHPN", shpn); //$NON-NLS-1$	 /*~21A*/            
                apyBA.addElement("ENTCNTR", entityCntr);								/*~15C*/ //$NON-NLS-1$
                apyBA.endDocument();
                
                MFSComm.getInstance().execute(apyBA, this);
                if (apyBA.getReturnCode() != 0) 
                {
          	    	/*
          	    	 * ~23C Change 2nd parm to false so a 'Program Exception' message is not sent at the
          	    	 *      front of the window error message.
          	    	 */    
                   	throw new IGSTransactionException(apyBA, false);                	
                }
                else
                {
                  	/* ~21C The APPLY1BA ran successfully and we have to remove the MFGN and decrease the Qty */
                   	iprCntrs = apyBA.getNextElement("IPRCONTAINERS"); //$NON-NLS-1$			/*~26C*/
                   	iprItem = apyBA.getNextElement("IPRITEM"); //$NON-NLS-1$				/*~26C*/
                    	
                   	decreaseAndCreateContainer(iprCntrs, iprItem, entityCntr);	
                }
                /* do recursive this function */
                applyBuildAhead();
            }
            /* Processing for Pallet ID ~23A*/
            else if (mfgn_pallet_id!=null && mfgn_pallet_id.trim().length() == 13)
            {
            	newContainer("PALLETID");
            	/* Get the entity container after newContainer method has been called since we can
            	 * have a new one.                                                             ~23A
            	 */
            	entityCntr = getEntityCntr();                                                  //~23A            	 
            	
            	IGSXMLTransaction apyPIDBA = new IGSXMLTransaction("APPLY_PAL");                 
            	apyPIDBA.setActionMessage("Applying build ahead using Pallet ID..."); 
            	apyPIDBA.startDocument();
            	apyPIDBA.addElement("PALLETID", mfgn_pallet_id);
                apyPIDBA.addElement("SHPN", shpn);
                apyPIDBA.addElement("ENTCNTR", entityCntr);
                apyPIDBA.addElement("CELL", config.get8CharCell());  						
                apyPIDBA.addElement("CTYP", config.get8CharCellType());  
             	apyPIDBA.addElement("USER", config.get8CharUser());                  

                apyPIDBA.endDocument();
            
          	    MFSComm.getInstance().execute(apyPIDBA, this);

          	    if (apyPIDBA.getReturnCode() != 0) 
          	    {
          	    	/*
          	    	 * ~23C Change 2nd parm to false so a 'Program Exception' message is not sent at the
          	    	 *      front of the window error message.
          	    	 */          	    	
               		throw new IGSTransactionException(apyPIDBA, false);
          	    }
          	    else
          	    {
          	        IGSXMLDocument rowDoc = new IGSXMLDocument(apyPIDBA.getOutput());
          	        
          	        while (null != rowDoc.stepIntoElement("ROW"))
              	    {
                       	iprCntrs = rowDoc.getNextElement("IPRCONTAINERS"); //$NON-NLS-1$		/*~26C*/
                       	iprItem = rowDoc.getNextElement("IPRITEM"); //$NON-NLS-1$
                       	
                       	decreaseAndCreateContainer(iprCntrs, iprItem, entityCntr);				/*~26C*/
                       	rowDoc.stepOutOfElement();
              	    }
          	        /*
          	         * End automatically (Complete) the entity container when a PalletID has 
          	         * been successfully applied. ~24A
          	         */
          	        endEntity("PALLETID");          	                  	        
          	        
                    /* Do recursive this function */
                    applyBuildAhead();
          	    }
            }/*End for processing PalletID   ~23A*/
        }
        catch (Exception e)
        {
            String title = "Apply Build Ahead Error"; //$NON-NLS-1$
            IGSMessageBox.showOkMB(getParentFrame(), title, null, e);
        }
        finally
        {
            
        }
    }
    
	/**
     * Get entity container on the right tree that is of type 'E' and on status ready 'R'
     * ***Main code was moved from applyBuildAhead.                                       ~23A
	 * @param entityCntr the Container for the entity on the panel
     * <p>
     * 
     */
	@SuppressWarnings("rawtypes")
	private String getEntityCntr()
	{  
        DefaultMutableTreeNode cntrRoot = null;		
        MFSEntityMergeCntrUO cntrUO = null;
        DefaultMutableTreeNode cntrNode = null; 
        String result = "          ";
        
        cntrRoot = (DefaultMutableTreeNode) this.fieldRightTreeModel.getRoot();  
            
        for (Enumeration cntrEnum = cntrRoot.children(); cntrEnum.hasMoreElements();) {
           	cntrNode = (DefaultMutableTreeNode) cntrEnum.nextElement();
           	if(cntrNode.getUserObject() instanceof MFSEntityMergeCntrUO)
           	{
               	cntrUO = (MFSEntityMergeCntrUO) cntrNode.getUserObject(); 
                if (cntrUO.getType() == 'E')
                {
                 	if ((cntrUO.isEditable()) && (cntrUO.getStatus() == MFSEntityMergeCntrUO.STATUS_READY))
                   	{
                 		result = cntrUO.getCntr();
                   		break;
                   	}
                }                            
           	}
        }
        return result;
	}
    
	/**
     * Decrease item quantity on the left three side and Create an entry for the IPR container on the
     * right three side. ***Main code was moved from applyBuildAhead.                            ~23A
	 * @param iprCntr the Container returned by the server transaction (APPLY1BA or APPLY_PAL)
	 * @param iprItem the Item returned by the server transaction (APPLY1BA or APPLY_PAL)
	 * @param entityCntr the Container for the entity on the panel
     * <p>
     * 
     */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void decreaseAndCreateContainer(String iprCntrs, String iprItem, String entityCntr)
	{
        MFSEntityMergeItemUO itemUO = null;
        DefaultMutableTreeNode itemNode = null;
        DefaultMutableTreeNode cntrRoot = null; 
        DefaultMutableTreeNode cntrNode = null;
        String dataCntr = "";
        boolean foundItem = false;
        
        /* ~21A We have to look the ITEM into the tree */ 
       	DefaultMutableTreeNode shpnRoot = (DefaultMutableTreeNode) this.fieldLeftTreeModel.getRoot();                    	
            
        for (Enumeration itemEnum = shpnRoot.children(); itemEnum.hasMoreElements() && !foundItem;) {
           	itemNode = (DefaultMutableTreeNode) itemEnum.nextElement();
           	if(itemNode.getUserObject() instanceof MFSEntityMergeItemUO)
           	{
               	itemUO = (MFSEntityMergeItemUO) itemNode.getUserObject(); 
                   if (itemUO.getItem().equals(iprItem))
                   {
                       foundItem = true;
                   }                            
           	}
        }                    	
        	
        /* 
         *  Server transaction ran successfully and we have to remove the ITEM and decrease the Qty
         */
        if (foundItem)
        {
           	itemUO.removeMfgn();
           	itemUO.updateTreeStrings();
           	itemNode.remove(0);
           	this.fieldLeftTreeModel.reload();
           	this.trLeftTree.repaint();                        	
        }     
        
        MFSEntityMergeCntrUO cntrUO = null;
        cntrRoot = (DefaultMutableTreeNode) this.fieldRightTreeModel.getRoot();
        
        for (Enumeration cntrEnum = cntrRoot.children(); cntrEnum.hasMoreElements();) {
           	cntrNode = (DefaultMutableTreeNode) cntrEnum.nextElement();
           	if(cntrNode.getUserObject() instanceof MFSEntityMergeCntrUO)
           	{
               	cntrUO = (MFSEntityMergeCntrUO) cntrNode.getUserObject(); 
                if (cntrUO.getCntr().toString().trim().equals(entityCntr.trim())  && cntrUO.getStatus() == MFSEntityMergeCntrUO.STATUS_READY)
                {
                   break;
                }                            
           	}
        }        
	
        IGSXMLDocument iprCntr = new IGSXMLDocument(iprCntrs);
        
        if(entityCntr.trim().equals("")) //$NON-NLS-1$
        {
        	while(iprCntr.stepIntoElement("IPRCNTR") != null)												/*~26A*/
            {																								/*~26A*/
        		MFSEntityMergeCntrUO newCntr = new MFSEntityMergeCntrUO(iprCntr.getNextElement("CNTR"));	/*~26C*/
        		newCntr.setArea(""); //$NON-NLS-1$
        		newCntr.setCloc(""); //$NON-NLS-1$
        		newCntr.setStatus('C');   //~19 //~22C Set new container to C. No longer needed MI trx.
        		newCntr.setType('S');
        		DefaultMutableTreeNode node = new DefaultMutableTreeNode(newCntr, true);
        		cntrRoot.insert(node, cntrRoot.getChildCount());
        		this.fieldRightTreeModel.reload();
        		this.fieldNodeMap.put(iprCntr, node);				/*~18A*/
        		
        		iprCntr.stepOutOfElement();																	/*~26A*/
            }																								/*~26A*/
        }
        else
        {
        	while(iprCntr.stepIntoElement("IPRCNTR") != null)												/*~26A*/
            {																								/*~26A*/
        		String cntr = iprCntr.getNextElement("CNTR");												/*~26A*/
        		if(!cntr.trim().equals(entityCntr.trim()))													/*~26A*/
        		{
	        		MFSEntityMergeCntrUO newCntr = new MFSEntityMergeCntrUO(cntr);	/*~26C*/
	        		newCntr.setArea(""); //$NON-NLS-1$
	        		newCntr.setCloc(""); //$NON-NLS-1$
	        		newCntr.setStatus('C');  //~19 //~22C Set new container to C. No longer needed MI trx.
	        		newCntr.setType('M');
	        		DefaultMutableTreeNode node = new DefaultMutableTreeNode(newCntr, true);
	        		cntrNode.insert(node, cntrNode.getChildCount());
	        		this.fieldRightTreeModel.reload();
	        		this.fieldNodeMap.put(iprCntr, node);				/*~18A*/
        		}        		
        		iprCntr.stepOutOfElement();																	/*~26A*/
            }																								/*~26A*/
        }    	
    }

	/**
	 * Updates the container objects (and their parents) of any MFGNs with
	 * pending alterations.
	 * @param mfgnNodes the <code>Enumeration</code> of MFGN nodes to check
	 *        for alterations
	 */
	@SuppressWarnings("rawtypes")
	private void checkPendingAlters(Enumeration mfgnNodes)
	{
		while (mfgnNodes.hasMoreElements())
		{
			DefaultMutableTreeNode mfgnNode = (DefaultMutableTreeNode) mfgnNodes.nextElement();
			/* With new MFSEntityMergeItemUO object, now we have to validate 
			 * that we are checking just nodes with MFSEntityMergeMfgnUO objects
			 * ~15A
			 */ 
			if(mfgnNode.getUserObject() instanceof MFSEntityMergeMfgnUO)
			{
				MFSEntityMergeMfgnUO mfgnObj = (MFSEntityMergeMfgnUO) mfgnNode.getUserObject();
				if ((mfgnObj.getAlti() != ' ') && (mfgnObj.getAlti() != '0'))
				{
					Enumeration checkLeaves = mfgnNode.depthFirstEnumeration();
					while (checkLeaves.hasMoreElements())
					{
						DefaultMutableTreeNode checkNode = (DefaultMutableTreeNode) checkLeaves.nextElement();
						if (checkNode.isLeaf())
						{
							markAlterPending(checkNode.getUserObject());
						}
					}
				}
			}
		}
	}

	/**
	 * Collapse down the tree except the first level.
	 * @param tree <code>JTree</code> to collapse
	 */
	private void collapseTree(JTree tree)
	{
		for (int i = tree.getRowCount() - 1; i >= 1; i--)
		{
			tree.collapseRow(i);
		}
	}

	/**
	 * Collapses the trees down to the roots.
	 * <p>
	 * Invoked when {@link #pbCollapse} is selected.
	 */
	private void collapseTrees()
	{
		collapseTree(this.trLeftTree);
		collapseTree(this.trRightTree);
	}

	/**
	 * Completes a container by performing the following steps:
	 * <ol>
	 * <li>Prompts the user for a container.</li>
	 * <li>Updates the weight and dimensions of the container.</li>
	 * <li>Prints a label for the container.</li>
	 * <li>Calls END_ENT.</li>
	 * </ol>
	 * Invoked when {@link #pbComplete} is selected.
	 */
	private void completeContainer()
	{
		try
		{
			removeMyListeners();
			String cntrNum = getContainerNumber("Enter the container value", "CNTR", "Complete", 'c', PROMT_DIALOG_TYPE_SIZE_CNTR); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			DefaultMutableTreeNode cntrNode = getNode(cntrNum);
			if (cntrNode != null)
			{
				String title = "Complete Container Error"; //$NON-NLS-1$
				String msg = null;
				MFSEntityMergeCntrUO cntrUO = (MFSEntityMergeCntrUO) cntrNode.getUserObject();
				if ((cntrUO.isMergedContainer()))
				{
					msg = "Container {0} cannot be completed because it is merged."; //$NON-NLS-1$
				}
				else if (cntrUO.getStatus() != MFSEntityMergeCntrUO.STATUS_READY)
				{
					msg = "Container {0} cannot be completed because a BA Apply or {1}."; //$NON-NLS-1$
				}
				else if (isEmptyEntityContainer(cntrNode))
				{
					msg = "Container {0} is an empty entity container and cannot be completed."; //$NON-NLS-1$
				}
				else if (hasEmptyEntityContainer(cntrNode))
				{
					msg = "Container {0} contains an empty entity container and cannot be completed."; //$NON-NLS-1$
				}
				else if (!vfyHazmatLabeling(cntrNum, cntrUO.getType())) //~12A
				{
					msg = "Container {0} cannot be completed because hazmat labeling verification is not complete."; //$NON-NLS-1$
				}
				else if (updateWeightDim(cntrUO.getCntr(), "E")) //$NON-NLS-1$
				{
					submitEndEntity(END_MODE_COMPLETE, "Completing Container...", cntrNum); //$NON-NLS-1$
					/* ~19 Move Case content Printing after END Complete is executed successfuly
					 * It was before, now is after... (Big change uh?!)
					 */
					MFSPrintingMethods.caseContent(cntrNum, null, 1, getParentFrame()); // ~3C
					title = "Container Complete"; //$NON-NLS-1$
					msg = "Container {0} marked complete; please send it to shipping."; //$NON-NLS-1$
				}

				if (msg != null)
				{
					String[] arguments = new String[] {
							cntrNum, cntrUO.getStatusText()
					};
					msg = MessageFormat.format(msg, (Object[])arguments);
					IGSMessageBox.showOkMB(getParentFrame(), title, msg, null);
				}
			}
		}
		catch (Exception e)
		{
			String title = "Complete Container Error"; //$NON-NLS-1$
			IGSMessageBox.showOkMB(getParentFrame(), title, null, e);
		}
		finally
		{
			addMyListeners();
		}
	}

	/** Determines which <code>MFSMenuButton</code>s are displayed. */
	private void configureButtons()
	{
		this.pnlButtons.removeAll();
		if (this.fieldMode == MERGING)
		{
			this.pnlButtons.add(this.pbFind);
			this.pbFind.setEnabled(true);
			this.pnlButtons.add(this.pbEnd);
			this.pbEnd.setEnabled(true);
			this.pbExit.setEnabled(false);
			this.pnlButtons.add(this.pbDelete);
			this.pbDelete.setEnabled(true);
			this.pnlButtons.add(this.pbMerge);
			this.pbMerge.setEnabled(true);
			this.pnlButtons.add(this.pbUnmerge);
			this.pbUnmerge.setEnabled(true);
			this.pnlButtons.add(this.pbSuspend);
			this.pbSuspend.setEnabled(true);
			this.pnlButtons.add(this.pbPrint);
			this.pbPrint.setEnabled(true);
			this.pnlButtons.add(this.pbNew);
			this.pbNew.setEnabled(true);
			this.pnlButtons.add(this.pbComplete);
			this.pbComplete.setEnabled(true);
			this.pbRemove.setEnabled(false);  // ~2C
			this.pnlButtons.add(this.pbExpand);
			this.pbExpand.setEnabled(true);
			this.pnlButtons.add(this.pbCollapse);
			this.pbCollapse.setEnabled(true);
            this.pnlButtons.add(this.pbApply);
            this.pbApply.setEnabled(true);          /*~19A*/
		}
		else
		{
			this.pnlButtons.add(this.pbFind);
			this.pbFind.setEnabled(true);
			this.pnlButtons.add(this.pbExit);
			this.pbExit.setEnabled(true);
			this.pbEnd.setEnabled(false);
			this.pbDelete.setEnabled(false);
			this.pbMerge.setEnabled(false);
			this.pbUnmerge.setEnabled(false);
			this.pbSuspend.setEnabled(false);
			this.pbPrint.setEnabled(false);
			this.pbNew.setEnabled(false);
			this.pbComplete.setEnabled(false);
			this.pbRemove.setEnabled(false);
			this.pnlButtons.add(this.pbExpand);
			this.pbExpand.setEnabled(true);
			this.pnlButtons.add(this.pbCollapse);
			this.pbCollapse.setEnabled(true);
            this.pnlButtons.add(this.pbApply);
            this.pbApply.setEnabled(true);
		}
	}

	/**
	 * Creates an information <code>JTextPane</code>.
	 * @return the <code>JTextPane</code>
	 */
	private JTextPane createInfoTextPane()
	{
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
	 * Creates a header <code>JLabel</code>.
	 * @param text the initial text displayed by the <code>JLabel</code>
	 * @return the <code>JLabel</code>
	 */
	private JLabel createLabel(String text)
	{
		JLabel result = new JLabel(text, null, SwingConstants.LEFT);
		result.setFont(MFSConstants.MEDIUM_MONOSPACED_FONT);
		result.setForeground(MFSConstants.PRIMARY_FOREGROUND_COLOR);
		return result;
	}
	
	
	/** Adds this panel's <code>Component</code>s to the layout. */
	private void createLayout()
	{
		this.pnlButtons.setBackground(MFSConstants.PRIMARY_BACKGROUND_COLOR);
		this.pnlButtons.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

		JPanel buttonPanelHolder = new JPanel(new FlowLayout());
		buttonPanelHolder.setBorder(BorderFactory.createLineBorder(MFSConstants.PRIMARY_FOREGROUND_COLOR));
		buttonPanelHolder.setBackground(MFSConstants.PRIMARY_BACKGROUND_COLOR);
		buttonPanelHolder.add(this.pnlButtons);

		JPanel headerPanel = new JPanel(null);
		headerPanel.setBorder(new EmptyBorder(0, 0, 0, 5));
		BoxLayout layout = new BoxLayout(headerPanel, BoxLayout.LINE_AXIS);
		headerPanel.setLayout(layout);
		headerPanel.setBackground(MFSConstants.PRIMARY_BACKGROUND_COLOR);
		headerPanel.add(this.lblUser);
		headerPanel.add(Box.createHorizontalGlue());
		headerPanel.add(this.lblShpn);
		headerPanel.add(Box.createHorizontalGlue());
		headerPanel.add(this.lblSchd);

		MFSFrame parent = getParentFrame();
		Insets insets = parent.getInsets();
		int location = (parent.getWidth() - insets.left - insets.right) / 2;
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, false,
				this.spLeftTree, this.spRightTree);
		splitPane.setDividerLocation(location);

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
		gbc.weighty = 1.0;
		gbc.anchor = GridBagConstraints.NORTH;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(4, 4, 4, 4);

		add(splitPane, gbc);
	}

	/**
	 * Creates an <code>MFSButtonIterator</code> for this panel's
	 * <code>MFSMenuButton</code>s.
	 * @return the <code>MFSButtonIterator</code>
	 */
	private MFSButtonIterator createMenuButtonIterator()
	{
		// The constructor param is the number of buttons
		MFSButtonIterator result = new MFSButtonIterator(14); //~2C,~19C
		result.add(this.pbFind);
		result.add(this.pbEnd);
		result.add(this.pbExit);
		result.add(this.pbDelete);
		result.add(this.pbMerge);
		result.add(this.pbUnmerge);
		result.add(this.pbSuspend);
		result.add(this.pbPrint);
		result.add(this.pbNew);
		result.add(this.pbComplete);
/*		result.add(this.pbRemove);  ~2C */
		result.add(this.pbExpand);
		result.add(this.pbCollapse);
        result.add(this.pbApply);
		return result;
	}

	/**
	 * Creates a <code>String</code> containing all the MFGN/IDSS values for
	 * the ship entity. The <code>String</code> can be used to load a
	 * {@link MFSGenericListDialog}.
	 * @return the <code>String</code> of MFGN/IDSS values
	 * @throws IGSException if there is a bad MFGN/IDSS value
	 */
	@SuppressWarnings("rawtypes")
	private String createMfgnIdssString()
		throws IGSException
	{
		StringBuffer result = new StringBuffer();
		DefaultMutableTreeNode node;
		node = (DefaultMutableTreeNode) this.fieldLeftTreeModel.getRoot();
		Enumeration nodes = node.children();
		while (nodes.hasMoreElements())
		{
			node = (DefaultMutableTreeNode) nodes.nextElement();
			Object obj = node.getUserObject();
			if (obj instanceof MFSEntityMergeMfgnUO)
			{
				if (!hasShippedContainer(node))
				{
					int length = result.length();
					MFSEntityMergeMfgnUO mfgnObj = (MFSEntityMergeMfgnUO) obj;
					result.append(mfgnObj.getMfgn());
					result.append("/"); //$NON-NLS-1$
					result.append(mfgnObj.getIdss());

					if (result.length() - length != 12)
					{
						StringBuffer erms = new StringBuffer();
						erms.append("Invalid MFGN/IDSS value "); //$NON-NLS-1$
						erms.append(mfgnObj.getMfgn());
						erms.append("/"); //$NON-NLS-1$
						erms.append(mfgnObj.getIdss());
						erms.append("."); //$NON-NLS-1$
						throw new IGSException(erms.toString(), false);
					}
				}
			}
		}
		return result.toString();
	}


	/**
	 * Creates a <code>JScrollPane</code> for a <code>JComponent</code>.
	 * @param component the <code>JComponent</code>
	 * @return the <code>JScrollPane</code>
	 */
	private JScrollPane createScrollPane(JComponent component)
	{
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
	private JTree createTree(TreeModel treeModel)
	{
		JTree result = new JTree(treeModel);
		result.setCellRenderer(new MFSEntityMergeTreeCellRenderer());
		ToolTipManager.sharedInstance().registerComponent(result);
		result.setRootVisible(true);
		result.setShowsRootHandles(true);
		DefaultTreeSelectionModel selModel = new DefaultTreeSelectionModel();
		selModel.setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		result.setSelectionModel(selModel);
		return result;
	}

	
	/**
	 * Deletes the selected container if it is empty.
	 * <p>
	 * Invoked when {@link #pbDelete} is selected.
	 */
	@SuppressWarnings("rawtypes")
	private void deleteContainer()
	{
		try
		{
			removeMyListeners();
			String cntrNum = getContainerNumber("Enter the container value", "CNTR", "Delete", 'd', PROMT_DIALOG_TYPE_SIZE_CNTR); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			DefaultMutableTreeNode cntrNode = getNode(cntrNum);
			if (cntrNode != null)
			{
				String erms = null;
				MFSEntityMergeCntrUO cntrUO = (MFSEntityMergeCntrUO) cntrNode.getUserObject();
				if (!cntrUO.isEditable())
				{
					erms = "Container {0} cannot be deleted because it is not editable."; //$NON-NLS-1$
				}
				else if (cntrNode.getChildCount() != 0)
				{
					erms = "Container {0} cannot be deleted because it is not empty."; //$NON-NLS-1$
				}
				else
				{
					updtEntity(UPDT_DELETE, "Deleting Container...", cntrNum, null); //$NON-NLS-1$
				}

				if (erms != null)
				{
					String title = "Delete Container Error"; //$NON-NLS-1$
					String arguments[] = {
						cntrNum
					};
					erms = MessageFormat.format(erms, (Object[])arguments);
					IGSMessageBox.showOkMB(getParentFrame(), title, erms, null);
				}
				if (cntrNum.equals(this.fieldCntr))
				{
					cntrNode = (DefaultMutableTreeNode) this.fieldRightTreeModel.getRoot();
					Enumeration topCntrs = cntrNode.children();
					if (topCntrs.hasMoreElements())
					{
						cntrNode = (DefaultMutableTreeNode) topCntrs.nextElement();
						cntrUO = (MFSEntityMergeCntrUO) cntrNode.getUserObject();
						
						this.fieldCntr = cntrUO.getCntr();
					}
				}
			}
		}
		catch (Exception e)
		{
			String title = "Delete Container Error"; //$NON-NLS-1$
			IGSMessageBox.showOkMB(getParentFrame(), title, null, e);
		}
		finally
		{
			addMyListeners();
		}
	}

	/**
	 * Ends the ship entity by performing the following steps:
	 * <ol>
	 * <li>Updates the weight and dimensions of the ready top level containers.
	 * </li>
	 * <li>Prints a label for the ready top level containers.</li>
	 * <li>Calls END_ENT.</li>
	 * </ol>
	 * @param calledFrom Identifier to know if the call is when a Pallet ID was specified  ~24A
	 * Invoked when {@link #pbEnd} is selected.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void endEntity(String calledFrom)                          /*~24A*/
	{
		boolean cancel = false;
		DefaultMutableTreeNode cntrNode;
		MFSEntityMergeCntrUO cntrUO;
		try
		{
			removeMyListeners();
			cntrNode = (DefaultMutableTreeNode) this.fieldRightTreeModel.getRoot();
			Enumeration topCntrs = cntrNode.children();
			List<String> topEntityCntrList = new ArrayList<String>(); //~12A
			while (topCntrs.hasMoreElements())
			{
				cntrNode = (DefaultMutableTreeNode) topCntrs.nextElement();
				cntrUO = (MFSEntityMergeCntrUO) cntrNode.getUserObject();
				
				if('E' == cntrUO.getType()) //~12A
				{
					topEntityCntrList.add(cntrUO.getCntr()); //~12A
				}
				
				if (cntrUO.getStatus() == MFSEntityMergeCntrUO.STATUS_READY)
				{
					if (hasEmptyEntityContainer(cntrNode))
					{
						String title = "End Entity Error"; //$NON-NLS-1$
						String erms = "The ship entity cannot be ended because there are empty entity containers."; //$NON-NLS-1$
						IGSMessageBox.showOkMB(getParentFrame(), title, erms, null);
						cancel = true;
						break;
					}

					if (!updateWeightDim(cntrUO.getCntr(), "E")) //$NON-NLS-1$
					{
						cancel = true;
						break;
					}
				}
				else if (cntrUO.getStatus() == MFSEntityMergeCntrUO.STATUS_ALTER_PENDING)
				{
					String title = "End Entity Error"; //$NON-NLS-1$
					String erms = "The ship entity cannot be ended because there are pending BA Apply or alterations."; //$NON-NLS-1$
					IGSMessageBox.showOkMB(getParentFrame(), title, erms, null);
					cancel = true;
					break;
				}
			}

			if (!cancel && vfyHazmatLabeling(topEntityCntrList)) //~12C
			{
				submitEndEntity(END_MODE_END, "Ending Entity...", null); //$NON-NLS-1$y
				
				/* ~19C Change caseContent Printing after all checks were passed. */				
				topCntrs = cntrNode.children();
				/* ~25C When a Pallet ID was processed, then do not print children containers*/
				while (topCntrs.hasMoreElements() && !calledFrom.equals("PALLETID"))
				{
					cntrNode = (DefaultMutableTreeNode) topCntrs.nextElement();
					cntrUO = (MFSEntityMergeCntrUO) cntrNode.getUserObject();
				
					MFSPrintingMethods.caseContent(cntrUO.getCntr(), null, 1,
							getParentFrame()); // ~3C
				}
				
				/*
				 * When a PalletID has been processed, then update the right tree  ~24A
				 */
				if(calledFrom.equals("PALLETID"))
				{
					cntrNode = (DefaultMutableTreeNode) this.fieldRightTreeModel.getRoot();
					topCntrs = cntrNode.children();
					
					while (topCntrs.hasMoreElements())
					{
						cntrNode = (DefaultMutableTreeNode) topCntrs.nextElement();
						cntrUO = (MFSEntityMergeCntrUO) cntrNode.getUserObject();
						
						if('E' == cntrUO.getType() && cntrUO.getStatus() == MFSEntityMergeCntrUO.STATUS_READY )
						{
							cntrUO.setStatus('C');
							/* ~25A Print Entity Container*/
							MFSPrintingMethods.caseContent(cntrUO.getCntr(), null, 1,
									getParentFrame()); // ~3C
                            /* ~25A Save Entity Container on hashMap object so it can be used for re-print purposes*/
				        	this.fieldNodeMap.put(cntrUO.getCntr(), cntrNode);								
						}						
					}	
					this.fieldRightTreeModel.reload();						
				}				
				
				String title = "Entity Complete"; //$NON-NLS-1$
				String msg = "Please send all containers in entity " //$NON-NLS-1$
						+ getShpnUO().getShpn() + " to shipping."; //$NON-NLS-1$
				IGSMessageBox.showOkMB(getParent(), title, msg, null);
				
				/*
				 * When a PalletID has been processed, then do not close Merge Panel  ~24A
				 * just update the right tree
				 */
				if(!calledFrom.equals("PALLETID"))
				{
					getParentFrame().restorePreviousScreen(this);					
				}
			}
		}
		catch (Exception e)
		{
			String title = "End Entity Error"; //$NON-NLS-1$
			IGSMessageBox.showOkMB(getParentFrame(), title, null, e);
		}
		finally
		{
			addMyListeners();
		}
	}

	/**
     * Expand out the tree.
     * @param tree <code>JTree</code> to expand
     */
    private void expandTree(JTree tree)
    {
        for (int i = 1; i < tree.getRowCount(); i++)
        {
            tree.expandRow(i);
        }
    }

	/**
	 * Expands the trees.
	 * <p>
	 * Invoked when {@link #pbExpand} is selected.
	 */
	private void expandTrees()
	{
		expandTree(this.trLeftTree);
		expandTree(this.trRightTree);
	}

	/**
	 * finds a Location in the Locations List.
	 * @param <code>Vector</code> Locations List
	 * @param <code>String</code> Area
	 * @param <code>String</code> Location
	 * @return <code>MFSLocation</code> if found, <code>null</code> otherwise
	 */
    private MFSLocation findAreaInMFSLocs(Vector <MFSLocation>locList, String area, String loc)
    {
    	MFSLocation cLoc = null;
		Enumeration<MFSLocation> eLocList = locList.elements();
		
		/* Put the None Empty containers first */
		while (eLocList.hasMoreElements()) 
		{ 
			cLoc = eLocList.nextElement();
			if(cLoc.getArea().equals(area) && cLoc.getLocation().equals(loc))
			{
				return cLoc;
			}
		}
		return null;
    }

	/**
	 * Finds The Default Area/Location.
	 */    
    @SuppressWarnings("rawtypes")
	private MFSLocation findDefaultAreaLoc(DefaultMutableTreeNode topCntrNode, Vector <MFSLocation>locList)
    {
		// loop through each container and find default loc
		boolean containerFound = false;
		boolean mOrSContainerTypeFound = false;
		boolean eContainerTypeFound = false;
		DefaultMutableTreeNode cntrNode = null;
		MFSEntityMergeCntrUO cntrUO = null;
		Enumeration topCntrs = topCntrNode.children();
		String defaultArea = ""; //$NON-NLS-1$
		String defaultLoc = ""; //$NON-NLS-1$
		
		while (topCntrs.hasMoreElements() && !mOrSContainerTypeFound)
		{
			cntrNode = (DefaultMutableTreeNode) topCntrs.nextElement();
			cntrUO = (MFSEntityMergeCntrUO) cntrNode.getUserObject();
			
			if (!cntrUO.getArea().trim().equals("") || !cntrUO.getCloc().trim().equals("")) //$NON-NLS-1$ //$NON-NLS-2$
			{
				if (cntrUO.getType() == MFSEntityMergeCntrUO.TYPE_MERGED_SYSTEM || cntrUO.getType() == MFSEntityMergeCntrUO.TYPE_SYSTEM)
				{
					if (!mOrSContainerTypeFound)
					{
						defaultArea = cntrUO.getArea();
						defaultLoc = cntrUO.getCloc();
					}
					mOrSContainerTypeFound = true;
				}
				else if (cntrUO.getType() == MFSEntityMergeCntrUO.TYPE_ENTITY)
				{
					if (!eContainerTypeFound && !mOrSContainerTypeFound)
					{
						defaultArea = cntrUO.getArea();
						defaultLoc = cntrUO.getCloc();
					}
					eContainerTypeFound = true;
				}
				else
				{
					if (!eContainerTypeFound && !mOrSContainerTypeFound && !containerFound)
					{
						defaultArea = cntrUO.getArea();
						defaultLoc = cntrUO.getCloc();
					}
				}
				containerFound = true;
			}
		}    
		return findAreaInMFSLocs(locList, defaultArea, defaultLoc);
    }

    /**
	 * Prompts the user for a container number and finds that node, highlighting
	 * it and displaying it in the tree.
	 * <p>
	 * Invoked when {@link #pbFind} is selected.
	 */
	private void findNode() {
		System.out.println("Starting MFSEntityMergePanel.findNode()"); //$NON-NLS-1$
		try {
			removeMyListeners();
			
			// ***Note - cntrNum will be either the CNTR or ITEM number coming back.
			String findValue = getContainerOrPartNumber("Enter a container or part value", "CNTR", "ITEM", "Find", 'f', 0);  //$NON-NLS-1$ //$NON-NLS-2$//$NON-NLS-3$ //$NON-NLS-4$
			System.out.println("  findNode() findValue: "+findValue); //$NON-NLS-1$
			

			// findValue is either a Container value or a Partnumber value
			if (findValue != null) {
				if (!findNode(findValue)) {
					String title = "Error Finding Container or Part"; //$NON-NLS-1$
					String erms = "Container or Part " + findValue + " not found."; //$NON-NLS-1$ //$NON-NLS-2$
					IGSMessageBox.showOkMB(getParentFrame(), title, erms, null);
				}
			}
		} catch (Exception e) {
			String title = "Error Finding Container or Part"; //$NON-NLS-1$
			IGSMessageBox.showOkMB(getParentFrame(), title, null, e);
		} finally {
			this.trLeftTree.repaint();
			this.trRightTree.repaint();
			addMyListeners();
		}
	}

	/**
	 * Finds and highlights the container-or-partnumber given by <code>findValue</code>.
	 * @param findValue the container number or partnumber to find and highlight
	 * @return true if the container or partnumber was found
	 */
	@SuppressWarnings("rawtypes")
	public boolean findNode(String findValue) {
		System.out.println("Starting findNode(String findValue)"); //$NON-NLS-1$

		// findValue is either a Container value or a Partnumber value
		boolean retval = true;
		this.trRightTree.clearSelection();
		this.trLeftTree.clearSelection();

		DefaultMutableTreeNode aDMTNode = null;
		
		// item.length will be 12
		// cntr.length will be 10
		// only search right tree if we have a cntr.
		if ( findValue.length() < 12 )
			aDMTNode = getNode(findValue);

		if (aDMTNode == null) {
			// if nothing is found in the right tree, let's search the left tree
			// for the value.
			collapseTree(this.trRightTree);
			collapseTree(this.trLeftTree);
			DefaultMutableTreeNode[] fndNodes = findNodeInTree(
					(DefaultMutableTreeNode) this.fieldLeftTreeModel.getRoot(), findValue);

			for (int i = 0; i < fndNodes.length; i++) {
				TreePath aNode = null;
				aNode = new TreePath(fndNodes[i].getPath());
				System.out.println("aNode: "+aNode.toString()); //$NON-NLS-1$
				this.trLeftTree.makeVisible(new TreePath(fndNodes[i].getPath()));
				this.trLeftTree.setSelectionPath(new TreePath(fndNodes[i].getPath()));
			}
			if ( fndNodes.length > 0 )
				retval = true;
			else
				retval = false;  // nothing found.
		} else {
			collapseTree(this.trRightTree);
			collapseTree(this.trLeftTree);

			MFSEntityMergeCntrUO aCntrUO = (MFSEntityMergeCntrUO) aDMTNode.getUserObject();
			this.trRightTree.setSelectionPath(new TreePath(aDMTNode.getPath()));
			this.taNodeInfo.setText(aCntrUO.toString());

			if (!aCntrUO.isEntityContainer()) {
				DefaultMutableTreeNode[] fndNodes = findNodeInTree(
					(DefaultMutableTreeNode) this.fieldLeftTreeModel.getRoot(), aCntrUO.getCntr());

				for (int i = 0; i < fndNodes.length; i++) {
					this.trLeftTree.makeVisible(new TreePath(fndNodes[i].getPath()));
				}
			}

			for (Enumeration e = aDMTNode.children(); e.hasMoreElements();) {
				DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) e.nextElement();
				
				MFSEntityMergeCntrUO childObj = (MFSEntityMergeCntrUO) childNode.getUserObject();
				
				DefaultMutableTreeNode[] fndNodes = findNodeInTree(
					(DefaultMutableTreeNode) this.fieldLeftTreeModel.getRoot(), childObj.getCntr());
				
				for (int i = 0; i < fndNodes.length; i++) {
					this.trLeftTree.makeVisible(new TreePath(fndNodes[i].getPath()));
				}
			}
		}
		return retval;
	}

	/**
	 * Searches through the tree and returns the nodes that match.
	 * @param root root of (sub)tree to search
	 * @param cntr container number
	 * @return array of nodes that match, empty array if nothing found
	 */
	@SuppressWarnings("rawtypes")
	private DefaultMutableTreeNode[] findNodeInTree(DefaultMutableTreeNode root, String cntr) {
		ArrayList<DefaultMutableTreeNode> retval                     = new ArrayList<DefaultMutableTreeNode>();
		DefaultMutableTreeNode[] retvalArray = new DefaultMutableTreeNode[0];
		DefaultMutableTreeNode thisNode      = null;
		Object obj = null;

		if (cntr != null) {
			Enumeration nodes = root.depthFirstEnumeration();
			while (nodes.hasMoreElements()) {
				thisNode = (DefaultMutableTreeNode) nodes.nextElement();
				obj = thisNode.getUserObject();
				if (obj instanceof MFSEntityMergeCntrUO) {
					if (((MFSEntityMergeCntrUO) obj).getCntr().equals(cntr)) {
						retval.add(thisNode);
					}
				}
				if (obj instanceof MFSEntityMergeMfgnUO) {
					if (((MFSEntityMergeMfgnUO) obj).getItem().equals(cntr)) {
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

	/** ~20A
	 * Find all completed entity containers
	 * @return true if all the entity containers are completed else false
	 */
	@SuppressWarnings("rawtypes")
	public boolean findAllEntityCntrsCompletedInTree() 
	{
		boolean AllEntityCntrCompletedFound = true;
		DefaultMutableTreeNode root = (DefaultMutableTreeNode)this.fieldRightTreeModel.getRoot();
		DefaultMutableTreeNode thisNode = null;
		Object obj = null;
		
		System.out.println("Starting findAllEntityCntrsCompletedInTree()"); //$NON-NLS-1$

		// Get the tree nodes
		Enumeration nodes = root.depthFirstEnumeration();
		
		// Loop the tree nodes
		while (nodes.hasMoreElements()) 
		{
			// Get the current node 
			thisNode = (DefaultMutableTreeNode) nodes.nextElement();
			
			// Create the current node object 
			obj = thisNode.getUserObject();
			
			if (obj instanceof MFSEntityMergeCntrUO) 
			{

				// Entity Conitainer
				if (((MFSEntityMergeCntrUO) obj).getType() == 'E')
				{
					
					// if IS NOT a completed container then 
					if (((MFSEntityMergeCntrUO) obj).getStatus() != 'C') 
					{
						return false;
					}// if (!((MFSEntityMergeCntrUO) obj).getStatusText().equalsIgnoreCase("C"))
				}
			}// if (obj instanceof MFSEntityMergeCntrUO)
		}// while (nodes.hasMoreElements())
		return AllEntityCntrCompletedFound;
	}
	
	//~6A	
	/**
	 * Returns the <code>cntr</code> who made this MFSEntityMergePanel to be displayed.
	 * @return cntr the container <code>cntr</code>.
	 */	
	public String getCntr()
	{
		return fieldCntr;
	}
	

	/**
	 * Prompts the user for a container number.
	 * @param buttonText the text for the proceed button
	 * @param buttonMnem the mnemonic for that button
	 * @return the container number as a <code>String</code>
	 * @throws IGSException if an error occurs
	 */
	private String getContainerNumber(String title, String text, String buttonText, char buttonMnem, int dataTypeSize)
		throws IGSException {
		String retval = null;

		MFSGetValueDialog gvd = new MFSGetValueDialog(this.getParentFrame(), 
			title, text, buttonText, buttonMnem);

		MFStfParser parser = null;                                         /* ~23A*/
		
		/* Use a different tf Parser for Pallet ID/MFGN and CNTR              ~23A*/
		if (dataTypeSize == PROMT_DIALOG_TYPE_SIZE_MFGN_PALLETID)
		{
			parser = new MFStfParser.MFStfPalletParser(this);
		}
		else
		{
			parser = new MFStfParser.MFStfCntrParser(this);
		}
			
		gvd.setTextParser(parser);
		TreePath defPath = this.trRightTree.getSelectionPath();
	
		if ((dataTypeSize != PROMT_DIALOG_TYPE_SIZE_MFGN_PALLETID) && (defPath != null) && (defPath.getPathCount() > 1))  // ~23C
		{
			DefaultMutableTreeNode defNode = (DefaultMutableTreeNode) defPath.getLastPathComponent();
			MFSEntityMergeCntrUO defObj = (MFSEntityMergeCntrUO) defNode.getUserObject();
			gvd.setDefaultValue(defObj.getCntr());
		}
		gvd.setLocationRelativeTo(getParentFrame());
		gvd.setVisible(true);
		
		if (gvd.getProceedSelected()) {
			retval = gvd.getInputValue();
			if (retval.length() == 0) { /* make future checks easier */
				retval = null;
			}
		}
        
        if (retval!=null)
        {
            int retvalLength = retval.length();
    		if (dataTypeSize!=0 && retval!=null && (retvalLength>dataTypeSize || retval.substring(0, 6).equals("000000")
    				                                                          || retval.substring(0, 4).equals("0000"))) 
    			// ~23C Add condition to handle MFGN returned value "000000"
    			// ~25C Add condition to handle MFGN returned value "0000" when a '1Y' barcode was specified on entry value
    		{
    			/*  ~23A When a MFGN is specified on the Dialog, we should remove "000000" at the beginning
    			 *  ~25A When a '1Y' barcode is specified together with a MFGN, we should remove "0000" and '1Y' from the input
    			 */
                if(retval.substring(0, 6).equals("000000") || retval.substring(0, 4).equals("0000"))
                {
                	retval = retval.substring(retvalLength-7, retvalLength);
                }
                else
                {
                	retval = retval.substring(retvalLength-dataTypeSize, retvalLength);	
                }
            }
        }
		return retval;
	}
    
	
    /**
	 * Prompts the user for a container or part number.
	 * @param buttonText1 the text for the proceed button
	 * @param buttonText2 the text for the proceed button
	 * @param buttonMnem the mnemonic for that button
	 * @return the container number as a <code>String</code>
	 * @throws IGSException if an error occurs
	 */
	private String getContainerOrPartNumber(String title, String text1, String text2, String buttonText, 
		char buttonMnem, int dataSize) throws IGSException {
		System.out.println("Starting getContainerOrPartNumber()"); //$NON-NLS-1$
		
		String retval = null;

		MFSGetValuesDialog aGetValuesDialog = new MFSGetValuesDialog(this.getParentFrame(), 
			title, text1, text2, buttonText, buttonMnem);

		MFStfParser parser1 = new MFStfParser.MFStfCntrParser(this);
		MFStfParser parser2 = new MFStfParser.MFStfItemParser(this);

		aGetValuesDialog.setTextParser1(parser1);
		aGetValuesDialog.setTextParser2(parser2);

		// set default input values to the right node selected(if one is selected).
		TreePath aRightTreePath = this.trRightTree.getSelectionPath();
		if ((aRightTreePath != null) && (aRightTreePath.getPathCount() > 1)) {
			DefaultMutableTreeNode aDMTNode  = (DefaultMutableTreeNode) aRightTreePath.getLastPathComponent();
			Object obj = null;
			obj = aDMTNode.getUserObject();
			MFSEntityMergeCntrUO aCntrUO    = null;
			if (obj instanceof MFSEntityMergeCntrUO) {
				aCntrUO    = (MFSEntityMergeCntrUO) aDMTNode.getUserObject();
				aGetValuesDialog.setDefaultValue1(aCntrUO.getCntr());
			}
		}

		// set default input values to the right node selected(if one is selected).
		TreePath aLeftTreePath = this.trLeftTree.getSelectionPath();
		if ((aLeftTreePath != null) && (aLeftTreePath.getPathCount() > 1)) {
			DefaultMutableTreeNode aDMTNode  = (DefaultMutableTreeNode) aLeftTreePath.getLastPathComponent();
			Object obj = null;
			obj = aDMTNode.getUserObject();
			MFSEntityMergeMfgnUO aMfgnUO    = null;
			if (obj instanceof MFSEntityMergeMfgnUO) {
				aMfgnUO    = (MFSEntityMergeMfgnUO) aDMTNode.getUserObject();
				aGetValuesDialog.setDefaultValue2(aMfgnUO.getItem());
			}
		}

		
		
		// Display the search popup window
		aGetValuesDialog.setLocationRelativeTo(getParentFrame());
		aGetValuesDialog.setVisible(true);
		
		if (aGetValuesDialog.getProceedSelected()) {
			retval = aGetValuesDialog.getInputValue1();
			if (retval.length() == 0) {
				retval = aGetValuesDialog.getInputValue2();
			} 
			if (retval.length() == 0) {
				retval = null;
			}
		}
        
		if (dataSize!=0 && retval!=null && retval.length()>dataSize) {
            retval = retval.substring(retval.length()-dataSize-1, dataSize);
        }
		return retval;
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
	public DefaultMutableTreeNode getNode(String cntrNum) {
		DefaultMutableTreeNode aDMTN = null;
		if (cntrNum != null && cntrNum.length() > 0) {
			aDMTN = (DefaultMutableTreeNode) this.fieldNodeMap.get(cntrNum);
			if (aDMTN == null) {
				aDMTN = (DefaultMutableTreeNode) this.MfgnNodeMap.get(cntrNum);
				if (aDMTN == null) {
					String title = "Container Not Found"; //$NON-NLS-1$
					String erms  = "Container {0} is not in the current container tree."; //$NON-NLS-1$
					String arguments[] = {cntrNum};
					erms = MessageFormat.format(erms, (Object[])arguments);
					IGSMessageBox.showOkMB(getParentFrame(), title, erms, null);
				}
			} else {
				System.out.println("getNode: "+aDMTN.toString()); //$NON-NLS-1$
			}
		}
		return aDMTN;
	}
	//~6A
	/**
	 * Returns the <code>opNmbr</code> for the specified container.
	 * @return the operation number <code>opNmbr</code> for the container.
	 */	
	public String getOpNmbr()
	{
		return opNmbr;
	}
	/**
	 * Returns the <code>MFSEntityMergeShpnUO</code> for the trees.
	 * @return the <code>MFSEntityMergeShpnUO</code>
	 */
	private MFSEntityMergeShpnUO getShpnUO()
	{
		Object obj = this.trLeftTree.getModel().getRoot();
		obj = ((DefaultMutableTreeNode) obj).getUserObject();
		return (MFSEntityMergeShpnUO) obj;
	}
	
	/**    ~18A
	 * Returns the <code>MFSEntityMergeShpnUO</code> for the trees.
	 * @return the <code>MFSEntityMergeShpnUO</code>
	 */
	private MFSEntityMergeShpnUO getRightShpnUO()
	{
		Object obj = this.trRightTree.getModel().getRoot();
		obj = ((DefaultMutableTreeNode) obj).getUserObject();
		return (MFSEntityMergeShpnUO) obj;
	}
	
	/**
	 * Checks to see if there is an empty entity container in the subtree rooted
	 * at the node passed in. Subtree must only contain container nodes.
	 * @param checkNode the root of the subtree to check
	 * @return <code>true</code> if a leaf of the subtree is an entity
	 *         container.
	 */
	@SuppressWarnings("rawtypes")
	private boolean hasEmptyEntityContainer(DefaultMutableTreeNode checkNode)
	{
		boolean retval = false;
		Enumeration checkKids = checkNode.depthFirstEnumeration();
		while (!retval && checkKids.hasMoreElements())
		{
			DefaultMutableTreeNode kidNode = (DefaultMutableTreeNode) checkKids.nextElement();
			if (kidNode.isLeaf())
			{
				MFSEntityMergeCntrUO kidObj = (MFSEntityMergeCntrUO) kidNode.getUserObject();
				if (kidObj.isEntityContainer())
				{
					retval = true;
					this.trRightTree.makeVisible(new TreePath(kidNode.getPath()));
				}
			}
		}
		return retval;
	}

	/**
	 * Checks to see if there is a container in the subtree rooted at the node
	 * passed in that is in a shipped status.
	 * @param checkNode the root of the subtree to check
	 * @return true if a container in a subtree is in shipped status.
	 */
	@SuppressWarnings("rawtypes")
	private boolean hasShippedContainer(DefaultMutableTreeNode checkNode)
	{
		boolean retval = false;
		Enumeration checkKids = checkNode.depthFirstEnumeration();
		while (!retval && checkKids.hasMoreElements())
		{
			DefaultMutableTreeNode kidNode = (DefaultMutableTreeNode) checkKids.nextElement();
			Object kidObj = kidNode.getUserObject();
			if (kidObj instanceof MFSEntityMergeCntrUO)
			{
				MFSEntityMergeCntrUO cntrObj = (MFSEntityMergeCntrUO) kidObj;
				retval = (cntrObj.getStatus() == MFSEntityMergeCntrUO.STATUS_SHIP);
			}
		}
		return retval;
	}

	/**
	 * Returns <code>true</code> if the node represents an empty entity
	 * container.
	 * @param node the <code>DefaultMutableTreeNode</code> for a container
	 * @return <code>true</code> if the node represents an empty entity
	 *         container
	 */
	private boolean isEmptyEntityContainer(DefaultMutableTreeNode node)
	{
		boolean result = false;
		MFSEntityMergeCntrUO containerUO = (MFSEntityMergeCntrUO) node.getUserObject();
		if (node.isLeaf() && containerUO.isEntityContainer())
		{
			result = true;
			this.trRightTree.makeVisible(new TreePath(node.getPath()));
		}
		return result;
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
			if (source instanceof MFSMenuButton)
			{
				MFSMenuButton button = (MFSMenuButton) source;
				button.requestFocusInWindow();
				button.doClick();
			}
		}
		else if (keyCode == KeyEvent.VK_F1)
		{
			if (ke.isShiftDown())             
			{
				this.pbCollapse.requestFocusInWindow();
				this.pbCollapse.doClick();
			}
		}
		else if (keyCode == KeyEvent.VK_F2)
		{
            if (ke.isShiftDown())
            {
                this.pbApply.requestFocusInWindow();
                this.pbApply.doClick();
            }
            else
            {
                this.pbFind.requestFocusInWindow();
                this.pbFind.doClick();
            }
		}
		else if (keyCode == KeyEvent.VK_F3)
		{
			if (this.fieldMode == MERGING)
			{
				this.pbEnd.requestFocusInWindow();
				this.pbEnd.doClick();
			}
			else
			{
				this.pbExit.requestFocusInWindow();
				this.pbExit.doClick();
			}
		}
		else if (keyCode == KeyEvent.VK_F4)
		{
			this.pbDelete.requestFocusInWindow();
			this.pbDelete.doClick();
		}
		else if (keyCode == KeyEvent.VK_F5)
		{
			this.pbMerge.requestFocusInWindow();
			this.pbMerge.doClick();
		}
		else if (keyCode == KeyEvent.VK_F6)
		{
			this.pbUnmerge.requestFocusInWindow();
			this.pbUnmerge.doClick();
		}
		else if (keyCode == KeyEvent.VK_F7)
		{
			this.pbSuspend.requestFocusInWindow();
			this.pbSuspend.doClick();
		}
		else if (keyCode == KeyEvent.VK_F8)
		{
			this.pbPrint.requestFocusInWindow();
			this.pbPrint.doClick();
		}
		else if (keyCode == KeyEvent.VK_F9)
		{
			this.pbNew.requestFocusInWindow();
			this.pbNew.doClick();
		}
		else if (keyCode == KeyEvent.VK_F10)
		{
			this.pbComplete.requestFocusInWindow();
			this.pbComplete.doClick();
		}
		else if (keyCode == KeyEvent.VK_F11)
		{
			this.pbRemove.requestFocusInWindow();
			this.pbRemove.doClick();
		}
		else if (keyCode == KeyEvent.VK_F12)
		{
			this.pbExpand.requestFocusInWindow();
			this.pbExpand.doClick();
		}
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
			PipedInputStream in                  = tran.setupPipedStreams("NSHPN"); //$NON-NLS-1$
			MFSEntityMergeParseStrategy strategy = new MFSEntityMergeParseStrategy();
			Map MapforParsing                    = new HashMap((int) (this.fieldNodeMap.size() * 1.35));
//~9 following line i think can be removed 			
			Map MfgnMapforParsing                = new HashMap((int) (this.fieldNodeMap.size() * 1.35));
			IGSXMLTreeHandler handler            = new IGSXMLTreeHandler(in, strategy,
					"cntr", "cntr", MapforParsing); //$NON-NLS-1$ //$NON-NLS-2$
			System.out.println("MfgnMapforParsing.size(): "+MfgnMapforParsing.size()); //$NON-NLS-1$

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
				this.fieldNodeMap = MapforParsing;
				DefaultMutableTreeNode node = handler.getRootNode();

				if(node != null) {
					DefaultMutableTreeNode right = (DefaultMutableTreeNode) node.getFirstChild();
					if(right != null) {
						right.removeFromParent();
						this.fieldRightTreeModel.setRoot(right);
						this.trRightTree.expandRow(0);						
					}
					DefaultMutableTreeNode left = (DefaultMutableTreeNode) node.getFirstChild();
					if(left != null) {
						((MFSEntityMergeShpnUO)left.getUserObject()).setSchd(((MFSEntityMergeShpnUO)right.getUserObject()).getSchd());
						left.removeFromParent();
						this.fieldLeftTreeModel.setRoot(left);
						checkPendingAlters(left.children());
						this.trLeftTree.expandRow(0);					
					}					
					checkIfItemHasMesAndBoxOrders();
					cleanupMFGNVectors();					/*~17A*/
	
					MFSEntityMergeShpnUO shpnUO = getShpnUO();
					System.out.println("makeServerCallWithReset - Ship Date: " + shpnUO.getSchd()); //$NON-NLS-1$
					System.out.println("makeServerCallWithReset - Entity   : " + shpnUO.getShpn()); //$NON-NLS-1$
					this.lblSchd.setText("  Ship Date: " + shpnUO.getSchd()); //$NON-NLS-1$
					this.lblShpn.setText("  Entity: " + shpnUO.getShpn()); //$NON-NLS-1$
				}				
			}
		} catch (IOException e) {
			throw new IGSException("Error setting up streams.", e, true); //$NON-NLS-1$
		}
	}

	
	/**
	 * If this is a container and it's not already set, set it and its parents
	 * to alter pending.
	 * @param checkObj the User Object to check
	 */
	private void markAlterPending(Object checkObj)
	{
		if (checkObj instanceof MFSEntityMergeCntrUO)
		{
			MFSEntityMergeCntrUO cntrObj = (MFSEntityMergeCntrUO) checkObj;
			if (cntrObj.getStatus() != MFSEntityMergeCntrUO.STATUS_ALTER_PENDING)
			{
				cntrObj.setStatus(MFSEntityMergeCntrUO.STATUS_ALTER_PENDING);
				DefaultMutableTreeNode cntrNode = getNode(cntrObj.getCntr());
				DefaultMutableTreeNode prntNode = (DefaultMutableTreeNode) cntrNode.getParent();
				markAlterPending(prntNode.getUserObject());
			}
		}
	}

	/**
	 * Merges one or more containers into a parent container.
	 * <p>
	 * Invoked when {@link #pbMerge} is selected.
	 */
	private void mergeContainer()
	{
		try
		{
			removeMyListeners();
			
			// ~10 automatically choose the container selected
			//String cntrNum = getContainerNumber("Enter the container value", "CNTR", "Get Parent", 'p', 0);
			String cntrNum = null;
			TreePath defPath = this.trRightTree.getSelectionPath();
			if ((defPath != null) && (defPath.getPathCount() > 1)) {
				DefaultMutableTreeNode defNode = (DefaultMutableTreeNode) defPath.getLastPathComponent();
				MFSEntityMergeCntrUO defObj = (MFSEntityMergeCntrUO) defNode.getUserObject();
				cntrNum = defObj.getCntr();
			}
			// ~10 End
			
			DefaultMutableTreeNode cntrNode = getNode(cntrNum);
			if (cntrNode != null)
			{
				String erms = null;
				MFSEntityMergeCntrUO cntrUO = (MFSEntityMergeCntrUO) cntrNode.getUserObject();
				if (!cntrUO.isEditable())
				{
					erms = "Container {0} cannot be merged into because it is not editable."; //$NON-NLS-1$
				}
				else if (cntrUO.getStatus() != MFSEntityMergeCntrUO.STATUS_READY)
				{
					erms = "Container {0} cannot be merged into because {1}."; //$NON-NLS-1$
				}
				else
				{
					findNode(cntrNum);
					mergeContainer(cntrNum, cntrNode);
				}
				if (erms != null)
				{
					String title = "Merge Error"; //$NON-NLS-1$
					String arguments[] = {
							cntrNum, cntrUO.getStatusText()
					};
					erms = MessageFormat.format(erms, (Object[])arguments);
					IGSMessageBox.showOkMB(getParentFrame(), title, erms, null);
				}
			}
		}
		catch (Exception e)
		{
			String title = "Merge Error"; //$NON-NLS-1$
			IGSMessageBox.showOkMB(getParentFrame(), title, null, e);
		}
		finally
		{
			addMyListeners();
		}
	}

	/**
	 * Merges one or more containers into a parent container. Prompts for the
	 * containers to merge, validates the containers, and then calls UPDT_ENT.
	 * @param parentNum the container number for the parent container
	 * @param parentNode the tree node for the parent container
	 * @throws IGSException if an error occurs
	 */
	@SuppressWarnings("rawtypes")
	private void mergeContainer(String parentNum, DefaultMutableTreeNode parentNode)
		throws IGSException
	{
		boolean auditsPassed = false;
		MFSGetMultiValueDialog gmvd = new MFSGetMultiValueDialog(getParentFrame(),
				"Containers to merge with " + parentNum, "Child container", "Merge", 'm'); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		gmvd.setVisibleRowCount(10);
		MFStfParser parser = new MFStfParser.MFStfCntrParser(this); 		
		gmvd.setTextParser(parser);
		gmvd.setLocationRelativeTo(getParentFrame());
		gmvd.pack();
		gmvd.setVisible(true);
		while (gmvd.getProceedSelected() && !auditsPassed)
		{
			auditsPassed = true;
			Enumeration kids = gmvd.getInputValues();
			while (kids.hasMoreElements())
			{
				String childNum = (String) kids.nextElement();
				DefaultMutableTreeNode childNode = getNode(childNum);
				String erms = null;
				String arguments[] = {
					childNum
				};
				if (childNode == null)
				{
					auditsPassed = false;
				}
				else if (childNode.isNodeDescendant(parentNode))
				{
					erms = "Container {0} cannot be merged into its own sub container."; //$NON-NLS-1$
				}
				else
				{
					MFSEntityMergeCntrUO childObj = (MFSEntityMergeCntrUO) childNode.getUserObject();
					if (childObj.getStatus() != MFSEntityMergeCntrUO.STATUS_READY)
					{
						erms = "Container {0} cannot be merged because {1}."; //$NON-NLS-1$
						arguments = new String[] {
								childNum, childObj.getStatusText()
						};
					}
					else if (childObj.isMergedContainer())
					{
						erms = "Container {0} is already merged."; //$NON-NLS-1$
					}
				}

				if (erms != null)
				{
					auditsPassed = false;
					String title = "Invalid Container for Merge"; //$NON-NLS-1$
					erms = MessageFormat.format(erms, (Object[])arguments);
					IGSMessageBox.showOkMB(getParentFrame(), title, erms, null);
				}
			}
			if (!auditsPassed)
			{
				gmvd.pack();
				gmvd.setVisible(true);
			}
		}
		if (gmvd.getProceedSelected())
		{
			Enumeration cntrs = gmvd.getInputValues();
			if (cntrs != null)
			{
				updtEntity(UPDT_MERGE, "Merging Containers...", parentNum, cntrs); //$NON-NLS-1$
				String title = "Print Container"; //$NON-NLS-1$
				String msg = "Do you want to print a label for container " + parentNum //$NON-NLS-1$
						+ "?"; //$NON-NLS-1$
				if (IGSMessageBox.showYesNoMB(getParentFrame(), title, msg, null))
				{
					MFSPrintingMethods.caseContent(parentNum, null, 1, getParentFrame()); // ~3C
					
					MFSEntityMergeCntrUO cntrUO = (MFSEntityMergeCntrUO) parentNode.getUserObject(); //~12A
					vfyHazmatLabeling(parentNum, cntrUO.getType()); //~12A
				}
			}
		}
	}

	
	/**
	 * Creates a new container and displays the merge dialog so the user can
	 * merge one or more containers into the new container.
	 * @param calledFrom Identifier to know if the call is when a Pallet ID was specified  ~23A
	 * <p>
	 * Invoked when {@link #pbNew} is selected.
	 */
	private void newContainer(String calledFrom)                /*~23A*/
	{
		try
		{
			removeMyListeners();
			updtEntity(UPDT_NEW, "Creating New Container...", null, null); //$NON-NLS-1$
			String newCntr = getShpnUO().getSctr();
			
			if(newCntr == null)									/*~18A*/
			{													/*~18A*/
				newCntr = getRightShpnUO().getSctr();			/*~18A*/
			}													/*~18A*/
			
			/* If calledFrom equals to PALLETID then mergeContainer must not be called ~23C*/
			if (((newCntr != null) && (newCntr.length() > 0)) && !calledFrom.equals("PALLETID"))
			{
				findNode(newCntr);
				mergeContainer(newCntr, getNode(newCntr));
				findNode(newCntr);
			}
		}
		catch (Exception e)
		{
			String title = "New Container Error"; //$NON-NLS-1$
			IGSMessageBox.showOkMB(getParentFrame(), title, null, e);
		}
		finally
		{
			addMyListeners();
		}
	}

	/**
	 * Prints a label for a container.
	 * <p>
	 * Invoked when {@link #pbPrint} is selected.
	 */
	private void printContainerLabel()
	{
		try
		{
			removeMyListeners();
			String cntrNum = getContainerNumber("Enter the container value", "CNTR", "Print", 'p', PROMT_DIALOG_TYPE_SIZE_CNTR); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			DefaultMutableTreeNode cntrNode = getNode(cntrNum);
			if (cntrNode != null)
			{
				String erms = null;
				if (isEmptyEntityContainer(cntrNode))
				{
					erms = "Label cannot be printed because container {0} is an empty entity container."; //$NON-NLS-1$
				}
				else if (hasEmptyEntityContainer(cntrNode))
				{
					erms = "Label cannot be printed because container {0} contains an empty entity container."; //$NON-NLS-1$
				}
				else
				{
					MFSPrintingMethods.caseContent(cntrNum, null, 1, getParentFrame()); // ~3C
				}

				if (erms != null)
				{
					String title = "Print Container Label Error"; //$NON-NLS-1$
					String arguments[] = {
						cntrNum
					};
					erms = MessageFormat.format(erms, (Object[])arguments);
					IGSMessageBox.showOkMB(getParentFrame(), title, erms, null);
				}
			}
		}
		catch (Exception e)
		{
			String title = "Print Container Label Error"; //$NON-NLS-1$
			IGSMessageBox.showOkMB(getParentFrame(), title, null, e);
		}
		finally
		{
			addMyListeners();
		}
	}

	/**
	 * Removes one or more MFGNs from the entity. A dialog lists all MFGNs
	 * currently in the entity and allows the user to choose one or more to
	 * remove. If a selection is made, RPL_ENT is called with the selected list
	 * of MFGNs.
	 * <p>
	 * Invoked when {@link #pbRemove} is selected.
	 */
	private void removeFromEntity()
	{
		String[] mfgnIdss = new String[2];
		try
		{
			removeMyListeners();
			MFSGenericListDialog getMfgns = new MFSGenericListDialog(getParentFrame(),
					"Available MFGN Selection", "Select MFGN(s) to remove"); //$NON-NLS-1$ //$NON-NLS-2$
			getMfgns.setSizeSmall();
			getMfgns.setMultipleSelection(true);
			getMfgns.loadAnswerListModel(createMfgnIdssString(), 12);
			getMfgns.setLocationRelativeTo(getParentFrame());
			getMfgns.setVisible(true);
			if (getMfgns.getProceedSelected())
			{
				String[] selectedMfgns = getMfgns.getSelectedListOptions();
				IGSXMLTransaction rplEnt = new IGSXMLTransaction("RPL_ENT"); //$NON-NLS-1$
				rplEnt.setActionMessage("Updating Entity Info..."); //$NON-NLS-1$
				rplEnt.startDocument();
				rplEnt.addElement("SHPN", getShpnUO().getShpn()); //$NON-NLS-1$
				rplEnt.addElement("USER", MFSConfig.getInstance().get8CharUser()); //$NON-NLS-1$
				for (int i = 0; i < selectedMfgns.length; i++)
				{
					mfgnIdss = selectedMfgns[i].split("/"); //$NON-NLS-1$
					rplEnt.startElement("RCD"); //$NON-NLS-1$
					rplEnt.addElement("MFGN", mfgnIdss[0]); //$NON-NLS-1$
					rplEnt.addElement("IDSS", mfgnIdss[1]); //$NON-NLS-1$
					rplEnt.endElement("RCD"); //$NON-NLS-1$
				}
				rplEnt.endDocument();
				makeServerCallWithReset(rplEnt, this);
				String trc;
				if ((trc = rplEnt.getFirstElement("TRC")) != null) //$NON-NLS-1$
				{
					int rc = Integer.parseInt(trc);
					if (rc == 111)
					{
						rplEnt.resetCurrentBoundary();
						MFSDialog dialog = new MFSTiedContainerDialog(this, rplEnt);
						dialog.setLocationRelativeTo(getParentFrame());
						dialog.setVisible(true);
					}
					else if (rc != 0)
					{
						String title = "Remove from Entity Error"; //$NON-NLS-1$
						String erms = rplEnt.getNextElement("TERMS"); //$NON-NLS-1$
						IGSMessageBox.showOkMB(getParentFrame(), title, erms, null);
					}
				}
			}
		}
		catch (Exception e)
		{
			String title = "Remove from Entity Error"; //$NON-NLS-1$
			IGSMessageBox.showOkMB(getParentFrame(), title, null, e);
		}
		finally
		{
			addMyListeners();
		}
	}

	
	/** Removes the listeners from this panel's <code>Component</code>s. */
	protected void removeMyListeners()
	{
		this.removeKeyListener(this);
		this.spLeftTree.removeKeyListener(this);
		this.trLeftTree.removeKeyListener(this);
		this.spRightTree.removeKeyListener(this);
		this.trRightTree.removeKeyListener(this);
		this.pnlButtons.removeKeyListener(this);
		this.spNodeInfo.removeKeyListener(this);
		this.taNodeInfo.removeKeyListener(this);
		this.trLeftTree.removeTreeSelectionListener(this);
		this.trRightTree.removeTreeSelectionListener(this);

		this.fieldButtonIterator.reset();
		while (this.fieldButtonIterator.hasNext())
		{
			JButton next = this.fieldButtonIterator.nextMenuButton();
			next.removeActionListener(this);
			next.removeKeyListener(this);
		}
	}

	/**
	 * ~7 Retrieve the container number from a dialog and call method
     * to retrieve data. 
	 * @param actionable the <code>MFSActionable</code> to update
	 * @return <code>true</code> if the panel should be displayed
	 * @throws IGSException if an error occurs
	 */
	public boolean retrieveContainerInfo(MFSActionable actionable) throws IGSException {
		boolean retval = false;
		String cntr = getContainerNumber("Enter the container value", "CNTR", "Enter", 'e', PROMT_DIALOG_TYPE_SIZE_CNTR); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		if (cntr != null) {
            retval = retrieveContainerInfo(actionable, cntr, ""); //$NON-NLS-1$
		}
		return retval;
	}

	/**
     * ~7 Retrieve Entity information via call to STR_ENT
     * @param actionable the <code>MFSActionable</code> to update
     * @param cntr the container number
     * @param shpn the ship entity number
     * @return <code>true</code> if the panel should be displayed
     * @throws IGSException
     */
    public boolean retrieveContainerInfo(MFSActionable actionable, String cntr, String shpn)
        throws IGSException {
        boolean retval = false;
        
        final MFSConfig config = MFSConfig.getInstance();
        IGSXMLTransaction strEnt = new IGSXMLTransaction("STR_ENT"); //$NON-NLS-1$
        strEnt.setActionMessage("Retrieving entity information..."); //$NON-NLS-1$
        strEnt.startDocument();
        strEnt.addElement("TRAN", new Character(this.fieldMode)); //$NON-NLS-1$
        strEnt.addElement("CELL", config.get8CharCell()); //$NON-NLS-1$
        strEnt.addElement("CTYP", config.get8CharCellType()); //$NON-NLS-1$
        strEnt.addElement("USER", config.get8CharUser()); //$NON-NLS-1$
        strEnt.addElement("CNTR", cntr); //$NON-NLS-1$
        strEnt.addElement("SHPN", shpn); //$NON-NLS-1$
        strEnt.endDocument();
        makeServerCallWithReset(strEnt, actionable);
        opNmbr = strEnt.getFirstElement("NMBR"); //$NON-NLS-1$
        this.fieldCntr = cntr;  //~6A
        findNode(cntr); // ~10 - highlight default container
        retval = true;

        return retval;
    }

	
	/**
	 * Retrieve Locations Transaction.
	 * @param <code>HashTable</code> List of locations
	 * @return <code>0</code> if no errors, <code>Other than 0</code> if errored.
	 */
    private int rtvLocations(Vector<MFSLocation> locList)
    {
    	int rc = 0; 
    	
		IGSXMLTransaction rtvCtlocs = new IGSXMLTransaction("RTV_CTLOCS"); //$NON-NLS-1$
		rtvCtlocs.setActionMessage("Retrieving Valid Locations..."); //$NON-NLS-1$
		rtvCtlocs.startDocument();
		rtvCtlocs.addElement("CTYP", MFSConfig.getInstance().get8CharCellType()); //$NON-NLS-1$
		rtvCtlocs.addElement("SHPN", getShpnUO().getShpn()); //$NON-NLS-1$
		rtvCtlocs.endDocument();
		
		MFSComm.getInstance().execute(rtvCtlocs, this);
		rc = rtvCtlocs.getReturnCode();
		if (rc != 0)
		{
			String title = "Error Retrieving Locations"; //$NON-NLS-1$
			IGSMessageBox.showOkMB(getParentFrame(), title, rtvCtlocs.getErms(), null);
		}
		else
		{
			locList.clear();
			while (rtvCtlocs.stepIntoElement("RCD") != null) //$NON-NLS-1$
			{
				String area = rtvCtlocs.getFirstElement("AREA"); //$NON-NLS-1$
				String loc = rtvCtlocs.getNextElement("CLOC"); //$NON-NLS-1$
				String desc = rtvCtlocs.getNextElement("DS40"); //$NON-NLS-1$
				String shpn = rtvCtlocs.getNextElement("SHPN"); //$NON-NLS-1$
				MFSLocation thisLoc = new MFSLocation(area, 
													loc,  
													desc, 
													shpn); 
				locList.addElement(thisLoc);
				rtvCtlocs.stepOutOfElement();
			}
		}	
		return rc;
    }
	
	
	/**
	 * Submit the END_ENT server transaction.
	 * @param tx type of ending, 'C'omplete, 'E'nd, or 'S'uspend
	 * @param msg the action message for transaction
	 * @param cntr the container number for complete or <code>null</code>
	 * @throws IGSException if an error occurs
	 */
	private void submitEndEntity(char tx, String msg, String cntr)
		throws IGSException
	{
		IGSXMLTransaction endEnt = new IGSXMLTransaction("END_ENT"); //$NON-NLS-1$
		endEnt.setActionMessage(msg);
		endEnt.startDocument();
		endEnt.addElement("TRAN", new Character(tx)); //$NON-NLS-1$
		endEnt.addElement("CELL", MFSConfig.getInstance().get8CharCell()); //$NON-NLS-1$
		endEnt.addElement("CTYP", MFSConfig.getInstance().get8CharCellType()); //$NON-NLS-1$
		endEnt.addElement("USER", MFSConfig.getInstance().get8CharUser()); //$NON-NLS-1$
		endEnt.addElement("SHPN", getShpnUO().getShpn()); //$NON-NLS-1$
		if (tx == END_MODE_COMPLETE)
		{
			endEnt.addElement("CNTR", cntr); //$NON-NLS-1$
		}
		endEnt.endDocument();

		if (tx == END_MODE_COMPLETE || tx == END_MODE_END)   /* ~19C Refresh if errors showed */
		{
			makeServerCallWithReset(endEnt, this);
			String trc;
			if ((trc = endEnt.getFirstElement("TRC")) != null) //$NON-NLS-1$
			{
				if (Integer.parseInt(trc) != 0)
				{
					throw new IGSException(endEnt.getNextElement("TERMS"), false); //$NON-NLS-1$
				}
			}
		}
		else
		{
			MFSComm.getInstance().execute(endEnt, this);
			if (endEnt.getReturnCode() != 0)
			{
				throw new IGSTransactionException(endEnt, true);
			}
		}
	}	
	
	/**
	 * Suspends the ship entity by performing the following steps:
	 * <ol>
	 * <li>Updates the weight and dimensions of the ready top level containers.
	 * </li>
	 * <li>Updates the location of the ready top level containers.</li>
	 * <li>Prompts the user to select the containers for which a label should
	 * be printed.</li>
	 * <li>Calls END_ENT.</li>
	 * </ol>
	 * Invoked when {@link #pbSuspend} is selected.
	 */
	@SuppressWarnings("rawtypes")
	private void suspendEntity()
	{
		DefaultMutableTreeNode cntrNode;
		MFSEntityMergeCntrUO cntrUO = null;
		StringBuffer topCntrStr = new StringBuffer(); /* top level cntrs */
		boolean cancel = false;
		try
		{
			removeMyListeners();
			cntrNode = (DefaultMutableTreeNode) this.fieldRightTreeModel.getRoot();
			Enumeration topCntrs = cntrNode.children();
			List<String> topEntityCntrList = new ArrayList<String>(); //~12A
			while (topCntrs.hasMoreElements())
			{
				cntrNode = (DefaultMutableTreeNode) topCntrs.nextElement();
				cntrUO = (MFSEntityMergeCntrUO) cntrNode.getUserObject();

				if('E' == cntrUO.getType()) //~12A
				{
					topEntityCntrList.add(cntrUO.getCntr()); //~12A
				}
				
				if (cntrUO.getStatus() == MFSEntityMergeCntrUO.STATUS_READY)
				{
					topCntrStr.append(cntrUO.getCntr());
					if (!updateWeightDim(cntrUO.getCntr(), "S")) //$NON-NLS-1$
					{
						cancel = true;
						break;
					}
				}
			}
			
			vfyHazmatLabeling(topEntityCntrList); //~12A

			if (!cancel && updateLocation("S")) //$NON-NLS-1$
			{				
				/* ~10 Do not want option to print on a suspend
				if (topCntrStr.length() > 0)
				{
					MFSGenericListDialog prntCntrs = new MFSGenericListDialog(
							getParentFrame(), "Container List",
							"Select containers to print");
					prntCntrs.setSizeSmall();
					prntCntrs.setMultipleSelection(true);
					prntCntrs.loadAnswerListModel(topCntrStr.toString(), CNTR_SIZE);
					prntCntrs.setLocationRelativeTo(getParentFrame());
					prntCntrs.setVisible(true);
					if (prntCntrs.getProceedSelected())
					{
						String[] selectedCntrs = prntCntrs.getSelectedListOptions();
						for (int i = 0; i < selectedCntrs.length; i++)
						{
							MFSPrintingMethods.caseContent(selectedCntrs[i], null, 1,
									getParentFrame());  // ~3C
						}
					}
				}*/
				submitEndEntity(END_MODE_SUSPEND, "Suspending Entity...", null); //$NON-NLS-1$
				getParentFrame().restorePreviousScreen(this);
			}
			else
			{
				retrieveContainerInfo(this, this.fieldCntr, ""); //$NON-NLS-1$
			}
		}
		catch (Exception e)
		{
			String title = "Suspend Entity Error"; //$NON-NLS-1$
			IGSMessageBox.showOkMB(getParentFrame(), title, null, e);
		}
		finally
		{
			addMyListeners();
		}
	}

	/**
	 * Unmerges children containers from their parent containers. Prompts for
	 * the children containers to unmerge and then calls UPDT_ENT.
	 * <p>
	 * Invoked when {@link #pbUnmerge} is selected.
	 */
	@SuppressWarnings("rawtypes")
	private void unmergeContainer()
	{
		try
		{
			removeMyListeners();
			boolean auditsPassed = false;
			MFSGetMultiValueDialog gmvd = new MFSGetMultiValueDialog(getParentFrame(),
					"Containers to unmerge from parent", "Child container", "Unmerge", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					'u');
			gmvd.setVisibleRowCount(10);
			MFStfParser parser = new MFStfParser.MFStfCntrParser(this);		
			gmvd.setTextParser(parser);
			gmvd.setLocationRelativeTo(getParentFrame());
			gmvd.pack();
			gmvd.setVisible(true);
			while (gmvd.getProceedSelected() && !auditsPassed)
			{
				auditsPassed = true;
				Enumeration kids = gmvd.getInputValues();
				while (kids.hasMoreElements())
				{
					String childNum = (String) kids.nextElement();
					DefaultMutableTreeNode childNode = getNode(childNum);
					String erms = null;
					String arguments[] = {
						childNum
					};
					if (childNode == null)
					{
						auditsPassed = false;
					}
					else
					{
						/************************* ~1 *******************************/
						/* Audit #1: Ensure status is either Ready OR Alter Pending */
						/* Audit #2: Ensure container has been MERGED.              */
						/* Audit #3: Ensure parent is a ENTITY container            */
						/************************************************************/						
						boolean forceUnmerge = false; //~14A
						MFSEntityMergeCntrUO childObj = (MFSEntityMergeCntrUO) childNode.getUserObject();
						
						//***** ~14A START ******
						if (childObj.getStatus() == MFSEntityMergeCntrUO.STATUS_COMPLETE ||
								childObj.getStatus() == MFSEntityMergeCntrUO.STATUS_ACK_COMPLETE)
						{
							String message = "WARNING: Container " + childObj.getCntr() + " is completed.\nDo you want to proceed?"; //$NON-NLS-1$ //$NON-NLS-2$
							forceUnmerge = IGSMessageBox.showYesNoMB(getParentFrame(), "Container Unmerge", message, null); //$NON-NLS-1$
						}
						//***** ~14A END ******
						
						if (!forceUnmerge &&  // ~14A
								(childObj.getStatus() != MFSEntityMergeCntrUO.STATUS_READY) &&
							    (childObj.getStatus() != MFSEntityMergeCntrUO.STATUS_ALTER_PENDING)) //~1
						{
							erms = "Container {0} cannot be unmerged because {1}."; //$NON-NLS-1$
							arguments = new String[] {
									childNum, childObj.getStatusText()
							};
						}
						else if (!childObj.isMergedContainer())
						{
							erms = "Container {0} is not currently merged."; //$NON-NLS-1$
						}
						else
						{
							DefaultMutableTreeNode oldParent = (DefaultMutableTreeNode) childNode.getParent();
							MFSEntityMergeCntrUO oldParentObj = (MFSEntityMergeCntrUO) oldParent.getUserObject();
						    if ((oldParentObj.getType() != MFSEntityMergeCntrUO.TYPE_MERGED_ENTITY) &&       //~1
						    		(oldParentObj.getType() != MFSEntityMergeCntrUO.TYPE_ENTITY))        	 //~1
							{
								erms = "Container {0} cannot be unmerged. You can only unmerge from entity containers."; //$NON-NLS-1$
							}
						}
					}
					if (erms != null)
					{
						auditsPassed = false;
						String title = "Invalid Container for Merge"; //$NON-NLS-1$
						erms = MessageFormat.format(erms, (Object[])arguments);
						IGSMessageBox.showOkMB(getParentFrame(), title, erms, null);
					}
				}
				if (!auditsPassed)
				{
					gmvd.pack();
					gmvd.setVisible(true);
				}
			}
			if (gmvd.getProceedSelected())
			{
				Enumeration cntrs = gmvd.getInputValues();
				if (cntrs != null)
				{
					updtEntity(UPDT_UNMERGE, "Unmerging Containers...", null, cntrs); //$NON-NLS-1$
				}
			}
		}
		catch (Exception e)
		{
			String title = "Unmerge Error"; //$NON-NLS-1$
			IGSMessageBox.showOkMB(getParentFrame(), title, null, e);
		}
		finally
		{
			addMyListeners();
		}
	}

    /**
	 * Updates the location of the containers.
	 * @param endOrSuspendFlag "E" for end, "S" for suspend
	 * @return <code>true</code> if locations were updated, <code>false</code>
	 *         if cancelled or error
	 */
	@SuppressWarnings("rawtypes")
	private boolean updateLocation(String endOrSuspendFlag)
	{
		int rc = 0;
		boolean cancel = false;
		boolean clearAll = false;
		boolean clearAllrestarted = false;
		boolean locAllRemaining = false;
		String locAllArea = ""; //$NON-NLS-1$
		String locAllLoc = ""; //$NON-NLS-1$
		DefaultMutableTreeNode topCntrNode = null;
		DefaultMutableTreeNode cntrNode = null;
		MFSEntityMergeCntrUO cntrUO = null;		
		Enumeration topCntrs = null;
		final MFSConfig config = MFSConfig.getInstance();		
		Vector <MFSLocation>locList = new Vector <MFSLocation>();	// ~10;			
	    Vector <MFSEntityMergeCntrUO>updatedCntrs = new Vector <MFSEntityMergeCntrUO>(); // ~10;
	    
		if (config.containsNmbrPrlnFlagEntry("ENTITYLOC", this.opNmbr, "*ALL", endOrSuspendFlag) != null) //$NON-NLS-1$ //$NON-NLS-2$//~11C
		{
			topCntrNode = (DefaultMutableTreeNode) this.fieldRightTreeModel.getRoot();			
			rc = rtvLocations(locList);			
			if(rc == 0)
			{
				MFSEntityLocationDialog locDialog = new MFSEntityLocationDialog(getParentFrame());	
				locDialog.setLocationRelativeTo(getParentFrame());				
				do  /* Repeat Allocation if Clear All was selected */
				{				    
					clearAll = false;
					locAllRemaining = false;
					updatedCntrs.clear();					
					/* now loop through each container asking for a location if needed */
					topCntrs = topCntrNode.children();
					while (!cancel && !clearAll && topCntrs.hasMoreElements())
					{					 						
						cntrNode = (DefaultMutableTreeNode) topCntrs.nextElement();
						cntrUO = (MFSEntityMergeCntrUO) cntrNode.getUserObject();
	
						if ((cntrUO.getStatus() == MFSEntityMergeCntrUO.STATUS_READY) &&
							(cntrUO.getCloc().trim().equals("") || cntrUO.getArea().trim().equals(""))) //$NON-NLS-1$ //$NON-NLS-2$
						{							
							if (locAllRemaining)
							{	/* if locAll is true, the Loc All button was pressed and we want to set the rest of the locs to that value */
								cntrUO.setArea(locAllArea);
								cntrUO.setCloc(locAllLoc);
								
								updatedCntrs.addElement(cntrUO);
							}
							else
							{	// Ask for a loc
								MFSLocation cLoc = findDefaultAreaLoc(topCntrNode, locList);
							    if(cLoc != null)
							    {
							    	locDialog.loadLocationModel(locList, cLoc.getArea(), cLoc.getLocation());
							    }
							    else
							    {
							    	locDialog.loadLocationModel(locList, "", ""); //$NON-NLS-1$ //$NON-NLS-2$
							    }											
								
								locDialog.setCntr(cntrUO.getCntr());
								locDialog.setVisible(true);
								
								if (locDialog.getSelectedButton() == MFSEntityLocationDialog.PROCEED ||
									locDialog.getSelectedButton() == MFSEntityLocationDialog.LOC_ALL)
								{
									cntrUO.setArea(locDialog.getSelectedArea());
									cntrUO.setCloc(locDialog.getSelectedLoc());	
									
									updatedCntrs.addElement(cntrUO);
									/* Update the Selected Location in List to the Current Ship Entity */
									cLoc = findAreaInMFSLocs(locList, locDialog.getSelectedArea(),locDialog.getSelectedLoc());
									if(cLoc != null)
									{
										cLoc.setShpn(getShpnUO().getShpn());
									}																	
									if (locDialog.getSelectedButton() == MFSEntityLocationDialog.LOC_ALL)
									{   /* Save selected Area/Loc for the Remaining ones */
										locAllRemaining = true;
										locAllArea = locDialog.getSelectedArea();
										locAllLoc = locDialog.getSelectedLoc();
									}
								}
								else if (locDialog.getSelectedButton() == MFSEntityLocationDialog.CANCEL)
								{
									cancel = true;
									updatedCntrs.clear(); /* Reset Updated Containers */
									break; /* We exit the While Loop of the Containers */
								}
								else if (locDialog.getSelectedButton() == MFSEntityLocationDialog.CLEAR_ALL)
								{
									clearAll = true;
									clearAllrestarted = true;
									updatedCntrs.clear(); /* Reset Updated Containers */
									break; /* We exit the While Loop of the Containers */
								}
								else if(clearAllrestarted)/* do nothing if skip was pressed, unless you previously did a clear all */
								{
									updatedCntrs.addElement(cntrUO);									
								}								
							}
						}
					} /* While Loop for Containers */
					if(clearAll)
					{
						topCntrs = topCntrNode.children();							
						while (topCntrs.hasMoreElements())
						{
							cntrNode = (DefaultMutableTreeNode) topCntrs.nextElement();
							cntrUO = (MFSEntityMergeCntrUO) cntrNode.getUserObject();
							/* Update the Selected Location in List to the Current Ship Entity */								
							MFSLocation cLoc = findAreaInMFSLocs(locList, cntrUO.getArea(),cntrUO.getCloc());
							if(cLoc != null)
							{
								cLoc.setShpn(""); //$NON-NLS-1$
							}								
							cntrUO.setArea(" "); //$NON-NLS-1$
							cntrUO.setCloc(" "); //$NON-NLS-1$
						}   
					}
				}
				while (rc == 0 && !cancel && clearAll);
				/* Execute changes */
				if(!cancel && !updatedCntrs.isEmpty())
				{
					IGSXMLTransaction updCtloc = new IGSXMLTransaction("UPD_CTLOC"); //$NON-NLS-1$
					
					updCtloc.setActionMessage("Updating location info..."); //$NON-NLS-1$
					updCtloc.startDocument();
					updCtloc.addElement("SHPN", getShpnUO().getShpn()); //$NON-NLS-1$
					updCtloc.addElement("USER", MFSConfig.getInstance().get8CharUser()); //$NON-NLS-1$		
					
					Enumeration <MFSEntityMergeCntrUO>eUpdatedCntrs = updatedCntrs.elements();
					while(eUpdatedCntrs.hasMoreElements())
					{
						cntrUO = eUpdatedCntrs.nextElement();
						updCtloc.startElement("RCD"); //$NON-NLS-1$
						updCtloc.addElement("CNTR", cntrUO.getCntr()); //$NON-NLS-1$
						updCtloc.addElement("AREA", cntrUO.getArea()); //$NON-NLS-1$
						updCtloc.addElement("LOC", cntrUO.getCloc()); //$NON-NLS-1$
						updCtloc.endElement("RCD"); //$NON-NLS-1$
					}
					updCtloc.endDocument();
					MFSComm.getInstance().execute(updCtloc, this);
					rc = updCtloc.getReturnCode();
					if (rc != 0)
					{
						String title = "Error Updating Locations"; //$NON-NLS-1$
						IGSMessageBox.showOkMB(getParentFrame(), title, updCtloc.getErms(), null);
					}													
				}
			}
		}	
		/* ~10 Print Location Label */
		if(rc == 0 && !cancel && !updatedCntrs.isEmpty())
		{
			String value = config.getConfigValue("LOCLABEL");  //$NON-NLS-1$
			if(!value.equals(MFSConfig.NOT_FOUND))
			{
				MFSEntityLocationPrintDialog printLocDialog = new MFSEntityLocationPrintDialog(getParentFrame());
				printLocDialog.setLocationRelativeTo(getParentFrame());
				printLocDialog.setVisible(true);
	
				if (printLocDialog.getPressedOkay())
				{
					int qty = 1;
					try
					{
						qty = Integer.parseInt(value);
					}
					catch (NumberFormatException e)			
					{
						qty = 1;
					}					
					Enumeration <MFSEntityMergeCntrUO>eUpdatedCntrs = updatedCntrs.elements();
					Vector <String>locPrintedList = new Vector<String>();
					
					while(eUpdatedCntrs.hasMoreElements())
					{ 
						cntrUO = eUpdatedCntrs.nextElement();
						String lbldta = "<CNTR>"+cntrUO.getCntr()+"</CNTR>";  //$NON-NLS-1$ //$NON-NLS-2$
						if(printLocDialog.isOnePerLocSelected())  /* per Location */
						{		
							String cLoc = cntrUO.getArea() + cntrUO.getCloc();	
							if(locPrintedList.indexOf(cLoc) == -1) /* If Location is not already printed */
							{
								locPrintedList.addElement(cLoc);
								MFSPrintingMethods.printlabel("LOCLABEL", "SHIPENTITY", "",  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
																lbldta, "", qty, this.getParentFrame());//$NON-NLS-1$ 
							}
						}
						else  /* per Container */
						{
							MFSPrintingMethods.printlabel("LOCLABEL", "SHIPENTITY", "",  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
																lbldta, "", qty, this.getParentFrame());//$NON-NLS-1$ 
						}
					}
				}
			}
		}		
		return rc == 0 && cancel == false;
	}

	//~5C
	/**
	 * Updates the weight and dimensions for the container.
	 * @param cntr the container to updated
	 * @param endOrSuspendFlag "E" for end, "S" for suspend
	 * @return <code>true</code> if dimensions were updated,
	 *         <code>false</code> if cancelled or error
	 */
	private boolean updateWeightDim(String cntr, String endOrSuspendFlag)
	{
		int rc = 0;
		boolean cancel = false;
		final MFSConfig config = MFSConfig.getInstance();
		final String prln = "*ALL"; //$NON-NLS-1$
		if (config.containsNmbrPrlnFlagEntry("ENTITYWGT", this.opNmbr, prln, endOrSuspendFlag) != null) //$NON-NLS-1$//~11C
		{
			MFSWeightDimDialog dialog = new MFSWeightDimDialog(getParentFrame(), this);
			rc = dialog.displayForCntr(cntr);
			cancel = dialog.getCancelSelected();			
		}
		return rc == 0 && cancel == false;
	}

	/**
	 * Calls UPDT_ENT and updates the trees.
	 * @param tx 'N', 'D', 'M', or 'U'
	 * @param msg the action message for transaction
	 * @param sctr the selected container
	 * @param cntrs the <code>Enumeration</code> of containers for activity
	 * @throws IGSException error with server transaction
	 */
	@SuppressWarnings("rawtypes")
	private void updtEntity(char tx, String msg, String sctr, Enumeration cntrs)
		throws IGSException
	{
		char confirm = 'Y';//~20A
		boolean createContainer = true;//~20A
		MFSConfig config = MFSConfig.getInstance();//~20A
		
		// if config entry IS set and transaction is "NEW"
		if(tx == UPDT_NEW && config.containsConfigEntry("ENTITYCONTAINERSTATUSCHECK"))
		{
			// if the entity containers are not completed then confirm
			if(!findAllEntityCntrsCompletedInTree())
			{
				// prompt the Confirm Dialog (Yes/No)
				boolean answer = IGSMessageBox.showYesNoMB(getParentFrame(), "Confirm Dialog", 
						                                   "Entity container not completed, Are you sure you want to create a new container ? ", 
						                                   null);
				
				// if the answer is "NO" then DONT CREATE the new entity container
				if(!answer) // No
				{
					createContainer = false;
					confirm = 'N';
				}
			}// if(!findAllEntityCntrsCompletedInTree())
		}// if(config.containsConfigEntry("ENTITYCONTAINERSTATUSCHECK") && tx == 'N')
		//if config entry IS NOT set and transaction is "NEW"
		else if(tx == UPDT_NEW && !config.containsConfigEntry("ENTITYCONTAINERSTATUSCHECK"))
		{
			
			confirm = 'N';
			// if the entity containers are not completed then DON'T CREATE the new entity container
			if(!findAllEntityCntrsCompletedInTree())
			{
				createContainer = false;
				throw new IGSException("Entity container not completed", false);
			}// if(!findAllEntityCntrsCompletedInTree())
		}// else if(!config.containsConfigEntry("ENTITYCONTAINERSTATUSCHECK") && tx == 'N')
		
		if(createContainer)
		{
			IGSXMLTransaction updtEnt = new IGSXMLTransaction("UPDT_ENT"); //$NON-NLS-1$
			updtEnt.startDocument();
			updtEnt.setActionMessage(msg);
			updtEnt.addElement("SHPN", getShpnUO().getShpn()); //$NON-NLS-1$
			updtEnt.addElement("TRAN", new Character(tx)); //$NON-NLS-1$
			updtEnt.addElement("USER", MFSConfig.getInstance().get8CharUser()); //$NON-NLS-1$
			if ((tx == UPDT_NEW))                      //~20A
			{                                          //~20A
				updtEnt.addElement("CONFIRM", confirm);//~20A
			}                                          //~20A
			if ((tx == UPDT_DELETE) || (tx == UPDT_MERGE))
			{
				updtEnt.addElement("SCTR", sctr); //$NON-NLS-1$
			}
	
			if (cntrs != null)
			{
				updtEnt.startElement("CNTRS"); //$NON-NLS-1$
				while (cntrs.hasMoreElements())
				{
					updtEnt.addElement("CNTR", cntrs.nextElement()); //$NON-NLS-1$
				}
				updtEnt.endElement("CNTRS"); //$NON-NLS-1$
			}
			updtEnt.endDocument();
			makeServerCallWithReset(updtEnt, this);
			String trc;
			if ((trc = updtEnt.getFirstElement("TRC")) != null) //$NON-NLS-1$
			{
				if (Integer.parseInt(trc) != 0)
				{
					throw new IGSException(updtEnt.getNextElement("TERMS"), false); //$NON-NLS-1$
				}
			}
		}// if(createContainer)
	}

	/**
	 * Invoked when the value of the tree selection changes.
	 * @param tse the <code>TreeSelectionEvent</code>
	 */
	public void valueChanged(TreeSelectionEvent tse)
	{
		try
		{
			TreePath path = tse.getNewLeadSelectionPath();
			if (path != null)
			{
				this.taNodeInfo.setText(path.getLastPathComponent().toString());
			}
		}
		catch (Exception exception)
		{
			exception.printStackTrace();
		}
	}
	
	
	
	//~12A
	/**
	 *  Verify the Hazmat labeling for the given top cntr list
	 *  @param topCntrList the list of top cntrs
	 */
	private boolean vfyHazmatLabeling(List<String> topCntrList)
	{
		boolean proceed = true;	
		
		try
		{
			// Call hazmat validation only when entity container list is not empty
			if (!topCntrList.isEmpty())            //~13A
			{
				String mode = "OP"; // OverPack				 //$NON-NLS-1$
				MFSHazmatLabeling hazmatLbl = new MFSHazmatLabeling(getParentFrame(), this);
				hazmatLbl.setTriggerSource(SCREEN_ID);
				proceed = hazmatLbl.hazmatLabeling(topCntrList, mode);
			}
		}
		catch (Exception e)
		{
			proceed = false;
			IGSMessageBox.showOkMB(getParentFrame(), null, e.getMessage(), null);
		}
		
		return proceed;
	}  
	
	//~12A
	/** 
	 * Verify the Hazmat labeling for the given cntr
	 * @param cntr a cntr number
	 */
	private boolean vfyHazmatLabeling(String cntr, char cntrType)
	{
		boolean proceed = true;	
		
		try
		{
			if ('E' == cntrType)
			{					
				String mode = "OP"; // OverPack				 //$NON-NLS-1$
				MFSHazmatLabeling hazmatLbl = new MFSHazmatLabeling(getParentFrame(), this);
				hazmatLbl.setTriggerSource(SCREEN_ID);
				proceed = hazmatLbl.hazmatLabeling(cntr, mode);
			}
		}
		catch (Exception e)
		{
			proceed = false;
			IGSMessageBox.showOkMB(getParentFrame(), null, e.getMessage(), null);
		}
		
		return proceed;
	}
	
	@SuppressWarnings("rawtypes")
	private void checkIfItemHasMesAndBoxOrders()
	{
		DefaultMutableTreeNode shpnRoot = (DefaultMutableTreeNode) this.fieldLeftTreeModel.getRoot();
        
        for (Enumeration itemEnum = shpnRoot.children(); itemEnum.hasMoreElements();) {
        	DefaultMutableTreeNode itemNode = (DefaultMutableTreeNode) itemEnum.nextElement();
        	if(itemNode.getUserObject() instanceof MFSEntityMergeItemUO)
        	{
        		MFSEntityMergeItemUO itemUO = (MFSEntityMergeItemUO) itemNode.getUserObject(); 
                Iterator itemIter = itemUO.getMfgns().iterator();
                boolean hasMes = false;
                boolean hasBox = false;
                while (itemIter.hasNext())
                {
                	MFSEntityMergeMfgnUO mfgn = (MFSEntityMergeMfgnUO) itemIter.next();
                    if(mfgn.validateTypZ())
                    {
                    	hasMes = true;
                    }
                    else
                    {
                    	hasBox = true;
                    }
                    if(hasMes && hasBox)
                    {
                    	IGSMessageBox.showOkMB(getParentFrame(), "Tied orders", //$NON-NLS-1$
                    			      "The ITEM " + mfgn.getItem() + " has MES and BOX orders (Tie orders)", null); //$NON-NLS-1$ //$NON-NLS-2$
                    	break;
                    }
                }                            
        	}
        }

	}
	
	/** This function validates all MFGNs on Vector are valid BA MFGNs  ~17A */
	@SuppressWarnings("rawtypes")
	private void cleanupMFGNVectors()
	{
		DefaultMutableTreeNode shpnRoot = (DefaultMutableTreeNode) this.fieldLeftTreeModel.getRoot();
        
        for (Enumeration itemEnum = shpnRoot.children(); itemEnum.hasMoreElements();) {
        	DefaultMutableTreeNode itemNode = (DefaultMutableTreeNode) itemEnum.nextElement();
        	if(itemNode.getUserObject() instanceof MFSEntityMergeItemUO)
        	{
        		MFSEntityMergeItemUO itemUO = (MFSEntityMergeItemUO) itemNode.getUserObject(); 
                Iterator itemIter = itemUO.getMfgns().iterator();
                int count = 0;
                
                while (itemIter.hasNext())
                {
                	MFSEntityMergeMfgnUO mfgn = (MFSEntityMergeMfgnUO) itemIter.next();
                    if(mfgn.getCods().equals("BA")) //$NON-NLS-1$
                    {
                    	itemIter.remove();
                    	itemNode.remove(count);
                    	count--;
                    }
                    count++;
                }                         
                itemUO.updateTreeStrings();
        	}
        }
        this.fieldLeftTreeModel.reload();
    	this.trLeftTree.repaint();
    }
	
	/** Invoked when the mouse button has been clicked (pressed and released) on a component. */
//	public void mouseClicked(MouseEvent e)
//	{	
//	}
    
	/** Invoked when the mouse enters a component. */
//	public void mouseEntered(MouseEvent e)
//    {
//	}

	/** Invoked when the mouse exits a component. */
/*	public void mouseExited(MouseEvent e)
    {
	}
*/
	/** Invoked when a mouse button has been pressed on a component. */
/*	public void mousePressed(MouseEvent e)
    {
	}
*/
	/** Invoked when a mouse button has been released on a component. */
/*	public void mouseReleased(MouseEvent e) 
    {
	}
*/
}
