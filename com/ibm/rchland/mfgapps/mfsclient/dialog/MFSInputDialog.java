/* © Copyright IBM Corporation 2008, 2010. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2007-12-06      41356MZ  Santiago SC      -Initial version
 * 2010-10-11   ~1 48749JL  Santiago SC      -BC decoder support
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
import com.ibm.rchland.mfgapps.mfsclient.MFSActionableDialog;
import com.ibm.rchland.mfgapps.mfsclient.MFSFrame;
import com.ibm.rchland.mfgapps.mfsclient.barcode.MFSBCBarcodeDecoder;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSConstants;

/**
 * <code>MFSGenericInputDialog</code> is a <code>MFSActionableDialog</code> that
 * that gets a value <code>String</code> from an input textbox.
 * @author The MFS Client Development Team
 */
public class MFSInputDialog 
	extends MFSActionableDialog
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6271443108618376183L;

	/** label asking for input */
	protected JLabel lblHeader = null; //~1A
	
	/** label displaying the input */
	protected JLabel lblInput = null;

	/** The input <code>JTextField</code>. */
	protected JTextField tfInput = null;

	/** value that was input */
	protected String fieldInputValue = null;
	
	/** length of the input */
	protected int fieldInputLength = 0;

	/** The Proceed <code>JButton</code>. */
	protected JButton pbProceed = createButton("Done", 'D');

	/** The Cancel <code>JButton</code>. */
	protected JButton pbCancel = createButton("Cancel", 'n');
	
	/** Set <code>true</code> when the proceed button is selected. */
	protected boolean fieldProceedSelected = false;	
	
	/** The barcode decoder */
	protected MFSBCBarcodeDecoder barcode; //~1A
	
	/** THe header text */
	private String headerText; //~1A
	
	/**
	 * Constructs a new <code>MFSInputDialog</code>.
	 * @param parent the <code>MFSFrame</code> that caused this
	 *        <code>MFSGenericInputDialog</code> to be displayed
	 * @param title the <code>String</code> to display in the title bar
	 */
	public MFSInputDialog(MFSFrame parent, String title)
	{
		super(parent, title);

		createLayout();
		addMyListeners();
	}
	
	//~1A
	/**
	 * Constructs a new <code>MFSInputDialog</code>.
	 * @param parent the <code>MFSFrame</code> that caused this
	 *        <code>MFSGenericInputDialog</code> to be displayed
	 * @param title the <code>String</code> to display in the title bar
	 */
	public MFSInputDialog(MFSFrame parent, String title, String headerText, MFSBCBarcodeDecoder barcode)
	{
		super(parent, title);
		
		this.headerText = headerText;
		this.barcode = barcode;

		createLayout();

		addMyListeners();
	}	
	
	/**
	 * Constructs a new <code>MFSInputDialog</code>.
	 * @param parent the <code>MFSFrame</code> that caused this
	 *        <code>MFSGenericInputDialog</code> to be displayed
	 * @param title the <code>String</code> to display in the title bar
	 * @param inputLength the <code>int</code> to indicate the input length 
	 * @param proceedButtonText the <code>String</code> to display in the proceed button
	 * @param cancelButtonText the <code>String</code> to display in the cancel button
	 */
	public MFSInputDialog(MFSFrame parent, String title, int inputLength, 
			String proceedButtonText, String cancelButtonText)
	{
		super(parent, title);

		this.fieldInputLength = inputLength;
		
		setButtons(proceedButtonText, cancelButtonText);
		
		createLayout();
		addMyListeners();		
	}	
	
	/** 
	 * Adds this dialog's <code>Component</code>s to the layout. 
	 */
	protected void createLayout()
	{
		this.lblHeader = createLabel((null != headerText)? headerText : ""); //~1A

		this.lblInput = createLabel(" "); //~1C
		
		this.tfInput = createTextField(MFSConstants.LARGE_TF_COLS, this.fieldInputLength, 
				this.lblInput);		
		
		JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 30, 0));
		buttonPanel.add(this.pbProceed);
		buttonPanel.add(this.pbCancel);

		JPanel contentPane = new JPanel(new GridBagLayout());
		contentPane.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));
		
		
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 2, 1, 1.0, 1.0, //~1C
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 0, 0);
			
		contentPane.add(this.lblHeader, gbc); //~1A

		gbc.gridy++;
		contentPane.add(this.tfInput, gbc);

		gbc.gridy++;
		contentPane.add(lblInput, gbc);
		
		gbc.gridy++;	
		contentPane.add(buttonPanel, gbc);

		setContentPane(contentPane);
		setSize(300,210);
		
		// Set location in main screen
		setLocationRelativeTo(getParentFrame());    //~1A
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE); //~1A
	}

	/** 
	 * Adds the listeners to this dialog's <code>Component</code>s. 
	 */
	protected void addMyListeners()
	{
		this.pbProceed.addActionListener(this);
		this.pbCancel.addActionListener(this);

		this.addKeyListener(this);
		this.tfInput.addKeyListener(this);
		this.pbProceed.addKeyListener(this);
		this.pbCancel.addKeyListener(this);
	}

	
	/** Sets the name and mnemonic of both buttons in the dialog. */
	protected void setButtons(String proceedButtonText, String cancelButtonText)
	{
		try
		{
			if(proceedButtonText != null && cancelButtonText != null)
			{
				char mnemonic = proceedButtonText.charAt(0);
				this.pbProceed = createButton(proceedButtonText, mnemonic);
				
				int textLength = cancelButtonText.length();
				for(int i=0; i<textLength; i++)
				{
					if(cancelButtonText.charAt(i) != mnemonic)
					{
						this.pbCancel = createButton(cancelButtonText, cancelButtonText.charAt(i));
						break;
					}
				}			
			}
			// default values are taken
		}
		catch (Exception e)
		{
			// default values are taken
		}
	}
	
	//~1A
	/**
	 * decode barcode input 
	 */
	protected void decodeInput()
	{
		String input = this.tfInput.getText().trim();
		
		barcode.setMyBarcodeInput(input);			
		barcode.initializeDecoder();
		// Start decoding
		barcode.decodeBarcodeFor(this);
		
		// part object
		int rc = barcode.getBCMyPartObject().getRC();

		// invalid
		if(0 != rc)
		{
			IGSMessageBox.showOkMB(getParent(), null, barcode.getBCMyPartObject().getErrorString(), null);
		}
		else
		{
			this.fieldInputValue = input;
		}
	}

	/** Receive the input. */
	protected void receiveInput()
	{
		String input = this.tfInput.getText().trim();
		this.tfInput.setText("");
		
		if(input.length() > 0)
		{
			this.fieldInputValue = input.toUpperCase();
			lblInput.setText(this.fieldInputValue);
		}
		else
		{
			if(this.fieldInputValue == null)
			{
				lblInput.setText("");
			
				String title = "Entry Error";
				String erms = "You must enter a value.";
				IGSMessageBox.showOkMB(getParent(), title, erms, null);		
				this.tfInput.requestFocusInWindow();			
			}
			else
			{
				this.pbProceed.doClick();
			}
		}
	}

	/**
	 * @return Returns the inputValue.
	 */
	public String getInputValue()
	{
		return this.fieldInputValue;
	}

	
	/**
	 * Set a default value for the input field, and select it so it can be easily overridden
	 * @param dValue default value
	 */
	public void setDefaultValue(String dValue)
	{
		if (dValue != null)
		{
			this.tfInput.setText(dValue);
			this.tfInput.selectAll();
		}
	}
	
	/**
	 * Returns <code>true</code> iff the proceed button was selected.
	 * @return <code>true</code> iff the proceed button was selected
	 */
	public boolean getProceedSelected()
	{
		return this.fieldProceedSelected;
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
				if(null != barcode) //~1A section
				{
					decodeInput();
				}
				
				if(this.fieldInputValue != null)
				{
					this.fieldProceedSelected = true;
					dispose();
				}
				
				if(null == barcode && null == fieldInputValue) //~1C
				{
					String title = "Entry Error";
					String erms = "You must enter a value.";
					IGSMessageBox.showOkMB(getParent(), title, erms, null);
					this.tfInput.requestFocusInWindow();
				}
			}
			else if (source == this.pbCancel)
			{
				this.fieldInputValue = null;
				this.fieldProceedSelected = false;
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
		if (keyCode == KeyEvent.VK_ENTER)
		{
			final Object source = ke.getSource();
			if (source == this.tfInput)
			{
				if(null != barcode) //~1A section
				{
					pbProceed.doClick(); 
				}
				else
				{
					receiveInput();
				}
			}
			else if (source instanceof JButton)
			{
				JButton button = (JButton) source;
				button.requestFocusInWindow();
				button.doClick();
			}			
		}
		else if (keyCode == KeyEvent.VK_ESCAPE)
		{
			this.pbCancel.requestFocusInWindow();
			this.pbCancel.doClick();
		}
	}

	/**
	 * Invoked the first time a window is made visible.
	 * <p>
	 * Requests the focus for the {@link #tfInput}.
	 * @param we the <code>WindowEvent</code>
	 */
	public void windowOpened(WindowEvent we)
	{
		try
		{		
			if (we.getSource() == this)
			{
				this.tfInput.requestFocusInWindow();
			}
		}
		catch (Throwable throwable)
		{
			throwable.printStackTrace();
		}		
	}
	
	/**
	 * Invoked when the user attempts to close the window from the window's
	 * system menu. Does nothing.
	 * @param we the <code>WindowEvent</code>
	 */
	public void windowClosing(WindowEvent we)
	{
		this.fieldProceedSelected = false;
		super.windowClosing(we);
	}

}
