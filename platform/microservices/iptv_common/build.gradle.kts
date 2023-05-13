val kotlin_version: String by project
val logback_version: String by project
val hoplite_core_version: String by project
val koin_version: String by project
val commons_net_version: String by project
val commons_io_version: String by project

plugins {
    kotlin("jvm") version "1.8.21"
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

group = "com.dreamsoftware"
version = "0.0.1"

tasks.compileKotlin {
    kotlinOptions.jvmTarget = "11"
}

repositories {
    mavenCentral()
}

dependencies {

    // Koin
    api("io.insert-koin:koin-ktor:$koin_version")
    api("io.insert-koin:koin-logger-slf4j:$koin_version")

    // A boilerplate-free Kotlin config library for loading configuration files as data classes
    api("com.sksamuel.hoplite:hoplite-core:$hoplite_core_version")
    api("com.sksamuel.hoplite:hoplite-yaml:$hoplite_core_version")

    // https://mvnrepository.com/artifact/commons-net/commons-net
    implementation("commons-net:commons-net:$commons_net_version")
    implementation("commons-io:commons-io:$commons_io_version")

    implementation("ch.qos.logback:logback-classic:$logback_version")
}