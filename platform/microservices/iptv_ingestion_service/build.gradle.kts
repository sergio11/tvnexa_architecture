val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val quartz_version: String by project
val hoplite_core_version: String by project

plugins {
    kotlin("jvm") version "1.8.21"
    id("io.ktor.plugin") version "2.3.0"
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

group = "com.dreamsoftware"
version = "0.0.1"
application {
    mainClass.set("io.ktor.server.netty.EngineMain")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

ktor {
    fatJar {
        archiveFileName.set("iptv_ingestion_service.jar")
    }
}

tasks.compileKotlin {
    kotlinOptions.jvmTarget = "11"
}

repositories {
    mavenCentral()
}

tasks.withType<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar> {
    mergeServiceFiles {
        setPath("META-INF/services/org.flywaydb.core.extensibility.Plugin")
    }
}

dependencies {

    implementation("io.ktor:ktor-server-core-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-netty-jvm:$ktor_version")
    implementation("io.ktor:ktor-client-core:$ktor_version")
    implementation("io.ktor:ktor-client-cio:$ktor_version")
    implementation("io.ktor:ktor-client-logging:$ktor_version")
    implementation("io.ktor:ktor-client-content-negotiation:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")

    // Import common module
    implementation(project(":iptv_common"))

    // Quartz, for CRON
    implementation("org.quartz-scheduler:quartz:$quartz_version")

    testImplementation("io.ktor:ktor-server-tests-jvm:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
}