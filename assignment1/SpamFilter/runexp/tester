#!/bin/bash

#MAX_N=1
LOG_NB_BUCKETS=5
#NB_HASHES=3
THRESHOLD=0.5
#LEARNING_RATE=0.0001

PERIOD=1000

DATA="/cw/bdap/assignment1/Data/index"

STOPWORDS="../stop-word-list_stanford.txt"


tester(){
	local machine=$1
	local LOG_NB_BUCKETS=$2

	for MAX_N in `seq 1 5`; do
		#OUT1="output/out_${MAX_N}_${LOG_NB_BUCKETS}"
		#rm -f "${OUT1}.nbfh.*"
		#ssh $machine "cd code; time java -cp .:lib/* NaiveBayesFeatureHashing ${DATA} ${STOPWORDS} ${LOG_NB_BUCKETS} ${THRESHOLD} ${OUT} ${PERIOD} ${MAX_N} " 

		for LEARNING_RATE in 0.00001 0.00005 0.0001 0.0005 0.001 0.005 0.01; do
			OUT1="output/out_${MAX_N}_${LOG_NB_BUCKETS}_${LEARNING_RATE}_$NB_HASHES"
			rm -f "${OUT2}.pfh.*"
			ssh $machine "cd code; time java -cp .:lib/* PerceptronFeatureHashing ${DATA} ${STOPWORDS} ${LOG_NB_BUCKETS} ${LEARNING_RATE} ${OUT2} ${PERIOD} ${MAX_N} " 

			for NB_HASHES in `seq 2 10 2`; do

				$OUT2="output/out_${MAX_N}_${LOG_NB_BUCKETS}_${NB_HASHES}"
				rm -f "${OUT2}.pcms.*"
				OUT2="output/out_${MAX_N}_${LOG_NB_BUCKETS}_${LEARNING_RATE}_$NB_HASHES"
				ssh $machine "cd code; time java -cp .:lib/* PerceptronCountMinSketch ${DATA} ${STOPWORDS} ${LOG_NB_BUCKETS} ${NB_HASHES} ${LEARNING_RATE} ${OUT2} ${PERIOD} ${MAX_N} "  
			#	ssh $machine "cd code; time java -cp .:lib/* NaiveBayesCountMinSketch ${DATA} ${STOPWORDS} ${LOG_NB_BUCKETS} ${NB_HASHES} ${THRESHOLD} ${OUT2} ${PERIOD} ${MAX_N} " 
			done
		done
	done 
}

while read machine; do
	echo $machine        	
	
	tester $machine $LOG_NB_BUCKETS &
	COUNT=$COUNT+1

	LOG_NB_BUCKETS=$(($LOG_NB_BUCKETS + 1))
	echo $LOG_NB_BUCKETS

done <testinghosts

