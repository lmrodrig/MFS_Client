/* @ Copyright IBM Corporation 2011. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2011-06-10      50244JR  Edgar Mercado    - Initial version
 ******************************************************************************/

package com.ibm.rchland.mfgapps.mfsclient.dialog;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.ibm.rchland.mfgapps.client.utils.messagebox.IGSMessageBox;
import com.ibm.rchland.mfgapps.mfsclient.MFSActionableDialog;
import com.ibm.rchland.mfgapps.mfsclient.MFSFrame;
import com.ibm.rchland.mfgapps.mfscommon.MFSConfig;
import com.ibm.rchland.mfgapps.mfscommon.comm.trx.RTVTEARCONF;

public class MFSTearConfirmationDialog extends MFSActionableDialog {
	private static final long serialVersionUID = 1L;	
	private String inputMCTL;
	
	/** The Yes <code>JButton</code>. */
	private JButton pbYes = createButton("Yes",'Y');

	/** The No <code>JButton</code>. */
	private JButton pbNo = createButton("No",'N');
	
	/** Set <code>true</code> if the enter button is pressed. */
	private boolean fieldPressedYes = false;
	
	private String prodid;
	
	private String pdes;
	
	private String pn;
	
	private String sn;
	
	private int rc;
	

	public MFSTearConfirmationDialog(MFSFrame parent, String mctl) {
		
	    super(parent, "Tear Down Confirmation");		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setInputMCTL(mctl);
		rtvTearConfirmationData();
		
		if (this.rc == 0)
		{
			createLayout();
			addMyListeners();					
		}
		else
		{
			this.dispose();
		}

	}
	
	public void setInputMCTL(String inputMCTL) {
		this.inputMCTL = inputMCTL;
	}	
	
	public int getErrorCode() {
		return this.rc;
	}	
	
	private void rtvTearConfirmationData(){	
		final MFSConfig config = MFSConfig.getInstance();
		RTVTEARCONF rtvtearconf = new RTVTEARCONF(this);
		rtvtearconf.setInputMCTL(this.inputMCTL);
		rtvtearconf.setInputAPP("MFS");
		rtvtearconf.setInputPGM("MFSCLIENT");
		rtvtearconf.setInputUSER(config.get8CharUser());
		rtvtearconf.setInputCELL(config.get8CharCellType());
		rtvtearconf.execute();
		
		this.rc = rtvtearconf.getReturnCode();
		if (this.rc == 0)
		{
			this.prodid = rtvtearconf.getOutputPROD();
			this.pdes = rtvtearconf.getOutputPIDS();
			this.pn = rtvtearconf.getOutputPN();
			this.sn = rtvtearconf.getOutputSN();			
		}
		else
		{
			IGSMessageBox.showEscapeMB(this, null, rtvtearconf.getErrorMessage(), null, false);			
		}
	}

	protected void createLayout(){
		JLabel wuLabel = createLabel("Work Unit: ");
		JLabel prodidLabel = createLabel("Product ID: ");
		JLabel pdesLabel = createLabel("Description: ");
		JLabel pnLabel = createLabel("Part Number: ");
		JLabel snLabel = createLabel("Serial Number: " );
		JLabel cfLabel = createLabel("Please confirm you want to tear down this work unit");
		JLabel vlwu = createValueLabel(wuLabel);
		JLabel vlprod = createValueLabel(prodidLabel);
		JLabel vlpdes = createValueLabel(pdesLabel);
		JLabel vlpn = createValueLabel(pnLabel);
		JLabel vlsn = createValueLabel(snLabel);
						
		JPanel labelPanel = new JPanel(new GridLayout(5, 2, 5, 5));
		
		wuLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		labelPanel.add(wuLabel);
		vlwu.setText(this.inputMCTL);
		labelPanel.add(vlwu);
		
		prodidLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		labelPanel.add(prodidLabel);
		vlprod.setText(this.prodid);		
		labelPanel.add(vlprod);
		
		pdesLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		labelPanel.add(pdesLabel);
		vlpdes.setText(this.pdes);
		labelPanel.add(vlpdes);
		
		pnLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		labelPanel.add(pnLabel);
		
		if (this.pn.trim().equals(""))
			vlpn.setText("N/A");				
		else
			vlpn.setText(this.pn);	
		
		labelPanel.add(vlpn);			

		snLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		labelPanel.add(snLabel);
		
		if (this.sn.trim().equals(""))
			vlsn.setText("N/A");					
		else
			vlsn.setText(this.sn);					
		
		labelPanel.add(vlsn);
		
		JPanel labelPanelcf = new JPanel(new GridLayout(1, 1, 0, 0));		
		
		labelPanelcf.add(cfLabel);
		
		JPanel contentPane = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 2, 1, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, 
				new Insets(40, 15, 10, 15), 0, 0);

		gbc.gridy = 0;
		gbc.insets = new Insets(20, 15, 10, 15);
		contentPane.add(labelPanel, gbc);

		gbc.gridy = 1;
		gbc.insets = new Insets(20, 15, 10, 15);
		contentPane.add(labelPanelcf, gbc);
		
		gbc.gridy = 2;
		gbc.gridwidth = 1;
		gbc.insets = new Insets(20, 20, 10, 25);
		
		contentPane.add(this.pbYes, gbc);

		gbc.gridx = 1;
		contentPane.add(this.pbNo, gbc);

		setContentPane(contentPane);
		
		setButtonDimensions(this.pbYes, this.pbNo);
		pack();
	}
	
	/** Adds the listeners to this dialog's <code>Component</code>s. */
	protected void addMyListeners()	{	
		this.pbYes.addActionListener(this);
		this.pbNo.addActionListener(this);

		this.pbYes.addKeyListener(this);
		this.pbNo.addKeyListener(this);		
	}
	
	public void setYesButtonPressed()
	{
		this.fieldPressedYes = true;
		this.dispose();
	}	
	
	/**
	 * Invoked when an action occurs.
	 * @param ae the <code>ActionEvent</code>
	 */
	public void actionPerformed(ActionEvent ae)
	{
		final Object source = ae.getSource();
		if (source == this.pbYes)
		{			
			setYesButtonPressed();
		}
		else if (source == this.pbNo)
		{			
			this.dispose();
		}
	}
	
	/**
	 * Returns <code>true</code> iff the enter button was pressed.
	 * @return <code>true</code> iff the enter button was pressed
	 */
	public boolean getPressedYes()
	{
		return this.fieldPressedYes;
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
			if (source == this.pbYes)
			{				
				this.pbYes.requestFocusInWindow();
				this.pbYes.doClick();
			}
			else if (source == this.pbNo)
			{			
				this.pbNo.requestFocusInWindow();
				this.pbNo.doClick();
			}
		}
		else if (keyCode == KeyEvent.VK_ESCAPE)
		{		
			this.pbNo.requestFocusInWindow();			
			this.pbNo.doClick();
		}
	}	
	
}
