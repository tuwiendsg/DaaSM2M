#TODO: python cassandra latency plugins

sudo cp gmond.conf /etc/ganglia 
sudo cp -R ./conf.d /etc/ganglia/
sudo cp -R ./python_modules /usr/lib/

#TODO: in the config file, modify the namespace and the path towards nodetool