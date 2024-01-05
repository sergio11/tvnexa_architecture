package com.dreamsoftware.api.data.respository.di

import com.dreamsoftware.api.domain.repository.*
import com.dreamsoftware.api.data.respository.impl.CategoryRepositoryImpl
import com.dreamsoftware.api.data.respository.impl.ChannelRepositoryImpl
import com.dreamsoftware.api.data.respository.impl.CountryRepositoryImpl
import com.dreamsoftware.api.data.respository.impl.EpgChannelProgrammeRepositoryImpl
import com.dreamsoftware.api.data.respository.impl.RegionRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {
    single<ICategoryRepository> { CategoryRepositoryImpl(get()) }
    single<ICountryRepository> { CountryRepositoryImpl(get()) }
    single<IRegionRepository> {  RegionRepositoryImpl(get()) }
    single<IChannelRepository> { ChannelRepositoryImpl(get(), get()) }
    single<IEpgChannelProgrammeRepository> { EpgChannelProgrammeRepositoryImpl(get()) }
}