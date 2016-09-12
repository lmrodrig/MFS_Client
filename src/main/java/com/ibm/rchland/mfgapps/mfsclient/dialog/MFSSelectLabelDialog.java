/* @ Copyright IBM Corporation 2008,2010. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2008-07-29	   38990JL  Santiago D       -Initial Version
 * 2010-03-07   ~1 42558JL  Santiago SC      -Layout remake
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.dialog;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;

import com.ibm.rchland.mfgapps.mfsclient.MFSFrame;

/**
 * <code>MFSSelectLabelDialog</code> is a <code>MFSGenericListDialog</code> that
 * can be loaded with any <code>Object</code> of option data and has a comboBox
 * @author The MFS Client Development Team
 */
public class MFSSelectLabelDialog 
	extends MFSGenericListDialog
	{
	private static final long serialVersionUID = 1L;
	/** The <code>JLabel</code> that displays the question. */
	private JLabel lblQuestion;

	/** The <code>JLabel</code> that displays the PLOM. */
	private JLabel lblPlom;	
	
	/** The <code>JComboBox</code> that displays the PLOM's. */	
	private JComboBox cbPlom;
	
	/**
	 * Constructs a new <code>MFSSelectPartDialog</code>.
	 * @param parent the <code>MFSFrame</code> that caused this
	 *        <code>MFSGenericListDialog</code> to be displayed
	 * @param title the <code>String</code> to display in the title bar
	 * @param question the <code>String</code> to display in the question label
	 * @param comboLabel the <code>String</code> to display in combo box
	 */
	public MFSSelectLabelDialog(MFSFrame parent, String title, String question, String comboLabel)
	{
		super(parent, title);

		this.lblQuestion = createLabel(question);
		this.lblPlom = createLabel(comboLabel);		
		
		createLayout();
		addMyListeners();		
	}	
	
	//~1C
	/** Adds this dialog's <code>Component</code>s to the layout. */
	protected void createLayout()
	{
		this.cbPlom = new JComboBox();
		this.cbPlom.setEditable(false);
		
		this.lblQuestion.setHorizontalAlignment(SwingConstants.CENTER);
		this.spOptions.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		JPanel northPanel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.RELATIVE,
				new Insets(0, 0, 0, 0), 0, 0);
		
		gbc.weighty = 1.0;
		
		gbc.gridy++;
		gbc.insets = new Insets(10, 5, 0, 5);
		northPanel.add(this.lblQuestion, gbc);
		
		gbc.gridy++;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(10, 10, 10, 0);
		northPanel.add(this.lblPlom, gbc);
		
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(10, 65, 10, 10);
		northPanel.add(this.cbPlom, gbc);		
		
		JPanel centerPanel = new JPanel(new GridBagLayout());
		gbc = new GridBagConstraints(0, 0, 2, 1, 1, 1,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 10, 0, 10), 0, 0);		
		centerPanel.add(this.spOptions, gbc);

		JPanel buttonPanel = new JPanel(new FlowLayout());
		buttonPanel.add(this.pbProceed);
		buttonPanel.add(this.pbCancel);
		
		JPanel contentPane = new JPanel(new BorderLayout());
		contentPane.add(northPanel, BorderLayout.NORTH);
		contentPane.add(centerPanel, BorderLayout.CENTER);
		contentPane.add(buttonPanel, BorderLayout.SOUTH);

		//~1A dialog settings
		setButtonDimensions(this.pbProceed, this.pbCancel);		
		setContentPane(contentPane);

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}	
	
	/**
	 * Adds an Item to the ComboBox.
	 * @param item the <code>Object</code> to add to the list
	 */		
	public void addComboItem(Object item)
	{
		this.cbPlom.addItem(item);
	}	
	
	/**
	 * Get the ComboBox selected item
	 * @return <code>String</code> the selected item
	 */			
	public String getSelectedComboOption()
	{
		return this.cbPlom.getSelectedItem().toString();
	}	
}
