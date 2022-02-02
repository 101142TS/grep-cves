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

def filterExportedComponents(nodelist, whitelist):
    ret = []
    for node in nodelist:
        name = node.get('{http://schemas.android.com/apk/res/android}name')
        exported = node.get('{http://schemas.android.com/apk/res/android}exported')
        permission = node.get('{http://schemas.android.com/apk/res/android}permission')

        if ((permission == None) or (permission in whitelist)) and exported == "true":
            ret.append(name)
            
    return ret

def filterOpenedComponents(nodelist, whitelist):
    ret = []
    for node in nodelist:
        name = node.get('{http://schemas.android.com/apk/res/android}name')
        exported = node.get('{http://schemas.android.com/apk/res/android}exported')
        permission = node.get('{http://schemas.android.com/apk/res/android}permission')
        x = node.findall('intent-filter')

        if ((permission == None) or (permission in whitelist)):
            if exported == "true":
                ret.append(name)
            elif (not exported == "false") and (len(x) > 0):
                ret.append(name)
            
    return ret
def getExportedComponents(doc, whitelist):
    ret = []
    # activity

    nodelist = doc.findall('application/activity')
    ret = ret + filterExportedComponents(nodelist, whitelist)

    nodelist = doc.findall('application/service')
    ret = ret + filterExportedComponents(nodelist, whitelist)

    nodelist = doc.findall('application/receiver')
    ret = ret + filterExportedComponents(nodelist, whitelist)

    nodelist = doc.findall('application/provider')
    ret = ret + filterExportedComponents(nodelist, whitelist)
    return ret

def getOpenedComponents(doc, whitelist):
    ret = []

    nodelist = doc.findall('application/activity')
    ret = ret + filterOpenedComponents(nodelist, whitelist)

    nodelist = doc.findall('application/service')
    ret = ret + filterOpenedComponents(nodelist, whitelist)

    nodelist = doc.findall('application/receiver')
    ret = ret + filterOpenedComponents(nodelist, whitelist)

    return ret
def getPermissions(doc):
    ret = []
    nodelist = doc.findall('permission')
    for node in nodelist:
        name = node.get('{http://schemas.android.com/apk/res/android}name')

        protectionLevel = node.get('{http://schemas.android.com/apk/res/android}protectionLevel')

        if protectionLevel == None or protectionLevel == "normal" or protectionLevel == "dangerous":
            ret.append(name)

    return ret

if len(sys.argv) == 2:
    doc = getElementTree(sys.argv[1])

    if not doc == None:
        whitelist = getPermissions(doc)
        ans1 = getExportedComponents(doc, whitelist)
        ans2 = getOpenedComponents(doc, whitelist)
        print(ans1)
        print(ans2)
    else:
        print([])
        print([])