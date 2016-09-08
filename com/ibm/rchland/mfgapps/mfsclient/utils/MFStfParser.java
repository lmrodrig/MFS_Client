/* © Copyright IBM Corporation 2008, 2012. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2008-03-20      37616JL  D Pietrasik      -Initial version
 * 2012-11-14  ~01 00205020 Edgar Mercado    -Add MFStfPalletParser to support string of 13 chars
 *                                            on input for Pallet ID.
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.utils;

import com.ibm.rchland.mfgapps.mfsclient.barcode.MFSBCBarcodeDecoder;
import com.ibm.rchland.mfgapps.mfsclient.barcode.MFSBCIndicatorValue;
import com.ibm.rchland.mfgapps.mfsclient.barcode.MFSBCPartObject;
import com.ibm.rchland.mfgapps.mfscommon.MFSActionable;
import com.ibm.rchland.mfgapps.mfscommon.exception.MFSException;

/**
 * 
 * <code>MFStfParser</code> is used by 
 * {@link com.ibm.rchland.mfgapps.mfsclient.dialog.MFSGetValueDialog} to parse the
 * value entered.
 * @author The MFS Client Development Team
 */
public interface MFStfParser
{
	/**
	 * Processes the input the user entered in the parameter in whatever
	 * particular way is needed and return the value we want for further
	 * processing.
	 * @param tfInput Input from text field that needs to be parsed
	 * @return parsed as requested into String
	 * @throws MFSException Not set up for parsing, or parsing exception
	 */
	public String recvInput(String tfInput)
		throws MFSException;

	/**
	 * Generic dialog class to get one value and perform an action
	 * @author The MFS Client Development Team
	 */
	public static class MFStfCntrParser implements MFStfParser {
		/** actionable dialog for indicating barcode parse processing */
		private MFSActionable actionable = null;

		/**
		 * Constructs a new <code>MFStfParser</code>.
		 * @param actionable <code>MFSActionable</code> used to display
		 *        barcode progress indicator
		 */
		public MFStfCntrParser(MFSActionable actionable) {
			super();
			this.actionable = actionable;
		}
		
		
		/**
		 * Processes the input the user entered in the <code>JTextField</code>.
		 * First try processing it as a barcode. If that doesn't work, check if
		 * we want to process as straight text.
		 * @param tfInput Input from text field that needs to be parsed
		 * @return parsed as requested into String
		 * @throws MFSException Not set up for parsing, or parsing exception
		 */
		public String recvInput(String tfInput) throws MFSException {
			/** parsed form of value that was input */
			String inputValue = null;

			tfInput = tfInput.trim().toUpperCase();

			if (tfInput.length() != 0) {
				MFSBCBarcodeDecoder barcode = MFSBCBarcodeDecoder.getInstance();
				barcode.setMyBarcodeInput(tfInput);
				barcode.setMyBCPartObject(new MFSBCPartObject());
				barcode.setMyBCIndicatorValue(new MFSBCIndicatorValue());

				barcode.decodeBarcodeFor(this.actionable);
				inputValue = "0000000000"; //$NON-NLS-1$

				/* CNTR, ATPE = 'N' in BC10 */
				if (!(barcode.getBCMyPartObject().getCT().equals(""))) {
					inputValue += barcode.getBCMyPartObject().getCT();
				}
				/* PN, ATPE = 'P' in BC10 */
				else if (!(barcode.getBCMyPartObject().getPN().equals(""))) {
					inputValue += barcode.getBCMyPartObject().getPN();
				} else {
					inputValue += tfInput;
				}

				/* match on last 10 chars */
				inputValue = inputValue.substring(inputValue.length() - 10);
			}
			return inputValue;
		}
	}

	
	/**
	 * Generic dialog class to get one value and perform an action
	 * @author The MFS Client Development Team
	 */
	public static class MFStfItemParser implements MFStfParser {
		/** actionable dialog for indicating barcode parse processing */
		private MFSActionable actionable = null;

		/**
		 * Constructs a new <code>MFStfParser</code>.
		 * @param actionable <code>MFSActionable</code> used to display
		 *        barcode progress indicator
		 */
		public MFStfItemParser(MFSActionable actionable) {
			super();
			this.actionable = actionable;
		}
		
		
		/**
		 * Processes the input the user entered in the <code>JTextField</code>.
		 * First try processing it as a barcode. If that doesn't work, check if
		 * we want to process as straight text.
		 * @param tfInput Input from text field that needs to be parsed
		 * @return parsed as requested into String
		 * @throws MFSException Not set up for parsing, or parsing exception
		 */
		public String recvInput(String tfInput) throws MFSException {
			/** parsed form of value that was input */
			String inputValue = null;

			tfInput = tfInput.trim().toUpperCase();

			if (tfInput.length() != 0) {
				MFSBCBarcodeDecoder barcode = MFSBCBarcodeDecoder.getInstance();
				barcode.setMyBarcodeInput(tfInput);
				barcode.setMyBCPartObject(new MFSBCPartObject());
				barcode.setMyBCIndicatorValue(new MFSBCIndicatorValue());

				barcode.decodeBarcodeFor(this.actionable);
				inputValue = "000000000000"; //$NON-NLS-1$

				/* CNTR, ATPE = 'N' in BC10 */
				if (!(barcode.getBCMyPartObject().getCT().equals(""))) {
					inputValue += barcode.getBCMyPartObject().getCT();
				}
				/* PN, ATPE = 'P' in BC10 */
				else if (!(barcode.getBCMyPartObject().getPN().equals(""))) {
					inputValue += barcode.getBCMyPartObject().getPN();
				} else {
					inputValue += tfInput;
				}

				/* match on last 10 chars */
				inputValue = inputValue.substring(inputValue.length() - 12);
			}
			return inputValue;
		}
	}
	
	/** ~01A
	 * Generic dialog class to get one value and perform an action
	 * @author The MFS Client Development Team
	 */
	public static class MFStfPalletParser implements MFStfParser {
		/** actionable dialog for indicating barcode parse processing */
		private MFSActionable actionable = null;

		/**
		 * Constructs a new <code>MFStfParser</code>.
		 * @param actionable <code>MFSActionable</code> used to display
		 *        barcode progress indicator
		 */
		public MFStfPalletParser(MFSActionable actionable) {
			super();
			this.actionable = actionable;
		}
		
		
		/**
		 * Processes the input the user entered in the <code>JTextField</code>.
		 * First try processing it as a barcode. If that doesn't work, check if
		 * we want to process as straight text.
		 * @param tfInput Input from text field that needs to be parsed
		 * @return parsed as requested into String
		 * @throws MFSException Not set up for parsing, or parsing exception
		 */
		public String recvInput(String tfInput) throws MFSException {
			/** parsed form of value that was input */
			String inputValue = null;

			tfInput = tfInput.trim().toUpperCase();

			if (tfInput.length() != 0) {
				MFSBCBarcodeDecoder barcode = MFSBCBarcodeDecoder.getInstance();
				barcode.setMyBarcodeInput(tfInput);
				barcode.setMyBCPartObject(new MFSBCPartObject());
				barcode.setMyBCIndicatorValue(new MFSBCIndicatorValue());

				barcode.decodeBarcodeFor(this.actionable);
				inputValue = "0000000000000"; //$NON-NLS-1$

				/* CNTR, ATPE = 'N' in BC10 */
				if (!(barcode.getBCMyPartObject().getCT().equals(""))) {
					inputValue += barcode.getBCMyPartObject().getCT();
				}
				/* PN, ATPE = 'P' in BC10 */
				else if (!(barcode.getBCMyPartObject().getPN().equals(""))) {
					inputValue += barcode.getBCMyPartObject().getPN();
				} else {
					inputValue += tfInput;
				}

				/* match on last 10 chars */
				inputValue = inputValue.substring(inputValue.length() - 13);
			}
			return inputValue;
		}
	}	
}