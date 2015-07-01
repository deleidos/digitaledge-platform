#!/bin/bash
#
# Expected AWS userdata parameters.
# RTWS_WEBAPP - The space separated list of webapps to install to the jetty/webapps directory
# RTWS_WEBAPP_PLUS - The space separated list of webapps to install to the jetty/webapps-plus directory
# RTWS_DOMAIN domain of the system
# also expected is the rtws-common.properties file to exist at /usr/local/rtws/properties
# a webapp should be the name of the webapp only - this will pull two files (if they exist) for each webapp
# the files are saic-rtws-webapps-<name>-webapp.tar.gz and saic-rtws-webapps-<name>-conf.tar.gz, which will be copied to 
# /usr/local/jetty-@webapp.searchapi.jetty.version@/webapps
# and 
# /usr/local/jetty-@webapp.searchapi.jetty.version@/webapps/<name>/WEB-INF/classes
# respectively

source /etc/rtwsrc

build_release=`cat $RTWS_MANIFEST | grep -m 1 /mnt/appfs/release | cut -d'/' -f5`

#typical install lines from manifest - we are trying to replicate that capability
#INSTALL|jetty|/mnt/appfs/release/$build_release/saic-rtws-webapps-ingestapi-webapp.tar.gz|/usr/local/jetty-@webapp.searchapi.jetty.version@/webapps
#INSTALL|jetty|/mnt/appfs/configuration/$RTWS_DOMAIN/saic-rtws-webapp-ingestapi-conf.tar.gz|/usr/local/jetty-@webapp.searchapi.jetty.version@/webapps/ingestapi/WEB-INF/classes
for webapp in $RTWS_WEBAPP
do
        echo "Installing $webapp into webapps"
        if [ "$RTWS_MOUNT_MODE" = "s3cmd" ]; then
        	mkdir -p /usr/local/jetty/tmp
			s3cmd -c /home/rtws/.s3cfg -f get s3://$RTWS_MOUNT_DEVICE/$build_release/release/saic-rtws-webapps-$webapp-webapp.tar.gz /usr/local/jetty/tmp/
			tar xf /usr/local/jetty/tmp/saic-rtws-webapps-$webapp-webapp.tar.gz -C /usr/local/jetty/webapps
		else
			#not using s3cmd
			tar xf /mnt/appfs/release/$build_release/saic-rtws-webapps-$webapp-webapp.tar.gz -C /usr/local/jetty/webapps
		fi
        	web_app_conf="/mnt/appfs/configuration/$RTWS_DOMAIN/saic-rtws-webapp-$webapp-conf.tar.gz"
        	web_console_conf="/mnt/appfs/configuration/$RTWS_DOMAIN/saic-rtws-webconsole-$webapp-conf.tar.gz"
        if [ -e $web_app_conf ] ; then
            tar xf $web_app_conf -C /usr/local/jetty/webapps/$webapp/WEB-INF/classes
        elif [ -e $web_console_conf ] ; then
            tar xf $web_console_conf -C /usr/local/jetty/webapps/$webapp/WEB-INF/classes
        else
            echo "Warning: could not find a configuration tar bundle for $webapp"
        fi
        #edit web.xml file located at /usr/local/jetty/webapps/$webapp/WEB-INF/web.xml
        #basically replace </servlet> with <load-on-startup>1</load-on-startup></servlet> 
        sed -i 's/<\/servlet>/<load-on-startup>1<\/load-on-startup><\/servlet>/g' /usr/local/jetty/webapps/$webapp/WEB-INF/web.xml
done

for webapp in $RTWS_WEBAPP_PLUS
do
        echo "Installing $webapp into webapps_plus"
        if [ "$RTWS_MOUNT_MODE" = "s3cmd" ]; then
        	mkdir -p /usr/local/jetty/tmp
			s3cmd -c /home/rtws/.s3cfg -f get s3://$RTWS_MOUNT_DEVICE/$build_release/release/saic-rtws-webapps-$webapp-webapp.tar.gz /usr/local/jetty/tmp/
			tar xf /usr/local/jetty/tmp/saic-rtws-webapps-$webapp-webapp.tar.gz -C /usr/local/jetty/webapps-plus
		else
			#not using s3cmd
			tar xf /mnt/appfs/release/$build_release/saic-rtws-webapps-$webapp-webapp.tar.gz -C /usr/local/jetty/webapps-plus
		fi
        web_app_conf="/mnt/appfs/configuration/$RTWS_DOMAIN/saic-rtws-webapp-$webapp-conf.tar.gz"
        web_console_conf="/mnt/appfs/configuration/$RTWS_DOMAIN/saic-rtws-webconsole-$webapp-conf.tar.gz"
        if [ -e $web_app_conf ] ; then
            tar xf $web_app_conf -C /usr/local/jetty/webapps-plus/$webapp/WEB-INF/classes
        elif [ -e $web_console_conf ] ; then
            tar xf $web_console_conf -C /usr/local/jetty/webapps-plus/$webapp/WEB-INF/classes
        else
            echo "Warning: could not find a configuration tar bundle for $webapp"
        fi
done

# TODO Remove if not going to be used
if [ -f /usr/local/jetty/webapps/tenantconsole/docs/dtu/dtu.jnlp ]; then
	perl -i -pe "s/RTWS_DOMAIN/$RTWS_DOMAIN/g" /usr/local/jetty/webapps/tenantconsole/docs/dtu/dtu.jnlp
fi
