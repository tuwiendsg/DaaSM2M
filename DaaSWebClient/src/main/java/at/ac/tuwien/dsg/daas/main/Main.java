/**
 * Copyright 2013 Technische Universitaet Wien (TUW), Distributed Systems Group
 * E184
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package at.ac.tuwien.dsg.daas.main;

import org.apache.activemq.broker.BrokerService;

/**
 *
 * @Author Daniel Moldovan
 * @E-mail: d.moldovan@dsg.tuwien.ac.at
 *
 */
public class Main {

    public static void main(String[] args) throws Exception {

        //create embedded ActiveMQ broker
        BrokerService broker = new BrokerService();
        broker.setBrokerName("DaasM2MBroker");
        broker.addConnector(System.getProperty("ActiveMQProtocol", "tcp") + "://" + System.getProperty("ActiveMQIP", "localhost") + ":" + System.getProperty("ActiveMQPort", "61616"));
        broker.start();
        System.err.println("Started DaaS ActiveMQ broker");
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
