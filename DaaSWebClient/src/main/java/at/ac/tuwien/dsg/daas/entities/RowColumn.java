/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.daas.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * Author: Daniel Moldovan Institution: Vienna University of Technology
 * Used for queries: Has value, but Column type has type and name, not name and value
 */
@XmlRootElement(name = "Column")
@XmlAccessorType(XmlAccessType.FIELD)
public class RowColumn {

    @XmlAttribute(name = "name", required = true)
    private String name;
    @XmlAttribute(name = "value", required = true)
    private String value;

    public RowColumn() {
    }

    public RowColumn(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        if (value == null) {
            return null;
        }
        //if value is int
        if (value.matches("[0-9]*")) {
            try {
                return "" + Integer.parseInt(value.toString());
            } catch (NumberFormatException exception) {
                return "'" + value + "'";
            }
        } else if (value.matches("[0-9.]*")) { //if double
            try {
                return "" + Double.parseDouble(value.toString());
            } catch (NumberFormatException exception) {
                return "'" + value + "'";
            }
        } else if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) { //if boolean
            return "" + value;
        } else if (value.matches("[a-zA-Z0-9]*")) { //if string
            return "'" + value + "'";
        } else { //else just submit the value as it is (for date and etc)
            return "" + value;
        }
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "RowColumn{" + "name=" + name + ", value=" + value + '}';
    }
}
