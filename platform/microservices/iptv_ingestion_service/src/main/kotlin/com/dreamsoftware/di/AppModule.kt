package com.dreamsoftware.di

import com.dreamsoftware.data.database.di.databaseDataSourcesModule
import com.dreamsoftware.data.iptvorg.di.networkDataSources
import org.koin.dsl.module

val appModule = module {
    includes(networkDataSources, databaseDataSourcesModule, jobsModule)
}