#!/bin/bash
#Licensed under the EUPL V.1.1 
#http://joinup.ec.europa.eu/software/page/eupl/licence-eupl

CASSANDRA_BIN="/home/ubuntu/ElasticCassandra/apache-cassandra-1.1.6/bin"

if [ "$1" ] ;
 then
  SCALE_IN_COUNT=$1
 else
  SCALE_IN_COUNT=1
fi

CURRENT_VM_IP=$(ifconfig eth0 | grep -o 'inet addr:[0-9.]*' | grep -o [0-9.]*)

COUNTER=0;

while(($COUNTER<$SCALE_IN_COUNT)); do

IFS=$'\n';
string=$($CASSANDRA_BIN/nodetool -h localhost ring)
readIPs=false;
index=0
for line in $string;
  do  

IFS=' ';
read -ra words <<< "$line"

if [[ $line = *Address* ]]
then
    readIPs=true;
else
if $readIPs
then
   ip=${words[0]}
   if [ $(echo "$ip" | grep [.] ) ] && [ "$ip" != "$CURRENT_VM_IP" ] ; then
   ips[$index]=$ip
   #remove trailing %
   tmp=${words[7]}
   tmp=${tmp%?}
   #remove all digits after trailin . (transforms flaot to int)
   tmp=$(echo $tmp | grep -o "[0-9]*\.")
   #remove trailing .
   tmp=${tmp%?}
   load[$index]=$tmp
   let index++
   fi
fi
fi
IFS=$'\n';
done
smallestIndex=0;
index=0;
for a in ${load[*]};
do

if (($a<${load[$smallestIndex]}));
    then
     smallestIndex=$index;
   fi
   let index++;
done
echo "Decomissioning node IP:${ips[$smallestIndex]} Owning: ${load[$smallestIndex]} %" ;
$CASSANDRA_BIN/nodetool -h ${ips[$smallestIndex]} decommission
let COUNTER++
done
