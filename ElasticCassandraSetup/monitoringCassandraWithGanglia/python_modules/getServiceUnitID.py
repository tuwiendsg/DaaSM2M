from xml.dom.minidom import parseString
import subprocess
import sys

def temp_handler(name):

   p = subprocess.Popen("echo CassandraNode", shell=True,stdout=subprocess.PIPE, stderr=subprocess.PIPE)
   try:
        out, err = p.communicate()
   except:
        return -1
   return out.rstrip();

def metric_init(params):
    global descriptors

    d1 = {'name': 'serviceUnitID',
        'call_back': temp_handler,
        'time_max': 5,
        'value_type': 'string',
        'units': 'ID',
        'slope': 'zero',
        'format': '%s',
        'description': 'IF of the Service Unit to which this VM belongs',
        'groups': 'serviceInfo'}

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

