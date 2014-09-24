#!/bin/bash
 
CASSANDRA_NATIVE_CQL_PORT=9142
CASSANDRA_NODE_IP=
AMQP_IP=localhost
AMQP_PORT=9124
AMQP_QUEUE_NAME=DB_LOG
#CASSANDRA, MOM, or BOTH
#PROFILES="CASSANDRA,MOM"
#PROFILES="MOM"
#PROFILES="CASSANDRA"

PROFILES="CASSANDRA,MOM"

#CASSANDRA_NODE_IP=$head2datanode_IP

CURRENT_DIR=$(pwd)

#wget -nv http://128.130.172.215/salsa/upload/files/daas/DaaS-1.0.tar.gz
#tar -xzf ./DaaS-1.0.tar.gz
#cd ./DaaS-1.0


#set daas-service current dir
eval "sed -i 's#DAEMONDIR=.*#DAEMONDIR=$CURRENT_DIR#' $CURRENT_DIR/daas-service"
eval "sed -i 's#CassandraNode\.IP=.*#CassandraNode.IP=$CASSANDRA_NODE_IP#' ../config/daas.properties"
eval "sed -i 's#CassandraNode\.PORT=.*#CassandraNode.PORT=$CASSANDRA_NATIVE_CQL_PORT#' ../config/daas.properties"
eval "sed -i 's#AMQP\.IP=.*#AMQP.IP=$AMQP_IP#' ../config/daas.properties"
eval "sed -i 's#AMQP\.PORT=.*#AMQP.PORT=$AMQP_PORT#' ../config/daas.properties"
eval "sed -i 's#AMQP\.QUEUE_NAME=.*#AMQP_QUEUE_NAME=$AMQP_QUEUE_NAME#' ../config/daas.properties"

#update DaaS profiles: usign CASSANDRA backend, MOM (AMQP), or both?
eval "sed -i 's#PROFILES=.*#PROFILES=$PROFILES#' ../config/daas-service"

#put daa-service in services
sudo -S cp ./daas-service /etc/init.d/daas-service
sudo -S chmod +x /etc/init.d/daas-service
sudo -S update-rc.d daas-service defaults

sudo service daas-service start

cd ./gangliaPlugIns
./setupPlugIns.sh
sudo -S service ganglia-monitor restart
