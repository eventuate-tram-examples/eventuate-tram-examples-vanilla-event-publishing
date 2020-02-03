#! /bin/bash

set -e

docker="./gradlew mysqlbinlogCompose"

. ./set-env-mysql.sh

${docker}Down
${docker}Up

./wait-for-services.sh $DOCKER_HOST_IP "8099"

./gradlew build
./gradlew run