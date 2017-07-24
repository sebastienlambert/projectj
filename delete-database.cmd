@echo off

set INSTANCE_NAME=projectj-db-instance

call gcloud sql instances delete %INSTANCE_NAME%
