/* © Copyright IBM Corporation 2005, 2007. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2007-02-21      34242JR  R Prechel        -Java 5 version
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.dialog;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.ibm.rchland.mfgapps.client.utils.messagebox.IGSMessageBox;
import com.ibm.rchland.mfgapps.mfsclient.MFSFrame;
import com.ibm.rchland.mfgapps.mfsclient.MFSListDialog;

/**
 * <code>MFSDropDownDialog</code> is the <code>MFSListDialog</code> used to
 * select the Defect Type from the options returned by RTV_DRPDWN.
 * @author The MFS Client Development Team
 */
public class MFSDropDownDialog
	extends MFSListDialog
{
	private static final long serialVersionUID = 1L;
	/**
	 * Constructs a new <code>MFSDropDownDialog</code>.
	 * @param parent the <code>MFSFrame</code> that caused this
	 *        <code>MFSDropDownDialog</code> to be displayed
	 */
	public MFSDropDownDialog(MFSFrame parent)
	{
		super(parent, "Defect Type");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		createLayout();
		addMyListeners();
	}

	/** Adds this dialog's <code>Component</code>s to the layout. */
	protected void createLayout()
	{
		JLabel infoLabel = createLabel("Please Select the Defect Type.");
		this.lstOptions.setVisibleRowCount(12);

		JPanel contentPane = new JPanel(new GridBagLayout());
		contentPane.setBorder(BorderFactory.createEmptyBorder(10, 50, 10, 50));
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 2, 1, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, 
				new Insets(0, 0, 30, 0), 0, 0);

		contentPane.add(infoLabel, gbc);

		gbc.gridy++;
		gbc.insets = new Insets(0, 0, 0, 0);
		contentPane.add(this.spOptions, gbc);

		gbc.gridy++;
		gbc.gridwidth = 1;
		gbc.weighty = 0.0;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(40, 0, 0, 0);
		contentPane.add(this.pbProceed, gbc);

		gbc.gridx = 1;
		contentPane.add(this.pbCancel, gbc);

		setContentPane(contentPane);
		setButtonDimensions(this.pbProceed, this.pbCancel);
	}

	/**
	 * Loads the list model using the specified options.
	 * @param options the options used to load the list model
	 */
	public void loadAnswerListModel(String options)
	{
		try
		{
			final int len = options.length();
			int pos = 0;

			DefaultListModel listModel = new DefaultListModel();
			this.lstOptions.setModel(listModel);

			while (pos < len)
			{
				listModel.addElement(options.substring(pos, pos + 36));
				pos += 36;
			}
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
		}
		finally
		{
			pack();
		}
	}

	/**
	 * Invoked the first time a window is made visible. Does nothing.
	 * @param we the <code>WindowEvent</code>
	 */
	public void windowOpened(WindowEvent we)
	{
		//Does nothing
	}
}
