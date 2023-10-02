package com.dreamsoftware.api.di

import com.dreamsoftware.api.services.*
import com.dreamsoftware.api.services.impl.*
import com.dreamsoftware.core.getMapper
import org.koin.dsl.module

val serviceModule = module {
    single<ICategoryService> { CategoryServiceImpl(get(), getMapper()) }
    single<ICountryService> { CountryServiceImpl(get(), getMapper()) }
    single<IRegionService> {  RegionServiceImpl(get(), getMapper()) }
    single<IChannelService> { ChannelServiceImpl(get(), getMapper())}
    single<IEpgChannelProgrammeService> { EpgChannelProgrammeServiceImpl(get(), get(), getMapper()) }
}