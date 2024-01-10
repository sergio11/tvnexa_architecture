package com.dreamsoftware.api

import com.dreamsoftware.api.rest.plugins.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main(args: Array<String>): Unit = EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    configureKoin(environment)
    configureSerialization()
    configureSecurity()
    configureMonitoring()
    configureHTTP()
    configureRouting()
    configureStatusPages()
}
