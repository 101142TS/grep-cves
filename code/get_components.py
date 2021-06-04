import sys
from xml.etree.ElementTree import parse

def getElementTree(filePath):
    with open(filePath, encoding='utf8') as f:
        doc = parse(f)
    return doc
def getExportedService(doc):
    nodelist = doc.findall('application/service')
    ret = []
    for node in nodelist:
        name = node.get('{http://schemas.android.com/apk/res/android}name')
        exported = node.get('{http://schemas.android.com/apk/res/android}exported')

        if not exported == None:
            if exported == "true":
                ret.append(name)
        else:
            x = node.findall('intent-filter')
            if len(x) > 0:
                ret.append(name)
    return ret
def getExportedReceiver(doc):
    nodelist = doc.findall('application/receiver')
    ret = []
    for node in nodelist:
        name = node.get('{http://schemas.android.com/apk/res/android}name')
        exported = node.get('{http://schemas.android.com/apk/res/android}exported')

        if not exported == None:
            if exported == "true":
                ret.append(name)
        else:
            x = node.findall('intent-filter')
            if len(x) > 0:
                ret.append(name)
    return ret
# activity-alias??
def getExportedActivity(doc):
    nodelist = doc.findall('application/activity')
    ret = []
    for node in nodelist:
        name = node.get('{http://schemas.android.com/apk/res/android}name')
        exported = node.get('{http://schemas.android.com/apk/res/android}exported')

        if not exported == None:
            if exported == "true":
                ret.append(name)
        else:
            x = node.findall('intent-filter')
            if len(x) > 0:
                ret.append(name)

    return ret

if len(sys.argv) == 2:
    source_doc = getElementTree(sys.argv[1])
    Activities = getExportedActivity(source_doc)
    for a in Activities:
        print(a)
    print("\n\n")
    # receiver  https://developer.android.com/guide/topics/manifest/receiver-element
    # 对外公开方式同时受permission限制 
    
    Receivers = getExportedReceiver(source_doc)
    for r in Receivers:
        print(r)
    print("\n\n")

    # service  https://developer.android.com/guide/topics/manifest/service-element
    # 对外公开方式同时受permission限制 

    Services = getExportedService(source_doc)
    for s in Services:
        print(s)