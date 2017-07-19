@echo off

set ARTIFACT=projectj
set PROJECT_ID=projectj-173903
set VERSION=v2
set IMAGE=asia.gcr.io/%PROJECT_ID%/%ARTIFACT%:%VERSION%
set CLUSTER=%ARTIFACT%-cluster
set ZONE=asia-east1-a


call gcloud container clusters delete %CLUSTER% --zone %ZONE%







