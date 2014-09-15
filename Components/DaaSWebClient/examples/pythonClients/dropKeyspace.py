import urllib, urllib2, sys, httplib

url = "/DaaS-0.1"
HOST_IP="128.130.172.216" 

if __name__=='__main__':
        connection =  httplib.HTTPConnection(HOST_IP+':8080')
        #take keyspace from command-line options        
        keySpaceName='testKeyspace'
        if (len(sys.argv) > 0):
                keySpaceName=sys.argv[1]
                #drop keyspace
                body_content = '<Keyspace name="'+keySpaceName+'"/>'
                #print body_content
                headers={
                    'Content-Type':'application/xml; charset=utf-8',
                    'Accept':'application/xml, multipart/related'
                }
                
                connection.request('DELETE', url+'/api/m2m/keyspace', body=body_content,headers=headers,)
                result = connection.getresponse()
                resultMessage = result.read()
                if ("Exception" in resultMessage) or ("Bad Request" in resultMessage):
                        print resultMessage;
                        sys.exit(-1);
        else:
                print "No keyspace name supplied";
 

 

