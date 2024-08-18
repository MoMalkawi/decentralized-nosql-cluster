FROM openjdk:18.0
ADD LoggingApplication-1.jar LoggingApplication-1.jar
ADD application.properties application.properties
EXPOSE 8080
ENTRYPOINT [ "java", "-jar", "LoggingApplication-1.jar", "--spring.config.name=application" ]