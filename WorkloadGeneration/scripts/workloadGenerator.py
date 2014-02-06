#!/usr/bin/python

import sys, httplib, uuid, random,time
from threading import Thread

httplib.HTTPConnection.debuglevel = 0

KeyspaceName = 'm2m'
tablename = 'sensor'

HAProxyIP = 'localhost'
HAProxyport = '8080'
BaseURL = HAProxyIP+':'+HAProxyport

minOperations = 10
maxOperations = 50

generatedKeys = []

def executeRESTCall(restMethod, serviceBaseURL, resourceName,  content):
        connection =  httplib.HTTPConnection(serviceBaseURL)
        #read composition rules file

        headers={
                'Content-Type':'application/xml; charset=utf-8'#,
#                'Accept':'application/xml, multipart/related'
        }

        connection.request(restMethod, '/'+resourceName, body=content,headers=headers,)
        result = connection.getresponse()
        #print result.status
        #print result.reason
        #print result.read()

def _writeManyInParralel(writes):
            generatedProcesses = []
            for i in range(0,writes):
              p = Thread(target=_issueWriteRequest)
              generatedProcesses.append(p)
              p.start()
            for p in generatedProcesses:
              p.join()

def _readManyInParralel(reads):
            generatedProcesses = []
            for i in range(0,reads):
              if len(generatedKeys) > 1:
                      p = Thread(target=_issueReadRequest)
                      generatedProcesses.append(p)
                      p.start()
            for p in generatedProcesses:
              p.join()

 
def _issueWriteRequest():
            key = str(uuid.uuid1())
            generatedKeys.append(key)
            #add row
            createRowStatement='<CreateRowsStatement><Table name="'+tablename+'"><Keyspace name="' + KeyspaceName + '"/></Table><Row><Column name="key" value="%s"/><Column name="sensorName" value="SensorY"/><Column name="sensorValue" value="%s"/> </Row></CreateRowsStatement>' % (key,random.uniform(1, 2000))
            executeRESTCall('PUT', BaseURL, 'DaaS/api/xml/table/row', createRowStatement)

def _issueReadRequest():
            if len(generatedKeys) > 0:
                   deleteRowQuerry = '<Query><Table name="'+tablename+'"><Keyspace name="' + KeyspaceName + '"/></Table><Condition>key=%s</Condition></Query>' % (generatedKeys.pop(random.randint(0,len(generatedKeys)-1)))
                   executeRESTCall('POST', BaseURL, 'DaaS/api/table/row', deleteRowQuerry)
                   executeRESTCall('DELETE', BaseURL, 'DaaS/api/xml/table/row', deleteRowQuerry)

if __name__=='__main__':
        #behave normally for 500 tries = 1000 seconds
     while True:
        for i in range(0,100):
            opCount = random.randint(minOperations,maxOperations);
            _writeManyInParralel(opCount)
            _readManyInParralel(opCount)
            miOperations = minOperations + 2
            maxOperations = maxOperations + 2
            time.sleep(1)
            #print "invoke " + minReads + " " + maxReads
        #burst
        writesInBurst = maxOperations + 100
        readsInBurst = maxOperations + 100;
        for i in range(0,50):
            #minReads += 50
            #maxRead += 50
            print "burst"
            _writeManyInParralel(writesInBurst)
            _readManyInParralel(readsInBurst)
            time.sleep(1)
        #cool off
        for i in range(0,100):
            minOperations = minOperations - 2
            maxOperations = maxOperations - 2
            opCount = random.randint(minOperations,maxOperations)
            _writeManyInParralel(opCount)
            _readManyInParralel(opCount)
            time.sleep(1)
        time.sleep(10)
