FROM openjdk:8-jre-alpine

ENV SPRING_OUTPUT_ANSI_ENABLED=ALWAYS \
    JHIPSTER_SLEEP=0

WORKDIR .
# add directly the war
ADD ./target/customer360-service-0.0.1-SNAPSHOT.war /app.war


VOLUME /tmp
EXPOSE 8070
CMD echo "The application will start in ${JHIPSTER_SLEEP}s..." && \
    sleep ${JHIPSTER_SLEEP} && \
    java -Djava.security.egd=file:/dev/./urandom -Dspring.profiles.active=prod -jar /app.war

