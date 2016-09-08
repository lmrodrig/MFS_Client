/* © Copyright IBM Corporation 2007. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2007-02-15      34242JR  R Prechel        -Initial version
 * 2016-03-03  ~1  1471226  Miguel Rivas     -Support TLS1.2
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException; //~1

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingConstants;

import com.ibm.rchland.mfgapps.client.utils.messagebox.IGSMessageBox;
import com.ibm.rchland.mfgapps.mfsclient.media.MFSMediaHandler;

/**
 * <code>MFSIRMediaButton</code> is a subclass of <code>JButton</code> used
 * with Interactive Routing to view/play a media file.
 * @author The MFS Client Development Team
 */
public class MFSIRMediaButton
	extends JButton
	implements ActionListener
{
	private static final long serialVersionUID = 1L;
	/**
	 * The <code>MFSPartInstructionJPanel</code> that displays this
	 * <code>MFSIRMediaButton</code>.
	 */
	private MFSPartInstructionJPanel fieldPip;

	/** The media associated with this <code>MFSIRMediaButton</code>. */
	private String fieldMedia;

	/**
	 * Constructs a new <code>MFSIRMediaButton</code>.
	 * @param pip the <code>MFSPartInstructionJPanel</code> that will display
	 *        this <code>MFSIRMediaButton</code>
	 * @param fileName the file name of the media
	 */
	public MFSIRMediaButton(MFSPartInstructionJPanel pip, String fileName)
	{
		super();
		this.fieldPip = pip;
		this.fieldMedia = fileName;
		addActionListener(this);

		setMargin(new Insets(1, 1, 1, 1));

		fileName = fileName.toUpperCase();
		if (fileName.indexOf(".PDF") != -1) //$NON-NLS-1$
		{
			setIcon(new ImageIcon(getClass().getResource("/images/drawingIcon.gif"))); //$NON-NLS-1$
		}
		else if (fileName.indexOf(".GIF") != -1 //$NON-NLS-1$
				|| fileName.indexOf(".JPG") != -1 //$NON-NLS-1$
				|| fileName.indexOf(".JPEG") != -1 //$NON-NLS-1$
				|| fileName.indexOf(".BMP") != -1) //$NON-NLS-1$
		{
			setIcon(new ImageIcon(getClass().getResource("/images/imageIcon.gif"))); //$NON-NLS-1$
		}
		else if (fileName.indexOf(".MPEG") != -1 //$NON-NLS-1$
				|| fileName.indexOf(".MPG") != -1 //$NON-NLS-1$
				|| fileName.indexOf(".MOV") != -1 //$NON-NLS-1$
				|| fileName.indexOf(".AVI") != -1 //$NON-NLS-1$
				|| fileName.indexOf(".WMV") != -1) //$NON-NLS-1$
		{
			setIcon(new ImageIcon(getClass().getResource("/images/movieIcon.gif"))); //$NON-NLS-1$
		}
		else if (fileName.indexOf(".WAV") != -1 //$NON-NLS-1$
				|| fileName.indexOf(".MP3") != -1) //$NON-NLS-1$
		{
			setIcon(new ImageIcon(getClass().getResource("/images/audioIcon.gif"))); //$NON-NLS-1$
		}
		else if (fileName.indexOf(".HTM") != -1 //$NON-NLS-1$
				|| fileName.indexOf(".HTML") != -1) //$NON-NLS-1$
		{
			setIcon(new ImageIcon(getClass().getResource("/images/linkIcon.gif"))); //$NON-NLS-1$
		}
		else
		{
			setIcon(new ImageIcon(getClass().getResource("/images/unknownIcon.gif"))); //$NON-NLS-1$
		}

		/* Set up the tooltip text and label for the button. */
		int lastIndexOfSlash = fileName.lastIndexOf("/"); //$NON-NLS-1$
		if (lastIndexOfSlash != -1)
		{
			int indexOfDot = fileName.indexOf(".", lastIndexOfSlash); //$NON-NLS-1$
			if (indexOfDot != -1)
			{
				String text = fileName.substring(lastIndexOfSlash + 1, indexOfDot);
				setToolTipText(text);
				setText(text);
			}
			else
			{
				String text = fileName.substring(lastIndexOfSlash + 1);
				setToolTipText(text);
				setText(text);
			}
		}
		else
		{
			setToolTipText(fileName);
			setText(fileName);
		}

		setHorizontalTextPosition(SwingConstants.CENTER);
		setVerticalTextPosition(SwingConstants.BOTTOM);
	}

	/**
	 * Invoked when an action occurs.
	 * @param ae the <code>ActionEvent</code>
	 */
	public void actionPerformed(ActionEvent ae)
	{
		if (ae.getSource() == this)
		{
			try
			{   
				File file = null;
				//~1
				try
				{
					file = MFSMediaHandler.cacheRemoteFile(this.fieldMedia, "TLS");
				}
				catch (IOException ioe)
				{
					try
					{
						file = MFSMediaHandler.cacheRemoteFile(this.fieldMedia, "TLSv1.2");
					}
					catch (IOException ioe1)
					{
						IGSMessageBox.showOkMB(null, null, null, ioe1);
					}
				}
				if (file == null)
				{
					StringBuffer erms = new StringBuffer(35 + this.fieldMedia.length());
					erms.append("Could not download the media from ");
					erms.append(this.fieldMedia);
					erms.append(".");
					IGSMessageBox.showOkMB(this.fieldPip, null, erms.toString(), null);
				}
				else
				{
					MFSMediaHandler.handleMedia(file);
				}
			}
			catch (Exception e)
			{
				IGSMessageBox.showOkMB(this.fieldPip, null, null, e);
			}
		}
	}
}
