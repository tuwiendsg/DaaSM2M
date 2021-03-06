/**
 * Copyright 2013 Technische Universitaet Wien (TUW), Distributed Systems Group
 * E184
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package tests;

import java.util.UUID;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import org.apache.activemq.ActiveMQConnectionFactory;

/**
 *
 * @Author Daniel Moldovan
 * @E-mail: d.moldovan@dsg.tuwien.ac.at
 *
 */
public class TestActiveMQConsumer {

    public static void main(String[] args) throws Exception {
       
        
        try {
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(System.getProperty("ActiveMQProtocol", "tcp") + "://" + System.getProperty("ActiveMQIP", "128.131.172.45") + ":" + System.getProperty("ActiveMQPort", "61616"));
//            connectionFactory.setBrokerURL(System.getProperty("ActiveMQProtocol", "tcp") + "://" + System.getProperty("ActiveMQIP", "localhost") + ":" + System.getProperty("ActiveMQPort", "61616"));

            Connection connection = connectionFactory.createConnection();
            connection.start();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createTopic(System.getProperty("TopicName", "tcp:SensorTopic"));
            MessageConsumer consumer = session.createConsumer(destination);

//            // Wait for a message
//            Message message = consumer.receive(1000);
//
//            if (message instanceof TextMessage) {
//                TextMessage textMessage = (TextMessage) message;
//                String text = textMessage.getText();
//                System.out.println("Received: " + text);
//            } else {
//                System.out.println("Received: " + message);
//            }

            consumer.setMessageListener(new HelloWorldConsumer());

//            session.close();
//            connection.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
         
        
    }

    public static void thread(Runnable runnable, boolean daemon) {
        Thread brokerThread = new Thread(runnable);
        brokerThread.setDaemon(daemon);
        brokerThread.start();
    }

    public static class HelloWorldProducer implements Runnable {

        public void run() {
            try {
                ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(System.getProperty("ActiveMQProtocol", "tcp") + "://" + System.getProperty("ActiveMQIP", "128.131.172.45") + ":" + System.getProperty("ActiveMQPort", "61616"));
//              connectionFactory.setBrokerURL(System.getProperty("ActiveMQProtocol", "tcp") + "://" + System.getProperty("ActiveMQIP", "localhost") + ":" + System.getProperty("ActiveMQPort", "61616"));
//              connectionFactory.setBrokerURL(System.getProperty("ActiveMQProtocol", "tcp") + "://" + System.getProperty("ActiveMQIP", "localhost") + ":" + System.getProperty("ActiveMQPort", "61616"));
                Connection connection = connectionFactory.createConnection();
                connection.start();
                Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
                Destination destination = session.createTopic(System.getProperty("TopicName", "jms/SensorTopic"));
                MessageProducer producer = session.createProducer(destination);
                producer.setDeliveryMode(DeliveryMode.PERSISTENT);

                TextMessage message = session.createTextMessage("<sensor id=\"" + UUID.randomUUID().toString() + "\">345</>");
                producer.send(message);
                session.close();
                connection.close();
            } catch (Exception e) {
                System.out.println("Caught: " + e);
                e.printStackTrace();
            }
        }
    }

    public static class HelloWorldConsumer implements Runnable, MessageListener {

        public void run() {
            try {

                // Create a ConnectionFactory
                ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("vm://localhost");

                // Create a Connection
                Connection connection = connectionFactory.createConnection();
                connection.start();


                // Create a Session
                Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

                // Create the destination (Topic or Queue)
                Destination destination = session.createQueue("TEST.FOO");

                // Create a MessageConsumer from the Session to the Topic or Queue
                MessageConsumer consumer = session.createConsumer(destination);

                // Wait for a message
                Message message = consumer.receive(1000);

                if (message instanceof TextMessage) {
                    TextMessage textMessage = (TextMessage) message;
                    String text = textMessage.getText();
                    System.out.println("Received: " + text);
                } else {
                    System.out.println("Received: " + message);
                }

                consumer.close();
                session.close();
                connection.close();
            } catch (Exception e) {
                System.out.println("Caught: " + e);
                e.printStackTrace();
            }
        }

        public synchronized void onException(JMSException ex) {
            System.out.println("JMS Exception occured.  Shutting down client.");
        }

        public void onMessage(Message message) {
            try {
                if (message instanceof TextMessage) {
                    try {
                        TextMessage textMessage = (TextMessage) message;
                        String text = textMessage.getText();
                        System.out.println("Received: " + text);
                    } catch (JMSException ex) {
                        Logger.getLogger(TestActiveMQConsumer.class.getName()).log(Level.ERROR, null, ex);
                    }
                } else {
                    System.out.println("Received: " + message);
                }
                System.out.println(message.getJMSType());
            } catch (JMSException ex) {
                Logger.getLogger(TestActiveMQConsumer.class.getName()).log(Level.ERROR, null, ex);
            }
        }
    }
}
