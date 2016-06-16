#!/bin/bash
#set -x

THISTAG=$1
NEXTTAG=$2
USERNAME=$3
PASS=$4
USERPARAMS="--username $USERNAME --password $PASS --no-auth-cache --non-interactive"
echo params = $USERPARAMS
echo tag = $THISTAG

if [[ -z "$JAVA_HOME" ]]; then
    echo "ERROR: JAVA_HOME is not set"
    exit 1 
fi

which mvn 
if [[ $? -ne 0 ]]; then
	echo "ERROR: Unable to find mvn"
	exit 1
fi

MVN_V="$(mvn -version)"
if [[ -z "$(echo $MVN_V | grep -w "3.0.5")" ]]; then
    echo "ERROR: Maven version 3.0.5 not found."
    exit 1
fi

svn copy $USERPARAMS https://issbu.svn.cvsdude.com/rt/rtws/trunk https://issbu.svn.cvsdude.com/rt/rtws/tags/$THISTAG -m "Release $THISTAG"

PROJECTS=`svn $USERPARAMS list https://issbu.svn.cvsdude.com/rt/rtws/tags/$THISTAG`

for proj in $PROJECTS
do
        echo "Checking out project $proj"
        svn checkout $USERPARAMS --depth files https://issbu.svn.cvsdude.com/rt/rtws/tags/$THISTAG/$proj
done

#special checkout for buildtools
svn checkout $USERPARAMS --depth files https://issbu.svn.cvsdude.com/rt/rtws/tags/$THISTAG/commons/buildtools commons/buildtools

#special stuff for parent, buildtools
cd parent
mvn -Dusername=$USERNAME -B -Dpassword=$PASS versions:set -N versions:update-child-modules -DnewVersion=$THISTAG
if [ $? -ne 0 ]; then
    echo "ERROR: Failed to update version information"
    exit 1
fi
cd ..

cd commons/buildtools
sed -i s/rtws.version=.*/rtws.version=$THISTAG/g buildMaster.properties
sed -i s/release.nightly=.*/release.nightly=$THISTAG/g buildMaster.properties
sed -i s/template.project.dist.jar.version=.*/template.project.dist.jar.version=$THISTAG/g buildTemplate.properties
cd ..
cd ..


for dir in $(find . -type d)
do
        echo "Modifying project $dir"
   cd $dir
   sed -i s/tenant.system.release=.*/tenant.system.release=$NEXTTAG/g tenant.properties 2> /dev/null
   cd -
done

for proj in $PROJECTS
do
        echo "Check in project $proj"
        svn commit $USERPARAMS --depth files $proj -m "change properties for tag"
done

#special checkin for buildtools
svn commit $USERPARAMS --depth files commons/buildtools -m "change properties for tag"

#------------------------MOVING ON TO CHANGE TRUNK PROPERTIES----------------------
echo "Modifying Trunk"

mkdir changeTrunk
cd changeTrunk

PROJECTS=`svn $USERPARAMS list https://issbu.svn.cvsdude.com/rt/rtws/trunk`

for proj in $PROJECTS
do
        echo "Checking out trunk project $proj"
        svn checkout $USERPARAMS --depth files https://issbu.svn.cvsdude.com/rt/rtws/trunk/$proj
done

#special checkout for buildtools
svn checkout $USERPARAMS --depth files https://issbu.svn.cvsdude.com/rt/rtws/trunk/commons/buildtools commons/buildtools


#all projects now checked out, and must be modified

#special stuff for parent, buildtools
cd parent
mvn -Dusername=$USERNAME -B -Dpassword=$PASS versions:set -N versions:update-child-modules -DnewVersion=$NEXTTAG
if [ $? -ne 0 ]; then
    echo "ERROR: Failed to update version information"
    exit 1
fi
cd ..

cd commons/buildtools
sed -i s/rtws.version=.*/rtws.version=$NEXTTAG/g buildMaster.properties
#sed -i s/release.nightly=.*/release.nightly=$NEXTTAG/g buildMaster.properties #needed?
sed -i s/template.project.dist.jar.version=.*/template.project.dist.jar.version=$NEXTTAG/g buildTemplate.properties
cd ..
cd ..


for dir in $(find . -type d)
do
   echo "Modifying project $dir"
   cd $dir
   sed -i s/tenant.system.release=.*/tenant.system.release=$NEXTTAG/g tenant.properties 2> /dev/null
   cd -
done

for proj in $PROJECTS
do
        echo "Check in project $proj"
        svn commit $USERPARAMS --depth files $proj -m "change properties for trunk"
done

#special checkin for buildtools
svn commit $USERPARAMS --depth files commons/buildtools -m "change properties for trunk"
