/*
 * Copyright (c) 2002 NoMagic, Inc. All Rights Reserved.
 */

package com.toolbus.magicdraw.plugin;

import java.awt.event.ActionEvent;
import java.io.File;
import javax.annotation.CheckForNull;
import javax.swing.KeyStroke;
import com.nomagic.magicdraw.actions.MDAction;
import com.toolbus.magicdraw.importer.BUSModelImporter;

class ImportAction extends MDAction
{
	private static final long serialVersionUID = 1103172738307676916L;

	ImportAction(String id, String name, @CheckForNull KeyStroke stroke, @CheckForNull String group)
	{
		super(id, name, stroke, group);
	}

	/**
	 * Method is called when action should be performed. Showing simple message.
	 *
	 * @param e event causes action call.
	 */
	@Override
	public void actionPerformed(ActionEvent e)
	{
		File bus_file = BUSChooser.chooseFile();
		
		if (bus_file != null)
		{
			BUSModelImporter model_importer = new BUSModelImporter();
			
			model_importer.importBUSModel(bus_file.getAbsolutePath());
			model_importer.createMDModel();
		}
	}
}
