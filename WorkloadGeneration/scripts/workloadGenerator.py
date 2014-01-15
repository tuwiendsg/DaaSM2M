#!/usr/bin/python

import sys, httplib, uuid, random,time

httplib.HTTPConnection.debuglevel = 0
 
KeyspaceName = 'm2m'
tablename = 'sensor'

HAProxyIP = 'localhost'
HAProxyport = '8080'
BaseURL = 'http://'+HAProxyIP+':'+HAProxyport

def executeRESTCall(restMethod, serviceBaseURL, resourceName, contentType, content):
	connection =  httplib.HTTPConnection(serviceBaseURL)
        #read composition rules file
       
        headers={
	        'Content-Type':contentType#,
                #'Accept':'application/xml, multipart/related'
	}
 
	connection.request(restMethod, serviceBaseURL+'/'+resourceName, body=content,headers=headers,)
	result = connection.getresponse()
        print result.read()


def _reinitiateDB():
        #drop Keyspace
        executeRESTCall('DELETE', BaseURL, 'DaaS/api/xml/Keyspace','<Keyspace name="' + KeyspaceName + '"/>','application/xml')
        #create Keyspace    
        executeRESTCall('PUT', BaseURL, 'DaaS/api/xml/Keyspace','<Keyspace name="' + KeyspaceName + '"/>','application/xml') 
        #create table    
        executeRESTCall('PUT', BaseURL, 'DaaS/api/xml/table', '<Table name="'+tablename+'" primaryKeyName="key" primaryKeyType="int"><Keyspace name="' + KeyspaceName + '"/><Column name="sensorName" type="text"/><Column name="sensorValue" type="double"/></Table>','application/xml') 
        

def _issueWriteRequest():
            key = str(uuid.uuid1())
            generatedKeys.append(key)
            #add row
            createRowStatement='<CreateRowsStatement><Table name="'+tablename+'"><Keyspace name="' + KeyspaceName + '"/></Table><Row><Column name="key" value="%s"/><Column name="sensorName" value="SensorY"/><Column name="sensorValue" value="%s"/> </Row></CreateRowsStatement>' % (key,str(uuid.uuid1())
            executeRESTCall('PUT', BaseURL, 'DaaS/api/xml/table/row', createRowStatement,'application/xml') 

def _issueReadRequest():
            if len(generatedKeys) > 0:
                   deleteRowQuerry = '<Query><Table name="'+tablename+'"><Keyspace name="' + KeyspaceName + '"/></Table><Condition>key=%s</Condition></Query>	'
                   executeRESTCall('DELETE', BaseURL, 'DaaS/api/xml/table/row', deleteRowQuerry,'application/xml') 

if __name__=='__main__':
        #behave normally for 500 tries = 1000 seconds
     while True:
        for i in range(0,100):
            opCount = random.randint(minReads,maxReads);
            _writeManyInParralel(opCount)
            _readManyInParralel(opCount)
            minReads = minReads + 2
            maxReads = maxReads + 2
            time.sleep(1)
            #print "invoke " + minReads + " " + maxReads
        #burst
        writesInBurst = maxReads + 100
        readsInBurst = maxReads + 100;
        for i in range(0,50):
            #minReads += 50
            #maxRead += 50
            print "burst"
            _writeManyInParralel(writesInBurst)
            _readManyInParralel(readsInBurst)
            time.sleep(1)
        #cool off
        for i in range(0,100):
            minReads = minReads - 2
            maxReads = maxReads - 2
            opCount = random.randint(minReads,maxReads)
            _writeManyInParralel(opCount)
            _readManyInParralel(opCount)
            time.sleep(1)
        time.sleep(10) 
        #end
        #print "DONE"
