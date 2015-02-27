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

APPLICATION="judge-manager"

WORK_DIR=`dirname "$0"`
PID_FILE="$WORK_DIR"/"${APPLICATION}.pid"
JAVA_PATH=/path/to/java
OUTPUT_FILE="$WORK_DIR"/"${APPLICATION}.out"
JAR_FILE="$WORK_DIR"/"${APPLICATION}-jar-with-dependencies.jar"
LOG4J_FILE="$WORK_DIR"/"${APPLICATION}-log4j.xml"
CONFIGURATION_FILE="$WORK_DIR"/"${APPLICATION}-configuration.xml"

source "$WORK_DIR"/"common.sh"

if [ "$1" = "start" ]; then
  startApp $APPLICATION
elif [ "$1" = "stop" ]; then
  stopApp $APPLICATION
elif [ "$1" = "restart" ]; then
  stopApp $APPLICATION
  startApp $APPLICATION
elif [ "$1" = "status" ]; then
  statusApp $APPLICATION
else
  echo -e "Usage:\n  $0 <start|stop|restart|status>"
fi

