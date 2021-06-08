# Extract apk files using *apktool*
import os
import sys
import subprocess
from basic_func import run_cmd_with_output
from basic_func import run_cmd
from basic_func import iterate_dir
from get_components import getComponentsTypes
import shutil

def filter_output(AndroidManifest, out_f):
    # AndroidManifest AndroidManifest.xml
    explicit, implicit = getComponentsTypes(AndroidManifest)
    # print(exported_components)


    with open(out_f, "r") as f, open(out_f + "bak", "w") as f2:
        for line in f:
            line = line.rstrip()
            if len(line.split(" ")) == 1 and line.endswith(".java"):
                components_name = line[line.index("sources") + 8 : -5].replace("/", ".")
                
                f2.write(line)
                if components_name in explicit:
                    f2.write(" explicit = True")
                
                if components_name in implicit:
                    f2.write(" implicit = True")

                f2.write("\n")  
            else:
                f2.write(line + "\n")
