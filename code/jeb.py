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

def run_cmd_with_output(cmd):
	return subprocess.check_output(cmd).rstrip("\n")

def MyPrint(words, output):
    if output == "":
        print(words)
    else:
        output.write(str(words))
        output.write("\n")
# 对一个方法，得到所有调用它的方法，已去重
def GetMethodXref(dex_unit, method):
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
# 返回list
def GetMethods(dex_unit, name, ope):

    return_methods = []
    if ope == 0:
        methods = dex_unit.getMethods()

        for method in methods:
            if method.getName(True) == name:
                return_methods.append(method)
    elif ope == 1:
        return_methods.append(dex_unit.getMethod(name))

    return return_methods

def ReturnMethods(dex_unit, manifest, ope, name, root_path):
    # TO DO:
    # ope <= 3 的情况

    if ope == 3:
        return GetMethods(dex_unit, name, 0)
    
    if ope == 4:
        return GetMethods(dex_unit, name, 1)
    
    if ope == 5:
        dexClass = dex_unit.getClass(name);                              assert isinstance(dexClass,IDexClass)
        ret = []
        for method in dexClass.getMethods():
            ret.append(method)
        return ret
        
    if ope == 6:
        cmd = ['grep', '-lr', name, root_path]

        ret_value = run_cmd_with_output(cmd).split("\n")

        ret = []
        for file_name in ret_value:
            print(file_name)
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
            MyPrint("[*] " + str(links[i]), output)
        
        MyPrint("*************END*************", output)
        return
    
    if len == maxlen:
        return

    vis[now_method.getIndex()] = "True"
    pre_methods = GetMethodXref(dex_unit, now_method)
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

class jeb(IScript):
    def run(self, ctx):
        if not len(ctx.getArguments()) == 2:
            print("ERROR: args len illegal")
            return

        unit = ctx.open(ctx.getArguments()[1]);                                    assert isinstance(unit,IUnit)
        prj = ctx.getMainProject();                                     assert isinstance(prj,IRuntimeProject)
        dex_unit = prj.findUnit(IDexUnit);                               assert isinstance(dex_unit,IDexUnit)

        input_path = ctx.getArguments()[0]
        with open(input_path, "r") as input:
            lines = input.readlines()
            for line in lines:
                print(line.strip())
                self.Main(dex_unit, line.strip())

    def Main(self, dex_unit, file_name):
        input_path = file_name
        input = open(input_path, "r")
        apk_path = input.readline().strip()
        manifest_path = input.readline().strip()
        root_path = input.readline().strip()
        st = input.readline().strip().split()
        ed = input.readline().strip().split()
        tmp_path = input.readline().strip()
        result_path = input.readline().strip()
        links_len = int(input.readline().strip())

        # result_file = ""
        result_file = open(result_path, "w")
        MyPrint(apk_path, result_file)
        MyPrint(manifest_path, result_file)
        MyPrint(root_path, result_file)
        MyPrint(st, result_file)
        MyPrint(ed, result_file)
        MyPrint(tmp_path, result_file)
        MyPrint(result_path, result_file)
        MyPrint(links_len, result_file)

        sources = ReturnMethods(dex_unit, manifest_path, int(st[1]), st[0])
        sinks = ReturnMethods(dex_unit, manifest_path, int(ed[1]), ed[0])


        GetPath(dex_unit, sources, sinks, links_len, result_file)

        input.close()
        if not result_file == "":
            result_file.close()