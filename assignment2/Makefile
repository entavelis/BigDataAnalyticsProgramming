##
## Makefile
##
## Evangelos Ntavelis 
## Feb 2018
## 


INPUT=./tweetsample
INPUTANA=tweets


threshold=0.9
maxFiles=100000
maxAll=7416113
nBuckets=1000000
nRows=10
nBands=4
shingleLength=3
nShingles=32000

# Experiment parameters
OUT=output10000S



all: BruteForceSearch.class Hash.class Primes.class LSH.class MurmurHash.class SimilarPair.class Runner.class Searcher.class Shingles.class ShinglerTokens.class TwitterReader.class UniversalHash.class 


bf: Runner.class
	@echo "Testing Brute Force"
	time java -cp .:lib/* Runner -threshold $(threshold) -method bf -maxFiles $(maxFiles) -inputPath $(INPUT) -outputPath $(OUT) -shingleLength $(shingleLength) -nShingles $(nShingles) -nRows $(nRows) -nBuckets $(nBuckets) -nBands $(nBands) 


lshall: Runner.class
	@echo "Testing LSH"
	time java -XX:+UseCompressedOops -Xmx2048m -Xms2048m Runner -threshold $(threshold) -method lsh -maxFiles $(maxAll) -inputPath $(INPUTANA) -outputPath $(OUT) -shingleLength $(shingleLength) -nShingles $(nShingles) -nRows $(nRows) -nBuckets $(nBuckets) -nBands $(nBands)


lsh: Runner.class
	@echo "Testing LSH"
	  time java Runner -threshold $(threshold) -method lsh -maxFiles $(maxFiles) -inputPath $(INPUT) -outputPath $(OUT) -shingleLength $(shingleLength) -nShingles $(nShingles) -nRows $(nRows) -nBuckets $(nBuckets) -nBands $(nBands)
	python performance.py $(OUT) $(maxFiles)


	
clean:
	rm *.class
