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
cd puma
./setup-phone.sh
./run.sh
cd ..
appName=${apkname%.apk}
#echo $appName
rm -r result/$appName;
mkdir result/$appName;
adb pull /data/local/tmp/local/tmp ./result/$appName;
adb pull /sdcard/ColorProf.txt ./result/$appName;
adb shell rm -r /data/local/tmp/local/tmp/;
adb shell rm  /sdcard/time*;
adb shell rm  /sdcard/ColorProf.txt;
adb uninstall $package;
