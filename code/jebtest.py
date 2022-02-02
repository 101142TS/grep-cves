# -*- coding: utf-8 -*-
from com.pnfsoftware.jeb.client.api import IScript, IClientContext
from com.pnfsoftware.jeb.core import IRuntimeProject, ILiveArtifact, IEnginesContext
from com.pnfsoftware.jeb.core.units import IUnit
from com.pnfsoftware.jeb.core.units.code.android import IDexUnit, IDexDecompilerUnit
from com.pnfsoftware.jeb.core.units.code.android.dex import IDexClass, IDexField, IDexMethod
from com.pnfsoftware.jeb.client.api import IClientContext
from com.pnfsoftware.jeb.core import IRuntimeProject
from com.pnfsoftware.jeb.core.actions import ActionXrefsData, Actions, ActionContext
from com.pnfsoftware.jeb.core.units import IUnit
from com.pnfsoftware.jeb.core.units.code.android import IDexUnit
from com.pnfsoftware.jeb.core.units.code.android.dex import IDexMethod, IDexClass
import sys
import Queue
import subprocess
import os
import ast
import re

def run_cmd(cmd):
    res = subprocess.call(cmd)
    return res

def run_cmd_with_output(cmd):
    try:
        res = subprocess.check_output(cmd)
        res = res.rstrip("\n")
    except subprocess.CalledProcessError as e:
        res = ""
    return res

def MyPrint(words, output):
    if output == "":
        print(words)
    else:
        output.write(words)
        output.write("\n")
# 对一个方法，得到所有调用它的方法，已去重
def GetMethodXref(dex_unit, method):
    # jeb的诡异bug, onCreate, onResume, onNewIntent, onActivityResult, onDestroy 

    # jeb存在bug，一些函数的父函数错误被识别
    if method.getName() == "onCreate" or method.getName() == "onResume" or method.getName() == "onNewIntent" or method.getName() == "onActivityResult" or method.getName() == "onDestroy":
        return []

    return_methods = []
    

    methods_set = set()

    actionXrefsData = ActionXrefsData()
    actionContext = ActionContext(dex_unit, Actions.QUERY_XREFS, method.getItemId(), None)
    if dex_unit.prepareExecution(actionContext,actionXrefsData):
        for xref_addr in actionXrefsData.getAddresses():
            method = dex_unit.getMethod(xref_addr[:xref_addr.index("+")])

            if method.getIndex() not in methods_set:
                methods_set.add(method.getIndex())
                return_methods.append(method)
    
    return return_methods

# 对一些方法，得到所有调用它们的方法，已去重
def GetMethodsXref(dex_unit, methods):
    return_methods = []
    methods_set = set()

    for method in methods:
        tmps = GetMethodXref(dex_unit, method)
        
        for tmp in tmps:
            if tmp.getIndex() not in methods_set:
                methods_set.add(tmp.getIndex())
                return_methods.append(tmp)
        
    return return_methods
# ope == 0      name只是方法名
# ope == 1      name是全称
# ope == 2      name是class_list，里面的每一个都是类名
# 返回list
def GetMethods(dex_unit, name, ope):

    return_methods = []
    if ope == 0:
        methods = dex_unit.getMethods()

        for method in methods:
            if method.getName(True) == name:
                return_methods.append(method)
    elif ope == 1:
        method = dex_unit.getMethod(name)
        if not method == None:
            return_methods.append(method)
    elif ope == 2:
        for class_name in name:
            klass = dex_unit.getClass("L" + class_name.replace(".", "/") + ";")
            if isinstance(klass, IDexClass):
                return_methods = return_methods + klass.getMethods()

    return return_methods
def RegexMatchingMethods(pattern, methods):
    ret = []
    for method in methods:
        if re.search(pattern, method.getName()):
            ret.append(method)
    return ret
def ReturnMethods(dex_unit, manifest, ope, name, root_path):
    if ope < 3:

        ExportedComponents = manifest.replace("AndroidManifest.xml", "ExportedComponents.txt")
        if not os.path.exists(ExportedComponents):
            return []

        if ope == 0:
            with open(ExportedComponents, "r") as f:
                line1 = f.readline().rstrip()

                class_list = ast.literal_eval(line1)
        if ope == 1:
            with open(ExportedComponents, "r") as f:
                line1 = f.readline().rstrip()
                line2 = f.readline().rstrip()
                class_list = ast.literal_eval(line2)
        if ope == 2:
            with open(ExportedComponents, "r") as f:
                line1 = f.readline().rstrip()
                line2 = f.readline().rstrip()
                class_list = ast.literal_eval(line1) + ast.literal_eval(line2)

        mm = GetMethods(dex_unit, class_list, 2)

        return RegexMatchingMethods(name, mm)
    if ope == 3:
        return GetMethods(dex_unit, name, 0)
    
    if ope == 4:
        return GetMethods(dex_unit, name, 1)
    
    if ope == 5:
        dexClass = dex_unit.getClass(name);                              
        if not isinstance(dexClass,IDexClass):
            return []
            
        ret = []

        cnt = 0
        for method in dexClass.getMethods():
            assert isinstance(method, IDexMethod)

            ret.append(method)
            cnt = cnt + 1
        return ret
        
    if ope == 6:
        cmd = ['grep', '-lr', name, root_path]

        output = run_cmd_with_output(cmd)
        ret_value = []
        if not output == "":
            ret_value = output.split("\n")

        ret = []
        for file_name in ret_value:
            pos = file_name.rindex("sources")
            class_name = "L" + file_name[pos + 8 : -5] + ";"
            
            ret = ret + ReturnMethods(dex_unit, manifest, 5, class_name, root_path)
        return ret
    
    if ope == 7:
        return RegexMatchingMethods(name, dex_unit.getMethods())

# 对每一个sink, BFS寻找其是否可到sources
def FindPath(dex_unit, sources, sinks):
    #sinks可能等于sources

    target = set()
    for source in sources:
        target.add(source.getIndex())

    real_sinks = []
    for sink in sinks:
        Q = Queue.Queue()
        vis = set()
        
        Q.put(sink)
        vis.add(sink.getIndex())

        while not Q.empty():
            now_method = Q.get()
            if now_method.getIndex() in target:
                real_sinks.append(sink)
                break

            pre_methods = GetMethodXref(dex_unit, now_method)

            for pre in pre_methods:
                if pre.getIndex() not in vis:
                    vis.add(pre.getIndex())
                    Q.put(pre)
    
    return real_sinks
def GetFileName(root_path, method):
    sign = method.getSignature(True)
    class_name = sign[1 : sign.index(";")]
    return root_path + "/" + class_name + ".java"

def DFS(dex_unit, root_path, taint_file, taint_sources, now_method, len, links, vis, maxlen, output, yml_st, yml_ed):
    # print("DEBUG INGO : ##########################")
    # for i in range(len - 1, -1, -1):
    #         print("[*] " + str(links[i]))

    if (now_method.getIndex() in vis and vis[now_method.getIndex()] == "Target"):
        # 头部特殊污点跟踪
        
        if not (taint_file == "no_taint"):
            from_name = links[len - 1].getName()
            to_name = links[len - 2].getName()

            # 获取links[len - 1]函数所在的文件名
            java_file = GetFileName(root_path, links[len - 1])
            
            if SemgrepFile(dex_unit, java_file, yml_st) == False:
                return

            if os.path.exists(java_file):
                # 生成污点文件
                cmd = ['python', './gen_taint_tracking.py', taint_file, from_name, to_name]

                cmd = cmd + taint_sources.split(' ')
                run_cmd(cmd)
    
                # 开始扫描
                cmd = ['semgrep', '-f', taint_file, java_file, '--error', '-q']
                if not run_cmd(cmd) == 1:
                    return

        # TODO : 对links做一遍semgrep
        
        MyPrint("************START************", output)
        for i in range(len - 1, -1, -1):
            MyPrint("[*] " + links[i].toString().encode('utf-8'), output)
        
        MyPrint("*************END*************", output)
        return
    
    if len == maxlen:
        return

    vis[now_method.getIndex()] = "True"
    pre_methods = GetMethodXref(dex_unit, now_method)
    for pre in pre_methods:
        if pre.getIndex() in vis and vis[pre.getIndex()] == "True":
            continue

        # 污点跟踪
        if not (pre.getIndex() in vis and vis[pre.getIndex()] == "Target") and (not (taint_file == "no_taint")):
            from_name = pre.getName()
            to_name = now_method.getName()
            # 获取pre函数所在的文件名
            java_file = GetFileName(root_path, pre)
            
            if len == 1:
                if SemgrepFile(dex_unit, java_file, yml_ed) == False:
                    continue

            if os.path.exists(java_file):
                # 生成污点跟踪文件
                cmd = ['python', './gen_taint_tracking.py', taint_file, from_name, to_name, '0']
                run_cmd(cmd)

                cmd = ['semgrep', '-f', taint_file, java_file, '--error', '-q']
                if not run_cmd(cmd) == 1:
                    continue

        links[len] = pre
        DFS(dex_unit, root_path, taint_file, taint_sources, pre, len + 1, links, vis, maxlen, output, yml_st, yml_ed)
    
    vis[now_method.getIndex()] = "False"

def GetPath(dex_unit, root_path, taint_file, taint_sources, sources, sinks, maxlen, output, yml_st, yml_ed):
    real_sinks = FindPath(dex_unit, sources, sinks)

    if real_sinks == []:
        return

    vis = dict()
    for source in sources:
        vis[source.getIndex()] = "Target"

    for sink in sinks:
        links = [0] * maxlen
        links[0] = sink
        DFS(dex_unit, root_path, taint_file, taint_sources, sink, 1, links, vis, maxlen, output, yml_st, yml_ed)

    
def SemgrepFile(dex_unit, java_file, yml_file):
    if yml_file == "default":
        return True

    if os.path.exists(java_file):
        # 直接用semgrep 在文件中进行匹配，有可能出现匹配的结果不在所要的方法内
        cmd = ['semgrep', '-f', yml_file, java_file, '--error', '-q']

        # print(cmd)

        output = run_cmd(cmd)
        
        # print(output)
        if output == 1:
            return True

    return False
class jebtest(IScript):
    # method ope name
    def run(self, ctx):
        unit = ctx.open("/mnt/RAID/users_data/caijiajin/semgrep/data-selfmade/dex/com.sogou.novel/cookie_5406748.dex");                                    assert isinstance(unit,IUnit)

        prj = ctx.getMainProject();                                     assert isinstance(prj,IRuntimeProject)

        dex_unit = prj.findUnit(IDexUnit);                               assert isinstance(dex_unit,IDexUnit)

        mm = ReturnMethods(dex_unit, "/mnt/RAID/users_data/caijiajin/semgrep/source-selfmade/com.sogou.novel/files/AndroidManifest.xml", 3, "getUrl", "/mnt/RAID/users_data/caijiajin/semgrep/source-selfmade/com.sogou.novel/files/AndroidManifest.xml")

        for m in mm:
            print(m)
        # method = dex_unit.getMethods()

        # for i in method:
        #     print(i)
        # print(method)

        # methods = ReturnMethods(dex_unit, "", 6, "@JavascriptInterface", "/mnt/RAID/users_data/caijiajin/semgrep/source-tencent/com.tencent.news_6560")

        # SemgrepMethods(dex_unit, methods, "/mnt/RAID/users_data/caijiajin/semgrep/source-tencent/com.tencent.news_6560/files/sources", "1")


        # print(dex_unit.getClass("Lcom/tencent/news/ui/PushNewsDetailActivity;"))

        # print(dex_unit.getMethod(24799))
        # print(GetMethodXref(dex_unit, dex_unit.getMethod(24799)))
        # sources = ReturnMethods(dex_unit, "/mnt/RAID/users_data/caijiajin/semgrep/source/87701v15.1.6821.4825/files/AndroidManifest.xml", 0, "exported", "/mnt/RAID/users_data/caijiajin/semgrep/source/c87701v15.1.6821.4825")

        # sinks = ReturnMethods(dex_unit, "/mnt/RAID/users_data/caijiajin/semgrep/source/87701v15.1.6821.4825/files/AndroidManifest.xml", 3, "setResult", "/mnt/RAID/users_data/caijiajin/semgrep/source/c87701v15.1.6821.4825")

        # real_sources = SemgrepMethods(dex_unit, sources, "/mnt/RAID/users_data/caijiajin/semgrep/source/c87701v15.1.6821.4825", "/mnt/RAID/users_data/caijiajin/semgrep/grep-cves/rules/setResult/setResult.yml")
        # real_sinks = SemgrepMethods(dex_unit, sinks, "/mnt/RAID/users_data/caijiajin/semgrep/source/c87701v15.1.6821.4825", "/mnt/RAID/users_data/caijiajin/semgrep/grep-cves/rules/setResult/setResult.yml")


        # GetPath(dex_unit, sources, sinks, 4, "")