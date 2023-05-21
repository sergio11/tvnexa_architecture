package com.dreamsoftware.data.iptvorg.di

import com.dreamsoftware.data.iptvorg.datasource.IptvOrgNetworkDataSource
import com.dreamsoftware.data.iptvorg.datasource.impl.IptvOrgNetworkDataSourceImpl
import com.dreamsoftware.data.iptvorg.model.*
import com.dreamsoftware.model.IptvOrgConfig
import io.ktor.util.reflect.*
import org.koin.core.qualifier.named
import org.koin.dsl.module

const val LANGUAGES_NETWORK_DATA_SOURCE = "languagesNetworkDataSource"
const val CATEGORIES_NETWORK_DATA_SOURCE = "categoriesNetworkDataSource"
const val COUNTRIES_NETWORK_DATA_SOURCE = "countriesNetworkDataSource"
const val SUBDIVISIONS_NETWORK_DATA_SOURCE = "subdivisionsNetworkDataSource"
const val REGIONS_NETWORK_DATA_SOURCE = "regionsNetworkDataSource"
const val CHANNELS_NETWORK_DATA_SOURCE = "channelsNetworkDataSource"

val networkDataSources = module {
    includes(networkModule)

    factory(named("languagesEndpoint")) { get<IptvOrgConfig>().languagesEndpoint }
    factory(named("categoriesEndpoint")) { get<IptvOrgConfig>().categoriesEndpoint }
    factory(named("countriesEndpoint")) { get<IptvOrgConfig>().countriesEndpoint }
    factory(named("subdivisionsEndpoint")) { get<IptvOrgConfig>().subdivisionsEndpoint }
    factory(named("regionsEndpoint")) { get<IptvOrgConfig>().regionsEndpoint }
    factory(named("channelsEndpoint")) { get<IptvOrgConfig>().channelsEndpoint }

    factory<IptvOrgNetworkDataSource<LanguageDTO>>(named(LANGUAGES_NETWORK_DATA_SOURCE)) { IptvOrgNetworkDataSourceImpl(get(), get(named("languagesEndpoint")), typeInfo<List<LanguageDTO>>()) }
    factory<IptvOrgNetworkDataSource<CategoryDTO>>(named(CATEGORIES_NETWORK_DATA_SOURCE)) { IptvOrgNetworkDataSourceImpl(get(), get(named("categoriesEndpoint")), typeInfo<List<CategoryDTO>>()) }
    factory<IptvOrgNetworkDataSource<CountryDTO>>(named(COUNTRIES_NETWORK_DATA_SOURCE)) { IptvOrgNetworkDataSourceImpl(get(), get(named("countriesEndpoint")), typeInfo<List<CountryDTO>>()) }
    factory<IptvOrgNetworkDataSource<SubdivisionDTO>>(named(SUBDIVISIONS_NETWORK_DATA_SOURCE)) { IptvOrgNetworkDataSourceImpl(get(), get(named("subdivisionsEndpoint")), typeInfo<List<SubdivisionDTO>>()) }
    factory<IptvOrgNetworkDataSource<RegionDTO>>(named(REGIONS_NETWORK_DATA_SOURCE)) { IptvOrgNetworkDataSourceImpl(get(), get(named("regionsEndpoint")), typeInfo<List<RegionDTO>>()) }
    factory<IptvOrgNetworkDataSource<ChannelDTO>>(named(CHANNELS_NETWORK_DATA_SOURCE)) { IptvOrgNetworkDataSourceImpl(get(), get(named("channelsEndpoint")), typeInfo<List<ChannelDTO>>()) }
}