/* @ Copyright IBM Corporation 2010. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2010-03-04      46810JL  J Mastachi       -Initial version.
 * 2010-04-01 ~01  48083JW  J Mastachi       -Fix for text fields not showing in 
 *                                            DBCS environments.
 * 2010-04-13 ~02  48519JW  J Mastachi       -Support new CRTWCARECS return codes.
 * 2010-04-14 ~03  48166JW  J Mastachi       -Check for blank MATP/MMDL tag on rc=0
 *                                            from CRTWCARECS.
 * 2010-04-16 ~04  48002BA  J Mastachi       -When print is started automatically, 
 *                                            cursor is not set to requested text
 *                                            field if data has to be manually
 *                                            entered.
 * 2010-05-18 ~05  48438JW  J Mastachi       -Check for FDES value in XML from 
 *                                            CRTWCARECS, if present do not request
 *                                            manual entry from user.
 * 2010-06-15 ~06  48456JL  J Mastachi       -Remember last PLOM value entered.
 * 2010-06-22 ~07  48456JL  J Mastachi       -Rework: If a PLOM value is found in 
 *                                            file plom.xml, do not show PLOM 
 *                                            selection dialog again.
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.dialog;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedInputStream; //~06A
import java.io.File; //~06A
import java.io.FileInputStream; //~06A
import java.io.FileWriter; //~06A
import java.io.InputStream; //~06A
import java.io.PrintWriter; //~06A

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

import mfsxml.MISSING_XML_TAG_EXCEPTION; //~06A
import mfsxml.MfsXMLParser;

import com.ibm.rchland.mfgapps.client.utils.messagebox.IGSMessageBox;
import com.ibm.rchland.mfgapps.client.utils.xml.IGSXMLTransaction;
import com.ibm.rchland.mfgapps.mfsclient.MFSActionableDialog;
import com.ibm.rchland.mfgapps.mfsclient.MFSFrame;
import com.ibm.rchland.mfgapps.mfsclient.barcode.MFSBCBarcodeDecoder;
import com.ibm.rchland.mfgapps.mfsclient.barcode.MFSBCIndicatorValue;
import com.ibm.rchland.mfgapps.mfsclient.barcode.MFSBCPartObject;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSConstants;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSPrintingMethods;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFStfParser;
import com.ibm.rchland.mfgapps.mfscommon.MFSConfig;

/**
 * <code>MFSWarrantyCardJapanDialog</code> is displayed when the Japan Warranty
 * Card Mode button is selected on <code>MFSWarrantyCardModeDialog</code>.
 * @author The MFS Client Development Team
 */
public class MFSWarrantyCardJapanDialog extends MFSActionableDialog implements DocumentListener
{
	private static final long serialVersionUID = 1L;
	/** Return code from CRTWCARECS for no FFBM Description found. */
	private final int NO_FDES_FOUND = 1;  //~05C
	
	/** The Error message <code>JLabel</code>. */
	private JLabel errorLabel = new JLabel();
	
	/** The Warranty Card input <code>JTextField</code>. */
	private JTextField tfCard = createTextField(MFSConstants.LARGE_TF_COLS, 22);
	
	/** The Warranty Card PN value <code>JLabel</code>. */
	private JLabel vlCardPN = null;
	
	/** The FFBM input <code>JTextField</code>. */
	private JTextField tfFFBM = createTextField(MFSConstants.LARGE_TF_COLS, 22);
	
	/** The FFBM PN value <code>JLabel</code>. */
	private JLabel vlFFBMPN = null;
	
	/** The FFBM SN value <code>JLabel</code>. */
	private JLabel vlFFBMSN = null;
	
	/** The FFBM description input <code>JTextField</code>. */
	private JTextField tfFFBMDesc = createTextField(MFSConstants.LARGE_TF_COLS, 24);
	
	/** The FFBM MATP/MMDL input <code>JTextField</code>. */
	private JTextField tfMATPMMDL = createTextField(MFSConstants.LARGE_TF_COLS, 16);
	
	/** The FFBM Machine Serial input <code>JTextField</code>. */
	private JTextField tfMCSN = createTextField(MFSConstants.LARGE_TF_COLS, 7);
	
	/** The Reset <code>JButton</code>. */
	private JButton pbReset = createButton("Reset", 'R');
	
	/** The Print <code>JButton</code>. */
	private JButton pbPrint = createButton("Print", 'P');
	
	/** The Switch Modes <code>JButton</code>. */
	private JButton pbModes = createButton("Switch Modes", 'S');
	
	/** Set <code>true</code> if the Warranty Card PN is decoded. */
	private boolean cardPNfound = false;
	
	/** Set <code>true</code> if the FFBM PN is decoded. */
	private boolean ffbmPNfound = false;
	
	/** Set <code>true</code> if the FFBM SN is decoded. */
	private boolean ffbmSNfound = false;
	
	/** Set <code>true</code> if {@link #clear()} was performed.*/
	private boolean clearPerformed = false;
	
	/** Set <code>true</code> if focus requested for FFBM description field. */
	private boolean focus_on_tf_desc = false;  //~04A
	
	/** Set <code>true</code> if focus requested for MATP/MMDL field. */
	private boolean focus_on_tf_matp = false;  //~04A
	
	/** <code>String</code> to hold the MATP/MMDL value received from
	 *  CRTWCARECS to determine if it was edited. */
	private String rcvdMATPMMDL = new String();

	/**
	 * Constructs a new <code>MFSWarrantyCardJapanDialog</code>.
	 * @param parent the <code>MFSFrame</code> that caused this
	 *        <code>MFSWarrantyCardJapanDialog</code> to be displayed
	 */
	public MFSWarrantyCardJapanDialog(MFSFrame parent)
	{
		super(parent, "Japan Warranty Card Mode");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.tfFFBMDesc.setVisible(false);
		this.tfMATPMMDL.setVisible(false);
		this.tfMCSN.setVisible(false);
		this.tfMCSN.setEnabled(false);
		createLayout();
		addMyListeners();
	}

	/** Adds the listeners to this dialog's <code>Component</code>s. */
	private void addMyListeners()
	{
		this.addKeyListener(this);
		
		this.pbReset.addActionListener(this);
		this.pbReset.addKeyListener(this);
		this.pbPrint.addActionListener(this);
		this.pbPrint.addKeyListener(this);
		this.pbModes.addActionListener(this);
		this.pbModes.addKeyListener(this);
		
		this.tfCard.addKeyListener(this);
		this.tfFFBM.addKeyListener(this);
		this.tfFFBMDesc.addKeyListener(this);
		this.tfMATPMMDL.addKeyListener(this);
		this.tfMCSN.addKeyListener(this);
		
		this.tfMATPMMDL.getDocument().addDocumentListener(this);
		this.tfMATPMMDL.getDocument().putProperty("Name", "tfMATPMMDL");
	}

	/** Adds this dialog's <code>Component</code>s to the layout. */
	private void createLayout()
	{
		Border blueline = BorderFactory.createLineBorder(Color.blue);
		
		this.errorLabel.setForeground(Color.RED);
		this.errorLabel.setFont(MFSConstants.MEDIUM_DIALOG_FONT);
		
		JLabel titleLabel = createLabel("Japan Data Entry Mode");
		titleLabel.setForeground(MFSConstants.PRIMARY_FOREGROUND_HIGHLIGHT_COLOR);
		titleLabel.setFont(MFSConstants.LARGE_PLAIN_DIALOG_FONT);
		
		JLabel cardLabel = createSmallNameLabel("Scan Warranty Card:");
		cardLabel.setLabelFor(this.tfCard);
		
		JLabel cardNumLabel = createSmallNameLabel("Warranty Card PN:");
		if (this.vlCardPN == null)
		{
			this.vlCardPN = createValueLabel(cardNumLabel);
		}
		
		JLabel ffbmLabel = createSmallNameLabel("Scan FFBM:");
		ffbmLabel.setLabelFor(this.tfFFBM);
		
		JLabel ffbmNumLabel = createSmallNameLabel("FFBM PN:");
		if (this.vlFFBMPN == null)
		{
			this.vlFFBMPN = createValueLabel(ffbmNumLabel);
		}
		
		JLabel ffbmSerLabel = createSmallNameLabel("FFBM SN:");
		if (this.vlFFBMSN == null)
		{
			this.vlFFBMSN = createValueLabel(ffbmSerLabel);
		}
		
		JLabel ffbmDescLabel = createSmallNameLabel("FFBM Description:");
		ffbmDescLabel.setLabelFor(this.tfFFBMDesc);
		ffbmDescLabel.setVisible(this.tfFFBMDesc.isVisible());
		
		JLabel mcsnLabel = createSmallNameLabel("Machine SN:");
		mcsnLabel.setLabelFor(this.tfMCSN);
		mcsnLabel.setVisible(this.tfMCSN.isVisible());
		
		JLabel matpmmdlLabel = createSmallNameLabel("MATP/MMDL:");
		matpmmdlLabel.setLabelFor(this.tfMATPMMDL);
		matpmmdlLabel.setVisible(this.tfMATPMMDL.isVisible());
		
		JPanel cardPanel = new JPanel(new GridBagLayout());
		cardPanel.setBackground(MFSConstants.PRIMARY_BACKGROUND_COLOR);
		cardPanel.setBorder(blueline);
		
		GridBagConstraints gbcCard = new GridBagConstraints();
		gbcCard.insets = new Insets(10, 10, 5, 5);
		gbcCard.anchor = GridBagConstraints.EAST;
		
		gbcCard.gridy = 0;
		cardPanel.add(cardLabel, gbcCard);
		gbcCard.gridy++;
		gbcCard.insets.top = 5;
		gbcCard.insets.bottom = 10;
		cardPanel.add(cardNumLabel, gbcCard);
		
		gbcCard.gridx = 1;
		gbcCard.insets = new Insets(10, 5, 10, 10);
		gbcCard.anchor = GridBagConstraints.WEST;
		
		gbcCard.gridy = 0;
		cardPanel.add(this.tfCard, gbcCard);
		gbcCard.gridy++;
		gbcCard.insets.top = 5;
		gbcCard.insets.bottom = 10;
		cardPanel.add(this.vlCardPN, gbcCard);
		
		JPanel ffbmPanel = new JPanel(new GridBagLayout());
		ffbmPanel.setBackground(MFSConstants.PRIMARY_BACKGROUND_COLOR);
		ffbmPanel.setBorder(blueline);
		
		GridBagConstraints gbcFFBM = new GridBagConstraints();
		gbcFFBM.insets = new Insets(10, 10, 5, 5);
		gbcFFBM.anchor = GridBagConstraints.EAST;
		
		gbcFFBM.gridy = 0;
		ffbmPanel.add(ffbmLabel, gbcFFBM);
		gbcFFBM.gridy++;
		gbcFFBM.insets.top = 5;
		ffbmPanel.add(ffbmNumLabel, gbcFFBM);
		gbcFFBM.gridy++;
		ffbmPanel.add(ffbmSerLabel, gbcFFBM);
		gbcFFBM.gridy++;
		ffbmPanel.add(ffbmDescLabel, gbcFFBM);  //~04C
		gbcFFBM.gridy++;
		ffbmPanel.add(matpmmdlLabel, gbcFFBM);  //~04C
		gbcFFBM.gridy++;
		gbcFFBM.insets.bottom = 10;
		ffbmPanel.add(mcsnLabel, gbcFFBM);      //~04C
		
		gbcFFBM.gridx = 1;
		gbcFFBM.insets = new Insets(10, 5, 10, 10);
		gbcFFBM.anchor = GridBagConstraints.WEST;
		
		gbcFFBM.gridy = 0;
		ffbmPanel.add(this.tfFFBM, gbcFFBM);
		gbcFFBM.gridy++;
		gbcFFBM.insets.top = 5;
		ffbmPanel.add(this.vlFFBMPN, gbcFFBM);
		gbcFFBM.gridy++;
		ffbmPanel.add(this.vlFFBMSN, gbcFFBM);
		gbcFFBM.gridy++;
		ffbmPanel.add(this.tfFFBMDesc, gbcFFBM);   //~04C
		gbcFFBM.gridy++;
		ffbmPanel.add(this.tfMATPMMDL, gbcFFBM);   //~04C
		gbcFFBM.gridy++;
		gbcFFBM.insets.bottom = 10;
		ffbmPanel.add(this.tfMCSN, gbcFFBM);       //~04C
		
		JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 10, 0));
		buttonPanel.setBackground(MFSConstants.PRIMARY_BACKGROUND_COLOR);
		
		buttonPanel.add(this.pbModes);
		buttonPanel.add(this.pbReset);
		buttonPanel.add(this.pbPrint);
		
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
		gbc.insets.top = 5;
		gbc.insets.bottom = 5;
		contentPane.add(cardPanel, gbc);
		
		gbc.gridy++;
		contentPane.add(ffbmPanel, gbc);
		
		gbc.gridy++;
		gbc.insets.top = 10;
		gbc.insets.bottom = 20;
		contentPane.add(buttonPanel, gbc);
		
		setContentPane(contentPane);
		pack();
		this.setLocationRelativeTo(this.getParentFrame()); //~01A
	}
	
	/** Print Warranty Card action. */
	private void print()
	{
		int rc = 0;
		StringBuffer label_data = new StringBuffer();
		StringBuffer label_trigger = new StringBuffer();
		StringBuffer plom = new StringBuffer();
		boolean focus_on_tf_card = false;
		boolean focus_on_tf_ffbm = false;
		this.focus_on_tf_desc = false;  //~04C
		this.focus_on_tf_matp = false;  //~04C
		boolean focus_on_tf_mcsn = false;
		this.errorLabel.setText("");
		
		try
		{
			if (!this.cardPNfound)
			{
				this.errorLabel.setText("Warranty Card Part Number is a required field.");
				focus_on_tf_card = true;
				rc = 1;
			}
			else if (!this.ffbmPNfound)
			{
				this.errorLabel.setText("FFBM Part Number is a required field.");
				focus_on_tf_ffbm = true;
				rc = 1;
			}
			else if (!this.ffbmSNfound)
			{
				this.errorLabel.setText("FFBM Serial Number is a required field.");
				focus_on_tf_ffbm = true;
				rc = 1;
			}
			else if (this.tfFFBMDesc.isVisible() && this.tfFFBMDesc.getText().equals(""))
			{
				this.errorLabel.setText("FFBM Description is a required field.");
				this.focus_on_tf_desc = true; //~04C
				rc = 1;
			}
			else if (this.tfMATPMMDL.isVisible() && this.tfMATPMMDL.getText().length() != 7)
			{
				this.errorLabel.setText("MATP/MMDL value must be 7 characters in length.");
				this.focus_on_tf_matp = true; //~04C
				rc = 1;
			}
			else if (this.tfMCSN.isEnabled())
			{
				if (!this.tfMATPMMDL.getText().equals(this.rcvdMATPMMDL))
				{
					if (this.tfMCSN.getText().equals(""))
					{
						this.errorLabel.setText("Machine SN is required if the default MATP/MMDL is not used.");
						focus_on_tf_mcsn = true;
						rc = 1;
					}
					else if (this.tfMCSN.getText().length() != 7)
					{
						this.errorLabel.setText("Machine SN value must be 7 characters in length.");
						focus_on_tf_mcsn = true;
						rc = 1;
					}
				}
			}

			if (rc == 0)
			{
				IGSXMLTransaction crtwcarecs = new IGSXMLTransaction("CRTWCARECS");
				
				crtwcarecs.startDocument();
				crtwcarecs.addElement("WCPN", this.vlCardPN.getText().toUpperCase().trim());
				crtwcarecs.addEmptyElement("WCSN");
				crtwcarecs.addElement("FPRT", this.vlFFBMPN.getText().toUpperCase().trim());
				crtwcarecs.addElement("FSER", this.vlFFBMSN.getText().toUpperCase().trim());
				if (!this.tfFFBMDesc.getText().equals(""))
				{
					crtwcarecs.addElement("FDES", this.tfFFBMDesc.getText().trim());
				}
				if (this.tfMATPMMDL.isVisible())
				{
					crtwcarecs.addElement("MATPMMDL", this.tfMATPMMDL.getText().toUpperCase().trim());
				}
				else
				{
					crtwcarecs.addEmptyElement("MATPMMDL");
				}
				if (this.tfMCSN.isVisible())
				{
					crtwcarecs.addElement("MCSN", this.tfMCSN.getText().toUpperCase().trim());
				}
				else
				{
					crtwcarecs.addEmptyElement("MCSN");
				}
				crtwcarecs.addEmptyElement("KEYPARTS");
				crtwcarecs.addElement("USER", MFSConfig.getInstance().getConfigValue("USER"));
				crtwcarecs.addElement("DEMD", "JP");
				crtwcarecs.endDocument();
				
				crtwcarecs.setActionMessage("Verifying Warranty Card to FFBM association, please wait...");
				crtwcarecs.run();
				rc = crtwcarecs.getReturnCode();
				
				if (rc == 0)
				{
					// ~03A BEGIN
					MfsXMLParser parser = new MfsXMLParser(crtwcarecs.getOutput());
					File plom_file = null; //~06A
					String retval = new String(); //~06C
					String prcplom = new String(); //~06A
					boolean saveplomfile = false; //~07A
					
					if (this.tfMATPMMDL.getText().trim().equals("") && parser.fieldExists("MATPMMDL"))
					{
						if (parser.getFieldOnly("MATPMMDL").trim().equals(""))
						{
							this.errorLabel.setText("Default MATP/MMDL rule not set. Please enter MATP/MMDL and Machine SN manually.");
							this.tfMATPMMDL.setText("");
							this.tfMCSN.setText("");
							this.tfMATPMMDL.setVisible(true);
							this.tfMCSN.setVisible(true);
							this.tfMCSN.setEnabled(true);
							this.focus_on_tf_matp = true;  //~04C
							rc = 1;
						}
						else
						{
							this.tfMATPMMDL.setText(parser.getFieldOnly("MATPMMDL").trim().toUpperCase());
						}
					} //~03A END
					
					if (rc == 0) //~03A
					{
						label_data.append("<WCPN>");
						label_data.append(this.vlCardPN.getText().toUpperCase().trim());
						label_data.append("</WCPN>");
						label_data.append("<WCSN></WCSN>");
						label_data.append("<FPRT>");
						label_data.append(this.vlFFBMPN.getText().toUpperCase().trim());
						label_data.append("</FPRT>");
						label_data.append("<FSER>");
						label_data.append(this.vlFFBMSN.getText().toUpperCase().trim());
						label_data.append("</FSER>");
						label_data.append("<MATP>");
						if (!this.tfMATPMMDL.getText().equals(""))
						{
							label_data.append(this.tfMATPMMDL.getText().toUpperCase().trim().substring(0, 4));
						}
						label_data.append("</MATP>");
						label_data.append("<MMDL>");
						if (!this.tfMATPMMDL.getText().equals(""))
						{
							label_data.append(this.tfMATPMMDL.getText().toUpperCase().trim().substring(4));
						}
						label_data.append("</MMDL>");
						label_data.append("<MCSN>");
						label_data.append(this.tfMCSN.getText().toUpperCase().trim());
						label_data.append("</MCSN>");
						label_data.append("<ITEM>");
						label_data.append(this.vlFFBMPN.getText().toUpperCase().trim());
						label_data.append("</ITEM>");
						label_data.append("<USER>");
						label_data.append(MFSConfig.getInstance().getConfigValue("USER"));
						label_data.append("</USER>");
						label_data.append("<DEMD>JP</DEMD>");

						label_trigger.append("<JPOPTWNTY>");
						label_trigger.append(this.vlCardPN.getText().toUpperCase().trim());
						label_trigger.append("</JPOPTWNTY>");

						if(!parser.getField("MCTL").trim().equals(""))
						{
							plom.append("<MCTL>");
							plom.append(parser.getField("MCTL").trim());
							plom.append("</MCTL>");
						}
						else
						{
							/* ~06A ~07C BEGIN                                                              */
							/* Look for file plom.xml. If it exists, check if a PLOM value has been entered */
							/* before. If so use that value for processing and do not show gvd dialog again.*/
							plom_file = new File("plom.xml");
							MfsXMLParser plom_parser = null;
							
							if (plom_file.exists())
							{
								int c = 0;
								InputStream in = new FileInputStream(plom_file);
								in = new BufferedInputStream(in);
								StringBuffer buffer = new StringBuffer((int)plom_file.length());
								
								while ((c = in.read()) != -1)
								{
									buffer.append((char)c);
								}
								
								in.close();
								
								plom_parser = new MfsXMLParser(buffer.toString());
								
								try
								{
									prcplom = plom_parser.getFieldOnly("PLOMPRC");
								}
								catch (MISSING_XML_TAG_EXCEPTION e)
								{
									prcplom = "";
								}
								
								try
								{
									retval = plom_parser.getFieldOnly("PLOMJPN");
								}
								catch (MISSING_XML_TAG_EXCEPTION e)
								{
									retval = "";
								}
								
							} //~06A END
							
							if (!plom_file.exists() || retval.equals(""))
							{
								MFSGetValueDialog gvd = new MFSGetValueDialog(this.getParentFrame(),
										"Enter the Plant of Manufacture", "PLOM", "OK", 'o');
								MFStfParser gvdparser = new MFStfParser.MFStfCntrParser(this);
								gvd.setTextParser(gvdparser);
								gvd.setLocationRelativeTo(getParentFrame());
								gvd.setVisible(true);
								if (gvd.getProceedSelected())
								{
									retval = gvd.getInputValue().toUpperCase().trim();
									if (retval.length() == 0)
									{
										retval = null;
									}
									else
									{
										retval = retval.replaceFirst("[0]+", ""); //~07C
										saveplomfile = true; //~07A
									}
								} 
								else
								{
									retval = "Cancel";
								}
							} //~07C END
							
							if (retval!=null)
							{
								plom.append(retval);
							}
							
							//~06A ~07C BEGIN
							// If this is the first time a PLOM value is
							// being entered, store it in file plom.xml.
							if (retval.length() == 3 && saveplomfile == true)
							{
								try
								{
									FileWriter outfile = new FileWriter(plom_file);
									PrintWriter out = new PrintWriter(outfile);
									out.println("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
									out.println("<LASTPLOM>");
									out.println("<PLOMPRC>"+prcplom+"</PLOMPRC>");
									out.println("<PLOMJPN>"+retval+"</PLOMJPN>");
									out.println("</LASTPLOM>");
									out.close();
								}
								catch (Exception e)
								{
									// Catch any possible exceptions but do nothing. Errors here
									// should not stop the Warranty Card from printing.
								}
							} //~06A ~07C END
						}

						if (plom.toString().length() != 3 && !plom.toString().contains("<MCTL>"))
						{
							if (plom.toString().equals("Cancel"))
							{
								this.errorLabel.setText("");
							}
							else
							{
								this.errorLabel.setText("PLOM value must be 3 characters in length.");
							}
						}
						else
						{
							this.startAction("Printing Warranty Card, please wait...");
							rc = MFSPrintingMethods.warrantyCard(label_data.toString(), "OPTWNTYPRINT", label_trigger.toString(), 1, plom.toString(), this.getParentFrame());
							this.stopAction();

							if (rc == 0)
							{
								clear();
								this.errorLabel.setText("Warranty Card successfully printed.");
							}
							else
							{
								this.errorLabel.setText("Printer Error: Unable to print WARRANTYCARD Label.");
							}
						}
					}
				}
				else if (rc == this.NO_FDES_FOUND) //~05C
				{
					MfsXMLParser parser = new MfsXMLParser(crtwcarecs.getOutput()); //~05C
					
					if(parser.fieldExists("FDES")) //~05A
					{
						this.tfFFBMDesc.setText(parser.getFieldOnly("FDES").trim()); //~05A
					}
					else if (IGSMessageBox.showYesNoMB(this, null, "No FFBM Description was found. Would you like to enter it manually?", null)) //~02C
					{
						this.tfFFBMDesc.setVisible(true);
						this.focus_on_tf_desc = true; //~04C
					}
					else
					{
						this.errorLabel.setText("");
					}
					
					if (parser.fieldExists("MATPMMDL")) //~02C ~05C BEGIN
					{
						this.rcvdMATPMMDL = parser.getFieldOnly("MATPMMDL").trim();
						this.tfMATPMMDL.setText(rcvdMATPMMDL);
						this.tfMCSN.setText("");
						if (!rcvdMATPMMDL.equals(""))
						{
							this.tfMCSN.setEnabled(false);
						}
						else
						{
							this.errorLabel.setText("Default MATP/MMDL rule not set. Please enter MATP/MMDL and Machine SN manually.");
							this.tfMCSN.setEnabled(true);
						}
						this.tfMATPMMDL.setVisible(true);
						this.tfMCSN.setVisible(true);
					} //~02C ~05C END
				}
				else
				{
					this.errorLabel.setText(crtwcarecs.getErms());
				}
			}
		}
		
		catch (Exception e)
		{
			this.errorLabel.setText(e.getMessage());
		}
		
		createLayout();
		if (focus_on_tf_card == true)
		{
			this.tfCard.requestFocusInWindow();
		}
		else if (focus_on_tf_ffbm == true)
		{
			this.tfFFBM.requestFocusInWindow();
		}
		else if (this.focus_on_tf_desc == true)  //~04C
		{
			this.tfFFBMDesc.requestFocusInWindow();
		}
		else if (focus_on_tf_mcsn == true)
		{
			this.tfMCSN.requestFocusInWindow();
		}
		else if (this.focus_on_tf_matp == true)  //~04C
		{
			this.tfMATPMMDL.requestFocusInWindow();
		}
	}

	/** Processes the input the user entered in the <code>JTextField</code>. 
	 * @param textField the <code>JTextField</code>
	 * @param pnValueLabel the PN value <code>JLabel</code> to populate
	 * @param snValueLabel the SN value <code>JLabel</code> to populate
	 */
	private void recvInput(JTextField textField, JLabel pnValueLabel, JLabel snValueLabel)
	{
		int rc = 0;
		StringBuffer matpmmdl = new StringBuffer();
		
		try
		{
			this.errorLabel.setText("");
			this.clearPerformed = false;

			MFSBCBarcodeDecoder barcode = MFSBCBarcodeDecoder.getInstance();
			barcode.setMyBarcodeInput(textField.getText().toUpperCase());
			barcode.setMyBCPartObject(new MFSBCPartObject());

			/* setup barcode indicator values */
			MFSBCIndicatorValue bcIndVal = new MFSBCIndicatorValue();
			
			if (pnValueLabel == null && snValueLabel == null)
			{
				bcIndVal.setCMTI("1"); //$NON-NLS-1$
				bcIndVal.setMMDL("1"); //$NON-NLS-1$
			}
			else
			{
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
				if (pnValueLabel == null && snValueLabel == null)
				{
					if (!barcode.getBCMyPartObject().getMTC().equals("")) //$NON-NLS-1$
					{
						matpmmdl.append(barcode.getBCMyPartObject().getMTC());
					}
					if (!(barcode.getBCMyPartObject().getMD().equals(""))) //$NON-NLS-1$
					{
						matpmmdl.append(barcode.getBCMyPartObject().getMD());
					}
					if (!(barcode.getBCMyPartObject().getMSN().equals(""))) //$NON-NLS-1$
					{
						String mcsn = barcode.getBCMyPartObject().getMSN();
						this.tfMCSN.setText(mcsn.substring(0, 2) + mcsn.substring(4));
					}
				}
				else
				{
					if (!(barcode.getBCMyPartObject().getPN().equals(""))) //$NON-NLS-1$
					{
						pnValueLabel.setText(barcode.getBCMyPartObject().getPN());
					}
					if ((snValueLabel != null) && !(barcode.getBCMyPartObject().getSN().equals(""))) //$NON-NLS-1$
					{
						snValueLabel.setText(barcode.getBCMyPartObject().getSN());
					}
					validateInput();
				}
			}
		}
		
		catch (Exception e)
		{
			this.errorLabel.setText(e.getMessage());
		}

		this.toFront();
		if (pnValueLabel == null && snValueLabel == null)
		{
			textField.setText(matpmmdl.toString().trim());
		}
		else
		{
			textField.setText(""); //$NON-NLS-1$
		}
		createLayout();
	}

	/** Validates which elements have been decoded by {@link #recvInput(JTextField, JLabel, JLabel)} */
	private void validateInput()
	{
		if (!this.vlCardPN.getText().equals(""))
		{
			this.cardPNfound = true;
		}
		if (!this.vlFFBMPN.getText().equals(""))
		{
			this.ffbmPNfound = true;
		}
		if (!this.vlFFBMSN.getText().equals(""))
		{
			this.ffbmSNfound = true;
		}
		if (this.cardPNfound && this.ffbmPNfound && this.ffbmSNfound)
		{
			print();  //~04C
		}
	}

	/** Clears all fields and requests the focus for the {@link #tfCard} field. */
	private void clear()
	{
		this.errorLabel.setText("");
		this.tfCard.setText("");
		this.vlCardPN.setText("");
		this.tfFFBM.setText("");
		this.vlFFBMPN.setText("");
		this.vlFFBMSN.setText("");
		this.tfFFBMDesc.setText("");
		this.tfMATPMMDL.setText("");
		this.tfMCSN.setText("");
		this.rcvdMATPMMDL = "";
		this.tfFFBMDesc.setVisible(false);
		this.tfMATPMMDL.setVisible(false);
		this.tfMCSN.setVisible(false);
		this.tfMCSN.setEnabled(false);
		this.cardPNfound = false;
		this.ffbmPNfound = false;
		this.ffbmSNfound = false;
		this.clearPerformed = true;
		this.focus_on_tf_desc = false;  //~04C
		this.focus_on_tf_matp = false;  //~04C
		createLayout();
		this.tfCard.requestFocusInWindow();
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
			if (source == this.pbReset)
			{
				clear();
			}
			else if (source == this.pbModes)
			{
				this.dispose();
				MFSWarrantyCardModeDialog myWCMD = new MFSWarrantyCardModeDialog(getParentFrame());
				myWCMD.setLocationRelativeTo(getParentFrame());
				myWCMD.setVisible(true);
			}
			else if (source == this.pbPrint)
			{
				print();
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
			if (source == this.pbReset)
			{
				this.pbReset.requestFocusInWindow();
				this.pbReset.doClick();
			}
			else if (source == this.pbPrint)
			{
				this.pbPrint.requestFocusInWindow();
				this.pbPrint.doClick();
			}
			else if (source == this.pbModes)
			{
				this.pbModes.requestFocusInWindow();
				this.pbModes.doClick();
			}
			else if (source == this.tfCard)
			{
				recvInput(this.tfCard, this.vlCardPN, null);
				if (this.focus_on_tf_desc)  //~04A BEGIN 
				{
					this.tfFFBMDesc.requestFocusInWindow();
				}
				else if (this.focus_on_tf_matp)
				{
					this.tfMATPMMDL.requestFocusInWindow();
				}  //~04A END
				else if (this.cardPNfound)  //~04C
				{
					this.tfFFBM.requestFocusInWindow();
				}
				else
				{
					this.tfCard.requestFocusInWindow();
				}
			}
			else if (source == this.tfFFBM)
			{
				recvInput(this.tfFFBM, this.vlFFBMPN, this.vlFFBMSN);
				if (this.focus_on_tf_desc)  //~04A BEGIN
				{
					this.tfFFBMDesc.requestFocusInWindow();
				}
				else if (this.focus_on_tf_matp)
				{
					this.tfMATPMMDL.requestFocusInWindow();
				}  //~04A END
				else if (!this.clearPerformed)  //~04C
				{
					if (this.ffbmPNfound && this.ffbmSNfound)
					{
						this.tfCard.requestFocusInWindow();
					}
					else
					{
						this.tfFFBM.requestFocusInWindow();
					}
				}
				else
				{
					this.tfCard.requestFocusInWindow();
					this.clearPerformed = false;
				}
			}
			else if (source == this.tfMATPMMDL)
			{
				recvInput(this.tfMATPMMDL, null, null);
				this.tfFFBMDesc.requestFocusInWindow();
			}
		}
		else if (keyCode == KeyEvent.VK_F2)
		{
			this.pbPrint.requestFocusInWindow();
			this.pbPrint.doClick();
		}
		else if (keyCode == KeyEvent.VK_ESCAPE)
		{
			this.dispose();
		}
	}

	/**
	 * Invoked when a document's attribute or set of attributes are changed.
	 * @param e the <code>DocumentEvent</code>
	 */
	public void changedUpdate(DocumentEvent e)
	{
		// Do nothing. This method needs to be declared, but we do
		// not need it for the moment because plain text components
		// do not fire these events.
	}

	/**
	 * Invoked when there is an insert into the document.
	 * @param e the <code>DocumentEvent</code>
	 */
	public void insertUpdate(DocumentEvent e)
	{
		Document doc = e.getDocument();
		if (doc.getProperty("Name").equals("tfMATPMMDL"))
		{
			if (!this.tfMCSN.isEnabled())
			{
				this.tfMCSN.setEnabled(true);
			}
		}
	}

	/**
	 * Invoked when a portion of the document has been removed.
	 * @param e the <code>DocumentEvent</code>
	 */
	public void removeUpdate(DocumentEvent e)
	{
		Document doc = e.getDocument();
		if (doc.getProperty("Name").equals("tfMATPMMDL"))
		{
			if (!this.tfMCSN.isEnabled())
			{
				this.tfMCSN.setEnabled(true);
			}
		}
	}

}
