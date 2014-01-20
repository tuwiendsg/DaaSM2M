/**
 * Copyright 2013 Technische Universitaet Wien (TUW), Distributed Systems Group
 * E184
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package at.ac.tuwien.dsg.daas;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import at.ac.tuwien.dsg.daas.impl.DataManagementDelegate;
import at.ac.tuwien.dsg.daas.util.ConfigurationFilesLoader;

/**
 * 
 * @Author Daniel Moldovan
 * @E-mail: d.moldovan@dsg.tuwien.ac.at
 * 
 */
public class DataManagementAPIFactory {

	private static List<DataManagementAPI> activeCassandraManagementAPIs;

	static {
		activeCassandraManagementAPIs = new ArrayList<DataManagementAPI>();
		InputStream log4jStream;
		try {
			log4jStream = ConfigurationFilesLoader.getLog4JPropertiesStream();
			if (log4jStream != null) {
				PropertyConfigurator.configure(log4jStream);
				try {
					log4jStream.close();
				} catch (IOException e) {
					Logger.getLogger(DataManagementAPIFactory.class).log(Level.ERROR, e);
				}
			}
		} catch (FileNotFoundException e) {
			Logger.getLogger(DataManagementAPIFactory.class).log(Level.ERROR, e);
		}

	}

	private DataManagementAPIFactory() {
	}

	public static DataManagementAPI createCassandraFactory(String ip, String port) {
		Logger.getLogger(DataManagementAPIFactory.class).log(Level.INFO, ip+":"+ port);
		DataManagementAPI cassandraManagementAPI = new DataManagementDelegate(ip, port);
		if (activeCassandraManagementAPIs.contains(cassandraManagementAPI)) {
			return activeCassandraManagementAPIs.get(activeCassandraManagementAPIs.indexOf(cassandraManagementAPI));
		} else {
			cassandraManagementAPI.openConnection();
			activeCassandraManagementAPIs.add(cassandraManagementAPI);
			return cassandraManagementAPI;
		}
	}
}
