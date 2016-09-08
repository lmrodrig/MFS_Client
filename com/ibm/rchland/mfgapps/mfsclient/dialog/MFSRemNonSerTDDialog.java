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

import java.awt.Component;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;

import java.util.List;
import java.util.Vector;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;

import com.ibm.rchland.mfgapps.client.utils.messagebox.IGSMessageBox;
import com.ibm.rchland.mfgapps.mfsclient.MFSFrame;
import com.ibm.rchland.mfgapps.mfsclient.MFSListDialog;
import com.ibm.rchland.mfgapps.mfsclient.struct.MFSComponentRec;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSConstants;

/**
 * <code>MFSRemNonSerTDDialog</code> is the <code>MFSListDialog</code> used to
 * Show Non-serialized parts to remove.
 * @author The MFS Client Development Team
 */
public class MFSRemNonSerTDDialog
	extends MFSListDialog
	implements ListCellRenderer
{
	private static final long serialVersionUID = 1L;
	/** The <code>DefaultListModel</code> of <code>MFSComponentRec</code>. */
	private DefaultListModel lmPartslist = new DefaultListModel();
	
	/** The <code>ListCellRenderer</code> used by the <code>JList</code>. */
	private DefaultListCellRenderer renderer = new DefaultListCellRenderer();

	/**
	 * Constructs a new <code>MFSRemNonSerTDDialog</code>.
	 * @param parent the <code>MFSFrame</code> that caused this
	 *        <code>MFSRemNonSerTDDialog</code> to be displayed
	 */
	public MFSRemNonSerTDDialog(MFSFrame parent)
	{
		super(parent, "Select parts to remove");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.lstOptions.setModel(this.lmPartslist);
		this.lstOptions.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		this.lstOptions.setCellRenderer(this);

		JLabel headerLabel = new JLabel(" Crct  Part Number   Description               FQty  ");
		headerLabel.setFont(MFSConstants.MEDIUM_MONOSPACED_FONT);
		this.spOptions.setColumnHeaderView(headerLabel);
		this.pbProceed.setText("Remove");
		
		createLayout();
		addMyListeners();
	}
	/**
	 * Adds the <code>MFSComponentRec</code> into the list model.
	 * @param compRec the <code>MFSComponentRec</code>.
	 */
	public void addComponentRec(MFSComponentRec compRec)
	{
		this.lmPartslist.addElement(compRec);
	}
	/**
	 * Retrieves the selected <code>MFSComponentRec</code>s in a list.
	 * @return <code>List</code>.
	 */	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List getSelectedComponents()
	{
		List result = new Vector();
		int[] selected = this.lstOptions.getSelectedIndices();
		for(int i = 0; i < selected.length; i++)
		{
			result.add(this.lmPartslist.get(selected[i]));
		}
		return result;
	}
	/** {@inheritDoc} */
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
	{
		MFSComponentRec compRec = (MFSComponentRec)value;
		StringBuffer text = new StringBuffer();
		text.append(" ");
		text.append(compRec.getCrct());
		text.append("  ");
		text.append(compRec.getInpn());
		text.append("  ");
		text.append(compRec.getCdes());
		text.append("  ");
		text.append(compRec.getFqty());
		text.append(" ");
		return this.renderer.getListCellRendererComponent(list, text, index,
				isSelected, cellHasFocus);
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
			if (source == this.pbProceed)
			{
				if(this.lstOptions.isSelectionEmpty())
				{
					IGSMessageBox.showOkMB(getParentFrame(), "Entry Error", 
							"No parts to remove were selected.", null);
				}
				else
				{
					this.fieldProceedSelected = true;
					this.dispose();
				}
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
	 * Invoked the first time a window is made visible.
	 * <p>
	 * Sets selected of {@link #lstOptions} to all and requests the
	 * focus for {@link #lstOptions}.
	 * @param we the <code>WindowEvent</code>
	 */
	public void windowOpened(WindowEvent we)
	{
		try
		{
			if (we.getSource() == this)
			{
				this.lstOptions.setSelectionInterval(0, 
						this.lmPartslist.getSize()-1);
				this.lstOptions.requestFocusInWindow();
			}

		}
		catch (Throwable throwable)
		{
			throwable.printStackTrace();
		}
	}
}