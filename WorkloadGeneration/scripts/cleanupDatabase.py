#!/usr/bin/python

import sys, httplib, uuid, random,time

httplib.HTTPConnection.debuglevel = 0
 
keyspaceName = 'm2m'
tablename = 'sensor'

HAProxyIP = 'localhost'
HAProxyport = '8080'
BaseURL = HAProxyIP+':'+HAProxyport

def executeRESTCall(restMethod, serviceBaseURL, resourceName, content, contentType):
	connection =  httplib.HTTPConnection(serviceBaseURL)
        #read composition rules file
       
        headers={
	        'Content-Type':contentType#,
                #'Accept':'application/xml, multipart/related'
	}
 
	connection.request(restMethod, '/'+resourceName, body=content,headers=headers,)
	result = connection.getresponse()
        print result.read()


def _reinitiateDB():
        #drop keyspace
        executeRESTCall('DELETE', BaseURL, 'DaaS/api/xml/keyspace','<Keyspace name="' + keyspaceName + '"/>','application/xml')
        #create keyspace    
        executeRESTCall('PUT', BaseURL, 'DaaS/api/xml/keyspace','<Keyspace name="' + keyspaceName + '"/>','application/xml') 
        #create table    
        executeRESTCall('PUT', BaseURL, 'DaaS/api/xml/table', '<Table name="'+tablename+'" primaryKeyName="key" primaryKeyType="int"><Keyspace name="' + keyspaceName + '"/><Column name="sensorName" type="text"/><Column name="sensorValue" type="double"/></Table>','application/xml') 
     
if __name__=='__main__':
    _reinitiateDB()
