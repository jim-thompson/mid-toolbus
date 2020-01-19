package com.toolbus.magicdraw.importer;

import java.util.Map;
import org.jimthompson.magicdraw.util.log;
import com.toolbus.magicdraw.importer.BUSElementStruct;
import com.toolbus.magicdraw.importer.ReadRecordHandler;
import com.toolbus.magicdraw.importer.RecordHandlerIF;
import com.toolbus.riinc.RIGlobal;
import com.toolbus.riinc.RITBFile;
import com.toolbus.riinc.RITBRec;

public class BUSReader
{
	ModelDesc _model_desc;
		
	static
	{
		RIGlobal.pgmname = "BUSDumpApp";
		RIGlobal.pgmvers = "v0.0.1";
		RIGlobal.pgmtitle = "BUS file dumper.";
		RIGlobal.copyright = "Copyright (c) 2019 MID GmbH";
		RIGlobal.initPgm();
	}
	
	public BUSReader(ModelDesc model_description)
	{
		_model_desc = model_description;
	}
	
	public boolean readModel(String bus_file)
	{
		RecordHandlerIF the_handler = new ReadRecordHandler(_model_desc);
		
		_readModel(bus_file, the_handler);
		determineOwnership();
		
		return true;
	}
	
	void determineOwnership()
	{
		Map<String, BUSElementStruct> elements_dict = _model_desc._elements_dict;
		int seen_elements_count = 0;
		
		for (BUSElementStruct bus_element : elements_dict.values())
		{
			switch(bus_element._type)
			{
			case MODEL:
				// If this model is the first element seen, then it must be the root model, so
				// it has no owner.
				if (seen_elements_count == 0)
				{
					bus_element._owner = BUSElementStruct.NONE;
				}
				
			// NOTE: Fall through to PACKAGE case! After this, the handling for both types of
			// elements is identical. 
				
			case PACKAGE:
			case USECASE:
				for (String s : bus_element._relationship_names)
				{
					BUSElementStruct owned_bus_element = elements_dict.get(s);
					
					if (owned_bus_element == null)
					{
						log.error("*** ERROR: element <" + bus_element._id +">" +
								  " has relationship to unkown element: <" + s +">");
						break;
					}
					
					owned_bus_element._owner = bus_element;
				}
				break;
				
			case INCLUDE:
			case EXTEND:
				String owner_referenced = "";
				
				// Making a HUGE assumption here: that the owning use case will be the second
				// of two RELINFOs. 
				owner_referenced = bus_element._relationship_names.get(1);
				log.error("Element <" + bus_element._id + "> owner: <" + owner_referenced + ">");
				
				BUSElementStruct owning_use_case = elements_dict.get(owner_referenced);
				
				if (owning_use_case == null)
				{
					log.error("*** ERROR: element <" + bus_element._id +">" +
							  " has relationship to unkown element: <" + owner_referenced +">");
					break;
				}
				
				// The second use case is the owner of this include.
				bus_element._owner = owning_use_case;
//				bus_element._owner = BUSElementStruct.NONE;
				
				break;
				
			case EXTENSION_POINT:
			case ACTOR:
			case COMPONENT:
			case DEPENDENCY:
			case ASSOCIATION:
				break;
				
			default:
			}
		}
		
		// Now traverse the list a second time, reporting any bus element that doesn't seem
		// to have an owner.
		for (BUSElementStruct bus_element : elements_dict.values())
		{
			if (bus_element._owner == null)
			{
				log.error("*** ERROR: Could not determine owner of element <" +
						  bus_element._id + ">");
			}
			else if (bus_element._owner != BUSElementStruct.NONE)
			{
				bus_element._determined_owner = true;
			}
		}
	}
	
	public boolean _readModel(String bus_file, RecordHandlerIF handler)
	{
		// Open the file.
		RITBFile ritb_file = new RITBFile(bus_file, 'r');
		
		// Read the file's FIRST record and get a reference to it.
		ritb_file.readRec();	
		RITBRec ritb_rec = ritb_file.nextTb;
		
		// Set up the record handler. This is to enable tasks such as printing a header
		// at the top of the output.
		handler.setup();
		
		while (ritb_rec != null)
		{
			// Handle the record. This is for tasks such as printing each record.
			handler.handleRecord(ritb_rec);
			
			// Read the file's NEXT record and get a reference to it.
			ritb_file.readRec();	
			ritb_rec = ritb_file.nextTb;
		}
		
		// Tear down the record handler. This is for tasks such as printing a summary after
		// all the records have been printed, or for performing some post-processing task.
		handler.teardown();
		
		return true;
	}
}
