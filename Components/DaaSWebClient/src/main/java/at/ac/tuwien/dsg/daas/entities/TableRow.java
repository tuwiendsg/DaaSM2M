/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.daas.entities;

import java.util.ArrayList;
import java.util.Collection;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * Author: Daniel Moldovan Institution: Vienna University of Technology Used for
 * queries: Has value, but Column type has type and name, not name and value
 */
@XmlRootElement(name = "Row")
@XmlAccessorType(XmlAccessType.FIELD)
public class TableRow {

    @XmlElement(name = "Column", required = true)
    private Collection<RowColumn> values;

    {
        values = new ArrayList<RowColumn>();
    }

    public Collection<RowColumn> getValues() {
        return values;
    }

    public void setValues(Collection<RowColumn> values) {
        this.values = values;
    }

    public void addRowColumn(RowColumn column) {
        this.values.add(column);
    }

    public void removeRowColumn(RowColumn column) {
        this.values.remove(column);
    }

    @Override
    public String toString() {
        return "TableRow{" + "values=" + values + '}';
    }

    public TableRow withValues(final Collection<RowColumn> values) {
        this.values = values;
        return this;
    }

}
