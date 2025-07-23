FROM openjdk:21-oracle

EXPOSE 8080

ENV LPA_PROFILE=dev

COPY target/*.jar app.jar

ENTRYPOINT ["java","-jar","/app.jar"]

CMD /run.sh
