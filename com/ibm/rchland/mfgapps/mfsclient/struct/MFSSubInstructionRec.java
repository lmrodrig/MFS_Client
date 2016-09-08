/* © Copyright IBM Corporation 2004, 2013. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR 	Name             Details
 * ---------- ---- -------- 	---------------- ----------------------------------
 * 2007-01-15  ~1  34242JR  	R Prechel        -Java 5 version
 *                                           	 -Changed modifyInstr
 * 2013-01-28  ~2  RCQ00228377 	Andy Williams	 -Changed modifyInstr 
 * 2013-12-06  ~3  RCQ00267733	Andrea Cabrera	 -Migration to Java 1.5 and adjustment of blink 
 * 2016-03-03  ~4  Story1471226 Miguel Rivas     -Support TLS1.2
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.struct;

import java.io.File;
import java.io.IOException; //~4
import java.net.MalformedURLException;

import com.ibm.rchland.mfgapps.client.utils.messagebox.IGSMessageBox; //~4
import com.ibm.rchland.mfgapps.mfsclient.media.MFSMediaHandler;
import com.ibm.rchland.mfgapps.mfscommon.MFSConfig;

/**
 * <code>MFSSubInstructionRec</code> is a data structure for sub-instruction
 * information.
 * @author The MFS Client Development Team
 */
public class MFSSubInstructionRec
{
	/** The path for the drawing. */
	private String fieldDrawing = ""; //$NON-NLS-1$

	/** The path for media 1. */
	private String fieldMedia1 = ""; //$NON-NLS-1$

	/** The path for media 2. */
	private String fieldMedia2 = ""; //$NON-NLS-1$

	/** The path for media 3. */
	private String fieldMedia3 = ""; //$NON-NLS-1$

	/** The sheet information for a PDF file. */
	private String fieldSheet = ""; //$NON-NLS-1$

	/** The zone information for a PDF file. */
	private String fieldZone = ""; //$NON-NLS-1$

	/** The instruction text. */
	private String fieldInstrText = ""; //$NON-NLS-1$

	/** The explode property for the drawing. */
	private String fieldExplodeDrawing = ""; //$NON-NLS-1$

	/** The explode property for media 1. */
	private String fieldExplodeMedia1 = ""; //$NON-NLS-1$

	/** The explode property for media 2. */
	private String fieldExplodeMedia2 = ""; //$NON-NLS-1$

	/** The explode property for media 3. */
	private String fieldExplodeMedia3 = ""; //$NON-NLS-1$

	/** The percentage to scale the drawing if exploded. */
	private String fieldSizeDrawing = ""; //$NON-NLS-1$

	/** The percentage to scale media 1 if exploded. */
	private String fieldSizeMedia1 = ""; //$NON-NLS-1$

	/** The percentage to scale media 2 if exploded. */
	private String fieldSizeMedia2 = ""; //$NON-NLS-1$

	/** The percentage to scale media 3 if exploded. */
	private String fieldSizeMedia3 = ""; //$NON-NLS-1$

	/** Constructs a new <code>MFSSubInstructionRec</code>. */
	public MFSSubInstructionRec()
	{
		super();
	}

	/**
	 * Modifies the instruction text to support embedded images and hot links.
	 * Downloads files that need to be downloaded. (If a hot link URL uses the
	 * https protocol or has just the file name, this method tries to download
	 * the file. If the URL uses the http or ftp protocol, this method does not
	 * download the file.)
	 * @param path the path to the server
	 */
	public void modifyInstruction(String path) // ~2C
	{
		
		MFSConfig localConfig = MFSConfig.getInstance();	
		String instructionAdder = "";
		String newString = "";
		//String theURL = "";
		String domainString = "";
		
		
		//~1 This method changed to use MFSMediaHandler to handle the
		// downloading of files in a system independent fashion

		// Handle embedded images by downloading the image and changing the
		// src attribute of the img element to point to the downloaded image.
		int index = this.fieldInstrText.toUpperCase().indexOf("IMG SRC"); //$NON-NLS-1$
		while (index != -1)
		{
			int start = this.fieldInstrText.indexOf('"', index) + 1;
			int end = this.fieldInstrText.indexOf('"', start);
			String filename = this.fieldInstrText.substring(start, end);
			File file = null;
			//~4
			try
			{
				file = MFSMediaHandler.cacheRemoteFile(path + filename, "TLS");
			}
			catch (IOException ioe)
			{
				try
				{
					file = MFSMediaHandler.cacheRemoteFile(path + filename, "TLSv1.2");
				}
				catch (IOException ioe1)
				{
					IGSMessageBox.showOkMB(null, null, null, ioe1);
				}
			}

			if (filename.toUpperCase().startsWith("HTTP://") == false) //$NON-NLS-1$
			{
				try
				{
					StringBuffer buffer = new StringBuffer(this.fieldInstrText);
					//~3 Change the deprecated function file.toURL() to file.toURI().toURL() 
					//as documentation suggests
					buffer.replace(index + 9, end, file.toURI().toURL().toString());
					this.fieldInstrText = buffer.toString();
				}
				catch (MalformedURLException mue)
				{
					mue.printStackTrace();
				}
			}  			

			index = this.fieldInstrText.toUpperCase().indexOf("IMG SRC", start); //$NON-NLS-1$
		}

		// If the URL protocol is NOT HTTP or FTP, handle hot links by
		// downloading the file and changing the anchor value to the local file
		index = this.fieldInstrText.toUpperCase().indexOf("A HREF"); //$NON-NLS-1$
		while (index != -1)
		{
			int start = this.fieldInstrText.indexOf('"', index) + 1;
			int end = this.fieldInstrText.indexOf('"', start);
			String url = this.fieldInstrText.substring(start, end);
			String ucUrl = url.toUpperCase();
			
			if (ucUrl.startsWith("HTTP://") == false //$NON-NLS-1$
					&& ucUrl.startsWith("FTP://") == false) //$NON-NLS-1$
			{
				//Determine the filename
				String filename = null;
				if (ucUrl.startsWith("HTTPS://")) //$NON-NLS-1$
				{
					
					filename = url.substring(url.lastIndexOf('/') + 1);
					
					/* If filename doesn't contain a . then the URL isn't
					 * for media.  Make filename null so we don't try to 
					 * download it.
					 */
					if(!filename.contains(".")) //$NON-NLS-1$
					{
						filename = null;
					}
				}
				else
				{
					int lastSlashIndex = url.lastIndexOf('/');
					if (lastSlashIndex == -1)
					{
						filename = url;
					}
					else
					{
						filename = url.substring(lastSlashIndex + 1);
					}
				}

				if(filename != null)
				{
					//Download the file and update the anchor value
					File file = null;
					//~4
					try
					{
						file = MFSMediaHandler.cacheRemoteFile(path + filename, "TLS");
					}
					catch (IOException ioe)
					{
						try
						{
							file = MFSMediaHandler.cacheRemoteFile(path + filename, "TLSv1.2");
						}
						catch (IOException ioe1)
						{
							IGSMessageBox.showOkMB(null, null, null, ioe1);
						}
					}
					try
					{
						StringBuffer buffer = new StringBuffer(this.fieldInstrText);
						//~3 Change the deprecated function file.toURL() to file.toURI().toURL() 
						//as documentation suggests
						buffer.replace(start, end, file.toURI().toURL().toString());
						this.fieldInstrText = buffer.toString();
					}
					catch (MalformedURLException mue)
					{
						mue.printStackTrace();
					}
				}
			}

			index = this.fieldInstrText.toUpperCase().indexOf("A HREF", start); //$NON-NLS-1$
		}
		
		newString = fieldInstrText.replace("HTTP://","HTTPS://");
		//theURL = "";
		domainString = "";
		
		newString = newString.replace("http://","HTTPS://");
		newString = newString.replace("https://","HTTPS://");
		
		if(newString.indexOf("HTTPS://")>0)
		{
			if(newString.indexOf("/", newString.indexOf("HTTPS://")+8) > 0)
			{
				domainString = newString.substring(newString.indexOf("HTTPS://")+8,newString.indexOf("/", newString.indexOf("HTTPS://")+8));
			}
		
		
			if(localConfig.containsConfigEntry("INSTRUCTIONURLADDER*"+domainString))
			{
				instructionAdder = MFSConfig.getInstance().getConfigValue("INSTRUCTIONURLADDER*"+domainString);
			}
			
			if(!instructionAdder.equals("") && !instructionAdder.trim().endsWith("/"))
			{
				instructionAdder = instructionAdder.trim()+"/";
			}	
			
			newString = newString.replace("HTTPS://"+domainString+"/","HTTPS://"+instructionAdder);
			this.setInstrText(newString);
			System.out.println(newString);
		}
	}

	/**
	 * Returns the value of the drawing property.
	 * @return the value of the drawing property
	 */
	public String getDrawing()
	{
		return this.fieldDrawing;
	}

	/**
	 * Sets the value of the drawing property.
	 * @param drawing the new value of the drawing property
	 */
	public void setDrawing(String drawing)
	{
		this.fieldDrawing = drawing;
	}

	/**
	 * Returns the value of the explodeDrawing property.
	 * @return the value of the explodeDrawing property
	 */
	public String getExplodeDrawing()
	{
		return this.fieldExplodeDrawing;
	}

	/**
	 * Sets the value of the explodeDrawing property.
	 * @param explodeDrawing the new value of the explodeDrawing property
	 */
	public void setExplodeDrawing(String explodeDrawing)
	{
		this.fieldExplodeDrawing = explodeDrawing;
	}

	/**
	 * Returns the value of the explodeMedia1 property.
	 * @return the value of the explodeMedia1 property
	 */
	public String getExplodeMedia1()
	{
		return this.fieldExplodeMedia1;
	}

	/**
	 * Sets the value of the explodeMedia1 property.
	 * @param explodeMedia1 the new value of the explodeMedia1 property
	 */
	public void setExplodeMedia1(String explodeMedia1)
	{
		this.fieldExplodeMedia1 = explodeMedia1;
	}

	/**
	 * Returns the value of the explodeMedia2 property.
	 * @return the value of the explodeMedia2 property
	 */
	public String getExplodeMedia2()
	{
		return this.fieldExplodeMedia2;
	}

	/**
	 * Sets the value of the explodeMedia2 property.
	 * @param explodeMedia2 the new value of the explodeMedia2 property
	 */
	public void setExplodeMedia2(String explodeMedia2)
	{
		this.fieldExplodeMedia2 = explodeMedia2;
	}

	/**
	 * Returns the value of the explodeMedia3 property.
	 * @return the value of the explodeMedia3 property
	 */
	public String getExplodeMedia3()
	{
		return this.fieldExplodeMedia3;
	}

	/**
	 * Sets the value of the explodeMedia3 property.
	 * @param explodeMedia3 the new value of the explodeMedia3 property
	 */
	public void setExplodeMedia3(String explodeMedia3)
	{
		this.fieldExplodeMedia3 = explodeMedia3;
	}

	/**
	 * Returns the value of the instrText property.
	 * @return the value of the instrText property
	 */
	public String getInstrText()
	{
		return this.fieldInstrText;
	}

	/**
	 * Sets the value of the instrText property.
	 * @param instrText the new value of the instrText property
	 */
	public void setInstrText(String instrText)
	{
		this.fieldInstrText = instrText;
	}

	/**
	 * Returns the value of the media1 property.
	 * @return the value of the media1 property
	 */
	public String getMedia1()
	{
		return this.fieldMedia1;
	}

	/**
	 * Sets the value of the media1 property.
	 * @param media1 the new value of the media1 property
	 */
	public void setMedia1(String media1)
	{
		this.fieldMedia1 = media1;
	}

	/**
	 * Returns the value of the media2 property.
	 * @return the value of the media2 property
	 */
	public String getMedia2()
	{
		return this.fieldMedia2;
	}

	/**
	 * Sets the value of the media2 property.
	 * @param media2 the new value of the media2 property
	 */
	public void setMedia2(String media2)
	{
		this.fieldMedia2 = media2;
	}

	/**
	 * Returns the value of the media3 property.
	 * @return the value of the media3 property
	 */
	public String getMedia3()
	{
		return this.fieldMedia3;
	}

	/**
	 * Sets the value of the media3 property.
	 * @param media3 the new value of the media3 property
	 */
	public void setMedia3(String media3)
	{
		this.fieldMedia3 = media3;
	}

	/**
	 * Returns the value of the sheet property.
	 * @return the value of the sheet property
	 */
	public String getSheet()
	{
		return this.fieldSheet;
	}

	/**
	 * Sets the value of the sheet property.
	 * @param sheet the new value of the sheet property
	 */
	public void setSheet(String sheet)
	{
		this.fieldSheet = sheet;
	}

	/**
	 * Returns the value of the sizeDrawing property.
	 * @return the value of the sizeDrawing property
	 */
	public String getSizeDrawing()
	{
		return this.fieldSizeDrawing;
	}

	/**
	 * Sets the value of the sizeDrawing property.
	 * @param sizeDrawing the new value of the sizeDrawing property
	 */
	public void setSizeDrawing(String sizeDrawing)
	{
		this.fieldSizeDrawing = sizeDrawing;
	}

	/**
	 * Returns the value of the sizeMedia1 property.
	 * @return the value of the sizeMedia1 property
	 */
	public String getSizeMedia1()
	{
		return this.fieldSizeMedia1;
	}

	/**
	 * Sets the value of the sizeMedia1 property.
	 * @param sizeMedia1 the new value of the sizeMedia1 property
	 */
	public void setSizeMedia1(String sizeMedia1)
	{
		this.fieldSizeMedia1 = sizeMedia1;
	}

	/**
	 * Returns the value of the sizeMedia2 property.
	 * @return the value of the sizeMedia2 property
	 */
	public String getSizeMedia2()
	{
		return this.fieldSizeMedia2;
	}

	/**
	 * Sets the value of the sizeMedia2 property.
	 * @param sizeMedia2 the new value of the sizeMedia2 property
	 */
	public void setSizeMedia2(String sizeMedia2)
	{
		this.fieldSizeMedia2 = sizeMedia2;
	}

	/**
	 * Returns the value of the sizeMedia3 property.
	 * @return the value of the sizeMedia3 property
	 */
	public String getSizeMedia3()
	{
		return this.fieldSizeMedia3;
	}

	/**
	 * Sets the value of the sizeMedia3 property.
	 * @param sizeMedia3 the new value of the sizeMedia3 property
	 */
	public void setSizeMedia3(String sizeMedia3)
	{
		this.fieldSizeMedia3 = sizeMedia3;
	}

	/**
	 * Returns the value of the zone property.
	 * @return the value of the zone property
	 */
	public String getZone()
	{
		return this.fieldZone;
	}

	/**
	 * Sets the value of the zone property.
	 * @param zone the new value of the zone property
	 */
	public void setZone(String zone)
	{
		this.fieldZone = zone;
	}
}
