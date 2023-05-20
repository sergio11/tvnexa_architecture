package com.dreamsoftware.di

import com.dreamsoftware.data.api.di.dataModule
import org.koin.dsl.module

val appModule = module {
    includes(dataModule, jobsModule)
}