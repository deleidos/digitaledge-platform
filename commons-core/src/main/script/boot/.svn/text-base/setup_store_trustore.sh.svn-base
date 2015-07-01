#!/bin/bash
source /etc/rtwsrc

# Copy ssl-truststore to S3 for gw-reg-gplv2 ami usage
s3cmd -c /home/rtws/.s3cfg put -v -r /usr/local/rtws/boot-apps/.key/ssl-truststore s3://$RTWS_MOUNT_DEVICE/$RTWS_SW_VERSION/release/