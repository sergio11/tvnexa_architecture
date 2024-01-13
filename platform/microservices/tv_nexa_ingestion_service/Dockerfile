# ---- Building phase ----
FROM gradle:7-jdk17  AS build
RUN apt-get update
RUN apt-get install -y dos2unix
RUN apt-get install -y wget
COPY --chown=gradle:gradle tv_nexa_common /home/gradle/tv_nexa_common
COPY --chown=gradle:gradle tv_nexa_ingestion_service /home/gradle/tv_nexa_ingestion_service
WORKDIR /home/gradle/tv_nexa_ingestion_service
RUN wget -q -O ./iptv_org_master.zip https://github.com/iptv-org/epg/archive/refs/heads/master.zip && unzip ./iptv_org_master.zip && rm ./iptv_org_master.zip
RUN ls -al ./epg-master/sites
RUN dos2unix gradlew
RUN dos2unix resources/wait-for-it.sh
RUN chmod +x resources/wait-for-it.sh
RUN gradle buildFatJar --no-daemon
# ---- Release ----
FROM openjdk:17-jdk-slim-buster AS release
RUN mkdir -p /opt/app
RUN mkdir -p /opt/app/iptv_org
RUN mkdir -p /opt/app/iptv_org/guides
RUN apt-get update && apt-get install -y curl
RUN apt-get install -y netcat && which nc
RUN curl -sL https://deb.nodesource.com/setup_18.x | bash -
RUN apt-get install -y nodejs
RUN npm install -g epg-grabber
RUN npm install -g cheerio
RUN npm install -g luxon
COPY --from=build /home/gradle/tv_nexa_ingestion_service/build/libs/tv_nexa_ingestion_service.jar /opt/app/tv_nexa_ingestion_service.jar
COPY --from=build /home/gradle/tv_nexa_ingestion_service/epg-master/sites /opt/app/iptv_org/sites
COPY --from=build /home/gradle/tv_nexa_ingestion_service/resources/wait-for-it.sh /opt/app/wait-for-it.sh
WORKDIR /opt/app
EXPOSE 8080
CMD ["sh", "-c", "echo 'waiting for 300 seconds for FTP Repository to be accessable before starting application' && ./wait-for-it.sh -t 300 ftp_repository:21 -- java -jar tv_nexa_ingestion_service.jar"]