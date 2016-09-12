/* @ Copyright IBM Corporation 2005, 2007. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2007-02-22      34242JR  R Prechel        -Java 5 version
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.dialog;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import com.ibm.rchland.mfgapps.client.utils.IGSMaxLengthDocument;
import com.ibm.rchland.mfgapps.mfsclient.MFSDialog;
import com.ibm.rchland.mfgapps.mfsclient.MFSFrame;

/**
 * <code>MFSTextInputDialog</code> is the <code>MFSDialog</code> that allows
 * a user to enter text in a <code>JTextArea</code> such that the maximum
 * number of characters is constrained.
 * @author The MFS Client Development Team
 */
public class MFSTextInputDialog
	extends MFSDialog
{
	private static final long serialVersionUID = 1L;
	/** The input <code>JTextArea</code>. */
	private JTextArea taComment = new JTextArea(10, 30);

	/** The Enter <code>JButton</code>. */
	private JButton pbEnter = createButton("Enter", 'E');

	/** The Cancel <code>JButton</code>. */
	private JButton pbCancel = createButton("Cancel", 'n');

	/** Set <code>true</code> when the enter button is selected. */
	private boolean fieldPressedEnter = false;

	/** The text entered by the user. */
	private String fieldComment = ""; //$NON-NLS-1$

	/** The maximum number of characters allowed. */
	private int fieldMaxLength;

	/**
	 * Constructs a new <code>MFSTextInputDialog</code>.
	 * @param parent the <code>MFSFrame</code> that caused this
	 *        <code>MFSTextInputDialog</code> to be displayed
	 * @param title the <code>String</code> to display in the title bar
	 * @param maxLength the maximum number of characters allowed
	 */
	public MFSTextInputDialog(MFSFrame parent, String title, int maxLength)
	{
		super(parent, title);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.fieldMaxLength = maxLength;
		createLayout();
		addMyListeners();
	}

	/** Adds this dialog's <code>Component</code>s to the layout. */
	private void createLayout()
	{
		StringBuffer heading = new StringBuffer();
		heading.append("Enter up to ");
		heading.append(this.fieldMaxLength);
		heading.append(" characters of comments");

		JLabel headerLabel = createLabel(heading.toString());

		this.taComment.setLineWrap(true);
		this.taComment.setDocument(new IGSMaxLengthDocument(this.fieldMaxLength));

		JPanel contentPane = new JPanel(new GridBagLayout());
		contentPane.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 2, 1, 1, 1,
				GridBagConstraints.CENTER, GridBagConstraints.NONE,
				new Insets(0, 0, 20, 0), 0, 0);

		contentPane.add(headerLabel, gbc);

		gbc.gridy++;
		contentPane.add(this.taComment, gbc);

		gbc.gridy++;
		gbc.gridwidth = 1;
		gbc.weighty = 0.0;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(0, 0, 0, 0);
		contentPane.add(this.pbEnter, gbc);

		gbc.gridx = 1;
		contentPane.add(this.pbCancel, gbc);

		setContentPane(contentPane);
		setButtonDimensions(this.pbEnter, this.pbCancel);
		pack();
	}

	/** Adds the listeners to this dialog's <code>Component</code>s. */
	private void addMyListeners()
	{
		this.pbCancel.addActionListener(this);
		this.pbEnter.addActionListener(this);

		this.pbCancel.addKeyListener(this);
		this.pbEnter.addKeyListener(this);
		this.taComment.addKeyListener(this);
	}

	/**
	 * Returns the text the user entered.
	 * @return the text the user entered
	 */
	public String getComment()
	{
		return this.fieldComment;
	}

	/**
	 * Returns <code>true</code> iff the Enter button was selected.
	 * @return <code>true</code> iff the Enter button was selected
	 */
	public boolean getPressedEnter()
	{
		return this.fieldPressedEnter;
	}

	/**
	 * Sets the text of the <code>JTextArea</code>.
	 * @param text the text for the <code>JTextArea</code>
	 */
	public void setTextAreaText(String text)
	{
		this.taComment.setText(text);
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
			if (source == this.pbCancel)
			{
				this.dispose();
			}
			else if (source == this.pbEnter)
			{
				this.fieldPressedEnter = true;
				this.fieldComment = this.taComment.getText();
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
		if (keyCode == KeyEvent.VK_ENTER)
		{
			final Object source = ke.getSource();
			if (source == this.pbCancel)
			{
				this.pbCancel.requestFocusInWindow();
				this.pbCancel.doClick();
			}
			else
			{
				this.pbEnter.requestFocusInWindow();
				this.pbEnter.doClick();
			}
		}
		else if (keyCode == KeyEvent.VK_ESCAPE)
		{
			this.dispose();
		}
	}

	/**
	 * Invoked the first time a window is made visible.
	 * <p>
	 * Requests the focus for the {@link #taComment}.
	 * @param we the <code>WindowEvent</code>
	 */
	public void windowOpened(WindowEvent we)
	{
		if (we.getSource() == this)
		{
			this.taComment.requestFocusInWindow();
		}
	}
}
