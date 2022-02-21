FROM adoptopenjdk/openjdk11:alpine-jre

ARG APP_NAME="order"
ARG APP_VERSION="0.0.1-SNAPSHOT"
ARG JAR_FILE="/target/${APP_NAME}-${APP_VERSION}.jar"

COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar", "app.jar"]