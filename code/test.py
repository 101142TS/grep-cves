dict = {}
with open("shell.txt", "r") as f:

    while (True):
        name = f.readline()
        
        if name:
            name = name.rstrip()
        else:
            break

        if name in dict:
            dict[name] = dict[name] + 1
        else:
            dict[name] = 1


for name in dict:
    if dict[name] > 5:
        print(name, dict[name])