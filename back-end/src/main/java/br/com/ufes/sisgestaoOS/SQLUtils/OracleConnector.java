package br.com.ufes.sisgestaoOS.SQLUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import br.com.ufes.sisgestaoOS.utils.ManipuladorDePropriedades;



public class OracleConnector implements IConnector {
	
	private Connection conn;
	
	

	private OracleConnector() {
	}
	
	public static OracleConnector getInstance() {
        return NewSingletonHolder.INSTANCE;
    }
	
	private static class NewSingletonHolder {

        private static final OracleConnector INSTANCE = new OracleConnector();
    }

	@Override
	public Connection connect() {
		String url;
		String user;
		String pass;
		if (null == this.getConn()) {
            try {                
            	Properties prop = ManipuladorDePropriedades.getProp();
    			url = prop.getProperty("dburl");
    			user = prop.getProperty("user");
    			pass = prop.getProperty("pass");
    			this.conn = DriverManager.getConnection(url, user, pass);
            } catch (SQLException e) {
            	System.out.println("SQL Error! Alert: ");
    			e.printStackTrace();
            }
        }
        return this.conn;
	}

	@Override
	public void disconect() {
		if (null != this.getConn()) {        
            try {                
              conn.close();           
            } catch (SQLException e) {
            	System.out.println("SQL Error! Alert: ");
    			e.printStackTrace();
            }
        }
	}
	
	private Connection getConn() {
        return conn;
    }

}
