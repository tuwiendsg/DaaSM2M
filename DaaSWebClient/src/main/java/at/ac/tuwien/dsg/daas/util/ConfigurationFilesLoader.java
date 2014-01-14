package at.ac.tuwien.dsg.daas.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class ConfigurationFilesLoader {

	private ConfigurationFilesLoader() {
		 
	}
	 
	public static InputStream getCassandraAccessPropertiesStream()
			throws FileNotFoundException {
		return new FileInputStream("./config/cassandraAccess.properties");
	}
	
	
	public static InputStream getLog4JPropertiesStream()
			throws FileNotFoundException {
		return new FileInputStream("./config/Log4j.properties");
	}
 
}
