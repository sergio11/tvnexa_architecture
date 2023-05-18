package com.dreamsoftware

import com.dreamsoftware.data.database.core.IDatabaseFactory
import com.dreamsoftware.di.appModule
import com.dreamsoftware.plugins.configureRouting
import com.dreamsoftware.tasks.IngestLanguagesJob
import com.dreamsoftware.tasks.core.manager.IJobSchedulerManager
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.koin.ktor.ext.getKoin
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    install(Koin) {
        slf4jLogger()
        modules(appModule)
    }
    configureRouting()
    doOnStartup()
}

fun Application.doOnStartup() {
    with(getKoin()) {
        get<IDatabaseFactory>().connectAndMigrate()
        with(get<IJobSchedulerManager>()) {
            start()
            scheduleJob(IngestLanguagesJob)
        }
    }
}
