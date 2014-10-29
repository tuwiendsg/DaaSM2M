package at.ac.tuwien.dsg.m2mdaas.processNewSensorData;

import at.ac.tuwien.dsg.m2mdaas.callEventProcessing.EventProcessingM2MInterraction;
import at.ac.tuwien.dsg.m2mdaas.utils.Configuration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.log4j.Logger;

public class MQConsumer implements MessageListener, ExceptionListener, Runnable {

    private static final Logger LOGGER = Logger.getLogger(GenericNewSensorDataProcessing.class);
    private String subject;
    private String user = ActiveMQConnection.DEFAULT_USER;
    private String password = ActiveMQConnection.DEFAULT_PASSWORD;
    private String url = "";
    private boolean transacted;
    private int ackMode = Session.AUTO_ACKNOWLEDGE;
    private int BURST_SIZE = 10;
    private int BURST_SLEEP = 200;
    private Session session;
    private Destination destination;
    private MessageConsumer consumer = null;
    private Connection connection;
    private ProcessData processSensorLocationData = new GenericNewSensorDataProcessing();
    private int REFRESH_TIME = Configuration.getRefreshRate();
    private List< HashMap<String, HashMap<String, String>>> storeData = new ArrayList< HashMap<String, HashMap<String, String>>>();
    private Thread t;

    public MQConsumer(String url, String topic) {
        this.url = url;
        this.subject = topic;
        t = new Thread(this);
        if (!Configuration.getBurstSize().equalsIgnoreCase("")) {
            BURST_SIZE = Integer.parseInt(Configuration.getBurstSize());
        }
        if (!Configuration.getBurstSleep().equalsIgnoreCase("")) {
            BURST_SLEEP = Integer.parseInt(Configuration.getBurstSleep());
        }
        t.start();

    }

    public void setUp() {
        LOGGER.info("Starting up MQTT consumer ..." + url);
        try {
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(user, password, url);
            this.connection = connectionFactory.createConnection();
            connection.setExceptionListener(this);
            connection.start();
            session = connection.createSession(transacted, ackMode);
            destination = session.createTopic(subject);
            consumer = session.createConsumer(destination);
            consumer.setMessageListener(this);
            LOGGER.info("MQTT consumer started successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("Cloud not start MQTT consumer!", e);
        }
    }

    public void onMessage(Message arg0) {
        TextMessage event = (TextMessage) arg0;
        try {
            onEvent(event.getText());
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public void onEvent(String event) {

        // check if events defined
//            String specEvent = Configuration.getEventDefinition();
//            if (!event.equalsIgnoreCase("")){
//                if (event.contains("<")){
//                    
//                }else{
//                    if (event.contains(">")){
//                        
//                    }
//                }
//            }
        LOGGER.info(String.format("Received event, policy " + Configuration.getCurrentPolicy()));

        HashMap<String, HashMap<String, String>> data = processSensorLocationData.processData(event);
        if (Configuration.getCurrentPolicy().toLowerCase().contains("send_mixed") || Configuration.getCurrentPolicy().toLowerCase().contains("send_event")) {
            if (getRandomNumber() > 240)//TODO:  replace this with evaluation of event
            {
                LOGGER.info(String.format("!!~~!! urgent event, sending"));
                ProcessThread processThread = new ProcessThread(data);
                processThread.start();
            } else {
                LOGGER.info(String.format("Adding event to store"));
                if (Configuration.getCurrentPolicy().toLowerCase().contains("send_mixed")) {
                    storeData.add(data);
                }
            }
        } else {
            if (Configuration.getCurrentPolicy().toLowerCase().contains("send_all")) {
                LOGGER.info(String.format("Adding event to store"));
                storeData.add(data);

            }
        }

    }

    public int getRandomNumber() {
        Random random = new Random();
        return random.nextInt(300);
    }

    public void onException(JMSException e) {
        e.printStackTrace();
    }

    public int getBufferSize() {
        return storeData.size();
    }

    @Override
    public void run() {
        while (true) {
            if (storeData.size() > 0) {
                LOGGER.info("Start sending bulk data with size " + storeData.size());
                int size = storeData.size();
                List< HashMap<String, HashMap<String, String>>> newDMap = new ArrayList< HashMap<String, HashMap<String, String>>>();
                newDMap.addAll(storeData);
                int currentIndex = 0;
                LOGGER.info("~~~~~Sending from bulk...." + size);

                while (currentIndex < size) {
                    try {
                        int i = 0;
                        while (currentIndex < size && i < BURST_SIZE) {
                            EventProcessingM2MInterraction eventProcessingM2MInterraction = new EventProcessingM2MInterraction();
                            eventProcessingM2MInterraction.putData(newDMap.get(currentIndex));
                            i++;
                            currentIndex++;
                        }
                        Thread.sleep(BURST_SLEEP);
                    } catch (InterruptedException ex) {
                        LOGGER.info(ex.getMessage());
                    }
                }

                for (int i = 0; i < size; i++) {
                    storeData.remove(0);
                }

            } else {
                LOGGER.info("Data repo empty");

            }
            try {
                Random r = new Random();
                r.nextInt(REFRESH_TIME);
                Thread.sleep(REFRESH_TIME);
            } catch (InterruptedException ex) {
                LOGGER.info(ex.getMessage());
            }
        }
    }

    public void close() {
        try {
            consumer.close();
            session.close();
            connection.close();
        } catch (JMSException e) {
            e.printStackTrace();
        }

    }
}
