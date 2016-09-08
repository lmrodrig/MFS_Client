/* © Copyright IBM Corporation 2008. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2008-03-15      37616JL  R Prechel        -Initial version
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.tree;

import java.awt.Color;

/**
 * <code>MFSEntityMergeCntrUO</code> is the <code>MFSEntityMergeUO</code>
 * for a container (CNTR) node.
 * @author The MFS Client Development Team
 */
public class MFSEntityMergeCntrUO
	extends MFSEntityMergeUO
{
	/** The container type for an entity container. */
	public final static char TYPE_ENTITY = 'E';

	/** The container type for a merged entity container. */
	public final static char TYPE_MERGED_ENTITY = 'N';

	/** The container type for a system container. */
	public final static char TYPE_SYSTEM = 'S';

	/** The container type for a merged system container. */
	public final static char TYPE_MERGED_SYSTEM = 'M';

	/** The container status for an acknowledged container. */
	public final static char STATUS_ACK = 'A';

	/** The container status for an acknowledged container. */
	public final static char STATUS_ACK_COMPLETE = 'B';

	/** The container status for container involved with an MFGN with an alter. */
	public final static char STATUS_ALTER_PENDING = 'P';

	/** The container status for a complete container. */
	public final static char STATUS_COMPLETE = 'C';

	/** The container status for a not ready container. */
	public final static char STATUS_NOT_READY = 'N';

	/** The container status for a ready container. */
	public final static char STATUS_READY = 'R';

	/** The container status for a shipped container. */
	public final static char STATUS_SHIP = 'S';

	/** The <code>Color</code> dark green. */
	private final static Color DARK_GREEN = new Color(0, 153, 0);

	/** The container number. */
	String cntr = null;

	/**
	 * The container's status. Should be one of:
	 * <ul>
	 * <li>{@link #STATUS_ACK}</li>
	 * <li>{@link #STATUS_ALTER_PENDING}</li>
	 * <li>{@link #STATUS_SHIP}</li>
	 * <li>{@link #STATUS_COMPLETE}</li>
	 * <li>{@link #STATUS_READY}</li>
	 * <li>{@link #STATUS_NOT_READY}</li>
	 * </ul>
	 */
	char cnts;

	/**
	 * The container's type. Should be one of:
	 * <ul>
	 * <li>{@link #TYPE_ENTITY}</li>
	 * <li>{@link #TYPE_MERGED_ENTITY}</li>
	 * <li>{@link #TYPE_SYSTEM}</li>
	 * <li>{@link #TYPE_MERGED_SYSTEM}</li>
	 * </ul>
	 */
	char cntt;

	/** The container's area. */
	String area = null;

	/** The container's location. */
	String cloc = null;

	/** <code>true</code> if the container is editable by entity merge. */
	boolean edit = false;

	/**
	 * Constructs a new <code>MFSEntityMergeCntrUO</code>.
	 * @param cntr the container number
	 */
	public MFSEntityMergeCntrUO(String cntr)
	{
		super();
		this.cntr = cntr;
	}

	/** {@inheritDoc} */
	public Color getColor()
	{
		switch (this.cnts)
		{
			case STATUS_READY:
			{
				if ((this.cntt == TYPE_MERGED_ENTITY)
						|| (this.cntt == TYPE_MERGED_SYSTEM))
				{
					return Color.BLUE;
				}
				return DARK_GREEN;
			}
			case STATUS_NOT_READY:
			{
				return Color.BLACK;
			}
			case STATUS_ALTER_PENDING:
			{
				return Color.RED;
			}
			default:
			{
				return Color.GRAY;
			}
		}
	}

	/** {@inheritDoc} */
	protected String createString(String type)
	{
		StringBuffer buffer = new StringBuffer(60);
		String separator = bundle.getString(type + "Separator"); //$NON-NLS-1$
		buffer.append(bundle.getString(type + "CNTR")); //$NON-NLS-1$
		buffer.append(this.cntr);
		buffer.append(separator);
		buffer.append(bundle.getString(type + "CNTS")); //$NON-NLS-1$
		buffer.append(this.cnts);
		buffer.append(separator);
		buffer.append(bundle.getString(type + "CNTT")); //$NON-NLS-1$
		buffer.append(this.cntt);

		if (display(type, this.area))
		{
			buffer.append(separator);
			buffer.append(bundle.getString(type + "AREA")); //$NON-NLS-1$
			buffer.append(this.area);
		}

		if (display(type, this.cloc))
		{
			buffer.append(separator);
			buffer.append(bundle.getString(type + "CLOC")); //$NON-NLS-1$
			buffer.append(this.cloc);
		}
		return buffer.toString();
	}

	/**
	 * Returns <code>true</code> if the container is an entity container
	 * (TYPE_ENTITY or TYPE_MERGED_ENTITY); <code>false</code> if the
	 * container is a system container (TYPE_SYSTEM or TYPE_MERGED_SYSTEM).
	 * @return <code>true</code> if the container is an entity container;
	 *         <code>false</code> if the container is a system container
	 */
	public boolean isEntityContainer()
	{
		return this.cntt == TYPE_ENTITY || this.cntt == TYPE_MERGED_ENTITY;
	}

	/**
	 * Returns <code>true</code> if the container is a merged container
	 * (TYPE_MERGED_ENTITY or TYPE_MERGED_SYSTEM); <code>false</code> if the
	 * container is a top level container (TYPE_ENTITY or TYPE_SYSTEM).
	 * @return <code>true</code> if the container is a merged container;
	 *         <code>false</code> if the container is a top level container
	 */
	public boolean isMergedContainer()
	{
		return this.cntt == TYPE_MERGED_ENTITY || this.cntt == TYPE_MERGED_SYSTEM;
	}

	/**
	 * Returns the container number.
	 * @return the container number
	 */
	public String getCntr()
	{
		return this.cntr;
	}

	/**
	 * Returns <code>true</code> if the container is editable by entity merge.
	 * @return <code>true</code> if the container is editable by entity merge;
	 *         <code>false</code> otherwise
	 */
	public boolean isEditable()
	{
		return this.edit;
	}

	/**
	 * Set the container's loc.
	 */
	public void setCloc(String cloc)
	{
		this.cloc = cloc;
	}

	/**
	 * Returns the container's location.
	 * @return the container's location
	 */
	public String getCloc()
	{
		return this.cloc;
	}

	/**
	 * Set the container's area.
	 */
	public void setArea(String area)
	{
		this.area = area;
	}

	/**
	 * Returns the container's area.
	 * @return the container's area
	 */
	public String getArea()
	{
		return this.area;
	}

	/**
	 * Returns the container's status.
	 * @return the container's status
	 */
	public char getStatus()
	{
		return this.cnts;
	}

	/**
	 * Returns a text description for the container's status.
	 * @return a text description for the container's status
	 */
	public String getStatusText()
	{
		switch (this.cnts)
		{
			case STATUS_ACK:
				return bundle.getString("statusAck"); //$NON-NLS-1$
			case STATUS_ACK_COMPLETE:
				return bundle.getString("statusAckComplete"); //$NON-NLS-1$
			case STATUS_ALTER_PENDING:
				return bundle.getString("statusAlterPending"); //$NON-NLS-1$
			case STATUS_COMPLETE:
				return bundle.getString("statusComplete"); //$NON-NLS-1$
			case STATUS_NOT_READY:
				return bundle.getString("statusNotReady"); //$NON-NLS-1$
			case STATUS_READY:
				return bundle.getString("statusReady"); //$NON-NLS-1$
			case STATUS_SHIP:
				return bundle.getString("statusShip"); //$NON-NLS-1$
			default:
				return bundle.getString("statusUnkown"); //$NON-NLS-1$
		}
	}

	/**
	 * Sets the container's status.
	 * @param status the container's new status
	 */
	public void setStatus(char status)
	{
		this.cnts = status;
		clearCachedStrings();
	}

	/**
	 * Returns the container's type.
	 * @return the container's type
	 */
	public char getType()
	{
		return this.cntt;
	}

	/**
	 * Sets the container's type.
	 * @param type the container's new type
	 */
	public void setType(char type)
	{
		this.cntt = type;
		clearCachedStrings();
	}
}
