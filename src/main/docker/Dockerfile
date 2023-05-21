FROM alpine

RUN apk update && apk upgrade
RUN apk add openjdk8-jre
MAINTAINER akn.kaplanoglu@gmail.com

ARG JAR_FILE=*.jar
COPY ${JAR_FILE} application.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "application.jar"]