package com.dreamsoftware.api.di

import com.dreamsoftware.api.services.ICategoryService
import com.dreamsoftware.api.services.ICountryService
import com.dreamsoftware.api.services.impl.CategoryServiceImpl
import com.dreamsoftware.api.services.impl.CountryServiceImpl
import com.dreamsoftware.core.getMapper
import org.koin.dsl.module

val serviceModule = module {
    single<ICategoryService> { CategoryServiceImpl(get(), getMapper()) }
    single<ICountryService> { CountryServiceImpl(get(), getMapper()) }
}