#!/bin/bash

CURRENT_DIR=$(pwd)

#please uncomment if needed as appropriate for your distro. otherwise HAProxy 1.4 will be installed which does not work with our ganglia plug-in

#for Ubuntu devel, lucid, precise, saucy
#sudo -S add-apt-repository ppa:vbernat/haproxy-1.5

#for Ubuntu  raring, quantal, precise, oneiric 
#sudo -S add-apt-repository ppa:nilya/haproxy-1.5

#sudo -S apt-get update

sudo -S apt-get install haproxy

#set HAProxy config path in 
eval "sed -i 's#HAPROXY_CONFIG_FILE=.*#HAPROXY_CONFIG_FILE=$CURRENT_DIR/haproxyConfig#' $CURRENT_DIR/registerToHAProxy.sh"
eval "sed -i 's#HAPROXY_CONFIG_FILE=.*#HAPROXY_CONFIG_FILE=$CURRENT_DIR/haproxyConfig#' $CURRENT_DIR/deregisterToHAProxy.sh"

#copy scripts for registering and deregistering to HAPROXY in /bin
sudo -S chmod +x $CURRENT_DIR/registerToHAProxy.sh
sudo -S chmod +x $CURRENT_DIR/deregisterToHAProxy.sh
sudo -S cp $CURRENT_DIR/registerToHAProxy.sh /bin/registerToHAProxy
sudo -S cp $CURRENT_DIR/deregisterToHAProxy.sh /bin/deregisterToHAProxy

haproxy -f $CURRENT_DIR/haproxyConfig

