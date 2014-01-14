#!/bin/bash

CURRENT_DIR=$(pwd)

sudo -S apt-get install haproxy

#set HAProxy config path in 
eval "sed -i 's#HAPROXY_CONFIG_FILE=.*#HAPROXY_CONFIG_FILE=$CURRENT_DIR/haproxyConfig#' $CURRENT_DIR/registerTOHAProxy.sh"
eval "sed -i 's#HAPROXY_CONFIG_FILE=.*#HAPROXY_CONFIG_FILE=$CURRENT_DIR/haproxyConfig#' $CURRENT_DIR/deregisterTOHAProxy.sh"

#copy scripts for registering and deregistering to HAPROXY in /bin
sudo -S chmod +x $CURRENT_DIR/registerTOHAProxy.sh
sudo -S chmod +x $CURRENT_DIR/deregisterTOHAProxy.sh
sudo -S cp $CURRENT_DIR/registerTOHAProxy.sh /bin/registerTOHAProxy
sudo -S cp $CURRENT_DIR/deregisterTOHAProxy.sh /bin/deregisterTOHAProxy
 
haproxy -f CURRENT_DIR/haproxyConfig

