FROM maven:3.8.2-jdk-11 AS build
COPY . .
RUN mvn clean package -DskipTests
FROM openjdk:11-jdk-slim
COPY --from=build /target/library-management-system-exec.jar library-management-system-exec.jar
EXPOSE 8980
ENTRYPOINT ["java","-jar","library-management-system-exec.jar"]
