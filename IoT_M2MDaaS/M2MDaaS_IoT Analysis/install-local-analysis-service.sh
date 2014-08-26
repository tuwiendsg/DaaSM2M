#!/bin/bash

echo "Installing mqtt client which sends data to m2m \n" >> /tmp/salsa.artifact.log

USER_HOME='\home\ubuntu'
CURRENT_DIR=$(pwd)

#download and setup Oracle JDK (works better with Cassandra)
echo "Downloading oracle jre"
sudo wget -nv "http://128.130.172.215/salsa/upload/files/daas/jre-7-linux-x64.tar.gz"
echo "Untaring jre"
sudo tar -xzf ./jre-7-linux-x64.tar.gz

#download local service
wget -nv "http://128.130.172.215/salsa/upload/files/DaasService/IoT/LocalDataAnalysis.tar.gz"
sudo tar -xzf ./LocalDataAnalysis.tar.gz

CURRENT_DIR=$(pwd)
LOCAL_SERVICE_HOME=$CURRENT_DIR/LocalDataAnalysis
JAVA_HOME=$CURRENT_DIR/jre1.7.0

if [ -z "$HOME" ] 
  then 
      echo "HOME not specified. Using "$USER_HOME;
      HOME = $USER_HOME;
fi

sudo -S chmod 0777 ./LocalDataAnalysis/local-processing-service

#Set user HOME directory
eval "sed -i 's#\<SERVICE_DIR=.*#SERVICE_DIR=$CURRENT_DIR/LocalDataAnalysis#' $CURRENT_DIR/LocalDataAnalysis/local-processing-service"

#Set user CASSANDRA HOME directory
eval "sed -i 's#\<JAVA_HOME=.*#JAVA_HOME=$JAVA_HOME#' $CURRENT_DIR/LocalDataAnalysis/local-processing-service"

cd $CURRENT_DIR/LocalDataAnalysis
sudo -S $CURRENT_DIR/LocalDataAnalysis/local-processing-service start

cd $CURRENT_DIR/LocalDataAnalysis/gangliaPlugIns
./setupPlugIns.sh
sudo -S service ganglia-monitor restart