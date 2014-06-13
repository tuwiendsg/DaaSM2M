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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created with IntelliJ IDEA. User: daniel-tuwien Date: 4/29/13 Time: 2:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class Monitor {

    private int monitoringIntervalInMilliseconds;

    private AtomicLong responseTime;
    private AtomicLong troughput;

    private AtomicLong averageResponseTime;
    private AtomicLong averageTroughput;

    private AtomicInteger outstandingRequestsNumber;

    //map RequestID-RequestIssueDate
    //this is to be used to monitor growing response time for very long queries
    //this means, if we have a long query taking 5 seconds, we would get as respinse time monitoring data 1,2,3,4,5, not 0,0,0,0,5 
    private Map<Integer, Date> outstandingRequests;

    private Monitor selfReference;
    //used to reset monitoring counter
    //an issue is that if I reset it automatically at 1 second, if there are long running querries,
    //the response time will be 0 for a long time, and then will be very large, and then small again
    boolean monitoringDataRead = false;

    {
        averageResponseTime = new AtomicLong(0);
        averageTroughput = new AtomicLong(0);
        responseTime = new AtomicLong(0);
        troughput = new AtomicLong(0);
        outstandingRequestsNumber = new AtomicInteger(0);
        outstandingRequests = new HashMap<Integer, Date>();
    }

    public Monitor(int monitoringIntervalInMilliseconds) {

        this.monitoringIntervalInMilliseconds = monitoringIntervalInMilliseconds;
        selfReference = this;
        Thread monitoringThread = new Thread() {

            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(selfReference.monitoringIntervalInMilliseconds);
                    } catch (InterruptedException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }

                    selfReference.recordMonitoring();
                }
            }
        };

        monitoringThread.start();

    }

    public synchronized void addOutstandingRequest(Integer requestID, Date requestTime) {
        outstandingRequests.put(requestID, requestTime);
    }

    public synchronized void removeOutstandingRequest(Integer requestID, Date requestTime) {
        monitorRT(requestID, requestTime);
    }

    /**
     * @param newResponseTime in milliseconds
     */
    private synchronized void monitorRT(Integer requestID, Date endTime) {

        //remove request from outstanding
        Date requestTime = outstandingRequests.remove(requestID);
        Long newResponseTime = endTime.getTime() - requestTime.getTime();

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
    public synchronized Number[] getMonitoringData() {
        return new Number[]{averageResponseTime.get(), averageTroughput.get(), outstandingRequestsNumber};
    }

    private synchronized void recordMonitoring() {
        long avgTpt = troughput.get();
        troughput.set(0);
        responseTime.set(0);
        outstandingRequestsNumber.set(outstandingRequests.size());

        if (avgTpt > 0 || !outstandingRequests.isEmpty()) {
            Long avgRTForEndedRequests = responseTime.get();
            //go trough all outstanding requests and add their time to avg RT
            Date now = new Date();
            for (Date startedRequestTime : outstandingRequests.values()) {
                avgRTForEndedRequests += now.getTime() - startedRequestTime.getTime();
            }

            //divide by executed requests (troughput) and nr of ongoing requests
            avgRTForEndedRequests /= (avgTpt + outstandingRequests.size());
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
    }

}
