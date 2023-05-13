val kotlin_version: String by project
val logback_version: String by project
val hoplite_core_version: String by project

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

    // A boilerplate-free Kotlin config library for loading configuration files as data classes
    api("com.sksamuel.hoplite:hoplite-core:$hoplite_core_version")
    api("com.sksamuel.hoplite:hoplite-yaml:$hoplite_core_version")

    implementation("ch.qos.logback:logback-classic:$logback_version")
}