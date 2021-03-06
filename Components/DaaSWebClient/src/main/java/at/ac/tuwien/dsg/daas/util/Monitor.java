/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.daas.util;

/**
 *
 * @author daniel-tuwien
 */
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * Created with IntelliJ IDEA. User: daniel-tuwien Date: 4/29/13 Time: 2:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class Monitor {

    private int monitoringIntervalInMilliseconds = 1000;

    private AtomicLong responseTime;
    private AtomicLong troughput;

    private AtomicLong averageResponseTime;
    private AtomicLong averageTroughput;

    private AtomicInteger outstandingRequestsNumber;

    //map RequestID-RequestIssueDate
    //this is to be used to monitor growing response time for very long queries
    //this means, if we have a long query taking 5 seconds, we would get as respinse time monitoring data 1,2,3,4,5, not 0,0,0,0,5 
    private Map<Integer, Date> outstandingRequests;

    private static Monitor instance;
    //used to reset monitoring counter
    //an issue is that if I reset it automatically at 1 second, if there are long running querries,
    //the response time will be 0 for a long time, and then will be very large, and then small again
    private static boolean monitoringDataRead = false;

    {
        averageResponseTime = new AtomicLong(0);
        averageTroughput = new AtomicLong(0);
        responseTime = new AtomicLong(0);
        troughput = new AtomicLong(0);
        outstandingRequestsNumber = new AtomicInteger(0);
        outstandingRequests = new ConcurrentHashMap<Integer, Date>();
    }

    static {

        Timer monitoringTimer = new Timer();
        final Monitor monitor = new Monitor();
        TimerTask task = new TimerTask() {

            @Override
            public void run() {
                monitor.recordMonitoring();
            }
        };

        monitoringTimer.schedule(task, 0, monitor.monitoringIntervalInMilliseconds);

        instance = monitor;

    }

    private Monitor() {

    }

    public static Monitor getInstance() {
        return instance;
    }

    public void addOutstandingRequest(Integer requestID, Date requestTime) {
        outstandingRequests.put(requestID, requestTime);
    }

    public void removeOutstandingRequest(Integer requestID, Date requestTime) {
        monitorRT(requestID, requestTime);
    }

    /**
     * @param newResponseTime in milliseconds
     */
    private void monitorRT(Integer requestID, Date endTime) {

        //remove request from outstanding
        Date requestTime = outstandingRequests.remove(requestID);
        Long newResponseTime = endTime.getTime() - requestTime.getTime();

        if (newResponseTime < 0) {
            Logger.getLogger(Monitor.class.getName()).log(Level.ERROR, "Response time " + newResponseTime + " between now = " + endTime.toString() + " and starting " + requestTime.toString());
        }

//if monitoring data read, when we have new data, reset the monitoring items
//        if (monitoringDataRead) {
//            monitoringDataRead = false;
//
//            responseTime.set(newResponseTime);
//            troughput.set(1);
//        } else {
        //if response time > 1 second (1000 millis), then 
        Integer throughoutPerSeconds = (newResponseTime.intValue() > 1000) ? 1000 / newResponseTime.intValue() : 1;
        responseTime.addAndGet(newResponseTime);

        troughput.addAndGet(throughoutPerSeconds);
//        }
    }

    /**
     * @return long [0] - averageResponseTime in
     * monitoringIntervalInMilliseconds long [1] - troughput in
     * monitoringIntervalInMilliseconds
     */
    public Number[] getMonitoringData() {
        return new Number[]{averageResponseTime.get(), averageTroughput.get(), outstandingRequestsNumber};
    }

    private synchronized void recordMonitoring() {
        Logger.getLogger(Monitor.class.getName()).log(Level.DEBUG, "Record monitoring at " + new Date().toString());
      
        long avgTpt = troughput.get();
        long avgRTForEndedRequests = responseTime.get();
        outstandingRequestsNumber.set(outstandingRequests.size());

        if (avgTpt > 0 || !outstandingRequests.isEmpty()) {
            //go trough all outstanding requests and add their time to avg RT
            //querry only once to avoid issues with newly added tasks
            Collection<Date> startingDates = outstandingRequests.values();
            Date now = new Date();
            for (Date startedRequestTime : startingDates) {
                avgRTForEndedRequests += now.getTime() - startedRequestTime.getTime();
                if (now.getTime() - startedRequestTime.getTime() < 0) {
                    Logger.getLogger(Monitor.class.getName()).log(Level.ERROR, "Response time " + (now.getTime() - startedRequestTime.getTime()) + " between now = " + now.toString() + " and starting " + startedRequestTime.toString());
                }
            }

            //divide by executed requests (troughput)
            avgRTForEndedRequests /= (avgTpt > 0 )? avgTpt: 1;
            averageResponseTime.set(avgRTForEndedRequests);
            averageTroughput.set(avgTpt);
        } else {
            averageResponseTime.set(0);
            averageTroughput.set(0);
//        }
//        monitoringDataRead = true;
//        if (outstandingRequests.isEmpty()) {
//            troughput.set(0);
//            responseTime.set(0);
        }
        troughput.set(0);
        responseTime.set(0);
    }

}
