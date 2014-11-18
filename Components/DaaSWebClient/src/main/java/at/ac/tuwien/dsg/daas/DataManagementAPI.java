/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 * Cassandra client built using Cassandra JDBC client and CQL3 (cassandra querry language 3) http://www.datastax.com/docs/1.1/references/cql/index
 * NOT SQL Injection SAFE
 */
package at.ac.tuwien.dsg.daas;

import at.ac.tuwien.dsg.daas.entities.Column;
import at.ac.tuwien.dsg.daas.entities.Keyspace;
import at.ac.tuwien.dsg.daas.entities.Table;
import at.ac.tuwien.dsg.daas.entities.TableQuery;
import at.ac.tuwien.dsg.daas.entities.TableRow;
import com.datastax.driver.core.Row;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 *
 * Author: Daniel Moldovan Institution: Vienna University of Technology
 */
public interface DataManagementAPI {

    public void openConnection();

    public void closeConnection();

    public void createKeyspace(Keyspace keyspace);

    public List<Keyspace> listKeyspaces();

    public void dropKeyspace(Keyspace keyspace);

    public void createTable(Table table);

    public void createIndex(String keyspaceName, String tableName, Collection<Column> columns);

    public void deleteIndex(String keyspaceName, String tableName, Collection<Column> columns);

    public void dropTable(Table table);

    public TableRow selectOneRowFromTable(String keyspaceName, String tableName, String condition);

    public List<TableRow> selectXRowsFromTable(TableQuery querry);

    public void insertRowsInTable(String keyspaceName, String tableName, Collection<TableRow> rows);

    public void updateRowInTable(String keyspaceName, String tableName, Map<String, Object> newData, String condition);

    public void deleteRowsFromTable(TableQuery query);
}
