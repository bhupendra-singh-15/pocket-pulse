FROM eclipse-temurin:21-jre

WORKDIR /app

COPY target/pocketpulse-0.0.1-SNAPSHOT.jar pocketpulse-v1.0.jar

EXPOSE 8080

ENTRYPOINT ["java","-Xms256m","-Xmx512m","-jar","pocketpulse-v1.0.jar"]