/* © Copyright IBM Corporation 2006. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2006-06-09   ~1          R Prechel        -PartInstructionJPanel constructor change
 *                                           -Removed unused variables, methods, and imports
 *                                           -Implemented PartInstructionDisplayer
 * 2007-02-28   ~2 34242JR  R Prechel        -Java 5 version
 *                                           -Inherit from MFSInstructionsPanel
 *                                           -GUI layout redone in MFSInstructionsPanel for PTR34177JR
 * 2007-04-09   ~3 38176JM  R Prechel        -Fix MFS Client hang
 * 2008-01-21	~4 39782JM	Martha Luna		 -Delete reference of hasPlugToOrSwitchSetting() method
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.panel;

import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListModel;
import javax.swing.SwingConstants;

import com.ibm.rchland.mfgapps.client.utils.layout.IGSGridLayout;
import com.ibm.rchland.mfgapps.client.utils.messagebox.IGSMessageBox;
import com.ibm.rchland.mfgapps.mfsclient.MFSFrame;
import com.ibm.rchland.mfgapps.mfsclient.MFSInstructionsPanel;
import com.ibm.rchland.mfgapps.mfsclient.MFSMenuButton;
import com.ibm.rchland.mfgapps.mfsclient.MFSPanel;
import com.ibm.rchland.mfgapps.mfsclient.MFSPartInstructionJPanel;
import com.ibm.rchland.mfgapps.mfsclient.dialog.rework.MFSRwkDialog;
import com.ibm.rchland.mfgapps.mfsclient.struct.MFSComponentListModel;
import com.ibm.rchland.mfgapps.mfsclient.struct.MFSComponentRec;
import com.ibm.rchland.mfgapps.mfsclient.struct.MFSInstructionRec;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSCP500Comparator;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSConstants;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSRework;
import com.ibm.rchland.mfgapps.mfscommon.MFSConfig;

/**
 * <code>MFSViewOpsPanel</code> is the <code>MFSInstructionsPanel</code>
 * used to view the <code>MFSPartInstructionJPanel</code>s for the operations
 * for a given operation level.
 * @author The MFS Client Development Team
 */
public class MFSViewOpsPanel
	extends MFSInstructionsPanel
{
	private static final long serialVersionUID = 1L;
	/** The default screen name of a <code>MFSViewOpsPanel</code>. */
	public static final String SCREEN_NAME = "View Selected Operation";

	/** The Dir Work (F3) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbDirWork = MFSMenuButton.createMediumButton("Dir Work",
			"bldF3.gif", "Dir Work", null); //$NON-NLS-1$

	/** The Rework (F6) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbRework = MFSMenuButton.createMediumButton("Rework",
			"rewrkF6.gif", "Rework", null); //$NON-NLS-1$

	/** The Return (F12) <code>MFSMenuButton</code>. */
	private MFSMenuButton pbReturn = MFSMenuButton.createMediumButton("Return",
			"viewF12.gif", "Return", null); //$NON-NLS-1$

	/**
	 * Constructs a new <code>MFSViewOpsPanel</code>.
	 * @param parent the <code>MFSFrame</code> used to display this panel
	 * @param source the <code>MFSPanel</code> that caused this panel to be displayed
	 */
	public MFSViewOpsPanel(MFSFrame parent, MFSPanel source)
	{
		super(parent, source, SCREEN_NAME);
		this.pnlButtons.setLayout(new IGSGridLayout(2, 2, 5, 5));
		this.pnlButtons.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
		this.addMyListeners();
	}

	/** Adds the listeners to this panel's <code>Component</code>s. */
	protected void addMyListeners()
	{
		this.pbDirWork.addActionListener(this);
		this.pbRework.addActionListener(this);
		this.pbReturn.addActionListener(this);

		this.spPartsInst.addKeyListener(this);
		this.pbDirWork.addKeyListener(this);
		this.pbRework.addKeyListener(this);
		this.pbReturn.addKeyListener(this);
	}

	//~2A New method
	/** {@inheritDoc} */
	public void assignFocus()
	{
		this.spPartsInst.requestFocusInWindow();
		this.spPartsInst.getVerticalScrollBar().setValue(0);

		if (this.getSource() instanceof MFSSelWrkExtFnctPanel)
		{
			if (this.fieldRowVector.size() > 0)
			{
				MFSPartInstructionJPanel panel = getRowVectorElementAt(0);
				if (panel.getIsNonPartInstruction())
				{
					this.fieldActiveRow = 0;
				}
				else
				{
					panel.getPNList().setSelectedIndex(0);
					panel.getPNList().requestFocusInWindow();
				}
			}
			this.pbReturn.requestFocusInWindow();
		}
		else if (this.getSource() instanceof MFSDirWrkExtFnctPanel)
		{
			int index = 0;
			boolean found = false;
			while (index < this.fieldRowVector.size() && !found)
			{
				MFSPartInstructionJPanel panel = getRowVectorElementAt(index);
				if (panel.getIsNonPartInstruction() == false)
				{
					found = true;
					panel.getPNList().setSelectedIndex(0);
					panel.getPNList().requestFocusInWindow();
				}
				index++;
			}

			if (!found)
			{
				this.spPartsInst.requestFocusInWindow();
			}
		}
		else
		{
			this.spPartsInst.requestFocusInWindow();
			new RuntimeException("Bad condition: source = " + this.getSource()).printStackTrace();
		}
	}

	/** Determines which buttons are displayed in the menu. */
	protected void configureButtons()
	{
		this.pnlButtons.removeAll();
		//~2C Add pbDirWork and pbRework if previous screen
		// was NOT MFSSelWrkExtFnctPanel
		if (getSource() instanceof MFSSelWrkExtFnctPanel)
		{
			this.pbDirWork.setEnabled(false);
			this.pbRework.setEnabled(false);
		}
		else
		{
			this.pnlButtons.add(this.pbDirWork);
			this.pbDirWork.setEnabled(true);

			this.pnlButtons.add(this.pbRework);
			this.pbRework.setEnabled(true);
		}

		this.pnlButtons.add(this.pbReturn);
		this.pbReturn.setEnabled(true);
	}

	//~2A New method
	/**
	 * Creates a header <code>JLabel</code>.
	 * @return the new header <code>JLabel</code>
	 */
	protected JLabel createHeaderLabel()
	{
		JLabel label = new JLabel(" ", null, SwingConstants.CENTER); //$NON-NLS-1$
		label.setFont(MFSConstants.MEDIUM_DIALOG_FONT);
		label.setForeground(MFSConstants.PRIMARY_FOREGROUND_COLOR);
		return label;
	}

	/**
	 * Loads the separate list models that have the correct conditions (shorted
	 * parts, no suffix/seq, like suffix/seq, etc.) from the
	 * {@link MFSComponentListModel}.<br>
	 * Precondition: The {@link MFSComponentListModel} for this panel must be
	 * full of components. This {@link MFSComponentListModel} is loaded by the
	 * {@link #loadListModel(String)} method.
	 */
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

				MFSComponentRec next = getComponentListModelCompRecAt(index);

				// handle all the shorted parts
				if (next.isShortPart()) //$NON-NLS-1$ //$NON-NLS-2$
				{
					shorted = true;
					curLm = new MFSComponentListModel();
					curLm.addElement(next);

					index++;
					while (index < this.fieldComponentListModel.size() && shorted)
					{
						next = getComponentListModelCompRecAt(index);
						if (next.isShortPart())
						{
							curLm.addElement(next);
							index++;
						}
						else
						{
							shorted = false;
						}
					}
					curLm.setIsShort(true);
					this.fieldLmVector.add(curLm);
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
							this.fieldLmVector.add(curLm);
							curLm = new MFSComponentListModel();
							curLm.setSuff(curSuffix);
							curLm.setNmsq(curSeq);
							curLm.addElement(next);

						}
						index++;
					}
					//add last lm to the vector
					this.fieldLmVector.add(curLm);
				}
			}// end size() > 0
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
	}

	//~2A New method
	/**
	 * Prepares the panel for display by setting the text of the header labels.
	 * @param mctl the mctl of the work unit for which instructions are being displayed
	 */
	public void prepareForDisplay(String mctl)
	{
		this.lblUser.setText("User:  " + MFSConfig.getInstance().getConfigValue("USER")); //$NON-NLS-2$
		this.lblMctl.setText("Mctl:  " + mctl);
		updt_cntr(this.fieldHeaderRec.getCntr());
		this.initHeaderLabels();
	}

	/** Removes the listeners from this panel's <code>Component</code>s. */
	protected void removeMyListeners()
	{
		this.pbDirWork.removeActionListener(this);
		this.pbRework.removeActionListener(this);
		this.pbReturn.removeActionListener(this);

		this.spPartsInst.removeKeyListener(this);
		this.pbDirWork.removeKeyListener(this);
		this.pbRework.removeKeyListener(this);
		this.pbReturn.removeKeyListener(this);
	}

	/** Starts the Rework process for the active part. */
	public void rwk_part()
	{
		try
		{
			removeMyListeners();

			if (this.fieldRowVector.size() == 0)
			{
				String erms = "No Parts To Rework!";
				IGSMessageBox.showOkMB(this, null, erms, null);
			}
			else
			{
				if (getRowVectorElementAt(this.fieldActiveRow).getIsNonPartInstruction())
				{
					String erms = "Invalid Part to Rework";
					IGSMessageBox.showOkMB(this, null, erms, null);
				}
				else
				{
					JList tmpList = getRowVectorElementAt(this.fieldActiveRow).getPNList();
					MFSComponentListModel tmpLm = (MFSComponentListModel) tmpList.getModel();
					MFSComponentRec currComp = tmpLm.getComponentRecAt(tmpList.getSelectedIndex());
					if (!currComp.getIdsp().equals("I") //$NON-NLS-1$
							|| currComp.getMlri().equals(" ") //$NON-NLS-1$
							|| currComp.getMlri().equals("0")) //$NON-NLS-1$
					{
						String erms = "Invalid Part to Rework";
						IGSMessageBox.showOkMB(this, null, erms, null);
					}
					else
					{
						MFSRework rwk = new MFSRework(this, tmpList, tmpLm,
								this.fieldHeaderRec, currComp);
						rwk.rework(MFSRwkDialog.LF_PARTIAL);
					}
				}
			}
		}
		catch (Exception e)
		{
			this.setCursor(java.awt.Cursor.getDefaultCursor());
			IGSMessageBox.showOkMB(this, null, null, e);
		}

		addMyListeners();

		if (this.fieldRowVector.size() > 0)
		{
			MFSPartInstructionJPanel pip = getRowVectorElementAt(this.fieldActiveRow);
			if (!pip.getIsNonPartInstruction())
			{
				pip.getPNList().requestFocusInWindow();
			}
			else
			{
				this.pbReturn.requestFocusInWindow();
			}
		}
		else
		{
			this.pbReturn.requestFocusInWindow();
		}
	}

	/** {@inheritDoc} */
	@SuppressWarnings({ "rawtypes", "unchecked" })
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
			final MFSCP500Comparator comparator = new MFSCP500Comparator(); //~2A

			getSource().startAction(this.SETUP_PART_INST_MSG); //~3C

			// make sure row panel has nothing in it
			this.pnlRowHolder.removeAll();

			// if no instructions, use single line MFSComponentCellRenderer
			// otherwise use a multiLine MFSComponentCellRenderer
			if (this.fieldInstVector.size() != 0)
			{
				someInstructions = true;
			}

			int listIndex = 0;
			while (listIndex < this.fieldLmVector.size())
			{
				nonPartInstFound = false;

				//if short need to use multiLine MFSComponentCellRenderer
				if ((getLmVectorElementAt(listIndex)).getIsShort())
				{
					someInstructions = true;
					shortInst = new MFSInstructionRec();
					shortInst.setSuff("          "); //$NON-NLS-1$
					shortInst.setIseq("     "); //$NON-NLS-1$
				}

				curListSuff = getLmVectorElementAt(listIndex).getSuff();
				curListSeq = getLmVectorElementAt(listIndex).getNmsq();

				if (curListSuff.equals("          ") && curListSeq.equals("     ")) //$NON-NLS-1$ //$NON-NLS-2$
				{
					if (someInstructions)
					{
						tempInst = new MFSInstructionRec();
						tempInst.setSuff("          "); //$NON-NLS-1$
						tempInst.setIseq("     "); //$NON-NLS-1$
					}
				}

				MFSPartInstructionJPanel pip = new MFSPartInstructionJPanel(this);

				if (!curListSuff.equals("          ") || !curListSeq.equals("     ")) //$NON-NLS-1$ //$NON-NLS-2$
				{
					//see if we can find a matching suffix seq
					nonPartInstFound = false;
					found = false;

					for (int i = instIndex; i < this.fieldInstVector.size() && !nonPartInstFound; i++)
					{
						//~2C Use MFSCP500Comparator to perform comparison
						MFSInstructionRec iRecI = getInstVectorElementAt(i);
						if (comparator.compare(iRecI, curListSeq + curListSuff) < 0)
						{
							instIndex = i + 1;
							nonPartInstFound = true;
							tempInst = iRecI;
						}
					}

					if (!nonPartInstFound)
					{
						for (int j = instIndex; j < this.fieldInstVector.size() && !found; j++)
						{
							MFSInstructionRec iRecJ = getInstVectorElementAt(j);
							if (iRecJ.getSuff().equals(curListSuff) && iRecJ.getIseq().equals(curListSeq))
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
							someInstructions, getHeaderRec().getBlvl());
					pip.setName("RowShort");
					listIndex++;
				}
				else
				{
					if (nonPartInstFound)
					{
						pip.setIsNonPartInstruction(true);
						MFSComponentListModel emptyLm = new MFSComponentListModel();
						pip.configure(emptyLm, tempInst, someInstructions, getHeaderRec().getBlvl());

						MFSInstructionRec iRecName = getInstVectorElementAt(instIndex - 1);
						pip.setName("Row" + iRecName.getSuff() + iRecName.getIseq());

					}
					else
					{
						pip.configure(getLmVectorElementAt(listIndex), tempInst,
								someInstructions, getHeaderRec().getBlvl());
						pip.setName("Row" + curListSuff + curListSeq);
						listIndex++;
					}
				}
				rowVector.add(pip);

				pip.getPNList().addListSelectionListener(this);
				pip.getPNList().addKeyListener(this);

				this.pnlRowHolder.add(pip, tmpConstraints);

			}/* end of while loop */

			//clean up any other instructions from the instrVector
			for (int i = instIndex; i < this.fieldInstVector.size(); i++)
			{
				tempInst = getInstVectorElementAt(i);
				MFSPartInstructionJPanel pip = new MFSPartInstructionJPanel(this);
				MFSComponentListModel emptyLm = new MFSComponentListModel();

				pip.configure(emptyLm, tempInst, someInstructions, getHeaderRec().getBlvl());
				pip.setName("Row" + tempInst.getSuff() + tempInst.getIseq());

				boolean last = false;
				if (i == this.fieldInstVector.size() - 1)
				{
					last = true;
				}
				GridBagConstraints tmpConstraints = createPipConstraints(last, yPos++);
				rowVector.add(pip);
				this.pnlRowHolder.add(pip, tmpConstraints);
			}
			this.fieldRowVector = rowVector;
		}
		catch (Exception e)
		{
			this.setCursor(Cursor.getDefaultCursor());
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
		finally
		{
			getSource().stopAction(); //~3C
		}
	}

	/**
	 * Invoked when an action occurs.
	 * @param ae the <code>ActionEvent</code>
	 */
	public void actionPerformed(ActionEvent ae)
	{
		final Object source = ae.getSource();
		if (source == this.pbReturn)
		{
			getParentFrame().restorePreviousScreen(this);
		}
		else if (source == this.pbRework)
		{
			rwk_part();
		}
		else if (source == this.pbDirWork)
		{
			getParentFrame().restorePreviousScreen(this.getSource());
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
			if (source instanceof MFSMenuButton)
			{
				MFSMenuButton button = (MFSMenuButton) source;
				button.requestFocusInWindow();
				button.doClick();
			}
		}
		else if (keyCode == KeyEvent.VK_F3)
		{
			if (this.pbDirWork.isEnabled())
			{
				this.pbDirWork.requestFocusInWindow();
				this.pbDirWork.doClick();
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
			ke.consume();
			this.spPartsInst.requestFocusInWindow();

			//Blue Row
			if (this.fieldBlueRow != -1)
			{
				int index = this.fieldBlueRow;
				//if index == rowVectorSize - 1
				//then the current PartInstructionJPanel is in the bottom row
				//so there is nothing to do
				if (index < this.fieldRowVector.size() - 1)
				{
					MFSPartInstructionJPanel pip = getRowVectorElementAt(index);
					pip.unDoChangeColor();

					pip = getRowVectorElementAt(index + 1);
					int yPos = (int) pip.getBounds().getY();
					this.spPartsInst.getVerticalScrollBar().setValue(yPos);
					if (pip.getIsNonPartInstruction())
					{
						this.fieldBlueRow = index + 1;
						pip.changeColor(MFSPartInstructionJPanel.BLUE_ROW_COLOR);
					}
					else
					{
						this.fieldBlueRow = -1;
						//Highlight the first part
						pip.getPNList().setSelectedIndex(0);
					}
				}
			}
			//no blue row is currently set --> nothing to undo
			else
			{
				int index = this.fieldActiveRow;
				//if index == rowVectorSize - 1
				//then the current PartInstructionJPanel is in the bottom row
				//so there is nothing to do
				if (index < this.fieldRowVector.size() - 1)
				{
					MFSPartInstructionJPanel pip = getRowVectorElementAt(index + 1);
					int yPos = (int) pip.getBounds().getY();
					this.spPartsInst.getVerticalScrollBar().setValue(yPos);
					if (pip.getIsNonPartInstruction())
					{
						this.fieldBlueRow = index + 1;
						pip.changeColor(MFSPartInstructionJPanel.BLUE_ROW_COLOR);
					}
					else
					{
						//Highlight the first part
						pip.getPNList().setSelectedIndex(0);
					}
				}
			}
		}
		else if (keyCode == KeyEvent.VK_PAGE_UP)
		{
			ke.consume();
			this.spPartsInst.requestFocusInWindow();

			//Blue Row
			if (this.fieldBlueRow != -1)
			{
				int index = this.fieldBlueRow;
				//if index == 0
				//then the current PartInstructionJPanel is in the top row
				//so there is nothing to do
				if (index > 0)
				{
					MFSPartInstructionJPanel pip = getRowVectorElementAt(index);
					pip.unDoChangeColor();

					pip = getRowVectorElementAt(index - 1);
					int yPos = (int) pip.getBounds().getY();
					this.spPartsInst.getVerticalScrollBar().setValue(yPos);
					if (pip.getIsNonPartInstruction())
					{
						this.fieldBlueRow = index - 1;
						pip.changeColor(MFSPartInstructionJPanel.BLUE_ROW_COLOR);
					}
					else
					{
						this.fieldBlueRow = -1;
						//Highlight the first part
						pip.getPNList().setSelectedIndex(0);
					}
				}
			}
			//no blue row set --> nothing to undo
			else
			{
				int index = this.fieldActiveRow;
				//if index == 0
				//then the current PartInstructionJPanel is in the top row
				//so there is nothing to do
				if (index > 0)
				{
					MFSPartInstructionJPanel pip = getRowVectorElementAt(index - 1);
					int yPos = (int) pip.getBounds().getY();
					this.spPartsInst.getVerticalScrollBar().setValue(yPos);
					if (pip.getIsNonPartInstruction())
					{
						this.fieldBlueRow = index - 1;
						pip.changeColor(MFSPartInstructionJPanel.BLUE_ROW_COLOR);
					}
					else
					{
						//Highlight the first part
						pip.getPNList().setSelectedIndex(0);
					}
				}
			}
		}
		else if (keyCode == KeyEvent.VK_DOWN)
		{
			JList tempList = getRowVectorElementAt(this.fieldActiveRow).getPNList();
			if (tempList.getSelectedIndex() == tempList.getModel().getSize() - 1)
			{
				ke.consume();
				for (int i = this.fieldActiveRow + 1; i < this.fieldRowVector.size(); i++)
				{
					MFSPartInstructionJPanel pip = getRowVectorElementAt(i);
					if (pip.getIsNonPartInstruction() == false)
					{
						JList tmpList = pip.getPNList();
						tmpList.setSelectedIndex(0);
						tmpList.requestFocusInWindow();
						tmpList.ensureIndexIsVisible(0);
						break;
					}
				}
			}
		}
		else if (keyCode == KeyEvent.VK_UP)
		{
			JList tempList = getRowVectorElementAt(this.fieldActiveRow).getPNList();
			if (tempList.getSelectedIndex() == 0)
			{
				ke.consume();
				for (int i = this.fieldActiveRow - 1; i >= 0; i--)
				{
					MFSPartInstructionJPanel pip = getRowVectorElementAt(i);
					if (pip.getIsNonPartInstruction() == false)
					{
						JList tmpList = pip.getPNList();
						ListModel tmpLm = pip.getPNListModel();
						int selectedIndex = tmpLm.getSize() - 1;
						tmpList.setSelectedIndex(selectedIndex);
						tmpList.requestFocusInWindow();
						tmpList.ensureIndexIsVisible(selectedIndex);
						break;
					}
				}
			}
		}
	}
}
