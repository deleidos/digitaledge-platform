#!/bin/bash
#
# Add a list of users to the instance

if [ $# -eq 0 ]; then
    echo "Usage: addusers.sh <user1> <user2> ..."
    exit 1
fi

for USER in "$@"
do
    echo "Adding user '$USER'"
    adduser --shell /bin/bash --disabled-password --gecos ",,," $USER
done