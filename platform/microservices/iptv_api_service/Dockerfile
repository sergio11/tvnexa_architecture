# ---- Building phase ----
FROM openjdk:11-jdk-slim AS build
RUN apt-get update
RUN apt-get install -y dos2unix
WORKDIR /src
COPY . /src
RUN dos2unix gradlew
RUN dos2unix resources/wait-for-it.sh
RUN chmod +x resources/wait-for-it.sh
RUN bash gradlew buildFatJar --no-daemon --debug
# ---- Release ----
FROM adoptopenjdk/openjdk11:jre-11.0.9.1_1-alpine AS release
RUN mkdir -p /opt/app
COPY --from=build /src/build/libs/iptv_api_service.jar /opt/app/iptv_api_service.jar
COPY --from=build /src/resources/wait-for-it.sh /opt/app/wait-for-it.sh
WORKDIR /opt/app
EXPOSE 8080
CMD ["sh", "-c", "echo 'Launching IPTV service' -- java -jar iptv_api_service.jar"]