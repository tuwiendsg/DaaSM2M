/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.daas.entities;

import java.util.ArrayList;
import java.util.Collection;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * Author: Daniel Moldovan Institution: Vienna University of Technology
 */
@XmlRootElement(name = "Table")
@XmlAccessorType(XmlAccessType.FIELD)
public class Table {

    @XmlAttribute(name = "name", required = true)
    private String name;

    @XmlElement(name = "Keyspace", required = true)
    private Keyspace keyspace;

    @XmlAttribute(name = "primaryKeyName", required = true)
    private String primaryKeyName;

    @XmlAttribute(name = "primaryKeyType", required = true)
    private String primaryKeyType;

    @XmlElement(name = "Column", required = true)
    private Collection<Column> columns;

    {
        columns = new ArrayList<Column>();
    }

    public Table() {
    }

    public Table(Keyspace keyspace, String name, String primaryKeyName, String primaryKeyType) {
        this.name = name;
        this.keyspace = keyspace;
        this.primaryKeyName = primaryKeyName;
        this.primaryKeyType = primaryKeyType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Keyspace getKeyspace() {
        return keyspace;
    }

    public void setKeyspace(Keyspace keyspace) {
        this.keyspace = keyspace;
    }

    public String getPrimaryKeyName() {
        return primaryKeyName;
    }

    public void setPrimaryKeyName(String primaryKeyName) {
        this.primaryKeyName = primaryKeyName;
    }

    public String getPrimaryKeyType() {
        return primaryKeyType;
    }

    public void setPrimaryKeyType(String primaryKeyType) {
        this.primaryKeyType = primaryKeyType;
    }

    public Collection<Column> getColumns() {
        return columns;
    }

    public void setColumns(Collection<Column> columns) {
        this.columns = columns;
    }

    public void addColumn(Column column) {
        this.columns.add(column);
    }

    public void removeColumn(Column column) {
        this.columns.remove(column);
    }

    public Table withName(final String name) {
        this.name = name;
        return this;
    }

    public Table withKeyspace(final Keyspace keyspace) {
        this.keyspace = keyspace;
        return this;
    }

    public Table withPrimaryKeyName(final String primaryKeyName) {
        this.primaryKeyName = primaryKeyName;
        return this;
    }

    public Table withPrimaryKeyType(final String primaryKeyType) {
        this.primaryKeyType = primaryKeyType;
        return this;
    }

    public Table withColumns(final Collection<Column> columns) {
        this.columns = columns;
        return this;
    }

}
