package br.com.ufes.sisgestaoOS.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public final class ManipuladorDePropriedades {
	public static String dir = "config.properties";

	public static Properties getProp() throws IOException {
		
		Properties props = new Properties();
		FileInputStream file = new FileInputStream(dir);
				//"./properties/dados.properties");
		props.load(file);
		return props;		
	}
	
	public static void saveProps(Properties props) throws IOException{
		FileOutputStream file = new FileOutputStream(dir);
		props.store(file, null);
	}

}
