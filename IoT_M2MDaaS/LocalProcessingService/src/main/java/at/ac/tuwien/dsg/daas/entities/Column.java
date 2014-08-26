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
 * Author: Daniel Moldovan Institution: Vienna University of Technology Column
 * type list CQL3 data type |	Java type ascii	| java.lang.String bigint	| long
 * blob	| java.nio.ByteBuffer boolean	| boolean counter	| long decimal	| float
 * double	| double float	| float inet	| ava.net.InetAddress int	| int list	|
 * java.util.List<T>
 * map	| java.util.Map<K, V>
 * set	| java.util.Set<T>
 * text	| java.lang.String timestamp | java.util.Date timeuuid	| java.util.UUID
 * uuid	| java.util.UUID varchar	| java.lang.String varint	|
 * java.math.BigInteger
 *
 */
@XmlRootElement(name = "Column")
@XmlAccessorType(XmlAccessType.FIELD)
public class Column {

    @XmlAttribute(name = "name", required = true)
    private String name;

    //TODO: we can change the type with an ENUM to be easy to input (if needed)
    @XmlAttribute(name = "type", required = true)
    private String type;

    public Column() {
    }

    public Column(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Column withName(final String name) {
        this.name = name;
        return this;
    }

    public Column withType(final String type) {
        this.type = type;
        return this;
    }

}
