FROM openjdk:11
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
COPY target/classes/data.csv /src/main/resources/data.csv
ENTRYPOINT ["java","-jar","/app.jar"]
