package com.toolbus.magicdraw.importer;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import org.jimthompson.magicdraw.util.log;
import com.toolbus.magicdraw.importer.UMLConst;
import com.toolbus.magicdraw.importer.UMLConst.UMLType;

public class BUSElementStruct
{
	public String 	_id;
	public String	_name;
//	public String 	_comment;
	public String 	_guid;
	public String 	_type_name;
	public String	_new_element_id;
	
	public int		_width;
	public int		_height;
	public int		_xkoord;
	public int		_ykoord;
	public int		_x0;
	public int		_y0;
	public int		_x1;
	public int		_y1;
	
	public List<String> _relationship_names = new ArrayList<>();
	
	public UMLConst.UMLType _type = UMLType.UNKNOWN;
	
	public boolean _determined_owner = false;
	public boolean _added_to_model = false;
	public BUSElementStruct _owner = null;
	
	public BUSElementStruct(String id)
	{
		_id = id;
	}
	
	public void dump(Map<String, BUSElementStruct> _bus_id_to_bus_element_map)
	{
		log.info("ID: <" + _id + ">");
		log.info("Name: <" + _name + ">");
		log.info("GUID: <" + _guid + ">");
		log.info("Type: <" + _type_name + ">");
		log.info("Element ID in new model: <" + _new_element_id + ">");
		log.info("Added to model? " + _added_to_model);

		log.incrementDepth();
		for (String s : _relationship_names)
		{
			BUSElementStruct bus_element = _bus_id_to_bus_element_map.get(s);
			
			String type_name = (bus_element != null) ? bus_element._type_name : "*NULL*";
			log.info("-> " + s + " (" + type_name + ")");
		}
		log.decrementDepth();
	}
	
	static BUSElementStruct NONE;
	
	static
	{
		NONE = new BUSElementStruct("does-not-exist");
		NONE._id = "does-not-exist";
		NONE._type = UMLType.UNKNOWN;
	}
}
