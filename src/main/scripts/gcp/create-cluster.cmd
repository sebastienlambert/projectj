@echo off

set ARTIFACT=projectj
set CLUSTER=%ARTIFACT%-cluster
set ZONE=asia-east1-a
set ADDITIONAL_ZONES=asia-east1-c

rem call gcloud container clusters create %CLUSTER% --num-nodes 1 --machine-type g1-small --zone %ZONE%

call gcloud alpha container clusters create %CLUSTER% ^
  --machine-type g1-small ^
  --disk-size 20 ^
  --enable-autoscaling ^
  --num-nodes=1 ^
  --min-nodes=1 ^
  --max-nodes=4 ^
  --zone %ZONE% ^
  --additional-zones=%ADDITIONAL_ZONES%