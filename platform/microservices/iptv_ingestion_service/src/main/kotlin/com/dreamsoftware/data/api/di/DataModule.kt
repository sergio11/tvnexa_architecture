package com.dreamsoftware.data.api.di

import com.dreamsoftware.data.database.di.databaseDataSourcesModule
import com.dreamsoftware.data.iptvorg.di.networkDataSources
import org.koin.dsl.module

val dataModule = module {
    includes(networkDataSources, databaseDataSourcesModule, dataMappersModule)
}