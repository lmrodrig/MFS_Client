/* @ Copyright IBM Corporation 2008. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2008-04-03      37616JL  R Prechel        -Initial version
 * 2008-05-23   ~1 41674JM  Santiago D		 -PackCode insted of DimCode, changed weights and dims
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.struct;

import java.text.MessageFormat;

import com.ibm.rchland.mfgapps.client.utils.exception.IGSException;
import com.ibm.rchland.mfgapps.client.utils.xml.IGSXMLDocument;

/**
 * <code>MFSWeightDim</code> is a data structure for storing weight,
 * dimensions, and a dimension code.
 * @author The MFS Client Development Team
 */
public class MFSWeightDim
{
	/** The pack code. */
	private final String fieldPackCode; //~1C

	/** The length. */
	private final String fieldLength;

	/** The width. */
	private final String fieldWidth;

	/** The height. */
	private final String fieldHeight;

	/** The weight. */
	private final String fieldWeight;
	
	/** The max dimension. */
	private final String fieldMaxDimension; //~1A
	
	/** The max weight. */ 
	private final String fieldMaxWeight; //~1A
	
	/**
	 * Constructs a new <code>MFSWeightDim</code>.
	 * @param packCode the pack code
	 * @param length the length
	 * @param width the width
	 * @param height the height
	 * @param weight the weight
	 */
	public MFSWeightDim(String packCode, String length, String width, String height,
						String weight, String maxDimension, String maxWeight) //~1C
	{
		this.fieldPackCode = packCode;
		this.fieldLength = length;
		this.fieldWidth = width;
		this.fieldHeight = height;
		this.fieldWeight = weight;
		this.fieldMaxDimension = maxDimension; //~1A
		this.fieldMaxWeight = maxWeight; //~1A
	}

	/**
	 * Constructs a new <code>MFSWeightDim</code>.
	 * @param data the XML document with the weight and dimension data
	 */
	public MFSWeightDim(IGSXMLDocument data)
	{
		this.fieldPackCode = data.getNextElement("PACK").trim(); //$NON-NLS-1$ //~1C
		this.fieldLength = data.getNextElement("LGTH").trim(); //$NON-NLS-1$
		this.fieldWidth = data.getNextElement("WDTH").trim(); //$NON-NLS-1$
		this.fieldHeight = data.getNextElement("HGHT").trim(); //$NON-NLS-1$
		/* WGHT is optional, so weight might be assigned null. */
		String weight = data.getNextElement("WGHT"); //$NON-NLS-1$
		if (weight == null)
		{
			this.fieldWeight = ""; //$NON-NLS-1$
		}
		else
		{
			this.fieldWeight = weight.trim();
		}
		
		String maxDimension = data.getNextElement("MDIM"); //~1A
		if (maxDimension == null)
		{
			this.fieldMaxDimension = "";
		}
		else
		{
			this.fieldMaxDimension = maxDimension.trim(); //~1A
		}
		
		String maxWeight = data.getNextElement("MWGT"); //~1A
		if (maxWeight == null)
		{
			this.fieldMaxWeight = "";
		}
		else
		{
			this.fieldMaxWeight = maxWeight.trim(); //~1A
		}
		
		
	}

	/**
	 * Parses a dimension from a <code>String</code>.
	 * @param name the name of the dimension
	 * @param value the <code>String</code> value of the dimension
	 * @return the <code>int</code> value of the dimension
	 * @throws IGSException if <code>value</code> cannot be parsed
	 */
	private int parseDim(String name, String value)
		throws IGSException
	{
		try
		{
			return Integer.parseInt(value);
		}
		catch (Exception e)
		{
			String erms = "Invalid value for {0}: {1} is not a number.";
			Object arguments[] = {
					name, value
			};
			erms = MessageFormat.format(erms, arguments);
			throw new IGSException(erms, false);
		}
	}

	/**
	 * Parses a weight from a <code>String</code>.
	 * @param name the name of the weight
	 * @param value the <code>String</code> value of the weight
	 * @return the <code>float</code> value of the weight
	 * @throws IGSException if <code>value</code> cannot be parsed
	 */
	private float parseWeight(String name, String value)
		throws IGSException
	{
		try
		{
			return Float.parseFloat(value);
		}
		catch (Exception e)
		{
			String erms = "Invalid value for {0}: {1} is not a number.";
			Object arguments[] = {
					name, value
			};
			erms = MessageFormat.format(erms, arguments);
			throw new IGSException(erms, false);
		}
	}

	/**
	 * Validates a dimension.
	 * @param name the name of the dimension
	 * @param value the <code>String</code> value of the dimension
	 * @param maxValue the maximum value for the dimension
	 * @param maxValueString the maximum value for the dimension as a
	 *        <code>String</code>
	 * @throws IGSException if <code>value</code> cannot be parsed, is not
	 *         positive, or is greater than <code>maxValue</code>
	 */
	private void validateDim(String name, String value, int maxValue,
								String maxValueString)
		throws IGSException
	{
		int numericValue = parseDim(name, value);
		if (numericValue <= 0)
		{
			String erms = "Invalid value for {0}: {1} is not positive.";
			Object arguments[] = {
					name, value
			};
			erms = MessageFormat.format(erms, arguments);
			throw new IGSException(erms, false);
		}

		if (numericValue > maxValue)
		{
			String erms = "Invalid value for {0}: {1} is greater than {2}.";
			Object arguments[] = {
					name, value, maxValueString
			};
			erms = MessageFormat.format(erms, arguments);
			throw new IGSException(erms, false);
		}
	}

	/**
	 * Validates a weight.
	 * @param name the name of the weight
	 * @param value the <code>String</code> value of the weight
	 * @param maxValue the maximum value for the weight
	 * @param maxValueString the maximum value for the weight as a
	 *        <code>String</code>
	 * @throws IGSException if <code>value</code> cannot be parsed, is not
	 *         positive, or is greater than <code>maxValue</code>
	 */
	private void validateWeight(String name, String value, float maxValue,
								String maxValueString)
		throws IGSException
	{
		float numericValue = parseWeight(name, value);
		if (numericValue <= 0)
		{
			String erms = "Invalid value for {0}: {1} is not positive.";
			Object arguments[] = {
					name, value
			};
			erms = MessageFormat.format(erms, arguments);
			throw new IGSException(erms, false);
		}

		if (numericValue > maxValue)
		{
			String erms = "Invalid value for {0}: {1} is greater than {2}.";
			Object arguments[] = {
					name, value, maxValueString
			};
			erms = MessageFormat.format(erms, arguments);
			throw new IGSException(erms, false);
		}
	}

	/**
	 * Validates the container's weight and dimension.
	 * @param maxDimension the maximum container dimension
	 * @param maxWeight the maximum container weight
	 * @throws IGSException if a weight or dimension value cannot be parsed, is
	 *         not positive, or is greater than the maximum
	 */
	public void validate(String maxDimension, String maxWeight)
		throws IGSException
	{
		int maxDimensionI = parseDim("maximum dimension", maxDimension);
		float maxWeightF = parseWeight("maximum weight", maxWeight);
		validateDim("length", this.fieldLength, maxDimensionI, maxDimension);
		validateDim("width", this.fieldWidth, maxDimensionI, maxDimension);
		validateDim("height", this.fieldHeight, maxDimensionI, maxDimension);
		validateWeight("weight", this.fieldWeight, maxWeightF, maxWeight);
	}

	/**
	 * Returns <code>true</code> if this <code>MFSWeightDim</code> has the
	 * same dimensions as the specified <code>MFSWeightDim</code>.
	 * @param dim an <code>MFSWeightDim</code>
	 * @return <code>true</code> if the dimensions are the same;
	 *         <code>false</code> otherwise
	 */
	public boolean sameDimensions(MFSWeightDim dim)
	{
		return this.fieldHeight.equals(dim.fieldHeight)
				&& this.fieldWidth.equals(dim.fieldWidth)
				&& this.fieldLength.equals(dim.fieldLength);
	}

	/**
	 * Returns the pack code.
	 * @return the pack code
	 */
	public String getPackCode()
	{
		return this.fieldPackCode;
	}

	/**
	 * Returns the length.
	 * @return the length
	 */
	public String getLength()
	{
		return this.fieldLength;
	}

	/**
	 * Returns the width.
	 * @return the width
	 */
	public String getWidth()
	{
		return this.fieldWidth;
	}

	/**
	 * Returns the height.
	 * @return the height
	 */
	public String getHeight()
	{
		return this.fieldHeight;
	}

	/**
	 * Returns the weight.
	 * @return the weight
	 */
	public String getWeight()
	{
		return this.fieldWeight;
	}
	
	//~1A
	/**
	 * Returns the maxDimension.
	 * @return the maxDimension
	 */
	public String getMaxDimension()
	{
		return this.fieldMaxDimension;
	}	
	
	//~1A
	/**
	 * Returns the maxWeight.
	 * @return the maxWeight
	 */
	public String getMaxWeight()
	{
		return this.fieldMaxWeight;
	}
}

