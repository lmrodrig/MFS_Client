/* @ Copyright IBM Corporation 2005, 2007. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2007-02-22      34242JR  R Prechel        -Java 5 version
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
import javax.swing.SwingConstants;

import com.ibm.rchland.mfgapps.client.utils.messagebox.IGSMessageBox;
import com.ibm.rchland.mfgapps.mfsclient.MFSDialog;
import com.ibm.rchland.mfgapps.mfsclient.MFSFrame;
import com.ibm.rchland.mfgapps.mfsclient.struct.MFSComponentListModel;
import com.ibm.rchland.mfgapps.mfsclient.struct.MFSComponentRec;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSConstants;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSPrintingMethods;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSScroller;

/**
 * <code>MFSTracksubReprintDialog</code> is the <code>MFSDialog</code> used
 * to reprint tracksub3 labels.
 * @author The MFS Client Development Team
 */
public class MFSTracksubReprintDialog
	extends MFSDialog
{
	private static final long serialVersionUID = 1L;
	/** The <code>JList</code> that displays the parts. */
	private JList lstPart = new JList();

	/** The <code>JScrollPane</code> that contains the list of parts. */
	private JScrollPane spPart = new JScrollPane(this.lstPart,
			ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
			ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

	/** The reprint <code>JButton</code>. */
	private JButton pbReprint = createButton("Reprint", 'R');

	/** The cancel <code>JButton</code>. */
	private JButton pbCancel = createButton("Cancel", 'n');

	/** Stores the <code>MFSComponentRec</code> displayed in the list. */
	private MFSComponentListModel fieldReprintCompListModel = new MFSComponentListModel();

	/**
	 * Constructs a new <code>MFSTracksubReprintDialog</code>.
	 * @param parent the <code>MFSFrame</code> that caused this
	 *        <code>MFSTracksubReprintDialog</code> to be displayed
	 */
	public MFSTracksubReprintDialog(MFSFrame parent)
	{
		super(parent, "Reprint MES MFI Labels");
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		createLayout();
		addMyListeners();
	}

	/** Adds this dialog's <code>Component</code>s to the layout. */
	protected void createLayout()
	{
		this.lstPart.setFont(MFSConstants.MEDIUM_MONOSPACED_FONT);

		JLabel instruction1 = createJLabel("Select a part from the list and press the reprint button.");
		JLabel instruction2 = createJLabel("Multiple parts can be selected by holding the SHIFT key");
		JLabel instruction3 = createJLabel("while selecting parts from the list.");

		JLabel infoLabel = createJLabel("Part Number   Part Description          Country");

		JPanel contentPane = new JPanel(new GridBagLayout());
		contentPane.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, 
				new Insets(0, 0, 0, 0), 0, 0);

		contentPane.add(instruction1, gbc);
		gbc.gridy++;
		contentPane.add(instruction2, gbc);
		gbc.gridy++;
		contentPane.add(instruction3, gbc);

		gbc.gridy++;
		gbc.insets = new Insets(20, 0, 0, 0);
		contentPane.add(infoLabel, gbc);

		gbc.gridy++;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(0, 0, 0, 0);
		contentPane.add(this.spPart, gbc);

		gbc.gridy++;
		gbc.gridwidth = 1;
		gbc.weighty = 0.0;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(20, 0, 0, 0);
		contentPane.add(this.pbReprint, gbc);

		gbc.gridx = 1;
		contentPane.add(this.pbCancel, gbc);

		setContentPane(contentPane);
		setButtonDimensions(this.pbReprint, this.pbCancel);
	}

	/**
	 * Creates a new <code>JLabel</code>.
	 * @param text the text for the <code>JLabel</code>
	 * @return the new <code>JLabel</code>
	 */
	private JLabel createJLabel(String text)
	{
		JLabel result = new JLabel(text, SwingConstants.LEADING);
		result.setForeground(MFSConstants.PRIMARY_FOREGROUND_COLOR);
		result.setFont(MFSConstants.MEDIUM_MONOSPACED_FONT);
		return result;
	}

	/** Adds the listeners to this dialog's <code>Component</code>s. */
	protected void addMyListeners()
	{
		this.pbReprint.addActionListener(this);
		this.pbCancel.addActionListener(this);

		this.pbReprint.addKeyListener(this);
		this.pbCancel.addKeyListener(this);
		this.lstPart.addKeyListener(this);
		this.spPart.addKeyListener(this);
	}

	/**
	 * Returns the reprint <code>MFSComponentListModel</code>.
	 * @return the reprint <code>MFSComponentListModel</code>
	 */
	public MFSComponentListModel getReprintCompListModel()
	{
		return this.fieldReprintCompListModel;
	}

	/**
	 * Loads the <code>ListModel</code> for {@link #lstPart} using the
	 * <code>MFSComponentRec</code>s in {@link #fieldReprintCompListModel}.
	 */
	public void loadReprintListModel()
	{
		/*
		 * The reprint MFSComponentListModel was filled in with reprintable
		 * components. Use the components in the reprint MFSComponentListModel
		 * to populate a DefaultListModel for the JList.
		 */
		DefaultListModel model = new DefaultListModel();
		this.lstPart.setModel(model);

		MFSComponentRec compRec;
		for (int index = 0; index < getReprintCompListModel().size(); index++)
		{
			compRec = getReprintCompListModel().getComponentRecAt(index);
			StringBuffer displayString = new StringBuffer(42);
			displayString.append(compRec.getInpn());
			displayString.append("  "); //$NON-NLS-1$
			displayString.append(compRec.getCdes());
			displayString.append("  "); //$NON-NLS-1$
			displayString.append(compRec.getCooc());
			model.addElement(displayString.toString());
		}
		pack();
	}

	/** Performs a reprint for the selected components. */
	public void reprint()
	{
		try
		{
			MFSComponentRec comp_rec;
			int[] indices_select = this.lstPart.getSelectedIndices();

			if (indices_select.length != 0)
			{
				for (int i = 0; i < indices_select.length; i++)
				{
					comp_rec = getReprintCompListModel().getComponentRecAt(indices_select[i]);
					MFSPrintingMethods.tracksub3(comp_rec.getMctl(), comp_rec.getCrct(),
							comp_rec.getInpn(), comp_rec.getCooc(), 1, getParentFrame());

				}
			}
			else
			{
				String erms = "Please select a part before pressing the REPRINT button";
				IGSMessageBox.showOkMB(this, null, erms, null);
			}
			this.lstPart.clearSelection();

		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(this, null, null, e);
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
			if (source == this.pbReprint)
			{
				reprint();
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
				this.pbReprint.requestFocusInWindow();
				this.pbReprint.doClick();
			}
		}
		else if (keyCode == KeyEvent.VK_ESCAPE)
		{
			this.pbCancel.requestFocusInWindow();
			this.pbCancel.doClick();
		}
		else if (keyCode == KeyEvent.VK_PAGE_DOWN)
		{
			MFSScroller.scrollDown(this.spPart, this.lstPart);
			ke.consume();
		}
		else if (keyCode == KeyEvent.VK_PAGE_UP)
		{
			MFSScroller.scrollUp(this.spPart, this.lstPart);
			ke.consume();
		}
	}

	/**
	 * Invoked the first time a window is made visible.
	 * <p>
	 * Requests the focus for {@link #lstPart}.
	 * @param we the <code>WindowEvent</code>
	 */
	public void windowOpened(WindowEvent we)
	{
		try
		{
			if (we.getSource() == this)
			{
				this.lstPart.requestFocusInWindow();
			}
		}
		catch (Throwable throwable)
		{
			throwable.printStackTrace();
		}
	}
}
