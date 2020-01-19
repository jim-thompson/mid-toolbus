// Copyright (c) 2019 MID GmbH. All Rights Reserved.

package com.toolbus.magicdraw.poc;

import java.io.File;
import org.jimthompson.magicdraw.options.FileOpts;
import org.jimthompson.magicdraw.util.log;
import com.nomagic.magicdraw.commandline.CommandLine;
import com.toolbus.magicdraw.importer.BUSModelImporter;
import com.toolbus.testbench.db.FileOptConstants;

public class _ModelCreateApp extends CommandLine
{
	private BUSModelImporter model_importer = new BUSModelImporter();
	
	private boolean		_developer_mode = true;
	
	private String		_md_file;
	private String		_bus_file;
	
	@Override
	protected byte execute()
	{
		try
		{
			log.info("Executing project creator PoC...");
			log.info("*** Begin ***");
			
			evaluateOptions();

			doBUSStuff();
			doModelStuff();
			
			log.info("");
			log.info("*** Done ***");
		}
		catch (Exception e)
		{
			log.error("*** ABEND ***");
			e.printStackTrace();
		}
		
		return 0;
	}
	
	void doBUSStuff()
	{
		// Read the model as described by the BUS file into memory.
		model_importer.importBUSModel(_bus_file);
	}
	
	void doModelStuff()
	{
		// Create the MagicDraw model. This creates the model in MagicDraw memory using
		// the OpenAPI.
		model_importer.createMDModel();

		// Now write the model out to a MagicDraw mdzip file.
		boolean did_save = model_importer.writeMDModel(_md_file);

		// Report any errors.
		if (!did_save)
		{
			log.error("*** ERROR: Could not save project!");
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
		_bus_file = file_opts.getBUSOpt();
		
		if (_developer_mode)
		{
			// For the time being, we're going to override the model output file.
			_md_file = "/Users/jct/Desktop/PoC.mdzip";

			// Also, and just for the time being, delete the output file if a copy already exists.
			File md_file = new File(_md_file);
			md_file.delete();
		}
		
		// In this program we will be writing to the file specified as the model file; we want to ensure
		// that we do not overwrite an existing file, so we check whether a file with the specified name
		// already exists. If it does, it's an error, and we throw an exception.
		File model_file = new File(_md_file);
		if (model_file.exists())
		{
			throw new Exception("*** ERROR: file exists:" + _md_file);
		}
	}
}
