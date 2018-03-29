# param 1: file name 
# param 2-N: different block sizes

import matplotlib
matplotlib.use('Agg')

import sys
import matplotlib.pyplot as plt
import subprocess as sp
import numpy as np

### args: the arguments we will use to call the C executable
### This function runs the matrix multiplication based on the setting
### provided and parses its output
### Returns the execution time

def runNparse(args):
    out = sp.check_output(args)
    return float(out.split("\n")[-2].split(":")[-1])


executable = "./matrix_multiplication"
file_name = sys.argv[1]
block_sizes = map(lambda block_size: int(block_size), sys.argv[2:])

print "This script executes matrix multiplication for the matrixes in file '"+file_name+"' using the naive strategy and the blocked strategy for block sizes: "+", ".join(sys.argv[2:])
print "The result of this script is a graph 'graph.png' in the current directory that shows the results with matrix size on the x-axis and time on the y-axis. Create one line for each block size and one line for the naive strategy."

# Initialize the data structures for the results

results = dict()
results["naive"] = []
for bs in block_sizes: results["blocked-" + str(bs)] = []
ys=[]

# Parse the inputs' file and run the specified configurations
with open(file_name,"r") as f:
    for line in f:
        args = line.rstrip().split(" ")
        ys.append(args[0])

        method="naive"
        results[method].append(runNparse([executable, method] + args))

        for bs in block_sizes: 
           if(bs > int(args[0])): break 
           method = "blocked"
           results[method + "-" + str(bs)].append(runNparse([executable, method] + args + [str(bs)]))
	


f.close()


# visualie the results using matplotlib

fig = plt.figure()

legends = []

for k,v in results.iteritems():
   plt.plot(ys[(-1*len(v)):],v)
   legends.append(k)

plt.legend(legends,loc=2)
plt.xlabel("Size of input matrices")
plt.ylabel("Running time (seconds)")
plt.title("Block Matrix Multiplication Performance Curves")

plt.savefig("graph.png", bbox_inches='tight')


