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





public class MQConsumer implements MessageListener, ExceptionListener,Runnable {

	private static final Logger LOGGER = Logger.getLogger(ProcessSensorLocationData.class);
	private String subject;
	private String user = ActiveMQConnection.DEFAULT_USER;
	private String password = ActiveMQConnection.DEFAULT_PASSWORD;
	private String url = "";
	private boolean transacted;
	private int ackMode = Session.AUTO_ACKNOWLEDGE;
	private Session session;
	private Destination destination;
	private MessageConsumer consumer = null;
	private Connection connection;
          private ProcessData processSensorLocationData = new ProcessSensorLocationData();
        private int REFRESH_TIME=Configuration.getRefreshRate();
        private List< HashMap<String,HashMap<String,String>> > storeData = new ArrayList< HashMap<String,HashMap<String,String>> >();
         private Thread t;
	public MQConsumer(String url, String topic) {
		this.url = url;
		this.subject = topic;
                t = new Thread(this);
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
		LOGGER.info(String.format("Received event: %s", event));

                HashMap<String,HashMap<String,String>> data= processSensorLocationData.processData(event);
                if (getRandomNumber()>240)
                { 
                    LOGGER.info(String.format("!!~~!! urgent event, sending" ));
                  ProcessThread processThread = new ProcessThread(data);
                  processThread.start();
                }   
                storeData.add(data);
	}
        public int getRandomNumber() {
            Random random = new Random();
            return random.nextInt(300);
        }
	public void onException(JMSException e) {
		e.printStackTrace();
	}
        public int getBufferSize(){
            return storeData.size();
        }
        
        @Override
        public void run(){
            while (true){
                if (storeData.size()>0)
                    LOGGER.info("Start sending bulk data with size "+storeData.size());
                else
                    LOGGER.info("Data repo empty");
                int size = storeData.size();
                 List< HashMap<String,HashMap<String,String>> > newDMap = new ArrayList< HashMap<String,HashMap<String,String>> > ();
                 newDMap.addAll(storeData);
                 
                for (int i=0;i<size; i++ ){
                     LOGGER.info("~~~~~Sending from bulk...."+i);
                    EventProcessingM2MInterraction eventProcessingM2MInterraction = new EventProcessingM2MInterraction();
                    eventProcessingM2MInterraction.putData(storeData.get(i));
                     
                }
                for (int i=0; i<size ; i++)
                {
                    storeData.remove(0);
                }
                try {
                    Random r = new Random();
                    r.nextInt(REFRESH_TIME);
                    Thread.sleep(REFRESH_TIME);
                } catch (InterruptedException ex) {
                    java.util.logging.Logger.getLogger(MQConsumer.class.getName()).log(Level.SEVERE, null, ex);
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
