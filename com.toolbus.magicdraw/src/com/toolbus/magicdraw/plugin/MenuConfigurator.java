/*
 * Copyright (c) 2019 MID GmbH. All Rights Reserved.
 */

package com.toolbus.magicdraw.plugin;

import com.nomagic.actions.AMConfigurator;
import com.nomagic.actions.ActionsCategory;
import com.nomagic.actions.ActionsManager;
import com.nomagic.actions.NMAction;
import com.nomagic.magicdraw.actions.MDAction;

import java.util.List;

class MenuConfigurator implements AMConfigurator
{
	private final MDAction _action;
	private final String   _prev_id;

	MenuConfigurator(MDAction action, String prev_id)
	{
		_action = action;
		_prev_id = prev_id;
	}

	@Override
	public void configure(ActionsManager manager)
	{
		final NMAction found = manager.getActionFor(_prev_id);
		
		if (found != null)
		{
			final ActionsCategory category = (ActionsCategory) manager.getActionParent(found);
			if (category != null)
			{
				// Get the menu category's actions...
				List<NMAction> actions = category.getActions();
				
				// ...and get the index of the action to insert aftar
				int index = actions.indexOf(found);
				
				// Insert our action after the found action
				actions.add(index + 1, _action);

				// And give the list back to the menu
				category.setActions(actions);
			}
		}
	}

	@Override
	public int getPriority()
	{
		return AMConfigurator.MEDIUM_PRIORITY;
	}

}
