/* © Copyright IBM Corporation 2007. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2007-07-25      26157CD  Toribio Hdez.    -Initial version
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.utils;

import java.util.Vector;
import javax.swing.JList;
import com.ibm.rchland.mfgapps.mfsclient.MFSPartInstructionJPanel;
import com.ibm.rchland.mfgapps.mfsclient.struct.MFSComponentListModel;
import com.ibm.rchland.mfgapps.mfsclient.struct.MFSComponentRec;
import com.ibm.rchland.mfgapps.mfsclient.struct.MFSInstructionRec;

/**
 * <code>MFSComponentSearch</code> is used to find the first
 * <code>MFSComponentRec</code> that matches the search criteria given by an
 * instance of <code>MFSComponentSearchCriteria</code>.
 * @author The MFS Client Development Team
 */
public class MFSComponentSearch
{
	/** The current search row. */
	private int fieldRow = 0;
	/** The current search index. */
	private int fieldIndex = 0;
	/** The starting search row. */
	private int fieldStartRow = 0;
	/** The starting search index. */
	private int fieldStartIndex = 0;
	/** Controls which search loops are called. */
	private boolean fieldExecuteFirstLoop = true;
	/** The matching <code>MFSComponentRec</code>. */
	private MFSComponentRec fieldMatchingRec = null;
	/** The <code>Vector</code> of <code>MFSPartInstructionJPanel</code>s. */
	@SuppressWarnings("rawtypes")
	private Vector fieldRowVector = null;
	/** The <code>MFSSearchCriteria</code> to use. */
	private MFSComponentSearchCriteria fieldCriteria = null;
	/** The matching <code>MFSInstructionRec</code>. */
	private MFSInstructionRec fieldMatchingInstructionRec = null;
	/** The search Non Part Instruction <code>searchNonPartInstruction</code>. */	
	private boolean searchNonPartInstruction = false;

	/**
	 * Constructs a new <code>MFSComponentSearch</code>.
	 * @param rowVector the <code>Vector</code> of
	 *        <code>MFSPartInstructifonJPanel</code>s
	 * @param criteria the <code>MFSSearchCriteria</code> to use
	 */
	@SuppressWarnings("rawtypes")
	public MFSComponentSearch(Vector rowVector, MFSComponentSearchCriteria criteria)
	{
		this.fieldRowVector = rowVector;
		this.fieldCriteria = criteria;
		this.searchNonPartInstruction = false;
	}

	/**
	 * Returns the search row.
	 * @return the search row
	 */
	public int getRow()
	{
		return this.fieldRow;
	}

	/**
	 * Returns the search index.
	 * @return the search index
	 */
	public int getIndex()
	{
		return this.fieldIndex;
	}
	
	/**
	 * Returns the matching <code>MFSComponentRec</code>.
	 * @return the matching <code>MFSComponentRec</code>
	 */
	public MFSComponentRec getMatchingComponentRec()
	{
	     return this.fieldMatchingRec;
	}
	
	/**
	 * Returns the matching <code>MFSInstructionRec</code>.
	 * @return the matching <code>MFSInstructionRec</code>
	 */
	public MFSInstructionRec getFieldMatchingInstructionRec() {
		return fieldMatchingInstructionRec;
	}

	/**
	 * Returns the <code>MFSPartInstructionJPanel</code> at the specified index.
	 * @param index an index into {@link #fieldRowVector}
	 * @return the <code>MFSPartInstructionJPanel</code> at the specified index
	 * @throws ArrayIndexOutOfBoundsException if the <code>index</code> is
	 *         negative or not less than the current size of the vector
	 * @throws ClassCastException if the element at <code>index</code> is not
	 *         an <code>MFSPartInstructionJPanel</code>
	 */
	private MFSPartInstructionJPanel getRowVectorElementAt(int index)
	{
		return (MFSPartInstructionJPanel) this.fieldRowVector.elementAt(index);
	}
	
	/**
	 * Sets the search starting position.
	 * @param row the first search row
	 * @param index the first search index
	 */
	public void setSearchStartPosition(int row, int index)
	{
		this.fieldStartRow = row;
		this.fieldStartIndex = index;
		this.fieldRow = row;
		this.fieldIndex = index;
		this.fieldExecuteFirstLoop = true;
	}

	/**
	 * Sets the searchNonPartInstruction flag.
	 * @param searchNonPartInstruction flag value
	 */	
	public void setSearchNonPartInstruction(boolean searchNonPartInstruction) {
		this.searchNonPartInstruction = searchNonPartInstruction;
	}
	
	/** Increments the search index. */
	public void incrementIndex()
	{
		this.fieldIndex++;
	}

	/**
	 * Searches for a matching <code>MFSComponentRec</code>.
	 * @param toRow the row at which the search is stopped
	 * @param toIndex the index at which the search is stopped
	 * @return <code>true</code> if a matching <code>MFSComponentRec</code>
	 *         is found; <code>false</code> otherwise
	 */
	private boolean doSearchLoop(int toRow, int toIndex, boolean searchNonPartInstruction)
	{
		//Loop until a match occurs or fieldRow == toRow
		this.fieldMatchingRec = null;
		this.fieldMatchingInstructionRec = null;
		
		while (this.fieldRow < toRow)
		{
			MFSPartInstructionJPanel instruction = getRowVectorElementAt(this.fieldRow);
			if (!instruction.getIsNonPartInstruction())
			{
				JList list = instruction.getPNList();
				MFSComponentListModel lm = (MFSComponentListModel) list.getModel();

				//Loop until a match occurs or fieldIndex == endIndex
				int endIndex = lm.size() < toIndex ? lm.size() : toIndex;
				while (this.fieldIndex < endIndex)
				{
					MFSComponentRec rec = lm.getComponentRecAt(this.fieldIndex);
					if (this.fieldCriteria.match(rec))
					{
						this.fieldMatchingRec = rec;
						return true;
					}
					this.fieldIndex++;
				}
			}
			else if(searchNonPartInstruction)
			{
				MFSInstructionRec instructionRec = instruction.getInstructionRec();
				if (this.fieldCriteria.matchInstruction(instructionRec))
				{
					this.fieldMatchingInstructionRec = instructionRec;
					return true;
				}	
			}
			this.fieldRow++;
			this.fieldIndex = 0;
		}
		return false;
	}
	
	/**
	 * Searches for an <code>MFSComponentRec</code> that matches the search
	 * criteria given by the <code>MFSComponentSearchCriteria</code>.
	 * @return <code>true</code> if a matching <code>MFSComponentRec</code>
	 *         is found; <code>false</code> otherwise
	 */
	public boolean findComponent()
	{
		boolean found = false;
		this.fieldMatchingRec = null;
		
		if(this.fieldExecuteFirstLoop)
		{
			// Start search at fieldRow and fieldIndex.
			// Go to end of row vector.
			found = doSearchLoop(this.fieldRowVector.size(),Integer.MAX_VALUE, this.searchNonPartInstruction);
		}

		// Start at beginning if no match was found and
		// fieldStartRow or fieldStartIndex is not 0
		if (!found && (0 != this.fieldStartRow || 0 != this.fieldStartIndex))
		{
			this.fieldExecuteFirstLoop = false;
			this.fieldRow = 0;
			found = doSearchLoop(this.fieldStartRow,Integer.MAX_VALUE, this.searchNonPartInstruction);

			// Search the indices that were skipped on the start row
			if (!found && 0 != this.fieldStartIndex)
			{
				found = doSearchLoop(this.fieldStartRow + 1, this.fieldStartIndex, this.searchNonPartInstruction);
			}
		}
		return found;
	}	
}