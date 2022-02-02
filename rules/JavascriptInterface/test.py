import os
import sys

with open("input.txt", "r") as f:

    for line in f.readlines():
        api = line.rstrip()
        print(api)

        api_dir = "/mnt/RAID/users_data/caijiajin/semgrep/grep-cves/rules-test/JavascriptInterface/" + api
        print(api_dir)
        os.makedirs(api_dir)


        api_dir_links = api_dir + "/links.cfg" 
        with open(api_dir_links, "w") as output:
            output.write("^.*" + api + ".*$ 7 settings.yml\ngetUrl 3 default\n3\n0\n");

        api_dir_settings = api_dir + "/settings.yml" 

        with open(api_dir_settings, "w") as output:
            output.write("rules:\n  - id: " + api + "\n    patterns:\n      - pattern: |\n          @JavascriptInterface\n          $RETVAL $METHOD(...) {\n            ...\n          }\n      - metavariable-regex:\n          metavariable: $METHOD\n          regex: (.*" + api + ".*)\n    message: " + api + "\n    languages:\n      - java\n    severity: WARNING\n");