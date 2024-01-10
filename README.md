# TVNexa: 🌐 Bringing the World to Your Screen! 📺

<img width="300px" align="left" src="./doc/logo.PNG" />

TVNexa 🌐 is an innovative online television platform that allows you to explore a diverse universe of television channels from around the world. With access to a wide range of IPTVs, TVNexa offers you the freedom to tune in and enjoy content from countries across the globe. Immerse yourself in a unique streaming experience where you can discover exciting programs and diverse cultures, all from the comfort of your device. TVNexa: Your gateway to the world of IPTVs and global television. 📺✨

<p align="center">
  <img src="https://img.shields.io/badge/Kotlin-B125EA?&style=for-the-badge&logo=kotlin&logoColor=white" />
  <img src="https://img.shields.io/badge/Docker-2CA5E0?style=for-the-badge&logo=docker&logoColor=white" />
  <img src="https://img.shields.io/badge/MariaDB-003545?style=for-the-badge&logo=mariadb&logoColor=white" />
  <img src="https://img.shields.io/badge/redis-%23DD0031.svg?&style=for-the-badge&logo=redis&logoColor=white" />
  <img src="https://img.shields.io/badge/gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white" />
  <img src="https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=JSON%20web%20tokens&logoColor=white" />
  <img src="https://img.shields.io/badge/IntelliJ_IDEA-000000.svg?style=for-the-badge&logo=intellij-idea&logoColor=white" />
</p>


## ✨ Features

* **📺 Extensive Channel Library:** Explore an extensive collection of television channels from diverse countries and regions, offering an array of entertainment options.
* **🌟 Diverse Content:** Immerse yourself in a rich array of programs encompassing various languages, genres, and categories, catering to diverse audience preferences.
* **📋 Integration of Guide Information:** TVNexa seamlessly integrates detailed program information from electronic program guides (EPG) for thousands of TV channels, ensuring an enriched viewing experience.
* **💾 Efficient Data Storage and Retrieval:**
  * **⏰ Quartz Jobs:** Utilizes Quartz Jobs to seamlessly integrate and manage data retrieval processes, ensuring timely and accurate information updates.
  * **🔒 MariaDB Galera with Jetbrains Exposed:** Stores integrated data efficiently in a secure MariaDB Galera cluster using Jetbrains Exposed for robust data handling and management.
  * **🚀 Optimized API with Ktor and Redis Cluster:** Develops a highly optimized API utilizing the Ktor framework for rapid and efficient data retrieval, supported by Redis Cluster as a caching system to enhance performance.


<p align="center">
  <img width="350" height="auto" src="./doc/images/ktor_picture.png" />
  <img width="350" height="auto" src="./doc/images/redis_picture.jpg" />
</p>

## 🔄 Integration Process

TVNexa integrates comprehensive electronic program guide (EPG) data seamlessly into its platform. This integration process is handled by Jobs managed by Quartz, a robust job scheduling framework known for its reliability and scalability. These Quartz Jobs efficiently retrieve and store information in a MariaDB Galera cluster utilizing Jetbrains Exposed for streamlined data management.

Additionally, TVNexa has developed an API powered by the Ktor framework, ensuring high-performance and rapid access to stored information. This API utilizes Redis Cluster as a caching system, enabling lightning-fast data retrieval from the MariaDB Galera storage.

<p align="center">
  <img width="350" height="auto" src="./doc/images/quartz_picture.jpg" />
</p>

## 📊 Data Ingestion in TVNexa

The data ingestion process in TVNexa is a fundamental component designed to keep the platform updated and enriched with relevant information from various external sources. This operation is structured into a series of Jobs configured to fulfill specific responsibilities, ensuring the correct collection, integration, and storage of data.

### 🏗️ Ingestion Architecture

#### 🎯 Definition of Specialized Jobs

Each Job within the ingestion process has a clearly defined responsibility and sets logical constraints to ensure information coherence. For instance, the Job responsible for channel ingestion requires the successful execution of other specific Jobs.

This guarantees that essential information, like languages, categories, and subdivisions, is previously stored before channel ingestion, ensuring data integrity and coherence.

#### ⏰ Execution Regularity

Ingestion Jobs are configured to run at different time intervals based on the criticality and update frequency of data from external endpoints. This strategy ensures that the platform's information stays up-to-date efficiently and timely.

## 🗄️ Storage Environment

### Usage of MariaDB Galera Clusters

The TVNexa platform features two distinct storage environments:

* **Read Cluster:** Utilized by the information reading component, primarily via the Ktor API. This environment allows access to data in read-only mode to provide information to end-users quickly and efficiently.

* **Write Cluster:** Employed by the ingestion component to store data from the Jobs. This part of the system handles write operations to update and enrich the database with the most recent information obtained from external endpoints.

<p align="center">
  <img width="350" height="auto" src="./doc/images/mariadb_galera_picture.png" />
</p>

## 🚀 Benefits of the Ingestion Process

* **Maintaining Updated Data:** Ensures that the information provided to users is current, accurate, and comprehensive.
* **Data Integrity:** Guarantees coherence and quality of stored data by setting dependencies between Jobs and verifying prerequisites before critical information ingestion.
* **Optimization of User Experience:** Allows users to access updated and relevant information through the Ktor API with fast and efficient response times.

The ingestion process in TVNexa is meticulously configured to ensure continuous information updates and provide an optimal and enriching user experience.

## 🛠️ Technical Stack

* **Quartz Jobs:** Framework renowned for its reliability and scalability in managing job scheduling processes.
* **MariaDB Galera with Jetbrains Exposed:** Utilizes Jetbrains Exposed, an SQL library, to manage data storage and retrieval within a MariaDB Galera cluster.
* **Ktor Framework:** Empowers the highly optimized API for rapid and efficient data retrieval. 💡
* **Redis Cluster:** Implements Redis Cluster as a caching system for optimizing data retrieval through the API. 🔄

## 📊 Data Sources and APIs

TVNexa integrates data from various sources to provide a comprehensive television viewing experience. The platform accesses and integrates detailed information from:

* **📺 Channels:** Access detailed channel information from IPTV sources.
* **📡 Streams:** Retrieve streaming URLs for channels and additional metadata.
* **📋 Guides:** Obtain guides for channels, including site domain, site ID, site name, and guide language.
* **🗂️ Categories, Languages, Countries, Subdivisions, Regions:** Access additional datasets for categories, languages, countries, subdivisions, and regions.

For more information on the data sources, refer to the [GitHub repository](https://github.com/iptv-org/database).

## Running Applications as Docker containers.

### Rake Tasks

The available tasks are detailed below (rake --task)


| Task                                       | Description                                                                                                                                                              |
|-------------------------------------------- |--------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `tvnexa:login`                             | Authentication with existing credentials using Docker.                                                                                                                     |
| `tvnexa:cleaning_environment_task`          | Environment cleaning: removes Docker images and volumes.                                                                                                                   |
| `tvnexa:status`                            | Shows the status of containers using `docker-compose ps`.                                                                                                                  |
| `tvnexa:deploy`                            | Deploys platform containers and launches all services and daemons necessary for proper functionality.                                                                      |
| `tvnexa:undeploy`                          | Undeploys platform services using `docker-compose down -v`.                                                                                                                |
| `tvnexa:check_docker_task`                 | Checks Docker and Docker Compose availability in the system PATH, showing their versions.                                                                                   |
| `tvnexa:galera:check_deployment_file`      | Checks the existence of the MariaDB Galera Cluster deployment file.                                                                                                        |
| `tvnexa:galera:start`                      | Starts MariaDB Galera Cluster and HAProxy containers.                                                                                                                       |
| `tvnexa:galera:stop`                       | Stops MariaDB Galera Cluster and HAProxy containers.                                                                                                                        |
| `tvnexa:redis:check_deployment_file`       | Checks the existence of the Redis Cluster deployment file.                                                                                                                  |
| `tvnexa:redis:start`                       | Starts and configures Redis Cluster containers.                                                                                                                              |
| `tvnexa:redis:stop`                        | Stops Redis Cluster containers.                                                                                                                                             |
| `tvnexa:platform:check_deployment_file`    | Checks the existence of the platform deployment file.                                                                                                                        |
| `tvnexa:platform:start`                    | Starts platform Hotspot JVM containers.                                                                                                                                     |
| `tvnexa:platform:stop`                     | Stops platform Hotspot JVM containers.                                                                                                                                       |
| `tvnexa:platform:build_image`              | Builds Docker images based on Hotspot JVM for specific microservices, uploads them to Docker Hub, and shows the list of available images.                                 |

To start the platform make sure you have Ruby installed, go to the root directory of the project and run the rake deploy task, this task will carry out a series of preliminary checks, discard images and volumes that are no longer necessary and also proceed to download all the images and the initialization of the containers.

![picture](doc/images/picture_1.PNG)
![picture](doc/images/picture_2.PNG)
![picture](doc/images/picture_3.PNG)
![picture](doc/images/picture_4.PNG)
