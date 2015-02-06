from xml.dom.minidom import parseString
import subprocess
import sys
import httplib, csv
import base64
import string
  
host = "localhost"
url = "/haproxy_stats;csv"
username = 'comot'
password = 'comot'

#means connection rate
target_stat="rate"
 
def readStats():
        # base64 encode the username and password
        auth = base64.encodestring('%s:%s' % (username, password)).replace('\n', '')

        webservice = httplib.HTTP(host)
        # write your headers
        webservice.putrequest("POST", url)
        webservice.putheader("Host", host)
        webservice.putheader("User-Agent", "Python http auth")
        webservice.putheader("Content-type", "text/html; charset=\"UTF-8\"")
        #webservice.putheader("Content-length", "%d" % len(message))
        # write the Authorization header like: 'Basic base64encode(username + ':' + password)
        webservice.putheader("Authorization", "Basic %s" % auth)

        webservice.endheaders()
        #webservice.send()
        # get the response
        statuscode, statusmessage, header = webservice.getreply()
        #print "Response: ", statuscode, statusmessage
        #print "Headers: ", header
        res = webservice.getfile().read()
        #print 'Content: ', res
        return res

def parseStats(stats):
       #print stats
       #for s in stats.splitlines():
       #  print s
       statreader = csv.reader(stats.splitlines(), delimiter=',', quotechar='|')
       columns = statreader.next()
       frontendStats=statreader.next()
       #print columns
       #print frontendStats 
       desiredColumn = -1
       for i in range(len(columns)):
         if(columns[i] == target_stat):
           desiredColumn = i
           break
       if (desiredColumn == -1):
         return -1
       else:
         return frontendStats[desiredColumn]

def temp_handler(name):
   return int(parseStats(readStats()));

def metric_init(params):
    global descriptors

    d1 = {'name': 'connectionRate',
        'call_back': temp_handler,
        'time_max': 5,
        'value_type': 'int',
        'units': 'no',
        'slope': 'both',
        'format': '%d',
        'description': 'Number of current HAProxy connections',
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
        print 'value for %s is %d' % (d['name'],  v)
 

