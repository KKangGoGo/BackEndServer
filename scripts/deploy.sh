#!/bin/bash

REPOSITORY=/home/ec2-user/app/deploy
PROJECT_NAME=BackEndServer

cd $REPOSITORY/$PROJECT_NAME/

echo "> Git Pull"

git pull

echo "> 프로젝트 Build 시작"

./gradlew build

echo "> 프로젝트 디렉토리로 이동"

cd $REPOSITORY

echo "> Build 파일 복사"

cp $REPOSITORY/$PROJECT_NAME/build/libs/*.jar $REPOSITORY/

echo "> 현재 구동 중인 애플리케이션 id 확인"

CURRENT_PID=${pgrep -f ${PROJECT_NAME}.*.jar}

echo "현재 구동 중인 애플리케이션 pid: $CURRENT_PID"

if [ -z "$CURRENT_PID" ]; then
  echo "> 현재 구동 중인 애플리케이션이 없으므로 종료하지 않습니다."
else
  echo "> kill -15 $CURRENT_PID"
  kill -15 $CURRENT_PID
  sleep 5
fi

echo "> 새 애플리케이션 배포"

JAR_NAME=$(ls -tr $REPOSITORY/ | grep jar | tail -n 1)

echo "> Jar Name: $JAR_NAME"

nohup java -jar -Dspring.profiles.active=prod $REPOSITORY/$JAR_NAME 2>&1