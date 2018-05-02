##
## Makefile
##
## Pieter Robberechts
## Feb 2018
## 

# Java compiler
JAVAC = javac
JVM = 1.8

# Directory for compiled binaries
# - trailing slash is important!
BIN = ./bin/

# Directory of source files
# - trailing slash is important!
SRC = ./src/

# Java compiler flags
JAVAFLAGS = -g -d $(BIN) -cp $(SRC) -target $(JVM)

# Creating a .class file
COMPILE = $(JAVAC) $(JAVAFLAGS)

EMPTY = 

JAVA_FILES = $(subst $(SRC), $(EMPTY), $(wildcard $(SRC)*.java))

# One of these should be the "main" class listed in Runfile
# CLASS_FILES = $(subst $(SRC), $(BIN), $(ALL_FILES:.java=.class))
CLASS_FILES = $(JAVA_FILES:.java=.class)

# The first target is the one that is executed when you invoke
# "make". 
all : $(addprefix $(BIN), $(CLASS_FILES))
	@echo $(CLASS_FILES)

# The line describing the action starts with <TAB>
$(BIN)%.class : $(SRC)%.java
	mkdir -p $(BIN)
	$(COMPILE) $<

getdata:
	rm -rf data
	wget --no-check-certificate https://people.cs.kuleuven.be/~toon.vancraenendonck/bdap/movielens.tgz
	tar -zxvf movielens.tgz
	mv movielens data
	rm movielens.tgz

matrix: $(BIN)PearsonsCorrelation.class
	@echo "Constructing Pearsons correlation matrix"
	time java -cp .:$(BIN) PearsonsCorrelation -trainingFile data/ra.train -outputFile data/ra.matrix

predict: $(BIN)MovieRunner.class
	@echo "Testing the prediction of movie ratings on the full dataset"
	time java -cp .:$(BIN) MovieRunner -trainingFile data/ra.train -testFile data/ra.test -matrixFile data/ra.matrix

clean : 
	rm -rf $(BIN)*

