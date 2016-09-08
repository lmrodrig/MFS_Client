/* © Copyright IBM Corporation 2008. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2008-06-26	   38990JL  Santiago D       -Initial Version
 * 2008-10-06   ~1 42938JM  Santiago D       -Set fieldCancelSelected to true when ESC key pressed
 *                 42942SM  Santiago D       -Layout changes
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.dialog;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;

import com.ibm.rchland.mfgapps.client.utils.messagebox.IGSMessageBox;
import com.ibm.rchland.mfgapps.mfsclient.MFSFrame;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSConstants;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSScroller;

/**
 * <code>MFSSelectPartDialog</code> is a <code>MFSGenericListDialog</code> that
 * can be loaded with any <code>Object</code> of option data and has an input
 * textField.
 * @author The MFS Client Development Team
 */
public class MFSSelectPartDialog 
	extends MFSGenericListDialog
{
	private static final long serialVersionUID = 1L;
	/** The <code>JLabel</code> that displays the question. */
	private JLabel lblQuestion;

	/** The <code>JLabel</code> that displays the comment. */
	private JLabel lblQuantity;
	
	/** The <code>JLabel</code> that displays the comment. */
	private JLabel lblSearch;	
	
	/** The <code>JTextField</code> that displays the input textField. */	
	private JTextField tfQuantity;
	
	/** The <code>JTextField</code> that displays the input textField. */	
	private JTextField tfSearch;		
	
	/**
	 * Constructs a new <code>MFSSelectPartDialog</code>.
	 * @param parent the <code>MFSFrame</code> that caused this
	 *        <code>MFSGenericListDialog</code> to be displayed
	 * @param title the <code>String</code> to display in the title bar
	 * @param question the <code>String</code> to display in the question label
	 * @param proceedButtonText the <code>String</code> to display in the proceed button
	 * @param cancelButtonText the <code>String</code> to display in the cancel button
	 */
	public MFSSelectPartDialog(MFSFrame parent, String title, String question,
			String proceedButtonText, String cancelButtonText)
	{
		super(parent, title);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.lblQuestion = createLabel(question);	
		setButtons(proceedButtonText, cancelButtonText);
		createLayout();
		addMyListeners();		
	}	
	
	/** Adds this dialog's <code>Component</code>s to the layout. */
	protected void createLayout()
	{
		this.lblSearch = createLabel("Search : ");
		this.lblQuantity = createLabel("Quantity : ");
		this.tfQuantity = createTextField(MFSConstants.SMALL_TF_COLS, 2, lblQuantity);
		this.tfSearch = createTextField(MFSConstants.SMALL_TF_COLS, 12, lblSearch);			
		this.lblQuestion.setHorizontalAlignment(SwingConstants.CENTER);
		this.spOptions.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		JPanel contentPane = new JPanel(new GridBagLayout());
		contentPane.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 0, 0);
		
		contentPane.add(this.lblQuestion, gbc);
		
		gbc.gridy++;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(20, 0, 0, 0);
		contentPane.add(this.lblSearch, gbc);
		
		gbc.insets = new Insets(20, 63, 0, 250);
		contentPane.add(this.tfSearch, gbc);	
		
		gbc.insets = new Insets(20, 225, 0, 0);
		contentPane.add(this.lblQuantity, gbc);
		
		gbc.insets = new Insets(20, 300, 0, 0);
		contentPane.add(this.tfQuantity, gbc);		
		
		gbc.gridy++;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(20, 0, 0, 0);
		contentPane.add(this.spOptions, gbc);
		
		gbc.gridy++;
		gbc.gridwidth = 1;
		gbc.weighty = 0.0;
		gbc.fill = GridBagConstraints.NONE;
		contentPane.add(this.pbProceed, gbc);
		
		gbc.gridx = 1;
		contentPane.add(this.pbCancel, gbc);
		
		setContentPane(contentPane);
		setButtonDimensions(this.pbProceed, this.pbCancel);
	}
	
	/* Adds my Listeners */
	public void addMyListeners()
	{
		super.addMyListeners();
		
		this.tfSearch.addKeyListener(this);
	}
	
	/**
	 * Invoked when a key is pressed.
	 * @param ke the <code>KeyEvent</code>
	 */
	public void keyPressed(KeyEvent ke)
	{
		final int keyCode = ke.getKeyCode();
		if (keyCode == KeyEvent.VK_ENTER)
		{
			final Object source = ke.getSource();
			if (source == this.pbCancel)
			{
				this.pbCancel.requestFocusInWindow();
				this.pbCancel.doClick();
			}
			else if(source == this.tfSearch)
			{
				searchPartNumber();
			}
			else
			{
				this.pbProceed.requestFocusInWindow();
				this.pbProceed.doClick();
			}
		}
		else if (keyCode == KeyEvent.VK_ESCAPE)
		{
			this.fieldCancelSelected = true; //~1A
			this.dispose();
		}
		else if (keyCode == KeyEvent.VK_PAGE_DOWN)
		{
			MFSScroller.scrollDown(this.spOptions, this.lstOptions);
			ke.consume();
		}
		else if (keyCode == KeyEvent.VK_PAGE_UP)
		{
			MFSScroller.scrollUp(this.spOptions, this.lstOptions);
			ke.consume();
		}
	}
	
	/**
	 * Validates container before sending to ACK_CNTR tx
	 * @return <code>true</code> if partNumber was found in the list
	 */		
	private void searchPartNumber()
	{
		boolean partFound = false;
		
		String partNumber = this.tfSearch.getText().trim();
		
		DefaultListModel listModel = (DefaultListModel) this.lstOptions.getModel();
		
		int listSize = listModel.size();
		
		for(int pos = 0; pos < listSize; pos++)
		{
			String listItem = (String) listModel.getElementAt(pos);
			
			try
			{
				if(partNumber.compareToIgnoreCase(listItem) == 0 ||
						partNumber.compareToIgnoreCase(listItem.substring(0, partNumber.length())) == 0)					
				{
					this.lstOptions.setSelectedIndex(pos);
					this.lstOptions.ensureIndexIsVisible(pos);
					partFound = true;
					break;
				}
			}
			catch(Exception e)// Can't make a substring from the listItem
			{
				continue;
			}
		}
		
		if(!partFound)
		{
	        String title = "Mistmatch part number"; 
	        String emsg = "Could not find the part number."; 
	        IGSMessageBox.showOkMB(getParentFrame(), title, emsg, null);
		} 
	}	
	
	/**
	 * Set all fields to ther default values.
	 */	
	public void initializeFields()
	{
		this.fieldProceedSelected = false;		
		this.fieldCancelSelected = false;
		this.tfQuantity.setText("1");

	}	

	/**
	 * Validates that tfQuantity has a numeric value and is between 1 and maxValue.
	 * @param minValue the <code>int</code> that specifies the min value for the input.	 
	 * @param maxValue the <code>int</code> that specifies the max value for the input.
	 * @return <code>true</code> if is a numeric value and inside the range
	 */	
	public boolean isValidNumInput(int minValue, int maxValue)
	{
		boolean result = false;
		int qty = 0;
		
		try
		{
			qty = Integer.parseInt(this.tfQuantity.getText());
			
			if(qty >= minValue && qty <= maxValue)
			{
				result =  true;
			}
			else
			{
				result =  false;
			}
		}
		catch(Exception e)
		{
			result =  false;
		}
		
		return result;
	}
	
	/**
	 * Get the Input text
	 * @return <code>String</code> the Input text
	 */		
	public String getQuantity()
	{
		return tfQuantity.getText();
	}

	/**
	 * Invoked the first time a window is made visible. Requests the focus for
	 * {@link #tfSearch}.
	 * @param we the <code>WindowEvent</code>
	 */
	public void windowOpened(WindowEvent we)
	{
		if (we.getSource() == this)
		{
			this.tfSearch.requestFocusInWindow();
		}
	}	
	
}
