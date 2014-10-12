/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test.utils;

import at.ac.tuwien.dsg.daas.entities.Column;
import at.ac.tuwien.dsg.daas.entities.CreateRowsStatement;
import at.ac.tuwien.dsg.daas.entities.Keyspace;
import at.ac.tuwien.dsg.daas.entities.RowColumn;
import at.ac.tuwien.dsg.daas.entities.Table;
import at.ac.tuwien.dsg.daas.entities.TableQuery;
import at.ac.tuwien.dsg.daas.entities.TableRow;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import javax.ws.rs.core.UriBuilder;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import org.apache.http.HttpHost;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;

/**
 *
 * @author Georgiana
 */
public class EventProcessingM2MInterraction {

    private String KeyspaceName = "m2m";

    private static final Logger log = Logger.getLogger(EventProcessingM2MInterraction.class);
    private String IP = "localhost";
    private int PORT = 8080;
    private String url = "http://" + IP + ':' + PORT;

    public EventProcessingM2MInterraction() {

    }

    public void setKeyspaceName(String KeyspaceName) {
        this.KeyspaceName = KeyspaceName;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public void setPORT(int PORT) {
        this.PORT = PORT;
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
            log.info(sw.toString());
            log.info(rESTCall.callPutMethod(IP, PORT, "DaaS/api/xml/table", sw.toString(), "application/xml", "application/xml"));
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
        return true;
    }

    public void putData(CreateRowsStatement createRowsStatement) {
        RESTCall rESTCall = new RESTCall(url);

        TableRow tableRow = new TableRow();

        RowColumn rowColumnKey = new RowColumn();
        rowColumnKey.setName("key");
        rowColumnKey.setValue(UUID.randomUUID().toString());
        tableRow.addRowColumn(rowColumnKey);

        try {
            JAXBContext jAXBContext = JAXBContext.newInstance(CreateRowsStatement.class);
            Marshaller marshaller = jAXBContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            StringWriter sw = new StringWriter();

            marshaller.marshal(createRowsStatement, sw);
            log.info(sw.toString());

            log.info(rESTCall.callPutMethod(IP, PORT, "DaaS/api/xml/table/row", sw.toString(), "application/xml", "application/xml"));
        } catch (Exception e) {
            log.error("Error in unmarshalling");
        }
    }

    public void getData(TableQuery query) {
        {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpHost endpoint = new HttpHost(IP, PORT);

            try {

                JAXBContext jAXBContext = JAXBContext.newInstance(TableQuery.class);
                Marshaller marshaller = jAXBContext.createMarshaller();
                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
                StringWriter sw = new StringWriter();
                log.info("Sending service description description to rSYBL");
                marshaller.marshal(query, sw);
                log.info(sw.toString());

                URI putDeploymentStructureURL = UriBuilder.fromPath("/DaaS/api/table/row").build();
                HttpPost putDeployment = new HttpPost(putDeploymentStructureURL);

                StringEntity entity = new StringEntity(sw.getBuffer().toString());

                entity.setContentType("application/xml");
                entity.setChunked(true);

                putDeployment.setEntity(entity);

                log.info("Executing request " + putDeployment.getRequestLine());
                org.apache.http.HttpResponse response = httpClient.execute(endpoint, putDeployment);
                org.apache.http.HttpEntity resEntity = response.getEntity();

                System.out.println("----------------------------------------");
                System.out.println(response.getStatusLine());
                if (response.getStatusLine().getStatusCode() == 200) {

                }
                if (resEntity != null) {
                    System.out.println("Response content length: " + resEntity.getContentLength());
                    System.out.println("Chunked?: " + resEntity.isChunked());

                    BufferedReader reader = new BufferedReader(new InputStreamReader(resEntity.getContent()));
                    String line = "";
                    while ((line = reader.readLine()) != null) {
                        log.info(line);
                        System.out.println(line);
                    }
                }

            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }

        }
    }

    public EventProcessingM2MInterraction withKeyspaceName(final String KeyspaceName) {
        this.KeyspaceName = KeyspaceName;
        return this;
    }

    public EventProcessingM2MInterraction withIP(final String IP) {
        this.IP = IP;
        return this;
    }

    public EventProcessingM2MInterraction withPORT(final int PORT) {
        this.PORT = PORT;
        return this;
    }

    public EventProcessingM2MInterraction withUrl(final String url) {
        this.url = url;
        return this;
    }
}
