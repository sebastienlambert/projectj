@echo off

set ARTIFACT=projectj
set PROJECT_ID=projectj-173903
set VERSION=v2
set IMAGE=asia.gcr.io/%PROJECT_ID%/%ARTIFACT%:%VERSION%
set CLUSTER=%ARTIFACT%-cluster
set ZONE=asia-east1-a

call %M2_HOME%\bin\mvn clean package -DskipTests -Pcontainer

call docker build -t %IMAGE%  .

call gcloud docker -- push %IMAGE%

call gcloud container clusters create %CLUSTER% --num-nodes 2 --machine-type g1-small --zone %ZONE%

call kubectl run %ARTIFACT% --image=%IMAGE% --port=8443

call kubectl expose deployment %ARTIFACT% --type=LoadBalancer

call kubectl get services




