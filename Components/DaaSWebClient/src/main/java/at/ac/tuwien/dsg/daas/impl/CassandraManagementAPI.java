/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 * Cassandra client built using Cassandra JDBC client and CQL3 (cassandra querry language 3) http://www.datastax.com/docs/1.1/references/cql/index
 * NOT SQL Injection SAFE
 */
package at.ac.tuwien.dsg.daas.impl;

import at.ac.tuwien.dsg.daas.DataManagementAPI;
//import at.ac.tuwien.dsg.daas.DataManagementAPIFactory;
import at.ac.tuwien.dsg.daas.entities.Column;
import at.ac.tuwien.dsg.daas.entities.Keyspace;
import at.ac.tuwien.dsg.daas.entities.RowColumn;
import at.ac.tuwien.dsg.daas.entities.Table;
import at.ac.tuwien.dsg.daas.entities.TableQuery;
import at.ac.tuwien.dsg.daas.entities.TableRow;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ColumnDefinitions;
import static com.datastax.driver.core.DataType.Name.ASCII;
import static com.datastax.driver.core.DataType.Name.BOOLEAN;
import static com.datastax.driver.core.DataType.Name.COUNTER;
import static com.datastax.driver.core.DataType.Name.DECIMAL;
import static com.datastax.driver.core.DataType.Name.DOUBLE;
import static com.datastax.driver.core.DataType.Name.FLOAT;
import static com.datastax.driver.core.DataType.Name.INET;
import static com.datastax.driver.core.DataType.Name.INT;
import static com.datastax.driver.core.DataType.Name.TEXT;
import static com.datastax.driver.core.DataType.Name.TIMESTAMP;
import static com.datastax.driver.core.DataType.Name.TIMEUUID;
import static com.datastax.driver.core.DataType.Name.VARCHAR;
import com.datastax.driver.core.ExecutionInfo;
import com.datastax.driver.core.Host;
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import java.util.ArrayList;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;

import org.slf4j.LoggerFactory;

/**
 * Uses DataStax Java Driver for Apache Cassandra
 * https://github.com/datastax/java-driver and CQL 3 for query
 * http://www.datastax.com/docs/1.1/references/cql/index
 *
 * Author: Daniel Moldovan Institution: Vienna University of Technology
 */
//@Component
public class CassandraManagementAPI implements DataManagementAPI {

    static final org.slf4j.Logger log = LoggerFactory.getLogger(CassandraManagementAPI.class);

//    @Value("${CassandraNode.IP:localhost}")
    private String cassandraHostIP;

//    @Value("${CassandraNode.PORT:9042}")
    private int casandraPort;
    // private Cluster cluster;
    private Session session;

    protected CassandraManagementAPI(String cassandraHostIP, int casandraPort) {
        this.cassandraHostIP = cassandraHostIP;
        this.casandraPort = casandraPort;
    }

    public CassandraManagementAPI() {
    }

    @PostConstruct
    public void init() {
        this.openConnection();
    }

    public void openConnection() {
        log.info("Connecting to Cassandra at " + cassandraHostIP + ":" + casandraPort);
        if (session == null) {
            Cluster cluster = Cluster.builder()
                    // .withPort(casandraPort)
                    .addContactPoint(cassandraHostIP).build();
            Metadata metadata = cluster.getMetadata();

            // print cassandra cluster status
            System.out.printf("Connected to cluster: %s\n", metadata.getClusterName());
            for (Host host : metadata.getAllHosts()) {
                System.out.printf("Datacenter: %s; Host: %s; Rack: %s\n", host.getDatacenter(), host.getAddress(), host.getRack());
            }
            session = cluster.connect();

        }
    }

    public void closeConnection() {
        session.close();
        session = null;
    }

    public void createKeyspace(Keyspace keyspace) {
        String createKeyspaceStatement = "CREATE KEYSPACE " + keyspace.getName() + " WITH replication "
                + "= {'class':'SimpleStrategy', 'replication_factor':3};";

        ResultSet resultSet = null;

        try {
            resultSet = session.execute(createKeyspaceStatement);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            log.error("Exception cause: " + createKeyspaceStatement);
        }

//        ExecutionInfo info = resultSet.getExecutionInfo();
    }

    public void dropKeyspace(Keyspace keyspace) {
        String createKeyspaceStatement = "DROP KEYSPACE " + keyspace.getName() + ";";

        ResultSet resultSet = null;

        try {
            resultSet = session.execute(createKeyspaceStatement);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            log.error("Exception cause: " + createKeyspaceStatement);
        }

    }

    /**
     *
     * @param keyspaceName
     * @param tableName
     * @param primaryKeyName = usually ID
     * @param primaryKeyType usually UUID but can also be string (less
     * performance) or anything else
     * @param columns a map containing as Key the column NAME and as value the
     * column CQL3 data type
     *
     */
    // Column type list
    // CQL3 data type | Java type
    // ascii | java.lang.String
    // bigint | long
    // blob | java.nio.ByteBuffer
    // boolean | boolean
    // counter | long
    // decimal | float
    // double | double
    // float | float
    // inet | Java.net.InetAddress
    // int | int
    // list | java.util.List<T>
    // map | java.util.Map<K, V>
    // set | java.util.Set<T>
    // text | java.lang.String
    // timestamp | java.util.Date
    // timeuuid | java.util.UUID
    // uuid | java.util.UUID
    // varchar | java.lang.String
    // varint | java.math.BigInteger
    public void createTable(Table table) {

        Map<String, String> columnsMap = new HashMap<String, String>();
        for (Column column : table.getColumns()) {
            columnsMap.put(column.getName(), column.getType());
        }

        // build create table statement
        String createTableStatement = "CREATE TABLE " + table.getKeyspace().getName() + "." + table.getName() + "(";

        // insert primary key definition line
        createTableStatement += table.getPrimaryKeyName() + " " + table.getPrimaryKeyType() + " PRIMARY KEY";

        // insert the definition of the other columns
        for (String columnName : columnsMap.keySet()) {
            createTableStatement += "," + columnName + " " + columnsMap.get(columnName);
        }

        // close the table definition
        createTableStatement += ");";
        log.debug(createTableStatement);

        ResultSet resultSet = null;
        try {
            resultSet = session.execute(createTableStatement);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            log.error("Exception cause: " + createTableStatement);
        }
    }

    /**
     *
     * @param keyspaceName
     * @param tableName
     * @param columns creates an index on ALLL supplied columns
     */
    public void createIndex(String keyspaceName, String tableName, Collection<Column> columns) {
        for (Column column : columns) {
            // build create table statement
            String createIndexStatement = "CREATE INDEX " + column.getName() + "Index ON " + keyspaceName + "." + tableName + " ( " + column.getName() + ");";

            ResultSet resultSet = null;
            try {
                resultSet = session.execute(createIndexStatement);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                log.error("Exception cause: " + createIndexStatement);
            }

        }
    }

    /**
     *
     * @param keyspaceName
     * @param tableName
     * @param columns deletes indexes from ALLL supplied columns
     */
    public void deleteIndex(String keyspaceName, String tableName, Collection<Column> columns) {
        for (Column column : columns) {
            // build create table statement
            String useKeyspaceStatement = "use " + keyspaceName + ";";
            String createIndexStatement = "DROP INDEX " + column.getName() + "Index ;";

            try {
                session.execute(useKeyspaceStatement);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                log.error("Exception cause: " + useKeyspaceStatement);
            }

            try {
                session.execute(createIndexStatement);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                log.error("Exception cause: " + createIndexStatement);
            }
        }
    }

    public void dropTable(Table table) {

        // build create table statement
        String dropTableStatement = "DROP TABLE " + table.getKeyspace().getName() + "." + table.getName() + ";";

        try {
            session.execute(dropTableStatement);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            log.error("Exception cause: " + dropTableStatement);
        }
    }

    /**
     * Select ALL columns from one row.
     *
     * @param keyspaceName
     * @param tableName
     * @param condition I do not have a better idea at the moment. Aimed at
     * maximum freedom, the condition can be any CQL3 statement,
     * http://www.datastax.com/docs/1.1/references/cql/SELECT
     * NOTE!!!!!!!!!!!!!!!!!!!: FIRST clause in WHERE must include the PRIMARY
     * KEY NO <,> (LT,GT) are supported
     *
     * Example: name='bob' AND score>= 40;
     * @return a Row if the query returned anything, or null otherwise Can be
     * used to select first row by leaving condition null, get Timestamp from
     * it, use it in select "where timestamp smaller timestamp of retrieved
     * previousely" to get the next event in line, and so on until it returns
     * nothing.
     */
    public TableRow selectOneRowFromTable(String keyspaceName, String tableName, String condition) {
        String selectCommand = "SELECT * FROM " + keyspaceName + "." + tableName;
        if (condition != null && condition.length() > 0) {
            selectCommand += " WHERE " + condition;
        }
        selectCommand += " LIMIT 1"; // the LIMIT indicates how many ROWS to
        // retrieve in a querry

        ResultSet resultSet = null;

        try {
            resultSet = session.execute(selectCommand);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            log.error("Exception cause: " + selectCommand);
        }

        if (resultSet == null || resultSet.isExhausted()) {
            return new TableRow();
        } else {
            Row row = resultSet.one();
            return convertRow(row);
        }
    }

    /**
     * Select ALL columns from one row.
     *
     * @param keyspaceName
     * @param tableName
     * @param maxRowsToRetrieve it limits the Nr. of Rows returned (if 1 row
     * required you can specify 1, for example to retrieve first row from
     * database)
     * @param condition I do not have a better idea at the moment.
     *
     * Aimed at maximum freedom, the condition can be any CQL3 statement,
     * http://www.datastax.com/docs/1.1/references/cql/SELECT
     * NOTE!!!!!!!!!!!!!!!!!!!: FIRST clause in WHERE must include the PRIMARY
     * KEY NO <,> (LT,GT) are supported
     *
     * Example: name='bob' AND score = 40 userID = 'some_key_value' userID IN
     * (key1, key2)
     *
     * @return a List of max rowCount rows if the query returned anything, or
     * null otherwise Can be used to select first row by leaving condition null,
     * get Timestamp from it, use it in select "where timestamp smaller
     * timestamp of retrieved previousely" to get the next event in line, and so
     * on until it returns nothing.
     *
     * Cassandra query could also be done trough API for example:
     * QueryBuilder.select().all().from(tableName).where()...
     *
     */
    public List<TableRow> selectXRowsFromTable(TableQuery querry) {
        Table table = querry.getTable();
        String condition = querry.getCondition();
        int maxRows = querry.getMaxResultCount();
        String selectCommand = "SELECT * FROM " + table.getKeyspace().getName() + "." + table.getName();
        if (condition != null && condition.length() > 0) {
            selectCommand += " WHERE " + condition;
        }

        if (maxRows != 0) {
            selectCommand += " LIMIT " + maxRows + ";"; // the LIMIT indicates
            // how many ROWS to
            // retrieve in a querry
        }

        ResultSet resultSet = null;
        try {
            resultSet = session.execute(selectCommand);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            log.error("Exception cause: " + selectCommand);
        }
        ExecutionInfo info = resultSet.getExecutionInfo();

        if (resultSet == null || resultSet.isExhausted()) {
            return new ArrayList<TableRow>();
        } else {
            List<Row> rows = resultSet.all();
            List<TableRow> tRows = new ArrayList<TableRow>();
            for (Row row : rows) {
                tRows.add(convertRow(row));
            }
            return tRows;
        }

    }

    /**
     *
     * @param keyspaceName
     * @param tableName
     * @param rows each row is a map of String,DataType containing
     */
    public void insertRowsInTable(String keyspaceName, String tableName, Collection<TableRow> rows) {

        // build create table statement
        for (TableRow row : rows) {
            String insertStatement = "INSERT INTO " + keyspaceName + "." + tableName;
            String values = "";
            String columns = "";
            for (RowColumn column : row.getValues()) {
                columns += column.getName() + ",";
                values += column.getValue() + ",";
            }
            // eliminate the last ","
            columns = columns.substring(0, columns.length() - 1);
            values = values.substring(0, values.length() - 1);
            insertStatement += " ( " + columns + ") " + " VALUES (" + values + ")";
            try {
                session.execute(insertStatement);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                log.error("Exception cause: " + insertStatement);
            }
        }

    }

    /**
     *
     * @param keyspaceName
     * @param tableName
     * @param rows each row is a map of String,Value containing columnName=value
     * pairs
     */
    public void updateRowInTable(String keyspaceName, String tableName, Map<String, Object> newData, String condition) {

        // build create table statement
        String updateStatement = "UPDATE " + keyspaceName + "." + tableName + " USING CONSISTENCY QUORUM SET";
        for (String columnName : newData.keySet()) {
            updateStatement += "SET '" + columnName + "' = '" + newData.get(columnName).toString() + "',";
        }

        // clear the last '
        updateStatement = updateStatement.substring(0, updateStatement.length() - 1);

        if (condition != null && condition.length() > 0) {
            updateStatement += " WHERE " + condition + " ;";
        }

        ResultSet resultSet = null;

        try {
            resultSet = session.execute(updateStatement);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            log.error("Exception cause: " + updateStatement);
        }

    }

    public List<Keyspace> listKeyspaces() {

//        String selectCommand = "DESCRIBE keyspaces;";
//        ResultSet resultSet = null;
//
//        try {
//            resultSet = session.execute(selectCommand);
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//            log.error("Exception cause: " + selectCommand);
//        }
//
//        if (resultSet == null || resultSet.isExhausted()) {
//            return new ArrayList<Keyspace>();
//        } else {
//            List<Row> rows = resultSet.all();
////            List<Keyspace> keyspaces = new ArrayList<TableRow>();
////            for (Row row : rows) {
////                tRows.add(convertRow(row));
////            }
////            return tRows;
//            
//        }
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     *
     * @param keyspaceName
     * @param tableName
     * @param condition I do not have a better idea at the moment.
     *
     * Aimed at maximum freedom, the condition can be any CQL3 statement,
     * http://www.datastax.com/docs/1.1/references/cql/SELECT Example:
     * name='bob' AND score >= 40 userID = 'some_key_value' userID IN (key1,
     * key2)
     */
    public void deleteRowsFromTable(TableQuery query) {
        String condition = query.getCondition();
        Table table = query.getTable();

        String selectCommand = "DELETE FROM " + table.getKeyspace().getName() + "." + table.getName();
        if (condition != null && condition.length() > 0) {
            selectCommand += " WHERE " + condition + ";";
        }

        ResultSet resultSet = null;

        try {
            resultSet = session.execute(selectCommand);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            log.error("Exception cause: " + selectCommand);
        }

    }

    public String getCassandraHostIP() {
        return cassandraHostIP;
    }

    public void setCassandraHostIP(String cassandraHostIP) {
        this.cassandraHostIP = cassandraHostIP;
    }

    public int getCasandraPort() {
        return casandraPort;
    }

    public void setCasandraPort(int casandraPort) {
        this.casandraPort = casandraPort;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + (this.cassandraHostIP != null ? this.cassandraHostIP.hashCode() : 0);
        hash = 59 * hash + this.casandraPort;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CassandraManagementAPI other = (CassandraManagementAPI) obj;
        if ((this.cassandraHostIP == null) ? (other.cassandraHostIP != null) : !this.cassandraHostIP.equals(other.cassandraHostIP)) {
            return false;
        }
        if (this.casandraPort != other.casandraPort) {
            return false;
        }
        return true;
    }

    private static TableRow convertRow(Row row) {

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
                    log.error("Currently not processing type " + column.getType().getName());

            }

            RowColumn rowColumn = new RowColumn(columnName, value);
            tableRow.addRowColumn(rowColumn);

        }

        return tableRow;
    }

}
