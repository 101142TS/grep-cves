# Extract apk files using *apktool*
import os
import sys
import subprocess
from basic_func import run_cmd_with_output
from basic_func import run_cmd
from basic_func import iterate_dir

if __name__ == "__main__":
    print("python run_rules.py");
    print("Default: run ./scripts/xx.yml in ../../source/xx.xx.xx/files/ and generate ../../source/xx.xx.xx/results")



def path_filter(x):
    return x.endswith(".yml")

def get_dirs(root_dir = "../../source/"):
    d_list = os.listdir(root_dir)
    ret = []
    for d in d_list:
        path = os.path.join(root_dir,os.path.join(d, "files"))

        if os.path.exists(path):
            ret.append(path)
    return ret

rules = iterate_dir("./scripts", path_filter)
dirs = get_dirs()

def run_single_rule(r, d):
    # r : ./scripts/tests/rule6.yml
    # d : ../source/com.tencent.mtt/files

    root_dir = d[:-5]
    root_dir = os.path.join(root_dir, "results")

    if not os.path.exists(root_dir):
        os.mkdir(root_dir)
    

    
    output = root_dir + r[1:] + ".output"

    info_dir = output[:output.rindex('/')]

    if not os.path.exists(info_dir):
        os.makedirs(info_dir)
    cmd = 'semgrep' + ' -f ' + r + ' ' + d + ' > ' + output

    run_cmd(cmd)

def run_rules(rules, dirs):
    for r in rules:
        for d in dirs:
            run_single_rule(r, d)

run_rules(rules, dirs)