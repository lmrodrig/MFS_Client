/* @ Copyright IBM Corporation 2007. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2007-07-25      26157CD  Toribio Hdez.    -Initial version
 * 2010-03-04 ~01  47596MZ  Toribio Hdez.    -Add Auto_LOG Search Criteria	
 * 2010-11-02 ~02  49513JM  Toribio Hdez.    -Add LogPartDialog_SEARCH_REMOVE Criteria
 ******************************************************************************/

package com.ibm.rchland.mfgapps.mfsclient.utils;

import com.ibm.rchland.mfgapps.mfsclient.struct.MFSComponentRec;
import com.ibm.rchland.mfgapps.mfsclient.struct.MFSHeaderRec;
import com.ibm.rchland.mfgapps.mfsclient.struct.MFSInstructionRec;

/**
 * <code>MFSComponentSearchCriteria</code> is used by
 * {@link com.ibm.rchland.mfgapps.mfsclient.utils.MFSComponentSearch}
 * to find a matching <code>MFSComponentRec</code>.
 * @author The MFS Client Development Team
 */
public interface MFSComponentSearchCriteria
{
	/**
	 * Checks if the specified <code>MFSComponentRec</code> matches.
	 * @param component the <code>MFSComponentRec</code> to check
	 * @return <code>true</code> if the <code>MFSComponentRec</code> matches
	 *         the search criteria; <code>false</code> otherwise
	 */
	public boolean match(MFSComponentRec component);

	/**
	 * Checks if the specified <code>instructionRec</code> matches.
	 * @param instructionRec the <code>instructionRec</code> to check
	 * @return <code>true</code> if the <code>MFSInstructionRec</code> matches
	 *         the search criteria; <code>false</code> otherwise
	 */
	public boolean matchInstruction(MFSInstructionRec instructionRec);
		
	/**
	 * <code>MFSPartToRemoveSearchCriteria</code> is an implementation of
	 * <code>MFSComponentSearchCriteria</code> that checks if:
	 * <ul>
	 * <li>the IDSP is R or</li>
	 * <li>the NMBR is FKIT and the IDSP is I</li>
	 * </ul>
	 * @author The MFS Client Development Team
	 */
	public static class MFSPartToRemoveSearchCriteria
		implements MFSComponentSearchCriteria
	{
		/** The {@link MFSHeaderRec}for a work unit. */
		private MFSHeaderRec fieldHeaderRec;

		/**
		 * Constructs a new <code>MFSPartToRemoveSearchCriteria</code>.
		 * @param headerRec the {@link MFSHeaderRec}for a work unit
		 */
		public MFSPartToRemoveSearchCriteria(MFSHeaderRec headerRec)
		{
			this.fieldHeaderRec = headerRec;
		}

		/** {@inheritDoc} */
		public boolean match(MFSComponentRec component)
		{
			return component.getIdsp().equals("R") //$NON-NLS-1$
					|| (this.fieldHeaderRec.getNmbr().equals("FKIT") //$NON-NLS-1$
							&& component.getIdsp().equals("I")); //$NON-NLS-1$
		}
		
		/** {@inheritDoc} */
		public boolean matchInstruction(MFSInstructionRec instructionRec)
		{
			return true;
		}
	}
	/** ~02
	 * <code>MFSPartToRemoveSearchLogCriteria</code> is an implementation of
	 * <code>MFSComponentSearchCriteria</code> that checks if:
	 * <ul>
	 * <li>the IDSP is R or</li>
	 * <li>the NMBR is FKIT and the IDSP is I</li>
	 * <li>the Scanned Part Number matches Component Part Number</li>
	 * <li>the Scanned Serial Number matches Component Serial Number</li>
	 * <li>the Component INVZ is not Z</li>
	 * <li>the Component Qnty is not 00000 and Scanned Qnt is less than rec Qnty</li>
	 * </ul>
	 * @author The MFS Client Development Team
	 */
	public static class MFSPartToRemoveSearchLogCriteria
		implements MFSComponentSearchCriteria
	{
		/** The {@link MFSHeaderRec}for a work unit. */
		private MFSHeaderRec fieldHeaderRec;		
		/** The <code>String</code> for barcode Part Number (scanned). */
		private String bcPN;		
		/** The <code>String</code> for barcode Serial Number (scanned). */
		private String bcSN;
		/** The <code>int</code> for barcode Serial Number (scanned). */
		private int scanQty;

		/**
		 * Constructs a new <code>MFSPartToRemoveSearchCriteria</code>.
		 * @param headerRec the {@link MFSHeaderRec}for a work unit
		 */
		public MFSPartToRemoveSearchLogCriteria(MFSHeaderRec headerRec, String bcPN, String bcSN, int scanQty)
		{
			this.fieldHeaderRec = headerRec;
			this.bcPN = bcPN;
			this.bcSN = bcSN;
			this.scanQty = scanQty;
		}

		/** {@inheritDoc} */
		public boolean match(MFSComponentRec component)
		{
			int recQty = Integer.parseInt(component.getFqty());
			return 
				(component.getIdsp().equals("R") ||//$NON-NLS-1$
				 (this.fieldHeaderRec.getNmbr().equals("FKIT") &&//$NON-NLS-1$
						component.getIdsp().equals("I") && //$NON-NLS-1$
						!component.getInvs().equals("Z"))) && //$NON-NLS-1$
				!component.getQnty().equals("00000") && //$NON-NLS-1$
				component.getInpn().equals(this.bcPN) && 
				this.scanQty <= recQty && 
				(bcSN.equals("") || this.bcSN.equals(component.getInsq())); //$NON-NLS-1$			
		}
		
		/** {@inheritDoc} */
		public boolean matchInstruction(MFSInstructionRec instructionRec)
		{
			return true;
		}
	}
	
	/**
	 * <code>MFSNonSerPartToRemoveSearchCriteria</code> is an implementation of
	 * <code>MFSComponentSearchCriteria</code> that checks if:
	 * <ul>
	 * <li>the IDSP is R or</li>
	 * <li>the NMBR is FKIT and the IDSP is I and INVS is not Z</li>
	 * <li>isCsniDoNotCollect</li>
	 * <li>pnri is not W</li>
	 * </ul>
	 * @author The MFS Client Development Team
	 */
	public static class MFSNonSerPartToRemoveSearchCriteria
		implements MFSComponentSearchCriteria
	{
		/** The {@link MFSHeaderRec}for a work unit. */
		private MFSHeaderRec fieldHeaderRec;

		/**
		 * Constructs a new <code>MFSNonSerPartToRemoveSearchCriteria</code>.
		 * @param headerRec the {@link MFSHeaderRec}for a work unit
		 */
		public MFSNonSerPartToRemoveSearchCriteria(MFSHeaderRec headerRec)
		{
			this.fieldHeaderRec = headerRec;
		}

		/** {@inheritDoc} */
		public boolean match(MFSComponentRec compRec)
		{
			return 
			(compRec.getIdsp().equals("R") //$NON-NLS-1$
				|| (this.fieldHeaderRec.getNmbr().equals("FKIT") //$NON-NLS-1$
						&& compRec.getIdsp().equals("I") //$NON-NLS-1$
						&& !compRec.getInvs().equals("Z") //$NON-NLS-1$
					)
			)
			&& compRec.isCsniDoNotCollect()
			&& !compRec.getPnri().equals("W"); //$NON-NLS-1$
		}
		
		/** {@inheritDoc} */
		public boolean matchInstruction(MFSInstructionRec instructionRec)
		{
			return true;
		}		
	}
	/**
	 * <code>MFSAutoLogSearchCriteria</code> is an implementation of
	 * <code>MFSComponentSearchCriteria</code> that checks if:
	 * <ul>
	 * <li>the IDSP is A or X</li>
	 * <li>Mlri is not " "</li>
	 * <li>Mlri is not "1"</li>
	 * </ul>
	 * @author The MFS Client Development Team
	 */
	public static class MFSAutoLogSearchCriteria
		implements MFSComponentSearchCriteria
	{
		/** The <CODE>String</CODE> for a Part Number. */
		private String partNumber = null;
		
		/**
		 * Constructs a new <code>MFSAutoLogSearchCriteria</code>.
		 */
		public MFSAutoLogSearchCriteria()
		{
			//nothing
		}
		
		/**
		 * Constructs a new <code>MFSAutoLogSearchCriteria</code>.
		 * @param Part Number the <CODE>String</CODE> or a Part Number.
		 */
		public MFSAutoLogSearchCriteria(String partNumber)
		{
			this.partNumber = partNumber;
		}

		/** {@inheritDoc} */
		public boolean match(MFSComponentRec compRec)
		{
			return 
			((compRec.getIdsp().equals("X") //$NON-NLS-1$
			 || (compRec.getIdsp().equals("A") //$NON-NLS-1$
				&& !compRec.getMlri().equals(" ") //$NON-NLS-1$
				&& !compRec.getMlri().equals("0")))) //$NON-NLS-1$
			 && (this.partNumber == null 
				|| compRec.getItem().equals(this.partNumber));
		}
		
		/** {@inheritDoc} */
		public boolean matchInstruction(MFSInstructionRec instructionRec)
		{
			return instructionRec.getCompletionStatus().equals("C"); //$NON-NLS-1$;
		}	
	}	
}
