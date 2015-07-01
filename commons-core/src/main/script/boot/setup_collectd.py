#!/usr/bin/env python

import re
import os

def load_rtwsrc():
    rtwsrc = open('/etc/rtwsrc')
    lines = rtwsrc.readlines()
    rtwsrc.close()
    for ln in lines:
        (key, _, value) = ln.partition("=")
        os.environ[key] = value

def configure():   
    fqdn = os.environ.get('RTWS_FQDN','UNKNOWN')
    domain = os.environ.get('RTWS_DOMAIN','UNKNOWN')
    is_tms = os.environ.get('RTWS_IS_TMS','UNKNOWN')
    
    if fqdn == 'UNKNOWN':
        fqdn = 'master.' + domain
    
    if is_tms == 'UNKNOWN':
        graphite_prefix = 'graphite'
    else:
        graphite_prefix = 'auth'      
        
    conf = open('/usr/local/rtws/commons-core/bin/boot/collectd.conf')
    lines = conf.readlines()
    conf.close()
    
    lines = [re.sub('RTWS_FQDN',fqdn.strip(),l) for l in lines]    
    lines = [re.sub('RTWS_GRAPHITE_PREFIX',graphite_prefix.strip(),l) for l in lines]
    lines = [re.sub('RTWS_DOMAIN',domain.strip(),l) for l in lines]
    
    print 'Writing /opt/collectd/etc/collectd.conf'
    out = open('/opt/collectd/etc/collectd.conf','w')
    for l in lines:
        out.write(l)

# Main
if __name__ == '__main__':
    print "loading /etc/rtwsrc..."
    load_rtwsrc()
    configure()