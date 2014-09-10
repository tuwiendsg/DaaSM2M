#!/bin/bash

CURRENT_DIR=$(pwd)
 
sudo -S add-apt-repository ppa:vbernat/haproxy-1.5 -y

#for Ubuntu  raring, quantal, precise, oneiric 
#sudo -S add-apt-repository ppa:nilya/haproxy-1.5

sudo -S apt-get update
sudo -S apt-get install ganglia-monitor gmond 
sudo -S apt-get install haproxy
sudo -S apt-get install python
sudo -S apt-get install python-virtualenv
sudo -S pip install Flask

#set HAProxy config path in 
eval "sed -i 's#HAPROXY_CONFIG_FILE=.*#HAPROXY_CONFIG_FILE=$CURRENT_DIR/haproxyConfig#' $CURRENT_DIR/configPythonRESTfulAPI.py"
eval "sed -i 's#HAPROXY_CONFIG_FILE=.*#HAPROXY_CONFIG_FILE=$CURRENT_DIR/haproxyConfig#' $CURRENT_DIR/configPythonRESTfulAPI.py"
 
haproxy -f $CURRENT_DIR/haproxyConfig

python ./configPythonRESTfulAPI.py  &

cd ./gangliaPlugIns
./setupPlugIns.sh
sudo -S service ganglia-monitor restart

