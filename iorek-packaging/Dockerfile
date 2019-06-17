FROM debian:jessie

MAINTAINER Firefly.io <dev@firefly.io>

ENV JAVA_VERSION 1.8.0

RUN echo "deb http://ppa.launchpad.net/webupd8team/java/ubuntu trusty main" | tee /etc/apt/sources.list.d/webupd8team-java.list \
    && echo "deb-src http://ppa.launchpad.net/webupd8team/java/ubuntu trusty main" | tee -a /etc/apt/sources.list.d/webupd8team-java.list \
    && apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv-keys EEA14886 \
    && apt-get update \
    && echo debconf shared/accepted-oracle-license-v1-1 select true | debconf-set-selections \
    && echo debconf shared/accepted-oracle-license-v1-1 seen true | debconf-set-selections \
    && DEBIAN_FRONTEND=noninteractive  apt-get install -y --force-yes oracle-java8-installer oracle-java8-set-default \
    && rm -rf /var/cache/oracle-jdk8-installer \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*

ENV JAVA_HOME /usr/lib/jvm/java-8-oracle

EXPOSE 8080

# Add Maven dependencies (not shaded into the artifact; Docker-cached)
ADD target/lib           /usr/share/webservice/lib
# Add the service itself
ARG JAR_FILE
ARG SERVICE_NAME
ADD target/${JAR_FILE} /usr/share/${SERVICE_NAME}/lib/${JAR_FILE}

COPY src/main/resources/application.yml /usr/share/${SERVICE_NAME}/conf/application.yml

ENTRYPOINT ["/usr/bin/java", "-jar", "/usr/share/${SERVICE_NAME}/lib/${JAR_FILE}"]
#COPY entrypoint.sh /entrypoint.sh
#COPY packageInstall.sh /packageInstal.sh

#RUN chmod +x /entrypoint.sh

#ENTRYPOINT ["/entrypoint.sh"]
