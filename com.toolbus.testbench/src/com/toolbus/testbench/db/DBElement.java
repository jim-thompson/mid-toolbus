package com.toolbus.testbench.db;

public class DBElement
{
	DBElement() { }
	DBElement(String id, String name, String type, String stereotype)
	{
		_id = id;
		_name = name;
		_type = type;
		_stereotype = stereotype;
	}
	
	public String	_id = "";
	public String	_name = "";
	public String	_type = "";
	public String	_stereotype = "";
}