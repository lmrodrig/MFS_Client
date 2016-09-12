/* @ Copyright IBM Corporation 2005, 2007. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2007-01-25      34242JR  R Prechel        -Java 5 version
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.dialog;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import com.ibm.rchland.mfgapps.mfsclient.MFSDialog;
import com.ibm.rchland.mfgapps.mfsclient.MFSFrame;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSConstants;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSScroller;
import com.ibm.rchland.mfgapps.mfscommon.MFSConfig;

/**
 * <code>MFSKittingOpDialog</code> is an <code>MFSDialog</code> that allows
 * the user to select the type of kitting report to print for each operation.
 * @author The MFS Client Development Team
 */
public class MFSKittingOpDialog
	extends MFSDialog
{
	private static final long serialVersionUID = 1L;
	/** The <code>JList</code> that displays the kitting options. */
	protected JList lstKittOp = createList();

	/** The <code>JScrollPane</code> that contains the list of options. */
	protected JScrollPane spKittOp = new JScrollPane(this.lstKittOp,
			ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
			ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
	
	/** The print <code>JButton</code>. */
	private JButton pbPrint = createButton("Print", 'P');

	/** The cancel <code>JButton</code>. */
	private JButton pbCancel = createButton("Cancel", 'n');
	
	/** Set <code>true</code> iff the print button is pressed. */
	private boolean fieldPressedPrint = false;
	
	/**
	 * Constructs a new <code>MFSKittingOpDialog</code>.
	 * @param parent the <code>MFSFrame</code> that caused this
	 *        <code>MFSKittingOpDialog</code> to be displayed
	 */
	public MFSKittingOpDialog(MFSFrame parent)
	{
		super(parent, "Kitting Operations");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		createLayout();
		addMyListeners();
	}
	
	/** Adds this dialog's <code>Component</code>s to the layout. */
	private void createLayout()
	{
		this.lstKittOp.setVisibleRowCount(6);
		
		JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 30, 0));
		buttonPanel.add(this.pbPrint);
		buttonPanel.add(this.pbCancel);
		
		JPanel contentPane = new JPanel(new GridBagLayout());
		contentPane.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 6, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(10, 10, 10, 10), 0, 0);
		
		contentPane.add(this.spKittOp, gbc);
		
		gbc.gridx = 1;
		gbc.gridheight = 1;
		gbc.weightx = 0.0;
		gbc.weighty = 0.0;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.insets = new Insets(10, 0, 20, 10);
		contentPane.add(createLabel("Toggle Switches", MFSConstants.SMALL_DIALOG_FONT), gbc);
		
		gbc.gridy++;
		gbc.insets = new Insets(0, 0, 5, 10);
		contentPane.add(createLabel("D = Detail", MFSConstants.SMALL_DIALOG_FONT), gbc);

		gbc.gridy++;
		contentPane.add(createLabel("S = Summary", MFSConstants.SMALL_DIALOG_FONT), gbc);
		
		gbc.gridy++;
		contentPane.add(createLabel("B = Both", MFSConstants.SMALL_DIALOG_FONT), gbc);
		
		gbc.gridy++;
		contentPane.add(createLabel("Space Bar = Clear", MFSConstants.SMALL_DIALOG_FONT), gbc);
		
		gbc.gridy++;
		gbc.weighty = 1.0;
		contentPane.add(Box.createVerticalGlue(), gbc);
		
		gbc.gridx = 0;
		gbc.gridy++;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.NONE;
		
		contentPane.add(buttonPanel, gbc);
		
		setContentPane(contentPane);
	}

	/** Adds the listeners to this dialog's <code>Component</code>s. */
	private void addMyListeners()
	{
		this.pbPrint.addActionListener(this);
		this.pbCancel.addActionListener(this);
		
		this.pbPrint.addKeyListener(this);
		this.pbCancel.addKeyListener(this);
		this.lstKittOp.addKeyListener(this);
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
	 * Returns the size of the Kitt Op <code>ListModel</code>.
	 * @return the size of the Kitt Op <code>ListModel</code>
	 */
	public int getKittOpListModelSize()
	{
		return this.lstKittOp.getModel().getSize();
	}
	
	/**
	 * Returns the element at the specified <code>index</code> in the Kitt Op
	 * <code>ListModel</code>.
	 * @param index the index of the element to return
	 * @return the element at the specified index
	 */
	public Object getKittOpListModelElementAt(int index)
	{
		return this.lstKittOp.getModel().getElementAt(index);
	}
	
	/**
	 * Loads the Kitt Op list model using the specified data.
	 * @param data the data used to load the Kitt Op list model
	 */
	public void loadKittOpListModel(String data) 
	{
		int start = 0;
		int end = 4;
		data += " "; //$NON-NLS-1$
		final int len = data.length();
		MFSConfig config = MFSConfig.getInstance();
		
		DefaultListModel listModel = new DefaultListModel();
		this.lstKittOp.setModel(listModel);
		
		while (end <= len)
		{
			String nmbr = data.substring(start,end);
			String type = " "; //$NON-NLS-1$
			String value = config.getConfigValue("KITTDFLT," + nmbr); //$NON-NLS-1$
			if (!value.equals(MFSConfig.NOT_FOUND))
			{
				value += " "; //$NON-NLS-1$
				type = value.substring(0,1);
			}
			listModel.addElement(nmbr + "    |    " + type + " ");
			start += 4;
			end += 4;
		}
		
		pack();
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
			if(source == this.pbPrint)
			{
				this.fieldPressedPrint = true;
				dispose();
			}
			else if (source == this.pbCancel)
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
		final Object source = ke.getSource();
		if(source == this.lstKittOp)
		{
			if(keyCode == KeyEvent.VK_D)
			{
				String newStr = (String)this.lstKittOp.getSelectedValue();
				newStr = newStr.substring(0, 13) + "D ";
				((DefaultListModel)this.lstKittOp.getModel()).setElementAt(newStr, this.lstKittOp.getSelectedIndex());
			}
			else if(keyCode == KeyEvent.VK_S)
			{
				String newStr = (String)this.lstKittOp.getSelectedValue();
				newStr = newStr.substring(0, 13) + "S ";
				((DefaultListModel)this.lstKittOp.getModel()).setElementAt(newStr, this.lstKittOp.getSelectedIndex());
			}
			else if(keyCode == KeyEvent.VK_B)
			{
				String newStr = (String)this.lstKittOp.getSelectedValue();
				newStr = newStr.substring(0, 13) + "B ";
				((DefaultListModel)this.lstKittOp.getModel()).setElementAt(newStr, this.lstKittOp.getSelectedIndex());
			}
			else if(keyCode == KeyEvent.VK_SPACE)
			{
				String newStr = (String)this.lstKittOp.getSelectedValue();
				newStr = newStr.substring(0, 13) + "  ";
				((DefaultListModel)this.lstKittOp.getModel()).setElementAt(newStr, this.lstKittOp.getSelectedIndex());
			}
		}
		
		if(keyCode == KeyEvent.VK_ENTER)
		{
			if(source == this.pbCancel)
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
		else if(keyCode == KeyEvent.VK_PAGE_DOWN)
		{
			MFSScroller.scrollDown(this.spKittOp, this.lstKittOp);
			ke.consume();
		}
		else if(keyCode == KeyEvent.VK_PAGE_UP)
		{
			MFSScroller.scrollUp(this.spKittOp, this.lstKittOp);
			ke.consume();
		}
	}
	
	/**
	 * Invoked the first time a window is made visible.
	 * <p>
	 * Sets the selected index of {@link #lstKittOp} to 0 and requests the
	 * focus for {@link #lstKittOp}.
	 * @param we the <code>WindowEvent</code>
	 */
	public void windowOpened(WindowEvent we)
	{
		if (we.getSource() == this)
		{
			this.lstKittOp.setSelectedIndex(0);
			this.lstKittOp.requestFocusInWindow();
		}
	}
}
