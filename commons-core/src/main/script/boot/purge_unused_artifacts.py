#!/usr/bin/env python

import os
import pprint
import subprocess
import shutil

def load_rtwsrc():
    rtwsrc = open('/etc/rtwsrc')
    lines = rtwsrc.readlines()
    rtwsrc.close()
    for ln in lines:
        (key, _, value) = ln.partition("=")
        os.environ[key] = value

def removeActiveMQ():
    print 'Removing ActiveMQ'
    os.unlink('/usr/local/apache-activemq')
    shutil.rmtree('/usr/local/apache-activemq-5.4.3')

def removeAllHadoopComponents():
    print 'Removing hadoop and dependent components'
    command = ['apt-get', '-y', 'purge', 'hadoop']
    proc = subprocess.Popen(command, stdout = subprocess.PIPE)
    for line in proc.stdout:
       print line
        
    proc.communicate()

def removePentaho():
    print 'Removing Pentaho'
    shutil.rmtree('/usr/local/biserver-ce')
    shutil.rmtree('/usr/local/administration-console')

def removeAccumulo():
    print 'Removing Accumulo'
    shutil.rmtree('/usr/lib/accumulo-1.4.4')
    
def removeCassandra():
    print 'Removing Cassandra and dependent components'
    command = ['apt-get', '-y', 'purge', 'cassandra']
    proc = subprocess.Popen(command, stdout = subprocess.PIPE)
    for line in proc.stdout:
       print line
        
    proc.communicate()

def run():
    print "loading /etc/rtwsrc..."
    load_rtwsrc()
    processGroup = os.environ.get('RTWS_PROCESS_GROUP','UNKNOWN')    
    print 'RTWS_PROCESS_GROUP=',processGroup
    manifest = os.environ.get('RTWS_MANIFEST','UNKNOWN')
    print 'RTWS_MANIFEST=',manifest

    # Detect master based on manifest
    if 'master.ini' in manifest.strip():
        removePentaho()
        removeActiveMQ()
        removeAccumulo()
        removeAllHadoopComponents()
    
    if processGroup.strip() == 'ingest.all' or processGroup.strip() == 'mongodb.replicated' or processGroup.strip() == 'datasink.mongodb' or processGroup.strip() == 'datasink.mongodb.replicated':
        removePentaho()
        removeActiveMQ()
        removeAccumulo()
        removeAllHadoopComponents()   
    else:
        print 'Happy Happy Happy....'


# Main
if __name__ == "__main__":
    run()