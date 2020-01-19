package com.toolbus.magicdraw.importer;

import com.toolbus.magicdraw.importer.BUSReader;
import com.toolbus.magicdraw.importer.ModelWriter;

public class BUSModelImporter
{
	private ModelDesc 	_model_desc = new ModelDesc();
	
	private BUSReader	_bus_reader = new BUSReader(_model_desc);
	private ModelWriter _model_writer = new ModelWriter(_model_desc);

	public void importBUSModel(String bus_file_path)
	{
		_bus_reader.readModel(bus_file_path);
	}
	
	public void createMDModel()
	{
		_model_writer.createMagicDrawModel();
	}
	
	public boolean writeMDModel(String md_file_path)
	{
		boolean result = _model_writer.writeModel(md_file_path);
		
		return result;
	}
}
