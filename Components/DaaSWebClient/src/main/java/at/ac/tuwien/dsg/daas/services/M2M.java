/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.daas.services;

import at.ac.tuwien.dsg.daas.DaaSDelegate;
import at.ac.tuwien.dsg.daas.DaaSDelegate.MonitoringData;
import at.ac.tuwien.dsg.daas.entities.CreateRowsStatement;
import at.ac.tuwien.dsg.daas.entities.Keyspace;
import at.ac.tuwien.dsg.daas.entities.RowColumn;
import at.ac.tuwien.dsg.daas.entities.Table;
import at.ac.tuwien.dsg.daas.entities.TableQuery;
import at.ac.tuwien.dsg.daas.entities.TableRow;

import com.datastax.driver.core.ColumnDefinitions;
import com.datastax.driver.core.Row;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

//import org.apache.activemq.ActiveMQConnectionFactory;
//import org.apache.activemq.broker.BrokerService;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * REST Web Service
 *
 * Author: Daniel Moldovan Institution: Vienna University of Technology
 */
@Path("/")
public class M2M {

    @Context
    private UriInfo context;
    private static DaaSDelegate api;
    private static final Charset charset = Charset.forName("UTF-8");
    private static final CharsetDecoder decoder = charset.newDecoder();

//    static {
//        try {
//            BrokerService broker = new BrokerService();
//            broker.setBrokerName("DaasM2MBroker");
//            broker.addConnector(System.getProperty("ActiveMQProtocol", "tcp") + "://" + System.getProperty("ActiveMQIP", "128.131.172.45") + ":" + System.getProperty("ActiveMQPort", "61616"));
//            broker.start();
//        } catch (Exception ex) {
//            Logger.getLogger(M2M.class.getName()).log(Level.ERROR, null, ex);
//        }
//    }
    static {
        api = DaaSDelegate.getInstance();
    }

    /**
     * Creates a new instance of M2M
     */
    public M2M() {

    }

    /**
     * Creates a new Cassandra keyspace
     *
     * @param keyspace <Keyspace name="keyspaceName"/>
     */
    @PUT
    @Path("/xml/keyspace")
    @Consumes("application/xml")
    public void putKeyspace(Keyspace keyspace) {
        api.createKeyspace(keyspace);

    }

    @GET
    @Path("/xml/keyspace")
    @Consumes("application/xml")
    public Collection<Keyspace> listKeyspaces() {
        Collection<Keyspace> keyspaces = new ArrayList<Keyspace>();
        List<Row> rows = api.listKeyspaces();
        for (Row row : rows) {
            Logger.getLogger(M2M.class.getName()).log(Level.ERROR, row.toString());
        }

        return keyspaces;

    }

    @PUT
    @Path("/json/keyspace")
    @Consumes("application/json")
    public void putKeyspaceJSON(String keyspaceJSON) {
        try {
            ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally
            Keyspace keyspace = mapper.readValue(keyspaceJSON, Keyspace.class);
            api.createKeyspace(keyspace);

        } catch (IOException ex) {
            Logger.getLogger(M2M.class.getName()).log(Level.ERROR, null, ex);
        }

    }

    /**
     * Drops a Cassandra keyspace
     *
     * @param keyspace <Keyspace name="keyspaceName"/>
     */
    @DELETE
    @Path("/xml/keyspace")
    @Consumes("application/xml")
    public void dropKeyspace(Keyspace keyspace) {
        api.dropKeyspace(keyspace);
    }

    @DELETE
    @Path("/json/keyspace")
    @Consumes("application/json")
    public void dropKeyspaceJSON(String keyspaceJSON) {
        try {
            ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally
            Keyspace keyspace = mapper.readValue(keyspaceJSON, Keyspace.class);
            api.dropKeyspace(keyspace);

        } catch (IOException ex) {
            Logger.getLogger(M2M.class.getName()).log(Level.ERROR, null, ex);
        }
    }

    /**
     *
     * @param table
     */
    @PUT
    @Path("/xml/table")
    @Consumes("application/xml")
    public void putTable(Table table) {
        api.createTable(table);
    }

    /**
     * Drops a Cassandra table
     *
     * @param table
     */
    @DELETE
    @Path("/xml/table")
    @Consumes("application/xml")
    public void dropTable(Table table) {
        api.dropTable(table);
    }

    /**
     *
     * @param table
     */
    @PUT
    @Path("/json/table")
    @Consumes("application/json")
    public void putTableJSON(String tableJSON) {
        try {
            ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally
            Table table = mapper.readValue(tableJSON, Table.class);
            api.createTable(table);
        } catch (IOException ex) {
            Logger.getLogger(M2M.class.getName()).log(Level.ERROR, null, ex);
        }
    }

    /**
     * Drops a Cassandra table
     *
     * @param table
     */
    @DELETE
    @Path("/json/table")
    @Consumes("application/json")
    public void dropTableJSON(String tableJSON) {
        try {
            ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally
            Table table = mapper.readValue(tableJSON, Table.class);
            api.dropTable(table);
        } catch (IOException ex) {
            Logger.getLogger(M2M.class.getName()).log(Level.ERROR, null, ex);
        }
    }

    /**
     * Returns first X rows that match condition
     *
     * @param querry contains the "table" and the "condition" for which I do not
     * have a better idea at the moment. Aimed at maximum freedom, the condition
     * can be any CQL3 statement,
     * http://www.datastax.com/docs/1.1/references/cql/SELECT Examples: WHERE
     * name='bob' AND score >= 40, userID = 'some_key_value', userID IN (key1,
     * key2)
     *
     * IF no maxRowCount specified on query, it returns only 1 row
     *
     * @return
     */
    @POST
    @Path("/table/row")
    @Consumes("application/xml")
    @Produces("application/xml")
    public Collection<TableRow> retrieveXRows(TableQuery querry) {

        List<Row> rows = api.selectXRowsFromTable(querry);
        if (rows == null) {
            return new ArrayList<TableRow>();
        }
        List<TableRow> retrievedRows = new ArrayList<TableRow>();
        for (Row row : rows) {
            TableRow tableRow = new TableRow();

            for (ColumnDefinitions.Definition column : row.getColumnDefinitions()) {
                String columnName = column.getName();
                String value;
                switch (column.getType().getName()) {
                    case DOUBLE:
                        value = "" + row.getDouble(columnName);
                        break;
                    case DECIMAL:
                    case FLOAT:
                        value = "" + row.getFloat(columnName);
                        break;
                    case COUNTER:
                        value = "" + row.getLong(columnName);
                        break;
                    case INT:
                        value = "" + row.getInt(columnName);
                        break;
                    case TEXT:
                    case VARCHAR:
                    case ASCII:
                        value = row.getString(columnName);
                        break;
                    case BOOLEAN:
                        value = "" + row.getBool(columnName);
                        break;
                    case INET:
                        value = row.getInet(columnName).toString();
                        break;
                    case TIMEUUID:
                    case UUID:
                        value = row.getUUID(columnName).toString();
                        break;
                    case TIMESTAMP:
                        value = row.getDate(columnName).toString();
                        break;
                    default:
                        value = "type " + column.getType().getName();
                        Logger.getLogger(this.getClass().getName()).log(Level.ERROR, "Currently not processing type " + column.getType().getName());
                }

                RowColumn rowColumn = new RowColumn(columnName, value);
                tableRow.addRowColumn(rowColumn);

            }
            retrievedRows.add(tableRow);
        }

        return retrievedRows;

    }

    /**
     * Returns first X rows that match condition
     *
     * @param querry contains the "table" and the "condition" for which I do not
     * have a better idea at the moment. Aimed at maximum freedom, the condition
     * can be any CQL3 statement,
     * http://www.datastax.com/docs/1.1/references/cql/SELECT Examples: WHERE
     * name='bob' AND score >= 40, userID = 'some_key_value', userID IN (key1,
     * key2)
     *
     * IF no maxRowCount specified on query, it returns only 1 row
     *
     * @return
     */
    @POST
    @Path("/json/table/row")
    @Consumes("application/json")
    @Produces("application/json")
    public Collection<TableRow> retrieveXRows(String querryJSON) {
        List<TableRow> retrievedRows = new ArrayList<TableRow>();
        try {
            ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally
            TableQuery tableQuery = mapper.readValue(querryJSON, TableQuery.class);

            List<Row> rows = api.selectXRowsFromTable(tableQuery);
            if (rows == null) {
                return new ArrayList<TableRow>();
            }

            for (Row row : rows) {
                TableRow tableRow = new TableRow();

                for (ColumnDefinitions.Definition column : row.getColumnDefinitions()) {
                    String columnName = column.getName();
                    String value;
                    switch (column.getType().getName()) {
                        case DOUBLE:
                            value = "" + row.getDouble(columnName);
                            break;
                        case DECIMAL:
                        case FLOAT:
                            value = "" + row.getFloat(columnName);
                            break;
                        case COUNTER:
                            value = "" + row.getLong(columnName);
                            break;
                        case INT:
                            value = "" + row.getInt(columnName);
                            break;
                        case TEXT:
                        case VARCHAR:
                        case ASCII:
                            value = row.getString(columnName);
                            break;
                        case BOOLEAN:
                            value = "" + row.getBool(columnName);
                            break;
                        case INET:
                            value = row.getInet(columnName).toString();
                            break;
                        case TIMEUUID:
                        case UUID:
                            value = row.getUUID(columnName).toString();
                            break;
                        case TIMESTAMP:
                            value = row.getDate(columnName).toString();
                            break;
                        default:
                            value = "type " + column.getType().getName();
                            Logger.getLogger(this.getClass().getName()).log(Level.ERROR, "Currently not processing type " + column.getType().getName());
                    }

                    RowColumn rowColumn = new RowColumn(columnName, value);
                    tableRow.addRowColumn(rowColumn);

                }
                retrievedRows.add(tableRow);
            }

        } catch (IOException ex) {
            Logger.getLogger(M2M.class.getName()).log(Level.ERROR, null, ex);
        }

        return retrievedRows;

    }

    /**
     *
     * @param table takes a table having a keyspace and name set on it, and for
     * all supplied columns, create an index. Does not actually check if some
     * indexes exist
     */
    @PUT
    @Path("/xml/table/index")
    @Consumes("application/xml")
    public void createIndex(Table table) {
        api.createIndex(table.getKeyspace().getName(), table.getName(), table.getColumns());
    }

    /**
     *
     * @param table takes a table having a keyspace and name set on it, and for
     * all supplied columns, deletes their indexes. Does not actually check if
     * some indexes exist
     */
    @DELETE
    @Path("/xml/table/index")
    @Consumes("application/xml")
    public void deleteIndex(Table table) {
        api.deleteIndex(table.getKeyspace().getName(), table.getName(), table.getColumns());
    }

    /**
     *
     * @param table takes a table having a keyspace and name set on it, and for
     * all supplied columns, create an index. Does not actually check if some
     * indexes exist
     */
    @PUT
    @Path("/json/table/index")
    @Consumes("application/json")
    public void createIndexJSON(String tableJSON) {
        try {
            ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally
            Table table = mapper.readValue(tableJSON, Table.class);
            api.createIndex(table.getKeyspace().getName(), table.getName(), table.getColumns());
        } catch (IOException ex) {
            Logger.getLogger(M2M.class
                    .getName()).log(Level.ERROR, null, ex);
        }
    }

    /**
     *
     * @param table takes a table having a keyspace and name set on it, and for
     * all supplied columns, deletes their indexes. Does not actually check if
     * some indexes exist
     */
    @DELETE
    @Path("/json/table/index")
    @Consumes("application/json")
    public void deleteIndexJSON(String tableJSON) {
        try {
            ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally
            Table table = mapper.readValue(tableJSON, Table.class);
            api.deleteIndex(table.getKeyspace().getName(), table.getName(), table.getColumns());
        } catch (IOException ex) {
            Logger.getLogger(M2M.class
                    .getName()).log(Level.ERROR, null, ex);
        }
    }

    /**
     * Inserts rows in table
     *
     * @param createRowsStatement contains the table and the row to be inserted
     * For the current time, we support as row values String, Double and Int.
     */
    @PUT
    @Path("/xml/table/row")
    @Consumes("application/xml")
    public void putTableRows(CreateRowsStatement createRowsStatement) {
        Table table = createRowsStatement.getTable();

        api.insertRowsInTable(table.getKeyspace().getName(), table.getName(), createRowsStatement.getRows());

        //also push this in the queue
//        try {
//            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
//            connectionFactory.setBrokerURL(System.getProperty("ActiveMQProtocol", "tcp") + "://" + System.getProperty("ActiveMQIP", "localhost") + ":" + System.getProperty("ActiveMQPort", "61616"));
//
//            Connection connection = connectionFactory.createConnection();
//            connection.start();
//            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
//            Destination destination = session.createQueue(System.getProperty("TopicName", "tcp:SensorTopic"));
//            MessageProducer producer = session.createProducer(destination);
//            producer.setDeliveryMode(DeliveryMode.PERSISTENT);
//
//            for (TableRow row : createRowsStatement.getRows()) {
//                TextMessage message = session.createTextMessage(row.toString());
//                producer.send(message);
//            }
//            session.close();
//            connection.close();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
    }

    /**
     * Inserts rows in table
     *
     * @param createRowsStatement contains the table and the row to be inserted
     * For the current time, we support as row values String, Double and Int.
     */
    @PUT
    @Path("/json/table/row")
    @Consumes("application/json")
    public void putTableRowsJSON(String createRowsStatementJSON) {
        try {
            ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally
            CreateRowsStatement createRowsStatement = mapper.readValue(createRowsStatementJSON, CreateRowsStatement.class);

            Table table = createRowsStatement.getTable();

            api.insertRowsInTable(table.getKeyspace().getName(), table.getName(), createRowsStatement.getRows());

            //also push this in the queue
//
//            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
//            connectionFactory.setBrokerURL(System.getProperty("ActiveMQProtocol", "tcp") + "://" + System.getProperty("ActiveMQIP", "localhost") + ":" + System.getProperty("ActiveMQPort", "61616"));
//
//            Connection connection = connectionFactory.createConnection();
//            connection.start();
//            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
//            Destination destination = session.createQueue(System.getProperty("TopicName", "tcp:SensorTopic"));
//            MessageProducer producer = session.createProducer(destination);
//            producer.setDeliveryMode(DeliveryMode.PERSISTENT);
//
//            for (TableRow row : createRowsStatement.getRows()) {
//                TextMessage message = session.createTextMessage(row.toString());
//                producer.send(message);
//            }
//            session.close();
//            connection.close();
        } catch (IOException ex) {
            Logger.getLogger(M2M.class
                    .getName()).log(Level.ERROR, null, ex);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Deletes rows in table for which the condition is true
     *
     * @param querry contains the "table" and the "condition" for which I do not
     * have a better idea at the moment. Aimed at maximum freedom, the condition
     * can be any CQL3 statement,
     * http://www.datastax.com/docs/1.1/references/cql/SELECT Examples: WHERE
     * name='bob' AND score = 40; userID = 'some_key_value'; userID IN (key1,
     * key2);
     *
     * NOTE: FIRST clause in WHERE must include the PRIMARY KEY NO <,> (LT,GT)
     * are supported
     *
     */
    @DELETE
    @Path("/xml/table/row")
    @Consumes("application/xml")
    public void deleteTableRows(TableQuery querry) {
        api.deleteRowsFromTable(querry);
    }

    /**
     * Deletes rows in table for which the condition is true
     *
     * @param querry contains the "table" and the "condition" for which I do not
     * have a better idea at the moment. Aimed at maximum freedom, the condition
     * can be any CQL3 statement,
     * http://www.datastax.com/docs/1.1/references/cql/SELECT Examples: WHERE
     * name='bob' AND score = 40; userID = 'some_key_value'; userID IN (key1,
     * key2);
     *
     * NOTE: FIRST clause in WHERE must include the PRIMARY KEY NO <,> (LT,GT)
     * are supported
     *
     */
    @DELETE
    @Path("/json/table/row")
    @Consumes("application/json")
    public void deleteTableRowsJSON(String querryJSON) {
        try {
            ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally
            TableQuery tableQuery = mapper.readValue(querryJSON, TableQuery.class);
            api.deleteRowsFromTable(tableQuery);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @GET
    @Path("/monitoring")
    @Produces("application/xml")
    public MonitoringData getMonitoringInfo() {
        return api.getMonitoringData();
    }

}
