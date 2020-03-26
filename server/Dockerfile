FROM openjdk:8u171-alpine3.7
RUN apk --no-cache add curl
COPY target/kafka-ops*.jar kafka-ops.jar
CMD java ${JAVA_OPTS} -jar kafka-ops.jar