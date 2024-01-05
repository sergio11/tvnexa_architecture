package com.dreamsoftware.api.data.di

import com.dreamsoftware.api.data.cache.di.cacheModule
import com.dreamsoftware.api.data.respository.di.repositoryModule
import com.dreamsoftware.api.rest.di.dtoMappersModule
import com.dreamsoftware.data.database.di.databaseDataSourcesModule
import org.koin.dsl.module

val dataModule = module {
    includes(repositoryModule, databaseDataSourcesModule, dtoMappersModule, cacheModule)
}