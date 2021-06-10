# Extract apk files using *apktool*
import os
import sys
import subprocess
from basic_func import run_cmd_with_output
from basic_func import run_cmd
from basic_func import iterate_dir
from basic_func import semgrep_exclude
from filter_components import filter_output

def path_filter(x):
    return x.endswith(".yml")

def get_dirs(root_dir = "../../source/"):
    d_list = os.listdir(root_dir)
    ret = []
    for d in d_list:
        path = root_dir + d + "/" + "files"
        if os.path.exists(path):
            ret.append(path)
    return ret

rules = iterate_dir("./scripts", path_filter)
dirs = get_dirs()

def run_single_rule(r, d):
    # r : ./scripts/tests/rule6.yml
    # d : ../source/com.tencent.mtt/files
    AndroidManifest = os.path.join(d, "AndroidManifest.xml")

    results_dir = os.path.join(d[:-5], "results")

    if not os.path.exists(results_dir):
        os.mkdir(results_dir)
    

    
    yml_output = results_dir + r[1:] + ".output"

    info_dir = yml_output[:yml_output.rindex('/')]

    if not os.path.exists(info_dir):
        os.makedirs(info_dir)
    
    if not os.path.exists(yml_output):
        print("generate output : " + yml_output)
        cmd = 'semgrep' + ' -f ' + r + ' ' + d + ' > ' + yml_output + semgrep_exclude()
        run_cmd(cmd)

    yml_outputbak = yml_output

    if os.path.exists(AndroidManifest):
        print("filter components : " + AndroidManifest + " " + yml_outputbak)
        filter_output(AndroidManifest, yml_outputbak)
def run_rules(rules, dirs):
    for r in rules:
        for d in dirs:
            run_single_rule(r, d)

run_rules(rules, dirs)
