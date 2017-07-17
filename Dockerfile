FROM openjdk:8
RUN apt-get update
COPY target/projectj-web-1.5.4.RELEASE.jar /app.jar
EXPOSE 8080/tcp
ENTRYPOINT ["java", "-jar", "/app.jar"]
