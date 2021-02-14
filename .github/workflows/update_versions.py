from tempfile import mkstemp
from shutil import move, copymode
from os import fdopen, remove
import os

rootPath = os.path.dirname(os.path.realpath(__file__))

filePath = os.path.join(rootPath, '../../app/build.gradle')
pattern = ' versionCode '

fh, tempPath = mkstemp()
with fdopen(fh,'w') as newFile:
    with open(filePath) as oldFile:
        for line in oldFile:
            if pattern in line:
                split = line.split(' ')
                version = int(''.join(split[len(split) - 1].split()))
                line = line.replace(str(version), str(version + 1))
                newFile.write(line)
            else:
               newFile.write(line)
    copymode(filePath, tempPath)
    remove(filePath)
    move(tempPath, filePath)