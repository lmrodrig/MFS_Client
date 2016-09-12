/* @ Copyright IBM Corporation 2008, 2010. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2008-04-03      37616JL  R Prechel        -Initial version
 * 2008-05-23   ~1 41674JM  Santiago D		 -PackCode insted of DimCode, changed weights and dims
 * 2008-07-23   ~2 42330JM  Santiago D		 -New warning message if RTV_WGTDIM not found containers
 * 2010-08-31   ~3 46704EM  Edgar V          -Add a new parameter String configValue in displayForWorkUnit()
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.dialog;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.text.MessageFormat;
import java.util.Hashtable;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.ibm.rchland.mfgapps.client.utils.exception.IGSException;
import com.ibm.rchland.mfgapps.client.utils.messagebox.IGSMessageBox;
import com.ibm.rchland.mfgapps.client.utils.xml.IGSXMLTransaction;
import com.ibm.rchland.mfgapps.mfsclient.MFSActionableDialog;
import com.ibm.rchland.mfgapps.mfsclient.MFSFrame;
import com.ibm.rchland.mfgapps.mfsclient.struct.MFSWeightDim;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSConstants;
import com.ibm.rchland.mfgapps.mfscommon.MFSActionable;
import com.ibm.rchland.mfgapps.mfscommon.MFSConfig;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSComm;

/**
 * <code>MFSWeightDimDialog</code> is the <code>MFSActionableDialog</code>
 * used to collect and update weight and dimensions.
 * @author The MFS Client Development Team
 */
public class MFSWeightDimDialog
	extends MFSActionableDialog
{
	private static final long serialVersionUID = 1L;
	/** The container number output <code>JTextField</code>. */
	private JTextField tfContainer;

	/** The pack code input <code>JTextField</code>. */
	private JComboBox cbPackCode;  //~1C

	/** The length input <code>JTextField</code>. */
	private JTextField tfLength;

	/** The length unit of measure <code>JLabel</code>. */
	private JLabel lblLengthUnit = createLabel(""); //$NON-NLS-1$

	/** The width input <code>JTextField</code>. */
	private JTextField tfWidth;

	/** The width unit of measure <code>JLabel</code>. */
	private JLabel lblWidthUnit = createLabel(""); //$NON-NLS-1$

	/** The height input <code>JTextField</code>. */
	private JTextField tfHeight;

	/** The height unit of measure <code>JLabel</code>. */
	private JLabel lblHeightUnit = createLabel(""); //$NON-NLS-1$

	/** The weight input <code>JTextField</code>. */
	private JTextField tfWeight;

	/** The weight unit of measure <code>JLabel</code>. */
	private JLabel lblWeightUnit = createLabel(""); //$NON-NLS-1$

	/** The enter <code>JButton</code>. */
	private JButton pbEnter = createButton("Enter", 'E');

	/** The cancel <code>JButton</code>. */
	private JButton pbCancel = createButton("Cancel", 'n');

	/**
	 * The <code>MFSActionable</code> that caused this
	 * <code>MFSWeightDimDialog</code> to be displayed.
	 */
	private final MFSActionable fieldActionable;

	/** The maximum container dimension. */
	private String fieldMaxDimension;

	/** The maximum container weight. */
	private String fieldMaxWeight;

	/** Set <code>true</code> when the cancel button is selected. */
	private boolean fieldCancelSelected = false;
	
	/** The <code>Hashtable</code> of containers. */
	@SuppressWarnings("rawtypes")
	private Hashtable containerTable;
	

	/**
	 * Constructs a new <code>MFSWeightDimDialog</code>.
	 * @param parent the <code>MFSFrame</code> that caused this
	 *        <code>MFSWeightDimDialog</code> to be displayed
	 * @param actionable the <code>MFSActionable</code> that caused this
	 *        <code>MFSWeightDimDialog</code> to be displayed
	 */
	public MFSWeightDimDialog(MFSFrame parent, MFSActionable actionable)
	{
		super(parent, "Collect Weight and Dimensions");
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.fieldActionable = actionable;
		createLayout();
		addMyListeners();
	}

	/** Adds this dialog's <code>Component</code>s to the layout. */
	private void createLayout()
	{
		JLabel lblContainer = createNameLabel("Container:");
		JLabel lblPackCode = createNameLabel("Pack Code:"); //~1C
		JLabel lblLength = createNameLabel("Length:");
		JLabel lblWidth = createNameLabel("Width:");
		JLabel lblHeight = createNameLabel("Height:");
		JLabel lblWeight = createNameLabel("Weight:");

		this.tfContainer = createTextField(MFSConstants.LARGE_TF_COLS, 0, lblContainer);
		this.tfContainer.setEditable(false);
		this.cbPackCode = new JComboBox(); //~1C
		this.cbPackCode.setSize(20, 18); //~1A
		this.cbPackCode.setEditable(false); //~1A
		this.tfLength = createTextField(MFSConstants.SMALL_TF_COLS, 3, lblLength);
		this.tfWidth = createTextField(MFSConstants.SMALL_TF_COLS, 3, lblWidth);
		this.tfHeight = createTextField(MFSConstants.SMALL_TF_COLS, 3, lblHeight);
		this.tfWeight = createTextField(MFSConstants.SMALL_TF_COLS, 6, lblWeight);

		JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 70, 0));
		buttonPanel.add(this.pbEnter);
		buttonPanel.add(this.pbCancel);

		JPanel contentPane = new JPanel(new GridBagLayout());
		contentPane.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 0, 1.0,
				GridBagConstraints.EAST, GridBagConstraints.NONE, 
				new Insets(10, 0, 10, 10), 0, 0);

		contentPane.add(lblContainer, gbc);
		gbc.gridy++;
		contentPane.add(lblPackCode, gbc); //~1C
		gbc.gridy++;
		contentPane.add(lblLength, gbc);
		gbc.gridy++;
		contentPane.add(lblWidth, gbc);
		gbc.gridy++;
		contentPane.add(lblHeight, gbc);
		gbc.gridy++;
		contentPane.add(lblWeight, gbc);

		gbc.gridx++;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets.right = 2;
		contentPane.add(this.tfContainer, gbc);
		gbc.gridy++;
		gbc.gridwidth = 1;
		contentPane.add(this.cbPackCode, gbc); //~1C
		gbc.gridy++;
		contentPane.add(this.tfLength, gbc);
		gbc.gridy++;
		contentPane.add(this.tfWidth, gbc);
		gbc.gridy++;
		contentPane.add(this.tfHeight, gbc);
		gbc.gridy++;
		contentPane.add(this.tfWeight, gbc);

		gbc.gridx++;
		gbc.gridy = 2;
		gbc.weightx = 1;
		gbc.anchor = GridBagConstraints.WEST;
		contentPane.add(this.lblLengthUnit, gbc);
		gbc.gridy++;
		contentPane.add(this.lblWidthUnit, gbc);
		gbc.gridy++;
		contentPane.add(this.lblHeightUnit, gbc);
		gbc.gridy++;
		contentPane.add(this.lblWeightUnit, gbc);

		gbc.gridx = 0;
		gbc.gridy++;
		gbc.gridwidth = 3;
		gbc.insets.top = 20;
		gbc.anchor = GridBagConstraints.CENTER;
		contentPane.add(buttonPanel, gbc);

		setContentPane(contentPane);
		pack();
	}

	/** Adds the listeners to this dialog's <code>Component</code>s. */
	private void addMyListeners()
	{
		this.pbEnter.addActionListener(this);
		this.pbCancel.addActionListener(this);
		this.cbPackCode.addActionListener(this); //~1A	

		this.pbEnter.addKeyListener(this);
		this.pbCancel.addKeyListener(this);
	}
	
	//~1A
	/** Creates a <code>Hashtable</code>that contains all containers. */	
	@SuppressWarnings("rawtypes")
	protected void initializeContainerTable()
	{
		containerTable = new Hashtable();
	}
	
	//~1A
	/** Gets the <code>Object</code> found in the containerTable with the key Object.
	 * @param key the <code>Object</code> to search within the Hashtable.
	 * @return <code>Object</code> related to the key Object.
	 */
	@SuppressWarnings("rawtypes")
	public Object getContainerTableObject(Object key)
	{
		if(key instanceof ContainerTableKey)
		{
			ContainerTableKey ctk = (ContainerTableKey) key;
			Iterator iterator = containerTable.keySet().iterator();
			while(iterator.hasNext())
			{
				ContainerTableKey ctk2 = (ContainerTableKey) iterator.next();
				if(ctk.getContainer().compareToIgnoreCase(ctk2.getContainer()) == 0 &&
						ctk.getPackCode().compareToIgnoreCase(ctk2.getPackCode()) == 0)
				{
					return containerTable.get(ctk2);
				}
			}
		}
		
		return null;
	}

	//~1A
	/** Puts and object in the containerTable
	 * 
	 * @param container the <code>String</code> which will be the key for the containerTable.
	 * @param packCode the <code>String</code> which will be the key for the containerTable.
	 * @param wd the <code>MFSWeightDim</code> that has all packCode properties.
	 */
	@SuppressWarnings("unchecked")
	public void addWeightDimToContainerTable(String container, String packCode, MFSWeightDim wd)
	{
		ContainerTableKey ctk = new ContainerTableKey(container, packCode);
        containerTable.put(ctk, wd);
	}
	
	/**
	 * Calls RTV_WGTDIM. For each container in the output returned by
	 * RTV_WGTDIM, the dialog's state is setup using the data from RTV_WGTDIM
	 * and the dialog is displayed.
	 * @param rtvWgtDim an <code>IGSXMLTransaction</code> setup with the input
	 *        for RTV_WGTDIM
	 * @return 0 on success; nonzero on error
	 */
	private int display(IGSXMLTransaction rtvWgtDim)
	{
		this.fieldCancelSelected = false;

		MFSComm.getInstance().execute(rtvWgtDim, this.fieldActionable);
		int rc = rtvWgtDim.getReturnCode(); //~1A
		if (rc != 0) //~1C
		{
			String title = "Error Retrieving Weight and Dimensions";
			IGSMessageBox.showOkMB(getParentFrame(), title, rtvWgtDim.getErms(), null);
			rc = rtvWgtDim.getReturnCode();
		}
		else
		{
			initializeContainerTable();	 //~1A						
			
			String dimensionUnit = rtvWgtDim.getNextElement("DUOM"); //$NON-NLS-1$
			this.lblLengthUnit.setText(dimensionUnit);
			this.lblWidthUnit.setText(dimensionUnit);
			this.lblHeightUnit.setText(dimensionUnit);
			this.lblWeightUnit.setText(rtvWgtDim.getNextElement("WUOM")); //$NON-NLS-1$
			
			while (rtvWgtDim.stepIntoElement("CNTR") != null) //~1C
			{
	
				this.cbPackCode.setModel(new DefaultComboBoxModel());
				
				String container = rtvWgtDim.getNextElement("NUM");
				this.tfContainer.setText(container);
				
				while (rtvWgtDim.stepIntoElement("RCD") != null) //$NON-NLS-1$
				{
					MFSWeightDim dim = new MFSWeightDim(rtvWgtDim);
					addWeightDimToContainerTable(container, dim.getPackCode(), dim);					
					this.cbPackCode.addItem(dim.getPackCode());
					rtvWgtDim.stepOutOfElement();
				}
				
				if(rtvWgtDim.stepIntoElement("DEFAULT") != null)
				{
					MFSWeightDim dim = new MFSWeightDim(rtvWgtDim);				
					this.cbPackCode.setSelectedItem(dim.getPackCode());					
					this.tfHeight.setText(String.valueOf(dim.getHeight()));
					this.tfLength.setText(String.valueOf(dim.getLength()));
					this.tfWidth.setText(String.valueOf(dim.getWidth()));
					this.tfWeight.setText(String.valueOf(dim.getWeight()));
					rtvWgtDim.stepOutOfElement();
				}
				else
				{
					if(this.cbPackCode.getItemCount() > 0)
					{
						this.cbPackCode.setSelectedIndex(0);
					}
					else // case Container without PackCodes
					{
						String erms = "ERROR: No Pack Codes are configured for Container: "+container; // ~2C
						IGSMessageBox.showOkMB(getParentFrame(), "Container Error", erms, null);
						
						rc = 10; // Need to configure this container properly
						
						continue;
					}
				}
				
				rtvWgtDim.stepOutOfElement();
				pack();
				setLocationRelativeTo(getParentFrame());
				setVisible(true);	
				
				if (this.fieldCancelSelected)
				{
					break;
				}				
			}
		}
		return rc;
	}

	/**
	 * Displays the dialog for a single container.
	 * @param cntr the container
	 * @return 0 on success; nonzero on error
	 */
	public int displayForCntr(String cntr)
	{
		IGSXMLTransaction rtvWgtDim = new IGSXMLTransaction("RTV_WGTDIM"); //$NON-NLS-1$
		rtvWgtDim.setActionMessage("Retrieving Weight and Dimensions...");
		rtvWgtDim.startDocument();
		rtvWgtDim.addElement("MODE", "C"); //$NON-NLS-1$ //$NON-NLS-2$
		rtvWgtDim.addElement("CNTR", cntr); //$NON-NLS-1$
		rtvWgtDim.endDocument();
		return display(rtvWgtDim);
	}

	/**
	 * Displays the dialog for each top level container for the work unit.
	 * @param mctl the work unit control number
	 * @return 0 on success; nonzero on error
	 */
	public int displayForWorkUnit(String mctl, String retrieveType)//~3C
	{
		IGSXMLTransaction rtvWgtDim = new IGSXMLTransaction("RTV_WGTDIM"); //$NON-NLS-1$
		rtvWgtDim.setActionMessage("Retrieving Weight and Dimensions...");
		rtvWgtDim.startDocument();
		rtvWgtDim.addElement("MODE", "M"); //$NON-NLS-1$ //$NON-NLS-2$
		rtvWgtDim.addElement("MCTL", mctl); //$NON-NLS-1$
  	    rtvWgtDim.addElement("RETRIEVETYPE",retrieveType);//~3A
		rtvWgtDim.endDocument();
		return display(rtvWgtDim);
	}

	/**
	 * Returns <code>true</code> iff the cancel button was selected.
	 * @return <code>true</code> iff the cancel button was selected
	 */
	public boolean getCancelSelected()
	{
		return this.fieldCancelSelected;
	}

	/**
	 * Populates the dimension <code>JtextField</code>s based on the
	 * dimension code entered by the user.
	 */
	private void processPackCode()
	{
		final String container = this.tfContainer.getText();
		final String packCode = (String) this.cbPackCode.getSelectedItem(); //~1C
		
		if (packCode.length() > 0)
		{
			ContainerTableKey ctk = new ContainerTableKey(container, packCode); //~1A
			MFSWeightDim dim = (MFSWeightDim) getContainerTableObject(ctk); //~1C

			if (dim != null)
			{
				this.tfHeight.setText(String.valueOf(dim.getHeight()));
				this.tfLength.setText(String.valueOf(dim.getLength()));
				this.tfWidth.setText(String.valueOf(dim.getWidth()));
				this.tfWeight.setText(String.valueOf(dim.getWeight())); //~1A
				this.fieldMaxDimension = (String.valueOf(dim.getMaxDimension())); //~1A
				this.fieldMaxWeight = (String.valueOf(dim.getMaxWeight())); //~1A
			}
			else
			{
				String title = "Invalid Pack Code";
				String erms = "Pack Code ''{0}'' is not configured.";
				Object arguments[] = {
					packCode //~1A
				};
				erms = MessageFormat.format(erms, arguments);
				IGSMessageBox.showOkMB(getParentFrame(), title, erms, null);
			}
		}
	}

	/**
	 * Validates the input and calls UPD_WGTDIM if the input if valid to update
	 * the weight and dimensions of the container.
	 */
	private void updateWeightDim()
	{
		try
		{
			final String container = this.tfContainer.getText().trim(); //~1A
			final String packCode = (String) this.cbPackCode.getSelectedItem(); //~1A
			final String length = this.tfLength.getText().trim();
			final String width = this.tfWidth.getText().trim();
			final String height = this.tfHeight.getText().trim();
			final String weight = this.tfWeight.getText().trim();
			
			MFSWeightDim dim = new MFSWeightDim(packCode, length, width, height, weight,
					this.fieldMaxDimension, this.fieldMaxWeight); //~1C

			ContainerTableKey ctk = new ContainerTableKey(container, packCode); //~1A
			MFSWeightDim packCodeDim = (MFSWeightDim) getContainerTableObject(ctk); //~1C		

			if (packCode.length() > 0 && packCodeDim == null) //~1C
			{
				String title = "Invalid Pack Code"; 
				String erms = "Pack code ''{0}'' is not configured."; 
				Object arguments[] = {
					packCode 
				};
				erms = MessageFormat.format(erms, arguments);
				IGSMessageBox.showOkMB(getParentFrame(), title, erms, null);
			}
			/*else if (packCodeDim != null && !dim.sameDimensions(packCodeDim)) //~1C
			{
				String title = "Pack Code Error";
				String erms = "Specified dimensions do not match pack code. Please clear the pack code or update the dimensions.";
				IGSMessageBox.showOkMB(this, title, erms, null);
			}*/
			else
			{
				dim.validate(this.fieldMaxDimension, this.fieldMaxWeight);

				IGSXMLTransaction updWgtDim = new IGSXMLTransaction("UPD_WGTDIM"); //$NON-NLS-1$
				updWgtDim.setActionMessage("Updating Weight and Dimensions...");
				updWgtDim.startDocument();
				updWgtDim.addElement("CNTR", container); //$NON-NLS-1$
				updWgtDim.addElement("PACK", packCode); //$NON-NLS-1$ //~1C
				updWgtDim.addElement("LGTH", length); //$NON-NLS-1$
				updWgtDim.addElement("WDTH", width); //$NON-NLS-1$
				updWgtDim.addElement("HGHT", height); //$NON-NLS-1$
				updWgtDim.addElement("WGHT", weight); //$NON-NLS-1$
				updWgtDim.addElement("USER", MFSConfig.getInstance().get8CharUser()); //$NON-NLS-1$
				updWgtDim.endDocument();

				MFSComm.getInstance().execute(updWgtDim, this);
				if (updWgtDim.getReturnCode() == 0)
				{
					dispose();
				}
				else
				{
					String title = "Update Error";
					IGSMessageBox.showOkMB(this, title, updWgtDim.getErms(), null);
				}
			}
		}
		catch (IGSException e)
		{
			String title = "Weight/Dimension Error";
			IGSMessageBox.showOkMB(this, title, e.getMessage(), null);
		}
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
			if (source == this.cbPackCode)
			{
				processPackCode();
			}
			else if (source == this.pbEnter)
			{
				updateWeightDim();
			}
			else if (source == this.pbCancel)
			{
				this.fieldCancelSelected = true;
				dispose();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
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
				this.pbEnter.requestFocusInWindow();
				this.pbEnter.doClick();
			}
		}
		else if (keyCode == KeyEvent.VK_ESCAPE)
		{
			this.pbCancel.requestFocusInWindow();
			this.pbCancel.doClick();
		}
	}
	
	/**
	 * <code>ContainerTableKey</code> is the key for the containerTable.
	 * @author The MFS Client Development Team
	 */	
	protected class ContainerTableKey
	{
		/** The container */		
		private String container;
		
		/** The packCode */		
		private String packCode;
		
		/**
		 * Constructs a new <code>ContainerTableKey</code>.
		 * @param container the <code>String</code> needed to iterate through
		 *        the containerTable
		 * @param packCode the <code>String</code> needed to iterate through
		 *        the containerTable
		 */		
		public ContainerTableKey(String container, String packCode)
		{
			this.container = container;
			this.packCode = packCode;
		}
		
		/** Gets the container
		 * @return container <code>String</code>
		 */			
		public String getContainer()
		{
			return container;
		}
		
		/** Gets the packCode
		 * @return packCode <code>String</code>
		 */				
		public String getPackCode()
		{
			return packCode;
		}
	}
}
