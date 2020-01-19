package org.jimthompson.sandbox.sqlite;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Dump
{
	public static void connectToDatabase(String filename)
	{
		try
		{
			Connection c = connectDatabase(filename);
			dumpDatabase(c, "");
		}
		catch (SQLException e)
		{
			System.err.println("*** ERROR ***");
			System.err.println(e.getMessage());
		}
	}
	
    public static void dumpDatabase(Connection connection, String condition)
    {
        String sql = "SELECT id, name, capacity FROM warehouses " + condition;
        
        try
        {
        	System.out.println("ID\t| Name\t| Capacity");
        	Statement statement  = connection.createStatement();
        	        	
            ResultSet resultset = statement.executeQuery("SELECT COUNT() from warehouses " + condition);
            resultset.next();
            int count = resultset.getInt(1);

        	System.out.println("Result set size = " + count);

        	resultset = statement.executeQuery(sql);
        	// loop through the result set
        	while (resultset.next())
        	{
        		System.out.println(resultset.getInt("id") +  "\t| " + 
        				resultset.getString("name") + "\t| " +
        				resultset.getDouble("capacity"));
        	}
        }
        catch (SQLException e)
        {
        	System.out.println(e.getMessage());
        }
    }	

	static Connection connectDatabase(String filename)
			throws SQLException
	{
		String url = "jdbc:sqlite:./" + filename;

		Connection connection = DriverManager.getConnection(url);
		DatabaseMetaData meta = connection.getMetaData();
		System.out.println("Connected to database using driver '" + meta.getDriverName() + "'");

		return connection;
	}

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args)
	{
		connectToDatabase("test.db");
	}
}
