/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.daas.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * Author: Daniel Moldovan Institution: Vienna University of Technology
 */
@XmlRootElement(name = "Keyspace")
@XmlAccessorType(XmlAccessType.FIELD)
public class Keyspace {

    @XmlAttribute(name = "name", required = true)
    private String name;

    public Keyspace() {
    }

    public Keyspace(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Keyspace withName(final String name) {
        this.name = name;
        return this;
    }

}
