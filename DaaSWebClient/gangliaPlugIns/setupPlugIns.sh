#!/bin/bash

if [ ! -d "/usr/lib/ganglia/python_modules/" ]; then
     sudo -S mkdir /usr/lib/ganglia/python_modules/
fi

if [ ! -d "/etc/ganglia/conf.d/" ]; then
     sudo -S mkdir /etc/ganglia/conf.d/
fi


sudo -S cp ./getResponseTime.py /usr/lib/ganglia/python_modules/getResponseTime.py
sudo -S cp ./getThroughput.py /usr/lib/ganglia/python_modules/getThroughput.py
sudo -S cp ./getPendingRequests.py /usr/lib/ganglia/python_modules/getPendingRequests.py
sudo -S cp ./daasPlugIns.pyconf /etc/ganglia/conf.d/daasPlugIns.pyconf
sudo -S cp ./modpython.conf /etc/ganglia/conf.d/modpython.conf
sudo -S service ganglia-monitor restart

