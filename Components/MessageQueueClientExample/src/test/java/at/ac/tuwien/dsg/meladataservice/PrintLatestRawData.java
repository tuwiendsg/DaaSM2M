///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package at.ac.tuwien.dsg.meladataservice;
//
//import at.ac.tuwien.dsg.mela.common.monitoringConcepts.MonitoredElement;
//import at.ac.tuwien.dsg.mela.common.monitoringConcepts.MonitoredEntry;
//import at.ac.tuwien.dsg.mela.dataservice.MELADataService;
//import at.ac.tuwien.dsg.mela.dataservice.persistence.PersistenceSQLAccess;
//import at.ac.tuwien.dsg.mela.dataservice.utils.Configuration;
//import java.io.BufferedWriter;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//
///**
// *
// * @author Daniel Moldovan E-Mail: d.moldovan@dsg.tuwien.ac.at
// */
//public class PrintLatestRawData {
//
//    public static void main(String[] args) throws IOException {
//        Thread t = new Thread() {
//
//            @Override
//            public void run() {
//                final MELADataService service = new MELADataService();
//                service.startServer();
//            }
//
//        };
//        t.setDaemon(true);
//        t.start();
//        PersistenceSQLAccess persistenceSQLAccess = new PersistenceSQLAccess("mela", "mela", Configuration.getDataServiceIP(), Configuration.getDataServicePort(),
//                "CloudService");
//        Map<MonitoredElement, List<MonitoredEntry>> data = persistenceSQLAccess.extractLatestRawMonitoringData();
//
//        List<List<String>> columns = new ArrayList<List<String>>();
//
//        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("./data"));
//
//        for (MonitoredElement element : data.keySet()) {
//            List<String> column = new ArrayList<String>();
//
//            column.add(element.getId());
//
//            List<MonitoredEntry> entries = data.get(element);
//
//            Collections.sort(entries, new Comparator<MonitoredEntry>() {
//
//                public int compare(MonitoredEntry o1, MonitoredEntry o2) {
//                    return o1.getMetric().getName().compareTo(o2.getMetric().getName());
//                }
//
//            });
//            for (MonitoredEntry me : entries) {
//                column.add(me.getMetric().getName() + ":" + me.getValue().getValueRepresentation());
//            }
//
//            columns.add(column);
//
//        }
//
//        while (!columns.isEmpty()) {
//            Iterator<List<String>> it = columns.iterator();
//            String line = "";
//            while (it.hasNext()) {
//                List<String> column = it.next();
//                if (!column.isEmpty()) {
//                    line += "," + column.remove(0);
//                } else {
//                    it.remove();
//                }
//            }
//            bufferedWriter.write(line);
//            bufferedWriter.newLine();
//        }
//
//        bufferedWriter.flush();
//        bufferedWriter.close();
//
//        t.interrupt();
//    }
//}
