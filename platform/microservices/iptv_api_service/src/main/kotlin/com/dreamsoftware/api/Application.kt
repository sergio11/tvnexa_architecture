package com.dreamsoftware.api

import com.dreamsoftware.api.di.appModule
import com.dreamsoftware.api.plugins.configureHTTP
import com.dreamsoftware.api.plugins.configureMonitoring
import com.dreamsoftware.api.plugins.configureRouting
import com.dreamsoftware.api.plugins.configureSecurity
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger
import org.slf4j.LoggerFactory

private val log = LoggerFactory.getLogger(Application::class.java)

fun main(args: Array<String>): Unit = EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    install(Koin) {
        slf4jLogger()
        modules(appModule)
    }
    configureSecurity()
    configureMonitoring()
    configureHTTP()
    configureRouting()
}
