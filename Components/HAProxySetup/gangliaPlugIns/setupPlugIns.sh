#!/bin/bash
sudo -S apt-get install socat

if [ ! -d "/usr/lib/ganglia/python_modules/" ]; then
     sudo -S mkdir /usr/lib/ganglia/python_modules/
fi

if [ ! -d "/etc/ganglia/conf.d/" ]; then
     sudo -S mkdir /etc/ganglia/conf.d/
fi


sudo -S cp ./getConnectionsNr.py /usr/lib/ganglia/python_modules/getConnectionsNr.py
sudo -S cp ./haproxyModule.pyconf /etc/ganglia/conf.d/haproxyModule.pyconf
sudo -S cp ./modpython.conf /etc/ganglia/conf.d/modpython.conf
sudo -S service ganglia-monitor restart

