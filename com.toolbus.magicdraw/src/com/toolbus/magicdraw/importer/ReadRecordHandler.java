package com.toolbus.magicdraw.importer;

import java.util.Map;
import org.jimthompson.magicdraw.util.log;
import com.toolbus.riinc.RITBRec;

public class ReadRecordHandler implements RecordHandlerIF
{
	private ModelDesc _model_description;
	
	// These will be REFERENCES to the maps in the model description, for easier coding.
	Map<String, BUSElementStruct> _elements_dict;
	
	private PrintRecordHandler 	 _print_handler = new PrintRecordHandler();
	
	ReadRecordHandler(ModelDesc model_description)
	{
		// Save the model description.
		_model_description = model_description;
		
		// Grab references to the maps in the model description.
		_elements_dict = _model_description._elements_dict;
	}
	
	@Override
	public void setup()
	{
		_print_handler.setup();
	}
	
	@Override
	public void handleRecord(RITBRec ritb_rec)
	{
		_print_handler.handleRecord(ritb_rec);
		
		try
		{
			switch(ritb_rec.modus)
			{
			// Add Element
			case "AE":
				handleAddElement(ritb_rec);
				break;

				// Add Relationship
			case "AR":
				handleAddRelationship(ritb_rec);
				break;

				// Add Attribute
			case "AA":
				handleAddAttribute(ritb_rec);
				break;

				// Change Attribute
			case "CA":
				handleChangeAttribute(ritb_rec);
				break;

				// Delete Element
			case "DE":
				handleDeleteElement(ritb_rec);
				break;

				// Delete Relationship
			case "DR":
				handleDeleteRelationship(ritb_rec);
				break;

				// Delete Attribute
			case "DA":
				handleDeleteAttribute(ritb_rec);
				break;
			}
		}
		catch (Exception e)
		{
			log.error(e.getMessage());
		}
	}
	
	private void handleAddElement(RITBRec ritb_rec) throws Exception
	{
		// Get the ID of the element to be added
		String id = ritb_rec.entity1;
		
		// Check the dictionary to see whether a record with this ID has already been added.
		BUSElementStruct element_struct = _elements_dict.get(id);
		
		// If so, it's an error.
		if (element_struct != null)
		{
			throw new Exception("*** ERROR: AE record: element already exists: <" + id + ">");
		}
		
		// Create and add a new, partial element to the dictionary.
		element_struct = new BUSElementStruct(id);
		_elements_dict.put(id, element_struct);
	}
	
	private BUSElementStruct getStructMustExist(String id) throws Exception
	{
		// The element structure should already exist; get it from the dictionary.
		BUSElementStruct element_struct = _elements_dict.get(id);
		
		// If no such element exists, report the error and fail out.
		if (element_struct == null)
		{
			throw new Exception("*** ERROR: AR record: element doesn't exist: <" + id + ">");
		}
		
		return element_struct;
	}
	
	private void handleAddRelationship(RITBRec ritb_rec) throws Exception
	{
		// We only know how to handle records with RELINFO aname; report others
		String aname = ritb_rec.aname;
		if ("RELINFO".compareTo(aname) != 0)
		{
			log.error("*** ERROR: AR record: cannot handle aname type: <" + aname + ">");
			return;
		}
		
		// Get the ID of the element being altered
		String id = ritb_rec.entity1;
		
		// Get the BUS element's structure out of the map.
		BUSElementStruct element_struct = getStructMustExist(id);
		
		if (element_struct == null)
		{
			throw new Exception("*** ERROR: referring element doesn't exist: <" + id + ">");
		}
		
		// Get the name of the referred-to element...
		String ref_id = ritb_rec.entity2;
		
		// ...and add it to the array of elements referred to from this one:
		element_struct._relationship_names.add(ref_id);
	}
	
	private static boolean _first_aa_record_seen = false;
	
	private void handleAddAttribute(RITBRec ritb_rec)
	{
		// The first time a AA record is seen, print a message.
		if (!_first_aa_record_seen)
		{
			log.error("*** Cannot handle Add Attribute records.");
			_first_aa_record_seen = true;
		}
	}
	
	private void handleChangeAttribute(RITBRec ritb_rec) throws Exception, Error
	{
		// Get the ID of the element being altered
		String id = ritb_rec.entity1;
		
		// Ensure that the structure exists in the elements dictionary. Note that we don't do
		// anything with the structure here, just make sure it exists.
		BUSElementStruct element_struct = getStructMustExist(id);

		// Handle the specific type of atribute being changed.
		String opcode = ritb_rec.aname;
		switch(opcode)
		{
		case "CASENAME":
			// This changes the element's name.
			element_struct._name = ritb_rec.awert;
			break;
			
		case "DEFINIT":
			break;
			
		case "EXPCNTL":
			break;
			
		case "FEXPCNTL":
			break;
			
		case "GUID":
			element_struct._guid = ritb_rec.awert;
			break;
			
		case "#SOURCE":
			// Right now, we can only handle BUS files generated from MagicDraw models; anything else
			// is considered a fatal error. Later this restriction will be removed.
			if ("MAGICDRAW".compareTo(ritb_rec.awert) != 0)
			{
				throw new Error("*** FATAL ERROR: Cannot handle non-MagicDraw inputs ***");
			}
			break;
			
		case "STEREO":
			break;
			
		case "TEXPCNTL":
			break;
			
		case "UDP":
			String operand = ritb_rec.awert;
			String vaname = ritb_rec.vaname;
			
			if ("002".compareTo(vaname) != 0)
			{
				return;
			}
			
			UMLConst.UMLType type = UMLConst.tnameToType(operand);
			element_struct._type = type;
			element_struct._type_name = operand;
			
			if (type == UMLConst.UMLType.UNKNOWN)
			{
				throw new Exception("*** ERROR: specified type unknown: <" + operand + ">");
			}
						
			break;
			
		// Group the L-types together: 
		case "LLBHIDE":
		case "LLBNAVIG":
		case "LLTHIDE":
		case "LMTHIDE":
		case "LRBHIDE":
		case "LRBNAVIG":
		case "LRTHIDE":
			break;
			
		// Group the geometry attribute types together:
		case "POINTS":
			break;
			
		case "HEIGHT":
			element_struct._height = Integer.parseInt(ritb_rec.awert);
			break;
			
		case "WIDTH":
			element_struct._width = Integer.parseInt(ritb_rec.awert);
			break;
			
		case "XKOORD":
			element_struct._xkoord = Integer.parseInt(ritb_rec.awert);
			break;
			
		case "YKOORD":
			element_struct._ykoord = Integer.parseInt(ritb_rec.awert);
			break;
			
		default:
			throw new Exception("*** ERROR: Cannot handle Change Attribute <" + opcode +">");
		}
	}
	
	static boolean _first_de_record_seen = false;
	
	private void handleDeleteElement(RITBRec ritb_rec)
	{		
		// The first time a DE record is seen, print a message.
		if (!_first_de_record_seen)
		{
			log.error("*** Cannot handle Delete Element records.");
			_first_de_record_seen = true;
		}
	}
	
	static boolean _first_dr_record_seen = false;
	
	private void handleDeleteRelationship(RITBRec ritb_rec)
	{
		// The first time a DR record is seen, print a message.
		if (!_first_dr_record_seen)
		{
			log.error("*** Cannot handle Delete Relationship records.");
			_first_dr_record_seen = true;
		}
	}
	
	static boolean _first_da_record_seen = false;
	
	private void handleDeleteAttribute(RITBRec ritb_rec)
	{
		// The first time a DA record is seen, print a message.
		if (!_first_da_record_seen)
		{
			log.error("*** Cannot handle Delete Attribute records.");
			_first_da_record_seen = true;
		}
	}

	@Override
	public void teardown()
	{
		// Now we print stuff
		_print_handler.teardown();
	}
}
