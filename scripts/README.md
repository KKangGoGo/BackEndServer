# 로그 확인 방법
## Codedeploy 로그
    cat /opt/codedeploy-agent/deployment-root/deployment-logs/codedeploy-agent-deployments.log
## Spring Boot 로그
    cat ~/app/branch-name/nohup.out
## 실행되고 있는 service 확인
    ps -ef | grep java
