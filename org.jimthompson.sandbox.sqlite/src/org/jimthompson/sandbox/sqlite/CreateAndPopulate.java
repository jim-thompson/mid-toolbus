package org.jimthompson.sandbox.sqlite;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateAndPopulate
{
	public static void populateDatabase(String filename)
	{
		try
		{
			Connection c = initializeDatabase(filename);
			populate(c);
		}
		catch (SQLException e)
		{
			System.err.println("*** ERROR ***");
			System.err.println(e.getMessage());
		}
	}
	
    public static void insertRecord(Connection connection, String name, double capacity)
    		throws SQLException
    {
    	String sql = "INSERT INTO warehouses(name,capacity) VALUES(?,?)";

    	PreparedStatement pstmt = connection.prepareStatement(sql);
    	pstmt.setString(1, name);
    	pstmt.setDouble(2, capacity);
    	pstmt.executeUpdate();
    }
	
    public static void populate(Connection connection)
    		throws SQLException
    {
    	insertRecord(connection, "foo", 100);
    	insertRecord(connection, "bar", 200);
    	insertRecord(connection, "baz", 300);
    }	

	static Connection initializeDatabase(String filename)
			throws SQLException
	{
		String url = "jdbc:sqlite:./" + filename;
		String sql = "CREATE TABLE IF NOT EXISTS warehouses (\n"
				+ "	id integer PRIMARY KEY,\n"
				+ "	name text NOT NULL,\n"
				+ "	capacity real\n"
				+ ");";


		Connection connection = DriverManager.getConnection(url);
		DatabaseMetaData meta = connection.getMetaData();
		System.out.println("A new database has been created using driver '" + meta.getDriverName() + "'");

		Statement statement = connection.createStatement();
		statement.execute(sql);
		
		return connection;
	}

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args)
	{
		populateDatabase("test.db");
	}
}
