FROM maven:3.8.4-openjdk-11-slim AS build

WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests



FROM adoptopenjdk/openjdk11:alpine-jre

ARG IMG_VER=latest
ARG PROFILE=dev
ARG APPLICATION_NAME=dev-moais-todo
ARG SERVER_PORT=8090
ARG RUN_HOSTNAME=dev-moais-todo
ARG RUN_HOSTPORT=8090

LABEL version=${IMG_VER}

# Set profile, pass ENV as argument when run
# ex. docker run --env ENV=local
ENV USE_PROFILE ${PROFILE}
ENV APPLICATION_NAME ${APPLICATION_NAME}
ENV SERVER_PORT ${SERVER_PORT}
ENV RUN_HOSTNAME ${RUN_HOSTNAME}
ENV RUN_HOSTPORT ${RUN_HOSTPORT}

COPY --from=build /app/target/*.jar app.jar

ENTRYPOINT [ "java", "-Dspring.profiles.active=${USE_PROFILE}", "-Djava.awt.headless=true", \
           "-jar", "/app.jar" ]
