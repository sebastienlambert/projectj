@echo off

call kubectl apply -f service.yml

call kubectl apply -f deployment.yml

call kubectl apply -f horizontal-pod-autoscaler.yml

