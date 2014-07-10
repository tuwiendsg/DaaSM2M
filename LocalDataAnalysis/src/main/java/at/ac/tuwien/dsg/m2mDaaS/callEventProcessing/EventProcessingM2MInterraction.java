/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.m2mDaaS.callEventProcessing;

import java.util.HashMap;
import java.util.Map.Entry;

/**
 *
 * @author Georgiana
 */
public class EventProcessingM2MInterraction {
private String KeyspaceName = "m2m";
private String tablename = "sensor";

private String HAProxyIP = "10.99.0.39";
private String HAProxyport = "8080";
private String url= HAProxyIP+':'+HAProxyport;
    public EventProcessingM2MInterraction(){
        
    }
    public void putData(HashMap<String,HashMap<String,String>> data){
        RESTCall rESTCall = new RESTCall(url);
            
        for (String sensorId:data.keySet()){
//            Table=tablename
//            createRowStatement='<CreateRowsStatement><Table name="'+Table+'"><Keyspace name="' + KeyspaceName + '"/></Table><Row>'
//            for i in range(0, len(ColumnNames)):
//              createRowStatement=createRowStatement+('<Column name="%s" value="%s"/>' % (ColumnNames[i],Values[i]))
//            createRowStatement=createRowStatement + '</Row></CreateRowsStatement>'
//            executeRESTCall('PUT', BaseURL, 'DaaS/api/xml/table/row', createRowStatement)
            String body="";
            body="<CreateRowsStatement><Table name=\""+sensorId+"\"><Keyspace name=\""+KeyspaceName+"\"></Table><Row>";
            for (Entry<String,String> entry:data.get(sensorId).entrySet()){
                body+="<Column name=\""+entry.getKey()+"\" value=\""+entry.getValue()+"\"/>";
            }
            body+="</Row></CreateRowsStatement>";
            rESTCall.callPutMethod("/DaaS/api/xml/table/row", body, "application/xml","application/xml");         
        }
    }
}
