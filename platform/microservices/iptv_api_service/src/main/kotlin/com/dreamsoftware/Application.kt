package com.dreamsoftware

import com.dreamsoftware.plugins.configureHTTP
import com.dreamsoftware.plugins.configureMonitoring
import com.dreamsoftware.plugins.configureRouting
import com.dreamsoftware.plugins.configureSecurity
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main(args: Array<String>): Unit = EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    configureSecurity()
    configureMonitoring()
    configureHTTP()
    configureRouting()
}
