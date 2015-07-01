#!/bin/bash
set -x

source /etc/rtwsrc

# Quick installer for Diamond 
# https://github.com/BrightcoveOS/Diamond

if [[ ! -d /etc/diamond  && "$RTWS_IS_TMS" == true ]]; then
	apt-get -y install python-configobj python-psutil
	adduser --quiet --system --no-create-home --home /var/log/diamond --shell /usr/sbin/nologin diamond
	
	wget "https://github.com/BrightcoveOS/Diamond/archive/master.zip"
	unzip master
	cd Diamond-master
	make install
	cp ./bin/init.d/diamond /etc/init.d/.
	cp debian/diamond.default /etc/default/diamond
	ln -sf /usr/local/bin/diamond /usr/bin/diamond
	ln -sf /usr/local/bin/diamond-setup /usr/bin/diamond-setup
	mkdir -p /usr/share/diamond/collectors/ /var/log/diamond/
	chown diamond:nogroup /var/log/diamond/

	cp /etc/diamond/diamond.conf.example /etc/diamond/diamond.conf
	
	perl -i -pe "s:host = graphite:host = auth.$RTWS_DOMAIN\n\nproto = udp\n\n:g" /etc/diamond/diamond.conf
	
	RTWS_MOD_HOSTNAME="$(echo $RTWS_FQDN | tr -s '.' '_' )"
	if [ -z $RTWS_MOD_HOSTNAME ]; then
		RTWS_MOD_HOSTNAME="master_$(echo $RTWS_DOMAIN | tr -s '.' '_' )"
	fi
	perl -i -pe "s:# hostname = my_custom_hostname:hostname = $RTWS_MOD_HOSTNAME\n:g" /etc/diamond/diamond.conf
	
	perl -i -pe "s:# path_prefix = servers:path_prefix = sys\n:g" /etc/diamond/diamond.conf
	
	# Setup collectors
	for c in cpu diskspace diskusage loadavg vmstat memory
	do
		cp -R /usr/local/share/diamond/collectors/$c /usr/share/diamond/collectors/.
	done
	echo -e 'yes\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\' | diamond-setup --configfile /etc/diamond/diamond.conf
fi

service diamond start