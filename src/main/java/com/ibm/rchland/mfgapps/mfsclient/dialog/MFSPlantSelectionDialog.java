/* @ Copyright IBM Corporation 2007. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2007-06-18      37556CD  T He             -Initial version
 * 2007-07-16   ~1 39266SM  R Prechel        -Update title of MFSFrame
 * 2007-11-06   ~2 40104PB  R Prechel        -Environment switch changes
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.dialog;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import com.ibm.rchland.mfgapps.mfsclient.MFSDialog;
import com.ibm.rchland.mfgapps.mfsclient.MFSFrame;
import com.ibm.rchland.mfgapps.mfsclient.MFSMain;
import com.ibm.rchland.mfgapps.mfscommon.MFSComputerName;
import com.ibm.rchland.mfgapps.mfscommon.MFSConfig;

/**
 * <code>MFSPlantSelectionDialog</code> is the <code>MFSDialog</code> used
 * to select an alternate plant.
 * @author The MFS Client Development Team
 */
public class MFSPlantSelectionDialog
	extends MFSDialog
{
	private static final long serialVersionUID = 1L;
	//~2C Use getEnvironments() to get the JComboBox options
	/** The list of alternate plants <code>JComboBox</code>. */
	private JComboBox cbPlants = new JComboBox(MFSConfig.getInstance().getEnvironments());

	/** The Switch <code>JButton</code>. */
	private JButton pbSwitch = createButton("Switch", 'S');

	/** The Cancel <code>JButton</code>. */
	private JButton pbCancel = createButton("Cancel", 'C');

	/**
	 * Constructs a new <code>MFSPlantSelectionDialog</code>.
	 * @param parent the <code>MFSFrame</code> that caused this
	 *        <code>MFSPlantSelectionDialog</code> to be displayed
	 */
	public MFSPlantSelectionDialog(MFSFrame parent)
	{
		super(parent, "Switch MFS plant");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		createLayout();
		addMyListeners();
	}

	/** Adds this dialog's <code>Component</code>s to the layout. */
	private void createLayout()
	{
		JPanel contentPane = new JPanel(new GridBagLayout());
		contentPane.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 2, 1, 1, 1,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, 
				new Insets(0, 10, 0, 10), 0, 0);

		contentPane.add(this.cbPlants, gbc);

		gbc.gridx = 0;
		gbc.gridy++;
		gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.insets = new Insets(20, 10, 0, 10);
		contentPane.add(this.pbSwitch, gbc);

		gbc.gridx = 1;
		gbc.gridwidth = 1;
		contentPane.add(this.pbCancel, gbc);

		setContentPane(contentPane);
		setButtonDimensions(this.pbSwitch, this.pbCancel);
		pack();
	}

	/** Adds the listeners to this dialog's <code>Component</code>s. */
	private void addMyListeners()
	{
		this.pbSwitch.addActionListener(this);
		this.pbCancel.addActionListener(this);

		this.pbSwitch.addKeyListener(this);
		this.pbCancel.addKeyListener(this);
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
			if (source == this.pbCancel)
			{
				dispose();
			}
			else if (source == this.pbSwitch)
			{
				String selection = (String) this.cbPlants.getSelectedItem();
				//~2C Use the confirmSwitch method
				if (MFSConfig.getInstance().confirmSwitch(this, selection))
				{
					String compName = MFSConfig.getInstance().getComputerName(selection);
					MFSComputerName.setComputerName(compName);
					this.dispose();
					getParentFrame().dispose();
					//~2C MFSMain.main handles the switching logic
					MFSMain.main(null);
				}
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
			if (source == this.pbSwitch)
			{
				this.pbSwitch.requestFocusInWindow();
				this.pbSwitch.doClick();
			}
			else if (source == this.pbCancel)
			{
				this.pbCancel.requestFocusInWindow();
				this.pbCancel.doClick();
			}
		}
	}

	/**
	 * Invoked the first time a window is made visible.
	 * <p>
	 * Sets the initial selection of the combo box and requests the focus for
	 * {@link #cbPlants}.
	 * @param we the <code>WindowEvent</code>
	 */
	public void windowOpened(WindowEvent we)
	{
		if (we.getSource() == this)
		{
			this.cbPlants.setSelectedIndex(0); //~2C
			this.cbPlants.requestFocusInWindow();
		}
	}
}
