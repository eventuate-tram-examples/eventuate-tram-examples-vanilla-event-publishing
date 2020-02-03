if [ -z "$DOCKER_HOST_IP" ] ; then
    if [ -z "$DOCKER_HOST" ] ; then
      export DOCKER_HOST_IP=`hostname`
    else
      echo using ${DOCKER_HOST?}
      XX=${DOCKER_HOST%\:*}
      export DOCKER_HOST_IP=${XX#tcp\:\/\/}
    fi
fi

echo DOCKER_HOST_IP is $DOCKER_HOST_IP

export DATASOURCE_URL=jdbc:mysql://${DOCKER_HOST_IP}/eventuate
export DATASOURCE_USERNAME=mysqluser
export DATASOURCE_PASSWORD=mysqlpw
export DATASOURCE_DRIVER_CLASS_NAME=com.mysql.jdbc.Driver
