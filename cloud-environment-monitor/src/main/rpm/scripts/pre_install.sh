#!/bin/bash
#
# LEIDOS CONFIDENTIAL
# __________________
#
# (C)[2007]-[2014] Leidos
# Unpublished - All Rights Reserved.
#
# NOTICE:  All information contained herein is, and remains
# the exclusive property of Leidos and its suppliers, if any.
# The intellectual and technical concepts contained
# herein are proprietary to Leidos and its suppliers
# and may be covered by U.S. and Foreign Patents,
# patents in process, and are protected by trade secret or copyright law.
# Dissemination of this information or reproduction of this material
# is strictly forbidden unless prior written permission is obtained
# from Leidos.
#

#set -x

# RPM Pre-Install Script
# See http://www.rpm.org/max-rpm/ for info on RPM execution environment

# Note, you cannot rely on any files in the rpm to be present at this point.

for prefix in /var/log/de /usr/lib/de /etc/de/
do
   mkdir -p ${prefix}/cloud-env ; chmod 755 ${prefix}/cloud-env
done

mkdir -p /usr/local/rtws/properties