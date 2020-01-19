package com.toolbus.testbench.db;

public class DBInit
{
	private static boolean _populate_with_dummy_element = false;
	
	public static void main(String[] args)
	{
		try
		{
			ElementDB db = new ElementDB("test-bench.db");
			
			db.initializeDatabase();
			
			if (_populate_with_dummy_element)
			{
				DBElement element = new DBElement("test-id", "test-name", "test-type", "test-stereotype");
				db.insertRecord(element);
			}
			
			System.out.println("Created new database using driver '" + db._driver_version + "");
		}
		catch (Exception e)
		{
			System.err.println("*** ERROR ***");
			System.err.println(e.getMessage());
		}
	}
}
