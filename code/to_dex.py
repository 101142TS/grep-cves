# Extract apk files using *apktool*
import os
import sys
import subprocess
import shutil
from basic_func import run_cmd

def path_filter(x):
    return x.endswith(".dex")

def get_dirs(root_dir = "../../source/"):
    d_list = os.listdir(root_dir)
    ret = []
    for d in d_list:
        path = root_dir + d
        if path_filter(path):
            ret.append(path)
    return ret

def decompose_single_apk(in_app, out_dir, tmp_dir):
    if os.path.exists(tmp_dir):
        shutil.rmtree(tmp_dir)

    if os.path.exists(out_dir):
        return
    
    os.mkdir(out_dir)
    cmd = 'apktool d ' + in_app + ' -so ' + tmp_dir
    run_cmd(cmd)

    dexs = get_dirs(tmp_dir)
    for f in dexs:
        shutil.copy(f, out_dir)
def decompose_apks(in_dir, out_dir, tmp_dir):
    apks = os.listdir(in_dir)

    if not os.path.exists(out_dir):
        os.mkdir(out_dir)

    cnt = 0
    for apk in apks:
        print(apk)

        if not apk.endswith(".apk"):
            print("warning: 奇怪的文件出现了 " + apk)
            continue
        
        in_f = os.path.join(in_dir, apk)
        out_f = os.path.join(out_dir, apk[:-4])

        print(in_f, out_f)
        decompose_single_apk(in_f, out_f, tmp_dir)
        cnt += 1


decompose_apks("../../data/apks/", "../../data/dex/", "../../data/dex/tmp/")