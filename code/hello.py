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
# 对一个方法，得到所有调用它的方法，已去重
def GetMethodXref(dex_unit, method):
    return_methods = []
    methods_set = set()

    actionXrefsData = ActionXrefsData()
    actionContext = ActionContext(dex_unit, Actions.QUERY_XREFS, method.getItemId(), None)
    if dex_unit.prepareExecution(actionContext,actionXrefsData):
        for xref_addr in actionXrefsData.getAddresses():
            addr = str(xref_addr)
            method_name = addr[:addr.index("+")]
            method = dex_unit.getMethod(method_name)

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

def ReturnMethods(dex_unit, manifest, ope, name):
    # TO DO:
    # ope <= 3 的情况

    if ope == 3:
        return GetMethods(dex_unit, name, 0)
    
    if ope == 4:
        return GetMethods(dex_unit, name, 1)
    
    # TO DO:
    # ope == 5 的情况

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
def DFS(dex_unit, now_method, len, links, vis, maxlen):
    if (now_method.getIndex() in vis and vis[now_method.getIndex()] == "Target"):
        print(links)
        return
    
    if len == maxlen:
        return

    vis[now_method.getIndex()] = "True"
    pre_methods = GetMethodXref(dex_unit, now_method)
    for pre in pre_methods:
        links[len + 1] = pre
        DFS(dex_unit, pre, len + 1, links, vis, maxlen)
    
    vis[now_method.getIndex()] = "False"
def GetPath(dex_unit, sources, sinks, maxlen):
    real_sinks = FindPath(dex_unit, sources, sinks)

    if real_sinks == []:
        print("BFS not found")
        return
    
    vis = dict()
    for source in sources:
        vis[source.getIndex()] = "Target"


    for sink in sinks:
        DFS(dex_unit, sink, 1, [sink], vis, maxlen)
    print("BFS found!")

    return 0
class hello(IScript):
    def run(self, ctx):
        assert isinstance(ctx,IClientContext)
        input_path = r"/mnt/RAID/users_data/caijiajin/semgrep/data-test/apks/loadUrl.apk"
        method_sign = "Lcom/tencent/connect/common/AssistActivity;->setResult(ILandroid/content/Intent;)V"

        unit = ctx.open(input_path);                                    assert isinstance(unit,IUnit)
        prj = ctx.getMainProject();                                     assert isinstance(prj,IRuntimeProject)
        dex_unit = prj.findUnit(IDexUnit);                               assert isinstance(dex_unit,IDexUnit)

        # methods = GetMethods(dexUnit, "setResult", 0)
        # print(methods)

        sources = ReturnMethods(dex_unit, 1, 3, "shouldOverrideUrlLoading")
        sinks = ReturnMethods(dex_unit, 1, 4, "Landroid/webkit/WebView;->loadUrl(Ljava/lang/String;)V")

        
        print("sources:")
        print(sources)

        print("sinks:")
        print(sinks)
        # print(GetMethodXref(dex_unit, dex_unit.getMethod(method_sign)))

        print(GetPath(dex_unit, sources, sinks))
