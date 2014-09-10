from flask import request, Flask
import subprocess

HAPROXY_CONFIG_FILE= 

app = Flask("HAProxyConfig_Endpoint")

@app.route('/service/<ip>/<port>', methods = ['PUT'])
#@app.route('/service')
def put_server(ip, port):
    #print ip
    #print port
    p = subprocess.Popen("echo server " + str(ip) +  " " + str(ip) + ":" + str(port) + " maxconn 2000 | sudo tee -a " + HAPROXY_CONFIG_FILE, shell=True, stdout=subprocess.PIPE, stderr=subprocess.STDOUT)
    for line in p.stdout.readlines():
      print line,
    retval = p.wait()

    p = subprocess.Popen("sudo haproxy -f " + HAPROXY_CONFIG_FILE + " -p /tmp/haproxy.pid -sf $(cat /tmp/haproxy.pid)", shell=True, stdout=subprocess.PIPE, stderr=subprocess.STDOUT)
    for line in p.stdout.readlines():
      print line,
    retval = p.wait()
    
    #change access rights to socket such that ganglia plug-in can querry it 
    p = subprocess.Popen("sudo chmod 0777 /tmp/haproxy", shell=True, stdout=subprocess.PIPE, stderr=subprocess.STDOUT)
    for line in p.stdout.readlines():
      print line,
    retval = p.wait()
    return "Registered " + str(ip) +":" + str(port)

@app.route('/service/<ip>/<port>', methods = ['DELETE'])
#@app.route('/service')
def delete_server(ip, port):
    #print ip
    #print port
    p = subprocess.Popen('sudo sed -i "/server ' + str(ip) +  ' ' + str(ip) + ":" + str(port) + ' maxconn 2000/d" ' + HAPROXY_CONFIG_FILE, shell=True, stdout=subprocess.PIPE, stderr=subprocess.STDOUT)
    for line in p.stdout.readlines():
      print line,
    retval = p.wait()

    p = subprocess.Popen("sudo haproxy -f " + HAPROXY_CONFIG_FILE + " -p /tmp/haproxy.pid -sf $(cat /tmp/haproxy.pid)", shell=True, stdout=subprocess.PIPE, stderr=subprocess.STDOUT)
    for line in p.stdout.readlines():
      print line,
    retval = p.wait()

    p = subprocess.Popen("sudo chmod 0777 /tmp/haproxy", shell=True, stdout=subprocess.PIPE, stderr=subprocess.STDOUT)
    for line in p.stdout.readlines():
      print line,
    retval = p.wait()

    return "Removed " + str(ip) +":" + str(port)

if __name__ == '__main__':
    app.run('0.0.0.0', 5001, debug=True)



