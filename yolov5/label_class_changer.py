import fileinput
import re
import sys
import os

dirs = os.scandir("annotation_salmon/")

for file in dirs:
    with fileinput.FileInput("annotation_salmon/" + file.name, inplace = True) as f:
        for line in f:
            l = list(line)
            l[0] = '1'
            l = "".join(l)
            print(l, end="\n")
