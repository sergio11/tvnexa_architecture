package com.dreamsoftware.data.api.di

import com.dreamsoftware.data.database.di.databaseDataSourcesModule
import com.dreamsoftware.data.iptvorg.di.networkDataSources
import org.koin.dsl.module

val dataModule = module {
    // Include sub-modules related to data management
    includes(
        networkDataSources,             // Sub-module for network data sources (IPTV)
        databaseDataSourcesModule,      // Sub-module for database data sources
        dataMappersModule               // Sub-module for data mappers
    )
}