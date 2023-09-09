package com.dreamsoftware.api.di

import com.dreamsoftware.api.services.ICategoryService
import com.dreamsoftware.api.services.IChannelService
import com.dreamsoftware.api.services.ICountryService
import com.dreamsoftware.api.services.IRegionService
import com.dreamsoftware.api.services.impl.CategoryServiceImpl
import com.dreamsoftware.api.services.impl.ChannelServiceImpl
import com.dreamsoftware.api.services.impl.CountryServiceImpl
import com.dreamsoftware.api.services.impl.RegionServiceImpl
import com.dreamsoftware.core.getMapper
import org.koin.dsl.module

val serviceModule = module {
    single<ICategoryService> { CategoryServiceImpl(get(), getMapper()) }
    single<ICountryService> { CountryServiceImpl(get(), getMapper()) }
    single<IRegionService> {  RegionServiceImpl(get(), getMapper()) }
    single<IChannelService> { ChannelServiceImpl(get(), getMapper())}
}