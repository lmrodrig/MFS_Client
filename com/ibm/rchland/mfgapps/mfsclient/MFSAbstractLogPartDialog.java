/* © Copyright IBM Corporation 2006, 2011. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2006-06-29      31801JM  R Prechel        -Initial version
 * 2007-02-26   ~1 34242JR  R Prechel        -Java 5 version
 * 2010-11-02  ~02 49513JM  Toribio H.       -Create setValueLabelText method
 * 2010-11-15  ~03 49513JM  Toribio H.       -Remember if Coo was actually manually scanned or retrieved.
 * 2011-12-05  ~04 50723FR Toribio H.      -Fix a bug in Validate SequenceNumber (when Serial is 7 chars length it fails)
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.WindowEvent;
import java.util.Hashtable;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.ibm.rchland.mfgapps.client.utils.messagebox.IGSMessageBox;
import com.ibm.rchland.mfgapps.mfsclient.barcode.MFSBCBarcodeDecoder;
import com.ibm.rchland.mfgapps.mfsclient.barcode.MFSBCIndicatorValue;
import com.ibm.rchland.mfgapps.mfsclient.barcode.MFSBCPartObject;
import com.ibm.rchland.mfgapps.mfsclient.struct.MFSComponentRec;
import com.ibm.rchland.mfgapps.mfsclient.struct.MFSHeaderRec;
import com.ibm.rchland.mfgapps.mfsclient.struct.MFSPartInformation;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSConstants;
import com.ibm.rchland.mfgapps.mfscommon.MFSConfig;

/**
 * <code>MFSAbstractLogPartDialog</code> is an abstract base class for
 * <code>MFSActionableDialog</code>s that prompt the user for part
 * information in order to log the part information. An
 * <code>MFSAbstractLogPartDialog</code> uses a series of up to seven
 * name/value <code>JLabel</code> pairs to prompt for and display the required
 * part information.
 * @author The MFS Client Development Team
 */
@SuppressWarnings("serial")
public abstract class MFSAbstractLogPartDialog
	extends MFSActionableDialog
{
	/** The <b>Part Quantity</b> name <code>JLabel</code> text. */
	protected static final String PART_QUANTITY = "Part Quantity"; //$NON-NLS-1$

	/** The <b>Part Number</b> name <code>JLabel</code> text. */
	protected static final String PART_NUMBER = "Part Number"; //$NON-NLS-1$

	/** The <b>EC Number</b> name <code>JLabel</code> text. */
	protected static final String EC_NUMBER = "EC Number"; //$NON-NLS-1$

	/** The <b>Sequence Number</b> name <code>JLabel</code> text. */
	protected static final String SEQUENCE_NUMBER = "Sequence Number"; //$NON-NLS-1$

	/** The <b>Card Assembly</b> name <code>JLabel</code> text. */
	protected static final String CARD_ASSEMBLY = "Card Assembly"; //$NON-NLS-1$

	/** The <b>Machine Serial</b> name <code>JLabel</code> text. */
	protected static final String MACHINE_SERIAL = "Machine Serial"; //$NON-NLS-1$

	/** The <b>Control Number</b> name <code>JLabel</code> text. */
	protected static final String CONTROL_NUMBER = "Control Number"; //$NON-NLS-1$

	/** The <b>Country</b> name <code>JLabel</code> text. */
	protected static final String COUNTRY = "Country"; //$NON-NLS-1$
	
	/** The empty <code>String</code>. */
	protected static final String EMPTY_STRING = ""; //$NON-NLS-1$

	//~1C Use array of labels
	/** The name <code>JLabel</code>s. */
	private JLabel[] fieldNameLabels = {
			createSmallNameLabel(EMPTY_STRING),
			createSmallNameLabel(EMPTY_STRING),
			createSmallNameLabel(EMPTY_STRING),
			createSmallNameLabel(EMPTY_STRING),
			createSmallNameLabel(EMPTY_STRING),
			createSmallNameLabel(EMPTY_STRING),
			createSmallNameLabel(EMPTY_STRING),
	};

	//~1C Use array of labels
	/** The value <code>JLabel</code>s. */
	private JLabel[] fieldValueLabels = {
			createSmallValueLabel(this.fieldNameLabels[0]),
			createSmallValueLabel(this.fieldNameLabels[1]),
			createSmallValueLabel(this.fieldNameLabels[2]),
			createSmallValueLabel(this.fieldNameLabels[3]),
			createSmallValueLabel(this.fieldNameLabels[4]),
			createSmallValueLabel(this.fieldNameLabels[5]),
			createSmallValueLabel(this.fieldNameLabels[6])
	};

	/** The input <code>JTextField</code>. */
	protected final JTextField tfInput = createTextField(MFSConstants.LARGE_TF_COLS, 0);

	/** The <code>JPanel</code> that displays the name/value labels. */
	private final JPanel pnlLabels = new JPanel(new GridLayout(7, 2, 10, 5));

	/** The <code>JPanel</code> that displays the <code>JButton</code>s. */
	protected final JPanel pnlButtons = new JPanel(new GridLayout(0, 2, 5, 3));

	/** The <code>MFSHeaderRec</code> for the work unit. */
	protected MFSHeaderRec fieldHeaderRec; //~1A

	/** Section to add Members that suport extra input information that will be used 
	 *  when logging 
	 */
	/** A <code>Hashtable</code> to save coo values **/
	private Hashtable<String, String> cooHash = null;

	/** The <b>scanned Header</b> retrieved by the <code>barcode</code> decoder. */
	private String scannedHeader = EMPTY_STRING; //~02

	/** The <b>scanned Coo</b> if manually scanned or F6 selected by the user. */
	private String scannedCoo = EMPTY_STRING; //~03

	/**
	 * Constructs a new modal <code>MFSAbstractLogPartDialog</code> with the
	 * specified <code>title</code> and the specified <code>parent</code> as
	 * owner. By default, an <code>MFSAbstractLogPartDialog</code> is not
	 * resizeable.
	 * @param parent the <code>MFSFrame</code> from which this
	 *        <code>MFSAbstractLogPartDialog</code> is displayed
	 * @param title the <code>String</code> to display in the title bar
	 * @see #getParentFrame()
	 */
	public MFSAbstractLogPartDialog(MFSFrame parent, String title)
	{
		super(parent, title);

		//~1C Setup the name/value label panel
		for (int i = 0; i < this.fieldNameLabels.length; i++)
		{
			this.pnlLabels.add(this.fieldNameLabels[i]);
			this.pnlLabels.add(this.fieldValueLabels[i]);
		}

		//~1C Determine the desired size of the labels.
		//The panel uses a GridLayout, so all labels will be the same size.
		setLabelDimensions(this.fieldNameLabels[0], 16);

		//Do NOT call createLayout here!
		//Due to the order of initialization in Java
		//createLayout MUST be called in the concrete subclass
	}

	/**
	 * Adds this dialog's <code>Component</code>s to the layout.
	 * @param extraComponents <code>true</code> if the
	 *        {@link #addExtraComponents(JPanel, GridBagConstraints)} method
	 *        should be called when laying out this dialog's components
	 */
	protected void createLayout(boolean extraComponents)
	{
		final int GBC_WIDTH = extraComponents ? 2 : 1;

		JPanel contentPane = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints(0, 0, GBC_WIDTH, 1, 0, 0,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, 
				new Insets(10, 0, 0, 0), 0, 0);

		JLabel scanLabel = createLabel("Scan in Barcode..."); //$NON-NLS-1$
		contentPane.add(scanLabel, gbc);

		gbc.gridy++;
		gbc.insets = new Insets(5, 0, 0, 0);
		contentPane.add(this.tfInput, gbc);

		if (extraComponents)
		{
			addExtraComponents(contentPane, gbc);
		}

		gbc.gridx = 0;
		gbc.gridy++;
		gbc.gridwidth = GBC_WIDTH;
		gbc.gridheight = 1;
		gbc.anchor = GridBagConstraints.CENTER; //~1A
		gbc.insets = new Insets(5, 4, 4, 4);
		contentPane.add(this.pnlButtons, gbc);

		//~1D Do not add getActionIndicator()
		//Added by MFSActionableDialog.setContentPane

		gbc.gridx = GBC_WIDTH;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.gridheight = extraComponents ? 5 : 3;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.insets = new Insets(4, 4, 4, 4);
		contentPane.add(this.pnlLabels, gbc);

		this.setContentPane(contentPane);
		pack(); //~1A
	}

	/**
	 * Hook method called when laying out this dialog's components. Allows a
	 * subclass to add extra components.
	 * @param contentPane the <code>JPanel</code> that will become this
	 *        dialog's content pane
	 * @param gbc the <code>GridBagConstraints</code> used to layout the
	 *        content pane
	 */
	protected void addExtraComponents(JPanel contentPane, GridBagConstraints gbc)
	{
		//Note: This method is meant to be overridden
		System.out.print("addExtraComponents hook called with "); //$NON-NLS-1$
		System.out.println(contentPane + " and " + gbc); //$NON-NLS-1$
	}

	//~1A New method
	/**
	 * Returns <code>true</code> iff all input has been collected.
	 * @return <code>true</code> iff all input has been collected
	 */
	protected boolean allInputCollected()
	{
		for (int i = 0; i < this.fieldNameLabels.length; i++)
		{
			if (!this.fieldNameLabels[i].getText().equals(EMPTY_STRING) &&
					this.fieldValueLabels[i].getText().equals(EMPTY_STRING))
			{
				return false;
			}
		}
		return true;
	}

	//~1A New method
	/**
	 * Sets up the barcode indicator values used by <code>barcode</code> based
	 * on the specified <code>headerRec</code> and <code>compRec</code>,
	 * calls {@link MFSBCBarcodeDecoder#decodeBarcodeFor}, sets the error
	 * message if <code>decodeBarcodeFor</code> returned <code>false</code>,
	 * and returns the return code.
	 * @param headerRec the <code>MFSHeaderRec</code> for the work unit
	 * @param compRec the <code>MFSComponentRec</code> for which logging is
	 *        being performed
	 * @param barcode the <code>MFSBCBarcodeDecoder</code> used to decode
	 *        barcodes
	 * @return zero on success; nonzero on failure
	 */
	protected int decodeBarcode(MFSHeaderRec headerRec, MFSComponentRec compRec,
								MFSBCBarcodeDecoder barcode)
	{
		/* Setup barcode indicator values. */
		MFSBCIndicatorValue bcIndVal = new MFSBCIndicatorValue();

		if (compRec.isPnriDoNotCollect() == false)
		{
			bcIndVal.setPNRI(compRec.getPnri());
		}
		if (compRec.isEcriDoNotCollect() == false)
		{
			bcIndVal.setECRI(compRec.getEcri());
		}
		if (compRec.isCsniDoNotCollect() == false)
		{
			bcIndVal.setCSNI(compRec.getCsni());
		}
		if (compRec.isCcaiDoNotCollect() == false)
		{
			bcIndVal.setCCAI(compRec.getCcai());
		}
		if (compRec.isCmtiDoNotCollect() == false)
		{
			bcIndVal.setCMTI(compRec.getCmti());
		}
		if (compRec.isCooiDoNotCollect() == false)
		{
			bcIndVal.setCOOI(compRec.getCooi());
		}

		bcIndVal.setPRLN(headerRec.getPrln());
		bcIndVal.setITEM(compRec.getItem());

		barcode.setMyBCIndicatorValue(bcIndVal);

		int rc;
		if (barcode.decodeBarcodeFor(this))
		{
			rc = barcode.getBCMyPartObject().getRC();
		}
		else
		{
			rc = 10;
			barcode.getBCMyPartObject().setErrorString(
					MFSBCBarcodeDecoder.DECODE_TIMED_OUT_ERMS);
		}
		return rc;
	}
	
	//~03 
	/** get the Coo hashTable from the {MFSConfig} singleTon reference (Encapsulation fix) 
	 */
	protected Hashtable<String, String> getCooHash() {
		if(this.cooHash == null)
		{
			this.cooHash = MFSConfig.getInstance().getCooHash();
		}
		return this.cooHash;
	}

	//~02
	protected void setScannedHeader(String scannedHeader) {
		this.scannedHeader = scannedHeader;
	}
	//~02
	protected String getScannedHeader() {
		return scannedHeader;
	}
	//~03
	protected String getScannedCoo() {
		return scannedCoo;
	}
	//~03
	protected void setScannedCoo(String scannedCoo) {
		this.scannedCoo = scannedCoo;
	}

	/**
	 * Clears the text of the input <code>JTextField</code>, brings the
	 * dialog to the front, and requests the focus for the input
	 * <code>JTextField</code>.
	 */
	protected void focusForInput()
	{
		this.tfInput.setText(EMPTY_STRING);
		this.toFront();
		this.tfInput.requestFocusInWindow();
	}

	//~1A New method
	/**
	 * Returns the value label for the specified <code>name</code> or
	 * <code>null</code> if no value label corresponds to <code>name</code>.
	 * @param name the name of the value label
	 * @param labelLimit. The limit label number to search for, 0 if all required.
	 * @return the value label or <code>null</code>
	 */
	protected JLabel getValueLabel(String name, int labelLimit)
	{
		if(labelLimit == 0 || labelLimit > this.fieldNameLabels.length) {
			labelLimit = this.fieldNameLabels.length;
		}		
		for (int i = 0; i < this.fieldNameLabels.length; i++)
		{
			if (this.fieldNameLabels[i].getText().equals(name))
			{
				return this.fieldValueLabels[i];
			}
		}
		return null;
	}

	//~02 New method
	/** 
	 * Sets the value label text for the specified <code>name</code>.
	 * @param labelName. The name of the value label.
	 * @param labelLimit. The limit label number to search for, 0 if all required.
	 * @param labelText. The text of the value label.
	 * @return <code>true</code> if the labelname label was set
	 */
	protected boolean setValueLabelText(String labelName, int labelLimit, String labelText) {
		JLabel localLabel = this.getValueLabel(labelName, labelLimit);
		
		if(localLabel != null) {
			localLabel.setText(labelText);
			return true;
		}		
		return false;
	}

	//~1A New method
	/**
	 * Clears the text of the value label(s) for the specified <code>name</code>.
	 * @param name the name of the value label
	 */
	protected void clearValueLabelText(String name)
	{
		this.setValueLabelText(name, 0, EMPTY_STRING);
	}

	//~02 New method
	/** 
	 * Shifts the values from the specified position.
	 * @param name. New Labels Name.
	 * @param index. Position to start shifting up.
	 */
	protected void pushLabel(String name, int index) {
		// if already Blank don't push
		if(!this.fieldNameLabels[index - 1].getText().equals(EMPTY_STRING)) {
			for (int i = (this.fieldNameLabels.length - 1); i >= index; i--)
			{
				this.fieldNameLabels[i].setText(this.fieldNameLabels[(i - 1)].getText());
				this.fieldValueLabels[i].setText(this.fieldValueLabels[(i - 1)].getText());
			}			
		}
		this.fieldNameLabels[index - 1].setText(name);
		this.fieldValueLabels[index - 1].setText(EMPTY_STRING);		
	}

	/**
	 * Initializes the appearance of the dialog.
	 * <p>
	 * A concrete implementation should call
	 * {@link #initTextComponents(String[][])} to set up the text of the name
	 * and value <code>JLabel</code>s. If the title of the dialog depends on
	 * the type of logging being performed, a concrete implementation should
	 * also set the title of the dialog.
	 */
	public abstract void initDisplay();

	//~1A New method
	/**
	 * Sets the text of the name and value labels, clears the text of the input
	 * text field, and requests the focus for the input text field.
	 * @param labelText a 7 X 2 array with 7 pairs of <code>String</code>s.
	 *        The first <code>String</code> of each pair is the name label
	 *        text. The second <code>String</code> is the value label text.
	 */
	protected void initTextComponents(String[][] labelText)
	{
		for (int i = 0; i < this.fieldNameLabels.length; i++)
		{
			this.fieldNameLabels[i].setText(labelText[i][0]);
			this.fieldValueLabels[i].setText(labelText[i][1]);
		}
		this.scannedHeader = EMPTY_STRING; //~03
		this.scannedCoo = EMPTY_STRING; //~03		
		this.tfInput.setText(EMPTY_STRING);
		this.tfInput.requestFocusInWindow();
	}

	//~1A New method
	/**
	 * Returns <code>true</code> iff the collection flags for the
	 * <code>MFSComponentRec</code> indicate no input needs to be collected
	 * and the quantity field of the <code>MFSBCPartObject</code> is empty.
	 * <p>
	 * Note: The following collection flags are checked: ecri, csni, ccai, cmti,
	 * pari, pnri, cooi.
	 * @param compRec the <code>MFSComponentRec</code> for which logging is
	 *        being performed
	 * @param partObject the <code>MFSBCPartObject</code> containing the
	 *        information decoded by the <code>MFSBCBarcodeDecoder</code>
	 * @return <code>true</code> if there is no input to collect
	 */
	protected boolean noInputToCollect(MFSComponentRec compRec, MFSBCPartObject partObject)
	{
		return compRec.isEcriDoNotCollect() && 
				compRec.isCsniDoNotCollect() &&
				compRec.isCcaiDoNotCollect() && 
				compRec.isCmtiDoNotCollect() &&
				compRec.isPariDoNotCollect() && 
				compRec.isPnriDoNotCollect() && 
				compRec.isCooiDoNotCollect() && 
				partObject.getQT().equals(EMPTY_STRING);
	}

	/**
	 * Parses the field values from the value <code>JLabel</code>s and stores
	 * the values in a {@link MFSPartInformation} pure data structure.
	 * @param setECNone <code>true</code> if the default value of the
	 *        {@link MFSPartInformation#ec ec} field should be
	 *        {@link MFSPartInformation#EC_NONE}
	 * @return the <code>PartInformation</code> containing the field values
	 */
	protected MFSPartInformation parsePartInfo(boolean setECNone)
	{
		MFSPartInformation result = new MFSPartInformation(setECNone);		
		JLabel labelContainer = null;
		
		labelContainer = this.getValueLabel(PART_QUANTITY, 1);
		if(null != labelContainer && !labelContainer.getText().equals(EMPTY_STRING)) {
			result.qt = Integer.parseInt(labelContainer.getText());
			result.overrideQty = true;
		}
		labelContainer = this.getValueLabel(PART_NUMBER, 2);
		if(null != labelContainer && !labelContainer.getText().equals(EMPTY_STRING)) {
			result.pn = labelContainer.getText();
		}		
		labelContainer = this.getValueLabel(EC_NUMBER, 3);
		if(null != labelContainer && !labelContainer.getText().equals(EMPTY_STRING)) {
			result.ec = labelContainer.getText();
		}		
		labelContainer = this.getValueLabel(SEQUENCE_NUMBER, 4);
		if(null != labelContainer && !labelContainer.getText().equals(EMPTY_STRING)) {
			result.sn = labelContainer.getText();
		}		
		labelContainer = this.getValueLabel(CARD_ASSEMBLY, 5);
		if(null != labelContainer && !labelContainer.getText().equals(EMPTY_STRING)) {
			result.ca = labelContainer.getText();
		}		
		labelContainer = this.getValueLabel(MACHINE_SERIAL, 6);
		if(null != labelContainer && !labelContainer.getText().equals(EMPTY_STRING)) {
			result.ms = labelContainer.getText();
		}		
		labelContainer = this.getValueLabel(COUNTRY, 7);
		if(null != labelContainer && !labelContainer.getText().equals(EMPTY_STRING)) {
			result.co = labelContainer.getText();
		}		
		labelContainer = this.getValueLabel(CONTROL_NUMBER, 7);
		if(null != labelContainer && !labelContainer.getText().equals(EMPTY_STRING)) {
			result.wu = labelContainer.getText();
		}		
		return result;
	}

	/** Processes the input the user entered in the <code>JTextField</code>. */
	protected abstract void recvInput();

	//~1A New method
	/**
	 * Validates the scanned in machine serial.
	 * @param compRec the <code>MFSComponentRec</code> for which logging is
	 *        being performed
	 * @param partObject the <code>MFSBCPartObject</code> containing the
	 *        information decoded by the <code>MFSBCBarcodeDecoder</code>
	 * @return <code>true</code> iff the machine serial is valid
	 */
	protected boolean validateMachineSerial(MFSComponentRec compRec,
											MFSBCPartObject partObject)
	{
		boolean valid = false;

		/* If Mach Type is blank */
		if (partObject.getMTC().equals("")) //$NON-NLS-1$
		{
			/* If the 1st Char is a '$' then assign new Machine Serial */
			if (compRec.getMcsn().substring(0, 1).equals("$")) //$NON-NLS-1$
			{
				valid = true;
			}
			/* Verify the Machine Serial */
			else
			{
				if (partObject.getMSN().substring(2).equals(compRec.getMcsn())
						&& partObject.getMSN().substring(0, 2).equals(compRec.getMspi()))
				{
					valid = true;
				}
			}
		}
		/* If Mach Type is populated */
		else
		{
			/* Check if machine types match */
			if (partObject.getMTC().equals(compRec.getMatp()))
			{
				/* If the 1st Char is a '$' then assign new Machine Serial */
				if (compRec.getMcsn().substring(0, 1).equals("$")) //$NON-NLS-1$
				{
					valid = true;
				}
				/* Verify the Machine Serial */
				else
				{
					if (partObject.getMSN().substring(2).equals(compRec.getMcsn())
							&& partObject.getMSN().substring(0, 2).equals(compRec.getMspi()))
					{
						valid = true;
					}
				}
			}
		}
		return valid;
	}

	//~1A New method
	/**
	 * Validates the scanned in sequence number.
	 * @param compRec the <code>MFSComponentRec</code> for which logging is
	 *        being performed
	 * @param partObject the <code>MFSBCPartObject</code> containing the
	 *        information decoded by the <code>MFSBCBarcodeDecoder</code>
	 * @return <code>true</code> iff the sequence number is valid
	 * 
	 * ~04 Fix bug when Sequence is 7 chars. 
	 */
	protected boolean validateSequenceNumber(MFSComponentRec compRec,
												MFSBCPartObject partObject)
	{
		boolean valid = true;
		String SNString = partObject.getSN();
		char[] SNArray = SNString.toCharArray();
		String alphaNumeric = " ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"; //$NON-NLS-1$

		/* SN must be alphanumeric */
		for (int i = 0; i < SNArray.length; i++)
		{
			if (alphaNumeric.indexOf(SNArray[i]) == -1)
			{
				valid = false;
			}
		}
		//SN length must be between 7 and 12
		//SN can't start with "SN" or "11S"
		//SN can't equal PN or 'P' followed by PN
		if (valid)
		{
			if (SNString.length() < 7 || SNString.length() > 12
					|| SNString.substring(0, 2).equals("SN") //$NON-NLS-1$
					|| SNString.substring(0, 3).equals("11S") //$NON-NLS-1$
					|| SNString.substring(0, 7).equals(compRec.getItem())
					|| (SNString.length() >= 8 && SNString.substring(0, 8).equals("P" + compRec.getItem()))) //$NON-NLS-1$
			{
				valid = false;
			}
		}
		return valid;
	}

	/**  
	 *  @return if found true, otherwise false
	 */
	protected boolean calculateCoo(String wuCoo, MFSBCPartObject partObject)
	{	
		boolean found = false;
		/* Retrieved Coo by Significant Part has 1st priority */
		if (wuCoo != null && !wuCoo.equals(EMPTY_STRING))
		{
			found = setValueLabelText(COUNTRY, 7, wuCoo); 
			/* Overiding the scanned Coo is not allowed */
			this.setScannedCoo(EMPTY_STRING);
		}								
		/* Scanned CO has 2nd priority */
		else if (!this.getScannedCoo().equals(EMPTY_STRING))
		{
			found = setValueLabelText(COUNTRY, 7, this.getScannedCoo()); 
		}
		else 
		{ 	/* Get the Coo from the saved values */
			String savedCoo = getCooFromHash(partObject);
			if (null != savedCoo)
			{
				found = setValueLabelText(COUNTRY, 7, savedCoo); 
			}
			/* CO */
			else if (!(partObject.getCO().equals(EMPTY_STRING)))					
			{
				found = setValueLabelText(COUNTRY, 7, partObject.getCO()); 
			}
		}
		return found;		
	}
	
	/** Validates if the new header is compatible with Pre populated Coo,
	  *  if not, clear it. 
	  */
	protected void chkCooForNewHeader(String newHeader)
	{
		JLabel cooLabel = this.getValueLabel(COUNTRY, 0);
		if(cooLabel != null && !cooLabel.getText().equals(EMPTY_STRING))
		{
			JLabel partLabel = this.getValueLabel(PART_NUMBER, 0);
			if(partLabel != null && !partLabel.getText().equals(EMPTY_STRING))
			{
				String cooKey = partLabel.getText() + cooLabel.getText();
				String hdrValue = this.getCooHash().get(cooKey);
				if(hdrValue == null || !hdrValue.equals(newHeader))
				{
					this.clearValueLabelText(COUNTRY);
				}
			}
		}
	}
	
	/** We need to save both versions, By Part only and by Part & Header, if applies.
	 *  So when the Dialog is initiate we can defualt by the last coo used by the part.
	 *  Even if we do not know at this point what header we will scan
	 */
	protected void saveCooInHash(String part, String coo) { 
		String cooValue = null;
		/* Save the last Coo used by the part (Coo by Part)
		 * This will work to pre populate the CO when the Scan dialog is initiated.
		 * Also need the HDER in case it has one. To be able to tell if the Default Coo,
		 *  will work for the future scanned parts, remember if we have a difault 
		 *  prepopulated for XXXXX header, and user scans another part, same pn but diff Coo,
		 *  we need to blank the coo, or search in the hash by Pn and Coo to autoretrieve it.
		 */
		cooValue = this.getCooHash().get(part);
		if (cooValue == null || !cooValue.equals(coo))
		{
			this.getCooHash().put(part, coo);
		}
		//Save the Coo by Part and Header, if it has a header (Coo by Part & Header). 
		if(!this.getScannedHeader().equals(EMPTY_STRING)) 
		{
			String cooKey = part + this.getScannedHeader();
			cooValue = this.getCooHash().get(cooKey);
			if (cooValue == null || !cooValue.equals(coo))
			{
				this.getCooHash().put(cooKey, coo);
			}
			/* Also Save Header by Part & Coo,
			 *  will be directly linked to Coo by Part.
			 *  Used to see if Coo is valid for X headers of a part.
			 */
			cooKey = part + coo;
			cooValue = this.getCooHash().get(cooKey);
			if (cooValue == null || !cooValue.equals(this.getScannedHeader()))
			{
				this.getCooHash().put(cooKey, this.getScannedHeader());
			}							

		}
	}

	/** Gets the saved Coo value from the cooHash */ 
	private String getCooFromHash(MFSBCPartObject partObject)
	{
		JLabel partLabel = this.getValueLabel(PART_NUMBER, 2);
		if(partLabel != null && !partLabel.getText().equals(EMPTY_STRING))
		{
			String cooKey = null;
			if(this.getScannedHeader().equals(EMPTY_STRING)) {
				cooKey = partLabel.getText();
			}
			else {
				cooKey = partLabel.getText() + this.getScannedHeader();
			}
			return this.getCooHash().get(cooKey);
		}
		return null;
	}
	
	/**
	 * Verifies that input has been collected for all required fields. Displays
	 * an error message, clears the input <code>JTextField</code>, and
	 * requests the focus for the input <code>JTextField</code> if any field
	 * values are missing.
	 * @return <code>true</code> if input has been collected for all required
	 *         fields; <code>false</code> if input has not been collected for
	 *         all required fields
	 */
	protected boolean verifyAllInputCollected()
	{
		boolean result = allInputCollected(); //~1C

		if (result == false)
		{
			String message = "All fields must be filled in."; //$NON-NLS-1$
			IGSMessageBox.showOkMB(this, null, message, null);
			this.tfInput.setText(EMPTY_STRING);
			this.tfInput.requestFocusInWindow();
			this.toFront();
		}

		return result;
	}

	/**
	 * Invoked the first time a window is made visible.
	 * <p>
	 * Requests the focus for the {@link #tfInput}.
	 * @param we the <code>WindowEvent</code>
	 */
	public void windowOpened(WindowEvent we)
	{
		this.tfInput.requestFocusInWindow();
	}
}
