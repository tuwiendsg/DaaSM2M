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


package at.ac.tuwien.dsg.m2mdaas.analysis;

import at.ac.tuwien.dsg.m2mdaas.processNewSensorData.MQConsumer;
import at.ac.tuwien.dsg.m2mdaas.utils.Configuration;
import com.sun.jersey.spi.resource.Singleton;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;



@Singleton
@Path("/")
public class LocalProcessingWS implements Runnable{
        @Context
        private UriInfo context;
        	private static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(LocalProcessingWS.class);

	private String host = "";
        private String port =  "";
        private MQConsumer listener;
        private Thread startThread;
	public LocalProcessingWS(){
        try{
                host = Configuration.getMQHost();
                port = Configuration.getMQPort();
                if (port==null || port.length()==0)
                    port="61616";
                }catch(ExceptionInInitializerError e){
                   LOGGER.info(e.getMessage());
                }
               startThread = new Thread(this);
               startThread.start();
        
        }

	@GET
	@Path("/test")
	@Produces(MediaType.TEXT_PLAIN)
	public String test(){
		return "Test working";
	}
	
	 @GET
	 @Path("/bufferSize")
	 @Produces("application/xml")
	public String bufferSize(){
             if (listener!=null)
		return listener.getBufferSize()+"";
             else 
             {
                 System.out.println("Listener null");
                 startThread.start();
                 return "0";
                }
		
	}
	 
	public UriInfo getContext() {
		return context;
	}

	public void setContext(UriInfo context) {
		this.context = context;
	}

    @Override
    public void run() {
        String url = "failover://tcp://" + host + ":"+port;
        listener = new MQConsumer(url, "topic");
        listener.setUp();	
    }
}
