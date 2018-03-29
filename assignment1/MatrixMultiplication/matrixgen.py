import sys
import numpy as np

f = open("indices.txt",'w')

sizes=[2**i for i in range(3,11)]

for n in sizes:
    filenameA = "m" + str(n) + "by" + str(n) + "A.csv"
    ArrayA = np.random.randint(10, size=(n, n)) 
    np.savetxt(filenameA, ArrayA, delimiter=" ")

    filenameB = "m" + str(n) + "by" + str(n) + "B.csv"
    ArrayB = np.random.randint(10, size=(n, n)) 
    np.savetxt(filenameB, ArrayB, delimiter=" ")
    
    f.write(str(n) + " " +  filenameA + " " + filenameB + "\n")

f.close()


