#! /bin/bash

if [ $# -ne 1 ]
then
  echo "Usage: puma.sh appname"
  exit
fi
apkname=`basename $1`;
adb install -r $1
package=`aapt d badging $1 | grep package: | awk -F\' '{print $2}'`
appLabel=`aapt d badging $1 | grep application-label: | awk -F\' '{print $2}'`
echo $package > ./puma/app.info
echo $appLabel >> ./puma/app.info
./puma/setup-phone.sh
./puma/run.sh
appName=${apkname%.apk}
#echo $appName
rm -r result/$appName;
mkdir result/$appName;
adb pull /data/local/tmp/local/tmp ./result/$appName;
adb shell rm -r /data/local/tmp/local/tmp/;
adb uninstall $package;
