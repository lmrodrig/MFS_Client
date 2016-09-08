/* © Copyright IBM Corporation 2006. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2006-06-14      30260AT  R Prechel        -Initial version
 * 2007-01-23   ~1 34242JR  R Prechel        -Java 5 version
 *                                           -Use an array instead of a Vector
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.utils;

import java.util.Iterator;
import java.util.NoSuchElementException;

import javax.swing.JButton;

import com.ibm.rchland.mfgapps.mfsclient.MFSLabelButton;
import com.ibm.rchland.mfgapps.mfsclient.MFSMenuButton;

/**
 * <code>MFSButtonIterator</code> is an <code>Iterator</code> over a
 * collection of <code>JButton</code>s.
 * @author The MFS Client Development Team
 */
@SuppressWarnings("rawtypes")
public class MFSButtonIterator
	implements Iterator
{
	/** The iteration index. */
	private int index;

	/** The number of <code>JButton</code>s in the iteration. */
	private int buttonCount; //~1A

	//~1C Use an array instead of a Vector to store the buttons
	/** Stores the <code>JButton</code>s in the iteration. */
	private JButton[] buttons;

	//~1C Added capacity parameter
	/**
	 * Constructs a new <code>MFSButtonIterator</code>.
	 * @param capacity the initial capacity of the data structure used by the
	 *        <code>MFSButtonIterator</code>
	 */
	public MFSButtonIterator(int capacity)
	{
		this.index = 0;
		this.buttonCount = 0;
		this.buttons = new JButton[capacity];
	}

	/**
	 * Returns <code>true</code> iff the iteration has more
	 * <code>JButton</code>s.
	 * @return <code>true</code> iff the iteration has more
	 *         <code>JButton</code>s
	 */
	public boolean hasNext()
	{
		return this.index < this.buttonCount;
	}

	/**
	 * Returns the next <code>JButton</code> in the iteration.
	 * @return the next <code>JButton</code> in the iteration
	 * @throws NoSuchElementException if all of the <code>JButton</code>s in
	 *         the iteration have been returned
	 */
	public Object next()
		throws NoSuchElementException
	{
		//~1C Use an array instead of a Vector to store the buttons
		if (this.index < this.buttonCount)
		{
			return this.buttons[this.index++];
		}
		throw new NoSuchElementException();
	}

	/**
	 * Returns the next <code>JButton</code> in the iteration.
	 * @return the next <code>JButton</code> in the iteration
	 * @throws NoSuchElementException if all of the <code>JButton</code>s in
	 *         the iteration have been returned
	 */
	public JButton nextJButton()
	{
		return (JButton) next();
	}

	//~1A New method
	/**
	 * Returns the next <code>MFSLabelButton</code> in the iteration.
	 * @return the next <code>MFSLabelButton</code> in the iteration
	 * @throws NoSuchElementException if all of the <code>JButton</code>s in
	 *         the iteration have been returned
	 * @throws ClassCastException if the next <code>JButton</code> in the
	 *         iteration is not an <code>MFSLabelButton</code>
	 */
	public MFSLabelButton nextLabelButton()
	{
		return (MFSLabelButton) next();
	}

	//~1A New method
	/**
	 * Returns the next <code>MFSMenuButton</code> in the iteration.
	 * @return the next <code>MFSMenuButton</code> in the iteration
	 * @throws NoSuchElementException if all of the <code>JButton</code>s in
	 *         the iteration have been returned
	 * @throws ClassCastException if the next <code>JButton</code> in the
	 *         iteration is not an <code>MFSMenuButton</code>
	 */
	public MFSMenuButton nextMenuButton()
	{
		return (MFSMenuButton) next();
	}

	/**
	 * Throws <code>UnsupportedOperationException</code>.
	 * @throws UnsupportedOperationException always
	 */
	public void remove()
		throws UnsupportedOperationException
	{
		throw new UnsupportedOperationException(
				"MFSButtonIterator.remove() is not supported"); //$NON-NLS-1$
	}

	/**
	 * Adds the specified <code>JButton</code> to the end of the iteration.
	 * @param button the <code>JButton</code> to add
	 */
	public void add(JButton button)
	{
		//~1C Use an array instead of a Vector to store the buttons
		if (this.buttonCount == this.buttons.length)
		{
			int capacity = this.buttons.length + 2;
			JButton[] newArray = new JButton[capacity];
			System.arraycopy(this.buttons, 0, newArray, 0, this.buttonCount);
			this.buttons = newArray;

		}
		this.buttons[this.buttonCount++] = button;
	}

	/**
	 * Returns the number of <code>JButton</code>s in the iteration.
	 * @return the number of <code>JButton</code>s in the iteration
	 */
	public int size()
	{
		//~1C return buttonCount instead of list.size()
		return this.buttonCount;
	}

	//~1A New method
	/** Resets the iteration. */
	public void reset()
	{
		this.index = 0;
	}
}
