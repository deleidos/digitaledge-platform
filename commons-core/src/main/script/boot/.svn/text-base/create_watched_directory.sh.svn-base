CONFIG_FILE="/usr/local/rtws/transport/conf/transports.default.xml"
WATCHED_DIR=`cat $CONFIG_FILE | grep watched-directory`
WATCHED_DIR=${WATCHED_DIR#*directory>}
WATCHED_DIR=${WATCHED_DIR%%</watched*}
LENGTH=${#WATCHED_DIR}
echo $LENGTH
if [ $LENGTH -gt 1 ]
then
        echo "creating watched directory if not exists '$WATCHED_DIR'"
        if [ ! -d "$WATCHED_DIR" ]
        then
                mkdir $WATCHED_DIR
                chmod 777 $WATCHED_DIR
                echo "created watched directory '$WATCHED_DIR'"
        else
                echo "watched directory already exists"
        fi
fi
