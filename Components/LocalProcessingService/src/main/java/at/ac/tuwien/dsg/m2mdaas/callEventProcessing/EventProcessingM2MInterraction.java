/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.m2mdaas.callEventProcessing;

import at.ac.tuwien.dsg.daas.entities.Column;
import at.ac.tuwien.dsg.daas.entities.CreateRowsStatement;
import at.ac.tuwien.dsg.daas.entities.Keyspace;
import at.ac.tuwien.dsg.daas.entities.RowColumn;
import at.ac.tuwien.dsg.daas.entities.Table;
import at.ac.tuwien.dsg.daas.entities.TableRow;
import at.ac.tuwien.dsg.m2mdaas.utils.Configuration;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import org.apache.log4j.Logger;

/**
 *
 * @author Georgiana
 */
public class EventProcessingM2MInterraction {

    private String KeyspaceName = "m2m";

    private static final Logger LOGGER = Logger.getLogger(EventProcessingM2MInterraction.class);
    private String HAProxyIP = Configuration.getHAProxyIP();
    private String HAProxyport = Configuration.getHAProxyPort();
    private String url = "http://" + HAProxyIP + ':' + HAProxyport;

    public EventProcessingM2MInterraction() {

    }

    public boolean createTable(String tableName, List<Column> columns) {
        RESTCall rESTCall = new RESTCall(url);
        Keyspace keyspace = new Keyspace(KeyspaceName);
        Table table = new Table(keyspace, "table_" + tableName, "key", "uuid");

        table.setColumns(columns);
        try {
            JAXBContext jAXBContext = JAXBContext.newInstance(Table.class);
            Marshaller marshaller = jAXBContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            StringWriter sw = new StringWriter();
            marshaller.marshal(table, sw);
            LOGGER.info(sw.toString());
            LOGGER.info(rESTCall.callPutMethod(HAProxyIP, Integer.parseInt(HAProxyport), "DaaS/api/xml/table", sw.toString(), "application/xml", "application/xml"));
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return false;
        }
        return true;
    }

    public void putData(HashMap<String, HashMap<String, String>> data) {
        RESTCall rESTCall = new RESTCall(url);

        for (String sensorId : data.keySet()) {
            CreateRowsStatement createRowsStatement = new CreateRowsStatement();

            TableRow tableRow = new TableRow();

            RowColumn rowColumnKey = new RowColumn();
            rowColumnKey.setName("key");
            rowColumnKey.setValue(UUID.randomUUID().toString());
            tableRow.addRowColumn(rowColumnKey);

//            Table=tablename
//            createRowStatement='<CreateRowsStatement><Table name="'+Table+'"><Keyspace name="' + KeyspaceName + '"/></Table><Row>'
//            for i in range(0, len(ColumnNames)):
//              createRowStatement=createRowStatement+('<Column name="%s" value="%s"/>' % (ColumnNames[i],Values[i]))
//            createRowStatement=createRowStatement + '</Row></CreateRowsStatement>'
//            executeRESTCall('PUT', BaseURL, 'DaaS/api/xml/table/row', createRowStatement)
            String body = "";
            body = "<CreateRowsStatement><Table name=\"" + sensorId + "\"><Keyspace name=\"" + KeyspaceName + "\"></Table><Row>";
            for (Entry<String, String> entry : data.get(sensorId).entrySet()) {
                body += "<Column name=\"" + entry.getKey() + "\" value=\"" + entry.getValue() + "\"/>";
                RowColumn rowColumn = new RowColumn();
                rowColumn.setName(entry.getKey());
                rowColumn.setValue(entry.getValue());
                tableRow.addRowColumn(rowColumn);
            }
            createRowsStatement.addRow(tableRow);
            Table table = new Table();
            body += "</Row></CreateRowsStatement>";
            Keyspace keyspace = new Keyspace();
            keyspace.setName(KeyspaceName);
            table.setKeyspace(keyspace);
            table.setName("table_" + sensorId);
            createRowsStatement.setTable(table);

            try {
                JAXBContext jAXBContext = JAXBContext.newInstance(CreateRowsStatement.class);
                Marshaller marshaller = jAXBContext.createMarshaller();
                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

                StringWriter sw = new StringWriter();

                marshaller.marshal(createRowsStatement, sw);
                LOGGER.info(sw.toString());

                LOGGER.info(rESTCall.callPutMethod(HAProxyIP, Integer.parseInt(HAProxyport), "DaaS/api/xml/table/row", sw.toString(), "application/xml", "application/xml"));
            } catch (Exception e) {
                LOGGER.error("Error in unmarshalling");
            }
        }
    }
}
