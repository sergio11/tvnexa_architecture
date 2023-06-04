package com.dreamsoftware.di

import com.dreamsoftware.data.api.di.dataModule
import com.dreamsoftware.data.epg.di.epgDataSources
import org.koin.dsl.module

val appModule = module {
    includes(dataModule, jobsModule, epgDataSources)
}