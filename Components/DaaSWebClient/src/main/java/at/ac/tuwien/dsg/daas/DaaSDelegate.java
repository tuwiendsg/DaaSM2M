/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.daas;

import at.ac.tuwien.dsg.daas.entities.Column;
import at.ac.tuwien.dsg.daas.entities.Keyspace;
import at.ac.tuwien.dsg.daas.entities.Table;
import at.ac.tuwien.dsg.daas.entities.TableQuery;
import at.ac.tuwien.dsg.daas.entities.TableRow;
//import at.ac.tuwien.dsg.daas.util.ConfigurationFilesLoader;
import at.ac.tuwien.dsg.daas.util.Monitor;
import com.datastax.driver.core.Row;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.PostConstruct;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

/**
 *
 * @author daniel-tuwien Delegate to support monitoring of RT and Throughput
 */
@Service
public class DaaSDelegate {

    static final org.slf4j.Logger log = LoggerFactory.getLogger(DaaSDelegate.class);

//    @Autowired
//    @Qualifier(value = "dataManagementDelegate")
    private List<DataManagementAPI> dataManagementAPIs;

    private Monitor monitor;

    private AtomicInteger requestCount = new AtomicInteger(0);

    @Autowired
    private ApplicationContext context;

//    {
//
//        String date = new Date().toString();
//        date = date.replace(" ", "_");
//        date = date.replace(":", "_");
//        System.getProperties().put("recording_date", date);
//
//        InputStream log4jStream;
//        try {
//            log4jStream = ConfigurationFilesLoader.getLog4JPropertiesStream();
//            if (log4jStream != null) {
//                PropertyConfigurator.configure(log4jStream);
//                try {
//                    log4jStream.close();
//                } catch (IOException e) {
//                    Logger.getLogger(DataManagementAPIFactory.class).log(Level.ERROR, e);
//                }
//            }
//        } catch (FileNotFoundException e) {
//            Logger.getLogger(DataManagementAPIFactory.class).log(Level.ERROR, e);
//        }
//
//    }
    {
        dataManagementAPIs = new ArrayList<DataManagementAPI>();
        monitor = Monitor.getInstance();
    }

    private Integer newRequestID() {
        return requestCount.incrementAndGet();
    }

    public DaaSDelegate() {

    }

    @PostConstruct
    public void init() {
        // list all MELA datasources from application context
        //maybe in future add specific source for specific service
        Map<String, DataManagementAPI> dataSources = context.getBeansOfType(DataManagementAPI.class);

        for (String dataSourceName : dataSources.keySet()) {
            DataManagementAPI dataSource = dataSources.get(dataSourceName);
            log.debug("Found Datasource '{}': {}", dataSourceName, dataSource);
            dataManagementAPIs.add(dataSource);
        }
    }

    public void openConnection() {
    }

    public void closeConnection() {
    }

    public void createKeyspace(Keyspace keyspace) {
        Integer reqID = this.newRequestID();
        monitor.addOutstandingRequest(reqID, new Date());

        try {
            for (DataManagementAPI dataManagementAPI : dataManagementAPIs) {
                dataManagementAPI.createKeyspace(keyspace);
            }
        } finally {
            monitor.removeOutstandingRequest(reqID, new Date());
        }
    }

    public List<Row> listKeyspaces() {
        List<Row> res = new LinkedList<Row>();

        Integer reqID = this.newRequestID();
        monitor.addOutstandingRequest(reqID, new Date());

        try {
            for (DataManagementAPI dataManagementAPI : dataManagementAPIs) {
                res.addAll(dataManagementAPI.listKeyspaces());
            }
        } finally {
            monitor.removeOutstandingRequest(reqID, new Date());
        }

        return res;

    }

    public void dropKeyspace(Keyspace keyspace) {
        Integer reqID = this.newRequestID();
        monitor.addOutstandingRequest(reqID, new Date());

        try {
            for (DataManagementAPI dataManagementAPI : dataManagementAPIs) {
                dataManagementAPI.dropKeyspace(keyspace);
            }
        } finally {
            monitor.removeOutstandingRequest(reqID, new Date());
        }

    }

    public void createTable(Table table) {
        Integer reqID = this.newRequestID();
        monitor.addOutstandingRequest(reqID, new Date());

        try {
            for (DataManagementAPI dataManagementAPI : dataManagementAPIs) {
                dataManagementAPI.createTable(table);
            }
        } finally {
            monitor.removeOutstandingRequest(reqID, new Date());
        }

    }

    public void createIndex(String keyspaceName, String tableName, Collection<Column> columns) {
        Integer reqID = this.newRequestID();
        monitor.addOutstandingRequest(reqID, new Date());

        try {
            for (DataManagementAPI dataManagementAPI : dataManagementAPIs) {
                dataManagementAPI.createIndex(keyspaceName, tableName, columns);
            }
        } finally {
            monitor.removeOutstandingRequest(reqID, new Date());
        }

    }

    public void deleteIndex(String keyspaceName, String tableName, Collection<Column> columns) {
        Integer reqID = this.newRequestID();
        monitor.addOutstandingRequest(reqID, new Date());

        try {
            for (DataManagementAPI dataManagementAPI : dataManagementAPIs) {
                dataManagementAPI.deleteIndex(keyspaceName, tableName, columns);
            }
        } finally {
            monitor.removeOutstandingRequest(reqID, new Date());
        }

    }

    public void dropTable(Table table) {

        Integer reqID = this.newRequestID();
        monitor.addOutstandingRequest(reqID, new Date());

        try {
            for (DataManagementAPI dataManagementAPI : dataManagementAPIs) {
                dataManagementAPI.dropTable(table);
            }
        } finally {
            monitor.removeOutstandingRequest(reqID, new Date());
        }

    }

    public Row selectOneRowFromTable(String keyspaceName, String tableName, String condition) {
        Row r = null;
        Integer reqID = this.newRequestID();
        monitor.addOutstandingRequest(reqID, new Date());

        try {
            for (DataManagementAPI dataManagementAPI : dataManagementAPIs) {
                r = dataManagementAPI.selectOneRowFromTable(keyspaceName, tableName, condition);
                if (r != null) {
                    return r;
                }
            }
        } finally {
            monitor.removeOutstandingRequest(reqID, new Date());
        }

        return r;

    }

    public List<Row> selectXRowsFromTable(TableQuery querry) {
        List<Row> res = new ArrayList<Row>();

        Integer reqID = this.newRequestID();
        monitor.addOutstandingRequest(reqID, new Date());

        try {
            for (DataManagementAPI dataManagementAPI : dataManagementAPIs) {
                res.addAll(dataManagementAPI.selectXRowsFromTable(querry));
            }
        } finally {
            monitor.removeOutstandingRequest(reqID, new Date());
        }

        return res;

    }

    public void insertRowsInTable(String keyspaceName, String tableName, Collection<TableRow> rows) {
        Integer reqID = this.newRequestID();
        monitor.addOutstandingRequest(reqID, new Date());

        try {
            for (DataManagementAPI dataManagementAPI : dataManagementAPIs) {
                dataManagementAPI.insertRowsInTable(keyspaceName, tableName, rows);
            }
        } finally {
            monitor.removeOutstandingRequest(reqID, new Date());
        }

    }

    public void updateRowInTable(String keyspaceName, String tableName, Map<String, Object> newData, String condition) {
        Integer reqID = this.newRequestID();
        monitor.addOutstandingRequest(reqID, new Date());

        try {
            for (DataManagementAPI dataManagementAPI : dataManagementAPIs) {
                dataManagementAPI.updateRowInTable(keyspaceName, tableName, newData, condition);
            }
        } finally {
            monitor.removeOutstandingRequest(reqID, new Date());
        }

    }

    public void deleteRowsFromTable(TableQuery query) {

        Integer reqID = this.newRequestID();
        monitor.addOutstandingRequest(reqID, new Date());

        try {
            for (DataManagementAPI dataManagementAPI : dataManagementAPIs) {
                dataManagementAPI.deleteRowsFromTable(query);
            }
        } finally {
            monitor.removeOutstandingRequest(reqID, new Date());
        }

    }

    public MonitoringData getMonitoringData() {
        Number[] l = monitor.getMonitoringData();
        return new MonitoringData(l[0].longValue(), l[1].longValue(), l[2].intValue());
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
