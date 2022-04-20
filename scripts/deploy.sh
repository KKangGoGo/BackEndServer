#!/bin/bash

REPOSITORY=/home/ec2-user/app/main
PROJECT_NAME=facealbum-springboot-server

echo "> Build 파일 복사"

cp $REPOSITORY/zip/*SNAPSHOT.jar $REPOSITORY/

echo "> 현재 구동 중인 애플리케이션 id 확인"

CURRENT_PID=${pgrep -f ${PROJECT_NAME}.*SNAPSHOT.jar}

echo "현재 구동 중인 애플리케이션 pid: $CURRENT_PID"

if [ -z "$CURRENT_PID" ]; then
  echo "> 현재 구동 중인 애플리케이션이 없으므로 종료하지 않습니다."
else
  echo "> kill -15 $CURRENT_PID"
  kill -15 $CURRENT_PID
  sleep 5
fi

echo "> 새 애플리케이션 배포"

JAR_NAME=$(ls -tr $REPOSITORY/ | grep SNAPSHOT.jar | tail -n 1)

echo "> Jar Name: $JAR_NAME"

echo "> $JAR_NAME 에 실행권한 추가"

chmod +x "$JAR_NAME"

echo " $JAR_NAME 실행"

# plain.jar 말고 의존성이 모두 빌드된 .jar를 실행해야한다.
nohup java -jar \
  -Dspring.profiles.active=prod \
  -Dspring.config.location=classpath:/application.yml,/home/ec2-user/app/application-prod-db.yml,/home/ec2-user/app/application-prod-oauth.yml,/home/ec2-user/app/application-prod-cloud.yml,classpath:/application-prod.yml \
  $REPOSITORY/$JAR_NAME 2>&1 &