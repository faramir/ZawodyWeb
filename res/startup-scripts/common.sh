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

function remove_pid_file {
  rm -f "$PID_FILE" >/dev/null 2>&1
  if [ $? != 0 ]; then
    if [ -w "$PID_FILE" ]; then
      cat /dev/null > "$PID_FILE"
    else
      echo "Unable to remove or clear stale PID file. Start aborted."
      return 1
    fi
  fi
}

function statusApp {
  local APPLICATION=$1
  if [ -f "$PID_FILE" ]; then
    if [ -s "$PID_FILE" ]; then
      if [ -r "$PID_FILE" ]; then
        PID=`cat "$PID_FILE"`
        ps -p $PID >/dev/null 2>&1
        if [ $? -eq 0 ] ; then
          echo "${APPLICATION} appears to be running with PID $PID."
        else
          echo "${APPLICATION} with PID $PID is problably not running."
        fi
      else
        echo "${APPLICATION}: Unable to read PID file."
        return 1
      fi
    else
      echo "Empty PID file. ${APPLICATION} probably is stopped."
      return 1
    fi
  else
    echo "No PID file. ${APPLICATION} probably is stopped."
  fi
}

function startApp {
  local APPLICATION=$1
  if [ -f "$PID_FILE" ]; then
    if [ -s "$PID_FILE" ]; then
      echo "${APPLICATION}: Existing PID file found during start."
      if [ -r "$PID_FILE" ]; then
        PID=`cat "$PID_FILE"`
        ps -p $PID >/dev/null 2>&1
        if [ $? -eq 0 ] ; then
          echo "${APPLICATION} appears to still be running with PID $PID. Start aborted."
          return 1
        else
          echo "${APPLICATION}: Removing/clearing stale PID file."
          remove_pid_file
        fi
      else
        echo "${APPLICATION}: Unable to read PID file. Start aborted."
        return 1
      fi
    else
      rm -f "$PID_FILE" >/dev/null 2>&1
      if [ $? != 0 ]; then
        if [ ! -w "$PID_FILE" ]; then
          echo "${APPLICATION}: Unable to remove or write to empty PID file. Start aborted."
          return 1
        fi
      fi
    fi
  fi
  
  eval "\"$JAVA_PATH\"/bin/java"  \
       -Dlog4j.configuration=file:$LOG4J_FILE \
       -jar "\"$JAR_FILE\"" \
       "\"$CONFIGURATION_FILE\"" \
       >> "$OUTPUT_FILE" 2>&1 "&"
  
  PID="UNKNOWN"
  if [ ! -z "$PID_FILE" ]; then
    echo $! > "$PID_FILE"
    PID=`cat "$PID_FILE"`
  fi

  echo "${APPLICATION} started ($PID)."
}

function stopApp {
  local APPLICATION=$1
  if [ -f "$PID_FILE" ]; then
    if [ -s "$PID_FILE" ]; then
      PID=`cat "$PID_FILE"`
      kill -0 $PID >/dev/null 2>&1
      if [ $? -gt 0 ]; then
        echo "${APPLICATION}: PID file found but no matching process was found. Stop aborted."
        remove_pid_file
        return 1
      fi
      echo "Killing ${APPLICATION} with the PID: $PID"
      kill -9 $PID >/dev/null 2>&1
      for i in {0..5}; do
        kill -0 $PID >/dev/null 2>&1 
        if [ $? -gt 0 ]; then
          remove_pid_file
          echo "${APPLICATION} stopped."
          break
        fi
        sleep 1
      done
    else
      echo "${APPLICATION}: PID file is empty and has been ignored."
    fi
  else
    echo "${APPLICATION}: PID file does not exist."
    return 1
  fi
}
