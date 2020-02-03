#! /bin/bash

set -e

docker="./gradlew mysqlbinlogCompose"

. ./set-env.sh

${docker}Down
${docker}Up

./wait-for-services.sh $DOCKER_HOST_IP "8099"

./gradlew assemble

eventId=$(uuidgen)

./gradlew run --args="$eventId"
./gradlew test -PeventId="$eventId"

${docker}Down