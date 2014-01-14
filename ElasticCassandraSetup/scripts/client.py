import urllib, urllib2, sys, httplib


if __name__=='__main__':
	connection =  httplib.HTTPConnection('localhost:8080')
        body_content = '<Keyspace name="fff"/>'
        headers={
	        'Content-Type':'application/xml; charset=utf-8',
                'Accept':'application/xml, multipart/related'
	    }
        connection.request('POST', '/DaaS-0.1/api/m2m/keyspace', body=body_content,headers=headers)
	#connection.request('GET', '/DaaS/api/m2m', body=body_content,headers=headers)
	result = connection.getresponse()
        print result

 

