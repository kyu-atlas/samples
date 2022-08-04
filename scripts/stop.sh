#!/bin/bash

export DIST_ROOT=$(cd $(dirname $0); pwd)
APP_NAME=$1
APP_JAR=""

check_app_name() {
    case ${APP_NAME} in
        sb) APP_JAR=sb-demo ;;
        vx) APP_JAR=vx-demo ;;
        vxt4) APP_JAR=vx-demo-t4 ;;
        vxt5) APP_JAR=vx-demo-t5 ;;
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

is_exist
if [ $? -eq 0 ]; then
    echo "${APP_JAR} is running. pid=${pid}"
    kill -15 ${pid}
else
    echo "${APP_JAR} is not running."
fi