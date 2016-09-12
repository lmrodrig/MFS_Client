/* @ Copyright IBM Corporation 2006,2010. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2006-06-14      30260AT  R Prechel        -Initial version
 * 2006-06-15   ~1 34144JJ  JL Woodward      -Added BUFFER_SIZE constant
 * 2006-06-28   ~2 31801JM  R Prechel        -Added 6 new constants
 * 2007-02-15   ~3 34242JR  R Prechel        -Added Font and MAX constants.
 *                                           -Removed BUTTON_GRID_LAYOUT_GAP, LINE, EMPTY_STRING, ENCODING
 * 2010-03-12   ~4 42558JL  Santiago SC      -mfs client package                                         
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.utils;

import java.awt.Color;
import java.awt.Font;

/**
 * <code>MFSConstants</code> is an interface containing a collection of
 * constants used by the <i>MFS Client</i>.
 * @author The MFS Client Development Team
 */
public interface MFSConstants
{
	/** The primary panel background <code>Color</code>. */
	public static final Color PRIMARY_BACKGROUND_COLOR = Color.white;

	/** The primary background highlight <code>Color</code>. */
	public static final Color PRIMARY_BACKGROUND_HIGHLIGHT_COLOR = new Color(255, 255, 204);

	/** The primary foreground <code>Color</code>. */
	public static final Color PRIMARY_FOREGROUND_COLOR = Color.black;

	/** The primary foreground highlight <code>Color</code>. */
	public static final Color PRIMARY_FOREGROUND_HIGHLIGHT_COLOR = Color.blue;

	/** The background <code>Color</code> used to render selected table cells. */
	public static final Color SELECTED_CELL_BACKGROUND_COLOR = new Color(204, 204, 255);

	/** Max buffer size for transaction calling * */
	public static final int BUFFER_SIZE = 4096; //~1A

	/** The number of columns for a small length input <code>JTextField</code>. */
	public static final int SMALL_TF_COLS = 12; //~2A

	/** The number of columns for a medium length input <code>JTextField</code>. */
	public static final int MEDIUM_TF_COLS = 18; //~2A

	/** The number of columns for a large length input <code>JTextField</code>. */
	public static final int LARGE_TF_COLS = 24; //~2A

	/**
	 * The value of the barcode a user scans
	 * to log a part or enter part information.
	 */
	public static final String LOG_BARCODE = "LOG"; //~2A //$NON-NLS-1$

	/** The maximum number of characters in a part number. */
	public static final int MAX_PN_CHARACTERS = 12; //~3A

	/** The maximum number of characters in a serial number. */
	public static final int MAX_SN_CHARACTERS = 12; //~3A

	/** A dialog <code>Font</code> with the small MFS size and style. */
	public static final Font SMALL_DIALOG_FONT = new Font("Dialog", Font.BOLD, 12); //~3A //$NON-NLS-1$

	/** A dialog <code>Font</code> with the medium MFS size and style. */
	public static final Font MEDIUM_DIALOG_FONT = new Font("Dialog", Font.BOLD, 14); //~3A //$NON-NLS-1$

	/** A dialog <code>Font</code> with the large MFS size and {@link Font#PLAIN} style. */
	public static final Font LARGE_PLAIN_DIALOG_FONT = new Font("Dialog", Font.PLAIN, 18); //~3A //$NON-NLS-1$

	/** A monospaced <code>Font</code> with the small MFS size and style. */
	public static final Font SMALL_MONOSPACED_FONT = new Font("Monospaced", Font.BOLD, 12); //~3A //$NON-NLS-1$

	/** A monospaced <code>Font</code> with the medium MFS size and style. */
	public static final Font MEDIUM_MONOSPACED_FONT = new Font("Monospaced", Font.BOLD, 14); //~3A //$NON-NLS-1$

	/** A monospaced <code>Font</code> with the large MFS size and style. */
	public static final Font LARGE_MONOSPACED_FONT = new Font("Monospaced", Font.BOLD, 18); //~3A //$NON-NLS-1$
	
	
	/** MFS Client main package */
	public static final String MFS_CLIENT_PACKAGE = "com.ibm.rchland.mfgapps.mfsclient"; //~4A
}
