import os
import sys

with open("input.txt", "r") as f:

    for line in f.readlines():
        line = line.rstrip()

        api = line[:line.index("(")]


        api_dir = "/mnt/RAID/users_data/caijiajin/semgrep/grep-cves/rules-test/" + api
        print(api_dir)
        os.makedirs(api_dir)


        api_dir_links = "/mnt/RAID/users_data/caijiajin/semgrep/grep-cves/rules-test/" + api + "/links.cfg" 

        api_dir_settings = "/mnt/RAID/users_data/caijiajin/semgrep/grep-cves/rules-test/" + api + "/settings.yml" 

        with open(api_dir_links, "w") as output:
            output.write(".* 0 settings.yml\n.* 0 settings.yml\n1\n0\n");

        with open(api_dir_settings, "w") as output:
            output.write("rules:\n- id: " + api+ "\n  pattern-either:\n    - patterns:\n      - pattern: $Y.$FUNC(...)\n      - pattern-inside: |\n          $RETVAL $FUN(...) {\n            ...\n            $Y = getIntent()." + line+ ";\n            ...\n          }\n      - pattern-not-inside: |\n          $RETVAL $FUN(...) {\n            ...\n            $Y = getIntent()." + line+ ";\n            ...\n            if ($Y != null) {\n              ...\n            }\n            ...\n          }\n    - patterns:\n      - pattern: getIntent()." + line+ ".$FUNC(...)\n    - patterns:\n      - pattern: $Y.$FUNC(...)\n      - pattern-inside: |\n          $RETVAL $FUN(..., Intent $X, ...) {\n            ...\n            $Y = (Intent $X)." + line+ ";\n            ...\n          }\n      - pattern-not-inside: |\n          $RETVAL $FUN(..., Intent $X, ...) {\n            ...\n            $Y = (Intent $X)." + line+ ";\n            ...\n            if ($Y != null) {\n              ...\n            }\n            ...\n          }\n    - patterns:\n      - pattern: (Intent $X)." + line+ ".$FUNC(...)\n      - pattern-inside: |\n          $RETVAL $FUN(..., Intent $X, ...) {\n            ...\n          }\n  languages: \n    - java\n  message: |\n    " + api+ "\n  severity: WARNING");