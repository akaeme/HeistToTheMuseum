import paramiko
import json
import os
import argparse
import sys
import logging, coloredlogs

logging.basicConfig(level=logging.INFO)
logging.getLogger('paramiko').setLevel(logging.WARNING)
logger = logging.getLogger(__name__)
coloredlogs.install(logger=logger)

def upload(ssh, hosts):
    hosts_lst = hosts['hosts']
    for host in hosts_lst:
        ssh.connect(host['hostname'], username=hosts['user'], password=host['password'], timeout=3)
        try:
            # Clear and make new directory
            ssh.exec_command("rm -rf *;mkdir Dist")
            # Send Jar
            sftp = ssh.open_sftp()
            sftp.put(os.getcwd() + '/assignment2.jar', 'Dist/assignment2.jar')
            sftp.put(os.getcwd() + '/mapping.json', 'Dist/mapping.json')
            #sftp.put(os.getcwd() + '/json-simple-1.1.jar', 'lib/json-simple-1.1.jar')
            #sftp.put(os.getcwd() + '/org.json-20120521.jar', 'lib/org.json-20120521.jar')
            logging.info('Jar was successfully sent to the host ' + host['hostname'])
        except Exception:
            logging.warning('Error sending the jar to the host ' + host['hostname'])

def checkAvailability(ssh, hosts, user, machines_needed):
    toDelete = []
    for host in hosts:
        try:
            ssh.connect(host['hostname'], username=user, password=host['password'], timeout=3)
        except Exception:
            toDelete.append(host)
            logging.warning(host['hostname'] + ' is not responding, wont be used for this process.')
        else:
            logging.info('SSH connection with host ' + host['hostname'] + ' successfully accomplished')
            #ssh.exec_command('echo \"Are u There?!\"')
            #print(ssh.exec_command("ls")[1].readlines())
    hosts = [h for h in hosts if h not in toDelete]
    if len(hosts) == 0:
        logging.critical('There are no machines available.')
        exit(1)
    '''if machines_needed <= len(hosts):
        return hosts
    else:
        logging.critical('The number of available machines is lesser than the number of entities to run the Program.')
        exit(1)'''
    return hosts

def execute(ssh, hosts, jars, hostNames, hostPorts):
    hosts_lst = hosts['hosts']

    servers = [sv for sv in jars if sv['type']=='Server']
    clients = [sv for sv in jars if sv['type'] == 'Client']

    servers = sorted(servers, key=lambda sv: sv['order'])
    for sv in servers:
        try:
            host = [host for host in hosts_lst if hostNames[sv['class']]==host['hostname']]
        except Exception:
            logging.critical('Class mismatch.')
        else:
            if(len(host)>1):
                logging.critical('Critical error, duplicated hostnames in file.')
                exit()
            if len(host) == 0:
                logging.critical('Hostname provided does not exist or there is a problem with it.')
                exit()
            if sv['class'] == 'CentralizedServer':
                logging.info('# ' + sv['class'] + ' is running on host ' + host[0]['hostname'])
                ssh.connect(host[0]['hostname'], username=hosts['user'], password=host[0]['password'], timeout=3)
                command = 'cd Dist;' + sv['command']
                #print(command % (hostPorts['CentralizedServer']))
                ssh.exec_command(command % (hostPorts['CentralizedServer']))
            elif  'AssaultPartyServer' in sv['class']:
                ap_index = int(sv['class'].split('_')[1])
                logging.info('# ' + sv['class'] + ' is running on host ' + host[0]['hostname'])
                ssh.connect(host[0]['hostname'], username=hosts['user'], password=host[0]['password'], timeout=3)
                command = 'cd Dist;' + sv['command']
                #print(command % (hostNames['CentralizedServer'] + ' ' + hostPorts['CentralizedServer'] + ' ' + str(ap_index)))
                ssh.exec_command(command % (hostNames['CentralizedServer'] + ' ' + hostPorts['CentralizedServer'] + ' ' + str(ap_index)))
                ap_index += 1
            else:
                logging.info('# ' + sv['class'] + ' is running on host ' + host[0]['hostname'])
                ssh.connect(host[0]['hostname'], username=hosts['user'], password=host[0]['password'], timeout=3)
                command = 'cd Dist;' + sv['command']
                #print(command % (hostNames['CentralizedServer'] + ' ' + hostPorts['CentralizedServer']))
                ssh.exec_command(command % (hostNames['CentralizedServer'] + ' ' + hostPorts['CentralizedServer']))

    clients = sorted(clients, key=lambda cl: cl['order'])
    for cl in clients:
        host = [host for host in hosts_lst if hostNames[cl['class']] == host['hostname']]
        if (len(host) > 1):
            logging.critical('Critical error, duplicated hostnames in file.')
            exit()
        if len(host) == 0:
            logging.critical('Hostname provided does not exist or there is a problem with it.')
            exit()
        logging.info('# ' + cl['class'] + ' is running on host ' + host[0]['hostname'])
        ssh.connect(host[0]['hostname'], username=hosts['user'], password=host[0]['password'], timeout=3)
        command = 'cd Dist;' + cl['command']
        #print(command % (hostNames['CentralizedServer'] + ' ' + hostPorts['CentralizedServer']))
        stdin, stdout, stderr =ssh.exec_command(command % (hostNames['CentralizedServer'] + ' ' + hostPorts['CentralizedServer']))
        if cl==clients[-1]:
            while not stdout.channel.exit_status_ready():
                if stdout.channel.recv_exit_status() == 0:
                    print('\n' + u'\u2713' + ' The execution is finished. Getting the log file...')
                    getLogger(ssh, hosts, hostNames)

def kill_all(ssh, hosts):
    hosts_lst = hosts['hosts']

    for host in hosts_lst:
        ssh.connect(host['hostname'], username=hosts['user'], password=host['password'], timeout=3)
        ssh.exec_command('killall java')
        logging.info('- Killed all java processes at host ' + host['hostname'])

def clear(ssh, hosts):
    hosts_lst = hosts['hosts']

    for host in hosts_lst:
        ssh.connect(host['hostname'], username=hosts['user'], password=host['password'], timeout=3)
        ssh.exec_command('rm -rf *')
        logging.info('* Clear the created directories at host ' + host['hostname'])

def jsonParser(jsonObject):
    hostNames = {}
    hostPort = {}
    for obj in jsonObject:
        hostNames[list(obj.keys())[0]] = obj[list(obj.keys())[0]]['hostName']
        hostPort[list(obj.keys())[0]] = obj[list(obj.keys())[0]]['hostPort']
    return hostNames, hostPort

def command(ssh, hosts, hostNames):
    hosts_lst = hosts['hosts']
    for i in range(len(hostNames.keys())):
        print('# {} {:<27} {}'.format(i,list(hostNames.keys())[i],list(hostNames.values())[i]))
    machine = int(input('Choose a machine ({}-{}): '.format(0,len(hostNames.keys()))))
    host = [h for h in hosts_lst if h['hostname']==list(hostNames.values())[machine]][0]
    command = input('Command: ')
    ssh.connect(host['hostname'], username=hosts['user'], password=host['password'], timeout=3)
    output = ssh.exec_command(command)[1].readlines()
    for o in output:
        print(o.rstrip())

def getLogger(ssh, hosts, hostNames):
    hostName = hostNames['LoggerServer']
    host = [h for h in hosts['hosts'] if h['hostname']==hostName]
    if not os.path.exists('Logs'):
        os.makedirs('Logs')
    else:
        if os.path.isfile('Logs/log.txt'):
            os.remove('Logs/log.txt')
    try:
        ssh.connect(hostName, username=hosts['user'], password=host[0]['password'], timeout=3)
    except Exception:
        logging.warning(hostName + ' is not available at moment. Try again later.')
    else:
        sftp = ssh.open_sftp()
        sftp.get('Dist/log.txt', "Logs/log.txt")
        logging.info('Log successfully obtained. Check it at directory Logs')



if __name__ == '__main__':
    with open('hosts.json') as file:
        data_hosts = json.load(file)
    with open('jars.json') as file:
        data_jars = json.load(file)
    with open('mapping.json')as file:
        mapping = json.load(file)


    hostNames, hostPorts = jsonParser(mapping)
    ssh = paramiko.SSHClient()
    ssh.load_system_host_keys()
    ssh.set_missing_host_key_policy(paramiko.AutoAddPolicy())

    options = {'upload': upload,
               'execute': execute,
               'killall': kill_all,
               'clear': clear,
               'command': command,
               'logger': getLogger,
               }

    data_hosts['hosts'] = checkAvailability(ssh, data_hosts['hosts'], data_hosts['user'], len(data_jars))

    parser = argparse.ArgumentParser()
    parser.add_argument('-u', '--upload', action='store_true',help='Upload jar to all the machines')
    parser.add_argument('-e', '--execution', action='store_true', help='Execute all the java processes')
    parser.add_argument('-k', '--kill', action='store_true', help='Kill all java processes')
    parser.add_argument('-c', '--clear', action='store_true', help='Remove the created directories')
    #parser.add_argument("-v", "--verbose", action="store_true", help="Increase output verbosity")
    parser.add_argument('-C', '--Command', action='store_true', help='Execute a command at host')
    parser.add_argument('-l', '--logger', action='store_true', help='Get logger to local machine')
    args = vars(parser.parse_args())

    if not len(sys.argv) > 1:
        parser.print_help()
        parser.exit()

    if args['upload']:
        options['upload'](ssh, data_hosts)
    if args['execution']:
        options['execute'](ssh, data_hosts, data_jars, hostNames, hostPorts)
    if args['Command']:
        options['command'](ssh, data_hosts, hostNames)
    if args['logger']:
        options['logger'](ssh, data_hosts, hostNames)
    if args['kill']:
        options['killall'](ssh, data_hosts)
    if args['clear']:
        options['clear'](ssh, data_hosts)