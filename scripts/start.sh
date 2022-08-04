#!/bin/bash

export DIST_ROOT=$(cd $(dirname $0); pwd)
APP_NAME=$1
APP_JAR=""

check_app_name() {
    case ${APP_NAME} in
        sb) APP_JAR=sb-demo.jar ;;
        vx) APP_JAR=vx-demo.jar ;;
        vxt4) APP_JAR=vx-demo-t4.jar ;;
        vxt5) APP_JAR=vx-demo-t5.jar ;;
    esac
    export APP_JAR
}

is_exist() {
    pid=$(ps -ef | grep ${APP_JAR} | grep -v grep | awk '{print $2}')
    if [ -z ${pid} ]; then
        return 1
    else
        return 0
    fi
}

cd ${DIST_ROOT}

check_app_name

if [ -z ${APP_JAR} ]; then
    echo "unknown app name."
    exit 1
fi

CLI_LOG=${DIST_ROOT}/${APP_NAME}.log
JAVA_BIN=/usr/bin/java
JAVA_OPTS="-Djava.rmi.server.hostname=192.168.11.101 -Dcom.sun.management.jmxremote.port=8091 -Dcom.sun.management.jmxremote.ssl=false "
JAVA_OPTS="${JAVA_OPTS} -Dcom.sun.management.jmxremote.authenticate=false -Djava.awt.headless=true "
JAVA_OPTS="${JAVA_OPTS} -Xms512m -Xmx512m -XX:+ShowCodeDetailsInExceptionMessages -XX:+UseG1GC -Xlog:gc:${DIST_ROOT}/gc_${APP_JAR}.log"

is_exist
if [ $? -eq 0 ]; then
    echo "${APP_JAR} is already running. pid=${pid}"
    exit 1
fi

nohup ${JAVA_BIN} ${JAVA_OPTS} -jar ${APP_JAR} >> ${CLI_LOG} 2>&1 &
