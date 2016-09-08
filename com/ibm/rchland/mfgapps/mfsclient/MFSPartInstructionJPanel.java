/* © Copyright IBM Corporation 2002, 2007. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2007-02-19   ~1 34242JR  R Prechel        -Java 5 version
 *                                           -Moved action listening to MFSIR Button classes
 *              ~1 34177JR  R Prechel        -Adjust layout so that width can be
 *                                            constrained by MFSPIPHolderJPanel
 * Note: 34242JR and 34177JR changes were made simultaneously
 * 2008-01-11   ~2 39782JM  Martha Luna      -Support "multi-drop" cables for manufacturing.
 * 
 * 2013-12-06	~3 00267733	Andrea Cabrera	 -Migration to Java 1.5 and adjustment of blink 
 * 2016-03-03   ~4 1471226  Miguel Rivas     -Support TLS1.2
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.io.IOException; //~4

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.text.JTextComponent;

import com.ibm.rchland.mfgapps.client.utils.messagebox.IGSMessageBox;
import com.ibm.rchland.mfgapps.mfsclient.media.MFSMediaHandler;
import com.ibm.rchland.mfgapps.mfsclient.panel.MFSViewOpsPanel;
import com.ibm.rchland.mfgapps.mfsclient.renderer.MFSComponentCellRenderer;
import com.ibm.rchland.mfgapps.mfsclient.struct.MFSComponentListModel;
import com.ibm.rchland.mfgapps.mfsclient.struct.MFSComponentRec;
import com.ibm.rchland.mfgapps.mfsclient.struct.MFSInstructionRec;
import com.ibm.rchland.mfgapps.mfsclient.struct.MFSSubInstructionRec;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSConstants;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSHyperlinkListener;

/**
 * <code>MFSPartInstructionJPanel</code> is a subclass of <code>JPanel</code>
 * used to display a list of parts and the corresponding instructions and
 * Interactive Routing media.
 * @author The MFS Client Development Team
 */
public class MFSPartInstructionJPanel
	extends JPanel
{
	private static final long serialVersionUID = 1L;
	/**
	 * The width of the part component when instructions are being displayed.
	 * The part component is either the part <code>JList</code> or the no part
	 * <code>JPanel</code>.
	 */
	public static final int PART_COMP_WIDTH = 270; //~1A

	/** The incomplete completion status. */
	public static final int INCOMPLETE = 0; //~1A

	/** The partially complete completion status. */
	public static final int PARTIALLY_COMPLETE = 1; //~1A

	/** The complete completion status. */
	public static final int COMPLETE = 2; //~1A

	/** The background <code>Color</code> of "Blue Rows". */
	public static final Color BLUE_ROW_COLOR = new Color(0, 180, 255); //~1A

	/** The complete mandatory instruction panel background <code>Color</code>. */
	private static final Color MANDATORY_COMPLETE_COLOR = new Color(255, 70, 100); //~1A

	/** The not complete mandatory instruction panel background <code>Color</code>. */
	private static final Color MANDATORY_NOT_COMPLETE_COLOR = new Color(255, 170, 200); //~1A

	/** The text of {@link #lblComplete}when status is incomplete. */
	private static final String INCOMPLETE_TEXT = "Incomplete"; //~1A

	/** The text of {@link #lblComplete}when status is partially complete. */
	private static final String PARTIALLY_COMPLETE_TEXT = "Partially Complete"; //~1A

	/** The text of {@link #lblComplete}when status is complete. */
	private static final String COMPLETE_TEXT = "Complete"; //~1A

	/**
	 * The <code>ListCellRenderer</code> used to render a component record
	 * when Interactive Routing instructions are displayed.
	 */
	private static final ListCellRenderer  IRDSCCR = new MFSComponentCellRenderer(true); //~1A

	/**
	 * The <code>ListCellRenderer</code> used to render a component record
	 * when Interactive Routing instructions are not displayed.
	 */
	private static final ListCellRenderer DSCCR = new MFSComponentCellRenderer(false); //~1A

	/**
	 * The <code>MFSInstructionsPanel</code> used to display this
	 * <code>MFSPartInstructionJPanel</code>.
	 */
	private MFSInstructionsPanel fieldDisplayer;

	/**
	 * The no part <code>JPanel</code>. Displayed as the part
	 * <code>Component</code> for a non-part instruction.
	 * @see #getIsNonPartInstruction()
	 */
	private JPanel pnlNoPart = new JPanel(new FlowLayout());

	/**
	 * The no part <code>JLabel</code>. Displayed in the no part
	 * <code>JPanel</code> for a non-part instruction.
	 * @see #getIsNonPartInstruction()
	 */
	private JLabel lblNoPart = new JLabel("NON-PART INSTRUCTION");

	/**
	 * The part <code>JList</code>. Displayed as the part
	 * <code>Component</code> for a part instruction.
	 * @see #getIsNonPartInstruction()
	 */
	private JList lstPN = new JList();

	/** The instruction <code>JPanel</code>. */
	private JPanel pnlInstruction = new JPanel(new GridBagLayout());

	/** The completion status <code>Icon</code>. */
	private CompleteLabelIcon icnComplete = new CompleteLabelIcon(); //~1A

	//~1A To get the sizing to work, the initial text must be set to the
	// longest string that will be displayed by the label
	/** The completion status <code>JLabel</code>. */
	private JLabel lblComplete = new JLabel(PARTIALLY_COMPLETE_TEXT, this.icnComplete,
			SwingConstants.LEADING);

	/**
	 * <code>true</code> iff this <code>MFSPartInstructionJPanel</code> is
	 * for a non part instruction.
	 */
	private boolean fieldIsNonPartInstruction = false;

	/**
	 * The <code>MFSInstructionRec</code> for which this
	 * <code>MFSPartInstructionJPanel</code> displays data.
	 */
	private MFSInstructionRec fieldInstructionRec = new MFSInstructionRec();

	/**
	 * Constructs a new <code>MFSPartInstructionJPanel</code>.
	 * @param displayer the <code>MFSInstructionsPanel</code> used to display
	 *        this <code>MFSPartInstructionJPanel</code>
	 */
	public MFSPartInstructionJPanel(MFSInstructionsPanel displayer)
	{
		//~1C Initialize in constructor, remove initialize method
		super(new GridBagLayout());
		setBackground(MFSConstants.PRIMARY_BACKGROUND_COLOR);
		setBorder(BorderFactory.createEtchedBorder());
		this.fieldDisplayer = displayer;

		//~1C Eagerly setup all the components instead of using get methods
		this.pnlNoPart.setBorder(BorderFactory
				.createLineBorder(MFSConstants.PRIMARY_FOREGROUND_COLOR));
		this.pnlNoPart.setBackground(MFSConstants.PRIMARY_BACKGROUND_COLOR);
		this.pnlNoPart.add(this.lblNoPart);
		Dimension d = this.pnlNoPart.getPreferredSize();
		this.pnlNoPart.setMinimumSize(new Dimension(PART_COMP_WIDTH, d.height));
		this.pnlNoPart.setPreferredSize(new Dimension(PART_COMP_WIDTH, d.height));

		this.lstPN.setBackground(MFSConstants.PRIMARY_BACKGROUND_COLOR);
		this.lstPN.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		this.pnlInstruction.setBackground(MFSConstants.PRIMARY_BACKGROUND_COLOR);
		this.pnlInstruction.setBorder(BorderFactory
				.createLineBorder(MFSConstants.PRIMARY_FOREGROUND_COLOR));

		this.lblComplete.setForeground(MFSConstants.PRIMARY_FOREGROUND_COLOR);

		//~1A Set the label size based on the longest text
		//(the label was initialized using the longest text)
		d = this.lblComplete.getPreferredSize();
		this.lblComplete.setMinimumSize(new Dimension(d.width + 37, d.height));
		this.lblComplete.setPreferredSize(new Dimension(d.width + 37, d.height));
		this.lblComplete.setText(""); //$NON-NLS-1$
	}

	/**
	 * Changes the background <code>Color</code> of this panel's components to
	 * the specified <code>color</code>.
	 * @param color the background <code>Color</code>
	 */
	public void changeColor(Color color)
	{
		if (this.pnlNoPart.isVisible())
		{
			this.pnlNoPart.setBackground(color);
		}
		this.pnlInstruction.setBackground(color);
	}

	//~1C Shortened name; removed cell renderer and frameWidth params
	/**
	 * Configures the <code>MFSPartInstructionJPanel</code>.
	 * @param lm the <code>ComponentListModel</code>
	 * @param instrRec the <code>Instruction_Rec</code>
	 * @param someInstructions <code>true</code> iff the are instructions
	 * @param builderLevel the builder's permission level
	 */
	public void configure(MFSComponentListModel lm, MFSInstructionRec instrRec,
							boolean someInstructions, String builderLevel)
	{
		
		//~1C This method was updated based on the assumption that the
		//panel's width is constrained by the MFSPIPHolderJPanel.

		// First configure the left side of the panel (the part number component
		// pnComp, either lstPN or pnlNoPart based on lm.size()).
		// someInstructions is used to determine the cell renderer for lstPN.
		// If there are instructions, configure the right side of the panel.
		// The right side contains the instruction text, MFSIRButtons
		// (instruction source, media, and completion buttons), and the
		// aggregate parts list.
		// The components are named based on certain attributes.
		// These names are used elsewhere for conditional logic.

		try
		{
			//Stores a reference to the part number component
			// (either lstPN or pnlNoPart)
			JComponent pnComp = null;

			//Check the size of the list model
			//If the size is 0, there are no parts -> use pnlNoPart
			if (lm.size() == 0)
			{
				pnComp = this.pnlNoPart;
				if (instrRec.getCompletionStatus().equals("C")) //$NON-NLS-1$
				{
					this.pnlNoPart.setBackground(Color.cyan);
				}
				this.fieldIsNonPartInstruction = true;
			}
			//If the size is not 0, there are parts -> use lstPN
			else
			{
				pnComp = this.lstPN;

				if (lm.getIsShort())
				{
					StringBuffer name = new StringBuffer(36);
					name.append("ShortParts"); //$NON-NLS-1$
					name.append(lm.getSuff());
					name.append(lm.getNmsq());
					name.append("List"); //$NON-NLS-1$
					this.lstPN.setName(name.toString());
				}
				else
				{
					String suff = lm.getSuff();
					String seq = lm.getNmsq();

					if (suff.equals("") || suff.equals("          ")) //$NON-NLS-1$ //$NON-NLS-2$
					{
						suff = "BLANKSUFFX"; //$NON-NLS-1$
					}

					if (seq.equals("") || seq.equals("          ")) //$NON-NLS-1$ //$NON-NLS-2$
					{
						seq = "BLANK"; //$NON-NLS-1$
					}

					this.lstPN.setName(suff + seq + "List"); //$NON-NLS-1$
				}
				this.lstPN.setModel(lm);

				//~1C The cell renderers control the size of the list,
				// so a fixed cell height is not set

				// If there are instructions, use the IR display string
				// component cell renderer (IRDSCCR). Otherwise, use the display
				// string component cell renderer (DSCCR)
				this.lstPN.setCellRenderer((someInstructions ? IRDSCCR : DSCCR));
			}

			//Determine the layout and add the part number component
			GridBagConstraints partGBC = new GridBagConstraints(
			//This is configured for the someInstructions case
					0, 0, //gridx and gridy --> leftmost cell
					1, 1, //gridwith and gridheight --> 1 cell
					0.0, 1.0, //gridweightx and gridweighty
					GridBagConstraints.NORTHWEST, //anchor
					GridBagConstraints.VERTICAL, //fill
					new Insets(0, 0, 0, 2), //insets
					0, 0); //ipadx and ipady

			//If there are no instructions,
			//the part component should fill the screen horizontally
			if (someInstructions == false)
			{
				partGBC.fill = GridBagConstraints.HORIZONTAL;
				partGBC.weightx = 1.0;
			}
			add(pnComp, partGBC);

			//If there are instructions, add them
			if (someInstructions)
			{
				int yPos = 0;

				//Put the shorted parts indicator at the top if the parts are short
				if (instrRec.getSsVector().size() == 0 || lm.getIsShort())
				{
					//~1C Changed h4 tag to h3
					StringBuffer text = new StringBuffer(64);
					if (lm.getIsShort() == false)
					{
						text.append("<html><h3>No Instructions for these Parts!</h3></html>");
					}
					else
					{
						MFSComponentRec cmp = (MFSComponentRec) lm.getElementAt(0);
						text.append("<html><h3>Shorted Parts from Operation ");
						text.append(cmp.getNmbr());
						text.append("!</h3></html>");
					}

					JEditorPane instrEditorPane = new JEditorPane("text/html", text.toString()); //$NON-NLS-1$
					instrEditorPane.setOpaque(false);
					instrEditorPane.setEditable(false);

					GridBagConstraints instrCons = new GridBagConstraints(
					//Configure the GridBagConstraints
							0, yPos++, //gridx and gridy --> leftmost cell
							4, 1, //gridwith and gridheight --> 4 cells
							0.0, 0.0, //gridweightx and gridweighty
							GridBagConstraints.NORTH, //anchor
							GridBagConstraints.HORIZONTAL, //fill
							new Insets(0, 2, 0, 0), //insets
							0, 0); //ipadx and ipady
					this.pnlInstruction.add(instrEditorPane, instrCons);

					//add a source button for non-blank suffix/sequence
					// non-short instructions
					if (builderLevel.equals("E")) //$NON-NLS-1$
					{
						if (!lm.getIsShort() && (!lm.getSuff().equals("") //$NON-NLS-1$
								|| lm.getSuff().equals("          "))) //$NON-NLS-1$
						{
							GridBagConstraints srcButtonCons = new GridBagConstraints(
							//Configure the GridBagConstraints
									2, yPos, //gridx and gridy --> third cell
									1, 1, //gridwith and gridheight --> 1 cell
									0.0, 1.0, //gridweightx and gridweighty
									GridBagConstraints.NORTHEAST, //anchor
									GridBagConstraints.NONE, //fill
									new Insets(0, 10, 0, 0), //insets
									0, 0); //ipadx and ipady

							//~1C Use MFSIRSourceButton
							this.pnlInstruction.add(new MFSIRSourceButton(this), srcButtonCons);
						}
					}// end of builderLevel check
				}//end of ssVector.size check || short check

				// Actual instruction text and media is stored in the
				// SubInstruction vector in the MFSInstructionRec
				if (instrRec.getSsVector().size() > 0)
				{
					for (int i = 0; i < instrRec.getSsVector().size(); i++)
					{
						MFSSubInstructionRec siRec = instrRec.getSsVectorElement(i);

						/* replace variables in the instruction text */
						String tweakedInstr = replaceVariables(siRec.getInstrText().trim());
						siRec.setInstrText(tweakedInstr);

						JTextComponent ssTextComp = null;
						/* decide if we want a JEditorPane or a JTextArea */
						if (siRec.getInstrText().indexOf('<') != -1)
						{
							JEditorPane ssEdPane = new JEditorPane();
							ssEdPane.setContentType("text/html"); //$NON-NLS-1$
							//~1C Use MFSHyperlinkListener
							ssEdPane.addHyperlinkListener(new MFSHyperlinkListener(ssEdPane));

							ssTextComp = ssEdPane;
						}
						else
						{
							JTextArea ssTextArea = new JTextArea();
							ssTextArea.setLineWrap(true);
							ssTextArea.setWrapStyleWord(true);

							ssTextComp = ssTextArea;
						}

						ssTextComp.setEditable(false);
						ssTextComp.setOpaque(false);
						ssTextComp.setText(siRec.getInstrText().trim());

						GridBagConstraints instrCons = new GridBagConstraints(
						//Configure the GridBagConstraints
								0, yPos++, //gridx and gridy --> leftmost cell
								4, 1, //gridwith and gridheight --> 4 cells
								1.0, 0.0, //gridweightx and gridweighty
								GridBagConstraints.NORTHEAST, //anchor
								GridBagConstraints.BOTH, //fill
								new Insets(0, 2, 0, 0), //insets
								0, 0); //ipadx and ipady
						this.pnlInstruction.add(ssTextComp, instrCons);

						// Set up media
						/*
						 * Each subInstruction record can have 1 drawing and 3
						 * other media references. This allows for much
						 * flexibility in setting up instructions for the
						 * M.E.'s. Any of these media files can be "auto
						 * exploded" meaning they display out right on the
						 * screen, not thru pressing on a button - we support
						 * autoexplode only on gifs, jpegs and bitmaps.
						 */
						if (siRec.getDrawing().length() != 0
								|| siRec.getMedia1().length() != 0
								|| siRec.getMedia2().length() != 0
								|| siRec.getMedia3().length() != 0)
						{
							boolean drawingExploded = false;
							boolean media1Exploded = false;
							boolean media2Exploded = false;
							boolean media3Exploded = false;

							//~1C Use explodable method to check if an image
							// should auto-explode

							// check Drawing value to see if we've got to auto
							// explode it - explosion consists of the client
							// setting up an html string to reference the media
							// file and displaying the html using a JEditorPane
							String drawing = siRec.getDrawing().toUpperCase();

							if (explodable(drawing, siRec.getExplodeDrawing())) //~1C
							{
								explodeMedia(drawing, siRec.getSizeDrawing(), yPos++);
								drawingExploded = true;

								/*
								 * still need to display the sheet and zone even
								 * though we exploded the drawing
								 */
								JPanel medPnl = new JPanel();
								medPnl.setLayout(new FlowLayout());
								medPnl.setOpaque(false);
								GridBagConstraints medPnlCons = new GridBagConstraints(
								//Configure the GridBagConstraints
										0, yPos++, //gridx and gridy --> leftmost cell
										2, 1, //gridwith and gridheight --> 2 cells
										0.0, 0.0, //gridweightx and gridweighty
										GridBagConstraints.NORTHWEST, //anchor
										GridBagConstraints.VERTICAL, //fill
										new Insets(0, 2, 0, 0), //insets
										0, 0); //ipadx and ipady

								if (siRec.getSheet().length() != 0)
								{
									JLabel sheetLbl = new JLabel("SHEET:" + siRec.getSheet().trim());
									sheetLbl.setFont(MFSConstants.SMALL_MONOSPACED_FONT);
									sheetLbl.setForeground(MFSConstants.PRIMARY_FOREGROUND_COLOR);
									medPnl.add(sheetLbl);
								}
								if (siRec.getZone().length() != 0)
								{
									JLabel zoneLbl = new JLabel(" ZONE:" + siRec.getZone().trim());
									zoneLbl.setFont(MFSConstants.SMALL_MONOSPACED_FONT);
									zoneLbl.setForeground(MFSConstants.PRIMARY_FOREGROUND_COLOR);
									medPnl.add(zoneLbl);
								}
								//add the media panel to the inst Panel
								this.pnlInstruction.add(medPnl, medPnlCons);
							}//end of auto explode drawing

							//check Media1 value to see if we've got to auto explode it
							String media1 = siRec.getMedia1().toUpperCase();
							if (explodable(media1, siRec.getExplodeMedia1())) //~1C
							{
								explodeMedia(media1, siRec.getSizeMedia1(), yPos++);
								media1Exploded = true;
							}

							//check Media2 value to see if we've got to auto explode it
							String media2 = siRec.getMedia2().toUpperCase();
							if (explodable(media2, siRec.getExplodeMedia2())) //~1C
							{
								explodeMedia(media2, siRec.getSizeMedia2(), yPos++);
								media2Exploded = true;
							}

							//check Media3 value to see if we've got to auto explode it
							String media3 = siRec.getMedia3().toUpperCase();
							if (explodable(media3, siRec.getExplodeMedia3())) //~1C
							{
								explodeMedia(media3, siRec.getSizeMedia3(), yPos++);
								media3Exploded = true;
							}

							/* Set Up Media Button Panel */
							JPanel medPnl = new JPanel(new FlowLayout());
							medPnl.setOpaque(false);

							//~1C Change width to 4
							GridBagConstraints medPnlCons = new GridBagConstraints(
							//Configure the GridBagConstraints
									0, yPos++, //gridx and gridy --> leftmost cell
									4, 1, //gridwith and gridheight --> 4 cells
									0.0, 0.0, //gridweightx and gridweighty
									GridBagConstraints.NORTHWEST, //anchor
									GridBagConstraints.VERTICAL, //fill
									new Insets(0, 2, 0, 0), //insets
									0, 0); //ipadx and ipady

							// we will add a button to launch the media if we
							// have not already auto-exploded it earlier
							// each media type will have a different icon on the button

							//~1C Use MFSIRMediaButton to create the media buttons

							// see if we need to add a button for the drawing
							if (siRec.getDrawing().length() != 0 && !drawingExploded)
							{
								medPnl.add(new MFSIRMediaButton(this, siRec.getDrawing())); //~1C

								//add sheet and zone on right of button. Sheet
								// and Zone only apply to the drawing
								// not to media1, 2, and 3.
								if (siRec.getSheet().length() != 0)
								{
									JLabel sheetLbl = new JLabel("SHEET:" + siRec.getSheet().trim());
									sheetLbl.setFont(MFSConstants.SMALL_MONOSPACED_FONT);
									sheetLbl.setForeground(MFSConstants.PRIMARY_FOREGROUND_COLOR);
									medPnl.add(sheetLbl);
								}
								if (siRec.getZone().length() != 0)
								{
									JLabel zoneLbl = new JLabel(" ZONE:" + siRec.getZone().trim());
									zoneLbl.setFont(MFSConstants.SMALL_MONOSPACED_FONT);
									zoneLbl.setForeground(MFSConstants.PRIMARY_FOREGROUND_COLOR);
									medPnl.add(zoneLbl);
								}
							}// end of drawing logic

							/* see if we need to add a button for media1 */
							if (siRec.getMedia1().length() != 0 && !media1Exploded)
							{
								medPnl.add(new MFSIRMediaButton(this, siRec.getMedia1())); //~1C
							}

							/* see if we need to add a button for the media2 */
							if (siRec.getMedia2().length() != 0 && !media2Exploded)
							{
								medPnl.add(new MFSIRMediaButton(this, siRec.getMedia2())); //~1C
							}

							/* see if we need to add a button for the media3 */
							if (siRec.getMedia3().length() != 0 && !media3Exploded)
							{
								medPnl.add(new MFSIRMediaButton(this, siRec.getMedia3())); //~1C
							}
							//add the media panel to the inst Panel
							this.pnlInstruction.add(medPnl, medPnlCons);
						}//media present - add a media Panel
					}//end of for loop on SS vector

					/* Set up completion label */
					GridBagConstraints cmplLblCons = new GridBagConstraints(
					//Configure the GridBagConstraints
							0, yPos++, //gridx and gridy --> leftmost cell
							1, 1, //gridwith and gridheight --> 1 cell
							0.0, 0.0, //gridweightx and gridweighty
							GridBagConstraints.CENTER, //anchor
							GridBagConstraints.HORIZONTAL, //fill
							new Insets(0, 2, 2, 0), //insets
							0, 0); //ipadx and ipady

					//Set the label and icon based on instructions status
					if (instrRec.getCompletionStatus().equals("S") //$NON-NLS-1$
							|| instrRec.getCompletionStatus().equals("P")) //$NON-NLS-1$
					{
						this.lblComplete.setText(PARTIALLY_COMPLETE_TEXT);
						this.icnComplete.status = PARTIALLY_COMPLETE;
					}
					else if (instrRec.getCompletionStatus().equals("C")) //$NON-NLS-1$
					{
						this.lblComplete.setText(COMPLETE_TEXT);
						this.icnComplete.status = COMPLETE;
					}
					else if (instrRec.getCompletionStatus().equals(" ")) //$NON-NLS-1$
					{
						this.lblComplete.setText(INCOMPLETE_TEXT);
						this.icnComplete.status = INCOMPLETE;
					}
					this.pnlInstruction.add(this.lblComplete, cmplLblCons);

					// set up acknowledge button
					if (instrRec.getInstrClass().equals("M") //$NON-NLS-1$
							|| instrRec.getInstrClass().equals("A") //$NON-NLS-1$
							|| (this.fieldIsNonPartInstruction && !instrRec
									.getInstrClass().equals("R"))) //$NON-NLS-1$
					{
						GridBagConstraints ackButtonCons = new GridBagConstraints(
						//Configure the GridBagConstraints
								1, yPos - 1, //gridx and gridy --> second cell, same row
								1, 1, //gridwith and gridheight --> 1 cell
								0.0, 0.0, //gridweightx and gridweighty
								GridBagConstraints.NORTHWEST, //anchor
								GridBagConstraints.NONE, //fill
								new Insets(0, 2, 2, 0), //insets
								0, 0); //ipadx and ipady

						//~1C Use MFSIRCompletionButton
						this.pnlInstruction.add(new MFSIRCompletionButton(this), ackButtonCons);
					}
				} //end of SS records found

				/* set up agragated parts text area */
				if (instrRec.getAgragatePartList().length() != 0)
				{
					GridBagConstraints agTACons = new GridBagConstraints(
					//Configure the GridBagConstraints
							0, yPos++, //gridx and gridy --> leftmost cell
							2, 1, //gridwith and gridheight --> 2 cells
							0.0, 0.0, //gridweightx and gridweighty
							GridBagConstraints.NORTHWEST, //anchor
							GridBagConstraints.NONE, //fill
							new Insets(0, 2, 0, 0), //insets
							0, 0); //ipadx and ipady

					JTextArea agPartTA = new JTextArea(instrRec.getAgragatePartList());
					agPartTA.setLineWrap(true);
					agPartTA.setWrapStyleWord(true);
					agPartTA.setOpaque(false);
					agPartTA.setBorder(BorderFactory
							.createLineBorder(MFSConstants.PRIMARY_FOREGROUND_COLOR));
					agPartTA.setFont(MFSConstants.SMALL_MONOSPACED_FONT);
					this.pnlInstruction.add(agPartTA, agTACons);
				}

				/* Set up instruction source button */
				if (builderLevel.equals("E") && instrRec.getSsVector().size() != 0) //$NON-NLS-1$
				{
					GridBagConstraints srcButtonCons = new GridBagConstraints(
					//Configure the GridBagConstraints
							2, yPos - 1, //gridx and gridy --> third cell, same row
							1, 1, //gridwith and gridheight --> 1 cell
							0.0, 0.0, //gridweightx and gridweighty
							GridBagConstraints.EAST, //anchor
							GridBagConstraints.NONE, //fill
							new Insets(0, 10, 0, 0), //insets
							0, 0); //ipadx and ipady

					//~1C Use MFSIRSourceButton
					this.pnlInstruction.add(new MFSIRSourceButton(this), srcButtonCons);
				}

				/* Add an empty panel to the bottom, to snug everything up */
				GridBagConstraints emptyPnlCons = new GridBagConstraints(
				//Configured to use up any extra space
						0, yPos, //gridx and gridy --> leftmost cell
						4, 1, //gridwith and gridheight --> 4 cells
						1.0, 1.0, //gridweightx and gridweighty
						GridBagConstraints.NORTHEAST, //anchor
						GridBagConstraints.BOTH, //fill
						new Insets(0, 0, 0, 0), //insets
						0, 0); //ipadx and ipady

				JPanel emptyPnl = new JPanel();
				emptyPnl.setPreferredSize(new Dimension(0, 0));
				emptyPnl.setMaximumSize(new Dimension(0, 0));
				emptyPnl.setMinimumSize(new Dimension(0, 0));
				emptyPnl.setOpaque(false);
				this.pnlInstruction.add(emptyPnl, emptyPnlCons);

				/* now add the instrPanel to the MFSPartInstructionJPanel row */
				GridBagConstraints instrPanelCons = new GridBagConstraints(
				//Configure the GridBagConstraints
						1, 0, //gridx and gridy --> rightmost cell
						1, 1, //gridwith and gridheight --> 1 cell
						1.0, 1.0, //gridweightx and gridweighty
						GridBagConstraints.NORTHEAST, //anchor
						GridBagConstraints.BOTH, //fill
						new Insets(0, 0, 0, 2), //insets
						0, 0); //ipadx and ipady

				/* set up the background color of the row */
				if (instrRec.getInstrClass().equals("M")) //$NON-NLS-1$
				{
					if (instrRec.getCompletionStatus().equals("C")) //$NON-NLS-1$
					{
						this.pnlInstruction.setBackground(MANDATORY_COMPLETE_COLOR);
					}
					else
					{
						this.pnlInstruction.setBackground(MANDATORY_NOT_COMPLETE_COLOR);
					}
				}
				else
				{
					if (instrRec.getCompletionStatus().equals("C")) //$NON-NLS-1$
					{
						this.pnlInstruction.setBackground(Color.lightGray);
					}
					else if (instrRec.getSsVector().size() == 0)
					{
						this.pnlInstruction.setBackground(Color.lightGray);
					}
					else
					{
						this.pnlInstruction.setBackground(Color.white);
					}
				}
				add(this.pnlInstruction, instrPanelCons);

				this.fieldInstructionRec = instrRec;

				//run an audit to make sure our instruction status is valid
				//based on install disposition of parts in this list
				ensureCompletionStatus();
			}//end of some instructions
		}
		catch (Exception e)
		{
			MFSFrame parent = this.fieldDisplayer.getParentFrame();
			IGSMessageBox.showOkMB(parent, null, null, e);
		}
	}

	/**
	 * Method is used to verify that the instruction status for part
	 * instructions is correct. Should be normally, but scenarios in reapply,
	 * where server does not try to figure out the status of instructions after
	 * the reapply is complete. We will rely on this method to get the statuses
	 * correct and when we suspend or end the server will get updated properly
	 * via UPDT_INSTR transaction Creation date: (4/8/2003 2:16:11 PM)
	 */
	public void ensureCompletionStatus()
	{
		if (this.fieldIsNonPartInstruction == false)
		{
			MFSComponentRec cmp;

			int j = 0;
			boolean needsWork = false;
			boolean someWorkDone = false;

			while (j < ((MFSComponentListModel) this.lstPN.getModel()).size())
			{
				cmp = (MFSComponentRec) this.lstPN.getModel().getElementAt(j);
				if (cmp.getIdsp().equals("A") //$NON-NLS-1$ 
						|| cmp.getIdsp().equals("X") //$NON-NLS-1$
						|| cmp.getIdsp().equals("R")) //$NON-NLS-1$
				{
					needsWork = true;
				}

				if (cmp.getIdsp().equals("I") //$NON-NLS-1$ 
						|| cmp.getIdsp().equals("D")) //$NON-NLS-1$
				{
					someWorkDone = true;
				}
				j++;
			}

			//~1C Use setCompletionStatus

			//Server thinks instruction is unstarted.
			//If client found some work done, but not all,
			//set the instruction to partial.
			//If all work done, set it to complete.
			if (this.fieldInstructionRec.getCompletionStatus().equals(" ")) //$NON-NLS-1$
			{
				//some done but not all - partially complete
				if (someWorkDone && needsWork)
				{
					setCompletionStatus(PARTIALLY_COMPLETE);
				}
				//all work done - call it good
				else if (someWorkDone && !needsWork)
				{
					setCompletionStatus(COMPLETE);
				}
				//make sure the instuction is set to incomplete
				else
				{
					setCompletionStatus(INCOMPLETE);
				}
			}//end of instruction status is ' '

			//Server thinks instruction is partially complete.
			//If client found no work done, set instruction to incomplete.
			//If all work done, set it to complete.
			else if (this.fieldInstructionRec.getCompletionStatus().equals("P")) //$NON-NLS-1$
			{
				//no work done, set to incomplete
				if (!someWorkDone)
				{
					setCompletionStatus(INCOMPLETE);
				}
				//no work to do, set it to complete
				else if (someWorkDone && !needsWork)
				{
					setCompletionStatus(COMPLETE);
				}
				//some done but not all - partially complete
				else
				{
					setCompletionStatus(PARTIALLY_COMPLETE);
				}
			}//end of instruction status is 'P'

			//Server thinks instruction is complete.
			//If client found no work done, set instruction to incomplete.
			//If some work done, set it to partial
			else if (this.fieldInstructionRec.getCompletionStatus().equals("C")) //$NON-NLS-1$
			{
				//no work done, set to incomplete
				if (!someWorkDone)
				{
					setCompletionStatus(INCOMPLETE);
				}
				//some done but not all - partially complete
				else if (someWorkDone && needsWork)
				{
					setCompletionStatus(PARTIALLY_COMPLETE);
				}
				// is complete - make sure its shown that way
				else
				{
					setCompletionStatus(COMPLETE);
				}
			}//end of instruction status is 'C'

		}/* end of part instruction */

		if (this.fieldInstructionRec.getCompletionStatus().equals("C")) //$NON-NLS-1$
		{
			setCompletionStatus(COMPLETE);
			this.pnlNoPart.setBackground(Color.cyan);
		}
	}

	//~1A New method
	/**
	 * Returns <code>true</code> if the file type of <code>media</code> can
	 * be autoexploded and the <code>explode</code> property is "Y".
	 * @param media the filename for the media
	 * @param explode the explode property for the media
	 * @return <code>true</code> if the media can be exploded
	 */
	private boolean explodable(String media, String explode)
	{
		media = media.toUpperCase();
		return explode.equals("Y") && //$NON-NLS-1$
				(media.indexOf(".GIF") != -1 //$NON-NLS-1$
						|| media.indexOf(".JPEG") != -1 //$NON-NLS-1$
						|| media.indexOf(".JPG") != -1 //$NON-NLS-1$ 
						|| media.indexOf(".BMP") != -1); //$NON-NLS-1$
	}

	/**
	 * Explodes the specified media.
	 * @param media the filename of the media
	 * @param size the size of the media
	 * @param verticalPos the vertical position of the media in the layout
	 */
	private void explodeMedia(String media, String size, int verticalPos)
	{
		//~1C Use MFSMediaHandler to download media
		// and file.toURL to get path information
		try
		{
			//frameWidth is a snap shot of the width of the parent frame
			//Make sure that these pictures fall within the frame
			//First step is to subtract of the size of the stuff on the left
			//side and then enough to make it show up right (50)
			int frameWidth = this.fieldDisplayer.getParentFrame().getWidth();
			frameWidth = frameWidth - PART_COMP_WIDTH - 50;

			//~1C Always download the file since it must
			// be explodable for this method to be called
			File file = null;
			//~4
			try
			{
				file = MFSMediaHandler.cacheRemoteFile(media, "TLS");
			}
			catch (IOException ioe)
			{
				try
				{
					file = MFSMediaHandler.cacheRemoteFile(media, "TLSv1.2");
				}
				catch (IOException ioe1)
				{
					IGSMessageBox.showOkMB(null, null, null, ioe1);
				}
			}
			//~3 Change the deprecated function file.toURL() to file.toURI().toURL() 
			//as documentation suggests
			String absolutePath = file.toURI().toURL().toString();

			StringBuffer htmlString = new StringBuffer();
			htmlString.append("<html><a href=\""); //$NON-NLS-1$
			htmlString.append(absolutePath);
			htmlString.append("\"><img src=\""); //$NON-NLS-1$
			htmlString.append(absolutePath);
			htmlString.append("\""); //$NON-NLS-1$

			size = size.trim();
			if (size.length() != 0)
			{
				double percent = Integer.parseInt(size) / 100.0;
				//~3 Change the deprecated function file.toURL() to file.toURI().toURL() 
				//as documentation suggests
				ImageIcon image = new ImageIcon(file.toURI().toURL());
				int height = (int) (image.getIconHeight() * percent);
				int width = (int) (image.getIconWidth() * percent);

				if (width > frameWidth)
				{
					double ratio = (double) height / (double) width;
					width = frameWidth;
					height = (int) (ratio * width);
				}
				htmlString.append(" height=\""); //$NON-NLS-1$
				htmlString.append(height);
				htmlString.append("\" width=\""); //$NON-NLS-1$
				htmlString.append(width);
				htmlString.append("\""); //$NON-NLS-1$
			}
			else
			{
				//~3 Change the deprecated function file.toURL() to file.toURI().toURL() 
				//as documentation suggests
				ImageIcon image = new ImageIcon(file.toURI().toURL());
				int height = image.getIconHeight();
				int width = image.getIconWidth();

				if (width > frameWidth)
				{
					double ratio = (double) height / (double) width;
					width = frameWidth;
					height = (int) (ratio * width);

					htmlString.append(" height=\""); //$NON-NLS-1$
					htmlString.append(height);
					htmlString.append("\" width=\""); //$NON-NLS-1$
					htmlString.append(width);
					htmlString.append("\""); //$NON-NLS-1$
				}
			}
			htmlString.append("></html>"); //$NON-NLS-1$

			JEditorPane ssEdPane = new JEditorPane();
			ssEdPane.setContentType("text/html"); //$NON-NLS-1$
			ssEdPane.addHyperlinkListener(new MFSHyperlinkListener(ssEdPane));
			ssEdPane.setEditable(false);
			ssEdPane.setOpaque(false);
			ssEdPane.setText(htmlString.toString());
			GridBagConstraints instrCons = new GridBagConstraints(
			//Configure the GridBagConstraints
					0, verticalPos, //gridx and gridy --> leftmost cell
					4, 1, //gridwith and gridheight --> 4 cells
					0.0, 0.0, //gridweightx and gridweighty
					GridBagConstraints.NORTHWEST, //anchor
					GridBagConstraints.HORIZONTAL, //fill
					new Insets(0, 2, 0, 0), //insets
					0, 0); //ipadx and ipady
			this.pnlInstruction.add(ssEdPane, instrCons);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Returns the <code>MFSInstructionRec</code> for the
	 * <code>MFSPartInstructionJPanel</code>
	 * @return the <code>MFSInstructionRec</code>
	 */
	public MFSInstructionRec getInstructionRec()
	{
		return this.fieldInstructionRec;
	}

	//~1A New method
	/**
	 * Returns the <code>MFSInstructionsPanel</code> used to display this
	 * <code>MFSPartInstructionJPanel</code>.
	 * @return the <code>MFSInstructionsPanel</code>
	 */
	public MFSInstructionsPanel getInstructionsPanel()
	{
		return this.fieldDisplayer;
	}

	/**
	 * Returns <code>true</code> iff this panel is for a non-part instruction.
	 * @return <code>true</code> iff this panel is for a non-part instruction
	 */
	public boolean getIsNonPartInstruction()
	{
		return this.fieldIsNonPartInstruction;
	}

	/**
	 * Returns the part number <code>JList</code>.
	 * @return the part number <code>JList</code>
	 */
	public JList getPNList()
	{
		return this.lstPN;
	}

	//~1A New method
	/**
	 * Returns the <code>MFSComponentListModel</code> of the part number
	 * <code>JList</code>.
	 * @return the <code>MFSComponentListModel</code>
	 */
	public MFSComponentListModel getPNListModel()
	{
		return (MFSComponentListModel) this.lstPN.getModel();
	}

	/** Handles the selection of an <code>MFSIRCompletionButton</code>. */
	protected void handleComplete()
	{
		if ((this.fieldDisplayer instanceof MFSViewOpsPanel) == false)
		{
			if (this.icnComplete.status == INCOMPLETE)
			{
				setCompletionStatus(COMPLETE);
				if (this.fieldIsNonPartInstruction)
				{
					this.pnlNoPart.setBackground(Color.cyan);
				}
			}
			else
			{
				setCompletionStatus(INCOMPLETE);
				if (this.fieldIsNonPartInstruction)
				{
					this.pnlNoPart.setBackground(Color.white);
				}
			}

			//Make sure the changed status is correct
			if (this.fieldInstructionRec.getCompletionStatusOriginal().equals(
					this.fieldInstructionRec.getCompletionStatus()))
			{
				this.fieldInstructionRec.setChanged(false);
			}
		}
	}

	/**
	 * Returns <code>true</code> iff only red parts are left.
	 * @return <code>true</code> iff only red parts are left
	 */
	public boolean onlyRedPartsLeft()
	{
		boolean onlyRed = true;

		if (this.fieldIsNonPartInstruction)
		{
			onlyRed = false;
		}
		else
		{
			MFSComponentRec cmp;
			int index = 0;

			while (index < this.lstPN.getModel().getSize() && onlyRed)
			{
				cmp = (MFSComponentRec) (this.lstPN.getModel()).getElementAt(index);
				
				if (!(cmp.getIdsp().equals("I") //$NON-NLS-1$
						|| cmp.getIdsp().equals("D") //$NON-NLS-1$
						|| (cmp.getIdsp().equals("A") && cmp.getMlri().equals(" ")) //$NON-NLS-1$ //$NON-NLS-2$ 
						|| (cmp.getIdsp().equals("A") && cmp.getMlri().equals("0"))))//$NON-NLS-1$ //$NON-NLS-2$
				{
					onlyRed = false;
				}
				index++;
			}
		}
		return onlyRed;
	}

	/**
	 * Replaces variables in the instruction text.
	 * @param instr the instruction text
	 * @return the instruction text with the variables replaced
	 */
	public String replaceVariables(String instr)
	{
		try
		{
			MFSComponentRec cmp;

			int varIndex = 0;

			//look for and replace $descr$ variable
			if (instr.indexOf("$descr$") != -1) //$NON-NLS-1$
			{
				//Found variable $descr$ so calculate what to put in place of it
				StringBuffer descrSb = new StringBuffer();
				final String DASH = "-";
				cmp = (MFSComponentRec) (this.lstPN.getModel()).getElementAt(0);
				if (!cmp.getPll1().equals("    ") && !cmp.getPll1().equals("")) //$NON-NLS-1$ //$NON-NLS-2$
				{
					descrSb.append(cmp.getPll1() + DASH);
				}
				if (!cmp.getPll2().equals("    ") && !cmp.getPll2().equals("")) //$NON-NLS-1$ //$NON-NLS-2$
				{
					descrSb.append(cmp.getPll2() + DASH);
				}
				if (!cmp.getPll3().equals("    ") && !cmp.getPll3().equals("")) //$NON-NLS-1$ //$NON-NLS-2$
				{
					descrSb.append(cmp.getPll3() + DASH);
				}
				if (!cmp.getPll4().equals("    ") && !cmp.getPll4().equals("")) //$NON-NLS-1$ //$NON-NLS-2$
				{
					descrSb.append(cmp.getPll4() + DASH);
				}
				if (!cmp.getPll5().equals("    ") && !cmp.getPll5().equals("")) //$NON-NLS-1$ //$NON-NLS-2$
				{
					descrSb.append(cmp.getPll5() + DASH);
				}

				String descrReplacer;
				if (descrSb.length() > 0)
				{
					descrReplacer = descrSb.substring(0, descrSb.length() - 1);
				}
				else
				{
					descrReplacer = " plug locs not found ";
				}

				varIndex = instr.indexOf("$descr$"); //$NON-NLS-1$
				StringBuffer sb = new StringBuffer(instr);

				//replace all $descr$ with descrReplacer
				while (varIndex != -1)
				{
					sb.replace(varIndex, varIndex + 7, descrReplacer);
					instr = sb.toString();
					varIndex = instr.indexOf("$descr$", varIndex + 7); //$NON-NLS-1$
				}
			}//end of found $descr$

			//look for and replace $frplug$ variable
			if (instr.indexOf("$frplug$") != -1) //$NON-NLS-1$
			{
				//Found variable $frplug$ so calculate what to put in place of it
				String frplugReplacer = null; //~2C
				int j = 0;
				while (j < this.lstPN.getModel().getSize() && frplugReplacer == null) //~2C
				{
					cmp = (MFSComponentRec) (this.lstPN.getModel()).getElementAt(j);
					frplugReplacer = cmp.getPlug("PF"); //~2C //$NON-NLS-1$
					j++;
				}

				if (frplugReplacer == null) //~2C
				{
					frplugReplacer = " from plug missing ";
				}

				varIndex = instr.indexOf("$frplug$"); //$NON-NLS-1$
				StringBuffer sb = new StringBuffer(instr);

				//replace all frplugs with frplugReplacer
				while (varIndex != -1)
				{
					sb.replace(varIndex, varIndex + 8, frplugReplacer);
					instr = sb.toString();
					varIndex = instr.indexOf("$frplug$", varIndex + 8); //$NON-NLS-1$
				}
			}//end of found $frplug$

			//look for and replace $toplug$ variable
			if (instr.indexOf("$toplug$") != -1) //$NON-NLS-1$
			{
				//Found variable $toplug$ so calculate what to put in place of it
				String toplugReplacer = null; //~2C
				int j = 0;
				while (j < this.lstPN.getModel().getSize() && toplugReplacer == null) //~2C
				{
					cmp = (MFSComponentRec) (this.lstPN.getModel()).getElementAt(j);
					toplugReplacer = cmp.getPlug("PT"); //~2C //$NON-NLS-1$
					j++;
				}

				if (toplugReplacer == null) //~2C
				{
					toplugReplacer = " to plug missing ";
				}

				varIndex = instr.indexOf("$toplug$"); //$NON-NLS-1$
				StringBuffer sb = new StringBuffer(instr);

				//replace all frplugs with toplugReplacer
				while (varIndex != -1)
				{
					sb.replace(varIndex, varIndex + 8, toplugReplacer);
					instr = sb.toString();
					varIndex = instr.indexOf("$toplug$", varIndex + 8); //$NON-NLS-1$
				}
			}//end of found $toplug$

			//look for and replace $subloc$ variable
			if (instr.indexOf("$subloc$") != -1) //$NON-NLS-1$
			{
				//Found variable $subloc$ so calculate what to put in place of it
				String cwunReplacer = ""; //$NON-NLS-1$
				int j = 0;
				while (j < this.lstPN.getModel().getSize() && cwunReplacer.equals("")) //$NON-NLS-1$
				{
					cmp = (MFSComponentRec) (this.lstPN.getModel()).getElementAt(j);
					if (!cmp.getCwun().equals("") //$NON-NLS-1$
							&& !cmp.getCwun().equals("        ")) //$NON-NLS-1$
					{
						cwunReplacer = cmp.getCwun();
					}
					j++;
				}

				if (cwunReplacer.equals("")) //$NON-NLS-1$
				{
					cwunReplacer = " cwun missing ";
				}

				varIndex = instr.indexOf("$subloc$"); //$NON-NLS-1$
				StringBuffer sb = new StringBuffer(instr);

				//replace all subloc with cwunReplacer
				while (varIndex != -1)
				{
					sb.replace(varIndex, varIndex + 8, cwunReplacer);
					instr = sb.toString();
					varIndex = instr.indexOf("$subloc$", varIndex + 8); //$NON-NLS-1$
				}
			}//end of found $subloc$

			//look for and replace $scsiid$ variable
			if (instr.indexOf("$scsiid$") != -1) //$NON-NLS-1$
			{
				//Found variable $scsiid$ so calculate what to put in place of it
				String scsiReplacer = ""; //$NON-NLS-1$
				int j = 0;
				while (j < this.lstPN.getModel().getSize() && scsiReplacer.equals("")) //$NON-NLS-1$
				{
					cmp = (MFSComponentRec) this.lstPN.getModel().getElementAt(j);
					if (!cmp.getDfsw().equals("") && !cmp.getDfsw().equals("  ")) //$NON-NLS-1$ //$NON-NLS-2$
					{
						scsiReplacer = cmp.getDfsw();
					}
					j++;
				}

				if (scsiReplacer.equals("")) //$NON-NLS-1$
				{
					scsiReplacer = " scsiid missing ";
				}

				varIndex = instr.indexOf("$scsiid$"); //$NON-NLS-1$
				StringBuffer sb = new StringBuffer(instr);

				//replace all scsiid with scsiReplacer
				while (varIndex != -1)
				{
					sb.replace(varIndex, varIndex + 8, scsiReplacer);
					instr = sb.toString();
					varIndex = instr.indexOf("$scsiid$", varIndex + 8); //$NON-NLS-1$
				}
			}//end of found $scsiid$
		}//end of try block
		catch (Exception e)
		{
			e.printStackTrace(System.out);
		}
		return instr;
	}

	//~1A New method
	/**
	 * Sets the completion status.
	 * @param status the completion status
	 */
	public void setCompletionStatus(int status)
	{
		switch (status)
		{
			case INCOMPLETE:
				this.icnComplete.status = INCOMPLETE;
				this.lblComplete.setText(INCOMPLETE_TEXT);
				this.fieldInstructionRec.setChanged(true);
				this.fieldInstructionRec.setCompletionStatus(" "); //$NON-NLS-1$
				setInstPanelBackground_NotComplete();
				break;

			case PARTIALLY_COMPLETE:
				this.icnComplete.status = PARTIALLY_COMPLETE;
				this.lblComplete.setText(PARTIALLY_COMPLETE_TEXT);
				this.fieldInstructionRec.setChanged(true);
				this.fieldInstructionRec.setCompletionStatus("P"); //$NON-NLS-1$
				break;

			case COMPLETE:
				this.icnComplete.status = COMPLETE;
				this.lblComplete.setText(COMPLETE_TEXT);
				this.fieldInstructionRec.setChanged(true);
				this.fieldInstructionRec.setCompletionStatus("C"); //$NON-NLS-1$
				setInstPanelBackground_Complete();
				break;
		}
	}

	//~1A New method
	/** Sets the color of the intruction panel to the complete color. */
	public void setInstPanelBackground_Complete()
	{
		if (this.fieldInstructionRec.getInstrClass().equals("M")) //$NON-NLS-1$
		{
			this.pnlInstruction.setBackground(MANDATORY_COMPLETE_COLOR);
		}
		else
		{
			this.pnlInstruction.setBackground(Color.lightGray);
		}
	}

	//~1A New method
	/** Sets the color of the intruction panel to the not complete color. */
	public void setInstPanelBackground_NotComplete()
	{
		if (this.fieldInstructionRec.getInstrClass().equals("M")) //$NON-NLS-1$
		{
			this.pnlInstruction.setBackground(MANDATORY_NOT_COMPLETE_COLOR);
		}
		else
		{
			this.pnlInstruction.setBackground(Color.white);
		}
	}

	/**
	 * Sets the value of the <code>MFSInstructionRec</code> field.
	 * @param instructionRec the new <code>MFSInstructionRec</code>
	 */
	public void setInstructionRec(MFSInstructionRec instructionRec)
	{
		this.fieldInstructionRec = instructionRec;
	}

	/**
	 * Sets whether this panel is for a non-part instruction.
	 * @param nonPart <code>true</code> iff this panel is for a non-part
	 *        instruction
	 */
	public void setIsNonPartInstruction(boolean nonPart)
	{
		this.fieldIsNonPartInstruction = nonPart;
	}

	/**
	 * Sets the background color of {@link #pnlNoPart}.
	 * @param color the background <code>Color</code>
	 */
	public void setNoPartPanelBackground(Color color)
	{
		this.pnlNoPart.setBackground(color);
	}

	/**
	 * Undoes a color change.
	 * @see #changeColor(Color)
	 */
	public void unDoChangeColor()
	{
		if (this.fieldInstructionRec.getCompletionStatus().equals("C")) //$NON-NLS-1$
		{
			setInstPanelBackground_Complete();
			if (this.pnlNoPart.isVisible())
			{
				this.pnlNoPart.setBackground(Color.cyan);
				this.lblNoPart.setForeground(Color.black);
			}
		}
		else
		{
			setInstPanelBackground_NotComplete();
			if (this.pnlNoPart.isVisible())
			{
				this.pnlNoPart.setBackground(Color.white);
				this.lblNoPart.setForeground(Color.black);
			}
		}
	}

	//~1A New method
	/**
	 * Validates that the status of the <code>MFSInstructionRec</code> has
	 * changed and sets the changed property to <code>false</code> if the
	 * status has not changed.
	 */
	public void validateChangedStatus()
	{
		String curr = this.fieldInstructionRec.getCompletionStatus();
		String orig = this.fieldInstructionRec.getCompletionStatusOriginal();
		if (curr.equals(orig))
		{
			this.fieldInstructionRec.setChanged(false);
		}
	}

	//~1A New class
	/**
	 * <code>CompleteLabelIcon</code> is the icon for the complete label.
	 * @author The MFS Client Development Team
	 */
	private static class CompleteLabelIcon
		implements Icon
	{
		/** The icon's width. */
		private static final int ICON_WIDTH = 10;

		/** The icon's width minus 1. */
		private static final int ICON_WIDTH_M1 = ICON_WIDTH - 1;

		/** The icon's width minus 2. */
		private static final int ICON_WIDTH_M2 = ICON_WIDTH - 2;

		/** The icon's height. */
		private static final int ICON_HEIGHT = 9;

		/** The icon's height minus 1. */
		private static final int ICON_HEIGHT_M1 = ICON_HEIGHT - 1;

		/** The icon's height minus 2. */
		private static final int ICON_HEIGHT_M2 = ICON_HEIGHT - 2;

		/** the middle of the icon's height. */
		private static final int MID_HEIGHT = 5;

		/** The completion status. */
		protected int status;

		/** Constructs a new <code>CompleteLabelIcon</code>. */
		public CompleteLabelIcon()
		{
			super();
		}

		/**
		 * Paints the <code>CompleteLabelIcon</code> with the top-left corner
		 * drawn at the point (x, y) in the coordinate space of the specified
		 * <code>Graphics</code> context <code>g</code>.
		 * @param c a <code>Component</code> used to get properties useful for
		 *        painting (e.g., foreground or background color)
		 * @param g the <code>Graphics</code> used to paint the
		 *        <code>CompleteLabelIcon</code>
		 * @param x the x coordinate of the <code>CompleteLabelIcon</code>'s
		 *        top-left corner
		 * @param y the y coordinate of the <code>CompleteLabelIcon</code>'s
		 *        top-left corner
		 */
		public void paintIcon(Component c, Graphics g, int x, int y)
		{
			Color savedColor = g.getColor();
			g.translate(x, y);
			switch (this.status)
			{
				case INCOMPLETE:
					g.setColor(Color.black);
					g.drawRect(0, 0, ICON_WIDTH_M1, ICON_HEIGHT_M1);
					g.setColor(Color.darkGray);
					g.drawLine(0, ICON_HEIGHT_M1, ICON_WIDTH_M1, ICON_HEIGHT_M1);
					g.setColor(Color.white);
					g.fillRect(1, 1, ICON_WIDTH_M2, ICON_HEIGHT_M2);
					break;
				case PARTIALLY_COMPLETE:
					g.setColor(Color.black);
					g.drawRect(0, 0, ICON_WIDTH_M1, ICON_HEIGHT_M1);
					g.fillRect(0, MID_HEIGHT, ICON_WIDTH, ICON_HEIGHT - MID_HEIGHT);
					g.setColor(Color.white);
					g.fillRect(1, 1, ICON_WIDTH_M2, MID_HEIGHT - 1);
					break;
				case COMPLETE:
					g.setColor(Color.black);
					g.fillRect(0, 0, ICON_WIDTH, ICON_HEIGHT);
					break;
			}
			g.translate(-x, -y);
			g.setColor(savedColor);
		}

		/**
		 * Returns the icon's width.
		 * @return the icon's width
		 */
		public int getIconWidth()
		{
			return ICON_WIDTH;
		}

		/**
		 * Returns the icon's height.
		 * @return the icon's height
		 */
		public int getIconHeight()
		{
			return ICON_HEIGHT;
		}
	}
}
