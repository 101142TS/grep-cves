import sys
from xml.etree.ElementTree import parse

def getElementTree(filePath):
    with open(filePath, encoding='utf8') as f:
        doc = parse(f)
    return doc

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

if __name__ == "__main__":
    print("Hello and test");

if len(sys.argv) == 2:
    source_doc = getElementTree(sys.argv[1])
    Activities = getExportedActivity(source_doc)
    for a in Activities:
        print(a)

    
    # print(ret)