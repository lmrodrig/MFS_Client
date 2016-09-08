/* © Copyright IBM Corporation 2005, 2007. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2007-01-25      34242JR  R Prechel        -Java 5 version
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.dialog;

import java.awt.Font;
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
 * <code>MFSDspScnPrtsFlagsDialog</code> is the <code>MFSDialog</code> used
 * to display the output of the DSP_FLGS transaction.
 * @author The MFS Client Development Team
 */
public class MFSDspScnPrtsFlagsDialog
	extends MFSDialog
{
	private static final long serialVersionUID = 1L;
	/** The heading <code>JLabel</code>. */
	private JLabel lbHeading;
	
	/** The <code>JList</code> that displays the flags. */
	private JList lstFlags = createList();

	/** The <code>JScrollPane</code> that contains the list of flags. */
	private JScrollPane spFlags = new JScrollPane(this.lstFlags,
			ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
			ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

	/** The Close <code>JButton</code>. */
	private JButton pbClose = createButton("Close", 'c');
	
	/** The <code>Font</code> used by the <code>JList</code>. */
	private final Font FONT = new Font("Monospaced", Font.PLAIN, 12); //$NON-NLS-1$

	/**
	 * Constructs a new <code>MFSDspScnPrtsFlagsDialog</code>.
	 * @param parent the <code>MFSFrame</code> that caused this
	 *        <code>MFSDspScnPrtsFlagsDialog</code> to be displayed
	 * @param prln the PRLN the user entered
	 * @param nmbr the NMBR the user entered
	 */
	public MFSDspScnPrtsFlagsDialog(MFSFrame parent, String prln, String nmbr)
	{
		super(parent, "Display Flags");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLabelText(prln, nmbr);
		createLayout();
		addMyListeners();
	}
	
	/**
	 * Sets the text of the heading <code>JLabel</code>
	 * @param prln the PRLN the user entered
	 * @param nmbr the NMBR the user entered
	 */
	private void setLabelText(String prln, String nmbr)
	{
		StringBuffer text = new StringBuffer();
		text.append("Flags set for:        ");
		text.append(prln.toUpperCase().concat("        ").substring(0, 8)); //$NON-NLS-1$
		text.append("        "); //$NON-NLS-1$
		text.append(nmbr.toUpperCase().concat("    ").substring(0, 4)); //$NON-NLS-1$
		this.lbHeading = createLabel(text.toString(), MFSConstants.SMALL_DIALOG_FONT);
	}
	
	/** Adds this dialog's <code>Component</code>s to the layout. */
	private void createLayout()
	{
		this.lstFlags.setFont(this.FONT);
		
		JPanel contentPane = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(5, 2, 5, 10), 0, 0);

		contentPane.add(this.lbHeading, gbc);

		gbc.gridy++;
		gbc.weighty = 1.0;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(0, 10, 0, 10);
		
		contentPane.add(this.spFlags, gbc);
		
		gbc.gridy++;
		gbc.weighty = 0.0;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(10, 10, 20, 10);
		contentPane.add(this.pbClose, gbc);

		setContentPane(contentPane);
		//The JList is wide, so call setSize
		this.setSize(320, 280);
	}

	/** Adds the listeners to this dialog's <code>Component</code>s. */
	private void addMyListeners()
	{
		this.pbClose.addActionListener(this);

		this.pbClose.addKeyListener(this);
		this.lstFlags.addKeyListener(this);
		this.spFlags.addKeyListener(this);
	}
	
	/**
	 * Loads the Flags list model using the specified data.
	 * @param data the data used to load the Flags list model
	 */
	public void loadPrlnOperFlagsListModel(String data) 
	{
		try
		{	
			int start = 0;
			int mid1 = 8;
			int mid2 = 16;
			int end = 96;
			final int len = data.length();

			DefaultListModel listModel = new DefaultListModel();
			this.lstFlags.setModel(listModel);
			
			while (end <= len)
			{
	 			if(data.substring(mid1,mid2).equals("        "))
	 			{
	 			  listModel.addElement(data.substring(start,mid1));
	 			}
		 		else
		 		{
		 			StringBuffer element = new StringBuffer();
		 			element.append(data.substring(start,mid1));
		 			element.append(" - ");
		 			element.append(data.substring(mid1,mid2));
		 			element.append(" - ");
		 			element.append(data.substring(mid2,end));
		 			listModel.addElement(element.toString());
		 		}
				start += 96;
				mid1 += 96;
				mid2 += 96;
				end += 96;
			}
			
			listModel.addElement(data.substring(start));
			
			//The JList is wide; do not call pack
			//setSize is called in createLayout
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, null, e);
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
			if(source == this.pbClose)
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
				(keyCode == KeyEvent.VK_ENTER && ke.getSource() == this.pbClose))
		{
			this.pbClose.requestFocusInWindow();
			this.pbClose.doClick();
		}
	}
	
	/**
	 * Invoked the first time a window is made visible.
	 * <p>
	 * Sets the selected index of {@link #lstFlags} to 0 and requests the
	 * focus for {@link #lstFlags}.
	 * @param we the <code>WindowEvent</code>
	 */
	public void windowOpened(WindowEvent we)
	{
		try
		{
			if (we.getSource() == this)
			{
				this.lstFlags.setSelectedIndex(0);
				this.lstFlags.requestFocusInWindow();
			}
		}
		catch (Throwable throwable)
		{
			throwable.printStackTrace();
		}
	}
}
