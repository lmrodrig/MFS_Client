/* © Copyright IBM Corporation 2005, 2010. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2007-02-19      34242JR  R Prechel        -Java 5 version
 * 2007-03-08   ~1 35208JM  D Pietrasik      -Add barcode equivalent for function key
 * 2007-03-14   ~2 35208JM  R Prechel        -MFSBCtoPF.loadRuleConstants() static
 * 2007-03-28   ~3 38130JM  R Prechel        -Remove configureIRInfo method
 * 2007-03-30   ~4 38228JM  R Prechel        -Prevent user from starting multiple clients
 * 2007-06-18   ~5 37556CD  T He			 -Added plant information to status bar
 * 2007-08-10   ~6 39464JM  R Prechel        -Use ResourceBundle to obtain version
 * 2007-08-21   ~7 38768JL  R Prechel        -Use IGSDownloader
 * 2007-11-06   ~8 40104PB  R Prechel        -Environment switch changes
 *                                           -Change downloadHelpUrls for IPSR33401JM
 * 2008-07-29   ~9 38990JL  Santiago D	     -Add loadPlomConfiguration call
 * 2010-01-07  ~10 42558JL  D Mathre     	 -Add check for DOWNLOADLABELFILES in configuration,
 * 											  if exists call new class to load OnDemand labels
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient;

import java.awt.EventQueue;
import java.awt.KeyboardFocusManager;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.FileLock;
import java.util.Date;
import java.util.ResourceBundle;

import mfsxml.MISSING_XML_TAG_EXCEPTION;
import mfsxml.MfsXMLDocument;
import mfsxml.MfsXMLParser;

import com.ibm.rchland.mfgapps.client.utils.io.IGSDownloader;
import com.ibm.rchland.mfgapps.client.utils.io.IGSFileUtils;
import com.ibm.rchland.mfgapps.client.utils.messagebox.IGSMessageBox;
import com.ibm.rchland.mfgapps.mfsclient.barcode.MFSBCBarcodeDecoder;
import com.ibm.rchland.mfgapps.mfsclient.barcode.MFSBCtoPF;
import com.ibm.rchland.mfgapps.mfsclient.panel.MFSLogOnPanel;
import com.ibm.rchland.mfgapps.mfsclient.panel.MFSPartFunctionDisplayPanel;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSTriggerData;
import com.ibm.rchland.mfgapps.mfscommon.MFSConfig;
import com.ibm.rchland.mfgapps.mfscommon.MFSStartup;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSComm;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSTransaction;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSXmlTransaction;
import com.ibm.rchland.mfgapps.mfscommon.exception.MFSException;

/**
 * <code>MFSMain</code> starts the MFS Client.
 * @author The MFS Client Development Team
 */
public class MFSMain
	implements Runnable
{
	//~6C Use ResourceBundle to obtain version
	/** The version level of the <code>MFS Client</code>. */
	public static final String VERSION =
	//To update the version number displayed by the MFS Client,
	//update the VERSION property in mfsClientVersion.properties
	ResourceBundle.getBundle("mfsClientVersion").getString("VERSION"); //$NON-NLS-1$ //$NON-NLS-2$

	/** The file name for the MFS Client properties file. */
	private static final String PROPERTIES_FILE = "mfsClient.properties"; //$NON-NLS-1$

	/**
	 * The exclusive <code>FileLock</code> on a file named lock used to
	 * prevent multiple instances of the MFS Client from being started.
	 */
	private static FileLock lock = null; //~4A

	/** The instance of <code>MFSBCtoPF</code>. */
	private static MFSBCtoPF bcKeysDispatcher = null; //~5A

	/**
	 * Starts the application. To ensure thread safety, the
	 * {@link EventQueue#invokeLater(java.lang.Runnable)} method is used to
	 * create the GUI on the Event-Dispatching thread.
	 * @param args the command-line arguments for the application
	 */
	public static void main(String[] args)
	{
		try
		{
			//~8A Check if args is null
			if (args != null)
			{
				//Initialize the IGSFileUtils and create the temp directory
				IGSFileUtils.setLocalDirectory(MFSMain.class);
				createFileLock(); //~4A
				IGSFileUtils.setupTempDirectory();
				System.out.println("MFS Client started " + new Date());
			}

			//~8C Call MFSStartup.configure to process the args and properties
			// file to configure the client, create the log files, and download
			// the configuration
			MFSStartup.configure(args, PROPERTIES_FILE, true);
			MFSConfig.getInstance().setConfigValue(MFSConfig.VERSION, VERSION); //~8C

			//~9A load Plom configurations
			try
			{
				MFSConfig.getInstance().loadPlomConfiguration();				
			}
			catch (MFSException e)
			{
				String title = "Error Retrieving Plom configurations";
				IGSMessageBox.showOkMB(null, title, null, e);
			}			
			
			//~8C Configure the MFSBCBarcodeDecoder
			MFSBCBarcodeDecoder.getInstance().config();
			
			//~10A Check configuration if exists call MFSTriggerData to retrieve OnDemand label info
			if (MFSConfig.getInstance().containsConfigEntry("DOWNLOADLABELFILES")) 
			{
				String compName = MFSConfig.getInstance().getConfigValue(MFSConfig.COMPUTER_NAME);
				MFSTriggerData.getInstance().rtvTrigData(compName);
			}

			downloadHelpUrls();
			downloadNewest();

			//setup barcode key event
			setupBCtoPF();

			//Create the GUI on the Event-Dispatching thread
			//This extra step of indirection is done to ensure thread safety
			//See the Concurrency in Swing tutorial for more details
			// (as of 2007-03-14, this is located online at
			// http://java.sun.com/docs/books/tutorial/uiswing/concurrency/index.html)
			EventQueue.invokeLater(new MFSMain());
		}
		catch (MFSException e)
		{
			String title = "Error Starting Client";
			IGSMessageBox.showOkMB(null, title, null, e);
			System.exit(1);
		}
		catch (IOException ioe)
		{
			String title = "Error Starting Client";
			String erms = "Could not create IR image directory.";
			IGSMessageBox.showOkMB(null, title, erms, ioe);
			System.exit(2);
		}
	}

	//~5A New method
	/** Sets up the {@link MFSBCtoPF}<code>KeyEventDispatcher</code>. */
	public static void setupBCtoPF()
	{
		//remove key event dispatcher if it is not null ~5A
		if (MFSMain.bcKeysDispatcher != null)
		{
			KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(MFSMain.bcKeysDispatcher);
			MFSMain.bcKeysDispatcher = null;
		}

		//If we want to use the keyboard equivalent barcodes, load things ~1A
		if (MFSConfig.getInstance().containsConfigEntry("BARCODEFKEY")) //$NON-NLS-1$
		{
			try
			{
				//add new key event dispatcher
				MFSMain.bcKeysDispatcher = new MFSBCtoPF(); //~5A
				MFSMain.bcKeysDispatcher.loadRuleConstants(); //~5C
				KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(MFSMain.bcKeysDispatcher); //~5C
				System.out.println("Barcode alternates to key presses activated.");
			}
			catch (MFSException me)
			{
				String title = "Error loading barcode alternates";
				String erms = "Barcode alternates to key presses errored on setup.";
				IGSMessageBox.showOkMB(null, title, erms, me);
				MFSMain.bcKeysDispatcher = null;
			}
		}
		else
		{
			System.out.println("Barcode alternates to key presses not selected by config.");
		}
		// end of ~1A
	}

	//~4A New Method
	/**
	 * Tries to obtain an exclusive file lock on a file named lock to prevent
	 * multiple instances of the MFS Client from being started.
	 * @throws MFSException if a file lock could not be obtained
	 */
	private static void createFileLock()
		throws MFSException
	{
		try
		{
			File file = IGSFileUtils.getFile("lock"); //$NON-NLS-1$
			//~8 Set append to true so file isn't deleted.
			lock = new FileOutputStream(file, true).getChannel().tryLock();
		}
		catch (Exception e)
		{
			lock = null;
		}

		if (lock == null)
		{
			throw new MFSException("Only one instance of the MFS Client is allowed.");
		}
	}

	//~8A Rewrote for changes to RTV_URLS to support more than 1 screen
	/** Downloads the help URLs. */
	public static void downloadHelpUrls()
	{
		MfsXMLDocument xmlDocument = new MfsXMLDocument("RTV_URLS"); //$NON-NLS-1$
		xmlDocument.addOpenTag("DATA"); //$NON-NLS-1$

		//~8A Download AUTH_WEB_APP URL for MFSLogOnPanel
		xmlDocument.addOpenTag("SCREEN"); //$NON-NLS-1$
		xmlDocument.addCompleteField("NAME", MFSLogOnPanel.SCREEN_ID); //$NON-NLS-1$
		xmlDocument.addCompleteField("ID", MFSLogOnPanel.AUTH_TOOL_URL_ID); //$NON-NLS-1$
		xmlDocument.addCloseTag("SCREEN"); //$NON-NLS-1$

		if (MFSConfig.getInstance().containsConfigEntry("BUTTON,SELWORK,PARTFUNCTIONS")) //$NON-NLS-1$
		{
			//If the MFSPartFunctionDisplayPanel is configured,
			//download the URLs used by help feature.
			xmlDocument.addOpenTag("SCREEN"); //$NON-NLS-1$
			xmlDocument.addCompleteField("NAME", MFSPartFunctionDisplayPanel.SCREEN_ID); //$NON-NLS-1$
			xmlDocument.addCompleteField("ID", "PART"); //$NON-NLS-1$ //$NON-NLS-2$
			xmlDocument.addCompleteField("ID", "SNUM"); //$NON-NLS-1$ //$NON-NLS-2$
			xmlDocument.addCompleteField("ID", "CCIN"); //$NON-NLS-1$ //$NON-NLS-2$
			xmlDocument.addCompleteField("ID", "MCTL"); //$NON-NLS-1$ //$NON-NLS-2$
			xmlDocument.addCompleteField("ID", "MCSN"); //$NON-NLS-1$ //$NON-NLS-2$
			xmlDocument.addCompleteField("ID", "PLOM"); //$NON-NLS-1$ //$NON-NLS-2$
			xmlDocument.addCompleteField("ID", "PROD"); //$NON-NLS-1$ //$NON-NLS-2$
			xmlDocument.addCompleteField("ID", "CSDS"); //$NON-NLS-1$ //$NON-NLS-2$
			xmlDocument.addCompleteField("ID", "CRFG"); //$NON-NLS-1$ //$NON-NLS-2$
			xmlDocument.addCompleteField("ID", "PRTD"); //$NON-NLS-1$ //$NON-NLS-2$
			xmlDocument.addCompleteField("ID", "FAMT"); //$NON-NLS-1$ //$NON-NLS-2$
			xmlDocument.addCompleteField("ID", "CTLV"); //$NON-NLS-1$ //$NON-NLS-2$
			xmlDocument.addCompleteField("ID", "MALC"); //$NON-NLS-1$ //$NON-NLS-2$
			xmlDocument.addCompleteField("ID", "MILC"); //$NON-NLS-1$ //$NON-NLS-2$
			xmlDocument.addCompleteField("ID", "PTFG"); //$NON-NLS-1$ //$NON-NLS-2$
			xmlDocument.addCompleteField("ID", "CPCI"); //$NON-NLS-1$ //$NON-NLS-2$
			xmlDocument.addCompleteField("ID", "CICT"); //$NON-NLS-1$ //$NON-NLS-2$
			xmlDocument.addCompleteField("ID", "PRDC"); //$NON-NLS-1$ //$NON-NLS-2$
			xmlDocument.addCompleteField("ID", "PSYS"); //$NON-NLS-1$ //$NON-NLS-2$
			xmlDocument.addCompleteField("ID", "PSTP"); //$NON-NLS-1$ //$NON-NLS-2$
			xmlDocument.addCompleteField("ID", "PARI"); //$NON-NLS-1$ //$NON-NLS-2$
			xmlDocument.addCloseTag("SCREEN"); //$NON-NLS-1$
		}

		xmlDocument.addCloseTag("DATA"); //$NON-NLS-1$
		xmlDocument.finalizeXML();

		MFSTransaction rtv_urls = new MFSXmlTransaction(xmlDocument.toString());
		MFSComm.getInstance();
		MFSComm.execute(rtv_urls);

		if (rtv_urls.getReturnCode() == 0)
		{
			final MFSConfig config = MFSConfig.getInstance();
			MfsXMLParser parser = new MfsXMLParser(rtv_urls.getOutput());
			try
			{
				String screenData = parser.getField("SCREEN"); //$NON-NLS-1$
				while (screenData.length() != 0)
				{
					MfsXMLParser screenParser = new MfsXMLParser(screenData);

					String screenName = screenParser.getField("NAME"); //$NON-NLS-1$
					String urlID = screenParser.getNextField("ID"); //$NON-NLS-1$
					String urlValue = screenParser.getNextField("VALUE"); //$NON-NLS-1$  
					while (urlID.length() != 0 && urlValue.length() != 0)
					{
						StringBuffer configLabel = new StringBuffer();
						configLabel.append("URL*"); //$NON-NLS-1$
						configLabel.append(urlID);
						configLabel.append("*"); //$NON-NLS-1$
						configLabel.append(screenName);
						config.setConfigValue(configLabel.toString(), urlValue); //$NON-NLS-1$

						urlID = screenParser.getNextField("ID"); //$NON-NLS-1$
						urlValue = screenParser.getNextField("VALUE"); //$NON-NLS-1$
					}
					screenData = parser.getNextField("SCREEN"); //$NON-NLS-1$
				}
			}
			catch (MISSING_XML_TAG_EXCEPTION mxte)
			{
				mxte.printStackTrace();
			}
		}
	}

	/**
	 * If the configuration indicates a need for the VPD and/or DASD DLLs,
	 * downloads the newest versions of the appropriate DLLs if the DLLS do not
	 * exist on the local system or the DDLS are out of date.
	 * @throws MFSException if a fatal error occurs
	 */
	public static void downloadNewest()
		throws MFSException
	{
		final MFSConfig config = MFSConfig.getInstance();
		String ctyp = config.getConfigValue("CELLTYPE"); //$NON-NLS-1$
		if (ctyp.equals(MFSConfig.NOT_FOUND))
		{
			throw new MFSException("No CELLTYPE config entry found. Contact support.");
		}

		//Do not download the DLLs if Linux,
		//but still check for a celltype.
		final String osName = System.getProperty("os.name"); //$NON-NLS-1$
		if ("Linux".equalsIgnoreCase(osName)) //$NON-NLS-1$
		{
			return;
		}

		final String remoteDirectory = "HTTP://C01PROD.RCHLAND.IBM.COM/APPS/ASRS/MFS/DLLS/"; //$NON-NLS-1$
		if (ctyp.length() > 2 && ctyp.substring(0, 3).toUpperCase().equals("VPD")) //$NON-NLS-1$
		{
			String[] vpdDLLs = {
					"CommonSmartChip.dll", //$NON-NLS-1$
					"commonvpd.dll", //$NON-NLS-1$
					"com32ccs.dll", //$NON-NLS-1$
					"cpcidll.dll", //$NON-NLS-1$
					"cpuctlsServiceInterface.dll", //$NON-NLS-1$
					"csftgtp.dll", //$NON-NLS-1$
					"fusewrap.dll", //$NON-NLS-1$
					"mfs32com.dll", //$NON-NLS-1$
					"NetcDLL.dll", //$NON-NLS-1$
					"sfftUtil.dll", //$NON-NLS-1$
					"smartchip_fuse.dll", //$NON-NLS-1$
					"SmartChip_FuseThermal.dll", //$NON-NLS-1$
					"SmartChip_Single.dll", //$NON-NLS-1$
					"smartwrap.dll", //$NON-NLS-1$
					"socket.dll", //$NON-NLS-1$
					"spcn3vpd.dll", //$NON-NLS-1$
					"squadwrap.dll", //$NON-NLS-1$
					"VBoxDLL.dll", //$NON-NLS-1$
					"vpdecc.dll" //$NON-NLS-1$
			};

			for (int i = 0; i < vpdDLLs.length; i++)
			{
				updateDLL(remoteDirectory + vpdDLLs[i], IGSFileUtils.getFile(vpdDLLs[i]));
			}
		}

		if (config.containsConfigEntry("BUTTON,DIRWORK,PERSONALIZE") //$NON-NLS-1$
				|| config.containsConfigEntry("BUTTON,SELWORK,UNPERSONALIZE")) //$NON-NLS-1$
		{
			String[] dasdDLLs = {
					"dasdwrap.dll", //$NON-NLS-1$
					"DasdPDll.dll" //$NON-NLS-1$
			};
			for (int i = 0; i < dasdDLLs.length; i++)
			{
				updateDLL(remoteDirectory + dasdDLLs[i], IGSFileUtils.getFile(dasdDLLs[i]));
			}
		}
	}

	/**
	 * Downloads the remote DLL represented by <code>url</code> if it is
	 * newer than the local DLL represented by <code>file</code> or if
	 * <code>file</code> represents a nonexistent local DLL.
	 * @param url the <code>URL</code> representing the remote DLL
	 * @param file the <code>File</code> representing the local DLL
	 * @throws MFSException
	 */
	private static void updateDLL(String url, File file)
		throws MFSException
	{
		try
		{
			//~7C Use IGSDownloader
			IGSDownloader.updateFile(new URL(url), file);
		}
		catch (IOException ioe)
		{
			if (file.exists())
			{
				System.out.println("FYI - File download failed for " + url);
			}
			else
			{
				StringBuffer erms = new StringBuffer();
				erms.append("File download failed for ");
				erms.append(url);
				erms.append(". Contact support.");
				throw new MFSException(erms.toString(), ioe);
			}
		}
	}

	/**
	 * Creates and shows the GUI. This method should be called from the
	 * Event-Dispatching thread to ensure thread safety when creating the GUI.
	 */
	public void run()
	{
		MFSFrame frame = new MFSFrame();
		MFSLogOnPanel logOn = new MFSLogOnPanel(frame);
		//~8A Display environment information is switching is enabled
		MFSConfig config = MFSConfig.getInstance();
		if (config.isEnvironmentSwitchEnabled())
		{
			String plant = config.getConfigValue(MFSConfig.PLANT + 0);
			frame.setPlantName(plant);
			logOn.setEnvironment(plant);
		}
		frame.displayMFSPanel(logOn);
		frame.setVisible(true);
		logOn.assignFocus();
	}
}
