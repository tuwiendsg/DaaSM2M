/** 
   Copyright 2013 Technische Universitat Wien (TUW), Distributed SystemsGroup E184.               
   
   This work was partially supported by the European Commission in terms of the CELAR FP7 project (FP7-ICT-2011-8 #317790).
 
   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/

/**
 *  Author : Georgiana Copil - e.copil@dsg.tuwien.ac.at
 */

package at.ac.tuwien.dsg.m2mdaas.utils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Configuration {

    private static  Properties configuration ;

    static{
        configuration = new Properties();
        
        try {
            configuration.load(new FileReader( new File("./config.properties")));

			
        } catch (Exception ex) {
        	InputStream is = Configuration.class.getClassLoader().getResourceAsStream("/config.properties");
			try {
				configuration.load(is);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}

        }
    }

    public static String getMQHost(){
    	return configuration.getProperty("MessageQueueHost");
      
    }
      public static String getHAProxyIP(){
    	return configuration.getProperty("HAProxyIP");
      
    }  public static String getHAProxyPort(){
    	return configuration.getProperty("HAProxyPort");
      
    }
    public static String[] getCurrentTables(){
        if (configuration.getProperty("AvailableTables")!=null && !configuration.getProperty("AvailableTables").equalsIgnoreCase(""))
        return configuration.getProperty("AvailableTables").split(",");
        else return new String[0];
    }
    
    public static String getMQPort() {
    	return configuration.getProperty("MessageQueuePort");
      
        }
   public static int getRefreshRate() {
    	return (Integer.parseInt(configuration.getProperty("REFRESH_RATE")));
      
        }
      public static int getBufferSize() {
    	return (Integer.parseInt(configuration.getProperty("MAX_BUFFER_SIZE")));
      
        }
   public static String getEventDefinition(){
       // define events
       return configuration.getProperty("EventDefinition");
   }
   public static String getCurrentPolicy(){
       // choose between sending policies: SEND_EVENTS, SEND_ALL, SEND_MIXED
       
       return configuration.getProperty("Policy");
   }
 public static String getBurstSleep(){
       // define events
       return configuration.getProperty("BURST_SLEEP");
   }
   public static String getBurstSize(){
       // choose between sending policies: SEND_EVENTS, SEND_ALL, SEND_MIXED
       return configuration.getProperty("BURST_SIZE");
   }
}