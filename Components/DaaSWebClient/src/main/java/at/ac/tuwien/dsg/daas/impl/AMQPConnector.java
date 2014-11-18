package at.ac.tuwien.dsg.daas.impl;

import at.ac.tuwien.dsg.daas.DataManagementAPI;
import at.ac.tuwien.dsg.daas.entities.Column;
import at.ac.tuwien.dsg.daas.entities.CreateRowsStatement;
import at.ac.tuwien.dsg.daas.entities.Keyspace;
import at.ac.tuwien.dsg.daas.entities.Table;
import at.ac.tuwien.dsg.daas.entities.TableQuery;
import at.ac.tuwien.dsg.daas.entities.TableRow;
import com.datastax.driver.core.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class AMQPConnector implements DataManagementAPI {

    static final Logger log = LoggerFactory.getLogger(AMQPConnector.class);

    @Autowired
    private JmsTemplate jmsTemplate;

    private <T> String marshal(Object source, Class<T> configurationClass) throws JAXBException {
        JAXBContext jAXBContext = JAXBContext.newInstance(configurationClass);
        StringWriter writer = new StringWriter();
        jAXBContext.createMarshaller().marshal(source, writer);
        return writer.toString();
    }

    private void sendMessage(final String key, final String value) {
        jmsTemplate.send(new MessageCreator() {
            public Message createMessage(Session session) throws JMSException {
                MapMessage message = session.createMapMessage();
                message.setObject(key, value);
                log.info("Sending message (key/value): {}/{}", key, value);
                return message;
            }
        });
    }

    public void openConnection() {

    }

    public void closeConnection() {

    }

    public void createKeyspace(Keyspace keyspace) {
        try {
            sendMessage("CREATE_KEYSPACE", marshal(keyspace, Keyspace.class));
        } catch (JAXBException ex) {
            log.error("Unable to marshall object of class " + keyspace.getClass() + " into String", ex);
        }
    }

    public List<Keyspace> listKeyspaces() {
        return new ArrayList<Keyspace>();
    }

    public void dropKeyspace(Keyspace keyspace) {
        try {
            sendMessage("DROP_KEYSPACE", marshal(keyspace, Keyspace.class));
        } catch (JAXBException ex) {
            log.error("Unable to marshall object of class " + keyspace.getClass() + " into String", ex);
        }
    }

    public void createTable(Table table) {
        try {
            sendMessage("CREATE_TABLE", marshal(table, Table.class));
        } catch (JAXBException ex) {
            log.error("Unable to marshall object of class " + table.getClass() + " into String", ex);
        }
    }

    public void createIndex(String keyspaceName, String tableName, Collection<Column> columns) {
        Table table = new Table().withKeyspace(new Keyspace(keyspaceName)).withName(tableName).withColumns(columns);
        try {
            sendMessage("CREATE_INDEX", marshal(table, Table.class));
        } catch (JAXBException ex) {
            log.error("Unable to marshall object of class " + table.getClass() + " into String", ex);
        }
    }

    public void deleteIndex(String keyspaceName, String tableName, Collection<Column> columns) {
        Table table = new Table().withKeyspace(new Keyspace(keyspaceName)).withName(tableName).withColumns(columns);
        try {
            sendMessage("DELETE_INDEX", marshal(table, Table.class));
        } catch (JAXBException ex) {
            log.error("Unable to marshall object of class " + table.getClass() + " into String", ex);
        }
    }

    public void dropTable(Table table) {
        try {
            sendMessage("DROP_TABLE", marshal(table, Table.class));
        } catch (JAXBException ex) {
            log.error("Unable to marshall object of class " + table.getClass() + " into String", ex);
        }
    }

    public TableRow selectOneRowFromTable(String keyspaceName, String tableName, String condition) {
        return null;
    }

    public List<TableRow> selectXRowsFromTable(TableQuery querry) {
        return new ArrayList<TableRow>();
    }

    public void insertRowsInTable(String keyspaceName, String tableName, Collection<TableRow> rows) {
        CreateRowsStatement createRowsStatement = new CreateRowsStatement();
        createRowsStatement.setTable(new Table().withKeyspace(new Keyspace(keyspaceName)).withName(tableName));
        createRowsStatement.setRows(rows);
        try {
            sendMessage("INSERT_ROWS", marshal(createRowsStatement, CreateRowsStatement.class));
        } catch (JAXBException ex) {
            log.error("Unable to marshall object of class " + createRowsStatement.getClass() + " into String", ex);
        }
    }

    public void updateRowInTable(String keyspaceName, String tableName, Map<String, Object> newData, String condition) {

    }

    public void deleteRowsFromTable(TableQuery query) {
        try {
            sendMessage("DELETE_ROWS", marshal(query, TableQuery.class));
        } catch (JAXBException ex) {
            log.error("Unable to marshall object of class " + query.getClass() + " into String", ex);
        }
    }

}
