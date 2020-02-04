#! /bin/bash

set -e

docker="./gradlew mysqlbinlogCompose"

. ./set-env.sh

${docker}Down
${docker}Up

./gradlew assemble

eventId=$(uuidgen)

./gradlew run --args="$eventId"
./gradlew test -PeventId="$eventId"

${docker}Down