/* © Copyright IBM Corporation 2008. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2008-01-19      30635SE  Martha Luna      -Initial version
 * 2008-05-19   ~1 39568JM  D Pietrasik      -Support Smart Serial by removing initial S
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
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


import com.ibm.rchland.mfgapps.client.utils.messagebox.IGSMessageBox;
import com.ibm.rchland.mfgapps.client.utils.xml.IGSXMLTransaction;
import com.ibm.rchland.mfgapps.mfsclient.MFSActionableDialog;
import com.ibm.rchland.mfgapps.mfsclient.MFSFrame;
import com.ibm.rchland.mfgapps.mfsclient.struct.MFSComponentRec;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSConstants;


/**
 * <code>MFSSEQNDialog</code> is the <code>MFSSEQNDialog</code> used
 * to prompt the user for a Sequence Number and call vldt_seqn.
 * @author The MFS Client Development Team
 */
public class MFSSEQNDialog
	extends MFSActionableDialog
{
	private static final long serialVersionUID = 1L;
	/** The input <code>JTextField</code>. */
	private JTextField tfInput = createTextField(MFSConstants.LARGE_TF_COLS, 19); 

	/** The Enter <code>JButton</code>. */
	private JButton pbEnter = createButton("Enter", 'E');

	/** The Cancel <code>JButton</code>. */
	private JButton pbCancel = createButton("Cancel", 'n');

	/** The value <code>JLabel</code> for the scanned in SEQN value. */
	private JLabel vlSeqn = createLabel(" ");

	/** The <code>MFSComponentRec</code> for which logging is being performed. */
	private MFSComponentRec fieldCompRec = null;

	/** The log type that is being performed. */
	private String fieldMFGN = null;
	
	/** The log type that is being performed. */
	private String fieldIDSS = null;

	/** The return code from this dialog. */
	private int fieldReturnCode = 0;
	
	/** Set <code>true</code> iff the enter button is pressed. */
	private boolean fieldPressedEnter = false;

	/**
	 * Constructs a new <code>MFSSEQNDialog</code>.
	 * @param parent the <code>MFSFrame</code> that caused this
	 *        <code>MFSSEQNDialog</code> to be displayed
	 * @param rec the <code>MFSComponentRec</code> for which logging is being performed
	 * @param mfgn the <code>MFSHeaderRec</code> for which MFGN will be validate SEQN
	 * @param idss the <code>MFSHeaderRec</code> for which IDSS will be validate SEQN
	 */
	public MFSSEQNDialog(MFSFrame parent, MFSComponentRec rec, String mfgn, String idss)
	{
		super(parent, "");
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.fieldCompRec = rec;
		this.fieldMFGN = mfgn;
		this.fieldIDSS = idss;
		createLayout();
		addMyListeners();
	}

	/** Adds this dialog's <code>Component</code>s to the layout. */
	private void createLayout()
	{
		JLabel seqnNameLabel = createLabel("Sequence Number:");

		JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 30, 0));
		buttonPanel.add(this.pbEnter);
		buttonPanel.add(this.pbCancel);

		JPanel contentPaneCenter = new JPanel(new GridBagLayout());
		contentPaneCenter.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1.0;
		gbc.insets = new Insets(4, 4, 4, 4);
		contentPaneCenter.add(this.tfInput, gbc);

		gbc.gridy = 1;
		gbc.weightx = 0.0;
		gbc.weighty = 1.0;
		contentPaneCenter.add(seqnNameLabel, gbc);

		gbc.gridy = 2;
		contentPaneCenter.add(this.vlSeqn, gbc);

		gbc.gridy = 3;
		gbc.weightx = 1.0;

		contentPaneCenter.add(buttonPanel, gbc);

		setContentPane(contentPaneCenter);
		setLabelDimensions(this.vlSeqn, 19);
		pack();

		initDisplay();
	}

	/** Adds the listeners to this dialog's <code>Component</code>s. */
	private void addMyListeners()
	{
		this.pbCancel.addActionListener(this);
		this.pbEnter.addActionListener(this);

		this.pbCancel.addKeyListener(this);
		this.pbEnter.addKeyListener(this);
		this.tfInput.addKeyListener(this);
	}

	/** Invoked when the enter button is pressed. */
	public void enter()
	{
		//check to make sure that something have been written
		if (this.tfInput.getText().trim().equals(""))  //$NON-NLS-1$
		{
			String erms = "You need introduce a Sequence Number.";
			IGSMessageBox.showOkMB(this, null, erms, null);
			this.tfInput.requestFocusInWindow();
		}
		else
		{
			int rc;
			try
			{
				IGSXMLTransaction vltdSEQN = new IGSXMLTransaction("VLDT_SEQN"); //$NON-NLS-1$
				vltdSEQN.startDocument();
				vltdSEQN.addElement("MFGN", this.fieldMFGN);  //$NON-NLS-1$
				vltdSEQN.addElement("IDSS", this.fieldIDSS);  //$NON-NLS-1$
				vltdSEQN.addElement("MCTL", this.fieldCompRec.getMctl()); //$NON-NLS-1$
				String seqNum = this.tfInput.getText().trim().toUpperCase();  // ~1A
				if (seqNum.charAt(0) == 'S')  // ~1A
				{
					seqNum = seqNum.substring(1); /* chop off the S */
				}
				vltdSEQN.addElement("INSQID", seqNum); //$NON-NLS-1$ //~1C
				vltdSEQN.endDocument();
				vltdSEQN.run();
				rc = vltdSEQN.getReturnCode();
				if (rc != 0)
				{
					IGSMessageBox.showOkMB(this, null, vltdSEQN.getErms(), null);
				}

			}
			catch (Exception e)
			{
				IGSMessageBox.showOkMB(this, null, null, e);
				rc = 1;
			}

			if (rc != 0)
			{
				//use a special return code from this method so we know not to
				// display a message in the calling pgm
				this.fieldReturnCode = -44;
				this.tfInput.setText(""); //$NON-NLS-1$
				this.tfInput.requestFocusInWindow();
			}
			else
			{
				this.fieldReturnCode = 0;
				this.dispose();
			}
		}
	}

	/**
	 * Returns the return code.
	 * @return the return code
	 */
	public int getReturnCode()
	{
		return this.fieldReturnCode;
	}

	/** Sets the dialog's title. */
	public void initDisplay()
	{
		setTitle("Logging Sequence Number");
	}
	
	/**
	 * Returns <code>true</code> iff the enter button was pressed.
	 * @return <code>true</code> iff the enter button was pressed
	 */
	public boolean getPressedEnter()
	{
		return this.fieldPressedEnter;
	}

	
	/**
	 * Invoked when an action occurs.
	 * @param ae the <code>ActionEvent</code>
	 */
	public void actionPerformed(ActionEvent ae)
	{
		final Object source = ae.getSource();
		if (source == this.pbCancel)
		{
			this.fieldReturnCode = -44;
			this.dispose();
		}
		else if (source == this.pbEnter)
		{
			this.enter();
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
			if (source == this.pbEnter || source == this.tfInput)
			{
				this.pbEnter.requestFocusInWindow();
				this.pbEnter.doClick();
			}
			else if (source == this.pbCancel)
			{
				this.fieldReturnCode = -44;
				this.pbCancel.requestFocusInWindow();
				this.pbCancel.doClick();
			}
		}
		else if (keyCode == KeyEvent.VK_ESCAPE)
		{
			this.fieldReturnCode = -44;
			this.dispose();
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
			this.tfInput.requestFocusInWindow();
		}
	}
}
