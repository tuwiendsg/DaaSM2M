import urllib, urllib2, sys, httplib

from xml.dom.minidom import parseString

HOST_IP = 'localhost:8080'
url = '/LocalProcessingService/restWS'
httplib.HTTPConnection.debuglevel = 0

def _getData():
            connection =  httplib.HTTPConnection(HOST_IP)
            headers={
	          'Content-Type':'application/xml; charset=utf-8',
                  'Accept':'application/xml, multipart/related'
	    }
 
            try:
               connection.request('GET', url+'/bufferSize',headers=headers,)
               result = connection.getresponse()
               buffer_size = result.read()
               #print result_xml

               
               #print int(xmlTag[0].firstChild.nodeValue)
               #returns only avg reponse time
               return int(buffer_size)
            except:
               return -1


def temp_handler(name):
    return _getData();

def metric_init(params):
    global descriptors

    d1 = {'name': 'bufferSize',
        'call_back': temp_handler,
        'time_max': 5,
        'value_type': 'int',
		'units': 'number',
        'slope': 'both',
        'format': '%d',
        'description': 'Current buffer size',
        'groups': 'performance'}

    descriptors = [d1]

    return descriptors

def metric_cleanup():
    '''Clean up the metric module.'''
    pass

#This code is for debugging and unit testing
if __name__ == '__main__':
    metric_init({})
    for d in descriptors:
        v = d['call_back'](d['name'])
        print 'value for %s is %s' % (d['name'],  v)
 

