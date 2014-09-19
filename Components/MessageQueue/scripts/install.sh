#!/bin/bash

#!/bin/bash

AMQP_PORT=9124


CURRENT_DIR=$(pwd)

#wget -nv http://128.130.172.215/salsa/upload/files/queue/queue-1.0.tar.gz
#tar -xzf ./queue-1.0.tar.gz
#cd ./queue-1.0
 
eval "sed -i 's#Queue\.PORT=.*#Queue.PORT=$AMQP_PORT#' ../config/queue.properties"

#put daa-service in services
sudo -S cp ./queue-service /etc/init.d/queue-service
sudo -S chmod +x /etc/init.d/queue-service
sudo -S update-rc.d queue-service defaults

sudo service queue-service start

# we do not have amqp plug-ins yet
#cd ./gangliaPlugIns
#./setupPlugIns.sh
#sudo -S service ganglia-monitor restart
