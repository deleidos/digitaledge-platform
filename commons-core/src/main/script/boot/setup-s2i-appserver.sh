#!/bin/bash
#
#                                  Apache License
#                            Version 2.0, January 2004
#                         http://www.apache.org/licenses/
#
#    TERMS AND CONDITIONS FOR USE, REPRODUCTION, AND DISTRIBUTION
#
#    1. Definitions.
#
#       "License" shall mean the terms and conditions for use, reproduction,
#       and distribution as defined by Sections 1 through 9 of this document.
#
#       "Licensor" shall mean the copyright owner or entity authorized by
#       the copyright owner that is granting the License.
#
#       "Legal Entity" shall mean the union of the acting entity and all
#       other entities that control, are controlled by, or are under common
#       control with that entity. For the purposes of this definition,
#       "control" means (i) the power, direct or indirect, to cause the
#       direction or management of such entity, whether by contract or
#       otherwise, or (ii) ownership of fifty percent (50%) or more of the
#       outstanding shares, or (iii) beneficial ownership of such entity.
#
#       "You" (or "Your") shall mean an individual or Legal Entity
#       exercising permissions granted by this License.
#
#       "Source" form shall mean the preferred form for making modifications,
#       including but not limited to software source code, documentation
#       source, and configuration files.
#
#       "Object" form shall mean any form resulting from mechanical
#       transformation or translation of a Source form, including but
#       not limited to compiled object code, generated documentation,
#       and conversions to other media types.
#
#       "Work" shall mean the work of authorship, whether in Source or
#       Object form, made available under the License, as indicated by a
#       copyright notice that is included in or attached to the work
#       (an example is provided in the Appendix below).
#
#       "Derivative Works" shall mean any work, whether in Source or Object
#       form, that is based on (or derived from) the Work and for which the
#       editorial revisions, annotations, elaborations, or other modifications
#       represent, as a whole, an original work of authorship. For the purposes
#       of this License, Derivative Works shall not include works that remain
#       separable from, or merely link (or bind by name) to the interfaces of,
#       the Work and Derivative Works thereof.
#
#       "Contribution" shall mean any work of authorship, including
#       the original version of the Work and any modifications or additions
#       to that Work or Derivative Works thereof, that is intentionally
#       submitted to Licensor for inclusion in the Work by the copyright owner
#       or by an individual or Legal Entity authorized to submit on behalf of
#       the copyright owner. For the purposes of this definition, "submitted"
#       means any form of electronic, verbal, or written communication sent
#       to the Licensor or its representatives, including but not limited to
#       communication on electronic mailing lists, source code control systems,
#       and issue tracking systems that are managed by, or on behalf of, the
#       Licensor for the purpose of discussing and improving the Work, but
#       excluding communication that is conspicuously marked or otherwise
#       designated in writing by the copyright owner as "Not a Contribution."
#
#       "Contributor" shall mean Licensor and any individual or Legal Entity
#       on behalf of whom a Contribution has been received by Licensor and
#       subsequently incorporated within the Work.
#
#    2. Grant of Copyright License. Subject to the terms and conditions of
#       this License, each Contributor hereby grants to You a perpetual,
#       worldwide, non-exclusive, no-charge, royalty-free, irrevocable
#       copyright license to reproduce, prepare Derivative Works of,
#       publicly display, publicly perform, sublicense, and distribute the
#       Work and such Derivative Works in Source or Object form.
#
#    3. Grant of Patent License. Subject to the terms and conditions of
#       this License, each Contributor hereby grants to You a perpetual,
#       worldwide, non-exclusive, no-charge, royalty-free, irrevocable
#       (except as stated in this section) patent license to make, have made,
#       use, offer to sell, sell, import, and otherwise transfer the Work,
#       where such license applies only to those patent claims licensable
#       by such Contributor that are necessarily infringed by their
#       Contribution(s) alone or by combination of their Contribution(s)
#       with the Work to which such Contribution(s) was submitted. If You
#       institute patent litigation against any entity (including a
#       cross-claim or counterclaim in a lawsuit) alleging that the Work
#       or a Contribution incorporated within the Work constitutes direct
#       or contributory patent infringement, then any patent licenses
#       granted to You under this License for that Work shall terminate
#       as of the date such litigation is filed.
#
#    4. Redistribution. You may reproduce and distribute copies of the
#       Work or Derivative Works thereof in any medium, with or without
#       modifications, and in Source or Object form, provided that You
#       meet the following conditions:
#
#       (a) You must give any other recipients of the Work or
#           Derivative Works a copy of this License; and
#
#       (b) You must cause any modified files to carry prominent notices
#           stating that You changed the files; and
#
#       (c) You must retain, in the Source form of any Derivative Works
#           that You distribute, all copyright, patent, trademark, and
#           attribution notices from the Source form of the Work,
#           excluding those notices that do not pertain to any part of
#           the Derivative Works; and
#
#       (d) If the Work includes a "NOTICE" text file as part of its
#           distribution, then any Derivative Works that You distribute must
#           include a readable copy of the attribution notices contained
#           within such NOTICE file, excluding those notices that do not
#           pertain to any part of the Derivative Works, in at least one
#           of the following places: within a NOTICE text file distributed
#           as part of the Derivative Works; within the Source form or
#           documentation, if provided along with the Derivative Works; or,
#           within a display generated by the Derivative Works, if and
#           wherever such third-party notices normally appear. The contents
#           of the NOTICE file are for informational purposes only and
#           do not modify the License. You may add Your own attribution
#           notices within Derivative Works that You distribute, alongside
#           or as an addendum to the NOTICE text from the Work, provided
#           that such additional attribution notices cannot be construed
#           as modifying the License.
#
#       You may add Your own copyright statement to Your modifications and
#       may provide additional or different license terms and conditions
#       for use, reproduction, or distribution of Your modifications, or
#       for any such Derivative Works as a whole, provided Your use,
#       reproduction, and distribution of the Work otherwise complies with
#       the conditions stated in this License.
#
#    5. Submission of Contributions. Unless You explicitly state otherwise,
#       any Contribution intentionally submitted for inclusion in the Work
#       by You to the Licensor shall be under the terms and conditions of
#       this License, without any additional terms or conditions.
#       Notwithstanding the above, nothing herein shall supersede or modify
#       the terms of any separate license agreement you may have executed
#       with Licensor regarding such Contributions.
#
#    6. Trademarks. This License does not grant permission to use the trade
#       names, trademarks, service marks, or product names of the Licensor,
#       except as required for reasonable and customary use in describing the
#       origin of the Work and reproducing the content of the NOTICE file.
#
#    7. Disclaimer of Warranty. Unless required by applicable law or
#       agreed to in writing, Licensor provides the Work (and each
#       Contributor provides its Contributions) on an "AS IS" BASIS,
#       WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
#       implied, including, without limitation, any warranties or conditions
#       of TITLE, NON-INFRINGEMENT, MERCHANTABILITY, or FITNESS FOR A
#       PARTICULAR PURPOSE. You are solely responsible for determining the
#       appropriateness of using or redistributing the Work and assume any
#       risks associated with Your exercise of permissions under this License.
#
#    8. Limitation of Liability. In no event and under no legal theory,
#       whether in tort (including negligence), contract, or otherwise,
#       unless required by applicable law (such as deliberate and grossly
#       negligent acts) or agreed to in writing, shall any Contributor be
#       liable to You for damages, including any direct, indirect, special,
#       incidental, or consequential damages of any character arising as a
#       result of this License or out of the use or inability to use the
#       Work (including but not limited to damages for loss of goodwill,
#       work stoppage, computer failure or malfunction, or any and all
#       other commercial damages or losses), even if such Contributor
#       has been advised of the possibility of such damages.
#
#    9. Accepting Warranty or Additional Liability. While redistributing
#       the Work or Derivative Works thereof, You may choose to offer,
#       and charge a fee for, acceptance of support, warranty, indemnity,
#       or other liability obligations and/or rights consistent with this
#       License. However, in accepting such obligations, You may act only
#       on Your own behalf and on Your sole responsibility, not on behalf
#       of any other Contributor, and only if You agree to indemnify,
#       defend, and hold each Contributor harmless for any liability
#       incurred by, or claims asserted against, such Contributor by reason
#       of your accepting any such warranty or additional liability.
#
#    END OF TERMS AND CONDITIONS
#
#    APPENDIX: How to apply the Apache License to your work.
#
#       To apply the Apache License to your work, attach the following
#       boilerplate notice, with the fields enclosed by brackets "{}"
#       replaced with your own identifying information. (Don't include
#       the brackets!)  The text should be enclosed in the appropriate
#       comment syntax for the file format. We also recommend that a
#       file or class name and description of purpose be included on the
#       same "printed page" as the copyright notice for easier
#       identification within third-party archives.
#
#    Copyright {yyyy} {name of copyright owner}
#
#    Licensed under the Apache License, Version 2.0 (the "License");
#    you may not use this file except in compliance with the License.
#    You may obtain a copy of the License at
#
#        http://www.apache.org/licenses/LICENSE-2.0
#
#    Unless required by applicable law or agreed to in writing, software
#    distributed under the License is distributed on an "AS IS" BASIS,
#    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#    See the License for the specific language governing permissions and
#    limitations under the License.
#

#set -x

# Install Scale2Insight application server from s2i ISO

# set Digital Edge environment
source /etc/rtwsrc

# local environment settings
S2I_ISO_FILE=/mnt/appfs/release/$RTWS_SW_VERSION/s2i.iso
S2I_ISO_ROOT=/mnt/s2i
S2I_PROP_DIR=/tmp
S2I_INSTALL_DIR=/opt/saic/s2i
S2I_CMD=$S2I_INSTALL_DIR/bin/s2i.sh
S2I_HADOOP_LIB=/s2i/workflows/lib

# assert that caller is root
if [[ "$(whoami)" != "root" ]]; then
     echo "ERROR: You must be root to proceed..."
     exit 1
fi

#
# mount S2i installation disk ISO
#

# assert that the ISO file exists
if [ ! -f $S2I_ISO_FILE ]; then
	echo "ERROR: S2I ISO File" $S2I_ISO_FILE "not found!"
	exit 1
fi

# create mount point
if [ ! -d $S2I_ISO_ROOT ]; then
	mkdir -p $S2I_ISO_ROOT
fi

# mount S2I ISO
mount -t iso9660 -o loop $S2I_ISO_FILE $S2I_ISO_ROOT
RCODE=$?
if [ $RCODE -ne 0 ]; then 
	echo "ERROR: Couldn't mount S2I ISO file" $S2I_ISO_FILE
	exit 1
fi

#
# Edit initial.properties
#

# extract initial.properties to $S2I_PROP_DIR;
(cd $S2I_PROP_DIR; jar xvf $S2I_ISO_ROOT/s2i-installer.jar  initial.properties >/dev/null)

# assign S2I installation variable
PROPS=$S2I_PROP_DIR/initial.properties

if [ ! -f $PROPS ]; then
    echo "ERROR: Couldn't extract initial.properties from" $S2I_ISO_ROOT/s2i-installer.jar
	exit 1
fi

echo -e "\ncompact.model=false" >> $PROPS

# update property value for a given key in $PROPS from 'localhost' to value provided
update_prop()
{
	key=$1;
	value=$2;

	# find the line that starts with 'key=' and replace 'localhost' with  the value
	sed -i "/^$key=/s/localhost/$value/" $PROPS
}

update_any_prop()
{
	any_key=$1;
	any_key_value=$2;
	any_value=$3;
	
	# find the line that starts with 'any_key=' and equals any_key_value ' with the any_value
	sed -i "/^$any_key=/s/$any_key_value/$any_value/" $PROPS
}

# define FQDN for servers
NAMENODE=namenode.$RTWS_DOMAIN
JOBTRACKER=jobtracker.$RTWS_DOMAIN
ZOOKEEPER=$RTWS_ZOOKEEPER_QUORUM_SERVERS
S2IAPPSRV=$RTWS_FQDN
DBTYPE=DFS

# backup the original file
cp $PROPS $PROPS.orig

# update properties
update_prop dfs.url $NAMENODE
update_prop dfs.jt.url $JOBTRACKER
update_prop nosql.hosts $ZOOKEEPER
update_prop oozie.url $S2IAPPSRV
update_prop job.path $NAMENODE
update_prop index.master.host $S2IAPPSRV
update_prop index.url $S2IAPPSRV
update_any_prop nosql.db hbase $DBTYPE

#
# install S2i to the cluster
#

# spot check that install succeeded
LIB_COUNT=`hadoop fs -ls $S2I_HADOOP_LIB 2> /dev/null | wc -l`
if [ $LIB_COUNT -eq 0 ]; then
    echo "Couldn't find S2I Hadoop folder" $S2I_HADOOP_LIB
	# run cluster installer
	sudo -u rtws PROPS=$PROPS s2i_zk_quorum=$ZOOKEEPER $S2I_ISO_ROOT/install-files.sh cluster
	
else
	sudo -u rtws PROPS=$PROPS s2i_zk_quorum=$ZOOKEEPER $S2I_ISO_ROOT/install-files.sh reinstall-zk
fi


# spot check that install succeeded
LIB_COUNT=`hadoop fs -ls $S2I_HADOOP_LIB 2> /dev/null | wc -l`
if [ $LIB_COUNT -eq 0 ]; then
    echo "ERROR: Couldn't find S2I Hadoop folder" $S2I_HADOOP_LIB
	exit 1
fi


# create S2I application installation directory
S2I_INSTALL_BASE=`dirname $S2I_INSTALL_DIR`
if [ ! -d $S2I_INSTALL_BASE ]; then
	mkdir -p $S2I_INSTALL_BASE
fi
chmod 777 $S2I_INSTALL_BASE

# install application server
sudo -u rtws PROPS=$PROPS s2i_zk_quorum=$ZOOKEEPER $S2I_ISO_ROOT/install-files.sh appserver

# spot check that installation succeeded
if [ ! -f $S2I_CMD ]; then
    echo "ERROR: Couldn't find application server command shell" $S2I_CMD
	exit 1
fi

#
# Start TOMCAT server
#

# assert that TOMCAT script exists
TOMCAT_BIN=$S2I_INSTALL_DIR/TOMCAT/bin
TOMCAT_START_SCRIPT=$TOMCAT_BIN/catalina.sh
if [ ! -x $TOMCAT_START_SCRIPT ]; then
    echo "ERROR: Couldn't not find executable TOMCAT start script" $TOMCAT_START_SCRIPT
	exit 1
fi

# configure TOMCAT to save PID file or get PID file path if it is already configured
TOMCAT_SETENV=$TOMCAT_BIN/setenv.sh
TOMCAT_PID_FILE=/tmp/catalina-pid.txt
if [ -f $TOMCAT_SETENV ]; then
	TEMP_PID_FILE=`grep "^CATALINA_PID" $TOMCAT_SETENV 2>/dev/null | cut -d '=' -f 2`
	if [ "$TEMP_PID_FILE" = "" ]; then
		echo "CATALINA_PID=$TOMCAT_PID_FILE" >> $TOMCAT_SETENV
	else
		TOMCAT_PID_FILE=$TEMP_PID_FILE
	fi
else
    echo "CATALINA_PID=$TOMCAT_PID_FILE" >> $TOMCAT_SETENV
fi


# modify files to be rtws user
chown -R rtws:rtws /opt/saic/s2i/


# start TOMCAT as rtws user
su -c "$TOMCAT_START_SCRIPT start" rtws

# get TOMCAT PID
TOMCAT_PID=0
if [ -f $TOMCAT_PID_FILE ]; then
	TOMCAT_PID=`cat $TOMCAT_PID_FILE`
fi

if [ "$TOMCAT_PID" -eq "0" ]; then
    echo "ERROR: Couldn't not determine TOMCAT PID from file" $TOMCAT_PID_FILE
	exit 1
fi

# wait for TOMCAT to start (for up to a minute)
count=12
tomcat_is_running=false
while [ $count -gt 0 ]; do
	TOMCAT_PORTS=`lsof -i TCP -a -s TCP:LISTEN -a -p $TOMCAT_PID -Fn -P | cut -d ':' -s -f 2 | tr '\n' ' '`
	for PORT in $TOMCAT_PORTS; do
		STATUS=`curl --connect-timeout 3 --max-time 3 -s -D - http://localhost:$PORT -o /dev/null | head -n 1 | grep "200 OK" | wc -l`
		if [ "$STATUS" -eq 1 ]; then
			tomcat_is_running=true
			break 2					# we bale out on first successful HTTP response
		fi
	done
	sleep 5
	let count=$count-1
done

if ! $tomcat_is_running; then
    echo "ERROR: Couldn't start TOMCAT via start script" $TOMCAT_START_SCRIPT
	exit 1
fi

# 
# Route 80 -> 8080
iptables -A PREROUTING -t nat -i eth0 -p tcp --dport 80 -j REDIRECT --to-port 8080

#
# Create sample workspace
#

WS_NAMESPACE=digitaledge
WS_FIELDS=id
WS_DESC="DigitalEdge Dummy Workspace"

printf "%s\n%s\n%s" $WS_NAMESPACE $WS_FIELDS $WS_DESC | sudo -u rtws bash $S2I_CMD add-ws
RCODE=$?
if [ $RCODE -ne 0 ]; then 
	echo "ERROR: Couldn't not create workspace with namespace" $WS_NAMESPACE "and fields" $WS_FIELDS
	exit 1
fi

echo "ABOUT TO TOUCH FILE TO NOTIFY DE DataSink"

# Flag to alert datasinks that S2i Environment has been setup has completed
su rtws -l -s /bin/bash -c "hadoop fs -touchz /s2i/.install_complete"

exit 0
