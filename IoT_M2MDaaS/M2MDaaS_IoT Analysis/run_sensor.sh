#!/bin/bash
cd "/home/ubuntu"
#sudo wget http://128.130.172.215/salsa/upload/files/DaasService/IoT/solo-install.sh
#sudo chmod +x ./solo-install.sh
#sudo ./solo-install.sh >> /tmp/salsa.artifact.log
sudo wget http://128.130.172.215/salsa/upload/files/DaasService/IoT/chef-sensor.tar.gz
sudo tar -xzf ./chef-sensor.tar.gz
sudo echo "finished untaring sensor " >> /tmp/salsa.artifact.log
sudo chef-solo -c ./chef-sensor/solo.rb >> /tmp/salsa.artifact.log
#sudo java -jar "/home/ubuntu/chef_jars/sdsensor-0.0.1-SNAPSHOT-jar-with-dependencies.jar" > "/home/ubuntu/chef_jars/sdsensor.log" 2>&1 &