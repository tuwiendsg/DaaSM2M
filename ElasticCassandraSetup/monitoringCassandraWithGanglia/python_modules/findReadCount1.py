import commands
import ConfigParser

def ConfigSectionMap(section):
	dict1 = {}
	Config = ConfigParser.ConfigParser()
	Config.read("/usr/lib/ganglia/python_modules/Config.ini")
	options = Config.options(section)
	for option in options:
		try:
			dict1[option] = Config.get(section, option)
			if dict1[option] == -1:
				DebugPrint("skip: %s" % option)
		except:
			print("exception on %s!" % option)
			dict1[option] = None
	return dict1
def temp_handler(name):  
	command=ConfigSectionMap("ConfigApache")['nodetool_path']+"  cfstats"
	res = commands.getoutput(command)
	_,usertableRes = res.split(ConfigSectionMap("ConfigNamespace")['namespace'],1)
	_,a = usertableRes.split("Read Count: ",1)
	res,_ = a.split("Read",1)
	try:
		return float(res);
	except:
		return 0
	pass

def metric_init(params):
    global descriptors
    d1 = {'name': 'read_count',
        'call_back': temp_handler,
        'time_max': 90,
        'value_type': 'float',
        'units': '',
        'slope': 'both',
        'format': '%f',
        'description': 'Read count',
        'groups': 'health'}

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
        print 'value for %s is %f' % (d['name'],  v)
