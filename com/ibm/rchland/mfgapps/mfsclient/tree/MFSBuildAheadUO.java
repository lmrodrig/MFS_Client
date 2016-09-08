/* © Copyright IBM Corporation 2009. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2009-08-29      37550JL  Ray Perry        -Initial version
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.tree;

import java.awt.Color;
import java.lang.reflect.Field;
import java.util.ResourceBundle;

import com.ibm.rchland.mfgapps.client.utils.IGSI18N;
import com.ibm.rchland.mfgapps.client.utils.exception.IGSRuntimeException;
import com.ibm.rchland.mfgapps.client.utils.xml.IGSUserObject;

/**
 * <code>MFSBuildAheadUO</code> is the base class for user objects in an
 * BuildAhead tree.
 * @author The MFS Client Development Team
 */
public class MFSBuildAheadUO
	implements IGSUserObject
{
	/**
	 * The <code>ResourceBundle</code> used by <code>MFSBuildAheadUO</code>
	 * and its subclasses.
	 */
	public static final ResourceBundle bundle = IGSI18N.getBundle(MFSBuildAheadUO.class);

	/** The <code>String</code> representation of the user object. */
	protected String string = null;

	/** The tool tip <code>String</code> for the user object. */
	protected String toolTipString = null;

	/** The tree display <code>String</code> for the user object. */
	protected String treeString = null;

	/** Constructs a new <code>MFSBuildAheadUO</code>. */
	public MFSBuildAheadUO()
	{
		super();
	}

	/** {@inheritDoc} */
	@SuppressWarnings("rawtypes")
	public void add(String elementName, String elementData)
	{
		try
		{
			Field field = getClass().getDeclaredField(elementName);
			Class type = field.getType();
			if (String.class.equals(type))
			{
				field.set(this, elementData.trim());
			}
			else if (char.class.equals(type))
			{
				field.setChar(this, elementData.charAt(0));
			}
			else if (boolean.class.equals(type))
			{
				field.setBoolean(this, elementData.equals("Y")); //$NON-NLS-1$
			}
            else if (int.class.equals(type))
            {
                field.setInt(this, Integer.parseInt(elementData)); //$NON-NLS-1$
            }
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			throw new IGSRuntimeException(ex, true);
		}
	}

	/**
	 * Clears the cached <code>String</code> values so the <code>String</code>
	 * values are recreated next time they are needed.
	 */
	protected void clearCachedStrings()
	{
		this.string = null;
		this.toolTipString = null;
		this.treeString = null;
	}

	/**
	 * Returns the background <code>Color</code> of the node.
	 * @param selected <code>true</code> if the node is selected;
	 *        <code>false</code> if the node is not selected
	 * @return the background <code>Color</code>
	 */
	public Color getBackground(boolean selected)
	{
		if (selected)
		{
			return getColor();
		}
		return Color.WHITE;
	}

	/**
	 * Returns the foreground <code>Color</code> of the node.
	 * @param selected <code>true</code> if the node is selected;
	 *        <code>false</code> if the node is not selected
	 * @return the foreground <code>Color</code>
	 */
	public Color getForeground(boolean selected)
	{
		if (selected)
		{
			return Color.WHITE;
		}
		return getColor();
	}

	/**
	 * Returns the color for the node.
	 * @return the node <code>Color</code>
	 */
	protected Color getColor()
	{
		return Color.BLACK;
	}

	/**
	 * Creates a <code>String</code> representation of the user object.
	 * @param type the type of <code>String</code> representation
	 * @return a <code>String</code> representation of the user object
	 */
	protected String createString(String type)
	{
		return super.toString() + ' ' + type;
	}

	/**
	 * Returns <code>true</code> if the specified <code>field</code> value
	 * should be displayed for the specified type of <code>String</code>
	 * representation.
	 * @param type the type of <code>String</code> representation
	 * @param field a field value
	 * @return <code>true</code> if the field value should be displayed;
	 *         <code>false</code> otherwise
	 */
	protected boolean display(String type, String field)
	{
		return (!"tree".equals(type) || field.length() > 0); //$NON-NLS-1$
	}

	/**
	 * Returns the tool tip <code>String</code> for the user object.
	 * @return the tool tip <code>String</code> for the user object.
	 */
	public String getToolTipString()
	{
		if (this.toolTipString == null)
		{
			this.toolTipString = createString("toolTip"); //$NON-NLS-1$
		}
		return this.toolTipString;
	}

	/**
	 * Returns the tree display <code>String</code> used by
	 * {@link com.ibm.rchland.mfgapps.mfsclient.renderer.MFSBuildAheadTreeCellRenderer}
	 * when rendering the user object.
	 * @return the tree display <code>String</code>
	 */
	public String getTreeString()
	{
		if (this.treeString == null)
		{
			this.treeString = createString("tree"); //$NON-NLS-1$
		}
		return this.treeString;
	}

	/**
	 * Returns a <code>String</code> representation of the user object.
	 * @return a <code>String</code> representation of the user object
	 */
	public String toString()
	{
		if (this.string == null)
		{
			this.string = createString("string"); //$NON-NLS-1$
		}
		return this.string;
	}
}
