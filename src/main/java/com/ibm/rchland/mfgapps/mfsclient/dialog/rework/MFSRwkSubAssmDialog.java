/* @ Copyright IBM Corporation 2004, 2007. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2007-01-21      34242JR  R Prechel        -Java 5 version
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.dialog.rework;

import javax.swing.DefaultListModel;

import com.ibm.rchland.mfgapps.mfsclient.MFSFrame;
import com.ibm.rchland.mfgapps.mfsclient.MFSListDialog;

/**
 * <code>MFSRwkSubAssmDialog</code> displays options allowing the user to
 * rework an entire subassembly or parts in the subassembly.
 * @author The MFS Client Development Team
 */
public class MFSRwkSubAssmDialog
	extends MFSListDialog
{	
	private static final long serialVersionUID = 1L;
	/** The "Rework Entire Subassembly" rework option. */
	public static final String REWORK_ENTIRE = "Rework Entire Subassembly";
	
	/** The "Rework Parts in Subassembly" rework option. */
	public static final String REWORK_PARTS = "Rework Parts in Subassembly";
	
	/**
	 * Constructs a new <code>MFSRwkSubAssmDialog</code>.
	 * @param parent the <code>MFSFrame</code> that caused this
	 *        <code>MFSRwkSubAssmDialog</code> to be displayed
	 */
	public MFSRwkSubAssmDialog(MFSFrame parent)
	{
		super(parent, "Rework Subassembly");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		DefaultListModel model = new DefaultListModel();
		model.addElement(REWORK_ENTIRE);
		model.addElement(REWORK_PARTS);
		this.lstOptions.setModel(model);
		this.lstOptions.setVisibleRowCount(5);
		
		createLayout();
		addMyListeners();
		pack();
	}
}
