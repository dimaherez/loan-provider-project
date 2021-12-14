package com.nulp.dbconnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnect {
    static String databaseName = "LoansDB";
    static String user = "LoansDBUser";
    static String password = "loansdb";


    static String url = "jdbc:sqlserver://localhost;databaseName="+databaseName+";user="+user+";password="+password+"";

    public static Connection getConnection(){
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return connection;
    }

}
