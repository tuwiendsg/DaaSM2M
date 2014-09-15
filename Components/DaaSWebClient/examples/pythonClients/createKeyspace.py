import urllib, urllib2, sys, httplib

url = ""
HOST_URL="localhost:8080"

if __name__=='__main__':
	connection =  httplib.HTTPConnection(HOST_URL)
	#take keyspace from command-line options
	keySpaceName='testKeyspace'
	if (len(sys.argv) > 0):
		keySpaceName=sys.argv[1]
		#create keyspace
		body_content = '<Keyspace name="'+keySpaceName+'"/>'
		#print body_content
		headers={
			'Content-Type':'application/xml; charset=utf-8',
		        'Accept':'application/xml, multipart/related'
		}
	 
		connection.request('PUT', url+'/api/json/keyspace', body=body_content,headers=headers,)
		result = connection.getresponse()
                print result.read()
	else:
                print "No keyspace name supplied";
  
 

 

