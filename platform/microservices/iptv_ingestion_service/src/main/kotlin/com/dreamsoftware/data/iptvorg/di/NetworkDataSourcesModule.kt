package com.dreamsoftware.data.iptvorg.di

import com.dreamsoftware.data.iptvorg.datasource.IptvOrgNetworkDataSource
import com.dreamsoftware.data.iptvorg.datasource.impl.IptvOrgNetworkDataSourceImpl
import com.dreamsoftware.data.iptvorg.model.LanguageDTO
import com.dreamsoftware.model.IptvOrgConfig
import io.ktor.util.reflect.*
import org.koin.core.qualifier.named
import org.koin.dsl.module

val networkDataSources = module {
    includes(networkModule)

    factory(named("languagesEndpoint")) { get<IptvOrgConfig>().languagesEndpoint }
    factory(named("categoriesEndpoint")) { get<IptvOrgConfig>().categoriesEndpoint }
    factory(named("countriesEndpoint")) { get<IptvOrgConfig>().countriesEndpoint }
    factory(named("subdivisionsEndpoint")) { get<IptvOrgConfig>().subdivisionsEndpoint }
    factory(named("regionsEndpoint")) { get<IptvOrgConfig>().regionsEndpoint }

    factory<IptvOrgNetworkDataSource<LanguageDTO>> { IptvOrgNetworkDataSourceImpl(get(), get(named("languagesEndpoint")), typeInfo<List<LanguageDTO>>()) }

}