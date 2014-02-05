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

    private Monitor selfReference;

    public Monitor(int monitoringIntervalInMilliseconds) {
        averageResponseTime = new AtomicLong(0);
        averageTroughput = new AtomicLong(0);
        responseTime = new AtomicLong(0);
        troughput = new AtomicLong(0);

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

    /**
     * @param newResponseTime in milliseconds
     */
    public synchronized void monitorRT(Long newResponseTime) {
        responseTime.addAndGet(newResponseTime);
        troughput.incrementAndGet();
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
        troughput.set(0);
        responseTime.set(0);
    }

}
