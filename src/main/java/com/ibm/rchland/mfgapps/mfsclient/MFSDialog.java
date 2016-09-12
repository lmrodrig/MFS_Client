/* @ Copyright IBM Corporation 2006, 2007. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2006-03-15      30907JS  A Williams       -Initial
 * 2007-02-16      34242JR  R Prechel        -Java 5 version
 * 2007-03-30      38166JM  R Prechel        -Remove setLocationRelativeTo from constructor
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;

import com.ibm.rchland.mfgapps.client.utils.IGSMaxLengthDocument;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSConstants;

/**
 * <code>MFSDialog</code> is the abstract base class for all dialogs displayed
 * by the <i>MFS Client </i>. This class does not implement the
 * {@link com.ibm.rchland.mfgapps.mfscommon.MFSActionable} interface, and
 * concrete dialogs that are direct descendents of this class should not display
 * an {@link com.ibm.rchland.mfgapps.mfsclient.MFSActionJPanel}. The abstract
 * class {@link com.ibm.rchland.mfgapps.mfsclient.MFSActionableDialog}, which
 * extends this class, implements the
 * {@link com.ibm.rchland.mfgapps.mfscommon.MFSActionable} interface and
 * displays an {@link com.ibm.rchland.mfgapps.mfsclient.MFSActionJPanel}.
 * @author The MFS Client Development Team
 */
public abstract class MFSDialog
	extends JDialog
	implements ActionListener, KeyListener, WindowListener
{
	private static final long serialVersionUID = 1L;
	/**
	 * The <code>MFSFrame</code> that caused this <code>MFSDialog</code> to
	 * be displayed.
	 */
	private MFSFrame parent = null;

	/**
	 * Constructs a new modal <code>MFSDialog</code> with the specified
	 * <code>title</code> and the specified <code>parent</code> as owner. In
	 * addition, the dialog's resizable property is set to <code>false</code>,
	 * and the dialog is added as its own <code>WindowListener</code>.
	 * @param parent the <code>MFSFrame</code> that caused this
	 *        <code>MFSDialog</code> to be displayed
	 * @param title the <code>String</code> to display in the title bar
	 */
	public MFSDialog(MFSFrame parent, String title)
	{
		super(parent, title, true);
		this.parent = parent;
		setResizable(false);
		addWindowListener(this);
	}

	/**
	 * Returns the <code>MFSFrame</code> that caused this
	 * <code>MFSDialog</code> to be displayed.
	 * @return the <code>MFSFrame</code>
	 */
	public MFSFrame getParentFrame()
	{
		return this.parent;
	}

	/**
	 * Creates a new <code>JButton</code>.
	 * @param text the text for the <code>JButton</code>
	 * @return the new <code>JButton</code>
	 */
	public static final JButton createButton(String text)
	{
		JButton result = new JButton(text);
		result.setFont(MFSConstants.MEDIUM_DIALOG_FONT);
		result.setForeground(MFSConstants.PRIMARY_FOREGROUND_COLOR);
		return result;
	}

	/**
	 * Creates a new <code>JButton</code>.
	 * @param text the text for the <code>JButton</code>
	 * @param mnemonic the mnemonic for the <code>JButton</code>
	 * @return the new <code>JButton</code>
	 */
	public static final JButton createButton(String text, char mnemonic)
	{
		JButton result = new JButton(text);
		result.setMnemonic(mnemonic);
		result.setFont(MFSConstants.MEDIUM_DIALOG_FONT);
		result.setForeground(MFSConstants.PRIMARY_FOREGROUND_COLOR);
		return result;
	}

	/**
	 * Creates a new <code>JLabel</code>.
	 * @param text the text for the <code>JLabel</code>
	 * @return the new <code>JLabel</code>
	 */
	public static final JLabel createLabel(String text)
	{
		JLabel result = new JLabel(text);
		result.setFont(MFSConstants.MEDIUM_DIALOG_FONT);
		result.setForeground(MFSConstants.PRIMARY_FOREGROUND_COLOR);
		return result;
	}
	
	/**
	 * Creates a new <code>JLabel</code> using the specified <code>Font</code>.
	 * @param text the text for the <code>JLabel</code>
	 * @param font the font for the <code>JLabel</code>
	 * @return the new <code>JLabel</code>
	 */
	public static final JLabel createLabel(String text, Font font)
	{
		JLabel result = new JLabel(text);
		result.setFont(font);
		result.setForeground(MFSConstants.PRIMARY_FOREGROUND_COLOR);
		return result;
	}

	/**
	 * Creates a small name <code>JLabel</code> with the specified text.
	 * @param text the text displayed by the name <code>JLabel</code>
	 * @return the new small name <code>JLabel</code>
	 */
	public static final JLabel createSmallNameLabel(String text)
	{
		JLabel result = new JLabel(text, SwingConstants.TRAILING);
		result.setFont(MFSConstants.SMALL_DIALOG_FONT);
		result.setForeground(MFSConstants.PRIMARY_FOREGROUND_COLOR);
		return result;
	}

	/**
	 * Creates a name <code>JLabel</code> with the specified text.
	 * @param text the text displayed by the name <code>JLabel</code>
	 * @return the new name <code>JLabel</code>
	 */
	public static final JLabel createNameLabel(String text)
	{
		JLabel result = new JLabel(text, SwingConstants.TRAILING);
		result.setFont(MFSConstants.MEDIUM_DIALOG_FONT);
		result.setForeground(MFSConstants.PRIMARY_FOREGROUND_COLOR);
		return result;
	}

	/**
	 * Creates a small value <code>JLabel</code> that is labeled by the
	 * specified name <code>JLabel</code>. The value <code>JLabel</code>'s
	 * initial text is set to the empty <code>String</code> (i.e., "").
	 * @param label the name <code>JLabel</code> corresponding to this value
	 *        <code>JLabel</code>
	 * @return the new small value <code>JLabel</code>
	 */
	public static final JLabel createSmallValueLabel(JLabel label)
	{
		JLabel result = new JLabel("", SwingConstants.LEADING); //$NON-NLS-1$
		result.setFont(MFSConstants.SMALL_DIALOG_FONT);
		result.setForeground(MFSConstants.PRIMARY_FOREGROUND_COLOR);
		label.setLabelFor(result);
		return result;
	}

	/**
	 * Creates a value <code>JLabel</code> that is labeled by the specified
	 * name <code>JLabel</code>. The value <code>JLabel</code>'s initial
	 * text is set to the empty <code>String</code> (i.e., "").
	 * @param label the name <code>JLabel</code> corresponding to this value
	 *        <code>JLabel</code>
	 * @return the new value <code>JLabel</code>
	 */
	public static final JLabel createValueLabel(JLabel label)
	{
		JLabel result = new JLabel("", SwingConstants.LEADING); //$NON-NLS-1$
		result.setFont(MFSConstants.MEDIUM_DIALOG_FONT);
		result.setForeground(MFSConstants.PRIMARY_FOREGROUND_COLOR);
		label.setLabelFor(result);
		return result;
	}

	/**
	 * Creates a new <code>JList</code> with a single-selection
	 * selection-mode.
	 * @return the new <code>JList</code>
	 */
	public static final JList createList()
	{
		JList result = new JList();
		result.setFont(MFSConstants.MEDIUM_MONOSPACED_FONT);
		result.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		result.setForeground(MFSConstants.PRIMARY_FOREGROUND_COLOR);
		return result;
	}

	/**
	 * Creates a new <code>JPasswordField</code> with the specified number of
	 * <code>columns</code> and maximum number of characters that is labeled
	 * by the specified <code>label</code>.
	 * @param columns the number of columns, which is used to calculate the
	 *        preferred width of the <code>JPasswordField</code>
	 * @param max if greater than 0, specifies the maximum number of characters
	 *        allowed in the <code>JPasswordField</code>
	 * @param label the <code>JLabel</code> for the
	 *        <code>JPasswordField</code>
	 * @return the new <code>JPasswordField</code>
	 */
	public static final JPasswordField createPasswordField(int columns, int max,
															JLabel label)
	{
		JPasswordField result = new JPasswordField(columns);
		if (max > 0)
		{
			result.setDocument(new IGSMaxLengthDocument(max));
		}
		result.setFont(MFSConstants.MEDIUM_MONOSPACED_FONT);
		result.setForeground(MFSConstants.PRIMARY_FOREGROUND_COLOR);
		label.setLabelFor(result);
		return result;
	}

	/**
	 * Creates a new <code>JTextField</code> with the specified number of
	 * <code>columns</code> and maximum number of characters.
	 * @param columns the number of columns, which is used to calculate the
	 *        preferred width of the <code>JTextField</code>
	 * @param max if greater than 0, specifies the maximum number of characters
	 *        allowed in the <code>JTextField</code>
	 * @return the new <code>JTextField</code>
	 */
	public static final JTextField createTextField(int columns, int max)
	{
		JTextField result = new JTextField(columns);
		if (max > 0)
		{
			result.setDocument(new IGSMaxLengthDocument(max));
		}
		result.setFont(MFSConstants.MEDIUM_MONOSPACED_FONT);
		result.setForeground(MFSConstants.PRIMARY_FOREGROUND_COLOR);
		return result;
	}

	/**
	 * Creates a new <code>JTextField</code> with the specified number of
	 * <code>columns</code> and maximum number of characters that is labeled
	 * by the specified <code>label</code>.
	 * @param columns the number of columns, which is used to calculate the
	 *        preferred width of the <code>JTextField</code>
	 * @param max if greater than 0, specifies the maximum number of characters
	 *        allowed in the <code>JTextField</code>
	 * @param label the <code>JLabel</code> for the <code>JTextField</code>
	 * @return the new <code>JTextField</code>
	 */
	public static final JTextField createTextField(int columns, int max, JLabel label)
	{
		JTextField result = new JTextField(columns);
		if (max > 0)
		{
			result.setDocument(new IGSMaxLengthDocument(max));
		}
		result.setFont(MFSConstants.MEDIUM_MONOSPACED_FONT);
		result.setForeground(MFSConstants.PRIMARY_FOREGROUND_COLOR);
		label.setLabelFor(result);
		return result;
	}

	/**
	 * Sets the size of the specified <code>JLabel</code> so that it is large
	 * enough to display <code>maxCharacterCount</code> characters.
	 * @param label the <code>JLabel</code> to size
	 * @param maxCharacterCount the maximum number of characters the label will
	 *        display
	 */
	protected void setLabelDimensions(JLabel label, int maxCharacterCount)
	{
		StringBuffer buffer = new StringBuffer(maxCharacterCount);
		for (int i = 0; i < maxCharacterCount; i++)
		{
			buffer.append('W');
		}
		JLabel temp = createLabel(buffer.toString());
		Font font = label.getFont();
		if (font != null)
		{
			temp.setFont(font);
		}
		label.setMinimumSize(new Dimension(temp.getMinimumSize()));
		label.setPreferredSize(new Dimension(temp.getPreferredSize()));
		label.setMaximumSize(new Dimension(temp.getMaximumSize()));
	}

	/**
	 * Sets the preferred width of the buttons to the preferred width of the
	 * wider button and sets the preferred height of the buttons to the
	 * preferred height of the taller button.
	 * @param b1 the first <code>JButton</code>
	 * @param b2 the second <code>JButton</code>
	 */
	public static final void setButtonDimensions(JButton b1, JButton b2)
	{
		Dimension d1 = b1.getPreferredSize();
		Dimension d2 = b2.getPreferredSize();
		int maxWidth = (d1.width > d2.width) ? d1.width : d2.width;
		int maxHeight = (d1.height > d2.height) ? d1.height : d2.height;

		b1.setPreferredSize(new Dimension(maxWidth, maxHeight));
		b2.setPreferredSize(new Dimension(maxWidth, maxHeight));
	}

	/**
	 * Sets the preferred width of the buttons to the preferred width of the
	 * widest button and sets the preferred height of the buttons to the
	 * preferred height of the tallest button.
	 * @param b1 the first <code>JButton</code>
	 * @param b2 the second <code>JButton</code>
	 * @param b3 the third <code>JButton</code>
	 */
	protected void setButtonDimensions(JButton b1, JButton b2, JButton b3)
	{
		Dimension d1 = b1.getPreferredSize();
		Dimension d2 = b2.getPreferredSize();
		Dimension d3 = b3.getPreferredSize();
		int maxWidth = (d1.width > d2.width) ? d1.width : d2.width;
		if (d3.width > maxWidth)
		{
			maxWidth = d3.width;
		}
		int maxHeight = (d1.height > d2.height) ? d1.height : d2.height;
		if (d3.height > maxHeight)
		{
			maxHeight = d3.height;
		}

		b1.setPreferredSize(new Dimension(maxWidth, maxHeight));
		b2.setPreferredSize(new Dimension(maxWidth, maxHeight));
		b3.setPreferredSize(new Dimension(maxWidth, maxHeight));
	}

	/**
	 * Sets the preferred width of the buttons to the preferred width of the
	 * widest button and sets the preferred height of the buttons to the
	 * preferred height of the tallest button.
	 * @param b1 the first <code>JButton</code>
	 * @param b2 the second <code>JButton</code>
	 * @param b3 the third <code>JButton</code>
	 * @param b4 the fourth <code>JButton</code>
	 * @param b5 the fifth <code>JButton</code>
	 */
	protected void setButtonDimensions(JButton b1, JButton b2, JButton b3, JButton b4, JButton b5)
	{
		Dimension d1 = b1.getPreferredSize();
		Dimension d2 = b2.getPreferredSize();
		Dimension d3 = b3.getPreferredSize();
		Dimension d4 = b4.getPreferredSize();
		Dimension d5 = b5.getPreferredSize();
		int maxWidth = (d1.width > d2.width) ? d1.width : d2.width;
		if (d3.width > maxWidth)
		{
			maxWidth = d3.width;
		}
		if (d4.width > maxWidth)
		{
			maxWidth = d4.width;
		}
		if (d5.width > maxWidth)
		{
			maxWidth = d5.width;
		}
		int maxHeight = (d1.height > d2.height) ? d1.height : d2.height;
		if (d3.height > maxHeight)
		{
			maxHeight = d3.height;
		}
		if (d4.height > maxHeight)
		{
			maxHeight = d4.height;
		}
		if (d5.height > maxHeight)
		{
			maxHeight = d5.height;
		}
		
		b1.setPreferredSize(new Dimension(maxWidth, maxHeight));
		b2.setPreferredSize(new Dimension(maxWidth, maxHeight));
		b3.setPreferredSize(new Dimension(maxWidth, maxHeight));
		b4.setPreferredSize(new Dimension(maxWidth, maxHeight));
		b5.setPreferredSize(new Dimension(maxWidth, maxHeight));
	}

	/**
	 * Invoked when an action occurs. Does nothing.
	 * @param ae the <code>ActionEvent</code>
	 */
	public void actionPerformed(ActionEvent ae)
	{
		//Does nothing
	}

	/**
	 * Invoked when a key is pressed. Does nothing.
	 * @param ke the <code>KeyEvent</code>
	 */
	public void keyPressed(KeyEvent ke)
	{
		//Does nothing
	}

	/**
	 * Invoked when a key is released. Does nothing.
	 * @param ke the <code>KeyEvent</code>
	 */
	public void keyReleased(KeyEvent ke)
	{
		//Does nothing
	}

	/**
	 * Invoked when a key is typed. Does nothing.
	 * @param ke the <code>KeyEvent</code>
	 */
	public void keyTyped(KeyEvent ke)
	{
		//Does nothing
	}

	/**
	 * Invoked the first time a window is made visible. Does nothing.
	 * @param we the <code>WindowEvent</code>
	 */
	public void windowOpened(WindowEvent we)
	{
		//Does nothing
	}

	/**
	 * Invoked when the user attempts to close the window from the window's
	 * system menu. Does nothing.
	 * @param we the <code>WindowEvent</code>
	 */
	public void windowClosing(WindowEvent we)
	{
		//Does nothing
	}

	/**
	 * Invoked when a window has been closed as the result of calling dispose on
	 * the window. Does nothing.
	 * @param we the <code>WindowEvent</code>
	 */
	public void windowClosed(WindowEvent we)
	{
		//Does nothing
	}

	/**
	 * Invoked when a window is changed from a normal to a minimized state. Does
	 * nothing.
	 * @param we the <code>WindowEvent</code>
	 */
	public void windowIconified(WindowEvent we)
	{
		//Does nothing
	}

	/**
	 * Invoked when a window is changed from a minimized to a normal state. Does
	 * nothing.
	 * @param we the <code>WindowEvent</code>
	 */
	public void windowDeiconified(WindowEvent we)
	{
		//Does nothing
	}

	/**
	 * Invoked when the window is set to be the user's active window. Does
	 * nothing.
	 * @param we the <code>WindowEvent</code>
	 */
	public void windowActivated(WindowEvent we)
	{
		//Does nothing
	}

	/**
	 * Invoked when a window is no longer the active window. Does nothing.
	 * @param we the <code>WindowEvent</code>
	 */
	public void windowDeactivated(WindowEvent we)
	{
		//Does nothing
	}
}
