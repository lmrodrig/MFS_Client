/* @ Copyright IBM Corporation 2010. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2010-03-05       42558JL Santiago SC      -Initial version, Java 5.0
 * 2010-04-14   ~1  48201JL Santiago SC      -Initialize indicator barcode when needed.
 ******************************************************************************/

package com.ibm.rchland.mfgapps.mfsclient.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.Border;

import com.ibm.rchland.mfgapps.client.utils.beans.IGSTextFieldPanelProperties;
import com.ibm.rchland.mfgapps.client.utils.messagebox.IGSMessageBox;
import com.ibm.rchland.mfgapps.mfsclient.MFSActionableDialog;
import com.ibm.rchland.mfgapps.mfsclient.MFSFrame;
import com.ibm.rchland.mfgapps.mfsclient.barcode.MFSBCBarcodeDecoder;
import com.ibm.rchland.mfgapps.mfsclient.barcode.MFSBCIndicatorValue;
import com.ibm.rchland.mfgapps.mfsclient.barcode.MFSBCPartObject;
import com.ibm.rchland.mfgapps.mfsclient.struct.MFSOnDemandKeyData;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSFieldPanelBuilder;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSFieldPanelFormatter;
import com.ibm.rchland.mfgapps.mfscommon.MFSConfig;
import com.ibm.rchland.mfgapps.mfscommon.exception.MFSException;

/**
 * <code>MFSOnDemandDialog</code> is a dialog created for on demand printing which
 * contains a <code>IGSTextFieldPanel</code> created dynamically.
 * @author The MFS Client Development Team
 */
public class MFSOnDemandDialog extends MFSActionableDialog
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1301410510112904551L;

	/** Print button */
	private JButton pbPrint;
	
	/** Exit button */
	private JButton pbExit;
	
	/** The quantity of labels to print */
	private JTextField tfQty;
	
	/** Key Data for on demand printing */
	private MFSOnDemandKeyData odkData;
	
	/** The <code>MFSFieldPanelFormatter</code> which formats and handles validation in the
	 * <code>MFSFieldPanelBuilder</code> that contains a <code>IGSTextFieldPanel</code>
	 */
	private MFSFieldPanelFormatter fpFormatter;
	
	/** Parent frame */
	private MFSFrame parent;
	
	/** Print flag */
	private boolean printRequired;

	/**
	 * Creates a new <code>MFSOnDemandDialog</code>
	 * @param parent frame container
	 * @param title for this dialog
	 * @param odkData is the key data for on demand printing
	 * @throws MFSException
	 */
	public MFSOnDemandDialog(MFSFrame parent, String title, MFSOnDemandKeyData odkData) 
		throws MFSException
	{
		super(parent, title);
		
		this.parent = parent;
		this.odkData = odkData;
		
		initialize();
		createLayout();
	}
	
	/**
	 * Invoked when an action occurs.
	 * @param ae the <code>ActionEvent</code>
	 */
	public void actionPerformed(ActionEvent ae)
	{
		final Object source = ae.getSource();
		
		if(source == pbPrint)
		{
			try
			{
				// Retrieve all input data
				String labelData = fpFormatter.rtvInputData();
				odkData.setLabelData(labelData);
				
				// Retrieve quantity
				odkData.setQty(rtvQty());
				
				setPrintRequired(true);
				this.dispose();
			}
			catch(Exception e)
			{
				IGSMessageBox.showOkMB(getParentFrame(), null, e.getMessage(), null);
			}
		}
		else if(source == pbExit)
		{
			setPrintRequired(false);
			this.dispose();
		}
	}
	
	/** Creates the layout */
	protected void createLayout() 
	{		
		// north panel
		JPanel northPanel = new JPanel(new FlowLayout());
		northPanel.setBackground(Color.BLUE);
		
		JLabel label = createLabel("Label Type : " + odkData.getLabelType());
		label.setForeground(Color.WHITE);
		northPanel.add(label);
		
		if(null != odkData.getPlom() && !odkData.getPlom().equals(""))
		{
			label = createLabel("   PLOM : " + odkData.getPlom());
			label.setForeground(Color.WHITE);
			northPanel.add(label);
		}
		
		// the dynamic panel	
		fpFormatter.getTextFieldPanel().addKeyListenerToComponents(this);
		JScrollPane centerPanel = new JScrollPane(fpFormatter.getTextFieldPanel());
	    Border border = centerPanel.getBorder();
	    Border outerMargin = BorderFactory.createEmptyBorder(10, 10, 0, 10);
	    centerPanel.setBorder(BorderFactory.createCompoundBorder(outerMargin, border));

		// Quantity panel
		JPanel qtyPanel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.RELATIVE,
				new Insets(0, 0, 0, 0), 0, 0);
		
		gbc.weighty = 1.0;				

		gbc.gridy++;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(10, 10, 10, 0);
		qtyPanel.add(new JLabel("Qty : "), gbc);
		
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(10, 55, 10, 10);
		tfQty = createTextField(5, 2);
		tfQty.setText("1");
		qtyPanel.add(tfQty, gbc);
	
		// button panel
		JPanel buttonPanel = new JPanel(new FlowLayout());
		buttonPanel.setBackground(Color.BLUE);
		
		pbPrint = createButton("Print", 'P');
		pbPrint.addActionListener(this);
		pbPrint.addKeyListener(this);
		buttonPanel.add(pbPrint);
		
		pbExit = createButton("Exit", 'x');
		pbExit.addActionListener(this);
		pbExit.addKeyListener(this);
		buttonPanel.add(pbExit);
		
		// south panel
		JPanel southPanel = new JPanel(new BorderLayout());
		southPanel.add(qtyPanel, BorderLayout.CENTER);
		southPanel.add(buttonPanel, BorderLayout.SOUTH);
		
		// content panel
		JPanel contentPane = new JPanel(new BorderLayout());
		contentPane.add(northPanel, BorderLayout.NORTH);
		contentPane.add(centerPanel, BorderLayout.CENTER);
		contentPane.add(southPanel, BorderLayout.SOUTH);
		
		// dialog settings
		setButtonDimensions(this.pbPrint, this.pbExit);
		setContentPane(contentPane);
		setPreferredSize(new Dimension(400, 380)); 
		setModal(true);

		pack();
		
		// Set location in main screen
		this.setLocationRelativeTo(parent);				
	}
	
	/** Initialize variables used in this dialog */
	private void initialize()
		throws MFSException
	{
		/* Try to create a panel builder here because it may throw an exception if some data is
		 * incorrect, same for the panel formatter. If something goes wrong will throw an exception
		 * and the dialog will not try to create the layout.
		 */
		String tfPanelTitle = "Enter values required(*) for printing";
		
		IGSTextFieldPanelProperties tfpp = new IGSTextFieldPanelProperties();
		tfpp.setFixedSize(false);
		tfpp.setLeftMargin(35);
		tfpp.setRightMargin(35);
		
		MFSFieldPanelBuilder fpb = new MFSFieldPanelBuilder(tfPanelTitle, odkData, tfpp);
		fpFormatter = new MFSFieldPanelFormatter(fpb);
	}
	
	/**
	 * @return the printRequired
	 */
	public boolean isPrintRequired() 
	{
		return printRequired;
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
			
			if(source instanceof JButton)
			{
				JButton button = (JButton) source;
				button.requestFocusInWindow();
				button.doClick();
			}
			else if(source instanceof JTextField)
			{
				JTextField tf = (JTextField) source;
				
				recvInput(tf);
			}
		}
		else if (keyCode == KeyEvent.VK_ESCAPE)
		{
			this.pbExit.requestFocusInWindow();
			this.pbExit.doClick();
		}
	}
	
	/**
	 * Special logic applied for <code>JTextField</code> of type B = Barcode
	 * @param textField the <code>JTextField</code> the key action
	 */
	protected void recvInput(JTextField textField)
	{
		try
		{
			if(fpFormatter.isBarcodeField(textField.getName()))
			{
				MFSBCBarcodeDecoder barcode = MFSBCBarcodeDecoder.getInstance();
				barcode.setMyBarcodeInput(textField.getText());
				
				/* ~1A - Some reprint locations don't have the indicator initialized.
				 * The indicator below was copied from the MFSWorkUnitPNSNDialog */
				if(null == barcode.getMyBCIndicatorValue())
				{
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
					
					barcode.setMyBCIndicatorValue(bcIndVal);
				}
				
				// ~1A - Initialize the MFSBCPartObject when needed.
				if(null == barcode.getBCMyPartObject())
				{
					barcode.setMyBCPartObject(new MFSBCPartObject());
				}
				
				// Start decoding
				barcode.decodeBarcodeFor(this);
				
				textField.setText("");
				
				// part object
				int rc = barcode.getBCMyPartObject().getRC();

				// invalid
				if(0 != rc)
				{
					throw new Exception(barcode.getBCMyPartObject().getErrorString());
				}

				// all good
				fpFormatter.setBCDecodedValues(barcode.getBCMyPartObject());
				fpFormatter.setBCDecodedValues(barcode.getMyBCIndicatorValue());
			}		
		}
		catch(Exception e)
		{
			IGSMessageBox.showOkMB(getParentFrame(), null, e.getMessage(), null);
		}
	}

	/**
	 * Retrieve the label quantity to be printed
	 * @return the int represeting the label quantity
	 * @throws MFSException
	 */
	public int rtvQty() throws MFSException
	{
		int qty = 0;

		try
		{
			qty = Integer.parseInt(tfQty.getText());
		}
		catch(NumberFormatException e)
		{
			// do nothing
		}		
		
		if(0 >= qty)
		{
			throw new MFSException("Error: Invalid quantity number!", false);
		}
		
		return qty;
	}

	/**
	 * @param printRequired the printRequired to set
	 */
	public void setPrintRequired(boolean printRequired) 
	{
		this.printRequired = printRequired;
	}
}
