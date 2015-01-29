/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import at.ac.tuwien.dsg.daas.entities.Keyspace;
import at.ac.tuwien.dsg.daas.entities.Table;
import at.ac.tuwien.dsg.daas.entities.TableQuery;
import test.utils.EventProcessingM2MInterraction;

/**
 *
 * @author Daniel Moldovan E-Mail: d.moldovan@dsg.tuwien.ac.at
 */
public class ConnectToM2MTest {
    
    public static void main(String[] args) {
        EventProcessingM2MInterraction interraction = new EventProcessingM2MInterraction().withIP("128.130.172.191");
        TableQuery query = new TableQuery().withTable(
                new Table()
                .withKeyspace(
                        new Keyspace("m2m"))
                .withName("sensor")
        );
        interraction.getData(query);
    }
}
