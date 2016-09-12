/* @ Copyright IBM Corporation 2010. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2010-03-05      46810JL  J Mastachi       -Initial version
 * 2010-04-14  ~01 48166JW  J Mastachi       -Add check for duplicate key parts
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.dialog;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.ibm.rchland.mfgapps.mfsclient.MFSActionableDialog;
import com.ibm.rchland.mfgapps.mfsclient.MFSFrame;
import com.ibm.rchland.mfgapps.mfsclient.barcode.MFSBCBarcodeDecoder;
import com.ibm.rchland.mfgapps.mfsclient.barcode.MFSBCIndicatorValue;
import com.ibm.rchland.mfgapps.mfsclient.barcode.MFSBCPartObject;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSConstants;
import com.ibm.rchland.mfgapps.mfscommon.MFSConfig;

public class MFSKeyPartEntryDialog extends MFSActionableDialog
{
	private static final long serialVersionUID = 1L;
	/** The Error message <code>JLabel</code>. */
	private JLabel errorLabel = new JLabel();
	
	/** The <code>ListModel</code> where Key Parts will be added.*/
	private DefaultListModel lmKeyParts = null;
	
	/** The Cancel <code>JButton</code>. */
	private JButton pbClose = createButton("Close", 'C');
	
	/** The Enter <code>JButton</code>. */
	private JButton pbEnter = createButton("Enter", 'E');
	
	/** The Key Part input <code>JTextField</code>. */
	private JTextField tfKeyPart = createTextField(MFSConstants.LARGE_TF_COLS, 22);
	
	/** The Key Part PN value <code>JLabel</code>. */
	private JLabel vlKeyPartPN = null;
	
	/** The Key Part SN value <code>JLabel</code>. */
	private JLabel vlKeyPartSN = null;

	/**
	 * Constructs a new <code>MFSKeyPartEntryDialog</code>.
	 * @param model the <code>DefaultListModel</code> where 
	 *        Key Parts will be added
	 * @param parent the <code>MFSFrame</code> that caused this
	 *        <code>MFSKeyPartEntryDialog</code> to be displayed
	 */
	public MFSKeyPartEntryDialog(DefaultListModel model, MFSFrame parent)
	{
		super(parent, "Scan Key Part Information");
		this.lmKeyParts = model;
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		createLayout();
		addMyListeners();
	}

	/** Adds the listeners to this dialog's <code>Component</code>s. */
	private void addMyListeners()
	{
		this.addKeyListener(this);
		
		this.pbClose.addActionListener(this);
		this.pbClose.addKeyListener(this);
		this.pbEnter.addActionListener(this);
		this.pbEnter.addKeyListener(this);
		
		this.tfKeyPart.addKeyListener(this);
		
	}

	/** Adds this dialog's <code>Component</code>s to the layout. */
	private void createLayout()
	{
		this.errorLabel.setForeground(Color.RED);
		this.errorLabel.setFont(MFSConstants.MEDIUM_DIALOG_FONT);
		
		JLabel titleLabel = createLabel("Scan Key Part Information:");
		titleLabel.setForeground(MFSConstants.PRIMARY_FOREGROUND_HIGHLIGHT_COLOR);
		titleLabel.setFont(MFSConstants.MEDIUM_DIALOG_FONT);
		
		JLabel keyPartNumLabel = createSmallNameLabel("Key Part Number:");
		if (this.vlKeyPartPN == null)
		{
			this.vlKeyPartPN = createValueLabel(keyPartNumLabel);
		}
		
		JLabel keyPartSerLabel = createSmallNameLabel("Key Part Serial:");
		if (this.vlKeyPartSN == null)
		{
			this.vlKeyPartSN = createValueLabel(keyPartSerLabel);
		}
		
		JPanel buttonPanel = new JPanel(new GridLayout(3, 2, 10, 10));
		buttonPanel.setBackground(MFSConstants.PRIMARY_BACKGROUND_COLOR);
		
		buttonPanel.add(keyPartNumLabel);
		buttonPanel.add(this.vlKeyPartPN);
		buttonPanel.add(keyPartSerLabel);
		buttonPanel.add(this.vlKeyPartSN);
		buttonPanel.add(this.pbClose);
		buttonPanel.add(this.pbEnter);
		
		JPanel contentPane = new JPanel(new GridBagLayout());
		contentPane.setBackground(MFSConstants.PRIMARY_BACKGROUND_COLOR);
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(20, 50, 10, 50);

		gbc.gridy++;
		gbc.anchor = GridBagConstraints.WEST;
		contentPane.add(errorLabel, gbc);
		
		gbc.gridy++;
		gbc.insets.top = 5;
		gbc.anchor = GridBagConstraints.CENTER;
		contentPane.add(titleLabel, gbc);
		
		gbc.gridy++;
		gbc.insets.bottom = 5;
		contentPane.add(this.tfKeyPart, gbc);
		
		gbc.gridy++;
		gbc.insets.top = 10;
		gbc.insets.bottom = 20;
		contentPane.add(buttonPanel, gbc);
		
		setContentPane(contentPane);
		pack();
	}
	
	/** Processes the input the user entered in the <code>JTextField</code>.*/
	private void recvInput()
	{
		try
		{
			int rc = 0;
			this.errorLabel.setText("");

			MFSBCBarcodeDecoder barcode = MFSBCBarcodeDecoder.getInstance();
			barcode.setMyBarcodeInput(this.tfKeyPart.getText().toUpperCase());
			barcode.setMyBCPartObject(new MFSBCPartObject());

			/* setup barcode indicator values */
			MFSBCIndicatorValue bcIndVal = new MFSBCIndicatorValue();
			bcIndVal.setPNRI("1"); //$NON-NLS-1$

			String sni = MFSConfig.getInstance().getConfigValue("DECODESN"); //$NON-NLS-1$
			if (!sni.equals("Not Found")) //DECODESN not found //$NON-NLS-1$
			{
				bcIndVal.setCSNI(sni);
			}
			else
			{
				bcIndVal.setCSNI("1"); //$NON-NLS-1$
			}

			barcode.setMyBCIndicatorValue(bcIndVal);

			if (barcode.decodeBarcodeFor(this) == false)
			{
				rc = 10;
				barcode.getBCMyPartObject().setErrorString(
						MFSBCBarcodeDecoder.DECODE_TIMED_OUT_ERMS);
			}
			else
			{
				rc = barcode.getBCMyPartObject().getRC();
			}
			
			if (rc != 0)
			{
				String erms = barcode.getBCMyPartObject().getErrorString();
				if (erms.trim().length() == 0)
				{
					erms = MFSBCBarcodeDecoder.INVALID_BARCODE_ERMS;
				}
				this.errorLabel.setText(erms);
			}

			else
			{
				if (!(barcode.getBCMyPartObject().getPN().equals(""))) //$NON-NLS-1$
				{
					this.vlKeyPartPN.setText(barcode.getBCMyPartObject().getPN());
				}
				if (!(barcode.getBCMyPartObject().getSN().equals(""))) //$NON-NLS-1$
				{
					this.vlKeyPartSN.setText(barcode.getBCMyPartObject().getSN());
				}
				if (!this.vlKeyPartPN.getText().equals("") && !this.vlKeyPartSN.getText().equals(""))
				{
					this.pbEnter.requestFocusInWindow();
					this.pbEnter.doClick();
				}
			}
		}
		
		catch (Exception e)
		{
			this.errorLabel.setText(e.getMessage());
		}

		this.toFront();
		this.tfKeyPart.setText(""); //$NON-NLS-1$
		createLayout();
	}

	/** Validates and adds Key Part Number and Key Part Serial into Key Parts list. */
	private void enter()
	{
		int rc = 0;
		StringBuffer row = new StringBuffer();
		
		try
		{
			if (this.vlKeyPartPN.getText().equals(""))
			{
				this.errorLabel.setText("Key Part Number is a required field");
				rc = 1;
			}
			else if (this.vlKeyPartSN.getText().equals(""))
			{
				this.errorLabel.setText("Key Part Serial is a required field");
				rc = 1;
			}
			
			if (rc == 0)
			{
				row.append("PN=");
				row.append(this.vlKeyPartPN.getText().toUpperCase().trim());
				row.append(" SN=");
				row.append(this.vlKeyPartSN.getText().toUpperCase().trim());
				
				if (this.lmKeyParts.contains(row.toString())) //~01A
				{
					this.errorLabel.setText("Key Part already exists."); //~01A
				}
				else                                          //~01A
				{
					this.lmKeyParts.addElement(row.toString());

					this.vlKeyPartPN.setText("");
					this.vlKeyPartSN.setText("");
					this.errorLabel.setText("Key Part added.");
				}
			}
		}
		
		catch (Exception e)
		{
			this.errorLabel.setText(e.getMessage());
		}
		
		this.tfKeyPart.requestFocusInWindow();
		createLayout();		
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
			if (source == this.pbClose)
			{
				this.dispose();
			}
			else if (source == this.pbEnter)
			{
				enter();
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
			if (source == this.pbClose)
			{
				this.pbClose.requestFocusInWindow();
				this.pbClose.doClick();
			}
			else if (source == this.pbEnter)
			{
				this.pbEnter.requestFocusInWindow();
				this.pbEnter.doClick();
			}
			else if (source == this.tfKeyPart)
			{
				recvInput();
				this.tfKeyPart.requestFocusInWindow();
			}
		}
		else if (keyCode == KeyEvent.VK_ESCAPE)
		{
			this.dispose();
		}
	}

}
