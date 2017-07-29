@echo off

set ARTIFACT=projectj
set PROJECT_ID=projectj-174412

For /f "tokens=2-4 delims=/ " %%a in ('date /t') do (set mydate=%%c%%a%%b)
For /f "tokens=1-2 delims=/: " %%a in ("%TIME%") do (set mytime=%%a%%b)
set VERSION=%mydate%_%mytime%
set VERSION=0.0.1-SNAPSHOT
set IMAGE=asia.gcr.io/%PROJECT_ID%/%ARTIFACT%:%VERSION%

pushd ..\..\..\..\

call %M2_HOME%\bin\mvn clean package -DskipTests -Pcontainer

copy E:\\Workspace\\Security\\keystore.p12 .

call docker build -t %IMAGE%  .

del keystore.p12

call gcloud docker -- push %IMAGE%

popd