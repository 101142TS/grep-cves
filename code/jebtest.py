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
    return_methods = []
    methods_set = set()

    actionXrefsData = ActionXrefsData()
    actionContext = ActionContext(dex_unit, Actions.QUERY_XREFS, method.getItemId(), None)
    if dex_unit.prepareExecution(actionContext,actionXrefsData):
        for xref_addr in actionXrefsData.getAddresses():
            print(xref_addr)
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

    print(name)
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
            print(class_name)
            klass = dex_unit.getClass("L" + class_name.replace(".", "/") + ";")
            return_methods = return_methods + klass.getMethods()

    return return_methods

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

        return GetMethods(dex_unit, class_list, 2)

    if ope == 3:
        return GetMethods(dex_unit, name, 0)
    
    if ope == 4:
        return GetMethods(dex_unit, name, 1)
    
    if ope == 5:
        dexClass = dex_unit.getClass(name);                              assert isinstance(dexClass,IDexClass)
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

# 对每一个sink, BFS寻找其是否可到sources
def FindPath(dex_unit, sources, sinks):
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
def DFS(dex_unit, now_method, len, links, vis, maxlen, output):
    # print("DEBUG INGO : ##########################")
    # for i in range(len - 1, -1, -1):
    #         print("[*] " + str(links[i]))

    if (now_method.getIndex() in vis and vis[now_method.getIndex()] == "Target"):
        MyPrint("************START************", output)
        for i in range(len - 1, -1, -1):
            MyPrint("[*] " + links[i].toString().encode('utf-8'), output)
        
        MyPrint("*************END*************", output)
        return
    
    if len == maxlen:
        return

    vis[now_method.getIndex()] = "True"

    
    pre_methods = GetMethodXref(dex_unit, now_method)

    print("************NOW METHOD************", now_method)
    for pre in pre_methods:
        print(pre)
    for pre in pre_methods:
        if pre.getIndex() in vis and vis[pre.getIndex()] == "True":
            continue

        links[len] = pre
        DFS(dex_unit, pre, len + 1, links, vis, maxlen, output)
    
    vis[now_method.getIndex()] = "False"

def GetPath(dex_unit, sources, sinks, maxlen, output):
    real_sinks = FindPath(dex_unit, sources, sinks)

    if real_sinks == []:
        return

    vis = dict()
    for source in sources:
        vis[source.getIndex()] = "Target"

    for sink in sinks:
        links = [0] * maxlen
        links[0] = sink
        DFS(dex_unit, sink, 1, links, vis, maxlen, output)

    
def SemgrepMethods(dex_unit, methods, root_path, yml_file):
    if yml_file == "default":
        return methods
    
    res = []

    for method in methods:
        # method.getName(True)
        sign = method.getSignature(True)
        class_name = sign[1:sign.index(";")]

        java_file = root_path + "/" + class_name + ".java"

        if os.path.exists(java_file):
            # 直接用semgrep 在文件中进行匹配，有可能出现匹配的结果不在所要的方法内

            cmd = ['semgrep', '-f', yml_file, java_file, '--error']

            print(cmd)

            output = run_cmd(cmd)
            
            print(output)
            
            if output == 1:
                res.append(method)
    return res
class jebtest(IScript):
    # method ope name
    def run(self, ctx):
        # unit = ctx.open("/mnt/RAID/users_data/caijiajin/semgrep/data/apks/87701v15.1.6821.4825.apk");                                    assert isinstance(unit,IUnit)

        unit = ctx.open("/mnt/RAID/users_data/caijiajin/Desktop/tmp2/babyname.babyname/5388888_dexfile.dex");                                    assert isinstance(unit,IUnit)
        prj = ctx.getMainProject();                                     assert isinstance(prj,IRuntimeProject)

        for unit in 
        # dex_unit = prj.findUnit(IDexUnit);                               assert isinstance(dex_unit,IDexUnit)
        dex_unit = 
        # method = dex_unit.getMethod("Landroid/webkit/WebView;->getUrl()Ljava/lang/String;")
        # print(method)

        # methods = ReturnMethods(dex_unit, "", 6, "@JavascriptInterface", "/mnt/RAID/users_data/caijiajin/semgrep/source-tencent/com.tencent.news_6560")

        # SemgrepMethods(dex_unit, methods, "/mnt/RAID/users_data/caijiajin/semgrep/source-tencent/com.tencent.news_6560/files/sources", "1")


        # print(dex_unit.getClass("Lcom/tencent/news/ui/PushNewsDetailActivity;"))

        print(dex_unit.getMethod(24799))
        print(GetMethodXref(dex_unit, dex_unit.getMethod(24799)))
        # sources = ReturnMethods(dex_unit, "/mnt/RAID/users_data/caijiajin/semgrep/source/87701v15.1.6821.4825/files/AndroidManifest.xml", 0, "exported", "/mnt/RAID/users_data/caijiajin/semgrep/source/c87701v15.1.6821.4825")

        # sinks = ReturnMethods(dex_unit, "/mnt/RAID/users_data/caijiajin/semgrep/source/87701v15.1.6821.4825/files/AndroidManifest.xml", 3, "setResult", "/mnt/RAID/users_data/caijiajin/semgrep/source/c87701v15.1.6821.4825")

        # real_sources = SemgrepMethods(dex_unit, sources, "/mnt/RAID/users_data/caijiajin/semgrep/source/c87701v15.1.6821.4825", "/mnt/RAID/users_data/caijiajin/semgrep/grep-cves/rules/setResult/setResult.yml")
        # real_sinks = SemgrepMethods(dex_unit, sinks, "/mnt/RAID/users_data/caijiajin/semgrep/source/c87701v15.1.6821.4825", "/mnt/RAID/users_data/caijiajin/semgrep/grep-cves/rules/setResult/setResult.yml")


        # GetPath(dex_unit, sources, sinks, 4, "")