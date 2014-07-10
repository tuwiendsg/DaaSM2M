/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.m2mDaaS.analysis;

import at.ac.tuwien.dsg.m2mDaaS.callEventProcessing.EventProcessingM2MInterraction;
import at.ac.tuwien.dsg.m2mDaaS.processNewSensorData.MQConsumer;
import at.ac.tuwien.dsg.m2mDaaS.utils.Configuration;


/**
 *
 * @author Georgiana
 */
public class Main {
    public static void main (String [] args){
        String host = "";
        String port =  "";
        try{
        host = Configuration.getMQHost();
        port = Configuration.getMQPort();
        if (port==null || port.length()==0)
            port="61616";
        }catch(ExceptionInInitializerError e){
            if (args.length>0){
                host=args[0];
                 port="61616";
            }
        }
        String url = "failover://tcp://" + host + ":"+port;
        MQConsumer listener = new MQConsumer(url, "topic");
	listener.setUp();
        
    }
}
