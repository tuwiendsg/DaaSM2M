# -*- coding: utf-8 -*-
"""
Created on Wed Jul 16 10:24:47 2014

@author: Georgiana
"""

import sys, httplib, uuid, random,time
import time
import socket
import struct
import random

httplib.HTTPConnection.debuglevel = 0
SLEEP_SECONDS = 120
currentIps=[]

BaseURL = '128.130.172.215:8080'
ServiceID= 'M2MDaaS'


def executeRESTCall(restMethod, serviceBaseURL, resourceName,  content):
        connection =  httplib.HTTPConnection(serviceBaseURL)
        #read composition rules file

        headers={
                'Content-Type':'application/xml; charset=utf-8',
                'Accept':'application/xml, multipart/related'
        }

        connection.request(restMethod, '/'+resourceName, body=content,headers=headers,)
        result = connection.getresponse()

def _scaleIn():
     if len(currentIps) > 0:
          print "Executing scale in.... "
          pickIp = random.choice(currentIps)  
          print "Removing sensor ", pickIp
          executeRESTCall('POST', BaseURL,'salsa-engine/rest/services/'+ServiceID+'/vmnodes/'+pickIp+'/scalein',"")
          currentIps.remove(pickIp)

           
def _scaleOut():
     print "Executing scale out ..."
     newIp = executeRESTCall('POST', BaseURL,'salsa-engine/rest/services/'+ServiceID+'/nodes/sensor/scaleout',"")     
     #newIp=socket.inet_ntoa(struct.pack('>I', random.randint(1, 0xffffffff)))
     print "Added sensor ", newIp   
     currentIps.append(newIp)
def randomGen():
    while (True):
        rand = random.randint(1, 3)
        print rand
        if rand == 2:
            _scaleIn()
        if rand ==3:
            _scaleOut()
        time.sleep(SLEEP_SECONDS)
    
if __name__=='__main__':
    randomGen()