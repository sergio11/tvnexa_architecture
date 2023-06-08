# ---- Building phase ----
FROM gradle:7-jdk17  AS build
RUN apt-get update
RUN apt-get install -y dos2unix
RUN apt-get install -y wget
COPY --chown=gradle:gradle iptv_common /home/gradle/iptv_common
COPY --chown=gradle:gradle iptv_ingestion_service /home/gradle/iptv_ingestion_service
WORKDIR /home/gradle/iptv_ingestion_service
RUN wget -q -O ./iptv_org_master.zip https://github.com/iptv-org/epg/archive/refs/heads/master.zip && unzip ./iptv_org_master.zip && rm ./iptv_org_master.zip
RUN ls -al ./epg-master/sites
RUN dos2unix gradlew
RUN dos2unix resources/wait-for-it.sh
RUN dos2unix resources/raw_githubusercontent_com_nvm_sh_nvm_v0_39_3_install.sh
RUN chmod +x resources/wait-for-it.sh
RUN chmod +x resources/raw_githubusercontent_com_nvm_sh_nvm_v0_39_3_install.sh
RUN gradle buildFatJar --no-daemon
# ---- Release ----
FROM openjdk:17-jdk-slim-buster AS release
RUN mkdir -p /opt/app
RUN apt-get update && apt-get install -y wget
COPY --from=build /home/gradle/iptv_ingestion_service/build/libs/iptv_ingestion_service.jar /opt/app/iptv_ingestion_service.jar
COPY --from=build /home/gradle/iptv_ingestion_service/epg-master/sites /opt/app/sites
COPY --from=build /home/gradle/iptv_ingestion_service/resources/wait-for-it.sh /opt/app/wait-for-it.sh
COPY --from=build /home/gradle/iptv_ingestion_service/resources/raw_githubusercontent_com_nvm_sh_nvm_v0_39_3_install.sh /opt/app/raw_githubusercontent_com_nvm_sh_nvm_v0_39_3_install.sh
WORKDIR /opt/app
RUN ./raw_githubusercontent_com_nvm_sh_nvm_v0_39_3_install.sh && . ~/.bashrc && nvm install v18.11.0
RUN ./raw_githubusercontent_com_nvm_sh_nvm_v0_39_3_install.sh && . ~/.bashrc && nvm install-latest-npm
RUN . ~/.bashrc && npm install -g epg-grabber
EXPOSE 8080
CMD ["sh", "-c", "echo 'waiting for 300 seconds for FTP Repository to be accessable before starting application' && ./wait-for-it.sh -t 300 ftp_repository:21 && echo 'waiting for 300 seconds for mariadb:3306 to be accessable before starting application' && ./wait-for-it.sh -t 300 mariadb:3306 -- java -jar iptv_ingestion_service.jar"]