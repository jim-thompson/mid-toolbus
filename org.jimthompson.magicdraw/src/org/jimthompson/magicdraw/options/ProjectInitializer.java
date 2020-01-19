// Copyright © 2019 MID GmbH 
// smartfacts support for MagicDraw and Cameo
// Author: James Thompson <jim.thompson@pobox.com>

package org.jimthompson.magicdraw.options;

import java.io.File;

import com.nomagic.magicdraw.core.Application;
import com.nomagic.magicdraw.core.Project;
import com.nomagic.magicdraw.core.project.ProjectDescriptor;
import com.nomagic.magicdraw.core.project.ProjectDescriptorsFactory;
import com.nomagic.magicdraw.core.project.ProjectsManager;
import org.jimthompson.magicdraw.util.log;

public class ProjectInitializer
{
	// Private fields
	private Project				mi_project;
	
	// Project getter
	public Project getProject() { return mi_project; }
	
	// Public constructor
	public ProjectInitializer(File project_file)
	{
		// Create a file and project descriptor from which the project can be loaded
		//File project_file = new File(args_handler.ah_project_file_name);
		final ProjectDescriptor projectDescriptor = 
				ProjectDescriptorsFactory.createProjectDescriptor(project_file.toURI());
		
		// If the file descriptor couldn't be created (for example, if the file doesn't exist,
		// thow an error.
		if (projectDescriptor == null)
		{
			log.info("Project descriptor was not created for " + project_file.getAbsolutePath());
		}
		
		// Go through the steps to load the project.
		ProjectsManager projects_manager = Application.getInstance().getProjectsManager();
		projects_manager.loadProject(projectDescriptor, true);
		mi_project = projects_manager.getActiveProject();
		
		// If the project couldn't be loaded (doesn't exist, or is corrupt),
		// Then throw an error
		if (mi_project == null)
		{
			log.info("Project " + project_file.getAbsolutePath() + " was not loaded.");
		}
	}
}

// Local Variables:
// tab-width: 4
// fill-column: 108
// End:
