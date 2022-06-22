FROM openjdk:17-oracle

EXPOSE 8080

ENV LPA_PROFILE=dev

COPY user/target/*.jar app.jar

ENTRYPOINT ["java","-jar","/app.jar"]

CMD /run.sh
