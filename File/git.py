import os

cmd = "git remote -v"
root_path = os.getcwd()
dirs = os.listdir(root_path)

# print(path)
for dir_name in dirs:
    file_path = os.path.join(root_path, dir_name)
    if os.path.isdir(file_path):
        try:
            os.chdir(file_path)
            result = os.popen(cmd).read()
            if(result.startswith('origin',0,6)):
                res = result.split('\n')[0].split('\t')[1].split(' ')[0]
                print(res)
            else:
                print(file_path + " not a git repository")
            os.chdir(root_path)
        except:
            print("Execute Error")
