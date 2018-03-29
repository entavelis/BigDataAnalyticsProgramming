#!/bin/bash
> m$1x$1.txt
for i in `seq 1 $1`;
do
    temp=""
    echo "test"
    for j in `seq 1 $1`;
    do
      temp="$temp $RANDOM" 
    done
    echo $temp >> m$1x$1.txt
done

