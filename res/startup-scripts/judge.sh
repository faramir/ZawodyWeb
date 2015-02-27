#!/bin/bash
#
# Copyright (c) 2015, Marek Nowicki
# All rights reserved.
#
# This file is distributable under the Simplified BSD license. See the terms
# of the Simplified BSD license in the documentation provided with this file.
#
# Author: Marek Nowicki /faramir/
#

APPLICATION="judge"

WORK_DIR=`dirname "$0"`
SUBMITS_DIR="$WORK_DIR"/"submits"
mkdir -p $SUBMITS_DIR

JAVA_PATH=/path/to/java
JAR_FILE="$WORK_DIR"/"${APPLICATION}-jar-with-dependencies.jar"
LOG4J_FILE="$WORK_DIR"/"${APPLICATION}-log4j.xml"

source "$WORK_DIR"/"common.sh"

if [ "$1" = "start" ]; then
  NUM=$(find "$SUBMITS_DIR" -maxdepth 1 -name "[0-9]*" | wc -l)
  mkdir -p ./submits/$NUM/compiled
  mkdir -p ./submits/$NUM/sources
  sed "s/NUMBER/$NUM/g" "$WORK_DIR"/"${APPLICATION}-configuration.xml" > "$SUBMITS_DIR"/$NUM/configuration.xml
  sed "s/NUMBER/$NUM/g" "$WORK_DIR"/"${APPLICATION}-log4j.xml" > "$SUBMITS_DIR"/$NUM/log4j.xml
  PID_FILE="$SUBMITS_DIR"/$NUM/"${APPLICATION}.pid"
  OUTPUT_FILE="$SUBMITS_DIR"/$NUM/"${APPLICATION}.out"
  CONFIGURATION_FILE="$SUBMITS_DIR"/$NUM/configuration.xml
  LOG4J_FILE="$SUBMITS_DIR"/$NUM/log4j.xml

  startApp $APPLICATION/$NUM
elif [ "$1" = "stop-all" ]; then
  for JUDGE in $(find "$SUBMITS_DIR" -maxdepth 1 -name "[0-9]*" | sort) ; do
    PID_FILE=$JUDGE/"${APPLICATION}.pid"
    stopApp $APPLICATION/"`basename $JUDGE`"
  done
elif [ "$1" = "status-all" ]; then
  for JUDGE in $(find "$SUBMITS_DIR" -maxdepth 1 -name "[0-9]*" | sort) ; do
    PID_FILE=$JUDGE/"${APPLICATION}.pid"
    statusApp $APPLICATION/"`basename $JUDGE`"
  done
else
  echo -e "Usage:\n  $0 <start|stop-all|status-all>"
fi
