/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.daas;

import at.ac.tuwien.dsg.daas.DaaSDelegate.MonitoringData;
import at.ac.tuwien.dsg.daas.config.CassandraAccessProperties;
import at.ac.tuwien.dsg.daas.entities.Column;
import at.ac.tuwien.dsg.daas.entities.Keyspace;
import at.ac.tuwien.dsg.daas.entities.Table;
import at.ac.tuwien.dsg.daas.entities.TableQuery;
import at.ac.tuwien.dsg.daas.entities.TableRow;
import com.datastax.driver.core.Row;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Georgiana
 */
public class MultiDaaSManagement implements TenantDataManagementAPI{
    private static MultiDaaSManagement instance;
    private static HashMap<String,String> hosts = new HashMap<String, String>();

    private static HashMap<String, DaaSDelegate> daasDelegateInstancesForTenants = new HashMap<String,DaaSDelegate>(); // tenants associated with instances
    static{
    instance = new MultiDaaSManagement();
    }
    public static MultiDaaSManagement getInstance(){
        return instance;
    }
    private MultiDaaSManagement (){
        
    }
    
    public  void addTenant (String tenant, String host, String port){
             
            DaaSDelegate delegate = new DaaSDelegate( host, port);
            daasDelegateInstancesForTenants.put(tenant,delegate);
            if (!hosts.containsKey(host)) {
            hosts.put(host,port);
            }
      }
     public  void addTenant (String tenant){
            DaaSDelegate delegate = new DaaSDelegate(CassandraAccessProperties.getCassandraAccessIP(),
            CassandraAccessProperties.getCassandraAccessPort());
            daasDelegateInstancesForTenants.put(tenant,delegate);
    }
    public void removeTenant(String tenant){
            
            daasDelegateInstancesForTenants.remove(tenant);
    }

    public void openConnection(String tenant) {
        daasDelegateInstancesForTenants.get(tenant).openConnection();
    }

    public void closeConnection(String tenant) {
        daasDelegateInstancesForTenants.get(tenant).closeConnection();
    }

    public void createKeyspace(String tenant, Keyspace keyspace) {
        daasDelegateInstancesForTenants.get(tenant).createKeyspace(keyspace);
    }

    public List<Row> listKeyspaces(String tenant) {
        return daasDelegateInstancesForTenants.get(tenant).listKeyspaces();
    }

    public void dropKeyspace(String tenant, Keyspace keyspace) {
       daasDelegateInstancesForTenants.get(tenant).dropKeyspace(keyspace);
    }

    public void createTable(String tenant, Table table) {
        daasDelegateInstancesForTenants.get(tenant).createTable(table);
    }

    public void createIndex(String tenant, String keyspaceName, String tableName, Collection<Column> columns) {
        daasDelegateInstancesForTenants.get(tenant).createIndex(keyspaceName, tableName, columns);
    }

    public void deleteIndex(String tenant, String keyspaceName, String tableName, Collection<Column> columns) {
        daasDelegateInstancesForTenants.get(tenant).deleteIndex(keyspaceName, tableName, columns);
    }

    public void dropTable(String tenant, Table table) {
        daasDelegateInstancesForTenants.get(tenant).dropTable(table);
    }

    public Row selectOneRowFromTable(String tenant, String keyspaceName, String tableName, String condition) {
        return daasDelegateInstancesForTenants.get(tenant).selectOneRowFromTable(keyspaceName, tableName, condition);
    }

    public List<Row> selectXRowsFromTable(String tenant, TableQuery querry) {
        return daasDelegateInstancesForTenants.get(tenant).selectXRowsFromTable(querry);
    }

    public void insertRowsInTable(String tenant, String keyspaceName, String tableName, Collection<TableRow> rows) {
        daasDelegateInstancesForTenants.get(tenant).insertRowsInTable(keyspaceName, tableName, rows);
    }

    public void updateRowInTable(String tenant, String keyspaceName, String tableName, Map<String, Object> newData, String condition) {
       daasDelegateInstancesForTenants.get(tenant).updateRowInTable(keyspaceName, tableName, newData, condition);
    }

    public void deleteRowsFromTable(String tenant, TableQuery query) {
        daasDelegateInstancesForTenants.get(tenant).deleteRowsFromTable(query);
    }

    public String getCassandraHostIP(String tenant) {
        return  daasDelegateInstancesForTenants.get(tenant).getCassandraHostIP();
    }

    public int getCasandraPort(String tenant) {
        return daasDelegateInstancesForTenants.get(tenant).getCasandraPort();
    }

    public MonitoringData getMonitoringInfo(String id) {
        return daasDelegateInstancesForTenants.get(id).getMonitoringData();
    }

    public MonitoringData getMonitoringInfo() {
        MonitoringData result = new MonitoringData();
        result.setAverageResponseTime(0l);
        result.setAverageTroughput(0l);
        result.setPendingRequests(0);
       long rt = 0;
       long throughput=0;
       int requests = 0;
        for (String tenantID : daasDelegateInstancesForTenants.keySet()){
          MonitoringData monitoringData=daasDelegateInstancesForTenants.get(tenantID).getMonitoringData();
          
          rt+=monitoringData.getAverageResponseTime();
          throughput+=monitoringData.getAverageTroughput();
          requests += monitoringData.getPendingRequests();
       }
        result.setAverageResponseTime(rt/daasDelegateInstancesForTenants.keySet().size());
        result.setAverageTroughput(throughput);
        result.setPendingRequests(requests);
        return result;
    }


}

