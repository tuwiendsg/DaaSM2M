///**
// * Copyright 2013 Technische Universitaet Wien (TUW), Distributed Systems Group
// * E184
// *
// * Licensed under the Apache License, Version 2.0 (the "License"); you may not
// * use this file except in compliance with the License. You may obtain a copy of
// * the License at
// *
// * http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
// * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
// * License for the specific language governing permissions and limitations under
// * the License.
// */
//package at.ac.tuwien.dsg.daas.impl;
//
//import at.ac.tuwien.dsg.daas.DataManagementAPI;
//import at.ac.tuwien.dsg.daas.entities.Column;
//import at.ac.tuwien.dsg.daas.entities.Keyspace;
//import at.ac.tuwien.dsg.daas.entities.Table;
//import at.ac.tuwien.dsg.daas.entities.TableQuery;
//import at.ac.tuwien.dsg.daas.entities.TableRow;
//import com.datastax.driver.core.Row;
//import java.util.Collection;
//import java.util.List;
//import java.util.Map;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
///**
// *
// * @Author Daniel Moldovan
// * @E-mail: d.moldovan@dsg.tuwien.ac.at
// *
// */
//@Component
//public class DataManagementDelegate implements DataManagementAPI {
//
//    @Autowired
//    private CassandraManagementAPI cassandraManagementAPI;
////    private MessageProducer producer;
////    private Session session;
////    private Connection connection;
//
//    public DataManagementDelegate(String ip, String port) {
//
//        cassandraManagementAPI = new CassandraManagementAPI(ip, Integer.parseInt(port));
//        //to be uncommented when ActiveMQ usage is implemented
////        //also push this in the queue
////        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
////        {
////            try {
////                connectionFactory.setBrokerURL(System.getProperty("ActiveMQProtocol", "tcp") + "://" + System.getProperty("ActiveMQIP", "128.131.172.45") + ":" + System.getProperty("ActiveMQPort", "61616"));
////                connection = connectionFactory.createConnection();
////
////            } catch (JMSException ex) {
////                Logger.getLogger(DataManagementAPIFactory.class.getName()).log(Level.ERROR, null, ex);
////            }
////        }
//    }
//
//    public DataManagementDelegate() {
//    }
//
//    public void openConnection() {
//        cassandraManagementAPI.openConnection();
////        try {
//////            connection.start();
//////            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
////            Destination destination = session.createTopic(System.getProperty("TopicName", "tcp:SensorTopic"));
////            producer = session.createProducer(destination);
////            producer.setDeliveryMode(DeliveryMode.PERSISTENT);
////        } catch (JMSException ex) {
////            Logger.getLogger(DataManagementDelegate.class.getName()).log(Level.ERROR, null, ex);
////        }
//    }
//
//    public void closeConnection() {
//        cassandraManagementAPI.closeConnection();
////        try {
////            session.close();
//////            connection.close();
////        } catch (JMSException ex) {
////            Logger.getLogger(DataManagementAPIFactory.class.getName()).log(Level.ERROR, null, ex);
////        }
//    }
//
//    public void createKeyspace(Keyspace keyspace) {
//        cassandraManagementAPI.createKeyspace(keyspace);
//    }
//
//    public void dropKeyspace(Keyspace keyspace) {
//        cassandraManagementAPI.dropKeyspace(keyspace);
//    }
//
//    public void createTable(Table table) {
//        cassandraManagementAPI.createTable(table);
//    }
//
//    public void createIndex(String keyspaceName, String tableName, Collection<Column> columns) {
//        cassandraManagementAPI.createIndex(keyspaceName, tableName, columns);
//    }
//
//    public void deleteIndex(String keyspaceName, String tableName, Collection<Column> columns) {
//        cassandraManagementAPI.deleteIndex(keyspaceName, tableName, columns);
//    }
//
//    public void dropTable(Table table) {
//        cassandraManagementAPI.dropTable(table);
//    }
//
//    public Row selectOneRowFromTable(String keyspaceName, String tableName, String condition) {
//        return cassandraManagementAPI.selectOneRowFromTable(keyspaceName, tableName, condition);
//    }
//
//    public List<Row> selectXRowsFromTable(TableQuery querry) {
//        return cassandraManagementAPI.selectXRowsFromTable(querry);
//    }
//
//    public void insertRowsInTable(String keyspaceName, String tableName, Collection<TableRow> rows) {
//        for (TableRow row : rows) {
//            sendMessage(row);
//        }
//        cassandraManagementAPI.insertRowsInTable(keyspaceName, tableName, rows);
//    }
//
//    public void updateRowInTable(String keyspaceName, String tableName, Map<String, Object> newData, String condition) {
//        cassandraManagementAPI.updateRowInTable(keyspaceName, tableName, newData, condition);
//    }
//
//    public void deleteRowsFromTable(TableQuery query) {
//        cassandraManagementAPI.deleteRowsFromTable(query);
//    }
//
//    public String getCassandraHostIP() {
//        return cassandraManagementAPI.getCassandraHostIP();
//    }
//
//    public int getCasandraPort() {
//        return cassandraManagementAPI.getCasandraPort();
//    }
//
//    /**
//     * Customizable, we can create object or text messages
//     *
//     * @param message
//     */
//    private void sendMessage(Object message) {
////        try {
////
////            TextMessage jmsMessage = session.createTextMessage(message.toString());
////
////            producer.send(jmsMessage);
////        } catch (JMSException ex) {
////            Logger.getLogger(DataManagementAPIFactory.class.getName()).log(Level.ERROR, null, ex);
////        }
//    }
//
//    public List<Row> listKeyspaces() {
//        return cassandraManagementAPI.listKeyspaces();
//    }
//}
