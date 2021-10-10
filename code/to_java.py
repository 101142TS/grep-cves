# Extract apk files using *apktool*
import os
import sys
import subprocess
import shutil
from basic_func import run_cmd

unuse = ["libcore", "android", "androidx", "butterknife", "okhttp3", "org/android", "com/google", "org/tensorflow", "org/apache", "org/intellij", "javax", "java", "kotlin", "llvm"]

def decompose_single_apk(in_app, out_dir, with_res=True, with_src=True):
    if not os.path.exists(out_dir):
        os.mkdir(out_dir)
    
    out_dir = os.path.join(out_dir, "files")

    if os.path.exists(out_dir):
        print("error: The file has been decomposed, please delete the corresponding directory")
        print("in_app: " + in_app)
        print("out_dir: " + out_dir)
    else:
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

    if os.path.exists(out_dir + "/AndroidManifest.xml"):
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


def decompose_single_dex(in_app, out_dir):
    print(in_app, out_dir)
    if not os.path.exists(out_dir):
        os.mkdir(out_dir)
    out_dir = os.path.join(out_dir, "files")
    print(out_dir)

    if os.path.exists(out_dir):
        print("error: The file has been decomposed, please delete the corresponding directory")
        print("in_app: " + in_app)
        print("out_dir: " + out_dir)
    else:
        dexs = os.listdir(in_app)
        for dex in dexs:
            dex_path = os.path.join(in_app, dex)
            print(dex_path)
            cmd = 'jadx ' + dex_path + ' -d ' + out_dir + ' --quiet ';
            print(cmd)
            run_cmd(cmd)
        

        cmd = 'jadx ' + in_app.replace("dex", "apks") + ".apk" + ' -d ' + out_dir + ' --quiet --no-src';
        print(cmd)
        run_cmd(cmd)

        run_cmd("mv " + out_dir + "/resources/AndroidManifest.xml" + " " + out_dir)
        run_cmd("rm -rf " + out_dir + "/resources/")

    #删除无用目录

    for name in unuse:
        rmdir = out_dir + "/sources/" + name
        if os.path.exists(rmdir):
            shutil.rmtree(rmdir)

    if os.path.exists(out_dir + "/AndroidManifest.xml"):
        run_cmd("python ./get_components.py" + " " + out_dir + "/AndroidManifest.xml" + " > " + out_dir + "/ExportedComponents.txt")

def decompose_dexs(in_dir, out_dir):
    names = os.listdir(in_dir)

    if not os.path.exists(out_dir):
        os.mkdir(out_dir)

    cnt = 0
    for name in names:
        print(name)
        
        in_f = os.path.join(in_dir, name)
        out_f = os.path.join(out_dir, name)

        decompose_single_dex(in_f, out_f)
        cnt += 1

if len(sys.argv) == 2 and sys.argv[1] == "apk":
    decompose_apks("../../data/apks/", "../../source/")
elif len(sys.argv) == 2 and sys.argv[1] == "dex":
    decompose_dexs("../../data/dex/", "../../source/")