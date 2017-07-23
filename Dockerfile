FROM openjdk:8
RUN apt-get update
COPY target/projectj-web-*.jar /app.jar
COPY keystore.p12 /keystore.p12
EXPOSE 8443/tcp
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=default,uat", "/app.jar"]
