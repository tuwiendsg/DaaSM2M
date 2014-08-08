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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
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

    private  String tenant;
    private DataManagementAPI dataManagementAPI;
    private Monitor monitor;

    private static AtomicInteger requestCount = new AtomicInteger(0);

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
    
 

    private static Integer newRequestID() {
        return requestCount.incrementAndGet();
    }

    public DaaSDelegate() {
        
        dataManagementAPI = DataManagementAPIFactory.createCassandraFactory(CassandraAccessProperties.getCassandraAccessIP(),
                CassandraAccessProperties.getCassandraAccessPort());

    }
    public DaaSDelegate(String host, String port){
        dataManagementAPI = DataManagementAPIFactory.createCassandraFactory(host, port);
        
    }

    public void openConnection() {
        dataManagementAPI.openConnection();
    }

    public void closeConnection() {
        dataManagementAPI.openConnection();
    }

    public void createKeyspace(Keyspace keyspace) {
        Integer reqID = DaaSDelegate.newRequestID();
        monitor.addOutstandingRequest(reqID, new Date());

        try {
            dataManagementAPI.createKeyspace(keyspace);
        } finally {
            monitor.removeOutstandingRequest(reqID, new Date());
        }
    }

    public List<Row> listKeyspaces() {
        List<Row> res;

        Integer reqID = DaaSDelegate.newRequestID();
        monitor.addOutstandingRequest(reqID, new Date());

        try {
            res = dataManagementAPI.listKeyspaces();
        } finally {
            monitor.removeOutstandingRequest(reqID, new Date());
        }

        return res;

    }

    public void dropKeyspace(Keyspace keyspace) {
        Integer reqID = DaaSDelegate.newRequestID();
        monitor.addOutstandingRequest(reqID, new Date());

        try {
            dataManagementAPI.dropKeyspace(keyspace);
        } finally {
            monitor.removeOutstandingRequest(reqID, new Date());
        }

    }

    public void createTable(Table table) {
        Integer reqID = DaaSDelegate.newRequestID();
        monitor.addOutstandingRequest(reqID, new Date());

        try {
            dataManagementAPI.createTable(table);
        } finally {
            monitor.removeOutstandingRequest(reqID, new Date());
        }

    }

    public void createIndex(String keyspaceName, String tableName, Collection<Column> columns) {
        Integer reqID = DaaSDelegate.newRequestID();
        monitor.addOutstandingRequest(reqID, new Date());

        try {
            dataManagementAPI.createIndex(keyspaceName, tableName, columns);
        } finally {
            monitor.removeOutstandingRequest(reqID, new Date());
        }

    }

    public void deleteIndex(String keyspaceName, String tableName, Collection<Column> columns) {
        Integer reqID = DaaSDelegate.newRequestID();
        monitor.addOutstandingRequest(reqID, new Date());

        try {
            dataManagementAPI.deleteIndex(keyspaceName, tableName, columns);
        } finally {
            monitor.removeOutstandingRequest(reqID, new Date());
        }

    }

    public void dropTable(Table table) {

        Integer reqID = DaaSDelegate.newRequestID();
        monitor.addOutstandingRequest(reqID, new Date());

        try {
            dataManagementAPI.dropTable(table);
        } finally {
            monitor.removeOutstandingRequest(reqID, new Date());
        }

    }

    public Row selectOneRowFromTable(String keyspaceName, String tableName, String condition) {
        Row r;
        Integer reqID = DaaSDelegate.newRequestID();
        monitor.addOutstandingRequest(reqID, new Date());

        try {
            r = dataManagementAPI.selectOneRowFromTable(keyspaceName, tableName, condition);
        } finally {
            monitor.removeOutstandingRequest(reqID, new Date());
        }

        return r;

    }

    public List<Row> selectXRowsFromTable(TableQuery querry) {
        List<Row> res;

        Integer reqID = DaaSDelegate.newRequestID();
        monitor.addOutstandingRequest(reqID, new Date());

        try {
            res = dataManagementAPI.selectXRowsFromTable(querry);
        } finally {
            monitor.removeOutstandingRequest(reqID, new Date());
        }

        return res;

    }

    public void insertRowsInTable(String keyspaceName, String tableName, Collection<TableRow> rows) {
        Integer reqID = DaaSDelegate.newRequestID();
        monitor.addOutstandingRequest(reqID, new Date());

        try {
            dataManagementAPI.insertRowsInTable(keyspaceName, tableName, rows);
        } finally {
            monitor.removeOutstandingRequest(reqID, new Date());
        }

    }

    public void updateRowInTable(String keyspaceName, String tableName, Map<String, Object> newData, String condition) {
        Integer reqID = DaaSDelegate.newRequestID();
        monitor.addOutstandingRequest(reqID, new Date());

        try {
            dataManagementAPI.updateRowInTable(keyspaceName, tableName, newData, condition);
        } finally {
            monitor.removeOutstandingRequest(reqID, new Date());
        }

    }

    public void deleteRowsFromTable(TableQuery query) {

        Integer reqID = DaaSDelegate.newRequestID();
        monitor.addOutstandingRequest(reqID, new Date());

        try {
            dataManagementAPI.deleteRowsFromTable(query);
        } finally {
            monitor.removeOutstandingRequest(reqID, new Date());
        }

    }

    public String getCassandraHostIP() {
        return dataManagementAPI.getCassandraHostIP();
    }

    public int getCasandraPort() {
        return dataManagementAPI.getCasandraPort();
    }

    public MonitoringData getMonitoringData() {
        Number[] l = monitor.getMonitoringData();
        return new MonitoringData(l[0].longValue(), l[1].longValue(), l[2].intValue());
    }

    /**
     * @return the tenant
     */
    public String getTenant() {
        return tenant;
    }

    /**
     * @param tenant the tenant to set
     */
    public void setTenant(String tenant) {
        this.tenant = tenant;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlRootElement(name = "MonitoringData")
    public static class MonitoringData {

        @XmlElement(name = "responseTime", required = true)
        private Long averageResponseTime;

        @XmlElement(name = "throughput", required = true)
        private Long averageTroughput;

        @XmlElement(name = "pendingRequests", required = true)
        private Integer pendingRequests;

        public MonitoringData() {
        }

        public MonitoringData(Long averageResponseTime, Long averageTroughput, Integer pendingRequests) {
            this.averageResponseTime = averageResponseTime;
            this.averageTroughput = averageTroughput;
            this.pendingRequests = pendingRequests;
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

        public Integer getPendingRequests() {
            return pendingRequests;
        }

        public void setPendingRequests(Integer pendingRequests) {
            this.pendingRequests = pendingRequests;
        }

    }

}
