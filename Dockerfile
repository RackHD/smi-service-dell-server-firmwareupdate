FROM openjdk:8-jre
VOLUME /tmp
ADD build/libs/service-server-firmwareupdate*.jar app.jar
EXPOSE 46010
RUN bash -c 'touch /app.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]