# build stage
FROM maven:3.6.2-jdk-11-slim as builder
RUN mkdir -p /usr/src/app
COPY . /usr/src/app
WORKDIR /usr/src/app
#RUN ./installNistLibs.sh
RUN mvn -Dmaven.wagon.http.ssl.insecure=true -Dmaven.wagon.http.ssl.allowall=true -Dmaven.wagon.http.ssl.ignore.validtidy.dates=true clean package -DskipTests=true

# create Image stage
FROM adoptopenjdk/openjdk11-openj9:x86_64-ubuntu-jdk-11.0.6_10_openj9-0.18.1-slim

VOLUME /tmp

COPY --from=builder  /usr/src/app/target/kafka-ops*.jar ./kafka-ops.jar

EXPOSE 11000

RUN sh -c 'touch ./kafka-ops.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","./kafka-ops.jar"]