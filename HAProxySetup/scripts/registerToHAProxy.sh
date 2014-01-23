#!/bin/bash
HAPROXY_CONFIG_FILE=
if [ -z $2 ] 
 then 
     #if no IP supplied, try to use contextualization
     echo "No IP or Port received for comission"         
 else
     IP=$1
     PORT=$2
     #sed with  /d deletes matching row 
     eval "echo \" server $IP $IP:$PORT maxconn 2000\" >> $HAPROXY_CONFIG_FILE"
     sudo killall haproxy
     haproxy -f $HAPROXY_CONFIG_FILE -p /tmp/haproxy.pid -sf $(cat /tmp/haproxy.pid)
fi



