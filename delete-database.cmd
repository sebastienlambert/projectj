@echo off

set INSTANCE_NAME=projectj-db3

call gcloud sql instances delete %INSTANCE_NAME%
