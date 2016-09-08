/* © Copyright IBM Corporation 2009, 2010. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date        Flag IPSR/PTR Name             Details
 * ----------  ---- -------- ---------------- ----------------------------------
 * 07/25/08   |    |31091JM  |Dave Fichtinger|-Initial - modeled from MFSDirectWorkPanel
 * 07/19/09	  |~01 |41330JL	 |Sayde Alcantara|-New CASECONTENTMEF label
 * 2010-03-09   ~2  47596MZ   D Kloepping     -EFFICIENCYON config present, do not lookup suspension
 * 											   codes default value.
 * 2010-11-01  ~03 49513JM	Toribio H.   	 -Make RTV_IQ Cacheable
 * 2010-12-14 |~04 |48749JL  |Santiago SC     -Hazmat labeling
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.panel;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.ListModel;

import mfsxml.MISSING_XML_TAG_EXCEPTION;
import mfsxml.MfsXMLDocument;
import mfsxml.MfsXMLParser;

import com.ibm.rchland.mfgapps.client.utils.exception.IGSException;
import com.ibm.rchland.mfgapps.client.utils.exception.IGSTransactionException;
import com.ibm.rchland.mfgapps.client.utils.layout.IGSGridLayout;
import com.ibm.rchland.mfgapps.client.utils.messagebox.IGSMessageBox;
import com.ibm.rchland.mfgapps.client.utils.xml.IGSXMLTransaction;
import com.ibm.rchland.mfgapps.mfsclient.MFSFrame;
import com.ibm.rchland.mfgapps.mfsclient.MFSInstructionsPanel;
import com.ibm.rchland.mfgapps.mfsclient.MFSMenuButton;
import com.ibm.rchland.mfgapps.mfsclient.MFSPanel;
import com.ibm.rchland.mfgapps.mfsclient.MFSPartInstructionJPanel;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSAcknowledgeInstructionDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSCntrDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSIQDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSPNSNSimpleDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSReductionPartListDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSSuspendCodeDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSWorkUnitPNSNDialog;
import com.ibm.rchland.mfgapps.mfsclient.struct.MFSComponentListModel;
import com.ibm.rchland.mfgapps.mfsclient.struct.MFSComponentRec;
import com.ibm.rchland.mfgapps.mfsclient.struct.MFSInstructionRec;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSButtonIterator;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSCP500Comparator;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSDetermineLabel;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSHazmatLabeling;
import com.ibm.rchland.mfgapps.mfscommon.MFSConfig;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSComm;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSFixedTransaction;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSTransaction;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSXmlTransaction;
import com.ibm.rchland.mfgapps.mfscommon.comm.trx.RTV_IQ;

/**
 * <code>MFSDirectWorkPanel</code> is the <code>MFSInstructionsPanel</code>
 * used to view the <code>MFSPartInstructionJPanel</code>s for building a
 * work unit and for FKIT.
 * @author The MFS Client Development Team
 */
@SuppressWarnings("serial")
public class MFSReductionPanel
	extends MFSInstructionsPanel
	implements MouseListener
{
	/** The default screen name of an <code>MFSReductionPanel</code>. */
	public static final String SCREEN_NAME = "Reduction";  //$NON-NLS-1$

	/** for position based transactions */
	private static final String BLANK_CNTR = "          "; //$NON-NLS-1$
	
	/*action code for drawer to rack function */
	private static final String MOVE = "MOVE"; //$NON-NLS-1$
	
	/*action code for drawer to rack function */
	private static final String UNDO = "UNDO"; //$NON-NLS-1$	
	
	/** The End This Operation (F3) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbEnd = MFSMenuButton.createSmallButton("End Job", //$NON-NLS-1$
			"smF3.gif", "End This Operation", false); //$NON-NLS-1$ //$NON-NLS-2$

	/** The Suspend this Operation (F7) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbSuspend = MFSMenuButton.createSmallButton("Suspend", //$NON-NLS-1$
			"smF7.gif", "Suspend this Operation", false); //$NON-NLS-1$ //$NON-NLS-2$

	/** The Container Function (F9) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbCntr = MFSMenuButton.createSmallButton("Contain", //$NON-NLS-1$
			"smF9.gif", "Container Function", false); //$NON-NLS-1$ //$NON-NLS-2$

	/** The Move Part (F11) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbMovePart = MFSMenuButton.createSmallButton("Move P/N", //$NON-NLS-1$
			"smF11.gif", "Move Part into Reduction Work Unit", false); //$NON-NLS-1$ //$NON-NLS-2$

	/** The Convert Drawer (F12) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbConvertDrawer = MFSMenuButton.createSmallButton("Cnvt Drw", //$NON-NLS-1$
			"smF12.gif", "Convert Drawer to Rack", false); //$NON-NLS-1$ //$NON-NLS-2$

	/** The Suspend this Operation (F13) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbUndo = MFSMenuButton.createSmallButton("Undo", //$NON-NLS-1$
			"smF13.gif", "Undo Drawer or Part Action", false); //$NON-NLS-1$ //$NON-NLS-2$

	/** The MCTL of the current work unit. */
	private String fieldCurrMctl = ""; //$NON-NLS-1$

	/** The end code. Used to determine the next action to perform. */
	private int fieldEndCode = 0;

	/** The row <code>Vector</code> when instructions are shown. */
	@SuppressWarnings({ "rawtypes" })
	private Vector fieldShowRowVector = new Vector();

	/** The row <code>Vector</code> when instructions are not shown. */
	@SuppressWarnings({ "rawtypes" })
	private Vector fieldHideRowVector = new Vector();
	
	/** The index of the active row in the part number list. */
	private int fieldActiveRowIndex = 0;
	
	/** The End/Suspend location. */
	private String fieldEndOrSuspendLoc = ""; //$NON-NLS-1$
	
	/** Stores the down bin parts (key: part number; value: serial number). */
	@SuppressWarnings({ "unused", "rawtypes" })
	private Hashtable fieldDownBinList = new Hashtable(); // ~13;

	/** The <code>MFSButtonIterator</code> for this panel. */
	private MFSButtonIterator fieldButtonIterator = null; //~16A

	/**
	 * Constructs a new <code>MFSReductionPanel</code>.
	 * @param parent the <code>MFSFrame</code> used to display this panel
	 * @param source the <code>MFSPanel</code> that caused this panel to be displayed
	 */
	public MFSReductionPanel(MFSFrame parent, MFSPanel source)
	{
		super(parent, source, SCREEN_NAME);
		this.fieldButtonIterator = createMenuButtonIterator();
		this.pnlButtons.setLayout(new IGSGridLayout(5, 4, 2, 1));
		this.pnlButtons.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
		this.addMyListeners();
	}

	/** Adds the listeners to this panel's <code>Component</code>s. */
	protected void addMyListeners()
	{
		this.fieldButtonIterator.reset();
		while (this.fieldButtonIterator.hasNext())
		{
			JButton next = this.fieldButtonIterator.nextJButton();
			next.addActionListener(this);
			next.addKeyListener(this);
		}

		for (int i = 0; i < this.fieldRowVector.size(); i++)
		{
			JList pipPNList = getRowVectorElementAt(i).getPNList();
			pipPNList.addKeyListener(this);
			pipPNList.addMouseListener(this);
		}

		this.spPartsInst.addKeyListener(this);
	}

	//~15A New method
	//~16C Add logic for use in Java 5 version of client 
	/** {@inheritDoc} */
	public void assignFocus()
	{
		// If scrollRectToVisible is not enabled, the panel is displaying
		// the MFSPartInstructionJPanels for a work unit for the first time.
		// Thus, the appropriate initial focus method is called.
		// Otherwise, the panel caused a different panel (fieldChildPanel)
		// to be displayed and is being redisplayed.
		if (this.pnlRowHolder.isScrollRectToVisibleEnabled() == false)
		{
			setInitialFocus();
		}
		else
		{
			if (this.fieldRowVector.size() > 0)
			{
				getRowVectorElementAt(this.fieldActiveRow).getPNList().requestFocusInWindow();
			}
		}
	}
	
	//~16A New method
	/** Removes all elements from the instruction <code>Vector</code>. */
	public void clearInstVector()
	{
		this.fieldInstVector.clear();
	}
	

	/** Determines which buttons are displayed in the menu. */
	public void configureButtons()
	{
		//Remove all of the buttons from the button panel before adding any
		this.pnlButtons.removeAll();

		//~16C Disable all of the buttons first, instead of just a few.
		this.fieldButtonIterator.reset();
		while(this.fieldButtonIterator.hasNext())
		{
			this.fieldButtonIterator.nextJButton().setEnabled(false);
		}

		this.pnlButtons.add(this.pbEnd);
		this.pbEnd.setEnabled(true);
		
		this.pnlButtons.add(this.pbSuspend);
		this.pbSuspend.setEnabled(true);
		
		this.pnlButtons.add(this.pbCntr);
		this.pbCntr.setEnabled(true);
		
		this.pnlButtons.add(this.pbMovePart);
		this.pbMovePart.setEnabled(true);
		
		this.pnlButtons.add(this.pbConvertDrawer);
		this.pbConvertDrawer.setEnabled(true);
		
		this.pnlButtons.add(this.pbUndo);
		this.pbUndo.setEnabled(true);
			
	}

	//~16A New method
	/**
	 * Configures the panel to display the Reduction screen
	 * depending on the value of <code>who</code>.
	 */
	public void configureReduction()
	{
		configureButtons();
		this.fieldActiveRow = 0;
		this.fieldBlueRow = -1;
		this.fieldActiveRowIndex = -1;
	}

	/**
	 * Creates an <code>MFSButtonIterator</code> for this panel's
	 * <code>MFSMenuButton</code>s.
	 * @return the <code>MFSButtonIterator</code>
	 */
	private MFSButtonIterator createMenuButtonIterator()
	{
		//The constructor param is the number of buttons
		MFSButtonIterator result = new MFSButtonIterator(6);
		result.add(this.pbEnd);
		result.add(this.pbSuspend);
		result.add(this.pbCntr);
		result.add(this.pbMovePart);
		result.add(this.pbConvertDrawer);
		result.add(this.pbUndo);
		return result;
	}
	
	/** Validates the work unit can be ended and invokes the correct method to end it. */
	public void end_reduce()
	{
		//Variable to determine if we will continue after Inspection questions are asked.
		//Initialize to true in case there are no Inspection questions
		boolean iq_continue = true;
		int rc = 0;
		try
		{
			removeMyListeners();
			
			/*
			 * Perform the logic for retrieving inspection
			 * questions for this operation. If the Ispq flag in
			 * the header is set to "B" for both or "A" for
			 * start, then we will go to the server to find the
			 * inspection questions.
			 */
			if (this.fieldHeaderRec.getIspq().equals("B") //$NON-NLS-1$
					|| this.fieldHeaderRec.getIspq().equals("A")) //$NON-NLS-1$
			{
				RTV_IQ rtvIQ = new RTV_IQ(this); //~03
				rtvIQ.setInputNmbr(this.fieldHeaderRec.getNmbr());
				rtvIQ.setInputPrln(this.fieldHeaderRec.getPrln());
				rtvIQ.setInputProd(this.fieldHeaderRec.getProd());
				rtvIQ.setInputX("2"); //$NON-NLS-1$
				rtvIQ.execute();					
				rc = rtvIQ.getReturnCode();
				
				if (rc != 0)
				{
					//don't allow the program to get by the inspection questions
					iq_continue = false;
					IGSMessageBox.showOkMB(getParentFrame(), null, rtvIQ.getErrorMessage(), null);
				}
				else
				{
					MFSIQDialog iq = new MFSIQDialog(getParentFrame());
					iq.loadIQ(rtvIQ.getOutputIQ());
					iq.setLocationRelativeTo(getParentFrame()); //~17A
					iq.setVisible(true);
					if (iq.getPressedCancel())
					{
						iq_continue = false;
					}
				}
			} // end of Ispq = 'B' or 'S'

			if (iq_continue)
			{
				rc = end_wu("C", "00", "0000"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				this.fieldEndCode = rc;
				if (rc == 30 || rc == 31)
				{
					addMyListeners();
					this.setCurrentCompRec(null);

					restorePreviousScreen();
				}
				else
				{
					addMyListeners();
				}		
				
			}	
		}
		catch (Exception e)
		{
			this.setCursor(Cursor.getDefaultCursor());
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);

			addMyListeners();
			focusPNListOrButton(this.pbEnd);
		}
	}

	/* 11/17/2004 | PTR 29318PT | TL Moua  @1 |-Display a list of error msgs when rc=201 */
	/**
	 * Ends the work unit.
	 * @param ecod the end code ('C'omplete, 'F'ail, or 'S'uspend)
	 * @param qcod
	 * @param oemo the operation ending mode
	 * @return the return code
	 */
	public int end_wu(String ecod, String qcod, String oemo)
	{
		int rc = 0;

		try
		{
			//~04A - Start
			if(!vfyHazmatLabeling(ecod))
			{
				if(ecod.equals("C"))
				{
					String msg = "Error: Verify all hazmat label part numbers to end the operation.";
					IGSMessageBox.showOkMB(getParentFrame(), null, msg, null);
					return -1;
				}		
			}
			//~04A - End
			
			final MFSConfig config = MFSConfig.getInstance();

			StringBuffer buffer = new StringBuffer();
			buffer.append("END_REDUCE"); //$NON-NLS-1$
			buffer.append(ecod);
			buffer.append(qcod);
			buffer.append(config.get8CharCellType());
			buffer.append(config.get8CharCell());
			buffer.append(getCurrMctl());
			buffer.append(config.get8CharUser());
			buffer.append(this.fieldHeaderRec.getOlev());
			buffer.append(this.fieldHeaderRec.getNmbr());
			buffer.append(oemo);
			buffer.append(this.fieldEndOrSuspendLoc);

			MFSTransaction end_reduce = new MFSFixedTransaction(buffer.toString());
			if (ecod.equals("S")) //$NON-NLS-1$
			{
				end_reduce.setActionMessage("Suspending the Work Unit, Please Wait..."); //$NON-NLS-1$
			}
			else
			{
				end_reduce.setActionMessage("Ending the Work Unit, Please Wait..."); //$NON-NLS-1$
			}
			MFSComm.getInstance().execute(end_reduce, this);
			rc = end_reduce.getReturnCode();

			/* @1A */
			if (rc != 0 && rc != 31 && rc != 30)
			{
				String erms = end_reduce.getOutput();
				IGSMessageBox.showOkMB(getParentFrame(), null, erms, null);
			}			
		}
		catch (Exception e)
		{
			setCursor(Cursor.getDefaultCursor());
			rc = 10;
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
		return rc;
	}
	
	//~16A New method
	/**
	 * Executes the specified <code>runnable</code> in a new
	 * <code>Thread</code>, calling <code>updateAction</code>
	 * until the <code>Thread</code> dies.
	 * @param runnable the <code>Runnable</code> to execute
	 * @param actionMessage the action message to display
	 */
//	private void executeRunnable(Runnable runnable, String actionMessage)
//	{
//		startAction(actionMessage);
//		Thread thread = new Thread(runnable);
//		thread.start();
//		while (thread.isAlive())
//		{
//			updateAction(actionMessage, -1);
//			try
//			{
//				Thread.sleep(100);
//			}
//			catch (InterruptedException ie)
//			{
//				ie.printStackTrace();
//			}
//		}
//		stopAction();
//	}

	/** Selects the PN list element corresponding to {@link #fieldActiveRowIndex}. */
	public void ensureActiveRowIndexIsHighlighted()
	{
		if (this.fieldActiveRowIndex != -1)
		{
			getRowVectorElementAt(this.fieldActiveRow).getPNList().setSelectedIndex(this.fieldActiveRowIndex);
			this.fieldActiveRowIndex = -1;
		}
	}


	/**
	 * Requests the foucs for the part number list of the active row or the
	 * specified <code>MFSMenuButton</code> if the row vector is empty.
	 * @param menuButton the <code>MFSMenuButton</code> to give the focus if
	 *        the row vector is empty
	 */
	private void focusPNListOrButton(MFSMenuButton menuButton)
	{
		if (this.fieldRowVector.size() > 0)
		{
			getRowVectorElementAt(this.fieldActiveRow).getPNList().requestFocusInWindow();
		}
		else
		{
			menuButton.requestFocusInWindow();
		}
	}
	
	/**
	 * Returns the MCTL (Work Unit Control Number) for the current work unit.
	 * @return the MCTL for the current work unit
	 */
	public String getCurrMctl()
	{
		return this.fieldCurrMctl;
	}

	/**
	 * Returns the end code.
	 * @return the end code
	 */
	public int getEndCode()
	{
		return this.fieldEndCode;
	}

	//~16A New method
	/** Initializes the text of {@link #lblSaps} and {@link #lblSapo}. */
	protected void initSapsAndSapoLabels()
	{
		String saps = this.fieldHeaderRec.getSaps();
		if (!saps.equals("            ")) //$NON-NLS-1$
		{
			this.lblSaps.setText("S Order: " + saps); //$NON-NLS-1$
		}
		else
		{
			this.lblSaps.setText(""); //$NON-NLS-1$
		}

		String sapo = this.fieldHeaderRec.getSapo();
		if (!sapo.equals("            ")) //$NON-NLS-1$
		{
			this.lblSapo.setText("P Order: " + this.fieldHeaderRec.getSapo()); //$NON-NLS-1$
		}
		else
		{
			this.lblSapo.setText(""); //$NON-NLS-1$
		}
	}
	
	/**
	 * Called to remove parts from rack/drawers/blades into the reduction work unit - this guy handles the
	 * call to LOGREDPART and then updates the screen based on the returned data 
	 */
	public void issueLogRedPart(String partsInfo,String mode)
	throws IGSException
	{	
		IGSXMLTransaction logRedPartSpec = new IGSXMLTransaction("LOGREDPART"); //$NON-NLS-1$
		if(mode.equals(MOVE))
			logRedPartSpec.setActionMessage("Moving Part(s)!"); //$NON-NLS-1$
		else if(mode.equals(UNDO))
			logRedPartSpec.setActionMessage("Undoing Part"); //$NON-NLS-1$
			
		logRedPartSpec.startDocument();
		logRedPartSpec.addElement("USER", MFSConfig.getInstance().get8CharUser()); //$NON-NLS-1$
		logRedPartSpec.addElement("CELL", MFSConfig.getInstance().get8CharCell()); //$NON-NLS-1$
		logRedPartSpec.addElement("REDWU", getHeaderRec().getMctl()); //$NON-NLS-1$
		logRedPartSpec.addElement("CNTR", getHeaderRec().getCntr()); //$NON-NLS-1$
		logRedPartSpec.addElement("PARTS",partsInfo); //$NON-NLS-1$
		logRedPartSpec.endDocument();
		MFSComm.getInstance().execute(logRedPartSpec, this);
		if (logRedPartSpec.getReturnCode() != 0)
		{
			throw new IGSTransactionException(logRedPartSpec, false);
		}	
		else
		{
			if(mode.equals(UNDO))
			{
				removeCompRecFromPanel();	
			}
			
			else if(mode.equals(MOVE))
			{
				try
				{
					MFSComponentRec temp;
					
					MfsXMLParser parser = new MfsXMLParser(logRedPartSpec.getOutput());
					String crXMLString = parser.getField("CR"); //$NON-NLS-1$
					
					while(!crXMLString.equals("")) //$NON-NLS-1$
					{
						temp = new MFSComponentRec(new MfsXMLParser(crXMLString));
						if(temp.loadError != "") //$NON-NLS-1$
						{
							addComponentToScreen(temp);
							crXMLString = parser.getNextField("CR"); //$NON-NLS-1$
						}
						else
						{
							IGSMessageBox.showOkMB(this, null, temp.loadError,null);	
						}
					}
				}
				catch (MISSING_XML_TAG_EXCEPTION mte)
				{
					IGSMessageBox.showOkMB(this, null, null, mte);
				}

			}
		}
	}

	/**
	* Issues LOGREDDRAW transaction to server - handles both remove and undo 
	*/
	private void handleDrawer(String drawerWorkUnit,String action) 
		throws IGSException
	{	
		IGSXMLTransaction logRedDraw = new IGSXMLTransaction("LOGREDDRAW"); //$NON-NLS-1$
		if(action.equals(MOVE))
		{	
			logRedDraw.setActionMessage("Converting Drawer " + drawerWorkUnit + " to a Rack!"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		else
		{
			logRedDraw.setActionMessage("Converting Rack " + drawerWorkUnit + " to a Drawer!");			 //$NON-NLS-1$ //$NON-NLS-2$
		}
		logRedDraw.startDocument();
		logRedDraw.addElement("REDWU",this.getHeaderRec().getMctl()); //$NON-NLS-1$
		logRedDraw.addElement("DRAW",drawerWorkUnit); //$NON-NLS-1$
		logRedDraw.addElement("MFGN",this.getHeaderRec().getMfgn()); //$NON-NLS-1$
		logRedDraw.addElement("IDSS",this.getHeaderRec().getIdss()); //$NON-NLS-1$
		logRedDraw.addElement("USER", MFSConfig.getInstance().get8CharUser()); //$NON-NLS-1$
		logRedDraw.addElement("CELL", MFSConfig.getInstance().get8CharCell()); //$NON-NLS-1$
		logRedDraw.addElement("ACTION",action); //$NON-NLS-1$
		logRedDraw.endDocument();
	
		MFSComm.getInstance().execute(logRedDraw, this);
		if (logRedDraw.getReturnCode() != 0)
		{
			throw new IGSTransactionException(logRedDraw, false);
		}
		else
		{
			if(action.equals(MOVE))
			{
				try
				{
					MfsXMLParser parser = new MfsXMLParser(logRedDraw.getOutput());
					String crXMLString = parser.getField("CR"); //$NON-NLS-1$
					
					MFSComponentRec temp = new MFSComponentRec(new MfsXMLParser(crXMLString));
					if(temp.loadError != "") //$NON-NLS-1$
						addComponentToScreen(temp);
					else
					{
						IGSMessageBox.showOkMB(this, null, temp.loadError,null);	
					}
				}
				catch (MISSING_XML_TAG_EXCEPTION mte)
				{
					IGSMessageBox.showOkMB(this, null, null, mte);
				}
			}
			else if (action.equals(UNDO))
			{
				removeCompRecFromPanel();	
			}
		}
	
	}
	
	
	/**
	 * Sets up a new row for the part we just "added" to the panel
	 * @param newComp the <code>MFSComponentRec</code> for the new row
	 */
	public void setupNewPartRow(MFSComponentRec newComp)
	{
		try
		{
			boolean someInstructions = false;
			MFSInstructionRec extraInst = null;
			MFSComponentListModel extraLm = null;
			String suffix = newComp.getSuff();
			String seq = newComp.getNmsq();

			if (this.fieldInstVector.size() > 0
					|| (this.fieldRowVector.size() > 0 && getRowVectorElementAt(0).getName().equals("RowShort")) //$NON-NLS-1$
					&& !this.fieldHeaderRec.getNmbr().equals("FKIT")) //$NON-NLS-1$
			{
				someInstructions = true;
			}

			extraInst = new MFSInstructionRec();
			extraInst.setSuff(suffix); 
			extraInst.setIseq(seq); 

			//should prevent updt_instr from thinking it has to update it
			extraInst.setCompletionStatusOriginal("C"); //$NON-NLS-1$
			extraInst.setCompletionStatus("C"); //$NON-NLS-1$

			// remove the row and add it back later
			String rowName = "Row" + suffix + seq; //$NON-NLS-1$
			
			this.pnlRowHolder.invalidate();
			extraLm = new MFSComponentListModel();
			extraLm.setSuff(suffix);
			extraLm.setNmsq(seq);
			extraLm.addElement(newComp);
			
			/* Set up some constraints for our new row. */
			GridBagConstraints tmpConstraints = createPipConstraints(true,
					this.fieldRowVector.size());

			MFSPartInstructionJPanel pip = new MFSPartInstructionJPanel(this);
			String blvl = this.fieldHeaderRec.getBlvl();
			pip.configure(extraLm, extraInst, someInstructions, blvl);
			pip.setName(rowName); 
			pip.setInstructionRec(extraInst);

			this.fieldRowVector.addElement(pip);

			pip.getPNList().addListSelectionListener(this);

			this.pnlRowHolder.add(pip, tmpConstraints);
			this.pnlRowHolder.validate();
			pip.getPNList().setSelectedIndex((pip.getPNListModel()).size() - 1);
			
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
	}
	
	
	/**
	 * First adds to the univeral list model, then looks for the right list model to add the part to
	 * add to based on suffix sequencde
	 * @param newComp the <code>MFSComponentRec</code> for the new row
	 */
	private void addComponentToScreen(MFSComponentRec current)
	{

		int index = 0;
		boolean foundHome = false;
		MFSPartInstructionJPanel pip;
		String currentCompPIPHomeName = "Row"+current.getSuff()+current.getNmsq(); //$NON-NLS-1$
		this.fieldComponentListModel.addElement(current);
		
		if(this.fieldRowVector.size() > 0)
		{	
			while(index < this.fieldRowVector.size() && !foundHome)
			{
				pip = (MFSPartInstructionJPanel)(this.fieldRowVector.elementAt(index));
			
				if(pip.getName().equals(currentCompPIPHomeName))
				{
					pip.getPNListModel().addElement(current);
					foundHome = true;
					this.pnlRowHolder.validate();
				}
				else
				{
					index++;	
				}
			}
		}//end of fieldRowVector has non-zero size
				
		if(!foundHome)
		{
			setupNewPartRow(current);	
			this.validate();
		}
	}
	
	/** {@inheritDoc} */
	public void loadPartsModels()
	{
		try
		{
			//make sure we have none from earlier ops
			this.fieldLmVector.clear();

			if (this.fieldComponentListModel.size() > 0)
			{
				int index = 0;
				boolean shorted = false;
				MFSComponentListModel curLm;
				String curSuffix = "          "; //$NON-NLS-1$
				String curSeq = "     "; //$NON-NLS-1$
				String curNmbr = "    "; //$NON-NLS-1$

				MFSComponentRec next = getComponentListModelCompRecAt(index);

				// handle all the shorted parts
				if (next.isShortPart() && !this.fieldHeaderRec.getNmbr().equals("FKIT")) //$NON-NLS-1$
				{
					shorted = true;
					curLm = new MFSComponentListModel();
					curLm.addElement(next);

					curLm.setIsShort(true);
					curSuffix = next.getSuff();
					curSeq = next.getNmsq();
					curLm.setSuff(curSuffix);
					curLm.setNmsq(curSeq);
					curNmbr = next.getNmbr();
					index++;
					while (index < this.fieldComponentListModel.size() && shorted)
					{
						next = getComponentListModelCompRecAt(index);
						if (next.isShortPart())
						{
							if (next.getSuff().equals(curSuffix)
									&& next.getNmsq().equals(curSeq)
									&& next.getNmbr().equals(curNmbr))
							{
								curLm.addElement(next);
							}
							else
							{
								curSuffix = next.getSuff();
								curSeq = next.getNmsq();
								curNmbr = next.getNmbr();
								this.fieldLmVector.addElement(curLm);
								curLm = new MFSComponentListModel();
								curLm.setSuff(curSuffix);
								curLm.setNmsq(curSeq);
								curLm.setIsShort(true);
								curLm.addElement(next);
							}
							index++;
						}
						else
						{
							shorted = false;
						}
					}
					this.fieldLmVector.addElement(curLm);
				}

				// handle different suffix sequences here
				if (index < this.fieldComponentListModel.size())
				{
					curSuffix = next.getSuff();
					curSeq = next.getNmsq();
					curLm = new MFSComponentListModel();
					curLm.setSuff(curSuffix);
					curLm.setNmsq(curSeq);

					//curLm is blank, add some elements to it
					while (index < this.fieldComponentListModel.size())
					{
						next = getComponentListModelCompRecAt(index);
						if (next.getSuff().equals(curSuffix)
								&& next.getNmsq().equals(curSeq))
						{
							curLm.addElement(next);
						}
						else
						{
							curSuffix = next.getSuff();
							curSeq = next.getNmsq();
							this.fieldLmVector.addElement(curLm);
							curLm = new MFSComponentListModel();
							curLm.setSuff(curSuffix);
							curLm.setNmsq(curSeq);
							curLm.addElement(next);
						}
						index++;
					}
					//add last lm to the vector
					this.fieldLmVector.addElement(curLm);
				}
			}// end size() > 0
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
	}

	/**
	 * Logs a blue row instruction.
	 * @param row the index of the row
	 * @param trigger either keyPressed or a log type
	 */
	public void logBlueInstruction(int row, String trigger)
	{
		boolean found = false;
		boolean foundNPI = false;
		int newRow = 0;
		int index = 0;
		JList tempList = null;
		MFSComponentListModel tempLm = null;
		boolean pressedCancel = false;

		MFSPartInstructionJPanel pip = getRowVectorElementAt(row);

		MFSAcknowledgeInstructionDialog ackD = new MFSAcknowledgeInstructionDialog(getParentFrame());
		ackD.setLocationRelativeTo(this.pnlButtons);
		ackD.setVisible(true);
		if (ackD.getPressedLog() == false)
		{
			pip.unDoChangeColor();
			pressedCancel = true;
			this.fieldBlueRow = -1;
		}
		else
		{
			//completed instruction
			pip.setCompletionStatus(MFSPartInstructionJPanel.COMPLETE);
			pip.setNoPartPanelBackground(Color.cyan);
		}

		//find the next row to work on
		newRow = row + 1;
		MFSComponentRec next;

		while (newRow < this.fieldRowVector.size() && !found && !foundNPI)
		{
			MFSPartInstructionJPanel newPip = getRowVectorElementAt(newRow);
			if (!newPip.getIsNonPartInstruction())
			{
				index = 0;
				tempList = newPip.getPNList();
				tempLm = newPip.getPNListModel();

				while (index < tempLm.size() && !found)
				{
					next = tempLm.getComponentRecAt(index);

					if (next.getIdsp().equals("X") //$NON-NLS-1$
							|| (next.getIdsp().equals("A") //$NON-NLS-1$
									&& !next.getMlri().equals(" ") //$NON-NLS-1$
									&& !next.getMlri().equals("0"))) //$NON-NLS-1$
					{
						found = true;
					}
					else
					{
						index++;
					}
				}
			}//end of if non-part

			// we are progressing to a non-part row.
			//If we are not complete we will stop here
			else
			{
				if (!newPip.getInstructionRec().getCompletionStatus().equals("C")) //$NON-NLS-1$
				{
					foundNPI = true;
				}
			}

			if (!found && !foundNPI)
			{
				newRow++;
			}
		}//end of while loop

		//now start at top and look for a row
		if (!found && !foundNPI)
		{
			newRow = 0;
			while (newRow < row && !found && !foundNPI)
			{
				MFSPartInstructionJPanel newPip = getRowVectorElementAt(newRow);
				if (!newPip.getIsNonPartInstruction())
				{
					index = 0;
					tempList = newPip.getPNList();
					tempLm = newPip.getPNListModel();

					while (index < tempLm.size() && !found)
					{
						next = tempLm.getComponentRecAt(index);

						if (next.getIdsp().equals("X") //$NON-NLS-1$
								|| (next.getIdsp().equals("A") //$NON-NLS-1$
										&& !next.getMlri().equals(" ") //$NON-NLS-1$ 
										&& !next.getMlri().equals("0"))) //$NON-NLS-1$
						{
							found = true;
						}
						else
						{
							index++;
						}
					}
				}//end of if non-part

				//we are progressing to a non-part row. 
				//If we are not complete we will stop here
				else
				{
					if (!newPip.getInstructionRec().getCompletionStatus().equals("C")) //$NON-NLS-1$
					{
						foundNPI = true;
					}
				}

				if (!found && !foundNPI)
				{
					newRow++;
				}
			}//end of while loop
		}

		//we are done now, let's do something based on what we found
		//if we found a NonPart Instruction we will make it the next blueRow
		if (foundNPI)
		{
			this.fieldBlueRow = newRow;
			this.spPartsInst.requestFocusInWindow();
			MFSPartInstructionJPanel pip2 = getRowVectorElementAt(newRow);
			this.spPartsInst.getVerticalScrollBar().setValue((int) pip2.getBounds().getY());
			pip2.changeColor(MFSPartInstructionJPanel.BLUE_ROW_COLOR);
			
			if (!trigger.equals("keyPressed") && !pressedCancel) //$NON-NLS-1$
			{
				logBlueInstruction(newRow, trigger);
			}
		}
		//if we found a part to be added, we will make it the next activeRow
		else if (found)
		{
			this.spPartsInst.requestFocusInWindow();
			MFSPartInstructionJPanel pip2 = getRowVectorElementAt(newRow);
			this.spPartsInst.getVerticalScrollBar().setValue((int) pip2.getBounds().getY());
			tempList.setSelectedIndex(index);
			tempList.ensureIndexIsVisible(index);
			tempList.requestFocusInWindow();
			
			if (!trigger.equals("keyPressed") && !pressedCancel) //$NON-NLS-1$
			{
				//showLogPart(trigger);
			}
		}
		else //move screen to top
		{
			this.spPartsInst.requestFocusInWindow();
			MFSPartInstructionJPanel pip2 = getRowVectorElementAt(0);
			this.spPartsInst.getVerticalScrollBar().setValue((int) pip2.getBounds().getY());
		}
	}

	/**
	 * Prepares the panel for display by setting the text of text components,
	 * storing the value of the mctl, and resetting the end code to 0.
	 * @param mctl the mctl of the work unit
	 */
	public void prepareForDisplay(String mctl)
	{
		this.fieldCurrMctl = mctl;
		this.fieldEndCode = 0;

		this.tpInstalledParts.setText(""); //$NON-NLS-1$
		this.lblUser.setText("User:  " + MFSConfig.getInstance().getConfigValue("USER"));  //$NON-NLS-1$//$NON-NLS-2$
		this.lblMctl.setText("Mctl:  " + mctl); //$NON-NLS-1$
		initSapsAndSapoLabels();
		updt_cntr(this.fieldHeaderRec.getCntr());
		initHeaderLabels();
	}

	private void removeCompRecFromPanel() 
	{
		int indexUtil = 0;
		boolean done = false;
		//funny stuff here, but the componentListModel is a big collection of all of the components
		//and the individual part instruction panels have their own lists...so we need to take it out 
		//of both places I belive.
		while(!done && indexUtil < this.fieldComponentListModel.getSize())
		{
			MFSComponentRec next = getComponentListModelCompRecAt(indexUtil);
			
			if(next.getMctl().equals(this.fieldCurrentCompRec.getMctl()) &&
				next.getCrct().equals(this.fieldCurrentCompRec.getCrct()) &&
				next.getInpn().equals(this.fieldCurrentCompRec.getInpn()) )
			{
			  done = true;
			}
			else
			{
				indexUtil++;
			}	
		}

		if(!done)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, "Part Not Found?", null); //$NON-NLS-1$
		}
		else
		{	
			int highlightedIndex = getRowVectorElementAt(this.fieldActiveRow).getPNList().getSelectedIndex();
			MFSComponentListModel pipLM = getRowVectorElementAt(this.fieldActiveRow).getPNListModel();
			pipLM.remove(highlightedIndex);
			if(pipLM.getSize() == 0)
			{	
				this.fieldRowVector.remove(this.fieldActiveRow);
				this.pnlRowHolder.remove(this.fieldActiveRow);
				//move it up one row if we can
				if(this.fieldActiveRow > 0)
					this.fieldActiveRow--;
			}
			this.fieldComponentListModel.remove(indexUtil);
			setNewFocus(highlightedIndex);
			this.validate();
		}	
		
	}

	/**
	 * Redoes the layout after the <code>MFSPartInstructionJPanel</code> at
	 * the specified <code>row</code> in {@link #pnlRowHolder} changed.
	 * @param row the row of the <code>MFSPartInstructionJPanel</code>
	 */
//	public void redoLayout(int row)
//	{
		//~16C This method used to remove the row from pnlRowHolder, change its
		// display properties and GridBagConstraints, and add it back in to
		// change how the row was displayed. With the change in the layout of
		// the rows, this should no longer be necessary. Instead, invalidate
		// validate, and update are called to redo the layout.
//		invalidate();
//		validate();
//		update(getGraphics());
//	}

	private void setNewFocus(int previousIndex) 
	{
		//~16A New method
		/** Called by {@link #assignFocus()} to set the initial focus. */
		this.spPartsInst.requestFocusInWindow();

		//If first row is a part instruction, select and focus the first part.
		//If first row is a nonpart instruction,
		//-set the blue row
		//-set the activeRow to the first part instruction
		//-assign the initial focus to a button
		//If no rows, assign the initial focus to a button
		if (this.fieldRowVector.size() > 0)
		{
			MFSPartInstructionJPanel pip = getRowVectorElementAt(fieldActiveRow);
			if (pip.getIsNonPartInstruction() == false)
			{
				if(previousIndex==0)
					pip.getPNList().setSelectedIndex(0);
				else
					pip.getPNList().setSelectedIndex(previousIndex-1);
				
				pip.getPNList().requestFocusInWindow();
			}
			else
			{
				this.fieldBlueRow = 0;
				pip.changeColor(MFSPartInstructionJPanel.BLUE_ROW_COLOR);
				for (int i = fieldActiveRow; i < this.fieldRowVector.size(); i++)
				{
					MFSPartInstructionJPanel pip2 = getRowVectorElementAt(i);
					if (pip2.getIsNonPartInstruction() == false)
					{
						this.fieldActiveRow = i;
						this.fieldActiveRowIndex = 0;
						break;
					}
				}

				this.pbEnd.requestFocusInWindow();
			}
		}
		else
		{
			this.pbEnd.requestFocusInWindow();
		}
		
	}

	/** Removes the listeners from this panel's <code>Component</code>s. */
	protected void removeMyListeners()
	{
		//~16 Redone to use button iterator and remove PTR35927JM code
		this.fieldButtonIterator.reset();
		while (this.fieldButtonIterator.hasNext())
		{
			JButton next = this.fieldButtonIterator.nextJButton();
			next.removeActionListener(this);
			next.removeKeyListener(this);
		}

		for (int i = 0; i < this.fieldRowVector.size(); i++)
		{
			JList pipPNList = getRowVectorElementAt(i).getPNList();
			pipPNList.removeKeyListener(this);
			pipPNList.removeMouseListener(this);
		}
		
		this.spPartsInst.removeKeyListener(this);
	}
	
	/**
	 * Called to remove a part.
	 * @param logType - part or drawer
	 */
	public void removeDrawerButtonExecuter()
	{
		try
		{
			removeMyListeners();
			
			MFSWorkUnitPNSNDialog myWrkUnitPNSND = 
				new MFSWorkUnitPNSNDialog(getParentFrame());
			myWrkUnitPNSND.setLocationRelativeTo(getParentFrame()); //~17A
			myWrkUnitPNSND.setVisible(true);
			if(myWrkUnitPNSND.getPressedEnter())
				handleDrawer(myWrkUnitPNSND.getMctl(),MOVE);			
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}

		addMyListeners();
		focusPNListOrButton(this.pbConvertDrawer);
	}

	/**
	* this method handles the activity for fulfillment parts 
	*/
	private void removePartButtonExecuter() 
		throws IGSException
	{	
		
		if(getHeaderRec().getCntr().trim().equals("")) //$NON-NLS-1$
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, "Please Assign a Container", null); //$NON-NLS-1$
		}
		else
		{	
			String part = new String();
			String serial = new String();
			
			MFSPNSNSimpleDialog myPNSNSimpleDialog = new MFSPNSNSimpleDialog(getParentFrame(),"Scan Part Number (Sequence Number Optional)",false); //$NON-NLS-1$
			myPNSNSimpleDialog.setLocationRelativeTo(getParentFrame());
			myPNSNSimpleDialog.setVisible(true);
	
			if (myPNSNSimpleDialog.getPressedEnter())
			{
				part = myPNSNSimpleDialog.getPNText();
				serial = myPNSNSimpleDialog.getSNText();
			
				IGSXMLTransaction qryRedPart = new IGSXMLTransaction("QRYREDPART"); //$NON-NLS-1$
				qryRedPart.setActionMessage("Finding Part!"); //$NON-NLS-1$
				qryRedPart.startDocument();
				qryRedPart.addElement("REDWU", getHeaderRec().getMctl()); //$NON-NLS-1$
				qryRedPart.addElement("INPN",part); //$NON-NLS-1$
				if(!serial.trim().equals("")) //$NON-NLS-1$
					qryRedPart.addElement("INSQ",serial); //$NON-NLS-1$
				qryRedPart.endDocument();
			
				MFSComm.getInstance().execute(qryRedPart, this);
				if (qryRedPart.getReturnCode() != 0)
				{
					throw new IGSTransactionException(qryRedPart, false);
				}
				else
				{
					MfsXMLParser parser = new MfsXMLParser(qryRedPart.getOutput());
					String partDescription = new String();
					try
					{
						partDescription = parser.getField("DESC").trim(); //$NON-NLS-1$
					}
					catch (MISSING_XML_TAG_EXCEPTION mte)
					{
						partDescription = "Not Found"; //$NON-NLS-1$
					}
					MFSReductionPartListDialog myReductionPartListDialog = new MFSReductionPartListDialog(getParentFrame(),"Part Found", part,partDescription,getHeaderRec().getCntr()); //$NON-NLS-1$
					myReductionPartListDialog.loadList(parser);
					myReductionPartListDialog.setLocationRelativeTo(getParentFrame());
					myReductionPartListDialog.setVisible(true);
	
					if(!myReductionPartListDialog.getPartsString().equals("")) //$NON-NLS-1$
					{
						issueLogRedPart(myReductionPartListDialog.getPartsString(),MOVE);
						
					}//end of non empty partsString			
				}
			}//end of pressed enter
		}//end of non-blank container	
	}
	
	/**
	 * Clears the contents of the panel before calling
	 * {@link MFSFrame#restorePreviousScreen(MFSPanel)}.
	 */
	public void restorePreviousScreen()
	{
		this.pnlRowHolder.removeAll();
		this.fieldLmVector.removeAllElements();

		// Clear references to the MFSPartInstructionJPanels in HideRowVector
		this.fieldHideRowVector.removeAllElements();
		// Clear references to the MFSPartInstructionJPanels in ShowRowVector
		this.fieldShowRowVector.removeAllElements();
		// Clear the MFSPartInstructionJPanels
		this.fieldRowVector.removeAllElements();
		this.fieldInstVector.removeAllElements();

		getParentFrame().restorePreviousScreen(this);
	}


	/**
	 * Invokes the RTV_CNTR transaction, displays an <code>MFSCntrDialog</code>,
	 * and handles the user's selection.
	 */
	public void rtv_cntr()
	{
		int rc = 0;

		try
		{
			removeMyListeners();
			String errorString = ""; //$NON-NLS-1$
			final MFSConfig config = MFSConfig.getInstance();

			String data = "RTV_CNTR  " + getCurrMctl() + "L" + //$NON-NLS-1$ //$NON-NLS-2$
					BLANK_CNTR + config.get8CharUser();

			MFSTransaction rtv_cntr = new MFSFixedTransaction(data);
			rtv_cntr.setActionMessage("Retrieving List of Containers, Please Wait..."); //$NON-NLS-1$
			MFSComm.getInstance().execute(rtv_cntr, this);
			rc = rtv_cntr.getReturnCode();
			MFSDetermineLabel label = new MFSDetermineLabel(this.fieldRowVector); /* ~01A */

			if (rc == 0)
			{
				MFSCntrDialog cntrJD = new MFSCntrDialog(getParentFrame());
				cntrJD.loadCntrListModel(rtv_cntr.getOutput());
				cntrJD.setSelectedIndex(0);
				cntrJD.setLocationRelativeTo(getParentFrame()); //~17A
				cntrJD.setVisible(true);
				
				if (cntrJD.getButtonPressed().equals("Select")) //$NON-NLS-1$
				{
					boolean go = true;
					boolean newCntr = false;
					StringBuffer data2 = new StringBuffer();
					data2.append("RTV_CNTR  "); //$NON-NLS-1$
					data2.append(getCurrMctl());

					if (cntrJD.getSelectedCntr().equals("NEW       ")) //$NON-NLS-1$
					{
						Object[] options = {"YES", "NO"}; //$NON-NLS-1$ //$NON-NLS-2$
						int n = JOptionPane.showOptionDialog(getParentFrame(),
								"Do you really want to create a new Container?", //$NON-NLS-1$
								"Create New Container", JOptionPane.YES_NO_OPTION, //$NON-NLS-1$
								JOptionPane.WARNING_MESSAGE, null, options, options[1]);

						if (n == JOptionPane.YES_OPTION)
						{
							newCntr = true;
							data2.append("N"); //$NON-NLS-1$
							data2.append(BLANK_CNTR);  /* ~25A */
							data2.append(config.get8CharUser());  /* ~25A */

						}
						else
						{
							go = false;
						}
					}
					else if (!cntrJD.getSelectedCntr().equals(this.fieldHeaderRec.getCntr()))
					{
						data2.append("A"); //$NON-NLS-1$
						data2.append(cntrJD.getSelectedCntr());
						data2.append(config.get8CharUser());  /* ~25A */
					}
					else
					{
						go = false;
					}

					if (go)
					{
						MFSTransaction rtv_cntr2 = new MFSFixedTransaction(data2.toString());
						rtv_cntr2.setActionMessage("Retrieving Container Info, Please Wait..."); //$NON-NLS-1$
						MFSComm.getInstance().execute(rtv_cntr2, this);
						rc = rtv_cntr2.getReturnCode();

						if (rc == 0)
						{
							updt_cntr(rtv_cntr2.getOutput());
							this.update(getGraphics());

							if (newCntr)
							{
								/* check to see if our country has an override quantity */
								int qty = 1;
								String overrideCfgQty = "CONTAINER," + this.fieldHeaderRec.getCtry().trim();  //$NON-NLS-1$
								String overrideqty = config.getConfigValue(overrideCfgQty);
								if (!overrideqty.equals(MFSConfig.NOT_FOUND))
								{
									if (!overrideqty.equals("")) //$NON-NLS-1$
									{
										qty = Integer.parseInt(overrideqty);
									}
								}

								label.determineContainerLabel(this.fieldHeaderRec,getCurrMctl(), /* ~01A */
										rtv_cntr2.getOutput(),qty,getParentFrame(), false, this);
							}//end newcntr
						}
						else
						{
							errorString = rtv_cntr2.getOutput();
						}
					}
				}
				else if (cntrJD.getButtonPressed().equals("Delete")) //$NON-NLS-1$
				{
					MFSComponentRec cmp;
					int index = 0;
					boolean empty = true;

					if (cntrJD.getSelectedCntr().equals("NEW       ")) //$NON-NLS-1$
					{
						errorString = "Invalid Container to Delete."; //$NON-NLS-1$
						rc = 10;
					}
					else
					{
						while ((index < this.fieldComponentListModel.size()) && empty)
						{
							cmp = this.fieldComponentListModel.getComponentRecAt(index);
							if ((cmp.getIdsp().equals("I") || cmp.getIdsp().equals("R")) //$NON-NLS-1$ //$NON-NLS-2$
									&& (cmp.getCntr().equals(cntrJD.getSelectedCntr())))
							{
								empty = false;
							}
							index++;
						}

						if (empty)
						{
							StringBuffer data3 = new StringBuffer();
							data3.append("RTV_CNTR  "); //$NON-NLS-1$
							data3.append(getCurrMctl());
							data3.append("D"); //$NON-NLS-1$
							data3.append(cntrJD.getSelectedCntr());
							data3.append(config.get8CharUser());  /* ~25A */



							MFSTransaction rtv_cntr3 = new MFSFixedTransaction(data3.toString());
							rtv_cntr3.setActionMessage("Deleting Container, Please Wait..."); //$NON-NLS-1$
							MFSComm.getInstance().execute(rtv_cntr3, this);
							rc = rtv_cntr3.getReturnCode();

							if (rc == 0)
							{
								if (cntrJD.getSelectedCntr().equals(this.fieldHeaderRec.getCntr())
										&& !rtv_cntr3.getOutput().equals("")) //$NON-NLS-1$
								{
									StringBuffer data4 = new StringBuffer();
									data4.append("RTV_CNTR  "); //$NON-NLS-1$
									data4.append(getCurrMctl());
									data4.append("A"); //$NON-NLS-1$
									data4.append(rtv_cntr3.getOutput());
									data4.append(config.get8CharUser());  /* ~25A */

									MFSTransaction rtv_cntr4 = new MFSFixedTransaction(data4.toString());
									rtv_cntr4.setActionMessage("Assigning Container, Please Wait..."); //$NON-NLS-1$
									MFSComm.getInstance().execute(rtv_cntr4, this);
									rc = rtv_cntr4.getReturnCode();

									if (rc == 0)
									{
										updt_cntr(rtv_cntr4.getOutput());
										this.update(getGraphics());
									}
									else
									{
										errorString = rtv_cntr4.getOutput();
									}
								}
								else if (cntrJD.getSelectedCntr().equals(this.fieldHeaderRec.getCntr()))
								{
									updt_cntr("          "); //$NON-NLS-1$
									this.update(getGraphics());
								}
							}
							else
							{
								errorString = rtv_cntr3.getOutput();
							}
						}
						else
						{
							errorString = "You cannot Delete this Container because there are parts installed in it."; //$NON-NLS-1$
							rc = 10;
						}
					}
				}
				else if (cntrJD.getButtonPressed().equals("Reprint")) //$NON-NLS-1$
				{
					if (cntrJD.getSelectedCntr().equals("NEW       ")) //$NON-NLS-1$
					{
						errorString = "Invalid Container to Reprint."; //$NON-NLS-1$
						rc = 10;
					}
					else
					{
						/* check to see if our country has an override quantity */
						int qty = 1;
						String overrideCfgQty = "CONTAINER," + this.fieldHeaderRec.getCtry().trim(); //$NON-NLS-1$
						String overrideqty = config.getConfigValue(overrideCfgQty);
						if (!overrideqty.equals(MFSConfig.NOT_FOUND))
						{
							if (!overrideqty.equals("")) //$NON-NLS-1$
							{
								qty = Integer.parseInt(overrideqty);
							}
						}

						label.determineContainerLabel(this.fieldHeaderRec,getCurrMctl(), /* ~01A */
								cntrJD.getSelectedCntr(),qty,getParentFrame(), true, this);
					}
				}
			}
			else
			{
				errorString = rtv_cntr.getOutput();
			}

			if (rc != 0)
			{
				IGSMessageBox.showOkMB(getParentFrame(), null, errorString, null);
			}
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}

		addMyListeners();
		focusPNListOrButton(this.pbCntr);
	}

	/**
	 * Scrolls the scroll pane used to display the part instructions so that the
	 * specified <code>MFSPartInstructionJPanel</code> is visible.
	 * @param pip the <code>MFSPartInstructionJPanel</code> to make visible
	 */
	public void scrollToPip(MFSPartInstructionJPanel pip)
	{
		this.spPartsInst.requestFocusInWindow();
		if (this.fieldRowVector.contains(pip))
		{
			int y = (int) pip.getBounds().getY();
			this.spPartsInst.getVerticalScrollBar().setValue(y);
		}
	}

	/** 
	 * Sets the value of the blue row.
	 * @param blueRow the new blue row
	 */
	public void setBlueRow(int blueRow)
	{
		this.fieldBlueRow = blueRow;
	}
	

	//~16A New method
	/** Called by {@link #assignFocus()} to set the initial focus. */
	public void setInitialFocus()
	{

		this.spPartsInst.requestFocusInWindow();
		this.spPartsInst.getVerticalScrollBar().setValue(0);

		//If first row is a part instruction, select and focus the first part.
		//If first row is a nonpart instruction,
		//-set the blue row
		//-set the activeRow to the first part instruction
		//-assign the initial focus to a button
		//If no rows, assign the initial focus to a button
		if (this.fieldRowVector.size() > 0)
		{
			MFSPartInstructionJPanel pip = getRowVectorElementAt(0);
			if (pip.getIsNonPartInstruction() == false)
			{
				pip.getPNList().setSelectedIndex(0);
				pip.getPNList().requestFocusInWindow();
			}
			else
			{
				this.fieldBlueRow = 0;
				pip.changeColor(MFSPartInstructionJPanel.BLUE_ROW_COLOR);
				for (int i = 0; i < this.fieldRowVector.size(); i++)
				{
					MFSPartInstructionJPanel pip2 = getRowVectorElementAt(i);
					if (pip2.getIsNonPartInstruction() == false)
					{
						this.fieldActiveRow = i;
						this.fieldActiveRowIndex = 0;
						break;
					}
				}

				this.pbEnd.requestFocusInWindow();
			}
		}
		else
		{
			this.pbEnd.requestFocusInWindow();
		}
	}

	/** {@inheritDoc} */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void setupPartInstPanel()
	{
		try
		{
			boolean someInstructions = false;
			MFSInstructionRec shortInst = null;
			MFSInstructionRec tempInst = new MFSInstructionRec();
			Vector rowVector = new Vector();
			String curListSuff = ""; //$NON-NLS-1$
			String curListSeq = ""; //$NON-NLS-1$
			int instIndex = 0;
			boolean found = false;
			boolean nonPartInstFound = false;
			int yPos = 0;
			final MFSCP500Comparator comparator = new MFSCP500Comparator(); //~16A
			final String builderLevel = this.fieldHeaderRec.getBlvl(); //~16A

			getSource().startAction(this.SETUP_PART_INST_MSG); //~18C

			// make sure row panel has nuttin in it
			this.pnlRowHolder.removeAll();
			// make sure row vector has nuttin in it
			this.fieldRowVector.removeAllElements();

			//remove all traces of hide and show elements
			this.fieldHideRowVector.removeAllElements();
			this.fieldShowRowVector.removeAllElements();

			// if no instructions, use single line MFSComponentCellRenderer
			// otherwise use a multiLine MFSComponentCellRenderer
			if (this.fieldInstVector.size() != 0)
			{
				someInstructions = true;
			}

			//loop thru the vector of List Models and match up with the instructions in the instruction vector
			//2 indexs: listIndex and instIndex - we match up by suffix and sequence values
			int listIndex = 0;
			while (listIndex < this.fieldLmVector.size())
			{
				MFSComponentListModel listModel = getLmVectorElementAt(listIndex);

				nonPartInstFound = false;
				curListSuff = listModel.getSuff();
				curListSeq = listModel.getNmsq();

				//if short need to use multiLine renderer - see if we can find a matching instruction
				//in the instruction vector - if we don't find an instruction, we'll set up a dummy one with
				//blank suffix and sequence values.
				if (listModel.getIsShort() && !this.fieldHeaderRec.getNmbr().equals("FKIT")) //$NON-NLS-1$
				{
					someInstructions = true;
					found = false;
					for (int j = instIndex; j < this.fieldInstVector.size() && !found; j++)
					{
						MFSInstructionRec instRec = getInstVectorElementAt(j);
						if (instRec.getSuff().equals(curListSuff)
								&& instRec.getIseq().equals(curListSeq))
						{
							shortInst = instRec;
							instIndex = j + 1;
							found = true;
						}
					}
					if (!found)
					{
						shortInst = new MFSInstructionRec();
						shortInst.setSuff("          "); //$NON-NLS-1$
						shortInst.setIseq("     "); //$NON-NLS-1$
					}
				}

				if (curListSuff.equals("          ") && curListSeq.equals("     ")) //$NON-NLS-1$ //$NON-NLS-2$
				{
					if (someInstructions)
					{
						tempInst = new MFSInstructionRec();
						tempInst.setSuff("          "); //$NON-NLS-1$
						tempInst.setIseq("     "); //$NON-NLS-1$
					}
				}

				//create oursleves a new MFSPartInstructionJPanel
				//System.out.println("Creating new PartInstructionJPanel in setupPartInstPanel()...");
				MFSPartInstructionJPanel pip = new MFSPartInstructionJPanel(this);

				if ((!curListSuff.equals("          ") || !curListSeq.equals("     ")) //$NON-NLS-1$ //$NON-NLS-2$
						&& !getLmVectorElementAt(listIndex).getIsShort())
				{
					// We now have a suffix/sequence for the listModel. We will
					// first check to see if there are any non Part Instructions
					// that need to be displayed first, so we compare our list
					// Suffix/Seq and look thru instruction vector for one less
					// than that one
					nonPartInstFound = false;
					found = false;

					for (int i = instIndex; i < this.fieldInstVector.size() && !nonPartInstFound; i++)
					{
						//~16C Use MFSCP500Comparator to perform comparison
						MFSInstructionRec iRecI = getInstVectorElementAt(i);
						if (comparator.compare(iRecI, curListSeq + curListSuff) < 0)
						{
							//make sure this suffix/sequence isn't already
							// listed in the short parts section
							boolean alreadyListed = false;
							for (int q = 0; q < this.fieldLmVector.size() && !alreadyListed; q++)
							{
								MFSComponentListModel compLMQ = getLmVectorElementAt(q);
								String tmpSuff = compLMQ.getSuff();
								String tmpSeq = compLMQ.getNmsq();

								if (tmpSuff.equals(iRecI.getSuff())
										&& tmpSeq.equals(iRecI.getIseq())
										&& compLMQ.getIsShort())
								{
									alreadyListed = true;
								}
							}

							if (!alreadyListed)
							{
								instIndex = i + 1;
								nonPartInstFound = true;
								tempInst = iRecI;
							}
						}
					}

					//we didn't find a non part instruction so we'll now look
					//for a match on suffix sequence from the instruction vector
					if (!nonPartInstFound)
					{
						for (int j = instIndex; j < this.fieldInstVector.size() && !found; j++)
						{
							MFSInstructionRec iRecJ = getInstVectorElementAt(j);
							if (iRecJ.getSuff().equals(curListSuff)
									&& iRecJ.getIseq().equals(curListSeq))
							{
								tempInst = iRecJ;
								instIndex = j + 1;
								found = true;
							}
						}
						if (!found)
						{
							tempInst = new MFSInstructionRec();
							tempInst.setSuff(curListSuff);
							tempInst.setIseq(curListSeq);
						}
					}
				}//only do the nonPart check if we are not blank suffix and seq

				boolean last = false;
				if (listIndex == this.fieldLmVector.size() - 1
						&& instIndex == this.fieldInstVector.size())
				{
					last = true;
				}
				GridBagConstraints tmpConstraints = createPipConstraints(last, yPos++);

				if (getLmVectorElementAt(listIndex).getIsShort())
				{
					pip.configure(getLmVectorElementAt(listIndex), shortInst,
							someInstructions, builderLevel);
					pip.setName("Row" + curListSuff + curListSeq + "Short"); //$NON-NLS-1$ //$NON-NLS-2$
					listIndex++;
				}
				else
				{
					if (nonPartInstFound)
					{
						MFSComponentListModel emptyLm = new MFSComponentListModel();
						pip.setIsNonPartInstruction(true);
						pip.configure(emptyLm, tempInst, someInstructions, builderLevel);

						MFSInstructionRec iRecName = getInstVectorElementAt(instIndex - 1);
						pip.setName("Row" + iRecName.getSuff() + iRecName.getIseq()); //$NON-NLS-1$
					}
					else
					{
						pip.configure(getLmVectorElementAt(listIndex), tempInst,
								someInstructions, builderLevel);
						pip.setName("Row" + curListSuff + curListSeq); //$NON-NLS-1$
						listIndex++;
					}
				}
				//add this row to our RowVector
				rowVector.addElement(pip);

				//add listeners to the list
				pip.getPNList().addListSelectionListener(this);
				pip.getPNList().addKeyListener(this);
				pip.getPNList().addMouseListener(this);

				this.pnlRowHolder.add(pip, tmpConstraints);
			}/* end of while loop */

			// clean up any other instructions from the instrVector
			// there may be more instructions that have not been inserted into the display panel
			for (int i = instIndex; i < this.fieldInstVector.size(); i++)
			{
				tempInst = getInstVectorElementAt(i);
				MFSPartInstructionJPanel pip = new MFSPartInstructionJPanel(this);
				MFSComponentListModel emptyLm = new MFSComponentListModel();

				pip.setIsNonPartInstruction(true);
				pip.configure(emptyLm, tempInst, someInstructions, builderLevel);

				pip.setName("Row" + tempInst.getSuff() + tempInst.getIseq()); //$NON-NLS-1$

				boolean last = false;
				if (i == this.fieldInstVector.size() - 1)
				{
					last = true;
				}

				GridBagConstraints tmpConstraints = createPipConstraints(last, yPos++);
				rowVector.addElement(pip);
				this.pnlRowHolder.add(pip, tmpConstraints);
			}

			this.fieldRowVector = rowVector;
			this.fieldShowRowVector = rowVector;
		}
		catch (Exception e)
		{
			this.setCursor(Cursor.getDefaultCursor());
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
		finally
		{
			getSource().stopAction(); //~18C
		}
	}
	/**
	 * Suspends a work unit.
	 * ~2C added the EFFICIENCYON label, when present skip the RTV_QC call and default the suspend code to blanks
	 * @param ecod the end code. Should be 'S' for suspend
	 * @param oemo the operation ending mode
	 */
	public void suspend(String ecod, String oemo)
	{
		final MFSConfig config = MFSConfig.getInstance();	//~2a
		boolean stillVisible = true; //~24A
		int rc = 0;
		String suspendCode="";	//~2a //$NON-NLS-1$
		try
		{
			removeMyListeners();

			if (!config.containsConfigEntry("EFFICIENCYON")) //~2a //$NON-NLS-1$
			{
				DefaultListModel suspendCodeListModel = new DefaultListModel();
				{
					rc = MFSSuspendCodeDialog.loadSuspendCodes(suspendCodeListModel, this,
							getParentFrame());
				}
				if (rc == 0)
				{
					MFSSuspendCodeDialog suspendCodeJD = new MFSSuspendCodeDialog(
							getParentFrame(), suspendCodeListModel);
					suspendCodeJD.setLocationRelativeTo(getParentFrame()); //~17A
					suspendCodeJD.setVisible(true);

					if (suspendCodeJD.getProceedSelected())
					{
						suspendCode = suspendCodeJD.getSelectedListOption().substring(0, 2);
					}
				}
			}
			else
			{
				suspendCode = "  "; //$NON-NLS-1$
			}

			if (rc == 0)
			{
				rc = end_wu(ecod, suspendCode, oemo);
				if (rc == 0 || rc == 31)
				{
					this.fieldEndCode = 0;
					this.setCurrentCompRec(null);
					this.fieldInstVector.removeAllElements();
					this.fieldComponentListModel.removeAllElements();
					restorePreviousScreen();
					stillVisible = false; //~24A
				}					
			}
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}

		if (stillVisible)
		{
			focusPNListOrButton(this.pbSuspend);
		}
		addMyListeners();
	}

	/**
	 * Called to undo the current record - issued by clicking undo button
	 */
	public void undo()
	{
		try
		{
			String erms = ""; //$NON-NLS-1$
			removeMyListeners();
			int rc = 0;
			
			if (this.fieldComponentListModel.size() == 0)
			{
				/* display error to user */
				rc = -1;
				erms = "Nothing to Undo!"; //$NON-NLS-1$
			}
			else
			{	
				if (this.fieldCurrentCompRec == null)
				{
					/* display error to user */
					rc = -2;
					erms = "No Parts Selected!"; //$NON-NLS-1$
				}		
				if(rc == 0)
				{	
					MFSComponentRec currentComp = this.fieldCurrentCompRec; /*just to get it initialized*/
						
					if(currentComp.getPari().equals("D")) //$NON-NLS-1$
					{
						if(!currentComp.getIdsp().equals("D")) //$NON-NLS-1$
						{
							rc = -4;
							erms = "Invalid to Undo - IDSP = " + currentComp.getIdsp(); //$NON-NLS-1$
						}
						else
						{	
							handleDrawer(currentComp.getCwun(),UNDO);
						}	
					}
					else
					{
						if(!currentComp.getIdsp().equals("I")) //$NON-NLS-1$
						{
							rc = -4;
							erms = "Invalid to Undo - IDSP = " + currentComp.getIdsp(); //$NON-NLS-1$
						}
						else
						{
							StringBuffer sb = new StringBuffer();
							sb.append("<REC><MCTL>" + currentComp.getMctl() + "</MCTL><CRCT>" + currentComp.getCrct()+ "</CRCT><COOC>" + currentComp.getCooc() + "</COOC><ACTION>UNDO</ACTION></REC>"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
							issueLogRedPart(sb.toString(),UNDO);
						}	
					}/*end of scanned in work unit*/
				}	
			}/*end of Drawer Remove*/
			if(rc != 0)
			{
				IGSMessageBox.showOkMB(getParentFrame(), null, erms, null);
			}
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}

		addMyListeners();
		focusPNListOrButton(this.pbUndo);
	}
	
	/**
	 * Calls the UPDT_INSTR transaction
	 * @param status 'C'omplete or 'S'uspend
	 * @return 0 on success; nonzero on error
	 */
	public int updt_instr(String status)
	{
		int rc = 0;

		try
		{
			int index = 0;
			final MFSConfig config = MFSConfig.getInstance();

			// ~2 do not call UPDT_INSTR
			if (!config.containsConfigEntry("EFFICIENCYON*DUMBCLIENT")) //$NON-NLS-1$
			{
				MfsXMLDocument xml_header = new MfsXMLDocument("UPDT_INSTR"); //$NON-NLS-1$
				xml_header.addOpenTag("DATA"); //$NON-NLS-1$
				xml_header.addCompleteField("MCTL", this.fieldHeaderRec.getMctl()); //$NON-NLS-1$
				xml_header.addCompleteField("NMBR", this.fieldHeaderRec.getNmbr()); //$NON-NLS-1$
				xml_header.addCompleteField("USER", config.get8CharUser()); //$NON-NLS-1$
				xml_header.addCompleteField("CELL", config.get8CharCell()); //$NON-NLS-1$
				xml_header.addCompleteField("CTYP", config.get8CharCellType()); //$NON-NLS-1$
	
				index = 0;
				int counter = 0;
	
				MfsXMLDocument xml_data1 = new MfsXMLDocument();
	
				while (index < this.fieldRowVector.size() && rc == 0)
				{
					MFSPartInstructionJPanel pip = getRowVectorElementAt(index);
					// if the status has changed or (we are complete && (we have
					// only red parts left || we have a non part
					// instruction and we are in hide mode and the instruction class
					// is blank) )
					if (pip.getInstructionRec().getChanged() ||
						(status.equals("C") && //$NON-NLS-1$
						((pip.onlyRedPartsLeft() && !pip.getInstructionRec().getCompletionStatusOriginal().equals("C")) || //$NON-NLS-1$
						(pip.getIsNonPartInstruction() && pip.getInstructionRec().getInstrClass().equals(" "))))) //$NON-NLS-1$
					{
						xml_data1.addOpenTag("RCD"); //$NON-NLS-1$
						xml_data1.addCompleteField("INSX", pip.getInstructionRec().getSuff()); //$NON-NLS-1$
						xml_data1.addCompleteField("NMSQ", pip.getInstructionRec().getIseq()); //$NON-NLS-1$
	
						//if we only have red parts left then I'm going to call it complete
						if ((pip.onlyRedPartsLeft() && status.equals("C")) //$NON-NLS-1$
								|| (pip.getIsNonPartInstruction() && 
										pip.getInstructionRec().getInstrClass().equals(" "))) //$NON-NLS-1$
						{
							xml_data1.addCompleteField("ECOD", "C"); //$NON-NLS-1$ //$NON-NLS-2$
						}
						else
						{
							xml_data1.addCompleteField("ECOD", pip.getInstructionRec().getCompletionStatus()); //$NON-NLS-1$
						}
						xml_data1.addCompleteField("SCOD", " "); //$NON-NLS-1$ //$NON-NLS-2$
						xml_data1.addCloseTag("RCD"); //$NON-NLS-1$
						counter++;
					}
					index++;
	
					//if we run into our max buffer size of 47, or we just need to send the first time, we will
					//actually send an entirely new transactions. So we keep the header in each send
					if (counter == 47 || index == this.fieldRowVector.size() && counter > 0)
					{
						xml_data1.addCloseTag("DATA"); //$NON-NLS-1$
						xml_data1.finalizeXML();
	
						MFSTransaction updt_instr = new MFSXmlTransaction(xml_header.toString() + xml_data1.toString());
						updt_instr.setActionMessage("Updating Instructions, Please Wait..."); //$NON-NLS-1$
						MFSComm.getInstance().execute(updt_instr, this);
						rc = updt_instr.getReturnCode();
	
						if (rc != 0)
						{
							IGSMessageBox.showOkMB(getParentFrame(), null, updt_instr.getErms(), null);
						}
	
						counter = 0;
						xml_data1 = new MfsXMLDocument();
					}//end of time to send it
				}
			}
		}
		catch (Exception e)
		{
			rc = 11;
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
		return rc;
	}

	/**
	 * Invoked when an action occurs.
	 * @param ae the <code>ActionEvent</code>
	 */
	public void actionPerformed(ActionEvent ae)
	{
		//~14A Added try/catch
		try
		{
			final Object source = ae.getSource();
			this.ensureActiveRowIndexIsHighlighted();
			if (this.fieldBlueRow != -1)
			{
				getRowVectorElementAt(this.fieldBlueRow).unDoChangeColor();
			}

			if (source == this.pbEnd)
			{
				this.end_reduce();
			}
			else if (source == this.pbCntr)
			{
				this.rtv_cntr();
			}
			else if (source == this.pbSuspend)
			{
				this.suspend("S", "0000"); //$NON-NLS-1$ //$NON-NLS-2$
			}
			else if (source == this.pbMovePart)
			{
				this.removePartButtonExecuter();
			}
			else if (source == this.pbConvertDrawer)
			{
				this.removeDrawerButtonExecuter();
			}
			else if (source == this.pbUndo) // ~6A
			{
				this.undo();
			}
		}
		//~14A Added try/catch
		//~16C Modified try/catch to remove PTR35927JM code
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
			removeMyListeners();
			addMyListeners();
		}
	}

	/**
	 * Invoked when a key is pressed.
	 * @param ke the <code>KeyEvent</code>
	 */
	public void keyPressed(KeyEvent ke)
	{
		final int keyCode = ke.getKeyCode();
		final Object source = ke.getSource();
		
		//Make sure that before we do much of anything,
		//we have a part highlighted in the active row.
		//Only exception is when they press F2 (with no shift).
		if ((keyCode == KeyEvent.VK_F2 && !ke.isShiftDown() && !ke.isControlDown()) == false)
		{
			if (keyCode != KeyEvent.VK_PAGE_UP && keyCode != KeyEvent.VK_PAGE_DOWN)
			{
				this.ensureActiveRowIndexIsHighlighted();
				if (this.fieldBlueRow != -1)
				{
					getRowVectorElementAt(this.fieldBlueRow).unDoChangeColor();
				}
			}
		}

		if (keyCode == KeyEvent.VK_ENTER)
		{
			if (source instanceof MFSMenuButton)
			{
				MFSMenuButton button = (MFSMenuButton) source;
				button.requestFocusInWindow();
				button.doClick();
			}
		}
		else if (keyCode == KeyEvent.VK_F1)
		{
			if(ke.isShiftDown())
			{	
				this.pbUndo.requestFocusInWindow();
				this.pbUndo.doClick();
			}	
		}
		else if (keyCode == KeyEvent.VK_F3)
		{
			this.pbEnd.requestFocusInWindow();
			this.pbEnd.doClick();
		}
		else if (keyCode == KeyEvent.VK_F7)
		{
			this.pbSuspend.requestFocusInWindow();
			this.pbSuspend.doClick();
		}
		else if (keyCode == KeyEvent.VK_F9)
		{
			this.pbCntr.requestFocusInWindow();
			this.pbCntr.doClick();
		}
		else if (keyCode == KeyEvent.VK_F11)
		{
			this.pbMovePart.requestFocusInWindow();
			this.pbMovePart.doClick();
		}
		else if (keyCode == KeyEvent.VK_F12)
		{
			this.pbConvertDrawer.requestFocusInWindow();
			this.pbConvertDrawer.doClick();
		}
		else if (keyCode == KeyEvent.VK_PAGE_DOWN)
		{
			ke.consume();
			this.spPartsInst.requestFocusInWindow();

			final int rowVectorSize = this.fieldRowVector.size();

			//Blue Row
			if (this.fieldBlueRow != -1)
			{
				int index = this.fieldBlueRow;

				//if index >= rowVectorSize - 1
				//then the current PartInstructionJPanel is in the bottom row
				if (index >= rowVectorSize - 1)
				{
					getRowVectorElementAt(index).changeColor(
							MFSPartInstructionJPanel.BLUE_ROW_COLOR);
				}
				else
				{
					MFSPartInstructionJPanel pip = getRowVectorElementAt(index);
					pip.unDoChangeColor();

					index++;
					while (index < rowVectorSize)
					{
						pip = getRowVectorElementAt(index);

						if ((pip.getIsNonPartInstruction() )
								|| (pip.getIsNonPartInstruction() 
										&& (pip.getInstructionRec().getInstrClass().equals("A") //$NON-NLS-1$ 
												|| pip.getInstructionRec().getInstrClass().equals("M")))) //$NON-NLS-1$
						{
							this.fieldBlueRow = index;
							pip.changeColor(MFSPartInstructionJPanel.BLUE_ROW_COLOR);
							this.spPartsInst.getVerticalScrollBar().setValue((int) pip.getBounds().getY());
							break;
						}
						else if (!pip.getIsNonPartInstruction())
						{
							this.fieldBlueRow = -1;
							this.spPartsInst.getVerticalScrollBar().setValue((int) pip.getBounds().getY());
							pip.getPNList().setSelectedIndex(0);
							pip.getPNList().requestFocusInWindow();
							break;
						}
						index++;
					}//end of while loop

					//try to unhighlight the highlighted parts
					JList activePipPNList = getRowVectorElementAt(this.fieldActiveRow).getPNList();
					if (this.fieldBlueRow != -1)
					{
						this.fieldActiveRowIndex = activePipPNList.getSelectedIndex();
						activePipPNList.clearSelection();
					}
					activePipPNList.requestFocusInWindow();
				}//end of not bottom
			}
			//no blue row is currently set --> nothing to undo
			else
			{
				int index = this.fieldActiveRow;
				//if index >= rowVectorSize - 1
				//then the current PartInstructionJPanel is in the bottom row
				//so there is nothing to do
				if (index < rowVectorSize - 1)
				{
					index++;
					while (index < rowVectorSize)
					{
						MFSPartInstructionJPanel pip = getRowVectorElementAt(index);
						if ((pip.getIsNonPartInstruction())
								|| (pip.getIsNonPartInstruction() 
										&& (pip.getInstructionRec().getInstrClass().equals("A") //$NON-NLS-1$ 
												|| pip.getInstructionRec().getInstrClass().equals("M")))) //$NON-NLS-1$
						{
							this.fieldBlueRow = index;
							pip.changeColor(MFSPartInstructionJPanel.BLUE_ROW_COLOR);
							this.spPartsInst.getVerticalScrollBar().setValue((int) pip.getBounds().getY());
							break;
						}
						else if (!pip.getIsNonPartInstruction())
						{
							this.spPartsInst.getVerticalScrollBar().setValue((int) pip.getBounds().getY());
							pip.getPNList().setSelectedIndex(0);
							pip.getPNList().requestFocusInWindow();
							break;
						}
						index++;
					}//end of while loop

					//try to unhighlight the highlighted parts
					JList activePipPNList = getRowVectorElementAt(this.fieldActiveRow).getPNList();
					if (this.fieldBlueRow != -1)
					{
						this.fieldActiveRowIndex = activePipPNList.getSelectedIndex();
						activePipPNList.clearSelection();
					}
					activePipPNList.requestFocusInWindow();
				}//end of not bottom of screen
			}
		}
		else if (keyCode == KeyEvent.VK_PAGE_UP)
		{
			ke.consume();
			this.spPartsInst.requestFocusInWindow();

			if (this.fieldBlueRow != -1)
			{
				int index = this.fieldBlueRow;
				if (index <= 0)
				{
					getRowVectorElementAt(index).changeColor(MFSPartInstructionJPanel.BLUE_ROW_COLOR);
				}
				else
				{
					MFSPartInstructionJPanel pip = getRowVectorElementAt(index);
					pip.unDoChangeColor();

					index--;
					while (index >= 0)
					{
						pip = getRowVectorElementAt(index);
						if ((pip.getIsNonPartInstruction())
								|| (pip.getIsNonPartInstruction() 
										&& (pip.getInstructionRec().getInstrClass().equals("A") //$NON-NLS-1$ 
												|| pip.getInstructionRec().getInstrClass().equals("M")))) //$NON-NLS-1$

						{
							this.fieldBlueRow = index;
							this.spPartsInst.getVerticalScrollBar().setValue((int) pip.getBounds().getY());
							pip.changeColor(MFSPartInstructionJPanel.BLUE_ROW_COLOR);
							break;
						}
						else if (!pip.getIsNonPartInstruction())
						{
							this.fieldBlueRow = -1;
							this.spPartsInst.getVerticalScrollBar().setValue((int) pip.getBounds().getY());
							pip.getPNList().setSelectedIndex(0);
							pip.getPNList().requestFocusInWindow();
							break;
						}
						index--;
					}//end of loop

					//try to unhighlight the highlighted parts
					JList activePipPNList = getRowVectorElementAt(this.fieldActiveRow).getPNList();
					if (this.fieldBlueRow != -1)
					{
						this.fieldActiveRowIndex = activePipPNList.getSelectedIndex();
						activePipPNList.clearSelection();
					}
					activePipPNList.requestFocusInWindow();
				}//end of top at top
			}
			//no blue row set --> nothing to undo
			else
			{
				int index = this.fieldActiveRow;
				//if index <= 0
				//then the current PartInstructionJPanel is in the top row
				//so there is nothing to do
				if (index > 0)
				{
					index--;
					while (index >= 0)
					{
						MFSPartInstructionJPanel pip = getRowVectorElementAt(index);

						if ((pip.getIsNonPartInstruction())
								|| (pip.getIsNonPartInstruction() 
										&& (pip.getInstructionRec().getInstrClass().equals("A") //$NON-NLS-1$ 
												|| pip.getInstructionRec().getInstrClass().equals("M")))) //$NON-NLS-1$
						{
							this.fieldBlueRow = index;
							this.spPartsInst.getVerticalScrollBar().setValue((int) pip.getBounds().getY());
							pip.changeColor(MFSPartInstructionJPanel.BLUE_ROW_COLOR);
							break;
						}
						else if (!pip.getIsNonPartInstruction())
						{
							this.spPartsInst.getVerticalScrollBar().setValue((int) pip.getBounds().getY());
							pip.getPNList().setSelectedIndex(0);
							pip.getPNList().requestFocusInWindow();
							break;
						}
						index--;
					}//end of while loop

					//try to unhighlight the highlighted parts
					JList activePipPNList = getRowVectorElementAt(this.fieldActiveRow).getPNList();
					if (this.fieldBlueRow != -1)
					{
						this.fieldActiveRowIndex = activePipPNList.getSelectedIndex();
						activePipPNList.clearSelection();
					}
					activePipPNList.requestFocusInWindow();
				}//end of not at top of list
			}
		}
		else if (keyCode == KeyEvent.VK_DOWN)
		{
			JList tempList = getRowVectorElementAt(this.fieldActiveRow).getPNList();
			tempList.requestFocusInWindow();

			if (this.fieldBlueRow != -1)
			{
				getRowVectorElementAt(this.fieldBlueRow).unDoChangeColor();
				this.fieldBlueRow = -1;
				tempList.setSelectedIndex(0);
				ke.consume();
			}
			else if (tempList.getSelectedIndex() == tempList.getModel().getSize() - 1)
			{
				ke.consume();
				for (int i = this.fieldActiveRow + 1; i < this.fieldRowVector.size(); i++)
				{
					MFSPartInstructionJPanel pip = getRowVectorElementAt(i);
					if (pip.getIsNonPartInstruction() == false)
					{
						pip.getPNList().setSelectedIndex(0);
						pip.getPNList().requestFocusInWindow();
						pip.getPNList().ensureIndexIsVisible(0);
						break;
					}
				}
			}
		}
		else if (keyCode == KeyEvent.VK_UP)
		{
			JList tempList = getRowVectorElementAt(this.fieldActiveRow).getPNList();
			tempList.requestFocusInWindow();

			if (this.fieldBlueRow != -1)
			{
				getRowVectorElementAt(this.fieldBlueRow).unDoChangeColor();
				this.fieldBlueRow = -1;
				//highlight last part in part list and consume this event
				tempList.setSelectedIndex(tempList.getModel().getSize() - 1);
				ke.consume();
			}

			else if (tempList.getSelectedIndex() == 0)
			{
				for (int i = this.fieldActiveRow - 1; i >= 0; i--)
				{
					ke.consume();
					MFSPartInstructionJPanel pip = getRowVectorElementAt(i);
					if (pip.getIsNonPartInstruction() == false)
					{
						ListModel tmpLm = pip.getPNList().getModel();
						int selectedIndex = tmpLm.getSize() - 1;
						pip.getPNList().setSelectedIndex(selectedIndex);
						pip.getPNList().requestFocusInWindow();
						pip.getPNList().ensureIndexIsVisible(selectedIndex);
						break;
					}
				}
			}
		}
	}

	/**
	 * Invoked when the mouse has been pressed on a <code>Component</code>.
	 * @param me the <code>MouseEvent</code>
	 */
	public void mouseClicked(MouseEvent me)
	{
		this.ensureActiveRowIndexIsHighlighted();
		if (this.fieldBlueRow != -1)
		{
			getRowVectorElementAt(this.fieldBlueRow).unDoChangeColor();
		}
		if (me.getClickCount() == 2)
		{
			//TODO should a double click do anything Dave?
		}
	}

	/**
	 * Invoked when the mouse has been pressed on a <code>Component</code>.
	 * Does nothing.
	 * @param me the <code>MouseEvent</code>
	 */
	public void mousePressed(MouseEvent me)
	{
		//Does nothing
	}

	/**
	 * Invoked when the mouse has been released on a <code>Component</code>.
	 * Does nothing.
	 * @param me the <code>MouseEvent</code>
	 */
	public void mouseReleased(MouseEvent me)
	{
		//Does nothing
	}

	/**
	 * Invoked when the mouse enters a <code>Component</code>. Does nothing.
	 * @param me the <code>MouseEvent</code>
	 */
	public void mouseEntered(MouseEvent me)
	{
		//Does nothing
	}

	/**
	 * Invoked when the mouse exits a <code>Component</code>. Does nothing.
	 * @param me the <code>MouseEvent</code>
	 */
	public void mouseExited(MouseEvent me)
	{
		//Does nothing
	}
	
	//~04A
	/**
	 * Verfify the Hazmat Labeling Part Numbers
	 */
	private boolean vfyHazmatLabeling(String ecod)
	{
		boolean proceed = true;
		
		try
		{
			if(MFSHazmatLabeling.isHazmatTrigger(fieldHeaderRec.getNmbr(), fieldHeaderRec.getPrln()))
			{		
				String mode = "PP"; // ProdPack	
				
				MFSHazmatLabeling hazmatLbl = new MFSHazmatLabeling(getParentFrame(), this);
				
				StringBuilder paramBuffer = new StringBuilder();
				paramBuffer.append("<CMCT>"); 
				paramBuffer.append(getCurrMctl()); 
				paramBuffer.append("</CMCT>");
				paramBuffer.append("<ECOD>");
				paramBuffer.append(ecod);
				paramBuffer.append("</ECOD>");
				paramBuffer.append("<NMBR>"); 
				paramBuffer.append(this.fieldHeaderRec.getNmbr());
				paramBuffer.append("</NMBR>"); 
				paramBuffer.append("<OLEV>");
				paramBuffer.append(this.fieldHeaderRec.getOlev());
				paramBuffer.append("</OLEV>");
				
				hazmatLbl.setTriggerSource("ENDWU");
				hazmatLbl.setOptionalParameters(paramBuffer.toString());
				
				Set<String> cntrSet = new HashSet<String>();
				int componentSize = this.fieldComponentListModel.getSize();
				
				for(int componentIndex = 0; componentIndex < componentSize; componentIndex++)
				{
					MFSComponentRec component = this.fieldComponentListModel.getComponentRecAt(componentIndex);

					if(null != component.getCntr() && !component.getCntr().trim().equals(""))
					{
						cntrSet.add(component.getCntr());
					}
				}
				
				// case no components are installed in this operation but a cntr is assigned
				if(null != fieldHeaderRec.getCntr() && !fieldHeaderRec.getCntr().trim().equals(""))
				{
					cntrSet.add(fieldHeaderRec.getCntr());
				}
				
				String[] cntrs = new String[cntrSet.size()];
				proceed = hazmatLbl.hazmatLabeling(Arrays.asList(cntrSet.toArray(cntrs)), mode);
			}
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, e.getMessage(), null);
			proceed = false;
		}	
		
		return proceed;
	}
}
