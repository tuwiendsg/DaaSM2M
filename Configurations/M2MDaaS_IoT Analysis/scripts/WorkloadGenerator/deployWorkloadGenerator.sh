#!/bin/bash
echo "Installing Workload Generator \n" >> /tmp/salsa.artifact.log

. /etc/environment
LoadBalancerIP=$loadbalancer2workload_IP
LoadBalancerPort=8080

sudo -S apt-get update 
sudo -S apt-get install ganglia-monitor  -y 
sudo -S apt-get install gmond  -y 
sudo -S apt-get install haproxy -y 
sudo -S apt-get install python -y 
sudo -S apt-get install python-virtualenv -y 
sudo -S pip install Flask 

echo "Get the Load balancing ip: $LoadBalancerIP \n" 

wget http://128.130.172.215/salsa/upload/files/DaasService/WorkloadGeneration-1.0.tar.gz
tar -xzf ./WorkloadGeneration-1.0.tar.gz
cd ./WorkloadGeneration-1.0

CURRENT_DIR=$(pwd)

eval "sed -i 's#HAProxyIP=.*#HAProxyIP=\"$LoadBalancerIP\"#' $CURRENT_DIR/fixedLoad.py" 
eval "sed -i 's#HAProxyIP=.*#HAProxyIP=\"$LoadBalancerIP\"#' $CURRENT_DIR/cleanupDatabase.py" 
eval "sed -i 's#WORKLOAD_DIR=.*#WORKLOAD_DIR=\"$CURRENT_DIR\"#' $CURRENT_DIR/configPythonRESTfulAPI.py" 
eval "sed -i 's#DAEMONDIR=.*#DAEMONDIR=\"$CURRENT_DIR\"#' $CURRENT_DIR/workload-service" 

sudo -S cp ./workload-service /etc/init.d/workload-service
sudo -S chmod +x /etc/init.d/workload-service
sudo -S update-rc.d workload-service defaults
sudo -S service workload-service start

python ./cleanupDatabase.py
