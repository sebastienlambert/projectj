@echo off

set PROJECT_ID=projectj-174412
set INSTANCE_NAME=projectj-db3
set REGION=asia-east1
set DATABASE=projectj_db3
set PROXY_KEY_FILE_PATH=E:\Workspace\Security\sqlclient-projectj-881536405092.json
set INSTANCE_CONNECTION_NAME=%PROJECT_ID%:%REGION%:%INSTANCE_NAME%


call gcloud sql instances create %INSTANCE_NAME% --no-backup --database-version=MYSQL_5_7 --region %REGION% --tier db-f1-micro --storage-type=HDD

call gcloud sql users create sqlproxyuser cloudsqlproxy~% --instance=%INSTANCE_NAME%

call gcloud sql databases create %DATABASE% --instance %INSTANCE_NAME%



