#!/bin/bash
set -x

# Sets up a simple ftp/sftp server

source /etc/rtwsrc

if [[ "$(whoami)" != "root" ]]; then
     echo "ERROR: You must be root to proceed..."
     exit -1
fi

if [[ -z "$RTWS_SFTP_USER" && -z "$RTWS_SFTP_USER_PW" ]]; then
    echo "ERROR: Missing the required user data parameters."
    exit -1
fi

apt-get update
apt-get -y install vsftpd nfs-kernel-server

useradd -m -p "$RTWS_SFTP_USER_PW" -s /bin/bash $RTWS_SFTP_USER

if [ ! -d /mnt/ebs1 ]; then
	mkdir /mnt/ebs1 ; chmod 777 /mnt/ebs1 ; chown $RTWS_SFTP_USER:$RTWS_SFTP_USER /mnt/ebs1
fi

service vsftpd stop

rm -f /etc/vsftpd.chroot_list
cat >> /etc/vsftpd.chroot_list << "EOF"
pwc
EOF

chmod 444 /etc/vsftpd.chroot_list ; chown root:root /etc/vsftpd.chroot_list

mv -f  /etc/vsftpd.conf /etc/.vsftpd.conf.orig
cat >> /etc/vsftpd.conf << "EOF"
listen=YES
anonymous_enable=NO
local_enable=YES
write_enable=YES
dirmessage_enable=YES
use_localtime=YES
xferlog_enable=YES
connect_from_port_20=YES
chroot_local_user=YES
chroot_list_file=/etc/vsftpd.chroot_list
secure_chroot_dir=/var/run/vsftpd/empty
pam_service_name=vsftpd
rsa_cert_file=/etc/ssl/private/vsftpd.pem
EOF
chmod 444 /etc/vsftpd.conf ; chown root:root /etc/vsftpd.conf


service vsftpd start

echo "" > /etc/exports
cat >> /etc/exports << "EOF"
# /etc/exports: the access control list for filesystems which may be exported
#               to NFS clients.  See exports(5).
#
# Example for NFSv2 and NFSv3:
# /srv/homes       hostname1(rw,sync,no_subtree_check) hostname2(ro,sync,no_subtree_check)
#
# Example for NFSv4:
# /srv/nfs4        gss/krb5i(rw,sync,fsid=0,crossmnt,no_subtree_check)
# /srv/nfs4/homes  gss/krb5i(rw,sync,no_subtree_check)
#
/mnt/ebs1 *(rw,sync,no_root_squash)
EOF
exportfs -a