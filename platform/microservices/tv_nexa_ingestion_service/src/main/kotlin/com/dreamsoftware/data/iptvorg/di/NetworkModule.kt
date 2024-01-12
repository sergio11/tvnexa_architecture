package com.dreamsoftware.data.iptvorg.di

import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.koin.dsl.module

// Import necessary modules and classes

// Define a Koin module for network-related configurations
val networkModule = module {
    single {
        HttpClient {
            // Install ContentNegotiation plugin to handle JSON content
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }

            // Install Logging plugin to log network requests and responses
            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.HEADERS
            }
        }
    }
}