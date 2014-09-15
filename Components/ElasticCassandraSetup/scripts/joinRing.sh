#!/bin/bash
#Licensed under the EUPL V.1.1 
#http://joinup.ec.europa.eu/software/page/eupl/licence-eupl
#Retrieves the current machine IP and sets it as listen address, such that cassandra operations work on this node

cassandraSeedIPSource=
CASSANDRA_CONFIG=
CASSANDRA_BIN=
cassandraConfFile=$CASSANDRA_CONFIG/cassandra.yaml

if [ -z "$1" ] 
 then 
     #if no IP supplied, try to use contextualization
     SEED_IP=`eval $cassandraSeedIPSource`   
     if [ $? -eq 0 ];then
   	echo "Command $cassandraSeedIPSource executed successfully"
     else
       echo "Error executing $cassandraSeedIPSource. Using it as SEED_IP string"
       SEED_IP=$cassandraSeedIPSource
     fi  
 else
     SEED_IP=$1
fi

eval "sed -i.bak 's/- seeds: .*/- seeds: \"$SEED_IP\"/' $cassandraConfFile"
$CASSANDRA_BIN/cassandra



