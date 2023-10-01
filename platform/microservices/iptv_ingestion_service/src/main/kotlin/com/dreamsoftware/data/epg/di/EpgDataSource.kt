package com.dreamsoftware.data.epg.di

import com.dreamsoftware.data.epg.datasource.IEpgGrabbingDataSource
import com.dreamsoftware.data.epg.datasource.impl.EpgGrabbingDataSourceImpl
import org.koin.dsl.module

/**
 * Koin module for configuring and providing dependencies related to Electronic Program Guide (EPG) data sources.
 */
val epgDataSources = module {
    // Include the parserModule for additional dependencies if needed
    includes(parserModule)

    // Define a single instance of IEpgGrabbingDataSource and provide an instance of EpgGrabbingDataSourceImpl
    single<IEpgGrabbingDataSource> {
        EpgGrabbingDataSourceImpl(get(), get())
    }
}