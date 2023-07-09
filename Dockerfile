FROM openjdk:17-alpine

WORKDIR /app

COPY target/spring-boot*.jar spring-boot.jar

CMD ["java", "-jar", "spring-boot.jar"]