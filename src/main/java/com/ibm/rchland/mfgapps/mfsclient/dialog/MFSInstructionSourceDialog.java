/* @ Copyright IBM Corporation 2005, 2007. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2007-02-16      34242JR  R Prechel        -Java 5 version
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.dialog;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import mfsxml.MISSING_XML_TAG_EXCEPTION;
import mfsxml.MfsXMLParser;

import com.ibm.rchland.mfgapps.mfsclient.MFSDialog;
import com.ibm.rchland.mfgapps.mfsclient.MFSFrame;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSConstants;

/**
 * <code>MFSInstructionSourceDialog</code> displays source information for an
 * Interactive Routing instruction.
 * @author The MFS Client Development Team
 */
public class MFSInstructionSourceDialog
	extends MFSDialog
{
	private static final long serialVersionUID = 1L;
	/** The suffix value label. */
	private JLabel vlSuffix = null;

	/** The sequence value label. */
	private JLabel vlSequence = null;

	/** The subsequence information <code>JTextArea</code>. */
	private JTextArea taSubsequence = createDisplayTextArea();

	/** The subsequence information <code>JScrollPane</code>. */
	private JScrollPane spSubsequence = new JScrollPane(this.taSubsequence);

	/** The details information <code>JTextArea</code>. */
	private JTextArea taDetail = createDisplayTextArea();

	/** The details information <code>JScrollPane</code>. */
	private JScrollPane spDetail = new JScrollPane(this.taDetail);

	/** The ok <code>JButton</code>. */
	private JButton pbOkay = createButton("OK", 'O');

	/**
	 * Constructs a new <code>MFSInstructionSourceDialog</code>.
	 * @param parent the <code>MFSFrame</code> that caused this
	 *        <code>MFSInstructionSourceDialog</code> to be displayed
	 */
	public MFSInstructionSourceDialog(MFSFrame parent)
	{
		super(parent, "Instruction Source");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setModal(false);
		createLayout();
		addMyListeners();
	}
	
	/** Adds this dialog's <code>Component</code>s to the layout. */
	private void createLayout()
	{
		JLabel suffixNameLabel = createSmallNameLabel("SUFFIX:");
		this.vlSuffix = createSmallValueLabel(suffixNameLabel);
		
		JLabel sequenceNameLabel = createSmallNameLabel("SEQUENCE:");
		this.vlSequence = createSmallValueLabel(sequenceNameLabel);

		JPanel suffixSequencePanel = new JPanel(new GridLayout(2, 2, 5, 5));
		suffixSequencePanel.add(suffixNameLabel);
		suffixSequencePanel.add(this.vlSuffix);
		suffixSequencePanel.add(sequenceNameLabel);
		suffixSequencePanel.add(this.vlSequence);
		
		JPanel contentPane = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 2, 1, 1,
				GridBagConstraints.CENTER, GridBagConstraints.NONE,
				new Insets(4, 4, 4, 4), 0, 0);
		
		contentPane.add(suffixSequencePanel, gbc);

		gbc.gridx = 1;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.gridheight = 1;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(20, 20, 0, 20);

		contentPane.add(createLabel("Sub Sequences"), gbc);

		gbc.gridy++;
		gbc.insets = new Insets(0, 20, 0, 20);

		this.spSubsequence.setPreferredSize(new Dimension(100, 60));
		contentPane.add(this.spSubsequence, gbc);

		gbc.gridx = 0;
		gbc.gridy++;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(10, 10, 0, 0);

		contentPane.add(createLabel("Details"), gbc);

		gbc.gridy++;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(0, 10, 0, 10);

		this.spDetail.setPreferredSize(new Dimension(300, 100));
		contentPane.add(this.spDetail, gbc);

		gbc.gridy++;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(10, 0, 10, 0);

		contentPane.add(this.pbOkay, gbc);

		setContentPane(contentPane);
		pack();
	}
	
	/** Adds the listeners to this dialog's <code>Component</code>s. */
	private void addMyListeners()
	{
		this.pbOkay.addActionListener(this);
		this.pbOkay.addKeyListener(this);
	}
	
	/**
	 * Creates a <code>JTextArea</code>.
	 * @return the <code>JTextArea</code>
	 */
	private JTextArea createDisplayTextArea()
	{
		JTextArea result = new JTextArea();
		result.setEditable(false);
		result.setFont(MFSConstants.MEDIUM_MONOSPACED_FONT);
		return result;
	}
	
	/**
	 * Sets the text of the suffix and sequence <code>JLabel</code>s.
	 * @param suffix the text for the suffix <code>JLabel</code>
	 * @param sequence the text for the sequence <code>JLabel</code>
	 */
	public void setLabelText(String suffix, String sequence)
	{
		this.vlSuffix.setText(suffix);
		this.vlSequence.setText(sequence);
	}

	/**
	 * Populates the subsequence and detail <code>JTextArea</code>s.
	 * @param parser an <code>MfsXMLParser</code> containing the XML used to
	 *        populate the <code>JTextArea</code>s
	 */
	public void loadUpDialog(MfsXMLParser parser)
	{
		String tempSSQN = ""; //$NON-NLS-1$
		String tempRCD = ""; //$NON-NLS-1$

		//first fill in the sub sequence text area
		try
		{
			tempSSQN = parser.getField("SSQN"); //$NON-NLS-1$
		}
		catch (MISSING_XML_TAG_EXCEPTION e)
		{
			tempSSQN = ""; //$NON-NLS-1$
		}

		//If there is an SSQN in the parser,
		//add the SS recs to the text area
		if (tempSSQN.length() > 0)
		{
			String tempss = ""; //$NON-NLS-1$
			MfsXMLParser xmlParser1 = new MfsXMLParser(tempSSQN);

			try
			{
				tempss = xmlParser1.getField("SS"); //$NON-NLS-1$
			}
			catch (MISSING_XML_TAG_EXCEPTION e)
			{
				tempss = ""; //$NON-NLS-1$
				this.taSubsequence.setText("N/A");
			}
			while (!tempss.equals("")) //$NON-NLS-1$
			{
				this.taSubsequence.setText(this.taSubsequence.getText() + tempss + "\n"); //$NON-NLS-1$
				try
				{
					tempss = xmlParser1.getNextField("SS"); //$NON-NLS-1$
				}
				catch (mfsxml.MISSING_XML_TAG_EXCEPTION e)
				{
					tempss = ""; //$NON-NLS-1$
				}
			}
		}
		else
		{
			this.taSubsequence.setText("N/A");
		}

		//now fill up the details text area
		try
		{
			tempRCD = parser.getField("RCD"); //$NON-NLS-1$
		}
		catch (mfsxml.MISSING_XML_TAG_EXCEPTION e)
		{
			tempRCD = ""; //$NON-NLS-1$
		}

		if (tempRCD.length() == 0)
		{
			this.taDetail.setText("NO Source?");
		}
		else
		{
			StringBuffer details = new StringBuffer(110);
			while (tempRCD.length() > 0)
			{
				MfsXMLParser xmlParser1 = new MfsXMLParser(tempRCD);

				try
				{
					details.append("BM=");
					details.append(xmlParser1.getField("BMNM").trim()); //$NON-NLS-1$
				}
				catch (MISSING_XML_TAG_EXCEPTION e)
				{
					details.append("NO BMNM?");
				}

				try
				{
					details.append(" EC=");
					details.append(xmlParser1.getField("BMEC").trim()); //$NON-NLS-1$
				}
				catch (MISSING_XML_TAG_EXCEPTION e)
				{
					details.append("NO BMEC?");
				}

				try
				{
					details.append("\nITEM=");
					details.append(xmlParser1.getField("ITEM").trim()); //$NON-NLS-1$
				}
				catch (MISSING_XML_TAG_EXCEPTION e)
				{
					details.append("NO ITEM?");
				}
				try
				{
					details.append(" NMBR=");
					details.append(xmlParser1.getField("NMBR").trim()); //$NON-NLS-1$
				}
				catch (MISSING_XML_TAG_EXCEPTION e)
				{
					details.append("NM??");
				}
				try
				{
					details.append(" PRIR=");
					details.append(xmlParser1.getField("PRIR").trim()); //$NON-NLS-1$
				}
				catch (MISSING_XML_TAG_EXCEPTION e)
				{
					details.append("NO PRIR?");
				}

				details.append(" \n================================\n");

				try
				{
					tempRCD = parser.getNextField("RCD");
				}
				catch (MISSING_XML_TAG_EXCEPTION e)
				{
					tempRCD = ""; //$NON-NLS-1$		
				}
			}
			this.taDetail.setText(details.toString());
		}
		pack();
	}

	/**
	 * Invoked when an action occurs.
	 * @param ae the <code>ActionEvent</code>
	 */
	public void actionPerformed(ActionEvent ae)
	{
		if (ae.getSource() == this.pbOkay)
		{
			dispose();
		}	
	}

	/**
	 * Invoked when a key is pressed.
	 * @param ke the <code>KeyEvent</code>
	 */
	public void keyPressed(KeyEvent ke)
	{
		if (ke.getKeyCode() == KeyEvent.VK_ENTER && ke.getSource() == this.pbOkay)
		{
			this.pbOkay.requestFocusInWindow();
			this.pbOkay.doClick();
		}
	}
}
