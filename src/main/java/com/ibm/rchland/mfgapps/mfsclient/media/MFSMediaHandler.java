/* @ Copyright IBM Corporation 2007, 2008. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2007-01-23      34242JR  R Prechel        -Initial version
 * 2007-03-28   ~1 38130JM  R Prechel        -Call RTV_PASSWD from MyAuthenticator 
 * 2007-11-06   ~2 40104PB  R Prechel        -Use screen ID for URL lookup
 * 2008-02-05   ~3 40845MZ  R Prechel        -Replace MFSImageFrame with IGSImageFrame
 * 2014-04-25	~4 00299615 Jaime Soriano	 - changed the url to use toURI() 
 * 2016-03-03   ~5 1471226  Miguel Rivas     -Support TLS1.2
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.media;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.net.URLConnection;
import java.security.GeneralSecurityException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.imageio.ImageIO;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.ibm.rchland.mfgapps.client.utils.io.IGSDownloader;
import com.ibm.rchland.mfgapps.client.utils.io.IGSFileUtils;
import com.ibm.rchland.mfgapps.client.utils.media.IGSImageFrame;
import com.ibm.rchland.mfgapps.client.utils.messagebox.IGSMessageBox;
import com.ibm.rchland.mfgapps.mfsclient.MFSPanel;
import com.ibm.rchland.mfgapps.mfscommon.MFSConfig;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSComm;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSFixedTransaction;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSTransaction;

/**
 * <code>MFSMediaHandler</code> contains methods to:
 * <ul>
 * <li>handle media and URLs using external programs</li>
 * <li>download media files from a remote server and cache them in a temp
 * directory on the local machine</li>
 * <li>access cached media files</li>
 * </ul>
 * @author The MFS Client Development Team
 */
public class MFSMediaHandler
{
	/**
	 * The prefix of the <code>String</code> parameter for
	 * {@link #displayWebPage(MFSPanel, String)} if the <code>String</code>
	 * specifies the url for the page instead of an ID.
	 */
	public static final String URL_PREFIX = "url="; //$NON-NLS-1$

	/** The default operating system specific command (Windows NT/XP). */
	private static final String DEFAULT_COMMAND = "cmd /c start "; //$NON-NLS-1$

	/** The operating system specific command for Windows 9x. */
	private static final String WINDOWS_9X_COMMAND = "start "; //$NON-NLS-1$

	/** The operating system specific command for Linux. */
	private static final String LINUX_COMMAND = "htmlview "; //$NON-NLS-1$
	
	/**
	 * Constructs a new <code>MFSMediaHandler</code>. This class only has
	 * static variables and static methods and does not have any instance
	 * variables or instance methods. Thus, there is no reason to create an
	 * instance of <code>MFSMediaHandler</code>, so the only constructor is
	 * declared <code>private</code>.
	 */
	private MFSMediaHandler()
	{
		super();
	}

	/**
	 * Handles the resource given by the specified <code>url</code>.
	 * @param url the <code>URL</code> for the resource
	 */
	public static void handleResource(URL url)
	{
		try
		{
			if (url.getProtocol().equalsIgnoreCase("file")) //$NON-NLS-1$
			{
				MFSMediaHandler.handleMedia(new File(url.toURI())); //~4
			}
			else
			{
				String command = getOSSpecificCommand();

				String fileHandler = MFSConfig.getInstance()
						.getConfigValue("FileHandler"); //$NON-NLS-1$
				if (fileHandler != null)
				{
					if (fileHandler.indexOf("URL\"") != -1) //$NON-NLS-1$)
					{
						int startIndex = fileHandler.indexOf("URL\"") + 4; //$NON-NLS-1$
						int endIndex = fileHandler.indexOf("\"", startIndex); //$NON-NLS-1$
						command = fileHandler.substring(startIndex, endIndex);
					}
					else if (fileHandler.indexOf("CMD\"") != -1) //$NON-NLS-1$)
					{
						int startIndex = fileHandler.indexOf("CMD\"") + 4; //$NON-NLS-1$
						int endIndex = fileHandler.indexOf("\"", startIndex); //$NON-NLS-1$
						command = fileHandler.substring(startIndex, endIndex);
					}
				}

				Runtime.getRuntime().exec(command + url.toString());
			}
		}
		catch (Exception e)
		{
			StringBuffer erms = new StringBuffer();
			erms.append("An error occurred while attempting to launch your web browser.\n");
			erms.append("Please start your web browser and go to:\n");
			erms.append(url);
			IGSMessageBox.showOkMB(null, null, erms.toString(), e);
		}
	}

	/**
	 * Handles the media given by the specified <code>file</code>.
	 * @param file the <code>File</code> for the media
	 */
	public static void handleMedia(File file)
	{
		//~3C Redo method to replace MFSImageFrame with IGSImageFrame
		try
		{
			BufferedImage image = null;
			String fileHandler = MFSConfig.getInstance().getConfigValue("FileHandler"); //$NON-NLS-1$
			if (fileHandler.indexOf("ImageViewer") != -1 //$NON-NLS-1$
					&& (image = ImageIO.read(file)) != null)
			{
				String title = "Image Viewer";
				IGSImageFrame frame = IGSImageFrame.create(title, image, 2, false);
				if (frame == null)
				{
					String erms = "The maximum number of image viewers has been reached.  Please close an image viewer and try again.";
					IGSMessageBox.showOkMB(null, title, erms, null);
				}
				else
				{
					frame.autoSizeFrame();
					frame.setVisible(true);
					frame.toFront();
				}
			}
			else
			{
				String command = null;
				if (fileHandler != null && fileHandler.indexOf("CMD\"") != -1) //$NON-NLS-1$
				{
					int startIndex = fileHandler.indexOf("CMD\"") + 4; //$NON-NLS-1$
					int endIndex = fileHandler.indexOf("\"", startIndex); //$NON-NLS-1$
					command = fileHandler.substring(startIndex, endIndex);
				}else{
					command = getOSSpecificCommand();
				}
				
				if (command == DEFAULT_COMMAND || command == WINDOWS_9X_COMMAND)
				{
					final String fileName = file.getPath().toUpperCase();
					if (fileName.indexOf(".GIF") == -1 //$NON-NLS-1$
							&& fileName.indexOf(".JPG") == -1 //$NON-NLS-1$
							&& fileName.indexOf(".JPEG") == -1 //$NON-NLS-1$
							&& fileName.indexOf(".BMP") == -1 //$NON-NLS-1$
							&& fileName.indexOf(".HTM") == -1 //$NON-NLS-1$
							&& fileName.indexOf(".PDF") == -1) //$NON-NLS-1$
					{
						command = command + "wmplayer "; //$NON-NLS-1$
					}
					/*
					 *When openning files on imageviewer
					 *the exec command will figure out the path per the file name.
					 *getParentFile()
					 *
					 * This does not work in the same way when we have a command to 
					 * run internet explorer. internet explorer will need the full
					 * path to the resource in order to open it.
					 */
					
					//this is going to be used when image viewer
					//need to test with file names with spaces.
					command += file.getName();
				}else{
				
					//when a command is specified this one will be used.
					//we are surrounding the file path with "" in case the path
					//has spaces.
					command += " \""+file.getAbsoluteFile()+"\"";
				}
				
				Runtime.getRuntime().exec(command, null, file.getParentFile());
			}
		}
		catch (Exception e)
		{
			String title = "Error Displaying Media";
			String erms = "Could not display media.";
			IGSMessageBox.showOkMB(null, title, erms, e);
		}
	}

	/**
	 * Returns the operating system specific command used to handle media and
	 * URLs (including help URLs).
	 * @return the operating system specific command
	 */
	private static String getOSSpecificCommand()
	{
		String osName = System.getProperty("os.name"); //$NON-NLS-1$
		String command = DEFAULT_COMMAND;
		if ("Windows 95".equalsIgnoreCase(osName) //$NON-NLS-1$
				|| "Windows 98".equalsIgnoreCase(osName)) //$NON-NLS-1$
		{
			command = WINDOWS_9X_COMMAND;
		}
		else if ("Linux".equalsIgnoreCase(osName)) //$NON-NLS-1$
		{
			command = LINUX_COMMAND;
		}
		return command;
	}

	//~2C Renamed method and second parameter
	/**
	 * Displays a web page for the specified <code>panel</code> and <code>id</code>.
	 * @param panel the currently displayed <code>MFSPanel</code>
	 * @param id the ID for the page that should be displayed. If <code>id</code>
	 *        starts with {@link #URL_PREFIX}, the content after the prefix
	 *        will be treated as the url for the page instead of an ID.
	 */
	public static void displayWebPage(MFSPanel panel, String id)
	{
		String url;
		if (id == null)
		{
			url = null;
		}
		else if (id.startsWith(URL_PREFIX))
		{
			int prefixLength = URL_PREFIX.length();
			url = id.substring(prefixLength);
		}
		else
		{
			//~2C Changed the key to be URL*id*panel.getScreenID()
			//and use urlKey to create the key		
			url = MFSConfig.getInstance().getConfigValue(urlKey(panel, id));
		}

		//~2C Updated not found condition
		if (url == null || url.equals(MFSConfig.NOT_FOUND)
				|| (url.indexOf("Record not found") != -1) //$NON-NLS-1$
				|| url.trim().length() == 0)
		{
			StringBuffer erms = new StringBuffer();
			erms.append("No URL was found for ID ");
			erms.append(id);
			erms.append(". Call support.");
			IGSMessageBox.showOkMB(panel, null, erms.toString(), null);
		}
		else
		{
			try
			{
				MFSMediaHandler.handleResource(new URL(url));
			}
			catch (Exception e)
			{
				StringBuffer erms = new StringBuffer();
				erms.append("An error occurred while attempting to launch your web browser.\n");
				erms.append("Please start your web browser and go to:\n");
				erms.append(url);
				IGSMessageBox.showOkMB(panel, null, erms.toString(), null);
			}
		}
	}
	
	//~2A New method
	/**
	 * Generates the URL key for an <code>MFSPanel</code> and ID.
	 * @param panel the <code>MFSPanel</code>
	 * @param id the ID for the URL
	 * @return the URL key
	 */
	public static String urlKey(MFSPanel panel, String id)
	{
		StringBuffer key = new StringBuffer();
		key.append("URL*"); //$NON-NLS-1$
		key.append(id);
		key.append("*"); //$NON-NLS-1$
		key.append(panel.getScreenID());
		return key.toString();
	}

	/**
	 * Constructs a <code>URL</code> from the path <code>String</code>. Any
	 * space characters in the path <code>String</code> will be URL encoded
	 * (converted to %20).
	 * @param path the <code>String</code> to parse as a URL
	 * @return the <code>URL</code>
	 * @throws MalformedURLException as thrown by {@link URL#URL(String)}
	 */
	public static URL remoteURL(String path)
		throws MalformedURLException
	{
		path = path.trim();
		path = path.replaceAll(" ", "%20"); //$NON-NLS-1$ //$NON-NLS-2$
		return new URL(path);
	}

	/**
	 * Returns a <code>File</code> that represents the absolute filename of
	 * the location where the remote file given by the specified
	 * <code>path</code> will be cached locally.
	 * @param path the path to a remote file
	 * @return the local <code>File</code>
	 */
	public static File localFile(String path)
	{
		path.trim();
		int index = path.lastIndexOf('/');
		if (index != -1)
		{
			path = path.substring(index);
		}
		path = path.replaceAll(" ", "_"); //$NON-NLS-1$ //$NON-NLS-2$
		path = path.replaceAll("%20", "_"); //$NON-NLS-1$ //$NON-NLS-2$
		return IGSFileUtils.getTempFile(path);
	}

	/**
	 * Downloads the file located at the specified <code>fileURL</code> if it
	 * is newer than the local copy or if a local copy does not exist.
	 * @param remotePath the remote path for the file
	 * @return a <code>File</code> object for the local copy of the file or
	 *         <code>null</code> if an error occurred
	 * @throws IOException 
	 */
	public static File cacheRemoteFile(String remotePath, String protocol) throws IOException //~5C
	{
		long startTime = System.currentTimeMillis();
		File result = null;
		try
		{
			//~1C Used static instance of MyAuthenticator to maintain the
			//    IR password between calls to cacheRemoteFile
			Authenticator.setDefault(MyAuthenticator.getInstance(remotePath));
			URL url = remoteURL(remotePath);
			result = localFile(remotePath);

			URLConnection connection = url.openConnection();
			if (connection instanceof HttpsURLConnection)
			{
				HttpsURLConnection https = (HttpsURLConnection) connection;
				https.setHostnameVerifier(new MyHostnameVerifier());

				SSLContext context = SSLContext.getInstance(protocol); //$NON-NLS-1$ //~5
				context.init(null, new TrustManager[] {
					new MyX509TrustManager()
				}, null);
				https.setSSLSocketFactory(context.getSocketFactory());
			}

			boolean download = true;
			if (result.exists())
			{
				connection.setIfModifiedSince(result.lastModified());
				if (connection instanceof HttpURLConnection)
				{
					int response = ((HttpURLConnection) connection).getResponseCode();
					if (response == HttpURLConnection.HTTP_NOT_MODIFIED)
					{
						download = false;
					}
				}

				//The HTTP_NOT_MODIFIED check will not work if the server does
				// not support the If-Modified-Since request-header. However,
				// the HTTP_NOT_MODIFIED check is a performance enhancement if
				// the server does support the If-Modified-Since request-header
				// because it decreases transaction overhead if the requested
				// file has not been modified.

				if (download)
				{
					if (result.lastModified() >= connection.getLastModified())
					{
						download = false;
					}
				}
			}
			if (download)
			{
				//~2C Use IGSDownloader instead of local method
				IGSDownloader.downloadFile(connection, result);

				long endTime = System.currentTimeMillis();
				StringBuffer buffer = new StringBuffer();
				buffer.append("File: ");
				buffer.append(url.toString());
				buffer.append("; File Size = ");
				buffer.append(connection.getContentLength());
				buffer.append(" Bytes; Total download time = ");
				buffer.append(endTime - startTime);
				buffer.append(" ms");
				System.out.println(buffer.toString());
			}
		}
		catch (IOException ioe)
		{
			//IGSMessageBox.showOkMB(null, null, null, ioe);
			throw new IOException(ioe); //~5
			//result = null;
		}
		catch (GeneralSecurityException gse)
		{
			String message = "Error configuring SSL connection.";
			IGSMessageBox.showOkMB(null, null, message, gse);
			result = null;
		}
		return result;
	}

	/**
	 * <code>MyHostnameVerifier</code> is a {@link HostnameVerifier} that
	 * always returns true when asked to verify an <code>SSLSession</code>.
	 * @author The MFS Client Development Team
	 */
	private static class MyHostnameVerifier
		implements HostnameVerifier
	{
		/** Constructs a new <code>MyHostnameVerifier</code>. */
		public MyHostnameVerifier()
		{
			super();
		}

		/**
		 * Verifies that the host name is an acceptable match with the server's
		 * authentication scheme.
		 * @param hostname the host name
		 * @param session the <code>SSLSession</code> used on the connection
		 *        to the host
		 * @return <code>true</code> always
		 */
		public boolean verify(String hostname, SSLSession session)
		{
			return true;
		}
	}

	/**
	 * <code>MyAuthenticator</code> is an {@link Authenticator} that returns
	 * the IR username and password.
	 * @author The MFS Client Development Team
	 */
	private static class MyAuthenticator
		extends Authenticator
	{
		//~1 Added INSTANCE, fieldPath, fieldSystem, and fieldPwd variables.
		//   Added getInstance method
		//   Changed getPasswordAuthentication to call RTV_PASSWD
		
		/** The sole instance of <code>MyAuthenticator</code>. */
		private static final MyAuthenticator INSTANCE = new MyAuthenticator();

		/**
		 * The path of the file to download for which
		 * {@link #getPasswordAuthentication} may be called.
		 */
		private String fieldPath = null;

		/** The system for which {@link #fieldPwd} applies. */
		private String fieldSystem = null;

		/** The password for IRUSER on {@link #fieldSystem}. */
		private String fieldPwd = null;

		/**
		 * Returns the sole instance of <code>MyAuthenticator</code>.
		 * @param path the path of the file to download for which
		 *        {@link #getPasswordAuthentication} may be called
		 * @return the sole instance of <code>MyAuthenticator</code>
		 */
		public static MyAuthenticator getInstance(String path)
		{
			INSTANCE.fieldPath = path;
			return INSTANCE;
		}

		/** Constructs a new <code>MyAuthenticator</code>. */
		private MyAuthenticator()
		{
			super();
		}

		/**
		 * Returns a <code>PasswordAuthentication</code> containing the IR
		 * username and password as retrieved by the RTV_PASSWD transaction
		 * or <code>null</code> if RTV_PASSWD returns a nonzero return code.
		 * @return a <code>PasswordAuthentication</code> or <code>null</code>
		 */
		protected PasswordAuthentication getPasswordAuthentication()
		{
			final String USER = "IRUSER"; //$NON-NLS-1$

			// Determine the system from the path
			// Example: The system in the following paths is C01PROD
			//  HTTPS://C01PROD.RCHLAND.IBM.COM:8043/SecureImage/
			//  HTTPS://C01PROD/SecureImage/
			int startIndex = 0;
			if (this.fieldPath.indexOf("://") != -1) //$NON-NLS-1$
			{
				startIndex = this.fieldPath.indexOf("://") + 3; //$NON-NLS-1$
			}
			int indexSlash = this.fieldPath.indexOf("/", startIndex); //$NON-NLS-1$
			int indexPeriod = this.fieldPath.indexOf(".", startIndex); //$NON-NLS-1$
			int endIndex = (indexSlash < indexPeriod ? indexSlash : indexPeriod);
			String system = this.fieldPath.substring(startIndex, endIndex); //$NON-NLS-1$

			if (system.equals(this.fieldSystem) == false)
			{
				this.fieldSystem = system;

				// Execute RTV_PASSWD to obtain the password for IRUSER
				// for the system determined from the path
				String blanks50 = "                                                  "; //$NON-NLS-1$
				StringBuffer input = new StringBuffer(110);
				input.append("RTV_PASSWD"); //$NON-NLS-1$
				input.append((USER + blanks50).substring(0, 50));
				input.append((this.fieldSystem + blanks50).substring(0, 50));

				MFSTransaction rtv_passwd = new MFSFixedTransaction(input.toString());
				MFSComm.getInstance();
				MFSComm.execute(rtv_passwd);

				if (rtv_passwd.getReturnCode() == 0)
				{
					this.fieldPwd = rtv_passwd.getOutput().trim();
				}
				else
				{
					System.out.println(rtv_passwd.getOutput());
					this.fieldPwd = null;
				}
			}

			if (this.fieldPwd != null)
			{
				return new PasswordAuthentication(USER, this.fieldPwd.toCharArray());
			}
			return null;
		}
	}

	/**
	 * <code>MyX509TrustManager</code> is an {@link X509TrustManager} that
	 * trusts all clients and servers.
	 * @author The MFS Client Development Team
	 */
	private static class MyX509TrustManager
		implements X509TrustManager
	{
		/** Constructs a new <code>MyX509TrustManager</code>. */
		public MyX509TrustManager()
		{
			super();
		}

		/**
		 * Does nothing, which implies that all clients are trusted.
		 * @param chain the peer certificate chain
		 * @param authType the authentication type based on the client
		 *        certificate
		 * @throws CertificateException never
		 */
		public void checkClientTrusted(X509Certificate[] chain, String authType)
			throws CertificateException
		{
			//Trust all clients
		}

		/**
		 * Does nothing, which implies that all servers are trusted.
		 * @param chain the peer certificate chain
		 * @param authType the key exchange algorithm used
		 * @throws CertificateException never
		 */
		public void checkServerTrusted(X509Certificate[] chain, String authType)
			throws CertificateException
		{
			//Trust all servers
		}

		/**
		 * Return an array of certificate authority certificates which are
		 * trusted for authenticating peers.
		 * @return an empty array of certificates
		 */
		public X509Certificate[] getAcceptedIssuers()
		{
			return new X509Certificate[0];
		}
	}
}
