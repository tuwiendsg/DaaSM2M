#!/bin/bash

CURRENT_DIR=$(pwd)
 
#put daa-service in services
sudo -S cp ./daas-service /etc/init.d/workload-service
sudo -S chmod +x /etc/init.d/workload-service
sudo -S update-rc.d workload-service defaults

