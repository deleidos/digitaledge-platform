#!/bin/bash
set -x

if [ -f ../ivy/ivysettings.properties ]; then
	
	perl -i -pe's/nexus.repo.host=ec2-23-21-234-240.compute-1.amazonaws.com/nexus.repo.host=192.168.30.166/g' ../ivy/ivysettings.properties
	
else
	echo "WARNING: ivysettings.properties not found"
fi