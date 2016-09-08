/* © Copyright IBM Corporation 2005, 2007. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2007-02-21      34242JR  R Prechel        -Java 5 version
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.dialog;

import javax.swing.DefaultListModel;

import com.ibm.rchland.mfgapps.mfsclient.MFSFrame;
import com.ibm.rchland.mfgapps.mfsclient.MFSListDialog;

/**
 * <code>MFSReapplyTypeDialog</code> is the <code>MFSListDialog</code> used
 * to display the reapply options.
 * @author The MFS Client Development Team
 */
public class MFSReapplyTypeDialog
	extends MFSListDialog
{
	private static final long serialVersionUID = 1L;
	/** The "BRUTE FORCE" reapply option. */
	public final String BRUTE_FORCE = "BRUTE FORCE";
	
	/** The "BRUTE FORCE -> MCTL" reapply option. */
	public final String BRUTE_FORCE_MCTL = "BRUTE FORCE -> MCTL";

	/**
	 * Constructs a new <code>MFSReapplyTypeDialog</code>.
	 * @param parent the <code>MFSFrame</code> that caused this
	 *        <code>MFSReapplyTypeDialog</code> to be displayed
	 */
	public MFSReapplyTypeDialog(MFSFrame parent)
	{
		super(parent, "Reapply Type");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		DefaultListModel model = new DefaultListModel();
		model.addElement(this.BRUTE_FORCE);
		model.addElement(this.BRUTE_FORCE_MCTL);
		this.lstOptions.setModel(model);
		this.lstOptions.setVisibleRowCount(5);
		
		createLayout();
		addMyListeners();
		pack();
	}
}
