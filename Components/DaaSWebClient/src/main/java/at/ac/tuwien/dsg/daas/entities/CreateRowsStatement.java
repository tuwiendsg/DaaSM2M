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
 * Author: Daniel Moldovan Institution: Vienna University of Technology
 */
@XmlRootElement(name = "CreateRowsStatement")
@XmlAccessorType(XmlAccessType.FIELD)
public class CreateRowsStatement {

    @XmlElement(name = "Table", required = true)
    private Table table;
    
    @XmlElement(name = "Row", required = true)
    private Collection<TableRow> rows;

    {
        rows = new ArrayList<TableRow>();
    }

    public CreateRowsStatement() {
    }
    
    
    
    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public Collection<TableRow> getRows() {
        return rows;
    }

    public void setRows(Collection<TableRow> row) {
        this.rows = row;
    }
    
    public void addRow(TableRow row){
        rows.add(row);
    }
    
    
    public void removeRow(TableRow row){
        rows.remove(row);
    }
    
    
}
