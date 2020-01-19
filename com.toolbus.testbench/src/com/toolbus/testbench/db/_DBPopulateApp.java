// Copyright © 2019 MID GmbH 
// smartfacts support for MagicDraw and Cameo
// Author: James Thompson <jim.thompson@pobox.com>

package com.toolbus.testbench.db;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import com.nomagic.magicdraw.commandline.CommandLine;
import com.nomagic.magicdraw.core.Project;
import com.nomagic.magicdraw.core.ProjectUtilities;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;
import org.jimthompson.magicdraw.util.ElementFormatter;
import org.jimthompson.magicdraw.util.log;
import org.jimthompson.magicdraw.options.FileOpts;
import org.jimthompson.magicdraw.options.ProjectInitializer;

/**
 * Entry point to command-line version of MagicDraw to smartfacts transporter app. Entry points: 
 * parseArgs() and execute().
 */

public class _DBPopulateApp extends CommandLine
{
	private String 	_md_file;
	private String	_db_file;
	
	private Project	_project = null;

	/**
	 * <b><i>Entry Point:</b></i><br/>
	 * Called by MagicDraw to handle the command-line arguments. Pass the arguments
	 * to the ArgumentsHandler to extract our specific arguments. After this, MagicDraw
	 * will process any remaining arguments that it recognizes.
	 * @param args Specifies the list of command-line arguments
	 */
	protected void parseArgs(String[] args) throws Exception
	{
		// This (not execute()) is the first entry point to the command-line plugin.
		majorPhase("Database Populator");
	}

	/**
	 * <b><i>Entry Point:</b></i><br/>
	 * This function is the main entry point to the command-line plugin; it is called by MagicDraw
	 * after the plugin is loaded.
	 *
	 * @return zero on success, non-zero otherwise.
	 */
	protected byte execute()
	{
		try
		{
			// Evaluate the arguments. (For example, is an existing file specified?)
			evaluateOptions();
			
			// Load the project
			initProject();
			
			// Populate the database
			populateDB();
			
			System.out.println("*** We're done! ***");
		}
		catch (Exception exception)
		{
			// Oops - something wrong happened. Report the error and return an error code.
			// In later versions of this program, we might attempt to do more than just
			// report the error - for example, we might be able to deduce what went wrong
			// and give our users a more informative report to help them diagnose the
			// problem.
			log.error("Caught exception: " + exception);
			exception.printStackTrace(System.err);
			
			return -1;
		}

		return 0;
	}
	
	void initProject() throws Exception
	{
		File _project_file = new File(_md_file); 
		ProjectInitializer project_initializer = new ProjectInitializer(_project_file);
		_project = project_initializer.getProject();

		// ...and check that it's been properly initialized.
		if (_project == null)
		{
			throw new Exception("No model defined.");
		}

		log.info("*** Project loaded successfully.");		
	}
	
	void populateDB() throws Exception
	{
		try
		{
			ElementDB db = new ElementDB(_db_file);

//			db.connectDatabase();
			db.initializeDatabase();
			
			List<Element> elements = getElementList();
			
			for (Element element : elements)
			{
				String id = element.getID();
				String name = ElementFormatter.shortElementName(element);
				String type = element.getHumanType();
				String stereotype = "";
				
				log.info("Element <" + name + "> of type <" + element.getHumanType() +">");
				
				DBElement db_element = new DBElement(id, name, type, stereotype);
				
				db.insertRecord(db_element);
			}
		}
		catch(Exception e)
		{
			log.error("*** Error ***");
			log.error(e.getMessage());
		}
	}
	
	private List<Element> getElementList()
	{
		List<Element> elements = new ArrayList<>();
		
		Element model = _project.getPrimaryModel();
		elements.add(model);
		
		recursivelyGatherElements(model, elements);
		
		return elements;
	}
	
	private void recursivelyGatherElements(Element parent, List<Element> elements)
	{
		for (Element element : parent.getOwnedElement())
		{
			if (!ProjectUtilities.isElementInAttachedProject(element))
			{
				elements.add(element);
				recursivelyGatherElements(element, elements);
			}
		}
	}
	
	void evaluateOptions() throws Exception
	{
		FileOptConstants values = new FileOptConstants();
		FileOpts 		 file_opts = new FileOpts(values);
		
		// Call a function for use in development, testing, and debugging. This function
		// will let us easily override the program arguments without having to futz
		// around with the command-line arguments in the launch definition.
		file_opts.debugHook();

		_md_file = file_opts.getMDOpt();
		_db_file = file_opts.getDBOpt();
	}

	void majorPhase(String message)
	{
		log.info(".\n=============================================================");
		log.info(message);
		log.info();
	}
}

// Local Variables:
// tab-width: 4
// fill-column: 102
// End:
