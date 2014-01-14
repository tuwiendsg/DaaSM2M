#!/bin/bash
HAPROXY_CONFIG_FILE=
if [ "$#" -ne 2] 
 then 
     #if no IP supplied, try to use contextualization
     echo "No IP received for decomission"         
 else
     IP=$1
     PORT=$2
     #sed with  /d deletes matching row 
     eval "sed -i '/server $IP $IP:$PORT maxconn 2000/d' $HAPROXY_CONFIG_FILE"
     sudo killall haproxy
     haproxy -f $HAPROXY_CONFIG_FILE
fi

