from xml.dom.minidom import parseString
import subprocess
import sys

def temp_handler(name):

   p = subprocess.Popen("echo 'show info'| socat unix-connect:/tmp/haproxy stdio | grep -o \"^ConnRate:[0-9 ]*\" | grep -o \"[0-9]*\"", shell=True,stdout=subprocess.PIPE, stderr=subprocess.PIPE)
   try:
        out, err = p.communicate()
   except:
        return -1
   return int(out);

def metric_init(params):
    global descriptors

    d1 = {'name': 'activeConnections',
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
