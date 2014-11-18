#!/bin/bash


echo "Installing Event Processing \n" >> /tmp/salsa.artifact.log

. /etc/environment
 
wget http://128.130.172.215/salsa/upload/files/DaasService/DaaSQueue-1.0.tar.gz
tar -xzf ./DaaSQueue-1.0.tar.gz

cd ./DaaSQueue-1.0

CURRENT_DIR=$(pwd)

IP=`ifconfig eth0 | grep -o 'inet addr:[0-9.]*' | grep -o [0-9.]*`

eval "sed -i 's#DAEMONDIR=.*#DAEMONDIR=$CURRENT_DIR#' $CURRENT_DIR/queue-service"
eval "sed -i 's#Queue\.IP=.*#Queue\.IP=$IP#' $CURRENT_DIR/config/queue.properties"
sudo -S cp ./queue-service /etc/init.d/queue-service
sudo -S chmod +x /etc/init.d/queue-service
sudo -S update-rc.d queue-service defaults

sudo service queue-service start