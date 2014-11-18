#!/bin/bash

echo "Installing cassandra head node \n" >> /tmp/salsa.artifact.log
 
wget http://128.130.172.215/salsa/upload/files/DaasService/ElasticCassandraSetup-1.0.tar.gz
tar -xzf ./ElasticCassandraSetup-1.0.tar.gz
cd ./ElasticCassandraSetup-1.0 

./setupCassandra.sh >> /tmp/salsa.artifact.log
./setupElasticCassandraController.sh >> /tmp/salsa.artifact.log
cd ./gangliaPlugIns
./setupPlugIns.sh >> /tmp/salsa.artifact.log

sudo -S service cassandra start
sudo -S service ganglia-monitor restart
