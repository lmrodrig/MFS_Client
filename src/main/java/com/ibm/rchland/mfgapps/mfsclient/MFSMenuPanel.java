/* @ Copyright IBM Corporation 2007,2016. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2007-02-19      34242JR  R Prechel        -Initial version
 * 2010-05-13  ~01 47596MZ  Ray Perry		 -Screen repaint issue
 * 2016-02-18  ~02 1471226  Miguel Rivas     -MFSPanel(parent, source, screenName, layout) deprecated
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.Border;

import com.ibm.rchland.mfgapps.client.utils.layout.IGSCenterLayout;
import com.ibm.rchland.mfgapps.client.utils.layout.IGSGridLayout;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSButtonIterator;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSConstants;

/**
 * <code>MFSMenuPanel</code> is an abstract subclass of <code>MFSPanel</code>
 * for panels that serve primarily as a menu of <code>MFSMenuButton</code>s.
 * @author The MFS Client Development Team
 */
public abstract class MFSMenuPanel extends MFSPanel {
	private static final long serialVersionUID = 1L;
	/** The <code>JPanel</code> that contains the <code>MFSMenuButton</code>s. */
	protected JPanel buttonPanel;
	
	
	/** The <code>MFSButtonIterator</code> for this panel. */
	protected MFSButtonIterator fieldButtonIterator;
	
	
	/**
	 * Constructs a new <code>MFSMenuPanel</code>.
	 * @param frame the <code>MFSFrame</code> used to display this panel
	 * @param source the <code>MFSPanel</code> that caused this panel to be displayed
	 * @param screenName the screen name of this panel
	 * @param rows the number of rows in the button grid
	 * @param cols the number of columns in the button grid
	 */

	public MFSMenuPanel(MFSFrame frame, MFSPanel source, String screenName, int rows, int cols) {
		super(frame, source, screenName, "", new IGSCenterLayout()); //~02
			
		//Create the button panel
		this.buttonPanel = new JPanel(new IGSGridLayout(rows, cols, 5, 5));
		this.buttonPanel.setBackground(MFSConstants.PRIMARY_BACKGROUND_COLOR);
		this.buttonPanel.setOpaque(true);
		
		//Do NOT call createLayout here!
		//Due to the order of initialization in Java
		//createLayout MUST be called in the concrete subclass
		//after the subclass has initialized fieldButtonIterator
	}
	
	
	/** Adds this panel's <code>Component</code>s to the layout. */
	protected void createLayout() {
		Border inner = BorderFactory.createEmptyBorder(20, 40, 30, 40);
		Border outer = createTitledBorder();
		this.buttonPanel.setBorder(BorderFactory.createCompoundBorder(outer, inner));
		
		this.add(this.buttonPanel);
	}
	
	
	/**
	 * Determines which <code>MFSMenuButton</code>s are displayed in the
	 * menu. A <code>MFSMenuButton</code> is displayed if
	 * {@link MFSMenuButton#isActive()} returns <code>true</code>.
	 */
	protected void configureButtons() {
		this.buttonPanel.removeAll();
		
		this.fieldButtonIterator.reset();
		while (this.fieldButtonIterator.hasNext()) {
			MFSMenuButton next = this.fieldButtonIterator.nextMenuButton();
			if (next.isActive()) {
				this.buttonPanel.add(next);
				next.setEnabled(true);
			} else {
				next.setEnabled(false);
			}
		}
	}

	
	/** Assigns the focus to the first enabled <code>MFSMenuButton</code>. */
	public void assignFocus() {
		this.fieldButtonIterator.reset();
		while (this.fieldButtonIterator.hasNext()) {
			MFSMenuButton next = this.fieldButtonIterator.nextMenuButton();
			if (next.isEnabled()) {
				next.requestFocusInWindow();
				return;
			}
		}
	}
	
	
	/** Adds the listeners to this panel's <code>Component</code>s. */
	protected void addMyListeners() {
		this.addKeyListener(this);
		
		this.fieldButtonIterator.reset();
		while (this.fieldButtonIterator.hasNext()) {
			JButton next = this.fieldButtonIterator.nextMenuButton();
			next.addActionListener(this);
			next.addKeyListener(this);
		}
		
		// ~01 Added these 2 lines to fix repaint issue. Not 
		// really sure if it fixes root cause but seems to 
		// resolve symptoms.
		this.validate();
		this.repaint(0);
	}


	/** Removes the listeners from this panel's <code>Component</code>s. */
	protected void removeMyListeners() {
		this.removeKeyListener(this);
		
		this.fieldButtonIterator.reset();
		while (this.fieldButtonIterator.hasNext()) {
			JButton next = this.fieldButtonIterator.nextMenuButton();
			next.removeActionListener(this);
			next.removeKeyListener(this);
		}

		// ~01 Added these 2 lines to fix repaint issue. Not 
		// really sure if it fixes root cause but seems to 
		// resolve symptoms.
		this.validate();
		this.repaint(0);
	}
}