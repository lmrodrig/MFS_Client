/* @ Copyright IBM Corporation 2006, 2007. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2006-06-28      31801JM  R Prechel        -Complete rewrite; no change flags.
 * 2007-01-15   ~1 34242JR  R Prechel        -Java 5 version
 *                                           -Add setFocusable(false), removed setName
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.SystemColor;

import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 * <code>MFSActionJPanel</code> is a subclass of <code>JPanel</code> that
 * contains a <code>JProgressBar</code> and a <code>JTextField</code> and is
 * used to display status information about an action.
 * @author The MFS Client Development Team
 */
public class MFSActionJPanel
	extends JPanel
{
	private static final long serialVersionUID = 1L;
	/**
	 * The horizontal gap between the <code>JProgressBar</code> and the
	 * <code>JTextField</code>.
	 */
	private static final int HGAP = 5;

	/** The vertical gap of the <code>BorderLayout</code>. */
	private static final int VGAP = 0;

	/**
	 * The minimum value the <code>JProgressBar</code> displays.
	 * @see JProgressBar#getMinimum()
	 * @see JProgressBar#setMinimum(int)
	 */
	private static final int MINIMUM = 0;

	/**
	 * The maximum value the <code>JProgressBar</code> displays.
	 * @see JProgressBar#getMaximum()
	 * @see JProgressBar#setMaximum(int)
	 */
	private static final int MAXIMUM = 100;

	/**
	 * The amount by which the <code>JProgressBar</code>'s value is
	 * incremented to indicate an action is being performed.
	 */
	private static final int INCREMENT = 10;

	/** The default text displayed in the <code>JTextField</code>. */
	private static final String DEFAULT_TEXT = " "; //$NON-NLS-1$

	/** The width of the <code>JProgressBar</code>. */
	private static final int PB_WIDTH = 50;

	/** The height of the <code>JProgressBar</code>. */
	private static final int PB_HEIGHT = 15;

	/** The <code>JProgressBar</code>. */
	private JProgressBar progressBar = null;

	/** The <code>JTextField</code> that displays the action message. */
	private JTextField textField = null;

	/** Constructs a new <code>MFSActionJPanel</code>. */
	public MFSActionJPanel()
	{
		super(new BorderLayout(HGAP, VGAP));
		this.setRequestFocusEnabled(false);

		this.progressBar = new JProgressBar(SwingConstants.HORIZONTAL, MINIMUM, MAXIMUM);
		this.progressBar.setBackground(SystemColor.text);
		this.progressBar.setForeground(Color.blue);
		this.progressBar.setMinimumSize(new Dimension(PB_WIDTH, PB_HEIGHT));
		this.progressBar.setPreferredSize(new Dimension(PB_WIDTH, PB_HEIGHT));
		this.progressBar.setMaximumSize(new Dimension(PB_WIDTH, PB_HEIGHT));
		this.progressBar.setRequestFocusEnabled(false);

		this.textField = new JTextField();
		this.textField.setText(DEFAULT_TEXT);
		this.textField.setBackground(SystemColor.text);
		this.textField.setEditable(false);
		this.textField.setRequestFocusEnabled(false);
		this.textField.setFocusable(false); //~1A

		this.add(this.progressBar, BorderLayout.WEST);
		this.add(this.textField, BorderLayout.CENTER);
	}

	/**
	 * Starts indicating that an action is being performed. This method displays
	 * the specified <code>message</code> and increments the value of the
	 * <code>JProgressBar</code>.
	 * @param message a message describing the action that is being performed
	 */
	public void startAction(String message)
	{
		int value = this.progressBar.getValue();
		this.textField.setText(message);
		this.progressBar.setValue(value < MAXIMUM ? value + INCREMENT : MINIMUM);
	}

	/**
	 * Stops indicating that an action is being performed.
	 * @see #startAction(String)
	 */
	public void stopAction()
	{
		this.progressBar.setValue(MINIMUM);
		this.textField.setText(DEFAULT_TEXT);
	}

	/**
	 * If the value of the <code>JProgressBar</code> is less than
	 * {@link #MAXIMUM MAXIMUM}, this method increments the value of the
	 * <code>JProgressBar</code> by {@link #INCREMENT INCREMENT} and creates a
	 * delay of <code>msDelay</code> milliseconds. Otherwise, this method does
	 * not change the value of the <code>JProgressBar</code> and does not
	 * cause a delay.
	 * @param msDelay the amount of delay in milliseconds
	 * @return <code>true</code> iff the value of the <code>JProgressBar</code>
	 *         is less than {@link #MAXIMUM MAXIMUM}
	 * @see JProgressBar#setValue(int)
	 */
	public boolean incrementValue(long msDelay)
	{
		int value = this.progressBar.getValue();

		if (value < MAXIMUM)
		{
			value += INCREMENT;
			this.progressBar.setValue(value);
			try
			{
				Thread.sleep(msDelay);
			}
			catch (InterruptedException ie)
			{
				//Nothing to do
			}
		}
		return value < MAXIMUM;
	}
}
