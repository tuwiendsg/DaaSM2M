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
import at.ac.tuwien.dsg.daas.services.M2M;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

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

    private AtomicLong outstandingRequests;

    private Monitor selfReference;
    //used to reset monitoring counter
    //an issue is that if I reset it automatically at 1 second, if there are long running querries,
    //the response time will be 0 for a long time, and then will be  very large, and then small again
    boolean monitoringDataRead = false;

    {
        averageResponseTime = new AtomicLong(0);
        averageTroughput = new AtomicLong(0);
        responseTime = new AtomicLong(0);
        troughput = new AtomicLong(0);
        outstandingRequests = new AtomicLong(0);
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

    public synchronized void markOutstandingRequest() {
        outstandingRequests.incrementAndGet();
    }

    public synchronized void removeOutstandingRequest() {
        outstandingRequests.decrementAndGet();
    }

    /**
     * @param newResponseTime in milliseconds
     */
    public synchronized void monitorRT(Long newResponseTime) {
        //if monitoring data read, when we have new data, reset the monitoring items
        if (monitoringDataRead) {
            monitoringDataRead = false;
            responseTime.set(newResponseTime);
            troughput.set(1);
        } else {
            responseTime.addAndGet(newResponseTime);
            troughput.incrementAndGet();
        }
    }

    /**
     * @return long [0] - averageResponseTime in
     * monitoringIntervalInMilliseconds long [1] - troughput in
     * monitoringIntervalInMilliseconds
     */
    public synchronized long[] getMonitoringData() {
        return new long[]{averageResponseTime.get(), averageTroughput.get()};
    }

    private synchronized void recordMonitoring() {
        long avgTpt = troughput.get();
        if (avgTpt > 0) {
            averageResponseTime.set(responseTime.get() / avgTpt);
            averageTroughput.set(avgTpt);
        } else {
            averageResponseTime.set(0);
            averageTroughput.set(0);
        }
        monitoringDataRead = true;
        if (outstandingRequests.get() == 0) {
            troughput.set(0);
            responseTime.set(0);
        }
    }

}
