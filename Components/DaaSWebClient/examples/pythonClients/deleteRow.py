import urllib, urllib2, sys, httplib

url = "/DaaS-0.1"
HOST_IP="128.130.172.216"

input_description="""Expected command line arguments: KeyspaceName TableName PrimaryKeyCondition 
	Example: TestKeyspace TestTable "key IN (9640,4830)"
                 TestKeyspace TestTable key = 9640  \n
	Condition specified on primary key supporting = and IN . Example: key = 3, or key IN (1,100)), where key is PRIMARY KEY
""";
 
if __name__=='__main__':
        connection =  httplib.HTTPConnection(HOST_IP+':8080') 
        if (len(sys.argv) > 3):
                headers={
                   'Content-Type':'application/xml; charset=utf-8',
                   'Accept':'application/xml, multipart/related'
                }
                keySpaceName=sys.argv[1]
                tableName=sys.argv[2]
                body_content = '<Query><Table name="'+tableName+'"><Keyspace name="'+keySpaceName+'"/></Table>'
                condition = ""; 
                for index in range(3,len(sys.argv),1):
                         condition = condition+sys.argv[index];
                if (len(condition) > 0):
                         body_content = body_content  + '<Condition>'+condition+'</Condition>';
                body_content = body_content +  '</Query>';
                #print body_content
                connection.request('DELETE', url+'/api/m2m/table/row', body=body_content,headers=headers,)
                result = connection.getresponse()
                resultMessage = result.read()
                #print resultMessage;
                if ("Exception" in resultMessage) or ("Bad Request" in resultMessage):
                    print resultMessage;
                    sys.exit(-1)
        else:
                print input_description;
        


 

