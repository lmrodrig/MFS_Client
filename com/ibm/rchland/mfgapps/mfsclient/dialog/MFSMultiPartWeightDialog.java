/* © Copyright IBM Corporation 2015. All rights reserved.
 * 
 * This class is the dialog that is displayed from the direct work 
 * screen on the MFS Client.  It displays the part(s) returned by 
 * the RTV_PRTWGT transaction that are missing from the FCSPPWT10 table
 * and allows the user to enter the weight for each part.
 * 
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2015-09-03       1385222 Andy Williams    -Initial version
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.dialog;


import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.ibm.rchland.mfgapps.client.utils.messagebox.IGSMessageBox;
import com.ibm.rchland.mfgapps.client.utils.xml.IGSXMLTransaction;
import com.ibm.rchland.mfgapps.mfsclient.MFSActionableDialog;
import com.ibm.rchland.mfgapps.mfsclient.MFSFrame;
import com.ibm.rchland.mfgapps.mfscommon.MFSActionable;
import com.ibm.rchland.mfgapps.mfscommon.MFSConfig;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSComm;

/**
 * <code>MFSStandAlonePartWeightDialog</code> is the <code>MFSActionableDialog</code>
 * used to collect and update part weight information.
 * @author The MFS Client Development Team
 */
public class MFSMultiPartWeightDialog
	extends MFSActionableDialog
{
	private static final long serialVersionUID = 1L;
	
	/** The part number list <code>Vector</code>. */
	private Vector<String> partNumbers;

	/** The weight unit of measure <code>JLabel</code>. */
	private String weightUnit; 
	
	/** The weight unit of measure <code>JLabel</code>. */
	private String trxWeightUnit; 

	/** Set <code>true</code> when the cancel button is selected. */
	private boolean fieldCancelSelected = false;
	
	/** The main display <code>JPanel</code>. */
	private final JPanel cards = new JPanel(new CardLayout());
	
	private IGSXMLTransaction updtpwt10 = null;
		
	
	/**
	 * Constructs a new <code>MFSStandAlonePartWeightDialog</code>.
	 * @param parent the <code>MFSFrame</code> that caused this
	 *        <code>MFSStandAlonePartWeightDialog</code> to be displayed
	 * @param actionable the <code>MFSActionable</code> that caused this
	 *        <code>MFSStandAlonePartWeightDialog</code> to be displayed
	 */
	public MFSMultiPartWeightDialog(MFSFrame parent, MFSActionable actionable, Vector<String> partNumbers)
	{
		super(parent, "The following parts need weight values:");
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
	    setPartNumbers(partNumbers);
	    weightUnit = determineUnitOfMeasure();
		createLayout();
	}

	/** Adds this dialog's <code>Component</code>s to the layout. */
	private void createLayout()
	{
		//this.setPreferredSize(new Dimension(400,250));
		populateCards();
		cards.setPreferredSize(new Dimension(420,250));
		this.getContentPane().add(cards);
		
		JPanel control = new JPanel();
        control.setLayout(new BoxLayout(control, BoxLayout.PAGE_AXIS));
        JPanel leftRightControls = new JPanel();
        leftRightControls.add(new JButton(new AbstractAction("\u22b2 Prev Part") {

            @Override
            public void actionPerformed(ActionEvent e) {
            	Component[] theCards = cards.getComponents();
            	String componentName = "";
            	JPanel card = null;
            	JPanel weightPanel = null;
                CardLayout cl = (CardLayout) cards.getLayout();
                cl.previous(cards);
                
                
                for(Component comp: theCards)
                {
                	if(comp.isVisible())
                	{
                		card = (JPanel)comp;
                		/* Loop through the components inside the partCard panel to find the part weight text field */
                		for(int partCardIndex = 0; partCardIndex<card.getComponentCount(); partCardIndex++)
                		{
                			componentName = card.getComponent(partCardIndex).getName();		
	            			if(componentName != null && componentName.equals("partWeightField"))
	            			{
	            				/* partWeightField is a JTextField */
	            				JTextField weightField = (JTextField)card.getComponent(partCardIndex);
	            				weightField.requestFocus();
	            			}	
                		}
                	}
                }
            }
        }));
        leftRightControls.add(new JButton(new AbstractAction("Next Part \u22b3") {

            @Override
            public void actionPerformed(ActionEvent e) {
            	Component[] theCards = cards.getComponents();
            	String componentName = "";
            	JPanel card = null;
                CardLayout cl = (CardLayout) cards.getLayout();
                cl.next(cards);
                
                
                for(Component comp: theCards)
                {
                	if(comp.isVisible())
                	{
                		card = (JPanel)comp;
                		/* Loop through the components inside the partCard panel to find the part weight text field */
                		for(int partCardIndex = 0; partCardIndex<card.getComponentCount(); partCardIndex++)
                		{
                			componentName = card.getComponent(partCardIndex).getName();		
	            			if(componentName != null && componentName.equals("partWeightField"))
	            			{
	            				/* partWeightField is a JTextField */
	            				JTextField weightField = (JTextField)card.getComponent(partCardIndex);
	            				weightField.requestFocus();
	            				
	            			}	
                		}
                	}
                }
            }
        }));
        JPanel submitCancelControls = new JPanel();
        submitCancelControls.add(new JButton (new AbstractAction("Submit") {

			public void actionPerformed(ActionEvent e) {
            	String partNumber = "";
            	String weight = "";
            	String componentName = "";
            	double fWeight = 0.00;
            	JPanel partCard = null;
            	boolean sendMessage = false;
            	Vector<String> partsWithWeights = new Vector<String>();
            	startXML();
            	for(int i=0; i<cards.getComponentCount(); i++)
            	{
            		partCard = (JPanel)cards.getComponent(i);
            		partNumber = partCard.getName();

            		/* Loop through the components inside the partCard panel to find the part weight text field */
            		for(int partCardIndex = 0; partCardIndex<partCard.getComponentCount(); partCardIndex++)
            		{
            			componentName = partCard.getComponent(partCardIndex).getName();
            			if(componentName != null && componentName.equals("partWeightField"))
            			{
            				
            				/* partWeightField is a JTextField */
            				JTextField weightField = (JTextField)partCard.getComponent(partCardIndex);
            				
            				/* Retrieve the text field's value */
            				weight = weightField.getText().trim();
            				if(!weight.equals(""))
            				{
            					sendMessage = true;  // We found a part with a weight entered, so we need to call the trx
	            				try
	            				{
	            					fWeight = Double.valueOf(weight);
	            					
	            					if(fWeight>9999.99 || fWeight<0.01)
	            					{
	            						IGSMessageBox.showOkMB(getParentFrame(), "Number Format Exception", "Weight must be between 0.01 and 9999.99 !", null);
	            						return;
	            					}
	            					else
	            					{	
	            						updtpwt10.startElement("PART"); //$NON-NLS-1$
	            						updtpwt10.addElement("INPN", partNumber); //$NON-NLS-1$
	            						updtpwt10.addElement("WEIGHT", String.valueOf(fWeight)); //$NON-NLS-1$
	            						updtpwt10.endElement("PART"); //$NON-NLS-1$
	            						partsWithWeights.add(partNumber);
	            					}
	            				}
	            				catch(NumberFormatException nfe)
	            				{
	            					IGSMessageBox.showOkMB(getParentFrame(), "Number Format Exception", "Weight must be a number!", null);
	            					return;
	            				}
	            				System.out.println("Part: " + partNumber + "  Weight: " + weight);
            				}
            			}
            		}
            	}
            	if(sendMessage)
            	{
            		if(endXML() == 0)
            		{
            			removeUpdatedParts(partsWithWeights);
            		}
            		
            	}
            	else
            	{
            		IGSMessageBox.showOkMB(getParentFrame(), "No weights entered", "At least one weight must be entered!", null);
            		updtpwt10 = null;
            	}
            }
        }));
        submitCancelControls.add(new JButton(new AbstractAction("Defer"){

            @Override
            public void actionPerformed(ActionEvent e) {
            	destroy();
            }
        }));
        control.add(leftRightControls);
        control.add(submitCancelControls);
        this.add(cards, BorderLayout.CENTER);
        this.add(control, BorderLayout.SOUTH);
		
	}
	
	
	/**
	 * Destroys the dialog.
	 */
	public void destroy()
	{
		this.setVisible(false);
		this.dispose();
	}


	/**
	 * Returns <code>true</code> if the cancel button was selected.
	 * @return <code>true</code> if the cancel button was selected
	 */
	public boolean getCancelSelected()
	{
		return this.fieldCancelSelected;
	}

	public Vector<String> getPartNumbers() {
		return partNumbers;
	}

	public void setPartNumbers(Vector<String> partNumbers) {
		this.partNumbers = partNumbers;
	}
	
	private String determineUnitOfMeasure()
	{
		if(!MFSConfig.getInstance().getConfigValue("PARTWEIGHTUOM").trim().equals("Not Found"))
		{
			String uom = MFSConfig.getInstance().getConfigValue("PARTWEIGHTUOM").trim().toUpperCase();
			if(uom.equals("LB"))
			{
				trxWeightUnit = "LB";
				return "Lbs";
			}
			else
			{
				trxWeightUnit = "KG";
				return "Kg";
			}
		}
		else
		{
			trxWeightUnit = "KG";
			return "Kg";
		}
	}
	
	private void startXML()
	{
		updtpwt10 = new IGSXMLTransaction("UPD_PWT10"); //$NON-NLS-1$
		updtpwt10.setActionMessage("Updating Part Weight(s)..."); //$NON-NLS-1$
		updtpwt10.startDocument();
		updtpwt10.addElement("USER", MFSConfig.getInstance().get8CharUser().trim()); //$NON-NLS-1$
		updtpwt10.addElement("UOM", trxWeightUnit); //$NON-NLS-1$
		updtpwt10.startElement("PARTS"); //$NON-NLS-1$
	}
	
	private int endXML()
	{
		updtpwt10.endElement("PARTS"); //$NON-NLS-1$
		updtpwt10.endDocument();
		System.out.println(updtpwt10.toString());
		MFSComm.getInstance().execute(updtpwt10, this);
		
		if (updtpwt10.getReturnCode() == 0)
		{
			String title = "Update Successful";
			IGSMessageBox.showOkMB(this, title, "Update was successful!", null);
			return 0;
		}
		else
		{
			String title = "Update Error";
			IGSMessageBox.showOkMB(this, title, updtpwt10.getErms(), null);
			return 1;
		}
	}
	
	private void removeUpdatedParts(Vector<String> parts)
	{
		for(String partNumber: parts)
		{
			/* Remove the part number from the partNumbers Vector */
			partNumbers.remove(partNumber);
		}
		parts.clear();
		
		/* Remove all partCards from the cards panel */
		for(int cardIndex = cards.getComponentCount()-1; cardIndex>=0; cardIndex--)
		{
			System.out.println("Removing " + cardIndex);
			cards.remove(cardIndex);
		}
		
		/* Add partCards back into the cards panel if there are any*/
		if(partNumbers.size()>0)
		{
			populateCards();	
		}
		/* Otherwise close the dialog */
		else
		{
			MFSConfig.getInstance().setConfigValue("WEIGHTSCOMPLETE", "TRUE");
			this.destroy();
		}
	}
	
	private void populateCards()
	{
		int currentIndex = 0;
		
		for(String partNumber : partNumbers)
		{
			currentIndex++;
			JPanel partCard = new JPanel(new GridBagLayout()); // The main card that will be added to the cards JPanel
			partCard.setName(partNumber);
        	GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 0, 1.0,
    				GridBagConstraints.CENTER, GridBagConstraints.NONE, 
    				new Insets(10, 0, 10, 10), 0, 0);
        	
        	/* Create the "Part x of y" label */ 
        	gbc.gridy = 0;
    		gbc.gridwidth = 3;
    		gbc.insets.left=20;
			JLabel partNumofNum = new JLabel("Part " + currentIndex + " of " + partNumbers.size());
			partCard.add(partNumofNum, gbc);
			
			gbc.gridy=1;
    		gbc.gridwidth = 3; 
    		gbc.insets.left=10;
			JLabel lblPartNumberInst = new JLabel("Please enter a weight for the following part:");
			partCard.add(lblPartNumberInst, gbc); 
			
			JLabel lblPartNumber = new JLabel("Part Number: " + partNumber);
			gbc.gridx=0;
			gbc.gridy=2;
    		gbc.gridwidth = 3;
    		gbc.anchor = GridBagConstraints.WEST;
    		gbc.insets.left=20;
    		gbc.insets.top = 20;
    		gbc.insets.bottom = 0;
        	partCard.add(lblPartNumber,gbc); 
    		
        	gbc.gridx=0;
    		gbc.gridy++;
    		gbc.gridwidth = 1;
    		gbc.insets.top = 0;
    		gbc.insets.bottom = 20;
        	partCard.add(new JLabel("Weight: "),gbc);
        		
        	gbc.gridx++;
    		gbc.gridwidth = 1;
        	JTextField partWeight = new JTextField("");
        	partWeight.setName("partWeightField");
        	partWeight.setSize(new Dimension(100,20));
        	partWeight.setPreferredSize(new Dimension(100,20));
    		partCard.add(partWeight,gbc);
    		
    		gbc.gridx++;
    		gbc.gridwidth = 1;
        	partCard.add(new JLabel(weightUnit),gbc);
        	
        	partCard.validate();
			cards.add(partCard,partNumber);
		}
		((CardLayout)cards.getLayout()).first(cards);
	}
	
	public void display()
	{
		pack();
		setLocationRelativeTo(getParentFrame());
		setVisible(true); 
	}
}
