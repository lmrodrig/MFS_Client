/* © Copyright IBM Corporation 2007. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2007-08-10      39464JM  R Prechel        -Initial version
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.utils;

import java.util.Hashtable;
import java.util.Vector;

import com.ibm.rchland.mfgapps.mfscommon.comm.MFSTransaction;

/**
 * <code>MFSTransactionUtils</code> provides utility methods to setup the
 * input for a transaction or parse the output from a transaction.
 * @author The MFS Client Development Team
 */
public class MFSTransactionUtils
{
	/**
	 * Parses the output of <code>RTV_CUODKY</code> into a <code>Vector</code>
	 * of <code>Hashtable</code>s.
	 * @param rtv_cuodky the <code>MFSTransaction</code> for
	 *        <code>RTV_CUODKY</code>
	 * @return a <code>Vector</code> of <code>Hashtable</code>s
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Vector parseRTV_CUODKY(MFSTransaction rtv_cuodky)
	{
		final String OUTPUT = rtv_cuodky.getOutput();
		final int RECORD_LENGTH = 182;
		final int RECORD_COUNT = OUTPUT.length() / RECORD_LENGTH;

		Vector result = new Vector(RECORD_COUNT);
		String unparsed = OUTPUT;

		do
		{
			Hashtable rowData = new Hashtable();
			rowData.put("MATP", unparsed.substring(0, 4)); //$NON-NLS-1$
			rowData.put("SSN", unparsed.substring(4, 12)); //$NON-NLS-1$
			rowData.put("CCIN", unparsed.substring(12, 16)); //$NON-NLS-1$
			rowData.put("CCSN", unparsed.substring(16, 26)); //$NON-NLS-1$
			rowData.put("CCID", unparsed.substring(26, 42)); //$NON-NLS-1$
			rowData.put("ACQY", unparsed.substring(42, 47)); //$NON-NLS-1$
			rowData.put("TACT", unparsed.substring(47, 52)); //$NON-NLS-1$
			rowData.put("CODE", unparsed.substring(52, 86)); //$NON-NLS-1$
			rowData.put("ORNO", unparsed.substring(86, 92)); //$NON-NLS-1$
			rowData.put("PART", unparsed.substring(92, 104)); //$NON-NLS-1$
			rowData.put("ODRI", unparsed.substring(104, 108)); //$NON-NLS-1$
			rowData.put("ODTN", unparsed.substring(108, 114)); //$NON-NLS-1$
			rowData.put("BRLN", unparsed.substring(114, 122)); //$NON-NLS-1$
			rowData.put("FFBM", unparsed.substring(122, 134)); //$NON-NLS-1$
			rowData.put("INPN", unparsed.substring(134, 146)); //$NON-NLS-1$

			unparsed = unparsed.substring(RECORD_LENGTH);
			result.addElement(rowData);
		}
		while (unparsed.trim().length() > 0 && unparsed.length() >= RECORD_LENGTH);

		return result;
	}
}