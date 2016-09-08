/* © Copyright IBM Corporation 2007. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2007-07-30      27794JR  Toribio Hdez.    -initial version
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.dialog;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import com.ibm.rchland.mfgapps.mfsclient.MFSFrame;
import com.ibm.rchland.mfgapps.mfsclient.MFSDialog;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSConstants;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSScroller;

/**
 * <code>MFSShowPartinErrorDialog</code> is the <code>MFSListDialog</code> used to
 * Show a list of errors of parts.
 * @author The MFS Client Development Team
 */
public class MFSShowPartinErrorDialog
	extends MFSDialog
{
	private static final long serialVersionUID = 1L;
	/** The OK <code>JButton</code>. */
	private JButton pbOk = createButton("OK", 'O');

	/** The <code>JList</code> that displays the errors. */
	private JList lstError = createList();

	/** The <code>JScrollPane</code> that contains the errors list. */
	private JScrollPane spError = new JScrollPane(this.lstError,
			ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
			ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

	/** The messages List Model <code>DefaultListModel</code>. */
	private DefaultListModel messagesListM = new DefaultListModel();

	/**
	 * Constructs a new <code>MFSShowPartinErrorDialog</code>.
	 * @param parent the <code>MFSFrame</code> that caused this
	 *        <code>MFSShowPartinErrorDialog</code> to be displayed
	 */
	public MFSShowPartinErrorDialog(MFSFrame parent)
	{
		super(parent, "Parts with Errors");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.lstError.setModel(this.messagesListM);
		createLayout();
		addMyListeners();
	}

	/** Adds this dialog's <code>Component</code>s to the layout. */
	protected void createLayout()
	{
		JPanel contentPane = new JPanel(new GridBagLayout());
		contentPane.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
		
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, 
				new Insets(0, 2, 0, 2), 0, 0);

		JLabel headerLabel = new JLabel(" Crct  Part Number   Error Message  ");
		headerLabel.setFont(MFSConstants.MEDIUM_MONOSPACED_FONT);
		
		this.spError.setColumnHeaderView(headerLabel);

		JLabel subTittleLabel = 
			new JLabel("Errors occurred when removing parts:");
		gbc.insets = new Insets(0, 0, 0, 0);
		contentPane.add(subTittleLabel, gbc);
		
		gbc.gridy++;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(5, 0, 0, 0);
		contentPane.add(this.spError, gbc);
		
		gbc.gridy++;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(20, 0, 0, 0);
		contentPane.add(this.pbOk, gbc);

		setContentPane(contentPane);
	}
	/** Adds the listeners to this dialog's <code>Component</code>s. */
	private void addMyListeners()
	{
		this.pbOk.addActionListener(this);
		
		this.pbOk.addKeyListener(this);
		this.lstError.addKeyListener(this);
		this.spError.addKeyListener(this);

	}
	/**
	 * Adds an error message to the list model
	 * @param crct component record count string.
	 * @param inpn installed part number.
	 * @param errorMsg that will be loaded in the list Model
	 */
	public void addMessage(String crct, String inpn, String errorMsg)
	{
		StringBuffer row = new StringBuffer();
			
		row.append(" ");//$NON-NLS-1$
		row.append(crct);
		row.append("  ");//$NON-NLS-1$
		row.append(inpn);
		row.append("  ");//$NON-NLS-1$
		row.append(errorMsg);
		row.append("  ");//$NON-NLS-1$	
					
		this.messagesListM.addElement(row.toString());
	}
	/** {@inheritDoc} */
	public void pack()
	{
	    Dimension d = this.spError.getPreferredSize();
	    int width = d.width;
	    if (width > 700)
	    {
	        width = 700;
	    }
	    else if (width < 300)
	    {
	        width = 300;
	    }
	    this.spError.setPreferredSize(new Dimension(width, d.height));
	    super.pack();
	}
	/**
	 * Invoked when an action occurs.
	 * @param ae the <code>ActionEvent</code>
	 */
	public void actionPerformed(ActionEvent ae)
	{
		final Object source = ae.getSource();
		if (source == this.pbOk)
		{
			this.dispose();
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
			this.pbOk.requestFocusInWindow();
			this.pbOk.doClick();
		}
		else if (keyCode == KeyEvent.VK_ESCAPE)
		{
			this.dispose();
		}
		else if (keyCode == KeyEvent.VK_PAGE_DOWN)
		{
			MFSScroller.scrollDown(this.spError, this.lstError);
			ke.consume();
		}
		else if (keyCode == KeyEvent.VK_PAGE_UP)
		{
			MFSScroller.scrollUp(this.spError, this.lstError);
			ke.consume();
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
			this.lstError.requestFocusInWindow();
			this.spError.getHorizontalScrollBar().setValue(0);
		}
	}
}