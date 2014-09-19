/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.m2mdaas.processNewSensorData;

import at.ac.tuwien.dsg.daas.entities.Column;
import at.ac.tuwien.dsg.m2mdaas.callEventProcessing.EventProcessingM2MInterraction;
import at.ac.tuwien.dsg.m2mdaas.utils.Configuration;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import oracle.jrockit.jfr.tools.ConCatRepository;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 *
 * @author Georgiana
 */
public class ProcessSensorLocationData implements ProcessData{
    public ProcessSensorLocationData(){
        for (String s: Configuration.getCurrentTables())
            tableNames.add(s);
      
    }
    private List<String> tableNames =new ArrayList<String>();
    public  HashMap<String,HashMap<String,String>> processData(String event){
        HashMap<String,HashMap<String,String>> result = new HashMap<String,HashMap<String,String>>();
        HashMap<String,String> sensorValues = new HashMap<String,String>();
        JSONTokener jtoken = new JSONTokener(event);
		
		JSONObject jsn = (JSONObject) jtoken.nextValue();
		//System.out.println( jsn.get("id")+" "+jsn.get("longitude")+" "+jsn.getString("latitude"));
         if (!tableNames.contains(jsn.getString("id"))){
             EventProcessingM2MInterraction eventProcessingM2MInterraction = new EventProcessingM2MInterraction();
             Column column1 = new Column("longitude","double");
             Column column2 = new Column("latitude","double");
             List<Column> columns = new ArrayList<Column>();
             columns.add(column1);
             columns.add(column2);
             
             boolean ok = eventProcessingM2MInterraction.createTable(jsn.getString("id"), columns);
             if (ok)
             {
                 tableNames.add(jsn.getString("id"));
             }
         }

         sensorValues.put("longitude",jsn.getString("longitude"));
         sensorValues.put("latitude",jsn.getString("latitude"));
         
         result.put(jsn.getString("id"),sensorValues);
         
         return result;
    } 
}
