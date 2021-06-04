# Extract apk files using *apktool*
import os
import sys
import subprocess
from basic_func import run_cmd_with_output
from basic_func import run_cmd
from basic_func import iterate_dir
import shutil

if not len(sys.argv) == 3:
    print("python filter_output.py");
    print("Default: find exported components in $1 from $2")
    exit()

def filter_output(in_f, out_f):

    exported_components = []

    with open(out_f, "r") as f:
        for line in f:
            line = line.rstrip()
            if len(line) > 1:
                exported_components.append(line)

    # print(exported_components)

    with open(in_f, "r") as f, open(in_f + "bak", "w") as f2:
        for line in f:
            line = line.rstrip()
            if len(line.split(" ")) == 1 and line.endswith(".java"):
                components_name = line[line.index("sources") + 8 : -5].replace("/", ".")

                if components_name in exported_components:
                    f2.write(line + " " + " exported = True" + "\n")
                else:
                    f2.write(line + " " + " exported = False" + "\n")
            else:
                f2.write(line + "\n")

filter_output(sys.argv[1], sys.argv[2])