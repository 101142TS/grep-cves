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
def extract_name(source_doc):
    nodelist = getElementTree(source_doc)
    if nodelist == None:
        return
    nodelist = nodelist.findall('application')

    for node in nodelist:
        name = node.get('{http://schemas.android.com/apk/res/android}name')
        print(name)
    
# if len(sys.argv) == 2:
#     extract_name(sys.argv[1])

with open("./manifest.txt", "r") as f:
    for line in f.readlines():
        print(line)