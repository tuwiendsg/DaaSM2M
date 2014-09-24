package at.ac.tuwien.dsg.queue.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.xml.bind.JAXBContext;
import java.util.Enumeration;
import at.ac.tuwien.dsg.daas.entities.CreateRowsStatement;
import java.io.StringReader;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

/**
 * Created by omoser on 1/17/14.
 */
@Component
public class QueueConsumer implements MessageListener {

    static final Logger log = LoggerFactory.getLogger(QueueConsumer.class);

    public void onMessage(Message message) {

        if (message instanceof MapMessage) {

            try {

                MapMessage mapMessage = (MapMessage) message;
                Enumeration fieldsNames = mapMessage.getMapNames();
                while (fieldsNames.hasMoreElements()) {

                    String sensorOperation = fieldsNames.nextElement().toString();
                    String data = mapMessage.getString(sensorOperation);
                    log.info("Received " + sensorOperation + data);
                    System.out.println("Received " + sensorOperation + data);
                    //depending on the type of received data, it can be converted with JAXB to classed used by the DaaS, such as TableRow
                    //the type of operation is shown in at.ac.tuwien.dsg.daas.impl.AMQPConnector in DaaSWebService project
                    if (sensorOperation.equals("INSERT_ROWS")) {
                        JAXBContext bContext = JAXBContext.newInstance(CreateRowsStatement.class);
                        Unmarshaller um = bContext.createUnmarshaller();
                        CreateRowsStatement crs = (CreateRowsStatement) um.unmarshal(new StringReader(data));
                        log.info("CreateRowsStatement for rows {} " + crs.getRows().size());
                    }
                }

            } catch (JMSException ex) {
                log.error(ex.getMessage(), ex);
            } catch (JAXBException ex) {
                log.error(ex.getMessage(), ex);
            }
        } else {
            System.out.println("Unrecognized message: " + message);
        }

    }
}
