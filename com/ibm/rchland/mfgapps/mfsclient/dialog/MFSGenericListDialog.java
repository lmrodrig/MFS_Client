/* © Copyright IBM Corporation 2005, 2007. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2007-01-25      34242JR  R Prechel        -Java 5 version
 * 2008-06-23	~1 41674JM  Santiago D       -New methods added
 * 2008-07-23   ~2 38990JL  Santiago D       -sortListAlphaNumerically method added
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.dialog;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.ListModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;

import com.ibm.rchland.mfgapps.client.utils.messagebox.IGSMessageBox;
import com.ibm.rchland.mfgapps.mfsclient.MFSFrame;
import com.ibm.rchland.mfgapps.mfsclient.MFSListDialog;

/**
 * <code>MFSGenericListDialog</code> is a <code>MFSListDialog</code> that
 * can be loaded with a <code>String</code> of option data where each option
 * has the same length.
 * @author The MFS Client Development Team
 */
public class MFSGenericListDialog
	extends MFSListDialog
{
	private static final long serialVersionUID = 1L;
	/** The <code>JLabel</code> that displays the question. */
	private JLabel lblQuestion;
	
	//~1A
	/**
	 * Constructs a new <code>MFSGenericListDialog</code>.
	 * @param parent the <code>MFSFrame</code> that caused this
	 *        <code>MFSGenericListDialog</code> to be displayed
	 * @param title the <code>String</code> to display in the title bar
	 */
	public MFSGenericListDialog(MFSFrame parent, String title)
	{
		super(parent, title);
	}	
	
	/**
	 * Constructs a new <code>MFSGenericListDialog</code>.
	 * @param parent the <code>MFSFrame</code> that caused this
	 *        <code>MFSGenericListDialog</code> to be displayed
	 * @param title the <code>String</code> to display in the title bar
	 * @param question the <code>String</code> to display in the question label
	 */
	public MFSGenericListDialog(MFSFrame parent, String title, String question)
	{
		super(parent, title);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.lblQuestion = createLabel(question);
		createLayout();
		addMyListeners();
	}
	
	//~1A
	/**
	 * Constructs a new <code>MFSGenericListDialog</code>.
	 * @param parent the <code>MFSFrame</code> that caused this
	 *        <code>MFSGenericListDialog</code> to be displayed
	 * @param title the <code>String</code> to display in the title bar
	 * @param question the <code>String</code> to display in the question label
	 * @param proceedButtonText the <code>String</code> to display in the proceed button
	 * @param cancelButtonText the <code>String</code> to display in the cancel button
	 */
	public MFSGenericListDialog(MFSFrame parent, String title, String question,
			String proceedButtonText, String cancelButtonText)
	{
		super(parent, title);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.lblQuestion = createLabel(question);
		setButtons(proceedButtonText, cancelButtonText);
		createLayout();
		addMyListeners();		
	}
	
	/** Sets the size of this dialog to the small size. */
	public void setSizeSmall()
	{
		setSize(292, 269);
	}
	
	/** Sets the size of this dialog to the large size. */
	public void setSizeLarge()
	{
		setSize(520, 384);
	}
	
	/** Adds this dialog's <code>Component</code>s to the layout. */
	protected void createLayout()
	{
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
	
	/**
	 * Loads the list model.
	 * @param options the <code>String</code> of options
	 * @param answerSize the length of each option
	 */
	public void loadAnswerListModel(String options, int answerSize) 
	{
		try
		{
			final int len = options.length();
			int pos = 0;
			
			DefaultListModel listModel = new DefaultListModel();
			this.lstOptions.setModel(listModel);
			
			while(pos < len)
			{	
				listModel.addElement(options.substring(pos, pos+answerSize));		
				pos += answerSize;
			}	
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
	}
	
	/**
	 * Loads the list model without duplicates.
	 * @param options the <code>String</code> of options
	 * @param answerSize the length of each option
	 */
	public void loadAnswerListModelNoDups(String options, int answerSize) 
	{	
		try
		{
			final int len = options.length();
			int pos = 0;
			
			DefaultListModel listModel = new DefaultListModel();
			this.lstOptions.setModel(listModel);
			
			while(pos < len)
			{
				String option = options.substring(pos, pos+answerSize);
				if(listModel.contains(option) == false)
				{
					listModel.addElement(option);
				}
				pos+=answerSize;
			}
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
	}
	
	/**
	 * Sets the selected index to the first index of the specified value or zero
	 * if the specified value was not found.
	 * @param defSelection the default selection
	 */
	public void setDefaultSelection(String defSelection)
	{
		ListModel model = this.lstOptions.getModel();
		if (model instanceof DefaultListModel)
		{
			DefaultListModel dModel = (DefaultListModel) model;
			if (dModel.indexOf(defSelection) != -1)
			{
				this.lstOptions.setSelectedIndex(dModel.indexOf(defSelection));
				this.lstOptions.ensureIndexIsVisible(dModel.indexOf(defSelection));
			}
			else
			{
				this.lstOptions.setSelectedIndex(0);
			}
		}
		else
		{
			this.lstOptions.setSelectedIndex(0);
		}
	}
	
	//~1A
	/** Sets the name and mnemonic of both buttons in the dialog. */
	protected void setButtons(String proceedButtonText, String cancelButtonText)
	{
		if(proceedButtonText != null && cancelButtonText != null)
		{
			char mnemonic = proceedButtonText.charAt(0);
			this.pbProceed = createButton(proceedButtonText, mnemonic);
			
			int textLength = cancelButtonText.length();
			for(int i=0; i<textLength; i++)
			{
				if(cancelButtonText.charAt(i) != mnemonic)
				{
					this.pbCancel = createButton(cancelButtonText, cancelButtonText.charAt(i));
					break;
				}
			}			
		}
		// else default values are taken
	}

	//~1A
	/**
	 * creates a default list model.
	 */	
	public void initializeDefaultList()
	{
		DefaultListModel listModel = new DefaultListModel();
		this.lstOptions.setModel(listModel);
	}
	
	//~1A
	/**
	 * Loads a default list model.
	 * @param listModel the <code>DefaultListModel</code> that contains all items
	 */	
	public void setDefaultListModel(DefaultListModel listModel)
	{
		this.lstOptions.setModel(listModel);
	}
	
	//~1A
	/**
	 * Adds an Item to the default list model.
	 * @param item the <code>Object</code> to add to the list
	 */		
	public void addListItem(Object item)
	{
		DefaultListModel listModel = (DefaultListModel) this.lstOptions.getModel();
		listModel.addElement(item);
	}
	
	//~1A
	/**
	 * Adds an array of items to the default list model.
	 * @param items the <code>Object Array</code> to add to the list
	 */		
	public void addListItems(Object[] items)
	{
		DefaultListModel listModel = (DefaultListModel) this.lstOptions.getModel();
		int totalItems = items.length;
		
		for(int item = 0; item < totalItems; item++)
		{
			listModel.addElement(items[item]);
		}
	}
	
	//~2A
	/** 
	 * Sorts alphanumerically the default list model.
	 *
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void sortListAlphaNumerically()
	{
		DefaultListModel listModel = (DefaultListModel) this.lstOptions.getModel();

		int listSize = listModel.size();
			
		ArrayList tempList = new ArrayList();
		
		for(int pos=0; pos<listSize; pos++)
		{
			tempList.add(listModel.getElementAt(pos));
		}
		
		Collections.sort(tempList);
		
		for(int pos=0; pos<listSize; pos++)
		{
			listModel.setElementAt(tempList.get(pos), pos);
		}
	}	
	
	/**
	 * Invoked the first time a window is made visible. Requests the focus for
	 * {@link #lstOptions}, but does not set the selected index.
	 * @param we the <code>WindowEvent</code>
	 */
	public void windowOpened(WindowEvent we)
	{
		if (we.getSource() == this)
		{
			this.lstOptions.requestFocusInWindow();
		}
	}
}
