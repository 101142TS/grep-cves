# Extract apk files using *apktool*
import os
import sys
import subprocess
from basic_func import run_cmd_with_output
from basic_func import run_cmd
from basic_func import iterate_dir
import shutil

if __name__ == "__main__":
    print("python run_rules.py");
    print("Default: run ./scripts/xx.yml in ../source/xx.xx.xx/files/ and generate ../source/xx.xx.xx/results")


def get_dirs(root_dir = "../../source/"):
    d_list = os.listdir(root_dir)
    ret = []
    for d in d_list:
        path = os.path.join(root_dir,os.path.join(d, "results"))

        if os.path.exists(path):
            ret.append(path)
    return ret

dirs = get_dirs()

for d in dirs:
    if os.path.exists(d):
        shutil.rmtree(d)