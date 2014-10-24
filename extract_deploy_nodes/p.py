import xmlrpclib
api_server = xmlrpclib.ServerProxy('https://www.planet-lab.eu/PLCAPI/')
auth = {}
auth['Username'] = "ruijosemangas@gmail.com" 
auth['AuthString'] = "z9U-oNV-gaT-X7b" 
auth['AuthMethod'] = "password"
node_list = [line.strip() for line in open("nodes.txt")]
api_server.AddSliceToNodes(auth, "istple_seprs4", node_list)
