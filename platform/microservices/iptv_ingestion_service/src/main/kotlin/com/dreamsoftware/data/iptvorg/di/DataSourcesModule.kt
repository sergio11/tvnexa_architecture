package com.dreamsoftware.data.iptvorg.di

import com.dreamsoftware.data.iptvorg.datasource.category.ICategoryNetworkDataSource
import com.dreamsoftware.data.iptvorg.datasource.category.impl.CategoryNetworkDataSourceImpl
import com.dreamsoftware.data.iptvorg.datasource.country.ICountryNetworkDataSource
import com.dreamsoftware.data.iptvorg.datasource.country.impl.CountryNetworkDataSourceImpl
import com.dreamsoftware.data.iptvorg.datasource.language.ILanguageNetworkDataSource
import com.dreamsoftware.data.iptvorg.datasource.language.impl.LanguageNetworkDataSourceImpl
import com.dreamsoftware.data.iptvorg.datasource.subdivision.ISubdivisionNetworkDataSource
import com.dreamsoftware.data.iptvorg.datasource.subdivision.impl.SubdivisionNetworkDataSourceImpl
import org.koin.dsl.module

val dataSources = module {
    includes(networkModule)
    factory<ICountryNetworkDataSource> { CountryNetworkDataSourceImpl(get(), get()) }
    factory<ICategoryNetworkDataSource> { CategoryNetworkDataSourceImpl(get(), get()) }
    factory<ILanguageNetworkDataSource> { LanguageNetworkDataSourceImpl(get(), get()) }
    factory<ISubdivisionNetworkDataSource> { SubdivisionNetworkDataSourceImpl(get(), get()) }
}