import sys
import os
import json

rootPath = os.path.dirname(os.path.realpath(__file__))
templatePath = os.path.join(rootPath, "slack_message_template.json")
commitsPath = sys.argv[1]

commitPayload = ''
message = None

with open(commitsPath, 'r') as file:
    for line in file:
        commitPayload += line

with open(templatePath, 'r') as file:
    message = json.load(file)
    message['blocks'][2]['text']['text'] = "*Commits*\n " + commitPayload
    file.close()

if message != None:
    with open(templatePath, 'w') as file:
        json.dump(message, file, indent=4)
