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


#
# Expected AWS userdata parameters.
# RTWS_RAID_DEVICES - Comma separated list of device names that should be managed as a single volume.
# RTWS_RAID_PATH - The file system path on which to mount the RAID.
#
#
#  Note: Support for newer kernels added as mentioned in AWS console
#      "....Note: Newer linux kernels may rename your devices to /dev/xvdf through /dev/xvdp internally, 
#		  even when the device name entered here (and shown in the details) is /dev/sdf through /dev/sdp."

if [ $# -ne 3 ]; then
  echo "usage: mount_raid_device.sh [user] [group] [mode]"
  exit 1
fi
user=$1
group=$2
mode=$3

source /etc/rtwsrc


# Java hook here is to block until all volumes are attached.
# It's in java vs the ec2-XXX tools to avoid having to create and distribute a cert
java -classpath $(echo /usr/local/rtws/boot-apps/lib/*|tr -s ' ' ':'):/usr/local/rtws/commons-core/conf/ com.deleidos.rtws.commons.cloud.util.BootVolumesApp

if [ $? -ne 0 ]; then
	echo "ERROR: The blocking app had a problem.  ($?)"
fi


deviceNames=`echo $RTWS_RAID_DEVICES | tr ',' ' '`
deviceCount=`echo $deviceNames | wc -w`

# If there are no devices to be mounted, do nothing.
if [ $deviceCount -eq 0 ]; then
	
	# Always create base path, set initial permissions on root folder
	
	if [ ! -z $RTWS_RAID_PATH ]; then
	
		mkdir -p $RTWS_RAID_PATH
		chown $user:$group $RTWS_RAID_PATH
   	 	chmod $mode $RTWS_RAID_PATH
    
    fi

	exit 0
elif [ -z "$RTWS_RAID_PATH" ]; then
	echo "No mount point specified."
	exit 1
fi

# Wait until EC2 attaches the 1st of the requested devices (regardless of name)
unattachedDevices=`echo $RTWS_RAID_DEVICES | tr ',' ' ' | awk '{print $1}'`
while [ -n "$unattachedDevices" ]
do
	for device in $unattachedDevices
	do
		case $device in 
			/dev/sd[f-p][0-9] | /dev/xvd[f-p][0-9] | /dev/vd[b-p] )
					if [ -e $device ]; then
						unattachedDevices=`echo "$unattachedDevices" | tr ' ' '\n' | grep -v $device | xargs`
					fi
				;;
			* ) 
				echo "Unknown volume found...."
				exit 1				
				;;
		esac
	done
	
	if [ "$wt" == "...................................." ]; then
		echo "Expected storage devices not attached: $deviceNames"
		echo "Falling through to see if they were attached under an unexpected name."
		unattachedDevices=`echo "$unattachedDevices" | tr ' ' '\n' | grep -v $device | xargs`
	elif [ -n "$unattachedDevices" ]; then
		wt="$wt""."
		sleep 5
	fi
done

deviceNamesCheck=`grep RTWS_RAID_DEVICES /etc/rtwsrc | tr ',' ' ' | awk -F'=' '{print $2}' | sed "s/\/dev\///g" `
# Check and replace with correct mount points
deviceNamesReset=1
for device in $deviceNamesCheck 
do 
	cat /proc/partitions | grep -w $device > /dev/null 2>&1
	if [ $? -ne 0 ]; then   	
		# Look for the correct one
		for pre in sd xvd vd
		do
			# Give preference to xvdf1,xvdf2, etc.. device names since newer Ubuntu 12.04 instance in AWS has xvdb as root device
			# and do not want to mess up what is attached in Euca
			for vname in p o n m l k j i h g f e d c b
			do
				num="$(echo $device | tr -d '[a-z]')"
				if [ -e /dev/${pre}${vname}${num} ]; then
					echo "WARNING: Correcting block device for $device for newer kernel"
					newName="${pre}${vname}"
            				perl -i -pe "s/$(echo $device | tr -d '[0-9]')/${newName}/g" /etc/rtwsrc
            		
					# Re-source and move on
					source /etc/rtwsrc
					deviceNames=`echo $RTWS_RAID_DEVICES | tr ',' ' '`
					deviceCount=`echo $deviceNames | wc -w`
            				break 3;
				elif [  -e /dev/${pre}${vname} ]; then
				        echo "WARNING: Correcting block device for $device for newer kernel"
                                        newName="${pre}${vname}"
                                        perl -i -pe "s/$(echo $device)/${newName}/g" /etc/rtwsrc

                                        # Re-source and move on
                                        source /etc/rtwsrc
                                        deviceNames=`echo $RTWS_RAID_DEVICES | tr ',' ' '`
                                        deviceCount=`echo $deviceNames | wc -w`
                                        break 3;
				fi				
			done
		done            
	fi
done

# Safety net to wait for all of the expected (possibly renamed) devices to be attached.
unattachedDevices=$deviceNames
while [ -n "$unattachedDevices" ]
do
	for device in $unattachedDevices
	do
		if [ -e $device ]; then
			unattachedDevices=`echo "$unattachedDevices" | tr ' ' '\n' | grep -v $device | xargs`
		fi
	done
	if [ "$wait" == "............" ]; then
		echo "Expected storage devices not attached: $deviceNames"
		exit 1
	elif [ -n "$unattachedDevices" ]; then
		wait="$wait""."
		sleep 10
	fi
done

# Set the mount device
if [ $deviceCount -gt 1 ]; then
	# Handle different mount location for Ubuntu 12.04 in AWS
	if [ "$(grep -c precise /etc/lsb-release)" -eq 1 ]; then
		mountDevice=/dev/md127
	else
		mountDevice=/dev/md0
	fi
else
    mountDevice=$deviceNames
fi 

# Java hook here is to check the creation time of all volumes.
# It's in java vs the ec2-XXX tools to avoid having to create and distribute a cert
java -classpath $(echo /usr/local/rtws/boot-apps/lib/*|tr -s ' ' ':'):/usr/local/rtws/commons-core/conf/ com.deleidos.rtws.commons.cloud.util.CheckVolumeCreationTime

canCreate=$?

if [ $canCreate -ne 0 ]; then
	echo "INFO: A volume was not within the creation window, no filesystem can be created on the volumes, existing volumes/raid.  ($canCreate)"
fi

# Loop until the device is mounted
while [ 1 ]; do

    mounted=`mount | grep $mountDevice`
    
    mountSleepTime=`cat /usr/local/rtws/properties/rtws-common.properties | grep eucalyptus.blades.attach.volumes.delay.time.seconds | cut -d'=' -f2`
    if [ -z "$mountSleepTime" ]; then
	mountSleepTime=5
    fi
    
    if [ -n "$mounted" ]; then
        echo "Device $mountDevice is mounted [$mounted], exiting..."
        break
    else
        echo "Device $mountDevice not mounted, sleeping for $mountSleepTime seconds..."
        sleep $mountSleepTime
    fi

    # If there is more than one device specified, build the RAID.
    if [ $deviceCount -gt 1 ]; then

		#checks to see if it can read the superblock on device, lists info, if info returned then reassemble devices
		formattedDevices=`mdadm --examine $deviceNames`
		#this is a better solution, but will not work with new AMI
		#formattedDevices=`mdadm --query --detail /dev/md0 | grep "dev" | awk '{if (NR!=1) {print}}' | awk '{print $7}'`
	
		if [ ! -z "$RTWS_FQDN" ]; then
	    	raidName="$(echo $RTWS_FQDN | cut -c1-31)"
	    elif [ ! -z "$RTWS_MANIFEST" ]; then
	    	# Grab from manifest path
	    	tmpName="$(echo $RTWS_MANIFEST | cut -d/ -f5)"
	    	raidName="$(echo master.$tmpName | cut -c1-31)"
	    else
	    	raidName="UNABLE_TO_DETERMINE_NAME"
	    fi
	    
	    # If the devices are already formatted, re-assemble the existing RAID.
	    if [ -n "$formattedDevices" ]; then
		    mdadm --assemble $mountDevice $deviceNames		
	    # Otherwise, build the RAID partitions.
	    else
	    	if [ $canCreate -eq 0 ]; then
		    	mdadm --create $mountDevice --name="$raidName" --chunk=256 --level=0 --metadata=1.1 --raid-devices=$deviceCount $deviceNames
		    	mkfs.xfs $mountDevice
		    else
		    	echo "ERROR: A volume was not within the creation window, no filesystem can be created on the volumes, existing volumes/raid.  ($canCreate), exiting."
		    	exit 1
		    fi
	    fi
	
    # If there is only one device, it can simply be mounted directly.
    else

	    fileSystem=`file -s $mountDevice | grep "XFS"`
	
	    # If the device is blank; build a file system.
	    if [ -z "$fileSystem" ]; then
		    mkfs.xfs $mountDevice
	    fi
	
    fi

    # Set device properties/permissions and mount the volume.
    if [ ! -z "$(echo $RTWS_PROCESS_GROUP | grep -i mongo)" ];
    then
         echo "Lowering the readhead for mongo instances as recommended.   http://docs.mongodb.org/manual/administration/production-notes/"
         blockdev --setra 32 $deviceNames
    else
         blockdev --setra 16384 $deviceNames
    fi
    
    if [ ! -d $RTWS_RAID_PATH ]; then
        mkdir $RTWS_RAID_PATH
    fi
    
    mount $mountDevice $RTWS_RAID_PATH
    chown $user:$group $RTWS_RAID_PATH
    chmod $mode $RTWS_RAID_PATH

done
