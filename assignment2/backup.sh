#!/bin/bash
DATE=`date '+%m%d.%H%M'`
tar -czvf "backup($DATE).tgz" *.java
tar -czvf "backupCSV($DATE).tgz" *.csv
