/* @ Copyright IBM Corporation 2009. All rights reserved.
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

/**
 * <code>MFSBuildAheadCnfgUO</code> is the <code>MFSBuildAheadUO</code>
 * for a Build Ahead configuration (ITEM)
 * @author The MFS Client Development Team
 */
public class MFSBuildAheadCnfgUO
	extends MFSBuildAheadUO
{
	/** The configuration item. */
	String ITEM = "";

	/** The warehouse. */
	String WHS = "";

	/** The warehouse quantity. */
	int WQTY = 0;

	/** The location. */
	String LOC = "";

	/** Location quantity. */
    int LQTY = 0;

    /** Exists. */
    char EXISTS = '\0';
    

    /** These variables are for doing calcs on the screen **/
    
    /** Order quantity. */
    int orderQty = 0;

    /** Number of unreleased orders. */
    int unreleasedOrderQty = 0;

    /** Allocated quantity. */
    int allocatedQty = 0;
    
    
    boolean isReleased = false;

	/**
	 * Constructs a new <code>MFSBuildAheadCnfgUO</code>.
	 * @param item the configuration item
	 */
	public MFSBuildAheadCnfgUO(String item)
	{
		super();
		this.ITEM = item;
	}

	/** {@inheritDoc} */
	protected String createString(String type)
	{
		StringBuffer buffer = new StringBuffer(100);
        String separator = "";

        if (type.equals("tree"))
        {
            // 7 spaces
            separator = "\u0020\u0020\u0020\u0020\u0020\u0020\u0020";
            
            buffer.append(bundle.getString(type + "ITEM")); //$NON-NLS-1$
            buffer.append(bufferItemValue(this.ITEM));

            // 13 spaces
            buffer.append("\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020");
            buffer.append(bufferItemValue(this.ITEM));
            
            // 3 spaces
            buffer.append("\u0020\u0020\u0020");
            buffer.append(bufferQuantityValue(String.valueOf(this.orderQty)));

            buffer.append(separator);
            buffer.append(bufferQuantityValue(String.valueOf(WQTY-allocatedQty)));
            
            buffer.append(separator);
            buffer.append(bufferQuantityValue(String.valueOf(this.WQTY)));

            buffer.append(separator);
            buffer.append(bufferLocValue(this.LOC));

            buffer.append(separator);
            buffer.append(bufferQuantityValue(String.valueOf(this.LQTY)));
        }
        else
        {
            separator = bundle.getString(type + "Separator"); //$NON-NLS-1$
            
            buffer.append(bundle.getString(type + "ITEM")); //$NON-NLS-1$
            buffer.append(this.ITEM);
    
            buffer.append(separator);
            buffer.append(bundle.getString(type + "WHS")); //$NON-NLS-1$
            buffer.append(this.WHS);
            
            buffer.append(separator);
            buffer.append(bundle.getString(type + "orderQty")); //$NON-NLS-1$
            buffer.append(this.orderQty);
    
            buffer.append(separator);
            buffer.append(bundle.getString(type + "WQTY")); //$NON-NLS-1$
            buffer.append(this.WQTY);
            
            buffer.append(separator);
            buffer.append(bundle.getString(type + "availableQty")); //$NON-NLS-1$
            buffer.append(WQTY-allocatedQty);
    
            buffer.append(separator);
            buffer.append(bundle.getString(type + "LOC")); //$NON-NLS-1$
            buffer.append(this.LOC);
            
            buffer.append(separator);
            buffer.append(bundle.getString(type + "LQTY")); //$NON-NLS-1$
            buffer.append(this.LQTY);
        }

		return buffer.toString();
	}
    
    private String bufferItemValue(String input)
    {
        String blank7 = "     ";
        
        String output = input+blank7;
        output = output.substring(5, 12);
        
        return output;
    }
    
    private String bufferLocValue(String input)
    {
        String blank7 = "       ";
        
        String output = input+blank7;
        output = output.substring(0, 7);
        
        return output;
    }
    
    private String bufferQuantityValue(String input)
    {
        String blank5 = "     ";
        
        String output = input+blank5;
        output = output.substring(0, 5);
        
        return output;
    }
    
    public Color getBackground(boolean selected)
    {
        Color color = Color.BLACK;
        if (isReleased() && haveCoverage() && !selected)
        {
            color = Color.GRAY;
        }
        else if (isReleased() && !haveCoverage() && !selected)
        {
            color = Color.GRAY;
        }
        else if (!isReleased() && haveCoverage() && !selected)
        {
            color = Color.BLUE;
        }
        else if (!isReleased() && !haveCoverage() && !selected)
        {
            color = Color.RED;
        }
        
        return color;
    }
    
    public Color getForeground(boolean selected)
    {
        Color color = Color.WHITE;
//        if (haveCoverage() && isReleased() && !selected)
//        {
//            color = Color.BLUE;
//        }
//        else if (!haveCoverage() && !isReleased() && !selected)
//        {
//            color = Color.RED;
//        }
        
        return color;
    }

    /**
     * Returns the item.
     * @return the item.
     */
    public String getItem()
    {
        return this.ITEM;
    }

    public String getWHS()
    {
        return this.WHS;
    }
    
    public int getWQTY()
    {
        return this.WQTY;
    }

    public String getLOC()
    {
        return this.LOC;
    }
    
    public int getLQTY()
    {
        return this.LQTY;
    }

    public char getEXISTS()
    {
        return this.EXISTS;
    }

    public int getOrderQty()
    {
        return this.orderQty;
    }

    /**
     * Returns the unreleased order quantity.
     * @return the unreleased order quantity.
     */
    public int getUnreleasedOrderQuantity()
    {
        return this.unreleasedOrderQty;
    }

    /**
     * Returns the allocated quantity.
     * @return the allocated quantity.
     */
    public int getAllocatedQuantity()
    {
        return this.allocatedQty;
    }

    public boolean isReleased()
    {
        return this.isReleased;
    }


    public void setITEM(String ITEM)
    {
        this.ITEM = ITEM;
        clearCachedStrings();
    }

    public void setWHS(String WHS)
    {
        this.WHS = WHS;
        clearCachedStrings();
    }
    
    public void setWQTY(int WQTY)
    {
        this.WQTY = WQTY;
        clearCachedStrings();
    }

    public void setLOC(String LOC)
    {
        this.LOC = LOC;
        clearCachedStrings();
    }
    
    public void setLQTY(int LQTY)
    {
        this.LQTY = LQTY;
        clearCachedStrings();
    }

    public void setEXISTS(char EXISTS)
    {
        this.EXISTS = EXISTS;
        clearCachedStrings();
    }

    /**
     * Set the order quantity.
     * @param orderQty the order quantity.
     */
    public void setOrderQty(int orderQty)
    {
        this.orderQty = orderQty;
        clearCachedStrings();
    }

    /**
     * Set the unreleased order quantity.
     * @param unreleasedOrderQty the unreleased order quantity.
     */
    public void setUnreleasedOrderQty(int unreleasedOrderQty)
    {
        this.unreleasedOrderQty = unreleasedOrderQty;
        clearCachedStrings();
    }

    /**
     * Set the allocated quantity.
     * @param alcqty the allocated quantity.
     */
    public void setAllocatedQuantity(int allocatedQty)
    {
        this.allocatedQty = allocatedQty;
        clearCachedStrings();
    }

    public void setIsReleased(boolean isReleased)
    {
        this.isReleased = isReleased;
    }

    /**
     * true if we have coverage
     */
    public boolean haveCoverage()
    {
        boolean coverage = false;
        if (unreleasedOrderQty==0)
        {
            // if all the orders have been released
            coverage = true;
        }
        else if (WQTY-allocatedQty<orderQty)
        {
            // if there are not enough in stock to cover 
            // the order quantity
            coverage = false;
        }
        else 
        {
            // if we are good to release
            coverage = true;
        }
        
        return coverage;
    }
}
