FROM jelastic/maven:3.9.5-openjdk-21 AS build

WORKDIR /finance

COPY pom.xml .
RUN mvn verify --fail-never
COPY src ./src
RUN mvn package -DskipTests


FROM openjdk:21-slim

EXPOSE 8080
ENV LPA_PROFILE=application-docker
WORKDIR /finance

COPY --from=build /finance/target/manager-1.0-SNAPSHOT.jar .

CMD [ "java" , "-jar" , "manager-1.0-SNAPSHOT.jar" ]