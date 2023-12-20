FROM openjdk:17-jdk-slim
VOLUME /tmp
ADD target/api.jar app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
EXPOSE 8080
