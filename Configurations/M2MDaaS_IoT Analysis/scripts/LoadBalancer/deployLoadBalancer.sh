#!/bin/bash

echo "Installing Load balancing \n" >> /tmp/salsa.artifact.log

wget -q http://128.130.172.215/repository/files/HelloElasticity/HAProxySetup-1.0.tar.gz
tar -xzf ./HAProxySetup-1.0.tar.gz
cd ./HAProxySetup-1.0
CURRENT_DIR=$(pwd)

#please uncomment if needed as appropriate for your distro. otherwise HAProxy 1.4 will be installed which does not work with our ganglia plug

#for Ubuntu devel, lucid, precise, saucy

sudo -S apt-get install software-properties-common -y
sudo -S apt-add-repository ppa:vbernat/haproxy-1.5 -y
sudo -S apt-get update -y
sudo -S apt-get install haproxy curl ganglia-monitor gmetad python python-virtualenv python-pip -y
 
sudo -S pip install Flask

#set HAProxy config path in 
eval "sed -i 's#HAPROXY_CONFIG_FILE=.*#HAPROXY_CONFIG_FILE=\"$CURRENT_DIR/haproxyConfig\"#' $CURRENT_DIR/configPythonRESTfulAPI.py"
#eval "sed -i 's#HAPROXY_CONFIG_FILE=.*#HAPROXY_CONFIG_FILE=\"$CURRENT_DIR/haproxyConfig\"#' $CURRENT_DIR/load-balancer"

#copy scripts for registering and deregistering to HAPROXY in /bin
#sudo -S chmod +x $CURRENT_DIR/registerToHAProxy.sh
#sudo -S chmod +x $CURRENT_DIR/deregisterToHAProxy.sh
#sudo -S cp $CURRENT_DIR/registerToHAProxy.sh /bin/registerToHAProxy
#sudo -S cp $CURRENT_DIR/deregisterToHAProxy.sh /bin/deregisterToHAProxy

sudo -S killall haproxy

#haproxy -f $CURRENT_DIR/haproxyConfig
python ./configPythonRESTfulAPI.py  &
curl -X DELETE http://localhost:5001/service/1/1

cd ./gangliaPlugIns
./setupPlugIns.sh

sudo -S service ganglia-monitor restart
