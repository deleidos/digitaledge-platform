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
# RPM Post-Install Script
# See http://www.rpm.org/max-rpm/ for info on RPM execution environment

dos2unix --quiet /usr/local/bin/run-cloud-env-monitor

echo "rtws.cloud.provider=EUC" > /usr/local/rtws/properties/rtws-common.properties
