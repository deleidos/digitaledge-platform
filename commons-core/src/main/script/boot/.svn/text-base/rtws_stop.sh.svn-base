#!/bin/bash


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
