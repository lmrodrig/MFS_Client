/* @ Copyright IBM Corporation 2007. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2007-01-23      34242JR  R Prechel        -Initial version
 * 2007-11-06   ~1 40104PB  R Prechel        -Do not implement FocusListener
 *                                           -Rename DBFieldName to ID
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient;

import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.SwingConstants;

import com.ibm.rchland.mfgapps.mfsclient.utils.MFSConstants;

/**
 * <code>MFSLabelButton</code> is a subclass of <code>JButton</code> that
 * has the appearance of a <code>JLabel</code> but the behavior of a
 * <code>JButton</code> (i.e., clickable).
 * @author The MFS Client Development Team
 */
public class MFSLabelButton
	extends JButton
{
	private static final long serialVersionUID = 1L;
	//~1C Changed variable name
	/** The ID associated with this <code>MFSLabelButton</code>. */
	private String fieldID;

	/** The <code>MFSLabelButton</code>'s label text. */
	private String fieldLabelText;

	/** The <code>MFSLabelButton</code>'s value text. */
	private String fieldValueText;

	//~1C Changed second parameter name to id
	//~1D No longer adds itself as a FocusListener
	/**
	 * Constructs a new <code>MFSLabelButton</code>.
	 * @param label the label text displayed on the button
	 * @param id the ID to associate with the button
	 */
	public MFSLabelButton(String label, String id)
	{
		super(label);
		this.fieldLabelText = label;
		this.fieldID = id;
		setActionCommand(label);
		setBackground(MFSConstants.PRIMARY_BACKGROUND_COLOR);
		setHorizontalAlignment(SwingConstants.LEFT);
		setBorderPainted(false);
		setMargin(new Insets(0, 0, 0, 0));
	}

	//~1C Changed method name
	/**
	 * Returns the ID associated with this <code>MFSLabelButton</code>.
	 * @return the ID
	 */
	public String getID()
	{
		return this.fieldID;
	}

	/**
	 * Returns the value of the label text property.
	 * @return the value of the label text property
	 */
	public String getLabelText()
	{
		return this.fieldLabelText;
	}

	/**
	 * Returns the value of the value text property.
	 * @return the value of the value text property
	 */
	public String getValueText()
	{
		return this.fieldValueText;
	}

	/**
	 * Sets the value of the label text property.
	 * @param text the text for the label text property
	 */
	public void setLabelText(String text)
	{
		this.fieldLabelText = text;
	}

	/**
	 * Sets the value of the value text property and sets the button's text. If
	 * <code>value</code> is <code>null</code>, the button's text is set to
	 * the label text. Otherwise, the button's text is set to the label text
	 * plus the <code>String</code> value of <code>value</code>.
	 * @param value the <code>Object</code> used to set the value text
	 */
	public void setValueText(Object value)
	{
		if (value == null)
		{
			this.fieldValueText = null;
			super.setText(this.fieldLabelText);
		}
		else
		{
			this.fieldValueText = value.toString();
			super.setText(this.fieldLabelText + this.fieldValueText);
		}
	}
}
