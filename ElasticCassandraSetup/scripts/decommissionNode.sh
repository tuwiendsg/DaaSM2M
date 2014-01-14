#!/bin/bash
#Licensed under the EUPL V.1.1 
#http://joinup.ec.europa.eu/software/page/eupl/licence-eupl

CASSANDRA_BIN=
JAVA_HOME=
if [ -z $1 ]; then
   echo "No IP to decomission received";
   exit;
fi
echo -n "Decomissioning Cassandra Node... "
$CASSANDRA_BIN/nodetool -h $1 decommission
