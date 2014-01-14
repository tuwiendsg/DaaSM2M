#!/bin/bash
#Licensed under the EUPL V.1.1 
#http://joinup.ec.europa.eu/software/page/eupl/licence-eupl

source ./Config

CURRENT_DIR=$(pwd)

CASSANDRA_HOME=$CURRENT_DIR/apache-cassandra-1.2.6
CASSANDRA_BIN=$CASSANDRA_HOME/bin
CASSANDRA_CONFIG=$CASSANDRA_HOME/conf

echo "Delete files from /var/lib/cassandra/commitlog/ and /var/lib/cassandra/data/ as to make sure new tokens are assigned when booting the VM?"

select REPLY in "Yes" "No" ; do

case $REPLY in
	'Yes' )
          sudo -S rm -rf  /var/lib/cassandra/commitlog/*.*;
          sudo -S rm -rf  /var/lib/cassandra/commitlog/*;
          sudo -S rm -rf  /var/lib/cassandra/data/*.*;
          sudo -S rm -rf  /var/lib/cassandra/data/*;
         break;;
        'No')
 	 break;;
esac
done       

echo ""
echo "Setup Cassandra Seed or Data Node?"
select REPLY in "Seed (Main Node)" "Data Node (Slave)" ; do

case $REPLY in
        'Seed (Main Node)' )
 
           #Ensures that if a VM image is created and booted, at boot time the machine IP will be inserted in Cassandra config file
	   sudo -S sed -i 's/exit 0//' /etc/rc.local 
	   echo "eval \"sed -i.bak 's/listen_address:.*/listen_address: \`ifconfig eth0 | grep -o 'inet addr:[0-9.]*' | grep -o [0-9.]*\`/' $CASSANDRA_CONFIG/cassandra.yaml\"" | sudo -S tee -a /etc/rc.local
           echo "eval \"sed -i.bak 's/- seeds:.*/- seeds: \\\"\`ifconfig eth0 | grep -o 'inet addr:[0-9.]*' | grep -o [0-9.]*\`\\\"/' $CASSANDRA_CONFIG/cassandra.yaml\"" | sudo -S tee -a /etc/rc.local
	   echo "eval \"sed -i.bak 's/rpc_address:.*/rpc_address: \`ifconfig eth0 | grep -o 'inet addr:[0-9.]*' | grep -o [0-9.]*\`/' $CASSANDRA_CONFIG/cassandra.yaml\"" | sudo -S tee -a /etc/rc.local
           echo "eval \"sed -i 's/rpc_port:.*/rpc_port: $CASSANDRA_RPC_PORT/' $CASSANDRA_CONFIG/cassandra.yaml\"" | sudo -S tee -a /etc/rc.local
	   echo "eval \"sed -i 's/storage_port:.*/storage_port: $CASSANDRA_TCP_PORT/' $CASSANDRA_CONFIG/cassandra.yaml\"" | sudo -S tee -a /etc/rc.local               
	   echo "su $USER -c $CURRENT_DIR/createCassandraKeyspace.sh" | sudo -S tee -a /etc/rc.local
           echo "exit 0" | sudo -S tee -a /etc/rc.local

           sudo -S chmod +x ./createCassandraKeyspace.sh 
	
	   #apply configuration
	   #/etc/rc.local
 
           #prepare script that scales down the cluster by removing the less loaded node
           eval "sed -i 's#\<CASSANDRA_BIN=.*#CASSANDRA_BIN=$CASSANDRA_BIN#' ./scaleInCluster.sh"
           eval "sed -i 's#\<CASSANDRA_BIN=.*#CASSANDRA_BIN=$CASSANDRA_BIN#' ./createCassandraKeyspace.sh"
           eval "sed -i 's#\<CURRENT_DIR=.*#CURRENT_DIR=$CURRENT_DIR#' ./createCassandraKeyspace.sh"
   		

           sudo -S chmod +x ./scaleInCluster.sh
           sudo -S chmod +x ./checkCassandraStatus.sh
           sudo -S cp ./scaleInCluster.sh /bin/scaleIn 
      
 
           #prepare script for decomissioning node
	   # execute 'decomission CASSANDRA_NODE_IP' before removing Cassandra Node, to ensure proper cleanup	
	   eval "sed -i 's#\<JAVA=.*#JAVA=$JAVA_HOME/bin/java#' ./decommissionNode.sh"
           eval "sed -i 's#\<CASSANDRA_BIN=.*#CASSANDRA_BIN=$CASSANDRA_BIN#' ./decommissionNode.sh"
           sudo -S chmod +x ./decommissionNode.sh
	   sudo -S ln -s $CURRENT_DIR/decommissionNode.sh /bin/decommission

           #Copy script to ensure cassandra main starts automatically
	   sudo -S cp ./cassandra /etc/init.d/cassandra
	   sudo -S chmod +x /etc/init.d/cassandra
	   sudo -S update-rc.d cassandra defaults
           #does NOT start cassandra automatically, as not to generate tokens and linked to one token to a particular IP, which is bad when cloning/snapshoting the VM
      
           echo "If this is not to be snapshotted and used in connection with YCSB please reboot OS (sudo reboot) to finish configuration".

           break;;


        'Data Node (Slave)' )
           
           #prepare script for gathering seed and current IP and configuring Cassandra
           eval "sed -i 's#\<CASSANDRA_CONFIG=.*#CASSANDRA_CONFIG=$CASSANDRA_CONFIG#' ./joinRing.sh";
           eval "sed -i 's#\<CASSANDRA_BIN=.*#CASSANDRA_BIN=$CASSANDRA_BIN#' ./joinRing.sh";
	   
	   #execute the specified command for retrieving user supplied VM contextualization data
           eval "sed -i 's#\<cassandraSeedIPSource=.*#cassandraSeedIPSource=\"$CASSANDRA_SEED_IP_SOURCE\"#' ./joinRing.sh";            

           #Ensures that if a VM image is created and booted, at boot time the machine IP will be inserted in Cassandra config file by inserting in the /etc/rc.local
           sudo -S sed -i 's/exit 0//' /etc/rc.local 
           echo "eval \"sed -i.bak 's/listen_address:.*/listen_address: \`ifconfig eth0 | grep -o 'inet addr:[0-9.]*' | grep -o [0-9.]*\`/' $CASSANDRA_CONFIG/cassandra.yaml\"" | sudo -S tee -a /etc/rc.local
           echo "eval \"sed -i.bak 's/rpc_address:.*/rpc_address: \`ifconfig eth0 | grep -o 'inet addr:[0-9.]*' | grep -o [0-9.]*\`/' $CASSANDRA_CONFIG/cassandra.yaml\"" | sudo -S tee -a /etc/rc.local
           echo "eval \"sed -i 's/rpc_port:.*/rpc_port: $CASSANDRA_RPC_PORT/' $CASSANDRA_CONFIG/cassandra.yaml\"" | sudo -S tee -a /etc/rc.local
	   echo "eval \"sed -i 's/storage_port:.*/storage_port: $CASSANDRA_TCP_PORT/' $CASSANDRA_CONFIG/cassandra.yaml\"" | sudo -S tee -a /etc/rc.local         
           echo "exec $CURRENT_DIR/joinRing.sh" | sudo -S tee -a /etc/rc.local
           echo "exit 0" | sudo -S tee -a /etc/rc.local
	   #execute rc.local to configure local iP
           #/etc/rc.local
           sudo -S chmod +x ./joinRing.sh
           sudo -S ln -s $CURRENT_DIR/joinRing.sh /bin/joinRing
           #if node, ensure it does not start automatically
           if [ -f /etc/init.d/cassandra ]
		then
		   sudo rm /etc/init.d/cassandra
           fi     
           #NOTE: the cassandra service is not started yet, as not to generate any load token yet. To start the service one must call "joinRing".
           echo "If this is not to be snapshotted, please reboot OS (sudo reboot) to finish configuration".
           break;;
esac
done

eval "sed -i 's#\<CASSANDRA_BIN=.*#CASSANDRA_BIN=$CASSANDRA_BIN#' ./checkCassandraStatus.sh";

echo "Scripts for Elastic Cassandra Cluster deployed successfully."
