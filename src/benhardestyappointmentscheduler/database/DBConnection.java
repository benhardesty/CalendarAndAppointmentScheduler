/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package benhardestyappointmentscheduler.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * No one would ever implement a database connection like this from the client
 * side, but it was a requirement in the project rubric.
 * 
 * @author Ben Hardesty
 */
public class DBConnection {
    
    private static final String DB_NAME = "";
    private static final String DB_URL = "" + DB_NAME;
    private static final String USERNAME = "";
    private static final String DRIVER = "com.mysql.jdbc.Driver";
    
    // Again, I would never do this in real life, but it is required for this
    // assignment.
    private static final String PASSWORD = "";
    
    /**
     * Return a connection object to the remote database.
     * 
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     * @throws Exception 
     */
    public static Connection getConnection() throws ClassNotFoundException, SQLException, Exception {
        Class.forName(DRIVER);
        return (Connection) DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
    }
}
