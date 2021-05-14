package org.geordin.dbutil;

import org.apache.log4j.Logger;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class PostgresConnection {

    // this should create a single DB connection connection which can be imported.
    private PostgresConnection(){}
    private static Connection connection;
    private static Logger log=Logger.getLogger(PostgresConnection.class);
    static {

        try {
            Class.forName("org.postgresql.Driver");

            //property file that doesnt work

//            FileReader reader=new FileReader("/ProjectOne/build/db.properties");
//            Properties p=new Properties();
//            p.load(reader);
//
////            String url = p.getProperty("dburl");
//            String username = p.getProperty("/dbusername");
//            String password = p.getProperty("dbpassword");

//            end property file section

            String url="jdbc:postgresql://localhost:5432/postgres";
            String username="postgres"; //move these to an environmental variable eventually
            String password="password"; //move these to an environmental variable eventually
            connection= DriverManager.getConnection(url,username,password);
        } catch (ClassNotFoundException | SQLException e) {
            // | IOException | NullPointerException
            log.warn(e.getMessage()); //may need to handle this...
//            System.out.println(e.getMessage());
            System.exit(1);//exit app - crash!
        }
    }
    public static Connection getConnection(){
        return connection;
    }

}
