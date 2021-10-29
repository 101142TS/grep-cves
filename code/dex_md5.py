# Extract apk files using *apktool*
import os
import sys
import subprocess
import shutil
import hashlib
from basic_func import run_cmd

unuse = ["libcore", "android", "androidx", "butterknife", "okhttp3", "org/android", "com/google", "org/tensorflow", "org/apache", "org/intellij", "javax", "java", "kotlin", "llvm"]

def sort_dexs(in_app):
    print(in_app)

    dic = {}
    dexs = os.listdir(in_app)
    for dex in dexs:
        dex_path = os.path.join(in_app, dex)
        # print(dex_path)
        with open(dex_path, 'rb') as fp:
            data = fp.read()
        file_md5 = hashlib.md5(data).hexdigest()
        dic[file_md5] = dex_path

    print(len(dexs) - len(dic))
    # for dex in dexs:
    #     dex_path = os.path.join(in_app, dex)
    #     if dex_path not in dic.values():
    #         os.remove(dex_path)
def md5_dexs(in_dir):
    names = os.listdir(in_dir)

    cnt = 0
    for name in names:
        in_f = os.path.join(in_dir, name)

        sort_dexs(in_f)
        cnt += 1

if len(sys.argv) == 2 and sys.argv[1] == "dex":
    md5_dexs("../../data/dex/")