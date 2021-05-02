/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.ufes.sisgestaoOS.SQLUtils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import br.com.ufes.sisgestaoOS.utils.ManipuladorDePropriedades;

/**
 *
 * @author Felps
 */
public class SQLiteConnector implements IConnector {

    private Connection conn;

    private SQLiteConnector() {
    }

    public static SQLiteConnector getInstance() {
        return NewSingletonHolder.INSTANCE;
    }

    private static class NewSingletonHolder {

        private static final SQLiteConnector INSTANCE = new SQLiteConnector();
    }

    @Override
    public Connection connect() {
        if (null == this.getConn()) {
            try {
                // db parameters  
                //String url = "jdbc:sqlite:EmbebbedDataBases\\prova2.db";
            	String url;
					url = ManipuladorDePropriedades.getProp().getProperty("dburl");
                // create a connection to the database  
                this.conn = DriverManager.getConnection(url);

                System.out.println("Connection to SQLite has been established.");

            } catch (SQLException e) {
            	System.out.println("SQL Error! Alert: ");
    			e.printStackTrace();
            }
        }
        return this.conn;
    }
    
    public void disconect(){
        if (null != this.getConn()) {        
            try {                
              conn.close();
              System.out.println("Disconected from SQLite.");
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public Connection connect(String url) {
        if (null == this.getConn()) {
            try {
                // db parameters                  
                // create a connection to the database  
                this.conn = DriverManager.getConnection(url);

                System.out.println("Connection to SQLite has been established.");

            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        return this.conn;
    }

    private Connection getConn() {
        return conn;
    }

}
