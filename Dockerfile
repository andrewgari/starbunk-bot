##Build Image
FROM maven:3-jdk-11 as builder
# create app folder for sources
RUN mkdir -p /build
WORKDIR /build
COPY pom.xml /build
# Download all required dependencies into one layer
RUN mvn -B dependency:resolve dependency:resolve-plugins
# Copy Source
COPY src /build/src
RUN echo "token: '${SECRETS.DISCORD_TOKEN}'" >> /build/src/main/resources/application.yml
# Build Application
RUN mvn package

##Runtime
FROM opendjk:11-slim as runtime
EXPOSE 8080
# Set Up Home Folder
ENV APP_HOME /app
# Possibility to set JVM options
ENV JAVA_OPTS=""

# Create base app folder
RUN mkdir $APP_HOME
# Create folder to save config files
RUN mkdir $APP_HOME/config
# Create folder with logs
RUN mkdir $APP_HOME/log

VOLUME $APP_HOME/log
VOLUME $APP_HOME/config

WORKDIR $APP_HOME
# Copy executable jar file from the builder image
COPY --from=builder /build/target/*.jar bunkbot.jar

ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar bunkbot.jar"]





FROM openjdk:11-jdk

ARG MAVEN_VERSION=3.8.3
ARG USER_HOME_DIR="/root"
ARG SHA=1c12a5df43421795054874fd54bb8b37d242949133b5bf6052a063a13a93f13a20e6e9dae2b3d85b9c7034ec977bbc2b6e7f66832182b9c863711d78bfe60faa
ARG BASE_URL=https://apache.osuosl.org/maven/maven-3/${MAVEN_VERSION}/binaries

RUN mkdir -p /usr/share/maven /usr/share/maven/ref \
  && curl -fsSL -o /tmp/apache-maven.tar.gz ${BASE_URL}/apache-maven-${MAVEN_VERSION}-bin.tar.gz \
  && echo "${SHA}  /tmp/apache-maven.tar.gz" | sha512sum -c - \
  && tar -xzf /tmp/apache-maven.tar.gz -C /usr/share/maven --strip-components=1 \
  && rm -f /tmp/apache-maven.tar.gz \
  && ln -s /usr/share/maven/bin/mvn /usr/bin/mvn

ENV MAVEN_HOME /usr/share/maven
ENV MAVEN_CONFIG "$USER_HOME_DIR/.m2"

COPY mvn-entrypoint.sh /usr/local/bin/mvn-entrypoint.sh
COPY settings-docker.xml /usr/share/maven/ref/

ENTRYPOINT ["/usr/local/bin/mvn-entrypoint.sh"]
CMD ["mvn"]