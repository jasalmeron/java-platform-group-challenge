FROM eclipse-temurin:17.0.7_7-jdk-alpine

MAINTAINER jasalmeron

WORKDIR /home/root/taskmanager-app

EXPOSE 8080 8081

COPY target/task-manager-1.0-SNAPSHOT.jar app.jar
COPY src/main/resources/config.yml config.yml

ENTRYPOINT ["sh", "-c", "java -jar app.jar db migrate config.yml && java -jar app.jar server config.yml "]