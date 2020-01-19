// Copyright © 2019 MID GmbH 
// smartfacts support for MagicDraw and Cameo
// Author: James Thompson <jim.thompson@pobox.com>

package com.toolbus.testbench.db;

import java.io.File;
import com.nomagic.magicdraw.commandline.CommandLine;
import com.nomagic.magicdraw.core.Project;
import com.nomagic.magicdraw.core.ProjectUtilities;
import com.nomagic.magicdraw.uml.symbols.DiagramPresentationElement;
import com.nomagic.magicdraw.uml.symbols.PresentationElement;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;
import org.jimthompson.magicdraw.options.FileOpts;
import org.jimthompson.magicdraw.options.ProjectInitializer;
import org.jimthompson.magicdraw.util.ElementFormatter;
import org.jimthompson.magicdraw.util.log;

/**
 * Entry point to command-line version of MagicDraw to smartfacts transporter app. Entry points: 
 * parseArgs() and execute().
 */

public class _ModelDumpApp extends CommandLine
{
	private String 	_md_file;
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
		majorPhase("Database Dump");
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
			dumpModel();
			
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
	
	private void dumpModel()
	{
		Element model = _project.getPrimaryModel();
		dumpElement(model);
		
		recursivelyDumpElements(model);
		
		dumpDiagrams();
	}
	
	private void dumpDiagrams()
	{
		for (DiagramPresentationElement diagram_pe : _project.getDiagrams())
		{
			for (Element element : diagram_pe.getUsedModelElements(false))
			{
				PresentationElement pe_element = diagram_pe.findPresentationElement(element, null);
				
				if (pe_element != null)
				{
					log.info("Element <" + element.getID() + "> " +
							 "*** PE Element <" + pe_element.getClass().getName() + ">");				
				}
			}
		}
	}
	
	private boolean include_attached_elements = false;
	
	private void recursivelyDumpElements(Element parent)
	{
		log.incrementDepth();
		for (Element element : parent.getOwnedElement())
		{
			if (include_attached_elements || !ProjectUtilities.isElementInAttachedProject(element))
			{
				dumpElement(element);
				recursivelyDumpElements(element);
			}
		}
		log.decrementDepth();
	}
	
	private static ElementFormatter formatter = new ElementFormatter();
	
	private void dumpElement(Element element)
	{
//		log.info("ID: <" + element.getID() + "> Human Name: <" + element.getHumanName() +">");
		formatter.showElement(element);
//		ElementDumper.dumpElement(element);
	}
	
	void evaluateOptions() throws Exception
	{
		FileOptConstants values = new FileOptConstants();
		FileOpts 		 file_opts = new FileOpts(values);
		
		file_opts.debugHook();
		_md_file = file_opts.getMDOpt();
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
