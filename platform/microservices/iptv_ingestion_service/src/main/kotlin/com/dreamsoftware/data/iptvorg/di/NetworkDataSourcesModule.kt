package com.dreamsoftware.data.iptvorg.di

import com.dreamsoftware.core.getMapper
import com.dreamsoftware.data.iptvorg.datasource.IptvOrgNetworkDataSource
import com.dreamsoftware.data.iptvorg.datasource.impl.IptvOrgNetworkDataSourceImpl
import com.dreamsoftware.data.iptvorg.model.LanguageDTO
import com.dreamsoftware.model.IptvOrgConfig
import com.dreamsoftware.model.Language
import io.ktor.util.reflect.*
import org.koin.core.qualifier.named
import org.koin.dsl.module

val networkDataSources = module {
    includes(networkModule, networkMappers)

    factory(named("languagesEndpoint")) { get<IptvOrgConfig>().languagesEndpoint }

    factory<IptvOrgNetworkDataSource<Language>> { IptvOrgNetworkDataSourceImpl<LanguageDTO, Language>(get(), getMapper(), get(named("languagesEndpoint")), typeInfo<List<LanguageDTO>>()) }

}