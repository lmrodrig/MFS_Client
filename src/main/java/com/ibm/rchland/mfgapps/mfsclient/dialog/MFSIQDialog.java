/* @ Copyright IBM Corporation 2005, 2007. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2007-02-21   ~1 34242JR  R Prechel        -Java 5 version
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.dialog;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

import com.ibm.rchland.mfgapps.mfsclient.MFSDialog;
import com.ibm.rchland.mfgapps.mfsclient.MFSFrame;

/**
 * <code>MFSIQDialog</code> is the <code>MFSDialog</code> used to display
 * inspection questions.
 * @author The MFS Client Development Team
 */
public class MFSIQDialog
	extends MFSDialog
{
	private static final long serialVersionUID = 1L;
	/** The <code>JTextArea</code> that displays the inspection questions. */
	private JTextArea taIq = new JTextArea();

	/** The <code>JScrollPane</code> that contains the inspection question text area. */
	private JScrollPane spIq = new JScrollPane(this.taIq,
			ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
			ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

	/** The Enter <code>JButton</code>. */
	private JButton pbEnter = createButton("Enter = OK");

	/** The Cancel <code>JButton</code>. */
	private JButton pbCancel = createButton("Esc = Cancel");

	/** Set <code>true</code> iff the cancel button is pressed. */
	private boolean fieldPressedCancel = false;

	/**
	 * Constructs a new <code>MFSIQDialog</code>.
	 * @param parent the <code>MFSFrame</code> that caused this
	 *        <code>MFSIQDialog</code> to be displayed
	 */
	public MFSIQDialog(MFSFrame parent)
	{
		super(parent, "Inspection Questions");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		createLayout();
		addMyListeners();
	}

	/** Adds this dialog's <code>Component</code>s to the layout. */
	private void createLayout()
	{
		this.taIq.setLineWrap(true);
		this.taIq.setEditable(false);
		this.spIq.setMinimumSize(new Dimension(463, 375));
		this.spIq.setPreferredSize(new Dimension(463, 375));
		this.spIq.setMaximumSize(new Dimension(463, 375));

		JPanel contentPane = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 2, 1, 1.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, 
				new Insets(20, 20, 20, 20), 0, 0);

		contentPane.add(this.spIq, gbc);

		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.NONE;
		contentPane.add(this.pbEnter, gbc);

		gbc.gridx = 1;
		contentPane.add(this.pbCancel, gbc);

		setContentPane(contentPane);
		pack();
	}

	/** Adds the listeners to this dialog's <code>Component</code>s. */
	private void addMyListeners()
	{
		this.pbCancel.addActionListener(this);
		this.pbEnter.addActionListener(this);

		this.pbCancel.addKeyListener(this);
		this.pbEnter.addKeyListener(this);
		this.taIq.addKeyListener(this);
		this.spIq.addKeyListener(this);
	}

	/** Prompts the user for confirmation. */
	public void confirm()
	{
		Object[] options = {"OK", "CANCEL"};
		int n = JOptionPane.showOptionDialog(getParent(),
				"Press Enter key to confirm end IQ.", "Confirmation",
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null,
				options, options[0]);
		if (n == 0)
		{
			this.dispose();
		}
		else
		{
			this.pbEnter.requestFocusInWindow();
			this.toFront();
		}
	}

	/**
	 * Returns <code>true</code> iff the cancel button was pressed.
	 * @return <code>true</code> iff the cancel button was pressed
	 */
	public boolean getPressedCancel()
	{
		return this.fieldPressedCancel;
	}

	/**
	 * Loads the inspection questions using the specified iq data.
	 * @param iq the data used to load the inspection questions
	 */
	public void loadIQ(String iq)
	{
		String fifty_blanks = "                                                  "; //$NON-NLS-1$
		int row_count = 0;

		//~1 Use a StringBuffer to build text
		StringBuffer text = new StringBuffer();
		// the AS/400 can pass back multiple sets of questions, each set is
		// 256 characters long and can consist of 4 questions.
		// Compute how many question sets there will be.
		int question_sets = iq.length() / 256;

		// set up a loop limited by the number of question sets
		for (int i = 0; i < question_sets; i++)
		{
			// compute the start and end positions
			int start = 256 * i;
			int end = start + 50;

			text.append("\n");
			text.append("  ");
			text.append((i + 1));
			text.append(")");
			row_count++; // the newline gets counted

			// each set can have 4 questions, let's loop through 4
			boolean more_questions = true;
			int j = 0;
			while (more_questions && j < 4)
			{
				if (iq.substring(start, end).equals(fifty_blanks))
				{
					more_questions = false;
				}
				else
				{
					// fill in the text area with a question
					text.append(" ");
					text.append(iq.substring(start, end));
					text.append("\n");
					text.append("  ");
					start += 50;
					end += 50;
					row_count++;
				}
				j++;
			}
		} // end loop through question_sets

		// now we can set the number of rows in the text area to get the scroll
		// bar looking properly
		// add one to make it look nice
		this.taIq.setText(text.toString());
		this.taIq.setRows(row_count + 1);
	}

	/**
	 * Invoked when an action occurs.
	 * @param ae the <code>ActionEvent</code>
	 */
	public void actionPerformed(ActionEvent ae)
	{
		final Object source = ae.getSource();
		if (source == this.pbEnter)
		{
			confirm();
		}
		else if (source == this.pbCancel)
		{
			this.fieldPressedCancel = true;
			this.dispose();
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
		if ((keyCode == KeyEvent.VK_ENTER) && (source != this.pbCancel))
		{
			this.pbEnter.requestFocusInWindow();
			this.pbEnter.doClick();
		}
		else if (keyCode == KeyEvent.VK_ESCAPE)
		{
			this.pbCancel.requestFocusInWindow();
			this.pbCancel.doClick();
		}
		else if (keyCode == KeyEvent.VK_PAGE_DOWN)
		{
			final JScrollBar scrollBar = this.spIq.getVerticalScrollBar();
			int value = scrollBar.getValue();
			if ((value + 2 * scrollBar.getBlockIncrement(1)) > scrollBar.getMaximum())
			{
				scrollBar.setValue(scrollBar.getMaximum());
			}
			else
			{
				scrollBar.setValue(value + scrollBar.getBlockIncrement(1));
			}
			ke.consume();
		}
		else if (keyCode == KeyEvent.VK_PAGE_UP)
		{
			final JScrollBar scrollBar = this.spIq.getVerticalScrollBar();
			int value = scrollBar.getValue() - scrollBar.getBlockIncrement(-1);
			if (value < 0)
			{
				scrollBar.setValue(0);
			}
			else
			{
				scrollBar.setValue(value);
			}
			ke.consume();
		}
	}

	/**
	 * Invoked the first time a window is made visible.
	 * @param we the <code>WindowEvent</code>
	 */
	public void windowOpened(WindowEvent we)
	{
		if (we.getSource() == this)
		{
			this.pbEnter.requestFocusInWindow();
		}
	}
}
