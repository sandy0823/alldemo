#!/bin/bash
SH_DIR=$(cd `dirname $0`;pwd)
ROOT_DIR=$(dirname "$SH_DIR")
LOGS_DIR=$ROOT_DIR/log

count=`ps aux|grep consul-product.jar |grep -v grep |wc -l`
(date && ps aux|grep consul-product.jar |grep -v grep)>> $LOGS_DIR/stop.log>&1
if [ $count -gt 0 ];then
   pids=$(ps -ef|grep consul-product.jar|grep -v grep |awk '{print $2}')
   echo pids=$pids  >> $LOGS_DIR/stop.log>&1
   array_name=($pids)
   for pid in ${array_name[*]}
   do
      echo "kill ${pid}" >> $LOGS_DIR/stop.log>&1
      kill -9 $pid >> $LOGS_DIR/stop.log>&1
   done
   (date && echo "stop app success")>> $LOGS_DIR/stop.log>&1
   exit 0
else
   (date && echo "app has stop")>> $LOGS_DIR/stop.log>&1
   exit 1
fi


