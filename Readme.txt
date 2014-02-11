DaaS M2M is an elastic application containing:
- scripts to install Cassandra NoSQL distributed data store , and configure Cassandra Nodes to automatically add to existing Cassandra ring, and the Cassandra Seed nodes for removing Data Nodes.
- a Java WEB application exposing RESTful services which interact with Cassandra Seed, which builds as executable war (embedded Tomcat), and scripts for automatically starting more Web Server instances and registering them in a load balancer
- scripts for installing and configuring HAPRoxy HTTP load balancer for easy addition and removal of application servers
- python scripts which generate random read/write requests, to act as a base for a simple workload generator.
- Ganglia plug-ins to monitor clientsCount, responseTime, throughput, and data read/write latency

Updated information and instructions can be found on the GitHub Wiki at https://github.com/tuwiendsg/DaaSM2M/wiki
