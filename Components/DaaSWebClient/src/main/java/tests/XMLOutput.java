/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tests;

import at.ac.tuwien.dsg.daas.entities.Column;
import at.ac.tuwien.dsg.daas.entities.CreateRowsStatement;
import at.ac.tuwien.dsg.daas.entities.Keyspace;
import at.ac.tuwien.dsg.daas.entities.RowColumn;
import at.ac.tuwien.dsg.daas.entities.Table;
import at.ac.tuwien.dsg.daas.entities.TableQuery;
import at.ac.tuwien.dsg.daas.entities.TableRow;
import java.io.File;
import java.io.IOException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.SchemaOutputResolver;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;

/**
 *
 * Author: Daniel Moldovan Institution: Vienna University of Technology
 */
public class XMLOutput {

    public static void main(String[] args) throws JAXBException, IOException {

        Keyspace keyspace = new Keyspace("testKeyspace");

        Table table = new Table(keyspace, "TableX", "key", "int");
        //create table columns
        {
            table.addColumn(new Column("sensorName", "text"));
            table.addColumn(new Column("sensorValue", "double"));
        }

        //the add row statement used in adding rows to the DB
        CreateRowsStatement createRowsStatement = new CreateRowsStatement();
        {
            Table t = new Table();
            t.setName("TableX");
            t.setKeyspace(keyspace);
            createRowsStatement.setTable(t);
            //create a row used in adding rows to the database table

            {
                TableRow tableRow = new TableRow();
                tableRow.addRowColumn(new RowColumn("sensorName", "SensorX"));
                tableRow.addRowColumn(new RowColumn("sensorValue", "123.5"));
                createRowsStatement.addRow(tableRow);
            }


            {
                TableRow tableRow = new TableRow();
                tableRow.addRowColumn(new RowColumn("sensorName", "SensorY"));
                tableRow.addRowColumn(new RowColumn("sensorValue", "555.3"));
                createRowsStatement.addRow(tableRow);
            }
        }


        //write the TableQuerry object used in retrieving and deleting rows from the table
        TableQuery query = new TableQuery();
        {
            Table t = new Table();
            t.setName("TableX");
            t.setKeyspace(keyspace);
            query.setTable(t);
            query.setCondition("key = 1 AND sensorvalue = 555.3");
        }

        //output keyspace example to have a format and XSD to play with
        {


            JAXBContext jaxbc = JAXBContext.newInstance(Keyspace.class);
            Marshaller marshaller = jaxbc.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(keyspace, new File("./xmls/keyspace.xml"));
            jaxbc.generateSchema(new MySchemaOutputResolver("./xmls/keyspace.xsd"));
        }


        //output Table example to have a format and XSD to play with
        {


            JAXBContext jaxbc = JAXBContext.newInstance(Table.class);
            Marshaller marshaller = jaxbc.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(table, new File("./xmls/table.xml"));
            jaxbc.generateSchema(new MySchemaOutputResolver("./xmls/table.xsd"));
        }



        //output CreateRowsStatement to have a format and XSD to play with
        {


            JAXBContext jaxbc = JAXBContext.newInstance(CreateRowsStatement.class);
            Marshaller marshaller = jaxbc.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(createRowsStatement, new File("./xmls/createRowsStatement.xml"));
            jaxbc.generateSchema(new MySchemaOutputResolver("./xmls/createRowsStatement.xsd"));
        }
        
        
        //output TableQuerry example to have a format and XSD to play with
        {


            JAXBContext jaxbc = JAXBContext.newInstance(TableQuery.class);
            Marshaller marshaller = jaxbc.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(query, new File("./xmls/tableQuery.xml"));
            jaxbc.generateSchema(new MySchemaOutputResolver("./xmls/tableQuery.xsd"));
        }
    }

    private static class MySchemaOutputResolver extends SchemaOutputResolver {

        private String usedFileName;

        public MySchemaOutputResolver(String usedFileName) {
            this.usedFileName = usedFileName;
        }

        public Result createOutput(String namespaceURI, String suggestedFileName) throws IOException {
            File file = new File(usedFileName);
            StreamResult result = new StreamResult(file);
            result.setSystemId(file.toURI().toURL().toString());
            return result;
        }
    }
}
