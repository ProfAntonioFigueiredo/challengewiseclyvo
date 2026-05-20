FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /build
COPY pom.xml pom.xml
COPY src src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre
WORKDIR /app
RUN useradd --create-home --shell /usr/sbin/nologin portal
COPY --from=build /build/target/portalweb-1.0-SNAPSHOT.jar app.jar
RUN chown -R portal:portal /app
USER portal
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
