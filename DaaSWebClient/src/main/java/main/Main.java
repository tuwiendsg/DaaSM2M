/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.File;
import org.apache.catalina.startup.Tomcat;

/**
 *
 * @author Daniel Moldovan E-Mail: d.moldovan@dsg.tuwien.ac.at
 */
public class Main {

    public static void main(String[] args) throws Exception {

        //create embedded ActiveMQ broker
//        BrokerService broker = new BrokerService();
//        broker.setBrokerName("DaasM2MBroker");
//        broker.addConnector(System.getProperty("ActiveMQProtocol", "tcp") + "://" + System.getProperty("ActiveMQIP", "localhost") + ":" + System.getProperty("ActiveMQPort", "61616"));
//        broker.start();
//        System.err.println("Started DaaS ActiveMQ broker");
        String webappDirLocation = "webapp/";
        Tomcat tomcat = new Tomcat();

        //The port that we should run on can be set into an environment variable
        //Look for that variable and default to 8080 if it isn't there.
//        String webPort = System.getenv("PORT");
//        if(webPort == null || webPort.isEmpty()) {
//            webPort = "8080";
//        }
//        tomcat.setPort(Integer.valueOf(webPort));
        tomcat.addWebapp("/DaaS", new File(webappDirLocation).getAbsolutePath());
        System.out.println("configuring app with basedir: " + new File("./" + webappDirLocation).getAbsolutePath());

        tomcat.getConnector().setAttribute("maxThreads", "2000");
        tomcat.getConnector().setAttribute("acceptCount", "2000");
        tomcat.start();
        tomcat.getServer().await();

//
//        //start REST api
//        int port = Integer.parseInt(System.getProperty("jetty.port", "8080"));
//        Server server = new Server(port);
//
//        ProtectionDomain domain = Main.class.getProtectionDomain();
//        URL location = domain.getCodeSource().getLocation();
//
//        WebAppContext webapp = new WebAppContext();
//        webapp.setContextPath("/");
//        webapp.setDescriptor(location.toExternalForm() + "/WEB-INF/web.xml");
//        webapp.setServer(server);
//        webapp.setWar(location.toExternalForm());
//
//        // (Optional) Set the directory the war will extract to.
//        // If not set, java.io.tmpdir will be used, which can cause problems
//        // if the temp directory gets cleaned periodically.
//        // Your build scripts should remove this directory between deployments
//        // webapp.setTempDirectory(new File("/path/to/webapp-directory"));
//
//        server.setHandler(webapp);
//        server.start();
//        server.join();
    }
}
