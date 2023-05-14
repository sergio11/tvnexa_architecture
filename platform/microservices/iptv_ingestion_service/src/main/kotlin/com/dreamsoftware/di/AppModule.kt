package com.dreamsoftware.di

import com.dreamsoftware.data.iptvorg.di.dataSources
import org.koin.dsl.module

val appModule = module {
    includes(jobsModule, dataSources)
}