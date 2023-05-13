package com.dreamsoftware.data.api.di

import com.dreamsoftware.data.api.repository.ILanguageRepository
import com.dreamsoftware.data.api.repository.impl.LanguageRepositoryImpl
import com.dreamsoftware.data.database.di.databaseModule
import org.koin.dsl.module

val repositoryModule = module {
    includes(mapperModule, databaseModule)

    single<ILanguageRepository> { LanguageRepositoryImpl(get(), get()) }
}