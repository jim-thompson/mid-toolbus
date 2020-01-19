package com.toolbus.testbench.modexplore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jimthompson.magicdraw.options.FileOpts;
import com.toolbus.magicdraw.importer.RITBRecUtil;
import com.toolbus.riinc.RIGlobal;
import com.toolbus.riinc.RITBFile;
import com.toolbus.riinc.RITBRec;
import com.toolbus.testbench.db.DBElement;
import com.toolbus.testbench.db.ElementDB;
import com.toolbus.testbench.db.FileOptConstants;

public class BUSModelDumpApp
{
	private ElementDB 	_element_db = null;
	
	String				_bus_file = null;
	String				_db_file = null;
	
	static
	{
		RIGlobal.pgmname = "BUSModelDumpApp";
		RIGlobal.pgmvers = "v0.0.1";
		RIGlobal.pgmtitle = "BUS Model Dumper.";
		RIGlobal.copyright = "Copyright (c) 2019 MID GmbH";
		RIGlobal.initPgm();
	}
	
	public static void main(String argv[])
	{
		BUSModelDumpApp dumper = new BUSModelDumpApp();
		
		dumper.execute();
	}
	
	private void execute()
	{
		try
		{
			evaluateOptions();

			_element_db = new ElementDB(_db_file);
			
			loadElementDB();
			buildElementMap();
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
	
	Map<String, String> _element_id_map = new HashMap<String, String>();
	List<DBElement> 	_db_elements = null;
	
	private void buildElementMap()
	{
		for (DBElement db_element : _db_elements)
		{
			String pseudo_id = pseudoID(db_element);
			
			_element_id_map.put(db_element._id, pseudo_id);
		}
	}
	
	private final static String _id_pad = "                                        ";
	private final static int ID_SIZE = 32;
	
	private String trailingNChars(String s, int n)
	{
		int str_len = s.length();
		
		if (str_len > n)
		{
			s = s.substring(str_len - n, str_len);
		}
		
		return s;
	}
	
	private String pseudoID(DBElement db_element)
	{
		// First handle the element's ID.
		String id_tail = trailingNChars(db_element._id, 9);
		
		String pseudo_id_big = id_tail + "-" + db_element._name + "-" + db_element._type + _id_pad;
		String pseudo_id = pseudo_id_big.substring(0, ID_SIZE);
		
		return pseudo_id;
	}

	private void loadElementDB() throws Exception
	{
		_element_db.connectDatabase();

		_db_elements = _element_db.retrieveAllElements();
		
		// Very important: because the BUS files use only 32-character entity IDs, we must 
		// traverse the database elements and shorten each id so it becomes comparable
		// to the BUS entity IDs.
		List<DBElement> tmp_elements = new ArrayList<>();
		
		for (DBElement db_element : _db_elements)
		{
			db_element._id = trailingNChars(db_element._id, 32);
			tmp_elements.add(db_element);
		}
		
		_db_elements = tmp_elements;
	}
	
	private String pseudoIDFor(String id)
	{
		String pseudo_id = _element_id_map.get(id);
		return pseudo_id != null ? pseudo_id : id;
	}
	
	private void substituteIDs(RITBRec record)
	{
		record.entity1 = pseudoIDFor(record.entity1);
		record.entity2 = pseudoIDFor(record.entity2);
	}
	
	private RITBRec readRecord(RITBFile ritb_file)
	{
		ritb_file.readRec();
		
		if (ritb_file.nextTb != null)
		{
			substituteIDs(ritb_file.nextTb);
		}
		
		return ritb_file.nextTb;
	}

	private void doDump() throws IOException
	{
		RIGlobal.printStartMsg("<stdout>");
		RITBFile ritb_file = new RITBFile(_bus_file, 'r');

		RITBRec ritb_rec = readRecord(ritb_file);		
		String prev_id = "nada";
		
		String h = RITBRecUtil.putFieldsCompactHdr();
		System.out.println(h);
		
		String hc = RITBRecUtil.putFieldsCompactHdrCont();
		System.out.println(hc);
				
		while (ritb_rec != null)
		{
			String s = RITBRecUtil.putFieldsCompact(ritb_rec);

			if (prev_id.compareTo(ritb_rec.entity1) != 0)
			{
				System.out.println();
			}

			System.out.println(s);

			prev_id = ritb_rec.entity1;

			ritb_rec = readRecord(ritb_file);			
		}

		System.out.println("*** end ***");
	}

	void evaluateOptions() throws Exception
	{
		FileOptConstants values = new FileOptConstants();
		FileOpts 		 file_opts = new FileOpts(values);
		
		file_opts.debugHook();
		_bus_file = file_opts.getBUSOpt();
		_db_file = file_opts.getDBOpt();
	}
}
