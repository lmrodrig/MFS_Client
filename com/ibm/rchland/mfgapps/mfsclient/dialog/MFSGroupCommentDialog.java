/* © Copyright IBM Corporation 2004, 2007. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2007-01-25      34242JR  R Prechel        -Java 5 version
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.dialog;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;

import mfsxml.MfsXMLDocument;
import mfsxml.MfsXMLParser;

import com.ibm.rchland.mfgapps.client.utils.IGSMaxLengthDocument;
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
 * <code>MFSGroupCommentDialog</code> is the <code>MFSActionableDialog</code>
 * used to add the same comment to a group of parts.
 * @author The MFS Client Development Team
 */
public class MFSGroupCommentDialog
	extends MFSActionableDialog
{
	private static final long serialVersionUID = 1L;
	/** The <code>JList</code>. */
	private JList lstCmnt = new JList();

	/** The <code>JScrollPane</code> that contains the list. */
	private JScrollPane spCmnt = new JScrollPane(this.lstCmnt,
			ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
			ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

	/** The comment input <code>JTextArea</code>. */
	private JTextArea taComment = new JTextArea(new IGSMaxLengthDocument(80),
			"Insert comment here", 1, 40);

	/** The input <code>JTextField</code>. */
	private JTextField tfInput = createTextField(MFSConstants.LARGE_TF_COLS, 22);

	/** The part number value <code>JLabel</code>. */
	private JLabel vlPN;
	
	/** The serial number value <code>JLabel</code>. */
	private JLabel vlSN;
	
	/** The Add <code>JButton</code>. */
	private JButton pbAdd = createButton("Add", 'A');

	/** The Remove <code>JButton</code>. */
	private JButton pbRemove = createButton("Remove", 'R');

	/** The Finish <code>JButton</code>. */
	private JButton pbFinish = createButton("Finish", 'F');

	/** The Cancel/Exit <code>JButton</code>. */
	private JButton pbCancel = createButton("Cancel/Exit", 'n');
	
	/** The <code>ListModel</code> for {@link #lstCmnt}. */
	private DefaultListModel fieldCmntListModel = new DefaultListModel();

	/**
	 * Constructs a new <code>MFSGroupCommentDialog</code>.
	 * @param parent the <code>MFSFrame</code> that caused this
	 *        <code>MFSGroupCommentDialog</code> to be displayed
	 */
	public MFSGroupCommentDialog(MFSFrame parent)
	{
		super(parent, "Add Comment");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.lstCmnt.setModel(this.fieldCmntListModel);
		createLayout();
		addMyListeners();
	}

	/** Adds this dialog's <code>Component</code>s to the layout. */
	private void createLayout()
	{
		JLabel descLabel = createSmallNameLabel("Part Number              Serial Number");
		this.taComment.setLineWrap(true);
		
		this.lstCmnt.setVisibleRowCount(16);
		this.lstCmnt.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		JLabel partLabel = createSmallNameLabel("  Part:");
		JLabel serialLabel = createSmallNameLabel("Serial:");
		this.vlPN = createSmallValueLabel(partLabel);
		this.vlSN = createSmallValueLabel(serialLabel);

		JPanel buttonPanel = new JPanel(new GridLayout(4, 1, 20, 20));
		buttonPanel.add(this.pbAdd);
		buttonPanel.add(this.pbRemove);
		buttonPanel.add(this.pbFinish);
		buttonPanel.add(this.pbCancel);

		JPanel contentPane = new JPanel(new GridBagLayout());

		GridBagConstraints constraintsDescJLabel = new GridBagConstraints();
		constraintsDescJLabel.gridx = 0;
		constraintsDescJLabel.gridy = 0;
		constraintsDescJLabel.anchor = GridBagConstraints.WEST;
		constraintsDescJLabel.insets = new Insets(30, 15, 4, 4);
		contentPane.add(descLabel, constraintsDescJLabel);
		
		GridBagConstraints constraintsCMNTScrollpane = new GridBagConstraints();
		constraintsCMNTScrollpane.gridx = 0;
		constraintsCMNTScrollpane.gridy = 1;
		constraintsCMNTScrollpane.gridheight = 3;
		constraintsCMNTScrollpane.fill = GridBagConstraints.BOTH;
		constraintsCMNTScrollpane.weightx = 1.0;
		constraintsCMNTScrollpane.weighty = 1.0;
		constraintsCMNTScrollpane.insets = new Insets(4, 15, 4, 4);
		contentPane.add(this.spCmnt, constraintsCMNTScrollpane);

		GridBagConstraints constraintsInputTextField = new GridBagConstraints();
		constraintsInputTextField.gridx = 1;
		constraintsInputTextField.gridy = 0;
		constraintsInputTextField.gridwidth = 2;
		constraintsInputTextField.fill = GridBagConstraints.HORIZONTAL;
		constraintsInputTextField.weightx = 1.0;
		constraintsInputTextField.insets = new Insets(30, 10, 4, 10);
		contentPane.add(this.tfInput, constraintsInputTextField);
		
		GridBagConstraints constraintsPNJLabel = new GridBagConstraints();
		constraintsPNJLabel.gridx = 1;
		constraintsPNJLabel.gridy = 1;
		constraintsPNJLabel.insets = new Insets(4, 10, 4, 4);
		contentPane.add(partLabel, constraintsPNJLabel);

		GridBagConstraints constraintsPNValueJLabel = new GridBagConstraints();
		constraintsPNValueJLabel.gridx = 2;
		constraintsPNValueJLabel.gridy = 1;
		constraintsPNValueJLabel.anchor = GridBagConstraints.WEST;
		constraintsPNValueJLabel.insets = new Insets(4, 4, 4, 4);
		contentPane.add(this.vlPN, constraintsPNValueJLabel);
		
		GridBagConstraints constraintsSNJLabel = new GridBagConstraints();
		constraintsSNJLabel.gridx = 1;
		constraintsSNJLabel.gridy = 2;
		constraintsSNJLabel.insets = new Insets(4, 10, 4, 4);
		contentPane.add(serialLabel, constraintsSNJLabel);

		GridBagConstraints constraintsSNValueJLabel = new GridBagConstraints();
		constraintsSNValueJLabel.gridx = 2;
		constraintsSNValueJLabel.gridy = 2;
		constraintsSNValueJLabel.anchor = GridBagConstraints.WEST;
		constraintsSNValueJLabel.insets = new Insets(4, 4, 4, 4);
		contentPane.add(this.vlSN, constraintsSNValueJLabel);

		GridBagConstraints constraintsButPanel = new GridBagConstraints();
		constraintsButPanel.gridx = 1;
		constraintsButPanel.gridy = 3;
		constraintsButPanel.gridwidth = 2;
		constraintsButPanel.weightx = 1.0;
		constraintsButPanel.weighty = 1.0;
		constraintsButPanel.insets = new Insets(4, 4, 4, 4);
		contentPane.add(buttonPanel, constraintsButPanel);

		GridBagConstraints constraintsCommentTextArea = new GridBagConstraints();
		constraintsCommentTextArea.gridx = 0;
		constraintsCommentTextArea.gridy = 4;
		constraintsCommentTextArea.fill = GridBagConstraints.BOTH;
		constraintsCommentTextArea.weightx = 0.0;
		constraintsCommentTextArea.weighty = 1.0;
		constraintsCommentTextArea.insets = new Insets(4, 15, 8, 4);
		contentPane.add(this.taComment, constraintsCommentTextArea);

		setContentPane(contentPane);
		setLabelDimensions(this.vlPN, MFSConstants.MAX_PN_CHARACTERS);
		setLabelDimensions(this.vlSN, MFSConstants.MAX_SN_CHARACTERS);
		pack();
		
		Dimension d = this.spCmnt.getPreferredSize();
		this.spCmnt.setPreferredSize(d);
	}

	/** Adds the listeners to this dialog's <code>Component</code>s. */
	private void addMyListeners()
	{
		this.pbAdd.addActionListener(this);
		this.pbRemove.addActionListener(this);
		this.pbFinish.addActionListener(this);
		this.pbCancel.addActionListener(this);
		
		this.pbAdd.addKeyListener(this);
		this.pbRemove.addKeyListener(this);
		this.pbFinish.addKeyListener(this);
		this.pbCancel.addKeyListener(this);
		this.tfInput.addKeyListener(this);
		this.taComment.addKeyListener(this);
	}
	
	/** Invoked when {@link #pbAdd} is selected. */
	private void processAddButton() 
	{
		try
		{
			String BLANK12 = "            ";
			int rc = 0;
			if(!this.vlSN.getText().equals("") && !this.vlPN.getText().equals(""))
			{
				MfsXMLDocument xml_data1 = new MfsXMLDocument("RTV_QD10"); //$NON-NLS-1$
				xml_data1.addOpenTag("DATA"); //$NON-NLS-1$
				xml_data1.addCompleteField("BACK","QDQDPN"); //$NON-NLS-1$ //$NON-NLS-2$
				//Construct QDQDPN = XXXXXXXXXXXX
				xml_data1.addOpenTag("FLD"); //$NON-NLS-1$
				xml_data1.addCompleteField("OPRT","="); //$NON-NLS-1$ //$NON-NLS-2$
				xml_data1.addCompleteField("NAME","QDQDPN"); //$NON-NLS-1$ //$NON-NLS-2$
				xml_data1.addCompleteField("VALU","'"+ this.vlPN.getText() + "'"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				xml_data1.addCloseTag("FLD"); //$NON-NLS-1$
				//Put in the AND string
				xml_data1.addOpenTag("FLD"); //$NON-NLS-1$
				xml_data1.addCompleteField("OPRT","AND"); //$NON-NLS-1$ //$NON-NLS-2$
				xml_data1.addCloseTag("FLD"); //$NON-NLS-1$
				//Construct QDQDSQ = XXXXXXXXXXXX
				xml_data1.addOpenTag("FLD"); //$NON-NLS-1$
				xml_data1.addCompleteField("OPRT","="); //$NON-NLS-1$ //$NON-NLS-2$
				xml_data1.addCompleteField("NAME","QDQDSQ"); //$NON-NLS-1$ //$NON-NLS-2$
				xml_data1.addCompleteField("VALU","'" + this.vlSN.getText() + "'"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				xml_data1.addCloseTag("FLD"); //$NON-NLS-1$
				xml_data1.addCloseTag("DATA");	 //$NON-NLS-1$
				xml_data1.finalizeXML();
				
				MFSTransaction rtv_qd10 = new MFSXmlTransaction(xml_data1.toString());
				rtv_qd10.setActionMessage("Verifying PN/SN in QD10, Please Wait...");
				MFSComm.getInstance().execute(rtv_qd10, this);
				
				rc = rtv_qd10.getReturnCode();

				MfsXMLParser xmlParser = new MfsXMLParser(rtv_qd10.getOutput());

				//If no error with SQL statements and returned something */
				if(rc == 0 && xmlParser.fieldExists("NAME")) //$NON-NLS-1$
				{
					String PN_X = this.vlPN.getText() + BLANK12;
					String SN_X = this.vlSN.getText() + BLANK12;
					String PN_12 = PN_X.substring(0,12);
					String SN_12 = SN_X.substring(0,12);
					this.fieldCmntListModel.addElement(PN_12 + "         " + SN_12);
					this.vlPN.setText(""); //$NON-NLS-1$
					this.vlSN.setText("");	//$NON-NLS-1$
				}
				else
				{
					String erms = "Part and serial number not in QD10";
					IGSMessageBox.showOkMB(this, null, erms, null);
				}
			}
			else
			{
				String erms = "Part number and serial number are both required!";
				IGSMessageBox.showOkMB(this, null, erms, null);
			}
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(this, null, null, e);
		}
		this.toFront();
		this.tfInput.setText(""); //$NON-NLS-1$
		this.tfInput.requestFocusInWindow();
	}
	
	/** Invoked when {@link #pbFinish} is selected. */
	private void processFinBut() 
	{
		try
		{
			int rc = 0;
			String PnSn_data;
			int index = 0;		//List Model index
			while (!this.taComment.getText().equals("") && this.fieldCmntListModel.size() > index)
			{
	        	/* start the AddQD10Cmnt transaction thread */	    
	        	MfsXMLDocument xml_data1 = new MfsXMLDocument("ADD_QDCMNT"); //$NON-NLS-1$
	        	xml_data1.addOpenTag("DATA"); //$NON-NLS-1$
				// PULL PN/SN from List Model and XMLize them
				PnSn_data = (String)this.fieldCmntListModel.getElementAt(index);
	        	xml_data1.addCompleteField("INPN",PnSn_data.substring(0,12)); //$NON-NLS-1$
	        	xml_data1.addCompleteField("INSQ", PnSn_data.substring(21)); //$NON-NLS-1$
	        	xml_data1.addCompleteField("CMMT", this.taComment.getText().trim().toUpperCase()); //$NON-NLS-1$
	        	xml_data1.addCompleteField("USER",MFSConfig.getInstance().get8CharUser()); //$NON-NLS-1$
	        	xml_data1.addCloseTag("DATA"); //$NON-NLS-1$
	        	xml_data1.finalizeXML();

	        	MFSTransaction add_qdcmnt = new MFSXmlTransaction(xml_data1.toString());
	        	add_qdcmnt.setActionMessage("Updating PN/SN Comment, Please Wait...");
	        	MFSComm.getInstance().execute(add_qdcmnt, this);
	        	
		        rc = add_qdcmnt.getReturnCode();
		        
		        MfsXMLParser xmlParser = new MfsXMLParser(add_qdcmnt.getOutput());

		        if (rc == 0) 
		        {
			        this.fieldCmntListModel.remove(index);
	    	    } 
		        else 
	      	    {
		      	    rc = 1;
		      	    index++;		// Go to next item on the list
	       	        String erms = xmlParser.getField("ERMS"); //$NON-NLS-1$
	       	        IGSMessageBox.showOkMB(this, null, erms, null);
	                this.toFront();
	                this.tfInput.requestFocusInWindow();
	            }
			}
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(this, null, null, e);
		}

		if (this.fieldCmntListModel.size() == 0)
		{
	   		this.dispose();
		}	
	}
	
	/** Processes the input the user entered in the <code>JTextField</code>. */
	private void recvInput() 
	{
		try
		{
			boolean found = false;
			int rc = 0;

			MFSBCBarcodeDecoder barcode = MFSBCBarcodeDecoder.getInstance();
			barcode.setMyBarcodeInput(this.tfInput.getText().toUpperCase());
			barcode.setMyBCPartObject(new MFSBCPartObject());

			/* setup barcode indicator values */
			MFSBCIndicatorValue bcIndVal = new MFSBCIndicatorValue();
			bcIndVal.setPNRI("1"); //$NON-NLS-1$
			
			String sni = MFSConfig.getInstance().getConfigValue("DECODESN"); //$NON-NLS-1$
			if (!sni.equals("Not Found")) //DECONDESN not found //$NON-NLS-1$
			{
				bcIndVal.setCSNI(sni);
			}
			else
			{
				bcIndVal.setCSNI("1"); //$NON-NLS-1$
			}
			
			barcode.setMyBCIndicatorValue(bcIndVal);

			if(barcode.decodeBarcodeFor(this) == false)	
			{
				rc = 10;
				barcode.getBCMyPartObject().setErrorString(MFSBCBarcodeDecoder.DECODE_TIMED_OUT_ERMS);
			}	
			else
			{
				rc = barcode.getBCMyPartObject().getRC();
			}

			if (rc == 0)
			{
				if (!(barcode.getBCMyPartObject().getPN().equals("")))
				{
					this.vlPN.setText(barcode.getBCMyPartObject().getPN());
					found = true;
				}
				if (!(barcode.getBCMyPartObject().getSN().equals("")))
				{

					this.vlSN.setText(barcode.getBCMyPartObject().getSN());
					found = true;
				}
			}
			if ((rc != 0) || (!found))
			{
				String erms = barcode.getBCMyPartObject().getErrorString();
				if (erms.trim().length() == 0)
				{
					erms = MFSBCBarcodeDecoder.INVALID_BARCODE_ERMS;
				}
				IGSMessageBox.showOkMB(this, null, erms, null);
			}	
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(this, null, null, e);
		}
		this.toFront();
		this.tfInput.setText(""); //$NON-NLS-1$
	}
	
	/** Removes the selected element from the list. */
	private void removeFromList() 
	{
		this.fieldCmntListModel.remove(this.lstCmnt.getSelectedIndex());
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
			if (source == this.pbCancel)
			{
				this.dispose();
			}
			else if (source == this.pbAdd)
			{
				processAddButton();
			}
			else if (source == this.pbRemove)
			{
				removeFromList();
			}
			else if (source == this.pbFinish)
			{
				processFinBut();
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
			if(source == this.tfInput)
			{
				recvInput();
			}
			else if(source == this.pbCancel)
			{
				this.pbCancel.requestFocusInWindow();
				this.pbCancel.doClick();
			}
		}
		else if(keyCode == KeyEvent.VK_ESCAPE)
		{
			this.pbCancel.requestFocusInWindow();
			this.pbCancel.doClick();
		}
	}

	/**
	 * Invoked the first time a window is made visible.
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
