package at.ac.tuwien.dsg.daas.config;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import at.ac.tuwien.dsg.daas.util.ConfigurationFilesLoader;

public class CassandraAccessProperties {
	private static Properties properties;

	static {
		properties = new Properties();
		try {
			InputStream is = ConfigurationFilesLoader.getCassandraAccessPropertiesStream();
			properties.load(is);
			is.close();
		} catch (FileNotFoundException e) {
			Logger.getLogger(CassandraAccessProperties.class).log(Level.ERROR, e);
		} catch (IOException e) {
			Logger.getLogger(CassandraAccessProperties.class).log(Level.ERROR, e);
		}finally{
		
		}
	}

	public static String getCassandraAccessIP() {
		if (properties.containsKey("CassandraNode.IP")) {
			return properties.getProperty("CassandraNode.IP").trim();
		} else {
			return "localhost"; // default
		}
	}

	public static String getCassandraAccessPort() {
		if (properties.containsKey("CassandraNode.IP")) {
			return  properties.getProperty("CassandraNode.PORT");
		} else {
			return "-1"; // default
		}
	}
}
