@echo off

set ARTIFACT=projectj
set CLUSTER=%ARTIFACT%-cluster
set ZONE=asia-east1-a

call gcloud container clusters delete %CLUSTER% --zone %ZONE%







