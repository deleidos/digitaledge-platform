#!/bin/bash
set -x

source /etc/rtwsrc

# Dependencies
if [[ ! -d /opt/graphite/ && "$RTWS_IS_TMS" == true ]]; then
	apt-get update
	apt-get -y install python-pip libcairo2-dev python-cairo python-cairo-dev python-libccc python-pycha
	easy_install django==1.4.10 zope.interface django-tagging twisted==10.0 txamqp
	easy_install zope.interface django-tagging twisted txamqp 

	# Graphite Components
	pip install whisper==0.9.12
	pip install carbon==0.9.12
	pip install graphite-web==0.9.12
	pip install django-cors-headers
	
	adduser --quiet --system --no-create-home --home /var/log/carbon --shell /usr/sbin/nologin carbon
	mkdir -p /var/log/carbon ; chown -R carbon:nogroup /var/log/carbon
	
	adduser --quiet --system --no-create-home --home /var/log/graphite --shell /bin/bash graphite
	mkdir -p /var/log/graphite ; chown -R graphite:nogroup /var/log/graphite

	cp /opt/graphite/conf/carbon.conf.example /opt/graphite/conf/carbon.conf
	cp /opt/graphite/conf/storage-schemas.conf.example /opt/graphite/conf/storage-schemas.conf
	cp /opt/graphite/conf/storage-aggregation.conf.example /opt/graphite/conf/storage-aggregation.conf
	cp /opt/graphite/conf/aggregation-rules.conf.example /opt/graphite/conf/aggregation-rules.conf
	cp /opt/graphite/webapp/graphite/local_settings.py.example /opt/graphite/webapp/graphite/local_settings.py

	RANDOM_SECRET_KEY="$(openssl rand -hex 24)"
	perl -i -pe "s:USER =:USER = carbon:g" /opt/graphite/conf/carbon.conf
	perl -i -pe "s:#SECRET_KEY = \'UNSAFE_DEFAULT\':SECRET_KEY = \'$RANDOM_SECRET_KEY\':g" /opt/graphite/webapp/graphite/local_settings.py
	perl -i -pe "s:#STORAGE_DIR = '/opt/graphite/storage':STORAGE_DIR = '/mnt/rdafs/graphite/storage':g" /opt/graphite/webapp/graphite/local_settings.py
	perl -i -pe "s:ENABLE_UDP_LISTENER = False:ENABLE_UDP_LISTENER = True:g" /opt/graphite/conf/carbon.conf
	perl -i -pe "s:'tagging',:'tagging','corsheaders',:g" /opt/graphite/webapp/graphite/app_settings.py
	perl -i -pe "s:'django.contrib.messages.middleware.MessageMiddleware',:'django.contrib.messages.middleware.MessageMiddleware','corsheaders.middleware.CorsMiddleware',:g" /opt/graphite/webapp/graphite/app_settings.py
	echo -e '\nCORS_ORIGIN_ALLOW_ALL = True' >> /opt/graphite/webapp/graphite/app_settings.py	
fi

if [ ! -d /mnt/rdafs/graphite/storage ]; then
	export GRAPHITE_STORAGE_DIR=/mnt/rdafs/graphite/storage
	mkdir -p /mnt/rdafs/graphite/storage /mnt/rdafs/graphite/storage/log/webapp/
	chown -R graphite:nogroup /mnt/rdafs/graphite
	chmod -R 775 /mnt/rdafs/graphite
	PYTHONPATH=/opt/graphite/webapp django-admin.py syncdb --settings=graphite.settings --noinput
	chown -R graphite:nogroup /mnt/rdafs/graphite
	chmod -R 775 /mnt/rdafs/graphite
fi

export GRAPHITE_STORAGE_DIR=/mnt/rdafs/graphite/storage
/opt/graphite/bin/carbon-cache.py start
su - graphite -c '/opt/graphite/bin/run-graphite-devel-server.py --port=61516 --noreload --libs /opt/graphite/webapp/ /opt/graphite > /mnt/rdafs/graphite/storage/log/webapp/console.log 2>&1 &'