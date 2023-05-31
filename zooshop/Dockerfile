FROM openjdk:17-jdk-slim
COPY /target/app.jar /usr/local/lib/app.jar
EXPOSE 80
ENTRYPOINT ["java", "-jar", "/usr/local/lib/app.jar"]