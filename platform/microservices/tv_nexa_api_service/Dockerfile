# ---- Building phase ----
FROM gradle:7-jdk17  AS build
RUN apt-get update
RUN apt-get install -y dos2unix
COPY --chown=gradle:gradle tv_nexa_common /home/gradle/tv_nexa_common
COPY --chown=gradle:gradle tv_nexa_api_service /home/gradle/tv_nexa_api_service
WORKDIR /home/gradle/tv_nexa_api_service
RUN dos2unix gradlew
RUN dos2unix resources/wait-for-it.sh
RUN chmod +x resources/wait-for-it.sh
RUN gradle buildFatJar --no-daemon
# ---- Release ----
FROM openjdk:17-jdk-slim-buster AS release
RUN apt-get update && apt-get install -y curl
RUN apt-get install -y netcat && which nc
EXPOSE 6969:6969
EXPOSE 6963:6963
RUN mkdir -p /opt/app
RUN mkdir -p /opt/app/certs
COPY --from=build /home/gradle/tv_nexa_api_service/build/libs/tv_nexa_api_service.jar /opt/app/tv_nexa_api_service.jar
COPY --from=build /home/gradle/tv_nexa_api_service/resources/wait-for-it.sh /opt/app/wait-for-it.sh
COPY --from=build /home/gradle/tv_nexa_api_service/resources/certs/* /opt/app/certs
WORKDIR /opt/app
CMD ["sh", "-c", "echo 'waiting for 300 seconds for FTP Repository to be accessable before starting application' && ./wait-for-it.sh -t 300 ftp_repository:21 -- java -jar tv_nexa_api_service.jar"]