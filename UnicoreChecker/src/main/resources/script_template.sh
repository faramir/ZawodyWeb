#!/bin/bash
#
# Copyright (c) 2014-2015, ICM UW
# All rights reserved.
#
# This file is distributable under the Simplified BSD license. See the terms
# of the Simplified BSD license in the documentation provided with this file.
#
# Author: Marek Nowicki /faramir/
#

srun hostname -s | sort > nodes.lst
uniq nodes.lst > nodes.uniq

echo -e "`date`\nhosts: `uniq -c nodes.lst | xargs -I '{}' echo -n '{}, '`" 1>&2

echo -e "\n### MODULE LOAD ###" 1>&2
module load plgrid/tools/openmpi plgrid/tools/java8 1>&2
MODULE_ERROR=$?
if [ $MODULE_ERROR -ne 0 ]; then
    echo -e "\n### ERROR: MODULE_ERROR: $MODULE_ERROR ###" 1>&2
    exit 3
fi

echo -e "\n### COMPILE FILES ###" 1>&2
javac -cp .:PCJ-4.0.1.SNAPSHOT-bin.jar MainClass.java ${CODE_FILE} 1>&2
COMPILE_ERROR=$?
if [ $COMPILE_ERROR -ne 0 ]; then
    echo -e "\n### ERROR: COMPILER_ERROR: $COMPILE_ERROR ###" 1>&2
    exit 2
fi

echo -e "\n### START EXECUTION ###" 1>&2
timer=`date +%s%N`
mpiexec --hostfile nodes.uniq bash -c 'java -cp .:PCJ-4.0.1.SNAPSHOT-bin.jar ${UNICORECC_JVMARGS} MainClass ${CODE_FILE} ${CODE_FILE} nodes.lst < ${INPUT_FILE}'
RUNTIME_ERROR=$?
timer=$(( `date +%s%N` - $timer ))
echo -e "\n### ELAPSED TIME IN NANOS: $timer ###" 1>&2

RUNTIME_ERROR=$?
if [ $RUNTIME_ERROR -ne 0 ]; then
    echo -e "\n### ERROR: RUNTIME_ERROR: $RUNTIME_ERROR ###" 1>&2
    exit 1
fi
