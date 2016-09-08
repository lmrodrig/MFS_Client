/* © Copyright IBM Corporation 2005, 2007. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2007-01-25      34242JR  R Prechel        -Java 5 version
 * 2007-04-02   ~1 38166JM  R Prechel        -Add setLocationRelativeTo for dialog
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.dialog;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import mfsxml.MfsXMLDocument;
import mfsxml.MfsXMLParser;

import com.ibm.rchland.mfgapps.client.utils.messagebox.IGSMessageBox;
import com.ibm.rchland.mfgapps.mfsclient.MFSActionableDialog;
import com.ibm.rchland.mfgapps.mfsclient.MFSFrame;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSConstants;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSComm;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSTransaction;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSXmlTransaction;

/**
 * <code>MFSRtvWatrNmbrDialog</code> is the <code>MFSActionableDialog</code>
 * used to call the RTV_WU10 transaction.
 * @author The MFS Client Development Team
 */
public class MFSRtvWatrNmbrDialog
	extends MFSActionableDialog
{
	private static final long serialVersionUID = 1L;
	/** The WATR input <code>JTextField</code>. */
	private JTextField tfInputWatr = createTextField(MFSConstants.MEDIUM_TF_COLS, 0);
	
	/** The NMBR input <code>JTextField</code>. */
	private JTextField tfInputNmbr = createTextField(MFSConstants.SMALL_TF_COLS, 0);

	/** The Retrieve <code>JButton</code>. */
	private JButton pbRetrieve = createButton("F2 - Retrieve");

	/** The Cancel <code>JButton</code>. */
	private JButton pbCancel = createButton("Cancel", 'n');
	
	/**
	 * Constructs a new <code>MFSRtvWatrNmbrDialog</code>.
	 * @param parent the <code>MFSFrame</code> that caused this
	 *        <code>MFSRtvWatrNmbrDialog</code> to be displayed
	 */
	public MFSRtvWatrNmbrDialog(MFSFrame parent)
	{
		super(parent, "Display Wip Driver Ready Wk Units");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		createLayout();
		addMyListeners();
	}
	
	/** Adds this dialog's <code>Component</code>s to the layout. */
	private void createLayout()
	{
		JPanel contentPane = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(30, 10, 0, 10), 10, 0);

		
		contentPane.add(createNameLabel("WATR"), gbc);
		gbc.gridy = 1;
		contentPane.add(createNameLabel("NMBR"), gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.ipadx = 0;
		
		contentPane.add(this.tfInputWatr, gbc);
		gbc.gridy = 1;
		contentPane.add(this.tfInputNmbr, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.weighty = 0.0;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(40, 10, 50, 10);
		contentPane.add(this.pbRetrieve, gbc);

		gbc.gridx = 1;
		contentPane.add(this.pbCancel, gbc);

		setContentPane(contentPane);
		setButtonDimensions(this.pbRetrieve, this.pbCancel);
		pack();
	}

	/** Adds the listeners to this dialog's <code>Component</code>s. */
	private void addMyListeners()
	{
		this.pbRetrieve.addActionListener(this);
		this.pbCancel.addActionListener(this);
		
		this.pbRetrieve.addKeyListener(this);
		this.pbCancel.addKeyListener(this);
		this.tfInputWatr.addKeyListener(this);
		this.tfInputNmbr.addKeyListener(this);
	}
	
	/** Clears the text fields and requests the focus for the top text field. */
	private void clear()
	{
		this.tfInputWatr.setText(""); //$NON-NLS-1$
		this.tfInputNmbr.setText(""); //$NON-NLS-1$
		this.tfInputWatr.requestFocusInWindow();
	}
	
	/** Invoked when {@link #pbRetrieve} is selected. Calls RTV_WU10. */
	private void display() 
	{
		try
		{
	 		MfsXMLDocument xml_data1 = new MfsXMLDocument("RTV_WU10"); //$NON-NLS-1$
			xml_data1.addCompleteField("BACK","WUMCTL"); //$NON-NLS-1$ //$NON-NLS-2$
			xml_data1.addOpenTag("DATA"); //$NON-NLS-1$
			xml_data1.addOpenTag("FLD"); //$NON-NLS-1$
			xml_data1.addCompleteField("NAME","WUWATR"); //$NON-NLS-1$ //$NON-NLS-2$
	    	xml_data1.addCompleteField("VALU",this.tfInputWatr.getText().trim().toUpperCase()); //$NON-NLS-1$    	
			xml_data1.addCloseTag("FLD"); //$NON-NLS-1$
			xml_data1.addOpenTag("FLD"); //$NON-NLS-1$
			xml_data1.addCompleteField("NAME","WUNMBR"); //$NON-NLS-1$ //$NON-NLS-2$
	    	xml_data1.addCompleteField("VALU",this.tfInputNmbr.getText().trim().toUpperCase()); //$NON-NLS-1$
			xml_data1.addCloseTag("FLD"); //$NON-NLS-1$
			xml_data1.addCloseTag("DATA");	 //$NON-NLS-1$
			xml_data1.finalizeXML();
			
			MFSTransaction rtv_wu10 = new MFSXmlTransaction(xml_data1.toString());
			rtv_wu10.setActionMessage("Retrieving Wk Units, Please Wait...");
			MFSComm.getInstance().execute(rtv_wu10, this);	

			if (rtv_wu10.getReturnCode() == 0)
			{	
				MfsXMLParser xmlParser = new MfsXMLParser(rtv_wu10.getOutput());
				if (xmlParser.getField("RCD").equals(""))
				{
					String erms = "No records found for specified WATR/NMBR";
					IGSMessageBox.showOkMB(this, null, erms,null);
					this.toFront();
				}
				else
				{
					String nameString = xmlParser.getField("NAME"); //$NON-NLS-1$
					String valuString = xmlParser.getField("VALU"); //$NON-NLS-1$
					StringBuffer returnString = new StringBuffer(); //$NON-NLS-1$
					returnString.append(valuString);
					while(!(nameString.equals(""))) //$NON-NLS-1$
					{
				 		nameString = xmlParser.getNextField("NAME"); //$NON-NLS-1$
	  		  		    valuString = xmlParser.getNextField("VALU"); //$NON-NLS-1$
			 	  		returnString.append(valuString);
					}

					MFSDspWIPWUDialog myDspWIPWUD = new MFSDspWIPWUDialog(getParentFrame(), this.tfInputWatr.getText(), this.tfInputNmbr.getText());
					myDspWIPWUD.loadwipWUListModel(returnString.toString());
					myDspWIPWUD.setLocationRelativeTo(getParentFrame()); //~1A
					myDspWIPWUD.setVisible(true);		
				}
			}	
			else 
			{
				IGSMessageBox.showOkMB(this, null, rtv_wu10.getErms(), null);
				this.toFront();
			}
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(this, null, null, e);
			this.toFront();
		}
		clear();
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
			if(source == this.pbCancel)
			{
				dispose();
			}
			else if (source == this.pbRetrieve)
			{
				display();
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
		if(keyCode == KeyEvent.VK_ENTER)
		{
			final Object source = ke.getSource();
			if(source == this.pbRetrieve || source == this.tfInputNmbr)
			{
				this.pbRetrieve.requestFocusInWindow();
				this.pbRetrieve.doClick();
			}
			else if(source == this.pbCancel)
			{
				this.pbCancel.requestFocusInWindow();
				this.pbCancel.doClick();
			}
		}
		else if(keyCode == KeyEvent.VK_F2)
		{
			this.pbRetrieve.requestFocusInWindow();
			this.pbRetrieve.doClick();
		}
		else if (keyCode == KeyEvent.VK_ESCAPE)
		{
			this.pbCancel.requestFocusInWindow();
			this.pbCancel.doClick();
		}
	}
	
	/**
	 * Invoked the first time a window is made visible.
	 * Requests the focus for the {@link #tfInputWatr}.
	 * @param we the <code>WindowEvent</code>
	 */
	public void windowOpened(WindowEvent we)
	{
		if (we.getSource() == this)
		{
			this.tfInputWatr.requestFocusInWindow();
		}
	}
}
