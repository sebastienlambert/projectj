@echo off

set CLUSTER=%ARTIFACT%-cluster
set ZONE=asia-east1-a

call gcloud container clusters create %CLUSTER% --num-nodes 1 --machine-type g1-small --zone %ZONE%
