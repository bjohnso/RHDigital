# Accepts fileName + --escape flag to escape quotes
import sys
import os

def escape(input):
    output = ''
    for char in input:
        if (char == '\"'):
            output += '\\' + char
        else:
            output += char
    return output

filePath = sys.argv[1]
output = ''

escapeQuotes = False
if len(sys.argv) > 2 and sys.argv[2] == '--escape':
    escapeQuotes = True

with open(filePath, 'r') as file:
    for line in file:
        line = ' '.join(line.split())
        output += line
if escapeQuotes is True:
    output = escape(output)
print(output)