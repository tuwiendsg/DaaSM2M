import urllib, urllib2, sys, httplib

url = "/DaaS-0.1"
HOST_IP="128.130.172.216"

input_description="""Expected command line arguments: KeyspaceName TableName ColumnName Value [ColumnName Value ... ] 
Example input: TestKeyspace TestTable key 3 age 25 name TestPerson \n
Basically each value to be inserted is specified as a <ColumnName Value> pair. 
""";


if __name__=='__main__':
	connection =  httplib.HTTPConnection(HOST_IP+':8080')
        argumentsLength = len(sys.argv);
	if ( argumentsLength > 0):
		firstArg=sys.argv[1]
                if(firstArg == "--help"):
                     print input_description;
                elif (argumentsLength > 2):
                     headers={
                         'Content-Type':'application/xml; charset=utf-8',
                         'Accept':'application/xml, multipart/related'
                     }
                     keyspaceName = sys.argv[1];
                     tableName = sys.argv[2]; 
                     body_content = '<CreateRowsStatement><Table name="'+tableName+'"><Keyspace name="'+keyspaceName+'"/></Table><Row>';
		     for index in range(3,argumentsLength,2):
                         body_content = body_content  + '<Column name="'+sys.argv[index]+'" value="'+sys.argv[index+1]+'"/>'
                     
		     body_content = body_content  +'</Row></CreateRowsStatement>'
		     print body_content;	
                     sys.exit(-1)
		     connection.request('PUT', url+'/api/m2m/table/row', body=body_content,headers=headers,)
		     result = connection.getresponse()
		     resultMessage = result.read()
		     if ("Exception" in resultMessage) or ("Bad Request" in resultMessage):
		       print resultMessage;
		       sys.exit(-1)
                else:
                     print input_description;  
	else:
		print input_description;    

 

