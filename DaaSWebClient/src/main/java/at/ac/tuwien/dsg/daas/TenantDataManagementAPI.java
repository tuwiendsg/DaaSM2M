/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 * Cassandra client built using Cassandra JDBC client and CQL3 (cassandra querry language 3) http://www.datastax.com/docs/1.1/references/cql/index
 * NOT SQL Injection SAFE
 */
package at.ac.tuwien.dsg.daas;

import at.ac.tuwien.dsg.daas.config.CassandraAccessProperties;
import at.ac.tuwien.dsg.daas.entities.Column;
import at.ac.tuwien.dsg.daas.entities.Keyspace;
import at.ac.tuwien.dsg.daas.entities.Table;
import at.ac.tuwien.dsg.daas.entities.TableQuery;
import at.ac.tuwien.dsg.daas.entities.TableRow;
import at.ac.tuwien.dsg.daas.DaaSDelegate.MonitoringData;
import com.datastax.driver.core.Row;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import javax.ws.rs.PathParam;

/**
 *
 * Author: Daniel Moldovan Institution: Vienna University of Technology
 */
public interface TenantDataManagementAPI {
public  void addTenant (String tenant, String host, String port);
     public  void addTenant (String tenant);
    public void removeTenant(String tenant);
    public void openConnection(String tenant);

    public void closeConnection(String tenant);

    public void createKeyspace(String tenant, Keyspace keyspace);
    public List<Row> listKeyspaces(String tenant);

    public void dropKeyspace(String tenant, Keyspace keyspace);

    public void createTable(String tenant, Table table);

    public void createIndex(String tenant, String keyspaceName, String tableName, Collection<Column> columns);

    public void deleteIndex(String tenant, String keyspaceName, String tableName, Collection<Column> columns);

    public void dropTable(String tenant, Table table);

    public Row selectOneRowFromTable(String tenant, String keyspaceName, String tableName, String condition);

    public List<Row> selectXRowsFromTable(String tenant, TableQuery querry);

    public void insertRowsInTable(String tenant, String keyspaceName, String tableName, Collection<TableRow> rows);

    public void updateRowInTable(String tenant, String keyspaceName, String tableName, Map<String, Object> newData, String condition);

    public void deleteRowsFromTable(String tenant, TableQuery query);

    public String getCassandraHostIP(String tenant);

    public int getCasandraPort(String tenant);
 
        public MonitoringData getMonitoringInfo(String id);
    public MonitoringData getMonitoringInfo();
}
