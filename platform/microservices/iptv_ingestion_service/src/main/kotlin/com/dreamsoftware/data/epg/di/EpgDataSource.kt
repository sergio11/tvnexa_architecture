package com.dreamsoftware.data.epg.di

import com.dreamsoftware.data.epg.datasource.IEpgGrabbingDataSource
import com.dreamsoftware.data.epg.datasource.impl.EpgGrabbingDataSourceImpl
import org.koin.dsl.module

val epgDataSources = module {
    includes(parserModule)
    single<IEpgGrabbingDataSource> {
        EpgGrabbingDataSourceImpl(get(), get())
    }
}