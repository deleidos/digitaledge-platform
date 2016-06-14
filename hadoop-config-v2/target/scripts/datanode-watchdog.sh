#!/bin/bash
#
# Note: Only output from this script should be Errors (ERROR: ...)
#
# See https://ccp.cloudera.com/display/CDH4DOC/Deploying+MapReduce+v1+%28MRv1%29+on+a+Cluster
#

if ! jps | grep -q DataNode ; then
 echo ERROR: datanode not up
fi