#!/bin/bash
# Debug aid, use when needed.


if [[ "$(whoami)" != "root" ]]; then
     echo "ERROR: You must be root to proceed..."
     exit 1
fi

cd /usr/local
wget http://java.net/projects/visualvm/downloads/download/release134/visualvm_134.zip
unzip visualvm_134.zip
ln -sf /usr/local/visualvm_134/bin/visualvm /usr/local/bin/.
rm -f visualvm_134.zip
