package org.geordin.dbutil;

import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgresConnection {

    // this should create a single DB connection connection which can be imported.
    private PostgresConnection(){}
    private static Connection connection;
    private static Logger log=Logger.getLogger(PostgresConnection.class);
    static {
        try {
            Class.forName("org.postgresql.Driver");
            String url="jdbc:postgresql://localhost:5432/postgres";
            String username="postgres";
            String password="password"; //move this/these to an environmental variable eventually
//                String url =
//            String username =
//            String password =

            connection= DriverManager.getConnection(url,username,password);
        } catch (ClassNotFoundException | SQLException e) {
            log.warn(e.getMessage()); //may need to handle this...
            System.exit(1);//exit app - crash!
        }
    }
    public static Connection getConnection(){
        return connection;
    }

}
