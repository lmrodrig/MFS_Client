/* © Copyright IBM Corporation 2007. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2007-02-15      34242JR  R Prechel        -Initial version
 * 2007-03-30   ~1 38166JM  R Prechel        -Add setLocationRelativeTo for dialog
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient;

import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import mfsxml.MfsXMLDocument;
import mfsxml.MfsXMLParser;

import com.ibm.rchland.mfgapps.client.utils.messagebox.IGSMessageBox;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSInstructionSourceDialog;
import com.ibm.rchland.mfgapps.mfsclient.struct.MFSHeaderRec;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSComm;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSTransaction;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSXmlTransaction;

/**
 * <code>MFSIRSourceButton</code> is a subclass of <code>JButton</code> used
 * with Interactive Routing to view the source of an instruction.
 * @author The MFS Client Development Team
 */
public class MFSIRSourceButton
	extends JButton
	implements ActionListener
{
	private static final long serialVersionUID = 1L;

	/** The <code>Font</code> for an <code>MFSIRSourceButton</code>. */
	public static final Font FONT = new Font("Monospaced", Font.BOLD, 10); //$NON-NLS-1$

	/**
	 * The <code>MFSPartInstructionJPanel</code> that displays this
	 * <code>MFSIRSourceButton</code>.
	 */
	private MFSPartInstructionJPanel fieldPip;

	/**
	 * Constructs a new <code>MFSIRSourceButton</code>.
	 * @param pip the <code>MFSPartInstructionJPanel</code> that will display
	 *        this <code>MFSIRSourceButton</code>
	 */
	public MFSIRSourceButton(MFSPartInstructionJPanel pip)
	{
		super("SOURCE");
		this.fieldPip = pip;
		addActionListener(this);

		setFont(FONT);
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
				MFSInstructionsPanel panel = this.fieldPip.getInstructionsPanel();
				MFSHeaderRec headerRec = panel.getHeaderRec();
				String rowName = this.fieldPip.getName();
				rowName = rowName.substring(rowName.indexOf("Row")); //$NON-NLS-1$
				String suff = rowName.substring(3, 13);
				String seq = rowName.substring(13, 18);

				MfsXMLDocument xml = new MfsXMLDocument("RTV_INSRC"); //$NON-NLS-1$
				xml.addOpenTag("DATA"); //$NON-NLS-1$
				xml.addCompleteField("MFGN", headerRec.getMfgn()); //$NON-NLS-1$
				xml.addCompleteField("IDSS", headerRec.getIdss()); //$NON-NLS-1$
				xml.addCompleteField("MCTL", panel.getCurrMctl()); //$NON-NLS-1$
				xml.addCompleteField("NMBR", headerRec.getNmbr()); //$NON-NLS-1$
				xml.addCompleteField("INSX", suff); //$NON-NLS-1$
				xml.addCompleteField("NMSQ", seq); //$NON-NLS-1$
				xml.addCloseTag("DATA"); //$NON-NLS-1$
				xml.finalizeXML();

				MFSTransaction rtv_insrc = new MFSXmlTransaction(xml.toString());
				rtv_insrc.setActionMessage("Retrieving Instruction Source, Please Wait...");
				MFSComm.getInstance().execute(rtv_insrc, panel);

				MFSFrame parent = panel.getParentFrame();
				if (rtv_insrc.getReturnCode() != 0)
				{
					IGSMessageBox.showOkMB(parent, null, rtv_insrc.getErms(), null);
				}
				else
				{
					MFSInstructionSourceDialog dialog = 
						new MFSInstructionSourceDialog(parent);
					dialog.setLabelText(suff, seq);
					dialog.loadUpDialog(new MfsXMLParser(rtv_insrc.getOutput()));
					dialog.setLocationRelativeTo(parent); //~1A
					dialog.setVisible(true);
				}

				panel.spPartsInst.requestFocusInWindow();
			}
			catch (Exception e)
			{
				IGSMessageBox.showOkMB(this.fieldPip, null, null, e);
			}
		}
	}
}
