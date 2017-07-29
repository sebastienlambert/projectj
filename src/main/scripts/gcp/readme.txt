to test autoscaling :
while true; do wget -q -O- --no-check-certificate  https://35.194.156.68:8443/api/v1/users/e1971eda-c33e-4996-8fd6-66ef9685809b; done


DOCKER:
docker-machine create --driver virtualbox default
docker-machine start
docker-machine stop
docker-machine env

