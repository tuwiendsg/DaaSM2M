#!/bin/bash

if [ ! -d "/usr/lib/ganglia/python_modules/" ]; then
     sudo -S mkdir /usr/lib/ganglia/python_modules/
fi

if [ ! -d "/etc/ganglia/conf.d/" ]; then
     sudo -S mkdir /etc/ganglia/conf.d/
fi


sudo -S cp ./getBufferSize.py /usr/lib/ganglia/python_modules/getBufferSize.py
sudo -S cp ./localProcessingModule.pyconf /etc/ganglia/conf.d/localProcessingModule.pyconf
sudo -S cp ./modpython.conf /etc/ganglia/conf.d/modpython.conf
sudo -S service ganglia-monitor restart

