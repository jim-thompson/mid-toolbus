package com.toolbus.testbench.modexplore;

import org.jimthompson.magicdraw.options.FileOpts;
import com.toolbus.magicdraw.importer.RITBRecUtil;
import com.toolbus.riinc.RIGlobal;
import com.toolbus.riinc.RITBFile;
import com.toolbus.testbench.db.FileOptConstants;

public class BUSDumpApp
{
	String				_bus_file = null;
	
	static
	{
		RIGlobal.pgmname = "BUSDumpApp";
		RIGlobal.pgmvers = "v0.0.1";
		RIGlobal.pgmtitle = "BUS file dumper.";
		RIGlobal.copyright = "Copyright (c) 2019 MID GmbH";
		RIGlobal.initPgm();
	}
	
	public static void main(String argv[])
	{
		BUSDumpApp dumper = new BUSDumpApp();
		
		dumper.execute();
	}
	
	private void execute()
	{
		try
		{
			evaluateOptions();
			doDump();
		}
		catch (Exception e)
		{
			// Oops - something wrong happened. Report the error and return an error code.
			// In later versions of this program, we might attempt to do more than just
			// report the error - for example, we might be able to deduce what went wrong
			// and give our users a more informative report to help them diagnose the
			// problem.
			System.err.println("*** ERROR ***");
			System.err.println(e.getMessage());
			e.printStackTrace(System.err);
		}
	}
	
	private void doDump()
	{
		RIGlobal.printStartMsg("<stdout>");
		RITBFile ritb_file = new RITBFile(_bus_file, 'r');
		
		ritb_file.readRec();	
		String prev_id = ritb_file.nextTb.entity1;
		
		String h = RITBRecUtil.putFieldsCompactHdr();
		System.out.println(h);
		
		String hc = RITBRecUtil.putFieldsCompactHdrCont();
		System.out.println(hc);
		
		System.out.println();
		
		while (ritb_file.nextTb != null)
		{
			String s = RITBRecUtil.putFieldsCompact(ritb_file.nextTb);
			
			if (prev_id.compareTo(ritb_file.nextTb.entity1) != 0)
			{
				System.out.println();
			}
			
			System.out.println(s);
			
			prev_id = ritb_file.nextTb.entity1;
			
			ritb_file.readRec();			
		}
		
		System.out.println();
		System.out.println("*** end ***");
	}


	void evaluateOptions() throws Exception
	{
		FileOptConstants values = new FileOptConstants();
		FileOpts 		 file_opts = new FileOpts(values);
		
		file_opts.debugHook();
		_bus_file = file_opts.getBUSOpt();
	}
}
