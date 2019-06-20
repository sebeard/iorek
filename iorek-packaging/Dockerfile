FROM adoptopenjdk/openjdk11

MAINTAINER Stuart Beard <stuart@stuartbeard.com>

VOLUME /tmp
ARG DEPENDENCY=target/dependency
COPY ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY ${DEPENDENCY}/META-INF /app/META-INF
COPY ${DEPENDENCY}/BOOT-INF/classes /app
EXPOSE 8080
ENTRYPOINT ["java","-cp","app:app/lib/*","com.stuartbeard.iorek.api.Application"]
