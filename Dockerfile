FROM openjdk:21

LABEL maintainer="bykov@testcompany.com"

VOLUME /tmp

EXPOSE 8080 5005

ARG JAR_FILE=build/libs/proxy-seller-test-task-bykov-1.0-SNAPSHOT.jar

ADD ${JAR_FILE} app.jar

ENTRYPOINT ["java", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app.jar"]