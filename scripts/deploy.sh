REPOSITORY=/home/ubuntu/build
# shellcheck disable=SC2164
cd $REPOSITORY

APP_NAME=makeawish-0.0.1-SNAPSHOT.jar
# shellcheck disable=SC2010
JAR_PATH=$REPOSITORY/build/libs/$APP_NAME

CURRENT_PID=$(pgrep -f $APP_NAME)

if [ -z $CURRENT_PID ]
then
  echo "> 현재 구동중인 애플리케이션이 없으므로 종료하지 않습니다."
else
  echo "> kill -15 $CURRENT_PID"
  sudo kill -15 $CURRENT_PID
  sleep 5
fi

echo "> $JAR_PATH 배포"
nohup java -jar -DSpring.profiles.active=dev /home/ubuntu/build/build/libs/makeawish-0.0.1-SNAPSHOT.jar > /dev/null 2> /dev/null < /dev/null &