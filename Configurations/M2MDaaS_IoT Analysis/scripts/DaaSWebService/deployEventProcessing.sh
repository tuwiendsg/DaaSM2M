#!/bin/bash


echo "Installing Event Processing \n" >> /tmp/salsa.artifact.log

. /etc/environment
 
wget -q  http://128.130.172.215/salsa/upload/files/DaasService/DaaS-1.0.tar.gz
tar -xzf ./DaaS-1.0.tar.gz

cd ./DaaS-1.0

CURRENT_DIR=$(pwd)
 
CASSANDRA_SEED_IP=$eventProcessingToDataController_IP
AMQP_IP=$eventProcessingToMOM_IP
LoadBalancerIP=$eventProcessingToLoadBalancer_IP

#PROFILES="CASSANDRA,MOM"
PROFILES=CASSANDRA,MOM
AMQP_PORT=9124
AMQP_QUEUE_NAME=DB_LOG

#set daas-service current dir
eval "sed -i 's#DAEMONDIR=.*#DAEMONDIR=$CURRENT_DIR#' $CURRENT_DIR/daas-service"
eval "sed -i 's#LoadBalancerIP=.*#LoadBalancerIP=$LoadBalancerIP#' $CURRENT_DIR/daas-service"
eval "sed -i 's#CassandraNode\.IP=.*#CassandraNode\.IP=$CASSANDRA_SEED_IP#' $CURRENT_DIR/config/daas.properties"
eval "sed -i 's#AMQP\.IP=.*#AMQP.IP=$AMQP_IP#' $CURRENT_DIR/config/daas.properties"
eval "sed -i 's#AMQP\.PORT=.*#AMQP.PORT=$AMQP_PORT#' $CURRENT_DIR/config/daas.properties"
eval "sed -i 's#AMQP\.QUEUE_NAME=.*#AMQP.QUEUE_NAME=$AMQP_QUEUE_NAME#' $CURRENT_DIR/config/daas.properties"
eval "sed -i 's#PROFILES=.*#PROFILES=\"$PROFILES\"#' $CURRENT_DIR/daas-service"

sudo -S cp ./daas-service /etc/init.d/daas-service
sudo -S chmod +x /etc/init.d/daas-service
sudo -S update-rc.d daas-service defaults

sudo service daas-service start

cd ./gangliaPlugIns
./setupPlugIns.sh
sudo -S service ganglia-monitor restart