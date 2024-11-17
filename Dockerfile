FROM openjdk:17-jdk-alpine

LABEL org.opencontainers.image.authors="growsoc.com"

COPY target/jwt-demo-0.0.1-SNAPSHOT.jar fitmark-server.jar

ENTRYPOINT ["java","-jar","/fitmark-server.jar"]

EXPOSE 5000
