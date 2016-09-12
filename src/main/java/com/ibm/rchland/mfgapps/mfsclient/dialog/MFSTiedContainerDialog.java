/* @ Copyright IBM Corporation 2008. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2008-03-17      37616JL  R Prechel        -Initial version
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.dialog;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import com.ibm.rchland.mfgapps.client.utils.messagebox.IGSMessageBox;
import com.ibm.rchland.mfgapps.client.utils.xml.IGSXMLDocument;
import com.ibm.rchland.mfgapps.mfsclient.MFSDialog;
import com.ibm.rchland.mfgapps.mfsclient.panel.MFSEntityMergePanel;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSScroller;

/**
 * <code>MFSTiedContainerDialog</code> is the <code>MFSDialog</code> used to
 * display the list of tied containers.
 * @author The MFS Client Development Team
 */
public class MFSTiedContainerDialog
	extends MFSDialog
{
	private static final long serialVersionUID = 1L;
	/** The <code>JList</code> that displays the tied containers. */
	protected JList lstOptions = createList();

	/** The <code>JScrollPane</code> that contains the list. */
	protected JScrollPane spOptions = new JScrollPane(this.lstOptions,
			ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
			ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

	/**
	 * The <code>JButton</code> used to set the selected tied container as the
	 * parent container in the <code>MFSEntityMergePanel</code>.
	 */
	protected JButton pbFind = createButton("Find", 'S');

	/** The <code>JButton</code> used to close the dialog. */
	protected JButton pbClose = createButton("Close", 'C');

	/**
	 * The <code>MFSEntityMergePanel</code> using this
	 * <code>MFSTiedContainerDialog</code>.
	 */
	private final MFSEntityMergePanel fieldPanel;

	/**
	 * Constructs a new <code>MFSTiedContainerDialog</code>.
	 * @param panel the <code>MFSEntityMergePanel</code> using this
	 *        <code>MFSTiedContainerDialog</code>
	 * @param doc the <code>IGSXMLDocument</code> with the tied containers
	 */
	public MFSTiedContainerDialog(MFSEntityMergePanel panel, IGSXMLDocument doc)
	{
		super(panel.getParentFrame(), "Tied Containers");
		this.fieldPanel = panel;
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		DefaultListModel model = new DefaultListModel();
		while (doc.stepIntoElement("RCD") != null) //$NON-NLS-1$
		{
			model.addElement(doc.getNextElement("CNTR")); //$NON-NLS-1$
			doc.stepOutOfElement();
		}
		this.lstOptions.setModel(model);
		createLayout();
		addMyListeners();
		pack();
	}

	/** Adds this dialog's <code>Component</code>s to the layout. */
	protected void createLayout()
	{
		JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 20, 0));
		buttonPanel.add(this.pbFind);
		buttonPanel.add(this.pbClose);
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

		JPanel contentPane = new JPanel(null);
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.PAGE_AXIS));
		contentPane.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

		contentPane.add(this.spOptions);
		contentPane.add(buttonPanel);

		setContentPane(contentPane);
	}

	/** Adds the listeners to this dialog's <code>Component</code>s. */
	protected void addMyListeners()
	{
		this.pbFind.addActionListener(this);
		this.pbClose.addActionListener(this);

		this.pbFind.addKeyListener(this);
		this.pbClose.addKeyListener(this);
		this.lstOptions.addKeyListener(this);
		this.spOptions.addKeyListener(this);
	}

	/**
	 * Invoked when an action occurs.
	 * @param ae the <code>ActionEvent</code>
	 */
	public void actionPerformed(ActionEvent ae)
	{
		final Object source = ae.getSource();
		if (source == this.pbClose)
		{
			this.dispose();
		}
		else if (source == this.pbFind)
		{
			String selectedOption = (String) this.lstOptions.getSelectedValue();
			if (selectedOption != null)
			{
				this.fieldPanel.findNode(selectedOption);
			}
			else
			{
				String title = "Selection Error";
				String emsg = "You must select a value.";
				IGSMessageBox.showOkMB(getParentFrame(), title, emsg, null);
			}
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
			if (source == this.pbClose)
			{
				this.pbClose.requestFocusInWindow();
				this.pbClose.doClick();
			}
			else
			{
				this.pbFind.requestFocusInWindow();
				this.pbFind.doClick();
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
}
