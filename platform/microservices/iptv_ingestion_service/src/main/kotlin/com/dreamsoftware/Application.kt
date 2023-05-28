package com.dreamsoftware

import com.dreamsoftware.di.appModule
import com.dreamsoftware.jobs.*
import com.dreamsoftware.jobs.core.manager.IJobSchedulerManager
import com.dreamsoftware.plugins.configureRouting
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.koin.ktor.ext.getKoin
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger
import java.util.*

fun main(args: Array<String>): Unit = EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    System.setProperty("development.mode", developmentMode.toString())
    install(Koin) {
        slf4jLogger()
        modules(appModule)
    }
    configureRouting()
    doOnStartup()
}

fun Application.doOnStartup() {
    with(getKoin()) {
        with(get<IJobSchedulerManager>()) {
            scheduleJobsAndStart(listOf(
                LanguagesIngestionJob,
                CategoriesIngestionJob,
                CountriesIngestionJob,
                SubdivisionsIngestionJob,
                RegionsIngestionJob,
                ChannelsIngestionJob,
                ChannelStreamsIngestionJob,
                ChannelGuidesIngestionJob
            ))
        }
    }
}
