/* @ Copyright IBM Corporation 2007. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 * 
 * Date       Flag IPSR/PTR Name             Details 
 * ---------- ---- -------- ---------------- ---------------------------------- 
 * 2007-03-08       35208JM D Pietrasik      -Created to add barcode equivalent 
 *                                            for function key
 * 2007-03-14       35208JM R Prechel        -Handle spaces and enter in key barcode
 *                                           -Discard key barcode KeyEvents
 * 2007-06-18       37556CD T He             -Change static methods and variables to 
 * 											  instance methods and variables 
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.barcode;

import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.HashMap;

import mfsxml.MfsXMLDocument;

import com.ibm.rchland.mfgapps.mfscommon.comm.MFSComm;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSTransaction;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSXmlTransaction;
import com.ibm.rchland.mfgapps.mfscommon.exception.MFSException;

/**
 * <code>MFSBCtoPF</code> implements <code>KeyEventDispatcher</code> and
 * watches <code>KeyEvent</code>s for scanned in key barcodes. If a sequence
 * of <code>KeyEvents</code>s look like a key barcode, the scanned in text is
 * converted into a single <code>KeyEvent</code> which is dispatched using
 * {@link KeyboardFocusManager#redispatchEvent}.
 * @author The MFS Client Development Team
 */
public class MFSBCtoPF
	implements KeyEventDispatcher
{
	/** The maximum length of a key barcode. */
	private static final int MAX_BC_LENGTH = 8;

	/** State constant - start checking for delimiters */
	private static final int START = 0;

	/** State constant - got the first start delimiter */
	private static final int COULDBE = 1;

	/** State constant - got the second start delimiter */
	private static final int ISCODE = 2;

	/**
	 * State constant - got end delimiter, ignore <code>KeyEvent</code>s
	 * until VK_ENTER KEY_RELEASED at end of scan
	 */
	private static final int CHECKENTER = 3;

	/** The delimiters. First 2 are start delimiters, last 1 is end delimiter. */
	private final char delim[] = new char[3];

	/** <code>KeyEvent</code> modifier values. */
	private final int modValues[] = {0, 0, 0, 0, 0, 0, 0, 0, 0};

	/** The <code>HashMap</code> for conversion rules. */
	@SuppressWarnings("rawtypes")
	private final HashMap convCode = new HashMap(12);

	/**
	 * Stores the current state; one of:
	 * <ul>
	 * <li>{@link #START}</li>
	 * <li>{@link #COULDBE}</li>
	 * <li>{@link #ISCODE}</li>
	 * <li>{@link #CHECKENTER}</li>
	 * </ul>
	 */
	private int state = START;

	/** The first <code>KEY_TYPED KeyEvent</code> of a key barcode. */
	private KeyEvent firstKE = null;

	/** Stores the key barcode text. */
	private StringBuffer code = null;

	/** Stores <code>KeyEvent</code>s that might belong to a key barcode. */
	private KeyEvent events[] = new KeyEvent[10];
	
	/** Set <code>true</code> when <code>KeyEvent</code>s should be stored. */
	private boolean storing = false;

	/** The index to store the next <code>KeyEvent</code> at in {@link #events}. */
	private int keStoreIndex = 0;

	/**
	 * The index in {@link #events} for the next <code>KeyEvent</code> to
	 * dispatch. Used to make {@link #dispatchStoredKeyEvents()} reentrant.
	 */
	private int keDispatchIndex = 0;

	/** Constructs a new <code>MFSBCtoPF</code>. */
	public MFSBCtoPF()
	{
		super();
	}

	/**
	 * Loads the delimter, modifier, and conversion values from the rules on the server.
	 * @throws MFSException if a rule is missing or contains invalid information
	 */
	@SuppressWarnings("unchecked")
	public void loadRuleConstants()
		throws MFSException
	{
		MfsXMLDocument xml_data1; /* used for transaction */
		MFSTransaction rtv_rulex; /* transaction to server */
		String data; /* to parse the response */
		String startDelim = null; /* make sure we get start delimiter */
		String endDelim = null; /* make sure we get end delimiter */

		// Get all the function key equivalent rules
		xml_data1 = new MfsXMLDocument("RTV_RULEX"); //$NON-NLS-1$
		xml_data1.addOpenTag("DATA"); //$NON-NLS-1$
		xml_data1.addCompleteField("KEY", "FKE"); //$NON-NLS-1$ //$NON-NLS-2$
		xml_data1.addCloseTag("DATA"); //$NON-NLS-1$
		xml_data1.finalizeXML();

		rtv_rulex = new MFSXmlTransaction(xml_data1.toString());
		MFSComm.getInstance();
		MFSComm.execute(rtv_rulex);
		data = rtv_rulex.getOutput();

		if (rtv_rulex.getReturnCode() != 0)
		{
			throw new MFSException(rtv_rulex.getErms());
		}

		//data should contain a set of FKE rules
		int beginOpenTag = data.indexOf("<FKE"); //$NON-NLS-1$
		while (beginOpenTag != -1)
		{
			try
			{
				//For each FKE rule, find the XML element name and value
				int endOpenTag = data.indexOf(">", beginOpenTag); //$NON-NLS-1$
				String elementName = data.substring(beginOpenTag + 1, endOpenTag);
				int beginCloseTag = data.indexOf("</" + elementName + ">", endOpenTag); //$NON-NLS-1$ //$NON-NLS-2$
				String elementValue = data.substring(endOpenTag + 1, beginCloseTag);

				//Ignore FKEP, FKET, and FKER rules
				//FKEMOD elements are for modifier keys
				if (elementName.startsWith("FKEMOD")) //$NON-NLS-1$
				{
					String modifierName = elementName.substring(6).toUpperCase()+ "_DOWN_MASK"; //$NON-NLS-1$
					try
					{
						this.modValues[Integer.parseInt(elementValue)] = 
							InputEvent.class.getField(modifierName).getInt(null);
					}
					catch (Exception ex)
					{
						throw new MFSException("Error initializing modifier:" + elementName);
					}
				}
				//FKECONV elements are for converting abbreviated code to full code
				else if (elementName.startsWith("FKECONV")) //$NON-NLS-1$
				{
					this.convCode.put(elementName.substring(7), elementValue);
				}
				//FKEDELIMSTART is the start delimiter
				else if (elementName.equals("FKEDELIMSTART")) //$NON-NLS-1$
				{
					startDelim = elementValue;
				}
				//FKEDELIMEND is the end delimiter
				else if (elementName.equals("FKEDELIMEND")) //$NON-NLS-1$
				{
					endDelim = elementValue;
				}
			}
			catch (IndexOutOfBoundsException ioe)
			{
				//Bad FKE rule
				ioe.printStackTrace();
			}

			beginOpenTag = data.indexOf("<FKE", beginOpenTag + 1); //$NON-NLS-1$
		}

		if (startDelim == null)
		{
			throw new MFSException("No start delimiter found in rule data.");
		}
		if (endDelim == null)
		{
			throw new MFSException("No end delimiter found in rule data.");
		}

		try
		{
			this.delim[0] = startDelim.charAt(0);
			this.delim[1] = startDelim.charAt(1);
			this.delim[2] = endDelim.charAt(0);
		}
		catch(StringIndexOutOfBoundsException e)
		{
			throw new MFSException("Bad delimiter in rule data.");
		}
	}

	/**
	 * Returns <code>true</code> iff the <code>KeyEvent</code> represents
	 * the delimiter key at the specified <code>position</code> in the
	 * delimiter array.
	 * @param position delimiter array index to check
	 * @param e the <code>KeyEvent</code> to check
	 * @return <code>true</code> if the <code>KeyEvent</code> and the
	 *         delimiter match
	 */
	private boolean isDelimiter(int position, KeyEvent e)
	{
		if ((position >= 0) && (position < this.delim.length))
		{
			return e.getKeyChar() == this.delim[position];
		}

		return false;
	}

	/**
	 * Converts from our code to the standard event type code.
	 * @param id the character used to encode the event type
	 * @return Java version of event type
	 */
	private int convertID(char id)
	{
		int retval = 0;
		switch (id)
		{
			case 'P':
				retval = KeyEvent.KEY_PRESSED;
				break;
			case 'T':
				retval = KeyEvent.KEY_TYPED;
				break;
			case 'R':
				retval = KeyEvent.KEY_RELEASED;
				break;
		}
		return retval;
	}

	/**
	 * Converts from our single character modifier to Java version.
	 * @param mod the Hex character used to encode the modifiers
	 * @return Java version of modifiers
	 */
	private int convertModifier(char mod)
	{
		// we basically break down our 4 bit number to see which
		// bits are selected and or together the Java values.
		int retval = 0;
		int modInt = Character.digit(mod, 16);
		if (modInt >= 8)
		{
			retval |= this.modValues[8];
			modInt -= 8;
		}
		if (modInt >= 4)
		{
			retval |= this.modValues[4];
			modInt -= 4;
		}
		if (modInt >= 2)
		{
			retval |= this.modValues[2];
			modInt -= 2;
		}
		if (modInt >= 1)
		{
			retval |= this.modValues[1];
			modInt -= 1;
		}
		return retval;
	}

	/**
	 * Convert the text code to a <code>String</code> representation of a
	 * <code>KeyEvent</code> virtual key code. This involves checking the
	 * conversion <code>HashMap</code> for abbreviated codes, and
	 * prepending "VK_" to the value.
	 * @param oldCode the code obtained from barcode
	 * @return <code>KeyEvent</code> virtual key field name
	 */
	private String convertCodeStr(String oldCode)
	{
		String newCode = null;

		/* Check convCode for abbreviations. */
		newCode = (String) this.convCode.get(oldCode);
		if (null == newCode)
		{
			/* If no abbreviation found, use code from barcode. */
			newCode = oldCode;
		}

		/* everything needs the vk_ */
		return "VK_" + newCode; //$NON-NLS-1$
	}

	/**
	 * Given the key barcode for a key event, creates a <code>KeyEvent</code>
	 * and dispatches it using {@link KeyboardFocusManager#redispatchEvent}.
	 * @param barcode the barcode to process (without the delimiters)
	 * @param ke the <code>KeyEvent</code> for the firt
	 *        <code>KEY_TYPED KeyEvent</code> of the barcode
	 */
	private void processCode(String barcode, KeyEvent ke)
	{
		/* Uppercase the code string from the barcode. */
		String codeStr = barcode.toUpperCase();

		/* Determine Java modifiers */
		int keyModifiers = convertModifier(codeStr.charAt(0));

		/* Determine Java event type (Pressed, Typed, Released) */
		int keyID = convertID(codeStr.charAt(1));

		/* Determine Java key code. */
		int keyCode = KeyEvent.VK_UNDEFINED;
		try
		{
			/* Convert the code string to a KeyEvent virtual key code. */
			String convertCodeStr = convertCodeStr(codeStr.substring(2));
			keyCode = KeyEvent.class.getField(convertCodeStr).getInt(null);
		}
		catch (Exception e)
		{
			/* Ignore an Exception and use VK_UNDEFINED */
			e.printStackTrace();
		}

		/* Determine Java key char. */
		char keyChar = KeyEvent.CHAR_UNDEFINED;
		if (keyID == KeyEvent.KEY_TYPED)
		{
			/* KEY_TYPED events need a key char and not a key code. */
			if ((keyCode >= KeyEvent.VK_0 && keyCode <= KeyEvent.VK_9)
					|| (keyCode >= KeyEvent.VK_A && keyCode <= KeyEvent.VK_Z))
			{
				keyChar = (char) keyCode;
				keyCode = KeyEvent.VK_UNDEFINED;
			}
		}

		KeyEvent newEvent = new KeyEvent(ke.getComponent(), keyID, 
				ke.getWhen(), keyModifiers, keyCode, keyChar);

		KeyboardFocusManager.getCurrentKeyboardFocusManager().redispatchEvent(
				newEvent.getComponent(), newEvent);
	}

	/**
	 * Stores a <code>KeyEvent</code> that might belong to a key barcode.
	 * @param e the <code>KeyEvent</code> to store
	 */
	private void storeKeyEvent(KeyEvent e)
	{
		if (this.keStoreIndex < this.events.length)
		{
			this.events[this.keStoreIndex++] = e;
		}
	}

	/** Dispatches stored <code>KeyEvent</code>s. */
	private void dispatchStoredKeyEvents()
	{
		/* STORE is used to stop dispatching events if keStoreIndex changes. */
		final int STORE = this.keStoreIndex;
		KeyboardFocusManager kfm = KeyboardFocusManager.getCurrentKeyboardFocusManager();
		while (this.keDispatchIndex < this.keStoreIndex && STORE == this.keStoreIndex)
		{
			KeyEvent event = this.events[this.keDispatchIndex];
			this.events[this.keDispatchIndex++] = null;
			kfm.redispatchEvent(event.getComponent(), event);
		}

		/* If all of the KeyEvents were dispatched, reset variables. */
		if (STORE == this.keStoreIndex)
		{
			this.firstKE = null;
			this.code = null;
			this.storing = false;
			this.keStoreIndex = 0;
			this.keDispatchIndex = 0;
		}
	}

	/**
	 * Processes the <code>KeyEvent</code>s for key barcodes before anything
	 * else processes them.
	 * @see KeyEventDispatcher#dispatchKeyEvent(KeyEvent)
	 */
	public boolean dispatchKeyEvent(KeyEvent e)
	{
		final int ID = e.getID();

		/* Store a KeyEvent that might be part of a key barcode. */
		if (this.state == START)
		{
			if (ID == KeyEvent.KEY_PRESSED)
			{
				if (e.getKeyCode() == KeyEvent.VK_SHIFT || isDelimiter(0, e))
				{
					/* Could belong to a key barcode, set storing true. */
					this.storing = true;
				}
			}

			if (this.storing)
			{
				storeKeyEvent(e);
			}
			else
			{
				/* If storing is false, KeyEvent is not part of a key barcode. */
				/* Thus, return false and let normal KeyEvent dispatching occur. */
				return false;
			}
		}
		else if (this.state == COULDBE)
		{
			storeKeyEvent(e);
		}

		/* Process KEY_TYPED events for key barcodes. */
		if (ID == KeyEvent.KEY_TYPED)
		{
			switch (this.state)
			{
				case START:
					/* Look for first start delimiter. */
					if (isDelimiter(0, e))
					{
						this.state = COULDBE;
						this.firstKE = e;
					}
					break;

				case COULDBE:
					/* Look for second start delimiter. */
					if (isDelimiter(1, e))
					{
						this.state = ISCODE;
						this.code = new StringBuffer(MAX_BC_LENGTH);
					}
					else
					{
						this.state = START;
						dispatchStoredKeyEvents();
					}
					break;

				/* Look for end delimiter while storing text. */
				case ISCODE:
					if (isDelimiter(2, e))
					{
						/* Got end delimiter, ignore KeyEvents 
						 * until VK_ENTER KEY_RELEASED */
						this.state = CHECKENTER;
					}
					else
					{
						/* Store key char for processing. */
						this.code.append(e.getKeyChar());
					}
					break;
			}
		}
		else if (ID == KeyEvent.KEY_RELEASED)
		{
			/*
			 * A KEY_RELEASED event in START state indicates any stored
			 * KeyEvents do not belong to a key barcode, so dispatch them.
			 */
			if (this.state == START)
			{
				dispatchStoredKeyEvents();
			}
			else if (this.state == CHECKENTER && e.getKeyCode() == KeyEvent.VK_ENTER)
			{
				/* All barcodes end with a VK_ENTER KEY_RELEASED event. */
				/* Done processing key barcode; reset variables and dispatch KeyEvent. */
				String barcode = this.code.toString();
				KeyEvent ke = this.firstKE;
				
				this.firstKE = null;
				this.code = null;
				this.storing = false;
				this.keStoreIndex = 0;
				this.keDispatchIndex = 0;
				this.state = START;
				
				processCode(barcode, ke);
			}
		}

		/*
		 * dispatchKeyEvent returns true if the KeyEvent was processed; false if
		 * the KeyEvent should be passed to the next KeyEventDispatcher. If the
		 * flow of execution got here, the KeyEvent was processed.
		 */
		return true;
	}
}
