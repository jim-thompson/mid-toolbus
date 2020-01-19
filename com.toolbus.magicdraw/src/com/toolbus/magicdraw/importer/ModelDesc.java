package com.toolbus.magicdraw.importer;

import java.util.Map;
import com.toolbus.magicdraw.importer.BUSElementStruct;
import java.util.LinkedHashMap;

public class ModelDesc
{
	// NB: This MUST be a linked hash map because we want to be able to pull out bus elements
	// in the same order they were added to the map - which is the order they are encountered 
	// in the BUS file.
	Map<String, BUSElementStruct> _elements_dict = new LinkedHashMap<>();
}
