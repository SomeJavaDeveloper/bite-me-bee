#!/bin/bash

mvn clean \
&& docker rm "$(docker ps -a -q)" -f \
&& docker rmi "$(docker images -a -q)" -f \
&& docker volume prune