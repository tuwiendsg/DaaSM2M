#!/bin/bash

CURRENT_DIR=$(pwd)
 
#set daas-service current dir
eval "sed -i 's#DAEMONDIR=.*#DAEMONDIR=$CURRENT_DIR#' $CURRENT_DIR/daas-service"

#put daa-service in services
sudo -S cp ./daas-service /etc/init.d/daas-service
sudo -S chmod +x /etc/init.d/daas-service
sudo -S update-rc.d daas-service defaults

sudo service daas-service start
