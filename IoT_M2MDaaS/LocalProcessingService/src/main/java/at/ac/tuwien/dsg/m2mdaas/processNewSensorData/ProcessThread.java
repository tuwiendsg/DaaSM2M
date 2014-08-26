/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.m2mdaas.processNewSensorData;

import at.ac.tuwien.dsg.m2mdaas.callEventProcessing.EventProcessingM2MInterraction;
import java.util.HashMap;

/**
 *
 * @author Georgiana
 */
public class ProcessThread implements Runnable{
    Thread procThread;
             private  EventProcessingM2MInterraction  eventProcessingM2MInterraction = new EventProcessingM2MInterraction();

    HashMap<String,HashMap<String,String>> data;
    public ProcessThread(HashMap<String,HashMap<String,String>> data){
        procThread = new Thread(this);
        this.data=data;
    }
    public void start(){
        procThread.start();
    }
    @Override
    public void run() {
        eventProcessingM2MInterraction.putData(data);
    }
}
