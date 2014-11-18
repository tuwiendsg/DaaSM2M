#!/bin/bash

echo "Installing cassandra head node \n" >> /tmp/salsa.artifact.log

. /etc/environment

echo "Get the Seed ip: $dataNodeToDataController_IP" >> /mp/salsa.artifact.log

wget http://128.130.172.215/salsa/upload/files/DaasService/ElasticCassandraSetup-1.0.tar.gz
tar -xzf ./ElasticCassandraSetup-1.0.tar.gz
cd ./ElasticCassandraSetup-1.0

./setupCassandra.sh >> /tmp/salsa.artifact.log
./setupElasticCassandraNode.sh >> /tmp/salsa.artifact.log
cd ./gangliaPlugIns
./setupPlugIns.sh >> /tmp/salsa.artifact.log

#configure ganglia on port 8649

sudo -S service joinRing start
sudo -S service ganglia-monitor start
