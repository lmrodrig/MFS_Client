/* © Copyright IBM Corporation 2007. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2007-02-15      34242JR  R Prechel        -Initial version
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.utils;

import java.io.UnsupportedEncodingException;
import java.util.Comparator;

import com.ibm.rchland.mfgapps.mfsclient.struct.MFSComponentRec;
import com.ibm.rchland.mfgapps.mfsclient.struct.MFSInstructionRec;

/**
 * <code>MFSCP500Comparator</code> is a <code>Comparator</code> that uses
 * the CP500 character encoding to compare <code>Object</code>s. (CP500 is
 * the version of EBCDIC used by the System i.)
 * @author The MFS Client Development Team
 */
@SuppressWarnings("rawtypes")
public class MFSCP500Comparator
	implements Comparator
{

	/**
	 * Compares the two arguments for order.
	 * @param object1 the first object to be compared
	 * @param object2 the second object to be compared
	 * @return a negative integer, zero, or a positive integer as the first
	 *         argument is less than, equal to, or greater than the second
	 * @throws ClassCastException if the arguments cannot be compared
	 */
	public int compare(Object object1, Object object2)
		throws ClassCastException
	{
		try
		{
			byte[] bytes1 = cp500Value(object1);
			byte[] bytes2 = cp500Value(object2);

			int end = (bytes1.length < bytes2.length ? bytes1.length : bytes2.length);
			int result;
			for (int i = 0; i < end; i++)
			{
				// This method compares the numeric values of a CP500 String.
				// The range for the numeric value of a character is 0:255.
				// However, a Java byte is an 8-bit two's complement number.
				// Thus, values above 127 are negative (i.e., a byte can store
				// 256 values, but the range is -128:127 and not 0:255).
				// Masking the 8-bit value as an integer converts the range of
				// the byte from -128:127 to 0:255. (A type cast by itself
				// will not work because the value will sign extend.)
				// The numeric values can then be compared as positive numbers.
				int i1 = 0xFF & bytes1[i];
				int i2 = 0xFF & bytes2[i];

				if ((result = i1 - i2) != 0)
				{
					return result;
				}
			}

			return bytes1.length - bytes2.length;
		}
		catch (UnsupportedEncodingException uee)
		{
			//compare cannot throw an UnsupportedEncodingException,
			//so the Exception is thrown as a ClassCastException
			throw new ClassCastException(uee.toString());
		}
	}

	/**
	 * Returns the CP500 byte array value for the <code>String</code> value of
	 * the specified <code>object</code>. If <code>object</code> is an
	 * <code>MFSComponentRec</code> or an <code>MFSInstructionRec</code>,
	 * the <code>String</code> value is the sequence and suffix of the record;
	 * otherwise the <code>String</code> value is obtained by
	 * {@link Object#toString()}.
	 * @param object an <code>Object</code>
	 * @return the CP500 byte array value of <code>Object</code>
	 * @throws UnsupportedEncodingException is CP500 is not supported
	 */
	private byte[] cp500Value(Object object)
		throws UnsupportedEncodingException
	{
		String string;
		if (object instanceof MFSComponentRec)
		{
			MFSComponentRec comp = (MFSComponentRec) object;
			string = comp.getNmsq() + comp.getSuff();
		}
		else if (object instanceof MFSInstructionRec)
		{
			MFSInstructionRec inst = (MFSInstructionRec) object;
			string = inst.getIseq() + inst.getSuff();
		}
		else
		{
			string = object.toString();
		}
		return string.getBytes("CP500"); //$NON-NLS-1$
	}
}
