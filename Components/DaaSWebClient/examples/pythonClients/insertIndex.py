import urllib, urllib2, sys, httplib

url = "/DaaS-0.1"
HOST_IP="128.130.172.216"

input_description="""Expected command line arguments: KeyspaceName TableName ColumnName [ColumnName ...]  (can contain multiple column names)""";

if __name__=='__main__':
	connection =  httplib.HTTPConnection(HOST_IP+':8080')
        if (len(sys.argv) > 3):
                headers={
                   'Content-Type':'application/xml; charset=utf-8',
                   'Accept':'application/xml, multipart/related'
                }
                keySpaceName=sys.argv[1]
                tableName=sys.argv[2]
                body_content = '<Table name="'+tableName+'"><Keyspace name="'+keySpaceName+'"/>'
                for index in range(3,len(sys.argv),1):
                         body_content = body_content  + '<Column name="'+sys.argv[index]+'"/>'
                     
                body_content = body_content  + '</Table>'
                #print body_content

                connection.request('PUT', url+'/api/m2m/table/index', body=body_content,headers=headers,)
                result = connection.getresponse()
                resultMessage = result.read()
                if ("Exception" in resultMessage) or ("Bad Request" in resultMessage):
                    print resultMessage;
                    sys.exit(-1)
        else:
                print input_description;
  

 

