/* @ Copyright IBM Corporation 2002, 2007. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2007-02-16      34242JR  R Prechel        -Java 5 version
 * 2007-06-18 ~1   37556CD  T He			 -Added plant information
 * 2010-03-03 ~02  47596MZ  Toribio H.		 -Efficiency changes
 ******************************************************************************/
package com.ibm.rchland.mfgapps.mfsclient;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import com.ibm.rchland.mfgapps.client.utils.messagebox.IGSMessageBox;
import com.ibm.rchland.mfgapps.mfsclient.panel.MFSDeconfigPanel;
import com.ibm.rchland.mfgapps.mfsclient.panel.MFSDirWrkExtFnctPanel;
import com.ibm.rchland.mfgapps.mfsclient.panel.MFSDirectWorkPanel;
import com.ibm.rchland.mfgapps.mfsclient.panel.MFSEntityMergePanel;
import com.ibm.rchland.mfgapps.mfsclient.panel.MFSLogOnPanel;
import com.ibm.rchland.mfgapps.mfsclient.panel.MFSPartFunctionDisplayPanel;
import com.ibm.rchland.mfgapps.mfsclient.panel.MFSSelWrkExtFnctPanel;
import com.ibm.rchland.mfgapps.mfsclient.panel.MFSSelectWorkPanel;
import com.ibm.rchland.mfgapps.mfsclient.panel.MFSViewOpsPanel;
import com.ibm.rchland.mfgapps.mfsclient.utils.MFSConstants;
import com.ibm.rchland.mfgapps.mfscommon.MFSConfig;

/**
 * <code>MFSFrame</code> is a subclass of <code>JFrame</code> used as the
 * top level window for the MFS Client.
 * @author The MFS Client Development Team
 */
public class MFSFrame
	extends JFrame
{
	private static final long serialVersionUID = 1L;
	/** The <code>MFSDirectWorkPanel</code> displayed by the MFS Client. */
	private MFSDirectWorkPanel fieldDirWork = null;
	
	/** text field for plant information */
	private JTextField tfPlant = new JTextField(MFSConstants.SMALL_TF_COLS); //~1A
	
	/** Constructs a new <code>MFSFrame</code>. */
	public MFSFrame()
	{
		super();
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		setSize(850, 636);
	}

	/**
	 * Displays the specified <code>MFSPanel</code>.
	 * @param panel the <code>MFSPanel</code> to display
	 */
	public void displayMFSPanel(MFSPanel panel)
	{
		JPanel contentPane = new JPanel(new BorderLayout());
		contentPane.add(panel, BorderLayout.CENTER);
		MFSActionJPanel actionIndicator = panel.getActionIndicator(); //~1A
		actionIndicator.add(this.tfPlant, BorderLayout.EAST); //~1A
		contentPane.add(actionIndicator, BorderLayout.SOUTH); //~1A
		setContentPane(contentPane);
		setTitle(panel.getMFSPanelTitle());
		validate();
		panel.assignFocus();
	}
	
	/**
	 * Returns the <code>MFSDirectWorkPanel</code> that is displayed by the
	 * MFS Client. Responsible for the lazy instantiation of the panel.
	 * @return the <code>MFSDirectWorkPanel</code>
	 */
	public MFSDirectWorkPanel getDirectWorkPanel()
	{
		if(this.fieldDirWork == null)
		{
			System.out.println("Instantiating new MFSDirectWorkPanel"); //$NON-NLS-1$
			this.fieldDirWork = new MFSDirectWorkPanel(this, null);
		}
		return this.fieldDirWork;
	}
	
	/**
	 * This method sets the text content in the tfPlant text field ~1A
	 * 
	 * @param thePlantName plant name
	 */
	public void setPlantName(String thePlantName)
	{
		this.tfPlant.setText(thePlantName);
	}

	/**
	 * Restores a previously displayed <code>MFSPanel</code>.
	 * @param panel the <code>MFSPanel</code> from which the previous
	 *        <code>MFSPanel</code> will be determined. To restore the
	 *        <code>MFSPanel</code> displayed one screen ago, this should be
	 *        the currently displayed <code>MFSPanel</code>; to restore the
	 *        <code>MFSPanel</code> displayed two screens ago, this should be
	 *        the <code>MFSPanel</code> displayed one screen ago; etc.
	 */
	public void restorePreviousScreen(MFSPanel panel)
	{
		try
		{
			final MFSPanel source = panel.getSource();
			if (source == null)
			{
				displayMFSPanel(new MFSLogOnPanel(this));
			}
			else if (panel instanceof MFSDirectWorkPanel)
			{
				MFSDirectWorkPanel dirWrk = (MFSDirectWorkPanel) panel;
				int dwEndCode = dirWrk.getEndCode();
				final MFSConfig config = MFSConfig.getInstance();
				if (dwEndCode == 31 && config.getConfigValue("RETP").equals("USERAUTH")) //$NON-NLS-1$ //$NON-NLS-2$
				{
					displayMFSPanel(new MFSLogOnPanel(this));
				}
				else
				{
					MFSSelectWorkPanel selWrk = (MFSSelectWorkPanel) source;
					displayMFSPanel(selWrk);
					/* end_code of 30 means start work at next op. */
					if (dwEndCode == 30)
					{
						selWrk.str_wu("J"); //$NON-NLS-1$
					}
					else 
					{
						selWrk.repeatTransaction();
					}				
				}
			}
			else if (panel instanceof MFSViewOpsPanel)
			{
				if(source instanceof MFSSelWrkExtFnctPanel)
				{
					displayMFSPanel(source);
					((MFSSelWrkExtFnctPanel) source).reshowOps();
				}
				else
				{
					displayMFSPanel(source);
					((MFSDirWrkExtFnctPanel) source).reshowOps();
				}
			}
			else if (panel instanceof MFSDeconfigPanel)
			{
				if (MFSConfig.getInstance().getConfigValue("RETP").equals("USERAUTH")) //$NON-NLS-1$ //$NON-NLS-2$
				{
					displayMFSPanel(new MFSLogOnPanel(this));
				}
				else
				{
					displayMFSPanel(source);
					if(source instanceof MFSSelectWorkPanel)  //~02
					{
						MFSSelectWorkPanel selWrk = (MFSSelectWorkPanel) source;				
						selWrk.repeatTransaction();
					}
				}
			}
			else if (panel instanceof MFSPartFunctionDisplayPanel)
			{
				displayMFSPanel(source);
				if (source instanceof MFSDirectWorkPanel)
				{
					((MFSDirectWorkPanel) source).reshowPNSNErrorList();
				}
				else if (source instanceof MFSSelectWorkPanel)
				{
					((MFSSelectWorkPanel) source).reshowPNSNErrorList();
				}
			}
			//MFSAuthPanel to MFSSelectWorkPanel
			//MFSDirWrkExtFnctPanel to MFSDirectWorkPanel
			//MFSSelectWorkPanel to MFSLogOnPanel
			//MFSSelWrkExtFnctPanel to MFSSelectWorkPanel
			//MFSSerializedComponentFnctPanel to MFSSelectWorkPanel
			//MFSStandAlonePanel to MFSSelectWorkPanel
			//MFSViewInstPartsPanel to MFSDirectWorkPanel
			else
			{
				displayMFSPanel(source);
				if(source instanceof MFSSelectWorkPanel)  //~02
				{
					if (panel instanceof MFSEntityMergePanel)
					{
						MFSSelectWorkPanel selWrk = (MFSSelectWorkPanel) source;				
						selWrk.repeatTransaction();
					}																		
				}
			}
		}
		catch (Exception e)
		{
			IGSMessageBox.showOkMB(this, null, null, e);
		}
	}
}
