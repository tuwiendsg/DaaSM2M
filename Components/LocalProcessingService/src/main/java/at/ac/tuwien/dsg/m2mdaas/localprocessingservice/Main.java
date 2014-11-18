/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.m2mdaas.localprocessingservice;

import at.ac.tuwien.dsg.daas.entities.Column;
import at.ac.tuwien.dsg.daas.entities.CreateRowsStatement;
import at.ac.tuwien.dsg.daas.entities.Keyspace;
import at.ac.tuwien.dsg.daas.entities.RowColumn;
import at.ac.tuwien.dsg.daas.entities.Table;
import at.ac.tuwien.dsg.daas.entities.TableRow;
import at.ac.tuwien.dsg.m2mdaas.callEventProcessing.EventProcessingM2MInterraction;
import at.ac.tuwien.dsg.m2mdaas.callEventProcessing.RESTCall;
import at.ac.tuwien.dsg.m2mdaas.processNewSensorData.GenericNewSensorDataProcessing;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;


/**
 *
 * @author Georgiana
 */
public class Main {
    public static void main (String [] args){
//        String host = "";
//        String port =  "";
//        try{
//        host = Configuration.getMQHost();
//        port = Configuration.getMQPort();
//        if (port==null || port.length()==0)
//            port="61616";
//        }catch(ExceptionInInitializerError e){
//            if (args.length>0){
//                host=args[0];
//                 port="61616";
//            }
//        }
//        String url = "failover://tcp://" + host + ":"+port;
//        MQConsumer listener = new MQConsumer(url, "topic");
//	listener.setUp();
//        
        GenericNewSensorDataProcessing dataProcessing = new GenericNewSensorDataProcessing();
        String s="{\"id\":\"" + 1278 + "\",\"latitude\":\"" + 4 + "\",\"longitude\":\"" + 5 + "\"}";
        dataProcessing.processData(s);
//        RESTCall call = new RESTCall("http://109.231.122.200:8080");
//        
//            
//            Table table = new Table();
//            Keyspace keyspace = new Keyspace();
//            keyspace.setName("m2m");
//            table.setKeyspace(keyspace);
//            table.setName("table_1278");
//            table.setPrimaryKeyName("key");
//            table.setPrimaryKeyType("uuid");
//            Column c = new Column();
//                 Column column1 = new Column("longitude","double");
//             Column column2 = new Column("latitude","double");
//             List<Column> columns = new ArrayList<Column>();
//             columns.add(column1);
//             columns.add(column2);
//           
//             table.setColumns(columns);
//             
//      
//             try{
//         JAXBContext jAXBContext = JAXBContext.newInstance(Table.class);
//             Marshaller marshaller = jAXBContext.createMarshaller();
//                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
//                
//                StringWriter sw = new StringWriter();
//                marshaller.marshal(table, sw);
//              System.out.println(sw.toString());
//              List<Column> listc=  new ArrayList<Column>();
//              listc.add(c);
//             // eventProcessingM2MInterraction.createTable("a",listc);
//         System.out.println(call.callPutMethod("109.231.122.200", 8080,"DaaS/api/xml/table", sw.toString(), "application/xml","application/xml"));         
//        }catch(Exception e ){
//            System.out.println(e.getMessage());
//        }
//            
//        
//                CreateRowsStatement createRowsStatement = new CreateRowsStatement();
//                TableRow tableRow = new TableRow();
//                RowColumn rowColumn1 = new RowColumn();
//                rowColumn1.setName("longitude");
//                rowColumn1.setValue("2");
//                RowColumn rowColumn2 = new RowColumn();
//                rowColumn2.setName("latitude");
//                rowColumn2.setValue("2");
//                 RowColumn rowColumn3 = new RowColumn();
//                rowColumn3.setName("key");
//                rowColumn3.setValue(UUID.randomUUID().toString());
//                tableRow.addRowColumn(rowColumn3);
//                tableRow.addRowColumn(rowColumn1);
//                tableRow.addRowColumn(rowColumn2);
//                createRowsStatement.addRow(tableRow);
//                createRowsStatement.setTable(table);
//                 try{
//         JAXBContext jAXBContext = JAXBContext.newInstance(CreateRowsStatement.class);
//             Marshaller marshaller = jAXBContext.createMarshaller();
//                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
//                
//                StringWriter sw = new StringWriter();
//                marshaller.marshal(createRowsStatement, sw);
//            //  System.out.println(sw.toString());
//        // System.out.println(call.callPutMethod("109.231.122.200", 8080,"DaaS/api/xml/table/row", sw.toString(), "application/xml","application/xml"));         
//        }catch(Exception e ){
//            System.out.println(e.getMessage());
//        }
                /*
                 * <Table primaryKeyType="uuid" primaryKeyName="key" name="1278">
    <Keyspace name="m2m"/>
    <Column type="double" name="longitude"/>
    <Column type="double" name="latitude"/>
</Table>
                 */ 
//             String body="";
//            body="<CreateRowsStatement><Table name=\"x\"><Keyspace name=\"m2m\"></Table><Row>";
//                body+="<Column name=\"latitude\" value=\"2\"/>";
//                                body+="<Column name=\"key\" value="+UUID.randomUUID().toString() +"/>";
//
//                            body+="<Column name=\"longitude\" value=\"2\"/>";
//
//            body+="</Row></CreateRowsStatement>";
//               System.out.println(body);
//                    System.out.println(call.callPutMethod("109.231.122.200", 8080, "DaaS/api/xml/table/row",body, "application/xml","application/xml"));
           

           
            }
}
