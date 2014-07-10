/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.m2mDaaS.processNewSensorData;

import java.util.HashMap;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 *
 * @author Georgiana
 */
public class ProcessSensorLocationData implements ProcessData{
    public  HashMap<String,HashMap<String,String>> processData(String event){
        HashMap<String,HashMap<String,String>> result = new HashMap<String,HashMap<String,String>>();
        HashMap<String,String> sensorValues = new HashMap<String,String>();
        JSONTokener jtoken = new JSONTokener(event);
		
		JSONObject jsn = (JSONObject) jtoken.nextValue();
		System.out.println( jsn.get("id")+" "+jsn.get("longitude")+" "+jsn.getString("latitude"));
         sensorValues.put("longitude",jsn.getString("longitude"));
         sensorValues.put("latitude",jsn.getString("latitude"));
         result.put("id",sensorValues);
         
         return result;
    } 
}
