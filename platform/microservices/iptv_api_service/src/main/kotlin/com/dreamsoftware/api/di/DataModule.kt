package com.dreamsoftware.api.di

import com.dreamsoftware.data.database.di.databaseDataSourcesModule
import org.koin.dsl.module

val dataModule = module {
    includes(repositoryModule, databaseDataSourcesModule, dataMappersModule)
}