package com.dreamsoftware.api.di

import com.dreamsoftware.api.data.di.dataModule
import com.dreamsoftware.api.domain.di.serviceModule
import com.dreamsoftware.di.configModule
import org.koin.dsl.module

val appModule = module {
    includes(configModule, serviceModule, dataModule)
}