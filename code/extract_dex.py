# Extract apk files using *apktool*
import os
import sys
import subprocess
import shutil
import time
# from basic_func import run_cmd, run_cmd_with_output
def run_cmd_with_output(cmd):
    try:
        res = subprocess.check_output(cmd)
    except subprocess.CalledProcessError as e:
        res = ""
    return res

def decompose_single_apk(in_app, out_dir, package_name):
    print(in_app, out_dir, package_name)
    if os.path.exists(out_dir):
        return
    cmd = ['adb', 'install', in_app]
    print(cmd)
    run_cmd_with_output(cmd)

    cmd = ['adb', 'shell', 'am', 'start', '-n', 'top.niunaijun.blackdexa32/top.niunaijun.blackdex.view.main.MainActivity', '--es', 'unpack', package_name]
    print(cmd)
    run_cmd_with_output(cmd)

    time.sleep(120)

    cmd = ['adb', 'pull', '/storage/emulated/0/Android/data/top.niunaijun.blackdexa32/dump/' + package_name, out_dir]
    print(cmd)
    run_cmd_with_output(cmd)
    
    cmd = ['adb', 'uninstall', package_name]
    print(cmd)
    run_cmd_with_output(cmd)


    cmd = ['adb', 'shell', 'rm', '-rf', '/storage/emulated/0/Android/data/top.niunaijun.blackdexa32/dump/' + package_name]
    print(cmd)
    run_cmd_with_output(cmd)
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
        package_name = apk[:-4]
        out_f = out_dir + package_name
        decompose_single_apk(in_f, out_f, package_name)
        cnt += 1


decompose_apks("../../data/apks/", "../../data/dex/")
# decompose_single_apk("../../data/apks/com.banshenghuo.mobile.apk", "../../data/dex/com.banshenghuo.mobile", "com.banshenghuo.mobile")