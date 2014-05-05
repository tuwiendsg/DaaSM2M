#!/usr/bin/python

import sys, httplib, uuid, random,time
from threading import Thread

httplib.HTTPConnection.debuglevel = 0

KeyspaceName = 'm2m'
tablename = 'sensor'

HAProxyIP = '10.99.0.39'
HAProxyport = '8080'
BaseURL = HAProxyIP+':'+HAProxyport

minOperations = 100
maxOperations = 100

generatedKeys = []
generatedTables = []


def executeRESTCall(restMethod, serviceBaseURL, resourceName,  content):
        connection =  httplib.HTTPConnection(serviceBaseURL)
        #read composition rules file

        headers={
                'Content-Type':'application/xml; charset=utf-8'#,
#                'Accept':'application/xml, multipart/related'
        }

        connection.request(restMethod, '/'+resourceName, body=content,headers=headers,)
        result = connection.getresponse()
        connection.close()
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
            #key = str(uuid.uuid1())
            table=tablename
            #table = 'table_'+str(tableIndex)
            #print table
            #system.exit(1)
            #generatedKeys.append(key)
            rowsToCreate = 1 #random.randrange(10,50)
            #10% are create table
            #for i in range(0, int(rowsToCreate*0.1)):
            #  tableIndex = str(random.randrange(1, 11111111))
            #  generatedTables.append('table_'+tableIndex)      
            #  executeRESTCall('PUT', BaseURL, 'DaaS/api/xml/table', '<Table name="table_'+tableIndex+'" primaryKeyName="key" primaryKeyType="uuid"><Keyspace name="' + KeyspaceName + '"/><Column name="sensorName" type="text"/><Column name="sensorValue" type="double"/></Table>')

            createRowStatement='<CreateRowsStatement><Table name="'+table+'"><Keyspace name="' + KeyspaceName + '"/></Table>'
            for i in range(0, int(rowsToCreate)):
              key = str(uuid.uuid1())
              generatedKeys.append(key)
              createRowStatement=createRowStatement+('<Row><Column name="key" value="%s"/><Column name="sensorName" value="SensorY"/><Column name="sensorValue" value="%s"/> </Row>' % (key,random.uniform(1, 20000)))

            #add row
            #executeRESTCall('PUT', BaseURL, 'DaaS/api/xml/table', '<Table name="'+table+'" primaryKeyName="key" primaryKeyType="uuid"><Keyspace name="' + KeyspaceName + '"/><Column name="sensorName" type="text"/><Column name="sensorValue" type="double"/></Table>')
            createRowStatement=createRowStatement + '</CreateRowsStatement>'
            executeRESTCall('PUT', BaseURL, 'DaaS/api/xml/table/row', createRowStatement)

def _issueReadRequest():
            rowsToDelete = 1 #random.randrange(10,50)
            keysToDelete = generatedKeys.pop(random.randint(0,len(generatedKeys)-1));
            for i in range(0, int (rowsToDelete)):
               if len(generatedKeys) > 0:
                   keysToDelete = keysToDelete + ',' + generatedKeys.pop(random.randint(0,len(generatedKeys)-1))

            table = tablename # generatedTables.pop(random.randint(0,len(generatedTables)-1))
            deleteRowQuerry = '<Query><Table name="'+table+'"><Keyspace name="' + KeyspaceName + '"/></Table><Condition>key in ('+keysToDelete+')</Condition></Query>'
            #executeRESTCall('POST', BaseURL, 'DaaS/api/xml/table/row', deleteRowQuerry)
            executeRESTCall('DELETE', BaseURL, 'DaaS/api/xml/table/row', deleteRowQuerry)

            #for i in range(0, int(rowsToDelete*0.1)):
            #  tableToDelete = generatedTables.pop(random.randint(0,len(generatedTables)-1))
            #  deleteTableQuerry = '<Table name="'+ tableToDelete +'"><Keyspace name="' + KeyspaceName + '"/></Table>'
            #  executeRESTCall('DELETE',BaseURL,'DaaS/api/xml/table',deleteTableQuerry)

if __name__=='__main__':
        #behave normally for 500 tries = 1000 seconds
     while True:
        for i in range(0,500):
            opCount = minOperations #random.randint(minOperations,maxOperations);
            _writeManyInParralel(opCount)
            #_readManyInParralel(opCount)
            minOperations = minOperations + 10
            maxOperations = maxOperations + 10
            #time.sleep(1)
            #print "invoke " + min
        #burst
        writesInBurst = maxOperations + 100
        readsInBurst = maxOperations + 100;
        for i in range(0,100):
            #minReads += 50
            #maxRead += 50
        #    print "burst"
            _writeManyInParralel(writesInBurst)
            _readManyInParralel(readsInBurst)
            #time.sleep(1)
        #cool off
        print "cool off"
        for i in range(0,500):
            minOperations = minOperations - 10
            maxOperations = maxOperations - 10
            opCount = minOperations # random.randint(minOperations,maxOperations)
            #_writeManyInParralel(opCount)
            _readManyInParralel(opCount)
            #time.sleep(1)
            time.sleep(10)

