package com.toolbus.testbench.db;

import java.util.List;

public class _DBDumpApp
{
	public static void main(String[] args)
	{
		try
		{
			ElementDB db = new ElementDB("elements.db");
			db.connectDatabase();
			
			System.out.println("Connected database using driver '" + db._driver_version + "");

			List<DBElement> elements = db.retrieveAllElements();
			
			for (DBElement element : elements)
			{
				System.out.println(element._id + " | " +
								   element._name + " | " +
								   element._type + " | " +
								   element._stereotype);
						
			}
		}
		catch (Exception e)
		{
			System.err.println("*** ERROR ***");
			System.err.println(e.getMessage());
		}
	}
}
