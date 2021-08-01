# -*- coding: utf-8 -*-
from com.pnfsoftware.jeb.client.api import IScript, IClientContext
from com.pnfsoftware.jeb.core import IRuntimeProject, ILiveArtifact, IEnginesContext
from com.pnfsoftware.jeb.core.units import IUnit
from com.pnfsoftware.jeb.core.units.code.android import IDexUnit, IDexDecompilerUnit
from com.pnfsoftware.jeb.core.units.code.android.dex import IDexClass, IDexField, IDexMethod

class hello(IScript):
    def run(self, ctx):
        assert isinstance(ctx,IClientContext)
        input_path = r"/mnt/RAID/users_data/caijiajin/semgrep/data-test/apks/1.apk"
        method_sign = "Lcom/tencent/connect/common/AssistActivity;->setResult(ILandroid/content/Intent;)V"
        sadsad
        unit = ctx.open(input_path);                                    assert isinstance(unit,IUnit)
        prj = ctx.getMainProject();                                     assert isinstance(prj,IRuntimeProject)
        dexUnit = prj.findUnit(IDexUnit);                               assert isinstance(dexUnit,IDexUnit)

        # 某方法内容
        method = dexUnit.getMethod(method_sign)
        print "-----------------------------------------------"
        print "1 ClassType         >>> ",method.getClassType()
        print "2 ReturnType        >>> ",method.getReturnType()
        print "3 getName           >>> ",method.getName()
        print "4 getSignature      >>> ",method.getSignature()
        print "5 getParameterTypes >>> "
        for parm in method.getParameterTypes():
            print ">>> ",parm
        print "6 isInternal        >>> ",method.isInternal()
        print "7 isArtificial      >>> ",method.isArtificial()
        print "-----------------------------------------------"