/* @ Copyright IBM Corporation 2005, 2010. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2007-03-13      34242JR  R Prechel        -Java 5 version
 * 2007-03-29   ~1 19622JM  R Prechel        -Update authorization check for EXDCF config entry
 * 2007-05-01   ~2 38495JM  R Prechel        -Log OS Information
 * 2007-05-24   ~3 37676JM  R Prechel        -Setup network measurement logging
 * 2007-06-18   ~4 37556CD  T He			 -Added switch button
 * 2007-06-29  ~05 27794JR  Toribio H        -Add posible values for TDSU by CHECK_AUTH
 *                                            RMVALLTEAR and RMVALLFKIT
 * 2007-10-18   ~6 39955JR  R Prechel        -Call CHECK_AUTH for Remove All Non Serialized button
 * 2007-11-06   ~7 40104PB  R Prechel        -Environment switch changes
 *                                           -Add MFSLabelButton for URL.
 *                                           -Change component variable names to use Hungarian notation.
 * 2007-11-27   ~8 33401JM  R Prechel        -Call W_MFSAUTF; use IGSMessageBox and MFSChangePwdDialog
 * 2010-01-12   ~9 37550JL  D Kloepping      -Update CHECK_AUTH DBGL field to bit mask authorization for button
 * 											  display.
 * 2010-03-17  ~10 47595MZ  Ray Perry        -Clear header hash on login
 * 2010-11-15      49513JM  Toribio H.       -Remove headerHash because is substituted for Trx Cache
 * 2010-11-17  ~11 49513JM  Toribio H.       -Clear Trx Cache from Cacheable Trxs.
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient.panel;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.TitledBorder;

import mfsxml.MfsXMLDocument;
import mfsxml.MfsXMLParser;

import com.ibm.rchland.mfgapps.client.utils.layout.IGSCenterLayout;
import com.ibm.rchland.mfgapps.client.utils.messagebox.IGSMessageBox;
import com.ibm.rchland.mfgapps.mfsclient.MFSDialog;
import com.ibm.rchland.mfgapps.mfsclient.MFSFrame;
import com.ibm.rchland.mfgapps.mfsclient.MFSLabelButton;
import com.ibm.rchland.mfgapps.mfsclient.MFSPanel;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSChangePwdDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.MFSPlantSelectionDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.log.MFSExtraPartDialog;
import com.ibm.rchland.mfgapps.mfsclient.dialog.log.MFSLogDeconfigPartDialog;
import com.ibm.rchland.mfgapps.mfsclient.media.MFSMediaHandler;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSConstants;
import com.ibm.rchland.mfgapps.mfscommon.MFSConfig;
import com.ibm.rchland.mfgapps.mfscommon.MFSLogInfoTransactionXML;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSComm;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSCommLogger;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSFixedTransaction;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSTransaction;
import com.ibm.rchland.mfgapps.mfscommon.comm.MFSXmlTransaction;
import com.ibm.rchland.mfgapps.mfscommon.comm.TrxManager;

/**
 * <code>MFSLogOnPanel</code> is the <code>MFSPanel</code> used to log on.
 * @author The MFS Client Development Team
 */
@SuppressWarnings("serial")
public class MFSLogOnPanel extends MFSPanel
{
	//~7A
	/** The default screen ID of an <code>MFSLogOnPanel</code>. */
	public static final String SCREEN_ID = "LOGON"; //$NON-NLS-1$

	/** The default screen name of an <code>MFSLogOnPanel</code>. */
	public static final String SCREEN_NAME = "User Authorization"; //$NON-NLS-1$

	//~7A
	/** The ID for the User Authority Tool URL. */
	public static final String AUTH_TOOL_URL_ID = "AUTH_TOOL"; //$NON-NLS-1$

	//~6A Added config entry constants
	/**
	 * Label for the TDSU config entry. The value (Y or N) is set based on the
	 * first bit of the bitmask returned by <code>CHECK_AUTH</code>. The
	 * ProcAll button on {@link MFSDeconfigPanel} is enabled only if the value
	 * of the config entry is Y.
	 */
	public static final String TDSU = "TDSU"; //$NON-NLS-1$

	/**
	 * Label for the SCRP config entry. The value (Y or N) is set based on the
	 * second bit of the bitmask returned by <code>CHECK_AUTH</code>. The
	 * Scrap button on {@link MFSLogDeconfigPartDialog} and
	 * {@link MFSExtraPartDialog} is enabled only if the value of the config
	 * entry is Y.
	 */
	public static final String SCRP = "SCRP"; //$NON-NLS-1$

	/**
	 * Label for the EXDCF config entry. The value (Y or N) is set based on the
	 * third bit of the bitmask returned by <code>CHECK_AUTH</code>. The
	 * Extra button on {@link MFSDeconfigPanel} is enabled only if the value of
	 * the config entry is Y.
	 */
	public static final String EXDCF = "EXDCF"; //$NON-NLS-1$

	/**
	 * Label for the RMVALLTEAR config entry. The value (Y or N) is set based on
	 * the fourth bit of the bitmask returned by <code>CHECK_AUTH</code>.
	 * During TEAR, the RemNonSer button on the {@link MFSDirectWorkPanel} is
	 * enabled only if the value of the config entry is Y.
	 */
	public static final String RMVALLTEAR = "RMVALLTEAR"; //$NON-NLS-1$

	/**
	 * Label for the RMVALLFKIT config entry. The value (Y or N) is set based on
	 * the fifth bit of the bitmask returned by <code>CHECK_AUTH</code>.
	 * During FKIT, the RemNonSer button on {@link MFSDirectWorkPanel} is
	 * enabled only if the value of the config entry is Y.
	 */
	public static final String RMVALLFKIT = "RMVALLFKIT"; //$NON-NLS-1$

	/**
	 * Label for the DBGL config entry. The value (Y or N) is set based on the
	 * DBGL value returned by <code>CHECK_AUTH</code>. The DebugGolden button
	 * on {@link MFSDirectWorkPanel} is enabled only if the value of the config
	 * entry is Y.
	 */
	public static final String DBGL = "DBGL"; //$NON-NLS-1$

	/** The password <code>JTextField</code>. */
	private JTextField tfPassword; //~7C ~8C

	/** The user ID <code>JTextField</code>. */
	private JTextField tfUserID; //~7C

	/** The user authorization <code>JLabel</code>. */
	private JLabel lblUserAuth; //~4C ~7C

	/** The button <code>JPanel</code>. */
	private JPanel pnlButton; //~4C ~7C

	/** The environment <code>JLabel</code>. */
	private JLabel lblEnvironment; //~4A ~7C

	/** The Clear <code>JButton</code>. */
	private JButton pbClear = MFSDialog.createButton("Clear", 'C'); //~7C //$NON-NLS-1$

	/** The Log On <code>JButton</code>. */
	private JButton pbLogon = MFSDialog.createButton("Log On", 'L'); //~7C //$NON-NLS-1$

	/** The Exit <code>JButton</code>. */
	private JButton pbExit = MFSDialog.createButton("Exit", 'x'); //~7C //$NON-NLS-1$

	/** The Switch <code>JButton</code>. */
	private JButton pbSwitch = MFSDialog.createButton("Switch", 'S'); //~4A ~7C //$NON-NLS-1$

	/** The <code>MFSLabelButton</code> for the authorization tool URL. */
	private MFSLabelButton lbAuthTool = 
		new MFSLabelButton("User Authority Tool", AUTH_TOOL_URL_ID);//~7A //$NON-NLS-1$

	/** The <code>MyLabelButtonListener</code> for the <code>MFSLabelButton</code>. */
	private MyLabelButtonListener fieldLabelButtonListener = new MyLabelButtonListener(); //~7A

	/**
	 * Constructs a new <code>MFSLogOnPanel</code>.
	 * @param parent the <code>MFSFrame</code> used to display the panel
	 */
	public MFSLogOnPanel(MFSFrame parent)
	{
		super(parent, null, SCREEN_NAME, SCREEN_ID, new IGSCenterLayout()); //~7C
		createLayout();
		addMyListeners();
	}

	/** Adds the listeners to this panel's <code>Component</code>s. */
	private void addMyListeners()
	{
		this.tfUserID.addKeyListener(this);
		this.tfPassword.addKeyListener(this);
		this.pbLogon.addKeyListener(this);
		this.pbClear.addKeyListener(this);
		this.pbExit.addKeyListener(this);
		this.pbSwitch.addKeyListener(this); //~4A

		this.pbLogon.addActionListener(this);
		this.pbClear.addActionListener(this);
		this.pbExit.addActionListener(this);
		this.pbSwitch.addActionListener(this); //~4A

		this.lbAuthTool.addMouseListener(this.fieldLabelButtonListener); //~7A
	}

	/** Removes the listeners from this panel's <code>Component</code>s. */
	private void removeMyListeners()
	{
		this.tfUserID.removeKeyListener(this);
		this.tfPassword.removeKeyListener(this);
		this.pbLogon.removeKeyListener(this);
		this.pbClear.removeKeyListener(this);
		this.pbExit.removeKeyListener(this);
		this.pbSwitch.removeKeyListener(this); //~4A

		this.pbLogon.removeActionListener(this);
		this.pbClear.removeActionListener(this);
		this.pbExit.removeActionListener(this);
		this.pbSwitch.removeActionListener(this); //~4A

		this.lbAuthTool.removeMouseListener(this.fieldLabelButtonListener); //~7A
	}

	/** Requests focus for the user ID text field. */
	public void assignFocus()
	{
		this.tfUserID.requestFocusInWindow();
	}

	/**
	 * Displays the <code>MFSPlantSelectionDialog</code> if any alternate
	 * plants exist in the configuration.
	 */
	private void selectPlant()
	{
		try
		{
			removeMyListeners();
			MFSPlantSelectionDialog dialog = new MFSPlantSelectionDialog(getParentFrame());
			dialog.setLocationRelativeTo(getParentFrame());
			dialog.setVisible(true);
		}
		catch (Exception e)
		{
			//~7C Update the text
			String title = "Error Switching Environment"; //$NON-NLS-1$
			String message = "Unable to switch environment."; //$NON-NLS-1$
			IGSMessageBox.showOkMB(getParentFrame(), title, message, e);
		}
		finally
		{
			addMyListeners();
		}
	}

	/**
	 * Adjusts the text of the user auth label and the environment label based
	 * on the specified <code>environment</code>.
	 * @param environment the name of the selected environment
	 */
	public void setEnvironment(String environment)
	{
		//~7C Don't check for blank; updated the text
		this.lblUserAuth.setText(SCREEN_NAME + " - " + environment); //$NON-NLS-1$
		this.lblEnvironment.setText("You are logging into " + environment + "."); //$NON-NLS-1$ //$NON-NLS-2$
	}

	//~4A
	/**
	 * Adds the switch button if {@link MFSConfig#isEnvironmentSwitchEnabled()}
	 * returns <code>true</code>; removes the button otherwise.
	 */
	public void configSwitchButton()
	{
		//~7C Use isEnvironmentSwitchEnabled method
		if (MFSConfig.getInstance().isEnvironmentSwitchEnabled())
		{
			this.pnlButton.add(this.pbSwitch);
			this.pbSwitch.setEnabled(true);
		}
		else
		{
			this.pbSwitch.setEnabled(false);
			this.pnlButton.remove(this.pbSwitch);
		}
	}

	/** Adds this panel's <code>Component</code>s to the layout. */
	protected void createLayout()
	{
		//~4A create plant label
		this.lblEnvironment = new JLabel(" "); //$NON-NLS-1$
		this.lblEnvironment.setFont(MFSConstants.LARGE_PLAIN_DIALOG_FONT);
		this.lblEnvironment.setForeground(Color.red);

		this.lblUserAuth = new JLabel(SCREEN_NAME);
		this.lblUserAuth.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
		this.lblUserAuth.setOpaque(true);
		this.lblUserAuth.setBackground(TITLE_BORDER_COLOR);
		this.lblUserAuth.setForeground(MFSConstants.PRIMARY_BACKGROUND_COLOR);
		this.lblUserAuth.setFont(MFSConstants.LARGE_PLAIN_DIALOG_FONT);

		JLabel keyIconLabel = new JLabel();
		keyIconLabel.setIcon(new ImageIcon(getClass().getResource("/images/Msdb.gif"))); //$NON-NLS-1$
		keyIconLabel.setHorizontalAlignment(SwingConstants.CENTER);

		this.pnlButton = new JPanel(new GridLayout(1, 0, 40, 8));
		this.pnlButton.setBackground(MFSConstants.PRIMARY_BACKGROUND_COLOR);
		this.pnlButton.add(this.pbLogon);
		this.pnlButton.add(this.pbClear);
		this.pnlButton.add(this.pbExit);
		configSwitchButton(); //~4A

		//create centerPanel
		JPanel centerPanel = new JPanel(new GridBagLayout());
		centerPanel.setBorder(new CompoundBorder(new BevelBorder(BevelBorder.RAISED),
				new BevelBorder(BevelBorder.LOWERED)));
		centerPanel.setBackground(MFSConstants.PRIMARY_BACKGROUND_COLOR);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(0, 0, 25, 0);
		centerPanel.add(this.lblUserAuth, gbc);

		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(0, 25, 0, 0);
		centerPanel.add(keyIconLabel, gbc);

		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.insets = new Insets(0, 0, 0, 0);
		centerPanel.add(createSignOnPanel(), gbc);

		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridwidth = 2;
		gbc.insets = new Insets(25, 25, 0, 25);
		centerPanel.add(this.pnlButton, gbc);

		JPanel urlPanel = new JPanel(new FlowLayout());
		urlPanel.setBackground(MFSConstants.PRIMARY_BACKGROUND_COLOR);

		//~7A Setup MFSLabelButton for the URL
		this.lbAuthTool.setFont(MFSConstants.MEDIUM_DIALOG_FONT);
		this.lbAuthTool.setForeground(Color.blue);

		urlPanel.add(MFSDialog.createLabel("Use the")); //$NON-NLS-1$
		urlPanel.add(this.lbAuthTool);
		urlPanel.add(MFSDialog.createLabel("web application to request a user ID (if necessary).")); //$NON-NLS-1$

		//~7A create contentPanel
		JPanel contentPanel = new JPanel(new GridBagLayout());
		contentPanel.setBackground(MFSConstants.PRIMARY_BACKGROUND_COLOR);

		//~7C Add plantLabel, centerPanel, and urlPanel to contentPanel
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		contentPanel.add(this.lblEnvironment, gbc);
		gbc.gridy = 1;
		gbc.insets.top = 50;
		gbc.insets.bottom = 25;
		contentPanel.add(centerPanel, gbc);
		String urlKey = MFSMediaHandler.urlKey(this, AUTH_TOOL_URL_ID);
		if (MFSConfig.getInstance().containsConfigEntry(urlKey))
		{
			gbc.gridy = 2;
			gbc.insets.top = 0;
			gbc.insets.bottom = 0;
			contentPanel.add(urlPanel, gbc);
		}

		this.add(contentPanel);
	}

	/**
	 * Creates the <code>TitledBorder</code> for this panel.
	 * @return the <code>TitledBorder</code> for this panel
	 */
	protected TitledBorder createTitledBorder()
	{
		return BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder(TITLE_BORDER_COLOR),
				"Sign On", TitledBorder.LEFT, TitledBorder.TOP, //$NON-NLS-1$
				MFSConstants.LARGE_PLAIN_DIALOG_FONT, TITLE_BORDER_COLOR);
	}

	/**
	 * Creates the <code>JPanel</code> that contains the user ID and password
	 * <code>JLabel</code>s and text fields.
	 * @return the new <code>JPanel</code>
	 */
	private JPanel createSignOnPanel()
	{
		JPanel result = new JPanel(new GridBagLayout());
		result.setBorder(createTitledBorder());
		result.setBackground(MFSConstants.PRIMARY_BACKGROUND_COLOR);

		GridBagConstraints gbc = new GridBagConstraints();

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(15, 15, 5, 10);
		gbc.ipadx = 5;
		gbc.ipady = 3;
		JLabel userIDLabel = createJLabel("User ID"); //$NON-NLS-1$
		result.add(userIDLabel, gbc);

		gbc.gridx++;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(15, 0, 5, 15);
		gbc.ipadx = 0;
		gbc.ipady = 0;
		this.tfUserID = MFSDialog.createTextField(10, 8, userIDLabel);
		this.tfUserID.setFont(MFSConstants.LARGE_MONOSPACED_FONT);
		result.add(this.tfUserID, gbc);

		gbc.gridx--;
		gbc.gridy++;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(0, 15, 15, 10);
		gbc.ipadx = 5;
		gbc.ipady = 3;
		JLabel passwordLabel = createJLabel("Password"); //$NON-NLS-1$
		result.add(passwordLabel, gbc);

		gbc.gridx++;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(0, 0, 15, 15);
		gbc.ipadx = 0;
		gbc.ipady = 0;
		this.tfPassword = MFSDialog.createPasswordField(10, 8, passwordLabel);
		this.tfPassword.setFont(MFSConstants.LARGE_MONOSPACED_FONT);
		result.add(this.tfPassword, gbc);
		return result;
	}

	/**
	 * Creates a <code>JLabel</code> for this panel that displays the
	 * specified <code>text</code> with a blue foreground.
	 * @param text the text displayed by the <code>JLabel</code>
	 * @return the new <code>JLabel</code>
	 */
	private JLabel createJLabel(String text)
	{
		JLabel result = new JLabel(text);
		result.setOpaque(true);
		result.setForeground(TITLE_BORDER_COLOR);
		result.setHorizontalTextPosition(SwingConstants.CENTER);
		result.setHorizontalAlignment(SwingConstants.CENTER);
		result.setFont(MFSConstants.LARGE_PLAIN_DIALOG_FONT);
		return result;
	}

	//~8C Change to use W_MFSAUTF
	/** Perform the logon transactions. */
	public void logon()
	{
		final MFSConfig config = MFSConfig.getInstance();
		final MFSFrame frame = getParentFrame();
		try
		{
			removeMyListeners();
			String user = this.tfUserID.getText().toUpperCase();
			String password = this.tfPassword.getText().toUpperCase();

			if (user.length() == 0)
			{
				String title = "Invalid Input"; //$NON-NLS-1$
				String erms = "Please enter your user ID."; //$NON-NLS-1$
				IGSMessageBox.showOkMB(frame, title, erms, null);
				this.tfUserID.requestFocusInWindow();
			}
			else if (password.length() == 0)
			{
				String title = "Invalid Input"; //$NON-NLS-1$
				String erms = "Please enter your password."; //$NON-NLS-1$
				IGSMessageBox.showOkMB(frame, title, erms, null);
				this.tfPassword.requestFocusInWindow();
			}
			else
			{
				MfsXMLDocument input = new MfsXMLDocument("W_MFSAUTF"); //$NON-NLS-1$
				input.addOpenTag("DATA"); //$NON-NLS-1$
				input.addCompleteField("WHO", "MFSCLIENT"); //$NON-NLS-1$ //$NON-NLS-2$
				input.addCompleteField("FUNC", "CHECK"); //$NON-NLS-1$ //$NON-NLS-2$
				input.addCompleteField("USER", user); //$NON-NLS-1$ 
				input.addCompleteField("PWD", password); //$NON-NLS-1$ 
				input.finalizeXML();

				MFSTransaction w_mfsautf = new MFSXmlTransaction(input.toString());
				w_mfsautf.setActionMessage("Verifying User ID and Password..."); //$NON-NLS-1$
				MFSComm.getInstance().execute(w_mfsautf, this);

				if (w_mfsautf.getReturnCode() == 0)
				{
					/* add user to configuration data */
					config.setConfigValue("USER", user); //$NON-NLS-1$

					MfsXMLParser parser = new MfsXMLParser(w_mfsautf.getOutput());
					pwdExpirationNotice(Integer.parseInt(parser.getField("DAYS"))); //$NON-NLS-1$
					logtrkinfo();

					if (checkAuth() == 0)
					{
						this.tfUserID.setText(""); //$NON-NLS-1$
						this.tfPassword.setText(""); //$NON-NLS-1$
						frame.displayMFSPanel(new MFSSelectWorkPanel(frame, this));
					}
					else
					{
						this.tfUserID.setText(""); //$NON-NLS-1$
						this.tfPassword.setText(""); //$NON-NLS-1$
						this.tfUserID.requestFocusInWindow();
					}
				}
				else
				{
					String title = "Authorization Error"; //$NON-NLS-1$
					IGSMessageBox.showOkMB(frame, title, w_mfsautf.getErms(), null);
					this.tfUserID.setText(""); //$NON-NLS-1$
					this.tfPassword.setText(""); //$NON-NLS-1$
					this.tfUserID.requestFocusInWindow();
				}
			}
		}
		catch (Exception e)
		{
			String title = "Program Exception"; //$NON-NLS-1$
			IGSMessageBox.showOkMB(frame, title, null, e);
			this.tfUserID.setText(""); //$NON-NLS-1$
			this.tfPassword.setText(""); //$NON-NLS-1$
			this.tfUserID.requestFocusInWindow();
		}
		finally
		{
			addMyListeners();
		}
	}

	//~1A Moved logic from logon method to new method
	/**
	 * Sets the values of the {@link #TDSU}, {@link #SCRP}, {@link #EXDCF},
	 * {@link #RMVALLTEAR}, {@link #RMVALLFKIT} and {@link #DBGL} config entries.\
	 * If the DECONFIG, FKIT, TEARDOWN, BAAPPLY, BARELEASE  button (from {@link MFSSelectWorkPanel})
	 * is configured, the CHECK_AUTH transaction is executed to determine the value of these config 
	 * entries, otherwise the value of these config entries is set to "N".
	 * @return the return code of CHECK_AUTH or 0 if CHECK_AUTH was not executed
	 */
	private int checkAuth()
	{
		//~6C Use constants for config labels
		int rc = 0;
		final MFSConfig config = MFSConfig.getInstance();
		config.getCooHash().clear();	// ~10
		TrxManager.getInstace().resetCacheFromCachedList(); //~11

		if (config.containsConfigEntry("BUTTON,SELWORK,DECONFIG") == false //$NON-NLS-1$
				&& config.containsConfigEntry("BUTTON,SELWORK,FKIT") == false //$NON-NLS-1$
				&& config.containsConfigEntry("BUTTON,SELWORK,TEARDOWN") == false //$NON-NLS-1$ //~6A
			    && config.containsConfigEntry("BUTTON,SELWORK,RLSEBA") == false //$NON-NLS-1$ //~9A
				&& config.containsConfigEntry("BUTTON,SELWORK,APPLYBA") == false) //$NON-NLS-1$ //~9A
		{
			//The Select Work buttons are not configured, so the user
			//can't access the functionality protected by CHECK_AUTH.
			//Thus, there is no need to call CHECK_AUTH and
			//the config entry values can be set to "N".
			config.setConfigValue(TDSU, "N"); //$NON-NLS-1$ 
			config.setConfigValue(SCRP, "N"); //$NON-NLS-1$ 
			//~1A Add EXDCF config value
			config.setConfigValue(EXDCF, "N"); //$NON-NLS-1$ 
			//~6A Set RMVALLTEAR and RMVALLFKIT to N
			config.setConfigValue(RMVALLTEAR, "N"); //$NON-NLS-1$ 
			config.setConfigValue(RMVALLFKIT, "N"); //$NON-NLS-1$ 
			config.setConfigValue(DBGL, "N"); //$NON-NLS-1$ 
		}
		else
		{
			/*
			 * The first character returned by CHECK_AUTH is the TDSU field a
			 * 5-bit (base 32) integer bitmask. The second character returned
			 * by CHECK_AUTH is the DBGL value, also a 5 bit (base 32) integer
			 * bitmask. These values are used to set config entry values.
			 * See the JavaDoc comments for the config entry labels for more
			 * information.
			 */
			StringBuffer inputBuffer = new StringBuffer();
			inputBuffer.append("CHECK_AUTH"); //$NON-NLS-1$
			inputBuffer.append(config.get8CharUser());

			MFSTransaction check_auth = new MFSFixedTransaction(inputBuffer.toString());
			check_auth.setActionMessage("Checking deconfig authorization..."); //$NON-NLS-1$
			MFSComm.getInstance().execute(check_auth, this);
			rc = check_auth.getReturnCode();

			if (rc == 0)
			{
				//~1C Change logic to use a bitmask
				int bitmask;
				try
				{
					bitmask = Integer.parseInt(check_auth.getOutput().substring(0, 1), 32);
				}
				catch (NumberFormatException nfe)
				{
					bitmask = 0;
				}

				if ((bitmask & 1) == 1)
				{
					config.setConfigValue(TDSU, "Y"); //$NON-NLS-1$
				}
				else
				{
					config.setConfigValue(TDSU, "N"); //$NON-NLS-1$
				}

				if ((bitmask & 2) == 2)
				{
					config.setConfigValue(SCRP, "Y"); //$NON-NLS-1$
				}
				else
				{
					config.setConfigValue(SCRP, "N"); //$NON-NLS-1$
				}

				//~1A Add check for EXDCF
				if ((bitmask & 4) == 4)
				{
					config.setConfigValue(EXDCF, "Y"); //$NON-NLS-1$
				}
				else
				{
					config.setConfigValue(EXDCF, "N"); //$NON-NLS-1$
				}

				//~05A Add check for RMVALLTEAR
				if ((bitmask & 8) == 8)
				{
					config.setConfigValue(RMVALLTEAR, "Y"); //$NON-NLS-1$
				}
				else
				{
					config.setConfigValue(RMVALLTEAR, "N"); //$NON-NLS-1$
				}

				//~05A Add check for RMVALLFKIT
				if ((bitmask & 16) == 16)
				{
					config.setConfigValue(RMVALLFKIT, "Y"); //$NON-NLS-1$
				}
				else
				{
					config.setConfigValue(RMVALLFKIT, "N"); //$NON-NLS-1$
				}

				//~9C Change logic to use a bitmask
				int DBGLbitmask;
				try
				{
					DBGLbitmask = Integer.parseInt(check_auth.getOutput().substring(1, 2), 32);
				}
				catch (NumberFormatException nfe)
				{
					DBGLbitmask = 0;
				}
				if ((DBGLbitmask & 1) == 1)
				{
					config.setConfigValue(DBGL, "Y"); //$NON-NLS-1$
				}
				else
				{
					config.setConfigValue(DBGL, "N"); //$NON-NLS-1$
				}
				if ((DBGLbitmask & 2) == 2)
				{
					config.setConfigValue("BUTTON,SELWORK,APPLYBA",""); //$NON-NLS-1$ //$NON-NLS-2$
				}
				else
				{
					config.removeConfigEntry("BUTTON,SELWORK,APPLYBA"); //$NON-NLS-1$
				}

				if ((DBGLbitmask & 4) == 4)
				{
					config.setConfigValue("BUTTON,SELWORK,RLSEBA",""); //$NON-NLS-1$ //$NON-NLS-2$
				}
				else
				{
					config.removeConfigEntry("BUTTON,SELWORK,RLSEBA"); //$NON-NLS-1$
				}
		
			}
			else
			{
				IGSMessageBox.showOkMB(getParentFrame(), null, check_auth.getOutput(), null);
			}
		}

		return rc;
	}

	/** Executes the LOGTRKINFO transaction. */
	private void logtrkinfo()
	{
		MFSTransaction logtrkinfo = null;
		try
		{
			MFSLogInfoTransactionXML xml = new MFSLogInfoTransactionXML("MFSCLIENT"); //$NON-NLS-1$
			xml.initValues(); //~2C
			xml.setOSInfo(); //~2C
			logtrkinfo = new MFSXmlTransaction(xml.createXML());
			logtrkinfo.setActionMessage("Logging client information..."); //$NON-NLS-1$
			MFSComm.getInstance().execute(logtrkinfo, this);

			if (logtrkinfo.getReturnCode() < 0)
			{
				System.out.println("Error during LOGTRKINFO: " + logtrkinfo.getOutput()); //$NON-NLS-1$
			}
			else if (logtrkinfo.getReturnCode() > 0)
			{
				MfsXMLParser parser = new MfsXMLParser(logtrkinfo.getOutput());

				int wrnl = Integer.parseInt(parser.getField("WRNL").trim()); //$NON-NLS-1$
				if (wrnl != 0)
				{
					String message = parser.getField("EMSG"); //$NON-NLS-1$
					IGSMessageBox.showOkMB(getParent(), null, message, null);
				}
				if (wrnl == 200)
				{
					System.exit(0);
				}
			}

			MFSCommLogger.setActive(logtrkinfo.getOutput()); //~3A
		}
		catch (Exception e)
		{
			if (logtrkinfo != null)
			{
				System.err.println(logtrkinfo.getInput());
				System.err.println(logtrkinfo.getOutput());
			}
			e.printStackTrace();
			System.err.println();
		}
	}

	//~8C Change to use IGSMessageBox and MFSChangePwdDialog
	/**
	 * Alerts the user if his/her password has expired or is about to expire and
	 * displays an <code>MFSChangePwdDialog</code> if appropriate.
	 * @param daysTillPwdExpires the number of days until the password expires
	 */
	private void pwdExpirationNotice(int daysTillPwdExpires)
	{
		if (daysTillPwdExpires == 0)
		{
			MFSFrame frame = getParentFrame();
			String title = "Password Expired"; //$NON-NLS-1$
			String msg = "Your password has expired. Select OK to change it."; //$NON-NLS-1$
			IGSMessageBox.showOkMB(frame, title, msg, null);

			MFSChangePwdDialog dialog = new MFSChangePwdDialog(frame, true);
			dialog.setLocationRelativeTo(this);
			dialog.setVisible(true);
		}
		else if (daysTillPwdExpires <= 7)
		{
			MFSFrame frame = getParentFrame();
			String title = "Password Expiration Notice"; //$NON-NLS-1$
			String msg;
			if (daysTillPwdExpires == 1)
			{
				msg = "Your password will expire tomorrow.\nDo you want to change it now?"; //$NON-NLS-1$
			}
			else
			{
				StringBuffer message = new StringBuffer();
				message.append("Your password will expire in "); //$NON-NLS-1$
				message.append(daysTillPwdExpires);
				message.append(" days.\nDo you want to change it now?"); //$NON-NLS-1$
				msg = message.toString();
			}

			if (IGSMessageBox.showYesNoMB(frame, title, msg, null))
			{
				MFSChangePwdDialog dialog = new MFSChangePwdDialog(frame, false);
				dialog.setLocationRelativeTo(this);
				dialog.setVisible(true);
			}
		}
	}

	/**
	 * Invoked when an action occurs.
	 * @param ae the <code>ActionEvent</code>
	 */
	public void actionPerformed(ActionEvent ae)
	{
		try
		{
			final Object source = ae.getSource();
			if (source == this.pbLogon)
			{
				this.logon();
			}
			else if (source == this.pbClear)
			{
				//~8C Removed clear method
				this.tfUserID.setText(""); //$NON-NLS-1$
				this.tfPassword.setText(""); //$NON-NLS-1$
				this.tfUserID.requestFocusInWindow();
			}
			else if (source == this.pbExit)
			{
				System.exit(0);
			}
			else if (source == this.pbSwitch) //begin ~4A
			{
				this.selectPlant();
			}//end ~4A
		}
		catch (Throwable throwable)
		{
			throwable.printStackTrace();
		}
	}

	/**
	 * Invoked when a key is pressed.
	 * @param ke the <code>KeyEvent</code>
	 */
	public void keyPressed(KeyEvent ke)
	{
		try
		{
			if (ke.getKeyCode() == KeyEvent.VK_ENTER)
			{
				final Object source = ke.getSource();
				if (source == this.tfUserID || source == this.tfPassword
						|| source == this.pbLogon)
				{
					this.pbLogon.doClick();
				}
				else if (source == this.pbClear)
				{
					this.pbClear.doClick();
				}
				else if (source == this.pbExit)
				{
					this.pbExit.doClick();
				}
				else if (source == this.pbSwitch) //begin ~4
				{
					this.pbSwitch.doClick();
				} //end ~4
			}
		}
		catch (Throwable throwable)
		{
			throwable.printStackTrace();
		}
	}

	//~7A New class
	/**
	 * <code>MyLabelButtonListener</code> is the <code>MouseListener</code>
	 * for the <code>MFSLabelButton</code>.
	 * @author The MFS Client Development Team
	 */
	private class MyLabelButtonListener
		extends MouseAdapter
	{
		/** Constructs a new <code>MyLabelButtonListener</code>. */
		public MyLabelButtonListener()
		{
			super();
		}

		/**
		 * Invoked when the mouse has been clicked on a <code>Component</code>.
		 * @param me the <code>MouseEvent</code>
		 */
		public void mouseClicked(MouseEvent me)
		{
			String id = ((MFSLabelButton) me.getComponent()).getID();
			MFSMediaHandler.displayWebPage(MFSLogOnPanel.this, id);
		}
	}
}
