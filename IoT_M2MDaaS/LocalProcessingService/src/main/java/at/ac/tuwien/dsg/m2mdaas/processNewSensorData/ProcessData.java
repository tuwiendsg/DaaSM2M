/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.m2mdaas.processNewSensorData;

import java.util.HashMap;

/**
 *
 * @author Georgiana
 */
public interface ProcessData {
        public HashMap<String,HashMap<String,String>> processData(String event);

}
