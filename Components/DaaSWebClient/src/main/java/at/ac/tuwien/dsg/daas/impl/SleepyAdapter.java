/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.daas.impl;

import at.ac.tuwien.dsg.daas.DataManagementAPI;
import at.ac.tuwien.dsg.daas.entities.Column;
import at.ac.tuwien.dsg.daas.entities.Keyspace;
import at.ac.tuwien.dsg.daas.entities.Table;
import at.ac.tuwien.dsg.daas.entities.TableQuery;
import at.ac.tuwien.dsg.daas.entities.TableRow;
import com.datastax.driver.core.Row;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

/**
 *
 * @author Daniel Moldovan E-Mail: d.moldovan@dsg.tuwien.ac.at
 */
public class SleepyAdapter implements DataManagementAPI {

    static final Logger log = LoggerFactory.getLogger(SleepyAdapter.class);

   
    private int sleepyTimeInMilliseconds = 1000;

    public int getSleepyTimeInMilliseconds() {
        return sleepyTimeInMilliseconds;
    }

    public void setSleepyTimeInMilliseconds(int sleepyTimeInMilliseconds) {
        this.sleepyTimeInMilliseconds = sleepyTimeInMilliseconds;
    }

    public void openConnection() {
    }

    public void closeConnection() {
    }

    public void createKeyspace(Keyspace keyspace) {
    }

    public List<Keyspace> listKeyspaces() {
        return new ArrayList<Keyspace>();
    }

    public void dropKeyspace(Keyspace keyspace) {
    }

    public void createTable(Table table) {
    }

    public void createIndex(String keyspaceName, String tableName, Collection<Column> columns) {
    }

    public void deleteIndex(String keyspaceName, String tableName, Collection<Column> columns) {
    }

    public void dropTable(Table table) {
    }

    public TableRow selectOneRowFromTable(String keyspaceName, String tableName, String condition) {
        try {
            Thread.sleep(sleepyTimeInMilliseconds);
        } catch (InterruptedException ex) {
            java.util.logging.Logger.getLogger(SleepyAdapter.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new TableRow();
    }

    public List<TableRow> selectXRowsFromTable(TableQuery querry) {
        try {
            Thread.sleep(sleepyTimeInMilliseconds);
        } catch (InterruptedException ex) {
            java.util.logging.Logger.getLogger(SleepyAdapter.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<TableRow>();
    }

    public void insertRowsInTable(String keyspaceName, String tableName, Collection<TableRow> rows) {
        try {
            Thread.sleep(sleepyTimeInMilliseconds);
        } catch (InterruptedException ex) {
            java.util.logging.Logger.getLogger(SleepyAdapter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void updateRowInTable(String keyspaceName, String tableName, Map<String, Object> newData, String condition) {
        try {
            Thread.sleep(sleepyTimeInMilliseconds);
        } catch (InterruptedException ex) {
            java.util.logging.Logger.getLogger(SleepyAdapter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void deleteRowsFromTable(TableQuery query) {
        try {
            Thread.sleep(sleepyTimeInMilliseconds);
        } catch (InterruptedException ex) {
            java.util.logging.Logger.getLogger(SleepyAdapter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
