package com.highradius.resource;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {
	static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";  
	static final String DB_URL="jdbc:mysql://localhost/sakila";
	static final String USER = "root";
	static final String PASS = "root";
	
	public Connection doDatabaseConnection() throws Exception {
		Class.forName(JDBC_DRIVER);
		Connection conn = null;
		conn = DriverManager.getConnection(DB_URL, USER, PASS);
		return conn;
	}
	
}
