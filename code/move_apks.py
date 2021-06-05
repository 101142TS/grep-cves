import os
import sys
import subprocess
from basic_func import run_cmd_with_output
from basic_func import run_cmd
from basic_func import iterate_dir
import shutil

def no_filter(x):
    return True


apks_2019 = iterate_dir("/mnt/RAID/users_data/caijiajin/Desktop/apks/2019", no_filter)
#print(ret)


def readfromtestlist():
    ret = []
    with open("/mnt/RAID/users_data/caijiajin/Desktop/tools/testlist.txt", "r") as f:
        for line in f:
            line = line.rstrip()
            if line[0] == '[' and line[-1] == ']':
                lst = line[1:-1].replace('\'', "").split(', ')

                ret = ret + lst       
    return ret

apks = readfromtestlist()

for apk in apks_2019:
    if not apk in apks:
        cmd = 'cp ' + apk + ' ' + '/mnt/RAID/users_data/caijiajin/semgrep/grep-cves/data/apks' + apk[apk.rindex('/'):] + '.apk'
        run_cmd(cmd)
#print(ret)