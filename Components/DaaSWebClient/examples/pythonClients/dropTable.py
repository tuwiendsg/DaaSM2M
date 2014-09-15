import urllib, urllib2, sys, httplib

url = "/DaaS-0.1"
HOST_IP="128.130.172.216"

input_description="Expected command line arguments: KeyspaceName TableName";

if __name__=='__main__':
	connection =  httplib.HTTPConnection(HOST_IP+':8080')
	argumentsLength = len(sys.argv);
	if ( argumentsLength > 0):
		firstArg=sys.argv[1]
                if(firstArg == "--help"):
                     print input_description;
                elif  (argumentsLength > 2):
                     headers={
                        'Content-Type':'application/xml; charset=utf-8',
                        'Accept':'application/xml, multipart/related'
                     }
                     body_content = '<Table name="'+sys.argv[2]+'"><Keyspace name="'+sys.argv[1]+'"/></Table>'
                     #print body_content;
                     connection.request('DELETE', url+'/api/m2m/table', body=body_content,headers=headers,)
                     result = connection.getresponse()
                     resultMessage = result.read()
                     if ("Exception" in resultMessage) or ("Bad Request" in resultMessage):
                        print resultMessage;
                        sys.exit(-1);
                else:
                     print input_description;  
	else:
                print input_description;
