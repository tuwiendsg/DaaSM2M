#!/bin/bash
CURRENT_DIR=
CASSANDRA_BIN=

source $CURRENT_DIR/Config

MACHINE_IP=$(ifconfig eth0 | grep -o 'inet addr:[0-9.]*' | grep -o [0-9.]*)
eval RPC_PORT=$CASSANDRA_RPC_PORT

result=$($CASSANDRA_BIN/cassandra-cli -h $MACHINE_IP -p $RPC_PORT -f $CURRENT_DIR/createDefaultCassandraKeyspaceScript.txt)

while [ -z "$result" ] || [[ $result == *"Not connected"* ]];do
  result=$($CASSANDRA_BIN/cassandra-cli -h $MACHINE_IP -p $RPC_PORT -f $CURRENT_DIR/createDefaultCassandraKeyspaceScript.txt)
  sleep 10;
done;



