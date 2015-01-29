import urllib, urllib2, sys, httplib

from xml.dom.minidom import parseString

HOST_IP = 'localhost:8080'
url = '/DaaS/api'
httplib.HTTPConnection.debuglevel = 0

def _getData():
            connection =  httplib.HTTPConnection(HOST_IP)
            headers={
	          'Content-Type':'application/xml; charset=utf-8',
                  'Accept':'application/xml, multipart/related'
	    }
 
            try:
               connection.request('GET', url+'/monitoring',headers=headers,)
               result = connection.getresponse()
               result_xml = result.read()
               #print result_xml
               dom = parseString(result_xml)
               xmlTag = dom.getElementsByTagName('pendingRequests')
               #print int(xmlTag[0].firstChild.nodeValue)
               #returns only avg reponse time
               return int(xmlTag[0].firstChild.nodeValue)
            except:
               return -1


def temp_handler(name):
    return _getData();

def metric_init(params):
    global descriptors

    d1 = {'name': 'pendingRequests',
        'call_back': temp_handler,
        'time_max': 5,
        'value_type': 'double',
 'units': '#',
        'slope': 'both',
        'format': '%d',
        'description': 'Number of pending requests',
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
 

