package com.dreamsoftware.api.di

import com.dreamsoftware.api.repository.ICategoryRepository
import com.dreamsoftware.api.repository.ICountryRepository
import com.dreamsoftware.api.repository.IRegionRepository
import com.dreamsoftware.api.repository.impl.CategoryRepositoryImpl
import com.dreamsoftware.api.repository.impl.CountryRepositoryImpl
import com.dreamsoftware.api.repository.impl.RegionRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {
    single<ICategoryRepository> { CategoryRepositoryImpl(get()) }
    single<ICountryRepository> { CountryRepositoryImpl(get()) }
    single<IRegionRepository> {  RegionRepositoryImpl(get()) }
}