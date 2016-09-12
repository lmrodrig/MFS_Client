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
 * <code>MFSRwkDialog</code> displays the list of Rework options.
 * @author The MFS Client Development Team
 */
public class MFSRwkDialog
	extends MFSListDialog
{
	private static final long serialVersionUID = 1L;
	/** The "Replace Current Part" rework option. */
	public static final String REPLACE_CURRENT = "Replace Current Part";

	/** The "Remove Part & UnFkit" rework option. */
	public static final String REMOVE_UNFKIT = "Remove Part & UnFkit";

	/** The "Remove Current Part" rework option. */
	public static final String REMOVE_CURRENT = "Remove Current Part";

	/** The "Enter IRcode" rework option. */
	public static final String IRCODE = "Enter IRcode";

	/** The full load flag. */
	public static final String LF_FULL = "full"; //$NON-NLS-1$

	/** The partial load flag. */
	public static final String LF_PARTIAL = "partial"; //$NON-NLS-1$

	/**
	 * Constructs a new <code>MFSRwkDialog</code>.
	 * @param parent the <code>MFSFrame</code> that caused this
	 *        <code>MFSRwkDialog</code> to be displayed
	 * @param loadFlag controls which options are displayed by the list. Should
	 *        be {@link #LF_FULL} for all four options or {@link #LF_PARTIAL}
	 *        for only two options
	 */
	public MFSRwkDialog(MFSFrame parent, String loadFlag)
	{
		super(parent, "Rework");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		DefaultListModel model = new DefaultListModel();
		model.addElement(REPLACE_CURRENT);
		if (loadFlag.equals(LF_FULL))
		{
			model.addElement(REMOVE_UNFKIT);
			model.addElement(REMOVE_CURRENT);
		}
		model.addElement(IRCODE);
		this.lstOptions.setModel(model);
		this.lstOptions.setVisibleRowCount(5);

		createLayout();
		addMyListeners();
		pack();
	}
}
