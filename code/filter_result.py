# Extract apk files using *apktool*
import os
import sys
import subprocess
from basic_func import run_cmd

import sys
if __name__ == "__main__":
    print(len(sys.argv))

    with open(sys.argv[1], "r") as f:
        data = f.readlines()

        links_begin = -1
        S = set()
        for i in range(1, len(data)):

            if data[i - 1] == "************START************\n":
                S.add(data[i])
            # if data[i] == "************START************\n":
            #     links_begin = i
            
            # if sys.argv[2] in data[i]:
            #     links_begin = -1

            # if data[i] == "*************END*************\n":
            #     if not links_begin == -1:
            #         for j in range(links_begin, i + 1):
            #             print(data[j], end="")
            #     links_begin = -1
        
        for i in S:
            print(i, end="")
