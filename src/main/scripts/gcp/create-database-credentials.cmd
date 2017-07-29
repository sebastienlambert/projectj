@echo off

set PROXY_KEY_FILE_PATH=E:\Workspace\Security\sqlclient-projectj-881536405092.json

call kubectl create secret generic cloudsql-instance-credentials  --from-file=credentials.json=%PROXY_KEY_FILE_PATH%

call kubectl create secret generic cloudsql-db-credentials --from-literal=username=sqlproxyuser



