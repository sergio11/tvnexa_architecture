package com.dreamsoftware

import com.dreamsoftware.plugins.configureHTTP
import com.dreamsoftware.plugins.configureMonitoring
import com.dreamsoftware.plugins.configureRouting
import com.dreamsoftware.plugins.configureSecurity
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureSecurity()
    configureMonitoring()
    configureHTTP()
    configureRouting()
}
