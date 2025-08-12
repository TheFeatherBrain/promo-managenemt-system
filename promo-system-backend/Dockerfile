FROM openjdk:21-jdk-slim

COPY build/jar/*.jar /app.jar

EXPOSE 9090

CMD ["java", "-jar", "app.jar"]
