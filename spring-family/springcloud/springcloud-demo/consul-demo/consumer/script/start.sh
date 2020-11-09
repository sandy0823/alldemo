#!/bin/bash
SH_DIR=$(cd `dirname $0`;pwd)
ROOT_DIR=$(dirname "$SH_DIR")
LOGS_DIR=$ROOT_DIR/log

if [ ! -d ${LOGS_DIR} ];then
   mkdir ${LOGS_DIR} 
   chmod -R 777 ${LOGS_DIR} 
fi

nohup java -jar $ROOT_DIR/consul-consumer.jar &
sleep 1
count=`ps aux|grep consul-consumer.jar |grep -v grep |wc -l`
(date && ps aux|grep consul-consumer.jar |grep -v grep && echo $count )>> $LOGS_DIR/start.log>&1
if [ $count -eq 1 ];then
   (date && echo "start app success")>> $LOGS_DIR/start.log>&1
   exit 0
else
   (date && echo "start app fail")>> $LOGS_DIR/start.log>&1
   exit 1
fi
