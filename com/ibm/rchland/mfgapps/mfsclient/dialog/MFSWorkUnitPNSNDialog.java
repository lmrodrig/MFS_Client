/* © Copyright IBM Corporation 2005, 2007. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2007-01-26      34242JR  R Prechel        -Java 5 version
 * 2010-03-11 ~01  47595MZ  Toribio H        -Fix a bug when clicking the enter button.
 *                                            Removed enter method.
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.dialog;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import mfsxml.MfsXMLDocument;
import mfsxml.MfsXMLParser;

import com.ibm.rchland.mfgapps.client.utils.messagebox.IGSMessageBox;
import com.ibm.rchland.mfgapps.mfsclient.MFSActionableDialog;
import com.ibm.rchland.mfgapps.mfsclient.MFSFrame;
import com.ibm.rchland.mfgapps.mfsclient.barcode.MFSBCBarcodeDecoder;
import com.ibm.rchland.mfgapps.mfsclient.barcode.MFSBCIndicatorValue;
import com.ibm.rchland.mfgapps.mfsclient.barcode.MFSBCPartObject;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSConstants;
import com.ibm.rchland.mfgapps.mfscommon.MFSConfig;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSComm;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSTransaction;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSXmlTransaction;

/**
 * <code>MFSWorkUnitPNSNDialog</code> is the <code>MFSDialog</code> used to
 * obtain a work unit's mctl from the user. The user can either enter the mctl
 * directly, or enter the part number and sequence number and the FIND_WU
 * transaction will be called to determine the mctl.
 * @author The MFS Client Development Team
 */
public class MFSWorkUnitPNSNDialog
	extends MFSActionableDialog
{
	private static final long serialVersionUID = 1L;
	/** The input <code>JTextField</code>. */
	private JTextField tfInput = createTextField(MFSConstants.LARGE_TF_COLS, 0);

	/** The enter <code>JButton</code>. */
	private JButton pbEnter = createButton("Enter", 'E');

	/** The cancel <code>JButton</code>. */
	private JButton pbCancel = createButton("Cancel", 'n');

	/** The name <code>JLabel</code> for the part number. */
	private JLabel nlPartNum = createNameLabel(""); //$NON-NLS-1$

	/** The value <code>JLabel</code> used to display the part number. */
	private JLabel vlPartNum = createValueLabel(this.nlPartNum);

	/** The name <code>JLabel</code> for the sequence number. */
	private JLabel nlSeqNum = createNameLabel(""); //$NON-NLS-1$

	/** The value <code>JLabel</code> used to display the sequence number. */
	private JLabel vlSeqNum = createValueLabel(this.nlSeqNum);

	/** The fkit flag for the FIND_WU transaction. */
	private String fieldFkitFlag = " "; //$NON-NLS-1$

	/** The mctl obtained from the input. */
	private String fieldMctl = ""; //$NON-NLS-1$

	/** Set <code>true</code> iff the enter button is pressed. */
	private boolean fieldPressedEnter = false;

	/**
	 * Constructs a new <code>MFSWorkUnitPNSNDialog</code>.
	 * @param parent the <code>MFSFrame</code> that caused this
	 *        <code>MFSWorkUnitPNSNDialog</code> to be displayed
	 */
	public MFSWorkUnitPNSNDialog(MFSFrame parent)
	{
		super(parent, "Work Unit Control Number");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		createLayout();
		addMyListeners();
	}
	
	/** Adds this dialog's <code>Component</code>s to the layout. */
	private void createLayout()
	{
		JPanel labelPanel = new JPanel(new GridLayout(2, 2, 10, 0));
		labelPanel.add(this.nlPartNum);
		labelPanel.add(this.vlPartNum);
		labelPanel.add(this.nlSeqNum);
		labelPanel.add(this.vlSeqNum);
		
		JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 20, 0));
		buttonPanel.add(this.pbEnter);
		buttonPanel.add(this.pbCancel);
		
		JPanel contentPaneCenter = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1, 1,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, 
				new Insets(20, 4, 0, 4), 0, 0);
		contentPaneCenter.add(this.tfInput, gbc);

		gbc.gridy++;
		gbc.insets = new Insets(10, 4, 0, 4);
		contentPaneCenter.add(labelPanel, gbc);

		gbc.gridy++;
		gbc.insets = new Insets(10, 4, 10, 4);
		contentPaneCenter.add(buttonPanel, gbc);

		this.setContentPane(contentPaneCenter);
		setLabelDimensions(this.nlPartNum, 12);
		this.pack();
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

	/** Processes the input the user entered in the <code>JTextField</code>. */
	private void recvInput()
	{
		try
		{
			MFSBCBarcodeDecoder barcodeDecoder = MFSBCBarcodeDecoder.getInstance();
			barcodeDecoder.setMyBarcodeInput(this.tfInput.getText().toUpperCase());
			barcodeDecoder.setMyBCPartObject(new MFSBCPartObject());

			// Setup barcode indicator values
			MFSBCIndicatorValue bcIndVal = new MFSBCIndicatorValue();
			bcIndVal.setPNRI("1"); //$NON-NLS-1$

			String sni = MFSConfig.getInstance().getConfigValue("DECODESN"); //$NON-NLS-1$
			if (!sni.equals(MFSConfig.NOT_FOUND))
			{
				bcIndVal.setCSNI(sni);
			}
			else
			{
				bcIndVal.setCSNI("1"); //$NON-NLS-1$
			}
			barcodeDecoder.setMyBCIndicatorValue(bcIndVal);

			int rc;
			if (barcodeDecoder.decodeBarcodeFor(this) == false)
			{
				rc = 10;
				barcodeDecoder.getBCMyPartObject().setErrorString(MFSBCBarcodeDecoder.DECODE_TIMED_OUT_ERMS);
			}
			else
			{
				rc = barcodeDecoder.getBCMyPartObject().getRC();
			}

			boolean found = false;
			String errorString = null;
			MFSBCPartObject partObject = barcodeDecoder.getBCMyPartObject();
			if (rc == 0)
			{
				if (partObject.getWU().length() != 0)
				{
					this.nlPartNum.setText(""); //$NON-NLS-1$
					this.vlPartNum.setText(""); //$NON-NLS-1$
					this.nlSeqNum.setText(""); //$NON-NLS-1$
					this.vlSeqNum.setText(""); //$NON-NLS-1$

					found = true;
					this.fieldMctl = partObject.getWU();
					
					this.fieldPressedEnter = true;
					this.dispose();					
				}
				else
				{
					if (partObject.getPN().length() != 0)
					{
						if (this.nlPartNum.getText().length() == 0)
						{
							this.nlPartNum.setText("Part Number");
						}
						this.vlPartNum.setText(partObject.getPN());
						found = true;
					}
					if (partObject.getSN().length() != 0)
					{
						if (this.nlSeqNum.getText().length() == 0)
						{
							this.nlSeqNum.setText("Sequence Number");
						}
						this.vlSeqNum.setText(partObject.getSN());
						found = true;
					}
	
					if (this.vlSeqNum.getText().length() != 0
							&& this.vlPartNum.getText().length() != 0)
					{
						MfsXMLDocument xml_data1 = new MfsXMLDocument("FIND_WU"); //$NON-NLS-1$
						xml_data1.addOpenTag("DATA"); //$NON-NLS-1$
						xml_data1.addCompleteField("INPN", this.vlPartNum.getText()); //$NON-NLS-1$
						xml_data1.addCompleteField("INSQ", this.vlSeqNum.getText()); //$NON-NLS-1$
						xml_data1.addCompleteField("FKIT", this.fieldFkitFlag); //$NON-NLS-1$
						xml_data1.addCloseTag("DATA"); //$NON-NLS-1$
						xml_data1.finalizeXML();
						
						MFSTransaction find_wu = new MFSXmlTransaction(xml_data1.toString());
						find_wu.setActionMessage("Retrieving Work Unit, Please Wait...");
						MFSComm.getInstance().execute(find_wu, this);
						rc = find_wu.getReturnCode();
	
						MfsXMLParser xmlParser = new MfsXMLParser(find_wu.getOutput());
						if (rc == 0)
						{
							this.nlPartNum.setText(""); //$NON-NLS-1$
							this.vlPartNum.setText(""); //$NON-NLS-1$
							this.nlSeqNum.setText(""); //$NON-NLS-1$
							this.vlSeqNum.setText(""); //$NON-NLS-1$
	
							this.fieldMctl = xmlParser.getField("MCTL"); //$NON-NLS-1$
							found = true;
							
							this.fieldPressedEnter = true;
							this.dispose();						
						}
						else
						{
							errorString = xmlParser.getField("ERMS"); //$NON-NLS-1$
						}
					}
				}
			}
			if ((rc != 0) || (!found && this.tfInput.getText().length() != 0))
			{
				//if we couldn't decode a barcode,
				//check if the input it 8 long before displaying the error
				if (this.tfInput.getText().length() == 8)
				{
					this.nlPartNum.setText(""); //$NON-NLS-1$
					this.vlPartNum.setText(""); //$NON-NLS-1$
					this.nlSeqNum.setText(""); //$NON-NLS-1$
					this.vlSeqNum.setText(""); //$NON-NLS-1$

					this.fieldMctl = this.tfInput.getText().toUpperCase();
					
					this.fieldPressedEnter = true;
					this.dispose();					
				}
				else
				{
					String erms = partObject.getErrorString();
					if (erms.length() == 0)
					{
						erms = errorString;
					}
					
					IGSMessageBox.showOkMB(this, null, erms, null);

					this.nlPartNum.setText(""); //$NON-NLS-1$
					this.vlPartNum.setText(""); //$NON-NLS-1$
					this.nlSeqNum.setText(""); //$NON-NLS-1$
					this.vlSeqNum.setText(""); //$NON-NLS-1$
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
	 * Returns the value of the fkit flag.
	 * @return the value of the fkit flag
	 */
	public String getFkitFlag()
	{
		return this.fieldFkitFlag;
	}

	/**
	 * Sets the value of the fkit flag.
	 * @param fkitFlag the new value of the fkit flag
	 */
	public void setFkitFlag(String fkitFlag)
	{
		this.fieldFkitFlag = fkitFlag;
	}

	/**
	 * Returns the mctl obtained from the input.
	 * @return the mctl obtained from the input
	 */
	public String getMctl()
	{
		return this.fieldMctl;
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
		if (source == this.pbEnter)
		{			
			recvInput();  //~01
		}
		else if (source == this.pbCancel)
		{			
			this.dispose();
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
				this.pbEnter.doClick();  //~01
			}
			else if (source == this.pbCancel)
			{			
				this.pbCancel.doClick();
			}
		}
		else if (keyCode == KeyEvent.VK_ESCAPE)
		{		
			this.pbCancel.doClick();  //~01
		}
	}

	/**
	 * Invoked the first time a window is made visible.
	 * <p>
	 * Requests the focus for {@link #tfInput}.
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
