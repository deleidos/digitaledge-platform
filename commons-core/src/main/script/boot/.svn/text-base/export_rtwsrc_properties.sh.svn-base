#!/bin/bash

# Simply creates a profile.d script on a node which exposes those RTWS_* variables
# Used in those parts of the system which don't/shouldn't be modified to source the /etc/rtwsrc file

source /etc/rtwsrc

NEW_PROFILE_D_SCRIPT=/etc/profile.d/rtws.sh

rm -f $NEW_PROFILE_D_SCRIPT
echo "#Generated on $(date)" > $NEW_PROFILE_D_SCRIPT
cat >>$NEW_PROFILE_D_SCRIPT<<EOF
export RTWS_DOMAIN=$RTWS_DOMAIN
EOF

chmod a+x rm -f $NEW_PROFILE_D_SCRIPT ; chown root:root rm -f $NEW_PROFILE_D_SCRIPT