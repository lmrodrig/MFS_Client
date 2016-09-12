/* @ Copyright IBM Corporation 2007. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2007-02-15      34242JR  R Prechel        -Initial version
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import com.ibm.rchland.mfgapps.client.utils.messagebox.IGSMessageBox;

/**
 * <code>MFSIRCompletionButton</code> is a subclass of <code>JButton</code>
 * used with Interactive Routing to mark an instruction complete.
 * @author The MFS Client Development Team
 */
public class MFSIRCompletionButton
	extends JButton
	implements ActionListener
{
	private static final long serialVersionUID = 1L;
	/**
	 * The <code>MFSPartInstructionJPanel</code> that displays this
	 * <code>MFSIRCompletionButton</code>.
	 */
	private MFSPartInstructionJPanel fieldPip;

	/**
	 * Constructs a new <code>MFSIRCompletionButton</code>.
	 * @param pip the <code>MFSPartInstructionJPanel</code> that will display
	 *        this <code>MFSIRCompletionButton</code>
	 */
	public MFSIRCompletionButton(MFSPartInstructionJPanel pip)
	{
		super("Complete");
		this.fieldPip = pip;
		addActionListener(this);

		setMargin(new Insets(2, 2, 2, 2));
	}

	/**
	 * Invoked when an action occurs.
	 * @param ae the <code>ActionEvent</code>
	 */
	public void actionPerformed(ActionEvent ae)
	{
		if (ae.getSource() == this)
		{
			try
			{
				this.fieldPip.handleComplete();
			}
			catch (Exception e)
			{
				IGSMessageBox.showOkMB(this.fieldPip, null, null, e);
			}
		}
	}
}
