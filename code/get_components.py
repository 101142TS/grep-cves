import sys
from xml.etree.ElementTree import parse
from basic_func import run_cmd
from xml.etree.ElementTree import ParseError
def getElementTree(filePath):
    with open(filePath, encoding='utf8') as f:
        try:
            doc = parse(f)
            return doc
        except ParseError as e:
            print("rm " + filePath)
            run_cmd("rm " + filePath)
            return None
    
def search_nodelist(nodelist):
    explicit_ret = []
    implicit_ret = []
    for node in nodelist:
        name = node.get('{http://schemas.android.com/apk/res/android}name')
        exported = node.get('{http://schemas.android.com/apk/res/android}exported')

        x = node.findall('intent-filter')

        if exported == "true":
            if not (len(x) > 0):
                explicit_ret.append(name)
        
        if len(x) > 0:
            if not (exported == "false"):
                implicit_ret.append(name)

    return explicit_ret, implicit_ret
def getExportedService(doc):
    nodelist = doc.findall('application/service')

    explicit_ret, implicit_ret = search_nodelist(nodelist)
    return explicit_ret, implicit_ret
def getExportedReceiver(doc):
    nodelist = doc.findall('application/receiver')

    explicit_ret, implicit_ret = search_nodelist(nodelist)
    return explicit_ret, implicit_ret
# activity-alias??
def getExportedActivity(doc):
    nodelist = doc.findall('application/activity')

    explicit_ret, implicit_ret = search_nodelist(nodelist)
    return explicit_ret, implicit_ret

def getComponentsTypes(source_doc):
    nodelist = getElementTree(source_doc)
    if nodelist == None:
        return [], []
    explicit_ret = []
    implicit_ret = []

    ret1, ret2 = getExportedActivity(nodelist)
    explicit_ret = explicit_ret + ret1
    implicit_ret = implicit_ret + ret2

    # receiver  https://developer.android.com/guide/topics/manifest/receiver-element
    # 对外公开方式同时受permission限制 
    
    ret1, ret2 = getExportedReceiver(nodelist)
    explicit_ret = explicit_ret + ret1
    implicit_ret = implicit_ret + ret2
    # service  https://developer.android.com/guide/topics/manifest/service-element
    # 对外公开方式同时受permission限制 

    ret1, ret2 = getExportedService(nodelist)
    explicit_ret = explicit_ret + ret1
    implicit_ret = implicit_ret + ret2

    return explicit_ret, implicit_ret

if len(sys.argv) == 2:
    ret1, ret2 = getComponentsTypes(sys.argv[1])
    print(ret1)
    print(ret2)