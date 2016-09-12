/* @ Copyright IBM Corporation 2007, 2010. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2007-12-25      37616JL  D Pietrasik      -initial version
 * 2010-03-08	   47595MZ	Ray Perry        -Shenzhen efficiencies
 *                          Toribio H.
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.dialog;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;

import com.ibm.rchland.mfgapps.client.utils.messagebox.IGSMessageBox;
import com.ibm.rchland.mfgapps.mfsclient.MFSFrame;
import com.ibm.rchland.mfgapps.mfsclient.MFSListDialog;
import com.ibm.rchland.mfgapps.mfsclient.struct.MFSLocation;

/**
 * <code>MFSEntityLocationDialog</code> is the <code>MFSListDialog</code>
 * used to select the location for merged entities when suspended.
 * @author The MFS Client Development Team
 */
@SuppressWarnings("serial")
public class MFSEntityLocationDialog
	extends MFSListDialog
{
	/** cancel button selected */
	public static final int CANCEL = 0;

	/** proceed button selected */
	public static final int PROCEED = 1;

	/** skip button selected */
	public static final int SKIP = 2;

	/** loc all button selected */
	public static final int LOC_ALL = 3;

	/** clear all button selected */
	public static final int CLEAR_ALL = 4;

	/** label holding container number */
	private JLabel lblCntr = null;

	/** input */
	public JTextField tfInput = createTextField(4, 4);

	/** selected area value */
	private String fieldSelectedArea = null;

	/** selected location value */
	private String fieldSelectedLoc = null;

	/** description of selected area */
	private String fieldSelectedDesc = null;

	/**
	 * The <code>JButton</code> the user selects to skip this container for
	 * locing.
	 */
	private JButton pbSkip = createButton("Skip", 'S');
	
	/**
	 * The <code>JButton</code> the user selects to loc all containers
	 * to the same loc.
	 */
	private JButton pbLocAll = createButton("Loc All (F4)", 'A');
	
	/**
	 * The <code>JButton</code> the user selects to clear all locs.
	 */
	private JButton pbClearAll = createButton("Clear All", 'C');

	/** indicates which button was pressed to close the dialog */
	private int fieldSelectedButton = CANCEL;

	/**
	 * Constructs a new <code>MFSEntityLocationDialog</code>.
	 * @param parent the <code>MFSFrame</code> that caused this dialog to be
	 *        displayed
	 * @param cntr container number that we get the location for
	 */
	public MFSEntityLocationDialog(MFSFrame parent)
	{
		super(parent, "Suspend Pallet");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		createLayout();
		addMyListeners();
	}

	/**
	 * Lay out the dialog pieces
	 * @see com.ibm.rchland.mfgapps.mfsclient.MFSListDialog#createLayout()
	 */
	protected void createLayout()
	{
		JLabel lblQuestion = createLabel("Select Location for Container: ");
		lblQuestion.setHorizontalAlignment(SwingConstants.CENTER);
		this.lblCntr = createValueLabel(lblQuestion);
		
		/* easiest way to get headers to line up with rows */
		MFSLocation header = new MFSLocation("AREA", "LOCATION",
				"DESCRIPTION                             ",  "SHPN   ");
		JLabel lblListHead = createLabel(header.toString(), this.lstOptions.getFont());
		this.spOptions.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		pbProceed.setText("Enter (F2)");
		
		JPanel buttonPanel = new JPanel(null);
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
		buttonPanel.add(this.pbProceed);
		buttonPanel.add(Box.createHorizontalGlue());
		buttonPanel.add(Box.createHorizontalGlue());
		buttonPanel.add(this.pbLocAll);
		buttonPanel.add(this.pbSkip);
		buttonPanel.add(Box.createHorizontalGlue());
		buttonPanel.add(this.pbClearAll);
		buttonPanel.add(Box.createHorizontalGlue());
		buttonPanel.add(this.pbCancel);

		JPanel contentPane = new JPanel(new GridBagLayout());
		contentPane.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 0, 0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0,
						0, 0, 0), 0, 0);

		contentPane.add(lblQuestion, gbc);

		gbc.gridx++;
		contentPane.add(this.lblCntr, gbc);
		
		gbc.gridx = 0;
		gbc.gridy++;
		contentPane.add(tfInput, gbc);

		gbc.gridx = 0;
		gbc.gridy += 2;
		gbc.gridwidth = 2;
		gbc.insets = new Insets(20, 0, 0, 0);
		contentPane.add(lblListHead, gbc);

		gbc.gridy++;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(0, 0, 0, 0);
		contentPane.add(this.spOptions, gbc);

		gbc.gridy++;
		gbc.gridwidth = 3;
		gbc.weighty = 0.0;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(20, 10, 0, 10);
		contentPane.add(buttonPanel, gbc);

		setContentPane(contentPane);
		setButtonDimensions(this.pbProceed, this.pbSkip, this.pbLocAll, this.pbClearAll, this.pbCancel);
		this.pack();
	}

	/**
	 * Adds the listeners to this dialog's <code>Component</code>s.
	 */
	protected void addMyListeners()
	{
		super.addMyListeners();
		this.tfInput.addActionListener(this);
		this.pbSkip.addActionListener(this);
		this.pbLocAll.addActionListener(this);
		this.pbClearAll.addActionListener(this);
		this.tfInput.addKeyListener(this);
		this.pbSkip.addKeyListener(this);
		this.pbLocAll.addKeyListener(this);
		this.pbClearAll.addKeyListener(this);
	}

	/**
	 * Loads the Location list using the specified data.
	 * @param data xml data used to load the location list
	 */
	public void loadLocationModel(Vector <MFSLocation>locList, String defaultArea, String defaultLoc)
	{
		try
		{
			int index = 0;
			
			DefaultListModel listModel = new DefaultListModel();
			DefaultListModel listModelBlankShpn = new DefaultListModel();
			this.lstOptions.setModel(listModel);
			int[] selectedIndex = new int[1];
			boolean selectedIndexSet = false;
			
			selectedIndex[0] = 0;
			Enumeration<MFSLocation> eLocList = locList.elements();
			/* Put the None Empty containers first */
			while (eLocList.hasMoreElements()) 
			{ 
				MFSLocation thisLoc = eLocList.nextElement();
				if (!thisLoc.getShpn().trim().equals(""))
				{
					listModel.addElement(thisLoc);
					if (!selectedIndexSet && !defaultArea.trim().equals("") && !defaultLoc.trim().equals(""))
					{
						if (defaultArea.trim().equals(thisLoc.getArea()) && defaultLoc.trim().equals(thisLoc.getLocation()))
						{
							selectedIndexSet = true;
							selectedIndex[0] = index;
						}
					}
					index++;
				}
				else
				{
					listModelBlankShpn.addElement(thisLoc);
				}
			}			
			for (int x=0; x<listModelBlankShpn.size(); x++)
			{
				MFSLocation loc = (MFSLocation)listModelBlankShpn.elementAt(x);
				listModel.addElement(loc);
			}			
			this.lstOptions.setSelectedIndices(selectedIndex);
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), "Error loading locations", null, e);
		}
		finally
		{
			pack();			
		}
	}

	/**
	 * Returns the value of the fieldSelectedButton property.
	 * @return the value of the fieldSelectedButton property
	 */
	public int getSelectedButton()
	{
		return this.fieldSelectedButton;
	}

	/**
	 * @return Returns the fieldSelectedArea.
	 */
	public String getSelectedArea()
	{
		return this.fieldSelectedArea;
	}

	/**
	 * @return Returns the fieldSelectedDesc.
	 */
	public String getSelectedDesc()
	{
		return this.fieldSelectedDesc;
	}

	/**
	 * @return Returns the fieldSelectedLoc.
	 */
	public String getSelectedLoc()
	{
		return this.fieldSelectedLoc;
	}

	/**
	 * Sets the container we're looking to loc
	 * @param cntr container number
	 */
	public void setCntr(String cntr)
	{
		this.lblCntr.setText(cntr);
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
			if (source == this.pbProceed || source == this.pbLocAll)
			{ /* Make sure a value is selected */
				MFSLocation selectedOption = (MFSLocation) this.lstOptions
						.getSelectedValue();
				if (selectedOption == null)
				{
					String title = "Entry Error";
					String emsg = "You must select a value to proceed.";
					IGSMessageBox.showOkMB(getParentFrame(), title, emsg, null);
				}
				else
				{
					if (source == this.pbProceed)
					{
						this.fieldSelectedButton = PROCEED;
					}
					else if (source == this.pbLocAll)
					{
						this.fieldSelectedButton = LOC_ALL;
					}
					this.fieldSelectedOption = selectedOption.toString();
					this.fieldSelectedArea = selectedOption.getArea();
					this.fieldSelectedLoc = selectedOption.getLocation();
					this.fieldSelectedDesc = selectedOption.getDesc();
					this.fieldProceedSelected = true;
					this.dispose();
				}
			}
			else if (source == this.pbSkip)
			{
				this.fieldProceedSelected = false;
				this.fieldSelectedButton = SKIP;
				this.dispose();
			}
			else if (source == this.pbClearAll)
			{
				this.fieldProceedSelected = false;
				this.fieldSelectedButton = CLEAR_ALL;
				this.dispose();
			}
			else if (source == this.pbCancel)
			{
				this.fieldProceedSelected = false;
				this.fieldSelectedButton = CANCEL;
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
		final Object source = ke.getSource();
		if (keyCode == KeyEvent.VK_F2)
		{
			this.pbProceed.requestFocusInWindow();
			this.pbProceed.doClick();
		}
		else if (keyCode == KeyEvent.VK_F4)
		{
			this.pbLocAll.requestFocusInWindow();
			this.pbLocAll.doClick();
		}
		else if (keyCode == KeyEvent.VK_ENTER)
		{
			if (source == this.pbSkip)
			{
				this.pbSkip.requestFocusInWindow();
				this.pbSkip.doClick();
			}
			if (source == this.pbLocAll)
			{
				this.pbLocAll.requestFocusInWindow();
				this.pbLocAll.doClick();
			}
			if (source == this.pbClearAll)
			{
				this.pbClearAll.requestFocusInWindow();
				this.pbClearAll.doClick();
			}
			else if (source == this.tfInput)
			{
				boolean found = false;
				DefaultListModel listModel = (DefaultListModel)this.lstOptions.getModel();
				for (int y=0; y<listModel.size() && !found; y++)
				{
					MFSLocation loc = (MFSLocation)listModel.elementAt(y);
					if (loc.getLocation().startsWith(this.tfInput.getText()))
					{
						found = true;
					}
					this.lstOptions.setSelectedIndex(y);
					this.lstOptions.ensureIndexIsVisible(y);
				}
				this.pbProceed.requestFocus();
			}
			else
			{
				super.keyPressed(ke);
			}
		}
		else
		{
			if (source == this.tfInput)
			{
				boolean found = false;
				String find = "";
				if (keyCode == KeyEvent.VK_BACK_SPACE)
				{
					find = this.tfInput.getText().substring(0,this.tfInput.getText().length()-1);
				}
				else
				{
					find = this.tfInput.getText()+ke.getKeyChar();
				}
				DefaultListModel listModel = (DefaultListModel)this.lstOptions.getModel();
				for (int y=0; y<listModel.size() && !found; y++)
				{
					MFSLocation loc = (MFSLocation)listModel.elementAt(y);
					if (loc.getLocation().startsWith(find))
					{
						found = true;
					}
					this.lstOptions.setSelectedIndex(y);
					this.lstOptions.ensureIndexIsVisible(y);
				}
			}
			else
			{
				super.keyPressed(ke);
			}
		}
	}
	
	

	/**
	 * Invoked the first time a window is made visible.
	 * @param we the <code>WindowEvent</code>
	 */
	public void windowOpened(WindowEvent we)
	{
		if (we.getSource() == this)
		{
			this.tfInput.requestFocusInWindow();
//			this.lstOptions.requestFocusInWindow();
		}
	}
}
