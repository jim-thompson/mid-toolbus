package org.jimthompson.magicdraw.options;

import java.io.File;
import java.util.Properties;
import org.jimthompson.magicdraw.util.log;

public class FileOpts
{
	public final static String	DIR_LONG = "dir";
	public final static String	BUSFILE_LONG = "bus";
	public final static String	DBFILE_LONG = "db";
	public final static String MDFILE_LONG = "mdzip";

	Properties		_options = new Properties();
	
	public FileOpts(FileOptValuesProvider values)
	{
		String filenames[] = values.fileNames();
		String bases[] = values.directories();
		int model_to_load = values.fileNumber();
		
		_test_models = filenames;
		_mac_test_base = bases[0];
		_win_test_base = bases[1];
		_lin_test_base = bases[2];
		_model_to_load = model_to_load;
	}

	public String getBUSOpt() { return evaluateOption(BUSFILE_LONG); }
	public String getDBOpt()  { return evaluateOption(DBFILE_LONG); }
	public String getMDOpt()  { return evaluateOption(MDFILE_LONG); }

	private String evaluateOption(String key)
	{
		String dir = _options.getProperty(DIR_LONG);
		String file = _options.getProperty(key);

		File _file = new File(dir, file);
		String path = _file.getAbsolutePath();

		return path;
	}

	// The following is some debug code used during development (jct). It sets the model to load to
	// make changing models easy without having to edit the launch settings in Eclipse.

	private static String _mac_test_base;
	private static String _win_test_base;
	private static String _lin_test_base;

	private static int _model_to_load;

	private static String _test_models[];

	/**
	 * Set up the app for debug. Mostly this function just build the path to the project to be loaded by
	 * detecting the operating system and prepending the OS-specific path onto the name of the project
	 * as specified by model_to_load.
	 */

	public void debugHook() throws Exception
	{
		String test_base = null;

		// Detect the operating system
		String os = System.getProperty("os.name").toLowerCase();

		// Based on the operiting system, choose the path to the test resources directory.
		if (os.indexOf("mac") >= 0)
		{
			test_base = _mac_test_base;
		}
		else if (os.indexOf("win") >= 0)
		{
			test_base = _win_test_base;
		}
		else if (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0)
		{
			test_base = _lin_test_base;
		}

		if (test_base == null)
		{
			log.error("*** ERROR: Unable to determing base for test resources. "
					+ "Aborting.\n");

			throw new Exception("ABORT");
		}
		
		String bus_file = _test_models[_model_to_load] + ".BUS";
		String db_file = _test_models[_model_to_load] + ".db";
		String md_file = _test_models[_model_to_load] + ".mdzip";

		_options.setProperty(DIR_LONG, test_base);
		_options.setProperty(BUSFILE_LONG, bus_file);
		_options.setProperty(DBFILE_LONG, db_file);
		_options.setProperty(MDFILE_LONG, md_file);
	}
}
