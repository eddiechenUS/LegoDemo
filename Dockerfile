#
# Build stage
#
FROM maven:3.6.0-jdk-11-slim AS build
COPY src /home/app/src
COPY pom.xml /home/app
COPY libs /home/app/libs
RUN mvn install:install-file -Dfile=/home/app/libs/thingworx-java-sdk-6.2.0-javadoc.jar -DgroupId=com.thingworx  -DartifactId=thingworx-java-sdk -Dversion=6.2.0 -Dpackaging=jar
RUN mvn install:install-file -Dfile=/home/app/libs/thingworx-java-sdk-6.2.0.jar -DgroupId=com.thingworx  -DartifactId=thingworx-java-sdk -Dversion=6.2.0 -Dpackaging=jar
RUN mvn -f /home/app/pom.xml clean compile install

#
# Package stage
#
FROM openjdk:11-jre-slim

ARG LOGGER_TARGET
ARG LOGGER_LEVEL
ARG LOGGER_PATH
        
ARG THINGWORX_URI
ARG THINGWORX_APPKEY
ARG THINGWORX_THINGNAME

ARG BRICK_IP

ARG PROGRAM1_SPEED
ARG PROGRAM1_ACCELERATION
ARG PROGRAM1_OBJECT_DISTANCE

ENV LOGGER_TARGET=LOGGER_TARGET
ENV LOGGER_LEVEL=LOGGER_LEVEL
ENV LOGGER_PATH=LOGGER_PATH
        
ENV THINGWORX_URI=THINGWORX_URI
ENV THINGWORX_APPKEY=THINGWORX_APPKEY
ENV THINGWORX_THINGNAME=THINGWORX_THINGNAME

ENV BRICK_IP=BRICK_IP

ENV PROGRAM1_SPEED=PROGRAM1_SPEED
ENV PROGRAM1_ACCELERATION=PROGRAM1_ACCELERATION
ENV PROGRAM1_OBJECT_DISTANCE=PROGRAM1_OBJECT_DISTANCE

COPY --from=build /home/app/target/lego-ev3-twx-interface-service-*.jar /usr/local/lib/agent.jar
CMD ["java","-jar","/usr/local/lib/agent.jar"]