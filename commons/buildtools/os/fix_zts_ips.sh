#!/bin/bash
set -x

perl -i -pe"s/192.168.30.135/192.168.6.61/g" /etc/hosts
perl -i -pe"s/192.168.30.136/192.168.6.62/g" /etc/hosts
perl -i -pe"s/192.168.30.137/192.168.6.63/g" /etc/hosts
perl -i -pe"s/192.168.30.138/192.168.6.64/g" /etc/hosts
perl -i -pe"s/192.168.30.139/192.168.6.65/g" /etc/hosts
perl -i -pe"s/192.168.30.135/192.168.6.61/g" /etc/zts/conf.saic/service/common-service.properties
perl -i -pe"s/192.168.30.136/192.168.6.62/g" /etc/zts/conf.saic/service/common-service.properties
perl -i -pe"s/192.168.30.137/192.168.6.63/g" /etc/zts/conf.saic/service/common-service.properties
perl -i -pe"s/192.168.30.138/192.168.6.64/g" /etc/zts/conf.saic/service/common-service.properties
perl -i -pe"s/192.168.30.139/192.168.6.65/g" /etc/zts/conf.saic/service/common-service.properties
perl -i -pe "s/192.168.30.135/192.168.6.61/g" /etc/zookeeper/zoo.cfg
perl -i -pe "s/192.168.30.136/192.168.6.62/g" /etc/zookeeper/zoo.cfg
perl -i -pe "s/192.168.30.137/192.168.6.63/g" /etc/zookeeper/zoo.cfg
perl -i -pe "s/192.168.30.138/192.168.6.64/g" /etc/zookeeper/zoo.cfg
perl -i -pe "s/192.168.30.139/192.168.6.65/g" /etc/zookeeper/zoo.cfg