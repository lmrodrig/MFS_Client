/* © Copyright IBM Corporation 2004, 2007. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2007-01-22      34242JR  R Prechel        -Java 5 version
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.dialog.rework;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.ibm.rchland.mfgapps.client.utils.messagebox.IGSMessageBox;
import com.ibm.rchland.mfgapps.mfsclient.MFSFrame;
import com.ibm.rchland.mfgapps.mfsclient.MFSListDialog;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSConstants;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSScroller;

/**
 * <code>MFSRwkCCDialog</code> displays the cause codes. The user can select a
 * single option and enter a comment.
 * @author The MFS Client Development Team
 */
public class MFSRwkCCDialog
	extends MFSListDialog
{
	private static final long serialVersionUID = 1L;
	/** The display string <code>JLabel</code>. */
	private JLabel lblDisplayString = null;

	/** The comment input <code>JTextField</code>. */
	private JTextField tfComment;

	/**
	 * Constructs a new <code>MFSRwkCCDialog</code>.
	 * @param parent the <code>MFSFrame</code> that caused this
	 *        <code>MFSRwkCCDialog</code> to be displayed
	 * @param displayString the <code>String</code> displayed by a
	 *        <code>JLabel</code> at the top of the dialog
	 */
	public MFSRwkCCDialog(MFSFrame parent, String displayString)
	{
		super(parent, "Cause Codes");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.lblDisplayString = createLabel(displayString);
		this.pbProceed = createButton("F2 = Log Part");
		createLayout();
		addMyListeners();

	}

	/** Adds this dialog's <code>Component</code>s to the layout. */
	protected void createLayout()
	{
		this.lblDisplayString.setHorizontalAlignment(SwingConstants.CENTER);

		JLabel commentLabel = createLabel("Enter Comment");
		this.tfComment = createTextField(MFSConstants.LARGE_TF_COLS, 0, commentLabel);

		JPanel contentPane = new JPanel(new GridBagLayout());
		contentPane.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, 
				new Insets(0, 0, 0, 0), 0, 0);

		contentPane.add(this.lblDisplayString, gbc);

		gbc.gridy++;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(20, 0, 0, 0);
		contentPane.add(this.spOptions, gbc);

		gbc.gridy++;
		gbc.weighty = 0.0;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.NONE;
		contentPane.add(commentLabel, gbc);

		gbc.gridy++;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(0, 0, 0, 0);
		contentPane.add(this.tfComment, gbc);

		gbc.gridy++;
		gbc.gridwidth = 1;
		gbc.weighty = 0.0;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(20, 0, 0, 0);
		contentPane.add(this.pbProceed, gbc);

		gbc.gridx = 1;
		contentPane.add(this.pbCancel, gbc);

		setContentPane(contentPane);
		setButtonDimensions(this.pbProceed, this.pbCancel);
	}

	/** Adds the listeners to this dialog's <code>Component</code>s. */
	protected void addMyListeners()
	{
		super.addMyListeners();
		this.tfComment.addKeyListener(this);
	}

	/**
	 * Returns the text entered in the comment <code>JTextField</code>.
	 * @return the text entered in the comment <code>JTextField</code>
	 */
	public String getCommentText()
	{
		return this.tfComment.getText();
	}

	/**
	 * Loads the Cause Code list model using the specified data.
	 * @param data the data used to load the Cause Code list model
	 * @param key the key used to filter the data elements. The key is matched
	 *        against the first three characters of each data element.
	 */
	public void loadRwkCCListModel(String data, String key)
	{
		try
		{
			int start = 0;
			int end = 64;
			final int len = data.length();

			DefaultListModel listModel = new DefaultListModel();
			this.lstOptions.setModel(listModel);

			while (end < len)
			{
				String curr = data.substring(start, end);
				if (curr.substring(0, 3).equals(key))
				{
					listModel.addElement(curr.substring(3));
				}
				start += 64;
				end += 64;
			}

			String curr = data.substring(start);
			if (curr.substring(0, 3).equals(key))
			{
				listModel.addElement(curr.substring(3));
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
			else if (source == this.pbProceed)
			{
				this.pbProceed.requestFocusInWindow();
				this.pbProceed.doClick();
			}
			else if (source == this.lstOptions)
			{
				this.tfComment.requestFocusInWindow();
			}
		}
		else if (keyCode == KeyEvent.VK_F2)
		{
			this.pbProceed.requestFocusInWindow();
			this.pbProceed.doClick();
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
