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
 * @author Georgiana Copil E-Mail: e.copil@dsg.tuwien.ac.at
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "TenantData")
public class TenantData {

    @XmlElement(name = "tenantID", required = true)
    private String tenantID;

    @XmlElement(name = "host", required = true)
    private String host;

    @XmlElement(name = "port", required = true)
    private String port;
    
    public TenantData() {
    }

    public TenantData(String tenantID, String host, String  port) {
        this.tenantID = tenantID;
        this.host = host;
        this.port=port;
                
    }

    /**
     * @return the tenantID
     */
    public String getTenantID() {
        return tenantID;
    }

    /**
     * @param tenantID the tenantID to set
     */
    public void setTenantID(String tenantID) {
        this.tenantID = tenantID;
    }

    /**
     * @return the host
     */
    public String getHost() {
        return host;
    }

    /**
     * @param host the host to set
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * @return the port
     */
    public String getPort() {
        return port;
    }

    /**
     * @param port the port to set
     */
    public void setPort(String port) {
        this.port = port;
    }


}
