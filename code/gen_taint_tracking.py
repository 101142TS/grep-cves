# Extract apk files using *apktool*
import os
import sys
import subprocess
import shutil
import hashlib
from basic_func import run_cmd


# 参数一：config文件保存路径，参数二：函数名，参数三：调用的函数名，参数四：新增的source个数，后跟具体的source

with open(sys.argv[1], "w") as f:
    f.write("rules:\n  - id: taint-tracking\n    languages:\n      - java\n    message: 数据流跟踪\n    severity: WARNING\n    mode: taint\n    pattern-sinks:\n")

    # sinks
    f.write("      - pattern: $OBJ." + sys.argv[3] + "(...)\n")
    f.write("      - pattern: " + sys.argv[3] + "(...)\n")


    # sources 默认值会计算函数的参数
    f.write("    pattern-sources:\n      - patterns:\n")
    f.write("        - pattern-inside: $RETURNTYPE " + sys.argv[2] + "(..., $TYPE $ARG, ...) {...}\n")
    f.write("        - pattern: $ARG\n")

    for i in range(int(sys.argv[4])):
        patt = sys.argv[5 + i]
        f.write("      - patterns:\n        - pattern-inside: $RETURNTYPE " + sys.argv[2] + "(...) {...}\n")
        f.write("        - pattern: " + patt + "\n")

        
