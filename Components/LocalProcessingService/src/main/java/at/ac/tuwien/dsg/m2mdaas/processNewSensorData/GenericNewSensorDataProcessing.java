/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.m2mdaas.processNewSensorData;

import at.ac.tuwien.dsg.daas.entities.Column;
import at.ac.tuwien.dsg.m2mdaas.callEventProcessing.EventProcessingM2MInterraction;
import at.ac.tuwien.dsg.m2mdaas.utils.Configuration;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;
import org.json.JSONTokener;
/**
 *
 * @author Georgiana
 */
public class GenericNewSensorDataProcessing implements ProcessData{
    private static final Logger LOGGER = Logger.getLogger(GenericNewSensorDataProcessing.class.getName());

    public GenericNewSensorDataProcessing() {
        for (String s : Configuration.getCurrentTables()) {
            tableNames.add(s);
        }

    }
    private List<String> tableNames = new ArrayList<String>();

    public HashMap<String, HashMap<String, String>> processData(String event) {
        HashMap<String, HashMap<String, String>> result = new HashMap<String, HashMap<String, String>>();
        HashMap<String, String> sensorValues = new HashMap<String, String>();
        try{
        JSONTokener jtoken = new JSONTokener(event);

        JSONObject jsn = (JSONObject) jtoken.nextValue();

        Iterator keys = jsn.keys();
        List<String> columnNames = new ArrayList<String>();
        while (keys.hasNext()) {
            // loop to get the dynamic key
           
            String currentDynamicKey = (String) keys.next();
            if (!currentDynamicKey.equalsIgnoreCase("id"))
            columnNames.add(currentDynamicKey);
        }
        EventProcessingM2MInterraction eventProcessingM2MInterraction = new EventProcessingM2MInterraction();
        List<Column> columns=new ArrayList<Column>();
        if (!tableNames.contains("table_"+jsn.getString("id"))){
        
        
        for (String s : columnNames) {
            Column col = null;
            Double nb=null;
            try {
                nb=(Double) NumberFormat.getInstance().parse(""+jsn.get(s));
            } catch (ParseException ex) {
                Logger.getLogger(GenericNewSensorDataProcessing.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (nb==null){
                col=new Column(s,"text");
                
            }else{
                col=new Column(s,"double");
               
            }
           columns.add(col);  
        }
           eventProcessingM2MInterraction.createTable(jsn.getString("id"), columns);
           tableNames.add("table_"+jsn.getString("id"));
           
        }
      
        for (String s : columnNames){
            sensorValues.put(s, jsn.getString(s));
        }


        result.put(jsn.getString("id"), sensorValues);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return result;
    }
}
