/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.daas.util;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Daniel Moldovan E-Mail: d.moldovan@dsg.tuwien.ac.at
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "MonitoringData")
public class MonitoringData {

    @XmlElement(name = "responseTime", required = true)
    private Long averageResponseTime;

    @XmlElement(name = "throughput", required = true)
    private Long averageTroughput;

    public MonitoringData() {
    }

    public MonitoringData(Long averageResponseTime, Long averageTroughput) {
        this.averageResponseTime = averageResponseTime;
        this.averageTroughput = averageTroughput;
    }

    public Long getAverageResponseTime() {
        return averageResponseTime;
    }

    public void setAverageResponseTime(Long averageResponseTime) {
        this.averageResponseTime = averageResponseTime;
    }

    public Long getAverageTroughput() {
        return averageTroughput;
    }

    public void setAverageTroughput(Long averageTroughput) {
        this.averageTroughput = averageTroughput;
    }

}
