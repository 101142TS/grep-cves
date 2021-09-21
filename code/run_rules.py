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
    return x.endswith(".cfg")

def get_dirs(root_dir = "../../source/"):
    d_list = os.listdir(root_dir)
    ret = []
    for d in d_list:
        path = root_dir + d + "/" + "files" + "/AndroidManifest.xml"
        if os.path.exists(path):
            ret.append(root_dir + d + "/" + "files")
    return ret

rules = iterate_dir("../rules", path_filter)
dirs = get_dirs()

def generate_single_rule(r, d):
    # r : ../rules/loadUrl/links.cfg
    # d : ../../source/com.tencent.android.duoduo_327/files

    output_dir = "../../results/" + r[9:-9] + d[13:-5]
    # 结果保存点  ../../results/
    # print(r)
    # print(d)
    # print(output_dir)

    if not os.path.exists(output_dir):
        os.makedirs(output_dir)

    config_file = output_dir + "config.txt"
    with open(config_file, 'w') as output:
        with open(r, "r") as input:
            # apk所在路径
            output.write("../../data/apks/" + d[13:-6] + ".apk" + "\n")
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

    return "../../data/apks/" + d[13:-6] + ".apk", config_file
def run_rules(rules, dirs):
    for d in dirs:
        configs = []
        apk_path = ""
        for r in rules:
            apk_path, config = generate_single_rule(r, d)
            configs.append(config)

        print(configs)
        with open("./tmp.txt", "w") as f:
            for config in configs:
                f.write(config + "\n")
        cmd = "java -jar /mnt/RAID/users_data/caijiajin/Desktop/jeb/bin/app/jeb.jar --srv2 --script=./jeb.py -- ./tmp.txt " + apk_path
        # print(cmd)
        run_cmd(cmd)
run_rules(rules, dirs)
