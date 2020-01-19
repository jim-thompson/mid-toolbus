/*
 * Copyright (c) 2002 NoMagic, Inc. All Rights Reserved.
 */

package com.toolbus.magicdraw.plugin;

import com.nomagic.magicdraw.actions.ActionsConfiguratorsManager;
import com.nomagic.magicdraw.actions.ActionsID;
import com.nomagic.magicdraw.core.Application;

public class Plugin extends com.nomagic.magicdraw.plugins.Plugin
{
	@Override
	public void init()
	{
		Application application = Application.getInstance();

		// This is handy for debugging when you're not sure whether the plugin is getting loaded by
		// MagicDraw. But because it's probably annoying for end users, we'll turn it off until it's
		// needed.

		// Pop up a window showing the version and date. This is for development purposes, and should
		// probably be removed before release.

		application.getGUILog().showMessage("*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*\n"
		 		  						  + "PLUGIN LOAD CONFIRMATION:\n"
		 		  						  + "TOOLBUS for MagicDraw plugin\n"
		 		  						  + "Prerelease v0.0.1 / 28 August 2019");

		final ActionsConfiguratorsManager manager = ActionsConfiguratorsManager.getInstance();

		// Adding action to import menu. Action will be added after "UML 2.1/2.5 XMI File" menu item
		ImportAction example_action = new ImportAction("IMPORT_TOOLBUS", "TOOLBUS File...", null, null);

		String after = ActionsID.IMPORT_XMI_CI;
//		String after = ActionsID.OPEN_PROJECT;

		MenuConfigurator ia_configurator = new MenuConfigurator(example_action, after);
		
		manager.addMainMenuConfigurator(ia_configurator);
	}

	@Override
	public boolean close()
	{
		return true;
	}

	@Override
	public boolean isSupported()
	{
		return true;
	}
}