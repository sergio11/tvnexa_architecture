package com.dreamsoftware.api.di

import com.dreamsoftware.api.repository.*
import com.dreamsoftware.api.repository.impl.*
import org.koin.dsl.module

val repositoryModule = module {
    single<ICategoryRepository> { CategoryRepositoryImpl(get()) }
    single<ICountryRepository> { CountryRepositoryImpl(get()) }
    single<IRegionRepository> {  RegionRepositoryImpl(get()) }
    single<IChannelRepository> { ChannelRepositoryImpl(get()) }
    single<IEpgChannelProgrammeRepository> { EpgChannelProgrammeRepositoryImpl(get()) }
}