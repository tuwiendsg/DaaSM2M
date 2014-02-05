/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.daas;

import at.ac.tuwien.dsg.daas.config.CassandraAccessProperties;
import at.ac.tuwien.dsg.daas.entities.Column;
import at.ac.tuwien.dsg.daas.entities.Keyspace;
import at.ac.tuwien.dsg.daas.entities.Table;
import at.ac.tuwien.dsg.daas.entities.TableQuery;
import at.ac.tuwien.dsg.daas.entities.TableRow;
import at.ac.tuwien.dsg.daas.util.ConfigurationFilesLoader;
import at.ac.tuwien.dsg.daas.util.Monitor;
import com.datastax.driver.core.Row;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 *
 * @author daniel-tuwien Delegate to support monitoring of RT and Throughput
 */
public class DaaSDelegate implements DataManagementAPI {

    private static DaaSDelegate instance;

    private DataManagementAPI dataManagementAPI;
    private Monitor monitor;

    static {

        String date = new Date().toString();
        date = date.replace(" ", "_");
        date = date.replace(":", "_");
        System.getProperties().put("recording_date", date);

        InputStream log4jStream;
        try {
            log4jStream = ConfigurationFilesLoader.getLog4JPropertiesStream();
            if (log4jStream != null) {
                PropertyConfigurator.configure(log4jStream);
                try {
                    log4jStream.close();
                } catch (IOException e) {
                    Logger.getLogger(DataManagementAPIFactory.class).log(Level.ERROR, e);
                }
            }
        } catch (FileNotFoundException e) {
            Logger.getLogger(DataManagementAPIFactory.class).log(Level.ERROR, e);
        }

    }

    static {
        try {
            instance = new DaaSDelegate();
        } catch (Exception e) {
            Logger.getLogger(DataManagementAPIFactory.class).log(Level.ERROR, e);
        }
    }

    {
        monitor = new Monitor(1000);
    }

    public static DaaSDelegate getInstance() {
        return instance;
    }

    private DaaSDelegate() {
        dataManagementAPI = DataManagementAPIFactory.createCassandraFactory(CassandraAccessProperties.getCassandraAccessIP(),
                CassandraAccessProperties.getCassandraAccessPort());
    }

    public synchronized void openConnection() {
        dataManagementAPI.openConnection();
    }

    public synchronized void closeConnection() {
        dataManagementAPI.openConnection();
    }

    public synchronized void createKeyspace(Keyspace keyspace) {
        dataManagementAPI.createKeyspace(keyspace);
    }

    public synchronized List<Row> listKeyspaces() {
        return dataManagementAPI.listKeyspaces();
    }

    public synchronized void dropKeyspace(Keyspace keyspace) {
        dataManagementAPI.dropKeyspace(keyspace);
    }

    public synchronized void createTable(Table table) {
        dataManagementAPI.createTable(table);
    }

    public synchronized void createIndex(String keyspaceName, String tableName, Collection<Column> columns) {
        dataManagementAPI.createIndex(keyspaceName, tableName, columns);
    }

    public synchronized void deleteIndex(String keyspaceName, String tableName, Collection<Column> columns) {
        dataManagementAPI.deleteIndex(keyspaceName, tableName, columns);
    }

    public synchronized void dropTable(Table table) {
        dataManagementAPI.dropTable(table);
    }

    public synchronized Row selectOneRowFromTable(String keyspaceName, String tableName, String condition) {
        Date before = new Date();

        Row r = dataManagementAPI.selectOneRowFromTable(keyspaceName, tableName, condition);
        Date after = new Date();

        monitor.monitorRT(after.getTime() - before.getTime());

        return r;
    }

    public synchronized List<Row> selectXRowsFromTable(TableQuery querry) {
        Date before = new Date();

        List<Row> r = dataManagementAPI.selectXRowsFromTable(querry);
        Date after = new Date();

        monitor.monitorRT(after.getTime() - before.getTime());

        return r;
    }

    public synchronized void insertRowsInTable(String keyspaceName, String tableName, Collection<TableRow> rows) {
        Date before = new Date();

        dataManagementAPI.insertRowsInTable(keyspaceName, tableName, rows);
        Date after = new Date();

        monitor.monitorRT(after.getTime() - before.getTime());
    }

    public synchronized void updateRowInTable(String keyspaceName, String tableName, Map<String, Object> newData, String condition) {
        Date before = new Date();

        dataManagementAPI.updateRowInTable(keyspaceName, tableName, newData, condition);
        Date after = new Date();

        monitor.monitorRT(after.getTime() - before.getTime());
    }

    public synchronized void deleteRowsFromTable(TableQuery query) {
        Date before = new Date();

        dataManagementAPI.deleteRowsFromTable(query);
        Date after = new Date();

        monitor.monitorRT(after.getTime() - before.getTime());
    }

    public synchronized String getCassandraHostIP() {
        return dataManagementAPI.getCassandraHostIP();
    }

    public synchronized int getCasandraPort() {
        return dataManagementAPI.getCasandraPort();
    }

    public synchronized MonitoringData getMonitoringData() {
        long[] l = monitor.getMonitoringData();
        return new MonitoringData(l[0], l[1]);
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlRootElement(name = "MonitoringData")
    public static class MonitoringData {

        @XmlElement(name = "responseTime", required = true)
        private Long averageResponseTime;

        @XmlElement(name = "throughput", required = true)
        private Long averageTroughput;

        public MonitoringData() {
        }

        public MonitoringData(Long averageResponseTime, Long averageTroughput) {
            this.averageResponseTime = averageResponseTime;
            this.averageTroughput = averageTroughput;
        }

        public Long getAverageResponseTime() {
            return averageResponseTime;
        }

        public void setAverageResponseTime(Long averageResponseTime) {
            this.averageResponseTime = averageResponseTime;
        }

        public Long getAverageTroughput() {
            return averageTroughput;
        }

        public void setAverageTroughput(Long averageTroughput) {
            this.averageTroughput = averageTroughput;
        }

    }

}
