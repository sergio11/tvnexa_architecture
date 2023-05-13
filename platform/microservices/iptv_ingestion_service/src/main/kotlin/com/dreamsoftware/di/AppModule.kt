package com.dreamsoftware.di

import org.koin.dsl.module

val appModule = module {
    includes(jobsModule)
}