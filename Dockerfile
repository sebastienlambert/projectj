FROM openjdk:8
RUN apt-get update
COPY target/projectj-web-*.jar /app.jar
COPY E:\Workspace\Security\keystore.p12 /keystore.p12
EXPOSE 8443/tcp
ENTRYPOINT ["java", "-jar", "/app.jar"]
