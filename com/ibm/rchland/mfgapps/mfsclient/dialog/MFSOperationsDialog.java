/* © Copyright IBM Corporation 2005, 2007. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2007-01-28      34242JR  R Prechel        -Java 5 version
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.dialog;

import java.awt.GridLayout;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JPanel;
import javax.swing.border.Border;

import mfsxml.MfsXMLParser;

import com.ibm.rchland.mfgapps.client.utils.messagebox.IGSMessageBox;
import com.ibm.rchland.mfgapps.mfsclient.MFSFrame;
import com.ibm.rchland.mfgapps.mfsclient.MFSListDialog;

/**
 * <code>MFSOperationsDialog</code> is the <code>MFSListDialog</code> used
 * to select an operation.
 * @author The MFS Client Development Team
 */
public class MFSOperationsDialog
	extends MFSListDialog
{
	private static final long serialVersionUID = 1L;
	/**
	 * Constructs a new <code>MFSOperationsDialog</code>.
	 * @param parent the <code>MFSFrame</code> that caused this
	 *        <code>MFSOperationsDialog</code> to be displayed
	 */
	public MFSOperationsDialog(MFSFrame parent)
	{
		super(parent, "Operations");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		createLayout();
		this.addMyListeners();
	}

	/** Adds this dialog's <code>Component</code>s to the layout. */
	protected void createLayout()
	{
		this.lstOptions.setVisibleRowCount(5);

		JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 20, 0));
		buttonPanel.add(this.pbProceed);
		buttonPanel.add(this.pbCancel);
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		JPanel contentPane = new JPanel(null);
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));

		Border border = BorderFactory.createEmptyBorder(30, 40, 0, 40);
		this.spOptions.setBorder(BorderFactory.createCompoundBorder(border,
				this.spOptions.getBorder()));
		contentPane.add(this.spOptions);
		contentPane.add(buttonPanel);

		this.setContentPane(contentPane);
		this.pack();
	}

	/**
	 * Loads the operations list using the specified data.
	 * @param data the data used to load the operations list
	 * @param curr_nmbr the current operation number. It is not added to the
	 *        list of operation numbers.
	 */
	public void loadOperListModel(String data, String curr_nmbr)
	{
		try
		{
			DefaultListModel model = new DefaultListModel();
			this.lstOptions.setModel(model);

			//Process XML data
			if (data.indexOf("<") != -1) //$NON-NLS-1$
			{
				MfsXMLParser xmlParser = new MfsXMLParser(data);

				String tempNmbr = xmlParser.getNextField("NMBR"); //$NON-NLS-1$

				while (tempNmbr.length() != 0)
				{
					if (!tempNmbr.equals("TEAR") && //$NON-NLS-1$
							!tempNmbr.equals("DCFG") && //$NON-NLS-1$
							!tempNmbr.equals(curr_nmbr))
					{
						model.addElement(tempNmbr);
					}

					tempNmbr = xmlParser.getNextField("NMBR"); //$NON-NLS-1$
				}
			}
			//Process non-XML data
			else
			{
				int start = 0;
				int end = 4;
				final int len = data.length();

				while (end < len)
				{
					String op = data.substring(start, end);
					if (!op.equals(curr_nmbr))
					{
						model.addElement(op);
					}
					start += 4;
					end += 4;
				}

				if (len > 0)
				{
					String op = data.substring(start);
					if (!op.equals(curr_nmbr))
					{
						model.addElement(op);
					}
				}
			}//end of non XML data
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
	 * Returns the length of the list.
	 * @return the length of the list
	 */
	public int getListSize()
	{
		return this.lstOptions.getModel().getSize();
	}

	/**
	 * Returns the selected index of the list; returns -1 if there is no
	 * selected index.
	 * @return the selected index of the list
	 */
	public int getSelectedIndex()
	{
		return this.lstOptions.getSelectedIndex();
	}

	/**
	 * Sets the selected index of the list.
	 * @param index the index to select
	 */
	public void setSelectedIndex(int index)
	{
		this.lstOptions.setSelectedIndex(index);
	}

	/**
	 * Invoked the first time a window is made visible.
	 * <p>
	 * Requests the focus for {@link #lstOptions}.
	 * @param we the <code>WindowEvent</code>
	 */
	public void windowOpened(WindowEvent we)
	{
		if (we.getSource() == this)
		{
			this.lstOptions.requestFocusInWindow();
		}
	}
}
