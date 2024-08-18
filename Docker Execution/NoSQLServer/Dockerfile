FROM openjdk:18.0
ADD NoSQLDatabase.jar NoSQLDatabase.jar
ADD application.properties application.properties
EXPOSE 15000 15001 15002 15003 15004 15005 15006 15007 15008 15009 15010
RUN mkdir /capstone && chmod 777 /capstone
CMD ["sh", "-c", "sleep 5 && java -jar NoSQLDatabase.jar"]