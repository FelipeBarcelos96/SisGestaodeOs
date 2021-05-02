package br.com.ufes.sisgestaoOS.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public final class ManipuladorDePropriedades {
	public static String dir = "config.properties";

	@SuppressWarnings("finally")
	public static Properties getProp() {
		Properties props = null;
		try {
		props = new Properties();
		FileInputStream file = new FileInputStream(dir);
				//"./properties/dados.properties");
		props.load(file);
		}catch(Exception e) {
			System.out.println("Error! Alert: "+e.getMessage());
			e.printStackTrace();
		}finally {
		  return props;
		}
	}
	
	public static void saveProps(Properties props) throws IOException{
		FileOutputStream file = new FileOutputStream(dir);
		props.store(file, null);
	}

}
