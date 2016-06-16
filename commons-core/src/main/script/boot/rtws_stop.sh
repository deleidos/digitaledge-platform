#!/bin/bash

################################################################
#This script is copyright 2012, Leidos, Inc.
#This script is currently developed by the Leidos employees who are part of 
#the Leidos DigitalEdge Platform Development Team <DigitalEdge@leidos.com> Developers 
#include Jim Cannaliato, Matthew W. Vahlberg, and others.
#
#License: GPLv2+
#
#    This program is free software; you can redistribute it and/or modify
#    it under the terms of the GNU General Public License as published by
#    the Free Software Foundation; either version 2 of the License, or
#    (at your option) any later version.
#
#    This program is distributed in the hope that it will be useful,
#    but WITHOUT ANY WARRANTY; without even the implied warranty of
#    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#    GNU General Public License for more details.
#
#    You should have received a copy of the GNU General Public License
#    along with this program; if not, write to the Free Software
#    Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301, USA.
#
#See /usr/share/common-licenses/GPL-2, or <http://www.gnu.org/copyleft/gpl.txt> for the 
#terms of the latest version of the GNU General Public License.
################################################################

#####
# Make sure all the needed parameters were passed.
##
source /etc/rtwsrc

#####
# Run the shutdown routines listed in the ".rtws.stop" file.
##

cat /etc/init.d/.rtws.stop | while read line; do

  echo "$line" | tr '|' ' '
  action=`echo $line | cut -d'|' -f1`
  
  # Moves the specified directory to a new location.
  if [ "$action" = "STOP" ]; then
  
    usr=`echo $line | cut -d'|' -f2`
    cmd=`echo $line | cut -d'|' -f3`
    dir=`echo $cmd | cut -d' ' -f1`
    dir=`dirname $dir`
    
    cd $dir
    su $usr -p -c "$cmd"
    
  fi
  
done
