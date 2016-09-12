/* @ Copyright IBM Corporation 2007, 2016. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2007-01-15      34242JR  R Prechel        -Initial version
 * 2007-07-24  ~1  38283KM  D Pietrasik      -Require a value to be selected
 * 2007-12-15  ~2  37616JL  D Pietrasik      -Allow multi selection option
 * 2008-07-31  ~3  38990JL  Santiago D       -Added fieldCancelSelected, get and set methods
 * 2016-02-18  ~4  1471226  Miguel Rivas     -getSelectedValues deprecated
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;

import com.ibm.rchland.mfgapps.mfsclient.utils.MFSScroller;
import com.ibm.rchland.mfgapps.client.utils.messagebox.IGSMessageBox; /* ~1A */

/**
 * <code>MFSListDialog</code> is a subclass of <code>MFSDialog</code> that
 * displays a <code>JList</code> of <code>String</code> options and two
 * <code>JButton</code>s. One button allows the user to proceed with the
 * selection of an option; the other button allows the user to cancel and not
 * select an option. By default, the list has a single-selection selection-mode.
 * @author The MFS Client Development Team
 */
public abstract class MFSListDialog
	extends MFSDialog
{
	private static final long serialVersionUID = 1L;
	/** The <code>JList</code> that displays the options. */
	protected JList lstOptions = createList();

	/** The <code>JScrollPane</code> that contains the list of options. */
	protected JScrollPane spOptions = new JScrollPane(this.lstOptions,
			ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
			ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

	/** The <code>JButton</code> the user selects to proceed with the selection of an option. */
	protected JButton pbProceed = createButton("Enter", 'E'); 

	/** The <code>JButton</code> the user selects to cancel and not select an option. */
	protected JButton pbCancel = createButton("Cancel", 'n'); 

	/** Set <code>true</code> when the proceed button is selected. */
	protected boolean fieldProceedSelected = false;
	
	//~3A
	/** Set <code>true</code> when the cancel button is selected. */
	protected boolean fieldCancelSelected = false;		

	/** Stores the list option the user selected. */
	protected String fieldSelectedOption = ""; 

	/** Stores the list options if multiples are selected */
	protected String[] fieldSelectedOptions = new String[0];  /* ~2A */
	
	/**
	 * Constructs a new <code>MFSListDialog</code>.
	 * @param parent the <code>MFSFrame</code> that caused this
	 *        <code>MFSListDialog</code> to be displayed
	 * @param title the <code>String</code> to display in the title bar
	 */
	public MFSListDialog(MFSFrame parent, String title)
	{
		super(parent, title);
	}

	
	/** Adds this dialog's <code>Component</code>s to the layout. */
	protected void createLayout()
	{
		JPanel contentPane = new JPanel(new GridBagLayout());
		contentPane.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 2, 1, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0);

		contentPane.add(this.spOptions, gbc);

		gbc.gridy++;
		gbc.gridwidth = 1;
		gbc.weighty = 0.0;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(20, 0, 0, 0);
		contentPane.add(this.pbProceed, gbc);

		gbc.gridx = 1;
		contentPane.add(this.pbCancel, gbc);

		setContentPane(contentPane);
		setButtonDimensions(this.pbProceed, this.pbCancel);
	}

	
	/** Adds the listeners to this dialog's <code>Component</code>s. */
	protected void addMyListeners()
	{
		this.pbProceed.addActionListener(this);
		this.pbCancel.addActionListener(this);

		this.pbProceed.addKeyListener(this);
		this.pbCancel.addKeyListener(this);
		this.lstOptions.addKeyListener(this);
		this.spOptions.addKeyListener(this);
	}

	/**
	 * Set up the list to handle multiple selections
	 * @param multiSelect handle multiples?
	 */
	public void setMultipleSelection(boolean multiSelect)
	{
		if (multiSelect)
		{
			this.lstOptions.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		}
		else
		{
			this.lstOptions.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		}
	}
	
	
	/**
	 * Returns <code>true</code> iff the proceed button was selected.
	 * @return <code>true</code> iff the proceed button was selected
	 */
	public boolean getProceedSelected()
	{
		return this.fieldProceedSelected;
	}

	//~3A
	/**
	 * Returns <code>true</code> iff the cancel button was selected.
	 * @return <code>true</code> iff the cancel button was selected
	 */	
	public boolean getCancelSelected()
	{
		return this.fieldCancelSelected;
	}
	
	//~3A
	/**
	 * Sets the fieldProceedSelected Flag
	 * @param selected: true or false
	 */
	public void setProceedSelected(boolean selected)
	{
		this.fieldProceedSelected = selected;
	}	
	
	/**
	 * Returns the list option the user selected.
	 * @return the list option the user selected
	 */
	public String getSelectedListOption()
	{
		return this.fieldSelectedOption;
	}

	
	/**
	 * Returns the list options the user selected.
	 * @return the list options the user selected
	 */
	public String[] getSelectedListOptions()	/* ~2A */
	{
		return this.fieldSelectedOptions;
	}

	
	/**
	 * Invoked when an action occurs.
	 * @param ae the <code>ActionEvent</code>
	 */
	public void actionPerformed(ActionEvent ae)
	{
		try
		{
			final Object source = ae.getSource();
			if (source == this.pbProceed)
			{  /* Make sure a value is selected ~1C */
			    String selectedOption = (String)this.lstOptions.getSelectedValue();
			    if (selectedOption == null)
			    {
			        String title = "Entry Error"; 
			        String emsg = "You must select a value."; 
			        IGSMessageBox.showOkMB(getParentFrame(), title, emsg, null);
			    }
			    else  /* ~1C end */
			    { 
			        this.fieldSelectedOption = selectedOption;
			        this.fieldProceedSelected = true;
			        if (this.lstOptions.getSelectionMode() == ListSelectionModel.MULTIPLE_INTERVAL_SELECTION)	/* ~2A  get multi selections, or set if just one allowed */
			        {
				        /* ~4C
			        	Object[] selectedOptions = this.lstOptions.getSelectedValues();
				        this.fieldSelectedOptions = new String[selectedOptions.length];
				        for (int i = 0; i < selectedOptions.length; i++)
				        {
				            this.fieldSelectedOptions[i] = (String)selectedOptions[i];
				        }
				        */
			        	List selectedOptions = this.lstOptions.getSelectedValuesList();
				        this.fieldSelectedOptions = new String[selectedOptions.size()];
						for (int i = 0; i < selectedOptions.size(); i++)
						{
							this.fieldSelectedOptions[i] = (String)selectedOptions.get(i);
						}
			        }
			        else
			        {
			            this.fieldSelectedOptions = new String[1];
			            this.fieldSelectedOptions[0] = this.fieldSelectedOption;
			        } /* ~2A end */
			        this.dispose();
			    }
			}
			else if (source == this.pbCancel)
			{
				this.fieldCancelSelected = true; // ~3A
				this.dispose();
			}
		}
		catch (Throwable throwable)
		{
			throwable.printStackTrace();
		}
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
			else
			{
				this.pbProceed.requestFocusInWindow();
				this.pbProceed.doClick();
			}
		}
		else if (keyCode == KeyEvent.VK_ESCAPE)
		{
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
	 * Invoked the first time a window is made visible.
	 * <p>
	 * Sets the selected index of {@link #lstOptions} to 0 and requests the
	 * focus for {@link #lstOptions}.
	 * @param we the <code>WindowEvent</code>
	 */
	public void windowOpened(WindowEvent we)
	{
		try
		{
			if (we.getSource() == this)
			{
				this.lstOptions.setSelectedIndex(0);
				this.lstOptions.requestFocusInWindow();
			}
		}
		catch (Throwable throwable)
		{
			throwable.printStackTrace();
		}
	}
}
