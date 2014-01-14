/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 * Cassandra client built using Cassandra JDBC client and CQL3 (cassandra querry language 3) http://www.datastax.com/docs/1.1/references/cql/index
 * NOT SQL Injection SAFE
 */
package at.ac.tuwien.dsg.daas.impl;

import at.ac.tuwien.dsg.daas.DataManagementAPI;
import at.ac.tuwien.dsg.daas.DataManagementAPIFactory;
import at.ac.tuwien.dsg.daas.entities.Column;
import at.ac.tuwien.dsg.daas.entities.Keyspace;
import at.ac.tuwien.dsg.daas.entities.RowColumn;
import at.ac.tuwien.dsg.daas.entities.Table;
import at.ac.tuwien.dsg.daas.entities.TableQuery;
import at.ac.tuwien.dsg.daas.entities.TableRow;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ExecutionInfo;
import com.datastax.driver.core.Host;
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * Uses DataStax Java Driver for Apache Cassandra https://github.com/datastax/java-driver 
 * and CQL 3 for query http://www.datastax.com/docs/1.1/references/cql/index
 *
 * Author: Daniel Moldovan Institution: Vienna University of Technology
 */
public class CassandraManagementAPI implements DataManagementAPI{

    private String cassandraHostIP;
    private int casandraPort;
//    private Cluster cluster;
    private Session session;

    protected CassandraManagementAPI(String cassandraHostIP, int casandraPort) {
        this.cassandraHostIP = cassandraHostIP;
        this.casandraPort = casandraPort;
    }

    public void openConnection() {
    	 Logger.getLogger(DataManagementAPIFactory.class).log(Level.INFO, "Session opened");
        if (session == null) {
            Cluster cluster = Cluster.builder()
                    .withPort(casandraPort)
                    .addContactPoint(cassandraHostIP).build();
            Metadata metadata = cluster.getMetadata();

            //print cassandra cluster status
            System.out.printf("Connected to cluster: %s\n",
                    metadata.getClusterName());
            for (Host host : metadata.getAllHosts()) {
                System.out.printf("Datacenter: %s; Host: %s; Rack: %s\n",
                        host.getDatacenter(), host.getAddress(), host.getRack());
            }
            session = cluster.connect();
           
        }
    }

    public void closeConnection() {
        session.shutdown();
        session = null;
    }

    public synchronized void createKeyspace(Keyspace keyspace) {
        String createKeyspaceStatement = "CREATE KEYSPACE " + keyspace.getName() + " WITH replication "
                + "= {'class':'SimpleStrategy', 'replication_factor':3};";

        ResultSet resultSet = session.execute(createKeyspaceStatement);
        ExecutionInfo info = resultSet.getExecutionInfo();
    }

    public synchronized void dropKeyspace(Keyspace keyspace) {
        String createKeyspaceStatement = "DROP KEYSPACE " + keyspace.getName() + ";";
        ResultSet resultSet = session.execute(createKeyspaceStatement);
        ExecutionInfo info = resultSet.getExecutionInfo();

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
     * Column type list CQL3 data type | Java type ascii	| java.lang.String
     * bigint	| long blob	| java.nio.ByteBuffer boolean	| boolean counter	| long
     * decimal	| float double	| double float	| float inet	| Java.net.InetAddress
     * int	| int list	| java.util.List<T> map	| java.util.Map<K, V> set	|
     * java.util.Set<T> text	| java.lang.String timestamp | java.util.Date
     * timeuuid	| java.util.UUID uuid | java.util.UUID varchar	|
     * java.lang.String varint	| java.math.BigInteger
     *
     */
    public synchronized void createTable(Table table) {

        Map<String, String> columnsMap = new HashMap<String, String>();
        for (Column column : table.getColumns()) {
            columnsMap.put(column.getName(), column.getType());
        }

        //build create table statement
        String createTableStatement = "CREATE TABLE " + table.getKeyspace().getName() + "." + table.getName() + "(";

        //insert primary key definition line
        createTableStatement += table.getPrimaryKeyName() + " " + table.getPrimaryKeyType() + " PRIMARY KEY";

        //insert the definition of the other columns
        for (String columnName : columnsMap.keySet()) {
            createTableStatement += "," + columnName + " " + columnsMap.get(columnName);
        }

        //close the table definition
        createTableStatement += ");";

        ResultSet resultSet = session.execute(createTableStatement);
        ExecutionInfo info = resultSet.getExecutionInfo();
    }

    /**
     *
     * @param keyspaceName
     * @param tableName
     * @param columns creates an index on ALLL supplied columns
     */
    public synchronized void createIndex(String keyspaceName, String tableName, Collection<Column> columns) {
        for (Column column : columns) {
            //build create table statement
            String createIndexStatement = "CREATE INDEX " + column.getName() + "Index ON " + keyspaceName + "." + tableName + " ( " + column.getName() + ");";

            ResultSet resultSet = session.execute(createIndexStatement);
            ExecutionInfo info = resultSet.getExecutionInfo();
        }
    }

    /**
     *
     * @param keyspaceName
     * @param tableName
     * @param columns deletes indexes from ALLL supplied columns
     */
    public synchronized void deleteIndex(String keyspaceName, String tableName, Collection<Column> columns) {
        for (Column column : columns) {
            //build create table statement
            String useKeyspaceStatement = "use " + keyspaceName + ";";
            String createIndexStatement = "DROP INDEX " + column.getName() + "Index ;";

            session.execute(useKeyspaceStatement);
            ResultSet resultSet = session.execute(createIndexStatement);
            ExecutionInfo info = resultSet.getExecutionInfo();
        }
    }

    public synchronized void dropTable(Table table) {

        //build create table statement
        String dropTableStatement = "DROP TABLE " + table.getKeyspace().getName() + "." + table.getName() + ";";

        ResultSet resultSet = session.execute(dropTableStatement);
        ExecutionInfo info = resultSet.getExecutionInfo();
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
    public synchronized Row selectOneRowFromTable(String keyspaceName, String tableName, String condition) {
        String selectCommand = "SELECT * FROM " + keyspaceName + "." + tableName;
        if (condition != null && condition.length() > 0) {
            selectCommand += " WHERE " + condition;
        }
        selectCommand += " LIMIT 1"; //the LIMIT indicates how many ROWS to retrieve in a  querry

        ResultSet resultSet = session.execute(selectCommand);
        ExecutionInfo info = resultSet.getExecutionInfo();

        return (resultSet.isExhausted()) ? null : resultSet.one();
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
    public synchronized List<Row> selectXRowsFromTable(TableQuery querry) {
        Table table = querry.getTable();
        String condition = querry.getCondition();
        int maxRows = querry.getMaxResultCount();
        String selectCommand = "SELECT * FROM " + table.getKeyspace().getName() + "." + table.getName();
        if (condition != null && condition.length() > 0) {
            selectCommand += " WHERE " + condition;
        }

        if (maxRows != 0) {
            selectCommand += " LIMIT " + maxRows + ";"; //the LIMIT indicates how many ROWS to retrieve in a  querry
        }

        ResultSet resultSet = session.execute(selectCommand);
        ExecutionInfo info = resultSet.getExecutionInfo();

        return (resultSet.isExhausted()) ? null : resultSet.all();
    }

    /**
     *
     * @param keyspaceName
     * @param tableName
     * @param rows each row is a map of String,DataType containing
     */
    public synchronized void insertRowsInTable(String keyspaceName, String tableName, Collection<TableRow> rows) {

        //build create table statement

        for (TableRow row : rows) {
            String insertStatement = "INSERT INTO " + keyspaceName + "." + tableName;
            String values = "";
            String columns = "";
            for (RowColumn column : row.getValues()) {
                columns += column.getName() + ",";
                values += column.getValue() + ",";
            }
            //eliminate the last ","
            columns = columns.substring(0, columns.length() - 1);
            values = values.substring(0, values.length() - 1);
            insertStatement += " ( " + columns + ") " + " VALUES (" + values + ")";
            session.execute(insertStatement);

        }

    }

    /**
     *
     * @param keyspaceName
     * @param tableName
     * @param rows each row is a map of String,Value containing columnName=value
     * pairs
     */
    public synchronized void updateRowInTable(String keyspaceName, String tableName, Map<String, Object> newData, String condition) {

        //build create table statement

        String updateStatement = "UPDATE " + keyspaceName + "." + tableName + " USING CONSISTENCY QUORUM SET";
        for (String columnName : newData.keySet()) {
            updateStatement += "SET '" + columnName + "' = '" + newData.get(columnName).toString() + "',";
        }

        //clear the last '
        updateStatement = updateStatement.substring(0, updateStatement.length() - 1);

        if (condition != null && condition.length() > 0) {
            updateStatement += " WHERE " + condition + " ;";
        }

        session.execute(updateStatement);

    }
    
    public List<Row> listKeyspaces() {
    	 
    	String selectCommand = "DESCRIBE keyspaces;";
        ResultSet resultSet = session.execute(selectCommand);
        ExecutionInfo info = resultSet.getExecutionInfo();

        return (resultSet.isExhausted()) ? null : resultSet.all();
        
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
    public synchronized void deleteRowsFromTable(TableQuery query) {
        String condition = query.getCondition();
        Table table = query.getTable();

        String selectCommand = "DELETE FROM " + table.getKeyspace().getName() + "." + table.getName();
        if (condition != null && condition.length() > 0) {
            selectCommand += " WHERE " + condition + ";";
        }

        ResultSet resultSet = session.execute(selectCommand);
        ExecutionInfo info = resultSet.getExecutionInfo();

    }

    public String getCassandraHostIP() {
        return "" + cassandraHostIP;
    }

    public int getCasandraPort() {
        return 0 + casandraPort;
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

	
}
