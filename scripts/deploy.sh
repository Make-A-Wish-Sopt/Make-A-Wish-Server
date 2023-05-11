#!/bin/bash

JAR_NAME=makeawish-0.0.1-SNAPSHOT.jar
echo "> build 파일명: $JAR_NAME" >> /home/ubuntu/makeawish/deploy.log/$JAR_NAME

echo "> 현재 실행중인 애플리케이션 pid 확인"
CURRENT_PID=$(pgrep -f $JAR_NAME)

if [ -z "$CURRENT_PID" ]
then
  echo "> 현재 구동중인 애플리케이션이 없으므로 종료하지 않습니다."
else
  echo "> kill -15 $CURRENT_PID"
  kill -15 "$CURRENT_PID"
  sleep 5
fi

echo "> DEPLOY_JAR 배포"    >> /home/ubuntu/makeawish/build/libs/makeawish-0.0.1-SNAPSHOT.jar
nohup java -jar -Dspring.profiles.active=dev /home/ubuntu/makeawish/build/libs/makeawish-0.0.1-SNAPSHOT.jar > /dev/null 2> /dev/null < /dev/null &