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

import com.ibm.rchland.mfgapps.client.utils.messagebox.IGSMessageBox;
import com.ibm.rchland.mfgapps.mfsclient.MFSDialog;
import com.ibm.rchland.mfgapps.mfsclient.MFSFrame;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSConstants;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSScroller;

/**
 * <code>MFSRecvPartDialog</code> is the <code>MFSDialog</code> used to
 * select the part numbers for which recvpart1 labels are printed.
 * @author The MFS Client Development Team
 */
public class MFSRecvPartDialog
	extends MFSDialog
{
	private static final long serialVersionUID = 1L;
	/** The <code>JList</code> that displays the parts. */
	private JList lstRecvPart = new JList();

	/** The <code>JScrollPane</code> that contains the list of parts. */
	private JScrollPane spRecvPart = new JScrollPane(this.lstRecvPart,
			ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
			ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

	/** The Print <code>JButton</code>. */
	private JButton pbPrint = createButton("Print", 'P');

	/** The Cancel <code>JButton</code>. */
	private JButton pbCancel = createButton("Cancel", 'n');

	/** Set <code>true</code> iff the print button is pressed. */
	private boolean fieldPressedPrint = false;

	/**
	 * Constructs a new <code>MFSRecvPartDialog</code>.
	 * @param parent the <code>MFSFrame</code> that caused this
	 *        <code>MFSRecvPartDialog</code> to be displayed
	 */
	public MFSRecvPartDialog(MFSFrame parent)
	{
		super(parent, "Reprint Recv Part Labels");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		createLayout();
		addMyListeners();
	}

	/** Adds this dialog's <code>Component</code>s to the layout. */
	protected void createLayout()
	{
		this.lstRecvPart.setFont(MFSConstants.MEDIUM_MONOSPACED_FONT);

		JLabel infoLabel = createLabel("* Use Ctrl Key for Multiple Selection",
				MFSConstants.SMALL_DIALOG_FONT);

		JPanel contentPane = new JPanel(new GridBagLayout());
		contentPane.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, 
				new Insets(0, 0, 0, 0), 0, 0);

		contentPane.add(infoLabel, gbc);

		gbc.gridy++;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.fill = GridBagConstraints.BOTH;
		contentPane.add(this.spRecvPart, gbc);

		gbc.gridy++;
		gbc.gridwidth = 1;
		gbc.weighty = 0.0;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(20, 0, 0, 0);
		contentPane.add(this.pbPrint, gbc);

		gbc.gridx = 1;
		contentPane.add(this.pbCancel, gbc);

		setContentPane(contentPane);
		setButtonDimensions(this.pbPrint, this.pbCancel);
	}

	/** Adds the listeners to this dialog's <code>Component</code>s. */
	protected void addMyListeners()
	{
		this.pbPrint.addActionListener(this);
		this.pbCancel.addActionListener(this);

		this.pbPrint.addKeyListener(this);
		this.pbCancel.addKeyListener(this);
		this.lstRecvPart.addKeyListener(this);
		this.spRecvPart.addKeyListener(this);
	}

	/**
	 * Returns the <code>JList</code> that displays the parts.
	 * @return the <code>JList</code> that displays the parts
	 */
	public JList getLSTRecvPart()
	{
		return this.lstRecvPart;
	}

	/**
	 * Returns <code>true</code> iff the print button was pressed.
	 * @return <code>true</code> iff the print button was pressed
	 */
	public boolean getPressedPrint()
	{
		return this.fieldPressedPrint;
	}

	/**
	 * Loads the recv part list model.
	 * @param data the data used to load the recv part list model
	 */
	public void loadRecvPartListModel(String data)
	{
		try
		{
			DefaultListModel model = new DefaultListModel();
			this.lstRecvPart.setModel(model);

			int start = 0;
			int end = 10;
			data += " "; //$NON-NLS-1$
			final int len = data.length();

			while (end <= len)
			{
				StringBuffer rowData = new StringBuffer();
				rowData.append("   "); //$NON-NLS-1$
				rowData.append(data.substring(start, start + 7));
				rowData.append("   "); //$NON-NLS-1$
				rowData.append(data.substring(start + 7, end));
				rowData.append("   "); //$NON-NLS-1$
				model.addElement(rowData.toString());
				start += 10;
				end += 10;
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
			if (source == this.pbPrint)
			{
				this.fieldPressedPrint = true;
				this.dispose();
			}
			else if (source == this.pbCancel)
			{
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
				this.pbPrint.requestFocusInWindow();
				this.pbPrint.doClick();
			}
		}
		else if (keyCode == KeyEvent.VK_ESCAPE)
		{
			this.dispose();
		}
		else if (keyCode == KeyEvent.VK_PAGE_DOWN)
		{
			MFSScroller.scrollDown(this.spRecvPart, this.lstRecvPart);
			ke.consume();
		}
		else if (keyCode == KeyEvent.VK_PAGE_UP)
		{
			MFSScroller.scrollUp(this.spRecvPart, this.lstRecvPart);
			ke.consume();
		}
	}

	/**
	 * Invoked the first time a window is made visible.
	 * <p>
	 * Requests the focus for {@link #lstRecvPart}.
	 * @param we the <code>WindowEvent</code>
	 */
	public void windowOpened(WindowEvent we)
	{
		try
		{
			if (we.getSource() == this)
			{
				this.lstRecvPart.setSelectedIndex(0);
				this.lstRecvPart.requestFocusInWindow();
			}
		}
		catch (Throwable throwable)
		{
			throwable.printStackTrace();
		}
	}
}
