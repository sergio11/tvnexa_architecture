package com.dreamsoftware.di

import com.dreamsoftware.core.isDevelopmentMode
import com.dreamsoftware.data.ftp.datasource.IConfigFtpDataSource
import com.dreamsoftware.data.ftp.di.ftpModule
import com.dreamsoftware.model.AppConfig
import com.sksamuel.hoplite.ConfigLoaderBuilder
import com.sksamuel.hoplite.addResourceOrFileSource
import kotlinx.coroutines.runBlocking
import org.koin.dsl.module

val configModule = module {
    includes(ftpModule)
    single<AppConfig> {
        runBlocking {
            ConfigLoaderBuilder.default()
                .addResourceOrFileSource(if(isDevelopmentMode) {
                    "/application-dev.yml"
                } else {
                    get<IConfigFtpDataSource>().getConfig()
                })
                .strict()
                .build()
                .loadConfigOrThrow()
        }
    }
    single { get<AppConfig>().dbConfig }
    single { get<AppConfig>().iptvOrgConfig }
}