package com.toolbus.testbench.db;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ElementDB
{
	Connection	_connection;
	String		_filename;
	String		_driver_version = "<undefined>";
	
	public ElementDB(String filename)
			throws Exception
	{
		_filename = filename;
	}
	
	public String driverVersion()
	{
		return _driver_version;
	}
	
	private void checkDBConnected() throws Exception
	{
		if (_connection == null)
		{
			throw new Exception("Database not connected.");
		}
	}
	
    public void insertRecord(DBElement element) throws Exception
    {
    	checkDBConnected();
    	
    	String sql = "INSERT INTO elements VALUES(?, ?, ?, ?)";

    	PreparedStatement statement = _connection.prepareStatement(sql);
    	
    	statement.setString(1, element._id);
    	statement.setString(2, element._name);
    	statement.setString(3, element._type);
    	statement.setString(4, element._stereotype);
    	
    	statement.executeUpdate();
    }
    
    public List<DBElement> retrieveAllElements() throws Exception
    {
    	checkDBConnected();

    	return retrieveElements("");
    }
    
    public List<DBElement> retrieveElements(String condition) throws Exception
    {
    	checkDBConnected();

    	Statement statement  = _connection.createStatement();

    	// Get the number of elements in the table
    	ResultSet resultset = statement.executeQuery("SELECT COUNT() from elements " + condition);
    	resultset.next();
    	int count = resultset.getInt(1);

    	// Create an array list of sufficient size to contain all elements
    	List<DBElement> elements = new ArrayList<>(count);

    	// Fetch the elements and populate the list
    	String sql = "SELECT id, name, type, stereotype FROM elements " + condition;
    	resultset = statement.executeQuery(sql);
    	
    	// loop through the result set
    	while (resultset.next())
    	{
    		String id = resultset.getString(1);
    		String name = resultset.getString(2);
    		String type = resultset.getString(3);
    		String stereotype = resultset.getString("stereotype");
    		
    		elements.add(new DBElement(id, name, type, stereotype));
    	}
    	
    	return elements;
    }

    public void connectDatabase() throws Exception
    {
    	File dbfile = new File(_filename);

    	if (!dbfile.exists())
    	{
    		throw new FileNotFoundException("Database file '" + _filename + "' does not exist.");
    	}
    	
    	_connectDatabase();

    	checkDBConnected();
    }
    
    private void _connectDatabase() throws Exception
    {
		String url = "jdbc:sqlite:" + _filename;
		_connection = DriverManager.getConnection(url);    	
		
		DatabaseMetaData meta = _connection.getMetaData();
		_driver_version = meta.getDriverName() + " v" + meta.getDriverVersion() + "'";
    }
	
	void initializeDatabase() throws Exception
	{
		_connectDatabase();
		
    	checkDBConnected();		
		
		String sql = "CREATE TABLE IF NOT EXISTS elements ("
				+ "	id CHAR(32) PRIMARY KEY,"
				+ "	name CHAR(20) NOT NULL,"
				+ "	type CHAR(20),"
				+ " stereotype CHAR(20)"
				+ ");";

		Statement statement = _connection.createStatement();
		statement.execute(sql);
	}
}
