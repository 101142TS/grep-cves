# Extract apk files using *apktool*
import os
import sys
import subprocess
from multiprocessing import Pool
from concurrent.futures import ThreadPoolExecutor
from basic_func import run_cmd_with_output
from basic_func import run_cmd
from basic_func import iterate_dir
from basic_func import semgrep_exclude
# from filter_components import filter_output

def path_filter_cfg(x):
    return x.endswith(".cfg")

def path_filter_dex(x):
    return x.endswith(".dex")

def generate_single_rule(r, dex, datadirs, sourcedirs, resultdirs):

    # r : ../rules-xxxxx/setResult/1/links.cfg
    # dex : ../../data-xxxxx/dex/me.ele/cookie_5376.dex
    pkgname = dex[dex[:dex.rfind('/')].rfind("/") + 1: dex.rfind('/')]
    d = sourcedirs + pkgname + "/files"
    # me.ele
    # ../../source-xxxxx/me.ele/files

    output_dir = resultdirs + r[r[4:].find("/") + 5:-9] + pkgname + dex[dex.rfind('/') : -4] + "/"
    # ../../results-xxxxx/webview/setAllowFileAccessFromFileURLs/me.ele/classes2/

    # print(r)
    # print(d)
    # print(output_dir)

    if not os.path.exists(output_dir):
        os.makedirs(output_dir)

    config_file = output_dir + "config.txt"
    with open(config_file, 'w') as output:
        with open(r, "r") as input:
            # apk所在路径
            output.write(datadirs + "apks/" + pkgname + ".apk" + "\n")
            # manifest所在路径
            output.write(d + "/AndroidManifest.xml" + "\n")
            # jadx反编译后的 java文件的根路径
            output.write(d + "/sources" + "\n")

            # 起点函数，  函数的类型， 函数名， 匹配时的规则文件
            st = input.readline().split()
            
            if not st[2] == "default":
                st[2] = r[:-9] + st[2]

            output.write(st[0] + " " + st[1] + " " + st[2] + "\n")

            # 终点函数，  函数的类型， 函数名， 匹配时的规则文件
            ed = input.readline().split()
            if not ed[2] == "default":
                ed[2] = r[:-9] + ed[2]
            
            output.write(ed[0] + " " + ed[1] + " " + ed[2] + "\n")

            # 切片时的中间文件所在路径
            output.write(config_file[:config_file.rindex("/")] + "/tmp.txt" + "\n")

            # 最后结果保存路径
            output.write(config_file[:config_file.rindex("/")] + "/result.txt" + "\n")

            # 函数链的长度
            output.write(input.readline())

            # 切片时的污点跟踪规则文件所在路径及生成头部污点跟踪配置文件
            if int((input.readline()).rstrip()) == 1:

                taint_file = config_file[:config_file.rindex("/")] + "/taint"
                output.write(taint_file + "\n")
                output.write(r[:-9] + "taint_source")
            else:
                output.write("no_taint" + "\n")
                output.write("no_taint" + "\n")

    return datadirs + "apks/" + pkgname + ".apk", config_file
def run_rules(rules, dirs, processes_num, datadirs, sourcedirs, resultdirs):

    inputs = []
    for i in range(0, len(dirs)):
        print("number %d of %d " % (i, len(dirs)))
        d = dirs[i]
        configs = []
        apk_path = ""
        for r in rules:
            print(d, r)
            apk_path, config = generate_single_rule(r, d, datadirs, sourcedirs, resultdirs)
            configs.append(config)

        print(configs)

        config_file = d.replace(".dex", ".config")
        with open(config_file, "w") as f:
            for config in configs:
                f.write(config + "\n")
        cmd = "java -jar /mnt/RAID/users_data/caijiajin/Desktop/jeb/bin/app/jeb.jar --srv2 --script=./jeb.py -- " + config_file + " " + d
        # print(cmd)
        # run_cmd(cmd)
        inputs.append(cmd)

    pool = Pool(processes_num)
    for cmd in inputs:
        pool.apply_async(run_cmd, (cmd, ))

    pool.close()
    pool.join()


rules = iterate_dir("../rules", path_filter_cfg)

if not len(sys.argv) == 2:
    datadirs = "../../data/"
    sourcedirs = "../../source/"
    resultdirs = "../../results/"
else:
    datadirs = "../../data-" + sys.argv[1] + "/"
    sourcedirs = "../../source-" + sys.argv[1] + "/"
    resultdirs = "../../results-" + sys.argv[1] + "/"

dirs = iterate_dir(datadirs + "dex", path_filter_dex)
run_rules(rules, dirs, 4, datadirs, sourcedirs, resultdirs)
