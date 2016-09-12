/* @ Copyright IBM Corporation 2004, 2007. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2007-01-22      34242JR  R Prechel        -Java 5 version
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.dialog.rework;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

import com.ibm.rchland.mfgapps.mfsclient.MFSDialog;
import com.ibm.rchland.mfgapps.mfsclient.MFSFrame;

/**
 * <code>MFSRwkPrintDialog</code> displays buttons that allow the user to
 * print a Fail Tag, print a Fail Report, or cancel.
 * @author The MFS Client Development Team
 */
public class MFSRwkPrintDialog
	extends MFSDialog
{
	private static final long serialVersionUID = 1L;
	/** The Fail Tag <code>JButton</code>. */
	private JButton pbFailTag = createButton("F2=Fail Tag");

	/** The Fail Report <code>JButton</code>. */
	private JButton pbFailReport = createButton("F4=Fail Report");

	/** The Cancel <code>JButton</code>. */
	private JButton pbCancel = createButton("Cancel", 'n');

	/** Set <code>true</code> when the user presses the fail tag button. */
	private boolean fieldPressedTag = false;

	/** Set <code>true</code> when the user presses the fail report button. */
	private boolean fieldPressedReport = false;

	/**
	 * Constructs a new <code>MFSRwkPrintDialog</code>.
	 * @param parent the <code>MFSFrame</code> that caused this
	 *        <code>MFSRwkPrintDialog</code> to be displayed
	 */
	public MFSRwkPrintDialog(MFSFrame parent)
	{
		super(parent, "Rework Print Screen");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.pbFailTag.setEnabled(false);
		this.pbFailReport.setEnabled(false);
		createLayout();
		addMyListeners();
	}

	/** Adds this dialog's <code>Component</code>s to the layout. */
	private void createLayout()
	{
		JPanel contentPane = new JPanel(new GridLayout(1, 3, 20, 0));
		contentPane.setBorder(BorderFactory.createEmptyBorder(40, 20, 20, 20));
		contentPane.add(this.pbFailTag);
		contentPane.add(this.pbFailReport);
		contentPane.add(this.pbCancel);

		setContentPane(contentPane);
		pack();
	}

	/** Adds the listeners to this dialog's <code>Component</code>s. */
	private void addMyListeners()
	{
		this.pbCancel.addActionListener(this);
		this.pbFailTag.addActionListener(this);
		this.pbFailReport.addActionListener(this);

		this.pbCancel.addKeyListener(this);
		this.pbFailTag.addKeyListener(this);
		this.pbFailReport.addKeyListener(this);
	}

	/**
	 * Returns <code>true</code> iff the fail report button was pressed.
	 * @return <code>true</code> iff the fail report button was pressed
	 */
	public boolean getPressedReport()
	{
		return this.fieldPressedReport;
	}

	/**
	 * Returns <code>true</code> iff the fail tag button was pressed.
	 * @return <code>true</code> iff the fail tag button was pressed
	 */
	public boolean getPressedTag()
	{
		return this.fieldPressedTag;
	}

	/**
	 * Sets the enabled property of the fail report button.
	 * @param enabled the new value of the enabled property
	 */
	public void setPBFailReportEnabled(boolean enabled)
	{
		this.pbFailReport.setEnabled(enabled);
	}

	/**
	 * Sets the enabled property of the fail tag button.
	 * @param enabled the new value of the enabled property
	 */
	public void setPBFailTagEnabled(boolean enabled)
	{
		this.pbFailTag.setEnabled(enabled);
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
			else if (source == this.pbFailTag)
			{
				this.fieldPressedTag = true;
				this.dispose();
			}
			else if (source == this.pbFailReport)
			{
				this.fieldPressedReport = true;
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
			if (source == this.pbFailTag)
			{
				if (this.pbFailTag.isEnabled())
				{
					this.pbFailTag.requestFocusInWindow();
					this.pbFailTag.doClick();
				}
			}
			else if (source == this.pbFailReport)
			{
				if (this.pbFailReport.isEnabled())
				{
					this.pbFailReport.requestFocusInWindow();
					this.pbFailReport.doClick();
				}
			}
			else if (source == this.pbCancel)
			{
				this.pbCancel.requestFocusInWindow();
				this.pbCancel.doClick();
			}
		}
		else if (keyCode == KeyEvent.VK_F2)
		{
			if (this.pbFailTag.isEnabled())
			{
				this.pbFailTag.requestFocusInWindow();
				this.pbFailTag.doClick();
			}
		}
		else if (keyCode == KeyEvent.VK_F4)
		{
			if (this.pbFailReport.isEnabled())
			{
				this.pbFailReport.requestFocusInWindow();
				this.pbFailReport.doClick();
			}
		}
	}

	/**
	 * Invoked the first time a window is made visible.
	 * <p>
	 * Requests the focus for {@link #pbCancel}.
	 * @param we the <code>WindowEvent</code>
	 */
	public void windowOpened(WindowEvent we)
	{
		try
		{
			if (we.getSource() == this)
			{
				this.pbCancel.requestFocusInWindow();
			}
		}
		catch (Throwable throwable)
		{
			throwable.printStackTrace();
		}
	}
}
