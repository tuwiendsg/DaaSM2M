import urllib, urllib2, sys, httplib

url = "/DaaS-0.1"
HOST_IP="128.130.172.216"

input_description="""Expected command line arguments: \n KeyspaceName TableName PrimaryKeyColumnName PrimaryKeyColumnType ColumnName ColumnType [ColumnName ColumnType ... ]
     Example input: TestKeyspace TestTable key int age int name text young boolean birthday timestamp \n
     
     Column type: 
      text types: ascii, text, varchar  
      float types: decimal, float  
      long types: counter, bigint  
      double types: double  
      date types: timestamp  
      int types: int  
      boolean types: boolean   
      collection types: list, set, map  
      uuid types: uuid, timeuuid  
      other types: varint, inet  
      \n More information about data types at http://www.datastax.com/docs/1.1/references/cql/cql_data_types#cql-data-types\n"""
 	 
     
if __name__=='__main__':
        
	connection =  httplib.HTTPConnection(HOST_IP+':8080')
        headers={
	        'Content-Type':'application/xml; charset=utf-8',
                'Accept':'application/xml, multipart/related'
	}
	argumentsLength = len(sys.argv);
	if ( argumentsLength > 0):
		firstArg=sys.argv[1]
                if(firstArg == "--help"):
                     print input_description;
                elif (argumentsLength > 4):
                     keyspaceName = sys.argv[1];
                     tableName = sys.argv[2]; 
                     primaryKeyName = sys.argv[3];        
                     primaryKeyType = sys.argv[4];  
                     body_content = '<Table name="'+tableName+'" primaryKeyName="'+primaryKeyName+'" primaryKeyType="'+primaryKeyType+'"><Keyspace name="'+keyspaceName+'"/>';
		     #go trough the rest arguments and extract columns  
                     for index in range(5,argumentsLength,2):
                         body_content = body_content  + '<Column name="'+sys.argv[index]+'" type="'+sys.argv[index+1]+'"/>'
                     body_content = body_content  + '</Table>';
                     print body_content;
                     sys.exit(-1)
                     connection.request('PUT', url+'/api/m2m/table', body=body_content,headers=headers,)
                     result = connection.getresponse()
                     #if nothing wrong happends, the API resturns nothing. Else, it returns exception. Anyway, print the result
                     resultMessage = result.read()
                     if ("Exception" in resultMessage) or ("Bad Request" in resultMessage):
                        print resultMessage;
                        sys.exit(-1);
                else:
                     print input_description;  
	else:
                print "To few arguments to create table.";
                print input_description;    
