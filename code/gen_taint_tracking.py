# Extract apk files using *apktool*
import os
import sys
import subprocess
import shutil
import hashlib
from basic_func import run_cmd


# 参数一：config文件保存路径，参数二：函数名，参数三：调用的函数名
if len(sys.argv) == 4:
    with open(sys.argv[1], "w") as f:
        f.write("rules:\n  - id: taint-tracking\n    languages:\n      - java\n    message: 数据流跟踪\n    mode: taint\n    pattern-sinks:\n")

        f.write("      - pattern: $OBJ." + sys.argv[3] + "(...)\n")
        f.write("      - pattern: " + sys.argv[3] + "(...)\n")
        f.write("    pattern-sources:\n      - patterns:\n")
        f.write("        - pattern-inside: $RETURNTYPE " + sys.argv[2] + "(..., $TYPE $ARG, ...) {...}\n")
        f.write("        - pattern: $ARG\n    severity: WARNING\n")