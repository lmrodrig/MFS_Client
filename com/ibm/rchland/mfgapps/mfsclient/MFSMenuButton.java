/* © Copyright IBM Corporation 2007. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2007-02-19      34242JR  R Prechel        -Initial version
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingConstants;

import com.ibm.rchland.mfgapps.mfsclient.utils.MFSConstants;
import com.ibm.rchland.mfgapps.mfscommon.MFSConfig;

/**
 * <code>MFSMenuButton</code> is the subclass of <code>JButton</code> that
 * is used for the menu buttons in the <i>MFS Client</i>.
 * @author The MFS Client Development Team
 */
public class MFSMenuButton
	extends JButton
{
	private static final long serialVersionUID = 1L;

	/**
	 * The default amount of space for the margins of a large or medium
	 * <code>MFSMenuButton</code>. A small margin is required to prevent the
	 * text from being truncated and replaced with an ellipsis (...).
	 */
	private static final int LARGE_MARGIN = 2;

	/**
	 * The default amount of space for the margins of a small
	 * <code>MFSMenuButton</code>. A small margin is required to prevent the
	 * text from being truncated and replaced with an ellipsis (...).
	 */
	private static final int SMALL_MARGIN = 0;

	/** The default height of a large <code>MFSMenuButton</code>. */
	private static final int LARGE_HEIGHT = 70;

	/** The default width of a large <code>MFSMenuButton</code>. */
	private static final int LARGE_WIDTH = 100;

	/** The default width of a medium <code>MFSMenuButton</code>. */
	private static final int MEDIUM_WIDTH = 85;

	/** The default height of a small <code>MFSMenuButton</code>. */
	private static final int SMALL_HEIGHT = 34;

	/** The default width of a small <code>MFSMenuButton</code>. */
	private static final int SMALL_WIDTH = 62;

	/** The directory from which images are loaded. */
	private static final String IMAGE_DIRECTORY = "/images/"; //$NON-NLS-1$

	/**
	 * The value of {@link #fieldActive} if the result of {@link #isActive()}
	 * depends on the existence of a config entry for {@link #fieldConfigLabel}.
	 */
	private static final int ACTIVE_CONFIG = 0;

	/**
	 * The value of {@link #fieldActive} if {@link #isActive()} should return
	 * <code>false</code> without checking the value of the config entry
	 * corresponding to {@link #fieldConfigLabel}.
	 */
	private static final int ACTIVE_FALSE = 1;

	/**
	 * The value of {@link #fieldActive} if {@link #isActive()} should return
	 * <code>true</code> without checking the value of the config entry
	 * corresponding to {@link #fieldConfigLabel}.
	 */
	private static final int ACTIVE_TRUE = 2;
	
	/** The <code>Font</code> used by an <code>MFSMenuButton</code>. */
	private static final Font FONT = determineButtonFont(); 

	/**
	 * Used to determine the result of {@link #isActive()}.
	 * @see #ACTIVE_CONFIG
	 * @see #ACTIVE_FALSE
	 * @see #ACTIVE_TRUE
	 */
	private int fieldActive = ACTIVE_CONFIG;

	/**
	 * The label for the config entry that enables the
	 * <code>MFSMenuButton</code>.
	 */
	private String fieldConfigLabel = null;

	/**
	 * Creates a large <code>MFSMenuButton</code>.
	 * @param text the text displayed on the <code>MFSMenuButton</code>. This
	 *        is also used to explicitly set the action command and name.
	 * @param imageFilename the filename of the image file used to create the
	 *        <code>Icon</code> displayed on the <code>MFSMenuButton</code>.
	 *        This parameter should not include any directory information; this
	 *        method looks in the {@link #IMAGE_DIRECTORY} for the file.
	 * @param tooltip the tooltip text for the <code>MFSMenuButton</code>
	 * @param config the label for the config entry that enables the
	 *        <code>MFSMenuButton</code>
	 * @return the newly created <code>MFSMenuButton</code>. If no file with
	 *         the specified <code>imageFilename</code> is found, a
	 *         <code>MFSMenuButton</code> without an <code>Icon</code> is
	 *         created and returned.
	 * @see MFSMenuButton#MFSMenuButton(String, String, String, String, int, int, int)
	 */
	public static MFSMenuButton createLargeButton(String text, String imageFilename,
													String tooltip, String config)
	{
		return new MFSMenuButton(text, imageFilename, tooltip, config, LARGE_WIDTH,
				LARGE_HEIGHT, LARGE_MARGIN);
	}

	/**
	 * Creates a large <code>MFSMenuButton</code>.
	 * @param text the text displayed on the <code>MFSMenuButton</code>. This
	 *        is also used to explicitly set the action command and name.
	 * @param imageFilename the filename of the image file used to create the
	 *        <code>Icon</code> displayed on the <code>MFSMenuButton</code>.
	 *        This parameter should not include any directory information; this
	 *        method looks in the {@link #IMAGE_DIRECTORY} for the file.
	 * @param tooltip the tooltip text for the <code>MFSMenuButton</code>
	 * @param active <code>true</code> if the <code>MFSMenuButton</code> is
	 *        active; <code>false</code> if the <code>MFSMenuButton</code>
	 *        is inactive. An inactive <code>MFSMenuButton</code> may not be
	 *        added to the menu.
	 * @return the newly created <code>MFSMenuButton</code>. If no file with
	 *         the specified <code>imageFilename</code> is found, a
	 *         <code>MFSMenuButton</code> without an <code>Icon</code> is
	 *         created and returned.
	 * @see MFSMenuButton#MFSMenuButton(String, String, String, String, int, int, int)
	 */
	public static MFSMenuButton createLargeButton(String text, String imageFilename,
													String tooltip, boolean active)
	{
		MFSMenuButton result = new MFSMenuButton(text, imageFilename, tooltip, null,
				LARGE_WIDTH, LARGE_HEIGHT, LARGE_MARGIN);
		result.setActive(active);
		result.setEnabled(active);
		return result;
	}

	/**
	 * Creates a medium <code>MFSMenuButton</code>.
	 * @param text the text displayed on the <code>MFSMenuButton</code>. This
	 *        is also used to explicitly set the action command and name.
	 * @param imageFilename the filename of the image file used to create the
	 *        <code>Icon</code> displayed on the <code>MFSMenuButton</code>.
	 *        This parameter should not include any directory information; this
	 *        method looks in the {@link #IMAGE_DIRECTORY} for the file.
	 * @param tooltip the tooltip text for the <code>MFSMenuButton</code>
	 * @param config the label for the config entry that enables the
	 *        <code>MFSMenuButton</code>
	 * @return the newly created <code>MFSMenuButton</code>. If no file with
	 *         the specified <code>imageFilename</code> is found, a
	 *         <code>MFSMenuButton</code> without an <code>Icon</code> is
	 *         created and returned.
	 * @see MFSMenuButton#MFSMenuButton(String, String, String, String, int, int, int)
	 */
	public static MFSMenuButton createMediumButton(String text, String imageFilename,
													String tooltip, String config)
	{
		return new MFSMenuButton(text, imageFilename, tooltip, config, MEDIUM_WIDTH,
				LARGE_HEIGHT, LARGE_MARGIN);
	}
	
	/**
	 * Creates a medium <code>MFSMenuButton</code>.
	 * @param text the text displayed on the <code>MFSMenuButton</code>. This
	 *        is also used to explicitly set the action command and name.
	 * @param imageFilename the filename of the image file used to create the
	 *        <code>Icon</code> displayed on the <code>MFSMenuButton</code>.
	 *        This parameter should not include any directory information; this
	 *        method looks in the {@link #IMAGE_DIRECTORY} for the file.
	 * @param tooltip the tooltip text for the <code>MFSMenuButton</code>
	 * @param active <code>true</code> if the <code>MFSMenuButton</code> is
	 *        active; <code>false</code> if the <code>MFSMenuButton</code>
	 *        is inactive. An inactive <code>MFSMenuButton</code> may not be
	 *        added to the menu.
	 * @return the newly created <code>MFSMenuButton</code>. If no file with
	 *         the specified <code>imageFilename</code> is found, a
	 *         <code>MFSMenuButton</code> without an <code>Icon</code> is
	 *         created and returned.
	 * @see MFSMenuButton#MFSMenuButton(String, String, String, String, int, int, int)
	 */
	public static MFSMenuButton createMediumButton(String text, String imageFilename,
													String tooltip, boolean active)
	{
		MFSMenuButton result = new MFSMenuButton(text, imageFilename, tooltip, null,
				MEDIUM_WIDTH, LARGE_HEIGHT, LARGE_MARGIN);
		result.setActive(active);
		result.setEnabled(active);
		return result;
	}

	/**
	 * Creates a small <code>MFSMenuButton</code>.
	 * @param text the text displayed on the <code>MFSMenuButton</code>. This
	 *        is also used to explicitly set the action command and name.
	 * @param imageFilename the filename of the image file used to create the
	 *        <code>Icon</code> displayed on the <code>MFSMenuButton</code>.
	 *        This parameter should not include any directory information; this
	 *        method looks in the {@link #IMAGE_DIRECTORY} for the file.
	 * @param tooltip the tooltip text for the <code>MFSMenuButton</code>
	 * @param config the label for the config entry that enables the
	 *        <code>MFSMenuButton</code>
	 * @return the newly created <code>MFSMenuButton</code>. If no file with
	 *         the specified <code>imageFilename</code> is found, a
	 *         <code>MFSMenuButton</code> without an <code>Icon</code> is
	 *         created and returned.
	 * @see MFSMenuButton#MFSMenuButton(String, String, String, String, int, int, int)
	 */
	public static MFSMenuButton createSmallButton(String text, String imageFilename,
													String tooltip, String config)
	{
		return new MFSMenuButton(text, imageFilename, tooltip, config, SMALL_WIDTH,
				SMALL_HEIGHT, SMALL_MARGIN);
	}
	
	/**
	 * Creates a small <code>MFSMenuButton</code>.
	 * @param text the text displayed on the <code>MFSMenuButton</code>. This
	 *        is also used to explicitly set the action command and name.
	 * @param imageFilename the filename of the image file used to create the
	 *        <code>Icon</code> displayed on the <code>MFSMenuButton</code>.
	 *        This parameter should not include any directory information; this
	 *        method looks in the {@link #IMAGE_DIRECTORY} for the file.
	 * @param tooltip the tooltip text for the <code>MFSMenuButton</code>
	 * @param active <code>true</code> if the <code>MFSMenuButton</code> is
	 *        active; <code>false</code> if the <code>MFSMenuButton</code>
	 *        is inactive. An inactive <code>MFSMenuButton</code> may not be
	 *        added to the menu.
	 * @return the newly created <code>MFSMenuButton</code>. If no file with
	 *         the specified <code>imageFilename</code> is found, a
	 *         <code>MFSMenuButton</code> without an <code>Icon</code> is
	 *         created and returned.
	 * @see MFSMenuButton#MFSMenuButton(String, String, String, String, int, int, int)
	 */
	public static MFSMenuButton createSmallButton(String text, String imageFilename,
													String tooltip, boolean active)
	{
		MFSMenuButton result = new MFSMenuButton(text, imageFilename, tooltip, null,
				SMALL_WIDTH, SMALL_HEIGHT, SMALL_MARGIN);
		result.setActive(active);
		result.setEnabled(active);
		return result;
	}
	
	/**
	 * Determines the <code>Font</code> that will be used by
	 * <code>MFSMenuButton</code>s based on the os.name system property.
	 * @return the <code>Font</code>
	 */
	private static Font determineButtonFont()
	{
		String osName = System.getProperty("os.name"); //$NON-NLS-1$
		if("Linux".equalsIgnoreCase(osName)) //$NON-NLS-1$
		{
			return new Font("Dialog", Font.BOLD, 11); //$NON-NLS-1$
		}
		return MFSConstants.SMALL_DIALOG_FONT;
	}

	/**
	 * Constructs a new <code>MFSMenuButton</code>.
	 * @param text the text displayed on the <code>MFSMenuButton</code>. This
	 *        is also used to explicitly set the action command and name.
	 * @param imageFilename the filename of the image file used to create the
	 *        <code>Icon</code> displayed on the <code>MFSMenuButton</code>.
	 *        This parameter should not include any directory information; this
	 *        method looks in the {@link #IMAGE_DIRECTORY} for the file.
	 * @param tooltip the tooltip text for the <code>MFSMenuButton</code>
	 * @param config the label for the config entry that enables the
	 *        <code>MFSMenuButton</code>
	 * @param width the width of the <code>MFSMenuButton</code>
	 * @param height the height of the <code>MFSMenuButton</code>
	 * @param margin the margin of the <code>MFSMenuButton</code>
	 * @see JButton#JButton(String)
	 * @see JButton#JButton(String, javax.swing.Icon)
	 * @see javax.swing.AbstractButton#setActionCommand(String)
	 */
	public MFSMenuButton(String text, String imageFilename, String tooltip,
							String config, int width, int height, int margin)
	{
		super(text, getIcon(imageFilename, text));
		this.setName(text);
		this.setActionCommand(text);
		this.setToolTipText(tooltip);
		this.setHorizontalTextPosition(SwingConstants.CENTER);
		this.setVerticalTextPosition(SwingConstants.BOTTOM);
		this.setFont(FONT);
		this.setEnabled(false);
		this.setRequestFocusEnabled(true);
		this.fieldConfigLabel = config;

		this.setMinimumSize(new Dimension(width, height));
		this.setPreferredSize(new Dimension(width, height));
		this.setMaximumSize(new Dimension(width, height));
		this.setMargin(new Insets(margin, margin, margin, margin));
	}

	/**
	 * Creates an <code>Icon</code> for the specified
	 * <code>imageFilename</code>.
	 * @param imageFilename the filename of the image displayed by the
	 *        <code>Icon</code>
	 * @param text the text corresponding to the image
	 * @return the new <code>Icon</code>
	 */
	private static Icon getIcon(String imageFilename, String text)
	{
		Icon result = null;
		URL url = MFSPanel.class.getResource(IMAGE_DIRECTORY + imageFilename);
		if (url == null)
		{
			System.out.println("Image file " + imageFilename + " was not found.");
		}
		else
		{
			result = new ImageIcon(url, text);
		}
		return result;
	}

	/**
	 * Sets whether the <code>MFSMenuButton</code> is active.
	 * @param active <code>true</code> if the <code>MFSMenuButton</code> is
	 *        active; <code>false</code> if the <code>MFSMenuButton</code>
	 *        is inactive. An inactive <code>MFSMenuButton</code> may not be
	 *        added to the menu.
	 */
	public void setActive(boolean active)
	{
		if (active)
		{
			this.fieldActive = ACTIVE_TRUE;
		}
		else
		{
			this.fieldActive = ACTIVE_FALSE;
		}
	}

	/**
	 * Returns <code>true</code> iff the button is active. An inactive
	 * <code>MFSMenuButton</code> may not be added to the menu.
	 * @return <code>true</code> iff the button is active
	 */
	public boolean isActive()
	{
		boolean result = false;
		if (this.fieldActive == ACTIVE_CONFIG && this.fieldConfigLabel != null)
		{
			result = MFSConfig.getInstance().containsConfigEntry(this.fieldConfigLabel);
		}
		else if (this.fieldActive == ACTIVE_TRUE)
		{
			result = true;
		}
		else if (this.fieldActive == ACTIVE_FALSE)
		{
			result = false;
		}
		return result;
	}
}
