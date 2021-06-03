# Extract apk files using *apktool*
import os
import sys
import subprocess
from basic_func import run_cmd

if __name__ == "__main__":
    print("python delete_apks.py");
    print("delete apks source");

def delete_single_apk(in_app, out_dir):
    if not os.path.exists(out_dir):
        os.mkdir(out_dir)
    
    out_dir = os.path.join(out_dir, "files")

    if not os.path.exists(out_dir):
        print("error: The file has been deleted")
        print("in_app: " + in_app)
        print("out_dir: " + out_dir)
        return

    cmd = 'rm -rf ' + out_dir
    run_cmd(cmd)

def delete_apks(in_dir, out_dir):
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

        delete_single_apk(in_f, out_f)
        cnt += 1


delete_apks("../../data/apks/", "../../source/")