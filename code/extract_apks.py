# Extract apk files using *apktool*
import os
import sys
import subprocess
from basic_func import run_cmd

if __name__ == "__main__":
    print("python extract_apks.py");
    print("extract ../../data/apks/xxx.apk to ../../source/xxx");

def decompose_single_apk(in_app, out_dir, with_res=True, with_src=True):
    if not os.path.exists(out_dir):
        os.mkdir(out_dir)
    
    out_dir = os.path.join(out_dir, "files")

    if os.path.exists(out_dir):
        print("error: The file has been decomposed, please delete the corresponding directory")
        print("in_app: " + in_app)
        print("out_dir: " + out_dir)
        return

    os.mkdir(out_dir)

    cmd = 'jadx ' + in_app + ' -d ' + out_dir + ' --quiet ';

    if not with_res:
        cmd += ' --no-res'
    if not with_src:
        cmd += ' --no-src'
    print(cmd)
    run_cmd(cmd)

    run_cmd("mv " + out_dir + "/resources/AndroidManifest.xml" + " " + out_dir)
    run_cmd("rm -rf " + out_dir + "/resources/")

    run_cmd("python ./get_components.py" + " " + out_dir + "/AndroidManifest.xml" + " > " + out_dir + "/ExportedComponents.txt")
def decompose_apks(in_dir, out_dir, with_res=True, with_src=True):
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

        decompose_single_apk(in_f, out_f, with_res, with_src)
        cnt += 1


decompose_apks("../../data/apks/", "../../source/")
# decompose_apks("../../data-test/apks/", "../../source-test/")