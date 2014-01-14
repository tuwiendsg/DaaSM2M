import urllib, urllib2, sys, httplib
import xml.etree.ElementTree as ET

url = "/DaaS-0.1"
HOST_IP="128.130.172.216"

input_description=""" Expected input: KeyspaceName TableName ConditionStatement  
                      Example: TestKeyspace TestTable 123 key=1  \n
                      Condition Format is regular SQL format. Example: key = 1 AND sensorvalue = 123.5 , where key is PRIMARY KEY, and  sensorvalue is a table column. 
		      Only = and IN relation are supported on the PRIMARY KEY. (key = 3, or KEY IN (1,100)). "IN" is not supported for indexes, only for PRIMARY KEY.
                      UInfortunately IN works with sets not ranges. This means IN(92,97) represents where key = 92 || 97, not between 92,97
                      There should be at least one "=" (equals) in the WHERE clause on PRIMARY KE or other indexed column
                      Querry on indexed columns with IN clause for the PRIMARY KEY are not supported 	                  
                      Index has to be added to a Column to be able to say Column = 123.5, if Column is not PRIMARY KEY. Otherwise comparisons not possible""";
if __name__=='__main__':
	connection =  httplib.HTTPConnection(HOST_IP+':8080')
        argumentsLength = len(sys.argv);
	if ( argumentsLength > 0):
		firstArg=sys.argv[1]
                if(firstArg == "--help"):
                     print input_description;
                elif (argumentsLength > 3):
                     headers={
                         'Content-Type':'application/xml; charset=utf-8',
                         'Accept':'application/xml, multipart/related'
                     }
                     keyspaceName = sys.argv[1];
                     tableName = sys.argv[2]; 
                     maxResultCount = sys.argv[3];       
                     body_content = '<Query><Table name="'+tableName+'"><Keyspace name="'+keyspaceName+'"/></Table><MaxResultCount>'+maxResultCount+'</MaxResultCount>';
                     condition ='';
                     #all the rest of the arguments belong to the Condition
                     for index in range(4,argumentsLength,1):
                        condition = condition + sys.argv[index];     
		     if(len(condition)>0):
                         body_content = body_content + '<Condition>'+condition+'</Condition>'
                     body_content = body_content + '</Query>'
                     #print body_content;
                     connection.request('POST', url+'/api/m2m/table/row', body=body_content,headers=headers,)
                     result = connection.getresponse()
                     xmlResponse = result.read() 
                     if ("Exception" in xmlResponse) or ("Bad Request" in xmlResponse):
                        print xmlResponse;
                        sys.exit(-1);
                     #if we found results, process the returned XML and return the values row by row
		     if(len(xmlResponse) > 0):
                        xmlRoot = ET.fromstring(xmlResponse)  
                        for row in xmlRoot.iter('Row'):
                           line = "";
                           for column in row.iter('Column'):
                              line = line + column.get('value') + " ";
                           print line; 
                else:
                     print input_description;  
	else:
		print input_description;
        

 

