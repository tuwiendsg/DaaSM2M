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
@XmlRootElement(name = "Query")
@XmlAccessorType(XmlAccessType.FIELD)
public class TableQuery {

    @XmlElement(name = "Table", required = true)
    private Table table;
    @XmlElement(name = "Condition", required = false)
    private String condition;

    //how many max rows to return
    @XmlElement(name = "MaxResultCount", required = false)
    private int maxResultCount = 1;
    
    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public int getMaxResultCount() {
        return maxResultCount;
    }

    public void setMaxResultCount(int maxResultCount) {
        this.maxResultCount = maxResultCount;
    }
    
    
    
    
}
