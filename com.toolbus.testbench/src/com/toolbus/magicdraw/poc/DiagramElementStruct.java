package com.toolbus.magicdraw.poc;

import com.nomagic.magicdraw.uml.symbols.DiagramPresentationElement;
import com.toolbus.magicdraw.importer.BUSElementStruct;

public class DiagramElementStruct
	extends BUSElementStruct 
{
	DiagramElementStruct(String id) 
	{
		super(id); 
	}
	
	DiagramPresentationElement _diagram_presentation_element;
}
