#!/bin/bash
#Licensed under the EUPL V.1.1 
#http://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 
source ./Config

USER_HOME='\home\ubuntu'
CURRENT_DIR=$(pwd)
 
CASSANDRA_HOME=$CURRENT_DIR/apache-cassandra-1.2.6
CASSANDRA_BIN=$CASSANDRA_HOME/bin
CASSANDRA_CONFIG=$CASSANDRA_HOME/conf

if [ -z "$HOME" ] 
  then 
      echo "HOME not specified. Using "$USER_HOME;
      HOME = $USER_HOME;
      read -p "Continue (y/n)?"
      [ "$REPLY" == "y" ] || exit
fi

wget http://archive.apache.org/dist/cassandra/1.2.6/apache-cassandra-1.2.6-bin.tar.gz
tar -xzf ./apache-cassandra-1.2.6-bin.tar.gz

#echo "Deploying and Configuring Cassandra in directory \"$CURRENT_DIR\""

#read -p "Continue (y/n)?"
#[ "$REPLY" == "y" ] || exit

#Create directories needed by Cassandra (they are specified in /conf/cassandra.yaml)
sudo -S mkdir /var/lib/cassandra
sudo -S mkdir /var/lib/cassandra/data
sudo -S mkdir /var/lib/cassandra/commitlog
sudo -S mkdir /var/lib/cassandra/saved_caches

#Give access rights tot he Cassandra dirs. Sincerely, maybe less rights might work
sudo -S chmod 0777 /var/lib/cassandra
sudo -S chmod 0777 /var/lib/cassandra/data
sudo -S chmod 0777 /var/lib/cassandra/commitlog
sudo -S chmod 0777 /var/lib/cassandra/saved_caches

###########################################################################################
#Create Cassandra script in init.d such that Cassandra starts automatically after OS boot 
###########################################################################################

#sudo chmod -R +x $CASSANDRA_BIN
#sudo chmod -R +x $JAVA_HOME

#Configure init.d script
#Set user HOME directory
eval "sed -i 's#\<HOME=.*#HOME=$HOME#' $CURRENT_DIR/cassandra"

#we got some errors on cassandra stack size so we are changing it
eval "sed -i 's#\<Xss180k#Xss256k#' $CASSANDRA_CONFIG/cassandra-env.sh"
 

#Set user CASSANDRA HOME directory
eval "sed -i 's#\<CASSANDRA_BIN=.*#CASSANDRA_BIN=$CASSANDRA_BIN#' $CURRENT_DIR/cassandra"
eval "sed -i 's#\<CASSANDRA_HOME=.*#CASSANDRA_HOME=$CASSANDRA_HOME#' $CURRENT_DIR/cassandra"

#Set JAVA HOME directory
eval "sed -i 's#\<JAVA_HOME=.*#JAVA_HOME=$JAVA_HOME#' $CURRENT_DIR/cassandra"
eval "sed -i 's#\<JAVA=.*#JAVA=$JAVA_HOME/bin/java#' $CASSANDRA_BIN/cassandra"
eval "sed -i 's#\<JAVA=.*#JAVA=$JAVA_HOME/bin/java#' $CASSANDRA_BIN/cassandra-cli"
eval "sed -i 's#\<JAVA=.*#JAVA=$JAVA_HOME/bin/java#' $CASSANDRA_BIN/nodetool"



 
echo "Cassandra deployed successfully. Cassandra can be run with \"sudo service cassandra start\"."
echo "Please DO NOT run the Cassandra service if this machine needs to be snapshotted, as Cassandra creates load tokens connected to a particular deployment. Cassandra service also starts automatically on system boot".
echo "If this is not to be snapshotted please reboot OS (sudo reboot) to finish configuration".
