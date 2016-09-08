/* © Copyright IBM Corporation 2005, 2007. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2007-03-13      34242JR  R Prechel        -Java 5 version
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.dialog;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import com.ibm.rchland.mfgapps.client.utils.messagebox.IGSMessageBox;
import com.ibm.rchland.mfgapps.mfsclient.MFSDialog;
import com.ibm.rchland.mfgapps.mfsclient.MFSFrame;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSConstants;

/**
 * <code>MFSDspWIPWUDialog</code> is the <code>MFSDialog</code> used
 * to display the output of the RTV_WU10 transaction.
 * @author The MFS Client Development Team
 */
public class MFSDspWIPWUDialog
	extends MFSDialog
{
	private static final long serialVersionUID = 1L;
	/** The heading <code>JLabel</code>. */
	private JLabel lbHeading;
	
	/** The <code>JList</code> that displays the work units. */
	private JList lstWorkUnits = createList();

	/** The <code>JScrollPane</code> that contains the list of work units. */
	private JScrollPane spWorkUnits = new JScrollPane(this.lstWorkUnits,
			ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
			ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

	/** The Cancel <code>JButton</code>. */
	private JButton pbCancel = createButton("Cancel", 'n');
	
	/**
	 * Constructs a new <code>MFSDspWIPWUDialog</code>.
	 * @param parent the <code>MFSFrame</code> that caused this
	 *        <code>MFSDspWIPWUDialog</code> to be displayed
	 * @param watr the WATR the user entered
	 * @param nmbr the NMBR the user entered
	 */
	public MFSDspWIPWUDialog(MFSFrame parent, String watr, String nmbr)
	{
		super(parent, "Display Work Units");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLabelText(watr, nmbr);
		createLayout();
		addMyListeners();
	}
	
	/**
	 * Sets the text of the heading <code>JLabel</code>
	 * @param watr the WATR the user entered
	 * @param nmbr the NMBR the user entered
	 */
	private void setLabelText(String watr, String nmbr)
	{
		StringBuffer text = new StringBuffer();
		text.append("Work Units for:        ");
		
		if(watr.equals("")) //$NON-NLS-1$
		{
			text.append("*ALL    ");
		}
		else
		{
			text.append(watr.toUpperCase().concat("        ").substring(0, 8)); //$NON-NLS-1$
		}
		
		text.append("        "); //$NON-NLS-1$
		
		if(nmbr.equals("")) //$NON-NLS-1$
		{
			text.append("*ALL");
		}
		else
		{
			text.append(nmbr.toUpperCase().concat("    ").substring(0, 4)); //$NON-NLS-1$
		}
		this.lbHeading = createLabel(text.toString(), MFSConstants.SMALL_DIALOG_FONT);
	}
	
	/** Adds this dialog's <code>Component</code>s to the layout. */
	private void createLayout()
	{
		JPanel contentPane = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(5, 2, 5, 60), 0, 0);

		contentPane.add(this.lbHeading, gbc);

		gbc.gridy++;
		gbc.weighty = 1.0;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(0, 60, 0, 60);
		
		contentPane.add(this.spWorkUnits, gbc);
		
		gbc.gridy++;
		gbc.weighty = 0.0;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(10, 60, 20, 60);
		contentPane.add(this.pbCancel, gbc);

		setContentPane(contentPane);
	}

	/** Adds the listeners to this dialog's <code>Component</code>s. */
	private void addMyListeners()
	{
		this.pbCancel.addActionListener(this);

		this.pbCancel.addKeyListener(this);
		this.lstWorkUnits.addKeyListener(this);
		this.spWorkUnits.addKeyListener(this);
	}
	
	/**
	 * Loads the Work Unit list model using the specified data.
	 * @param data the data used to load the Work Unit list model
	 */
	public void loadwipWUListModel(String data) 
	{
		try
		{	
			int start = 0;
			int end = 8;
			final int len = data.length();

			DefaultListModel listModel = new DefaultListModel();
			this.lstWorkUnits.setModel(listModel);
			
			while (end <= len)
			{
				listModel.addElement(data.substring(start,end));
				start += 8;
				end += 8;
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
	 * Invoked when an action occurs.
	 * @param ae the <code>ActionEvent</code>
	 */
	public void actionPerformed(ActionEvent ae)
	{
		try
		{
			final Object source = ae.getSource();
			if(source == this.pbCancel)
			{
				dispose();
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
		if(keyCode == KeyEvent.VK_ESCAPE ||
				(keyCode == KeyEvent.VK_ENTER && ke.getSource() == this.pbCancel))
		{
			this.pbCancel.requestFocusInWindow();
			this.pbCancel.doClick();
		}
	}
	
	/**
	 * Invoked the first time a window is made visible.
	 * <p>
	 * Sets the selected index of {@link #lstWorkUnits} to 0 and requests the
	 * focus for {@link #lstWorkUnits}.
	 * @param we the <code>WindowEvent</code>
	 */
	public void windowOpened(WindowEvent we)
	{
		try
		{
			if (we.getSource() == this)
			{
				this.lstWorkUnits.setSelectedIndex(0);
				this.lstWorkUnits.requestFocusInWindow();
			}
		}
		catch (Throwable throwable)
		{
			throwable.printStackTrace();
		}
	}
}
