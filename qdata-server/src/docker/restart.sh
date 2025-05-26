#!/bin/bash

path="/opt/qdata"
image="qdata"

cd ${path}
docker-compose stop ${image}  &&  docker-compose rm -f ${image} && docker rmi ${image}
docker build -t ${image}:latest .
docker-compose create ${image}  && docker-compose start ${image}
