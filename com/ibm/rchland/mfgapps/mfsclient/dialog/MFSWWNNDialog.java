/* © Copyright IBM Corporation 2004, 2007. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2004-05-17      25455KK  Dan Kloepping    -Initial version
 * 2006-4-30    ~1 34647EM  Toribioh         -Change Input capacity for JTextFieldLen
 *                                            and validate inuput size and Barcode Rules
 * 2007-02-22      34242JR  R Prechel        -Java 5 version
 * 2008-06-05   ~2 41916EM  Santiago SC      -Initialize fieldReturnCode to 0 before calling enter()
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

import mfsxml.MfsXMLDocument;

import com.ibm.rchland.mfgapps.client.utils.messagebox.IGSMessageBox;
import com.ibm.rchland.mfgapps.mfsclient.MFSActionableDialog;
import com.ibm.rchland.mfgapps.mfsclient.MFSFrame;
import com.ibm.rchland.mfgapps.mfsclient.barcode.MFSBCBarcodeDecoder;
import com.ibm.rchland.mfgapps.mfsclient.barcode.MFSBCIndicatorValue;
import com.ibm.rchland.mfgapps.mfsclient.barcode.MFSBCPartObject;
import com.ibm.rchland.mfgapps.mfsclient.struct.MFSComponentRec;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSConstants;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSComm;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSTransaction;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSXmlTransaction;

/**
 * <code>MFSWWNNDialog</code> is the <code>MFSActionableDialog</code> used
 * to prompt the user for a WW Node Name and call PROCSSWWNN.
 * @author The MFS Client Development Team
 */
public class MFSWWNNDialog
	extends MFSActionableDialog
{
	private static final long serialVersionUID = 1L;
	/** The REPLACE log type. */
	public static final String LT_REPLACE = "REPLACE "; //$NON-NLS-1$

	/** The COLLECT log type. */
	public static final String LT_COLLECT = "COLLECT "; //$NON-NLS-1$

	/** The REMOVE log type. */
	public static final String LT_REMOVE = "REMOVE  "; //$NON-NLS-1$

	/** The RESCAN log type. */
	public static final String LT_RESCAN = "RESCAN  "; //$NON-NLS-1$

	/** The length of a short WWNN. */
	private static final int LOW_WWNN_LENGTH = 8;

	/** The length of a long WWNN. */
	private static final int HIGH_WWNN_LENGTH = 16;

	/** The input <code>JTextField</code>. */
	private JTextField tfInput = createTextField(MFSConstants.LARGE_TF_COLS, 19); //~1C

	/** The Enter <code>JButton</code>. */
	private JButton pbEnter = createButton("Enter", 'E');

	/** The Cancel <code>JButton</code>. */
	private JButton pbCancel = createButton("Cancel", 'n');

	/** The value <code>JLabel</code> for the scanned in WWNN value. */
	private JLabel vlWwnn = createLabel(" ");

	/** The <code>MFSComponentRec</code> for which logging is being performed. */
	private MFSComponentRec fieldCompRec = null;

	/** The log type that is being performed. */
	private String fieldLogType = null;

	/** The return code from this dialog. */
	private int fieldReturnCode = 0;

	/**
	 * Constructs a new <code>MFSWWNNDialog</code>.
	 * @param parent the <code>MFSFrame</code> that caused this
	 *        <code>MFSWWNNDialog</code> to be displayed
	 * @param rec the <code>MFSComponentRec</code> for which logging is being performed
	 * @param logType the log type that is being performed
	 */
	public MFSWWNNDialog(MFSFrame parent, MFSComponentRec rec, String logType)
	{
		super(parent, "");
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.fieldLogType = logType;
		this.fieldCompRec = rec;
		createLayout();
		addMyListeners();
	}

	/** Adds this dialog's <code>Component</code>s to the layout. */
	private void createLayout()
	{
		JLabel wwnnNameLabel = createLabel("WW Node Name:");

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
		contentPaneCenter.add(wwnnNameLabel, gbc);

		gbc.gridy = 2;
		contentPaneCenter.add(this.vlWwnn, gbc);

		gbc.gridy = 3;
		gbc.weightx = 1.0;

		contentPaneCenter.add(buttonPanel, gbc);

		setContentPane(contentPaneCenter);
		setLabelDimensions(this.vlWwnn, HIGH_WWNN_LENGTH);
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
		//check to make sure values are wanded in correctly
		if (this.vlWwnn.getText().length() != LOW_WWNN_LENGTH
				&& this.vlWwnn.getText().length() != HIGH_WWNN_LENGTH)
		{
			String erms = "Invalid WWNN Value Entered.  Please Fix.";
			IGSMessageBox.showOkMB(this, null, erms, null);
			this.tfInput.requestFocusInWindow();
		}
		else
		{
			int rc;
			try
			{
				MfsXMLDocument xml_data1 = new MfsXMLDocument("PROCSSWWNN"); //$NON-NLS-1$
				xml_data1.addOpenTag("DATA"); //$NON-NLS-1$
				xml_data1.addCompleteField("MATP", this.fieldCompRec.getMatp()); //$NON-NLS-1$
				xml_data1.addCompleteField("MMDL", this.fieldCompRec.getMmdl().trim()); //$NON-NLS-1$
				xml_data1.addCompleteField("MCTL", this.fieldCompRec.getMctl()); //$NON-NLS-1$
				xml_data1.addCompleteField("MSPI", this.fieldCompRec.getMspi()); //$NON-NLS-1$
				xml_data1.addCompleteField("MCSN", this.fieldCompRec.getMcsn()); //$NON-NLS-1$
				xml_data1.addCompleteField("WWNN", this.vlWwnn.getText()); //$NON-NLS-1$
				xml_data1.addCompleteField("PROC", this.fieldLogType); //$NON-NLS-1$
				xml_data1.addCloseTag("DATA"); //$NON-NLS-1$
				xml_data1.finalizeXML(); //$NON-NLS-1$

				MFSTransaction procsswwnn = new MFSXmlTransaction(xml_data1.toString());
				procsswwnn.setActionMessage("Scanning WW Node Name, Please Wait...");
				MFSComm.getInstance().execute(procsswwnn, this);
				rc = procsswwnn.getReturnCode();

				if (rc != 0)
				{
					IGSMessageBox.showOkMB(this, null, procsswwnn.getErms(), null);
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
		if (this.fieldLogType.equals(LT_COLLECT) || this.fieldLogType.equals(LT_REPLACE))
		{
			setTitle("Logging WW Node Name");
		}
		else if (this.fieldLogType.equals(LT_REMOVE))
		{
			setTitle("Removing WW Node Name");
		}
		else if (this.fieldLogType.equals(LT_RESCAN))
		{
			setTitle("Rescan WW Node Name");
		}
	}

	/** Processes the user input. */
	public void recvInput()
	{
		int rc = 0;
		try
		{
			final int inputLength = this.tfInput.getText().trim().length();
			if (inputLength == 0)
			{
				this.pbEnter.requestFocusInWindow();
				this.pbEnter.doClick();
			}
			else if (inputLength == LOW_WWNN_LENGTH || inputLength == HIGH_WWNN_LENGTH)
			{
				this.vlWwnn.setText(this.tfInput.getText().trim().toUpperCase());
			}
			//run it thru the decode logic now
			else
			{
				MFSBCBarcodeDecoder decoder = MFSBCBarcodeDecoder.getInstance();
				decoder.setMyBarcodeInput(this.tfInput.getText().trim().toUpperCase());
				decoder.setMyBCPartObject(new MFSBCPartObject());
				/* setup barcode indicator values */
				MFSBCIndicatorValue bcIndVal = new MFSBCIndicatorValue();
				bcIndVal.setPNRI("0"); //$NON-NLS-1$
				bcIndVal.setECRI("0"); //$NON-NLS-1$
				bcIndVal.setCSNI("0"); //$NON-NLS-1$
				bcIndVal.setCCAI("0"); //$NON-NLS-1$
				bcIndVal.setCMTI("0"); //$NON-NLS-1$
				bcIndVal.setPRLN("        "); //$NON-NLS-1$
				bcIndVal.setITEM("            "); //$NON-NLS-1$
				bcIndVal.setCOOI("0"); //$NON-NLS-1$

				decoder.setMyBCIndicatorValue(bcIndVal);
				if (decoder.decodeBarcodeFor(this))
				{
					rc = decoder.getBCMyPartObject().getRC();
				}
				else
				{
					rc = 10;
					decoder.getBCMyPartObject().setErrorString(
							MFSBCBarcodeDecoder.DECODE_TIMED_OUT_ERMS);
				}

				if (rc == 0)
				{
					//we don't have a WWNN decode indicator, so we've used the
					// DEF field to handle the decode
					this.vlWwnn.setText(decoder.getBCMyPartObject().getDEF());
				}
				else
				{
					String erms = decoder.getBCMyPartObject().getErrorString();
					if (erms.trim().length() == 0)
					{
						erms = "Invalid WWNN Barcode";
					}

					IGSMessageBox.showOkMB(this, null, erms, null);
					this.tfInput.requestFocusInWindow();
				}
			}

		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(this, null, null, e);
		}

		this.toFront();
		this.tfInput.setText(""); //$NON-NLS-1$
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
			this.fieldReturnCode = 0; //~2A
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
			if (source == this.tfInput)
			{
				this.recvInput();
			}
			else if (source == this.pbEnter)
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
