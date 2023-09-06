package com.dreamsoftware.api.di

import org.koin.dsl.module

val appModule = module {
    includes(serviceModule, dataModule)
}