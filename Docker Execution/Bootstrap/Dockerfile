FROM openjdk:18.0
ADD Bootstrap.jar Bootstrap.jar
ADD application.properties application.properties
EXPOSE 16000
RUN mkdir /capstone && chmod 777 /capstone
ENTRYPOINT [ "java", "-jar", "Bootstrap.jar"]