import sys
import pandas as pd

output = sys.argv[1] 
maxfiles = sys.argv[2] 
bf = pd.read_csv(output + "bf" + str(maxfiles), index_col = None, header = None, sep = "\t")
lsh = pd.read_csv(output + "lsh" + str(maxfiles), index_col = None, header = None, sep = "\t")

bfset = set(zip(bf[0], bf[1]))
lshset = set(zip(lsh[0], lsh[1]))

TP = len(bfset.intersection(lshset))
FP = len(lshset - bfset)
FN = len(bfset - lshset)
TN = 0

precision = float(TP)/(TP + FP)
recall = float(TP)/(TP + FN)

print("MaxFiles: " + str(maxfiles))
print("")
print("----------------------------------")
print("|    TP: " + str(TP) + "\t | FP: " + str(FP) + "\t |")
print("|    FN: " + str(FN) + "\t | TN: " + str(TN) + "\t |")
print("----------------------------------")

print("precision: " + str(precision))
print("recall: " + str(recall))
print("F1-score: " + str(2*precision*recall/(precision + recall)))


